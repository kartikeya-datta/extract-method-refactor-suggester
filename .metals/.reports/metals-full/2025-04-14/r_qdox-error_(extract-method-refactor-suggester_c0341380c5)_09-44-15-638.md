error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5053.java
text:
```scala
i@@nstance.activate(pk);

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

package org.jboss.as.ejb3.component.entity.entitycache;

import static org.jboss.as.ejb3.EjbMessages.MESSAGES;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ejb.NoSuchEntityException;

import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;

/**
 * @author John Bailey
 * @author <a href="wfink@redhat.com">Wolf-Dieter Fink</a>
 */
public class ReferenceCountingEntityCache implements ReadyEntityCache {
    private final ConcurrentMap<Object, CacheEntry> cache = new ConcurrentHashMap<Object, CacheEntry>();
    private final EntityBeanComponent component;

    public ReferenceCountingEntityCache(final EntityBeanComponent component) {
        this.component = component;
    }

    public synchronized void create(final EntityBeanComponentInstance instance) {
        final CacheEntry entry = realCreate(instance);
        entry.referenceCount.incrementAndGet();
    }

    private CacheEntry realCreate(final EntityBeanComponentInstance instance) {
        final CacheEntry cacheEntry = new CacheEntry(instance);
        final CacheEntry existing = cache.putIfAbsent(instance.getPrimaryKey(), cacheEntry);
        if (existing != null) {
            if (existing.instance.isRemoved()) {
                //this happens in an instance is removed and then re-added in the space of the same transaction
                existing.replacedInstance = instance;
            } else {
                throw MESSAGES.instanceAlreadyRegisteredForPK(instance.getPrimaryKey());
            }
        }
        return cacheEntry;
    }

    @Override
    public synchronized boolean contains(final Object key) {
        if(cache.containsKey(key)) {
            final CacheEntry cacheEntry = cache.get(key);
            return true;
        }
        return false;
    }
    @Override
    public synchronized boolean containsNotRemoved(final Object key) {
        if(cache.containsKey(key)) {
            final CacheEntry cacheEntry = cache.get(key);
            if (cacheEntry.replacedInstance != null) {
                return !cacheEntry.replacedInstance.isRemoved();
            } else {
                return !cacheEntry.instance.isRemoved();
            }
        }
        return false;
    }

    public synchronized EntityBeanComponentInstance get(final Object key) throws NoSuchEntityException {
        if (!cache.containsKey(key)) {
            final EntityBeanComponentInstance instance = createInstance(key);
            realCreate(instance);
        }
        final CacheEntry cacheEntry = cache.get(key);
        cacheEntry.referenceCount.incrementAndGet();
        if (cacheEntry.replacedInstance != null) {
            return cacheEntry.replacedInstance;
        } else {
            return cacheEntry.instance;
        }
    }
    public synchronized void release(final EntityBeanComponentInstance instance, boolean success) {
        if (instance.isDiscarded()) {
            return;
        }
        if (instance.getPrimaryKey() == null) return;  // TODO: Should this be an Exception
        final CacheEntry cacheEntry = cache.get(instance.getPrimaryKey());
        if (cacheEntry == null) {
            throw MESSAGES.entityBeanInstanceNotFoundInCache(instance);
        }
        if (cacheEntry.replacedInstance != null) {
            //this can happen if an entity is removed and a new entity with the same PK is added in a transactions
            if (instance == cacheEntry.replacedInstance) {
                if (success) {
                    cacheEntry.instance = cacheEntry.replacedInstance;
                } else if (cacheEntry.instance.isDiscarded()) {
                    //if the TX was a failure, and the previous instance has been discarded
                    //we just remove the entry and return
                    cache.remove(instance.getPrimaryKey());
                    return;
                }
                cacheEntry.replacedInstance = null;
            }
        }
        //TODO: this should probably be somewhere else
        //roll back unsuccessful removal
        if (!success && instance.isRemoved()) {
            instance.setRemoved(false);
        }
        if (cacheEntry.referenceCount.decrementAndGet() == 0) {
            final Object pk = instance.getPrimaryKey();
            try {
                instance.passivate();
                component.releaseEntityBeanInstance(instance);
            } finally {
                cache.remove(pk);
            }
        }
    }

    public synchronized void discard(final EntityBeanComponentInstance instance) {
        if(instance.getPrimaryKey() == null) {
            //instance has already been passivated
            return;
        }
        final CacheEntry entry = cache.get(instance.getPrimaryKey());
        if (entry != null) {
            if (instance == entry.replacedInstance) {
                //this instance that is being discarded is the new instance
                //we can just set it to null
                entry.replacedInstance = null;
            } else if (entry.replacedInstance == null) {
                //if there is a new instance we cannot discard the entry entirely
                cache.remove(instance.getPrimaryKey());
            }
        }
    }

    public void start() {
    }

    public void stop() {
    }

    private EntityBeanComponentInstance createInstance(final Object pk) {
        final EntityBeanComponentInstance instance = component.acquireUnAssociatedInstance();
        instance.associate(pk);
        return instance;
    }

    private class CacheEntry {
        private final AtomicInteger referenceCount = new AtomicInteger(0);
        private volatile EntityBeanComponentInstance instance;
        private volatile EntityBeanComponentInstance replacedInstance;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5053.java