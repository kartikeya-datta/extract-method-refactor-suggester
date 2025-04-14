error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9658.java
text:
```scala
p@@ublic class WicketHeaderMarkupLoadListener implements IMarkupLoadListener

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) eelco12 $
 * $Revision$
 * $Date: 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) $
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
package wicket.markup.parser.onLoadListener;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Page;
import wicket.Component.IVisitor;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupException;
import wicket.markup.MarkupFragment;
import wicket.markup.MarkupStream;
import wicket.markup.html.BodyContainer;
import wicket.markup.html.WebPage;
import wicket.markup.html.internal.HeaderContainer;
import wicket.markup.html.internal.WicketHeadContainer;
import wicket.markup.parser.filter.HtmlHeaderSectionHandler;

/**
 * A markup load listener which adds the wicket:head containers to Page, Panel,
 * Border etc.
 * 
 * @author Juergen Donnerstag
 */
public class WicketHeaderLoader implements IMarkupLoadListener
{
	/**
	 * @see wicket.markup.parser.onLoadListener.IMarkupLoadListener#onAssociatedMarkupLoaded(wicket.MarkupContainer,
	 *      wicket.markup.MarkupFragment)
	 */
	public void onAssociatedMarkupLoaded(final MarkupContainer container,
			final MarkupFragment fragment)
	{
		// Remove any existing wicket header container <wicket:head>
		container.visitChildren(WicketHeadContainer.class, new IVisitor()
		{
			public Object component(final Component<?> component)
			{
				component.remove();
				return CONTINUE_TRAVERSAL;
			}
		});

		// Get the header container <head> from the page
		final Page page = container.getPage();
		final HeaderContainer headerContainer = (HeaderContainer)page
				.get(HtmlHeaderSectionHandler.HEADER_ID);

		// Search for wicket:head in the associated markup, create container for
		// these tags and copy the body onload and onunload attributes
		fragment.visitChildren(MarkupFragment.class, new MarkupFragment.IVisitor()
		{
			private boolean foundBody = false;

			/**
			 * @see wicket.markup.MarkupFragment.IVisitor#visit(wicket.markup.MarkupElement,
			 *      wicket.markup.MarkupFragment)
			 */
			public Object visit(final MarkupElement element, final MarkupFragment parent)
			{
				final MarkupFragment frag = (MarkupFragment)element;
				final ComponentTag tag = frag.getTag();

				// if <wicket:head>, than
				if (tag.isWicketHeadTag())
				{
					if (foundBody == true)
					{
						throwMarkupException(fragment, tag,
								"<wicket:head> must be before the <body>, <wicket:panel> ... tag");
					}

					// Create a new wicket header container
					WicketHeadContainer header = newWicketHeaderContainer(container, frag);

					// Determine if the wicket:head markup should be printed or
					// not.
					header.setEnable(headerContainer.okToRender(header));

					return CONTINUE_TRAVERSAL_BUT_DONT_GO_DEEPER;
				}
				else if (tag.isBodyTag())
				{
					// Found <body>. "Copy" the attributes to the page's body
					// tag, if the container loading the markup is not a Page
					foundBody = true;
					if ((page instanceof WebPage) && !(container instanceof Page))
					{
						addBodyModifier(BodyContainer.ONLOAD, container, tag);
						addBodyModifier(BodyContainer.ONUNLOAD, container, tag);
					}
				}
				else if (tag.isMajorWicketComponentTag())
				{
					// Allow for improved error messages
					foundBody = true;
				}

				return CONTINUE_TRAVERSAL;
			}
		});
	}

	/**
	 * Create a new WicketHeadContainer. Users may wish subclass the method to
	 * implemented more sophisticated header scoping stragegies.
	 * 
	 * @param parent
	 *            The parent for the header
	 * @param fragment
	 *            The markup fragment associated with the wicket:head
	 * @return The new header part container s
	 */
	public WicketHeadContainer newWicketHeaderContainer(final MarkupContainer parent,
			final MarkupFragment fragment)
	{
		return new WicketHeadContainer(parent, fragment.getTag().getId(), fragment);
	}

	/**
	 * Throw a MarkupException
	 * 
	 * @param parent
	 *            The associated markup file
	 * @param tag
	 *            The element causing the error
	 * @param message
	 *            The error message
	 */
	private void throwMarkupException(final MarkupFragment parent, final MarkupElement tag,
			final String message)
	{
		// Create a MarkupStream and position it at the error location
		MarkupStream markupStream = new MarkupStream(parent);
		while (markupStream.hasMore())
		{
			if (markupStream.next() == tag)
			{
				break;
			}
		}
		throw new MarkupException(markupStream,
				"<wicket:head> must be before the <body>, <wicket:panel> ... tag");
	}

	/**
	 * Attach an AttributeModifier to the body container which appends the new
	 * value to the onLoad attribute
	 * 
	 * @param attribute
	 *            The body tags attribute name
	 * @param container
	 *            The container loading the associate markup
	 * @param tag
	 *            The tag to "copy" the attributes from
	 */
	private void addBodyModifier(final String attribute, final MarkupContainer container,
			final ComponentTag tag)
	{
		final CharSequence value = tag.getString(attribute);
		if (value != null)
		{
			// Attach an AttributeModifier to the body container
			// which appends the new value to the onLoad attribute
			((WebPage)container.getPage()).getBodyContainer().addModifier(attribute, value,
					container);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9658.java