error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15714.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15714.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15714.java
text:
```scala
c@@hild.render(getMarkupStream());

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.extensions.markup.html.repeater;

import java.util.Iterator;

import wicket.Component;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;

/**
 * <p>
 * A repeater view that renders all of its children, using its body markup, in
 * no specified order.
 * </p>
 * Example:
 * <p>
 * <u>Java:</u>
 * 
 * <pre>
 * RepeatingView view = new RepeatingView(&quot;repeater&quot;);
 * view.add(new Label(&quot;1&quot;, &quot;hello&quot;));
 * view.add(new Label(&quot;2&quot;, &quot;goodbye&quot;));
 * view.add(new Label(&quot;3&quot;, &quot;good morning&quot;));
 * </pre>
 * 
 * </p>
 * <p>
 * <u>Markup:</u>
 * 
 * <pre>
 *        &lt;ul&gt;&lt;li wicket:id=&quot;repeater&quot;&gt;&lt;/li&gt;&lt;/ul&gt;
 * </pre>
 * 
 * </p>
 * <p>
 * <u>Yields:</u>
 * 
 * <pre>
 *        &lt;ul&gt;&lt;li&gt;goodbye&lt;/li&gt;&lt;li&gt;hello&lt;/li&gt;&lt;li&gt;good morning&lt;/li&gt;&lt;/ul&gt;
 * </pre>
 * 
 * @author Igor Vaynberg ( ivaynberg )
 * 
 */
public class RepeatingView extends WebMarkupContainer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Counter used for generating unique child component ids. */
	private long childIdCounter = 0;

	/** @see Component#Component(String) */
	public RepeatingView(String id)
	{
		super(id);
	}

	/** @see Component#Component(String, IModel) */
	public RepeatingView(String id, IModel model)
	{
		super(id, model);
	}

	/**
	 * Generates a unique id string. This makes it easy to add items to be
	 * rendered w/out having to worry about generating unique id strings in your
	 * code.
	 * 
	 * @return unique child id
	 */
	public String newChildId()
	{
		childIdCounter++;

		if (childIdCounter == Long.MAX_VALUE)
		{
			// mmm yeah...like this will ever happen
			throw new RuntimeException("generateChildId() out of space.");
		}

		return String.valueOf(childIdCounter);
	}

	/**
	 * Renders all child items in no specified order
	 * 
	 * @param markupStream The markup stream
	 */
	protected void onRender(final MarkupStream markupStream)
	{
		final int markupStart = markupStream.getCurrentIndex();

		Iterator it = renderIterator();
		if (it.hasNext())
		{
			do
			{
				markupStream.setCurrentIndex(markupStart);
				renderChild((Component)it.next());
			}
			while (it.hasNext());
		}
		else
		{
			markupStream.skipComponent();
		}
	}

	/**
	 * Returns an iterator for the collection of child components to be
	 * rendered.
	 * 
	 * Child component are rendered in the order they are in the iterator. Since
	 * we use the iterator returned by wicket's
	 * <code>MarkupContainer#iterator()</code> method and that method does not
	 * guarantee any kind of ordering neither do we. This method can be
	 * overridden by subclasses to create an ordering scheme, see
	 * <code>OrderedRepeatingView#renderIterator()</code>.
	 * 
	 * @return iterator over child components to be rendered
	 */
	protected Iterator renderIterator()
	{
		return iterator();
	}

	/**
	 * Render a single child. This method can be overridden to modify how a
	 * single child component is rendered.
	 * 
	 * @param child
	 *            Child component to be rendered
	 */
	protected void renderChild(final Component child)
	{
		child.render();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15714.java