error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17663.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17663.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[205,2]

error in qdox parser
file content:
```java
offset: 7416
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17663.java
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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import wicket.markup.html.panel.Panel;

/**
 * A component that represents a tree. It renders using nested lists that in turn
 * use panels.
 * <p>
 * This type of tree is best used when you want to display your tree using nested
 * &lt;ul&gt; and &lt;li&gt; tags.
 * </p>
 * <p>
 * For example, this could be the rendering result of a tree: (it will actually look
 * a bit different as we are using panels as well, but you'll get the idea)
 * <pre>
 * &lt;ul id="nested"&gt;
 *   &lt;li id="row"&gt;
 *     &lt;span id="label"&gt;foo&lt;/span&gt;
 *   &lt;/li&gt;
 *   &lt;ul id="nested"&gt;
 *     &lt;li id="row"&gt;
 *       &lt;span id="label"&gt;bar&lt;/span&gt;
 *     &lt;/li&gt;
 *   &lt;/ul&gt;
 *   &lt;li id="row"&gt;
 *     &lt;span id="label"&gt;suck&lt;/span&gt;
 *   &lt;/li&gt;
 * &lt;/ul&gt;
 * </pre>
 * </p>
 * Override the getXXXPanel methods to provide your own customized rendering.
 * Look at the filebrowser example of the wicket-examples project for an example
 * </p>
 *
 * @see wicket.markup.html.tree.AbstractTree
 * @see wicket.markup.html.tree.TreeNodeModel
 * @see wicket.markup.html.tree.TreeRows
 * @see wicket.markup.html.tree.TreeRow
 * @see wicket.markup.html.tree.TreeRowReplacementModel
 *
 * @author Eelco Hillenius
 */
public class Tree extends AbstractTree
{
    /**
     * Constructor.
     * @param componentName The name of this container
     * @param model the underlying tree model
     */
    public Tree(final String componentName, final TreeModel model)
    {
        super(componentName, model);
    }

    /**
     * Constructor.
     * @param componentName The name of this container
     * @param model the underlying tree model
     * @param makeTreeModelUnique whether to make the user objects of the tree model
     * unique. If true, the default implementation will wrapp all user objects in
     * instances of {@link IdWrappedUserObject}. If false, users must ensure that the
     * user objects are unique within the tree in order to have the tree working properly
     */
    public Tree(final String componentName, final TreeModel model,
    		final boolean makeTreeModelUnique)
    {
        super(componentName, model, makeTreeModelUnique);
    }


    /**
     * Constructor using the given tree state. This tree state holds the tree model and
     * the currently visible paths.
     * @param componentName The name of this container
     * @param treeState the tree state that holds the tree model and the currently visible
     * paths
     */
    public Tree(final String componentName, TreeStateCache treeState)
    {
        super(componentName, treeState);
    }

    /**
     * Builds the structures needed to display the currently visible tree paths.
     * @param treeState the current tree state
     */
    protected void applySelectedPaths(TreeStateCache treeState)
    {
        removeAll();
        List visiblePathsList = new ArrayList();
        Enumeration e = treeState.getVisiblePathsFromRoot(); // get all visible
        while (e.hasMoreElements()) // put enumeration in a list
        {
            visiblePathsList.add(e.nextElement());
        }
        List nestedList = new ArrayList(); // reference to the first level list
        buildList(visiblePathsList, 0, 0, nestedList); // build the nested lists
        add(getTreeRowsPanel("tree", nestedList)); // add the tree panel
    }

    /**
     * Gets the panel which displays the tree rows. Usually you'll want this panel
     * to be attached to a UL (Unnumbered List) tag.
     * Override this if you want to provide your own panel.
     * @param nestedList the list that represents the currently visible tree paths.
     * @param componentName the name of the panel. Warning: this must be used to construct
     * the panel.
     * @return the panel that is used to display visible tree paths
     */
    protected Panel getTreeRowsPanel(String componentName, List nestedList)
    {
        return new TreeRows(componentName, nestedList, this);
    }

    /**
     * Gets the panel that displays one row. Usually you'll want this panel to
     * be attached to a LI (List Item) tag.
     * Override this if you want to provide your own panel.
     * @param componentName the name of the panel.
     * Warning: if you did not override {@link TreeRows}, this must be
     * used to construct the panel.
     * @param nodeModel the model that holds a reference to the tree node and some
     * other usefull objects that help you construct the panel
     * @return the panel that displays one row
     */
    protected Panel getTreeRowPanel(String componentName, TreeNodeModel nodeModel)
    {
        return new TreeRow(componentName, this, nodeModel);
    }

    /**
     * Gets the panel that displays one row. For internal usage only.
     * @param componentName the name o fthe panel
     * @param nodeModel the node model
     * @return the panel that displays one row
     */
    final Panel internalGetTreeRowPanel(String componentName, TreeNodeModel nodeModel)
    {
        return getTreeRowPanel(componentName, nodeModel);
    }

    /**
     * Builds nested lists that represent the current visible tree paths.
     * @param visiblePathsList the whole - flat - list of visible paths
     * @param index the current index in the list of visible paths
     * @param level the current nesting level
     * @param rows a list that holds the current level of rows
     * @return the index in the list of visible paths
     */
    private int buildList(final List visiblePathsList, int index, int level, final List rows)
    {
        int len = visiblePathsList.size();
        while (index < len)
        {
            TreePath path = (TreePath) visiblePathsList.get(index);
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            int thisLevel = treeNode.getLevel();
            if (thisLevel > level) // go deeper
            {
                List nestedRows = new ArrayList();
                rows.add(nestedRows);
                index = buildList(visiblePathsList, index, thisLevel, nestedRows);
            }
            else if (thisLevel < level) // end of nested
            {
                return index;
            }
            else // node
            {
                TreeNodeModel nodeModel = new TreeNodeModel(treeNode, getTreeState(), path);
                rows.add(nodeModel);
                index++;
            }
        }
        return index;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17663.java