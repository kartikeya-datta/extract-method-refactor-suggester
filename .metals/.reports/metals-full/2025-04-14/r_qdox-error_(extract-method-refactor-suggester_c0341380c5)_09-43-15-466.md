error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7342.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7342.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7342.java
text:
```scala
r@@eturn Geometry.toDisplay(control.getParent(), bounds);

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.presentations.defaultpresentation;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.layout.SizeCache;
import org.eclipse.ui.internal.presentations.util.AbstractTabFolder;
import org.eclipse.ui.internal.presentations.util.AbstractTabItem;
import org.eclipse.ui.internal.presentations.util.PartInfo;
import org.eclipse.ui.internal.presentations.util.ProxyControl;
import org.eclipse.ui.internal.presentations.util.StandardSystemToolbar;
import org.eclipse.ui.internal.presentations.util.TabFolderEvent;
import org.eclipse.ui.internal.util.Util;

/**
 * @since 3.1
 */
public class NativeTabFolder extends AbstractTabFolder {

    private TabFolder control;
    private ViewForm viewForm;
    private StandardSystemToolbar systemToolbar;
    private CLabel title;
    private ProxyControl topCenter;
    private SizeCache topCenterCache;
    
    private static final String FULL_TITLE = "part_title"; //$NON-NLS-1$
    
    private Listener selectionListener = new Listener() {
        public void handleEvent(Event e) {
            fireEvent(TabFolderEvent.EVENT_TAB_SELECTED, getTab(e.item));
        }
    };
    
    private IPropertyListener systemToolbarListener = new IPropertyListener() {

        public void propertyChanged(Object source, int propId) {
            Point location;
            
            if (propId == TabFolderEvent.EVENT_PANE_MENU) {
                location = getPaneMenuLocation();
            } else {
                location = new Point(0,0);
            }
            
            fireEvent(propId, getSelection(), location);
        }
        
    };

    public NativeTabFolder(Composite parent) {
        control = new TabFolder(parent, SWT.BOTTOM);
        control.addListener(SWT.Selection, selectionListener);
        attachListeners(control, false);
        
        viewForm = new ViewForm(control, SWT.FLAT);
        attachListeners(viewForm, false);
        systemToolbar = new StandardSystemToolbar(viewForm, true, false, true, true, true);
        systemToolbar.addListener(systemToolbarListener);
        viewForm.setTopRight(systemToolbar.getControl());
        
        topCenter = new ProxyControl(viewForm);
        topCenterCache = new SizeCache();
        
        title = new CLabel(viewForm, SWT.LEFT);
        attachListeners(title, false);
        viewForm.setTopLeft(title);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#computeSize(int, int)
     */
    public Point computeSize(int widthHint, int heightHint) {
        return new Point(50, 50);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#add(int)
     */
    public AbstractTabItem add(int index, int flags) {
        NativeTabItem item = new NativeTabItem(this, index);
        item.getWidget().setData(item);
        
        return item;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#layout(boolean)
     */
    public void layout(boolean flushCache) {
        super.layout(flushCache);
        
        Rectangle oldBounds = viewForm.getBounds();
        Rectangle newBounds = control.getClientArea();
        
        viewForm.setBounds(newBounds);
        
        if (Util.equals(oldBounds, newBounds)) {
            viewForm.layout(flushCache);
        }
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getPaneMenuLocation()
     */
    public Point getPaneMenuLocation() {
        return systemToolbar.getPaneMenuLocation();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setState(int)
     */
    public void setState(int state) {
        super.setState(state);
        
        systemToolbar.setState(state);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getClientArea()
     */
    public Rectangle getClientArea() {
        Control content = viewForm.getContent();
        
        if (content == null) {
            return new Rectangle(0,0,0,0);
        }
        
        return Geometry.toControl(control, DragUtil.getDisplayBounds(content));
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getItems()
     */
    public AbstractTabItem[] getItems() {
        TabItem[] items = control.getItems();
        
        AbstractTabItem[] result = new AbstractTabItem[items.length];
        
        for (int i = 0; i < result.length; i++) {
            result[i] = getTab(items[i]);
        }
        
        return result;
    }
    
    /**
     * @param item
     * @return
     * @since 3.1
     */
    private AbstractTabItem getTab(Widget item) {
        return (AbstractTabItem)item.getData();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setSelection(org.eclipse.ui.internal.presentations.util.Widget)
     */
    public void setSelection(AbstractTabItem toSelect) {
        if (toSelect == null) {
            return;
        }
        
        NativeTabItem tab = (NativeTabItem) toSelect;
        control.setSelection(new TabItem[] {(TabItem)tab.getWidget()});
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setSelectedInfo(org.eclipse.ui.internal.presentations.util.PartInfo)
     */
    public void setSelectedInfo(PartInfo info) {
        if (!Util.equals(title.getText(), info.title)) {
            title.setText(info.title);
        }
        if (title.getImage() != info.image) {
            title.setImage(info.image);
        }
    }
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getToolbarParent()
     */
    public Composite getToolbarParent() {
        return viewForm;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getTabArea()
     */
    public Rectangle getTabArea() {

        Rectangle bounds = control.getBounds();
        
        Rectangle clientArea = control.getClientArea();
        
        bounds.x = 0;
        bounds.y = 0;
        Geometry.expand(bounds, 0, 0, - (clientArea.height + clientArea.y), 0);
        
        return bounds;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setToolbar(org.eclipse.swt.widgets.Control)
     */
    public void setToolbar(Control toolbarControl) {
        
        if (toolbarControl != null) { 
            topCenterCache.setControl(toolbarControl);
            topCenter.setTarget(topCenterCache);
            viewForm.setTopCenter(topCenter.getControl());
        } else {
            topCenterCache.setControl(null);
            topCenter.setTarget(null);
            viewForm.setTopCenter(null);
        }
        
        super.setToolbar(toolbarControl);
    }
    
    public Control getControl() {
        return control;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#isOnBorder(org.eclipse.swt.graphics.Point)
     */
    public boolean isOnBorder(Point globalPos) {
        Point localPos = getControl().toControl(globalPos);
        
        Rectangle clientArea = getClientArea();
        return localPos.y > clientArea.y && localPos.y < clientArea.y + clientArea.height; 
    }
    
    public AbstractTabItem getSelection() {
        TabItem[] sel = control.getSelection();
        
        if (sel.length == 0) {
            return null;
        }

        return getTab(sel[0]);
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getContentParent()
     */
    public Composite getContentParent() {
        return viewForm;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#setContent(org.eclipse.swt.widgets.Control)
     */
    public void setContent(Control newContent) {
        viewForm.setContent(newContent);
    }
    
    /**
     * @return
     * @since 3.1
     */
    public TabFolder getTabFolder() {
        return control;
    }

    /**
     * @param item
     * @param newTitle
     * @since 3.1
     */
    /* protected */ void setSelectedTitle(String newTitle) {
        title.setText(newTitle);
    }

    /**
     * @param image
     * @since 3.1
     */
    /* protected */ void setSelectedImage(Image image) {
        title.setImage(image);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.presentations.util.AbstractTabFolder#getItem(org.eclipse.swt.graphics.Point)
     */
    public AbstractTabItem getItem(Point toFind) {
        return getSelection();
    }
    
    /**
     * @param enabled
     * @since 3.1
     */
    public void enablePaneMenu(boolean enabled) {
        systemToolbar.enablePaneMenu(enabled);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7342.java