error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8559.java
text:
```scala
n@@ew NewWizardShortcutAction(window, element);

package org.eclipse.ui.actions;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jface.action.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.*;
import org.eclipse.ui.internal.dialogs.WizardCollectionElement;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.internal.registry.NewWizardsRegistryReader;
import java.util.*;
import java.util.List;

/**
 * A <code>NewWizardMenu</code> is used to populate a menu manager with
 * New Wizard actions.  The visible actions are determined by user preference
 * from the Perspective Customize dialog.
 */
public class NewWizardMenu extends ContributionItem {
	private Action showDlgAction = new NewWizardAction();
	private Action newProjectAction;
	private Action newExampleAction;
	private Map actions = new HashMap(21);
	private NewWizardsRegistryReader reader = new NewWizardsRegistryReader();
	private boolean enabled = true;
	private IWorkbenchWindow window;

	private boolean dirty = true;
	private IMenuListener menuListener = new IMenuListener() {
		public void menuAboutToShow(IMenuManager manager) {
			manager.markDirty();
			dirty = true;
		}
	};
	/**
	 * Create a new wizard shortcut menu.  
	 * <p>
	 * If the menu will appear on a semi-permanent basis, for instance within
	 * a toolbar or menubar, the value passed for <code>register</code> should be true.
	 * If set, the menu will listen to perspective activation and update itself
	 * to suit.  In this case clients are expected to call <code>deregister</code> 
	 * when the menu is no longer needed.  This will unhook any perspective
	 * listeners.
	 * </p>
	 *
	 * @param innerMgr the location for the shortcut menu contents
	 * @param window the window containing the menu
	 * @param register if <code>true</code> the menu listens to perspective changes in
	 * 		the window
	 */
	public NewWizardMenu(
		IMenuManager innerMgr,
		IWorkbenchWindow window,
		boolean register) {
		this(window);
		fillMenu(innerMgr);
		// Must be done after constructor to ensure field initialization.
	}

	public NewWizardMenu(IWorkbenchWindow window) {
		super();
		this.window = window;
		newProjectAction = new NewProjectAction(window);
		newExampleAction = new NewExampleAction(window);
	}
	/* (non-Javadoc)
	 * Fills the menu with New Wizards.
	 */
	private void fillMenu(IContributionManager innerMgr) {
		// Remove all.
		innerMgr.removeAll();

		if (this.enabled) {
			// Add new project ..
			innerMgr.add(newProjectAction);

			// Get visible actions.
			List actions = null;
			IWorkbenchPage page = window.getActivePage();
			if (page != null)
				actions = ((WorkbenchPage) page).getNewWizardActionIds();
			if (actions != null) {
				if(actions.size() > 0)
					innerMgr.add(new Separator());
				for (Iterator i = actions.iterator(); i.hasNext();) {
					String id = (String) i.next();
					IAction action = getAction(id);
					if (action != null)
						innerMgr.add(action);
				}
			}

			if (hasExamples()) {
				//Add examples ..
				innerMgr.add(new Separator());
				innerMgr.add(newExampleAction);
			}

			// Add other ..
			innerMgr.add(new Separator());
			innerMgr.add(showDlgAction);
		}
	}
	/* (non-Javadoc)
	 * Returns the action for the given wizard id, or null if not found.
	 */
	private IAction getAction(String id) {
		// Keep a cache, rather than creating a new action each time,
		// so that image caching in ActionContributionItem works.
		IAction action = (IAction) actions.get(id);
		if (action == null) {
			WorkbenchWizardElement element = reader.findWizard(id);
			if (element != null) {
				action =
					new NewWizardShortcutAction(window.getWorkbench(), element);
				actions.put(id, action);
			}
		}
		return action;
	}
	/* (non-Javadoc)
	 * Method declared on IContributionItem.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/* (non-Javadoc)
	 * Method declared on IContributionItem.
	 */
	public boolean isDynamic() {
		return true;
	}
	/* (non-Javadoc)
	 * Method declared on IContributionItem.
	 */
	public boolean isDirty() {
		return dirty;
	}
	/**
	 * Sets the enabled state of the receiver.
	 * 
	 * @param enabledValue if <code>true</code> the menu is enabled; else
	 * 		it is disabled
	 */
	public void setEnabled(boolean enabledValue) {
		this.enabled = enabledValue;
	}
	/**
	 * Removes all listeners from the containing workbench window.
	 * <p>
	 * This method should only be called if the shortcut menu is created
	 * with <code>register = true</code>.
	 * </p>
	 * 
	 * @deprecated
	 */
	public void deregisterListeners() {
	}
	/* (non-Javadoc)
	 * Method declared on IContributionItem.
	 */
	public void fill(Menu menu, int index) {
		if (getParent() instanceof MenuManager)
			 ((MenuManager) getParent()).addMenuListener(menuListener);

		if (!dirty)
			return;

		MenuManager manager = new MenuManager();
		fillMenu(manager);
		IContributionItem items[] = manager.getItems();
		for (int i = 0; i < items.length; i++) {
			items[i].fill(menu, index++);
		}
		dirty = false;
	}

	/**
	 * Return whether or not any examples are in the current
	 * install.
	 * @return boolean
	 */
	private boolean hasExamples() {
		NewWizardsRegistryReader rdr = new NewWizardsRegistryReader(false);

		Object[] children = rdr.getWizards().getChildren();

		for (int i = 0; i < children.length; i++) {
			WizardCollectionElement currentChild =
				(WizardCollectionElement) children[i];
			if (currentChild
				.getId()
				.equals(NewWizardsRegistryReader.FULL_EXAMPLES_WIZARD_CATEGORY))
				return true;
		}
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8559.java