error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5067.java
text:
```scala
r@@eturn true;

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
package org.apache.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * Adapter implementation of {@link org.apache.wicket.behavior.IBehavior}. It
 * is recommended to extend from this class instead of directly implementing
 * {@link org.apache.wicket.behavior.IBehavior} as this class has an extra clean
 * 
 * @author Ralf Ebert
 * @author Eelco Hillenius
 */
public abstract class AbstractBehavior implements IBehavior, IHeaderContributor
{
	/**
	 * Construct.
	 */
	public AbstractBehavior()
	{
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#beforeRender(org.apache.wicket.Component)
	 */
	public void beforeRender(Component component)
	{
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#bind(org.apache.wicket.Component)
	 */
	public void bind(final Component component)
	{
	}

	/**
	 * This method is called either by {@link #onRendered(Component)} or
	 * {@link #onException(Component, RuntimeException)} AFTER they called their
	 * respective template methods. Override this template method to do any
	 * necessary cleanup.
	 */
	public void cleanup()
	{
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#detach(Component)
	 */
	public void detach(Component component)
	{

	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#exception(org.apache.wicket.Component,
	 *      java.lang.RuntimeException)
	 */
	public final void exception(Component component, RuntimeException exception)
	{
		try
		{
			onException(component, exception);
		}
		finally
		{
			cleanup();
		}
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#getStatelessHint(org.apache.wicket.Component)
	 */
	public boolean getStatelessHint(Component component)
	{
		return true;
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#onComponentTag(org.apache.wicket.Component,
	 *      org.apache.wicket.markup.ComponentTag)
	 */
	public void onComponentTag(final Component component, final ComponentTag tag)
	{
	}

	/**
	 * In case an unexpected exception happened anywhere between
	 * onComponentTag() and rendered(), onException() will be called for any
	 * behavior.
	 * 
	 * @param component
	 *            the component that has a reference to this behavior and during
	 *            which processing the exception occured
	 * @param exception
	 *            the unexpected exception
	 */
	public void onException(Component component, RuntimeException exception)
	{
	}

	/**
	 * Called when a component that has this behavior coupled was rendered.
	 * 
	 * @param component
	 *            the component that has this behavior coupled
	 */
	public void onRendered(Component component)
	{
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#afterRender(org.apache.wicket.Component)
	 */
	public final void afterRender(final Component component)
	{
		try
		{
			onRendered(component);
		}
		finally
		{
			cleanup();
		}
	}

	/**
	 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	public void renderHead(IHeaderResponse response)
	{
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#isEnabled(Component)
	 */
	public boolean isEnabled(Component component)
	{
		return component.isEnabled();
	}

	/**
	 * @see org.apache.wicket.behavior.IBehavior#isTemporary()
	 */
	public boolean isTemporary()
	{
		return false;
	}

	// TODO remove these methods after compatibility release

	/**
	 * @param component
	 * @deprecated replaced by {@link #detach(Component)}
	 */
	public final void detachModel(Component component)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param component
	 * @deprecated replaced by {@link #afterRender(Component)}
	 */
	public final void rendered(Component component)
	{
		throw new UnsupportedOperationException();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5067.java