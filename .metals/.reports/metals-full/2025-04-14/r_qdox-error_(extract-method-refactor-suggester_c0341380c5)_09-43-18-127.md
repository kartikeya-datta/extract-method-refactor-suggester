error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9780.java
text:
```scala
public P@@ageLink(MarkupContainer parent, final String id, final Class<? extends Page> c)

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.link;

import wicket.MarkupContainer;
import wicket.Page;

/**
 * Links to a given page via an object implementing the IPageLink delayed
 * linking interface. PageLinks can be constructed directly with an IPageLink
 * interface or with a Page Class object. In the latter case, an IPageLink
 * implementation is provided which constructs a Page of the given class when
 * the link is clicked. A default no-args constructor must be available in this
 * case or a WicketRuntimeException will be thrown when Wicket fails to
 * instantiate the class.
 * 
 * @see IPageLink
 * @author Jonathan Locke
 */
public class PageLink extends Link
{
	private static final long serialVersionUID = 1L;

	/** The delayed linking Page source. */
	private final IPageLink pageLink;

	/**
	 * Constructs a link that instantiates the given Page class when the link is
	 * clicked. The instantiated Page is used to render a response to the user.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param c
	 *            Page class
	 */
	public PageLink(MarkupContainer parent, final String id, final Class c)
	{
		super(parent, id);

		// Ensure that c is a subclass of Page
		if (!Page.class.isAssignableFrom(c))
		{
			throw new IllegalArgumentException("Class " + c + " is not a subclass of Page");
		}

		this.pageLink = new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				// Create page using page factory
				return PageLink.this.getPage().getPageFactory().newPage(c);
			}

			public Class getPageIdentity()
			{
				return c;
			}
		};
	}

	/**
	 * This constructor is ideal if a Page object was passed in from a previous
	 * Page. Construct a link to the Page.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See component
	 * @param page
	 *            The page
	 */
	public PageLink(MarkupContainer parent, final String id, final Page page)
	{
		super(parent, id);

		this.pageLink = new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				// Create page using page factory
				return page;
			}

			public Class getPageIdentity()
			{
				return page.getClass();
			}
		};
	}

	/**
	 * This constructor is ideal for constructing pages lazily.
	 * 
	 * Constructs a link which invokes the getPage() method of the IPageLink
	 * interface when the link is clicked. Whatever Page objects is returned by
	 * this method will be rendered back to the user.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param pageLink
	 *            An implementation of IPageLink which will create the page
	 *            linked to if and when this hyperlink is clicked at a later
	 *            time.
	 */
	public PageLink(MarkupContainer parent, final String id, final IPageLink pageLink)
	{
		super(parent, id);
		this.pageLink = pageLink;
	}

	/**
	 * Returns true if the given page is of the same class as the (delayed)
	 * destination of this page link.
	 * 
	 * @see wicket.markup.html.link.Link#linksTo(wicket.Page)
	 */
	@Override
	public boolean linksTo(final Page page)
	{
		return page.getClass() == pageLink.getPageIdentity();
	}

	/**
	 * Handles a link click by asking for a concrete Page instance through the
	 * IPageLink.getPage() delayed linking interface. This call will normally
	 * cause the destination page to be created.
	 * 
	 * @see wicket.markup.html.link.Link#onClick()
	 */
	@Override
	public void onClick()
	{
		// Set page source's page as response page
		setResponsePage(pageLink.getPage());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9780.java