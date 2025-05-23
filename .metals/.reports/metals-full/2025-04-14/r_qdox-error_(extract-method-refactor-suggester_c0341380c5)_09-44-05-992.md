error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5101.java
text:
```scala
c@@ollector = Lucene.wrapTimeLimitingCollector(collector, searchContext.timeEstimateCounter(), searchContext.timeoutInMillis());

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

package org.elasticsearch.search.internal;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.*;
import org.elasticsearch.common.lease.Releasable;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.lucene.MinimumScoreCollector;
import org.elasticsearch.common.lucene.MultiCollector;
import org.elasticsearch.common.lucene.search.FilteredCollector;
import org.elasticsearch.common.lucene.search.XCollector;
import org.elasticsearch.common.lucene.search.XFilteredQuery;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.search.dfs.CachedDfSource;
import org.elasticsearch.search.internal.SearchContext.Lifetime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Context-aware extension of {@link IndexSearcher}.
 */
public class ContextIndexSearcher extends IndexSearcher implements Releasable {

    public static enum Stage {
        NA,
        MAIN_QUERY
    }

    /** The wrapped {@link IndexSearcher}. The reason why we sometimes prefer delegating to this searcher instead of <tt>super</tt> is that
     *  this instance may have more assertions, for example if it comes from MockInternalEngine which wraps the IndexSearcher into an
     *  AssertingIndexSearcher. */
    private final IndexSearcher in;

    private final SearchContext searchContext;

    private CachedDfSource dfSource;

    private List<Collector> queryCollectors;

    private Stage currentState = Stage.NA;

    public ContextIndexSearcher(SearchContext searchContext, Engine.Searcher searcher) {
        super(searcher.reader());
        in = searcher.searcher();
        this.searchContext = searchContext;
        setSimilarity(searcher.searcher().getSimilarity());
    }

    @Override
    public void close() {
    }

    public void dfSource(CachedDfSource dfSource) {
        this.dfSource = dfSource;
    }

    /**
     * Adds a query level collector that runs at {@link Stage#MAIN_QUERY}. Note, supports
     * {@link org.elasticsearch.common.lucene.search.XCollector} allowing for a callback
     * when collection is done.
     */
    public void addMainQueryCollector(Collector collector) {
        if (queryCollectors == null) {
            queryCollectors = new ArrayList<>();
        }
        queryCollectors.add(collector);
    }

    public void inStage(Stage stage) {
        this.currentState = stage;
    }

    public void finishStage(Stage stage) {
        assert currentState == stage : "Expected stage " + stage + " but was stage " + currentState;
        this.currentState = Stage.NA;
    }

    @Override
    public Query rewrite(Query original) throws IOException {
        if (original == searchContext.query() || original == searchContext.parsedQuery().query()) {
            // optimize in case its the top level search query and we already rewrote it...
            if (searchContext.queryRewritten()) {
                return searchContext.query();
            }
            Query rewriteQuery = in.rewrite(original);
            searchContext.updateRewriteQuery(rewriteQuery);
            return rewriteQuery;
        } else {
            return in.rewrite(original);
        }
    }

    @Override
    public Weight createNormalizedWeight(Query query) throws IOException {
        try {
            // if its the main query, use we have dfs data, only then do it
            if (dfSource != null && (query == searchContext.query() || query == searchContext.parsedQuery().query())) {
                return dfSource.createNormalizedWeight(query);
            }
            return in.createNormalizedWeight(query);
        } catch (Throwable t) {
            searchContext.clearReleasables(Lifetime.COLLECTION);
            throw new RuntimeException(t);
        }
    }

    @Override
    public void search(List<AtomicReaderContext> leaves, Weight weight, Collector collector) throws IOException {
        final boolean timeoutSet = searchContext.timeoutInMillis() != -1;
        final boolean terminateAfterSet = searchContext.terminateAfter() != SearchContext.DEFAULT_TERMINATE_AFTER;

        if (timeoutSet) {
            // TODO: change to use our own counter that uses the scheduler in ThreadPool
            // throws TimeLimitingCollector.TimeExceededException when timeout has reached
            collector = Lucene.wrapTimeLimitingCollector(collector, searchContext.timeoutInMillis());
        }
        if (terminateAfterSet) {
            // throws Lucene.EarlyTerminationException when given count is reached
            collector = Lucene.wrapCountBasedEarlyTerminatingCollector(collector, searchContext.terminateAfter());
        }
        if (currentState == Stage.MAIN_QUERY) {
            if (searchContext.parsedPostFilter() != null) {
                // this will only get applied to the actual search collector and not
                // to any scoped collectors, also, it will only be applied to the main collector
                // since that is where the filter should only work
                collector = new FilteredCollector(collector, searchContext.parsedPostFilter().filter());
            }
            if (queryCollectors != null && !queryCollectors.isEmpty()) {
                collector = new MultiCollector(collector, queryCollectors.toArray(new Collector[queryCollectors.size()]));
            }

            // apply the minimum score after multi collector so we filter aggs as well
            if (searchContext.minimumScore() != null) {
                collector = new MinimumScoreCollector(collector, searchContext.minimumScore());
            }
        }

        // we only compute the doc id set once since within a context, we execute the same query always...
        try {
            if (timeoutSet || terminateAfterSet) {
                try {
                    super.search(leaves, weight, collector);
                } catch (TimeLimitingCollector.TimeExceededException e) {
                    assert timeoutSet : "TimeExceededException thrown even though timeout wasn't set";
                    searchContext.queryResult().searchTimedOut(true);
                } catch (Lucene.EarlyTerminationException e) {
                    assert terminateAfterSet : "EarlyTerminationException thrown even though terminateAfter wasn't set";
                    searchContext.queryResult().terminatedEarly(true);
                }
                if (terminateAfterSet && searchContext.queryResult().terminatedEarly() == null) {
                    searchContext.queryResult().terminatedEarly(false);
                }
            } else {
                super.search(leaves, weight, collector);
            }

            if (currentState == Stage.MAIN_QUERY) {
                if (queryCollectors != null && !queryCollectors.isEmpty()) {
                    for (Collector queryCollector : queryCollectors) {
                        if (queryCollector instanceof XCollector) {
                            ((XCollector) queryCollector).postCollection();
                        }
                    }
                }
            }
        } finally {
            searchContext.clearReleasables(Lifetime.COLLECTION);
        }
    }

    @Override
    public Explanation explain(Query query, int doc) throws IOException {
        try {
            if (searchContext.aliasFilter() == null) {
                return super.explain(query, doc);
            }
            XFilteredQuery filteredQuery = new XFilteredQuery(query, searchContext.aliasFilter());
            return super.explain(filteredQuery, doc);
        } finally {
            searchContext.clearReleasables(Lifetime.COLLECTION);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5101.java