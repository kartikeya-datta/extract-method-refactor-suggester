error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[153,80]

error in qdox parser
file content:
```java
offset: 4661
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17798.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.util.watch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.util.listener.ChangeListenerSet;
import wicket.util.listener.IChangeListener;
import wicket.util.thread.ICode;
import wicket.util.thread.Task;
import wicket.util.time.Duration;
import wicket.util.time.Time;


/**
 * Monitors changes to changeables, calling a change listener when a changeable changes.
 * @author Jonathan Locke
 */
public final class Watcher
{
    // Code broadcaster for reporting
    private static final Log log = LogFactory.getLog(Watcher.class);

    // The changeable to entry map
    private final Map changeableToEntry = new HashMap();

    /**
     * For two-phase construction
     */
    public Watcher()
    {
    }

    /**
     * Constructor
     * @param pollFrequency How often to check on changeables
     */
    public Watcher(final Duration pollFrequency)
    {
        start(pollFrequency);
    }

    /**
     * Start watching at a given polling rate
     * @param pollFrequency The poll rate
     */
    public void start(final Duration pollFrequency)
    {
        // Construct task with the given polling frequency
        final Task task = new Task("Watcher");

        task.run(pollFrequency, new ICode()
        {
            public void run(final Log codeListener)
            {
                for (final Iterator iterator = changeableToEntry.values().iterator(); iterator
                        .hasNext();)
                {
                    // Get next entry
                    final Entry entry = (Entry) iterator.next();

                    // If the changeable has been modified after the last known
                    // modification time
                    final Time changeableLastModified = entry.changeable.lastModifiedTime();

                    if (changeableLastModified.after(entry.lastModifiedTime))
                    {
                        // Notify all listeners that the changeable changed
                        entry.listeners.notifyListeners();

                        // Update timestamp
                        entry.lastModifiedTime = changeableLastModified;
                    }
                }
            }
        });
    }

    /**
     * Adds a changeable and change listener to this monitor
     * @param changeable The changeable thing to monitor
     * @param listener The listener to call if the changeable changes
     */
    public final void add(final IChangeable changeable, final IChangeListener listener)
    {
        // Look up entry for changeable
        final Entry entry = (Entry) changeableToEntry.get(changeable);

        // Found it?
        if (entry == null)
        {
            if (changeable.lastModifiedTime() != null)
            {
                // Construct new entry
                final Entry newEntry = new Entry();

                newEntry.changeable = changeable;
                newEntry.lastModifiedTime = changeable.lastModifiedTime();
                newEntry.listeners.add(listener);

                // Put in map
                changeableToEntry.put(changeable, newEntry);
            }
            else
            {
                log.info("Cannot track changes to resource " + changeable);
            }
        }
        else
        {
            // Add listener to existing entry
            entry.listeners.add(listener);
        }
    }

    // Container class for holding changeable entries to watch
    private static final class Entry
    {
        // The changeable
        IChangeable changeable;

        // The last time the changeable was changed
        Time lastModifiedTime;

        // The set of listeners to call when the changeable changes
        final ChangeListenerSet listeners = new ChangeListenerSet();
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17798.java