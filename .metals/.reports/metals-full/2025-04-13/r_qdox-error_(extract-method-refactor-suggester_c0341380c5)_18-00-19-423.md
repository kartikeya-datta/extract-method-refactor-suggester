error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9182.java
text:
```scala
S@@tring url = getServletPath();

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
package wicket.protocol.http.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.Application;
import wicket.IRedirectListener;
import wicket.RequestListenerInterface;
import wicket.WicketRuntimeException;
import wicket.protocol.http.WebApplication;
import wicket.protocol.http.WebRequest;
import wicket.util.lang.Bytes;
import wicket.util.string.StringValueConversionException;
import wicket.util.string.Strings;
import wicket.util.upload.FileUploadException;

/**
 * A Servlet specific WebRequest implementation wrapping a HttpServletRequest
 * 
 * @author Ate Douma
 */
public class ServletWebRequest extends WebRequest
{
	/** Servlet request information. */
	private final HttpServletRequest httpServletRequest;

	/** Log */
	private static final Logger log = LoggerFactory.getLogger(ServletWebRequest.class);

	/**
	 * Protected constructor.
	 * 
	 * @param httpServletRequest
	 *            The servlet request information
	 */
	public ServletWebRequest(final HttpServletRequest httpServletRequest)
	{
		this.httpServletRequest = httpServletRequest;
	}

	/**
	 * Gets the servlet context path.
	 * 
	 * @return Servlet context path
	 */
	@Override
	public String getContextPath()
	{
		return httpServletRequest.getContextPath();
	}

	/**
	 * Returns the preferred <code>Locale</code> that the client will accept
	 * content in, based on the Accept-Language header. If the client request
	 * doesn't provide an Accept-Language header, this method returns the
	 * default locale for the server.
	 * 
	 * @return the preferred <code>Locale</code> for the client
	 */
	@Override
	public Locale getLocale()
	{
		return httpServletRequest.getLocale();
	}

	/**
	 * Gets the request parameter with the given key.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter value
	 */
	@Override
	public String getParameter(final String key)
	{
		return httpServletRequest.getParameter(key);
	}

	/**
	 * Gets the request parameters.
	 * 
	 * @return Map of parameters
	 */
	@Override
	public Map<String, Object> getParameterMap()
	{
		final Map<String, Object> map = new HashMap<String, Object>();

		for (final Enumeration enumeration = httpServletRequest.getParameterNames(); enumeration
				.hasMoreElements();)
		{
			final String name = (String)enumeration.nextElement();
			String[] parameterValues = httpServletRequest.getParameterValues(name);
			if (parameterValues.length == 1)
			{
				map.put(name, parameterValues[0]);
			}
			else
			{
				map.put(name, parameterValues);
			}
		}

		return map;
	}

	/**
	 * Gets the request parameters with the given key.
	 * 
	 * @param key
	 *            Parameter name
	 * @return Parameter values
	 */
	@Override
	public String[] getParameters(final String key)
	{
		return httpServletRequest.getParameterValues(key);
	}

	/**
	 * Gets the path info if any.
	 * 
	 * @return Any servlet path info
	 */
	@Override
	public String getPath()
	{
		String url = Strings.stripJSessionId(httpServletRequest.getRequestURI());
		String rootPath = ((WebApplication)Application.get()).getRootPath();
		if (url.startsWith(rootPath))
		{
			// We return everything after rootPath, making sure we start with a slash.
			return url.substring(rootPath.endsWith("/") ? rootPath.length() - 1 : rootPath.length());
		}
		return null;
	}

	/**
	 * Gets the servlet path.
	 * 
	 * @return Servlet path
	 */
	@Override
	public String getServletPath()
	{
		return httpServletRequest.getServletPath();
	}

	/**
	 * Gets the wrapped http servlet request object.
	 * 
	 * @return the wrapped http serlvet request object.
	 */
	@Override
	public final HttpServletRequest getHttpServletRequest()
	{
		return httpServletRequest;
	}

	/**
	 * Gets the relative url (url without the context path and without a leading
	 * '/'). Use this method to load resources using the servlet context.
	 * 
	 * @return Request URL
	 */
	@Override
	public String getRelativeURL()
	{
		/*
		 * Servlet 2.3 specification :
		 * 
		 * Servlet Path: The path section that directly corresponds to the
		 * mapping which activated this request. This path starts with a "/"
		 * character except in the case where the request is matched with the
		 * "/*" pattern, in which case it is the empty string.
		 * 
		 * PathInfo: The part of the request path that is not part of the
		 * Context Path or the Servlet Path. It is either null if there is no
		 * extra path, or is a string with a leading "/".
		 */
		String url = httpServletRequest.getServletPath();
		final String pathInfo = httpServletRequest.getPathInfo();

		if (pathInfo != null)
		{
			url += pathInfo;
		}

		final String queryString = httpServletRequest.getQueryString();

		if (queryString != null)
		{
			url += ("?" + queryString);
		}

		// If url is non-empty it has to start with '/', which we should lose
		if (!url.equals(""))
		{
			// Remove leading '/'
			url = url.substring(1);
		}

		// strip any jsession id
		url = Strings.stripJSessionId(url);

		return url;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "[method = " + httpServletRequest.getMethod() + ", protocol = "
				+ httpServletRequest.getProtocol() + ", requestURL = "
				+ httpServletRequest.getRequestURL() + ", contentType = "
				+ httpServletRequest.getContentType() + ", contentLength = "
				+ httpServletRequest.getContentLength() + ", contextPath = "
				+ httpServletRequest.getContextPath() + ", pathInfo = "
				+ httpServletRequest.getPathInfo() + ", requestURI = "
				+ httpServletRequest.getRequestURI() + ", servletPath = "
				+ httpServletRequest.getServletPath() + ", pathTranslated = "
				+ httpServletRequest.getPathTranslated() + "]";
	}

	/**
	 * @see wicket.protocol.http.WebRequest#newMultipartWebRequest(wicket.util.lang.Bytes)
	 */
	@Override
	public WebRequest newMultipartWebRequest(Bytes maxsize)
	{
		try
		{
			return new MultipartServletWebRequest(httpServletRequest, maxsize);
		}
		catch (FileUploadException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	/**
	 * This will return true if the header "Wicket-Ajax" is set.
	 * 
	 * @see wicket.protocol.http.WebRequest#isAjax()
	 */
	// TODO matej? should we have a simple way of supporting other ajax things?
	// or should they just set that same header??
	@Override
	public boolean isAjax()
	{
		boolean ajax = false;

		String ajaxHeader = httpServletRequest.getHeader("Wicket-Ajax");
		if (Strings.isEmpty(ajaxHeader) == false)
		{
			try
			{
				ajax = Strings.isTrue(ajaxHeader);
			}
			catch (StringValueConversionException e)
			{
				// We are not interested in this exception but we log it anyway
				log.debug("Couldn't convert the Wicket-Ajax header: " + ajaxHeader);
			}
		}

		return ajax;
	}
	
	/**
	 * This method by default calls isAjax(), wicket ajax request do have
	 * an header set. And for all the ajax request the versioning should be merged
	 * with the previous one. And when it sees that the current request is a 
	 * redirect to page request the version will also be merged with the previous one
	 * because refresh in the browser or redirects to a page shouldn't generate a new
	 * version. 
	 * 
	 * @see wicket.Request#mergeVersion()
	 */
	public boolean mergeVersion()
	{
		RequestListenerInterface intface = getRequestParameters().getInterface();
		return isAjax() || intface == IRedirectListener.INTERFACE;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9182.java