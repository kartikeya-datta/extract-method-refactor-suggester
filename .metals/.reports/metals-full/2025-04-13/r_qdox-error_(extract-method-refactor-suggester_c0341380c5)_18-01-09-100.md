error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3560.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3560.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3560.java
text:
```scala
a@@nimationProcessor = new ProgressAnimationProcessor(this);

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
import java.util.HashSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.progress.WorkbenchJob;
/**
 * The AnimationManager is the class that keeps track of the animation items to
 * update.
 */
public class AnimationManager {
	private static AnimationManager singleton;

	boolean animated = false;
	private IJobProgressManagerListener listener;
	IAnimationProcessor animationProcessor;

	public static AnimationManager getInstance() {
		if (singleton == null)
			singleton = new AnimationManager();
		return singleton;
	}

	/**
	 * Get the background color to be used.
	 * 
	 * @param control
	 *            The source of the display.
	 * @return Color
	 */
	static Color getItemBackgroundColor(Control control) {
		return control.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}

	AnimationManager() {

		animationProcessor = new ImageAnimationProcessor(this);

		listener = getProgressListener();
		ProgressManager.getInstance().addListener(listener);

	}
	/**
	 * Add an items to the list
	 * 
	 * @param item
	 */
	void addItem(final AnimationItem item) {
		animationProcessor.addItem(item);
	}

	/**
	 * Return whether or not the current state is animated.
	 * 
	 * @return boolean
	 */
	boolean isAnimated() {
		return animated;
	}
	/**
	 * Set whether or not the receiver is animated.
	 * 
	 * @param boolean
	 */
	void setAnimated(final boolean bool) {
		animated = bool;
		if (bool) {
			animationStarted();
		}
		else{
			animationFinished();
		}
	}
	/**
	 * Dispose the images in the receiver.
	 */
	void dispose() {
		setAnimated(false);
		ProgressManager.getInstance().removeListener(listener);
	}



	private IJobProgressManagerListener getProgressListener() {
		return new IJobProgressManagerListener() {
			HashSet jobs = new HashSet();
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#addJob(org.eclipse.ui.internal.progress.JobInfo)
			 */
			public void addJob(JobInfo info) {
				incrementJobCount(info);
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#refreshJobInfo(org.eclipse.ui.internal.progress.JobInfo)
			 */
			public void refreshJobInfo(JobInfo info) {
				int state = info.getJob().getState();
				if (state == Job.RUNNING)
					addJob(info);
				else
					removeJob(info);
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#refreshAll()
			 */
			public void refreshAll() {
				ProgressManager manager = ProgressManager.getInstance();
				jobs.clear();
				setAnimated(false);
				JobInfo[] currentInfos = manager.getJobInfos(showsDebug());
				for (int i = 0; i < currentInfos.length; i++) {
					addJob(currentInfos[i]);
				}
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#remove(org.eclipse.ui.internal.progress.JobInfo)
			 */
			public void removeJob(JobInfo info) {
				if (jobs.contains(info.getJob())) {
					decrementJobCount(info.getJob());
				}
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#showsDebug()
			 */
			public boolean showsDebug() {
				return false;
			}
			private void incrementJobCount(JobInfo info) {
				//Don't count the animate job itself
				if (isNotTracked(info))
					return;
				if (jobs.isEmpty())
					setAnimated(true);
				jobs.add(info.getJob());
			}
			private void decrementJobCount(Job job) {
				jobs.remove(job);
				if (jobs.isEmpty())
					setAnimated(false);
			}
			/**
			 * If this is one of our jobs or not running then don't bother.
			 */
			private boolean isNotTracked(JobInfo info) {
				//We always track errors
				Job job = info.getJob();
				return job.getState() != Job.RUNNING || animationProcessor.isProcessorJob(job);
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#addGroup(org.eclipse.ui.internal.progress.GroupInfo)
			 */
			public void addGroup(GroupInfo info) {
				//Don't care about groups
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#removeGroup(org.eclipse.ui.internal.progress.GroupInfo)
			 */
			public void removeGroup(GroupInfo group) {
				//Don't care about groups
			}
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#refreshGroup(org.eclipse.ui.internal.progress.GroupInfo)
			 */
			public void refreshGroup(GroupInfo info) {
				//Don't care about groups
			}
		};
	}


	/**
	 * The animation has started. Get the items to do any s other start
	 * behaviour.
	 */
	private void animationStarted() {
		UIJob animationStartJob = new WorkbenchJob(ProgressMessages
				.getString("AnimationManager.AnimationStart")) {//$NON-NLS-1$
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public IStatus runInUIThread(IProgressMonitor monitor) {
				animationProcessor.animationStarted();
				return Status.OK_STATUS;
			}
		};
		animationStartJob.setSystem(true);
		animationStartJob.schedule();
	}
	/**
	 * The animation has finished. Get the items to do any finish
	 * behaviour.
	 */
	private void animationFinished() {
		UIJob animationDoneJob = new WorkbenchJob(ProgressMessages.getString("AnimationManager.DoneJobName")) { //$NON-NLS-1$
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public IStatus runInUIThread(IProgressMonitor monitor) {
				animationProcessor.animationFinished();
				return Status.OK_STATUS;
			}
		};
		animationDoneJob.setSystem(true);
		animationDoneJob.schedule();
	}
	/**
	 * Get the preferred width for widgets displaying the animation.
	 * 
	 * @return int. Return 0 if there is no image data.
	 */
	int getPreferredWidth() {
		return animationProcessor.getPreferredWidth();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3560.java