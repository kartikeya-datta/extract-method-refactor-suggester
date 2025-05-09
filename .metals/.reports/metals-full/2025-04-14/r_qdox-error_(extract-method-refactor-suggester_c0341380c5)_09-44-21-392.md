error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3528.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3528.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3528.java
text:
```scala
c@@lusterHealth = client1.admin().cluster().prepareHealth().setWaitForYellowStatus().setWaitForActiveShards(10).execute().actionGet();

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

package org.elasticsearch.test.integration.indices.settings;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.common.settings.ImmutableSettings.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class UpdateNumberOfReplicasTests extends AbstractNodesTests {

    protected Client client1;
    protected Client client2;

    @BeforeMethod public void startNodes() {
        startNode("node1");
        startNode("node2");
        client1 = getClient1();
        client2 = getClient2();

        createIndex();
    }

    protected void createIndex() {
        logger.info("Creating index test");
        client1.admin().indices().create(createIndexRequest("test")).actionGet();
    }

    protected String getConcreteIndexName() {
        return "test";
    }

    @AfterMethod public void closeNodes() {
        client1.close();
        client2.close();
        closeAllNodes();
    }

    protected Client getClient1() {
        return client("node1");
    }

    protected Client getClient2() {
        return client("node2");
    }

    @Test public void simpleUpdateNumberOfReplicasTests() throws Exception {
        logger.info("Running Cluster Health");
        ClusterHealthResponse clusterHealth = client1.admin().cluster().prepareHealth().setWaitForGreenStatus().execute().actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));
        assertThat(clusterHealth.indices().get("test").activePrimaryShards(), equalTo(5));
        assertThat(clusterHealth.indices().get("test").numberOfReplicas(), equalTo(1));
        assertThat(clusterHealth.indices().get("test").activeShards(), equalTo(10));

        for (int i = 0; i < 10; i++) {
            client1.prepareIndex("test", "type1", Integer.toString(i)).setSource(jsonBuilder().startObject()
                    .field("value", "test" + i)
                    .endObject()).execute().actionGet();
        }

        client1.admin().indices().prepareRefresh().execute().actionGet();

        for (int i = 0; i < 10; i++) {
            CountResponse countResponse = client1.prepareCount().setQuery(matchAllQuery()).execute().actionGet();
            assertThat(countResponse.count(), equalTo(10l));
        }

        logger.info("Increasing the number of replicas from 1 to 2");
        client1.admin().indices().prepareUpdateSettings("test").setSettings(settingsBuilder().put("index.number_of_replicas", 2)).execute().actionGet();
        Thread.sleep(200);

        logger.info("Running Cluster Health");
        clusterHealth = client1.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.YELLOW));
        assertThat(clusterHealth.indices().get("test").activePrimaryShards(), equalTo(5));
        assertThat(clusterHealth.indices().get("test").numberOfReplicas(), equalTo(2));
        assertThat(clusterHealth.indices().get("test").activeShards(), equalTo(10));

        logger.info("starting another node to new replicas will be allocated to it");
        startNode("node3");
        Thread.sleep(100);

        logger.info("Running Cluster Health");
        clusterHealth = client1.admin().cluster().prepareHealth().setWaitForGreenStatus().setWaitForNodes("3").execute().actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));
        assertThat(clusterHealth.indices().get("test").activePrimaryShards(), equalTo(5));
        assertThat(clusterHealth.indices().get("test").numberOfReplicas(), equalTo(2));
        assertThat(clusterHealth.indices().get("test").activeShards(), equalTo(15));

        for (int i = 0; i < 10; i++) {
            CountResponse countResponse = client1.prepareCount().setQuery(matchAllQuery()).execute().actionGet();
            assertThat(countResponse.count(), equalTo(10l));
        }

        logger.info("Decreasing number of replicas from 2 to 0");
        client1.admin().indices().prepareUpdateSettings("test").setSettings(settingsBuilder().put("index.number_of_replicas", 0)).execute().actionGet();
        Thread.sleep(200);

        logger.info("Running Cluster Health");
        clusterHealth = client1.admin().cluster().prepareHealth().setWaitForGreenStatus().setWaitForNodes("3").execute().actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));
        assertThat(clusterHealth.indices().get("test").activePrimaryShards(), equalTo(5));
        assertThat(clusterHealth.indices().get("test").numberOfReplicas(), equalTo(0));
        assertThat(clusterHealth.indices().get("test").activeShards(), equalTo(5));

        for (int i = 0; i < 10; i++) {
            CountResponse countResponse = client1.prepareCount().setQuery(matchAllQuery()).execute().actionGet();
            assertThat(countResponse.count(), equalTo(10l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3528.java