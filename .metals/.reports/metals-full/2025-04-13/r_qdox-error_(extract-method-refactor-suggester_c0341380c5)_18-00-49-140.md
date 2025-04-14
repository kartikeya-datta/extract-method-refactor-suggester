error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3877.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3877.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3877.java
text:
```scala
protected v@@oid populateItem(final ListItem<Person> item)

package wicket.examples.compref;

import java.util.ArrayList;

import wicket.examples.WicketExamplePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Check;
import wicket.markup.html.form.CheckGroup;
import wicket.markup.html.form.CheckGroupSelector;
import wicket.markup.html.form.Form;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.PropertyModel;

/**
 * CheckGroup and Check components example page
 * 
 * @author ivaynberg
 */
public class CheckGroupPage extends WicketExamplePage
{
	private final CheckGroup group;
	
	/**
	 * Constructor
	 */
	public CheckGroupPage()
	{
		Form form = new Form(this, "form")
		{
			@Override
			protected void onSubmit()
			{
				info("selected person(s): " + group.getModelObjectAsString());
			}
		};

		group = new CheckGroup<String>(form, "group", new ArrayList<String>());

		new CheckGroupSelector(group, "groupselector");
		new ListView<Person>(group, "persons", ComponentReferenceApplication.getPersons())
		{

			@Override
			protected void populateItem(ListItem<Person> item)
			{
				new Check<Person>(item, "checkbox", item.getModel());
				new Label(item, "name", new PropertyModel(item.getModel(), "name"));
				new Label(item, "lastName", new PropertyModel(item.getModel(), "lastName"));
			}

		};

		new FeedbackPanel(this, "feedback");
	}

	@Override
	protected void explain()
	{
		String html = "<form wicket:id=\"form\">\n"
				+ "<span wicket:id=\"group\">\n"
				+ "<input type=\"checkbox\" wicket:id=\"groupselector\">check/uncheck all</input>\n"
				+ "<tr wicket:id=\"persons\">\n"
				+ "<td><input type=\"checkbox\" wicket:id=\"checkbox\"/></td>\n"
				+ "<td><span wicket:id=\"name\">[this is where name will be]</span></td>\n"
				+ "<td><span wicket:id=\"lastName\">[this is where lastname will be]</span></td>\n"
				+ "</tr>\n</span>\n</form>";
		String code = "&nbsp;&nbsp;&nbsp;&nbsp;Form f=new Form(\"form\");<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;add(f);<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;CheckGroup group=new CheckGroup(\"group\");<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;form.add(group);<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;group.add(new CheckGroupSelector(\"groupselector\"));<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;ListView persons=new ListView(\"persons\", getPersons()) {<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;protected void populateItem(ListItem item) {<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;item.add(new Check(\"check\", item.getModel()));<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;item.add(new Label(\"name\", new PropertyModel(item.getModel(), \"name\")));<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;item.add(new Label(\"lastName\", new PropertyModel(item.getModel(), \"lastName\")));<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;};<br/>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;group.add(persons);<br/>";
		new ExplainPanel(this, html, code);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3877.java