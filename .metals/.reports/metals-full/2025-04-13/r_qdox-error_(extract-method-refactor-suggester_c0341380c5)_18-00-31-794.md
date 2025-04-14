error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17667.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17667.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[180,2]

error in qdox parser
file content:
```java
offset: 4813
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17667.java
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

import wicket.model.IModel;

/**
 * A replacement model for
 * {@link wicket.markup.ComponentTagAttributeModifier}s for tree row components.
 *
 * @see wicket.markup.ComponentTagAttributeModifier
 * @see wicket.markup.html.tree.Tree
 * @see wicket.markup.html.tree.TreeRow
 *
 * @author Eelco Hillenius
 */
public class TreeRowReplacementModel implements IModel
{
    /** model of one tree node. */
    private final TreeNodeModel treeNodeModel;

    /**
     * Construct.
     * @param treeNodeModel model of one tree node
     */
    public TreeRowReplacementModel(TreeNodeModel treeNodeModel)
    {
        this.treeNodeModel = treeNodeModel;
    }

    /**
     * Gets the class depending on some attributes of the current tree node.
     * @see wicket.model.IModel#getObject()
     */
    public final Object getObject()
    {
        return (treeNodeModel != null) ? getJunctionCSSClass(treeNodeModel) : null;
    }

    /**
     * @see wicket.model.IModel#setObject(java.lang.Object)
     */
    public final void setObject(Object object)
    {
        // nothing to do here
    }

    /**
     * Gets the css class name/ replacement value for the given node.
     * @param treeNodeModel model of one tree node
     * @return css class name/ replacement value
     */
    protected String getJunctionCSSClass(TreeNodeModel treeNodeModel)
    {
        final String cssClass;

        if (treeNodeModel.isLeaf())
        {
            if (treeNodeModel.hasSiblings())
            {
                cssClass = getCSSClassForLeafWithSiblings();
            }
            else
            {
                cssClass = getCSSClassForEndLeaf();
            }
        }
        else
        {
            if (treeNodeModel.hasSiblings())
            {
                if (treeNodeModel.isExpanded())
                {
                    cssClass = getCSSClassForExpandedJunctionWithSiblings();
                }
                else
                {
                    cssClass = getCSSClassForClosedJunctionWithSiblings();
                }
            }
            else
            {
                if (treeNodeModel.isExpanded())
                {
                    cssClass = getCSSClassForExpandedEndJunction();
                }
                else
                {
                    cssClass = getCSSClassForClosedEndJunction();
                }
            }
        }

        return cssClass;
    }

    /**
     * Gets the css class for a closed junction with no siblings.
     * @return the css class
     */
    private String getCSSClassForClosedEndJunction()
    {
        final String cssClass;
        cssClass = "tree-junction-closed-end";
        return cssClass;
    }

    /**
     * Gets the css class for an expanded junction with no siblings.
     * @return the css class
     */
    private String getCSSClassForExpandedEndJunction()
    {
        final String cssClass;
        cssClass = "tree-junction-expanded-end";
        return cssClass;
    }

    /**
     * Gets the css class for a closed junction with siblings.
     * @return the css class
     */
    private String getCSSClassForClosedJunctionWithSiblings()
    {
        final String cssClass;
        cssClass = "tree-junction-closed-siblings";
        return cssClass;
    }

    /**
     * Gets the css class for an expanded junction with siblings.
     * @return the css class
     */
    private String getCSSClassForExpandedJunctionWithSiblings()
    {
        final String cssClass;
        cssClass = "tree-junction-expanded-siblings";
        return cssClass;
    }

    /**
     * Gets the css class for a leaf with no siblings.
     * @return the css class
     */
    private String getCSSClassForEndLeaf()
    {
        final String cssClass;
        cssClass = "tree-leaf-end";
        return cssClass;
    }

    /**
     * Gets the css class for leaf with siblings.
     * @return the css class
     */
    protected String getCSSClassForLeafWithSiblings()
    {
        final String cssClass;
        cssClass = "tree-leaf-siblings";
        return cssClass;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17667.java