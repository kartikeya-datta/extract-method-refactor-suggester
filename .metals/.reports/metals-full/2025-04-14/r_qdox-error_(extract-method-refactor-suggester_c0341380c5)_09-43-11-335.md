error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11318.java
text:
```scala
public final C@@lass< ? extends Component> getContainerClass()

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
package org.apache.wicket.markup;

import org.apache.wicket.Component;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.string.Strings;


/**
 * A stream of {@link org.apache.wicket.markup.MarkupElement}s, subclasses of which are
 * {@link org.apache.wicket.markup.ComponentTag} and {@link org.apache.wicket.markup.RawMarkup}. A
 * markup stream has a current index in the list of markup elements. The next markup element can be
 * retrieved and the index advanced by calling next(). If the index hits the end, hasMore() will
 * return false.
 * <p>
 * The current markup element can be accessed with get() and as a ComponentTag with getTag().
 * <p>
 * The stream can be sought to a particular location with setCurrentIndex().
 * <p>
 * Convenience methods also exist to skip component tags (and any potentially nested markup) or raw
 * markup.
 * <p>
 * Several boolean methods of the form at*() return true if the markup stream is positioned at a tag
 * with a given set of characteristics.
 * <p>
 * The resource from which the markup was loaded can be retrieved with getResource().
 * 
 * @author Jonathan Locke
 */
public final class MarkupStream
{
	/** Element at currentIndex */
	private MarkupElement current;

	/** Current index in markup stream */
	private int currentIndex = 0;

	/** The markup element list */
	private final Markup markup;

	/**
	 * Constructor
	 * 
	 * @param markup
	 *            List of markup elements
	 */
	public MarkupStream(final Markup markup)
	{
		this.markup = markup;

		if (markup.size() > 0)
		{
			current = get(currentIndex);
		}
	}

	/**
	 * @return True if current markup element is a close tag
	 */
	public boolean atCloseTag()
	{
		return atTag() && getTag().isClose();
	}

	/**
	 * @return True if current markup element is an openclose tag
	 */
	public boolean atOpenCloseTag()
	{
		return atTag() && getTag().isOpenClose();
	}

	/**
	 * @param componentId
	 *            Required component name attribute
	 * @return True if the current markup element is an openclose tag with the given component name
	 */
	public boolean atOpenCloseTag(final String componentId)
	{
		return atOpenCloseTag() && componentId.equals(getTag().getId());
	}

	/**
	 * @return True if current markup element is an open tag
	 */
	public boolean atOpenTag()
	{
		return atTag() && getTag().isOpen();
	}

	/**
	 * @param id
	 *            Required component id attribute
	 * @return True if the current markup element is an open tag with the given component name
	 */
	public boolean atOpenTag(final String id)
	{
		return atOpenTag() && id.equals(getTag().getId());
	}

	/**
	 * @return True if current markup element is a tag
	 */
	public boolean atTag()
	{
		return current instanceof ComponentTag;
	}

	/**
	 * Compare this markup stream with another one
	 * 
	 * @param that
	 *            The other markup stream
	 * @return True if each MarkupElement in this matches each element in that
	 */
	public boolean equalTo(final MarkupStream that)
	{
		// While a has more markup elements
		while (hasMore())
		{
			// Get an element from each
			final MarkupElement thisElement = this.get();
			final MarkupElement thatElement = that.get();

			// and if the elements are not equal
			if (thisElement != null && thatElement != null)
			{
				if (!thisElement.equalTo(thatElement))
				{
					// fail the comparison
					return false;
				}
			}
			else
			{
				// If one element is null,
				if (!(thisElement == null && thatElement == null))
				{
					// fail the comparison
					return false;
				}
			}
			next();
			that.next();
		}

		// If we've run out of markup elements in b
		if (!that.hasMore())
		{
			// then the two streams match perfectly
			return true;
		}

		// Stream b had extra elements
		return false;
	}

	/**
	 * True, if associate markup is the same. It will change e.g. if the markup file has been
	 * re-loaded or the locale has been changed.
	 * 
	 * @param markupStream
	 *            The markup stream to compare with.
	 * @return true, if markup has not changed
	 */
	public final boolean equalMarkup(final MarkupStream markupStream)
	{
		if (markupStream == null)
		{
			return false;
		}
		return markup == markupStream.markup;
	}

	/**
	 * Find the markup element index of the component with 'path'
	 * 
	 * @param path
	 *            The component path expression
	 * @param id
	 *            The component's id to search for
	 * @return -1, if not found
	 */
	public final int findComponentIndex(final String path, final String id)
	{
		return markup.findComponentIndex(path, id);
	}

	/**
	 * @return The current markup element
	 */
	public MarkupElement get()
	{
		return current;
	}

	/**
	 * @param index
	 *            The index of a markup element
	 * @return The MarkupElement element
	 */
	public MarkupElement get(final int index)
	{
		return markup.get(index);
	}

	/**
	 * Get the component/container's Class which is directly associated with the stream.
	 * 
	 * @return The component's class
	 */
	public final Class< ? extends Component< ? >> getContainerClass()
	{
		return markup.getMarkupResourceData().getResource().getMarkupClass();
	}

	/**
	 * @return Current index in markup stream
	 */
	public int getCurrentIndex()
	{
		return currentIndex;
	}

	/**
	 * Gets the markup encoding. A markup encoding may be specified in a markup file with an XML
	 * encoding specifier of the form &lt;?xml ... encoding="..." ?&gt;.
	 * 
	 * @return The encoding, or null if not found
	 */
	public final String getEncoding()
	{
		return markup.getMarkupResourceData().getEncoding();
	}

	/**
	 * @return The resource where this markup stream came from
	 */
	public IResourceStream getResource()
	{
		return markup.getMarkupResourceData().getResource();
	}

	/**
	 * @return The current markup element as a markup tag
	 */
	public ComponentTag getTag()
	{
		if (current instanceof ComponentTag)
		{
			return (ComponentTag)current;
		}

		throwMarkupException("Tag expected");

		return null;
	}

	/**
	 * Get the wicket namespace valid for this specific markup
	 * 
	 * @return wicket namespace
	 */
	public final String getWicketNamespace()
	{
		return markup.getMarkupResourceData().getWicketNamespace();
	}

	/**
	 * Return the XML declaration string, in case if found in the markup.
	 * 
	 * @return Null, if not found.
	 */
	public String getXmlDeclaration()
	{
		return markup.getMarkupResourceData().getXmlDeclaration();
	}

	/**
	 * @return True if this markup stream has more MarkupElement elements
	 */
	public boolean hasMore()
	{
		return currentIndex < markup.size();
	}

	/**
	 * 
	 * @return true, if underlying markup has been merged (inheritance)
	 */
	public final boolean isMergedMarkup()
	{
		return markup instanceof MergedMarkup;
	}

	/**
	 * Note:
	 * 
	 * @return The next markup element in the stream
	 */
	public MarkupElement next()
	{
		if (++currentIndex < markup.size())
		{
			return current = get(currentIndex);
		}

		return null;
	}

	/**
	 * @param currentIndex
	 *            New current index in the stream
	 */
	public void setCurrentIndex(final int currentIndex)
	{
		current = get(currentIndex);
		this.currentIndex = currentIndex;
	}

	/**
	 * Skips this component and all nested components
	 */
	public final void skipComponent()
	{
		// Get start tag
		final ComponentTag startTag = getTag();

		if (startTag.isOpen())
		{
			// With HTML not all tags require a close tag which
			// must have been detected by the HtmlHandler earlier on.
			if (startTag.hasNoCloseTag() == false)
			{
				// Skip <tag>
				next();

				// Skip nested components
				skipToMatchingCloseTag(startTag);
			}

			// Skip </tag>
			next();
		}
		else if (startTag.isOpenClose())
		{
			// Skip <tag/>
			next();
		}
		else
		{
			// We were something other than <tag> or <tag/>
			throwMarkupException("Skip component called on bad markup element " + startTag);
		}
	}

	/**
	 * Skips any raw markup at the current position
	 */
	public void skipRawMarkup()
	{
		while (true)
		{
			if (current instanceof RawMarkup)
			{
				if (next() != null)
				{
					continue;
				}
			}
			else if ((current instanceof ComponentTag) && !(current instanceof WicketTag))
			{
				ComponentTag tag = (ComponentTag)current;
				if (tag.isAutoComponentTag())
				{
					if (next() != null)
					{
						continue;
					}
				}
				else if (tag.isClose() && tag.getOpenTag().isAutoComponentTag())
				{
					if (next() != null)
					{
						continue;
					}
				}
			}
			break;
		}
	}

	/**
	 * Skips any markup at the current position until the wicket tag name is found.
	 * 
	 * @param wicketTagName
	 *            wicket tag name to seek
	 */
	public void skipUntil(final String wicketTagName)
	{
		while (true)
		{
			if ((current instanceof WicketTag) &&
				((WicketTag)current).getName().equals(wicketTagName))
			{
				return;
			}

			// go on until we reach the end
			if (next() == null)
			{
				return;
			}
		}
	}

	/**
	 * Renders markup until a closing tag for openTag is reached.
	 * 
	 * @param openTag
	 *            The open tag
	 */
	public void skipToMatchingCloseTag(final ComponentTag openTag)
	{
		// Loop through the markup in this container
		while (hasMore())
		{
			// If the current markup tag closes the openTag
			if (get().closes(openTag))
			{
				// Done!
				return;
			}

			// Skip element
			next();
		}
		throwMarkupException("Expected close tag for " + openTag);
	}

	/**
	 * Throws a new markup exception
	 * 
	 * @param message
	 *            The exception message
	 * @throws MarkupException
	 */
	public void throwMarkupException(final String message)
	{
		throw new MarkupException(this, message);
	}

	/**
	 * @return An HTML string highlighting the current position in the markup stream
	 */
	public String toHtmlDebugString()
	{
		final StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < markup.size(); i++)
		{
			if (i == currentIndex)
			{
				buffer.append("<font color = \"red\">");
			}

			final MarkupElement element = markup.get(i);

			buffer.append(Strings.escapeMarkup(element.toString(), true).toString());

			if (i == currentIndex)
			{
				buffer.append("</font>");
			}
		}

		return buffer.toString();
	}

	/**
	 * @return String representation of markup stream
	 */
	@Override
	public String toString()
	{
		return "[markup = " + String.valueOf(markup) + ", index = " + currentIndex +
			", current = " + ((current == null) ? "null" : current.toUserDebugString()) + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11318.java