error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3232.java
text:
```scala
s@@earchContext.idCache().refresh(searchContext.searcher().getTopReaderContext().leaves());

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

package org.elasticsearch.search.query;

import com.google.common.collect.ImmutableMap;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.search.SearchParseElement;
import org.elasticsearch.search.SearchPhase;
import org.elasticsearch.search.facet.FacetPhase;
import org.elasticsearch.search.internal.ContextIndexSearcher;
import org.elasticsearch.search.internal.ScopePhase;
import org.elasticsearch.search.internal.SearchContext;
import org.elasticsearch.search.sort.SortParseElement;
import org.elasticsearch.search.sort.TrackScoresParseElement;

import java.util.Map;

/**
 *
 */
public class QueryPhase implements SearchPhase {

    private final FacetPhase facetPhase;

    @Inject
    public QueryPhase(FacetPhase facetPhase) {
        this.facetPhase = facetPhase;
    }

    @Override
    public Map<String, ? extends SearchParseElement> parseElements() {
        ImmutableMap.Builder<String, SearchParseElement> parseElements = ImmutableMap.builder();
        parseElements.put("from", new FromParseElement()).put("size", new SizeParseElement())
                .put("indices_boost", new IndicesBoostParseElement())
                .put("indicesBoost", new IndicesBoostParseElement())
                .put("query", new QueryParseElement())
                .put("queryBinary", new QueryBinaryParseElement())
                .put("query_binary", new QueryBinaryParseElement())
                .put("filter", new FilterParseElement())
                .put("filterBinary", new FilterBinaryParseElement())
                .put("filter_binary", new FilterBinaryParseElement())
                .put("sort", new SortParseElement())
                .put("trackScores", new TrackScoresParseElement())
                .put("track_scores", new TrackScoresParseElement())
                .put("min_score", new MinScoreParseElement())
                .put("minScore", new MinScoreParseElement())
                .put("timeout", new TimeoutParseElement())
                .putAll(facetPhase.parseElements());
        return parseElements.build();
    }

    @Override
    public void preProcess(SearchContext context) {
        context.preProcess();
        facetPhase.preProcess(context);
    }

    public void execute(SearchContext searchContext) throws QueryPhaseExecutionException {
        searchContext.queryResult().searchTimedOut(false);
        // set the filter on the searcher
        if (searchContext.scopePhases() != null) {
            // we have scoped queries, refresh the id cache
            try {
                searchContext.idCache().refresh(searchContext.searcher().subReaders());
            } catch (Exception e) {
                throw new QueryPhaseExecutionException(searchContext, "Failed to refresh id cache for child queries", e);
            }

            // the first scope level is the most nested child
            for (ScopePhase scopePhase : searchContext.scopePhases()) {
                if (scopePhase instanceof ScopePhase.TopDocsPhase) {
                    ScopePhase.TopDocsPhase topDocsPhase = (ScopePhase.TopDocsPhase) scopePhase;
                    int numDocs = (searchContext.from() + searchContext.size());
                    if (numDocs == 0) {
                        numDocs = 1;
                    }
                    try {
                        numDocs *= topDocsPhase.factor();
                        while (true) {
                            topDocsPhase.clear();
                            if (topDocsPhase.scope() != null) {
                                searchContext.searcher().processingScope(topDocsPhase.scope());
                            }
                            TopDocs topDocs = searchContext.searcher().search(topDocsPhase.query(), numDocs);
                            if (topDocsPhase.scope() != null) {
                                // we mark the scope as processed, so we don't process it again, even if we need to rerun the query...
                                searchContext.searcher().processedScope();
                            }
                            topDocsPhase.processResults(topDocs, searchContext);

                            // check if we found enough docs, if so, break
                            if (topDocsPhase.numHits() >= (searchContext.from() + searchContext.size())) {
                                break;
                            }
                            // if we did not find enough docs, check if it make sense to search further
                            if (topDocs.totalHits <= numDocs) {
                                break;
                            }
                            // if not, update numDocs, and search again
                            numDocs *= topDocsPhase.incrementalFactor();
                            if (numDocs > topDocs.totalHits) {
                                numDocs = topDocs.totalHits;
                            }
                        }
                    } catch (Exception e) {
                        throw new QueryPhaseExecutionException(searchContext, "Failed to execute child query [" + scopePhase.query() + "]", e);
                    }
                } else if (scopePhase instanceof ScopePhase.CollectorPhase) {
                    try {
                        ScopePhase.CollectorPhase collectorPhase = (ScopePhase.CollectorPhase) scopePhase;
                        // collector phase might not require extra processing, for example, when scrolling
                        if (!collectorPhase.requiresProcessing()) {
                            continue;
                        }
                        if (scopePhase.scope() != null) {
                            searchContext.searcher().processingScope(scopePhase.scope());
                        }
                        Collector collector = collectorPhase.collector();
                        searchContext.searcher().search(collectorPhase.query(), collector);
                        collectorPhase.processCollector(collector);
                        if (collectorPhase.scope() != null) {
                            // we mark the scope as processed, so we don't process it again, even if we need to rerun the query...
                            searchContext.searcher().processedScope();
                        }
                    } catch (Exception e) {
                        throw new QueryPhaseExecutionException(searchContext, "Failed to execute child query [" + scopePhase.query() + "]", e);
                    }
                }
            }
        }

        searchContext.searcher().processingScope(ContextIndexSearcher.Scopes.MAIN);
        try {
            searchContext.queryResult().from(searchContext.from());
            searchContext.queryResult().size(searchContext.size());

            Query query = searchContext.query();

            TopDocs topDocs;
            int numDocs = searchContext.from() + searchContext.size();
            if (numDocs == 0) {
                // if 0 was asked, change it to 1 since 0 is not allowed
                numDocs = 1;
            }

            if (searchContext.searchType() == SearchType.COUNT) {
                TotalHitCountCollector collector = new TotalHitCountCollector();
                searchContext.searcher().search(query, collector);
                topDocs = new TopDocs(collector.getTotalHits(), Lucene.EMPTY_SCORE_DOCS, 0);
            } else if (searchContext.searchType() == SearchType.SCAN) {
                topDocs = searchContext.scanContext().execute(searchContext);
            } else if (searchContext.sort() != null) {
                topDocs = searchContext.searcher().search(query, null, numDocs, searchContext.sort());
            } else {
                topDocs = searchContext.searcher().search(query, numDocs);
            }
            searchContext.queryResult().topDocs(topDocs);
        } catch (Exception e) {
            throw new QueryPhaseExecutionException(searchContext, "Failed to execute main query", e);
        } finally {
            searchContext.searcher().processedScope();
        }

        facetPhase.execute(searchContext);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3232.java