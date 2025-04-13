error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15933.java
text:
```scala
private final I@@terator rows;

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
package org.apache.wicket.markup.repeater.data;

import java.util.Iterator;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.version.undo.Change;


/**
 * A pageable DataView which breaks the data in the IDataProvider into a number
 * of data-rows, depending on the column size. A typical use case is to show
 * items in a table with ie 3 columns where the table is filled left to right
 * top-down so that for each third item a new row is created.
 * <p>
 * Example
 * 
 * <pre>
 *   &lt;tbody&gt;
 *     &lt;tr wicket:id=&quot;rows&quot; class&quot;even&quot;&gt;
 *       &lt;td wicket:id=&quot;cols&quot;&gt;
 *         &lt;span wicket:id=&quot;id&quot;&gt;Test ID&lt;/span&gt;
 *       &lt;/td&gt;
 *     &lt;/tr&gt;
 *   &lt;/tbody&gt;  
 * </pre>
 * 
 * and in java:
 * 
 * <pre>
 * add(new GridView(&quot;rows&quot;, dataProvider).setColumns(3));
 * </pre>
 * 
 * @author Igor Vaynberg
 * @author Christian Essl
 * 
 */
public abstract class GridView extends DataViewBase
{

	private int columns = 1;
	private int rows = Integer.MAX_VALUE;


	/**
	 * @param id
	 *            component id
	 * @param dataProvider
	 *            data provider
	 */
	public GridView(String id, IDataProvider dataProvider)
	{
		super(id, dataProvider);
	}


	/**
	 * @return number of columns
	 */
	public int getColumns()
	{
		return columns;
	}

	/**
	 * Sets number of columns
	 * 
	 * @param cols
	 *            number of colums
	 * @return this for chaining
	 */
	public GridView setColumns(int cols)
	{
		if (cols < 1)
		{
			throw new IllegalArgumentException();
		}

		if (columns != cols)
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					private static final long serialVersionUID = 1L;

					final int old = columns;

					public void undo()
					{
						columns = old;
					}

					public String toString()
					{
						return "GridViewColumnsChange[component: " + getPath()
								+ ", removed columns: " + old + "]";
					}
				});
			}
			columns = cols;
		}
		updateItemsPerPage();
		return this;
	}

	/**
	 * @return number of rows per page
	 */
	public int getRows()
	{
		return rows;
	}

	/**
	 * Sets number of rows per page
	 * 
	 * @param rows
	 *            number of rows
	 * @return this for chaining
	 */
	public GridView setRows(int rows)
	{
		if (rows < 1)
		{
			throw new IllegalArgumentException();
		}

		if (this.rows != rows)
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					private static final long serialVersionUID = 1L;

					final int old = GridView.this.rows;

					public void undo()
					{
						GridView.this.rows = old;
					}

					public String toString()
					{
						return "GridViewRowsChange[component: " + getPath() + ", removed rows: "
								+ old + "]";
					}
				});
			}
			this.rows = rows;
		}

		// TODO Post 1.2: Performance: Can this be moved into the this.rows != rows if
		// block for optimization?
		updateItemsPerPage();
		return this;
	}

	private void updateItemsPerPage()
	{
		int items = Integer.MAX_VALUE;

		long result = (long)rows * (long)columns;

		// overflow check
		int desiredHiBits = -((int)(result >>> 31) & 1);
		int actualHiBits = (int)(result >>> 32);

		if (desiredHiBits == actualHiBits)
		{
			items = (int)result;
		}

		internalSetRowsPerPage(items);

	}


	protected void addItems(Iterator items)
	{
		if (items.hasNext())
		{
			final int cols = getColumns();

			int row = 0;

			do
			{
				// Build a row
				Item rowItem = newRowItem(newChildId(), row);
				RepeatingView rowView = new RepeatingView("cols");
				rowItem.add(rowView);
				add(rowItem);

				// Populate the row
				for (int index = 0; index < cols; index++)
				{
					final Item cellItem;
					if (items.hasNext())
					{
						cellItem = (Item)items.next();
					}
					else
					{
						cellItem = newEmptyItem(newChildId(), index);
						populateEmptyItem(cellItem);
					}
					rowView.add(cellItem);
				}

				// increase row
				row++;

			}
			while (items.hasNext());
		}

	}

	/**
	 * @return data provider
	 */
	public IDataProvider getDataProvider()
	{
		return internalGetDataProvider();
	}

	/**
	 * @see org.apache.wicket.extensions.markup.html.repeater.pageable.AbstractPageableView#getItems()
	 */
	public Iterator getItems()
	{
		return new ItemsIterator(iterator());
	}

	/**
	 * Add component to an Item for which there is no model anymore and is shown
	 * in a cell
	 * 
	 * @param item
	 *            Item object
	 */
	abstract protected void populateEmptyItem(Item item);

	/**
	 * Create a Item which represents an empty cell (there is no model for it in
	 * the DataProvider)
	 * 
	 * @param id
	 * @param index
	 * @return created item
	 */
	protected Item newEmptyItem(String id, int index)
	{
		return new Item(id, index, null);
	}

	/**
	 * Create a new Item which will hold a row.
	 * 
	 * @param id
	 * @param index
	 * @return created Item
	 */
	protected Item newRowItem(String id, int index)
	{
		return new Item(id, index, null);
	}

	/**
	 * Iterator that iterats over all items in the cells
	 * 
	 * @author igor
	 * 
	 */
	private static class ItemsIterator implements Iterator
	{
		private Iterator rows;
		private Iterator cells;

		private Item next;

		/**
		 * @param rows
		 *            iterator over child row views
		 */
		public ItemsIterator(Iterator rows)
		{
			this.rows = rows;
			findNext();
		}

		/**
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		/**
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext()
		{
			return next != null;
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public Object next()
		{
			Item item = next;
			findNext();
			return item;
		}

		private void findNext()
		{
			next = null;

			if (cells != null && cells.hasNext())
			{
				next = (Item)cells.next();
			}

			while (rows.hasNext())
			{
				MarkupContainer row = (MarkupContainer)rows.next();
				cells = ((MarkupContainer)row.iterator().next()).iterator();
				if (cells.hasNext())
				{
					next = (Item)cells.next();
					break;
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15933.java