error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9801.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9801.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,130]

error in qdox parser
file content:
```java
offset: 130
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9801.java
text:
```scala
". Enable debug messages for org.apache.wicket.util.resource.locator.ResourceStreamLocator to get a list of all filenames tried.")@@;

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
package org.apache.wicket.markup.loader;

import java.io.IOException;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupFactory;
import org.apache.wicket.markup.MarkupNotFoundException;
import org.apache.wicket.markup.MarkupResourceStream;
import org.apache.wicket.markup.MergedMarkup;
import org.apache.wicket.markup.TagUtils;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Merge the 2+ markups involved in markup inheritance. From a users perspective there is only one
 * markup associated with the component, the merged one.
 * 
 * @author Juergen Donnerstag
 */
public class InheritedMarkupMarkupLoader implements IMarkupLoader
{
	/**
	 * Constructor.
	 */
	public InheritedMarkupMarkupLoader()
	{
	}

	/**
	 * Load the markup from the resource stream with the base MarkupLoader provided, than check if
	 * markup inheritance must be applied. If yes, than load the base markup and merge them.
	 */
	public final Markup loadMarkup(final MarkupContainer container,
		final MarkupResourceStream markupResourceStream, final IMarkupLoader baseLoader,
		final boolean enforceReload) throws IOException, ResourceStreamNotFoundException
	{
		// read and parse the markup
		Markup markup = baseLoader.loadMarkup(container, markupResourceStream, null, enforceReload);

		// Check if markup contains <wicket:extend> which tells us that
		// we need to read the inherited markup as well.
		int extendIndex = requiresBaseMarkup(markup);
		if (extendIndex == -1)
		{
			return markup;
		}

		// Load the base markup
		final Markup baseMarkup = getBaseMarkup(container, markup, enforceReload);
		if ((baseMarkup == null) || (baseMarkup == Markup.NO_MARKUP))
		{
			throw new MarkupNotFoundException(
				"Base markup of inherited markup not found. Component class: " +
					markup.getMarkupResourceStream()
						.getContainerInfo()
						.getContainerClass()
						.getName() +
					" Enable debug messages for org.apache.wicket.util.resource.Resource to get a list of all filenames tried.");
		}

		// Merge base and derived markup
		return new MergedMarkup(markup, baseMarkup, extendIndex);
	}

	/**
	 * Load the base markup
	 * 
	 * @param container
	 * @param markup
	 * @param enforceReload
	 * @return the base markup
	 */
	private Markup getBaseMarkup(final MarkupContainer container, final Markup markup,
		final boolean enforceReload)
	{
		final Class<?> location = markup.getMarkupResourceStream().getMarkupClass().getSuperclass();

		// get the base markup
		return MarkupFactory.get().getMarkup(container, location, enforceReload);
	}

	/**
	 * Check if markup contains &lt;wicket:extend&gt; which tells us that we need to read the
	 * inherited markup as well. &lt;wicket:extend&gt; MUST BE the first wicket tag in the markup.
	 * Skip raw markup
	 * 
	 * @param markup
	 * @return == 0, if no wicket:extend was found
	 */
	private int requiresBaseMarkup(final IMarkupFragment markup)
	{
		for (int i = 0; i < markup.size(); i++)
		{
			if (TagUtils.isExtendTag(markup, i))
			{
				// Ok, inheritance is on and we must get the
				// inherited markup as well.
				return i;
			}
		}
		return -1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9801.java