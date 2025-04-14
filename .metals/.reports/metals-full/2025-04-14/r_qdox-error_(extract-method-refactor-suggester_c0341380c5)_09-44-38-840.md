error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3891.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3891.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3891.java
text:
```scala
protected v@@oid onSelectionChanged(final Object newSelection)

/*
 * $Id: ExamplePaging.java 5838 2006-05-24 20:44:49 +0000 (Wed, 24 May 2006)
 * joco01 $ $Revision$ $Date: 2006-05-24 20:44:49 +0000 (Wed, 24 May
 * 2006) $
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
package wicket.examples.displaytag;

import java.util.ArrayList;
import java.util.List;

import wicket.PageParameters;
import wicket.examples.displaytag.utils.ListObject;
import wicket.examples.displaytag.utils.MyPageableListViewNavigator;
import wicket.examples.displaytag.utils.SimplePageableListView;
import wicket.examples.displaytag.utils.TestList;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.list.ListItem;
import wicket.model.Model;

/**
 * Table with paging
 * 
 * @author Juergen Donnerstag
 */
public class ExamplePaging extends Displaytag
{
	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public ExamplePaging(final PageParameters parameters)
	{
		// Test data
		final List<ListObject> data = new TestList(55, false);

		// =======================================================================
		// Add pageable table with alternating row styles. A Label component
		// is automatically created for each list item.
		// - The list contains more items than the page
		final SimplePageableListView<ListObject> table = new SimplePageableListView<ListObject>(this, "rows", data, 10);

		new MyPageableListViewNavigator(this, "pageTableNav", table);

		// =======================================================================
		// Add pageable table with alternating row styles
		// - The list contains less items than the page
		// - Explicitly create a Label
		List<ListObject> data2 = new ArrayList<ListObject>();
		data2.addAll(data.subList(0, 10));
		final SimplePageableListView<ListObject> table2 = new SimplePageableListView<ListObject>(this, "rows2", data2, 20)
		{
			@Override
			public void populateItem(final ListItem listItem)
			{
				super.populateItem(listItem);

				final ListObject value = (ListObject)listItem.getModelObject();
				new Label(listItem, "comments", value.getDescription());
			}
		};

		new MyPageableListViewNavigator(this, "pageTableNav2", table2);

		// =======================================================================
		// Empty table
		List<ListObject> data4 = new ArrayList<ListObject>();
		final SimplePageableListView<ListObject> table4 = new SimplePageableListView<ListObject>(this, "rows4", data4, 10);
		new MyPageableListViewNavigator(this, "pageTableNav4", table4);

		// =======================================================================
		new Label(this, "info5", "");

		final List<String> addRemoveOptions = new ArrayList<String>();
		addRemoveOptions.add("10");
		addRemoveOptions.add("5");
		addRemoveOptions.add("3");
		addRemoveOptions.add("2");
		addRemoveOptions.add("1");
		addRemoveOptions.add("-1");
		addRemoveOptions.add("-2");
		addRemoveOptions.add("-3");
		addRemoveOptions.add("-5");
		addRemoveOptions.add("-10");

		new DropDownChoice<String>(this, "addRemove", new Model<String>(null), addRemoveOptions)
		{
			@Override
			protected boolean wantOnSelectionChangedNotifications()
			{
				return true;
			}

			@Override
			protected void onSelectionChanged(Object newSelection)
			{
				int anz = Integer.parseInt((String)newSelection);

				Label info5 = (Label)ExamplePaging.this.get("info5");

				List<ListObject> data5 = (List)ExamplePaging.this.get("rows5").getModelObject();
				if (anz > 0)
				{
					data5.addAll(data.subList(0, anz));
					info5.setModelObject("" + anz + " elements add to the list");
				}
				else if (anz < 0)
				{
					anz = data5.size() + anz;
					if (anz < 0)
					{
						anz = 0;
					}

					info5.setModelObject("" + (data5.size() - anz)
							+ " elements removed from the list");

					while (data5.size() > anz)
					{
						data5.remove(0);
					}
				}
			}
		};

		List<ListObject> data5 = new ArrayList<ListObject>();
		final SimplePageableListView<ListObject> table5 = new SimplePageableListView<ListObject>(this, "rows5", data5, 4);
		new MyPageableListViewNavigator(this, "pageTableNav5", table5);
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3891.java