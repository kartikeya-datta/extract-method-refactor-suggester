error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6355.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6355.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6355.java
text:
```scala
public v@@oid clearData() {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jmeter.visualizers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import org.apache.jmeter.samplers.Clearable;

/**
 * MonitorGraph will draw the performance history of a given server. It displays
 * 4 lines:
 * <p>
 */
public class MonitorGraph extends JComponent implements MouseListener, MonitorGuiListener, Clearable {
	// NOTUSED protected static int width = 500;
	private MonitorAccumModel MODEL;

	private MonitorModel CURRENT;

	private boolean CPU = false;// TODO is this needed? It's never read

	private boolean HEALTH = true;

	private boolean LOAD = true;

	private boolean MEM = true;

	private boolean THREAD = true;

	private boolean YGRID = true;

	private boolean XGRID = true;

	private int GRAPHMAX = 0;// TODO is this needed? It's never read

	/**
	 * 
	 * @deprecated Only for use in unit testing
	 */
	public MonitorGraph() {
		// log.warn("Only for use in unit testing");
	}

	/**
	 * 
	 */
	public MonitorGraph(MonitorAccumModel model) {
		this.MODEL = model;
		GRAPHMAX = model.getBufferSize();
		init();
	}

	private void init() {
		repaint();
	}

	public void setCpu(boolean cpu) {
		this.CPU = cpu;
	}

	public void setHealth(boolean health) {
		this.HEALTH = health;
	}

	public void setLoad(boolean load) {
		this.LOAD = load;
	}

	public void setMem(boolean mem) {
		this.MEM = mem;
	}

	public void setThread(boolean thread) {
		this.THREAD = thread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * The method will fist check to see if the graph is visible. If it is, it
	 * will repaint the graph.
	 */
	public void updateGui(final MonitorModel model) {
		if (this.isShowing()) {
			this.CURRENT = model;
			repaint();
		}
	}

	/**
	 * painComponent is responsible for drawing the actual graph. This is
	 * because of how screen works. Tried to use clipping, but I don't
	 * understand it well enough to get the desired effect.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.CURRENT != null) {
			synchronized (MODEL) {
				List samples = MODEL.getAllSamples(this.CURRENT.getURL());
				int size = samples.size();
				synchronized (samples) {
					Iterator e;
					if (size > getWidth()) {
						e = samples.listIterator(size - getWidth());
					} else {
						e = samples.iterator();
					}
					MonitorModel last = null;
					for (int i = 0; e.hasNext(); i++) {
						MonitorModel s = (MonitorModel) e.next();
						if (last == null) {
							last = s;
						}
						drawSample(i, s, g, last);
						last = s;
					}
				}
			}
		}
	}

	/**
	 * updateGui() will call repaint
	 */
	public void updateGui() {
		repaint();
	}

	/**
	 * clear will repaint the graph
	 */
	public void clear() {
		paintComponent(getGraphics());
		this.repaint();
	}

	private void drawSample(int x, MonitorModel model, Graphics g, MonitorModel last) {
		double width = getWidth();
		double height = getHeight() - 10.0;
		int xaxis = (int) (width * (x / width));
		int lastx = (int) (width * ((x - 1) / width));

		// draw grid only when x is 1. If we didn't
		// the grid line would draw over the data
		// lines making it look bad.
		if (YGRID && x == 1) {
			int q1 = (int) (height * 0.25);
			int q2 = (int) (height * 0.50);
			int q3 = (int) (height * 0.75);
			g.setColor(Color.lightGray);
			g.drawLine(0, q1, getWidth(), q1);
			g.drawLine(0, q2, getWidth(), q2);
			g.drawLine(0, q3, getWidth(), q3);
		}
		if (XGRID && x == 1) {
			int x1 = (int) (width * 0.25);
			int x2 = (int) (width * 0.50);
			int x3 = (int) (width * 0.75);
			g.drawLine(x1, 0, x1, getHeight());
			g.drawLine(x2, 0, x2, getHeight());
			g.drawLine(x3, 0, x3, getHeight());
			g.drawLine(getWidth(), 0, getWidth(), getHeight());
		}

		if (HEALTH) {
			int hly = (int) (height - (height * (model.getHealth() / 3.0)));
			int lasty = (int) (height - (height * (last.getHealth() / 3.0)));

			g.setColor(Color.green);
			g.drawLine(lastx, lasty, xaxis, hly);
		}

		if (LOAD) {
			int ldy = (int) (height - (height * (model.getLoad() / 100.0)));
			int lastldy = (int) (height - (height * (last.getLoad() / 100.0)));

			g.setColor(Color.blue);
			g.drawLine(lastx, lastldy, xaxis, ldy);
		}

		if (MEM) {
			int mmy = (int) (height - (height * (model.getMemload() / 100.0)));
			int lastmmy = (int) (height - (height * (last.getMemload() / 100.0)));

			g.setColor(Color.orange);
			g.drawLine(lastx, lastmmy, xaxis, mmy);
		}

		if (THREAD) {
			int thy = (int) (height - (height * (model.getThreadload() / 100.0)));
			int lastthy = (int) (height - (height * (last.getThreadload() / 100.0)));

			g.setColor(Color.red);
			g.drawLine(lastx, lastthy, xaxis, thy);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6355.java