error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8042.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8042.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8042.java
text:
```scala
E@@ngine.DeleteByQuery prepareDeleteByQuery(BytesReference source, @Nullable String[] filteringAliases, String... types) throws ElasticSearchException;

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
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

package org.elasticsearch.index.shard.service;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.index.cache.filter.FilterCacheStats;
import org.elasticsearch.index.cache.filter.ShardFilterCache;
import org.elasticsearch.index.cache.id.IdCacheStats;
import org.elasticsearch.index.cache.id.ShardIdCache;
import org.elasticsearch.index.deletionpolicy.SnapshotIndexCommit;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.engine.EngineException;
import org.elasticsearch.index.engine.SegmentsStats;
import org.elasticsearch.index.fielddata.FieldDataStats;
import org.elasticsearch.index.fielddata.IndexFieldDataService;
import org.elasticsearch.index.fielddata.ShardFieldData;
import org.elasticsearch.index.flush.FlushStats;
import org.elasticsearch.index.get.GetStats;
import org.elasticsearch.index.get.ShardGetService;
import org.elasticsearch.index.indexing.IndexingStats;
import org.elasticsearch.index.indexing.ShardIndexingService;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.mapper.ParsedDocument;
import org.elasticsearch.index.mapper.SourceToParse;
import org.elasticsearch.index.merge.MergeStats;
import org.elasticsearch.index.percolator.PercolatorQueriesRegistry;
import org.elasticsearch.index.percolator.stats.ShardPercolateService;
import org.elasticsearch.index.refresh.RefreshStats;
import org.elasticsearch.index.search.stats.SearchStats;
import org.elasticsearch.index.search.stats.ShardSearchService;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.DocsStats;
import org.elasticsearch.index.shard.IllegalIndexShardStateException;
import org.elasticsearch.index.shard.IndexShardComponent;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.store.StoreStats;
import org.elasticsearch.index.termvectors.ShardTermVectorService;
import org.elasticsearch.index.warmer.ShardIndexWarmerService;
import org.elasticsearch.index.warmer.WarmerStats;
import org.elasticsearch.search.suggest.completion.CompletionStats;

/**
 *
 */
public interface IndexShard extends IndexShardComponent {

    ShardIndexingService indexingService();

    ShardGetService getService();

    ShardSearchService searchService();

    ShardIndexWarmerService warmerService();

    ShardFilterCache filterCache();

    ShardIdCache idCache();

    ShardFieldData fieldData();

    ShardRouting routingEntry();

    DocsStats docStats();

    StoreStats storeStats();

    IndexingStats indexingStats(String... types);

    SearchStats searchStats(String... groups);

    GetStats getStats();

    MergeStats mergeStats();

    SegmentsStats segmentStats();

    RefreshStats refreshStats();

    FlushStats flushStats();

    WarmerStats warmerStats();

    FilterCacheStats filterCacheStats();

    IdCacheStats idCacheStats();

    FieldDataStats fieldDataStats(String... fields);

    CompletionStats completionStats(String... fields);

    PercolatorQueriesRegistry percolateRegistry();

    ShardPercolateService shardPercolateService();

    ShardTermVectorService termVectorService();

    MapperService mapperService();

    IndexFieldDataService indexFieldDataService();

    IndexService indexService();

    IndexShardState state();

    Engine.Create prepareCreate(SourceToParse source) throws ElasticSearchException;

    ParsedDocument create(Engine.Create create) throws ElasticSearchException;

    Engine.Index prepareIndex(SourceToParse source) throws ElasticSearchException;

    ParsedDocument index(Engine.Index index) throws ElasticSearchException;

    Engine.Delete prepareDelete(String type, String id, long version) throws ElasticSearchException;

    void delete(Engine.Delete delete) throws ElasticSearchException;

    Engine.DeleteByQuery prepareDeleteByQuery(BytesReference querySource, @Nullable String[] filteringAliases, String... types) throws ElasticSearchException;

    void deleteByQuery(Engine.DeleteByQuery deleteByQuery) throws ElasticSearchException;

    Engine.GetResult get(Engine.Get get) throws ElasticSearchException;

    void refresh(Engine.Refresh refresh) throws ElasticSearchException;

    void flush(Engine.Flush flush) throws ElasticSearchException;

    void optimize(Engine.Optimize optimize) throws ElasticSearchException;

    <T> T snapshot(Engine.SnapshotHandler<T> snapshotHandler) throws EngineException;

    SnapshotIndexCommit snapshotIndex() throws EngineException;

    void recover(Engine.RecoveryHandler recoveryHandler) throws EngineException;

    Engine.Searcher acquireSearcher(String source);

    Engine.Searcher acquireSearcher(String source, Mode mode);

    /**
     * Returns <tt>true</tt> if this shard can ignore a recovery attempt made to it (since the already doing/done it)
     */
    public boolean ignoreRecoveryAttempt();

    void readAllowed() throws IllegalIndexShardStateException;

    void readAllowed(Mode mode) throws IllegalIndexShardStateException;

    public enum Mode {
        READ,
        WRITE
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8042.java