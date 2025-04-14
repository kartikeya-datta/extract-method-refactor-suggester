error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9474.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9474.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9474.java
text:
```scala
s@@etRedirect(false);

/*
 * $Id: PageableListViewNavigationIncrementLink.java,v 1.3 2005/02/17 06:15:27
 * jonathanlocke Exp $ $Revision$ $Date$
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

import wicket.Page;
import wicket.markup.html.link.IPageLink;
import wicket.markup.html.link.PageLink;

/**
 * An incremental link to a page of a PageableListView. Assuming your list view
 * navigation looks like
 * 
 * <pre>
 * 
 *	 [first / &lt;&lt; / &lt;] 1 | 2 | 3 [&gt; / &gt;&gt; /last]
 *	
 * </pre>
 * 
 * <p>
 * and "&lt;" meaning the previous and "&lt;&lt;" goto the "current page - 5",
 * than it is this kind of incremental page links which can easily be created.
 * 
 * @author Juergen Donnerstag
 */
public class PageableListViewNavigationIncrementLink extends PageLink
{
	/** The increment. */
	private final int increment;

	/** The PageableListView the page links are referring to. */
	private final PageableListView pageableListView;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *			  See Component
	 * @param pageableListView
	 *			  The list view the page links are referring to
	 * @param increment
	 *			  increment by
	 */
	public PageableListViewNavigationIncrementLink(final String id,
			final PageableListView pageableListView, final int increment)
	{
		super(id, new IPageLink()
		{
			/**
			 * @see wicket.markup.html.link.IPageLink#getPage()
			 */
			public Page getPage()
			{
				// Determine the page number based on the current
				// PageableListView page
				// and the increment
				int idx = pageableListView.getCurrentPage() + increment;
				if (idx < 0)
				{
					idx = 0;
				}
				else if (idx > (pageableListView.getList().size() - 1))
				{
					idx = pageableListView.getList().size() - 1;
				}

				// Tell the PageableListView which page to print next
				pageableListView.setCurrentPage(idx);

				// Return the PageableListView page the link is referring to
				return pageableListView.getPage();
			}

			/**
			 * @see wicket.markup.html.link.IPageLink#getPageIdentity()
			 */
			public Class getPageIdentity()
			{
				return pageableListView.getPage().getClass();
			}
		});

		this.increment = increment;
		this.pageableListView = pageableListView;
	}

	/**
	 * Handles a link click by asking for a concrete Page instance through the
	 * IPageLink.getPage() delayed linking interface. This call will normally
	 * cause the destination page to be created.
	 * 
	 * @see wicket.markup.html.link.Link#onClick()
	 */
	public void onClick()
	{
		// We do not need to redirect
		getRequestCycle().setRedirect(false);
		
		super.onClick();
	}
	
	/**
	 * @return True if it is referring to the first page of the underlying
	 *		   PageableListView.
	 */
	public boolean isFirst()
	{
		return pageableListView.getCurrentPage() <= 0;
	}

	/**
	 * @return True if it is referring to the last page of the underlying
	 *		   PageableListView.
	 */
	public boolean isLast()
	{
		return pageableListView.getCurrentPage() >= (pageableListView.getPageCount() - 1);
	}

	/**
	 * Returns true if the page link links to the given page.
	 * 
	 * @param page
	 *			  The page to test
	 * @return True if this link links to the given page
	 * @see wicket.markup.html.link.PageLink#linksTo(wicket.Page)
	 */
	public boolean linksTo(final Page page)
	{
		int currentPage = pageableListView.getCurrentPage();
		if (((increment < 0) && (currentPage <= 0))
 ((increment > 0) && (currentPage >= (pageableListView.getPageCount() - 1))))
		{
			return true;
		}

		return false;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9474.java