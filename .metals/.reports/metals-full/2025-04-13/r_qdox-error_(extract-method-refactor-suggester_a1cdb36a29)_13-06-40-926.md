error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8059.java
text:
```scala
E@@ get(K key, boolean lock);

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

package org.jboss.as.ejb3.cache.spi;

import java.io.Serializable;

import org.jboss.as.ejb3.cache.Cacheable;
import org.jboss.as.ejb3.cache.Identifiable;
import org.jboss.as.ejb3.component.stateful.StatefulTimeoutInfo;

/**
 * An in-memory store for {@link BackingCacheEntry} instances that integrates a persistent store and the ability to use its
 * knowledge of when objects are accessed to coordinate the passivation and expiration of cached objects. Note that this class
 * does NOT call any callbacks; it performs passivation and expiration by invoking methods on the
 * {@link #setPassivatingCache(PassivatingBackingCache) injected backing cache}; the cache performs the actual passivation or
 * removal.
 *
 * @author Brian Stansberry
 * @author Paul Ferraro
 */
public interface BackingCacheEntryStore<K extends Serializable, V extends Cacheable<K>, E extends BackingCacheEntry<K, V>>
    extends GroupCompatibilityChecker {
    /**
     * Put a new entry into the store. This operation should only be performed once per entry.
     *
     * @param entry the object to store. Cannot be <code>null</code>.
     *
     * @throws IllegalStateException if the store is already managing an entry with the same {@link Identifiable#getId() id}. It
     *         is not a requirement that the store throw this exception in this case, but it is permissible. This basically puts
     *         the onus on callers to ensure this operation is only performed once per entry.
     */
    void insert(E entry);

    /**
     * Gets the entry with the given id from the store.
     *
     * @param key {@link Identifiable#getId() id} of the entry. Cannot be <code>null</code>.
     * @return the object store under <code>id</code>. May return <code>null</code>.
     */
    E get(K key);

    /**
     * Update an already cached item.
     *
     * @param entry the entry to update
     * @param modified was the entry modified since {@link #get(Object)} was called?
     *
     * @throws IllegalStateException if the store isn't already managing an entry with the same {@link Identifiable#getId() id}.
     *         It is not a requirement that the store throw this exception in this case, but it is permissible. This basically
     *         puts the onus on callers to ensure {@link #insert(E)} is invoked before the first replication.
     */
    void update(E entry, boolean modified);

    /**
     * Remove the object with the given key from the store.
     *
     * @param key {@link Identifiable#getId() id} of the entry. Cannot be <code>null</code>.
     *
     * @return the object that was cached under <code>key</code>
     */
    E remove(K key);

    /**
     * Remove the entry with the given key from any in-memory store while retaining it in the persistent store.
     *
     * @param entry the entry to passivate
     */
    void passivate(E entry);

    /**
     * Gets whether this store supports clustering functionality.
     *
     * @return <code>true</code> if clustering is supported, <code>false</code> otherwise
     */
    boolean isClustered();

    /**
     * Perform any initialization work.
     */
    void start();

    /**
     * Perform any shutdown work.
     */
    void stop();

    BackingCacheEntryStoreConfig getConfig();

    StatefulTimeoutInfo getTimeout();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8059.java