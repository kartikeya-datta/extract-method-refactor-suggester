error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,2]

error in qdox parser
file content:
```java
offset: 2
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11160.java
text:
```scala
!(@@markupStream.atOpenCloseTag("region")

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
package wicket.markup.html.border;

import wicket.Container;
import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.ComponentWicketTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.HtmlContainer;


/**
 * A border component has associated markup which is drawn and determines placement of any
 * markup and/or components nested within the border component.
 * <p>
 * The portion of the border's associated markup file which is to be used in rendering the
 * border is denoted by a tag (of any type) with a component name of "[border]". The
 * children of the border component instance are then inserted into this markup at the
 * first tag named "[body]" in the border's associated markup.
 * <p>
 * For example, if a border's associated markup looked like this:
 * <p>
 * 
 * <pre>
 * 
 * 
 * 
 *      &lt;html&gt;
 *      &lt;body&gt;
 *        &lt;span componentName = &quot;[border]&quot;&gt;
 *            First &lt;span componentName = &quot;[body]&quot;/&gt; Last
 *        &lt;/span&gt;
 *      &lt;/body&gt;
 *      &lt;/html&gt;
 * 
 * 
 *  
 * </pre>
 * 
 * <p>
 * And the border was used on a page like this:
 * <p>
 * 
 * <pre>
 * 
 * 
 * 
 *      &lt;html&gt;
 *      &lt;body&gt;
 *        &lt;span componentName = &quot;myBorder&quot;&gt;
 *            Middle
 *        &lt;/span&gt;
 *      &lt;/body&gt;
 *      &lt;/html&gt;
 * 
 * 
 *  
 * </pre>
 * 
 * <p>
 * Then the resulting HTML would look like this:
 * <p>
 * 
 * <pre>
 * 
 * 
 * 
 *      &lt;html&gt;
 *      &lt;body&gt;
 *        &lt;span componentName = &quot;[border]&quot;&gt;
 *            First Middle Last
 *        &lt;/span&gt;
 *      &lt;/body&gt;
 *      &lt;/html&gt;
 * 
 * 
 *  
 * </pre>
 * 
 * <p>
 * In other words, the body of the myBorder component is substituted into the border's
 * associated markup at the position indicated by the "[body]" component.
 * @author Jonathan Locke
 */
public abstract class Border extends HtmlContainer
{
    /** The open tag for this border component. */
    private ComponentTag openTag;

    /**
     * Constructor.
     * @param componentName Name of border component
     */
    public Border(final String componentName)
    {
        super(componentName);
    }

    /**
     * @see wicket.Component#handleBody(wicket.RequestCycle,
     *      wicket.markup.MarkupStream,
     *      wicket.markup.ComponentTag)
     */
    protected final void handleBody(final RequestCycle cycle, final MarkupStream markupStream,
            final ComponentTag openTag)
    {
        // Save open tag for callback later to render body
        this.openTag = openTag;

        // Render the associated markup
        renderAssociatedMarkup(cycle, "border",
                "Markup for a border component must begin a component named '[border]'");
    }

    /**
     * Returns the open tag for this border component.
     * @return The open tag.
     */
    public final ComponentTag getOpenTag()
    {
        return openTag;
    }
    

	/**
	 * Border makes use of [body] component names to indentify the position
	 * where to insert the border's body. As [body] is a special component
	 * name and Container is not able to handle it, we will do.
	 *  
	 * @param cycle The current request cycle
	 * @param markupStream The current markup stream
	 * @param tag The current component tag
	 * @return true, if Container was able to resolve the component name 
	 * 		and to render the component
	 */
	protected boolean resolveComponent(final RequestCycle cycle, final MarkupStream markupStream, final ComponentTag tag)
	{
		// If it's a [body] tag
		if (!tag.getComponentName().equals("[body]") &&
		        !(markupStream.atOpenTag("region") 
		                && "body".equalsIgnoreCase(((ComponentWicketTag)tag).getNameAttribute())))
		{
		    return false;
		}
		
		if (!markupStream.atOpenCloseTag())
		{
			markupStream.throwMarkupException("A <wicket:region name=body> tag must be an open-close tag.");
		}
		
		// Render the children tag
		renderTag(cycle, tag);
		markupStream.next();

		// Find nearest Border at or above this container
		Border border = (Border) ((this instanceof Border) ? this : findParent(Border.class));

		// If markup stream is null, that indicates we already
		// recursed into
		// this block of log and set it to null (below). If we did
		// that,
		// then we want to go up another level of border nesting.
		if (border.getMarkupStream() == null)
		{
			// Find Border at or above parent of this border
			Container borderParent = border.getParent();

			border = (Border) ((borderParent instanceof Border) ? borderParent : borderParent
					.findParent(Border.class));
		}

		// Get the border's markup
		final MarkupStream borderMarkup = border.findMarkupStream();

		// Set markup of border to null. This allows us to find the
		// border's
		// parent's markup. It also indicates that we've been here
		// in the log
		// just above.
		border.setMarkupStream(null);

		// Draw the children of the border component using its
		// original in-line
		// markup stream (not the border's associated markup stream)
		border.renderBody(cycle, border.findMarkupStream(), border.getOpenTag());

		// Restore border markup so it can continue rendering
		border.setMarkupStream(borderMarkup);
		
		return true;
	}
}

///////////////////////////////// End of File /////////////////////////////////
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11160.java