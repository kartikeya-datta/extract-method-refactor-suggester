error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/88.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/88.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/88.java
text:
```scala
L@@ist<IComponentResolver> getComponentResolvers();

package wicket.settings;

import java.util.List;

import wicket.markup.html.WebPage;
import wicket.markup.resolver.AutoComponentResolver;
import wicket.markup.resolver.IComponentResolver;

/**
 * Interface for page related settings.
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Eelco Hillenius
 */
public interface IPageSettings
{
	/**
	 * Adds a component resolver to the list.
	 * 
	 * @param resolver
	 *            The {@link IComponentResolver} that is added
	 */
	void addComponentResolver(IComponentResolver resolver);

	/**
	 * Get the (modifiable) list of IComponentResolvers.
	 * 
	 * @see AutoComponentResolver for an example
	 * @return List of ComponentResolvers
	 */
	List getComponentResolvers();

	/**
	 * Gets whether Wicket should try to support opening multiple windows for
	 * the same session transparently. If this is true - the default setting -,
	 * Wicket tries to detect whether a new window was opened by a user (e.g. in
	 * Internet Explorer by pressing ctrl+n or ctrl+click on a link), and if it
	 * detects that, it creates a new page map for that window on the fly. As a
	 * page map represents the 'history' of one window, each window will then
	 * have their own history. If two windows would share the same page map, the
	 * non-bookmarkable links on one window could refer to stale state after
	 * working a while in the other window.
	 * <p>
	 * <strong> Currently, Wicket trying to do this is a best effort that is not
	 * completely fail safe. When the client does not support cookies, support
	 * gets tricky and incomplete. See {@link WebPage}'s internals for the
	 * implementation. </strong>
	 * </p>
	 * 
	 * @return Whether Wicket should try to support multiple windows
	 *         transparently
	 */
	boolean getAutomaticMultiWindowSupport();

	/**
	 * @return Returns the maxPageVersions.
	 */
	int getMaxPageVersions();

	/**
	 * @return Returns the pagesVersionedByDefault.
	 */
	boolean getVersionPagesByDefault();

	/**
	 * @param maxPageVersions
	 *            The maxPageVersion to set.
	 */
	void setMaxPageVersions(int maxPageVersions);

	/**
	 * @param pagesVersionedByDefault
	 *            The pagesVersionedByDefault to set.
	 */
	void setVersionPagesByDefault(boolean pagesVersionedByDefault);

	/**
	 * Sets whether Wicket should try to support opening multiple windows for
	 * the same session transparently. If this is true - the default setting -,
	 * Wicket tries to detect whether a new window was opened by a user (e.g. in
	 * Internet Explorer by pressing ctrl+n or ctrl+click on a link), and if it
	 * detects that, it creates a new page map for that window on the fly. As a
	 * page map represents the 'history' of one window, each window will then
	 * have their own history. If two windows would share the same page map, the
	 * non-bookmarkable links on one window could refer to stale state after
	 * working a while in the other window.
	 * <p>
	 * <strong> Currently, Wicket trying to do this is a best effort that is not
	 * completely fail safe. When the client does not support cookies, support
	 * gets tricky and incomplete. See {@link WebPage}'s internals for the
	 * implementation. </strong>
	 * </p>
	 * 
	 * @param automaticMultiWindowSupport
	 *            Whether Wicket should try to support multiple windows
	 *            transparently
	 */
	void setAutomaticMultiWindowSupport(boolean automaticMultiWindowSupport);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/88.java