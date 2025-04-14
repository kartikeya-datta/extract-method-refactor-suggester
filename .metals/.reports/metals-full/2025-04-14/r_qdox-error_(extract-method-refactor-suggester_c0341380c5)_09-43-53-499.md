error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/862.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/862.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/862.java
text:
```scala
final A@@ppClientWrapper wrapper = new AppClientWrapper(archive, " -Dnode0=" + managementClient.getMgmtAddress() + " --ejb-client-properties=" + props, "client-override.jar", "");

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.test.integration.ee.appclient.basic;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.test.integration.ee.appclient.util.AppClientWrapper;
import org.jboss.as.test.shared.integration.ejb.security.CallbackHandler;
import org.jboss.ejb.client.EJBClient;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests that an application client can launch and conntect to a remote EJB
 *
 * @author Stuart Douglas
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SimpleApplicationClientTestCase {


    private static Archive archive;


    private static final String APP_NAME = "simple-app-client-test";

    private static final String MODULE_NAME = "ejb";

    @ArquillianResource
    private ManagementClient managementClient;

    @Deployment(testable = false)
    public static Archive<?> deploy() {
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, APP_NAME + ".ear");

        final JavaArchive lib = ShrinkWrap.create(JavaArchive.class, "lib.jar");
        lib.addClasses(AppClientSingletonRemote.class, AppClientWrapper.class, CallbackHandler.class);
        ear.addAsLibrary(lib);

        final JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, MODULE_NAME + ".jar");
        ejb.addClasses(SimpleApplicationClientTestCase.class, AppClientStateSingleton.class);
        ear.addAsModule(ejb);


        final JavaArchive appClient = ShrinkWrap.create(JavaArchive.class, "client-annotation.jar");
        appClient.addClasses(AppClientMain.class);
        appClient.addAsManifestResource(new StringAsset("Main-Class: " + AppClientMain.class.getName() + "\n"), "MANIFEST.MF");
        ear.addAsModule(appClient);


        final JavaArchive clientDD = ShrinkWrap.create(JavaArchive.class, "client-dd.jar");
        clientDD.addClasses(DescriptorClientMain.class);
        clientDD.addAsManifestResource(new StringAsset("Main-Class: " + DescriptorClientMain.class.getName() + "\n"), "MANIFEST.MF");
        clientDD.addAsManifestResource(SimpleApplicationClientTestCase.class.getPackage(), "application-client.xml", "application-client.xml");
        ear.addAsModule(clientDD);

        final JavaArchive clientOverride = ShrinkWrap.create(JavaArchive.class, "client-override.jar");
        clientOverride.addClasses(DescriptorClientMain.class);
        clientOverride.addAsManifestResource(new StringAsset("Main-Class: " + DescriptorClientMain.class.getName() + "\n"), "MANIFEST.MF");
        clientOverride.addAsManifestResource(SimpleApplicationClientTestCase.class.getPackage(), "application-client.xml", "application-client.xml");
        clientOverride.addAsManifestResource(SimpleApplicationClientTestCase.class.getPackage(), "jboss-client.xml", "jboss-client.xml");
        ear.addAsModule(clientOverride);

        archive = ear;
        return ear;
    }

    /**
     * Tests a simple app client that calls an ejb with its command line parameters
     */
    @Test
    public void simpleAppClientTest() throws Exception {
        final StatelessEJBLocator<AppClientSingletonRemote> locator = new StatelessEJBLocator(AppClientSingletonRemote.class, APP_NAME, MODULE_NAME, AppClientStateSingleton.class.getSimpleName(), "");
        final AppClientSingletonRemote remote = EJBClient.createProxy(locator);
        remote.reset();
        final AppClientWrapper wrapper = new AppClientWrapper(archive, "--host=remote://" + managementClient.getMgmtAddress() + ":4447", "client-annotation.jar", "cmdLineParam");
        try {
            final String result = remote.awaitAppClientCall();
            assertTrue("App client call failed. App client output: " + wrapper.readAllUnformated(1000), result != null);
            assertEquals("cmdLineParam", result);
        } finally {
            wrapper.quit();
        }
    }

    /**
     * Tests an app client with a deployment descriptor, that injects an env-entry and an EJB.
     *
     * @throws Exception
     */
    @Test
    public void descriptorBasedAppClientTest() throws Exception {
        final StatelessEJBLocator<AppClientSingletonRemote> locator = new StatelessEJBLocator(AppClientSingletonRemote.class, APP_NAME, MODULE_NAME, AppClientStateSingleton.class.getSimpleName(), "");
        final AppClientSingletonRemote remote = EJBClient.createProxy(locator);
        remote.reset();
        final AppClientWrapper wrapper = new AppClientWrapper(archive, "--host=remote://" + managementClient.getMgmtAddress() + ":4447", "client-dd.jar", "");
        try {
            final String result = remote.awaitAppClientCall();
            assertTrue("App client call failed. App client output: " + wrapper.readAllUnformated(1000), result != null);
            assertEquals("EnvEntry", result);
        } finally {
            wrapper.quit();
        }
    }

    /**
     * Tests an app client with a deployment descriptor, that injects an env-entry and an EJB.
     *
     * @throws Exception
     */
    @Test
    public void testAppClientJBossDescriptor() throws Exception {
        final StatelessEJBLocator<AppClientSingletonRemote> locator = new StatelessEJBLocator(AppClientSingletonRemote.class, APP_NAME, MODULE_NAME, AppClientStateSingleton.class.getSimpleName(), "");
        final AppClientSingletonRemote remote = EJBClient.createProxy(locator);
        remote.reset();
        URL props = getClass().getClassLoader().getResource("jboss-ejb-client.properties");
        final AppClientWrapper wrapper = new AppClientWrapper(archive, "--ejb-client-properties=" + props, "client-override.jar", "");
        try {
            final String result = remote.awaitAppClientCall();
            assertTrue("App client call failed. App client output: " + wrapper.readAllUnformated(1000), result != null);
            assertEquals("OverridenEnvEntry", result);
        } finally {
            wrapper.quit();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/862.java