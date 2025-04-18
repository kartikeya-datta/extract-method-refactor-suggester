error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/526.java
text:
```scala
s@@earchResponse = client.prepareSearch().setQuery(QueryBuilders.queryString("_id:XXX*").lowercaseExpandedTerms(false)).execute().actionGet();

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

package org.elasticsearch.test.integration.search.simple;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.test.integration.AbstractNodesTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimpleSearchTests extends AbstractNodesTests {

    private Client client;

    @BeforeClass
    public void createNodes() throws Exception {
        startNode("node1");
        client = getClient();
    }

    @AfterClass
    public void closeNodes() {
        client.close();
        closeAllNodes();
    }

    protected Client getClient() {
        return client("node1");
    }

    @Test
    public void simpleIpTests() throws Exception {
        client.admin().indices().prepareDelete().execute().actionGet();

        client.admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("number_of_shards", 1)).execute().actionGet();

        client.admin().indices().preparePutMapping("test").setType("type1")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                        .startObject("from").field("type", "ip").endObject()
                        .startObject("to").field("type", "ip").endObject()
                        .endObject().endObject().endObject())
                .execute().actionGet();

        client.prepareIndex("test", "type1", "1").setSource("from", "192.168.0.5", "to", "192.168.0.10").setRefresh(true).execute().actionGet();

        SearchResponse search = client.prepareSearch()
                .setQuery(boolQuery().must(rangeQuery("from").lt("192.168.0.7")).must(rangeQuery("to").gt("192.168.0.7")))
                .execute().actionGet();

        assertThat(search.hits().totalHits(), equalTo(1l));
    }

    @Test
    public void simpleIdTests() {
        client.admin().indices().prepareDelete().execute().actionGet();
        client.admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("number_of_shards", 1)).execute().actionGet();

        client.prepareIndex("test", "type", "XXX1").setSource("field", "value").setRefresh(true).execute().actionGet();
        // id is not indexed, but lets see that we automatically convert to
        SearchResponse searchResponse = client.prepareSearch().setQuery(QueryBuilders.termQuery("_id", "XXX1")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));

        searchResponse = client.prepareSearch().setQuery(QueryBuilders.queryString("_id:XXX1")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));

        // id is not index, but we can automatically support prefix as well
        searchResponse = client.prepareSearch().setQuery(QueryBuilders.prefixQuery("_id", "XXX")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));

        searchResponse = client.prepareSearch().setQuery(QueryBuilders.queryString("_id:XXX*")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));
    }

    @Test
    public void simpleDateRangeWithUpperInclusiveEnabledTests() throws Exception {
        client.admin().indices().prepareDelete().execute().actionGet();
        client.admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder()).execute().actionGet();
        client.prepareIndex("test", "type1", "1").setSource("field", "2010-01-05T02:00").execute().actionGet();
        client.prepareIndex("test", "type1", "2").setSource("field", "2010-01-06T02:00").execute().actionGet();
        client.admin().indices().prepareRefresh().execute().actionGet();

        // test include upper on ranges to include the full day on the upper bound
        SearchResponse searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.rangeQuery("field").gte("2010-01-05").lte("2010-01-06")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(2l));
        searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.rangeQuery("field").gte("2010-01-05").lt("2010-01-06")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));
    }

    @Test
    public void simpleDateRangeWithUpperInclusiveDisabledTests() throws Exception {
        client.admin().indices().prepareDelete().execute().actionGet();
        client.admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("index.mapping.date.parse_upper_inclusive", false)).execute().actionGet();
        client.prepareIndex("test", "type1", "1").setSource("field", "2010-01-05T02:00").execute().actionGet();
        client.prepareIndex("test", "type1", "2").setSource("field", "2010-01-06T02:00").execute().actionGet();
        client.admin().indices().prepareRefresh().execute().actionGet();

        // test include upper on ranges to include the full day on the upper bound (disabled here though...)
        SearchResponse searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.rangeQuery("field").gte("2010-01-05").lte("2010-01-06")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));
        searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.rangeQuery("field").gte("2010-01-05").lt("2010-01-06")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(1l));
    }

    @Test
    public void simpleDateMathTests() throws Exception {
        client.admin().indices().prepareDelete().execute().actionGet();
        client.admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder()).execute().actionGet();
        client.prepareIndex("test", "type1", "1").setSource("field", "2010-01-05T02:00").execute().actionGet();
        client.prepareIndex("test", "type1", "2").setSource("field", "2010-01-06T02:00").execute().actionGet();
        client.admin().indices().prepareRefresh().execute().actionGet();

        SearchResponse searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.rangeQuery("field").gte("2010-01-03||+2d").lte("2010-01-04||+2d")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(2l));

        searchResponse = client.prepareSearch("test").setQuery(QueryBuilders.queryString("field:[2010-01-03||+2d TO 2010-01-04||+2d]")).execute().actionGet();
        assertThat(searchResponse.hits().totalHits(), equalTo(2l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/526.java