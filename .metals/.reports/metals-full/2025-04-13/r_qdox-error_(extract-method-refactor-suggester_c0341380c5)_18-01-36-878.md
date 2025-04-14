error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6030.java
text:
```scala
public static final S@@erviceName SERVICE_NAME = ServiceName.JBOSS.append("osgi", "framework");

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

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;

import org.jboss.as.jmx.MBeanServerService;
import org.jboss.as.osgi.parser.OSGiSubsystemState.OSGiModule;
import org.jboss.logging.Logger;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.BatchServiceBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceNotFoundException;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.DeployerService;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.bundle.AbstractUserBundle;
import org.jboss.osgi.framework.bundle.BundleManager;
import org.jboss.osgi.framework.plugin.BundleDeploymentPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.framework.launch.Framework;

/**
 * Service responsible for creating and managing the life-cycle of the OSGi Framework.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 11-Sep-2010
 */
public class FrameworkService implements Service<BundleContext> {
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("osgi.system.context");
    private static final Logger log = Logger.getLogger("org.jboss.as.osgi");

    private InjectedValue<BundleManager> injectedBundleManager = new InjectedValue<BundleManager>();
    private InjectedValue<MBeanServer> injectedMBeanServer = new InjectedValue<MBeanServer>();
    private InjectedValue<Configuration> injectedConfig = new InjectedValue<Configuration>();
    private Framework framework;

    public static void addService(final BatchBuilder batchBuilder, Mode initialMode) {
        FrameworkService service = new FrameworkService();
        BatchServiceBuilder<?> serviceBuilder = batchBuilder.addService(FrameworkService.SERVICE_NAME, service);
        serviceBuilder.addDependency(BundleManagerService.SERVICE_NAME, BundleManager.class, service.injectedBundleManager);
        serviceBuilder.addDependency(MBeanServerService.SERVICE_NAME, MBeanServer.class, service.injectedMBeanServer);
        serviceBuilder.addDependency(Configuration.SERVICE_NAME, Configuration.class, service.injectedConfig);
        serviceBuilder.setInitialMode(initialMode);
    }

    public static BundleContext getServiceValue(ServiceContainer container) {
        try {
            ServiceController<?> controller = container.getRequiredService(SERVICE_NAME);
            return (BundleContext) controller.getValue();
        } catch (ServiceNotFoundException ex) {
            throw new IllegalStateException("Cannot obtain required service: " + SERVICE_NAME);
        }
    }

    public synchronized void start(StartContext context) throws StartException {
        log.infof("Starting OSGi Framework");
        try {
            // Start the OSGi {@link Framework}
            final ServiceContainer serviceContainer = context.getController().getServiceContainer();
            BundleManager bundleManager = injectedBundleManager.getValue();
            framework = bundleManager.getFrameworkState();
            framework.start();

            // Register the {@link MBeanServer} as OSGi service
            BundleContext sysContext = framework.getBundleContext();
            MBeanServer mbeanServer = injectedMBeanServer.getValue();
            sysContext.registerService(MBeanServer.class.getName(), mbeanServer, null);

            // Register a {@link SynchronousBundleListener} that removes the {@link DeploymentService}
            BundleListener uninstallListener = new SynchronousBundleListener() {

                @Override
                public void bundleChanged(BundleEvent event) {
                    if (event.getType() == BundleEvent.UNINSTALLED) {
                        AbstractUserBundle userBundle;
                        try {
                            userBundle = AbstractUserBundle.assertBundleState(event.getBundle());
                        } catch (RuntimeException ex) {
                            // ignore
                            return;
                        }
                        Deployment deployment = userBundle.getDeployment();
                        ServiceName serviceName = deployment.getAttachment(ServiceName.class);
                        if (serviceName != null) {
                            ServiceController<?> controller = serviceContainer.getService(serviceName);
                            if (controller != null) {
                                controller.setMode(ServiceController.Mode.REMOVE);
                            }
                        }
                    }
                }
            };
            sysContext.addBundleListener(uninstallListener);

            // Create the list of {@link Deployment}s for the configured modules
            List<Deployment> deployments = new ArrayList<Deployment>();
            BundleDeploymentPlugin depPlugin = bundleManager.getPlugin(BundleDeploymentPlugin.class);
            for (OSGiModule module : injectedConfig.getValue().getModules()) {
                ModuleIdentifier identifier = module.getIdentifier();
                Deployment dep = depPlugin.createDeployment(identifier);
                dep.setAutoStart(module.isStart());
                deployments.add(dep);
            }

            // Deploy the bundles through the {@link DeployerService}
            ServiceReference sref = sysContext.getServiceReference(DeployerService.class.getName());
            DeployerService service = (DeployerService) sysContext.getService(sref);
            service.deploy(deployments.toArray(new Deployment[deployments.size()]));

        } catch (Throwable t) {
            throw new StartException("Failed to start OSGi Framework: " + framework, t);
        }
    }

    public synchronized void stop(StopContext context) {
        log.infof("Stopping OSGi Framework");
        if (framework != null) {
            try {
                framework.stop();
                framework.waitForStop(2000);
            } catch (Exception ex) {
                log.errorf(ex, "Cannot stop OSGi Framework");
            }
        }
    }

    @Override
    public BundleContext getValue() throws IllegalStateException {
        if (framework == null || framework.getState() != Bundle.ACTIVE)
            throw new IllegalStateException("Cannot get BundleContext for: " + framework);
        return framework.getBundleContext();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6030.java