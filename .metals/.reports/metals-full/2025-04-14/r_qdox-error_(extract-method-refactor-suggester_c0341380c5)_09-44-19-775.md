error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9354.java
text:
```scala
public static final R@@edirectStrategy REDIRECT_STRATEGY = new DefaultRedirectStrategy() {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.test.integration.security.common;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.network.NetworkUtils;
import org.jboss.as.test.integration.security.common.negotiation.JBossNegotiateSchemeFactory;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.security.auth.callback.UsernamePasswordHandler;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.util.Base64;

/**
 * Common utilities for JBoss AS security tests.
 *
 * @author Jan Lanik
 * @author Josef Cacek
 */
public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    /** The REDIRECT_STRATEGY for Apache HTTP Client */
    private static final RedirectStrategy REDIRECT_STRATEGY = new DefaultRedirectStrategy() {
        @Override
        public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
            boolean isRedirect = false;
            try {
                isRedirect = super.isRedirected(request, response, context);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            if (!isRedirect) {
                final int responseCode = response.getStatusLine().getStatusCode();
                isRedirect = (responseCode == 301 || responseCode == 302);
            }
            return isRedirect;
        }
    };

    /**
     * Return MD5 hash of the given string value, encoded with given {@link Coding}. If the value or coding is <code>null</code>
     * then original value is returned.
     *
     * @param value
     * @param coding
     * @return encoded MD5 hash of the string or original value if some of parameters is null
     */
    public static String hashMD5(String value, Coding coding) {
        return (coding == null || value == null) ? value : hash(value, "MD5", coding);
    }

    public static String hash(String target, String algorithm, Coding coding) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = target.getBytes();
        byte[] byteHash = md.digest(bytes);

        String encodedHash = null;

        switch (coding) {
            case BASE_64:
                encodedHash = Base64.encodeBytes(byteHash);
                break;
            case HEX:
                encodedHash = toHex(byteHash);
                break;
            default:
                throw new IllegalArgumentException("Unsuported coding:" + coding.name());
        }

        return encodedHash;
    }

    public static String toHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            // top 4 bits
            char c = (char) ((b >> 4) & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
            // bottom 4 bits
            c = (char) (b & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
        }
        return sb.toString();
    }

    public static URL getResource(String name) {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        return tccl.getResource(name);
    }

    private static final long STOP_DELAY_DEFAULT = 0;

    /**
     * stops execution of the program indefinitely useful in testsuite debugging
     */
    public static void stop() {
        stop(STOP_DELAY_DEFAULT);
    }

    /**
     * stop test execution for a given time interval useful for debugging
     *
     * @param delay interval (milliseconds), if delay<=0, interval is considered to be infinite (Long.MAX_VALUE)
     */
    public static void stop(long delay) {
        long currentTime = System.currentTimeMillis();
        long remainingTime = 0 < delay ? currentTime + delay - System.currentTimeMillis() : Long.MAX_VALUE;
        while (remainingTime > 0) {
            try {
                Thread.sleep(remainingTime);
            } catch (InterruptedException ex) {
                remainingTime = currentTime + delay - System.currentTimeMillis();
                continue;
            }
        }
    }

    public static void applyUpdates(final List<ModelNode> updates, final ModelControllerClient client) throws Exception {
        for (ModelNode update : updates) {
            applyUpdate(update, client);
        }
    }

    public static void applyUpdate(ModelNode update, final ModelControllerClient client) throws Exception {
        ModelNode result = client.execute(new OperationBuilder(update).build());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Client update: " + update);
            LOGGER.info("Client update result: " + result);
        }
        if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
            LOGGER.debug("Operation succeeded.");
        } else if (result.hasDefined("failure-description")) {
            throw new RuntimeException(result.get("failure-description").toString());
        } else {
            throw new RuntimeException("Operation not successful; outcome = " + result.get("outcome"));
        }
    }

    /**
     * Read the contents of an HttpResponse's entity and return it as a String. The content is converted using the character set
     * from the entity (if any), failing that, "ISO-8859-1" is used.
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getContent(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * Makes HTTP call with FORM authentication.
     *
     * @param URL
     * @param user
     * @param pass
     * @param expectedStatusCode
     * @throws Exception
     */
    public static void makeCall(String URL, String user, String pass, int expectedStatusCode) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(URL);

            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);

            // We should get the Login Page
            StatusLine statusLine = response.getStatusLine();
            System.out.println("Login form get: " + statusLine);
            assertEquals(200, statusLine.getStatusCode());

            System.out.println("Initial set of cookies:");
            List<Cookie> cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }

            // We should now login with the user name and password
            HttpPost httpost = new HttpPost(URL + "/j_security_check");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("j_username", user));
            nvps.add(new BasicNameValuePair("j_password", pass));

            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            response = httpclient.execute(httpost);
            entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);

            statusLine = response.getStatusLine();

            // Post authentication - we have a 302
            assertEquals(302, statusLine.getStatusCode());
            Header locationHeader = response.getFirstHeader("Location");
            String location = locationHeader.getValue();

            HttpGet httpGet = new HttpGet(location);
            response = httpclient.execute(httpGet);

            entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);

            System.out.println("Post logon cookies:");
            cookies = httpclient.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }

            // Either the authentication passed or failed based on the expected status code
            statusLine = response.getStatusLine();
            assertEquals(expectedStatusCode, statusLine.getStatusCode());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

    /**
     * Exports given archive to the given file path.
     *
     * @param archive
     * @param filePath
     */
    public static void saveArchive(Archive<?> archive, String filePath) {
        archive.as(ZipExporter.class).exportTo(new File(filePath), true);
    }

    /**
     * Exports given archive to the given folder.
     *
     * @param archive archive to export (not-<code>null</code>)
     * @param folderPath
     */
    public static void saveArchiveToFolder(Archive<?> archive, String folderPath) {
        final File exportFile = new File(folderPath, archive.getName());
        LOGGER.info("Exporting archive: " + exportFile.getAbsolutePath());
        archive.as(ZipExporter.class).exportTo(exportFile, true);
    }

    /**
     * Returns "secondary.test.address" system property if such exists. If not found, then there is a fallback to
     * {@link ManagementClient#getMgmtAddress()}. Returned value can be converted to cannonical hostname if
     * useCannonicalHost==true. Returned value is not formatted for URLs (i.e. square brackets are not placed around IPv6 addr -
     * for instance "::1")
     *
     * @param mgmtClient management client instance (may be <code>null</code>)
     * @param useCannonicalHost
     * @return
     */
    public static String getSecondaryTestAddress(final ManagementClient mgmtClient, final boolean useCannonicalHost) {
        String address = System.getProperty("secondary.test.address");
        if (StringUtils.isBlank(address) && mgmtClient != null) {
            address = mgmtClient.getMgmtAddress();
        }
        if (useCannonicalHost) {
            address = getCannonicalHost(address);
        }
        return address;
    }

    /**
     * Returns "secondary.test.address" system property if such exists. If not found, then there is a fallback to
     * {@link ManagementClient#getMgmtAddress()}. Returned value is formatted to use in URLs (i.e. if it's IPv6 address, then
     * square brackets are placed around - e.g. "[::1]")
     *
     * @param mgmtClient management client instance (may be <code>null</code>)
     * @return
     */
    public static String getSecondaryTestAddress(final ManagementClient mgmtClient) {
        return NetworkUtils.formatPossibleIpv6Address(getSecondaryTestAddress(mgmtClient, false));
    }

    /**
     * Returns response body for the given URL request as a String. It also checks if the returned HTTP status code is the
     * expected one. If the server returns {@link HttpServletResponse#SC_UNAUTHORIZED} and username is provided, then a new
     * request is created with the provided credentials (basic authentication).
     *
     * @param url URL to which the request should be made
     * @param user Username (may be null)
     * @param pass Password (may be null)
     * @param expectedStatusCode expected status code returned from the requested server
     * @return HTTP response body
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String makeCallWithBasicAuthn(URL url, String user, String pass, int expectedStatusCode)
            throws IOException, URISyntaxException {
        LOGGER.info("Requesting URL " + url);
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            final HttpGet httpGet = new HttpGet(url.toURI());
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpServletResponse.SC_UNAUTHORIZED != statusCode || StringUtils.isEmpty(user)) {
                assertEquals("Unexpected HTTP response status code.", expectedStatusCode, statusCode);
                return EntityUtils.toString(response.getEntity());
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("HTTP response was SC_UNAUTHORIZED, let's authenticate the user " + user);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null)
                EntityUtils.consume(entity);

            final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pass);
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(url.getHost(), url.getPort()), credentials);

            response = httpClient.execute(httpGet);
            statusCode = response.getStatusLine().getStatusCode();
            assertEquals("Unexpected status code returned after the authentication.", expectedStatusCode, statusCode);
            return EntityUtils.toString(response.getEntity());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Returns response body for the given URL request as a String. It also checks if the returned HTTP status code is the
     * expected one. If the server returns {@link HttpServletResponse#SC_UNAUTHORIZED} and an username is provided, then the
     * given user is authenticated against Kerberos and a new request is executed under the new subject.
     *
     * @param uri URI to which the request should be made
     * @param user Username
     * @param pass Password
     * @param expectedStatusCode expected status code returned from the requested server
     * @return HTTP response body
     * @throws IOException
     * @throws URISyntaxException
     * @throws PrivilegedActionException
     * @throws LoginException
     */
    public static String makeCallWithKerberosAuthn(final URI uri, final String user, final String pass,
            final int expectedStatusCode) throws IOException, URISyntaxException,
            PrivilegedActionException, LoginException {
        LOGGER.info("Requesting URI: " + uri);
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            httpClient.getAuthSchemes().register(AuthPolicy.SPNEGO, new JBossNegotiateSchemeFactory(true));
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), new NullHCCredentials());

            final HttpGet httpGet = new HttpGet(uri);
            final HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpServletResponse.SC_UNAUTHORIZED != statusCode || StringUtils.isEmpty(user)) {
                assertEquals("Unexpected HTTP response status code.", expectedStatusCode, statusCode);
                return EntityUtils.toString(response.getEntity());
            }
            final HttpEntity entity = response.getEntity();
            final Header[] authnHeaders = response.getHeaders("WWW-Authenticate");
            assertTrue("WWW-Authenticate header is present", authnHeaders != null && authnHeaders.length > 0);
            final Set<String> authnHeaderValues = new HashSet<String>();
            for (final Header header : authnHeaders) {
                authnHeaderValues.add(header.getValue());
            }
            assertTrue("WWW-Authenticate: Negotiate header is missing", authnHeaderValues.contains("Negotiate"));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("HTTP response was SC_UNAUTHORIZED, let's authenticate the user " + user);
            }
            if (entity != null)
                EntityUtils.consume(entity);

            // Use our custom configuration to avoid reliance on external config
            Configuration.setConfiguration(new Krb5LoginConfiguration());
            // 1. Authenticate to Kerberos.
            final LoginContext lc = new LoginContext(Utils.class.getName(), new UsernamePasswordHandler(user, pass));
            lc.login();

            // 2. Perform the work as authenticated Subject.
            final String responseBody = Subject.doAs(lc.getSubject(), new PrivilegedExceptionAction<String>() {
                public String run() throws Exception {
                    final HttpResponse response = httpClient.execute(httpGet);
                    int statusCode = response.getStatusLine().getStatusCode();
                    assertEquals("Unexpected status code returned after the authentication.", expectedStatusCode, statusCode);
                    return EntityUtils.toString(response.getEntity());
                }
            });
            lc.logout();
            return responseBody;
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Creates request against SPNEGO protected web-app with FORM fallback. It tries to login using SPNEGO first - if it fails,
     * FORM is used.
     *
     * @param contextUrl
     * @param page
     * @param user
     * @param pass
     * @param expectedStatusCode
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws PrivilegedActionException
     * @throws LoginException
     */
    public static String makeHttpCallWithFallback(final String contextUrl, final String page, final String user,
            final String pass, final int expectedStatusCode) throws IOException, URISyntaxException,
            PrivilegedActionException, LoginException {
        final String strippedContextUrl = StringUtils.stripEnd(contextUrl, "/");
        final String url = strippedContextUrl + page;
        LOGGER.info("Requesting URL: " + url);
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setRedirectStrategy(REDIRECT_STRATEGY);
        String unauthorizedPageBody = null;
        try {
            httpClient.getAuthSchemes().register(AuthPolicy.SPNEGO, new JBossNegotiateSchemeFactory(true));
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), new NullHCCredentials());

            final HttpGet httpGet = new HttpGet(url);
            final HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpServletResponse.SC_UNAUTHORIZED != statusCode || StringUtils.isEmpty(user)) {
                assertEquals("Unexpected HTTP response status code.", expectedStatusCode, statusCode);
                return EntityUtils.toString(response.getEntity());
            }
            final Header[] authnHeaders = response.getHeaders("WWW-Authenticate");
            assertTrue("WWW-Authenticate header is present", authnHeaders != null && authnHeaders.length > 0);
            final Set<String> authnHeaderValues = new HashSet<String>();
            for (final Header header : authnHeaders) {
                authnHeaderValues.add(header.getValue());
            }
            assertTrue("WWW-Authenticate: Negotiate header is missing", authnHeaderValues.contains("Negotiate"));

            LOGGER.debug("HTTP response was SC_UNAUTHORIZED, let's authenticate the user " + user);
            unauthorizedPageBody = EntityUtils.toString(response.getEntity());

            // Use our custom configuration to avoid reliance on external config
            Configuration.setConfiguration(new Krb5LoginConfiguration());
            // 1. Authenticate to Kerberos.
            final LoginContext lc = new LoginContext(Utils.class.getName(), new UsernamePasswordHandler(user, pass));
            lc.login();

            // 2. Perform the work as authenticated Subject.
            final String responseBody = Subject.doAs(lc.getSubject(), new PrivilegedExceptionAction<String>() {
                public String run() throws Exception {
                    final HttpResponse response = httpClient.execute(httpGet);
                    int statusCode = response.getStatusLine().getStatusCode();
                    assertEquals("Unexpected status code returned after the authentication.", expectedStatusCode, statusCode);
                    return EntityUtils.toString(response.getEntity());
                }
            });
            lc.logout();
            return responseBody;
        } catch (LoginException e) {
            assertNotNull(unauthorizedPageBody);
            assertTrue(unauthorizedPageBody.contains("j_security_check"));

            HttpPost httpPost = new HttpPost(strippedContextUrl + "/j_security_check");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("j_username", user));
            nameValuePairs.add(new BasicNameValuePair("j_password", pass));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            final HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            assertEquals("Unexpected status code returned after the authentication.", expectedStatusCode, statusCode);
            return EntityUtils.toString(response.getEntity());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Creates request against SPNEGO protected web-app with FORM fallback. It doesn't try to login using SPNEGO - it uses FORM
     * authn directly.
     *
     * @param contextUrl
     * @param page
     * @param user
     * @param pass
     * @param expectedStatusCode
     * @return
     * @throws IOException
     * @throws URISyntaxException
     * @throws PrivilegedActionException
     * @throws LoginException
     */
    public static String makeHttpCallWoSPNEGO(final String contextUrl, final String page, final String user, final String pass,
            final int expectedStatusCode) throws IOException, URISyntaxException,
            PrivilegedActionException, LoginException {
        final String strippedContextUrl = StringUtils.stripEnd(contextUrl, "/");
        final String url = strippedContextUrl + page;
        LOGGER.info("Requesting URL: " + url);
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.setRedirectStrategy(REDIRECT_STRATEGY);
        String unauthorizedPageBody = null;
        try {
            final HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpServletResponse.SC_UNAUTHORIZED != statusCode || StringUtils.isEmpty(user)) {
                assertEquals("Unexpected HTTP response status code.", expectedStatusCode, statusCode);
                return EntityUtils.toString(response.getEntity());
            }
            final Header[] authnHeaders = response.getHeaders("WWW-Authenticate");
            assertTrue("WWW-Authenticate header is present", authnHeaders != null && authnHeaders.length > 0);
            final Set<String> authnHeaderValues = new HashSet<String>();
            for (final Header header : authnHeaders) {
                authnHeaderValues.add(header.getValue());
            }
            assertTrue("WWW-Authenticate: Negotiate header is missing", authnHeaderValues.contains("Negotiate"));

            LOGGER.debug("HTTP response was SC_UNAUTHORIZED, let's authenticate the user " + user);
            unauthorizedPageBody = EntityUtils.toString(response.getEntity());

            assertNotNull(unauthorizedPageBody);
            LOGGER.info(unauthorizedPageBody);
            assertTrue(unauthorizedPageBody.contains("j_security_check"));

            HttpPost httpPost = new HttpPost(strippedContextUrl + "/j_security_check");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("j_username", user));
            nameValuePairs.add(new BasicNameValuePair("j_password", pass));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpClient.execute(httpPost);
            statusCode = response.getStatusLine().getStatusCode();
            assertEquals("Unexpected status code returned after the authentication.", expectedStatusCode, statusCode);
            return EntityUtils.toString(response.getEntity());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Sets or removes (in case value==null) a system property. It's only a helper method, which avoids
     * {@link NullPointerException} thrown from {@link System#setProperty(String, String)} method, when the value is
     * <code>null</code>.
     *
     * @param key property name
     * @param value property value
     * @return the previous string value of the system property
     */
    public static String setSystemProperty(final String key, final String value) {
        return value == null ? System.clearProperty(key) : System.setProperty(key, value);
    }

    /**
     * Returns management address (host) from the givem {@link ManagementClient}. If the returned value is IPv6 address then
     * square brackets around are stripped.
     *
     * @param managementClient
     * @return
     */
    public static final String getHost(final ManagementClient managementClient) {
        return StringUtils.strip(managementClient.getMgmtAddress(), "[]");
    }

    /**
     * Returns cannonical hostname retrieved from management address of the givem {@link ManagementClient}.
     *
     * @param managementClient
     * @return
     */
    public static final String getCannonicalHost(final ManagementClient managementClient) {
        return getCannonicalHost(managementClient.getMgmtAddress());
    }

    /**
     * Returns cannonical hostname form of the given address.
     *
     * @param address hosname or IP address
     * @return
     */
    public static final String getCannonicalHost(final String address) {
        String host = StringUtils.strip(address, "[]");
        try {
            host = InetAddress.getByName(host).getCanonicalHostName();
        } catch (UnknownHostException e) {
            LOGGER.warn("Unable to get cannonical host name", e);
        }
        return host.toLowerCase(Locale.ENGLISH);
    }

    /**
     * Returns given URI with the replaced hostname. If the URI or host is null, then the original URI is returned.
     *
     * @param uri
     * @param host
     * @return
     * @throws URISyntaxException
     */
    public static final URI replaceHost(final URI uri, final String host) throws URISyntaxException {
        final String origHost = uri == null ? null : uri.getHost();
        final String newHost = NetworkUtils.formatPossibleIpv6Address(host);
        if (origHost == null || newHost == null || newHost.equals(origHost)) {
            return uri;
        }
        return new URI(uri.toString().replace(origHost, newHost));
    }

    /**
     * Returns servlet URL, as concatenation of webapp URL and servlet path.
     *
     * @param webAppURL web application context URL (e.g. injected by Arquillian)
     * @param servletPath Servlet path starting with slash (must be not-<code>null</code>)
     * @param mgmtClient Management Client (may be null)
     * @param useCannonicalHost flag which says if host in URI should be replaced by the cannonical host.
     * @return
     * @throws URISyntaxException
     */
    public static final URI getServletURI(final URL webAppURL, final String servletPath, final ManagementClient mgmtClient,
            boolean useCannonicalHost) throws URISyntaxException {
        URI resultURI = new URI(webAppURL.toExternalForm() + servletPath.substring(1));
        if (useCannonicalHost) {
            resultURI = replaceHost(resultURI, getCannonicalHost(mgmtClient));
        }
        return resultURI;
    }

    /**
     * Generates content of jboss-ejb3.xml file as an ShrinkWrap asset with the given security domain name.
     *
     * @param securityDomain security domain name
     * @return Asset instance
     */
    public static Asset getJBossEjb3XmlAsset(final String securityDomain) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<jboss:ejb-jar xmlns:jboss='http://www.jboss.com/xml/ns/javaee'");
        sb.append("\n\txmlns='http://java.sun.com/xml/ns/javaee'");
        sb.append("\n\txmlns:s='urn:security'");
        sb.append("\n\tversion='3.1'");
        sb.append("\n\timpl-version='2.0'>");
        sb.append("\n\t<assembly-descriptor><s:security>");
        sb.append("\n\t\t<ejb-name>*</ejb-name>");
        sb.append("\n\t\t<s:security-domain>").append(securityDomain).append("</s:security-domain>");
        sb.append("\n\t</s:security></assembly-descriptor>");
        sb.append("\n</jboss:ejb-jar>");
        return new StringAsset(sb.toString());
    }

    /**
     * Generates content of jboss-web.xml file as an ShrinkWrap asset with the given security domain name and given valve class.
     *
     * @param securityDomain security domain name (not-<code>null</code>)
     * @param valveClassName valve class (e.g. an Authenticator) which should be added to jboss-web file (may be
     *        <code>null</code>)
     * @return Asset instance
     */
    public static Asset getJBossWebXmlAsset(final String securityDomain, final String valveClassName) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<jboss-web>");
        sb.append("\n\t<security-domain>").append(securityDomain).append("</security-domain>");
        if (StringUtils.isNotEmpty(valveClassName)) {
            sb.append("\n\t<valve><class-name>").append(valveClassName).append("</class-name></valve>");
        }
        sb.append("\n</jboss-web>");
        return new StringAsset(sb.toString());
    }

    /**
     * Generates content of jboss-web.xml file as an ShrinkWrap asset with the given security domain name.
     *
     * @param securityDomain security domain name
     * @return Asset instance
     */
    public static Asset getJBossWebXmlAsset(final String securityDomain) {
        return getJBossWebXmlAsset(securityDomain, null);
    }

    /**
     * Generates content of the jboss-deployment-structure.xml deployment descriptor as an ShrinkWrap asset. It fills the given
     * dependencies (module names) into it.
     *
     * @param dependencies AS module names
     * @return
     */
    public static Asset getJBossDeploymentStructure(String... dependencies) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<jboss-deployment-structure><deployment><dependencies>");
        if (dependencies != null) {
            for (String moduleName : dependencies) {
                sb.append("\n\t<module name='").append(moduleName).append("'/>");
            }
        }
        sb.append("\n</dependencies></deployment></jboss-deployment-structure>");
        return new StringAsset(sb.toString());
    }

    /**
     * Creates content of users.properties and/or roles.properties files for given array of role names.
     * <p>
     * For instance if you provide 2 roles - "role1", "role2" then the result will be:
     *
     * <pre>
     * role1=role1
     * role2=role2
     * </pre>
     *
     * If you use it as users.properties and roles.properties, then <code>roleName == userName == password</code>
     *
     * @param roles role names (used also as user names and passwords)
     * @return not-<code>null</code> content of users.properties and/or roles.properties
     */
    public static String createUsersFromRoles(String... roles) {
        final StringBuilder sb = new StringBuilder();
        if (roles != null) {
            for (String role : roles) {
                sb.append(role).append("=").append(role).append("\n");
            }
        }
        return sb.toString();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9354.java