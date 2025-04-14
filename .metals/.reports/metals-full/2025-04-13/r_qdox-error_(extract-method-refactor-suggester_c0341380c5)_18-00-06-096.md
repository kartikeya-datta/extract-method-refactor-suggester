error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5116.java
text:
```scala
G@@C gc = new GC(canvas);

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
package org.eclipse.ui.internal.progress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.misc.Assert;

/**
 * The ProgressCanvasViewer is the viewer used by progress windows. It displays text
 * on the canvas.
 */
public class ProgressCanvasViewer extends AbstractProgressViewer {
    Canvas canvas;

    Object[] displayedItems = new Object[0];

    private final static List EMPTY_LIST = new ArrayList();

    /**
     * Font metrics to use for determining pixel sizes.
     */
    private FontMetrics fontMetrics;

    private int numShowItems = 1;

    private int maxCharacterWidth;

    /**
     * Create a new instance of the receiver with the supplied
     * parent and style bits.
     * @param parent The composite the Canvas is created in
     * @param style style bits for the canvas
     * @param itemsToShow the number of items this will show
     * @param numChars The number of characters for the width hint.
     */
    ProgressCanvasViewer(Composite parent, int style, int itemsToShow, int numChars) {
        super();
        numShowItems = itemsToShow;
        maxCharacterWidth = numChars;
        canvas = new Canvas(parent, style);
        hookControl(canvas);
        // Compute and store a font metric
        GC gc = new GC(canvas);
        gc.setFont(JFaceResources.getDefaultFont());
        fontMetrics = gc.getFontMetrics();
        gc.dispose();
        initializeListeners();
    }

    /**
     * NE: Copied from ContentViewer.  We don't want the OpenStrategy hooked
     * in StructuredViewer.hookControl otherwise the canvas will take focus
     * since it has a key listener.  We don't want this included in the window's
     * tab traversal order.  Defeating it here is more self-contained then 
     * setting the tab list on the shell or other parent composite.
     */
    protected void hookControl(Control control) {
        control.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                handleDispose(event);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
     */
    protected Widget doFindInputItem(Object element) {
        return null; // No widgets associated with items
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
     */
    protected Widget doFindItem(Object element) {
        return null; // No widgets associated with items
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget,
     *      java.lang.Object, boolean)
     */
    protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
        canvas.redraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
     */
    protected List getSelectionFromWidget() {
        //No selection on a Canvas
        return EMPTY_LIST;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
     */
    protected void internalRefresh(Object element) {
        displayedItems = getSortedChildren(getRoot());
        canvas.redraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
     */
    public void reveal(Object element) {
        //Nothing to do here as we do not scroll
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List,
     *      boolean)
     */
    protected void setSelectionToWidget(List l, boolean reveal) {
        //Do nothing as there is no selection
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.Viewer#getControl()
     */
    public Control getControl() {
        return canvas;
    }

    private void initializeListeners() {
        canvas.addPaintListener(new PaintListener() {
            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
             */
            public void paintControl(PaintEvent event) {

                GC gc = event.gc;
                ILabelProvider labelProvider = (ILabelProvider) getLabelProvider();

                int itemCount = Math.min(displayedItems.length, numShowItems);

                int yOffset = 0;
                if (numShowItems == 1) {//If there is a single item try to center it
                    Rectangle clientArea = canvas.getParent().getClientArea();
                    int size = clientArea.height;
                    yOffset = size - (fontMetrics.getHeight());
                    yOffset = yOffset / 2;
                }

                for (int i = 0; i < itemCount; i++) {
                    String string = labelProvider.getText(displayedItems[i]);
                    if(string == null)
                    	string = "";//$NON-NLS-1$
                    gc.drawString(string, 2, yOffset
                            + (i * fontMetrics.getHeight()), true);
                }
            }
        });
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ContentViewer#setLabelProvider(org.eclipse.jface.viewers.IBaseLabelProvider)
     */
    public void setLabelProvider(IBaseLabelProvider labelProvider) {
        Assert.isTrue(labelProvider instanceof ILabelProvider);
        super.setLabelProvider(labelProvider);
    }

    /**
     * Get the size hints for the receiver. These are used for 
     * layout data.
     * @return Point - the preferred x and y coordinates
     */
    public Point getSizeHints() {

        Display display = canvas.getDisplay();

        GC gc = new GC(display);
        FontMetrics fm = gc.getFontMetrics();
        int charWidth = fm.getAverageCharWidth();
        int charHeight = fm.getHeight();
        int maxWidth = display.getBounds().width / 2;
        int maxHeight = display.getBounds().height / 6;
        int fontWidth = charWidth * maxCharacterWidth;
        int fontHeight = charHeight * numShowItems;
        if (maxWidth < fontWidth)
            fontWidth = maxWidth;
        if (maxHeight < fontHeight)
            fontHeight = maxHeight;
        gc.dispose();
        return new Point(fontWidth, fontHeight);
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.AbstractProgressViewer#add(java.lang.Object[])
	 */
	public void add(Object[] elements) {
		refresh(true);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.progress.AbstractProgressViewer#remove(java.lang.Object[])
	 */
	public void remove(Object[] elements) {
		refresh(true);
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5116.java