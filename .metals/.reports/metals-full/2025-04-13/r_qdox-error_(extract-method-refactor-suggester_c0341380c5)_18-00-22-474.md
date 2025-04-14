error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7220.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7220.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7220.java
text:
```scala
r@@eturn new PrimaryResponse<>(shardRequest.request, new ShardDeleteByQueryResponse(), null);

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

package org.elasticsearch.action.deletebyquery;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.search.Filter;
import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.action.support.replication.TransportShardReplicationOperationAction;
import org.elasticsearch.cache.recycler.CacheRecycler;
import org.elasticsearch.cache.recycler.PageCacheRecycler;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.ParsedQuery;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.internal.DefaultSearchContext;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.internal.ShardSearchRequest;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 *
 */
public class TransportShardDeleteByQueryAction extends TransportShardReplicationOperationAction<ShardDeleteByQueryRequest, ShardDeleteByQueryRequest, ShardDeleteByQueryResponse> {

    private final ScriptService scriptService;
    private final CacheRecycler cacheRecycler;
    private final PageCacheRecycler pageCacheRecycler;
    private final BigArrays bigArrays;

    @Inject
    public TransportShardDeleteByQueryAction(Settings settings, TransportService transportService,
                                             ClusterService clusterService, IndicesService indicesService, ThreadPool threadPool,
                                             ShardStateAction shardStateAction, ScriptService scriptService, CacheRecycler cacheRecycler,
                                             PageCacheRecycler pageCacheRecycler, BigArrays bigArrays) {
        super(settings, transportService, clusterService, indicesService, threadPool, shardStateAction);
        this.scriptService = scriptService;
        this.cacheRecycler = cacheRecycler;
        this.pageCacheRecycler = pageCacheRecycler;
        this.bigArrays = bigArrays;
    }

    @Override
    protected boolean checkWriteConsistency() {
        return true;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.INDEX;
    }

    @Override
    protected ShardDeleteByQueryRequest newRequestInstance() {
        return new ShardDeleteByQueryRequest();
    }

    @Override
    protected ShardDeleteByQueryRequest newReplicaRequestInstance() {
        return new ShardDeleteByQueryRequest();
    }

    @Override
    protected ShardDeleteByQueryResponse newResponseInstance() {
        return new ShardDeleteByQueryResponse();
    }

    @Override
    protected String transportAction() {
        return DeleteByQueryAction.NAME + "/shard";
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, ShardDeleteByQueryRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.WRITE);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, ShardDeleteByQueryRequest request) {
        return state.blocks().indexBlockedException(ClusterBlockLevel.WRITE, request.index());
    }

    @Override
    protected PrimaryResponse<ShardDeleteByQueryResponse, ShardDeleteByQueryRequest> shardOperationOnPrimary(ClusterState clusterState, PrimaryOperationRequest shardRequest) {
        ShardDeleteByQueryRequest request = shardRequest.request;
        IndexService indexService = indicesService.indexServiceSafe(shardRequest.request.index());
        IndexShard indexShard = indexService.shardSafe(shardRequest.shardId);

        SearchContext.setCurrent(new DefaultSearchContext(0, new ShardSearchRequest().types(request.types()).nowInMillis(request.nowInMillis()), null,
                indexShard.acquireSearcher("delete_by_query"), indexService, indexShard, scriptService, cacheRecycler,
                pageCacheRecycler, bigArrays));
        try {
            Engine.DeleteByQuery deleteByQuery = indexShard.prepareDeleteByQuery(request.source(), request.filteringAliases(), request.types())
                    .origin(Engine.Operation.Origin.PRIMARY);
            SearchContext.current().parsedQuery(new ParsedQuery(deleteByQuery.query(), ImmutableMap.<String, Filter>of()));
            indexShard.deleteByQuery(deleteByQuery);
        } finally {
            SearchContext searchContext = SearchContext.current();
            searchContext.clearAndRelease();
            SearchContext.removeCurrent();
        }
        return new PrimaryResponse<ShardDeleteByQueryResponse, ShardDeleteByQueryRequest>(shardRequest.request, new ShardDeleteByQueryResponse(), null);
    }


    @Override
    protected void shardOperationOnReplica(ReplicaOperationRequest shardRequest) {
        ShardDeleteByQueryRequest request = shardRequest.request;
        IndexService indexService = indicesService.indexServiceSafe(shardRequest.request.index());
        IndexShard indexShard = indexService.shardSafe(shardRequest.shardId);

        SearchContext.setCurrent(new DefaultSearchContext(0, new ShardSearchRequest().types(request.types()).nowInMillis(request.nowInMillis()), null,
                indexShard.acquireSearcher("delete_by_query", IndexShard.Mode.WRITE), indexService, indexShard, scriptService,
                cacheRecycler, pageCacheRecycler, bigArrays));
        try {
            Engine.DeleteByQuery deleteByQuery = indexShard.prepareDeleteByQuery(request.source(), request.filteringAliases(), request.types())
                    .origin(Engine.Operation.Origin.REPLICA);
            SearchContext.current().parsedQuery(new ParsedQuery(deleteByQuery.query(), ImmutableMap.<String, Filter>of()));
            indexShard.deleteByQuery(deleteByQuery);
        } finally {
            SearchContext searchContext = SearchContext.current();
            searchContext.clearAndRelease();
            SearchContext.removeCurrent();
        }
    }

    @Override
    protected ShardIterator shards(ClusterState clusterState, ShardDeleteByQueryRequest request) {
        GroupShardsIterator group = clusterService.operationRouting().deleteByQueryShards(clusterService.state(), request.index(), request.routing());
        for (ShardIterator shardIt : group) {
            if (shardIt.shardId().id() == request.shardId()) {
                return shardIt;
            }
        }
        throw new ElasticsearchIllegalStateException("No shards iterator found for shard [" + request.shardId() + "]");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7220.java