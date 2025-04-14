error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15932.java
text:
```scala
private final M@@arkupContainer markupProvider;

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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.parser.XmlTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Objects;
import org.apache.wicket.version.undo.Change;

/**
 * Usually you either have a markup file or a xml tag with
 * wicket:id="myComponent" to associate markup with a component. However in some
 * rare cases, especially when working with small panels it is a bit awkward to
 * maintain tiny pieces of markup in plenty of panel markup files. Use cases are
 * for example list views where list items are different depending on a state.
 * <p>
 * Fragments provide a means to maintain the panels tiny piece of markup in the
 * parents markup file.
 * <p>
 * 
 * <pre>
 *             &lt;span wicket:id=&quot;myPanel&quot;&gt;Example input (will be removed)&lt;/span&gt;
 *            
 *             &lt;wicket:fragment wicket:id=&quot;frag1&quot;&gt;panel 1&lt;/wicket:fragment&gt;
 *             &lt;wicket:fragment wicket:id=&quot;frag2&quot;&gt;panel 2&lt;/wicket:fragment&gt;
 * </pre> 
 * <pre>
 *             add(new Fragment(&quot;myPanel1&quot;, &quot;frag1&quot;);
 * </pre>
 * 
 * @author Juergen Donnerstag
 */
public class Fragment extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	/** The wicket:id of the associated markup fragment */
	private String markupId;

	/** The container providing the inline markup */
	private MarkupContainer markupProvider;

	/**
	 * Constructor.
	 * 
	 * @see org.apache.wicket.Component#Component(String)
	 * 
	 * @param id
	 *            The component id
	 * @param markupId
	 *            The associated id of the associated markup fragment
	 */
	public Fragment(final String id, final String markupId)
	{
		this(id, markupId, null, null);
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
	 * @param model
	 *            The model for this fragment
	 */
	public Fragment(final String id, final String markupId, final IModel model)
	{
		this(id, markupId, null, model);
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
			final IModel model)
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
			addStateChange(new Change()
			{
				private static final long serialVersionUID = 1L;
				private final String oldMarkupId = Fragment.this.markupId;

				public void undo()
				{
					Fragment.this.markupId = oldMarkupId;
				}

			});
		}
		this.markupId = markupId;
	}

	/**
	 * Make sure we open up open-close tags to open-body-close
	 * 
	 * @see org.apache.wicket.Component#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
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
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		// Skip the components body. It will be replaced by the fragment
		markupStream.skipRawMarkup();

		final MarkupStream providerMarkupStream = chooseMarkupStream(markupStream);
		if (providerMarkupStream == null)
		{
			throw new IllegalStateException(
					"no markup stream found for providing markup container " + markupProvider);
		}

		renderFragment(providerMarkupStream, openTag);
	}

	/**
	 * Get the markup stream which shall be used to search for the fragment
	 * 
	 * @param markupStream
	 *            The markup stream is associated with the component (not the
	 *            fragment)
	 * @return The markup stream to be used to find the fragment markup
	 */
	protected MarkupStream chooseMarkupStream(final MarkupStream markupStream)
	{
		MarkupStream stream = null;

		if (this.markupProvider == null)
		{
			stream = markupStream;
		}
		else
		{

			stream = this.markupProvider.getAssociatedMarkupStream(false);
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
		// remember the current position in the markup. Will have to come back
		// to it.
		int currentIndex = providerMarkupStream.getCurrentIndex();

		// Find the markup fragment
		int index = providerMarkupStream.findComponentIndex(null, markupId);
		if (index == -1)
		{
			throw new MarkupException("Markup of component class `"
					+ providerMarkupStream.getContainerClass().getName()
					+ "` does not contain a fragment with wicket:id `" + markupId + "`. Context: "
					+ toString());
		}

		// Set the markup stream position to where the fragment begins
		providerMarkupStream.setCurrentIndex(index);

		try
		{
			// Get the fragments open tag
			ComponentTag fragmentOpenTag = providerMarkupStream.getTag();

			// We'll completely ignore the fragments open tag. It'll not be
			// rendered
			providerMarkupStream.next();

			// Render the body of the fragment
			super.onComponentTagBody(providerMarkupStream, fragmentOpenTag);
		}
		finally
		{
			// Make sure the markup stream is positioned where we started back
			// at the original component
			providerMarkupStream.setCurrentIndex(currentIndex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15932.java