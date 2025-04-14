error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5498.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5498.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5498.java
text:
```scala
p@@ublic class OrderByLink extends Link<Void>

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
package org.apache.wicket.extensions.markup.html.repeater.data.sort;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.string.Strings;

/**
 * A component that represents a sort header. When the link is clicked it will toggle the state of a
 * sortable property within the sort state object.
 * 
 * @author Phil Kulak
 * @author Igor Vaynberg (ivaynberg)
 */
public class OrderByLink extends Link
{
	private static final long serialVersionUID = 1L;

	/** sortable property */
	private final String property;

	/** locator for sort state object */
	private final ISortStateLocator stateLocator;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the component id of the link
	 * @param property
	 *            the name of the sortable property this link represents. this value will be used as
	 *            parameter for sort state object methods. sort state object will be located via the
	 *            stateLocator argument.
	 * @param stateLocator
	 *            locator used to locate sort state object that this will use to read/write state of
	 *            sorted properties
	 */
	public OrderByLink(final String id, final String property, final ISortStateLocator stateLocator)
	{
		this(id, property, stateLocator, DefaultCssProvider.getInstance());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the component id of the link
	 * @param property
	 *            the name of the sortable property this link represents. this value will be used as
	 *            parameter for sort state object methods. sort state object will be located via the
	 *            stateLocator argument.
	 * @param stateLocator
	 *            locator used to locate sort state object that this will use to read/write state of
	 *            sorted properties
	 * @param cssProvider
	 *            CSS provider that will be used generate the value of class attribute for this link
	 * 
	 * @see OrderByLink.ICssProvider
	 * 
	 */
	public OrderByLink(final String id, final String property,
		final ISortStateLocator stateLocator, final ICssProvider cssProvider)
	{
		super(id);

		if (cssProvider == null)
		{
			throw new IllegalArgumentException("argument [cssProvider] cannot be null");
		}

		if (property == null)
		{
			throw new IllegalArgumentException("argument [sortProperty] cannot be null");
		}

		this.property = property;
		this.stateLocator = stateLocator;
		add(new CssModifier(this, cssProvider));
	}

	/**
	 * @see org.apache.wicket.markup.html.link.Link
	 */
	@Override
	public final void onClick()
	{
		sort();
		onSortChanged();
	}

	/**
	 * This method is a hook for subclasses to perform an action after sort has changed
	 */
	protected void onSortChanged()
	{
		// noop
	}

	/**
	 * Re-sort data provider according to this link
	 * 
	 * @return this
	 */
	public final OrderByLink sort()
	{
		if (isVersioned())
		{
			// version the old state
			addStateChange();
		}

		ISortState state = stateLocator.getSortState();

		// get current sort order
		SortOrder order = state.getPropertySortOrder(property);

		// set next sort order
		state.setPropertySortOrder(property, nextSortOrder(order));

		return this;
	}

	/**
	 * returns the next sort order when changing it
	 * 
	 * @param order
	 *            previous sort order
	 * @return next sort order
	 */
	protected SortOrder nextSortOrder(final SortOrder order)
	{
		// init / flip order
		if (order == SortOrder.NONE)
		{
			return SortOrder.ASCENDING;
		}
		else
		{
			return order == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING;
		}
	}

	/**
	 * Uses the specified ICssProvider to add css class attributes to the link.
	 * 
	 * @author Igor Vaynberg ( ivaynberg )
	 * 
	 */
	public static class CssModifier extends Behavior
	{
		private static final long serialVersionUID = 1L;
		private final OrderByLink link;
		private final ICssProvider provider;

		/**
		 * @param link
		 *            the link this modifier is being added to
		 * @param provider
		 *            implementation of ICssProvider
		 */
		public CssModifier(final OrderByLink link, final ICssProvider provider)
		{
			this.link = link;
			this.provider = provider;
		}

		@Override
		public void onComponentTag(final Component component, final ComponentTag tag)
		{
			super.onComponentTag(component, tag);

			final ISortState sortState = link.stateLocator.getSortState();
			String cssClass = provider.getClassAttributeValue(sortState, link.property);
			if (!Strings.isEmpty(cssClass))
			{
				tag.append("class", cssClass, " ");
			}

		}
	}


	/**
	 * Interface used to generate values of css class attribute for the anchor tag If the generated
	 * value is null class attribute will not be added
	 * 
	 * @author igor
	 */
	public static interface ICssProvider extends IClusterable
	{
		/**
		 * @param state
		 *            current sort state
		 * @param property
		 *            sort property represented by the {@link OrderByLink}
		 * @return the value of the "class" attribute for the given sort state/sort property
		 *         combination
		 */
		public String getClassAttributeValue(ISortState state, String property);
	}


	/**
	 * Easily constructible implementation of ICSSProvider
	 * 
	 * @author Igor Vaynberg (ivaynberg)
	 * 
	 */
	public static class CssProvider implements ICssProvider
	{
		private static final long serialVersionUID = 1L;

		private final String ascending;

		private final String descending;

		private final String none;

		/**
		 * @param ascending
		 *            css class when sorting is ascending
		 * @param descending
		 *            css class when sorting is descending
		 * @param none
		 *            css class when not sorted
		 */
		public CssProvider(final String ascending, final String descending, final String none)
		{
			this.ascending = ascending;
			this.descending = descending;
			this.none = none;
		}

		/**
		 * @see org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByLink.ICssProvider#getClassAttributeValue(org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState,
		 *      java.lang.String)
		 */
		public String getClassAttributeValue(final ISortState state, final String property)
		{
			SortOrder dir = state.getPropertySortOrder(property);

			if (dir == SortOrder.ASCENDING)
			{
				return ascending;
			}
			else if (dir == SortOrder.DESCENDING)
			{
				return descending;
			}
			else
			{
				return none;
			}
		}
	}

	/**
	 * Convenience implementation of ICssProvider that always returns a null and so never adds a
	 * class attribute
	 * 
	 * @author Igor Vaynberg ( ivaynberg )
	 */
	public static class VoidCssProvider extends CssProvider
	{
		private static final long serialVersionUID = 1L;

		private static ICssProvider instance = new VoidCssProvider();

		/**
		 * @return singleton instance
		 */
		public static ICssProvider getInstance()
		{
			return instance;
		}

		private VoidCssProvider()
		{
			super("", "", "");
		}
	}

	/**
	 * Default implementation of ICssProvider
	 * 
	 * @author Igor Vaynberg ( ivaynberg )
	 */
	public static class DefaultCssProvider extends CssProvider
	{
		private static final long serialVersionUID = 1L;

		private static DefaultCssProvider instance = new DefaultCssProvider();

		private DefaultCssProvider()
		{
			super("wicket_orderUp", "wicket_orderDown", "wicket_orderNone");
		}

		/**
		 * @return singleton instance
		 */
		public static DefaultCssProvider getInstance()
		{
			return instance;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5498.java