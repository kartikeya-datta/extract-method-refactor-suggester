error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13249.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13249.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13249.java
text:
```scala
i@@f (tag.getId() == null)

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.ComponentTag;
import wicket.markup.ComponentWicketTag;
import wicket.markup.MarkupElement;
import wicket.markup.MarkupException;
import wicket.markup.parser.AbstractMarkupFilter;
import wicket.markup.parser.IMarkupFilter;
import wicket.markup.parser.IXmlPullParser;

/**
 * This is a markup inline filter. It identifies Wicket parameter tags like
 * &lt;wicket:param key=value/&gt; and assigns the key/value pair to the
 * attribute list of the immediately preceding Wicket component.
 * <p>
 * Example:
 * 
 * <pre>
 *    &lt;table&gt;&lt;tr id=&quot;wicket-myTable&quot;&gt;
 *      &lt;wicket:param rowsPerPage=10/&gt;
 *      ...
 *    &lt;/tr&gt;&lt;/table&gt;
 * </pre>
 * 
 * @author Juergen Donnerstag
 */
public final class WicketParamTagHandler extends AbstractMarkupFilter
{
	/** Logging */
	private final static Log log = LogFactory.getLog(WicketParamTagHandler.class);

	/** The last Wicket component tag found in markup */
	private ComponentTag componentTag;

	/** The tag immediately preceeding the current tag */
	private ComponentTag lastTag;

	/** The last element in the chain of MarkupFilters must be a IXmlPullParser */
	private IXmlPullParser xmlParser;

	/** True, if wicket param tags shall be removed from output */
	private boolean stripWicketTag = true;

	/**
	 * Construct.
	 * 
	 * @param parent
	 *            The next MarkupFilter in the chain
	 */
	public WicketParamTagHandler(final IMarkupFilter parent)
	{
		super(parent);

		// Find the XML parser at the end of the chain
		IMarkupFilter parser = parent.getParent();
		while (parser.getParent() != null)
		{
			parser = parser.getParent();
		}
		this.xmlParser = (IXmlPullParser)parser;
	}

	/**
	 * Enable/disable removing Wicket param tags
	 * 
	 * @param strip
	 *            True, if Wicket param tags shall be removed
	 */
	public void setStripWicketTag(final boolean strip)
	{
		this.stripWicketTag = strip;
	}

	/**
	 * Get the next tags from the next MarkupFilter in the chain. Identify
	 * wicket param tags and handle them as described above.
	 * <p>
	 * Note: IXmlPullParser which is the last element in the chain returns
	 * XmlTag objects which are derived from MarkupElement.
	 * WicketParamTagHandler hwoever assumes that the next MarkupFilter in the
	 * chain returns either ComponentTags or ComponentWicketTags. Both are
	 * subclasses of MarkupElement as well. Thus, WicketParamTagHandler can not
	 * be the first MarkupFilter immediately following the IXmlPullParser. Some
	 * inline filter converting XmlTags into ComponentTags must preceed it.
	 * 
	 * @see wicket.markup.parser.IMarkupFilter#nextTag()
	 * @return The next MarkupElement from markup. If null, no more tags are
	 *         available
	 */
	public final MarkupElement nextTag() throws ParseException
	{
		// The next tag from markup. If null, no more tags are available.
		// NOTE: WE ARE EXPECTING ComponentTags. See the comment above.
		final ComponentTag tag = (ComponentTag)getParent().nextTag();
		if (tag == null)
		{
			return tag;
		}

		// Handle the current tag and remember it
		lastTag = handleNext(tag);
		return lastTag;
	}

	/**
	 * Handle wicket param tags.
	 * 
	 * @param tag
	 *            The current tag
	 * @return The next MarkupElement to be processed
	 * @throws ParseException
	 */
	private ComponentTag handleNext(ComponentTag tag) throws ParseException
	{
		// Ignore all close tags. Wicket params tags must not be close tags
		// and components preceding the param tag can not be close tags
		// either.
		if (tag.isClose())
		{
			return tag;
		}

		// Wicket component tags will have a component name assigned.
		// Ignore all none wicket components.
		if (tag.getComponentId() == null)
		{
			// Reset the last tag seen. Null meaning: the last tag was
			// no wicket tag.
			componentTag = null;
			return tag;
		}

		// By now we know it is a wicket component tag. If it is no
		// wicket param tag, than remember it and we are done.
		if (!(tag instanceof ComponentWicketTag) || !((ComponentWicketTag)tag).isParamTag())
		{
			componentTag = tag;
			return tag;
		}

		// By now we know it is a Wicket param tag.
		// Only empty TEXT is allowed in between the component tag the
		// param tag.
		final CharSequence rawMarkup = xmlParser.getInputSubsequence(lastTag.getPos()
				+ lastTag.getLength(), tag.getPos());

		if (rawMarkup.length() > 0)
		{
			String text = rawMarkup.toString();
			text = text.replaceAll("[\\r\\n]+", "").trim();

			if (text.length() > 0)
			{
				throw new MarkupException("There must not be any text between a component tag and "
						+ "it's related param tag. Only spaces and line breaks are allowed.");
			}
		}

		// TODO: <wicket:params name = "myProperty">My completely free text that
		// can contain everything</wicket:params> is currently not supported

		// Add the parameters to the previous component tag
		lastTag.putAll(tag.getAttributes());

		// If wicket param tags shall not be included in the output, than
		// go ahead and process the next one.

		if (stripWicketTag == true)
		{
			tag = (ComponentTag)getParent().nextTag();
		}
		else
		{
			// E.g. An "Expected close tag" exception will be thrown if the
			// component uses replaceComponentTagBody() to replace the body
			// of the component (see src/test/.../MyLabel.html).
			log.debug("Be careful. Not stripping <wicket:param> may cause subtle errors.");
		}

		return tag;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13249.java