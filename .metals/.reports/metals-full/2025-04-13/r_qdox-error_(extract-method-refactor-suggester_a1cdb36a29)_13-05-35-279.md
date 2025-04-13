error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4045.java
text:
```scala
a@@ddQueryParser(queryParsers, new MatchQueryParser());

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

package org.elasticsearch.indices.query;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.*;

import java.util.Map;

/**
 *
 */
public class IndicesQueriesRegistry {

    private ImmutableMap<String, QueryParser> queryParsers;
    private ImmutableMap<String, FilterParser> filterParsers;

    @Inject
    public IndicesQueriesRegistry(Settings settings, @Nullable ClusterService clusterService) {
        Map<String, QueryParser> queryParsers = Maps.newHashMap();
        addQueryParser(queryParsers, new TextQueryParser());
        addQueryParser(queryParsers, new NestedQueryParser());
        addQueryParser(queryParsers, new HasChildQueryParser());
        addQueryParser(queryParsers, new TopChildrenQueryParser());
        addQueryParser(queryParsers, new DisMaxQueryParser());
        addQueryParser(queryParsers, new IdsQueryParser());
        addQueryParser(queryParsers, new MatchAllQueryParser());
        addQueryParser(queryParsers, new QueryStringQueryParser(settings));
        addQueryParser(queryParsers, new BoostingQueryParser());
        addQueryParser(queryParsers, new BoolQueryParser(settings));
        addQueryParser(queryParsers, new TermQueryParser());
        addQueryParser(queryParsers, new TermsQueryParser());
        addQueryParser(queryParsers, new FuzzyQueryParser());
        addQueryParser(queryParsers, new FieldQueryParser(settings));
        addQueryParser(queryParsers, new RangeQueryParser());
        addQueryParser(queryParsers, new PrefixQueryParser());
        addQueryParser(queryParsers, new WildcardQueryParser());
        addQueryParser(queryParsers, new FilteredQueryParser());
        addQueryParser(queryParsers, new ConstantScoreQueryParser());
        addQueryParser(queryParsers, new CustomBoostFactorQueryParser());
        addQueryParser(queryParsers, new CustomScoreQueryParser());
        addQueryParser(queryParsers, new CustomFiltersScoreQueryParser());
        addQueryParser(queryParsers, new SpanTermQueryParser());
        addQueryParser(queryParsers, new SpanNotQueryParser());
        addQueryParser(queryParsers, new SpanFirstQueryParser());
        addQueryParser(queryParsers, new SpanNearQueryParser());
        addQueryParser(queryParsers, new SpanOrQueryParser());
        addQueryParser(queryParsers, new MoreLikeThisQueryParser());
        addQueryParser(queryParsers, new MoreLikeThisFieldQueryParser());
        addQueryParser(queryParsers, new FuzzyLikeThisQueryParser());
        addQueryParser(queryParsers, new FuzzyLikeThisFieldQueryParser());
        addQueryParser(queryParsers, new WrapperQueryParser());
        addQueryParser(queryParsers, new IndicesQueryParser(clusterService));
        this.queryParsers = ImmutableMap.copyOf(queryParsers);

        Map<String, FilterParser> filterParsers = Maps.newHashMap();
        addFilterParser(filterParsers, new HasChildFilterParser());
        addFilterParser(filterParsers, new NestedFilterParser());
        addFilterParser(filterParsers, new TypeFilterParser());
        addFilterParser(filterParsers, new IdsFilterParser());
        addFilterParser(filterParsers, new LimitFilterParser());
        addFilterParser(filterParsers, new TermFilterParser());
        addFilterParser(filterParsers, new TermsFilterParser());
        addFilterParser(filterParsers, new RangeFilterParser());
        addFilterParser(filterParsers, new NumericRangeFilterParser());
        addFilterParser(filterParsers, new PrefixFilterParser());
        addFilterParser(filterParsers, new ScriptFilterParser());
        addFilterParser(filterParsers, new GeoDistanceFilterParser());
        addFilterParser(filterParsers, new GeoDistanceRangeFilterParser());
        addFilterParser(filterParsers, new GeoBoundingBoxFilterParser());
        addFilterParser(filterParsers, new GeoPolygonFilterParser());
        addFilterParser(filterParsers, new QueryFilterParser());
        addFilterParser(filterParsers, new FQueryFilterParser());
        addFilterParser(filterParsers, new BoolFilterParser());
        addFilterParser(filterParsers, new AndFilterParser());
        addFilterParser(filterParsers, new OrFilterParser());
        addFilterParser(filterParsers, new NotFilterParser());
        addFilterParser(filterParsers, new MatchAllFilterParser());
        addFilterParser(filterParsers, new ExistsFilterParser());
        addFilterParser(filterParsers, new MissingFilterParser());
        addFilterParser(filterParsers, new IndicesFilterParser(clusterService));
        addFilterParser(filterParsers, new WrapperFilterParser());
        this.filterParsers = ImmutableMap.copyOf(filterParsers);
    }

    /**
     * Adds a global query parser.
     */
    public void addQueryParser(QueryParser queryParser) {
        Map<String, QueryParser> queryParsers = Maps.newHashMap(this.queryParsers);
        addQueryParser(queryParsers, queryParser);
        this.queryParsers = ImmutableMap.copyOf(queryParsers);
    }

    public void addFilterParser(FilterParser filterParser) {
        Map<String, FilterParser> filterParsers = Maps.newHashMap(this.filterParsers);
        addFilterParser(filterParsers, filterParser);
        this.filterParsers = ImmutableMap.copyOf(filterParsers);
    }

    public ImmutableMap<String, QueryParser> queryParsers() {
        return queryParsers;
    }

    public ImmutableMap<String, FilterParser> filterParsers() {
        return filterParsers;
    }

    private void addQueryParser(Map<String, QueryParser> queryParsers, QueryParser queryParser) {
        for (String name : queryParser.names()) {
            queryParsers.put(name, queryParser);
        }
    }

    private void addFilterParser(Map<String, FilterParser> filterParsers, FilterParser filterParser) {
        for (String name : filterParser.names()) {
            filterParsers.put(name, filterParser);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4045.java