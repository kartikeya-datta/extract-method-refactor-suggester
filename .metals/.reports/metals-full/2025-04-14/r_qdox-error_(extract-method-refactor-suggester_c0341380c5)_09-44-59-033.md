error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7003.java
text:
```scala
d@@ependencies.add(new Dependency<Object>(ServiceName.JBOSS.append("data-source", datasource)));

package org.jboss.as.clustering.infinispan.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.LoaderConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.Parser;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.loaders.CacheLoader;
import org.infinispan.loaders.CacheStore;
import org.infinispan.loaders.file.FileCacheStore;
import org.infinispan.loaders.jdbc.TableManipulation;
import org.infinispan.loaders.jdbc.binary.JdbcBinaryCacheStore;
import org.infinispan.loaders.jdbc.connectionfactory.ManagedConnectionFactory;
import org.infinispan.loaders.jdbc.mixed.JdbcMixedCacheStore;
import org.infinispan.loaders.jdbc.stringbased.JdbcStringBasedCacheStore;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.tm.BatchModeTransactionManager;
import org.infinispan.util.TypedProperties;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jboss.as.clustering.infinispan.InfinispanMessages;
import org.jboss.as.clustering.infinispan.RemoteCacheStore;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.naming.ManagedReferenceInjector;
import org.jboss.as.naming.ServiceBasedNamingStore;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.services.path.AbstractPathService;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
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
            for (ConfigurationBuilder builder: holder.getNamedConfigurationBuilders().values()) {
                Configuration config = builder.build();
                map.put(config.clustering().cacheMode(), config);
            }
            for (CacheMode mode: CacheMode.values()) {
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
        try {
            InputStream input = url.openStream();
            Parser parser = new Parser(Parser.class.getClassLoader());
            try {
                return parser.parse(input);
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Failed to parse %s", url), e);
        }
    }

    private static URL find(String resource, ClassLoader... loaders) {
        for (ClassLoader loader: loaders) {
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
        // the name attribute is required, and can always be found from the operation address
        PathAddress cacheAddress = PathAddress.pathAddress(operation.get(OP_ADDR));
        String cacheName = cacheAddress.getLastElement().getValue();
        model.get(ModelKeys.NAME).set(cacheName);

        this.populateCacheMode(operation, model);
        this.populate(operation, model);
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model, ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {
        // Because we use child resources in a read-only manner to configure the cache, replace the local model with the full model
        model = Resource.Tools.readModel(context.readResource(PathAddress.EMPTY_ADDRESS));

        // Configuration to hold the operation data
        ConfigurationBuilder builder = new ConfigurationBuilder().read(getDefaultConfiguration(this.mode));

         // create a list for dependencies which may need to be added during processing
        List<Dependency<?>> dependencies = new LinkedList<Dependency<?>>();

        // get all required addresses, names and service names
        PathAddress cacheAddress = PathAddress.pathAddress(operation.get(OP_ADDR));
        PathAddress containerAddress = cacheAddress.subAddress(0, cacheAddress.size() - 1);
        String cacheName = cacheAddress.getLastElement().getValue();
        String containerName = containerAddress.getLastElement().getValue();

        // process cache configuration ModelNode describing overrides to defaults
        processModelNode(containerName, model, builder, dependencies);

        ServiceName containerServiceName = EmbeddedCacheManagerService.getServiceName(containerName);
        ServiceName cacheServiceName = containerServiceName.append(cacheName);
        ServiceName cacheConfigurationServiceName = CacheConfigurationService.getServiceName(containerName, cacheName);

        // get container Model
        Resource rootResource = context.getRootResource();
        ModelNode container = rootResource.navigate(containerAddress).getModel();

        // get default cache of the container and start mode
        // AS7-3488 make default-cache no required attribute
        String defaultCache = container.get(ModelKeys.DEFAULT_CACHE).asString();
        ServiceController.Mode initialMode = model.hasDefined(ModelKeys.START) ? StartMode.valueOf(model.get(ModelKeys.START).asString()).getMode() : ServiceController.Mode.ON_DEMAND;

        // install the cache configuration service (configures a cache)
        ServiceTarget target = context.getServiceTarget();
        InjectedValue<EmbeddedCacheManager> containerInjection = new InjectedValue<EmbeddedCacheManager>();
        CacheConfigurationDependencies cacheConfigurationDependencies = new CacheConfigurationDependencies(containerInjection);
        CacheConfigurationService cacheConfigurationService = new CacheConfigurationService(cacheName, builder, cacheConfigurationDependencies);

        ServiceBuilder<Configuration> configBuilder = target.addService(cacheConfigurationServiceName, cacheConfigurationService)
                .addDependency(containerServiceName, EmbeddedCacheManager.class, containerInjection)
                .setInitialMode(ServiceController.Mode.PASSIVE)
        ;

        Configuration config = builder.build();
        if (config.invocationBatching().enabled()) {
            cacheConfigurationDependencies.getTransactionManagerInjector().inject(BatchModeTransactionManager.getInstance());
        } else if (config.transaction().transactionMode() == org.infinispan.transaction.TransactionMode.TRANSACTIONAL) {
            configBuilder.addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, TransactionManager.class, cacheConfigurationDependencies.getTransactionManagerInjector());
            if (config.transaction().useSynchronization()) {
                configBuilder.addDependency(TxnServices.JBOSS_TXN_SYNCHRONIZATION_REGISTRY, TransactionSynchronizationRegistry.class, cacheConfigurationDependencies.getTransactionSynchronizationRegistryInjector());
            }
        }

        // add in any additional dependencies resulting from ModelNode parsing
        for (Dependency<?> dependency: dependencies) {
            this.addDependency(configBuilder, dependency);
        }
        // add an alias for the default cache
        if (cacheName.equals(defaultCache)) {
            configBuilder.addAliases(CacheConfigurationService.getServiceName(containerName, null));
        }
        newControllers.add(configBuilder.install());
        log.debugf("Cache configuration service for %s installed for container %s", cacheName, containerName);

        // now install the corresponding cache service (starts a configured cache)
        CacheDependencies cacheDependencies = new CacheDependencies(containerInjection);
        CacheService<Object, Object> cacheService = new CacheService<Object, Object>(cacheName, cacheDependencies);

        ServiceBuilder<Cache<Object,Object>> cacheBuilder = target.addService(cacheServiceName, cacheService)
                .addDependency(cacheConfigurationServiceName)
                .setInitialMode(initialMode)
        ;

        if (config.transaction().recovery().enabled()) {
            cacheBuilder.addDependency(TxnServices.JBOSS_TXN_ARJUNA_RECOVERY_MANAGER, XAResourceRecoveryRegistry.class, cacheDependencies.getRecoveryRegistryInjector());
        }

        // add an alias for the default cache
        if (cacheName.equals(defaultCache)) {
            cacheBuilder.addAliases(CacheService.getServiceName(containerName,  null));
        }

        if (initialMode == ServiceController.Mode.ACTIVE) {
            cacheBuilder.addListener(verificationHandler);
        }

        newControllers.add(cacheBuilder.install());

        String jndiName = (model.hasDefined(ModelKeys.JNDI_NAME) ? InfinispanJndiName.toJndiName(model.get(ModelKeys.JNDI_NAME).asString()) : InfinispanJndiName.defaultCacheJndiName(containerName, cacheName)).getAbsoluteName();
        ContextNames.BindInfo bindInfo = ContextNames.bindInfoFor(jndiName);

        BinderService binder = new BinderService(bindInfo.getBindName());
        @SuppressWarnings("rawtypes")
        ServiceBuilder<ManagedReferenceFactory> binderBuilder = target.addService(bindInfo.getBinderServiceName(), binder)
                .addAliases(ContextNames.JAVA_CONTEXT_SERVICE_NAME.append(jndiName))
                .addDependency(cacheServiceName, Cache.class, new ManagedReferenceInjector<Cache>(binder.getManagedObjectInjector()))
                .addDependency(bindInfo.getParentContextServiceName(), ServiceBasedNamingStore.class, binder.getNamingStoreInjector())
                .setInitialMode(ServiceController.Mode.PASSIVE)
        ;
        newControllers.add(binderBuilder.install());

        log.debugf("Cache service for cache %s installed for container %s", cacheName, containerName);
    }

    private <T> void addDependency(ServiceBuilder<?> builder, Dependency<T> dependency) {
        ServiceName name = dependency.getName();
        Injector<T> injector = dependency.getInjector();
        if (injector != null) {
            builder.addDependency(name, dependency.getType(), injector);
        } else {
            builder.addDependency(name);
        }
    }

    abstract void populateCacheMode(ModelNode fromModel, ModelNode toModel) throws OperationFailedException;

    /**
     * Transfer elements common to both operations and models
     *
     * @param fromModel
     * @param toModel
     */
    void populate(ModelNode fromModel, ModelNode toModel) {

        if (fromModel.hasDefined(ModelKeys.START)) {
            toModel.get(ModelKeys.START).set(fromModel.get(ModelKeys.START));
        }
        if (fromModel.hasDefined(ModelKeys.BATCHING)) {
            toModel.get(ModelKeys.BATCHING).set(fromModel.get(ModelKeys.BATCHING));
        }
        if (fromModel.hasDefined(ModelKeys.INDEXING)) {
            toModel.get(ModelKeys.INDEXING).set(fromModel.get(ModelKeys.INDEXING));
        }
        if (fromModel.hasDefined(ModelKeys.JNDI_NAME)) {
            toModel.get(ModelKeys.JNDI_NAME).set(fromModel.get(ModelKeys.JNDI_NAME));
        }
    }

    /**
     * Create a Configuration object initialized from the operation ModelNode
     * @param containerName the name of the cache container
     * @param cache ModelNode representing cache configuration
     * @param builder ConfigurationBuilder object to add data to
     * @return initialised Configuration object
     */
    void processModelNode(String containerName, ModelNode cache, ConfigurationBuilder builder, List<Dependency<?>> dependencies) {
        builder.classLoader(this.getClass().getClassLoader());
        builder.clustering().cacheMode(CacheMode.valueOf(cache.require(ModelKeys.MODE).asString()));

        if (cache.hasDefined(ModelKeys.INDEXING)) {
            Indexing indexing = Indexing.valueOf(cache.get(ModelKeys.INDEXING).asString());
            builder.indexing().enabled(indexing.isEnabled()).indexLocalOnly(indexing.isLocalOnly());
        }
        if (cache.hasDefined(ModelKeys.QUEUE_SIZE)) {
            int size = cache.get(ModelKeys.QUEUE_SIZE).asInt();
            builder.clustering().async().replQueueMaxElements(size).useReplQueue(size > 0);
        }
        if (cache.hasDefined(ModelKeys.QUEUE_FLUSH_INTERVAL)) {
            builder.clustering().async().replQueueInterval(cache.get(ModelKeys.QUEUE_FLUSH_INTERVAL).asLong());
        }
        if (cache.hasDefined(ModelKeys.REMOTE_TIMEOUT)) {
            builder.clustering().sync().replTimeout(cache.get(ModelKeys.REMOTE_TIMEOUT).asLong());
        }
        if (cache.hasDefined(ModelKeys.OWNERS)) {
            builder.clustering().hash().numOwners(cache.get(ModelKeys.OWNERS).asInt());
        }
        if (cache.hasDefined(ModelKeys.VIRTUAL_NODES)) {
            builder.clustering().hash().numVirtualNodes(cache.get(ModelKeys.VIRTUAL_NODES).asInt());
        }
        if (cache.hasDefined(ModelKeys.L1_LIFESPAN)) {
            long lifespan = cache.get(ModelKeys.L1_LIFESPAN).asLong();
            if (lifespan > 0) {
                builder.clustering().l1().enable().lifespan(lifespan);
            } else {
                builder.clustering().l1().disable();
            }
        }

        // locking is a child resource
        if (cache.hasDefined(ModelKeys.LOCKING) && cache.get(ModelKeys.LOCKING, ModelKeys.LOCKING_NAME).isDefined()) {
            ModelNode locking = cache.get(ModelKeys.LOCKING, ModelKeys.LOCKING_NAME);
            if (locking.hasDefined(ModelKeys.ISOLATION)) {
                builder.locking().isolationLevel(IsolationLevel.valueOf(locking.get(ModelKeys.ISOLATION).asString()));
            }
            if (locking.hasDefined(ModelKeys.STRIPING)) {
                builder.locking().useLockStriping(locking.get(ModelKeys.STRIPING).asBoolean());
            }
            if (locking.hasDefined(ModelKeys.ACQUIRE_TIMEOUT)) {
                builder.locking().lockAcquisitionTimeout(locking.get(ModelKeys.ACQUIRE_TIMEOUT).asLong());
            }
            if (locking.hasDefined(ModelKeys.CONCURRENCY_LEVEL)) {
                builder.locking().concurrencyLevel(locking.get(ModelKeys.CONCURRENCY_LEVEL).asInt());
            }
        }

        TransactionMode txMode = TransactionMode.NONE;
        LockingMode lockingMode = LockingMode.OPTIMISTIC;
        // locking is a child resource
        if (cache.hasDefined(ModelKeys.TRANSACTION) && cache.get(ModelKeys.TRANSACTION, ModelKeys.TRANSACTION_NAME).isDefined()) {
            ModelNode transaction = cache.get(ModelKeys.TRANSACTION, ModelKeys.TRANSACTION_NAME);
            if (transaction.hasDefined(ModelKeys.STOP_TIMEOUT)) {
                builder.transaction().cacheStopTimeout(transaction.get(ModelKeys.STOP_TIMEOUT).asLong());
            }
            if (transaction.hasDefined(ModelKeys.MODE)) {
                txMode = TransactionMode.valueOf(transaction.get(ModelKeys.MODE).asString());
            }
            if (transaction.hasDefined(ModelKeys.LOCKING)) {
                lockingMode = LockingMode.valueOf(transaction.get(ModelKeys.LOCKING).asString());
            }
        }
        builder.transaction()
            .transactionMode(txMode.getMode())
            .lockingMode(lockingMode)
            .useSynchronization(!txMode.isXAEnabled())
            .recovery().enabled(txMode.isRecoveryEnabled())
        ;
        if (txMode.isRecoveryEnabled()) {
            builder.transaction().syncCommitPhase(true).syncRollbackPhase(true);
        }
        if (cache.hasDefined(ModelKeys.BATCHING)) {
            if (cache.get(ModelKeys.BATCHING).asBoolean()) {
                builder.transaction().transactionMode(org.infinispan.transaction.TransactionMode.TRANSACTIONAL).invocationBatching().enable();
            } else {
                builder.transaction().invocationBatching().disable();
            }
        }
        // eviction is a child resource
        if (cache.hasDefined(ModelKeys.EVICTION) && cache.get(ModelKeys.EVICTION, ModelKeys.EVICTION_NAME).isDefined()) {
            ModelNode eviction = cache.get(ModelKeys.EVICTION, ModelKeys.EVICTION_NAME);

            if (eviction.hasDefined(ModelKeys.STRATEGY)) {
                builder.eviction().strategy(EvictionStrategy.valueOf(eviction.get(ModelKeys.STRATEGY).asString()));
            }
            if (eviction.hasDefined(ModelKeys.MAX_ENTRIES)) {
                builder.eviction().maxEntries(eviction.get(ModelKeys.MAX_ENTRIES).asInt());
            }
        }
        // expiration is a child resource
        if (cache.hasDefined(ModelKeys.EXPIRATION) && cache.get(ModelKeys.EXPIRATION, ModelKeys.EXPIRATION_NAME).isDefined()) {
            ModelNode expiration = cache.get(ModelKeys.EXPIRATION, ModelKeys.EXPIRATION_NAME);
            if (expiration.hasDefined(ModelKeys.MAX_IDLE)) {
                builder.expiration().maxIdle(expiration.get(ModelKeys.MAX_IDLE).asLong());
            }
            if (expiration.hasDefined(ModelKeys.LIFESPAN)) {
                builder.expiration().lifespan(expiration.get(ModelKeys.LIFESPAN).asLong());
            }
            if (expiration.hasDefined(ModelKeys.INTERVAL)) {
                builder.expiration().wakeUpInterval(expiration.get(ModelKeys.INTERVAL).asLong());
            }
        }

        String storeKey = this.findStoreKey(cache);
        if (storeKey != null) {
            ModelNode store = this.getStoreModelNode(cache);
            builder.loaders()
                    .shared(store.hasDefined(ModelKeys.SHARED) ? store.get(ModelKeys.SHARED).asBoolean() : false)
                    .preload(store.hasDefined(ModelKeys.PRELOAD) ? store.get(ModelKeys.PRELOAD).asBoolean() : false)
                    .passivation(store.hasDefined(ModelKeys.PASSIVATION) ? store.get(ModelKeys.PASSIVATION).asBoolean() : true)
            ;
            LoaderConfigurationBuilder storeBuilder = builder.loaders().addCacheLoader()
                    .fetchPersistentState(store.hasDefined(ModelKeys.FETCH_STATE) ? store.get(ModelKeys.FETCH_STATE).asBoolean() : true)
                    .purgeOnStartup(store.hasDefined(ModelKeys.PURGE) ? store.get(ModelKeys.PURGE).asBoolean() : true)
                    .purgeSynchronously(true)
            ;
            storeBuilder.singletonStore().enabled(store.hasDefined(ModelKeys.SINGLETON) ? store.get(ModelKeys.SINGLETON).asBoolean() : false);
            this.buildCacheStore(storeBuilder, containerName, store, storeKey, dependencies);
        }
    }

    private String findStoreKey(ModelNode cache) {
        if (cache.hasDefined(ModelKeys.STORE)) {
            return ModelKeys.STORE;
        } else if (cache.hasDefined(ModelKeys.FILE_STORE)) {
            return ModelKeys.FILE_STORE;
        } else if (cache.hasDefined(ModelKeys.JDBC_STORE)) {
            return ModelKeys.JDBC_STORE;
        } else if (cache.hasDefined(ModelKeys.REMOTE_STORE)) {
            return ModelKeys.REMOTE_STORE;
        }
        return null;
    }

    private ModelNode getStoreModelNode(ModelNode cache) {
        if (cache.hasDefined(ModelKeys.STORE)) {
            return cache.get(ModelKeys.STORE, ModelKeys.STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.FILE_STORE)) {
            return cache.get(ModelKeys.FILE_STORE, ModelKeys.FILE_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.JDBC_STORE)) {
            return cache.get(ModelKeys.JDBC_STORE, ModelKeys.JDBC_STORE_NAME);
        } else if (cache.hasDefined(ModelKeys.REMOTE_STORE)) {
            return cache.get(ModelKeys.REMOTE_STORE, ModelKeys.REMOTE_STORE_NAME);
        }
        return null;
    }


    private void buildCacheStore(LoaderConfigurationBuilder builder, String containerName, ModelNode store, String storeKey, List<Dependency<?>> dependencies) {
        final Properties properties = new TypedProperties();
        if (store.hasDefined(ModelKeys.PROPERTY)) {
            for (Property property : store.get(ModelKeys.PROPERTY).asPropertyList()) {
                // the format of the property elements
                //  "property" => {
                //       "relative-to" => {"value" => "fred"},
                //   }
                String propertyName = property.getName();
                Property complexValue = property.getValue().asProperty();
                String propertyValue = complexValue.getValue().asString();
                properties.setProperty(propertyName, propertyValue);
            }
        }
        builder.withProperties(properties);

        if (storeKey.equals(ModelKeys.FILE_STORE)) {
            builder.cacheLoader(new FileCacheStore());
            final String path = store.hasDefined(ModelKeys.PATH) ? store.get(ModelKeys.PATH).asString() : containerName;
            Injector<String> injector = new SimpleInjector<String>() {
                @Override
                public void inject(String value) {
                    StringBuilder location = new StringBuilder(value);
                    if (path != null) {
                        location.append(File.separatorChar).append(path);
                    }
                    properties.setProperty("location", location.toString());
                }
            };
            String relativeTo = store.hasDefined(ModelKeys.RELATIVE_TO) ? store.get(ModelKeys.RELATIVE_TO).asString() : ServerEnvironment.SERVER_DATA_DIR;
            dependencies.add(new Dependency<String>(AbstractPathService.pathNameOf(relativeTo), String.class, injector));
            properties.setProperty("fsyncMode", "perWrite");
        } else if (storeKey.equals(ModelKeys.JDBC_STORE)) {
            builder.cacheLoader(this.createJDBCStore(properties, store));
            String datasource = store.require(ModelKeys.DATASOURCE).asString();
            dependencies.add(new Dependency<Object>(ServiceName.JBOSS.append("data-source").append("reference-factory").append(datasource)));
            properties.setProperty("datasourceJndiLocation", datasource);
            properties.setProperty("connectionFactoryClass", ManagedConnectionFactory.class.getName());
        } else if (storeKey.equals(ModelKeys.REMOTE_STORE)) {
            builder.cacheLoader(new RemoteCacheStore());
            for (ModelNode server: store.require(ModelKeys.REMOTE_SERVERS).asList()) {
                String outboundSocketBinding = server.get(ModelKeys.OUTBOUND_SOCKET_BINDING).asString();
                Injector<OutboundSocketBinding> injector = new SimpleInjector<OutboundSocketBinding>() {
                    @Override
                    public void inject(OutboundSocketBinding value) {
                        try {
                            String address = value.getDestinationAddress().getHostAddress() + ":" + value.getDestinationPort();
                            String serverList = properties.getProperty("serverList");
                            properties.setProperty("serverList", (serverList == null) ? address : serverList + ";" + address);
                        } catch (UnknownHostException e) {
                            throw InfinispanMessages.MESSAGES.failedToInjectSocketBinding(e, value);
                        }
                    }
                };
                dependencies.add(new Dependency<OutboundSocketBinding>(OutboundSocketBinding.OUTBOUND_SOCKET_BINDING_BASE_SERVICE_NAME.append(outboundSocketBinding), OutboundSocketBinding.class, injector));
            }
            if (store.hasDefined(ModelKeys.CACHE)) {
                properties.setProperty("remoteCacheName", store.get(ModelKeys.CACHE).asString());
                properties.setProperty("useDefaultRemoteCache", Boolean.toString(false));
            } else {
                properties.setProperty("useDefaultRemoteCache", Boolean.toString(true));
            }
            if (store.hasDefined(ModelKeys.SOCKET_TIMEOUT)) {
                properties.setProperty("soTimeout", store.require(ModelKeys.SOCKET_TIMEOUT).asString());
            }
            if (store.hasDefined(ModelKeys.TCP_NO_DELAY)) {
                properties.setProperty("tcpNoDelay", store.require(ModelKeys.TCP_NO_DELAY).asString());
            }
        } else {
            String className = store.require(ModelKeys.CLASS).asString();
            try {
                CacheLoader loader = CacheLoader.class.getClassLoader().loadClass(className).asSubclass(CacheLoader.class).newInstance();
                builder.cacheLoader(loader);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("%s is not a valid cache store", className), e);
            }
        }
    }

    private CacheStore createJDBCStore(Properties properties, ModelNode store) {
        boolean useEntryTable = store.hasDefined(ModelKeys.ENTRY_TABLE);
        boolean useBucketTable = store.hasDefined(ModelKeys.BUCKET_TABLE);
        if (useEntryTable && !useBucketTable) {
            this.setEntryTableProperties(properties, store.get(ModelKeys.ENTRY_TABLE), "", "stringsTableNamePrefix");
            return new JdbcStringBasedCacheStore();
        } else if (useBucketTable && !useEntryTable) {
            this.setBucketTableProperties(properties, store.get(ModelKeys.BUCKET_TABLE), "", "bucketTableNamePrefix");
            return new JdbcBinaryCacheStore();
        }
        // Else, use mixed mode
        this.setEntryTableProperties(properties, store.get(ModelKeys.ENTRY_TABLE), "ForStrings", "tableNamePrefixForStrings");
        this.setBucketTableProperties(properties, store.get(ModelKeys.BUCKET_TABLE), "ForBinary", "tableNamePrefixForBinary");
        return new JdbcMixedCacheStore();
    }

    private void setBucketTableProperties(Properties properties, ModelNode table, String propertySuffix, String tableNamePrefixProperty) {
        this.setTableProperties(properties, table, propertySuffix, tableNamePrefixProperty, "ispn_bucket");
    }

    private void setEntryTableProperties(Properties properties, ModelNode table, String propertySuffix, String tableNamePrefixProperty) {
        this.setTableProperties(properties, table, propertySuffix, tableNamePrefixProperty, "ispn_entry");
    }

    private void setTableProperties(Properties properties, ModelNode table, String propertySuffix, String tableNamePrefixProperty, String defaultTableNamePrefix) {
        properties.setProperty("batchSize", Integer.toString(table.isDefined() && table.hasDefined(ModelKeys.BATCH_SIZE) ? table.get(ModelKeys.BATCH_SIZE).asInt() : TableManipulation.DEFAULT_BATCH_SIZE));
        properties.setProperty("fetchSize", Integer.toString(table.isDefined() && table.hasDefined(ModelKeys.FETCH_SIZE) ? table.get(ModelKeys.FETCH_SIZE).asInt() : TableManipulation.DEFAULT_FETCH_SIZE));
        properties.setProperty(tableNamePrefixProperty, table.isDefined() && table.hasDefined(ModelKeys.PREFIX) ? table.get(ModelKeys.PREFIX).asString() : defaultTableNamePrefix);
        properties.setProperty("idColumnName" + propertySuffix, this.getColumnProperty(table,  ModelKeys.ID_COLUMN, ModelKeys.NAME, "id"));
        properties.setProperty("idColumnType" + propertySuffix, this.getColumnProperty(table,  ModelKeys.ID_COLUMN, ModelKeys.TYPE, "VARCHAR"));
        properties.setProperty("dataColumnName" + propertySuffix, this.getColumnProperty(table,  ModelKeys.DATA_COLUMN, ModelKeys.NAME, "datum"));
        properties.setProperty("dataColumnType" + propertySuffix, this.getColumnProperty(table,  ModelKeys.DATA_COLUMN, ModelKeys.TYPE, "BINARY"));
        properties.setProperty("timestampColumnName" + propertySuffix, this.getColumnProperty(table,  ModelKeys.TIMESTAMP_COLUMN, ModelKeys.NAME, "version"));
        properties.setProperty("timestampColumnType" + propertySuffix, this.getColumnProperty(table,  ModelKeys.TIMESTAMP_COLUMN, ModelKeys.TYPE, "BIGINT"));
    }

    private String getColumnProperty(ModelNode table, String columnKey, String key, String defaultValue) {
        if (!table.isDefined() || !table.hasDefined(columnKey)) return defaultValue;
        ModelNode column = table.get(columnKey);
        return column.hasDefined(key) ? column.get(key).asString() : defaultValue;
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

    private abstract class SimpleInjector<I> implements Injector<I> {
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

        CacheConfigurationDependencies(Value<EmbeddedCacheManager> container) {
            this.container = container;
        }

        Injector<TransactionManager> getTransactionManagerInjector() {
            return this.tm;
        }

        Injector<TransactionSynchronizationRegistry> getTransactionSynchronizationRegistryInjector() {
            return this.tsr;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7003.java