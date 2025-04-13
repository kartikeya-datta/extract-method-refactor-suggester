error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8275.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8275.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8275.java
text:
```scala
public S@@toreSettings(final Application application)

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
package org.apache.wicket.settings.def;

import java.io.File;
import java.io.IOException;

import org.apache.wicket.Application;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.IStoreSettings;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Bytes;

/**
 * The implementation of {@link IStoreSettings}
 */
public class StoreSettings implements IStoreSettings
{
	private static final int DEFAULT_CACHE_SIZE = 40;

	private static final int DEFAULT_FILE_CHANNEL_POOL_CAPACITY = 50;

	private static final Bytes DEFAULT_MAX_SIZE_PER_SESSION = Bytes.megabytes(10);

	private int fileChannelPoolCapacity = DEFAULT_FILE_CHANNEL_POOL_CAPACITY;

	private int inmemoryCacheSize = DEFAULT_CACHE_SIZE;

	private Bytes maxSizePerSession = DEFAULT_MAX_SIZE_PER_SESSION;

	private File fileStoreFolder = null;

	/**
	 * Construct.
	 * 
	 * @param application
	 */
	public StoreSettings(Application application)
	{
	}

	public int getFileChannelPoolCapacity()
	{
		return fileChannelPoolCapacity;
	}

	public void setFileChannelPoolCapacity(int capacity)
	{
		if (capacity < 0)
		{
			throw new IllegalArgumentException(
				"File channel pool capacity must be a positive number.");
		}
		fileChannelPoolCapacity = capacity;
	}

	public int getInmemoryCacheSize()
	{
		return inmemoryCacheSize;
	}

	public void setInmemoryCacheSize(int inmemoryCacheSize)
	{
		this.inmemoryCacheSize = inmemoryCacheSize;
	}

	public Bytes getMaxSizePerSession()
	{
		return maxSizePerSession;
	}

	public void setMaxSizePerSession(final Bytes maxSizePerSession)
	{
		this.maxSizePerSession = Args.notNull(maxSizePerSession, "maxSizePerSession");
	}

	public File getFileStoreFolder()
	{
		if (fileStoreFolder == null)
		{
			if (Application.exists())
			{
				fileStoreFolder = (File)((WebApplication)Application.get()).getServletContext()
					.getAttribute("javax.servlet.context.tempdir");
			}

			if (fileStoreFolder != null)
			{
				return fileStoreFolder;
			}

			try
			{
				fileStoreFolder = File.createTempFile("file-prefix", null).getParentFile();
			}
			catch (IOException e)
			{
				throw new WicketRuntimeException(e);
			}
		}
		return fileStoreFolder;
	}

	public void setFileStoreFolder(final File fileStoreFolder)
	{
		this.fileStoreFolder = Args.notNull(fileStoreFolder, "fileStoreFolder");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8275.java