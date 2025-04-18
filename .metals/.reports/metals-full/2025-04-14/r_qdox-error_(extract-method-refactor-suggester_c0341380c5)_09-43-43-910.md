error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13416.java
text:
```scala
M@@arkupStream markupStream = new MarkupStream(getAssociatedMarkup(false));

/*
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
package wicket.protocol.http.portlet;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Page;
import wicket.PageMap;
import wicket.PageParameters;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupNotFoundException;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebPage;
import wicket.markup.html.internal.PortletHeaderContainer;
import wicket.markup.parser.filter.HtmlHeaderSectionHandler;
import wicket.model.IModel;

/**
 * Base class for portlet pages.
 * 
 * @param <T>
 *            The type
 * @see WebPage
 * @see Page
 * @author Janne Hietam&auml;ki
 */
public class PortletPage<T> extends Page<T>
{
	private static final long serialVersionUID = 1L;

	/** log. */
	private static final Log log = LogFactory.getLog(PortletPage.class);

	/*
	 * Current portlet mode. PortletMode is not serializable, so this is stored
	 * as a String.
	 */
	private String portletMode = PortletMode.VIEW.toString();

	/*
	 * Current window state. WindowState is not serializable, so this is stored
	 * as a String.
	 */
	private String windowState = WindowState.NORMAL.toString();

	/**
	 * @see Page#Page()
	 */
	protected PortletPage()
	{
		super();
		commonInit();
	}

	/**
	 * @see Page#Page(IModel)
	 */
	protected PortletPage(final IModel<T> model)
	{
		super(model);
	}

	/**
	 * @see Page#Page(PageMap)
	 */
	protected PortletPage(final PageMap pageMap)
	{
		super(pageMap);
		commonInit();
	}

	/**
	 * @see Page#Page(PageMap, IModel)
	 */
	protected PortletPage(final PageMap pageMap, final IModel<T> model)
	{
		super(pageMap, model);
		commonInit();
	}

	/**
	 * Constructor which receives wrapped query string parameters for a request.
	 * Having this constructor public means that your page is 'bookmarkable' and
	 * hence can be called/ created from anywhere. For bookmarkable pages (as
	 * opposed to when you construct page instances yourself, this constructor
	 * will be used in preference to a no-arg constructor, if both exist. Note
	 * that nothing is done with the page parameters argument. This constructor
	 * is provided so that tools such as IDEs will include it their list of
	 * suggested constructors for derived classes.
	 * 
	 * @param parameters
	 *            Wrapped query string parameters.
	 */
	protected PortletPage(final PageParameters parameters)
	{
		this((IModel<T>)null);
	}

	/**
	 * Markup type for portlets is always html
	 * 
	 * @return Markup type
	 */
	@Override
	public final String getMarkupType()
	{
		return "html";
	}

	/**
	 * @return The PortletRequestCycle for this PortletPage.
	 */
	protected final PortletRequestCycle getPortletRequestCycle()
	{
		return (PortletRequestCycle)getRequestCycle();
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * 
	 * @param portletMode
	 */
	public final void setPortletMode(PortletMode portletMode)
	{
		if (!portletMode.equals(this.portletMode))
		{
			this.portletMode = portletMode.toString();
			onSetPortletMode(portletMode);
		}
	}

	/**
	 * THIS METHOD IS NOT PART OF THE WICKET PUBLIC API. DO NOT CALL IT.
	 * <p>
	 * 
	 * @param windowState
	 */
	public final void setWindowState(WindowState windowState)
	{
		if (!windowState.equals(this.windowState))
		{
			this.windowState = windowState.toString();
			onSetWindowState(windowState);
		}
	}

	/**
	 * Get current portlet mode
	 * 
	 * @see javax.portlet.PortletMode
	 * @return portlet mode
	 */
	public PortletMode getPortletMode()
	{
		return new PortletMode(portletMode);
	}

	/**
	 * Get current window state
	 * 
	 * @see javax.portlet.WindowState
	 * 
	 * @return portlet window state
	 */
	public WindowState getWindowState()
	{
		return new WindowState(windowState);
	}

	/**
	 * Called when the portlet mode is changed.
	 * 
	 * @param portletMode
	 */
	protected void onSetPortletMode(PortletMode portletMode)
	{
	}

	/*
	 * Called when the window state is changed.
	 * 
	 * @param portletMode
	 */
	protected void onSetWindowState(WindowState windowState)
	{
	}
	
	/**
	 * Common code executed by constructors.
	 */
	private void commonInit()
	{
		MarkupStream markupStream = getAssociatedMarkupStream(false);
		if (markupStream == null)
		{
			throw new MarkupNotFoundException(
					"Each Page must have associated markup. Unable to find the markup file for Page: "
							+ this.toString());
		}
		
		// The <head> container. It can be accessed, replaced
		// and attribute modifiers can be attached.
		markupStream.setCurrentIndex(0);
		while (markupStream.hasMoreComponentTags())
		{
			final ComponentTag tag = markupStream.getTag();
			if (tag.isOpen() && tag.isHeadTag())
			{
				// Add a default container if the tag has the default
				// name. If the tag has a wicket:id, than the user
				// must create the component.
				if (HtmlHeaderSectionHandler.HEADER_ID.equals(tag.getId()))
				{
					new PortletHeaderContainer(this, tag.getId());
				}
				break;
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13416.java