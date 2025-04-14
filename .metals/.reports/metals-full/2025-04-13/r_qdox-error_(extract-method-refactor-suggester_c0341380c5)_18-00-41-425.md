error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9151.java
text:
```scala
d@@eleteByQueryRequestBuilder.setIndicesOptions(IndicesOptions.lenientExpandOpen());

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

package org.elasticsearch.deleteByQuery;

import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.deletebyquery.DeleteByQueryRequestBuilder;
import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.Matchers.*;

public class DeleteByQueryTests extends ElasticsearchIntegrationTest {

    @Test
    public void testDeleteAllNoIndices() {
        client().admin().indices().prepareRefresh().execute().actionGet();
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = client().prepareDeleteByQuery();
        deleteByQueryRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        deleteByQueryRequestBuilder.setIndicesOptions(IndicesOptions.fromOptions(false, true, true, false));
        DeleteByQueryResponse actionGet = deleteByQueryRequestBuilder.execute().actionGet();
        assertThat(actionGet.getIndices().size(), equalTo(0));
    }

    @Test
    public void testDeleteAllOneIndex() {
        String json = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\"," + "\"message\":\"trying out Elastic Search\"" + "}";
        final long iters = randomIntBetween(1, 50);
        for (int i = 0; i < iters; i++) {
            client().prepareIndex("twitter", "tweet", "" + i).setSource(json).execute().actionGet();
        }
        refresh();
        SearchResponse search = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        assertThat(search.getHits().totalHits(), equalTo(iters));
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = client().prepareDeleteByQuery();
        deleteByQueryRequestBuilder.setQuery(QueryBuilders.matchAllQuery());

        DeleteByQueryResponse actionGet = deleteByQueryRequestBuilder.execute().actionGet();
        assertThat(actionGet.status(), equalTo(RestStatus.OK));
        assertThat(actionGet.getIndex("twitter"), notNullValue());
        assertThat(actionGet.getIndex("twitter").getFailedShards(), equalTo(0));

        client().admin().indices().prepareRefresh().execute().actionGet();
        search = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        assertThat(search.getHits().totalHits(), equalTo(0l));
    }

    @Test
    public void testMissing() {

        String json = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\"," + "\"message\":\"trying out Elastic Search\"" + "}";

        client().prepareIndex("twitter", "tweet").setSource(json).setRefresh(true).execute().actionGet();

        SearchResponse search = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        assertThat(search.getHits().totalHits(), equalTo(1l));
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder = client().prepareDeleteByQuery();
        deleteByQueryRequestBuilder.setIndices("twitter", "missing");
        deleteByQueryRequestBuilder.setQuery(QueryBuilders.matchAllQuery());

        try {
            deleteByQueryRequestBuilder.execute().actionGet();
            fail("Exception should have been thrown.");
        } catch (IndexMissingException e) {
            //everything well
        }

        deleteByQueryRequestBuilder.setIndicesOptions(IndicesOptions.lenient());
        DeleteByQueryResponse actionGet = deleteByQueryRequestBuilder.execute().actionGet();
        assertThat(actionGet.status(), equalTo(RestStatus.OK));
        assertThat(actionGet.getIndex("twitter").getFailedShards(), equalTo(0));
        assertThat(actionGet.getIndex("twitter"), notNullValue());

        client().admin().indices().prepareRefresh().execute().actionGet();
        search = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        assertThat(search.getHits().totalHits(), equalTo(0l));
    }

    @Test
    public void testFailure() throws Exception {
        client().admin().indices().prepareCreate("twitter").execute().actionGet();

        DeleteByQueryResponse response = client().prepareDeleteByQuery("twitter")
                .setQuery(QueryBuilders.hasChildQuery("type", QueryBuilders.matchAllQuery()))
                .execute().actionGet();

        NumShards twitter = getNumShards("twitter");

        assertThat(response.status(), equalTo(RestStatus.BAD_REQUEST));
        assertThat(response.getIndex("twitter").getSuccessfulShards(), equalTo(0));
        assertThat(response.getIndex("twitter").getFailedShards(), equalTo(twitter.numPrimaries));
        assertThat(response.getIndices().size(), equalTo(1));
        assertThat(response.getIndices().get("twitter").getFailedShards(), equalTo(twitter.numPrimaries));
        assertThat(response.getIndices().get("twitter").getFailures().length, equalTo(twitter.numPrimaries));
        for (ShardOperationFailedException failure : response.getIndices().get("twitter").getFailures()) {
            assertThat(failure.reason(), containsString("[twitter] [has_child] unsupported in delete_by_query api"));
            assertThat(failure.status(), equalTo(RestStatus.BAD_REQUEST));
            assertThat(failure.shardId(), greaterThan(-1));
        }
    }

    @Test
    public void testDeleteByFieldQuery() throws Exception {
        client().admin().indices().prepareCreate("test").execute().actionGet();
        int numDocs = scaledRandomIntBetween(10, 100);
        for (int i = 0; i < numDocs; i++) {
            client().prepareIndex("test", "test", Integer.toString(i))
                    .setRouting(randomAsciiOfLengthBetween(1, 5))
                    .setSource("foo", "bar").get();
        }
        refresh();
        assertHitCount(client().prepareCount("test").setQuery(QueryBuilders.matchQuery("_id", Integer.toString(between(0, numDocs - 1)))).get(), 1);
        assertHitCount(client().prepareCount("test").setQuery(QueryBuilders.matchAllQuery()).get(), numDocs);
        client().prepareDeleteByQuery("test")
                .setQuery(QueryBuilders.matchQuery("_id", Integer.toString(between(0, numDocs - 1))))
                .execute().actionGet();
        refresh();
        assertHitCount(client().prepareCount("test").setQuery(QueryBuilders.matchAllQuery()).get(), numDocs - 1);

    }

    @Test
    public void testDateMath() throws Exception {
        index("test", "type", "1", "d", "2013-01-01");
        ensureGreen();
        refresh();
        assertHitCount(client().prepareCount("test").get(), 1);
        client().prepareDeleteByQuery("test").setQuery(QueryBuilders.rangeQuery("d").to("now-1h")).get();
        refresh();
        assertHitCount(client().prepareCount("test").get(), 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9151.java