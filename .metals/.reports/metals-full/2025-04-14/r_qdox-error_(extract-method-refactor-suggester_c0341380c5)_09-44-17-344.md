error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9266.java
text:
```scala
p@@ublic class DefaultTreeState implements ITreeState, Serializable

package wicket.xtree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.tree.TreeNode;

/**
 * Default implementation of TreeState. 
 * <p>
 * This implementation tries to be as lightweight as possible.
 * @author Matej Knopp
 */
public class DefaultTreeState implements TreeState, Serializable 
{
	// set of nodes which are collapsed or expanded (depends on nodesCollapsed veriable)
	private Set<TreeNode> nodes = new HashSet<TreeNode>();
	
	// wether the nodes set should be treated as collapsed or expanded
	private boolean nodesCollapsed = true;  
	
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
			
			for (TreeStateListener l : listeners)
			{
				l.allNodesCollapsed();
			}
		}
	}
	
	public void collapseNode(TreeNode node) 
	{
		if (nodesCollapsed == true)
			nodes.add(node);
		else
			nodes.remove(node);
		
		for (TreeStateListener l : listeners)
		{
			l.nodeCollapsed(node);
		}
	}

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
		
			for (TreeStateListener l : listeners)
				l.allNodesCollapsed();
		}
	}

	public void expandNode(TreeNode node) 
	{
		if (nodesCollapsed == false)
			nodes.add(node);
		else
			nodes.remove(node);
		
		for (TreeStateListener l : listeners)
			l.nodeExpanded(node);
	}

	public boolean isNodeExpanded(TreeNode node) 
	{
		if (nodesCollapsed == false)
			return nodes.contains(node);
		else
			return nodes.contains(node) == false;
	}
		
	private Set<TreeNode> selectedNodes = new HashSet<TreeNode>();
	
	public Collection<TreeNode> getSelectedNodes() 
	{
		return selectedNodes;
	}

	private boolean allowSelectMultiple = false;
	
	public boolean isAllowSelectMultiple() 
	{
		return allowSelectMultiple;
	}
	
	public void setAllowSelectMultiple(boolean value) 
	{
		this.allowSelectMultiple = value;
	}

	public boolean isNodeSelected(TreeNode node) 
	{
		return selectedNodes.contains(node);
	}	

	public void selectNode(TreeNode node, boolean selected) 
	{
		if (selected == true && selectedNodes.contains(node) == false)
		{
			if (isAllowSelectMultiple() == false && selectedNodes.size() > 0)
			{
				for (Iterator<TreeNode> i = selectedNodes.iterator(); i.hasNext(); )
				{
					TreeNode current = i.next();
					for (TreeStateListener l : listeners)
					{
						l.nodeUnselected(current);
					}
					i.remove();
				}					
			}
			selectedNodes.add(node);
			for (TreeStateListener l : listeners)
			{
				l.nodeSelected(node);
			}
		}
		else if (selected == false && selectedNodes.contains(node) == true)
		{
			selectedNodes.remove(node);
			for (TreeStateListener l : listeners)
			{
				l.nodeUnselected(node);
			}
		}
	}

	private List<TreeStateListener> listeners = new ArrayList<TreeStateListener>();
	
	public void addTreeStateListener(TreeStateListener l) 
	{
		if (listeners.contains(l) == false)
			listeners.add(l);
	}

	public void removeTreeStateListener(TreeStateListener l) 
	{
		listeners.remove(l);		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9266.java