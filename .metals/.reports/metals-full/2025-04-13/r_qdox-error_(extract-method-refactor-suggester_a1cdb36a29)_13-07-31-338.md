error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2676.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2676.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2676.java
text:
```scala
t@@reeSelectionModel.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

/*
 * $Id$ $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.tree;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import wicket.markup.html.panel.Panel;

/**
 * Base component for trees. The trees from this package work with the Swing
 * tree models and {@link javax.swing.tree.DefaultMutableTreeNode}s. Hence,
 * users can re-use their Swing tree models.
 * 
 * @author Eelco Hillenius
 */
public abstract class AbstractTree extends Panel
{
	/** AbstractTree state for this component. */
	private TreeState treeState;

	/**
	 * Construct using the given model as the tree model to use. A new tree
	 * state will be constructed by calling newTreeState.
	 * 
	 * @param id
	 *            The id of this component
	 * @param model
	 *            the underlying tree model
	 */
	public AbstractTree(final String id, final TreeModel model)
	{
		super(id);
		this.treeState = newTreeState(model);
	}

	/**
	 * Construct using the given tree state that holds the model to be
	 * used as the tree model.
	 * 
	 * @param id
	 *            The id of this component
	 * @param treeState
	 *            treeState that holds the underlying tree model
	 */
	public AbstractTree(final String id, final TreeState treeState)
	{
		super(id);
		this.treeState = treeState;
	}

	/**
	 * Gets the current tree state.
	 * 
	 * @return the tree current tree state
	 */
	public final TreeState getTreeState()
	{
		return treeState;
	}

	/**
	 * Convenience method that determines whether the path of the given tree
	 * node is expanded in this tree's state.
	 * 
	 * @param node
	 *            the tree node
	 * @return whether the path of the given tree node is expanded
	 */
	public final boolean isExpanded(DefaultMutableTreeNode node)
	{
		return isExpanded(new TreePath(node.getPath()));
	}

	/**
	 * Convenience method that determines whether the given path is expanded in
	 * this tree's state.
	 * 
	 * @param path
	 *            the tree path
	 * @return whether the given path is expanded
	 */
	public final boolean isExpanded(TreePath path)
	{
		return treeState.isExpanded(path);
	}

	/**
	 * Gets whether the tree root node should be displayed.
	 * 
	 * @return whether the tree root node should be displayed
	 */
	public final boolean isRootVisible()
	{
		return treeState.isRootVisible();
	}

	/**
	 * Creates a new tree state by creating a new {@link TreeState}object,
	 * which is then set as the current tree state, creating a new
	 * {@link TreeSelectionModel}and then calling setTreeModel with this
	 * 
	 * @param model
	 *            the model that the new tree state applies to
	 * @return the tree state
	 */
	public TreeState newTreeState(final TreeModel model)
	{
		return newTreeState(model, true);
	}

	/**
	 * Sets the new expanded state, based on the given node
	 * 
	 * @param node
	 *            the tree node model
	 */
	public final void setExpandedState(final DefaultMutableTreeNode node)
	{
		final TreePath selection = new TreePath(node.getPath());
		setExpandedState(selection, (!treeState.isExpanded(selection))); // inverse
	}

	/**
	 * Sets the expanded property in the stree state for selection.
	 * 
	 * @param selection
	 *            the selection to set the expanded property for
	 * @param expanded
	 *            true if the selection is expanded, false otherwise
	 */
	public final void setExpandedState(final TreePath selection, final boolean expanded)
	{
		treeState.setExpandedState(selection, expanded);
	}

	/**
	 * Sets whether the tree root node should be displayed.
	 * 
	 * @param rootVisible
	 *            whether the tree node should be displayed
	 */
	public final void setRootVisible(final boolean rootVisible)
	{
		treeState.setRootVisible(rootVisible);
	}

	/**
	 * Sets the new expanded state (to true), based on the given user node and
	 * set the tree path to the currently selected.
	 * 
	 * @param node
	 *            the tree node model
	 */
	public final void setSelected(final DefaultMutableTreeNode node)
	{
		final TreePath selection = new TreePath(node.getPath());
		treeState.setSelectedPath(selection);
		setExpandedState(selection, true);
	}

	/**
	 * Sets the current tree state to the given tree state.
	 * 
	 * @param treeState
	 *            the tree state to set as the current one
	 */
	public final void setTreeState(final TreeState treeState)
	{
		this.treeState = treeState;
	}

	/**
	 * Gives the current tree model as a string.
	 * 
	 * @return the current tree model as a string
	 */
	public final String toString()
	{
		StringBuffer b = new StringBuffer("-- TREE MODEL --\n");
		TreeState state = getTreeState();
		TreeModel treeModel = null;
		if (state != null)
		{
			treeModel = state.getModel();
		}
		if (treeModel != null)
		{
			StringBuffer tabs = new StringBuffer();
			DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeModel
					.getRoot();
			Enumeration e = rootNode.preorderEnumeration();
			while (e.hasMoreElements())
			{
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
				tabs.delete(0, tabs.length());
				tabs.append("|");
				for (int i = 0; i < node.getLevel(); i++)
				{
					tabs.append("-");
				}
				b.append(tabs).append(node).append("\n");
			}
		}
		else
		{
			b.append("<EMPTY>");
		}
		return b.toString();
	}

	/**
	 * Creates a new tree state by creating a new {@link TreeState}object,
	 * which is then set as the current tree state, creating a new
	 * {@link TreeSelectionModel}and then calling setTreeModel with this
	 * 
	 * @param treeModel
	 *            the model that the new tree state applies to
	 * @param rootVisible
	 *            whether the tree node should be displayed
	 * @return the tree state
	 */
	protected final TreeState newTreeState(final TreeModel treeModel, final boolean rootVisible)
	{
		final TreeState treeState = new TreeState();
		final TreeSelectionModel treeSelectionModel = new DefaultTreeSelectionModel();
		treeSelectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeState.setModel(treeModel);
		treeState.setSelectionModel(treeSelectionModel);
		treeState.setRootVisible(rootVisible);
		treeModel.addTreeModelListener(treeState);
		return treeState;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2676.java