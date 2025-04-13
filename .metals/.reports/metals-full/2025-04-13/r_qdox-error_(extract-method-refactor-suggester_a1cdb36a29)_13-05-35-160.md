error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18329.java
text:
```scala
private static final S@@et commands = new HashSet();

//$Header$
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

package org.apache.jmeter.report.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.report.gui.action.AbstractAction;
import org.apache.jmeter.report.gui.action.ReportActionRouter;
import org.apache.jmeter.report.gui.action.ReportExitCommand;
import org.apache.jmeter.report.gui.tree.ReportTreeNode;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.HashTreeTraverser;
import org.apache.jorphan.collections.ListedHashTree;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * @author Peter Lin
 * @version $Revision$
 */
public class ReportCheckDirty extends AbstractAction implements HashTreeTraverser, ActionListener {
	private static final Logger log = LoggingManager.getLoggerForClass();

	private static Map previousGuiItems;

	public static final String CHECK_DIRTY = "check_dirty";

	public static final String SUB_TREE_SAVED = "sub_tree_saved";

	public static final String SUB_TREE_LOADED = "sub_tree_loaded";

	public static final String ADD_ALL = "add_all";

	// Not implemented: public static final String SAVE = "save_as";
	// Not implemented: public static final String SAVE_ALL = "save_all";
	// Not implemented: public static final String SAVE_TO_PREVIOUS = "save";
	public static final String REMOVE = "check_remove";

	boolean checkMode = false;

	boolean removeMode = false;

	boolean dirty = false;

	private static Set commands = new HashSet();
	static {
		commands.add(CHECK_DIRTY);
		commands.add(SUB_TREE_SAVED);
		commands.add(SUB_TREE_LOADED);
		commands.add(ADD_ALL);
		commands.add(REMOVE);
	}

	public ReportCheckDirty() {
		previousGuiItems = new HashMap();
		ReportActionRouter.getInstance().addPreActionListener(ReportExitCommand.class, this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(ReportExitCommand.EXIT)) {
			doAction(e);
		}
	}

	/**
	 * @see org.apache.jmeter.gui.action.Command#doAction(ActionEvent)
	 */
	public void doAction(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals(SUB_TREE_SAVED)) {
			HashTree subTree = (HashTree) e.getSource();
			subTree.traverse(this);
		} else if (action.equals(SUB_TREE_LOADED)) {
			ListedHashTree addTree = (ListedHashTree) e.getSource();
			addTree.traverse(this);
		} else if (action.equals(ADD_ALL)) {
			previousGuiItems.clear();
			ReportGuiPackage.getInstance().getTreeModel().getReportPlan().traverse(this);
		} else if (action.equals(REMOVE)) {
			ReportGuiPackage guiPackage = ReportGuiPackage.getInstance();
			ReportTreeNode[] nodes = guiPackage.getTreeListener().getSelectedNodes();
			removeMode = true;
			for (int i = nodes.length - 1; i >= 0; i--) {
				guiPackage.getTreeModel().getCurrentSubTree(nodes[i]).traverse(this);
			}
			removeMode = false;
		}
		checkMode = true;
		dirty = false;
		HashTree wholeTree = ReportGuiPackage.getInstance().getTreeModel().getReportPlan();
		wholeTree.traverse(this);
		ReportGuiPackage.getInstance().setDirty(dirty);
		checkMode = false;
	}

	/**
	 * The tree traverses itself depth-first, calling processNode for each
	 * object it encounters as it goes.
	 */
	public void addNode(Object node, HashTree subTree) {
		log.debug("Node is class:" + node.getClass());
		ReportTreeNode treeNode = (ReportTreeNode) node;
		if (checkMode) {
			if (previousGuiItems.containsKey(treeNode)) {
				if (!previousGuiItems.get(treeNode).equals(treeNode.getTestElement())) {
					dirty = true;
				}
			} else {
				dirty = true;
			}
		} else if (removeMode) {
			previousGuiItems.remove(treeNode);
		} else {
			previousGuiItems.put(treeNode, treeNode.getTestElement().clone());
		}
	}

	/**
	 * Indicates traversal has moved up a step, and the visitor should remove
	 * the top node from it's stack structure.
	 */
	public void subtractNode() {
	}

	/**
	 * Process path is called when a leaf is reached. If a visitor wishes to
	 * generate Lists of path elements to each leaf, it should keep a Stack data
	 * structure of nodes passed to it with addNode, and removing top items for
	 * every subtractNode() call.
	 */
	public void processPath() {
	}

	/**
	 * @see org.apache.jmeter.gui.action.Command#getActionNames()
	 */
	public Set getActionNames() {
		return commands;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18329.java