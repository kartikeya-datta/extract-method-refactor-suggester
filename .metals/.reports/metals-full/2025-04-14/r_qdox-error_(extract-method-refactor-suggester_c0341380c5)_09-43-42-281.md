error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1078.java
text:
```scala
private static final S@@tring NO_TARGETS_MSG = WorkbenchMessages.getString("Workbench.showInNoTargets"); //$NON-NLS-1$

/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.registry.IViewDescriptor;
import org.eclipse.ui.internal.registry.IViewRegistry;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.IShowInTargetList;
import org.eclipse.ui.part.ShowInContext;

/**
 * A <code>ShowInMenu</code> is used to populate a menu manager with
 * Show In actions.  The items to show are determined from the active perspective
 * and active part. 
 */
public class ShowInMenu extends ContributionItem {
	
private static final String NO_TARGETS_MSG = WorkbenchMessages.getString("Workbench.showInNoTargets");
 
private IWorkbenchWindow window;
	
private Map actions = new HashMap(21);

private boolean dirty = true;
private IMenuListener menuListener = new IMenuListener() {
	public void menuAboutToShow(IMenuManager manager) {
		manager.markDirty();
		dirty = true;
	}
};

/**
 * Creates a Show In menu.
 *
 * @param window the window containing the menu
 */
public ShowInMenu(IWorkbenchWindow window) {
	this.window = window;
}

protected IWorkbenchWindow getWindow() {
	return window;
}

public boolean isDirty() {
	return dirty;
}	
/**
 * Overridden to always return true and force dynamic menu building.
 */
public boolean isDynamic() {
	return true;
}

public void fill(Menu menu, int index) {
	if (getParent() instanceof MenuManager)
		((MenuManager)getParent()).addMenuListener(menuListener);

	if (!dirty)
		return;

	MenuManager manager = new MenuManager();
	fillMenu(manager);
	IContributionItem[] items = manager.getItems();
	if (items.length == 0) {
		MenuItem item = new MenuItem(menu, SWT.NONE, index++);
		item.setText(NO_TARGETS_MSG);
		item.setEnabled(false);
	}
	else {
		for (int i = 0; i < items.length; i++) {
			items[i].fill(menu,index++);
		}
	}
	dirty = false;
}

/**
 * Fills the menu with Show In actions.
 */
private void fillMenu(IMenuManager innerMgr) {
	// Remove all.
	innerMgr.removeAll();

	IWorkbenchPart sourcePart = getSourcePart();
	if (sourcePart == null) {
		return;
	}
	ShowInContext context = getContext(sourcePart);
	if (context == null) {
		return;
	}
	if (context.getInput() == null && (context.getSelection() == null || context.getSelection().isEmpty())) {
		return;
	}
	
	IViewDescriptor[] viewDescs = getViewDescriptors(sourcePart);
	if (viewDescs.length == 0) {
		return;
	}

	for (int i = 0; i < viewDescs.length; ++i) {
		IAction action = getAction(viewDescs[i]);
		if (action != null) {
			innerMgr.add(action);
		}
	}
}

/**
 * Returns the action for the given view id, or null if not found.
 */
private IAction getAction(IViewDescriptor desc) {
	// Keep a cache, rather than creating a new action each time,
	// so that image caching in ActionContributionItem works.
	IAction action = (IAction) actions.get(desc.getId());
	if (action == null) {
		if (desc != null) {
			action = new ShowInAction(window, desc);
			actions.put(desc.getId(), action);
		}
	}
	return action;
}

/**
 * Returns the Show In... target part ids for the given source part.  
 * Merges the contributions from the current perspective and the source part.
 */
private ArrayList getShowInPartIds(IWorkbenchPart sourcePart) {
	ArrayList targetIds = new ArrayList();
	WorkbenchPage page = (WorkbenchPage) getWindow().getActivePage();
	if (page != null) {
		targetIds.addAll(page.getShowInPartIds());
	}
	IShowInTargetList targetList = getShowInTargetList(sourcePart);
	if (targetList != null) {
		String[] partIds = targetList.getShowInTargetIds();
		if (partIds != null) {
			for (int i = 0; i < partIds.length; ++i) {
				if (!targetIds.contains(partIds[i])) {
					targetIds.add(partIds[i]);
				}
			}
		}
	}
	return targetIds;
}
	
/**
 * Returns the source part, or <code>null</code> if there is no applicable
 * source part
 * <p>
 * This implementation returns the current part in the window.
 * Subclasses may extend or reimplement.
 * 
 * @return the source part or <code>null</code>
 */
private IWorkbenchPart getSourcePart() {
	IWorkbenchPage page = getWindow().getActivePage();
	if (page != null) {
		return page.getActivePart();
	}
	return null;
}
	
/**
 * Returns the <code>IShowInSource</code> provided by the source part,
 * or <code>null</code> if it does not provide one.
 * 
 * @param sourcePart the source part
 * @return an <code>IShowInSource</code> or <code>null</code>
 */
private IShowInSource getShowInSource(IWorkbenchPart sourcePart) {
	if (sourcePart instanceof IShowInSource) {
		return (IShowInSource) sourcePart;
	}
	Object o = sourcePart.getAdapter(IShowInSource.class);
	if (o instanceof IShowInSource) {
		return (IShowInSource) o;
	}
	return null;
}

/**
 * Returns the <code>IShowInTargetList</code> for the given source part,
 * or <code>null</code> if it does not provide one.
 * 
 * @param sourcePart the source part
 * @return the <code>IShowInTargetList</code> or <code>null</code>
 */
private IShowInTargetList getShowInTargetList(IWorkbenchPart sourcePart) {
	if (sourcePart instanceof IShowInTargetList) {
		return (IShowInTargetList) sourcePart;
	}
	Object o = sourcePart.getAdapter(IShowInTargetList.class);
	if (o instanceof IShowInTargetList) {
		return (IShowInTargetList) o;
	}
	return null;
}

/**
 * Returns the <code>ShowInContext</code> to show in the selected target,
 * or <code>null</code> if there is no valid context to show.
 * <p>
 * This implementation obtains the context from the <code>IShowInSource</code>
 * of the source part (if provided), or, if the source part is an editor,
 * it creates the context from the editor's input and selection.
 * <p>
 * Subclasses may extend or reimplement.
 * 
 * @return the <code>ShowInContext</code> to show or <code>null</code>
 */
private ShowInContext getContext(IWorkbenchPart sourcePart) {
	IShowInSource source = getShowInSource(sourcePart);
	if (source != null) {
		ShowInContext context = source.getShowInContext();
		if (context != null) {
			return context;
		}
	}
	else if (sourcePart instanceof IEditorPart) {
		Object input = ((IEditorPart) sourcePart).getEditorInput();
		ISelectionProvider sp = sourcePart.getSite().getSelectionProvider();
		ISelection sel = sp == null ? null : sp.getSelection();
		return new ShowInContext(input, sel);
	}
	return null;
}
	
/**
 * Returns the view descriptors to show in the dialog.
 */
private IViewDescriptor[] getViewDescriptors(IWorkbenchPart sourcePart) {
	String srcId = sourcePart.getSite().getId();
	ArrayList ids = getShowInPartIds(sourcePart);
	ArrayList descs = new ArrayList();
	IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
	for (Iterator i = ids.iterator(); i.hasNext();) {
		String id = (String) i.next();
		if (!id.equals(srcId)) {
			IViewDescriptor desc = reg.find(id);
			if (desc != null) {
				descs.add(desc);
			}
		}
	}
	return (IViewDescriptor[]) descs.toArray(new IViewDescriptor[descs.size()]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1078.java