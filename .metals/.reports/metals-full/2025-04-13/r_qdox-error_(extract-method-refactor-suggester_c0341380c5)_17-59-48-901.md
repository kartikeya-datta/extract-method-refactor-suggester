error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10369.java
text:
```scala
f@@ail();

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

package org.elasticsearch.tribe;

import com.google.common.base.Predicate;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.discovery.MasterNotDiscoveredException;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.TestCluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.Matchers.equalTo;

/**
 */
public class TribeTests extends ElasticsearchIntegrationTest {

    private TestCluster cluster2;
    private Node tribeNode;
    private Client tribeClient;

    @Before
    public void setupSecondCluster() {
        // create another cluster
        cluster2 = new TestCluster(randomLong(), 2, cluster().getClusterName() + "-2");
        cluster2.beforeTest(getRandom(), getPerTestTransportClientRatio());
        cluster2.ensureAtLeastNumNodes(2);

        Settings settings = ImmutableSettings.builder()
                .put("tribe.t1.cluster.name", cluster().getClusterName())
                .put("tribe.t2.cluster.name", cluster2.getClusterName())
                .build();

        tribeNode = NodeBuilder.nodeBuilder()
                .settings(settings)
                .node();
        tribeClient = tribeNode.client();
    }

    @After
    public void tearDownSecondCluster() {
        tribeNode.close();
        cluster2.afterTest();
        cluster2.close();
    }

    @Test
    public void testTribeOnOneCluster() throws Exception {
        logger.info("create 2 indices, test1 on t1, and test2 on t2");
        cluster().client().admin().indices().prepareCreate("test1").get();
        cluster2.client().admin().indices().prepareCreate("test2").get();


        // wait till the tribe node connected to the cluster, by checking if the index exists in the cluster state
        logger.info("wait till test1 and test2 exists in the tribe node state");
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object o) {
                ClusterState tribeState = tribeNode.client().admin().cluster().prepareState().setLocal(true).get().getState();
                return tribeState.getMetaData().hasIndex("test1") && tribeState.getMetaData().hasIndex("test2") &&
                        tribeState.getRoutingTable().hasIndex("test1") && tribeState.getRoutingTable().hasIndex("test2");
            }
        });

        logger.info("wait till tribe has the same nodes as the 2 clusters");
        awaitSameNodeCounts();

        assertThat(tribeClient.admin().cluster().prepareHealth().setLocal(true).setWaitForGreenStatus().get().getStatus(), equalTo(ClusterHealthStatus.GREEN));

        logger.info("create 2 docs through the tribe node");
        tribeClient.prepareIndex("test1", "type1", "1").setSource("field1", "value1").get();
        tribeClient.prepareIndex("test2", "type1", "1").setSource("field1", "value1").get();
        tribeClient.admin().indices().prepareRefresh().get();

        logger.info("verify they are there");
        assertHitCount(tribeClient.prepareCount().get(), 2l);
        assertHitCount(tribeClient.prepareSearch().get(), 2l);
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object o) {
                ClusterState tribeState = tribeNode.client().admin().cluster().prepareState().setLocal(true).get().getState();
                return tribeState.getMetaData().index("test1").mapping("type1") != null &&
                        tribeState.getMetaData().index("test2").mapping("type2") != null;
            }
        });


        logger.info("write to another type");
        tribeClient.prepareIndex("test1", "type2", "1").setSource("field1", "value1").get();
        tribeClient.prepareIndex("test2", "type2", "1").setSource("field1", "value1").get();
        tribeClient.admin().indices().prepareRefresh().get();


        logger.info("verify they are there");
        assertHitCount(tribeClient.prepareCount().get(), 4l);
        assertHitCount(tribeClient.prepareSearch().get(), 4l);
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object o) {
                ClusterState tribeState = tribeNode.client().admin().cluster().prepareState().setLocal(true).get().getState();
                return tribeState.getMetaData().index("test1").mapping("type1") != null && tribeState.getMetaData().index("test1").mapping("type2") != null &&
                        tribeState.getMetaData().index("test2").mapping("type1") != null && tribeState.getMetaData().index("test2").mapping("type2") != null;
            }
        });

        logger.info("make sure master level write operations fail... (we don't really have a master)");
        try {
            tribeClient.admin().indices().prepareCreate("tribe_index").setMasterNodeTimeout("10ms").get();
            assert false;
        } catch (MasterNotDiscoveredException e) {
            // all is well!
        }

        logger.info("delete an index, and make sure its reflected");
        cluster2.client().admin().indices().prepareDelete("test2").get();
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object o) {
                ClusterState tribeState = tribeNode.client().admin().cluster().prepareState().setLocal(true).get().getState();
                return tribeState.getMetaData().hasIndex("test1") && !tribeState.getMetaData().hasIndex("test2") &&
                        tribeState.getRoutingTable().hasIndex("test1") && !tribeState.getRoutingTable().hasIndex("test2");
            }
        });

        logger.info("stop a node, make sure its reflected");
        cluster2.stopRandomNode();
        awaitSameNodeCounts();
    }

    private void awaitSameNodeCounts() throws Exception {
        awaitBusy(new Predicate<Object>() {
            @Override
            public boolean apply(Object o) {
                DiscoveryNodes tribeNodes = tribeNode.client().admin().cluster().prepareState().setLocal(true).get().getState().getNodes();
                return countDataNodesForTribe("t1", tribeNodes) == cluster().client().admin().cluster().prepareState().get().getState().getNodes().dataNodes().size()
                        && countDataNodesForTribe("t2", tribeNodes) == cluster2.client().admin().cluster().prepareState().get().getState().getNodes().dataNodes().size();
            }
        });
    }

    private int countDataNodesForTribe(String tribeName, DiscoveryNodes nodes) {
        int count = 0;
        for (DiscoveryNode node : nodes) {
            if (!node.dataNode()) {
                continue;
            }
            if (tribeName.equals(node.getAttributes().get(TribeService.TRIBE_NAME))) {
                count++;
            }
        }
        return count;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10369.java