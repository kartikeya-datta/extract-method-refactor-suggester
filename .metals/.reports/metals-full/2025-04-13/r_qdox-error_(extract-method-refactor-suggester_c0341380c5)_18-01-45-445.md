error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1131.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1131.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1131.java
text:
```scala
g@@etStatusDialog().addStatusAdapter(statusAdapter, modal);

/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.statushandlers;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.progress.ProgressManagerUtil;

/**
 * This is a default workbench error handler.
 * 
 * @see WorkbenchAdvisor#getWorkbenchErrorHandler()
 * @since 3.3
 */
public class WorkbenchErrorHandler extends AbstractStatusHandler {

	private WorkbenchStatusDialog statusDialog;

	/**
	 * For testing purposes only. This method must not be used by any other
	 * clients.
	 * 
	 * @param dialog
	 *            a new WorkbenchStatusDialog to be set.
	 */
	void setStatusDialog(WorkbenchStatusDialog dialog) {
		statusDialog = dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.statushandlers.AbstractStatusHandler#handle(org.eclipse.ui.statushandlers.StatusAdapter,
	 *      int)
	 */
	public void handle(final StatusAdapter statusAdapter, int style) {
		if (((style & StatusManager.SHOW) == StatusManager.SHOW)
 ((style & StatusManager.BLOCK) == StatusManager.BLOCK)) {

			// INFO status is set in the adapter when the passed adapter has OK
			// or CANCEL status
			if (statusAdapter.getStatus().getSeverity() == IStatus.OK
 statusAdapter.getStatus().getSeverity() == IStatus.CANCEL) {
				IStatus status = statusAdapter.getStatus();
				statusAdapter.setStatus(new Status(IStatus.INFO, status
						.getPlugin(), status.getMessage(), status
						.getException()));
			}

			boolean modal = ((style & StatusManager.BLOCK) == StatusManager.BLOCK);
			getStatusDialog().addError(statusAdapter, modal);
		}

		if ((style & StatusManager.LOG) == StatusManager.LOG) {
			StatusManager.getManager().addLoggedStatus(
					statusAdapter.getStatus());
			WorkbenchPlugin.getDefault().getLog()
					.log(statusAdapter.getStatus());
		}
	}

	/**
	 * This method returns current {@link WorkbenchStatusDialog}.
	 * 
	 * @return current {@link WorkbenchStatusDialog}
	 */
	private WorkbenchStatusDialog getStatusDialog() {
		if (statusDialog == null) {
			initStatusDialog();
		}
		return statusDialog;
	}

	/**
	 * This methods should be overridden to configure
	 * {@link WorkbenchStatusDialog} behavior. It is advised to use only
	 * following methods of {@link WorkbenchStatusDialog}:
	 * <ul>
	 * <li>{@link WorkbenchStatusDialog#enableDefaultSupportArea(boolean)}</li>
	 * <li>{@link WorkbenchStatusDialog#setDetailsAreaProvider(AbstractStatusAreaProvider)}</li>
	 * <li>{@link WorkbenchStatusDialog#setSupportAreaProvider(AbstractStatusAreaProvider)}</li>
	 * </ul>
	 * Default configuration does nothing.
	 * 
	 * @param statusDialog
	 *            a status dialog to be configured.
	 */
	protected void configureStatusDialog(
			final WorkbenchStatusDialog statusDialog) {
		// default configuration does nothing
	}

	/**
	 * This method initializes {@link WorkbenchStatusDialog} and is called only
	 * once.
	 */
	private void initStatusDialog() {
		// this class must be instantiated in UI thread
		// (temporary solution, under investigation)
		if (Display.getCurrent() != null) {
			statusDialog = new WorkbenchStatusDialog(ProgressManagerUtil
					.getDefaultParent(), null);
			configureStatusDialog(statusDialog);
		} else {
			// if not ui than sync exec
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					statusDialog = new WorkbenchStatusDialog(
							ProgressManagerUtil.getDefaultParent(), null);
					configureStatusDialog(statusDialog);
				}
			});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1131.java