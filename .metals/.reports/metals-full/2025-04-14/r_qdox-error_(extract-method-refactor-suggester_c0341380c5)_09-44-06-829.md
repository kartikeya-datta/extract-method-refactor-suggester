error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10386.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10386.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10386.java
text:
```scala
final M@@BeanServerConnection mbeanServer = JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remoting-jmx://127.0.0.1:9999")).getMBeanServerConnection();

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
package org.jboss.as.test.smoke.surefire.servermodule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.test.smoke.modular.utils.PollingUtils;
import org.jboss.as.test.smoke.modular.utils.ShrinkWrapUtils;
import org.jboss.as.test.smoke.surefire.servermodule.archive.sar.Simple;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.exporter.ExplodedExporter;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.as.arquillian.container.Authentication.getCallbackHandler;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;

/**
 * Tests deployment to a standalone server, both via the client API and by the
 * filesystem scanner.
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
@RunWith(Arquillian.class)
public class ServerInModuleDeploymentTestCase  {

    @Test
    public void testDeploymentStreamApi() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar",
                Simple.class.getPackage());
        final ServerDeploymentManager manager = ServerDeploymentManager.Factory
                .create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        try {
            testDeployments(client, new DeploymentExecutor() {

                @Override
                public void initialDeploy() {
                    final InputStream is = archive.as(ZipExporter.class).exportAsInputStream();
                    try {
                        Future<?> future = manager.execute(manager.newDeploymentPlan()
                                .add("test-deployment.sar", is).deploy("test-deployment.sar").build());
                        awaitDeploymentExecution(future);
                    } finally {
                        if(is != null) try {
                            is.close();
                        } catch (IOException ignore) {
                            //
                        }
                    }
                }

                @Override
                public void fullReplace() {
                    final InputStream is = archive.as(ZipExporter.class).exportAsInputStream();
                    try {
                        Future<?> future = manager.execute(manager.newDeploymentPlan()
                                .replace("test-deployment.sar", is).build());
                        awaitDeploymentExecution(future);
                    } finally {
                        if(is != null) try {
                            is.close();
                        } catch (IOException ignore) {
                            //
                        }
                    }
                }

                @Override
                public void undeploy() {
                    Future<?> future = manager.execute(manager.newDeploymentPlan().undeploy("test-deployment.sar")
                            .remove("test-deployment.sar").build());
                    awaitDeploymentExecution(future);
                }
            });
        } finally {
            if(client != null) try {
                client.close();
            } catch (IOException ignore) {
                //
            }
        }
    }

    @Test
    public void testDeploymentFileApi() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar",
                Simple.class.getPackage());
        final ServerDeploymentManager manager = ServerDeploymentManager.Factory
                .create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final File dir = new File("target/archives");
        dir.mkdirs();
        final File file = new File(dir, "test-deployment.sar");
        archive.as(ZipExporter.class).exportTo(file, true);

        final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        try {
            testDeployments(client, new DeploymentExecutor() {

                @Override
                public void initialDeploy() throws IOException {
                    Future<?> future = manager.execute(manager.newDeploymentPlan().add("test-deployment.sar", file)
                            .deploy("test-deployment.sar").build());
                    awaitDeploymentExecution(future);
                }

                @Override
                public void fullReplace() throws IOException {
                    Future<?> future = manager.execute(manager.newDeploymentPlan().replace("test-deployment.sar", file).build());
                    awaitDeploymentExecution(future);
                }

                @Override
                public void undeploy() {
                    Future<?> future = manager.execute(manager.newDeploymentPlan().undeploy("test-deployment.sar")
                            .remove("test-deployment.sar").build());
                    awaitDeploymentExecution(future);
                }
            });
        } finally {
            if(client != null) try {
                client.close();
            } catch (IOException ignore) {
                //
            }
        }
    }

    @Test
    public void testFilesystemScannerRegistration() throws Exception {
        final File deployDir = createDeploymentDir("dummy");

        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final String scannerName = "dummy";
        addDeploymentScanner(deployDir, client, scannerName, false);
        removeDeploymentScanner(client, scannerName);
        addDeploymentScanner(deployDir, client, scannerName, false);
        removeDeploymentScanner(client, scannerName);
    }

    @Test
    public void testFilesystemDeployment_Marker() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar",
                Simple.class.getPackage());
        final File dir = new File("target/archives");
        dir.mkdirs();
        final File file = new File(dir, "test-deployment.sar");
        archive.as(ZipExporter.class).exportTo(file, true);

        final File deployDir = createDeploymentDir("marker-deployments");

        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final String scannerName = "markerZips";
        addDeploymentScanner(deployDir, client, scannerName, false);

        try {
            final File target = new File(deployDir, "test-deployment.sar");
            final File deployed = new File(deployDir, "test-deployment.sar.deployed");
            Assert.assertFalse(target.exists());

            testDeployments(client, new DeploymentExecutor() {
                @Override
                public void initialDeploy() throws IOException {
                    // Copy file to deploy directory
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
                    // The test is going to call this as soon as the deployment
                    // sends a notification
                    // but often before the scanner has completed the process
                    // and deleted the
                    // .dodpeloy put down by initialDeploy(). So pause a bit to
                    // let that complete
                    // so we don't end up having our own file deleted
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!dodeploy.exists() && !isdeploying.exists()) {
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

                    if (dodeploy.exists()) {
                        Assert.fail("initialDeploy step did not complete in a reasonably timely fashion");
                    }

                    // Copy file to deploy directory again
                    initialDeploy();
                }

                @Override
                public void undeploy() {
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!dodeploy.exists() && !isdeploying.exists() && deployed.exists()) {
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
                    if (dodeploy.exists() || !deployed.exists()) {
                        Assert.fail("fullReplace step did not complete in a reasonably timely fashion");
                    }

                    // Delete file from deploy directory
                    deployed.delete();
                }
            });
        } finally {
            try {
                removeDeploymentScanner(client, scannerName);
            } catch (Exception e) {
            }
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void testFilesystemDeployment_Auto() throws Exception {
        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar",
                Simple.class.getPackage());
        final File dir = new File("target/archives");
        dir.mkdirs();
        final File file = new File(dir, "test-deployment.sar");
        archive.as(ZipExporter.class).exportTo(file, true);

        final File deployDir = createDeploymentDir("auto-deployments");

        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final String scannerName = "autoZips";
        addDeploymentScanner(deployDir, client, scannerName, true);

        try {
            final File target = new File(deployDir, "test-deployment.sar");
            final File deployed = new File(deployDir, "test-deployment.sar.deployed");
            Assert.assertFalse(target.exists());

            testDeployments(client, new DeploymentExecutor() {
                @Override
                public void initialDeploy() throws IOException {
                    // Copy file to deploy directory
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

                    Assert.assertTrue(file.exists());
                }

                @Override
                public void fullReplace() throws IOException {
                    // The test is going to call this as soon as the deployment
                    // sends a notification
                    // but often before the scanner has completed the process
                    // and deleted the
                    // .isdeploying put down by deployment scanner. So pause a bit to
                    // let that complete
                    // so we don't end up having our own file deleted
                    final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!isdeploying.exists()) {
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

                    if (isdeploying.exists()) {
                        Assert.fail("initialDeploy step did not complete in a reasonably timely fashion");
                    }

                    // Copy file to deploy directory again
                    initialDeploy();
                }

                @Override
                public void undeploy() {
                   final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!isdeploying.exists() && deployed.exists()) {
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
                    if (!deployed.exists()) {
                        Assert.fail("fullReplace step did not complete in a reasonably timely fashion");
                    }

                    // Delete file from deploy directory
                    target.delete();
                }
            });
        } finally {
            try {
                removeDeploymentScanner(client, scannerName);
            } catch (Exception e) {
            }
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }

    @Test
    public void testExplodedFilesystemDeployment() throws Exception {

        final File deployDir = createDeploymentDir("exploded-deployments");

        ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), 9999, getCallbackHandler());
        final String scannerName = "exploded";
        addDeploymentScanner(deployDir, client, scannerName, false);

        final JavaArchive archive = ShrinkWrapUtils.createJavaArchive("servermodule/test-deployment.sar",
                Simple.class.getPackage());
        final File dir = new File("target/archives");
        dir.mkdirs();
        archive.as(ExplodedExporter.class).exportExploded(deployDir);

        try {
            final File deployed = new File(deployDir, "test-deployment.sar.deployed");
            Assert.assertFalse(deployed.exists());

            testDeployments(client, new DeploymentExecutor() {
                @Override
                public void initialDeploy() throws IOException {

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
                    // The test is going to call this as soon as the deployment
                    // sends a notification
                    // but often before the scanner has completed the process
                    // and deleted the
                    // .dodpeloy put down by initialDeploy(). So pause a bit to
                    // let that complete
                    // so we don't end up having our own file deleted
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!dodeploy.exists() && !isdeploying.exists()) {
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

                    if (dodeploy.exists()) {
                        Assert.fail("initialDeploy step did not complete in a reasonably timely fashion");
                    }

                    // Copy file to deploy directory again
                    initialDeploy();
                }

                @Override
                public void undeploy() {
                    final File dodeploy = new File(deployDir, "test-deployment.sar.dodeploy");
                    final File isdeploying = new File(deployDir, "test-deployment.sar.isdeploying");
                    for (int i = 0; i < 500; i++) {
                        if (!dodeploy.exists() && !isdeploying.exists() && deployed.exists()) {
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
                    if (dodeploy.exists() || !deployed.exists()) {
                        Assert.fail("fullReplace step did not complete in a reasonably timely fashion");
                    }

                    // Delete file from deploy directory
                    deployed.delete();
                }
            });
        } finally {
            try {
                removeDeploymentScanner(client, scannerName);
            } catch (Exception e) {
            }
            try {
                client.close();
            } catch (Exception e) {
            }
        }
    }

    private ModelNode addDeploymentScanner(final File deployDir, final ModelControllerClient client, final String scannerName, final boolean autoDeployZipped)
            throws IOException {
        ModelNode add = new ModelNode();
        add.get(OP).set(ADD);
        ModelNode addr = new ModelNode();
        addr.add("subsystem", "deployment-scanner");
        addr.add("scanner", scannerName);
        add.get(OP_ADDR).set(addr);
        add.get("path").set(deployDir.getAbsolutePath());
        add.get("scan-enabled").set(true);
        add.get("scan-interval").set(1000);
        if (autoDeployZipped == false) {
            add.get("auto-deploy-zipped").set(false);
        }

        ModelNode result = client.execute(add);
        Assert.assertEquals(ModelDescriptionConstants.SUCCESS, result.require(ModelDescriptionConstants.OUTCOME).asString());
        return result;
    }

    private void removeDeploymentScanner(final ModelControllerClient client, final String scannerName) throws IOException {

        ModelNode addr = new ModelNode();
        addr.add("subsystem", "deployment-scanner");
        addr.add("scanner", scannerName);
        ModelNode op = new ModelNode();
        op.get(OP).set(REMOVE);
        op.get(OP_ADDR).set(addr);
        client.execute(op);
    }

    private File createDeploymentDir(String dir) {
        final File deployDir = new File("target", dir);
        cleanFile(deployDir);
        deployDir.mkdirs();
        Assert.assertTrue(deployDir.exists());
        return deployDir;
    }

    private void testDeployments(ModelControllerClient client, DeploymentExecutor deploymentExecutor) throws Exception {
        final MBeanServerConnection mbeanServer = JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:remote://127.0.0.1:9999")).getMBeanServerConnection();
        final ObjectName name = new ObjectName("jboss.test:service=testdeployments");

        // NOTE: Use polling until we have jmx over remoting
        // final TestNotificationListener listener = new TestNotificationListener(name);
        // mbeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, listener, null, null);
        try {
            // Initial deploy
            deploymentExecutor.initialDeploy();
            PollingUtils.retryWithTimeout(10000, new PollingUtils.WaitForMBeanTask(mbeanServer, name));

            //listener.await();
            Assert.assertNotNull(mbeanServer.getMBeanInfo(name));

            // Full replace
            // listener.reset(2);
            deploymentExecutor.fullReplace();
            PollingUtils.retryWithTimeout(10000, new PollingUtils.WaitForMBeanTask(mbeanServer, name));

            // listener.await();
            Assert.assertNotNull(mbeanServer.getMBeanInfo(name));

            // Undeploy
            // listener.reset(1);
            deploymentExecutor.undeploy();
            // listener.await();
            try {
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < 10000) {
                    mbeanServer.getMBeanInfo(name);
                    Thread.sleep(100);
                }
                Assert.fail("Should not have found MBean");
            } catch (Exception expected) {
            }
        } finally {
            //mbeanServer.removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, listener);
        }

    }

    private void awaitDeploymentExecution(Future<?> future) {
        try {
            future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
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
            if (!(notification instanceof MBeanServerNotification)) {
                return;
            }

            MBeanServerNotification mBeanServerNotification = (MBeanServerNotification) notification;
            if (!name.equals(mBeanServerNotification.getMBeanName())) {
                return;
            }

            if (MBeanServerNotification.REGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())) {
                latch.countDown();
            } else if (MBeanServerNotification.UNREGISTRATION_NOTIFICATION.equals(mBeanServerNotification.getType())) {
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
            //there seems to be a race condition where
            Thread.sleep(200);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10386.java