error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18109.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18109.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18109.java
text:
```scala
public static final S@@tring HEADER_AJAX = "Wicket-Ajax";

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
package org.apache.wicket.request.http;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;

import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Time;

/**
 * Base class for request that provides additional web-related information.
 * 
 * @author Matej Knopp
 * @author Igor Vaynberg
 */
public abstract class WebRequest extends Request
{
	/** marker for Ajax requests */
	protected static final String PARAM_AJAX = "wicket-ajax";
	/** marker for Ajax requests */
	protected static final String HEADER_AJAX = "Wicket-Ajax";
	/** marker for Ajax-relative url */
	protected static final String PARAM_AJAX_BASE_URL = "wicket-ajax-baseurl";
	/** marker for Ajax-relative url */
	protected static final String HEADER_AJAX_BASE_URL = "Wicket-Ajax-BaseURL";

	/**
	 * @return request cookies
	 */
	public abstract List<Cookie> getCookies();

	/**
	 * @param cookieName
	 * @return cookie with specified name or <code>null</code> if the cookie does not exist
	 */
	public Cookie getCookie(final String cookieName)
	{
		for (Cookie cookie : getCookies())
		{
			if (cookie.getName().equals(cookieName))
			{
				return cookie;
			}
		}
		return null;
	}

	/**
	 * Returns all the values of the specified request header.
	 * 
	 * @param name
	 * @return unmodifiable list of header values
	 */
	public abstract List<String> getHeaders(String name);

	/**
	 * Returns the value of the specified request header as a <code>String</code>
	 * 
	 * @param name
	 * @return string value of request header
	 */
	public abstract String getHeader(String name);

	/**
	 * Returns the value of the specified request header as a <code>long</code> value that
	 * represents a <code>Date</code> object. Use this method with headers that contain dates, such
	 * as <code>If-Modified-Since</code>.
	 * 
	 * @param name
	 * @return date value of request header or <code>null</code> if not found
	 */
	public abstract Time getDateHeader(String name);

	/**
	 * Convenience method for retrieving If-Modified-Since header.
	 * 
	 * @return date representing the header or <code>null</code> if not set
	 */
	public final Time getIfModifiedSinceHeader()
	{
		return getDateHeader("If-Modified-Since");
	}


	/**
	 * Returns whether this request is an Ajax request. This implementation checks for values of
	 * {@value #PARAM_AJAX} url parameter or the {@value #HEADER_AJAX} header. Subclasses can use
	 * other approaches.
	 * 
	 * @return <code>true</code> if this request is an ajax request, <code>false</code> otherwise.
	 */
	public boolean isAjax()
	{
		return Strings.isTrue(getHeader(HEADER_AJAX)) ||
			Strings.isTrue(getRequestParameters().getParameterValue(PARAM_AJAX).toString());
	}

	/**
	 * Signals whether or not request processing should preserve the current client url - in other
	 * words, handle this request without redirecting. By default, this method returns {@code false}
	 * .
	 * 
	 * For example, this method can be used to preserve the url that caused a 404 in the browser if
	 * Wicket is also responsible for rendering the 404 page. If this method returns the default
	 * value of {@code false} then Wicket will redirect to the bookmarkable url of the error page,
	 * instead of preserving the url that caused the 404 in the browser.
	 * 
	 * @return {@code true} if current client url should be preserved
	 */
	public boolean shouldPreserveClientUrl()
	{
		return false;
	}

	/**
	 * Returns request with specified URL and same POST parameters as this request.
	 * 
	 * @param url
	 *            Url instance
	 * @return request with specified URL.
	 */
	@Override
	public WebRequest cloneWithUrl(final Url url)
	{
		return new WebRequest()
		{
			@Override
			public Url getUrl()
			{
				return url;
			}

			@Override
			public IRequestParameters getPostParameters()
			{
				return WebRequest.this.getPostParameters();
			}

			@Override
			public List<Cookie> getCookies()
			{
				return WebRequest.this.getCookies();
			}

			@Override
			public Time getDateHeader(final String name)
			{
				return WebRequest.this.getDateHeader(name);
			}

			@Override
			public Locale getLocale()
			{
				return WebRequest.this.getLocale();
			}

			@Override
			public String getHeader(final String name)
			{
				return WebRequest.this.getHeader(name);
			}

			@Override
			public List<String> getHeaders(final String name)
			{
				return WebRequest.this.getHeaders(name);
			}

			@Override
			public Charset getCharset()
			{
				return WebRequest.this.getCharset();
			}

			@Override
			public Url getClientUrl()
			{
				return WebRequest.this.getClientUrl();
			}

			@Override
			public Object getContainerRequest()
			{
				return WebRequest.this.getContainerRequest();
			}

			@Override
			public boolean shouldPreserveClientUrl()
			{
				return WebRequest.this.shouldPreserveClientUrl();
			}
		};
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18109.java