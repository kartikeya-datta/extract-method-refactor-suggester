error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3632.java
text:
```scala
i@@n.reset(0);

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

package org.apache.cassandra.streaming;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.ColumnFamily;
import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.db.Table;
import org.apache.cassandra.db.compaction.AbstractCompactedRow;
import org.apache.cassandra.db.compaction.CompactionController;
import org.apache.cassandra.db.compaction.PrecompactedRow;
import org.apache.cassandra.io.sstable.*;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.BytesReadTracker;
import org.apache.cassandra.utils.Pair;

public class IncomingStreamReader
{
    private static final Logger logger = LoggerFactory.getLogger(IncomingStreamReader.class);

    protected final PendingFile localFile;
    protected final PendingFile remoteFile;
    protected final StreamInSession session;
    private final Socket socket;

    public IncomingStreamReader(StreamHeader header, Socket socket) throws IOException
    {
        this.socket = socket;
        InetSocketAddress remoteAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
        session = StreamInSession.get(remoteAddress.getAddress(), header.sessionId);
        session.addFiles(header.pendingFiles);
        // set the current file we are streaming so progress shows up in jmx
        session.setCurrentFile(header.file);
        session.setTable(header.table);
        // pendingFile gets the new context for the local node.
        remoteFile = header.file;
        localFile = remoteFile != null ? StreamIn.getContextMapping(remoteFile) : null;
    }

    public void read() throws IOException
    {
        if (remoteFile != null)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Receiving stream");
                logger.debug("Creating file for {} with {} estimated keys",
                             localFile.getFilename(),
                             remoteFile.estimatedKeys);
            }

            assert remoteFile.estimatedKeys > 0;
            SSTableReader reader = null;
            logger.debug("Estimated keys {}", remoteFile.estimatedKeys);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            try
            {
                reader = streamIn(dis, localFile, remoteFile);
            }
            catch (IOException ex)
            {
                retry();
                throw ex;
            }
            finally
            {
                dis.close();
            }

            session.finished(remoteFile, reader);
        }

        session.closeIfFinished();
    }

    private SSTableReader streamIn(DataInput input, PendingFile localFile, PendingFile remoteFile) throws IOException
    {
        ColumnFamilyStore cfs = Table.open(localFile.desc.ksname).getColumnFamilyStore(localFile.desc.cfname);
        DecoratedKey key;
        SSTableWriter writer = new SSTableWriter(localFile.getFilename(), remoteFile.estimatedKeys);
        CompactionController controller = null;

        try
        {
            BytesReadTracker in = new BytesReadTracker(input);

            for (Pair<Long, Long> section : localFile.sections)
            {
                long length = section.right - section.left;
                long bytesRead = 0;
                while (bytesRead < length)
                {
                    in.reset();
                    key = SSTableReader.decodeKey(StorageService.getPartitioner(), localFile.desc, ByteBufferUtil.readWithShortLength(in));
                    long dataSize = SSTableReader.readRowSize(in, localFile.desc);
                    ColumnFamily cf = null;
                    if (cfs.metadata.getDefaultValidator().isCommutative())
                    {
                        // take care of counter column family
                        if (controller == null)
                            controller = new CompactionController(cfs, Collections.<SSTableReader>emptyList(), Integer.MAX_VALUE, true);
                        SSTableIdentityIterator iter = new SSTableIdentityIterator(cfs.metadata, in, key, 0, dataSize, true);
                        AbstractCompactedRow row = controller.getCompactedRow(iter);
                        writer.append(row);
                        // row append does not update the max timestamp on its own
                        writer.updateMaxTimestamp(row.maxTimestamp());

                        if (row instanceof PrecompactedRow)
                        {
                            // we do not purge so we should not get a null here
                            cf = ((PrecompactedRow)row).getFullColumnFamily();
                        }
                    }
                    else
                    {
                        // skip BloomFilter
                        IndexHelper.skipBloomFilter(in);
                        // skip Index
                        IndexHelper.skipIndex(in);

                        // restore ColumnFamily
                        cf = ColumnFamily.create(cfs.metadata);
                        ColumnFamily.serializer().deserializeFromSSTableNoColumns(cf, in);
                        ColumnFamily.serializer().deserializeColumns(in, cf, true, true);

                        // write key and cf
                        writer.append(key, cf);
                    }

                    // update cache
                    ColumnFamily cached = cfs.getRawCachedRow(key);
                    if (cached != null)
                    {
                        switch (remoteFile.type)
                        {
                            case AES:
                                if (dataSize > DatabaseDescriptor.getInMemoryCompactionLimit())
                                {
                                    // We have a key in cache for a very big row, that is fishy. We don't fail here however because that would prevent the sstable
                                    // from being build (and there is no real point anyway), so we just invalidate the row for correction and log a warning.
                                    logger.warn("Found a cached row over the in memory compaction limit during post-streaming rebuilt; it is highly recommended to avoid huge row on column family with row cache enabled.");
                                    cfs.invalidateCachedRow(key);
                                }
                                else
                                {
                                    assert cf != null;
                                    cfs.updateRowCache(key, cf);
                                }
                                break;
                            default:
                                cfs.invalidateCachedRow(key);
                                break;
                        }
                    }

                    bytesRead += in.getBytesRead();
                    remoteFile.progress += in.getBytesRead();
                }
            }
            return writer.closeAndOpenReader();
        }
        finally
        {
            writer.cleanupIfNecessary();
        }
    }

    private void retry() throws IOException
    {
        /* Ask the source node to re-stream this file. */
        session.retry(remoteFile);

        /* Delete the orphaned file. */
        FileUtils.deleteWithConfirm(new File(localFile.getFilename()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3632.java