error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15951.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15951.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15951.java
text:
```scala
private final S@@tring path;

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
package org.apache.wicket.util.file;

import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.Vector;

/**
 * Keeps track of files awaiting deletion, and deletes them when an associated marker
 * object is reclaimed by the garbage collector.
 * 
 * @author Noel Bergman
 * @author Martin Cooper
 */
public class FileCleaner
{
	/**
	 * Queue of <code>Tracker</code> instances being watched.
	 */
	private static ReferenceQueue /* Tracker */q = new ReferenceQueue();

	/**
	 * Collection of <code>Tracker</code> instances in existence.
	 */
	private static Collection /* Tracker */trackers = new Vector();

	/**
	 * The thread that will clean up registered files.
	 */
	private static Thread reaper = new Thread("File Reaper")
	{
		/**
		 * Run the reaper thread that will delete files as their associated marker objects
		 * are reclaimed by the garbage collector.
		 */
		public void run()
		{
			for (;;)
			{
				Tracker tracker = null;
				try
				{
					// Wait for a tracker to remove.
					tracker = (Tracker)q.remove();
				}
				catch (Exception e)
				{
					continue;
				}

				tracker.delete();
				tracker.clear();
				trackers.remove(tracker);
			}
		}
	};

	/**
	 * The static initializer that starts the reaper thread.
	 */
	static
	{
		reaper.setPriority(Thread.MAX_PRIORITY);
		reaper.setDaemon(true);
		reaper.start();
	}

	/**
	 * Stop the daemon thread
	 */
	public static void destroy()
	{
		if (reaper != null)
		{
			reaper.interrupt();
			
			// TODO Do we need to manually remove the temp files now?
		}
	}
	
	/**
	 * Track the specified file, using the provided marker, deleting the file when the
	 * marker instance is garbage collected.
	 * @param file The file to be tracked.
	 * @param marker The marker object used to track the file.
	 */
	public static void track(File file, Object marker)
	{
		trackers.add(new Tracker(file, marker, q));
	}

	/**
	 * Track the specified file, using the provided marker, deleting the file when the
	 * marker instance is garbage collected.
	 * @param path The full path to the file to be tracked.
	 * @param marker The marker object used to track the file.
	 */
	public static void track(String path, Object marker)
	{
		trackers.add(new Tracker(path, marker, q));
	}

	/**
	 * Retrieve the number of files currently being tracked, and therefore awaiting
	 * deletion.
	 * @return the number of files being tracked.
	 */
	public static int getTrackCount()
	{
		return trackers.size();
	}

	/**
	 * Inner class which acts as the reference for a file pending deletion.
	 */
	private static class Tracker extends PhantomReference
	{

		/**
		 * The full path to the file being tracked.
		 */
		private String path;

		/**
		 * Constructs an instance of this class from the supplied parameters.
		 * @param file The file to be tracked.
		 * @param marker The marker object used to track the file.
		 * @param queue The queue on to which the tracker will be pushed.
		 */
		public Tracker(File file, Object marker, ReferenceQueue queue)
		{
			this(file.getPath(), marker, queue);
		}

		/**
		 * Constructs an instance of this class from the supplied parameters.
		 * @param path The full path to the file to be tracked.
		 * @param marker The marker object used to track the file.
		 * @param queue The queue on to which the tracker will be pushed.
		 */
		public Tracker(String path, Object marker, ReferenceQueue queue)
		{
			super(marker, queue);
			this.path = path;
		}

		/**
		 * Deletes the file associated with this tracker instance.
		 * @return <code>true</code> if the file was deleted successfully;
		 *         <code>false</code> otherwise.
		 */
		public boolean delete()
		{
			return new File(path).delete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15951.java