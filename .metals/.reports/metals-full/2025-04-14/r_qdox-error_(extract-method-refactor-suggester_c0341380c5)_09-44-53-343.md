error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3651.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3651.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3651.java
text:
```scala
C@@ommitLog.instance.sync();

package org.apache.cassandra.db.commitlog;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.*;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.utils.WrappedRunnable;

class BatchCommitLogExecutorService extends AbstractCommitLogExecutorService implements ICommitLogExecutorService, BatchCommitLogExecutorServiceMBean
{
    private final BlockingQueue<CheaterFutureTask> queue;

    public BatchCommitLogExecutorService()
    {
        this(DatabaseDescriptor.getConcurrentWriters());
    }

    public BatchCommitLogExecutorService(int queueSize)
    {
        queue = new LinkedBlockingQueue<CheaterFutureTask>(queueSize);
        Runnable runnable = new WrappedRunnable()
        {
            public void runMayThrow() throws Exception
            {
                while (true)
                {
                    processWithSyncBatch();
                    completedTaskCount++;
                }
            }
        };
        new Thread(runnable, "COMMIT-LOG-WRITER").start();

        registerMBean(this);
    }

    public long getPendingTasks()
    {
        return queue.size();
    }

    private final ArrayList<CheaterFutureTask> incompleteTasks = new ArrayList<CheaterFutureTask>();
    private final ArrayList taskValues = new ArrayList(); // TODO not sure how to generify this
    private void processWithSyncBatch() throws Exception
    {
        CheaterFutureTask firstTask = queue.take();
        if (!(firstTask.getRawCallable() instanceof CommitLog.LogRecordAdder))
        {
            firstTask.run();
            return;
        }

        // attempt to do a bunch of LogRecordAdder ops before syncing
        // (this is a little clunky since there is no blocking peek method,
        //  so we have to break it into firstTask / extra tasks)
        incompleteTasks.clear();
        taskValues.clear();
        long end = System.nanoTime() + (long)(1000000 * DatabaseDescriptor.getCommitLogSyncBatchWindow());

        // it doesn't seem worth bothering future-izing the exception
        // since if a commitlog op throws, we're probably screwed anyway
        incompleteTasks.add(firstTask);
        taskValues.add(firstTask.getRawCallable().call());
        while (!queue.isEmpty()
               && queue.peek().getRawCallable() instanceof CommitLog.LogRecordAdder
               && System.nanoTime() < end)
        {
            CheaterFutureTask task = queue.remove();
            incompleteTasks.add(task);
            taskValues.add(task.getRawCallable().call());
        }

        // now sync and set the tasks' values (which allows thread calling get() to proceed)
        try
        {
            CommitLog.instance().sync();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < incompleteTasks.size(); i++)
        {
            incompleteTasks.get(i).set(taskValues.get(i));
        }
    }


    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
    {
        return newTaskFor(Executors.callable(runnable, value));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
    {
        return new CheaterFutureTask(callable);
    }

    public void execute(Runnable command)
    {
        try
        {
            queue.put((CheaterFutureTask)command);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void add(CommitLog.LogRecordAdder adder)
    {
        try
        {
            submit((Callable)adder).get();
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static class CheaterFutureTask<V> extends FutureTask<V>
    {
        private final Callable rawCallable;

        public CheaterFutureTask(Callable<V> callable)
        {
            super(callable);
            rawCallable = callable;
        }

        public Callable getRawCallable()
        {
            return rawCallable;
        }

        @Override
        public void set(V v)
        {
            super.set(v);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3651.java