error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13669.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13669.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13669.java
text:
```scala
G@@roupAwareBackingCache<K, V, UUID, SerializationGroupMember<K, V, UUID>> backingCache = new GroupAwareBackingCacheImpl<K, V, UUID>(factory, container, groupCache, Executors.defaultThreadFactory());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.ejb3.cache.impl.factory;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.as.ejb3.cache.Cache;
import org.jboss.as.ejb3.cache.CacheFactory;
import org.jboss.as.ejb3.cache.Cacheable;
import org.jboss.as.ejb3.cache.PassivationManager;
import org.jboss.as.ejb3.cache.StatefulObjectFactory;
import org.jboss.as.ejb3.cache.impl.GroupAwareCache;
import org.jboss.as.ejb3.cache.impl.backing.GroupAwareBackingCacheImpl;
import org.jboss.as.ejb3.cache.impl.backing.PassivatingBackingCacheImpl;
import org.jboss.as.ejb3.cache.impl.backing.SerializationGroupContainer;
import org.jboss.as.ejb3.cache.impl.backing.SerializationGroupMemberContainer;
import org.jboss.as.ejb3.cache.spi.BackingCacheEntryStore;
import org.jboss.as.ejb3.cache.spi.BackingCacheEntryStoreSource;
import org.jboss.as.ejb3.cache.spi.BackingCacheLifecycleListener;
import org.jboss.as.ejb3.cache.spi.GroupAwareBackingCache;
import org.jboss.as.ejb3.cache.spi.PassivatingBackingCache;
import org.jboss.as.ejb3.cache.spi.SerializationGroup;
import org.jboss.as.ejb3.cache.spi.SerializationGroupMember;
import org.jboss.as.ejb3.component.stateful.StatefulTimeoutInfo;

/**
 * {@link CacheFactory} implementation that can return a group-aware cache. How the cache functions depends on the
 * behavior of the {@link BackingCacheEntryStore} implementations returned by the injected {@link BackingCacheEntryStoreSource}.
 *
 * @author Brian Stansberry
 * @author Paul Ferraro
 */
public class GroupAwareCacheFactory<K extends Serializable, V extends Cacheable<K>> implements CacheFactory<K, V>, BackingCacheLifecycleListener {

    private final AtomicReference<SerializationGroupContainer<K, V>> groupContainerRef = new AtomicReference<SerializationGroupContainer<K, V>>();
    private final AtomicInteger memberCounter = new AtomicInteger();
    private final BackingCacheEntryStoreSource<K, V, UUID> storeSource;

    public GroupAwareCacheFactory(BackingCacheEntryStoreSource<K, V, UUID> storeSource) {
        this.storeSource = storeSource;
    }

    // --------------------------------------------------- StatefulCacheFactory

    @Override
    public Cache<K, V> createCache(String beanName, StatefulObjectFactory<V> factory, PassivationManager<K, V> passivationManager, StatefulTimeoutInfo timeout) {

        // Create/find the cache for SerializationGroup that the container
        // may be associated with
        SerializationGroupContainer<K, V> groupContainer = this.groupContainerRef.get();
        if (groupContainer == null) {
            groupContainer = this.createGroupContainer(passivationManager, timeout);
            if (!this.groupContainerRef.compareAndSet(null, groupContainer)) {
                groupContainer = this.groupContainerRef.get();
            }
        }
        groupContainer.addMemberPassivationManager(passivationManager);
        PassivatingBackingCache<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>> groupCache = groupContainer.getGroupCache();

        SerializationGroupMemberContainer<K, V, UUID> container = new SerializationGroupMemberContainer<K, V, UUID>(passivationManager, groupCache, this.storeSource);

        // Create the store for SerializationGroupMembers from the container
        BackingCacheEntryStore<K, V, SerializationGroupMember<K, V, UUID>> store = storeSource.createIntegratedObjectStore(beanName, container, timeout);
        container.setBackingCacheEntryStore(store);

        // Set up the backing cache with the store and group cache
        GroupAwareBackingCache<K, V, UUID, SerializationGroupMember<K, V, UUID>> backingCache = new GroupAwareBackingCacheImpl<K, V, UUID>(factory, container, groupCache, Executors.newScheduledThreadPool(1, Executors.defaultThreadFactory()));

        // Listen for backing cache lifecycle changes so we know when to start/stop groupCache
        backingCache.addLifecycleListener(this);

        // Finally, the front-end cache
        return new GroupAwareCache<K, V, UUID, SerializationGroupMember<K, V, UUID>>(backingCache, true);
    }

    private SerializationGroupContainer<K, V> createGroupContainer(PassivationManager<K, V> passivationManager, StatefulTimeoutInfo timeout) {
        SerializationGroupContainer<K, V> container = new SerializationGroupContainer<K, V>(passivationManager);

        BackingCacheEntryStore<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>> store = storeSource.createGroupIntegratedObjectStore(container, timeout);

        PassivatingBackingCache<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>> groupCache = new PassivatingBackingCacheImpl<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>>(container, container, container, store);

        container.setGroupCache(groupCache);

        return container;
    }

    // TODO Make the group cache a service
    @Override
    public void lifecycleChange(LifecycleState newState) {
        switch (newState) {
            case STARTING: {
                if (this.memberCounter.incrementAndGet() == 1) {
                    PassivatingBackingCache<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>> groupCache = this.groupContainerRef.get().getGroupCache();
                    synchronized (groupCache) {
                        groupCache.start();
                    }
                }
                break;
            }
            case STOPPED: {
                if (this.memberCounter.decrementAndGet() == 0) {
                    PassivatingBackingCache<UUID, Cacheable<UUID>, SerializationGroup<K, V, UUID>> groupCache = this.groupContainerRef.get().getGroupCache();
                    synchronized (groupCache) {
                        groupCache.stop();
                    }
                }
                break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13669.java