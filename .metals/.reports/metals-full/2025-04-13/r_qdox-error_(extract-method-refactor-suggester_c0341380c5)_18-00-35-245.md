error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/31.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/31.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/31.java
text:
```scala
l@@ogger.trace("memtable memory usage is {} bytes with {} live", liveBytes + flushingBytes, liveBytes);

package org.apache.cassandra.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Iterables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;

class MeteredFlusher implements Runnable
{
    private static Logger logger = LoggerFactory.getLogger(MeteredFlusher.class);

    public void run()
    {
        // first, find how much memory non-active memtables are using
        Memtable activelyMeasuring = Memtable.activelyMeasuring;
        long flushingBytes = activelyMeasuring == null ? 0 : activelyMeasuring.getLiveSize();
        flushingBytes += countFlushingBytes();

        // next, flush CFs using more than 1 / (maximum number of memtables it could have in the pipeline)
        // of the total size allotted.  Then, flush other CFs in order of size if necessary.
        long liveBytes = 0;
        try
        {
            for (ColumnFamilyStore cfs : ColumnFamilyStore.all())
            {
                long size = cfs.getTotalMemtableLiveSize();
                int maxInFlight = (int) Math.ceil((double) (1 // live memtable
                                                            + 1 // potentially a flushed memtable being counted by jamm
                                                            + DatabaseDescriptor.getFlushWriters()
                                                            + DatabaseDescriptor.getFlushQueueSize())
                                                  / (1 + cfs.getIndexedColumns().size()));
                if (size > (DatabaseDescriptor.getTotalMemtableSpaceInMB() * 1048576L - flushingBytes) / maxInFlight)
                {
                    logger.info("flushing high-traffic column family {}", cfs);
                    cfs.forceFlush();
                }
                else
                {
                    liveBytes += size;
                }
            }

            if (flushingBytes + liveBytes <= DatabaseDescriptor.getTotalMemtableSpaceInMB() * 1048576L)
                return;

            logger.info("estimated {} bytes used by all memtables pre-flush", liveBytes);

            // sort memtables by size
            List<ColumnFamilyStore> sorted = new ArrayList<ColumnFamilyStore>();
            Iterables.addAll(sorted, ColumnFamilyStore.all());
            Collections.sort(sorted, new Comparator<ColumnFamilyStore>()
            {
                public int compare(ColumnFamilyStore o1, ColumnFamilyStore o2)
                {
                    long size1 = o1.getTotalMemtableLiveSize();
                    long size2 = o2.getTotalMemtableLiveSize();
                    if (size1 < size2)
                        return -1;
                    if (size1 > size2)
                        return 1;
                    return 0;
                }
            });

            // flush largest first until we get below our threshold.
            // although it looks like liveBytes + flushingBytes will stay a constant, it will not if flushes finish
            // while we loop, which is especially likely to happen if the flush queue fills up (so further forceFlush calls block)
            while (true)
            {
                flushingBytes = countFlushingBytes();
                if (liveBytes + flushingBytes <= DatabaseDescriptor.getTotalMemtableSpaceInMB() * 1048576L || sorted.isEmpty())
                    break;

                ColumnFamilyStore cfs = sorted.remove(sorted.size() - 1);
                long size = cfs.getTotalMemtableLiveSize();
                logger.info("flushing {} to free up {} bytes", cfs, size);
                liveBytes -= size;
                cfs.forceFlush();
            }
        }
        finally
        {
            logger.debug("memtable memory usage is {} bytes with {} live", liveBytes + flushingBytes, liveBytes);
        }
    }

    private long countFlushingBytes()
    {
        long flushingBytes = 0;
        for (ColumnFamilyStore cfs : ColumnFamilyStore.all())
        {
            for (Memtable memtable : cfs.getMemtablesPendingFlush())
                flushingBytes += memtable.getLiveSize();
        }
        return flushingBytes;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/31.java