error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6607.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6607.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6607.java
text:
```scala
public C@@ollection<String> getMenuCategories() {

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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
//import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.util.HeaderAsPropertyRenderer;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

public class PropertyControlGui extends AbstractConfigGui
    implements ActionListener, UnsharedComponent {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_NAMES_0 = "name"; // $NON-NLS-1$

    private static final String COLUMN_NAMES_1 = "value"; // $NON-NLS-1$

    // TODO: add and delete not currently supported
    private static final String ADD = "add"; // $NON-NLS-1$

    private static final String DELETE = "delete"; // $NON-NLS-1$

    private static final String SYSTEM = "system"; // $NON-NLS-1$

    private static final String JMETER = "jmeter"; // $NON-NLS-1$

    private JCheckBox systemButton = new JCheckBox("System");
    private JCheckBox jmeterButton = new JCheckBox("JMeter");

    private JLabel tableLabel = new JLabel("Properties");

    /** The table containing the list of arguments. */
    private transient JTable table;

    /** The model for the arguments table. */
    protected transient ObjectTableModel tableModel;

//    /** A button for adding new arguments to the table. */
//    private JButton add;
//
//    /** A button for removing arguments from the table. */
//    private JButton delete;

    public PropertyControlGui() {
        super();
        init();
    }

    public String getLabelResource() {
        return "property_visualiser_title"; // $NON-NLS-1$
    }

    @Override
    public Collection getMenuCategories() {
        return Arrays.asList(new String[] { MenuFactory.NON_TEST_ELEMENTS });
    }

    public void actionPerformed(ActionEvent action) {
        String command = action.getActionCommand();
        if (ADD.equals(command)){
            return;
        }
        if (DELETE.equals(command)){
            return;
        }
        if (SYSTEM.equals(command)){
            setUpData();
            return;
        }
        if (JMETER.equals(command)){
            setUpData();
            return;
        }

    }

    public void add(SampleResult sample) {
    }

    public TestElement createTestElement() {
        TestElement el = new ConfigTestElement();
        modifyTestElement(el);
        return el;
    }
    @Override
    public void configure(TestElement element) {
        super.configure(element);
        setUpData();
    }

    private void setUpData(){
        tableModel.clearData();
        Properties p=null;
        if (systemButton.isSelected()){
            p = System.getProperties();
        }
        if (jmeterButton.isSelected()) {
            p = JMeterUtils.getJMeterProperties();
        }
        if (p == null) {
            return;
        }
        Set s = p.entrySet();
        ArrayList al = new ArrayList(s);
        Collections.sort(al, new Comparator(){
            public int compare(Object o1, Object o2) {
                String m1,m2;
                m1=(String)((Map.Entry)o1).getKey();
                m2=(String)((Map.Entry)o2).getKey();
                return m1.compareTo(m2);
            }
        });
        Iterator i = al.iterator();
        while(i.hasNext()){
            tableModel.addRow(i.next());
        }

    }

    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
    }
    private Component makeMainPanel() {
        initializeTableModel();
        table = new JTable(tableModel);
        table.getTableHeader().setDefaultRenderer(new HeaderAsPropertyRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return makeScrollPane(table);
    }

    /**
     * Create a panel containing the title label for the table.
     *
     * @return a panel containing the title label
     */
    private Component makeLabelPanel() {
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ButtonGroup bg = new ButtonGroup();
        bg.add(systemButton);
        bg.add(jmeterButton);
        jmeterButton.setSelected(true);
        systemButton.setActionCommand(SYSTEM);
        jmeterButton.setActionCommand(JMETER);
        systemButton.addActionListener(this);
        jmeterButton.addActionListener(this);

        labelPanel.add(systemButton);
        labelPanel.add(jmeterButton);
        labelPanel.add(tableLabel);
        return labelPanel;
    }

//    /**
//     * Create a panel containing the add and delete buttons.
//     *
//     * @return a GUI panel containing the buttons
//     */
//    private JPanel makeButtonPanel() {// Not currently used
//        add = new JButton(JMeterUtils.getResString("add")); // $NON-NLS-1$
//        add.setActionCommand(ADD);
//        add.setEnabled(true);
//
//        delete = new JButton(JMeterUtils.getResString("delete")); // $NON-NLS-1$
//        delete.setActionCommand(DELETE);
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
//         add.addActionListener(this);
//        delete.addActionListener(this);
//        buttonPanel.add(add);
//        buttonPanel.add(delete);
//        return buttonPanel;
//    }

    /**
     * Initialize the components and layout of this component.
     */
    private void init() {
        JPanel p = this;

            setLayout(new BorderLayout(0, 5));
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
            p = new JPanel();

        p.setLayout(new BorderLayout());

        p.add(makeLabelPanel(), BorderLayout.NORTH);
        p.add(makeMainPanel(), BorderLayout.CENTER);
        // Force a minimum table height of 70 pixels
        p.add(Box.createVerticalStrut(70), BorderLayout.WEST);
        //p.add(makeButtonPanel(), BorderLayout.SOUTH);

        add(p, BorderLayout.CENTER);
        table.revalidate();
    }
    private void initializeTableModel() {
        tableModel = new ObjectTableModel(new String[] { COLUMN_NAMES_0, COLUMN_NAMES_1 },
                new Functor[] {
                new Functor(Map.Entry.class, "getKey"), // $NON-NLS-1$
                new Functor(Map.Entry.class, "getValue") },  // $NON-NLS-1$
                new Functor[] {
                null, //new Functor("setName"), // $NON-NLS-1$
                new Functor(Map.Entry.class,"setValue", new Class[] { Object.class }) // $NON-NLS-1$
            },
                new Class[] { String.class, String.class });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6607.java