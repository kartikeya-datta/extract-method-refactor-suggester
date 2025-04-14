error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/217.java
text:
```scala
r@@eturn new ErrorCodeRequestHandler(500);

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
package org.apache.wicket;

import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.markup.html.pages.ExceptionErrorPage;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.servlet.ResponseIOException;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.EmptyRequestHandler;
import org.apache.wicket.request.handler.IPageRequestHandler;
import org.apache.wicket.request.handler.ListenerInvocationNotAllowedException;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.handler.ErrorCodeRequestHandler;
import org.apache.wicket.request.mapper.StalePageException;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.settings.IExceptionSettings.UnexpectedExceptionDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If an exception is thrown when a page is being rendered this mapper will decide which error page
 * to show depending on the exception type and {@link Application#getExceptionSettings() application
 * configuration}
 */
public class DefaultExceptionMapper implements IExceptionMapper
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultExceptionMapper.class);

	public IRequestHandler map(Exception e)
	{
		try
		{
			return internalMap(e);
		}
		catch (RuntimeException e2)
		{
			// hmmm, we were already handling an exception! give up
			logger.error("unexpected exception when handling another exception: " + e.getMessage(),
				e);
			return new ErrorCodeRequestHandler(500);
		}
	}

	private IRequestHandler internalMap(Exception e)
	{
		final Application application = Application.get();

		// check if we are processing an Ajax request and if we want to invoke the failure handler
		if (isProcessingAjaxRequest())
		{
			switch (application.getExceptionSettings().getAjaxErrorHandlingStrategy())
			{
				case INVOKE_FAILURE_HANDLER :
					return new ErrorCodeRequestHandler(500);
			}
		}

		if (e instanceof StalePageException)
		{
			// If the page was stale, just rerender it
			// (the url should always be updated by an redirect in that case)
			return new RenderPageRequestHandler(new PageProvider(((StalePageException)e).getPage()));
		}
		else if (e instanceof PageExpiredException)
		{
			return createPageRequestHandler(new PageProvider(Application.get()
				.getApplicationSettings()
				.getPageExpiredErrorPage()));
		}
		else if (e instanceof AuthorizationException ||
			e instanceof ListenerInvocationNotAllowedException)
		{
			return createPageRequestHandler(new PageProvider(Application.get()
				.getApplicationSettings()
				.getAccessDeniedPage()));
		}
		else if (e instanceof ResponseIOException)
		{
			logger.error("Connection lost, give up responding.", e);
			return new EmptyRequestHandler();
		}
		else
		{

			final UnexpectedExceptionDisplay unexpectedExceptionDisplay = application.getExceptionSettings()
				.getUnexpectedExceptionDisplay();

			logger.error("Unexpected error occurred", e);

			if (IExceptionSettings.SHOW_EXCEPTION_PAGE.equals(unexpectedExceptionDisplay))
			{
				Page currentPage = extractCurrentPage();
				return createPageRequestHandler(new PageProvider(new ExceptionErrorPage(e,
					currentPage)));
			}
			else if (IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE.equals(unexpectedExceptionDisplay))
			{
				return createPageRequestHandler(new PageProvider(
					application.getApplicationSettings().getInternalErrorPage()));
			}
			else
			{
				// IExceptionSettings.SHOW_NO_EXCEPTION_PAGE
				return new EmptyRequestHandler();
			}
		}
	}

	private RenderPageRequestHandler createPageRequestHandler(PageProvider pageProvider)
	{
		RequestCycle requestCycle = RequestCycle.get();

		if (requestCycle == null)
		{
			throw new IllegalStateException(
				"there is no current request cycle attached to this thread");
		}

		/*
		 * Use NEVER_REDIRECT policy to preserve the original page's URL for non-Ajax requests and
		 * always redirect for ajax requests
		 */
		RenderPageRequestHandler.RedirectPolicy redirect = RenderPageRequestHandler.RedirectPolicy.NEVER_REDIRECT;

		if (isProcessingAjaxRequest())
		{
			redirect = RenderPageRequestHandler.RedirectPolicy.ALWAYS_REDIRECT;
		}

		return new RenderPageRequestHandler(pageProvider, redirect);
	}

	private boolean isProcessingAjaxRequest()
	{

		RequestCycle rc = RequestCycle.get();
		Request request = rc.getRequest();
		if (request instanceof WebRequest)
		{
			return ((WebRequest)request).isAjax();
		}
		return false;
	}

	/**
	 * @return the page being rendered when the exception was thrown, or {@code null} if it cannot
	 *         be extracted
	 */
	private Page extractCurrentPage()
	{
		final RequestCycle requestCycle = RequestCycle.get();

		IRequestHandler handler = requestCycle.getActiveRequestHandler();

		if (handler == null)
		{
			handler = requestCycle.getRequestHandlerScheduledAfterCurrent();
		}

		if (handler instanceof IPageRequestHandler)
		{
			IPageRequestHandler pageRequestHandler = (IPageRequestHandler)handler;
			return (Page)pageRequestHandler.getPage();
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/217.java