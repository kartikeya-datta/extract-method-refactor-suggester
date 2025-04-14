error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6889.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6889.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6889.java
text:
```scala
public F@@ilterToolbar(MarkupContainer parent, String id, final DataTable table, final IFilterStateLocator stateLocator)

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.extensions.markup.html.repeater.data.table.filter;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.extensions.markup.html.repeater.RepeatingView;
import wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import wicket.extensions.markup.html.repeater.data.table.DataTable;
import wicket.extensions.markup.html.repeater.data.table.IColumn;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.util.string.AppendingStringBuffer;

/**
 * Toolbar that creates a form to hold form components used to filter data in
 * the data table. Form components are provided by columns that implement
 * IFilteredColumn.
 * 
 * @author Igor Vaynberg (ivaynber)
 * 
 */
public class FilterToolbar extends AbstractToolbar
{
	private static final long serialVersionUID = 1L;
	private static final String FILTER_COMPONENT_ID = "filter";

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be added to
	 * @param stateLocator
	 *            locator responsible for finding object used to store filter's
	 *            state
	 */
	public FilterToolbar(MarkupContainer<?> parent, String id, final DataTable table, final IFilterStateLocator stateLocator)
	{
		super(parent,id, table);

		if (table == null)
		{
			throw new IllegalArgumentException("argument [table] cannot be null");
		}
		if (stateLocator == null)
		{
			throw new IllegalArgumentException("argument [stateLocator] cannot be null");
		}

		// create the form used to contain all filter components

		final FilterForm form = new FilterForm(this,"filter-form", stateLocator)
		{
			private static final long serialVersionUID = 1L;

			protected void onSubmit()
			{
				table.setCurrentPage(0);
			}
		};
		add(form);

		// add javascript to restore focus to a filter component

		add(new WebMarkupContainer(this,"focus-restore")
		{
			private static final long serialVersionUID = 1L;

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				AppendingStringBuffer script = new AppendingStringBuffer("<script>_filter_focus_restore('").append(
						form.getFocusTrackerFieldCssId()).append("');</script>");
				replaceComponentTagBody(markupStream, openTag, script);
			}
		});

		// populate the toolbar with components provided by filtered columns

		RepeatingView filters = new RepeatingView(form,"filters");
		form.add(filters);

		IColumn[] cols = table.getColumns();
		for (int i = 0; i < cols.length; i++)
		{
			WebMarkupContainer item = new WebMarkupContainer(filters,filters.newChildId());
			item.setRenderBodyOnly(true);

			IColumn col = cols[i];
			Component filter = null;

			if (col instanceof IFilteredColumn)
			{
				IFilteredColumn filteredCol = (IFilteredColumn)col;
				filter = filteredCol.getFilter(item,FILTER_COMPONENT_ID, form);
			}

			if (filter == null)
			{
				filter = new NoFilter(item,FILTER_COMPONENT_ID);
			}
			else
			{
				if (!filter.getId().equals(FILTER_COMPONENT_ID))
				{
					throw new IllegalStateException(
							"filter component returned  with an invalid component id. invalid component id ["
									+ filter.getId() + "] required component id ["
									+ FILTER_COMPONENT_ID + "] generating column ["
									+ col.toString() + "] ");
				}
			}

			item.add(filter);

			filters.add(item);
		}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6889.java