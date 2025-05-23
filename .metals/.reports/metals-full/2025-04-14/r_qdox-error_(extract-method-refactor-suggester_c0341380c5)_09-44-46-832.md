error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8772.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8772.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8772.java
text:
```scala
i@@f (Strings.isEmpty(path) || ("/".equals(path)))

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.protocol.http;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.ApplicationPages;
import wicket.ApplicationSettings;
import wicket.Component;
import wicket.IRedirectListener;
import wicket.Page;
import wicket.PageParameters;
import wicket.RequestCycle;
import wicket.Resource;
import wicket.Response;
import wicket.SharedResources;
import wicket.WicketRuntimeException;
import wicket.protocol.http.servlet.ServletWebRequest;
import wicket.response.BufferedResponse;
import wicket.util.io.Streams;
import wicket.util.string.Strings;

/**
 * RequestCycle implementation for HTTP protocol. Holds the application,
 * session, request and response objects for a given HTTP request. Contains
 * methods (urlFor*) which yield a URL for bookmarkable pages as well as
 * non-bookmarkable component interfaces. The protected handleRender method is
 * the internal entrypoint which takes care of the details of rendering a
 * response to an HTTP request.
 *
 * @see RequestCycle
 * @author Jonathan Locke
 * @author Johan Compagner
 * @author Gili Tzabari
 */
public class WebRequestCycle extends RequestCycle
{
	/** Path prefix for shared resources */
	public static final String resourceReferencePrefix = "/resources/";

	/** Logging object */
	private static final Log log = LogFactory.getLog(WebRequestCycle.class);

	/** Return value for doParseRequest: URL did not match and was not handled */
	public static final int PARSE_REQUEST_NO_MATCH = 0;

	/** Return value for doParseRequest: The URL points to a page and the response page was set */
	public static final int PARSE_REQUEST_RETURNING_PAGE = 1;
	
	/** Return value for doParseRequest: The URL points to some static content */
	public static final int PARSE_REQUEST_RETURNING_STATIC_CONTENT = 2;

	/**
	 * Constructor which simply passes arguments to superclass for storage
	 * there.
	 *
	 * @param session
	 *            The session
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 */
	public WebRequestCycle(final WebSession session, final WebRequest request,
			final Response response)
	{
		super(session, request, response);
	}

	/**
	 * @return Request as a WebRequest
	 */
	public WebRequest getWebRequest()
	{
		return (WebRequest)request;
	}

	/**
	 * @return Response as a WebResponse
	 */
	public WebResponse getWebResponse()
	{
		return (WebResponse)response;
	}

	/**
	 * @return Session as a WebSession
	 */
	public WebSession getWebSession()
	{
		return (WebSession)session;
	}

	/**
	 * Parses a request. The following four steps are followed:
	 * <p>
	 * 1. If the URL requested is in the form of a component listener
	 * invocation, then that invocation will occur and is expected to generate a
	 * response.
	 * <p>
	 * 2. If the URL is to a bookmarkable page, then an instance of that page is
	 * created and is expected render a response.
	 * <p>
	 * 3. If the URL is for the application's home page, an instance of the home
	 * page will be created and is expected to render a response.
	 * <p>
	 * 4. Finally, an attempt is made to render the requested resource as static
	 * content, available through the servlet context.
	 * <p>
	 * If all four steps are executed and content cannot be found to satisfy the
	 * request, then false is returned.
	 *
	 * @return True if a Page should be rendered back to the user
	 */
	protected final boolean parseRequest()
	{
		// Try different methods of parsing and dispatching the request
		if (callDispatchedComponentListener())
		{
			// if it is, we don't need to update the cluster, etc, and return false
		}
		// it wasn't a dispatched listener, try other methods
		else if (callComponentListener() || bookmarkablePage() || homePage())
		{
			// Returning a page
			return true;
		}
		// If it's not a resource reference or static content
		else if (resourceReference() || staticContent())
	    {
			// if it is, we don't need to update the cluster, etc, and return false
	    }
		else
		{
		    int rtn = doParseRequest();
		    if (rtn == PARSE_REQUEST_RETURNING_PAGE)
		    {
		        return true;
		    }
		    else if (rtn == PARSE_REQUEST_RETURNING_STATIC_CONTENT)
		    {
				// if it is, we don't need to update the cluster, etc, and return false
		    }
		    else
		    {
				// not found... send 404 to client indicating that no resource was found
				// for the request uri
				WebResponse webResponse = (WebResponse)getResponse();
				HttpServletResponse httpServletResponse = webResponse.getHttpServletResponse();
				try
				{
					httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				catch (IOException e)
				{
					// that seems unlikely... anyway, log exception and forget about it
					log.error("unable to send 404 for " + getRequest() + ", cause: " + e.getMessage(), e);
				}
			}
		}

		// Don't update the cluster, not returning a page
		setUpdateCluster(false);
		setResponsePage((Page)null);
		return false;
	}

	/**
	 * Users may implement there own strategies on how to handle specific URLs.
	 * Bare in mind that Wicket's default strategies are extended and not 
	 * replaced and thus you can not replace existing ones. Please see 
	 * WebRequestCrawlerSave and WebResponseCrawlerSave for examples on how
	 * to replace existing strategies.<p>
	 * A possible implementation of doParseRequest may automatically resolve
	 * static URLs like *.css and *.js or handle specific URLs which return
	 * images from a database without the need to register the resource with
	 * the application, or ...
	 * 
	 * @see WebRequestCrawlerSave
	 * @see WebResponseCrawlerSave
	 * 
	 * @return PARSE_REQUEST_RETURNING_PAGE - returning a page; PARSE_REQUEST_RETURNING_STATIC_CONTENT - static content (no Page); else - not found
	 */
	protected int doParseRequest()
	{
		return PARSE_REQUEST_NO_MATCH;
	}
	
	/**
	 * Redirects browser to the given page.
	 * NOTE: Usually, you should never call this method directly, but work with
	 * setResponsePage instead. This method is part of Wicket's internal
	 * behaviour and should only be used when you want to circumvent the normal
	 * framework behaviour and issue the redirect directly.
	 *
	 * @param page
	 *            The page to redirect to
	 * @throws ServletException
	 */
	protected void redirectTo(final Page page) throws ServletException
	{
		String redirectUrl = null;
		
		// Check if use serverside response for client side redirects
		ApplicationSettings settings = application.getSettings();
		if ((settings.getRenderStrategy() == ApplicationSettings.REDIRECT_TO_BUFFER)
		        && (application instanceof WebApplication))
		{
		    // remember the current response
			final Response currentResponse = getResponse();
			try
			{
				// create the redirect response.
				// override the encodeURL so that it will use the real once encoding.
				final BufferedResponse redirectResponse = new BufferedResponse()
				{
					public String encodeURL(String url)
					{
						return currentResponse.encodeURL(url);
					}
				};
				redirectResponse.setCharacterEncoding(currentResponse.getCharacterEncoding());

				// redirect the response to the buffer
				setResponse(redirectResponse);

				// test if the invoker page was the same as the page that is going to be renderd
				if (getInvokePage() == getResponsePage())
				{
					// set it to null because it is already ended in the page.doRender()
					setInvokePage(null);
				}

				// render the page into the buffer
				page.doRender();

				// re-assign the original response
				setResponse(currentResponse);

				final String responseRedirect = redirectResponse.getRedirectUrl();
				if (responseRedirect != null)
				{
					// if the redirectResponse has another redirect url set
					// then the rendering of this page caused a redirect to something else.
					// set this redirect then.
					redirectUrl = responseRedirect;
				}
				else if (redirectResponse.getContentLength() > 0)
				{
					// if no content is created then don't set it in the redirect buffer
				    // (maybe access failed).
					// Set the encoding of the response (what the browser wants)
					redirectResponse.setCharacterEncoding(currentResponse.getCharacterEncoding());

					// close it so that the reponse is fixed and encoded from here on.
					redirectResponse.close();

					redirectUrl = page.urlFor(page, IRedirectListener.class);
          // TODO adouma: no Portlets support yet so typecasted for Servlet env.
					((WebApplication)application).addRedirect(
					        ((ServletWebRequest)getWebRequest()).getHttpServletRequest(), redirectUrl, redirectResponse);
				}
			}
			catch (RuntimeException ex)
			{
				// re-assign the original response
				setResponse(currentResponse);
				internalOnRuntimeException(page, ex);
				return;
			}
		}
		else
		{
			session.touch(page);
			// redirect page can touch its models already (via for example the constructors)
			page.internalEndRequest();
		}

		if (redirectUrl == null)
		{
			redirectUrl = page.urlFor(page, IRedirectListener.class);
		}
		// Redirect to the url for the page
		response.redirect(redirectUrl);
	}

	/**
	 * Creates a prefix for a url.
	 * @return Prefix for URLs including the context path and servlet path.
	 */
	protected StringBuffer urlPrefix()
	{
		final StringBuffer buffer = new StringBuffer();
		final WebRequest request = getWebRequest();
		if (request != null)
		{
			final String contextPath = request.getContextPath();
			buffer.append(contextPath);
			String path = request.getServletPath();
			if (path == null || "".equals(path)) 
			{
				path = "/";
			}
			buffer.append(path);			
		}

		return buffer;
	}

	/**
	 * Activates a bookmarkable page if one was specified in the request.
	 *
	 * @return True if a bookmarkable page was created and returned for the
	 *         request.
	 * @throws WicketRuntimeException
	 */
	private boolean bookmarkablePage()
	{
		// Get any component parameter
		final String bookmarkableName = request.getParameter("bookmarkablePage");
		if (bookmarkableName != null)
		{
			// first see whether we have a logical mapping
			Class pageClass = application.getPages().classForAlias(bookmarkableName);

			// nope, we don't have a logical mapping, so this should be a full class name
			if (pageClass == null)
			{
			    try
			    {
					pageClass = session.getClassResolver().resolveClass(bookmarkableName);
			    }
			    catch (RuntimeException e)
			    {
					try
					{
						getWebResponse().getHttpServletResponse().sendError(
						        HttpServletResponse.SC_NOT_FOUND,
						        "Unable to load Bookmarkable Page");

						return true;
					}
					catch (IOException ex)
					{
						// that seems unlikely... anyway, log exception and forget about it
						log.error("unable to send 404 for " + getRequest() + ", cause: " + ex.getMessage(), ex);
						return false;
					}
			    }
			}

		    try
		    {
				Page newPage = session.getPageFactory().newPage(pageClass,
						new PageParameters(getRequest().getParameterMap()));

				// If response is set in the construtor of the bookmarkable page.
				if (getResponsePage() == null)
				{
					setResponsePage(newPage);
				}
				setUpdateCluster(true);
				return true;
		    }
		    catch (RuntimeException e)
		    {
		        throw new WicketRuntimeException("Unable to instantiate Page class: "
		                + bookmarkableName + ". See below for details.", e);
		    }
		}
		return false;
	}

	/**
	 * Calls a dispatched component listener interface on a page that already exists in the
	 * session. This is the same as callComponentListener, except that the actual handler
	 * is not the component itself, but an attached 'even request listener'. Such listeners
	 * (typically used for AJAX behaviour) are responsible for their own output (which
	 * could be XML, javascript, HTML or whatever), and thus the current page should not
	 * be rendered when this method returns true.
	 *
	 * @return True if the dispatched listener was successfully called
	 * @throws WicketRuntimeException
	 */
	private boolean callDispatchedComponentListener()
	{
		if (request.getParameter("dispatched") != null)
		{
			if (!callComponentListener())
			{
				throw new WicketRuntimeException("incomplete dispatched request");
			}

			return true;
		}

		return false;
	}

	/**
	 * Calls a component listener interface on a page that already exists in the
	 * session. The session component is found using the path in the 'component'
	 * parameter of the request. The interface method to be called is determined
	 * by the 'interface' parameter of the request. The interface can only be
	 * one of the interfaces listed in the secureInterfaceMethods map in the
	 * RequestDispatcher implementation.
	 *
	 * @return True if the component listener was successfully called
	 * @throws WicketRuntimeException
	 */
	private boolean callComponentListener()
	{
		// Get any component parameter
		final String path = request.getParameter("path");
		final String pageMapName = request.getParameter("pagemap");
		if (path != null)
		{
			// Get version number
			final String versionNumberString = request.getParameter("version");
			final int versionNumber = Strings.isEmpty(versionNumberString) ? 0 : Integer
					.parseInt(versionNumberString);

			// Get page from path
			final Page page = session.getPage(pageMapName, path, versionNumber);

			// Does page exist?
			if (page != null)
			{
				// Assume cluster needs to be updated now, unless listener invocation
				// change this (for example, with a simple page redirect)
				setUpdateCluster(true);

				// Execute the user's code
				String interfaceName = request.getParameter("interface");
				if (interfaceName == null)
				{
					interfaceName = "IRedirectListener";
				}
				invokeInterface(page, path, interfaceName);
				return true;
			}
			else
			{
				onExpiredPage();
				return true;
			}
		}
		return false;
	}

	/**
	 * If no context path was provided, activates the home page.
	 *
	 * @return True if the home page was activated
	 * @throws WicketRuntimeException
	 */
	private boolean homePage()
	{
		final String path = getWebRequest().getPath();
		final String servletPath = getWebRequest().getServletPath();
		if (Strings.isEmpty(path) || ("/".equals(path) && "".equals(servletPath)))
		{
			try
			{
				Class homePage = application.getPages().getHomePage();
				ApplicationPages.HomePageRenderStrategy homePageStrategy = application.getPages().getHomePageRenderStrategy();
				if (homePageStrategy == ApplicationPages.BOOKMARK_REDIRECT)
				{
					setResponsePage(homePage);
				}
				else
				{
					Page newPage = newPage(homePage);

					// check if the home page didn't set a page by itself
					if (getResponsePage() == null)
					{
						if (homePageStrategy == ApplicationPages.PAGE_REDIRECT)
						{
							//see if we have to redirect the render part by default
							//so that a homepage has the same url as a post or get to that page.
							ApplicationSettings.RenderStrategy strategy = getSession().getApplication()
									.getSettings().getRenderStrategy();
							boolean issueRedirect = (strategy == ApplicationSettings.REDIRECT_TO_RENDER
 strategy == ApplicationSettings.REDIRECT_TO_BUFFER);
							setRedirect(issueRedirect);
						}
						setResponsePage(newPage);
					}
				}
				setUpdateCluster(true);
			}
			catch (WicketRuntimeException e)
			{
				throw new WicketRuntimeException("Could not create home page", e);
			}
			return true;
		}
		return false;
	}


	/**
	 * Invokes a given interface on a component.
	 *
	 * @param component
	 *            The component
	 * @param method
	 *            The name of the method to call
	 */
	private void invokeInterface(final Component component, final Method method)
	{
		try
		{
			// Invoke the interface method on the component
			method.invoke(component, new Object[] { });
		}
		catch (IllegalAccessException e)
		{
			throw new WicketRuntimeException("Cannot access method " + method + " of interface "
					+ method.getClass().getName(), e);
		}
		catch (InvocationTargetException e)
		{
			throw new WicketRuntimeException("Method " + method + " of interface " + method.getClass().getName()
					+ " threw an exception", e);
		}
	}

	/**
	 * Invokes a given interface on a component on a given page
	 *
	 * @param page
	 *            The page where the component is
	 * @param path
	 *            The path to the component
	 * @param interfaceName
	 *            The name of the interface to call
	 */
	private void invokeInterface(final Page page, final String path, final String interfaceName)
	{
		setInvokePage(page);
		// Invoke interface on the component at the given path on the page
		final Component component = page.get(Strings.afterFirstPathComponent(path, ':'));
		if (component != null)
		{
			if (!component.isVisible())
			{
				try
				{
					getWebResponse().getHttpServletResponse().sendError(HttpServletResponse.SC_FORBIDDEN,
					        "Unable to execute this request");
				}
				catch (IOException ex)
				{
					// that seems unlikely... anyway, log exception and forget about it
					log.error("unable to send 403 for " + getRequest() + ", cause: " + ex.getMessage(), ex);
				}
				return;
			}
			Method method = getRequestInterfaceMethod(interfaceName);
			if (method != null)
			{
				// Set the page for the component as the response page
				setResponsePage(page);
				if (!interfaceName.equals("IRedirectListener"))
				{
					// Clear all feedback messages if it isn't a redirect
					page.getFeedbackMessages().clear();

					// and see if we have to redirect the render part by default
					ApplicationSettings.RenderStrategy strategy = getSession().getApplication()
							.getSettings().getRenderStrategy();
					boolean issueRedirect = (strategy == ApplicationSettings.REDIRECT_TO_RENDER
 strategy == ApplicationSettings.REDIRECT_TO_BUFFER);

					setRedirect(issueRedirect);
				}

				// Invoke interface on component
				invokeInterface(component, method);

//				// Set form component values from cookies
//				setFormComponentValuesFromCookies(page);
			}
			else
			{
				throw new WicketRuntimeException("Attempt to access unknown interface " + interfaceName);
			}
		}
		else
		{
			// Must be an internal error of some kind or someone is hacking
			// around with URLs in their browser.
			log.error("No component found for " + path);
			setResponsePage(newPage(application.getPages().getInternalErrorPage()));
		}
	}


	/**
	 * Creates a new page.
	 *
	 * @param pageClass
	 *            The page class to instantiate
	 * @return The page
	 * @throws WicketRuntimeException
	 */
	private final Page newPage(final Class pageClass)
	{
		final PageParameters parameters = new PageParameters(getRequest().getParameterMap());
		return session.getPageFactory().newPage(pageClass, parameters);
	}

	/**
	 * Called when the requested page is not available.
	 */
	private void onExpiredPage()
	{
		// Page was expired from session, probably because backtracking
		// limit was reached
		setResponsePage(newPage(application.getPages().getPageExpiredErrorPage()));
	}

	/**
	 * Renders resource to user if URL matches resource pattern
	 *
	 * @return True if the resource was found
	 */
	private boolean resourceReference()
	{
		final String path = request.getPath();
		if (path.startsWith(resourceReferencePrefix))
		{
			final String rawResourceKey = path.substring(resourceReferencePrefix.length());

			SharedResources sharedResources = getApplication().getSharedResources();
			Resource resource = sharedResources.get(rawResourceKey);
			if (resource == null)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Could not find resource referenced by key " + rawResourceKey);
				}
				try
				{
					getWebResponse().getHttpServletResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				catch (IOException ex)
				{
					log.error("error sending 404", ex);
					throw new WicketRuntimeException("Could not find resource referenced by key " + rawResourceKey +
							" and send a 404", ex);
				}
				// do return true, the response is handled.
				return true;
			}
			sharedResources.onResourceRequested(rawResourceKey);
			resource.onResourceRequested();
			return true;
		}
		return false;
	}

	/**
	 * @return True if static content was returned
	 */
	private boolean staticContent()
	{
		try
		{
			// Get the relative URL we need for loading the resource from
			// the servlet context
			final String url = '/' + getWebRequest().getRelativeURL();
			// NOTE: we NEED to put the '/' in front as otherwise some versions of
			// application servers (e.g. Jetty 5.1.x) will fail for requests like
			// '/mysubdir/myfile.css'

			// Get servlet context
			final ServletContext context = ((WebApplication)application).getWicketServlet()
					.getServletContext();

			// Set content type
			response.setContentType(context.getMimeType(url));

			// NOTE: Servlet container prevents accessing WEB-INF/** already.
			final InputStream in = context.getResourceAsStream(url);
			if (in != null)
			{
				try
				{
					// Copy resource input stream to servlet output stream
					Streams.copy(in, getWebResponse().getHttpServletResponse()
							.getOutputStream());
				}
				finally
				{
					// NOTE: We close only the InputStream. The app server will
					// close the output stream.
					in.close();
				}
				return true;
			}
			else
			{
				// No static content was found
				return false;
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException("Cannot load static content for request " + request, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8772.java