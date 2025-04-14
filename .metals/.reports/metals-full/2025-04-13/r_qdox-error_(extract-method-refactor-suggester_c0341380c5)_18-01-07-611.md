error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12767.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12767.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12767.java
text:
```scala
r@@esult = new CompareAssertionResult(getName());

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
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.assertions.CompareAssertionResult;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

public class ComparisonVisualizer extends AbstractVisualizer implements Clearable {
    private JTree resultsTree;

    private DefaultTreeModel treeModel;

    private DefaultMutableTreeNode root;

    private JTextPane base, secondary;

    public ComparisonVisualizer() {
        super();
        init();
    }

    public void add(SampleResult sample) {

        DefaultMutableTreeNode currNode = new DefaultMutableTreeNode(sample);
        treeModel.insertNodeInto(currNode, root, root.getChildCount());
        if (root.getChildCount() == 1) {
            resultsTree.expandPath(new TreePath(root));
        }
    }

    public String getLabelResource() {
        return "comparison_visualizer_title"; //$NON-NLS-1$
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.add(getTreePanel());
        split.add(getSideBySidePanel());
        add(split, BorderLayout.CENTER);
    }

    private JComponent getSideBySidePanel() {
        JPanel main = new JPanel(new GridLayout(1, 2));
        JScrollPane base = new JScrollPane(getBaseTextPane());
        base.setPreferredSize(base.getMinimumSize());
        JScrollPane secondary = new JScrollPane(getSecondaryTextPane());
        secondary.setPreferredSize(secondary.getMinimumSize());
        main.add(base);
        main.add(secondary);
        main.setPreferredSize(main.getMinimumSize());
        return main;
    }

    private JTextPane getBaseTextPane() {
        base = new JTextPane();
        base.setEditable(false);
        base.setBackground(getBackground());
        return base;
    }

    private JTextPane getSecondaryTextPane() {
        secondary = new JTextPane();
        secondary.setEditable(false);
        return secondary;
    }

    private JComponent getTreePanel() {
        root = new DefaultMutableTreeNode("Root"); //$NON-NLS-1$
        treeModel = new DefaultTreeModel(root);
        resultsTree = new JTree(treeModel);
        resultsTree.setCellRenderer(new TreeNodeRenderer());
        resultsTree.setCellRenderer(new TreeNodeRenderer());
        resultsTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        resultsTree.addTreeSelectionListener(new Selector());
        resultsTree.setRootVisible(false);
        resultsTree.setShowsRootHandles(true);

        JScrollPane treePane = new JScrollPane(resultsTree);
        treePane.setPreferredSize(new Dimension(150, 50));
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.add(treePane);
        return panel;
    }

    private class Selector implements TreeSelectionListener {
        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
         */
        public void valueChanged(TreeSelectionEvent e) {
            try {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) resultsTree.getLastSelectedPathComponent();
                SampleResult sr = (SampleResult) node.getUserObject();
                AssertionResult[] results = sr.getAssertionResults();
                CompareAssertionResult result = null;
                for (AssertionResult r : results) {
                    if (r instanceof CompareAssertionResult) {
                        result = (CompareAssertionResult) r;
                        break;
                    }
                }
                if (result == null)
                    result = new CompareAssertionResult();
                base.setText(result.getBaseResult());
                secondary.setText(result.getSecondaryResult());
            } catch (Exception err) {
                base.setText(JMeterUtils.getResString("comparison_invalid_node") + err); //$NON-NLS-1$
                secondary.setText(JMeterUtils.getResString("comparison_invalid_node") + err); //$NON-NLS-1$
            }
            base.setCaretPosition(0);
            secondary.setCaretPosition(0);
        }
    }

    public void clearData() {
        while (root.getChildCount() > 0) {
            // the child to be removed will always be 0 'cos as the nodes are
            // removed the nth node will become (n-1)th
            treeModel.removeNodeFromParent((DefaultMutableTreeNode) root.getChildAt(0));
            base.setText(""); //$NON-NLS-1$
            secondary.setText(""); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12767.java