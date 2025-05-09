error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11051.java
text:
```scala
r@@eturn InfinispanDescriptions.getCacheContainerAddDescription(locale);

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

package org.jboss.as.clustering.infinispan.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.clustering.infinispan.InfinispanMessages.MESSAGES;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import javax.management.MBeanServer;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.infinispan.Cache;
import org.infinispan.config.Configuration;
import org.infinispan.config.Configuration.CacheMode;
import org.infinispan.config.FluentConfiguration;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.loaders.AbstractCacheStoreConfig;
import org.infinispan.loaders.CacheStore;
import org.infinispan.loaders.CacheStoreConfig;
import org.infinispan.manager.CacheContainer;
import org.infinispan.transaction.LockingMode;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jboss.as.clustering.jgroups.ChannelFactory;
import org.jboss.as.clustering.jgroups.subsystem.ChannelFactoryService;
import org.jboss.as.clustering.jgroups.subsystem.ChannelService;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.naming.ManagedReferenceInjector;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.deployment.JndiName;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.services.path.AbstractPathService;
import org.jboss.as.threads.ThreadsServices;
import org.jboss.as.txn.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceBuilder.DependencyType;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;
import org.jboss.msc.value.Value;
import org.jboss.tm.XAResourceRecoveryRegistry;
import org.jgroups.Channel;

/**
 * @author Paul Ferraro
 */
public class CacheContainerAdd extends AbstractAddStepHandler implements DescriptionProvider {

    static ModelNode createOperation(ModelNode address, ModelNode existing) {
        ModelNode operation = Util.getEmptyOperation(ADD, address);
        populate(existing, operation);
        return operation;
    }

    private static void populate(ModelNode source, ModelNode target) {
        target.get(ModelKeys.DEFAULT_CACHE).set(source.require(ModelKeys.DEFAULT_CACHE));
        if (source.hasDefined(ModelKeys.JNDI_NAME)) {
            target.get(ModelKeys.JNDI_NAME).set(source.get(ModelKeys.JNDI_NAME));
        }
        if (source.hasDefined(ModelKeys.LISTENER_EXECUTOR)) {
            target.get(ModelKeys.LISTENER_EXECUTOR).set(source.get(ModelKeys.LISTENER_EXECUTOR));
        }
        if (source.hasDefined(ModelKeys.EVICTION_EXECUTOR)) {
            target.get(ModelKeys.EVICTION_EXECUTOR).set(source.get(ModelKeys.EVICTION_EXECUTOR));
        }
        if (source.hasDefined(ModelKeys.REPLICATION_QUEUE_EXECUTOR)) {
            target.get(ModelKeys.REPLICATION_QUEUE_EXECUTOR).set(source.get(ModelKeys.REPLICATION_QUEUE_EXECUTOR));
        }
        if (source.hasDefined(ModelKeys.ALIAS)) {
            ModelNode aliases = target.get(ModelKeys.ALIAS);
            for (ModelNode alias : source.get(ModelKeys.ALIAS).asList()) {
                aliases.add(alias);
            }
        }
        if (source.hasDefined(ModelKeys.TRANSPORT)) {
            target.get(ModelKeys.TRANSPORT).set(source.get(ModelKeys.TRANSPORT));
        }
        ModelNode caches = target.get(ModelKeys.CACHE);
        for (ModelNode cache : source.require(ModelKeys.CACHE).asList()) {
            caches.add(cache);
        }
    }

    protected void populateModel(ModelNode operation, ModelNode model) {
        populate(operation, model);
    }

    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) {
        final PathAddress address = PathAddress.pathAddress(operation.get(OP_ADDR));
        final String name = address.getLastElement().getValue();

        String defaultCache = operation.require(ModelKeys.DEFAULT_CACHE).asString();

        EmbeddedCacheManager config = new EmbeddedCacheManager(name, defaultCache);

        ServiceName[] aliases = null;
        if (operation.hasDefined(ModelKeys.ALIAS)) {
            List<ModelNode> list = operation.get(ModelKeys.ALIAS).asList();
            aliases = new ServiceName[list.size()];
            for (int i = 0; i < list.size(); i++) {
                aliases[i] = EmbeddedCacheManagerService.getServiceName(list.get(i).asString());
            }
        }

        ServiceTarget target = context.getServiceTarget();
        ServiceName serviceName = EmbeddedCacheManagerService.getServiceName(name);
        ServiceBuilder<CacheContainer> builder = target.addService(serviceName, new EmbeddedCacheManagerService(config))
                .addDependency(EmbeddedCacheManagerDefaultsService.SERVICE_NAME, EmbeddedCacheManagerDefaults.class, config.getDefaultsInjector())
                .addDependency(DependencyType.OPTIONAL, TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, TransactionManager.class, config.getTransactionManagerInjector())
                .addDependency(DependencyType.OPTIONAL, TxnServices.JBOSS_TXN_SYNCHRONIZATION_REGISTRY, TransactionSynchronizationRegistry.class, config.getTransactionSynchronizationRegistryInjector())
                .addDependency(DependencyType.OPTIONAL, TxnServices.JBOSS_TXN_ARJUNA_RECOVERY_MANAGER, XAResourceRecoveryRegistry.class, config.getXAResourceRecoveryRegistryInjector())
                .addDependency(DependencyType.OPTIONAL, ServiceName.JBOSS.append("mbean", "server"), MBeanServer.class, config.getMBeanServerInjector())
                .addAliases(aliases)
                .setInitialMode(ServiceController.Mode.ON_DEMAND);

        String jndiName = (operation.hasDefined(ModelKeys.JNDI_NAME) ? toJndiName(operation.get(ModelKeys.JNDI_NAME).asString()) : JndiName.of("java:jboss").append(InfinispanExtension.SUBSYSTEM_NAME).append(name)).getAbsoluteName();
        final ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);

        BinderService binder = new BinderService(bindInfo.getBindName());
        newControllers.add(target.addService(bindInfo.getBinderServiceName(), binder)
                .addAliases(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(jndiName))
                .addDependency(serviceName, CacheContainer.class, new ManagedReferenceInjector<CacheContainer>(binder.getManagedObjectInjector()))
                .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class, binder.getNamingStoreInjector())
                .setInitialMode(ServiceController.Mode.ON_DEMAND)
                .install());
        boolean requiresTransport = false;
        Map<String, Configuration> configurations = config.getConfigurations();
        for (ModelNode cache : operation.require(ModelKeys.CACHE).asList()) {
            String cacheName = cache.require(ModelKeys.NAME).asString();
            Configuration configuration = new Configuration();
            configuration.setClassLoader(this.getClass().getClassLoader());
            FluentConfiguration fluent = configuration.fluent();
            Configuration.CacheMode mode = CacheMode.valueOf(cache.require(ModelKeys.MODE).asString());
            requiresTransport |= mode.isClustered();
            fluent.mode(mode);
            if (cache.hasDefined(ModelKeys.BATCHING)) {
                if (cache.get(ModelKeys.BATCHING).asBoolean()) {
                    fluent.invocationBatching();
                }
            }
            if (cache.hasDefined(ModelKeys.INDEXING)) {
                Indexing indexing = Indexing.valueOf(cache.get(ModelKeys.INDEXING).asString());
                if (indexing.isEnabled()) {
                    fluent.indexing().indexLocalOnly(indexing.isLocalOnly());
                }
            }
            if (cache.hasDefined(ModelKeys.QUEUE_SIZE)) {
                fluent.async().replQueueMaxElements(cache.get(ModelKeys.QUEUE_SIZE).asInt());
            }
            if (cache.hasDefined(ModelKeys.QUEUE_FLUSH_INTERVAL)) {
                fluent.async().replQueueInterval(cache.get(ModelKeys.QUEUE_FLUSH_INTERVAL).asLong());
            }
            if (cache.hasDefined(ModelKeys.REMOTE_TIMEOUT)) {
                fluent.sync().replTimeout(cache.get(ModelKeys.REMOTE_TIMEOUT).asLong());
            }
            if (cache.hasDefined(ModelKeys.OWNERS)) {
                fluent.hash().numOwners(cache.get(ModelKeys.OWNERS).asInt());
            }
            if (cache.hasDefined(ModelKeys.VIRTUAL_NODES)) {
                fluent.hash().numVirtualNodes(cache.get(ModelKeys.VIRTUAL_NODES).asInt());
            }
            if (cache.hasDefined(ModelKeys.L1_LIFESPAN)) {
                long lifespan = cache.get(ModelKeys.L1_LIFESPAN).asLong();
                if (lifespan > 0) {
                    fluent.l1().lifespan(lifespan);
                } else {
                    fluent.l1().disable();
                }
            }
            if (cache.hasDefined(ModelKeys.LOCKING)) {
                ModelNode locking = cache.get(ModelKeys.LOCKING);
                FluentConfiguration.LockingConfig fluentLocking = fluent.locking();
                if (locking.hasDefined(ModelKeys.ISOLATION)) {
                    fluentLocking.isolationLevel(IsolationLevel.valueOf(locking.get(ModelKeys.ISOLATION).asString()));
                }
                if (locking.hasDefined(ModelKeys.STRIPING)) {
                    fluentLocking.useLockStriping(locking.get(ModelKeys.STRIPING).asBoolean());
                }
                if (locking.hasDefined(ModelKeys.ACQUIRE_TIMEOUT)) {
                    fluentLocking.lockAcquisitionTimeout(locking.get(ModelKeys.ACQUIRE_TIMEOUT).asLong());
                }
                if (locking.hasDefined(ModelKeys.CONCURRENCY_LEVEL)) {
                    fluentLocking.concurrencyLevel(locking.get(ModelKeys.CONCURRENCY_LEVEL).asInt());
                }
            }
            FluentConfiguration.TransactionConfig fluentTx = fluent.transaction();
            TransactionMode txMode = TransactionMode.NON_XA;
            LockingMode lockingMode = LockingMode.OPTIMISTIC;
            if (cache.hasDefined(ModelKeys.TRANSACTION)) {
                ModelNode transaction = cache.get(ModelKeys.TRANSACTION);
                if (transaction.hasDefined(ModelKeys.STOP_TIMEOUT)) {
                    fluentTx.cacheStopTimeout(transaction.get(ModelKeys.STOP_TIMEOUT).asInt());
                }
                if (transaction.hasDefined(ModelKeys.MODE)) {
                    txMode = TransactionMode.valueOf(transaction.get(ModelKeys.MODE).asString());
                }
                if (transaction.hasDefined(ModelKeys.LOCKING)) {
                    lockingMode = LockingMode.valueOf(transaction.get(ModelKeys.LOCKING).asString());
                }
                if (transaction.hasDefined(ModelKeys.EAGER_LOCKING)) {
                    EagerLocking eager = EagerLocking.valueOf(transaction.get(ModelKeys.EAGER_LOCKING).asString());
                    fluentTx.lockingMode(eager.isEnabled() ? LockingMode.PESSIMISTIC : LockingMode.OPTIMISTIC).eagerLockSingleNode(eager.isSingleOwner());
                }
            }
            fluentTx.transactionMode(txMode.getMode());
            fluentTx.lockingMode(lockingMode);
            FluentConfiguration.RecoveryConfig recovery = fluentTx.useSynchronization(!txMode.isXAEnabled()).recovery();
            if (txMode.isRecoveryEnabled()) {
                recovery.syncCommitPhase(true).syncRollbackPhase(true);
            } else {
                recovery.disable();
            }
            if (cache.hasDefined(ModelKeys.EVICTION)) {
                ModelNode eviction = cache.get(ModelKeys.EVICTION);
                FluentConfiguration.EvictionConfig fluentEviction = fluent.eviction();
                if (eviction.hasDefined(ModelKeys.STRATEGY)) {
                    fluentEviction.strategy(EvictionStrategy.valueOf(eviction.get(ModelKeys.STRATEGY).asString()));
                }
                if (eviction.hasDefined(ModelKeys.MAX_ENTRIES)) {
                    fluentEviction.maxEntries(eviction.get(ModelKeys.MAX_ENTRIES).asInt());
                }
            }
            if (cache.hasDefined(ModelKeys.EXPIRATION)) {
                ModelNode expiration = cache.get(ModelKeys.EXPIRATION);
                FluentConfiguration.ExpirationConfig fluentExpiration = fluent.expiration();
                if (expiration.hasDefined(ModelKeys.MAX_IDLE)) {
                    fluentExpiration.maxIdle(expiration.get(ModelKeys.MAX_IDLE).asLong());
                }
                if (expiration.hasDefined(ModelKeys.LIFESPAN)) {
                    fluentExpiration.lifespan(expiration.get(ModelKeys.LIFESPAN).asLong());
                }
                if (expiration.hasDefined(ModelKeys.INTERVAL)) {
                    fluentExpiration.wakeUpInterval(expiration.get(ModelKeys.INTERVAL).asLong());
                }
            }
            if (cache.hasDefined(ModelKeys.STATE_TRANSFER)) {
                ModelNode stateTransfer = cache.get(ModelKeys.STATE_TRANSFER);
                FluentConfiguration.StateRetrievalConfig fluentStateTransfer = fluent.stateRetrieval();
                if (stateTransfer.hasDefined(ModelKeys.ENABLED)) {
                    fluentStateTransfer.fetchInMemoryState(stateTransfer.get(ModelKeys.ENABLED).asBoolean());
                }
                if (stateTransfer.hasDefined(ModelKeys.TIMEOUT)) {
                    fluentStateTransfer.timeout(stateTransfer.get(ModelKeys.TIMEOUT).asLong());
                }
                if (stateTransfer.hasDefined(ModelKeys.FLUSH_TIMEOUT)) {
                    fluentStateTransfer.logFlushTimeout(stateTransfer.get(ModelKeys.FLUSH_TIMEOUT).asLong());
                }
            }
            if (cache.hasDefined(ModelKeys.REHASHING)) {
                ModelNode rehashing = cache.get(ModelKeys.REHASHING);
                FluentConfiguration.HashConfig fluentHash = fluent.hash();
                if (rehashing.hasDefined(ModelKeys.ENABLED)) {
                    fluentHash.rehashEnabled(rehashing.get(ModelKeys.ENABLED).asBoolean());
                }
                if (rehashing.hasDefined(ModelKeys.TIMEOUT)) {
                    fluentHash.rehashRpcTimeout(rehashing.get(ModelKeys.TIMEOUT).asLong());
                }
            }
            if (cache.hasDefined(ModelKeys.STORE)) {
                ModelNode store = cache.get(ModelKeys.STORE);
                FluentConfiguration.LoadersConfig fluentStores = fluent.loaders();
                fluentStores.shared(store.hasDefined(ModelKeys.SHARED) ? store.get(ModelKeys.SHARED).asBoolean() : false);
                fluentStores.preload(store.hasDefined(ModelKeys.PRELOAD) ? store.get(ModelKeys.PRELOAD).asBoolean() : false);
                fluentStores.passivation(store.hasDefined(ModelKeys.PASSIVATION) ? store.get(ModelKeys.PASSIVATION).asBoolean() : true);
                CacheStoreConfig storeConfig = buildCacheStore(name, builder, store);
                storeConfig.singletonStore().enabled(store.hasDefined(ModelKeys.SINGLETON) ? store.get(ModelKeys.SINGLETON).asBoolean() : false);
                storeConfig.fetchPersistentState(store.hasDefined(ModelKeys.FETCH_STATE) ? store.get(ModelKeys.FETCH_STATE).asBoolean() : true);
                storeConfig.purgeOnStartup(store.hasDefined(ModelKeys.PURGE) ? store.get(ModelKeys.PURGE).asBoolean() : true);
                if (store.hasDefined(ModelKeys.PROPERTY) && (storeConfig instanceof AbstractCacheStoreConfig)) {
                    Properties properties = new Properties();
                    for (Property property : store.get(ModelKeys.PROPERTY).asPropertyList()) {
                        properties.setProperty(property.getName(), property.getValue().asString());
                    }
                    ((AbstractCacheStoreConfig) storeConfig).setProperties(properties);
                }
                fluentStores.addCacheLoader(storeConfig);
            }
            configurations.put(cacheName, configuration);

            StartMode startMode = cache.hasDefined(ModelKeys.START) ? StartMode.valueOf(cache.get(ModelKeys.START).asString()) : StartMode.LAZY;
            ServiceBuilder<Cache<Object, Object>> cacheBuilder = new CacheService<Object, Object>(cacheName).build(target, serviceName).addDependency(bindInfo.getBinderServiceName()).setInitialMode(startMode.getMode());
            if (cacheName.equals(defaultCache)) {
                cacheBuilder.addAliases(CacheService.getServiceName(name, null));
            }
            if (configuration.isTransactionalCache()) {
                cacheBuilder.addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER);
                if (configuration.isUseSynchronizationForTransactions()) {
                    cacheBuilder.addDependency(TxnServices.JBOSS_TXN_SYNCHRONIZATION_REGISTRY);
                }
                if (configuration.isTransactionRecoveryEnabled()) {
                    cacheBuilder.addDependencies(TxnServices.JBOSS_TXN_ARJUNA_RECOVERY_MANAGER);
                }
            }
            if (startMode.getMode() == ServiceController.Mode.ACTIVE) {
                cacheBuilder.addListener(verificationHandler);
            }
            newControllers.add(cacheBuilder.install());
        }
        if (!configurations.containsKey(defaultCache)) {
            throw MESSAGES.invalidCacheStore(defaultCache, name);
        }

        if (requiresTransport) {
            Transport transportConfig = new Transport();
            String stack = null;
            if (operation.hasDefined(ModelKeys.TRANSPORT)) {
                ModelNode transport = operation.get(ModelKeys.TRANSPORT);
                if (transport.hasDefined(ModelKeys.STACK)) {
                    stack = transport.get(ModelKeys.STACK).asString();
                }
                addExecutorDependency(builder, transport, ModelKeys.EXECUTOR, transportConfig.getExecutorInjector());
                if (transport.hasDefined(ModelKeys.LOCK_TIMEOUT)) {
                    transportConfig.setLockTimeout(transport.get(ModelKeys.LOCK_TIMEOUT).asLong());
                }
                if (transport.hasDefined(ModelKeys.SITE)) {
                    transportConfig.setSite(transport.get(ModelKeys.SITE).asString());
                }
                if (transport.hasDefined(ModelKeys.RACK)) {
                    transportConfig.setRack(transport.get(ModelKeys.RACK).asString());
                }
                if (transport.hasDefined(ModelKeys.MACHINE)) {
                    transportConfig.setMachine(transport.get(ModelKeys.MACHINE).asString());
                }
            }
            builder.addDependency(ChannelService.getServiceName(name), Channel.class, transportConfig.getChannelInjector());
            config.setTransport(transportConfig);

            InjectedValue<ChannelFactory> channelFactory = new InjectedValue<ChannelFactory>();
            newControllers.add(target.addService(ChannelService.getServiceName(name), new ChannelService(name, channelFactory))
                    .addDependency(ChannelFactoryService.getServiceName(stack), ChannelFactory.class, channelFactory)
                    .setInitialMode(ServiceController.Mode.ON_DEMAND)
                    .install());
        }

        addExecutorDependency(builder, operation, ModelKeys.LISTENER_EXECUTOR, config.getListenerExecutorInjector());
        addScheduledExecutorDependency(builder, operation, ModelKeys.EVICTION_EXECUTOR, config.getEvictionExecutorInjector());
        addScheduledExecutorDependency(builder, operation, ModelKeys.REPLICATION_QUEUE_EXECUTOR, config.getReplicationQueueExecutorInjector());

        newControllers.add(builder.install());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.jboss.as.controller.descriptions.DescriptionProvider#getModelDescription(java.util.Locale)
     */
    @Override
    public ModelNode getModelDescription(Locale locale) {
        return LocalDescriptions.getCacheContainerAddDescription(locale);
    }

    private void addExecutorDependency(ServiceBuilder<CacheContainer> builder, ModelNode model, String key, Injector<Executor> injector) {
        if (model.hasDefined(key)) {
            builder.addDependency(ThreadsServices.executorName(model.get(key).asString()), Executor.class, injector);
        }
    }

    private void addScheduledExecutorDependency(ServiceBuilder<CacheContainer> builder, ModelNode model, String key, Injector<ScheduledExecutorService> injector) {
        if (model.hasDefined(key)) {
            builder.addDependency(ThreadsServices.executorName(model.get(key).asString()), ScheduledExecutorService.class, injector);
        }
    }

    private CacheStoreConfig buildCacheStore(final String name, ServiceBuilder<CacheContainer> builder, ModelNode store) {
        if (store.hasDefined(ModelKeys.CLASS)) {
            String className = store.get(ModelKeys.CLASS).asString();
            try {
                CacheStore cacheStore = Class.forName(className).asSubclass(CacheStore.class).newInstance();
                return cacheStore.getConfigurationClass().asSubclass(CacheStoreConfig.class).newInstance();
            } catch (Exception e) {
                throw MESSAGES.invalidCacheStore(e, className);
            }
        }
        // If no class, we assume it's a file cache store
        FileCacheStoreConfig storeConfig = new FileCacheStoreConfig();
        String relativeTo = store.hasDefined(ModelKeys.RELATIVE_TO) ? store.get(ModelKeys.RELATIVE_TO).asString() : ServerEnvironment.SERVER_DATA_DIR;
        builder.addDependency(AbstractPathService.pathNameOf(relativeTo), String.class, storeConfig.getRelativeToInjector());
        storeConfig.setPath(store.hasDefined(ModelKeys.PATH) ? store.get(ModelKeys.PATH).asString() : name);
        return storeConfig;
    }

    private static JndiName toJndiName(String value) {
        return value.startsWith("java:") ? JndiName.of(value) : JndiName.of("java:jboss").append(value.startsWith("/") ? value.substring(1) : value);
    }

    static class EmbeddedCacheManager implements EmbeddedCacheManagerConfiguration {
        private final InjectedValue<EmbeddedCacheManagerDefaults> defaults = new InjectedValue<EmbeddedCacheManagerDefaults>();
        private final InjectedValue<TransactionManager> transactionManager = new InjectedValue<TransactionManager>();
        private final InjectedValue<TransactionSynchronizationRegistry> transactionSynchronizationRegistry = new InjectedValue<TransactionSynchronizationRegistry>();
        private final InjectedValue<XAResourceRecoveryRegistry> recoveryRegistry = new InjectedValue<XAResourceRecoveryRegistry>();
        private final InjectedValue<MBeanServer> mbeanServer = new InjectedValue<MBeanServer>();
        private final InjectedValue<Executor> listenerExecutor = new InjectedValue<Executor>();
        private final InjectedValue<ScheduledExecutorService> evictionExecutor = new InjectedValue<ScheduledExecutorService>();
        private final InjectedValue<ScheduledExecutorService> replicationQueueExecutor = new InjectedValue<ScheduledExecutorService>();

        private final String name;
        private final String defaultCache;
        private final Map<String, Configuration> configurations = new HashMap<String, Configuration>();
        private Transport transport;

        EmbeddedCacheManager(String name, String defaultCache) {
            this.name = name;
            this.defaultCache = defaultCache;
        }

        Injector<EmbeddedCacheManagerDefaults> getDefaultsInjector() {
            return this.defaults;
        }

        Injector<TransactionManager> getTransactionManagerInjector() {
            return this.transactionManager;
        }

        Injector<TransactionSynchronizationRegistry> getTransactionSynchronizationRegistryInjector() {
            return this.transactionSynchronizationRegistry;
        }

        Injector<XAResourceRecoveryRegistry> getXAResourceRecoveryRegistryInjector() {
            return this.recoveryRegistry;
        }

        Injector<MBeanServer> getMBeanServerInjector() {
            return this.mbeanServer;
        }

        Injector<Executor> getListenerExecutorInjector() {
            return this.listenerExecutor;
        }

        Injector<ScheduledExecutorService> getEvictionExecutorInjector() {
            return this.evictionExecutor;
        }

        Injector<ScheduledExecutorService> getReplicationQueueExecutorInjector() {
            return this.replicationQueueExecutor;
        }

        void setTransport(Transport transport) {
            this.transport = transport;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getDefaultCache() {
            return this.defaultCache;
        }

        @Override
        public Map<String, Configuration> getConfigurations() {
            return this.configurations;
        }

        @Override
        public TransportConfiguration getTransportConfiguration() {
            return this.transport;
        }

        @Override
        public EmbeddedCacheManagerDefaults getDefaults() {
            return this.defaults.getValue();
        }

        @Override
        public TransactionManager getTransactionManager() {
            return this.transactionManager.getOptionalValue();
        }

        @Override
        public TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
            return this.transactionSynchronizationRegistry.getOptionalValue();
        }

        @Override
        public XAResourceRecoveryRegistry getXAResourceRecoveryRegistry() {
            return this.recoveryRegistry.getOptionalValue();
        }

        @Override
        public MBeanServer getMBeanServer() {
            return this.mbeanServer.getOptionalValue();
        }

        @Override
        public Executor getListenerExecutor() {
            return this.listenerExecutor.getOptionalValue();
        }

        @Override
        public ScheduledExecutorService getEvictionExecutor() {
            return this.evictionExecutor.getOptionalValue();
        }

        @Override
        public ScheduledExecutorService getReplicationQueueExecutor() {
            return this.replicationQueueExecutor.getOptionalValue();
        }
    }

    static class Transport implements TransportConfiguration {
        private final InjectedValue<Channel> channel = new InjectedValue<Channel>();
        private final InjectedValue<Executor> executor = new InjectedValue<Executor>();

        private Long lockTimeout;
        private String site;
        private String rack;
        private String machine;

        void setLockTimeout(long lockTimeout) {
            this.lockTimeout = lockTimeout;
        }

        void setSite(String site) {
            this.site = site;
        }

        void setRack(String rack) {
            this.rack = rack;
        }

        void setMachine(String machine) {
            this.machine = machine;
        }

        Injector<Channel> getChannelInjector() {
            return this.channel;
        }

        Injector<Executor> getExecutorInjector() {
            return this.executor;
        }

        @Override
        public Value<Channel> getChannel() {
            return this.channel;
        }

        @Override
        public Executor getExecutor() {
            return this.executor.getOptionalValue();
        }

        @Override
        public Long getLockTimeout() {
            return this.lockTimeout;
        }

        @Override
        public String getSite() {
            return this.site;
        }

        @Override
        public String getRack() {
            return this.rack;
        }

        @Override
        public String getMachine() {
            return this.machine;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11051.java