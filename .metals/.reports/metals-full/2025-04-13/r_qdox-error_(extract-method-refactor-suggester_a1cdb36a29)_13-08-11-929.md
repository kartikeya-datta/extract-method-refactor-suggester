error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15499.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15499.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,92]

error in qdox parser
file content:
```java
offset: 92
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15499.java
text:
```scala
"Border body container not initialized. Did you forget to call setBorderBodyContainer() ??")@@;

/*
 * $Id: Border.java 4831 2006-03-08 13:32:22 -0800 (Wed, 08 Mar 2006)
 * jdonnerstag $ $Revision$ $Date: 2006-03-08 13:32:22 -0800 (Wed, 08 Mar
 * 2006) $
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
package wicket.markup.html.border;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.IAlternateParentProvider;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupException;
import wicket.markup.MarkupFragment;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import wicket.markup.html.internal.WicketHeadContainer;
import wicket.markup.parser.XmlTag;
import wicket.markup.parser.filter.WicketTagIdentifier;
import wicket.markup.resolver.IComponentResolver;
import wicket.model.IModel;

/**
 * A border component has associated markup which is drawn and determines
 * placement of any markup and/or components nested within the border component.
 * <p>
 * The portion of the border's associated markup file which is to be used in
 * rendering the border is denoted by a &lt;wicket:border&gt; tag. The children
 * of the border component instance are then inserted into this markup,
 * replacing the first &lt;wicket:body&gt; tag in the border's associated
 * markup.
 * <p>
 * For example, if a border's associated markup looked like this:
 * 
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   &lt;wicket:border&gt;
 *     First &lt;wicket:body/&gt; Last
 *   &lt;/wicket:border&gt;
 * &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 * 
 * And the border was used on a page like this:
 * 
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   &lt;span wicket:id = &quot;myBorder&quot;&gt;
 *     Middle
 *   &lt;/span&gt;
 * &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 * 
 * Then the resulting HTML would look like this:
 * 
 * <pre>
 * &lt;html&gt;
 * &lt;body&gt;
 *   First Middle Last
 * &lt;/body&gt;
 * &lt;/html&gt;
 * </pre>
 * 
 * In other words, the body of the myBorder component is substituted into the
 * border's associated markup at the position indicated by the
 * &lt;wicket:body&gt; tag.
 * <p>
 * Regarding &lt;wicket:body/&gt; you have two options. Either use
 * &lt;wicket:body/&gt; (open-close tag) which will automatically be expanded to
 * &lt;wicket:body&gt;body content&lt;/wicket:body&gt; or use
 * &lt;wicket:body&gt;preview region&lt;/wicket:body&gt; in your border's
 * markup. The preview region (everything in between the open and close tag)
 * will automatically be removed.
 * 
 * @param <T>
 *            Type of model object this component holds
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public abstract class Border<T> extends WebMarkupContainerWithAssociatedMarkup<T>
		implements
			IComponentResolver,
			IAlternateParentProvider
{
	private static final String BODY = "body";
	private static final String BORDER = "border";

	static
	{
		// register "wicket:border" and "wicket:body"
		WicketTagIdentifier.registerWellKnownTagName(BORDER);
		WicketTagIdentifier.registerWellKnownTagName(BODY);
	}

	/** The border's wicket:body container */
	private MarkupContainer body;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public Border(MarkupContainer parent, final String id)
	{
		this(parent, id, null);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Border(MarkupContainer parent, final String id, final IModel<T> model)
	{
		super(parent, id, model);

		// Get the wicket:border fragment in the the associate markup file
		MarkupFragment fragment = getAssociatedMarkup(true).getWicketFragment(BORDER, true);
		initBodyContainer(fragment, this);
	}

	/**
	 * If the 'markup' contains wicket:body, than create a new body container
	 * and add it to the 'parent'.
	 * 
	 * @param markup
	 *            The markup to search wicket:body in
	 * @param parent
	 *            The new parent for the body container, if found
	 */
	private void initBodyContainer(final MarkupFragment markup, final MarkupContainer parent)
	{
		if (this.body == null)
		{
			// No recursive search. Search just the direct child elements of the
			// parent markup
			for (MarkupElement elem : markup)
			{
				if (elem instanceof MarkupFragment)
				{
					MarkupFragment fragment = (MarkupFragment)elem;

					// If wicket:body, than create a new body container with
					// 'parent'
					if (fragment.getTag().isWicketBodyTag())
					{
						setBorderBodyContainer(parent);
						break;
					}
				}
			}
		}
	}

	/**
	 * Get the Border's body container. Might be null, if not yet created.
	 * 
	 * @return MarkupContainer
	 */
	protected final MarkupContainer getBodyContainer()
	{
		return this.body;
	}

	/**
	 * 
	 * @see wicket.markup.IAlternateParentProvider#getAlternateParent(java.lang.Class,
	 *      java.lang.String)
	 */
	public MarkupContainer getAlternateParent(final Class childClass, final String childId)
	{
		// If, and only if, a body container exists, than redirect new
		// components to become children of the body.
		return ((this.body == null) || WicketHeadContainer.class.isInstance(childClass) ? this : this.body);
	}

	/**
	 * If the markup contains a container in between the wicket:border and
	 * wicket:body tag, than you must use setBorderBodyContainer() to tell the
	 * Border what the correct container for the Body component will be.
	 * <p>
	 * Create a new Border Body container and add it to the 'parent'. If a body
	 * container has already been created, remove that first.
	 * 
	 * @param parent
	 * @return MarkupContainer
	 */
	protected final MarkupContainer setBorderBodyContainer(final MarkupContainer parent)
	{
		if (this.body != null)
		{
			// TODO Check if empty (no child components)
			this.body.remove();
		}

		// Auto-components are automatically removed at the end of the render
		// cycle. Hence, no '<auto>-' id.
		this.body = new BorderBody(parent, this.getId() + "Body");
		return body;
	}

	/**
	 * @see wicket.markup.html.MarkupContainer#getMarkupFragment(java.lang.String)
	 */
	@Override
	public MarkupFragment getMarkupFragment(final String id)
	{
		// Find the tag in the associated markup
		MarkupFragment fragment = getAssociatedMarkup(true).getWicketFragment(BORDER, true)
				.getChildFragment(id, false);
		
		if (fragment != null)
		{
			return fragment;
		}
		
		// wicket:head must be search for outside wicket:border
		return getAssociatedMarkup(true).getChildFragment(id, true);
	}

	/**
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	@Override
	protected final void onComponentTagBody(final MarkupStream markupStream,
			final ComponentTag openTag)
	{
		// Render the associated markup
		renderAssociatedMarkup(BORDER,
				"Markup for a border component must begin a tag like '<wicket:border>'");

		// Skip the whole border markup: <span wicket:id="myBorder> ... </span>
		markupStream.skipToMatchingCloseTag(openTag);
	}

	/**
	 * 
	 * @see wicket.markup.resolver.IComponentResolver#resolve(wicket.MarkupContainer,
	 *      wicket.markup.MarkupStream, wicket.markup.ComponentTag)
	 */
	public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
		// The resolver is needed because the tag id is '<auto>-body' but the
		// body's container is border.getId() + "Body"

		if (tag.isWicketBodyTag())
		{
			if (this.body == null)
			{
				throw new MarkupException(markupStream,
						"Border body container not initialized. Did you forget to call newBorderBodyContainer() ??");
			}

			// If current tag equals the body open tag, than ...
			if (markupStream.get() == this.body.getMarkupFragment().get(0))
			{
				// Render the body and its children
				this.body.render(markupStream);
				return true;
			}
		}

		// Unable to resolve the request
		return false;
	}

	/**
	 * The wicket:body container
	 */
	private class BorderBody extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		// True, if <wicket:body/>
		private transient boolean wasOpenClose = false;

		/**
		 * Construct.
		 * 
		 * @param parent
		 * @param id
		 */
		public BorderBody(final MarkupContainer parent, final String id)
		{
			super(parent, id);
		}

		/**
		 * Get the wicket:body fragment
		 * 
		 * @see wicket.Component#getMarkupFragment()
		 */
		@Override
		public MarkupFragment getMarkupFragment()
		{
			return Border.this.getAssociatedMarkup(true).getWicketFragment(BORDER, true)
					.getWicketFragment(BODY, true);
		}

		/**
		 * @see wicket.MarkupContainer#getMarkupFragment(java.lang.String)
		 */
		@Override
		public MarkupFragment getMarkupFragment(final String id)
		{
			// First search for the 'id' in the <span
			// wicket:id="myBorder">...</span> markup
			MarkupFragment fragment = Border.this.getMarkupFragment().getChildFragment(id, false);
			if (fragment != null)
			{
				return fragment;
			}

			// If not yet found, try the parent container which might be the
			// Border or any other container in between the Border and the Body.
			return getParent().getMarkupFragment(id);
		}

		/**
		 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
		 */
		@Override
		protected void onComponentTag(final ComponentTag tag)
		{
			// Automatically change the tag from OpenClose to Open-Body-Close if
			// needed
			if (tag.isOpenClose())
			{
				tag.setType(XmlTag.Type.OPEN);
				this.wasOpenClose = true;
			}

			super.onComponentTag(tag);
		}

		/**
		 * 
		 * @see wicket.MarkupContainer#onComponentTagBody(wicket.markup.MarkupStream,
		 *      wicket.markup.ComponentTag)
		 */
		@Override
		protected void onComponentTagBody(final MarkupStream markupStream,
				final ComponentTag openTag)
		{
			// Get the body's markup which is equal to the border's markup
			// <span wicket:id="myBorder"> xxxx </span>
			MarkupFragment borderFragment = Border.super.getMarkupFragment();
			MarkupStream borderMarkupStream = new MarkupStream(borderFragment);

			// Skip the <span wicket:id="myBorder> open tag
			borderMarkupStream.next();

			// Render wicket:body and its children
			renderComponentTagBody(borderMarkupStream, borderFragment.getTag());

			// If the open tag was Open-Body-Close than remove the body. The
			// body raw markup might be used as none-computed preview text.
			if (wasOpenClose == false)
			{
				markupStream.skipRawMarkup();
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15499.java