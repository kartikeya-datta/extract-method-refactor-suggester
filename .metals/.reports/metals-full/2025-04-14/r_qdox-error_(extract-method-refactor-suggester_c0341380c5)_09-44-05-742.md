error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1815.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1815.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1815.java
text:
```scala
public static final S@@tring ACTION_NAME = SnapshotsStatusAction.NAME + "[nodes]";

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

package org.elasticsearch.action.admin.cluster.snapshots.status;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.nodes.*;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.SnapshotId;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.index.snapshots.IndexShardSnapshotStatus;
import org.elasticsearch.snapshots.SnapshotsService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Transport client that collects snapshot shard statuses from data nodes
 */
public class TransportNodesSnapshotsStatus extends TransportNodesOperationAction<TransportNodesSnapshotsStatus.Request, TransportNodesSnapshotsStatus.NodesSnapshotStatus, TransportNodesSnapshotsStatus.NodeRequest, TransportNodesSnapshotsStatus.NodeSnapshotStatus> {

    private static final String ACTION_NAME = "cluster/snapshot/status/nodes";

    private final SnapshotsService snapshotsService;

    @Inject
    public TransportNodesSnapshotsStatus(Settings settings, ClusterName clusterName, ThreadPool threadPool, ClusterService clusterService, TransportService transportService, SnapshotsService snapshotsService, ActionFilters actionFilters) {
        super(settings, ACTION_NAME, clusterName, threadPool, clusterService, transportService, actionFilters);
        this.snapshotsService = snapshotsService;
    }

    public void status(String[] nodesIds, SnapshotId[] snapshotIds, @Nullable TimeValue timeout, ActionListener<NodesSnapshotStatus> listener) {
        execute(new Request(nodesIds).snapshotIds(snapshotIds).timeout(timeout), listener);
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.GENERIC;
    }

    @Override
    protected boolean transportCompress() {
        return true; // compress since the metadata can become large
    }

    @Override
    protected Request newRequest() {
        return new Request();
    }

    @Override
    protected NodeRequest newNodeRequest() {
        return new NodeRequest();
    }

    @Override
    protected NodeRequest newNodeRequest(String nodeId, Request request) {
        return new NodeRequest(nodeId, request);
    }

    @Override
    protected NodeSnapshotStatus newNodeResponse() {
        return new NodeSnapshotStatus();
    }

    @Override
    protected NodesSnapshotStatus newResponse(Request request, AtomicReferenceArray responses) {
        final List<NodeSnapshotStatus> nodesList = Lists.newArrayList();
        final List<FailedNodeException> failures = Lists.newArrayList();
        for (int i = 0; i < responses.length(); i++) {
            Object resp = responses.get(i);
            if (resp instanceof NodeSnapshotStatus) { // will also filter out null response for unallocated ones
                nodesList.add((NodeSnapshotStatus) resp);
            } else if (resp instanceof FailedNodeException) {
                failures.add((FailedNodeException) resp);
            } else {
                logger.warn("unknown response type [{}], expected NodeSnapshotStatus or FailedNodeException", resp);
            }
        }
        return new NodesSnapshotStatus(clusterName, nodesList.toArray(new NodeSnapshotStatus[nodesList.size()]),
                failures.toArray(new FailedNodeException[failures.size()]));
    }

    @Override
    protected NodeSnapshotStatus nodeOperation(NodeRequest request) throws ElasticsearchException {
        ImmutableMap.Builder<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> snapshotMapBuilder = ImmutableMap.builder();
        try {
            String nodeId = clusterService.localNode().id();
            for (SnapshotId snapshotId : request.snapshotIds) {
                ImmutableMap<ShardId, IndexShardSnapshotStatus> shardsStatus = snapshotsService.currentSnapshotShards(snapshotId);
                if (shardsStatus == null) {
                    continue;
                }
                ImmutableMap.Builder<ShardId, SnapshotIndexShardStatus> shardMapBuilder = ImmutableMap.builder();
                for (ImmutableMap.Entry<ShardId, IndexShardSnapshotStatus> shardEntry : shardsStatus.entrySet()) {
                    SnapshotIndexShardStatus shardStatus;
                    IndexShardSnapshotStatus.Stage stage = shardEntry.getValue().stage();
                    if (stage != IndexShardSnapshotStatus.Stage.DONE && stage != IndexShardSnapshotStatus.Stage.FAILURE) {
                        // Store node id for the snapshots that are currently running.
                        shardStatus = new SnapshotIndexShardStatus(shardEntry.getKey(), shardEntry.getValue(), nodeId);
                    } else {
                        shardStatus = new SnapshotIndexShardStatus(shardEntry.getKey(), shardEntry.getValue());
                    }
                    shardMapBuilder.put(shardEntry.getKey(), shardStatus);
                }
                snapshotMapBuilder.put(snapshotId, shardMapBuilder.build());
            }
            return new NodeSnapshotStatus(clusterService.localNode(), snapshotMapBuilder.build());
        } catch (Exception e) {
            throw new ElasticsearchException("failed to load metadata", e);
        }
    }

    @Override
    protected boolean accumulateExceptions() {
        return true;
    }

    static class Request extends NodesOperationRequest<Request> {

        private SnapshotId[] snapshotIds;

        public Request() {
        }

        public Request(String[] nodesIds) {
            super(nodesIds);
        }

        public Request snapshotIds(SnapshotId[] snapshotIds) {
            this.snapshotIds = snapshotIds;
            return this;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            // This operation is never executed remotely
            throw new UnsupportedOperationException("shouldn't be here");
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            // This operation is never executed remotely
            throw new UnsupportedOperationException("shouldn't be here");
        }
    }

    public static class NodesSnapshotStatus extends NodesOperationResponse<NodeSnapshotStatus> {

        private FailedNodeException[] failures;

        NodesSnapshotStatus() {
        }

        public NodesSnapshotStatus(ClusterName clusterName, NodeSnapshotStatus[] nodes, FailedNodeException[] failures) {
            super(clusterName, nodes);
            this.failures = failures;
        }

        public FailedNodeException[] failures() {
            return failures;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            nodes = new NodeSnapshotStatus[in.readVInt()];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new NodeSnapshotStatus();
                nodes[i].readFrom(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(nodes.length);
            for (NodeSnapshotStatus response : nodes) {
                response.writeTo(out);
            }
        }
    }


    static class NodeRequest extends NodeOperationRequest {

        private SnapshotId[] snapshotIds;

        NodeRequest() {
        }

        NodeRequest(String nodeId, TransportNodesSnapshotsStatus.Request request) {
            super(request, nodeId);
            snapshotIds = request.snapshotIds;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            int n = in.readVInt();
            snapshotIds = new SnapshotId[n];
            for (int i = 0; i < n; i++) {
                snapshotIds[i] = SnapshotId.readSnapshotId(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            if (snapshotIds != null) {
                out.writeVInt(snapshotIds.length);
                for (int i = 0; i < snapshotIds.length; i++) {
                    snapshotIds[i].writeTo(out);
                }
            } else {
                out.writeVInt(0);
            }
        }
    }

    public static class NodeSnapshotStatus extends NodeOperationResponse {

        private ImmutableMap<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> status;

        NodeSnapshotStatus() {
        }

        public NodeSnapshotStatus(DiscoveryNode node, ImmutableMap<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> status) {
            super(node);
            this.status = status;
        }

        public ImmutableMap<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> status() {
            return status;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            int numberOfSnapshots = in.readVInt();
            ImmutableMap.Builder<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> snapshotMapBuilder = ImmutableMap.builder();
            for (int i = 0; i < numberOfSnapshots; i++) {
                SnapshotId snapshotId = SnapshotId.readSnapshotId(in);
                ImmutableMap.Builder<ShardId, SnapshotIndexShardStatus> shardMapBuilder = ImmutableMap.builder();
                int numberOfShards = in.readVInt();
                for (int j = 0; j < numberOfShards; j++) {
                    ShardId shardId =  ShardId.readShardId(in);
                    SnapshotIndexShardStatus status = SnapshotIndexShardStatus.readShardSnapshotStatus(in);
                    shardMapBuilder.put(shardId, status);
                }
                snapshotMapBuilder.put(snapshotId, shardMapBuilder.build());
            }
            status = snapshotMapBuilder.build();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            if (status != null) {
                out.writeVInt(status.size());
                for (ImmutableMap.Entry<SnapshotId, ImmutableMap<ShardId, SnapshotIndexShardStatus>> entry : status.entrySet()) {
                    entry.getKey().writeTo(out);
                    out.writeVInt(entry.getValue().size());
                    for (ImmutableMap.Entry<ShardId, SnapshotIndexShardStatus> shardEntry : entry.getValue().entrySet()) {
                        shardEntry.getKey().writeTo(out);
                        shardEntry.getValue().writeTo(out);
                    }
                }
            } else {
                out.writeVInt(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1815.java