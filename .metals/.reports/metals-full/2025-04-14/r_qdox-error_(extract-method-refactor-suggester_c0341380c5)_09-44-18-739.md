error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1095.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1095.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1095.java
text:
```scala
P@@rogressContentProvider provider = (ProgressContentProvider) viewer.getContentProvider();

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

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.ViewSite;
import org.eclipse.ui.part.ViewPart;

/**
 * The ProgressView is the class that shows the details of the
 * current workbench progress.
 */

public class ProgressView extends ViewPart implements IViewPart {

	ProgressTreeViewer viewer;
	Action cancelAction;
	Action deleteAction;
	Action showErrorAction;
	Action clearErrorsAction;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		viewer =
			new ProgressTreeViewer(
				parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setUseHashlookup(true);
		viewer.setSorter(getViewerSorter());

		initContentProvider();
		initLabelProvider();
		initContextMenu();
		initPulldownMenu();
		getSite().setSelectionProvider(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {

	}
	/**
	 * Sets the content provider for the viewer.
	 */
	protected void initContentProvider() {
		IContentProvider provider = new ProgressContentProvider(viewer);
		viewer.setContentProvider(provider);
		viewer.setInput(provider);
	}

	/**
	 * Sets the label provider for the viewer.
	 */
	protected void initLabelProvider() {
		viewer.setLabelProvider(new ProgressLabelProvider());

	}

	/**
	 * Initialize the context menu for the receiver.
	 */

	private void initContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$

		Menu menu = menuMgr.createContextMenu(viewer.getTree());

		createCancelAction();
		createDeleteAction();
		createShowErrorAction();
		createClearErrorsAction();
		menuMgr.add(cancelAction);
		menuMgr.add(deleteAction);
		menuMgr.add(showErrorAction);
		menuMgr.add(clearErrorsAction);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				cancelAction.setEnabled(false);
				deleteAction.setEnabled(false);
				showErrorAction.setEnabled(false);
				JobInfo info = getSelectedInfo();
				if (info == null) {
					return;
				}
				int code = info.getJob().getState();
				if (code == Job.RUNNING)
					cancelAction.setEnabled(true);
				else if (info.getErrorStatus() != null) {
					deleteAction.setEnabled(true);
					showErrorAction.setEnabled(true);
				}

			}
		});

		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(menuMgr, viewer);
		viewer.getTree().setMenu(menu);

	}

	private void initPulldownMenu() {
		IMenuManager menuMgr =
			((ViewSite) getSite()).getActionBars().getMenuManager();
		menuMgr.add(new Action(ProgressMessages.getString("ProgressView.VerboseAction"), IAction.AS_CHECK_BOX) { //$NON-NLS-1$

			/* (non-Javadoc)
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				JobProgressManager provider = JobProgressManager.getInstance();
				provider.debug = !provider.debug;
				setChecked(provider.debug);
				provider.refreshAll();
			}

		});

	}

	/**
	 * Return the selected objects. If any of the selections are 
	 * not JobInfos or there is no selection then return null.
	 * @return JobInfo[] or <code>null</code>.
	 */
	private IStructuredSelection getSelection() {

		//If the provider has not been set yet move on.
		ISelectionProvider provider = getSite().getSelectionProvider();
		if (provider == null)
			return null;
		ISelection currentSelection = provider.getSelection();
		if (currentSelection instanceof IStructuredSelection) {
			return (IStructuredSelection) currentSelection;
		}
		return null;
	}

	/**
	 * Get the currently selected job info. Only return 
	 * it if it is the only item selected and it is a
	 * JobInfo.
	 * @return
	 */
	JobInfo getSelectedInfo() {
		IStructuredSelection selection = getSelection();
		if (selection != null && selection.size() == 1) {
			JobTreeElement element =
				(JobTreeElement) selection.getFirstElement();
			if (element.isJobInfo())
				return (JobInfo) element;
		}
		return null;

	}

	/**
	 * Return a viewer sorter for looking at the jobs.
	 * @return
	 */
	private ViewerSorter getViewerSorter() {
		return new ViewerSorter() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
			 */
			public int compare(Viewer testViewer, Object e1, Object e2) {
				return ((Comparable) e1).compareTo(e2);
			}
		};
	}

	/**
	 * Create the cancel action for the receiver.
	 * @return Action
	 */
	private void createCancelAction() {
			cancelAction = new Action(ProgressMessages.getString("ProgressView.CancelAction")) {//$NON-NLS-1$
	/* (non-Javadoc)
	  * @see org.eclipse.jface.action.Action#run()
	 */
			public void run() {
				JobInfo element = getSelectedInfo();
				//Check it case it got removed after enablement
				if (element == null) {
					return;
				}
				element.getJob().cancel();

			}

		};
	}

	/**
	 * Create the delete action for the receiver.
	 * @return Action
	 */
	private void createDeleteAction() {
			deleteAction = new Action(ProgressMessages.getString("ProgressView.DeleteAction")) {//$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
			public void run() {
				JobInfo element = getSelectedInfo();
				//Check it case it got removed after enablement
				if (element == null) {
					return;
				}
				JobProgressManager.getInstance().clearJob(element.getJob());
			}
		};
	}

	/**
	 * Create the clear all errors action for the receiver.
	 * @return Action
	 */
	private void createClearErrorsAction() {
			clearErrorsAction = new Action(ProgressMessages.getString("ProgressView.ClearAllAction")) {//$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
			public void run() {
				JobProgressManager.getInstance().clearAllErrors();
			}
		};
	}

	/**
	 * Create the show error action for the receiver.
	 * @return Action
	 */
	private void createShowErrorAction() {
			showErrorAction = new Action(ProgressMessages.getString("ProgressView.ShowErrorAction")) {//$NON-NLS-1$
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
			public void run() {
				JobInfo element = getSelectedInfo();
				ErrorDialog.openError(
					viewer.getControl().getShell(),
					element.getDisplayString(),
					element.getErrorStatus().getMessage(),
					element.getErrorStatus());
			}

		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1095.java