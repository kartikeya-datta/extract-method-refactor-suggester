error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18300.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18300.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18300.java
text:
```scala
private static final L@@ogger log = LoggingManager.getLoggerForClass();

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

package org.apache.jmeter.modifiers.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;

import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.modifiers.UserParameters;
import org.apache.jmeter.processor.gui.AbstractPreProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class UserParametersGui extends AbstractPreProcessorGui {
	private static Logger log = LoggingManager.getLoggerForClass();

	private String THREAD_COLUMNS_PREFIX = JMeterUtils.getResString("user") + "_"; // $NON-NLS-1$ $NON-NLS-2$

	private JTable paramTable;

	private PowerTableModel tableModel;

	private int numUserColumns = 1;

	private JButton addParameterButton, addUserButton, deleteRowButton, deleteColumnButton;

	private JCheckBox perIterationCheck;

	public UserParametersGui() {
		super();
		init();
	}

	public String getLabelResource() {
		return "user_parameters_title"; // $NON-NLS-1$
	}

	public void configure(TestElement el) {
		initTableModel();
		paramTable.setModel(tableModel);
		UserParameters params = (UserParameters) el;
		CollectionProperty names = params.getNames();
		CollectionProperty threadValues = params.getThreadLists();
		tableModel.setColumnData(0, (List) names.getObjectValue());
		PropertyIterator iter = threadValues.iterator();
		if (iter.hasNext()) {
			tableModel.setColumnData(1, (List) iter.next().getObjectValue());
		}
		int count = 2;
		while (iter.hasNext()) {
			String colName = THREAD_COLUMNS_PREFIX + count;
			tableModel.addNewColumn(colName, String.class);
			tableModel.setColumnData(count, (List) iter.next().getObjectValue());
			count++;
		}
		perIterationCheck.setSelected(params.isPerIteration());
		super.configure(el);
	}

	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		UserParameters params = new UserParameters();
		modifyTestElement(params);
		return params;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement params) {
		if (paramTable.isEditing()) {
			paramTable.getCellEditor().stopCellEditing();
		}
		((UserParameters) params).setNames(new CollectionProperty(UserParameters.NAMES, tableModel
				.getColumnData(JMeterUtils.getResString("name")))); // $NON-NLS-1$
		CollectionProperty threadLists = new CollectionProperty(UserParameters.THREAD_VALUES, new ArrayList());
		log.debug("making threadlists from gui = " + threadLists);
		for (int x = 1; x < tableModel.getColumnCount(); x++) {
			threadLists.addItem(tableModel.getColumnData(THREAD_COLUMNS_PREFIX + x));
			log.debug("Adding column to threadlist: " + tableModel.getColumnData(THREAD_COLUMNS_PREFIX + x));
			log.debug("Threadlists now = " + threadLists);
		}
		log.debug("In the end, threadlists = " + threadLists);
		((UserParameters) params).setThreadLists(threadLists);
		((UserParameters) params).setPerIteration(perIterationCheck.isSelected());
		super.configureTestElement(params);
	}

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    public void clearGui() {
        super.clearGui();
        
        initTableModel();
        paramTable.setModel(tableModel);
        perIterationCheck.setSelected(false);
    }    

	private void init() {
		setBorder(makeBorder());
		setLayout(new BorderLayout());
		JPanel vertPanel = new VerticalPanel();
		vertPanel.add(makeTitlePanel());

		perIterationCheck = new JCheckBox(JMeterUtils.getResString("update_per_iter"), true); // $NON-NLS-1$
		Box perIterationPanel = Box.createHorizontalBox();
		perIterationPanel.add(perIterationCheck);
		perIterationPanel.add(Box.createHorizontalGlue());
		vertPanel.add(perIterationPanel);
		add(vertPanel, BorderLayout.NORTH);

		add(makeParameterPanel(), BorderLayout.CENTER);
	}

	private JPanel makeParameterPanel() {
		JLabel tableLabel = new JLabel(JMeterUtils.getResString("user_parameters_table")); // $NON-NLS-1$
		initTableModel();
		paramTable = new JTable(tableModel);
		// paramTable.setRowSelectionAllowed(true);
		// paramTable.setColumnSelectionAllowed(true);
		paramTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// paramTable.setCellSelectionEnabled(true);
		// paramTable.setPreferredScrollableViewportSize(new Dimension(100,
		// 70));

		JPanel paramPanel = new JPanel(new BorderLayout());
		paramPanel.add(tableLabel, BorderLayout.NORTH);
		JScrollPane scroll = new JScrollPane(paramTable);
		scroll.setPreferredSize(scroll.getMinimumSize());
		paramPanel.add(scroll, BorderLayout.CENTER);
		paramPanel.add(makeButtonPanel(), BorderLayout.SOUTH);
		return paramPanel;
	}

	protected void initTableModel() {
		tableModel = new PowerTableModel(new String[] { JMeterUtils.getResString("name"), // $NON-NLS-1$
				THREAD_COLUMNS_PREFIX + numUserColumns }, new Class[] { String.class, String.class });
	}

	private JPanel makeButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));
		addParameterButton = new JButton(JMeterUtils.getResString("add_parameter")); // $NON-NLS-1$
		addUserButton = new JButton(JMeterUtils.getResString("add_user")); // $NON-NLS-1$
		deleteRowButton = new JButton(JMeterUtils.getResString("delete_parameter")); // $NON-NLS-1$
		deleteColumnButton = new JButton(JMeterUtils.getResString("delete_user")); // $NON-NLS-1$
		buttonPanel.add(addParameterButton);
		buttonPanel.add(deleteRowButton);
		buttonPanel.add(addUserButton);
		buttonPanel.add(deleteColumnButton);
		addParameterButton.addActionListener(new AddParamAction());
		addUserButton.addActionListener(new AddUserAction());
		deleteRowButton.addActionListener(new DeleteRowAction());
		deleteColumnButton.addActionListener(new DeleteColumnAction());
		return buttonPanel;
	}

	private class AddParamAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (paramTable.isEditing()) {
				TableCellEditor cellEditor = paramTable.getCellEditor(paramTable.getEditingRow(), paramTable
						.getEditingColumn());
				cellEditor.stopCellEditing();
			}

			tableModel.addNewRow();
			tableModel.fireTableDataChanged();

			// Enable DELETE (which may already be enabled, but it won't hurt)
			deleteRowButton.setEnabled(true);

			// Highlight (select) the appropriate row.
			int rowToSelect = tableModel.getRowCount() - 1;
			paramTable.setRowSelectionInterval(rowToSelect, rowToSelect);
		}
	}

	private class AddUserAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			if (paramTable.isEditing()) {
				TableCellEditor cellEditor = paramTable.getCellEditor(paramTable.getEditingRow(), paramTable
						.getEditingColumn());
				cellEditor.stopCellEditing();
			}

			tableModel.addNewColumn(THREAD_COLUMNS_PREFIX + tableModel.getColumnCount(), String.class);
			tableModel.fireTableDataChanged();

			// Enable DELETE (which may already be enabled, but it won't hurt)
			deleteColumnButton.setEnabled(true);

			// Highlight (select) the appropriate row.
			int colToSelect = tableModel.getColumnCount() - 1;
			paramTable.setColumnSelectionInterval(colToSelect, colToSelect);
		}
	}

	private class DeleteRowAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (paramTable.isEditing()) {
				TableCellEditor cellEditor = paramTable.getCellEditor(paramTable.getEditingRow(), paramTable
						.getEditingColumn());
				cellEditor.cancelCellEditing();
			}

			int rowSelected = paramTable.getSelectedRow();
			if (rowSelected >= 0) {
				tableModel.removeRow(rowSelected);
				tableModel.fireTableDataChanged();

				// Disable DELETE if there are no rows in the table to delete.
				if (tableModel.getRowCount() == 0) {
					deleteRowButton.setEnabled(false);
				}

				// Table still contains one or more rows, so highlight (select)
				// the appropriate one.
				else {
					int rowToSelect = rowSelected;

					if (rowSelected >= tableModel.getRowCount()) {
						rowToSelect = rowSelected - 1;
					}

					paramTable.setRowSelectionInterval(rowToSelect, rowToSelect);
				}
			}
		}
	}

	private class DeleteColumnAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (paramTable.isEditing()) {
				TableCellEditor cellEditor = paramTable.getCellEditor(paramTable.getEditingRow(), paramTable
						.getEditingColumn());
				cellEditor.cancelCellEditing();
			}

			int colSelected = paramTable.getSelectedColumn();
			if (colSelected == 0 || colSelected == 1) {
				JOptionPane.showMessageDialog(null, 
						JMeterUtils.getResString("column_delete_disallowed"), // $NON-NLS-1$
						"Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (colSelected >= 0) {
				tableModel.removeColumn(colSelected);
				tableModel.fireTableDataChanged();

				// Disable DELETE if there are no rows in the table to delete.
				if (tableModel.getColumnCount() == 0) {
					deleteColumnButton.setEnabled(false);
				}

				// Table still contains one or more rows, so highlight (select)
				// the appropriate one.
				else {

					if (colSelected >= tableModel.getColumnCount()) {
						colSelected = colSelected - 1;
					}

					paramTable.setColumnSelectionInterval(colSelected, colSelected);
				}
			}
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18300.java