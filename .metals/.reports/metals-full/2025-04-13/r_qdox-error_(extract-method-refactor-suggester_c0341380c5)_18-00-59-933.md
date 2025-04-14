error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17785.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17785.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[164,80]

error in qdox parser
file content:
```java
offset: 4719
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17785.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.thread;

import org.apache.commons.logging.Log;

import wicket.util.time.Duration;
import wicket.util.time.Time;


/**
 * Runs a block of code periodically. The Task can be started at a given time and can be a
 * daemon.
 * @author Jonathan Locke
 */
public final class Task
{
    /** The name of this task. */
    private final String name;

    /** The time that the task should start. */
    private Time startTime = Time.now();

    /** True if the task's thread should be a daemon. */
    private boolean isDaemon = true;

    /** True if the tasks's thread has already started executing. */ 
    private boolean isStarted = false;

    /** The log to give to the user's code. */
    private Log log = null;

    /**
     * Constructor.
     * @param name The name of this task
     */
    public Task(final String name)
    {
        this.name = name;
    }

    /**
     * Runs this task at the given frequency.
     * @param frequency The frequency at which to run the code
     * @param code The code to run
     * @throws IllegalStateException Thrown if task is already running
     */
    public synchronized final void run(final Duration frequency, final ICode code)
    {
        if (!isStarted)
        {
            final Runnable runnable = new Runnable()
            {
                public void run()
                {
                    // Sleep until start time
                    startTime.fromNow().sleep();

                    while (true)
                    {
                        // Get the start of the current period
                        final Time startOfPeriod = Time.now();

                        try
                        {
                            // Run the user's code
                            code.run(log);
                        }
                        catch (Exception e)
                        {
                            log.error("Unhandled exception thrown by user code in task " + name, e);
                        }

                        // Sleep until the period is over (or not at all if it's
                        // already passed)
                        startOfPeriod.add(frequency).fromNow().sleep();
                    }
                }
            };

            final Thread thread = new Thread(runnable, name + " Task");

            thread.setDaemon(isDaemon);
            thread.start();

            isStarted = true;
        }
        else
        {
            throw new IllegalStateException("Attempt to start task that is already started");
        }
    }

    /**
     * Sets start time for this task.
     * @param startTime The time this task should start running
     * @throws IllegalStateException Thrown if task is already running
     */
    public synchronized void setStartTime(final Time startTime)
    {
        if (isStarted)
        {
            throw new IllegalStateException(
                    "Attempt to set start time of task that is already started");
        }

        this.startTime = startTime;
    }

    /**
     * Set daemon or not.
     * @param daemon True if this task's thread should be a daemon
     * @throws IllegalStateException Thrown if task is already running
     */
    public synchronized void setDaemon(final boolean daemon)
    {
        if (isStarted)
        {
            throw new IllegalStateException(
                    "Attempt to set daemon boolean of task that is already started");
        }

        isDaemon = daemon;
    }

    /**
     * Set log for user code.
     * @param log The log
     */
    public synchronized void setLog(final Log log)
    {
        this.log = log;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[name="
                + name + ", startTime=" + startTime + ", isDaemon=" + isDaemon + ", isStarted="
                + isStarted + ", codeListener=" + log + "]";
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17785.java