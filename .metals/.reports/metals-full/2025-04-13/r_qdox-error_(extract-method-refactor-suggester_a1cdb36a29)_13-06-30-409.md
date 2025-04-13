error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16765.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16765.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16765.java
text:
```scala
s@@uper.onBeforeAttach();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup.html.tree.table;

import java.io.Serializable;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.ResourceReference;
import wicket.behavior.AbstractBehavior;
import wicket.markup.ComponentTag;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Fragment;
import wicket.markup.html.tree.AbstractTree;
import wicket.markup.html.tree.DefaultAbstractTree;
import wicket.markup.html.tree.table.ColumnLocation.Alignment;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * TreeTable is a component that represents a grid with a tree. It's divided
 * into columns. One of the columns has to be column derived from
 * {@link AbstractTreeColumn}.
 * 
 * @author Matej Knopp
 */
public class TreeTable extends DefaultAbstractTree
{
	/**
	 * Callback for rendering tree node text.
	 */
	public static interface IRenderNodeCallback extends Serializable
	{
		/**
		 * Renders the tree node to text.
		 * 
		 * @param node
		 *            The tree node to render
		 * @return the tree node as text
		 */
		public String renderNode(TreeNode node);
	}

	/**
	 * Represents a content of a cell in TreeColumn (column containing the
	 * actual tree).
	 * 
	 * @author Matej Knopp
	 */
	private class TreeFragment extends Fragment
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param parent
		 * @param id
		 * @param node
		 * @param level
		 * @param renderNodeCallback
		 *            The call back for rendering nodes
		 */
		public TreeFragment(MarkupContainer<?> parent, String id, final TreeNode node, int level,
				final IRenderNodeCallback renderNodeCallback)
		{
			super(parent, id, "fragment");

			newIndentation(this, "indent", node, level);

			newJunctionLink(this, "link", "image", node);

			MarkupContainer nodeLink = newNodeLink(this, "nodeLink", node);

			newNodeIcon(nodeLink, "icon", node);

			new Label(nodeLink, "label", new Model<String>()
			{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public String getObject()
				{
					return renderNodeCallback.renderNode(node);
				}
			});
		}
	}

	/** Reference to the css file. */
	private static final ResourceReference CSS = new ResourceReference(DefaultAbstractTree.class,
			"res/tree-table.css");

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a tree cell for given node. This method is supposed to be used by
	 * TreeColumns (columns that draw the actual tree).
	 * 
	 * @param parent
	 *            Parent component
	 * 
	 * @param id
	 *            Component ID
	 * 
	 * @param node
	 *            Tree node for the row
	 * 
	 * @param level
	 *            How deep is the node nested (for convenience)
	 * 
	 * @param callback
	 *            Used to get the display string
	 * @return The tree cell
	 */
	public static final Component newTreeCell(MarkupContainer<?> parent, String id, TreeNode node,
			int level, IRenderNodeCallback callback)
	{
		TreeTable table = parent.findParent(TreeTable.class);

		return table.newTreePanel(parent, id, node, level, callback);
	}

	// columns of the TreeTable
	private IColumn columns[];

	/**
	 * Creates the TreeTable for the given array of columns.
	 * 
	 * @param parent
	 * @param id
	 * @param columns
	 */
	public TreeTable(MarkupContainer parent, String id, IColumn columns[])
	{
		super(parent, id);
		init(columns);
	}

	/**
	 * Creates the TreeTable for the given model and array of columns.
	 * 
	 * @param parent
	 *            The parent component
	 * @param id
	 *            The component id
	 * @param model
	 *            The tree model
	 * @param columns
	 *            The columns
	 */
	public TreeTable(MarkupContainer parent, String id, IModel<TreeModel> model, IColumn columns[])
	{
		super(parent, id, model);
		init(columns);
	}


	/**
	 * Creates the TreeTable for the given TreeModel and array of columns.
	 * 
	 * @param parent
	 *            The parent component
	 * @param id
	 *            The component id
	 * @param model
	 *            The tree model
	 * @param columns
	 *            The columns
	 */
	public TreeTable(MarkupContainer parent, String id, TreeModel model, IColumn columns[])
	{
		super(parent, id, model);
		init(columns);
	}
	
	private boolean hasLeftColumn() {
		for (int i = 0; i < columns.length; ++i) {
			if (columns[i].getLocation().getAlignment().equals(Alignment.LEFT))
				return true;
		}
		return false;
	}

	/**
	 * Adds the header to the TreeTable.
	 */
	protected void addHeader()
	{
		int i = 0;

		// create the view for side columns
		SideColumnsView sideColumns = new SideColumnsView(this, "sideColumns", null);
		for (IColumn column : columns)
		{
			if (column.getLocation().getAlignment() == Alignment.LEFT
 column.getLocation().getAlignment() == Alignment.RIGHT)
			{
				Component component = column.newHeader(sideColumns, "" + i++);
				sideColumns.addColumn(column, component, null);
			}
		}

		i = 0;

		// create the view for middle columns
		MiddleColumnsView middleColumns = new MiddleColumnsView(this, "middleColumns", null, hasLeftColumn());
		for (IColumn column : columns)
		{
			if (column.getLocation().getAlignment() == Alignment.MIDDLE)
			{
				Component component = column.newHeader(middleColumns, "" + i++);
				middleColumns.addColumn(column, component, null);
			}
		}
	};

	/**
	 * @see wicket.markup.html.tree.DefaultAbstractTree#getCSS()
	 */
	@Override
	protected ResourceReference getCSS()
	{
		return CSS;
	}

	/**
	 * Creates a new instance of the TreeFragment.
	 * 
	 * @param parent
	 *            The parent component
	 * @param id
	 *            The component id
	 * @param node
	 *            The tree node
	 * @param level
	 *            The level of the tree row
	 * @param renderNodeCallback
	 *            The node call back
	 * @return The tree panel
	 */
	protected Component newTreePanel(MarkupContainer<?> parent, String id, final TreeNode node,
			int level, IRenderNodeCallback renderNodeCallback)
	{
		return new TreeFragment(parent, id, node, level, renderNodeCallback);
	}

	/**
	 * @see AbstractTree#onBeforeAttach()
	 */
	@Override
	protected void onBeforeAttach()
	{
		// has the header been added yet?
		if (get("sideColumns") == null)
		{
			// no. initialize columns first
			for (IColumn column : columns)
			{
				column.setTreeTable(this);
			}

			// add the tree table header
			addHeader();
		}

		super.onAttach();
	}

	/**
	 * Populates one row of the tree.
	 * 
	 * @param item
	 *            the tree node component
	 * @param level
	 *            the current level
	 */
	@Override
	protected void populateTreeItem(WebMarkupContainer<TreeNode> item, int level)
	{
		final TreeNode node = item.getModelObject();

		int i = 0;

		// add side columns
		SideColumnsView sideColumns = new SideColumnsView(item, "sideColumns", node);
		for (IColumn column : columns)
		{
			if (column.getLocation().getAlignment() == Alignment.LEFT
 column.getLocation().getAlignment() == Alignment.RIGHT)
			{
				Component component;
				// first try to create a renderable
				IRenderable renderable = column.newCell(node, level);

				if (renderable == null)
				{
					// if renderable failed, try to create a regular component
					component = column.newCell(sideColumns, "" + i++, node, level);
				}
				else
				{
					component = null;
				}

				sideColumns.addColumn(column, component, renderable);
			}
		}

		i = 0;

		// add middle columns
		MiddleColumnsView middleColumns = new MiddleColumnsView(item, "middleColumns", node, hasLeftColumn());
		for (IColumn column : columns)
		{
			if (column.getLocation().getAlignment() == Alignment.MIDDLE)
			{
				Component component;
				// first try to create a renderable
				IRenderable renderable = column.newCell(node, level);

				if (renderable == null)
				{
					// if renderable failed, try to create a regular component
					component = column.newCell(middleColumns, "" + i++, node, level);
				}
				else
				{
					component = null;
				}

				middleColumns.addColumn(column, component, renderable);
			}
		}

		// do distinguish between selected and unselected rows we add an
		// behavior
		// that modifies row css class.
		item.add(new AbstractBehavior()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentTag(Component component, ComponentTag tag)
			{
				super.onComponentTag(component, tag);
				if (getTreeState().isNodeSelected(node))
				{
					tag.put("class", "row-selected");
				}
				else
				{
					tag.put("class", "row");
				}
			}
		});
	}

	/**
	 * Internal initialization. Also checks if at least one of the columns is
	 * derived from AbstractTreeColumn.
	 * 
	 * @param columns
	 *            The columns
	 */
	private void init(IColumn columns[])
	{
		boolean found = false;
		for (IColumn column : columns)
		{
			if (column instanceof AbstractTreeColumn)
			{
				found = true;
				break;
			}
		}
		if (found == false)
		{
			throw new IllegalArgumentException(
					"At least one column in TreeTable must be derived from AbstractTreeColumn.");
		}

		this.columns = columns;

		// Attach the javascript that resizes the header according to the body
		// This is necessary to support fixed position header. The header does not
		// scroll together with body. The body contains vertical scrollbar. The 
		// header width must be same as body content width, so that the columns
		// are properly aligned.
		new Label(this, "attachJavascript", "Wicket.TreeTable.attachUpdate(\"" + getMarkupId()
				+ "\");").setEscapeModelStrings(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16765.java