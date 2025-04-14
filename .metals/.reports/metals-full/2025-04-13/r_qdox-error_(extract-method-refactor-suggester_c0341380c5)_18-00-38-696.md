error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5054.java
text:
```scala
i@@nstance.activate(pk);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.ejb3.component.entity.entitycache;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.NoSuchEJBException;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;

/**
 * Cache of entity bean component instances by transaction key
 *
 * @author Stuart Douglas
 * @author <a href="wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class TransactionLocalEntityCache implements ReadyEntityCache {
    private final TransactionSynchronizationRegistry transactionSynchronizationRegistry;
    private final ConcurrentMap<Object, Map<Object, CacheEntry>> cache = new ConcurrentHashMap<Object, Map<Object, CacheEntry>>(Runtime.getRuntime().availableProcessors());
    private final EntityBeanComponent component;

    public TransactionLocalEntityCache(final EntityBeanComponent component) {
        this.component = component;
        this.transactionSynchronizationRegistry = component.getTransactionSynchronizationRegistry();
    }

    @Override
    public synchronized boolean contains(final Object key) {
        if (!isTransactionActive() || !cache.containsKey(key)) {
            return false;
        }
        final Map<Object, CacheEntry> cache = prepareCache();

        return cache.containsKey(key);
    }

    @Override
    public synchronized boolean containsNotRemoved(final Object key) {
        if (!isTransactionActive() || !cache.containsKey(key)) {
            return false;
        }
        final Map<Object, CacheEntry> cache = prepareCache();

        return cache.containsKey(key) && !cache.get(key).instance.isRemoved();
    }

    @Override
    public EntityBeanComponentInstance get(final Object key) throws NoSuchEJBException {
        if (!isTransactionActive()) {
            return createInstance(key);
        }

        final Map<Object, CacheEntry> cache = prepareCache();
        if (!cache.containsKey(key)) {
            final EntityBeanComponentInstance instance = createInstance(key);
            realCreate(instance, false);
        }
        final CacheEntry cacheEntry = cache.get(key);
        cacheEntry.referenceCount.incrementAndGet();
        return cacheEntry.instance;
    }

    @Override
    public void discard(final EntityBeanComponentInstance instance) {
        if (isTransactionActive()) {
            final Object key = transactionSynchronizationRegistry.getTransactionKey();
            final Map<Object, CacheEntry> map = cache.get(key);
            if (map != null) {
                map.remove(instance.getPrimaryKey());
            }
        }
    }

    @Override
    public void create(final EntityBeanComponentInstance instance) throws NoSuchEJBException {
        realCreate(instance, true);
    }

    private void realCreate(final EntityBeanComponentInstance instance, boolean incRefCount) throws NoSuchEJBException {
        if (isTransactionActive()) {
            final Map<Object, CacheEntry> map = prepareCache();
            final CacheEntry cacheEntry = new CacheEntry(instance);
            map.put(instance.getPrimaryKey(), cacheEntry);
            if(incRefCount) {
                cacheEntry.referenceCount.incrementAndGet();
            }
        }
    }

    @Override
    public void release(final EntityBeanComponentInstance instance, boolean success) {

        if (instance.isDiscarded()) {
            return;
        }
        if (instance.getPrimaryKey() == null) {
            return;
        }

        if (!success && instance.isRemoved()) {
            instance.setRemoved(false);
        }
        final Object key = transactionSynchronizationRegistry.getTransactionKey();
        if (key == null) {
            instance.passivate();
            component.releaseEntityBeanInstance(instance);
            return;
        }

        final Map<Object, CacheEntry> map = cache.get(key);
        if (map != null) {
            final CacheEntry cacheEntry = map.get(instance.getPrimaryKey());
            if (cacheEntry == null) {
                throw MESSAGES.entityBeanInstanceNotFoundInCache(instance);
            }
            if (cacheEntry.referenceCount.decrementAndGet() <= 0) {
                final Object pk = instance.getPrimaryKey();
                try {
                    instance.passivate();
                    component.releaseEntityBeanInstance(instance);
                } finally {
                    map.remove(pk);
                }
            }
        }
    }

    public void reference(EntityBeanComponentInstance instance) {
        final Map<Object, CacheEntry> cache = prepareCache();
        final CacheEntry cacheEntry = cache.get(instance.getPrimaryKey());
        if (cacheEntry == null) {
            throw MESSAGES.entityBeanInstanceNotFoundInCache(instance);
        }
        cacheEntry.referenceCount.incrementAndGet();
    }

    @Override
    public synchronized void start() {

    }

    @Override
    public synchronized void stop() {
    }

    private Map<Object, CacheEntry> prepareCache() {
        final Object key = transactionSynchronizationRegistry.getTransactionKey();
        Map<Object, CacheEntry> map = cache.get(key);
        if (map != null) {
            return map;
        }
        map = Collections.synchronizedMap(new HashMap<Object, CacheEntry>());
        final Map<Object, CacheEntry> existing = cache.putIfAbsent(key, map);
        if (existing != null) {
            map = existing;
        }
        transactionSynchronizationRegistry.registerInterposedSynchronization(new Synchronization() {
            @Override
            public void beforeCompletion() {

            }

            @Override
            public void afterCompletion(final int status) {
                cache.remove(key);
            }
        });
        return map;
    }

    private EntityBeanComponentInstance createInstance(Object pk) {
        final EntityBeanComponentInstance instance = component.acquireUnAssociatedInstance();
        instance.associate(pk);
        return instance;
    }

    private boolean isTransactionActive() {
        return transactionSynchronizationRegistry.getTransactionKey() != null;
    }

    private class CacheEntry {
        private final AtomicInteger referenceCount = new AtomicInteger(0);
        private final EntityBeanComponentInstance instance;

        private CacheEntry(EntityBeanComponentInstance instance) {
            this.instance = instance;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5054.java