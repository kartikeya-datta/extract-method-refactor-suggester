error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16625.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16625.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16625.java
text:
```scala
protected final v@@oid onPopulate()

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
package org.apache.wicket.markup.repeater;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.util.ModelIteratorAdapter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.version.undo.Change;


/**
 * An abstract repeater view that provides refreshing functionality to its subclasses. The view is
 * refreshed every request, making it well suited for displaying dynamic data.
 * <p>
 * The view is populated by implementing {@link RefreshingView#getItemModels() } and
 * {@link RefreshingView#populateItem(Item) } methods. RefreshingView builds the items that will be
 * rendered by looping over the models retrieved from {@link RefreshingView#getItemModels() } and
 * calling the {@link RefreshingView#newItem(String, int, IModel) } to generate the child item
 * container followed by a call to {@link RefreshingView#populateItem(Item) } to let the user
 * populate the newly created item container with custom components.
 * </p>
 * <p>
 * The provided {@link ModelIteratorAdapter} can make implementing
 * {@link RefreshingView#getItemModels() } easier if you have an iterator over item objects.
 * </p>
 * 
 * @see RepeatingView
 * @see ModelIteratorAdapter
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class RefreshingView extends RepeatingView
{
	private static final long serialVersionUID = 1L;

	/**
	 * The item reuse strategy that will be used to recycle items when the page is changed or the
	 * view is redrawn.
	 * 
	 * @see IItemReuseStrategy
	 */
	private IItemReuseStrategy itemReuseStrategy;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 */
	public RefreshingView(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model
	 */
	public RefreshingView(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Refresh the items in the view. Delegates the creation of items to the selected item reuse
	 * strategy
	 */
	protected void onPopulate()
	{

		IItemFactory itemFactory = new IItemFactory()
		{

			public Item newItem(int index, IModel model)
			{
				String id = RefreshingView.this.newChildId();
				Item item = RefreshingView.this.newItem(id, index, model);
				RefreshingView.this.populateItem(item);
				return item;
			}

		};

		Iterator models = getItemModels();
		Iterator items = getItemReuseStrategy().getItems(itemFactory, models, getItems());
		removeAll();
		addItems(items);
	}

	/**
	 * Returns an iterator over models for items that will be added to this view
	 * 
	 * @return an iterator over models for items that will be added to this view
	 */
	protected abstract Iterator getItemModels();

	/**
	 * Populate the given Item container.
	 * <p>
	 * <b>be carefull</b> to add any components to the item and not the view itself. So, don't do:
	 * 
	 * <pre>
	 * add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * but:
	 * 
	 * <pre>
	 * item.add(new Label(&quot;foo&quot;, &quot;bar&quot;));
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param item
	 *            The item to populate
	 */
	protected abstract void populateItem(final Item item);

	/**
	 * Factory method for Item container. Item containers are simple MarkupContainer used to
	 * aggregate the user added components for a row inside the view.
	 * 
	 * @see Item
	 * @param id
	 *            component id for the new data item
	 * @param index
	 *            the index of the new data item
	 * @param model
	 *            the model for the new data item
	 * 
	 * @return DataItem created DataItem
	 */
	protected Item newItem(final String id, int index, final IModel model)
	{
		return new Item(id, index, model);
	}

	/**
	 * @return iterator over item instances that exist as children of this view
	 */
	public Iterator getItems()
	{
		return iterator();
	}

	/**
	 * Add items to the view. Prior to this all items were removed so every request this function
	 * starts from a clean slate.
	 * 
	 * @param items
	 *            item instances to be added to this view
	 */
	protected void addItems(Iterator items)
	{
		while (items.hasNext())
		{
			add((Item)items.next());
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// ITEM GENERATION
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * @return currently set item reuse strategy. Defaults to <code>DefaultItemReuseStrategy</code>
	 *         if none was set.
	 * 
	 * @see DefaultItemReuseStrategy
	 */
	public IItemReuseStrategy getItemReuseStrategy()
	{
		if (itemReuseStrategy == null)
		{
			return DefaultItemReuseStrategy.getInstance();
		}
		return itemReuseStrategy;
	}

	/**
	 * Sets the item reuse strategy. This strategy controls the creation of {@link Item}s.
	 * 
	 * @see IItemReuseStrategy
	 * 
	 * @param strategy
	 *            item reuse strategy
	 * @return this for chaining
	 */
	public RefreshingView setItemReuseStrategy(IItemReuseStrategy strategy)
	{
		if (strategy == null)
		{
			throw new IllegalArgumentException();
		}

		if (!strategy.equals(itemReuseStrategy))
		{
			if (isVersioned())
			{
				addStateChange(new Change()
				{
					private static final long serialVersionUID = 1L;

					private final IItemReuseStrategy old = itemReuseStrategy;

					public void undo()
					{
						itemReuseStrategy = old;
					}

					public String toString()
					{
						return "ItemsReuseStrategyChange[component: " + getPath() + ", reuse: " +
								old + "]";
					}
				});
			}
			itemReuseStrategy = strategy;
		}
		return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16625.java