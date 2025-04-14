error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3895.java
text:
```scala
i@@nternalCluster().wipeIndices("test");

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
package org.elasticsearch.indexing;

import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.elasticsearch.test.junit.annotations.TestLogging;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 *
 */
public class IndexActionTests extends ElasticsearchIntegrationTest {

    /**
     * This test tries to simulate load while creating an index and indexing documents
     * while the index is being created.
     */
    @Test
    @TestLogging("action.search:TRACE,indices.recovery:TRACE,index.shard.service:TRACE")
    public void testAutoGenerateIdNoDuplicates() throws Exception {
        int numberOfIterations = randomIntBetween(10, 50);
        for (int i = 0; i < numberOfIterations; i++) {
            Throwable firstError = null;
            createIndex("test");
            int numOfDocs = randomIntBetween(10, 100);
            logger.info("indexing [{}] docs", numOfDocs);
            List<IndexRequestBuilder> builders = new ArrayList<>(numOfDocs);
            for (int j = 0; j < numOfDocs; j++) {
                builders.add(client().prepareIndex("test", "type").setSource("field", "value"));
            }
            indexRandom(true, builders);
            logger.info("verifying indexed content");
            int numOfChecks = randomIntBetween(8, 12);
            for (int j = 0; j < numOfChecks; j++) {
                try {
                    logger.debug("running search with all types");
                    assertHitCount(client().prepareSearch("test").get(), numOfDocs);
                } catch (Throwable t) {
                    logger.error("search for all docs types failed", t);
                    if (firstError == null) {
                        firstError = t;
                    }
                }
                try {
                    logger.debug("running search with a specific type");
                    assertHitCount(client().prepareSearch("test").setTypes("type").get(), numOfDocs);
                } catch (Throwable t) {
                    logger.error("search for all docs of a specific type failed", t);
                    if (firstError == null) {
                        firstError = t;
                    }
                }
            }
            if (firstError != null) {
                fail(firstError.getMessage());
            }
            cluster().wipeIndices("test");
        }
    }

    @Test
    public void testCreatedFlag() throws Exception {
        createIndex("test");
        ensureGreen();

        IndexResponse indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_1").execute().actionGet();
        assertTrue(indexResponse.isCreated());

        indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_2").execute().actionGet();
        assertFalse(indexResponse.isCreated());

        client().prepareDelete("test", "type", "1").execute().actionGet();

        indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_2").execute().actionGet();
        assertTrue(indexResponse.isCreated());

    }

    @Test
    public void testCreatedFlagWithFlush() throws Exception {
        createIndex("test");
        ensureGreen();

        IndexResponse indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_1").execute().actionGet();
        assertTrue(indexResponse.isCreated());

        client().prepareDelete("test", "type", "1").execute().actionGet();

        flush();

        indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_2").execute().actionGet();
        assertTrue(indexResponse.isCreated());
    }

    @Test
    public void testCreatedFlagParallelExecution() throws Exception {
        createIndex("test");
        ensureGreen();

        int threadCount = 20;
        final int docCount = 300;
        int taskCount = docCount * threadCount;

        final AtomicIntegerArray createdCounts = new AtomicIntegerArray(docCount);
        ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
        List<Callable<Void>> tasks = new ArrayList<>(taskCount);
        final Random random = getRandom();
        for (int i=0;i< taskCount; i++ ) {
            tasks.add(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    int docId = random.nextInt(docCount);
                    IndexResponse indexResponse = index("test", "type", Integer.toString(docId), "field1", "value");
                    if (indexResponse.isCreated()) createdCounts.incrementAndGet(docId);
                    return null;
                }
            });
        }

        threadPool.invokeAll(tasks);

        for (int i=0;i<docCount;i++) {
            assertThat(createdCounts.get(i), lessThanOrEqualTo(1));
        }
    }

    @Test
    public void testCreatedFlagWithExternalVersioning() throws Exception {
        createIndex("test");
        ensureGreen();

        IndexResponse indexResponse = client().prepareIndex("test", "type", "1").setSource("field1", "value1_1").setVersion(123)
                                              .setVersionType(VersionType.EXTERNAL).execute().actionGet();
        assertTrue(indexResponse.isCreated());
    }

    @Test
    public void testCreateFlagWithBulk() {
        createIndex("test");
        ensureGreen();

        BulkResponse bulkResponse = client().prepareBulk().add(client().prepareIndex("test", "type", "1").setSource("field1", "value1_1")).execute().actionGet();
        assertThat(bulkResponse.hasFailures(), equalTo(false));
        assertThat(bulkResponse.getItems().length, equalTo(1));
        IndexResponse indexResponse = bulkResponse.getItems()[0].getResponse();
        assertTrue(indexResponse.isCreated());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3895.java