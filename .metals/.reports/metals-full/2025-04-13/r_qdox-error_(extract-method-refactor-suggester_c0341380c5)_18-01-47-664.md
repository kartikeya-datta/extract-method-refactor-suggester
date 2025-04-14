error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5469.java
text:
```scala
t@@his.size = componentSettings.get("size", "10%");

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.indices.cache.filter;

import com.carrotsearch.hppc.ObjectOpenHashSet;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.lucene.search.DocIdSet;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.MemorySizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.ConcurrentCollections;
import org.elasticsearch.common.util.concurrent.EsRejectedExecutionException;
import org.elasticsearch.index.cache.filter.weighted.WeightedFilterCache;
import org.elasticsearch.node.settings.NodeSettingsService;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class IndicesFilterCache extends AbstractComponent implements RemovalListener<WeightedFilterCache.FilterCacheKey, DocIdSet> {

    private final ThreadPool threadPool;
    private final CacheRecycler cacheRecycler;

    private Cache<WeightedFilterCache.FilterCacheKey, DocIdSet> cache;

    private volatile String size;
    private volatile long sizeInBytes;
    private volatile TimeValue expire;

    private final TimeValue cleanInterval;

    private final Set<Object> readersKeysToClean = ConcurrentCollections.newConcurrentSet();

    private volatile boolean closed;


    public static final String INDICES_CACHE_FILTER_SIZE = "indices.cache.filter.size";
    public static final String INDICES_CACHE_FILTER_EXPIRE = "indices.cache.filter.expire";

    class ApplySettings implements NodeSettingsService.Listener {
        @Override
        public void onRefreshSettings(Settings settings) {
            boolean replace = false;
            String size = settings.get(INDICES_CACHE_FILTER_SIZE, IndicesFilterCache.this.size);
            if (!size.equals(IndicesFilterCache.this.size)) {
                logger.info("updating [indices.cache.filter.size] from [{}] to [{}]", IndicesFilterCache.this.size, size);
                IndicesFilterCache.this.size = size;
                replace = true;
            }
            TimeValue expire = settings.getAsTime(INDICES_CACHE_FILTER_EXPIRE, IndicesFilterCache.this.expire);
            if (!Objects.equal(expire, IndicesFilterCache.this.expire)) {
                logger.info("updating [indices.cache.filter.expire] from [{}] to [{}]", IndicesFilterCache.this.expire, expire);
                IndicesFilterCache.this.expire = expire;
                replace = true;
            }
            if (replace) {
                Cache<WeightedFilterCache.FilterCacheKey, DocIdSet> oldCache = IndicesFilterCache.this.cache;
                computeSizeInBytes();
                buildCache();
                oldCache.invalidateAll();
            }
        }
    }

    @Inject
    public IndicesFilterCache(Settings settings, ThreadPool threadPool, CacheRecycler cacheRecycler, NodeSettingsService nodeSettingsService) {
        super(settings);
        this.threadPool = threadPool;
        this.cacheRecycler = cacheRecycler;
        this.size = componentSettings.get("size", "20%");
        this.expire = componentSettings.getAsTime("expire", null);
        this.cleanInterval = componentSettings.getAsTime("clean_interval", TimeValue.timeValueSeconds(60));
        computeSizeInBytes();
        buildCache();
        logger.debug("using [node] weighted filter cache with size [{}], actual_size [{}], expire [{}], clean_interval [{}]",
                size, new ByteSizeValue(sizeInBytes), expire, cleanInterval);

        nodeSettingsService.addListener(new ApplySettings());
        threadPool.schedule(cleanInterval, ThreadPool.Names.SAME, new ReaderCleaner());
    }

    private void buildCache() {
        CacheBuilder<WeightedFilterCache.FilterCacheKey, DocIdSet> cacheBuilder = CacheBuilder.newBuilder()
                .removalListener(this)
                .maximumWeight(sizeInBytes).weigher(new WeightedFilterCache.FilterCacheValueWeigher());

        // defaults to 4, but this is a busy map for all indices, increase it a bit
        cacheBuilder.concurrencyLevel(16);

        if (expire != null) {
            cacheBuilder.expireAfterAccess(expire.millis(), TimeUnit.MILLISECONDS);
        }

        cache = cacheBuilder.build();
    }

    private void computeSizeInBytes() {
        this.sizeInBytes = MemorySizeValue.parseBytesSizeValueOrHeapRatio(size).bytes();
    }

    public void addReaderKeyToClean(Object readerKey) {
        readersKeysToClean.add(readerKey);
    }

    public void close() {
        closed = true;
        cache.invalidateAll();
    }

    public Cache<WeightedFilterCache.FilterCacheKey, DocIdSet> cache() {
        return this.cache;
    }

    @Override
    public void onRemoval(RemovalNotification<WeightedFilterCache.FilterCacheKey, DocIdSet> removalNotification) {
        WeightedFilterCache.FilterCacheKey key = removalNotification.getKey();
        if (key == null) {
            return;
        }
        if (key.removalListener != null) {
            key.removalListener.onRemoval(removalNotification);
        }
    }

    /**
     * The reason we need this class is because we need to clean all the filters that are associated
     * with a reader. We don't want to do it every time a reader closes, since iterating over all the map
     * is expensive. There doesn't seem to be a nicer way to do it (and maintaining a list per reader
     * of the filters will cost more).
     */
    class ReaderCleaner implements Runnable {

        @Override
        public void run() {
            if (closed) {
                return;
            }
            if (readersKeysToClean.isEmpty()) {
                schedule();
                return;
            }
            try {
                threadPool.executor(ThreadPool.Names.GENERIC).execute(new Runnable() {
                    @Override
                    public void run() {
                        Recycler.V<ObjectOpenHashSet<Object>> keys = cacheRecycler.hashSet(-1);
                        try {
                            for (Iterator<Object> it = readersKeysToClean.iterator(); it.hasNext(); ) {
                                keys.v().add(it.next());
                                it.remove();
                            }
                            cache.cleanUp();
                            if (!keys.v().isEmpty()) {
                                for (Iterator<WeightedFilterCache.FilterCacheKey> it = cache.asMap().keySet().iterator(); it.hasNext(); ) {
                                    WeightedFilterCache.FilterCacheKey filterCacheKey = it.next();
                                    if (keys.v().contains(filterCacheKey.readerKey())) {
                                        // same as invalidate
                                        it.remove();
                                    }
                                }
                            }
                            schedule();
                        } finally {
                            keys.close();
                        }
                    }
                });
            } catch (EsRejectedExecutionException ex) {
                logger.debug("Can not run ReaderCleaner - execution rejected", ex);
            } 
        }
        
        private void schedule() {
            try {
                threadPool.schedule(cleanInterval, ThreadPool.Names.SAME, this);
            } catch (EsRejectedExecutionException ex) {
                logger.debug("Can not schedule ReaderCleaner - execution rejected", ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5469.java