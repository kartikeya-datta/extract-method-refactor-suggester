error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8567.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8567.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8567.java
text:
```scala
public C@@lass getPageIdentity();

/*
 * $Id$ $Revision:
 * 1.7 $ $Date$
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
package wicket.markup.html.link;

import java.io.Serializable;

import wicket.Page;

/**
 * Interface that is used to implement delayed page linking. The getPage()
 * method returns an instance of Page when a link is actually clicked (thus
 * avoiding the need to create a destination Page object for every link on a
 * given Page in advance). The getIdentity() method returns the subclass of Page
 * that getPage() will return if and when it is called.
 * <p>
 * This way of arranging things is useful in determining whether a link links to
 * a given page, which is in turn useful for deciding how to display the link
 * (because links in a navigation which link to a page itself are not useful and
 * generally should instead indicate where the user is in the navigation).
 * <p>
 * To understand how getIdentity() is used in this way, take a look at the
 * Link.linksTo() method and its override in PageLink. Also, see the
 * documentation for getIdentity() below.
 * 
 * @see Link#linksTo(Page)
 * @see PageLink#linksTo(Page)
 * @author Jonathan Locke
 */
public interface IPageLink extends Serializable
{
	/**
	 * Gets the page to go to.
	 * 
	 * @return The page to go to.
	 */
	public Page getPage();

	/**
	 * Gets the class of the destination page, which serves as a form of
	 * identity that can be used at rendering time to determine if a link is on
	 * the same Page that it links to (except when pages are parameterized. in
	 * that case, see Link.linksTo() for details).
	 * <p>
	 * A page's identity is important because links which are on the same
	 * page that they link to often need to be displayed in a different way to
	 * indicate that they are 'disabled' and don't go anywhere. Links can be
	 * manually disabled by calling Link.setDisabled(). Links which have
	 * setAutoEnable(true) will automatically enable or disable themselves
	 * depending on whether or not Link.linksTo() returns true. The default
	 * implementation of PageLink.linksTo() therefore looks like this:
	 * 
	 * <pre>
	 * private final IPageLink pageLink;
	 * 
	 * public boolean linksTo(final Page page)
	 * {
	 * 	return page.getClass() == pageLink.getDestinationIdentity();
	 * }
	 * </pre>
	 * 
	 * @return The class of page linked to, as a form of identity
	 * @see Link#linksTo(Page)
	 */
	public Class getIdentity();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8567.java