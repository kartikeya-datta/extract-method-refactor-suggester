error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9472.java
text:
```scala
private final P@@ureJavaCrc32C checksum = new PureJavaCrc32C();

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
import java.io.PushbackInputStream;
import java.util.Arrays;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * CompressorInputStream for the framing Snappy format.
 *
 * <p>Based on the "spec" in the version "Last revised: 2013-10-25"</p>
 *
 * @see <a href="http://code.google.com/p/snappy/source/browse/trunk/framing_format.txt">Snappy framing format description</a>
 * @since 1.7
 */
public class FramedSnappyCompressorInputStream extends CompressorInputStream {
    /**
     * package private for tests only.
     */
    static final long MASK_OFFSET = 0xa282ead8L;

    private static final int STREAM_IDENTIFIER_TYPE = 0xff;
    private static final int COMPRESSED_CHUNK_TYPE = 0;
    private static final int UNCOMPRESSED_CHUNK_TYPE = 1;
    private static final int PADDING_CHUNK_TYPE = 0xfe;
    private static final int MIN_UNSKIPPABLE_TYPE = 2;
    private static final int MAX_UNSKIPPABLE_TYPE = 0x7f;
    private static final int MAX_SKIPPABLE_TYPE = 0xfd;

    private static final byte[] SZ_SIGNATURE = new byte[] {
        (byte) STREAM_IDENTIFIER_TYPE, // tag
        6, 0, 0, // length
        's', 'N', 'a', 'P', 'p', 'Y'
    };

    /** The underlying stream to read compressed data from */
    private final PushbackInputStream in;

    private SnappyCompressorInputStream currentCompressedChunk;

    // used in no-arg read method
    private final byte[] oneByte = new byte[1];

    private boolean endReached, inUncompressedChunk;

    private int uncompressedBytesRemaining;
    private long expectedChecksum = -1;
    private PureJavaCrc32C checksum = new PureJavaCrc32C();

    /**
     * Constructs a new input stream that decompresses snappy-framed-compressed data
     * from the specified input stream.
     * @param in  the InputStream from which to read the compressed data
     */
    public FramedSnappyCompressorInputStream(InputStream in) throws IOException {
        this.in = new PushbackInputStream(in, 1);
        readStreamIdentifier();
    }

    /** {@inheritDoc} */
    @Override
    public int read() throws IOException {
        return read(oneByte, 0, 1) == -1 ? -1 : oneByte[0] & 0xFF;
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (currentCompressedChunk != null) {
            currentCompressedChunk.close();
            currentCompressedChunk = null;
        }
        in.close();
    }

    /** {@inheritDoc} */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = readOnce(b, off, len);
        if (read == -1) {
            readNextBlock();
            if (endReached) {
                return -1;
            }
            read = readOnce(b, off, len);
        }
        return read;
    }

    /** {@inheritDoc} */
    @Override
    public int available() throws IOException {
        if (inUncompressedChunk) {
            return Math.min(uncompressedBytesRemaining,
                            in.available());
        } else if (currentCompressedChunk != null) {
            return currentCompressedChunk.available();
        }
        return 0;
    }

    /**
     * Read from the current chunk into the given array.
     *
     * @return -1 if there is no current chunk or the number of bytes
     * read from the current chunk (which may be -1 if the end of the
     * chunk is reached).
     */
    private int readOnce(byte[] b, int off, int len) throws IOException {
        int read = -1;
        if (inUncompressedChunk) {
            int amount = Math.min(uncompressedBytesRemaining, len);
            if (amount == 0) {
                return -1;
            }
            read = in.read(b, off, amount);
            if (read != -1) {
                uncompressedBytesRemaining -= read;
                count(read);
            }
        } else if (currentCompressedChunk != null) {
            long before = currentCompressedChunk.getBytesRead();
            read = currentCompressedChunk.read(b, off, len);
            if (read == -1) {
                currentCompressedChunk.close();
                currentCompressedChunk = null;
            } else {
                count(currentCompressedChunk.getBytesRead() - before);
            }
        }
        if (read > 0) {
            checksum.update(b, off, read);
        }
        return read;
    }

    private void readNextBlock() throws IOException {
        verifyLastChecksumAndReset();
        inUncompressedChunk = false;
        int type = readOneByte();
        if (type == -1) {
            endReached = true;
        } else if (type == STREAM_IDENTIFIER_TYPE) {
            in.unread(type);
            pushedBackBytes(1);
            readStreamIdentifier();
            readNextBlock();
        } else if (type == PADDING_CHUNK_TYPE
 (type > MAX_UNSKIPPABLE_TYPE && type <= MAX_SKIPPABLE_TYPE)) {
            skipBlock();
            readNextBlock();
        } else if (type >= MIN_UNSKIPPABLE_TYPE && type <= MAX_UNSKIPPABLE_TYPE) {
            throw new IOException("unskippable chunk with type " + type
                                  + " (hex " + Integer.toHexString(type) + ")"
                                  + " detected.");
        } else if (type == UNCOMPRESSED_CHUNK_TYPE) {
            inUncompressedChunk = true;
            uncompressedBytesRemaining = readSize() - 4 /* CRC */;
            expectedChecksum = unmask(readCrc());
        } else if (type == COMPRESSED_CHUNK_TYPE) {
            long size = readSize() - 4 /* CRC */;
            expectedChecksum = unmask(readCrc());
            currentCompressedChunk =
                new SnappyCompressorInputStream(new BoundedInputStream(in, size));
            // constructor reads uncompressed size
            count(currentCompressedChunk.getBytesRead());
        } else {
            // impossible as all potential byte values have been covered
            throw new IOException("unknown chunk type " + type
                                  + " detected.");
        }
    }

    private long readCrc() throws IOException {
        byte[] b = new byte[4];
        int read = IOUtils.readFully(in, b);
        count(read);
        if (read != 4) {
            throw new IOException("premature end of stream");
        }
        long crc = 0;
        for (int i = 0; i < 4; i++) {
            crc |= (b[i] & 0xFFL) << (8 * i);
        }
        return crc;
    }

    static long unmask(long x) {
        // ugly, maybe we should just have used ints and deal with the
        // overflow
        x -= MASK_OFFSET;
        x &= 0xffffFFFFL;
        return ((x >> 17) | (x << 15)) & 0xffffFFFFL;
    }

    private int readSize() throws IOException {
        int b = 0;
        int sz = 0;
        for (int i = 0; i < 3; i++) {
            b = readOneByte();
            if (b == -1) {
                throw new IOException("premature end of stream");
            }
            sz |= (b << (i * 8));
        }
        return sz;
    }

    private void skipBlock() throws IOException {
        int size = readSize();
        long read = IOUtils.skip(in, size);
        count(read);
        if (read != size) {
            throw new IOException("premature end of stream");
        }
    }

    private void readStreamIdentifier() throws IOException {
        byte[] b = new byte[10];
        int read = IOUtils.readFully(in, b);
        count(read);
        if (10 != read || !matches(b, 10)) {
            throw new IOException("Not a framed Snappy stream");
        }
    }

    private int readOneByte() throws IOException {
        int b = in.read();
        if (b != -1) {
            count(1);
            return b & 0xFF;
        }
        return -1;
    }

    private void verifyLastChecksumAndReset() throws IOException {
        if (expectedChecksum >= 0 && expectedChecksum != checksum.getValue()) {
            throw new IOException("Checksum verification failed");
        }
        expectedChecksum = -1;
        checksum.reset();
    }

    /**
     * Checks if the signature matches what is expected for a .sz file.
     *
     * <p>.sz files start with a chunk with tag 0xff and content sNaPpY.</p>
     * 
     * @param signature the bytes to check
     * @param length    the number of bytes to check
     * @return          true if this is a .sz stream, false otherwise
     */
    public static boolean matches(byte[] signature, int length) {

        if (length < SZ_SIGNATURE.length) {
            return false;
        }

        byte[] shortenedSig = signature;
        if (signature.length > SZ_SIGNATURE.length) {
            shortenedSig = new byte[SZ_SIGNATURE.length];
            System.arraycopy(signature, 0, shortenedSig, 0, SZ_SIGNATURE.length);
        }

        return Arrays.equals(shortenedSig, SZ_SIGNATURE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9472.java