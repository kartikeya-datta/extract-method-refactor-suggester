error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12812.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12812.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12812.java
text:
```scala
protected transient J@@Panel legendPanel;

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
 * 
 */

package org.apache.jmeter.visualizers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This class implements a statistical analyser that plots the accumulated time
 * taken to load each set of pages. The number of plots is equivalent to the
 * number of times the set of pages is configured to load.
 * 
 * 
 * Created 2001/08/11
 */
public class GraphAccumVisualizer extends AbstractVisualizer implements ImageVisualizer, GraphAccumListener, Clearable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	protected transient GraphAccumModel model;

	protected transient GraphAccum graph;

	transient protected JPanel legendPanel;

	/**
	 * Constructor.
	 */
	public GraphAccumVisualizer() {
		super();
		model = new GraphAccumModel();
		model.addGraphAccumListener(this);
		init();
		log.debug("Start : GraphAccumVisualizer1");
		log.debug("End : GraphAccumVisualizer1");
	}

	public String getLabelResource() {
		return "graph_full_results_title"; // $NON-NLS-1$
	}

	public void add(SampleResult res) {
		model.addNewSample(res);
	}

	/**
	 * Returns the panel where labels can be added.
	 * 
	 * @return a panel where labels can be added
	 */
	public Object getWhiteCanvas() {
		return legendPanel;
	}

	/**
	 * Gets the Image attribute of the GraphVisualizer object.
	 * 
	 * @return the Image value
	 */
	public Image getImage() {
		log.debug("Start : getImage1");
		Image result = graph.createImage(graph.getWidth(), graph.getHeight());

		graph.paintComponent(result.getGraphics());
		log.debug("End : getImage1");
		return result;
	}

	/**
	 * Updates the gui to reflect changes.
	 */
	public void updateGui() {
		log.debug("Start : updateGui1");
		graph.updateGui();
		log.debug("End : updateGui1");
	}

	/**
	 * Updates gui to reflect small changes.
	 * 
	 * @param s
	 *            sample to be added to plot
	 */
	public void updateGui(SampleResult s) {
		log.debug("Start : updateGui2");
		log.debug("End : updateGui2");
	}

	/**
	 * Clear this visualizer data.
	 */
	public synchronized void clearData() {
		model.clearData();
		graph.clearData();
		log.debug("Start : clear1");
		repaint();
		log.debug("End : clear1");
	}

	/**
	 * Returns a description of this instance.
	 * 
	 * @return description of this instance
	 */
	public String toString() {
		String toString = "Show the samples analysys as dot plots";

		log.debug("toString1 : Returning - " + toString);
		return toString;
	}

	/**
	 * Setup all the swing components.
	 */
	private void init() {
		log.debug("Start : init1");
		graph = new GraphAccum(model);
		graph.setVisualizer(this);

		this.setLayout(new BorderLayout());

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);

		mainPanel.setBorder(margin);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// TITLE
		JLabel panelTitleLabel = new JLabel(JMeterUtils.getResString("graph_full_results_title")); // $NON-NLS-1$
		Font curFont = panelTitleLabel.getFont();
		int curFontSize = curFont.getSize();

		curFontSize += 4;
		panelTitleLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), curFontSize));
		mainPanel.add(panelTitleLabel);

		mainPanel.add(getNamePanel());
		mainPanel.add(getFilePanel());

		JScrollPane graphScrollPanel = new JScrollPane(graph, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		graphScrollPanel.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		legendPanel = new JPanel();

		JScrollPane legendScrollPanel = new JScrollPane(legendPanel);
		JSplitPane graphSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphScrollPanel, legendScrollPanel);

		this.add(mainPanel, BorderLayout.NORTH);
		this.add(graphSplitPane, BorderLayout.CENTER);
		log.debug("End : init1");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12812.java