error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[117,2]

error in qdox parser
file content:
```java
offset: 3401
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8173.java
text:
```scala
import org.apache.jmeter.report.gui.action.AbstractAction;

/*
 * Copyright 2005 The Apache Software Foundation.
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

package org.apache.jmeter.report.gui.action;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.jmeter.gui.ReportGuiPackage;
import org.apache.jmeter.gui.action.AbstractAction;
import org.apache.jmeter.report.gui.tree.ReportTreeListener;
import org.apache.jmeter.report.gui.tree.ReportTreeNode;
import org.apache.jmeter.testelement.TestElement;

/**
 * @author Peter Lin
 * @version $Revision$
 */
public class ReportCopy extends AbstractAction {
	private static ReportTreeNode copiedNode = null;

	private static ReportTreeNode copiedNodes[] = null;

	private static String COPY = "Copy";

	private static HashSet commands = new HashSet();
	static {
		commands.add(COPY);
	}

	/*
	 * @see org.apache.jmeter.report.gui.action.Command#getActionNames()
	 */
	public Set getActionNames() {
		return commands;
	}

	public void doAction(ActionEvent e) {
		ReportTreeListener treeListener = ReportGuiPackage.getInstance()
				.getTreeListener();
		ReportTreeNode[] nodes = treeListener.getSelectedNodes();
		setCopiedNodes(nodes);
	}

	public static ReportTreeNode[] getCopiedNodes() {
		for (int i = 0; i < copiedNodes.length; i++) {
			if (copiedNodes[i] == null) {
				return null;
			}
		}
		return cloneTreeNodes(copiedNodes);
	}

	public static ReportTreeNode getCopiedNode() {
		if (copiedNode == null) {
			return null;
		}
		return cloneTreeNode(copiedNode);
	}

	public static void setCopiedNode(ReportTreeNode node) {
		copiedNode = cloneTreeNode(node);
	}

	public static ReportTreeNode cloneTreeNode(ReportTreeNode node) {
		ReportTreeNode treeNode = (ReportTreeNode) node.clone();
		treeNode.setUserObject(((TestElement) node.getUserObject()).clone());
		cloneChildren(treeNode, node);
		return treeNode;
	}

	public static void setCopiedNodes(ReportTreeNode nodes[]) {
		copiedNodes = new ReportTreeNode[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			copiedNodes[i] = cloneTreeNode(nodes[i]);
		}
	}

	public static ReportTreeNode[] cloneTreeNodes(ReportTreeNode nodes[]) {
		ReportTreeNode treeNodes[] = new ReportTreeNode[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			treeNodes[i] = cloneTreeNode(nodes[i]);
		}
		return treeNodes;
	}

	private static void cloneChildren(ReportTreeNode to, ReportTreeNode from) {
		Enumeration enumFrom = from.children();
		while (enumFrom.hasMoreElements()) {
			ReportTreeNode child = (ReportTreeNode) enumFrom.nextElement();
			ReportTreeNode childClone = (ReportTreeNode) child.clone();
			childClone.setUserObject(((TestElement) child.getUserObject())
					.clone());
			to.add(childClone);
			cloneChildren((ReportTreeNode) to.getLastChild(), child);
		}
	}
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8173.java