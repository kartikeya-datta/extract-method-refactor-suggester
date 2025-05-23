error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1955.java
text:
```scala
i@@nt size = randomIntBetween(1, 10);

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

import org.apache.lucene.search.Explanation;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchPhaseExecutionException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregatorFactory.ExecutionMode;
import org.elasticsearch.search.aggregations.bucket.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertSearchResponse;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 *
 */
@ElasticsearchIntegrationTest.SuiteScopeTest()
public class TopHitsTests extends ElasticsearchIntegrationTest {

    private static final String TERMS_AGGS_FIELD = "terms";
    private static final String SORT_FIELD = "sort";

    public static String randomExecutionHint() {
        return randomBoolean() ? null : randomFrom(ExecutionMode.values()).toString();
    }

    @Override
    public void setupSuiteScopeCluster() throws Exception {
        createIndex("idx");
        createIndex("empty");
        List<IndexRequestBuilder> builders = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            builders.add(client().prepareIndex("idx", "type", Integer.toString(i)).setSource(jsonBuilder()
                    .startObject()
                    .field(TERMS_AGGS_FIELD, "val" + (i / 10))
                    .field(SORT_FIELD, i + 1)
                    .field("text", "some text to entertain")
                    .field("field1", 5)
                    .endObject()));
        }

        builders.add(client().prepareIndex("idx", "field-collapsing", "1").setSource(jsonBuilder()
                .startObject()
                .field("group", "a")
                .field("text", "term x y z b")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "2").setSource(jsonBuilder()
                .startObject()
                .field("group", "a")
                .field("text", "term x y z n rare")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "3").setSource(jsonBuilder()
                .startObject()
                .field("group", "b")
                .field("text", "x y z term")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "4").setSource(jsonBuilder()
                .startObject()
                .field("group", "b")
                .field("text", "x y term")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "5").setSource(jsonBuilder()
                .startObject()
                .field("group", "b")
                .field("text", "x term")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "6").setSource(jsonBuilder()
                .startObject()
                .field("group", "b")
                .field("text", "term rare")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "7").setSource(jsonBuilder()
                .startObject()
                .field("group", "c")
                .field("text", "x y z term")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "8").setSource(jsonBuilder()
                .startObject()
                .field("group", "c")
                .field("text", "x y term b")
                .endObject()));
        builders.add(client().prepareIndex("idx", "field-collapsing", "9").setSource(jsonBuilder()
                .startObject()
                .field("group", "c")
                .field("text", "rare x term")
                .endObject()));

        indexRandom(true, builders);
        ensureSearchable();
    }

    private String key(Terms.Bucket bucket) {
        return randomBoolean() ? bucket.getKey() : bucket.getKeyAsText().string();
    }

    @Test
    public void testBasics() throws Exception {
        SearchResponse response = client().prepareSearch("idx").setTypes("type")
                .addAggregation(terms("terms")
                        .executionHint(randomExecutionHint())
                        .field(TERMS_AGGS_FIELD)
                        .subAggregation(
                                topHits("hits").addSort(SortBuilders.fieldSort(SORT_FIELD).order(SortOrder.DESC))
                        )
                )
                .get();

        assertSearchResponse(response);

        Terms terms = response.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        assertThat(terms.getName(), equalTo("terms"));
        assertThat(terms.getBuckets().size(), equalTo(5));

        long higestSortValue = 0;
        for (int i = 0; i < 5; i++) {
            Terms.Bucket bucket = terms.getBucketByKey("val" + i);
            assertThat(bucket, notNullValue());
            assertThat(key(bucket), equalTo("val" + i));
            assertThat(bucket.getDocCount(), equalTo(10l));
            TopHits topHits = bucket.getAggregations().get("hits");
            SearchHits hits = topHits.getHits();
            assertThat(hits.totalHits(), equalTo(10l));
            assertThat(hits.getHits().length, equalTo(3));
            higestSortValue += 10;
            assertThat((Long) hits.getAt(0).sortValues()[0], equalTo(higestSortValue));
            assertThat((Long) hits.getAt(1).sortValues()[0], equalTo(higestSortValue - 1));
            assertThat((Long) hits.getAt(2).sortValues()[0], equalTo(higestSortValue - 2));

            assertThat(hits.getAt(0).sourceAsMap().size(), equalTo(4));
        }
    }

    @Test
    public void testPagination() throws Exception {
        int size = randomIntBetween(0, 10);
        int from = randomIntBetween(0, 10);
        SearchResponse response = client().prepareSearch("idx").setTypes("type")
                .addAggregation(terms("terms")
                                .executionHint(randomExecutionHint())
                                .field(TERMS_AGGS_FIELD)
                                .subAggregation(
                                        topHits("hits").addSort(SortBuilders.fieldSort(SORT_FIELD).order(SortOrder.DESC))
                                                .setFrom(from)
                                                .setSize(size)
                                )
                )
                .get();
        assertSearchResponse(response);

        SearchResponse control = client().prepareSearch("idx")
                .setTypes("type")
                .setFrom(from)
                .setSize(size)
                .setPostFilter(FilterBuilders.termFilter(TERMS_AGGS_FIELD, "val0"))
                .addSort(SORT_FIELD, SortOrder.DESC)
                .get();
        assertSearchResponse(control);
        SearchHits controlHits = control.getHits();

        Terms terms = response.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        assertThat(terms.getName(), equalTo("terms"));
        assertThat(terms.getBuckets().size(), equalTo(5));

        Terms.Bucket bucket = terms.getBucketByKey("val0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getDocCount(), equalTo(10l));
        TopHits topHits = bucket.getAggregations().get("hits");
        SearchHits hits = topHits.getHits();
        assertThat(hits.totalHits(), equalTo(controlHits.totalHits()));
        assertThat(hits.getHits().length, equalTo(controlHits.getHits().length));
        for (int i = 0; i < hits.getHits().length; i++) {
            logger.info(i + ": top_hits: [" + hits.getAt(i).id() + "][" + hits.getAt(i).sortValues()[0] + "] control: [" + controlHits.getAt(i).id() + "][" + controlHits.getAt(i).sortValues()[0] + "]");
            assertThat(hits.getAt(i).id(), equalTo(controlHits.getAt(i).id()));
            assertThat(hits.getAt(i).sortValues()[0], equalTo(controlHits.getAt(i).sortValues()[0]));
        }
    }

    @Test
    public void testSortByBucket() throws Exception {
        SearchResponse response = client().prepareSearch("idx").setTypes("type")
                .addAggregation(terms("terms")
                                .executionHint(randomExecutionHint())
                                .field(TERMS_AGGS_FIELD)
                                .order(Terms.Order.aggregation("max_sort", false))
                                .subAggregation(
                                        topHits("hits").addSort(SortBuilders.fieldSort(SORT_FIELD).order(SortOrder.DESC)).setTrackScores(true)
                                )
                                .subAggregation(
                                        max("max_sort").field(SORT_FIELD)
                                )
                )
                .get();
        assertSearchResponse(response);

        Terms terms = response.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        assertThat(terms.getName(), equalTo("terms"));
        assertThat(terms.getBuckets().size(), equalTo(5));

        long higestSortValue = 50;
        int currentBucket = 4;
        for (Terms.Bucket bucket : terms.getBuckets()) {
            assertThat(key(bucket), equalTo("val" + currentBucket--));
            assertThat(bucket.getDocCount(), equalTo(10l));
            TopHits topHits = bucket.getAggregations().get("hits");
            SearchHits hits = topHits.getHits();
            assertThat(hits.totalHits(), equalTo(10l));
            assertThat(hits.getHits().length, equalTo(3));
            assertThat((Long) hits.getAt(0).sortValues()[0], equalTo(higestSortValue));
            assertThat((Long) hits.getAt(1).sortValues()[0], equalTo(higestSortValue - 1));
            assertThat((Long) hits.getAt(2).sortValues()[0], equalTo(higestSortValue - 2));
            Max max = bucket.getAggregations().get("max_sort");
            assertThat(max.getValue(), equalTo(((Long) higestSortValue).doubleValue()));
            higestSortValue -= 10;
        }
    }

    @Test
    public void testFieldCollapsing() throws Exception {
        SearchResponse response = client().prepareSearch("idx").setTypes("field-collapsing")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchQuery("text", "term rare"))
                .addAggregation(terms("terms")
                                .executionHint(randomExecutionHint())
                                .field("group")
                                .order(Terms.Order.aggregation("max_score", false))
                                .subAggregation(
                                        topHits("hits").setSize(1)
                                )
                                .subAggregation(
                                        max("max_score").script("_doc.score")
                                )
                )
                .get();
        assertSearchResponse(response);

        Terms terms = response.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        assertThat(terms.getName(), equalTo("terms"));
        assertThat(terms.getBuckets().size(), equalTo(3));

        Iterator<Terms.Bucket> bucketIterator = terms.getBuckets().iterator();
        Terms.Bucket bucket = bucketIterator.next();
        assertThat(key(bucket), equalTo("b"));
        TopHits topHits = bucket.getAggregations().get("hits");
        SearchHits hits = topHits.getHits();
        assertThat(hits.totalHits(), equalTo(4l));
        assertThat(hits.getHits().length, equalTo(1));
        assertThat(hits.getAt(0).id(), equalTo("6"));

        bucket = bucketIterator.next();
        assertThat(key(bucket), equalTo("c"));
        topHits = bucket.getAggregations().get("hits");
        hits = topHits.getHits();
        assertThat(hits.totalHits(), equalTo(3l));
        assertThat(hits.getHits().length, equalTo(1));
        assertThat(hits.getAt(0).id(), equalTo("9"));

        bucket = bucketIterator.next();
        assertThat(key(bucket), equalTo("a"));
        topHits = bucket.getAggregations().get("hits");
        hits = topHits.getHits();
        assertThat(hits.totalHits(), equalTo(2l));
        assertThat(hits.getHits().length, equalTo(1));
        assertThat(hits.getAt(0).id(), equalTo("2"));
    }

    @Test
    public void testFetchFeatures() {
        SearchResponse response = client().prepareSearch("idx").setTypes("type")
                .setQuery(matchQuery("text", "text").queryName("test"))
                .addAggregation(terms("terms")
                                .executionHint(randomExecutionHint())
                                .field(TERMS_AGGS_FIELD)
                                .subAggregation(
                                        topHits("hits").setSize(1)
                                            .addHighlightedField("text")
                                            .setExplain(true)
                                            .addFieldDataField("field1")
                                            .addScriptField("script", "doc['field1'].value")
                                            .setFetchSource("text", null)
                                            .setVersion(true)
                                )
                )
                .get();
        assertSearchResponse(response);

        Terms terms = response.getAggregations().get("terms");
        assertThat(terms, notNullValue());
        assertThat(terms.getName(), equalTo("terms"));
        assertThat(terms.getBuckets().size(), equalTo(5));

        for (Terms.Bucket bucket : terms.getBuckets()) {
            TopHits topHits = bucket.getAggregations().get("hits");
            SearchHits hits = topHits.getHits();
            assertThat(hits.totalHits(), equalTo(10l));
            assertThat(hits.getHits().length, equalTo(1));

            SearchHit hit = hits.getAt(0);
            HighlightField highlightField = hit.getHighlightFields().get("text");
            assertThat(highlightField.getFragments().length, equalTo(1));
            assertThat(highlightField.getFragments()[0].string(), equalTo("some <em>text</em> to entertain"));

            Explanation explanation = hit.explanation();
            assertThat(explanation.toString(), containsString("text:text"));

            long version = hit.version();
            assertThat(version, equalTo(1l));

            assertThat(hit.matchedQueries()[0], equalTo("test"));

            SearchHitField field = hit.field("field1");
            assertThat(field.getValue().toString(), equalTo("5"));

            field = hit.field("script");
            assertThat(field.getValue().toString(), equalTo("5"));

            assertThat(hit.sourceAsMap().size(), equalTo(1));
            assertThat(hit.sourceAsMap().get("text").toString(), equalTo("some text to entertain"));
        }
    }

    @Test
    public void testInvalidSortField() throws Exception {
        try {
            client().prepareSearch("idx").setTypes("type")
                    .addAggregation(terms("terms")
                                    .executionHint(randomExecutionHint())
                                    .field(TERMS_AGGS_FIELD)
                                    .subAggregation(
                                            topHits("hits").addSort(SortBuilders.fieldSort("xyz").order(SortOrder.DESC))
                                    )
                    ).get();
            fail();
        } catch (SearchPhaseExecutionException e) {
            assertThat(e.getMessage(), containsString("No mapping found for [xyz] in order to sort on"));
        }
    }

    @Test
    public void testFailWithSubAgg() throws Exception {
        String source = "{\n" +
                "  \"aggs\": {\n" +
                "    \"top-tags\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"tags\"\n" +
                "      },\n" +
                "      \"aggs\": {\n" +
                "        \"top_tags_hits\": {\n" +
                "          \"top_hits\": {},\n" +
                "          \"aggs\": {\n" +
                "            \"max\": {\n" +
                "              \"max\": {\n" +
                "                \"field\": \"age\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            client().prepareSearch("idx").setTypes("type")
                    .setSource(source)
                    .get();
            fail();
        } catch (SearchPhaseExecutionException e) {
            assertThat(e.getMessage(), containsString("Aggregator [top_tags_hits] of type [top_hits] cannot accept sub-aggregations"));
        }
    }

    @Test
    public void testEmptyIndex() throws Exception {
        SearchResponse response = client().prepareSearch("empty").setTypes("type")
                .addAggregation(topHits("hits"))
                .get();
        assertSearchResponse(response);

        TopHits hits = response.getAggregations().get("hits");
        assertThat(hits, notNullValue());
        assertThat(hits.getName(), equalTo("hits"));
        assertThat(hits.getHits().totalHits(), equalTo(0l));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1955.java