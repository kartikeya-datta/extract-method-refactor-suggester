error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/187.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/187.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/187.java
text:
```scala
A@@bstractCompactedRow compactedRow = controller.getCompactedRow(new ArrayList<SSTableIdentityIterator>(rows));

package org.apache.cassandra.db.compaction;
/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */


import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.iterators.CollatingIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.sstable.SSTableIdentityIterator;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.sstable.SSTableScanner;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.ReducingIterator;

public class CompactionIterator extends ReducingIterator<SSTableIdentityIterator, AbstractCompactedRow>
implements Closeable, CompactionInfo.Holder
{
    private static Logger logger = LoggerFactory.getLogger(CompactionIterator.class);

    public static final int FILE_BUFFER_SIZE = 1024 * 1024;

    protected final List<SSTableIdentityIterator> rows = new ArrayList<SSTableIdentityIterator>();
    protected final CompactionType type;
    protected final CompactionController controller;

    private long totalBytes;
    private long bytesRead;
    private long row;

    // the bytes that had been compacted the last time we delayed to throttle,
    // and the time in milliseconds when we last throttled
    private long bytesAtLastDelay;
    private long timeAtLastDelay;

    // current target bytes to compact per millisecond
    private int targetBytesPerMS = -1;

    public CompactionIterator(CompactionType type, Iterable<SSTableReader> sstables, CompactionController controller) throws IOException
    {
        this(type, getCollatingIterator(sstables), controller);
    }

    @SuppressWarnings("unchecked")
    protected CompactionIterator(CompactionType type, Iterator iter, CompactionController controller)
    {
        super(iter);
        this.type = type;
        this.controller = controller;
        row = 0;
        totalBytes = bytesRead = 0;
        for (SSTableScanner scanner : getScanners())
        {
            totalBytes += scanner.getFileLength();
        }
    }

    @SuppressWarnings("unchecked")
    protected static CollatingIterator getCollatingIterator(Iterable<SSTableReader> sstables) throws IOException
    {
        // TODO CollatingIterator iter = FBUtilities.<SSTableIdentityIterator>getCollatingIterator();
        CollatingIterator iter = FBUtilities.getCollatingIterator();
        for (SSTableReader sstable : sstables)
        {
            iter.addIterator(sstable.getDirectScanner(FILE_BUFFER_SIZE));
        }
        return iter;
    }

    public CompactionInfo getCompactionInfo()
    {
        return new CompactionInfo(controller.getKeyspace(),
                                  controller.getColumnFamily(),
                                  type,
                                  bytesRead,
                                  totalBytes);
    }

    @Override
    protected boolean isEqual(SSTableIdentityIterator o1, SSTableIdentityIterator o2)
    {
        return o1.getKey().equals(o2.getKey());
    }

    public void reduce(SSTableIdentityIterator current)
    {
        rows.add(current);
    }

    protected AbstractCompactedRow getReduced()
    {
        assert rows.size() > 0;

        try
        {
            AbstractCompactedRow compactedRow = controller.getCompactedRow(rows);
            if (compactedRow.isEmpty())
            {
                controller.invalidateCachedRow(compactedRow.key);
                return null;
            }

            // If the raw is cached, we call removeDeleted on it to have/ coherent query returns. However it would look
            // like some deleted columns lived longer than gc_grace + compaction. This can also free up big amount of
            // memory on long running instances
            controller.removeDeletedInCache(compactedRow.key);

            return compactedRow;
        }
        finally
        {
            rows.clear();
            if ((row++ % controller.getThrottleResolution()) == 0)
            {
                bytesRead = 0;
                for (SSTableScanner scanner : getScanners())
                    bytesRead += scanner.getFilePointer();
                throttle();
            }
        }
    }

    private void throttle()
    {
        if (DatabaseDescriptor.getCompactionThroughputMbPerSec() < 1 || StorageService.instance.isBootstrapMode())
            // throttling disabled
            return;
        int totalBytesPerMS = DatabaseDescriptor.getCompactionThroughputMbPerSec() * 1024 * 1024 / 1000;

        // bytes compacted and time passed since last delay
        long bytesSinceLast = bytesRead - bytesAtLastDelay;
        long msSinceLast = System.currentTimeMillis() - timeAtLastDelay;

        // determine the current target
        int newTarget = totalBytesPerMS /
            Math.max(1, CompactionManager.instance.getActiveCompactions());
        if (newTarget != targetBytesPerMS)
            logger.debug("{} now compacting at {} bytes/ms.", this, newTarget);
        targetBytesPerMS = newTarget;

        // the excess bytes that were compacted in this period
        long excessBytes = bytesSinceLast - msSinceLast * targetBytesPerMS;

        // the time to delay to recap the deficit
        long timeToDelay = excessBytes / Math.max(1, targetBytesPerMS);
        if (timeToDelay > 0)
        {
            if (logger.isTraceEnabled())
                logger.trace(String.format("Compacted %d bytes in %d ms: throttling for %d ms",
                                           bytesSinceLast, msSinceLast, timeToDelay));
            try
            {
                Thread.sleep(timeToDelay);
            }
            catch (InterruptedException e)
            {
                throw new AssertionError(e);
            }
        }
        bytesAtLastDelay = bytesRead;
        timeAtLastDelay = System.currentTimeMillis();
    }

    public void close() throws IOException
    {
        FileUtils.close(getScanners());
    }

    protected Iterable<SSTableScanner> getScanners()
    {
        return ((CollatingIterator)source).getIterators();
    }

    public String toString()
    {
        return this.getCompactionInfo().toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/187.java