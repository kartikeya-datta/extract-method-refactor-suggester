error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7083.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7083.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7083.java
text:
```scala
r@@eturn new InternalSearchRequest("test", 0).source(builder.buildAsBytes());

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

import org.elasticsearch.client.Client;
import org.elasticsearch.node.internal.InternalNode;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchContextMissingException;
import org.elasticsearch.search.SearchService;
import org.elasticsearch.search.SearchShardTarget;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.controller.ShardDoc;
import org.elasticsearch.search.dfs.AggregatedDfs;
import org.elasticsearch.search.dfs.DfsSearchResult;
import org.elasticsearch.search.fetch.FetchSearchRequest;
import org.elasticsearch.search.fetch.FetchSearchResult;
import org.elasticsearch.search.fetch.QueryFetchSearchResult;
import org.elasticsearch.search.internal.InternalSearchRequest;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.elasticsearch.search.query.QuerySearchResult;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.trove.ExtTIntArrayList;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.elasticsearch.util.collect.Lists.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public class SingleInstanceEmbeddedSearchTests extends AbstractNodesTests {

    private SearchService searchService;

    private SearchPhaseController searchPhaseController;

    @BeforeClass public void createNodeAndInitWithData() throws Exception {
        startNode("server1");

        client("server1").admin().indices().create(createIndexRequest("test")).actionGet();
        index(client("server1"), "1", "test1", 1);
        index(client("server1"), "2", "test2", 2);
        index(client("server1"), "3", "test3", 2);
        index(client("server1"), "4", "test4", 2);
        index(client("server1"), "5", "test5", 2);
        client("server1").admin().indices().refresh(refreshRequest("test")).actionGet();

        searchService = ((InternalNode) node("server1")).injector().getInstance(SearchService.class);
        searchPhaseController = ((InternalNode) node("server1")).injector().getInstance(SearchPhaseController.class);
    }

    @AfterClass public void closeNode() {
        closeAllNodes();
    }

    @Test public void testDirectDfs() throws Exception {
        DfsSearchResult dfsResult = searchService.executeDfsPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));

        assertThat(dfsResult.terms().length, equalTo(1));
        assertThat(dfsResult.freqs().length, equalTo(1));
        assertThat(dfsResult.terms()[0].field(), equalTo("name"));
        assertThat(dfsResult.terms()[0].text(), equalTo("test1"));
        assertThat(dfsResult.freqs()[0], equalTo(1));
    }

    @Test public void testDirectQuery() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));
        assertThat(queryResult.topDocs().totalHits, equalTo(1));
    }

    @Test public void testDirectFetch() throws Exception {
        QueryFetchSearchResult queryFetchResult = searchService.executeFetchPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));
        assertThat(queryFetchResult.queryResult().topDocs().totalHits, equalTo(1));
        assertThat(queryFetchResult.fetchResult().hits().hits().length, equalTo(1));
        assertThat(queryFetchResult.fetchResult().hits().hits()[0].sourceAsString(), equalTo(source("1", "test1", 1)));
        assertThat(queryFetchResult.fetchResult().hits().hits()[0].id(), equalTo("1"));
        assertThat(queryFetchResult.fetchResult().hits().hits()[0].type(), equalTo("type1"));
    }

    @Test public void testQueryThenFetch() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));
        assertThat(queryResult.topDocs().totalHits, equalTo(1));

        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(newArrayList(queryResult));
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);
        assertThat(docIdsToLoad.size(), equalTo(1));
        assertThat(docIdsToLoad.values().iterator().next().size(), equalTo(1));

        FetchSearchResult fetchResult = searchService.executeFetchPhase(new FetchSearchRequest(queryResult.id(), docIdsToLoad.values().iterator().next()));
        assertThat(fetchResult.hits().hits()[0].sourceAsString(), equalTo(source("1", "test1", 1)));
        assertThat(fetchResult.hits().hits()[0].id(), equalTo("1"));
        assertThat(fetchResult.hits().hits()[0].type(), equalTo("type1"));
    }

    @Test public void testQueryAndFetch() throws Exception {
        QueryFetchSearchResult result = searchService.executeFetchPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));
        FetchSearchResult fetchResult = result.fetchResult();
        assertThat(fetchResult.hits().hits()[0].sourceAsString(), equalTo(source("1", "test1", 1)));
        assertThat(fetchResult.hits().hits()[0].id(), equalTo("1"));
        assertThat(fetchResult.hits().hits()[0].type(), equalTo("type1"));
    }

    @Test public void testDfsQueryThenFetch() throws Exception {
        DfsSearchResult dfsResult = searchService.executeDfsPhase(searchRequest(searchSource().query(termQuery("name", "test1"))));
        AggregatedDfs dfs = searchPhaseController.aggregateDfs(newArrayList(dfsResult));

        QuerySearchResult queryResult = searchService.executeQueryPhase(new QuerySearchRequest(dfsResult.id(), dfs));
        assertThat(queryResult.topDocs().totalHits, equalTo(1));

        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(newArrayList(queryResult));
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);
        assertThat(docIdsToLoad.size(), equalTo(1));
        assertThat(docIdsToLoad.values().iterator().next().size(), equalTo(1));

        FetchSearchResult fetchResult = searchService.executeFetchPhase(new FetchSearchRequest(queryResult.id(), docIdsToLoad.values().iterator().next()));
        assertThat(fetchResult.hits().hits()[0].sourceAsString(), equalTo(source("1", "test1", 1)));
        assertThat(fetchResult.hits().hits()[0].id(), equalTo("1"));
        assertThat(fetchResult.hits().hits()[0].type(), equalTo("type1"));
    }

    @Test public void testSimpleQueryFacetsNoExecutionType() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(
                searchSource().query(wildcardQuery("name", "te*"))
                        .facets(facets().facet("age2", termQuery("age", 2)).facet("age1", termQuery("age", 1)))
        ));
        assertThat(queryResult.facets().countFacet("age2").count(), equalTo(4l));
        assertThat(queryResult.facets().countFacet("age1").count(), equalTo(1l));
    }

    @Test public void testSimpleQueryFacetsQueryExecutionCollect() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(
                searchSource().query(wildcardQuery("name", "te*"))
                        .facets(facets().queryExecution("collect").facet("age2", termQuery("age", 2)).facet("age1", termQuery("age", 1)))
        ));
        assertThat(queryResult.facets().countFacet("age2").count(), equalTo(4l));
        assertThat(queryResult.facets().countFacet("age1").count(), equalTo(1l));
    }

    @Test public void testSimpleQueryFacetsQueryExecutionIdset() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(
                searchSource().query(wildcardQuery("name", "te*"))
                        .facets(facets().queryExecution("idset").facet("age2", termQuery("age", 2)).facet("age1", termQuery("age", 1)))
        ));
        assertThat(queryResult.facets().countFacet("age2").count(), equalTo(4l));
        assertThat(queryResult.facets().countFacet("age1").count(), equalTo(1l));
    }

    @Test public void testQueryFetchKeepAliveTimeout() throws Exception {
        QuerySearchResult queryResult = searchService.executeQueryPhase(searchRequest(searchSource().query(termQuery("name", "test1"))).scroll(new Scroll(TimeValue.timeValueMillis(10))));
        assertThat(queryResult.topDocs().totalHits, equalTo(1));

        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(newArrayList(queryResult));
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);
        assertThat(docIdsToLoad.size(), equalTo(1));
        assertThat(docIdsToLoad.values().iterator().next().size(), equalTo(1));

        // sleep more than the 100ms the timeout wheel it set to
        Thread.sleep(300);

        try {
            searchService.executeFetchPhase(new FetchSearchRequest(queryResult.id(), docIdsToLoad.values().iterator().next()));
            assert true : "context should be missing since it timed out";
        } catch (SearchContextMissingException e) {
            // all is well
        }
    }


    private InternalSearchRequest searchRequest(SearchSourceBuilder builder) {
        return new InternalSearchRequest("test", 0, builder.buildAsBytes());
    }

    private void index(Client client, String id, String nameValue, int age) {
        client.index(indexRequest("test").type("type1").id(id).source(source(id, nameValue, age))).actionGet();
    }

    private String source(String id, String nameValue, int age) {
        return "{ type1 : { \"id\" : \"" + id + "\", \"name\" : \"" + nameValue + "\", age : " + age + " } }";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7083.java