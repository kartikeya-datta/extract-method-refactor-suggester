error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2266.java
text:
```scala
i@@f (child instanceof CoolBar && (((CoolBar)child).getStyle() & SWT.VERTICAL) != 0)

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.themes.ColorUtil;

/**
 * Draws a styled frame around its contained controls.
 * This class is intended to be used to wrap various
 * trim elements that appear in the workbench.
 * 
 *  Currently this class expects a <b>single</b> child
 *  control.
 *  
 * @since 3.3
 *
 */
public class TrimFrame {
	private static int blend = 40;
	
    Canvas canvas = null;
    
    public TrimFrame(Composite parent) {
        createControl(parent);
    }

    private void createControl(Composite parent) {
        dispose();
        canvas = new Canvas(parent, SWT.NONE);
        canvas.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        
        // paint the border
        canvas.addPaintListener(new PaintListener() {
            private void drawLine (GC gc, int x1, int y1, int x2, int y2, boolean flipXY) {
                if (flipXY) {
                    int tmp = x1; 
                    x1 = y1;
                    y1 = tmp;
                    tmp = x2;
                    x2 = y2;
                    y2 = tmp;
                }
                
                 gc.drawLine(x1, y1, x2, y2);
            }
            
            public void paintControl(PaintEvent e) {
                Canvas canvas = (Canvas)e.widget;
                Control child = canvas.getChildren ()[0];
                
                // Are we horizontally or vertically aligned
                boolean flipXY = false;                
                if (child instanceof ToolBar && (((ToolBar)child).getStyle() & SWT.VERTICAL) != 0)
                    flipXY = true;
                else if (child instanceof CoolBar && (((CoolBar)child).getStyle() & SWT.VERTICAL) != 0)
                    flipXY = true;
                
                Rectangle bb = canvas.getBounds();
                int maxX = bb.width-1;
                int maxY = bb.height-1;
                 
                if (flipXY) {
                    int tmp = maxX;
                    maxX = maxY;
                    maxY = tmp;
                }
                
                Color white = e.gc.getDevice ().getSystemColor(SWT.COLOR_WHITE);
                Color shadow = e.gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
                RGB outerRGB = ColorUtil.blend(white.getRGB(), shadow.getRGB(), blend); 
                Color outerColor = new Color(e.gc.getDevice(), outerRGB);
                
                // Draw the 'outer' bits
                e.gc.setForeground(outerColor);
                
                // Top Line and curve
                drawLine(e.gc, 1, 0, maxX-5, 0, flipXY); 
                drawLine(e.gc, maxX-4, 1, maxX-3, 1, flipXY);
                drawLine(e.gc, maxX-2, 2, maxX-2, 2, flipXY); 
                drawLine(e.gc, maxX-1, 3, maxX-1, 4, flipXY);
                
                // Bottom line and curve
                drawLine(e.gc, 1, maxY, maxX-5, maxY, flipXY); 
                drawLine( e.gc, maxX-4, maxY-1, maxX-3, maxY-1, flipXY);
                drawLine(e.gc, maxX-2, maxY-2, maxX-2, maxY-2, flipXY);
                drawLine(e.gc, maxX-1, maxY-3, maxX-1, maxY-4, flipXY);
                
                // Left & Right edges 
                drawLine(e.gc, 0, 1, 0, maxY-1, flipXY); 
                drawLine(e.gc, maxX, 5, maxX, maxY-5, flipXY);
                
                // Dispose the color since we created it...
                outerColor.dispose();
                
                // Draw the 'inner' curve
                e.gc.setForeground (white);

                drawLine(e.gc, 1, 1, maxX-5, 1, flipXY);
                drawLine(e.gc, maxX-4, 2, maxX-3, 2, flipXY);
                drawLine(e.gc, maxX-3, 3, maxX-2, 3, flipXY);
                drawLine( e.gc, maxX-2, 4, maxX-2, 4, flipXY);
                
                drawLine(e.gc, 1, maxY-1, maxX-5, maxY-1, flipXY);
                drawLine(e.gc, maxX-4, maxY-2, maxX-3, maxY-2, flipXY);
                drawLine( e.gc, maxX-3, maxY-3, maxX-2, maxY-3, flipXY);
                drawLine(e.gc, maxX-2, maxY-4, maxX-2, maxY-4, flipXY);
                
                // Left and Right sides
                drawLine(e.gc, 1, 1, 1, maxY-1, flipXY); 
                drawLine(e.gc, maxX-1, 5, maxX-1, maxY-5, flipXY);
            }
        });
        
        // provide a layout that provides enough extra space to
        // draw the border and to place the child conrol in the
        // correct location
        canvas.setLayout(new Layout() {

			protected Point computeSize(Composite composite, int wHint,
                    int hHint, boolean changed) {
                Control[] children = composite.getChildren();
                
                if (children.length == 0)
                    return new Point(0,0);

                Point innerSize = children[0].computeSize(hHint, wHint, changed); 
                innerSize.x += 4;
                innerSize.y += 4;
                
                Control child = children[0];
                if (child instanceof ToolBar && (((ToolBar)child).getStyle() & SWT.VERTICAL) != 0)
                    innerSize.y += 3;
                else
                    innerSize.x += 3;
                
                return innerSize;
            }

            protected void layout(Composite composite, boolean flushCache) { 
                Control[] children = composite.getChildren();
                if (children.length == 0)
                    return;

                children[0].setLocation(2, 2);
            }             
        });
    }

    /**
     * Dispose the frame
     */
    private void dispose() {
        if (canvas != null && !canvas.isDisposed())
            canvas.dispose();
    }
    
    /**
     * @return The border canvas
     */
    public Composite getComposite() {
    	return canvas;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2266.java