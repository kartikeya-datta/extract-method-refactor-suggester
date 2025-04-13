error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[173,2]

error in qdox parser
file content:
```java
offset: 4291
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17665.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup.html.tree;


import java.io.Serializable;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import wicket.model.Model;

/**
 * Specialized model for trees. It makes it easier/ less verbose to work with the
 * current tree node, and acts as a context-like wrapper object.
 * Each tree node has its own node model.
 *
 * @author Eelco Hillenius
 */
public final class TreeNodeModel extends Model
{
    /** tree node. */
    private final DefaultMutableTreeNode treeNode;

    /** tree state. */
    private final TreeStateCache treeState;

    /** part of path. */
    private final TreePath path;

    /**
     * Construct.
     * @param treeNode the current tree node
     * @param treeState the (shared) reference to the tree state
     * @param path the (shared) current path
     */
    public TreeNodeModel(final DefaultMutableTreeNode treeNode,
            final TreeStateCache treeState, final TreePath path)
    {
        super(null);
        this.treeNode = treeNode;
        this.treeState = treeState;
        this.path = path;
    }

    /**
     * Gets the wrapped tree path.
     * @return the wrapped tree path.
     */
    public final TreePath getPath()
    {
        return path;
    }

    /**
     * Gets the wrapped treeNode. NOTE: if you made the tree's user
     * objects unique by calling the <code>makeUnique</code> method of
     * {@link AbstractTree}, the user objects are wrapped in instances
     * of {@link IdWrappedUserObject}.
     * @return the wrapped treeNode
     */
    public final DefaultMutableTreeNode getTreeNode()
    {
        return treeNode;
    }

    /**
     * Gets the user object of the wrapped tree node.
     * @return the user object of the wrapped tree node
     */
    public final Serializable getUserObject()
    {
    	Object obj = treeNode.getUserObject();
    	if(obj instanceof IdWrappedUserObject)
    	{
    		return (Serializable)((IdWrappedUserObject)obj).getUserObject();
    	}
    	else
    	{
    		return (Serializable)obj;
    	}
    }

    /**
     * Gets the tree state object.
     * @return the tree state object.
     */
    public final TreeStateCache getTreeState()
    {
        return treeState;
    }

    /**
     * Gets whether this node is a leaf.
     * @return whether this node is a leaf.
     */
    public final boolean isLeaf()
    {
        return treeNode.isLeaf();
    }

    /**
     * Gets the current level.
     * @return the current level.
     */
    public final int getLevel()
    {
        return treeNode.getLevel();
    }

    /**
     * Gets whether this node is the root.
     * @return whether this node is the root.
     */
    public final boolean isRoot()
    {
        return treeNode.isRoot();
    }

    /**
     * Finds the tree path for the given user object.
     * @param userObject the user object
     * @return the tree path for the given user object
     */
    public TreePath findTreePath(Object userObject)
    {
        return treeState.findTreePath(userObject);
    }

    /**
     * Gets the tree node's siblings.
     * @return siblings.
     */
    public final boolean hasSiblings()
    {
        return (treeNode.getNextSibling() != null);
    }

    /**
     * Whether this node is part of the expanded path.
     * @return whether this node is part of the expanded path
     */
    public boolean isExpanded()
    {
        return treeState.isExpanded(path);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return String.valueOf(treeNode);
    }
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17665.java