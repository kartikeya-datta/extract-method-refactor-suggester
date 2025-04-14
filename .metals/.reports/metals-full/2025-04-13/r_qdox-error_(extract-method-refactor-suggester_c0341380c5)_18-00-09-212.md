error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5212.java
text:
```scala
r@@eturn execute(new Request(shardId, nodesIds).setTimeout(timeout));

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

package org.elasticsearch.gateway.local.state.shards;

import com.google.common.collect.Lists;
import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.support.nodes.*;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 *
 */
public class TransportNodesListGatewayStartedShards extends TransportNodesOperationAction<TransportNodesListGatewayStartedShards.Request, TransportNodesListGatewayStartedShards.NodesLocalGatewayStartedShards, TransportNodesListGatewayStartedShards.NodeRequest, TransportNodesListGatewayStartedShards.NodeLocalGatewayStartedShards> {

    private LocalGatewayShardsState shardsState;

    @Inject
    public TransportNodesListGatewayStartedShards(Settings settings, ClusterName clusterName, ThreadPool threadPool, ClusterService clusterService, TransportService transportService) {
        super(settings, clusterName, threadPool, clusterService, transportService);
    }

    TransportNodesListGatewayStartedShards initGateway(LocalGatewayShardsState shardsState) {
        this.shardsState = shardsState;
        return this;
    }

    public ActionFuture<NodesLocalGatewayStartedShards> list(ShardId shardId, Set<String> nodesIds, @Nullable TimeValue timeout) {
        return execute(new Request(shardId, nodesIds).timeout(timeout));
    }

    @Override
    protected String executor() {
        return ThreadPool.Names.GENERIC;
    }

    @Override
    protected String transportAction() {
        return "/gateway/local/started-shards";
    }

    @Override
    protected boolean transportCompress() {
        return true; // this can become big...
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
    protected NodeLocalGatewayStartedShards newNodeResponse() {
        return new NodeLocalGatewayStartedShards();
    }

    @Override
    protected NodesLocalGatewayStartedShards newResponse(Request request, AtomicReferenceArray responses) {
        final List<NodeLocalGatewayStartedShards> nodesList = Lists.newArrayList();
        final List<FailedNodeException> failures = Lists.newArrayList();
        for (int i = 0; i < responses.length(); i++) {
            Object resp = responses.get(i);
            if (resp instanceof NodeLocalGatewayStartedShards) { // will also filter out null response for unallocated ones
                nodesList.add((NodeLocalGatewayStartedShards) resp);
            } else if (resp instanceof FailedNodeException) {
                failures.add((FailedNodeException) resp);
            }
        }
        return new NodesLocalGatewayStartedShards(clusterName, nodesList.toArray(new NodeLocalGatewayStartedShards[nodesList.size()]),
                failures.toArray(new FailedNodeException[failures.size()]));
    }

    @Override
    protected NodeLocalGatewayStartedShards nodeOperation(NodeRequest request) throws ElasticSearchException {
        try {
            ShardStateInfo shardStateInfo = shardsState.loadShardInfo(request.shardId);
            if (shardStateInfo != null) {
                return new NodeLocalGatewayStartedShards(clusterService.localNode(), shardStateInfo.version);
            }
            return new NodeLocalGatewayStartedShards(clusterService.localNode(), -1);
        } catch (Exception e) {
            throw new ElasticSearchException("failed to load started shards", e);
        }
    }

    @Override
    protected boolean accumulateExceptions() {
        return true;
    }

    static class Request extends NodesOperationRequest<Request> {

        private ShardId shardId;

        public Request() {
        }

        public Request(ShardId shardId, Set<String> nodesIds) {
            super(nodesIds.toArray(new String[nodesIds.size()]));
            this.shardId = shardId;
        }

        public ShardId shardId() {
            return this.shardId;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = ShardId.readShardId(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            shardId.writeTo(out);
        }
    }

    public static class NodesLocalGatewayStartedShards extends NodesOperationResponse<NodeLocalGatewayStartedShards> {

        private FailedNodeException[] failures;

        NodesLocalGatewayStartedShards() {
        }

        public NodesLocalGatewayStartedShards(ClusterName clusterName, NodeLocalGatewayStartedShards[] nodes, FailedNodeException[] failures) {
            super(clusterName, nodes);
            this.failures = failures;
        }

        public FailedNodeException[] failures() {
            return failures;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            nodes = new NodeLocalGatewayStartedShards[in.readVInt()];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = new NodeLocalGatewayStartedShards();
                nodes[i].readFrom(in);
            }
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeVInt(nodes.length);
            for (NodeLocalGatewayStartedShards response : nodes) {
                response.writeTo(out);
            }
        }
    }


    static class NodeRequest extends NodeOperationRequest {

        ShardId shardId;

        NodeRequest() {
        }

        NodeRequest(String nodeId, TransportNodesListGatewayStartedShards.Request request) {
            super(request, nodeId);
            this.shardId = request.shardId();
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            shardId = ShardId.readShardId(in);
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            shardId.writeTo(out);
        }
    }

    public static class NodeLocalGatewayStartedShards extends NodeOperationResponse {

        private long version = -1;

        NodeLocalGatewayStartedShards() {
        }

        public NodeLocalGatewayStartedShards(DiscoveryNode node, long version) {
            super(node);
            this.version = version;
        }

        public boolean hasVersion() {
            return version != -1;
        }

        public long version() {
            return this.version;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            version = in.readLong();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeLong(version);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5212.java