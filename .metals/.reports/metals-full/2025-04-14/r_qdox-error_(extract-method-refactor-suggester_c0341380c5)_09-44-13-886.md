error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15930.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15930.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15930.java
text:
```scala
private final i@@nt startIndex;

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
package org.apache.wicket.markup.html.navigation.paging;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.version.undo.Change;

/**
 * A navigation for a PageableListView that holds links to other pages of the
 * PageableListView.
 * <p>
 * For each row (one page of the list of pages) a {@link PagingNavigationLink}will
 * be added that contains a {@link Label} with the page number of that link
 * (1..n).
 * 
 * <pre>
 *  
 * 	&lt;td wicket:id=&quot;navigation&quot;&gt;
 * 		&lt;a wicket:id=&quot;pageLink&quot; href=&quot;SearchCDPage.html&quot;&gt;
 * 			&lt;span wicket:id=&quot;pageNumber&quot;&gt;1&lt;/&gt;
 * 		&lt;/a&gt;
 * 	&lt;/td&gt;
 * 	
 * </pre>
 * 
 * thus renders like:
 * 
 * <pre>
 * 	1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
 * </pre>
 * 
 * </p>
 * <p>
 * Override method populateItem to customize the rendering of the navigation.
 * For instance:
 * 
 * <pre>
 * 	protected void populateItem(LoopItem loopItem) {
 * 		final int page = loopItem.getIteration();
 *		final PagingNavigationLink link = new PagingNavigationLink(&quot;pageLink&quot;, pageableListView, page);
 *		if (page &gt; 0) {
 *			loopItem.add(new Label(&quot;separator&quot;, &quot;|&quot;));
 *		} else {
 *			loopItem.add(new Label(&quot;separator&quot;, &quot;&quot;));
 *		}
 *		link.add(new Label(&quot;pageNumber&quot;, String.valueOf(page + 1)));
 *		link.add(new Label(&quot;pageLabel&quot;, &quot;page&quot;));
 *		loopItem.add(link);
 *	}
 * </pre>
 * 
 * With:
 * 
 * <pre>
 *	&lt;span wicket:id=&quot;navigation&quot;&gt;
 *		&lt;span wicket:id=&quot;separator&quot;&gt;&lt;/span&gt;
 *		&lt;a wicket:id=&quot;pageLink&quot; href=&quot;#&quot;&gt;
 *			&lt;span wicket:id=&quot;pageLabel&quot;&gt;&lt;/span&gt;&lt;span wicket:id=&quot;pageNumber&quot;&gt;&lt;/span&gt;
 *		&lt;/a&gt;
 *	&lt;/span&gt;
 * </pre>
 * 
 * renders like:
 * 
 * <pre>
 *	page1 | page2 | page3 | page4 | page5 | page6 | page7 | page8 | page9
 * </pre>
 * 
 * </p>
 * Assuming a PageableListView with 1000 entries and not more than 10 lines
 * shall be printed per page, the navigation bar would have 100 entries. Because
 * this is not feasible PagingNavigation's navigation bar is pageable as well.
 * <p>
 * The page links displayed are automatically adjusted based on the number of
 * page links to be displayed and a margin. The margin makes sure that the page
 * link pointing to the current page is not at the left or right end of the page
 * links currently printed and thus providing a better user experience.
 * <p>
 * Use setMargin() and setViewSize() to adjust the navigation's bar view size
 * and margin.
 * <p>
 * Please
 * 
 * @see PagingNavigator for a ready made component which already includes links
 *      to the first, previous, next and last page.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Juergen Donnerstag
 */
public class PagingNavigation extends Loop
{
	private static final long serialVersionUID = 1L;

	/**
	 * Undo change for navigation start index. Makes certain that back button
	 * works with paging in the navigator.
	 */
	private final class StartIndexChange extends Change
	{
		private static final long serialVersionUID = 1L;

		private int startIndex;

		/**
		 * Constructor, remembers the startIndex.
		 * 
		 * @param startIndex
		 *            the startIndex to remember.
		 */
		private StartIndexChange(int startIndex)
		{
			this.startIndex = startIndex;
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		public final void undo()
		{
			PagingNavigation.this.startIndex = startIndex;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "StartIndexChange[component: " + getPath() + ", prefix: " + startIndex + "]";
		}
	}

	/** The PageableListView this navigation is navigating. */
	protected IPageable pageable;

	/** The label provider for the text that the links should be displaying. */
	protected IPagingLabelProvider labelProvider;

	/** Offset for the Loop */
	private int startIndex;

	/**
	 * Number of links on the left and/or right to keep the current page link
	 * somewhere near the middle.
	 */
	private int margin = -1;

	/** Default separator between page numbers. Null: no separator. */
	private String separator = null;

	/**
	 * The maximum number of page links to show.
	 */
	private int viewSize = 10;


	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The underlying pageable component to navigate
	 */
	public PagingNavigation(final String id, final IPageable pageable)
	{
		this(id, pageable, null);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The underlying pageable component to navigate
	 * @param labelProvider
	 *            The label provider for the text that the links should be
	 *            displaying.
	 */
	public PagingNavigation(final String id, final IPageable pageable,
			final IPagingLabelProvider labelProvider)
	{
		super(id, pageable.getPageCount());
		this.pageable = pageable;
		this.labelProvider = labelProvider;
		startIndex = 0;
	}

	/**
	 * Gets the margin, default value is half the view size, unless explicitly
	 * set.
	 * 
	 * @return the margin
	 */
	public int getMargin()
	{
		if (margin == -1 && viewSize != 0)
		{
			return viewSize / 2;
		}
		return margin;
	}

	/**
	 * Gets the seperator.
	 * 
	 * @return the seperator
	 */
	public String getSeparator()
	{
		return separator;
	}

	/**
	 * Gets the view size (is fixed by user).
	 * 
	 * @return view size
	 */
	public int getViewSize()
	{
		return viewSize;
	}

	/**
	 * view size of the navigation bar.
	 * 
	 * @param size
	 */
	public void setViewSize(final int size)
	{
		this.viewSize = size;
	}

	/**
	 * Sets the margin.
	 * 
	 * @param margin
	 *            the margin
	 */
	public void setMargin(final int margin)
	{
		this.margin = margin;
	}

	/**
	 * Sets the seperator. Null meaning, no separator at all.
	 * 
	 * @param separator
	 *            the seperator
	 */
	public void setSeparator(final String separator)
	{
		this.separator = separator;
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	protected void onBeforeRender()
	{
		
		// PagingNavigation itself (as well as the PageableListView)
		// may have pages.

		// The index of the first page link depends on the PageableListView's
		// page currently printed.
		this.setStartIndex();
		super.onBeforeRender();
	}

	/**
	 * Allow subclasses replacing populateItem to calculate the current page
	 * number
	 * 
	 * @return start index
	 */
	protected final int getStartIndex()
	{
		return this.startIndex;
	}

	/**
	 * Populate the current cell with a page link (PagingNavigationLink)
	 * enclosing the page number the link is pointing to. Subclasses may provide
	 * there own implementation adding more sophisticated page links.
	 * 
	 * @see org.apache.wicket.markup.html.list.Loop#populateItem(Loop.LoopItem)
	 */
	protected void populateItem(final Loop.LoopItem loopItem)
	{
		// Get the index of page this link shall point to
		final int pageIndex = getStartIndex() + loopItem.getIteration();

		// Add a page link pointing to the page
		final PagingNavigationLink link = newPagingNavigationLink("pageLink", pageable, pageIndex);
		loopItem.add(link);

		// Add a page number label to the list which is enclosed by the link
		String label = "";
		if (labelProvider != null)
		{
			label = labelProvider.getPageLabel(pageIndex);
		}
		else
		{
			label = String.valueOf(pageIndex + 1);
		}
		link.add(new Label("pageNumber", label));
	}

	/**
	 * Factory method for creating page number links.
	 * 
	 * @param id
	 *            the component id.
	 * @param pageable
	 *            the pageable for the link
	 * @param pageIndex
	 *            the page index the link points to
	 * @return the page navigation link.
	 */
	protected PagingNavigationLink newPagingNavigationLink(String id, IPageable pageable,
			int pageIndex)
	{
		return new PagingNavigationLink(id, pageable, pageIndex);
	}

	/**
	 * Renders the page link. Add the separator if not the last page link
	 * 
	 * @see Loop#renderItem(Loop.LoopItem)
	 */
	protected void renderItem(final Loop.LoopItem loopItem)
	{
		// Call default implementation
		super.renderItem(loopItem);

		// Add separator if not last page
		if (separator != null && (loopItem.getIteration() != getIterations() - 1))
		{
			getResponse().write(separator);
		}
	}

	/**
	 * Get the first page link to render. Adjust the first page link based on
	 * the current PageableListView page displayed.
	 */
	private void setStartIndex()
	{
		// Which startIndex are we currently using
		int firstListItem = this.startIndex;

		// How many page links shall be displayed
		int viewSize = Math.min(getViewSize(), pageable.getPageCount());
		int margin = getMargin();

		// What is the PageableListView's page index to be displayed
		int currentPage = pageable.getCurrentPage();

		// Make sure the current page link index is within the current
		// window taking the left and right margin into account
		if (currentPage < (firstListItem + margin))
		{
			firstListItem = currentPage - margin;
		}
		else if ((currentPage >= (firstListItem + viewSize - margin)))
		{

			firstListItem = (currentPage + margin + 1) - viewSize;
		}

		// Make sure the first index is >= 0 and the last index is <=
		// than the last page link index.
		if ((firstListItem + viewSize) >= pageable.getPageCount())
		{
			firstListItem = pageable.getPageCount() - viewSize;
		}

		if (firstListItem < 0)
		{
			firstListItem = 0;
		}

		if ((viewSize != getIterations()) || (this.startIndex != firstListItem))
		{
			this.modelChanging();

			// Tell the ListView what the new start index shall be
			addStateChange(new StartIndexChange(this.startIndex));
			this.startIndex = firstListItem;

			this.setIterations(Math.min(viewSize, pageable.getPageCount()));

			this.modelChanged();

			// force all children to be re-rendered
			removeAll();
		}
	}

	/**
	 * Set the number of iterations.
	 * 
	 * @param i
	 *            the number of iterations
	 */
	private void setIterations(int i)
	{
		setModelObject(new Integer(i));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15930.java