error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3896.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3896.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3896.java
text:
```scala
I@@ndicesService indicesService = internalCluster().getInstance(IndicesService.class);

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
package org.elasticsearch.indices.leaks;

import org.apache.lucene.util.LuceneTestCase.BadApple;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.index.mapper.DocumentMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.service.IndexService;
import org.elasticsearch.index.shard.service.IndexShard;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest.ClusterScope;
import org.junit.Test;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.test.ElasticsearchIntegrationTest.*;
import static org.hamcrest.Matchers.nullValue;

/**
 */
@ClusterScope(scope= Scope.TEST, numDataNodes =1)
public class IndicesLeaksTests extends ElasticsearchIntegrationTest {


    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Test
    @BadApple(bugUrl = "https://github.com/elasticsearch/elasticsearch/issues/3232")
    public void testIndexShardLifecycleLeak() throws Exception {

        client().admin().indices().prepareCreate("test")
                .setSettings(ImmutableSettings.builder().put("index.number_of_shards", 1).put("index.number_of_replicas", 0))
                .execute().actionGet();

        client().admin().cluster().prepareHealth().setWaitForGreenStatus().execute().actionGet();

        IndicesService indicesService = cluster().getInstance(IndicesService.class);
        IndexService indexService = indicesService.indexServiceSafe("test");
        Injector indexInjector = indexService.injector();
        IndexShard shard = indexService.shardSafe(0);
        Injector shardInjector = indexService.shardInjector(0);

        performCommonOperations();

        List<WeakReference> indexReferences = new ArrayList<>();
        List<WeakReference> shardReferences = new ArrayList<>();

        // TODO if we could iterate over the already created classes on the injector, we can just add them here to the list
        // for now, we simple add some classes that make sense

        // add index references
        indexReferences.add(new WeakReference(indexService));
        indexReferences.add(new WeakReference(indexInjector));
        indexReferences.add(new WeakReference(indexService.mapperService()));
        for (DocumentMapper documentMapper : indexService.mapperService()) {
            indexReferences.add(new WeakReference(documentMapper));
        }
        indexReferences.add(new WeakReference(indexService.aliasesService()));
        indexReferences.add(new WeakReference(indexService.analysisService()));
        indexReferences.add(new WeakReference(indexService.fieldData()));
        indexReferences.add(new WeakReference(indexService.queryParserService()));


        // add shard references
        shardReferences.add(new WeakReference(shard));
        shardReferences.add(new WeakReference(shardInjector));

        indexService = null;
        indexInjector = null;
        shard = null;
        shardInjector = null;

        client().admin().indices().prepareDelete().execute().actionGet();

        for (int i = 0; i < 100; i++) {
            System.gc();
            int indexNotCleared = 0;
            for (WeakReference indexReference : indexReferences) {
                if (indexReference.get() != null) {
                    indexNotCleared++;
                }
            }
            int shardNotCleared = 0;
            for (WeakReference shardReference : shardReferences) {
                if (shardReference.get() != null) {
                    shardNotCleared++;
                }
            }
            logger.info("round {}, indices {}/{}, shards {}/{}", i, indexNotCleared, indexReferences.size(), shardNotCleared, shardReferences.size());
            if (indexNotCleared == 0 && shardNotCleared == 0) {
                break;
            }
        }

        //Thread.sleep(1000000);

        for (WeakReference indexReference : indexReferences) {
            assertThat("dangling index reference: " + indexReference.get(), indexReference.get(), nullValue());
        }

        for (WeakReference shardReference : shardReferences) {
            assertThat("dangling shard reference: " + shardReference.get(), shardReference.get(), nullValue());
        }
    }

    private void performCommonOperations() {
        client().prepareIndex("test", "type", "1").setSource("field1", "value", "field2", 2, "field3", 3.0f).execute().actionGet();
        client().admin().indices().prepareRefresh().execute().actionGet();
        client().prepareSearch("test").setQuery(QueryBuilders.queryString("field1:value")).execute().actionGet();
        client().prepareSearch("test").setQuery(QueryBuilders.termQuery("field1", "value")).execute().actionGet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3896.java