error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1959.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1959.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1959.java
text:
```scala
p@@ublic static class Chunk

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
import java.util.HashMap;
import java.util.Map;

import org.apache.cassandra.config.ConfigurationException;
import org.apache.cassandra.io.sstable.Component;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.util.FileUtils;
import org.apache.cassandra.utils.BigLongArray;

/**
 * Holds metadata about compressed file
 */
public class CompressionMetadata
{
    public final long dataLength;
    public final long compressedFileLength;
    private final BigLongArray chunkOffsets;
    public final String indexFilePath;
    public final CompressionParameters parameters;

    /**
     * Create metadata about given compressed file including uncompressed data length, chunk size
     * and list of the chunk offsets of the compressed data.
     *
     * This is an expensive operation! Don't create more than one for each
     * sstable.
     *
     * @param dataFilePath Path to the compressed file
     *
     * @return metadata about given compressed file.
     */
    public static CompressionMetadata create(String dataFilePath)
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

    // This is package protected because of the tests.
    CompressionMetadata(String indexFilePath, long compressedLength) throws IOException
    {
        this.indexFilePath = indexFilePath;

        DataInputStream stream = new DataInputStream(new FileInputStream(indexFilePath));

        String compressorName = stream.readUTF();
        int optionCount = stream.readInt();
        Map<String, String> options = new HashMap<String, String>();
        for (int i = 0; i < optionCount; ++i)
        {
            String key = stream.readUTF();
            String value = stream.readUTF();
            options.put(key, value);
        }
        int chunkLength = stream.readInt();
        try
        {
            parameters = new CompressionParameters(compressorName, chunkLength, options);
        }
        catch (ConfigurationException e)
        {
            throw new RuntimeException("Cannot create CompressionParameters for stored parameters", e);
        }

        dataLength = stream.readLong();
        compressedFileLength = compressedLength;
        chunkOffsets = readChunkOffsets(stream);

        FileUtils.closeQuietly(stream);
    }

    public ICompressor compressor()
    {
        return parameters.sstableCompressor;
    }

    public int chunkLength()
    {
        return parameters.chunkLength();
    }

    /**
     * Read offsets of the individual chunks from the given input.
     *
     * @param input Source of the data.
     *
     * @return collection of the chunk offsets.
     *
     * @throws java.io.IOException on any I/O error (except EOF).
     */
    private BigLongArray readChunkOffsets(DataInput input) throws IOException
    {
        int chunkCount = input.readInt();
        BigLongArray offsets = new BigLongArray(chunkCount);

        for (int i = 0; i < chunkCount; i++)
        {
            try
            {
                offsets.set(i, input.readLong());
            }
            catch (EOFException e)
            {
                throw new EOFException(String.format("Corrupted Index File %s: read %d but expected %d chunks.",
                                                     indexFilePath,
                                                     i,
                                                     chunkCount));
            }
        }

        return offsets;
    }

    /**
     * Get a chunk of compressed data (offset, length) corresponding to given position
     *
     * @param position Position in the file.
     * @return pair of chunk offset and length.
     * @throws java.io.IOException on any I/O error.
     */
    public Chunk chunkFor(long position) throws IOException
    {
        // position of the chunk
        int idx = (int) (position / parameters.chunkLength());

        if (idx >= chunkOffsets.size)
            throw new EOFException();

        long chunkOffset = chunkOffsets.get(idx);
        long nextChunkOffset = (idx + 1 == chunkOffsets.size)
                                ? compressedFileLength
                                : chunkOffsets.get(idx + 1);

        return new Chunk(chunkOffset, (int) (nextChunkOffset - chunkOffset - 4)); // "4" bytes reserved for checksum
    }

    public static class Writer extends RandomAccessFile
    {
        // place for uncompressed data length in the index file
        private long dataLengthOffset = -1;

        public Writer(String path) throws IOException
        {
            super(path, "rw");
        }

        public void writeHeader(CompressionParameters parameters) throws IOException
        {
            // algorithm
            writeUTF(parameters.sstableCompressor.getClass().getSimpleName());
            writeInt(parameters.otherOptions.size());
            for (Map.Entry<String, String> entry : parameters.otherOptions.entrySet())
            {
                writeUTF(entry.getKey());
                writeUTF(entry.getValue());
            }

            // store the length of the chunk
            writeInt(parameters.chunkLength());
            // store position and reserve a place for uncompressed data length and chunks count
            dataLengthOffset = getFilePointer();
            writeLong(-1);
            writeInt(-1);
        }

        public void finalizeHeader(long dataLength, int chunks) throws IOException
        {
            assert dataLengthOffset != -1 : "writeHeader wasn't called";

            long currentPosition = getFilePointer();

            // seek back to the data length position
            seek(dataLengthOffset);

            // write uncompressed data length and chunks count
            writeLong(dataLength);
            writeInt(chunks);

            // seek forward to the previous position
            seek(currentPosition);
        }

        /**
         * Get a chunk offset by it's index.
         *
         * @param chunkIndex Index of the chunk.
         *
         * @return offset of the chunk in the compressed file.
         *
         * @throws IOException any I/O error.
         */
        public long chunkOffsetBy(int chunkIndex) throws IOException
        {
            if (dataLengthOffset == -1)
                throw new IllegalStateException("writeHeader wasn't called");

            long position = getFilePointer();

            // seek to the position of the given chunk
            seek(dataLengthOffset
                 + 8 // size reserved for uncompressed data length
                 + 4 // size reserved for chunk count
                 + (chunkIndex * 8L));

            try
            {
                return readLong();
            }
            finally
            {
                // back to the original position
                seek(position);
            }
        }

        /**
         * Reset the writer so that the next chunk offset written will be the
         * one of {@code chunkIndex}.
         */
        public void resetAndTruncate(int chunkIndex) throws IOException
        {
            seek(dataLengthOffset
                 + 8 // size reserved for uncompressed data length
                 + 4 // size reserved for chunk count
                 + (chunkIndex * 8L));
            getChannel().truncate(getFilePointer());
        }
    }

    /**
     * Holds offset and length of the file chunk
     */
    public class Chunk
    {
        public final long offset;
        public final int length;

        public Chunk(long offset, int length)
        {
            this.offset = offset;
            this.length = length;
        }

        public String toString()
        {
            return String.format("Chunk<offset: %d, length: %d>", offset, length);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1959.java