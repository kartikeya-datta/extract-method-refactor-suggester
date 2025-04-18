error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/480.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/480.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/480.java
text:
```scala
i@@f (original == searchContext.query() || original == searchContext.parsedQuery().query()) {

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
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

package org.elasticsearch.search.internal;

import org.apache.lucene.search.*;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.lucene.MultiCollector;
import org.elasticsearch.common.lucene.search.ExtendedIndexSearcher;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.search.dfs.CachedDfSource;

import java.io.IOException;
import java.util.List;

/**
 * @author kimchy (shay.banon)
 */
public class ContextIndexSearcher extends ExtendedIndexSearcher {

    private SearchContext searchContext;

    private CachedDfSource dfSource;

    private List<Collector> collectors;

    private List<Collector> globalCollectors;

    private boolean useGlobalCollectors = false;

    public ContextIndexSearcher(SearchContext searchContext, Engine.Searcher searcher) {
        super(searcher.searcher());
        this.searchContext = searchContext;
    }

    public void dfSource(CachedDfSource dfSource) {
        this.dfSource = dfSource;
    }

    public void addCollector(Collector collector) {
        if (collectors == null) {
            collectors = Lists.newArrayList();
        }
        collectors.add(collector);
    }

    public List<Collector> collectors() {
        return collectors;
    }

    public void addGlobalCollector(Collector collector) {
        if (globalCollectors == null) {
            globalCollectors = Lists.newArrayList();
        }
        globalCollectors.add(collector);
    }

    public List<Collector> globalCollectors() {
        return globalCollectors;
    }

    public void useGlobalCollectors(boolean useGlobalCollectors) {
        this.useGlobalCollectors = useGlobalCollectors;
    }

    @Override public Query rewrite(Query original) throws IOException {
        if (original == searchContext.query() || original == searchContext.originalQuery()) {
            // optimize in case its the top level search query and we already rewrote it...
            if (searchContext.queryRewritten()) {
                return searchContext.query();
            }
            Query rewriteQuery = super.rewrite(original);
            searchContext.updateRewriteQuery(rewriteQuery);
            return rewriteQuery;
        } else {
            return super.rewrite(original);
        }
    }

    @Override protected Weight createWeight(Query query) throws IOException {
        if (dfSource == null) {
            return super.createWeight(query);
        }
        return query.weight(dfSource);
    }

    @Override public void search(Weight weight, Filter filter, Collector collector) throws IOException {
        if (searchContext.timeout() != null) {
            collector = new TimeLimitingCollector(collector, searchContext.timeout().millis());
        }
        if (useGlobalCollectors) {
            if (globalCollectors != null) {
                collector = new MultiCollector(collector, globalCollectors.toArray(new Collector[globalCollectors.size()]));
            }
        } else {
            if (collectors != null) {
                collector = new MultiCollector(collector, collectors.toArray(new Collector[collectors.size()]));
            }
        }
        // we only compute the doc id set once since within a context, we execute the same query always...
        if (searchContext.timeout() != null) {
            searchContext.queryResult().searchTimedOut(false);
            try {
                super.search(weight, filter, collector);
            } catch (TimeLimitingCollector.TimeExceededException e) {
                searchContext.queryResult().searchTimedOut(true);
            }
        } else {
            super.search(weight, filter, collector);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/480.java