error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14067.java
text:
```scala
public C@@riteriaBuilder getQueryBuilder() {

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
package org.apache.openjpa.persistence;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cache;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.QueryBuilder;
import javax.persistence.spi.LoadState;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.Reflection;
import org.apache.openjpa.enhance.StateManager;
import org.apache.openjpa.kernel.AbstractBrokerFactory;
import org.apache.openjpa.kernel.AutoDetach;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.kernel.BrokerFactory;
import org.apache.openjpa.kernel.DelegatingBrokerFactory;
import org.apache.openjpa.kernel.DelegatingFetchConfiguration;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.lib.conf.Configurations;
import org.apache.openjpa.lib.conf.ProductDerivations;
import org.apache.openjpa.lib.conf.Value;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.Closeable;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.criteria.CriteriaBuilder;
import org.apache.openjpa.persistence.meta.MetamodelImpl;
import org.apache.openjpa.persistence.query.OpenJPAQueryBuilder;
import org.apache.openjpa.persistence.query.QueryBuilderImpl;
import org.apache.openjpa.util.OpenJPAException;
import serp.util.Strings;

/**
 * Implementation of {@link EntityManagerFactory} that acts as a
 * facade to a {@link BrokerFactory}.
 *
 * @author Marc Prud'hommeaux
 * @nojavadoc
 */
public class EntityManagerFactoryImpl
    implements OpenJPAEntityManagerFactory, OpenJPAEntityManagerFactorySPI,
    Closeable, PersistenceUnitUtil {

    private static final Localizer _loc = Localizer.forPackage
        (EntityManagerFactoryImpl.class);

    private DelegatingBrokerFactory _factory = null;
    private transient Constructor<FetchPlan> _plan = null;
    private transient StoreCache _cache = null;
    private transient QueryResultCache _queryCache = null;
    private transient MetamodelImpl _metaModel;
    
    /**
     * Default constructor provided for auto-instantiation.
     */
    public EntityManagerFactoryImpl() {
    }

    /**
     * Supply delegate on construction.
     */
    public EntityManagerFactoryImpl(BrokerFactory factory) {
        setBrokerFactory(factory);
    }

    /**
     * Delegate.
     */
    public BrokerFactory getBrokerFactory() {
        return _factory.getDelegate();
    }

    /**
     * Delegate must be provided before use.
     */
    public void setBrokerFactory(BrokerFactory factory) {
        _factory = new DelegatingBrokerFactory(factory,
            PersistenceExceptions.TRANSLATOR);
    }

    public OpenJPAConfiguration getConfiguration() {
        return _factory.getConfiguration();
    }
    
    /* 
     * @see javax.persistence.EntityManagerFactory#getProperties()
     * 
     * This does not return the password property.
     */
    public Map getProperties() {
        Map properties = _factory.getAllProperties();
        
        // Remove the password property
        properties.remove("javax.persistence.jdbc.password");
        properties.remove("openjpa.ConnectionPassword");
        
        return properties;
    }

    public Object putUserObject(Object key, Object val) {
        return _factory.putUserObject(key, val);
    }

    public Object getUserObject(Object key) {
        return _factory.getUserObject(key);
    }

    public StoreCache getStoreCache() {
        _factory.lock();
        try {
            if (_cache == null) {
                OpenJPAConfiguration conf = _factory.getConfiguration();
                _cache = new StoreCacheImpl(this,
                    conf.getDataCacheManagerInstance().getSystemDataCache());
            }
            return _cache;
        } finally {
            _factory.unlock();
        }
    }

    public StoreCache getStoreCache(String cacheName) {
        return new StoreCacheImpl(this, _factory.getConfiguration().
            getDataCacheManagerInstance().getDataCache(cacheName, true));
    }

    public QueryResultCache getQueryResultCache() {
        _factory.lock();
        try {
            if (_queryCache == null)
                _queryCache = new QueryResultCacheImpl(_factory.
                    getConfiguration().getDataCacheManagerInstance().
                    getSystemQueryCache());
            return _queryCache;
        } finally {
            _factory.unlock();
        }
    }

    public OpenJPAEntityManagerSPI createEntityManager() {
        return createEntityManager(null);
    }

    public OpenJPAEntityManagerSPI createEntityManager(Map props) {
        if (props == null)
            props = Collections.EMPTY_MAP;
        else if (!props.isEmpty())
            props = new HashMap(props);

        OpenJPAConfiguration conf = getConfiguration();
        String user = (String) Configurations.removeProperty
            ("ConnectionUserName", props);
        if (user == null)
            user = conf.getConnectionUserName();
        String pass = (String) Configurations.removeProperty
            ("ConnectionPassword", props);
        if (pass == null)
            pass = conf.getConnectionPassword();

        String str = (String) Configurations.removeProperty
            ("TransactionMode", props);
        boolean managed;
        if (str == null)
            managed = conf.isTransactionModeManaged();
        else {
            Value val = conf.getValue("TransactionMode");
            managed = Boolean.parseBoolean(val.unalias(str));
        }

        Object obj = Configurations.removeProperty("ConnectionRetainMode", 
            props);
        int retainMode;
        if (obj instanceof Number)
            retainMode = ((Number) obj).intValue();
        else if (obj == null)
            retainMode = conf.getConnectionRetainModeConstant();
        else {
            Value val = conf.getValue("ConnectionRetainMode");
            try {
                retainMode = Integer.parseInt(val.unalias((String) obj));
            } catch (Exception e) {
                throw new ArgumentException(_loc.get("bad-em-prop",
                    "openjpa.ConnectionRetainMode", obj),
                    new Throwable[]{ e }, obj, true);
            }
        }

        Broker broker = _factory.newBroker(user, pass, managed, retainMode,
            false);
            
        // add autodetach for close and rollback conditions to the configuration
        broker.setAutoDetach(AutoDetach.DETACH_CLOSE, true);
        broker.setAutoDetach(AutoDetach.DETACH_ROLLBACK, true);
        
        broker.setDetachedNew(false);
        OpenJPAEntityManagerSPI em = newEntityManagerImpl(broker);

        // allow setting of other bean properties of EM
        String[] prefixes = ProductDerivations.getConfigurationPrefixes();
        List<RuntimeException> errs = null;
        Method setter;
        String prop, prefix;
        Object val;
        for (Map.Entry entry : (Set<Map.Entry>) props.entrySet()) {
            prop = (String) entry.getKey();
            prefix = null;
            for (int i = 0; i < prefixes.length; i++) {
                prefix = prefixes[i] + ".";
                if (prop.startsWith(prefix))
                    break;
                prefix = null; 
            } 
            if (prefix == null)
                continue; 
            prop = prop.substring(prefix.length());
            try {
                setter = Reflection.findSetter(em.getClass(), prop, true);
            } catch (OpenJPAException ke) {
                if (errs == null)
                    errs = new LinkedList<RuntimeException>();
                errs.add(PersistenceExceptions.toPersistenceException(ke));
                continue;
            }

            val = entry.getValue();
            try {
                if (val instanceof String) {
                    if ("null".equals(val))
                        val = null;
                    else
                        val = Strings.parse((String) val,
                            setter.getParameterTypes()[0]);
                }
                Reflection.set(em, setter, val);
            } catch (Throwable t) {
                while (t.getCause() != null)
                    t = t.getCause();
                ArgumentException err = new ArgumentException(_loc.get
                    ("bad-em-prop", prop, entry.getValue()),
                    new Throwable[]{ t }, null, true);
                if (errs == null)
                    errs = new LinkedList<RuntimeException>();
                errs.add(err);
            }
        }

        if (errs != null) {
            em.close();
            if (errs.size() == 1)
                throw errs.get(0);
            throw new ArgumentException(_loc.get("bad-em-props"),
                errs.toArray(new Throwable[errs.size()]),
                null, true);
        }
        return em;
    }

    /**
     * Create a new entity manager around the given broker.
     */
    protected EntityManagerImpl newEntityManagerImpl(Broker broker) {
        return new EntityManagerImpl(this, broker);
    }

    public void addLifecycleListener(Object listener, Class... classes) {
        _factory.addLifecycleListener(listener, classes);
    }

    public void removeLifecycleListener(Object listener) {
        _factory.removeLifecycleListener(listener);
    }

    public void addTransactionListener(Object listener) {
        _factory.addTransactionListener(listener);
    }

    public void removeTransactionListener(Object listener) {
        _factory.removeTransactionListener(listener);
    }

    public void close() {
        _factory.close();
    }

    public boolean isOpen() {
        return !_factory.isClosed();
    }

    public int hashCode() {
        return _factory.hashCode();
    }

    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof EntityManagerFactoryImpl))
            return false;
        return _factory.equals(((EntityManagerFactoryImpl) other)._factory);
    }

    /**
     * Create a store-specific facade for the given fetch configuration.
	 * If no facade class exists, we use the default {@link FetchPlan}.
     */
    FetchPlan toFetchPlan(Broker broker, FetchConfiguration fetch) {
        if (fetch == null)
            return null;

        if (fetch instanceof DelegatingFetchConfiguration)
            fetch = ((DelegatingFetchConfiguration) fetch).
                getInnermostDelegate();

        try {
            if (_plan == null) {
                Class storeType = (broker == null) ? null : broker.
                    getStoreManager().getInnermostDelegate().getClass();
                Class cls = _factory.getConfiguration().
                    getStoreFacadeTypeRegistry().
                    getImplementation(FetchPlan.class, storeType, 
                    		FetchPlanImpl.class);
                _plan = cls.getConstructor(FetchConfiguration.class);
            }
            return _plan.newInstance(fetch);
        } catch (InvocationTargetException ite) {
            throw PersistenceExceptions.toPersistenceException
                (ite.getTargetException());
        } catch (Exception e) {
            throw PersistenceExceptions.toPersistenceException(e);
        }
	}

    public Cache getCache() {
        return getStoreCache();
    }

    public QueryBuilder getQueryBuilder() {
        return new CriteriaBuilder().setMetaModel(getMetamodel());
    }
    
    public OpenJPAQueryBuilder getDynamicQueryBuilder() {
        return new QueryBuilderImpl(this);
    }

    public Set<String> getSupportedProperties() {
        return _factory.getSupportedProperties();
    }

    public MetamodelImpl getMetamodel() {
        if (_metaModel == null) {
            _metaModel = new MetamodelImpl(getConfiguration()
                .getMetaDataRepositoryInstance());
        }
        return _metaModel;
    }

    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return this;
    }

    /**
     * Get the identifier for the specified entity.  If not managed by any
     * of the em's in this PU or not persistence capable, return null.
     */
    public Object getIdentifier(Object entity) {
        return OpenJPAPersistenceUtil.getIdentifier(this, entity);
    }

    public boolean isLoaded(Object entity) {
        return isLoaded(entity, null);
    }

    public boolean isLoaded(Object entity, String attribute) {
        if (entity == null) {
            return false;
        }
        
        return (OpenJPAPersistenceUtil.isLoaded(this, entity, attribute) ==
            LoadState.LOADED);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14067.java