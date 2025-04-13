error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12830.java
text:
```scala
l@@.allNodesExpanded();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.tree.TreeNode;

import org.apache.wicket.IClusterable;

/**
 * Default implementation of TreeState.
 * <p>
 * This implementation tries to be as lightweight as possible. By default all nodes are collapsed.
 * 
 * @author Matej Knopp
 */
public class DefaultTreeState implements ITreeState, IClusterable
{
	private static final long serialVersionUID = 1L;

	/** Whether multiple selections can be done. */
	private boolean allowSelectMultiple = false;

	/** Tree state listeners. */
	private final List listeners = new ArrayList(1);

	/**
	 * set of nodes which are collapsed or expanded (depends on nodesCollapsed variable).
	 */
	private final Set nodes = new HashSet();

	/** Whether the nodes set should be treated as set of collapsed or expanded nodes. */
	private boolean nodesCollapsed = false; // by default treat the node set as expanded nodes

	/** Set selected nodes. */
	private final Set selectedNodes = new HashSet();

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#addTreeStateListener(org.apache.wicket.markup.html.tree.ITreeStateListener)
	 */
	public void addTreeStateListener(ITreeStateListener l)
	{
		if (listeners.contains(l) == false)
		{
			listeners.add(l);
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#collapseAll()
	 */
	public void collapseAll()
	{
		if (nodes.isEmpty() && nodesCollapsed == false)
		{
			// all nodes are already collapsed, do nothing
		}
		else
		{
			// clear all nodes from the set and sets the nodes as expanded
			nodes.clear();
			nodesCollapsed = false;

			Object[] listenersCopy = listeners.toArray();
			for (int i = 0; i < listenersCopy.length; i++)
			{
				ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
				l.allNodesCollapsed();
			}
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#collapseNode(javax.swing.tree.TreeNode)
	 */
	public void collapseNode(TreeNode node)
	{
		if (nodesCollapsed == true)
		{
			nodes.add(node);
		}
		else
		{
			nodes.remove(node);
		}

		Object[] listenersCopy = listeners.toArray();
		for (int i = 0; i < listenersCopy.length; i++)
		{
			ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
			l.nodeCollapsed(node);
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#expandAll()
	 */
	public void expandAll()
	{
		if (nodes.isEmpty() && nodesCollapsed == true)
		{
			// all nodes are already expanded, do nothing
		}
		else
		{
			// clear node set and set nodes policy as collapsed
			nodes.clear();
			nodesCollapsed = true;

			Object[] listenersCopy = listeners.toArray();
			for (int i = 0; i < listenersCopy.length; i++)
			{
				ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
				l.allNodesCollapsed();
			}
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#expandNode(javax.swing.tree.TreeNode)
	 */
	public void expandNode(TreeNode node)
	{
		if (nodesCollapsed == false)
		{
			nodes.add(node);
		}
		else
		{
			nodes.remove(node);
		}

		Object[] listenersCopy = listeners.toArray();
		for (int i = 0; i < listenersCopy.length; i++)
		{
			ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
			l.nodeExpanded(node);
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#getSelectedNodes()
	 */
	public Collection getSelectedNodes()
	{
		return Collections.unmodifiableList(new ArrayList(selectedNodes));
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#isAllowSelectMultiple()
	 */
	public boolean isAllowSelectMultiple()
	{
		return allowSelectMultiple;
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#isNodeExpanded(javax.swing.tree.TreeNode)
	 */
	public boolean isNodeExpanded(TreeNode node)
	{
		if (nodesCollapsed == false)
		{
			return nodes.contains(node);
		}
		else
		{
			return nodes.contains(node) == false;
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#isNodeSelected(javax.swing.tree.TreeNode)
	 */
	public boolean isNodeSelected(TreeNode node)
	{
		return selectedNodes.contains(node);
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#removeTreeStateListener(org.apache.wicket.markup.html.tree.ITreeStateListener)
	 */
	public void removeTreeStateListener(ITreeStateListener l)
	{
		listeners.remove(l);
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#selectNode(javax.swing.tree.TreeNode,
	 *      boolean)
	 */
	public void selectNode(TreeNode node, boolean selected)
	{
		if (isAllowSelectMultiple() == false && selectedNodes.size() > 0)
		{
			for (Iterator i = selectedNodes.iterator(); i.hasNext();)
			{
				TreeNode current = (TreeNode)i.next();
				if (current.equals(node) == false)
				{
					i.remove();
					Object[] listenersCopy = listeners.toArray();
					for (int j = 0; j < listenersCopy.length; j++)
					{
						ITreeStateListener l = (ITreeStateListener)listenersCopy[j];
						l.nodeUnselected(current);
					}
				}
			}
		}

		if (selected == true && selectedNodes.contains(node) == false)
		{

			selectedNodes.add(node);
			Object[] listenersCopy = listeners.toArray();
			for (int i = 0; i < listenersCopy.length; i++)
			{
				ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
				l.nodeSelected(node);
			}
		}
		else if (selected == false && selectedNodes.contains(node) == true)
		{
			selectedNodes.remove(node);
			Object[] listenersCopy = listeners.toArray();
			for (int i = 0; i < listenersCopy.length; i++)
			{
				ITreeStateListener l = (ITreeStateListener)listenersCopy[i];
				l.nodeUnselected(node);
			}
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.tree.ITreeState#setAllowSelectMultiple(boolean)
	 */
	public void setAllowSelectMultiple(boolean value)
	{
		allowSelectMultiple = value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12830.java