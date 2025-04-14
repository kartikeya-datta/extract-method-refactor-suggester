error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9874.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9874.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9874.java
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

import com.google.common.collect.Sets;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.geodistance.GeoDistance;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
public class GeoDistanceTests extends ElasticsearchIntegrationTest {

    @Override
    public Settings indexSettings() {
        return ImmutableSettings.builder()
                .put("index.number_of_shards", between(1, 5))
                .put("index.number_of_replicas", between(0, 1))
                .build();
    }

    private IndexRequestBuilder indexCity(String idx, String name, String... latLons) throws Exception {
        XContentBuilder source = jsonBuilder().startObject().field("city", name);
        source.startArray("location");
        for (int i = 0; i < latLons.length; i++) {
            source.value(latLons[i]);
        }
        source.endArray();
        source = source.endObject();
        return client().prepareIndex(idx, "type").setSource(source);
    }

    @Before
    public void init() throws Exception {
        prepareCreate("idx")
                .addMapping("type", "location", "type=geo_point", "city", "type=string,index=not_analyzed")
                .execute().actionGet();

        prepareCreate("idx-multi")
                .addMapping("type", "location", "type=geo_point", "city", "type=string,index=not_analyzed")
                .execute().actionGet();

        createIndex("idx_unmapped");

        List<IndexRequestBuilder> cities = new ArrayList<IndexRequestBuilder>();
        cities.addAll(Arrays.asList(
                // below 500km
                indexCity("idx", "utrecht", "52.0945, 5.116"),
                indexCity("idx", "haarlem", "52.3890, 4.637"),
                // above 500km, below 1000km
                indexCity("idx", "berlin", "52.540, 13.409"),
                indexCity("idx", "prague", "50.097679, 14.441314"),
                // above 1000km
                indexCity("idx", "tel-aviv", "32.0741, 34.777")));

        // random cities with no location
        for (String cityName : Arrays.asList("london", "singapour", "tokyo", "milan")) {
            if (randomBoolean()) {
                cities.add(indexCity("idx", cityName));
            }
        }
        indexRandom(true, cities);

        cities.clear();
        cities.addAll(Arrays.asList(
                indexCity("idx-multi", "city1", "52.3890, 4.637", "50.097679,14.441314"), // first point is within the ~17.5km, the second is ~710km
                indexCity("idx-multi", "city2", "52.540, 13.409", "52.0945, 5.116"), // first point is ~576km, the second is within the ~35km
                indexCity("idx-multi", "city3", "32.0741, 34.777"))); // above 1000km

        // random cities with no location
        for (String cityName : Arrays.asList("london", "singapour", "tokyo", "milan")) {
            if (randomBoolean() || true) {
                cities.add(indexCity("idx-multi", cityName));
            }
        }
        indexRandom(true, cities);

        ensureSearchable();
    }

    @Test
    public void simple() throws Exception {
        SearchResponse response = client().prepareSearch("idx")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo(500)
                        .addRange(500, 1000)
                        .addUnboundedFrom(1000))
                        .execute().actionGet();

