error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2298.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2298.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2298.java
text:
```scala
w@@hile (popup != null && popup.isVisible()) {

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
package org.eclipse.ui.internal;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.internal.layout.LayoutUtil;

public class PerspectiveBarManager extends ToolBarManager {

    /**
     * The symbolic font name for the small font (value <code>"org.eclipse.jface.smallfont"</code>).
     */
    public static final String SMALL_FONT = "org.eclipse.ui.smallFont"; //$NON-NLS-1$

    public PerspectiveBarManager(int style) {
        super(style);
    }

    public ToolBar createControl(Composite parent) {
        ToolBar control = super.createControl(parent);

        if (control != null && !control.isDisposed())
                control.setFont(getFont());

        return control;
    }

    public PerspectiveBarManager(ToolBar toolbar) {
        super(toolbar);

        if (toolbar != null && !toolbar.isDisposed())
                toolbar.setFont(getFont());
    }

    /* (non-Javadoc)
     * Method declared on IContributionManager.
     */
    public void add(IContributionItem item) {
    	if (item instanceof PerspectiveBarNewContributionItem)
    		super.add(item);
    	else {
            if (allowItem(item)) {
            	item.setParent(this);
            	// perspective bar always has a PerspectiveBarNewContributionItem first, so insert after it.
            	insert(1, item);
            	itemAdded(item);
            }
        }
    }
    
    // TODO begin refactor this out? it is not good that we know we are inside a CoolBar
    private CoolBar coolBar;
    private Menu popup;
    
	public void handleChevron(SelectionEvent event) {
		/*
		 * If the popup menu is already there, then pop it down. This doesn't
		 * work... still need to figure this out.
		 */
		if (popup != null) {
			popup.dispose();
			popup = null;
			return;
		}
		CoolItem item = (CoolItem) event.widget;
		//ToolBar toolbar = (ToolBar)getControl();
		Control control = getControl();
		if (!(control instanceof ToolBar))
			return; // currently we only deal with toolbar items
		/* Retrieve the current bounding rectangle for the selected cool item. */
		Rectangle itemBounds = item.getBounds();
		/* Convert to display coordinates (i.e. was relative to CoolBar). */
		Point pt = coolBar.toDisplay(new Point(itemBounds.x, itemBounds.y));
		itemBounds.x = pt.x;
		itemBounds.y = pt.y;
		/* Retrieve the total number of buttons in the toolbar. */
		ToolBar toolBar = (ToolBar) control;
		ToolItem[] tools = toolBar.getItems();
		int toolCount = tools.length;
		int i = 0;
		while (i < toolCount) {
			/*
			 * Starting from the leftmost tool, retrieve the tool's bounding
			 * rectangle.
			 */
			Rectangle toolBounds = tools[i].getBounds();
			/* Convert to display coordinates (i.e. was relative to ToolBar). */
			pt = toolBar.toDisplay(new Point(toolBounds.x, toolBounds.y));
			toolBounds.x = pt.x;
			toolBounds.y = pt.y;
			/*
			 * Figure out the visible portion of the tool by looking at the
			 * intersection of the tool bounds with the cool item bounds.
			 */
			Rectangle intersection = itemBounds.intersection(toolBounds);
			/*
			 * If the tool is not completely within the cool item bounds, then
			 * the tool is at least partially hidden, and all remaining tools
			 * are completely hidden.
			 */
			if (!intersection.equals(toolBounds))
				break;
			i++;
		}
		/* Create a pop-up menu with items for each of the hidden buttons. */
		popup = new Menu(coolBar);
		
		for (int j = i; j < toolCount; j++) {
			ToolItem tool = tools[j];
			MenuItem menuItem = new MenuItem(popup, SWT.NONE);
			menuItem.setText(tool.getText());
			menuItem.setImage(tool.getImage());
			
			menuItem.setData("IContributionItem", tool.getData()); //$NON-NLS-1$

			menuItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					//rotate the selected item in and the other items right
					// don't touch the "Open" item
					MenuItem menuItem = (MenuItem)e.widget;
					PerspectiveBarContributionItem contribItem = (PerspectiveBarContributionItem)menuItem.getData("IContributionItem"); //$NON-NLS-1$
					PerspectiveBarContributionItem newItem = new PerspectiveBarContributionItem(contribItem.getPerspective(), contribItem.getPage());
					remove(contribItem);
					contribItem.dispose();
					insert(1, newItem);
					update(false);
					newItem.select();
					
/*					ToolItem[] items = toolBar.getItems();
					String text = items[index].getText();
					Image image = items[index].getImage();
					items[index].dispose();
					ToolItem ti = new ToolItem(toolBar, SWT.NONE, 1);					
					ti.setText(text);
					ti.setImage(image);*/
				}
			});
		}
		/*
		 * Display the pop-up menu immediately below the chevron, with the left
		 * edges aligned. Need to convert the given point to display
		 * coordinates in order to pass them to Menu.setLocation (i.e. was
		 * relative to CoolBar).
		 */
		pt = coolBar.toDisplay(new Point(event.x, event.y));
		popup.setLocation(pt.x, pt.y);
		popup.setVisible(true);
		Display display = coolBar.getDisplay();
		while (popup.isVisible()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if (popup != null) {
			popup.dispose();
			popup = null;
		}
	}

    /* (non-Javadoc)
	 * @see org.eclipse.jface.action.ToolBarManager#update(boolean)
	 */
	public void update(boolean force) {
		// TODO Auto-generated method stub
		super.update(force);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.ToolBarManager#relayout(org.eclipse.swt.widgets.ToolBar, int, int)
	 */
	protected void relayout(ToolBar toolBar, int oldCount, int newCount) {
		super.relayout(toolBar, oldCount, newCount);
		
		updateCoolBar();
		
    	if (coolBar != null) {
    		LayoutUtil.resize(coolBar);
    	}
	}
	
	private void updateCoolBar() {
		if (coolBar != null && !coolBar.isDisposed()) {
	    	// this is not ideal to be reaching in and setting the coolitem size
	    	// based on it's only child.
	    	if (coolBar.getItemCount() > 0) {
	    		CoolItem item = coolBar.getItem(0);
	    		Point s = item.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT);
	    		item.setSize(item.computeSize (s.x, s.y));
	    	}
		}
    }

    void setParent(CoolBar cool) {
        this.coolBar = cool;
    }

    // TODO end refactor this out?

    private Font getFont() {
        return JFaceResources.getFont(SMALL_FONT);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2298.java