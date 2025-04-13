error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3589.java
text:
```scala
i@@f (asyncDisplay == null || display.isDisposed()) {

/**********************************************************************
 * Copyright (c) 2003 IBM Corporation and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.ui.progress;

/**
 * The UIJob is a Job that runs within the UI Thread via an
 * asyncExec.
 *  @since 3.0
 */
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.misc.Assert;
import org.eclipse.ui.internal.progress.ProgressMessages;

public abstract class UIJob extends Job {
	private Display display;


	/**
	 * Create a new instance of the receiver with the supplied name.
	 * <b>NOTE:<b> It is recommended that <code>UIJob(String,Display></code>
	 * is used if possible so as to prevent unneccessary creation
	 * of the workbench or a display by the jobs mechanism.
	 * 
	 * @param name the job name
	 * 
	 */
	public UIJob(String name) {
		this(Display.getDefault(), name);
	}

	/**
	 * Create a new instance of the receiver with the supplied
	 * Display.
	 * 
	 * @param jobDisplay the display 
	 * @param name the job name
	 */
	public UIJob(Display jobDisplay, String name) {
		super(name);
		setDisplay(jobDisplay);
	}

	/**
	 * Convenience method to return a status for an exception.
	 * @param exception
	 * @return
	 */
	public static IStatus errorStatus(Throwable exception) {
		return new Status(
			IStatus.ERROR,
			PlatformUI.PLUGIN_ID,
			IStatus.ERROR,
			exception.getMessage(),
			exception);

	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 * Note: this message is marked final. Implementors should use
	 * runInUIThread() instead.
	 */
	public final IStatus run(final IProgressMonitor monitor) {

		Display asyncDisplay = getDisplay();

		if (asyncDisplay == null) {
			return Status.CANCEL_STATUS;
		}
		asyncDisplay.asyncExec(new Runnable() {
			public void run() {
				IStatus result = null;
				try {
					//As we are in the UI Thread we can
					//always know what to tell the job.
					setThread(Thread.currentThread());
					result = runInUIThread(monitor);
				} finally {
					if (result == null)
							result = new Status(
									IStatus.ERROR, 
									PlatformUI.PLUGIN_ID, 
									IStatus.ERROR, 
									ProgressMessages.getString("Error"), //$NON-NLS-1$
									null);
					done(result);
				}
			}
		});
		return Job.ASYNC_FINISH;
	}

	/**
	 * Run the job in the UI Thread.
	 * @param monitor
	 * @return IStatus
	 */
	public abstract IStatus runInUIThread(IProgressMonitor monitor);

	/**
	 * Sets the display to execute the asyncExec in.
	 * @param runDisplay
	 */
	public void setDisplay(Display runDisplay) {
		Assert.isNotNull(runDisplay);
		display = runDisplay;
	}
	/**
	 * Returns the display for use by the receiver. 
	 */
	public Display getDisplay() {
		return display;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3589.java