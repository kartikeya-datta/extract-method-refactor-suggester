error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5751.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5751.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5751.java
text:
```scala
&@@& !shell.isDisposed()) {

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * This is a default workbench error handler.
 * 
 * @see WorkbenchAdvisor#getWorkbenchErrorHandler()
 * @since 3.3
 */
public class WorkbenchErrorHandler extends AbstractStatusHandler {

	private WorkbenchStatusDialogManager statusDialogManager;

	/**
	 * For testing purposes only. This method must not be used by any other
	 * clients.
	 * 
	 * @param manager
	 *            a new WorkbenchStatusDialog to be set.
	 */
	void setStatusDialog(WorkbenchStatusDialogManager manager) {
		statusDialogManager = manager;
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

			final boolean block = ((style & StatusManager.BLOCK) == StatusManager.BLOCK);
			
			if (Display.getCurrent() != null) {
				showStatusAdapter(statusAdapter, block);
			} else {
				if (block) {
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							showStatusAdapter(statusAdapter, true);
						}
					});

				} else {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							showStatusAdapter(statusAdapter, false);
						}
					});
				}
			}
		}

		if ((style & StatusManager.LOG) == StatusManager.LOG) {
			StatusManager.getManager().addLoggedStatus(
					statusAdapter.getStatus());
			WorkbenchPlugin.getDefault().getLog()
					.log(statusAdapter.getStatus());
		}
	}
	
	/**
	 * Requests the status dialog manager to show the status adapter.
	 * 
	 * @param statusAdapter
	 *            the status adapter to show
	 * @param block
	 *            <code>true</code> to request a modal dialog and suspend the
	 *            calling thread till the dialog is closed, <code>false</code>
	 *            otherwise.
	 */
	private void showStatusAdapter(StatusAdapter statusAdapter, boolean block) {
		if (!PlatformUI.isWorkbenchRunning()) {
			// we are shutting down, so just log
			WorkbenchPlugin.log(statusAdapter.getStatus());
			return;
		}

		getStatusDialogManager().addStatusAdapter(statusAdapter, block);

		if (block) {
			Shell shell;
			while ((shell = getStatusDialogManager().getShell()) != null
					&& !getStatusDialogManager().getShell().isDisposed()) {
				if (!shell.getDisplay().readAndDispatch()) {
					Display.getDefault().sleep();
				}
			}
		}
	}

	/**
	 * This method returns current {@link WorkbenchStatusDialogManager}.
	 * 
	 * @return current {@link WorkbenchStatusDialogManager}
	 */
	private WorkbenchStatusDialogManager getStatusDialogManager() {
		if (statusDialogManager == null) {
			synchronized (this) {
				if (statusDialogManager == null) {
					statusDialogManager = new WorkbenchStatusDialogManager(null);
					configureStatusDialog(statusDialogManager);
				}
			}
		}
		return statusDialogManager;
	}

	/**
	 * This methods should be overridden to configure
	 * {@link WorkbenchStatusDialogManager} behavior. It is advised to use only
	 * following methods of {@link WorkbenchStatusDialogManager}:
	 * <ul>
	 * <li>{@link WorkbenchStatusDialogManager#enableDefaultSupportArea(boolean)}</li>
	 * <li>{@link WorkbenchStatusDialogManager#setDetailsAreaProvider(AbstractStatusAreaProvider)}</li>
	 * <li>{@link WorkbenchStatusDialogManager#setSupportAreaProvider(AbstractStatusAreaProvider)}</li>
	 * </ul>
	 * Default configuration does nothing.
	 * 
	 * @param statusDialog
	 *            a status dialog to be configured.
	 * @since 3.4
	 */
	protected void configureStatusDialog(
			final WorkbenchStatusDialogManager statusDialog) {
		// default configuration does nothing
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5751.java