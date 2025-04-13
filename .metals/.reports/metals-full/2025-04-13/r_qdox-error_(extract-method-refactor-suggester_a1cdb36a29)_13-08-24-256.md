error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13946.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13946.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13946.java
text:
```scala
.@@addListener(verificationHandler)

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.server.deployment;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_NAME;
import static org.jboss.as.server.deployment.DeploymentHandlerUtils.getContents;
import org.jboss.as.server.services.security.AbstractVaultReader;
import static org.jboss.msc.service.ServiceController.Mode.REMOVE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.server.ServerLogger;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.vfs.VirtualFile;

/**
 * Utility methods used by operation handlers involved with deployment.
 * <p/>
 * This class is part of the runtime operation and should not have any reference to dmr.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class DeploymentHandlerUtil {

    static class ContentItem {
        // either hash or <path, relativeTo, isArchive>
        private byte[] hash;
        private String path;
        private String relativeTo;
        private boolean isArchive;

        ContentItem(final byte[] hash) {
            assert hash != null : "hash is null";
            this.hash = hash;
        }

        ContentItem(final String path, final String relativeTo, final boolean isArchive) {
            assert path != null : "path is null";
            this.path = path;
            this.relativeTo = relativeTo;
            this.isArchive = isArchive;
        }

        byte[] getHash() {
            return hash;
        }
    }

    private DeploymentHandlerUtil() {
    }

    public static void deploy(final OperationContext context, final String deploymentUnitName, final String managementName, final AbstractVaultReader vaultReader, final ContentItem... contents) throws OperationFailedException {
        assert contents != null : "contents is null";

        if (context.isNormalServer()) {
            //
            final Resource deployment = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS);
            final ImmutableManagementResourceRegistration registration = context.getResourceRegistration();
            final ManagementResourceRegistration mutableRegistration = context.getResourceRegistrationForUpdate();

            DeploymentModelUtils.cleanup(deployment);

            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) {
                    final ServiceName deploymentUnitServiceName = Services.deploymentUnitName(deploymentUnitName);
                    final ServiceRegistry serviceRegistry = context.getServiceRegistry(true);
                    final ServiceController<?> deploymentController = serviceRegistry.getService(deploymentUnitServiceName);
                    if (deploymentController != null) {
                        final ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                        deploymentController.addListener(verificationHandler);
                        deploymentController.setMode(ServiceController.Mode.ACTIVE);
                        context.addStep(verificationHandler, OperationContext.Stage.VERIFY);

                        context.completeStep(new OperationContext.RollbackHandler() {
                            @Override
                            public void handleRollback(OperationContext context, ModelNode operation) {
                                deploymentController.setMode(ServiceController.Mode.NEVER);
                            }
                        });
                    } else {
                        final ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                        final Collection<ServiceController<?>> controllers = doDeploy(context, deploymentUnitName, managementName, verificationHandler, deployment, registration, mutableRegistration, vaultReader, contents);

                        context.addStep(verificationHandler, OperationContext.Stage.VERIFY);

                        context.completeStep(new OperationContext.ResultHandler() {
                            @Override
                            public void handleResult(OperationContext.ResultAction resultAction, OperationContext context, ModelNode operation) {
                                if(resultAction == OperationContext.ResultAction.ROLLBACK) {
                                    for(ServiceController<?> controller : controllers) {
                                        context.removeService(controller.getName());
                                    }
                                    if (context.hasFailureDescription()) {
                                        ServerLogger.ROOT_LOGGER.deploymentRolledBack(deploymentUnitName, getFormattedFailureDescription(context));
                                    } else {
                                        ServerLogger.ROOT_LOGGER.deploymentRolledBackWithNoMessage(deploymentUnitName);
                                    }
                                } else {
                                    ServerLogger.ROOT_LOGGER.deploymentDeployed(managementName, deploymentUnitName);
                                }
                            }
                        });
                    }
                }
            }, OperationContext.Stage.RUNTIME);
        }
    }

    public static Collection<ServiceController<?>> doDeploy(final OperationContext context, final String deploymentUnitName, final String managementName, final ServiceVerificationHandler verificationHandler,
                                                             final Resource deploymentResource, final ImmutableManagementResourceRegistration registration, final ManagementResourceRegistration mutableRegistration, final AbstractVaultReader vaultReader, final ContentItem... contents) {
        final ServiceName deploymentUnitServiceName = Services.deploymentUnitName(deploymentUnitName);
        final List<ServiceController<?>> controllers = new ArrayList<ServiceController<?>>();

        final ServiceTarget serviceTarget = context.getServiceTarget();
        final ServiceController<?> contentService;
        // TODO: overlay service
        final ServiceName contentsServiceName = deploymentUnitServiceName.append("contents");
        if (contents[0].hash != null)
            contentService = ContentServitor.addService(serviceTarget, contentsServiceName, contents[0].hash, verificationHandler);
        else {
            final String path = contents[0].path;
            final String relativeTo = contents[0].relativeTo;
            contentService = PathContentServitor.addService(serviceTarget, contentsServiceName, path, relativeTo, verificationHandler);
        }
        controllers.add(contentService);

        final RootDeploymentUnitService service = new RootDeploymentUnitService(deploymentUnitName, managementName, null, registration, mutableRegistration, deploymentResource, verificationHandler, vaultReader);
        final ServiceController<DeploymentUnit> deploymentUnitController = serviceTarget.addService(deploymentUnitServiceName, service)
                .addDependency(Services.JBOSS_DEPLOYMENT_CHAINS, DeployerChains.class, service.getDeployerChainsInjector())
                .addDependency(DeploymentMountProvider.SERVICE_NAME, DeploymentMountProvider.class, service.getServerDeploymentRepositoryInjector())
                .addDependency(contentsServiceName, VirtualFile.class, service.contentsInjector)
                .addListener(ServiceListener.Inheritance.ALL, verificationHandler)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
        controllers.add(deploymentUnitController);

        contentService.addListener(new AbstractServiceListener<Object>() {
            @Override
            public void transition(final ServiceController<?> controller, final ServiceController.Transition transition) {
                if (transition == ServiceController.Transition.REMOVING_to_REMOVED) {
                    deploymentUnitController.setMode(REMOVE);
                }
            }
        });
        return controllers;
    }

    public static void redeploy(final OperationContext context, final String deploymentUnitName,
                                final String managementName, final AbstractVaultReader vaultReader, final ContentItem... contents) throws OperationFailedException {
        assert contents != null : "contents is null";

        if (context.isNormalServer()) {
            //
            final Resource deployment = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS);
            final ImmutableManagementResourceRegistration registration = context.getResourceRegistration();
            final ManagementResourceRegistration mutableRegistration = context.getResourceRegistrationForUpdate();

            DeploymentModelUtils.cleanup(deployment);

            context.addStep(new OperationStepHandler() {
                public void execute(final OperationContext context, ModelNode operation) throws OperationFailedException {
                    final ServiceName deploymentUnitServiceName = Services.deploymentUnitName(deploymentUnitName);
                    context.removeService(deploymentUnitServiceName);
                    context.removeService(deploymentUnitServiceName.append("contents"));

                    final AtomicBoolean logged = new AtomicBoolean(false);
                    context.addStep(new OperationStepHandler() {
                        @Override
                        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                            ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                            context.addStep(verificationHandler, OperationContext.Stage.VERIFY);
                            doDeploy(context, deploymentUnitName, managementName, verificationHandler, deployment, registration, mutableRegistration, vaultReader, contents);
                            context.completeStep(new OperationContext.ResultHandler() {
                                @Override
                                public void handleResult(OperationContext.ResultAction resultAction, OperationContext context, ModelNode operation) {
                                    if (resultAction == OperationContext.ResultAction.ROLLBACK) {
                                        if (context.hasFailureDescription()) {
                                            ServerLogger.ROOT_LOGGER.redeployRolledBack(deploymentUnitName, getFormattedFailureDescription(context));
                                            logged.set(true);
                                        } else {
                                            ServerLogger.ROOT_LOGGER.redeployRolledBackWithNoMessage(deploymentUnitName);
                                            logged.set(true);
                                        }
                                    } else {
                                        ServerLogger.ROOT_LOGGER.deploymentRedeployed(deploymentUnitName);
                                    }
                                }
                            });
                        }
                    }, OperationContext.Stage.IMMEDIATE);

                    context.completeStep(new OperationContext.RollbackHandler() {
                        @Override
                        public void handleRollback(OperationContext context, ModelNode operation) {
                            ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                            doDeploy(context, deploymentUnitName, managementName, verificationHandler, deployment, registration, mutableRegistration, vaultReader, contents);
                            if (!logged.get()) {
                                if (context.hasFailureDescription()) {
                                    ServerLogger.ROOT_LOGGER.undeploymentRolledBack(deploymentUnitName, context.getFailureDescription().asString());
                                } else {
                                    ServerLogger.ROOT_LOGGER.undeploymentRolledBackWithNoMessage(deploymentUnitName);
                                }
                            }
                        }
                    });
                }
            }, OperationContext.Stage.RUNTIME);
        }
    }

    public static void replace(final OperationContext context, final ModelNode originalDeployment, final String deploymentUnitName, final String managementName,
                               final String replacedDeploymentUnitName, final AbstractVaultReader vaultReader, final ContentItem... contents) throws OperationFailedException {
        assert contents != null : "contents is null";

        if (context.isNormalServer()) {
            //
            final PathElement path = PathElement.pathElement(DEPLOYMENT, managementName);
            final Resource deployment = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS.append(path));
            final ImmutableManagementResourceRegistration registration = context.getResourceRegistration().getSubModel(PathAddress.EMPTY_ADDRESS.append(path));
            final ManagementResourceRegistration mutableRegistration = context.getResourceRegistrationForUpdate().getSubModel(PathAddress.EMPTY_ADDRESS.append(path));

            DeploymentModelUtils.cleanup(deployment);

            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                    final ServiceName replacedDeploymentUnitServiceName = Services.deploymentUnitName(replacedDeploymentUnitName);
                    final ServiceName replacedContentsServiceName = replacedDeploymentUnitServiceName.append("contents");
                    context.removeService(replacedContentsServiceName);
                    context.removeService(replacedDeploymentUnitServiceName);

                    ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                    final Collection<ServiceController<?>> controllers = doDeploy(context, deploymentUnitName, managementName, verificationHandler, deployment, registration, mutableRegistration, vaultReader, contents);
                    context.addStep(verificationHandler, OperationContext.Stage.VERIFY);

                    context.completeStep(new OperationContext.ResultHandler() {
                        @Override
                        public void handleResult(OperationContext.ResultAction resultAction, OperationContext context, ModelNode operation) {
                            if (resultAction == OperationContext.ResultAction.ROLLBACK) {
                                for (ServiceController<?> controller : controllers) {
                                    context.removeService(controller.getName());
                                }

                                DeploymentModelUtils.cleanup(deployment);
                                final String name = originalDeployment.require(NAME).asString();
                                final String runtimeName = originalDeployment.require(RUNTIME_NAME).asString();
                                final DeploymentHandlerUtil.ContentItem[] contents = getContents(originalDeployment.require(CONTENT));
                                final ServiceVerificationHandler svh = new ServiceVerificationHandler();
                                doDeploy(context, runtimeName, name, svh, deployment, registration, mutableRegistration, vaultReader, contents);

                                if (context.hasFailureDescription()) {
                                    ServerLogger.ROOT_LOGGER.replaceRolledBack(replacedDeploymentUnitName, deploymentUnitName, getFormattedFailureDescription(context));
                                } else {
                                    ServerLogger.ROOT_LOGGER.replaceRolledBackWithNoMessage(replacedDeploymentUnitName, deploymentUnitName);
                                }
                            } else {
                                ServerLogger.ROOT_LOGGER.deploymentReplaced(replacedDeploymentUnitName, deploymentUnitName);
                            }
                        }
                    });
                }
            }, OperationContext.Stage.RUNTIME);
        }
    }

    public static void undeploy(final OperationContext context, final String managementName, final String deploymentUnitName, final AbstractVaultReader vaultReader) {
        if (context.isNormalServer()) {
            final Resource deployment = context.readResourceForUpdate(PathAddress.EMPTY_ADDRESS);
            final ImmutableManagementResourceRegistration registration = context.getResourceRegistration();
            final ManagementResourceRegistration mutableRegistration = context.getResourceRegistrationForUpdate();
            DeploymentModelUtils.cleanup(deployment);

            context.addStep(new OperationStepHandler() {
                public void execute(OperationContext context, ModelNode operation) {
                    final ServiceName deploymentUnitServiceName = Services.deploymentUnitName(deploymentUnitName);

                    context.removeService(deploymentUnitServiceName);
                    context.removeService(deploymentUnitServiceName.append("contents"));

                    context.completeStep(new OperationContext.ResultHandler() {
                        @Override
                        public void handleResult(OperationContext.ResultAction resultAction, OperationContext context, ModelNode operation) {
                            if(resultAction == OperationContext.ResultAction.ROLLBACK) {
                                final ModelNode model = context.readResource(PathAddress.EMPTY_ADDRESS).getModel();
                                final String name = model.require(NAME).asString();
                                final String runtimeName = model.require(RUNTIME_NAME).asString();
                                final DeploymentHandlerUtil.ContentItem[] contents = getContents(model.require(CONTENT));
                                final ServiceVerificationHandler verificationHandler = new ServiceVerificationHandler();
                                doDeploy(context, runtimeName, name, verificationHandler, deployment, registration, mutableRegistration, vaultReader, contents);

                                if (context.hasFailureDescription()) {
                                    ServerLogger.ROOT_LOGGER.undeploymentRolledBack(deploymentUnitName, getFormattedFailureDescription(context));
                                } else {
                                    ServerLogger.ROOT_LOGGER.undeploymentRolledBackWithNoMessage(deploymentUnitName);
                                }
                            } else {
                                ServerLogger.ROOT_LOGGER.deploymentUndeployed(managementName, deploymentUnitName);
                            }
                        }
                    });
                }
            }, OperationContext.Stage.RUNTIME);
        }
    }

    private static String getFormattedFailureDescription(OperationContext context) {
        ModelNode failureDescNode = context.getFailureDescription();
        String failureDesc = failureDescNode.toString();
//        // Strip the wrapping {} from ModelType.OBJECT types
//        if (failureDescNode.getType() == ModelType.OBJECT && failureDesc.length() > 2
//                && failureDesc.charAt(0) == '{' && failureDesc.charAt(failureDesc.length() - 1) == '}') {
//            failureDesc = failureDesc.substring(1, failureDesc.length() - 1);
//        }

        if (failureDesc.contains("\n") && failureDesc.charAt(0) != '\n') {
            failureDesc = "\n" + failureDesc;
        }
        return failureDesc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13946.java