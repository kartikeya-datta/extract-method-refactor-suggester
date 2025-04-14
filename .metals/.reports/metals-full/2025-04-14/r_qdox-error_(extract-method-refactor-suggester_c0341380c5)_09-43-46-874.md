error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4624.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4624.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4624.java
text:
```scala
protected final I@@BreadCrumbParticipant getParticipant(final String componentId)

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
package org.apache.wicket.extensions.breadcrumb.panel;

import org.apache.wicket.extensions.breadcrumb.BreadCrumbLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;

/**
 * Bread crumb link specifically for {@link BreadCrumbPanel bread crumb panels}. It uses a
 * {@link IBreadCrumbPanelFactory bread crumb factory} to function.
 * 
 * @author Eelco Hillenius
 */
public class BreadCrumbPanelLink extends BreadCrumbLink
{
	private static final long serialVersionUID = 1L;

	/** The bread crumb model. */
	private final IBreadCrumbModel breadCrumbModel;

	/** factory for creating bread crumbs panels. */
	private final IBreadCrumbPanelFactory breadCrumbPanelFactory;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param caller
	 *            The calling panel which will be used to get the {@link IBreadCrumbModel bread
	 *            crumb model} from.
	 * @param panelClass
	 *            The class to use for creating instances. Must be of type {@link BreadCrumbPanel},
	 *            and must have constructor
	 *            {@link BreadCrumbPanel#BreadCrumbPanel(String, IBreadCrumbModel)}
	 */
	public BreadCrumbPanelLink(final String id, final BreadCrumbPanel caller,
		final Class<? extends BreadCrumbPanel> panelClass)
	{
		this(id, caller.getBreadCrumbModel(), new BreadCrumbPanelFactory(panelClass));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param breadCrumbModel
	 *            The bread crumb model
	 * @param panelClass
	 *            The class to use for creating instances. Must be of type {@link BreadCrumbPanel},
	 *            and must have constructor
	 *            {@link BreadCrumbPanel#BreadCrumbPanel(String, IBreadCrumbModel)}
	 */
	public BreadCrumbPanelLink(final String id, final IBreadCrumbModel breadCrumbModel,
		final Class<BreadCrumbPanel> panelClass)
	{
		this(id, breadCrumbModel, new BreadCrumbPanelFactory(panelClass));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            The component id
	 * @param breadCrumbModel
	 *            The bread crumb model
	 * @param breadCrumbPanelFactory
	 *            The factory to create bread crumb panels
	 */
	public BreadCrumbPanelLink(final String id, final IBreadCrumbModel breadCrumbModel,
		final IBreadCrumbPanelFactory breadCrumbPanelFactory)
	{
		super(id, breadCrumbModel);

		if (breadCrumbModel == null)
		{
			throw new IllegalArgumentException("argument breadCrumbModel must be not null");
		}
		if (breadCrumbPanelFactory == null)
		{
			throw new IllegalArgumentException("argument breadCrumbPanelFactory must be not null");
		}

		this.breadCrumbModel = breadCrumbModel;
		this.breadCrumbPanelFactory = breadCrumbPanelFactory;
	}

	/**
	 * Uses the set factory for creating a new instance of {@link IBreadCrumbParticipant}.
	 * 
	 * @see org.apache.wicket.extensions.breadcrumb.BreadCrumbLink#getParticipant(java.lang.String)
	 */
	@Override
	protected final IBreadCrumbParticipant getParticipant(String componentId)
	{
		return breadCrumbPanelFactory.create(componentId, breadCrumbModel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4624.java