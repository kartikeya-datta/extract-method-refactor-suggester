error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9251.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9251.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9251.java
text:
```scala
i@@f ((start != null) && (end != null)) {

/*
 * @(#)LineConnection.java
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
import java.util.*;
import java.io.*;
import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.util.*;

/**
 * A LineConnection is a standard implementation of the
 * ConnectionFigure interface. The interface is implemented with PolyLineFigure.
 *
 * @see ConnectionFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class LineConnection extends PolyLineFigure implements ConnectionFigure {

	protected Connector    myStartConnector;
	protected Connector    myEndConnector;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 6883731614578414801L;
	private int lineConnectionSerializedDataVersion = 1;

	/**
	 * Constructs a LineConnection. A connection figure has
	 * an arrow decoration at the start and end.
	 */
	public LineConnection() {
		super(4);
		setStartDecoration(new ArrowTip());
		setEndDecoration(new ArrowTip());
	}

	/**
	 * Tests whether a figure can be a connection target.
	 * ConnectionFigures cannot be connected and return false.
	 */
	public boolean canConnect() {
		return false;
	}

	/**
	 * Ensures that a connection is updated if the connection
	 * was moved.
	 */
	protected void basicMoveBy(int dx, int dy) {
		// don't move the start and end point since they are connected
		for (int i = 1; i < fPoints.size()-1; i++) {
			((Point) fPoints.elementAt(i)).translate(dx, dy);
		}

		updateConnection(); // make sure that we are still connected
	}

	/**
	 * Sets the start figure of the connection.
	 */
	public void connectStart(Connector newStartConnector) {
		setStartConnector(newStartConnector);
		startFigure().addFigureChangeListener(this);
	}

	/**
	 * Sets the end figure of the connection.
	 */
	public void connectEnd(Connector newEndConnector) {
		setEndConnector(newEndConnector);
		endFigure().addFigureChangeListener(this);
		handleConnect(startFigure(), endFigure());
	}

	/**
	 * Disconnects the start figure.
	 */
	public void disconnectStart() {
		startFigure().removeFigureChangeListener(this);
		setStartConnector(null);
	}

	/**
	 * Disconnects the end figure.
	 */
	public void disconnectEnd() {
		handleDisconnect(startFigure(), endFigure());
		endFigure().removeFigureChangeListener(this);
		setEndConnector(null);
	}

	/**
	 * Tests whether a connection connects the same figures
	 * as another ConnectionFigure.
	 */
	public boolean connectsSame(ConnectionFigure other) {
		return other.getStartConnector() == getStartConnector()
			&& other.getEndConnector() == getEndConnector();
	}

	/**
	 * Handles the disconnection of a connection.
	 * Override this method to handle this event.
	 */
	protected void handleDisconnect(Figure start, Figure end) {}

	/**
	 * Handles the connection of a connection.
	 * Override this method to handle this event.
	 */
	protected void handleConnect(Figure start, Figure end) {}

	/**
	 * Gets the start figure of the connection.
	 */
	public Figure startFigure() {
		if (getStartConnector() != null) {
			return getStartConnector().owner();
		}
		return null;
	}

	/**
	 * Gets the end figure of the connection.
	 */
	public Figure endFigure() {
		if (getEndConnector() != null) {
			return getEndConnector().owner();
		}
		return null;
	}

	protected void setStartConnector(Connector newStartConnector) {
		myStartConnector = newStartConnector;
	}
	
	/**
	 * Gets the start figure of the connection.
	 */
	public Connector getStartConnector() {
		return myStartConnector;
	}

	protected void setEndConnector(Connector newEndConnector) {
		myEndConnector = newEndConnector;
	}
	
	/**
	 * Gets the end figure of the connection.
	 */
	public Connector getEndConnector() {
		return myEndConnector;
	}

	/**
	 * Tests whether two figures can be connected.
	 */
	public boolean canConnect(Figure start, Figure end) {
		return true;
	}

	/**
	 * Sets the start point.
	 */
	public void startPoint(int x, int y) {
		willChange();
		if (fPoints.size() == 0) {
			fPoints.addElement(new Point(x, y));
		}
		else {
			fPoints.setElementAt(new Point(x, y), 0);
		}
		changed();
	}

	/**
	 * Sets the end point.
	 */
	public void endPoint(int x, int y) {
		willChange();
		if (fPoints.size() < 2) {
			fPoints.addElement(new Point(x, y));
		}
		else {
			fPoints.setElementAt(new Point(x, y), fPoints.size()-1);
		}
		changed();
	}

	/**
	 * Gets the start point.
	 */
	public Point startPoint(){
		Point p = (Point)fPoints.firstElement();
		return new Point(p.x, p.y);
	}

	/**
	 * Gets the end point.
	 */
	public Point endPoint() {
		Point p = (Point)fPoints.lastElement();
		return new Point(p.x, p.y);
	}

	/**
	 * Gets the handles of the figure. It returns the normal
	 * PolyLineHandles but adds ChangeConnectionHandles at the
	 * start and end.
	 */
	public Vector handles() {
		Vector handles = new Vector(fPoints.size());
		handles.addElement(new ChangeConnectionStartHandle(this));
		for (int i = 1; i < fPoints.size()-1; i++) {
			handles.addElement(new PolyLineHandle(this, locator(i), i));
		}
		handles.addElement(new ChangeConnectionEndHandle(this));
		return handles;
	}

	/**
	 * Sets the point and updates the connection.
	 */
	public void setPointAt(Point p, int i) {
		super.setPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Inserts the point and updates the connection.
	 */
	public void insertPointAt(Point p, int i) {
		super.insertPointAt(p, i);
		layoutConnection();
	}

	/**
	 * Removes the point and updates the connection.
	 */
	public void removePointAt(int i) {
		super.removePointAt(i);
		layoutConnection();
	}

	/**
	 * Updates the connection.
	 */
	public void updateConnection() {
		if (getStartConnector() != null) {
			Point start = getStartConnector().findStart(this);

			if(start != null) {
				startPoint(start.x, start.y);
			}
		}
		if (getEndConnector() != null) {
			Point end = getEndConnector().findEnd(this);
	  
			if(end != null) {
				endPoint(end.x, end.y);
			}
		}
	}

	/**
	 * Lays out the connection. This is called when the connection
	 * itself changes. By default the connection is recalculated
	 */
	public void layoutConnection() {
		updateConnection();
	}

	public void figureChanged(FigureChangeEvent e) {
		updateConnection();
	}

	public void figureRemoved(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRequestRemove(new FigureChangeEvent(this));
		}
	}

	public void figureRequestRemove(FigureChangeEvent e) {}
	public void figureInvalidated(FigureChangeEvent e) {}
	public void figureRequestUpdate(FigureChangeEvent e) {}

	public void release() {
		super.release();
		handleDisconnect(startFigure(), endFigure());
		if (getStartConnector() != null) {
			startFigure().removeFigureChangeListener(this);
		}
		if (getEndConnector() != null) {
			endFigure().removeFigureChangeListener(this);
		}
	}

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeStorable(getStartConnector());
		dw.writeStorable(getEndConnector());
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		Connector start = (Connector)dr.readStorable();
		if (start != null) {
			connectStart(start);
		}
		Connector end = (Connector)dr.readStorable();
		if (end != null) {
			connectEnd(end);
		}
		if (start != null && end != null) {
			updateConnection();
		}
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		if (getStartConnector() != null) {
			connectStart(getStartConnector());
		}
		if (getEndConnector() != null) {
			connectEnd(getEndConnector());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9251.java