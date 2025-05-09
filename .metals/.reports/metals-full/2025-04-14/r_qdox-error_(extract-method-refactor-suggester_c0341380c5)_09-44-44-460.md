error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1724.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1724.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[21,1]

error in qdox parser
file content:
```java
offset: 858
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1724.java
text:
```scala
class TapeInputStream extends FilterInputStream {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
p@@ackage org.apache.commons.compress.archivers.dump;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


/**
 * Filter stream that mimics a physical tape drive capable of compressing
 * the data stream.
 *
 * @NotThreadSafe
 */
public class TapeInputStream extends FilterInputStream {
    private byte[] blockBuffer = new byte[DumpArchiveConstants.TP_SIZE];
    private int currBlkIdx = -1;
    private int blockSize = DumpArchiveConstants.TP_SIZE;
    private int recordSize = DumpArchiveConstants.TP_SIZE;
    private int readOffset = DumpArchiveConstants.TP_SIZE;
    private boolean isCompressed = false;
    private long bytesRead = 0;

    /**
     * Constructor
     */
    public TapeInputStream(InputStream in) {
        super(in);
    }

    /**
     * Set the DumpArchive Buffer's block size. We need to sync the block size with the
     * dump archive's actual block size since compression is handled at the
     * block level.
     *
     * @param recsPerBlock
     *            records per block
     * @param isCompressed
     *            true if the archive is compressed
     * @throws IOException
     *             more than one block has been read
     * @throws IOException
     *             there was an error reading additional blocks.
     */
    public void resetBlockSize(int recsPerBlock, boolean isCompressed)
        throws IOException {
        this.isCompressed = isCompressed;

        blockSize = recordSize * recsPerBlock;

        // save first block in case we need it again
        byte[] oldBuffer = blockBuffer;

        // read rest of new block
        blockBuffer = new byte[blockSize];
        System.arraycopy(oldBuffer, 0, blockBuffer, 0, recordSize);
        readFully(blockBuffer, recordSize, blockSize - recordSize);

        this.currBlkIdx = 0;
        this.readOffset = recordSize;
    }

    /**
     * @see java.io.InputStream#available
     */
    @Override
    public int available() throws IOException {
        if (readOffset < blockSize) {
            return blockSize - readOffset;
        }

        return in.available();
    }

    /**
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        throw new IllegalArgumentException(
            "all reads must be multiple of record size (" + recordSize +
            " bytes.");
    }

    /**
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if ((len % recordSize) != 0) {
            throw new IllegalArgumentException(
                "all reads must be multiple of record size (" + recordSize +
                " bytes.");
        }

        int bytes = 0;

        while (bytes < len) {
            // we need to read from the underlying stream.
            // this will reset readOffset value.
            // return -1 if there's a problem.
            if ((readOffset == blockSize) && !readBlock(true)) {
                return -1;
            }

            int n = 0;

            if ((readOffset + (len - bytes)) <= blockSize) {
                // we can read entirely from the buffer.
                n = len - bytes;
            } else {
                // copy what we can from the buffer.
                n = blockSize - readOffset;
            }

            // copy data, increment counters.
            System.arraycopy(blockBuffer, readOffset, b, off, n);
            readOffset += n;
            bytes += n;
            off += n;
        }

        return bytes;
    }

    /**
     * Skip bytes. Same as read but without the arraycopy.
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public long skip(long len) throws IOException {
        if ((len % recordSize) != 0) {
            throw new IllegalArgumentException(
                "all reads must be multiple of record size (" + recordSize +
                " bytes.");
        }

        long bytes = 0;

        while (bytes < len) {
            // we need to read from the underlying stream.
            // this will reset readOffset value. We do not perform
            // any decompression if we won't eventually read the data.
            // return -1 if there's a problem.
            if ((readOffset == blockSize) &&
                    !readBlock((len - bytes) < blockSize)) {
                return -1;
            }

            long n = 0;

            if ((readOffset + (len - bytes)) <= blockSize) {
                // we can read entirely from the buffer.
                n = len - bytes;
            } else {
                // copy what we can from the buffer.
                n = blockSize - readOffset;
            }

            // do not copy data but still increment counters.
            readOffset += n;
            bytes += n;
        }

        return bytes;
    }

    /**
     * Close the input stream.
     *
     * @throws IOException on error
     */
    @Override
    public void close() throws IOException {
        if (in != null) {
            if (in != System.in) {
                in.close();
            }
        }
    }

