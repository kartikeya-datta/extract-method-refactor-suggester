error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/431.java
text:
```scala
final S@@tring headerId = "_" + Classes.simpleName(this.getClass())

/*
 * $Id$
 * $Revision$ $Date$
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
package wicket.markup.html;

import java.util.Iterator;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Response;
import wicket.markup.ComponentTag;
import wicket.markup.Markup;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupStream;
import wicket.markup.WicketTag;
import wicket.markup.html.internal.HtmlHeaderContainer;
import wicket.markup.resolver.IComponentResolver;
import wicket.model.IModel;
import wicket.response.NullResponse;
import wicket.util.lang.Classes;

/**
 * A common base container for all components with associated markup like
 * Panels and Borders.
 * 
 * @author Juergen Donnerstag
 */
public class WebMarkupContainerWithAssociatedMarkup extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	/** True if body onLoad attribute modifiers have been attached */
	private boolean checkedBody = false;
	
	/**
	 * @see Component#Component(String)
	 */
	public WebMarkupContainerWithAssociatedMarkup(final String id)
	{
		super(id);
	}

	/**
	 * @see wicket.Component#Component(String, IModel)
	 */
	public WebMarkupContainerWithAssociatedMarkup(final String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Called by components like Panel and Border which have associated Markup
	 * and which may have a &lt;wicket:head&gt; tag.
	 * <p>
	 * Whereas 'this' might be a Panel or Border, the HtmlHeaderContainer
	 * parameter has been added to the Page as a container for all headers any
	 * of its components might wish to contribute.
	 * <p>
	 * The headers contributed are rendered in the standard way.
	 * 
	 * @param container
	 *            The HtmlHeaderContainer added to the Page
	 */
	protected final void renderHeadFromAssociatedMarkupFile(final HtmlHeaderContainer container)
	{
		// Ask the Panel/Border if it has something to contribute to the header
		final HeaderPartContainer headerPart = getHeaderPart();

		// If the panel/border component has something to contribute to
		// the header ...
		if (headerPart != null)
		{
			// A component's header section must only be added once,
			// no matter how often the same Component has been added
			// to the page or any other container in the hierachy.
			if (container.get(headerPart.getId()) == null)
			{
				container.autoAdd(headerPart);

				// Check if the Panel/Border requires some <body onload="..">
				// attribute to be copied to the page's body tag.
				if (checkedBody == false)
				{
					checkedBody = true;
					checkBodyOnLoad();
				}
			}
			else
			{
				// TODO Performance: I haven't found a more efficient solution yet
				// Already added but all the components in this header part must
				// be touched (that they are rendered)
				Response response = getRequestCycle().getResponse();
				try
				{
					getRequestCycle().setResponse(NullResponse.getInstance());
					container.autoAdd(headerPart);
				}
				finally
				{
					getRequestCycle().setResponse(response);
				}
			}
		}
	}

	/**
	 * Check if the Panel/Border requires some <body onload=".."> attribute to be
	 * copied to the page's body tag.
	 */
	private void checkBodyOnLoad()
	{
		// Gracefully getAssociateMarkupStream. Throws no exception in case
		// markup is not found
		final MarkupStream associatedMarkupStream = getApplication().getMarkupCache()
				.getMarkupStream(this, false);

		// No associated markup => no body tag
		if (associatedMarkupStream == null)
		{
			return;
		}

		// Remember the current position within markup, where we need to
		// go back to, at the end.
		int index = associatedMarkupStream.getCurrentIndex();

		try
		{
			final Iterator iter = associatedMarkupStream.componentTagIterator();
			while (iter.hasNext())
			{
				final ComponentTag tag = (ComponentTag)iter.next();
				if ("body".equalsIgnoreCase(tag.getName()))
				{
					final String onLoad = tag.getAttributes().getString("onload");
					if (onLoad != null)
					{
						// Attach an AttributeModifier to the body container
						// which appends the new value to the onLoad
						// attribute
						getWebPage().getBodyContainer().addOnLoadModifier(onLoad);
					}

					// There can only be one body tag
					break;
				}
			}
		}
		finally
		{
			// Make sure we return to the orginal position in the markup
			associatedMarkupStream.setCurrentIndex(index);
		}
	}

	/**
	 * Gets the header part of the Panel/Border. Returns null if it doesn't
	 * have a header tag.
	 * 
	 * @return the header part for this panel/border or null if it doesn't
	 *         have a wicket:head tag.
	 */
	private final HeaderPartContainer getHeaderPart()
	{
		// Gracefully getAssociateMarkupStream. Throws no exception in case
		// markup is not found
		final MarkupStream associatedMarkupStream = getApplication().getMarkupCache()
				.getMarkupStream(this, false);

		// No associated markup => no header section
		if (associatedMarkupStream == null)
		{
			return null;
		}

		// Lazy scan the markup for a header component tag, if necessary
		// 'index' will be where <wicket:head> resides in the markup
		int index = Markup.NO_HEADER_FOUND;
		if (associatedMarkupStream.getHeaderIndex() != Markup.NO_HEADER_FOUND)
		{
			// The markup has been scanned already. Get the index where the
			// header tag resides from the markup
			index = associatedMarkupStream.getHeaderIndex();
		}

		// Ok, finished scanning the markup for header tag
		// If markup contains a header section, handle it now.
		if (index != Markup.NO_HEADER_FOUND)
		{
			// Position markup stream at beginning of header tag
			associatedMarkupStream.setCurrentIndex(index);

			// Create a HtmlHeaderContainer for the header tag found
			final MarkupElement element = associatedMarkupStream.get();
			if (element instanceof WicketTag)
			{
				final WicketTag wTag = (WicketTag)element;
				if ((wTag.isHeadTag() == true) && (wTag.getNamespace() != null))
				{
					// found <wicket:head>
					// create a unique id for the HtmlHeaderContainer to be
					// created
					final String headerId = "_" + Classes.name(this.getClass())
							+ this.getVariation() + "Header";

					// Create the header container and associate the markup with
					// it
					HeaderPartContainer headerContainer = new HeaderPartContainer(headerId, 
							this, wTag.getAttributes().getString(
									associatedMarkupStream.getWicketNamespace() + ":scope"));
					headerContainer.setMarkupStream(associatedMarkupStream);
					headerContainer.setRenderBodyOnly(true);

					// The container does have a header component
					return headerContainer;
				}
			}
		}

		// Though the container does have markup, it does not have a
		// <wicket:head> region.
		return null;
	}

	/**
	 * For each wicket:head tag a HeaderPartContainer is created and added to
	 * the HtmlHeaderContainer which has been added to the Page.
	 */
	private static final class HeaderPartContainer extends WebMarkupContainerWithAssociatedMarkup
			implements
				IComponentResolver
	{
		private static final long serialVersionUID = 1L;

		/** The panel or bordered page the header part is associated with */
		private final MarkupContainer container;

		/** <wicket:head scope="...">. A kind of namespace */
		private final String scope;

		/**
		 * @param id
		 *            The component id
		 * @param container
		 *            The Panel (or bordered page) the header part is associated
		 *            with
		 * @param scope
		 *            The scope of the wicket:head tag
		 */
		public HeaderPartContainer(final String id, final MarkupContainer container,
				final String scope)
		{
			super(id);
			this.container = container;
			this.scope = scope;
		}

		/**
		 * Get the scope of the header part
		 * 
		 * @return The scope name
		 */
		public final String getScope()
		{
			return this.scope;
		}

		/**
		 * 
		 * @see wicket.MarkupContainer#isTransparentResolver()
		 */
		public boolean isTransparentResolver()
		{
			return true;
		}

		/**
		 * @see IComponentResolver#resolve(MarkupContainer, MarkupStream,
		 *      ComponentTag)
		 */
		public final boolean resolve(final MarkupContainer container,
				final MarkupStream markupStream, final ComponentTag tag)
		{
			// The tag must be resolved against the panel and not against the
			// page
			Component component = this.container.get(tag.getId());
			if (component != null)
			{
				component.render(markupStream);
				return true;
			}

			return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/431.java