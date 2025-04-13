error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15847.java
text:
```scala
r@@eturn this;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.http.WebResponse;

/**
 * Mocked {@link WebResponse}.
 * 
 * @author Matej Knopp
 */
public class MockWebResponse extends WebResponse
{

	/**
	 * Construct.
	 */
	public MockWebResponse()
	{
	}

	private final List<Cookie> cookies = new ArrayList<Cookie>();

	@Override
	public void addCookie(Cookie cookie)
	{
		cookies.add(cookie);
	}

	@Override
	public void clearCookie(Cookie cookie)
	{
		cookies.remove(cookie);
	}

	/**
	 * @return cookies set in this response
	 */
	public List<Cookie> getCookies()
	{
		return Collections.unmodifiableList(cookies);
	}

	private String redirectUrl;

	@Override
	public void sendRedirect(String url)
	{
		redirectUrl = url;
	}

	/**
	 * @return redirect URL or <code>null</code> if {@link #sendRedirect(String)} was not called.
	 */
	public String getRedirectUrl()
	{
		return redirectUrl;
	}

	/**
	 * @return <code>true</code> if redirect URL was set, <code>false</code> otherwise.
	 */
	@Override
	public boolean isRedirect()
	{
		return redirectUrl != null;
	}

	private Long contentLength;

	@Override
	public void setContentLength(long length)
	{
		contentLength = length;
		setHeader("Content-Length", String.valueOf(length));
	}

	/**
	 * @return content length (set by {@link #setContentLength(long)})
	 */
	public Long getContentLength()
	{
		return contentLength;
	}

	private String contentType;

	@Override
	public void setContentType(String mimeType)
	{
		contentType = mimeType;
	}

	/**
	 * @return content mime type
	 */
	public String getContentType()
	{
		return contentType;
	}

	private final Map<String, Object> headers = new HashMap<String, Object>();

	@Override
	public void setDateHeader(String name, long date)
	{
		headers.put(name, date);
	}

	/**
	 * @param name
	 * 
	 * @return date header with specified name
	 */
	public long getDateHeader(String name)
	{
		Object value = headers.get(name);
		if (value == null)
		{
			throw new WicketRuntimeException("Date header '" + name + "' is not set.");
		}
		else if (value instanceof Long == false)
		{
			throw new WicketRuntimeException("Header '" + name + "' is not date type.");
		}
		else
		{
			return (Long)value;
		}
	}

	@Override
	public void setHeader(String name, String value)
	{
		headers.put(name, value);
		if (name.equals("Content-Type"))
		{
			setContentType(value);
		}
	}

	/**
	 * @param name
	 * 
	 * @return header string with specified name
	 */
	public String getHeader(String name)
	{
		Object value = headers.get(name);
		return value != null ? value.toString() : null;
	}

	/**
	 * @param name
	 * 
	 * @return <code>true</code> if the header was set, <code>false</code> otherwise
	 */
	public boolean hasHeader(String name)
	{
		return headers.containsKey(name);
	}

	/**
	 * @return set of all header names
	 */
	public Set<String> getHeaderNames()
	{
		return Collections.unmodifiableSet(headers.keySet());
	}

	Integer status;

	@Override
	public void setStatus(int sc)
	{
		status = sc;
	}

	/**
	 * @return status code or <code>null</code> if status was not set
	 */
	public Integer getStatus()
	{
		return status;
	}

	@Override
	public String encodeURL(CharSequence url)
	{
		return url.toString();
	}

	private StringBuilder textResponse;
	private ByteArrayOutputStream binaryResponse;

	@Override
	public void write(CharSequence sequence)
	{
		if (binaryResponse != null)
		{
			throw new IllegalStateException("Binary response has already been initiated.");
		}
		if (textResponse == null)
		{
			textResponse = new StringBuilder();
		}
		textResponse.append(sequence);
	}

	/**
	 * @return text response
	 */
	public CharSequence getTextResponse()
	{
		return textResponse;
	}

	@Override
	public void write(byte[] array)
	{
		if (textResponse != null)
		{
			throw new IllegalStateException("Text response has already been initiated.");
		}
		if (binaryResponse == null)
		{
			binaryResponse = new ByteArrayOutputStream();
		}
		try
		{
			binaryResponse.write(array);
		}
		catch (IOException ignored)
		{
		}
	}

	/**
	 * @return binary response
	 */
	public byte[] getBinaryResponse()
	{
		if (binaryResponse == null)
		{
			return null;
		}
		else
		{
			byte[] bytes = binaryResponse.toByteArray();
			if (getContentLength() != null)
			{
				byte[] trimmed = new byte[getContentLength().intValue()];
				System.arraycopy(bytes, 0, trimmed, 0, getContentLength().intValue());
				return trimmed;
			}
			return bytes;
		}
	}

	String errorMessage;

	@Override
	public void sendError(int sc, String msg)
	{
		status = sc;
		errorMessage = msg;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	@Override
	public void flush()
	{
	}

	@Override
	public void reset()
	{
		super.reset();
		if (binaryResponse != null)
		{
			binaryResponse = new ByteArrayOutputStream();
		}
		contentLength = null;
		contentType = null;
		if (cookies != null)
		{
			cookies.clear();
		}
		errorMessage = null;
		if (headers != null)
		{
			headers.clear();
		}
		redirectUrl = null;
		status = null;
		if (textResponse != null)
		{
			textResponse.setLength(0);
		}

	}

	@Override
	public Object getContainerResponse()
	{
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15847.java