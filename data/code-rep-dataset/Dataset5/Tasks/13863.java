if(certFile.exists() && !certFile.delete()) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.http.proxy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.parser.HTMLParseException;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.exec.KeyToolUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterException;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * Thread to handle one client request. Gets the request from the client and
 * passes it on to the server, then sends the response back to the client.
 * Information about the request and response is stored so it can be used in a
 * JMeter test plan.
 *
 */
public class Proxy extends Thread {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final byte[] CRLF_BYTES = { 0x0d, 0x0a };
    private static final String CRLF_STRING = "\r\n";

    private static final String NEW_LINE = "\n"; // $NON-NLS-1$

    private static final String[] headersToRemove;

    // Allow list of headers to be overridden
    private static final String PROXY_HEADERS_REMOVE = "proxy.headers.remove"; // $NON-NLS-1$

    private static final String PROXY_HEADERS_REMOVE_DEFAULT = "If-Modified-Since,If-None-Match,Host"; // $NON-NLS-1$

    private static final String PROXY_HEADERS_REMOVE_SEPARATOR = ","; // $NON-NLS-1$

    // for ssl connection
    private static final String KEYSTORE_TYPE =
        JMeterUtils.getPropDefault("proxy.cert.type", "JKS"); // $NON-NLS-1$ $NON-NLS-2$

    private static final String KEYMANAGERFACTORY =
        JMeterUtils.getPropDefault("proxy.cert.factory", "SunX509"); // $NON-NLS-1$ $NON-NLS-2$

    private static final String SSLCONTEXT_PROTOCOL =
        JMeterUtils.getPropDefault("proxy.ssl.protocol", "SSLv3"); // $NON-NLS-1$ $NON-NLS-2$

    // HashMap to save ssl connection between Jmeter proxy and browser
    private static final HashMap<String, SSLSocketFactory> hashHost = new HashMap<String, SSLSocketFactory>();

    // Proxy configuration SSL
    private static final String CERT_DIRECTORY =
        JMeterUtils.getPropDefault("proxy.cert.directory", JMeterUtils.getJMeterBinDir()); // $NON-NLS-1$

    private static final String CERT_FILE_DEFAULT = "proxyserver.jks";// $NON-NLS-1$

    private static final String CERT_FILE =
        JMeterUtils.getPropDefault("proxy.cert.file", CERT_FILE_DEFAULT); // $NON-NLS-1$

    // The alias to be used if dynamic host names are not possible
    private static final String JMETER_SERVER_ALIAS = ":jmeter:"; // $NON-NLS-1$

    private static final int CERT_VALIDITY = JMeterUtils.getPropDefault("proxy.cert.validity", 7); // $NON-NLS-1$

    private static final String DEFAULT_PASSWORD = "password"; // $NON-NLS-1$

    private static final SamplerCreatorFactory factory = new SamplerCreatorFactory();

    // Keys for user preferences
    private static final String USER_PASSWORD_KEY = "proxy_cert_PASSWORD";

    private static final Preferences prefs = Preferences.userNodeForPackage(Proxy.class);
    // Note: Windows user preferences are stored relative to: HKEY_CURRENT_USER\Software\JavaSoft\Prefs

    // Use with SSL connection
    private OutputStream outStreamClient = null;

    static {
        String removeList = JMeterUtils.getPropDefault(PROXY_HEADERS_REMOVE,PROXY_HEADERS_REMOVE_DEFAULT);
        headersToRemove = JOrphanUtils.split(removeList,PROXY_HEADERS_REMOVE_SEPARATOR);
        log.info("Proxy will remove the headers: "+removeList);
    }

    /** Socket to client. */
    private Socket clientSocket = null;

    /** Target to receive the generated sampler. */
    private ProxyControl target;

    /** Whether or not to capture the HTTP headers. */
    private boolean captureHttpHeaders;

    /** Reference to Deamon's Map of url string to page character encoding of that page */
    private Map<String, String> pageEncodings;
    /** Reference to Deamon's Map of url string to character encoding for the form */
    private Map<String, String> formEncodings;

