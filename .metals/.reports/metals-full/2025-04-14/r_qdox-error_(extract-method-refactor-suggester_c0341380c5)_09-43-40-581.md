error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15409.java
text:
```scala
r@@eturn MockHomePage.class;

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

import javax.servlet.ServletContext;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ng.request.component.IRequestablePage;
import org.apache.wicket.ng.request.cycle.RequestCycle;
import org.apache.wicket.ng.request.cycle.RequestCycleContext;
import org.apache.wicket.ng.request.handler.impl.RenderPageRequestHandler;
import org.apache.wicket.ng.request.handler.impl.render.RenderPageRequestHandlerDelegate;
import org.apache.wicket.ng.request.handler.impl.render.WebRenderPageRequestHandlerDelegate;
import org.apache.wicket.pageStore.IPageManager;
import org.apache.wicket.pageStore.IPageManagerContext;
import org.apache.wicket.protocol.http.MockServletContext;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.session.ISessionStore;

public class MockApplication extends WebApplication
{

	public MockApplication()
	{
	}

	@Override
	public Class<? extends Page> getHomePage()
	{
		return null;
	}

	@Override
	public String getConfigurationType()
	{
		return DEVELOPMENT;
	}

	@Override
	protected MockRequestCycle newRequestCycle(RequestCycleContext context)
	{
		return new MockRequestCycle(context);
	}

	private IRequestablePage lastRenderedPage;

	public IRequestablePage getLastRenderedPage()
	{
		return lastRenderedPage;
	}

	public void clearLastRenderedPage()
	{
		lastRenderedPage = null;
	}

	@Override
	public RenderPageRequestHandlerDelegate getRenderPageRequestHandlerDelegate(
		RenderPageRequestHandler renderPageRequestHandler)
	{
		return new WebRenderPageRequestHandlerDelegate(renderPageRequestHandler)
		{
			@Override
			public void respond(RequestCycle requestCycle)
			{
				lastRenderedPage = getPageProvider().getPageInstance();
				super.respond(requestCycle);
			}
		};
	}

	@Override
	protected IPageManager newPageManager(IPageManagerContext context)
	{
		return new MockPageManager(context);
	}

	/**
	 * @see org.apache.wicket.ng.protocol.http.WebApplication#newSessionStore()
	 */
	@Override
	public ISessionStore newSessionStore()
	{
		return new MockSessionStore();
	}

	public Session getSession()
	{
		return getSessionStore().lookup(null);
	}

	private MockServletContext mockServletContext;

	@Override
	public ServletContext getServletContext()
	{
		return mockServletContext;
	}

	@Override
	public final String getInitParameter(String key)
	{
		return null;
	}

	@Override
	protected void internalInit()
	{
		// TODO NG What should the proper path be
		mockServletContext = new MockServletContext(this, "");
		super.internalInit();
	}

	public void destroy()
	{
		internalDestroy();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15409.java