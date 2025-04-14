error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6005.java
text:
```scala
a@@ssertTrue("Batch did not fail.", line.contains("Composite operation failed and was rolled back"));

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
package org.jboss.as.test.integration.management.cli;

import org.jboss.as.test.integration.management.base.AbstractCliTestBase;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.as.test.integration.management.util.SimpleServlet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.exporter.zip.ZipExporterImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 *
 * @author Dominik Pospisil <dpospisi@redhat.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BatchTestCase extends AbstractCliTestBase {

    private static WebArchive[] wars = new WebArchive[3];
    private static File[] warFiles = new File[3];

    @ArquillianResource URL url;

    @Deployment
    public static Archive<?> getDeployment() {
        JavaArchive ja = ShrinkWrap.create(JavaArchive.class, "dummy.jar");
        ja.addClass(BatchTestCase.class);
        return ja;
    }

    @BeforeClass
    public static void before() throws Exception {

        // deployment1 - correct
        wars[0] = ShrinkWrap.create(WebArchive.class, "deployment0.war");
        wars[0].addClass(SimpleServlet.class);
        wars[0].addAsWebResource(new StringAsset("Version0"), "page.html");
        String tempDir = System.getProperty("java.io.tmpdir");
        warFiles[0] = new File(tempDir + File.separator + "deployment0.war");
        new ZipExporterImpl(wars[0]).exportTo(warFiles[0], true);

        // deployment2 - correct
        wars[1] = ShrinkWrap.create(WebArchive.class, "deployment1.war");
        wars[1].addClass(SimpleServlet.class);
        wars[1].addAsWebResource(new StringAsset("Version1"), "page.html");
        warFiles[1] = new File(tempDir + File.separator + "deployment1.war");
        new ZipExporterImpl(wars[1]).exportTo(warFiles[1], true);

        // deployment3 - malformed
        wars[2] = ShrinkWrap.create(WebArchive.class, "deployment2.war");
        wars[2].addClass(SimpleServlet.class);
        wars[2].addAsWebInfResource(new StringAsset("Malformed"), "web.xml");
        warFiles[2] = new File(tempDir + File.separator + "deployment2.war");
        new ZipExporterImpl(wars[2]).exportTo(warFiles[2], true);

        AbstractCliTestBase.initCLI();
    }

    @AfterClass
    public static void after() throws Exception {
        for (File warFile : warFiles) warFile.delete();
        AbstractCliTestBase.closeCLI();
    }

    @Test
    public void testRunBatch() throws Exception {

        // test a batch with two deployments

        cli.sendLine("batch");
        cli.sendLine("deploy " + warFiles[0].getAbsolutePath());
        cli.sendLine("deploy " + warFiles[1].getAbsolutePath());

        // check none of the archives are deployed yet
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment0/SimpleServlet"));
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment1/SimpleServlet"));

        cli.sendLine("run-batch");

        String line = cli.readOutput();
        assertTrue(line.contains("The batch executed successfully"));

        // check that now both are deployed
        String response = HttpRequest.get(getBaseURL(url) + "deployment0/SimpleServlet", 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("SimpleServlet") >=0);
        response = HttpRequest.get(getBaseURL(url) + "deployment1/SimpleServlet", 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("SimpleServlet") >=0);

        cli.sendLine("batch");
        cli.sendLine("undeploy deployment0.war");
        cli.sendLine("undeploy deployment1.war");
        cli.sendLine("holdback-batch dbatch");
        cli.sendLine("batch dbatch");

        // check that both are still deployed
        response = HttpRequest.get(getBaseURL(url) + "deployment0/SimpleServlet", 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("SimpleServlet") >=0);
        response = HttpRequest.get(getBaseURL(url) + "deployment1/SimpleServlet", 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("SimpleServlet") >=0);

        cli.sendLine("run-batch");
        // check that both undeployed
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment0/SimpleServlet"));
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment1/SimpleServlet"));
    }

    @Test
    public void testRollbackBatch() throws Exception {

        // test rollback of a batch with corrupted deployment
        cli.sendLine("batch");
        cli.sendLine("deploy " + warFiles[0].getAbsolutePath());
        cli.sendLine("deploy " + warFiles[2].getAbsolutePath());

        // check none of the archives are deployed yet
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment0/SimpleServlet"));
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment2/SimpleServlet"));

        // this should fail
        cli.sendLine("run-batch", true);

        String line = cli.readOutput();
        assertTrue("Batch did not fail.", line.contains("Failed to execute batch"));

        // check that still none of the archives are deployed
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment0/SimpleServlet"));
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment2/SimpleServlet"));

        cli.sendLine("discard-batch");

        // check that the rollback was clean and we can redeploy correct artifact
        cli.sendLine("deploy " + warFiles[0].getAbsolutePath());
        //line = cli.readLine(1000);
        //assertTrue("Deployment failed: " + line, line.indexOf("deployed successfully") >= 0);
        String response = HttpRequest.get(getBaseURL(url) + "deployment0/SimpleServlet", 1000, 10, TimeUnit.SECONDS);
        assertTrue("Invalid response: " + response, response.indexOf("SimpleServlet") >=0);
        // undeploy
        cli.sendLine("undeploy deployment0.war");
        assertTrue(checkUndeployed(getBaseURL(url) + "deployment0/SimpleServlet"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6005.java