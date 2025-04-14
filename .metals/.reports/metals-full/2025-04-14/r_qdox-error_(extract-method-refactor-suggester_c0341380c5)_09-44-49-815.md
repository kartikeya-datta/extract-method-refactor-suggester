error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10.java
text:
```scala
i@@f (!page.isPageStateless())

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.protocol.http;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wicket.Application;
import wicket.Page;
import wicket.PageMap;
import wicket.Request;
import wicket.Session;
import wicket.session.pagemap.IPageMapEntry;

/**
 * FIXME document me!
 * 
 * @author jcompagner
 */
public class SecondLevelCacheSessionStore extends HttpSessionStore
{
	private static final class SecondLevelCachePageMap extends PageMap
	{
		private static final long serialVersionUID = 1L;

		Page lastPage = null;

		/**
		 * Construct.
		 * 
		 * @param name
		 * @param session
		 */
		private SecondLevelCachePageMap(String name, Session session)
		{
			super(name, session);
		}

		@Override
		protected void removeEntry(IPageMapEntry entry)
		{
			String sessionId = getSession().getId();
			if (sessionId != null)
			{
				getStore().removePage(sessionId, entry.getPage());
			}
		}

		@Override
		protected void put(Page page)
		{
			if (!page.isStateless())
			{
				String sessionId = getSession().getId();
				if (sessionId != null)
				{
					if (lastPage != page)
					{
						lastPage = page;
						dirty();
					}
					getStore().storePage(sessionId, page);
				}
			}
		}

		@Override
		protected Page get(int id, int versionNumber)
		{
			if (lastPage != null && lastPage.getNumericId() == id)
			{
				Page page = lastPage.getVersion(versionNumber);
				if (page != null)
				{
					return page;
				}
			}
			String sessionId = getSession().getId();
			if (sessionId != null)
			{
				return getStore().getPage(sessionId, id, versionNumber);
			}
			return null;
		}

		private IStore getStore()
		{
			return ((SecondLevelCacheSessionStore)Application.get().getSessionStore()).getStore();
		}
	}

	/**
	 * FIXME document me!
	 */
	public interface IStore
	{

		/**
		 * @param sessionId
		 * @param page
		 */
		void storePage(String sessionId, Page page);

		/**
		 * @param sessionId
		 * @param id
		 * @param versionNumber
		 * @return The page
		 */
		Page getPage(String sessionId, int id, int versionNumber);

		/**
		 * @param sessionId
		 * @param page
		 */
		void removePage(String sessionId, Page page);

		/**
		 * @param sessionId
		 */
		void unbind(String sessionId);

	}

	private final IStore cachingStore;

	/**
	 * Construct.
	 * 
	 * @param store
	 */
	public SecondLevelCacheSessionStore(final IStore store)
	{
		this.cachingStore = new IStore()
		{
			Map<String, SoftReference<Map<String, SoftReference<Page>>>> sessionMap = new ConcurrentHashMap<String, SoftReference<Map<String, SoftReference<Page>>>>();

			public void unbind(String sessionId)
			{
				sessionMap.remove(sessionId);
				store.unbind(sessionId);
			}

			public void removePage(String sessionId, Page page)
			{
				SoftReference sr = sessionMap.get(sessionId);
				if (sr != null)
				{
					Map map = (Map)sr.get();
					if (map != null)
					{
						map.remove(page.getId());
					}
				}
				store.removePage(sessionId, page);
			}

			public Page getPage(String sessionId, int id, int versionNumber)
			{
				SoftReference sr = sessionMap.get(sessionId);
				if (sr != null)
				{
					Map map = (Map)sr.get();
					if (map != null)
					{
						SoftReference sr2 = (SoftReference)map.get(Integer.toString(id));
						if (sr2 != null)
						{
							Page page = (Page)sr2.get();
							if (page != null)
							{
								page = page.getVersion(versionNumber);
							}
							if (page != null)
							{
								return page;
							}
						}
					}
				}
				return store.getPage(sessionId, id, versionNumber);
			}

			public void storePage(String sessionId, Page page)
			{
				Map<String, SoftReference<Page>> pageMap = null;
				SoftReference<Map<String, SoftReference<Page>>> sr = sessionMap.get(sessionId);
				if (sr == null || (pageMap = sr.get()) == null)
				{
					pageMap = new ConcurrentHashMap<String, SoftReference<Page>>();
					sessionMap.put(sessionId, new SoftReference<Map<String, SoftReference<Page>>>(
							pageMap));
				}
				pageMap.put(page.getId(), new SoftReference<Page>(page));
				store.storePage(sessionId, page);
			}
		};
	}

	/**
	 * @see wicket.session.ISessionStore#setAttribute(wicket.Request,
	 *      java.lang.String, java.lang.Object)
	 */
	@Override
	public final void setAttribute(Request request, String name, Object value)
	{
		// ignore all pages, they are stored through the pagemap
		if (!(value instanceof Page))
		{
			super.setAttribute(request, name, value);
		}
	}

	/**
	 * @see wicket.protocol.http.AbstractHttpSessionStore#onUnbind(java.lang.String)
	 */
	@Override
	protected void onUnbind(String sessionId)
	{
		getStore().unbind(sessionId);
	}

	/**
	 * @return The store to use
	 */
	public IStore getStore()
	{
		return cachingStore;
	}

	/**
	 * @see wicket.protocol.http.HttpSessionStore#createPageMap(java.lang.String,
	 *      wicket.Session)
	 */
	@Override
	public PageMap createPageMap(String name, Session session)
	{
		return new SecondLevelCachePageMap(name, session);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10.java