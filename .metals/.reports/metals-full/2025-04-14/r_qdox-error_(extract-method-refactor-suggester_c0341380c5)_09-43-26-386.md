error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9133.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9133.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9133.java
text:
```scala
s@@napshotStatus.index().totalSize(), snapshotStatus.translog().expectedNumberOfOperations());

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

package org.elasticsearch.action.admin.indices.status;

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.TransportActions;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastShardOperationRequest;
import org.elasticsearch.action.support.broadcast.TransportBroadcastOperationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.routing.GroupShardsIterator;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.gateway.IndexShardGatewayService;
import org.elasticsearch.index.gateway.SnapshotStatus;
import org.elasticsearch.index.service.InternalIndexService;
import org.elasticsearch.index.shard.IndexShardState;
import org.elasticsearch.index.shard.recovery.RecoveryStatus;
import org.elasticsearch.index.shard.recovery.RecoveryTarget;
import org.elasticsearch.index.shard.service.InternalIndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

import static org.elasticsearch.common.collect.Lists.*;

/**
 * @author kimchy (shay.banon)
 */
public class TransportIndicesStatusAction extends TransportBroadcastOperationAction<IndicesStatusRequest, IndicesStatusResponse, TransportIndicesStatusAction.IndexShardStatusRequest, ShardStatus> {

    private final RecoveryTarget peerRecoveryTarget;

    @Inject public TransportIndicesStatusAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService,
                                                IndicesService indicesService, RecoveryTarget peerRecoveryTarget) {
        super(settings, threadPool, clusterService, transportService, indicesService);
        this.peerRecoveryTarget = peerRecoveryTarget;
    }

    @Override protected String transportAction() {
        return TransportActions.Admin.Indices.STATUS;
    }

    @Override protected String transportShardAction() {
        return "indices/status/shard";
    }

    @Override protected IndicesStatusRequest newRequest() {
        return new IndicesStatusRequest();
    }

    @Override protected boolean ignoreNonActiveExceptions() {
        return true;
    }

    /**
     * Status goes across *all* shards.
     */
    @Override protected GroupShardsIterator shards(IndicesStatusRequest request, ClusterState clusterState) {
        return clusterState.routingTable().allShardsGrouped(request.indices());
    }

    /**
     * We want to go over all assigned nodes (to get recovery status) and not just active ones.
     */
    @Override protected ShardRouting nextShardOrNull(ShardsIterator shardIt) {
        return shardIt.nextAssignedOrNull();
    }

    /**
     * We want to go over all assigned nodes (to get recovery status) and not just active ones.
     */
    @Override protected boolean hasNextShard(ShardsIterator shardIt) {
        return shardIt.hasNextAssigned();
    }

    @Override protected IndicesStatusResponse newResponse(IndicesStatusRequest request, AtomicReferenceArray shardsResponses, ClusterState clusterState) {
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

    @Override protected IndexShardStatusRequest newShardRequest() {
        return new IndexShardStatusRequest();
    }

    @Override protected IndexShardStatusRequest newShardRequest(ShardRouting shard, IndicesStatusRequest request) {
        return new IndexShardStatusRequest(shard.index(), shard.id());
    }

    @Override protected ShardStatus newShardResponse() {
        return new ShardStatus();
    }

    @Override protected ShardStatus shardOperation(IndexShardStatusRequest request) throws ElasticSearchException {
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
            shardStatus.translogOperations = indexShard.translog().size();
            Engine.Searcher searcher = indexShard.searcher();
            try {
                shardStatus.docs = new DocsStatus();
                shardStatus.docs.numDocs = searcher.reader().numDocs();
                shardStatus.docs.maxDoc = searcher.reader().maxDoc();
                shardStatus.docs.deletedDocs = searcher.reader().numDeletedDocs();
            } finally {
                searcher.release();
            }
        }
        // check on going recovery (from peer or gateway)
        RecoveryStatus peerRecoveryStatus = indexShard.peerRecoveryStatus();
        if (peerRecoveryStatus == null) {
            peerRecoveryStatus = peerRecoveryTarget.peerRecoveryStatus(indexShard.shardId());
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
            shardStatus.peerRecoveryStatus = new PeerRecoveryStatus(stage, peerRecoveryStatus.startTime(), peerRecoveryStatus.time(),
                    peerRecoveryStatus.phase1TotalSize(), peerRecoveryStatus.phase1ExistingTotalSize(),
                    peerRecoveryStatus.currentFilesSize(), peerRecoveryStatus.currentTranslogOperations());
        }

        IndexShardGatewayService gatewayService = indexService.shardInjector(request.shardId()).getInstance(IndexShardGatewayService.class);
        org.elasticsearch.index.gateway.RecoveryStatus gatewayRecoveryStatus = gatewayService.recoveryStatus();
        if (gatewayRecoveryStatus != null) {
            GatewayRecoveryStatus.Stage stage;
            switch (gatewayRecoveryStatus.stage()) {
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
            shardStatus.gatewayRecoveryStatus = new GatewayRecoveryStatus(stage, gatewayRecoveryStatus.startTime(), gatewayRecoveryStatus.time(),
                    gatewayRecoveryStatus.index().totalSize(), gatewayRecoveryStatus.index().existingTotalSize(), gatewayRecoveryStatus.index().currentFilesSize(), gatewayRecoveryStatus.translog().currentTranslogOperations());
        }

        SnapshotStatus snapshotStatus = gatewayService.snapshotStatus();
        if (snapshotStatus != null) {
            GatewaySnapshotStatus.Stage stage;
            switch (snapshotStatus.stage()) {
                case DONE:
                    stage = GatewaySnapshotStatus.Stage.DONE;
                    break;
                case FAILURE:
                    stage = GatewaySnapshotStatus.Stage.FAILURE;
                    break;
                case TRANSLOG:
                    stage = GatewaySnapshotStatus.Stage.TRANSLOG;
                    break;
                case FINALIZE:
                    stage = GatewaySnapshotStatus.Stage.FINALIZE;
                    break;
                case INDEX:
                    stage = GatewaySnapshotStatus.Stage.INDEX;
                    break;
                default:
                    stage = GatewaySnapshotStatus.Stage.NONE;
                    break;
            }
            shardStatus.gatewaySnapshotStatus = new GatewaySnapshotStatus(stage, snapshotStatus.startTime(), snapshotStatus.time(),
                    snapshotStatus.index().totalSize());
        }

        return shardStatus;
    }

    public static class IndexShardStatusRequest extends BroadcastShardOperationRequest {

        IndexShardStatusRequest() {
        }

        IndexShardStatusRequest(String index, int shardId) {
            super(index, shardId);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9133.java