error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10001.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10001.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10001.java
text:
```scala
a@@ddToBorder(link);

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
package org.apache.wicket.extensions.markup.html.repeater.data.sort;

import org.apache.wicket.markup.html.border.Border;

/**
 * A component that wraps markup with an OrderByLink. This has the advantage of being able to add
 * the attribute modifier to the wrapping element as opposed to the link, so that it can be attached
 * to &lt;th&gt; or any other element.
 * 
 * For example:
 * 
 * &lt;th wicket:id="order-by-border"&gt;Heading&lt;/th&gt;
 * 
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class OrderByBorder extends Border
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 *            see
	 *            {@link OrderByLink#OrderByLink(String, String, ISortStateLocator, org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider) }
	 * @param property
	 *            see
	 *            {@link OrderByLink#OrderByLink(String, String, ISortStateLocator, org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider) }
	 * @param stateLocator
	 *            see
	 *            {@link OrderByLink#OrderByLink(String, String, ISortStateLocator, org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider) }
	 * @param cssProvider
	 *            see
	 *            {@link OrderByLink#OrderByLink(String, String, ISortStateLocator, org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider) }
	 */
	public OrderByBorder(final String id, final String property,
		final ISortStateLocator stateLocator, final OrderByLink.ICssProvider cssProvider)
	{
		super(id);

		OrderByLink link = newOrderByLink("orderByLink", property, stateLocator);
		add(link);
		add(new OrderByLink.CssModifier(link, cssProvider));
		link.add(getBodyContainer());
	}

	/**
	 * create new sort order toggling link
	 * 
	 * @param id
	 *            component id
	 * @param property
	 *            sort property
	 * @param stateLocator
	 *            sort state locator
	 * @return link
	 */
	protected OrderByLink newOrderByLink(final String id, final String property,
		final ISortStateLocator stateLocator)
	{
		return new OrderByLink(id, property, stateLocator,
			OrderByLink.VoidCssProvider.getInstance())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged()
			{
				OrderByBorder.this.onSortChanged();
			}
		};
	}

	/**
	 * This method is a hook for subclasses to perform an action after sort has changed
	 */
	protected void onSortChanged()
	{
		// noop
	}

	/**
	 * @param id
	 *            see {@link OrderByLink#OrderByLink(String, String, ISortStateLocator)}
	 * @param property
	 *            see {@link OrderByLink#OrderByLink(String, String, ISortStateLocator)}
	 * @param stateLocator
	 *            see {@link OrderByLink#OrderByLink(String, String, ISortStateLocator)}
	 */
	public OrderByBorder(final String id, final String property,
		final ISortStateLocator stateLocator)
	{
		this(id, property, stateLocator, OrderByLink.DefaultCssProvider.getInstance());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10001.java