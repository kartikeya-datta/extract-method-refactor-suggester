error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9192.java
text:
```scala
t@@hreadPool.cached().execute(new Runnable() {

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

package org.elasticsearch.timer;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.timer.HashedWheelTimer;
import org.elasticsearch.common.timer.Timeout;
import org.elasticsearch.common.timer.Timer;
import org.elasticsearch.common.timer.TimerTask;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.threadpool.ThreadPool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.unit.TimeValue.*;
import static org.elasticsearch.common.util.concurrent.EsExecutors.*;

/**
 * @author kimchy (Shay Banon)
 */
public class TimerService extends AbstractComponent {

    public static enum ExecutionType {
        DEFAULT,
        THREADED
    }

    private final ThreadPool threadPool;

    private final TimeEstimator timeEstimator;

    private final ScheduledFuture timeEstimatorFuture;

    private final Timer timer;

    private final TimeValue tickDuration;

    private final int ticksPerWheel;

    public TimerService(ThreadPool threadPool) {
        this(ImmutableSettings.Builder.EMPTY_SETTINGS, threadPool);
    }

    @Inject public TimerService(Settings settings, ThreadPool threadPool) {
        super(settings);
        this.threadPool = threadPool;

        this.timeEstimator = new TimeEstimator();
        this.timeEstimatorFuture = threadPool.scheduleWithFixedDelay(timeEstimator, 50, 50, TimeUnit.MILLISECONDS);

        this.tickDuration = componentSettings.getAsTime("tick_duration", timeValueMillis(100));
        this.ticksPerWheel = componentSettings.getAsInt("ticks_per_wheel", 1024);

        this.timer = new HashedWheelTimer(logger, daemonThreadFactory(settings, "timer"), tickDuration.millis(), TimeUnit.MILLISECONDS, ticksPerWheel);
    }

    public void close() {
        timeEstimatorFuture.cancel(true);
        timer.stop();
    }

    public long estimatedTimeInMillis() {
        return timeEstimator.time();
    }

    public Timeout newTimeout(TimerTask task, TimeValue delay, ExecutionType executionType) {
        return newTimeout(task, delay.nanos(), TimeUnit.NANOSECONDS, executionType);
    }

    public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit, ExecutionType executionType) {
        if (executionType == ExecutionType.THREADED) {
            task = new ThreadedTimerTask(threadPool, task);
        }
        return timer.newTimeout(task, delay, unit);
    }

    private class ThreadedTimerTask implements TimerTask {

        private final ThreadPool threadPool;

        private final TimerTask task;

        private ThreadedTimerTask(ThreadPool threadPool, TimerTask task) {
            this.threadPool = threadPool;
            this.task = task;
        }

        @Override public void run(final Timeout timeout) throws Exception {
            threadPool.execute(new Runnable() {
                @Override public void run() {
                    try {
                        task.run(timeout);
                    } catch (Exception e) {
                        logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + ".", e);
                    }
                }
            });
        }
    }

    private static class TimeEstimator implements Runnable {

        private long time = System.currentTimeMillis();

        @Override public void run() {
            this.time = System.currentTimeMillis();
        }

        public long time() {
            return this.time;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9192.java