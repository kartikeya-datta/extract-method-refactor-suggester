error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10626.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10626.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10626.java
text:
```scala
final I@@tem oldItem = modelToItem.get(model);

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
package wicket.markup.repeater;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wicket.MarkupContainer;
import wicket.model.IModel;

/**
 * Reuse strategy that will reuse an old item if its model is equal to a model
 * inside the newModels iterator. Useful when state needs to be kept across
 * requests for as long as the item is visible within the view.
 * <p>
 * Notice that the <u>model</u> and not the <u>model object</u> needs to
 * implement the equals method. Most of the time it is a good idea to forward
 * the equals call to the object, however if a detachable model is used it is
 * often enough to compare object ids models point to ( this saves the model
 * from loading the object).
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class ReuseIfModelsEqualStrategy implements IItemReuseStrategy
{
	private static final long serialVersionUID = 1L;

	private static IItemReuseStrategy instance = new ReuseIfModelsEqualStrategy();

	/**
	 * @return static instance
	 */
	public static IItemReuseStrategy getInstance()
	{
		return instance;
	}

	/**
	 * @see wicket.markup.repeater.IItemReuseStrategy#getItems(MarkupContainer, wicket.markup.repeater.IItemFactory,
	 *      java.util.Iterator, java.util.Iterator)
	 */
	public Iterator getItems(final MarkupContainer parent, final IItemFactory factory, final Iterator newModels,
			Iterator existingItems)
	{
		final Map<IModel< ? >, Item> modelToItem = new HashMap<IModel< ? >, Item>();
		while (existingItems.hasNext())
		{
			final Item item = (Item)existingItems.next();
			modelToItem.put(item.getModel(), item);
		}

		return new Iterator()
		{
			private int index = 0;

			public boolean hasNext()
			{
				return newModels.hasNext();
			}

			public Object next()
			{
				final IModel model = (IModel)newModels.next();
				final Item oldItem = (Item)modelToItem.get(model);

				final Item item;
				if (oldItem == null)
				{
					item = factory.newItem(parent, index, model);
				}
				else
				{
					oldItem.setIndex(index);
					item = oldItem;
				}
				index++;

				return item;
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}

		};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10626.java