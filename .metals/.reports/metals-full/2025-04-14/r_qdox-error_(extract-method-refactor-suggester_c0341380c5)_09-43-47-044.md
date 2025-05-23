error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2593.java
text:
```scala
D@@eleteByQueryResponse queryResponse = client2.deleteByQuery(deleteByQueryRequest("test").query(termQuery("name", "test2"))).actionGet();

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

package org.elasticsearch.test.integration.document;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.admin.indices.cache.clear.ClearIndicesCacheResponse;
import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.broadcast.BroadcastOperationThreading;
import org.elasticsearch.client.Client;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.elasticsearch.util.Unicode;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.json.JsonQueryBuilders.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class DocumentActionsTests extends AbstractNodesTests {

    protected Client client1;
    protected Client client2;

    @BeforeMethod public void startNodes() {
        startNode("server1");
        startNode("server2");
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
        return client("server1");
    }

    protected Client getClient2() {
        return client("server2");
    }

    @Test public void testIndexActions() throws Exception {
        logger.info("Running Cluster Health");
        ClusterHealthResponse clusterHealth = client1.admin().cluster().health(clusterHealth().waitForGreenStatus()).actionGet();
        logger.info("Done Cluster Health, status " + clusterHealth.status());
        assertThat(clusterHealth.timedOut(), equalTo(false));
        assertThat(clusterHealth.status(), equalTo(ClusterHealthStatus.GREEN));

        logger.info("Indexing [type1/1]");
        IndexResponse indexResponse = client1.index(indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
        assertThat(indexResponse.index(), equalTo(getConcreteIndexName()));
        assertThat(indexResponse.id(), equalTo("1"));
        assertThat(indexResponse.type(), equalTo("type1"));
        logger.info("Refreshing");
        RefreshResponse refreshResponse = client1.admin().indices().refresh(refreshRequest("test")).actionGet();
        assertThat(refreshResponse.successfulShards(), equalTo(10));
        assertThat(refreshResponse.failedShards(), equalTo(0));

        logger.info("Clearing cache");
        ClearIndicesCacheResponse clearIndicesCacheResponse = client1.admin().indices().clearCache(clearIndicesCache("test")).actionGet();
        assertThat(clearIndicesCacheResponse.successfulShards(), equalTo(10));
        assertThat(clearIndicesCacheResponse.failedShards(), equalTo(0));

        logger.info("Optimizing");
        OptimizeResponse optimizeResponse = client1.admin().indices().optimize(optimizeRequest("test")).actionGet();
        assertThat(optimizeResponse.successfulShards(), equalTo(10));
        assertThat(optimizeResponse.failedShards(), equalTo(0));

        GetResponse getResult;

        logger.info("Get [type1/1]");
        for (int i = 0; i < 5; i++) {
            getResult = client1.get(getRequest("test").type("type1").id("1").operationThreaded(false)).actionGet();
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
            assertThat("cycle #" + i, getResult.sourceAsString(), equalTo(source("1", "test")));
            assertThat("cycle(map) #" + i, (String) ((Map) getResult.sourceAsMap().get("type1")).get("name"), equalTo("test"));
            getResult = client1.get(getRequest("test").type("type1").id("1").operationThreaded(true)).actionGet();
            assertThat("cycle #" + i, getResult.sourceAsString(), equalTo(source("1", "test")));
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
        }

        logger.info("Get [type1/2] (should be empty)");
        for (int i = 0; i < 5; i++) {
            getResult = client1.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat(getResult.exists(), equalTo(false));
        }

        logger.info("Delete [type1/1]");
        DeleteResponse deleteResponse = client1.delete(deleteRequest("test").type("type1").id("1")).actionGet();
        assertThat(deleteResponse.index(), equalTo(getConcreteIndexName()));
        assertThat(deleteResponse.id(), equalTo("1"));
        assertThat(deleteResponse.type(), equalTo("type1"));
        logger.info("Refreshing");
        client1.admin().indices().refresh(refreshRequest("test")).actionGet();

        logger.info("Get [type1/1] (should be empty)");
        for (int i = 0; i < 5; i++) {
            getResult = client1.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat(getResult.exists(), equalTo(false));
        }

        logger.info("Index [type1/1]");
        client1.index(indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
        logger.info("Index [type1/2]");
        client1.index(indexRequest("test").type("type1").id("2").source(source("2", "test2"))).actionGet();

        logger.info("Flushing");
        FlushResponse flushResult = client1.admin().indices().flush(flushRequest("test")).actionGet();
        assertThat(flushResult.successfulShards(), equalTo(10));
        assertThat(flushResult.failedShards(), equalTo(0));
        logger.info("Refreshing");
        client1.admin().indices().refresh(refreshRequest("test")).actionGet();

        logger.info("Get [type1/1] and [type1/2]");
        for (int i = 0; i < 5; i++) {
            getResult = client1.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
            assertThat("cycle #" + i, getResult.sourceAsString(), equalTo(source("1", "test")));
            getResult = client1.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat("cycle #" + i, getResult.sourceAsString(), equalTo(source("2", "test2")));
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
        }

        logger.info("Count");
        // check count
        for (int i = 0; i < 5; i++) {
            // test successful
            CountResponse countResponse = client1.count(countRequest("test").querySource(termQuery("_type", "type1")).operationThreading(BroadcastOperationThreading.NO_THREADS)).actionGet();
            assertThat(countResponse.count(), equalTo(2l));
            assertThat(countResponse.successfulShards(), equalTo(5));
            assertThat(countResponse.failedShards(), equalTo(0));

            countResponse = client1.count(countRequest("test").querySource(termQuery("_type", "type1")).operationThreading(BroadcastOperationThreading.SINGLE_THREAD)).actionGet();
            assertThat(countResponse.count(), equalTo(2l));
            assertThat(countResponse.successfulShards(), equalTo(5));
            assertThat(countResponse.failedShards(), equalTo(0));

            countResponse = client1.count(countRequest("test").querySource(termQuery("_type", "type1")).operationThreading(BroadcastOperationThreading.THREAD_PER_SHARD)).actionGet();
            assertThat(countResponse.count(), equalTo(2l));
            assertThat(countResponse.successfulShards(), equalTo(5));
            assertThat(countResponse.failedShards(), equalTo(0));

            // test failed (simply query that can't be parsed)
            countResponse = client1.count(countRequest("test").querySource(Unicode.fromStringAsBytes("{ term : { _type : \"type1 } }"))).actionGet();

            assertThat(countResponse.count(), equalTo(0l));
            assertThat(countResponse.successfulShards(), equalTo(0));
            assertThat(countResponse.failedShards(), equalTo(5));
        }

        logger.info("Delete by query");
        DeleteByQueryResponse queryResponse = client2.deleteByQuery(deleteByQueryRequest("test").querySource(termQuery("name", "test2"))).actionGet();
        assertThat(queryResponse.index(getConcreteIndexName()).successfulShards(), equalTo(5));
        assertThat(queryResponse.index(getConcreteIndexName()).failedShards(), equalTo(0));
        client1.admin().indices().refresh(refreshRequest("test")).actionGet();

        logger.info("Get [type1/1] and [type1/2], should be empty");
        for (int i = 0; i < 5; i++) {
            getResult = client1.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
            assertThat("cycle #" + i, getResult.sourceAsString(), equalTo(source("1", "test")));
            getResult = client1.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat("cycle #" + i, getResult.exists(), equalTo(false));
            assertThat(getResult.index(), equalTo(getConcreteIndexName()));
        }
    }

    private String source(String id, String nameValue) {
        return "{ type1 : { \"id\" : \"" + id + "\", \"name\" : \"" + nameValue + "\" } }";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2593.java