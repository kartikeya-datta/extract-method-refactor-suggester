error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7947.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7947.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7947.java
text:
```scala
i@@f (fillColorId.equals(FigureAttributeConstant.FILL_COLOR.getName())) {

/*
 * @(#)AbstractLineDecoration.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.io.*;
import java.awt.*;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.*;

/**
 * An standard implementation of a line decoration.
 *
 * @see PolyLineFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class AbstractLineDecoration implements LineDecoration {

	static final long serialVersionUID = 1577970039258356627L;

	private Color   fFillColor;
	private Color   fBorderColor;
	private transient Rectangle myBounds;

	public AbstractLineDecoration() {
	}

   /**
	* Draws the arrow tip in the direction specified by the given two
	* points.. (template method)
	*/
	public void draw(Graphics g, int x1, int y1, int x2, int y2) {
		// TBD: reuse the Polygon object
		Polygon p = outline(x1, y1, x2, y2);
		myBounds = p.getBounds();
		if (getFillColor() == null) {
			g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
		}
		else {
			Color drawColor = g.getColor();
			g.setColor(getFillColor());
			g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
			g.setColor(drawColor);
		}

		if (getBorderColor() != getFillColor()) {
			Color drawColor = g.getColor();
			g.setColor(getBorderColor());
			g.drawPolygon(p.xpoints, p.ypoints, p.npoints);
			g.setColor(drawColor);
		}
	}

	/**
	 * The LineDecoration has only a displayBox after it has been drawn
	 * at least once. If it has not yet been drawn then a rectangle of size 0
	 * is returned.
	 * @return the display box of a LineDecoration.
	 */
	public Rectangle displayBox() {
		if (myBounds != null) {
			return myBounds;
		}
		else {
			return new Rectangle(0, 0);
		}
	}

   /**
	* Hook method to calculates the outline of an arrow tip.
	*/
	public abstract Polygon outline(int x1, int y1, int x2, int y2);

	/**
	 * Stores the arrow tip to a StorableOutput.
	 */
	public void write(StorableOutput dw) {
		if (getFillColor() != null) {
			FigureAttributes.writeColor(dw, FigureAttributeConstant.FILL_COLOR.getName(), getFillColor());
		}
		else {
			dw.writeString("no" + FigureAttributeConstant.FILL_COLOR.getName());
		}

		if (getBorderColor() != null) {
			FigureAttributes.writeColor(dw, FigureAttributeConstant.FRAME_COLOR.getName(), getBorderColor());
		}
		else {
			dw.writeString("no" + FigureAttributeConstant.FRAME_COLOR.getName());
		}
	}

	/**
	 * Reads the arrow tip from a StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		String fillColorId = dr.readString();
		// read color only if one has been written
		if (fillColorId.equals(FigureAttributeConstant.FRAME_COLOR.getName())) {
			setFillColor(FigureAttributes.readColor(dr));
		}
		String borderColorId = dr.readString();
		// read color only if one has been written
		if (borderColorId.equals("BorderColor")
  borderColorId.equals(FigureAttributeConstant.FRAME_COLOR.getName())) {
			setBorderColor(FigureAttributes.readColor(dr));
		}
	}

	/**
	 * Sets color with which arrow is filled
	 */
	public void setFillColor(Color fillColor) {
		fFillColor = fillColor;
	}

	/**
	 * Returns color with which arrow is filled
	 */
	public Color getFillColor() {
		return fFillColor;
	}

	/**
	 * Sets color of arrow's border
	 */
	public void setBorderColor(Color borderColor) {
		fBorderColor = borderColor;
	}

	/**
	 * Returns color of arrow's border
	 */
	public Color getBorderColor() {
		return fBorderColor;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7947.java