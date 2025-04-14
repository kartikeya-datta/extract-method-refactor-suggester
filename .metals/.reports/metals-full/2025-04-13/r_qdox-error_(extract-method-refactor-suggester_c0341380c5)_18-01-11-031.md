error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5052.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5052.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5052.java
text:
```scala
c@@urrent = webRequest.getClientUrl();

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

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

/**
 * WebResponse that wraps a {@link ServletWebResponse}.
 * 
 * @author Matej Knopp
 */
public class ServletWebResponse extends WebResponse
{
	private final HttpServletResponse httpServletResponse;
	private final ServletWebRequest webRequest;

	/**
	 * Construct.
	 * 
	 * @param webRequest
	 * @param httpServletResponse
	 */
	public ServletWebResponse(ServletWebRequest webRequest, HttpServletResponse httpServletResponse)
	{
		Args.notNull(webRequest, "webRequest");
		Args.notNull(httpServletResponse, "httpServletResponse");

		this.httpServletResponse = httpServletResponse;
		this.webRequest = webRequest;
	}

	/**
	 * Returns the wrapped response
	 * 
	 * @return wrapped response
	 */
	public final HttpServletResponse getHttpServletResponse()
	{
		return httpServletResponse;
	}

	@Override
	public void addCookie(Cookie cookie)
	{
		httpServletResponse.addCookie(cookie);
	}

	@Override
	public void clearCookie(Cookie cookie)
	{
		cookie.setMaxAge(0);
		cookie.setValue(null);
		addCookie(cookie);
	}

	@Override
	public void setContentLength(long length)
	{
		httpServletResponse.addHeader("Content-Length", Long.toString(length));
	}

	@Override
	public void setContentType(String mimeType)
	{
		httpServletResponse.setContentType(mimeType);
	}

	@Override
	public void setDateHeader(String name, long date)
	{
		httpServletResponse.setDateHeader(name, date);
	}

	@Override
	public void setHeader(String name, String value)
	{
		httpServletResponse.setHeader(name, value);
	}

	@Override
	public void write(CharSequence sequence)
	{
		try
		{
			httpServletResponse.getWriter().append(sequence);
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	@Override
	public void write(byte[] array)
	{
		try
		{
			httpServletResponse.getOutputStream().write(array);
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	@Override
	public void setStatus(int sc)
	{
		httpServletResponse.setStatus(sc);
	}

	@Override
	public void sendError(int sc, String msg)
	{
		try
		{
			if (msg == null)
			{
				httpServletResponse.sendError(sc);
			}
			else
			{
				httpServletResponse.sendError(sc, msg);
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	@Override
	public String encodeURL(CharSequence url)
	{
		if (url != null)
		{
			if (url.length() > 0 && url.charAt(0) == '?')
			{
				// there is a bug in apache tomcat 5.5 where tomcat doesn't put sessionid to url
				// when the URL starts with '?'. So we prepend the URL with ./ and remove it
				// afterwards (unless some container prepends session id before './' or mangles
				// the URL otherwise

				String encoded = httpServletResponse.encodeURL("./" + url.toString());
				if (encoded.startsWith("./"))
				{
					return encoded.substring(2);
				}
				else
				{
					return encoded;
				}
			}
			else
			{
				return httpServletResponse.encodeURL(url.toString());
			}
		}
		// FIXME NULL? Really?
		return httpServletResponse.encodeURL(null);
	}

	private String getAbsolutePrefix()
	{
		HttpServletRequest httpServletRequest = webRequest.getHttpServletRequest();

		String port = "";
		if (("http".equals(httpServletRequest.getScheme()) && httpServletRequest.getServerPort() != 80) ||
			("https".equals(httpServletRequest.getScheme()) && httpServletRequest.getServerPort() != 443))
		{
			port = ":" + httpServletRequest.getServerPort();
		}
		return httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + port;
	}

	private String getAbsoluteURL(String url)
	{
		if (url.startsWith("http://") || url.startsWith("https://"))
		{
			return url;
		}
		else if (url.startsWith("/"))
		{
			return getAbsolutePrefix() + url;
		}
		else
		{
			HttpServletRequest httpServletRequest = webRequest.getHttpServletRequest();
			Charset charset = RequestUtils.getCharset(httpServletRequest);

			final Url current;

			current = webRequest.getBaseUrl();

			Url append = Url.parse(url, charset);
			current.concatSegments(append.getSegments());
			Url result = new Url(current.getSegments(), append.getQueryParameters());
			return Strings.join("/", getAbsolutePrefix(), httpServletRequest.getContextPath(),
				webRequest.getFilterPrefix(), result.toString());
		}
	}

	private boolean redirect = false;

	@Override
	public void sendRedirect(String url)
	{
		try
		{
			redirect = true;
			url = getAbsoluteURL(url);
			url = httpServletResponse.encodeRedirectURL(url);

			// wicket redirects should never be cached
			disableCaching();

			if (webRequest.isAjax())
			{
				httpServletResponse.addHeader("Ajax-Location", url);

				/*
				 * usually the Ajax-Location header is enough and we do not need to the redirect url
				 * into the response, but sometimes the response is processed via an iframe (eg
				 * using multipart ajax handling) and the headers are not available because XHR is
				 * not used and that is the only way javascript has access to response headers.
				 */
				httpServletResponse.getWriter().write(
					"<ajax-response><redirect>" + url + "</redirect></ajax-response>");

				setContentType("text/xml;charset=" +
					webRequest.getHttpServletRequest().getCharacterEncoding());
			}
			else
			{
				httpServletResponse.sendRedirect(url);
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	@Override
	public boolean isRedirect()
	{
		return redirect;
	}

	@Override
	public void flush()
	{
		try
		{
			httpServletResponse.flushBuffer();
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		httpServletResponse.reset();
		redirect = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5052.java