error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4784.java
text:
```scala
b@@undle.start(Bundle.START_TRANSIENT);

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

import static org.jboss.as.osgi.service.FrameworkBootstrapService.FRAMEWORK_BASE_NAME;
import static org.osgi.service.packageadmin.PackageAdmin.BUNDLE_TYPE_FRAGMENT;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceController.State;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.deployment.deployer.Deployment;
import org.jboss.osgi.framework.Services;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * A service that collects installed bundle services and starts them when all collected bundles were installed in the framework.
 *
 * @author Thomas.Diesler@jboss.com
 * @since 30-Mar-2011
 */
public class BundleStartTracker extends AbstractService<BundleStartTracker> {

    private static final Logger log = Logger.getLogger("org.jboss.as.osgi");

    public static final ServiceName SERVICE_NAME = FRAMEWORK_BASE_NAME.append("starttracker");

    private final InjectedValue<PackageAdmin> injectedPackageAdmin = new InjectedValue<PackageAdmin>();
    private final Map<ServiceName, Tuple> pendingServices = new ConcurrentHashMap<ServiceName, Tuple>();
    private final Map<ServiceName, Tuple> startedServices = new ConcurrentHashMap<ServiceName, Tuple>();
    private ServiceContainer serviceContainer;

    public static void addService(ServiceTarget serviceTarget) {
        BundleStartTracker service = new BundleStartTracker();
        ServiceBuilder<BundleStartTracker> builder = serviceTarget.addService(SERVICE_NAME, service);
        builder.addDependency(Services.PACKAGE_ADMIN, PackageAdmin.class, service.injectedPackageAdmin);
        builder.setInitialMode(Mode.PASSIVE);
        builder.install();
    }

    private BundleStartTracker() {
    }

    @Override
    public void start(StartContext context) throws StartException {
        serviceContainer = context.getController().getServiceContainer();
    }

    @Override
    public BundleStartTracker getValue() throws IllegalStateException {
        return this;
    }

    @SuppressWarnings("unchecked")
    void addInstalledBundle(ServiceName serviceName, Deployment deployment) {
        ServiceController<Bundle> controller = (ServiceController<Bundle>) serviceContainer.getRequiredService(serviceName);
        pendingServices.put(serviceName, new Tuple(controller, deployment));
        controller.addListener(new AbstractServiceListener<Bundle>() {

            @Override
            public void listenerAdded(ServiceController<? extends Bundle> controller) {
                State state = controller.getState();
                if (state == State.UP || state == State.START_FAILED)
                    processService(controller);
            }

            @Override
            public void serviceStarted(ServiceController<? extends Bundle> controller) {
                ServiceName key = controller.getName();
                Tuple value = pendingServices.get(key);
                startedServices.put(key, value);
                processService(controller);
            }

            @Override
            public void serviceFailed(ServiceController<? extends Bundle> controller, StartException reason) {
                processService(controller);
            }

            private void processService(ServiceController<? extends Bundle> controller) {
                controller.removeListener(this);
                Map<ServiceName, Tuple> bundlesToStart = null;
                synchronized (this) {
                    ServiceName key = controller.getName();
                    pendingServices.remove(key);
                    if (pendingServices.isEmpty()) {
                        bundlesToStart = new HashMap<ServiceName, Tuple>(startedServices);
                        startedServices.clear();
                    }
                }
                if (bundlesToStart != null) {
                    PackageAdmin packageAdmin = injectedPackageAdmin.getValue();
                    for (Tuple tuple : bundlesToStart.values()) {
                        Bundle bundle = tuple.controller.getValue();
                        Deployment dep = tuple.deployment;
                        if (dep.isAutoStart()) {
                            try {
                                int bundleType = packageAdmin.getBundleType(bundle);
                                if (bundleType != BUNDLE_TYPE_FRAGMENT) {
                                    bundle.start();
                                }
                            } catch (BundleException ex) {
                                log.errorf(ex, "Cannot start bundle: %s", bundle);
                            }
                        }
                    }
                }
            }
        });
    }

    private static final class Tuple {
        private ServiceController<? extends Bundle> controller;
        private Deployment deployment;

        Tuple(ServiceController<? extends Bundle> controller, Deployment deployment) {
            this.controller = controller;
            this.deployment = deployment;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4784.java