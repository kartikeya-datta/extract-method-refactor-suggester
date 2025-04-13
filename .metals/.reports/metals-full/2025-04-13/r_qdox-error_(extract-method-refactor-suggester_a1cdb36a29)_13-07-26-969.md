error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/673.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/673.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/673.java
text:
```scala
v@@oid put(String key, byte[] buffer)

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.db;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.io.sstable.SSTableWriter;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.WrappedRunnable;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

public class BinaryMemtable implements IFlushable
{
    private static final Logger logger = LoggerFactory.getLogger(BinaryMemtable.class);
    private final int threshold = DatabaseDescriptor.getBMTThreshold() * 1024 * 1024;
    private final AtomicInteger currentSize = new AtomicInteger(0);

    /* Table and ColumnFamily name are used to determine the ColumnFamilyStore */
    private boolean isFrozen = false;
    private final Map<DecoratedKey, byte[]> columnFamilies = new NonBlockingHashMap<DecoratedKey, byte[]>();
    /* Lock and Condition for notifying new clients about Memtable switches */
    private final Lock lock = new ReentrantLock();
    Condition condition;
    private final IPartitioner partitioner = StorageService.getPartitioner();
    private final ColumnFamilyStore cfs;

    public BinaryMemtable(ColumnFamilyStore cfs)
    {
        this.cfs = cfs;
        condition = lock.newCondition();
    }

    boolean isThresholdViolated()
    {
        return currentSize.get() >= threshold;
    }

    /*
     * This version is used by the external clients to put data into
     * the memtable. This version will respect the threshold and flush
     * the memtable to disk when the size exceeds the threshold.
    */
    void put(String key, byte[] buffer) throws IOException
    {
        if (isThresholdViolated())
        {
            lock.lock();
            try
            {
                if (!isFrozen)
                {
                    isFrozen = true;
                    cfs.submitFlush(this);
                    cfs.switchBinaryMemtable(key, buffer);
                }
                else
                {
                    cfs.applyBinary(key, buffer);
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        else
        {
            resolve(key, buffer);
        }
    }

    public boolean isClean()
    {
        return columnFamilies.isEmpty();
    }

    private void resolve(String key, byte[] buffer)
    {
        columnFamilies.put(partitioner.decorateKey(key), buffer);
        currentSize.addAndGet(buffer.length + key.length());
    }

    private List<DecoratedKey> getSortedKeys()
    {
        assert !columnFamilies.isEmpty();
        logger.info("Sorting " + this);
        List<DecoratedKey> keys = new ArrayList<DecoratedKey>(columnFamilies.keySet());
        Collections.sort(keys);
        return keys;
    }

    private SSTableReader writeSortedContents(List<DecoratedKey> sortedKeys) throws IOException
    {
        logger.info("Writing " + this);
        String path = cfs.getFlushPath();
        SSTableWriter writer = new SSTableWriter(path, sortedKeys.size(), StorageService.getPartitioner());

        for (DecoratedKey key : sortedKeys)
        {
            byte[] bytes = columnFamilies.get(key);
            assert bytes.length > 0;
            writer.append(key, bytes);
        }
        SSTableReader sstable = writer.closeAndOpenReader();
        logger.info("Completed flushing " + writer.getFilename());
        return sstable;
    }

    public void flushAndSignal(final Condition condition, ExecutorService sorter, final ExecutorService writer)
    {
        sorter.submit(new Runnable()
        {
            public void run()
            {
                final List<DecoratedKey> sortedKeys = getSortedKeys();
                writer.submit(new WrappedRunnable()
                {
                    public void runMayThrow() throws IOException
                    {
                        cfs.addSSTable(writeSortedContents(sortedKeys));
                        condition.signalAll();
                    }
                });
            }
        });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/673.java