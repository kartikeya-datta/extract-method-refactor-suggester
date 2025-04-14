error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5428.java
text:
```scala
t@@imeoutException = new TimeoutException("Timout", timeout); //$NON-NLS-1$

/*******************************************************************************
* Copyright (c) 2008 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.core.util;

import org.eclipse.core.runtime.*;
import org.eclipse.ecf.internal.core.ECFPlugin;
import org.eclipse.osgi.util.NLS;

public class SingleOperationFuture implements IFuture {

	class SingleOperationFutureProgressMonitor implements IProgressMonitor {

		private final IProgressMonitor monitor;

		public SingleOperationFutureProgressMonitor(IProgressMonitor progressMonitor) {
			this.monitor = progressMonitor;
		}

		public void beginTask(String name, int totalWork) {
			monitor.beginTask(name, totalWork);
		}

		public void done() {
			monitor.done();
		}

		public void internalWorked(double work) {
			monitor.internalWorked(work);
		}

		public boolean isCanceled() {
			return monitor.isCanceled();
		}

		public void setCanceled(boolean value) {
			monitor.setCanceled(value);
			// If this is intended to cancel
			// the operation, then we also call
			// SingleOperationFuture.this.setCanceled()
			if (value)
				SingleOperationFuture.this.setCanceled();
		}

		public void setTaskName(String name) {
			monitor.setTaskName(name);
		}

		public void subTask(String name) {
			monitor.subTask(name);
		}

		public void worked(int work) {
			monitor.worked(work);
		}

	}

	private Object resultValue = null;
	private IStatus status = null;
	private final IProgressMonitor progressMonitor;
	private TimeoutException timeoutException = null;

	public SingleOperationFuture(IExecutor executor, IProgressRunnable progressRunnable, IProgressMonitor progressMonitor) {
		Assert.isNotNull(executor);
		Assert.isNotNull(progressRunnable);
		this.progressMonitor = createProgressMonitor(progressMonitor);
		executor.execute(setter(progressRunnable));
	}

	public SingleOperationFuture(IExecutor executor, IProgressRunnable progressRunnable) {
		Assert.isNotNull(executor);
		Assert.isNotNull(progressRunnable);
		this.progressMonitor = createProgressMonitor(null);
		executor.execute(setter(progressRunnable));
	}

	public SingleOperationFuture(IProgressRunnable progressRunnable, IProgressMonitor progressMonitor) {
		Assert.isNotNull(progressRunnable);
		this.progressMonitor = createProgressMonitor(progressMonitor);
		Thread t = new Thread(setter(progressRunnable), this.toString());
		t.start();
	}

	public SingleOperationFuture(IProgressRunnable progressRunnable) {
		Assert.isNotNull(progressRunnable);
		this.progressMonitor = createProgressMonitor(null);
		Thread t = new Thread(setter(progressRunnable), this.toString());
		t.start();
	}

	public SingleOperationFuture(IProgressMonitor progressMonitor) {
		this.progressMonitor = createProgressMonitor(progressMonitor);
	}

	public SingleOperationFuture() {
		this((IProgressMonitor) null);
	}

	private IProgressMonitor createProgressMonitor(IProgressMonitor pm) {
		return new SingleOperationFutureProgressMonitor((pm == null) ? new NullProgressMonitor() : pm);
	}

	public synchronized Object get() throws InterruptedException, OperationCanceledException {
		throwIfCanceled();
		while (!isDone())
			wait();
		throwIfCanceled();
		return resultValue;
	}

	public synchronized Object get(long waitTimeInMillis) throws InterruptedException, TimeoutException, OperationCanceledException {
		// If we've been canceled then throw
		throwIfCanceled();
		// If we've previously experienced a timeout then throw
		if (timeoutException != null)
			throw timeoutException;
		// Compute start time and waitTime
		long startTime = (waitTimeInMillis <= 0) ? 0 : System.currentTimeMillis();
		long waitTime = waitTimeInMillis;
		// If waitTime out of bounds then throw timeout exception
		if (waitTime <= 0)
			throw createTimeoutException(waitTimeInMillis);
		// If we're already done, then return result
		if (isDone())
			return resultValue;
		// Otherwise, wait for some time, then throw if canceled during wait, return value if
		// we've received one during wait or throw timeout exception if too much time has elapsed
		for (;;) {
			wait(waitTime);
			throwIfCanceled();
			if (isDone())
				return resultValue;
			waitTime = waitTimeInMillis - (System.currentTimeMillis() - startTime);
			if (waitTime <= 0)
				throw createTimeoutException(waitTimeInMillis);
		}
	}

	public IProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public synchronized boolean isDone() {
		return (status != null);
	}

	synchronized void setCanceled() {
		setStatus(new Status(IStatus.ERROR, ECFPlugin.PLUGIN_ID, IStatus.ERROR, "Operation cancelled", null)); //$NON-NLS-1$
		notifyAll();
	}

	/**
	 * Set the underlying function call that will return a result asynchronously
	 * 
	 * @param function
	 *            the {@link IProgressRunnable} to be called
	 * @return Runnable to run in separate thread
	 */
	public Runnable setter(final IProgressRunnable function) {
		return new Runnable() {
			public void run() {
				try {
					set(function.run(getProgressMonitor()));
				} catch (Throwable ex) {
					setException(ex);
				}
			}
		};
	}

	public synchronized void setException(Throwable ex) {
		setStatus(new Status(IStatus.ERROR, ECFPlugin.PLUGIN_ID, IStatus.ERROR, "Exception during operation", ex)); //$NON-NLS-1$
		notifyAll();
	}

	public synchronized void set(Object newValue) {
		resultValue = newValue;
		setStatus(Status.OK_STATUS);
		notifyAll();
	}

	public synchronized IStatus getStatus() {
		return status;
	}

	private synchronized void setStatus(IStatus status) {
		this.status = status;
	}

	private TimeoutException createTimeoutException(long timeout) {
		setStatus(new Status(IStatus.ERROR, ECFPlugin.PLUGIN_ID, IStatus.ERROR, NLS.bind("Operation timeout after {0}ms", new Long(timeout)), null)); //$NON-NLS-1$
		timeoutException = new TimeoutException(getStatus(), timeout);
		return timeoutException;
	}

	private void throwIfCanceled() throws OperationCanceledException {
		IProgressMonitor pm = getProgressMonitor();
		if (pm != null && pm.isCanceled()) {
			throw new OperationCanceledException("Operation canceled"); //$NON-NLS-1$
		}
	}

	public boolean hasValue() {
		return isDone();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5428.java