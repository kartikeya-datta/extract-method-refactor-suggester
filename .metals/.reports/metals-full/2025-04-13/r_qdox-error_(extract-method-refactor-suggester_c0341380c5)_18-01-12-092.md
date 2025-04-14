error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5501.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5501.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5501.java
text:
```scala
o@@ldThreadContext = ThreadContext.detach();

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
package org.apache.wicket.ng.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ng.ThreadContext;
import org.apache.wicket.ng.request.IRequestHandler;
import org.apache.wicket.ng.request.IRequestMapper;
import org.apache.wicket.ng.request.Url;
import org.apache.wicket.ng.request.cycle.RequestCycle;
import org.apache.wicket.ng.request.handler.DefaultPageProvider;
import org.apache.wicket.ng.request.handler.IPageProvider;
import org.apache.wicket.ng.request.handler.PageAndComponentProvider;
import org.apache.wicket.ng.request.handler.impl.ListenerInterfaceRequestHandler;
import org.apache.wicket.ng.request.handler.impl.RenderPageRequestHandler;
import org.apache.wicket.ng.request.listener.RequestListenerInterface;
import org.apache.wicket.settings.IRequestCycleSettings.RenderStrategy;

/**
 * Experimental implementation
 * 
 * @author Matej Knopp
 */
public class WicketTester
{
	private final MockApplication application;

	private boolean followRedirects = true;

	private MockWebRequest lastRequest;
	private MockWebResponse lastResponse;

	private final List<MockWebRequest> previousRequests = new ArrayList<MockWebRequest>();
	private final List<MockWebResponse> previousResponses = new ArrayList<MockWebResponse>();

	private final ThreadContext oldThreadContext;

	/**
	 * Creates a new {@link WicketTester} instance. It also binds it's Application to current thread
	 * so all activities that needs the original application (if there was one bound to current
	 * thread) need to wait until {@link #destroy()} is invoked.
	 */
	public WicketTester()
	{
		oldThreadContext = ThreadContext.getAndClean();

		application = new MockApplication();
		application.setName("WicketTesterApplication");
		application.set();
		application.initApplication();
	}

	/**
	 * Destroys the tester. Restores {@link ThreadContext} to state before instance of
	 * {@link WicketTester} was created.
	 */
	public void destroy()
	{
		ThreadContext.restore(oldThreadContext);
		application.destroy();
	}

	int redirectCount;

	/**
	 * Processes the request in mocked Wicket environment.
	 * 
	 * @param request
	 *            request to process
	 * 
	 * @param forcedRequestHandler
	 *            optional parameter to override parsing the request URL and force
	 *            {@link IRequestHandler}
	 */
	public void processRequest(MockWebRequest request, IRequestHandler forcedRequestHandler)
	{
		try
		{
			if (lastRequest != null)
			{
				previousRequests.add(lastRequest);
			}
			if (lastResponse != null)
			{
				previousResponses.add(lastResponse);
			}

			lastRequest = request;
			lastResponse = new MockWebResponse();

			MockRequestCycle cycle = (MockRequestCycle)application.createRequestCycle(request,
				lastResponse);

			if (forcedRequestHandler != null)
			{
				cycle.forceRequestHandler(forcedRequestHandler);
			}

			cycle.processRequestAndDetach();

			if (followRedirects && lastResponse.isRedirect())
			{
				if (redirectCount == 100)
				{
					throw new IllegalStateException(
						"Possible infinite redirect detected. Bailing out.");
				}
				++redirectCount;
				Url newUrl = Url.parse(lastResponse.getRedirectUrl());
				if (newUrl.isAbsolute())
				{
					throw new WicketRuntimeException("Can not follow absolute redirect URL.");
				}

				// append redirect URL to current URL (what browser would do)
				Url mergedURL = new Url(request.getUrl().getSegments(), newUrl.getQueryParameters());
				mergedURL.concatSegments(newUrl.getSegments());

				processRequest(new MockWebRequest(mergedURL), null);

				--redirectCount;
			}
		}
		finally
		{
			redirectCount = 0;
		}
	}

	/**
	 * Renders the page specified by given {@link IPageProvider}. After render the page instance can
	 * be retreived using {@link #getLastRenderedPage()} and the rendered document will be available
	 * in {@link #getLastResponse()}.
	 * 
	 * Depending on {@link RenderStrategy} invoking this method can mean that a redirect will happen
	 * before the actual render.
	 * 
	 * @param pageProvider
	 */
	public void startPage(IPageProvider pageProvider)
	{
		MockWebRequest request = new MockWebRequest(new Url());
		IRequestHandler handler = new RenderPageRequestHandler(pageProvider);
		processRequest(request, handler);
	}

	/**
	 * Renders the page.
	 * 
	 * @see #startPage(IPageProvider)
	 * 
	 * @param page
	 */
	public void startPage(Page page)
	{
		startPage(new DefaultPageProvider(page));
	}

	/**
	 * Simulates processing URL that invokes specified {@link RequestListenerInterface} on
	 * component.
	 * 
	 * After the listener interface is invoked the page containing the component will be rendered
	 * (with an optional redirect - depending on {@link RenderStrategy}).
	 * 
	 * @param component
	 * @param listener
	 */
	public void executeListener(Component component, RequestListenerInterface listener)
	{
		// there are two ways to do this. RequestCycle could be forced to call the handler
		// directly but constructing and parsing the URL increases the chance of triggering bugs
		IRequestHandler handler = new ListenerInterfaceRequestHandler(new PageAndComponentProvider(
			component.getPage(), component), listener);

		Url url = urlFor(handler);

		processRequest(new MockWebRequest(url), null);
	}

	/**
	 * @return last request or <code>null</code> if no request has happened yet.
	 */
	public MockWebRequest getLastRequest()
	{
		return lastRequest;
	}

	/**
	 * @return last response or <code>null</code>> if no response has been produced yet.
	 */
	public MockWebResponse getLastResponse()
	{
		return lastResponse;
	}

	/**
	 * @return list of prior requests
	 */
	public List<MockWebRequest> getPreviousRequests()
	{
		return Collections.unmodifiableList(previousRequests);
	}

	/**
	 * @return list of prior responses
	 */
	public List<MockWebResponse> getPreviousResponses()
	{
		return Collections.unmodifiableList(previousResponses);
	}

	/**
	 * @return last rendered page
	 */
	public Page getLastRenderedPage()
	{
		return (Page)application.getLastRenderedPage();
	}

	/**
	 * Sets whether responses with redirects will be followed automatically.
	 * 
	 * @param followRedirects
	 */
	public void setFollowRedirects(boolean followRedirects)
	{
		this.followRedirects = followRedirects;
	}

	/**
	 * @return <code>true</code> if redirect responses will be followed automatically,
	 *         <code>false</code> otherwise.
	 */
	public boolean isFollowRedirects()
	{
		return followRedirects;
	}

	/**
	 * Encodes the {@link IRequestHandler} to {@link Url}. It should be safe to call this method
	 * outside request thread as log as no registered {@link IRequestMapper} requires a
	 * {@link RequestCycle}.
	 * 
	 * @param handler
	 * @return {@link Url} for handler.
	 */
	public Url urlFor(IRequestHandler handler)
	{
		return application.getRootRequestMapper().mapHandler(handler);
	}

	/**
	 * Returns the {@link MockApplication} for this environment.
	 * 
	 * @return application
	 */
	public MockApplication getApplication()
	{
		return application;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5501.java