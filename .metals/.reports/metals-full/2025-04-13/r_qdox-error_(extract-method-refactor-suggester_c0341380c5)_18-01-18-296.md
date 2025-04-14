error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/864.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/864.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/864.java
text:
```scala
U@@RL servletURL = new URL("http://" + managementClient.getMgmtAddress() + ":8080/" + context);

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
package org.jboss.as.test.integration.jsr88;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarOutputStream;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.shared.StateType;
import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.ee.deployment.spi.DeploymentManagerImpl;
import org.jboss.as.ee.deployment.spi.DeploymentMetaData;
import org.jboss.as.ee.deployment.spi.JarUtils;
import org.jboss.as.ee.deployment.spi.factories.DeploymentFactoryImpl;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.as.test.http.Authentication.PASSWORD;
import static org.jboss.as.test.http.Authentication.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Deployment API JSR-88 tests
 *
 * @author Thomas.Diesler@jboss.com
 * @since 01-Feb-2012
 */
@RunAsClient
@RunWith(Arquillian.class)
public class DeploymentTestCase {

    private static final long TIMEOUT = 10000;
    private static final String WAR_JBOSS_FILE = "jboss-web.xml";
    private static final String JAR_JBOSS_FILE = "jboss.xml";
    private static final String EAR_JBOSS_FILE = "jboss-app.xml";

    @ArquillianResource
    private ManagementClient managementClient;

    @Deployment
    public static Archive<?> fakeDeployment() {
        return ShrinkWrap.create(JavaArchive.class);
    }

    @Test
    public void testDeployUndeployEAR() throws Exception {

        // [AS7-3474] JSR88 undeployment does not work

        DeploymentManager manager = getDeploymentManager();
        ProgressObject progress = jsr88Deploy(manager, getEarArchive());

        DeploymentStatus state = progress.getDeploymentStatus();
        assertEquals(StateType.COMPLETED, state.getState());
        assertServletAccess("custom-context");

        Target[] targets = manager.getTargets();
        TargetModuleID[] targetModules = manager.getAvailableModules(ModuleType.EAR, targets);
        assertEquals(1, targetModules.length);

        jsr88Undeploy(manager, targetModules);

        try {
            assertServletAccess("custom-context");
            fail("Test deployment not undeployed");
        } catch (Exception e) {
            // ignore
        }
    }

    private DeploymentManager getDeploymentManager() throws Exception {
        String uri = DeploymentManagerImpl.DEPLOYER_URI + "?targetType=as7&serverHost=" + managementClient.getMgmtAddress() + "&serverPort=" + managementClient.getMgmtPort();
        DeploymentFactoryImpl.register();
        DeploymentFactoryManager dfManager = DeploymentFactoryManager.getInstance();
        DeploymentFactory[] factories = dfManager.getDeploymentFactories();
        DeploymentManager deploymentManager = factories[0].getDeploymentManager(uri, USERNAME, PASSWORD);
        return deploymentManager;
    }

    private ProgressObject jsr88Deploy(DeploymentManager manager, Archive<?> archive) throws Exception {
        Target[] targets = manager.getTargets();
        assertEquals(1, targets.length);

        InputStream deploymentPlan = createDeploymentPlan(archive.getName());

        // Deploy the test archive
        InputStream inputStream = archive.as(ZipExporter.class).exportAsInputStream();
        ProgressObject progress = manager.distribute(targets, inputStream, deploymentPlan);
        StateType state = awaitCompletion(progress, TIMEOUT);

        if (state == StateType.COMPLETED) {
            progress = manager.start(progress.getResultTargetModuleIDs());
            awaitCompletion(progress, TIMEOUT);
        }

        return progress;
    }

    private ProgressObject jsr88Undeploy(DeploymentManager manager, TargetModuleID[] resultTargetModuleIDs) throws Exception {
        Target[] targets = manager.getTargets();
        assertEquals(1, targets.length);

        ProgressObject progress = manager.stop(resultTargetModuleIDs);
        awaitCompletion(progress, TIMEOUT);

        progress = manager.undeploy(resultTargetModuleIDs);
        awaitCompletion(progress, TIMEOUT);

        return progress;
    }

    private StateType awaitCompletion(ProgressObject progress, long timeout) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        progress.addProgressListener(new ProgressListener() {
            public void handleProgressEvent(ProgressEvent event) {
                DeploymentStatus status = event.getDeploymentStatus();
                if (status.isCompleted() || status.isFailed()) {
                    latch.countDown();
                }
            }
        });

        final DeploymentStatus status = progress.getDeploymentStatus();
        if (status.isCompleted())
            return status.getState();

        if (latch.await(timeout, TimeUnit.MILLISECONDS) == false)
            throw new IllegalStateException("Deployment timeout: " + progress);

        return status.getState();
    }

    private void assertServletAccess(String context) throws IOException {
        // Check that we can access the servlet
        URL servletURL = new URL("http://localhost:8080/" + context);
        BufferedReader br = new BufferedReader(new InputStreamReader(servletURL.openStream()));
        String message = br.readLine();
        assertEquals("Hello World!", message);
    }

    private InputStream createDeploymentPlan(String deploymentFile) throws Exception {

        boolean webInf = false;
        String jbossDescriptorName = null;
        if (deploymentFile.endsWith(".war")) {
            jbossDescriptorName = WAR_JBOSS_FILE;
            webInf = true;
        } else if (deploymentFile.endsWith(".jar")) {
            jbossDescriptorName = JAR_JBOSS_FILE;
        } else if (deploymentFile.endsWith(".ear")) {
            jbossDescriptorName = EAR_JBOSS_FILE;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JarOutputStream plan = new JarOutputStream(baos);

        URL descriptorURL = getClass().getClassLoader().getResource(DeploymentTestCase.class.getPackage().getName().replace(".", "/") + "/" + jbossDescriptorName);
        File jbossDescriptorFile = new File(descriptorURL.getPath());
        JarUtils.addJarEntry(plan, "!/" + (webInf ? "WEB-INF/" : "META-INF/") + jbossDescriptorName, new FileInputStream(jbossDescriptorFile));

        // Setup deployment plan meta data with propriatary descriptor
        DeploymentMetaData metaData = new DeploymentMetaData(deploymentFile);

        metaData.addEntry(deploymentFile, jbossDescriptorName);

        // Add the meta data to the deployment plan
        String metaStr = metaData.toXMLString();
        JarUtils.addJarEntry(plan, DeploymentMetaData.ENTRY_NAME, new ByteArrayInputStream(metaStr.getBytes()));

        plan.flush();
        plan.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private Archive<?> getWebArchive() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, "deployment-web.war");
        archive.addClasses(SampleServlet.class);
        archive.addAsWebInfResource(DeploymentTestCase.class.getPackage(), "web.xml", "web.xml");
        return archive;
    }

    private Archive<?> getEjbArchive() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "deployment-ejb.jar");
        archive.addClasses(Echo.class, EchoHome.class, EchoBean.class);
        archive.addAsManifestResource(DeploymentTestCase.class.getPackage(), "ejb-jar.xml", "ejb-jar.xml");
        return archive;
    }

    private Archive<?> getEarArchive() {
        EnterpriseArchive archive = ShrinkWrap.create(EnterpriseArchive.class, "deployment-app.ear");
        archive.setApplicationXML(DeploymentTestCase.class.getPackage(), "application.xml");
        archive.add(getWebArchive(), "/", ZipExporter.class);
        archive.add(getEjbArchive(), "/", ZipExporter.class);
        return archive;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/864.java