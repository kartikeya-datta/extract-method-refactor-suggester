error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10932.java
text:
```scala
S@@tringBuilder buf = new StringBuilder();

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.kernel;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.commons.collections.set.MapBackedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.conf.BrokerValue;
import org.apache.openjpa.conf.MetaDataRepositoryValue;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.conf.OpenJPAVersion;
import org.apache.openjpa.datacache.DataCacheStoreManager;
import org.apache.openjpa.ee.ManagedRuntime;
import org.apache.openjpa.enhance.ManagedClassSubclasser;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.event.BrokerFactoryEvent;
import org.apache.openjpa.event.RemoteCommitEventManager;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.concurrent.ConcurrentReferenceHashSet;
import org.apache.openjpa.meta.MetaDataModes;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.util.GeneralException;
import org.apache.openjpa.util.InvalidStateException;
import org.apache.openjpa.util.MetaDataException;
import org.apache.openjpa.util.OpenJPAException;
import org.apache.openjpa.util.UserException;
import org.apache.openjpa.writebehind.WriteBehindCache;
import org.apache.openjpa.writebehind.WriteBehindCallback;
import org.apache.openjpa.writebehind.WriteBehindConfigurationException;
import org.apache.openjpa.writebehind.WriteBehindStoreManager;

/**
 * Abstract implementation of the {@link BrokerFactory}
 * that must be subclassed for a specific runtime.
 *
 * @author Abe White
 */
@SuppressWarnings("serial")
public abstract class AbstractBrokerFactory
    implements BrokerFactory {

    private static final Localizer _loc = Localizer.forPackage(AbstractBrokerFactory.class);

    // static mapping of configurations to pooled broker factories
    private static final Map<Object,AbstractBrokerFactory> _pool = 
       Collections.synchronizedMap(new HashMap<Object,AbstractBrokerFactory>());

    // configuration
    private final OpenJPAConfiguration _conf;
    private transient boolean _readOnly = false;
    private transient boolean _closed = false;
    private transient RuntimeException _closedException = null;
    private Map<Object,Object> _userObjects = null;

    // internal lock: spec forbids synchronization on this object
    private final ReentrantLock _lock = new ReentrantLock();

    // maps global transactions to associated brokers
    private transient ConcurrentHashMap<Object,Collection<Broker>> 
        _transactional = new ConcurrentHashMap<Object,Collection<Broker>>();

    // weak-ref tracking of open brokers
    private transient Set<Broker> _brokers;

    // cache the class names loaded from the persistent classes property so
    // that we can re-load them for each new broker
    private transient Collection<String> _pcClassNames = null;
    private transient Collection<ClassLoader> _pcClassLoaders = null;
    private transient boolean _persistentTypesLoaded = false;

    // lifecycle listeners to pass to each broker
    private transient Map<Object, Class<?>[]> _lifecycleListeners = null;

    // transaction listeners to pass to each broker
    private transient List<Object> _transactionListeners = null;

    // key under which this instance can be stored in the broker pool
    // and later identified
    private Object _poolKey;   
    
    private WriteBehindCallback _writeBehindCallback; 

    /**
     * Return an internal factory pool key for the given configuration.
     *
     * @since 1.1.0
     */
    protected static Object toPoolKey(Map<String,Object> map) {
        Object key = Configurations.getProperty("Id", map);
        return ( key != null) ? key : map;
    }

    /**
     * Register <code>factory</code> in the pool under <code>key</code>.
     *
     * @since 1.1.0
     */
    protected static void pool(Object key, AbstractBrokerFactory factory) {
        synchronized(_pool) {
            _pool.put(key, factory);
            factory.setPoolKey(key);
            factory.makeReadOnly();
        }
    }

    /**
     * Return the pooled factory matching the given key, or null
     * if none. The key must be of the form created by {@link #getPoolKey}.
     */
    public static AbstractBrokerFactory getPooledFactoryForKey(Object key) {
        return (AbstractBrokerFactory) _pool.get(key);
    }

    /**
     * Constructor. Configuration must be provided on construction.
     */
    protected AbstractBrokerFactory(OpenJPAConfiguration config) {
        _conf = config;
        _brokers = newBrokerSet();
        getPcClassLoaders();
        if (config.isInitializeEagerly()) {
            newBroker(_conf.getConnectionUserName(), 
            	_conf.getConnectionPassword(), 
            	_conf.isConnectionFactoryModeManaged(),
                _conf.getConnectionRetainModeConstant(), false).close(); 
        }

        initWriteBehindCallback();        
    }

    /**
     * Return the configuration for this factory.
     */
    public OpenJPAConfiguration getConfiguration() {
        return _conf;
    }

    public Broker newBroker() {
        return newBroker(_conf.getConnectionUserName(), _conf.getConnectionPassword());
    }

    public Broker newBroker(String user, String pass) {
        return newBroker(user, pass, _conf.isTransactionModeManaged(), _conf.getConnectionRetainModeConstant());
    }

    public Broker newBroker(boolean managed, int connRetainMode) {
        return newBroker(_conf.getConnectionUserName(), _conf.getConnectionPassword(), managed, connRetainMode);
    }

    public Broker newBroker(String user, String pass, boolean managed, int connRetainMode) {
        return newBroker(user, pass, managed, connRetainMode, true);
    }

    public Broker newBroker(String user, String pass, boolean managed, int connRetainMode, boolean findExisting) {
        return newBroker(user, pass, managed, connRetainMode, findExisting, false);
    }
    
    public Broker newBroker(String user, String pass, boolean managed, int connRetainMode, boolean findExisting,
        boolean writeBehindCallback) {
        try {
            assertOpen();
            makeReadOnly();

            Broker broker = null;
            if (findExisting)
                broker = findBroker(user, pass, managed);
            if (broker == null) {
                broker = newBrokerImpl(user, pass);
                initializeBroker(managed, connRetainMode, broker, false, writeBehindCallback);
            }
            return broker;
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (RuntimeException re) {
            throw new GeneralException(re);
        }
    }

    void initializeBroker(boolean managed, int connRetainMode, Broker broker, boolean fromDeserialization) {
        initializeBroker(managed, connRetainMode, broker, fromDeserialization, false);
    }    
    
    void initializeBroker(boolean managed, int connRetainMode, Broker broker, boolean fromDeserialization,
        boolean fromWriteBehindCallback) {
        assertOpen();
        makeReadOnly();
        
        DelegatingStoreManager dsm = createDelegatingStoreManager();

        ((BrokerImpl) broker).initialize(this, dsm, managed, connRetainMode, fromDeserialization,
            fromWriteBehindCallback);
        if (!fromDeserialization)
            addListeners(broker);

        // if we're using remote events, register the event manager so
        // that it can broadcast commit notifications from the broker
        RemoteCommitEventManager remote = _conf.getRemoteCommitEventManager();
        if (remote.areRemoteEventsEnabled())
            broker.addTransactionListener(remote);

        loadPersistentTypes(broker.getClassLoader());
        _brokers.add(broker);
        _conf.setReadOnly(Configuration.INIT_STATE_FROZEN);
    }

    /**
     * Add factory-registered lifecycle listeners to the broker.
     */
    protected void addListeners(Broker broker) {
        if (_lifecycleListeners != null && !_lifecycleListeners.isEmpty()) {
            for (Map.Entry<Object,Class<?>[]> entry : _lifecycleListeners.entrySet()) {
                broker.addLifecycleListener(entry.getKey(), entry.getValue());
            }
        }

        if (_transactionListeners != null && !_transactionListeners.isEmpty()) {
            for (Iterator<Object> itr = _transactionListeners.iterator();
                itr.hasNext(); ) {
                broker.addTransactionListener(itr.next());
            }
        }
    }

    /**
     * Load the configured persistent classes list. Performed automatically
     * whenever a broker is created.
     */
    public void loadPersistentTypes(ClassLoader envLoader) {
        // if we've loaded the persistent types and the class name list
        // is empty, then we can simply return. Note that there is a
        // potential threading scenario in which _persistentTypesLoaded is
        // false when read, but the work to populate _pcClassNames has
        // already been done. This is ok; _pcClassNames can tolerate
        // concurrent access, so the worst case is that the list is
        // persistent type data is processed multiple times, which this
        // algorithm takes into account.
        if (_persistentTypesLoaded && _pcClassNames.isEmpty())
            return;

        // cache persistent type names if not already
        ClassLoader loader = _conf.getClassResolverInstance().
            getClassLoader(getClass(), envLoader);
        Collection<Class<?>> toRedefine = new ArrayList<Class<?>>();
        if (!_persistentTypesLoaded) {
            Collection<Class<?>> clss = _conf.getMetaDataRepositoryInstance().
                loadPersistentTypes(false, loader, _conf.isInitializeEagerly());
            if (clss.isEmpty())
                _pcClassNames = Collections.emptyList();
            else {
                Collection<String> c = new ArrayList<String>(clss.size());
                for (Iterator<Class<?>> itr = clss.iterator(); itr.hasNext();) {
                    Class<?> cls = itr.next();
                    c.add(cls.getName());
                    if (needsSub(cls))
                        toRedefine.add(cls);
                }
                getPcClassLoaders().add(loader);
                _pcClassNames = c;
            }
            _persistentTypesLoaded = true;
        } else {
            // reload with this loader
            if (getPcClassLoaders().add(loader)) {
                for (String clsName : _pcClassNames) {
                    try {
                        Class<?> cls = Class.forName(clsName, true, loader);
                        if (needsSub(cls))
                            toRedefine.add(cls);
                    } catch (Throwable t) {
                        _conf.getLog(OpenJPAConfiguration.LOG_RUNTIME).warn(null, t);
                    }
                }
            }
        }

        // get the ManagedClassSubclasser into the loop
        ManagedClassSubclasser.prepareUnenhancedClasses(_conf, toRedefine, envLoader);
    }

    private boolean needsSub(Class<?> cls) {
        return !cls.isInterface()
            && !PersistenceCapable.class.isAssignableFrom(cls);
    }

    public void addLifecycleListener(Object listener, Class<?>[] classes) {
        lock();
        try {
            assertOpen();
            if (_lifecycleListeners == null)
                _lifecycleListeners = new HashMap<Object, Class<?>[]>(7);
            _lifecycleListeners.put(listener, classes);
        } finally {
            unlock();
        }
    }

    public void removeLifecycleListener(Object listener) {
        lock();
        try {
            assertOpen();
            if (_lifecycleListeners != null)
                _lifecycleListeners.remove(listener);
        } finally {
            unlock();
        }
    }

    public void addTransactionListener(Object listener) {
        lock();
        try {
            assertOpen();
            if (_transactionListeners == null)
                _transactionListeners = new LinkedList<Object>();
            _transactionListeners.add(listener);
        } finally {
            unlock();
        }
    }

    public void removeTransactionListener(Object listener) {
        lock();
        try {
            assertOpen();
            if (_transactionListeners != null)
                _transactionListeners.remove(listener);
        } finally {
            unlock();
        }
    }

    /**
     * Returns true if this broker factory is closed.
     */
    public boolean isClosed() {
        return _closed;
    }

    public void close() {
        lock();

        if(_writeBehindCallback != null) {
            _writeBehindCallback.close();
        }
        
        try {
            assertOpen();
            assertNoActiveTransaction();

            // remove from factory pool
            synchronized (_pool) {
                if (_pool.get(_poolKey) == this)
                    _pool.remove(_poolKey);
            }

            // close all brokers
            for (Broker broker : _brokers) {
                // Check for null because _brokers may contain weak references
                if ((broker != null) && (!broker.isClosed()))
                    broker.close();
            }

            if(_conf.metaDataRepositoryAvailable()) {
                // remove metadata repository from listener list
                PCRegistry.removeRegisterClassListener
                    (_conf.getMetaDataRepositoryInstance());
            }

            _conf.close();
            _closed = true;
            Log log = _conf.getLog(OpenJPAConfiguration.LOG_RUNTIME);
            if (log.isTraceEnabled())
                _closedException = new IllegalStateException();
        } finally {
            unlock();
        }
    }

    /**
     * Subclasses should override this method to add a <code>Platform</code>
     * property listing the runtime platform, such as:
     * <code>OpenJPA JDBC Edition: Oracle Database</code>
     */
    public Map<String,Object> getProperties() {
        // required props are VendorName and VersionNumber
        Map<String,Object> props = _conf.toProperties(true);
        props.put("VendorName", OpenJPAVersion.VENDOR_NAME);
        props.put("VersionNumber", OpenJPAVersion.VERSION_NUMBER);
        props.put("VersionId", OpenJPAVersion.VERSION_ID);
        return props;
    }

    public Set<String> getSupportedProperties() {
        return _conf.getPropertyKeys();
    }

    public Object getUserObject(Object key) {
        lock();
        try {
            assertOpen();
            return (_userObjects == null) ? null : _userObjects.get(key);
        } finally {
            unlock();
        }
    }

    public Object putUserObject(Object key, Object val) {
        lock();
        try {
            assertOpen();
            if (val == null)
                return (_userObjects == null) ? null : _userObjects.remove(key);

            if (_userObjects == null)
                _userObjects = new HashMap<Object,Object>();
            return _userObjects.put(key, val);
        } finally {
            unlock();
        }
    }

    public void lock() {
        _lock.lock();
    }

    public void unlock() {
        _lock.unlock();
    }

    /**
     * Replaces the factory with this JVMs pooled version if it exists. Also
     * freezes the factory.
     */
    protected Object readResolve()
        throws ObjectStreamException {
        AbstractBrokerFactory factory = getPooledFactoryForKey(_poolKey);
        if (factory != null)
            return factory;

        // reset these transient fields to empty values
        _transactional = new ConcurrentHashMap<Object,Collection<Broker>>();
        _brokers = newBrokerSet();

        makeReadOnly();
        return this;
    }

    private Set<Broker> newBrokerSet() {
        BrokerValue bv;
        if (_conf instanceof OpenJPAConfigurationImpl)
            bv = ((OpenJPAConfigurationImpl) _conf).brokerPlugin;
        else
            bv = (BrokerValue) _conf.getValue(BrokerValue.KEY);

        if (FinalizingBrokerImpl.class.isAssignableFrom(
            bv.getTemplateBrokerType(_conf))) {
            return MapBackedSet.decorate(new ConcurrentHashMap(), new Object() { });
        } else {
            return new ConcurrentReferenceHashSet<Broker>(ConcurrentReferenceHashSet.WEAK);
        }
    }

    ////////////////////////
    // Methods for Override
    ////////////////////////

    /**
     * Return a new StoreManager for this runtime. Note that the instance
     * returned here may be wrapped before being passed to the
     * {@link #newBroker} method.
     */
    protected abstract StoreManager newStoreManager();

    /**
     * Find a pooled broker, or return null if none. If using
     * managed transactions, looks for a transactional broker;
     * otherwise returns null by default. This method will be called before
     * {@link #newStoreManager} so that factory subclasses implementing
     * pooling can return a matching manager before a new {@link StoreManager}
     * is created.
     */
    protected Broker findBroker(String user, String pass, boolean managed) {
        if (managed)
            return findTransactionalBroker(user, pass);
        return null;
    }

    /**
     * Return a broker configured with the proper settings.
     * By default, this method constructs a new
     * BrokerImpl of the class set for this factory.
     */
    protected BrokerImpl newBrokerImpl(String user, String pass) {
        BrokerImpl broker = _conf.newBrokerInstance(user, pass);
        if (broker == null)
            throw new UserException(_loc.get("no-broker-class",
                _conf.getBrokerImpl()));

        return broker;
    }

    /**
     * Setup transient state used by this factory based on the
     * current configuration, which will subsequently be locked down. This
     * method will be called before the first broker is requested,
     * and will be re-called each time the factory is deserialized into a JVM
     * that has no configuration for this data store.
     */
    protected void setup() {
    }

    /////////////
    // Utilities
    /////////////

    /**
     * Find a managed runtime broker associated with the
     * current transaction, or returns null if none.
     */
    protected Broker findTransactionalBroker(String user, String pass) {
        Transaction trans;
        ManagedRuntime mr = _conf.getManagedRuntimeInstance();
        Object txKey;
        try {
            trans = mr.getTransactionManager().
                getTransaction();
            txKey = mr.getTransactionKey();

            if (trans == null
 trans.getStatus() == Status.STATUS_NO_TRANSACTION
 trans.getStatus() == Status.STATUS_UNKNOWN)
                return null;
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (Exception e) {
            throw new GeneralException(e);
        }

        Collection<Broker> brokers = _transactional.get(txKey);
        if (brokers != null) {
            // we don't need to synchronize on brokers since one JTA transaction
            // can never be active on multiple concurrent threads.
            for (Broker broker : brokers) {
                if (StringUtils.equals(broker.getConnectionUserName(), user) 
                 && StringUtils.equals(broker.getConnectionPassword(), pass))
                    return broker;
            }
        }
        return null;
    }

    /**
     * Configures the given broker with the current factory option settings.
     */
    protected void configureBroker(BrokerImpl broker) {
        broker.setOptimistic(_conf.getOptimistic());
        broker.setNontransactionalRead(_conf.getNontransactionalRead());
        broker.setNontransactionalWrite(_conf.getNontransactionalWrite());
        broker.setRetainState(_conf.getRetainState());
        broker.setRestoreState(_conf.getRestoreStateConstant());
        broker.setAutoClear(_conf.getAutoClearConstant());
        broker.setIgnoreChanges(_conf.getIgnoreChanges());
        broker.setMultithreaded(_conf.getMultithreaded());
        broker.setAutoDetach(_conf.getAutoDetachConstant());
        broker.setDetachState(_conf.getDetachStateInstance().getDetachState());
    }

    /**
     * Freezes the configuration of this factory.
     */
    public void makeReadOnly() {
        if (_readOnly)
            return;

        lock();
        try {
            // check again
            if (_readOnly)
                return;
            _readOnly = true;

            Log log = _conf.getLog(OpenJPAConfiguration.LOG_RUNTIME);
            if (log.isInfoEnabled())
                log.info(getFactoryInitializationBanner());
            if (log.isTraceEnabled()) {
                Map<String,Object> props = _conf.toProperties(true);
                String lineSep = J2DoPrivHelper.getLineSeparator();
                StringBuffer buf = new StringBuffer();
                Map.Entry<?,?> entry;
                for (Iterator<Map.Entry<String,Object>> itr = props.entrySet().iterator(); itr.hasNext();) {
                    entry = itr.next();
                    buf.append(entry.getKey()).append(": ").append(entry.getValue());
                    if (itr.hasNext())
                        buf.append(lineSep);
                }
                log.trace(_loc.get("factory-properties", buf.toString()));
            }

            // setup transient state
            setup();

            // register the metdata repository to auto-load persistent types
            // and make sure types are enhanced
            MetaDataRepository repos = _conf.getMetaDataRepositoryInstance();
            repos.setValidate(MetaDataRepository.VALIDATE_RUNTIME, true);
            repos.setResolve(MetaDataModes.MODE_MAPPING_INIT, true);
            PCRegistry.addRegisterClassListener(repos);

            // freeze underlying configuration and eagerly initialize to
            // avoid synchronization
            _conf.setReadOnly(Configuration.INIT_STATE_FREEZING);
            _conf.instantiateAll();
            if (_conf.isInitializeEagerly())
            	_conf.setReadOnly(Configuration.INIT_STATE_FROZEN);
            // fire an event for all the broker factory listeners
            // registered on the configuration.
            _conf.getBrokerFactoryEventManager().fireEvent(
                new BrokerFactoryEvent(this,
                    BrokerFactoryEvent.BROKER_FACTORY_CREATED));
        } finally {
            unlock();
        }
    }

    /**
     * Return an object to be written to the log when this broker factory
     * initializes. This happens after the configuration is fully loaded.
     */
    protected Object getFactoryInitializationBanner() {
        return _loc.get("factory-init", OpenJPAVersion.VERSION_NUMBER);
    }

    /**
     * Throw an exception if the factory is closed.  The exact message and
     * content of the exception varies whether TRACE is enabled or not.
     */
    private void assertOpen() {
        if (_closed) {
            if (_closedException == null)  // TRACE not enabled
                throw new InvalidStateException(_loc
                        .get("closed-factory-notrace"));
            else
                throw new InvalidStateException(_loc.get("closed-factory"))
                        .setCause(_closedException);
        }
    }

    ////////////////////
    // Broker utilities
    ////////////////////

    /**
     * Throws a {@link UserException} if a transaction is active. The thrown
     * exception will contain all the Brokers with active transactions as
     * failed objects in the nested exceptions.
     */
    private void assertNoActiveTransaction() {
        Collection<Throwable> excs;
        if (_transactional.isEmpty())
            return;

        excs = new ArrayList<Throwable>(_transactional.size());
        for (Collection<Broker> brokers : _transactional.values()) {
            for (Broker broker : brokers) {
                excs.add(new InvalidStateException(_loc.get("active")).setFailedObject(broker));
            }
        }

        if (!excs.isEmpty())
            throw new InvalidStateException(_loc.get("nested-exceps")).
                setNestedThrowables((Throwable[]) excs.toArray(new Throwable[excs.size()]));
    }

    /**
     * Synchronize the given broker with a managed transaction,
     * optionally starting one if none is in progress.
     *
     * @return true if synched with transaction, false otherwise
     */
    boolean syncWithManagedTransaction(BrokerImpl broker, boolean begin) {
        Transaction trans;
        try {
            ManagedRuntime mr = broker.getManagedRuntime();
            TransactionManager tm = mr.getTransactionManager();
            trans = tm.getTransaction();
            if (trans != null
                && (trans.getStatus() == Status.STATUS_NO_TRANSACTION
 trans.getStatus() == Status.STATUS_UNKNOWN))
                trans = null;

            if (trans == null && begin) {
                tm.begin();
                trans = tm.getTransaction();
            } else if (trans == null)
                return false;

            // synch broker and trans
            trans.registerSynchronization(broker);

            // we don't need to synchronize on brokers or guard against multiple
            // threads using the same trans since one JTA transaction can never
            // be active on multiple concurrent threads.
            Object txKey = mr.getTransactionKey();
            Collection<Broker> brokers = _transactional.get(txKey);
            
            if (brokers == null) {
                brokers = new ArrayList<Broker>(2);
                _transactional.put(txKey, brokers);
                trans.registerSynchronization(new RemoveTransactionSync(txKey));
            }
            brokers.add(broker);
            
            return true;
        } catch (OpenJPAException ke) {
            throw ke;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    /**
     * Returns a set of all the open brokers associated with this factory. The
     * returned set is unmodifiable, and may contain null references.
     */
    public Collection<Broker> getOpenBrokers() {
        return Collections.unmodifiableCollection(_brokers);
    }

    /**
     * Release <code>broker</code> from any internal data structures. This
     * is invoked by <code>broker</code> after the broker is fully closed.
     *
     * @since 1.1.0
     */
    protected void releaseBroker(BrokerImpl broker) {
        _brokers.remove(broker);
    }

    /**
     * @return a key that can be used to obtain this broker factory from the
     * pool at a later time.
     *
     * @since 1.1.0
     */
    public Object getPoolKey() {
        return _poolKey;
    }

    /**
     * Set a key that can be used to obtain this broker factory from the
     * pool at a later time.
     *
     * @since 1.1.0
     */
    void setPoolKey(Object key) {
        _poolKey = key;
    }

    /**
     * Simple synchronization listener to remove completed transactions
     * from our cache.
     */
    private class RemoveTransactionSync
        implements Synchronization {

        private final Object _trans;

        public RemoveTransactionSync(Object trans) {
            _trans = trans;
        }

        public void beforeCompletion() {
        }

        public void afterCompletion(int status) {
            _transactional.remove (_trans);
		}
	}
    
    /**
     * Method insures that deserialized EMF has this reference re-instantiated
     */
    private Collection<ClassLoader> getPcClassLoaders() {
       if (_pcClassLoaders == null)
         _pcClassLoaders = new ConcurrentReferenceHashSet<ClassLoader>(ConcurrentReferenceHashSet.WEAK);
          
       return _pcClassLoaders;
    }

    /**
     * <P>
     * Create a DelegatingStoreManager for use with a Broker created by this factory.
     * </P>
     * <P>
     * If a DataCache has been enabled a DataCacheStoreManager will be returned. If a WriteBehind cache is also enabled 
     * the DataCacheStoreManager will  delegate to a WriteBehindStoreManager. If no WriteBehindCache is in use the 
     * DataCache will delegate to a StoreManager returned by newStoreManager(). 
     * </P>
     * <P>
     * If no DataCache is in use an ROPStoreManager will be returned. 
     * </P>
     * 
     * @return A delegating store manager suitable for the current
     *         configuration.
     */
    protected DelegatingStoreManager createDelegatingStoreManager() { 
        // decorate the store manager for data caching and custom
        // result object providers; always make sure it's a delegating
        // store manager, because it's easier for users to deal with
        // that way
        StoreManager sm = newStoreManager();
        DelegatingStoreManager dsm = null;
        if (_conf.getDataCacheManagerInstance().getSystemDataCache() != null) {
            WriteBehindCache wbCache = _conf.getWriteBehindCacheManagerInstance().getSystemWriteBehindCache();
            if (wbCache != null) {
                dsm = new DataCacheStoreManager(new WriteBehindStoreManager(sm, wbCache));
            } else {
                dsm = new DataCacheStoreManager(sm);
            }
        }
        dsm = new ROPStoreManager((dsm == null) ? sm : dsm);
        
        return dsm;
    }
    
    protected void initWriteBehindCallback() { 
        WriteBehindCache cache = _conf.getWriteBehindCacheManagerInstance().getSystemWriteBehindCache();
        if (cache != null) {
            // Verify we are not missing one or more of the following required
            // WriteBehind configuration parameters:
            //   - openjpa.DataCache
            //   - openjpa.WriteBehindCallback
            if (_conf.getDataCacheManagerInstance().getSystemDataCache() == null) {
                throw new WriteBehindConfigurationException(
                    _loc.get("writebehind-cfg-err",
                    "openjpa.DataCache").getMessage());
            }
            if (_conf.getWriteBehindCallbackInstance() == null ) {
                throw new WriteBehindConfigurationException(
                    _loc.get("writebehind-cfg-err",
                    "openjpa.WriteBehindCallback").getMessage());
            }
    
            Broker broker =
                newBroker(_conf.getConnectionUserName(), 
                          _conf.getConnectionPassword(), 
                          false, // WriteBehind broker is always unmanaged.
                          _conf.getConnectionRetainModeConstant(),
                          false, true);

            // The Broker used by the WriteBehind cache should not be tracked
            // by the factory - we'll manually clean up when the factory is
            // closed.
            _transactional.remove(broker);
            _brokers.remove(broker);
            
            _writeBehindCallback = _conf.getWriteBehindCallbackInstance();
            _writeBehindCallback.initialize(broker, cache);

            new Thread(_writeBehindCallback).start();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10932.java