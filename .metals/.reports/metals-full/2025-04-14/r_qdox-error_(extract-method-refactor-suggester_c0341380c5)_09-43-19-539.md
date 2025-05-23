error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/972.java
text:
```scala
Q@@ueryBuilders.matchQuery("foo", "1")

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.validate;

import com.google.common.base.Charsets;
import org.elasticsearch.action.admin.indices.validate.query.ValidateQueryResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.Priority;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.index.query.QueryBuilders.queryString;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.*;
import static org.hamcrest.Matchers.*;

/**
 *
 */
public class SimpleValidateQueryTests extends ElasticsearchIntegrationTest {

    @Test
    public void simpleValidateQuery() throws Exception {

        client().admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1)).execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();
        client().admin().indices().preparePutMapping("test").setType("type1")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                        .startObject("foo").field("type", "string").endObject()
                        .startObject("bar").field("type", "integer").endObject()
                        .endObject().endObject().endObject())
                .execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();

        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery("foo".getBytes(Charsets.UTF_8)).execute().actionGet().isValid(), equalTo(false));
        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("_id:1")).execute().actionGet().isValid(), equalTo(true));
        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("_i:d:1")).execute().actionGet().isValid(), equalTo(false));

        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("foo:1")).execute().actionGet().isValid(), equalTo(true));
        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("bar:hey")).execute().actionGet().isValid(), equalTo(false));

        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("nonexistent:hello")).execute().actionGet().isValid(), equalTo(true));

        assertThat(client().admin().indices().prepareValidateQuery("test").setQuery(QueryBuilders.queryString("foo:1 AND")).execute().actionGet().isValid(), equalTo(false));
    }

    @Test
    public void explainValidateQuery() throws Exception {

        client().admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1)).execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();
        client().admin().indices().preparePutMapping("test").setType("type1")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                        .startObject("foo").field("type", "string").endObject()
                        .startObject("bar").field("type", "integer").endObject()
                        .startObject("baz").field("type", "string").field("analyzer", "snowball").endObject()
                        .startObject("pin").startObject("properties").startObject("location").field("type", "geo_point").endObject().endObject().endObject()
                        .endObject().endObject().endObject())
                .execute().actionGet();
        client().admin().indices().preparePutMapping("test").setType("child-type")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("child-type")
                        .startObject("_parent").field("type", "type1").endObject()
                        .startObject("properties")
                        .startObject("foo").field("type", "string").endObject()
                        .endObject()
                        .endObject().endObject())
                .execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();


        ValidateQueryResponse response;
        response = client().admin().indices().prepareValidateQuery("test")
                .setQuery("foo".getBytes(Charsets.UTF_8))
                .setExplain(true)
                .execute().actionGet();
        assertThat(response.isValid(), equalTo(false));
        assertThat(response.getQueryExplanation().size(), equalTo(1));
        assertThat(response.getQueryExplanation().get(0).getError(), containsString("Failed to parse"));
        assertThat(response.getQueryExplanation().get(0).getExplanation(), nullValue());

        assertExplanation(QueryBuilders.queryString("_id:1"), equalTo("ConstantScore(_uid:type1#1)"));

        assertExplanation(QueryBuilders.idsQuery("type1").addIds("1").addIds("2"),
                equalTo("ConstantScore(_uid:type1#1 _uid:type1#2)"));

        assertExplanation(QueryBuilders.queryString("foo"), equalTo("_all:foo"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.termQuery("foo", "1"),
                FilterBuilders.orFilter(
                        FilterBuilders.termFilter("bar", "2"),
                        FilterBuilders.termFilter("baz", "3")
                )
        ), equalTo("filtered(foo:1)->cache(bar:[2 TO 2]) cache(baz:3)"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.termQuery("foo", "1"),
                FilterBuilders.orFilter(
                        FilterBuilders.termFilter("bar", "2")
                )
        ), equalTo("filtered(foo:1)->cache(bar:[2 TO 2])"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.matchAllQuery(),
                FilterBuilders.geoPolygonFilter("pin.location")
                        .addPoint(40, -70)
                        .addPoint(30, -80)
                        .addPoint(20, -90)
        ), equalTo("ConstantScore(GeoPolygonFilter(pin.location, [[40.0, -70.0], [30.0, -80.0], [20.0, -90.0]]))"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.geoBoundingBoxFilter("pin.location")
                .topLeft(40, -80)
                .bottomRight(20, -70)
        ), equalTo("ConstantScore(GeoBoundingBoxFilter(pin.location, [40.0, -80.0], [20.0, -70.0]))"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.geoDistanceFilter("pin.location")
                .lat(10).lon(20).distance(15, DistanceUnit.MILES).geoDistance(GeoDistance.PLANE)
        ), equalTo("ConstantScore(GeoDistanceFilter(pin.location, PLANE, 15.0, 10.0, 20.0))"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.geoDistanceFilter("pin.location")
                .lat(10).lon(20).distance(15, DistanceUnit.MILES).geoDistance(GeoDistance.PLANE)
        ), equalTo("ConstantScore(GeoDistanceFilter(pin.location, PLANE, 15.0, 10.0, 20.0))"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.geoDistanceRangeFilter("pin.location")
                .lat(10).lon(20).from("15miles").to("25miles").geoDistance(GeoDistance.PLANE)
        ), equalTo("ConstantScore(GeoDistanceRangeFilter(pin.location, PLANE, [15.0 - 25.0], 10.0, 20.0))"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.termQuery("foo", "1"),
                FilterBuilders.andFilter(
                        FilterBuilders.termFilter("bar", "2"),
                        FilterBuilders.termFilter("baz", "3")
                )
        ), equalTo("filtered(foo:1)->+cache(bar:[2 TO 2]) +cache(baz:3)"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.termsFilter("foo", "1", "2", "3")),
                equalTo("ConstantScore(cache(foo:1 foo:2 foo:3))"));

        assertExplanation(QueryBuilders.constantScoreQuery(FilterBuilders.notFilter(FilterBuilders.termFilter("foo", "bar"))),
                equalTo("ConstantScore(NotFilter(cache(foo:bar)))"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.termQuery("foo", "1"),
                FilterBuilders.hasChildFilter(
                        "child-type",
                        QueryBuilders.fieldQuery("foo", "1")
                )
        ), equalTo("filtered(foo:1)->CustomQueryWrappingFilter(child_filter[child-type/type1](filtered(foo:1)->cache(_type:child-type)))"));

        assertExplanation(QueryBuilders.filteredQuery(
                QueryBuilders.termQuery("foo", "1"),
                FilterBuilders.scriptFilter("true")
        ), equalTo("filtered(foo:1)->ScriptFilter(true)"));

    }

    @Test
    public void explainValidateQueryTwoNodes() throws IOException {

        client().admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)).execute().actionGet();
        client().admin().cluster().prepareHealth().setWaitForEvents(Priority.LANGUID).setWaitForGreenStatus().execute().actionGet();
        client().admin().indices().preparePutMapping("test").setType("type1")
                .setSource(XContentFactory.jsonBuilder().startObject().startObject("type1").startObject("properties")
                        .startObject("foo").field("type", "string").endObject()
                        .startObject("bar").field("type", "integer").endObject()
                        .startObject("baz").field("type", "string").field("analyzer", "snowball").endObject()
                        .startObject("pin").startObject("properties").startObject("location").field("type", "geo_point").endObject().endObject().endObject()
                        .endObject().endObject().endObject())
                .execute().actionGet();

        client().admin().indices().prepareRefresh().execute().actionGet();


        
        for (Client client : cluster()) {
            ValidateQueryResponse response = client.admin().indices().prepareValidateQuery("test")
                    .setQuery("foo".getBytes(Charsets.UTF_8))
                    .setExplain(true)
                    .execute().actionGet();
            assertThat(response.isValid(), equalTo(false));
            assertThat(response.getQueryExplanation().size(), equalTo(1));
            assertThat(response.getQueryExplanation().get(0).getError(), containsString("Failed to parse"));
            assertThat(response.getQueryExplanation().get(0).getExplanation(), nullValue());

        }
        
        for (Client client : cluster()) {
                ValidateQueryResponse response = client.admin().indices().prepareValidateQuery("test")
                    .setQuery(QueryBuilders.queryString("foo"))
                    .setExplain(true)
                    .execute().actionGet();
            assertThat(response.isValid(), equalTo(true));
            assertThat(response.getQueryExplanation().size(), equalTo(1));
            assertThat(response.getQueryExplanation().get(0).getExplanation(), equalTo("_all:foo"));
            assertThat(response.getQueryExplanation().get(0).getError(), nullValue());
        }
    }

    @Test //https://github.com/elasticsearch/elasticsearch/issues/3629
    public void explainDateRangeInQueryString() {
        client().admin().indices().prepareCreate("test").setSettings(ImmutableSettings.settingsBuilder().put("index.number_of_shards", 1)).get();

        String aMonthAgo = ISODateTimeFormat.yearMonthDay().print(new DateTime(DateTimeZone.UTC).minusMonths(1));
        String aMonthFromNow = ISODateTimeFormat.yearMonthDay().print(new DateTime(DateTimeZone.UTC).plusMonths(1));

        client().prepareIndex("test", "type", "1").setSource("past", aMonthAgo, "future", aMonthFromNow).get();

        refresh();

        ValidateQueryResponse response = client().admin().indices().prepareValidateQuery()
                .setQuery(queryString("past:[now-2M/d TO now/d]")).setExplain(true).get();

        assertNoFailures(response);
        assertThat(response.getQueryExplanation().size(), equalTo(1));
        assertThat(response.getQueryExplanation().get(0).getError(), nullValue());
        DateTime twoMonthsAgo = new DateTime(DateTimeZone.UTC).minusMonths(2).withTimeAtStartOfDay();
        DateTime now = new DateTime(DateTimeZone.UTC).plusDays(1).withTimeAtStartOfDay();
        assertThat(response.getQueryExplanation().get(0).getExplanation(),
                equalTo("past:[" + twoMonthsAgo.getMillis() + " TO " + now.getMillis() + "]"));
        assertThat(response.isValid(), equalTo(true));
    }

    private void assertExplanation(QueryBuilder queryBuilder, Matcher<String> matcher) {
        ValidateQueryResponse response = client().admin().indices().prepareValidateQuery("test")
                .setTypes("type1")
                .setQuery(queryBuilder)
                .setExplain(true)
                .execute().actionGet();
        assertThat(response.getQueryExplanation().size(), equalTo(1));
        assertThat(response.getQueryExplanation().get(0).getError(), nullValue());
        assertThat(response.getQueryExplanation().get(0).getExplanation(), matcher);
        assertThat(response.isValid(), equalTo(true));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/972.java