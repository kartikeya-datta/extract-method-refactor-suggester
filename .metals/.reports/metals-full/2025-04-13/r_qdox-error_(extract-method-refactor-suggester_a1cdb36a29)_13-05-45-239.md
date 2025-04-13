error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1903.java
text:
```scala
r@@eturn -1;

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.menus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * @since 3.3
 * 
 */
public class MenuAddition extends AdditionBase {

	// Cache sub-additions
	private List additions = new ArrayList();

	private ImageDescriptor imageDesc = null;
	private Image icon = null;

	private IMenuService menuService;

	public MenuAddition(IConfigurationElement element, IMenuService service) {
		super(element);
		menuService = service;
	}

	public void readAdditions(IConfigurationElement addition, int insertionIndex) {
		IConfigurationElement[] items = addition.getChildren();
		for (int i = 0; i < items.length; i++) {
			String itemType = items[i].getName();

			if (IWorkbenchRegistryConstants.TAG_ITEM.equals(itemType)) {
				additions.add(insertionIndex++, new ItemAddition(items[i]));
			} else if (IWorkbenchRegistryConstants.TAG_WIDGET.equals(itemType)) {
				additions.add(insertionIndex++, new WidgetAddition(items[i]));
			} else if (IWorkbenchRegistryConstants.TAG_MENU.equals(itemType)) {
				MenuAddition newCache = new MenuAddition(items[i], menuService);
				newCache.readAdditions(items[i], 0);
				additions.add(insertionIndex++, newCache);
			} else if (IWorkbenchRegistryConstants.TAG_SEPARATOR
					.equals(itemType)) {
				additions
						.add(insertionIndex++, new SeparatorAddition(items[i]));
			}
		}
	}

	public String getMnemonic() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_MNEMONIC);
	}

	public String getLabel() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_LABEL);
	}

	public String getTooltip() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_TOOLTIP);
	}

	public Image getIcon() {
		if (imageDesc == null) {
			String extendingPluginId = element.getDeclaringExtension()
					.getContributor().getName();

			imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(
					extendingPluginId, getIconPath());
		}

		// Stall loading the icon until first access
		if (icon == null && imageDesc != null) {
			icon = imageDesc.createImage(true, null);
		}
		return icon;
	}

	private String getIconPath() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
	}

	public String toString() {
		return getClass().getName()
				+ "(" + getLabel() + ":" + getTooltip() + ") " + getIconPath(); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.menus.AdditionBase#getContributionItem()
	 */
	public IContributionItem getContributionItem() {
		return new MenuManager(getLabel(), getId()) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.MenuManager#getMenuText()
			 */
			public String getMenuText() {
				return getLabel();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.MenuManager#isEnabled()
			 */
			public boolean isEnabled() {
				return true;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.MenuManager#fill(org.eclipse.swt.widgets.Menu,
			 *      int)
			 */
			public void fill(Menu parent, int index) {
				super.fill(parent, index);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.MenuManager#fill(org.eclipse.swt.widgets.Menu,
			 *      int)
			 */
			public void fill(ToolBar parent, int index) {
				super.fill(parent, index);
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.jface.action.MenuManager#isVisible()
			 */
			public boolean isVisible() {
				return visible;
			}
		};
	}

	public void populateMenuManager(ContributionManager mgr) {
		for (Iterator additionIter = additions.iterator(); additionIter
				.hasNext();) {
			AdditionBase addition = (AdditionBase) additionIter.next();

			// Is this a dynamic item?
			if (addition instanceof ItemAddition
					&& ((ItemAddition) addition).isDynamic()) {
				ItemAddition dynamicItem = (ItemAddition) addition;

				// Get the list of contribution items and
				// add then into the menu manager.
				List items = new ArrayList();
				dynamicItem.getFiller().fillItems(items);
				for (Iterator itemIter = items.iterator(); itemIter.hasNext();) {
					IContributionItem item = (IContributionItem) itemIter
							.next();
					mgr.add(item);
				}
			} else {
				// normal item, just get the contribution
				// Should we just change getContributionItem to return
				// a list and move the logic into ItemAddition??
				IContributionItem ci = addition.getContributionItem();
				if (addition.getVisibleWhen() != null) {
					menuService.addContribution(new MenuActivation(ci, addition
							.getVisibleWhen(), menuService));
				}

				// Populate the sub-items of menus
				if (addition instanceof MenuAddition) {
					((MenuAddition) addition)
							.populateMenuManager((MenuManager) ci);
				}

				// Add the item to the manager
				mgr.add(ci);
			}
		}
	}

	/**
	 * @param additionId
	 *            The id of the addition to find
	 * @return the index of the given addition
	 */
	public int indexOf(String additionId) {
		int index = 0;
		for (Iterator iterator = additions.iterator(); iterator.hasNext();) {
			AdditionBase addition = (AdditionBase) iterator.next();
			if (additionId.equals(addition.getId()))
				return index;
			index++;
		}
		return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1903.java