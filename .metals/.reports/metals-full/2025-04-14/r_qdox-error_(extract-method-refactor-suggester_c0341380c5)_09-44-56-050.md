error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5038.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5038.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5038.java
text:
```scala
i@@nterceptContinuationURL = "/" + cycle.getRequest().getURL();

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.session.pagemap.IPageMapEntry;
import org.apache.wicket.util.lang.Objects;

/**
 * @author Jonathan Locke
 * @author jcompagner
 */
public abstract class PageMap implements IClusterable, IPageMap
{
	/**
	 * Visitor interface for visiting entries in this map
	 * 
	 * @author Jonathan Locke
	 */
	static interface IVisitor
	{
		/**
		 * @param entry
		 *            The page map entry
		 */
		public void entry(final IPageMapEntry entry);
	}


	/** Name of default pagemap */
	public static final String DEFAULT_NAME = null;

	private static final long serialVersionUID = 1L;

	/**
	 * Gets a page map for a page map name, automatically creating the page map
	 * if it does not exist. If you do not want the pagemap to be automatically
	 * created, you can call Session.pageMapForName(pageMapName, false).
	 * 
	 * @param pageMapName
	 *            The name of the page map to get
	 * @return The PageMap with the given name from the current session
	 */
	public static IPageMap forName(final String pageMapName)
	{
		Session session = Session.get();
		return (session != null) ? session.pageMapForName(pageMapName, true) : null;
	}

	/** URL to continue to after a given page. */
	private String interceptContinuationURL;

	/** Name of this page map */
	private final String name;

	/** Next available page identifier in this page map. */
	private int pageId = 0;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            The name of this page map
	 * @param session
	 *            The session holding this page map
	 */
	public PageMap(String name)
	{
		this.name = name;
	}

	/**
	 * @see org.apache.wicket.IPageMap#attributeForId(int)
	 */
	public final String attributeForId(final int id)
	{
		return attributePrefix() + id;
	}

	/**
	 * @see org.apache.wicket.IPageMap#clear()
	 */
	public void clear()
	{
		// Remove all entries
		visitEntries(new IVisitor()
		{
			public void entry(IPageMapEntry entry)
			{
				removeEntry(entry);
			}
		});
	}

	/**
	 * Redirects to any intercept page previously specified by a call to
	 * redirectToInterceptPage.
	 * 
	 * @return True if an original destination was redirected to
	 * @see PageMap#redirectToInterceptPage(Page)
	 */
	public final boolean continueToOriginalDestination()
	{
		// Get request cycle
		final RequestCycle cycle = RequestCycle.get();

		// If there's a place to go to
		if (interceptContinuationURL != null)
		{
			cycle.setRequestTarget(new RedirectRequestTarget(interceptContinuationURL));

			// Reset interception URL
			interceptContinuationURL = null;

			// Force session to replicate page maps
			dirty();
			return true;
		}
		return false;
	}

	/**
	 * @see org.apache.wicket.IPageMap#get(int, int)
	 */
	public abstract Page get(final int id, int versionNumber);

	/**
	 * @see org.apache.wicket.IPageMap#getEntry(int)
	 */
	public final IPageMapEntry getEntry(final int id)
	{
		return (IPageMapEntry)Session.get().getAttribute(attributeForId(id));
	}

	/**
	 * @see org.apache.wicket.IPageMap#getName()
	 */
	public final String getName()
	{
		return name;
	}

	/**
	 * @return Session this page map is in
	 */
	public final Session getSession()
	{
		return Session.get();
	}

	/**
	 * @see org.apache.wicket.IPageMap#getSizeInBytes()
	 */
	public final long getSizeInBytes()
	{
		long size = Objects.sizeof(this);
		Iterator it = getEntries().iterator();
		while (it.hasNext())
		{
			IPageMapEntry entry = (IPageMapEntry)it.next();
			if (entry instanceof Page)
			{
				size += ((Page)entry).getSizeInBytes();
			}
			else
			{
				size += Objects.sizeof(entry);
			}
		}
		return size;
	}

	/**
	 * @see org.apache.wicket.IPageMap#isDefault()
	 */
	public final boolean isDefault()
	{
		return name == PageMap.DEFAULT_NAME;
	}

	/**
	 * @see org.apache.wicket.IPageMap#nextId()
	 */
	public final int nextId()
	{
		dirty();
		return this.pageId++;
	}

	/**
	 * @see org.apache.wicket.IPageMap#put(org.apache.wicket.Page)
	 */
	public abstract void put(final Page page);

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The
	 * current request's URL is saved exactly as it was requested for future use
	 * by continueToOriginalDestination(); Only use this method when you plan to
	 * continue to the current URL at some later time; otherwise just use
	 * setResponsePage or, when you are in a constructor, redirectTo.
	 * 
	 * @param pageClazz
	 *            The page clazz to temporarily redirect to
	 */
	public final void redirectToInterceptPage(final Class pageClazz)
	{
		final RequestCycle cycle = RequestCycle.get();
		setUpRedirect(cycle);
		cycle.setResponsePage(pageClazz);
	}

	/**
	 * Redirects browser to an intermediate page such as a sign-in page. The
	 * current request's URL is saved exactly as it was requested for future use
	 * by continueToOriginalDestination(); Only use this method when you plan to
	 * continue to the current URL at some later time; otherwise just use
	 * setResponsePage or, when you are in a constructor, redirectTo.
	 * 
	 * @param page
	 *            The page to temporarily redirect to
	 */
	public final void redirectToInterceptPage(final Page page)
	{
		final RequestCycle cycle = RequestCycle.get();
		setUpRedirect(cycle);
		cycle.setResponsePage(page);
	}
	
	private void setUpRedirect(final RequestCycle cycle)
	{
		Session session = Session.get();
		if (session.isTemporary())
		{
			session.bind();
		}

		// The intercept continuation URL should be saved exactly as the
		// original request specified.
		interceptContinuationURL = cycle.getRequest().getPath();

		// Page map is dirty
		dirty();

		// Redirect to the page
		cycle.setRedirect(true);
	}

	/**
	 * @see org.apache.wicket.IPageMap#remove()
	 */
	public final void remove()
	{
		// First clear all pages from the session for this pagemap
		clear();

		// Then remove the pagemap itself
		Session.get().removePageMap(this);
	}

	/**
	 * @see org.apache.wicket.IPageMap#remove(org.apache.wicket.Page)
	 */
	public final void remove(final Page page)
	{
		// Remove the pagemap entry from session
		removeEntry(page.getPageMapEntry());
	}

	/**
	 * @see org.apache.wicket.IPageMap#removeEntry(org.apache.wicket.session.pagemap.IPageMapEntry)
	 */
	public abstract void removeEntry(final IPageMapEntry entry);

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[PageMap name=" + name + "]";
	}

	/**
	 * @return List of entries in this page map
	 */
	private final List getEntries()
	{
		final Session session = Session.get();
		final List attributes = session.getAttributeNames();
		final List list = new ArrayList();
		for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
		{
			final String attribute = (String)iterator.next();
			if (attribute.startsWith(attributePrefix()))
			{
				list.add(session.getAttribute(attribute));
			}
		}
		return list;
	}

	protected final void dirty()
	{
		Session.get().dirtyPageMap(this);
	}

	/**
	 * @param visitor
	 *            The visitor to call at each Page in this PageMap.
	 */
	protected final void visitEntries(final IVisitor visitor)
	{
		final Session session = Session.get();
		final List attributes = session.getAttributeNames();
		for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
		{
			final String attribute = (String)iterator.next();
			if (attribute.startsWith(attributePrefix()))
			{
				visitor.entry((IPageMapEntry)session.getAttribute(attribute));
			}
		}
	}

	/**
	 * @return The attribute prefix for this page map
	 */
	final String attributePrefix()
	{
		return Session.pageMapEntryAttributePrefix + name + ":";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5038.java