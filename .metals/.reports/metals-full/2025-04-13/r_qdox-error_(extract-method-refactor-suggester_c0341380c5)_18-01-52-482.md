error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1217.java
text:
```scala
t@@ag.setId("_" + tag.getName());

/*
 * $Id$
 * $Revision$ $Date$
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
import java.util.ArrayList;
import java.util.List;

import wicket.markup.ComponentTag;
import wicket.markup.Markup;
import wicket.markup.MarkupElement;
import wicket.markup.WicketTag;
import wicket.markup.parser.AbstractMarkupFilter;
import wicket.markup.parser.IMarkupFilter;
import wicket.markup.parser.XmlTag;

/**
 * This is a markup inline filter. It identifies xml tags which have a special
 * meaning for Wicket. There are two type of tags which have a special meaning
 * for Wicket.
 * <p>
 * <ul>
 * <li>All tags with Wicket namespace, e.g. &lt;wicket:remove&gt;</li>
 * <li>All tags with an attribute like wicket:id="myLabel" </li>
 * </ul>
 * 
 * @author Juergen Donnerstag
 */
public final class WicketTagIdentifier extends AbstractMarkupFilter
{
	/** List of well known wicket tag namses */
	private static List wellKnownTagNames;

	/** The current markup needed to get the markups namespace */
	private final Markup markup;

	/**
	 * Construct.
	 * 
	 * @param markup
	 *            The markup as known by now
	 * @param parent
	 *            The next MarkupFilter in the chain
	 */
	public WicketTagIdentifier(final Markup markup, final IMarkupFilter parent)
	{
		super(parent);
		this.markup = markup;
	}

	/**
	 * Get the next tag from the next MarkupFilter in the chain and search for
	 * Wicket specific tags.
	 * <p>
	 * Note: The xml parser - the next MarkupFilter in the chain - returns
	 * XmlTags which are a subclass of MarkupElement. The implementation of this
	 * filter will return either ComponentTags or ComponentWicketTags. Both are
	 * subclasses of MarkupElement as well and both maintain a reference to the
	 * XmlTag. But no XmlTag is returned.
	 * 
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next tag from markup to be processed. If null, no more tags
	 *         are available
	 */
	public MarkupElement nextTag() throws ParseException
	{
		// Get the next tag from the markup.
		// If null, no more tags are available
		XmlTag xmlTag = (XmlTag)getParent().nextTag();
		if (xmlTag == null)
		{
			return xmlTag;
		}

		final String namespace = this.markup.getWicketNamespace();

		// Identify tags with Wicket namespace
		ComponentTag tag;
		if (namespace.equalsIgnoreCase(xmlTag.getNamespace()))
		{
			// It is <wicket:...>
			tag = new WicketTag(xmlTag);

			// Make it a wicket component. Otherwise it would be RawMarkup
			tag.setId(tag.getName());

			if (wellKnownTagNames.contains(xmlTag.getName()) == false)
			{
				throw new ParseException("Unkown tag name with Wicket namespace: '"
						+ xmlTag.getName()
						+ "'. Might be you haven't installed the appropriate resolver?", tag
						.getPos());
			}
		}
		else
		{
			// Everything else, except tags with Wicket namespace
			tag = new ComponentTag(xmlTag);
		}

		// If the form <tag wicket:id = "value"> is used
		final String value = tag.getAttributes().getString(namespace + ":id");
		if (value != null)
		{
			if (value.trim().length() == 0)
			{
				throw new ParseException(
						"The wicket:id attribute value must not be empty. May be unmatched quotes?!?",
						tag.getPos());
			}
			// Make it a wicket component. Otherwise it would be RawMarkup
			tag.setId(value);
		}

		return tag;
	}

	/**
	 * Register a new well known wicket tag name (e.g. panel)
	 * 
	 * @param name
	 */
	public final static void registerWellKnownTagName(final String name)
	{
		if (wellKnownTagNames == null)
		{
			wellKnownTagNames = new ArrayList();
		}

		if (wellKnownTagNames.contains(name) == false)
		{
			wellKnownTagNames.add(name);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1217.java