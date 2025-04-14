error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[178,80]

error in qdox parser
file content:
```java
offset: 4940
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17762.java
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
package wicket.util.profile;

import java.util.Arrays;

// ----------------------------------------------------------------------------

/**
 * A non-shell profile tree node implementation. This implementation trades off some
 * object orientation "niceness" to achieve more memory compactness.
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov
 *         </a>, 2003
 */
final class ObjectProfileNode extends AbstractProfileNode
{
    final ILink m_link;

    final Object m_obj;

    int m_refcount;

    AbstractShellProfileNode m_shell;

    IObjectProfileNode[] m_children;

    ObjectProfileNode(final ObjectProfileNode parent, final Object obj, final ILink link)
    {
        super(parent);

        m_obj = obj;
        m_link = link;
        m_refcount = 1;
        m_children = EMPTY_OBJECTPROFILENODE_ARRAY;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#object()
     */
    public Object object()
    {
        return m_obj;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#name()
     */
    public String name()
    {
        return (m_link == null) ? ObjectProfiler.INPUT_OBJECT_NAME : m_link.name();
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#shell()
     */
    public IObjectProfileNode shell()
    {
        return m_shell;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#children()
     */
    public IObjectProfileNode[] children()
    {
        return m_children;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#refcount()
     */
    public int refcount()
    {
        return m_refcount;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#traverse(wicket.util.profile.IObjectProfileNode.INodeFilter, wicket.util.profile.IObjectProfileNode.INodeVisitor)
     */
    public boolean traverse(final INodeFilter filter, final INodeVisitor visitor)
    {
        if ((visitor != null) && ((filter == null) || filter.accept(this)))
        {
            visitor.previsit(this);

            final IObjectProfileNode[] children = m_children;

            for (int i = 0; i < children.length; ++i)
            {
                children[i].traverse(filter, visitor);
            }

            visitor.postvisit(this);

            return true;
        }

        return false;
    }

    // protected: .............................................................
    // package: ...............................................................

    /*
     * This method manages the vector in m_children field for an unfinished node.
     */
    void addFieldRef(final IObjectProfileNode node)
    {
        // [m_size is the child count]
        IObjectProfileNode[] children = m_children;
        final int childrenLength = children.length;

        if (m_size >= childrenLength)
        {
            final IObjectProfileNode[] newchildren = new IObjectProfileNode[Math.max(1,
                    childrenLength << 1)];

            System.arraycopy(children, 0, newchildren, 0, childrenLength);
            m_children = children = newchildren;
        }

        children[m_size++] = node;
    }

    /*
     * This method is called once on every node to lock it down into its immutable and
     * most compact representation during phase 2 of profile tree construction.
     */
    void finish()
    {
        final int childCount = m_size; // m_size is the child count for a

        // non-shell node
        if (childCount > 0)
        {
            if (childCount < m_children.length)
            {
                final IObjectProfileNode[] newadj = new IObjectProfileNode[childCount];

                System.arraycopy(m_children, 0, newadj, 0, childCount);

                m_children = newadj;
            }

            Arrays.sort(m_children);

            int size = 0;

            for (int i = 0; i < childCount; ++i)
            {
                size += m_children[i].size();
            }

            m_size = size; // m_size is the full node size for all nodes
        }
    }

    // private: ...............................................................
} // end of class
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17762.java