error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10352.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10352.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10352.java
text:
```scala
f@@ail();

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

package org.elasticsearch.index.aliases;

import org.elasticsearch.cache.recycler.CacheRecyclerModule;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.common.compress.CompressedString;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.inject.util.Providers;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.cache.IndexCacheModule;
import org.elasticsearch.index.codec.CodecModule;
import org.elasticsearch.index.engine.IndexEngineModule;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.IndexQueryParserModule;
import org.elasticsearch.index.query.IndexQueryParserService;
import org.elasticsearch.index.query.functionscore.FunctionScoreModule;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.index.similarity.SimilarityModule;
import org.elasticsearch.indices.InvalidAliasNameException;
import org.elasticsearch.indices.query.IndicesQueriesModule;
import org.elasticsearch.script.ScriptModule;
import org.elasticsearch.indices.fielddata.breaker.CircuitBreakerService;
import org.elasticsearch.indices.fielddata.breaker.DummyCircuitBreakerService;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 *
 */
public class IndexAliasesServiceTests extends ElasticsearchTestCase {
    public static IndexAliasesService newIndexAliasesService() {
        return new IndexAliasesService(new Index("test"), ImmutableSettings.Builder.EMPTY_SETTINGS, newIndexQueryParserService());
    }

    public static IndexQueryParserService newIndexQueryParserService() {
        Injector injector = new ModulesBuilder().add(
                new IndicesQueriesModule(),
                new CacheRecyclerModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new CodecModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new IndexSettingsModule(new Index("test"), ImmutableSettings.Builder.EMPTY_SETTINGS),
                new IndexNameModule(new Index("test")),
                new IndexQueryParserModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new AnalysisModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new SimilarityModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new ScriptModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new SettingsModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new IndexEngineModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new IndexCacheModule(ImmutableSettings.Builder.EMPTY_SETTINGS),
                new FunctionScoreModule(),
                new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(ClusterService.class).toProvider(Providers.of((ClusterService) null));
                        bind(CircuitBreakerService.class).to(DummyCircuitBreakerService.class);
                    }
                }
        ).createInjector();
        return injector.getInstance(IndexQueryParserService.class);
    }

    public static CompressedString filter(FilterBuilder filterBuilder) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        filterBuilder.toXContent(builder, ToXContent.EMPTY_PARAMS);
        builder.close();
        return new CompressedString(builder.string());
    }

    @Test
    public void testFilteringAliases() throws Exception {
        IndexAliasesService indexAliasesService = newIndexAliasesService();
        indexAliasesService.add("cats", filter(termFilter("animal", "cat")));
        indexAliasesService.add("dogs", filter(termFilter("animal", "dog")));
        indexAliasesService.add("all", null);

        assertThat(indexAliasesService.hasAlias("cats"), equalTo(true));
        assertThat(indexAliasesService.hasAlias("dogs"), equalTo(true));
        assertThat(indexAliasesService.hasAlias("turtles"), equalTo(false));

        assertThat(indexAliasesService.aliasFilter("cats").toString(), equalTo("cache(animal:cat)"));
        assertThat(indexAliasesService.aliasFilter("cats", "dogs").toString(), equalTo("BooleanFilter(cache(animal:cat) cache(animal:dog))"));

        // Non-filtering alias should turn off all filters because filters are ORed
        assertThat(indexAliasesService.aliasFilter("all"), nullValue());
        assertThat(indexAliasesService.aliasFilter("cats", "all"), nullValue());
        assertThat(indexAliasesService.aliasFilter("all", "cats"), nullValue());

        indexAliasesService.add("cats", filter(termFilter("animal", "feline")));
        indexAliasesService.add("dogs", filter(termFilter("animal", "canine")));
        assertThat(indexAliasesService.aliasFilter("dogs", "cats").toString(), equalTo("BooleanFilter(cache(animal:canine) cache(animal:feline))"));
    }

    @Test
    public void testAliasFilters() throws Exception {
        IndexAliasesService indexAliasesService = newIndexAliasesService();
        indexAliasesService.add("cats", filter(termFilter("animal", "cat")));
        indexAliasesService.add("dogs", filter(termFilter("animal", "dog")));

        assertThat(indexAliasesService.aliasFilter(), nullValue());
        assertThat(indexAliasesService.aliasFilter("dogs").toString(), equalTo("cache(animal:dog)"));
        assertThat(indexAliasesService.aliasFilter("dogs", "cats").toString(), equalTo("BooleanFilter(cache(animal:dog) cache(animal:cat))"));

        indexAliasesService.add("cats", filter(termFilter("animal", "feline")));
        indexAliasesService.add("dogs", filter(termFilter("animal", "canine")));

        assertThat(indexAliasesService.aliasFilter("dogs", "cats").toString(), equalTo("BooleanFilter(cache(animal:canine) cache(animal:feline))"));
    }

    @Test(expected = InvalidAliasNameException.class)
    public void testRemovedAliasFilter() throws Exception {
        IndexAliasesService indexAliasesService = newIndexAliasesService();
        indexAliasesService.add("cats", filter(termFilter("animal", "cat")));
        indexAliasesService.remove("cats");
        indexAliasesService.aliasFilter("cats");
    }


    @Test
    public void testUnknownAliasFilter() throws Exception {
        IndexAliasesService indexAliasesService = newIndexAliasesService();
        indexAliasesService.add("cats", filter(termFilter("animal", "cat")));
        indexAliasesService.add("dogs", filter(termFilter("animal", "dog")));

        try {
            indexAliasesService.aliasFilter("unknown");
            assert false;
        } catch (InvalidAliasNameException e) {
            // all is well
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10352.java