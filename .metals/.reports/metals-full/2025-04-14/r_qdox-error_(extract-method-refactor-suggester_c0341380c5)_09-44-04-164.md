error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13964.java
text:
```scala
l@@ength |= (((long) readOneByte()) << 24);

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
package org.apache.commons.compress.compressors.snappy;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;

/**
 * CompressorInputStream for the raw Snappy format.
 *
 * <p>This implementation uses an internal buffer in order to handle
 * the back-references that are at the heart of the LZ77 algorithm.
 * The size of the buffer must be at least as big as the biggest
 * offset used in the compressed stream.  The current version of the
 * Snappy algorithm as defined by Google works on 32k blocks and
 * doesn't contain offsets bigger than 32k which is the default block
 * size used by this class.</p>
 *
 * @see "http://code.google.com/p/snappy/source/browse/trunk/format_description.txt"
 * @since 1.7
 */
public class SnappyCompressorInputStream extends CompressorInputStream {

    /** Mask used to determine the type of "tag" is being processed */
    private static final int TAG_MASK = 0x03;

    /** Default block size */
    public static final int DEFAULT_BLOCK_SIZE = 32768;

    /** Buffer to write decompressed bytes to for back-references */
    private final byte[] decompressBuf;

    /** 
     * One behind the index of the last byte in the buffer that was
     * written
     */
    private int writeIndex;

    /**
     * Index of the next byte to be read.
     */
    private int readIndex;

    /** The actual block size specified */
    private final int blockSize;

    /** The underlying stream to read compressed data from */
    private final InputStream in;

    /** The size of the uncompressed data */
    private final int size;

    /**
     * Number of uncompressed bytes still to be read.
     */
    private int uncompressedBytesRemaining;

    // used in no-arg read method
    private final byte[] oneByte = new byte[1];

    private boolean endReached = false;

    /**
     * Constructor using the default buffer size of 32k.
     * 
     * @param is
     *            An InputStream to read compressed data from
     * 
     * @throws IOException
     */
    public SnappyCompressorInputStream(final InputStream is) throws IOException {
        this(is, DEFAULT_BLOCK_SIZE);
    }

    /**
     * Constructor using a configurable buffer size.
     * 
     * @param is
     *            An InputStream to read compressed data from
     * @param blockSize
     *            The block size used in compression
     * 
     * @throws IOException
     */
    public SnappyCompressorInputStream(final InputStream is, final int blockSize)
            throws IOException {
        this.in = is;
        this.blockSize = blockSize;
        this.decompressBuf = new byte[blockSize * 3];
        this.writeIndex = readIndex = 0;
        uncompressedBytesRemaining = size = (int) readSize();
    }

    /** {@inheritDoc} */
    @Override
    public int read() throws IOException {
        return read(oneByte, 0, 1) == -1 ? -1 : (oneByte[0] & 0xFF);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        in.close();
    }

    /** {@inheritDoc} */
    @Override
    public int available() {
        return writeIndex - readIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (endReached) {
            return -1;
        }
        final int avail = available();
        if (len > avail) {
            fill(len - avail);
        }

        int readable = Math.min(len, available());
        System.arraycopy(decompressBuf, readIndex, b, off, readable);
        readIndex += readable;
        if (readIndex > blockSize) {
            slideBuffer();
        }
        return readable;
    }

    /**
     * Try to fill the buffer with enough bytes to satisfy the current
     * read request.
     *
     * @param len the number of uncompressed bytes to read
     */
    private void fill(int len) throws IOException {
        if (uncompressedBytesRemaining == 0) {
            endReached = true;
        }
        int readNow = Math.min(len, uncompressedBytesRemaining);

        while (readNow > 0) {
            final int b = readOneByte();
            int length = 0;
            long offset = 0;

            switch (b & TAG_MASK) {

            case 0x00:

                length = readLiteralLength(b);

                if (expandLiteral(length)) {
                    return;
                }
                break;

            case 0x01:

                /*
                 * These elements can encode lengths between [4..11] bytes and
                 * offsets between [0..2047] bytes. (len-4) occupies three bits
                 * and is stored in bits [2..4] of the tag byte. The offset
                 * occupies 11 bits, of which the upper three are stored in the
                 * upper three bits ([5..7]) of the tag byte, and the lower
                 * eight are stored in a byte following the tag byte.
                 */

                length = 4 + ((b >> 2) & 0x07);
                offset = (b & 0xE0) << 3;
                offset |= readOneByte();

                if (expandCopy(offset, length)) {
                    return;
                }
                break;

            case 0x02:

                /*
                 * These elements can encode lengths between [1..64] and offsets
                 * from [0..65535]. (len-1) occupies six bits and is stored in
                 * the upper six bits ([2..7]) of the tag byte. The offset is
                 * stored as a little-endian 16-bit integer in the two bytes
                 * following the tag byte.
                 */

                length = (b >> 2) + 1;

                offset = readOneByte();
                offset |= readOneByte() << 8;

                if (expandCopy(offset, length)) {
                    return;
                }
                break;

            case 0x03:

                /*
                 * These are like the copies with 2-byte offsets (see previous
                 * subsection), except that the offset is stored as a 32-bit
                 * integer instead of a 16-bit integer (and thus will occupy
                 * four bytes).
                 */

                length = (b >> 2) + 1;

                offset = readOneByte();
                offset |= readOneByte() << 8;
                offset |= readOneByte() << 16;
                offset |= readOneByte() << 24;

                if (expandCopy(offset, length)) {
                    return;
                }
                break;
            }

            readNow -= length;
            uncompressedBytesRemaining -= length;
        }
    }

    /**
     * Slide buffer.
     *
     * <p>Move all bytes of the buffer after the first block down to
     * the beginning of the buffer.</p>
     */
    private void slideBuffer() {
        System.arraycopy(decompressBuf, blockSize, decompressBuf, 0,
                         blockSize * 2);
        writeIndex -= blockSize;
        readIndex -= blockSize;
    }


    /*
     * For literals up to and including 60 bytes in length, the
     * upper six bits of the tag byte contain (len-1). The literal
     * follows immediately thereafter in the bytestream. - For
     * longer literals, the (len-1) value is stored after the tag
     * byte, little-endian. The upper six bits of the tag byte
     * describe how many bytes are used for the length; 60, 61, 62
     * or 63 for 1-4 bytes, respectively. The literal itself follows
     * after the length.
     */
    private int readLiteralLength(int b) throws IOException {
        int length;
        switch (b >> 2) {
        case 60:
            length = readOneByte();
            break;
        case 61:
            length = readOneByte();
            length |= (readOneByte() << 8);
            break;
        case 62:
            length = readOneByte();
            length |= (readOneByte() << 8);
            length |= (readOneByte() << 16);
            break;
        case 63:
            length = readOneByte();
            length |= (readOneByte() << 8);
            length |= (readOneByte() << 16);
            length |= (readOneByte() << 24);
            break;
        default:
            length = b >> 2;
            break;
        }

        return length + 1;
    }

    /**
     * Literals are uncompressed data stored directly in the byte stream.
     * 
     * @param length
     *            The number of bytes to read from the underlying stream
     * 
     * @throws IOException
     *             If the first byte cannot be read for any reason other than
     *             end of file, or if the input stream has been closed, or if
     *             some other I/O error occurs.
     * @return True if the decompressed data should be flushed
     */
    private boolean expandLiteral(final int length) throws IOException {
        int bytesRead = in.read(decompressBuf, writeIndex, length);
        count(bytesRead);
        if (length != bytesRead) {
            throw new IOException("Premature end of stream");
        }

        writeIndex += length;
        return (writeIndex >= (2 * this.blockSize));
    }

    /**
     * Copies are references back into previous decompressed data, telling the
     * decompressor to reuse data it has previously decoded. They encode two
     * values: The offset, saying how many bytes back from the current position
     * to read, and the length, how many bytes to copy. Offsets of zero can be
     * encoded, but are not legal; similarly, it is possible to encode
     * backreferences that would go past the end of the block (offset > current
     * decompressed position), which is also nonsensical and thus not allowed.
     * 
     * @param off
     *            The offset from the backward from the end of expanded stream
     * @param length
     *            The number of bytes to copy
     * 
     * @throws IOException
     *             An the offset expands past the front of the decompression
     *             buffer
     * @return True if the decompressed data should be flushed
     */
    private boolean expandCopy(final long off, int length) throws IOException {
        if (off > blockSize) {
            throw new IOException("Offset is larger than block size");
        }
        int offset = (int) off;

        if (offset == 1) {
            byte lastChar = decompressBuf[writeIndex - 1];
            for (int i = 0; i < length; i++) {
                decompressBuf[writeIndex++] = lastChar;
            }
        } else if (length < offset) {
            System.arraycopy(decompressBuf, writeIndex - offset,
                    decompressBuf, writeIndex, length);
            writeIndex += length;
        } else {
            int fullRotations = length / offset;
            int pad = length - (offset * fullRotations);

            while (fullRotations-- != 0) {
                System.arraycopy(decompressBuf, writeIndex - offset,
                        decompressBuf, writeIndex, offset);
                writeIndex += offset;
            }

            if (pad > 0) {
                System.arraycopy(decompressBuf, writeIndex - offset,
                        decompressBuf, writeIndex, pad);

                writeIndex += pad;
            }
        }
        return (writeIndex >= (2 * this.blockSize));
    }

    /**
     * This helper method reads the next byte of data from the input stream. The
     * value byte is returned as an <code>int</code> in the range <code>0</code>
     * to <code>255</code>. If no byte is available because the end of the
     * stream has been reached, an Exception is thrown.
     * 
     * @return The next byte of data
     * @throws IOException
     *             EOF is reached or error reading the stream
     */
    private int readOneByte() throws IOException {
        int b = in.read();
        if (b == -1) {
            throw new IOException("Premature end of stream");
        }
        count(1);
        return b & 0xFF;
    }

    /**
     * The stream starts with the uncompressed length (up to a maximum of 2^32 -
     * 1), stored as a little-endian varint. Varints consist of a series of
     * bytes, where the lower 7 bits are data and the upper bit is set iff there
     * are more bytes to be read. In other words, an uncompressed length of 64
     * would be stored as 0x40, and an uncompressed length of 2097150 (0x1FFFFE)
     * would be stored as 0xFE 0xFF 0x7F.
     * 
     * @return The size of the uncompressed data
     * 
     * @throws IOException
     *             Could not read a byte
     */
    private long readSize() throws IOException {
        int index = 0;
        long sz = 0;
        int b = 0;

        do {
            b = readOneByte();
            sz |= (b & 0x7f) << (index++ * 7);
        } while (0 != (b & 0x80));
        return sz;
    }

    /**
     * Get the uncompressed size of the stream
     * 
     * @return the uncompressed size
     */
    public int getSize() {
        return size;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13964.java