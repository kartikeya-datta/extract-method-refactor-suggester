error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/895.java
text:
```scala
v@@oid clearUnsafe()

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.ArrayUtils;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.io.DataOutputBuffer;
import org.apache.cassandra.io.SSTable;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.BloomFilter;
import org.apache.cassandra.utils.DestructivePQIterator;
import org.apache.log4j.Logger;

/**
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */

public class Memtable implements Comparable<Memtable>
{
	private static Logger logger_ = Logger.getLogger( Memtable.class );

    private boolean isFrozen_;
    private volatile boolean isDirty_;
    private volatile boolean isFlushed_; // for tests, in particular forceBlockingFlush asserts this

    private int threshold_ = DatabaseDescriptor.getMemtableSize()*1024*1024;
    private int thresholdCount_ = (int)(DatabaseDescriptor.getMemtableObjectCount()*1024*1024);
    private AtomicInteger currentSize_ = new AtomicInteger(0);
    private AtomicInteger currentObjectCount_ = new AtomicInteger(0);

    /* Table and ColumnFamily name are used to determine the ColumnFamilyStore */
    private String table_;
    private String cfName_;
    /* Creation time of this Memtable */
    private long creationTime_;
    private Map<String, ColumnFamily> columnFamilies_ = new HashMap<String, ColumnFamily>();
    /* Lock and Condition for notifying new clients about Memtable switches */

    Memtable(String table, String cfName)
    {
        table_ = table;
        cfName_ = cfName;
        creationTime_ = System.currentTimeMillis();
    }

    public boolean isFlushed()
    {
        return isFlushed_;
    }

    /**
     * Compares two Memtable based on creation time.
     * @param rhs Memtable to compare to.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Memtable rhs)
    {
    	long diff = creationTime_ - rhs.creationTime_;
    	if ( diff > 0 )
    		return 1;
    	else if ( diff < 0 )
    		return -1;
    	else
    		return 0;
    }

    public int getCurrentSize()
    {
        return currentSize_.get();
    }
    
    public int getCurrentObjectCount()
    {
        return currentObjectCount_.get();
    }

    void resolveSize(int oldSize, int newSize)
    {
        currentSize_.addAndGet(newSize - oldSize);
    }

    void resolveCount(int oldCount, int newCount)
    {
        currentObjectCount_.addAndGet(newCount - oldCount);
    }

    boolean isThresholdViolated()
    {
        return currentSize_.get() >= threshold_ ||  currentObjectCount_.get() >= thresholdCount_;
    }

    String getColumnFamily()
    {
    	return cfName_;
    }

    synchronized void enqueueFlush(CommitLog.CommitLogContext cLogCtx)
    {
        if (!isFrozen_)
        {
            isFrozen_ = true;
            ColumnFamilyStore.submitFlush(this, cLogCtx);
        }
    }

    /**
     * Should only be called by ColumnFamilyStore.apply.  NOT a public API.
     * (CFS handles locking to avoid submitting an op
     *  to a flushing memtable.  Any other way is unsafe.)
    */
    void put(String key, ColumnFamily columnFamily)
    {
        assert !isFrozen_; // not 100% foolproof but hell, it's an assert
        isDirty_ = true;
        resolve(key, columnFamily);
    }

    /*
     * This version is used to switch memtable and force flush.
     * Flushing is still done in a separate executor -- forceFlush only blocks
     * until the flush runnable is queued.
    */
    public void forceflush()
    {
        if (isClean())
            return;

        try
        {
            Table.open(table_).getColumnFamilyStore(cfName_).switchMemtable();
            enqueueFlush(CommitLog.open(table_).getContext());
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /** flush synchronously (in the current thread, not on the executor).
     *  only the recover code should call this. */
    void flushOnRecovery() throws IOException {
        if (!isClean())
            flush(CommitLog.CommitLogContext.NULL);
    }

    private void resolve(String key, ColumnFamily columnFamily)
    {
    	ColumnFamily oldCf = columnFamilies_.get(key);
        if ( oldCf != null )
        {
            int oldSize = oldCf.size();
            int oldObjectCount = oldCf.getColumnCount();
            oldCf.addColumns(columnFamily);
            int newSize = oldCf.size();
            int newObjectCount = oldCf.getColumnCount();
            resolveSize(oldSize, newSize);
            resolveCount(oldObjectCount, newObjectCount);
            oldCf.delete(columnFamily);
        }
        else
        {
            columnFamilies_.put(key, columnFamily);
            currentSize_.addAndGet(columnFamily.size() + key.length());
            currentObjectCount_.addAndGet(columnFamily.getColumnCount());
        }
    }

    // for debugging
    public String contents()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<String, ColumnFamily> entry : columnFamilies_.entrySet())
        {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

    ColumnFamily getLocalCopy(String key, String columnFamilyColumn, IFilter filter)
    {
    	String[] values = RowMutation.getColumnAndColumnFamily(columnFamilyColumn);
    	ColumnFamily columnFamily = null;
        if(values.length == 1 )
        {
        	columnFamily = columnFamilies_.get(key);
        }
        else
        {
        	ColumnFamily cFamily = columnFamilies_.get(key);
        	if (cFamily == null) return null;

        	if (values.length == 2) {
                IColumn column = cFamily.getColumn(values[1]); // super or normal column
                if (column != null )
                {
                    columnFamily = cFamily.cloneMeShallow();
                    columnFamily.addColumn(column);
                }
        	}
            else
            {
                assert values.length == 3;
                SuperColumn superColumn = (SuperColumn)cFamily.getColumn(values[1]);
                if (superColumn != null)
                {
                    IColumn subColumn = superColumn.getSubColumn(values[2]);
                    if (subColumn != null)
                    {
                        columnFamily = cFamily.cloneMeShallow();
                        SuperColumn container = new SuperColumn(superColumn.name());
                        container.markForDeleteAt(superColumn.getLocalDeletionTime(), superColumn.getMarkedForDeleteAt());
                        container.addColumn(subColumn);
                        columnFamily.addColumn(container);
                    }
                }
        	}
        }
        /* Filter unnecessary data from the column based on the provided filter */
        return filter.filter(columnFamilyColumn, columnFamily);
    }

    void flush(CommitLog.CommitLogContext cLogCtx) throws IOException
    {
        logger_.info("Flushing " + this);
        ColumnFamilyStore cfStore = Table.open(table_).getColumnFamilyStore(cfName_);

        String directory = DatabaseDescriptor.getDataFileLocation();
        String filename = cfStore.getTempFileName();
        SSTable ssTable = new SSTable(directory, filename, StorageService.getPartitioner());

        // sort keys in the order they would be in when decorated
        final IPartitioner partitioner = StorageService.getPartitioner();
        final Comparator<String> dc = partitioner.getDecoratedKeyComparator();
        ArrayList<String> orderedKeys = new ArrayList<String>(columnFamilies_.keySet());
        Collections.sort(orderedKeys, new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return dc.compare(partitioner.decorateKey(o1), partitioner.decorateKey(o2));
            }
        });
        DataOutputBuffer buffer = new DataOutputBuffer();
        /* Use this BloomFilter to decide if a key exists in a SSTable */
        BloomFilter bf = new BloomFilter(columnFamilies_.size(), 15);
        for (String key : orderedKeys)
        {
            buffer.reset();
            ColumnFamily columnFamily = columnFamilies_.get(key);
            if ( columnFamily != null )
            {
                /* serialize the cf with column indexes */
                ColumnFamily.serializerWithIndexes().serialize( columnFamily, buffer );
                /* Now write the key and value to disk */
                ssTable.append(partitioner.decorateKey(key), buffer);
                bf.add(key);
            }
        }
        ssTable.closeRename(bf);
        cfStore.onMemtableFlush(cLogCtx);
        cfStore.storeLocation( ssTable.getDataFileLocation(), bf );
        buffer.close();
        isFlushed_ = true;
        logger_.info("Completed flushing " + this);
    }

    public String toString()
    {
        return "Memtable(" + cfName_ + ")@" + hashCode();
    }

    /**
     * there does not appear to be any data structure that we can pass to PriorityQueue that will
     * get it to heapify in-place instead of copying first, so we might as well return a Set.
    */
    Set<String> getKeys() throws ExecutionException, InterruptedException
    {
        return new HashSet<String>(columnFamilies_.keySet());
    }

    public static Iterator<String> getKeyIterator(Set<String> keys)
    {
        if (keys.size() == 0)
        {
            // cannot create a PQ of size zero (wtf?)
            return Arrays.asList(new String[0]).iterator();
        }
        PriorityQueue<String> pq = new PriorityQueue<String>(keys.size(), StorageService.getPartitioner().getDecoratedKeyComparator());
        pq.addAll(keys);
        return new DestructivePQIterator<String>(pq);
    }

    public boolean isClean()
    {
        // executor taskcount is inadequate for our needs here -- it can return zero under certain
        // race conditions even though a task has been processed.
        return !isDirty_;
    }

    /**
     * obtain an iterator of columns in this memtable in the specified order starting from a given column.
     */
    ColumnIterator getColumnIterator(final String key, final String cfName, final boolean isAscending, String startColumn)
    {
        ColumnFamily cf = columnFamilies_.get(key);
        final ColumnFamily columnFamily;
        if (cf != null)
            columnFamily = cf.cloneMeShallow();
        else
            columnFamily = new ColumnFamily(cfName, DatabaseDescriptor.getColumnFamilyType(cfName));

        final IColumn columns[] = (cf == null ? columnFamily : cf).getAllColumns().toArray(new IColumn[columnFamily.getAllColumns().size()]);
        // TODO if we are dealing with supercolumns, we need to clone them while we have the read lock since they can be modified later
        if (!isAscending)
            ArrayUtils.reverse(columns);
        IColumn startIColumn;
        if (DatabaseDescriptor.getColumnFamilyType(cfName).equals("Standard"))
            startIColumn = new Column(startColumn);
        else
            startIColumn = new SuperColumn(startColumn);

        // can't use a ColumnComparatorFactory comparator since those compare on both name and time (and thus will fail to match
        // our dummy column, since the time there is arbitrary).
        Comparator<IColumn> comparator = new Comparator<IColumn>()
        {
            public int compare(IColumn column1, IColumn column2)
            {
                return column1.name().compareTo(column2.name());
            }
        };
        if (!isAscending)
        {
            comparator = new ReverseComparator(comparator);
        }
        int index = Arrays.binarySearch(columns, startIColumn, comparator);
        final int startIndex = index < 0 ? -(index + 1) : index;

        return new ColumnIterator()
        {
            private int curIndex_ = startIndex;

            public ColumnFamily getColumnFamily()
            {
                return columnFamily;
            }

            public boolean hasNext()
            {
                return curIndex_ < columns.length;
            }

            public IColumn next()
            {
                return columns[curIndex_++];
            }

            public void close() throws IOException {}

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    public void clearUnsafe()
    {
        columnFamilies_.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/895.java