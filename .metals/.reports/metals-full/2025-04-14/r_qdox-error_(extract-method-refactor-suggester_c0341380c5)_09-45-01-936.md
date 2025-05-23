error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11156.java
text:
```scala
F@@ileUtils.copyURLToFile(ConnectorTestCase.class.getResource("test.keystore"), keyStoreFile);

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
package org.jboss.as.test.integration.management.api.web;

import java.io.File;
import java.io.IOException;
import org.apache.http.conn.ClientConnectionManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.util.HashSet;
import java.util.List;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.as.test.integration.management.cli.GlobalOpsTestCase;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.integration.management.api.AbstractMgmtTestBase;
import org.jboss.dmr.ModelNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ConnectorTestCase extends AbstractMgmtTestBase {

    private final File keyStoreFile = new File( System.getProperty("java.io.tmpdir"), "test.keystore"); 

    public enum Connector {

        HTTP("http", "http", "HTTP/1.1", false),
        HTTPS("http", "https", "HTTP/1.1", true),
        AJP("ajp", "http", "AJP/1.3", false);
        private final String name;
        private final String scheme;
        private final String protocol;
        private final boolean secure;

        private Connector(String name, String scheme, String protocol, boolean secure) {
            this.name = name;
            this.scheme = scheme;
            this.protocol = protocol;
            this.secure = secure;
        }

        final String getName() {
            return name;
        }

        final String getScheme() {
            return scheme;
        }

        final String getProtrocol() {
            return protocol;
        }

        final boolean isSecure() {
            return secure;
        }
    }
    @ArquillianResource
    URL url;

    @Deployment
    public static Archive<?> getDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "dummy.jar");
        ja.addClass(GlobalOpsTestCase.class);
        return ja;
    }

    @Before
    public void before() throws IOException {
        super.init(url.getHost(), MGMT_PORT);                
    }    

    @Test
    public void testDefaultConnectorList() throws Exception {

        // only http connector present as a default

        HashSet<String> connNames = getConnectorList();

        assertTrue("HTTP connector missing.", connNames.contains("http"));
        assertTrue(connNames.size() == 1);
    }

    @Test
    public void testHttpConnector() throws Exception {

        addConnector(Connector.HTTP);

        // check that the connector is live
        String cURL = "http://" + url.getHost() + ":8181";
        String response = HttpRequest.get(cURL, 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("JBoss") >= 0);

        removeConnector(Connector.HTTP);
    }

    @Test
    public void testHttpsConnector() throws Exception {
        
               
        FileUtils.copyURLToFile(ConnectorTestCase.class.getResource("/management/api/test.keystore"), keyStoreFile);        
        
        addConnector(Connector.HTTPS);

        // check that the connector is live
        String cURL = "https://" + url.getHost() + ":8181";
        HttpClient httpClient = wrapClient(new DefaultHttpClient());
        HttpGet get = new HttpGet(cURL);

        HttpResponse hr = httpClient.execute(get);
        String response = EntityUtils.toString(hr.getEntity());
        assertTrue("Invalid response: " + response, response.indexOf("JBoss") >= 0);

        removeConnector(Connector.HTTPS);
        
        if (keyStoreFile.exists()) keyStoreFile.delete();        
    }

    @Test
    public void testAjpConnector() throws Exception {
        addConnector(Connector.AJP);
        removeConnector(Connector.AJP);
    }

    @Test
    public void testAddAndRemoveRollbacks() throws Exception {
                
        // execute and rollback add socket
        ModelNode addSocketOp = getAddSocketBindingOp(Connector.HTTP);
        ModelNode ret = executeAndRollbackOperation(addSocketOp);
        assertTrue("failed".equals(ret.get("outcome").asString()));        

        // add socket again
        executeOperation(addSocketOp);

        // execute and rollback add connector
        ModelNode addConnectorOp = getAddConnectorOp(Connector.HTTP);
        ret = executeAndRollbackOperation(addConnectorOp);
        assertTrue("failed".equals(ret.get("outcome").asString()));        

        // add connector again
        executeOperation(addConnectorOp);
        
        // check it is listed
        assertTrue(getConnectorList().contains("test-" + Connector.HTTP.getName() + "-connector"));     
        
        // execute and rollback remove connector
        ModelNode removeConnOp = getRemoveConnectorOp(Connector.HTTP);
        ret = executeAndRollbackOperation(removeConnOp);
        assertTrue("failed".equals(ret.get("outcome").asString()));

        // execute remove connector again
        executeOperation(removeConnOp);
        
        Thread.sleep(5000);
        // check that the connector is not live
        String cURL = Connector.HTTP.getScheme() + "://" + url.getHost() + ":8181";

        assertTrue("Connector not removed.", testRequestFail(cURL));

        // execute and rollback remove socket binding        
        ModelNode removeSocketOp = getRemoveSocketBindingOp(Connector.HTTP);
        ret = executeAndRollbackOperation(removeSocketOp);
        assertTrue("failed".equals(ret.get("outcome").asString()));        

        // execute remove socket again
        executeOperation(removeSocketOp);
    }
    
    private void addConnector(Connector conn) throws Exception {

        // add socket binding
        ModelNode op = getAddSocketBindingOp(conn);
        executeOperation(op);

        // add connector
        op = getAddConnectorOp(conn);
        executeOperation(op);

        // check it is listed
        assertTrue(getConnectorList().contains("test-" + conn.getName() + "-connector"));
    }

    private ModelNode getAddSocketBindingOp(Connector conn) {
        ModelNode op = createOpNode("socket-binding-group=standard-sockets/socket-binding=test-" + conn.getName(), "add");
        op.get("port").set(8181);    
        return op;
    }
    
    private ModelNode getAddConnectorOp(Connector conn) {
        ModelNode op = createOpNode("subsystem=web/connector=test-" + conn.getName() + "-connector", "add");
        op.get("socket-binding").set("test-" + conn.getName());
        op.get("scheme").set(conn.getScheme());
        op.get("protocol").set(conn.getProtrocol());
        op.get("secure").set(conn.isSecure());
        op.get("enabled").set(true);
        if (conn.isSecure()) {
            ModelNode ssl = new ModelNode();
            ssl.get("certificate-key-file").set(keyStoreFile.getAbsolutePath());
            ssl.get("password").set("test123");
            op.get("ssl").set(ssl);
        }        
        return op;
    }
    
    
    private void removeConnector(Connector conn) throws Exception {

        // remove connector
        ModelNode op = getRemoveConnectorOp(conn);
        executeOperation(op);


        Thread.sleep(5000);
        // check that the connector is not live
        String cURL = conn.getScheme() + "://" + url.getHost() + ":8181";

        assertTrue("Connector not removed.", testRequestFail(cURL));

        // remove socket binding        
        op = getRemoveSocketBindingOp(conn);
        executeOperation(op);

    }
    
    private ModelNode getRemoveSocketBindingOp(Connector conn) {
        ModelNode op = createOpNode("socket-binding-group=standard-sockets/socket-binding=test-" + conn.getName(), "remove");
        return op;
    }
    
    private ModelNode getRemoveConnectorOp(Connector conn) {
        ModelNode op = createOpNode("subsystem=web/connector=test-" + conn.getName() + "-connector", "remove");
        return op;
    }    

    private HashSet<String> getConnectorList() throws Exception {

        ModelNode op = createOpNode("subsystem=web", "read-children-names");
        op.get("child-type").set("connector");
        ModelNode result = executeOperation(op);
        List<ModelNode> connectors = result.asList();
        HashSet<String> connNames = new HashSet<String>();
        for (ModelNode n : connectors) {
            connNames.add(n.asString());
        }

        return connNames;
    }

    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11156.java