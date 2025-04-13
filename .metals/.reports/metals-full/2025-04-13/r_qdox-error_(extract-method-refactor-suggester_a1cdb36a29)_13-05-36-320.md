error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4340.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4340.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4340.java
text:
```scala
s@@erverInventory.shutdown(-1, true); // TODO graceful shutdown // TODO async

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.host.controller;

import org.jboss.as.remoting.management.ManagementChannelRegistryService;

import static org.jboss.msc.service.ServiceController.Mode.ON_DEMAND;
import static org.jboss.as.host.controller.HostControllerLogger.ROOT_LOGGER;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.jboss.as.domain.controller.DomainController;
import org.jboss.as.network.NetworkInterfaceBinding;
import org.jboss.as.server.services.net.NetworkInterfaceService;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.threads.AsyncFutureTask;

/**
 * Service providing the {@link ServerInventory}
 *
 * @author Emanuel Muckenhuber
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
class ServerInventoryService implements Service<ServerInventory> {

    static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("host", "controller", "server-inventory");

    private final InjectedValue<ProcessControllerConnectionService> client = new InjectedValue<ProcessControllerConnectionService>();
    private final InjectedValue<NetworkInterfaceBinding> interfaceBinding = new InjectedValue<NetworkInterfaceBinding>();
    private final DomainController domainController;
    private final HostControllerEnvironment environment;
    private final HostRunningModeControl runningModeControl;
    private final int port;
    private final InjectedValue<ExecutorService> executorService = new InjectedValue<ExecutorService>();

    private final FutureServerInventory futureInventory = new FutureServerInventory();

    private ServerInventoryImpl serverInventory;

    private ServerInventoryService(final DomainController domainController, final HostRunningModeControl runningModeControl, final HostControllerEnvironment environment, final int port) {
        this.domainController = domainController;
        this.runningModeControl = runningModeControl;
        this.environment = environment;
        this.port = port;
    }

    static Future<ServerInventory> install(final ServiceTarget serviceTarget, final DomainController domainController, final HostRunningModeControl runningModeControl, final HostControllerEnvironment environment,
                                           final String interfaceBinding, final int port){
        final ServerInventoryCallbackService callbackService = new ServerInventoryCallbackService();
        serviceTarget.addService(ServerInventoryCallbackService.SERVICE_NAME, callbackService)
                .addDependency(ServerInventoryService.SERVICE_NAME, ServerInventory.class, callbackService.getServerInventoryInjectedValue())
                .setInitialMode(ON_DEMAND)
                .install();

        final ServerInventoryService inventory = new ServerInventoryService(domainController, runningModeControl, environment, port);
        serviceTarget.addService(ServerInventoryService.SERVICE_NAME, inventory)
                .addDependency(HostControllerService.HC_EXECUTOR_SERVICE_NAME, ExecutorService.class, inventory.executorService)
                .addDependency(ProcessControllerConnectionService.SERVICE_NAME, ProcessControllerConnectionService.class, inventory.getClient())
                .addDependency(NetworkInterfaceService.JBOSS_NETWORK_INTERFACE.append(interfaceBinding), NetworkInterfaceBinding.class, inventory.interfaceBinding)
                .addDependency(ManagementChannelRegistryService.SERVICE_NAME)
                .install();
        return inventory.futureInventory;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void start(StartContext context) throws StartException {
        ROOT_LOGGER.debug("Starting Host Controller Server Inventory");
        try {
            final ProcessControllerConnectionService processControllerConnectionService = client.getValue();
            final InetSocketAddress binding = new InetSocketAddress(interfaceBinding.getValue().getAddress(), port);
            serverInventory = new ServerInventoryImpl(domainController, environment, binding, processControllerConnectionService.getClient());
            processControllerConnectionService.setServerInventory(serverInventory);
            futureInventory.setInventory(serverInventory);
        } catch (Exception e) {
            futureInventory.setFailure(e);
            throw new StartException(e);
        }
    }

    /**
     * Stops all servers.
     *
     * {@inheritDoc}
     */
    @Override
    public synchronized void stop(final StopContext context) {
        if (runningModeControl.getRestartMode() == RestartMode.SERVERS) {
            context.asynchronous();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    try {
                        serverInventory.stopServers(-1, true); // TODO graceful shutdown // TODO async
                        serverInventory = null;
                        // client.getValue().setServerInventory(null);
                    } finally {
                        context.complete();
                    }
                }
            };
            executorService.getValue().execute(r);
        }
    }

    /** {@inheritDoc} */
    @Override
    public synchronized ServerInventory getValue() throws IllegalStateException, IllegalArgumentException {
        final ServerInventory serverInventory = this.serverInventory;
        if(serverInventory == null) {
            throw new IllegalStateException();
        }
        return serverInventory;
    }

    InjectedValue<ProcessControllerConnectionService> getClient() {
        return client;
    }

    private class FutureServerInventory extends AsyncFutureTask<ServerInventory>{

        protected FutureServerInventory() {
            super(null);
        }

        private void setInventory(ServerInventory inventory) {
            super.setResult(inventory);
        }

        private void setFailure(final Throwable t) {
            super.setFailed(t);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4340.java