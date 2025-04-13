error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10841.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10841.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10841.java
text:
```scala
i@@f (markup != null)

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
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.parser.filter.WicketTagIdentifier;

/**
 * 
 * @author Juergen Donnerstag
 */
public class PanelMarkupSourcingStrategy extends AssociatedMarkupSourcingStrategy
{
	/** */
	public static final String PANEL = "panel";

	static
	{
		// register "wicket:panel"
		WicketTagIdentifier.registerWellKnownTagName(PANEL);
	}

	/**
	 * Constructor.
	 */
	public PanelMarkupSourcingStrategy()
	{
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onComponentTagBody(final Component component, final MarkupStream markupStream,
		final ComponentTag openTag)
	{
		super.onComponentTagBody(component, markupStream, openTag);

		// Render the associated markup
		((MarkupContainer)component).renderAssociatedMarkup(PANEL,
			"Markup for a panel component has to contain part '<wicket:panel>'");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMarkupFragment getMarkup(final MarkupContainer parent, final Component child)
	{
		IMarkupFragment markup = parent.getAssociatedMarkup();
		if (markup == null)
		{
			throw new MarkupNotFoundException("Failed to find markup file associated. " +
				parent.getClass().getSimpleName() + ": " + parent.toString());
		}

		// Find <wicket:panel>
		IMarkupFragment panelMarkup = findPanelTag(markup);
		if (panelMarkup == null)
		{
			throw new MarkupNotFoundException(
				"Expected to find <wicket:panel> in associated markup file. Markup: " +
					markup.toString());
		}

		// If child == null, than return the markup fragment starting with <wicket:panel>
		if (child == null)
		{
			return panelMarkup;
		}

		// Find the markup for the child component
		markup = panelMarkup.find(child.getId());
		if ((child == null) || (markup != null))
		{
			return markup;
		}

		return findMarkupInAssociatedFileHeader(parent, child);
	}

	/**
	 * Search for &lt;wicket:panel ...&gt; on the same level.
	 * 
	 * @param markup
	 * @return null, if not found
	 */
	private final static IMarkupFragment findPanelTag(final IMarkupFragment markup)
	{
		MarkupStream stream = new MarkupStream(markup);

		while (stream.skipUntil(ComponentTag.class))
		{
			ComponentTag tag = stream.getTag();
			if (tag.isOpen() || tag.isOpenClose())
			{
				if (tag instanceof WicketTag)
				{
					WicketTag wtag = (WicketTag)tag;
					if (wtag.isPanelTag())
					{
						return stream.getMarkupFragment();
					}
				}
				stream.skipToMatchingCloseTag(tag);
			}

			stream.next();
		}

		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10841.java