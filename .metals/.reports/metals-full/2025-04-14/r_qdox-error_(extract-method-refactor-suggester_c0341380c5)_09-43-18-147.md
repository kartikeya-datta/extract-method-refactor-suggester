error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14607.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14607.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14607.java
text:
```scala
c@@onf.getRemoteCommitEventManager().addInternalListener(this);

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
package org.apache.openjpa.datacache;

import org.apache.openjpa.event.RemoteCommitListener;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.CacheMap;

/**
 * A {@link DataCache} implementation that is optimized for concurrent
 * access. When the cache fills up, values to remove from cache are chosen
 * randomly. Due to race conditions, it is possible that a get call might not
 * return a cached instance if that instance is being transferred between
 * internal datastructures.
 *
 * @since 0.4.0
 */
public class ConcurrentDataCache
    extends AbstractDataCache
    implements RemoteCommitListener {

    private static final Localizer _loc = Localizer.forPackage
        (ConcurrentDataCache.class);

    private final CacheMap _cache = newCacheMap();

    /**
     * Returns the underlying {@link CacheMap} that this cache is using.
     * This is not an unmodifiable view on the map, so care should be taken
     * with this reference. Implementations should probably not modify the
     * contents of the cache, but should only use this reference to
     * obtain cache metrics.
     */
    public CacheMap getCacheMap() {
        return _cache;
    }

    /**
     * Sets the maximum number of unpinned objects to keep hard
     * references to. If the map contains more unpinned objects than
     * <code>size</code>, then this method will result in the cache
     * flushing old values.
     */
    public void setCacheSize(int size) {
        _cache.setCacheSize(size);
    }

    /**
     * Returns the maximum number of unpinned objects to keep hard
     * references to.
     */
    public int getCacheSize() {
        return _cache.getCacheSize();
    }

    /**
     * Sets the maximum number of unpinned objects to keep soft
     * references to. If the map contains more soft references than
     * <code>size</code>, then this method will result in the cache
     * flushing values.
     */
    public void setSoftReferenceSize(int size) {
        _cache.setSoftReferenceSize(size);
    }

    /**
     * Returns the maximum number of unpinned objects to keep soft
     * references to. Defaults to <code>-1</code>.
     */
    public int getSoftReferenceSize() {
        return _cache.getSoftReferenceSize();
    }

    public void initialize(DataCacheManager mgr) {
        super.initialize(mgr);
        conf.getRemoteCommitEventManager().addListener(this);
    }

    public void unpinAll(Class cls, boolean subs) {
        if (log.isWarnEnabled())
            log.warn(_loc.get("cache-class-unpin-all", getName()));
        unpinAll(_cache.getPinnedKeys());
    }

    public void writeLock() {
        _cache.writeLock();
    }

    public void writeUnlock() {
        _cache.writeUnlock();
    }

    /**
     * Return the map to use as an internal cache; entry expirations must
     * invoke {@link AbstractDataCache#keyRemoved}.
     */
    protected CacheMap newCacheMap() {
        return new CacheMap() {
            protected void entryRemoved(Object key, Object value,
                boolean expired) {
                keyRemoved(key, expired);
            }
        };
    }

    protected DataCachePCData getInternal(Object key) {
        return (DataCachePCData) _cache.get(key);
    }

    protected DataCachePCData putInternal(Object key, DataCachePCData pc) {
        return (DataCachePCData) _cache.put(key, pc);
    }

    protected DataCachePCData removeInternal(Object key) {
        return (DataCachePCData) _cache.remove(key);
    }

    protected void removeAllInternal(Class cls, boolean subs) {
        // we could keep a histogram of the counts of contained classes and
        // only clear if we have the class, but that still wouldn't support subs
        // well, would involve synching, and won't yield much benefit when we're
        // used as a primary cache
        _cache.clear();
    }

    protected void clearInternal() {
        _cache.clear();
    }

    protected boolean pinInternal(Object key) {
        return _cache.pin(key);
    }

    protected boolean unpinInternal(Object key) {
        return _cache.unpin (key);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14607.java