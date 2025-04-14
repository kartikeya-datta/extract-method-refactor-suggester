error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17660.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17660.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[258,2]

error in qdox parser
file content:
```java
offset: 8537
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17660.java
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import wicket.markup.ComponentTagAttributeModifier;
import wicket.markup.html.HtmlContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.table.ListItem;
import wicket.markup.html.table.ListView;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A Tree that renders as a flat list, using spacers and nodes.
 * <p>
 * The visible tree rows are put in one flat list. For each row, a list is constructed
 * with fillers, that can be used to create indentation. After the fillers, the actual
 * node content is put.
 * </p>
 * <p>
 * For example:
 * <pre>
 * &lt;span id="spacers"&gt;&lt;/span&gt;&lt;span id=" spacers"&gt;&lt;/span&gt;
 * &lt;span id ="node"&gt;&lt;span id="label"&gt;foo&lt;/span&gt;&lt;/span&gt;
 * </pre>
 * Could be one row, where the node is on level two (hence the two spacer elements).
 * </p>
 * <p>
 * If you combine this with CSS like:
 * <pre>
 *	#spacers {
 *		padding-left: 16px;
 *	}
 * </pre>
 * you have an indented tree.
 * </p>
 *
 * @author Eelco Hillenius
 */
public abstract class FlatTree extends AbstractTree
{
    /** table for the current visible tree paths. */
    private ListView visibleTreePathTable;

    /**
     * Constructor.
     * @param componentName The name of this container
     * @param model the underlying tree model
     */
    public FlatTree(final String componentName, final TreeModel model)
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
    public FlatTree(final String componentName, final TreeModel model,
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
    public FlatTree(final String componentName, TreeStateCache treeState)
    {
        super(componentName, treeState);
    }

    /**
     * Builds the structures needed to display the currently visible tree paths.
     * @param treeState the current tree state
     */
    protected void applySelectedPaths(TreeStateCache treeState)
    {
        Enumeration e = treeState.getVisiblePathsFromRoot();
        List visiblePathsList = new ArrayList();
        while (e.hasMoreElements())
        {
            TreePath path = (TreePath)e.nextElement();
            DefaultMutableTreeNode treeNode =
                (DefaultMutableTreeNode)path.getLastPathComponent();
            TreeNodeModel treeNodeModel = new TreeNodeModel(treeNode, treeState, path);
            visiblePathsList.add(treeNodeModel);
        }
        Model model = new Model((Serializable) visiblePathsList);

        if (visibleTreePathTable == null)
        {
        	visibleTreePathTable = newVisibleTreePathListView(model);
            add(visibleTreePathTable);
        }
        else
        {
            visibleTreePathTable.removeAll();
            visibleTreePathTable.setModel(model);
        }
    }

	/**
	 * Creates a {@link ListView} to use for rendering the visible tree rows.
	 * The model parameter should be used as the model for the <code>ListView</code>.
	 * @param model a model that contains a list of {@link TreeNodeModel}s
	 * @return a new <code>ListView</code> that uses the provided model
	 */
	protected final ListView newVisibleTreePathListView(Model model)
	{
		return new VisibleTreePathListView("tree", model);
	}

    /**
     * Get image name for junction.
     * @param node the model with the current node
     * @return image name
     */
    protected abstract String getJunctionImageName(TreeNodeModel node);

    /**
     * Get image name for node.
     * @param node the model with the current node
     * @return image name
     */
    protected abstract String getNodeImageName(TreeNodeModel node);

    /**
     * Table for visible tree paths.
     */
    private final class VisibleTreePathListView extends ListView
    {
        /**
         * Construct.
         * @param name name of the component
         * @param model the model
         */
        public VisibleTreePathListView(String name, IModel model)
        {
            super(name, model);
        }

        /**
         * @see wicket.markup.html.table.ListView#populateItem(wicket.markup.html.table.ListItem)
         */
        protected void populateItem(ListItem listItem)
        {
            TreeNodeModel treeNodeModel = (TreeNodeModel)listItem.getModelObject();

            // add spacers
            int level = treeNodeModel.getLevel();
            List spacerList = new ArrayList(level);
            for(int i = 0; i < level; i++)
            {
                spacerList.add(treeNodeModel);
            }
            listItem.add(new SpacerList("spacers", spacerList));

            // add node
            HtmlContainer nodeContainer = new HtmlContainer("node");
            Serializable userObject = treeNodeModel.getUserObject();

            if (userObject == null)
            {
                throw new RuntimeException("userObject == null");
            }
            TreeNodeLink expandCollapsLink = new TreeNodeLink(
            		"expandCollapsLink", FlatTree.this, treeNodeModel);

            HtmlContainer junctionImg = new HtmlContainer("junctionImg");
            junctionImg.add(new ComponentTagAttributeModifier(
            		"src", true, new Model(getJunctionImageName(treeNodeModel))));
			expandCollapsLink.add(junctionImg);
            HtmlContainer nodeImg = new HtmlContainer("nodeImg");
            nodeImg.add(new ComponentTagAttributeModifier(
            		"src", true, new Model(getNodeImageName(treeNodeModel))));
			expandCollapsLink.add(nodeImg);
            nodeContainer.add(expandCollapsLink);

            TreeNodeLink selectLink = new TreeNodeLink(
            		"selectLink", FlatTree.this, treeNodeModel);
            selectLink.add(new Label("label", getUserObjectAsString(userObject)));
            nodeContainer.add(selectLink);
            listItem.add(nodeContainer);
        }

		/**
		 * Gets the user object as a string. Clients should override this to provide
		 * custom behaviour.
		 * @param userObject the user object
		 * @return the string representation of the user object
		 */
		protected String getUserObjectAsString(Serializable userObject)
		{
			return String.valueOf(userObject);
		}
    }

    /**
     * Renders spacer items.
     */
    private final class SpacerList extends ListView
    {
        /**
         * Construct.
         * @param componentName name of the component
         * @param list list
         */
        public SpacerList(String componentName, List list)
        {
            super(componentName, list);
        }

        /**
         * @see wicket.markup.html.table.ListView#populateItem(wicket.markup.html.table.ListItem)
         */
        protected void populateItem(ListItem listItem)
        {
        	// nothing needed; we just render the tags and use CSS to indent
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17660.java