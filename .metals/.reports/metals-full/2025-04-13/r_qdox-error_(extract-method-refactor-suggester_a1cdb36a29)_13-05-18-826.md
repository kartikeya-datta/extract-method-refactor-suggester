error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17669.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17669.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[211,2]

error in qdox parser
file content:
```java
offset: 6052
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17669.java
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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.VariableHeightLayoutCache;

import java.io.Serializable;

import java.util.Enumeration;
import java.util.List;

/**
 * Holder and handler for tree state.
 *
 * @author Eelco Hillenius
 */
public class TreeStateCache extends VariableHeightLayoutCache implements Serializable
{
    /** currently selected path. */
    private TreePath selectedPath;

    /** query that was used to select the current selected path. */
    private String selectedPathQuery;

    /** the objects owned by the selected path. */
    private List pathContent;

    /**
     * Expands the selected path and set selection to currently selected path.
     * @param selection the new selection.
     */
    public void setSelectedPath(TreePath selection)
    {
        setExpandedState(selection, true);
        this.selectedPath = selection;
    }

    /**
     * Expands the selected path and sets the selection to currently selected path and set the
     * content owned by the selected path.
     * @param selection the new selection
     * @param pathContent the path owned by the selection
     */
    public void setSelectedPath(TreePath selection, List pathContent)
    {
        setSelectedPath(selection);
        this.pathContent = pathContent;
    }

    /**
     * Expands the selected path and sets the selection to currently selected path,
     * sets the content owned by the selected path and sets the query that was used
     * to get the selection.
     * @param selection new selection
     * @param pathContent path owned by selection
     * @param selectedPathQuery query that was used to get selection
     */
    public void setSelectedPath(TreePath selection, List pathContent,
    		String selectedPathQuery)
    {
        setSelectedPath(selection, pathContent);
        this.selectedPathQuery = selectedPathQuery;
    }

    /**
     * Gets the currently selected path.
     * @return the currently selected path
     */
    public TreePath getSelectedPath()
    {
        if ((selectedPath == null) && isRootVisible())
        {
            selectedPath = new TreePath(getModel().getRoot());
        }

        return selectedPath;
    }

    /**
     * Gets the content that is owned by the current path.
     * @return list of content
     */
    public List getPathContent()
    {
        return pathContent;
    }

    /**
     * set the content that is owned by the current path.
     * @param list list of content
     */
    public void setPathContent(List list)
    {
        pathContent = list;
    }

    /**
     * @return String
     */
    public String getSelectedPathQuery()
    {
        return selectedPathQuery;
    }

    /**
     * @param selectedPathQuery
     */
    public void setSelectedPathQuery(String selectedPathQuery)
    {
        this.selectedPathQuery = selectedPathQuery;
    }

    /**
     * Returns an <code>Enumerator</code> that increments over the visible paths
     * starting at the root. The ordering of the enumeration is based on how the paths are
     * displayed.
     * @return an <code>Enumerator</code> that increments over the visible paths
     */
    public Enumeration getVisiblePathsFromRoot()
    {
        TreeNode root = (TreeNode) (getModel().getRoot());
        TreePath rootPath = new TreePath(root);

        return getVisiblePathsFrom(rootPath);
    }

    /**
     * get tree path in model for given user object.
     * @param userObject object to look for in model
     * @return TreePath
     */
    public TreePath findTreePath(Object userObject)
    {
        TreePath path = null;
        DefaultMutableTreeNode endNode = findNode(userObject);

        if (endNode != null)
        {
            path = new TreePath(endNode.getPath());
        }

        return path;
    }

    /**
     * find the node in model that has arg as its userObject.
     * @param userObject object to look for in model
     * @return DefaultMutableTreeNode
     */
    public DefaultMutableTreeNode findNode(Object userObject)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();

        return findNodeRecursively(root, null, userObject);
    }

    /**
     * Find the node by recursing.
     * @param currentNode the current node
     * @param resultNode the resulting node
     * @param userObject the user object to find
     * @return the found node
     */
    private DefaultMutableTreeNode findNodeRecursively(DefaultMutableTreeNode currentNode,
            DefaultMutableTreeNode resultNode, Object userObject)
    {
        int childCount = currentNode.getChildCount();

        if (currentNode.getUserObject().equals(userObject))
        {
            resultNode = currentNode;
        }
        else if (childCount > 0)
        {
            for (int i = 0; i < childCount; i++)
            {
                resultNode = findNodeRecursively(
                        (DefaultMutableTreeNode) currentNode.getChildAt(i), resultNode, userObject);

                if (resultNode != null)
                {
                    // found it! break loop
                    break;
                }
            }
        } // else: not found in this path

        return resultNode;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17669.java