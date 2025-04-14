error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3924.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3924.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3924.java
text:
```scala
public final v@@oid setPageTitle(final String title)

/*
 * $Id$ $Revision$ $Date$
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
package wicket.examples.template;

import wicket.examples.WicketExamplePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.Link;
import wicket.model.PropertyModel;

/**
 * Our base page that serves as a template for pages that inherit from it.
 * Doesn't have to be abstract, but was made abstract here to stress the fact
 * that this page is not meant for direct use.
 * <p>
 * Alternatively, instead of creating new instances of components whenever we
 * want to replace others (Banner1/ Banner2), we can re-use components. This
 * class could be re-written like this:
 * 
 * <pre>
 *       public abstract class TemplatePage extends WicketExamplePage
 *       {
 *              private String pageTitle = &quot;(no title)&quot;;
 *       
 *              private Banner currentBanner;
 *       
 *              private Banner banner1;
 *       
 *              private Banner banner2;
 *       
 *              public TemplatePage()
 *              {
 *                      new Label(this, &quot;title&quot;, new PropertyModel(this, &quot;pageTitle&quot;));
 *                      banner2 = new Banner2(this, &quot;ad&quot;);
 *                      currentBanner = banner1 = new Banner1(this, &quot;ad&quot;);
 *       
 *                     new Link(this, &quot;changeAdLink&quot;)
 *                     {
 *                              public void onClick()
 *                              {
 *                                      if (currentBanner == banner1)
 *                                      {
 *                                              currentBanner = banner2;
 *                                              banner2.reAttach();
 *                                      }
 *                                      else
 *                                      {
 *                                              currentBanner = banner1;
 *                                              banner1.reAttach();
 *                                      }
 *                              }
 *                      };
 *       ...
 * </pre>
 * 
 * </p>
 * 
 * @author Eelco Hillenius
 */
public abstract class TemplatePage extends WicketExamplePage
{
	/** title of the current page. */
	private String pageTitle = "(no title)";

	/** the current banner. */
	private Banner currentBanner;

	/**
	 * Constructor
	 */
	public TemplatePage()
	{
		new Label(this, "title", new PropertyModel(this, "pageTitle"));
		currentBanner = new Banner1(this, "ad");
		new Link(this, "changeAdLink")
		{
			/**
			 * @see wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick()
			{
				if (currentBanner.getClass() == Banner1.class)
				{
					// we already had a component directly under the page with
					// id 'ad'. Creating a new one like this (same id, same
					// hierarchy position) will have the effect that this new
					// component will be set as the current one, and thus will
					// be rendered instead of the previous child. In Wicket
					// pre 2.0 (before you had to pass in the parent in the
					// constructor to create the hierarchy, and had to use
					// Component#add instead) you achieved the same by calling
					// Component#replace. Now you either construct a new
					// component with the same parent and same id, or - if
					// you have a reference to a component that was previously
					// created with that parent - you call Component#reAttach
					// to set that component as the current one.
					new Banner2(TemplatePage.this, "ad");
				}
				else
				{
					new Banner1(TemplatePage.this, "ad");
				}
			}
		};
		new BookmarkablePageLink(this, "page1Link", Page1.class);
		new BookmarkablePageLink(this, "page2Link", Page2.class);
	}

	/**
	 * Gets the title.
	 * 
	 * @return title
	 */
	public final String getPageTitle()
	{
		return pageTitle;
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            title
	 */
	public final void setPageTitle(String title)
	{
		this.pageTitle = title;
	}

	/**
	 * @see wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		// TODO Bug: Versioning gives problems... probably has to do with markup
		// inheritance
		return false;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3924.java