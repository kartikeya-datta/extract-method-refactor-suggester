error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4059.java
text:
```scala
a@@ssertTrue("Deployment also on default server. " , failed);

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

import org.jboss.as.test.integration.management.cli.GlobalOpsTestCase;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import java.util.concurrent.TimeUnit;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.as.test.integration.management.util.SimpleServlet;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import java.io.IOException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.management.base.AbstractMgmtTestBase;
import org.jboss.as.test.integration.management.util.MgmtOperationException;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.jboss.as.test.integration.management.util.ModelUtil.createOpNode;

/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class VirtualServerTestCase extends AbstractMgmtTestBase {

    @ArquillianResource
    URL url;

    private static String defaultHost;
    private static String virtualHost;
    private static final Logger log = Logger.getLogger(VirtualServerTestCase.class.getName());

    @Deployment(order=1)
    public static Archive<?> getDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "dummy.jar");
        ja.addClass(GlobalOpsTestCase.class);
        return ja;
    }

    @Deployment(managed=false, name="vsdeployment", order=2)
    public static Archive<?> getVDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "vsDeployment.war");
        war.addClass(SimpleServlet.class);
        war.addAsWebResource(new StringAsset("Virtual Server Deployment"), "index.html");
        war.addAsWebResource(new StringAsset("Rewrite Test"), "/rewritten/index.html");
        war.addAsWebInfResource(new StringAsset("<jboss-web><virtual-host>test</virtual-host></jboss-web>"), "jboss-web.xml");
        return war;
    }

    @Before
    public void before() throws IOException {
        initModelControllerClient(url.getHost(), MGMT_PORT);
    }

    @AfterClass
    public static void after() throws IOException {
        closeModelControllerClient();
    }

    @Test
    public void testDefaultVirtualServer() throws IOException, MgmtOperationException {

        // get default VS
        ModelNode result = executeOperation(createOpNode("subsystem=web/virtual-server=default-host", "read-resource"));

        // check VS
        assertTrue(result.get("alias").isDefined());
        assertTrue(result.get("default-web-module").asString().equals("ROOT.war"));
    }

    @Test
    public void addRemoveVirtualServer(@ArquillianResource Deployer deployer) throws Exception {

        if (! resolveHosts()) {
            log.info("Unable to resolve alternate server host name.");
            return;
        }
        addVirtualServer();

        // deploy to virtual server
        deployer.deploy("vsdeployment");

        // check the deployment is available on and only on virtual server
        URL vURL = new URL(url.getProtocol(), virtualHost, url.getPort(), "/vsDeployment/index.html");
        String response = HttpRequest.get(vURL.toString(), 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("Virtual Server Deployment") >=0);

        URL dURL = new URL(url.getProtocol(), defaultHost, url.getPort(), "/vsDeployment/index.html");
        boolean failed = false;
        try {
            response = HttpRequest.get(dURL.toString(), 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            failed = true;
        }
        assertTrue("Deployment also on defaul server. " , failed);

        // undeploy form virtual server
        deployer.undeploy("vsdeployment");

        // remove virtual server
        removeVirtualServer();

    }

    private void addVirtualServer() throws IOException, MgmtOperationException {
        ModelNode addOp = createOpNode("subsystem=web/virtual-server=test", "add");
        addOp.get("alias").add(virtualHost);

        ModelNode rewrite = new ModelNode();
        rewrite.get("condition").setEmptyList();
        rewrite.get("pattern").set("toberewritten");
        rewrite.get("substitution").set("rewritten");
        rewrite.get("flags").set("nocase");
        addOp.get("rewrite").add(rewrite);

        executeOperation(addOp);

    }

    private void removeVirtualServer() throws IOException, MgmtOperationException {

        executeOperation(createOpNode("subsystem=web/virtual-server=test", "remove"));

    }

    private boolean resolveHosts() {
        if (url.getHost().equals("localhost") || (url.getHost().equals("127.0.0.1"))) {
            defaultHost = "localhost";
            virtualHost = "127.0.0.1";
            return true;
        }

        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4059.java