error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4894.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4894.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4894.java
text:
```scala
n@@ew Functor("getAvgPageBytes"),       //$NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *  
 */

package org.apache.jmeter.visualizers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.Calculator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.gui.NumberRenderer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.gui.RateRenderer;
import org.apache.jorphan.gui.RendererUtils;
import org.apache.jorphan.reflect.Functor;

/**
 * Simpler (lower memory) version of Aggregate Report (StatVisualizer).
 * Excludes the Median and 90% columns, which are expensive in memory terms
 */
public class SummaryReport extends AbstractVisualizer implements Clearable {
	private final String[] COLUMNS = { 
            JMeterUtils.getResString("sampler_label"),               //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_count"),      //$NON-NLS-1$
            JMeterUtils.getResString("average"),                     //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_min"),        //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_max"),        //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_stddev"),     //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_error%"),     //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_rate"),       //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_bandwidth"),  //$NON-NLS-1$
            JMeterUtils.getResString("average_bytes"),               //$NON-NLS-1$
            };

	private final String TOTAL_ROW_LABEL 
        = JMeterUtils.getResString("aggregate_report_total_label");  //$NON-NLS-1$

	protected JTable myJTable;

	protected JScrollPane myScrollPane;

	transient private ObjectTableModel model;

	Map tableRows = Collections.synchronizedMap(new HashMap());

	// Column renderers
	private static final TableCellRenderer[] RENDERERS = 
		new TableCellRenderer[]{
		    null, // Label
		    null, // count
		    null, // Mean
		    null, // Min
		    null, // Max
		    new NumberRenderer("#0.00"), // Std Dev.
		    new NumberRenderer("#0.00%"), // Error %age
		    new RateRenderer("#.0"),      // Throughpur
		    new NumberRenderer("#0.00"),  // kB/sec
		    new NumberRenderer("#.0"),    // avg. pageSize
		};

    public SummaryReport() {
		super();
		model = new ObjectTableModel(COLUMNS,
				Calculator.class,// All rows have this class
                new Functor[] { 
                    new Functor("getLabel"),              //$NON-NLS-1$
                    new Functor("getCount"),              //$NON-NLS-1$
    				new Functor("getMeanAsNumber"),       //$NON-NLS-1$
                    new Functor("getMin"),                //$NON-NLS-1$
                    new Functor("getMax"),                //$NON-NLS-1$
                    new Functor("getStandardDeviation"),                //$NON-NLS-1$
                    new Functor("getErrorPercentage"),    //$NON-NLS-1$
                    new Functor("getRate"),               //$NON-NLS-1$
    				new Functor("getKBPerSecond"),        //$NON-NLS-1$
                    new Functor("getPageSize"),           //$NON-NLS-1$
                },
                new Functor[] { null, null, null, null, null, null, null, null , null, null }, 
                new Class[] { String.class, Long.class, Long.class, Long.class, Long.class, 
                              String.class, String.class, String.class, String.class, String.class });
		clearData();
		init();
	}
    
	public static boolean testFunctors(){
		SummaryReport instance = new SummaryReport();
		return instance.model.checkFunctors(null,instance.getClass());
	}
	
	public String getLabelResource() {
		return "summary_report";  //$NON-NLS-1$
	}

	public void add(SampleResult res) {
		Calculator row = null;
		synchronized (tableRows) {
			row = (Calculator) tableRows.get(res.getSampleLabel());
			if (row == null) {
				row = new Calculator(res.getSampleLabel());
				tableRows.put(row.getLabel(), row);
				model.insertRow(row, model.getRowCount() - 1);
			}
		}
		row.addSample(res);
		Calculator tot = ((Calculator) tableRows.get(TOTAL_ROW_LABEL));
        tot.addSample(res);
		model.fireTableDataChanged();
	}

	/**
	 * Clears this visualizer and its model, and forces a repaint of the table.
	 */
	public void clearData() {
		model.clearData();
		tableRows.clear();
		tableRows.put(TOTAL_ROW_LABEL, new Calculator(TOTAL_ROW_LABEL));
		model.addRow(tableRows.get(TOTAL_ROW_LABEL));
	}

	/**
	 * Main visualizer setup.
	 */
	private void init() {
		this.setLayout(new BorderLayout());

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);

		mainPanel.setBorder(margin);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(makeTitlePanel());

		myJTable = new JTable(model);
		myJTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		RendererUtils.applyRenderers(myJTable, RENDERERS);
		myScrollPane = new JScrollPane(myJTable);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(myScrollPane, BorderLayout.CENTER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4894.java