error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2497.java
text:
```scala
s@@earcher.close();

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

package org.elasticsearch.action.admin.indices.status;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationRequest;
import org.elasticsearch.action.support.broadcast.TransportBroadcastOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.block.ClusterBlockException;
import org.elasticsearch.cluster.block.ClusterBlockLevel;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.gateway.IndexShardGatewayService;
import org.elasticsearch.indices.recovery.RecoveryState;
import org.elasticsearch.index.service.InternalIndexService;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.indices.recovery.RecoveryStatus;
import org.elasticsearch.indices.recovery.RecoveryTarget;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static com.google.common.collect.Lists.newArrayList;

/**
 *
 */
public class TransportIndicesStatusAction extends TransportBroadcastOperationAction<IndicesStatusRequest, IndicesStatusResponse, TransportIndicesStatusAction.IndexShardStatusRequest, ShardStatus> {

    private final IndicesService indicesService;

    private final RecoveryTarget peerRecoveryTarget;

    @Inject
    public TransportIndicesStatusAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService,
                                        IndicesService indicesService, RecoveryTarget peerRecoveryTarget) {
        super(settings, threadPool, clusterService, transportService);
        this.peerRecoveryTarget = peerRecoveryTarget;
        this.indicesService = indicesService;
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.MANAGEMENT;
    }

    @Override
    protected String transportAction() {
        return IndicesStatusAction.NAME;
    }

    @Override
    protected IndicesStatusRequest newRequest() {
        return new IndicesStatusRequest();
    }

    /**
     * Status goes across *all* shards.
     */
    @Override
    protected GroupShardsIterator shards(ClusterState state, IndicesStatusRequest request, String[] concreteIndices) {
        return state.routingTable().allAssignedShardsGrouped(concreteIndices, true);
    }

    @Override
    protected ClusterBlockException checkGlobalBlock(ClusterState state, IndicesStatusRequest request) {
        return state.blocks().globalBlockedException(ClusterBlockLevel.METADATA);
    }

    @Override
    protected ClusterBlockException checkRequestBlock(ClusterState state, IndicesStatusRequest countRequest, String[] concreteIndices) {
        return state.blocks().indicesBlockedException(ClusterBlockLevel.METADATA, concreteIndices);
    }

    @Override
    protected IndicesStatusResponse newResponse(IndicesStatusRequest request, AtomicReferenceArray shardsResponses, ClusterState clusterState) {
        int successfulShards = 0;
        int failedShards = 0;
        List<ShardOperationFailedException> shardFailures = null;
        final List<ShardStatus> shards = newArrayList();
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
                shards.add((ShardStatus) shardResponse);
                successfulShards++;
            }
        }
        return new IndicesStatusResponse(shards.toArray(new ShardStatus[shards.size()]), clusterState, shardsResponses.length(), successfulShards, failedShards, shardFailures);
    }

    @Override
    protected IndexShardStatusRequest newShardRequest() {
        return new IndexShardStatusRequest();
    }

    @Override
    protected IndexShardStatusRequest newShardRequest(ShardRouting shard, IndicesStatusRequest request) {
        return new IndexShardStatusRequest(shard.index(), shard.id(), request);
    }

    @Override
    protected ShardStatus newShardResponse() {
        return new ShardStatus();
    }

    @Override
    protected ShardStatus shardOperation(IndexShardStatusRequest request) throws ElasticsearchException {
        InternalIndexService indexService = (InternalIndexService) indicesService.indexServiceSafe(request.index());
        InternalIndexShard indexShard = (InternalIndexShard) indexService.shardSafe(request.shardId());
        ShardStatus shardStatus = new ShardStatus(indexShard.routingEntry());
        shardStatus.state = indexShard.state();
        try {
            shardStatus.storeSize = indexShard.store().estimateSize();
        } catch (IOException e) {
            // failure to get the store size...
        }
        if (indexShard.state() == IndexShardState.STARTED) {
//            shardStatus.estimatedFlushableMemorySize = indexShard.estimateFlushableMemorySize();
            shardStatus.translogId = indexShard.translog().currentId();
            shardStatus.translogOperations = indexShard.translog().estimatedNumberOfOperations();
            Engine.Searcher searcher = indexShard.acquireSearcher("indices_status");
            try {
                shardStatus.docs = new DocsStatus();
                shardStatus.docs.numDocs = searcher.reader().numDocs();
                shardStatus.docs.maxDoc = searcher.reader().maxDoc();
                shardStatus.docs.deletedDocs = searcher.reader().numDeletedDocs();
            } finally {
                searcher.release();
            }

            shardStatus.mergeStats = indexShard.mergeScheduler().stats();
            shardStatus.refreshStats = indexShard.refreshStats();
            shardStatus.flushStats = indexShard.flushStats();
        }

        if (request.recovery) {
            // check on going recovery (from peer or gateway)
            RecoveryStatus peerRecoveryStatus = indexShard.recoveryStatus();
            if (peerRecoveryStatus == null) {
                peerRecoveryStatus = peerRecoveryTarget.recoveryStatus(indexShard.shardId());
            }
            if (peerRecoveryStatus != null) {
                PeerRecoveryStatus.Stage stage;
                switch (peerRecoveryStatus.stage()) {
                    case INIT:
                        stage = PeerRecoveryStatus.Stage.INIT;
                        break;
                    case INDEX:
                        stage = PeerRecoveryStatus.Stage.INDEX;
                        break;
                    case TRANSLOG:
                        stage = PeerRecoveryStatus.Stage.TRANSLOG;
                        break;
                    case FINALIZE:
                        stage = PeerRecoveryStatus.Stage.FINALIZE;
                        break;
                    case DONE:
                        stage = PeerRecoveryStatus.Stage.DONE;
                        break;
                    default:
                        stage = PeerRecoveryStatus.Stage.INIT;
                }
                shardStatus.peerRecoveryStatus = new PeerRecoveryStatus(stage, peerRecoveryStatus.recoveryState().getTimer().startTime(),
                        peerRecoveryStatus.recoveryState().getTimer().time(),
                        peerRecoveryStatus.recoveryState().getIndex().totalByteCount(),
                        peerRecoveryStatus.recoveryState().getIndex().reusedByteCount(),
                        peerRecoveryStatus.recoveryState().getIndex().recoveredByteCount(), peerRecoveryStatus.recoveryState().getTranslog().currentTranslogOperations());
            }

            IndexShardGatewayService gatewayService = indexService.shardInjector(request.shardId()).getInstance(IndexShardGatewayService.class);
            RecoveryState gatewayRecoveryState = gatewayService.recoveryState();
            if (gatewayRecoveryState != null) {
                GatewayRecoveryStatus.Stage stage;
                switch (gatewayRecoveryState.getStage()) {
                    case INIT:
                        stage = GatewayRecoveryStatus.Stage.INIT;
                        break;
                    case INDEX:
                        stage = GatewayRecoveryStatus.Stage.INDEX;
                        break;
                    case TRANSLOG:
                        stage = GatewayRecoveryStatus.Stage.TRANSLOG;
                        break;
                    case DONE:
                        stage = GatewayRecoveryStatus.Stage.DONE;
                        break;
                    default:
                        stage = GatewayRecoveryStatus.Stage.INIT;
                }
                shardStatus.gatewayRecoveryStatus = new GatewayRecoveryStatus(stage, gatewayRecoveryState.getTimer().startTime(), gatewayRecoveryState.getTimer().time(),
                        gatewayRecoveryState.getIndex().totalByteCount(), gatewayRecoveryState.getIndex().reusedByteCount(), gatewayRecoveryState.getIndex().recoveredByteCount(), gatewayRecoveryState.getTranslog().currentTranslogOperations());
            }
        }
        return shardStatus;
    }

    public static class IndexShardStatusRequest extends BroadcastShardOperationRequest {

        boolean recovery;

        boolean snapshot;

        IndexShardStatusRequest() {
        }

        IndexShardStatusRequest(String index, int shardId, IndicesStatusRequest request) {
            super(index, shardId, request);
            recovery = request.recovery();
            snapshot = request.snapshot();
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            recovery = in.readBoolean();
            snapshot = in.readBoolean();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeBoolean(recovery);
            out.writeBoolean(snapshot);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2497.java