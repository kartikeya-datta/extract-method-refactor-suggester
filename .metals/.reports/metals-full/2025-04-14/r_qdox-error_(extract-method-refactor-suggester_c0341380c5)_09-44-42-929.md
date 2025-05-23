error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7548.java
text:
```scala
r@@eturn immutableCluster().size();

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

package org.elasticsearch.cluster.ack;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.admin.cluster.settings.ClusterUpdateSettingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.routing.IndexRoutingTable;
import org.elasticsearch.cluster.routing.IndexShardRoutingTable;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.allocation.decider.ThrottlingAllocationDecider;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.DiscoverySettings;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.Scope.TEST;
import static org.elasticsearch.test.TestCluster.DEFAULT_MAX_NUM_SHARDS;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;
import static org.hamcrest.Matchers.equalTo;

@ClusterScope(scope = TEST)
public class AckClusterUpdateSettingsTests extends ElasticsearchIntegrationTest {

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        //to test that the acknowledgement mechanism is working we better disable the wait for publish
        //otherwise the operation is most likely acknowledged even if it doesn't support ack
        return ImmutableSettings.builder()
                .put(DiscoverySettings.PUBLISH_TIMEOUT, 0)
                //make sure that enough concurrent reroutes can happen at the same time
                //we have a minimum of 2 nodes, and a maximum of 10 shards, thus 5 should be enough
                .put(ThrottlingAllocationDecider.CLUSTER_ROUTING_ALLOCATION_NODE_CONCURRENT_RECOVERIES, 5)
                .build();
    }

    @Override
    protected int minimumNumberOfShards() {
        return cluster().size();
    }

    @Override
    protected int numberOfReplicas() {
        return 0;
    }

    @Test
    public void testClusterUpdateSettingsAcknowledgement() {
        createIndex("test");
        ensureGreen();

        NodesInfoResponse nodesInfo = client().admin().cluster().prepareNodesInfo().get();
        String excludedNodeId = null;
        for (NodeInfo nodeInfo : nodesInfo) {
            if (nodeInfo.getNode().isDataNode()) {
                excludedNodeId = nodeInfo.getNode().id();
                break;
            }
        }
        assertNotNull(excludedNodeId);

        ClusterUpdateSettingsResponse clusterUpdateSettingsResponse = client().admin().cluster().prepareUpdateSettings()
                .setTransientSettings(settingsBuilder().put("cluster.routing.allocation.exclude._id", excludedNodeId)).get();
        assertAcked(clusterUpdateSettingsResponse);
        assertThat(clusterUpdateSettingsResponse.getTransientSettings().get("cluster.routing.allocation.exclude._id"), equalTo(excludedNodeId));

        for (Client client : clients()) {
            ClusterState clusterState = getLocalClusterState(client);
            assertThat(clusterState.routingNodes().metaData().transientSettings().get("cluster.routing.allocation.exclude._id"), equalTo(excludedNodeId));
            for (IndexRoutingTable indexRoutingTable : clusterState.routingTable()) {
                for (IndexShardRoutingTable indexShardRoutingTable : indexRoutingTable) {
                    for (ShardRouting shardRouting : indexShardRoutingTable) {
                        if (clusterState.nodes().get(shardRouting.currentNodeId()).id().equals(excludedNodeId)) {
                            //if the shard is still there it must be relocating and all nodes need to know, since the request was acknowledged
                            //reroute happens as part of the update settings and we made sure no throttling comes into the picture via settings
                            assertThat(shardRouting.relocating(), equalTo(true));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testClusterUpdateSettingsNoAcknowledgement() {
        client().admin().indices().prepareCreate("test")
                .setSettings(settingsBuilder()
                        .put("number_of_shards", between(cluster().dataNodes(), DEFAULT_MAX_NUM_SHARDS))
                        .put("number_of_replicas", 0)).get();
        ensureGreen();

        NodesInfoResponse nodesInfo = client().admin().cluster().prepareNodesInfo().get();
        String excludedNodeId = null;
        for (NodeInfo nodeInfo : nodesInfo) {
            if (nodeInfo.getNode().isDataNode()) {
                excludedNodeId = nodeInfo.getNode().id();
                break;
            }
        }
        assertNotNull(excludedNodeId);

        ClusterUpdateSettingsResponse clusterUpdateSettingsResponse = client().admin().cluster().prepareUpdateSettings().setTimeout("0s")
                .setTransientSettings(settingsBuilder().put("cluster.routing.allocation.exclude._id", excludedNodeId)).get();
        assertThat(clusterUpdateSettingsResponse.isAcknowledged(), equalTo(false));
        assertThat(clusterUpdateSettingsResponse.getTransientSettings().get("cluster.routing.allocation.exclude._id"), equalTo(excludedNodeId));
    }

    private static ClusterState getLocalClusterState(Client client) {
        return client.admin().cluster().prepareState().setLocal(true).get().getState();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7548.java