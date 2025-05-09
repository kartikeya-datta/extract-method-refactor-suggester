error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/329.java
text:
```scala
p@@ublic class RedirectPage extends WebPage

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup.html.pages;

import wicket.AttributeModifier;
import wicket.IRedirectListener;
import wicket.Page;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;
import wicket.model.Model;

/**
 * Page that let the browser redirect. Use this if you want to direct the
 * browser to some external URL, like Google etc. or if you want to redirect to
 * a Wicket page, but with a delay.
 * 
 * @author Eelco Hillenius
 */
public final class RedirectPage extends WebPage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. The page will immediately redirect to the given url.
	 * 
	 * @param url
	 *            The url to redirect to
	 */
	public RedirectPage(final CharSequence url)
	{
		this(url, 0);
	}

	/**
	 * Constructor. The page will redirect to the given url after waiting for
	 * the given number of seconds.
	 * 
	 * @param url
	 *            The url to redirect to
	 * @param waitBeforeRedirectInSeconds
	 *            The number of seconds the browser should wait before
	 *            redirecting
	 */
	public RedirectPage(final CharSequence url, final int waitBeforeRedirectInSeconds)
	{
		final WebMarkupContainer redirect = new WebMarkupContainer(this, "redirect");
		final String content = waitBeforeRedirectInSeconds + ";URL=" + url;
		redirect.add(new AttributeModifier("content", new Model<String>(content)));
	}

	/**
	 * Construct. The page will redirect to the given Page.
	 * 
	 * @param page
	 *            The page to redirect to.
	 */
	public RedirectPage(final Page page)
	{
		this(page.urlFor(IRedirectListener.INTERFACE), 0);
	}

	/**
	 * Construct. The page will redirect to the given Page after waiting for the
	 * given number of seconds.
	 * 
	 * @param page
	 *            The page to redirect to.
	 * @param waitBeforeRedirectInSeconds
	 *            The number of seconds the browser should wait before
	 *            redirecting
	 */
	public RedirectPage(final Page page, final int waitBeforeRedirectInSeconds)
	{
		this(page.urlFor(IRedirectListener.INTERFACE), waitBeforeRedirectInSeconds);
	}

	/**
	 * @see wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/329.java