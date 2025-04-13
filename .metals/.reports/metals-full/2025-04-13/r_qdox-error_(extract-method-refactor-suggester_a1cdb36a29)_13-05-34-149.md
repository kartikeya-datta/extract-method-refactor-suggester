error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/20.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/20.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/20.java
text:
```scala
m@@arkupStream = ((Fragment< ? >)parentWithAssociatedMarkup).findComponentIndex(component.getId());

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
package org.apache.wicket;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.panel.Fragment;

/**
 * Responding to an AJAX request requires that we position the markup stream at the component
 * associated with the AJAX request. That is straight forward in most cases except for "transparent"
 * components and for components which implement there own IComponentResolver.
 * 
 * @author Juergen Donnerstag
 */
final class MarkupFragmentFinder
{
	/**
	 * Construct
	 */
	public MarkupFragmentFinder()
	{
	}

	/**
	 * Get the markup stream and position it at the component
	 * 
	 * @param component
	 * @return A MarkupStream which is positioned at the component
	 */
	final MarkupStream find(final Component< ? > component)
	{
		// Get the parent's associated markup stream.
		MarkupContainer< ? > parentWithAssociatedMarkup = component.findParentWithAssociatedMarkup();
		MarkupStream markupStream = null;

		// Might be that we have to walk up the component hierarchy
		while (true)
		{
			markupStream = parentWithAssociatedMarkup.getAssociatedMarkupStream(true);

			// In case the component has already been rendered, this is a
			// performance short cut. But actually this was necessary because
			// transparent containers and components which implement
			// IComponentResolver destroy the 1:1 match between component path
			// and markup path.
			if (component.markupIndex != -1)
			{
				// Might be that the markup has been reloaded and that the
				// position has changed. Make sure the component is still
				// available
				try
				{
					markupStream.setCurrentIndex(component.markupIndex);
					MarkupElement elem = markupStream.get();
					if (elem instanceof ComponentTag)
					{
						ComponentTag tag = (ComponentTag)elem;
						String id = tag.getId();
						if ((id != null) && id.equals(component.getId()))
						{
							// Ok, found it
							return markupStream;
						}
					}
				}
				catch (IndexOutOfBoundsException ex)
				{
					// fall through. Don't do anything
				}
			}

			// Make sure the markup stream is positioned at the correct element
			String relativePath = getComponentRelativePath(component, parentWithAssociatedMarkup);

			// If the component is defined in the markup
			int index = markupStream.findComponentIndex(relativePath, component.getId());
			if (index != -1)
			{
				// than position the stream at the beginning of the component
				markupStream.setCurrentIndex(index);
				return markupStream;
			}

			if (parentWithAssociatedMarkup instanceof Fragment)
			{
				markupStream = ((Fragment)parentWithAssociatedMarkup).findComponentIndex(component.getId());
				return markupStream;
			}

			// Yet another exception for Border in the code base.
			// However if the container with the markup is a Border, than
			// ...
			if (parentWithAssociatedMarkup instanceof Border)
			{
				parentWithAssociatedMarkup = parentWithAssociatedMarkup.findParentWithAssociatedMarkup();
			}
			else
			{
				throw new WicketRuntimeException(
					"Unable to find the markup for the component. That may be due to transparent containers or components implementing IComponentResolver: " +
						component.toString());
			}

			// Not found, reset the stream
			markupStream = null;
		}
	}

	/**
	 * Get component path relative to the parent container with associated markup
	 * 
	 * @param component
	 * @param parentWithAssociatedMarkup
	 * @return the relative path
	 */
	private String getComponentRelativePath(final Component< ? > component,
		final MarkupContainer< ? > parentWithAssociatedMarkup)
	{
		final String componentPath = component.getParent().getPageRelativePath();
		final String parentWithAssociatedMarkupPath = parentWithAssociatedMarkup.getPageRelativePath();
		String relativePath = componentPath.substring(parentWithAssociatedMarkupPath.length());
		if (relativePath.startsWith(":"))
		{
			relativePath = relativePath.substring(1);
		}
		return relativePath;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/20.java