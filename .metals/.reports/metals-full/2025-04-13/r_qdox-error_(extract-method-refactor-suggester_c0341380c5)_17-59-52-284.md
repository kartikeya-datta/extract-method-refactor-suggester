error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1433.java
text:
```scala
final private static L@@og log = LogFactory.getLog(SortableListViewHeaders.class);

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
package wicket.examples.displaytag.list;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.list.ListView;
import wicket.markup.resolver.IComponentResolver;

/**
 * This is a convenient component to create sorted list view headers very easily. It first
 * scans the markup for &lt;th wicket:id=".*" ..&gt> tags and automatically creates a
 * SortableListViewHeader for each.
 * <p>
 * The component can only be used with &lt;thead&gt; tags.
 * @see SortableListViewHeaderGroup
 * @see SortableListViewHeader
 * @author Juergen Donnerstag
 */
public class SortableListViewHeaders extends WebMarkupContainer implements IComponentResolver
{
	/** Logging. */
	final private Log log = LogFactory.getLog(SortableListViewHeaders.class);
	
	/** Each SortableTableHeader (without 's) must be attached to a group. */
	final private SortableListViewHeaderGroup group;

	/**
	 * Construct.
	 * @param id The component's id; must not be null
	 * @param listView the underlying ListView
	 */
	public SortableListViewHeaders(final String id, final ListView listView)
	{
		super(id);

		this.group = new SortableListViewHeaderGroup(this, listView);
	}

	/**
	 * Compare two object of the column to be sorted, assuming both Objects support
	 * compareTo().
	 * @see Comparable#compareTo(java.lang.Object)
	 * @param header
	 * @param o1
	 * @param o2
	 * @return compare result
	 */
	protected int compareTo(final SortableListViewHeader header, final Object o1, final Object o2)
	{
		Comparable obj1 = getObjectToCompare(header, o1);
		Comparable obj2 = getObjectToCompare(header, o2);
		return obj1.compareTo(obj2);
	}

	/**
	 * Get one of the two Object to be compared for sorting a column.
	 * @param header
	 * @param object
	 * @return comparable object
	 */
	protected Comparable getObjectToCompare(final SortableListViewHeader header, final Object object)
	{
		return (Comparable)object;
	}

	/**
	 * 
	 * @param container
	 * @param markupStream
	 * @param tag
	 * @return true, if component got resolved
	 */
	public boolean resolve(wicket.MarkupContainer container,MarkupStream markupStream,ComponentTag tag)
	{
		if (tag.getName().equalsIgnoreCase("th"))
		{
			// Get component name
			final String componentId = tag.getId();
			if ((componentId != null) && (get(componentId) == null))
			{
				autoAdd(new SortableListViewHeader(componentId, group)
				{
					protected int compareTo(final Object o1, final Object o2)
					{
						return SortableListViewHeaders.this.compareTo(this, o1, o2);
					}

					protected Comparable getObjectToCompare(final Object object)
					{
						return SortableListViewHeaders.this.getObjectToCompare(this, object);
					}
				});
				return true;
			}
		}
		
	    return false;
	}
	
	/**
	 * Scan the related markup and attach a SortableListViewHeader to each &lt;th&gt; tag
	 * found.
	 * @see wicket.Component#onRender(MarkupStream)
	 */
	protected void onRender(final MarkupStream markupStream)
	{
		// Must be <thead> tag
		ComponentTag tag = markupStream.getTag();
		final ComponentTag openTag = tag;
		checkComponentTag(tag, "thead");

		// Continue with default behavior
		super.onRender(markupStream);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1433.java