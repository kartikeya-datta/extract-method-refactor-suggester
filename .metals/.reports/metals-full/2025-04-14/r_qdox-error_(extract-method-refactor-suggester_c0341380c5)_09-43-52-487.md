error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14182.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14182.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14182.java
text:
```scala
r@@eturn new PageableListView<Integer>(new EmptyPage(), "table", new Model<List<Integer>>(modelList), pageSize)

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.list;

import java.util.ArrayList;
import java.util.List;

import wicket.EmptyPage;
import wicket.WicketTestCase;
import wicket.model.Model;


/**
 * Test for tables.
 * 
 * @author Juergen Donnerstag
 */
public class TableTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public TableTest(String name)
	{
		super(name);
	}

	/**
	 * creates a table.
	 * 
	 * @param modelListSize
	 * @param pageSize
	 *            size of a page
	 * @return table
	 */
	private PageableListView createTable(final int modelListSize, final int pageSize)
	{
		List<Integer> modelList = new ArrayList<Integer>();
		for (int i = 0; i < modelListSize; i++)
		{
			modelList.add(new Integer(i));
		}

		return new PageableListView(new EmptyPage(), "table", new Model<List<Integer>>(modelList), pageSize)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem listItem)
			{
				// do nothing
			}
		};
	}

	/**
	 * 
	 */
	public void testTable()
	{
		PageableListView table = createTable(20, 4);
		assertEquals(4, table.getRowsPerPage());
		assertEquals(0, table.getCurrentPage());
		assertEquals(5, table.getPageCount());
		assertEquals(4, table.getViewSize());

		table = createTable(20, 6);
		assertEquals(6, table.getRowsPerPage());
		assertEquals(0, table.getCurrentPage());
		assertEquals(4, table.getPageCount());
		assertEquals(6, table.getViewSize());

		table.setCurrentPage(1);
		assertEquals(6, table.getRowsPerPage());
		assertEquals(1, table.getCurrentPage());
		assertEquals(4, table.getPageCount());
		assertEquals(6, table.getViewSize());
		assertEquals(6, table.getStartIndex());

		table.setCurrentPage(3);
		assertEquals(6, table.getRowsPerPage());
		assertEquals(3, table.getCurrentPage());
		assertEquals(4, table.getPageCount());
		assertEquals(2, table.getViewSize());
		assertEquals(18, table.getStartIndex());
	}

	/**
	 * 
	 */
	public void testEmptyTable()
	{
		PageableListView table = createTable(0, 4);
		assertEquals(4, table.getRowsPerPage());
		assertEquals(0, table.getCurrentPage());
		assertEquals(0, table.getPageCount());
		assertEquals(0, table.getViewSize());

		// null tables are a special case used for table navigation
		// bar, where there is no underlying model necessary, as
		// listItem.getIndex() is equal to the required
		// listItem.getModelObject()
		table = new PageableListView(new EmptyPage(), "table", new Model<String>(null), 10)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem listItem)
			{
				// do nothing
			}
		};
		assertEquals(0, table.getStartIndex());
		assertEquals(0, table.getViewSize());

		// These 2 methods are deliberately not available for Tables
		// table.setStartIndex(5);
		// table.setViewSize(10);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14182.java