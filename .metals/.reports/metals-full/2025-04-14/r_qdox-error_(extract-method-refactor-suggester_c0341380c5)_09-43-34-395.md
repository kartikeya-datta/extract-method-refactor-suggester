error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6963.java
text:
```scala
a@@ssertThat(indicesStatusResponse.index("test").docs().numDocs(), equalTo(1l));

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

package org.elasticsearch.test.integration.client.transport;

import org.elasticsearch.action.admin.indices.flush.FlushResponse;
import org.elasticsearch.action.admin.indices.optimize.OptimizeResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.status.IndicesStatusResponse;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.server.internal.InternalServer;
import org.elasticsearch.test.integration.AbstractServersTests;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.util.transport.TransportAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.json.JsonQueryBuilders.*;
import static org.elasticsearch.util.settings.ImmutableSettings.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public class SimpleSingleTransportClientTests extends AbstractServersTests {

    private TransportClient client;

    @AfterMethod public void closeServers() {
        closeAllServers();
        if (client != null) {
            client.close();
        }
    }

    @Test public void testOnlyWithTransportAddress() throws Exception {
        startServer("server1");
        TransportAddress server1Address = ((InternalServer) server("server1")).injector().getInstance(TransportService.class).boundAddress().publishAddress();
        client = new TransportClient(settingsBuilder().putBoolean("discovery.enabled", false).build());
        client.addTransportAddress(server1Address);
        testSimpleActions(client);
    }

    /*@Test*/

    public void testWithDiscovery() throws Exception {
        startServer("server1");
        client = new TransportClient(settingsBuilder().putBoolean("discovery.enabled", true).build());
        // wait a bit so nodes will be discovered
        Thread.sleep(1000);
        testSimpleActions(client);
    }

    private void testSimpleActions(Client client) throws Exception {
        logger.info("Creating index test");
        client.admin().indices().create(createIndexRequest("test")).actionGet();
        Thread.sleep(500);

        IndexResponse indexResponse = client.index(Requests.indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
        assertThat(indexResponse.id(), equalTo("1"));
        assertThat(indexResponse.type(), equalTo("type1"));

        logger.info("Refreshing");
        RefreshResponse refreshResponse = client.admin().indices().refresh(refreshRequest("test")).actionGet();
        assertThat(refreshResponse.successfulShards(), equalTo(5));
        assertThat(refreshResponse.failedShards(), equalTo(5)); // 5 are not active, since we started just one server

        logger.info("Optimizing");
        OptimizeResponse optimizeResponse = client.admin().indices().optimize(optimizeRequest("test")).actionGet();
        assertThat(optimizeResponse.successfulShards(), equalTo(5));
        assertThat(optimizeResponse.failedShards(), equalTo(5)); // 5 are not active, since we started just one server

        IndicesStatusResponse indicesStatusResponse = client.admin().indices().status(indicesStatus()).actionGet();
        assertThat(indicesStatusResponse.successfulShards(), equalTo(5));
        assertThat(indicesStatusResponse.failedShards(), equalTo(5)); // 5 are not active, since we started just one server
        assertThat(indicesStatusResponse.indices().size(), equalTo(1));
        assertThat(indicesStatusResponse.index("test").shards().size(), equalTo(5)); // 5 index shards (1 with 1 backup)
        assertThat(indicesStatusResponse.index("test").docs().numDocs(), equalTo(1));

        GetResponse getResult;

        for (int i = 0; i < 5; i++) {
            getResult = client.get(getRequest("test").type("type1").id("1").threadedOperation(false)).actionGet();
            assertThat("cycle #" + i, getResult.source(), equalTo(source("1", "test")));
            getResult = client.get(getRequest("test").type("type1").id("1").threadedOperation(true)).actionGet();
            assertThat("cycle #" + i, getResult.source(), equalTo(source("1", "test")));
        }

        for (int i = 0; i < 5; i++) {
            getResult = client.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat(getResult.empty(), equalTo(true));
        }

        DeleteResponse deleteResponse = client.delete(deleteRequest("test").type("type1").id("1")).actionGet();
        assertThat(deleteResponse.id(), equalTo("1"));
        assertThat(deleteResponse.type(), equalTo("type1"));
        client.admin().indices().refresh(refreshRequest("test")).actionGet();

        for (int i = 0; i < 5; i++) {
            getResult = client.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat(getResult.empty(), equalTo(true));
        }

        client.index(Requests.indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
        client.index(Requests.indexRequest("test").type("type1").id("2").source(source("2", "test"))).actionGet();

        FlushResponse flushResult = client.admin().indices().flush(flushRequest("test")).actionGet();
        assertThat(flushResult.successfulShards(), equalTo(5));
        assertThat(flushResult.failedShards(), equalTo(5)); // we only start one server
        client.admin().indices().refresh(refreshRequest("test")).actionGet();

        for (int i = 0; i < 5; i++) {
            getResult = client.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat("cycle #" + i, getResult.source(), equalTo(source("1", "test")));
            getResult = client.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat("cycle #" + i, getResult.source(), equalTo(source("2", "test")));
        }

        // check count
        for (int i = 0; i < 5; i++) {
            // test successful
            CountResponse countResponse = client.count(countRequest("test").querySource(termQuery("_type", "type1"))).actionGet();
            assertThat(countResponse.count(), equalTo(2l));
            assertThat(countResponse.successfulShards(), equalTo(5));
            assertThat(countResponse.failedShards(), equalTo(0));
            // test failed (simply query that can't be parsed)
            countResponse = client.count(countRequest("test").querySource("{ term : { _type : \"type1 } }")).actionGet();

            assertThat(countResponse.count(), equalTo(0l));
            assertThat(countResponse.successfulShards(), equalTo(0));
            assertThat(countResponse.failedShards(), equalTo(5));
        }

        DeleteByQueryResponse queryResponse = client.deleteByQuery(deleteByQueryRequest("test").querySource(termQuery("name", "test2"))).actionGet();
        assertThat(queryResponse.index("test").successfulShards(), equalTo(5));
        assertThat(queryResponse.index("test").failedShards(), equalTo(0));
        client.admin().indices().refresh(refreshRequest("test")).actionGet();

        for (int i = 0; i < 5; i++) {
            getResult = client.get(getRequest("test").type("type1").id("1")).actionGet();
            assertThat("cycle #" + i, getResult.source(), equalTo(source("1", "test")));
            getResult = client.get(getRequest("test").type("type1").id("2")).actionGet();
            assertThat("cycle #" + i, getResult.empty(), equalTo(false));
        }


        // stop the server
        closeServer("server1");

        // it should try and reconnect
        try {
            client.index(Requests.indexRequest("test").type("type1").id("1").source(source("1", "test"))).actionGet();
            assert false : "should fail...";
        } catch (NoNodeAvailableException e) {
            // all is well
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6963.java