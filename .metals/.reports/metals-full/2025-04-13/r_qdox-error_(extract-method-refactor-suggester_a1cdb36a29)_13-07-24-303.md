error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8803.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8803.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8803.java
text:
```scala
.@@put(AssertingLocalTransport.ASSERTING_TRANSPORT_MIN_VERSION_KEY, Version.V_1_4_0_Beta).build();

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
package org.elasticsearch.search.aggregations.bucket;

import org.elasticsearch.Version;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.mapper.core.DateFieldMapper;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.transport.AssertingLocalTransport;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.dateHistogram;
import static org.hamcrest.Matchers.equalTo;

/**
 * The serialisation of pre and post offsets for the date histogram aggregation was corrected in version 1.4 to allow negative offsets and as such the
 * serialisation of negative offsets in these tests would break in pre 1.4 versions.  These tests are separated from the other DateHistogramTests so the 
 * AssertingLocalTransport for these tests can be set to only use versions 1.4 onwards while keeping the other tests using all versions
 */
@ElasticsearchIntegrationTest.SuiteScopeTest
@ElasticsearchIntegrationTest.ClusterScope(scope=ElasticsearchIntegrationTest.Scope.SUITE)
public class DateHistogramOffsetTests extends ElasticsearchIntegrationTest {

    private DateTime date(String date) {
        return DateFieldMapper.Defaults.DATE_TIME_FORMATTER.parser().parseDateTime(date);
    }

    @Override
    protected Settings nodeSettings(int nodeOrdinal) {
        return ImmutableSettings.builder()
                .put(super.nodeSettings(nodeOrdinal))
                .put(AssertingLocalTransport.ASSERTING_TRANSPORT_MIN_VERSION_KEY, Version.V_1_4_0).build();
    }

    @After
    public void afterEachTest() throws IOException {
        internalCluster().wipeIndices("idx2");
    }

    @Test
    public void singleValue_WithPreOffset() throws Exception {
        prepareCreate("idx2").addMapping("type", "date", "type=date").execute().actionGet();
        IndexRequestBuilder[] reqs = new IndexRequestBuilder[5];
        DateTime date = date("2014-03-11T00:00:00+00:00");
        for (int i = 0; i < reqs.length; i++) {
            reqs[i] = client().prepareIndex("idx2", "type", "" + i).setSource(jsonBuilder().startObject().field("date", date).endObject());
            date = date.plusHours(1);
        }
        indexRandom(true, reqs);

        SearchResponse response = client().prepareSearch("idx2")
                .setQuery(matchAllQuery())
                .addAggregation(dateHistogram("date_histo")
                        .field("date")
                        .preOffset("-2h")
                        .interval(DateHistogram.Interval.DAY)
                        .format("yyyy-MM-dd"))
                .execute().actionGet();

        assertThat(response.getHits().getTotalHits(), equalTo(5l));

        DateHistogram histo = response.getAggregations().get("date_histo");
        Collection<? extends DateHistogram.Bucket> buckets = histo.getBuckets();
        assertThat(buckets.size(), equalTo(2));

        DateHistogram.Bucket bucket = histo.getBucketByKey("2014-03-10");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = histo.getBucketByKey("2014-03-11");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(3l));
    }

    @Test
    public void singleValue_WithPreOffset_MinDocCount() throws Exception {
        prepareCreate("idx2").addMapping("type", "date", "type=date").execute().actionGet();
        IndexRequestBuilder[] reqs = new IndexRequestBuilder[5];
        DateTime date = date("2014-03-11T00:00:00+00:00");
        for (int i = 0; i < reqs.length; i++) {
            reqs[i] = client().prepareIndex("idx2", "type", "" + i).setSource(jsonBuilder().startObject().field("date", date).endObject());
            date = date.plusHours(1);
        }
        indexRandom(true, reqs);

        SearchResponse response = client().prepareSearch("idx2")
                .setQuery(matchAllQuery())
                .addAggregation(dateHistogram("date_histo")
                        .field("date")
                        .preOffset("-2h")
                        .minDocCount(0)
                        .interval(DateHistogram.Interval.DAY)
                        .format("yyyy-MM-dd"))
                .execute().actionGet();

        assertThat(response.getHits().getTotalHits(), equalTo(5l));

        DateHistogram histo = response.getAggregations().get("date_histo");
        Collection<? extends DateHistogram.Bucket> buckets = histo.getBuckets();
        assertThat(buckets.size(), equalTo(2));

        DateHistogram.Bucket bucket = histo.getBucketByKey("2014-03-10");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = histo.getBucketByKey("2014-03-11");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(3l));
    }

    @Test
    public void singleValue_WithPostOffset() throws Exception {
        prepareCreate("idx2").addMapping("type", "date", "type=date").execute().actionGet();
        IndexRequestBuilder[] reqs = new IndexRequestBuilder[5];
        DateTime date = date("2014-03-11T00:00:00+00:00");
        for (int i = 0; i < reqs.length; i++) {
            reqs[i] = client().prepareIndex("idx2", "type", "" + i).setSource(jsonBuilder().startObject().field("date", date).endObject());
            date = date.plusHours(6);
        }
        indexRandom(true, reqs);

        SearchResponse response = client().prepareSearch("idx2")
                .setQuery(matchAllQuery())
                .addAggregation(dateHistogram("date_histo")
                        .field("date")
                        .postOffset("2d")
                        .interval(DateHistogram.Interval.DAY)
                        .format("yyyy-MM-dd"))
                .execute().actionGet();

        assertThat(response.getHits().getTotalHits(), equalTo(5l));

        DateHistogram histo = response.getAggregations().get("date_histo");
        Collection<? extends DateHistogram.Bucket> buckets = histo.getBuckets();
        assertThat(buckets.size(), equalTo(2));

        DateHistogram.Bucket bucket = histo.getBucketByKey("2014-03-13");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(4l));

        bucket = histo.getBucketByKey("2014-03-14");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(1l));
    }

    @Test
    public void singleValue_WithPostOffset_MinDocCount() throws Exception {
        prepareCreate("idx2").addMapping("type", "date", "type=date").execute().actionGet();
        IndexRequestBuilder[] reqs = new IndexRequestBuilder[5];
        DateTime date = date("2014-03-11T00:00:00+00:00");
        for (int i = 0; i < reqs.length; i++) {
            reqs[i] = client().prepareIndex("idx2", "type", "" + i).setSource(jsonBuilder().startObject().field("date", date).endObject());
            date = date.plusHours(6);
        }
        indexRandom(true, reqs);

        SearchResponse response = client().prepareSearch("idx2")
                .setQuery(matchAllQuery())
                .addAggregation(dateHistogram("date_histo")
                        .field("date")
                        .postOffset("2d")
                        .minDocCount(0)
                        .interval(DateHistogram.Interval.DAY)
                        .format("yyyy-MM-dd"))
                .execute().actionGet();

        assertThat(response.getHits().getTotalHits(), equalTo(5l));

        DateHistogram histo = response.getAggregations().get("date_histo");
        Collection<? extends DateHistogram.Bucket> buckets = histo.getBuckets();
        assertThat(buckets.size(), equalTo(2));

        DateHistogram.Bucket bucket = histo.getBucketByKey("2014-03-13");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(4l));

        bucket = histo.getBucketByKey("2014-03-14");
        assertThat(bucket, Matchers.notNullValue());
        assertThat(bucket.getDocCount(), equalTo(1l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8803.java