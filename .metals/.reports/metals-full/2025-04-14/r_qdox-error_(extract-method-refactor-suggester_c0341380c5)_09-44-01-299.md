error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10350.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10350.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10350.java
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
package org.elasticsearch.common.util.concurrent;

import org.elasticsearch.common.Priority;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 *
 */
public class PrioritizedExecutorsTests extends ElasticsearchTestCase {

    @Test
    public void testPriorityQueue() throws Exception {
        PriorityBlockingQueue<Priority> queue = new PriorityBlockingQueue<Priority>();
        queue.add(Priority.LANGUID);
        queue.add(Priority.NORMAL);
        queue.add(Priority.HIGH);
        queue.add(Priority.LOW);
        queue.add(Priority.URGENT);

        assertThat(queue.poll(), equalTo(Priority.URGENT));
        assertThat(queue.poll(), equalTo(Priority.HIGH));
        assertThat(queue.poll(), equalTo(Priority.NORMAL));
        assertThat(queue.poll(), equalTo(Priority.LOW));
        assertThat(queue.poll(), equalTo(Priority.LANGUID));
    }

    @Test
    public void testSubmitPrioritizedExecutorWithRunnables() throws Exception {
        ExecutorService executor = EsExecutors.newSinglePrioritizing(Executors.defaultThreadFactory());
        List<Integer> results = new ArrayList<Integer>(7);
        CountDownLatch awaitingLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(7);
        executor.submit(new AwaitingJob(awaitingLatch));
        executor.submit(new Job(6, Priority.LANGUID, results, finishedLatch));
        executor.submit(new Job(4, Priority.LOW, results, finishedLatch));
        executor.submit(new Job(1, Priority.HIGH, results, finishedLatch));
        executor.submit(new Job(5, Priority.LOW, results, finishedLatch)); // will execute after the first LOW (fifo)
        executor.submit(new Job(0, Priority.URGENT, results, finishedLatch));
        executor.submit(new Job(3, Priority.NORMAL, results, finishedLatch));
        executor.submit(new Job(2, Priority.HIGH, results, finishedLatch)); // will execute after the first HIGH (fifo)
        awaitingLatch.countDown();
        finishedLatch.await();

        assertThat(results.size(), equalTo(7));
        assertThat(results.get(0), equalTo(0));
        assertThat(results.get(1), equalTo(1));
        assertThat(results.get(2), equalTo(2));
        assertThat(results.get(3), equalTo(3));
        assertThat(results.get(4), equalTo(4));
        assertThat(results.get(5), equalTo(5));
        assertThat(results.get(6), equalTo(6));
    }

    @Test
    public void testExecutePrioritizedExecutorWithRunnables() throws Exception {
        ExecutorService executor = EsExecutors.newSinglePrioritizing(Executors.defaultThreadFactory());
        List<Integer> results = new ArrayList<Integer>(7);
        CountDownLatch awaitingLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(7);
        executor.execute(new AwaitingJob(awaitingLatch));
        executor.execute(new Job(6, Priority.LANGUID, results, finishedLatch));
        executor.execute(new Job(4, Priority.LOW, results, finishedLatch));
        executor.execute(new Job(1, Priority.HIGH, results, finishedLatch));
        executor.execute(new Job(5, Priority.LOW, results, finishedLatch)); // will execute after the first LOW (fifo)
        executor.execute(new Job(0, Priority.URGENT, results, finishedLatch));
        executor.execute(new Job(3, Priority.NORMAL, results, finishedLatch));
        executor.execute(new Job(2, Priority.HIGH, results, finishedLatch)); // will execute after the first HIGH (fifo)
        awaitingLatch.countDown();
        finishedLatch.await();

        assertThat(results.size(), equalTo(7));
        assertThat(results.get(0), equalTo(0));
        assertThat(results.get(1), equalTo(1));
        assertThat(results.get(2), equalTo(2));
        assertThat(results.get(3), equalTo(3));
        assertThat(results.get(4), equalTo(4));
        assertThat(results.get(5), equalTo(5));
        assertThat(results.get(6), equalTo(6));
    }

    @Test
    public void testSubmitPrioritizedExecutorWithCallables() throws Exception {
        ExecutorService executor = EsExecutors.newSinglePrioritizing(Executors.defaultThreadFactory());
        List<Integer> results = new ArrayList<Integer>(7);
        CountDownLatch awaitingLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(7);
        executor.submit(new AwaitingJob(awaitingLatch));
        executor.submit(new CallableJob(6, Priority.LANGUID, results, finishedLatch));
        executor.submit(new CallableJob(4, Priority.LOW, results, finishedLatch));
        executor.submit(new CallableJob(1, Priority.HIGH, results, finishedLatch));
        executor.submit(new CallableJob(5, Priority.LOW, results, finishedLatch)); // will execute after the first LOW (fifo)
        executor.submit(new CallableJob(0, Priority.URGENT, results, finishedLatch));
        executor.submit(new CallableJob(3, Priority.NORMAL, results, finishedLatch));
        executor.submit(new CallableJob(2, Priority.HIGH, results, finishedLatch)); // will execute after the first HIGH (fifo)
        awaitingLatch.countDown();
        finishedLatch.await();

        assertThat(results.size(), equalTo(7));
        assertThat(results.get(0), equalTo(0));
        assertThat(results.get(1), equalTo(1));
        assertThat(results.get(2), equalTo(2));
        assertThat(results.get(3), equalTo(3));
        assertThat(results.get(4), equalTo(4));
        assertThat(results.get(5), equalTo(5));
        assertThat(results.get(6), equalTo(6));
    }

