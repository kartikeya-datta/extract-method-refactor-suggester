error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1175.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1175.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1175.java
text:
```scala
public M@@arkupFragment getMarkupFragment(final String id)

/*
 * $Id$ $Revision$
 * $Date$
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
package wicket.markup.html.panel;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupFragment;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainerWithAssociatedMarkup;
import wicket.markup.html.internal.HeaderContainer;
import wicket.markup.parser.XmlTag;
import wicket.markup.parser.filter.WicketTagIdentifier;
import wicket.model.IModel;

/**
 * A panel is a reusable component that holds markup and other components.
 * <p>
 * Whereas WebMarkupContainer is an inline container like
 * 
 * <pre>
 *      ...
 *      &lt;span wicket:id=&quot;xxx&quot;&gt;
 *        &lt;span wicket:id=&quot;mylabel&quot;&gt;My label&lt;/span&gt;
 *        ....
 *      &lt;/span&gt;
 *      ...
 * </pre>
 * 
 * a Panel has its own associated markup file and the container content is taken
 * from that file, like:
 * 
 * <pre>
 *      &lt;span wicket:id=&quot;mypanel&quot;/&gt;
 *     
 *      TestPanel.html
 *      &lt;wicket:panel&gt;
 *        &lt;span wicket:id=&quot;mylabel&quot;&gt;My label&lt;/span&gt;
 *        ....
 *      &lt;/wicket:panel&gt;
 * </pre>
 * 
 * @param <T>
 *            Type of model object this component holds
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public class Panel<T> extends WebMarkupContainerWithAssociatedMarkup<T>
{
	private static final long serialVersionUID = 1L;
	
	private static final String PANEL = "panel";

	static
	{
		// register "wicket:panel"
		WicketTagIdentifier.registerWellKnownTagName(PANEL);
	}

	/** If if tag was an open-close tag */
	private boolean wasOpenCloseTag = false;

	/**
	 * @see wicket.Component#Component(MarkupContainer,String)
	 */
	public Panel(MarkupContainer parent, final String id)
	{
		super(parent, id);
	}

	/**
	 * @see wicket.Component#Component(MarkupContainer,String, IModel)
	 */
	public Panel(MarkupContainer parent, final String id, final IModel<T> model)
	{
		super(parent, id, model);
	}

	/**
	 * Get the child markup fragment with the 'id'
	 *  
	 * @param id
	 * @return MarkupFragment
	 */
	protected MarkupFragment getMarkupFragment(final String id)
	{
		return getAssociatedMarkup(true).getChildFragment(id, true);
	}

	/**
	 * 
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag)
	{
		if (tag.isOpenClose())
		{
			this.wasOpenCloseTag = true;

			// Convert <span wicket:id="myPanel" /> into
			// <span wicket:id="myPanel">...</span>
			tag.setType(XmlTag.Type.OPEN);
		}
		super.onComponentTag(tag);
	}

	/**
	 * 
	 * @see wicket.Component#onComponentTagBody(wicket.markup.MarkupStream,
	 *      wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		// Render the associated markup
		renderAssociatedMarkup(PANEL,
				"Markup for a panel component has to contain part '<wicket:panel>'");

		if (this.wasOpenCloseTag == false)
		{
			// Skip any raw markup in the body
			markupStream.skipRawMarkup();
		}
	}

	/**
	 * Check the associated markup file for a wicket header tag
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1175.java