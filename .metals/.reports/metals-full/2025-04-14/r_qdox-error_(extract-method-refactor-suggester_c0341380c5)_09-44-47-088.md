error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14379.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14379.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14379.java
text:
```scala
b@@uilder.addImportPackages(Logger.class, PackageAdmin.class, Module.class, ServiceContainer.class);

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

package org.jboss.as.test.integration.osgi.xservice;

import java.io.InputStream;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.integration.osgi.api.Echo;
import org.jboss.as.test.integration.osgi.xservice.bundle.TargetBundleActivator;
import org.jboss.as.test.integration.osgi.xservice.module.ClientModuleTwoActivator;
import org.jboss.as.test.osgi.FrameworkUtils;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController.State;
import org.jboss.msc.service.ServiceName;
import org.jboss.osgi.metadata.ManifestBuilder;
import org.jboss.osgi.metadata.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * A test that shows how a module can access another module's service.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 19-Apr-2011
 */
@RunWith(Arquillian.class)
public class ModuleAccessesBundleServiceTestCase extends AbstractXServiceTestCase {

    private static final String TARGET_BUNDLE_NAME = "example-xservice-mab-target-bundle";
    private static final String CLIENT_MODULE_NAME = "example-xservice-mab-client-module";

    @Inject
    public ServiceContainer serviceContainer;

    @ArquillianResource
    public Deployer deployer;

    @ArquillianResource
    BundleContext context;

    @ArquillianResource
    PackageAdmin packageAdmin;

    @Deployment
    public static JavaArchive createdeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "xservice-module-access");
        archive.addClasses(FrameworkUtils.class, AbstractXServiceTestCase.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addImportPackages(Logger.class, PackageAdmin.class, Module.class);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void moduleInvokesBundleService() throws Exception {

        // Deploy the bundle which contains the target service
        deployer.deploy(TARGET_BUNDLE_NAME);
        try {
            // Find the deployed bundle
            Bundle targetBundle = packageAdmin.getBundles(TARGET_BUNDLE_NAME, null)[0];
            targetBundle.start();

            // Install the client module
            deployer.deploy(CLIENT_MODULE_NAME);
            try {
                // Check that the client service is up
                ServiceName clientService = ServiceName.parse("jboss.osgi.example.invoker.service");
                assertServiceState(serviceContainer, clientService, State.UP, 5000);
            } finally {
                // Undeploy the client module
                deployer.undeploy(CLIENT_MODULE_NAME);
            }
        } finally {
            // Undeploy the target bundle
            deployer.undeploy(TARGET_BUNDLE_NAME);
        }
    }

    @Deployment(name = CLIENT_MODULE_NAME, managed = false, testable = false)
    public static JavaArchive getClientModuleArchive() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, CLIENT_MODULE_NAME);
        archive.addClasses(ClientModuleTwoActivator.class);
        archive.addAsServiceProvider(ServiceActivator.class, ClientModuleTwoActivator.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                ManifestBuilder builder = ManifestBuilder.newInstance();
                builder.addManifestHeader("Dependencies", "org.osgi.core,org.jboss.modules,org.jboss.logging,org.jboss.osgi.framework,deployment.example-xservice-mab-target-bundle");
                return builder.openStream();
            }
        });
        return archive;
    }

    @Deployment(name = TARGET_BUNDLE_NAME, managed = false, testable = false)
    public static JavaArchive getTargetBundleArchive() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, TARGET_BUNDLE_NAME);
        archive.addClasses(Echo.class, TargetBundleActivator.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addBundleActivator(TargetBundleActivator.class);
                builder.addImportPackages(Logger.class, BundleActivator.class, Module.class);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14379.java