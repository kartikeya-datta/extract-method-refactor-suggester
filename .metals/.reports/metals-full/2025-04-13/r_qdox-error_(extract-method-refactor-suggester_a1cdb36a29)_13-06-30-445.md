error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7354.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7354.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7354.java
text:
```scala
S@@tring componentPath = handler.getComponent().getPageRelativePath();

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
package org.apache.wicket.ng.request.mapper;

import org.apache.wicket.IRequestHandler;
import org.apache.wicket.Request;
import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.ng.request.Url;
import org.apache.wicket.ng.request.component.IRequestablePage;
import org.apache.wicket.ng.request.handler.DefaultPageProvider;
import org.apache.wicket.ng.request.handler.PageAndComponentProvider;
import org.apache.wicket.ng.request.handler.impl.ListenerInterfaceRequestHandler;
import org.apache.wicket.ng.request.handler.impl.RenderPageRequestHandler;
import org.apache.wicket.ng.request.mapper.info.ComponentInfo;
import org.apache.wicket.ng.request.mapper.info.PageComponentInfo;
import org.apache.wicket.ng.request.mapper.info.PageInfo;

/**
 * Decodes and encodes the following URLs:
 * 
 * <pre>
 *  Page Instance - Render (RenderPageRequestHandler)
 *  /wicket/page?2
 * 
 * 
 *  Page Instance - Listener (ListenerInterfaceRequestHandler)
 *  /wicket/page?2-click-foo-bar-baz
 *  /wicket/page?2-click.1-foo-bar-baz (1 is behavior index)
 * </pre>
 * 
 * @author Matej Knopp
 */
public class PageInstanceMapper extends AbstractMapper
{
	/**
	 * Construct.
	 */
	public PageInstanceMapper()
	{
	}

	/**
	 * @see org.apache.wicket.ng.request.IRequestMapper#mapRequest(org.apache.wicket.Request)
	 */
	public IRequestHandler mapRequest(Request request)
	{
		Url url = request.getUrl();
		if (urlStartsWith(url, getContext().getNamespace(), getContext().getPageIdentifier()))
		{
			PageComponentInfo info = getPageComponentInfo(url);
			if (info != null && info.getPageInfo().getPageId() != null)
			{
				Integer renderCount = info.getComponentInfo() != null ? info.getComponentInfo()
					.getRenderCount() : null;

				if (info.getComponentInfo() == null)
				{
					DefaultPageProvider provider = new DefaultPageProvider(info.getPageInfo()
						.getPageId(), renderCount);
					provider.setPageSource(getContext());
					// render page
					return new RenderPageRequestHandler(provider);
				}
				else
				{
					ComponentInfo componentInfo = info.getComponentInfo();

					PageAndComponentProvider provider = new PageAndComponentProvider(
						info.getPageInfo().getPageId(), renderCount,
						componentInfo.getComponentPath());

					provider.setPageSource(getContext());

					// listener interface
					RequestListenerInterface listenerInterface = requestListenerInterfaceFromString(componentInfo.getListenerInterface());

					return new ListenerInterfaceRequestHandler(provider, listenerInterface,
						componentInfo.getBehaviorIndex());
				}
			}
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.ng.request.IRequestMapper#mapHandler(org.apache.wicket.ng.request.IRequestHandler)
	 */
	public Url mapHandler(IRequestHandler requestHandler)
	{
		PageComponentInfo info = null;

		if (requestHandler instanceof RenderPageRequestHandler)
		{
			IRequestablePage page = ((RenderPageRequestHandler)requestHandler).getPage();

			PageInfo i = new PageInfo(page);
			info = new PageComponentInfo(i, null);
		}
		else if (requestHandler instanceof ListenerInterfaceRequestHandler)
		{
			ListenerInterfaceRequestHandler handler = (ListenerInterfaceRequestHandler)requestHandler;
			IRequestablePage page = handler.getPage();
			String componentPath = handler.getComponent().getPath();
			RequestListenerInterface listenerInterface = handler.getListenerInterface();

			PageInfo pageInfo = new PageInfo(page);
			ComponentInfo componentInfo = new ComponentInfo(page.getRenderCount(),
				requestListenerInterfaceToString(listenerInterface), componentPath,
				handler.getBehaviorIndex());
			info = new PageComponentInfo(pageInfo, componentInfo);
		}

		if (info != null)
		{
			Url url = new Url();
			url.getSegments().add(getContext().getNamespace());
			url.getSegments().add(getContext().getPageIdentifier());
			encodePageComponentInfo(url, info);
			return url;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @see org.apache.wicket.ng.request.IRequestMapper#getCompatibilityScore(org.apache.wicket.Request)
	 */
	public int getCompatibilityScore(Request request)
	{
		// always return 0 here so that the mounts have higher priority
		return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7354.java