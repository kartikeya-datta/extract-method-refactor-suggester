error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8404.java
text:
```scala
c@@ontext.installBundle(V200_JAR, deployer.getDeployment(V201_JAR));

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.test.integration.osgi.deployment;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.ClientConstants;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentHelper.ServerDeploymentException;
import org.jboss.as.test.integration.common.HttpRequest;
import org.jboss.as.test.integration.osgi.deployment.bundle.ServletV100;
import org.jboss.as.test.integration.osgi.deployment.bundle.ServletV101;
import org.jboss.as.test.integration.osgi.deployment.bundle.ServletV200;
import org.jboss.as.test.integration.osgi.deployment.suba.ResourceRevisionAccess;
import org.jboss.as.test.osgi.FrameworkUtils;
import org.jboss.osgi.metadata.ManifestBuilder;
import org.jboss.osgi.metadata.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.wiring.FrameworkWiring;

/**
 * Test simple OSGi bundle redeployment
 *
 * @author thomas.diesler@jboss.com
 * @since 29-Aug-2012
 */
@RunWith(Arquillian.class)
public class BundleReplaceTestCase {

    static final String BUNDLE_V100_WAB = "webapp-v100.wab";
    static final String BUNDLE_V101_WAB = "webapp-v101.wab";
    static final String WEBAPP_V200_WAR = "webapp-v200.war";
    static final String V100_JAR = "v100.jar";
    static final String V200_JAR = "v200.jar";
    static final String V201_JAR = "v201.jar";

    @ArquillianResource
    Deployer deployer;

    @ArquillianResource
    ManagementClient managementClient;

    @ArquillianResource
    BundleContext context;

    @Deployment
    public static Archive<?> getDeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "deployment-replace-tests");
        archive.addClasses(ServerDeploymentHelper.class, HttpRequest.class, FrameworkUtils.class);
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addImportPackages(ClientConstants.class, ModelControllerClient.class, ManagementClient.class, DeploymentPlanBuilder.class);
                builder.addImportPackages(FrameworkWiring.class);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void testRepeatedDeploy() throws Exception {
        ServerDeploymentHelper server = new ServerDeploymentHelper(managementClient.getControllerClient());
        String runtimeName = server.deploy(V100_JAR, deployer.getDeployment(V100_JAR));
        try {
            Bundle bundleA = FrameworkUtils.getBundles(context, V100_JAR, null)[0];
            Assert.assertEquals(V100_JAR, bundleA.getSymbolicName());
            try {
                server.deploy(V100_JAR, deployer.getDeployment(V100_JAR));
                Assert.fail("ServerDeploymentException expected");
            } catch (ServerDeploymentException e) {
                // expected
            }
        } finally {
            server.undeploy(runtimeName);
        }
    }

    @Test
    public void testRepeatedInstall() throws Exception {
        Bundle bundleA = context.installBundle(V100_JAR, deployer.getDeployment(V100_JAR));
        try {
            Assert.assertEquals(V100_JAR, bundleA.getSymbolicName());

            Bundle bundleB = context.installBundle(V100_JAR, deployer.getDeployment(V100_JAR));
            Assert.assertSame(bundleA, bundleB);
        } finally {
            bundleA.uninstall();
        }
    }

    @Test
    public void testInstallRuntimeNameExists() throws Exception {
        ServerDeploymentHelper server = new ServerDeploymentHelper(managementClient.getControllerClient());
        String runtimeName = server.deploy(V200_JAR, deployer.getDeployment(V200_JAR));
        try {
            try {
                context.installBundle(V200_JAR, deployer.getDeployment(V100_JAR));
                Assert.fail("BundleException expected");
            } catch (BundleException ex) {
                // expected
            }
        } finally {
            server.undeploy(runtimeName);
        }
    }

    @Test
    public void testDirectBundleReplace() throws Exception {
        ServerDeploymentHelper server = new ServerDeploymentHelper(managementClient.getControllerClient());
        String runtimeName = server.deploy(BUNDLE_V100_WAB, deployer.getDeployment(BUNDLE_V100_WAB));
        try {
            String result = performCall("simple-bundle", "simple", null);
            Assert.assertEquals("ServletV100", result);
            result = performCall("simple-bundle", "message.txt", null);
            Assert.assertEquals("Resource V1.0.0", result);

            runtimeName = server.replace(BUNDLE_V101_WAB, BUNDLE_V100_WAB, deployer.getDeployment(BUNDLE_V101_WAB), true);
            result = performCall("simple-bundle", "simple", null);
            Assert.assertEquals("ServletV101", result);
            result = performCall("simple-bundle", "message.txt", null);
            Assert.assertEquals("Resource V1.0.1", result);
        } finally {
            server.undeploy(runtimeName);
        }
    }

    @Test
    @Ignore("[AS7-6957] Cannot replace jar that bundle depends on")
    public void testDependentJarReplace() throws Exception {
        ServerDeploymentHelper server = new ServerDeploymentHelper(managementClient.getControllerClient());
        String jarName = server.deploy(V200_JAR, deployer.getDeployment(V200_JAR));
        String webappName = server.deploy(WEBAPP_V200_WAR, deployer.getDeployment(WEBAPP_V200_WAR));
        try {
            String result = performCall("webapp-v200", "simple", null);
            Assert.assertEquals("Revision deployment.v200.jar:main", result);
            result = performCall("webapp-v200", "message.txt", null);
            Assert.assertEquals("Resource V2.0.0", result);

            jarName = server.replace(V200_JAR, V201_JAR, deployer.getDeployment(V201_JAR), true);

            // The wiring should not be effected
            result = performCall("webapp-v200", "simple", null);
            Assert.assertEquals("Revision deployment.v200.jar:main", result);
            result = performCall("webapp-v200", "message.txt", null);
            Assert.assertEquals("Resource V2.0.0", result);
        } finally {
            server.undeploy(webappName);
            server.undeploy(jarName);
        }
    }

    private String performCall(String context, String pattern, String param) throws Exception {
        String urlspec = managementClient.getWebUri() + "/" + context + "/";
        URL url = new URL(urlspec + pattern + (param != null ? "?input=" + param : ""));
        return HttpRequest.get(url.toExternalForm(), 10, TimeUnit.SECONDS);
    }

    @Deployment(name = BUNDLE_V100_WAB, managed = false, testable = false)
    public static Archive<?> getWebApp100() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, BUNDLE_V100_WAB);
        archive.addClasses(ServletV100.class);
        archive.addAsResource(new StringAsset("Resource V1.0.0"), "message.txt");
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(BUNDLE_V100_WAB);
                builder.addBundleVersion("1.0.0");
                builder.addBundleManifestVersion(2);
                builder.addImportPackages(WebServlet.class, Servlet.class, HttpServlet.class);
                builder.addManifestHeader("Web-ContextPath", "/simple-bundle");
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = BUNDLE_V101_WAB, managed = false, testable = false)
    public static JavaArchive getWebApp101() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, BUNDLE_V101_WAB);
        archive.addClasses(ServletV101.class);
        archive.addAsResource(new StringAsset("Resource V1.0.1"), "message.txt");
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(BUNDLE_V101_WAB);
                builder.addBundleVersion("1.0.1");
                builder.addBundleManifestVersion(2);
                builder.addImportPackages(WebServlet.class, Servlet.class, HttpServlet.class);
                builder.addManifestHeader("Web-ContextPath", "/simple-bundle");
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = WEBAPP_V200_WAR, managed = false, testable = false)
    public static Archive<?> getWebV200War() {
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, WEBAPP_V200_WAR);
        archive.addClasses(ServletV200.class);
        archive.addAsWebResource(new StringAsset("Resource V2.0.0"), "message.txt");
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                ManifestBuilder builder = ManifestBuilder.newInstance();
                builder.addManifestHeader("Dependencies", "deployment.v200.jar");
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = V100_JAR, managed = false, testable = false)
    public static JavaArchive getV100Jar() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, V100_JAR);
        archive.setManifest(new Asset() {
            @Override
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(V100_JAR);
                builder.addBundleVersion("1.0.0");
                builder.addBundleManifestVersion(2);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = V200_JAR, managed = false, testable = false)
    public static JavaArchive getV200Jar() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, V200_JAR);
        archive.addClasses(ResourceRevisionAccess.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                ManifestBuilder builder = ManifestBuilder.newInstance();
                builder.addManifestHeader("Dependencies", "org.jboss.modules");
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = V201_JAR, managed = false, testable = false)
    public static JavaArchive getV201Jar() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, V201_JAR);
        archive.addClasses(ResourceRevisionAccess.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                ManifestBuilder builder = ManifestBuilder.newInstance();
                builder.addManifestHeader("Dependencies", "org.jboss.modules");
                return builder.openStream();
            }
        });
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8404.java