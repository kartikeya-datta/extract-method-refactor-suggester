error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/211.java
text:
```scala
c@@ache.add(new SoftReference(result));

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
package org.apache.wicket.protocol.http.pagestore;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Page;

/**
 * Cache that stores serialized pages. This is important to make sure that a single page is not
 * serialized twice or more when not necessary.
 * <p>
 * For example a page is serialized during request, but it might be also later serialized on session
 * replication. The purpose of this cache is to make sure that the data obtained from first
 * serialization is reused on second serialization.
 * 
 * @author Matej Knopp
 */
class SerializedPagesCache
{
	/**
	 * Construct.
	 * 
	 * @param size
	 */
	public SerializedPagesCache(final int size)
	{
		this.size = size;
		cache = new ArrayList(size);
	}

	private final int size;

	private final List /* SerializedPageWithSession */cache;

	SerializedPageWithSession removePage(Page page)
	{
		if (size > 0)
		{
			synchronized (cache)
			{
				for (Iterator i = cache.iterator(); i.hasNext();)
				{
					SoftReference ref = (SoftReference) i.next();					
					SerializedPageWithSession entry = (SerializedPageWithSession)ref.get();
					if (entry != null && entry.page.get() == page)
					{
						i.remove();
						return entry;
					}
				}
			}
		}
		return null;
	}

	SerializedPageWithSession getPage(Page page)
	{
		SerializedPageWithSession result = null;
		if (size > 0)
		{
			synchronized (cache)
			{
				for (Iterator i = cache.iterator(); i.hasNext();)
				{
					SoftReference ref = (SoftReference)i.next();
					SerializedPageWithSession entry = (SerializedPageWithSession)ref.get();
					if (entry != null && entry.page.get() == page)
					{
						i.remove();
						result = entry;
						break;
					}
				}

				if (result != null)
				{
					cache.add(result);
				}
			}
		}
		return result;
	}

	SerializedPageWithSession getPage(String sessionId, int pageId, String pageMapName,
		int version, int ajaxVersion)
	{
		if (size > 0)
		{
			synchronized (cache)
			{
				for (Iterator i = cache.iterator(); i.hasNext();)
				{
					SoftReference ref = (SoftReference)i.next();
					SerializedPageWithSession entry = (SerializedPageWithSession)ref.get();
					if (entry != null && entry.sessionId.equals(sessionId) && entry.pageId == pageId &&
						entry.pageMapName.equals(pageMapName) && entry.versionNumber == version &&
						entry.ajaxVersionNumber == ajaxVersion)
					{
						return entry;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Store the serialized page in cache
	 * 
	 * @return
	 * @param sessionId
	 * @param page
	 * @param pagesList
	 */
	SerializedPageWithSession storePage(String sessionId, Page page,
		List /* <SerializedPage> */pagesList)
	{
		SerializedPageWithSession entry = new SerializedPageWithSession(sessionId, page, pagesList);
		SoftReference ref = new SoftReference(entry);

		if (size > 0)
		{
			synchronized (cache)
			{
				removePage(page);
				cache.add(ref);
				if (cache.size() > size)
				{
					cache.remove(0);
				}
			}
		}

		return entry;
	}

	/**
	 * 
	 * @author Matej Knopp
	 */
	static class SerializedPageWithSession implements Serializable
	{
		private static final long serialVersionUID = 1L;

		// this is used for lookup on pagemap serialization. We don't have the
		// session id at that point, because it can happen outside the request
		// thread. We only have the page instance and we need to use it as a key
		final transient WeakReference /* <Page> */page;

		// list of serialized pages
		final List pages;

		final String sessionId;

		// after deserialization, we need to be able to know which page to load
		final int pageId;
		final String pageMapName;
		final int versionNumber;
		final int ajaxVersionNumber;

		SerializedPageWithSession(String sessionId, Page page, List /* <SerializablePage> */pages)
		{
			this.sessionId = sessionId;
			pageId = page.getNumericId();
			pageMapName = page.getPageMapName();
			versionNumber = page.getCurrentVersionNumber();
			ajaxVersionNumber = page.getAjaxVersionNumber();
			this.pages = new ArrayList(pages);
			this.page = new WeakReference(page);
		}

		SerializedPageWithSession(String sessionId, int pageId, String pageMapName,
			int versionNumber, int ajaxVersionNumber, List /* <SerializablePage> */pages)
		{
			this.sessionId = sessionId;
			page = new WeakReference(NO_PAGE);
			this.pageId = pageId;
			this.pageMapName = pageMapName;
			this.versionNumber = versionNumber;
			this.ajaxVersionNumber = ajaxVersionNumber;
			this.pages = pages;
		}

		static Object NO_PAGE = new Object();

		public String toString()
		{
			return getClass().getName() + " [ pageId:" + pageId + ", pageMapName: " + pageMapName +
				", session: " + sessionId + "]";
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/211.java