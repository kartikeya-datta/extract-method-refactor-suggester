error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/29.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/29.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/29.java
text:
```scala
public final C@@lass< ? extends Page< ? >> getPageClass()

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
package org.apache.wicket.markup.html.link;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.IPageMap;
import org.apache.wicket.Page;
import org.apache.wicket.PageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.util.lang.Classes;

/**
 * Renders a stable link which can be cached in a web browser and used at a later time.
 * 
 * @author Jonathan Locke
 */
public class BookmarkablePageLink extends Link<CharSequence>
{
	private static final long serialVersionUID = 1L;

	/** The page class that this link links to. */
	private final String pageClassName;

	/** Any page map for this link */
	private String pageMapName = null;

	/** The parameters to pass to the class constructor when instantiated. */
	protected MiniMap parameters;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            The name of this component
	 * @param pageClass
	 *            The class of page to link to
	 */
	public BookmarkablePageLink(final String id, final Class pageClass)
	{
		this(id, pageClass, null);
	}

	private MiniMap pageParametersToMiniMap(PageParameters parameters)
	{
		if (parameters != null)
		{
			MiniMap map = new MiniMap(parameters, parameters.keySet().size());
			return map;
		}
		else
		{
			return null;
		}

	}

	public PageParameters getPageParameters()
	{
		PageParameters result = new PageParameters();
		if (parameters != null)
		{
			for (Iterator i = parameters.entrySet().iterator(); i.hasNext();)
			{
				Entry entry = (Entry)i.next();
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	private void setParameterImpl(String key, Object value)
	{
		PageParameters parameters = getPageParameters();
		parameters.put(key, value);
		this.parameters = pageParametersToMiniMap(parameters);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param pageClass
	 *            The class of page to link to
	 * @param parameters
	 *            The parameters to pass to the new page when the link is clicked
	 */
	public BookmarkablePageLink(final String id, final Class pageClass,
		final PageParameters parameters)
	{
		super(id);

		this.parameters = pageParametersToMiniMap(parameters);

		if (pageClass == null)
		{
			throw new IllegalArgumentException("Page class for bookmarkable link cannot be null");
		}
		else if (!Page.class.isAssignableFrom(pageClass))
		{
			throw new IllegalArgumentException("Page class must be derived from " +
				Page.class.getName());
		}
		pageClassName = pageClass.getName();
	}

	/**
	 * Get tge page class registered with the link
	 * 
	 * @return Page class
	 */
	public final Class<? extends Page> getPageClass()
	{
		return Classes.resolveClass(pageClassName);
	}

	/**
	 * @return Page map for this link
	 */
	public final IPageMap getPageMap()
	{
		if (pageMapName != null)
		{
			return PageMap.forName(pageMapName);
		}
		else
		{
			return getPage().getPageMap();
		}
	}

	/**
	 * Whether this link refers to the given page.
	 * 
	 * @param page
	 *            the page
	 * @see org.apache.wicket.markup.html.link.Link#linksTo(org.apache.wicket.Page)
	 */
	@Override
	public boolean linksTo(final Page page)
	{
		return page.getClass() == getPageClass();
	}

	@Override
	protected boolean getStatelessHint()
	{
		return true;
	}

	/**
	 * THIS METHOD IS NOT USED! Bookmarkable links do not have a click handler. It is here to
	 * satisfy the interface only, as bookmarkable links will be dispatched by the handling servlet.
	 * 
	 * @see org.apache.wicket.markup.html.link.Link#onClick()
	 */
	@Override
	public final void onClick()
	{
		// Bookmarkable links do not have a click handler.
		// Instead they are dispatched by the request handling servlet.
	}

	/**
	 * @param pageMap
	 *            The pagemap for this link's destination
	 * @return This
	 */
	public final BookmarkablePageLink setPageMap(final IPageMap pageMap)
	{
		if (pageMap != null)
		{
			pageMapName = pageMap.getName();
			add(new AttributeModifier("target", true, new Model(pageMapName)));
		}
		return this;
	}

	/**
	 * Adds a given page property value to this link.
	 * 
	 * @param property
	 *            The property
	 * @param value
	 *            The value
	 * @return This
	 */
	public BookmarkablePageLink setParameter(final String property, final int value)
	{
		setParameterImpl(property, Integer.toString(value));
		return this;
	}

	/**
	 * Adds a given page property value to this link.
	 * 
	 * @param property
	 *            The property
	 * @param value
	 *            The value
	 * @return This
	 */
	public BookmarkablePageLink setParameter(final String property, final long value)
	{
		setParameterImpl(property, Long.toString(value));
		return this;
	}

	/**
	 * Adds a given page property value to this link.
	 * 
	 * @param property
	 *            The property
	 * @param value
	 *            The value
	 * @return This
	 */
	public BookmarkablePageLink setParameter(final String property, final String value)
	{
		setParameterImpl(property, value);
		return this;
	}

	/**
	 * Gets the url to use for this link.
	 * 
	 * @return The URL that this link links to
	 * @see org.apache.wicket.markup.html.link.Link#getURL()
	 */
	@Override
	protected CharSequence getURL()
	{
		if (pageMapName != null && getPopupSettings() != null)
		{
			throw new IllegalStateException("You cannot specify popup settings and a page map");
		}

		PageParameters parameters = getPageParameters();

		if (getPopupSettings() != null)
		{
			return urlFor(getPopupSettings().getPageMap(this), getPageClass(), parameters);
		}
		else
		{
			return urlFor(getPageMap(), getPageClass(), parameters);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/29.java