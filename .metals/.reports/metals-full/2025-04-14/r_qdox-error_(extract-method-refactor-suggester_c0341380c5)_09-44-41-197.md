error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15995.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15995.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15995.java
text:
```scala
J@@MeterUtils.getResString("sampler_label"),  //$NON-NLS-1$

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
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
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
//import javax.swing.table.AbstractTableModel;
//import javax.swing.table.TableModel;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

/**
 * Aggregrate Table-Based Reporting Visualizer for JMeter. Props to the people
 * who've done the other visualizers ahead of me (Stefano Mazzocchi), who I
 * borrowed code from to start me off (and much code may still exist). Thank
 * you!
 * 
 * @version $Revision$ on $Date$
 */
public class StatVisualizer extends AbstractVisualizer implements Clearable {
	private final String[] COLUMNS = { 
            JMeterUtils.getResString("url"),  //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_count"),  //$NON-NLS-1$
            JMeterUtils.getResString("average"),  //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_median"),  //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_90%_line"),  //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_min"),  //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_max"),  //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_error%"),  //$NON-NLS-1$
            JMeterUtils.getResString("aggregate_report_rate"),  //$NON-NLS-1$
			JMeterUtils.getResString("aggregate_report_bandwidth") };  //$NON-NLS-1$

	private final String TOTAL_ROW_LABEL 
        = JMeterUtils.getResString("aggregate_report_total_label");  //$NON-NLS-1$

	protected JTable myJTable;

	protected JScrollPane myScrollPane;

	transient private ObjectTableModel model;

	Map tableRows = Collections.synchronizedMap(new HashMap());

	public StatVisualizer() {
		super();
		model = new ObjectTableModel(COLUMNS, 
                new Functor[] { 
                    new Functor("getLabel"),   //$NON-NLS-1$
                    new Functor("getCount"),  //$NON-NLS-1$
    				new Functor("getMeanAsNumber"),   //$NON-NLS-1$
                    new Functor("getMedian"),  //$NON-NLS-1$
    				new Functor("getPercentPoint",  //$NON-NLS-1$
                            new Object[] { new Float(.900) }), 
                    new Functor("getMin"),  //$NON-NLS-1$
                    new Functor("getMax"),   //$NON-NLS-1$
                    new Functor("getErrorPercentageString"),   //$NON-NLS-1$
                    new Functor("getRateString"),  //$NON-NLS-1$
    				new Functor("getPageSizeString")   //$NON-NLS-1$
                },
                new Functor[] { null, null, null, null, null, null, null, null, null, null }, 
                new Class[] { String.class, Long.class, Long.class, Long.class, Long.class, 
                              Long.class, Long.class, String.class, String.class, String.class });
		clear();
		init();
	}

	public String getLabelResource() {
		return "aggregate_report";  //$NON-NLS-1$
	}

	public void add(SampleResult res) {
		SamplingStatCalculator row = null;
		synchronized (tableRows) {
			row = (SamplingStatCalculator) tableRows.get(res.getSampleLabel());
			if (row == null) {
				row = new SamplingStatCalculator(res.getSampleLabel());
				tableRows.put(row.getLabel(), row);
				model.insertRow(row, model.getRowCount() - 1);
			}
		}
		row.addSample(res);
		((SamplingStatCalculator) tableRows.get(TOTAL_ROW_LABEL)).addSample(res);
		model.fireTableDataChanged();
	}

	/**
	 * Clears this visualizer and its model, and forces a repaint of the table.
	 */
	public void clear() {
		model.clearData();
		tableRows.clear();
		tableRows.put(TOTAL_ROW_LABEL, new SamplingStatCalculator(TOTAL_ROW_LABEL));
		model.addRow(tableRows.get(TOTAL_ROW_LABEL));
	}

	// overrides AbstractVisualizer
	// forces GUI update after sample file has been read
	public TestElement createTestElement() {
		TestElement t = super.createTestElement();

		// sleepTill = 0;
		return t;
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

		// SortFilterModel mySortedModel =
		// new SortFilterModel(myStatTableModel);
		myJTable = new JTable(model);
		myJTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		myScrollPane = new JScrollPane(myJTable);
		this.add(mainPanel, BorderLayout.NORTH);
		this.add(myScrollPane, BorderLayout.CENTER);
	}
}

/**
 * Pulled this mainly out of a Core Java book to implement a sorted table -
 * haven't implemented this yet, it needs some non-trivial work done to it to
 * support our dynamically-sizing TableModel for this visualizer.
 * 
 * @version $Revision$
 */

//class SortFilterModel extends AbstractTableModel {
//	private TableModel model;
//
//	private int sortColumn;
//
//	private Row[] rows;
//
//	public SortFilterModel(TableModel m) {
//		model = m;
//		rows = new Row[model.getRowCount()];
//		for (int i = 0; i < rows.length; i++) {
//			rows[i] = new Row();
//			rows[i].index = i;
//		}
//	}
//
//	public SortFilterModel() {
//	}
//
//	public void setValueAt(Object aValue, int r, int c) {
//		model.setValueAt(aValue, rows[r].index, c);
//	}
//
//	public Object getValueAt(int r, int c) {
//		return model.getValueAt(rows[r].index, c);
//	}
//
//	public boolean isCellEditable(int r, int c) {
//		return model.isCellEditable(rows[r].index, c);
//	}
//
//	public int getRowCount() {
//		return model.getRowCount();
//	}
//
//	public int getColumnCount() {
//		return model.getColumnCount();
//	}
//
//	public String getColumnName(int c) {
//		return model.getColumnName(c);
//	}
//
//	public Class getColumnClass(int c) {
//		return model.getColumnClass(c);
//	}
//
//	public void sort(int c) {
//		sortColumn = c;
//		Arrays.sort(rows);
//		fireTableDataChanged();
//	}
//
//	public void addMouseListener(final JTable table) {
//		table.getTableHeader().addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent event) {
//				if (event.getClickCount() < 2) {
//					return;
//				}
//				int tableColumn = table.columnAtPoint(event.getPoint());
//				int modelColumn = table.convertColumnIndexToModel(tableColumn);
//
//				sort(modelColumn);
//			}
//		});
//	}
//
//	private class Row implements Comparable {
//		public int index;
//
//		public int compareTo(Object other) {
//			Row otherRow = (Row) other;
//			Object a = model.getValueAt(index, sortColumn);
//			Object b = model.getValueAt(otherRow.index, sortColumn);
//
//			if (a instanceof Comparable) {
//				return ((Comparable) a).compareTo(b);
//			} else {
//				return index - otherRow.index;
//			}
//		}
//	}
//} // class SortFilterModel
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15995.java