error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13990.java
text:
```scala
private static final C@@oncurrentMap<String, PersistentPageManager> managers = new ConcurrentHashMap<String, PersistentPageManager>();

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
package org.apache.wicket.page;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.wicket.pageStore.IPageStore;

/**
 * 
 */
public class PersistentPageManager extends AbstractPageManager
{
	/**
	 * A cache that holds all registered page managers. <br/>
	 * applicationName -> page manager
	 */
	private static ConcurrentMap<String, PersistentPageManager> managers = new ConcurrentHashMap<String, PersistentPageManager>();

	private final IPageStore pageStore;

	private final String applicationName;

	/**
	 * Construct.
	 * 
	 * @param applicationName
	 * @param pageStore
	 * @param context
	 */
	public PersistentPageManager(final String applicationName, final IPageStore pageStore,
		final IPageManagerContext context)
	{
		super(context);

		this.applicationName = applicationName;
		this.pageStore = pageStore;

		if (managers.containsKey(applicationName))
		{
			throw new IllegalStateException("Manager for application with key '" + applicationName +
				"' already exists.");
		}
		managers.put(applicationName, this);
	}

	/**
	 * Represents entry for single session. This is stored as session attribute and caches pages
	 * between requests.
	 * 
	 * @author Matej Knopp
	 */
	private static class SessionEntry implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private final String applicationName;

		private final String sessionId;

		private transient List<IManageablePage> pages;
		private transient List<Object> afterReadObject;

		/**
		 * Construct.
		 * 
		 * @param applicationName
		 * @param sessionId
		 */
		public SessionEntry(String applicationName, String sessionId)
		{
			this.applicationName = applicationName;
			this.sessionId = sessionId;
		}

		/**
		 * 
		 * @return page store
		 */
		private IPageStore getPageStore()
		{
			PersistentPageManager manager = managers.get(applicationName);

			if (manager == null)
			{
				return null;
			}

			return manager.pageStore;
		}

		/**
		 * 
		 * @param id
		 * @return null, if not found
		 */
		private IManageablePage findPage(int id)
		{
			for (IManageablePage p : pages)
			{
				if (p.getPageId() == id)
				{
					return p;
				}
			}
			return null;
		}

		/**
		 * Add the page to cached pages if page with same id is not already there
		 * 
		 * @param page
		 */
		private void addPage(IManageablePage page)
		{
			if (page != null)
			{
				if (findPage(page.getPageId()) != null)
				{
					return;
				}

				pages.add(page);
			}
		}

		/**
		 * If the pages are stored in temporary state (after deserialization) this method convert
		 * them to list of "real" pages
		 */
		private void convertAfterReadObjects()
		{
			if (pages == null)
			{
				pages = new ArrayList<IManageablePage>();
			}

			for (Object o : afterReadObject)
			{
				IManageablePage page = getPageStore().convertToPage(o);
				addPage(page);
			}

			afterReadObject = null;
		}

		/**
		 * 
		 * @param id
		 * @return manageable page
		 */
		public synchronized IManageablePage getPage(int id)
		{
			// check if pages are in deserialized state
			if (afterReadObject != null && afterReadObject.isEmpty() == false)
			{
				convertAfterReadObjects();
			}

			// try to find page with same id
			if (pages != null)
			{
				IManageablePage page = findPage(id);
				if (page != null)
				{
					return page;
				}
			}

			// not found, ask pagestore for the page
			return getPageStore().getPage(sessionId, id);
		}

		/**
		 * set the list of pages to remember after the request
		 * 
		 * @param pages
		 */
		public synchronized void setPages(final List<IManageablePage> pages)
		{
			this.pages = new ArrayList<IManageablePage>(pages);
			afterReadObject = null;
		}

		/**
		 * Serializes all pages in this {@link SessionEntry}. If this is http worker thread then
		 * there is available {@link IPageStore} which will be asked to prepare the page for
		 * serialization (see DefaultPageStore$SerializePage). If there is no {@link IPageStore}
		 * available (session loading/persisting in application initialization/destruction thread)
		 * then the pages are serialized without any pre-processing
		 * 
		 * @param s
		 * @throws IOException
		 */
		private void writeObject(final ObjectOutputStream s) throws IOException
		{
			s.defaultWriteObject();

			// prepare for serialization and store the pages
			List<Serializable> l = new ArrayList<Serializable>();
			IPageStore pageStore = getPageStore();
			for (IManageablePage p : pages)
			{
				Serializable preparedPage;
				if (pageStore != null)
				{
					preparedPage = pageStore.prepareForSerialization(sessionId, p);
				}
				else
				{
					preparedPage = p;
				}

				if (preparedPage != null)
				{
					l.add(preparedPage);
				}
			}
			s.writeObject(l);
		}

		/**
		 * Deserializes the pages in this {@link SessionEntry}. If this is http worker thread then
		 * there is available {@link IPageStore} which will be asked to restore the page from its
		 * optimized state (see DefaultPageStore$SerializePage). If there is no {@link IPageStore}
		 * available (session loading/persisting in application initialization/destruction thread)
		 * then the pages are deserialized without any post-processing
		 * 
		 * @param s
		 * @throws IOException
		 * @throws ClassNotFoundException
		 */
		@SuppressWarnings("unchecked")
		private void readObject(final ObjectInputStream s) throws IOException,
			ClassNotFoundException
		{
			s.defaultReadObject();

			afterReadObject = new ArrayList<Object>();

			List<Serializable> l = (List<Serializable>)s.readObject();

			// convert to temporary state after deserialization (will need to be processed
			// by convertAfterReadObject before the pages can be accessed)
			IPageStore pageStore = getPageStore();
			for (Serializable ser : l)
			{
				Object page;
				if (pageStore != null)
				{
					page = pageStore.restoreAfterSerialization(ser);
				}
				else
				{
					page = ser;
				}
				afterReadObject.add(page);
			}
		}
	}

	/**
	 * {@link RequestAdapter} for {@link PersistentPageManager}
	 * 
	 * @author Matej Knopp
	 */
	protected class PersitentRequestAdapter extends RequestAdapter
	{
		private static final String ATTRIBUTE_NAME = "wicket:persistentPageManagerData";

		private String getAttributeName()
		{
			return ATTRIBUTE_NAME + " - " + applicationName;
		}

		/**
		 * Construct.
		 * 
		 * @param context
		 */
		public PersitentRequestAdapter(IPageManagerContext context)
		{
			super(context);
		}

		/**
		 * @see org.apache.wicket.page.RequestAdapter#getPage(int)
		 */
		@Override
		protected IManageablePage getPage(int id)
		{
			// try to get session entry for this session
			SessionEntry entry = getSessionEntry(false);

			if (entry != null)
			{
				return entry.getPage(id);
			}
			else
			{
				return null;
			}
		}

		/**
		 * 
		 * @param create
		 * @return Session Entry
		 */
		private SessionEntry getSessionEntry(boolean create)
		{
			SessionEntry entry = (SessionEntry)getSessionAttribute(getAttributeName());
			if (entry == null && create)
			{
				bind();
				entry = new SessionEntry(applicationName, getSessionId());
			}
			if (entry != null)
			{
				synchronized (entry)
				{
					setSessionAttribute(getAttributeName(), entry);
				}
			}
			return entry;
		}

		/**
		 * @see org.apache.wicket.page.RequestAdapter#newSessionCreated()
		 */
		@Override
		protected void newSessionCreated()
		{
			// if the session is not temporary bind a session entry to it
			if (getSessionId() != null)
			{
				getSessionEntry(true);
			}
		}

		/**
		 * @see org.apache.wicket.page.RequestAdapter#storeTouchedPages(java.util.List)
		 */
		@Override
		protected void storeTouchedPages(final List<IManageablePage> touchedPages)
		{
			if (!touchedPages.isEmpty())
			{
				SessionEntry entry = getSessionEntry(true);
				entry.setPages(touchedPages);
				for (IManageablePage page : touchedPages)
				{
					pageStore.storePage(getSessionId(), page);
				}
			}
		}
	}

	/**
	 * @see org.apache.wicket.page.AbstractPageManager#newRequestAdapter(org.apache.wicket.page.IPageManagerContext)
	 */
	@Override
	protected RequestAdapter newRequestAdapter(IPageManagerContext context)
	{
		return new PersitentRequestAdapter(context);
	}

	/**
	 * @see org.apache.wicket.page.AbstractPageManager#supportsVersioning()
	 */
	@Override
	public boolean supportsVersioning()
	{
		return true;
	}

	/**
	 * @see org.apache.wicket.page.AbstractPageManager#sessionExpired(java.lang.String)
	 */
	@Override
	public void sessionExpired(String sessionId)
	{
		pageStore.unbind(sessionId);
	}

	/**
	 * @see org.apache.wicket.page.IPageManager#destroy()
	 */
	public void destroy()
	{
		managers.remove(applicationName);
		pageStore.destroy();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13990.java