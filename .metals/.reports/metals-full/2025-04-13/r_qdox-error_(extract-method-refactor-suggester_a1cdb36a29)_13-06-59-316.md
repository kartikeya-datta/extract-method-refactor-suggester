error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8777.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8777.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8777.java
text:
```scala
.@@facets(facets().facet("all", termQuery("multi", "test"), true).facet("test1", termQuery("name", "test1")));

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

package org.elasticsearch.test.integration.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.test.integration.AbstractServersTests;
import org.elasticsearch.util.Unicode;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.elasticsearch.action.search.SearchType.*;
import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.json.JsonQueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.elasticsearch.util.TimeValue.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public class TransportTwoServersSearchTests extends AbstractServersTests {

    private Client client;

    @BeforeClass public void createServers() throws Exception {
        startServer("server1");
        startServer("server2");
        client = getClient();

        client.admin().indices().create(createIndexRequest("test")).actionGet();

        for (int i = 0; i < 100; i++) {
            index(client("server1"), Integer.toString(i), "test", i);
        }
        client.admin().indices().refresh(refreshRequest("test")).actionGet();
    }

    @AfterClass public void closeServers() {
        client.close();
        closeAllServers();
    }

    protected Client getClient() {
        return client("server1");
    }

    @Test public void testDfsQueryThenFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
        }
    }

    //

    @Test public void testDfsQueryThenFetchWithSort() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true).sort("age", false);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i + 60)));
        }
    }

    @Test public void testQueryThenFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
        }
    }

    @Test public void testQueryThenFetchWithSort() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true).sort("age", false);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i + 60)));
        }
    }

    @Test public void testQueryAndFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(QUERY_AND_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60)); // 20 per shard
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        // TODO support scrolling
//        searchResponse = searchScrollAction.submit(new SearchScrollRequest(searchResponse.scrollId())).actionGet();
//
//        assertEquals(100, searchResponse.hits().totalHits());
//        assertEquals(40, searchResponse.hits().hits().length);
//        for (int i = 0; i < 40; i++) {
//            SearchHit hit = searchResponse.hits().hits()[i];
//            assertEquals("id[" + hit.id() + "]", Integer.toString(100 - 60 - 1 - i), hit.id());
//        }
    }

    @Test public void testDfsQueryAndFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_AND_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60)); // 20 per shard
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.target() + ": " +  hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        // TODO support scrolling
//        searchResponse = searchScrollAction.submit(new SearchScrollRequest(searchResponse.scrollId())).actionGet();
//
//        assertEquals(100, searchResponse.hits().totalHits());
//        assertEquals(40, searchResponse.hits().hits().length);
//        for (int i = 0; i < 40; i++) {
//            SearchHit hit = searchResponse.hits().hits()[i];
//            assertEquals("id[" + hit.id() + "]", Integer.toString(100 - 60 - 1 - i), hit.id());
//        }
    }

    @Test public void testSimpleFacets() throws Exception {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true)
                .facets(facets().facet("all", termQuery("multi", "test")).facet("test1", termQuery("name", "test1")));

        SearchResponse searchResponse = client.search(searchRequest("test").source(sourceBuilder)).actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));

        assertThat(searchResponse.facets().countFacet("test1").count(), equalTo(1l));
        assertThat(searchResponse.facets().countFacet("all").count(), equalTo(100l));
    }

    @Test public void testSimpleFacetsTwice() throws Exception {
        testSimpleFacets();
        testSimpleFacets();
    }

    @Test public void testFailedSearch() throws Exception {
        logger.info("Start Testing failed search");
        SearchResponse searchResponse = client.search(searchRequest("test").source(Unicode.fromStringAsBytes("{ xxx }"))).actionGet();
        assertThat(searchResponse.successfulShards(), equalTo(0));
        logger.info("Failures:");
        for (ShardSearchFailure searchFailure : searchResponse.shardFailures()) {
            logger.info("Reason : " + searchFailure.reason() + ", shard " + searchFailure.shard());
        }
        logger.info("Done Testing failed search");
    }


    private void index(Client client, String id, String nameValue, int age) {
        client.index(Requests.indexRequest("test").type("type1").id(id).source(source(id, nameValue, age))).actionGet();
    }

    private String source(String id, String nameValue, int age) {
        StringBuilder multi = new StringBuilder().append(nameValue);
        for (int i = 0; i < age; i++) {
            multi.append(" ").append(nameValue);
        }
        return "{ type1 : { \"id\" : \"" + id + "\", \"name\" : \"" + (nameValue + id) + "\", age : " + age + ", multi : \"" + multi.toString() + "\", _boost : " + (age * 10) + " } }";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8777.java