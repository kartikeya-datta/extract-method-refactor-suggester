error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10574.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10574.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10574.java
text:
```scala
private static final S@@tring COLLABORATION_PROJECTS_ARE_NOT_AVAILABLE_ = "No collaboration sessions joined.\n\nTo join a project-specific collaboration session, select a project in either the Navigator or Package Explorer view,\nright-click to open context menu for project, choose ECF menu, and choose 'Communications->Connect Project to Collaboration Group...'.";

/****************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.example.collab.ui;

import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

public class LineChatView extends ViewPart {

	public static final String VIEW_ID = "org.eclipse.ecf.internal.example.collab.ui.LineChatView"; //$NON-NLS-1$
	// The single view
	private static final String COLLABORATION_PROJECTS_ARE_NOT_AVAILABLE_ = "No resource collaboration sessions joined.\n\nTo join a resource collaboration, select a resource in either the Navigator or Package Explorer view,\nright-click to open context menu for resource, choose ECF menu, and choose 'Connect Project...'.";
	static protected LineChatView singleton = null;

	static protected Hashtable clientViews = new Hashtable();

	TabFolder tabFolder = null;

	Composite parentComposite = null;

	Label inactiveLabel = null;

	public static boolean isDisposed() {
		return (singleton == null);
	}

	public LineChatView() {
	}

	protected Object addClientView(LineChatClientView cv, TabItem ti) {
		synchronized (clientViews) {
			return clientViews.put(cv, ti);
		}
	}

	protected void removeClientView(LineChatClientView cv) {
		final TabItem ti = (TabItem) clientViews.remove(cv);

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if (ti != null)
					ti.dispose();

				if (clientViews.isEmpty()) {
					if (singleton != null) {
						if (singleton.tabFolder != null) {
							singleton.tabFolder.dispose();
							singleton.tabFolder = null;
						}

						createInactiveComposite(singleton.parentComposite);
						actionBars.getToolBarManager().removeAll();
						actionBars.getMenuManager().removeAll();
						actionBars.updateActionBars();
						if (!singleton.parentComposite.isDisposed()) {
							singleton.parentComposite.layout();
						}
					}
				}
			}
		});

	}

	protected void hideView() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					IWorkbenchPage wp = LineChatView.this.getSite().getPage();
					wp.hideView(LineChatView.this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setViewName(String name) {
		if (singleton != null)
			singleton.setPartName(name);
	}

	public static LineChatClientView createClientView(
			final LineChatHandler lch, final String name,
			final String initText, String downloaddir) throws Exception {
		LineChatClientView newView = null;
		synchronized (clientViews) {
			if (singleton == null)
				throw new InstantiationException("View not initialized");

			if (singleton.inactiveLabel != null) {
				singleton.inactiveLabel.dispose();
			}

			if (singleton.tabFolder == null) {
				singleton.tabFolder = new TabFolder(singleton.parentComposite,
						SWT.NORMAL);
			}

			newView = new LineChatClientView(lch, singleton, name, initText,
					downloaddir);
			TabItem ti = new TabItem(singleton.tabFolder, SWT.NULL);
			ti.setControl(newView.getTeamChat());
			ti.setText(newView.name);
			singleton.addClientView(newView, ti);
			actionBars.updateActionBars();
			singleton.parentComposite.layout();
		}
		return newView;
	}

	public void setFocus() {
		synchronized (clientViews) {
			singleton.parentComposite.setFocus();
		}
	}

	static IToolBarManager toolbarManager = null;
	static IActionBars actionBars = null;

	protected static void addActionToToolbar(Action action) {
		toolbarManager.add(action);
		actionBars.updateActionBars();
	}

	protected static void removeActionFromToolbar(Action action) {
		if (action == null)
			return;
		toolbarManager.remove(action.getId());
		actionBars.updateActionBars();
	}

	public void createPartControl(Composite parent) {
		singleton = this;
		IViewSite viewSite = this.getViewSite();
		actionBars = viewSite.getActionBars();
		toolbarManager = actionBars.getToolBarManager();
		parentComposite = parent;
		createInactiveComposite(parent);
	}

	private void createInactiveComposite(Composite parent) {
		if (!parent.isDisposed()) {
			inactiveLabel = new Label(parent, SWT.NONE);
			inactiveLabel.setText(COLLABORATION_PROJECTS_ARE_NOT_AVAILABLE_);
		}
	}

	protected void disposeClient(LineChatClientView lccv) {
		if (singleton != null)
			singleton.removeClientView(lccv);
	}

	public void dispose() {
		synchronized (clientViews) {
			closeAllClients();
		}
		singleton = null;
		super.dispose();
	}

	public void saveState(IMemento state) {
		// We can save state here, associated with all UI config for collab
		// views
		// For now, we'll just use it to close the window
	}

	protected void closeAllClients() {
		for (Enumeration e = clientViews.keys(); e.hasMoreElements();) {
			LineChatClientView vc = (LineChatClientView) e.nextElement();
			vc.closeClient();
		}
	}

	protected void setActiveTab(String name) {
		if (name == null)
			return;
		if (tabFolder != null) {
			TabItem[] items = tabFolder.getItems();
			if (items == null)
				return;
			for (int i = 0; i < items.length; i++) {
				String itemName = items[i].getText();
				if (name.equals(itemName)) {
					tabFolder.setSelection(i);
				}
			}
		}
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10574.java