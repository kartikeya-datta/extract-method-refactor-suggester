error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6912.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6912.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6912.java
text:
```scala
public P@@agingNavigationIncrementLink(MarkupContainer parent,final String id, final IPageable pageable,

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
package wicket.markup.html.navigation.paging;

import wicket.MarkupContainer;
import wicket.Page;
import wicket.markup.html.link.Link;

/**
 * An incremental link to a page of a PageableListView. Assuming your list view
 * navigation looks like
 * 
 * <pre>
 *    
 *   	 [first / &lt;&lt; / &lt;] 1 | 2 | 3 [&gt; / &gt;&gt; /last]
 *   	
 * </pre>
 * 
 * <p>
 * and "&lt;" meaning the previous and "&lt;&lt;" goto the "current page - 5",
 * than it is this kind of incremental page links which can easily be created.
 * 
 * @author Juergen Donnerstag
 * @author Martijn Dashorst
 */
public class PagingNavigationIncrementLink extends Link
{
	private static final long serialVersionUID = 1L;

	/** The increment. */
	private final int increment;

	/** The PageableListView the page links are referring to. */
	protected final IPageable pageable;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageable
	 *            The pageable component the page links are referring to
	 * @param increment
	 *            increment by
	 */
	public PagingNavigationIncrementLink(MarkupContainer<?> parent,final String id, final IPageable pageable,
			final int increment)
	{
		super(parent,id);
		setAutoEnable(true);
		this.increment = increment;
		this.pageable = pageable;
	}

	/**
	 * @see wicket.markup.html.link.Link#onClick()
	 */
	@Override
	public void onClick()
	{
		// Tell the PageableListView which page to print next
		pageable.setCurrentPage(getPageNumber());

		// We do need to redirect, else refresh refresh will go to next, next
		setRedirect(true);

		// Return the current page.
		setResponsePage(getPage());
	}

	/**
	 * Determines the next page number for the pageable component.
	 * 
	 * @return the new page number
	 */
	public final int getPageNumber()
	{
		// Determine the page number based on the current
		// PageableListView page and the increment
		int idx = pageable.getCurrentPage() + increment;

		// make sure the index lies between 0 and the last page
		return Math.max(0, Math.min(pageable.getPageCount() - 1, idx));
	}

	/**
	 * @return True if it is referring to the first page of the underlying
	 *         PageableListView.
	 */
	public boolean isFirst()
	{
		return pageable.getCurrentPage() <= 0;
	}

	/**
	 * @return True if it is referring to the last page of the underlying
	 *         PageableListView.
	 */
	public boolean isLast()
	{
		return pageable.getCurrentPage() >= (pageable.getPageCount() - 1);
	}

	/**
	 * Returns true if the page link links to the given page.
	 * 
	 * @param page
	 *            ignored
	 * @return True if this link links to the given page
	 * @see wicket.markup.html.link.PageLink#linksTo(wicket.Page)
	 */
	@Override
	public boolean linksTo(final Page page)
	{
		int currentPage = pageable.getCurrentPage();
		if (((increment < 0) && isFirst()) || ((increment > 0) && isLast()))
		{
			return true;
		}

		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6912.java