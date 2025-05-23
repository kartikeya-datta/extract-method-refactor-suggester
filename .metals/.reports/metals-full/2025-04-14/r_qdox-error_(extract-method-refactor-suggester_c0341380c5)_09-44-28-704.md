error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5098.java
text:
```scala
s@@criptService, pageCacheRecycler, bigArrays, threadPool.estimatedTimeInMillisCounter());

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

package org.elasticsearch.action.exists;


import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.TransportBroadcastOperationAction;
import org.elasticsearch.cache.recycler.PageCacheRecycler;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.internal.DefaultSearchContext;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.internal.ShardSearchRequest;
import org.elasticsearch.search.query.QueryPhaseExecutionException;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.google.common.collect.Lists.newArrayList;
import static org.elasticsearch.action.exists.ExistsRequest.DEFAULT_MIN_SCORE;

public class TransportExistsAction extends TransportBroadcastOperationAction<ExistsRequest, ExistsResponse, ShardExistsRequest, ShardExistsResponse> {

    private final IndicesService indicesService;

    private final ScriptService scriptService;

    private final PageCacheRecycler pageCacheRecycler;

    private final BigArrays bigArrays;

    @Inject
    public TransportExistsAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService,
                                IndicesService indicesService, ScriptService scriptService,
                                PageCacheRecycler pageCacheRecycler, BigArrays bigArrays, ActionFilters actionFilters) {
        super(settings, ExistsAction.NAME, threadPool, clusterService, transportService, actionFilters);
        this.indicesService = indicesService;
        this.scriptService = scriptService;
        this.pageCacheRecycler = pageCacheRecycler;
        this.bigArrays = bigArrays;
    }

    @Override
    protected void doExecute(ExistsRequest request, ActionListener<ExistsResponse> listener) {
        request.nowInMillis = System.currentTimeMillis();
        new ExistsAsyncBroadcastAction(request, listener).start();
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.SEARCH;
    }

    @Override
    protected ExistsRequest newRequest() {
        return new ExistsRequest();
    }

    @Override
    protected ShardExistsRequest newShardRequest() {
        return new ShardExistsRequest();
    }

    @Override
    protected ShardExistsRequest newShardRequest(int numShards, ShardRouting shard, ExistsRequest request) {
        String[] filteringAliases = clusterService.state().metaData().filteringAliases(shard.index(), request.indices());
        return new ShardExistsRequest(shard.shardId(), filteringAliases, request);
    }

    @Override
    protected ShardExistsResponse newShardResponse() {
        return new ShardExistsResponse();
    }

    @Override
    protected GroupShardsIterator shards(ClusterState clusterState, ExistsRequest request, String[] concreteIndices) {
        Map<String, Set<String>> routingMap = clusterState.metaData().resolveSearchRouting(request.routing(), request.indices());
        return clusterService.operationRouting().searchShards(clusterState, request.indices(), concreteIndices, routingMap, request.preference());
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, ExistsRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, ExistsRequest countRequest, String[] concreteIndices) {
        return state.blocks().indicesBlockedException(ClusterBlockLevel.READ, concreteIndices);
    }

    @Override
    protected ExistsResponse newResponse(ExistsRequest request, AtomicReferenceArray shardsResponses, ClusterState clusterState) {
        int successfulShards = 0;
        int failedShards = 0;
        boolean exists = false;
        List<ShardOperationFailedException> shardFailures = null;

        // if docs do exist, the last response will have exists = true (since we early terminate the shard requests)
        for (int i = shardsResponses.length() - 1; i >= 0 ; i--) {
            Object shardResponse = shardsResponses.get(i);
            if (shardResponse == null) {
                // simply ignore non active shards
            } else if (shardResponse instanceof BroadcastShardOperationFailedException) {
                failedShards++;
                if (shardFailures == null) {
                    shardFailures = newArrayList();
                }
                shardFailures.add(new DefaultShardOperationFailedException((BroadcastShardOperationFailedException) shardResponse));
            } else {
                successfulShards++;
                if ((exists = ((ShardExistsResponse) shardResponse).exists())) {
                    successfulShards = shardsResponses.length() - failedShards;
                    break;
                }
            }
        }
        return new ExistsResponse(exists, shardsResponses.length(), successfulShards, failedShards, shardFailures);
    }

    @Override
    protected ShardExistsResponse shardOperation(ShardExistsRequest request) throws ElasticsearchException {
        IndexService indexService = indicesService.indexServiceSafe(request.shardId().getIndex());
        IndexShard indexShard = indexService.shardSafe(request.shardId().id());

        SearchShardTarget shardTarget = new SearchShardTarget(clusterService.localNode().id(), request.shardId().getIndex(), request.shardId().id());
        SearchContext context = new DefaultSearchContext(0,
                new ShardSearchRequest(request).types(request.types())
                        .filteringAliases(request.filteringAliases())
                        .nowInMillis(request.nowInMillis()),
                shardTarget, indexShard.acquireSearcher("exists"), indexService, indexShard,
                scriptService, pageCacheRecycler, bigArrays);
        SearchContext.setCurrent(context);

        try {
            if (request.minScore() != DEFAULT_MIN_SCORE) {
                context.minimumScore(request.minScore());
            }
            BytesReference source = request.querySource();
            if (source != null && source.length() > 0) {
                try {
                    QueryParseContext.setTypes(request.types());
                    context.parsedQuery(indexService.queryParserService().parseQuery(source));
                } finally {
                    QueryParseContext.removeTypes();
                }
            }
            context.preProcess();
            try {
                Lucene.EarlyTerminatingCollector existsCollector = Lucene.createExistsCollector();
                Lucene.exists(context.searcher(), context.query(), existsCollector);
                return new ShardExistsResponse(request.shardId(), existsCollector.exists());
            } catch (Exception e) {
                throw new QueryPhaseExecutionException(context, "failed to execute exists", e);
            }
        } finally {
            // this will also release the index searcher
            context.close();
            SearchContext.removeCurrent();
        }
    }

    /**
     * An async broadcast action that early terminates shard request
     * upon any shard response reporting matched doc existence
     */
    final private class ExistsAsyncBroadcastAction extends AsyncBroadcastAction  {

        final AtomicBoolean processed = new AtomicBoolean(false);

        ExistsAsyncBroadcastAction(ExistsRequest request, ActionListener<ExistsResponse> listener) {
            super(request, listener);
        }

        @Override
        protected void onOperation(ShardRouting shard, int shardIndex, ShardExistsResponse response) {
            super.onOperation(shard, shardIndex, response);
            if (response.exists()) {
                finishHim();
            }
        }

        @Override
        protected void performOperation(final ShardIterator shardIt, final ShardRouting shard, final int shardIndex) {
            if (processed.get()) {
                return;
            }
            super.performOperation(shardIt, shard, shardIndex);
        }

        @Override
        protected void finishHim() {
            if (processed.compareAndSet(false, true)) {
                super.finishHim();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5098.java