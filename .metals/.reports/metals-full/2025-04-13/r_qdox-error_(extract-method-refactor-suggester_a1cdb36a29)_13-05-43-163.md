error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4358.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4358.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4358.java
text:
```scala
r@@eturn Math.min((int) (preWork * 100 / totalWork),100);

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

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The TaskInfo is the info on a task with a job. It is 
 * assumed that there is only one task running at a time -
 * any previous tasks in a Job will be deleted.
 */
public class TaskInfo extends SubTaskInfo {
	double preWork = 0;
	int totalWork = 0;

	/**
	 * Create a new instance of the receiver with the supplied total
	 * work and task name.
	 * @param parentJobInfo
	 * @param infoName
	 * @param total
	 */
	TaskInfo(JobInfo parentJobInfo, String infoName, int total) {
		super(parentJobInfo, infoName);
		totalWork = total;
	}

	/**
	 * Add the work increment to the total.
	 * @param workIncrement
	 */
	void addWork(double workIncrement) {
		preWork += workIncrement;

	}
	
	/**
	 * Add the amount of work to the recevier. Update a parent
	 * monitor by the increment scaled to the amount of ticks
	 * this represents. 
	 * @param workIncrement int the amount of work in the receiver
	 * @param parentMonitor The IProgressMonitor that is also listening
	 * @param parentTicks the number of ticks this monitor represents
	 */
	void addWork(double workIncrement, IProgressMonitor parentMonitor, int parentTicks) {
		addWork(workIncrement);
		parentMonitor.internalWorked(workIncrement * parentTicks /totalWork);
	}

	/**
	 * Get the display string for the task.
	 * @return String
	 */
	String getDisplayString() {
		
		if(totalWork == IProgressMonitor.UNKNOWN)
			return unknownProgress();
		
		if (taskName == null) {
			return getDisplayStringWithoutTask();
		} else {
			String[] messageValues = new String[3];
			messageValues[0] = String.valueOf(getPercentDone());
			messageValues[1] = jobInfo.getJob().getName();
			messageValues[2] = taskName;
			
			return ProgressMessages.format("JobInfo.DoneMessage", messageValues); //$NON-NLS-1$
		}

	}

	/**
	 * Get the display String without the task name.
	 * @return String
	 */
	public String getDisplayStringWithoutTask() {
		
		if(totalWork == IProgressMonitor.UNKNOWN)
			return jobInfo.getJob().getName();
		
		String[] messageValues = new String[2];
		messageValues[0] = jobInfo.getJob().getName();
		messageValues[1] = String.valueOf(getPercentDone());
		return ProgressMessages.format("JobInfo.NoTaskNameDoneMessage", messageValues); //$NON-NLS-1$
	}

	/**
	 * Return an integer representing the amount of work completed.
	 * @return
	 */
	int getPercentDone() {
		return (int) (preWork * 100 / totalWork);
	}

	/**
	 * Return the progress for a monitor whose totalWork
	 * is <code>IProgressMonitor.UNKNOWN</code>.
	 * @return String
	 */
	private String unknownProgress(){
		if (taskName == null) {
			return jobInfo.getJob().getName();
		} else {
			String[] messageValues = new String[2];
			messageValues[0] = jobInfo.getJob().getName();
			messageValues[1] = taskName;			
			return ProgressMessages.format("JobInfo.UnknownProgress", messageValues); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4358.java