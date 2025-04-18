error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9855.java
text:
```scala
i@@f (webPage.isPageStateless())

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
package org.apache.wicket.markup.html;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.IPageMap;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.parser.filter.HtmlHeaderSectionHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressingWebRequestProcessor;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressor;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget;
import org.apache.wicket.response.StringResponse;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.util.string.JavascriptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for HTML pages. This subclass of Page simply returns HTML when asked for its markup
 * type. It also has a method which subclasses can use to retrieve a bookmarkable link to the
 * application's home page.
 * <p>
 * WebPages can be constructed with any constructor when they are being used in a Wicket session,
 * but if you wish to link to a Page using a URL that is "bookmarkable" (which implies that the URL
 * will not have any session information encoded in it, and that you can call this page directly
 * without having a session first directly from your browser), you need to implement your Page with
 * a no-arg constructor or with a constructor that accepts a PageParameters argument (which wraps
 * any query string parameters for a request). In case the page has both constructors, the
 * constructor with PageParameters will be used.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Juergen Donnerstag
 * @author Gwyn Evans
 */
public class WebPage extends Page implements INewBrowserWindowListener
{
	/**
	 * Tries to determine whether this page was opened in a new window or tab. If it is (and this
	 * checker were able to recognize that), a new page map is created for this page instance, so
	 * that it will start using it's own history in sync with the browser window or tab.
	 */
	private static final class PageMapChecker extends AbstractBehavior
		implements
			IHeaderContributor
	{
		private static final long serialVersionUID = 1L;

		private final WebPage webPage;

		/**
		 * Construct.
		 * 
		 * @param webPage
		 */
		PageMapChecker(WebPage webPage)
		{
			this.webPage = webPage;
		}

		/**
		 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(IHeaderResponse)
		 */
		public final void renderHead(final IHeaderResponse headResponse)
		{
			Response response = headResponse.getResponse();
			final WebRequestCycle cycle = (WebRequestCycle)RequestCycle.get();
			final IRequestTarget target = cycle.getRequestTarget();

			// we don't want to render this for stateless pages
			if (webPage.getStatelessHint())
			{
				return;
			}

			IPageMap pageMap = webPage.getPageMap();
			String name = pageMap.getName();
			if (name == null)
			{
				name = "wicket:default";
			}
			else
			{
				name = name.replace('"', '_');
			}

			Session session = Session.get();

			Session.PageMapAccessMetaData meta = (Session.PageMapAccessMetaData)session.getMetaData(Session.PAGEMAP_ACCESS_MDK);
			if (meta == null)
			{
				meta = new Session.PageMapAccessMetaData();
				session.setMetaData(Session.PAGEMAP_ACCESS_MDK, meta);
			}
			boolean firstAccess = meta.add(pageMap);

			if (firstAccess)
			{
				// this is the first access to the pagemap, set window.name
				JavascriptUtils.writeOpenTag(response);
				response.write("if (window.name=='' || window.name.indexOf('wicket') > -1) { window.name=\"");
				response.write("wicket-" + name);
				response.write("\"; }");
				JavascriptUtils.writeCloseTag(response);
			}
			else
			{
				// Here is our trickery to detect whether the current request
				// was made in a new window/ tab, in which case it should go in
				// a different page map so that we don't intermingle the history
				// of those windows
				CharSequence url = null;
				if (target instanceof IBookmarkablePageRequestTarget)
				{
					IBookmarkablePageRequestTarget current = (IBookmarkablePageRequestTarget)target;
					BookmarkablePageRequestTarget redirect = new BookmarkablePageRequestTarget(
						session.createAutoPageMapName(), current.getPageClass(),
						current.getPageParameters());
					url = cycle.urlFor(redirect);
				}
				else
				{
					url = webPage.urlFor(INewBrowserWindowListener.INTERFACE);
				}
				JavascriptUtils.writeOpenTag(response);
				response.write("if (window.name=='' || (window.name.indexOf('wicket') > -1 && window.name!='" +
					"wicket-" + name + "')) { window.location=\"");
				response.write(url);
				response.write("\" + (window.location.hash != null ? window.location.hash : \"\"); }");
				JavascriptUtils.writeCloseTag(response);
			}
		}
	}

	/** log. */
	private static final Logger log = LoggerFactory.getLogger(WebPage.class);

	/** The resource references used for new window/tab support */
	private static ResourceReference cookiesResource = new ResourceReference(WebPage.class,
		"cookies.js");

	private static final long serialVersionUID = 1L;

	/**
	 * The url compressor that will compress the urls by collapsing the component path and listener
	 * interface
	 */
	private UrlCompressor compressor;

	/**
	 * Constructor. Having this constructor public means that your page is 'bookmarkable' and hence
	 * can be called/ created from anywhere.
	 */
	protected WebPage()
	{
		commonInit();
	}

	/**
	 * @see Page#Page(IModel)
	 */
	protected WebPage(final IModel model)
	{
		super(model);
		commonInit();
	}

	/**
	 * @see Page#Page(org.apache.wicket.IPageMap)
	 */
	protected WebPage(final IPageMap pageMap)
	{
		super(pageMap);
		commonInit();
	}

	/**
	 * @see Page#Page(org.apache.wicket.IPageMap, org.apache.wicket.model.IModel)
	 */
	protected WebPage(final IPageMap pageMap, final IModel model)
	{
		super(pageMap, model);
		commonInit();
	}

	/**
	 * Constructor which receives wrapped query string parameters for a request. Having this
	 * constructor public means that your page is 'bookmarkable' and hence can be called/ created
	 * from anywhere. For bookmarkable pages (as opposed to when you construct page instances
	 * yourself, this constructor will be used in preference to a no-arg constructor, if both exist.
	 * Note that nothing is done with the page parameters argument. This constructor is provided so
	 * that tools such as IDEs will include it their list of suggested constructors for derived
	 * classes.
	 * 
	 * Please call this constructor (or the one with the pagemap) if you want to remember the
	 * pageparameters {@link #getPageParameters()}. So that they are reused for stateless links.
	 * 
	 * @param parameters
	 *            Wrapped query string parameters.
	 */
	protected WebPage(final PageParameters parameters)
	{
		super(parameters);
		commonInit();
	}

	/**
	 * Constructor which receives wrapped query string parameters for a request. Having this
	 * constructor public means that your page is 'bookmarkable' and hence can be called/ created
	 * from anywhere. For bookmarkable pages (as opposed to when you construct page instances
	 * yourself, this constructor will be used in preference to a no-arg constructor, if both exist.
	 * Note that nothing is done with the page parameters argument. This constructor is provided so
	 * that tools such as IDEs will include it their list of suggested constructors for derived
	 * classes.
	 * 
	 * Please call this constructor (or the one without the pagemap) if you want to remember the
	 * pageparameters {@link #getPageParameters()}. So that they are reused for stateless links.
	 * 
	 * @param pageMap
	 *            The pagemap where the webpage needs to be constructed in.
	 * @param parameters
	 *            Wrapped query string parameters.
	 */
	protected WebPage(final IPageMap pageMap, final PageParameters parameters)
	{
		super(pageMap, parameters);
		commonInit();
	}

	/**
	 * Gets the markup type for a WebPage, which is "html" by default. Support for pages in another
	 * markup language, such as VXML, would require the creation of a different Page subclass in an
	 * appropriate package under org.apache.wicket.markup. To support VXML (voice markup), one might
	 * create the package org.apache.wicket.markup.vxml and a subclass of Page called VoicePage.
	 * 
	 * @return Markup type for HTML
	 */
	public String getMarkupType()
	{
		return "html";
	}

	/**
	 * This method is called when the compressing coding and response strategies are configured in
	 * your Application object like this:
	 * 
	 * <pre>
	 * protected IRequestCycleProcessor newRequestCycleProcessor()
	 * {
	 * 	return new UrlCompressingWebRequestProcessor();
	 * }
	 * </pre>
	 * 
	 * @return The URLCompressor for this webpage.
	 * 
	 * @since 1.2
	 * 
	 * @see UrlCompressingWebRequestProcessor
	 * @see UrlCompressor
	 */
	public final UrlCompressor getUrlCompressor()
	{
		if (compressor == null)
		{
			compressor = new UrlCompressor();
		}
		return compressor;
	}

	/**
	 * @see org.apache.wicket.markup.html.INewBrowserWindowListener#onNewBrowserWindow()
	 */
	public void onNewBrowserWindow()
	{
		// if the browser reports a history of 0 then make a new webpage
		WebPage clonedPage = this;
		try
		{
			clonedPage = (WebPage)Objects.cloneObject(this);
		}
		catch (Exception e)
		{
			log.error("Page " + clonedPage + " couldn't be cloned to move to another pagemap", e);
		}
		final IPageMap map = getSession().createAutoPageMap();
		clonedPage.moveToPageMap(map);
		setResponsePage(clonedPage);
	}

	/**
	 * Common code executed by constructors.
	 */
	private void commonInit()
	{
		// if automatic multi window support is on, add a page checker instance
		if (getApplication().getPageSettings().getAutomaticMultiWindowSupport())
		{
			add(new PageMapChecker(this));
		}
	}

	/**
	 * @see org.apache.wicket.Page#configureResponse()
	 */
	protected void configureResponse()
	{
		super.configureResponse();

		if (getWebRequestCycle().getResponse() instanceof WebResponse)
		{
			final WebResponse response = getWebRequestCycle().getWebResponse();
			setHeaders(response);
		}
	}

	/**
	 * Subclasses can override this to set there headers when the Page is being served. By default 2
	 * headers will be set
	 * 
	 * <pre>
	 * response.setHeader(&quot;Pragma&quot;, &quot;no-cache&quot;);
	 * response.setHeader(&quot;Cache-Control&quot;, &quot;no-cache, max-age=0, must-revalidate&quot;);
	 * </pre>
	 * 
	 * So if a Page wants to control this or doesn't want to set this info it should override this
	 * method and don't call super.
	 * 
	 * @param response
	 *            The WebResponse where set(Date)Header can be called on.
	 */
	protected void setHeaders(WebResponse response)
	{
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate"); // no-store
	}

	/**
	 * @return The WebRequestCycle for this WebPage.
	 */
	protected final WebRequestCycle getWebRequestCycle()
	{
		return (WebRequestCycle)getRequestCycle();
	}


	/**
	 * Creates and returns a bookmarkable link to this application's home page.
	 * 
	 * @param id
	 *            Name of link
	 * @return Link to home page for this application
	 */
	protected final BookmarkablePageLink homePageLink(final String id)
	{
		return new BookmarkablePageLink(id, getApplication().getHomePage());
	}

	/**
	 * 
	 * @see org.apache.wicket.Component#onDetach()
	 */
	protected void onDetach()
	{
		// This code can not go into HtmlHeaderContainer as
		// header.onEndRequest() is executed inside an iterator
		// and you can only call container.remove() which
		// is != iter.remove(). And the iterator is not available
		// inside onEndRequest(). Obviously WebPage.onEndRequest()
		// is invoked outside the iterator loop.
		HtmlHeaderContainer header = (HtmlHeaderContainer)get(HtmlHeaderSectionHandler.HEADER_ID);
		if (header != null)
		{
			this.remove(header);
		}
		else if (getApplication().getConfigurationType() == Application.DEVELOPMENT)
		{
			// the markup must at least contain a <body> tag for wicket to automatically
			// create a HtmlHeaderContainer. Log an error if no header container
			// was created but any of the components or behavior want to contribute
			// something to the header.
			header = new HtmlHeaderContainer(HtmlHeaderSectionHandler.HEADER_ID);
			add(header);

			Response orgResponse = getRequestCycle().getResponse();
			try
			{
				final StringResponse response = new StringResponse();
				getRequestCycle().setResponse(response);

				// Render all header sections of all components on the page
				renderHead(header);

				// Make sure all Components interested in contributing to the header
				// and there attached behaviors are asked.
				final HtmlHeaderContainer finalHeader = header;
				visitChildren(new IVisitor()
				{
					/**
					 * @see org.apache.wicket.Component.IVisitor#component(org.apache.wicket.Component)
					 */
					public Object component(Component component)
					{
						component.renderHead(finalHeader);
						return CONTINUE_TRAVERSAL;
					}
				});
				response.close();

				if (response.getBuffer().length() > 0)
				{
					// @TODO it is not yet working properly. JDo to fix it
// log.error("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
// log.error("You probably forgot to add a <body> tag to your markup since no Header Container was
// \n" +
// "found but components where found which want to write to the <head> section.\n" +
// response.getBuffer());
// log.error("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				}
			}
			finally
			{
				this.remove(header);
				getRequestCycle().setResponse(orgResponse);
			}
		}

		super.onDetach();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9855.java