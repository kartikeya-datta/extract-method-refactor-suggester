error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5961.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5961.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5961.java
text:
```scala
W@@orkbenchPlugin.log("Reference item " + refId + " not found for action " + item.getId()); //$NON-NLS-1$ //$NON-NLS-2$

package org.eclipse.ui.internal;

/************************************************************************
Copyright (c) 2000, 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.AbstractGroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * This builder reads the actions for an action set from the registry.
 */
public class PluginActionSetBuilder extends PluginActionBuilder {
	public static final String TAG_ACTION_SET = "actionSet"; //$NON-NLS-1$
	// As of 2.1, the "pulldown" attribute is deprecated, use "style" attribute instead.
	public static final String ATT_PULLDOWN = "pulldown"; //$NON-NLS-1$

	private PluginActionSet actionSet;
	private IWorkbenchWindow window;
	
	/**
	 * Constructs a new builder.
	 */
	public PluginActionSetBuilder() {
	}
	
	/* (non-Javadoc)
	 * Method declared on PluginActionBuilder.
	 */
	protected ActionDescriptor createActionDescriptor(IConfigurationElement element) {
		// As of 2.1, the "pulldown" attribute was deprecated and replaced by
		// the attribute "style". See doc for more details.
		boolean pullDownStyle = false;
		String style = element.getAttribute(ActionDescriptor.ATT_STYLE);
		if (style != null) {
			pullDownStyle = style.equals(ActionDescriptor.STYLE_PULLDOWN);
		} else {
			String pulldown = element.getAttribute(ATT_PULLDOWN);
			pullDownStyle = pulldown != null && pulldown.equals("true"); //$NON-NLS-1$
		}

		ActionDescriptor desc = null;
		if (pullDownStyle)
			desc = new ActionDescriptor(element, ActionDescriptor.T_WORKBENCH_PULLDOWN, window);
		else
			desc = new ActionDescriptor(element, ActionDescriptor.T_WORKBENCH, window);
		WWinPluginAction action = (WWinPluginAction) desc.getAction();
		action.setActionSetId(actionSet.getDesc().getId());
		actionSet.addPluginAction(action);
		return desc;
	}
	
	/* (non-Javadoc)
	 * Method declared on PluginActionBuilder.
	 */
	protected BasicContribution createContribution() {
		return new ActionSetContribution(actionSet.getDesc().getId());
	}

	/**
	 * Returns the insertion point for a new contribution item.  Clients should
	 * use this item as a reference point for insertAfter.
	 *
	 * @param startId the reference id for insertion
	 * @param sortId the sorting id for the insertion.  If null then the item
	 *		will be inserted at the end of all action sets.
	 * @param mgr the target menu manager.
	 * @param startVsEnd if <code>true</code> the items are added at the start of
	 *		action with the same id; else they are added to the end
	 * @return the insertion point, or null if not found.
	 */
	public static IContributionItem findInsertionPoint(String startId, String sortId, IContributionManager mgr, boolean startVsEnd) {
		// Get items.
		IContributionItem[] items = mgr.getItems();

		// Find the reference item.
		int insertIndex = 0;
		while (insertIndex < items.length) {
			if (startId.equals(items[insertIndex].getId()))
				break;
			++insertIndex;
		}
		if (insertIndex >= items.length)
			return null;

		// Calculate startVsEnd comparison value.
		int compareMetric = 0;
		if (startVsEnd)
			compareMetric = 1;

		// Find the insertion point for the new item.
		// We do this by iterating through all of the previous
		// action set contributions define within the current group.
		for (int nX = insertIndex + 1; nX < items.length; nX++) {
			IContributionItem item = items[nX];
			if (item.isSeparator() || item.isGroupMarker()) {
				// Fix for bug report 18357
				break;
			}
			if (item instanceof IActionSetContributionItem) {
				if (sortId != null) {
					String testId = ((IActionSetContributionItem) item).getActionSetId();
					if (sortId.compareTo(testId) < compareMetric)
						break;
				}
				insertIndex = nX;
			} else {
				break;
			}
		}
		// Return item.
		return items[insertIndex];
	}
	
	public static IContributionItem findSubInsertionPoint(String startId, String sortId, CoolBarManager mgr, boolean startVsEnd) {
		// Get items.
		IContributionItem[] items = mgr.getItems();

		// Find the reference item.
		int insertIndex = 0;
		while (insertIndex < items.length) {
			if (startId.equals(items[insertIndex].getId()))
				break;
			++insertIndex;
		}
		// look at each the items in each of the CoolBarContribution items
		if (insertIndex >= items.length) {
			insertIndex = 0;
			while (insertIndex < items.length) {
				CoolBarContributionItem item = (CoolBarContributionItem) items[insertIndex];
				IContributionItem foundItem = item.getToolBarManager().find(startId);
				if (foundItem != null)
					break;
				++insertIndex;
			}
		}
		if (insertIndex >= items.length)
			return null;

		// Calculate startVsEnd comparison value.
		int compareMetric = 0;
		if (startVsEnd)
			compareMetric = 1;

		// Find the insertion point for the new item.  We do this by iterating 
		// through all of the previous action set contributions.  This code 
		// assumes action set contributions are done in alphabetical order.
		for (int nX = insertIndex + 1; nX < items.length; nX++) {
			CoolBarContributionItem item = (CoolBarContributionItem) items[nX];
			if (item.getItems().length == 0)
				break;
			IContributionItem subItem = item.getItems()[0];
			if (subItem instanceof IActionSetContributionItem) {
				if (sortId != null) {
					String testId = ((IActionSetContributionItem) subItem).getActionSetId();
					if (sortId.compareTo(testId) < compareMetric)
						break;
				}
				insertIndex = nX;
			} else {
				break;
			}
		}
		// Return item.
		return items[insertIndex];
	}
	
	/**
	 * Read the actions within a config element.
	 */
	public void readActionExtensions(PluginActionSet set, IWorkbenchWindow window, IActionBars bars) {
		this.actionSet = set;
		this.window = window;
		cache = null;
		currentContribution = null;
		targetID = null;
		targetContributionTag = TAG_ACTION_SET;
		
		readElements(new IConfigurationElement[] {set.getConfigElement()});
		
		if (cache != null) {
			contribute(bars.getMenuManager(), bars.getToolBarManager(), true);
		} else {
			WorkbenchPlugin.log("Action Set is empty: " + set.getDesc().getId()); //$NON-NLS-1$
		}
	}
	

	/**
	 * Helper class to collect the menus and actions defined within a
	 * contribution element.
	 */
	private static class ActionSetContribution extends BasicContribution {
		private String actionSetId;
		
		public ActionSetContribution(String id) {
			super();
			actionSetId = id;
		}
		
		/**
		 * This implementation inserts the group into the action set additions group.  
		 */
		protected void addGroup(IContributionManager mgr, String name) {
			// Find the insertion point for this group.
			if (mgr instanceof CoolItemToolBarManager) {
				// In the coolbar case we need to create a CoolBarContributionItem
				// for the group if one does not already exist.
				CoolItemToolBarManager tBarMgr = (CoolItemToolBarManager) mgr;
				CoolBarManager cBarMgr = tBarMgr.getParentManager();
				IContributionItem cbItem = cBarMgr.find(actionSetId);
				if (cbItem == null) {
					IContributionItem refItem = findSubInsertionPoint(IWorkbenchActionConstants.MB_ADDITIONS, actionSetId, cBarMgr, true);
					// Add the CoolBarContributionItem to the CoolBarManager for this group.
					if (refItem == null) {
						cBarMgr.add(tBarMgr.getCoolBarItem());
					} else {
						cBarMgr.insertAfter(refItem.getId(), tBarMgr.getCoolBarItem());
					}
				}
				// Insert the group marker into the group, not the CoolBarmanager.
				ActionSetSeparator group = new ActionSetSeparator(name, actionSetId);
				tBarMgr.add(group);
			} else {
				IContributionItem refItem = findInsertionPoint(IWorkbenchActionConstants.MB_ADDITIONS, actionSetId, mgr, true);
				// Insert the new group marker.
				ActionSetSeparator group = new ActionSetSeparator(name, actionSetId);
				if (refItem == null) {
					mgr.add(group);
				} else {
					mgr.insertAfter(refItem.getId(), group);
				}
			}
		}
	
		/* (non-Javadoc)
		 * Method declared on Basic Contribution.
		 */
		protected void insertMenuGroup(IMenuManager menu, AbstractGroupMarker marker) {
			if (actionSetId != null) {
				IContributionItem[] items = menu.getItems();
				// Loop thru all the current groups looking for the first
				// group whose id > than the current action set id. Insert
				// current marker just before this item then.
				for (int i = 0; i < items.length; i++) {
					IContributionItem item = items[i];
					if (item.isSeparator() || item.isGroupMarker()) {
						if (item instanceof IActionSetContributionItem) {
							String testId = ((IActionSetContributionItem) item).getActionSetId();
							if (actionSetId.compareTo(testId) < 0) {
								menu.insertBefore(items[i].getId(), marker);
								return;
							}
						}
					}
				}
			}

			menu.add(marker);
		}

		/* (non-Javadoc)
		 * Method declared on Basic Contribution.
		 */
		protected void insertAfter(IContributionManager mgr, String refId, IContributionItem item) {
			IContributionItem refItem = findInsertionPoint(refId, actionSetId, mgr, true);
			if (refItem != null) {
				mgr.insertAfter(refItem.getId(), item);
			} else {
				WorkbenchPlugin.log("Reference item " + refId + " not found for action " + item.getId()); //$NON-NLS-1$
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5961.java