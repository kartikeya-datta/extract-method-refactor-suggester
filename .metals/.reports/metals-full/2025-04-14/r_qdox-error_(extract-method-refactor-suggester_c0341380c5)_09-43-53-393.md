error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8479.java
text:
```scala
O@@bject updateLock = new Object();

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
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.progress.UIJob;

/**
 * The ProgressContentProvider is the content provider used for
 * classes that listen to the progress changes.
 */
public class ProgressContentProvider implements ITreeContentProvider {

	/**
	 * The UpdatesInfo is a private class for keeping track of the
	 * updates required.
	 */
	private class UpdatesInfo {

		Collection additions = new HashSet();
		Collection deletions = new HashSet();
		Collection refreshes = new HashSet();
		boolean updateAll = false;

		private UpdatesInfo() {
		}

		/**
		 * Add an add update
		 * @param addition
		 */
		void add(JobInfo addition) {
			additions.add(addition);
		}

		/**
		 * Add a remove update
		 * @param addition
		 */
		void remove(JobInfo removal) {
			deletions.add(removal);
		}
		/**
		 * Add a refresh update
		 * @param addition
		 */
		void refresh(JobInfo refresh) {
			refreshes.add(refresh);
		}
		/**
		 * Reset the caches after completion of an update.
		 */
		void reset() {
			additions.clear();
			deletions.clear();
			refreshes.clear();
		}
	}

	TreeViewer viewer;
	Job updateJob;
	UpdatesInfo currentInfo = new UpdatesInfo();
	Object updateLock;

	public ProgressContentProvider(TreeViewer mainViewer) {
		viewer = mainViewer;
		JobProgressManager.getInstance().addProvider(this);
		createUpdateJob();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		return ((JobTreeElement) parentElement).getChildren();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if (element == this)
			return null;
		else
			return ((JobTreeElement) element).getParent();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element == this)
			return JobProgressManager.getInstance().getJobs().length > 0;
		else
			return ((JobTreeElement) element).hasChildren();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return JobProgressManager.getInstance().getJobs();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		JobProgressManager.getInstance().removeProvider(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(
		Viewer updateViewer,
		Object oldInput,
		Object newInput) {
	}
	/**
	 * Refresh the viewer as a result of a change in info.
	 * @param info
	 */
	void refresh(final JobInfo info) {

		synchronized (updateLock) {
			currentInfo.refresh(info);
		}
		//Add in a 100ms delay so as to keep priority low
		updateJob.schedule(100);
	}

	/**
	 * Refresh the viewer for all jobs.
	 * @param info
	 */
	void refreshAll() {
		synchronized (updateLock) {
			currentInfo.updateAll = true;
		}

		//Add in a 100ms delay so as to keep priority low
		updateJob.schedule(100);
	}

	/**
	 * Refresh the viewer as a result of an addition of info.
	 * @param info
	 */
	void add(final JobInfo info) {
		synchronized (updateLock) {
			currentInfo.add(info);
		}
		updateJob.schedule(100);
	}

	/**
	 * Refresh the viewer as a result of a removal of info.
	 * @param info
	 */
	void remove(final JobInfo info) {
		synchronized (updateLock) {
			currentInfo.remove(info);
		}
		updateJob.schedule(100);
	}

	/**
	 * Create the update job that handles the updatesInfo.
	 */
	private void createUpdateJob() {
			updateJob = new UIJob(ProgressMessages.getString("ProgressContentProvider.UpdateProgressJob")) {//$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
	 */
			public IStatus runInUIThread(IProgressMonitor monitor) {

				if (viewer.getControl().isDisposed())
					return Status.CANCEL_STATUS;
				//Lock update additions while working
				synchronized (updateLock) {
					if (currentInfo.updateAll)
						viewer.refresh(true);
					else {
						Object[] updateItems = currentInfo.refreshes.toArray();
						for (int i = 0; i < updateItems.length; i++) {
							viewer.refresh(updateItems[i], true);
						}
						viewer.add(
							viewer.getInput(),
							currentInfo.additions.toArray());

						viewer.remove(currentInfo.deletions.toArray());
					}
					currentInfo.reset();
				}
				return Status.OK_STATUS;

			}

		};
		updateJob.setSystem(true);
		updateJob.setPriority(Job.DECORATE);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8479.java