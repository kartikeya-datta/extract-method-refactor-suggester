error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3596.java
text:
```scala
T@@estElement te = cur.getTestElement();

// $Header$
/*
 * Copyright 2003-2004 The Apache Software Foundation.
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

package org.apache.jmeter.control.gui;

import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.ModuleController;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.WorkBench;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * ModuleController Gui.
 *
 * @author    Thad Smith
 * @version   $Revision$
 */
public class ModuleControllerGui
    extends AbstractControllerGui /*implements UnsharedComponent*/
{

    private JMeterTreeNode selected = null;

    private JComboBox nodes;
    private DefaultComboBoxModel nodesModel;

    public static final String CONTROLLER = "Module To Run";
    //TODO should be a resource, and probably ought to be resolved at run-time (to allow language change)

    /**
     * Initializes the gui panel for the ModuleController instance.
     */
    public ModuleControllerGui()
    {
        init();
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
     */
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("module_controller_title");
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
     */
    public void configure(TestElement el)
    {
        super.configure(el);
        this.selected = ((ModuleController) el).getSelectedNode();
        reinitialize();
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement()
    {
        ModuleController mc = new ModuleController();
        configureTestElement(mc);
        if (selected != null)
        {
            mc.setSelectedNode(selected);
        }
        return mc;
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement element)
    {
        configureTestElement(element);
        TreeNodeWrapper tnw = (TreeNodeWrapper) nodesModel.getSelectedItem();
        if (tnw != null)
        {
            selected = tnw.getTreeNode();
            if (selected != null)
            {
                ((ModuleController) element).setSelectedNode(selected);
            }
        }
    }

    public JPopupMenu createPopupMenu()
    {
        JPopupMenu menu = new JPopupMenu();
        JMenu addMenu =
            MenuFactory.makeMenus(
                new String[] {
                    MenuFactory.CONFIG_ELEMENTS,
                    MenuFactory.ASSERTIONS,
                    MenuFactory.TIMERS,
                    MenuFactory.LISTENERS,
                    },
                JMeterUtils.getResString("Add"),
                "Add");
        menu.add(addMenu);
        MenuFactory.addEditMenu(menu, true);
        MenuFactory.addFileMenu(menu);
        return menu;
    }

    private void init()
    {
        setLayout(
            new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
        setBorder(makeBorder());
        add(makeTitlePanel());

        // DROP-DOWN MENU
        JPanel modulesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modulesPanel.add(new JLabel(CONTROLLER));
        nodesModel = new DefaultComboBoxModel();
        nodes = new JComboBox(nodesModel);
        reinitialize();
        
		/* This listener subscription prevents freeing up the GUI when it's no longer in use
		 * (e.g. on locale change)...
		 * ... plus I don't think it's really necessary: configure(TestElement) already takes
		 * care of reinitializing the target combo when we come back to it. And I can't see how
		 * the tree can change in a relevant way without we leaving this GUI.
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
                    reinitialize();
                }
            };
            ActionRouter ar = ActionRouter.getInstance();
            ar.addPostActionListener(addToTree, listener);
            ar.addPostActionListener(remove, listener);
        }
        catch (ClassNotFoundException e)
        {
        }*/
        modulesPanel.add(nodes);
        add(modulesPanel);
    }

    private void reinitialize()
    {
        TreeNodeWrapper current;
        nodesModel.removeAllElements();
        GuiPackage gp = GuiPackage.getInstance();
        JMeterTreeNode root;
        if (gp != null)
        {
            root =
                (JMeterTreeNode) GuiPackage
                    .getInstance()
                    .getTreeModel()
                    .getRoot();
            buildNodesModel(root, "", 0);
        }
        if (selected != null)
        {
            for (int i = 0; i < nodesModel.getSize(); i++)
            {
                current = (TreeNodeWrapper) nodesModel.getElementAt(i);
                if (current.getTreeNode().equals(selected))
                {
                    nodesModel.setSelectedItem(current);
                    break;
                }
            }
        }
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
                else if (
                    te instanceof Controller
                        && !(te instanceof ModuleController))
                {
                    name.append(spaces(level));
                    name.append(parent_name);
                    name.append(cur.getName());
                    TreeNodeWrapper tnw =
                        new TreeNodeWrapper(cur, name.toString());
                    nodesModel.addElement(tnw);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3596.java