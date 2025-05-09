error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8283.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8283.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8283.java
text:
```scala
v@@alueTA.setInitialText((String)tableModel.getValueAt(selectedRow, 1));

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

package org.apache.jmeter.config.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.jmeter.gui.action.KeyStrokes;
import org.apache.jmeter.gui.util.JSyntaxTextArea;
import org.apache.jmeter.gui.util.JTextScrollPane;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ComponentUtil;
import org.apache.jorphan.gui.ObjectTableModel;

/**
 * Show detail of a Row
 */
public class RowDetailDialog extends JDialog implements ActionListener, DocumentListener {

    private static final long serialVersionUID = 6578889215615435475L;

    /** Command for moving a row up in the table. */
    private static final String NEXT = "next"; // $NON-NLS-1$

    /** Command for moving a row down in the table. */
    private static final String PREVIOUS = "previous"; // $NON-NLS-1$

    /** Command for CANCEL. */
    private static final String CLOSE = "close"; // $NON-NLS-1$

    private static final String UPDATE = "update"; // $NON-NLS-1$

    private JLabel nameLabel;

    private JTextField nameTF;

    private JLabel valueLabel;

    private JSyntaxTextArea valueTA;

    private JButton nextButton;

    private JButton previousButton;
    
    private JButton closeButton;

    private ObjectTableModel tableModel;

    private int selectedRow;
    
    private boolean textChanged = true; // change to false after the first insert


    public RowDetailDialog() {
        super();
    }

    public RowDetailDialog(ObjectTableModel tableModel, int selectedRow) {
        super((JFrame) null, JMeterUtils.getResString("detail"), true); //$NON-NLS-1$
        this.tableModel = tableModel;
        this.selectedRow = selectedRow;
        init();
    }

    @Override
    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        // Hide Window on ESC
        Action escapeAction = new AbstractAction("ESCAPE") {
            /**
             *
             */
            private static final long serialVersionUID = -8699034338969407625L;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        };
        // Do update on Enter
        Action enterAction = new AbstractAction("ENTER") {
            /**
             *
             */
            private static final long serialVersionUID = -1529005452976176873L;

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doUpdate(actionEvent);
                setVisible(false);
            }
        };
        ActionMap actionMap = rootPane.getActionMap();
        actionMap.put(escapeAction.getValue(Action.NAME), escapeAction);
        actionMap.put(enterAction.getValue(Action.NAME), enterAction);
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStrokes.ESC, escapeAction.getValue(Action.NAME));
        inputMap.put(KeyStrokes.ENTER, enterAction.getValue(Action.NAME));
        return rootPane;
    }

    private void init() {
        this.getContentPane().setLayout(new BorderLayout(10,10));

        nameLabel = new JLabel(JMeterUtils.getResString("name")); //$NON-NLS-1$
        nameTF = new JTextField(JMeterUtils.getResString("name"), 20); //$NON-NLS-1$
        nameTF.getDocument().addDocumentListener(this);
        JPanel namePane = new JPanel(new BorderLayout());
        namePane.add(nameLabel, BorderLayout.WEST);
        namePane.add(nameTF, BorderLayout.CENTER);

        valueLabel = new JLabel(JMeterUtils.getResString("value")); //$NON-NLS-1$
        valueTA = new JSyntaxTextArea(30, 80);
        valueTA.getDocument().addDocumentListener(this);
        setValues(selectedRow);
        JPanel valuePane = new JPanel(new BorderLayout());
        valuePane.add(valueLabel, BorderLayout.NORTH);
        JTextScrollPane jTextScrollPane = new JTextScrollPane(valueTA);
        valuePane.add(jTextScrollPane, BorderLayout.CENTER);

        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.add(namePane, BorderLayout.NORTH);
        detailPanel.add(valuePane, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(7, 3, 3, 3));
        mainPanel.add(detailPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton updateButton = new JButton(JMeterUtils.getResString("update")); //$NON-NLS-1$
        updateButton.setActionCommand(UPDATE);
        updateButton.addActionListener(this);
        closeButton = new JButton(JMeterUtils.getResString("close")); //$NON-NLS-1$
        closeButton.setActionCommand(CLOSE);
        closeButton.addActionListener(this);
        nextButton = new JButton(JMeterUtils.getResString("next")); //$NON-NLS-1$
        nextButton.setActionCommand(NEXT);
        nextButton.addActionListener(this);
        nextButton.setEnabled(selectedRow < tableModel.getRowCount()-1);
        previousButton = new JButton(JMeterUtils.getResString("previous")); //$NON-NLS-1$
        previousButton.setActionCommand(PREVIOUS);
        previousButton.addActionListener(this);
        previousButton.setEnabled(selectedRow > 0);

        buttonsPanel.add(updateButton);
        buttonsPanel.add(previousButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(closeButton);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(mainPanel);
        nameTF.requestFocusInWindow();

        this.pack();
        ComponentUtil.centerComponentInWindow(this);
    }

    /**
     * Do search
     * @param e {@link ActionEvent}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(action.equals(CLOSE)) {
            this.setVisible(false);
        }
        else if(action.equals(NEXT)) {
            selectedRow++;
            previousButton.setEnabled(true);
            nextButton.setEnabled(selectedRow < tableModel.getRowCount()-1);
            setValues(selectedRow);
        }
        else if(action.equals(PREVIOUS)) {
            selectedRow--;
            nextButton.setEnabled(true);
            previousButton.setEnabled(selectedRow > 0);
            setValues(selectedRow);
        }
        else if(action.equals(UPDATE)) {
            doUpdate(e);
        }
    }

    /**
     * Set TextField and TA values from model
     * @param selectedRow Selected row
     */
    private void setValues(int selectedRow) {
        nameTF.setText((String)tableModel.getValueAt(selectedRow, 0));
        valueTA.setText((String)tableModel.getValueAt(selectedRow, 1));
        valueTA.setCaretPosition(0);
        textChanged = false;
    }

    /**
     * Update model values
     * @param actionEvent
     */
    protected void doUpdate(ActionEvent actionEvent) {
        tableModel.setValueAt(nameTF.getText(), selectedRow, 0);
        tableModel.setValueAt(valueTA.getText(), selectedRow, 1);
        // Change Cancel label to Close
        closeButton.setText(JMeterUtils.getResString("close")); //$NON-NLS-1$
        textChanged = false;
    }

    /**
     * Change the label of Close button to Cancel (after the first text changes)
     */
    private void changeLabelButton() {
        if (!textChanged) {
            closeButton.setText(JMeterUtils.getResString("cancel")); //$NON-NLS-1$
            textChanged = true;
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changeLabelButton();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changeLabelButton();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        changeLabelButton();
    }
    
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8283.java