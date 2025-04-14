error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3903.java
text:
```scala
c@@luster().wipeIndices("idx");

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

package org.elasticsearch.search.aggregations;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.google.common.collect.Lists;
import org.apache.lucene.util.LuceneTestCase.Slow;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.fielddata.ordinals.InternalGlobalOrdinalsBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.aggregations.Aggregator.SubAggCollectionMode;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregatorFactory;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.test.ElasticsearchIntegrationTest;

import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Additional tests that aim at testing more complex aggregation trees on larger random datasets, so that things like
 * the growth of dynamic arrays is tested.
 */
@Slow
public class RandomTests extends ElasticsearchIntegrationTest {

    // Make sure that unordered, reversed, disjoint and/or overlapping ranges are supported
    // Duel with filters
    public void testRandomRanges() throws Exception {
        final int numDocs = scaledRandomIntBetween(500, 5000);
        final double[][] docs = new double[numDocs][];
        for (int i = 0; i < numDocs; ++i) {
            final int numValues = randomInt(5);
            docs[i] = new double[numValues];
            for (int j = 0; j < numValues; ++j) {
                docs[i][j] = randomDouble() * 100;
            }
        }

        createIndex("idx");
        for (int i = 0; i < docs.length; ++i) {
            XContentBuilder source = jsonBuilder()
                    .startObject()
                    .startArray("values");
            for (int j = 0; j < docs[i].length; ++j) {
                source = source.value(docs[i][j]);
            }
            source = source.endArray().endObject();
            client().prepareIndex("idx", "type").setSource(source).execute().actionGet();
        }
        assertNoFailures(client().admin().indices().prepareRefresh("idx").setIndicesOptions(IndicesOptions.lenientExpandOpen()).execute().get());

        final int numRanges = randomIntBetween(1, 20);
        final double[][] ranges = new double[numRanges][];
        for (int i = 0; i < ranges.length; ++i) {
            switch (randomInt(2)) {
            case 0:
                ranges[i] = new double[] { Double.NEGATIVE_INFINITY, randomInt(100) };
                break;
            case 1:
                ranges[i] = new double[] { randomInt(100), Double.POSITIVE_INFINITY };
                break;
            case 2:
                ranges[i] = new double[] { randomInt(100), randomInt(100) };
                break;
            default:
                throw new AssertionError();
            }
        }

        RangeBuilder query = range("range").field("values");
        for (int i = 0; i < ranges.length; ++i) {
            String key = Integer.toString(i);
            if (ranges[i][0] == Double.NEGATIVE_INFINITY) {
                query.addUnboundedTo(key, ranges[i][1]);
            } else if (ranges[i][1] == Double.POSITIVE_INFINITY) {
                query.addUnboundedFrom(key, ranges[i][0]);
            } else {
                query.addRange(key, ranges[i][0], ranges[i][1]);
            }
        }

        SearchRequestBuilder reqBuilder = client().prepareSearch("idx").addAggregation(query);
        for (int i = 0; i < ranges.length; ++i) {
            RangeFilterBuilder filter = FilterBuilders.rangeFilter("values");
            if (ranges[i][0] != Double.NEGATIVE_INFINITY) {
                filter = filter.from(ranges[i][0]);
            }
            if (ranges[i][1] != Double.POSITIVE_INFINITY){
                filter = filter.to(ranges[i][1]);
            }
            reqBuilder = reqBuilder.addAggregation(filter("filter" + i).filter(filter));
        }

        SearchResponse resp = reqBuilder.execute().actionGet();
        Range range = resp.getAggregations().get("range");

        for (int i = 0; i < ranges.length; ++i) {

            long count = 0;
            for (double[] values : docs) {
                for (double value : values) {
                    if (value >= ranges[i][0] && value < ranges[i][1]) {
                        ++count;
                        break;
                    }
                }
            }

            final Range.Bucket bucket = range.getBucketByKey(Integer.toString(i));
            assertEquals(bucket.getKey(), count, bucket.getDocCount());

            final Filter filter = resp.getAggregations().get("filter" + i);
            assertThat(filter.getDocCount(), equalTo(count));
        }
    }

    // test long/double/string terms aggs with high number of buckets that require array growth
    public void testDuelTerms() throws Exception {
        final int numDocs = scaledRandomIntBetween(1000, 2000);
        final int maxNumTerms = randomIntBetween(10, 5000);

        final IntOpenHashSet valuesSet = new IntOpenHashSet();
        immutableCluster().wipeIndices("idx");
        prepareCreate("idx")
                .setSettings(ImmutableSettings.builder().put(InternalGlobalOrdinalsBuilder.ORDINAL_MAPPING_THRESHOLD_INDEX_SETTING_KEY, randomIntBetween(1, maxNumTerms)))
                .addMapping("type", jsonBuilder().startObject()
                        .startObject("type")
                        .startObject("properties")
                        .startObject("string_values")
                        .field("type", "string")
                        .field("index", "not_analyzed")
                        .startObject("fields")
                            .startObject("doc_values")
                                .field("type", "string")
                                .field("index", "no")
                                .startObject("fielddata")
                                    .field("format", "doc_values")
                                .endObject()
                            .endObject()
                        .endObject()
                        .endObject()
                        .startObject("long_values")
                        .field("type", "long")
                        .endObject()
                        .startObject("double_values")
                        .field("type", "double")
                        .endObject()
                        .endObject()
                        .endObject()).execute().actionGet();

        List<IndexRequestBuilder> indexingRequests = Lists.newArrayList();
        for (int i = 0; i < numDocs; ++i) {
            final int[] values = new int[randomInt(4)];
            for (int j = 0; j < values.length; ++j) {
                values[j] = randomInt(maxNumTerms - 1) - 1000;
                valuesSet.add(values[j]);
            }
            XContentBuilder source = jsonBuilder()
                    .startObject()
                    .field("num", randomDouble())
                    .startArray("long_values");
            for (int j = 0; j < values.length; ++j) {
                source = source.value(values[j]);
            }
            source = source.endArray().startArray("double_values");
            for (int j = 0; j < values.length; ++j) {
                source = source.value((double) values[j]);
            }
            source = source.endArray().startArray("string_values");
            for (int j = 0; j < values.length; ++j) {
                source = source.value(Integer.toString(values[j]));
            }
            source = source.endArray().endObject();
            indexingRequests.add(client().prepareIndex("idx", "type").setSource(source));
        }
        indexRandom(true, indexingRequests);

        assertNoFailures(client().admin().indices().prepareRefresh("idx").setIndicesOptions(IndicesOptions.lenientExpandOpen()).execute().get());

        TermsAggregatorFactory.ExecutionMode[] globalOrdinalModes = new TermsAggregatorFactory.ExecutionMode[]{
                TermsAggregatorFactory.ExecutionMode.GLOBAL_ORDINALS_HASH,
                TermsAggregatorFactory.ExecutionMode.GLOBAL_ORDINALS
        };

        SearchResponse resp = client().prepareSearch("idx")
                .addAggregation(terms("long").field("long_values").size(maxNumTerms).collectMode(randomFrom(SubAggCollectionMode.values())).subAggregation(min("min").field("num")))
                .addAggregation(terms("double").field("double_values").size(maxNumTerms).collectMode(randomFrom(SubAggCollectionMode.values())).subAggregation(max("max").field("num")))
                .addAggregation(terms("string_map").field("string_values").collectMode(randomFrom(SubAggCollectionMode.values())).executionHint(TermsAggregatorFactory.ExecutionMode.MAP.toString()).size(maxNumTerms).subAggregation(stats("stats").field("num")))
                .addAggregation(terms("string_global_ordinals").field("string_values").collectMode(randomFrom(SubAggCollectionMode.values())).executionHint(globalOrdinalModes[randomInt(globalOrdinalModes.length - 1)].toString()).size(maxNumTerms).subAggregation(extendedStats("stats").field("num")))
                .addAggregation(terms("string_global_ordinals_doc_values").field("string_values.doc_values").collectMode(randomFrom(SubAggCollectionMode.values())).executionHint(globalOrdinalModes[randomInt(globalOrdinalModes.length - 1)].toString()).size(maxNumTerms).subAggregation(extendedStats("stats").field("num")))
                .execute().actionGet();
        assertAllSuccessful(resp);
        assertEquals(numDocs, resp.getHits().getTotalHits());

        final Terms longTerms = resp.getAggregations().get("long");
        final Terms doubleTerms = resp.getAggregations().get("double");
        final Terms stringMapTerms = resp.getAggregations().get("string_map");
        final Terms stringGlobalOrdinalsTerms = resp.getAggregations().get("string_global_ordinals");
        final Terms stringGlobalOrdinalsDVTerms = resp.getAggregations().get("string_global_ordinals_doc_values");

        assertEquals(valuesSet.size(), longTerms.getBuckets().size());
        assertEquals(valuesSet.size(), doubleTerms.getBuckets().size());
        assertEquals(valuesSet.size(), stringMapTerms.getBuckets().size());
        assertEquals(valuesSet.size(), stringGlobalOrdinalsTerms.getBuckets().size());
        assertEquals(valuesSet.size(), stringGlobalOrdinalsDVTerms.getBuckets().size());
        for (Terms.Bucket bucket : longTerms.getBuckets()) {
            final Terms.Bucket doubleBucket = doubleTerms.getBucketByKey(Double.toString(Long.parseLong(bucket.getKeyAsText().string())));
            final Terms.Bucket stringMapBucket = stringMapTerms.getBucketByKey(bucket.getKeyAsText().string());
            final Terms.Bucket stringGlobalOrdinalsBucket = stringGlobalOrdinalsTerms.getBucketByKey(bucket.getKeyAsText().string());
            final Terms.Bucket stringGlobalOrdinalsDVBucket = stringGlobalOrdinalsDVTerms.getBucketByKey(bucket.getKeyAsText().string());
            assertNotNull(doubleBucket);
            assertNotNull(stringMapBucket);
            assertNotNull(stringGlobalOrdinalsBucket);
            assertNotNull(stringGlobalOrdinalsDVBucket);
            assertEquals(bucket.getDocCount(), doubleBucket.getDocCount());
            assertEquals(bucket.getDocCount(), stringMapBucket.getDocCount());
            assertEquals(bucket.getDocCount(), stringGlobalOrdinalsBucket.getDocCount());
            assertEquals(bucket.getDocCount(), stringGlobalOrdinalsDVBucket.getDocCount());
        }
    }

    // Duel between histograms and scripted terms
    public void testDuelTermsHistogram() throws Exception {
        createIndex("idx");

        final int numDocs = scaledRandomIntBetween(500, 5000);
        final int maxNumTerms = randomIntBetween(10, 2000);
        final int interval = randomIntBetween(1, 100);

        final Integer[] values = new Integer[maxNumTerms];
        for (int i = 0; i < values.length; ++i) {
            values[i] = randomInt(maxNumTerms * 3) - maxNumTerms;
        }

        for (int i = 0; i < numDocs; ++i) {
            XContentBuilder source = jsonBuilder()
                    .startObject()
                    .field("num", randomDouble())
                    .startArray("values");
            final int numValues = randomInt(4);
            for (int j = 0; j < numValues; ++j) {
                source = source.value(randomFrom(values));
            }
            source = source.endArray().endObject();
            client().prepareIndex("idx", "type").setSource(source).execute().actionGet();
        }
        assertNoFailures(client().admin().indices().prepareRefresh("idx").setIndicesOptions(IndicesOptions.lenientExpandOpen()).execute().get());

        SearchResponse resp = client().prepareSearch("idx")
                .addAggregation(terms("terms").field("values").collectMode(randomFrom(SubAggCollectionMode.values())).script("floor(_value / interval)").param("interval", interval).size(maxNumTerms))
                .addAggregation(histogram("histo").field("values").interval(interval))
                .execute().actionGet();

        assertSearchResponse(resp);

        Terms terms = resp.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        Histogram histo = resp.getAggregations().get("histo");
        assertThat(histo, notNullValue());
        assertThat(terms.getBuckets().size(), equalTo(histo.getBuckets().size()));
        for (Terms.Bucket bucket : terms.getBuckets()) {
            final long key = bucket.getKeyAsNumber().longValue() * interval;
            final Histogram.Bucket histoBucket = histo.getBucketByKey(key);
            assertEquals(bucket.getDocCount(), histoBucket.getDocCount());
        }
    }

    public void testLargeNumbersOfPercentileBuckets() throws Exception {
        // test high numbers of percentile buckets to make sure paging and release work correctly
        createIndex("idx");

        final int numDocs = scaledRandomIntBetween(2500, 5000);
        logger.info("Indexing [" + numDocs +"] docs");
        List<IndexRequestBuilder> indexingRequests = Lists.newArrayList();
        for (int i = 0; i < numDocs; ++i) {
            indexingRequests.add(client().prepareIndex("idx", "type", Integer.toString(i)).setSource("double_value", randomDouble()));
        }
        indexRandom(true, indexingRequests);

        SearchResponse response = client().prepareSearch("idx").addAggregation(terms("terms").field("double_value").collectMode(randomFrom(SubAggCollectionMode.values())).subAggregation(percentiles("pcts").field("double_value"))).execute().actionGet();
        assertAllSuccessful(response);
        assertEquals(numDocs, response.getHits().getTotalHits());
    }

    // https://github.com/elasticsearch/elasticsearch/issues/6435
    public void testReduce() throws Exception {
        createIndex("idx");
        final int value = randomIntBetween(0, 10);
        indexRandom(true, client().prepareIndex("idx", "type").setSource("f", value));
        ensureYellow("idx"); // only one document let's make sure all shards have an active primary
        SearchResponse response = client().prepareSearch("idx")
                .addAggregation(filter("filter").filter(FilterBuilders.matchAllFilter())
                .subAggregation(range("range")
                        .field("f")
                        .addUnboundedTo(6)
                        .addUnboundedFrom(6)
                .subAggregation(sum("sum").field("f"))))
                .execute().actionGet();

        assertSearchResponse(response);

        Filter filter = response.getAggregations().get("filter");
        assertNotNull(filter);
        assertEquals(1, filter.getDocCount());

        Range range = filter.getAggregations().get("range");
        assertThat(range, notNullValue());
        assertThat(range.getName(), equalTo("range"));
        assertThat(range.getBuckets().size(), equalTo(2));

        Range.Bucket bucket = range.getBucketByKey("*-6.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-6.0"));
        assertThat(bucket.getFrom().doubleValue(), equalTo(Double.NEGATIVE_INFINITY));
        assertThat(bucket.getTo().doubleValue(), equalTo(6.0));
        assertThat(bucket.getDocCount(), equalTo(value < 6 ? 1L : 0L));
        Sum sum = bucket.getAggregations().get("sum");
        assertEquals(value < 6 ? value : 0, sum.getValue(), 0d);

        bucket = range.getBucketByKey("6.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("6.0-*"));
        assertThat(bucket.getFrom().doubleValue(), equalTo(6.0));
        assertThat(bucket.getTo().doubleValue(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(value >= 6 ? 1L : 0L));
        sum = bucket.getAggregations().get("sum");
        assertEquals(value >= 6 ? value : 0, sum.getValue(), 0d);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3903.java