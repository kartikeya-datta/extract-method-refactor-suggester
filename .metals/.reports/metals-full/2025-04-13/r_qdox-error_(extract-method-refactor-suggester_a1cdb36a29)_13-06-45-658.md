error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5000.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5000.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5000.java
text:
```scala
i@@f(sortingComparator != null)

/*
 * $Id$
 * $Revision$
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
package wicket;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import wicket.model.AbstractDetachableModel;
import wicket.model.IModel;

/**
 * Model for extracting feedback messages.
 *
 * @author Eelco Hillenius
 */
public final class FeedbackMessagesModel extends AbstractDetachableModel
{
	/**
	 * Whether to include the messages of any nested component.
	 */
	private boolean includeNestedComponents;

	/**
	 * Optional collecting component. When this is not set explicitly, the first occurence
	 * of {@link IFeedbackBoundary} will be searched for higher up in the run-time
	 * hierarchy.
	 */
	private Component collectingComponent;

	/**
	 * Comparator used for sorting the messages.
	 */
	private Comparator sortingComparator;

	/** lazy loaded, temporary list. */
	private transient List current;

	/**
	 * Construct. The first occurence of {@link IFeedbackBoundary}
	 * 	will be searched for higher up in the run-time hierarchy for collecting messages
	 * @param includeNestedComponents
	 * 		Whether to include the messages of any nested component
	 */
	public FeedbackMessagesModel(boolean includeNestedComponents)
	{
		this(includeNestedComponents, null, null);
	}

	/**
	 * Construct.
	 * @param includeNestedComponents
	 * 		Whether to include the messages of any nested component
	 * @param collectingComponent Optional collecting component.
	 * 	When this is not set explicitly, the first occurence of {@link IFeedbackBoundary}
	 * 	will be searched for higher up in the run-time hierarchy.
	 */
	public FeedbackMessagesModel(boolean includeNestedComponents, Component collectingComponent)
	{
		this(includeNestedComponents, collectingComponent, null);
	}

	/**
	 * Construct.
	 * @param includeNestedComponents
	 * 		Whether to include the messages of any nested component
	 * @param collectingComponent Optional collecting component.
	 * 	When this is not set explicitly, the first occurence of {@link IFeedbackBoundary}
	 * 	will be searched for higher up in the run-time hierarchy.
	 * @param sortingComparator 
	 */
	public FeedbackMessagesModel(boolean includeNestedComponents,
			Component collectingComponent, Comparator sortingComparator)
	{
		this.includeNestedComponents = includeNestedComponents;
		this.collectingComponent = collectingComponent;
		this.sortingComparator = sortingComparator;
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onGetObject(wicket.Component)
	 */
	public Object onGetObject(Component component)
	{
		if(current != null)
		{
			return current;
		}

		// get the message queue
		Page page = component.getPage();
		FeedbackMessages feedbackMessages = page.getFeedbackMessages();

		// if the queue is empty, just return an empty list
		if(feedbackMessages.isEmpty())
		{
			current = Collections.EMPTY_LIST;
		}
		else
		{
			final Component collector;
			if(collectingComponent != null)
			{
				// use the one that was explicitly set
				collector = collectingComponent;
			}
			else
			{
				// find the feedback enabled component that nests the component that uses this model
				// for example, this could be a FeedbackPanel
				collector = component.findParent(IFeedbackBoundary.class);
			}
	
			// get the messages for the target component, recurse depending
			// on property 'includeNestedComponents'
			current = feedbackMessages.messages(collector, includeNestedComponents,
					true, FeedbackMessage.DEBUG);
		}

		// sort the list before returning it
		sort(current);

		return current;
	}

	/**
	 * Sorts the list if property sortingComparator was set, otherwise it
	 * doesn't do any sorting.
	 * @param list list to sort; contains elements of {@link FeedbackMessage}.
	 */
	protected void sort(List list)
	{
		if(sortingComparator != null);
		{
			Collections.sort(list, sortingComparator);
		}
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onDetach()
	 */
	protected void onDetach()
	{
		current = null;
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onAttach()
	 */
	protected void onAttach()
	{
		// ignore
	}

	/**
	 * @see wicket.model.AbstractDetachableModel#onSetObject(wicket.Component, java.lang.Object)
	 */
	protected void onSetObject(Component component, Object object)
	{
		// ignore
	}

	/**
	 * @see wicket.model.IModel#getNestedModel()
	 */
	public IModel getNestedModel()
	{
		return null;
	}

	/**
	 * Sets the optional collecting component. When this is not set explicitly, the first occurence
	 * of {@link IFeedbackBoundary} will be searched for higher up in the run-time
	 * hierarchy.
	 * @param collectingComponent the collecting component
	 */
	public void setCollectingComponent(Component collectingComponent)
	{
		this.collectingComponent = collectingComponent;
	}

	/**
	 * Sets the comparator used for sorting the messages.
	 * @param sortingComparator comparator used for sorting the messages
	 */
	public void setSortingComparator(Comparator sortingComparator)
	{
		this.sortingComparator = sortingComparator;
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5000.java