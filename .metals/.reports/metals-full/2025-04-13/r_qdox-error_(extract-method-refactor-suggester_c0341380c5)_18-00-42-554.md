error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3541.java
text:
```scala
s@@b.append(m == null ? "<deleted>" : m.cfName).append(" (").append(cfId).append("), ");

/*
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

package org.apache.cassandra.db.commitlog;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.net.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.RowMutation;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.io.util.BufferedRandomAccessFile;

public class CommitLogSegment
{
    private static final Logger logger = LoggerFactory.getLogger(CommitLogSegment.class);
    private static Pattern COMMIT_LOG_FILE_PATTERN = Pattern.compile("CommitLog-(\\d+).log");

    public final long id;
    private final BufferedRandomAccessFile logWriter;

    // cache which cf is dirty in this segment to avoid having to lookup all ReplayPositions to decide if we could delete this segment
    private Map<Integer, Integer> cfLastWrite = new HashMap<Integer, Integer>();

    public CommitLogSegment()
    {
        id = System.currentTimeMillis();
        String logFile = DatabaseDescriptor.getCommitLogLocation() + File.separator + "CommitLog-" + id + ".log";
        logger.info("Creating new commitlog segment " + logFile);

        try
        {
            logWriter = createWriter(logFile);
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    // assume filename is a 'possibleCommitLogFile()'
    public static long idFromFilename(String filename)
    {
        Matcher matcher = COMMIT_LOG_FILE_PATTERN.matcher(filename);
        try
        {
            if (matcher.matches())
                return Long.valueOf(matcher.group(1));
            else
                return -1L;
        }
        catch (NumberFormatException e)
        {
            return -1L;
        }
    }

    public static boolean possibleCommitLogFile(String filename)
    {
        return COMMIT_LOG_FILE_PATTERN.matcher(filename).matches();
    }

    private static BufferedRandomAccessFile createWriter(String file) throws IOException
    {
        return new BufferedRandomAccessFile(new File(file), "rw", 128 * 1024, true);
    }

    public ReplayPosition write(RowMutation rowMutation) throws IOException
    {
        long currentPosition = -1L;
        try
        {
            currentPosition = logWriter.getFilePointer();
            assert currentPosition <= Integer.MAX_VALUE;
            ReplayPosition cLogCtx = new ReplayPosition(id, (int) currentPosition);

            for (ColumnFamily columnFamily : rowMutation.getColumnFamilies())
            {
                // check for null cfm in case a cl write goes through after the cf is
                // defined but before a new segment is created.
                CFMetaData cfm = DatabaseDescriptor.getCFMetaData(columnFamily.id());
                if (cfm == null)
                {
                    logger.error("Attempted to write commit log entry for unrecognized column family: " + columnFamily.id());
                }
                else
                {
                    turnOn(cfm.cfId, (int) currentPosition);
                }
            }

            // write mutation, w/ checksum on the size and data
            Checksum checksum = new CRC32();
            byte[] serializedRow = rowMutation.getSerializedBuffer(MessagingService.version_);
            checksum.update(serializedRow.length);
            logWriter.writeInt(serializedRow.length);
            logWriter.writeLong(checksum.getValue());
            logWriter.write(serializedRow);
            checksum.update(serializedRow, 0, serializedRow.length);
            logWriter.writeLong(checksum.getValue());

            return cLogCtx;
        }
        catch (IOException e)
        {
            if (currentPosition != -1)
                logWriter.seek(currentPosition);
            throw e;
        }
    }

    public void sync() throws IOException
    {
        logWriter.sync();
    }

    public ReplayPosition getContext()
    {
        long position = logWriter.getFilePointer();
        assert position <= Integer.MAX_VALUE;
        return new ReplayPosition(id, (int) position);
    }

    public String getPath()
    {
        return logWriter.getPath();
    }

    public String getName()
    {
        return logWriter.getPath().substring(logWriter.getPath().lastIndexOf(File.separator) + 1);
    }

    public long length()
    {
        try
        {
            return logWriter.length();
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    public void close()
    {
        try
        {
            logWriter.close();
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    void turnOn(Integer cfId, Integer position)
    {
        cfLastWrite.put(cfId, position);
    }

    /**
     * Turn the dirty bit off only if there has been no write since the flush
     * position was grabbed.
     */
    void turnOffIfNotWritten(Integer cfId, Integer flushPosition)
    {
        Integer lastWritten = cfLastWrite.get(cfId);
        if (lastWritten == null || lastWritten < flushPosition)
            cfLastWrite.remove(cfId);
    }

    void turnOff(Integer cfId)
    {
        cfLastWrite.remove(cfId);
    }

    // For debugging, not fast
    String dirtyString()
    {
        StringBuilder sb = new StringBuilder();
        for (Integer cfId : cfLastWrite.keySet())
        {
            CFMetaData m = DatabaseDescriptor.getCFMetaData(cfId);
            sb.append(m == null ? m.cfName : "<deleted>").append(" (").append(cfId).append("), ");
        }
        return sb.toString();
    }

    boolean isSafeToDelete()
    {
        return cfLastWrite.isEmpty();
    }

    @Override
    public String toString()
    {
        return "CommitLogSegment(" + logWriter.getPath() + ')';
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3541.java