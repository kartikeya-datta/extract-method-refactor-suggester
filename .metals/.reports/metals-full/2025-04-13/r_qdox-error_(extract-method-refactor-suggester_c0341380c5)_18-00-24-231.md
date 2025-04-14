error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3828.java
text:
```scala
public I@@ContributionItem getContributionItem(boolean forMenu) {

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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Wrapper for a ConfigurationElement defining a Menu or Toolbar 'item'
 * addition.
 * 
 * @since 3.3
 * 
 */
public class ItemAddition extends AdditionBase {

	// Icon Support
	private ImageDescriptor imageDesc = null;
	private Image icon = null;

	// Dynamic Item support
	private AbstractDynamicMenuItem filler;

	public ItemAddition(IConfigurationElement element) {
		super(element);
	}

	public String getCommandId() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_COMMAND_ID);
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

	public int getStyle() {
		// TODO: Check the command type to determine the 'style'
		// (Push, Check, Radio)
		return SWT.PUSH;
	}

	public String getIconPath() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_ICON);
	}

	public boolean isVisible() {
		// TODO: evaluate the 'visibleWhen' expression
		return true;
	}

	public boolean isEnabled() {
		// TODO: evaluate the 'enabledWhen' expression
		return true;
	}

	public String getClassSpec() {
		return element.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
	}

	public boolean isDynamic() {
		return getClassSpec() != null && getClassSpec().length() > 0;
	}

	public AbstractDynamicMenuItem getFiller() {
		if (filler == null) {
			filler = loadFiller();
		}
		return filler;
	}

	/**
	 * @return
	 */
	private AbstractDynamicMenuItem loadFiller() {
		if (filler == null) {
			try {
				filler = (AbstractDynamicMenuItem) element
						.createExecutableExtension(IWorkbenchRegistryConstants.ATT_CLASS);
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return filler;
	}

	public String toString() {
		return getClass().getName()
				+ "(" + getLabel() + ":" + getTooltip() + ") " + getIconPath(); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.menus.AdditionBase#getContribution()
	 */
	public IContributionItem getContributionItem() {
		return new ContributionItem(getId()) {

			public void fill(Menu parent, int index) {
				MenuItem newItem = new MenuItem(parent, getStyle(), index);
				newItem.setText(getLabel());

				if (getIconPath() != null)
					newItem.setImage(getIcon());

				newItem.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// Execute through the command service
					}

					public void widgetSelected(SelectionEvent e) {
						// Execute through the command service
					}
				});
			}

			public void fill(ToolBar parent, int index) {
				ToolItem newItem = new ToolItem(parent, getStyle(), index);

				if (getIconPath() != null)
					newItem.setImage(getIcon());
				else if (getLabel() != null)
					newItem.setText(getLabel());

				if (getTooltip() != null)
					newItem.setToolTipText(getTooltip());
				else
					newItem.setToolTipText(getLabel());

				newItem.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent e) {
						// Execute through the command service
					}

					public void widgetSelected(SelectionEvent e) {
						// Execute through the command service
					}
				});
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.ContributionItem#update()
			 */
			public void update() {
				update(null);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.ContributionItem#update(java.lang.String)
			 */
			public void update(String id) {
				if (getParent() != null) {
					getParent().update(true);
				}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3828.java