error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12244.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12244.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12244.java
text:
```scala
final i@@nt nextId()

/*
 * $Id$ $Revision$
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
package wicket;

import java.io.Serializable;
import java.util.Iterator;

import wicket.request.IRequestCycleProcessor;
import wicket.request.IRequestEncoder;
import wicket.util.collections.MostRecentlyUsedMap;

/**
 * THIS CLASS IS NOT PART OF THE WICKET PUBLIC API. DO NOT ATTEMPT TO USE IT.
 * 
 * A container for pages held in the session.
 * 
 * @author Jonathan Locke
 */
public final class PageMap implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** Default page map name */
	public static final String defaultName = "main";

	/** URL to continue to after a given page. */
	private String interceptContinuationURL;

	/** Name of this page map */
	private final String name;

	/** Next available page identifier. */
	private int pageId = 0;

	/** The still-live pages for this user session. */
	private transient MostRecentlyUsedMap pages;

	/** The session where this PageMap resides */
	private transient Session session;

	/**
	 * Visitor interface for visiting pages
	 * 
	 * @author Jonathan Locke
	 */
	static interface IVisitor
	{
		/**
		 * @param page
		 *            The page
		 */
		public void page(final Page page);
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of this page map
	 * @param session
	 *            The session holding this page map
	 */
	public PageMap(final String name, final Session session)
	{
		this.name = name;
		this.session = session;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * @return Returns the name.
	 */
	public final String getName()
	{
		return name;
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * @return True if this is the default page map
	 */
	public final boolean isDefault()
	{
		return name.equals(defaultName);
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * 
	 * Removes this PageMap from the Session.
	 */
	public final void remove()
	{
		session.removePageMap(this);
	}

	/**
	 * @return The next id for this pagemap
	 */
	final int getNextId()
	{
		session.dirty();
		return this.pageId++;
	}

	/**
	 * Redirects to any intercept page previously specified by a call to
	 * redirectToInterceptPage.
	 * 
	 * @return True if an original destination was redirected to
	 * @see PageMap#redirectToInterceptPage(Page)
	 */
	final boolean continueToOriginalDestination()
	{
		// Get request cycle
		final RequestCycle cycle = session.getRequestCycle();

		// If there's a place to go to
		if (interceptContinuationURL != null)
		{
			// Redirect there
			cycle.getResponse().redirect(interceptContinuationURL);

			// Since we are explicitly redirecting to a page already, we do not
			// want a second redirect to occur automatically
			cycle.setRedirect(false);

			// Reset interception URL
			interceptContinuationURL = null;

			// TODO why this?
			session.dirty();
			return true;
		}
		return false;
	}

	/**
	 * @param id
	 *            The identifier
	 * @return Any page having the given id
	 */
	final Page get(final String id)
	{
		final Page page = (Page)getPages().get(id);
		if (page != null)
		{
			page.setDirty(true);
		}
		return page;
	}

	/**
	 * @param page
	 *            The page to put into this map
	 * @return Any page that was removed
	 */
	final Page put(final Page page)
	{
		MostRecentlyUsedMap pages = getPages();
		pages.put(page.getId(), page);
		return (Page)pages.getRemovedValue();
	}

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The
	 * current request's url is saved for future use by method
	 * continueToOriginalDestination(); Only use this method when you plan to
	 * continue to the current url at some later time; otherwise just use
	 * setResponsePage or - when you are in a constructor or checkAccessMethod,
	 * call redirectTo.
	 * 
	 * @param page
	 *            The sign in page
	 */
	final void redirectToInterceptPage(final Page page)
	{
		final RequestCycle cycle = session.getRequestCycle();
		IRequestCycleProcessor processor = cycle.getRequestCycleProcessor();
		IRequestEncoder encoder = processor.getRequestEncoder();
		// TODO this conflicts with the use of IRequestEncoder. We should get
		// rid of encodeURL in favor of IRequestEncoder
		interceptContinuationURL = page.getResponse().encodeURL(cycle.getRequest().getURL());
		cycle.redirectTo(page);

		// TODO why this?
		session.dirty();
	}

	/**
	 * @param page
	 *            The page to remove
	 */
	final void remove(final Page page)
	{
		getPages().remove(page.getId());
	}

	/**
	 * Removes all pages from this map
	 */
	final void removeAll()
	{
		getPages().clear();
	}

	/**
	 * @param session
	 *            Session to set
	 */
	final void setSession(final Session session)
	{
		this.session = session;
	}

	/**
	 * @param visitor
	 *            The visitor to call at each Page in this PageMap.
	 */
	final void visitPages(final IVisitor visitor)
	{
		if (pages != null)
		{
			for (final Iterator iterator = pages.values().iterator(); iterator.hasNext();)
			{
				visitor.page((Page)iterator.next());
			}
		}
	}

	/**
	 * @return MRU map of pages
	 */
	private final MostRecentlyUsedMap getPages()
	{
		if (this.pages == null)
		{
			this.pages = new MostRecentlyUsedMap(session.getApplication().getSettings()
					.getMaxPages());
		}
		return this.pages;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12244.java