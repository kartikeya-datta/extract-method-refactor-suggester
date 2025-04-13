error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/740.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/740.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/740.java
text:
```scala
p@@age.render();

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
package org.apache.wicket.request.target.component;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;

/**
 * Request target for bookmarkable page links that also contain component path and interface name.
 * This is used for stateless forms and stateless links.
 * 
 * @author Matej Knopp
 */
public class BookmarkableListenerInterfaceRequestTarget extends BookmarkablePageRequestTarget
{
	private final String componentPath;
	private final String interfaceName;

	/**
	 * This constructor is called when a stateless link is clicked on but the page wasn't found in
	 * the session. Then this class will recreate the page and call the interface method on the
	 * component that is targeted with the component path.
	 * 
	 * @param pageMapName
	 * @param pageClass
	 * @param pageParameters
	 * @param componentPath
	 * @param interfaceName
	 * @param versionNumber
	 */
	public BookmarkableListenerInterfaceRequestTarget(String pageMapName,
		Class<? extends Page> pageClass, PageParameters pageParameters, String componentPath,
		String interfaceName, int versionNumber)
	{
		super(pageMapName, pageClass, pageParameters);
		this.componentPath = componentPath;
		this.interfaceName = interfaceName;
	}

	/**
	 * This constructor is called for generating the urls (RequestCycle.urlFor()) So it will alter
	 * the PageParameters to include the 2 org.apache.wicket params
	 * {@link WebRequestCodingStrategy#BOOKMARKABLE_PAGE_PARAMETER_NAME} and
	 * {@link WebRequestCodingStrategy#INTERFACE_PARAMETER_NAME}
	 * 
	 * @param pageMapName
	 * @param pageClass
	 * @param pageParameters
	 * @param component
	 * @param listenerInterface
	 */
	public BookmarkableListenerInterfaceRequestTarget(String pageMapName,
		Class<? extends Page> pageClass, PageParameters pageParameters, Component component,
		RequestListenerInterface listenerInterface)
	{
		this(pageMapName, pageClass, pageParameters, component.getPath(),
			listenerInterface.getName(), 0);

		int version = 0;
		setPage(component.getPage());

		// add the wicket:interface param to the params.
		// pagemap:(pageid:componenta:componentb:...):version:interface:behavior:urlDepth
		AppendingStringBuffer param = new AppendingStringBuffer(4 + componentPath.length() +
			interfaceName.length());
		if (pageMapName != null)
		{
			param.append(pageMapName);
		}
		param.append(Component.PATH_SEPARATOR);
		param.append(getComponentPath());
		param.append(Component.PATH_SEPARATOR);
		if (version != 0)
		{
			param.append(version);
		}
		// Interface
		param.append(Component.PATH_SEPARATOR);
		param.append(getInterfaceName());

		// Behavior (none)
		param.append(Component.PATH_SEPARATOR);

		// URL depth (not required)
		param.append(Component.PATH_SEPARATOR);

		pageParameters.put(WebRequestCodingStrategy.INTERFACE_PARAMETER_NAME, param.toString());
	}

	@Override
	public void processEvents(RequestCycle requestCycle)
	{
		Page page = getPage();
		if (page == null)
		{
			page = Session.get().getPage(getPageMapName(), componentPath, -1);
			if (page != null && page.getClass() == getPageClass())
			{
				setPage(page);
			}
			else
			{
				page = getPage(requestCycle);
			}
		}

		if (page == null)
		{
			throw new PageExpiredException(
				"Request cannot be processed. The target page does not exist anymore.");
		}

		final String pageRelativeComponentPath = Strings.afterFirstPathComponent(componentPath,
			Component.PATH_SEPARATOR);
		Component component = page.get(pageRelativeComponentPath);
		if (component == null)
		{
			// this is quite a hack to get components in repeater work.
			// But it still can fail if the repeater is a paging one or on every render
			// it will generate new index for the items...
			page.prepareForRender(false);
			component = page.get(pageRelativeComponentPath);
			if (component == null)
			{
				throw new WicketRuntimeException(
					"unable to find component with path " +
						pageRelativeComponentPath +
						" on stateless page " +
						page +
						" it could be that the component is inside a repeater make your component return false in getStatelessHint()");
			}
		}
		RequestListenerInterface listenerInterface = RequestListenerInterface.forName(interfaceName);
		if (listenerInterface == null)
		{
			throw new WicketRuntimeException("unable to find listener interface " + interfaceName);
		}
		listenerInterface.invoke(page, component);
	}

	@Override
	public void respond(RequestCycle requestCycle)
	{
		Page page = getPage(requestCycle);
		// if the listener call wanted to redirect
		// then do that if the page is not stateless.
		if (requestCycle.isRedirect() && !page.isPageStateless())
		{
			requestCycle.redirectTo(page);
		}
		else
		{
			// else render the page directly
			page.renderPage();
		}
	}

	/**
	 * @return The component path.
	 */
	public String getComponentPath()
	{
		return componentPath;
	}

	/**
	 * @return The interface name
	 */
	public String getInterfaceName()
	{
		return interfaceName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/740.java