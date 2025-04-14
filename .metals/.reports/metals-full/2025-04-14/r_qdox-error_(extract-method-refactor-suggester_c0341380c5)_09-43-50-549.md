error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1763.java
text:
```scala
s@@uper.figureInvalidated(new FigureChangeEvent(this, rect, e));

/*
 * @(#)BorderDecorator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.figures;

import java.awt.*;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;

/**
 * BorderDecorator decorates an arbitrary Figure with
 * a border.
 *
 * @version <$CURRENT_VERSION$>
 */
public  class BorderDecorator extends DecoratorFigure {

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 1205601808259084917L;
	private int borderDecoratorSerializedDataVersion = 1;

	private Point myBorderOffset;
	private Color myBorderColor;
	private Color myShadowColor;

	public BorderDecorator() {
	}

	public BorderDecorator(Figure figure) {
		super(figure);
	}

	/**
	 * Performs additional initialization code before the figure is decorated
	 * Subclasses may override this method.
	 */
	protected void initialize() {
		setBorderOffset(new Point(3,3));
	}

	public void setBorderOffset(Point newBorderOffset) {
		myBorderOffset = newBorderOffset;
	}
		
	public Point getBorderOffset() {
		if (myBorderOffset == null) {
			return new Point(0,0);
		}
		else {
			return myBorderOffset;
		}
	}

	/**
	 * Draws a the figure and decorates it with a border.
	 */
	public void draw(Graphics g) {
		Rectangle r = displayBox();
		super.draw(g);
		g.setColor(Color.white);
		g.drawLine(r.x, r.y, r.x, r.y + r.height);
		g.drawLine(r.x, r.y, r.x + r.width, r.y);
		g.setColor(Color.gray);
		g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
		g.drawLine(r.x , r.y + r.height, r.x + r.width, r.y + r.height);
	}

	/**
	 * Gets the displaybox including the border.
	 */
	public Rectangle displayBox() {
		Rectangle r = getDecoratedFigure().displayBox();
		r.grow(getBorderOffset().x, getBorderOffset().y);
		return r;
	}

	/**
	 * Invalidates the figure extended by its border.
	 */
	public void figureInvalidated(FigureChangeEvent e) {
		Rectangle rect = e.getInvalidatedRectangle();
		rect.grow(getBorderOffset().x, getBorderOffset().y);
		super.figureInvalidated(new FigureChangeEvent(e.getFigure(), rect));
	}

	public Insets connectionInsets() {
		Insets i = super.connectionInsets();
		i.top -= getBorderOffset().y;
		i.bottom -= getBorderOffset().y;
		i.left -= getBorderOffset().x;
		i.right -= getBorderOffset().x;

		return i;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1763.java