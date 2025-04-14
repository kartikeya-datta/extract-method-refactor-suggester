error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3724.java
text:
```scala
r@@unner.runWithProgress(progressRunnable);

/*******************************************************************************
* Copyright (c) 2008 EclipseSource, IBM, and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
*   IBM Corporation - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.core.util;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;

public class JobsExecutor extends AbstractExecutor {

	protected int fJobCounter = 1;
	protected String fExecutorName;
	protected boolean fSystem;
	protected ISchedulingRule fSchedulingRule;
	protected long delay;

	public JobsExecutor(String executorName) {
		this(executorName, false);
	}

	public JobsExecutor(String executorName, boolean system) {
		this(executorName, system, null);
	}

	public JobsExecutor(String executorName, boolean system, ISchedulingRule schedulingRule) {
		this(executorName, system, schedulingRule, 0L);
	}

	public JobsExecutor(String executorName, boolean system, ISchedulingRule schedulingRule, long delay) {
		this.fExecutorName = executorName;
		this.fSystem = system;
		this.fSchedulingRule = schedulingRule;
		this.delay = delay;
	}

	protected void setChildProgressMonitor(IProgressMonitor parent, IProgressMonitor child) {
		if (parent instanceof FutureProgressMonitor) {
			((FutureProgressMonitor) parent).setChildProgressMonitor(child);
		}
	}

	protected void safeRun(ISafeProgressRunner runner, IProgressRunnable progressRunnable) {
		runner.safeRun(progressRunnable);
	}

	protected String createJobName(String executorName, int jobCounter, IProgressRunnable runnable) {
		return "JobsExecutor(" + executorName + ")." + jobCounter; //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected AbstractFuture createFuture(IProgressMonitor progressMonitor) {
		return new SingleOperationFuture(progressMonitor);
	}

	public IFuture execute(final IProgressRunnable runnable, final IProgressMonitor clientProgressMonitor) {
		Assert.isNotNull(runnable);
		final AbstractFuture sof = createFuture(clientProgressMonitor);
		Job job = new Job(createJobName(fExecutorName, fJobCounter++, runnable)) {
			{
				setSystem(fSystem);
				setRule(fSchedulingRule);
			}

			protected IStatus run(IProgressMonitor monitor) {
				preSafeRun();
				// First check to make sure things haven't been canceled
				if (sof.isCanceled())
					return sof.getStatus();
				// Now add progress monitor as child of future progress monitor
				setChildProgressMonitor(sof.getProgressMonitor(), monitor);
				// Now run safely
				safeRun(sof, runnable);
				postSafeRun();
				return sof.getStatus();
			}
		};
		// Configure job before scheduling
		configureJobForExecution(job);
		job.schedule(delay);
		return sof;
	}

	protected void configureJobForExecution(Job job) {
		// do nothing by default
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3724.java