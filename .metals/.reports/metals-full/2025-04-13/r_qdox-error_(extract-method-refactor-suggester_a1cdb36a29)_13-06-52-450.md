error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/741.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/741.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/741.java
text:
```scala
g@@etPage(requestCycle).render();

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
package org.apache.wicket.request.target.component;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.apache.wicket.IPageFactory;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.settings.IRequestCycleSettings;

/**
 * Default implementation of {@link IBookmarkablePageRequestTarget}. Target that denotes a page that
 * is to be created from the provided page class. This is typically used for redirects to
 * bookmarkable pages or mounted pages.
 * 
 * <p>
 * We cannot issue redirects even with {@link IRequestCycleSettings#REDIRECT_TO_RENDER} because the
 * url has to be bookmarkable, eg user actually being able to bookmark it in the browser. if we
 * always redirected to a stateful url (e.g. /?wicket:interface=:0::::)that wouldn't work, as it
 * wouldn't be bookmarkable.
 * 
 * @author Eelco Hillenius
 * @author Igor Vaynberg (ivaynberg)
 */
public class BookmarkablePageRequestTarget implements IBookmarkablePageRequestTarget
{
	/** the page that was created in response for cleanup */
	private Page page;

	/**
	 * the class of the page. FIXME javadoc why use weak reference?
	 * */
	private final WeakReference<Class<? extends Page>> pageClassRef;

	/** optional page map name. */
	private final String pageMapName;

	/** optional page parameters. */
	private final PageParameters pageParameters;

	/**
	 * Construct.
	 * 
	 * @param <C>
	 * 
	 * @param pageClass
	 *            the class of the page
	 */
	public <C extends Page> BookmarkablePageRequestTarget(Class<C> pageClass)
	{
		this(null, pageClass);
	}

	/**
	 * Construct.
	 * 
	 * @param <C>
	 * 
	 * @param pageClass
	 *            the class of the page
	 * @param pageParameters
	 *            optional page parameters
	 */
	public <C extends Page> BookmarkablePageRequestTarget(Class<C> pageClass,
		PageParameters pageParameters)
	{
		this(null, pageClass, pageParameters);
	}

	/**
	 * Construct.
	 * 
	 * @param <C>
	 * 
	 * @param pageMapName
	 *            optional page map name
	 * 
	 * @param pageClass
	 *            the class of the page
	 */
	public <C extends Page> BookmarkablePageRequestTarget(String pageMapName, Class<C> pageClass)
	{
		this(pageMapName, pageClass, null);
	}

	/**
	 * Construct.
	 * 
	 * @param <C>
	 *            type of page
	 * 
	 * @param pageMapName
	 *            optional page map name
	 * @param pageClass
	 *            the class of the page
	 * @param pageParameters
	 *            optional page parameters
	 */
	public <C extends Page> BookmarkablePageRequestTarget(String pageMapName, Class<C> pageClass,
		PageParameters pageParameters)
	{
		if (pageClass == null)
		{
			throw new IllegalArgumentException("Argument pageClass must be not null");
		}

		if (!Page.class.isAssignableFrom(pageClass))
		{
			throw new IllegalArgumentException("Argument pageClass must be an instance of " +
				Page.class.getName());
		}
		pageClassRef = new WeakReference<Class<? extends Page>>(pageClass);
		this.pageParameters = (pageParameters == null) ? new PageParameters() : pageParameters;
		this.pageMapName = pageMapName;
	}

	/**
	 * @see org.apache.wicket.IRequestTarget#detach(org.apache.wicket.RequestCycle)
	 */
	public void detach(RequestCycle requestCycle)
	{
		if (page != null)
		{
			page.detach();
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		if (obj != null && (obj instanceof BookmarkablePageRequestTarget))
		{
			BookmarkablePageRequestTarget that = (BookmarkablePageRequestTarget)obj;
			if (getPageClass().equals(that.getPageClass()))
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
	 * @return The page that was created, null if the response did not happen yet
	 */
	public final Page getPage()
	{
		return page;
	}

	protected final void setPage(Page page)
	{
		this.page = page;
	}


	/**
	 * @see org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget#getPageClass()
	 */
	public final Class<? extends Page> getPageClass()
	{
		return pageClassRef.get();
	}

	/**
	 * @see org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget#getPageMapName()
	 */
	public final String getPageMapName()
	{
		return pageMapName;
	}

	/**
	 * @see org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget#getPageParameters()
	 */
	public final PageParameters getPageParameters()
	{
		return pageParameters;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = "BookmarkablePageRequestTarget".hashCode();
		result += getPageClass().hashCode();
		result += pageMapName != null ? pageMapName.hashCode() : 0;
		return 17 * result;
	}

	/*
	 * @see
	 * org.apache.wicket.request.target.IEventProcessor#processEvents(org.apache.wicket.RequestCycle
	 * )
	 */
	public void processEvents(RequestCycle requestCycle)
	{
		if (!requestCycle.isRedirect())
		{
			page = getPage(requestCycle);
		}
	}

	/*
	 * @see org.apache.wicket.IRequestTarget#respond(org.apache.wicket.RequestCycle)
	 */
	public void respond(RequestCycle requestCycle)
	{
		if (pageClassRef != null && pageClassRef.get() != null)
		{
			if (requestCycle.isRedirect())
			{
				IRequestCycleProcessor processor = requestCycle.getProcessor();
				String redirectUrl = processor.getRequestCodingStrategy()
					.encode(requestCycle, this)
					.toString();
				// WICKET-1916 - if we are redirecting to homepage, then redirectUrl equals "./",
				// and if we strip it to blank, no redirect occurs
				if (redirectUrl.startsWith("./") && redirectUrl.length() > 2)
				{
					redirectUrl = redirectUrl.substring(2);
				}
				requestCycle.getResponse().redirect(redirectUrl);
			}
			else
			{
				// Let the page render itself
				getPage(requestCycle).renderPage();
			}
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[BookmarkablePageRequestTarget@" + hashCode() + " pageClass=" +
			getPageClass().getName() + "]";
	}

	/**
	 * Constructs a new instance of a page given its class name
	 * 
	 * @param <C>
	 *            type of page
	 * 
	 * @param pageClass
	 *            class name of the page to be created
	 * @param requestCycle
	 *            request cycle
	 * @return new instance of page
	 */
	protected <C extends Page> Page newPage(final Class<C> pageClass,
		final RequestCycle requestCycle)
	{
		// Construct a new instance using the default page factory
		IPageFactory pageFactory = requestCycle.getApplication()
			.getSessionSettings()
			.getPageFactory();

		if (pageParameters == null || pageParameters.size() == 0)
		{
			return pageFactory.newPage(pageClass);
		}
		else
		{
			// Add bookmarkable params in for WICKET-400.
			final Map<String, String[]> requestMap = requestCycle.getRequest().getParameterMap();
			requestMap.putAll(pageParameters.toRequestParameters());
			return pageFactory.newPage(pageClass, pageParameters);
		}
	}

	/**
	 * Gets a newly constructed page if we are not in a redirect.
	 * 
	 * @param requestCycle
	 *            the request cycle
	 * @return the page
	 */
	protected final Page getPage(RequestCycle requestCycle)
	{
		if (page == null && !requestCycle.isRedirect())
		{
			page = newPage(getPageClass(), requestCycle);
		}
		return page;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/741.java