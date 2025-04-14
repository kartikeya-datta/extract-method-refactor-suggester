error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16292.java
text:
```scala
public S@@tring getPath()

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
package wicket.protocol.http;

import javax.servlet.http.HttpServletRequest;

import wicket.Request;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Subclass of Request for HTTP protocol requests which holds an underlying
 * HttpServletRequest object. A NULL request object implementing all methods as
 * NOPs is available via WebRequest#NULL. A variety of convenience methods are
 * available that operate on the HttpServletRequest object. These methods do
 * things such as providing access to parameters, cookies, URLs and path
 * information.
 * 
 * @author Jonathan Locke
 */
public class WebRequest extends Request
{
	/** Null WebRequest object that does nothing */
	public static final WebRequest NULL = new NullWebRequest();

	/** Servlet request information. */
	private final HttpServletRequest httpServletRequest;

	/**
	 * Protected constructor.
	 * 
	 * @param httpServletRequest
	 *            The servlet request information
	 */
	protected WebRequest(final HttpServletRequest httpServletRequest)
	{
		this.httpServletRequest = httpServletRequest;
	}

	/**
	 * Gets the servlet context path.
	 * 
	 * @return Servlet context path
	 */
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
	public String getParameter(final String key)
	{
		return httpServletRequest.getParameter(key);
	}

	/**
	 * Gets the request parameters.
	 * 
	 * @return Map of parameters
	 */
	public Map getParameterMap()
	{
		final Map map = new HashMap();

		for (final Enumeration enumeration = httpServletRequest.getParameterNames(); enumeration
				.hasMoreElements();)
		{
			final String name = (String)enumeration.nextElement();
			map.put(name, httpServletRequest.getParameter(name));
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
	public String[] getParameters(final String key)
	{
		return httpServletRequest.getParameterValues(key);
	}

	/**
	 * Gets the path info if any.
	 * 
	 * @return Any servlet path info
	 */
	public String getPathInfo()
	{
		return httpServletRequest.getPathInfo();
	}

	/**
	 * Gets the servlet path.
	 * 
	 * @return Servlet path
	 */
	public String getServletPath()
	{
		return httpServletRequest.getServletPath();
	}

	/**
	 * Gets the wrapped http servlet request object.
	 * 
	 * @return the wrapped http serlvet request object.
	 */
	public final HttpServletRequest getHttpServletRequest()
	{
		return httpServletRequest;
	}

	/**
	 * Gets the request url.
	 * 
	 * @return Request URL
	 */
	public String getURL()
	{
		/*
		 * Servlet 2.3 specification : Context Path: The path prefix associated
		 * with the ServletContext that this servlet is a part of. If this
		 * context is the default context rooted at the base of the web server's
		 * URL namespace, this path will be an empty string. Otherwise, this
		 * path starts with a "/" character but does not end with a "/"
		 * character.
		 */
		return httpServletRequest.getContextPath() + '/' + getRelativeURL();
	}

	/**
	 * Gets the relative url (url without the context path and without a leading
	 * '/'). Use this method to load resources using the servlet context.
	 * 
	 * @return Request URL
	 */
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
		return url;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16292.java