    private String port; // For identifying log messages

    /**
     * Default constructor - used by newInstance call in Daemon
     */
    public Proxy() {
        port = "";
    }

    /**
     * Configure the Proxy.
     * Intended to be called directly after construction.
     * Should not be called after it has been passed to a new thread,
     * otherwise the variables may not be published correctly.
     *
     * @param _clientSocket
     *            the socket connection to the client
     * @param _target
     *            the ProxyControl which will receive the generated sampler
     * @param _pageEncodings
     *            reference to the Map of Deamon, with mappings from page urls to encoding used
     * @param formEncodingsEncodings
     *            reference to the Map of Deamon, with mappings from form action urls to encoding used
     */
    void configure(Socket _clientSocket, ProxyControl _target, Map<String, String> _pageEncodings, Map<String, String> _formEncodings) {
        this.target = _target;
        this.clientSocket = _clientSocket;
        this.captureHttpHeaders = _target.getCaptureHttpHeaders();
        this.pageEncodings = _pageEncodings;
        this.formEncodings = _formEncodings;
        this.port = "["+ clientSocket.getPort() + "] ";
    }

    /**
     * Main processing method for the Proxy object
     */
    @Override
    public void run() {
        // Check which HTTPSampler class we should use
        String httpSamplerName = target.getSamplerTypeName();

        HttpRequestHdr request = new HttpRequestHdr(httpSamplerName);
        SampleResult result = null;
        HeaderManager headers = null;
        HTTPSamplerBase sampler = null;
        log.debug(port + "====================================================================");
        try {
            // Now, parse initial request (in case it is a CONNECT request)
            byte[] ba = request.parse(new BufferedInputStream(clientSocket.getInputStream()));
            if (ba.length == 0) {
                log.warn(port + "Empty request, ignored");
                throw new JMeterException(); // hack to skip processing
            }
            if (log.isDebugEnabled()) {
                log.debug(port + "Initial request: " + new String(ba));
            }
            outStreamClient = clientSocket.getOutputStream();

            if ((request.getMethod().startsWith(HTTPConstants.CONNECT)) && (outStreamClient != null)) {
                log.debug(port + "Method CONNECT => SSL");
                // write a OK reponse to browser, to engage SSL exchange
                outStreamClient.write(("HTTP/1.0 200 OK\r\n\r\n").getBytes(SampleResult.DEFAULT_HTTP_ENCODING)); // $NON-NLS-1$
                outStreamClient.flush();
               // With ssl request, url is host:port (without https:// or path)
                String[] param = request.getUrl().split(":");  // $NON-NLS-1$
                if (param.length == 2) {
                    log.debug(port + "Start to negotiate SSL connection, host: " + param[0]);
                    clientSocket = startSSL(clientSocket, param[0]);
                } else {
                    // Should not happen, but if it does we don't want to continue 
                    log.error("In SSL request, unable to find host and port in CONNECT request: " + request.getUrl());
                    throw new JMeterException(); // hack to skip processing
                }
                // Re-parse (now it's the http request over SSL)
                try {
                    ba = request.parse(new BufferedInputStream(clientSocket.getInputStream()));
                } catch (IOException ioe) { // most likely this is because of a certificate error
                    final String url = param.length>0 ?  " for '"+ param[0] +"'" : "";
                    log.warn(port + "Problem with SSL certificate"+url+"? Ensure browser is set to accept the JMeter proxy cert: " + ioe.getMessage());
                    // won't work: writeErrorToClient(HttpReplyHdr.formInternalError());
                    result = generateErrorResult(result, request, ioe, "\n**ensure browser is set to accept the JMeter proxy certificate**"); // Generate result (if nec.) and populate it
                    throw new JMeterException(); // hack to skip processing
                }
                if (log.isDebugEnabled()) {
                    log.debug(port + "Reparse: " + new String(ba));
                }
                if (ba.length == 0) {
                    log.warn(port + "Empty response to http over SSL. Probably waiting for user to authorize the certificate for " + request.getUrl());
                    throw new JMeterException(); // hack to skip processing
                }
            }

            SamplerCreator samplerCreator = factory.getSamplerCreator(request, pageEncodings, formEncodings);
            sampler = samplerCreator.createAndPopulateSampler(request, pageEncodings, formEncodings);

            /*
             * Create a Header Manager to ensure that the browsers headers are
             * captured and sent to the server
             */
            headers = request.getHeaderManager();
            sampler.setHeaderManager(headers);

            sampler.threadStarted(); // Needed for HTTPSampler2
            if (log.isDebugEnabled()) {
                log.debug(port + "Execute sample: " + sampler.getMethod() + " " + sampler.getUrl());
            }
            result = sampler.sample();

            // Find the page encoding and possibly encodings for forms in the page
            // in the response from the web server
            String pageEncoding = addPageEncoding(result);
            addFormEncodings(result, pageEncoding);

            writeToClient(result, new BufferedOutputStream(clientSocket.getOutputStream()));
            samplerCreator.postProcessSampler(sampler, result);
        } catch (JMeterException jme) {
            // ignored, already processed
        } catch (UnknownHostException uhe) {
            log.warn(port + "Server Not Found.", uhe);
            writeErrorToClient(HttpReplyHdr.formServerNotFound());
            result = generateErrorResult(result, request, uhe); // Generate result (if nec.) and populate it
        } catch (IllegalArgumentException e) {
            log.error(port + "Not implemented (probably used https)", e);
            writeErrorToClient(HttpReplyHdr.formNotImplemented("Probably used https instead of http. " +
                    "To record https requests, see " +
                    "<a href=\"http://jmeter.apache.org/usermanual/component_reference.html#HTTP_Proxy_Server\">HTTP Proxy Server documentation</a>"));
            result = generateErrorResult(result, request, e); // Generate result (if nec.) and populate it
        } catch (Exception e) {
            log.error(port + "Exception when processing sample", e);
            writeErrorToClient(HttpReplyHdr.formInternalError());
            result = generateErrorResult(result, request, e); // Generate result (if nec.) and populate it
        } finally {
            if (log.isDebugEnabled()) {
                if(sampler != null) {
                    log.debug(port + "Will deliver sample " + sampler.getName());
                }
            }
            /*
             * We don't want to store any cookies in the generated test plan
             */
            if (headers != null) {
                headers.removeHeaderNamed(HTTPConstants.HEADER_COOKIE);// Always remove cookies
                headers.removeHeaderNamed(HTTPConstants.HEADER_AUTHORIZATION);// Always remove authorization
                // Remove additional headers
                for(String hdr : headersToRemove){
                    headers.removeHeaderNamed(hdr);
                }
            }
            if(result != null) // deliverSampler allows sampler to be null, but result must not be null
            {
                target.deliverSampler(sampler, new TestElement[] { captureHttpHeaders ? headers : null }, result);
            }
            try {
                clientSocket.close();
            } catch (Exception e) {
                log.error(port + "Failed to close client socket", e);
            }
            if(sampler != null) {
                sampler.threadFinished(); // Needed for HTTPSampler2
            }
        }
    }

