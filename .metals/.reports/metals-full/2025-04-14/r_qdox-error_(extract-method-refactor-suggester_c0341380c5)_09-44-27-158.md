error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9402.java
text:
```scala
S@@erviceName deploymentService = Services.deploymentUnitName(contextName);

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

import org.jboss.as.osgi.deployment.DeploymentHolderService;
import org.jboss.as.osgi.deployment.ModuleRegistrationService;
import org.jboss.as.osgi.deployment.OSGiDeploymentService;
import org.jboss.as.osgi.parser.SubsystemState.Activation;
import org.jboss.as.server.deployment.Services;
import org.jboss.logging.Logger;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceNotFoundException;
import org.jboss.msc.service.ServiceRegistryException;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.bundle.AbstractUserBundle;
import org.jboss.osgi.framework.bundle.BundleManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.launch.Framework;

/**
 * Service responsible for creating and managing the life-cycle of the OSGi system {@link BundleContext}.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 29-Oct-2010
 */
public class BundleContextService implements Service<BundleContext> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("osgi", "context");

    private static final Logger log = Logger.getLogger("org.jboss.as.osgi");

    private final InjectedValue<BundleManager> injectedBundleManager = new InjectedValue<BundleManager>();
    private final InjectedValue<Framework> injectedFramework = new InjectedValue<Framework>();
    private BundleContext sysContext;

    public static void addService(final ServiceTarget target, Activation policy) {
        BundleContextService service = new BundleContextService();
        ServiceBuilder<?> serviceBuilder = target.addService(BundleContextService.SERVICE_NAME, service);
        serviceBuilder.addDependency(BundleManagerService.SERVICE_NAME, BundleManager.class, service.injectedBundleManager);
        serviceBuilder.addDependency(FrameworkService.SERVICE_NAME, Framework.class, service.injectedFramework);
        serviceBuilder.setInitialMode(policy == Activation.LAZY ? Mode.ON_DEMAND : Mode.ACTIVE);
        serviceBuilder.install();
    }

    public static BundleContext getServiceValue(ServiceContainer container) {
        try {
            ServiceController<?> controller = container.getRequiredService(SERVICE_NAME);
            return (BundleContext) controller.getValue();
        } catch (ServiceNotFoundException ex) {
            throw new IllegalStateException("Cannot obtain required service: " + SERVICE_NAME);
        }
    }

    public synchronized void start(final StartContext context) throws StartException {
        sysContext = injectedFramework.getValue().getBundleContext();

        // Register a {@link BundleListener} that installs a {@link ServiceListener}
        // with every Non-OSGi {@link DeploymentService}
        BundleListener bundleListener = new BundleListener() {

            @Override
            public void bundleChanged(BundleEvent event) {
                if (event.getType() == BundleEvent.INSTALLED) {

                    AbstractUserBundle userBundle;
                    try {
                        userBundle = AbstractUserBundle.assertBundleState(event.getBundle());
                    } catch (RuntimeException ex) {
                        // ignore
                        return;
                    }

                    Deployment dep = userBundle.getDeployment();
                    String contextName = DeploymentHolderService.getContextName(dep);

                    // Check if we have an {@link OSGiDeploymentService}
                    ServiceContainer container = context.getController().getServiceContainer();
                    ServiceName osgiDeploymentService = OSGiDeploymentService.getServiceName(contextName);
                    ServiceName deploymentService = Services.JBOSS_DEPLOYMENT.append(contextName);
                    if (container.getService(deploymentService) != null && container.getService(osgiDeploymentService) == null) {
                        ServiceName serviceName = ModuleRegistrationService.getServiceName(contextName);
                        try {
                            log.tracef("Register service: %s", serviceName);
                            BatchBuilder batchBuilder = container.batchBuilder();
                            ModuleRegistrationService.addService(batchBuilder, dep, contextName);
                            batchBuilder.install();
                        } catch (ServiceRegistryException ex) {
                            throw new IllegalStateException("Cannot register service: " + serviceName, ex);
                        }
                    }
                }
            }
        };
        sysContext.addBundleListener(bundleListener);
    }

    public synchronized void stop(StopContext context) {
        sysContext = null;
    }

    @Override
    public BundleContext getValue() throws IllegalStateException {
        return sysContext;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9402.java