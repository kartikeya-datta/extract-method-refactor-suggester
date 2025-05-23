error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6878.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6878.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6878.java
text:
```scala
public S@@impleListListItem(MarkupContainer parent,final int index, final IModel model)

/*
 * $Id: TableWithAlternatingRowStyle.java 3764 2006-01-14 17:38:33Z
 * jonathanlocke $ $Revision$ $Date: 2006-01-14 18:38:33 +0100 (Sa, 14
 * Jan 2006) $
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
package wicket.examples.displaytag.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wicket.Component;
import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.basic.Label;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.PageableListView;
import wicket.markup.resolver.IComponentResolver;
import wicket.model.BoundCompoundPropertyModel;
import wicket.model.IModel;


/**
 * This is a simple pageable ListView extension automatically creating Labels
 * for each ListItem. In case Label is not the right one, you can still add any
 * other component you want. Alternating row styles are provided as well.
 * 
 * @author Juergen Donnerstag
 */
public class SimplePageableListView extends PageableListView implements IComponentResolver
{
	/** The tags "class" attribute for odd index rows */
	public static String ODD = "odd";

	/** The tags "class" attribute for even index rows */
	public static String EVEN = "even";

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param data
	 * @param rowsPerPage
	 */
	public SimplePageableListView(MarkupContainer parent,final String id, final List data, final int rowsPerPage)
	{
		super(parent,id, data, rowsPerPage);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param rowsPerPage
	 */
	public SimplePageableListView(MarkupContainer parent,final String id, final IModel model, final int rowsPerPage)
	{
		super(parent,id, model, rowsPerPage);
	}

	/**
	 * Subclass Table's newCell() and return a ListItem which will add/modify
	 * its class attribute and thus provide ListItems with alternating row
	 * colours.
	 * 
	 * See wicket.markup.html.table.Table#newItem(int)
	 * 
	 * @param index
	 *            Index of item
	 * @return List item
	 */
	protected ListItem newItem(final int index)
	{
		return new SimpleListListItem(this,index, getListItemModel(getModel(), index));
	}

	/**
	 * Get the tags "class" attribute
	 * 
	 * @param id
	 *            The wicket:id of the tag
	 * @param index
	 *            The row index
	 * @return The class value to be used
	 */
	protected String getClassAttribute(final String id, final int index)
	{
		// add/modify the attribute controlling the CSS style
		return ((index % 2) == 0 ? EVEN : ODD);
	}

	/**
	 * 
	 * @param model
	 * @param index
	 * @return IModel
	 */
	protected IModel getListItemModel(final IModel model, final int index)
	{
		return new BoundCompoundPropertyModel(super.getListItemModel(model, index));
	}

	/**
	 * Automatically add Labels if the user didn't provide any component himself
	 * 
	 * @param container
	 * @param markupStream
	 * @param tag
	 * @return true, if component has been added
	 */
	public boolean resolve(final MarkupContainer container, final MarkupStream markupStream,
			final ComponentTag tag)
	{
		String componentId = tag.getId();
		if((componentId != null) && !componentId.startsWith(Component.AUTO_COMPONENT_PREFIX))
		{
			componentId = Component.AUTO_COMPONENT_PREFIX + componentId;
			tag.setId(componentId);
		}		
		newLabel(container,componentId).autoAdded();
		return true;
	}

	/**
	 * Create a new default component in case it is not explicitly defined.
	 * 
	 * @param id
	 * @return Usually a Label like component
	 */
	protected Component newLabel(MarkupContainer parent,final String id)
	{
		return new Label(parent,id);
	}

	/**
	 * May be overriden, but doesn't have to.
	 * 
	 * @param item
	 */
	protected void populateItem(final ListItem item)
	{
	}

	/**
	 * 
	 */
	public static class SimpleListLabel
	{
		private static Map idToLabel = new HashMap();

		/**
		 * Construct
		 */
		public SimpleListLabel()
		{
		}

		/**
		 * 
		 * @param id
		 * @return Label
		 */
		public static Label getInstance(MarkupContainer parent,final String id)
		{
			Label label = (Label)idToLabel.get(id);
			if (label == null)
			{
				label = new Label(parent,id);
				idToLabel.put(id, label);
			}

			return label;
		}
	}

	/**
	 * 
	 * @author Juergen Donnerstag
	 */
	public class SimpleListListItem extends ListItem
	{
		/**
		 * Constructor
		 * 
		 * @param index
		 *            The row index
		 * @param model
		 *            The associated model
		 */
		public SimpleListListItem(MarkupContainer<?> parent,final int index, final IModel model)
		{
			super(parent,index, model);
		}

		protected void onComponentTag(final ComponentTag tag)
		{
			// add/modify the attribute controlling the CSS style
			final String classAttr = getClassAttribute(tag.getId(), getIndex());
			if (classAttr != null)
			{
				tag.put("class", classAttr);
			}

			// continue with default behavior
			super.onComponentTag(tag);
		}
	};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6878.java