    /**
     * Get SSL connection from hashmap, creating it if necessary.
     *
     * @param host
     * @return a ssl socket factory, or null if keystore could not be opened/processed
     * @throws IOException
     */
    private SSLSocketFactory getSSLSocketFactory(String host) {
        synchronized (hashHost) {
            if (hashHost.containsKey(host)) {
                log.debug(port + "Good, already in map, host=" + host);
                return hashHost.get(host);
            }
            try {
                SSLContext sslcontext = SSLContext.getInstance(SSLCONTEXT_PROTOCOL);
                sslcontext.init(getKeyManagers(host), null, null);
                SSLSocketFactory sslFactory = sslcontext.getSocketFactory();
                hashHost.put(host, sslFactory);
                log.info(port + "KeyStore for SSL loaded OK and put host in map ("+host+")");
                return sslFactory;
            } catch (GeneralSecurityException e) {
                log.error(port + "Problem with SSL certificate", e);
            } catch (IOException e) {
                log.error(port + "Problem with keystore", e);
            }
            return null;
        }
    }

    /**
     * Return the key managers, wrapped if necessary to return a specific alias
     * 
     * @param serverAlias the alias to return, or null to use whatever is present
     * @param host the target host
     * @return the key managers
     * @throws GeneralSecurityException
     * @throws IOException if the store cannot be opened or read or the alias is missing
     */
    private KeyManager[] getKeyManagers(String host) throws GeneralSecurityException, IOException {
        final KeyStore ks;
        final String serverAlias;
        String keyPass;
        switch(ProxyControl.keystoreType) {
        case JMETER_KEYSTORE:
            ks = getJMeterKeyStore(getPassword(), (String) null);
            keyPass = getPassword(); // above call may have updated the stored password
            serverAlias = JMETER_SERVER_ALIAS;
            break;
        case DYNAMIC_KEYSTORE:
            ks = getJMeterKeyStore(getPassword(), host);
            keyPass = getPassword(); // above call may have updated the stored password
            serverAlias = host;
            break;
        case USER_KEYSTORE:
        default: // Not really needed, but avoids complaints about non-init password strings
            String keyStorePass = JMeterUtils.getPropDefault("proxy.cert.keystorepass", DEFAULT_PASSWORD); // $NON-NLS-1$
            ks = getKeyStore(keyStorePass.toCharArray());
            keyPass = JMeterUtils.getPropDefault("proxy.cert.keypassword", DEFAULT_PASSWORD); // $NON-NLS-1$
            serverAlias = ProxyControl.CERT_ALIAS;
            break;
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEYMANAGERFACTORY);
        kmf.init(ks, keyPass.toCharArray());
        final KeyManager[] keyManagers = kmf.getKeyManagers();
        if (serverAlias == null) {
            return keyManagers;
        } else {
            // Check if alias is suitable here, rather than waiting for connection to fail
            if (!ks.containsAlias(serverAlias)) {
                throw new IOException("Keystore does not contain alias " + serverAlias);
            }
            final int keyManagerCount = keyManagers.length;
            final KeyManager[] wrappedKeyManagers = new KeyManager[keyManagerCount];
            for (int i =0; i < keyManagerCount; i++) {
                wrappedKeyManagers[i] = new ServerAliasKeyManager(keyManagers[i], serverAlias);
            }
            return wrappedKeyManagers;
        }
    }

