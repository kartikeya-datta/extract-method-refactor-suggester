error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3383.java
text:
```scala
i@@tem.render();

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
package wicket.markup.html.list;

import wicket.MarkupContainer;
import wicket.WicketRuntimeException;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * A very simple loop component whose model is an Integer defining the number of
 * iterations the loop should render. During rendering, Loop iterates from 0 to
 * getIterations() - 1, creating a new MarkupContainer for each iteration. The
 * MarkupContainer is populated by the Loop subclass by implementing the
 * abstract method populate(LoopItem). The populate() method is called just
 * before the LoopItem container is rendered.
 * 
 * @author Juergen Donnerstag
 * @author Eelco Hillenius
 * @author Jonathan Locke
 */
public abstract class Loop extends AbstractRepeater<Integer>
{
	/**
	 * Item container for a Loop iteration.
	 * 
	 * @author Jonathan Locke
	 */
	public static final class LoopItem extends WebMarkupContainer<Integer>
	{
		private static final long serialVersionUID = 1L;

		/** The iteration number */
		private final int iteration;

		/**
		 * Constructor
		 * 
		 * @param parent
		 *            The parent of this component
		 * 
		 * @param iteration
		 *            The iteration of the loop
		 */
		private LoopItem(MarkupContainer parent, final int iteration)
		{
			super(parent, Integer.toString(iteration));
			this.iteration = iteration;
		}

		/**
		 * @return Returns the iteration.
		 */
		public int getIteration()
		{
			return iteration;
		}
	}

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param iterations
	 *            max index of the loop
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Loop(MarkupContainer parent, final String id, final int iterations)
	{
		super(parent, id, new Model<Integer>(iterations));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            Must contain a Integer model object
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Loop(MarkupContainer parent, final String id, final IModel<Integer> model)
	{
		super(parent, id, model);
	}

	/**
	 * @return The number of loop iterations
	 */
	public final int getIterations()
	{
		return getModelObject();
	}

	/**
	 * @see wicket.Component#internalOnAttach()
	 */
	@Override
	protected void internalOnAttach()
	{
		// Remove any previous loop contents
		removeAll();

		// Get number of iterations
		final int iterations = getIterations();
		if (iterations > 0)
		{
			// Create LoopItems for each iteration
			for (int iteration = 0; iteration < iterations; iteration++)
			{
				// Create item for loop iteration
				LoopItem item = newItem(iteration);

				// Add and populate item
				populateItem(item);
			}
		}
	}

	/**
	 * Create a new LoopItem for loop at iteration.
	 * 
	 * @param iteration
	 *            iteration in the loop
	 * @return LoopItem
	 */
	protected LoopItem newItem(int iteration)
	{
		return new LoopItem(this, iteration);
	}

	/**
	 * 
	 * @see wicket.Component#onRender(wicket.markup.MarkupStream)
	 */
	@Override
	protected final void onRender(final MarkupStream markupStream)
	{
		// Get number of iterations
		final int iterations = getIterations();
		if (iterations > 0)
		{
			// Loop through the markup in this container for each item
			for (int iteration = 0; iteration < iterations; iteration++)
			{
				// Get item for iteration
				final LoopItem item = (LoopItem)get(Integer.toString(iteration));

				// Item should have been constructed in internalOnBeginRequest
				if (item == null)
				{
					throw new WicketRuntimeException(
							"Loop item is null.  Probably the number of loop iterations were changed between onBeginRequest and render time.");
				}

				// Render iteration
				renderItem(item);
			}
		}
		markupStream.skipComponent();
	}

	/**
	 * Populates this loop item.
	 * 
	 * @param item
	 *            The iteration of the loop
	 */
	protected abstract void populateItem(LoopItem item);

	/**
	 * Renders this loop iteration.
	 * 
	 * @param item
	 *            The loop iteration
	 */
	protected void renderItem(final LoopItem item)
	{
		item.render(new MarkupStream(getMarkupFragment()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3383.java