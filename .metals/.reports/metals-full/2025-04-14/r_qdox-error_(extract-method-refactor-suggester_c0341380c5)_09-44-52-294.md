error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3599.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3599.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3599.java
text:
```scala
T@@estElement te = cur.getTestElement();

// $Header$
/*
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

package org.apache.jmeter.protocol.http.proxy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.gui.LogicControllerGui;
import org.apache.jmeter.engine.util.ValueReplacer;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.gui.util.PowerTableModel;
import org.apache.jmeter.protocol.http.proxy.ProxyControl;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.WorkBench;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author  Michael Stover
 * @author  Thad Smith (controller combo code, taken from ModuleController)
 * @author  <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Revision$ updated on $Date$
 */
public class ProxyControlGui
    extends LogicControllerGui
    implements
        JMeterGUIComponent,
        ActionListener,
        KeyListener,
        FocusListener,
        UnsharedComponent
{
    private static transient Logger log = LoggingManager.getLoggerForClass();
    private JTextField portField;

   /**
    * Used to indicate that HTTP request headers should be captured.
    * The default is to capture the HTTP request headers,
    * which are specific to particular browser settings.
    */
    private JCheckBox httpHeaders;

    /**
     * Whether to group requests together based on inactivity separation
     * periods -- and how to handle such grouping afterwards.
     */
	private JComboBox groupingMode;

	/**
	 * Add an Assertion to the first sample of each set
	 */
	private JCheckBox addAssertions;
	
	/**
	 * Set/clear the Use Keep-Alive box on the samplers
	 * (default is true)
	 */
	private JCheckBox useKeepAlive;

    /**
     * List of available target controllers
     */
    private JComboBox targetNodes;
    private DefaultComboBoxModel targetNodesModel;
    
    private ProxyControl model;

    private JTable excludeTable;
    private PowerTableModel excludeModel;
    private JTable includeTable;
    private PowerTableModel includeModel;
    private static final String CHANGE_TARGET = "change_target";
        
    private JButton stop, start, restart;
    private static final String STOP = "stop";
    private static final String START = "start";
    private static final String RESTART = "restart";
    private static final String ADD_INCLUDE = "add_include";
    private static final String ADD_EXCLUDE = "add_exclude";
    private static final String DELETE_INCLUDE = "delete_include";
    private static final String DELETE_EXCLUDE = "delete_exclude";

    private static final String INCLUDE_COL =
        JMeterUtils.getResString("patterns_to_include");
    private static final String EXCLUDE_COL =
        JMeterUtils.getResString("patterns_to_exclude");

    public ProxyControlGui()
    {
        super();
        log.debug("Creating ProxyControlGui");
        init();
    }

    public TestElement createTestElement()
    {
        model = makeProxyControl();
        log.debug("creating/configuring model = " + model);
        modifyTestElement(model);
        return model;
    }

    protected ProxyControl makeProxyControl()
        {
            ProxyControl local = new ProxyControl();
            return local;
        }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement el)
    {
        configureTestElement(el);
        if(el instanceof ProxyControl)
        {
			model = (ProxyControl)el;
            model.setPort(portField.getText());
            setIncludeListInProxyControl(model);
            setExcludeListInProxyControl(model);
            model.setCaptureHttpHeaders(httpHeaders.isSelected());
			model.setGroupingMode(groupingMode.getSelectedIndex());
			model.setAssertions(addAssertions.isSelected());
			model.setUseKeepAlive(useKeepAlive.isSelected());
            TreeNodeWrapper nw= (TreeNodeWrapper)targetNodes.getSelectedItem();
            if (nw == null)
            {
                model.setTarget(null);
            }
            else
            {
                model.setTarget(nw.getTreeNode());
            }
        }
    }

    protected void setIncludeListInProxyControl(ProxyControl element)
    {
        List includeList = getDataList(includeModel, INCLUDE_COL);
        element.setIncludeList(includeList);
    }

    protected void setExcludeListInProxyControl(ProxyControl element)
    {
        List excludeList = getDataList(excludeModel, EXCLUDE_COL);
        element.setExcludeList(excludeList);
    }

    private List getDataList(PowerTableModel model, String colName)
    {
        String[] dataArray = model.getData().getColumn(colName);
        List list = new LinkedList();
        for (int i = 0; i < dataArray.length; i++)
        {
            list.add(dataArray[i]);
        }
        return list;
    }

    public String getStaticLabel()
    {
        return JMeterUtils.getResString("proxy_title");
    }

    public Collection getMenuCategories()
    {
        return Arrays.asList(new String[] { MenuFactory.NON_TEST_ELEMENTS });
    }

    public void configure(TestElement element)
    {
        log.debug("Configuring gui with " + element);
        super.configure(element);
        model = (ProxyControl)element;
        portField.setText(model.getPropertyAsString(ProxyControl.PORT));
		httpHeaders.setSelected(model.getPropertyAsBoolean(ProxyControl.CAPTURE_HTTP_HEADERS));
		groupingMode.setSelectedIndex(model.getPropertyAsInt(ProxyControl.GROUPING_MODE));
		addAssertions.setSelected(model.getPropertyAsBoolean(ProxyControl.ADD_ASSERTIONS));
		useKeepAlive.setSelected(model.getPropertyAsBoolean(ProxyControl.USE_KEEPALIVE,true));
        
        reinitializeTargetCombo();

        populateTable(includeModel, model.getIncludePatterns().iterator());
        populateTable(excludeModel, model.getExcludePatterns().iterator());
        repaint();
    }

    private void populateTable(PowerTableModel model, PropertyIterator iter)
    {
        model.clearData();
        while (iter.hasNext())
        {
            model.addRow(new Object[] { iter.next().getStringValue()});
        }
        model.fireTableDataChanged();
    }

    public void focusLost(FocusEvent e)
    {
        try
        {
            ((JTable) e.getSource()).getCellEditor().stopCellEditing();
        }
        catch (Exception err)
        {}
    }

    public void focusGained(FocusEvent e)
    {}

    /****************************************
     * !ToDo (Method description)
     *
     *@param action  !ToDo (Parameter description)
     ***************************************/
    public void actionPerformed(ActionEvent action)
    {
        String command = action.getActionCommand();

        if (command.equals(STOP))
        {
            model.stopProxy();
            stop.setEnabled(false);
            start.setEnabled(true);
            restart.setEnabled(false);
        }
        else if (command.equals(START))
        {
            startProxy();
        }
        else if (command.equals(RESTART))
        {
            model.stopProxy();
            startProxy();
        }
        else if ( command.equals(ProxyControl.CAPTURE_HTTP_HEADERS)
 command.equals(ProxyControl.ADD_ASSERTIONS)
 command.equals(ProxyControl.GROUPING_MODE)
 command.equals(ProxyControl.USE_KEEPALIVE)
                 )
        {
            enableRestart();
        }
        else if (command.equals(ADD_EXCLUDE))
        {
            excludeModel.addNewRow();
            excludeModel.fireTableDataChanged();
            enableRestart();
        }
        else if (command.equals(ADD_INCLUDE))
        {
            includeModel.addNewRow();
            includeModel.fireTableDataChanged();
            enableRestart();
        }
        else if (command.equals(DELETE_EXCLUDE))
        {
            excludeModel.removeRow(excludeTable.getSelectedRow());
            excludeModel.fireTableDataChanged();
            enableRestart();
        }
        else if (command.equals(DELETE_INCLUDE))
        {
            includeModel.removeRow(includeTable.getSelectedRow());
            includeModel.fireTableDataChanged();
            enableRestart();
        }
        else if (command.equals(CHANGE_TARGET))
        {
            log.debug("Change target "+targetNodes.getSelectedItem());
            log.debug("In model "+model);
            TreeNodeWrapper nw= (TreeNodeWrapper)targetNodes.getSelectedItem();
            model.setTarget(nw.getTreeNode());
        }
    }

    private void startProxy()
    {
        ValueReplacer replacer = GuiPackage.getInstance().getReplacer();
        modifyTestElement(model);
        try
        {
            replacer.replaceValues(model);
            model.startProxy();
            start.setEnabled(false);
            stop.setEnabled(true);
            restart.setEnabled(false);
        }
        catch (InvalidVariableException e)
        {
            JOptionPane.showMessageDialog(
                this,
                JMeterUtils.getResString("invalid_variables"),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enableRestart()
    {
        if (stop.isEnabled())
        {
            restart.setEnabled(true);
        }
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param e  !ToDo (Parameter description)
     ***************************************/
    public void keyPressed(KeyEvent e)
    {}

    /****************************************
     * !ToDo (Method description)
     *
     *@param e  !ToDo (Parameter description)
     ***************************************/
    public void keyTyped(KeyEvent e)
    {}

    /****************************************
     * !ToDo (Method description)
     *
     *@param e  !ToDo (Parameter description)
     ***************************************/
    public void keyReleased(KeyEvent e)
    {
        String fieldName = e.getComponent().getName();

        if (fieldName.equals(ProxyControl.PORT))
        {
            try
            {
                Integer.parseInt(portField.getText());
            }
            catch (NumberFormatException nfe)
            {
                if (portField.getText().length() > 0)
                {
                    JOptionPane.showMessageDialog(
                        this,
                        "You must enter a valid number",
                        "Invalid data",
                        JOptionPane.WARNING_MESSAGE);

                    // Right now, the cleanest thing to do is simply clear the
                    // entire text field. We do not want to set the text to
                    // the default because that would be confusing to the user.
                    // For example, the user typed "5t" instead of "56". After
                    // the user closes the error dialog, the text would change
                    // from "5t" to "1".  A litle confusing. If anything, it
                    // should display just "5". Future enhancement...
                    portField.setText("");
                }
            }
            enableRestart();
        }
    }

    private void init()
    {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout());
        
        Box myBox = Box.createVerticalBox();
        myBox.add(createPortPanel());
        myBox.add(Box.createVerticalStrut(5));
        myBox.add(createTargetPanel());
        myBox.add(Box.createVerticalStrut(5));
        myBox.add(createGroupingPanel());
        myBox.add(Box.createVerticalStrut(5));
        mainPanel.add(myBox, BorderLayout.NORTH);
        
        Box includeExcludePanel = Box.createVerticalBox();
        includeExcludePanel.add(createIncludePanel());
        includeExcludePanel.add(createExcludePanel());
        mainPanel.add(includeExcludePanel, BorderLayout.CENTER);

        mainPanel.add(createControls(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createControls()
    {
        start = new JButton(JMeterUtils.getResString("start"));
        start.addActionListener(this);
        start.setActionCommand(START);
        start.setEnabled(true);

        stop = new JButton(JMeterUtils.getResString("stop"));
        stop.addActionListener(this);
        stop.setActionCommand(STOP);
        stop.setEnabled(false);

        restart = new JButton(JMeterUtils.getResString("restart"));
        restart.addActionListener(this);
        restart.setActionCommand(RESTART);
        restart.setEnabled(false);

        JPanel panel = new JPanel();
        panel.add(start);
        panel.add(stop);
        panel.add(restart);
        return panel;
    }

    private JPanel createPortPanel()
    {
        portField = new JTextField(ProxyControl.DEFAULT_PORT_S, 8);
        portField.setName(ProxyControl.PORT);
        portField.addKeyListener(this);

        JLabel label = new JLabel(JMeterUtils.getResString("port"));
        label.setLabelFor(portField);

        httpHeaders = new JCheckBox(JMeterUtils.getResString("proxy_headers"));
        httpHeaders.setName(ProxyControl.CAPTURE_HTTP_HEADERS);
        httpHeaders.setSelected(true); //maintain original default
        httpHeaders.addActionListener(this);
        httpHeaders.setActionCommand(ProxyControl.CAPTURE_HTTP_HEADERS);

		addAssertions = new JCheckBox(JMeterUtils.getResString("proxy_assertions"));
		addAssertions.setName(ProxyControl.ADD_ASSERTIONS);
		addAssertions.setSelected(false);
		addAssertions.addActionListener(this);
		addAssertions.setActionCommand(ProxyControl.ADD_ASSERTIONS);

		useKeepAlive = new JCheckBox(JMeterUtils.getResString("proxy_usekeepalive"));
		useKeepAlive.setName(ProxyControl.USE_KEEPALIVE);
		useKeepAlive.setSelected(true);
		useKeepAlive.addActionListener(this);
		useKeepAlive.setActionCommand(ProxyControl.USE_KEEPALIVE);

        HorizontalPanel panel = new HorizontalPanel();
        panel.add(label);
        panel.add(portField);

        panel.add(Box.createHorizontalStrut(10));
        panel.add(httpHeaders);

		panel.add(useKeepAlive);
		panel.add(addAssertions);

        return panel;
    }

    private JPanel createTargetPanel()
    {
        targetNodesModel= new DefaultComboBoxModel();
        targetNodes = new JComboBox(targetNodesModel);
        targetNodes.setActionCommand(CHANGE_TARGET);
        // Action listener will be added later
        
        JLabel label = new JLabel(JMeterUtils.getResString("proxy_target"));
        label.setLabelFor(targetNodes);
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(label);
        panel.add(targetNodes);

		/* This listener subscription prevents freeing up the GUI when it's no longer in use
		 * (e.g. on locale change)... plus causes some anoying NPEs in the GUI instance
		 * created by the menu manager just to find out our name and which menus we
		 * want to be in...
		 * ... plus I don't think it's really necessary: configure(TestElement) already takes
		 * care of reinitializing the target combo when we come back to it. And I can't see how
		 * the tree can change in a relevant way without we leaving this GUI (since it is very
		 * unlikely that we will want to record into one of the controllers created by the proxy).
		 * I'll comment it out for the time being:
		 * TODO: remove once we're convinced it's really unnecessary.
		 */
        /*try
        {
            Class addToTree =
                Class.forName("org.apache.jmeter.gui.action.AddToTree");
            Class remove = Class.forName("org.apache.jmeter.gui.action.Remove");
            ActionListener listener = new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    reinitializeTargetCombo();
                }
            };
            ActionRouter ar = ActionRouter.getInstance();
            ar.addPostActionListener(addToTree, listener);
            ar.addPostActionListener(remove, listener);
        }
        catch (ClassNotFoundException e)
        {
            // This should never happen -- throw an Error:
            throw new Error(e.toString());//JDK1.4: remove .toString()
        }*/
        
        return panel;        
    }

    private JPanel createGroupingPanel()
    {
        DefaultComboBoxModel m= new DefaultComboBoxModel();
        // Note: position of these elements in the menu *must* match the
        // corresponding ProxyControl.GROUPING_* values.
        m.addElement(JMeterUtils.getResString("grouping_no_groups"));
        m.addElement(JMeterUtils.getResString("grouping_add_separators"));
        m.addElement(JMeterUtils.getResString("grouping_in_controllers"));
        m.addElement(JMeterUtils.getResString("grouping_store_first_only"));
        groupingMode = new JComboBox(m);
        groupingMode.setName(ProxyControl.GROUPING_MODE);
        groupingMode.setSelectedIndex(0);
        groupingMode.addActionListener(this);
        groupingMode.setActionCommand(ProxyControl.GROUPING_MODE);

        JLabel label2 = new JLabel(JMeterUtils.getResString("grouping_mode"));
        label2.setLabelFor(groupingMode);
        
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(label2);
        panel.add(groupingMode);
        
        return panel;        
    }

    private JPanel createIncludePanel()
    {
        includeModel = new PowerTableModel(
                new String[] { INCLUDE_COL },
                new Class[] { String.class });
        includeTable = new JTable(includeModel);
        includeTable.setPreferredScrollableViewportSize(new Dimension(100, 50));
        includeTable.addFocusListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("patterns_to_include")));

        panel.add(new JScrollPane(includeTable), BorderLayout.CENTER);
        panel.add(createTableButtonPanel(ADD_INCLUDE, DELETE_INCLUDE),
            BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createExcludePanel()
    {
        excludeModel = new PowerTableModel(
            new String[] { EXCLUDE_COL },
            new Class[] { String.class });
        excludeTable = new JTable(excludeModel);
        excludeTable.setPreferredScrollableViewportSize(new Dimension(100, 50));
        excludeTable.addFocusListener(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("patterns_to_exclude")));

        panel.add(new JScrollPane(excludeTable), BorderLayout.CENTER);
        panel.add(createTableButtonPanel(ADD_EXCLUDE, DELETE_EXCLUDE),
            BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTableButtonPanel(
        String addCommand,
        String deleteCommand)
    {
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton(JMeterUtils.getResString("add"));
        addButton.setActionCommand(addCommand);
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton(JMeterUtils.getResString("delete"));
        deleteButton.setActionCommand(deleteCommand);
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    private void reinitializeTargetCombo() {
        log.debug("Reinitializing target combo");

        // Stop action notifications while we shuffle this around:
        targetNodes.removeActionListener(this);
        
        targetNodesModel.removeAllElements();
        GuiPackage gp = GuiPackage.getInstance();
        JMeterTreeNode root;
        if (gp != null)
        {
            root =
                (JMeterTreeNode) GuiPackage
                    .getInstance()
                    .getTreeModel()
                    .getRoot();
            targetNodesModel.addElement(
                new TreeNodeWrapper(
                    null,
                    JMeterUtils.getResString("use_recording_controller")));
            buildNodesModel(root, "", 0);
        }
        TreeNodeWrapper choice = null;
        for (int i = 0; i < targetNodesModel.getSize(); i++)
        {
            choice = (TreeNodeWrapper) targetNodesModel.getElementAt(i);
            log.debug("Selecting item "+choice+" for model "+model+" in "+this);
            if (choice.getTreeNode() == model.getTarget()) // .equals caused NPE
            {
                break;
            }
        }
        // Reinstate action notifications:
        targetNodes.addActionListener(this);
        // Set the current value:
        targetNodesModel.setSelectedItem(choice);        
        
        log.debug("Reinitialization complete");
    }
    
    private void buildNodesModel(
        JMeterTreeNode node,
        String parent_name,
        int level)
    {
        String seperator = " > ";
        if (node != null)
        {
            for (int i = 0; i < node.getChildCount(); i++)
            {
                StringBuffer name = new StringBuffer();
                JMeterTreeNode cur = (JMeterTreeNode) node.getChildAt(i);
                TestElement te = cur.createTestElement();
                if (te instanceof ThreadGroup)
                {
                    name.append(parent_name);
                    name.append(cur.getName());
                    name.append(seperator);
                    buildNodesModel(cur, name.toString(), level);
                }
                else if (te instanceof Controller)
                {
                    name.append(spaces(level));
                    name.append(parent_name);
                    name.append(cur.getName());
                    TreeNodeWrapper tnw =
                        new TreeNodeWrapper(cur, name.toString());
                    targetNodesModel.addElement(tnw);
                    name = new StringBuffer();
                    name.append(cur.getName());
                    name.append(seperator);
                    buildNodesModel(cur, name.toString(), level + 1);
                }
                else if (te instanceof TestPlan || te instanceof WorkBench)
                {
                    name.append(cur.getName());
                    name.append(seperator);
                    buildNodesModel(cur, name.toString(), 0);
                }
            }
        }
    }

    private String spaces(int level)
    {
        int multi = 4;
        StringBuffer spaces = new StringBuffer(level * multi);
        for (int i = 0; i < level * multi; i++)
        {
            spaces.append(" ");
        }
        return spaces.toString();
    }
    
    public void setNode(JMeterTreeNode node)
    {
        getNamePanel().setNode(node);
    }
}

class TreeNodeWrapper
{
    private JMeterTreeNode tn;
    private String label;

    private TreeNodeWrapper()
    {
    };

    public TreeNodeWrapper(JMeterTreeNode tn, String label)
    {
        this.tn = tn;
        this.label = label;
    }

    public JMeterTreeNode getTreeNode()
    {
        return tn;
    }

    public String toString()
    {
        return label;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3599.java