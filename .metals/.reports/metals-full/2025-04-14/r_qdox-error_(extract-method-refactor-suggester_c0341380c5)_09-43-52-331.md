error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2313.java
text:
```scala
t@@his.keepAlive = componentSettings.getAsTime("keep_alive", timeValueMinutes(60));

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

package org.elasticsearch.threadpool.blocking;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.SizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.DynamicExecutors;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.common.util.concurrent.TransferThreadPoolExecutor;
import org.elasticsearch.threadpool.support.AbstractThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static org.elasticsearch.common.settings.ImmutableSettings.Builder.*;
import static org.elasticsearch.common.unit.TimeValue.*;

/**
 * A thread pool that will will block the execute if all threads are busy.
 *
 * @author kimchy (shay.banon)
 */
public class BlockingThreadPool extends AbstractThreadPool {

    final int min;
    final int max;
    final int capacity;
    final TimeValue waitTime;
    final TimeValue keepAlive;

    final int scheduledSize;

    public BlockingThreadPool() {
        this(EMPTY_SETTINGS);
    }

    @Inject public BlockingThreadPool(Settings settings) {
        super(settings);
        this.scheduledSize = componentSettings.getAsInt("scheduled_size", 20);
        this.min = componentSettings.getAsInt("min", 10);
        this.max = componentSettings.getAsInt("max", 100);

        // capacity is set to 0 as it might cause starvation in blocking mode
        this.capacity = (int) componentSettings.getAsSize("capacity", new SizeValue(0)).singles();
        this.waitTime = componentSettings.getAsTime("wait_time", timeValueSeconds(60));
        this.keepAlive = componentSettings.getAsTime("keep_alive", timeValueSeconds(60));
        logger.debug("initializing {} thread pool with min[{}], max[{}], keep_alive[{}], capacity[{}], wait_time[{}], scheduled_size[{}]", getType(), min, max, keepAlive, capacity, waitTime, scheduledSize);
//        executorService = TransferThreadPoolExecutor.newBlockingExecutor(min, max, keepAlive.millis(), TimeUnit.MILLISECONDS, waitTime.millis(), TimeUnit.MILLISECONDS, capacity, EsExecutors.daemonThreadFactory(settings, "[tp]"));
        executorService = DynamicExecutors.newBlockingThreadPool(min, max, keepAlive.millis(), capacity, waitTime.millis(), EsExecutors.daemonThreadFactory(settings, "[tp]"));
        scheduledExecutorService = Executors.newScheduledThreadPool(scheduledSize, EsExecutors.daemonThreadFactory(settings, "[sc]"));
        cached = EsExecutors.newCachedThreadPool(keepAlive, EsExecutors.daemonThreadFactory(settings, "[cached]"));
        started = true;
    }

    @Override public String getType() {
        return "blocking";
    }

    @Override public int getMinThreads() {
        return min;
    }

    @Override public int getMaxThreads() {
        return max;
    }

    @Override public int getSchedulerThreads() {
        return scheduledSize;
    }

    @Override public int getPoolSize() {
        if (executorService instanceof TransferThreadPoolExecutor) {
            return ((TransferThreadPoolExecutor) executorService).getPoolSize();
        } else {
            return ((ThreadPoolExecutor) executorService).getPoolSize();
        }
    }

    @Override public int getActiveCount() {
        if (executorService instanceof TransferThreadPoolExecutor) {
            return ((TransferThreadPoolExecutor) executorService).getActiveCount();
        } else {
            return ((ThreadPoolExecutor) executorService).getActiveCount();
        }
    }


    @Override public int getSchedulerPoolSize() {
        return ((ThreadPoolExecutor) scheduledExecutorService).getPoolSize();
    }

    @Override public int getSchedulerActiveCount() {
        return ((ThreadPoolExecutor) scheduledExecutorService).getActiveCount();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2313.java