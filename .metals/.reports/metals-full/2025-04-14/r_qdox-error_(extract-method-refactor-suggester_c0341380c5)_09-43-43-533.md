error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3531.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3531.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3531.java
text:
```scala
i@@f (PlatformUI.isWorkbenchRunning() && refreshJob.setMessage(getDisplayString()))

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IStatusLineWithProgressManager;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.progress.UIJob;

/**
 * The WorkbenchProgressListener is a class that listens to progress on the
 * workbench and reports accordingly.
 */
class WorkbenchMonitorProvider {

	List workingMonitors = Collections.synchronizedList(new ArrayList());

	private class BackgroundProgressMonitor implements IProgressMonitor {

		double allWork;
		double worked;
		String taskName;
		String subTask = ""; //$NON-NLS-1$

		/**
		 * Create a new instance of the receiver with the supplied jobName
		 * 
		 * @param jobName
		 */
		BackgroundProgressMonitor(String jobName) {
			taskName = jobName;
		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String,
		 * int)
		 */
		public void beginTask(String name, int totalWork) {

			if (name != null && name.length() > 0)
				taskName = name;

			allWork = totalWork;
			subTask = ""; //$NON-NLS-1$
			worked = 0;
			updateMessage();
		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#done()
		 */
		public void done() {
			
			if(PlatformUI.isWorkbenchRunning())
				refreshJob.clearStatusLine();
				refreshJob.schedule(100);

		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
		 */
		public void internalWorked(double work) {
			worked += work;
			//Do not rely on monitor.done() to clear.
			if (worked >= allWork)
				done();
			else
				updateMessage();
		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
		 */
		public boolean isCanceled() {
			//No cancel functionality in status line currently
			return false;
		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
		 */
		public void setCanceled(boolean value) {
			//No cancel functionality in status line currently

		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
		 */
		public void setTaskName(String name) {
			taskName = name;
			updateMessage();

		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
		 */
		public void subTask(String name) {
			//Don't show this granularity
		}

		/*
		 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
		 */
		public void worked(int work) {
			internalWorked(work);

		}

		/**
		 * Update the message for the receiver.
		 */
		private void updateMessage() {
			if (refreshJob.setMessage(getDisplayString()))
				refreshJob.schedule(100);
		}

		/**
		 * Get the display string for the task.
		 * 
		 * @return String
		 */
		String getDisplayString() {

			if (worked == IProgressMonitor.UNKNOWN) {
				if (subTask.length() == 0)
					return taskName;
				else
					return ProgressMessages.format("MonitorProvider.twoValueUnknownMessage", new String[] { taskName, subTask }); //$NON-NLS-1$

			} else {
				int done = (int) (worked * 100 / allWork);
				String percentDone = String.valueOf(done);

				if (subTask.length() == 0)
					return ProgressMessages.format("MonitorProvider.oneValueMessage", new String[] { taskName, percentDone }); //$NON-NLS-1$

				return ProgressMessages.format("MonitorProvider.twoValueMessage", new String[] { taskName, subTask, String.valueOf(done)}); //$NON-NLS-1$

			}

		}

	}

	private class RefreshJob extends UIJob {

		String message;
		boolean clear = false;

		/**
		 * Return a new instance of the receiver.
		 * 
		 * @param name
		 */
		public RefreshJob() {
			super(ProgressMessages.getString("StatusLineProgressListener.Refresh")); //$NON-NLS-1$
			setPriority(Job.DECORATE);
			setSystem(true);

		}

		public IStatus runInUIThread(IProgressMonitor monitor) {
			IStatusLineWithProgressManager manager = getStatusLineManager();
			if (manager == null)
				return Status.CANCEL_STATUS;
			if (clear) {
				clear = false;
				BackgroundProgressMonitor next = nextMonitor(monitor);
				if(next == null)
					manager.clearProgress();
				else
					manager.setProgressMessage(next.getDisplayString());
			} else
				manager.setProgressMessage(message);
			return Status.OK_STATUS;
		}

		/**
		 * Set the message for the receiver. If it is a new message return a
		 * boolean.
		 * 
		 * @param newMessage
		 * @return boolean. true if an update is required
		 */
		synchronized boolean setMessage(String newMessage) {
			if (newMessage.equals(message))
				return false;
			message = newMessage;
			return true;
		}

		synchronized void clearStatusLine() {
			clear = true;
		}

	}

	RefreshJob refreshJob = new RefreshJob();

	/**
	 * Get the progress monitor for a job. If it is a UIJob get the main
	 * monitor from the status line. Otherwise get a background monitor.
	 * 
	 * @return IProgressMonitor
	 */
	IProgressMonitor getMonitor(Job job) {
		
		//Don't keep track of our own refreshes
		if(job instanceof RefreshJob)
			return new NullProgressMonitor();
		
		if (job instanceof UIJob) {
			return getUIProgressMonitor(job.getName());
		}

		return getBackgroundProgressMonitor(job.getName());
	}

	/**
	 * Return the status line manager if there is one. Return null if one
	 * cannot be found or it is not a IStatusLineWithProgressManager.
	 * 
	 * @return IStatusLineWithProgressManager
	 */
	private IStatusLineWithProgressManager getStatusLineManager() {

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null && window instanceof WorkbenchWindow) {
			IStatusLineManager manager = ((WorkbenchWindow) window).getStatusLineManager();
			if (manager instanceof IStatusLineWithProgressManager)
				return (IStatusLineWithProgressManager) manager;
		}
		return null;
	}

	/**
	 * Get a IProgressMonitor for the background jobs.
	 * 
	 * @param jobName
	 *           The name of the job.
	 * @return IProgressMonitor
	 */
	private IProgressMonitor getBackgroundProgressMonitor(String jobName) {
		return new BackgroundProgressMonitor(jobName);
	}

	/**
	 * Get a progress monitor for use with UIThreads. This monitor will use the
	 * status line directly if possible.
	 * 
	 * @param jobName.
	 *           Used if the task name is null.
	 * @return IProgressMonitor
	 */
	private IProgressMonitor getUIProgressMonitor(final String jobName) {
		return new IProgressMonitor() {

			IProgressMonitor internalMonitor;

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String,
			 * int)
			 */
			public void beginTask(String name, int totalWork) {

				if (name == null || name.length() == 0)
					getInternalMonitor().beginTask(jobName, totalWork);
				else
					getInternalMonitor().beginTask(name, totalWork);
			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#done()
			 */
			public void done() {
				getInternalMonitor().done();

			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
			 */
			public void internalWorked(double work) {
				getInternalMonitor().internalWorked(work);

			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
			 */
			public boolean isCanceled() {
				return getInternalMonitor().isCanceled();
			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
			 */
			public void setCanceled(boolean value) {
				getInternalMonitor().setCanceled(value);

			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
			 */
			public void setTaskName(String name) {
				getInternalMonitor().setTaskName(name);

			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
			 */
			public void subTask(String name) {
				getInternalMonitor().subTask(name);

			}

			/*
			 * (non-Javadoc) @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
			 */
			public void worked(int work) {
				getInternalMonitor().worked(work);

			}

			/**
			 * Get the monitor that is being wrapped. This is called lazily as
			 * we will not be able to get the monitor for the workbench outside
			 * of the UI Thread and so we will have to wait until the monitor
			 * is accessed.
			 * 
			 * Return a NullProgressMonitor if the one from the workbench
			 * cannot be found.
			 * 
			 * @return IProgressMonitor
			 */
			private IProgressMonitor getInternalMonitor() {
				if (internalMonitor == null) {
					IStatusLineWithProgressManager manager = getStatusLineManager();
					if (manager == null)
						internalMonitor = new NullProgressMonitor();
					else
						internalMonitor = manager.getProgressMonitor();
				}
				return internalMonitor;
			}
		};

	}

	/**
	 * Return the next monitor to update with if there is one.
	 * 
	 * @param finishedMonitor
	 * @return BackgroundProgressMonitor or <code>null</code>
	 */
	BackgroundProgressMonitor nextMonitor(IProgressMonitor finishedMonitor) {
		workingMonitors.remove(finishedMonitor);

		if (workingMonitors.size() > 0) 
			return (BackgroundProgressMonitor) workingMonitors.get(0);
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3531.java