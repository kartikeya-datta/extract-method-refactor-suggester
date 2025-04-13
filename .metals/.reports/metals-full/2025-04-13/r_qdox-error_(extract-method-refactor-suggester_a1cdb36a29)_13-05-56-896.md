error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15377.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15377.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15377.java
text:
```scala
t@@his(parent, id, columns.toArray(new IColumn[columns.size()]), dataProvider,

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
package wicket.extensions.ajax.markup.html.repeater.data.table;

import java.util.List;

import wicket.MarkupContainer;
import wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.repeater.Item;
import wicket.markup.repeater.OddEvenItem;
import wicket.model.IModel;

/**
 * Ajaxified implementation of the {@link DefaultDataTable}
 * 
 * @see DefaultDataTable
 * @author Igor Vaynberg ( ivaynberg )
 */
public class AjaxFallbackDefaultDataTable extends DataTable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            The parent of this component The parent of this component.
	 * @param id
	 *            component id
	 * @param columns
	 *            list of columns
	 * @param dataProvider
	 *            data provider
	 * @param rowsPerPage
	 *            number of rows per page
	 */
	public AjaxFallbackDefaultDataTable(MarkupContainer parent, final String id, final List<IColumn> columns,
			SortableDataProvider dataProvider, int rowsPerPage)
	{
		this(parent, id, (IColumn[])columns.toArray(new IColumn[columns.size()]), dataProvider,
				rowsPerPage);
	}

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            The parent of this component The parent of this component.
	 * @param id
	 *            component id
	 * @param columns
	 *            array of columns
	 * @param dataProvider
	 *            data provider
	 * @param rowsPerPage
	 *            number of rows per page
	 */
	public AjaxFallbackDefaultDataTable(MarkupContainer parent, final String id, final IColumn[] columns,
			final SortableDataProvider dataProvider, int rowsPerPage)
	{
		super(parent, id, columns, dataProvider, rowsPerPage);
		setVersioned(false);

		/*
		 * TODO General: ivaynberg: this is rediculous, would be better to have
		 * a onPopulateTopToolbars() callback
		 */

		addTopToolbar(new IToolbarFactory()
		{

			private static final long serialVersionUID = 1L;

			public AbstractToolbar newToolbar(WebMarkupContainer parent, String id, DataTable dataTable)
			{
				return new NavigationToolbar(parent, id, dataTable);
			}

		});

		addTopToolbar(new IToolbarFactory()
		{
			private static final long serialVersionUID = 1L;

			public AbstractToolbar newToolbar(WebMarkupContainer parent, String id, DataTable dataTable)
			{
				return new AjaxFallbackHeadersToolbar(parent, id, dataTable, dataProvider);
			}

		});

		addBottomToolbar(new IToolbarFactory()
		{
			private static final long serialVersionUID = 1L;

			public AbstractToolbar newToolbar(WebMarkupContainer parent, String id, DataTable dataTable)
			{
				return new NoRecordsToolbar(parent, id, dataTable);
			}

		});
	}

	@Override
	protected Item newRowItem(MarkupContainer parent, final String id, int index, IModel model)
	{
		return new OddEvenItem(parent, id, index, model);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15377.java