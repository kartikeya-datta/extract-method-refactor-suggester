error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3687.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3687.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3687.java
text:
```scala
i@@nt iters = scaledRandomIntBetween(10, 100);

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

package org.elasticsearch.cluster.routing.allocation;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.routing.MutableShardRouting;
import org.elasticsearch.cluster.routing.RoutingNodes;
import org.elasticsearch.cluster.routing.RoutingTable;
import org.elasticsearch.cluster.routing.ShardRoutingState;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.test.ElasticsearchAllocationTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.cluster.routing.ShardRoutingState.*;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class NodeVersionAllocationDeciderTests extends ElasticsearchAllocationTestCase {

    private final ESLogger logger = Loggers.getLogger(NodeVersionAllocationDeciderTests.class);

    @Test
    public void testDoNotAllocateFromPrimary() {
        AllocationService strategy = createAllocationService(settingsBuilder()
                .put("cluster.routing.allocation.concurrent_recoveries", 10)
                .put("cluster.routing.allocation.allow_rebalance", "always")
                .put("cluster.routing.allocation.cluster_concurrent_rebalance", -1)
                .build());

        logger.info("Building initial routing table");

        MetaData metaData = MetaData.builder()
                .put(IndexMetaData.builder("test").numberOfShards(5).numberOfReplicas(2))
                .build();

        RoutingTable routingTable = RoutingTable.builder()
                .addAsNew(metaData.index("test"))
                .build();

        ClusterState clusterState = ClusterState.builder().metaData(metaData).routingTable(routingTable).build();

        assertThat(routingTable.index("test").shards().size(), equalTo(5));
        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).shards().get(0).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(1).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(2).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(0).currentNodeId(), nullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(1).currentNodeId(), nullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(2).currentNodeId(), nullValue());
        }

        logger.info("start two nodes and fully start the shards");
        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder().put(newNode("node1")).put(newNode("node2"))).build();
        RoutingTable prevRoutingTable = routingTable;
        routingTable = strategy.reroute(clusterState).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(INITIALIZING));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(UNASSIGNED).size(), equalTo(2));

        }

        logger.info("start all the primary shards, replicas will start initializing");
        RoutingNodes routingNodes = clusterState.routingNodes();
        prevRoutingTable = routingTable;
        routingTable = strategy.applyStartedShards(clusterState, routingNodes.shardsWithState(INITIALIZING)).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        routingNodes = clusterState.routingNodes();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(INITIALIZING).size(), equalTo(1));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(UNASSIGNED).size(), equalTo(1));
        }

        routingNodes = clusterState.routingNodes();
        prevRoutingTable = routingTable;
        routingTable = strategy.applyStartedShards(clusterState, routingNodes.shardsWithState(INITIALIZING)).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        routingNodes = clusterState.routingNodes();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(STARTED).size(), equalTo(1));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(UNASSIGNED).size(), equalTo(1));
        }

        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder(clusterState.nodes())
                .put(newNode("node3", getPreviousVersion())))
                .build();
        prevRoutingTable = routingTable;
        routingTable = strategy.reroute(clusterState).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        routingNodes = clusterState.routingNodes();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(STARTED).size(), equalTo(1));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(UNASSIGNED).size(), equalTo(1));
        }


        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder(clusterState.nodes())
                .put(newNode("node4")))
                .build();
        prevRoutingTable = routingTable;
        routingTable = strategy.reroute(clusterState).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        routingNodes = clusterState.routingNodes();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(STARTED).size(), equalTo(1));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(INITIALIZING).size(), equalTo(1));
        }

        routingNodes = clusterState.routingNodes();
        prevRoutingTable = routingTable;
        routingTable = strategy.applyStartedShards(clusterState, routingNodes.shardsWithState(INITIALIZING)).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        routingNodes = clusterState.routingNodes();

        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).primaryShard().state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).replicaShardsWithState(STARTED).size(), equalTo(2));
        }
    }


    @Test
    public void testRandom() {
        AllocationService service = createAllocationService(settingsBuilder()
                .put("cluster.routing.allocation.concurrent_recoveries", 10)
                .put("cluster.routing.allocation.allow_rebalance", "always")
                .put("cluster.routing.allocation.cluster_concurrent_rebalance", -1)
                .build());

        logger.info("Building initial routing table");
        MetaData.Builder builder = MetaData.builder();
        RoutingTable.Builder rtBuilder = RoutingTable.builder();
        int numIndices = between(1, 20);
        for (int i = 0; i < numIndices; i++) {
            builder.put(IndexMetaData.builder("test_" + i).numberOfShards(between(1, 5)).numberOfReplicas(between(0, 2)));
        }
        MetaData metaData = builder.build();

        for (int i = 0; i < numIndices; i++) {
            rtBuilder.addAsNew(metaData.index("test_" + i));
        }
        RoutingTable routingTable = rtBuilder.build();

        ClusterState clusterState = ClusterState.builder().metaData(metaData).routingTable(routingTable).build();
        assertThat(routingTable.shardsWithState(UNASSIGNED).size(), equalTo(routingTable.allShards().size()));
        List<DiscoveryNode> nodes = new ArrayList<DiscoveryNode>();
        int nodeIdx = 0;
        int iters = atLeast(10);
        for (int i = 0; i < iters; i++) {
            DiscoveryNodes.Builder nodesBuilder = DiscoveryNodes.builder();
            int numNodes = between(1, 20);
            if (nodes.size() > numNodes) {
                Collections.shuffle(nodes, getRandom());
                nodes = nodes.subList(0, numNodes);
            } else {
                for (int j = nodes.size(); j < numNodes; j++) {
                    if (frequently()) {
                        nodes.add(newNode("node" + (nodeIdx++), randomBoolean() ? getPreviousVersion() : Version.CURRENT));
                    } else {
                        nodes.add(newNode("node" + (nodeIdx++), randomVersion()));
                    }
                }
            }
            for (DiscoveryNode node : nodes) {
               nodesBuilder.put(node);
            }
            clusterState = ClusterState.builder(clusterState).nodes(nodesBuilder).build();
            clusterState = stabilize(clusterState, service);
        }
    }

    @Test
    public void testRollingRestart() {
        AllocationService service = createAllocationService(settingsBuilder()
                .put("cluster.routing.allocation.concurrent_recoveries", 10)
                .put("cluster.routing.allocation.allow_rebalance", "always")
                .put("cluster.routing.allocation.cluster_concurrent_rebalance", -1)
                .build());

        logger.info("Building initial routing table");

        MetaData metaData = MetaData.builder()
                .put(IndexMetaData.builder("test").numberOfShards(5).numberOfReplicas(2))
                .build();

        RoutingTable routingTable = RoutingTable.builder()
                .addAsNew(metaData.index("test"))
                .build();

        ClusterState clusterState = ClusterState.builder().metaData(metaData).routingTable(routingTable).build();

        assertThat(routingTable.index("test").shards().size(), equalTo(5));
        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).shards().get(0).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(1).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(2).state(), equalTo(UNASSIGNED));
            assertThat(routingTable.index("test").shard(i).shards().get(0).currentNodeId(), nullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(1).currentNodeId(), nullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(2).currentNodeId(), nullValue());
        }
        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder()
                .put(newNode("old0", getPreviousVersion()))
                .put(newNode("old1", getPreviousVersion()))
                .put(newNode("old2", getPreviousVersion()))).build();
        clusterState = stabilize(clusterState, service);

        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder()
                .put(newNode("old0", getPreviousVersion()))
                .put(newNode("old1", getPreviousVersion()))
                .put(newNode("new0"))).build();

        clusterState = stabilize(clusterState, service);

        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder()
                .put(newNode("node0", getPreviousVersion()))
                .put(newNode("new1"))
                .put(newNode("new0"))).build();

        clusterState = stabilize(clusterState, service);

        clusterState = ClusterState.builder(clusterState).nodes(DiscoveryNodes.builder()
                .put(newNode("new2"))
                .put(newNode("new1"))
                .put(newNode("new0"))).build();

        clusterState = stabilize(clusterState, service);
        routingTable = clusterState.routingTable();
        for (int i = 0; i < routingTable.index("test").shards().size(); i++) {
            assertThat(routingTable.index("test").shard(i).shards().size(), equalTo(3));
            assertThat(routingTable.index("test").shard(i).shards().get(0).state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).shards().get(1).state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).shards().get(2).state(), equalTo(STARTED));
            assertThat(routingTable.index("test").shard(i).shards().get(0).currentNodeId(), notNullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(1).currentNodeId(), notNullValue());
            assertThat(routingTable.index("test").shard(i).shards().get(2).currentNodeId(), notNullValue());
        }
    }

    private ClusterState stabilize(ClusterState clusterState, AllocationService service) {
        logger.trace("RoutingNodes: {}", clusterState.routingNodes().prettyPrint());

        RoutingTable routingTable = service.reroute(clusterState).routingTable();
        clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
        RoutingNodes routingNodes = clusterState.routingNodes();
        assertRecoveryNodeVersions(routingNodes);

        logger.info("complete rebalancing");
        RoutingTable prev = routingTable;
        boolean stable = false;
        for (int i = 0; i < 1000; i++) {   // at most 200 iters - this should be enough for all tests
            logger.trace("RoutingNodes: {}", clusterState.getRoutingNodes().prettyPrint());
            routingTable = service.applyStartedShards(clusterState, routingNodes.shardsWithState(INITIALIZING)).routingTable();
            clusterState = ClusterState.builder(clusterState).routingTable(routingTable).build();
            routingNodes = clusterState.routingNodes();
            if (stable = (routingTable == prev)) {
                break;
            }
            assertRecoveryNodeVersions(routingNodes);
            prev = routingTable;
        }
        logger.info("stabilized success [{}]", stable);
        assertThat(stable, is(true));
        return clusterState;
    }

    private final void assertRecoveryNodeVersions(RoutingNodes routingNodes) {
        logger.trace("RoutingNodes: {}", routingNodes.prettyPrint());

        List<MutableShardRouting> mutableShardRoutings = routingNodes.shardsWithState(ShardRoutingState.RELOCATING);
        for (MutableShardRouting r : mutableShardRoutings) {
            String toId = r.relocatingNodeId();
            String fromId = r.currentNodeId();
            assertThat(fromId, notNullValue());
            assertThat(toId, notNullValue());
            logger.trace("From: " + fromId + " with Version: " + routingNodes.node(fromId).node().version() + " to: " + toId + " with Version: " + routingNodes.node(toId).node().version());
            assertTrue(routingNodes.node(toId).node().version().onOrAfter(routingNodes.node(fromId).node().version()));
        }

        mutableShardRoutings = routingNodes.shardsWithState(ShardRoutingState.INITIALIZING);
        for (MutableShardRouting r : mutableShardRoutings) {
            if (r.initializing() && r.relocatingNodeId() == null && !r.primary()) {
                MutableShardRouting primary = routingNodes.activePrimary(r);
                assertThat(primary, notNullValue());
                String fromId = primary.currentNodeId();
                String toId = r.currentNodeId();
                logger.trace("From: " + fromId + " with Version: " + routingNodes.node(fromId).node().version() + " to: " + toId + " with Version: " + routingNodes.node(toId).node().version());
                assertTrue(routingNodes.node(toId).node().version().onOrAfter(routingNodes.node(fromId).node().version()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3687.java