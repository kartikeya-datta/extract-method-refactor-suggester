error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15748.java
text:
```scala
s@@tream = new MarkupStream(markupProvider.getMarkup(null));

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
package org.apache.wicket.markup.html.panel;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import org.apache.wicket.markup.parser.XmlTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Objects;

/**
 * Usually you either have a markup file or a xml tag with wicket:id="myComponent" to associate
 * markup with a component. However in some rare cases, especially when working with small panels it
 * is a bit awkward to maintain tiny pieces of markup in plenty of panel markup files. Use cases are
 * for example list views where list items are different depending on a state.
 * <p>
 * Fragments provide a means to maintain the panels tiny piece of markup. Since it can be anywhere,
 * the component whose markup contains the fragment's markup must be provided (markup provider).
 * <p>
 * 
 * <pre>
 *  &lt;span wicket:id=&quot;myPanel&quot;&gt;Example input (will be removed)&lt;/span&gt;
 * 
 *  &lt;wicket:fragment wicket:id=&quot;frag1&quot;&gt;panel 1&lt;/wicket:fragment&gt;
 *  &lt;wicket:fragment wicket:id=&quot;frag2&quot;&gt;panel 2&lt;/wicket:fragment&gt;
 * </pre>
 * 
 * <pre>
 *  add(new Fragment(&quot;myPanel1&quot;, &quot;frag1&quot;, myPage);
 * </pre>
 * 
 * @author Juergen Donnerstag
 */
public class Fragment extends WebMarkupContainerWithAssociatedMarkup
{
	private static final long serialVersionUID = 1L;

	/** The wicket:id of the associated markup fragment */
	private String markupId;

	/** The container providing the inline markup */
	private final MarkupContainer markupProvider;

	/**
	 * Constructor.
	 * 
	 * @see org.apache.wicket.Component#Component(String)
	 * 
	 * @param id
	 *            The component id
	 * @param markupId
	 *            The associated id of the associated markup fragment
	 * @param markupProvider
	 *            The component whose markup contains the fragment's markup
	 */
	public Fragment(final String id, final String markupId, final MarkupContainer markupProvider)
	{
		this(id, markupId, markupProvider, null);
	}

	/**
	 * Constructor.
	 * 
	 * @see org.apache.wicket.Component#Component(String)
	 * 
	 * @param id
	 *            The component id
	 * @param markupId
	 *            The associated id of the associated markup fragment
	 * @param markupProvider
	 *            The component whose markup contains the fragment's markup
	 * @param model
	 *            The model for this fragment
	 */
	public Fragment(final String id, final String markupId, final MarkupContainer markupProvider,
		final IModel<?> model)
	{
		super(id, model);

		if (markupId == null)
		{
			throw new IllegalArgumentException("markupId cannot be null");
		}

		this.markupId = markupId;
		this.markupProvider = markupProvider;
	}

	/**
	 * The associated markup fragment can be modified
	 * 
	 * @param markupId
	 */
	public final void setMarkupTagReferenceId(final String markupId)
	{
		if (markupId == null)
		{
			throw new IllegalArgumentException("markupId cannot be null");
		}
		if (!Objects.equal(this.markupId, markupId))
		{
			addStateChange();
		}
		this.markupId = markupId;
	}

	/**
	 * Make sure we open up open-close tags to open-body-close
	 * 
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		if (tag.isOpenClose())
		{
			tag.setType(XmlTag.OPEN);
		}
		super.onComponentTag(tag);
	}

	/**
	 * 
	 * @see org.apache.wicket.Component#onComponentTagBody(org.apache.wicket.markup.MarkupStream,
	 *      org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		// Skip the components body. It will be replaced by the fragment
		if (((ComponentTag)markupStream.get(markupStream.getCurrentIndex() - 1)).isOpen())
		{
			markupStream.skipRawMarkup();
		}

		final MarkupStream providerMarkupStream = chooseMarkupStream(markupStream);
		if (providerMarkupStream == null)
		{
			throw new MarkupNotFoundException(
				"Fragment: No markup stream found for providing markup container " +
					markupProvider.toString() + ". Fragment: " + toString());
		}

		renderFragment(providerMarkupStream, openTag);
	}

	/**
	 * Get the markup stream which shall be used to search for the fragment
	 * 
	 * @param markupStream
	 *            The markup stream is associated with the component (not the fragment)
	 * @return The markup stream to be used to find the fragment markup
	 */
	protected MarkupStream chooseMarkupStream(final MarkupStream markupStream)
	{
		MarkupStream stream = null;

		// TODO Post 1.3: Cleanup this after deprecated constructors are removed
		if (markupProvider == null)
		{
			stream = markupStream;
		}
		else
		{
			stream = markupProvider.getAssociatedMarkupStream(false);
			if (stream == null)
			{
				// The following statement assumes that the markup provider is a
				// parent along the line up to the Page
				stream = markupProvider.getMarkupStream();
			}
		}
		return stream;
	}

	/**
	 * Render the markup starting at the current position of the markup strean
	 * 
	 * @see #onComponentTagBody(MarkupStream, ComponentTag)
	 * 
	 * @param providerMarkupStream
	 * @param openTag
	 */
	private void renderFragment(final MarkupStream providerMarkupStream, final ComponentTag openTag)
	{
		// remember the current position in the markup. Will have to come back to it.
		int currentIndex = providerMarkupStream.getCurrentIndex();

		// Find the markup fragment
		while (providerMarkupStream.hasMore())
		{
			MarkupElement elem = providerMarkupStream.get();
			if (elem instanceof ComponentTag)
			{
				ComponentTag tag = providerMarkupStream.getTag();
				if (tag.isOpen() || tag.isOpenClose())
				{
					if (tag.getId().equals(markupId))
					{
						break;
					}
				}
			}

			providerMarkupStream.nextOpenTag();
		}

		if (providerMarkupStream.hasMore() == false)
		{
			throw new MarkupException("Markup of component class `" +
				providerMarkupStream.getContainerClass().getName() +
				"` does not contain a fragment with wicket:id `" + markupId + "`. Context: " +
				toString());
		}

		try
		{
			// Get the fragments open tag
			ComponentTag fragmentOpenTag = providerMarkupStream.getTag();

			// if it is an open close tag, skip this fragment.
			if (!fragmentOpenTag.isOpenClose())
			{
				// We'll completely ignore the fragments open tag. It'll not be
				// rendered
				providerMarkupStream.next();

				// Render the body of the fragment
				super.onComponentTagBody(providerMarkupStream, fragmentOpenTag);
			}
		}
		finally
		{
			// Make sure the markup stream is positioned where we started back
			// at the original component
			providerMarkupStream.setCurrentIndex(currentIndex);
		}
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#hasAssociatedMarkup()
	 */
	@Override
	public boolean hasAssociatedMarkup()
	{
		return true;
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#getAssociatedMarkupStream(boolean)
	 */
	@Override
	public MarkupStream getAssociatedMarkupStream(boolean throwException)
	{
		MarkupStream stream = null;

		// TODO Post 1.3: Cleanup this after deprecated constructors are removed
		if (markupProvider != null)
		{
			stream = markupProvider.getAssociatedMarkupStream(false);
			if (stream == null)
			{
				// The following statement assumes that the markup provider is a
				// parent along the line up to the Page
				stream = markupProvider.getMarkupStream();
			}
		}

		// try self's markup stream
		if (stream == null)
		{
			stream = super.getAssociatedMarkupStream(false);
		}

		// if self doesn't have markup stream try the parent's
		if (stream == null)
		{
			MarkupContainer container = getParent();
			while (container != null)
			{
				if (container.hasAssociatedMarkup())
				{
					stream = container.getAssociatedMarkupStream(false);
					break;
				}
				container = container.getParent();
			}
		}

		// if we cant find any markup stream
		if ((stream == null) && throwException)
		{
			// fail, but fail with an error message that will point to this
			// component
			super.getAssociatedMarkupStream(true);
		}

		return stream;
	}

	/**
	 * Returns markup provider associated with this fragment
	 * 
	 * @return markup provider
	 */
	public final MarkupContainer getMarkupProvider()
	{
		return markupProvider;
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#getMarkup(org.apache.wicket.Component)
	 */
	@Override
	public IMarkupFragment getMarkup(final Component child)
	{
		IMarkupFragment markup = null;

		// Get the markup provider
		MarkupContainer provider = getMarkupProvider();
		if (provider == null)
		{
			provider = getParent();
		}

		if (provider.hasAssociatedMarkup())
		{
			markup = provider.getAssociatedMarkup();
		}
		else
		{
			markup = getParent().getMarkup();
		}

		if (markup == null)
		{
			return null;
		}

		markup = markup.find(markupId);

		if (child == null)
		{
			return markup;
		}

		return markup.find(child.getId());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15748.java