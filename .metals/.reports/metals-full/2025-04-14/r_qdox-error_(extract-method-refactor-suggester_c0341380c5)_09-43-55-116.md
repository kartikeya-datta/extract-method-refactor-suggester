error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1363.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1363.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1363.java
text:
```scala
c@@ompressed = new byte[Snappy.maxCompressedLength(metadata.chunkLength)];

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
 */
package org.apache.cassandra.io.compress;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import org.apache.cassandra.io.sstable.Component;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.io.util.RandomAccessReader;
import org.apache.cassandra.streaming.FileStreamTask;
import org.apache.cassandra.streaming.PendingFile;
import org.apache.cassandra.utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xerial.snappy.Snappy;

public class CompressedRandomAccessReader extends RandomAccessReader
{
    private static final Logger logger = LoggerFactory.getLogger(CompressedRandomAccessReader.class);

    /**
     * Transfer sections of the file to the given target channel
     * This method streams decompressed data so receiving party responsible for compression
     *
     * @param file The compressed file to transfer
     * @param target Channel to transfer data into
     *
     * @throws IOException on any I/O error.
     */
    public static void transfer(PendingFile file, WritableByteChannel target) throws IOException
    {
        RandomAccessReader compressedFile = CompressedRandomAccessReader.open(file.getFilename(), true);

        try
        {
            for (Pair<Long, Long> section : file.sections)
            {
                long length = section.right - section.left;

                compressedFile.seek(section.left);

                while (length > 0)
                {
                    int toRead = (length > FileStreamTask.CHUNK_SIZE) ? FileStreamTask.CHUNK_SIZE : (int) length;

                    ByteBuffer buffer = compressedFile.readBytes(toRead);

                    long bytesTransferred = 0;

                    while (bytesTransferred < toRead)
                    {
                        // we don't need to re-read a buffer, it will write starting from buffer.position()
                        long lastWrite = target.write(buffer);
                        bytesTransferred += lastWrite;
                        file.progress += lastWrite;
                    }

                    length -= bytesTransferred;

                    if (logger.isDebugEnabled())
                        logger.debug("Bytes transferred " + bytesTransferred + "/" + file.size);
                }
            }
        }
        finally
        {
            FileUtils.closeQuietly(compressedFile);
        }
    }

    /**
     * Get metadata about given compressed file including uncompressed data length, chunk size
     * and list of the chunk offsets of the compressed data.
     *
     * @param dataFilePath Path to the compressed file
     *
     * @return metadata about given compressed file.
     */
    public static CompressionMetadata metadata(String dataFilePath)
    {
        Descriptor desc = Descriptor.fromFilename(dataFilePath);

        try
        {
            return new CompressionMetadata(desc.filenameFor(Component.COMPRESSION_INFO), new File(dataFilePath).length());
        }
        catch (IOException e)
        {
            throw new IOError(e);
        }
    }

    public static RandomAccessReader open(String dataFilePath, boolean skipIOCache) throws IOException
    {
        return open(dataFilePath, metadata(dataFilePath), skipIOCache);
    }

    public static RandomAccessReader open(String dataFilePath, CompressionMetadata metadata) throws IOException
    {
        return open(dataFilePath, metadata, false);
    }

    public static RandomAccessReader open(String dataFilePath, CompressionMetadata metadata, boolean skipIOCache) throws IOException
    {
        return new CompressedRandomAccessReader(dataFilePath, metadata, skipIOCache);
    }

    private final CompressionMetadata metadata;
    // used by reBuffer() to escape creating lots of temporary buffers
    private final byte[] compressed;

    private final FileInputStream source;

    public CompressedRandomAccessReader(String dataFilePath, CompressionMetadata metadata, boolean skipIOCache) throws IOException
    {
        super(new File(dataFilePath), metadata.chunkLength, skipIOCache);
        this.metadata = metadata;
        compressed = new byte[metadata.chunkLength];
        // can't use super.read(...) methods
        // that is why we are allocating special InputStream to read data from disk
        // from already open file descriptor
        source = new FileInputStream(getFD());
    }

    @Override
    protected void reBuffer() throws IOException
    {
        decompressChunk(metadata.chunkFor(current));
    }

    private void decompressChunk(CompressionMetadata.Chunk chunk) throws IOException
    {
        if (source.getChannel().position() != chunk.offset)
            source.getChannel().position(chunk.offset);

        if (source.read(compressed, 0, chunk.length) != chunk.length)
            throw new IOException(String.format("(%s) failed to read %d bytes from offset %d.", getPath(), chunk.length, chunk.offset));

        validBufferBytes = Snappy.rawUncompress(compressed, 0, chunk.length, buffer, 0);

        // buffer offset is always aligned
        bufferOffset = current & ~(buffer.length - 1);
    }

    @Override
    public long length() throws IOException
    {
        return metadata.dataLength;
    }

    @Override
    public String toString()
    {
        return String.format("%s - chunk length %d, data length %d.", getPath(), metadata.chunkLength, metadata.dataLength);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1363.java