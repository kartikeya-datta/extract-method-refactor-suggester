error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14023.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14023.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14023.java
text:
```scala
+ (@@t2 - t1) + " miliseconds, bytes saved: " + bytes.length);

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
package wicket.protocol.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wicket.Application;
import wicket.Page;
import wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore;
import wicket.util.lang.Objects;

/**
 * Stores pages on disk.
 * <p>
 * Override {@link #getWorkDir()} to change the default directory for pages,
 * which is configured from the javax.servlet.context.tempdir attribute in the
 * servlet context.
 * 
 * @author jcompagner
 */
public class FilePageStore implements IPageStore
{
	/** log. */
	protected static Logger log = LoggerFactory.getLogger(FilePageStore.class);

	private File defaultWorkDir;

	/**
	 * Construct.
	 */
	public FilePageStore()
	{
		defaultWorkDir = (File)((WebApplication)Application.get()).getServletContext()
				.getAttribute("javax.servlet.context.tempdir");
	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore#getPage(java.lang.String,
	 *      int, int)
	 */
	public Page getPage(String sessionId, int id, int versionNumber)
	{
		File sessionDir = new File(getWorkDir(), sessionId);
		if (sessionDir.exists())
		{
			File pageFile = getPageFile(id, versionNumber, sessionDir);
			if (pageFile.exists())
			{
				// TODO Deleting newer files was not a good idea, as pushing the
				// back button a few times and then pressing refresh, would
				// trigger this delete, while the forward button is still in
				// effect. We should try to come up with an alternative, where
				// we don't serialize and write to the same file all the time,
				// but are sure we do when we need to. This would probably
				// involve keeping meta data or do something smart.

				// int tmp = versionNumber;
				// File f;
				// while ((f = getPageFile(id, tmp++, sessionDir)).exists())
				// {
				// f.delete();
				// }

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

					Page page = (Page)Objects.byteArrayToObject(pageData);
					return page.getVersion(versionNumber);
				}
				catch (Exception e)
				{
					log.debug("Error loading page " + id + "," + versionNumber
							+ " for the sessionid " + sessionId + " from disk", e);
				}
				finally
				{
					try
					{
						if (fis != null)
						{
							fis.close();
						}
					}
					catch (IOException ex)
					{
						// ignore
					}
				}
			}
		}
		return null;
	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore#removePage(java.lang.String,
	 *      wicket.Page)
	 */
	public void removePage(String sessionId, Page page)
	{
		File sessionDir = new File(getWorkDir(), sessionId);
		if (sessionDir.exists())
		{
			File pageFile = getPageFile(page.getNumericId(), page.getCurrentVersionNumber(),
					sessionDir);
			if (pageFile.exists())
			{
				pageFile.delete();
			}
		}
	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore#storePage(java.lang.String,
	 *      wicket.Page)
	 */
	public void storePage(String sessionId, Page page)
	{
		File sessionDir = new File(getWorkDir(), sessionId);
		sessionDir.mkdirs();
		File pageFile = getPageFile(page.getNumericId(), page.getCurrentVersionNumber(), sessionDir);

		// currently, we always have to write, as we don't know whether
		// it is a new version or really the same one

		// only store when not yet stored
		// if (!pageFile.exists())
		// {
		page.detach();
		FileOutputStream fos = null;
		try
		{
			long t1 = System.currentTimeMillis();
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			try
			{
				new ObjectOutputStream(out).writeObject(page);
			}
			finally
			{
				out.close();
			}
			byte[] bytes = out.toByteArray();
			fos = new FileOutputStream(pageFile);
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			fos.getChannel().write(bb);
			if (log.isDebugEnabled())
			{
				long t2 = System.currentTimeMillis();
				log.info("storing page " + page.getNumericId() + ","
						+ page.getCurrentVersionNumber() + " for session " + sessionId + " took "
						+ (t2 - t1) + " miliseconds");
			}
		}
		catch (Exception e)
		{
			log.error("Error saving page " + page.getId() + "," + page.getCurrentVersionNumber()
					+ " for the sessionid " + sessionId, e);
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
		// }
	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore#unbind(java.lang.String)
	 */
	public void unbind(String sessionId)
	{
		File sessionDir = new File(getWorkDir(), sessionId);
		if (sessionDir.exists())
		{
			File[] files = sessionDir.listFiles();
			if (files != null)
			{
				for (File element : files)
				{
					element.delete();
				}
			}
			sessionDir.delete();
		}
	}

	/**
	 * Returns the working directory for this disk-based PageStore. Override
	 * this to configure a different location. The default is
	 * javax.servlet.context.tempdir from the servlet context.
	 * 
	 * @return Working directory
	 */
	protected File getWorkDir()
	{
		return defaultWorkDir;
	}

	/**
	 * @param id
	 * @param versionNumber
	 * @param sessionDir
	 * @return The file pointing to the page
	 */
	private File getPageFile(int id, int versionNumber, File sessionDir)
	{
		return new File(sessionDir, Application.get().getApplicationKey() + "-page-" + id
				+ "-version-" + versionNumber);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14023.java