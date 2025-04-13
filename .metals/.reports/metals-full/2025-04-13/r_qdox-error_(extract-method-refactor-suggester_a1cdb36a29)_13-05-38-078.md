error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/437.java
text:
```scala
private i@@nt fRotation = 0;

/*
 * @(#)TriangleFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.contrib;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.util.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.figures.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;

/**
 * A triangle with same dimensions as its enclosing rectangle,
 * and apex at any of 8 places
 *
 * @author Doug Lea  (dl at gee, Tue Feb 25 17:30:58 1997)
 * @version <$CURRENT_VERSION$>
 */
public  class TriangleFigure extends RectangleFigure {

	static double[] rotations = {
		-Math.PI/2, -Math.PI/4, 
		0.0, Math.PI/4,
		Math.PI/2, Math.PI * 3/4, 
		Math.PI,  -Math.PI * 3/4
	};

	protected int fRotation = 0;

	public TriangleFigure() {
		super(new Point(0,0), new Point(0,0));
	}

	public TriangleFigure(Point origin, Point corner) {
		super(origin, corner);
	}

	public Vector handles() {
		Vector h = super.handles();
		h.addElement(new TriangleRotationHandle(this));
		return h;
	}

	public void rotate(double angle) {
		willChange();
		double dist = Double.MAX_VALUE;
		int best = 0;
		for (int i = 0; i < rotations.length; ++i) {
			double d = Math.abs(angle - rotations[i]);
			if (d < dist) {
				dist = d;
				best = i;
			}
		}
		fRotation = best;
		changed();
	}

	/** Return the polygon describing the triangle **/
	public Polygon getPolygon() {
		Rectangle r = displayBox();
		Polygon p = new Polygon();
		switch (fRotation) {
		case 0:
			p.addPoint(r.x + r.width/2, r.y);
			p.addPoint(r.x + r.width, r.y + r.height);
			p.addPoint(r.x, r.y + r.height);
			break;
		case 1:
			p.addPoint(r.x + r.width, r.y);
			p.addPoint(r.x + r.width, r.y + r.height);
			p.addPoint(r.x, r.y);
			break;
		case 2:
			p.addPoint(r.x + r.width, r.y + r.height/2);
			p.addPoint(r.x, r.y + r.height);
			p.addPoint(r.x, r.y);
			break;
		case 3:
			p.addPoint(r.x + r.width, r.y + r.height);
			p.addPoint(r.x, r.y + r.height);
			p.addPoint(r.x + r.width, r.y);
			break;
		case 4:
			p.addPoint(r.x + r.width/2, r.y + r.height);
			p.addPoint(r.x, r.y);
			p.addPoint(r.x + r.width, r.y);
			break;
		case 5:
			p.addPoint(r.x, r.y + r.height);
			p.addPoint(r.x, r.y);
			p.addPoint(r.x + r.width, r.y + r.height);
			break;
		case 6:
			p.addPoint(r.x, r.y + r.height/2);
			p.addPoint(r.x + r.width, r.y);
			p.addPoint(r.x + r.width, r.y + r.height);
			break;
		case 7:
			p.addPoint(r.x, r.y);
			p.addPoint(r.x + r.width, r.y);
			p.addPoint(r.x, r.y + r.height);
			break;
		}
		return p;
	}


	public void draw(Graphics g) {
		Polygon p = getPolygon();
		g.setColor(getFillColor());
		g.fillPolygon(p);
		g.setColor(getFrameColor());
		g.drawPolygon(p);
	}

	public Insets connectionInsets() {
		Rectangle r = displayBox();
		switch(fRotation) {
		case 0:
			return new Insets(r.height, r.width/2, 0, r.width/2);
		case 1:
			return new Insets(0, r.width, r.height, 0);
		case 2:
			return new Insets(r.height/2, 0, r.height/2, r.width);
		case 3:
			return new Insets(r.height, r.width, 0, 0);
		case 4:
			return new Insets(0, r.width/2, r.height, r.width/2);
		case 5:
			return new Insets(r.height, 0, 0, r.width);
		case 6:
			return new Insets(r.height/2, r.width, r.height/2, 0);
		case 7:
			return new Insets(0, 0, r.height, r.width);
		default:
			return null;
		}
	}

	public boolean containsPoint(int x, int y) {
		return getPolygon().contains(x, y);
	}

	public Point center() {
		return PolygonFigure.center(getPolygon());
	}

	public Point chop(Point p) {
		return PolygonFigure.chop(getPolygon(), p);
	}

	public Object clone() {
		TriangleFigure figure = (TriangleFigure) super.clone();
		figure.fRotation = fRotation;
		return figure;
	}

	public double getRotationAngle() {
		return rotations[fRotation];
	}
	
	//-- store / load ----------------------------------------------

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fRotation);
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		fRotation = dr.readInt();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/437.java