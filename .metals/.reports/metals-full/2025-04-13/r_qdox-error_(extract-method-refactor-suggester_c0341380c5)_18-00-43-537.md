error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5452.java
text:
```scala
c@@lusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForYellowStatus().setWaitForNodes("2").execute().actionGet();

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

package org.elasticsearch.test.integration.cluster;

import com.google.common.collect.Sets;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.Discovery;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.gateway.Gateway;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.internal.InternalNode;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.Set;

import static org.elasticsearch.client.Requests.clusterHealthRequest;
import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Test
public class MinimumMasterNodesTests extends AbstractZenNodesTests {

    @AfterMethod
    public void cleanAndCloseNodes() throws Exception {
        for (int i = 0; i < 10; i++) {
            if (node("node" + i) != null) {
                node("node" + i).stop();
                // since we store (by default) the index snapshot under the gateway, resetting it will reset the index data as well
                if (((InternalNode) node("node" + i)).injector().getInstance(NodeEnvironment.class).hasNodeFile()) {
                    ((InternalNode) node("node" + i)).injector().getInstance(Gateway.class).reset();
                }
            }
        }
        closeAllNodes();
    }

    @Test
    public void simpleMinimumMasterNodes() throws Exception {
        logger.info("--> cleaning nodes");
        buildNode("node1", settingsBuilder().put("gateway.type", "local"));
        buildNode("node2", settingsBuilder().put("gateway.type", "local"));
        cleanAndCloseNodes();


        Settings settings = settingsBuilder()
                .put("discovery.type", "zen")
                .put("discovery.zen.minimum_master_nodes", 2)
                .put("discovery.zen.ping_timeout", "200ms")
                .put("discovery.initial_state_timeout", "500ms")
                .put("gateway.type", "local")
                .put("index.number_of_shards", 1)
                .build();

        logger.info("--> start first node");
        startNode("node1", settings);

        logger.info("--> should be blocked, no master...");
        ClusterState state = client("node1").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));

        logger.info("--> start second node, cluster should be formed");
        startNode("node2", settings);

        ClusterHealthResponse clusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForNodes("2").execute().actionGet();
        assertThat(clusterHealthResponse.timedOut(), equalTo(false));

        state = client("node1").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));
        state = client("node2").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));

        state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(2));
        assertThat(state.metaData().indices().containsKey("test"), equalTo(false));

        client("node1").admin().indices().prepareCreate("test").execute().actionGet();
        logger.info("--> indexing some data");
        for (int i = 0; i < 100; i++) {
            client("node1").prepareIndex("test", "type1", Integer.toString(i)).setSource("field", "value").execute().actionGet();
        }
        // flush for simpler debugging
        client("node1").admin().indices().prepareFlush().execute().actionGet();

        client("node1").admin().indices().prepareRefresh().execute().actionGet();
        logger.info("--> verify we the data back");
        for (int i = 0; i < 10; i++) {
            assertThat(client("node1").prepareCount().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet().count(), equalTo(100l));
        }

        String masterNodeName = state.nodes().masterNode().name();
        String nonMasterNodeName = masterNodeName.equals("node1") ? "node2" : "node1";
        logger.info("--> closing master node {}", masterNodeName);
        closeNode(masterNodeName);

        Thread.sleep(200);

        state = client(nonMasterNodeName).admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));

        logger.info("--> starting the previous master node again...");
        startNode(masterNodeName, settings);

        clusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForNodes("2").execute().actionGet();
        assertThat(clusterHealthResponse.timedOut(), equalTo(false));

        state = client("node1").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));
        state = client("node2").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));

        state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(2));
        assertThat(state.metaData().indices().containsKey("test"), equalTo(true));

        logger.info("Running Cluster Health");
        ClusterHealthResponse clusterHealth = client("node1").admin().cluster().health(clusterHealthRequest().waitForGreenStatus()).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));

        logger.info("--> verify we the data back");
        for (int i = 0; i < 10; i++) {
            assertThat(client("node1").prepareCount().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet().count(), equalTo(100l));
        }

        masterNodeName = state.nodes().masterNode().name();
        nonMasterNodeName = masterNodeName.equals("node1") ? "node2" : "node1";
        logger.info("--> closing non master node {}", nonMasterNodeName);
        closeNode(nonMasterNodeName);

        Thread.sleep(200);

        state = client(masterNodeName).admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));

        logger.info("--> starting the previous master node again...");
        startNode(nonMasterNodeName, settings);

        clusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForNodes("2").execute().actionGet();
        assertThat(clusterHealthResponse.timedOut(), equalTo(false));

        state = client("node1").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));
        state = client("node2").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(false));

        state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(2));
        assertThat(state.metaData().indices().containsKey("test"), equalTo(true));

        logger.info("Running Cluster Health");
        clusterHealth = client("node1").admin().cluster().health(clusterHealthRequest().waitForGreenStatus()).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));

        logger.info("--> verify we the data back");
        for (int i = 0; i < 10; i++) {
            assertThat(client("node1").prepareCount().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet().count(), equalTo(100l));
        }
    }

    @Test
    public void multipleNodesShutdownNonMasterNodes() throws Exception {
        logger.info("--> cleaning nodes");
        buildNode("node1", settingsBuilder().put("gateway.type", "local"));
        buildNode("node2", settingsBuilder().put("gateway.type", "local"));
        buildNode("node3", settingsBuilder().put("gateway.type", "local"));
        buildNode("node4", settingsBuilder().put("gateway.type", "local"));
        cleanAndCloseNodes();


        Settings settings = settingsBuilder()
                .put("discovery.type", "zen")
                .put("discovery.zen.minimum_master_nodes", 3)
                .put("discovery.zen.ping_timeout", "200ms")
                .put("discovery.initial_state_timeout", "500ms")
                .put("gateway.type", "local")
                .build();

        logger.info("--> start first 2 nodes");
        startNode("node1", settings);
        startNode("node2", settings);

        Thread.sleep(500);

        ClusterState state = client("node1").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));
        state = client("node2").admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));

        logger.info("--> start two more nodes");
        startNode("node3", settings);
        startNode("node4", settings);

        ClusterHealthResponse clusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForNodes("4").execute().actionGet();
        assertThat(clusterHealthResponse.timedOut(), equalTo(false));

        state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(4));
        String masterNode = state.nodes().masterNode().name();
        LinkedList<String> nonMasterNodes = new LinkedList<String>();
        for (DiscoveryNode node : state.nodes()) {
            if (!node.name().equals(masterNode)) {
                nonMasterNodes.add(node.name());
            }
        }

        logger.info("--> indexing some data");
        for (int i = 0; i < 100; i++) {
            client("node1").prepareIndex("test", "type1", Integer.toString(i)).setSource("field", "value").execute().actionGet();
        }
        // flush for simpler debugging
        client("node1").admin().indices().prepareFlush().execute().actionGet();

        client("node1").admin().indices().prepareRefresh().execute().actionGet();
        logger.info("--> verify we the data back");
        for (int i = 0; i < 10; i++) {
            assertThat(client("node1").prepareCount().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet().count(), equalTo(100l));
        }

        Set<String> nodesToShutdown = Sets.newHashSet();
        nodesToShutdown.add(nonMasterNodes.removeLast());
        nodesToShutdown.add(nonMasterNodes.removeLast());
        logger.info("--> shutting down two master nodes {}", nodesToShutdown);
        for (String nodeToShutdown : nodesToShutdown) {
            closeNode(nodeToShutdown);
        }

        Thread.sleep(1000);

        String lastNonMasterNodeUp = nonMasterNodes.removeLast();
        logger.info("--> verify that there is no master anymore on remaining nodes");
        state = client(masterNode).admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));
        state = client(lastNonMasterNodeUp).admin().cluster().prepareState().setLocal(true).execute().actionGet().state();
        assertThat(state.blocks().hasGlobalBlock(Discovery.NO_MASTER_BLOCK), equalTo(true));

        logger.info("--> start back the nodes {}", nodesToShutdown);
        for (String nodeToShutdown : nodesToShutdown) {
            startNode(nodeToShutdown, settings);
        }

        clusterHealthResponse = client("node1").admin().cluster().prepareHealth().setWaitForNodes("4").execute().actionGet();
        assertThat(clusterHealthResponse.timedOut(), equalTo(false));

        logger.info("Running Cluster Health");
        ClusterHealthResponse clusterHealth = client("node1").admin().cluster().health(clusterHealthRequest().waitForGreenStatus()).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));

        state = client("node1").admin().cluster().prepareState().execute().actionGet().state();
        assertThat(state.nodes().size(), equalTo(4));

        logger.info("--> verify we the data back");
        for (int i = 0; i < 10; i++) {
            assertThat(client("node1").prepareCount().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet().count(), equalTo(100l));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5452.java