    /**
     * Peek at the next record from the input stream and return the data.
     *
     * @return The record data.
     * @throws IOException on error
     */
    public byte[] peek() throws IOException {
        // we need to read from the underlying stream. This
        // isn't a problem since it would be the first step in
        // any subsequent read() anyway.
        if ((readOffset == blockSize) && !readBlock(true)) {
            return null;
        }

        // copy data, increment counters.
        byte[] b = new byte[recordSize];
        System.arraycopy(blockBuffer, readOffset, b, 0, b.length);

        return b;
    }

    /**
     * Read a record from the input stream and return the data.
     *
     * @return The record data.
     * @throws IOException on error
     */
    public byte[] readRecord() throws IOException {
        byte[] result = new byte[recordSize];

        if (-1 == read(result, 0, result.length)) {
            throw new ShortFileException();
        }

        return result;
    }

    /**
     * Read next block. All decompression is handled here.
     *
     * @param decompress if false the buffer will not be decompressed.
     *        This is an optimization for longer seeks.
     * @return false if End-Of-File, else true
     */
    private boolean readBlock(boolean decompress) throws IOException {
        boolean success = true;

        if (in == null) {
            throw new IOException("input buffer is closed");
        }

        if (!isCompressed || (currBlkIdx == -1)) {
            // file is not compressed
            success = readFully(blockBuffer, 0, blockSize);
            bytesRead += blockSize;
        } else {
            in.read(blockBuffer, 0, 4);
            bytesRead += 4;

            int h = DumpArchiveUtil.convert32(blockBuffer, 0);
            boolean compressed = (h & 0x01) == 0x01;

            if (!compressed) {
                // file is compressed but this block is not.
                success = readFully(blockBuffer, 0, blockSize);
                bytesRead += blockSize;
            } else {
                // this block is compressed.
                int flags = (h >> 1) & 0x07;
                int length = (h >> 4) & 0x0FFFFFFF;
                byte[] compBuffer = new byte[length];
                success = readFully(compBuffer, 0, length);
                bytesRead += length;

                if (!decompress) {
                    // just in case someone reads the data.
                    Arrays.fill(blockBuffer, (byte) 0);
                } else {
                    switch (DumpArchiveConstants.COMPRESSION_TYPE.find(flags &
                        0x03)) {
                    case ZLIB:

                        try {
                            Inflater inflator = new Inflater();
                            inflator.setInput(compBuffer, 0, compBuffer.length);
                            length = inflator.inflate(blockBuffer);

                            if (length != blockSize) {
                                throw new ShortFileException();
                            }

                            inflator.end();
                        } catch (DataFormatException e) {
                            throw new DumpArchiveException("bad data", e);
                        }

                        break;

                    case BZLIB:
                        throw new UnsupportedCompressionAlgorithmException(
                            "BZLIB2");

                    case LZO:
                        throw new UnsupportedCompressionAlgorithmException(
                            "LZO");

                    default:
                        throw new UnsupportedCompressionAlgorithmException();
                    }
                }
            }
        }

        currBlkIdx++;
        readOffset = 0;

        return success;
    }

    /**
     * Read buffer
     */
    private boolean readFully(byte[] b, int off, int len)
        throws IOException {
        int count = 0;

        while (count < len) {
            int n = in.read(b, off + count, len - count);

            if (n == -1) {
                throw new ShortFileException();
            }

            count += n;
        }

        return true;
    }

    /**
     * Get number of bytes read.
     */
    public long getBytesRead() {
        return bytesRead;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1724.java