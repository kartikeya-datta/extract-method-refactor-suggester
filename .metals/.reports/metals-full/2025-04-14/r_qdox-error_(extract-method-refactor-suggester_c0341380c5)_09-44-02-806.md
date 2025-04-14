error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2380.java
text:
```scala
r@@eturn new MultipartRequest(getContainerRequest(), getFilterPrefix(), maxsize);

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
package org.apache.wicket.extensions.ajax.markup.html.form.upload;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileUploadException;

/**
 * A request object that stores information about the current upload in session so it is accessible
 * to the {@link UploadProgressBar}.
 * <p>
 * This request object is necessary for the UploadProgressBar to function properly. It is installed
 * like so:
 * 
 * <code>
 * class MyApplication extends WebApplication {
 * ...
 * 
 * @Override protected WebRequest newWebRequest(HttpServletRequest servletRequest, String
 *           filterPath) { return new UploadWebRequest(servletRequest, filterPath); } ... } </code>
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class UploadWebRequest extends ServletWebRequest
{

	/**
	 * Construct.
	 * 
	 * @param httpServletRequest
	 * @param filterPrefix
	 */
	public UploadWebRequest(final HttpServletRequest httpServletRequest, final String filterPrefix)
	{
		this(httpServletRequest, filterPrefix, null);
	}

	/**
	 * Construct.
	 * 
	 * @param httpServletRequest
	 * @param filterPrefix
	 * @param url
	 */
	public UploadWebRequest(final HttpServletRequest httpServletRequest, final String filterPrefix,
		final Url url)
	{
		super(httpServletRequest, filterPrefix, url);
	}

	/**
	 * @see org.apache.wicket.protocol.http.servlet.ServletWebRequest#newMultipartWebRequest(org.apache.wicket.util.lang.Bytes)
	 */
	@Override
	public MultipartServletWebRequest newMultipartWebRequest(final Bytes maxsize)
		throws FileUploadException
	{
		return new MultipartRequest(getHttpServletRequest(), getFilterPrefix(), maxsize);
	}

	private static final String SESSION_KEY = UploadWebRequest.class.getName();

	/**
	 * Retrieves {@link UploadInfo} from session, null if not found
	 * 
	 * @param req
	 * @return {@link UploadInfo} object from session, or null if not found
	 */
	static UploadInfo getUploadInfo(final HttpServletRequest req)
	{
		if (req == null)
		{
			throw new IllegalArgumentException("req cannot be null");
		}
		return (UploadInfo)req.getSession().getAttribute(SESSION_KEY);
	}

	/**
	 * Sets the {@link UploadInfo} object into session
	 * 
	 * @param req
	 * @param uploadInfo
	 */
	static void setUploadInfo(final HttpServletRequest req, final UploadInfo uploadInfo)
	{
		if (req == null)
		{
			throw new IllegalArgumentException("req cannot be null");
		}
		if (uploadInfo == null)
		{
			throw new IllegalArgumentException("uploadInfo cannot be null");
		}
		req.getSession().setAttribute(SESSION_KEY, uploadInfo);
	}

	/**
	 * Clears the {@link UploadInfo} object from session if one exists
	 * 
	 * @param req
	 */
	static void clearUploadInfo(final HttpServletRequest req)
	{
		if (req == null)
		{
			throw new IllegalArgumentException("req cannot be null");
		}
		req.getSession().removeAttribute(SESSION_KEY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2380.java