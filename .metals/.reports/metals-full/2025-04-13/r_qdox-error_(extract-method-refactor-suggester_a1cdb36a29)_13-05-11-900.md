error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8876.java
text:
```scala
w@@rite(container, "\t<link rel='stylesheet' type='text/css' href='" + urlFor(ref.getPath()) + "'/>\n");

package wicket.contrib.scriptaculous.dragdrop;

import wicket.Application;
import wicket.MarkupContainer;
import wicket.contrib.scriptaculous.Scriptaculous;
import wicket.contrib.scriptaculous.autocomplete.AutocompleteTextFieldSupport;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.IHeaderContributor;
import wicket.markup.html.PackageResourceReference;
import wicket.markup.html.image.Image;
import wicket.markup.html.internal.HtmlHeaderContainer;

public class DraggableImage extends Image implements IHeaderContributor
{
	private final String id;

	public DraggableImage(String wicketId, String id, String img)
	{
		super(wicketId, img);
		this.id = id;
	}

	protected void onRender(MarkupStream markupStream)
	{
		super.onRender(markupStream);

		getResponse().write("\n<script type='text/javascript'>new Draggable('");
		getResponse().write(id);
		getResponse().write("', {revert:true})</script>");
	}

	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		
		tag.put("id", id);
		tag.put("class", getId());
	}

	public void renderHead(HtmlHeaderContainer container)
	{
		Scriptaculous.get().renderHead(container);
		addCssReference(container, AutocompleteTextFieldSupport.class, "style.css");
	}

	private void addCssReference(final HtmlHeaderContainer container, final Class clazz, final String name)
	{
		PackageResourceReference ref = new PackageResourceReference(Application.get(), clazz, name);
		write(container, "\t<link rel='stylesheet' type='text/css' href='" + getPage().urlFor(ref.getPath()) + "'/>\n");
	}
	
	/**
	 * Writes the given string to the header container.
	 * 
	 * @param container
	 *            the header container
	 * @param s
	 *            the string to write
	 */
	protected void write(MarkupContainer container, String s)
	{
		container.getResponse().write(s);
	}

	public String getBodyOnLoad()
	{
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8876.java