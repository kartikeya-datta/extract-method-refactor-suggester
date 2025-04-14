error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2469.java
text:
```scala
p@@ageClass = session.getClassResolver().resolveClass(bookmarkablePageClass);

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
package wicket.protocol.http.portlet;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.Application;
import wicket.IRequestTarget;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.markup.MarkupException;
import wicket.protocol.http.request.WebErrorCodeResponseTarget;
import wicket.request.RequestParameters;
import wicket.request.compound.AbstractRequestTargetResolverStrategy;
import wicket.request.target.component.BookmarkablePageRequestTarget;
import wicket.request.target.component.PageRequestTarget;

/**
 * Portlet request target resolver strategy. 
 * 
 *   
 * @author Janne Hietam&auml;ki
 */

public class PortletRequestTargetResolverStrategy extends  AbstractRequestTargetResolverStrategy
{

	/** log. */
	private static final Logger log = LoggerFactory.getLogger(PortletRequestTargetResolverStrategy.class);

	/**
	 * @see wicket.request.compound.IRequestTargetResolverStrategy#resolve(wicket.RequestCycle,
	 *      wicket.request.RequestParameters)
	 */
	public final IRequestTarget resolve(final RequestCycle requestCycle,
			final RequestParameters requestParameters)
	{

		if (requestParameters.getBookmarkablePageClass() != null)
		{
			return resolveBookmarkablePage(requestCycle, requestParameters);
		}	

		final String componentPath = requestParameters.getComponentPath();
		if(componentPath!=null)
		{
			final Session session = requestCycle.getSession();
			final Page page = session.getPage(requestParameters.getPageMapName(), componentPath,
					requestParameters.getVersionNumber());

			// Does page exist?
			if (page != null)
			{
				// Set page on request
				requestCycle.getRequest().setPage(page);

				// see whether this resolves to a component call or just the page
				final String interfaceName = requestParameters.getInterfaceName();
				if (interfaceName != null)
				{
					return resolveListenerInterfaceTarget(requestCycle, page, componentPath,
							interfaceName, requestParameters);
				}
				else
				{
					return new PageRequestTarget(page);
				}
			}
		}
		if (requestParameters.getPath() == null && requestParameters.getComponentPath() == null)
		{
			return resolveHomePageTarget(requestCycle, requestParameters);
		}
		throw new WicketRuntimeException("Unable to resolve request target " + requestParameters);
	}



	/**
	 * Resolves to a bookmarkable page target.
	 * 
	 * @param requestCycle
	 *            the current request cycle
	 * @param requestParameters
	 *            the request parameters object
	 * @return the bookmarkable page as a request target
	 */
	@Override
	protected IRequestTarget resolveBookmarkablePage(final RequestCycle requestCycle,
			final RequestParameters requestParameters)
	{
		String bookmarkablePageClass = requestParameters.getBookmarkablePageClass();
		Session session = requestCycle.getSession();
		Application application = session.getApplication();
		Class<? extends Page> pageClass;
		try
		{
			pageClass = (Class<? extends Page>)session.getClassResolver().resolveClass(bookmarkablePageClass);
		}
		catch (RuntimeException e)
		{
			return new WebErrorCodeResponseTarget(HttpServletResponse.SC_NOT_FOUND,
			"Unable to load Bookmarkable Page");
		}

		try
		{
			PageParameters params = new PageParameters(requestParameters.getParameters());
			return new BookmarkablePageRequestTarget(requestParameters.getPageMapName(), pageClass,
					params);
		}
		catch (RuntimeException e)
		{
			throw new WicketRuntimeException("Unable to instantiate Page class: "
					+ bookmarkablePageClass + ". See below for details.", e);
		}
	}

	/**
	 * Resolves to a home page target.
	 * 
	 * @param requestCycle
	 *            the current request cycle.
	 * @param requestParameters
	 *            the request parameters object
	 * @return the home page as a request target
	 */
	@Override
	protected IRequestTarget resolveHomePageTarget(final RequestCycle requestCycle,
			final RequestParameters requestParameters)
	{
		Session session = requestCycle.getSession();
		Application application = session.getApplication();
		try
		{
			// Get the home page class
			Class<? extends Page> homePageClass = application.getHomePage();
			PageParameters parameters = new PageParameters(requestParameters.getParameters());
			BookmarkablePageRequestTarget homepageTarget = new BookmarkablePageRequestTarget(
					homePageClass, parameters);

			return homepageTarget;
		}
		catch (MarkupException e)
		{
			// Markup exception should pass without modification. They show
			// a nice error page
			throw e;
		}
		catch (WicketRuntimeException e)
		{
			throw new WicketRuntimeException("Could not create home page", e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2469.java