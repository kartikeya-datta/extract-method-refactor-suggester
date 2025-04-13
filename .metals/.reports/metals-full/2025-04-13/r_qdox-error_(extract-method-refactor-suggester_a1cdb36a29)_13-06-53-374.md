error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4289.java
text:
```scala
F@@inishedJobs.getInstance().clearAll();

/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.progress;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.preferences.ViewPreferencesAction;

/**
 * The ProgressView is the class that shows the details of the current workbench
 * progress.
 */
public class ProgressView extends ViewPart implements IViewPart {

	DetailedProgressViewer viewer;

	Action cancelAction;

	Action clearAllAction;

	private ProgressViewerContentProvider provider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		viewer = new DetailedProgressViewer(parent, SWT.MULTI);
		viewer.setUseHashlookup(false);
		viewer.setSorter(ProgressManagerUtil.getProgressViewerSorter());

		viewer.getControl().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true));

		initContentProvider();
		createClearAllAction();
		createCancelAction();
		initContextMenu();
		initPulldownMenu();
		initToolBar();
		getSite().setSelectionProvider(viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (viewer != null)
			viewer.setFocus();
	}

	/**
	 * Sets the content provider for the viewer.
	 */
	protected void initContentProvider() {
		provider = new ProgressViewerContentProvider(viewer, PrefUtil
				.getAPIPreferenceStore().getBoolean(
						IWorkbenchPreferenceConstants.SHOW_SYSTEM_JOBS),true);
		viewer.setContentProvider(provider);
		viewer.setInput(ProgressManager.getInstance());
	}

	/**
	 * Initialize the context menu for the receiver.
	 */
	private void initContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		menuMgr.add(cancelAction);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JobInfo info = getSelectedInfo();
				if (info == null)
					return;
			}
		});
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(menuMgr, viewer);
		viewer.getControl().setMenu(menu);
	}

	private void initPulldownMenu() {
		IMenuManager menuMgr = getViewSite().getActionBars().getMenuManager();
		menuMgr.add(clearAllAction);
		menuMgr.add(new ViewPreferencesAction() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.ui.internal.preferences.ViewPreferencesAction#openViewPreferencesDialog()
			 */
			public void openViewPreferencesDialog() {
				new JobsViewPreferenceDialog(viewer.getControl().getShell())
						.open();

			}
		});

	}

	private void initToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		IToolBarManager tm = bars.getToolBarManager();
		tm.add(clearAllAction);
	}

	/**
	 * Return the selected objects. If any of the selections are not JobInfos or
	 * there is no selection then return null.
	 * 
	 * @return JobInfo[] or <code>null</code>.
	 */
	private IStructuredSelection getSelection() {
		// If the provider has not been set yet move on.
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
	 * Get the currently selected job info. Only return it if it is the only
	 * item selected and it is a JobInfo.
	 * 
	 * @return JobInfo
	 */
	JobInfo getSelectedInfo() {
		IStructuredSelection selection = getSelection();
		if (selection != null && selection.size() == 1) {
			JobTreeElement element = (JobTreeElement) selection
					.getFirstElement();
			if (element.isJobInfo())
				return (JobInfo) element;
		}
		return null;
	}

	/**
	 * Create the cancel action for the receiver.
	 */
	private void createCancelAction() {
		cancelAction = new Action(ProgressMessages.ProgressView_CancelAction) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				viewer.cancelSelection();
			}
		};

	}

	/**
	 * Create the clear all action for the receiver.
	 */
	private void createClearAllAction() {
		clearAllAction = new Action(
				ProgressMessages.ProgressView_ClearAllAction) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				viewer.clearAll();
			}
		};
		clearAllAction
				.setToolTipText(ProgressMessages.NewProgressView_RemoveAllJobsToolTip);
		ImageDescriptor id = WorkbenchImages
				.getWorkbenchImageDescriptor("/elcl16/progress_remall.gif"); //$NON-NLS-1$
		if (id != null)
			clearAllAction.setImageDescriptor(id);
		id = WorkbenchImages
				.getWorkbenchImageDescriptor("/dlcl16/progress_remall.gif"); //$NON-NLS-1$
		if (id != null)
			clearAllAction.setDisabledImageDescriptor(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		provider.dispose();
		super.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4289.java