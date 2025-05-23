error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5474.java
text:
```scala
final S@@erviceBuilder<?> configBuilder = AsynchronousService.addService(target, CacheConfigurationService.getServiceName(containerName, cacheName), service)

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.infinispan.Cache;
import org.infinispan.commons.util.TypedProperties;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.cache.SingleFileStoreConfigurationBuilder;
import org.infinispan.configuration.cache.StoreConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.persistence.jdbc.configuration.AbstractJdbcStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.JdbcBinaryStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.JdbcMixedStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.JdbcStringBasedStoreConfigurationBuilder;
import org.infinispan.persistence.jdbc.configuration.TableManipulationConfigurationBuilder;
import org.infinispan.persistence.remote.configuration.RemoteStoreConfigurationBuilder;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.tm.BatchModeTransactionManager;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jboss.as.clustering.infinispan.CacheContainer;
import org.jboss.as.clustering.infinispan.InfinispanMessages;
import org.jboss.as.clustering.msc.AsynchronousService;
import org.jboss.as.clustering.naming.JndiNameFactory;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.naming.ManagedReferenceInjector;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.deployment.JndiName;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.Services;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;
import org.jboss.msc.value.Value;
import org.jboss.tm.XAResourceRecoveryRegistry;

/**
 * Base class for cache add handlers
 *
 * @author Richard Achmatowicz (c) 2011 Red Hat Inc.
 */
public abstract class CacheAdd extends AbstractAddStepHandler {

    private static final Logger log = Logger.getLogger(CacheAdd.class.getPackage().getName());
    private static final String DEFAULTS = "infinispan-defaults.xml";
    private static volatile Map<CacheMode, Configuration> defaults = null;

    public static synchronized Configuration getDefaultConfiguration(CacheMode cacheMode) {
        if (defaults == null) {
            ConfigurationBuilderHolder holder = load(DEFAULTS);
            Configuration defaultConfig = holder.getDefaultConfigurationBuilder().build();
            Map<CacheMode, Configuration> map = new EnumMap<CacheMode, Configuration>(CacheMode.class);
            map.put(defaultConfig.clustering().cacheMode(), defaultConfig);
            for (ConfigurationBuilder builder : holder.getNamedConfigurationBuilders().values()) {
                Configuration config = builder.build();
                map.put(config.clustering().cacheMode(), config);
            }
            for (CacheMode mode : CacheMode.values()) {
                if (!map.containsKey(mode)) {
                    map.put(mode, new ConfigurationBuilder().read(defaultConfig).clustering().cacheMode(mode).build());
                }
            }
            defaults = map;
        }
        return defaults.get(cacheMode);
    }

    private static ConfigurationBuilderHolder load(String resource) {
        URL url = find(resource, CacheAdd.class.getClassLoader());
        log.debugf("Loading Infinispan defaults from %s", url.toString());
        ParserRegistry parser = new ParserRegistry(ParserRegistry.class.getClassLoader());
        try (InputStream input = url.openStream()) {
            return parser.parse(input);
        } catch (IOException e) {
            throw InfinispanMessages.MESSAGES.failedToParse(e, url);
        }
    }

    private static URL find(String resource, ClassLoader... loaders) {
        for (ClassLoader loader : loaders) {
            if (loader != null) {
                URL url = loader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Failed to locate %s", resource));
    }

    final CacheMode mode;

    CacheAdd(CacheMode mode) {
        this.mode = mode;
    }

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {

        this.populate(operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        // Because we use child resources in a read-only manner to configure the cache, replace the local model with the full model
        ModelNode cacheModel = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

        // we also need the containerModel
        PathAddress containerAddress = getCacheContainerAddressFromOperation(operation);
        ModelNode containerModel = context.readResourceFromRoot(containerAddress).getModel();

        // install the services from a reusable method
        newControllers.addAll(this.installRuntimeServices(context, operation, containerModel, cacheModel, verificationHandler));
    }

    Collection<ServiceController<?>> installRuntimeServices(OperationContext context, ModelNode operation, ModelNode containerModel, ModelNode cacheModel, ServiceVerificationHandler verificationHandler) throws OperationFailedException {
        // get all required addresses, names and service names
        PathAddress cacheAddress = getCacheAddressFromOperation(operation);
        PathAddress containerAddress = getCacheContainerAddressFromOperation(operation);
        String cacheName = cacheAddress.getLastElement().getValue();
        String containerName = containerAddress.getLastElement().getValue();

        // get model attributes
        ModelNode resolvedValue = null;
        final String jndiName = ((resolvedValue = CacheResourceDefinition.JNDI_NAME.resolveModelAttribute(context, cacheModel)).isDefined()) ? resolvedValue.asString() : null;
        final ServiceController.Mode initialMode = StartMode.valueOf(CacheResourceDefinition.START.resolveModelAttribute(context, cacheModel).asString()).getMode();

        final ModuleIdentifier moduleId = (resolvedValue = CacheResourceDefinition.CACHE_MODULE.resolveModelAttribute(context, cacheModel)).isDefined() ? ModuleIdentifier.fromString(resolvedValue.asString()) : null;

        // create a list for dependencies which may need to be added during processing
        List<Dependency<?>> dependencies = new LinkedList<Dependency<?>>();
        // Infinispan Configuration to hold the operation data
        ConfigurationBuilder builder = new ConfigurationBuilder().read(getDefaultConfiguration(this.mode));

        // process cache configuration ModelNode describing overrides to defaults
        processModelNode(context, containerName, cacheModel, builder, dependencies);

        // get container Model to pick up the value of the default cache of the container
        // AS7-3488 make default-cache no required attribute
        String defaultCacheName = CacheContainerResourceDefinition.DEFAULT_CACHE.resolveModelAttribute(context, containerModel).asString();
        boolean defaultCache = cacheName.equals(defaultCacheName);

        ServiceTarget target = context.getServiceTarget();
        Configuration config = builder.build();

        Collection<ServiceController<?>> controllers = new ArrayList<ServiceController<?>>(3);

        // install the cache configuration service (configures a cache)
        controllers.add(this.installCacheConfigurationService(target, containerName, cacheName, defaultCache, moduleId, builder, config, dependencies, verificationHandler));
        log.debugf("Cache configuration service for %s installed for container %s", cacheName, containerName);

        // now install the corresponding cache service (starts a configured cache)
        controllers.add(this.installCacheService(target, containerName, cacheName, defaultCache, initialMode, config, verificationHandler));

        // install a name service entry for the cache
        controllers.add(this.installJndiService(target, containerName, cacheName, defaultCache, jndiName, verificationHandler));
        log.debugf("Cache service for cache %s installed for container %s", cacheName, containerName);

        if (config.clustering().cacheMode().isClustered()) {
            for (CacheServiceProvider provider: ServiceLoader.load(CacheServiceProvider.class, CacheServiceProvider.class.getClassLoader())) {
                log.debugf("Installing %s for cache %s/%s", provider.getClass().getSimpleName(), containerName, cacheName);
                controllers.addAll(provider.install(target, containerName, cacheName, defaultCache, moduleId));
            }
        }

        return controllers;
    }

    void removeRuntimeServices(OperationContext context, ModelNode operation, ModelNode containerModel, ModelNode cacheModel) throws OperationFailedException {
        // get container and cache addresses
        final PathAddress cacheAddress = getCacheAddressFromOperation(operation) ;
        final PathAddress containerAddress = getCacheContainerAddressFromOperation(operation) ;

        // get container and cache names
        final String cacheName = cacheAddress.getLastElement().getValue() ;
        final String containerName = containerAddress.getLastElement().getValue() ;

        // remove all services started by CacheAdd, in reverse order
        // remove the binder service
        ModelNode resolvedValue = null;
        final String jndiName = (resolvedValue = CacheResourceDefinition.JNDI_NAME.resolveModelAttribute(context, cacheModel)).isDefined() ? resolvedValue.asString() : null;

        String defaultCacheName = CacheContainerResourceDefinition.DEFAULT_CACHE.resolveModelAttribute(context, containerModel).asString();
        boolean defaultCache = cacheName.equals(defaultCacheName);

        ContextNames.BindInfo binding = createCacheBinding((jndiName != null) ? JndiNameFactory.parse(jndiName) : createJndiName(containerName, cacheName));
        context.removeService(binding.getBinderServiceName());
        // remove the CacheService instance
        context.removeService(CacheService.getServiceName(containerName, cacheName));
        // remove the cache configuration service
        context.removeService(CacheConfigurationService.getServiceName(containerName, cacheName));

        for (CacheServiceProvider provider: ServiceLoader.load(CacheServiceProvider.class, CacheServiceProvider.class.getClassLoader())) {
            for (ServiceName name: provider.getServiceNames(containerName, cacheName, defaultCache)) {
                context.removeService(name);
            }
        }

        log.debugf("cache %s removed for container %s", cacheName, containerName);
    }

    protected PathAddress getCacheAddressFromOperation(ModelNode operation) {
        return PathAddress.pathAddress(operation.get(OP_ADDR)) ;
    }

    protected PathAddress getCacheContainerAddressFromOperation(ModelNode operation) {
        final PathAddress cacheAddress = getCacheAddressFromOperation(operation) ;
        final PathAddress containerAddress = cacheAddress.subAddress(0, cacheAddress.size()-1) ;
        return containerAddress ;
    }

    ServiceController<?> installCacheConfigurationService(ServiceTarget target, String containerName, String cacheName, boolean defaultCache, ModuleIdentifier moduleId,
            ConfigurationBuilder builder, Configuration config, List<Dependency<?>> dependencies, ServiceVerificationHandler verificationHandler) {

        final InjectedValue<EmbeddedCacheManager> container = new InjectedValue<EmbeddedCacheManager>();
        final CacheConfigurationDependencies cacheConfigurationDependencies = new CacheConfigurationDependencies(container);
        final Service<Configuration> service = new CacheConfigurationService(cacheName, builder, moduleId, cacheConfigurationDependencies);
        final ServiceBuilder<?> configBuilder = target.addService(CacheConfigurationService.getServiceName(containerName, cacheName), service)
                .addDependency(EmbeddedCacheManagerService.getServiceName(containerName), EmbeddedCacheManager.class, container)
                .addDependency(Services.JBOSS_SERVICE_MODULE_LOADER, ModuleLoader.class, cacheConfigurationDependencies.getModuleLoaderInjector())
                .setInitialMode(ServiceController.Mode.PASSIVE)
        ;
        if (config.invocationBatching().enabled()) {
            cacheConfigurationDependencies.getTransactionManagerInjector().inject(BatchModeTransactionManager.getInstance());
        } else if (config.transaction().transactionMode() == org.infinispan.transaction.TransactionMode.TRANSACTIONAL) {
            configBuilder.addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, TransactionManager.class, cacheConfigurationDependencies.getTransactionManagerInjector());
            if (config.transaction().useSynchronization()) {
                configBuilder.addDependency(TxnServices.JBOSS_TXN_SYNCHRONIZATION_REGISTRY, TransactionSynchronizationRegistry.class, cacheConfigurationDependencies.getTransactionSynchronizationRegistryInjector());
            }
        }

        // add in any additional dependencies resulting from ModelNode parsing
        for (Dependency<?> dependency : dependencies) {
            addDependency(configBuilder, dependency);
        }
        // add an alias for the default cache
        if (defaultCache) {
            configBuilder.addAliases(CacheConfigurationService.getServiceName(containerName, null));
        }
        return configBuilder.install();
    }

    ServiceController<?> installCacheService(ServiceTarget target, String containerName, String cacheName, boolean defaultCache, ServiceController.Mode initialMode,
            Configuration config, ServiceVerificationHandler verificationHandler) {

        final InjectedValue<EmbeddedCacheManager> container = new InjectedValue<EmbeddedCacheManager>();
        final CacheDependencies cacheDependencies = new CacheDependencies(container);
        final Service<Cache<Object, Object>> service = new CacheService<Object, Object>(cacheName, cacheDependencies);
        final ServiceBuilder<?> builder = AsynchronousService.addService(target, CacheService.getServiceName(containerName, cacheName), service)
                .addDependency(GlobalComponentRegistryService.getServiceName(containerName))
                .addDependency(CacheConfigurationService.getServiceName(containerName, cacheName))
                .addDependency(EmbeddedCacheManagerService.getServiceName(containerName), EmbeddedCacheManager.class, container)
                .setInitialMode(initialMode)
        ;
        if (config.transaction().recovery().enabled()) {
            builder.addDependency(TxnServices.JBOSS_TXN_ARJUNA_RECOVERY_MANAGER, XAResourceRecoveryRegistry.class, cacheDependencies.getRecoveryRegistryInjector());
        }

        // add an alias for the default cache
        if (defaultCache) {
            builder.addAliases(CacheService.getServiceName(containerName, null));
        }

        if (initialMode == ServiceController.Mode.ACTIVE) {
            builder.addListener(verificationHandler);
        }

        return builder.install();
    }

    @SuppressWarnings("rawtypes")
    ServiceController<?> installJndiService(ServiceTarget target, String containerName, String cacheName, boolean defaultCache, String jndiName, ServiceVerificationHandler verificationHandler) {

        final ServiceName cacheServiceName = CacheService.getServiceName(containerName, cacheName);
        final ContextNames.BindInfo binding = createCacheBinding((jndiName != null) ? JndiNameFactory.parse(jndiName) : createJndiName(containerName, cacheName));
        final BinderService binder = new BinderService(binding.getBindName());
        ServiceBuilder<?> builder = target.addService(binding.getBinderServiceName(), binder)
                .addAliases(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(binding.getBindName()))
                .addDependency(cacheServiceName, Cache.class, new ManagedReferenceInjector<Cache>(binder.getManagedObjectInjector()))
                .addDependency(binding.getParentContextServiceName(), ServiceBasedNamingStore.class, binder.getNamingStoreInjector())
                .setInitialMode(ServiceController.Mode.PASSIVE)
        ;
        if (defaultCache) {
            ContextNames.BindInfo defaultBinding = createCacheBinding(createJndiName(containerName, CacheContainer.DEFAULT_CACHE_ALIAS));
            builder.addAliases(defaultBinding.getBinderServiceName(), ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(defaultBinding.getBindName()));
        }
        return builder.install();
    }

    private static JndiName createJndiName(String container, String cache) {
        return JndiNameFactory.createJndiName(JndiNameFactory.DEFAULT_JNDI_NAMESPACE, InfinispanExtension.SUBSYSTEM_NAME, "cache", container, cache);
    }

    private static ContextNames.BindInfo createCacheBinding(JndiName name) {
        return ContextNames.bindInfoFor(name.getAbsoluteName());
    }

    private static <T> void addDependency(ServiceBuilder<?> builder, Dependency<T> dependency) {
        final ServiceName name = dependency.getName();
        final Injector<T> injector = dependency.getInjector();
        if (injector != null) {
            builder.addDependency(name, dependency.getType(), injector);
        } else {
            builder.addDependency(name);
        }
    }

    /**
     * Transfer elements common to both operations and models
     *
     * @param fromModel
     * @param toModel
     */
    void populate(ModelNode fromModel, ModelNode toModel) throws OperationFailedException {

        CacheResourceDefinition.START.validateAndSet(fromModel, toModel);
        CacheResourceDefinition.BATCHING.validateAndSet(fromModel, toModel);
        CacheResourceDefinition.INDEXING.validateAndSet(fromModel, toModel);
        CacheResourceDefinition.JNDI_NAME.validateAndSet(fromModel, toModel);
        CacheResourceDefinition.CACHE_MODULE.validateAndSet(fromModel, toModel);
        CacheResourceDefinition.INDEXING_PROPERTIES.validateAndSet(fromModel, toModel);
    }

    /**
     * Create a Configuration object initialized from the operation ModelNode
     *
     * @param containerName the name of the cache container
     * @param cache         ModelNode representing cache configuration
     * @param builder       ConfigurationBuilder object to add data to
     * @return initialised Configuration object
     */
    void processModelNode(OperationContext context, String containerName, ModelNode cache, ConfigurationBuilder builder, List<Dependency<?>> dependencies) throws OperationFailedException {

        final Indexing indexing = Indexing.valueOf(CacheResourceDefinition.INDEXING.resolveModelAttribute(context, cache).asString());
        final boolean batching = CacheResourceDefinition.BATCHING.resolveModelAttribute(context, cache).asBoolean();

        // set the cache mode (may be modified when setting up clustering attributes)
        builder.clustering().cacheMode(this.mode);
        final ModelNode indexingPropertiesModel = CacheResourceDefinition.INDEXING_PROPERTIES.resolveModelAttribute(context, cache);
        Properties indexingProperties = new Properties();
        if (indexing.isEnabled() && indexingPropertiesModel.isDefined()) {
            for (Property p : indexingPropertiesModel.asPropertyList()) {
                String value = p.getValue().asString();
                indexingProperties.put(p.getName(), value);
            }
        }
        builder.indexing()
                .enabled(indexing.isEnabled())
                .indexLocalOnly(indexing.isLocalOnly())
                .withProperties(indexingProperties)
        ;

        // locking is a child resource
        if (cache.hasDefined(ModelKeys.LOCKING) && cache.get(ModelKeys.LOCKING, ModelKeys.LOCKING_NAME).isDefined()) {
            ModelNode locking = cache.get(ModelKeys.LOCKING, ModelKeys.LOCKING_NAME);

            final IsolationLevel isolationLevel = IsolationLevel.valueOf(LockingResource.ISOLATION.resolveModelAttribute(context, locking).asString());
            final boolean striping = LockingResource.STRIPING.resolveModelAttribute(context, locking).asBoolean();
            final long acquireTimeout = LockingResource.ACQUIRE_TIMEOUT.resolveModelAttribute(context, locking).asLong();
            final int concurrencyLevel = LockingResource.CONCURRENCY_LEVEL.resolveModelAttribute(context, locking).asInt();

            builder.locking()
                    .isolationLevel(isolationLevel)
                    .useLockStriping(striping)
                    .lockAcquisitionTimeout(acquireTimeout)
                    .concurrencyLevel(concurrencyLevel)
            ;
        }

        // locking is a child resource
        if (cache.hasDefined(ModelKeys.TRANSACTION) && cache.get(ModelKeys.TRANSACTION, ModelKeys.TRANSACTION_NAME).isDefined()) {
            ModelNode transaction = cache.get(ModelKeys.TRANSACTION, ModelKeys.TRANSACTION_NAME);

            long stopTimeout = TransactionResourceDefinition.STOP_TIMEOUT.resolveModelAttribute(context, transaction).asLong();
            TransactionMode txMode = TransactionMode.valueOf(TransactionResourceDefinition.MODE.resolveModelAttribute(context, transaction).asString());
            LockingMode lockingMode = LockingMode.valueOf(TransactionResourceDefinition.LOCKING.resolveModelAttribute(context, transaction).asString());

            builder.transaction()
                    .cacheStopTimeout(stopTimeout)
                    .transactionMode(txMode.getMode())
                    .lockingMode(lockingMode)
                    .useSynchronization(!txMode.isXAEnabled())
                    .recovery().enabled(txMode.isRecoveryEnabled())
            ;
        }

        if (batching) {
            builder.transaction().transactionMode(org.infinispan.transaction.TransactionMode.TRANSACTIONAL).invocationBatching().enable();
        } else {
            builder.transaction().invocationBatching().disable();
        }

        // eviction is a child resource
        if (cache.hasDefined(ModelKeys.EVICTION) && cache.get(ModelKeys.EVICTION, ModelKeys.EVICTION_NAME).isDefined()) {
            ModelNode eviction = cache.get(ModelKeys.EVICTION, ModelKeys.EVICTION_NAME);

            final EvictionStrategy strategy = EvictionStrategy.valueOf(EvictionResourceDefinition.EVICTION_STRATEGY.resolveModelAttribute(context, eviction).asString());
            builder.eviction().strategy(strategy);

            if (strategy.isEnabled()) {
                final int maxEntries = EvictionResourceDefinition.MAX_ENTRIES.resolveModelAttribute(context, eviction).asInt();
                builder.eviction().maxEntries(maxEntries);
            }
        }
        // expiration is a child resource
        if (cache.hasDefined(ModelKeys.EXPIRATION) && cache.get(ModelKeys.EXPIRATION, ModelKeys.EXPIRATION_NAME).isDefined()) {

            ModelNode expiration = cache.get(ModelKeys.EXPIRATION, ModelKeys.EXPIRATION_NAME);

            final long maxIdle = ExpirationResourceDefinition.MAX_IDLE.resolveModelAttribute(context, expiration).asLong();
            final long lifespan = ExpirationResourceDefinition.LIFESPAN.resolveModelAttribute(context, expiration).asLong();
            final long interval = ExpirationResourceDefinition.INTERVAL.resolveModelAttribute(context, expiration).asLong();

            builder.expiration()
                    .maxIdle(maxIdle)
                    .lifespan(lifespan)
                    .wakeUpInterval(interval)
            ;
            // Only enable the reaper thread if we need it
            if ((maxIdle > 0) || (lifespan > 0)) {
                builder.expiration().enableReaper();
            } else {
                builder.expiration().disableReaper();
            }
        }

        // stores are a child resource
        String storeKey = findStoreKey(cache);
        if (storeKey != null) {
            ModelNode store = getStoreModelNode(cache);

            final boolean shared = BaseStoreResourceDefinition.SHARED.resolveModelAttribute(context, store).asBoolean();
            final boolean preload = BaseStoreResourceDefinition.PRELOAD.resolveModelAttribute(context, store).asBoolean();
            final boolean passivation = BaseStoreResourceDefinition.PASSIVATION.resolveModelAttribute(context, store).asBoolean();
            final boolean fetchState = BaseStoreResourceDefinition.FETCH_STATE.resolveModelAttribute(context, store).asBoolean();
            final boolean purge = BaseStoreResourceDefinition.PURGE.resolveModelAttribute(context, store).asBoolean();
            final boolean singleton = BaseStoreResourceDefinition.SINGLETON.resolveModelAttribute(context, store).asBoolean();
            // TODO Fix me
            final boolean async = store.hasDefined(ModelKeys.WRITE_BEHIND) && store.get(ModelKeys.WRITE_BEHIND, ModelKeys.WRITE_BEHIND_NAME).isDefined();

            PersistenceConfigurationBuilder persistenceBuilder = builder.persistence()
                    .passivation(passivation)
            ;
            StoreConfigurationBuilder<?, ?> storeBuilder = this.buildCacheStore(context, persistenceBuilder, containerName, store, storeKey, dependencies)
                    .fetchPersistentState(fetchState)
                    .preload(preload)
                    .shared(shared)
                    .purgeOnStartup(purge)
            ;
            storeBuilder.singleton().enabled(singleton);

            if (async) {
                ModelNode writeBehind = store.get(ModelKeys.WRITE_BEHIND, ModelKeys.WRITE_BEHIND_NAME);
                storeBuilder.async().enable()
                        .flushLockTimeout(StoreWriteBehindResourceDefinition.FLUSH_LOCK_TIMEOUT.resolveModelAttribute(context, writeBehind).asLong())
                        .modificationQueueSize(StoreWriteBehindResourceDefinition.MODIFICATION_QUEUE_SIZE.resolveModelAttribute(context, writeBehind).asInt())
                        .shutdownTimeout(StoreWriteBehindResourceDefinition.SHUTDOWN_TIMEOUT.resolveModelAttribute(context, writeBehind).asLong())
                        .threadPoolSize(StoreWriteBehindResourceDefinition.THREAD_POOL_SIZE.resolveModelAttribute(context, writeBehind).asInt())
                ;
            }

            final Properties properties = new TypedProperties();
            if (store.hasDefined(ModelKeys.PROPERTY)) {
                for (Property property : store.get(ModelKeys.PROPERTY).asPropertyList()) {
                    // format of properties
                    // "property" => {
                    //   "property-name" => {"value => "property-value"}
                    // }
                    String propertyName = property.getName();
                    // get the value from ModelNode {"value" => "property-value"}
                    ModelNode propertyValue = null ;
                    propertyValue = StorePropertyResourceDefinition.VALUE.resolveModelAttribute(context,property.getValue());
                    properties.setProperty(propertyName, propertyValue.asString());
                }
            }
            storeBuilder.withProperties(properties);
        }
    }

    private static String findStoreKey(ModelNode cache) {
        if (cache.hasDefined(ModelKeys.STORE)) {
            return ModelKeys.STORE;
        } else if (cache.hasDefined(ModelKeys.FILE_STORE)) {
            return ModelKeys.FILE_STORE;
        } else if (cache.hasDefined(ModelKeys.STRING_KEYED_JDBC_STORE)) {
            return ModelKeys.STRING_KEYED_JDBC_STORE;
        } else if (cache.hasDefined(ModelKeys.BINARY_KEYED_JDBC_STORE)) {
            return ModelKeys.BINARY_KEYED_JDBC_STORE;
        } else if (cache.hasDefined(ModelKeys.MIXED_KEYED_JDBC_STORE)) {
            return ModelKeys.MIXED_KEYED_JDBC_STORE;
        } else if (cache.hasDefined(ModelKeys.REMOTE_STORE)) {
            return ModelKeys.REMOTE_STORE;
        }
        return null;
    }

    private static ModelNode getStoreModelNode(ModelNode cache) {
        if (cache.hasDefined(ModelKeys.STORE)) {
            return cache.get(ModelKeys.STORE, ModelKeys.STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.FILE_STORE)) {
            return cache.get(ModelKeys.FILE_STORE, ModelKeys.FILE_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.STRING_KEYED_JDBC_STORE)) {
            return cache.get(ModelKeys.STRING_KEYED_JDBC_STORE, ModelKeys.STRING_KEYED_JDBC_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.BINARY_KEYED_JDBC_STORE)) {
            return cache.get(ModelKeys.BINARY_KEYED_JDBC_STORE, ModelKeys.BINARY_KEYED_JDBC_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.MIXED_KEYED_JDBC_STORE)) {
            return cache.get(ModelKeys.MIXED_KEYED_JDBC_STORE, ModelKeys.MIXED_KEYED_JDBC_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.REMOTE_STORE)) {
            return cache.get(ModelKeys.REMOTE_STORE, ModelKeys.REMOTE_STORE_NAME);
        }
        return null;
    }


    private StoreConfigurationBuilder<?, ?> buildCacheStore(OperationContext context, PersistenceConfigurationBuilder persistenceBuilder, String containerName, ModelNode store, String storeKey, List<Dependency<?>> dependencies) throws OperationFailedException {

        ModelNode resolvedValue = null;
        if (storeKey.equals(ModelKeys.FILE_STORE)) {
            final SingleFileStoreConfigurationBuilder builder = persistenceBuilder.addSingleFileStore();

            final String path = ((resolvedValue = FileStoreResourceDefinition.PATH.resolveModelAttribute(context, store)).isDefined()) ? resolvedValue.asString() : InfinispanExtension.SUBSYSTEM_NAME + File.separatorChar + containerName;
            final String relativeTo = ((resolvedValue = FileStoreResourceDefinition.RELATIVE_TO.resolveModelAttribute(context, store)).isDefined()) ? resolvedValue.asString() : ServerEnvironment.SERVER_DATA_DIR;
            Injector<PathManager> injector = new SimpleInjector<PathManager>() {
                private volatile PathManager.Callback.Handle callbackHandle;
                @Override
                public void inject(PathManager value) {
                    this.callbackHandle = value.registerCallback(relativeTo, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED);
                    builder.location(value.resolveRelativePathEntry(path, relativeTo));
                }

                @Override
                public void uninject() {
                    super.uninject();
                    if (this.callbackHandle != null) {
                        this.callbackHandle.remove();
                    }
                }
            };
            dependencies.add(new Dependency<PathManager>(PathManagerService.SERVICE_NAME, PathManager.class, injector));
            return builder;
        } else if (storeKey.equals(ModelKeys.STRING_KEYED_JDBC_STORE) || storeKey.equals(ModelKeys.BINARY_KEYED_JDBC_STORE) || storeKey.equals(ModelKeys.MIXED_KEYED_JDBC_STORE)) {
            AbstractJdbcStoreConfigurationBuilder<?, ?> builder = buildJdbcStore(persistenceBuilder, context, store);

            String datasource = BaseJDBCStoreResourceDefinition.DATA_SOURCE.resolveModelAttribute(context, store).asString();

            dependencies.add(new Dependency<Object>(ServiceName.JBOSS.append("data-source", datasource)));
            builder.dataSource().jndiUrl(datasource);
            return builder;
        } else if (storeKey.equals(ModelKeys.REMOTE_STORE)) {
            final RemoteStoreConfigurationBuilder builder = persistenceBuilder.addStore(RemoteStoreConfigurationBuilder.class);
            for (ModelNode server : store.require(ModelKeys.REMOTE_SERVERS).asList()) {
                String outboundSocketBinding = server.get(ModelKeys.OUTBOUND_SOCKET_BINDING).asString();
                Injector<OutboundSocketBinding> injector = new SimpleInjector<OutboundSocketBinding>() {
                    @Override
                    public void inject(OutboundSocketBinding value) {
                        try {
                            builder.addServer().host(value.getDestinationAddress().getHostAddress()).port(value.getDestinationPort());
                        } catch (UnknownHostException e) {
                            throw InfinispanMessages.MESSAGES.failedToInjectSocketBinding(e, value);
                        }
                    }
                };
                dependencies.add(new Dependency<OutboundSocketBinding>(OutboundSocketBinding.OUTBOUND_SOCKET_BINDING_BASE_SERVICE_NAME.append(outboundSocketBinding), OutboundSocketBinding.class, injector));
            }
            if (store.hasDefined(ModelKeys.CACHE)) {
                builder.remoteCacheName(store.get(ModelKeys.CACHE).asString());
            }
            if (store.hasDefined(ModelKeys.SOCKET_TIMEOUT)) {
                builder.socketTimeout(store.require(ModelKeys.SOCKET_TIMEOUT).asLong());
            }
            if (store.hasDefined(ModelKeys.TCP_NO_DELAY)) {
                builder.tcpNoDelay(store.require(ModelKeys.TCP_NO_DELAY).asBoolean());
            }
            return builder;
        } else {
            String className = store.require(ModelKeys.CLASS).asString();
            try {
                return persistenceBuilder.addStore(StoreConfigurationBuilder.class.getClassLoader().loadClass(className).asSubclass(StoreConfigurationBuilder.class));
            } catch (Exception e) {
                throw InfinispanMessages.MESSAGES.invalidCacheStore(e, className);
            }
        }
    }

    private static AbstractJdbcStoreConfigurationBuilder<?, ?> buildJdbcStore(PersistenceConfigurationBuilder persistenceBuilder, OperationContext context, ModelNode store) throws OperationFailedException {
        boolean useStringKeyedTable = store.hasDefined(ModelKeys.STRING_KEYED_TABLE);
        boolean useBinaryKeyedTable = store.hasDefined(ModelKeys.BINARY_KEYED_TABLE);
        if (useStringKeyedTable && !useBinaryKeyedTable) {
            JdbcStringBasedStoreConfigurationBuilder builder = persistenceBuilder.addStore(JdbcStringBasedStoreConfigurationBuilder.class);
            buildStringKeyedTable(builder.table(), context, store.get(ModelKeys.STRING_KEYED_TABLE));
            return builder;
        } else if (useBinaryKeyedTable && !useStringKeyedTable) {
            JdbcBinaryStoreConfigurationBuilder builder = persistenceBuilder.addStore(JdbcBinaryStoreConfigurationBuilder.class);
            buildBinaryKeyedTable(builder.table(), context, store.get(ModelKeys.BINARY_KEYED_TABLE));
            return builder;
        }
        // Else, use mixed mode
        JdbcMixedStoreConfigurationBuilder builder = persistenceBuilder.addStore(JdbcMixedStoreConfigurationBuilder.class);
        buildStringKeyedTable(builder.stringTable(), context, store.get(ModelKeys.STRING_KEYED_TABLE));
        buildBinaryKeyedTable(builder.binaryTable(), context, store.get(ModelKeys.BINARY_KEYED_TABLE));
        return builder;
    }

    private static void buildBinaryKeyedTable(TableManipulationConfigurationBuilder<?, ?> builder, OperationContext context, ModelNode table) throws OperationFailedException {
        buildTable(builder, context, table, "ispn_bucket");
    }

    private static void buildStringKeyedTable(TableManipulationConfigurationBuilder<?, ?> builder, OperationContext context, ModelNode table) throws OperationFailedException {
        buildTable(builder, context, table, "ispn_entry");
    }

    private static void buildTable(TableManipulationConfigurationBuilder<?, ?> builder, OperationContext context, ModelNode table, String defaultTableNamePrefix) throws OperationFailedException {
        ModelNode tableNamePrefix = BaseJDBCStoreResourceDefinition.PREFIX.resolveModelAttribute(context, table);
        builder.batchSize(BaseJDBCStoreResourceDefinition.BATCH_SIZE.resolveModelAttribute(context, table).asInt())
                .fetchSize(BaseJDBCStoreResourceDefinition.FETCH_SIZE.resolveModelAttribute(context, table).asInt())
                .tableNamePrefix(tableNamePrefix.isDefined() ? tableNamePrefix.asString() : defaultTableNamePrefix)
                .idColumnName(getColumnProperty(context, table, ModelKeys.ID_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_NAME, "id"))
                .idColumnType(getColumnProperty(context, table, ModelKeys.ID_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_TYPE, "VARCHAR"))
                .dataColumnName(getColumnProperty(context, table, ModelKeys.DATA_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_NAME, "datum"))
                .dataColumnType(getColumnProperty(context, table, ModelKeys.DATA_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_TYPE, "BINARY"))
                .timestampColumnName(getColumnProperty(context, table, ModelKeys.TIMESTAMP_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_NAME, "version"))
                .timestampColumnType(getColumnProperty(context, table, ModelKeys.TIMESTAMP_COLUMN, BaseJDBCStoreResourceDefinition.COLUMN_TYPE, "BIGINT"));
    }

    private static String getColumnProperty(OperationContext context, ModelNode table, String columnKey, AttributeDefinition columnAttribute, String defaultValue) throws OperationFailedException
    {
        if (!table.isDefined() || !table.hasDefined(columnKey)) return defaultValue;
        ModelNode column = table.get(columnKey);
        ModelNode resolvedValue = null ;
        return ((resolvedValue = columnAttribute.resolveModelAttribute(context, column)).isDefined()) ? resolvedValue.asString() : defaultValue;
    }

    /*
     * Allows us to store dependency requirements for later processing.
     */
    protected class Dependency<I> {
        private final ServiceName name;
        private final Class<I> type;
        private final Injector<I> target;

        Dependency(ServiceName name) {
            this(name, null, null);
        }

        Dependency(ServiceName name, Class<I> type, Injector<I> target) {
            this.name = name;
            this.type = type;
            this.target = target;
        }

        ServiceName getName() {
            return name;
        }

        public Class<I> getType() {
            return type;
        }

        public Injector<I> getInjector() {
            return target;
        }
    }

    abstract class SimpleInjector<I> implements Injector<I> {
        @Override
        public void uninject() {
            // Do nothing
        }
    }

    private static class CacheDependencies implements CacheService.Dependencies {

        private final Value<EmbeddedCacheManager> container;
        private final InjectedValue<XAResourceRecoveryRegistry> recoveryRegistry = new InjectedValue<XAResourceRecoveryRegistry>();

        CacheDependencies(Value<EmbeddedCacheManager> container) {
            this.container = container;
        }

        Injector<XAResourceRecoveryRegistry> getRecoveryRegistryInjector() {
            return this.recoveryRegistry;
        }

        @Override
        public EmbeddedCacheManager getCacheContainer() {
            return this.container.getValue();
        }

        @Override
        public XAResourceRecoveryRegistry getRecoveryRegistry() {
            return this.recoveryRegistry.getOptionalValue();
        }
    }

    private static class CacheConfigurationDependencies implements CacheConfigurationService.Dependencies {

        private final Value<EmbeddedCacheManager> container;
        private final InjectedValue<TransactionManager> tm = new InjectedValue<TransactionManager>();
        private final InjectedValue<TransactionSynchronizationRegistry> tsr = new InjectedValue<TransactionSynchronizationRegistry>();
        private final InjectedValue<ModuleLoader> moduleLoader = new InjectedValue<ModuleLoader>();

        CacheConfigurationDependencies(Value<EmbeddedCacheManager> container) {
            this.container = container;
        }

        Injector<TransactionManager> getTransactionManagerInjector() {
            return this.tm;
        }

        Injector<TransactionSynchronizationRegistry> getTransactionSynchronizationRegistryInjector() {
            return this.tsr;
        }

        Injector<ModuleLoader> getModuleLoaderInjector() {
            return this.moduleLoader;
        }

        @Override
        public EmbeddedCacheManager getCacheContainer() {
            return this.container.getValue();
        }

        @Override
        public TransactionManager getTransactionManager() {
            return this.tm.getOptionalValue();
        }

        @Override
        public TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
            return this.tsr.getOptionalValue();
        }

        @Override
        public ModuleLoader getModuleLoader() {
            return this.moduleLoader.getValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5474.java