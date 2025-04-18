error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/268.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/268.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/268.java
text:
```scala
T@@imestampReconciler.instance,

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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Iterables;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.concurrent.JMXEnabledThreadPoolExecutor;
import org.apache.cassandra.concurrent.NamedThreadFactory;
import org.apache.cassandra.concurrent.StageManager;
import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.ColumnDefinition;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.IClock.ClockRelationship;
import org.apache.cassandra.db.clock.TimestampReconciler;
import org.apache.cassandra.db.columniterator.IColumnIterator;
import org.apache.cassandra.db.columniterator.IdentityQueryFilter;
import org.apache.cassandra.db.commitlog.CommitLog;
import org.apache.cassandra.db.commitlog.CommitLogSegment;
import org.apache.cassandra.db.filter.*;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.db.marshal.BytesType;
import org.apache.cassandra.db.marshal.LocalByPartionerType;
import org.apache.cassandra.dht.*;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.SSTable;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.io.sstable.SSTableTracker;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.LatencyTracker;
import org.apache.cassandra.utils.WrappedRunnable;

public class ColumnFamilyStore implements ColumnFamilyStoreMBean
{
    private static Logger logger_ = LoggerFactory.getLogger(ColumnFamilyStore.class);

    /*
     * submitFlush first puts [Binary]Memtable.getSortedContents on the flushSorter executor,
     * which then puts the sorted results on the writer executor.  This is because sorting is CPU-bound,
     * and writing is disk-bound; we want to be able to do both at once.  When the write is complete,
     * we turn the writer into an SSTableReader and add it to ssTables_ where it is available for reads.
     *
     * For BinaryMemtable that's about all that happens.  For live Memtables there are two other things
     * that switchMemtable does (which should be the only caller of submitFlush in this case).
     * First, it puts the Memtable into memtablesPendingFlush, where it stays until the flush is complete
     * and it's been added as an SSTableReader to ssTables_.  Second, it adds an entry to commitLogUpdater
     * that waits for the flush to complete, then calls onMemtableFlush.  This allows multiple flushes
     * to happen simultaneously on multicore systems, while still calling onMF in the correct order,
     * which is necessary for replay in case of a restart since CommitLog assumes that when onMF is
     * called, all data up to the given context has been persisted to SSTables.
     */
    private static ExecutorService flushSorter_
            = new JMXEnabledThreadPoolExecutor(1,
                                               Runtime.getRuntime().availableProcessors(),
                                               StageManager.KEEPALIVE,
                                               TimeUnit.SECONDS,
                                               new LinkedBlockingQueue<Runnable>(Runtime.getRuntime().availableProcessors()),
                                               new NamedThreadFactory("FLUSH-SORTER-POOL"));
    private static ExecutorService flushWriter_
            = new JMXEnabledThreadPoolExecutor(1,
                                               DatabaseDescriptor.getFlushWriters(),
                                               StageManager.KEEPALIVE,
                                               TimeUnit.SECONDS,
                                               new LinkedBlockingQueue<Runnable>(DatabaseDescriptor.getFlushWriters()),
                                               new NamedThreadFactory("FLUSH-WRITER-POOL"));
    private static ExecutorService postFlushExecutor_ = new JMXEnabledThreadPoolExecutor("MEMTABLE-POST-FLUSHER");
    
    private static final FilenameFilter DB_NAME_FILTER = new FilenameFilter()
    {
        public boolean accept(File dir, String name)
        {
            return name.matches("[^\\.][\\S]+?[\\.db]");
        }
    };

    private Set<Memtable> memtablesPendingFlush = new ConcurrentSkipListSet<Memtable>();

    public final String table_;
    public final String columnFamily_;
    public final IPartitioner partitioner_;

    private volatile int memtableSwitchCount = 0;

    /* This is used to generate the next index for a SSTable */
    private AtomicInteger fileIndexGenerator_ = new AtomicInteger(0);

    /* active memtable associated with this ColumnFamilyStore. */
    private Memtable memtable_;

    private final Map<byte[], ColumnFamilyStore> indexedColumns_;

    // TODO binarymemtable ops are not threadsafe (do they need to be?)
    private AtomicReference<BinaryMemtable> binaryMemtable_;

    /* SSTables on disk for this column family */
    private SSTableTracker ssTables_;

    private LatencyTracker readStats_ = new LatencyTracker();
    private LatencyTracker writeStats_ = new LatencyTracker();

    final CFMetaData metadata;
    
    private ColumnFamilyStore(String table, String columnFamilyName, IPartitioner partitioner, int generation, CFMetaData metadata)
    {
        assert metadata != null : "null metadata for " + table + ":" + columnFamilyName;
        table_ = table;
        columnFamily_ = columnFamilyName; 
        this.metadata = metadata;
        this.partitioner_ = partitioner;
        fileIndexGenerator_.set(generation);
        memtable_ = new Memtable(this, partitioner_);
        binaryMemtable_ = new AtomicReference<BinaryMemtable>(new BinaryMemtable(this));

        if (logger_.isDebugEnabled())
            logger_.debug("Starting CFS {}", columnFamily_);
        
        // scan for data files corresponding to this CF
        List<File> sstableFiles = new ArrayList<File>();
        
        for (File file : files(table, columnFamilyName))
        {
            if (file.getName().contains("-Data.db"))
            {
                sstableFiles.add(file.getAbsoluteFile());
            }
        }
        Collections.sort(sstableFiles, new FileUtils.FileComparator());

        /* Load the index files and the Bloom Filters associated with them. */
        List<SSTableReader> sstables = new ArrayList<SSTableReader>();
        for (File file : sstableFiles)
        {
            String filename = file.getAbsolutePath();
            if (SSTable.deleteIfCompacted(filename))
                continue;

            SSTableReader sstable;
            try
            {
                sstable = SSTableReader.open(Descriptor.fromFilename(filename), metadata, partitioner_);
            }
            catch (IOException ex)
            {
                logger_.error("Corrupt file " + filename + "; skipped", ex);
                continue;
            }
            sstables.add(sstable);
        }
        ssTables_ = new SSTableTracker(table, columnFamilyName);
        ssTables_.add(sstables);

        indexedColumns_ = new TreeMap<byte[], ColumnFamilyStore>(BytesType.instance);
        for (Map.Entry<byte[], ColumnDefinition> entry : metadata.column_metadata.entrySet())
        {
            byte[] column = entry.getKey();
            ColumnDefinition info = entry.getValue();
            if (info.index_type == null)
                continue;

            String indexedCfName = columnFamily_ + "." + (info.index_name == null ? FBUtilities.bytesToHex(column) : info.index_name);
            IPartitioner rowPartitioner = StorageService.getPartitioner();
            AbstractType columnComparator = (rowPartitioner instanceof OrderPreservingPartitioner || rowPartitioner instanceof ByteOrderedPartitioner)
                                            ? BytesType.instance
                                            : new LocalByPartionerType(StorageService.getPartitioner());
            CFMetaData indexedCfMetadata = new CFMetaData(table,
                                                          indexedCfName,
                                                          ColumnFamilyType.Standard,
                                                          ClockType.Timestamp,
                                                          columnComparator,
                                                          null,
                                                          new TimestampReconciler(),
                                                          "",
                                                          0,
                                                          false,
                                                          0,
                                                          0,
                                                          CFMetaData.DEFAULT_GC_GRACE_SECONDS,
                                                          Collections.<byte[], ColumnDefinition>emptyMap());
            ColumnFamilyStore indexedCfs = ColumnFamilyStore.createColumnFamilyStore(table, 
                                                                                     indexedCfName,
                                                                                     new LocalPartitioner(metadata.column_metadata.get(column).validator),
                                                                                     indexedCfMetadata);
            indexedColumns_.put(column, indexedCfs);
        }
    }
    
    String getMBeanName()
    {
        return "org.apache.cassandra.db:type=ColumnFamilyStores,keyspace=" + table_ + ",columnfamily=" + columnFamily_;
    }

    public long getMinRowSize()
    {
        long min = 0;
        for (SSTableReader sstable : ssTables_)
        {
           if (min == 0 || sstable.getEstimatedRowSize().min() < min)
               min = sstable.getEstimatedRowSize().min();
        }
        return min;
    }

    public long getMaxRowSize()
    {
        long max = 0;
        for (SSTableReader sstable : ssTables_)
        {
            if (sstable.getEstimatedRowSize().max() > max)
                max = sstable.getEstimatedRowSize().max();
        }
        return max;
    }

    public long getMeanRowSize()
    {
        long sum = 0;
        long count = 0;
        for (SSTableReader sstable : ssTables_)
        {
            sum += sstable.getEstimatedRowSize().median();
            count++;
        }
        return count > 0 ? sum / count : 0;
    }

    public int getMeanColumns()
    {
        long sum = 0;
        int count = 0;
        for (SSTableReader sstable : ssTables_)
        {
            sum += sstable.getEstimatedColumnCount().median();
            count++;
        }
        return count > 0 ? (int) (sum / count) : 0;
    }

    public static ColumnFamilyStore createColumnFamilyStore(String table, String columnFamily)
    {
        return createColumnFamilyStore(table, columnFamily, StorageService.getPartitioner(), DatabaseDescriptor.getCFMetaData(table, columnFamily));
    }

    public static synchronized ColumnFamilyStore createColumnFamilyStore(String table, String columnFamily, IPartitioner partitioner, CFMetaData metadata)
    {
        /*
         * Get all data files associated with old Memtables for this table.
         * These files are named as follows <Table>-1.db, ..., <Table>-n.db. Get
         * the max which in this case is n and increment it to use it for next
         * index.
         */
        List<Integer> generations = new ArrayList<Integer>();
        String[] dataFileDirectories = DatabaseDescriptor.getAllDataFileLocationsForTable(table);
        for (String directory : dataFileDirectories)
        {
            File fileDir = new File(directory);
            File[] files = fileDir.listFiles(DB_NAME_FILTER);
            
            for (File file : files)
            {
                if (file.isDirectory())
                    continue;
                String filename = file.getAbsolutePath();
                String cfName = getColumnFamilyFromFileName(filename);

                if (cfName.equals(columnFamily))
                {
                    generations.add(getGenerationFromFileName(filename));
                }
            }
        }
        Collections.sort(generations);
        int value = (generations.size() > 0) ? (generations.get(generations.size() - 1)) : 0;

        return new ColumnFamilyStore(table, columnFamily, partitioner, value, metadata);
    }
    
    // remove unnecessary files from the cf directory. these include temp files, orphans and zero-length files.
    static void scrubDataDirectories(String table, String columnFamily)
    {
        /* look for and remove orphans. An orphan is a -Filter.db or -Index.db with no corresponding -Data.db. */
        Pattern auxFilePattern = Pattern.compile("(.*)(-Filter\\.db$|-Index\\.db$)");
        for (File file : files(table, columnFamily))
        {
            String filename = file.getName();
            Matcher matcher = auxFilePattern.matcher(file.getAbsolutePath());
            if (matcher.matches())
            {
                String basePath = matcher.group(1);
                if (!new File(basePath + "-Data.db").exists())
                {
                    logger_.info(String.format("Removing orphan %s", file.getAbsolutePath()));
                    try
                    {
                        FileUtils.deleteWithConfirm(file);
                    }
                    catch (IOException e)
                    {
                        throw new IOError(e);
                    }
                }
            }
            else if (((file.length() == 0 && !filename.endsWith("-Compacted")) || (filename.contains("-" + SSTable.TEMPFILE_MARKER))))
            {
                try
                {
                    FileUtils.deleteWithConfirm(file);
                }
                catch (IOException e)
                {
                    throw new IOError(e);
                }
            }
        }
    }
    
    // returns runnables that need to update the system table.
    static Collection<Runnable> deleteCompactedFiles(String table, String columnFamily)
    {
        Collection<Runnable> runnables = new ArrayList<Runnable>();
        for (File file : files(table, columnFamily))
        {
            if (file.getName().contains("-Data.db"))
            {
                final String delPath = file.getAbsolutePath();
                if (SSTable.deleteIfCompacted(delPath))
                {
                    runnables.add(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                StatisticsTable.deleteSSTableStatistics(delPath);
                            }
                            catch (IOException ex)
                            {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        }
        return runnables;
    }

    private static Set<File> files(String table, String columnFamily)
    {
        assert table != null;
        assert columnFamily != null;
        Set<File> fileSet = new HashSet<File>();
        for (String directory : DatabaseDescriptor.getAllDataFileLocationsForTable(table))
        {
            File[] files = new File(directory).listFiles(DB_NAME_FILTER);
            if (files == null)
                continue;
            for (File file : files)
            {
                if (file.isDirectory())
                    continue;
                String cfName = getColumnFamilyFromFileName(file.getAbsolutePath());
                if (cfName.equals(columnFamily))
                    fileSet.add(file);
            }
        }
        return fileSet;
    }

    /**
     * @return the name of the column family
     */
    public String getColumnFamilyName()
    {
        return columnFamily_;
    }

    private static String getColumnFamilyFromFileName(String filename)
    {
        return Descriptor.fromFilename(filename).cfname;
    }

    public static int getGenerationFromFileName(String filename)
    {
        return Descriptor.fromFilename(filename).generation;
    }

    /*
     * @return a temporary file name for an sstable.
     * When the sstable object is closed, it will be renamed to a non-temporary
     * format, so incomplete sstables can be recognized and removed on startup.
     */
    public String getFlushPath()
    {
        long guessedSize = 2 * DatabaseDescriptor.getMemtableThroughput() * 1024*1024; // 2* adds room for keys, column indexes
        String location = DatabaseDescriptor.getDataFileLocationForTable(table_, guessedSize);
        if (location == null)
            throw new RuntimeException("Insufficient disk space to flush");
        return getTempSSTablePath(location);
    }

    public String getTempSSTablePath(String directory)
    {
        Descriptor desc = new Descriptor(new File(directory),
                                                         table_,
                                                         columnFamily_,
                                                         fileIndexGenerator_.incrementAndGet(),
                                                         true);
        return desc.filenameFor("Data.db");
    }

    /** flush the given memtable and swap in a new one for its CFS, if it hasn't been frozen already.  threadsafe. */
    Future<?> maybeSwitchMemtable(Memtable oldMemtable, final boolean writeCommitLog)
    {
        /**
         *  If we can get the writelock, that means no new updates can come in and 
         *  all ongoing updates to memtables have completed. We can get the tail
         *  of the log and use it as the starting position for log replay on recovery.
         */
        Table.flusherLock.writeLock().lock();
        try
        {
            if (oldMemtable.isFrozen())
                return null;

            assert memtable_ == oldMemtable;
            memtable_.freeze();
            final CommitLogSegment.CommitLogContext ctx = writeCommitLog ? CommitLog.instance().getContext() : null;
            logger_.info("switching in a fresh Memtable for " + columnFamily_ + " at " + ctx);

            // submit the memtable for any indexed sub-cfses, and our own
            final CountDownLatch latch = new CountDownLatch(1 + indexedColumns_.size());
            for (ColumnFamilyStore cfs : Iterables.concat(indexedColumns_.values(), Collections.singleton(this)))
            {
                submitFlush(cfs.memtable_, latch);
                cfs.memtable_ = new Memtable(cfs, cfs.partitioner_);
            }

            // when all the memtables have been written, including for indexes, mark the flush in the commitlog header.
            // a second executor makes sure the onMemtableFlushes get called in the right order,
            // while keeping the wait-for-flush (future.get) out of anything latency-sensitive.
            return postFlushExecutor_.submit(new WrappedRunnable()
            {
                public void runMayThrow() throws InterruptedException, IOException
                {
                    latch.await();
                    if (writeCommitLog)
                    {
                        // if we're not writing to the commit log, we are replaying the log, so marking
                        // the log header with "you can discard anything written before the context" is not valid
                        logger_.debug("Discarding {}", metadata.cfId);
                        CommitLog.instance().discardCompletedSegments(metadata.cfId, ctx);
                    }
                }
            });
        }
        finally
        {
            Table.flusherLock.writeLock().unlock();
            if (memtableSwitchCount == Integer.MAX_VALUE)
            {
                memtableSwitchCount = 0;
            }
            memtableSwitchCount++;
        }
    }

    void switchBinaryMemtable(DecoratedKey key, byte[] buffer)
    {
        binaryMemtable_.set(new BinaryMemtable(this));
        binaryMemtable_.get().put(key, buffer);
    }

    public void forceFlushIfExpired()
    {
        if (memtable_.isExpired())
            forceFlush();
    }

    public Future<?> forceFlush()
    {
        if (memtable_.isClean())
            return null;

        return maybeSwitchMemtable(memtable_, true);
    }

    public void forceBlockingFlush() throws ExecutionException, InterruptedException
    {
        Future<?> future = forceFlush();
        if (future != null)
            future.get();
    }

    public void forceFlushBinary()
    {
        if (binaryMemtable_.get().isClean())
            return;

        submitFlush(binaryMemtable_.get(), new CountDownLatch(1));
    }

    /**
     * Insert/Update the column family for this key.
     * Caller is responsible for acquiring Table.flusherLock!
     * param @ lock - lock that needs to be used.
     * param @ key - key for update/insert
     * param @ columnFamily - columnFamily changes
     */
    Memtable apply(DecoratedKey key, ColumnFamily columnFamily)
    {
        long start = System.nanoTime();

        boolean flushRequested = memtable_.isThresholdViolated();
        memtable_.put(key, columnFamily);
        writeStats_.addNano(System.nanoTime() - start);
        
        return flushRequested ? memtable_ : null;
    }

    /*
     * Insert/Update the column family for this key. param @ lock - lock that
     * needs to be used. param @ key - key for update/insert param @
     * columnFamily - columnFamily changes
     */
    void applyBinary(DecoratedKey key, byte[] buffer)
    {
        long start = System.nanoTime();
        binaryMemtable_.get().put(key, buffer);
        writeStats_.addNano(System.nanoTime() - start);
    }

    public static ColumnFamily removeDeletedCF(ColumnFamily cf, int gcBefore)
    {
        // in case of a timestamp tie, tombstones get priority over non-tombstones.
        // (we want this to be deterministic to avoid confusion.)
        if (cf.getColumnCount() == 0 && cf.getLocalDeletionTime() <= gcBefore)
            return null;
        return cf;
    }

    /*
     This is complicated because we need to preserve deleted columns, supercolumns, and columnfamilies
     until they have been deleted for at least GC_GRACE_IN_SECONDS.  But, we do not need to preserve
     their contents; just the object itself as a "tombstone" that can be used to repair other
     replicas that do not know about the deletion.
     */
    public static ColumnFamily removeDeleted(ColumnFamily cf, int gcBefore)
    {
        if (cf == null)
        {
            return null;
        }

        removeDeletedColumnsOnly(cf, gcBefore);
        return removeDeletedCF(cf, gcBefore);
    }

    private static void removeDeletedColumnsOnly(ColumnFamily cf, int gcBefore)
    {
        if (cf.isSuper())
            removeDeletedSuper(cf, gcBefore);
        else
            removeDeletedStandard(cf, gcBefore);
    }

    private static void removeDeletedStandard(ColumnFamily cf, int gcBefore)
    {
        for (Map.Entry<byte[], IColumn> entry : cf.getColumnsMap().entrySet())
        {
            byte[] cname = entry.getKey();
            IColumn c = entry.getValue();
            // remove columns if
            // (a) the column itself is tombstoned or
            // (b) the CF is tombstoned and the column is not newer than it
            // (we split the test to avoid computing ClockRelationship if not necessary)
            if ((c.isMarkedForDelete() && c.getLocalDeletionTime() <= gcBefore))
            {
                cf.remove(cname);
            }
            else
            {
                ClockRelationship rel = c.clock().compare(cf.getMarkedForDeleteAt());
                if ((ClockRelationship.LESS_THAN == rel) || (ClockRelationship.EQUAL == rel))
                {
                    cf.remove(cname);
                }
            }
        }
    }

    private static void removeDeletedSuper(ColumnFamily cf, int gcBefore)
    {
        // TODO assume deletion means "most are deleted?" and add to clone, instead of remove from original?
        // this could be improved by having compaction, or possibly even removeDeleted, r/m the tombstone
        // once gcBefore has passed, so if new stuff is added in it doesn't used the wrong algorithm forever
        for (Map.Entry<byte[], IColumn> entry : cf.getColumnsMap().entrySet())
        {
            SuperColumn c = (SuperColumn) entry.getValue();
            List<IClock> clocks = Arrays.asList(cf.getMarkedForDeleteAt());
            IClock minClock = c.getMarkedForDeleteAt().getSuperset(clocks);
            for (IColumn subColumn : c.getSubColumns())
            {
                // remove subcolumns if
                // (a) the subcolumn itself is tombstoned or
                // (b) the supercolumn is tombstoned and the subcolumn is not newer than it
                // (we split the test to avoid computing ClockRelationship if not necessary)
                if (subColumn.isMarkedForDelete() && subColumn.getLocalDeletionTime() <= gcBefore)
                {
                    c.remove(subColumn.name());
                }
                else
                {
                    ClockRelationship subRel = subColumn.clock().compare(minClock);
                    if ((ClockRelationship.LESS_THAN == subRel) || (ClockRelationship.EQUAL == subRel))
                    {
                        c.remove(subColumn.name());
                    }
                }
            }
            if (c.getSubColumns().isEmpty() && c.getLocalDeletionTime() <= gcBefore)
            {
                cf.remove(c.name());
            }
        }
    }

    /*
     * Called after the Memtable flushes its in-memory data, or we add a file
     * via bootstrap. This information is
     * cached in the ColumnFamilyStore. This is useful for reads because the
     * ColumnFamilyStore first looks in the in-memory store and the into the
     * disk to find the key. If invoked during recoveryMode the
     * onMemtableFlush() need not be invoked.
     *
     * param @ filename - filename just flushed to disk
     */
    public void addSSTable(SSTableReader sstable)
    {
        ssTables_.add(Arrays.asList(sstable));
        CompactionManager.instance.submitMinorIfNeeded(this);
    }

    /*
     * Add up all the files sizes this is the worst case file
     * size for compaction of all the list of files given.
     */
    long getExpectedCompactedFileSize(Iterable<SSTableReader> sstables)
    {
        long expectedFileSize = 0;
        for (SSTableReader sstable : sstables)
        {
            long size = sstable.length();
            expectedFileSize = expectedFileSize + size;
        }
        return expectedFileSize;
    }

    /*
     *  Find the maximum size file in the list .
     */
    SSTableReader getMaxSizeFile(Iterable<SSTableReader> sstables)
    {
        long maxSize = 0L;
        SSTableReader maxFile = null;
        for (SSTableReader sstable : sstables)
        {
            if (sstable.length() > maxSize)
            {
                maxSize = sstable.length();
                maxFile = sstable;
            }
        }
        return maxFile;
    }

    void forceCleanup()
    {
        CompactionManager.instance.submitCleanup(ColumnFamilyStore.this);
    }

    public Table getTable()
    {
        return Table.open(table_);
    }

    void markCompacted(Collection<SSTableReader> sstables)
    {
        ssTables_.markCompacted(sstables);
    }

    boolean isCompleteSSTables(Collection<SSTableReader> sstables)
    {
        return ssTables_.getSSTables().equals(new HashSet<SSTableReader>(sstables));
    }

    void replaceCompactedSSTables(Collection<SSTableReader> sstables, Iterable<SSTableReader> replacements)
    {
        ssTables_.replace(sstables, replacements);
    }

    /**
     * submits flush sort on the flushSorter executor, which will in turn submit to flushWriter when sorted.
     * TODO because our executors use CallerRunsPolicy, when flushSorter fills up, no writes will proceed
     * because the next flush will start executing on the caller, mutation-stage thread that has the
     * flush write lock held.  (writes aquire this as a read lock before proceeding.)
     * This is good, because it backpressures flushes, but bad, because we can't write until that last
     * flushing thread finishes sorting, which will almost always be longer than any of the flushSorter threads proper
     * (since, by definition, it started last).
     */
    void submitFlush(IFlushable flushable, CountDownLatch latch)
    {
        logger_.info("Enqueuing flush of {}", flushable);
        flushable.flushAndSignal(latch, flushSorter_, flushWriter_);
    }

    public int getMemtableColumnsCount()
    {
        return getMemtableThreadSafe().getCurrentOperations();
    }

    public int getMemtableDataSize()
    {
        return getMemtableThreadSafe().getCurrentThroughput();
    }

    public int getMemtableSwitchCount()
    {
        return memtableSwitchCount;
    }

    /**
     * get the current memtable in a threadsafe fashion.  note that simply "return memtable_" is
     * incorrect; you need to lock to introduce a thread safe happens-before ordering.
     *
     * do NOT use this method to do either a put or get on the memtable object, since it could be
     * flushed in the meantime (and its executor terminated).
     *
     * also do NOT make this method public or it will really get impossible to reason about these things.
     * @return
     */
    private Memtable getMemtableThreadSafe()
    {
        Table.flusherLock.readLock().lock();
        try
        {
            return memtable_;
        }
        finally
        {
            Table.flusherLock.readLock().unlock();
        }
    }

    public Collection<SSTableReader> getSSTables()
    {
        return ssTables_.getSSTables();
    }

    public long getReadCount()
    {
        return readStats_.getOpCount();
    }

    public double getRecentReadLatencyMicros()
    {
        return readStats_.getRecentLatencyMicros();
    }

    public long[] getLifetimeReadLatencyHistogramMicros()
    {
        return readStats_.getTotalLatencyHistogramMicros();
    }

    public long[] getRecentReadLatencyHistogramMicros()
    {
        return readStats_.getRecentLatencyHistogramMicros();
    }

    public long getTotalReadLatencyMicros()
    {
        return readStats_.getTotalLatencyMicros();
    }

// TODO this actually isn't a good meature of pending tasks
    public int getPendingTasks()
    {
        return Table.flusherLock.getQueueLength();
    }

    public long getWriteCount()
    {
        return writeStats_.getOpCount();
    }

    public long getTotalWriteLatencyMicros()
    {
        return writeStats_.getTotalLatencyMicros();
    }

    public double getRecentWriteLatencyMicros()
    {
        return writeStats_.getRecentLatencyMicros();
    }

    public long[] getLifetimeWriteLatencyHistogramMicros()
    {
        return writeStats_.getTotalLatencyHistogramMicros();
    }

    public long[] getRecentWriteLatencyHistogramMicros()
    {
        return writeStats_.getRecentLatencyHistogramMicros();
    }

    public ColumnFamily getColumnFamily(DecoratedKey key, QueryPath path, byte[] start, byte[] finish, List<byte[]> bitmasks, boolean reversed, int limit)
    {
        return getColumnFamily(QueryFilter.getSliceFilter(key, path, start, finish, bitmasks, reversed, limit));
    }

    public ColumnFamily getColumnFamily(DecoratedKey key, QueryPath path, byte[] start, byte[] finish, boolean reversed, int limit)
    {
        return getColumnFamily(QueryFilter.getSliceFilter(key, path, start, finish, null, reversed, limit));
    }

    /**
     * get a list of columns starting from a given column, in a specified order.
     * only the latest version of a column is returned.
     * @return null if there is no data and no tombstones; otherwise a ColumnFamily
     */
    public ColumnFamily getColumnFamily(QueryFilter filter)
    {
        return getColumnFamily(filter, (int) (System.currentTimeMillis() / 1000) - metadata.gcGraceSeconds);
    }

    private ColumnFamily cacheRow(DecoratedKey key)
    {
        ColumnFamily cached;
        if ((cached = ssTables_.getRowCache().get(key)) == null)
        {
            cached = getTopLevelColumns(QueryFilter.getIdentityFilter(key, new QueryPath(columnFamily_)), Integer.MIN_VALUE);
            if (cached == null)
                return null;
            ssTables_.getRowCache().put(key, cached);
        }
        return cached;
    }

    private ColumnFamily getColumnFamily(QueryFilter filter, int gcBefore)
    {
        assert columnFamily_.equals(filter.getColumnFamilyName());

        long start = System.nanoTime();
        try
        {
            if (ssTables_.getRowCache().getCapacity() == 0)
            {
                ColumnFamily cf = getTopLevelColumns(filter, gcBefore);
                // TODO this is necessary because when we collate supercolumns together, we don't check
                // their subcolumns for relevance, so we need to do a second prune post facto here.
                return cf.isSuper() ? removeDeleted(cf, gcBefore) : removeDeletedCF(cf, gcBefore);
            }

            ColumnFamily cached = cacheRow(filter.key);
            if (cached == null)
                return null;

            return filterColumnFamily(cached, filter, gcBefore);
        }
        finally
        {
            readStats_.addNano(System.nanoTime() - start);
        }
    }

    /** filter a cached row, which will not be modified by the filter, but may be modified by throwing out
     *  tombstones that are no longer relevant. */
    ColumnFamily filterColumnFamily(ColumnFamily cached, QueryFilter filter, int gcBefore)
    {
        // special case slicing the entire row:
        // we can skip the filter step entirely, and we can help out removeDeleted by re-caching the result
        // if any tombstones have aged out since last time.  (This means that the row cache will treat gcBefore as
        // max(gcBefore, all previous gcBefore), which is fine for correctness.)
        //
        // But, if the filter is asking for less columns than we have cached, we fall back to the slow path
        // since we have to copy out a subset.
        if (filter.filter instanceof SliceQueryFilter)
        {
            SliceQueryFilter sliceFilter = (SliceQueryFilter) filter.filter;
            if (sliceFilter.start.length == 0 && sliceFilter.finish.length == 0)
            {
                if (cached.isSuper() && filter.path.superColumnName != null)
                {
                    // subcolumns from named supercolumn
                    IColumn sc = cached.getColumn(filter.path.superColumnName);
                    if (sc == null || sliceFilter.count >= sc.getSubColumns().size())
                    {
                        ColumnFamily cf = cached.cloneMeShallow();
                        if (sc != null)
                            cf.addColumn(sc);
                        return removeDeleted(cf, gcBefore);
                    }
                }
                else
                {
                    // top-level columns
                    if (sliceFilter.count >= cached.getColumnCount())
                    {
                        removeDeletedColumnsOnly(cached, gcBefore);
                        return removeDeletedCF(cached, gcBefore);
                    }
                }
            }
        }

        IColumnIterator ci = filter.getMemtableColumnIterator(cached, null, getComparator());
        ColumnFamily cf = null;
        try
        {
            cf = ci.getColumnFamily().cloneMeShallow();
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
        filter.collectCollatedColumns(cf, ci, gcBefore);
        // TODO this is necessary because when we collate supercolumns together, we don't check
        // their subcolumns for relevance, so we need to do a second prune post facto here.
        return cf.isSuper() ? removeDeleted(cf, gcBefore) : removeDeletedCF(cf, gcBefore);
    }

    private ColumnFamily getTopLevelColumns(QueryFilter filter, int gcBefore)
    {
        // we are querying top-level columns, do a merging fetch with indexes.
        List<IColumnIterator> iterators = new ArrayList<IColumnIterator>();
        final ColumnFamily returnCF = ColumnFamily.create(metadata);
        try
        {
            IColumnIterator iter;

            /* add the current memtable */
            iter = filter.getMemtableColumnIterator(getMemtableThreadSafe(), getComparator());
            if (iter != null)
            {
                returnCF.delete(iter.getColumnFamily());
                iterators.add(iter);
            }

            /* add the memtables being flushed */
            for (Memtable memtable : memtablesPendingFlush)
            {
                iter = filter.getMemtableColumnIterator(memtable, getComparator());
                if (iter != null)
                {
                    returnCF.delete(iter.getColumnFamily());
                    iterators.add(iter);
                }
            }

            /* add the SSTables on disk */
            for (SSTableReader sstable : ssTables_)
            {
                iter = filter.getSSTableColumnIterator(sstable);
                if (iter.getColumnFamily() != null)
                {
                    returnCF.delete(iter.getColumnFamily());
                    iterators.add(iter);
                }
            }

            Comparator<IColumn> comparator = QueryFilter.getColumnComparator(getComparator());
            Iterator collated = IteratorUtils.collatedIterator(comparator, iterators);
            filter.collectCollatedColumns(returnCF, collated, gcBefore);
            // Caller is responsible for final removeDeletedCF.  This is important for cacheRow to work correctly:
            // we need to distinguish between "there is no data at all for this row" (BF will let us rebuild that efficiently)
            // and "there used to be data, but it's gone now" (we should cache the empty CF so we don't need to rebuild that slower)
            return returnCF;
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
        finally
        {
            /* close all cursors */
            for (IColumnIterator ci : iterators)
            {
                try
                {
                    ci.close();
                }
                catch (Throwable th)
                {
                    logger_.error("error closing " + ci, th);
                }
            }
        }
    }

    /**
      * Fetch a range of rows and columns from memtables/sstables.
      * 
      * @param superColumn optional SuperColumn to slice subcolumns of; null to slice top-level columns
      * @param range Either a Bounds, which includes start key, or a Range, which does not.
      * @param maxResults Maximum rows to return
      * @param columnFilter description of the columns we're interested in for each row
      * @return true if we found all keys we were looking for, otherwise false
     */
    public List<Row> getRangeSlice(byte[] superColumn, final AbstractBounds range, int maxResults, IFilter columnFilter)
    throws ExecutionException, InterruptedException
    {
        assert range instanceof Bounds
 (!((Range)range).isWrapAround() || range.right.equals(StorageService.getPartitioner().getMinimumToken()))
               : range;

        List<Row> rows = new ArrayList<Row>();
        DecoratedKey startWith = new DecoratedKey(range.left, (byte[])null);
        DecoratedKey stopAt = new DecoratedKey(range.right, (byte[])null);

        QueryFilter filter = new QueryFilter(null, new QueryPath(columnFamily_, superColumn, null), columnFilter);
        Collection<Memtable> memtables = new ArrayList<Memtable>();
        memtables.add(getMemtableThreadSafe());
        memtables.addAll(memtablesPendingFlush);

        Collection<SSTableReader> sstables = new ArrayList<SSTableReader>();
        Iterables.addAll(sstables, ssTables_);

        RowIterator iterator = RowIteratorFactory.getIterator(memtables, sstables, startWith, stopAt, filter, getComparator(), this);

        try
        {
            // pull rows out of the iterator
            boolean first = true; 
            while(iterator.hasNext())
            {
                Row current = iterator.next();
                DecoratedKey key = current.key;

                if (!stopAt.isEmpty() && stopAt.compareTo(key) < 0)
                    return rows;

                // skip first one
                if(range instanceof Bounds || !first || !key.equals(startWith))
                {
                    rows.add(current);
                    if (logger_.isDebugEnabled())
                        logger_.debug("scanned " + key);
                }
                first = false;

                if (rows.size() >= maxResults)
                    return rows;
            }
        }
        finally
        {
            try
            {
                iterator.close();
            }
            catch (IOException e)
            {
                throw new IOError(e);
            }
        }

        return rows;
    }

    public List<Row> scan(IndexClause clause, AbstractBounds range, IFilter dataFilter)
    {
        // TODO: allow merge join instead of just one index + loop
        IndexExpression first = highestSelectivityPredicate(clause);
        ColumnFamilyStore indexCFS = getIndexedColumnFamilyStore(first.column_name);
        assert indexCFS != null;
        DecoratedKey indexKey = indexCFS.partitioner_.decorateKey(first.value);

        List<Row> rows = new ArrayList<Row>();
        byte[] startKey = clause.start_key;
        
        outer:
        while (true)
        {
            /* we don't have a way to get the key back from the DK -- we just have a token --
             * so, we need to loop after starting with start_key, until we get to keys in the given `range`.
             * But, if the calling StorageProxy is doing a good job estimating data from each range, the range
             * should be pretty close to `start_key`. */
            QueryFilter indexFilter = QueryFilter.getSliceFilter(indexKey,
                                                                 new QueryPath(indexCFS.getColumnFamilyName()),
                                                                 startKey,
                                                                 ArrayUtils.EMPTY_BYTE_ARRAY,
                                                                 null,
                                                                 false,
                                                                 clause.count);
            ColumnFamily indexRow = indexCFS.getColumnFamily(indexFilter);
            if (indexRow == null)
                break;

            byte[] dataKey = null;
            int n = 0;
            for (IColumn column : indexRow.getSortedColumns())
            {
                if (column.isMarkedForDelete())
                    continue;
                dataKey = column.name();
                n++;
                DecoratedKey dk = partitioner_.decorateKey(dataKey);
                if (!range.right.equals(partitioner_.getMinimumToken()) && range.right.compareTo(dk.token) < 0)
                    break outer;
                if (!range.contains(dk.token))
                    continue;
                ColumnFamily data = getColumnFamily(new QueryFilter(dk, new QueryPath(columnFamily_), dataFilter));
                if (satisfies(data, clause, first))
                    rows.add(new Row(dk, data));
                if (rows.size() == clause.count)
                    break outer;
            }
            startKey = dataKey;
            if (n < clause.count)
                break;
        }

        return rows;
    }

    private IndexExpression highestSelectivityPredicate(IndexClause clause)
    {
        IndexExpression best = null;
        int bestMeanCount = Integer.MAX_VALUE;
        for (IndexExpression expression : clause.expressions)
        {
            ColumnFamilyStore cfs = getIndexedColumnFamilyStore(expression.column_name);
            if (cfs == null)
                continue;
            int columns = cfs.getMeanColumns();
            if (columns < bestMeanCount)
            {
                best = expression;
                bestMeanCount = columns;
            }
        }
        return best;
    }

    private static boolean satisfies(ColumnFamily data, IndexClause clause, IndexExpression first)
    {
        for (IndexExpression expression : clause.expressions)
        {
            // (we can skip "first" since we already know it's satisfied)
            if (expression == first)
                continue;
            // check column data vs expression
            IColumn column = data.getColumn(expression.column_name);
            if (column != null && !Arrays.equals(column.value(), expression.value))
                 return false;
        }
        return true;
    }

    public AbstractType getComparator()
    {
        return metadata.comparator;
    }

    /**
     * Take a snap shot of this columnfamily store.
     * 
     * @param snapshotName the name of the associated with the snapshot 
     */
    public void snapshot(String snapshotName)
    {
        try
        {
            forceBlockingFlush();
        }
        catch (ExecutionException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new AssertionError(e);
        }

        for (SSTableReader ssTable : ssTables_)
        {
            try
            {
                // mkdir
                File sourceFile = new File(ssTable.getFilename());
                File dataDirectory = sourceFile.getParentFile().getParentFile();
                String snapshotDirectoryPath = Table.getSnapshotPath(dataDirectory.getAbsolutePath(), table_, snapshotName);
                FileUtils.createDirectory(snapshotDirectoryPath);

                // hard links
                File targetLink = new File(snapshotDirectoryPath, sourceFile.getName());
                FileUtils.createHardLink(sourceFile, targetLink);

                sourceFile = new File(ssTable.indexFilename());
                targetLink = new File(snapshotDirectoryPath, sourceFile.getName());
                FileUtils.createHardLink(sourceFile, targetLink);

                sourceFile = new File(ssTable.filterFilename());
                targetLink = new File(snapshotDirectoryPath, sourceFile.getName());
                FileUtils.createHardLink(sourceFile, targetLink);
                if (logger_.isDebugEnabled())
                    logger_.debug("Snapshot for " + table_ + " table data file " + sourceFile.getAbsolutePath() +
                        " created as " + targetLink.getAbsolutePath());
            }
            catch (IOException e)
            {
                throw new IOError(e);
            }

        }
    }

    public void loadRowCache()
    {
        if (metadata.preloadRowCache)
        {
            logger_.debug(String.format("Loading cache for keyspace/columnfamily %s/%s", table_, columnFamily_));
            int ROWS = 4096;
            Token min = partitioner_.getMinimumToken();
            Token start = min;
            long i = 0;
            while (i < ssTables_.getRowCache().getCapacity())
            {
                List<Row> result;
                try
                {
                    result = getRangeSlice(null, new Bounds(start, min), ROWS, new IdentityQueryFilter());
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                for (Row row : result)
                    ssTables_.getRowCache().put(row.key, row.cf);
                i += result.size();
                if (result.size() < ROWS)
                    break;

                start = partitioner_.getToken(result.get(ROWS - 1).key.key);
            }
            logger_.info(String.format("Loaded %s rows into the %s cache", i, columnFamily_));
        }
    }


    public boolean hasUnreclaimedSpace()
    {
        return ssTables_.getLiveSize() < ssTables_.getTotalSize();
    }

    public long getTotalDiskSpaceUsed()
    {
        return ssTables_.getTotalSize();
    }

    public long getLiveDiskSpaceUsed()
    {
        return ssTables_.getLiveSize();
    }

    public int getLiveSSTableCount()
    {
        return ssTables_.size();
    }

    /** raw cached row -- does not fetch the row if it is not present.  not counted in cache statistics.  */
    public ColumnFamily getRawCachedRow(DecoratedKey key)
    {
        return ssTables_.getRowCache().getCapacity() == 0 ? null : ssTables_.getRowCache().getInternal(key);
    }

    void invalidateCachedRow(DecoratedKey key)
    {
        ssTables_.getRowCache().remove(key);
    }

    public void forceMajorCompaction()
    {
        CompactionManager.instance.submitMajor(this);
    }

    public void invalidateRowCache()
    {
        ssTables_.getRowCache().clear();
    }

    public int getKeyCacheSize()
    {
        return ssTables_.getKeyCache().getCapacity();
    }

    public static Iterable<ColumnFamilyStore> all()
    {
        Iterable<ColumnFamilyStore>[] stores = new Iterable[DatabaseDescriptor.getTables().size()];
        int i = 0;
        for (Table table : Table.all())
        {
            stores[i++] = table.getColumnFamilyStores();
        }
        return Iterables.concat(stores);
    }

    public Iterable<DecoratedKey> allKeySamples()
    {
        Collection<SSTableReader> sstables = getSSTables();
        Iterable<DecoratedKey>[] samples = new Iterable[sstables.size()];
        int i = 0;
        for (SSTableReader sstable: sstables)
        {
            samples[i++] = sstable.getKeySamples();
        }
        return Iterables.concat(samples);
    }

    /**
     * for testing.  no effort is made to clear historical memtables.
     */
    void clearUnsafe()
    {
        memtable_.clearUnsafe();
        ssTables_.clearUnsafe();
    }


    public Set<Memtable> getMemtablesPendingFlush()
    {
        return memtablesPendingFlush;
    }

    /**
     * Truncate practically deletes the entire column family's data
     * @return a Future to the delete operation. Call the future's get() to make
     * sure the column family has been deleted
     */
    public Future<?> truncate() throws IOException
    {
        // snapshot will also flush, but we want to truncate the most possible, and anything in a flush written
        // after truncateAt won't be truncated.
        try
        {
            forceBlockingFlush();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        final long truncatedAt = System.currentTimeMillis();
        snapshot(Table.getTimestampedSnapshotName("before-truncate"));

        Runnable runnable = new WrappedRunnable()
        {
            public void runMayThrow() throws InterruptedException, IOException
            {
                // putting markCompacted on the commitlogUpdater thread ensures it will run
                // after any compactions that were in progress when truncate was called, are finished
                List<SSTableReader> truncatedSSTables = new ArrayList<SSTableReader>();
                for (SSTableReader sstable : ssTables_.getSSTables())
                {
                    if (!sstable.newSince(truncatedAt))
                        truncatedSSTables.add(sstable);
                }
                markCompacted(truncatedSSTables);

                // Invalidate row cache
                invalidateRowCache();
            }
        };

        return postFlushExecutor_.submit(runnable);
    }

    public static Future<?> submitPostFlush(Runnable runnable)
    {
        return postFlushExecutor_.submit(runnable);
    }

    public long getBloomFilterFalsePositives()
    {
        long count = 0L;
        for (SSTableReader sstable: getSSTables())
        {
            count += sstable.getBloomFilterFalsePositiveCount();
        }
        return count;
    }

    public long getRecentBloomFilterFalsePositives()
    {
        long count = 0L;
        for (SSTableReader sstable: getSSTables())
        {
            count += sstable.getRecentBloomFilterFalsePositiveCount();
        }
        return count;
    }

    public double getBloomFilterFalseRatio()
    {
        long falseCount = 0L;
        long trueCount = 0L;
        for (SSTableReader sstable: getSSTables())
        {
            falseCount += sstable.getBloomFilterFalsePositiveCount();
            trueCount += sstable.getBloomFilterTruePositiveCount();
        }
        if (falseCount == 0L && trueCount == 0L)
            return 0d;
        return (double) falseCount / (trueCount + falseCount);
    }

    public double getRecentBloomFilterFalseRatio()
    {
        long falseCount = 0L;
        long trueCount = 0L;
        for (SSTableReader sstable: getSSTables())
        {
            falseCount += sstable.getRecentBloomFilterFalsePositiveCount();
            trueCount += sstable.getRecentBloomFilterTruePositiveCount();
        }
        if (falseCount == 0L && trueCount == 0L)
            return 0d;
        return (double) falseCount / (trueCount + falseCount);
    }

    public Set<byte[]> getIndexedColumns()
    {
        return indexedColumns_.keySet();
    }

    public ColumnFamilyStore getIndexedColumnFamilyStore(byte[] column)
    {
        return indexedColumns_.get(column);
    }

    public ColumnFamily newIndexedColumnFamily(byte[] column)
    {
        return ColumnFamily.create(indexedColumns_.get(column).metadata);
    }

    public DecoratedKey<LocalToken> getIndexKeyFor(byte[] name, byte[] value)
    {
        return indexedColumns_.get(name).partitioner_.decorateKey(value);
    }

    @Override
    public String toString()
    {
        return "ColumnFamilyStore(" +
               "table='" + table_ + '\'' +
               ", columnFamily='" + columnFamily_ + '\'' +
               ')';
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/268.java