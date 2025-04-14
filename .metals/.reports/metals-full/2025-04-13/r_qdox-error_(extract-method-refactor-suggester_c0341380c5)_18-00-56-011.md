error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9779.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9779.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9779.java
text:
```scala
final C@@lass<? extends Page> c)

/*
 * $Id: ImageMap.java 5231 2006-04-01 15:34:49 -0800 (Sat, 01 Apr 2006) joco01 $
 * $Revision: 1.9 $ $Date: 2006-04-01 15:34:49 -0800 (Sat, 01 Apr 2006) $
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
import wicket.PageMap;
import wicket.RequestCycle;
import wicket.Session;
import wicket.markup.ComponentTag;
import wicket.markup.html.WebMarkupContainer;
import wicket.util.string.Strings;

/**
 * Implementation of an internal frame component. Must be used with an iframe
 * (&lt;iframe src...) element. The src attribute will be generated.
 * 
 * @author Sven Meier
 * @author Ralf Ebert
 */
public class InternalFrame extends WebMarkupContainer implements ILinkListener
{
	private static final long serialVersionUID = 1L;

	/** The link. */
	private final IPageLink pageLink;

	/**
	 * The pagemap name where the page that will be created by this internal
	 * frame will be created in.
	 */
	private final String pageMapName;

	/**
	 * Constructs an internal frame that instantiates the given Page class when
	 * the content of the internal frame is requested. The instantiated Page is
	 * used to render a response to the user.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param pageMap
	 *            the pagemap where the page of the internal frame must be in
	 * @param c
	 *            Page class
	 */
	public InternalFrame(MarkupContainer parent, final String id, final PageMap pageMap,
			final Class c)
	{
		this(parent, id, pageMap, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				// Create page using page factory
				return Session.get().getPageFactory().newPage(c);
			}

			public Class getPageIdentity()
			{
				return c;
			}
		});

		// Ensure that c is a subclass of Page
		if (!Page.class.isAssignableFrom(c))
		{
			throw new IllegalArgumentException("Class " + c + " is not a subclass of Page");
		}
	}

	/**
	 * This constructor is ideal if a Page object was passed in from a previous
	 * Page. Construct an internal frame containing the given Page.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See component
	 * @param pageMap
	 *            the pagemap where the page of the internal frame must be in
	 * @param page
	 *            The page
	 */
	public InternalFrame(MarkupContainer parent, final String id, final PageMap pageMap,
			final Page page)
	{
		this(parent, id, pageMap, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			public Page getPage()
			{
				// use given page
				return page;
			}

			public Class getPageIdentity()
			{
				return page.getClass();
			}
		});
	}

	/**
	 * This constructor is ideal for constructing pages lazily.
	 * 
	 * Constructs an internal frame which invokes the getPage() method of the
	 * IPageLink interface when the content of the internal frame is requested.
	 * Whatever Page objects is returned by this method will be rendered back to
	 * the user.
	 * 
	 * @param parent
	 *            The parent of this component
	 * 
	 * @param id
	 *            See Component
	 * @param pageMap
	 *            the pagemap where the page of the internal frame must be in
	 * @param pageLink
	 *            An implementation of IPageLink which will create the page to
	 *            be contained in the internal frame if and when the content is
	 *            requested
	 */
	public InternalFrame(MarkupContainer parent, final String id, final PageMap pageMap,
			IPageLink pageLink)
	{
		super(parent, id);

		this.pageMapName = pageMap.getName();

		this.pageLink = pageLink;
	}

	/**
	 * Gets the url to use for this link.
	 * 
	 * @return The URL that this link links to
	 */
	protected CharSequence getURL()
	{
		return urlFor(ILinkListener.INTERFACE);
	}

	/**
	 * Handles this frame's tag.
	 * 
	 * @param tag
	 *            the component tag
	 * @see wicket.Component#onComponentTag(ComponentTag)
	 */
	@Override
	protected final void onComponentTag(final ComponentTag tag)
	{
		checkComponentTag(tag, "iframe");

		// Set href to link to this frame's frameRequested method
		CharSequence url = getURL();

		// generate the src attribute
		tag.put("src", Strings.replaceAll(url, "&", "&amp;"));

		super.onComponentTag(tag);
	}

	/**
	 * @see wicket.markup.html.link.ILinkListener#onLinkClicked()
	 */
	public final void onLinkClicked()
	{
		RequestCycle.get().getRequest().getRequestParameters().setPageMapName(pageMapName);

		setResponsePage(pageLink.getPage());
	}

	/**
	 * Returns the pageMap.
	 * 
	 * @return pageMap
	 */
	public final PageMap getPageMap()
	{
		return PageMap.forName(this.pageMapName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9779.java