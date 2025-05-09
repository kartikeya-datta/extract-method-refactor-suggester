error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3608.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3608.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3608.java
text:
```scala
r@@eturn Application.get().getMapperContext();

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
package org.apache.wicket.request.handler;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.page.IPageManager;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.IPageSource;
import org.apache.wicket.request.mapper.StalePageException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Args;

/**
 * Provides page instance for request handlers. Each of the constructors has just enough information
 * to get existing or create new page instance. Requesting or creating page instance is deferred
 * until {@link #getPageInstance()} is called.
 * <p>
 * Purpose of this class is to reduce complexity of both {@link IRequestMapper}s and
 * {@link IRequestHandler}s. {@link IRequestMapper} examines the URL, gathers all relevant
 * information about the page in the URL (combination of page id, page class, page parameters and
 * render count), creates {@link PageProvider} object and creates a {@link IRequestHandler} instance
 * that can use the {@link PageProvider} to access the page.
 * <p>
 * Apart from simplifying {@link IRequestMapper}s and {@link IRequestHandler}s {@link PageProvider}
 * also helps performance because creating or obtaining page from {@link IPageManager} is delayed
 * until the {@link IRequestHandler} actually requires the page.
 * 
 * @author Matej Knopp
 */
public class PageProvider implements IPageProvider
{
	private Integer renderCount;

	private IPageSource pageSource;

	private IRequestablePage pageInstance;

	private Class<? extends IRequestablePage> pageClass;

	private Integer pageId;

	private PageParameters pageParameters;

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return page instance with specified id.
	 * 
	 * @param pageId
	 * @param renderCount
	 *            optional argument
	 */
	public PageProvider(final int pageId, final Integer renderCount)
	{
		this.pageId = pageId;
		this.renderCount = renderCount;
	}

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return page instance with specified id if it exists and it's class matches pageClass. If
	 * none of these is true new page instance will be created.
	 * 
	 * @param pageId
	 * @param pageClass
	 * @param renderCount
	 *            optional argument
	 */
	public PageProvider(final int pageId, final Class<? extends IRequestablePage> pageClass,
		Integer renderCount)
	{
		this(pageId, pageClass, new PageParameters(), renderCount);
	}

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return page instance with specified id if it exists and it's class matches pageClass. If
	 * none of these is true new page instance will be created.
	 * 
	 * @param pageId
	 * @param pageClass
	 * @param pageParameters
	 * @param renderCount
	 *            optional argument
	 */
	public PageProvider(final int pageId, final Class<? extends IRequestablePage> pageClass,
		final PageParameters pageParameters, final Integer renderCount)
	{
		this.pageId = pageId;
		setPageClass(pageClass);
		setPageParameters(pageParameters);
		this.renderCount = renderCount;
	}

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return new instance of page with specified class.
	 * 
	 * @param pageClass
	 * @param pageParameters
	 */
	public PageProvider(final Class<? extends IRequestablePage> pageClass,
		final PageParameters pageParameters)
	{
		setPageClass(pageClass);
		if (pageParameters != null)
		{
			setPageParameters(pageParameters);
		}
	}

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return new instance of page with specified class.
	 * 
	 * @param pageClass
	 */
	public PageProvider(Class<? extends IRequestablePage> pageClass)
	{
		this(pageClass, null);
	}

	/**
	 * Creates a new page provider object. Upon calling of {@link #getPageInstance()} this provider
	 * will return the given page instance.
	 * 
	 * @param page
	 */
	public PageProvider(IRequestablePage page)
	{
		Args.notNull(page, "page");

		pageInstance = page;
	}

	/**
	 * @see org.apache.wicket.request.handler.IPageProvider#getPageInstance()
	 */
	public IRequestablePage getPageInstance()
	{
		if (pageInstance == null)
		{
			pageInstance = getPageInstance(pageId, pageClass, pageParameters, renderCount);
		}
		if (pageInstance == null)
		{
			throw new PageExpiredException("Page expired.");
		}
		touchPageInstance();
		return pageInstance;
	}

	/**
	 * @see org.apache.wicket.request.handler.IPageProvider#getPageParameters()
	 */
	public PageParameters getPageParameters()
	{
		if (pageParameters != null)
		{
			return pageParameters;
		}
		else if (pageInstance != null)
		{
			return pageInstance.getPageParameters();
		}
		else
		{
			return null;
		}
	}

	public boolean isNewPageInstance()
	{
		return pageInstance == null && pageId == null;
	}

	/**
	 * @see org.apache.wicket.request.handler.IPageProvider#getPageClass()
	 */
	public Class<? extends IRequestablePage> getPageClass()
	{
		if (pageClass != null)
		{
			return pageClass;
		}
		else
		{
			return getPageInstance().getClass();
		}
	}

	/**
	 * 
	 * @return always false
	 */
	protected boolean prepareForRenderNewPage()
	{
		return false;
	}

	protected IPageSource getPageSource()
	{
		if (pageSource != null)
		{
			return pageSource;
		}
		if (Application.exists())
		{
			return Application.get().getEncoderContext();
		}
		else
		{
			throw new IllegalStateException(
				"No application is bound to current thread. Call setPageSource() to manually assign pageSource to this provider.");
		}
	}

	private IRequestablePage getPageInstance(Integer pageId,
		Class<? extends IRequestablePage> pageClass, PageParameters pageParameters,
		Integer renderCount)
	{
		IRequestablePage page = null;

		boolean freshCreated = false;

		if (pageId != null)
		{
			page = getPageSource().getPageInstance(pageId);
			if (page != null && pageClass != null && page.getClass().equals(pageClass) == false)
			{
				page = null;
			}
			else if (page != null && pageParameters != null)
			{
				page.getPageParameters().overwriteWith(pageParameters);
			}
		}
		if (page == null)
		{
			if (pageClass != null)
			{
				page = getPageSource().newPageInstance(pageClass, pageParameters);
				freshCreated = true;
				if (prepareForRenderNewPage() && page instanceof Page)
				{
					((Page)page).internalPrepareForRender(false);
				}
			}
		}

		if (page != null && !freshCreated)
		{
			if (renderCount != null && page.getRenderCount() != renderCount)
			{
				throw new StalePageException(page);
			}
		}

		return page;
	}

	/**
	 * Detaches the page if it has been loaded (that means either
	 * {@link #PageProvider(IRequestablePage)} constructor has been used or
	 * {@link #getPageInstance()} has been called).
	 */
	public void detach()
	{
		if (pageInstance != null)
		{
			pageInstance.detach();
		}
	}

	/**
	 * If the {@link PageProvider} is used outside request thread (thread that does not have
	 * application instance assigned) it is necessary to specify a {@link IPageSource} instance so
	 * that {@link PageProvider} knows how to get a page instance.
	 * 
	 * @param pageSource
	 */
	public void setPageSource(IPageSource pageSource)
	{
		this.pageSource = pageSource;
	}

	/**
	 * 
	 * @param pageClass
	 */
	private void setPageClass(Class<? extends IRequestablePage> pageClass)
	{
		Args.notNull(pageClass, "pageClass");

		this.pageClass = pageClass;
	}

	/**
	 * 
	 * @param pageParameters
	 */
	private void setPageParameters(PageParameters pageParameters)
	{
		this.pageParameters = pageParameters;
	}

	/**
	 * 
	 */
	private void touchPageInstance()
	{
		// If there is application accessible from current thread touch
		// the page instance
		if (Application.exists())
		{
			Application.get().getPageManager().touchPage(pageInstance);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3608.java