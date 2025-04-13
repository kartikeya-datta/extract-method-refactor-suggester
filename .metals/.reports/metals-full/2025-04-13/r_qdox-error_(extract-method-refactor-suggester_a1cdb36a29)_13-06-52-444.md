error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1295.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1295.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1295.java
text:
```scala
d@@ialog = new BlockedJobsDialog(null, EventLoopProgressMonitor.this,reason);

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
package org.eclipse.ui.internal.dialogs;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.Status;

import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.progress.WorkbenchJob;

import org.eclipse.ui.internal.progress.BlockedJobsDialog;
import org.eclipse.ui.internal.WorkbenchMessages;
/**
 * Used to run an event loop whenever progress monitor methods
 * are invoked.  <p>
 * This is needed since editor save operations are done in the UI thread.  
 * Although save operations should be written to do the work in the non-UI thread, 
 * this was not done for 1.0, so this was added to keep the UI live
 * (including allowing the cancel button to work).
 */
public class EventLoopProgressMonitor extends ProgressMonitorWrapper
		implements
			IProgressMonitorWithBlocking {
	/**
	 * Threshold for how often the event loop is spun, in ms.
	 */
	private static int T_THRESH = 100;
	/**
	 * Maximum amount of time to spend processing events, in ms.
	 */
	private static int T_MAX = 50;
	/**
	 * The dialog that is shown when the operation is blocked
	 */
	private BlockedJobsDialog dialog;
	/**
	 * Last time the event loop was spun.
	 */
	private long lastTime = System.currentTimeMillis();
	/**
	 * Constructs a new monitor.
	 */
	public EventLoopProgressMonitor(IProgressMonitor monitor) {
		super(monitor);
	}
	/** 
	 * @see IProgressMonitor#beginTask
	 */
	public void beginTask(String name, int totalWork) {
		super.beginTask(name, totalWork);
		runEventLoop();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
	 */
	public void clearBlocked() {
		//the UI operation is no longer blocked so get rid of the progress dialog
		if (dialog == null || dialog.getShell() == null || dialog.getShell().isDisposed()){
			dialog = null;
			return;
		}
		dialog.close();
		dialog = null;
	}
	/**
	 * @see IProgressMonitor#done
	 */
	public void done() {
		super.done();
		runEventLoop();
	}
	/**
	 * @see IProgressMonitor#internalWorked
	 */
	public void internalWorked(double work) {
		super.internalWorked(work);
		runEventLoop();
	}
	/**
	 * @see IProgressMonitor#isCanceled
	 */
	public boolean isCanceled() {
		runEventLoop();
		return super.isCanceled();
	}
	/**
	 * Runs an event loop.
	 */
	private void runEventLoop() {
		// Only run the event loop so often, as it is expensive on some platforms
		// (namely Motif).
		long t = System.currentTimeMillis();
		if (t - lastTime < T_THRESH) {
			return;
		}
		lastTime = t;
		// Run the event loop.
		Display disp = Display.getDefault();
		if (disp == null) {
			return;
		}
		for (;;) {
			if (!disp.readAndDispatch()) { // Exceptions walk back to parent.
				break;
			}
			// Only run the event loop for so long.
			// Otherwise, this would never return if some other thread was 
			// constantly generating events.
			if (System.currentTimeMillis() - t > T_MAX) {
				break;
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
	 */
	public void setBlocked(IStatus reason) {
		
		//The UI operation has been blocked.  Open a progress dialog
		//to report the situation and give the user an opportunity to cancel.
		
		dialog = new BlockedJobsDialog(null, EventLoopProgressMonitor.this);
		dialog.setBlockOnOpen(false);
		
		WorkbenchJob dialogJob = new WorkbenchJob(WorkbenchMessages.getString("EventLoopProgressMonitor.OpenDialogJobName")){ //$NON-NLS-1$
			/* (non-Javadoc)
			 * @see org.eclipse.ui.progress.UIJob#runInUIThread(org.eclipse.core.runtime.IProgressMonitor)
			 */
			public IStatus runInUIThread(IProgressMonitor monitor) {
				
				if(dialog == null)
					return Status.CANCEL_STATUS;
				dialog.open();
				return Status.OK_STATUS;
			}
		};
		
		//Wait 3 second to prevent too many dialogs.
		dialogJob.setSystem(true);
		dialogJob.schedule(3000);
		
	}
	/**
	 * @see IProgressMonitor#setCanceled
	 */
	public void setCanceled(boolean b) {
		super.setCanceled(b);
		runEventLoop();
	}
	/**
	 * @see IProgressMonitor#setTaskName
	 */
	public void setTaskName(String name) {
		super.setTaskName(name);
		runEventLoop();
	}
	/**
	 * @see IProgressMonitor#subTask
	 */
	public void subTask(String name) {
		super.subTask(name);
		runEventLoop();
	}
	/**
	 * @see IProgressMonitor#worked
	 */
	public void worked(int work) {
		super.worked(work);
		runEventLoop();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1295.java