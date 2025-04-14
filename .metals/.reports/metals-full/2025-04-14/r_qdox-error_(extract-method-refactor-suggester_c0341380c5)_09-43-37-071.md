error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5512.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5512.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5512.java
text:
```scala
t@@his(index, type, client, RandomizedTest.scaledRandomIntBetween(2, 5));

package org.elasticsearch.test;/*
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

import com.carrotsearch.randomizedtesting.RandomizedTest;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.recovery.RecoveryWhileUnderLoadTests;
import org.junit.Assert;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

public class BackgroundIndexer implements AutoCloseable {

    private final ESLogger logger = Loggers.getLogger(RecoveryWhileUnderLoadTests.class);

    final Thread[] writers;
    final CountDownLatch stopLatch;
    final CopyOnWriteArrayList<Throwable> failures;
    final AtomicBoolean stop = new AtomicBoolean(false);
    final AtomicLong idGenerator = new AtomicLong();
    final AtomicLong indexCounter = new AtomicLong();
    final CountDownLatch startLatch = new CountDownLatch(1);

    public BackgroundIndexer(String index, String type, Client client) {
        this(index, type, client, RandomizedTest.scaledRandomIntBetween(3, 10));
    }

    public BackgroundIndexer(String index, String type, Client client, int writerCount) {
        this(index, type, client, writerCount, true);
    }

    public BackgroundIndexer(final String index, final String type, final Client client, final int writerCount, boolean autoStart) {

        failures = new CopyOnWriteArrayList<>();
        writers = new Thread[writerCount];
        stopLatch = new CountDownLatch(writers.length);
        logger.info("--> starting {} indexing threads", writerCount);
        for (int i = 0; i < writers.length; i++) {
            final int indexerId = i;
            final boolean batch = RandomizedTest.getRandom().nextBoolean();
            writers[i] = new Thread() {
                @Override
                public void run() {
                    long id = -1;
                    try {
                        startLatch.await();
                        logger.info("**** starting indexing thread {}", indexerId);
                        while (!stop.get()) {
                            if (batch) {
                                int batchSize = RandomizedTest.getRandom().nextInt(20) + 1;
                                BulkRequestBuilder bulkRequest = client.prepareBulk();
                                for (int i = 0; i < batchSize; i++) {
                                    id = idGenerator.incrementAndGet();
                                    bulkRequest.add(client.prepareIndex(index, type, Long.toString(id)).setSource("test", "value" + id));
                                }
                                BulkResponse bulkResponse = bulkRequest.get();
                                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                                    if (!bulkItemResponse.isFailed()) {
                                        indexCounter.incrementAndGet();
                                    } else {
                                        throw new ElasticsearchException("bulk request failure, id: ["
                                                + bulkItemResponse.getFailure().getId() + "] message: " + bulkItemResponse.getFailure().getMessage());
                                    }
                                }

                            } else {
                                id = idGenerator.incrementAndGet();
                                client.prepareIndex(index, type, Long.toString(id) + "-" + indexerId).setSource("test", "value" + id).get();
                                indexCounter.incrementAndGet();
                            }
                        }
                        logger.info("**** done indexing thread {}", indexerId);
                    } catch (Throwable e) {
                        failures.add(e);
                        logger.warn("**** failed indexing thread {} on doc id {}", e, indexerId, id);
                    } finally {
                        stopLatch.countDown();
                    }
                }
            };
            writers[i].start();
        }

        if (autoStart) {
            startLatch.countDown();
        }
    }

    public void start() {
        startLatch.countDown();
    }

    public void stop() throws InterruptedException {
        if (stop.get()) {
            return;
        }
        stop.set(true);

        Assert.assertThat("timeout while waiting for indexing threads to stop", stopLatch.await(6, TimeUnit.MINUTES), equalTo(true));
        assertNoFailures();
    }

    public long totalIndexedDocs() {
        return indexCounter.get();
    }

    public Throwable[] getFailures() {
        return failures.toArray(new Throwable[failures.size()]);
    }

    public void assertNoFailures() {
        Assert.assertThat(failures, emptyIterable());
    }

    @Override
    public void close() throws Exception {
        stop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5512.java