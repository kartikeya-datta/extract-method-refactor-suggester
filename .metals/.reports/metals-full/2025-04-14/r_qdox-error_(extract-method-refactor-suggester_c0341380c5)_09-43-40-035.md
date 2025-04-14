error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5824.java
text:
```scala
i@@f (event.getResult().getSeverity() == IStatus.ERROR) {

/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.progress;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;

/**
 * JobProgressManager provides the progress monitor to the 
 * job manager and informs any ProgressContentProviders of changes.
 */
public class JobProgressManager
	extends JobChangeAdapter
	implements IProgressProvider {

	private ArrayList providers = new ArrayList();
	private static JobProgressManager singleton;
	private Map jobs = Collections.synchronizedMap(new HashMap());
	boolean debug = false;

	/**
	 * Get the progress manager currently in use.
	 * @return JobProgressManager
	 */
	public static JobProgressManager getInstance() {
		if (singleton == null)
			singleton = new JobProgressManager();
		return singleton;
	}

	/**
	 * The JobMonitor is the inner class that handles the IProgressMonitor 
	 * integration with the ProgressMonitor.
	 */
	private class JobMonitor implements IProgressMonitor {
		Job job;
		boolean cancelled = false;

		/**
		 * Create a monitor on the supplied job.
		 * @param newJob
		 */
		JobMonitor(Job newJob) {
			job = newJob;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
		 */
		public void beginTask(String taskName, int totalWork) {
			if (isNonDisplayableJob(job))
				return;
			JobInfo info = getJobInfo(job);
			info.beginTask(taskName, totalWork);
			refresh(info);
		}
		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#done()
		 */
		public void done() {
			JobInfo info = getJobInfo(job);
			info.clearTaskInfo();
			info.clearChildren();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
		 */
		public void internalWorked(double work) {
			worked((int) work);

		}
		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
		 */
		public boolean isCanceled() {
			return cancelled;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
		 */
		public void setCanceled(boolean value) {
			cancelled = value;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
		 */
		public void setTaskName(String taskName) {
			if (isNonDisplayableJob(job))
				return;

			JobInfo info = getJobInfo(job);
			if (info.hasTaskInfo())
				info.setTaskName(taskName);
			else {
				beginTask(taskName, 100);
				return;
			}

			info.clearChildren();
			refresh(info);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
		 */
		public void subTask(String name) {
			if (isNonDisplayableJob(job))
				return;
			if (name.length() == 0)
				return;
			JobInfo info = getJobInfo(job);

			info.clearChildren();
			info.addSubTask(name);
			refresh(info);

		}

		/* (non-Javadoc)
		 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
		 */
		public void worked(int work) {
			if (isNonDisplayableJob(job))
				return;

			JobInfo info = getJobInfo(job);
			if (info.hasTaskInfo()) {
				info.addWork(work);
				refresh(info);
			}
		}
	}

	/**
	 * Create a new instance of the receiver.
	 */
	JobProgressManager() {
		Platform.getJobManager().setProgressProvider(this);
		Platform.getJobManager().addJobChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.IProgressProvider#createMonitor(org.eclipse.core.runtime.jobs.Job)
	 */
	public IProgressMonitor createMonitor(Job job) {
		return new JobMonitor(job);
	}

	/**
	 * Add a progress content provider to listen to the changes.
	 * @param provider
	 */
	void addProvider(ProgressContentProvider provider) {
		providers.add(provider);
	}

	/**
	 * Remove the supplied provider from the list of providers.
	 * @param provider
	 */
	void removeProvider(ProgressContentProvider provider) {
		providers.remove(provider);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#scheduled(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void scheduled(IJobChangeEvent event) {
		if (isNeverDisplayedJob(event.getJob()))
			return;
		JobInfo info = new JobInfo(event.getJob());
		jobs.put(event.getJob(), info);
		if (!isNonDisplayableJob(event.getJob()))
			add(info);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#aboutToRun(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void aboutToRun(IJobChangeEvent event) {
		if (!isNonDisplayableJob(event.getJob())) {
			JobInfo info = getJobInfo(event.getJob());
			refresh(info);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {

		JobInfo info = getJobInfo(event.getJob());
		if (event.getResult().getCode() == IStatus.ERROR) {
			info.setError(event.getResult());
		} else
			jobs.remove(event.getJob());
		//Only refresh if we are showing it
		if (!isNonDisplayableJob(event.getJob()))
			remove(info);
	}

	/**
	 * Get the JobInfo for the job. If it does not exist
	 * create it.
	 * @param job
	 * @return
	 */
	JobInfo getJobInfo(Job job) {
		JobInfo info = (JobInfo) jobs.get(job);
		if (info == null) {
			info = new JobInfo(job);
			jobs.put(job, info);
		}
		return info;
	}

	/**
	 * Refresh the content providers as a result of a change in info.
	 * @param info
	 */
	public void refresh(JobInfo info) {
		Iterator iterator = providers.iterator();
		while (iterator.hasNext()) {
			ProgressContentProvider provider =
				(ProgressContentProvider) iterator.next();
			provider.refresh(info);
		}

	}

	/**
	 * Refresh all the content providers as a result of a change in the whole model.
	 * @param info
	 */
	public void refreshAll() {
		Iterator iterator = providers.iterator();
		while (iterator.hasNext()) {
			ProgressContentProvider provider =
				(ProgressContentProvider) iterator.next();
			provider.refreshAll();
		}

	}

	/**
	 * Refresh the content providers as a result of a deletion of info.
	 * @param info
	 */
	public void remove(JobInfo info) {
		Iterator iterator = providers.iterator();
		while (iterator.hasNext()) {
			ProgressContentProvider provider =
				(ProgressContentProvider) iterator.next();
			provider.remove(info);
		}

	}

	/**
	 * Refresh the content providers as a result of an addition of info.
	 * @param info
	 */
	public void add(JobInfo info) {
		Iterator iterator = providers.iterator();
		while (iterator.hasNext()) {
			ProgressContentProvider provider =
				(ProgressContentProvider) iterator.next();
			provider.add(info);
		}

	}

	/**
	 * Return whether or not this job is currently displayable.
	 * @param job
	 * @return
	 */
	boolean isNonDisplayableJob(Job job) {
		if (isNeverDisplayedJob(job))
			return true;
		if (debug) //Always display in debug mode
			return false;
		else
			return job.isSystem() || job.getState() == Job.SLEEPING;
	}

	/**
	 * Return whether or not this job is ever displayable.
	 * @param job
	 * @return
	 */
	private boolean isNeverDisplayedJob(Job job) {
		if (job == null)
			return true;
		//Never display the update job
		if (job
			.getName()
			.equals(
				ProgressMessages.getString(
					"ProgressContentProvider.UpdateProgressJob"))) //$NON-NLS-1$
			return true;
		return false;
	}

	/**
	 * Get the jobs currently being displayed.
	 * @return Object[]
	 */
	public Object[] getJobs() {

		Iterator iterator = jobs.keySet().iterator();
		Collection result = new ArrayList();
		while (iterator.hasNext()) {
			Job next = (Job) iterator.next();
			if (isNonDisplayableJob(next))
				continue;
			result.add(jobs.get(next));
		}
		return result.toArray();
	}

	/**
	 * Clear the job out of the list of those being displayed.
	 * Only do this for jobs that are an error.
	 * @param job
	 */
	void clearJob(Job job) {
		JobInfo info = (JobInfo) jobs.get(job);
		if (info != null && info.getErrorStatus() != null) {
			jobs.remove(job);
			remove(info);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5824.java