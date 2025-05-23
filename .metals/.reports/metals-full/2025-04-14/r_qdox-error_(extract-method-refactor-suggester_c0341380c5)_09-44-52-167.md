error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12952.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12952.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12952.java
text:
```scala
r@@eturn Time.millis(value);

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
package org.apache.wicket.protocol.http.servlet;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.IWritableRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.lang.Checks;
import org.apache.wicket.util.string.PrependingStringBuffer;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.string.UrlUtils;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.upload.FileItemFactory;
import org.apache.wicket.util.upload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link WebRequest} subclass that wraps a {@link HttpServletRequest} object.
 * 
 * @author Matej Knopp
 * @author Juergen Donnerstag
 * @author Igor Vaynberg
 */
public class ServletWebRequest extends WebRequest
{
	private final HttpServletRequest httpServletRequest;

	private final Url url;

	private final String filterPrefix;

	private final ErrorAttributes errorAttributes;

	/**
	 * Construct.
	 * 
	 * @param httpServletRequest
	 * @param filterPrefix
	 *            contentPath + filterPath, used to extract the actual {@link Url}
	 */
	public ServletWebRequest(HttpServletRequest httpServletRequest, String filterPrefix)
	{
		this(httpServletRequest, filterPrefix, null);
	}

	/**
	 * Construct.
	 * 
	 * @param httpServletRequest
	 * @param filterPrefix
	 *            contentPath + filterPath, used to extract the actual {@link Url}
	 * @param url
	 */
	public ServletWebRequest(HttpServletRequest httpServletRequest, String filterPrefix, Url url)
	{
		Args.notNull(httpServletRequest, "httpServletRequest");
		Args.notNull(filterPrefix, "filterPrefix");

		this.httpServletRequest = httpServletRequest;
		this.filterPrefix = filterPrefix;

		errorAttributes = ErrorAttributes.of(httpServletRequest);

		if (url != null)
		{
			this.url = url;
		}
		else
		{
			this.url = getUrl(httpServletRequest, filterPrefix);
		}
	}

	/**
	 * Returns base url without context or filter mapping.
	 * 
	 * Example: if current url is
	 * 
	 * <pre>
	 * http://localhost:8080/context/filter/mapping/wicket/bookmarkable/com.foo.Page?1&id=2
	 * </pre>
	 * 
	 * the base url is wicket/bookmarkable/com.foo.Page
	 * 
	 * <pre>
	 * 
	 * @see org.apache.wicket.request.Request#getClientUrl()
	 */
	@Override
	public Url getClientUrl()
	{
		if (errorAttributes != null && !Strings.isEmpty(errorAttributes.getRequestUri()))
		{
			return setParameters(Url.parse(errorAttributes.getRequestUri(), getCharset()));
		}
		else if (!isAjax())
		{
			return getUrl(httpServletRequest, filterPrefix);
		}
		else
		{
			String base = null;

			base = getHeader(HEADER_AJAX_BASE_URL);

			if (base == null)
			{
				base = getRequestParameters().getParameterValue(PARAM_AJAX_BASE_URL).toString(null);
			}

			Checks.notNull(base, "Current ajax request is missing the base url header or parameter");

			return setParameters(Url.parse(base, getCharset()));
		}
	}

	private Url setParameters(Url url)
	{
		url.setPort(httpServletRequest.getServerPort());
		url.setHost(httpServletRequest.getServerName());
		url.setProtocol(httpServletRequest.getScheme());
		return url;
	}

	private Url getUrl(HttpServletRequest request, String filterPrefix)
	{
		if (filterPrefix.length() > 0 && !filterPrefix.endsWith("/"))
		{
			filterPrefix += "/";
		}
		StringBuilder url = new StringBuilder();
		String uri = request.getRequestURI();
		uri = Strings.stripJSessionId(uri);
		final int start = request.getContextPath().length() + filterPrefix.length() + 1;
		url.append(uri.substring(start));

		if (errorAttributes == null)
		{
			String query = request.getQueryString();
			if (!Strings.isEmpty(query))
			{
				url.append('?');
				url.append(query);
			}
		}

		return setParameters(Url.parse(url.toString(), getCharset()));
	}

	/**
	 * Returns the prefix of Wicket filter (without the leading /)
	 * 
	 * @return Wicket filter prefix
	 */
	public String getFilterPrefix()
	{
		return filterPrefix;
	}

	@Override
	public List<Cookie> getCookies()
	{
		Cookie[] cookies = httpServletRequest.getCookies();
		List<Cookie> result = (cookies == null) ? Collections.<Cookie> emptyList()
			: Arrays.asList(cookies);
		return Collections.unmodifiableList(result);
	}


	@Override
	public Locale getLocale()
	{
		return httpServletRequest.getLocale();
	}

	@Override
	public Time getDateHeader(String name)
	{
		long value = httpServletRequest.getDateHeader(name);

		if (value == -1)
		{
			return null;
		}

		return Time.valueOf(value);
	}

	@Override
	public String getHeader(String name)
	{
		return httpServletRequest.getHeader(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getHeaders(String name)
	{
		List<String> result = new ArrayList<String>();
		Enumeration<String> e = httpServletRequest.getHeaders(name);
		while (e.hasMoreElements())
		{
			result.add(e.nextElement());
		}
		return Collections.unmodifiableList(result);
	}

	private Map<String, List<StringValue>> postParameters = null;

	private static boolean isMultiPart(ServletRequest request)
	{
		String contentType = request.getContentType();
		return contentType != null && contentType.toLowerCase().contains("multipart");
	}

	protected Map<String, List<StringValue>> generatePostParameters()
	{
		Map<String, List<StringValue>> postParameters = new HashMap<String, List<StringValue>>();

		IRequestParameters queryParams = getQueryParameters();

		@SuppressWarnings("unchecked")
		Map<String, String[]> params = getContainerRequest().getParameterMap();
		for (Map.Entry<String, String[]> param : params.entrySet())
		{
			final String name = param.getKey();
			final String[] values = param.getValue();

			// build a mutable list of query params that have the same name as the post param
			List<StringValue> queryValues = queryParams.getParameterValues(name);
			if (queryValues == null)
			{
				queryValues = Collections.emptyList();
			}
			else
			{
				queryValues = new ArrayList<StringValue>(queryValues);
			}

			// the list that will contain accepted post param values
			List<StringValue> postValues = new ArrayList<StringValue>();

			for (String value : values)
			{
				StringValue val = StringValue.valueOf(value);
				if (queryValues.contains(val))
				{
					// if a query param with this value exists remove it and continue
					queryValues.remove(val);
				}
				else
				{
					// there is no query param with this value, assume post
					postValues.add(val);
				}
			}

			if (!postValues.isEmpty())
			{
				postParameters.put(name, postValues);
			}
		}
		return postParameters;
	}

	private Map<String, List<StringValue>> getPostRequestParameters()
	{
		if (postParameters == null)
		{
			postParameters = generatePostParameters();
		}
		return postParameters;
	}

	private final IRequestParameters postRequestParameters = new IWritableRequestParameters()
	{
		public void reset()
		{
			getPostRequestParameters().clear();
		}

		public void setParameterValues(String key, List<StringValue> values)
		{
			getPostRequestParameters().put(key, values);
		}

		public Set<String> getParameterNames()
		{
			return Collections.unmodifiableSet(getPostRequestParameters().keySet());
		}

		public StringValue getParameterValue(String name)
		{
			List<StringValue> values = getPostRequestParameters().get(name);
			if (values == null || values.isEmpty())
			{
				return StringValue.valueOf((String)null);
			}
			else
			{
				return values.iterator().next();
			}
		}

		public List<StringValue> getParameterValues(String name)
		{
			List<StringValue> values = getPostRequestParameters().get(name);
			if (values != null)
			{
				values = Collections.unmodifiableList(values);
			}
			return values;
		}
	};

	@Override
	public IRequestParameters getPostParameters()
	{
		return postRequestParameters;
	}

	@Override
	public Url getUrl()
	{
		return new Url(url);
	}

	@Override
	public ServletWebRequest cloneWithUrl(Url url)
	{
		return new ServletWebRequest(httpServletRequest, filterPrefix, url)
		{
			@Override
			public IRequestParameters getPostParameters()
			{
				// don't parse post parameters again
				return ServletWebRequest.this.getPostParameters();
			}
		};
	}

	/**
	 * Creates multipart web request from this request.
	 * 
	 * @param maxSize
	 * @return multipart request
	 * @throws FileUploadException
	 */
	public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize)
		throws FileUploadException
	{
		return new MultipartServletWebRequestImpl(getContainerRequest(), filterPrefix, maxSize);
	}

	/**
	 * Creates multipart web request from this request.
	 * 
	 * @param maxSize
	 * @param factory
	 * @return multipart request
	 * @throws FileUploadException
	 */
	public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, FileItemFactory factory)
		throws FileUploadException
	{
		return new MultipartServletWebRequestImpl(getContainerRequest(), filterPrefix, maxSize,
			factory);
	}

	private static final Logger logger = LoggerFactory.getLogger(ServletWebRequest.class);

	@Override
	public String getPrefixToContextPath()
	{
		PrependingStringBuffer buffer = new PrependingStringBuffer();
		Url filterPrefixUrl = Url.parse(filterPrefix, getCharset());
		for (int i = 0; i < filterPrefixUrl.getSegments().size() - 1; ++i)
		{
			buffer.prepend("../");
		}
		return buffer.toString();
	}

	@Override
	public Charset getCharset()
	{
		return RequestUtils.getCharset(httpServletRequest);
	}

	@Override
	public HttpServletRequest getContainerRequest()
	{
		return httpServletRequest;
	}

	@Override
	public String getContextPath()
	{
		return UrlUtils.normalizePath(httpServletRequest.getContextPath());
	}

	@Override
	public String getFilterPath()
	{
		return UrlUtils.normalizePath(filterPrefix);
	}

	@Override
	public boolean shouldPreserveClientUrl()
	{
		return errorAttributes != null && !Strings.isEmpty(errorAttributes.getRequestUri());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12952.java