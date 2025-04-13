error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4276.java
text:
```scala
w@@hile ((currentPage > 0) && ((currentPage * rowsPerPage) >= getList().size()))

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
package org.apache.wicket.markup.html.list;

import java.util.List;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.version.undo.Change;


/**
 * PageableListView is similar to ListView but provides in addition pageable views. A
 * PageableListView holds pageable rows of information. The rows can be re-ordered and deleted,
 * either one at a time or many at a time.
 * 
 * @author Jonathan Locke
 * @param <T>
 *            Model object type
 */
public abstract class PageableListView<T> extends ListView<T> implements IPageable
{
	private static final long serialVersionUID = 1L;

	/** The page to show. */
	private int currentPage;

	/** Number of rows per page of the list view. */
	private int rowsPerPage;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param rowsPerPage
	 *            Number of rows to show on a page
	 */
	public PageableListView(final String id, final IModel<List<T>> model, int rowsPerPage)
	{
		super(id, model);
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * Creates a pageable list view having the given number of rows per page that uses the provided
	 * object as a simple model.
	 * 
	 * @param id
	 *            See Component
	 * @param list
	 *            See Component
	 * @param rowsPerPage
	 *            Number of rows to show on a page
	 * @see ListView#ListView(String, List)
	 */
	public PageableListView(final String id, final List<T> list, final int rowsPerPage)
	{
		super(id, list);
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * Gets the index of the current page being displayed by this list view.
	 * 
	 * @return Returns the currentPage.
	 */
	public final int getCurrentPage()
	{
		// If first cell is out of range, bring page back into range
		while ((currentPage * rowsPerPage) >= getList().size())
		{
			currentPage--;
		}

		return currentPage;
	}

	/**
	 * Gets the number of pages in this list view.
	 * 
	 * @return The number of pages in this list view
	 */
	public final int getPageCount()
	{
		return ((getList().size() + rowsPerPage) - 1) / rowsPerPage;
	}

	/**
	 * Gets the maximum number of rows on each page.
	 * 
	 * @return the maximum number of rows on each page.
	 */
	public final int getRowsPerPage()
	{
		return rowsPerPage;
	}

	/**
	 * Sets the maximum number of rows on each page.
	 * 
	 * @param rowsPerPage
	 *            the maximum number of rows on each page.
	 */
	public final void setRowsPerPage(int rowsPerPage)
	{
		if (rowsPerPage < 0)
		{
			rowsPerPage = 0;
		}

		addStateChange(new RowsPerPageChange(this.rowsPerPage));
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @see org.apache.wicket.markup.html.list.ListView#getViewSize()
	 */
	@Override
	public int getViewSize()
	{
		if (getDefaultModelObject() != null)
		{
			super.setStartIndex(getCurrentPage() * getRowsPerPage());
			super.setViewSize(getRowsPerPage());
		}

		return super.getViewSize();
	}

	/**
	 * Sets the current page that this list view should show.
	 * 
	 * @param currentPage
	 *            The currentPage to set.
	 */
	public final void setCurrentPage(int currentPage)
	{
		if (currentPage < 0)
		{
			currentPage = 0;
		}

		int pageCount = getPageCount();
		if ((currentPage > 0) && (currentPage >= pageCount))
		{
			currentPage = pageCount - 1;
		}

		addStateChange(new CurrentPageChange(this.currentPage));
		this.currentPage = currentPage;
	}


	/**
	 * Prevent users from accidentally using it.
	 * 
	 * @see org.apache.wicket.markup.html.list.ListView#setStartIndex(int)
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public ListView<T> setStartIndex(int startIndex) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
			"You must not use setStartIndex() with PageableListView");
	}

	/**
	 * Prevent users from accidentally using it.
	 * 
	 * @param size
	 *            the view size
	 * @return This
	 * @throws UnsupportedOperationException
	 *             always
	 * @see org.apache.wicket.markup.html.list.ListView#setStartIndex(int)
	 */
	@Override
	public ListView<T> setViewSize(int size) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(
			"You must not use setViewSize() with PageableListView");
	}

	/**
	 * Records the changing of the current page.
	 */
	private class CurrentPageChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** the former 'current' page. */
		private final int currentPage;

		/**
		 * Construct.
		 * 
		 * @param currentPage
		 *            the former 'current' page
		 */
		CurrentPageChange(int currentPage)
		{
			this.currentPage = currentPage;
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		@Override
		public void undo()
		{
			setCurrentPage(currentPage);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "CurrentPageChange[currentPage: " + currentPage + "]";
		}
	}

	/**
	 * Records the changing of the number of rows per page.
	 */
	private class RowsPerPageChange extends Change
	{
		private static final long serialVersionUID = 1L;

		/** the former number of rows per page. */
		private final int rowsPerPage;

		/**
		 * Construct.
		 * 
		 * @param rowsPerPage
		 *            the former number of rows per page
		 */
		RowsPerPageChange(int rowsPerPage)
		{
			this.rowsPerPage = rowsPerPage;
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		@Override
		public void undo()
		{
			setRowsPerPage(rowsPerPage);
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "RowsPerPageChange[component: " + getPath() + ", prefix: " + rowsPerPage + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4276.java