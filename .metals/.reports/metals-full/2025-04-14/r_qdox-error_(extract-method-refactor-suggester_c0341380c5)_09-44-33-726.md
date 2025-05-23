error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7052.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7052.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7052.java
text:
```scala
t@@hrows IOException {

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
package org.jboss.as.test.clustering.cluster.web;

import static org.jboss.as.test.clustering.ClusterTestUtil.waitForReplication;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.clustering.cluster.ClusterAbstractTestCase;
import org.jboss.as.test.clustering.single.web.SimpleServlet;
import org.jboss.as.test.http.util.HttpClientUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Validate the <distributable/> works for a two-node cluster.
 *
 * @author Paul Ferraro
 * @author Radoslav Husar
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ClusteredWebSimpleTestCase extends ClusterAbstractTestCase {

    private static final int REQUEST_DURATION = 10000;

    @Deployment(name = DEPLOYMENT_1, managed = false)
    @TargetsContainer(CONTAINER_1)
    public static Archive<?> deployment0() {
        return getDeployment();
    }

    @Deployment(name = DEPLOYMENT_2, managed = false)
    @TargetsContainer(CONTAINER_2)
    public static Archive<?> deployment1() {
        return getDeployment();
    }

    private static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "distributable.war");
        war.addClass(SimpleServlet.class);
        war.setWebXML(ClusteredWebSimpleTestCase.class.getPackage(), "web.xml");
        log.info(war.toString(true));
        return war;
    }

    @Test
    @OperateOnDeployment(DEPLOYMENT_1)
    public void testSerialized(@ArquillianResource(SimpleServlet.class) URL baseURL) throws IOException {
        DefaultHttpClient client = HttpClientUtils.relaxedCookieHttpClient();

        // returns the URL of the deployment (http://127.0.0.1:8180/distributable)
        String url = baseURL.toString();
        log.info("URL = " + url);

        try {
            HttpResponse response = client.execute(new HttpGet(url + "simple"));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(1, Integer.parseInt(response.getFirstHeader("value").getValue()));
            Assert.assertFalse(Boolean.valueOf(response.getFirstHeader("serialized").getValue()));
            response.getEntity().getContent().close();

            response = client.execute(new HttpGet(url + "simple"));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(2, Integer.parseInt(response.getFirstHeader("value").getValue()));
            // This won't be true unless we have somewhere to which to replicate
            Assert.assertTrue(Boolean.valueOf(response.getFirstHeader("serialized").getValue()));
            response.getEntity().getContent().close();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    @Test
    @OperateOnDeployment(DEPLOYMENT_2) // For change, operate on the 2nd deployment first
    public void testSessionReplication(
            @ArquillianResource(SimpleServlet.class) @OperateOnDeployment(DEPLOYMENT_1) URL baseURL1,
            @ArquillianResource(SimpleServlet.class) @OperateOnDeployment(DEPLOYMENT_2) URL baseURL2)
            throws IllegalStateException, IOException, InterruptedException {
        DefaultHttpClient client = HttpClientUtils.relaxedCookieHttpClient();

        String url1 = baseURL1.toString() + "simple";
        String url2 = baseURL2.toString() + "simple";

        try {
            HttpResponse response = client.execute(new HttpGet(url1));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(1, Integer.parseInt(response.getFirstHeader("value").getValue()));
            response.getEntity().getContent().close();

            // Lets do this twice to have more debug info if failover is slow.
            response = client.execute(new HttpGet(url1));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(2, Integer.parseInt(response.getFirstHeader("value").getValue()));
            response.getEntity().getContent().close();

            // Lets wait for the session to replicate
            waitForReplication(GRACE_TIME_TO_REPLICATE);

            // Now check on the 2nd server

            // Note that this DOES rely on the fact that both servers are running on the "same" domain,
            // which is '127.0.0.0'. Otherwise you will have to spoof cookies. @Rado
            response = client.execute(new HttpGet(url2));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(3, Integer.parseInt(response.getFirstHeader("value").getValue()));
            response.getEntity().getContent().close();

            // Lets do one more check.
            response = client.execute(new HttpGet(url2));
            Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            Assert.assertEquals(4, Integer.parseInt(response.getFirstHeader("value").getValue()));
            response.getEntity().getContent().close();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    /**
     * Test that a session is gracefully served when a clustered application is undeployed.
     */
    @Test
    public void testGracefulServeOnUndeploy(
            @ArquillianResource(SimpleServlet.class) @OperateOnDeployment(DEPLOYMENT_1) URL baseURL1)
            throws Exception {
        this.abstractGracefulServe(baseURL1, true);
    }

    /**
     * Test that a session is gracefully served when clustered AS instanced is shutdown.
     */
    @Test
    public void testGracefulServeOnShutdown(
            @ArquillianResource(SimpleServlet.class) @OperateOnDeployment(DEPLOYMENT_1) URL baseURL1)
            throws Exception {
        this.abstractGracefulServe(baseURL1, false);
    }

    private void abstractGracefulServe(URL baseURL1, boolean undeployOnly)
            throws Exception {

        final DefaultHttpClient client = HttpClientUtils.relaxedCookieHttpClient();
        String url1 = baseURL1.toString() + "simple";

        // Make sure a normal request will succeed
        HttpResponse response = client.execute(new HttpGet(url1));
        Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
        response.getEntity().getContent().close();

        // Send a long request - in parallel
        String longRunningUrl = url1 + "?" + SimpleServlet.REQUEST_DURATION_PARAM + "=" + REQUEST_DURATION;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<HttpResponse> future = executor.submit(new RequestTask(client, longRunningUrl));

        // Make sure long request has started
        Thread.sleep(1000);

        if (undeployOnly) {
            // Undeploy the app only.
            undeploy(DEPLOYMENT_1);
        } else {
            // Shutdown server.
            stop(CONTAINER_1);
        }

        // Get result of long request
        // This request should succeed since it initiated before server shutdown
        try {
            response = future.get();
            Assert.assertEquals("Request should succeed since it initiated before undeply or shutdown.",
                    HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
            response.getEntity().getContent().close();
        } catch (ExecutionException e) {
            e.printStackTrace(System.err);
            Assert.fail(e.getCause().getMessage());
        }

        if (undeployOnly) {
            // If we are only undeploying, then subsequent requests should return 404.
            response = client.execute(new HttpGet(url1));
            Assert.assertEquals("If we are only undeploying, then subsequent requests should return 404.",
                    HttpServletResponse.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
            response.getEntity().getContent().close();
        }
    }

    /**
     * Request task to request a long running URL and then undeploy / shutdown the server.
     */
    private class RequestTask implements Callable<HttpResponse> {

        private HttpClient client;
        private String url;

        RequestTask(HttpClient client, String url) {
            this.client = client;
            this.url = url;
        }

        @Override
        public HttpResponse call() throws Exception {
            try {
                return client.execute(new HttpGet(url));
            } finally {
                client.getConnectionManager().closeExpiredConnections();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7052.java