error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16326.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16326.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16326.java
text:
```scala
.@@getAjaxVersionNumber(), page.getPageMapName(), page.getClass());

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
package org.apache.wicket.protocol.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore;
import org.apache.wicket.util.lang.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Stores pages on disk.
 * <p>
 * Override {@link #getWorkDir()} to change the default directory for pages,
 * which is configured from the javax.servlet.context.tempdir attribute in the
 * servlet context.
 * 
 * @author jcompagner
 */
public class TestFilePageStore implements IPageStore
{
	/** log. */
	protected static Logger log = LoggerFactory.getLogger(TestFilePageStore.class);

	public void destroy()
	{
	}

	private final File defaultWorkDir;
	private final String appName;
	
	volatile long totalSavingTime = 0;
	volatile long totalSerializationTime = 0;

	private volatile int saved;

	private volatile int bytesSaved;
	
	/**
	 * Construct.
	 */
	public TestFilePageStore()
	{
		this((File)((WebApplication)Application.get()).getServletContext().getAttribute(
				"javax.servlet.context.tempdir"));
	}

	/**
	 * Construct.
	 * 
	 * @param dir
	 *            The directory to save to.
	 */
	public TestFilePageStore(File dir)
	{
		defaultWorkDir = dir;
		appName = Application.get().getApplicationKey();
	}
	
	private class SessionPageKey
	{
		private final String sessionId;
		private final int id;
		private final int versionNumber;
		private final int ajaxVersionNumber;
		private final String pageMap;
		private final Class pageClass;

		SessionPageKey(String sessionId, Page page)
		{
			this(sessionId, page.getNumericId(), page.getCurrentVersionNumber(), page
					.getAjaxVersionNumber(), page.getPageMap().getName(), page.getClass());
		}

		SessionPageKey(String sessionId, int id, int versionNumber, int ajaxVersionNumber,
				String pagemap, Class pageClass)
		{
			this.sessionId = sessionId;
			this.id = id;
			this.versionNumber = versionNumber;
			this.ajaxVersionNumber = ajaxVersionNumber;
			this.pageClass = pageClass;
			this.pageMap = pagemap;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode()
		{
			return sessionId.hashCode() + id + versionNumber;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj)
		{
			if (obj instanceof SessionPageKey)
			{
				SessionPageKey key = (SessionPageKey)obj;
				return id == key.id
						&& versionNumber == key.versionNumber
						&& ajaxVersionNumber == key.ajaxVersionNumber
						&& ((pageMap != null && pageMap.equals(key.pageMap)) || (pageMap == null && key.pageMap == null))
						&& sessionId.equals(key.sessionId);
			}
			return false;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "SessionPageKey[" + sessionId + "," + id + "," + versionNumber + ","
					+ ajaxVersionNumber + ", " + pageMap + "]";
		}
	}
	
	public Page getPage(String sessionId, String pagemap, int id, int versionNumber, int ajaxVersionNumber)
	{
		SessionPageKey currentKey = new SessionPageKey(sessionId, id, versionNumber, ajaxVersionNumber, pagemap, null);
		File sessionDir = new File(getWorkDir(), sessionId);
		File pageFile = getPageFile(currentKey, sessionDir);
		if (pageFile.exists())
		{
			long t1 = System.currentTimeMillis();
			FileInputStream fis = null;
			try
			{
				byte[] pageData = null;
				fis = new FileInputStream(pageFile);
				int length = (int)pageFile.length();
				ByteBuffer bb = ByteBuffer.allocate(length);
				fis.getChannel().read(bb);
				if (bb.hasArray())
				{
					pageData = bb.array();
				}
				else
				{
					pageData = new byte[length];
					bb.get(pageData);
				}
				long t2 = System.currentTimeMillis();
				Page page = (Page)Objects.byteArrayToObject(pageData);
				page = page.getVersion(versionNumber);
				if (page != null && log.isDebugEnabled())
				{
					long t3 = System.currentTimeMillis();
					log.debug("restoring page " + page.getClass() + "[" + page.getNumericId()
							+ "," + page.getCurrentVersionNumber() + "] size: "
							+ pageData.length + " for session " + sessionId + " took "
							+ (t2 - t1) + " miliseconds to read in and " + (t3 - t2)
							+ " miliseconds to deserialize");
				}
				return page;
			}
			catch (Exception e)
			{
				log.error("Error", e);
			}
		}
		return null;
	}

	public void pageAccessed(String sessionId, Page page)
	{
		
	}

	public void removePage(String sessionId, String pageMapName, int pageId)
	{
	}

	public void storePage(String sessionId, Page page)
	{
		SessionPageKey key = new SessionPageKey(sessionId, page);
		byte[] serialized = Objects.objectToByteArray(page);
		//map.put(key, serialized);
		savePage(key, serialized);
	}

	public void unbind(String sessionId)
	{
	}

	private File getPageFile(SessionPageKey key, File sessionDir)
	{
		return new File(sessionDir, appName + "-pm-" + key.pageMap + "-p-" + key.id + "-v-"
				+ key.versionNumber + "-a-" + key.ajaxVersionNumber);
	}

	private File getWorkDir()
	{
		return defaultWorkDir;
	}
	
	private void savePage(SessionPageKey key, byte[] bytes)
	{
		File sessionDir = new File(getWorkDir(), key.sessionId);
		sessionDir.mkdirs();
		File pageFile = getPageFile(key, sessionDir);

		FileOutputStream fos = null;
		long t1 = System.currentTimeMillis();
		int length = 0;
		try
		{
			fos = new FileOutputStream(pageFile);
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			fos.getChannel().write(bb);
			length = bytes.length;
		}
		catch (Exception e)
		{
			log.error("Error saving page " + key.pageClass + " [" + key.id + ","
					+ key.versionNumber + "] for the sessionid " + key.sessionId);
		}
		finally
		{
			try
			{
				if (fos != null)
				{
					fos.close();
				}
			}
			catch (IOException ex)
			{
				// ignore
			}
		}
		long t3 = System.currentTimeMillis();
		if (log.isDebugEnabled())
		{
			log.debug("storing page " + key.pageClass + "[" + key.id + "," + key.versionNumber
					+ "] size: " + length + " for session " + key.sessionId + " took " + (t3 - t1)
					+ " miliseconds to save");
		}
		totalSavingTime += (t3 - t1);
		saved++;
		bytesSaved += length;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16326.java