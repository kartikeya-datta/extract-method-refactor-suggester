error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5615.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5615.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5615.java
text:
```scala
d@@eploymentServiceListener.waitForCompletion(); // Waiting for now.  This should not block at this point long term...

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.deployment;

import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.services.VFSMountService;
import org.jboss.msc.value.Values;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executors;

/**
 * (TEMPORARY) Deployment manager used to kick off a deployment for a virtual file root.
 *
 * @author John E. Bailey
 */
public class DeploymentManager implements Service<DeploymentManager> {
    private static final ServiceName MOUNT_SERVICE_NAME = ServiceName.JBOSS.append("mounts");
    private final ServiceContainer serviceContainer;
    private TempFileProvider tempFileProvider;

    public DeploymentManager(final ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    @Override
    public void start(final StartContext context) throws StartException {
        try {
            tempFileProvider = TempFileProvider.create("mount", Executors.newScheduledThreadPool(2));
        } catch(IOException e) {
            throw new StartException("Failed to create temp file provider", e);
        }
    }

    @Override
    public void stop(final StopContext context) {
        VFSUtils.safeClose(tempFileProvider);
    }

    @Override
    public DeploymentManager getValue() throws IllegalStateException {
        return this;
    }

    /**
     * Deploy a virtual file as a deployment.
     * 
     * @param deploymentRoot The root to deploy
     */
    public final void deploy(final VirtualFile deploymentRoot) throws DeploymentException {
        final ServiceContainer serviceContainer = this.serviceContainer;
        final BatchBuilder batchBuilder = serviceContainer.batchBuilder();
        final String deploymentPath = deploymentRoot.getPathName();
        try {
            // Setup VFS mount service
            // TODO: We should make sure this is an archive first...
            final ServiceName mountServiceName = MOUNT_SERVICE_NAME.append(deploymentPath);
            final VFSMountService vfsMountService = new VFSMountService(deploymentRoot.getPathName(), tempFileProvider, false);
            batchBuilder.addService(mountServiceName, vfsMountService);
                //.setInitialMode(ServiceController.Mode.ON_DEMAND);

            // Determine which deployment chain to use for this deployment
            final ServiceName deploymentChainServiceName = determineDeploymentChain();

            // Determine which deployment module loader to use for this deployment
            final ServiceName deploymentModuleLoaderServiceName = determineDeploymentModuleLoader();

            // Setup deployment service
            final ServiceName deploymentServiceName = DeploymentService.SERVICE_NAME.append(deploymentPath);
            final DeploymentService deploymentService = new DeploymentService(deploymentPath);
            final BatchServiceBuilder<?> deploymentServiceBuilder = batchBuilder.addService(deploymentServiceName, deploymentService);
            deploymentServiceBuilder.addDependency(mountServiceName)
                .toMethod(DeploymentService.DEPLOYMENT_ROOT_SETTER, Collections.singletonList(Values.immediateValue(deploymentRoot)));
            deploymentServiceBuilder.addDependency(deploymentChainServiceName)
                .toMethod(DeploymentService.DEPLOYMENT_CHAIN_SETTER, Collections.singletonList(Values.injectedValue()));
            deploymentServiceBuilder.addDependency(deploymentModuleLoaderServiceName)
                .toMethod(DeploymentService.DEPLOYMENT_MODULE_LOADER_SETTER, Collections.singletonList(Values.injectedValue()));

            // Setup deployment listener
            final DeploymentServiceListener deploymentServiceListener = new DeploymentServiceListener();
            batchBuilder.addListener(deploymentServiceListener);

            // Install the batch.
            batchBuilder.install();
            deploymentServiceListener.waitForCompletion();
        } catch(DeploymentException e) {
            throw e;
        } catch(Throwable t) {
            throw new DeploymentException(t);
        }
    }

    protected ServiceName determineDeploymentChain() {
        return null; // TODO:  Determine the chain
    }

    protected ServiceName determineDeploymentModuleLoader() {
        return null; // TODO:  Determine the loader
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5615.java