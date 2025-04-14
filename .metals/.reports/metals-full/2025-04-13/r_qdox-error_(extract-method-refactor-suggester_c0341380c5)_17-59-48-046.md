error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9715.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9715.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9715.java
text:
```scala
s@@uper(mountPath, bookmarkablePageClass, PageMap.DEFAULT_NAME);

package wicket.request.target.coding;

import wicket.PageMap;
import wicket.PageParameters;
import wicket.WicketRuntimeException;
import wicket.request.target.coding.BookmarkablePageRequestTargetUrlCodingStrategy;

/**
 * Url coding strategy for bookmarkable pages that encodes index based
 * parameters.
 * 
 * Strategy looks for parameters whose name is an integer in an incremented
 * order starting with zero. Found parameters will be appended to the url in the
 * form /mount-path/paramvalue0/paramvalue1/paramvalue2
 * 
 * When decoded these parameters will once again be available under their index (
 * PageParameters.getString("0"); )
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
public class IndexedParamUrlCodingStrategy extends BookmarkablePageRequestTargetUrlCodingStrategy
{

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            mount path
	 * @param bookmarkablePageClass
	 *            class of mounted page
	 */
	public IndexedParamUrlCodingStrategy(String mountPath, Class bookmarkablePageClass)
	{
		super(mountPath, bookmarkablePageClass, PageMap.DEFAULT_PAGEMAP_NAME);
	}

	/**
	 * Construct.
	 * 
	 * @param mountPath
	 *            mount path
	 * @param bookmarkablePageClass
	 *            class of mounted page
	 * @param pageMapName
	 *            name of pagemap
	 */
	public IndexedParamUrlCodingStrategy(String mountPath, Class bookmarkablePageClass,
			String pageMapName)
	{
		super(mountPath, bookmarkablePageClass, pageMapName);
	}

	protected void appendPageParameters(StringBuffer url, PageParameters parameters)
	{
		int i = 0;
		while (parameters.containsKey(String.valueOf(i)))
		{
			String value = parameters.getString(String.valueOf(i));
			url.append("/").append(urlEncode(value));
			i++;
		}

		if (i != parameters.size())
		{
			throw new WicketRuntimeException(
					"Not all parameters were encoded. Make sure all parameter names are integers in consecutive order starting with zero. Current parameter names are: "
							+ parameters.keySet().toString());
		}
	}

	protected PageParameters decodePageParameters(String urlFragment)
	{
		PageParameters params = new PageParameters();
		if (urlFragment == null)
		{
			return params;
		}
		if (urlFragment.startsWith("/"))
		{
			urlFragment = urlFragment.substring(1);
		}

		String[] parts = urlFragment.split("/");
		for (int i = 0; i < parts.length; i++)
		{
			params.put(String.valueOf(i), parts[i]);
		}
		return params;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9715.java