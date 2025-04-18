error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1436.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1436.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1436.java
text:
```scala
private transient L@@og log = null;

/*
 * $Id$ $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.util.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.util.time.Duration;
import wicket.util.time.Time;

/**
 * Runs a block of code periodically. The Task can be started at a given time in
 * the future and can be a daemon. The block of code will be passed a Log object
 * each time it is run through its ICode interface.
 * <p>
 * If the code block takes longer than the period to run, the next task
 * invocation will occur immediately. In this case, tasks will not occur at
 * precise multiples of the period. For example, if you run a task every 30
 * seconds, and the first run takes 40 seconds but the second takes 20 seconds,
 * your task will be invoked at 0 seconds, 40 seconds and 70 seconds (40 seconds +
 * 30 seconds), which is not an even multiple of 30 seconds.
 * <p>
 * In general, this is a simple task class designed for polling activities. If
 * you need precise guarantees, you probably should be using a different task
 * class.
 * 
 * @author Jonathan Locke
 */
public final class Task
{

	/** True if the task's thread should be a daemon. */
	private boolean isDaemon = true;

	/** True if the tasks's thread has already started executing. */
	private boolean isStarted = false;

	/** The log to give to the user's code. */
	private Log log = null;
	/** The name of this task. */
	private final String name;

	/** The time that the task should start. */
	private Time startTime = Time.now();

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of this task
	 */
	public Task(final String name)
	{
		this.name = name;
	}

	/**
	 * Runs this task at the given frequency. You may only call this method if
	 * the task has not yet been started. If the task is already running, an
	 * IllegalStateException will be thrown.
	 * 
	 * @param frequency
	 *            The frequency at which to run the code
	 * @param code
	 *            The code to run
	 * @throws IllegalStateException
	 *             Thrown if task is already running
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
							code.run(getLog());
						}
						catch (Exception e)
						{
							getLog().error(
									"Unhandled exception thrown by user code in task " + name, e);
						}

						// Sleep until the period is over (or not at all if it's
						// already passed)
						startOfPeriod.add(frequency).fromNow().sleep();
					}
				}
			};

			// Start the thread
			final Thread thread = new Thread(runnable, name + " Task");
			thread.setDaemon(isDaemon);
			thread.start();

			// We're started all right!
			isStarted = true;
		}
		else
		{
			throw new IllegalStateException("Attempt to start task that has already been started");
		}
	}

	/**
	 * Set daemon or not. For obvious reasons, this value can only be set before
	 * the task starts running. If you attempt to set this value after the task
	 * starts running, an IllegalStateException will be thrown.
	 * 
	 * @param daemon
	 *            True if this task's thread should be a daemon
	 * @throws IllegalStateException
	 *             Thrown if task is already running
	 */
	public synchronized void setDaemon(final boolean daemon)
	{
		if (isStarted)
		{
			throw new IllegalStateException(
					"Attempt to set daemon state of a task that has already been started");
		}

		isDaemon = daemon;
	}

	/**
	 * Set log for user code to log to when task runs.
	 * 
	 * @param log
	 *            The log
	 */
	public synchronized void setLog(final Log log)
	{
		this.log = log;
	}

	/**
	 * Sets start time for this task. You cannot set the start time for a task
	 * which is already running. If you attempt to, an IllegalStateException
	 * will be thrown.
	 * 
	 * @param startTime
	 *            The time this task should start running
	 * @throws IllegalStateException
	 *             Thrown if task is already running
	 */
	public synchronized void setStartTime(final Time startTime)
	{
		if (isStarted)
		{
			throw new IllegalStateException(
					"Attempt to set start time of task that has already been started");
		}

		this.startTime = startTime;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[name=" + name + ", startTime=" + startTime + ", isDaemon=" + isDaemon
				+ ", isStarted=" + isStarted + ", codeListener=" + log + "]";
	}

	/**
	 * Gets the log.
	 * 
	 * @return the log
	 */
	protected Log getLog()
	{
		if (log == null)
			log = LogFactory.getLog(Task.class);
		return log;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1436.java