error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14402.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14402.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14402.java
text:
```scala
r@@eturn !entry.listeners.add(listener);

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
package org.apache.wicket.util.watch;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.wicket.util.listener.ChangeListenerSet;
import org.apache.wicket.util.listener.IChangeListener;
import org.apache.wicket.util.thread.ICode;
import org.apache.wicket.util.thread.Task;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Monitors one or more <code>IModifiable</code> objects, calling a {@link IChangeListener
 * IChangeListener} when a given object's modification time changes.
 * 
 * @author Jonathan Locke
 * @since 1.2.6
 */
public class ModificationWatcher implements IModificationWatcher
{
	/** logger */
	private static final Logger log = LoggerFactory.getLogger(ModificationWatcher.class);

	/** maps <code>IModifiable</code> objects to <code>Entry</code> objects */
	private final Map<IModifiable, Entry> modifiableToEntry = new ConcurrentHashMap<IModifiable, Entry>();

	/** the <code>Task</code> to run */
	private Task task;

	/**
	 * Container class for holding modifiable entries to watch.
	 */
	private static final class Entry
	{
		// The most recent lastModificationTime polled on the object
		Time lastModifiedTime;

		// The set of listeners to call when the modifiable changes
		final ChangeListenerSet listeners = new ChangeListenerSet();

		// The modifiable thing
		IModifiable modifiable;
	}

	/**
	 * Default constructor for two-phase construction.
	 */
	public ModificationWatcher()
	{
	}

	/**
	 * Constructor that accepts a <code>Duration</code> argument representing the poll frequency.
	 * 
	 * @param pollFrequency
	 *            how often to check on <code>IModifiable</code>s
	 */
	public ModificationWatcher(final Duration pollFrequency)
	{
		start(pollFrequency);
	}

	/**
	 * @see org.apache.wicket.util.watch.IModificationWatcher#add(org.apache.wicket.util.watch.IModifiable,
	 *      org.apache.wicket.util.listener.IChangeListener)
	 */
	public final boolean add(final IModifiable modifiable, final IChangeListener listener)
	{
		// Look up entry for modifiable
		final Entry entry = modifiableToEntry.get(modifiable);

		// Found it?
		if (entry == null)
		{
			Time lastModifiedTime = modifiable.lastModifiedTime();
			if (lastModifiedTime != null)
			{
				// Construct new entry
				final Entry newEntry = new Entry();

				newEntry.modifiable = modifiable;
				newEntry.lastModifiedTime = lastModifiedTime;
				newEntry.listeners.add(listener);

				// Put in map
				modifiableToEntry.put(modifiable, newEntry);
			}
			else
			{
				// The IModifiable is not returning a valid lastModifiedTime
				log.info("Cannot track modifications to resource " + modifiable);
			}

			return true;
		}
		else
		{
			// Add listener to existing entry
			return entry.listeners.add(listener);
		}
	}

	/**
	 * @see org.apache.wicket.util.watch.IModificationWatcher#remove(org.apache.wicket.util.watch.IModifiable)
	 */
	public IModifiable remove(final IModifiable modifiable)
	{
		final Entry entry = modifiableToEntry.remove(modifiable);
		if (entry != null)
		{
			return entry.modifiable;
		}
		return null;
	}

	/**
	 * @see org.apache.wicket.util.watch.IModificationWatcher#start(org.apache.wicket.util.time.Duration)
	 */
	public void start(final Duration pollFrequency)
	{
		// Construct task with the given polling frequency
		task = new Task("ModificationWatcher");

		task.run(pollFrequency, new ICode()
		{
			public void run(final Logger log)
			{
				Iterator<Entry> iter = modifiableToEntry.values().iterator();
				while (iter.hasNext())
				{
					final Entry entry = iter.next();

					// If the modifiable has been modified after the last known
					// modification time
					final Time modifiableLastModified = entry.modifiable.lastModifiedTime();
					if ((modifiableLastModified != null) &&
						modifiableLastModified.after(entry.lastModifiedTime))
					{
						// Notify all listeners that the modifiable was modified
						entry.listeners.notifyListeners();

						// Update timestamp
						entry.lastModifiedTime = modifiableLastModified;
					}
				}
			}
		});
	}

	/**
	 * @see org.apache.wicket.util.watch.IModificationWatcher#destroy()
	 */
	public void destroy()
	{
		if (task != null)
		{
			// task.stop();
			task.interrupt();
		}
	}

	/**
	 * @see org.apache.wicket.util.watch.IModificationWatcher#getEntries()
	 */
	public final Set<IModifiable> getEntries()
	{
		return modifiableToEntry.keySet();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14402.java