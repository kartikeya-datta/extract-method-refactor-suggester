error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/181.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/181.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/181.java
text:
```scala
R@@owMutation rm = new RowMutation(cfs.table.name, new Row(filter.key, returnCF.cloneMe()));

/**
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
package org.apache.cassandra.db;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.db.columniterator.IColumnIterator;
import org.apache.cassandra.db.columniterator.SimpleAbstractColumnIterator;
import org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy;
import org.apache.cassandra.db.filter.NamesQueryFilter;
import org.apache.cassandra.db.filter.QueryFilter;
import org.apache.cassandra.db.marshal.CounterColumnType;
import org.apache.cassandra.io.sstable.SSTable;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.utils.CloseableIterator;

public class CollationController
{
    private static Logger logger = LoggerFactory.getLogger(CollationController.class);

    private final ColumnFamilyStore cfs;
    private final boolean mutableColumns;
    private final QueryFilter filter;
    private final int gcBefore;

    private int sstablesIterated = 0;

    public CollationController(ColumnFamilyStore cfs, boolean mutableColumns, QueryFilter filter, int gcBefore)
    {
        this.cfs = cfs;
        this.mutableColumns = mutableColumns;
        this.filter = filter;
        this.gcBefore = gcBefore;
    }

    public ColumnFamily getTopLevelColumns()
    {
        return filter.filter instanceof NamesQueryFilter
               && (cfs.metadata.cfType == ColumnFamilyType.Standard || filter.path.superColumnName != null)
               && cfs.metadata.getDefaultValidator() != CounterColumnType.instance
               ? collectTimeOrderedData()
               : collectAllData();
    }

    /**
     * Collects data in order of recency, using the sstable maxtimestamp data.
     * Once we have data for all requests columns that is newer than the newest remaining maxtimestamp,
     * we stop.
     */
    private ColumnFamily collectTimeOrderedData()
    {
        logger.debug("collectTimeOrderedData");

        ISortedColumns.Factory factory = mutableColumns
                                       ? ThreadSafeSortedColumns.factory()
                                       : TreeMapBackedSortedColumns.factory();
        ColumnFamily container = ColumnFamily.create(cfs.metadata, factory, filter.filter.isReversed());
        List<IColumnIterator> iterators = new ArrayList<IColumnIterator>();
        ColumnFamilyStore.ViewFragment view = cfs.markReferenced(filter.key);
        try
        {
            for (Memtable memtable : view.memtables)
            {
                IColumnIterator iter = filter.getMemtableColumnIterator(memtable, cfs.metadata.comparator);
                if (iter != null)
                {
                    iterators.add(iter);
                    container.delete(iter.getColumnFamily());
                    while (iter.hasNext())
                        container.addColumn(iter.next());
                }
            }

            // avoid changing the filter columns of the original filter
            // (reduceNameFilter removes columns that are known to be irrelevant)
            TreeSet<ByteBuffer> filterColumns = new TreeSet<ByteBuffer>(((NamesQueryFilter) filter.filter).columns);
            QueryFilter reducedFilter = new QueryFilter(filter.key, filter.path, new NamesQueryFilter(filterColumns));

            /* add the SSTables on disk */
            Collections.sort(view.sstables, SSTable.maxTimestampComparator);

            // read sorted sstables
            for (SSTableReader sstable : view.sstables)
            {
                long currentMaxTs = sstable.getMaxTimestamp();
                reduceNameFilter(reducedFilter, container, currentMaxTs);
                if (((NamesQueryFilter) reducedFilter.filter).columns.isEmpty())
                    break;

                IColumnIterator iter = reducedFilter.getSSTableColumnIterator(sstable);
                iterators.add(iter);
                if (iter.getColumnFamily() != null)
                {
                    container.delete(iter.getColumnFamily());
                    sstablesIterated++;
                    while (iter.hasNext())
                        container.addColumn(iter.next());
                }
            }

            // we need to distinguish between "there is no data at all for this row" (BF will let us rebuild that efficiently)
            // and "there used to be data, but it's gone now" (we should cache the empty CF so we don't need to rebuild that slower)
            if (iterators.isEmpty())
                return null;

            // do a final collate.  toCollate is boilerplate required to provide a CloseableIterator
            final ColumnFamily c2 = container;
            CloseableIterator<IColumn> toCollate = new SimpleAbstractColumnIterator()
            {
                final Iterator<IColumn> iter = c2.iterator();

                protected IColumn computeNext()
                {
                    return iter.hasNext() ? iter.next() : endOfData();
                }

                public ColumnFamily getColumnFamily()
                {
                    return c2;
                }

                public DecoratedKey getKey()
                {
                    return filter.key;
                }
            };
            ColumnFamily returnCF = container.cloneMeShallow();
            filter.collateColumns(returnCF, Collections.singletonList(toCollate), cfs.metadata.comparator, gcBefore);

            // "hoist up" the requested data into a more recent sstable
            if (sstablesIterated >= cfs.getMinimumCompactionThreshold() && cfs.getCompactionStrategy() instanceof SizeTieredCompactionStrategy)
            {
                RowMutation rm = new RowMutation(cfs.table.name, new Row(filter.key, returnCF));
                try
                {
                    rm.applyUnsafe(); // skipping commitlog is fine since we're just de-fragmenting existing data
                }
                catch (IOException e)
                {
                    // log and allow the result to be returned
                    logger.error("Error re-writing read results", e);
                }
            }

            // Caller is responsible for final removeDeletedCF.  This is important for cacheRow to work correctly:
            return returnCF;
        }
        finally
        {
            for (IColumnIterator iter : iterators)
                FileUtils.closeQuietly(iter);
            SSTableReader.releaseReferences(view.sstables);
        }
    }

    /**
     * remove columns from @param filter where we already have data in @param returnCF newer than @param sstableTimestamp
     */
    private void reduceNameFilter(QueryFilter filter, ColumnFamily returnCF, long sstableTimestamp)
    {
        AbstractColumnContainer container = filter.path.superColumnName == null
                                          ? returnCF
                                          : (SuperColumn) returnCF.getColumn(filter.path.superColumnName);
        if (container == null)
            return;

        for (Iterator<ByteBuffer> iterator = ((NamesQueryFilter) filter.filter).columns.iterator(); iterator.hasNext(); )
        {
            ByteBuffer filterColumn = iterator.next();
            IColumn column = container.getColumn(filterColumn);
            if (column != null && column.timestamp() > sstableTimestamp)
                iterator.remove();
        }
    }

    /**
     * Collects data the brute-force way: gets an iterator for the filter in question
     * from every memtable and sstable, then merges them together.
     */
    private ColumnFamily collectAllData()
    {
        logger.debug("collectAllData");
        ISortedColumns.Factory factory = mutableColumns
                                       ? ThreadSafeSortedColumns.factory()
                                       : ArrayBackedSortedColumns.factory();
        List<IColumnIterator> iterators = new ArrayList<IColumnIterator>();
        ColumnFamily returnCF = ColumnFamily.create(cfs.metadata, factory, filter.filter.isReversed());

        ColumnFamilyStore.ViewFragment view = cfs.markReferenced(filter.key);
        try
        {
            for (Memtable memtable : view.memtables)
            {
                IColumnIterator iter = filter.getMemtableColumnIterator(memtable, cfs.metadata.comparator);
                if (iter != null)
                {
                    returnCF.delete(iter.getColumnFamily());
                    iterators.add(iter);
                }
            }

            for (SSTableReader sstable : view.sstables)
            {
                IColumnIterator iter = filter.getSSTableColumnIterator(sstable);
                iterators.add(iter);
                if (iter.getColumnFamily() != null)
                {
                    returnCF.delete(iter.getColumnFamily());
                    sstablesIterated++;
                }
            }

            // we need to distinguish between "there is no data at all for this row" (BF will let us rebuild that efficiently)
            // and "there used to be data, but it's gone now" (we should cache the empty CF so we don't need to rebuild that slower)
            if (iterators.isEmpty())
                return null;

            filter.collateColumns(returnCF, iterators, cfs.metadata.comparator, gcBefore);

            // Caller is responsible for final removeDeletedCF.  This is important for cacheRow to work correctly:
            return returnCF;
        }
        finally
        {
            for (IColumnIterator iter : iterators)
                FileUtils.closeQuietly(iter);
            SSTableReader.releaseReferences(view.sstables);
        }
    }

    public int getSstablesIterated()
    {
        return sstablesIterated;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/181.java