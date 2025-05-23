error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10493.java
text:
```scala
final S@@tring beanName = this.context.getBeanName();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.wildfly.clustering.ejb.infinispan;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

import org.infinispan.Cache;
import org.infinispan.remoting.transport.Address;
import org.jboss.as.clustering.infinispan.affinity.KeyAffinityServiceFactory;
import org.jboss.as.clustering.infinispan.affinity.KeyAffinityServiceFactoryService;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.as.clustering.infinispan.invoker.RetryingCacheInvoker;
import org.jboss.as.clustering.infinispan.subsystem.CacheService;
import org.jboss.as.clustering.marshalling.MarshalledValueFactory;
import org.jboss.as.clustering.marshalling.MarshallingContext;
import org.jboss.as.clustering.marshalling.SimpleMarshalledValueFactory;
import org.jboss.as.clustering.marshalling.SimpleMarshallingContextFactory;
import org.jboss.as.clustering.marshalling.VersionedMarshallingConfiguration;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;
import org.wildfly.clustering.dispatcher.CommandDispatcherFactory;
import org.wildfly.clustering.ejb.BeanContext;
import org.wildfly.clustering.ejb.BeanManager;
import org.wildfly.clustering.ejb.BeanManagerFactory;
import org.wildfly.clustering.ejb.BeanManagerFactoryBuilderConfiguration;
import org.wildfly.clustering.ejb.BeanPassivationConfiguration;
import org.wildfly.clustering.ejb.PassivationListener;
import org.wildfly.clustering.ejb.IdentifierFactory;
import org.wildfly.clustering.ejb.RemoveListener;
import org.wildfly.clustering.ejb.Time;
import org.wildfly.clustering.ejb.infinispan.bean.InfinispanBeanFactory;
import org.wildfly.clustering.ejb.infinispan.group.InfinispanBeanGroupFactory;
import org.wildfly.clustering.group.NodeFactory;
import org.wildfly.clustering.registry.Registry;
import org.wildfly.clustering.spi.CacheServiceNames;
import org.wildfly.clustering.spi.ChannelServiceNames;

/**
 * Factory for creating an infinispan-based {@link BeanManager}.
 *
 * @author Paul Ferraro
 *
 * @param <G> the group identifier type
 * @param <I> the bean identifier type
 * @param <T> the bean type
 */
@SuppressWarnings("rawtypes")
public class InfinispanBeanManagerFactory<G, I, T> extends AbstractService<BeanManagerFactory<G, I, T>> implements BeanManagerFactory<G, I, T> {

    public static <G, I, T> ServiceBuilder<BeanManagerFactory<G, I, T>> build(String name, ServiceTarget target, ServiceName serviceName, BeanManagerFactoryBuilderConfiguration config, BeanContext context) {
        InfinispanBeanManagerFactory<G, I, T> factory = new InfinispanBeanManagerFactory<>(context, config);
        String containerName = config.getContainerName();
        ServiceName deploymentUnitServiceName = context.getDeploymentUnitServiceName();
        return target.addService(serviceName, factory)
                .addDependency(CacheService.getServiceName(containerName, BeanCacheConfigurationService.getCacheName(context.getDeploymentUnitServiceName())), Cache.class, factory.cache)
                .addDependency(KeyAffinityServiceFactoryService.getServiceName(containerName), KeyAffinityServiceFactory.class, factory.affinityFactory)
                .addDependency(deploymentUnitServiceName.append("marshalling"), VersionedMarshallingConfiguration.class, factory.config)
                .addDependency(deploymentUnitServiceName.append(name, "expiration"), ScheduledExecutorService.class, factory.scheduler)
                .addDependency(deploymentUnitServiceName.append(name, "eviction"), Executor.class, factory.executor)
                .addDependency(ChannelServiceNames.COMMAND_DISPATCHER.getServiceName(containerName), CommandDispatcherFactory.class, factory.dispatcherFactory)
                .addDependency(CacheServiceNames.REGISTRY.getServiceName(containerName), Registry.class, factory.registry)
                .addDependency(CacheServiceNames.NODE_FACTORY.getServiceName(containerName), NodeFactory.class, factory.nodeFactory)
        ;
    }

    private final BeanContext context;
    private final CacheInvoker invoker = new RetryingCacheInvoker(10, 100);
    private final InjectedValue<Cache> cache = new InjectedValue<>();
    private final InjectedValue<KeyAffinityServiceFactory> affinityFactory = new InjectedValue<>();
    private final InjectedValue<VersionedMarshallingConfiguration> config = new InjectedValue<>();
    private final InjectedValue<ScheduledExecutorService> scheduler = new InjectedValue<>();
    private final InjectedValue<Executor> executor = new InjectedValue<>();
    private final BeanPassivationConfiguration passivationConfig;
    private final InjectedValue<NodeFactory> nodeFactory = new InjectedValue<>();
    private final InjectedValue<Registry> registry = new InjectedValue<>();
    private final InjectedValue<CommandDispatcherFactory> dispatcherFactory = new InjectedValue<>();

    private InfinispanBeanManagerFactory(BeanContext context, BeanPassivationConfiguration passivationConfig) {
        this.context = context;
        this.passivationConfig = passivationConfig;
    }

    @Override
    public BeanManager<G, I, T> createBeanManager(final IdentifierFactory<G> groupIdentifierFactory, final IdentifierFactory<I> beanIdentifierFactory, final PassivationListener<T> passivationListener, final RemoveListener<T> removeListener) {
        MarshallingContext context = new SimpleMarshallingContextFactory().createMarshallingContext(this.config.getValue(), this.context.getClassLoader());
        MarshalledValueFactory<MarshallingContext> factory = new SimpleMarshalledValueFactory(context);
        Cache<G, BeanGroupEntry<I, T>> groupCache = this.cache.getValue();
        org.infinispan.configuration.cache.Configuration config = groupCache.getCacheConfiguration();
        BeanGroupFactory<G, I, T> groupFactory = new InfinispanBeanGroupFactory<>(groupCache, this.invoker, factory, context);
        Configuration<G, G, BeanGroupEntry<I, T>, BeanGroupFactory<G, I, T>> groupConfiguration = new SimpleConfiguration<>(groupCache, groupFactory, groupIdentifierFactory);
        Cache<BeanKey<I>, BeanEntry<G>> beanCache = this.cache.getValue();
        final String beanName = this.context.getBeanClass().getName();
        // If cache is clustered or configured with a write-through cache store
        // then we need to trigger any @PrePassivate/@PostActivate per request
        // See EJB.4.2.1 Instance Passivation and Conversational State
        final boolean evictionAllowed = config.persistence().usingStores();
        final boolean passivationEnabled = evictionAllowed && config.persistence().passivation();
        final boolean persistent = config.clustering().cacheMode().isClustered() || (evictionAllowed && !passivationEnabled);
        BeanFactory<G, I, T> beanFactory = new InfinispanBeanFactory<>(beanName, groupFactory, beanCache, this.invoker, this.context.getTimeout(), persistent ? passivationListener : null);
        Configuration<I, BeanKey<I>, BeanEntry<G>, BeanFactory<G, I, T>> beanConfiguration = new SimpleConfiguration<>(beanCache, beanFactory, beanIdentifierFactory);
        final NodeFactory<Address> nodeFactory = this.nodeFactory.getValue();
        final Registry<String, ?> registry = this.registry.getValue();
        final KeyAffinityServiceFactory affinityFactory = this.affinityFactory.getValue();
        final CommandDispatcherFactory dispatcherFactory = this.dispatcherFactory.getValue();
        final Time timeout = this.context.getTimeout();
        final ScheduledExecutorService scheduler = this.scheduler.getValue();
        final ExpirationConfiguration<T> expiration = new ExpirationConfiguration<T>() {
            @Override
            public Time getTimeout() {
                return timeout;
            }

            @Override
            public RemoveListener<T> getRemoveListener() {
                return removeListener;
            }

            @Override
            public ScheduledExecutorService getExecutor() {
                return scheduler;
            }
        };
        final Executor executor = this.executor.getValue();
        final BeanPassivationConfiguration passivationConfig = this.passivationConfig;
        final PassivationConfiguration<T> passivation = new PassivationConfiguration<T>() {
            @Override
            public PassivationListener<T> getPassivationListener() {
                return passivationListener;
            }

            @Override
            public boolean isEvictionAllowed() {
                return evictionAllowed;
            }

            @Override
            public boolean isPersistent() {
                return persistent;
            }

            @Override
            public BeanPassivationConfiguration getConfiguration() {
                return passivationConfig;
            }

            @Override
            public Executor getExecutor() {
                return executor;
            }
        };
        InfinispanBeanManagerConfiguration<T> configuration = new InfinispanBeanManagerConfiguration<T>() {
            @Override
            public String getBeanName() {
                return beanName;
            }

            @Override
            public KeyAffinityServiceFactory getAffinityFactory() {
                return affinityFactory;
            }

            @Override
            public Registry<String, ?> getRegistry() {
                return registry;
            }

            @Override
            public NodeFactory<Address> getNodeFactory() {
                return nodeFactory;
            }

            @Override
            public CommandDispatcherFactory getCommandDispatcherFactory() {
                return dispatcherFactory;
            }

            @Override
            public ExpirationConfiguration<T> getExpirationConfiguration() {
                return expiration;
            }

            @Override
            public PassivationConfiguration<T> getPassivationConfiguration() {
                return passivation;
            }
        };
        return new InfinispanBeanManager<>(configuration, beanConfiguration, groupConfiguration);
    }

    @Override
    public BeanManagerFactory<G, I, T> getValue() {
        return this;
    }

    private static class SimpleConfiguration<I, K, V, F> implements Configuration<I, K, V, F> {
        private final F factory;
        private final Cache<K, V> cache;
        private final IdentifierFactory<I> identifierFactory;

        SimpleConfiguration(Cache<K, V> cache, F factory, IdentifierFactory<I> identifierFactory) {
            this.factory = factory;
            this.cache = cache;
            this.identifierFactory = identifierFactory;
        }

        @Override
        public F getFactory() {
            return this.factory;
        }

        @Override
        public Cache<K, V> getCache() {
            return this.cache;
        }

        @Override
        public IdentifierFactory<I> getIdentifierFactory() {
            return this.identifierFactory;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10493.java