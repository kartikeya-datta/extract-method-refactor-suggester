error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13413.java
text:
```scala
M@@arkupFragment fragment = getAssociatedMarkup(false);

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

import wicket.Component;
import wicket.MarkupContainer;
import wicket.Response;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupFragment;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import wicket.markup.html.internal.HeaderContainer;
import wicket.markup.parser.XmlTag;
import wicket.markup.parser.filter.WicketTagIdentifier;
import wicket.markup.resolver.IComponentResolver;
import wicket.model.IModel;
import wicket.response.NullResponse;

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
 *    &lt;html&gt;
 *    &lt;body&gt;
 *      &lt;wicket:border&gt;
 *        First &lt;wicket:body/&gt; Last
 *      &lt;/wicket:border&gt;
 *    &lt;/body&gt;
 *    &lt;/html&gt;
 * </pre>
 * 
 * And the border was used on a page like this:
 * 
 * <pre>
 *    &lt;html&gt;
 *    &lt;body&gt;
 *      &lt;span wicket:id = &quot;myBorder&quot;&gt;
 *        Middle
 *      &lt;/span&gt;
 *    &lt;/body&gt;
 *    &lt;/html&gt;
 * </pre>
 * 
 * Then the resulting HTML would look like this:
 * 
 * <pre>
 *    &lt;html&gt;
 *    &lt;body&gt;
 *      First Middle Last
 *    &lt;/body&gt;
 *    &lt;/html&gt;
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
 */
public abstract class Border<T> extends WebMarkupContainerWithAssociatedMarkup<T>
		implements
			IComponentResolver
{
	private static final String BODY = "body";
	private static final String BORDER = "border";

	static
	{
		// register "wicket:fragement"
		WicketTagIdentifier.registerWellKnownTagName(BORDER);
		WicketTagIdentifier.registerWellKnownTagName(BODY);
	}

	/** If false, the content of <wicket:body> will not be printed */
	private boolean bodyVisible = true;

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
	}

	/**
	 * Get the Border's body container
	 * 
	 * @return MarkupContainer
	 */
	protected MarkupContainer getBodyContainer()
	{
		return this.body;
	}

	/**
	 * Create a new Border Body container and add it to the 'parent'
	 * 
	 * @param parent
	 * @return MarkupContainer
	 */
	protected MarkupContainer newBorderBodyContainer(final MarkupContainer parent)
	{
		if (this.body != null)
		{
			this.body.remove();
		}

		this.body = new BorderBody(parent);
		this.body.setRenderBodyOnly(!this.bodyVisible);
		return body;
	}

	/**
	 * When this method is called with a false value the components and raw
	 * markup that this border wraps will not be rendered.
	 * 
	 * @param bodyVisible
	 * @return this for chaining
	 */
	public Border setBorderBodyVisible(boolean bodyVisible)
	{
		this.bodyVisible = bodyVisible;
		return this;
	}

	/**
	 * Like Panels, Borders have associated Markup files and hence must
	 * implement IMarkupProvider. But Border are different in that they allow to
	 * have child components with markup either in the associated markup file
	 * (between the wicket:border and wicket:body tags) or the span tag which
	 * declares the border component.
	 * 
	 * @see wicket.markup.html.WebMarkupContainerWithAssociatedMarkup#getMarkupFragment(java.lang.String)
	 */
	@Override
	public MarkupFragment getMarkupFragment(String path)
	{
		// First try the associated markup
		MarkupFragment fragment = getAssociatedMarkupFragment(false);
		if (fragment != null)
		{
			fragment = fragment.getChildFragment(path, false);
		}

		if (fragment == null)
		{
			fragment = super.getMarkupFragment(path);
		}

		return fragment;
	}

	/**
	 * Render the tag body
	 * 
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

		// Skip the whole body of <span wicket:id="myBorder>
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
		if ((this.body == null) && tag.isWicketBodyTag())
		{
			this.body = newBorderBodyContainer(container);
			this.body.render(markupStream);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @see wicket.Component#renderHead(wicket.markup.html.internal.HeaderContainer)
	 */
	@Override
	public void renderHead(HeaderContainer container)
	{
		if (isHeadRendered() == false)
		{
			this.renderHeadFromAssociatedMarkupFile(container);
		}
		super.renderHead(container);
	}

	/**
	 * The wicket:body container
	 */
	private class BorderBody extends WebMarkupContainer implements IComponentResolver
	{
		private static final long serialVersionUID = 1L;

		private transient boolean wasOpenClose = false;

		/**
		 * Construct.
		 * 
		 * @param parent
		 */
		public BorderBody(final MarkupContainer parent)
		{
			super(parent, Component.AUTO_COMPONENT_PREFIX + BODY);
		}

		/**
		 * @see wicket.Component#getMarkupFragment()
		 */
		@Override
		public MarkupFragment getMarkupFragment()
		{
			// Get the border's markup
			return Border.this.getMarkupFragment();
		}

		/**
		 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
		 */
		@Override
		protected void onComponentTag(final ComponentTag tag)
		{
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
			// Get the borders markup
			MarkupFragment borderFragment = getMarkupFragment();
			MarkupStream borderMarkupStream = new MarkupStream(borderFragment);

			// Skip the <span wicket:id="myBorder> tag
			borderMarkupStream.next();

			// Render the borders body
			final Response originalResponse;
			if (Border.this.bodyVisible == true)
			{
				originalResponse = null;
			}
			else
			{
				originalResponse = getRequestCycle().setResponse(NullResponse.getInstance());
			}

			try
			{
				renderComponentTagBody(borderMarkupStream, borderFragment.getTag(0));
			}
			finally
			{
				if (originalResponse != null)
				{
					getRequestCycle().setResponse(originalResponse);
				}
			}

			if (wasOpenClose == false)
			{
				markupStream.skipRawMarkup();
			}
		}

		/**
		 * @see wicket.markup.resolver.IComponentResolver#resolve(wicket.MarkupContainer,
		 *      wicket.markup.MarkupStream, wicket.markup.ComponentTag)
		 */
		public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
				final ComponentTag tag)
		{
			if (tag.isWicketBodyTag())
			{
				return false;
			}
			
			Component child = getParent().get(tag.getId());
			if (child != null)
			{
				child.render(markupStream);
				return true;
			}
			return false;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13413.java