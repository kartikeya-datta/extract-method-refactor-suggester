error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7391.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7391.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7391.java
text:
```scala
m@@anager.setName(JMeterUtils.getResString("header_manager_title")); // $NON-NLS-1$

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.jmeter.protocol.http.config.MultipartUrlConfig;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui2;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler2;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerFactory;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

//For unit tests, @see TestHttpRequestHdr

/**
 * The headers of the client HTTP request.
 *
 */
public class HttpRequestHdr {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final String HTTP = "http"; // $NON-NLS-1$
    private static final String HTTPS = "https"; // $NON-NLS-1$
    private static final String PROXY_CONNECTION = "proxy-connection"; // $NON-NLS-1$
    private static final String CONTENT_TYPE = "content-type"; // $NON-NLS-1$
    private static final String CONTENT_LENGTH = "content-length"; // $NON-NLS-1$

    /** Filetype to be used for the temporary binary files*/
    private static final String binaryFileSuffix =
        JMeterUtils.getPropDefault("proxy.binary.filesuffix",// $NON-NLS-1$
                                   ".binary"); // $NON-NLS-1$

    /** Which content-types will be treated as binary (exact match) */
    private static final Set binaryContentTypes = new HashSet();

    /** Where to store the temporary binary files */
    private static final String binaryDirectory =
        JMeterUtils.getPropDefault("proxy.binary.directory",// $NON-NLS-1$
                JMeterUtils.getProperty("user.dir")); // $NON-NLS-1$ proxy.binary.filetype=binary

    static {
        String binaries = JMeterUtils.getPropDefault("proxy.binary.types", // $NON-NLS-1$
                "application/x-amf,application/x-java-serialized-object"); // $NON-NLS-1$
        if (binaries.length() > 0){
            StringTokenizer s = new StringTokenizer(binaries,"|, ");// $NON-NLS-1$
            while (s.hasMoreTokens()){
               binaryContentTypes.add(s.nextToken());
            }
        }
    }

    /**
     * Http Request method. Such as get or post.
     */
    private String method = ""; // $NON-NLS-1$

    /**
     * The requested url. The universal resource locator that hopefully uniquely
     * describes the object or service the client is requesting.
     */
    private String url = ""; // $NON-NLS-1$

    /**
     * Version of http being used. Such as HTTP/1.0.
     */
    private String version = ""; // NOTREAD // $NON-NLS-1$

    private byte[] rawPostData;

    private final Map headers = new HashMap();

    private final HTTPSamplerBase sampler;

    private HeaderManager headerManager;

    /*
     * Optionally number the requests
     */
    private static final boolean numberRequests =
        JMeterUtils.getPropDefault("proxy.number.requests", false); // $NON-NLS-1$

    private static volatile int requestNumber = 0;// running number

    public HttpRequestHdr() {
        this.sampler = HTTPSamplerFactory.newInstance();
    }

    /**
     * @param sampler the http sampler
     */
    public HttpRequestHdr(HTTPSamplerBase sampler) {
        this.sampler = sampler;
    }

    /**
     * Parses a http header from a stream.
     *
     * @param in
     *            the stream to parse.
     * @return array of bytes from client.
     */
    public byte[] parse(InputStream in) throws IOException {
        boolean inHeaders = true;
        int readLength = 0;
        int dataLength = 0;
        boolean firstLine = true;
        ByteArrayOutputStream clientRequest = new ByteArrayOutputStream();
        ByteArrayOutputStream line = new ByteArrayOutputStream();
        int x;
        while ((inHeaders || readLength < dataLength) && ((x = in.read()) != -1)) {
            line.write(x);
            clientRequest.write(x);
            if (firstLine && !CharUtils.isAscii((char) x)){// includes \n
                throw new IllegalArgumentException("Only ASCII supported in headers (perhaps SSL was used?)");
            }
            if (inHeaders && (byte) x == (byte) '\n') { // $NON-NLS-1$
                if (line.size() < 3) {
                    inHeaders = false;
                    firstLine = false; // cannot be first line either
                }
                if (firstLine) {
                    parseFirstLine(line.toString());
                    firstLine = false;
                } else {
                    // parse other header lines, looking for Content-Length
                    final int contentLen = parseLine(line.toString());
                    if (contentLen > 0) {
                        dataLength = contentLen; // Save the last valid content length one
                    }
                }
                if (log.isDebugEnabled()){
                    log.debug("Client Request Line: " + line.toString());
                }
                line.reset();
            } else if (!inHeaders) {
                readLength++;
            }
        }
        // Keep the raw post data
        rawPostData = line.toByteArray();

        if (log.isDebugEnabled()){
            log.debug("rawPostData in default JRE encoding: " + new String(rawPostData));
            log.debug("Request: " + clientRequest.toString());
        }
        return clientRequest.toByteArray();
    }

    private void parseFirstLine(String firstLine) {
        if (log.isDebugEnabled()) {
            log.debug("browser request: " + firstLine);
        }
        if (!CharUtils.isAsciiAlphanumeric(firstLine.charAt(0))) {
            throw new IllegalArgumentException("Unrecognised header line (probably used HTTPS)");
        }
        StringTokenizer tz = new StringTokenizer(firstLine);
        method = getToken(tz).toUpperCase(java.util.Locale.ENGLISH);
        url = getToken(tz);
        if (url.toLowerCase(java.util.Locale.ENGLISH).startsWith(HTTPConstants.PROTOCOL_HTTPS)) {
            throw new IllegalArgumentException("Cannot handle https URLS: " + url);
        }
        version = getToken(tz);
        if (log.isDebugEnabled()) {
            log.debug("parser input:  " + firstLine);
            log.debug("parsed method: " + method);
            log.debug("parsed url:    " + url);
            log.debug("parsed version:" + version);
        }
        if ("CONNECT".equalsIgnoreCase(method)){
            throw new IllegalArgumentException("Cannot handle CONNECT - probably used HTTPS");
        }
    }

    /*
     * Split line into name/value pairs and store in headers if relevant
     * If name = "content-length", then return value as int, else return 0
     */
    private int parseLine(String nextLine) {
        StringTokenizer tz;
        tz = new StringTokenizer(nextLine);
        String token = getToken(tz);
        // look for termination of HTTP command
        if (0 == token.length()) {
            return 0;
        }
        String trimmed = token.trim();
        String name = trimmed.substring(0, trimmed.length() - 1);// drop ':'
        String value = getRemainder(tz);
        headers.put(name.toLowerCase(java.util.Locale.ENGLISH), new Header(name, value));
        if (name.equalsIgnoreCase(CONTENT_LENGTH)) {
            return Integer.parseInt(value);
        }
        return 0;
    }

    private HeaderManager createHeaderManager() {
        HeaderManager manager = new HeaderManager();
        Iterator keys = headers.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (!key.equals(PROXY_CONNECTION) && !key.equals(CONTENT_LENGTH)) {
                manager.add((Header) headers.get(key));
            }
        }
        manager.setName("Browser-derived headers");
        manager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        manager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        return manager;
    }

    public HeaderManager getHeaderManager() {
        if(headerManager == null) {
            headerManager = createHeaderManager();
        }
        return headerManager;
    }

    public HTTPSamplerBase getSampler(Map pageEncodings, Map formEncodings)
            throws MalformedURLException, IOException {
        // Damn! A whole new GUI just to instantiate a test element?
        // Isn't there a beter way?
        HttpTestSampleGui tempGui = null;
        // Create the corresponding gui for the sampler class
        if(sampler instanceof HTTPSampler2) {
            tempGui = new HttpTestSampleGui2();
        }
        else {
            tempGui = new HttpTestSampleGui();
        }
        sampler.setProperty(TestElement.GUI_CLASS, tempGui.getClass().getName());

        // Populate the sampler
        populateSampler(pageEncodings, formEncodings);

        tempGui.configure(sampler);
        tempGui.modifyTestElement(sampler);
        // Defaults
        sampler.setFollowRedirects(false);
        sampler.setUseKeepAlive(true);

        if (log.isDebugEnabled()) {
            log.debug("getSampler: sampler path = " + sampler.getPath());
        }
        return sampler;
    }

    /**
     *
     * @return the sampler
     * @throws MalformedURLException
     * @throws IOException
     * @throws ProtocolException
     * @deprecated use the getSampler(HashMap pageEncodings, HashMap formEncodings) instead, since
     * that properly handles the encodings of the page
     */
    public HTTPSamplerBase getSampler() throws MalformedURLException, IOException, ProtocolException {
        return getSampler(null, null);
    }

    private String getContentType() {
        Header contentTypeHeader = (Header) headers.get(CONTENT_TYPE);
        if (contentTypeHeader != null) {
            return contentTypeHeader.getValue();
        }
        return null;
    }

    private boolean isMultipart(String contentType) {
        if (contentType != null && contentType.startsWith(HTTPConstants.MULTIPART_FORM_DATA)) {
            return true;
        }
        return false;
    }

    private MultipartUrlConfig getMultipartConfig(String contentType) {
        if(isMultipart(contentType)) {
            // Get the boundary string for the multiparts from the content type
            String boundaryString = contentType.substring(contentType.toLowerCase(java.util.Locale.ENGLISH).indexOf("boundary=") + "boundary=".length());
            return new MultipartUrlConfig(boundaryString);
        }
        return null;
    }

    private void populateSampler(Map pageEncodings, Map formEncodings)
            throws MalformedURLException, UnsupportedEncodingException {
        sampler.setDomain(serverName());
        if (log.isDebugEnabled()) {
            log.debug("Proxy: setting server: " + sampler.getDomain());
        }
        sampler.setMethod(method);
        log.debug("Proxy: setting method: " + sampler.getMethod());
        sampler.setPort(serverPort());
        if (log.isDebugEnabled()) {
            log.debug("Proxy: setting port: " + sampler.getPort());
        }
        if (url.indexOf("//") > -1) {
            String protocol = url.substring(0, url.indexOf(":"));
            if (log.isDebugEnabled()) {
                log.debug("Proxy: setting protocol to : " + protocol);
            }
            sampler.setProtocol(protocol);
        } else if (sampler.getPort() == HTTPConstants.DEFAULT_HTTPS_PORT) {
            sampler.setProtocol(HTTPS);
            if (log.isDebugEnabled()) {
                log.debug("Proxy: setting protocol to https");
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Proxy setting default protocol to: http");
            }
            sampler.setProtocol(HTTP);
        }

        URL pageUrl = null;
        if(sampler.isProtocolDefaultPort()) {
            pageUrl = new URL(sampler.getProtocol(), sampler.getDomain(), getPath());
        }
        else {
            pageUrl = new URL(sampler.getProtocol(), sampler.getDomain(), sampler.getPort(), getPath());
        }
        String urlWithoutQuery = getUrlWithoutQuery(pageUrl);


        // Check if the request itself tells us what the encoding is
        String contentEncoding = null;
        String requestContentEncoding = ConversionUtils.getEncodingFromContentType(getContentType());
        if(requestContentEncoding != null) {
            contentEncoding = requestContentEncoding;
        }
        else {
            // Check if we know the encoding of the page
            if (pageEncodings != null) {
                synchronized (pageEncodings) {
                    contentEncoding = (String) pageEncodings.get(urlWithoutQuery);
                }
            }
            // Check if we know the encoding of the form
            if (formEncodings != null) {
                synchronized (formEncodings) {
                    String formEncoding = (String) formEncodings.get(urlWithoutQuery);
                    // Form encoding has priority over page encoding
                    if (formEncoding != null) {
                        contentEncoding = formEncoding;
                    }
                }
            }
        }

        // Get the post data using the content encoding of the request
        String postData = null;
        if (log.isDebugEnabled()) {
            if(contentEncoding != null) {
                log.debug("Using encoding " + contentEncoding + " for request body");
            }
            else {
                log.debug("No encoding found, using JRE default encoding for request body");
            }
        }
        if (contentEncoding != null) {
            postData = new String(rawPostData, contentEncoding);
        } else {
            // Use default encoding
            postData = new String(rawPostData);
        }

        if(contentEncoding != null) {
            sampler.setPath(getPath(), contentEncoding);
        }
        else {
            // Although the spec says UTF-8 should be used for encoding URL parameters,
            // most browser use ISO-8859-1 for default if encoding is not known.
            // We use null for contentEncoding, then the url parameters will be added
            // with the value in the URL, and the "encode?" flag set to false
            sampler.setPath(getPath(), null);
        }
        if (log.isDebugEnabled()) {
            log.debug("Proxy: setting path: " + sampler.getPath());
        }
        if (numberRequests) {
            requestNumber++;
            sampler.setName(requestNumber + " " + sampler.getPath());
        } else {
            sampler.setName(sampler.getPath());
        }

        // Set the content encoding
        if(contentEncoding != null) {
            sampler.setContentEncoding(contentEncoding);
        }

        // If it was a HTTP GET request, then all parameters in the URL
        // has been handled by the sampler.setPath above, so we just need
        // to do parse the rest of the request if it is not a GET request
        if(!HTTPConstants.GET.equals(method)) {
            // Check if it was a multipart http post request
            final String contentType = getContentType();
            MultipartUrlConfig urlConfig = getMultipartConfig(contentType);
            if (urlConfig != null) {
                urlConfig.parseArguments(postData);
                // Tell the sampler to do a multipart post
                sampler.setDoMultipartPost(true);
                // Remove the header for content-type and content-length, since
                // those values will most likely be incorrect when the sampler
                // performs the multipart request, because the boundary string
                // will change
                getHeaderManager().removeHeaderNamed(CONTENT_TYPE);
                getHeaderManager().removeHeaderNamed(CONTENT_LENGTH);

                // Set the form data
                sampler.setArguments(urlConfig.getArguments());
                // Set the file uploads
                sampler.setHTTPFiles(urlConfig.getHTTPFileArgs().asArray());
            } else if (postData.trim().startsWith("<?")) {
                // Not sure if this is needed anymore. I assume these requests
                // do not have HTTPConstants.APPLICATION_X_WWW_FORM_URLENCODED as content type,
                // and they would therefore be catched by the last else if of these if else if tests
                sampler.addNonEncodedArgument("", postData, ""); //used when postData is pure xml (ex. an xml-rpc call)
            } else if (contentType == null || contentType.startsWith(HTTPConstants.APPLICATION_X_WWW_FORM_URLENCODED) ){
                // It is the most common post request, with parameter name and values
                // We also assume this if no content type is present, to be most backwards compatible,
                // but maybe we should only parse arguments if the content type is as expected
                sampler.parseArguments(postData.trim(), contentEncoding); //standard name=value postData
            } else if (postData.length() > 0) {
                if (isBinaryContent(contentType)) {
                    try {
                        File tempDir = new File(binaryDirectory);
                        File out = File.createTempFile(method, binaryFileSuffix, tempDir);
                        FileUtils.writeByteArrayToFile(out,rawPostData);
                        HTTPFileArg [] files = {new HTTPFileArg(out.getPath(),"",contentType)};
                        sampler.setHTTPFiles(files);
                    } catch (IOException e) {
                        log.warn("Could not create binary file: "+e);
                    }
                } else {
                    // Just put the whole postbody as the value of a parameter
                    sampler.addNonEncodedArgument("", postData, ""); //used when postData is pure xml (ex. an xml-rpc call)
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("sampler path = " + sampler.getPath());
        }
    }

    private boolean isBinaryContent(String contentType) {
        if (contentType == null) return false;
        return binaryContentTypes.contains(contentType);
    }

    //
    // Parsing Methods
    //

    /**
     * Find the //server.name from an url.
     *
     * @return server's internet name
     */
    private String serverName() {
        // chop to "server.name:x/thing"
        String str = url;
        int i = str.indexOf("//"); // $NON-NLS-1$
        if (i > 0) {
            str = str.substring(i + 2);
        }
        // chop to server.name:xx
        i = str.indexOf("/"); // $NON-NLS-1$
        if (0 < i) {
            str = str.substring(0, i);
        }
        // chop to server.name
        i = str.indexOf(":"); // $NON-NLS-1$
        if (0 < i) {
            str = str.substring(0, i);
        }
        return str;
    }

    // TODO replace repeated substr() above and below with more efficient method.

    /**
     * Find the :PORT from http://server.ect:PORT/some/file.xxx
     *
     * @return server's port (or UNSPECIFIED if not found)
     */
    private int serverPort() {
        String str = url;
        // chop to "server.name:x/thing"
        int i = str.indexOf("//");
        if (i > 0) {
            str = str.substring(i + 2);
        }
        // chop to server.name:xx
        i = str.indexOf("/");
        if (0 < i) {
            str = str.substring(0, i);
        }
        // chop XX
        i = str.indexOf(":");
        if (0 < i) {
            return Integer.parseInt(str.substring(i + 1).trim());
        }
        return HTTPSamplerBase.UNSPECIFIED_PORT;
    }

    /**
     * Find the /some/file.xxxx from http://server.ect:PORT/some/file.xxx
     *
     * @return the path
     */
    private String getPath() {
        String str = url;
        int i = str.indexOf("//");
        if (i > 0) {
            str = str.substring(i + 2);
        }
        i = str.indexOf("/");
        if (i < 0) {
            return "";
        }
        return str.substring(i);
    }

    /**
     * Returns the url string extracted from the first line of the client request.
     *
     * @return the url
     */
    public String getUrl(){
        return url;
    }

    /**
     * Returns the next token in a string.
     *
     * @param tk
     *            String that is partially tokenized.
     * @return The remainder
     */
    private String getToken(StringTokenizer tk) {
        if (tk.hasMoreTokens()) {
            return tk.nextToken();
        }
        return "";// $NON-NLS-1$
    }

    /**
     * Returns the remainder of a tokenized string.
     *
     * @param tk
     *            String that is partially tokenized.
     * @return The remainder
     */
    private String getRemainder(StringTokenizer tk) {
        StringBuffer strBuff = new StringBuffer();
        if (tk.hasMoreTokens()) {
            strBuff.append(tk.nextToken());
        }
        while (tk.hasMoreTokens()) {
            strBuff.append(" "); // $NON-NLS-1$
            strBuff.append(tk.nextToken());
        }
        return strBuff.toString();
    }

    private String getUrlWithoutQuery(URL _url) {
        String fullUrl = _url.toString();
        String urlWithoutQuery = fullUrl;
        String query = _url.getQuery();
        if(query != null) {
            // Get rid of the query and the ?
            urlWithoutQuery = urlWithoutQuery.substring(0, urlWithoutQuery.length() - query.length() - 1);
        }
        return urlWithoutQuery;
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7391.java