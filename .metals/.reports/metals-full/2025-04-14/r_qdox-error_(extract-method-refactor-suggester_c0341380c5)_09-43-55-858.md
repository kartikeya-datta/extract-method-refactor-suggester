error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9188.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9188.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9188.java
text:
```scala
f@@pBinders.addBinding().to(GeohashCellFilter.Parser.class).asEagerSingleton();

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

import com.google.common.collect.Sets;
import org.elasticsearch.common.geo.ShapesAvailability;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.multibindings.Multibinder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryParser;

import java.util.Set;

public class IndicesQueriesModule extends AbstractModule {

    private Set<Class<QueryParser>> queryParsersClasses = Sets.newHashSet();
    private Set<QueryParser> queryParsers = Sets.newHashSet();
    private Set<Class<FilterParser>> filterParsersClasses = Sets.newHashSet();
    private Set<FilterParser> filterParsers = Sets.newHashSet();

    public synchronized IndicesQueriesModule addQuery(Class<QueryParser> queryParser) {
        queryParsersClasses.add(queryParser);
        return this;
    }

    public synchronized IndicesQueriesModule addQuery(QueryParser queryParser) {
        queryParsers.add(queryParser);
        return this;
    }

    public synchronized IndicesQueriesModule addFilter(Class<FilterParser> filterParser) {
        filterParsersClasses.add(filterParser);
        return this;
    }

    public synchronized IndicesQueriesModule addFilter(FilterParser filterParser) {
        filterParsers.add(filterParser);
        return this;
    }

    @Override
    protected void configure() {
        bind(IndicesQueriesRegistry.class).asEagerSingleton();

        Multibinder<QueryParser> qpBinders = Multibinder.newSetBinder(binder(), QueryParser.class);
        for (Class<QueryParser> queryParser : queryParsersClasses) {
            qpBinders.addBinding().to(queryParser).asEagerSingleton();
        }
        for (QueryParser queryParser : queryParsers) {
            qpBinders.addBinding().toInstance(queryParser);
        }
        qpBinders.addBinding().to(MatchQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(MultiMatchQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(NestedQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(HasChildQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(HasParentQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(TopChildrenQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(DisMaxQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(IdsQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(MatchAllQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(QueryStringQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(BoostingQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(BoolQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(TermQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(TermsQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FuzzyQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(RegexpQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FieldQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(RangeQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(PrefixQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(WildcardQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FilteredQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(ConstantScoreQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(CustomBoostFactorQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(CustomScoreQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(CustomFiltersScoreQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanTermQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanNotQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FieldMaskingSpanQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanFirstQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanNearQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanOrQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(MoreLikeThisQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(MoreLikeThisFieldQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FuzzyLikeThisQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FuzzyLikeThisFieldQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(WrapperQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(IndicesQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(CommonTermsQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(SpanMultiTermQueryParser.class).asEagerSingleton();
        qpBinders.addBinding().to(FunctionScoreQueryParser.class).asEagerSingleton();

        if (ShapesAvailability.JTS_AVAILABLE) {
            qpBinders.addBinding().to(GeoShapeQueryParser.class).asEagerSingleton();
        }

        Multibinder<FilterParser> fpBinders = Multibinder.newSetBinder(binder(), FilterParser.class);
        for (Class<FilterParser> filterParser : filterParsersClasses) {
            fpBinders.addBinding().to(filterParser).asEagerSingleton();
        }
        for (FilterParser filterParser : filterParsers) {
            fpBinders.addBinding().toInstance(filterParser);
        }
        fpBinders.addBinding().to(HasChildFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(HasParentFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(NestedFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(TypeFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(IdsFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(LimitFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(TermFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(TermsFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(RangeFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(NumericRangeFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(PrefixFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(RegexpFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(ScriptFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(GeoDistanceFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(GeoDistanceRangeFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(GeoBoundingBoxFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(GeohashFilter.Parser.class).asEagerSingleton();
        fpBinders.addBinding().to(GeoPolygonFilterParser.class).asEagerSingleton();
        if (ShapesAvailability.JTS_AVAILABLE) {
            fpBinders.addBinding().to(GeoShapeFilterParser.class).asEagerSingleton();
        }
        fpBinders.addBinding().to(QueryFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(FQueryFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(BoolFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(AndFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(OrFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(NotFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(MatchAllFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(ExistsFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(MissingFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(IndicesFilterParser.class).asEagerSingleton();
        fpBinders.addBinding().to(WrapperFilterParser.class).asEagerSingleton();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9188.java