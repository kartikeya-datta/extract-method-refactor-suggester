error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3722.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3722.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3722.java
text:
```scala
public final v@@oid run() {

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.operations;

import java.text.MessageFormat;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * <p>
 * OperationHistoryActionHandler implements common behavior for the undo and redo
 * actions. It supports filtering of undo or redo on a particular context. (A
 * null context will cause undo and redo to be disabled.)  
 * </p>
 * <p>
 * OperationHistoryActionHandler provides an adapter in the info parameter of the
 * IOperationHistory undo and redo methods that is used to get UI info for prompting
 * the user during operations or operation approval.  Adapters are provided for
 * org.eclipse.ui.IWorkbenchWindow, org.eclipse.swt.widgets.Shell, and 
 * org.eclipse.ui.IWorkbenchPart.
 * </p>
 * <p>
 * OperationHistoryActionHandler assumes a linear undo/redo model. When the
 * handler is run, the operation history is asked to perform the most recent
 * undo for the handler's context. The handler can be configured (using
 * #setPruneHistory(true) to flush the operation undo or redo history for its
 * context when there is no valid operation on top of the history.  This avoids
 * keeping a stale history of invalid operations.  By default, pruning does not 
 * occur and it is assumed that clients of the particular undo context are pruning 
 * the history when necessary.
 * </p>
 * 
 * @since 3.1
  */
public abstract class OperationHistoryActionHandler extends Action implements
		ActionFactory.IWorkbenchAction, IAdaptable, IOperationHistoryListener {

	private class PartListener implements IPartListener {
		/**
		 * @see IPartListener#partActivated(IWorkbenchPart)
		 */
		public void partActivated(IWorkbenchPart part) {
		}

		/**
		 * @see IPartListener#partBroughtToTop(IWorkbenchPart)
		 */
		public void partBroughtToTop(IWorkbenchPart part) {
		}

		/**
		 * @see IPartListener#partClosed(IWorkbenchPart)
		 */
		public void partClosed(IWorkbenchPart part) {
			if (part.equals(site.getPart())) {
				dispose();
			}
		}

		/**
		 * @see IPartListener#partDeactivated(IWorkbenchPart)
		 */
		public void partDeactivated(IWorkbenchPart part) {
		}

		/**
		 * @see IPartListener#partOpened(IWorkbenchPart)
		 */
		public void partOpened(IWorkbenchPart part) {
		}
		
	}
	protected IUndoContext undoContext = null;
	private boolean pruning = false;

	private IPartListener partListener = new PartListener();
	protected IWorkbenchPartSite site;
	
	/**
	 * Construct an operation history action for the specified workbench window
	 * with the specified undo context.
	 * 
	 * @param site -
	 *            the workbench part site for the action.
	 * @param context -
	 *            the undo context to be used
	 */
	public OperationHistoryActionHandler(IWorkbenchPartSite site,
			IUndoContext context) {
		// string will be reset inside action
		super(""); //$NON-NLS-1$
		this.site = site;
		undoContext = context;
		site.getPage().addPartListener(partListener);
		getHistory().addOperationHistoryListener(this);
        // An update must be forced in case the undo limit is 0.
        // see bug #89707
        update();
	}

	/**
	 * Dispose of any resources allocated by this action.
	 */
	public void dispose() {
		getHistory().removeOperationHistoryListener(this);
		site.getPage().removePartListener(partListener);
		// we do not do anything to the history for our context because it may
		// be used elsewhere.
	}

	/*
	 * Flush the history associated with this action.
	 */
	protected abstract void flush();

	/*
	 * Return the string describing the command.
	 */
	protected abstract String getCommandString();

	/*
	 * Return the operation history we are using.
	 */
	protected IOperationHistory getHistory() {
		return getWorkbenchWindow().getWorkbench().getOperationSupport()
				.getOperationHistory();
	}

	/*
	 * Return the current operation.
	 */
	protected abstract IUndoableOperation getOperation();

	/**
	 * Run the action. Provide common error handling and let the subclasses do the
	 * real work.
	 */
	public void run() {
		try {
			runCommand();
		} catch (ExecutionException e) {
			reportException(e);
			if (pruning)
				flush();
		}

	}
	
	abstract IStatus runCommand() throws ExecutionException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(Shell.class)) {
			return getWorkbenchWindow().getShell();
		}
		if (adapter.equals(IWorkbenchWindow.class)) {
			return getWorkbenchWindow();
		}
		if (adapter.equals(IWorkbenchPart.class)) {
			return site.getPart();
		}
		if (adapter.equals(IUndoContext.class)) {
			return undoContext;
		}
		return null;
	}

	/*
	 * Return the progress monitor that should be used for operations
	 */
	protected IProgressMonitor getProgressMonitor() {
		return null;
	}
	
	/*
	 * Return the workbench window for this action handler
	 */
	private IWorkbenchWindow getWorkbenchWindow() {
		return site.getWorkbenchWindow();
	}

	/**
	 * The undo and redo subclasses should implement this.
	 * 
	 * @return - a boolean indicating enablement state
	 */
	protected abstract boolean shouldBeEnabled();

	/**
	 * Set the context shown by the handler. Normally the context is set as
	 * parts activate and deactivate, but a part may wish to set the context
	 * manually.
	 * 
	 * @param context -
	 *            the context to be used for the undo history
	 */
	public void setContext(IUndoContext context) {
		undoContext = context;
		update();
	}

	/**
	 * Specify whether the action handler should actively prune the operation
	 * history when invalid operations are encountered. The default value is
	 * <code>false</code>.
	 * 
	 * @param prune -
	 *            <code>true</code> if the history should be pruned by the
	 *            handler, and <code>false</code> if it should not.
	 * 
	 */
	public void setPruneHistory(boolean prune) {
		pruning = prune;
	}

	/**
	 * Something has changed in the operation history. Check to see if it
	 * involves this context.
	 * 
	 * @param event -
	 *            the event that has occurred.
	 */
	public void historyNotification(OperationHistoryEvent event) {
		switch (event.getEventType()) {
		case OperationHistoryEvent.OPERATION_ADDED:
		case OperationHistoryEvent.OPERATION_REMOVED:
		case OperationHistoryEvent.UNDONE:
		case OperationHistoryEvent.REDONE:
			if (event.getOperation().hasContext(undoContext)) {
		        site.getShell().getDisplay().asyncExec(new Runnable() {
		            public void run() {
		                update();
		            }
		        });
			}
			break;
		case OperationHistoryEvent.OPERATION_NOT_OK:
			if (event.getOperation().hasContext(undoContext)) {
		        site.getShell().getDisplay().asyncExec(new Runnable() {
		            public void run() {
		                if (pruning)
		                	flush();
		                else
		                	update();
		            }
		        });
			}
			break;
		case OperationHistoryEvent.OPERATION_CHANGED:
			if (event.getOperation() == getOperation()){
		        site.getShell().getDisplay().asyncExec(new Runnable() {
		            public void run() {
		                update();
		            }
		        });
			}
			break;
		}
	}

	/**
	 * Update enabling and labels according to the current status of the
	 * history.
	 */
	public void update() {
		boolean enabled = shouldBeEnabled();
		String text = getCommandString();
		if (enabled) {
			text = MessageFormat
					.format(
							"{0} {1}", new Object[] { text, getOperation().getLabel() }); //$NON-NLS-1$
		} else {
			/*
			 * if there is nothing to do and we are pruning the history, flush
			 * the history of this context.
			 */
			if (undoContext != null && pruning)
				flush();
		}
		setText(text.toString());
		setEnabled(enabled);
	}

	/*
	 * Report the specified execution exception to the log and to the user.
	 */
	final void reportException(ExecutionException e) {
		Throwable nestedException = e.getCause();
		Throwable exception = (nestedException == null) ? e : nestedException;
		String title = WorkbenchMessages.Error;
		String message = WorkbenchMessages.WorkbenchWindow_exceptionMessage;
		String exceptionMessage = exception.getMessage();
		if (exceptionMessage == null) {
			exceptionMessage = message;
		}
		IStatus status = new Status(IStatus.ERROR,
				WorkbenchPlugin.PI_WORKBENCH, 0, exceptionMessage, exception);
		WorkbenchPlugin.log(message, status);
		ErrorDialog.openError(getWorkbenchWindow().getShell(), title, message,
				status);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3722.java