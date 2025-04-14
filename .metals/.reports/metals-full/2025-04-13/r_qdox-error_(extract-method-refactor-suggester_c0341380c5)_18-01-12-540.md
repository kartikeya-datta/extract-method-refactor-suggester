error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6274.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6274.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6274.java
text:
```scala
r@@esult.matches(), result.count(), tookInMillis, result.reducedAggregations()

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
package org.elasticsearch.action.percolate;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.TransportGetAction;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.TransportBroadcastOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.elasticsearch.percolator.PercolateException;
import org.elasticsearch.percolator.PercolatorService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.google.common.collect.Lists.newArrayList;

/**
 *
 */
public class TransportPercolateAction extends TransportBroadcastOperationAction<PercolateRequest, PercolateResponse, PercolateShardRequest, PercolateShardResponse> {

    private final PercolatorService percolatorService;
    private final TransportGetAction getAction;

    @Inject
    public TransportPercolateAction(Settings settings, ThreadPool threadPool, ClusterService clusterService,
                                    TransportService transportService, PercolatorService percolatorService,
                                    TransportGetAction getAction, ActionFilters actionFilters) {
        super(settings, PercolateAction.NAME, threadPool, clusterService, transportService, actionFilters);
        this.percolatorService = percolatorService;
        this.getAction = getAction;
    }

    @Override
    protected void doExecute(final PercolateRequest request, final ActionListener<PercolateResponse> listener) {
        request.startTime = System.currentTimeMillis();
        if (request.getRequest() != null) {
            //create a new get request to make sure it has the same headers and context as the original percolate request
            GetRequest getRequest = new GetRequest(request.getRequest(), request);
            getAction.execute(getRequest, new ActionListener<GetResponse>() {
                @Override
                public void onResponse(GetResponse getResponse) {
                    if (!getResponse.isExists()) {
                        onFailure(new DocumentMissingException(null, request.getRequest().type(), request.getRequest().id()));
                        return;
                    }

                    BytesReference docSource = getResponse.getSourceAsBytesRef();
                    TransportPercolateAction.super.doExecute(new PercolateRequest(request, docSource), listener);
                }

                @Override
                public void onFailure(Throwable e) {
                    listener.onFailure(e);
                }
            });
        } else {
            super.doExecute(request, listener);
        }
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.PERCOLATE;
    }

    @Override
    protected PercolateRequest newRequest() {
        return new PercolateRequest();
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, PercolateRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.READ);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, PercolateRequest request, String[] concreteIndices) {
        return state.blocks().indicesBlockedException(ClusterBlockLevel.READ, concreteIndices);
    }

    @Override
    protected PercolateResponse newResponse(PercolateRequest request, AtomicReferenceArray shardsResponses, ClusterState clusterState) {
        return reduce(request, shardsResponses, percolatorService);
    }

    public static PercolateResponse reduce(PercolateRequest request, AtomicReferenceArray shardsResponses, PercolatorService percolatorService) {
        int successfulShards = 0;
        int failedShards = 0;

        List<PercolateShardResponse> shardResults = null;
        List<ShardOperationFailedException> shardFailures = null;

        byte percolatorTypeId = 0x00;
        for (int i = 0; i < shardsResponses.length(); i++) {
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
                PercolateShardResponse percolateShardResponse = (PercolateShardResponse) shardResponse;
                successfulShards++;
                if (!percolateShardResponse.isEmpty()) {
                    if (shardResults == null) {
                        percolatorTypeId = percolateShardResponse.percolatorTypeId();
                        shardResults = newArrayList();
                    }
                    shardResults.add(percolateShardResponse);
                }
            }
        }

        if (shardResults == null) {
            long tookInMillis = System.currentTimeMillis() - request.startTime;
            PercolateResponse.Match[] matches = request.onlyCount() ? null : PercolateResponse.EMPTY;
            return new PercolateResponse(shardsResponses.length(), successfulShards, failedShards, shardFailures, tookInMillis, matches);
        } else {
            PercolatorService.ReduceResult result = percolatorService.reduce(percolatorTypeId, shardResults);
            long tookInMillis = System.currentTimeMillis() - request.startTime;
            return new PercolateResponse(
                    shardsResponses.length(), successfulShards, failedShards, shardFailures,
                    result.matches(), result.count(), tookInMillis, result.reducedFacets(), result.reducedAggregations()
            );
        }
    }

    @Override
    protected PercolateShardRequest newShardRequest() {
        return new PercolateShardRequest();
    }

    @Override
    protected PercolateShardRequest newShardRequest(int numShards, ShardRouting shard, PercolateRequest request) {
        return new PercolateShardRequest(shard.shardId(), numShards, request);
    }

    @Override
    protected PercolateShardResponse newShardResponse() {
        return new PercolateShardResponse();
    }

    @Override
    protected GroupShardsIterator shards(ClusterState clusterState, PercolateRequest request, String[] concreteIndices) {
        Map<String, Set<String>> routingMap = clusterState.metaData().resolveSearchRouting(request.routing(), request.indices());
        return clusterService.operationRouting().searchShards(clusterState, request.indices(), concreteIndices, routingMap, request.preference());
    }

    @Override
    protected PercolateShardResponse shardOperation(PercolateShardRequest request) throws ElasticsearchException {
        try {
            return percolatorService.percolate(request);
        } catch (Throwable e) {
            logger.trace("{} failed to percolate", e, request.shardId());
            throw new PercolateException(request.shardId(), "failed to percolate", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6274.java