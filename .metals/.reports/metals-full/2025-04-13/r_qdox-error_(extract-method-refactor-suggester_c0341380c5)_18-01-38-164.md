error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5294.java
text:
```scala
c@@ycle.setResponsePage(page);

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
package wicket.markup.html.list;

import java.io.IOException;
import java.util.ArrayList;

import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.Model;
import wicket.protocol.http.WebRequestCycle;
import wicket.protocol.http.MockWebApplication;
import wicket.protocol.http.MockPage;


import junit.framework.TestCase;

/**
 * Test for ListView
 * @author Juergen Donnerstag
 */
public class ListViewTest extends TestCase
{
	/** Use a mock application to handle Link-clicked event */
	private static MockWebApplication application;

	protected void setUp() throws Exception
	{
		super.setUp();

		if (application == null)
		{
			application = new MockWebApplication(null);
		}
	}

	/**
	 * Create a predefined ListView
	 * @param modelListSize # of elements to go into the list
	 * @return list view
	 */
	private ListView createListView(final int modelListSize)
	{
		ArrayList modelList = new ArrayList();
		for (int i = 0; i < modelListSize; i++)
		{
			modelList.add(new Integer(i));
		}

		return new ListView("listView", new Model(modelList))
		{
			protected void populateItem(final ListItem listItem)
			{
				; // do nothing
			}
		};
	}

	/**
	 * Helper: Create a request cycle and set the next page to render
	 * @return request cycle
	 * @throws IOException
	 */
	private WebRequestCycle createRequestCycle() throws IOException
	{
		// Prepare the mock application to test the Link
		application.setupRequestAndResponse();
		WebRequestCycle cycle = new WebRequestCycle(application, application.getWicketSession(),
				application.getWicketRequest(), application.getWicketResponse());

		MockPage page = new MockPage(null);
		cycle.setPage(page);

		return cycle;
	}

	/**
	 * 
	 */
	public void testListView()
	{
		ListView lv = createListView(4);
		assertEquals(4, lv.getList().size());
		assertEquals(4, lv.getViewSize());
		assertEquals(0, lv.getStartIndex());
		assertEquals(new Integer(0), lv.getListObject(0));

		// This is the number of ListViews child-components
		assertEquals(0, lv.size());

		lv.setStartIndex(-1);
		assertEquals(0, lv.getStartIndex());

		lv.setStartIndex(3);
		assertEquals(3, lv.getStartIndex());

		// The upper boundary doesn't get tested, yet.
		lv.setStartIndex(99);
		assertEquals(99, lv.getStartIndex());

		lv.setViewSize(-1);
		assertEquals(0, lv.getViewSize());

		lv.setViewSize(0);
		assertEquals(0, lv.getViewSize());

		// The upper boundary doesn't get tested, yet.
		lv.setViewSize(99);
		assertEquals(0, lv.getViewSize());
		lv.setStartIndex(1);
		assertEquals(3, lv.getViewSize());
	}

	/**
	 * @throws IOException
	 */
	public void testListViewNewItem() throws IOException
	{
		ListView lv = createListView(4);
		ListItem li = lv.newItem(0);
		assertEquals(0, li.getIndex());
		assertEquals(new Integer(0), li.getModelObject());
	}

	/**
	 * @throws IOException
	 */
	public void testEmptyListView() throws IOException
	{
		// Empty tables
		ListView lv = createListView(0);
		assertEquals(0, lv.getStartIndex());
		assertEquals(0, lv.getViewSize());

		// null tables are a special case used for table navigation
		// bar, where there is no underlying model necessary, as
		// listItem.getIndex() is equal to the required listItem.getModelObject()
		lv = new ListView("listView", new Model(null))
		{
			protected void populateItem(final ListItem listItem)
			{
				; // do nothing
			}
		};
		assertEquals(0, lv.getStartIndex());
		assertEquals(0, lv.getViewSize());

		lv.setStartIndex(5);
		lv.setViewSize(10);
		assertEquals(5, lv.getStartIndex());
		assertEquals(10, lv.getViewSize());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5294.java