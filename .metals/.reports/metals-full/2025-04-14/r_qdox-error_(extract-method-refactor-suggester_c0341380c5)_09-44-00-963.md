error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17097.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17097.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17097.java
text:
```scala
C@@lass homePageClass = application.getRequiredPageSettings().getHomePage();

/*
 * $Id: DefaultRequestTargetResolverStrategy.java,v 1.4 2005/12/30 21:47:05
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
package wicket.request.compound;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import wicket.Application;
import wicket.Component;
import wicket.IRequestTarget;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.Session;
import wicket.WicketRuntimeException;
import wicket.protocol.http.request.WebErrorCodeResponseTarget;
import wicket.protocol.http.request.WebExternalResourceRequestTarget;
import wicket.request.IBookmarkablePageRequestTarget;
import wicket.request.IPageRequestTarget;
import wicket.request.IRequestCodingStrategy;
import wicket.request.RequestParameters;
import wicket.request.target.BookmarkablePageRequestTarget;
import wicket.request.target.ComponentResourceRequestTarget;
import wicket.request.target.ExpiredPageClassRequestTarget;
import wicket.request.target.ListenerInterfaceRequestTarget;
import wicket.request.target.PageRequestTarget;
import wicket.request.target.RedirectPageRequestTarget;
import wicket.request.target.SharedResourceRequestTarget;
import wicket.util.string.Strings;

/**
 * Default target resolver strategy. It tries to lookup any registered mount
 * with {@link wicket.request.IRequestCodingStrategy} and in case no mount was
 * found, it uses the {@link wicket.request.RequestParameters} object for
 * default resolving.
 * 
 * @author Eelco Hillenius
 */
public class DefaultRequestTargetResolverStrategy implements IRequestTargetResolverStrategy
{
	/**
	 * Construct.
	 */
	public DefaultRequestTargetResolverStrategy()
	{
	}

	/**
	 * @see wicket.request.compound.IRequestTargetResolverStrategy#resolve(wicket.RequestCycle,
	 *      RequestParameters)
	 */
	public final IRequestTarget resolve(RequestCycle requestCycle,
			RequestParameters requestParameters)
	{
		String path = requestCycle.getRequest().getPath();

		// first, see whether we can find any mount
		IRequestTarget mounted = requestCycle.getProcessor().getRequestCodingStrategy()
				.targetForPath(path);
		if (mounted != null)
		{
			// the path was mounted, so return that directly
			return mounted;
		} // else try different methods

		// See whether this request points to a rendered page
		if (requestParameters.getComponentPath() != null)
		{
			return resolveRenderedPage(requestCycle, requestParameters);
		}
		// see whether this request points to a bookmarkable page
		else if (requestParameters.getBookmarkablePageClass() != null)
		{
			return resolveBookmarkablePage(requestCycle, requestParameters);
		}
		// see whether this request points to a shared resource
		else if (requestParameters.getResourceKey() != null)
		{
			return resolveSharedResource(requestCycle, requestParameters);
		}
		// see whether this request points to the home page
		else if (Strings.isEmpty(path) || ("/".equals(path)))
		{
			return resolveHomePageTarget(requestCycle, requestParameters);
		}

		// if we get here, we have no regconized Wicket target, and thus
		// regard this as a external (non-wicket) resource request on
		// this server
		return resolveExternalResource(requestCycle);
	}

	/**
	 * Resolves to a shared resource target.
	 * 
	 * @param requestCycle
	 *            the current request cycle
	 * @param requestParameters
	 *            the request parameters object
	 * @return the shared resource as a request target
	 */
	protected IRequestTarget resolveSharedResource(final RequestCycle requestCycle,
			RequestParameters requestParameters)
	{
		String resourceKey = requestParameters.getResourceKey();
		return new SharedResourceRequestTarget(resourceKey);
	}

	/**
	 * Resolves to a page target that was previously rendered. Optionally
	 * resolves to a component call target, which is a specialization of a page
	 * target. If no corresponding page could be found, a expired page target
	 * will be returned.
	 * 
	 * @param requestCycle
	 *            the current request cycle
	 * @param requestParameters
	 *            the request parameters object
	 * @return the previously rendered page as a request target
	 */
	protected IRequestTarget resolveRenderedPage(RequestCycle requestCycle,
			RequestParameters requestParameters)
	{
		String componentPath = requestParameters.getComponentPath();
		Session session = requestCycle.getSession();
		Page page = session.getPage(requestParameters.getPageMapName(), componentPath,
				requestParameters.getVersionNumber());

		// Does page exist?
		if (page != null)
		{
			// see whether this resolves to a component call or just the page
			String interfaceName = requestParameters.getInterfaceName();
			if (interfaceName != null)
			{
				return resolveListenerInterfaceTarget(requestCycle, page, componentPath,
						interfaceName);
			}
			else
			{
				return new PageRequestTarget(page);
			}
		}
		else
		{
			// Page was expired from session, probably because backtracking
			// limit was reached
			return new ExpiredPageClassRequestTarget();
		}
	}

	/**
	 * Resolves the RequestTarget for the given interface. This method can be
	 * overriden if some special interface needs to resolve to its own target.
	 * 
	 * @param requestCycle
	 *            The current RequestCycle object
	 * @param page
	 *            The page object which holds the component for which this
	 *            interface is called on.
	 * @param componentPath
	 *            The component path for looking up the component in the page.
	 * @param interfaceName
	 *            The interface to resolve.
	 * @return The RequestTarget that was resolved
	 */
	protected IRequestTarget resolveListenerInterfaceTarget(RequestCycle requestCycle,
			final Page page, final String componentPath, final String interfaceName)
	{
		if (interfaceName.equals("IRedirectListener"))
		{
			return new RedirectPageRequestTarget(page);
		}
		else
		{
			Method listenerMethod = requestCycle.getRequestInterfaceMethod(interfaceName);
			if (listenerMethod == null)
			{
				throw new WicketRuntimeException("Attempt to access unknown interface "
						+ interfaceName);
			}
			String componentPart = Strings.afterFirstPathComponent(componentPath, ':');
			if (Strings.isEmpty(componentPart))
			{
				// we have an interface that is not redirect, but no
				// component... that must be wrong
				throw new WicketRuntimeException("when trying to call " + listenerMethod
						+ ", a component must be provided");
			}
			Component component = page.get(componentPart);
			if (!component.isVisible())
			{
				throw new WicketRuntimeException(
						"Calling listener methods on components that are not visible is not allowed");
			}
			if (interfaceName.equals("IResourceListener"))
			{
				return new ComponentResourceRequestTarget(page, component, listenerMethod);
			}
			return new ListenerInterfaceRequestTarget(page, component, listenerMethod);
		}
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
	protected IRequestTarget resolveBookmarkablePage(RequestCycle requestCycle,
			RequestParameters requestParameters)
	{
		String bookmarkablePageClass = requestParameters.getBookmarkablePageClass();
		Session session = requestCycle.getSession();
		Application application = session.getApplication();
		Class pageClass;
		try
		{
			pageClass = session.getClassResolver().resolveClass(bookmarkablePageClass);
		}
		catch (RuntimeException e)
		{
			return new WebErrorCodeResponseTarget(HttpServletResponse.SC_NOT_FOUND,
					"Unable to load Bookmarkable Page");
		}

		try
		{
			Page newPage = session.getPageFactory().newPage(pageClass,
					new PageParameters(requestParameters.getParameters()));

			// the response might have been set in the constructor of
			// the bookmarkable page
			IRequestTarget requestTarget = requestCycle.getRequestTarget();

			// is it possible that there is already another request target at
			// this point then the 2 below?
			if (!(requestTarget instanceof IPageRequestTarget || requestTarget instanceof IBookmarkablePageRequestTarget))
			{
				requestTarget = new PageRequestTarget(newPage);
			}

			return requestTarget;
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
	protected IRequestTarget resolveHomePageTarget(RequestCycle requestCycle,
			RequestParameters requestParameters)
	{
		Session session = requestCycle.getSession();
		Application application = session.getApplication();
		try
		{
			// Get the home page class
			Class homePageClass = application.getPages().getHomePage();

			// and create a dummy target for looking up whether the home page is
			// mounted
			BookmarkablePageRequestTarget pokeTarget = new BookmarkablePageRequestTarget(
					homePageClass);
			IRequestCodingStrategy requestCodingStrategy = requestCycle.getProcessor()
					.getRequestCodingStrategy();
			String path = requestCodingStrategy.pathForTarget(pokeTarget);

			if (path != null)
			{
				// The home page was mounted at the given path.
				// Issue a redirect to that path
				requestCycle.setRedirect(true);
				// our poke target is good enough
				return pokeTarget;
			}

			// else the home page was not mounted; render it now so
			// that we will keep a clean path
			PageParameters parameters = new PageParameters(requestParameters.getParameters());
			Page newPage = session.getPageFactory().newPage(homePageClass, parameters);
			
			// The response might have been set in the home page constructor
			IRequestTarget requestTarget = requestCycle.getRequestTarget();

			// Is it possible that there is already another request target at
			// this point then the 2 below?
			if (!(requestTarget instanceof IPageRequestTarget || requestTarget instanceof IBookmarkablePageRequestTarget))
			{
				requestTarget = new PageRequestTarget(newPage);
			}

			return requestTarget;
		}
		catch (WicketRuntimeException e)
		{
			throw new WicketRuntimeException("Could not create home page", e);
		}
	}

	/**
	 * Resolves to an external resource.
	 * 
	 * @param requestCycle
	 *            The current request cycle
	 * @return The external resource request target
	 */
	protected IRequestTarget resolveExternalResource(RequestCycle requestCycle)
	{
		// Get the relative URL we need for loading the resource from
		// the servlet context
		// NOTE: we NEED to put the '/' in front as otherwise some versions
		// of application servers (e.g. Jetty 5.1.x) will fail for requests
		// like '/mysubdir/myfile.css'
		final String url = '/' + requestCycle.getRequest().getRelativeURL();
		return new WebExternalResourceRequestTarget(url);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17097.java