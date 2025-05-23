error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3948.java
text:
```scala
t@@his.wait(); // wait until a new job is posted (or reenabled:38901)

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.processing;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.internal.core.search.Util;

public abstract class JobManager implements Runnable {

	/* queue of jobs to execute */
	protected IJob[] awaitingJobs = new IJob[10];
	protected int jobStart = 0;
	protected int jobEnd = -1;
	protected boolean executing = false;

	/* background processing */
	protected Thread processingThread;

	/* flag indicating whether job execution is enabled or not */
	private boolean enabled = true;

	public static boolean VERBOSE = false;
	/* flag indicating that the activation has completed */
	public boolean activated = false;
	
	private int awaitingClients = 0;

	public static void verbose(String log) {
		System.out.println("(" + Thread.currentThread() + ") " + log); //$NON-NLS-1$//$NON-NLS-2$
	}

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
	public void disable() {
		enabled = false;
		if (VERBOSE)
			JobManager.verbose("DISABLING background indexing"); //$NON-NLS-1$
	}
	/**
	 * Remove the index from cache for a given project.
	 * Passing null as a job family discards them all.
	 */
	public void discardJobs(String jobFamily) {

		if (VERBOSE)
			JobManager.verbose("DISCARD   background job family - " + jobFamily); //$NON-NLS-1$

		boolean wasEnabled = isEnabled();
		try {
			IJob currentJob;
			// cancel current job if it belongs to the given family
			synchronized(this){
				currentJob = this.currentJob();
				disable();
			}
			if (currentJob != null 
					&& (jobFamily == null || currentJob.belongsTo(jobFamily))) {
	
				currentJob.cancel();
			
				// wait until current active job has finished
				while (processingThread != null && executing){
					try {
						if (VERBOSE)
							JobManager.verbose("-> waiting end of current background job - " + currentJob); //$NON-NLS-1$ //$NON-NLS-2$
						Thread.sleep(50);
					} catch(InterruptedException e){
					}
				}
			}
	
			// flush and compact awaiting jobs
			int loc = -1;
			synchronized(this) {
				for (int i = jobStart; i <= jobEnd; i++) {
					currentJob = awaitingJobs[i];
					awaitingJobs[i] = null;
					if (!(jobFamily == null
 currentJob.belongsTo(jobFamily))) { // copy down, compacting
						awaitingJobs[++loc] = currentJob;
					} else {
						if (VERBOSE)
							JobManager.verbose("-> discarding background job  - " + currentJob); //$NON-NLS-1$
						currentJob.cancel();
					}
				}
				jobStart = 0;
				jobEnd = loc;
			}
		} finally {
			if (wasEnabled)
				enable();
		}
		if (VERBOSE)
			JobManager.verbose("DISCARD   DONE with background job family - " + jobFamily); //$NON-NLS-1$
	}
	public void enable() {
		enabled = true;
		if (VERBOSE)
			JobManager.verbose("ENABLING  background indexing"); //$NON-NLS-1$
		this.notifyAll(); // wake up the background thread if it is waiting			
	}
	public boolean isEnabled() {
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
	 *            and should synchronize (it is deliberately left to clients to decide whether
	 *            concurrent jobs might interfere or not. In general, multiple read jobs are ok).
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
			JobManager.verbose("STARTING  concurrent job - " + searchJob); //$NON-NLS-1$
		if (!searchJob.isReadyToRun()) {
			if (VERBOSE)
				JobManager.verbose("ABORTED   concurrent job - " + searchJob); //$NON-NLS-1$
			return IJob.FAILED;
		}

		int concurrentJobWork = 100;
		if (progress != null)
			progress.beginTask("", concurrentJobWork); //$NON-NLS-1$
		boolean status = IJob.FAILED;
		if (awaitingJobsCount() > 0) {
			switch (waitingPolicy) {

				case IJob.ForceImmediate :
					if (VERBOSE)
						JobManager.verbose("-> NOT READY - forcing immediate - " + searchJob);//$NON-NLS-1$
					boolean wasEnabled = isEnabled();
					try {
						disable(); // pause indexing
						status = searchJob.execute(progress == null ? null : new SubProgressMonitor(progress, concurrentJobWork));
					} finally {
						if (wasEnabled)
							enable();
					}
					if (VERBOSE)
						JobManager.verbose("FINISHED  concurrent job - " + searchJob); //$NON-NLS-1$
					return status;
					
				case IJob.CancelIfNotReady :
					if (VERBOSE)
						JobManager.verbose("-> NOT READY - cancelling - " + searchJob); //$NON-NLS-1$
					if (progress != null) progress.setCanceled(true);
					if (VERBOSE)
						JobManager.verbose("CANCELED concurrent job - " + searchJob); //$NON-NLS-1$
					throw new OperationCanceledException();

				case IJob.WaitUntilReady :
					int awaitingWork;
					IJob previousJob = null;
					IJob currentJob;
					IProgressMonitor subProgress = null;
					int totalWork = this.awaitingJobsCount();
					if (progress != null && totalWork > 0) {
						subProgress = new SubProgressMonitor(progress, concurrentJobWork / 2);
						subProgress.beginTask("", totalWork); //$NON-NLS-1$
						concurrentJobWork = concurrentJobWork / 2;
					}
					int originalPriority = this.processingThread.getPriority();
					try {
						synchronized(this) {
							
							// use local variable to avoid potential NPE (see Bug 20435 NPE when searching java method)
							Thread t = this.processingThread;
							if (t != null) {
								t.setPriority(Thread.currentThread().getPriority());
							}
							this.awaitingClients++;
						}
						while ((awaitingWork = awaitingJobsCount()) > 0) {
							if (subProgress != null && subProgress.isCanceled())
								throw new OperationCanceledException();
							currentJob = currentJob();
							// currentJob can be null when jobs have been added to the queue but job manager is not enabled
							if (currentJob != null && currentJob != previousJob) {
								if (VERBOSE)
									JobManager.verbose("-> NOT READY - waiting until ready - " + searchJob);//$NON-NLS-1$
								if (subProgress != null) {
									subProgress.subTask(
										Util.bind("manager.filesToIndex", Integer.toString(awaitingWork))); //$NON-NLS-1$
									subProgress.worked(1);
								}
								previousJob = currentJob;
							}
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
							}
						}
					} finally {
						synchronized(this) {
							this.awaitingClients--;
							
							// use local variable to avoid potential NPE (see Bug 20435 NPE when searching java method)
							Thread t = this.processingThread;
							if (t != null) {
								t.setPriority(originalPriority);
							}
						}
					}
					if (subProgress != null) {
						subProgress.done();
					}
			}
		}
		status = searchJob.execute(progress == null ? null : new SubProgressMonitor(progress, concurrentJobWork));
		if (progress != null) {
			progress.done();
		}
		if (VERBOSE)
			JobManager.verbose("FINISHED  concurrent job - " + searchJob); //$NON-NLS-1$
		return status;
	}
	public abstract String processName();
	
	public synchronized void request(IJob job) {
		if (!job.isReadyToRun()) {
			if (VERBOSE)
				JobManager.verbose("ABORTED request of background job - " + job); //$NON-NLS-1$
			return;
		}

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
			JobManager.verbose("REQUEST   background job - " + job); //$NON-NLS-1$
		this.notifyAll(); // wake up the background thread if it is waiting
	}
	/**
	 * Flush current state
	 */
	public void reset() {
		if (VERBOSE)
			JobManager.verbose("Reset"); //$NON-NLS-1$

		if (processingThread != null) {
			discardJobs(null); // discard all jobs
		} else {
			/* initiate background processing */
			processingThread = new Thread(this, this.processName());
			processingThread.setDaemon(true);
			// less prioritary by default, priority is raised if clients are actively waiting on it
			processingThread.setPriority(Thread.NORM_PRIORITY-1); 
			processingThread.start();
		}
	}
	/**
	 * Infinite loop performing resource indexing
	 */
	public void run() {

		long idlingStart = -1;
		activateProcessing();
		try {
			while (this.processingThread != null) {
				try {
					IJob job;
					synchronized (this) {
						if ((job = currentJob()) == null) {
							if (idlingStart < 0)
								idlingStart = System.currentTimeMillis();
							notifyIdle(System.currentTimeMillis() - idlingStart);
							this.wait(); // wait until a new job is posted
							Thread.sleep(500); // delay before processing the new job, allow some time for the active thread to finish
							continue;
						} else {
							idlingStart = -1;
						}
					}
					if (VERBOSE) {
						JobManager.verbose(awaitingJobsCount() + " awaiting jobs"); //$NON-NLS-1$
						JobManager.verbose("STARTING background job - " + job); //$NON-NLS-1$
					}
					try {
						executing = true;
						/*boolean status = */job.execute(null);
						//if (status == FAILED) request(job);
					} finally {
						executing = false;
						if (VERBOSE) {
							JobManager.verbose("FINISHED background job - " + job); //$NON-NLS-1$
						}
						moveToNextJob();
						if (this.awaitingClients == 0) {
							Thread.sleep(50);
						}
					}
				} catch (InterruptedException e) { // background indexing was interrupted
				}
			}
		} catch (RuntimeException e) {
			if (this.processingThread != null) { // if not shutting down
				// log exception
				org.eclipse.jdt.internal.core.Util.log(e, "Background Indexer Crash Recovery"); //$NON-NLS-1$
				
				// keep job manager alive
				this.discardJobs(null);
				this.processingThread = null;
				this.reset(); // this will fork a new thread with no waiting jobs, some indexes will be inconsistent
			}
			throw e;
		} catch (Error e) {
			if (this.processingThread != null && !(e instanceof ThreadDeath)) {
				// log exception
				org.eclipse.jdt.internal.core.Util.log(e, "Background Indexer Crash Recovery"); //$NON-NLS-1$
				
				// keep job manager alive
				this.discardJobs(null);
				this.processingThread = null;
				this.reset(); // this will fork a new thread with no waiting jobs, some indexes will be inconsistent
			}
			throw e;
		}
	}
	/**
	 * Stop background processing, and wait until the current job is completed before returning
	 */
	public void shutdown() {

		disable();
		discardJobs(null); // will wait until current executing job has completed
		Thread thread = this.processingThread;
		this.processingThread = null; // mark the job manager as shutting down so that the thread will stop by itself
		try {
			if (thread != null) { // see http://bugs.eclipse.org/bugs/show_bug.cgi?id=31858
				synchronized (this) {
					this.notifyAll(); // ensure its awake so it can be shutdown
				}
				thread.join();
			}
		} catch (InterruptedException e) {
		}
	}
public String toString() {
	StringBuffer buffer = new StringBuffer(10);
	buffer.append("Enabled:").append(this.enabled).append('\n'); //$NON-NLS-1$
	int numJobs = jobEnd - jobStart + 1;
	buffer.append("Jobs in queue:").append(numJobs).append('\n'); //$NON-NLS-1$
	for (int i = 0; i < numJobs && i < 15; i++) {
		buffer.append(i).append(" - job["+i+"]: ").append(awaitingJobs[jobStart+i]).append('\n'); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3948.java