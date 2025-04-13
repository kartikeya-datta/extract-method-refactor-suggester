error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10897.java
text:
```scala
C@@ollection<Object> getSelectedNodes();

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

import java.util.Collection;

/**
 * Tree state holds information about a tree such as which nodes are expanded / collapsed and which
 * nodes are selected, It can also fire callbacks on listener in case any of the information
 * changed.
 * 
 * @author Matej Knopp
 */
public interface ITreeState
{
	/**
	 * Adds a tree state listener. On state change events on the listener are fired.
	 * 
	 * @param l
	 *            Listener to add
	 */
	void addTreeStateListener(ITreeStateListener l);

	/**
	 * Collapses all nodes of the tree.
	 */
	void collapseAll();

	/**
	 * Collapses the given node.
	 * 
	 * @param node
	 *            Node to collapse
	 */
	void collapseNode(Object node);

	/**
	 * Expands all nodes of the tree.
	 */
	void expandAll();

	/**
	 * Expands the given node.
	 * 
	 * @param node
	 *            Node to expand
	 */
	void expandNode(Object node);

	/**
	 * Returns the collection of all selected nodes.
	 * 
	 * @return The collection of selected nodes
	 */
	Collection getSelectedNodes();

	/**
	 * Returns whether multiple nodes can be selected.
	 * 
	 * @return True if multiple nodes can be selected
	 */
	boolean isAllowSelectMultiple();

	/**
	 * Returns true if the given node is expanded.
	 * 
	 * @param node
	 *            The node to inspect
	 * @return True if the node is expanded
	 */
	boolean isNodeExpanded(Object node);

	/**
	 * Returns true if the given node is selected, false otherwise.
	 * 
	 * @param node
	 *            The node to inspect
	 * @return True if the node is selected
	 */
	boolean isNodeSelected(Object node);

	/**
	 * Removes a tree state listener.
	 * 
	 * @param l
	 *            The listener to remove
	 */
	void removeTreeStateListener(ITreeStateListener l);


	/**
	 * Marks given node as selected (or unselected) according to the selected value.
	 * <p>
	 * If tree is in single selection mode and a new node is selected, old node is automatically
	 * unselected (and the event is fired on listeners).
	 * 
	 * @param node
	 *            The node to select or deselect
	 * @param selected
	 *            If true, the node will be selected, otherwise, the node will be unselected
	 */
	void selectNode(Object node, boolean selected);

	/**
	 * Sets whether multiple nodes can be selected.
	 * 
	 * @param value
	 *            If true, multiple nodes can be selected. If false, only one node at a time can be
	 *            selected
	 */
	void setAllowSelectMultiple(boolean value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10897.java