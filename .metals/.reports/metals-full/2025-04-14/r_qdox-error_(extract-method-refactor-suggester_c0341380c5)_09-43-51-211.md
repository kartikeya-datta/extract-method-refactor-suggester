error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7189.java
text:
```scala
public v@@oid setup(final ManagementClient managementClient, final String containerId) throws Exception {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.test.integration.web.formauth;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.test.integration.security.common.AbstractSecurityDomainSetup;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.security.Constants.AUTHENTICATION;
import static org.jboss.as.security.Constants.CODE;
import static org.jboss.as.security.Constants.FLAG;
import static org.jboss.as.security.Constants.MODULE_OPTIONS;
import static org.jboss.as.security.Constants.SECURITY_DOMAIN;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests of form authentication
 *
 * @author Scott.Stark@jboss.org
 * @author lbarreiro@redhat.com
 */
@RunWith(Arquillian.class)
@RunAsClient
@ServerSetup(FormAuthUnitTestCase.FormAuthUnitTestCaseSetup.class)
public class FormAuthUnitTestCase {

    private static Logger log = Logger.getLogger(FormAuthUnitTestCase.class);

    @ArquillianResource
    private URL baseURLNoAuth;

    static class FormAuthUnitTestCaseSetup  extends AbstractSecurityDomainSetup {

        @Override
        protected String getSecurityDomainName() {
            return "jbossweb-form-auth";
        }

        @Override
        public void setup(final ManagementClient managementClient) throws Exception {
            final List<ModelNode> updates = new ArrayList<ModelNode>();

            ModelNode op = new ModelNode();
            op.get(OP).set(ADD);
            op.get(OP_ADDR).add(SUBSYSTEM, "security");
            op.get(OP_ADDR).add(SECURITY_DOMAIN, getSecurityDomainName());

            ModelNode rolesmodule = new ModelNode();
            rolesmodule.get(CODE).set("org.jboss.security.auth.spi.UsersRolesLoginModule");
            rolesmodule.get(FLAG).set("required");
            rolesmodule.get(MODULE_OPTIONS).add("unauthenticatedIdentity", "nobody");
            rolesmodule.get(MODULE_OPTIONS).add("usersProperties", "users.properties");
            rolesmodule.get(MODULE_OPTIONS).add("rolesProperties", "roles.properties");

            op.get(AUTHENTICATION).set(Arrays.asList(rolesmodule));
            updates.add(op);

            applyUpdates(managementClient.getControllerClient(), updates);
        }
    }

    DefaultHttpClient httpclient = new DefaultHttpClient();

    @Deployment(name="form-auth.war", testable = false)
    public static WebArchive deployment() {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        String resourcesLocation = "org/jboss/as/test/integration/web/formauth/resources/";

        WebArchive war = ShrinkWrap.create(WebArchive.class, "form-auth.war");
        war.setWebXML(tccl.getResource(resourcesLocation + "web.xml"));
        war.addAsWebInfResource(tccl.getResource(resourcesLocation + "jboss-web.xml"), "jboss-web.xml");

        war.addClass(SecureServlet.class);
        war.addClass(SecuredPostServlet.class);
        war.addClass(LogoutServlet.class);

        war.addAsWebResource(tccl.getResource(resourcesLocation + "index.html"), "index.html");
        war.addAsWebResource(tccl.getResource(resourcesLocation + "unsecure_form.html"), "unsecure_form.html");
        war.addAsWebResource(tccl.getResource(resourcesLocation + "restricted/errors.jsp"), "restricted/errors.jsp");
        war.addAsWebResource(tccl.getResource(resourcesLocation + "restricted/error.html"), "restricted/error.html");
        war.addAsWebResource(tccl.getResource(resourcesLocation + "restricted/login.html"), "restricted/login.html");

        log.info(war.toString(true));
        return war;
    }

    public static void applyUpdates(final List<ModelNode> updates, final ModelControllerClient client) throws Exception {
        for (ModelNode update : updates) {
            log.info("+++ Update on " + client + ":\n" + update.toString());
            ModelNode result = client.execute(new OperationBuilder(update).build());
            if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
                if (result.hasDefined("result"))
                    log.info(result.get("result"));
            } else if (result.hasDefined("failure-description")) {
                throw new RuntimeException(result.get("failure-description").toString());
            } else {
                throw new RuntimeException("Operation not successful; outcome = " + result.get("outcome"));
            }
        }
    }

    /**
     * Test form authentication of a secured servlet
     *
     * @throws Exception
     */
    @Test
    @OperateOnDeployment("form-auth.war")
    public void testFormAuth() throws Exception {
        log.info("+++ testFormAuth");
        doSecureGetWithLogin("restricted/SecuredServlet");
        /*
         * Access the resource without attempting a login to validate that the
         * session is valid and that any caching on the server is working as
         * expected.
         */
        doSecureGet("restricted/SecuredServlet");
    }

    /**
     * Test that a bad login is redirected to the errors.jsp and that the
     * session j_exception is not null.
     */
    @Test
    @OperateOnDeployment("form-auth.war")
    public void testFormAuthException() throws Exception {
        log.info("+++ testFormAuthException");

        URL url = new URL(baseURLNoAuth + "restricted/SecuredServlet");
        HttpGet httpget = new HttpGet(url.toURI());

        log.info("Executing request " + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);

        int statusCode = response.getStatusLine().getStatusCode();
        Header[] errorHeaders = response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);

        HttpEntity entity = response.getEntity();
        if ((entity != null) && (entity.getContentLength() > 0)) {
            String body = EntityUtils.toString(entity);
            assertTrue("Redirected to login page", body.indexOf("j_security_check") > 0);
        } else {
            fail("Empty body in response");
        }

        String sessionID = null;
        for (Cookie k : httpclient.getCookieStore().getCookies()) {
            if (k.getName().equalsIgnoreCase("JSESSIONID"))
                sessionID = k.getValue();
        }
        log.info("Saw JSESSIONID=" + sessionID);

        // Submit the login form
        HttpPost formPost = new HttpPost(baseURLNoAuth + "j_security_check");
        formPost.addHeader("Referer", baseURLNoAuth + "restricted/login.html");

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("j_username", "baduser"));
        formparams.add(new BasicNameValuePair("j_password", "badpass"));
        formPost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

        log.info("Executing request " + formPost.getRequestLine());
        HttpResponse postResponse = httpclient.execute(formPost);

        statusCode = postResponse.getStatusLine().getStatusCode();
        errorHeaders = postResponse.getHeaders("X-NoJException");
        assertTrue("Should see HTTP_OK. Got " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is not null", errorHeaders.length != 0);
        log.debug("Saw X-JException, " + Arrays.toString(errorHeaders));
    }

    /**
     * Test form authentication of a secured servlet and validate that there is
     * a SecurityAssociation setting Subject.
     */
    @Test
    @OperateOnDeployment("form-auth.war")
    public void testFormAuthSubject() throws Exception {
        log.info("+++ testFormAuthSubject");
        doSecureGetWithLogin("restricted/SecuredServlet");
    }

    /**
     * Test that a post from an unsecured form to a secured servlet does not
     * loose its data during the redirct to the form login.
     */
    @Test
    @OperateOnDeployment("form-auth.war")
    public void testPostDataFormAuth() throws Exception {
        log.info("+++ testPostDataFormAuth");

        URL url = new URL(baseURLNoAuth + "unsecure_form.html");
        HttpGet httpget = new HttpGet(url.toURI());

        log.info("Executing request " + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);

        int statusCode = response.getStatusLine().getStatusCode();
        Header[] errorHeaders = response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);
        EntityUtils.consume(response.getEntity());

        // Submit the form to /restricted/SecuredPostServlet
        HttpPost restrictedPost = new HttpPost(baseURLNoAuth + "restricted/SecuredPostServlet");

        List<NameValuePair> restrictedParams = new ArrayList<NameValuePair>();
        restrictedParams.add(new BasicNameValuePair("checkParam", "123456"));
        restrictedPost.setEntity(new UrlEncodedFormEntity(restrictedParams, "UTF-8"));

        log.info("Executing request " + restrictedPost.getRequestLine());
        HttpResponse restrictedResponse = httpclient.execute(restrictedPost);

        statusCode = restrictedResponse.getStatusLine().getStatusCode();
        errorHeaders = restrictedResponse.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);

        HttpEntity entity = restrictedResponse.getEntity();
        if ((entity != null) && (entity.getContentLength() > 0)) {
            String body = EntityUtils.toString(entity);
            assertTrue("Redirected to login page", body.indexOf("j_security_check") > 0);
        } else {
            fail("Empty body in response");
        }

        String sessionID = null;
        for (Cookie k : httpclient.getCookieStore().getCookies()) {
            if (k.getName().equalsIgnoreCase("JSESSIONID"))
                sessionID = k.getValue();
        }
        log.info("Saw JSESSIONID=" + sessionID);

        // Submit the login form
        HttpPost formPost = new HttpPost(baseURLNoAuth + "j_security_check");
        formPost.addHeader("Referer", baseURLNoAuth + "restricted/login.html");

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("j_username", "user1"));
        formparams.add(new BasicNameValuePair("j_password", "password1"));
        formPost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

        log.info("Executing request " + formPost.getRequestLine());
        HttpResponse postResponse = httpclient.execute(formPost);

        statusCode = postResponse.getStatusLine().getStatusCode();
        errorHeaders = postResponse.getHeaders("X-NoJException");
        assertTrue("Should see HTTP_MOVED_TEMP. Got " + statusCode, statusCode == HttpURLConnection.HTTP_MOVED_TEMP);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);
        EntityUtils.consume(postResponse.getEntity());

        // Follow the redirect to the SecureServlet
        Header location = postResponse.getFirstHeader("Location");
        URL indexURI = new URL(location.getValue());
        HttpGet war1Index = new HttpGet(url.toURI());

        log.info("Executing request " + war1Index.getRequestLine());
        HttpResponse war1Response = httpclient.execute(war1Index);

        statusCode = war1Response.getStatusLine().getStatusCode();
        errorHeaders = war1Response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);

        HttpEntity war1Entity = war1Response.getEntity();
        if ((war1Entity != null) && (entity.getContentLength() > 0)) {
            String body = EntityUtils.toString(war1Entity);
            if (body.indexOf("j_security_check") > 0)
                fail("Get of " + indexURI + " redirected to login page");
        } else {
            fail("Empty body in response");
        }
    }

    /**
     * Test that the war which uses <security-domain
     * flushOnSessionInvalidation="true"> in the jboss-web.xml does not have any
     * jaas security domain cache entries after the web session has been
     * invalidated.
     */
    // lbarerreiro: SKIPPED !!! No JMX connection on AS7
    // TODO: Other ways of getting this values !?!?
    @Ignore
    @Test
    public void testFlushOnSessionInvalidation() throws Exception {
        log.info("+++ testFlushOnSessionInvalidation");

        // MBeanServerConnection conn = (MBeanServerConnection) getServer();
        // ObjectName name = new
        // ObjectName("jboss.security:service=JaasSecurityManager");
        // JaasSecurityManagerServiceMBean secMgrService =
        // (JaasSecurityManagerServiceMBean)
        // MBeanServerInvocationHandler.newProxyInstance(conn, name,
        // JaasSecurityManagerServiceMBean.class, false);

        // Access a secured servlet to create a session and jaas cache entry
        doSecureGetWithLogin("restricted/SecuredServlet");

        // Validate that the jaas cache has 1 principal
        // List<Principal> principals =
        // secMgrService.getAuthenticationCachePrincipals("jbossweb-form-auth");
        // assertTrue("jbossweb-form-auth does not have one and only one principal",
        // principals.size() == 1);

        // Logout to clear the cache
        doSecureGet("Logout");
        // principals =
        // secMgrService.getAuthenticationCachePrincipals("jbossweb-form-auth");
        // log.info("jbossweb-form-auth principals = " +
        // Arrays.toString(principals.toArray()));
        // assertTrue("jbossweb-form-auth has cached principals",
        // principals.size() == 0);
    }

    public HttpPost doSecureGetWithLogin(String path) throws Exception {
        return doSecureGetWithLogin(path, "user2", "password2");
    }

    public HttpPost doSecureGetWithLogin(String path, String username, String password) throws Exception {
        log.info("+++ doSecureGetWithLogin : " + path);

        URL url = new URL(baseURLNoAuth + path);
        HttpGet httpget = new HttpGet(url.toURI());

        log.info("Executing request " + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);

        int statusCode = response.getStatusLine().getStatusCode();
        Header[] errorHeaders = response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);

        HttpEntity entity = response.getEntity();
        if ((entity != null) && (entity.getContentLength() > 0)) {
            String body = EntityUtils.toString(entity);
            assertTrue("Redirected to login page", body.indexOf("j_security_check") > 0);
        } else {
            fail("Empty body in response");
        }

        String sessionID = null;
        for (Cookie k : httpclient.getCookieStore().getCookies()) {
            if (k.getName().equalsIgnoreCase("JSESSIONID"))
                sessionID = k.getValue();
        }
        log.info("Saw JSESSIONID=" + sessionID);

        // Submit the login form
        HttpPost formPost = new HttpPost(baseURLNoAuth + "j_security_check");
        formPost.addHeader("Referer", baseURLNoAuth + "restricted/login.html");

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("j_username", username));
        formparams.add(new BasicNameValuePair("j_password", password));
        formPost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

        log.info("Executing request " + formPost.getRequestLine());
        HttpResponse postResponse = httpclient.execute(formPost);

        statusCode = postResponse.getStatusLine().getStatusCode();
        errorHeaders = postResponse.getHeaders("X-NoJException");
        assertTrue("Should see HTTP_MOVED_TEMP. Got " + statusCode, statusCode == HttpURLConnection.HTTP_MOVED_TEMP);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);
        EntityUtils.consume(postResponse.getEntity());

        // Follow the redirect to the SecureServlet
        Header location = postResponse.getFirstHeader("Location");
        URL indexURI = new URL(location.getValue());
        HttpGet war1Index = new HttpGet(url.toURI());

        log.info("Executing request " + war1Index.getRequestLine());
        HttpResponse war1Response = httpclient.execute(war1Index);

        statusCode = war1Response.getStatusLine().getStatusCode();
        errorHeaders = war1Response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);

        HttpEntity war1Entity = war1Response.getEntity();
        if ((war1Entity != null) && (entity.getContentLength() > 0)) {
            String body = EntityUtils.toString(war1Entity);
            if (body.indexOf("j_security_check") > 0)
                fail("Get of " + indexURI + " redirected to login page");
        } else {
            fail("Empty body in response");
        }

        return formPost;
    }

    public void doSecureGet(String path) throws Exception {
        log.info("+++ doSecureGet : " + path);

        String sessionID = null;
        for (Cookie k : httpclient.getCookieStore().getCookies()) {
            if (k.getName().equalsIgnoreCase("JSESSIONID"))
                sessionID = k.getValue();
        }
        log.info("Saw JSESSIONID=" + sessionID);

        URL url = new URL(baseURLNoAuth + path);
        HttpGet httpget = new HttpGet(url.toURI());

        log.info("Executing request" + httpget.getRequestLine());
        HttpResponse response = httpclient.execute(httpget);

        int statusCode = response.getStatusLine().getStatusCode();
        Header[] errorHeaders = response.getHeaders("X-NoJException");
        assertTrue("Wrong response code: " + statusCode, statusCode == HttpURLConnection.HTTP_OK);
        assertTrue("X-NoJException(" + Arrays.toString(errorHeaders) + ") is null", errorHeaders.length == 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7189.java