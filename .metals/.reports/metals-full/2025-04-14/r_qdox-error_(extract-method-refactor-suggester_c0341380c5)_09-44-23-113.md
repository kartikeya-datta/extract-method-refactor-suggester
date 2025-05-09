error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2316.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2316.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2316.java
text:
```scala
i@@f (cf == null || cf.getColumnCount() == 0)

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
import java.io.IOException;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.dht.Range;
import org.apache.cassandra.io.*;
import org.apache.cassandra.net.EndPoint;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.*;
import org.apache.cassandra.concurrent.DebuggableThreadPoolExecutor;
import org.apache.cassandra.concurrent.ThreadFactoryImpl;
import org.apache.cassandra.db.filter.*;
import org.apache.cassandra.db.marshal.AbstractType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Predicate;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

public final class ColumnFamilyStore implements ColumnFamilyStoreMBean
{
    private static Logger logger_ = Logger.getLogger(ColumnFamilyStore.class);

    private static final int BUFSIZE = 128 * 1024 * 1024;

    private static NonBlockingHashMap<String, Set<Memtable>> memtablesPendingFlush = new NonBlockingHashMap<String, Set<Memtable>>();
    private static ExecutorService flusher_ = new DebuggableThreadPoolExecutor(DatabaseDescriptor.getFlushMinThreads(), DatabaseDescriptor.getFlushMaxThreads(), Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactoryImpl("MEMTABLE-FLUSHER-POOL"));
    
    private final String table_;
    public final String columnFamily_;
    private final boolean isSuper_;

    private volatile Integer memtableSwitchCount = 0;

    /* This is used to generate the next index for a SSTable */
    private AtomicInteger fileIndexGenerator_ = new AtomicInteger(0);

    /* active memtable associated with this ColumnFamilyStore. */
    private Memtable memtable_;
    // this lock is to (1) serialize puts and
    // (2) make sure we don't perform puts on a memtable that is queued for flush.
    // (or conversely, flush a memtable that is mid-put.)
    // gets may be safely performed on a flushing ("frozen") memtable.
    private ReentrantReadWriteLock memtableLock_ = new ReentrantReadWriteLock(true);

    // TODO binarymemtable ops are not threadsafe (do they need to be?)
    private AtomicReference<BinaryMemtable> binaryMemtable_;

    /* SSTables on disk for this column family */
    private SortedMap<String, SSTableReader> ssTables_ = new TreeMap<String, SSTableReader>(new FileNameComparator(FileNameComparator.Descending));

    /* Modification lock used for protecting reads from compactions. */
    private ReentrantReadWriteLock sstableLock_ = new ReentrantReadWriteLock(true);

    private TimedStatsDeque readStats_ = new TimedStatsDeque(60000);
    private TimedStatsDeque writeStats_ = new TimedStatsDeque(60000);

    ColumnFamilyStore(String table, String columnFamilyName, boolean isSuper, int indexValue) throws IOException
    {
        table_ = table;
        columnFamily_ = columnFamilyName;
        isSuper_ = isSuper;
        fileIndexGenerator_.set(indexValue);
        memtable_ = new Memtable(table_, columnFamily_);
        binaryMemtable_ = new AtomicReference<BinaryMemtable>(new BinaryMemtable(table_, columnFamily_));
    }

    public static ColumnFamilyStore getColumnFamilyStore(String table, String columnFamily) throws IOException
    {
        /*
         * Get all data files associated with old Memtables for this table.
         * These files are named as follows <Table>-1.db, ..., <Table>-n.db. Get
         * the max which in this case is n and increment it to use it for next
         * index.
         */
        List<Integer> indices = new ArrayList<Integer>();
        String[] dataFileDirectories = DatabaseDescriptor.getAllDataFileLocationsForTable(table);
        for (String directory : dataFileDirectories)
        {
            File fileDir = new File(directory);
            File[] files = fileDir.listFiles();
            
            for (File file : files)
            {
                String filename = file.getName();
                String cfName = getColumnFamilyFromFileName(filename);

                if (cfName.equals(columnFamily))
                {
                    int index = getIndexFromFileName(filename);
                    indices.add(index);
                }
            }
        }
        Collections.sort(indices);
        int value = (indices.size() > 0) ? (indices.get(indices.size() - 1)) : 0;

        ColumnFamilyStore cfs = new ColumnFamilyStore(table, columnFamily, "Super".equals(DatabaseDescriptor.getColumnType(table, columnFamily)), value);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        try
        {
            mbs.registerMBean(cfs, new ObjectName(
                    "org.apache.cassandra.db:type=ColumnFamilyStores,name=" + table + ",columnfamily=" + columnFamily));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return cfs;
    }

    void onStart() throws IOException
    {
        if (logger_.isDebugEnabled())
            logger_.debug("Starting CFS " + columnFamily_);
        // scan for data files corresponding to this CF
        List<File> sstableFiles = new ArrayList<File>();
        String[] dataFileDirectories = DatabaseDescriptor.getAllDataFileLocationsForTable(table_);
        for (String directory : dataFileDirectories)
        {
            File fileDir = new File(directory);
            File[] files = fileDir.listFiles();
            for (File file : files)
            {
                String filename = file.getName();
                if (((file.length() == 0) || (filename.contains("-" + SSTable.TEMPFILE_MARKER))) && (filename.contains(columnFamily_)))
                {
                    file.delete();
                    continue;
                }

                String cfName = getColumnFamilyFromFileName(filename);
                if (cfName.equals(columnFamily_)
                    && filename.contains("-Data.db"))
                {
                    sstableFiles.add(file.getAbsoluteFile());
                }
            }
        }
        Collections.sort(sstableFiles, new FileUtils.FileComparator());

        /* Load the index files and the Bloom Filters associated with them. */
        for (File file : sstableFiles)
        {
            String filename = file.getAbsolutePath();
            SSTableReader sstable;
            try
            {
                sstable = SSTableReader.open(filename);
            }
            catch (IOException ex)
            {
                logger_.error("Corrupt file " + filename + "; skipped", ex);
                continue;
            }
            ssTables_.put(filename, sstable);
        }

        // submit initial check-for-compaction request
        MinorCompactionManager.instance().submit(ColumnFamilyStore.this);

        // schedule hinted handoff
        if (table_.equals(Table.SYSTEM_TABLE) && columnFamily_.equals(HintedHandOffManager.HINTS_CF))
        {
            HintedHandOffManager.instance().submit(this);
        }

        // schedule periodic flusher if required
        int flushPeriod = DatabaseDescriptor.getFlushPeriod(table_, columnFamily_);
        if (flushPeriod > 0)
        {
            PeriodicFlushManager.instance().submitPeriodicFlusher(this, flushPeriod);
        }
    }

    /*
     * This method is called to obtain statistics about
     * the Column Family represented by this Column Family
     * Store. It will report the total number of files on
     * disk and the total space oocupied by the data files
     * associated with this Column Family.
    */
    public String cfStats(String newLineSeparator)
    {
        StringBuilder sb = new StringBuilder();
        /*
         * We want to do this so that if there are
         * no files on disk we do not want to display
         * something ugly on the admin page.
        */
        if (ssTables_.size() == 0)
        {
            return sb.toString();
        }
        sb.append(columnFamily_ + " statistics :");
        sb.append(newLineSeparator);
        sb.append("Number of files on disk : " + ssTables_.size());
        sb.append(newLineSeparator);
        double totalSpace = 0d;
        for (SSTableReader sstable: ssTables_.values())
        {
            File f = new File(sstable.getFilename());
            totalSpace += f.length();
        }
        String diskSpace = FileUtils.stringifyFileSize(totalSpace);
        sb.append("Total disk space : " + diskSpace);
        sb.append(newLineSeparator);
        sb.append("--------------------------------------");
        sb.append(newLineSeparator);
        return sb.toString();
    }

    /*
     * This is called after bootstrap to add the files
     * to the list of files maintained.
    */
    void addToList(SSTableReader file)
    {
        sstableLock_.writeLock().lock();
        try
        {
            ssTables_.put(file.getFilename(), file);
        }
        finally
        {
            sstableLock_.writeLock().unlock();
        }
    }

    /*
     * This method forces a compaction of the SSTables on disk. We wait
     * for the process to complete by waiting on a future pointer.
    */
    boolean forceCompaction(List<Range> ranges, EndPoint target, long skip, List<String> fileList)
    {
        Future<Boolean> futurePtr = null;
        if (ranges != null)
        {
            futurePtr = MinorCompactionManager.instance().submit(ColumnFamilyStore.this, ranges, target, fileList);
        }
        else
        {
            MinorCompactionManager.instance().submitMajor(ColumnFamilyStore.this, skip);
        }

        boolean result = true;
        try
        {
            /* Waiting for the compaction to complete. */
            if (futurePtr != null)
            {
                result = futurePtr.get();
            }
            if (logger_.isDebugEnabled())
              logger_.debug("Done forcing compaction ...");
        }
        catch (ExecutionException ex)
        {
            if (logger_.isDebugEnabled())
              logger_.debug(LogUtil.throwableToString(ex));
        }
        catch (InterruptedException ex2)
        {
            if (logger_.isDebugEnabled())
              logger_.debug(LogUtil.throwableToString(ex2));
        }
        return result;
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
        return filename.split("-")[0];
    }

    protected static int getIndexFromFileName(String filename)
    {
        /*
         * File name is of the form <table>-<column family>-<index>-Data.db.
         * This tokenizer will strip the .db portion.
         */
        StringTokenizer st = new StringTokenizer(filename, "-");
        /*
         * Now I want to get the index portion of the filename. We accumulate
         * the indices and then sort them to get the max index.
         */
        int count = st.countTokens();
        int i = 0;
        String index = null;
        while (st.hasMoreElements())
        {
            index = (String) st.nextElement();
            if (i == (count - 2))
            {
                break;
            }
            ++i;
        }
        return Integer.parseInt(index);
    }

    /*
     * @return a temporary file name for an sstable.
     * When the sstable object is closed, it will be renamed to a non-temporary
     * format, so incomplete sstables can be recognized and removed on startup.
     */
    String getTempSSTablePath()
    {
        String fname = getTempSSTableFileName();
        return new File(DatabaseDescriptor.getDataFileLocationForTable(table_), fname).getAbsolutePath();
    }

    String getTempSSTableFileName()
    {
        return String.format("%s-%s-%s-Data.db",
                             columnFamily_, SSTable.TEMPFILE_MARKER, fileIndexGenerator_.incrementAndGet());
    }

    void switchMemtable(Memtable oldMemtable, CommitLog.CommitLogContext ctx)
    {
        memtableLock_.writeLock().lock();
        try
        {
            if (oldMemtable.isFrozen())
            {
                return;
            }
            logger_.info(columnFamily_ + " has reached its threshold; switching in a fresh Memtable");
            oldMemtable.freeze();
            getMemtablesPendingFlushNotNull(columnFamily_).add(oldMemtable); // it's ok for the MT to briefly be both active and pendingFlush
            submitFlush(oldMemtable, ctx);
            memtable_ = new Memtable(table_, columnFamily_);
        }
        finally
        {
            memtableLock_.writeLock().unlock();
        }

        if (memtableSwitchCount == Integer.MAX_VALUE)
        {
            memtableSwitchCount = 0;
        }
        memtableSwitchCount++;
    }

    void switchBinaryMemtable(String key, byte[] buffer) throws IOException
    {
        binaryMemtable_.set(new BinaryMemtable(table_, columnFamily_));
        binaryMemtable_.get().put(key, buffer);
    }

    public void forceFlush()
    {
        if (memtable_.isClean())
            return;

        CommitLog.CommitLogContext ctx = null;
        try
        {
            ctx = CommitLog.open().getContext();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        switchMemtable(memtable_, ctx);
    }

    void forceBlockingFlush() throws IOException, ExecutionException, InterruptedException
    {
        forceFlush();
        // block for flush to finish by adding a no-op action to the flush executorservice
        // and waiting for that to finish.  (this works since flush ES is single-threaded.)
        Future f = flusher_.submit(new Runnable()
        {
            public void run()
            {
            }
        });
        f.get();
    }

    public void forceFlushBinary()
    {
        submitFlush(binaryMemtable_.get());
    }

    /**
     * Insert/Update the column family for this key.
     * param @ lock - lock that needs to be used.
     * param @ key - key for update/insert
     * param @ columnFamily - columnFamily changes
     */
    void apply(String key, ColumnFamily columnFamily, CommitLog.CommitLogContext cLogCtx)
            throws IOException
    {
        long start = System.currentTimeMillis();
        Memtable initialMemtable = getMemtableThreadSafe();
        if (initialMemtable.isThresholdViolated())
        {
            switchMemtable(initialMemtable, cLogCtx);
        }
        memtableLock_.writeLock().lock();
        try
        {
            memtable_.put(key, columnFamily);
        }
        finally
        {
            memtableLock_.writeLock().unlock();
        }
        writeStats_.add(System.currentTimeMillis() - start);
    }

    /*
     * Insert/Update the column family for this key. param @ lock - lock that
     * needs to be used. param @ key - key for update/insert param @
     * columnFamily - columnFamily changes
     */
    void applyBinary(String key, byte[] buffer)
            throws IOException
    {
        long start = System.currentTimeMillis();
        binaryMemtable_.get().put(key, buffer);
        writeStats_.add(System.currentTimeMillis() - start);
    }

    /**
     * like resolve, but leaves the resolved CF as the only item in the list
     */
    private static void merge(List<ColumnFamily> columnFamilies)
    {
        ColumnFamily cf = ColumnFamily.resolve(columnFamilies);
        columnFamilies.clear();
        columnFamilies.add(cf);
    }

    private static ColumnFamily resolveAndRemoveDeleted(List<ColumnFamily> columnFamilies)
    {
        ColumnFamily cf = ColumnFamily.resolve(columnFamilies);
        return removeDeleted(cf);
    }

    /*
     This is complicated because we need to preserve deleted columns, supercolumns, and columnfamilies
     until they have been deleted for at least GC_GRACE_IN_SECONDS.  But, we do not need to preserve
     their contents; just the object itself as a "tombstone" that can be used to repair other
     replicas that do not know about the deletion.
     */
    static ColumnFamily removeDeleted(ColumnFamily cf)
    {
        return removeDeleted(cf, getDefaultGCBefore());
    }

    public static int getDefaultGCBefore()
    {
        return (int)(System.currentTimeMillis() / 1000) - DatabaseDescriptor.getGcGraceInSeconds();
    }

    static ColumnFamily removeDeleted(ColumnFamily cf, int gcBefore)
    {
        if (cf == null)
        {
            return null;
        }

        // in case of a timestamp tie, tombstones get priority over non-tombstones.
        // we want this to be deterministic in general to avoid confusion;
        // either way (tombstone or non- getting priority) would be fine,
        // but we picked this way because it makes removing delivered hints
        // easier for HintedHandoffManager.
        for (byte[] cname : cf.getColumnNames())
        {
            IColumn c = cf.getColumnsMap().get(cname);
            if (c instanceof SuperColumn)
            {
                long minTimestamp = Math.max(c.getMarkedForDeleteAt(), cf.getMarkedForDeleteAt());
                // don't operate directly on the supercolumn, it could be the one in the memtable.
                // instead, create a new SC and add in the subcolumns that qualify.
                cf.remove(cname);
                SuperColumn sc = ((SuperColumn)c).cloneMeShallow();
                for (IColumn subColumn : c.getSubColumns())
                {
                    if (subColumn.timestamp() > minTimestamp)
                    {
                        if (!subColumn.isMarkedForDelete() || subColumn.getLocalDeletionTime() > gcBefore)
                        {
                            sc.addColumn(subColumn);
                        }
                    }
                }
                if (sc.getSubColumns().size() > 0 || sc.getLocalDeletionTime() > gcBefore)
                {
                    cf.addColumn(sc);
                }
            }
            else if ((c.isMarkedForDelete() && c.getLocalDeletionTime() <= gcBefore)
 c.timestamp() <= cf.getMarkedForDeleteAt())
            {
                cf.remove(cname);
            }
        }

        if (cf.getColumnCount() == 0 && cf.getLocalDeletionTime() <= gcBefore)
        {
            return null;
        }
        return cf;
    }

    /*
     * This version is used only on start up when we are recovering from logs.
     * Hence no locking is required since we process logs on the main thread. In
     * the future we may want to parellelize the log processing for a table by
     * having a thread per log file present for recovery. Re-visit at that time.
     */
    void applyNow(String key, ColumnFamily columnFamily) throws IOException
    {
        getMemtableThreadSafe().put(key, columnFamily);
    }

    /*
     * This method is called when the Memtable is frozen and ready to be flushed
     * to disk. This method informs the CommitLog that a particular ColumnFamily
     * is being flushed to disk.
     */
    void onMemtableFlush(CommitLog.CommitLogContext cLogCtx) throws IOException
    {
        if (cLogCtx.isValidContext())
        {
            CommitLog.open().onMemtableFlush(table_, columnFamily_, cLogCtx);
        }
    }

    /*
     * Called after the Memtable flushes its in-memory data. This information is
     * cached in the ColumnFamilyStore. This is useful for reads because the
     * ColumnFamilyStore first looks in the in-memory store and the into the
     * disk to find the key. If invoked during recoveryMode the
     * onMemtableFlush() need not be invoked.
     *
     * param @ filename - filename just flushed to disk
     * param @ bf - bloom filter which indicates the keys that are in this file.
    */
    void storeLocation(SSTableReader sstable)
    {
        int ssTableCount;
        sstableLock_.writeLock().lock();
        try
        {
            ssTables_.put(sstable.getFilename(), sstable);
            ssTableCount = ssTables_.size();
        }
        finally
        {
            sstableLock_.writeLock().unlock();
        }

        /* it's ok if compaction gets submitted multiple times while one is already in process.
           worst that happens is, compactor will count the sstable files and decide there are
           not enough to bother with. */
        if (ssTableCount >= MinorCompactionManager.MINCOMPACTION_THRESHOLD)
        {
            if (logger_.isDebugEnabled())
              logger_.debug("Submitting " + columnFamily_ + " for compaction");
            MinorCompactionManager.instance().submit(this);
        }
    }

    private PriorityQueue<FileStruct> initializePriorityQueue(List<String> files, List<Range> ranges) throws IOException
    {
        PriorityQueue<FileStruct> pq = new PriorityQueue<FileStruct>();
        if (files.size() > 1 || (ranges != null && files.size() > 0))
        {
            FileStruct fs = null;
            for (String file : files)
            {
                fs = SSTableReader.get(file).getFileStruct();
                fs.advance(true);
                if (fs.isExhausted())
                {
                    continue;
                }
                pq.add(fs);
            }
        }
        return pq;
    }

    /*
     * Group files of similar size into buckets.
     */
    static Set<List<String>> getCompactionBuckets(Iterable<String> files, long min)
    {
        Map<List<String>, Long> buckets = new ConcurrentHashMap<List<String>, Long>();
        for (String fname : files)
        {
            File f = new File(fname);
            long size = f.length();

            boolean bFound = false;
            // look for a bucket containing similar-sized files:
            // group in the same bucket if it's w/in 50% of the average for this bucket,
            // or this file and the bucket are all considered "small" (less than `min`)
            for (List<String> bucket : buckets.keySet())
            {
                long averageSize = buckets.get(bucket);
                if ((size > averageSize / 2 && size < 3 * averageSize / 2)
 (size < min && averageSize < min))
                {
                    // remove and re-add because adding changes the hash
                    buckets.remove(bucket);
                    averageSize = (averageSize + size) / 2;
                    bucket.add(fname);
                    buckets.put(bucket, averageSize);
                    bFound = true;
                    break;
                }
            }
            // no similar bucket found; put it in a new one
            if (!bFound)
            {
                ArrayList<String> bucket = new ArrayList<String>();
                bucket.add(fname);
                buckets.put(bucket, size);
            }
        }

        return buckets.keySet();
    }

    /*
     * Break the files into buckets and then compact.
     */
    int doCompaction(int minThreshold, int maxThreshold) throws IOException
    {
        int filesCompacted = 0;
        for (List<String> files : getCompactionBuckets(ssTables_.keySet(), 50L * 1024L * 1024L))
        {
            if (files.size() < minThreshold)
            {
                continue;
            }
            Collections.sort(files, new FileNameComparator(FileNameComparator.Ascending));
            filesCompacted += doFileCompaction(files.subList(0, Math.min(files.size(), maxThreshold)), BUFSIZE);
        }
        return filesCompacted;
    }

    void doMajorCompaction(long skip) throws IOException
    {
        doMajorCompactionInternal(skip);
    }

    /*
     * Compact all the files irrespective of the size.
     * skip : is the amount in GB of the files to be skipped
     * all files greater than skip GB are skipped for this compaction.
     * Except if skip is 0 , in that case this is ignored and all files are taken.
     */
    void doMajorCompactionInternal(long skip) throws IOException
    {
        List<String> filesInternal = new ArrayList<String>(ssTables_.keySet());
        List<String> files;
        if (skip > 0L)
        {
            files = new ArrayList<String>();
            for (String file : filesInternal)
            {
                File f = new File(file);
                if (f.length() < skip * 1024L * 1024L * 1024L)
                {
                    files.add(file);
                }
            }
        }
        else
        {
            files = filesInternal;
        }
        doFileCompaction(files, BUFSIZE);
    }

    /*
     * Add up all the files sizes this is the worst case file
     * size for compaction of all the list of files given.
     */
    long getExpectedCompactedFileSize(List<String> files)
    {
        long expectedFileSize = 0;
        for (String file : files)
        {
            File f = new File(file);
            long size = f.length();
            expectedFileSize = expectedFileSize + size;
        }
        return expectedFileSize;
    }

    /*
     *  Find the maximum size file in the list .
     */
    String getMaxSizeFile(List<String> files)
    {
        long maxSize = 0L;
        String maxFile = null;
        for (String file : files)
        {
            File f = new File(file);
            if (f.length() > maxSize)
            {
                maxSize = f.length();
                maxFile = file;
            }
        }
        return maxFile;
    }

    boolean doAntiCompaction(List<Range> ranges, EndPoint target, List<String> fileList) throws IOException
    {
        List<String> files = new ArrayList<String>(ssTables_.keySet());
        return doFileAntiCompaction(files, ranges, target, fileList);
    }

    void forceCleanup()
    {
        MinorCompactionManager.instance().submitCleanup(ColumnFamilyStore.this);
    }

    /**
     * This function goes over each file and removes the keys that the node is not responsible for
     * and only keeps keys that this node is responsible for.
     *
     * @throws IOException
     */
    void doCleanupCompaction() throws IOException
    {
        List<String> files = new ArrayList<String>(ssTables_.keySet());
        for (String file : files)
        {
            doCleanup(file);
        }
    }

    /**
     * cleans up one particular file by removing keys that this node is not responsible for.
     *
     * @param file
     * @throws IOException
     */
    /* TODO: Take care of the comments later. */
    void doCleanup(String file) throws IOException
    {
        if (file == null)
        {
            return;
        }
        List<Range> myRanges;
        List<String> files = new ArrayList<String>();
        files.add(file);
        List<String> newFiles = new ArrayList<String>();
        Map<EndPoint, List<Range>> endPointtoRangeMap = StorageService.instance().constructEndPointToRangesMap();
        myRanges = endPointtoRangeMap.get(StorageService.getLocalStorageEndPoint());
        doFileAntiCompaction(files, myRanges, null, newFiles);
        if (logger_.isDebugEnabled())
          logger_.debug("Original file : " + file + " of size " + new File(file).length());
        sstableLock_.writeLock().lock();
        try
        {
            ssTables_.remove(file);
            for (String newfile : newFiles)
            {
                if (logger_.isDebugEnabled())
                  logger_.debug("New file : " + newfile + " of size " + new File(newfile).length());
                assert newfile != null;
                ssTables_.put(newfile, SSTableReader.open(newfile));
            }
            SSTableReader.get(file).delete();
        }
        finally
        {
            sstableLock_.writeLock().unlock();
        }
    }

    /**
     * This function is used to do the anti compaction process , it spits out the file which has keys that belong to a given range
     * If the target is not specified it spits out the file as a compacted file with the unecessary ranges wiped out.
     *
     * @param files
     * @param ranges
     * @param target
     * @param fileList
     * @return
     * @throws IOException
     */
    boolean doFileAntiCompaction(List<String> files, List<Range> ranges, EndPoint target, List<String> fileList) throws IOException
    {
        boolean result = false;
        long startTime = System.currentTimeMillis();
        long totalBytesRead = 0;
        long totalBytesWritten = 0;
        long totalkeysRead = 0;
        long totalkeysWritten = 0;
        String rangeFileLocation;
        String mergedFileName;
        // Calculate the expected compacted filesize
        long expectedRangeFileSize = getExpectedCompactedFileSize(files);
        /* in the worst case a node will be giving out half of its data so we take a chance */
        expectedRangeFileSize = expectedRangeFileSize / 2;
        rangeFileLocation = DatabaseDescriptor.getDataFileLocationForTable(table_, expectedRangeFileSize);
        // If the compaction file path is null that means we have no space left for this compaction.
        if (rangeFileLocation == null)
        {
            logger_.error("Total bytes to be written for range compaction  ..."
                          + expectedRangeFileSize + "   is greater than the safe limit of the disk space available.");
            return result;
        }
        PriorityQueue<FileStruct> pq = initializePriorityQueue(files, ranges);
        if (pq.isEmpty())
        {
            return result;
        }

        mergedFileName = getTempSSTableFileName();
        SSTableWriter rangeWriter = null;
        String lastkey = null;
        List<FileStruct> lfs = new ArrayList<FileStruct>();
        DataOutputBuffer bufOut = new DataOutputBuffer();
        int expectedBloomFilterSize = SSTableReader.getApproximateKeyCount(files);
        expectedBloomFilterSize = (expectedBloomFilterSize > 0) ? expectedBloomFilterSize : SSTableReader.indexInterval();
        if (logger_.isDebugEnabled())
          logger_.debug("Expected bloom filter size : " + expectedBloomFilterSize);
        List<ColumnFamily> columnFamilies = new ArrayList<ColumnFamily>();

        while (pq.size() > 0 || lfs.size() > 0)
        {
            FileStruct fs = null;
            if (pq.size() > 0)
            {
                fs = pq.poll();
            }
            if (fs != null
                && (lastkey == null || lastkey.equals(fs.getKey())))
            {
                // The keys are the same so we need to add this to the
                // ldfs list
                lastkey = fs.getKey();
                lfs.add(fs);
            }
            else
            {
                Collections.sort(lfs, new FileStructComparator());
                ColumnFamily columnFamily;
                bufOut.reset();
                if (lfs.size() > 1)
                {
                    for (FileStruct filestruct : lfs)
                    {
                        // We want to add only 2 and resolve them right there in order to save on memory footprint
                        if (columnFamilies.size() > 1)
                        {
                            // Now merge the 2 column families
                            merge(columnFamilies);
                        }
                        // deserialize into column families
                        columnFamilies.add(filestruct.getColumnFamily());
                    }
                    // Now after merging all crap append to the sstable
                    columnFamily = resolveAndRemoveDeleted(columnFamilies);
                    columnFamilies.clear();
                    if (columnFamily != null)
                    {
                        ColumnFamily.serializer().serializeWithIndexes(columnFamily, bufOut);
                    }
                }
                else
                {
                    // TODO deserializing only to reserialize is dumb
                    FileStruct filestruct = lfs.get(0);
                    ColumnFamily.serializer().serializeWithIndexes(filestruct.getColumnFamily(), bufOut);
                }
                if (Range.isTokenInRanges(StorageService.getPartitioner().getToken(lastkey), ranges))
                {
                    if (rangeWriter == null)
                    {
                        if (target != null)
                        {
                            rangeFileLocation = rangeFileLocation + File.separator + "bootstrap";
                        }
                        FileUtils.createDirectory(rangeFileLocation);
                        String fname = new File(rangeFileLocation, mergedFileName).getAbsolutePath();
                        rangeWriter = new SSTableWriter(fname, expectedBloomFilterSize, StorageService.getPartitioner());
                    }
                    rangeWriter.append(lastkey, bufOut);
                }
                totalkeysWritten++;
                for (FileStruct filestruct : lfs)
                {
                    filestruct.advance(true);
                    if (filestruct.isExhausted())
                    {
                        continue;
                    }
                    /* keep on looping until we find a key in the range */
                    while (!Range.isTokenInRanges(StorageService.getPartitioner().getToken(filestruct.getKey()), ranges))
                    {
                        filestruct.advance(true);
                        if (filestruct.isExhausted())
                        {
                            break;
                        }
                    }
                    if (!filestruct.isExhausted())
                    {
                        pq.add(filestruct);
                    }
                    totalkeysRead++;
                }
                lfs.clear();
                lastkey = null;
                if (fs != null)
                {
                    // Add back the fs since we processed the rest of
                    // filestructs
                    pq.add(fs);
                }
            }
        }

        if (rangeWriter != null)
        {
            rangeWriter.closeAndOpenReader();
            if (fileList != null)
            {
                //Retain order. The -Data.db file needs to be last because 
                //the receiving end checks for this file before opening the SSTable
                //and adding this to the list of SSTables.
                fileList.add(rangeWriter.indexFilename());
                fileList.add(rangeWriter.filterFilename());
                fileList.add(rangeWriter.getFilename());
            }
            result = true;
        }

        if (logger_.isDebugEnabled())
        {
            logger_.debug("Total time taken for range split   ..." + (System.currentTimeMillis() - startTime));
            logger_.debug("Total bytes Read for range split  ..." + totalBytesRead);
            logger_.debug("Total bytes written for range split  ..."
                          + totalBytesWritten + "   Total keys read ..." + totalkeysRead);
        }
        return result;
    }

    /*
    * This function does the actual compaction for files.
    * It maintains a priority queue of with the first key from each file
    * and then removes the top of the queue and adds it to the SStable and
    * repeats this process while reading the next from each file until its
    * done with all the files . The SStable to which the keys are written
    * represents the new compacted file. Before writing if there are keys
    * that occur in multiple files and are the same then a resolution is done
    * to get the latest data.
    *
    */
    private int doFileCompaction(List<String> files, int minBufferSize) throws IOException
    {
        if (DatabaseDescriptor.isSnapshotBeforeCompaction())
            Table.open(table_).snapshot("compact-" + columnFamily_);
        logger_.info("Compacting [" + StringUtils.join(files, ",") + "]");
        String compactionFileLocation = DatabaseDescriptor.getDataFileLocationForTable(table_, getExpectedCompactedFileSize(files));
        // If the compaction file path is null that means we have no space left for this compaction.
        // try again w/o the largest one.
        if (compactionFileLocation == null)
        {
            String maxFile = getMaxSizeFile(files);
            List<String> smallerSSTables = new ArrayList<String>(files);
            smallerSSTables.remove(maxFile);
            return doFileCompaction(smallerSSTables, minBufferSize);
        }

        String newfile = null;
        long startTime = System.currentTimeMillis();
        long totalBytesRead = 0;
        long totalBytesWritten = 0;
        long totalkeysRead = 0;
        long totalkeysWritten = 0;
        PriorityQueue<FileStruct> pq = initializePriorityQueue(files, null);

        if (pq.isEmpty())
        {
            logger_.warn("Nothing to compact (all files empty or corrupt)");
            // TODO clean out bad files, if any
            return 0;
        }

        String mergedFileName = getTempSSTableFileName();
        SSTableWriter writer = null;
        SSTableReader ssTable = null;
        String lastkey = null;
        List<FileStruct> lfs = new ArrayList<FileStruct>();
        DataOutputBuffer bufOut = new DataOutputBuffer();
        int expectedBloomFilterSize = SSTableReader.getApproximateKeyCount(files);
        expectedBloomFilterSize = (expectedBloomFilterSize > 0) ? expectedBloomFilterSize : SSTableReader.indexInterval();
        if (logger_.isDebugEnabled())
          logger_.debug("Expected bloom filter size : " + expectedBloomFilterSize);
        List<ColumnFamily> columnFamilies = new ArrayList<ColumnFamily>();

        while (pq.size() > 0 || lfs.size() > 0)
        {
            FileStruct fs = null;
            if (pq.size() > 0)
            {
                fs = pq.poll();
            }
            if (fs != null
                && (lastkey == null || lastkey.equals(fs.getKey())))
            {
                // The keys are the same so we need to add this to the
                // ldfs list
                lastkey = fs.getKey();
                lfs.add(fs);
            }
            else
            {
                Collections.sort(lfs, new FileStructComparator());
                ColumnFamily columnFamily;
                bufOut.reset();
                if (lfs.size() > 1)
                {
                    for (FileStruct filestruct : lfs)
                    {
                        // We want to add only 2 and resolve them right there in order to save on memory footprint
                        if (columnFamilies.size() > 1)
                        {
                            merge(columnFamilies);
                        }
                        // deserialize into column families
                        columnFamilies.add(filestruct.getColumnFamily());
                    }
                    // Now after merging all crap append to the sstable
                    columnFamily = resolveAndRemoveDeleted(columnFamilies);
                    columnFamilies.clear();
                    if (columnFamily != null)
                    {
                        ColumnFamily.serializer().serializeWithIndexes(columnFamily, bufOut);
                    }
                }
                else
                {
                    // TODO deserializing only to reserialize is dumb
                    FileStruct filestruct = lfs.get(0);
                    ColumnFamily.serializer().serializeWithIndexes(filestruct.getColumnFamily(), bufOut);
                }

                if (writer == null)
                {
                    String fname = new File(compactionFileLocation, mergedFileName).getAbsolutePath();
                    writer = new SSTableWriter(fname, expectedBloomFilterSize, StorageService.getPartitioner());
                }
                writer.append(lastkey, bufOut);
                totalkeysWritten++;

                for (FileStruct filestruct : lfs)
                {
                    filestruct.advance(true);
                    if (filestruct.isExhausted())
                    {
                        continue;
                    }
                    pq.add(filestruct);
                    totalkeysRead++;
                }
                lfs.clear();
                lastkey = null;
                if (fs != null)
                {
                    /* Add back the fs since we processed the rest of filestructs */
                    pq.add(fs);
                }
            }
        }
        if (writer != null)
        {
            // TODO if all the keys were the same nothing will be done here
            ssTable = writer.closeAndOpenReader();
            newfile = writer.getFilename();
        }
        sstableLock_.writeLock().lock();
        try
        {
            for (String file : files)
            {
                ssTables_.remove(file);
            }
            if (newfile != null)
            {
                ssTables_.put(newfile, ssTable);
                totalBytesWritten += (new File(newfile)).length();
            }
            for (String file : files)
            {
                SSTableReader.get(file).delete();
            }
        }
        finally
        {
            sstableLock_.writeLock().unlock();
        }

        String format = "Compacted to %s.  %d/%d bytes for %d/%d keys read/written.  Time: %dms.";
        long dTime = System.currentTimeMillis() - startTime;
        logger_.info(String.format(format, newfile, totalBytesRead, totalBytesWritten, totalkeysRead, totalkeysWritten, dTime));
        return files.size();
    }

    public static List<Memtable> getUnflushedMemtables(String cfName)
    {
        return new ArrayList<Memtable>(getMemtablesPendingFlushNotNull(cfName));
    }

    private static Set<Memtable> getMemtablesPendingFlushNotNull(String columnFamilyName)
    {
        Set<Memtable> memtables = memtablesPendingFlush.get(columnFamilyName);
        if (memtables == null)
        {
            memtablesPendingFlush.putIfAbsent(columnFamilyName, new ConcurrentSkipListSet<Memtable>());
            memtables = memtablesPendingFlush.get(columnFamilyName); // might not be the object we just put, if there was a race!
        }
        return memtables;
    }

    /* Submit memtables to be flushed to disk */
    private static void submitFlush(final Memtable memtable, final CommitLog.CommitLogContext cLogCtx)
    {
        logger_.info("Enqueuing flush of " + memtable);
        flusher_.submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    memtable.flush(cLogCtx);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
                getMemtablesPendingFlushNotNull(memtable.getColumnFamily()).remove(memtable);
            }
        });
    }

    static void submitFlush(final BinaryMemtable binaryMemtable)
    {
        logger_.info("Enqueuing flush of " + binaryMemtable);
        flusher_.submit(new Runnable()
        {
            public void run()
            {
                try
                {
                    binaryMemtable.flush();
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public boolean isSuper()
    {
        return isSuper_;
    }

    public void flushMemtableOnRecovery() throws IOException
    {
        getMemtableThreadSafe().flushOnRecovery();
    }

    public int getMemtableColumnsCount()
    {
        return getMemtableThreadSafe().getCurrentObjectCount();
    }

    public int getMemtableDataSize()
    {
        return getMemtableThreadSafe().getCurrentSize();
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
        memtableLock_.readLock().lock();
        try
        {
            return memtable_;
        }
        finally
        {
            memtableLock_.readLock().unlock();
        }
    }

    public Iterator<String> memtableKeyIterator() throws ExecutionException, InterruptedException
    {
        Set<String> keys;
        memtableLock_.readLock().lock();
        try
        {
            keys = memtable_.getKeys();
        }
        finally
        {
            memtableLock_.readLock().unlock();
        }
        return Memtable.getKeyIterator(keys);
    }

    /** not threadsafe.  caller must have lock_ acquired. */
    public Collection<SSTableReader> getSSTables()
    {
        return Collections.unmodifiableCollection(ssTables_.values());
    }

    public ReentrantReadWriteLock.ReadLock getReadLock()
    {
        return sstableLock_.readLock();
    }

    public int getReadCount()
    {
        return readStats_.size();
    }

    public double getReadLatency()
    {
        return readStats_.mean();
    }
    
    public int getPendingTasks()
    {
        return memtableLock_.getQueueLength();
    }

    /**
     * @return the number of write operations on this column family in the last minute
     */
    public int getWriteCount() {
        return writeStats_.size();
    }

    /**
     * @return average latency per write operation in the last minute
     */
    public double getWriteLatency() {
        return writeStats_.mean();
    }

    public ColumnFamily getColumnFamily(String key, QueryPath path, byte[] start, byte[] finish, boolean reversed, int limit) throws IOException
    {
        return getColumnFamily(new SliceQueryFilter(key, path, start, finish, reversed, limit));
    }

    public ColumnFamily getColumnFamily(QueryFilter filter) throws IOException
    {
        return getColumnFamily(filter, getDefaultGCBefore());
    }

    /**
     * get a list of columns starting from a given column, in a specified order.
     * only the latest version of a column is returned.
     * @return null if there is no data and no tombstones; otherwise a ColumnFamily
     */
    public ColumnFamily getColumnFamily(QueryFilter filter, int gcBefore) throws IOException
    {
        assert columnFamily_.equals(filter.getColumnFamilyName());

        long start = System.currentTimeMillis();

        // if we are querying subcolumns of a supercolumn, fetch the supercolumn with NQF, then filter in-memory.
        if (filter.path.superColumnName != null)
        {
            QueryFilter nameFilter = new NamesQueryFilter(filter.key, new QueryPath(columnFamily_), filter.path.superColumnName);
            ColumnFamily cf = getColumnFamily(nameFilter);
            if (cf == null)
                return cf;

            assert cf.getSortedColumns().size() == 1;
            SuperColumn sc = (SuperColumn)cf.getSortedColumns().iterator().next();
            SuperColumn scFiltered = filter.filterSuperColumn(sc, gcBefore);
            ColumnFamily cfFiltered = cf.cloneMeShallow();
            cfFiltered.addColumn(scFiltered);
            readStats_.add(System.currentTimeMillis() - start);
            return cfFiltered;
        }

        // we are querying top-level columns, do a merging fetch with indexes.
        sstableLock_.readLock().lock();
        List<ColumnIterator> iterators = new ArrayList<ColumnIterator>();
        try
        {
            final ColumnFamily returnCF;
            ColumnIterator iter;
        
            /* add the current memtable */
            memtableLock_.readLock().lock();
            try
            {
                iter = filter.getMemColumnIterator(memtable_, getComparator());
                returnCF = iter.getColumnFamily();
            }
            finally
            {
                memtableLock_.readLock().unlock();            
            }        
            iterators.add(iter);

            /* add the memtables being flushed */
            List<Memtable> memtables = getUnflushedMemtables(filter.getColumnFamilyName());
            for (Memtable memtable:memtables)
            {
                iter = filter.getMemColumnIterator(memtable, getComparator());
                returnCF.delete(iter.getColumnFamily());
                iterators.add(iter);
            }

            /* add the SSTables on disk */
            List<SSTableReader> sstables = new ArrayList<SSTableReader>(ssTables_.values());
            for (SSTableReader sstable : sstables)
            {
                iter = filter.getSSTableColumnIterator(sstable);
                if (iter.hasNext()) // initializes iter.CF
                {
                    returnCF.delete(iter.getColumnFamily());
                }
                iterators.add(iter);
            }

            Comparator<IColumn> comparator = filter.getColumnComparator(getComparator());
            Iterator collated = IteratorUtils.collatedIterator(comparator, iterators);
            if (!collated.hasNext())
                return null;

            filter.collectCollatedColumns(returnCF, collated, gcBefore);

            return removeDeleted(returnCF, gcBefore); // collect does a first pass but doesn't try to recognize e.g. the entire CF being tombstoned
        }
        finally
        {
            /* close all cursors */
            for (ColumnIterator ci : iterators)
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

            readStats_.add(System.currentTimeMillis() - start);
            sstableLock_.readLock().unlock();
        }
    }

    /**
     * @param startWith key to start with, inclusive.  empty string = start at beginning.
     * @param stopAt key to stop at, inclusive.  empty string = stop only when keys are exhausted.
     * @param maxResults
     * @return list of keys between startWith and stopAt
     */
    public RangeReply getKeyRange(final String startWith, final String stopAt, int maxResults)
    throws IOException, ExecutionException, InterruptedException
    {
        getReadLock().lock();
        try
        {
            return getKeyRangeUnsafe(startWith, stopAt, maxResults);
        }
        finally
        {
            getReadLock().unlock();
        }
    }

    private RangeReply getKeyRangeUnsafe(final String startWith, final String stopAt, int maxResults) throws IOException, ExecutionException, InterruptedException
    {
        // (OPP key decoration is a no-op so using the "decorated" comparator against raw keys is fine)
        final Comparator<String> comparator = StorageService.getPartitioner().getDecoratedKeyComparator();

        // create a CollatedIterator that will return unique keys from different sources
        // (current memtable, historical memtables, and SSTables) in the correct order.
        List<Iterator<String>> iterators = new ArrayList<Iterator<String>>();

        // we iterate through memtables with a priority queue to avoid more sorting than necessary.
        // this predicate throws out the keys before the start of our range.
        Predicate p = new Predicate()
        {
            public boolean evaluate(Object key)
            {
                String st = (String)key;
                return comparator.compare(startWith, st) <= 0 && (stopAt.isEmpty() || comparator.compare(st, stopAt) <= 0);
            }
        };

        // current memtable keys.  have to go through the CFS api for locking.
        iterators.add(IteratorUtils.filteredIterator(memtableKeyIterator(), p));
        // historical memtables
        for (Memtable memtable : ColumnFamilyStore.getUnflushedMemtables(columnFamily_))
        {
            iterators.add(IteratorUtils.filteredIterator(Memtable.getKeyIterator(memtable.getKeys()), p));
        }

        // sstables
        for (SSTableReader sstable : getSSTables())
        {
            FileStruct fs = sstable.getFileStruct();
            fs.seekTo(startWith);
            iterators.add(fs);
        }

        Iterator<String> collated = IteratorUtils.collatedIterator(comparator, iterators);
        Iterable<String> reduced = new ReducingIterator<String>(collated) {
            String current;

            public void reduce(String current)
            {
                 this.current = current;
            }

            protected String getReduced()
            {
                return current;
            }
        };

        try
        {
            // pull keys out of the CollatedIterator.  checking tombstone status is expensive,
            // so we set an arbitrary limit on how many we'll do at once.
            List<String> keys = new ArrayList<String>();
            boolean rangeCompletedLocally = false;
            for (String current : reduced)
            {
                if (!stopAt.isEmpty() && comparator.compare(stopAt, current) < 0)
                {
                    rangeCompletedLocally = true;
                    break;
                }
                // make sure there is actually non-tombstone content associated w/ this key
                // TODO record the key source(s) somehow and only check that source (e.g., memtable or sstable)
                QueryFilter filter = new SliceQueryFilter(current, new QueryPath(columnFamily_), ArrayUtils.EMPTY_BYTE_ARRAY, ArrayUtils.EMPTY_BYTE_ARRAY, false, 1);
                if (getColumnFamily(filter, Integer.MAX_VALUE) != null)
                {
                    keys.add(current);
                }
                if (keys.size() >= maxResults)
                {
                    rangeCompletedLocally = true;
                    break;
                }
            }
            return new RangeReply(keys, rangeCompletedLocally);
        }
        finally
        {
            for (Iterator iter : iterators)
            {
                if (iter instanceof FileStruct)
                {
                    ((FileStruct)iter).close();
                }
            }
        }
    }

    public AbstractType getComparator()
    {
        return DatabaseDescriptor.getComparator(table_, columnFamily_);
    }

    /**
     * Take a snap shot of this columnfamily store.
     * 
     * @param snapshotName the name of the associated with the snapshot 
     */
    public void snapshot(String snapshotName) throws IOException
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

        sstableLock_.readLock().lock();
        try
        {
            for (SSTableReader ssTable : new ArrayList<SSTableReader>(ssTables_.values()))
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
        }
        finally
        {
            sstableLock_.readLock().unlock();
        }
    }

    /**
     * for testing.  no effort is made to clear historical memtables.
     */
    void clearUnsafe()
    {
        sstableLock_.writeLock().lock();
        try
        {
            memtable_.clearUnsafe();
        }
        finally
        {
            sstableLock_.writeLock().unlock();
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2316.java