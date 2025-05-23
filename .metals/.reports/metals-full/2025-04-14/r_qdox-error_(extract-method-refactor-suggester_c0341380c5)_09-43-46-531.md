error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8122.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8122.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8122.java
text:
```scala
m@@ainPanel.setLayout(new VerticalLayout(5, VerticalLayout.BOTH));

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
import java.awt.Color;
import java.awt.FlowLayout;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.Calculator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.gui.layout.VerticalLayout;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.Functor;
import org.apache.log.Logger;

/**
 * This class implements a statistical analyser that calculates both the average
 * and the standard deviation of the sampling process. The samples are displayed
 * in a JTable, and the statistics are displayed at the bottom of the table.
 * 
 * created March 10, 2002
 * 
 * @version $Revision$ Updated on $Date$
 */
public class TableVisualizer extends AbstractVisualizer implements Clearable {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private final String[] COLUMNS = new String[] {
            JMeterUtils.getResString("table_visualizer_sample_num"), // $NON-NLS-1$
            JMeterUtils.getResString("table_visualizer_start_time"), // $NON-NLS-1$
            JMeterUtils.getResString("table_visualizer_thread_name"),// $NON-NLS-1$
			JMeterUtils.getResString("sampler_label"),  // $NON-NLS-1$
            JMeterUtils.getResString("table_visualizer_sample_time"), // $NON-NLS-1$
			JMeterUtils.getResString("success?"),  // $NON-NLS-1$
            JMeterUtils.getResString("table_visualizer_bytes") }; // $NON-NLS-1$

	private ObjectTableModel model = null;

	private JTable table = null;

	private JTextField dataField = null;

	private JTextField averageField = null;

	private JTextField deviationField = null;

	private JTextField noSamplesField = null;

	private JScrollPane tableScrollPanel = null;

	private transient Calculator calc = new Calculator();

	private long currentData = 0;

    private Format format = new SimpleDateFormat("HH:mm:ss.SSS");
    
	/**
	 * Constructor for the TableVisualizer object.
	 */
	public TableVisualizer() {
		super();
		model = new ObjectTableModel(COLUMNS,
				Sample.class,         // The object used for each row
				new Functor[] {
                new Functor("getCount"), // $NON-NLS-1$
                new Functor("getStartTimeFormatted",  // $NON-NLS-1$
                        new Object[]{format}),
                new Functor("getThreadName"), // $NON-NLS-1$
                new Functor("getLabel"), // $NON-NLS-1$
				new Functor("getData"), // $NON-NLS-1$
                new Functor("isSuccess"), // $NON-NLS-1$
                new Functor("getBytes") }, // $NON-NLS-1$
                new Functor[] { null, null, null, null, null, null, null }, 
                new Class[] { 
				Long.class, String.class, String.class, String.class, Long.class, Boolean.class, Integer.class });
		init();
	}

	public static boolean testFunctors(){
		TableVisualizer instance = new TableVisualizer();
		return instance.model.checkFunctors(null,instance.getClass());
	}
	

	public String getLabelResource() {
		return "view_results_in_table"; // $NON-NLS-1$
	}

	protected synchronized void updateTextFields() {
		noSamplesField.setText(Long.toString(calc.getCount()));
		dataField.setText(Long.toString(currentData));
		averageField.setText(Long.toString((long) calc.getMean()));
		deviationField.setText(Long.toString((long) calc.getStandardDeviation()));
	}

	public void add(SampleResult res) {
        currentData = res.getTime();
		synchronized (calc) {
			calc.addValue(currentData);
			int count = calc.getCount();
			Sample newS = new Sample(res.getSampleLabel(), res.getTime(), 0, 0, 0, 0, 0, 0,
                    res.isSuccessful(), count, res.getEndTime(),res.getBytes(),
                    res.getThreadName());
			model.addRow(newS);
		}
		updateTextFields();
	}

	public synchronized void clear() {
		log.debug("Clear called", new Exception("Debug"));
		// this.graph.clear();
		model.clearData();
		currentData = 0;
		calc.clear();
		dataField.setText("0000"); // $NON-NLS-1$
		averageField.setText("0000"); // $NON-NLS-1$
		deviationField.setText("0000"); // $NON-NLS-1$
		repaint();
	}

	public String toString() {
		return "Show the samples in a table";
	}

	private void init() {
		this.setLayout(new BorderLayout());

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);

		mainPanel.setBorder(margin);
		mainPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));

		// NAME
		mainPanel.add(makeTitlePanel());

		// Set up the table itself
		table = new JTable(model);
		// table.getTableHeader().setReorderingAllowed(false);
		tableScrollPanel = new JScrollPane(table);
		tableScrollPanel.setViewportBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		// Set up footer of table which displays numerics of the graphs
		JPanel dataPanel = new JPanel();
		JLabel dataLabel = new JLabel(JMeterUtils.getResString("graph_results_latest_sample")); // $NON-NLS-1$
		dataLabel.setForeground(Color.black);
		dataField = new JTextField(5);
		dataField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		dataField.setEditable(false);
		dataField.setForeground(Color.black);
		dataField.setBackground(getBackground());
		dataPanel.add(dataLabel);
		dataPanel.add(dataField);

		JPanel averagePanel = new JPanel();
		JLabel averageLabel = new JLabel(JMeterUtils.getResString("graph_results_average")); // $NON-NLS-1$
		averageLabel.setForeground(Color.blue);
		averageField = new JTextField(5);
		averageField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		averageField.setEditable(false);
		averageField.setForeground(Color.blue);
		averageField.setBackground(getBackground());
		averagePanel.add(averageLabel);
		averagePanel.add(averageField);

		JPanel deviationPanel = new JPanel();
		JLabel deviationLabel = new JLabel(JMeterUtils.getResString("graph_results_deviation")); // $NON-NLS-1$
		deviationLabel.setForeground(Color.red);
		deviationField = new JTextField(5);
		deviationField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		deviationField.setEditable(false);
		deviationField.setForeground(Color.red);
		deviationField.setBackground(getBackground());
		deviationPanel.add(deviationLabel);
		deviationPanel.add(deviationField);

		JPanel noSamplesPanel = new JPanel();
		JLabel noSamplesLabel = new JLabel(JMeterUtils.getResString("graph_results_no_samples")); // $NON-NLS-1$

		noSamplesField = new JTextField(10);
		noSamplesField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		noSamplesField.setEditable(false);
		noSamplesField.setForeground(Color.black);
		noSamplesField.setBackground(getBackground());
		noSamplesPanel.add(noSamplesLabel);
		noSamplesPanel.add(noSamplesField);

		JPanel tableInfoPanel = new JPanel();
		tableInfoPanel.setLayout(new FlowLayout());
		tableInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		tableInfoPanel.add(noSamplesPanel);
		tableInfoPanel.add(dataPanel);
		tableInfoPanel.add(averagePanel);
		tableInfoPanel.add(deviationPanel);

		// Set up the table with footer
		JPanel tablePanel = new JPanel();

		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(tableScrollPanel, BorderLayout.CENTER);
		tablePanel.add(tableInfoPanel, BorderLayout.SOUTH);

		// Add the main panel and the graph
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(tablePanel, BorderLayout.CENTER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8122.java