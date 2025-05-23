error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8698.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8698.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8698.java
text:
```scala
r@@eturn Geometry.toDisplay(paneFolder.getControl(), paneFolder.getTitleArea());

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.presentations.defaultpresentation;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.presentations.PaneFolder;
import org.eclipse.ui.internal.presentations.PaneFolderButtonListener;
import org.eclipse.ui.internal.presentations.util.AbstractTabFolder;
import org.eclipse.ui.internal.presentations.util.AbstractTabItem;
import org.eclipse.ui.internal.presentations.util.PartInfo;
import org.eclipse.ui.internal.presentations.util.TabFolderEvent;
import org.eclipse.ui.internal.util.Util;

/**
 * @since 3.1
 */
public class DefaultTabFolder extends AbstractTabFolder {

    private PaneFolder paneFolder;
    private Control viewToolBar;
    private Label titleLabel;
    
    private PaneFolderButtonListener buttonListener = new PaneFolderButtonListener() {
        public void stateButtonPressed(int buttonId) {
            fireEvent(TabFolderEvent.stackStateToEventId(buttonId));
        }

        /**
         * Called when a close button is pressed.
         *   
         * @param item the tab whose close button was pressed
         */
        public void closeButtonPressed(CTabItem item) {
            fireEvent(TabFolderEvent.EVENT_CLOSE, getTab(item));
        }
        /**
         * 
         * @since 3.0
         */
        public void showList(CTabFolderEvent event) {
            event.doit = false;
            fireEvent(TabFolderEvent.EVENT_SHOW_LIST);
        }
    };
    
    private Listener selectionListener = new Listener() {
        public void handleEvent(Event e) {
            AbstractTabItem item = getTab((CTabItem) e.item);

            if (item != null) {
                fireEvent(TabFolderEvent.EVENT_TAB_SELECTED, item);
            }
        }
    };
    
    private static DefaultTabFolderColors defaultColors = new DefaultTabFolderColors();
    
    private DefaultTabFolderColors[] activeShellColors = {defaultColors, defaultColors, defaultColors};
    private DefaultTabFolderColors[] inactiveShellColors = {defaultColors, defaultColors, defaultColors};
    private boolean shellActive = false;
    
    public DefaultTabFolder(Composite parent, int flags, boolean allowMin, boolean allowMax) {
        paneFolder = new PaneFolder(parent, flags | SWT.NO_BACKGROUND);
        paneFolder.addButtonListener(buttonListener);
        paneFolder.setMinimizeVisible(allowMin);
        paneFolder.setMaximizeVisible(allowMax);
        paneFolder.getControl().addListener(SWT.Selection, selectionListener);
        paneFolder.setTopRight(null);
        
        // Initialize view menu dropdown
        {            
            ToolBar actualToolBar = new ToolBar(paneFolder.getControl(), SWT.FLAT | SWT.NO_BACKGROUND);
            viewToolBar = actualToolBar;
            
	        ToolItem pullDownButton = new ToolItem(actualToolBar, SWT.PUSH);
	        Image hoverImage = WorkbenchImages
	                .getImage(IWorkbenchGraphicConstants.IMG_LCL_RENDERED_VIEW_MENU);
	        pullDownButton.setDisabledImage(hoverImage);
	        pullDownButton.setImage(hoverImage);
	        pullDownButton.setToolTipText(WorkbenchMessages.Menu); 
            actualToolBar.addMouseListener(new MouseAdapter() {
                public void mouseDown(MouseEvent e) {
                    fireEvent(TabFolderEvent.EVENT_PANE_MENU, getSelection(), getPaneMenuLocation());
                }
            });
        }
        
        // Initialize content description label
        {
	        titleLabel = new Label(paneFolder.getControl(), SWT.NONE);
	        titleLabel.moveAbove(null);
	        titleLabel.setVisible(false);
            attachListeners(titleLabel, false);
        }
        
        attachListeners(paneFolder.getControl(), false);
        attachListeners(paneFolder.getViewForm(), false);
        
        paneFolder.setTabHeight(computeTabHeight());
        
        viewToolBar.moveAbove(null);
    }

    /**
     * Changes the minimum number of characters to display in the pane folder
     * tab. This control how much information will be displayed to the user.
     * 
     * @param count
     *            The number of characters to display in the tab folder; this
     *            value should be a positive integer.
     * @see org.eclipse.swt.custom.CTabFolder#setMinimumCharacters(int)
     * @since 3.1
     */
    public void setMinimumCharacters(int count) {
        paneFolder.setMinimumCharacters(count);
    }
    
    public void setSimpleTabs(boolean simple) {
        paneFolder.setSimpleTab(simple);
    }
    
    /**
     * @param item
     * @return
     * @since 3.1
     */
    protected DefaultTabItem getTab(CTabItem item) {
        return (DefaultTabItem)item.getData();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#computeSize(int, int)
     */
    public Point computeSize(int widthHint, int heightHint) {
        return paneFolder.computeMinimumSize();
    }

    /* package */ PaneFolder getFolder() {
        return paneFolder;
    }
    
    public AbstractTabItem getSelection() {
        return getTab(paneFolder.getSelection());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#add(int)
     */
    public AbstractTabItem add(int index, int flags) {
        DefaultTabItem result = new DefaultTabItem((CTabFolder)getFolder().getControl(), index, flags);
        
        result.getWidget().setData(result);
        
        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getContentParent()
     */
    public Composite getContentParent() {
        return paneFolder.getContentParent();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setContent(org.eclipse.swt.widgets.Control)
     */
    public void setContent(Control newContent) {
        paneFolder.setContent(newContent);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getItems()
     */
    public AbstractTabItem[] getItems() {
        CTabItem[] items = paneFolder.getItems();
        
        AbstractTabItem[] result = new AbstractTabItem[items.length];
        
        for (int i = 0; i < result.length; i++) {
            result[i] = getTab(items[i]);
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getItemCount()
     */
    public int getItemCount() {
        // Override retrieving all the items when we just want the count.
        return paneFolder.getItemCount();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setSelection(org.eclipse.ui.internal.presentations.util.AbstractTabItem)
     */
    public void setSelection(AbstractTabItem toSelect) {
        paneFolder.setSelection(indexOf(toSelect));
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getToolbarParent()
     */
    public Composite getToolbarParent() {
        return paneFolder.getControl();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getControl()
     */
    public Control getControl() {
        return paneFolder.getControl();
    }
    
    public void setUnselectedCloseVisible(boolean visible) {
        paneFolder.setUnselectedCloseVisible(visible);
    }

    public void setUnselectedImageVisible(boolean visible) {
        paneFolder.setUnselectedImageVisible(visible);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getTabArea()
     */
    public Rectangle getTabArea() {
        return Geometry.toDisplay(paneFolder.getControl().getParent(), paneFolder.getTitleArea());
    }

    /**
     * @param enabled
     * @since 3.1
     */
    public void enablePaneMenu(boolean enabled) {
        if (enabled) {
            paneFolder.setTopRight(viewToolBar);
            viewToolBar.setVisible(true);
        } else {
            paneFolder.setTopRight(null);
            viewToolBar.setVisible(false);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setSelectedInfo(org.eclipse.ui.internal.presentations.util.PartInfo)
     */
    public void setSelectedInfo(PartInfo info) {
        String newTitle = DefaultTabItem.escapeAmpersands(info.contentDescription);
        
        if (!Util.equals(titleLabel.getText(), newTitle)) {
            titleLabel.setText(newTitle);
        }
    	
        if (!info.contentDescription.equals(Util.ZERO_LENGTH_STRING)) {
            paneFolder.setTopLeft(titleLabel);
            titleLabel.setVisible(true);
        } else {
            paneFolder.setTopLeft(null);
            titleLabel.setVisible(false);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getPaneMenuLocation()
     */
    public Point getPaneMenuLocation() {
        Point toolbarSize = viewToolBar.getSize();
        
        return viewToolBar.toDisplay(0,toolbarSize.y);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getPartListLocation()
     */
    public Point getPartListLocation() {
        return paneFolder.getControl().toDisplay(paneFolder.getChevronLocation());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getSystemMenuLocation()
     */
    public Point getSystemMenuLocation() {
        Rectangle bounds = DragUtil.getDisplayBounds(paneFolder.getControl());
		
		int idx = paneFolder.getSelectionIndex();
		if (idx > -1) {
		    CTabItem item = paneFolder.getItem(idx);
		    Rectangle itemBounds = item.getBounds();
		
		    bounds.x += itemBounds.x;
		    bounds.y += itemBounds.y;
		}
		
		Point location = new Point(bounds.x, bounds.y
		        + paneFolder.getTabHeight());
		
		return location;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#isOnBorder(org.eclipse.swt.graphics.Point)
     */
    public boolean isOnBorder(Point toTest) {
        Control content = paneFolder.getContent();
        if (content != null) {
            Rectangle displayBounds = DragUtil.getDisplayBounds(content);
            
            if (paneFolder.getTabPosition() == SWT.TOP) {
                return toTest.y >= displayBounds.y;
            }
            
            if (toTest.y >= displayBounds.y && toTest.y < displayBounds.y + displayBounds.height) {
                return true;
            }
        }
        
        return super.isOnBorder(toTest);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#layout(boolean)
     */
    public void layout(boolean flushCache) {
        paneFolder.layout(flushCache);
        super.layout(flushCache);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setState(int)
     */
    public void setState(int state) {
        paneFolder.setState(state);
        super.setState(state);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setActive(int)
     */
    public void setActive(int activeState) {
        super.setActive(activeState);
        updateColors();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setTabPosition(int)
     */
    public void setTabPosition(int tabPosition) {
        paneFolder.setTabPosition(tabPosition);
        super.setTabPosition(tabPosition);
        layout(true);
    }
    
    public void flushToolbarSize() {
        paneFolder.flushTopCenterSize();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setToolbar(org.eclipse.swt.widgets.Control)
     */
    public void setToolbar(Control toolbarControl) {
        paneFolder.setTopCenter(toolbarControl);
        super.setToolbar(toolbarControl);
    }
    
    public void setColors(DefaultTabFolderColors colors, int activationState, boolean shellActivationState) {
        Assert.isTrue(activationState < activeShellColors.length);
                
        if (shellActivationState) {
            activeShellColors[activationState] = colors;
        } else {
            inactiveShellColors[activationState] = colors;
        }
        
        if (activationState == getActive() && shellActive == shellActivationState) {
            updateColors();
        }
    }
    
    /**
     * 
     * @since 3.1
     */
    private void updateColors() {
        DefaultTabFolderColors currentColors = shellActive ? 
                activeShellColors[getActive()] 
                : inactiveShellColors[getActive()];
                
        paneFolder.setSelectionForeground(currentColors.foreground);
        paneFolder.setSelectionBackground(currentColors.background, currentColors.percentages, currentColors.vertical);
    }

    public void setColors(DefaultTabFolderColors colors, int activationState) {
        setColors(colors, activationState, true);
        setColors(colors, activationState, false);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#shellActive(boolean)
     */
    public void shellActive(boolean isActive) {
        this.shellActive = isActive;
        super.shellActive(isActive);
        
        updateColors();
    }

    /**
     * @param font
     * @since 3.1
     */
    public void setFont(Font font) {
        if (font != paneFolder.getControl().getFont()) {
            paneFolder.getControl().setFont(font);
            layout(true);
            paneFolder.setTabHeight(computeTabHeight());
        }
    }
    
    /**
     * @return the required tab height for this folder.
     */
    protected int computeTabHeight() {
        GC gc = new GC(getControl());

        // Compute the tab height
        int tabHeight = Math.max(viewToolBar.computeSize(SWT.DEFAULT,
                SWT.DEFAULT).y, gc.getFontMetrics().getHeight());

        gc.dispose();

        return tabHeight;
    }

    /**
     * @param b
     * @since 3.1
     */
    public void setSingleTab(boolean b) {
        paneFolder.setSingleTab(b);
        AbstractTabItem[] items = getItems();
        
        for (int i = 0; i < items.length; i++) {
            DefaultTabItem item = (DefaultTabItem)items[i];
        
            item.updateTabText();
        }
        
        layout(true);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8698.java