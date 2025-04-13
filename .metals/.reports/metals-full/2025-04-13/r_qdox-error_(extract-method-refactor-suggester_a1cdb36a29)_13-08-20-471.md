error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5503.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5503.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5503.java
text:
```scala
public v@@oid onClick()

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
package wicket.markup.html.list;

import java.util.Collections;
import java.util.Comparator;

import wicket.markup.ComponentTag;
import wicket.markup.html.border.Border;
import wicket.markup.html.link.Link;


/**
 * Sortable list view header component for a single list view column. Functionality provided
 * includes sorting the underlying list view and changing the colours (style) of the header.
 * @see SortableListViewHeaderGroup
 * @see SortableListViewHeaders
 * @author Juergen Donnerstag
 */
public abstract class SortableListViewHeader extends Border
{

	/** Sort ascending or descending */
	private boolean ascending;
	/** All sortable columns of a single list view are grouped */
	private final SortableListViewHeaderGroup group;

	/**
	 * Construct.
	 * @param componentName The component name
	 * @param group The group of headers the new one will be added to
	 */
	public SortableListViewHeader(final String componentName, final SortableListViewHeaderGroup group)
	{
		super(componentName);

		// Default to descending.
		this.ascending = false;
		this.group = group;

		// If user clicks on the header, sorting will reverse
		final SortableListViewHeader me = this;
		add(new Link("actionLink")
		{
			public void onLinkClicked()
			{
				// call SortableTableHeaders implementation
				me.linkClicked();

				// Redirect back to result to avoid refresh updating the link count
				getRequestCycle().setRedirect(true);
			}
		});
	}

	/**
	 * Compare two objects (list elements of list view's model object). Both objects must
	 * implement Comparable. In order to compare basic types like int or double, simply
	 * subclass the method.
	 * @param o1 first object
	 * @param o2 second object
	 * @return comparision result
	 */
	protected int compareTo(Object o1, Object o2)
	{
		Comparable obj1 = getObjectToCompare(o1);
		Comparable obj2 = getObjectToCompare(o2);
		return obj1.compareTo(obj2);
	}

	/**
	 * Get CSS style for the header based on ascending / descending. Delegate to the header
	 * group to determine the style.
	 * @return css class
	 */
	protected final String getCssClass()
	{
		// TODO This needs to be integrated with our CSS design
		return group.getCssClass(getName(), ascending);
	}

	/**
	 * Returns the comparable object of the list view the header/column is referring to, e.g.
	 * obj.getId();
	 * @param object the ListItems model object
	 * @return The object to compare
	 */
	protected Comparable getObjectToCompare(Object object)
	{
		return (Comparable)object;
	}

	/**
	 * Header has been clicked. Define what to do.
	 */
	protected void linkClicked()
	{
		// change sorting order: ascending <-> descending
		ascending = !ascending;

		// Tell the header group that something has changed
		group.setSortedColumn(getName());

		// sort the list view's model data accordingly
		sort();

		// Redirect back to result to avoid refresh updating the link count
		getRequestCycle().setRedirect(true);
	}

	/**
	 * Delegate to the header group to handle the tag.
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 * @param tag The current ComponentTag to handle
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		group.handleComponentTag(tag, getCssClass());
	}

	/**
	 * Sort list view's model object based on column data
	 */
	protected void sort()
	{
		Collections.sort(group.getListViewModelObject(), new Comparator()
		{
			public int compare(Object o1, Object o2)
			{
				if (ascending)
				{
					return compareTo(o1, o2);
				}
				else
				{
					return compareTo(o2, o1);
				}
			}
		});
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5503.java