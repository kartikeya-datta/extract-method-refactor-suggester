error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6730.java
text:
```scala
t@@hrow ServerMessages.MESSAGES.timeoutWaitingForModuleService(identifier);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
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
package org.jboss.as.server.moduleservice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.as.server.Bootstrap;
import org.jboss.as.server.ServerLogger;
import org.jboss.as.server.ServerMessages;
import org.jboss.as.server.Services;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.ModuleSpec;
import org.jboss.msc.service.AbstractServiceListener;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.State;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

/**
 * {@link ModuleLoader} that loads module definitions from msc services. Module specs are looked up in msc services that
 * correspond to the module names.
 * <p>
 * Modules are automatically removed when the corresponding service comes down.
 *
 * @author Stuart Douglas
 *
 */
public class ServiceModuleLoader extends ModuleLoader implements Service<ServiceModuleLoader> {

    // Provide logging
    private static final ServerLogger log = ServerLogger.MODULE_SERVICE_LOGGER;

    /**
     * Listener class that atomically retrieves the moduleSpec, and automatically removes the Module when the module spec
     * service is removed
     *
     * @author Stuart Douglas
     *
     */
    private class ModuleSpecLoadListener extends AbstractServiceListener<ModuleSpec> {

        private final CountDownLatch latch = new CountDownLatch(1);
        private final ModuleIdentifier identifier;
        private volatile StartException startException;
        private volatile ModuleSpec moduleSpec;

        private ModuleSpecLoadListener(ModuleIdentifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public void listenerAdded(ServiceController<? extends ModuleSpec> controller) {
            log.tracef("listenerAdded: %s", controller);
            State state = controller.getState();
            if (state == State.UP || state == State.START_FAILED) {
                done(controller, controller.getStartException());
            }
        }

        @Override
        public void transition(final ServiceController<? extends ModuleSpec> controller, final ServiceController.Transition transition) {
            switch (transition) {
                case STARTING_to_UP:
                    log.tracef("serviceStarted: %s", controller);
                    done(controller, null);
                    break;
                case STARTING_to_START_FAILED:
                    log.tracef(controller.getStartException(), "serviceFailed: %s", controller);
                    done(controller, controller.getStartException());
                    break;
                case STOP_REQUESTED_to_STOPPING: {
                    log.tracef("serviceStopping: %s", controller);
                    ModuleSpec moduleSpec = this.moduleSpec;
                    try {
                        Module module = loadModule(moduleSpec.getModuleIdentifier());
                        unloadModuleLocal(module);
                    } catch (ModuleLoadException e) {
                        // ignore, the module should always be already loaded by this point,
                        // and if not we will only mask the true problem
                    }
                    // TODO: what if the service is restarted?
                    controller.removeListener(this);
                    break;
                }
            }
        }

        private void done(ServiceController<? extends ModuleSpec> controller, StartException reason) {
            startException = reason;
            if (startException == null) {
                moduleSpec = controller.getValue();
            }
            latch.countDown();
        }

        public ModuleSpec getModuleSpec() throws ModuleLoadException {
            if (moduleSpec != null)
                return moduleSpec;
            if (startException != null)
                throw new ModuleLoadException(startException.getCause());
            try {
                log.tracef("waiting for: %s", identifier);
                if (latch.await(2000, TimeUnit.MILLISECONDS) == false)
                    throw new ModuleLoadException("Timeout waiting for module service: " + identifier);
            } catch (InterruptedException e) {
                // ignore
            }
            return moduleSpec;
        }
    }

    public static final ServiceName MODULE_SPEC_SERVICE_PREFIX = ServiceName.JBOSS.append("module", "spec", "service");

    public static final ServiceName MODULE_SERVICE_PREFIX = ServiceName.JBOSS.append("module", "service");

    public static final String MODULE_PREFIX = "deployment.";

    private final ModuleLoader mainModuleLoader;

    private volatile ServiceContainer serviceContainer;

    public ServiceModuleLoader(ModuleLoader mainModuleLoader) {
        this.mainModuleLoader = mainModuleLoader;
    }

    @Override
    protected Module preloadModule(final ModuleIdentifier identifier) throws ModuleLoadException {
        if (identifier.getName().startsWith(MODULE_PREFIX)) {
            return super.preloadModule(identifier);
        } else {
            return preloadModule(identifier, mainModuleLoader);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModuleSpec findModule(ModuleIdentifier identifier) throws ModuleLoadException {
        ServiceController<ModuleSpec> controller = (ServiceController<ModuleSpec>) serviceContainer.getService(moduleSpecServiceName(identifier));
        if (controller == null) {
            ServerLogger.MODULE_SERVICE_LOGGER.debugf("Could not load module '%s' as corresponding module spec service '%s' was not found", identifier, identifier);
            return null;
        }
        ModuleSpecLoadListener listener = new ModuleSpecLoadListener(identifier);
        controller.addListener(listener);
        return listener.getModuleSpec();
    }

    @Override
    public String toString() {
        return "Service Module Loader";
    }

    @Override
    public synchronized void start(StartContext context) throws StartException {
        if (serviceContainer != null) {
            throw ServerMessages.MESSAGES.serviceModuleLoaderAlreadyStarted();
        }
        serviceContainer = context.getController().getServiceContainer();
    }

    @Override
    public synchronized void stop(StopContext context) {
        if (serviceContainer == null) {
            throw ServerMessages.MESSAGES.serviceModuleLoaderAlreadyStopped();
        }
        serviceContainer = null;
    }

    @Override
    public ServiceModuleLoader getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public void relinkModule(Module module) throws ModuleLoadException {
        relink(module);
    }

    public static void addService(final ServiceTarget serviceTarget, final Bootstrap.Configuration configuration) {
        final Service<ServiceModuleLoader> service = new ServiceModuleLoader(configuration.getModuleLoader());
        final ServiceBuilder<?> serviceBuilder = serviceTarget.addService(Services.JBOSS_SERVICE_MODULE_LOADER, service);
        serviceBuilder.install();
    }

    /**
     * Returns the corresponding ModuleSpec service name for the given module.
     *
     * @param identifier The module identifier
     * @return The service name of the ModuleSpec service
     */
    public static ServiceName moduleSpecServiceName(ModuleIdentifier identifier) {
        if (!identifier.getName().startsWith(MODULE_PREFIX)) {
            ServerMessages.MESSAGES.missingModulePrefix(identifier, MODULE_PREFIX);
        }
        return MODULE_SPEC_SERVICE_PREFIX.append(identifier.getName()).append(identifier.getSlot());
    }

    /**
     * Returns the corresponding ModuleLoadService service name for the given module.
     *
     * @param identifier The module identifier
     * @return The service name of the ModuleLoadService service
     */
    public static ServiceName moduleServiceName(ModuleIdentifier identifier) {
        if (!identifier.getName().startsWith(MODULE_PREFIX)) {
            ServerMessages.MESSAGES.missingModulePrefix(identifier, MODULE_PREFIX);
        }
        return MODULE_SERVICE_PREFIX.append(identifier.getName()).append(identifier.getSlot());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6730.java