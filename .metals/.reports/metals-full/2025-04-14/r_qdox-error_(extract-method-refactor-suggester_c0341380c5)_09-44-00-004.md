error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3438.java
text:
```scala
i@@ndexDynamicSettings.addDynamicSetting(InternalIndexShard.INDEX_REFRESH_INTERVAL, Validator.TIME);

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

package org.elasticsearch.index.settings;

import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.routing.allocation.decider.FilterAllocationDecider;
import org.elasticsearch.cluster.routing.allocation.decider.ShardsLimitAllocationDecider;
import org.elasticsearch.cluster.settings.DynamicSettings;
import org.elasticsearch.cluster.settings.Validator;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.gateway.local.LocalGatewayAllocator;
import org.elasticsearch.index.engine.robin.RobinEngine;
import org.elasticsearch.index.gateway.IndexShardGatewayService;
import org.elasticsearch.index.indexing.slowlog.ShardSlowLogIndexingService;
import org.elasticsearch.index.merge.policy.LogByteSizeMergePolicyProvider;
import org.elasticsearch.index.merge.policy.LogDocMergePolicyProvider;
import org.elasticsearch.index.merge.policy.TieredMergePolicyProvider;
import org.elasticsearch.index.search.slowlog.ShardSlowLogSearchService;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.index.store.support.AbstractIndexStore;
import org.elasticsearch.index.translog.TranslogService;
import org.elasticsearch.index.translog.fs.FsTranslog;
import org.elasticsearch.indices.ttl.IndicesTTLService;

/**
 */
public class IndexDynamicSettingsModule extends AbstractModule {

    private final DynamicSettings indexDynamicSettings;

    public IndexDynamicSettingsModule() {
        indexDynamicSettings = new DynamicSettings();
        indexDynamicSettings.addDynamicSetting(AbstractIndexStore.INDEX_STORE_THROTTLE_MAX_BYTES_PER_SEC);
        indexDynamicSettings.addDynamicSetting(AbstractIndexStore.INDEX_STORE_THROTTLE_TYPE);
        indexDynamicSettings.addDynamicSetting(FilterAllocationDecider.INDEX_ROUTING_REQUIRE_GROUP + "*");
        indexDynamicSettings.addDynamicSetting(FilterAllocationDecider.INDEX_ROUTING_INCLUDE_GROUP + "*");
        indexDynamicSettings.addDynamicSetting(FilterAllocationDecider.INDEX_ROUTING_EXCLUDE_GROUP + "*");
        indexDynamicSettings.addDynamicSetting(FsTranslog.INDEX_TRANSLOG_FS_TYPE);
        indexDynamicSettings.addDynamicSetting(FsTranslog.INDEX_TRANSLOG_FS_BUFFER_SIZE);
        indexDynamicSettings.addDynamicSetting(FsTranslog.INDEX_TRANSLOG_FS_TRANSIENT_BUFFER_SIZE);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_NUMBER_OF_REPLICAS);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_AUTO_EXPAND_REPLICAS);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_READ_ONLY);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_BLOCKS_READ);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_BLOCKS_WRITE);
        indexDynamicSettings.addDynamicSetting(IndexMetaData.SETTING_BLOCKS_METADATA);
        indexDynamicSettings.addDynamicSetting(IndexShardGatewayService.INDEX_GATEWAY_SNAPSHOT_INTERVAL);
        indexDynamicSettings.addDynamicSetting(IndicesTTLService.INDEX_TTL_DISABLE_PURGE);
        indexDynamicSettings.addDynamicSetting(InternalIndexShard.INDEX_REFRESH_INTERVAL, Validator.TimeValueValidator.INSTANCE);
        indexDynamicSettings.addDynamicSetting(LocalGatewayAllocator.INDEX_RECOVERY_INITIAL_SHARDS);
        indexDynamicSettings.addDynamicSetting(LogByteSizeMergePolicyProvider.INDEX_MERGE_POLICY_MIN_MERGE_SIZE);
        indexDynamicSettings.addDynamicSetting(LogByteSizeMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGE_SIZE);
        indexDynamicSettings.addDynamicSetting(LogByteSizeMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGE_DOCS);
        indexDynamicSettings.addDynamicSetting(LogByteSizeMergePolicyProvider.INDEX_MERGE_POLICY_MERGE_FACTOR);
        indexDynamicSettings.addDynamicSetting(LogByteSizeMergePolicyProvider.INDEX_COMPOUND_FORMAT);
        indexDynamicSettings.addDynamicSetting(LogDocMergePolicyProvider.INDEX_MERGE_POLICY_MIN_MERGE_DOCS);
        indexDynamicSettings.addDynamicSetting(LogDocMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGE_DOCS);
        indexDynamicSettings.addDynamicSetting(LogDocMergePolicyProvider.INDEX_MERGE_POLICY_MERGE_FACTOR);
        indexDynamicSettings.addDynamicSetting(LogDocMergePolicyProvider.INDEX_COMPOUND_FORMAT);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_TERM_INDEX_INTERVAL);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_TERM_INDEX_DIVISOR);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_INDEX_CONCURRENCY);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_GC_DELETES);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_CODEC);
        indexDynamicSettings.addDynamicSetting(RobinEngine.INDEX_FAIL_ON_MERGE_FAILURE);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_THRESHOLD_INDEX_WARN);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_THRESHOLD_INDEX_INFO);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_THRESHOLD_INDEX_DEBUG);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_THRESHOLD_INDEX_TRACE);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_REFORMAT);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogIndexingService.INDEX_INDEXING_SLOWLOG_LEVEL);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_QUERY_WARN);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_QUERY_INFO);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_QUERY_DEBUG);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_QUERY_TRACE);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_FETCH_WARN);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_FETCH_INFO);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_FETCH_DEBUG);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_THRESHOLD_FETCH_TRACE);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_REFORMAT);
        indexDynamicSettings.addDynamicSetting(ShardSlowLogSearchService.INDEX_SEARCH_SLOWLOG_LEVEL);
        indexDynamicSettings.addDynamicSetting(ShardsLimitAllocationDecider.INDEX_TOTAL_SHARDS_PER_NODE);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_EXPUNGE_DELETES_ALLOWED);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_FLOOR_SEGMENT);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGE_AT_ONCE);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGE_AT_ONCE_EXPLICIT);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_MAX_MERGED_SEGMENT);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_SEGMENTS_PER_TIER);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_MERGE_POLICY_RECLAIM_DELETES_WEIGHT);
        indexDynamicSettings.addDynamicSetting(TieredMergePolicyProvider.INDEX_COMPOUND_FORMAT);
        indexDynamicSettings.addDynamicSetting(TranslogService.INDEX_TRANSLOG_FLUSH_THRESHOLD_OPS);
        indexDynamicSettings.addDynamicSetting(TranslogService.INDEX_TRANSLOG_FLUSH_THRESHOLD_SIZE);
        indexDynamicSettings.addDynamicSetting(TranslogService.INDEX_TRANSLOG_FLUSH_THRESHOLD_PERIOD);
        indexDynamicSettings.addDynamicSetting(TranslogService.INDEX_TRANSLOG_DISABLE_FLUSH);
    }

    public void addDynamicSettings(String... settings) {
        indexDynamicSettings.addDynamicSettings(settings);
    }

    public void addDynamicSetting(String setting, Validator validator) {
        indexDynamicSettings.addDynamicSetting(setting, validator);
    }

    @Override
    protected void configure() {
        bind(DynamicSettings.class).annotatedWith(IndexDynamicSettings.class).toInstance(indexDynamicSettings);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3438.java