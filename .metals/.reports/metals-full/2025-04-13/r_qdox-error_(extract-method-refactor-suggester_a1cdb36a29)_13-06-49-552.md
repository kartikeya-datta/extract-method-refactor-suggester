error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18346.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18346.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18346.java
text:
```scala
b@@oolean issueRedirect = (strategy == IRequestCycleSettings.RenderStrategy.REDIRECT_TO_RENDER || strategy == IRequestCycleSettings.RenderStrategy.REDIRECT_TO_BUFFER);

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
package org.apache.wicket.request.target.component.listener;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.RequestListenerInterface;
import org.apache.wicket.request.ObsoleteRequestParameters;
import org.apache.wicket.request.target.IEventProcessor;
import org.apache.wicket.request.target.component.PageRequestTarget;
import org.apache.wicket.settings.IRequestCycleSettings;

/**
 * The abstract implementation of
 * {@link org.apache.wicket.request.target.component.listener.IListenerInterfaceRequestTarget}.
 * Target that denotes a page instance and a call to a component on that page using an listener
 * interface method.
 * 
 * @author Eelco Hillenius
 * @author Johan Compagner
 */
public abstract class AbstractListenerInterfaceRequestTarget extends PageRequestTarget
	implements
		IListenerInterfaceRequestTarget,
		IEventProcessor
{
	/** The request parameters. */
	private final ObsoleteRequestParameters requestParameters;

	/** the target component. */
	private final Component component;

	/** the listener method. */
	private final RequestListenerInterface listener;

	/**
	 * Construct.
	 * 
	 * @param page
	 *            the page instance
	 * @param component
	 *            the target component
	 * @param listener
	 *            the listener interface
	 */
	public AbstractListenerInterfaceRequestTarget(final Page page, final Component component,
		RequestListenerInterface listener)
	{
		this(page, component, listener, null);
	}


	/**
	 * Construct.
	 * 
	 * @param page
	 *            the page instance
	 * @param component
	 *            the target component
	 * @param listener
	 *            the listener method
	 * @param requestParameters
	 *            the request parameter
	 */
	public AbstractListenerInterfaceRequestTarget(final Page page, final Component component,
		final RequestListenerInterface listener, final ObsoleteRequestParameters requestParameters)
	{
		super(page);

		if (component == null)
		{
			throw new IllegalArgumentException("Argument component must be not null");
		}

		this.component = component;

		if (listener == null)
		{
			throw new IllegalArgumentException("Argument listenerMethod must be not null");
		}

		this.listener = listener;
		this.requestParameters = requestParameters;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean equal = false;
		if (obj != null && obj.getClass().equals(getClass()))
		{
			AbstractListenerInterfaceRequestTarget that = (AbstractListenerInterfaceRequestTarget)obj;
			if (component.equals(that.component) && listener.equals(that.listener))
			{
				if (requestParameters != null)
				{
					return requestParameters.equals(that.requestParameters);
				}
				else
				{
					return that.requestParameters == null;
				}
			}
		}
		return equal;
	}

	/**
	 * @see org.apache.wicket.request.target.component.listener.IListenerInterfaceRequestTarget#getRequestListenerInterface()
	 */
	public final RequestListenerInterface getRequestListenerInterface()
	{
		return listener;
	}

	/**
	 * @see org.apache.wicket.request.target.component.listener.IListenerInterfaceRequestTarget#getRequestParameters()
	 */
	public final ObsoleteRequestParameters getRequestParameters()
	{
		return requestParameters;
	}

	/**
	 * @see org.apache.wicket.request.target.component.listener.IListenerInterfaceRequestTarget#getTarget()
	 */
	public final Component getTarget()
	{
		return component;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		int result = getClass().hashCode();
		result += component.hashCode();
		result += listener.hashCode();
		result += requestParameters != null ? requestParameters.hashCode() : 0;
		return 17 * result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buf = new StringBuffer(getClass().getName()).append("@")
			.append(hashCode())
			.append(getPage().toString())
			.append("->")
			.append(getTarget().getId())
			.append("->")
			.append(getRequestListenerInterface().getMethod().getDeclaringClass())
			.append(".")
			.append(getRequestListenerInterface().getName());

		if (requestParameters != null)
		{
			buf.append(" (request paramaters: ").append(requestParameters.toString()).append(")");
		}
		return buf.toString();
	}

	/**
	 * Common functionality to be called by processEvents()
	 * 
	 * @param requestCycle
	 *            The request cycle
	 */
	protected void onProcessEvents(final RequestCycle requestCycle)
	{
		getPage().startComponentRender(getTarget());

		final Application application = requestCycle.getApplication();
		// and see if we have to redirect the render part by default
		IRequestCycleSettings.RenderStrategy strategy = application.getRequestCycleSettings()
			.getRenderStrategy();
		boolean issueRedirect = (strategy == IRequestCycleSettings.REDIRECT_TO_RENDER || strategy == IRequestCycleSettings.REDIRECT_TO_BUFFER);

		requestCycle.setRedirect(issueRedirect);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18346.java