    private KeyStore getKeyStore(char[] password) throws GeneralSecurityException, IOException {
        File certFile = new File(CERT_DIRECTORY, CERT_FILE);
        InputStream in = null;
        final String certPath = certFile.getAbsolutePath();
        try {
            in = new BufferedInputStream(new FileInputStream(certFile));
            log.info(port + "Opened Keystore file: " + certPath);
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(in, password);
            return ks;
        } catch (FileNotFoundException e) {
            log.error(port + "Could not open Keystore file: " + certPath, e);
            throw e;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    // If host == null, we are not using dynamic keys
    private KeyStore getJMeterKeyStore(String keyStorePass, String host) throws GeneralSecurityException, IOException {
        final File certFile = new File(CERT_DIRECTORY, CERT_FILE);
        final String subject = host == null ? JMETER_SERVER_ALIAS : host;
        KeyStore keyStore = null;
        final String canonicalPath = certFile.getCanonicalPath();
        if (keyStorePass != null) { // Assume we have already created the store
            try {
                keyStore = getKeyStore(keyStorePass.toCharArray());
            } catch (Exception e) { // store is faulty, we need to recreate it
                log.warn(port + "Could not open expected file " + canonicalPath + " " + e.getMessage());            
            }
        }
        if (keyStore == null) { // no existing file or not valid
            keyStorePass = RandomStringUtils.randomAscii(20);
            setPassword(keyStorePass);
            if (host != null) { // i.e. Java 7
                log.info(port + "Creating Proxy CA in " + canonicalPath);
                KeyToolUtils.generateProxyCA(certFile, keyStorePass, CERT_VALIDITY);
                log.info(port + "Creating entry " + subject + " in " + canonicalPath);
                KeyToolUtils.generateHostCert(certFile, keyStorePass, subject, CERT_VALIDITY);
                log.info(port + "Created keystore in " + canonicalPath);
            } else {
                log.info(port + "Generating standard keypair in " + canonicalPath);
                // Must not exist
                if(!certFile.delete()) {
                    throw new IOException("Could not delete file:"+certFile.getAbsolutePath()+", this is needed for certificate generation");
                }
                KeyToolUtils.genkeypair(certFile, JMETER_SERVER_ALIAS, keyStorePass, CERT_VALIDITY, null, null);                    
            }
            keyStore = getKeyStore(keyStorePass.toCharArray()); // This should now work
        }
        // keyStorePass should not be null here; checking it avoids a possible NPE warning below
        if (keyStorePass != null && host != null && !keyStore.containsAlias(host)) {
            log.info(port + "Creating entry '" + host + "' in " + canonicalPath);
        // Requires Java 7
            KeyToolUtils.generateHostCert(certFile, keyStorePass, host, CERT_VALIDITY);
            keyStore = getKeyStore(keyStorePass.toCharArray()); // reload
        }
        return keyStore;
    }
    /**
     * Negotiate a SSL connection.
     *
     * @param sock socket in
     * @param host
     * @return a new client socket over ssl
     * @throws Exception if negotiation failed
     */
    private Socket startSSL(Socket sock, String host) throws IOException {
        SSLSocketFactory sslFactory = getSSLSocketFactory(host);
        SSLSocket secureSocket;
        if (sslFactory != null) {
            try {
                secureSocket = (SSLSocket) sslFactory.createSocket(sock,
                        sock.getInetAddress().getHostName(), sock.getPort(), true);
                secureSocket.setUseClientMode(false);
                if (log.isDebugEnabled()){
                    log.debug(port + "SSL transaction ok with cipher: " + secureSocket.getSession().getCipherSuite());
                }
                return secureSocket;
            } catch (IOException e) {
                log.error(port + "Error in SSL socket negotiation: ", e);
                throw e;
            }
        } else {
            log.warn(port + "Unable to negotiate SSL transaction, no keystore?");
            throw new IOException("Unable to negotiate SSL transaction, no keystore?");
        }
    }

    private SampleResult generateErrorResult(SampleResult result, HttpRequestHdr request, Exception e) {
        return generateErrorResult(result, request, e, "");
    }

    private static SampleResult generateErrorResult(SampleResult result, HttpRequestHdr request, Exception e, String msg) {
        if (result == null) {
            result = new SampleResult();
            ByteArrayOutputStream text = new ByteArrayOutputStream(200);
            e.printStackTrace(new PrintStream(text));
            result.setResponseData(text.toByteArray());
            result.setSamplerData(request.getFirstLine());
            result.setSampleLabel(request.getUrl());
        }
        result.setSuccessful(false);
        result.setResponseMessage(e.getMessage()+msg);
        return result;
    }

    /**
     * Write output to the output stream, then flush and close the stream.
     *
     * @param inBytes
     *            the bytes to write
     * @param out
     *            the output stream to write to
     * @param forcedHTTPS if we changed the protocol to https
     * @throws IOException
     *             if an IOException occurs while writing
     */
    private void writeToClient(SampleResult res, OutputStream out) throws IOException {
        try {
            String responseHeaders = messageResponseHeaders(res);
            out.write(responseHeaders.getBytes(SampleResult.DEFAULT_HTTP_ENCODING));
            out.write(CRLF_BYTES);
            out.write(res.getResponseData());
            out.flush();
            log.debug(port + "Done writing to client");
        } catch (IOException e) {
            log.error("", e);
            throw e;
        } finally {
            try {
                out.close();
            } catch (Exception ex) {
                log.warn(port + "Error while closing socket", ex);
            }
        }
    }

    /**
     * In the event the content was gzipped and unpacked, the content-encoding
     * header must be removed and the content-length header should be corrected.
     *
     * The Transfer-Encoding header is also removed.
     * If the protocol was changed to HTTPS then change any Location header back to http
     * @param res - response
     *
     * @return updated headers to be sent to client
     */
    private String messageResponseHeaders(SampleResult res) {
        String headers = res.getResponseHeaders();
        String [] headerLines=headers.split(NEW_LINE, 0); // drop empty trailing content
        int contentLengthIndex=-1;
        boolean fixContentLength = false;
        for (int i=0;i<headerLines.length;i++){
            String line=headerLines[i];
            String[] parts=line.split(":\\s+",2); // $NON-NLS-1$
            if (parts.length==2){
                if (HTTPConstants.TRANSFER_ENCODING.equalsIgnoreCase(parts[0])){
                    headerLines[i]=null; // We don't want this passed on to browser
                    continue;
                }
                if (HTTPConstants.HEADER_CONTENT_ENCODING.equalsIgnoreCase(parts[0])
                    &&
                    HTTPConstants.ENCODING_GZIP.equalsIgnoreCase(parts[1])
                ){
                    headerLines[i]=null; // We don't want this passed on to browser
                    fixContentLength = true;
                    continue;
                }
                if (HTTPConstants.HEADER_CONTENT_LENGTH.equalsIgnoreCase(parts[0])){
                    contentLengthIndex=i;
                    continue;
                }
            }
        }
        if (fixContentLength && contentLengthIndex>=0){// Fix the content length
            headerLines[contentLengthIndex]=HTTPConstants.HEADER_CONTENT_LENGTH+": "+res.getResponseData().length;
        }
        StringBuilder sb = new StringBuilder(headers.length());
        for (int i=0;i<headerLines.length;i++){
            String line=headerLines[i];
            if (line != null){
                sb.append(line).append(CRLF_STRING);
            }
        }
        return sb.toString();
    }

    /**
     * Write an error message to the client. The message should be the full HTTP
     * response.
     *
     * @param message
     *            the message to write
     */
    private void writeErrorToClient(String message) {
        try {
            OutputStream sockOut = clientSocket.getOutputStream();
            DataOutputStream out = new DataOutputStream(sockOut);
            out.writeBytes(message);
            out.flush();
        } catch (Exception e) {
            log.warn(port + "Exception while writing error", e);
        }
    }

    /**
     * Add the page encoding of the sample result to the Map with page encodings
     *
     * @param result the sample result to check
     * @return the page encoding found for the sample result, or null
     */
    private String addPageEncoding(SampleResult result) {
        String pageEncoding = ConversionUtils.getEncodingFromContentType(result.getContentType());
        if(pageEncoding != null) {
            String urlWithoutQuery = getUrlWithoutQuery(result.getURL());
            synchronized(pageEncodings) {
                pageEncodings.put(urlWithoutQuery, pageEncoding);
            }
        }
        return pageEncoding;
    }

    /**
     * Add the form encodings for all forms in the sample result
     *
     * @param result the sample result to check
     * @param pageEncoding the encoding used for the sample result page
     */
    private void addFormEncodings(SampleResult result, String pageEncoding) {
        FormCharSetFinder finder = new FormCharSetFinder();
        if (!result.getContentType().startsWith("text/")){ // TODO perhaps make more specific than this?
            return; // no point parsing anything else, e.g. GIF ...
        }
        try {
            finder.addFormActionsAndCharSet(result.getResponseDataAsString(), formEncodings, pageEncoding);
        }
        catch (HTMLParseException parseException) {
            log.debug(port + "Unable to parse response, could not find any form character set encodings");
        }
    }

    private String getUrlWithoutQuery(URL url) {
        String fullUrl = url.toString();
        String urlWithoutQuery = fullUrl;
        String query = url.getQuery();
        if(query != null) {
            // Get rid of the query and the ?
            urlWithoutQuery = urlWithoutQuery.substring(0, urlWithoutQuery.length() - query.length() - 1);
        }
        return urlWithoutQuery;
    }

    private String getPassword() {
        return prefs.get(USER_PASSWORD_KEY, null);
    }

    private void setPassword(String password) {
        prefs.put(USER_PASSWORD_KEY, password);        
    }

}