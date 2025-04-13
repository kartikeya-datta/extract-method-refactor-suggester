error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17761.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17761.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[199,80]

error in qdox parser
file content:
```java
offset: 6491
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17761.java
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

// ----------------------------------------------------------------------------

/**
 * A Factory for a few stock node filters. See the implementation for details.
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov
 *         </a>, 2003
 */
public abstract class ObjectProfileFilters
{
    // protected: .............................................................
    // package: ...............................................................
    // private: ...............................................................
    private ObjectProfileFilters()
    {
    } // this class is not extendible

    // public: ................................................................

    /**
     * Factory method for creating a visitor that only accepts profile nodes with sizes
     * larger than a given threshold value.
     * @param threshold node size in bytes
     * @return node filter
     */
    public static ObjectProfileNode.INodeFilter newSizeFilter(final int threshold)
    {
        return new SizeFilter(threshold);
    }

    /**
     * Factory method for creating a visitor that accepts a profile node only if it is at
     * least the k-th largest child of its parent for a given value of k. E.g.,
     * newRankFilter(1) will prune the profile tree so that only the largest child is
     * visited for every node.
     * @param rank acceptable size rank [must be >= 0]
     * @return node filter
     */
    public static ObjectProfileNode.INodeFilter newRankFilter(final int rank)
    {
        return new RankFilter(rank);
    }

    /**
     * Factory method for creating a visitor that accepts a profile node only if its size
     * is larger than a given threshold relative to the size of the root node (i.e., size
     * of the entire profile tree).
     * @param threshold size fraction threshold
     * @return node filter
     */
    public static ObjectProfileNode.INodeFilter newSizeFractionFilter(final double threshold)
    {
        return new SizeFractionFilter(threshold);
    }

    /**
     * Factory method for creating a visitor that accepts a profile node only if its size
     * is larger than a given threshold relative to the size of its parent node. This is
     * useful for pruning the profile tree to show the largest contributors at every tree
     * level.
     * @param threshold size fraction threshold
     * @return node filter
     */
    public static ObjectProfileNode.INodeFilter newParentSizeFractionFilter(final double threshold)
    {
        return new ParentSizeFractionFilter(threshold);
    }

    private static final class SizeFilter implements IObjectProfileNode.INodeFilter
    {
        private final int m_threshold;

        SizeFilter(final int threshold)
        {
            m_threshold = threshold;
        }

        /**
         * @see wicket.util.profile.IObjectProfileNode.INodeFilter#accept(wicket.util.profile.IObjectProfileNode)
         */
        public boolean accept(final IObjectProfileNode node)
        {
            return node.size() >= m_threshold;
        }
    } // end of nested class

    private static final class RankFilter implements IObjectProfileNode.INodeFilter
    {
        private final int m_threshold;

        RankFilter(final int threshold)
        {
            m_threshold = threshold;
        }

        /**
         * @see wicket.util.profile.IObjectProfileNode.INodeFilter#accept(wicket.util.profile.IObjectProfileNode)
         */
        public boolean accept(final IObjectProfileNode node)
        {
            final IObjectProfileNode parent = node.parent();

            if (parent == null)
            {
                return true;
            }

            final IObjectProfileNode[] siblings = parent.children();

            for (int r = 0, rLimit = Math.min(siblings.length, m_threshold); r < rLimit; ++r)
            {
                if (siblings[r] == node)
                {
                    return true;
                }
            }

            return false;
        }
    } // end of nested class

    private static final class SizeFractionFilter implements IObjectProfileNode.INodeFilter
    {
        private final double m_threshold;

        SizeFractionFilter(final double threshold)
        {
            m_threshold = threshold;
        }

        /**
         * @see wicket.util.profile.IObjectProfileNode.INodeFilter#accept(wicket.util.profile.IObjectProfileNode)
         */
        public boolean accept(final IObjectProfileNode node)
        {
            if (node.size() >= (m_threshold * node.root().size()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } // end of nested class

    private static final class ParentSizeFractionFilter implements IObjectProfileNode.INodeFilter
    {
        private final double m_threshold;

        ParentSizeFractionFilter(final double threshold)
        {
            m_threshold = threshold;
        }

        /**
         * @see wicket.util.profile.IObjectProfileNode.INodeFilter#accept(wicket.util.profile.IObjectProfileNode)
         */
        public boolean accept(final IObjectProfileNode node)
        {
            final IObjectProfileNode parent = node.parent();

            if (parent == null)
            {
                return true; // always accept root node
            }
            else if (node.size() >= (m_threshold * parent.size()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } // end of nested class
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17761.java