        assertSearchResponse(response);


        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("*-500.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-500.0"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("500.0-1000.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("500.0-1000.0"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("1000.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("1000.0-*"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(1l));
    }

    @Test
    public void simple_WithCustomKeys() throws Exception {
        SearchResponse response = client().prepareSearch("idx")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo("ring1", 500)
                        .addRange("ring2", 500, 1000)
                        .addUnboundedFrom("ring3", 1000))
                .execute().actionGet();

        assertSearchResponse(response);


        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("ring1");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("ring1"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("ring2");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("ring2"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("ring3");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("ring3"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(1l));
    }

    @Test
    public void unmapped() throws Exception {
        client().admin().cluster().prepareHealth("idx_unmapped").setWaitForYellowStatus().execute().actionGet();

        SearchResponse response = client().prepareSearch("idx_unmapped")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo(500)
                        .addRange(500, 1000)
                        .addUnboundedFrom(1000))
                .execute().actionGet();

        assertSearchResponse(response);


        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("*-500.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-500.0"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(0l));

        bucket = geoDist.getByKey("500.0-1000.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("500.0-1000.0"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(0l));

        bucket = geoDist.getByKey("1000.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("1000.0-*"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(0l));
    }

    @Test
    public void partiallyUnmapped() throws Exception {
        SearchResponse response = client().prepareSearch("idx", "idx_unmapped")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo(500)
                        .addRange(500, 1000)
                        .addUnboundedFrom(1000))
                .execute().actionGet();

        assertSearchResponse(response);


        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("*-500.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-500.0"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("500.0-1000.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("500.0-1000.0"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("1000.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("1000.0-*"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(1l));
    }


    @Test
    public void withSubAggregation() throws Exception {
        SearchResponse response = client().prepareSearch("idx")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo(500)
                        .addRange(500, 1000)
                        .addUnboundedFrom(1000)
                        .subAggregation(terms("cities").field("city")))
                .execute().actionGet();

        assertSearchResponse(response);


        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("*-500.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-500.0"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(2l));
        assertThat(bucket.getAggregations().asList().isEmpty(), is(false));
        Terms cities = bucket.getAggregations().get("cities");
        assertThat(cities, Matchers.notNullValue());
        Set<String> names = Sets.newHashSet();
        for (Terms.Bucket city : cities) {
            names.add(city.getKey().string());
        }
        assertThat(names.contains("utrecht") && names.contains("haarlem"), is(true));

        bucket = geoDist.getByKey("500.0-1000.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("500.0-1000.0"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(2l));
        assertThat(bucket.getAggregations().asList().isEmpty(), is(false));
        cities = bucket.getAggregations().get("cities");
        assertThat(cities, Matchers.notNullValue());
        names = Sets.newHashSet();
        for (Terms.Bucket city : cities) {
            names.add(city.getKey().string());
        }
        assertThat(names.contains("berlin") && names.contains("prague"), is(true));

        bucket = geoDist.getByKey("1000.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("1000.0-*"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
        assertThat(bucket.getDocCount(), equalTo(1l));
        assertThat(bucket.getAggregations().asList().isEmpty(), is(false));
        cities = bucket.getAggregations().get("cities");
        assertThat(cities, Matchers.notNullValue());
        names = Sets.newHashSet();
        for (Terms.Bucket city : cities) {
            names.add(city.getKey().string());
        }
        assertThat(names.contains("tel-aviv"), is(true));
    }

    @Test
    public void emptyAggregation() throws Exception {
        prepareCreate("empty_bucket_idx").addMapping("type", "value", "type=integer", "location", "type=geo_point").execute().actionGet();
        List<IndexRequestBuilder> builders = new ArrayList<IndexRequestBuilder>();
        for (int i = 0; i < 2; i++) {
            builders.add(client().prepareIndex("empty_bucket_idx", "type", "" + i).setSource(jsonBuilder()
                    .startObject()
                    .field("value", i * 2)
                    .field("location", "52.0945, 5.116")
                    .endObject()));
        }
        indexRandom(true, builders.toArray(new IndexRequestBuilder[builders.size()]));

        SearchResponse searchResponse = client().prepareSearch("empty_bucket_idx")
                .setQuery(matchAllQuery())
                .addAggregation(histogram("histo").field("value").interval(1l).emptyBuckets(true)
                        .subAggregation(geoDistance("geo_dist").field("location").point("52.3760, 4.894").addRange("0-100", 0.0, 100.0)))
                .execute().actionGet();

        assertThat(searchResponse.getHits().getTotalHits(), equalTo(2l));
        Histogram histo = searchResponse.getAggregations().get("histo");
        assertThat(histo, Matchers.notNullValue());
        Histogram.Bucket bucket = histo.getByKey(1l);
        assertThat(bucket, Matchers.notNullValue());

        GeoDistance geoDistance = bucket.getAggregations().get("geo_dist");
        assertThat(geoDistance, Matchers.notNullValue());
        assertThat(geoDistance.getName(), equalTo("geo_dist"));
        assertThat(geoDistance.buckets().size(), is(1));
        assertThat(geoDistance.buckets().get(0).getKey(), equalTo("0-100"));
        assertThat(geoDistance.buckets().get(0).getFrom(), equalTo(0.0));
        assertThat(geoDistance.buckets().get(0).getTo(), equalTo(100.0));
        assertThat(geoDistance.buckets().get(0).getDocCount(), equalTo(0l));
    }

    @Test
    public void multiValues() throws Exception {
        SearchResponse response = client().prepareSearch("idx-multi")
                .addAggregation(geoDistance("amsterdam_rings")
                        .field("location")
                        .unit(DistanceUnit.KILOMETERS)
                        .distanceType(org.elasticsearch.common.geo.GeoDistance.ARC)
                        .point("52.3760, 4.894") // coords of amsterdam
                        .addUnboundedTo(500)
                        .addRange(500, 1000)
                        .addUnboundedFrom(1000))
                .execute().actionGet();

        assertSearchResponse(response);

        GeoDistance geoDist = response.getAggregations().get("amsterdam_rings");
        assertThat(geoDist, notNullValue());
        assertThat(geoDist.getName(), equalTo("amsterdam_rings"));
        assertThat(geoDist.buckets().size(), equalTo(3));

        GeoDistance.Bucket bucket = geoDist.getByKey("*-500.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("*-500.0"));
        assertThat(bucket.getFrom(), equalTo(0.0));
        assertThat(bucket.getTo(), equalTo(500.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("500.0-1000.0");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("500.0-1000.0"));
        assertThat(bucket.getFrom(), equalTo(500.0));
        assertThat(bucket.getTo(), equalTo(1000.0));
        assertThat(bucket.getDocCount(), equalTo(2l));

        bucket = geoDist.getByKey("1000.0-*");
        assertThat(bucket, notNullValue());
        assertThat(bucket.getKey(), equalTo("1000.0-*"));
        assertThat(bucket.getFrom(), equalTo(1000.0));
        assertThat(bucket.getTo(), equalTo(Double.POSITIVE_INFINITY));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9874.java