error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1476.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1476.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1476.java
text:
```scala
public static final i@@nt MIN_LARGE_INTERVAL = 1;

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

package org.elasticsearch.action.bench;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Map;

import static org.elasticsearch.test.ElasticsearchIntegrationTest.between;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.randomFrom;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.randomBoolean;
import static org.elasticsearch.test.ElasticsearchIntegrationTest.randomAsciiOfLengthBetween;

/**
 * Utilities for building randomized benchmark tests.
 */
public class BenchmarkTestUtil {

    public static final int MIN_MULTIPLIER = 10;
    public static final int MAX_MULTIPLIER = 500;
    public static final int MIN_SMALL_INTERVAL = 1;
    public static final int MAX_SMALL_INTERVAL = 3;
    public static final int MIN_LARGE_INTERVAL = 5;
    public static final int MAX_LARGE_INTERVAL = 7;

    public static final String BENCHMARK_NAME = "test_benchmark";
    public static final String COMPETITOR_PREFIX = "competitor_";
    public static final String INDEX_TYPE = "test_type";

    public static final SearchType[] searchTypes = { SearchType.DFS_QUERY_THEN_FETCH,
                                                     SearchType.QUERY_THEN_FETCH,
                                                     SearchType.QUERY_AND_FETCH,
                                                     SearchType.DFS_QUERY_AND_FETCH,
                                                     SearchType.COUNT };

    public static enum TestIndexField {
        INT_FIELD("int_field"),
        FLOAT_FIELD("float_field"),
        BOOLEAN_FIELD("boolean_field"),
        STRING_FIELD("string_field");

        final String name;

        TestIndexField(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static enum TestQueryType {
        MATCH_ALL {
            @Override
            QueryBuilder getQuery() {
                return QueryBuilders.matchAllQuery();
            }
        },
        MATCH {
            @Override
            QueryBuilder getQuery() {
                return QueryBuilders.matchQuery(TestIndexField.STRING_FIELD.toString(),
                            randomAsciiOfLengthBetween(MIN_SMALL_INTERVAL, MAX_SMALL_INTERVAL));
            }
        },
        TERM {
            @Override
            QueryBuilder getQuery() {
                return QueryBuilders.termQuery(TestIndexField.STRING_FIELD.toString(),
                            randomAsciiOfLengthBetween(MIN_SMALL_INTERVAL, MAX_SMALL_INTERVAL));
            }
        },
        QUERY_STRING {
            @Override
            QueryBuilder getQuery() {
                return QueryBuilders.queryString(
                            randomAsciiOfLengthBetween(MIN_SMALL_INTERVAL, MAX_SMALL_INTERVAL));
            }
        },
        WILDCARD {
            @Override
            QueryBuilder getQuery() {
                return QueryBuilders.wildcardQuery(
                            TestIndexField.STRING_FIELD.toString(), randomBoolean() ? "*" : "?");
            }
        };

        abstract QueryBuilder getQuery();
    }

    public static BenchmarkRequest randomRequest(Client client, String[] indices, int numExecutorNodes,
                                                 Map<String, BenchmarkSettings> competitionSettingsMap,
                                                 int lowRandomIntervalBound, int highRandomIntervalBound) {

        final BenchmarkRequestBuilder builder = new BenchmarkRequestBuilder(client, indices);
        final BenchmarkSettings settings = randomSettings(lowRandomIntervalBound, highRandomIntervalBound);

        builder.setIterations(settings.iterations());
        builder.setConcurrency(settings.concurrency());
        builder.setMultiplier(settings.multiplier());
        builder.setSearchType(settings.searchType());
        builder.setWarmup(settings.warmup());
        builder.setNumExecutorNodes(numExecutorNodes);

        final int numCompetitors = between(lowRandomIntervalBound, highRandomIntervalBound);
        for (int i = 0; i < numCompetitors; i++) {
            builder.addCompetitor(randomCompetitor(client, COMPETITOR_PREFIX + i, indices,
                    competitionSettingsMap, lowRandomIntervalBound, highRandomIntervalBound));
        }

        final BenchmarkRequest request = builder.request();
        request.benchmarkName(BENCHMARK_NAME);
        request.cascadeGlobalSettings();
        request.applyLateBoundSettings(indices, new String[] { INDEX_TYPE });

        return request;
    }

    public static BenchmarkRequest randomRequest(Client client, String[] indices, int numExecutorNodes,
                                           Map<String, BenchmarkSettings> competitionSettingsMap) {

        return randomRequest(client, indices, numExecutorNodes,
                competitionSettingsMap, MIN_SMALL_INTERVAL, MAX_SMALL_INTERVAL);
    }

    public static SearchRequest randomSearch(Client client, String[] indices) {

        final SearchRequestBuilder builder = new SearchRequestBuilder(client);
        builder.setIndices(indices);
        builder.setTypes(INDEX_TYPE);
        builder.setQuery(randomFrom(TestQueryType.values()).getQuery());
        return builder.request();
    }

    public static BenchmarkCompetitor randomCompetitor(Client client, String name, String[] indices,
                                                Map<String, BenchmarkSettings> competitionSettingsMap,
                                                int lowRandomIntervalBound, int highRandomIntervalBound) {

        final BenchmarkCompetitorBuilder builder = new BenchmarkCompetitorBuilder();
        final BenchmarkSettings settings = randomSettings(lowRandomIntervalBound, highRandomIntervalBound);

        builder.setClearCachesSettings(randomCacheSettings());
        builder.setIterations(settings.iterations());
        builder.setConcurrency(settings.concurrency());
        builder.setMultiplier(settings.multiplier());
        builder.setSearchType(settings.searchType());
        builder.setWarmup(settings.warmup());
        builder.setName(name);

        final int numSearches = between(lowRandomIntervalBound, highRandomIntervalBound);
        for (int i = 0; i < numSearches; i++) {
            final SearchRequest searchRequest = randomSearch(client, indices);
            builder.addSearchRequest(searchRequest);
            settings.addSearchRequest(searchRequest);
        }

        if (competitionSettingsMap != null) {
            competitionSettingsMap.put(name, settings);
        }

        return builder.build();
    }

    public static BenchmarkSettings.ClearCachesSettings randomCacheSettings() {

        final BenchmarkSettings.ClearCachesSettings settings = new BenchmarkSettings.ClearCachesSettings();

        settings.filterCache(randomBoolean());
        settings.fieldDataCache(randomBoolean());
        settings.idCache(randomBoolean());
        settings.recycler(randomBoolean());

        if (randomBoolean()) {
            final int numFieldsToClear = between(1, TestIndexField.values().length);
            final String[] fields = new String[numFieldsToClear];
            for (int i = 0; i < numFieldsToClear; i++) {
                fields[i] = TestIndexField.values()[i].toString();
            }
            settings.fields(fields);
        }

        return settings;
    }

    public static BenchmarkSettings randomSettings(int lowRandomIntervalBound, int highRandomIntervalBound) {

        final BenchmarkSettings settings = new BenchmarkSettings();

        settings.concurrency(between(lowRandomIntervalBound, highRandomIntervalBound), true);
        settings.iterations(between(lowRandomIntervalBound, highRandomIntervalBound), true);
        settings.multiplier(between(MIN_MULTIPLIER, MAX_MULTIPLIER), true);
        settings.warmup(randomBoolean(), true);
        settings.searchType(searchTypes[between(0, searchTypes.length - 1)], true);

        return settings;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/1476.java