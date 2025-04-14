error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5204.java
text:
```scala
l@@istener.renderHead(headerContainer, headerContainer.getHeaderResponse());

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
package org.apache.wicket.markup.renderStrategy;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Generics;

/**
 * An abstract implementation of a header render strategy which is only missing the code to traverse
 * the child hierarchy, since the sequence of that traversal is what will make the difference
 * between the different header render strategies.
 * 
 * Besides the child hierarchy the render sequence by default (may be changed via subclassing) is as
 * follows:
 * <ul>
 * <li>1. application level headers</li>
 * <li>2. the root component's headers</li>
 * <li>3. the children hierarchy (to be implemented per subclass)</li>
 * </ul>
 * 
 * @author Juergen Donnerstag
 */
public abstract class AbstractHeaderRenderStrategy implements IHeaderRenderStrategy
{
	/** Application level contributors */
	private List<IHeaderContributor> renderHeadListeners;

	/**
	 * @return Gets the strategy registered with the application
	 */
	public static IHeaderRenderStrategy get()
	{
		// NOT OFFICIALLY SUPPORTED BY WICKET
		// By purpose it is "difficult" to change to another render strategy.
		// We don't want it to be modifiable by users, but we needed a way to easily test other
		// strategies.
		String className = System.getProperty("Wicket_HeaderRenderStrategy");
		if (className != null)
		{
			Class<?> clazz = null;
			try
			{
				clazz = Application.get()
					.getApplicationSettings()
					.getClassResolver()
					.resolveClass(className);

				if (clazz != null)
				{
					return (IHeaderRenderStrategy)clazz.newInstance();
				}
			}
			catch (ClassNotFoundException ex)
			{
				// ignore
			}
			catch (InstantiationException ex)
			{
				// ignore
			}
			catch (IllegalAccessException ex)
			{
				// ignore
			}
		}

		// Our default header render strategy
		// Pre 1.5
		// return new ParentFirstHeaderRenderStrategy();

		// Since 1.5
		return new ChildFirstHeaderRenderStrategy();
	}

	/**
	 * Construct.
	 */
	public AbstractHeaderRenderStrategy()
	{
	}

	public void renderHeader(final HtmlHeaderContainer headerContainer,
		final Component rootComponent)
	{
		Args.notNull(headerContainer, "headerContainer");
		Args.notNull(rootComponent, "rootComponent");

		// First the application level headers
		renderApplicationLevelHeaders(headerContainer);

		// Than the root component's headers
		renderRootComponent(headerContainer, rootComponent);

		// Than its child hierarchy
		renderChildHeaders(headerContainer, rootComponent);
	}

	/**
	 * Render the root component (e.g. Page).
	 * 
	 * @param headerContainer
	 * @param rootComponent
	 */
	protected void renderRootComponent(final HtmlHeaderContainer headerContainer,
		final Component rootComponent)
	{
		rootComponent.renderHead(headerContainer);
	}

	/**
	 * Render the child hierarchy headers.
	 * 
	 * @param headerContainer
	 * @param rootComponent
	 */
	abstract protected void renderChildHeaders(final HtmlHeaderContainer headerContainer,
		final Component rootComponent);

	/**
	 * Render the application level headers
	 * 
	 * @param headerContainer
	 */
	protected final void renderApplicationLevelHeaders(final HtmlHeaderContainer headerContainer)
	{
		Args.notNull(headerContainer, "headerContainer");

		if (renderHeadListeners != null)
		{
			for (IHeaderContributor listener : renderHeadListeners)
			{
				listener.renderHead(headerContainer.getHeaderResponse());
			}
		}
	}

	/**
	 * Add an application level contributor who's content will be added to any page or ajax
	 * response.
	 * 
	 * @param contributor
	 */
	public final void addListener(final IHeaderContributor contributor)
	{
		if (renderHeadListeners == null)
		{
			renderHeadListeners = Generics.newArrayList();
		}
		renderHeadListeners.add(contributor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5204.java