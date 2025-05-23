error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8137.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8137.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8137.java
text:
```scala
l@@.add(dr.readStorable());

/*
 * @(#)PertFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.samples.pert;

import CH.ifa.draw.framework.*;
import CH.ifa.draw.standard.*;
import CH.ifa.draw.figures.*;
import CH.ifa.draw.util.*;

import java.awt.*;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * @version <$CURRENT_VERSION$>
 */
public class PertFigure extends CompositeFigure {
	private static final int BORDER = 3;
	private Rectangle fDisplayBox;
	private List fPreTasks;
	private List fPostTasks;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -7877776240236946511L;
	private int pertFigureSerializedDataVersion = 1;

	public PertFigure() {
		initialize();
	}

	public int start() {
		int start = 0;
		Iterator iter = fPreTasks.iterator();
		while (iter.hasNext()) {
			PertFigure f = (PertFigure)iter.next();
			start = Math.max(start, f.end());
		}
		return start;
	}

	public int end() {
		return asInt(2);
	}

	public int duration() {
		return asInt(1);
	}

	public void setEnd(int value) {
		setInt(2, value);
	}

	public void addPreTask(PertFigure figure) {
		if (!fPreTasks.contains(figure)) {
			fPreTasks.add(figure);
		}
	}

	public void addPostTask(PertFigure figure) {
		if (!fPostTasks.contains(figure)) {
			fPostTasks.add(figure);
		}
	}

	public void removePreTask(PertFigure figure) {
		fPreTasks.remove(figure);
	}

	public void removePostTask(PertFigure figure) {
		fPostTasks.remove(figure);
	}

	private int asInt(int figureIndex) {
		NumberTextFigure t = (NumberTextFigure)figureAt(figureIndex);
		return t.getValue();
	}

	private String taskName() {
		TextFigure t = (TextFigure)figureAt(0);
		return t.getText();
	}

	private void setInt(int figureIndex, int value) {
		NumberTextFigure t = (NumberTextFigure)figureAt(figureIndex);
		t.setValue(value);
	}

	protected void basicMoveBy(int x, int y) {
		fDisplayBox.translate(x, y);
		super.basicMoveBy(x, y);
	}

	public Rectangle displayBox() {
		return new Rectangle(
			fDisplayBox.x,
			fDisplayBox.y,
			fDisplayBox.width,
			fDisplayBox.height);
	}

	public void basicDisplayBox(Point origin, Point corner) {
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);
		layout();
	}

	private void drawBorder(Graphics g) {
		super.draw(g);

		Rectangle r = displayBox();

		Figure f = figureAt(0);
		Rectangle rf = f.displayBox();
		g.setColor(Color.gray);
		g.drawLine(r.x, r.y+rf.height+2, r.x+r.width, r.y + rf.height+2);
		g.setColor(Color.white);
		g.drawLine(r.x, r.y+rf.height+3, r.x+r.width, r.y + rf.height+3);

		g.setColor(Color.white);
		g.drawLine(r.x, r.y, r.x, r.y + r.height);
		g.drawLine(r.x, r.y, r.x + r.width, r.y);
		g.setColor(Color.gray);
		g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
		g.drawLine(r.x , r.y + r.height, r.x + r.width, r.y + r.height);
	}

	public void draw(Graphics g) {
		drawBorder(g);
		super.draw(g);
	}

	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new NullHandle(this, RelativeLocator.southWest()));
		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		handles.add(new ConnectionHandle(this, RelativeLocator.east(),
								new PertDependency())
						   );
		return new HandleEnumerator(handles);
	}

	private void initialize() {
		fPostTasks = CollectionsFactory.current().createList();
		fPreTasks = CollectionsFactory.current().createList();
		fDisplayBox = new Rectangle(0, 0, 0, 0);

		Font f = new Font("Helvetica", Font.PLAIN, 12);
		Font fb = new Font("Helvetica", Font.BOLD, 12);

		TextFigure name = new TextFigure();
		name.setFont(fb);
		name.setText("Task");
		//name.setAttribute(FigureAttributeConstant.TEXT_COLOR.getName(), Color.white);
		add(name);

		NumberTextFigure duration = new NumberTextFigure();
		duration.setValue(0);
		duration.setFont(fb);
		add(duration);

		NumberTextFigure end = new NumberTextFigure();
		end.setValue(0);
		end.setFont(f);
		end.setReadOnly(true);
		add(end);
	}

	private void layout() {
		Point partOrigin = new Point(fDisplayBox.x, fDisplayBox.y);
		partOrigin.translate(BORDER, BORDER);
		Dimension extent = new Dimension(0, 0);

		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure f = fe.nextFigure();

			Dimension partExtent = f.size();
			Point corner = new Point(
								partOrigin.x+partExtent.width,
								partOrigin.y+partExtent.height);
			f.basicDisplayBox(partOrigin, corner);

			extent.width = Math.max(extent.width, partExtent.width);
			extent.height += partExtent.height;
			partOrigin.y += partExtent.height;
		}
		fDisplayBox.width = extent.width + 2*BORDER;
		fDisplayBox.height = extent.height + 2*BORDER;
	}

	private boolean needsLayout() {
		Dimension extent = new Dimension(0, 0);

		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure f = fe.nextFigure();
			extent.width = Math.max(extent.width, f.size().width);
		}
		int newExtent = extent.width + 2*BORDER;
		return newExtent != fDisplayBox.width;
	}

	public void update(FigureChangeEvent e) {
		if (e.getFigure() == figureAt(1)) {
			// duration has changed
			updateDurations();
		}
		if (needsLayout()) {
			layout();
			changed();
		}
	}

	public void figureChanged(FigureChangeEvent e) {
		update(e);
	}


	public void figureRemoved(FigureChangeEvent e) {
		update(e);
	}

	public void notifyPostTasks() {
		Iterator iter = fPostTasks.iterator();
		while (iter.hasNext()) {
			((PertFigure)iter.next()).updateDurations();
		}
	}

	public void updateDurations() {
		int newEnd = start()+duration();
		if (newEnd != end()) {
			setEnd(newEnd);
			notifyPostTasks();
		}
	}

	public boolean hasCycle(Figure start) {
		if (start == this) {
			return true;
		}
		Iterator iter = fPreTasks.iterator();
		while (iter.hasNext()) {
			if (((PertFigure)iter.next()).hasCycle(start)) {
				return true;
			}
		}
		return false;
	}

	//-- store / load ----------------------------------------------

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fDisplayBox.x);
		dw.writeInt(fDisplayBox.y);
		dw.writeInt(fDisplayBox.width);
		dw.writeInt(fDisplayBox.height);

		writeTasks(dw, fPreTasks);
		writeTasks(dw, fPostTasks);
	}

	public void writeTasks(StorableOutput dw, List l) {
		dw.writeInt(l.size());
		Iterator iter = l.iterator();
		while (iter.hasNext()) {
			dw.writeStorable((Storable)iter.next());
		}
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		fDisplayBox = new Rectangle(
			dr.readInt(),
			dr.readInt(),
			dr.readInt(),
			dr.readInt());
		layout();
		fPreTasks = readTasks(dr);
		fPostTasks = readTasks(dr);
	}

	public Insets connectionInsets() {
		Rectangle r = fDisplayBox;
		int cx = r.width/2;
		int cy = r.height/2;
		return new Insets(cy, cx, cy, cx);
	}

	public List readTasks(StorableInput dr) throws IOException {
		int size = dr.readInt();
		List l = CollectionsFactory.current().createList(size);
		for (int i=0; i<size; i++) {
			l.add((Figure)dr.readStorable());
		}
		return l;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8137.java