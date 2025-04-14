error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2529.java
text:
```scala
v@@.close();

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

package org.elasticsearch.benchmark.common.recycler;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.common.recycler.AbstractRecyclerC;
import org.elasticsearch.common.recycler.Recycler;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.elasticsearch.common.recycler.Recyclers.*;

/** Benchmark that tries to measure the overhead of object recycling depending on concurrent access. */
public class RecyclerBenchmark {

    private static final long NUM_RECYCLES = 5000000L;
    private static final Random RANDOM = new Random(0);

    private static long bench(final Recycler<?> recycler, long numRecycles, int numThreads) throws InterruptedException {
        final AtomicLong recycles = new AtomicLong(numRecycles);
        final CountDownLatch latch = new CountDownLatch(1);
        final Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; ++i){
            // Thread ids happen to be generated sequentially, so we also generate random threads so that distribution of IDs
            // is not perfect for the concurrent recycler
            for (int j = RANDOM.nextInt(5); j >= 0; --j) {
                new Thread();
            }

            threads[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        return;
                    }
                    while (recycles.getAndDecrement() > 0) {
                        final Recycler.V<?> v = recycler.obtain();
                        v.release();
                    }
                }
            };
        }
        for (Thread thread : threads) {
            thread.start();
        }
        final long start = System.nanoTime();
        latch.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws InterruptedException {
        final int limit = 100;
        final Recycler.C<Object> c = new AbstractRecyclerC<Object>() {

            @Override
            public Object newInstance(int sizing) {
                return new Object();
            }

            @Override
            public void recycle(Object value) {
                // do nothing
            }
        };

        final ImmutableMap<String, Recycler<Object>> recyclers = ImmutableMap.<String, Recycler<Object>>builder()
                .put("none", none(c))
                .put("concurrent-queue", concurrentDeque(c, limit))
                .put("locked", locked(deque(c, limit)))
                .put("concurrent", concurrent(dequeFactory(c, limit), Runtime.getRuntime().availableProcessors()))
                .put("soft-concurrent", concurrent(softFactory(dequeFactory(c, limit)), Runtime.getRuntime().availableProcessors())).build();

        // warmup
        final long start = System.nanoTime();
        while (System.nanoTime() - start < TimeUnit.SECONDS.toNanos(10)) {
            for (Recycler<?> recycler : recyclers.values()) {
                bench(recycler, NUM_RECYCLES, 2);
            }
        }

        // run
        for (int numThreads = 1; numThreads <= 4 * Runtime.getRuntime().availableProcessors(); numThreads *= 2) {
            System.out.println("## " + numThreads + " threads\n");
            System.gc();
            Thread.sleep(1000);
            for (Recycler<?> recycler : recyclers.values()) {
                bench(recycler, NUM_RECYCLES, numThreads);
            }
            for (int i = 0; i < 5; ++i) {
                for (Map.Entry<String, Recycler<Object>> entry : recyclers.entrySet()) {
                    System.out.println(entry.getKey() + "\t" + TimeUnit.NANOSECONDS.toMillis(bench(entry.getValue(), NUM_RECYCLES, numThreads)));
                }
                System.out.println();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2529.java