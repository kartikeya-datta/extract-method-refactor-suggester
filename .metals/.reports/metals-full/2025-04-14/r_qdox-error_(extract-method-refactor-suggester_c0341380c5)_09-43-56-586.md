error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9895.java
text:
```scala
.@@facets(facets().queryFacet("all", termQuery("multi", "test"), true).queryFacet("test1", termQuery("name", "test1")));

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

import org.elasticsearch.ElasticSearchException;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.elasticsearch.util.Unicode;
import org.elasticsearch.util.collect.Sets;
import org.elasticsearch.util.xcontent.builder.XContentBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import static org.elasticsearch.action.search.SearchType.*;
import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.elasticsearch.util.TimeValue.*;
import static org.elasticsearch.util.xcontent.XContentFactory.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (shay.banon)
 */
public class TransportTwoServersSearchTests extends AbstractNodesTests {

    private Client client;

    @BeforeClass public void createNodes() throws Exception {
        startNode("server1");
        startNode("server2");
        client = getClient();

        client.admin().indices().create(createIndexRequest("test")).actionGet();

        for (int i = 0; i < 100; i++) {
            index(client("server1"), Integer.toString(i), "test", i);
        }
        client.admin().indices().refresh(refreshRequest("test")).actionGet();
    }

    @AfterClass public void closeServers() {
        client.close();
        closeAllNodes();
    }

    protected Client getClient() {
        return client("server1");
    }

    @Test public void testDfsQueryThenFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
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

    @Test public void testDfsQueryThenFetchWithSort() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true).sort("age", false);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_THEN_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
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
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
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
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
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
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60)); // 20 per shard
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        Set<String> expectedIds = Sets.newHashSet();
        for (int i = 0; i < 40; i++) {
            expectedIds.add(Integer.toString(i));
        }
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
            // we don't do perfect sorting when it comes to scroll with Query+Fetch
            assertThat("make sure we don't have duplicates", expectedIds.remove(hit.id()), notNullValue());
        }
        assertThat("make sure we got all [" + expectedIds + "]", expectedIds.size(), equalTo(0));
    }

    @Test public void testDfsQueryAndFetch() throws Exception {
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true);

        SearchResponse searchResponse = client.search(searchRequest("test").source(source).searchType(DFS_QUERY_AND_FETCH).scroll(new Scroll(timeValueMinutes(10)))).actionGet();
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(60)); // 20 per shard
        for (int i = 0; i < 60; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
            assertThat(hit.explanation(), notNullValue());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        searchResponse = client.searchScroll(searchScrollRequest(searchResponse.scrollId())).actionGet();

        assertThat(searchResponse.hits().totalHits(), equalTo(100l));
        assertThat(searchResponse.hits().hits().length, equalTo(40));
        Set<String> expectedIds = Sets.newHashSet();
        for (int i = 0; i < 40; i++) {
            expectedIds.add(Integer.toString(i));
        }
        for (int i = 0; i < 40; i++) {
            SearchHit hit = searchResponse.hits().hits()[i];
//            System.out.println(hit.shard() + ": " +  hit.explanation());
//            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
            // we don't do perfect sorting when it comes to scroll with Query+Fetch
            assertThat("make sure we don't have duplicates", expectedIds.remove(hit.id()), notNullValue());
        }
        assertThat("make sure we got all [" + expectedIds + "]", expectedIds.size(), equalTo(0));
    }

    @Test public void testSimpleFacets() throws Exception {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true)
                .facets(facets().facet("all", termQuery("multi", "test"), true).facet("test1", termQuery("name", "test1")));

        SearchResponse searchResponse = client.search(searchRequest("test").source(sourceBuilder)).actionGet();
        assertThat("Failures " + Arrays.toString(searchResponse.shardFailures()), searchResponse.shardFailures().length, equalTo(0));
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));

        assertThat(searchResponse.facets().countFacet("test1").count(), equalTo(1l));
        assertThat(searchResponse.facets().countFacet("all").count(), equalTo(100l));
    }

    @Test public void testSimpleFacetsTwice() throws Exception {
        testSimpleFacets();
        testSimpleFacets();
    }

    @Test public void testFailedSearchWithWrongQuery() throws Exception {
        logger.info("Start Testing failed search with wrong query");
        try {
            SearchResponse searchResponse = client.search(searchRequest("test").source(Unicode.fromStringAsBytes("{ xxx }"))).actionGet();
            assertThat(searchResponse.totalShards(), equalTo(3));
            assertThat(searchResponse.successfulShards(), equalTo(0));
            assertThat(searchResponse.failedShards(), equalTo(3));
            assert false : "search should fail";
        } catch (ElasticSearchException e) {
            assertThat(e.unwrapCause(), instanceOf(SearchPhaseExecutionException.class));
            // all is well
        }
        logger.info("Done Testing failed search");
    }

    @Test public void testFailedSearchWithWrongFrom() throws Exception {
        logger.info("Start Testing failed search with wrong from");
        SearchSourceBuilder source = searchSource()
                .query(termQuery("multi", "test"))
                .from(1000).size(20).explain(true);
        SearchResponse response = client.search(searchRequest("test").searchType(DFS_QUERY_AND_FETCH).source(source)).actionGet();
        assertThat(response.hits().hits().length, equalTo(0));
        assertThat(response.totalShards(), equalTo(3));
        assertThat(response.successfulShards(), equalTo(3));
        assertThat(response.failedShards(), equalTo(0));

        response = client.search(searchRequest("test").searchType(QUERY_THEN_FETCH).source(source)).actionGet();
        assertThat(response.shardFailures().length, equalTo(0));
        assertThat(response.hits().hits().length, equalTo(0));

        response = client.search(searchRequest("test").searchType(DFS_QUERY_AND_FETCH).source(source)).actionGet();
        assertThat(response.shardFailures().length, equalTo(0));
        assertThat(response.hits().hits().length, equalTo(0));

        response = client.search(searchRequest("test").searchType(DFS_QUERY_THEN_FETCH).source(source)).actionGet();
        assertThat(response.shardFailures().length, equalTo(0));
        assertThat(response.hits().hits().length, equalTo(0));

        logger.info("Done Testing failed search");
    }

    private void index(Client client, String id, String nameValue, int age) throws IOException {
        client.index(Requests.indexRequest("test").type("type1").id(id).source(source(id, nameValue, age))).actionGet();
    }

    private XContentBuilder source(String id, String nameValue, int age) throws IOException {
        StringBuilder multi = new StringBuilder().append(nameValue);
        for (int i = 0; i < age; i++) {
            multi.append(" ").append(nameValue);
        }
        return jsonBuilder().startObject()
                .field("id", id)
                .field("name", nameValue + id)
                .field("age", age)
                .field("multi", multi.toString())
                .field("_boost", age * 10)
                .endObject();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9895.java