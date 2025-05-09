error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4783.java
text:
```scala
b@@uilder.addDependency(Services.FRAMEWORK_ACTIVE);

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

import static org.jboss.as.server.deployment.Services.deploymentUnitName;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.BundleManagerService;
import org.jboss.osgi.framework.Services;

/**
 * Service responsible for creating and managing the life-cycle of an OSGi deployment.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 20-Sep-2010
 */
public class BundleInstallService implements Service<BundleInstallService> {

    private static final Logger log = Logger.getLogger("org.jboss.as.osgi");

    public static final ServiceName SERVICE_NAME_BASE = ServiceName.JBOSS.append("osgi", "deployment");

    private final Deployment deployment;
    private InjectedValue<BundleManagerService> injectedBundleManager = new InjectedValue<BundleManagerService>();
    private InjectedValue<BundleStartTracker> injectedStartTracker = new InjectedValue<BundleStartTracker>();
    private ServiceName installedBundleName;

    private BundleInstallService(Deployment deployment) {
        this.deployment = deployment;
    }

    public static void addService(DeploymentPhaseContext phaseContext, Deployment deployment) {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final BundleInstallService service = new BundleInstallService(deployment);
        final String contextName = deploymentUnit.getName();
        final ServiceName serviceName = getServiceName(contextName);
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
        ServiceBuilder<BundleInstallService> builder = serviceTarget.addService(serviceName, service);
        builder.addDependency(Services.BUNDLE_MANAGER, BundleManagerService.class, service.injectedBundleManager);
        builder.addDependency(BundleStartTracker.SERVICE_NAME, BundleStartTracker.class, service.injectedStartTracker);
        builder.addDependency(deploymentUnitName(contextName));
        builder.addDependency(Services.FRAMEWORK_INIT);
        builder.install();
    }

    public static void removeService(DeploymentUnit context) {
        final ServiceName serviceName = getServiceName(context.getName());
        final ServiceController<?> serviceController = context.getServiceRegistry().getService(serviceName);
        if (serviceController != null) {
            serviceController.setMode(Mode.REMOVE);
        }
    }

    public static ServiceName getServiceName(String contextName) {
        ServiceName deploymentServiceName = deploymentUnitName(contextName);
        return BundleInstallService.SERVICE_NAME_BASE.append(deploymentServiceName.getSimpleName());
    }

    /**
     * Install the Bundle associated with this deployment.
     */
    public synchronized void start(StartContext context) throws StartException {
        log.infof("Installing deployment: %s", deployment);
        try {
            ServiceTarget serviceTarget = context.getChildTarget();
            BundleManagerService bundleManager = injectedBundleManager.getValue();
            installedBundleName = bundleManager.installBundle(serviceTarget, deployment);
            injectedStartTracker.getValue().addInstalledBundle(installedBundleName, deployment);
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
        log.infof("Uninstalling deployment: %s", deployment);
        try {
            BundleManagerService bundleManager = injectedBundleManager.getValue();
            bundleManager.uninstallBundle(deployment);
        } catch (Throwable t) {
            log.errorf(t, "Failed to uninstall deployment: %s", deployment);
        }

        // [JBAS-8801] Undeployment leaks root deployment service
        // [TODO] remove this workaround
        ServiceName serviceName = deploymentUnitName(context.getController().getName().getSimpleName());
        ServiceController<?> deploymentController = context.getController().getServiceContainer().getService(serviceName);
        if (deploymentController != null) {
            deploymentController.setMode(Mode.REMOVE);
        }
    }

    @Override
    public BundleInstallService getValue() throws IllegalStateException {
        return this;
    }

    public ServiceName getInstalledBundleName() {
        return installedBundleName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4783.java