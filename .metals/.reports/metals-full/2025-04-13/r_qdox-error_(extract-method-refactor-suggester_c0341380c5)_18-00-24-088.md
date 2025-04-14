error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8486.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8486.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8486.java
text:
```scala
r@@eturn (this.electionPolicy == null) ? nodes.get(0) : this.electionPolicy.elect(nodes);

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

package org.jboss.as.clustering.singleton;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.clustering.ClusterNode;
import org.jboss.as.clustering.GroupRpcDispatcher;
import org.jboss.as.clustering.service.ServiceProviderRegistry;
import org.jboss.as.clustering.service.ServiceProviderRegistryService;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * Decorates an MSC service ensuring that it is only started on one node in the cluster at any given time.
 * @author Paul Ferraro
 */
public class SingletonService<T extends Serializable> implements Service<T>, ServiceProviderRegistry.Listener, StopContext, SingletonRpcHandler<T> {

    public static final String DEFAULT_CONTAINER = "cluster";

    private final InjectedValue<ServiceProviderRegistry> registryRef = new InjectedValue<ServiceProviderRegistry>();
    private final InjectedValue<GroupRpcDispatcher> dispatcherRef = new InjectedValue<GroupRpcDispatcher>();
    private final Service<T> service;
    private final ServiceName serviceName;
    private final AtomicBoolean master = new AtomicBoolean(false);

    private volatile ServiceProviderRegistry registry;
    private volatile GroupRpcDispatcher dispatcher;
    private volatile SingletonElectionPolicy electionPolicy;
    private volatile SingletonRpcHandler<T> handler;
    private volatile StartContext context;
    private volatile boolean restartOnMerge = true;

    public SingletonService(Service<T> service, ServiceName serviceName) {
        this.service = service;
        this.serviceName = serviceName;
    }

    public ServiceBuilder<T> build(ServiceContainer target) {
        return this.build(target, DEFAULT_CONTAINER);
    }

    public ServiceBuilder<T> build(ServiceContainer target, String container) {
        ServiceName registryName = ServiceProviderRegistryService.getServiceName(container);
        synchronized (target) {
            if (target.getService(registryName) == null) {
                new ServiceProviderRegistryService().build(target, container).setInitialMode(ServiceController.Mode.ON_DEMAND).install();
            }
        }
        return target.addService(this.serviceName, this)
            .addDependency(ServiceProviderRegistryService.getServiceName(container), ServiceProviderRegistry.class, this.registryRef)
            .addDependency(ServiceName.JBOSS.append(DEFAULT_CONTAINER, container), GroupRpcDispatcher.class, this.dispatcherRef)
        ;
    }

    @Override
    public void start(StartContext context) throws StartException {
        this.context = context;
        this.dispatcher = this.dispatcherRef.getValue();
        this.registry = this.registryRef.getValue();
        String name = this.serviceName.getCanonicalName();
        this.handler = new RpcHandler(this.dispatcher, name);
        this.dispatcher.registerRPCHandler(name, this, SingletonService.class.getClassLoader());
        this.registry.register(name, this);
    }

    @Override
    public void stop(StopContext context) {
        String name = this.serviceName.getCanonicalName();
        this.registry.unregister(name);
        this.dispatcher.unregisterRPCHandler(name, this);
    }

    public void setElectionPolicy(SingletonElectionPolicy electionPolicy) {
        this.electionPolicy = electionPolicy;
    }

    public void setRestartOnMerge(boolean restart) {
        this.restartOnMerge = restart;
    }

    @Override
    public void serviceProvidersChanged(Set<ClusterNode> nodes, boolean merge) {
        if (this.elected(nodes)) {
            if (this.master.get()) {
                // If already master, don't bother re-electing, just restart if necessary
                if (this.restartOnMerge && merge) {
                    this.stopOldMaster();
                    this.startNewMaster();
                }
            } else {
                SingletonLogger.ROOT_LOGGER.electedMaster(this.serviceName.getCanonicalName());
                this.handler.stopOldMaster();
                this.startNewMaster();
            }
        } else if (this.master.get()) {
            SingletonLogger.ROOT_LOGGER.electedSlave(this.serviceName.getCanonicalName());
            this.stopOldMaster();
        }
    }

    private boolean elected(Set<ClusterNode> candidates) {
        ClusterNode elected = this.election(candidates);
        return (elected != null) ? elected.equals(this.dispatcher.getClusterNode()) : false;
    }

    private ClusterNode election(Set<ClusterNode> candidates) {
        List<ClusterNode> nodes = this.dispatcher.getClusterNodes();

        nodes.retainAll(candidates);

        if (nodes.isEmpty()) return null;

        return (this.electionPolicy == null) || (nodes.size() == 1) ? nodes.get(0) : this.electionPolicy.elect(nodes);
    }

    private void startNewMaster() {
        this.master.set(true);
        try {
            this.service.start(this.context);
        } catch (StartException e) {
            this.master.set(false);
            throw new RuntimeException(e);
        }
    }

    @Override
    public T getValue() {
        AtomicReference<T> ref = this.getValueRef();
        return (ref != null) ? ref.get() : this.handler.getValueRef().get();
    }

    @Override
    public AtomicReference<T> getValueRef() {
        return this.master.get() ? new AtomicReference<T>(this.service.getValue()) : null;
    }

    @Override
    public void stopOldMaster() {
        if (this.master.compareAndSet(true, false)) {
            this.service.stop(this);
        }
    }

    @Override
    public void asynchronous() throws IllegalStateException {
        this.context.asynchronous();
    }

    @Override
    public void complete() throws IllegalStateException {
        this.context.complete();
    }

    @Override
    public long getElapsedTime() {
        return this.context.getElapsedTime();
    }

    @Override
    public ServiceController<?> getController() {
        return this.context.getController();
    }

    @Override
    public void execute(Runnable command) {
        this.context.execute(command);
    }

    private class RpcHandler implements SingletonRpcHandler<T> {
        private final GroupRpcDispatcher dispatcher;
        private String name;

        RpcHandler(GroupRpcDispatcher dispatcher, String name) {
            this.dispatcher = dispatcher;
            this.name = name;
        }

        @Override
        public void stopOldMaster() {
            try {
                this.dispatcher.callMethodOnCluster(this.name, "stopOldMaster", new Object[0], new Class<?>[0], true);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public AtomicReference<T> getValueRef() {
            try {
                List<AtomicReference<T>> results = this.dispatcher.callMethodOnCluster(this.name, "getValueRef", new Object[0], new Class<?>[0], false);
                Iterator<AtomicReference<T>> refs = results.iterator();
                while (refs.hasNext()) {
                    if (refs.next() == null) {
                        refs.remove();
                    }
                }
                int count = results.size();
                if (count != 1) {
                    throw SingletonMessages.MESSAGES.unexpectedResponseCount(count);
                }
                return results.get(0);
            } catch (Exception e) {
                throw new IllegalStateException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8486.java