error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12308.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12308.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12308.java
text:
```scala
public A@@bstractPageableView(String id, IModel<? extends Collection<? extends T>> model)

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
package org.apache.wicket.markup.repeater;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.version.undo.Change;


/**
 * An abstract repeater view that provides paging functionality to its subclasses.
 * <p>
 * The view is populated by overriding the <code>getItemModels(int offset, int count)</code> method
 * and providing an iterator that returns models for items in the current page. The
 * AbstractPageableView builds the items that will be rendered by looping over the models and
 * calling the <code>newItem(String id, int index, IModel model)</code> to generate the child item
 * container followed by <code>populateItem(Component item)</code> to let the user populate the
 * newly created item container with with custom components.
 * </p>
 * 
 * @see org.apache.wicket.markup.repeater.RefreshingView
 * @see org.apache.wicket.markup.html.navigation.paging.IPageable
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 * @param <T>
 *            Model object type
 */
public abstract class AbstractPageableView<T> extends RefreshingView<T> implements IPageable
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Keeps track of the number of items we show per page. The default is Integer.MAX_VALUE which
	 * effectively disables paging.
	 */
	private int itemsPerPage = Integer.MAX_VALUE;

	/**
	 * Keeps track of the current page number.
	 */
	private int currentPage;

	/**
	 * <code>cachedItemCount</code> is used to cache the call to <code>internalGetItemCount()</code>
	 * for the duration of the request because that call can potentially be expensive ( a select
	 * count query ) and so we do not want to execute it multiple times.
	 */
	private transient int cachedItemCount;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractPageableView(String id, IModel<Collection<T>> model)
	{
		super(id, model);
		clearCachedItemCount();
	}

	/** @see org.apache.wicket.Component#Component(String) */
	public AbstractPageableView(String id)
	{
		super(id);
		clearCachedItemCount();
	}

	/**
	 * This method retrieves the subset of models for items in the current page and allows
	 * RefreshingView to generate items.
	 * 
	 * @return iterator over models for items in the current page
	 */
	@Override
	protected Iterator<IModel<T>> getItemModels()
	{
		int offset = getViewOffset();
		int size = getViewSize();

		Iterator<IModel<T>> models = getItemModels(offset, size);

		models = new CappedIteratorAdapter<T>(models, size);

		return models;
	}

	/**
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		clearCachedItemCount();
		super.onBeforeRender();
	}

	/**
	 * Returns an iterator over models for items in the current page
	 * 
	 * @param offset
	 *            index of first item in this page
	 * @param size
	 *            number of items that will be shown in the current page
	 * @return an iterator over models for items in the current page
	 */
	protected abstract Iterator<IModel<T>> getItemModels(int offset, int size);

	// /////////////////////////////////////////////////////////////////////////
	// ITEM COUNT CACHE
	// /////////////////////////////////////////////////////////////////////////

	private void clearCachedItemCount()
	{
		cachedItemCount = -1;
	}

	private void setCachedItemCount(int itemCount)
	{
		cachedItemCount = itemCount;
	}

	private int getCachedItemCount()
	{
		if (cachedItemCount < 0)
		{
			throw new IllegalStateException("getItemCountCache() called when cache was not set");
		}
		return cachedItemCount;
	}

	private boolean isItemCountCached()
	{
		return cachedItemCount >= 0;
	}

	// /////////////////////////////////////////////////////////////////////////
	// PAGING
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * @return maximum number of items that will be shown per page
	 */
	protected final int internalGetRowsPerPage()
	{
		return itemsPerPage;
	}

	/**
	 * Sets the maximum number of items to show per page. The current page will also be set to zero
	 * 
	 * @param items
	 */
	protected final void internalSetRowsPerPage(int items)
	{
		if (items < 1)
		{
			throw new IllegalArgumentException("Argument [itemsPerPage] cannot be less than 1");
		}

		if (itemsPerPage != items)
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					private static final long serialVersionUID = 1L;

					final int old = itemsPerPage;

					@Override
					public void undo()
					{
						itemsPerPage = old;
					}

					@Override
					public String toString()
					{
						return "ItemsPerPageChange[component: " + getPath() + ", itemsPerPage: " +
							old + "]";
					}
				});
			}
		}

		itemsPerPage = items;

		// because items per page can effect the total number of pages we always
		// reset the current page back to zero
		setCurrentPage(0);
	}

	/**
	 * @return total item count
	 */
	protected abstract int internalGetItemCount();

	/**
	 * Get the row count.
	 * 
	 * @see #getItemCount()
	 * 
	 * @return total item count, but 0 if not visible in the hierarchy
	 */
	public final int getRowCount()
	{
		if (!isVisibleInHierarchy())
		{
			return 0;
		}

		return getItemCount();
	}

	/**
	 * Get the item count. Since dataprovider.size() could potentially be expensive, the item count
	 * is cached.
	 * 
	 * @see #getRowCount()
	 * 
	 * @return the item count
	 */
	public final int getItemCount()
	{
		if (isItemCountCached())
		{
			return getCachedItemCount();
		}

		int count = internalGetItemCount();

		setCachedItemCount(count);
		return count;
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
	 */
	public final int getCurrentPage()
	{
		int page = currentPage;

		/*
		 * trim current page if its out of bounds this can happen if items are added/deleted between
		 * requests
		 */

		if (page > 0 && page >= getPageCount())
		{
			page = Math.max(getPageCount() - 1, 0);
			currentPage = page;
			return page;
		}

		return page;
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
	 */
	public final void setCurrentPage(int page)
	{
		// If page == 0, short-circuit the range check. This saves a call to
		// getPageCount(), but more importantly avoids it being called until
		// your AbstractPageableView is actually rendered.
		if (page != 0 && (page < 0 || (page >= getPageCount() && getPageCount() > 0)))
		{
			throw new IndexOutOfBoundsException("argument [page]=" + page + ", must be 0<=page<" +
				getPageCount());
		}

		if (currentPage != page)
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					private static final long serialVersionUID = 1L;

					private final int old = currentPage;

					@Override
					public void undo()
					{
						currentPage = old;
					}

					@Override
					public String toString()
					{
						return "CurrentPageChange[component: " + getPath() + ", currentPage: " +
							old + "]";
					}
				});

			}
		}
		currentPage = page;
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getPageCount()
	 */
	public final int getPageCount()
	{
		int total = getRowCount();
		int page = internalGetRowsPerPage();
		int count = total / page;

		if (page * count < total)
		{
			count++;
		}

		return count;

	}

	/**
	 * @return the index of the first visible item
	 */
	protected int getViewOffset()
	{
		return getCurrentPage() * internalGetRowsPerPage();
	}


	/**
	 * @return the number of items visible
	 */
	protected int getViewSize()
	{
		return Math.min(internalGetRowsPerPage(), getRowCount() - getViewOffset());
	}

	// /////////////////////////////////////////////////////////////////////////
	// HELPER CLASSES
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Iterator adapter that makes sure only the specified max number of items can be accessed from
	 * its delegate.
	 * 
	 * @param <T>
	 *            Model object type
	 */
	private static class CappedIteratorAdapter<T> implements Iterator<IModel<T>>
	{
		private final int max;
		private int index;
		private final Iterator<IModel<T>> delegate;

		/**
		 * Constructor
		 * 
		 * @param delegate
		 *            delegate iterator
		 * @param max
		 *            maximum number of items that can be accessed.
		 */
		public CappedIteratorAdapter(Iterator<IModel<T>> delegate, int max)
		{
			this.delegate = delegate;
			this.max = max;
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
			return (index < max) && delegate.hasNext();
		}

		/**
		 * @see java.util.Iterator#next()
		 */
		public IModel<T> next()
		{
			if (index >= max)
			{
				throw new NoSuchElementException();
			}
			index++;
			return delegate.next();
		}

	};

	/**
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach()
	{
		clearCachedItemCount();
		super.onDetach();
	}

	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException,
		ClassNotFoundException
	{
		// Read in all fields
		s.defaultReadObject();
		clearCachedItemCount();
	}
}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12308.java