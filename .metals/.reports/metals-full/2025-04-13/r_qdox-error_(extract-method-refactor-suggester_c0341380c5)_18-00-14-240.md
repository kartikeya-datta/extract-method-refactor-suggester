error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2905.java
text:
```scala
final S@@tring headerId = "_" + Classes.name(this.getClass())+ this.getVariation() + "Header";

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wicket.Component;
import wicket.IEventRequestHandler;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.Markup;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupException;
import wicket.markup.MarkupStream;
import wicket.markup.WicketTag;
import wicket.model.IModel;
import wicket.util.lang.Classes;

/**
 * A container of HTML markup and components. It is very similar to the base
 * class MarkupContainer, except that the markup type is defined to be HTML.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public class WebMarkupContainer extends MarkupContainer implements IHeaderContributor
{
	private List headerComponents;

	/**
	 * @see Component#Component(String)
	 */
	public WebMarkupContainer(final String id)
	{
		super(id);
	}

	/**
	 * @see wicket.Component#Component(String, IModel)
	 */
	public WebMarkupContainer(final String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Gets the markup type for this component.
	 * 
	 * @return Markup type of HTML
	 */
	public final String getMarkupType()
	{
		return "html";
	}

	/**
	 * Renders this component. This implementation just calls renderComponent.
	 */
	protected void onRender()
	{
		renderComponent(findMarkupStream());
	}

	/**
	 * Print to the web response what ever the component wants
	 * to contribute to the head section.
	 * 
	 * @see wicket.markup.html.IHeaderContributor#renderHead(wicket.markup.html.HtmlHeaderContainer)
	 * 
	 * @param container The HtmlHeaderContainer
	 */
	public void renderHead(final HtmlHeaderContainer container)
	{
		// Ask the child component if it has something to contribute
		WebMarkupContainer headerPart = getHeaderPart();

		// If the child component has something to contribute to 
		// the header and in case the very same Component has not 
		// contributed to the page, than ...
		// A component's header section must only be added once, 
		// no matter how often the same Component has been added 
		// to the page or any other container in the hierachy.
		if ((headerPart != null) && (container.get(headerPart.getId()) == null))
		{
			container.autoAdd(headerPart);
			
			// Check if the component requires some <body onLoad="..">
			// attribute to be copied to the page's body tag. 
			checkBodyOnLoad();
		}

		// get head and body contributions in one loop
		IEventRequestHandler[] handlers = getEventRequestHandlers();
		if (handlers != null)
		{
			for (int i = 0; i < handlers.length; i++)
			{
				if (handlers[i] instanceof IHeaderContributor)
				{
					((IHeaderContributor)handlers[i]).renderHead(container);
				}
				if (handlers[i] instanceof IBodyOnloadContributor)
				{
					String stmt = ((IBodyOnloadContributor)handlers[i]).getBodyOnload();
					if (stmt != null)
					{
						((WebPage)getPage()).appendToBodyOnLoad(stmt);
					}
				}
			}	
		}
	}
	
	/**
	 * Check if the component requires some <body onLoad=".."> attribute to 
	 * be copied to the page's body tag.
	 */
	private void checkBodyOnLoad()
	{
		// gracefull getAssociateMarkupStream. Throws no exception in case
		// markup is not found
		final MarkupStream associatedMarkupStream = getApplication().getMarkupCache()
				.getMarkupStream(this, false);

		// No associated markup => no body tag
		if (associatedMarkupStream == null)
		{
			return;
		}

		// Remember the current position within markup, where we need to 
		// back to, at the end.
		int index = associatedMarkupStream.getCurrentIndex();
		
		try
		{
		    // Start at the beginning
		    associatedMarkupStream.setCurrentIndex(0);
		    
			// Iterate the markup and find <body onLoad="...">
			do
			{
				final MarkupElement element = associatedMarkupStream.get();
				if (element instanceof ComponentTag)
				{
					final ComponentTag tag = (ComponentTag)element;
					if ("body".equalsIgnoreCase(tag.getName()))
					{
					    final String onLoad = tag.getAttributes().getString("onload");
					    if (onLoad != null)
					    {
					        ((WebPage)getPage()).appendToBodyOnLoad(onLoad);
					    }
					    
					    // There can only be one body tag
					    break;
					}
				}
			}
			while (associatedMarkupStream.next() != null);
		}
		finally
		{
		    // Make sure we return to the orginal position in the markup
		    associatedMarkupStream.setCurrentIndex(index);
		}
	}
	
	/**
	 * Gets the header part for the markup container. Returns null if it doesn't
	 * contribute to a header.
	 * 
	 * @return the header part for this markup container or null if it doesn't
	 *         contribute anything.
	 */
	private final WebMarkupContainer getHeaderPart()
	{
		// gracefull getAssociateMarkupStream. Throws no exception in case
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
		    
		    // Create a HtmlHeaderContainer for the header tag found and
		    // add all components from addToHeader list
			final MarkupElement element = associatedMarkupStream.get();
			if (element instanceof WicketTag)
			{
				final WicketTag wTag = (WicketTag)element;
				if ((wTag.isHeadTag() == true) && (wTag.getNamespace() != null))
				{
				    // found <wicket:head>
				    // create a unique id for the HtmlHeaderContainer to be created
					final String headerId = "_" + Classes.name(this.getClass()) + "Header";
					
					// Create the header container and associate the markup with it
					WebMarkupContainer headerContainer = new WebMarkupContainer(headerId);
					headerContainer.setMarkupStream(associatedMarkupStream);
					headerContainer.setRenderBodyOnly(true);
					
					// In case components are part of the region, the user must 
					// have provided the component objects by means of addToHeader().
					// All the component provided by the user, must now be added
					// to the newly created header container.
					if (this.headerComponents != null)
					{
						for (Iterator iter = headerComponents.iterator(); iter.hasNext();)
						{
					        headerContainer.add((Component)iter.next());
						}
					}
					
					// The container does have a header component
					return headerContainer;
				}
			}
		}

		if (this.headerComponents != null)
		{
			throw new MarkupException(
					"You have added header components but did not specify a <wicket:head> region in your Page markup: "
							+ this.toString());
		}
		
		// Though the container does have markup, it does not have a 
		// <wicket:head> region.
		return null;
	}

	/**
	 * Components which are part of a wicket header region, must be added by
	 * means of addToHeader() instead of add().
	 * 
	 * @param child
	 *            The component to be added to the header region.
	 */
	public final void addToHeader(final Component child)
	{
		if (this.headerComponents == null)
		{
			this.headerComponents = new ArrayList();
		}

		this.headerComponents.add(child);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2905.java