error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7135.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7135.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7135.java
text:
```scala
private static final L@@ogger log = LoggerFactory.getLogger(AsynchronousDataStore.class);

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
package org.apache.wicket.pageStore;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.wicket.util.lang.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade for {@link IDataStore} that does the actual saving asynchronously (in non-httpWorker
 * thread).
 * <p>
 * Creates an {@link Entry} for each triple (sessionId, pageId, data). Entry are saved using a
 * {@link java.lang.Runnable} implementation called StoreEntryRunnable. Tasks running and thread
 * coordination is managed using a {@linkjava.util.concurrent.ThreadPoolExecutor}
 * 
 * 
 * @author Matej Knopp
 * @author Andrea Del Bene
 */
public class AsynchronousDataStore implements IDataStore
{

	/** Log for reporting. */
	private static final Logger log = LoggerFactory.getLogger(AsyncDataStore.class);

	/**
	 * The maximum number of threads to be started by the {@link #savePagesExecutor}.
	 */
	private static final int MAX_THREADS = 1;

	/**
	 * The wrapped {@link IDataStore} that actually stores that pages
	 */
	private final IDataStore dataStore;

	private final ThreadPoolExecutor savePagesExecutor;

	/**
	 * The queue where the entries which have to be saved are temporary stored
	 */
	private final BlockingQueue<Runnable> entries;

	/**
	 * Construct.
	 * 
	 * @param dataStore
	 *            the wrapped {@link IDataStore} that actually saved the data
	 * @param capacity
	 *            the capacity of the queue that delays the saving
	 */
	public AsynchronousDataStore(final IDataStore dataStore, final int capacity)
	{
		this.dataStore = dataStore;
		entries = new LinkedBlockingQueue<Runnable>(capacity);
		savePagesExecutor = new ThreadPoolExecutor(MAX_THREADS, MAX_THREADS, 1l, TimeUnit.SECONDS,
			entries, new RejectStoringTask());

	}

	/**
	 * @see org.apache.wicket.pageStore.IDataStore#getData(java.lang.String, int)
	 */
	public byte[] getData(final String sessionId, final int pageId)
	{
		Entry entry = null;

		for (Runnable runnable : savePagesExecutor.getQueue())
		{
			StoreEntryRunnable storeEntryRunnable = (StoreEntryRunnable)runnable;
			Entry cursorEntry = storeEntryRunnable.getEntry();

			if (cursorEntry.getPageId() == pageId && cursorEntry.getSessionId().equals(sessionId))
			{
				entry = cursorEntry;
				break;
			}
		}

		final byte[] data;
		if (entry != null)
		{
			data = entry.getData();
			if (log.isDebugEnabled())
			{
				log.debug(
					"Returning the data (with length '{}') of a non-stored entry with sessionId '{}' and pageId '{}'",
					new Object[] { data.length, sessionId, pageId });
			}
		}
		else
		{
			data = dataStore.getData(sessionId, pageId);
			if (log.isDebugEnabled())
			{
				log.debug(
					"Returning the data (with length '{}') of a stored entry with sessionId '{}' and pageId '{}'",
					new Object[] { data.length, sessionId, pageId });
			}
		}
		return data;
	}

	/**
	 * @see org.apache.wicket.pageStore.IDataStore#removeData(java.lang.String, int)
	 */
	public void removeData(final String sessionId, final int pageId)
	{
		for (Runnable runnable : savePagesExecutor.getQueue())
		{
			StoreEntryRunnable storeEntryRunnable = (StoreEntryRunnable)runnable;
			Entry cursorEntry = storeEntryRunnable.getEntry();

			if (cursorEntry.getPageId() == pageId && cursorEntry.getSessionId().equals(sessionId))
			{
				savePagesExecutor.remove(runnable);
			}
		}

		dataStore.removeData(sessionId, pageId);
	}

	/**
	 * @see org.apache.wicket.pageStore.IDataStore#removeData(java.lang.String)
	 */
	public void removeData(final String sessionId)
	{
		for (Runnable runnable : savePagesExecutor.getQueue())
		{
			StoreEntryRunnable storeEntryRunnable = (StoreEntryRunnable)runnable;
			Entry cursorEntry = storeEntryRunnable.getEntry();

			if (cursorEntry.getSessionId().equals(sessionId))
			{
				savePagesExecutor.remove(runnable);
			}
		}

		dataStore.removeData(sessionId);
	}


	/**
	 * Save the entry in the queue if there is a room or directly pass it to the wrapped
	 * {@link IDataStore} if there is no such
	 * 
	 * @see org.apache.wicket.pageStore.IDataStore#storeData(java.lang.String, int, byte[])
	 */
	public void storeData(final String sessionId, final int pageId, final byte[] data)
	{
		Entry entry = new Entry(sessionId, pageId, data);
		StoreEntryRunnable storeEntryRunnable = new StoreEntryRunnable(entry, dataStore);

		savePagesExecutor.execute(storeEntryRunnable);
	}

	/**
	 * @see org.apache.wicket.pageStore.IDataStore#destroy()
	 */
	public void destroy()
	{
		log.debug("Going to shutdown the shutdown the task executor.");
		savePagesExecutor.shutdown();
		try
		{
			boolean stopped = savePagesExecutor.awaitTermination(30, TimeUnit.SECONDS);
			if (stopped == false)
			{
				log.warn("Some tasks didn't stop successfully. They were forcefully stopped.");
				savePagesExecutor.shutdownNow();
			}
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		log.debug("Going to shutdown the shutdown the underlying IDataStore.");
		dataStore.destroy();
	}

	/**
	 * @see org.apache.wicket.pageStore.IDataStore#isReplicated()
	 */
	public boolean isReplicated()
	{
		return dataStore.isReplicated();
	}

	/**
	 * Rejecting task handler.
	 * <p>
	 * If the queue is full a task is rejected and must be saved synchronously in this handler
	 */
	private static class RejectStoringTask implements RejectedExecutionHandler
	{

		public void rejectedExecution(final Runnable runnable, final ThreadPoolExecutor executor)
		{
			StoreEntryRunnable storeEntryRunnable = (StoreEntryRunnable)runnable;
			IDataStore dataStore = storeEntryRunnable.getDataStore();
			Entry entry = storeEntryRunnable.getEntry();

			log.debug("Queue is full. Entry '{}' will be saved synchronously.", entry);
			dataStore.storeData(entry.getSessionId(), entry.getPageId(), entry.getData());
		}

	}

	/**
	 * Task implementation to save a page entry in a separate thread
	 */
	private static class StoreEntryRunnable implements Runnable
	{
		private final Entry entry;
		private final IDataStore dataStore;

		public StoreEntryRunnable(final Entry entry, final IDataStore dataStore)
		{
			this.entry = entry;
			this.dataStore = dataStore;
		}

		public void run()
		{
			log.debug("Saving asynchronously: '{}'...", entry);
			dataStore.storeData(entry.getSessionId(), entry.getPageId(), entry.getData());
		}

		public Entry getEntry()
		{
			return entry;
		}

		public IDataStore getDataStore()
		{
			return dataStore;
		}
	}

	/**
	 * The structure used for an entry in the queue
	 */
	private static class Entry
	{
		private final String sessionId;
		private final int pageId;
		private final byte data[];

		public Entry(final String sessionId, final int pageId, final byte data[])
		{
			this.sessionId = Args.notNull(sessionId, "sessionId");
			this.pageId = pageId;
			this.data = Args.notNull(data, "data");
		}

		public String getSessionId()
		{
			return sessionId;
		}

		public int getPageId()
		{
			return pageId;
		}

		public byte[] getData()
		{
			return data;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + pageId;
			result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry)obj;
			if (pageId != other.pageId)
				return false;
			if (sessionId == null)
			{
				if (other.sessionId != null)
					return false;
			}
			else if (!sessionId.equals(other.sessionId))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "Entry [sessionId=" + sessionId + ", pageId=" + pageId + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7135.java