error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2639.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2639.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2639.java
text:
```scala
/*boolean status = */j@@ob.execute(null);

package org.eclipse.jdt.internal.core.search.processing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.internal.core.search.Util;
import java.io.*;
import java.util.*;

public abstract class JobManager implements Runnable {

	/* queue of jobs to execute */
	protected IJob[] awaitingJobs = new IJob[10];
	protected int jobStart = 0;
	protected int jobEnd = -1;
	protected boolean executing = false;

	/* background processing */
	protected Thread thread;

	/* flag indicating whether job execution is enabled or not */
	private boolean enabled = true;

	public static boolean VERBOSE = false;
	/* flag indicating that the activation has completed */
	public boolean activated = false;

	/**
	 * Invoked exactly once, in background, before starting processing any job
	 */
	public void activateProcessing() {
		this.activated = true;
	}
	/**
	 * Answer the amount of awaiting jobs.
	 */
	public synchronized int awaitingJobsCount() {

		// pretend busy in case concurrent job attempts performing before activated
		if (!activated)
			return 1;

		return jobEnd - jobStart + 1;

	}
	/**
	 * Answers the first job in the queue, or null if there is no job available
	 * Until the job has completed, the job manager will keep answering the same job.
	 */
	public synchronized IJob currentJob() {

		if (!enabled)
			return null;

		if (jobStart <= jobEnd) {
			return awaitingJobs[jobStart];
		}
		return null;
	}
	public synchronized void disable() {
		enabled = false;
	}
	/**
	 * Remove the index from cache for a given project.
	 * Passing null as a job family discards them all.
	 */
	public void discardJobs(String jobFamily) {
		boolean wasEnabled = isEnabled();
		try {
			disable();

			// wait until current job has completed
			while (thread != null && executing) {
				try {
					Thread.currentThread().sleep(50);
				} catch (InterruptedException e) {
				}
			}

			// flush and compact awaiting jobs
			int loc = -1;
			for (int i = jobStart; i <= jobEnd; i++) {
				IJob currentJob = awaitingJobs[i];
				awaitingJobs[i] = null;
				if (!(jobFamily == null
 currentJob.belongsTo(jobFamily))) { // copy down, compacting
					awaitingJobs[++loc] = currentJob;
				}
			}
			jobStart = 0;
			jobEnd = loc;
		} finally {
			if (wasEnabled)
				enable();
		}
	}
	public synchronized void enable() {
		enabled = true;
	}
	public synchronized boolean isEnabled() {
		return enabled;
	}
	/**
	 * Advance to the next available job, once the current one has been completed.
	 * Note: clients awaiting until the job count is zero are still waiting at this point.
	 */
	protected synchronized void moveToNextJob() {

		//if (!enabled) return;

		if (jobStart <= jobEnd) {
			awaitingJobs[jobStart++] = null;
			if (jobStart > jobEnd) {
				jobStart = 0;
				jobEnd = -1;
			}
		}
	}
	/**
	 * When idle, give chance to do something
	 */
	protected void notifyIdle(long idlingTime) {
	}
	/**
	 * This API is allowing to run one job in concurrence with background processing.
	 * Indeed since other jobs are performed in background, resource sharing might be 
	 * an issue.Therefore, this functionality allows a given job to be run without
	 * colliding with background ones.
	 * Note: multiple thread might attempt to perform concurrent jobs at the same time,
	 * 			and shoud synchronize (it is deliberately left to clients to decide whether
	 *			concurrent jobs might interfere or not, i.e. multiple read jobs are ok).
	 *
	 * Waiting policy can be:
	 * 		IJobConstants.ForceImmediateSearch
	 * 		IJobConstants.CancelIfNotReadyToSearch
	 * 		IJobConstants.WaitUntilReadyToSearch
	 *
	 */
	public boolean performConcurrentJob(
		IJob searchJob,
		int waitingPolicy,
		IProgressMonitor progress) {

		if (VERBOSE)
			System.out.println("-> performing concurrent job ("+ Thread.currentThread()+"): START - " + searchJob); //$NON-NLS-1$//$NON-NLS-2$
		boolean status = IJob.FAILED;
		if (awaitingJobsCount() > 0) {
			switch (waitingPolicy) {

				case IJob.ForceImmediate :
					if (VERBOSE)
						System.out.println(
							"-> performing concurrent job ("+ Thread.currentThread()+"): NOT READY - ForceImmediate - " + searchJob);//$NON-NLS-1$//$NON-NLS-2$
					boolean wasEnabled = isEnabled();
					try {
						disable(); // pause indexing
						status = searchJob.execute(progress);
						if (VERBOSE)
							System.out.println("-> performing concurrent job ("+ Thread.currentThread()+"): END - " + searchJob); //$NON-NLS-1$//$NON-NLS-2$
					} finally {
						if (wasEnabled)
							enable();
					}
					return status;
				case IJob.CancelIfNotReady :
					if (VERBOSE)
						System.out.println(
							"-> performing concurrent job ("+ Thread.currentThread()+"): NOT READY - CancelIfNotReady - " + searchJob); //$NON-NLS-1$//$NON-NLS-2$
					progress.setCanceled(true);
					break;

				case IJob.WaitUntilReady :
					int awaitingWork;
					IJob previousJob = null;
					IJob currentJob;
					while ((awaitingWork = awaitingJobsCount()) > 0) {
						if (progress != null && progress.isCanceled())
							throw new OperationCanceledException();
						currentJob = currentJob();
						// currentJob can be null when jobs have been added to the queue but job manager is not enabled
						if (currentJob != null && currentJob != previousJob) {
							if (VERBOSE)
								System.out.println(
									"-> performing concurrent job ("+ Thread.currentThread()+"): NOT READY - WaitUntilReady - " + searchJob);//$NON-NLS-1$//$NON-NLS-2$
							if (progress != null) {
								progress.subTask(
									Util.bind("manager.filesToIndex", Integer.toString(awaitingWork))); //$NON-NLS-1$
							}
							previousJob = currentJob;
						}
						try {
							Thread.currentThread().sleep(50);
						} catch (InterruptedException e) {
						}
					}
			}
		}
		status = searchJob.execute(progress);
		if (VERBOSE)
			System.out.println("-> performing concurrent job ("+ Thread.currentThread()+"): END - " + searchJob); //$NON-NLS-1$//$NON-NLS-2$
		return status;
	}
	public abstract String processName();
	
	public synchronized void request(IJob job) {

		// append the job to the list of ones to process later on
		int size = awaitingJobs.length;
		if (++jobEnd == size) { // when growing, relocate jobs starting at position 0
			jobEnd -= jobStart;
			System.arraycopy(
				awaitingJobs,
				jobStart,
				(awaitingJobs = new IJob[size * 2]),
				0,
				jobEnd);
			jobStart = 0;
		}
		awaitingJobs[jobEnd] = job;
		if (VERBOSE)
			System.out.println("-> requesting job ("+ Thread.currentThread()+"): " + job); //$NON-NLS-1$//$NON-NLS-2$

	}
	/**
	 * Flush current state
	 */
	public void reset() {

		if (thread != null) {
			discardJobs(null); // discard all jobs
		} else {
			/* initiate background processing */
			thread = new Thread(this, this.processName());
			thread.setDaemon(true);
			thread.start();
		}
	}
	/**
	 * Infinite loop performing resource indexing
	 */
	public void run() {

		long idlingStart = -1;
		activateProcessing();
		while (true) {
			try {
				IJob job;
				if ((job = currentJob()) == null) {
					if (idlingStart < 0)
						idlingStart = System.currentTimeMillis();
					notifyIdle(System.currentTimeMillis() - idlingStart);
					Thread.currentThread().sleep(500);
					continue;
				} else {
					idlingStart = -1;
				}
				if (VERBOSE) {
					System.out.println("-> executing ("+ Thread.currentThread()+"): " + job); //$NON-NLS-1$//$NON-NLS-2$
					System.out.println("\t" + awaitingJobsCount() + " awaiting jobs.");	//$NON-NLS-1$ //$NON-NLS-2$
				}
				try {
					executing = true;
					boolean status = job.execute(null);
					//if (status == FAILED) request(job);
					moveToNextJob();
				} finally {
					executing = false;
					Thread.currentThread().sleep(50);
				}
			} catch (InterruptedException e) { // background indexing was interrupted
			}
		}
	}
	/**
	 * Stop background processing, and wait until the current job is completed before returning
	 */
	public void shutdown() {

		disable();
		discardJobs(null); // will wait until current executing job has completed
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2639.java