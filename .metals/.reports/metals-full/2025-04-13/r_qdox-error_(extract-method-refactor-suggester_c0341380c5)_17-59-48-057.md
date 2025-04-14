error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4825.java
text:
```scala
final P@@age p = RequestCycle.get().getRequest().getPage();

/*
 * $Id: RestartResponseAtInterceptPageException.java,v 1.10 2006/02/13 00:16:32
 * jonathanlocke Exp $ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket;

/**
 * Causes Wicket to interrupt current request processing and immediately
 * redirect to an intercept page.
 * <p>
 * Similar to calling redirectToInteceptPage(Page) with the difference that this
 * exception will interrupt processing of the current request.
 * 
 * @see wicket.PageMap#redirectToInterceptPage(Page)
 * @see wicket.Component#redirectToInterceptPage(Page)
 * 
 * @author Igor Vaynberg (ivaynberg)
 * @author Jonathan Locke
 * @author Philip Chapman
 */
public class RestartResponseAtInterceptPageException extends AbstractRestartResponseException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Redirects to the specified intercept page
	 * 
	 * @param interceptPage
	 *            redirect page
	 */
	public RestartResponseAtInterceptPageException(final Page interceptPage)
	{
		if (interceptPage == null)
		{
			throw new IllegalStateException("Argument interceptPage cannot be null");
		}

		redirectToInterceptPage(interceptPage);
	}

	/**
	 * Redirects to the specified intercept page
	 * 
	 * @param interceptPageClass
	 *            Class of intercept page to instantiate
	 */
	public RestartResponseAtInterceptPageException(final Class interceptPageClass)
	{
		if (interceptPageClass == null)
		{
			throw new IllegalStateException("Argument pageClass cannot be null");
		}
		redirectToInterceptPage(Session.get().getPageFactory().newPage(interceptPageClass));
	}

	/**
	 * Redirects to intercept page using the page map for the current request
	 * 
	 * @param interceptPage
	 *            The intercept page to redirect to
	 */
	private void redirectToInterceptPage(final Page interceptPage)
	{
		Page p = RequestCycle.get().getRequest().getPage();
		final PageMap pageMap;


		/*
		 * page can be null if we throw the restart response exception before
		 * any page is instantiated in user's session. if this happens we switch
		 * to the pagemap of the interceptPage
		 */

		if (p == null)
		{
			pageMap = p.getPageMap();
		}
		else
		{
			pageMap = interceptPage.getPageMap();
		}

		pageMap.redirectToInterceptPage(interceptPage);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4825.java