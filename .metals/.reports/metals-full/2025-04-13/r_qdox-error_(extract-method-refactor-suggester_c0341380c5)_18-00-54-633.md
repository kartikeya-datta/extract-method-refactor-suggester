error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8431.java
text:
```scala
m@@odules.add(new IndexSettingsModule(index, indexSettings));

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

package org.elasticsearch.indices;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.IndexReaderPurgedListener;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.ElasticSearchIllegalStateException;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.collect.ImmutableSet;
import org.elasticsearch.common.collect.UnmodifiableIterator;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.Injectors;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.io.FileSystemUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.util.concurrent.ThreadSafe;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.index.*;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.AnalysisService;
import org.elasticsearch.index.cache.CacheStats;
import org.elasticsearch.index.cache.IndexCache;
import org.elasticsearch.index.cache.IndexCacheModule;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.IndexEngine;
import org.elasticsearch.index.engine.IndexEngineModule;
import org.elasticsearch.index.gateway.IndexGateway;
import org.elasticsearch.index.gateway.IndexGatewayModule;
import org.elasticsearch.index.mapper.MapperServiceModule;
import org.elasticsearch.index.merge.MergeStats;
import org.elasticsearch.index.percolator.PercolatorModule;
import org.elasticsearch.index.percolator.PercolatorService;
import org.elasticsearch.index.query.IndexQueryParserModule;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.service.InternalIndexService;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.similarity.SimilarityModule;
import org.elasticsearch.index.store.IndexStoreModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.elasticsearch.indices.store.IndicesStore;
import org.elasticsearch.plugins.IndexPluginsModule;
import org.elasticsearch.plugins.PluginsService;
import org.elasticsearch.threadpool.ThreadPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.elasticsearch.cluster.metadata.IndexMetaData.*;
import static org.elasticsearch.common.collect.MapBuilder.*;
import static org.elasticsearch.common.collect.Maps.*;
import static org.elasticsearch.common.collect.Sets.*;
import static org.elasticsearch.common.settings.ImmutableSettings.*;

/**
 * @author kimchy (shay.banon)
 */
@ThreadSafe
public class InternalIndicesService extends AbstractLifecycleComponent<IndicesService> implements IndicesService {

    private final NodeEnvironment nodeEnv;

    private final ThreadPool threadPool;

    private final InternalIndicesLifecycle indicesLifecycle;

    private final IndicesAnalysisService indicesAnalysisService;

    private final IndicesStore indicesStore;

    private final Injector injector;

    private final PluginsService pluginsService;

    private final Map<String, Injector> indicesInjectors = new HashMap<String, Injector>();

    private volatile ImmutableMap<String, IndexService> indices = ImmutableMap.of();

    @Inject public InternalIndicesService(Settings settings, NodeEnvironment nodeEnv, ThreadPool threadPool, IndicesLifecycle indicesLifecycle, IndicesAnalysisService indicesAnalysisService, IndicesStore indicesStore, Injector injector) {
        super(settings);
        this.nodeEnv = nodeEnv;
        this.threadPool = threadPool;
        this.indicesLifecycle = (InternalIndicesLifecycle) indicesLifecycle;
        this.indicesAnalysisService = indicesAnalysisService;
        this.indicesStore = indicesStore;
        this.injector = injector;

        this.pluginsService = injector.getInstance(PluginsService.class);

        try {
            FieldCache.DEFAULT.getClass().getMethod("setIndexReaderPurgedListener", IndexReaderPurgedListener.class);
            // LUCENE MONITOR - This is a hack to eagerly clean caches based on index reader
            FieldCache.DEFAULT.setIndexReaderPurgedListener(new CacheReaderPurgeListener(this));
            logger.trace("eager reader based cache eviction enabled");
        } catch (NoSuchMethodException e) {
            // no method
            logger.warn("lucene default FieldCache is used, not enabling eager reader based cache eviction");
        }
    }

    @Override protected void doStart() throws ElasticSearchException {
    }

    @Override protected void doStop() throws ElasticSearchException {
        ImmutableSet<String> indices = ImmutableSet.copyOf(this.indices.keySet());
        final CountDownLatch latch = new CountDownLatch(indices.size());
        for (final String index : indices) {
            threadPool.cached().execute(new Runnable() {
                @Override public void run() {
                    try {
                        deleteIndex(index, false, "shutdown");
                    } catch (Exception e) {
                        logger.warn("failed to delete index on stop [" + index + "]", e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            // ignore
        }
    }

    @Override protected void doClose() throws ElasticSearchException {
        indicesStore.close();
        indicesAnalysisService.close();
    }

    @Override public IndicesLifecycle indicesLifecycle() {
        return this.indicesLifecycle;
    }

    @Override public NodeIndicesStats stats() {
        long storeTotalSize = 0;
        long numberOfDocs = 0;
        CacheStats cacheStats = new CacheStats();
        MergeStats mergeStats = new MergeStats();
        for (IndexService indexService : indices.values()) {
            for (IndexShard indexShard : indexService) {
                try {
                    storeTotalSize += ((InternalIndexShard) indexShard).store().estimateSize().bytes();
                } catch (IOException e) {
                    // ignore
                }

                if (indexShard.state() == IndexShardState.STARTED) {
                    Engine.Searcher searcher = indexShard.searcher();
                    try {
                        numberOfDocs += searcher.reader().numDocs();
                    } finally {
                        searcher.release();
                    }
                }
                mergeStats.add(((InternalIndexShard) indexShard).mergeScheduler().stats());
            }
            cacheStats.add(indexService.cache().stats());
        }
        return new NodeIndicesStats(new ByteSizeValue(storeTotalSize), numberOfDocs, cacheStats, mergeStats);
    }

    /**
     * Returns <tt>true</tt> if changes (adding / removing) indices, shards and so on are allowed.
     */
    public boolean changesAllowed() {
        // we check on stop here since we defined stop when we delete the indices
        return lifecycle.started();
    }

    @Override public UnmodifiableIterator<IndexService> iterator() {
        return indices.values().iterator();
    }

    public boolean hasIndex(String index) {
        return indices.containsKey(index);
    }

    public Set<String> indices() {
        return newHashSet(indices.keySet());
    }

    public IndexService indexService(String index) {
        return indices.get(index);
    }

    @Override public IndexService indexServiceSafe(String index) throws IndexMissingException {
        IndexService indexService = indexService(index);
        if (indexService == null) {
            throw new IndexMissingException(new Index(index));
        }
        return indexService;
    }

    public synchronized IndexService createIndex(String sIndexName, Settings settings, String localNodeId) throws ElasticSearchException {
        if (!lifecycle.started()) {
            throw new ElasticSearchIllegalStateException("Can't create an index [" + sIndexName + "], node is closed");
        }
        Index index = new Index(sIndexName);
        if (indicesInjectors.containsKey(index.name())) {
            throw new IndexAlreadyExistsException(index);
        }

        indicesLifecycle.beforeIndexCreated(index);

        logger.debug("creating Index [{}], shards [{}]/[{}]", sIndexName, settings.get(SETTING_NUMBER_OF_SHARDS), settings.get(SETTING_NUMBER_OF_REPLICAS));

        Settings indexSettings = settingsBuilder()
                .put("settingsType", "index")
                .put(this.settings)
                .put(settings)
                .classLoader(settings.getClassLoader())
                .build();

        ModulesBuilder modules = new ModulesBuilder();
        modules.add(new IndexNameModule(index));
        modules.add(new LocalNodeIdModule(localNodeId));
        modules.add(new IndexSettingsModule(indexSettings));
        modules.add(new IndexPluginsModule(indexSettings, pluginsService));
        modules.add(new IndexStoreModule(indexSettings));
        modules.add(new IndexEngineModule(indexSettings));
        modules.add(new AnalysisModule(indexSettings, indicesAnalysisService));
        modules.add(new SimilarityModule(indexSettings));
        modules.add(new IndexCacheModule(indexSettings));
        modules.add(new IndexQueryParserModule(indexSettings));
        modules.add(new MapperServiceModule());
        modules.add(new IndexGatewayModule(indexSettings, injector.getInstance(Gateway.class)));
        modules.add(new IndexModule());
        modules.add(new PercolatorModule());

        Injector indexInjector = modules.createChildInjector(injector);

        indicesInjectors.put(index.name(), indexInjector);

        IndexService indexService = indexInjector.getInstance(IndexService.class);

        indicesLifecycle.afterIndexCreated(indexService);

        indices = newMapBuilder(indices).put(index.name(), indexService).immutableMap();

        return indexService;
    }

    @Override public synchronized void cleanIndex(String index, String reason) throws ElasticSearchException {
        deleteIndex(index, false, reason);
    }

    @Override public synchronized void deleteIndex(String index, String reason) throws ElasticSearchException {
        deleteIndex(index, true, reason);
    }

    private void deleteIndex(String index, boolean delete, String reason) throws ElasticSearchException {
        Injector indexInjector;
        IndexService indexService;
        synchronized (this) {
            indexInjector = indicesInjectors.remove(index);
            if (indexInjector == null) {
                if (!delete) {
                    return;
                }
                throw new IndexMissingException(new Index(index));
            }
            if (delete) {
                logger.debug("deleting Index [{}]", index);
            }

            Map<String, IndexService> tmpMap = newHashMap(indices);
            indexService = tmpMap.remove(index);
            indices = ImmutableMap.copyOf(tmpMap);
        }

        indicesLifecycle.beforeIndexClosed(indexService, delete);

        for (Class<? extends CloseableIndexComponent> closeable : pluginsService.indexServices()) {
            indexInjector.getInstance(closeable).close(delete);
        }

        ((InternalIndexService) indexService).close(delete, reason);

        indexInjector.getInstance(PercolatorService.class).close();
        indexInjector.getInstance(IndexCache.class).close();
        indexInjector.getInstance(AnalysisService.class).close();
        indexInjector.getInstance(IndexEngine.class).close();
        indexInjector.getInstance(IndexServiceManagement.class).close();

        indexInjector.getInstance(IndexGateway.class).close(delete);

        Injectors.close(injector);

        indicesLifecycle.afterIndexClosed(indexService.index(), delete);

        if (delete) {
            FileSystemUtils.deleteRecursively(nodeEnv.indexLocation(new Index(index)));
        }
    }

    private static class CacheReaderPurgeListener implements IndexReaderPurgedListener {

        private final IndicesService indicesService;

        private CacheReaderPurgeListener(IndicesService indicesService) {
            this.indicesService = indicesService;
        }

        @Override public void indexReaderPurged(IndexReader reader) {
            for (IndexService indexService : indicesService) {
                indexService.cache().clear(reader);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8431.java