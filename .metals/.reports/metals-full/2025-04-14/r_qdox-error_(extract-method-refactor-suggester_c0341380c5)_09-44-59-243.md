error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6673.java
text:
```scala
final F@@ile deployed = new File(deployDir, "test-deployment.sar.deployed");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.surefire.servermodule;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import junit.framework.Assert;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.server.Bootstrap;
import org.jboss.as.server.EmbeddedServerFactory;
import org.jboss.as.server.Main;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.test.modular.utils.ShrinkWrapUtils;
import org.jboss.as.test.surefire.servermodule.archive.sar.Simple;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Here to prove the forked surefire plugin is capable of running
 * modular tests. This plugin will load up this test class in a module that can see
 * org.jboss.as.standalone.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ServerInModuleStartupTestCase {

    static ServiceContainer container;
    @BeforeClass
    public static void startServer() throws Exception {

        EmbeddedServerFactory.setupCleanDirectories(System.getProperties());

        ServerEnvironment serverEnvironment = Main.determineEnvironment(new String[0], new Properties(System.getProperties()), System.getenv());
        Assert.assertNotNull(serverEnvironment);
        final Bootstrap bootstrap = Bootstrap.Factory.newInstance();
        final Bootstrap.Configuration configuration = new Bootstrap.Configuration();
        configuration.setServerEnvironment(serverEnvironment);
        configuration.setModuleLoader(Module.getBootModuleLoader());
        configuration.setPortOffset(0);

        container = bootstrap.startup(configuration, Collections.<ServiceActivator>emptyList()).get();
        Assert.assertNotNull(container);
    }

    @AfterClass
    public static void testServerStartupAndShutDown() throws Exception {
        container.shutdown();
        container.awaitTermination();
        Assert.assertTrue(container.isShutdownComplete());
    }

    @Test
    public void testXmlConfigDemo() throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999);
        try {
            ModelNode request = new ModelNode();
            request.get("operation").set("read-config-as-xml");
            request.get("address").setEmptyList();
            ModelNode r = client.execute(OperationBuilder.Factory.create(request).build());

            Assert.assertEquals(SUCCESS, r.require(OUTCOME).asString());
        } finally {
            StreamUtils.safeClose(client);
        }
    }

    @Test
    public void testDescriptionDemo() throws Exception {
        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999);
        try {
            ModelNode request = new ModelNode();
            request.get("operation").set("read-resource");
            request.get("address").setEmptyList();
            request.get("recursive").set(true);
            ModelNode r = client.execute(OperationBuilder.Factory.create(request).build());

            Assert.assertEquals(SUCCESS, r.require(OUTCOME).asString());

            request = new ModelNode();
            request.get("operation").set("read-resource-description");
            request.get("address").setEmptyList();
            request.get("recursive").set(true);
            r = client.execute(OperationBuilder.Factory.create(request).build());

            Assert.assertEquals(SUCCESS, r.require(OUTCOME).asString());
        } finally {
            StreamUtils.safeClose(client);
        }
    }

    @Test
    public void testDeploymentStreamApi() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar", Simple.class.getPackage());
        final ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(InetAddress.getByName("localhost"), 9999);

        testDeployments(new DeploymentExecutor() {

            @Override
            public void initialDeploy() {
                manager.execute(manager.newDeploymentPlan().add("test-deployment.sar", archive.as(ZipExporter.class).exportZip()).deploy("test-deployment.sar").build());
            }

            @Override
            public void fullReplace() {
                manager.execute(manager.newDeploymentPlan().replace("test-deployment.sar", archive.as(ZipExporter.class).exportZip()).build());
            }

            @Override
            public void undeploy() {
                manager.execute(manager.newDeploymentPlan().undeploy("test-deployment.sar").remove("test-deployment.sar").build());
            }
        });
    }

    @Test
    public void testDeploymentFileApi() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar", Simple.class.getPackage());
        final ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(InetAddress.getByName("localhost"), 9999);
        final File dir = new File("target/archives");
        dir.mkdirs();
        final File file = new File(dir, "test-deployment.sar");
        archive.as(ZipExporter.class).exportZip(file, true);

        testDeployments(new DeploymentExecutor() {

            @Override
            public void initialDeploy() throws IOException{
                manager.execute(manager.newDeploymentPlan().add("test-deployment.sar", file).deploy("test-deployment.sar").build());
            }

            @Override
            public void fullReplace() throws IOException {
                manager.execute(manager.newDeploymentPlan().replace("test-deployment.sar", file).build());
            }

            @Override
            public void undeploy() {
                manager.execute(manager.newDeploymentPlan().undeploy("test-deployment.sar").remove("test-deployment.sar").build());
            }
        });
    }

    @Test
    public void testFilesystemDeployment() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar", Simple.class.getPackage());
        final File dir = new File("target/archives");
        dir.mkdirs();
        final File file = new File(dir, "test-deployment.sar");
        archive.as(ZipExporter.class).exportZip(file, true);


        final File deployDir = new File("target", "deployments");
        cleanFile(deployDir);
        deployDir.mkdirs();
        Assert.assertTrue(deployDir.exists());

        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999);
        ModelNode add = new ModelNode();
        add.get(OP).set(ADD);
        ModelNode addr = new ModelNode();
        addr.add("subsystem", "deployment-scanner");
        addr.add("scanner", "test");
        add.get(OP_ADDR).set(addr);
        add.get("path").set(deployDir.getAbsolutePath());
        add.get("scan-enabled").set(true);
        add.get("scan-interval").set(1000);

        ModelNode result = client.execute(add);
        Assert.assertEquals(ModelDescriptionConstants.SUCCESS, result.require(ModelDescriptionConstants.OUTCOME).asString());

        try {
            final File target = new File(deployDir, "test-deployment.sar");
            final File deployed = new File(deployDir, "test-deployment.sar.isdeployed");
            Assert.assertFalse(target.exists());

            testDeployments(new DeploymentExecutor() {
                @Override
                public void initialDeploy() throws IOException {
                    //Copy file to deploy directory
                    final InputStream in = new BufferedInputStream(new FileInputStream(file));
                    try {
                        final OutputStream out = new BufferedOutputStream(new FileOutputStream(target));
                        try {
                            int i = in.read();
                            while (i != -1) {
                                out.write(i);
                                i = in.read();
                            }
                        } finally {
                            StreamUtils.safeClose(out);
                        }
                    } finally {
                        StreamUtils.safeClose(in);
                    }
                    // Create the .dodeploy file
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    final OutputStream out = new BufferedOutputStream(new FileOutputStream(dodeploy));
                    try {
                        out.write("test-deployment.sar".getBytes());
                    } finally {
                        StreamUtils.safeClose(out);
                    }
                    Assert.assertTrue(dodeploy.exists());
                }

                @Override
                public void fullReplace() throws IOException {
                    // The test is going to call this as soon as the deployment sends a notification
                    // but often before the scanner has completed the process and deleted the
                    // .dodpeloy put down by initialDeploy(). So pause a bit to let that complete
                    // so we don't end up having our own file deleted
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    for (int i = 0; i < 100; i++) {
                        if (!dodeploy.exists()) {
                            break;
                        }
                        // Wait for the last action to complete :(
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    //Copy file to deploy directory again
                    initialDeploy();
                }

                @Override
                public void undeploy() {
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    for (int i = 0; i < 100; i++) {
                        if (!dodeploy.exists() && deployed.exists()) {
                            break;
                        }
                        // Wait for the last action to complete :(
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    //Delete file from deploy directory
                    deployed.delete();
                }
            });
        } finally {
            try {
                client.execute(result.get(ModelDescriptionConstants.COMPENSATING_OPERATION));
            } catch (Exception e) {
                client.close();
            }
        }
    }

    private void testDeployments(DeploymentExecutor deploymentExecutor) throws Exception {
        final MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        final ObjectName name = new ObjectName("jboss.test:service=testdeployments");
        final TestNotificationListener listener = new TestNotificationListener(name);
        mbeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, listener, null, null);
        try {
            //Initial deploy
            deploymentExecutor.initialDeploy();
            listener.await();
            Assert.assertNotNull(mbeanServer.getMBeanInfo(name));

            //Full replace
            listener.reset(2);
            deploymentExecutor.fullReplace();
            listener.await();
            Assert.assertNotNull(mbeanServer.getMBeanInfo(name));

            //Undeploy
            listener.reset(1);
            deploymentExecutor.undeploy();
            listener.await();
            try {
                mbeanServer.getMBeanInfo(name);
                Assert.fail("Should not have found MBean");
            } catch (InstanceNotFoundException expected) {
            }
        } finally {
            mbeanServer.removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, listener);
        }

    }

    private static void cleanFile(File toClean) {
        if (toClean.isDirectory()) {
            for (File child : toClean.listFiles())
                cleanFile(child);
        }
        toClean.delete();
    }

    private interface DeploymentExecutor {
        void initialDeploy() throws IOException;
        void fullReplace() throws IOException;
        void undeploy() throws IOException;
    }

    private static class TestNotificationListener implements NotificationListener {
        private final ObjectName name;
        private volatile CountDownLatch latch = new CountDownLatch(1);

        private TestNotificationListener(ObjectName name) {
            this.name = name;
        }

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification instanceof MBeanServerNotification == false) {
                return;
            }

            MBeanServerNotification mBeanServerNotification = (MBeanServerNotification)notification;
            if (!name.equals(mBeanServerNotification.getMBeanName())) {
                return;
            }

            if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())){
                latch.countDown();
            } else if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())){
                latch.countDown();
            }
        }

        void reset(int i) {
            latch = new CountDownLatch(i);
        }
        void await() throws Exception {
            if (!latch.await(20, TimeUnit.SECONDS)) {
                Assert.fail("Timed out waiting for registration/unregistration");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6673.java