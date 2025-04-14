error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7084.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7084.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7084.java
text:
```scala
r@@eturn new InternalSearchRequest(shardRouting).source(builder.buildAsBytes());

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
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.routing.ShardRouting;
import org.elasticsearch.cluster.routing.ShardsIterator;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.node.internal.InternalNode;
import org.elasticsearch.search.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.controller.SearchPhaseController;
import org.elasticsearch.search.controller.ShardDoc;
import org.elasticsearch.search.dfs.AggregatedDfs;
import org.elasticsearch.search.dfs.DfsSearchResult;
import org.elasticsearch.search.fetch.FetchSearchRequest;
import org.elasticsearch.search.fetch.FetchSearchResult;
import org.elasticsearch.search.fetch.QueryFetchSearchResult;
import org.elasticsearch.search.internal.InternalScrollSearchRequest;
import org.elasticsearch.search.internal.InternalSearchRequest;
import org.elasticsearch.search.internal.InternalSearchResponse;
import org.elasticsearch.search.query.QuerySearchRequest;
import org.elasticsearch.search.query.QuerySearchResult;
import org.elasticsearch.search.query.QuerySearchResultProvider;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.elasticsearch.util.TimeValue;
import org.elasticsearch.util.collect.ImmutableMap;
import org.elasticsearch.util.collect.Sets;
import org.elasticsearch.util.trove.ExtTIntArrayList;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.client.Requests.*;
import static org.elasticsearch.index.query.xcontent.QueryBuilders.*;
import static org.elasticsearch.search.builder.SearchSourceBuilder.*;
import static org.elasticsearch.util.TimeValue.*;
import static org.elasticsearch.util.collect.Lists.*;
import static org.elasticsearch.util.collect.Maps.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author kimchy (Shay Banon)
 */
public class TwoInstanceEmbeddedSearchTests extends AbstractNodesTests {

    private IndicesService indicesService;

    private ClusterService clusterService;

    private Map<String, SearchService> nodeToSearchService;

    private SearchPhaseController searchPhaseController;

    @BeforeClass public void createNodeAndInitWithData() throws Exception {
        startNode("server1");
        startNode("server2");

        clusterService = ((InternalNode) node("server1")).injector().getInstance(ClusterService.class);
        indicesService = ((InternalNode) node("server1")).injector().getInstance(IndicesService.class);
        client("server1").admin().indices().create(Requests.createIndexRequest("test")).actionGet();

        for (int i = 0; i < 100; i++) {
            index(client("server1"), Integer.toString(i), "test", i);
        }
        client("server1").admin().indices().refresh(refreshRequest("test")).actionGet();

        SearchService searchService1 = ((InternalNode) node("server1")).injector().getInstance(SearchService.class);
        SearchService searchService2 = ((InternalNode) node("server2")).injector().getInstance(SearchService.class);

        nodeToSearchService = ImmutableMap.<String, SearchService>builder()
                .put(((InternalNode) node("server1")).injector().getInstance(ClusterService.class).state().nodes().localNodeId(), searchService1)
                .put(((InternalNode) node("server2")).injector().getInstance(ClusterService.class).state().nodes().localNodeId(), searchService2)
                .build();

        searchPhaseController = ((InternalNode) node("server1")).injector().getInstance(SearchPhaseController.class);
    }

    @AfterClass public void closeServers() {
        closeAllNodes();
    }

    @Test public void testDfsQueryThenFetch() throws Exception {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true).indexBoost("test", 1.0f).indexBoost("test2", 2.0f);

        List<DfsSearchResult> dfsResults = newArrayList();
        for (ShardsIterator shardsIt : indicesService.searchShards(clusterService.state(), new String[]{"test"}, null)) {
            for (ShardRouting shardRouting : shardsIt) {
                InternalSearchRequest searchRequest = searchRequest(shardRouting, sourceBuilder)
                        .scroll(new Scroll(new TimeValue(10, TimeUnit.MINUTES)));
                dfsResults.add(nodeToSearchService.get(shardRouting.currentNodeId()).executeDfsPhase(searchRequest));
            }
        }

        AggregatedDfs dfs = searchPhaseController.aggregateDfs(dfsResults);
        Map<SearchShardTarget, QuerySearchResultProvider> queryResults = newHashMap();
        for (DfsSearchResult dfsResult : dfsResults) {
            queryResults.put(dfsResult.shardTarget(), nodeToSearchService.get(dfsResult.shardTarget().nodeId()).executeQueryPhase(new QuerySearchRequest(dfsResult.id(), dfs)));
        }

        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        Map<SearchShardTarget, FetchSearchResult> fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        SearchHits hits = searchPhaseController.merge(sortedShardList, queryResults, fetchResults).hits();

        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = hits.hits()[i];
//            System.out.println(hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        // now try and scroll to the next batch of results
        Map<SearchShardTarget, QuerySearchResultProvider> scrollQueryResults = newHashMap();
        for (QuerySearchResultProvider queryResult : queryResults.values()) {
            scrollQueryResults.put(queryResult.queryResult().shardTarget(), nodeToSearchService.get(queryResult.shardTarget().nodeId()).executeQueryPhase(new InternalScrollSearchRequest(queryResult.id())));
        }
        queryResults = scrollQueryResults;

        sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        hits = searchPhaseController.merge(sortedShardList, queryResults, fetchResults).hits();
        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = hits.hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
        }
    }

    @Test public void testDfsQueryThenFetchWithSort() throws Exception {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(60).explain(true).sort("age", false);

        List<DfsSearchResult> dfsResults = newArrayList();
        for (ShardsIterator shardsIt : indicesService.searchShards(clusterService.state(), new String[]{"test"}, null)) {
            for (ShardRouting shardRouting : shardsIt) {
                InternalSearchRequest searchRequest = searchRequest(shardRouting, sourceBuilder)
                        .scroll(new Scroll(new TimeValue(10, TimeUnit.MINUTES)));
                dfsResults.add(nodeToSearchService.get(shardRouting.currentNodeId()).executeDfsPhase(searchRequest));
            }
        }

        AggregatedDfs dfs = searchPhaseController.aggregateDfs(dfsResults);
        Map<SearchShardTarget, QuerySearchResultProvider> queryResults = newHashMap();
        for (DfsSearchResult dfsResult : dfsResults) {
            queryResults.put(dfsResult.shardTarget(), nodeToSearchService.get(dfsResult.shardTarget().nodeId()).executeQueryPhase(new QuerySearchRequest(dfsResult.id(), dfs)));
        }

        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        Map<SearchShardTarget, FetchSearchResult> fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        SearchHits hits = searchPhaseController.merge(sortedShardList, queryResults, fetchResults).hits();
        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(60));
        for (int i = 0; i < 60; i++) {
            SearchHit hit = hits.hits()[i];
//            System.out.println(hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i)));
        }

        // now try and scroll to the next batch of results
        Map<SearchShardTarget, QuerySearchResultProvider> scrollQueryResults = newHashMap();
        for (QuerySearchResultProvider queryResult : queryResults.values()) {
            scrollQueryResults.put(queryResult.queryResult().shardTarget(), nodeToSearchService.get(queryResult.shardTarget().nodeId()).executeQueryPhase(new InternalScrollSearchRequest(queryResult.id()).scroll(new Scroll(timeValueMinutes(10)))));
        }
        queryResults = scrollQueryResults;

        sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        hits = searchPhaseController.merge(sortedShardList, queryResults, fetchResults).hits();
        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(40));
        for (int i = 0; i < 40; i++) {
            SearchHit hit = hits.hits()[i];
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(i + 60)));
        }

        // now try and scroll to the next next batch of results
        scrollQueryResults = newHashMap();
        for (QuerySearchResultProvider queryResult : queryResults.values()) {
            scrollQueryResults.put(queryResult.queryResult().shardTarget(), nodeToSearchService.get(queryResult.shardTarget().nodeId()).executeQueryPhase(new InternalScrollSearchRequest(queryResult.id())));
        }
        queryResults = scrollQueryResults;

        sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        hits = searchPhaseController.merge(sortedShardList, queryResults, fetchResults).hits();
        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(0));
    }

    @Test public void testQueryAndFetch() {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true);

        Map<SearchShardTarget, QueryFetchSearchResult> queryFetchResults = newHashMap();
        for (ShardsIterator shardsIt : indicesService.searchShards(clusterService.state(), new String[]{"test"}, null)) {
            for (ShardRouting shardRouting : shardsIt) {
                InternalSearchRequest searchRequest = searchRequest(shardRouting, sourceBuilder)
                        .scroll(new Scroll(new TimeValue(10, TimeUnit.MINUTES)));
                QueryFetchSearchResult queryFetchResult = nodeToSearchService.get(shardRouting.currentNodeId()).executeFetchPhase(searchRequest);
                queryFetchResults.put(queryFetchResult.shardTarget(), queryFetchResult);
            }
        }


        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(queryFetchResults.values());
        SearchHits hits = searchPhaseController.merge(sortedShardList, queryFetchResults, queryFetchResults).hits();

        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(60)); // 60 results, with size 20, since we have 3 shards
        for (int i = 0; i < 60; i++) {
            SearchHit hit = hits.hits()[i];
//            System.out.println(hit.id() + " " + hit.explanation());
            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - i - 1)));
        }

        // scrolling with query+fetch is not perfect when it comes to dist sorting
        Map<SearchShardTarget, QueryFetchSearchResult> scrollQueryFetchResults = newHashMap();
        for (QueryFetchSearchResult searchResult : queryFetchResults.values()) {
            QueryFetchSearchResult queryFetchResult = nodeToSearchService.get(searchResult.shardTarget().nodeId()).executeFetchPhase(new InternalScrollSearchRequest(searchResult.id()).scroll(new Scroll(timeValueMinutes(10))));
            scrollQueryFetchResults.put(queryFetchResult.shardTarget(), queryFetchResult);
        }
        queryFetchResults = scrollQueryFetchResults;

        sortedShardList = searchPhaseController.sortDocs(queryFetchResults.values());
        hits = searchPhaseController.merge(sortedShardList, queryFetchResults, queryFetchResults).hits();
        assertThat(hits.totalHits(), equalTo(100l));
        assertThat(hits.hits().length, equalTo(40));
        Set<String> expectedIds = Sets.newHashSet();
        for (int i = 0; i < 40; i++) {
            expectedIds.add(Integer.toString(i));
        }
        for (int i = 0; i < 40; i++) {
            SearchHit hit = hits.hits()[i];
//            System.out.println(hit.id() + " " + hit.explanation());
            // we don't do perfect sorting when it comes to scroll with Query+Fetch
//            assertThat("id[" + hit.id() + "]", hit.id(), equalTo(Integer.toString(100 - 60 - 1 - i)));
            assertThat("make sure we don't have duplicates", expectedIds.remove(hit.id()), notNullValue());
        }
        assertThat("make sure we got all [" + expectedIds + "]", expectedIds.size(), equalTo(0));
    }

    @Test public void testSimpleFacets() {
        SearchSourceBuilder sourceBuilder = searchSource()
                .query(termQuery("multi", "test"))
                .from(0).size(20).explain(true).sort("age", false)
                .facets(facets().facet("all", termQuery("multi", "test")).facet("test1", termQuery("name", "test1")));

        Map<SearchShardTarget, QuerySearchResultProvider> queryResults = newHashMap();
        for (ShardsIterator shardsIt : indicesService.searchShards(clusterService.state(), new String[]{"test"}, null)) {
            for (ShardRouting shardRouting : shardsIt) {
                InternalSearchRequest searchRequest = searchRequest(shardRouting, sourceBuilder)
                        .scroll(new Scroll(new TimeValue(10, TimeUnit.MINUTES)));
                QuerySearchResult queryResult = nodeToSearchService.get(shardRouting.currentNodeId()).executeQueryPhase(searchRequest);
                queryResults.put(queryResult.shardTarget(), queryResult);
            }
        }
        ShardDoc[] sortedShardList = searchPhaseController.sortDocs(queryResults.values());
        Map<SearchShardTarget, ExtTIntArrayList> docIdsToLoad = searchPhaseController.docIdsToLoad(sortedShardList);

        Map<SearchShardTarget, FetchSearchResult> fetchResults = newHashMap();
        for (Map.Entry<SearchShardTarget, ExtTIntArrayList> entry : docIdsToLoad.entrySet()) {
            SearchShardTarget shardTarget = entry.getKey();
            ExtTIntArrayList docIds = entry.getValue();
            FetchSearchResult fetchResult = nodeToSearchService.get(shardTarget.nodeId()).executeFetchPhase(new FetchSearchRequest(queryResults.get(shardTarget).queryResult().id(), docIds));
            fetchResults.put(fetchResult.shardTarget(), fetchResult.initCounter());
        }

        InternalSearchResponse searchResponse = searchPhaseController.merge(sortedShardList, queryResults, fetchResults);
        assertThat(searchResponse.hits().totalHits(), equalTo(100l));

        assertThat(searchResponse.facets().countFacet("test1").count(), equalTo(1l));
        assertThat(searchResponse.facets().countFacet("all").count(), equalTo(100l));
    }

    @Test public void testSimpleFacetsTwice() {
        testSimpleFacets();
        testSimpleFacets();
    }

    private InternalSearchRequest searchRequest(ShardRouting shardRouting, SearchSourceBuilder builder) {
        return new InternalSearchRequest(shardRouting, builder.buildAsBytes());
    }

    private void index(Client client, String id, String nameValue, int age) {
        client.index(indexRequest("test").type("type1").id(id).source(source(id, nameValue, age))).actionGet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7084.java