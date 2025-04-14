error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[180,80]

error in qdox parser
file content:
```java
offset: 6873
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9231.java
text:
```scala
{

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
package wicket.util.profile;

// ----------------------------------------------------------------------------

/**
 * The main interface for exploring an object profile tree. See individual methods for
 * details.
 * @see ObjectProfiler#profile(Object)
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov
 *         </a>, 2003
 */
public interface IObjectProfileNode
{ // TODO finalize javadoc
    /**
     * Returns the object associated with this node. This is never null except for shell
     * pseudo-nodes.
     * @return object instance [null only for shell nodes]
     */
    Object object();

    /**
     * Returns a human-readable name for this node, usually derived from the class field
     * or array index that was used to reach the {@link #object() object}associated with
     * this node.
     * @return node name [never null]
     */
    String name();

    /**
     * Returns the full size (in bytes) assigned to this node in its profile tree. This is
     * the sum of sizes of the node class's {@link #shell() shell}and its non-primitive
     * non-null {@link #children() instance fields}, computed as a closure over the
     * spanning tree produced by {@link ObjectProfiler#profile}.
     * @return total node size [always positive]
     */
    int size();

    /**
     * Returns the reference count for the associated {@link #object()}. This is exactly
     * the number of unique references to this object in the object graph submitted to
     * {@link ObjectProfiler#profile}.
     * @return reference count [always positive]
     */
    int refcount();

    /**
     * Returns the assigned ownership parent for this node. This is null for the root
     * node.
     * @return parent node [null only for the root node]
     */
    IObjectProfileNode parent();

    /**
     * Returns all children of this node. These are non-null references found in this
     * object's class fields (or array slots if the object is of an array type). The
     * result is sorted in decreasing {@link #size() size}order.
     * <P>
     * Note: the returned array also contains the {@link #shell() shell}pseudo-node.
     * @return array of children nodes, sorted by size [never null, may be empty]
     */
    IObjectProfileNode[] children();

    /**
     * Returns the shell pseudo-node for this node. This represents all instance data
     * fields that are "inlined" in the class definition represented by this node
     * (including all superclasses all the way to java.lang.Object). This includes
     * primitive data fields, object references representing non-primitive fields, and
     * (for arrays) the array length field and storage required for the array slots.
     * <P>
     * Another way to describe this is that node.shell().size() is the minimum size an
     * instance of node.object().getClass() can be (when all non-primitive instance fields
     * are set to 'null').
     * <P>
     * The returned reference is also guaranteed to be present somewhere in the array
     * returned by {@link #children()}. This data is kept in a separate node instance to
     * simplify tree visiting and node filtering.
     * @return shell pseudo-node [null only for shell nodes]
     */
    IObjectProfileNode shell();

    /**
     * Returns the full path from the profile tree root to this node, in that direction.
     * The result includes the root node as well as the current node.
     * <P>
     * Invariant: node.root() == node.path()[0] Invariant: node.path()[node.path().length -
     * 1] == node Invariant: node.path().length == node.pathlength()
     * @return node tree path [never null/empty]
     */
    IObjectProfileNode[] path();

    /**
     * A convenience method for retrieving the root node from any node in a profile tree.
     * <P>
     * Invariant: node.root() == node iff 'node' is the root of its profile tree
     * Invariant: node.root() == node.path()[0]
     * @return the root node for the profile tree that the current node is a part of
     *         [never null]
     */
    IObjectProfileNode root();

    /**
     * A convenience method for retrieving this node's tree path length.
     * @return path length [always positive]
     */
    int pathlength();

    /**
     * A generic hook for traversing profile trees using {@link INodeFilter filters}and
     * {@link INodeVisitor visitors}. See IObjectProfileNode.INodeFilter and
     * IObjectProfileNode.INodeVisitor for more details
     * @param filter [null is equivalent to no filtering]
     * @param visitor [may not be null]
     * @return 'true' iff either 'filter' was null or it returned 'true' for this node
     */
    boolean traverse(INodeFilter filter, INodeVisitor visitor);

    /**
     * Dumps this node into a flat-text format used by the
     * ObjectProfileVisitors#newDefaultNodePrinter
     * default node visitor.
     * @return indented dump string [could be very large]
     */
    String dump();

    // public: ................................................................

    /**
     * A generic interface for defining node filters. A node filter is used as a guard to
     * determine whether a given visitor should be given a shot as doing something with a
     * profile tree node.
     */
    interface INodeFilter
    {
        /**
         * @param node about to be visited [never null]
         * @return 'true' if 'node' and its children should be visited
         */
        boolean accept(IObjectProfileNode node);
    } // end of nested interface

    /**
     * A generic interface for defining node visitors. A node visitor is applied to a
     * profile tree node both before and after visiting the node's children, if any.
     */
    interface INodeVisitor
    {
        /**
         * Pre-order visit.
         * @param node being visited [never null]
         */
        void previsit(IObjectProfileNode node);

        /**
         * Post-order visit.
         * @param node being visited [never null]
         */
        void postvisit(IObjectProfileNode node);
    } // end of nested interface
} // end of interface
// ----------------------------------------------------------------------------@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9231.java