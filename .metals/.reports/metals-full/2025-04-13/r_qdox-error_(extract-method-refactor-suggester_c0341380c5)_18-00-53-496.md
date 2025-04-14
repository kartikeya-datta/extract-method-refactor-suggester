error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9955.java
text:
```scala
final M@@arkupParser parser = new MarkupParser(new XmlPullParser())

package wicket.markup;

import wicket.Application;
import wicket.markup.parser.IMarkupFilter;
import wicket.markup.parser.XmlPullParser;
import wicket.markup.parser.filter.PrependContextPathHandler;

/**
 * Default implementation of IMarkupParserFactory
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class MarkupParserFactory implements IMarkupParserFactory
{
	private IMarkupFilter[] filters;
	private Application application;

	/**
	 * Construct.
	 * 
	 * @param application
	 *            Application settings necessary to configure the parser
	 */
	public MarkupParserFactory(final Application application)
	{
		this.application = application;
		this.filters = new IMarkupFilter[] { new PrependContextPathHandler(application) };
	}

	/**
	 * Construct.
	 * 
	 * @param application
	 *            Application settings necessary to configure the parser
	 * @param filters
	 *            additional markup filters
	 */
	public MarkupParserFactory(final Application application, IMarkupFilter[] filters)
	{
		this.application = application;
		this.filters = new IMarkupFilter[filters.length+1];
		System.arraycopy(filters, 0, this.filters, 0, filters.length);
		this.filters[filters.length] = new PrependContextPathHandler(application);
	}

	/**
	 * Construct.
	 * 
	 * @param application
	 *            Application settings necessary to configure the parser
	 * @param filter
	 *            additional markup filter
	 */
	public MarkupParserFactory(final Application application, IMarkupFilter filter)
	{
		this.application = application;
		this.filters = new IMarkupFilter[] { filter, new PrependContextPathHandler(application) };
	}

	/**
	 * @see wicket.markup.IMarkupParserFactory#newMarkupParser()
	 */
	public MarkupParser newMarkupParser()
	{
		final MarkupParser parser = new MarkupParser(application, new XmlPullParser())
		{
			public void initFilterChain()
			{
				if (filters != null)
				{
					for (int i = 0; i < filters.length; i++)
					{
						appendMarkupFilter(filters[i]);
					}
				}
			}
		};
		return parser;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9955.java