error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15186.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15186.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15186.java
text:
```scala
public L@@ist<FileItem> parseRequest(HttpServletRequest request) throws FileUploadException

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
package org.apache.wicket.util.upload;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * High level API for processing file uploads.
 * </p>
 * 
 * <p>
 * This class handles multiple files per single HTML widget, sent using <code>multipart/mixed</code>
 * encoding type, as specified by <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Use
 * {@link #parseRequest(HttpServletRequest)} to acquire a list of {@link
 * org.apache.wicket.util.upload.FileItem}s associated with a given HTML widget.
 * </p>
 * 
 * <p>
 * How the data for individual parts is stored is determined by the factory used to create them; a
 * given part may be in memory, on disk, or somewhere else.
 * </p>
 * 
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @author Sean C. Sullivan
 */
public class ServletFileUpload extends FileUpload
{

	// ---------------------------------------------------------- Class methods


	/**
	 * Utility method that determines whether the request contains multipart content.
	 * 
	 * @param request
	 *            The servlet request to be evaluated. Must be non-null.
	 * 
	 * @return <code>true</code> if the request is multipart; <code>false</code> otherwise.
	 */
	public static final boolean isMultipartContent(HttpServletRequest request)
	{
		if (!"post".equals(request.getMethod().toLowerCase()))
		{
			return false;
		}
		return FileUploadBase.isMultipartContent(new ServletRequestContext(request));
	}


	// ----------------------------------------------------------- Constructors


	/**
	 * Constructs an uninitialized instance of this class. A factory must be configured, using
	 * <code>setFileItemFactory()</code>, before attempting to parse requests.
	 */
	public ServletFileUpload()
	{
		super();
	}


	/**
	 * Constructs an instance of this class which uses the supplied factory to create
	 * <code>FileItem</code> instances.
	 * 
	 * @param fileItemFactory
	 */
	public ServletFileUpload(FileItemFactory fileItemFactory)
	{
		super(fileItemFactory);
	}


	// --------------------------------------------------------- Public methods


	/**
	 * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a> compliant
	 * <code>multipart/form-data</code> stream.
	 * 
	 * @param request
	 *            The servlet request to be parsed.
	 * 
	 * @return A list of <code>FileItem</code> instances parsed from the request, in the order
	 *         that they were transmitted.
	 * 
	 * @exception FileUploadException
	 *                if there are problems reading/parsing the request or storing files.
	 */
	public List /* FileItem */parseRequest(HttpServletRequest request) throws FileUploadException
	{
		return parseRequest(new ServletRequestContext(request));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15186.java