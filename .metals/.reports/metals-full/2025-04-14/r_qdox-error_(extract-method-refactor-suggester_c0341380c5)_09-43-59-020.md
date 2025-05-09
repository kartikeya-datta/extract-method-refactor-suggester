error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1066
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14212.java
text:
```scala
public class SimpleManagementTestCase {

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

p@@ackage org.jboss.as.test.integration.osgi.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.osgi.parser.ModelConstants;
import org.jboss.as.test.osgi.OSGiManagementOperations;
import org.jboss.dmr.ModelNode;
import org.jboss.osgi.spi.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Version;

/**
 * Test OSGi management operations
 *
 * @author thomas.diesler@jboss.com
 * @author David Bosschaert
 * @since 06-Mar-2012
 */
@RunAsClient
@RunWith(Arquillian.class)
public class OSGiManagementTestCase {
    @ContainerResource
    ManagementClient managementClient;

    @Deployment(name = "test-bundle", managed = false, testable = false)
    public static JavaArchive createTestBundle() {
        return createTestBundle("test-bundle", "999");
    }

    @Deployment(name = "test-bundle2", managed = false, testable = false)
    public static JavaArchive createTestBundle2() {
        return createTestBundle("test-bundle2", "1.2.3.something");
    }

    private static JavaArchive createTestBundle(final String bsn, final String version) {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, bsn);
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(bsn);
                builder.addBundleVersion(version);
                builder.addBundleManifestVersion(2);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = "test-fragment", managed = false, testable = false)
    public static JavaArchive createTestFragment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "test-fragment");
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addFragmentHost("test-bundle");
                builder.addBundleManifestVersion(2);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void testActivationMode() throws Exception {
        boolean initialActivationState = isFrameworkActive();

        assertEquals("eager", OSGiManagementOperations.getActivationMode(getControllerClient()));
        try {
            assertTrue(OSGiManagementOperations.setActivationMode(getControllerClient(), "lazy"));
            assertEquals("lazy", OSGiManagementOperations.getActivationMode(getControllerClient()));

            assertEquals("This operation should not change the active status of the subsystem", initialActivationState, isFrameworkActive());
        } finally {
            // set it back
            assertTrue(OSGiManagementOperations.setActivationMode(getControllerClient(), "eager"));
        }
    }

    @Test
    public void testFrameworkProperties() throws Exception {
        boolean initialActivationState = isFrameworkActive();

        String propName = "testProp" + System.currentTimeMillis();
        assertTrue(OSGiManagementOperations.addProperty(getControllerClient(), propName, "testing123testing"));
        assertEquals("testing123testing", OSGiManagementOperations.readProperty(getControllerClient(), propName));
        assertTrue(OSGiManagementOperations.listProperties(getControllerClient()).contains(propName));
        assertTrue(OSGiManagementOperations.removeProperty(getControllerClient(), propName));
        assertFalse(OSGiManagementOperations.listProperties(getControllerClient()).contains(propName));

        assertEquals("This operation should not change the active status of the subsystem", initialActivationState, isFrameworkActive());
    }

    @Test
    public void testAddRemoveCapabilities() throws Exception {
        ensureFrameworkActive();

        String capabilityName = "org.jboss.test.testcap" + System.currentTimeMillis();
        String basedir = System.getProperty("jboss.dist");
        File targetdir = new File(basedir + "/bundles/" + capabilityName.replace(".", "/") + "/main");
        targetdir.mkdirs();

        File bundleFile = new File(targetdir, "testcapabilitybundle.jar");
        JavaArchive capabilityBundle = createTestBundle("capability-bundle", "1.0.1");
        capabilityBundle.as(ZipExporter.class).exportTo(bundleFile);

        assertTrue(OSGiManagementOperations.addCapability(getControllerClient(), capabilityName, 1));
        assertTrue(OSGiManagementOperations.listCapabilities(getControllerClient()).contains(capabilityName));

        Long capBundleId = OSGiManagementOperations.getBundleId(getControllerClient(), "capability-bundle", Version.parseVersion("1.0.1"));
        assertEquals("The capability bundle should not yet be added to the system, this requires a restart", new Long(-1), capBundleId);

        assertTrue(OSGiManagementOperations.removeCapability(getControllerClient(), capabilityName));
        assertFalse(OSGiManagementOperations.listCapabilities(getControllerClient()).contains(capabilityName));

        // clean up
        bundleFile.delete();
    }

    @Test
    public void testFrameworkActivation() throws Exception {
        boolean active = isFrameworkActive();
        if (!active) {
            OSGiManagementOperations.activateFramework(getControllerClient());
            assertTrue(isFrameworkActive());
        }
    }

    @Test
    public void testBundleRuntimeOperations(@ArquillianResource Deployer deployer) throws Exception {
        // No need to ensure the framework is active. If it isn't deploying a bundle
        // should trigger it into active mode.

        deployer.deploy("test-bundle");
        Long testBundleId = OSGiManagementOperations.getBundleId(getControllerClient(), "test-bundle", Version.parseVersion("999"));
        assertTrue(testBundleId > 0);

        ModelNode resultMap = OSGiManagementOperations.getBundleInfo(getControllerClient(), testBundleId);
        assertEquals(testBundleId.toString(), resultMap.get("id").asString());
        assertEquals("ACTIVE", resultMap.get("state").asString());
        assertEquals("1", resultMap.get("startlevel").asString());
        assertEquals("bundle", resultMap.get("type").asString());
        assertEquals("test-bundle", resultMap.get("symbolic-name").asString());
        assertEquals("999.0.0", resultMap.get("version").asString());

        assertTrue(OSGiManagementOperations.bundleStop(getControllerClient(), testBundleId));
        ModelNode resultMap3 = OSGiManagementOperations.getBundleInfo(getControllerClient(), testBundleId);
        assertEquals("RESOLVED", resultMap3.get("state").asString());

        assertTrue(OSGiManagementOperations.bundleStart(getControllerClient(), testBundleId));
        ModelNode resultMap2 = OSGiManagementOperations.getBundleInfo(getControllerClient(), testBundleId);
        assertEquals("ACTIVE", resultMap2.get("state").asString());

        deployer.deploy("test-fragment");
        Long testFragId = OSGiManagementOperations.getBundleId(getControllerClient(), "test-fragment", Version.parseVersion("0.0.0"));
        assertTrue(testFragId > 0);

        ModelNode resultMap4 = OSGiManagementOperations.getBundleInfo(getControllerClient(), testFragId);
        assertEquals(testFragId.toString(), resultMap4.get("id").asString());
        assertEquals("fragment", resultMap4.get("type").asString());
        assertEquals("test-fragment", resultMap4.get("symbolic-name").asString());
        assertEquals("0.0.0", resultMap4.get("version").asString());

        deployer.undeploy("test-fragment");
        deployer.undeploy("test-bundle");

        Long testBundleId2 = OSGiManagementOperations.getBundleId(getControllerClient(), "test-bundle", Version.parseVersion("999"));
        assertEquals("Bundle should have been undeployed", new Long(-1), testBundleId2);
        Long testFragId2 = OSGiManagementOperations.getBundleId(getControllerClient(), "test-fragment", Version.parseVersion("0.0.0"));
        assertEquals("Fragment should have been undeployed", new Long(-1), testFragId2);
    }

    @Test
    public void testStartLevel(@ArquillianResource Deployer deployer) throws Exception {
        ensureFrameworkActive();

        int initial = OSGiManagementOperations.getFrameworkStartLevel(getControllerClient());
        try {
            assertEquals(1, initial);

            deployer.deploy("test-bundle2");
            Long testBundleId = OSGiManagementOperations.getBundleId(getControllerClient(), "test-bundle2", Version.parseVersion("1.2.3.something"));
            assertTrue(testBundleId > 0);

            assertTrue(OSGiManagementOperations.bundleStart(getControllerClient(), testBundleId));
            ModelNode resultMap = OSGiManagementOperations.getBundleInfo(getControllerClient(), testBundleId);
            assertEquals("ACTIVE", resultMap.get(ModelConstants.STATE).asString());

            assertTrue(OSGiManagementOperations.setFrameworkStartLevel(getControllerClient(), 0));
            if (!waitForBundleStateAfterStartLevelChange("RESOLVED", testBundleId, 10000)) {
                fail("Bundle not RESOLVED");
            }
        } finally {
            assertTrue(OSGiManagementOperations.setFrameworkStartLevel(getControllerClient(), initial));
        }
    }

    private boolean waitForBundleStateAfterStartLevelChange(String state, long bundleId, int timeout) throws Exception {
        while (timeout > 0) {
            ModelNode node = OSGiManagementOperations.getBundleInfo(getControllerClient(), bundleId);
            if (state.equals(node.get(ModelConstants.STATE).asString())) {
                return true;
            }
            timeout -= 500;
            Thread.sleep(500);
        };
        return false;
    }

    private ModelControllerClient getControllerClient() {
        return managementClient.getControllerClient();
    }

    private boolean isFrameworkActive() throws Exception {
        return OSGiManagementOperations.listBundleIDs(getControllerClient()).size() > 0;
    }

    private void ensureFrameworkActive() throws Exception {
        boolean active = isFrameworkActive();
        if (!active) {
            OSGiManagementOperations.activateFramework(getControllerClient());
            assertTrue("Framework should be active", isFrameworkActive());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14212.java