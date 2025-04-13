error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9878.java
text:
```scala
.@@addAggregation(histogram("histo").field("value").interval(1l).minDocCount(0)

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

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertSearchResponse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 *
 */
public class MissingTests extends ElasticsearchIntegrationTest {


    @Override
    public Settings indexSettings() {
        return ImmutableSettings.builder()
                .put("index.number_of_shards", between(1, 5))
                .put("index.number_of_replicas", between(0, 1))
                .build();
    }

    int numDocs, numDocsMissing, numDocsUnmapped;

    @Before
    public void init() throws Exception {
        createIndex("idx");
        List<IndexRequestBuilder> builders = new ArrayList<IndexRequestBuilder>();
        numDocs = randomIntBetween(5, 20);
        numDocsMissing = randomIntBetween(1, numDocs - 1);
        for (int i = 0; i < numDocsMissing; i++) {
            builders.add(client().prepareIndex("idx", "type", ""+i).setSource(jsonBuilder()
                    .startObject()
                    .field("value", i)
                    .endObject()));
        }
        for (int i = numDocsMissing; i < numDocs; i++) {
            builders.add(client().prepareIndex("idx", "type", ""+i).setSource(jsonBuilder()
                    .startObject()
                    .field("tag", "tag1")
                    .endObject()));
        }

        createIndex("unmapped_idx");
        numDocsUnmapped = randomIntBetween(2, 5);
        for (int i = 0; i < numDocsUnmapped; i++) {
            builders.add(client().prepareIndex("unmapped_idx", "type", ""+i).setSource(jsonBuilder()
                    .startObject()
                    .field("value", i)
                    .endObject()));
        }

        indexRandom(true, builders.toArray(new IndexRequestBuilder[builders.size()]));
        ensureGreen(); // wait until we are ready to serve requests
        ensureSearchable();
    }

    @Test
    public void unmapped() throws Exception {
        SearchResponse response = client().prepareSearch("unmapped_idx")
                .addAggregation(missing("missing_tag").field("tag"))
                .execute().actionGet();

        assertSearchResponse(response);


        Missing missing = response.getAggregations().get("missing_tag");
        assertThat(missing, notNullValue());
        assertThat(missing.getName(), equalTo("missing_tag"));
        assertThat(missing.getDocCount(), equalTo((long) numDocsUnmapped));
    }

    @Test
    public void partiallyUnmapped() throws Exception {
        SearchResponse response = client().prepareSearch("idx", "unmapped_idx")
                .addAggregation(missing("missing_tag").field("tag"))
                .execute().actionGet();

        assertSearchResponse(response);


        Missing missing = response.getAggregations().get("missing_tag");
        assertThat(missing, notNullValue());
        assertThat(missing.getName(), equalTo("missing_tag"));
        assertThat(missing.getDocCount(), equalTo((long) numDocsMissing + numDocsUnmapped));
    }

    @Test
    public void simple() throws Exception {
        SearchResponse response = client().prepareSearch("idx")
                .addAggregation(missing("missing_tag").field("tag"))
                .execute().actionGet();

        assertSearchResponse(response);


        Missing missing = response.getAggregations().get("missing_tag");
        assertThat(missing, notNullValue());
        assertThat(missing.getName(), equalTo("missing_tag"));
        assertThat(missing.getDocCount(), equalTo((long) numDocsMissing));
    }

    @Test
    public void withSubAggregation() throws Exception {
        SearchResponse response = client().prepareSearch("idx", "unmapped_idx")
                .addAggregation(missing("missing_tag").field("tag")
                        .subAggregation(avg("avg_value").field("value")))
                .execute().actionGet();

        assertSearchResponse(response);

        assertThat("Not all shards are initialized", response.getSuccessfulShards(), equalTo(response.getTotalShards()));

        Missing missing = response.getAggregations().get("missing_tag");
        assertThat(missing, notNullValue());
        assertThat(missing.getName(), equalTo("missing_tag"));
        assertThat(missing.getDocCount(), equalTo((long) numDocsMissing + numDocsUnmapped));
        assertThat(missing.getAggregations().asList().isEmpty(), is(false));

        long sum = 0;
        for (int i = 0; i < numDocsMissing; ++i) {
            sum += i;
        }
        for (int i = 0; i < numDocsUnmapped; ++i) {
            sum += i;
        }
        Avg avgValue = missing.getAggregations().get("avg_value");
        assertThat(avgValue, notNullValue());
        assertThat(avgValue.getName(), equalTo("avg_value"));
        assertThat(avgValue.getValue(), equalTo((double) sum / (numDocsMissing + numDocsUnmapped)));
    }

    @Test
    public void withInheritedSubMissing() throws Exception {

        SearchResponse response = client().prepareSearch()
                .addAggregation(missing("top_missing").field("tag")
                        .subAggregation(missing("sub_missing")))
                .execute().actionGet();

        assertSearchResponse(response);


        Missing topMissing = response.getAggregations().get("top_missing");
        assertThat(topMissing, notNullValue());
        assertThat(topMissing.getName(), equalTo("top_missing"));
        assertThat(topMissing.getDocCount(), equalTo((long) numDocsMissing + numDocsUnmapped));
        assertThat(topMissing.getAggregations().asList().isEmpty(), is(false));

        Missing subMissing = topMissing.getAggregations().get("sub_missing");
        assertThat(subMissing, notNullValue());
        assertThat(subMissing.getName(), equalTo("sub_missing"));
        assertThat(subMissing.getDocCount(), equalTo((long) numDocsMissing + numDocsUnmapped));
    }

    @Test
    public void emptyAggregation() throws Exception {
        prepareCreate("empty_bucket_idx").addMapping("type", "value", "type=integer").execute().actionGet();
        List<IndexRequestBuilder> builders = new ArrayList<IndexRequestBuilder>();
        for (int i = 0; i < 2; i++) {
            builders.add(client().prepareIndex("empty_bucket_idx", "type", ""+i).setSource(jsonBuilder()
                    .startObject()
                    .field("value", i*2)
                    .endObject()));
        }
        indexRandom(true, builders.toArray(new IndexRequestBuilder[builders.size()]));

        SearchResponse searchResponse = client().prepareSearch("empty_bucket_idx")
                .setQuery(matchAllQuery())
                .addAggregation(histogram("histo").field("value").interval(1l).emptyBuckets(true)
                        .subAggregation(missing("missing")))
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(2l));
        Histogram histo = searchResponse.getAggregations().get("histo");
        assertThat(histo, Matchers.notNullValue());
        Histogram.Bucket bucket = histo.getByKey(1l);
        assertThat(bucket, Matchers.notNullValue());

        Missing missing = bucket.getAggregations().get("missing");
        assertThat(missing, Matchers.notNullValue());
        assertThat(missing.getName(), equalTo("missing"));
        assertThat(missing.getDocCount(), is(0l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9878.java