error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3438.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3438.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3438.java
text:
```scala
l@@ogFile_ = DatabaseDescriptor.getLogFileLocation() + File.separator +

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

import java.io.*;
import java.util.*;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.io.DataInputBuffer;
import org.apache.cassandra.io.DataOutputBuffer;
import org.apache.cassandra.io.IFileReader;
import org.apache.cassandra.io.IFileWriter;
import org.apache.cassandra.io.SequenceFile;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.FileUtils;

import org.apache.log4j.Logger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Commit Log tracks every write operation into the system. The aim
 * of the commit log is to be able to successfully recover data that was
 * not stored to disk via the Memtable. Every Commit Log maintains a
 * header represented by the abstraction CommitLogHeader. The header
 * contains a bit array and an array of longs and both the arrays are
 * of size, #column families for the Table, the Commit Log represents.
 *
 * Whenever a ColumnFamily is written to, for the first time its bit flag
 * is set to one in the CommitLogHeader. When it is flushed to disk by the
 * Memtable its corresponding bit in the header is set to zero. This helps
 * track which CommitLogs can be thrown away as a result of Memtable flushes.
 * Additionally, when a ColumnFamily is flushed and written to disk, its
 * entry in the array of longs is updated with the offset in the Commit Log
 * file where it was written. This helps speed up recovery since we can seek
 * to these offsets and start processing the commit log.
 *
 * Every Commit Log is rolled over everytime it reaches its threshold in size;
 * the new log inherits the "dirty" bits from the old.
 *
 * Over time there could be a number of commit logs that would be generated.
 * To allow cleaning up non-active commit logs, whenever we flush a column family and update its bit flag in
 * the active CL, we take the dirty bit array and bitwise & it with the headers of the older logs.
 * If the result is 0, then it is safe to remove the older file.  (Since the new CL
 * inherited the old's dirty bitflags, getting a zero for any given bit in the anding
 * means that either the CF was clean in the old CL or it has been flushed since the
 * switch in the new.)
 *
 * Author : Avinash Lakshman ( alakshman@facebook.com) & Prashant Malik ( pmalik@facebook.com )
 */
public class CommitLog
{
    private static volatile int SEGMENT_SIZE = 128*1024*1024; // roll after log gets this big
    private static volatile CommitLog instance_;
    private static Lock lock_ = new ReentrantLock();
    private static Logger logger_ = Logger.getLogger(CommitLog.class);
    private static Map<String, CommitLogHeader> clHeaders_ = new HashMap<String, CommitLogHeader>();

    public static final class CommitLogContext
    {
        static CommitLogContext NULL = new CommitLogContext(null, -1L);
        /* Commit Log associated with this operation */
        public final String file;
        /* Offset within the Commit Log where this row as added */
        public final long position;

        public CommitLogContext(String file, long position)
        {
            this.file = file;
            this.position = position;
        }

        boolean isValidContext()
        {
            return (position != -1L);
        }
    }

    public static class CommitLogFileComparator implements Comparator<String>
    {
        public int compare(String f, String f2)
        {
            return (int)(getCreationTime(f) - getCreationTime(f2));
        }

        public boolean equals(Object o)
        {
            if ( !(o instanceof CommitLogFileComparator) )
                return false;
            return true;
        }
    }

    public static void setSegmentSize(int size)
    {
        SEGMENT_SIZE = size;
    }

    static int getSegmentCount()
    {
        return clHeaders_.size();
    }

    static long getCreationTime(String file)
    {
        String[] entries = FBUtilities.strip(file, "-.");
        return Long.parseLong(entries[entries.length - 2]);
    }

    private static IFileWriter createWriter(String file) throws IOException
    {        
        return SequenceFile.writer(file);
    }

    static CommitLog open() throws IOException
    {
        if ( instance_ == null )
        {
            CommitLog.lock_.lock();
            try
            {

                if ( instance_ == null )
                {
                    instance_ = new CommitLog(false);
                }
            }
            finally
            {
                CommitLog.lock_.unlock();
            }
        }
        return instance_;
    }

    /* Current commit log file */
    private String logFile_;
    /* header for current commit log */
    private CommitLogHeader clHeader_;
    private IFileWriter logWriter_;

    /*
     * Generates a file name of the format CommitLog-<table>-<timestamp>.log in the
     * directory specified by the Database Descriptor.
    */
    private void setNextFileName()
    {
        logFile_ = DatabaseDescriptor.getLogFileLocation() + System.getProperty("file.separator") +
                   "CommitLog-" + System.currentTimeMillis() + ".log";
    }

    /*
     * param @ table - name of table for which we are maintaining
     *                 this commit log.
     * param @ recoverymode - is commit log being instantiated in
     *                        in recovery mode.
    */
    CommitLog(boolean recoveryMode) throws IOException
    {
        if ( !recoveryMode )
        {
            setNextFileName();            
            logWriter_ = CommitLog.createWriter(logFile_);
            writeCommitLogHeader();
        }
    }

    /*
     * This ctor is currently used only for debugging. We
     * are now using it to modify the header so that recovery
     * can be tested in as many scenarios as we could imagine.
     *
     * param @ logFile - logfile which we wish to modify.
    */
    CommitLog(File logFile) throws IOException
    {
        logFile_ = logFile.getAbsolutePath();
        logWriter_ = CommitLog.createWriter(logFile_);
    }

    String getLogFile()
    {
        return logFile_;
    }
    
    private CommitLogHeader readCommitLogHeader(IFileReader logReader) throws IOException
    {
        int size = (int)logReader.readLong();
        byte[] bytes = new byte[size];
        logReader.readDirect(bytes);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        return CommitLogHeader.serializer().deserialize(new DataInputStream(byteStream));
    }

    /*
     * Write the serialized commit log header into the specified commit log.
    */
    private static void writeCommitLogHeader(String commitLogFileName, byte[] bytes) throws IOException
    {
        IFileWriter logWriter = CommitLog.createWriter(commitLogFileName);
        writeCommitLogHeader(logWriter, bytes);
    }

    /*
     * This is invoked on startup via the ctor. It basically
     * writes a header with all bits set to zero.
    */
    private void writeCommitLogHeader() throws IOException
    {
        int cfSize = Table.TableMetadata.getColumnFamilyCount();
        clHeader_ = new CommitLogHeader(cfSize);
        writeCommitLogHeader(logWriter_, clHeader_.toByteArray());
    }

    /** writes header at the beginning of the file, then seeks back to current position */
    private void seekAndWriteCommitLogHeader(byte[] bytes) throws IOException
    {
        long currentPos = logWriter_.getCurrentPosition();
        logWriter_.seek(0);

        writeCommitLogHeader(logWriter_, bytes);

        logWriter_.seek(currentPos);
    }

    private static void writeCommitLogHeader(IFileWriter logWriter, byte[] bytes) throws IOException
    {
        logWriter.writeLong(bytes.length);
        logWriter.writeDirect(bytes);
    }

    void recover(File[] clogs) throws IOException
    {
        DataInputBuffer bufIn = new DataInputBuffer();

        for (File file : clogs)
        {
            IFileReader reader = SequenceFile.reader(file.getAbsolutePath());
            CommitLogHeader clHeader = readCommitLogHeader(reader);
            /* seek to the lowest position */
            int lowPos = CommitLogHeader.getLowestPosition(clHeader);
            /*
             * If lowPos == 0 then we need to skip the processing of this
             * file.
            */
            if (lowPos == 0)
                break;
            else
                reader.seek(lowPos);

            Set<Table> tablesRecovered = new HashSet<Table>();

            /* read the logs populate RowMutation and apply */
            while ( !reader.isEOF() )
            {
                byte[] bytes = new byte[(int)reader.readLong()];
                reader.readDirect(bytes);
                bufIn.reset(bytes, bytes.length);

                /* read the commit log entry */
                Row row = Row.serializer().deserialize(bufIn);
                Table table = Table.open(row.getTable());
                tablesRecovered.add(table);
                Collection<ColumnFamily> columnFamilies = new ArrayList<ColumnFamily>(row.getColumnFamilies());
                /* remove column families that have already been flushed */
                for (ColumnFamily columnFamily : columnFamilies)
                {
                    /* TODO: Remove this to not process Hints */
                    if ( !DatabaseDescriptor.isApplicationColumnFamily(columnFamily.name()) )
                    {
                        row.removeColumnFamily(columnFamily);
                        continue;
                    }
                    int id = table.getColumnFamilyId(columnFamily.name());
                    if ( !clHeader.isDirty(id) || reader.getCurrentPosition() < clHeader.getPosition(id) )
                        row.removeColumnFamily(columnFamily);
                }
                if ( !row.isEmpty() )
                {
                    table.applyNow(row);
                }
            }
            reader.close();
            /* apply the rows read -- success will result in the CL file being discarded */
            for (Table table : tablesRecovered)
            {
                table.flush(true);
            }
        }
    }

    /*
     * Update the header of the commit log if a new column family
     * is encountered for the first time.
    */
    private void maybeUpdateHeader(Row row) throws IOException
    {
        Table table = Table.open(row.getTable());
        for (ColumnFamily columnFamily : row.getColumnFamilies())
        {
            int id = table.getColumnFamilyId(columnFamily.name());
            if (!clHeader_.isDirty(id) || (clHeader_.isDirty(id) && clHeader_.getPosition(id) == 0))
            {
                clHeader_.turnOn(id, logWriter_.getCurrentPosition());
                seekAndWriteCommitLogHeader(clHeader_.toByteArray());
            }
        }
    }
    
    CommitLogContext getContext() throws IOException
    {
        return new CommitLogContext(logFile_, logWriter_.getCurrentPosition());
    }

    /*
     * Adds the specified row to the commit log. This method will reset the
     * file offset to what it is before the start of the operation in case
     * of any problems. This way we can assume that the subsequent commit log
     * entry will override the garbage left over by the previous write.
    */
    synchronized CommitLogContext add(Row row) throws IOException
    {
        long currentPosition = -1L;
        CommitLogContext cLogCtx = null;
        DataOutputBuffer cfBuffer = new DataOutputBuffer();

        try
        {
            /* serialize the row */
            cfBuffer.reset();
            Row.serializer().serialize(row, cfBuffer);
            currentPosition = logWriter_.getCurrentPosition();
            cLogCtx = new CommitLogContext(logFile_, currentPosition);
            /* Update the header */
            maybeUpdateHeader(row);
            logWriter_.writeLong(cfBuffer.getLength());
            logWriter_.append(cfBuffer);
            checkThresholdAndRollLog();
        }
        catch (IOException e)
        {
            if ( currentPosition != -1 )
                logWriter_.seek(currentPosition);
            throw e;
        }
        finally
        {                  	
            cfBuffer.close();            
        }
        return cLogCtx;
    }

    /*
     * This is called on Memtable flush to add to the commit log
     * a token indicating that this column family has been flushed.
     * The bit flag associated with this column family is set in the
     * header and this is used to decide if the log file can be deleted.
    */
    synchronized void onMemtableFlush(String tableName, String cf, CommitLog.CommitLogContext cLogCtx) throws IOException
    {
        Table table = Table.open(tableName);
        int id = table.getColumnFamilyId(cf);
        discardCompletedSegments(cLogCtx, id);
    }

    /*
     * Delete log segments whose contents have been turned into SSTables.
     *
     * param @ cLogCtx The commitLog context .
     * param @ id id of the columnFamily being flushed to disk.
     *
    */
    private void discardCompletedSegments(CommitLog.CommitLogContext cLogCtx, int id) throws IOException
    {
        /* retrieve the commit log header associated with the file in the context */
        CommitLogHeader commitLogHeader = clHeaders_.get(cLogCtx.file);
        if(commitLogHeader == null )
        {
            if( logFile_.equals(cLogCtx.file) )
            {
                /* this means we are dealing with the current commit log. */
                commitLogHeader = clHeader_;
                clHeaders_.put(cLogCtx.file, clHeader_);
            }
            else
                return;
        }

        /*
         * log replay assumes that we only have to look at entries past the last
         * flush position, so verify that this flush happens after the last.
         * (Currently Memtables are flushed on a single thread so this should be fine.)
        */
        assert cLogCtx.position >= commitLogHeader.getPosition(id);

        commitLogHeader.turnOff(id);
        /* Sort the commit logs based on creation time */
        List<String> oldFiles = new ArrayList<String>(clHeaders_.keySet());
        Collections.sort(oldFiles, new CommitLogFileComparator());
        List<String> listOfDeletedFiles = new ArrayList<String>();
        /*
         * Loop through all the commit log files in the history. Now process
         * all files that are older than the one in the context. For each of
         * these files the header needs to modified by performing a bitwise &
         * of the header with the header of the file in the context. If we
         * encounter the file in the context in our list of old commit log files
         * then we update the header and write it back to the commit log.
        */
        for(String oldFile : oldFiles)
        {
            if(oldFile.equals(cLogCtx.file))
            {
                /*
                 * We need to turn on again. This is because we always keep
                 * the bit turned on and the position indicates from where the
                 * commit log needs to be read. When a flush occurs we turn off
                 * perform & operation and then turn on with the new position.
                */
                commitLogHeader.turnOn(id, cLogCtx.position);
                writeCommitLogHeader(cLogCtx.file, commitLogHeader.toByteArray());
                break;
            }
            else
            {
                CommitLogHeader oldCommitLogHeader = clHeaders_.get(oldFile);
                oldCommitLogHeader.and(commitLogHeader);
                if(oldCommitLogHeader.isSafeToDelete())
                {
                	if (logger_.isDebugEnabled())
                	  logger_.debug("Deleting commit log:"+ oldFile);
                    FileUtils.deleteAsync(oldFile);
                    listOfDeletedFiles.add(oldFile);
                }
                else
                {
                    writeCommitLogHeader(oldFile, oldCommitLogHeader.toByteArray());
                }
            }
        }

        for ( String deletedFile : listOfDeletedFiles)
        {
            clHeaders_.remove(deletedFile);
        }
    }

    private void checkThresholdAndRollLog() throws IOException
    {
        if (logWriter_.getFileSize() >= SEGMENT_SIZE)
        {
            /* Rolls the current log file over to a new one. */
            setNextFileName();
            String oldLogFile = logWriter_.getFileName();
            logWriter_.close();

            /* point reader/writer to a new commit log file. */
            logWriter_ = CommitLog.createWriter(logFile_);
            /* squirrel away the old commit log header */
            clHeaders_.put(oldLogFile, new CommitLogHeader(clHeader_));
            // we leave the old 'dirty' bits alone, so we can test for
            // whether it's safe to remove a given log segment by and-ing its dirty
            // with the current one.
            clHeader_.zeroPositions();
            writeCommitLogHeader(logWriter_, clHeader_.toByteArray());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3438.java