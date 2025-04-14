error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17754.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17754.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[172,80]

error in qdox parser
file content:
```java
offset: 4436
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17754.java
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

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.LinkedList;

// ----------------------------------------------------------------------------

/**
 * Abstract base class for all node implementations in this package.
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov
 *         </a>, 2003
 */
abstract class AbstractProfileNode implements IObjectProfileNode, Comparable
{
    static final IObjectProfileNode[] EMPTY_OBJECTPROFILENODE_ARRAY = new IObjectProfileNode[0];

    int m_size;

    // private: ...............................................................
    private final IObjectProfileNode m_parent;

    private transient IObjectProfileNode[] m_path;

    // protected: .............................................................
    // package: ...............................................................
    AbstractProfileNode(final IObjectProfileNode parent)
    {
        m_parent = parent;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#size()
     */
    public final int size()
    {
        return m_size;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#parent()
     */
    public final IObjectProfileNode parent()
    {
        return m_parent;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#path()
     */
    public final IObjectProfileNode[] path()
    {
        IObjectProfileNode[] path = m_path;

        if (path != null)
        {
            return path;
        }
        else
        {
            final LinkedList /* IObjectProfileNode */_path = new LinkedList();

            for (IObjectProfileNode node = this; node != null; node = node.parent())
            {
                _path.addFirst(node);
            }

            path = new IObjectProfileNode[_path.size()];
            _path.toArray(path);

            m_path = path;

            return path;
        }
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#root()
     */
    public final IObjectProfileNode root()
    {
        IObjectProfileNode node = this;

        for (IObjectProfileNode parent = parent(); parent != null; node = parent, parent = parent
                .parent())
        {
            ;
        }

        return node;
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#pathlength()
     */
    public final int pathlength()
    {
        final IObjectProfileNode[] path = m_path;

        if (path != null)
        {
            return path.length;
        }
        else
        {
            int result = 0;

            for (IObjectProfileNode node = this; node != null; node = node.parent())
            {
                ++result;
            }

            return result;
        }
    }

    /**
     * @see wicket.util.profile.IObjectProfileNode#dump()
     */
    public final String dump()
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter out = new PrintWriter(sw);

        final INodeVisitor visitor = ObjectProfileVisitors.newDefaultNodePrinter(out, null, null,
                ObjectProfiler.SHORT_TYPE_NAMES);

        traverse(null, visitor);

        out.flush();

        return sw.toString();
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public final int compareTo(final Object obj)
    {
        return ((AbstractProfileNode) obj).m_size - m_size;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return super.toString() + ": name = " + name() + ", size = " + size();
    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17754.java