    @Test
    public void testSubmitPrioritizedExecutorWithMixed() throws Exception {
        ExecutorService executor = EsExecutors.newSinglePrioritizing(Executors.defaultThreadFactory());
        List<Integer> results = new ArrayList<Integer>(7);
        CountDownLatch awaitingLatch = new CountDownLatch(1);
        CountDownLatch finishedLatch = new CountDownLatch(7);
        executor.submit(new AwaitingJob(awaitingLatch));
        executor.submit(new CallableJob(6, Priority.LANGUID, results, finishedLatch));
        executor.submit(new Job(4, Priority.LOW, results, finishedLatch));
        executor.submit(new CallableJob(1, Priority.HIGH, results, finishedLatch));
        executor.submit(new Job(5, Priority.LOW, results, finishedLatch)); // will execute after the first LOW (fifo)
        executor.submit(new CallableJob(0, Priority.URGENT, results, finishedLatch));
        executor.submit(new Job(3, Priority.NORMAL, results, finishedLatch));
        executor.submit(new CallableJob(2, Priority.HIGH, results, finishedLatch)); // will execute after the first HIGH (fifo)
        awaitingLatch.countDown();
        finishedLatch.await();

        assertThat(results.size(), equalTo(7));
        assertThat(results.get(0), equalTo(0));
        assertThat(results.get(1), equalTo(1));
        assertThat(results.get(2), equalTo(2));
        assertThat(results.get(3), equalTo(3));
        assertThat(results.get(4), equalTo(4));
        assertThat(results.get(5), equalTo(5));
        assertThat(results.get(6), equalTo(6));
    }

    @Test
    public void testTimeout() throws Exception {
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        PrioritizedEsThreadPoolExecutor executor = EsExecutors.newSinglePrioritizing(Executors.defaultThreadFactory());
        final CountDownLatch block = new CountDownLatch(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    block.await();
                } catch (InterruptedException e) {
                    assert false;
                }
            }

            @Override
            public String toString() {
                return "the blocking";
            }
        });

        final AtomicBoolean executeCalled = new AtomicBoolean();
        final CountDownLatch timedOut = new CountDownLatch(1);
        executor.execute(new Runnable() {
                             @Override
                             public void run() {
                                 executeCalled.set(true);
                             }

                             @Override
                             public String toString() {
                                 return "the waiting";
                             }
                         }, timer, TimeValue.timeValueMillis(100) /* enough timeout to catch them in the pending list... */, new Runnable() {
                    @Override
                    public void run() {
                        timedOut.countDown();
                    }
                }
        );

        PrioritizedEsThreadPoolExecutor.Pending[] pending = executor.getPending();
        assertThat(pending.length, equalTo(1));
        assertThat(pending[0].task.toString(), equalTo("the waiting"));

        assertThat(timedOut.await(2, TimeUnit.SECONDS), equalTo(true));
        block.countDown();
        Thread.sleep(100); // sleep a bit to double check that execute on the timed out update task is not called...
        assertThat(executeCalled.get(), equalTo(false));

        timer.shutdownNow();
        executor.shutdownNow();
    }

    static class AwaitingJob extends PrioritizedRunnable {

        private final CountDownLatch latch;

        private AwaitingJob(CountDownLatch latch) {
            super(Priority.URGENT);
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Job extends PrioritizedRunnable {

        private final int result;
        private final List<Integer> results;
        private final CountDownLatch latch;

        Job(int result, Priority priority, List<Integer> results, CountDownLatch latch) {
            super(priority);
            this.result = result;
            this.results = results;
            this.latch = latch;
        }

        @Override
        public void run() {
            results.add(result);
            latch.countDown();
        }
    }

    static class CallableJob extends PrioritizedCallable<Integer> {

        private final int result;
        private final List<Integer> results;
        private final CountDownLatch latch;

        CallableJob(int result, Priority priority, List<Integer> results, CountDownLatch latch) {
            super(priority);
            this.result = result;
            this.results = results;
            this.latch = latch;
        }

        @Override
        public Integer call() throws Exception {
            results.add(result);
            latch.countDown();
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/10350.java