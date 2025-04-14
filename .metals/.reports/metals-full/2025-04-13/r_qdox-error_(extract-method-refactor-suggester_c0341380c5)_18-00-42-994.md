error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4543.java
text:
```scala
i@@f (!fe.hasNextFigure()) {

/*
 * @(#)RadiusHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.figures;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.util.Geom;
import CH.ifa.draw.util.Undoable;
import CH.ifa.draw.util.UndoableAdapter;
import java.awt.*;

/**
 * A Handle to manipulate the radius of a round corner rectangle.
 *
 * @version <$CURRENT_VERSION$>
 */
class RadiusHandle extends AbstractHandle {

	private static final int OFFSET = 4;

	public RadiusHandle(RoundRectangleFigure owner) {
		super(owner);
	}

	public void invokeStart(int  x, int  y, DrawingView view) {
		setUndoActivity(createUndoActivity(view));
		getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(owner()));
		((RadiusHandle.UndoActivity)getUndoActivity()).
			setOldRadius(((RoundRectangleFigure)owner()).getArc());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		int dx = x-anchorX;
		int dy = y-anchorY;
		RoundRectangleFigure owner = (RoundRectangleFigure)owner();
		Rectangle r = owner.displayBox();
		Point originalRadius = ((RadiusHandle.UndoActivity)getUndoActivity()).getOldRadius();
		int rx = Geom.range(0, r.width, 2*(originalRadius.x/2 + dx));
		int ry = Geom.range(0, r.height, 2*(originalRadius.y/2 + dy));
		owner.setArc(rx, ry);
	}

	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		Point currentRadius = ((RoundRectangleFigure)owner()).getArc();
		Point originalRadius = ((RadiusHandle.UndoActivity)getUndoActivity()).getOldRadius();
		// there has been no change so there is nothing to undo
		if ((currentRadius.x == originalRadius.x) && (currentRadius.y == originalRadius.y)) {
			setUndoActivity(null);
		}
	}

	public Point locate() {
		RoundRectangleFigure owner = (RoundRectangleFigure)owner();
		Point radius = owner.getArc();
		Rectangle r = owner.displayBox();
		return new Point(r.x+radius.x/2+OFFSET, r.y+radius.y/2+OFFSET);
	}

	public void draw(Graphics g) {
		Rectangle r = displayBox();

		g.setColor(Color.yellow);
		g.fillOval(r.x, r.y, r.width, r.height);

		g.setColor(Color.black);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new RadiusHandle.UndoActivity(newView);
	}
	
	public static class UndoActivity extends UndoableAdapter {
		private Point myOldRadius;
		
		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}
		
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			return resetRadius();
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			return resetRadius();
		}

		protected boolean resetRadius() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasMoreElements()) {
				return false;
			}
			RoundRectangleFigure currentFigure = (RoundRectangleFigure)fe.nextFigure();
			Point figureRadius = currentFigure.getArc();
			currentFigure.setArc(getOldRadius().x, getOldRadius().y);
			setOldRadius(figureRadius);
			return true;
		}
		
		protected void setOldRadius(Point newOldRadius) {
			myOldRadius = newOldRadius;
		}

		public Point getOldRadius() {
			return myOldRadius;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4543.java