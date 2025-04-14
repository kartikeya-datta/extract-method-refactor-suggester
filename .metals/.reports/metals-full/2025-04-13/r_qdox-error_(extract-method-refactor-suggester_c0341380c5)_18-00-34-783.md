error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12844.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12844.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12844.java
text:
```scala
r@@equestCycle.setUpdateSession(true);

/*
 * $Id: BookmarkablePageRequestTarget.java,v 1.3 2005/12/07 00:52:26 ivaynberg
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
package wicket.request.target;

import wicket.IPageFactory;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.request.IBookmarkablePageRequestTarget;
import wicket.request.IRequestCycleProcessor;
import wicket.request.target.mixin.IAccessChecker;

/**
 * Default implementation of {@link IBookmarkablePageRequestTarget}. Target
 * that denotes a page that is to be created from the provided page class. This
 * is typically used for redirects to bookmarkable pages or mounted pages.
 * 
 * @author Eelco Hillenius
 * @author Igor Vaynberg (ivaynberg)
 */
public class BookmarkablePageRequestTarget
		implements
			IBookmarkablePageRequestTarget,
			IAccessChecker
{
	/** the class of the page. */
	private final Class pageClass;

	/** optional page parameters. */
	private final PageParameters pageParameters;

	/** the page that was created in response for cleanup */
	private Page page;

	/** optional page map name. */
	private final String pageMapName;

	/**
	 * Construct.
	 * 
	 * @param pageClass
	 *            the class of the page
	 */
	public BookmarkablePageRequestTarget(Class pageClass)
	{
		this(null, pageClass);
	}

	/**
	 * Construct.
	 * 
	 * @param pageMapName
	 *            optional page map name
	 * 
	 * @param pageClass
	 *            the class of the page
	 */
	public BookmarkablePageRequestTarget(String pageMapName, Class pageClass)
	{
		this(null, pageClass, null);
	}

	/**
	 * Construct.
	 * 
	 * @param pageClass
	 *            the class of the page
	 * @param pageParameters
	 *            optional page parameters
	 */
	public BookmarkablePageRequestTarget(Class pageClass, PageParameters pageParameters)
	{
		this(null, pageClass, pageParameters);
	}

	/**
	 * Construct.
	 * 
	 * @param pageMapName
	 *            optional page map name
	 * @param pageClass
	 *            the class of the page
	 * @param pageParameters
	 *            optional page parameters
	 */
	public BookmarkablePageRequestTarget(String pageMapName, Class pageClass,
			PageParameters pageParameters)
	{
		if (pageClass == null)
		{
			throw new NullPointerException("argument pageClass must be not null");
		}

		if (!Page.class.isAssignableFrom(pageClass))
		{
			throw new IllegalArgumentException("pageClass must be an instance of "
					+ Page.class.getName());
		}
		this.pageClass = pageClass;
		this.pageParameters = pageParameters;
		this.pageMapName = pageMapName;
	}

	/**
	 * @see wicket.IRequestTarget#synchronizeOnSession(RequestCycle)
	 */
	public boolean synchronizeOnSession(RequestCycle requestCycle)
	{
		// we need to lock when we are not redirecting, i.e. we are
		// actually rendering the page
		return !requestCycle.getRedirect();
	}

	/**
	 * @see wicket.request.target.mixin.IAccessChecker#checkAccess(RequestCycle)
	 */
	public boolean checkAccess(RequestCycle requestCycle)
	{
		Page page = getPage(requestCycle);
		if (page != null)
		{
			return page.checkAccess();
		}
		return true;
	}

	/**
	 * Gets a newly constructed page if we are not in a redirect.
	 * 
	 * @param requestCycle
	 *            the request cycle
	 * @return the page
	 */
	private final Page getPage(RequestCycle requestCycle)
	{
		if (page == null && pageClass != null && !requestCycle.getRedirect())
		{
			page = newPage(pageClass, requestCycle);
		}
		return page;
	}

	/**
	 * Constructs a new instance of a page given its class name
	 * 
	 * @param pageClass
	 *            class name of the page to be created
	 * @param requestCycle
	 *            request cycle
	 * @return new instance of page
	 */
	protected Page newPage(Class pageClass, RequestCycle requestCycle)
	{
		PageParameters params = pageParameters;

		// TODO ? the parameters should already have been resolved ?
		/*
		 * if (isMounted()) { //decode page parameters from url Request request =
		 * requestCycle.getRequest(); String urlFragment =
		 * request.getPath().substring(mountPath.length()); params =
		 * paramsEncoder.decode(urlFragment); }
		 */
		// construct a new instance using the default page factory
		IPageFactory pageFactory = requestCycle.getApplication().getSettings()
				.getDefaultPageFactory();
		return pageFactory.newPage(pageClass, params);
	}

	/**
	 * @see wicket.IRequestTarget#respond(wicket.RequestCycle)
	 */
	public void respond(RequestCycle requestCycle)
	{
		if (pageClass != null)
		{
			if (requestCycle.getRedirect())
			{
				IRequestCycleProcessor processor = requestCycle.getRequestCycleProcessor();
				String redirectUrl = processor.getRequestEncoder().encode(requestCycle,
						new BookmarkablePageRequestTarget(pageClass, pageParameters));
				requestCycle.getResponse().redirect(redirectUrl);
			}
			else
			{
				requestCycle.setUpdateCluster(true);

				page = getPage(requestCycle);

				// let the page render itself
				page.doRender();
			}
		}
	}

	/**
	 * @see wicket.IRequestTarget#cleanUp(wicket.RequestCycle)
	 */
	public void cleanUp(RequestCycle requestCycle)
	{
		page = null;
	}

	/**
	 * @see wicket.request.IBookmarkablePageRequestTarget#getPageClass()
	 */
	public final Class getPageClass()
	{
		return pageClass;
	}

	/**
	 * @see wicket.request.IBookmarkablePageRequestTarget#getPageParameters()
	 */
	public final PageParameters getPageParameters()
	{
		return pageParameters;
	}

	/**
	 * @see wicket.request.IBookmarkablePageRequestTarget#getPageMapName()
	 */
	public final String getPageMapName()
	{
		return pageMapName;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean equal = false;
		if (obj != null && (obj instanceof BookmarkablePageRequestTarget))
		{
			BookmarkablePageRequestTarget that = (BookmarkablePageRequestTarget)obj;
			if (pageClass.equals(that.pageClass))
			{
				boolean mapMatch = false;

				if (pageMapName != null)
				{
					mapMatch = (that.pageMapName != null && pageMapName.equals(that.pageMapName));
				}
				else
				{
					mapMatch = (that.pageMapName == null);
				}

				equal = mapMatch;
			}
		}
		return equal;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int result = "BookmarkablePageRequestTarget".hashCode();
		result += pageClass.hashCode();
		result += pageMapName != null ? pageMapName.hashCode() : 0;
		return 17 * result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "BookmarkablePageRequestTarget@" + hashCode() + "{pageClass=" + pageClass.getName()
				+ "}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12844.java