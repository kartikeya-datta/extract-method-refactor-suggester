error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2134.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2134.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2134.java
text:
```scala
private v@@oid init() {// called from ctor, so must not be overridable

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

import java.util.HashMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;

public class MonitorPerformancePanel extends JSplitPane implements TreeSelectionListener, MonitorListener, Clearable {

	private JScrollPane TREEPANE;

	private JPanel GRAPHPANEL;

	private JTree SERVERTREE;

	private DefaultTreeModel TREEMODEL;

	private MonitorGraph GRAPH;

	private DefaultMutableTreeNode ROOTNODE;

	private HashMap SERVERMAP;

	private MonitorAccumModel MODEL;

	private SampleResult ROOTSAMPLE;

	public static final String LEGEND_HEALTH = JMeterUtils.getResString("monitor_legend_health"); //$NON-NLS-1$

	public static final String LEGEND_LOAD = JMeterUtils.getResString("monitor_legend_load"); //$NON-NLS-1$

	public static final String LEGEND_MEM = JMeterUtils.getResString("monitor_legend_memory_per"); //$NON-NLS-1$

	public static final String LEGEND_THREAD = JMeterUtils.getResString("monitor_legend_thread_per"); //$NON-NLS-1$

	public static final ImageIcon LEGEND_HEALTH_ICON = JMeterUtils.getImage("monitor-green-legend.gif"); //$NON-NLS-1$

	public static final ImageIcon LEGEND_LOAD_ICON = JMeterUtils.getImage("monitor-blue-legend.gif"); //$NON-NLS-1$

	public static final ImageIcon LEGEND_MEM_ICON = JMeterUtils.getImage("monitor-orange-legend.gif"); //$NON-NLS-1$

	public static final ImageIcon LEGEND_THREAD_ICON = JMeterUtils.getImage("monitor-red-legend.gif"); //$NON-NLS-1$

	public static final String GRID_LABEL_TOP = JMeterUtils.getResString("monitor_label_left_top"); //$NON-NLS-1$

	public static final String GRID_LABEL_MIDDLE = JMeterUtils.getResString("monitor_label_left_middle"); //$NON-NLS-1$

	public static final String GRID_LABEL_BOTTOM = JMeterUtils.getResString("monitor_label_left_bottom"); //$NON-NLS-1$

	public static final String GRID_LABEL_HEALTHY = JMeterUtils.getResString("monitor_label_right_healthy"); //$NON-NLS-1$

	public static final String GRID_LABEL_ACTIVE = JMeterUtils.getResString("monitor_label_right_active"); //$NON-NLS-1$

	public static final String GRID_LABEL_WARNING = JMeterUtils.getResString("monitor_label_right_warning"); //$NON-NLS-1$

	public static final String GRID_LABEL_DEAD = JMeterUtils.getResString("monitor_label_right_dead"); //$NON-NLS-1$

	public static final String PERF_TITLE = JMeterUtils.getResString("monitor_performance_title"); //$NON-NLS-1$

	public static final String SERVER_TITLE = JMeterUtils.getResString("monitor_performance_servers"); //$NON-NLS-1$

	protected Font plaintext = new Font("plain", Font.TRUETYPE_FONT, 10); //$NON-NLS-1$

	/**
	 * 
	 * @deprecated Only for use in unit testing
	 */
	public MonitorPerformancePanel() {
		// log.warn("Only for use in unit testing");
	}

	/**
	 * 
	 */
	public MonitorPerformancePanel(MonitorAccumModel model, MonitorGraph graph) {
		super();
		this.SERVERMAP = new HashMap();
		this.MODEL = model;
		this.MODEL.addListener(this);
		this.GRAPH = graph;
		init();
	}

	/**
	 * init() will create all the necessary swing panels, labels and icons for
	 * the performance panel.
	 */
	protected void init() {
		ROOTSAMPLE = new SampleResult();
		ROOTSAMPLE.setSampleLabel(SERVER_TITLE);
		ROOTSAMPLE.setSuccessful(true);
		ROOTNODE = new DefaultMutableTreeNode(ROOTSAMPLE);
		TREEMODEL = new DefaultTreeModel(ROOTNODE);
		SERVERTREE = new JTree(TREEMODEL);
		SERVERTREE.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		SERVERTREE.addTreeSelectionListener(this);
		SERVERTREE.setShowsRootHandles(true);
		TREEPANE = new JScrollPane(SERVERTREE);
		TREEPANE.setPreferredSize(new Dimension(150, 200));
		this.add(TREEPANE, JSplitPane.LEFT);
		this.setDividerLocation(0.18);

		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		JLabel title = new JLabel(" " + PERF_TITLE);
		title.setPreferredSize(new Dimension(200, 40));
		GRAPHPANEL = new JPanel();
		GRAPHPANEL.setLayout(new BorderLayout());
		GRAPHPANEL.setMaximumSize(new Dimension(MODEL.getBufferSize(), MODEL.getBufferSize()));
		GRAPHPANEL.setBackground(Color.white);
		GRAPHPANEL.add(GRAPH, BorderLayout.CENTER);
		right.add(GRAPHPANEL, BorderLayout.CENTER);

		right.add(title, BorderLayout.NORTH);
		right.add(createLegend(), BorderLayout.SOUTH);
		right.add(createLeftGridLabels(), BorderLayout.WEST);
		right.add(createRightGridLabels(), BorderLayout.EAST);
		this.add(right, JSplitPane.RIGHT);
	}

	/**
	 * Method will create the legends at the bottom of the performance tab
	 * explaining the meaning of each line.
	 * 
	 * @return JPanel
	 */
	public JPanel createLegend() {
		Dimension lsize = new Dimension(130, 18);

		JPanel legend = new JPanel();
		legend.setLayout(new FlowLayout());

		JLabel load = new JLabel(LEGEND_LOAD);
		load.setFont(plaintext);
		load.setPreferredSize(lsize);
		load.setIcon(LEGEND_LOAD_ICON);
		legend.add(load);

		JLabel mem = new JLabel(LEGEND_MEM);
		mem.setFont(plaintext);
		mem.setPreferredSize(lsize);
		mem.setIcon(LEGEND_MEM_ICON);
		legend.add(mem);

		JLabel thd = new JLabel(LEGEND_THREAD);
		thd.setFont(plaintext);
		thd.setPreferredSize(lsize);
		thd.setIcon(LEGEND_THREAD_ICON);
		legend.add(thd);

		JLabel health = new JLabel(LEGEND_HEALTH);
		health.setFont(plaintext);
		health.setPreferredSize(lsize);
		health.setIcon(LEGEND_HEALTH_ICON);
		legend.add(health);

		return legend;
	}

	/**
	 * Method is responsible for creating the left grid labels.
	 * 
	 * @return JPanel
	 */
	public JPanel createLeftGridLabels() {
		Dimension lsize = new Dimension(33, 20);
		JPanel labels = new JPanel();
		labels.setLayout(new BorderLayout());

		JLabel top = new JLabel(" " + GRID_LABEL_TOP);
		top.setFont(plaintext);
		top.setPreferredSize(lsize);
		labels.add(top, BorderLayout.NORTH);

		JLabel mid = new JLabel(" " + GRID_LABEL_MIDDLE);
		mid.setFont(plaintext);
		mid.setPreferredSize(lsize);
		labels.add(mid, BorderLayout.CENTER);

		JLabel bottom = new JLabel(" " + GRID_LABEL_BOTTOM);
		bottom.setFont(plaintext);
		bottom.setPreferredSize(lsize);
		labels.add(bottom, BorderLayout.SOUTH);
		return labels;
	}

	/**
	 * Method is responsible for creating the grid labels on the right for
	 * "healthy" and "dead"
	 * 
	 * @return JPanel
	 */
	public JPanel createRightGridLabels() {
		JPanel labels = new JPanel();
		labels.setLayout(new BorderLayout());
		labels.setPreferredSize(new Dimension(40, GRAPHPANEL.getWidth() - 100));
		Dimension lsize = new Dimension(40, 20);
		JLabel h = new JLabel(GRID_LABEL_HEALTHY);
		h.setFont(plaintext);
		h.setPreferredSize(lsize);
		labels.add(h, BorderLayout.NORTH);

		JLabel d = new JLabel(GRID_LABEL_DEAD);
		d.setFont(plaintext);
		d.setPreferredSize(lsize);
		labels.add(d, BorderLayout.SOUTH);
		return labels;
	}

	/**
	 * MonitorAccumModel will call this method to notify the component data has
	 * changed.
	 */
	public synchronized void addSample(MonitorModel model) {
		if (!SERVERMAP.containsKey(model.getURL())) {
			DefaultMutableTreeNode newnode = new DefaultMutableTreeNode(model);
			newnode.setAllowsChildren(false);
			SERVERMAP.put(model.getURL(), newnode);
			ROOTNODE.add(newnode);
			this.TREEPANE.updateUI();
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) SERVERTREE.getLastSelectedPathComponent();
		if (node != null) {
			Object usrobj = node.getUserObject();
			if (usrobj instanceof MonitorModel) {
				GRAPH.updateGui((MonitorModel) usrobj);
			}
		}
	}

	/**
	 * When the user selects a different node in the tree, we get the selected
	 * node. From the node, we get the UserObject used to create the treenode in
	 * the constructor.
	 */
	public void valueChanged(TreeSelectionEvent e) {
		// we check to see if the lastSelectedPath is null
		// after we clear, it would return null
		if (SERVERTREE.getLastSelectedPathComponent() != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) SERVERTREE.getLastSelectedPathComponent();
			Object usrobj = node.getUserObject();
			if (usrobj != null && usrobj instanceof MonitorModel) {
				MonitorModel mo = (MonitorModel) usrobj;
				GRAPH.updateGui(mo);
				this.updateUI();
			}
			TREEPANE.updateUI();
		}
	}

	/**
	 * clear will remove all child nodes from the ROOTNODE, clear the HashMap,
	 * update the graph and jpanel for the server tree.
	 */
	public void clearData() {
		this.SERVERMAP.clear();
		ROOTNODE.removeAllChildren();
		SERVERTREE.updateUI();
		GRAPH.clearData();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2134.java