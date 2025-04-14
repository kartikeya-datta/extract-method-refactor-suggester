error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7396.java
text:
```scala
s@@erviceBuilder.setInitialMode(Mode.ACTIVE);

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

package org.jboss.as.osgi.xservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.jboss.as.deployment.attachment.VirtualFileAttachment;
import org.jboss.as.deployment.chain.DeploymentChain;
import org.jboss.as.deployment.unit.DeploymentUnitContext;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.as.deployment.unit.DeploymentUnitProcessor;
import org.jboss.as.osgi.AbstractOSGiSubsystemTest;
import org.jboss.as.osgi.OSGiSubsystemSupport;
import org.jboss.as.osgi.OSGiSubsystemSupport.BatchedWork;
import org.jboss.as.osgi.deployment.OSGiAttachmentsDeploymentProcessor;
import org.jboss.as.osgi.deployment.OSGiManifestDeploymentProcessor;
import org.jboss.as.osgi.deployment.b.ClientBundleActivator;
import org.jboss.as.osgi.deployment.b.EchoInvoker;
import org.jboss.as.osgi.deployment.b.EchoTargetService;
import org.jboss.as.osgi.deployment.c.Echo;
import org.jboss.modules.DependencySpec;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleSpec;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.osgi.framework.Constants;
import org.jboss.osgi.framework.loading.SystemLocalLoader;
import org.jboss.osgi.framework.loading.VirtualFileResourceLoader;
import org.jboss.osgi.testing.OSGiManifestBuilder;
import org.jboss.osgi.vfs.AbstractVFS;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.vfs.VirtualFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * A test that shows how a bundle can access an MSC service.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 14-Oct-2010
 */
public class BundleAccessesModuleServiceTestCase extends AbstractOSGiSubsystemTest {

    private OSGiSubsystemSupport support;

    @Before
    public void setUp() throws Exception {
        support = new OSGiSubsystemSupport();
        DeploymentChain deploymentChain = support.getDeploymentChain();

        // A processor that constructs a {@link ModuleSpec} from the provided VirtualFile
        // The spec has a dependency on a {@link SystemLocalLoader} for a selected number of paths from the system cp.
        // All paths from the VirtualFile are exported. The spec is then registered with the {@link TestModuleLoader},
        // which handles identifiers starting with 'test'
        DeploymentUnitProcessor processor = new DeploymentUnitProcessor() {
            @Override
            public void processDeployment(DeploymentUnitContext context) throws DeploymentUnitProcessingException {
                if (context.getName().contains("module")) {
                    ModuleIdentifier identifier = ModuleIdentifier.create("test." + context.getName());
                    ModuleSpec.Builder specBuilder = ModuleSpec.build(identifier);
                    VirtualFile virtualFile = VirtualFileAttachment.getVirtualFileAttachment(context);
                    specBuilder.addResourceRoot(new VirtualFileResourceLoader(AbstractVFS.adapt(virtualFile)));

                    Set<String> paths = new HashSet<String>();
                    paths.add("org/jboss/msc/service");
                    paths.add("org/osgi/framework");
                    SystemLocalLoader sysLoader = new SystemLocalLoader(paths);
                    specBuilder.addDependency(DependencySpec.createLocalDependencySpec(sysLoader, sysLoader.getExportedPaths(), true));
                    specBuilder.addDependency(DependencySpec.createLocalDependencySpec());
                    getTestModuleLoader().addModuleSpec(specBuilder.create());
                }
            }
        };
        deploymentChain.addProcessor(processor, 10);
        deploymentChain.addProcessor(new OSGiManifestDeploymentProcessor(), 20);
        deploymentChain.addProcessor(new OSGiAttachmentsDeploymentProcessor(), Integer.MAX_VALUE);
    }

    @After
    public void tearDown() {
        if (support != null) {
            support.shutdown();
        }
    }

    @Override
    protected OSGiSubsystemSupport getSubsystemSupport() {
        return support;
    }


    @Test
    public void testTargetModuleService() throws Exception {

        JavaArchive archive = getTargetModuleArchive();
        assertNull("Bundle null", executeDeploy(archive));

        final ModuleIdentifier moduleId = ModuleIdentifier.create("test." + archive.getName());
        assertNotNull("Module not null", loadModule(moduleId));

        // Register the {@link EchoService}
        registerTargetService(moduleId);

        Bundle bundle = getBundleManager().installBundle(moduleId);
        assertBundleState(Bundle.INSTALLED, bundle.getState());

        // Invoke the {@link EchoService}
        String result = invokeTargetService(moduleId, "hello world");
        assertEquals("hello world", result);

        bundle.uninstall();
        assertBundleState(Bundle.UNINSTALLED, bundle.getState());
    }

    @Test
    public void testBundleInvokesTargetModuleService() throws Exception {

        JavaArchive archive = getTargetModuleArchive();
        assertNull("Bundle null", executeDeploy(archive));

        ModuleIdentifier moduleId = ModuleIdentifier.create("test." + archive.getName());
        assertNotNull("Module not null", loadModule(moduleId));

        // Register the {@link EchoService}
        registerTargetService(moduleId);

        Bundle targetBundle = getBundleManager().installBundle(moduleId);
        assertBundleState(Bundle.INSTALLED, targetBundle.getState());

        archive = getBundleArchive();
        Bundle clientBundle = executeDeploy(archive);
        assertBundleState(Bundle.INSTALLED, clientBundle.getState());

        clientBundle.start();
        assertBundleState(Bundle.ACTIVE, clientBundle.getState());

        String result = invokeTargetService(clientBundle, "hello world");
        assertEquals("hello world", result);

        targetBundle.uninstall();
        assertBundleState(Bundle.UNINSTALLED, targetBundle.getState());

        clientBundle.uninstall();
        assertBundleState(Bundle.UNINSTALLED, clientBundle.getState());
    }

    private void registerTargetService(final ModuleIdentifier moduleId) throws Exception {
        BatchedWork work = new BatchedWork() {
            @Override
            public void execute(BatchBuilder batchBuilder) throws Exception {
                Object service = loadClass(moduleId, EchoTargetService.class.getName()).newInstance();
                BatchServiceBuilder<?> serviceBuilder = batchBuilder.addService(EchoTargetService.SERVICE_NAME, (Service<?>) service);
                // Add the alias that the OSGi layer can use to lookup the service
                serviceBuilder.addAliases(ServiceName.of(Constants.JBOSGI_PREFIX, Echo.class.getName()));
                serviceBuilder.setInitialMode(Mode.IMMEDIATE);
            }
        };
        runWithLatchedBatch(work);
    }

    // Invokes the {@link EchoService} MSC service that was registered by the given module
    private String invokeTargetService(final ModuleIdentifier moduleId, final String message) throws Exception {
        ServiceController<?> controller = getServiceContainer().getRequiredService(EchoTargetService.SERVICE_NAME);
        Object service = controller.getValue();
        assertNotNull("Service not null", service);
        Method method = loadClass(moduleId, Echo.class.getName()).getMethod("echo", String.class);
        return (String) method.invoke(service, message);
    }

    // Invokes the {@link EchoServiceInvoker} OSGi service that was registered by the given bundle
    private String invokeTargetService(final Bundle bundle, final String message) throws Exception {
        BundleContext context = bundle.getBundleContext();
        ServiceReference sref = context.getServiceReference(EchoInvoker.class.getName());
        Object service = context.getService(sref);
        Class<?> clazz = bundle.loadClass(EchoInvoker.class.getName());
        Method method = clazz.getMethod("invoke", String.class);
        return (String) method.invoke(service, message);
    }

    private JavaArchive getBundleArchive() throws Exception {
        String uniqueName = support.getUniqueName("bundle");
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, uniqueName);
        archive.addClasses(ClientBundleActivator.class, EchoInvoker.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleManifestVersion(2);
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleVersion("1.0.0");
                builder.addBundleActivator(ClientBundleActivator.class);
                builder.addImportPackages(Echo.class);
                return builder.openStream();
            }
        });
        return archive;
    }

    private JavaArchive getTargetModuleArchive() throws Exception {
        String uniqueName = support.getUniqueName("module");
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, uniqueName);
        archive.addClasses(Echo.class, EchoTargetService.class);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7396.java