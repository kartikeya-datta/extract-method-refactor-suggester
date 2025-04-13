error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6628.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6628.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6628.java
text:
```scala
w@@orkDir = (File)((WebApplication)Application.get()).getServletContext()

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.Application;
import wicket.Page;
import wicket.protocol.http.SecondLevelCacheSessionStore.IStore;
import wicket.util.lang.Objects;

/**
 * @author jcompagner
 */
public class FileStore implements IStore
{
	/** log. */
	protected static Log log = LogFactory.getLog(FileStore.class);

	private File workDir;

	/**
	 * Construct.
	 */
	public FileStore()
	{
		workDir = (File)((WebApplication)Application.get()).getWicketServlet().getServletContext()
				.getAttribute("javax.servlet.context.tempdir");
	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IStore#getPage(java.lang.String,
	 *      int, int)
	 */
	public Page getPage(String sessionId, int id, int versionNumber)
	{
		File sessionDir = new File(workDir, sessionId);
		if (sessionDir.exists())
		{
			File pageFile = getPageFile(id, versionNumber, sessionDir);
			if (pageFile.exists())
			{
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
					log.debug("Error loading page " + id + " with version " + versionNumber
							+ " for the sessionid " + sessionId + " from disc", e);
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

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IStore#removePage(java.lang.String,
	 *      wicket.Page)
	 */
	public void removePage(String sessionId, Page page)
	{
		File sessionDir = new File(workDir, sessionId);
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
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IStore#storePage(java.lang.String,
	 *      wicket.Page)
	 */
	public void storePage(String sessionId, Page page)
	{
		File sessionDir = new File(workDir, sessionId);
		sessionDir.mkdirs();
		File pageFile = getPageFile(page.getNumericId(), page.getCurrentVersionNumber(), sessionDir);
		// TODO check can this be called everytime at this place? Putting should
		// be called after the rendering so it should be ok.
		page.detachModels();
		byte[] bytes = Objects.objectToByteArray(page);
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(pageFile);
			ByteBuffer bb = ByteBuffer.wrap(bytes);
			fos.getChannel().write(bb);
		}
		catch (Exception e)
		{
			log.debug("Error saving page " + page.getId() + " with version "
					+ page.getCurrentVersionNumber() + " for the sessionid " + sessionId
					+ " from disc", e);
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

	}

	/**
	 * @see wicket.protocol.http.SecondLevelCacheSessionStore.IStore#unbind(java.lang.String)
	 */
	public void unbind(String sessionId)
	{
		File sessionDir = new File(workDir, sessionId);
		if (sessionDir.exists())
		{
			;
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6628.java