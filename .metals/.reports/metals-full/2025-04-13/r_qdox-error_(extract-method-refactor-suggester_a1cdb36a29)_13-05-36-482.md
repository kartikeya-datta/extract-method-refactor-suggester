error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16312.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16312.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16312.java
text:
```scala
P@@ageableListView<String> listview = new PageableListView<String>(datacontainer, "rows", Arrays

package wicket.examples.ajax.builtin;

import java.util.Arrays;

import wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;

/**
 * Shows an ajaxian paging navigator in action.
 * 
 * @author Martijn Dashorst
 */
public class PageablesPage extends BasePage
{
	private static final String[] names = { "Doe, John", "Presley, Elvis", "Presly, Priscilla",
			"John, Elton", "Jackson, Michael", "Bush, George", "Baker, George",
			"Stallone, Sylvester", "Murphy, Eddie", "Potter, Harry", "Balkenende, Jan Peter",
			"Two Shoes, Goody", "Goodman, John", "Candy, John", "Belushi, James",
			"Jones, James Earl", "Kelly, Grace", "Osborne, Kelly", "Cartman", "Kenny",
			"Schwarzenegger, Arnold", "Pitt, Brad", "Richie, Nicole", "Richards, Denise",
			"Sheen, Charlie", "Sheen, Martin", "Esteves, Emilio", "Baldwin, Alec",
			"Knowles, Beyonce", "Affleck, Ben", "Lavigne, Avril", "Cuthbert, Elisha",
			"Longoria, Eva", "Clinton, Bill", "Willis, Bruce", "Farrell, Colin",
			"Hasselhoff, David", "Moore, Demi", };

	/**
	 * Constructor.
	 */
	public PageablesPage()
	{
		WebMarkupContainer datacontainer = new WebMarkupContainer(this, "data");
		datacontainer.setOutputMarkupId(true);

		PageableListView listview = new PageableListView(datacontainer, "rows", Arrays
				.asList(names), 10)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				new Label(item, "name", item.getModelObjectAsString());
			}
		};

		new AjaxPagingNavigator(datacontainer, "navigator", listview);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16312.java