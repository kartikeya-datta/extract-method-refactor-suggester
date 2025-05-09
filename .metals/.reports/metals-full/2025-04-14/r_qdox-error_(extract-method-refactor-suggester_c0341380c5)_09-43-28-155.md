error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12387.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12387.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12387.java
text:
```scala
final I@@nitialDeploymentTracker deploymentTracker = new InitialDeploymentTracker(context);

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
package org.jboss.as.osgi.parser;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ServiceLoader;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.osgi.SubsystemExtension;
import org.jboss.as.osgi.deployment.BundleActivateProcessor;
import org.jboss.as.osgi.deployment.BundleDependenciesProcessor;
import org.jboss.as.osgi.deployment.BundleDeploymentProcessor;
import org.jboss.as.osgi.deployment.BundleInstallProcessor;
import org.jboss.as.osgi.deployment.BundleResolveProcessor;
import org.jboss.as.osgi.deployment.BundleSubDeploymentMarkingProcessor;
import org.jboss.as.osgi.deployment.DeferredPhaseProcessor;
import org.jboss.as.osgi.deployment.FrameworkActivateProcessor;
import org.jboss.as.osgi.deployment.ModuleRegisterProcessor;
import org.jboss.as.osgi.deployment.OSGiBundleInfoParseProcessor;
import org.jboss.as.osgi.deployment.OSGiComponentParseProcessor;
import org.jboss.as.osgi.deployment.OSGiManifestStructureProcessor;
import org.jboss.as.osgi.deployment.OSGiXServiceParseProcessor;
import org.jboss.as.osgi.management.OSGiRuntimeResource;
import org.jboss.as.osgi.parser.SubsystemState.Activation;
import org.jboss.as.osgi.service.FrameworkBootstrapService;
import org.jboss.as.osgi.service.InitialDeploymentTracker;
import org.jboss.as.osgi.service.ModuleRegistrationTracker;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceTarget;

/**
 * OSGi subsystem operation handler.
 *
 * @author Thomas.Diesler@jboss.com
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author David Bosschaert
 * @since 11-Sep-2010
 */
class OSGiSubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final OSGiSubsystemAdd INSTANCE = new OSGiSubsystemAdd();

    private OSGiRuntimeResource resource;

    private OSGiSubsystemAdd() {
    }

    @Override
    protected Resource createResource(OperationContext context) {
        resource = new OSGiRuntimeResource();
        context.addResource(PathAddress.EMPTY_ADDRESS, resource);
        return resource;
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        OSGiRootResource.ACTIVATION.validateAndSet(operation, model);
    }

    @Override
    protected void performBoottime(final OperationContext context, final ModelNode operation, final ModelNode model,
            final ServiceVerificationHandler verificationHandler, final List<ServiceController<?>> newControllers) throws OperationFailedException {

        LOGGER.infoActivatingSubsystem();

        final Activation activation = Activation.valueOf(OSGiRootResource.ACTIVATION.resolveModelAttribute(context, model).asString().toUpperCase(Locale.ENGLISH));
        final ServiceTarget serviceTarget = context.getServiceTarget();
        final InitialDeploymentTracker deploymentTracker = new InitialDeploymentTracker(context, verificationHandler);
        final ModuleRegistrationTracker registrationTracker = new ModuleRegistrationTracker();

        // Collect the subsystem extensions
        final List<SubsystemExtension> extensions = new ArrayList<SubsystemExtension>();
        final Iterator<SubsystemExtension> services = ServiceLoader.load(SubsystemExtension.class, getClass().getClassLoader()).iterator();
        while(services.hasNext()) {
            extensions.add(services.next());
        }

        context.addStep(new OperationStepHandler() {
            @Override
            public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                newControllers.add(FrameworkBootstrapService.addService(serviceTarget, resource, deploymentTracker, extensions, verificationHandler));
                newControllers.add(registrationTracker.install(serviceTarget, verificationHandler));
                context.completeStep(OperationContext.RollbackHandler.NOOP_ROLLBACK_HANDLER);
            }
        }, OperationContext.Stage.RUNTIME);

        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            protected void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_OSGI_MANIFEST, new OSGiManifestStructureProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.STRUCTURE, Phase.STRUCTURE_BUNDLE_SUB_DEPLOYMENT, new BundleSubDeploymentMarkingProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_OSGI_BUNDLE_INFO, new OSGiBundleInfoParseProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_OSGI_PROPERTIES, new OSGiXServiceParseProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_OSGI_DEPLOYMENT, new BundleDeploymentProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_OSGI_COMPONENTS, new OSGiComponentParseProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.PARSE, Phase.PARSE_OSGI_SUBSYSTEM_ACTIVATOR, new FrameworkActivateProcessor(deploymentTracker));
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.REGISTER, Phase.REGISTER_BUNDLE_INSTALL, new BundleInstallProcessor(deploymentTracker));
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.DEPENDENCIES, Phase.DEPENDENCIES_BUNDLE, new BundleDependenciesProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.CONFIGURE_MODULE, Phase.CONFIGURE_RESOLVE_BUNDLE, new BundleResolveProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.CONFIGURE_MODULE, Phase.CONFIGURE_DEFERRED_PHASE, new DeferredPhaseProcessor());
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_RESOLVER_MODULE, new ModuleRegisterProcessor(registrationTracker));
                processorTarget.addDeploymentProcessor(OSGiExtension.SUBSYSTEM_NAME, Phase.INSTALL, Phase.INSTALL_BUNDLE_ACTIVATE, new BundleActivateProcessor());
            }
        }, OperationContext.Stage.RUNTIME);

        // Perform boottime on subsystem extensions
        for(SubsystemExtension extension : extensions) {
            extension.performBoottime(context, operation, model, verificationHandler, newControllers);
        }

        // Add the subsystem state as a service
        newControllers.add(SubsystemState.addService(serviceTarget, activation));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12387.java