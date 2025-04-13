error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11137.java
text:
```scala
public I@@nitialDeploymentTracker(final OperationContext context, final Activation activationMode) {

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.as.osgi.service;

import static org.jboss.as.osgi.OSGiConstants.SERVICE_BASE_NAME;
import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.server.Services.JBOSS_SERVER_CONTROLLER;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.osgi.parser.SubsystemState.Activation;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.Services;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceController.Transition;
import org.jboss.msc.service.ServiceListener;
import org.jboss.msc.service.ServiceListener.Inheritance;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.osgi.framework.IntegrationServices;
import org.jboss.osgi.framework.PersistentBundlesComplete;
import org.jboss.osgi.framework.PersistentBundlesHandler;
import org.osgi.framework.Bundle;

/**
 * A service that provides persistent bundles on framework startup.
 *
 * @author thomas.diesler@jboss.com
 * @since 12-Apr-2012
 */
public class PersistentBundlesIntegration implements PersistentBundlesHandler {

    public static ServiceController<?> addService(ServiceTarget serviceTarget, InitialDeploymentTracker deploymentTracker) {
        PersistentBundlesIntegration service = new PersistentBundlesIntegration();
        ServiceBuilder<PersistentBundlesHandler> builder = serviceTarget.addService(IntegrationServices.PERSISTENT_BUNDLES_HANDLER, service);
        builder.addDependencies(IntegrationServices.AUTOINSTALL_COMPLETE, InitialDeploymentTracker.INITIAL_DEPLOYMENTS_COMPLETE);
        builder.setInitialMode(Mode.ON_DEMAND);
        return builder.install();
    }

    private PersistentBundlesIntegration() {
    }

    @Override
    public void start(StartContext context) throws StartException {
        final ServiceController<?> controller = context.getController();
        LOGGER.debugf("Starting: %s in mode %s", controller.getName(), controller.getMode());
    }

    @Override
    public void stop(StopContext context) {
        ServiceController<?> controller = context.getController();
        LOGGER.debugf("Stopping: %s in mode %s", controller.getName(), controller.getMode());
    }

    @Override
    public PersistentBundlesIntegration getValue() {
        return this;
    }

    public static class InitialDeploymentTracker {

        static final ServiceName INITIAL_DEPLOYMENTS_COMPLETE = SERVICE_BASE_NAME.append("initial", "deployments", "COMPLETE");

        private final Set<ServiceName> bundleInstallServices = new HashSet<ServiceName>();
        private final AtomicBoolean deploymentInstallComplete = new AtomicBoolean(false);
        private final AtomicInteger deploymentCount;
        private final Set<String> deploymentNames;
        private ServiceListener<Bundle> bundleInstallListener;

        public InitialDeploymentTracker(final OperationContext context, Activation activationMode) {

            final ServiceTarget serviceTarget = context.getServiceTarget();
            final PersistentBundlesComplete installComplete = new PersistentBundlesComplete() {
                @Override
                protected boolean allServicesAdded(Set<ServiceName> trackedServices) {
                    synchronized (bundleInstallServices) {
                        return deploymentInstallComplete.get() && bundleInstallServices.size() == trackedServices.size();
                    }
                }
            };
            ServiceBuilder<Void> installCompleteBuilder = installComplete.install(serviceTarget);
            installCompleteBuilder.setInitialMode(activationMode == Activation.EAGER ? Mode.ACTIVE : Mode.ON_DEMAND);

            deploymentNames = getDeploymentNames(context);
            deploymentCount = new AtomicInteger(deploymentNames.size());
            if (deploymentCount.get() == 0) {
                // Install the INITIAL_DEPLOYMENTS_COMPLETE service
                initialDeploymentsComplete(serviceTarget);
                // Install the PERSISTENT_BUNDLES_COMPLETE service
                installCompleteBuilder.install();
                return;
            }

            final Set<ServiceName> deploymentServiceNames = new HashSet<ServiceName>();
            for (String deploymentName : deploymentNames) {
                ServiceName serviceName = Services.deploymentUnitName(deploymentName);
                deploymentServiceNames.add(serviceName.append(Phase.INSTALL.toString()));
            }

            final ServiceRegistry serviceRegistry = context.getServiceRegistry(false);
            final ServiceTarget listenerTarget = serviceRegistry.getService(JBOSS_SERVER_CONTROLLER).getServiceContainer();
            bundleInstallListener = installComplete.getListener();
            ServiceListener<Object> listener = new AbstractServiceListener<Object>() {
                @Override
                public void transition(ServiceController<? extends Object> controller, Transition transition) {
                    if (isClosed() == false) {
                        ServiceName serviceName = controller.getName();
                        synchronized (deploymentServiceNames) {
                            if (deploymentServiceNames.contains(serviceName)) {
                                switch (transition) {
                                    case STARTING_to_UP:
                                    case STARTING_to_START_FAILED:
                                        deploymentServiceNames.remove(serviceName);
                                        int remaining = deploymentCount.decrementAndGet();
                                        LOGGER.debugf("Deployment tracked: %s (remaining=%d)", serviceName.getCanonicalName(), remaining);
                                        if (deploymentCount.get() == 0) {
                                            listenerTarget.removeListener(this);
                                            initialDeploymentsComplete(serviceTarget);
                                            installComplete.checkAndComplete();
                                        }
                                }
                            }
                        }
                    }
                }
            };
            listenerTarget.addListener(Inheritance.ALL, listener);
        }

        public ServiceListener<Bundle> getBundleInstallListener() {
            return bundleInstallListener;
        }

        public boolean isClosed() {
            return deploymentCount.get() == 0;
        }

        public boolean hasDeploymentName(String depname) {
            return deploymentNames.contains(depname);
        }

        public void registerBundleInstallService(ServiceName serviceName) {
            synchronized (bundleInstallServices) {
                LOGGER.debugf("Register bundle install service: %s", serviceName);
                bundleInstallServices.add(serviceName);
            }
        }

        private void initialDeploymentsComplete(ServiceTarget serviceTarget) {
            LOGGER.debugf("Initial deployments complete");
            final ServiceBuilder<Void> deploymentCompleteBuilder = serviceTarget.addService(INITIAL_DEPLOYMENTS_COMPLETE, new AbstractService<Void>() {
                public void start(StartContext context) throws StartException {
                    final ServiceController<?> controller = context.getController();
                    LOGGER.debugf("Starting: %s in mode %s", controller.getName(), controller.getMode());
                }
            });
            deploymentInstallComplete.set(true);
            deploymentCompleteBuilder.install();
        }

        private Set<String> getDeploymentNames(OperationContext context) {
            final Set<String> result = new HashSet<String>();
            final ModelNode model = Resource.Tools.readModel(context.readResourceFromRoot(PathAddress.EMPTY_ADDRESS, true));
            final ModelNode depmodel = model.get(ModelDescriptionConstants.DEPLOYMENT);
            if (depmodel.isDefined()) {
                final List<ModelNode> deploymentNodes = depmodel.asList();
                for (ModelNode node : deploymentNodes) {
                    Property property = node.asProperty();
                    result.add(property.getName());
                }
                LOGGER.debugf("Expecting initial deployments: %s", result);
            }
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11137.java