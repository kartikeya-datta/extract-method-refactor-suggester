error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2174.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2174.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2174.java
text:
```scala
S@@STableReader sstable = writer.closeAndOpenReader(DatabaseDescriptor.getKeysCachedFraction(table_));

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.cassandra.io.SSTableWriter;
import org.apache.cassandra.io.SSTableReader;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.config.DatabaseDescriptor;

import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMap;
import org.apache.cassandra.dht.IPartitioner;

public class BinaryMemtable implements IFlushable<DecoratedKey>
{
    private static Logger logger_ = Logger.getLogger(BinaryMemtable.class);
    private int threshold_ = DatabaseDescriptor.getBMTThreshold() * 1024 * 1024;
    private AtomicInteger currentSize_ = new AtomicInteger(0);

    /* Table and ColumnFamily name are used to determine the ColumnFamilyStore */
    private String table_;
    private String cfName_;
    private boolean isFrozen_ = false;
    private Map<DecoratedKey, byte[]> columnFamilies_ = new NonBlockingHashMap<DecoratedKey, byte[]>();
    /* Lock and Condition for notifying new clients about Memtable switches */
    Lock lock_ = new ReentrantLock();
    Condition condition_;
    private final IPartitioner partitioner_ = StorageService.getPartitioner();

    BinaryMemtable(String table, String cfName) throws IOException
    {
        condition_ = lock_.newCondition();
        table_ = table;
        cfName_ = cfName;
    }

    public int getMemtableThreshold()
    {
        return currentSize_.get();
    }

    void resolveSize(int oldSize, int newSize)
    {
        currentSize_.addAndGet(newSize - oldSize);
    }


    boolean isThresholdViolated()
    {
        return currentSize_.get() >= threshold_;
    }

    String getColumnFamily()
    {
    	return cfName_;
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
            lock_.lock();
            try
            {
                ColumnFamilyStore cfStore = Table.open(table_).getColumnFamilyStore(cfName_);
                if (!isFrozen_)
                {
                    isFrozen_ = true;
                    cfStore.submitFlush(this);
                    cfStore.switchBinaryMemtable(key, buffer);
                }
                else
                {
                    cfStore.applyBinary(key, buffer);
                }
            }
            finally
            {
                lock_.unlock();
            }
        }
        else
        {
            resolve(key, buffer);
        }
    }

    private void resolve(String key, byte[] buffer)
    {
        columnFamilies_.put(partitioner_.decorateKey(key), buffer);
        currentSize_.addAndGet(buffer.length + key.length());
    }

    public List<DecoratedKey> getSortedKeys()
    {
        assert !columnFamilies_.isEmpty();
        logger_.info("Sorting " + this);
        List<DecoratedKey> keys = new ArrayList<DecoratedKey>(columnFamilies_.keySet());
        Collections.sort(keys, partitioner_.getDecoratedKeyComparator());
        return keys;
    }

    public SSTableReader writeSortedContents(List<DecoratedKey> sortedKeys) throws IOException
    {
        logger_.info("Writing " + this);
        ColumnFamilyStore cfStore = Table.open(table_).getColumnFamilyStore(cfName_);
        String path = cfStore.getTempSSTablePath();
        SSTableWriter writer = new SSTableWriter(path, sortedKeys.size(), StorageService.getPartitioner());

        for (DecoratedKey key : sortedKeys)
        {
            byte[] bytes = columnFamilies_.get(key);
            assert bytes.length > 0;
            writer.append(key, bytes);
        }
        SSTableReader sstable = writer.closeAndOpenReader();
        logger_.info("Completed flushing " + writer.getFilename());
        return sstable;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2174.java