error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1557.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1557.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1557.java
text:
```scala
S@@tring start = System.getProperty("user.dir", ""); // $NON-NLS-1$ // $NON-NLS-2$

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

package org.apache.jmeter.gui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterFileFilter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

public class FileListPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTable files = null;

    private transient ObjectTableModel tableModel = null;

    private static final String ACTION_BROWSE = "browse"; // $NON-NLS-1$

    private static final String LABEL_LIBRARY = "library"; // $NON-NLS-1$

    private JButton browse = new JButton(JMeterUtils.getResString(ACTION_BROWSE));

    private JButton clear = new JButton(JMeterUtils.getResString("clear")); // $NON-NLS-1$

    private JButton delete = new JButton(JMeterUtils.getResString("delete")); // $NON-NLS-1$

    private List<ChangeListener> listeners = new LinkedList<ChangeListener>();

    private String title;

    private String filetype;

    /**
     * Constructor for the FilePanel object.
     */
    public FileListPanel() {
        title = ""; // $NON-NLS-1$
        init();
    }

    public FileListPanel(String title) {
        this.title = title;
        init();
    }

    public FileListPanel(String title, String filetype) {
        this.title = title;
        this.filetype = filetype;
        init();
    }

    /**
     * Constructor for the FilePanel object.
     */
    public FileListPanel(ChangeListener l, String title) {
        this.title = title;
        init();
        listeners.add(l);
    }

    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    private void init() {
        this.setLayout(new BorderLayout(0, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        JLabel jtitle = new JLabel(title);

        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(jtitle);
        buttons.add(browse);
        buttons.add(delete);
        buttons.add(clear);
        add(buttons,BorderLayout.NORTH);

        this.initializeTableModel();
        files = new JTable(tableModel);
        files.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        files.revalidate();

        JScrollPane scrollpane = new JScrollPane(files);
        scrollpane.setPreferredSize(new Dimension(100,80));
        add(scrollpane,BorderLayout.CENTER);

        browse.setActionCommand(ACTION_BROWSE); // $NON-NLS-1$
        browse.addActionListener(this);
        clear.addActionListener(this);
        delete.addActionListener(this);
        //this.setPreferredSize(new Dimension(400,150));
    }

    /**
     * If the gui needs to enable/disable the FilePanel, call the method.
     *
     * @param enable
     */
    public void enableFile(boolean enable) {
        browse.setEnabled(enable);
        files.setEnabled(false);
    }

    /**
     * Add a single file to the table
     * @param f
     */
    public void addFilename(String f) {
        tableModel.addRow(f);
    }

    /**
     * clear the files from the table
     */
    public void clearFiles() {
        tableModel.clearData();
    }

    public void setFiles(String[] files) {
        this.clearFiles();
        for (int idx=0; idx < files.length; idx++) {
            addFilename(files[idx]);
        }
    }

    public String[] getFiles() {
        String[] _files = new String[tableModel.getRowCount()];
        for (int idx=0; idx < _files.length; idx++) {
            _files[idx] = (String)tableModel.getValueAt(idx,0);
        }
        return _files;
    }

    protected void deleteFile() {
        // If a table cell is being edited, we must cancel the editing before
        // deleting the row

        int rowSelected = files.getSelectedRow();
        if (rowSelected >= 0) {
            tableModel.removeRow(rowSelected);
            tableModel.fireTableDataChanged();

        }
    }

    private void fireFileChanged() {
        Iterator<ChangeListener> iter = listeners.iterator();
        while (iter.hasNext()) {
            iter.next().stateChanged(new ChangeEvent(this));
        }
    }

    protected void initializeTableModel() {
        tableModel = new ObjectTableModel(new String[] { JMeterUtils.getResString(LABEL_LIBRARY) },
                new Functor[0] , new Functor[0] , // i.e. bypass the Functors
                new Class[] { String.class });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            this.clearFiles();
        } else if (e.getActionCommand().equals(ACTION_BROWSE)) {
            JFileChooser chooser = new JFileChooser();
            String start = JMeterUtils.getPropDefault("user.dir", ""); // $NON-NLS-1$ // $NON-NLS-2$
            chooser.setCurrentDirectory(new File(start));
            chooser.setFileFilter(new JMeterFileFilter(new String[] { filetype }));
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.setMultiSelectionEnabled(true);
            chooser.showOpenDialog(GuiPackage.getInstance().getMainFrame());
            File[] cfiles = chooser.getSelectedFiles();
            if (cfiles != null) {
                for (int idx=0; idx < cfiles.length; idx++) {
                    this.addFilename(cfiles[idx].getPath());
                }
                fireFileChanged();
            }
        } else if (e.getSource() == delete) {
            this.deleteFile();
        } else {
            fireFileChanged();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1557.java