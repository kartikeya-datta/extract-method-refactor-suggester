error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8736.java
text:
```scala
t@@hrow new RestartResponseAtInterceptPageException(new BrowserInfoPage(getRequest().getRelativePathPrefixToContextRoot() + getRequest().getURL()));

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
package org.apache.wicket.protocol.http;

import org.apache.wicket.AbortException;
import org.apache.wicket.IRedirectListener;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.pages.BrowserInfoPage;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.request.ClientInfo;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author Eelco Hillenius
 */
public class WebRequestCycle extends RequestCycle
{
	/** Logging object */
	private static final Logger log = LoggerFactory.getLogger(WebRequestCycle.class);

	private static final MetaDataKey BROWSER_WAS_POLLED_KEY = new MetaDataKey(Boolean.class)
	{
		private static final long serialVersionUID = 1L;
	};

	/**
	 * Constructor which simply passes arguments to superclass for storage
	 * there. This instance will be set as the current one for this thread.
	 * 
	 * @param application
	 *            The applicaiton
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 */
	public WebRequestCycle(final WebApplication application, final WebRequest request,
			final Response response)
	{
		super(application, request, response);
	}

	/**
	 * By default returns the WebApplication's default request cycle processor.
	 * Typically, you don't override this method but instead override
	 * {@link WebApplication#getRequestCycleProcessor()}.
	 * <p>
	 * <strong>if you decide to override this method to provide a custom
	 * processor per request cycle, any mounts done via WebApplication will not
	 * work and and {@link #onRuntimeException(Page, RuntimeException)} is not
	 * called unless you deliberately put effort in it to make it work.</strong>
	 * </p>
	 * 
	 * @see org.apache.wicket.RequestCycle#getProcessor()
	 */
	public IRequestCycleProcessor getProcessor()
	{
		return ((WebApplication)getApplication()).getRequestCycleProcessor();
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
		return (WebSession)getSession();
	}

	/**
	 * Redirects browser to the given page. NOTE: Usually, you should never call
	 * this method directly, but work with setResponsePage instead. This method
	 * is part of Wicket's internal behavior and should only be used when you
	 * want to circumvent the normal framework behavior and issue the redirect
	 * directly.
	 * 
	 * @param page
	 *            The page to redirect to
	 */
	public final void redirectTo(final Page page)
	{
		String redirectUrl = null;

		// Check if use serverside response for client side redirects
		IRequestCycleSettings settings = application.getRequestCycleSettings();
		if ((settings.getRenderStrategy() == IRequestCycleSettings.REDIRECT_TO_BUFFER)
				&& (application instanceof WebApplication))
		{
			// remember the current response
			final WebResponse currentResponse = getWebResponse();
			try
			{
				// create the redirect response.
				final BufferedHttpServletResponse servletResponse = new BufferedHttpServletResponse(
						currentResponse.getHttpServletResponse());
				final WebResponse redirectResponse = new WebResponse(servletResponse)
				{
					public CharSequence encodeURL(CharSequence url)
					{
						return currentResponse.encodeURL(url);
					}
				};
				redirectResponse.setCharacterEncoding(currentResponse.getCharacterEncoding());

				// redirect the response to the buffer
				setResponse(redirectResponse);

				// render the page into the buffer
				page.renderPage();

				// re-assign the original response
				setResponse(currentResponse);

				final String responseRedirect = servletResponse.getRedirectUrl();
				if (responseRedirect != null)
				{
					// if the redirectResponse has another redirect url set
					// then the rendering of this page caused a redirect to
					// something else.
					// set this redirect then.
					redirectUrl = responseRedirect;
				}
				else if (servletResponse.getContentLength() > 0)
				{
					// call filter() so that any filters can process the
					// response
					servletResponse.filter(currentResponse);

					// Set the final character encoding before calling close
					servletResponse.setCharacterEncoding(currentResponse.getCharacterEncoding());
					// close it so that the reponse is fixed and encoded from
					// here on.
					servletResponse.close();

					redirectUrl = page.urlFor(IRedirectListener.INTERFACE).toString();
					int index = redirectUrl.indexOf("?");
					String sessionId = getWebRequest().getHttpServletRequest().getSession(true)
							.getId();
					((WebApplication)application).addBufferedResponse(sessionId, redirectUrl
							.substring(index + 1), servletResponse);
				}
			}
			catch (RuntimeException ex)
			{
				// re-assign the original response
				setResponse(currentResponse);
				if (ex instanceof AbortException)
					throw ex;

				if (!(ex instanceof PageExpiredException))
					logRuntimeException(ex);

				IRequestCycleProcessor processor = getProcessor();
				processor.respond(ex, this);
				return;
			}
		}
		else
		{
			redirectUrl = page.urlFor(IRedirectListener.INTERFACE).toString();

			// Redirect page can touch its models already (via for example the
			// constructors)
			// this can be removed i guess because this page will be detached in
			// the page target
			// page.internalDetach();
		}

		if (redirectUrl == null)
		{
			redirectUrl = page.urlFor(IRedirectListener.INTERFACE).toString();
		}

		// Always touch the page again so that a redirect listener makes a page
		// stateful and adds it to the pagemap
		getSession().touch(page);

		// Redirect to the url for the page
		response.redirect(redirectUrl);
	}

	/**
	 * @see org.apache.wicket.RequestCycle#newClientInfo()
	 */
	protected ClientInfo newClientInfo()
	{
		if (getApplication().getRequestCycleSettings().getGatherExtendedBrowserInfo())
		{
			Session session = getSession();
			if (session.getMetaData(BROWSER_WAS_POLLED_KEY) == null)
			{
				// we haven't done the redirect yet; record that we will be
				// doing that now and redirect
				session.setMetaData(BROWSER_WAS_POLLED_KEY, Boolean.TRUE);
				throw new RestartResponseAtInterceptPageException(new BrowserInfoPage(getRequest().getURL()));
			}
			// if we get here, the redirect already has been done; clear
			// the meta data entry; we don't need it any longer is the client
			// info object will be cached too
			session.setMetaData(BROWSER_WAS_POLLED_KEY, (Boolean)null);
		}
		return new WebClientInfo(this);
	}

	/**
	 * If it's an ajax request we always redirects.
	 * 
	 * @see org.apache.wicket.RequestCycle#getRedirect()
	 */
	public final boolean getRedirect()
	{
		if (getWebRequest().isAjax())
		{
			return true;
		}
		else
		{
			return super.getRedirect();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8736.java