error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10502.java
text:
```scala
b@@uilder.addDependency(DependencyType.OPTIONAL, SharedLocalYieldingClusterLockManagerService.getServiceName(containerName), SharedLocalYieldingClusterLockManager.class, this.lockManager);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.clustering.web.infinispan;

import static org.jboss.as.clustering.web.infinispan.InfinispanWebMessages.MESSAGES;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.infinispan.AdvancedCache;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.manager.CacheContainer;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.as.clustering.infinispan.affinity.KeyAffinityServiceFactory;
import org.jboss.as.clustering.infinispan.affinity.KeyAffinityServiceFactoryService;
import org.jboss.as.clustering.infinispan.atomic.AtomicMapCache;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.as.clustering.infinispan.invoker.RetryingCacheInvoker;
import org.jboss.as.clustering.infinispan.subsystem.AbstractCacheConfigurationService;
import org.jboss.as.clustering.infinispan.subsystem.CacheService;
import org.jboss.as.clustering.infinispan.subsystem.EmbeddedCacheManagerService;
import org.jboss.as.clustering.jgroups.subsystem.ChannelService;
import org.jboss.as.clustering.lock.SharedLocalYieldingClusterLockManager;
import org.jboss.as.clustering.lock.impl.SharedLocalYieldingClusterLockManagerService;
import org.jboss.as.clustering.msc.AsynchronousService;
import org.jboss.as.clustering.registry.Registry;
import org.jboss.as.clustering.registry.RegistryService;
import org.jboss.as.clustering.web.BatchingManager;
import org.jboss.as.clustering.web.ClusteringNotSupportedException;
import org.jboss.as.clustering.web.DistributedCacheManagerFactoryService;
import org.jboss.as.clustering.web.LocalDistributableSessionManager;
import org.jboss.as.clustering.web.OutgoingDistributableSessionData;
import org.jboss.as.clustering.web.SessionAttributeMarshallerFactory;
import org.jboss.as.clustering.web.impl.SessionAttributeMarshallerFactoryImpl;
import org.jboss.as.clustering.web.impl.TransactionBatchingManager;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.jboss.ReplicationConfig;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.value.InjectedValue;
import org.jboss.tm.XAResourceRecoveryRegistry;

/**
 * Factory for creating an Infinispan-backed distributed cache manager.
 *
 * @author Paul Ferraro
 */
public class DistributedCacheManagerFactory implements org.jboss.as.clustering.web.DistributedCacheManagerFactory {
    public static final String DEFAULT_CACHE_CONTAINER = "web";
    private static final ServiceName JVM_ROUTE_REGISTRY_SERVICE_NAME = DistributedCacheManagerFactoryService.JVM_ROUTE_REGISTRY_ENTRY_PROVIDER_SERVICE_NAME.getParent();

    private SessionAttributeStorageFactory storageFactory = new SessionAttributeStorageFactoryImpl();
    private CacheInvoker invoker = new RetryingCacheInvoker(10, 100);
    private SessionAttributeMarshallerFactory marshallerFactory = new SessionAttributeMarshallerFactoryImpl();
    @SuppressWarnings("rawtypes")
    private final InjectedValue<Registry> registry = new InjectedValue<Registry>();
    private final InjectedValue<SharedLocalYieldingClusterLockManager> lockManager = new InjectedValue<SharedLocalYieldingClusterLockManager>();
    @SuppressWarnings("rawtypes")
    private final InjectedValue<Cache> cache = new InjectedValue<Cache>();
    private final InjectedValue<KeyAffinityServiceFactory> affinityFactory = new InjectedValue<KeyAffinityServiceFactory>();

    @Override
    public <T extends OutgoingDistributableSessionData> org.jboss.as.clustering.web.DistributedCacheManager<T> getDistributedCacheManager(LocalDistributableSessionManager manager) throws ClusteringNotSupportedException {
        @SuppressWarnings("unchecked")
        Registry<String, Void> jvmRouteRegistry = this.registry.getValue();
        @SuppressWarnings("unchecked")
        AdvancedCache<String, Map<Object, Object>> cache = this.cache.getValue().getAdvancedCache();

        if (!cache.getCacheConfiguration().invocationBatching().enabled()) {
            ServiceName cacheServiceName = this.getCacheServiceName(manager.getReplicationConfig());
            throw new ClusteringNotSupportedException(MESSAGES.failedToConfigureWebApp(cacheServiceName.getParent().getSimpleName(), cacheServiceName.getSimpleName()));
        }

        BatchingManager batchingManager = new TransactionBatchingManager(cache.getTransactionManager());
        SessionAttributeStorage<T> storage = this.storageFactory.createStorage(manager.getReplicationConfig().getReplicationGranularity(), this.marshallerFactory.createMarshaller(manager));

        return new DistributedCacheManager<T>(manager, new AtomicMapCache<String, Object, Object>(cache), jvmRouteRegistry, this.lockManager.getOptionalValue(), storage, batchingManager, this.invoker, this.affinityFactory.getValue());
    }

    @Override
    public boolean addDeploymentDependencies(ServiceName deploymentServiceName, ServiceRegistry registry, ServiceTarget target, ServiceBuilder<?> builder, JBossWebMetaData metaData) {
        ServiceName templateCacheServiceName = this.getCacheServiceName(metaData.getReplicationConfig());
        if (registry.getService(templateCacheServiceName) == null) {
            return false;
        }
        String templateCacheName = templateCacheServiceName.getSimpleName();
        ServiceName containerServiceName = templateCacheServiceName.getParent();
        String containerName = containerServiceName.getSimpleName();
        ServiceName templateCacheConfigurationServiceName = AbstractCacheConfigurationService.getServiceName(containerName, templateCacheName);
        String cacheName = deploymentServiceName.getParent().getSimpleName() + deploymentServiceName.getSimpleName();
        ServiceName cacheConfigurationServiceName = AbstractCacheConfigurationService.getServiceName(containerName, cacheName);
        ServiceName cacheServiceName = CacheService.getServiceName(containerName, cacheName);

        final InjectedValue<EmbeddedCacheManager> container = new InjectedValue<EmbeddedCacheManager>();
        final InjectedValue<Configuration> config = new InjectedValue<Configuration>();
        target.addService(cacheConfigurationServiceName, new WebSessionCacheConfigurationService(cacheName, container, config))
                .addDependency(containerServiceName, EmbeddedCacheManager.class, container)
                .addDependency(templateCacheConfigurationServiceName, Configuration.class, config)
                .setInitialMode(ServiceController.Mode.ON_DEMAND)
                .install()
        ;
        final InjectedValue<EmbeddedCacheManager> cacheContainer = new InjectedValue<EmbeddedCacheManager>();
        CacheService.Dependencies dependencies = new CacheService.Dependencies() {
            @Override
            public EmbeddedCacheManager getCacheContainer() {
                return cacheContainer.getValue();
            }

            @Override
            public XAResourceRecoveryRegistry getRecoveryRegistry() {
                return null;
            }
        };
        AsynchronousService.addService(target, cacheServiceName, new CacheService<Object, Object>(cacheName, dependencies))
                .addDependency(cacheConfigurationServiceName)
                .addDependency(containerServiceName, EmbeddedCacheManager.class, cacheContainer)
                .addDependency(DependencyType.OPTIONAL, ChannelService.getServiceName(containerName))
                .setInitialMode(ServiceController.Mode.ON_DEMAND)
                .install()
        ;
        builder.addDependency(cacheServiceName, Cache.class, this.cache);
        builder.addDependency(JVM_ROUTE_REGISTRY_SERVICE_NAME, Registry.class, this.registry);
        builder.addDependency(SharedLocalYieldingClusterLockManagerService.getServiceName(containerName), SharedLocalYieldingClusterLockManager.class, this.lockManager);
        builder.addDependency(KeyAffinityServiceFactoryService.getServiceName(containerName), KeyAffinityServiceFactory.class, this.affinityFactory);
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Collection<ServiceController<?>> installServices(ServiceTarget target) {
        InjectedValue<Cache> cache = new InjectedValue<Cache>();
        InjectedValue<Registry.RegistryEntryProvider> providerValue = new InjectedValue<Registry.RegistryEntryProvider>();
        ServiceController<?> controller = target.addService(JVM_ROUTE_REGISTRY_SERVICE_NAME, new RegistryService(cache, providerValue))
                .addDependency(CacheService.getServiceName(DEFAULT_CACHE_CONTAINER, null), Cache.class, cache)
                .addDependency(DistributedCacheManagerFactoryService.JVM_ROUTE_REGISTRY_ENTRY_PROVIDER_SERVICE_NAME, Registry.RegistryEntryProvider.class, providerValue)
                .setInitialMode(ServiceController.Mode.ON_DEMAND)
                .install()
        ;
        return Collections.<ServiceController<?>>singleton(controller);
    }

    private ServiceName getCacheServiceName(ReplicationConfig config) {
        ServiceName baseServiceName = EmbeddedCacheManagerService.getServiceName(null);
        String cacheName = (config != null) ? config.getCacheName() : null;
        ServiceName serviceName = ServiceName.parse((cacheName != null) ? cacheName : DEFAULT_CACHE_CONTAINER);
        if (!baseServiceName.isParentOf(serviceName)) {
            serviceName = baseServiceName.append(serviceName);
        }
        return (serviceName.length() < 4) ? serviceName.append(CacheContainer.DEFAULT_CACHE_NAME) : serviceName;
    }

    @SuppressWarnings("rawtypes")
    public Injector<Registry> getRegistryInjector() {
        return this.registry;
    }

    public Injector<SharedLocalYieldingClusterLockManager> getLockManagerInjector() {
        return this.lockManager;
    }

    @SuppressWarnings("rawtypes")
    public Injector<Cache> getCacheInjector() {
        return this.cache;
    }

    public void setSessionAttributeMarshallerFactory(SessionAttributeMarshallerFactory marshallerFactory) {
        this.marshallerFactory = marshallerFactory;
    }

    public void setSessionAttributeStorageFactory(SessionAttributeStorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public void setCacheInvoker(CacheInvoker invoker) {
        this.invoker = invoker;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10502.java