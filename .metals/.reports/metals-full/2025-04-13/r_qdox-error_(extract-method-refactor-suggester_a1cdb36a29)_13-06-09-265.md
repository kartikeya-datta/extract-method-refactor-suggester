error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12667.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12667.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12667.java
text:
```scala
c@@ache.remove(getLocalAddress(cache));

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
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.jboss.as.clustering.infinispan.invoker.BatchCacheInvoker;
import org.jboss.as.clustering.infinispan.invoker.CacheInvoker;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.Value;

/**
 * @author Paul Ferraro
 */
@org.infinispan.notifications.Listener(sync = false)
public class RegistryService<K, V> implements Service<Registry<K, V>>, Registry<K, V> {

    static final Address LOCAL_ADDRESS = new Address() {};

    private final CacheInvoker invoker = new BatchCacheInvoker();
    private final Value<Cache<Address, Map.Entry<K, V>>> cache;
    private final Value<RegistryEntryProvider<K, V>> provider;
    private final Set<Listener<K, V>> listeners = new CopyOnWriteArraySet<Listener<K, V>>();

    public RegistryService(Value<Cache<Address, Map.Entry<K, V>>> cache, Value<RegistryEntryProvider<K, V>> provider) {
        this.cache = cache;
        this.provider = provider;
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
        return this.cache.getValue().getCacheManager().getClusterName();
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
        for (Map.Entry<K, V> entry: this.cache.getValue().values()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Map.Entry<K, V> getLocalEntry() {
        Cache<Address, Map.Entry<K, V>> cache = this.cache.getValue();
        return cache.get(getLocalAddress(cache));
    }

    @Override
    public Map.Entry<K, V> getRemoteEntry(Object address) {
        return this.cache.getValue().get(address);
    }

    @Override
    public void start(StartContext context) {
        this.refreshLocalEntry();
        Cache<Address, Map.Entry<K, V>> cache = this.cache.getValue();
        cache.getCacheManager().addListener(this);
        cache.addListener(this);
    }

    @Override
    public Map.Entry<K, V> refreshLocalEntry() {
        final Map.Entry<K, V> entry = this.createLocalCacheEntry();
        if (entry != null) {
            Operation<Void> operation = new Operation<Void>() {
                @Override
                public Void invoke(Cache<Address, Map.Entry<K, V>> cache) {
                    RegistryService.this.addLocalCacheEntry(cache, entry);
                    return null;
                }
            };
            this.invoker.invoke(this.cache.getValue(), operation);
        }
        return entry;
    }

    void addLocalCacheEntry(Cache<Address, Map.Entry<K, V>> cache, Map.Entry<K, V> entry) {
        if (entry != null) {
            cache.getAdvancedCache().withFlags(Flag.SKIP_REMOTE_LOOKUP).put(getLocalAddress(cache), entry);
        }
    }

    Map.Entry<K, V> createLocalCacheEntry() {
        RegistryEntryProvider<K, V> provider = this.provider.getValue();
        K key = provider.getKey();
        return (key != null) ? new AbstractMap.SimpleImmutableEntry<K, V>(key, provider.getValue()) : null;
    }

    @Override
    public void stop(StopContext context) {
        Cache<Address, Map.Entry<K, V>> cache = this.cache.getValue();
        cache.removeListener(this);
        cache.getCacheManager().removeListener(this);
        Operation<Void> operation = new Operation<Void>() {
            @Override
            public Void invoke(Cache<Address, Map.Entry<K, V>> cache) {
                cache.removeAsync(getLocalAddress(cache));
                return null;
            }
        };
        // Add SKIP_LOCKING flag to so that we aren't blocked by state transfer lock
        this.invoker.invoke(this.cache.getValue(), operation, Flag.SKIP_REMOTE_LOOKUP, Flag.SKIP_LOCKING);
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
                        Map.Entry<K, V> old = cache.getAdvancedCache().withFlags(Flag.CACHE_MODE_LOCAL).remove(member);
                        if (old != null) {
                            removed.add(old.getKey());
                        }
                    }
                }
                // Restore our entry in cache if we are joining (result of a split/merge)
                if (event.isMergeView()) {
                    RegistryService.this.addLocalCacheEntry(cache, RegistryService.this.createLocalCacheEntry());
                }
                return removed;
            }
        };

        Set<K> removed = this.invoker.invoke(this.cache.getValue(), operation);
        if (!removed.isEmpty()) {
            for (Listener<K, V> listener: this.listeners) {
                listener.removedEntries(removed);
            }
        }
    }

    // Yes, this could be static - but it references instance types
    private ThreadLocal<Map.Entry<K, V>> entry = new ThreadLocal<Map.Entry<K, V>>();

    @CacheEntryModified
    public void modified(CacheEntryModifiedEvent<Address, Map.Entry<K, V>> event) {
        if (event.isOriginLocal()) return;
        if (event.isPre()) {
            this.entry.set(event.getValue());
        } else {
            Map.Entry<K, V> old = this.entry.get();
            this.entry.remove();
            if (!this.listeners.isEmpty()) {
                Map.Entry<K, V> entry = event.getValue();
                if (entry != null) {
                    Map<K, V> entries = Collections.singletonMap(entry.getKey(), entry.getValue());
                    for (Listener<K, V> listener: this.listeners) {
                        if (old == null) {
                            listener.addedEntries(entries);
                        } else {
                            listener.updatedEntries(entries);
                        }
                    }
                }
            }
        }
    }

    @CacheEntryRemoved
    public void removed(CacheEntryRemovedEvent<Address, Map.Entry<K, V>> event) {
        if (event.isOriginLocal()) return;
        if (event.isPre()) {
            this.entry.set(event.getValue());
        } else {
            Map.Entry<K, V> entry = this.entry.get();
            this.entry.remove();
            if (entry != null) {
                Set<K> keys = Collections.singleton(entry.getKey());
                for (Listener<K, V> listener: this.listeners) {
                    listener.removedEntries(keys);
                }
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12667.java