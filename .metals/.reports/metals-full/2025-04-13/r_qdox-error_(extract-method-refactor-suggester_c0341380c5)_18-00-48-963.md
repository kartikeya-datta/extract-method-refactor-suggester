error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3649.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3649.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3649.java
text:
```scala
L@@ist jobInfos = Collections.synchronizedList(new ArrayList());

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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.progress.UIJob;

/**
 * The StatusLineProgressListener is a class that prints the current
 * progress on the status line.
 */
class StatusLineProgressListener implements IJobProgressManagerListener {

	List jobInfos = new ArrayList();

	private class RefreshJob extends UIJob {

		String message;

		/**
		 * Return a new instance of the receiver.
		 * @param name
		 */
		public RefreshJob() {
			super(ProgressMessages.getString("StatusLineProgressListener.Refresh")); //$NON-NLS-1$
			setPriority(Job.DECORATE);
			setSystem(true);

		}

		public IStatus runInUIThread(IProgressMonitor monitor) {
			IStatusLineManager manager = getStatusLineManager();
			if (manager == null)
				return Status.CANCEL_STATUS;
			manager.setMessage(message);
			return Status.OK_STATUS;
		}

		void setMessage(String newMessage) {
			message = newMessage;
		}

		/**
		 * Return the status line manager if there is one. Return
		 * null if one cannot be found.
		 * @return
		 */
		private IStatusLineManager getStatusLineManager() {
			IWorkbenchWindow window =
				WorkbenchPlugin
					.getDefault()
					.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window != null && window instanceof WorkbenchWindow)
				return ((WorkbenchWindow) window).getStatusLineManager();
			return null;
		}

	}

	RefreshJob refreshJob = new RefreshJob();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#add(org.eclipse.ui.internal.progress.JobInfo)
	 */
	public void add(JobInfo info) {
		jobInfos.add(info);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#refresh(org.eclipse.ui.internal.progress.JobInfo)
	 */
	public void refresh(JobInfo info) {

		if (info.getJob().getState() != Job.RUNNING)
			return;

		refreshJob.setMessage(info.getDisplayString());
		refreshJob.schedule(100);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#refreshAll()
	 */
	public void refreshAll() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#remove(org.eclipse.ui.internal.progress.JobInfo)
	 */
	public void remove(JobInfo info) {

		jobInfos.remove(info);

		refreshJob.setMessage(getNextMessage());
		refreshJob.schedule(100);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.IJobProgressManagerListener#showsDebug()
	 */
	public boolean showsDebug() {
		return false;
	}

	/**
	 * Return the String to update on the status line. If there
	 * is another running job return it's info - otherwise just
	 * return the empty String.
	 * @return
	 */
	private String getNextMessage() {

		Iterator remainingJobs = jobInfos.iterator();
		while (remainingJobs.hasNext()) {
			JobInfo next = (JobInfo) remainingJobs.next();
			if (next.getJob().getState() == Job.RUNNING)
				return next.getDisplayString();
		}
		return new String();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3649.java