error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/488.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/488.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/488.java
text:
```scala
i@@f (parameters != null)

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
package org.apache.wicket.cdi;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.IPageClassRequestHandler;
import org.apache.wicket.request.handler.IPageRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * A request cycle listener that takes care of propagating persistent conversations.
 * 
 * @see ConversationScoped
 * 
 * @author igor
 */
public class ConversationPropagator extends AbstractRequestCycleListener
{

	private static final MetaDataKey<String> CID_KEY = new MetaDataKey<String>()
	{
	};

	private static final String CID = "cid";

	private final CdiContainer container;

	/** propagation mode to use */
	private final ConversationPropagation propagation;

	/**
	 * Constructor
	 * 
	 * @param container
	 * @param propagation
	 */
	public ConversationPropagator(CdiContainer container, ConversationPropagation propagation)
	{
		if (propagation == ConversationPropagation.NONE)
		{
			throw new IllegalArgumentException(
				"If propagation is NONE do not set up the propagator");
		}

		this.container = container;
		this.propagation = propagation;
	}

	public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler)
	{
		String cid = cycle.getRequest().getRequestParameters().getParameterValue("cid").toString();
		Page page = getPage(handler);

		if (cid == null && page != null)
		{
			cid = page.getMetaData(CID_KEY);
		}

		container.activateConversationalContext(cycle, cid);
	}

	@Override
	public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler)
	{
		Conversation conversation = container.getCurrentConversation(cycle);

		if (conversation == null)
		{
			return;
		}


		// propagate a conversation across non-bookmarkable page instances

		Page page = getPage(handler);
		if (!conversation.isTransient() && page != null)
		{
			page.setMetaData(CID_KEY, conversation.getId());
		}

	}

	public void onRequestHandlerScheduled(RequestCycle cycle, IRequestHandler handler)
	{
		Conversation conversation = container.getCurrentConversation(cycle);

		if (conversation == null || conversation.isTransient())
		{
			return;
		}

		// propagate a converastion across non-bookmarkable page instances

		Page page = getPage(handler);
		if (page != null && !conversation.isTransient())
		{
			page.setMetaData(CID_KEY, conversation.getId());
		}

		if (propagation == ConversationPropagation.ALL)
		{
			// propagate cid to a scheduled bookmarkable page

			PageParameters parameters = getPageParameters(handler);
			if (parameters != null && propagation == ConversationPropagation.ALL)
			{
				parameters.add(CID, conversation.getId());
			}
		}
	}

	@Override
	public void onUrlMapped(RequestCycle cycle, IRequestHandler handler, Url url)
	{
		Conversation conversation = container.getCurrentConversation(cycle);

		if (conversation == null || conversation.isTransient())
		{
			return;
		}

		if (propagation == ConversationPropagation.ALL)
		{
			// propagate cid to bookmarkable pages via urls
			url.setQueryParameter(CID, conversation.getId());
		}
	}

	@Override
	public void onEndRequest(RequestCycle cycle)
	{
		container.deactivateConversationalContext(cycle);
	}

	/**
	 * Resolves a page instance from the request handler iff the page instance is already created
	 * 
	 * @param handler
	 * @return page or {@code null} if none
	 */
	protected Page getPage(IRequestHandler handler)
	{
		if (handler instanceof IPageRequestHandler)
		{
			IPageRequestHandler pageHandler = (IPageRequestHandler)handler;
			if (pageHandler.isPageInstanceCreated())
			{
				return (Page)pageHandler.getPage();
			}
		}
		return null;
	}

	/**
	 * Resolves page parameters from a request handler
	 * 
	 * @param handler
	 * @return page parameters or {@code null} if none
	 */
	protected PageParameters getPageParameters(IRequestHandler handler)
	{
		if (handler instanceof IPageClassRequestHandler)
		{
			IPageClassRequestHandler pageHandler = (IPageClassRequestHandler)handler;
			return pageHandler.getPageParameters();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/488.java