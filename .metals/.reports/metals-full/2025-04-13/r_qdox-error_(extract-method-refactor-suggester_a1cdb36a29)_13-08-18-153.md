error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14908.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14908.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14908.java
text:
```scala
M@@ap.Entry<K, V> old = cache.getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL).remove(member);

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

package org.jboss.as.clustering.registry;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.infinispan.Cache;
import org.infinispan.context.Flag;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.jboss.as.clustering.AsynchronousService;
import org.jboss.as.clustering.infinispan.invoker.BatchOperation;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;

/**
 * @author Paul Ferraro
 */
@org.infinispan.notifications.Listener(sync = false)
public class RegistryService<K, V> extends AsynchronousService<Registry<K, V>> implements Registry<K, V> {

    static final Address LOCAL_ADDRESS = new Address() {};

    @SuppressWarnings("rawtypes")
    private final InjectedValue<Cache> cacheRef = new InjectedValue<Cache>();
    private final RegistryEntryProvider<K, V> provider;
    private final Set<Listener<K, V>> listeners = new CopyOnWriteArraySet<Listener<K, V>>();
    private volatile Cache<Address, Map.Entry<K, V>> cache;

    public RegistryService(RegistryEntryProvider<K, V> provider) {
        this.provider = provider;
    }

    public ServiceBuilder<Registry<K, V>> build(ServiceTarget target, ServiceName serviceName, ServiceName cacheServiceName) {
        return target.addService(serviceName, this).addDependency(cacheServiceName, Cache.class, this.cacheRef);
    }

    @SuppressWarnings("rawtypes")
    public Injector<Cache> getCacheInjector() {
        return this.cacheRef;
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.msc.value.Value#getValue()
     */
    @Override
    public Registry<K, V> getValue() {
        return this;
    }

    @Override
    public String getName() {
        return this.cache.getCacheManager().getClusterName();
    }

    @Override
    public void addListener(Listener<K, V> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(Listener<K, V> listener) {
        this.listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     * @see org.jboss.as.clustering.registry.Registry#getEntries()
     */
    @Override
    public Map<K, V> getEntries() {
        Map<K, V> map = new HashMap<K, V>();
        for (Map.Entry<K, V> entry: this.cache.values()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Map.Entry<K, V> getLocalEntry() {
        return this.cache.get(getLocalAddress(this.cache));
    }

    @Override
    public Map.Entry<K, V> getRemoteEntry(Object address) {
        return this.cache.get(address);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void start() {
        this.cache = this.cacheRef.getValue();
        this.refreshLocalEntry();
        this.cache.getCacheManager().addListener(this);
        this.cache.addListener(this);
    }

    @Override
    public void refreshLocalEntry() {
        Operation<Void> operation = new Operation<Void>() {
            @Override
            public Void invoke(Cache<Address, Map.Entry<K, V>> cache) {
                RegistryService.this.addCacheEntry(cache);
                return null;
            }
        };
        this.invoke(operation);
    }

    void addCacheEntry(Cache<Address, Map.Entry<K, V>> cache) {
        K key = this.provider.getKey();
        if (key != null) {
            cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP).put(getLocalAddress(cache), new AbstractMap.SimpleImmutableEntry<K, V>(this.provider.getKey(), this.provider.getValue()));
        }
    }

    @Override
    protected void stop() {
        if (this.cache != null) {
            this.cache.removeListener(this);
            this.cache.getCacheManager().removeListener(this);
            Operation<Void> operation = new Operation<Void>() {
                @Override
                public Void invoke(Cache<Address, Map.Entry<K, V>> cache) {
                    cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP).remove(getLocalAddress(cache));
                    return null;
                }
            };
            this.invoke(operation);
        }
    }

    static Address getLocalAddress(Cache<?, ?> cache) {
        Address address = cache.getCacheManager().getAddress();
        return (address != null) ? address : LOCAL_ADDRESS;
    }

    @ViewChanged
    public void viewChanged(final ViewChangedEvent event) {
        Operation<Set<K>> operation = new Operation<Set<K>>() {
            @Override
            public Set<K> invoke(Cache<Address, Map.Entry<K, V>> cache) {
                Collection<Address> oldMembers = event.getOldMembers();
                Collection<Address> newMembers = event.getNewMembers();
                Set<K> removed = new HashSet<K>();
                // Remove entry of crashed member
                for (Address member: oldMembers) {
                    if (!newMembers.contains(member)) {
                        Map.Entry<K, V> old = cache.remove(member);
                        if (old != null) {
                            removed.add(old.getKey());
                        }
                    }
                }
                // Restore our entry in cache if we are joining (result of a split/merge)
                Address localAddress = event.getLocalAddress();
                if (!oldMembers.contains(localAddress) && newMembers.contains(localAddress)) {
                    RegistryService.this.addCacheEntry(cache);
                }
                return removed;
            }
        };

        Set<K> removed = this.invoke(operation);
        if (!removed.isEmpty()) {
            for (Listener<K, V> listener: this.listeners) {
                listener.removedEntries(removed);
            }
        }
    }

    @CacheEntryCreated
    public void created(CacheEntryCreatedEvent<Address, Map.Entry<K, V>> event) {
        if (event.isPre() || event.isOriginLocal()) return;
        if (!this.listeners.isEmpty()) {
            Map.Entry<K, V> entry = event.getCache().get(event.getKey());
            if (entry != null) {
                for (Listener<K, V> listener: this.listeners) {
                    listener.addedEntries(Collections.singletonMap(entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    @CacheEntryModified
    public void modified(CacheEntryModifiedEvent<Address, Map.Entry<K, V>> event) {
        if (event.isPre() || event.isOriginLocal()) return;
        if (!this.listeners.isEmpty()) {
            Map.Entry<K, V> entry = event.getCache().get(event.getKey());
            if (entry != null) {
                for (Listener<K, V> listener: this.listeners) {
                    listener.updatedEntries(Collections.singletonMap(entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    @CacheEntryRemoved
    public void removed(CacheEntryRemovedEvent<Address, Map.Entry<K, V>> event) {
        // Need to run prior to removal, so the cache entry is available
        if (!event.isPre() || event.isOriginLocal()) return;
        Map.Entry<K, V> entry = event.getValue();
        if (entry != null) {
            for (Listener<K, V> listener: this.listeners) {
                listener.removedEntries(Collections.singleton(entry.getKey()));
            }
        }
    }

    <R> R invoke(Operation<R> operation) {
        return new BatchOperation<Address, Map.Entry<K, V>, R>(operation).invoke(this.cache);
    }

    abstract class Operation<R> implements CacheInvoker.Operation<Address, Map.Entry<K, V>, R> {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14908.java