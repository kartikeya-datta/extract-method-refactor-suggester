error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4907.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4907.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4907.java
text:
```scala
g@@c.drawPolyline(shapeArray);

package org.eclipse.ui.internal;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class OvalComposite extends Composite implements PaintListener {
    
    static final int[] TOP_LEFT_CORNER = new int[] { 0, 2, 1, 1, 2, 0 };
	    
	private int orientation;
    private Color interiorColor;
		
	public OvalComposite(Composite parent, int orientation) {
	    super(parent, SWT.NONE);
	    
	    addPaintListener(this);
	    this.orientation = orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
        redraw();
	}
	
	public void paintControl(PaintEvent e) {
	    GC gc = e.gc;
	    Color color = e.display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	    gc.setForeground(color);
        if (interiorColor != null) {
            gc.setBackground(interiorColor);
        }
	
	    Shape shape = new Shape(TOP_LEFT_CORNER.length + 2);
	    
	    IntAffineMatrix rotation = IntAffineMatrix
	        .getRotation(orientation);
	    
	    rotation = rotation.multiply(IntAffineMatrix.ROT_180);
	    
	    Point size = getSize();

	    if (!Geometry.isHorizontal(orientation)) {
	    	Geometry.flipXY(size);
	    }
	    
	    shape.add(0, size.y);
	    shape.add(new Shape(TOP_LEFT_CORNER));
	    shape.add(IntAffineMatrix.translation(size.x - 3, 0).multiply(IntAffineMatrix.FLIP_YAXIS), 
	    		shape.reverse());

        Point rawSize = getSize();
	    Point adjust = new Point(0,0);
        switch(orientation) {
	        case SWT.TOP: adjust = rawSize; break;
	        case SWT.LEFT: adjust = new Point(rawSize.x - 1, 0); break;
	        case SWT.RIGHT: adjust = new Point(0, rawSize.y - 3); break;
        }
        
	    Shape targetShape = IntAffineMatrix.translation(adjust.x, adjust.y)
        	.multiply(rotation)
        	.transform(shape);
	    
	    int[] shapeArray = targetShape.getData();
        if (interiorColor != null) {
            gc.fillPolygon(shapeArray);
        }
	    gc.drawPolygon(shapeArray);
	}

	public Rectangle getClientArea() {
		Rectangle result = Geometry.copy(super.getClientArea());
		
		if (Geometry.isHorizontal(orientation)) {
			Geometry.expand(result, -6, -6, orientation == SWT.BOTTOM ? -1 : 0, orientation == SWT.TOP ? -1 : 0);
		} else {
			Geometry.expand(result, orientation == SWT.RIGHT ? -1 : 0, orientation == SWT.LEFT ? -1 : 0, -6, -6);
		}
		
		return result;
	}
	
	public Rectangle computeTrim(int x, int y, int width, int height) {
		Rectangle result = Geometry.copy(super.computeTrim(x, y, width, height));
		
        if (Geometry.isHorizontal(orientation)) {
            Geometry.expand(result, 6, 6, orientation == SWT.BOTTOM ? 1 : 0, orientation == SWT.TOP ? 1 : 0);
        } else {
            Geometry.expand(result, orientation == SWT.RIGHT ? 1 : 0, orientation == SWT.LEFT ? 1 : 0, 6, 6);
        }
        		
		return result;
	}

	/**
	 * @return Returns the interiorColor.
	 */
	public Color getInteriorColor() {
		return interiorColor;
	}

	/**
	 * @param interiorColor The interiorColor to set.
	 */
	public void setInteriorColor(Color interiorColor) {
		this.interiorColor = interiorColor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4907.java