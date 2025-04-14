error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/767.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/767.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,134]

error in qdox parser
file content:
```java
offset: 134
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/767.java
text:
```scala
+ "] to the provided [cellItem] object. Make sure you call add() on cellItem ( cellItem.add(new MyComponent(componentId, rowModel) )")@@;

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.extensions.markup.html.repeater.data.grid;

import java.io.Serializable;
import java.util.Iterator;

import wicket.WicketRuntimeException;
import wicket.extensions.markup.html.repeater.data.DataViewBase;
import wicket.extensions.markup.html.repeater.data.IDataProvider;
import wicket.extensions.markup.html.repeater.refreshing.Item;
import wicket.extensions.markup.html.repeater.refreshing.RefreshingView;
import wicket.extensions.markup.html.repeater.util.ArrayIteratorAdapter;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Acts as a base for data-grid views. Unlike a data view a data-grid view
 * populates both rows and columns. The columns are populated by an array of
 * provided ICellPopulator objects.
 * 
 * @see DataGridView
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class AbstractDataGridView extends DataViewBase
{
	private static final long serialVersionUID = 1L;

	private static final String CELL_REPEATER_ID = "cells";
	private static final String CELL_ITEM_ID = "cell";

	private ICellPopulator[] populators;

	private transient ArrayIteratorAdapter populatorsIteratorCache;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            component id
	 * @param populators
	 *            array of ICellPopulator objects that will be used to populate
	 *            cell items
	 * @param dataProvider
	 *            data provider
	 */
	public AbstractDataGridView(String id, ICellPopulator[] populators, IDataProvider dataProvider)
	{
		super(id, dataProvider);

		this.populators = populators;
	}

	/**
	 * Returns iterator over ICellPopulator elements in the populators array.
	 * This method caches the iterator implemenation in a transient member
	 * instance.
	 * 
	 * @return iterator over ICellPopulator elements in the populators array
	 */
	private Iterator getPopulatorsIterator()
	{
		if (populatorsIteratorCache == null)
		{
			populatorsIteratorCache = new ArrayIteratorAdapter(internalGetPopulators())
			{

				protected IModel model(Object object)
				{
					return new Model((Serializable)object);
				}

			};
		}
		else
		{
			populatorsIteratorCache.reset();
		}
		return populatorsIteratorCache;
	}


	protected final void populateItem(Item item)
	{
		final IModel rowModel = item.getModel();

		// TODO General: Does this need to be a refreshing view? since the rows
		// is a refreshing view this will be recreated anyways. maybe can se
		// orderedrepeatingview instead to simplify.
		item.add(new RefreshingView(CELL_REPEATER_ID)
		{
			private static final long serialVersionUID = 1L;

			protected Iterator getItemModels()
			{
				return getPopulatorsIterator();
			}

			protected void populateItem(Item item)
			{
				final ICellPopulator populator = (ICellPopulator)item.getModelObject();
				populator.populateItem(item, CELL_ITEM_ID, rowModel);

				if (item.get("cell") == null)
				{
					throw new WicketRuntimeException(populator.getClass().getName()
							+ ".populateItem() failed to add a component with id [" + CELL_ITEM_ID
							+ "] to the provided [cellItem] argument");
				}

			}

			protected Item newItem(String id, int index, IModel model)
			{
				return newCellItem(id, index, model);
			}

		});
	}

	protected final ICellPopulator[] internalGetPopulators()
	{
		return populators;
	}

	protected final Item newItem(String id, int index, IModel model)
	{
		return newRowItem(id, index, model);
	}


	/**
	 * Factory method for Item container that represents a row.
	 * 
	 * @see Item
	 * @see RefreshingView#newItem(String, int, IModel)
	 * 
	 * @param id
	 *            component id for the new data item
	 * @param index
	 *            the index of the new data item
	 * @param model
	 *            the model for the new data item.
	 * 
	 * @return DataItem created DataItem
	 */
	protected Item newRowItem(final String id, int index, final IModel model)
	{
		return new Item(id, index, model);
	}

	/**
	 * Factory method for Item container that represents a cell.
	 * 
	 * @see Item
	 * @see RefreshingView#newItem(String, int, IModel)
	 * 
	 * @param id
	 *            component id for the new data item
	 * @param index
	 *            the index of the new data item
	 * @param model
	 *            the model for the new data item
	 * 
	 * @return DataItem created DataItem
	 */
	protected Item newCellItem(final String id, int index, final IModel model)
	{
		return new Item(id, index, model);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/767.java