error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4303.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4303.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4303.java
text:
```scala
r@@eturn os.bytes().copyBytesArray().toBytes();

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

package org.elasticsearch.cluster;

import org.elasticsearch.cluster.block.ClusterBlocks;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.RoutingNodes;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.allocation.AllocationExplanation;
import org.elasticsearch.cluster.routing.allocation.RoutingAllocation;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.*;

import java.io.IOException;

/**
 *
 */
public class ClusterState {

    private final long version;

    private final RoutingTable routingTable;

    private final DiscoveryNodes nodes;

    private final MetaData metaData;

    private final ClusterBlocks blocks;

    private final AllocationExplanation allocationExplanation;

    // built on demand
    private volatile RoutingNodes routingNodes;

    public ClusterState(long version, ClusterState state) {
        this(version, state.metaData(), state.routingTable(), state.nodes(), state.blocks(), state.allocationExplanation());
    }

    public ClusterState(long version, MetaData metaData, RoutingTable routingTable, DiscoveryNodes nodes, ClusterBlocks blocks, AllocationExplanation allocationExplanation) {
        this.version = version;
        this.metaData = metaData;
        this.routingTable = routingTable;
        this.nodes = nodes;
        this.blocks = blocks;
        this.allocationExplanation = allocationExplanation;
    }

    public long version() {
        return this.version;
    }

    public long getVersion() {
        return version();
    }

    public DiscoveryNodes nodes() {
        return this.nodes;
    }

    public DiscoveryNodes getNodes() {
        return nodes();
    }

    public MetaData metaData() {
        return this.metaData;
    }

    public MetaData getMetaData() {
        return metaData();
    }

    public RoutingTable routingTable() {
        return routingTable;
    }

    public RoutingTable getRoutingTable() {
        return routingTable();
    }

    public RoutingNodes routingNodes() {
        return routingTable.routingNodes(this);
    }

    public RoutingNodes getRoutingNodes() {
        return readOnlyRoutingNodes();
    }

    public ClusterBlocks blocks() {
        return this.blocks;
    }

    public ClusterBlocks getBlocks() {
        return blocks;
    }

    public AllocationExplanation allocationExplanation() {
        return this.allocationExplanation;
    }

    public AllocationExplanation getAllocationExplanation() {
        return allocationExplanation();
    }

    /**
     * Returns a built (on demand) routing nodes view of the routing table. <b>NOTE, the routing nodes
     * are mutable, use them just for read operations</b>
     */
    public RoutingNodes readOnlyRoutingNodes() {
        if (routingNodes != null) {
            return routingNodes;
        }
        routingNodes = routingTable.routingNodes(this);
        return routingNodes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder newClusterStateBuilder() {
        return new Builder();
    }

    public static class Builder {

        private long version = 0;

        private MetaData metaData = MetaData.EMPTY_META_DATA;

        private RoutingTable routingTable = RoutingTable.EMPTY_ROUTING_TABLE;

        private DiscoveryNodes nodes = DiscoveryNodes.EMPTY_NODES;

        private ClusterBlocks blocks = ClusterBlocks.EMPTY_CLUSTER_BLOCK;

        private AllocationExplanation allocationExplanation = AllocationExplanation.EMPTY;

        public Builder nodes(DiscoveryNodes.Builder nodesBuilder) {
            return nodes(nodesBuilder.build());
        }

        public Builder nodes(DiscoveryNodes nodes) {
            this.nodes = nodes;
            return this;
        }

        public Builder routingTable(RoutingTable.Builder routingTable) {
            return routingTable(routingTable.build());
        }

        public Builder routingResult(RoutingAllocation.Result routingResult) {
            this.routingTable = routingResult.routingTable();
            this.allocationExplanation = routingResult.explanation();
            return this;
        }

        public Builder routingTable(RoutingTable routingTable) {
            this.routingTable = routingTable;
            return this;
        }

        public Builder metaData(MetaData.Builder metaDataBuilder) {
            return metaData(metaDataBuilder.build());
        }

        public Builder metaData(MetaData metaData) {
            this.metaData = metaData;
            return this;
        }

        public Builder blocks(ClusterBlocks.Builder blocksBuilder) {
            return blocks(blocksBuilder.build());
        }

        public Builder blocks(ClusterBlocks block) {
            this.blocks = block;
            return this;
        }

        public Builder allocationExplanation(AllocationExplanation allocationExplanation) {
            this.allocationExplanation = allocationExplanation;
            return this;
        }

        public Builder version(long version) {
            this.version = version;
            return this;
        }

        public Builder state(ClusterState state) {
            this.version = state.version();
            this.nodes = state.nodes();
            this.routingTable = state.routingTable();
            this.metaData = state.metaData();
            this.blocks = state.blocks();
            this.allocationExplanation = state.allocationExplanation();
            return this;
        }

        public ClusterState build() {
            return new ClusterState(version, metaData, routingTable, nodes, blocks, allocationExplanation);
        }

        public static byte[] toBytes(ClusterState state) throws IOException {
            CachedStreamOutput.Entry cachedEntry = CachedStreamOutput.popEntry();
            try {
                BytesStreamOutput os = cachedEntry.bytes();
                writeTo(state, os);
                return os.copiedByteArray();
            } finally {
                CachedStreamOutput.pushEntry(cachedEntry);
            }
        }

        public static ClusterState fromBytes(byte[] data, DiscoveryNode localNode) throws IOException {
            return readFrom(new BytesStreamInput(data, false), localNode);
        }

        public static void writeTo(ClusterState state, StreamOutput out) throws IOException {
            out.writeLong(state.version());
            MetaData.Builder.writeTo(state.metaData(), out);
            RoutingTable.Builder.writeTo(state.routingTable(), out);
            DiscoveryNodes.Builder.writeTo(state.nodes(), out);
            ClusterBlocks.Builder.writeClusterBlocks(state.blocks(), out);
            state.allocationExplanation().writeTo(out);
        }

        public static ClusterState readFrom(StreamInput in, @Nullable DiscoveryNode localNode) throws IOException {
            Builder builder = new Builder();
            builder.version = in.readLong();
            builder.metaData = MetaData.Builder.readFrom(in);
            builder.routingTable = RoutingTable.Builder.readFrom(in);
            builder.nodes = DiscoveryNodes.Builder.readFrom(in, localNode);
            builder.blocks = ClusterBlocks.Builder.readClusterBlocks(in);
            builder.allocationExplanation = AllocationExplanation.readAllocationExplanation(in);
            return builder.build();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4303.java