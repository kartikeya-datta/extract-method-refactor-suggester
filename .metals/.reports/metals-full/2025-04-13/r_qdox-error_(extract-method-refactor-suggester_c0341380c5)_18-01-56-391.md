error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3697.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3697.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3697.java
text:
```scala
i@@nt numQueries = scaledRandomIntBetween(250, 500);

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
package org.elasticsearch.percolator;

import org.elasticsearch.action.percolate.PercolateRequestBuilder;
import org.elasticsearch.action.percolate.PercolateResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.action.percolate.PercolateSourceBuilder.docBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertMatchCount;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class PercolatorFacetsAndAggregationsTests extends ElasticsearchIntegrationTest {

    @Test
    // Just test the integration with facets and aggregations, not the facet and aggregation functionality!
    public void testFacetsAndAggregations() throws Exception {
        client().admin().indices().prepareCreate("test").execute().actionGet();
        ensureGreen();

        int numQueries = atLeast(250);
        int numUniqueQueries = between(1, numQueries / 2);
        String[] values = new String[numUniqueQueries];
        for (int i = 0; i < values.length; i++) {
            values[i] = "value" + i;
        }
        int[] expectedCount = new int[numUniqueQueries];

        logger.info("--> registering {} queries", numQueries);
        for (int i = 0; i < numQueries; i++) {
            String value = values[i % numUniqueQueries];
            expectedCount[i % numUniqueQueries]++;
            QueryBuilder queryBuilder = matchQuery("field1", value);
            client().prepareIndex("test", PercolatorService.TYPE_NAME, Integer.toString(i))
                    .setSource(jsonBuilder().startObject().field("query", queryBuilder).field("field2", "b").endObject())
                    .execute().actionGet();
        }
        client().admin().indices().prepareRefresh("test").execute().actionGet();

        for (int i = 0; i < numQueries; i++) {
            String value = values[i % numUniqueQueries];
            PercolateRequestBuilder percolateRequestBuilder = client().preparePercolate()
                    .setIndices("test").setDocumentType("type")
                    .setPercolateDoc(docBuilder().setDoc(jsonBuilder().startObject().field("field1", value).endObject()));

            boolean useAggs = randomBoolean();
            if (useAggs) {
                percolateRequestBuilder.addAggregation(AggregationBuilders.terms("a").field("field2"));
            } else {
                percolateRequestBuilder.addFacet(FacetBuilders.termsFacet("a").field("field2"));

            }

            if (randomBoolean()) {
                percolateRequestBuilder.setPercolateQuery(matchAllQuery());
            }
            if (randomBoolean()) {
                percolateRequestBuilder.setScore(true);
            } else {
                percolateRequestBuilder.setSortByScore(true).setSize(numQueries);
            }

            boolean countOnly = randomBoolean();
            if (countOnly) {
                percolateRequestBuilder.setOnlyCount(countOnly);
            }

            PercolateResponse response = percolateRequestBuilder.execute().actionGet();
            assertMatchCount(response, expectedCount[i % numUniqueQueries]);
            if (!countOnly) {
                assertThat(response.getMatches(), arrayWithSize(expectedCount[i % numUniqueQueries]));
            }

            if (useAggs) {
                List<Aggregation> aggregations = response.getAggregations().asList();
                assertThat(aggregations.size(), equalTo(1));
                assertThat(aggregations.get(0).getName(), equalTo("a"));
                List<Terms.Bucket> buckets = new ArrayList<Terms.Bucket>(((Terms) aggregations.get(0)).getBuckets());
                assertThat(buckets.size(), equalTo(1));
                assertThat(buckets.get(0).getKeyAsText().string(), equalTo("b"));
                assertThat(buckets.get(0).getDocCount(), equalTo((long) expectedCount[i % values.length]));
            } else {
                assertThat(response.getFacets().facets().size(), equalTo(1));
                assertThat(response.getFacets().facets().get(0).getName(), equalTo("a"));
                assertThat(((TermsFacet) response.getFacets().facets().get(0)).getEntries().size(), equalTo(1));
                assertThat(((TermsFacet) response.getFacets().facets().get(0)).getEntries().get(0).getCount(), equalTo(expectedCount[i % values.length]));
                assertThat(((TermsFacet) response.getFacets().facets().get(0)).getEntries().get(0).getTerm().string(), equalTo("b"));
            }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3697.java