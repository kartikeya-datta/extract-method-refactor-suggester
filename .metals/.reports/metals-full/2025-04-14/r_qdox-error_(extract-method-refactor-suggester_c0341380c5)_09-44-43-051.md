error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12812.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12812.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12812.java
text:
```scala
D@@eployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.FIRST_MODULE_USE, Phase.FIRST_MODULE_USE_TRANSFORMER, new ClassFileTransformerProcessor());

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

package org.jboss.as.server;

import java.io.File;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.as.controller.AbstractControllerService;
import org.jboss.as.controller.BootContext;
import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ModelController;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.RunningModeControl;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ExtensibleConfigurationPersister;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.platform.mbean.PlatformMBeanConstants;
import org.jboss.as.platform.mbean.RootPlatformMBeanResource;
import org.jboss.as.repository.ContentRepository;
import org.jboss.as.server.controller.descriptions.ServerDescriptionProviders;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentMountProvider;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.ServiceLoaderProcessor;
import org.jboss.as.server.deployment.SubDeploymentProcessor;
import org.jboss.as.server.deployment.annotation.AnnotationIndexProcessor;
import org.jboss.as.server.deployment.annotation.CleanupAnnotationIndexProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndexProcessor;
import org.jboss.as.server.deployment.integration.Seam2Processor;
import org.jboss.as.server.deployment.jbossallxml.JBossAllXMLParsingProcessor;
import org.jboss.as.server.deployment.module.ClassFileTransformerProcessor;
import org.jboss.as.server.deployment.module.DeploymentRootExplodedMountProcessor;
import org.jboss.as.server.deployment.module.DeploymentRootMountProcessor;
import org.jboss.as.server.deployment.module.DeploymentVisibilityProcessor;
import org.jboss.as.server.deployment.module.DriverDependenciesProcessor;
import org.jboss.as.server.deployment.module.ManifestAttachmentProcessor;
import org.jboss.as.server.deployment.module.ManifestClassPathProcessor;
import org.jboss.as.server.deployment.module.ManifestDependencyProcessor;
import org.jboss.as.server.deployment.module.ManifestExtensionListProcessor;
import org.jboss.as.server.deployment.module.ManifestExtensionNameProcessor;
import org.jboss.as.server.deployment.module.ModuleClassPathProcessor;
import org.jboss.as.server.deployment.module.ModuleDependencyProcessor;
import org.jboss.as.server.deployment.module.ModuleExtensionListProcessor;
import org.jboss.as.server.deployment.module.ModuleExtensionNameProcessor;
import org.jboss.as.server.deployment.module.ModuleIdentifierProcessor;
import org.jboss.as.server.deployment.module.ModuleSpecProcessor;
import org.jboss.as.server.deployment.module.ServerDependenciesProcessor;
import org.jboss.as.server.deployment.module.SubDeploymentDependencyProcessor;
import org.jboss.as.server.deployment.module.descriptor.DeploymentStructureDescriptorParser;
import org.jboss.as.server.deployment.reflect.CleanupReflectionIndexProcessor;
import org.jboss.as.server.deployment.reflect.InstallReflectionIndexProcessor;
import org.jboss.as.server.deployment.service.ServiceActivatorDependencyProcessor;
import org.jboss.as.server.deployment.service.ServiceActivatorProcessor;
import org.jboss.as.server.mgmt.domain.RemoteFileRepository;
import org.jboss.as.server.moduleservice.ExtensionIndexService;
import org.jboss.as.server.moduleservice.ExternalModuleService;
import org.jboss.as.server.moduleservice.ServiceModuleLoader;
import org.jboss.as.server.services.security.AbstractVaultReader;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.threads.JBossThreadFactory;

/**
 * Service for the {@link ModelController} for an AS server instance.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class ServerService extends AbstractControllerService {

    private final InjectedValue<DeploymentMountProvider> injectedDeploymentRepository = new InjectedValue<DeploymentMountProvider>();
    private final InjectedValue<ContentRepository> injectedContentRepository = new InjectedValue<ContentRepository>();
    private final InjectedValue<ServiceModuleLoader> injectedModuleLoader = new InjectedValue<ServiceModuleLoader>();

    private final InjectedValue<ExternalModuleService> injectedExternalModuleService = new InjectedValue<ExternalModuleService>();
    private final InjectedValue<PathManager> injectedPathManagerService = new InjectedValue<PathManager>();

    private final Bootstrap.Configuration configuration;
    private final BootstrapListener bootstrapListener;
    private final ControlledProcessState processState;
    private final RunningModeControl runningModeControl;
    private volatile ExtensibleConfigurationPersister extensibleConfigurationPersister;
    private final AbstractVaultReader vaultReader;
    private final RemoteFileRepository remoteFileRepository;

    public static final String SERVER_NAME = "server";

    /**
     * Construct a new instance.
     *
     * @param configuration the bootstrap configuration
     * @param prepareStep the prepare step to use
     */
    ServerService(final Bootstrap.Configuration configuration, final ControlledProcessState processState,
                  final OperationStepHandler prepareStep, final BootstrapListener bootstrapListener,
                  final RunningModeControl runningModeControl, final AbstractVaultReader vaultReader, final RemoteFileRepository remoteFileRepository) {
        super(getProcessType(configuration.getServerEnvironment()), runningModeControl, null, processState,
                ServerDescriptionProviders.ROOT_PROVIDER, prepareStep, new RuntimeExpressionResolver(vaultReader));
        this.configuration = configuration;
        this.bootstrapListener = bootstrapListener;
        this.processState = processState;
        this.runningModeControl = runningModeControl;
        this.vaultReader = vaultReader;
        this.remoteFileRepository = remoteFileRepository;
    }

    static ProcessType getProcessType(ServerEnvironment serverEnvironment) {
        if (serverEnvironment != null) {
            switch (serverEnvironment.getLaunchType()) {
            case DOMAIN:
                return ProcessType.DOMAIN_SERVER;
            case STANDALONE:
                return ProcessType.STANDALONE_SERVER;
            case EMBEDDED:
                return ProcessType.EMBEDDED_SERVER;
            case APPCLIENT:
                return ProcessType.APPLICATION_CLIENT;
            }
        }

        return ProcessType.EMBEDDED_SERVER;
    }

    /**
     * Add this service to the given service target.
     *
     * @param serviceTarget the service target
     * @param configuration the bootstrap configuration
     */
    public static void addService(final ServiceTarget serviceTarget, final Bootstrap.Configuration configuration,
                                  final ControlledProcessState processState, final BootstrapListener bootstrapListener,
                                  final RunningModeControl runningModeControl, final AbstractVaultReader vaultReader, final RemoteFileRepository remoteFileRepository) {

        final ThreadGroup threadGroup = new ThreadGroup("ServerService ThreadGroup");
        final String namePattern = "ServerService Thread Pool -- %t";
        final ThreadFactory threadFactory = new JBossThreadFactory(threadGroup, Boolean.FALSE, null, namePattern, null, null, AccessController.getContext());

        // TODO determine why QueuelessThreadPoolService makes boot take > 35 secs
//        final QueuelessThreadPoolService serverExecutorService = new QueuelessThreadPoolService(Integer.MAX_VALUE, false, new TimeSpec(TimeUnit.SECONDS, 5));
//        serverExecutorService.getThreadFactoryInjector().inject(threadFactory);
        final ServerExecutorService serverExecutorService = new ServerExecutorService(threadFactory);
        serviceTarget.addService(Services.JBOSS_SERVER_EXECUTOR, serverExecutorService).install();

        ServerService service = new ServerService(configuration, processState, null, bootstrapListener, runningModeControl, vaultReader, remoteFileRepository);
        ServiceBuilder<?> serviceBuilder = serviceTarget.addService(Services.JBOSS_SERVER_CONTROLLER, service);
        serviceBuilder.addDependency(DeploymentMountProvider.SERVICE_NAME,DeploymentMountProvider.class, service.injectedDeploymentRepository);
        serviceBuilder.addDependency(ContentRepository.SERVICE_NAME, ContentRepository.class, service.injectedContentRepository);
        serviceBuilder.addDependency(Services.JBOSS_SERVICE_MODULE_LOADER, ServiceModuleLoader.class, service.injectedModuleLoader);
        serviceBuilder.addDependency(Services.JBOSS_EXTERNAL_MODULE_SERVICE, ExternalModuleService.class,
                service.injectedExternalModuleService);
        serviceBuilder.addDependency(PathManagerService.SERVICE_NAME, PathManager.class, service.injectedPathManagerService);
        if (configuration.getServerEnvironment().isAllowModelControllerExecutor()) {
            serviceBuilder.addDependency(Services.JBOSS_SERVER_EXECUTOR, ExecutorService.class, service.getExecutorServiceInjector());
        }

        serviceBuilder.install();
    }

    public synchronized void start(final StartContext context) throws StartException {
        ServerEnvironment serverEnvironment = configuration.getServerEnvironment();
        if (runningModeControl.isReloaded()) {

        }
        Bootstrap.ConfigurationPersisterFactory configurationPersisterFactory = configuration.getConfigurationPersisterFactory();
        extensibleConfigurationPersister = configurationPersisterFactory.createConfigurationPersister(serverEnvironment, getExecutorServiceInjector().getOptionalValue());
        setConfigurationPersister(extensibleConfigurationPersister);
        super.start(context);
    }

    protected void boot(final BootContext context) throws ConfigurationPersistenceException {
        boolean ok;
        try {
            final ServerEnvironment serverEnvironment = configuration.getServerEnvironment();
            final ServiceTarget serviceTarget = context.getServiceTarget();
            serviceTarget.addListener(ServiceListener.Inheritance.ALL, bootstrapListener);
            final File[] extDirs = serverEnvironment.getJavaExtDirs();
            final File[] newExtDirs = Arrays.copyOf(extDirs, extDirs.length + 1);
            newExtDirs[extDirs.length] = new File(serverEnvironment.getServerBaseDir(), "lib/ext");
            serviceTarget.addService(org.jboss.as.server.deployment.Services.JBOSS_DEPLOYMENT_EXTENSION_INDEX,
                    new ExtensionIndexService(newExtDirs)).setInitialMode(ServiceController.Mode.ON_DEMAND).install();


            // Activate module loader
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_SERVICE_MODULE_LOADER, new DeploymentUnitProcessor() {
                @Override
                public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
                    phaseContext.getDeploymentUnit().putAttachment(Attachments.SERVICE_MODULE_LOADER, injectedModuleLoader.getValue());
                    phaseContext.getDeploymentUnit().putAttachment(Attachments.EXTERNAL_MODULE_SERVICE, injectedExternalModuleService.getValue());
                }

                @Override
                public void undeploy(DeploymentUnit context) {
                    context.removeAttachment(Attachments.SERVICE_MODULE_LOADER);
                }
            });

            // Activate core processors for jar deployment
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_EXPLODED_MOUNT, new DeploymentRootExplodedMountProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_MOUNT, new DeploymentRootMountProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_MANIFEST, new ManifestAttachmentProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_ADDITIONAL_MANIFEST, new ManifestAttachmentProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_SUB_DEPLOYMENT, new SubDeploymentProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_MODULE_IDENTIFIERS, new ModuleIdentifierProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_ANNOTATION_INDEX, new AnnotationIndexProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_PARSE_JBOSS_ALL_XML, new JBossAllXMLParsingProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_JBOSS_DEPLOYMENT_STRUCTURE, new DeploymentStructureDescriptorParser());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.STRUCTURE, Phase.STRUCTURE_CLASS_PATH, new ManifestClassPathProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.PARSE, Phase.PARSE_DEPENDENCIES_MANIFEST, new ManifestDependencyProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.PARSE, Phase.PARSE_COMPOSITE_ANNOTATION_INDEX, new CompositeIndexProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.PARSE, Phase.PARSE_EXTENSION_LIST, new ManifestExtensionListProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.PARSE, Phase.PARSE_EXTENSION_NAME, new ManifestExtensionNameProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.PARSE, Phase.PARSE_SERVICE_LOADER_DEPLOYMENT, new ServiceLoaderProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_MODULE, new ModuleDependencyProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_SAR_MODULE, new ServiceActivatorDependencyProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_CLASS_PATH, new ModuleClassPathProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_EXTENSION_LIST, new ModuleExtensionListProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_SUB_DEPLOYMENTS, new SubDeploymentDependencyProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_JDK, new ServerDependenciesProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_VISIBLE_MODULES, new DeploymentVisibilityProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_DRIVERS, new DriverDependenciesProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.CONFIGURE_MODULE, Phase.CONFIGURE_MODULE_SPEC, new ModuleSpecProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.POST_MODULE, Phase.POST_MODULE_INSTALL_EXTENSION, new ModuleExtensionNameProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.POST_MODULE, Phase.POST_MODULE_REFLECTION_INDEX, new InstallReflectionIndexProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.POST_MODULE, Phase.POST_MODULE_TRANSFORMER, new ClassFileTransformerProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.INSTALL, Phase.INSTALL_SERVICE_ACTIVATOR, new ServiceActivatorProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.CLEANUP, Phase.CLEANUP_REFLECTION_INDEX, new CleanupReflectionIndexProcessor());
            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.CLEANUP, Phase.CLEANUP_ANNOTATION_INDEX, new CleanupAnnotationIndexProcessor());

            // Ext integration deployers

            DeployerChainAddHandler.addDeploymentProcessor(SERVER_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_SEAM, new Seam2Processor(serviceTarget));

            //jboss.xml parsers
            DeploymentStructureDescriptorParser.registerJBossXMLParsers();

            try {
                // Boot but don't rollback on runtime failures
                ok = boot(extensibleConfigurationPersister.load(), false);
                if (ok) {
                    finishBoot();
                }
            } finally {
                DeployerChainAddHandler.INSTANCE.clearDeployerMap();
            }
        } catch (Exception e) {
            ServerLogger.ROOT_LOGGER.caughtExceptionDuringBoot(e);
            ok = false;
        }

        if (ok) {
            // Trigger the started message
            bootstrapListener.tick();
        } else {
            // Die!
            ServerLogger.ROOT_LOGGER.unsuccessfulBoot();
            System.exit(1);
        }
    }

    protected boolean boot(List<ModelNode> bootOperations, boolean rollbackOnRuntimeFailure) throws ConfigurationPersistenceException {
        final List<ModelNode> operations = new ArrayList<ModelNode>(bootOperations);
        operations.add(DeployerChainAddHandler.OPERATION);
        return super.boot(operations, rollbackOnRuntimeFailure);
    }

    public void stop(final StopContext context) {
        super.stop(context);

        configuration.getExtensionRegistry().clear();
        configuration.getServerEnvironment().resetProvidedProperties();
    }

    @Override
    protected void initModel(Resource rootResource, ManagementResourceRegistration rootRegistration) {
        ServerControllerModelUtil.updateCoreModel(rootResource.getModel(), configuration.getServerEnvironment());
        ServerControllerModelUtil.initOperations(rootRegistration, injectedContentRepository.getValue(),
                extensibleConfigurationPersister, configuration.getServerEnvironment(), processState,
                runningModeControl, vaultReader, configuration.getExtensionRegistry(),
                getExecutorServiceInjector().getOptionalValue() != null, remoteFileRepository,
                (PathManagerService)injectedPathManagerService.getValue());

        // TODO maybe make creating of empty nodes part of the MNR description
        rootResource.registerChild(PathElement.pathElement(ModelDescriptionConstants.CORE_SERVICE, ModelDescriptionConstants.MANAGEMENT), Resource.Factory.create());
        rootResource.registerChild(PathElement.pathElement(ModelDescriptionConstants.CORE_SERVICE, ModelDescriptionConstants.SERVICE_CONTAINER), Resource.Factory.create());
        rootResource.registerChild(ServerEnvironmentResourceDescription.RESOURCE_PATH, Resource.Factory.create());
        ((PathManagerService)injectedPathManagerService.getValue()).addPathManagerResources(rootResource);

        // Platform MBeans
        rootResource.registerChild(PlatformMBeanConstants.ROOT_PATH, new RootPlatformMBeanResource());
    }

    /** Temporary replacement for QueuelessThreadPoolService */
    private static class ServerExecutorService implements Service<ExecutorService> {

        private final ThreadFactory threadFactory;
        private ExecutorService executorService;

        private ServerExecutorService(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
        }

        @Override
        public synchronized void start(StartContext context) throws StartException {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 20L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), threadFactory);
        }

        @Override
        public synchronized void stop(final StopContext context) {

            if (executorService != null) {
                context.asynchronous();
                Thread executorShutdown = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            executorService.shutdown();
                        } finally {
                            executorService = null;
                            context.complete();
                        }
                    }
                }, "ServerExecutorService Shutdown Thread");
                executorShutdown.start();
            }
        }

        @Override
        public synchronized ExecutorService getValue() throws IllegalStateException, IllegalArgumentException {
            return executorService;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12812.java