error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17811.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17811.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17811.java
text:
```scala
private S@@tring prefixToContextPath = "";

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
package org.apache.wicket.mock;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.Url.QueryParameter;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.UrlUtils;
import org.apache.wicket.util.time.Time;

/**
 * Mutable mock {@link WebRequest}.
 * 
 * @author Matej Knopp
 */
public class MockWebRequest extends WebRequest
{
	private Url url;
	private List<Cookie> cookies = new ArrayList<Cookie>();
	private Map<String, List<Object>> headers = new HashMap<String, List<Object>>();
	private MockRequestParameters postRequestParameters = new MockRequestParameters();
	private Locale locale = Locale.getDefault();
	private String contextPath;
	private String filterPath;
	private String prefixToContextPath;

	/**
	 * Construct.
	 * 
	 * @param url
	 */
	public MockWebRequest(Url url)
	{
		this.url = url;
	}

	/**
	 * Construct.
	 * 
	 * @param url
	 * @param contextPath
	 * @param filterPath
	 * @param prefixToContextPath
	 */
	public MockWebRequest(Url url, String contextPath, String filterPath, String prefixToContextPath)
	{
		this.url = url;
		this.contextPath = contextPath;
		this.filterPath = filterPath;
		this.prefixToContextPath = prefixToContextPath;
	}

	MockWebRequest(Url url, List<Cookie> cookies, Map<String, List<Object>> headers,
		MockRequestParameters postRequestParameters, Locale locale)
	{
		this.url = url;
		this.cookies.addAll(cookies);
		this.headers = headers;
		this.postRequestParameters = postRequestParameters;
		this.locale = locale;
	}

	@Override
	public MockWebRequest cloneWithUrl(Url url)
	{
		return new MockWebRequest(url, cookies, headers, postRequestParameters, locale);
	}

	/**
	 * @param url
	 */
	public void setUrl(Url url)
	{
		this.url = url;
	}

	@Override
	public Url getUrl()
	{
		return url;
	}

	@Override
	public String toString()
	{
		return "MockWebRequest [url=" + url + "]";
	}

	/**
	 * Sets cookies for current request.
	 * 
	 * @param cookies
	 */
	public void setCookies(List<Cookie> cookies)
	{
		this.cookies.clear();
		this.cookies.addAll(cookies);
	}

	/**
	 * @param cookie
	 */
	public void addCookie(Cookie cookie)
	{
		cookies.add(cookie);
	}

	@Override
	public List<Cookie> getCookies()
	{
		return Collections.unmodifiableList(cookies);
	}


	@Override
	public Time getDateHeader(String name)
	{
		List<Object> dates = headers.get(name);
		if (dates == null || dates.isEmpty())
		{
			return null;
		}

		Object date = dates.get(0);

		if (date instanceof Time == false)
		{
			throw new WicketRuntimeException("Date header with name '" + name +
				"' is not a valid Time.");
		}
		return (Time)date;
	}

	private void addHeaderObject(String name, Object value)
	{
		List<Object> values = headers.get(name);
		if (values == null)
		{
			values = new ArrayList<Object>();
			headers.put(name, values);
		}
		values.add(value);
	}

	/**
	 * Sets date header for given name.
	 * 
	 * @param name
	 * @param value
	 */
	public void setDateHeader(String name, Time value)
	{
		removeHeader(name);
		addHeaderObject(name, value);
	}

	/**
	 * Adds date header for given name.
	 * 
	 * @param name
	 * @param value
	 */
	public void addDateHeader(String name, Time value)
	{
		addHeaderObject(name, value);
	}

	@Override
	public String getHeader(String name)
	{
		List<Object> h = headers.get(name);
		return (h == null || h.isEmpty()) ? null : h.get(0).toString();
	}

	/**
	 * Sets header for given name.
	 * 
	 * @param name
	 * @param value
	 */
	public void setHeader(String name, String value)
	{
		removeHeader(name);
		addHeader(name, value);
	}

	/**
	 * Adds header for given name.
	 * 
	 * @param name
	 * @param value
	 */
	public void addHeader(String name, String value)
	{
		addHeaderObject(name, value);
	}


	/**
	 * Sets request locale.
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	@Override
	public Locale getLocale()
	{
		return locale;
	}

	@Override
	public List<String> getHeaders(String name)
	{
		List<String> res = new ArrayList<String>();
		List<Object> values = headers.get(name);
		if (values != null)
		{
			for (Object value : values)
			{
				if (value != null)
				{
					res.add(value.toString());
				}
			}
		}
		return res;
	}

	/**
	 * Removes header with specified name.
	 * 
	 * @param header
	 */
	public void removeHeader(String header)
	{
		headers.remove(header);
	}


	@Override
	public MockRequestParameters getPostParameters()
	{
		return postRequestParameters;
	}

	@Override
	public Charset getCharset()
	{
		return Charset.forName("UTF-8");
	}

	@Override
	public Url getClientUrl()
	{
		return new Url(url.getSegments(), Collections.<QueryParameter> emptyList());
	}

	@Override
	public Object getContainerRequest()
	{
		return this;
	}

	@Override
	public String getContextPath()
	{
		return UrlUtils.normalizePath(contextPath);
	}

	/**
	 * @param contextPath
	 * @return this
	 */
	public MockWebRequest setContextPath(String contextPath)
	{
		this.contextPath = contextPath;
		return this;
	}

	@Override
	public String getFilterPath()
	{
		return UrlUtils.normalizePath(filterPath);
	}

	/**
	 * @param filterPath
	 * @return this
	 */
	public MockWebRequest setFilterPath(String filterPath)
	{
		this.filterPath = filterPath;
		return this;
	}

	@Override
	public String getPrefixToContextPath()
	{
		return prefixToContextPath;
	}

	/**
	 * @param prefixToContextPath
	 * @return this
	 */
	public MockWebRequest setPrefixToContextPath(String prefixToContextPath)
	{
		this.prefixToContextPath = prefixToContextPath;
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17811.java