error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5808.java
text:
```scala
W@@icketTagIdentifier.registerWellKnownTagName("remove");

/*
 * $Id: WicketRemoveTagHandler.java,v 1.4 2005/01/23 17:45:03 jdonnerstag
 * Exp $ $Revision$ $Date$
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
package wicket.markup.parser.filter;

import java.text.ParseException;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupElement;
import wicket.markup.WicketTag;
import wicket.markup.parser.AbstractMarkupFilter;
import wicket.markup.parser.IMarkupFilter;

/**
 * This is a markup inline filter. It identifies preview regions useful for HTML
 * designers to design the page. But they must be removed prior to sending the
 * markup to the client. Preview regions are enclosed by &lt;wicket:remove&gt;
 * tags.
 * 
 * @author Juergen Donnerstag
 */
public final class WicketRemoveTagHandler extends AbstractMarkupFilter
{
	/** Flag value to use as component name for ignored components */
	public static final String IGNORE = "<<Removed by WicketRemoveTagHandler>>";

	static
	{
		// register "wicket:fragement"
		WicketTagIdentifier.registerWellKownTagName("remove");
	}

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The next MarkupFilter in the processing chain
	 */
	public WicketRemoveTagHandler(final IMarkupFilter parent)
	{
		super(parent);
	}

	/**
	 * Removes preview regions enclosed by &lt;wicket:remove&gt; tags. Note that
	 * for obvious reasons, nested components are not allowed.
	 * 
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next tag to be processed. Null, if not more tags are
	 *         available
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		// Get the next tag from the next MarkupFilter in the chain
		// If null, no more tags are available
		final ComponentTag openTag = (ComponentTag)getParent().nextTag();
		if (openTag == null)
		{
			return openTag;
		}

		// If it is not a remove tag, than we are finished
		if (!(openTag instanceof WicketTag)
 !((WicketTag)openTag).isRemoveTag())
		{
			return openTag;
		}

		// remove tag must not be open-close tags
		if (openTag.isOpenClose())
		{
			throw new ParseException("Wicket remove tag must not be an open-close tag: "
					+ openTag.toUserDebugString(), openTag.getPos());
		}

		// Find the corresponding close tag and remove all tags in between
		ComponentTag closeTag;
		while (null != (closeTag = (ComponentTag)getParent().nextTag()))
		{
			// No Wicket component tags are allowed within the preview region.
			// Wicket components will a component name assigned.
			if (closeTag.getId() == null)
			{
				continue;
			}

			// The first Wicket component following the preview region open
			// tag, must be it's corresponding close tag.
			if (closeTag.closes(openTag))
			{
				// Component's named with the IGNORE component name will be ignored 
				// by MarkupParser and not added to the Markup.
				openTag.setId(IGNORE);
				return openTag;
			}

			throw new ParseException(
					"Markup remove regions must not contain Wicket component tags. " + "tag: "
							+ closeTag.toUserDebugString(), closeTag.getPos());
		}

		throw new ParseException("Did not find close tag for markup remove region. "
				+ "Open tag: " + openTag.toUserDebugString(), openTag.getPos());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5808.java