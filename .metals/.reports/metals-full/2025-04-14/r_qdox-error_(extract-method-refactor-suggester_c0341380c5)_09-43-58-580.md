error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1965.java
text:
```scala
protected l@@ong minSSTableSize;

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

package org.apache.cassandra.db.compaction;

import java.util.*;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.DataTracker;
import org.apache.cassandra.io.sstable.SSTable;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.utils.Pair;

public class SizeTieredCompactionStrategy extends AbstractCompactionStrategy
{
    private static final Logger logger = LoggerFactory.getLogger(SizeTieredCompactionStrategy.class);
    protected static final long DEFAULT_MIN_SSTABLE_SIZE = 50L * 1024L * 1024L;
    protected static final String MIN_SSTABLE_SIZE_KEY = "min_sstable_size";
    protected static long minSSTableSize;
    protected volatile int estimatedRemainingTasks;

    public SizeTieredCompactionStrategy(ColumnFamilyStore cfs, Map<String, String> options)
    {
       super(cfs, options);
       this.estimatedRemainingTasks = 0;
       String optionValue = options.get(MIN_SSTABLE_SIZE_KEY);
       minSSTableSize = (null != optionValue) ? Long.parseLong(optionValue) : DEFAULT_MIN_SSTABLE_SIZE;
    }

    public AbstractCompactionTask getNextBackgroundTask(final int gcBefore)
    {
        if (cfs.isCompactionDisabled())
        {
            logger.debug("Compaction is currently disabled.");
            return null;
        }

        List<List<SSTableReader>> buckets = getBuckets(createSSTableAndLengthPairs(cfs.getUncompactingSSTables()), minSSTableSize);
        updateEstimatedCompactionsByTasks(buckets);

        List<List<SSTableReader>> prunedBuckets = new ArrayList<List<SSTableReader>>();
        for (List<SSTableReader> bucket : buckets)
        {
            if (bucket.size() < cfs.getMinimumCompactionThreshold())
                continue;

            Collections.sort(bucket, new Comparator<SSTableReader>()
            {
                public int compare(SSTableReader o1, SSTableReader o2)
                {
                    return o1.descriptor.generation - o2.descriptor.generation;
                }
            });
            prunedBuckets.add(bucket.subList(0, Math.min(bucket.size(), cfs.getMaximumCompactionThreshold())));
        }

        if (prunedBuckets.isEmpty())
            return null;

        List<SSTableReader> smallestBucket = Collections.min(prunedBuckets, new Comparator<List<SSTableReader>>()
        {
            public int compare(List<SSTableReader> o1, List<SSTableReader> o2)
            {
                long n = avgSize(o1) - avgSize(o2);
                if (n < 0)
                    return -1;
                if (n > 0)
                    return 1;
                return 0;
            }

            private long avgSize(List<SSTableReader> sstables)
            {
                long n = 0;
                for (SSTableReader sstable : sstables)
                    n += sstable.bytesOnDisk();
                return n / sstables.size();
            }
        });
        return new CompactionTask(cfs, smallestBucket, gcBefore);
    }

    public AbstractCompactionTask getMaximalTask(final int gcBefore)
    {
        return cfs.getSSTables().isEmpty() ? null : new CompactionTask(cfs, cfs.getSSTables(), gcBefore);
    }

    public AbstractCompactionTask getUserDefinedTask(Collection<SSTableReader> sstables, final int gcBefore)
    {
        return new CompactionTask(cfs, sstables, gcBefore)
                .isUserDefined(true)
                .compactionFileLocation(cfs.table.getDataFileLocation(1));
    }

    public int getEstimatedRemainingTasks()
    {
        return estimatedRemainingTasks;
    }

    private static List<Pair<SSTableReader, Long>> createSSTableAndLengthPairs(Collection<SSTableReader> collection)
    {
        List<Pair<SSTableReader, Long>> tableLengthPairs = new ArrayList<Pair<SSTableReader, Long>>();
        for(SSTableReader table: collection)
            tableLengthPairs.add(new Pair<SSTableReader, Long>(table, table.onDiskLength()));
        return tableLengthPairs;
    }

    /*
     * Group files of similar size into buckets.
     */
    static <T> List<List<T>> getBuckets(Collection<Pair<T, Long>> files, long minSSTableSize)
    {
        // Sort the list in order to get deterministic results during the grouping below
        List<Pair<T, Long>> sortedFiles = new ArrayList<Pair<T, Long>>(files);
        Collections.sort(sortedFiles, new Comparator<Pair<T, Long>>()
        {
            public int compare(Pair<T, Long> p1, Pair<T, Long> p2)
            {
                return p1.right.compareTo(p2.right);
            }
        });

        Map<List<T>, Long> buckets = new HashMap<List<T>, Long>();

        for (Pair<T, Long> pair: sortedFiles)
        {
            long size = pair.right;

            boolean bFound = false;
            // look for a bucket containing similar-sized files:
            // group in the same bucket if it's w/in 50% of the average for this bucket,
            // or this file and the bucket are all considered "small" (less than `minSSTableSize`)
            for (Entry<List<T>, Long> entry : buckets.entrySet())
            {
                List<T> bucket = entry.getKey();
                long averageSize = entry.getValue();
                if ((size > (averageSize / 2) && size < (3 * averageSize) / 2)
 (size < minSSTableSize && averageSize < minSSTableSize))
                {
                    // remove and re-add because adding changes the hash
                    buckets.remove(bucket);
                    long totalSize = bucket.size() * averageSize;
                    averageSize = (totalSize + size) / (bucket.size() + 1);
                    bucket.add(pair.left);
                    buckets.put(bucket, averageSize);
                    bFound = true;
                    break;
                }
            }
            // no similar bucket found; put it in a new one
            if (!bFound)
            {
                ArrayList<T> bucket = new ArrayList<T>();
                bucket.add(pair.left);
                buckets.put(bucket, size);
            }
        }

        return new LinkedList<List<T>>(buckets.keySet());
    }

    private void updateEstimatedCompactionsByTasks(List<List<SSTableReader>> tasks)
    {
        int n = 0;
        for (List<SSTableReader> bucket: tasks)
        {
            if (bucket.size() >= cfs.getMinimumCompactionThreshold())
                n += Math.ceil((double)bucket.size() / cfs.getMaximumCompactionThreshold());
        }
        estimatedRemainingTasks = n;
    }

    public long getMinSSTableSize()
    {
        return minSSTableSize;
    }

    public long getMaxSSTableSize()
    {
        return Long.MAX_VALUE;
    }

    public boolean isKeyExistenceExpensive(Set<? extends SSTable> sstablesToIgnore)
    {
        return cfs.getSSTables().size() - sstablesToIgnore.size() > 20;
    }

    public String toString()
    {
        return String.format("SizeTieredCompactionStrategy[%s/%s]",
            cfs.getMinimumCompactionThreshold(),
            cfs.getMaximumCompactionThreshold());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1965.java