error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9267.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9267.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9267.java
text:
```scala
p@@ublic interface ITreeState

package wicket.xtree;

import java.util.Collection;

import javax.swing.tree.TreeNode;

/**
 * Tree state holds information about a tree such as which nodes are 
 * expanded / collapsed and which nodes are selected,
 * It can also fire callbacks on listener in case any of the information changed.
 * @author Matej Knopp
 */
public interface TreeState  
{
	/**
	 * Expands all nodes of the tree.
	 */
	public void expandAll();
	
	/**
	 * Collapses all nodes of the tree.
	 */
	public void collapseAll();

	/**
	 * Expands the given node.
	 */
	public void expandNode(TreeNode node);

	/**
	 * Collapses the given node.
	 */
	public void collapseNode(TreeNode node);

	/**
	 * Returns true if the given node is expanded.
	 */
	public boolean isNodeExpanded(TreeNode node);
	
	

	/**
	 * Sets whether multiple nodes can be selected.
	 */
	public void setAllowSelectMultiple(boolean value);
	
	/**
	 * Returns whether multiple nodes can be selected.
	 */
	public boolean isAllowSelectMultiple();
	
	/**
	 * Marks given node as selected (or unselected) according to the
	 * selected value.
	 * <p>
	 * If tree is in single selection mode and a new node is selected,
	 * old node is automatically unselected (and the event is fired on listeners).
	 */
	public void selectNode(TreeNode node, boolean selected);
	
	/**
	 * Returns true if the given node is selected, false otherwise.
	 */
	public boolean isNodeSelected(TreeNode node);

	/**
	 * Returns the collection of all selected nodes.
	 */
	public Collection<TreeNode> getSelectedNodes();
	
	
	
	/**
	 * Adds a tree state listener. On state change events on the listener are fired.
	 */
	public void addTreeStateListener(TreeStateListener l);
	
	/**
	 * Removes a tree state listener. 
	 */
	public void removeTreeStateListener(TreeStateListener l);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9267.java