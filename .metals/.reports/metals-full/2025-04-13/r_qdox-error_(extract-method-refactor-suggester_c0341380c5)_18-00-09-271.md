error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15501.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15501.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15501.java
text:
```scala
i@@f(!finished) {

/*
 * Copyright  2007 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.util;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

/**
 * A worker ant executes a single task in a background thread.
 * After the run, any exception thrown is turned into a buildexception, which can be
 * rethrown, the finished attribute is set, then notifyAll() is called,
 * so that anyone waiting on the same notify object gets woken up.
 * </p>
 * This class is effectively a superset of
 * {@link org.apache.tools.ant.taskdefs.Parallel.TaskRunnable}
 *
 * @since Ant 1.8
 */

public class WorkerAnt extends Thread {

    private Task task;
    private Object notify;
    private volatile boolean finished=false;
    private volatile BuildException buildException;
    private volatile Throwable exception;

    /**
     * Error message if invoked with no task
     */
    public static final String ERROR_NO_TASK = "No task defined";


    /**
     * Create the worker.
     * <p/>
     * This does not start the thread, merely configures it.
     * @param task the task
     * @param notify what to notify
     */
    public WorkerAnt(Task task, Object notify) {
        this.task = task;
        this.notify = notify != null ? notify : this;
    }

    /**
     * Create the worker, using the worker as the notification point.
     * <p/>
     * This does not start the thread, merely configures it.
     * @param task the task
     */
    public WorkerAnt(Task task) {
        this(task,null);
    }

    /**
     * Get any build exception.
     * This would seem to be oversynchronised, but know that Java pre-1.5 can reorder volatile access.
     * The synchronized attribute is to force an ordering.
     *
     * @return the exception or null
     */
    public synchronized BuildException getBuildException() {
        return buildException;
    }

    /**
     * Get whatever was thrown, which may or may not be a buildException.
     * Assertion: getException() instanceof BuildException <=> getBuildException()==getException()
     * @return
     */
    public synchronized Throwable getException() {
        return exception;
    }


    /**
     * Get the task
     * @return the task
     */
    public Task getTask() {
        return task;
    }


    /**
     * Query the task/thread for being finished.
     * This would seem to be oversynchronised, but know that Java pre-1.5 can reorder volatile access.
     * The synchronized attribute is to force an ordering.
     * @return true if the task is finished.
     */
    public synchronized boolean isFinished() {
        return finished;
    }

    /**
     * Block on the notify object and so wait until the thread is finished.
     * @param timeout timeout in milliseconds
     * @throws InterruptedException if the execution was interrupted
     */
    public void waitUntilFinished(long timeout) throws InterruptedException {
        synchronized(notify) {
            while (!finished) {
                notify.wait(timeout);
            }
        }
    }

    /**
     * Raise an exception if one was caught
     *
     * @throws BuildException if one has been picked up
     */
    public void rethrowAnyBuildException() {
        BuildException ex = getBuildException();
        if (ex != null) {
            throw ex;
        }
    }


    /**
     * Handle a caught exception, by recording it and possibly wrapping it
     * in a BuildException for later rethrowing.
     * @param thrown what was caught earlier
     */
    private synchronized void caught(Throwable thrown) {
        exception = thrown;
        buildException = (thrown instanceof BuildException)?
                (BuildException)thrown
                :new BuildException(thrown);
    }

    /**
     * Run the task, which is skipped if null.
     * When invoked again, the task is re-run.
     */
    public void run() {
        try {
            if (task != null) {
                task.execute();
            }
        } catch (Throwable thrown) {
            caught(thrown);
        } finally {
            synchronized (notify) {
                finished=true;
                //reset the task.
                //wake up our owner, if it is waiting
                notify.notifyAll();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15501.java