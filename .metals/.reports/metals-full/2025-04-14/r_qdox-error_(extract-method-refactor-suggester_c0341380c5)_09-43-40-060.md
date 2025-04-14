error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4714.java
text:
```scala
final D@@eploymentUnit deploymentUnitContext = context.getDeploymentUnit();

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

package org.jboss.as.osgi.deployment;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.as.server.deployment.DeploymentService;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.osgi.service.BundleContextService;
import org.jboss.as.osgi.service.BundleManagerService;
import org.jboss.as.osgi.service.FrameworkService;
import org.jboss.as.osgi.service.PackageAdminService;
import org.jboss.as.osgi.service.StartLevelService;
import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.bundle.BundleManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.packageadmin.PackageAdmin;
import org.osgi.service.startlevel.StartLevel;

/**
 * Service responsible for creating and managing the life-cycle of an OSGi deployment.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 20-Sep-2010
 */
public class OSGiDeploymentService implements Service<Deployment> {

    private static final Logger log = Logger.getLogger("org.jboss.as.osgi");
    private static final OSGiDeploymentListener listener = new OSGiDeploymentListener();

    public static final ServiceName SERVICE_NAME_BASE = ServiceName.JBOSS.append("osgi", "deployment");

    private final Deployment deployment;
    private InjectedValue<BundleContext> injectedBundleContext = new InjectedValue<BundleContext>();
    private InjectedValue<BundleManager> injectedBundleManager = new InjectedValue<BundleManager>();

    private OSGiDeploymentService(Deployment deployment) {
        this.deployment = deployment;
    }

    public static void addService(DeploymentPhaseContext context) {
        final DeploymentUnit deploymentUnitContext = context.getDeploymentUnitContext();
        addService(context.getServiceTarget(), OSGiDeploymentAttachment.getAttachment(deploymentUnitContext), deploymentUnitContext.getName());
    }

    public static void removeService(DeploymentUnit context) {
        final ServiceName serviceName = getServiceName(context.getName());
        final ServiceController<?> serviceController = context.getServiceRegistry().getService(serviceName);
        if(serviceController != null) {
            serviceController.setMode(Mode.REMOVE);
        }
    }

    public static void addService(ServiceTarget serviceTarget, Deployment deployment, String contextName) {
        OSGiDeploymentService service = new OSGiDeploymentService(deployment);
        ServiceBuilder<Deployment> serviceBuilder = serviceTarget.addService(getServiceName(contextName), service);
        serviceBuilder.addDependency(BundleContextService.SERVICE_NAME, BundleContext.class, service.injectedBundleContext);
        serviceBuilder.addDependency(BundleManagerService.SERVICE_NAME, BundleManager.class, service.injectedBundleManager);
        serviceBuilder.addDependency(PackageAdminService.SERVICE_NAME);
        serviceBuilder.addDependency(DeploymentService.getServiceName(contextName));
        serviceBuilder.setInitialMode(Mode.ACTIVE);
        serviceBuilder.addListener(listener);
        serviceBuilder.install();
    }

    /**
     * Get the OSGiDeploymentService name for a given context
     */
    public static ServiceName getServiceName(String contextName) {
        ServiceName deploymentServiceName = DeploymentService.getServiceName(contextName);
        return OSGiDeploymentService.SERVICE_NAME_BASE.append(deploymentServiceName.getSimpleName());
    }

    /**
     * Install the Bundle associated with this deployment.
     *
     * @param context The start context
     */
    public synchronized void start(StartContext context) throws StartException {

        // Get the OSGi system context
        ServiceController<?> controller = context.getController();
        ServiceContainer serviceContainer = controller.getServiceContainer();

        // Make sure the Framework does not shut down when the last bundle gets removed
        ServiceController<?> contextController = serviceContainer.getService(FrameworkService.SERVICE_NAME);
        contextController.setMode(Mode.ACTIVE);

        log.tracef("Installing deployment: %s", deployment);
        try {
            boolean autoStart = deployment.isAutoStart();
            deployment.setAutoStart(false);
            BundleManager bundleManager = injectedBundleManager.getValue();
            bundleManager.installBundle(deployment);
            deployment.setAutoStart(autoStart);
        } catch (Throwable t) {
            throw new StartException("Failed to install deployment: " + deployment, t);
        }
    }

    /**
     * Uninstall the Bundle associated with this deployment.
     *
     * @param context The stop context.
     */
    public synchronized void stop(StopContext context) {
        log.tracef("Uninstalling deployment: %s", deployment);
        try {
            BundleManager bundleManager = injectedBundleManager.getValue();
            bundleManager.uninstallBundle(deployment);
        } catch (Throwable t) {
            log.errorf(t, "Failed to uninstall deployment: %s", deployment);
        }
    }

    @Override
    public Deployment getValue() throws IllegalStateException {
        return deployment;
    }

    static class OSGiDeploymentListener extends AbstractServiceListener<Deployment> {

        private final Set<Deployment> startedDeployments = new CopyOnWriteArraySet<Deployment>();
        private final Set<Deployment> pendingDeployments = new CopyOnWriteArraySet<Deployment>();

        @Override
        public void listenerAdded(ServiceController<? extends Deployment> controller) {
            pendingDeployments.add(controller.getValue());
        }

        @Override
        public void serviceStarted(ServiceController<? extends Deployment> controller) {
            startedDeployments.add(controller.getValue());
            processDeployment(controller);
        }

        @Override
        public void serviceFailed(ServiceController<? extends Deployment> controller, StartException reason) {
            processDeployment(controller);
        }

        private void processDeployment(ServiceController<? extends Deployment> controller) {

            controller.removeListener(this);
            Set<Deployment> bundlesToStart = null;

            synchronized (this) {
                pendingDeployments.remove(controller.getValue());
                if (pendingDeployments.isEmpty()) {
                    bundlesToStart = new HashSet<Deployment>(startedDeployments);
                    startedDeployments.clear();
                }
            }

            if (bundlesToStart != null) {
                ServiceContainer serviceContainer = controller.getServiceContainer();
                PackageAdmin packageAdmin = PackageAdminService.getServiceValue(serviceContainer);
                StartLevel startLevel = StartLevelService.getServiceValue(serviceContainer);
                for (Deployment dep : bundlesToStart) {
                    Bundle bundle = dep.getAttachment(Bundle.class);
                    if (packageAdmin.getBundleType(bundle) != PackageAdmin.BUNDLE_TYPE_FRAGMENT) {

                        boolean autoStart = dep.isAutoStart();

                        // Obtain the autoStart properties from the initiating deployment
                        // If this call is not a result of BundleContext.installBundle(...)
                        // there won't be an {@link InstallBundleInitiatorService}
                        String contextName = InstallBundleInitiatorService.getContextName(dep);
                        ServiceName serviceName = InstallBundleInitiatorService.getServiceName(contextName);
                        ServiceController<?> initiatingController = serviceContainer.getService(serviceName);
                        if (initiatingController != null) {
                            try {
                                Deployment initiatingDeployment = (Deployment) initiatingController.getValue();
                                autoStart = initiatingDeployment.isAutoStart();
                                Integer startlevel = initiatingDeployment.getStartLevel();
                                if (startlevel != null)
                                    startLevel.setBundleStartLevel(bundle, startlevel);
                            } finally {
                                initiatingController.setMode(Mode.REMOVE);
                            }
                        }

                        if (autoStart) {
                            log.tracef("Starting bundle: %s", bundle);
                            try {
                                bundle.start();
                            } catch (BundleException ex) {
                                log.errorf(ex, "Cannot start bundle: %s", bundle);
                            }
                        }
                    }
                }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4714.java