error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6645.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6645.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6645.java
text:
```scala
private transient J@@MeterTreeNode selectedNode = null;

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

package org.apache.jmeter.control;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.NullProperty;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;

/**
 * The goal of ModuleController is to add modularity to JMeter. The general idea
 * is that web applications consist of small units of functionality (i.e. Logon,
 * Create Account, Logoff...) which consist of requests that implement the
 * functionality. These small units of functionality can be stored in
 * SimpleControllers as modules that can be linked together quickly to form
 * tests. ModuleController facilitates this by acting as a pointer to any
 * controller that sits under the WorkBench. The controller and it's subelements
 * will be substituted in place of the ModuleController at runtime. Config
 * elements can be attached to the ModuleController to alter the functionality
 * (which user logs in, which account is created, etc.) of the module.
 *
 */
public class ModuleController extends GenericController implements ReplaceableController {

    private static final long serialVersionUID = 240L;

    private static final String NODE_PATH = "ModuleController.node_path";// $NON-NLS-1$

    private JMeterTreeNode selectedNode = null;

    /**
     * No-arg constructor
     *
     * @see java.lang.Object#Object()
     */
    public ModuleController() {
        super();
    }

    @Override
    public Object clone() {
        ModuleController clone = (ModuleController) super.clone();
        if (selectedNode == null) {
            this.restoreSelected();
        }
        clone.selectedNode = selectedNode; // TODO ?? (JMeterTreeNode) selectedNode.clone();
        return clone;
    }

    /**
     * Sets the (@link JMeterTreeNode) which represents the controller which
     * this object is pointing to. Used for building the test case upon
     * execution.
     *
     * @param tn
     *            JMeterTreeNode
     * @see org.apache.jmeter.gui.tree.JMeterTreeNode
     */
    public void setSelectedNode(JMeterTreeNode tn) {
        selectedNode = tn;
        setNodePath();
    }

    /**
     * Gets the (@link JMeterTreeNode) for the Controller
     *
     * @return JMeterTreeNode
     */
    public JMeterTreeNode getSelectedNode() {
        if (selectedNode == null){
            restoreSelected();
        }
        return selectedNode;
    }

    private void setNodePath() {
        List<String> nodePath = new ArrayList<String>();
        if (selectedNode != null) {
            TreeNode[] path = selectedNode.getPath();
            for (int i = 0; i < path.length; i++) {
                nodePath.add(((JMeterTreeNode) path[i]).getName());
            }
            // nodePath.add(selectedNode.getName());
        }
        setProperty(new CollectionProperty(NODE_PATH, nodePath));
    }

    public List<?> getNodePath() {
        JMeterProperty prop = getProperty(NODE_PATH);
        if (!(prop instanceof NullProperty)) {
            return (List<?>) ((CollectionProperty) prop).getObjectValue();
        }
        return null;
    }

    private void restoreSelected() {
        GuiPackage gp = GuiPackage.getInstance();
        if (gp != null) {
            JMeterTreeNode root = (JMeterTreeNode) gp.getTreeModel().getRoot();
            resolveReplacementSubTree(root);
        }
    }

    /**
     * Compute the replacement tree.
     * @param context
     */
    public void resolveReplacementSubTree(JMeterTreeNode context) {
        if (selectedNode == null) {
            List<?> nodePathList = getNodePath();
            if (nodePathList != null && nodePathList.size() > 0) {
                traverse(context, nodePathList, 1);
            }
        }
    }

    private void traverse(JMeterTreeNode node, List<?> nodePath, int level) {
        if (node != null && nodePath.size() > level) {
            for (int i = 0; i < node.getChildCount(); i++) {
                JMeterTreeNode cur = (JMeterTreeNode) node.getChildAt(i);
                if (cur.getName().equals(nodePath.get(level).toString())) {
                    if (nodePath.size() == (level + 1)) {
                        selectedNode = cur;
                    }
                    traverse(cur, nodePath, level + 1);
                }
            }
        }
    }

    /**
     * Copies the controller's subelements into the execution tree
     *
     */
    public HashTree getReplacementSubTree() {
        HashTree tree = new ListedHashTree();
        if (selectedNode != null) {
            if (!selectedNode.isEnabled()) {
                selectedNode = cloneTreeNode(selectedNode);
                selectedNode.setEnabled(true);
            }
            HashTree subtree = tree.add(selectedNode);
            createSubTree(subtree, selectedNode);
        }
        return tree;
    }

    private void createSubTree(HashTree tree, JMeterTreeNode node) {
        Enumeration<JMeterTreeNode> e = node.children();
        while (e.hasMoreElements()) {
            JMeterTreeNode subNode = e.nextElement();
            tree.add(subNode);
            createSubTree(tree.getTree(subNode), subNode);
        }
    }

    private static JMeterTreeNode cloneTreeNode(JMeterTreeNode node) {
        JMeterTreeNode treeNode = (JMeterTreeNode) node.clone();
        treeNode.setUserObject(((TestElement) node.getUserObject()).clone());
        cloneChildren(treeNode, node);
        return treeNode;
    }

    private static void cloneChildren(JMeterTreeNode to, JMeterTreeNode from) {
        Enumeration<JMeterTreeNode> enumr = from.children();
        while (enumr.hasMoreElements()) {
            JMeterTreeNode child = enumr.nextElement();
            JMeterTreeNode childClone = (JMeterTreeNode) child.clone();
            childClone.setUserObject(((TestElement) child.getUserObject()).clone());
            to.add(childClone);
            cloneChildren((JMeterTreeNode) to.getLastChild(), child);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6645.java