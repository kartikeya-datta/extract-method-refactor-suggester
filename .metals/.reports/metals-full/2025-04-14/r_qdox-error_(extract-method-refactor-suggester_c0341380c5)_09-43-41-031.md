error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4847.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4847.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4847.java
text:
```scala
protected v@@oid onRender()

/*
 * $Id$ $Revision$
 * $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.list;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;

/**
 * A ListView holds ListItems of information. The listItem can be re-ordered and
 * deleted, either one at a time or many at a time.
 * <p>
 * Example:
 * 
 * <pre>
 * 
 *  
 *   
 *      &lt;tbody&gt;
 *        &lt;tr id=&quot;wicket-rows&quot; class=&quot;even&quot;&gt;
 *            &lt;td&gt;&lt;span id=&quot;wicket-id&quot;&gt;Test ID&lt;/span&gt;&lt;/td&gt;
 *        ...    
 *    
 *   
 *  
 * </pre>
 * 
 * <p>
 * Though this example is about a HTML table, ListView is not at all limited to
 * HTML tables. Any kind of list can be rendered using ListView.
 * <p>
 * And the related Java code:
 * 
 * <pre>
 * add(new ListView(&quot;rows&quot;, listData)
 * {
 * 	public void populateItem(final ListItem item)
 * 	{
 * 		final UserDetails user = (UserDetails)item.getModelObject();
 * 		cell.add(new Label(&quot;id&quot;, user.getId()));
 * 	}
 * });
 * </pre>
 * 
 * <p>
 * Note: Because Wicket model object must be Serializable,
 * java.util.List.subList() can not be used for model object, as the List
 * implementation return by subList() is protected and can not be subclassed.
 * Which is why Wicket implements subList functionality with ListView.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public abstract class ListView extends WebMarkupContainer
{
	/** Log. */
	private static Log log = LogFactory.getLog(ListView.class);

	/** Max number (not index) of listItems to show */
	protected int viewSize = Integer.MAX_VALUE;

	/** Index of the first listItem to show */
	private int firstIndex = 0;

	/**
	 * @param name
	 *            See Component constructor
	 * @param list
	 *            List to cast to Serializable
	 * @see wicket.Component#Component(String, Serializable)
	 */
	public ListView(String name, List list)
	{
		super(name, (Serializable)list);
	}

	/**
	 * @see wicket.Component#Component(String, Serializable)
	 */
	public ListView(String name, Serializable object)
	{
		super(name, object);
	}

	/**
	 * @see wicket.Component#Component(String, Serializable, String)
	 */
	public ListView(String name, Serializable object, String expression)
	{
		super(name, object, expression);
	}

	/**
	 * Gets the list of items in the listView. This method is final because it
	 * is not designed to be overridden. If it were allowed to be overridden,
	 * the values returned by getModelObject() and getList() might not coincide.
	 * 
	 * @return The list of items in this list view.
	 */
	public final List getList()
	{
		List list = (List)getModelObject();
		return (list != null ? list : Collections.EMPTY_LIST);
	}

	/**
	 * Get index of first cell in page. Default is: 0.
	 * 
	 * @return Index of first cell in page. Default is: 0
	 */
	public final int getStartIndex()
	{
		return this.firstIndex;
	}

	/**
	 * Based on the model object's list size, firstIndex and view size,
	 * determine what the view size really will be. E.g. default for viewSize is
	 * Integer.MAX_VALUE, if not set via setViewSize(). If the underlying list
	 * has 10 elements, the value returned by getViewSize() will be 10 if
	 * startIndex = 0.
	 * 
	 * @return The number of listItems to be populated and rendered.
	 */
	public int getViewSize()
	{
		int size = this.viewSize;

		// If model object (list view) == null and viewSize has not been
		// deliberately changed, than size = 0.
		Object modelObject = getModelObject();
		if ((modelObject == null) && (viewSize == Integer.MAX_VALUE))
		{
			size = 0;
		}
		else if (modelObject instanceof List)
		{
			// Adjust view size to model object's list size
			final int modelSize = ((List)modelObject).size();
			if (firstIndex > modelSize)
			{
				return 0;
			}

			if ((size == Integer.MAX_VALUE) || ((firstIndex + size) > modelSize))
			{
				size = modelSize - firstIndex;
			}
		}
		else if (viewSize == Integer.MAX_VALUE)
		{
			// The model is not a list and size is not set; probably an error
			log.warn("model object (" + modelObject
					+ ") is not a List and the view size is not explicitly set");
		}

		// firstIndex + size must be smaller than Integer.MAX_VALUE
		if ((Integer.MAX_VALUE - size) <= firstIndex)
		{
			throw new IllegalStateException(
					"firstIndex + size must be smaller than Integer.MAX_VALUE");
		}

		return size;
	}

	/**
	 * Indicates a structural change to the ListView model.
	 */
	public void invalidateModel()
	{
		// Now that a structural change has been made to the model,
		// All pages using the same model should be expired
		super.invalidateModel();

		// Remove child listItems from listView since they came
		// from the old and now invalid model
		removeAll();
	}

	/**
	 * Set the index of the first listItem to render
	 * 
	 * @param startIndex
	 *            First index of model object's list to display
	 * @return This
	 */
	public ListView setStartIndex(final int startIndex)
	{
		this.firstIndex = startIndex;

		if (firstIndex < 0)
		{
			firstIndex = 0;
		}

		return this;
	}

	/**
	 * Define the maximum number of listItems to render. Default: render all.
	 * 
	 * @param size
	 *            Number of listItems to display
	 * @return This
	 */
	public ListView setViewSize(final int size)
	{
		this.viewSize = size;

		if (viewSize < 0)
		{
			viewSize = Integer.MAX_VALUE;
		}

		return this;
	}

	/**
	 * Provide list object at index. May be subclassed for virtual list, which
	 * don't implement List.
	 * 
	 * @param index
	 *            The list object's index
	 * @return the model list's object
	 */
	protected Serializable getListObject(final int index)
	{
		Object object = getList().get(index);
		if ((object != null) && !(object instanceof Serializable))
		{
			throw new ClassCastException(
					"ListView and ListItem model data must be serializable, index: " + index
							+ ", data: " + object); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (Serializable)object;
	}

	/**
	 * Renders this ListView (container).
	 */
	protected void handleRender()
	{
		// Ask parents for markup stream to use
		final MarkupStream markupStream = findMarkupStream();

		// Save position in markup stream
		final int markupStart = markupStream.getCurrentIndex();

		// Get number of listItems to be displayed
		int size = getViewSize();
		if (size > 0)
		{
			// Loop through the markup in this container for each child
			// container
			for (int i = 0; i < size; i++)
			{
				int lastIndex = firstIndex + i;

				// Get the name of the component for listItem i
				final String componentName = Integer.toString(lastIndex);

				// If this component does not already exist, populate it
				ListItem listItem = (ListItem)get(componentName);
				if (listItem == null)
				{
					// Create listItem for index i of the list
					listItem = newItem(lastIndex);
					populateItem(listItem);

					// Add cell to list view
					add(listItem);
				}

				// Rewind to start of markup for kids
				markupStream.setCurrentIndex(markupStart);

				// Render cell
				renderItem(listItem, i >= (size - 1));
			}
		}
		else
		{
			markupStream.skipComponent();
		}
	}

	/**
	 * Creates a new listItem for the given listItem index of this listView.
	 * 
	 * @param index
	 *            ListItem index
	 * @return The new ListItem
	 */
	protected ListItem newItem(final int index)
	{
		return new ListItem(index, this);
	}

	/**
	 * Populate a given listItem.
	 * 
	 * @param listItem
	 *            The listItem to populate
	 */
	protected abstract void populateItem(final ListItem listItem);

	/**
	 * Render a single listItem.
	 * 
	 * @param listItem
	 *            the listItem to be rendered
	 * @param lastItem
	 *            True, if item is last listItem in listView
	 */
	protected void renderItem(final ListItem listItem, final boolean lastItem)
	{
		listItem.render();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4847.java