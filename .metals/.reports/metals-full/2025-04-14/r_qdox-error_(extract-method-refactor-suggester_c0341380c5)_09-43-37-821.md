error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9923.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9923.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9923.java
text:
```scala
v@@iewIds.retainAll(objectManager.getEnabledObjects());

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

package org.eclipse.ui.internal;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.activities.IObjectActivityManager;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.internal.dialogs.ShowViewDialog;
import org.eclipse.ui.internal.registry.IViewDescriptor;
import org.eclipse.ui.internal.registry.IViewRegistry;

/**
 * A <code>ShowViewMenu</code> is used to populate a menu manager with
 * Show View actions.  The visible views are determined by user preference
 * from the Perspective Customize dialog. 
 */
public class ShowViewMenu extends ContributionItem {
	
	private IWorkbenchWindow window;
		
	private Comparator actionComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			if(collator == null)
		 		collator = Collator.getInstance();		
			IAction a1 = (IAction) o1;
			IAction a2 = (IAction) o2;
			return collator.compare(a1.getText(), a2.getText());
		}
	};

	private Action showDlgAction =
		new Action(WorkbenchMessages.getString("ShowView.title")) {//$NON-NLS-1$
			public void run() {
				showOther();
			}
		};

	private Map actions = new HashMap(21);

	//Maps pages to a list of opened views
	private Map openedViews = new HashMap();
	
	private boolean dirty = true;
	private IMenuListener menuListener = new IMenuListener() {
		public void menuAboutToShow(IMenuManager manager) {
			manager.markDirty();
			dirty = true;
		}
	};
	
	private static Collator collator;	

	/**
	 * Creates a Show View menu.
	 * 
	 * @param window the window containing the menu
	 */
	public ShowViewMenu(IWorkbenchWindow window, String id) {
		super(id);
		this.window = window;
		WorkbenchHelp.setHelp(showDlgAction, IHelpContextIds.SHOW_VIEW_OTHER_ACTION);
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
	
	/**
	 * Fills the menu with Show View actions.
	 */
	private void fillMenu(IMenuManager innerMgr) {
		// Remove all.
		innerMgr.removeAll();

		// If no page disable all.
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;

		// If no active perspective disable all
		if (page.getPerspective() == null)
			return;

		// Get visible actions. (copy, we're going to be modifying it)
		List viewIds = new ArrayList(((WorkbenchPage) page).getShowViewActionIds());
        
        IObjectActivityManager objectManager = window.getWorkbench().getObjectActivityManager(IWorkbenchConstants.PL_VIEWS, false);
        if (objectManager != null) {
            // prune off all filtered views
            viewIds.retainAll(objectManager.getActiveObjects());
        }
        // add all open views
        viewIds = addOpenedViews(page, viewIds);

		List actions = new ArrayList(viewIds.size());
		for (Iterator i = viewIds.iterator(); i.hasNext();) {
			String id = (String) i.next();
			IAction action = getAction(id);
			if (action != null) {
				actions.add(action);
			}
		}
		Collections.sort(actions, actionComparator);
		for (Iterator i = actions.iterator(); i.hasNext();) {
			innerMgr.add((IAction) i.next());
		}

		// Add Other ..
		innerMgr.add(new Separator());
		innerMgr.add(showDlgAction);
	}

	private List addOpenedViews(IWorkbenchPage page, List actions) {
		ArrayList views = getParts(page);
		ArrayList result = new ArrayList(views.size() + actions.size());

		for (int i = 0; i < actions.size(); i++) {
			Object element = actions.get(i);
			if (result.indexOf(element) < 0)
				result.add(element);
		}
		for (int i = 0; i < views.size(); i++) {
			Object element = views.get(i);
			if (result.indexOf(element) < 0)
				result.add(element);
		}
		return result;
	}
	/**
	 * Returns the action for the given view id, or null if not found.
	 */
	private IAction getAction(String id) {
		// Keep a cache, rather than creating a new action each time,
		// so that image caching in ActionContributionItem works.
		IAction action = (IAction) actions.get(id);
		if (action == null) {
			IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
			IViewDescriptor desc = reg.find(id);
			if (desc != null) {
				action = new ShowViewAction(window, desc);
				actions.put(id, action);
			}
		}
		return action;
	}
	
	/**
	 * Opens the view selection dialog.
	 */
	private void showOther() {
		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return;
		ShowViewDialog dlg =
			new ShowViewDialog(
				window.getShell(),
				WorkbenchPlugin.getDefault().getViewRegistry());
		dlg.open();
		if (dlg.getReturnCode() == Window.CANCEL)
			return;
		IViewDescriptor[] descs = dlg.getSelection();
		for (int i = 0; i < descs.length; ++i) {
			try {
				page.showView(descs[i].getID());
			} catch (PartInitException e) {
				ErrorDialog
					.openError(
						window.getShell(),
						WorkbenchMessages.getString("ShowView.errorTitle"), //$NON-NLS-1$
						e.getMessage(),
						e.getStatus());
			}
		}
	}

	private ArrayList getParts(IWorkbenchPage page) {
		ArrayList parts = (ArrayList) openedViews.get(page);
		if (parts == null) {
			parts = new ArrayList();
			openedViews.put(page, parts);
		}
		return parts;
	}
	
public void fill(Menu menu, int index) {
	if(getParent() instanceof MenuManager)
		((MenuManager)getParent()).addMenuListener(menuListener);

	if(!dirty)
		return;

	MenuManager manager = new MenuManager();
	fillMenu(manager);
	IContributionItem items[] = manager.getItems();
	for (int i = 0; i < items.length; i++) {
		items[i].fill(menu,index++);
	}
	dirty = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9923.java