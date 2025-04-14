error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15315.java
text:
```scala
c@@lass HeaderBufferingWebResponse extends WebResponse implements ICookieSavingResponse

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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.request.http.WebResponse;

/**
 * Response that keeps headers in buffers but writes the content directly to the response.
 * 
 * This is necessary to get {@link #reset()} working without removing the JSESSIONID cookie. When
 * {@link HttpServletResponse#reset()} is called it removes all cookies, including the JSESSIONID
 * cookie.
 * 
 * Calling {@link #reset()} on this response only clears the buffered headers. If there is any
 * content written to response it throws {@link IllegalStateException}.
 * 
 * @author Matej Knopp
 */
class HeaderBufferingWebResponse extends WebResponse implements IBufferedWebResponse
{
	private final WebResponse originalResponse;
	private final BufferedWebResponse bufferedResponse;

	public HeaderBufferingWebResponse(WebResponse originalResponse)
	{
		this.originalResponse = originalResponse;
		bufferedResponse = new BufferedWebResponse(originalResponse);
	}

	private boolean bufferedWritten = false;

	private void writeBuffered()
	{
		if (!bufferedWritten)
		{
			bufferedResponse.writeTo(originalResponse);
			bufferedWritten = true;
		}
	}

	private void checkHeader()
	{
		if (bufferedWritten)
		{
			throw new IllegalStateException("Header was already written to response!");
		}
	}

	@Override
	public void addCookie(Cookie cookie)
	{
		checkHeader();
		bufferedResponse.addCookie(cookie);
	}

	@Override
	public void clearCookie(Cookie cookie)
	{
		checkHeader();
		bufferedResponse.clearCookie(cookie);
	}

	private boolean flushed = false;

	@Override
	public void flush()
	{
		if (!bufferedWritten)
		{
			bufferedResponse.writeTo(originalResponse);
			bufferedResponse.reset();
		}
		originalResponse.flush();
		flushed = true;
	}

	@Override
	public boolean isRedirect()
	{
		return bufferedResponse.isRedirect();
	}

	@Override
	public void sendError(int sc, String msg)
	{
		checkHeader();
		bufferedResponse.sendError(sc, msg);
	}

	@Override
	public void sendRedirect(String url)
	{
		checkHeader();
		bufferedResponse.sendRedirect(url);
	}

	@Override
	public void setContentLength(long length)
	{
		checkHeader();
		bufferedResponse.setContentLength(length);
	}

	@Override
	public void setContentType(String mimeType)
	{
		checkHeader();
		bufferedResponse.setContentType(mimeType);
	}

	@Override
	public void setDateHeader(String name, long date)
	{
		checkHeader();
		bufferedResponse.setDateHeader(name, date);
	}

	@Override
	public void setHeader(String name, String value)
	{
		checkHeader();
		bufferedResponse.setHeader(name, value);
	}

	@Override
	public void setStatus(int sc)
	{
		bufferedResponse.setStatus(sc);
	}

	@Override
	public String encodeURL(CharSequence url)
	{
		return originalResponse.encodeURL(url);
	}

	@Override
	public void write(CharSequence sequence)
	{
		writeBuffered();
		originalResponse.write(sequence);
	}

	@Override
	public void write(byte[] array)
	{
		writeBuffered();
		originalResponse.write(array);
	}

	@Override
	public void reset()
	{
		if (flushed)
		{
			throw new IllegalStateException("Response has already been flushed!");
		}
		bufferedResponse.reset();
		bufferedWritten = false;
	}

	public void transferCookies(WebResponse webResponse)
	{
		bufferedResponse.transferCookies(webResponse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15315.java