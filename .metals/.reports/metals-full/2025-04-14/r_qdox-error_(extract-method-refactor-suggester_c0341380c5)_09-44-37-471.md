error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3508.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3508.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3508.java
text:
```scala
b@@uilder.installIntegrationService(serviceContainer, serviceTarget, new BundleLifecycleIntegration());

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

package org.jboss.as.osgi.service;

import static org.jboss.as.osgi.OSGiConstants.SERVICE_BASE_NAME;
import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.osgi.OSGiMessages.MESSAGES;
import static org.jboss.as.osgi.parser.SubsystemState.PROP_JBOSS_OSGI_SYSTEM_MODULES;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.osgi.OSGiConstants;
import org.jboss.as.osgi.SubsystemExtension;
import org.jboss.as.osgi.management.OSGiRuntimeResource;
import org.jboss.as.osgi.parser.SubsystemState;
import org.jboss.as.osgi.parser.SubsystemState.Activation;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.modules.log.ModuleLogger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceListener.Inheritance;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.framework.IntegrationService;
import org.jboss.osgi.framework.SystemPathsPlugin;
import org.jboss.osgi.framework.internal.FrameworkBuilder;
import org.osgi.framework.Constants;

/**
 * Service responsible for creating and managing the life-cycle of the OSGi Framework.
 *
 * @author Thomas.Diesler@jboss.com
 * @author David Bosschaert
 * @since 11-Sep-2010
 */
public class FrameworkBootstrapService implements Service<Void> {

    static final ServiceName FRAMEWORK_BOOTSTRAP_NAME = SERVICE_BASE_NAME.append("framework", "bootstrap");
    static final String MAPPED_OSGI_SOCKET_BINDINGS = "org.jboss.as.osgi.socket.bindings";

    private final InjectedValue<ServerEnvironment> injectedServerEnvironment = new InjectedValue<ServerEnvironment>();
    private final InjectedValue<SubsystemState> injectedSubsystemState = new InjectedValue<SubsystemState>();
    private final List<SubsystemExtension> extensions;
    private final OSGiRuntimeResource resource;

    public static ServiceController<Void> addService(ServiceTarget target, OSGiRuntimeResource resource, List<SubsystemExtension> extensions,
            ServiceVerificationHandler verificationHandler) {
        FrameworkBootstrapService service = new FrameworkBootstrapService(resource, extensions);
        ServiceBuilder<Void> builder = target.addService(FRAMEWORK_BOOTSTRAP_NAME, service);
        builder.addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, service.injectedServerEnvironment);
        builder.addDependency(OSGiConstants.SUBSYSTEM_STATE_SERVICE_NAME, SubsystemState.class, service.injectedSubsystemState);
        builder.addListener(Inheritance.ONCE, verificationHandler);
        return builder.install();
    }

    private FrameworkBootstrapService(OSGiRuntimeResource resource, List<SubsystemExtension> extensions) {
        this.extensions = extensions;
        this.resource = resource;
    }

    @Override
    public synchronized void start(StartContext context) throws StartException {
        ServiceController<?> controller = context.getController();
        LOGGER.tracef("Starting: %s in mode %s", controller.getName(), controller.getMode());
        try {
            ServiceContainer serviceContainer = context.getController().getServiceContainer();

            // Setup the OSGi {@link Framework} properties
            SubsystemState subsystemState = injectedSubsystemState.getValue();
            Map<String, Object> props = new HashMap<String, Object>(subsystemState.getProperties());
            setupIntegrationProperties(context, props);

            // Register the URLStreamHandlerFactory
            Module coreFrameworkModule = ((ModuleClassLoader) FrameworkBuilder.class.getClassLoader()).getModule();
            Module.registerURLStreamHandlerFactoryModule(coreFrameworkModule);
            Module.registerContentHandlerFactoryModule(coreFrameworkModule);

            ServiceTarget serviceTarget = context.getChildTarget();
            JAXPServiceProvider.addService(serviceTarget);
            ResolverService.addService(serviceTarget);
            RepositoryService.addService(serviceTarget);

            // Configure the {@link Framework} builder
            FrameworkBuilder builder = new FrameworkBuilder(props);
            builder.setServiceContainer(serviceContainer);
            builder.setServiceTarget(serviceTarget);

            // Install the integration services
            builder.installIntegrationService(serviceContainer, serviceTarget, new BundleInstallIntegration());
            builder.installIntegrationService(serviceContainer, serviceTarget, new FrameworkModuleIntegration(props));
            builder.installIntegrationService(serviceContainer, serviceTarget, new ModuleLoaderIntegration());
            builder.installIntegrationService(serviceContainer, serviceTarget, new SystemServicesIntegration(resource, extensions));

            Activation activation = subsystemState.getActivationPolicy();
            if (activation == Activation.EAGER) {
                // Install the bootstrap bundle services
                builder.installIntegrationService(serviceContainer, serviceTarget, new BootstrapBundlesIntegration());
                builder.installIntegrationService(serviceContainer, serviceTarget, new PersistentBundlesIntegration());
                builder.setInitialMode(Mode.ACTIVE);
            } else {
                // Exclude the bootstrap bundle services - see {@link FrameworkActivator}
                builder.addExcludedService(IntegrationService.BOOTSTRAP_BUNDLES_INSTALL);
                builder.addExcludedService(IntegrationService.PERSISTENT_BUNDLES_INSTALL);
                builder.setInitialMode(Mode.LAZY);
            }

            // Create the {@link Framework} services
            builder.createFrameworkServices(true);
        } catch (Throwable th) {
            throw MESSAGES.startFailedToCreateFrameworkServices(th);
        }
    }

    @Override
    public synchronized void stop(StopContext context) {
        ServiceController<?> controller = context.getController();
        LOGGER.tracef("Stopping: %s in mode %s", controller.getName(), controller.getMode());
    }

    @Override
    public Void getValue() throws IllegalStateException {
        return null;
    }

    private void setupIntegrationProperties(StartContext context, Map<String, Object> props) {

        // Setup the Framework's storage area.
        String storage = (String) props.get(Constants.FRAMEWORK_STORAGE);
        if (storage == null) {
            ServerEnvironment environment = injectedServerEnvironment.getValue();
            File dataDir = environment.getServerDataDir();
            storage = dataDir.getAbsolutePath() + File.separator + "osgi-store";
            props.put(Constants.FRAMEWORK_STORAGE, storage);
        }

        // Provide the ModuleLogger
        ModuleLogger moduleLogger = Module.getModuleLogger();
        if (moduleLogger != null)
            props.put(ModuleLogger.class.getName(), moduleLogger.getClass().getName());

        // Setup default system modules
        String sysmodules = (String) props.get(PROP_JBOSS_OSGI_SYSTEM_MODULES);
        if (sysmodules == null) {
            Set<String> sysModules = new LinkedHashSet<String>();
            sysModules.addAll(Arrays.asList(SystemPackagesIntegration.DEFAULT_SYSTEM_MODULES));
            sysmodules = sysModules.toString();
            sysmodules = sysmodules.substring(1, sysmodules.length() - 1);
            props.put(PROP_JBOSS_OSGI_SYSTEM_MODULES, sysmodules);
        }

        // Setup default system packages
        String syspackages = (String) props.get(Constants.FRAMEWORK_SYSTEMPACKAGES);
        if (syspackages == null) {
            Set<String> sysPackages = new LinkedHashSet<String>();
            sysPackages.addAll(Arrays.asList(SystemPackagesIntegration.JAVAX_API_PACKAGES));
            sysPackages.addAll(Arrays.asList(SystemPathsPlugin.DEFAULT_FRAMEWORK_PACKAGES));
            sysPackages.addAll(Arrays.asList(SystemPackagesIntegration.DEFAULT_INTEGRATION_PACKAGES));
            syspackages = sysPackages.toString();
            syspackages = syspackages.substring(1, syspackages.length() - 1);
            props.put(Constants.FRAMEWORK_SYSTEMPACKAGES, syspackages);
        }

        String extrapackages = (String) props.get(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA);
        if (extrapackages != null) {
            props.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, extrapackages);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3508.java