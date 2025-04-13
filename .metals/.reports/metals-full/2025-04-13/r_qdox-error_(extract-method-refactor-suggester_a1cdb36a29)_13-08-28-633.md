error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12640.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12640.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12640.java
text:
```scala
final D@@eploymentChain deploymentChain = deploymentChainProvider.determineDeploymentChain(deploymentUnitContext);

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

package org.jboss.as.model;

import static org.jboss.as.deployment.attachment.VirtualFileAttachment.attachVirtualFile;

import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;
import org.jboss.as.deployment.DeploymentFailureListener;
import org.jboss.as.deployment.ServerDeploymentRepository;
import org.jboss.as.deployment.DeploymentService;
import org.jboss.as.deployment.chain.DeploymentChain;
import org.jboss.as.deployment.chain.DeploymentChainProvider;
import org.jboss.as.deployment.client.api.server.ServerDeploymentActionResult;
import org.jboss.as.deployment.client.api.server.SimpleServerDeploymentActionResult;
import org.jboss.as.deployment.client.api.server.ServerUpdateActionResult.Result;
import org.jboss.as.deployment.module.MountHandle;
import org.jboss.as.deployment.unit.DeploymentUnitContext;
import org.jboss.as.deployment.unit.DeploymentUnitContextImpl;
import org.jboss.as.deployment.unit.DeploymentUnitProcessingException;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceActivatorContextImpl;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceNotFoundException;
import org.jboss.msc.service.StartException;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

/**
 * Helper class that can handle the runtime aspects of deploying and undeploying
 * on a server.
 *
 * @author Brian Stansberry
 */
class ServerDeploymentStartStopHandler {

    private static final Logger log = Logger.getLogger("org.jboss.as.deployment");

    <P> void deploy(final String deploymentName, final String runtimeName, final byte[] deploymentHash, final ServiceContainer serviceContainer,
            final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler, final P param) {
        try {
            BatchBuilder batchBuilder = serviceContainer.batchBuilder();
            deploy(deploymentName, runtimeName, deploymentHash, batchBuilder, serviceContainer, resultHandler, param);
            batchBuilder.install();
        }
        catch (Exception e) {
            resultHandler.handleFailure(e, param);
        }
    }

    <P> void deploy(final String deploymentName, final String runtimeName, final byte[] deploymentHash,
            final BatchBuilder batchBuilder, final ServiceContainer serviceContainer,
            final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler, final P param) {
        try {
            ServiceName deploymentServiceName = getDeploymentServiceName(deploymentName);
            // Add a listener so we can get ahold of the DeploymentService
            batchBuilder.addListener(new DeploymentServiceTracker<P>(resultHandler, param));

            activate(deploymentName, runtimeName, deploymentHash, deploymentServiceName, new ServiceActivatorContextImpl(batchBuilder), serviceContainer);
        }
        catch (RuntimeException e) {
            resultHandler.handleFailure(e, param);
        }
    }

    <P> void redeploy(final String deploymentName, final String runtimeName, final byte[] deploymentHash,
            final ServiceContainer serviceContainer, final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler, final P param) {
        try {
            ServiceName deploymentServiceName = getDeploymentServiceName(deploymentName);
            @SuppressWarnings("unchecked")
            final ServiceController<DeploymentService> controller = (ServiceController<DeploymentService>) serviceContainer.getService(deploymentServiceName);
            if(controller != null && controller.getMode() != ServiceController.Mode.REMOVE) {
                RedeploymentServiceTracker tracker = new RedeploymentServiceTracker();
                controller.addListener(tracker);
                synchronized (tracker) {
                    controller.setMode(ServiceController.Mode.REMOVE);
                    try {
                        tracker.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        if (resultHandler != null) {
                            resultHandler.handleFailure(e, param);
                            return;
                        }
                    }
                }
            }

            deploy(deploymentName, runtimeName, deploymentHash, serviceContainer, resultHandler, param);
        }
        catch (RuntimeException e) {
            if (resultHandler != null) {
                resultHandler.handleFailure(e, param);
            }
        }
    }

    <P> void undeploy(final String deploymentName, final ServiceContainer serviceContainer,
            final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler, final P param) {
        try {
            ServiceName deploymentServiceName = getDeploymentServiceName(deploymentName);
            @SuppressWarnings("unchecked")
            final ServiceController<DeploymentService> controller = (ServiceController<DeploymentService>) serviceContainer.getService(deploymentServiceName);
            if(controller != null) {
                controller.addListener(new UndeploymentServiceTracker<P>(resultHandler, param));
                controller.setMode(ServiceController.Mode.REMOVE);
            }
            else if (resultHandler != null) {
                SimpleServerDeploymentActionResult result = (param instanceof UUID) ? new SimpleServerDeploymentActionResult((UUID) param, Result.EXECUTED) : null;
                resultHandler.handleSuccess(result, param);
            }
        }
        catch (RuntimeException e) {
            if (resultHandler != null) {
                resultHandler.handleFailure(e, param);
            }
        }
    }

    private void activate(final String deploymentName, String runtimeName, final byte[] deploymentHash, final ServiceName deploymentServiceName, final ServiceActivatorContext context, final ServiceContainer serviceContainer) {
        log.infof("Activating deployment: %s", deploymentName);

        Closeable handle = null;
        try {
            final ServerDeploymentRepository deploymentRepo = getDeploymentRepository(serviceContainer);
            // The mount point we will use for the repository file
//          final VirtualFile deploymentRoot = VFS.getChild(getFullyQualifiedDeploymentPath(runtimeName));
            final VirtualFile deploymentRoot = VFS.getChild("deployments/" + runtimeName);

            // Mount virtual file
            try {
                handle = deploymentRepo.mountDeploymentContent(deploymentName, runtimeName, deploymentHash, deploymentRoot);
            } catch (IOException e) {
                throw new RuntimeException("Failed to mount deployment archive", e);
            }

            final BatchBuilder batchBuilder = context.getBatchBuilder();
            // Create deployment service
            DeploymentService deploymentService = new DeploymentService(handle);
            BatchServiceBuilder<Void> serviceBuilder = batchBuilder.addService(deploymentServiceName, deploymentService);

            // Create a sub-batch for this deployment
            final BatchBuilder deploymentSubBatch = batchBuilder.subBatchBuilder();

            // Setup a batch level dependency on deployment service
            deploymentSubBatch.addDependency(deploymentServiceName);

            // Add a deployment failure listener to the batch
            deploymentSubBatch.addListener(new DeploymentFailureListener(deploymentServiceName));

            // Create the deployment unit context
            final DeploymentUnitContext deploymentUnitContext = new DeploymentUnitContextImpl(deploymentServiceName.getSimpleName(), deploymentSubBatch, serviceBuilder);
            attachVirtualFile(deploymentUnitContext, deploymentRoot);
            deploymentUnitContext.putAttachment(MountHandle.ATTACHMENT_KEY, new MountHandle(handle));

            // Execute the deployment chain
            final DeploymentChainProvider deploymentChainProvider = DeploymentChainProvider.INSTANCE;
            final DeploymentChain deploymentChain = deploymentChainProvider.determineDeploymentChain(deploymentRoot);
            log.debugf("Executing deployment '%s' with chain: %s", deploymentName, deploymentChain);
            if(deploymentChain == null)
                throw new RuntimeException("Failed determine the deployment chain for deployment root: " + deploymentRoot);
            try {
                deploymentChain.processDeployment(deploymentUnitContext);
            } catch (DeploymentUnitProcessingException e) {
                throw new RuntimeException("Failed to process deployment chain.", e);
            }
        } catch(Throwable t) {
            VFSUtils.safeClose(handle);
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new RuntimeException("Failed to activate deployment unit " + deploymentName, t);
        }
    }

    private ServerDeploymentRepository getDeploymentRepository(ServiceContainer serviceContainer) throws ServiceNotFoundException {
        @SuppressWarnings("unchecked")
        ServiceController<ServerDeploymentRepository> serviceController = (ServiceController<ServerDeploymentRepository>) serviceContainer.getRequiredService(ServerDeploymentRepository.SERVICE_NAME);
        return serviceController.getValue();
    }

    private static ServiceName getDeploymentServiceName(String deploymentName) {
        return DeploymentService.SERVICE_NAME.append(deploymentName.replace('.', '_'));
    }

//    private static String getFullyQualifiedDeploymentPath(String name) {
//        final String fileName = name;
//        String path = System.getProperty("jboss.server.deploy.dir");
//        return (path.endsWith(File.separator)) ? path + fileName : path + File.separator + fileName;
//    }

    private static class AbstractDeploymentServiceTracker<P> extends AbstractServiceListener<Object> {

        protected final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler;
        protected final P param;

        protected AbstractDeploymentServiceTracker(final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler,
                final P param) {
            this.resultHandler = resultHandler;
            this.param = param;
        }

        @Override
        public void serviceFailed(ServiceController<?> controller, StartException reason) {

            if (resultHandler != null) {
                resultHandler.handleFailure(reason, param);
            }
        }

        protected void recordResult(ServiceController<? extends Object> controller) {
            // FIXME UpdateResultHandler should take ServiceName as result type
            SimpleServerDeploymentActionResult result =
                (param instanceof UUID) ? new SimpleServerDeploymentActionResult((UUID) param, Result.EXECUTED)
                                        : null;
            resultHandler.handleSuccess(result, param);
        }

    }

    private static class DeploymentServiceTracker<P> extends AbstractDeploymentServiceTracker<P> {

        private DeploymentServiceTracker(final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler,
                final P param) {
            super(resultHandler, param);
        }

        @Override
        public void serviceStarted(ServiceController<?> controller) {
            if (resultHandler != null) {
                recordResult(controller);
            }
        }

    }

    private static class UndeploymentServiceTracker<P> extends AbstractDeploymentServiceTracker<P> {

        private UndeploymentServiceTracker(final UpdateResultHandler<? super ServerDeploymentActionResult, P> resultHandler,
                final P param) {
            super(resultHandler, param);
        }

        @Override
        public void serviceStopped(ServiceController<?> controller) {
            if (resultHandler != null) {
                recordResult(controller);
            }
        }

    }

    private static class RedeploymentServiceTracker extends AbstractServiceListener<Object> {

        @Override
        public void serviceRemoved(ServiceController<? extends Object> controller) {
            synchronized (this) {
                notifyAll();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12640.java