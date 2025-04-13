error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9559.java
text:
```scala
i@@f (!fe.hasNextFigure()) {

/*
 * @(#)PolygonScaleHandle.java
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
import CH.ifa.draw.standard.*;
import CH.ifa.draw.util.Geom;
import CH.ifa.draw.util.Undoable;
import CH.ifa.draw.util.UndoableAdapter;
import java.awt.*;

/**
 * A Handle to scale and rotate a PolygonFigure
 * Based on RadiusHandle
 *
 * @author Doug Lea  (dl at gee, Sat Mar 1 09:06:09 1997)
 * @version <$CURRENT_VERSION$>
 */
class PolygonScaleHandle extends AbstractHandle {
	
	private Point fCurrent;
	
	public PolygonScaleHandle(PolygonFigure owner) {
		super(owner);
	}
	
	/**
	 * @param x the x position where the interaction started
	 * @param y the y position where the interaction started
	 * @param view the handles container
	 */
	public void invokeStart(int x, int  y, DrawingView view) {
		fCurrent = new Point(x, y);
		PolygonScaleHandle.UndoActivity activity = (PolygonScaleHandle.UndoActivity)createUndoActivity(view);
		setUndoActivity(activity);
		activity.setAffectedFigures(new SingleFigureEnumerator(owner()));
		activity.setPolygon(((PolygonFigure)(owner())).getPolygon());
	}

	/**
	 * Tracks a step of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 */
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		fCurrent = new Point(x, y);
		Polygon polygon = ((PolygonScaleHandle.UndoActivity)getUndoActivity()).getPolygon();
		((PolygonFigure)(owner())).scaleRotate(new Point(anchorX, anchorY), polygon, fCurrent);
	}

	/**
	 * Tracks the end of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		((PolygonFigure)(owner())).smoothPoints();
		if ((fCurrent.x == anchorX) && (fCurrent.y == anchorY)) {
			// there is nothing to undo
			setUndoActivity(null);
		}
		fCurrent = null;
	}
	
	public Point locate() {
		if (fCurrent == null) {
			return getOrigin();
		}
		else {
			return fCurrent;
		}
	}
	
	Point getOrigin() {
		// find a nice place to put handle
		// Need to pick a place that will not overlap with point handle
		// and is internal to polygon
	
		// Try for one HANDLESIZE step away from outermost toward center
	
		Point outer = ((PolygonFigure)(owner())).outermostPoint();
		Point ctr = ((PolygonFigure)(owner())).center();
		double len = Geom.length(outer.x, outer.y, ctr.x, ctr.y);
		if (len == 0) { // best we can do?
			return new Point(outer.x - HANDLESIZE/2, outer.y + HANDLESIZE/2);
		}
	
		double u = HANDLESIZE / len;
		if (u > 1.0) { // best we can do?
			return new Point((outer.x * 3 + ctr.x)/4, (outer.y * 3 + ctr.y)/4);
		}
		else {
			return new Point((int)(outer.x * (1.0 - u) + ctr.x * u),
			(int)(outer.y * (1.0 - u) + ctr.y * u));
		}
	}
	
	public void draw(Graphics g) {
		Rectangle r = displayBox();
	
		g.setColor(Color.yellow);
		g.fillOval(r.x, r.y, r.width, r.height);
	
		g.setColor(Color.black);
		g.drawOval(r.x, r.y, r.width, r.height);
	
		/* for debugging ...
		Point ctr = ((PolygonFigure)(owner())).center();
		g.setColor(Color.blue);
		g.fillOval(ctr.x, ctr.y, r.width, r.height);
		
		g.setColor(Color.black);
		g.drawOval(ctr.x, ctr.y, r.width, r.height);
		
		*/
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new PolygonScaleHandle.UndoActivity(newView);
	}
	
	public static class UndoActivity extends UndoableAdapter {
		private Polygon myPolygon;
		
		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}
		
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			return resetPolygon();
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			return resetPolygon();
		}

		protected boolean resetPolygon() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasMoreElements()) {
				return false;
			}
			PolygonFigure figure = (PolygonFigure)fe.nextFigure();
			Polygon backupPolygon = figure.getPolygon();
			figure.willChange();
			figure.setInternalPolygon(getPolygon());
			figure.changed();
			setPolygon(backupPolygon);
			return true;
		}
		
		protected void setPolygon(Polygon newPolygon) {
			myPolygon = newPolygon;
		}
		
		public Polygon getPolygon() {
			return myPolygon;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9559.java