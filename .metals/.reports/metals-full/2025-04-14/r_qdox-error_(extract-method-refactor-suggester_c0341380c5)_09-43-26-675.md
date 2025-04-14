error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8769.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8769.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8769.java
text:
```scala
public O@@bject getObject(final Component component)

/*
 * $Id$
 * $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package navmenu;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import wicket.AttributeModifier;
import wicket.contrib.markup.html.tree.Tree;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * Border component that holds the menu.
 *
 * @author Eelco Hillenius
 */
public class MenuBorder extends Border
{
	/**
	 * Construct.
	 * @param componentName
	 */
	public MenuBorder(String componentName)
	{
		super(componentName);
		TreeModel model = MenuApplication.getMenu();
		ULTree tree = new ULTree("tree", model);
		add(tree);
	}

	/**
	 * Tree that renders as nested lists (UL/ LI).
	 */
	private final class ULTree extends Tree
	{
		/**
		 * structure with nested nodes and lists to represent the tree model
		 * using lists.
		 */
		private List nestedList;

		/**
		 * Construct.
		 * @param componentName The name of this container
		 * @param model the tree model
		 */
		public ULTree(String componentName, TreeModel model)
		{
			super(componentName, model);
			setRootVisible(false);
			buildNestedListModel(model);
			UL treeRowsListView = new UL("rows", nestedList, 0);
			add(treeRowsListView);
		}

		/**
		 * Builds the internal structure.
		 * @param model the tree model that the internal structure is to be based on
		 */
		private void buildNestedListModel(TreeModel model)
		{
			nestedList = new ArrayList(); // reference to the first level list
			if (model != null)
			{
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
				if(root != null)
				{
					Enumeration children = root.children();
					while(children.hasMoreElements())
					{
						DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
						add(nestedList, child);
					}
				}
			}
		}

		/**
		 * Add node to list and add any childs recursively.
		 * @param list the list to add the node to
		 * @param node the node to add
		 */
		private void add(List list, DefaultMutableTreeNode node)
		{
			list.add(node);
			Enumeration children = node.children();
			if(children.hasMoreElements()) // any elements?
			{
				List childList = new ArrayList();
				list.add(childList);
				while(children.hasMoreElements())
				{
					DefaultMutableTreeNode child = (DefaultMutableTreeNode)children.nextElement();
					add(childList, child);
				}
			}
		}
	}

	/**
	 * Represents UL elements.
	 */
	private final class UL extends Panel
	{
		/** the level this view is on. */
		private final int level;

	    /**
	     * Constructor.
	     * @param componentName The name of this component
	     * @param list a list where each element is either a string or another list
	     * @param level the level this view is on (from 0..n-1)
	     */
	    public UL(String componentName, List list, int level)
	    {
	        super(componentName);
	        this.level = level;
	        WebMarkupContainer ul = new WebMarkupContainer("ul");
	        ul.add(new AttributeModifier("id", true, new Model(getLevelAsString())));
			Rows rows = new Rows("rows", list);
			ul.add(rows);
			add(ul);
	    }

	    /**
	     * Gets the level of this view as a string that is usuable with CSS.
	     * @return the level of this view as a string that is usuable with CSS
	     */
	    private String getLevelAsString()
	    {
	    	return "tabNavigation";
//	    	if(level == 0)
//	    	{
//	    		return "primary";
//	    	}
//	    	else
//	    	{
//	    		return "secondary";
//	    	}
	    }

	    /**
	     * The list class.
	     */
	    private final class Rows extends ListView
	    {
	        /**
	         * Construct.
	         * @param name name of the component
	         * @param list a list where each element is either a string or another list
	         */
	        public Rows(String name, List list)
	        {
	            super(name, list);
	        }

	        /**
	         * @see wicket.markup.html.list.ListView#populateItem(wicket.markup.html.list.ListItem)
	         */
	        protected void populateItem(ListItem listItem)
	        {
	        	final int index = listItem.getIndex();
	            Object modelObject = listItem.getModelObject();
	            if(modelObject instanceof List)
	            {
	                // create a panel that renders the sub list
	                List list = (List)modelObject;
					UL ul = new UL("row", list, level + 1);
	                listItem.add(ul);
	            }
	            else
	            {
	            	DefaultMutableTreeNode node = (DefaultMutableTreeNode)modelObject;
					LI li = new LI("row", node, level, index);
	                listItem.add(li);
	            }
	        }
	    }
	}

	/**
	 * Represents LI elements.
	 */
	private final class LI extends Panel
	{
		/** the level this view is on. */
		private final int level;

	    /**
	     * Constructor.
	     * @param componentName The name of this component
	     * @param node tree node
	     * @param level the level this view is on (from 0..n-1)
	     * @param index the sibling index
	     */
	    public LI(final String componentName, final DefaultMutableTreeNode node,
	    		final int level, final int index)
	    {
	        super(componentName);
	        this.level = level;
	        // add the row (with the LI element attached, and the label with the
	        // row's actual value to display
	        MenuItem menuItem = (MenuItem)node.getUserObject();
	        final String label = menuItem.getLabel();
	        final BookmarkablePageLink pageLink = new BookmarkablePageLink(
	        		"link", menuItem.getPageClass(), menuItem.getPageParameters());
	        pageLink.setAutoEnable(false);
	        pageLink.add(new Label("label", label));
	        add(pageLink);
	        // TODO this works for one level, but what about nesting?
			add(new AttributeModifier("class", true, new Model()
			{
				public Object getObject()
				{
					return (pageLink.linksTo(getPage())) ? "selectedTab" : null;
				}
			}));
	    }
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8769.java