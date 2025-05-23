error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1788.java
text:
```scala
M@@odelNode ssl = createOpNode("subsystem=web/connector=testConnector/ssl=configuration", "add");

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

package org.jboss.as.test.integration.web.security.cert;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.test.integration.management.Connector;
import org.jboss.as.test.integration.web.security.SecuredServlet;
import org.jboss.as.test.integration.web.security.WebSecurityPasswordBasedBase;
import org.jboss.dmr.ModelNode;
import org.jboss.security.JBossJSSESecurityDomain;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.security.Constants.AUTHENTICATION;
import static org.jboss.as.security.Constants.CODE;
import static org.jboss.as.security.Constants.FLAG;
import static org.jboss.as.security.Constants.JSSE;
import static org.jboss.as.security.Constants.MODULE_OPTIONS;
import static org.jboss.as.security.Constants.SECURITY_DOMAIN;
import static org.jboss.as.security.Constants.TRUSTSTORE_PASSWORD;
import static org.jboss.as.security.Constants.TRUSTSTORE_URL;
import static org.jboss.as.test.integration.management.util.ModelUtil.createOpNode;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for CLIENT-CERT authentication.
 *
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
@Ignore("AS7-3837")
public class WebSecurityCERTTestCase {

    protected final String URL = "https://localhost:8380/" + getContextPath() + "/secured/";


    @Deployment
    public static WebArchive deployment() {
        // FIXME hack to get things prepared before the deployment happens
        try {
            //TODO: convert this to a ServerSetupTask when fixing the test
            final ModelControllerClient client = null;
            // create required security domains
            createSecurityDomains(client);
            // create the test connector
            createTestConnector(client);
        } catch (Exception e) {
            // ignore
        }

        WebArchive war = ShrinkWrap.create(WebArchive.class, "web-secure-client-cert.war");
        war.addClass(SecuredServlet.class);

        war.addAsWebInfResource(WebSecurityCERTTestCase.class.getPackage(), "jboss-web.xml", "jboss-web.xml");
        war.addAsWebInfResource(WebSecurityCERTTestCase.class.getPackage(), "web.xml", "web.xml");

        war.addAsResource(WebSecurityCERTTestCase.class.getPackage(), "users.properties", "users.properties");
        war.addAsResource(WebSecurityCERTTestCase.class.getPackage(), "roles.properties", "roles.properties");

        WebSecurityPasswordBasedBase.printWar(war);
        return war;
    }

    @AfterClass
    public static void after() throws Exception {
        final ModelControllerClient client = null;
        // and remove the connector again
        removeTestConnector(client);
        // remove test security domains
        removeSecurityDomains(client);
    }

    @Test
    public void testClientCertSuccessfulAuth() throws Exception {
        makeCall("test", 200);
    }

    @Test
    public void testClientCertUnsuccessfulAuth() throws Exception {
        makeCall("test2", 403);
    }

    protected void makeCall(String alias, int expectedStatusCode) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient = wrapClient(httpclient, alias);
        try {
            HttpGet httpget = new HttpGet(URL);

            HttpResponse response = httpclient.execute(httpget);

            StatusLine statusLine = response.getStatusLine();
            System.out.println("Response: " + statusLine);
            assertEquals(expectedStatusCode, statusLine.getStatusCode());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

    public static void createTestConnector(final ModelControllerClient client) throws Exception {
        final List<ModelNode> updates = new ArrayList<ModelNode>();
        ModelNode op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).add("socket-binding-group", "standard-sockets");
        op.get(OP_ADDR).add("socket-binding", "https-test");
        op.get("interface").set("default");
        op.get("port").set(8380);

        updates.add(op);
        final ModelNode composite = Util.getEmptyOperation(COMPOSITE, new ModelNode());
                    final ModelNode steps = composite.get(STEPS);
        op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).add(SUBSYSTEM, "web");
        op.get(OP_ADDR).add("connector", "testConnector");
        op.get("socket-binding").set("https-test");
        op.get("enabled").set(true);
        op.get("protocol").set("HTTP/1.1");
        op.get("scheme").set("https");
        op.get("secure").set(true);
        steps.add(op);
        ModelNode ssl = createOpNode("subsystem=web/connector=testConnector/configuration=ssl", "add");
        ssl.get("name").set("https-test");
        ssl.get("key-alias").set("test");
        ssl.get("password").set("changeit");
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        URL keystore = tccl.getResource("security/server.keystore");
        ssl.get("certificate-key-file").set(keystore.getPath());
        ssl.get("protocol").set("TLS");
        ssl.get("verify-client").set(true);
        steps.add(ssl);
        updates.add(composite);
        applyUpdates(updates, client);
    }

    public static void createSecurityDomains(final ModelControllerClient client) throws Exception {
        final List<ModelNode> updates = new ArrayList<ModelNode>();
        ModelNode op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).add(SUBSYSTEM, "security");
        op.get(OP_ADDR).add(SECURITY_DOMAIN, "cert-test");
        ModelNode loginModule = op.get(AUTHENTICATION).add();
        loginModule.get(CODE).set("CertificateRoles");
        loginModule.get(FLAG).set("required");
        ModelNode moduleOptions = loginModule.get(MODULE_OPTIONS);
        moduleOptions.add("securityDomain", "cert");
        updates.add(op);

        op = new ModelNode();
        op.get(OP).set(ADD);
        op.get(OP_ADDR).add(SUBSYSTEM, "security");
        op.get(OP_ADDR).add(SECURITY_DOMAIN, "cert");
        ModelNode jsse = op.get(JSSE);
        jsse.get(TRUSTSTORE_PASSWORD).set("changeit");
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        URL keystore = tccl.getResource("security/jsse.keystore");
        jsse.get(TRUSTSTORE_URL).set(keystore.getPath());
        updates.add(op);

        applyUpdates(updates, client);
    }

    public static void removeTestConnector(final ModelControllerClient client) throws Exception {
        final List<ModelNode> updates = new ArrayList<ModelNode>();
        ModelNode op = new ModelNode();
        op.get(OP).set(REMOVE);
        op.get(OP_ADDR).add(SUBSYSTEM, "web");
        op.get(OP_ADDR).add("connector", "testConnector");
        updates.add(op);

        op = new ModelNode();
        op.get(OP).set(REMOVE);
        op.get(OP_ADDR).add("socket-binding-group", "standard-sockets");
        op.get(OP_ADDR).add("socket-binding", "https-test");
        updates.add(op);

        applyUpdates(updates, client);
    }

    public static void removeSecurityDomains(final ModelControllerClient client) throws Exception {
        final List<ModelNode> updates = new ArrayList<ModelNode>();

        ModelNode op = new ModelNode();
        op.get(OP).set(REMOVE);
        op.get(OP_ADDR).add(SUBSYSTEM, "security");
        op.get(OP_ADDR).add(SECURITY_DOMAIN, "cert-test");
        updates.add(op);

        op = new ModelNode();
        op.get(OP).set(REMOVE);
        op.get(OP_ADDR).add(SUBSYSTEM, "security");
        op.get(OP_ADDR).add(SECURITY_DOMAIN, "cert");
        updates.add(op);

        applyUpdates(updates, client);
    }

    public static void applyUpdates(final List<ModelNode> updates, final ModelControllerClient client) throws Exception {
        for (ModelNode update : updates) {
            applyUpdate(update, client);
        }
    }

    public static void applyUpdate(ModelNode update, final ModelControllerClient client) throws Exception {
        ModelNode result = client.execute(new OperationBuilder(update).build());
        if (result.hasDefined("outcome") && "success".equals(result.get("outcome").asString())) {
            if (result.hasDefined("result")) {
                System.out.println(result.get("result"));
            }
        } else if (result.hasDefined("failure-description")) {
            throw new RuntimeException(result.get("failure-description").toString());
        } else {
            throw new RuntimeException("Operation not successful; outcome = " + result.get("outcome"));
        }
    }

    public String getContextPath() {
        return "web-secure-client-cert";
    }

    public static HttpClient wrapClient(HttpClient base, String alias) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            JBossJSSESecurityDomain jsseSecurityDomain = new JBossJSSESecurityDomain("client-cert");
            jsseSecurityDomain.setKeyStorePassword("changeit");
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            URL keystore = tccl.getResource("security/client.keystore");
            jsseSecurityDomain.setKeyStoreURL(keystore.getPath());
            jsseSecurityDomain.setClientAlias(alias);
            jsseSecurityDomain.reloadKeyAndTrustStore();
            KeyManager[] keyManagers = jsseSecurityDomain.getKeyManagers();
            TrustManager[] trustManagers = jsseSecurityDomain.getTrustManagers();
            ctx.init(keyManagers, trustManagers, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 8380, ssf));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1788.java