error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15472.java
text:
```scala
s@@etLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));

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

package org.apache.jmeter.control.gui;

import java.awt.FlowLayout;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.ModuleController;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.action.ActionNames;
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
 */
public class ModuleControllerGui extends AbstractControllerGui /*
																 * implements
																 * UnsharedComponent
																 */
{

	private JMeterTreeNode selected = null;

	private JComboBox nodes;

	private DefaultComboBoxModel nodesModel;

	private JLabel warningLabel;

	/**
	 * Initializes the gui panel for the ModuleController instance.
	 */
	public ModuleControllerGui() {
		init();
	}

	public String getLabelResource() {
		return "module_controller_title"; // $NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
	 */
	public void configure(TestElement el) {
		super.configure(el);
		ModuleController controller = (ModuleController) el;
		this.selected = controller.getSelectedNode();
		if (selected == null && controller.getNodePath() != null) {
			warningLabel.setText(JMeterUtils.getResString("module_controller_warning") // $NON-NLS-1$
					+ renderPath(controller.getNodePath()));
		} else {
			warningLabel.setText(""); // $NON-NLS-1$
		}
		reinitialize();
	}

	private String renderPath(Collection path) {
		Iterator iter = path.iterator();
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		while (iter.hasNext()) {
			if (first) {
				first = false;
				iter.next();
				continue;
			}
			buf.append(iter.next());
			if (iter.hasNext()) {
				buf.append(" > "); // $NON-NLS-1$
			}
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		ModuleController mc = new ModuleController();
		configureTestElement(mc);
		if (selected != null) {
			mc.setSelectedNode(selected);
		}
		return mc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement element) {
		configureTestElement(element);
		TreeNodeWrapper tnw = (TreeNodeWrapper) nodesModel.getSelectedItem();
		if (tnw != null && tnw.getTreeNode() != null) {
			selected = tnw.getTreeNode();
			if (selected != null) {
				((ModuleController) element).setSelectedNode(selected);
			}
		}
	}
    
    /**
     * Implements JMeterGUIComponent.clear
     */
    public void clear() {
        super.clear();

        nodes.setSelectedIndex(-1);
        selected = null;
    }
    

	public JPopupMenu createPopupMenu() {
		JPopupMenu menu = new JPopupMenu();
		JMenu addMenu = MenuFactory.makeMenus(
				new String[] {
						MenuFactory.CONFIG_ELEMENTS, 
						MenuFactory.ASSERTIONS,
						MenuFactory.TIMERS, 
						MenuFactory.LISTENERS, 
				}, 
				JMeterUtils.getResString("add"),  // $NON-NLS-1$
				ActionNames.ADD);
		menu.add(addMenu);
		MenuFactory.addEditMenu(menu, true);
		MenuFactory.addFileMenu(menu);
		return menu;
	}

	private void init() {
		setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
		setBorder(makeBorder());
		add(makeTitlePanel());

		// DROP-DOWN MENU
		JPanel modulesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
		JLabel nodesLabel = new JLabel(JMeterUtils.getResString("module_controller_module_to_run")); // $NON-NLS-1$
		modulesPanel.add(nodesLabel);
		nodesModel = new DefaultComboBoxModel();
		nodes = new JComboBox(nodesModel);
        nodesLabel.setLabelFor(nodes);
		reinitialize();
		modulesPanel.add(nodes);
		warningLabel = new JLabel(""); // $NON-NLS-1$
		modulesPanel.add(warningLabel);
		add(modulesPanel);
	}

	private void reinitialize() {
		TreeNodeWrapper current;
		nodesModel.removeAllElements();
		GuiPackage gp = GuiPackage.getInstance();
		JMeterTreeNode root;
		if (gp != null) {
			root = (JMeterTreeNode) GuiPackage.getInstance().getTreeModel().getRoot();
			buildNodesModel(root, "", 0); // $NON-NLS-1$
		}
		if (selected != null) {
			for (int i = 0; i < nodesModel.getSize(); i++) {
				current = (TreeNodeWrapper) nodesModel.getElementAt(i);
				if ((current.getTreeNode() == null && selected == null)
 (current.getTreeNode() != null && current.getTreeNode().equals(selected))) {
					nodesModel.setSelectedItem(current);
					break;
				}
			}
		}
	}

	private void buildNodesModel(JMeterTreeNode node, String parent_name, int level) {
		if (level == 0 && (parent_name == null || parent_name.length() == 0)) {
			nodesModel.addElement(new TreeNodeWrapper(null, "")); // $NON-NLS-1$
		}
		String seperator = " > "; // $NON-NLS-1$
		if (node != null) {
			for (int i = 0; i < node.getChildCount(); i++) {
				StringBuffer name = new StringBuffer();
				JMeterTreeNode cur = (JMeterTreeNode) node.getChildAt(i);
				TestElement te = cur.getTestElement();
				if (te instanceof ThreadGroup) {
					name.append(parent_name);
					name.append(cur.getName());
					name.append(seperator);
					buildNodesModel(cur, name.toString(), level);
				} else if (te instanceof Controller && !(te instanceof ModuleController)) {
					name.append(spaces(level));
					name.append(parent_name);
					name.append(cur.getName());
					TreeNodeWrapper tnw = new TreeNodeWrapper(cur, name.toString());
					nodesModel.addElement(tnw);
					name = new StringBuffer();
					name.append(cur.getName());
					name.append(seperator);
					buildNodesModel(cur, name.toString(), level + 1);
				} else if (te instanceof TestPlan || te instanceof WorkBench) {
					name.append(cur.getName());
					name.append(seperator);
					buildNodesModel(cur, name.toString(), 0);
				}
			}
		}
	}

	private String spaces(int level) {
		int multi = 4;
		StringBuffer spaces = new StringBuffer(level * multi);
		for (int i = 0; i < level * multi; i++) {
			spaces.append(" "); // $NON-NLS-1$
		}
		return spaces.toString();
	}
}

class TreeNodeWrapper {

	private JMeterTreeNode tn;

	private String label;

	private TreeNodeWrapper() {
	};

	public TreeNodeWrapper(JMeterTreeNode tn, String label) {
		this.tn = tn;
		this.label = label;
	}

	public JMeterTreeNode getTreeNode() {
		return tn;
	}

	public String toString() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15472.java