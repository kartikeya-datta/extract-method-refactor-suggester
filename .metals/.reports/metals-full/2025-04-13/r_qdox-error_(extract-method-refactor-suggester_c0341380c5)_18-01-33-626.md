error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8243.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8243.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8243.java
text:
```scala
r@@eturn pageableListView.getPageCount();

/*
 * $Id: PageableListViewNavigation.java,v 1.3 2005/02/17 06:13:40 jonathanlocke
 * Exp $ $Revision$ $Date$
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
package wicket.markup.html.list;

import java.util.AbstractList;

import wicket.markup.html.basic.Label;
import wicket.model.DetachableModel;
import wicket.model.IModel;

/**
 * A navigation for a PageableListView that holds links to other pages of the
 * PageableListView.
 * <p>
 * For each row (one page of the list of pages) a
 * {@link PageableListViewNavigationLink}will be added that contains a
 * {@link Label}with the page number of that link (1..n).
 * 
 * <pre>
 * 
 *          &lt;td id=&quot;wicket-navigation&quot;&gt;
 *              &lt;a id=&quot;wicket-pageLink&quot; href=&quot;SearchCDPage.html&quot;&gt;
 *                 &lt;span id=&quot;wicket-pageNumber&quot;/&gt;
 *              &lt;/a&gt;
 *          &lt;/td&gt;
 *  
 * </pre>
 * 
 * thus renders like:
 * 
 * <pre>
 * 
 *          1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 |
 *  
 * </pre>
 * 
 * </p>
 * <p>
 * Override method populateItem to customize the rendering of the navigation.
 * For instance:
 * 
 * <pre>
 * 
 * protected void populateItem(ListItem listItem)
 * {
 * 	final int page = ((Integer)listItem.getModelObject()).intValue();
 * 	final PageableListViewNavigationLink link = new PageableListViewNavigationLink(&quot;pageLink&quot;,
 * 			pageableListView, page);
 * 	if (page &gt; 0)
 * 	{
 * 		listItem.add(new Label(&quot;separator&quot;, &quot;|&quot;));
 * 	}
 * 	else
 * 	{
 * 		listItem.add(new Label(&quot;separator&quot;, &quot;&quot;));
 * 	}
 * 	link.add(new Label(&quot;pageNumber&quot;, String.valueOf(page + 1)));
 * 	link.add(new Label(&quot;pageLabel&quot;, &quot;page&quot;));
 * 	listItem.add(link);
 * }
 * </pre>
 * 
 * With:
 * 
 * <pre>
 * 
 *          &lt;td id=&quot;wicket-navigation&quot;&gt;
 *              &lt;span id=&quot;wicket-separator&quot;/&gt;
 *              &lt;a id=&quot;wicket-pageLink&quot; href=&quot;#&quot;&gt;
 *                &lt;span id=&quot;wicket-pageLabel&quot;/&gt;&lt;span id=&quot;wicket-pageNumber&quot;/&gt;
 *              &lt;/a&gt;
 *          &lt;/td&gt;
 *  
 * </pre>
 * 
 * renders like:
 * 
 * <pre>
 * page1 | page2 | page3 | page4 | page5 | page6 | page7 | page8 | page9
 * </pre>
 * 
 * </p>
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Juergen Donnerstag
 */
public class PageableListViewNavigation extends ListView
{
	/** Serial Version ID. */
	private static final long serialVersionUID = 8591577491410447609L;

	/** The PageableListView this navigation is navigating. */
	protected PageableListView pageableListView;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name of the component
	 * @param pageableListView
	 *            The underlying list view to navigate
	 */
	public PageableListViewNavigation(final String name,
			final PageableListView pageableListView)
	{
		super(name, (IModel)null);

		this.pageableListView = pageableListView;
		this.setStartIndex(0);
	}

	/**
	 * @see wicket.Component#initModel()
	 */
	protected IModel initModel()
	{
		return new DetachableModel()
		{
			protected void onAttach()
			{
				setObject(new AbstractList()
				{
					public Object get(final int index)
					{
						return new Integer(index);
					}

					public int size()
					{
						return Integer.MAX_VALUE;
					}
				});
			}

			protected void onDetach()
			{
				setObject(null);
			}
		};
	}

	/**
	 * Get the number of page links per "window".
	 * 
	 * @see wicket.markup.html.list.ListView#setViewSize(int)
	 * @return The overall number of page links (number of PageableListView
	 *         pages)
	 */
	public int getViewSize()
	{
		return Math.min(pageableListView.getPageCount(), super.getViewSize());
	}

	/**
	 * Populate the current cell with a page link
	 * (PageableListViewNavigationLink) enclosing the page number the link is
	 * pointing to. Subclasses may provide there own implementation adding more
	 * sophisticated page links.
	 * 
	 * @param listItem
	 *            the list item to populate
	 * @see wicket.markup.html.list.PageableListView#populateItem(wicket.markup.html.list.ListItem)
	 */
	protected void populateItem(final ListItem listItem)
	{
		// Get the index of page this link shall point to
		final int pageIndex = ((Integer)listItem.getModelObject()).intValue();

		// Add a page link pointing to the page
		final PageableListViewNavigationLink link = new PageableListViewNavigationLink("pageLink",
				pageableListView, pageIndex);
		listItem.add(link);

		// Add a label (the page number) to the list which is enclosed by the
		// link
		link.add(new Label("pageNumber", String.valueOf(pageIndex + 1)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8243.java