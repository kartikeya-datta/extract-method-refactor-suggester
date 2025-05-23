error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1312.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1312.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1312.java
text:
```scala
r@@eturn new ProgressTreeContentProvider(viewer, true) {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
/**
 * The BlockedJobsDialog class displays a dialog that provides information on
 * the running jobs.
 */
public class BlockedJobsDialog extends IconAndMessageDialog {
	/**
	 * The running jobs progress tree.
	 * 
	 * @see /org.eclipse.ui.workbench/Eclipse
	 *      UI/org/eclipse/ui/internal/progress/ProgressTreeViewer.java
	 *      (org.eclipse.ui.internal.progress)
	 */
	private ProgressTreeViewer viewer;
	/**
	 * The name of the task that is being blocked.
	 */
	private String blockedTaskName = null;
	/**
	 * The Cancel button control.
	 */
	private Button cancelSelected;
	/**
	 * The cursor for the buttons.
	 */
	private Cursor arrowCursor;
	/**
	 * The cursor for the Shell.
	 */
	private Cursor waitCursor;
	private IProgressMonitor blockingMonitor;
	private JobTreeElement blockedElement = new BlockedUIElement();
	/**
	 * The BlockedUIElement is the JobTreeElement that represents the blocked
	 * job in the dialog.
	 */
	private class BlockedUIElement extends JobTreeElement {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#getChildren()
		 */
		Object[] getChildren() {
			return null;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#getDisplayString()
		 */
		String getDisplayString() {
			if (blockedTaskName == null)
				return ProgressMessages.getString("BlockedJobsDialog.UserInterfaceTreeElement"); //$NON-NLS-1$
			else
				return blockedTaskName;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#getDisplayImage()
		 */
		public Image getDisplayImage() {
			return JFaceResources.getImage(ProgressManager.WAITING_JOB_KEY);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#getParent()
		 */
		Object getParent() {
			return null;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#hasChildren()
		 */
		boolean hasChildren() {
			return false;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#isActive()
		 */
		boolean isActive() {
			return true;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#isJobInfo()
		 */
		boolean isJobInfo() {
			return false;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#cancel()
		 */
		public void cancel() {
			blockingMonitor.setCanceled(true);
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.progress.JobTreeElement#isCancellable()
		 */
		public boolean isCancellable() {
			return true;
		}
	}
	/**
	 * Creates a progress monitor dialog under the given shell. It also sets
	 * the dialog's\ message. <code>open</code> is non-blocking.
	 * 
	 * @param parentShell
	 *            The parent shell, or <code>null</code> to create a
	 *            top-level shell.
	 * @param blocking
	 *            The monitor that is blocking the job
	 */
	public BlockedJobsDialog(Shell parentShell, IProgressMonitor blocking, IStatus blockingStatus) {
		super(parentShell);
		blockingMonitor = blocking;
		setShellStyle(SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		// no close button
		setBlockOnOpen(false);
		setMessage(blockingStatus.getMessage());
	}
	/**
	 * This method creates the dialog area under the parent composite.
	 * 
	 * @param parent
	 *            The parent Composite.
	 * 
	 * @return parent The parent Composite.
	 */
	protected Control createDialogArea(Composite parent) {
		setMessage(message);
		createMessageArea(parent);
		showJobDetails(parent);
		return parent;
	}

	/**
	 * This method creates a dialog area in the parent composite and displays a
	 * progress tree viewer of the running jobs.
	 * 
	 * @param parent
	 *            The parent Composite.
	 */
	void showJobDetails(Composite parent) {
		if (ProgressManagerUtil.useNewProgress())
			viewer = new NewProgressViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
 SWT.BORDER) {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.internal.progress.ProgressTreeViewer#updateColors(org.eclipse.swt.widgets.TreeItem,
				 *      org.eclipse.ui.internal.progress.JobTreeElement)
				 */
				protected void updateColors(TreeItem treeItem, JobTreeElement element) {
					super.updateColors(treeItem, element);
					//Color the blocked element the not running color.
					if (element == blockedElement)
						setNotRunningColor(treeItem);
				}
			};
		else
		viewer = new ProgressTreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL
 SWT.BORDER) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.ProgressTreeViewer#updateColors(org.eclipse.swt.widgets.TreeItem,
			 *      org.eclipse.ui.internal.progress.JobTreeElement)
			 */
			protected void updateColors(TreeItem treeItem, JobTreeElement element) {
				super.updateColors(treeItem, element);
				//Color the blocked element the not running color.
				if (element == blockedElement)
					setNotRunningColor(treeItem);
			}
		};
		viewer.setUseHashlookup(true);
		viewer.setSorter(new ViewerSorter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			public int compare(Viewer testViewer, Object e1, Object e2) {
				return ((Comparable) e1).compareTo(e2);
			}
		});
		IContentProvider provider = getContentProvider();
		viewer.setContentProvider(provider);
		viewer.setInput(provider);
		viewer.setLabelProvider(new ProgressLabelProvider());
		GridData data = new GridData();
		data.horizontalSpan = 2;
		int heightHint = convertHeightInCharsToPixels(10);
		data.heightHint = heightHint;
		data.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(data);
	}
	/**
	 * Return the content provider used for the receiver.
	 * 
	 * @return ProgressTreeContentProvider
	 */
	private ProgressTreeContentProvider getContentProvider() {
		return new ProgressTreeContentProvider(viewer) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.progress.ProgressContentProvider#getElements(java.lang.Object)
			 */
			public Object[] getElements(Object inputElement) {
				Object[] elements = super.getElements(inputElement);
				Object[] result = new Object[elements.length + 1];
				System.arraycopy(elements, 0, result, 1, elements.length);
				result[0] = blockedElement;
				return result;
			}
		};
	}
	/**
	 * This method overrides the IconAndMessageDialog's method to create a
	 * Cancel button.
	 * 
	 * @param parent
	 *            The parent Composite.
	 * 
	 * @return parent The parent Composite.
	 */
	protected Control createButtonBar(Composite parent) {
		//new progress view does not need a cancel button
		//this method can be deleted when the old progress view option
		//is removed
		if (ProgressManagerUtil.useNewProgress())
			return parent;
		cancelSelected = createButton(parent, ProgressMessages.getString("CancelJobsButton.title"), //$NON-NLS-1$
				(new SelectionListener() {
					public void widgetSelected(SelectionEvent e) {
						viewer.cancelSelection();
					}
					public void widgetDefaultSelected(SelectionEvent e) {
						// Method used to set the default selection.
					}
				}));
		return parent;
	}
	/**
	 * Create a button with the supplied parameters.
	 * 
	 * @param parent
	 * @param text
	 * @param listener
	 * @return
	 */
	private Button createButton(Composite parent, String text, SelectionListener listener) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text); //$NON-NLS-1$
		button.addSelectionListener(listener);
		if (arrowCursor == null)
			arrowCursor = new Cursor(button.getDisplay(), SWT.CURSOR_ARROW);
		button.setCursor(arrowCursor);
		return button;
	}
	/**
	 * Clear the cursors in the dialog.
	 */
	private void clearCursors() {
		clearCursor(cancelSelected);
		clearCursor(getShell());
		if (arrowCursor != null)
			arrowCursor.dispose();
		if (waitCursor != null)
			waitCursor.dispose();
		arrowCursor = null;
		waitCursor = null;
	}
	/**
	 * Clear the cursor on the supplied control.
	 * 
	 * @param control
	 */
	private void clearCursor(Control control) {
		if (control != null && !control.isDisposed()) {
			control.setCursor(null);
		}
	}
	/**
	 * This method complements the Window's class' configureShell method by
	 * adding a title, and setting the appropriate cursor.
	 * 
	 * @param shell
	 *            The dialog's shell.
	 * 
	 * @see /org.eclipse.jface/src/org/eclipse/jface/window/Window.java
	 *      (org.eclipse.jface.window;)
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(ProgressMessages.getString("BlockedJobsDialog.BlockedTitle")); //$NON-NLS-1$
		if (waitCursor == null)
			waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
	}
	/**
	 * This method sets the message in the message label.
	 */
	private void setMessage(String messageString) {
		//must not set null text in a label
		message = messageString == null ? "" : messageString; //$NON-NLS-1$
		if (messageLabel == null || messageLabel.isDisposed())
			return;
		messageLabel.setText(message);
	}
	/**
	 * This method returns the dialog's lock image.
	 */
	protected Image getImage() {
		return JFaceResources.getImageRegistry().get(Dialog.DLG_IMG_LOCKED);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	public boolean close() {
		clearCursors();
		return super.close();
	}
	/**
	 * Set the name of the task being blocked. If this value is
	 * not set then the default blocked name will be used.
	 * @param taskName
	 */
	public void setBlockedTaskName(String taskName) {
		blockedTaskName = taskName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1312.java