error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13420.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13420.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13420.java
text:
```scala
private static final i@@nt bufferSize = 4096;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.commons.compress.archivers.zip;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;

/**
 * Encapsulates a Deflater and crc calculator, handling multiple types of output streams.
 * Currently #ZipEntry.DEFLATED and #ZipEntry.STORED are the only supported compression methods.
 *
 * @since 1.10
 */
public abstract class StreamCompressor implements Closeable {

    /*
     * Apparently Deflater.setInput gets slowed down a lot on Sun JVMs
     * when it gets handed a really big buffer.  See
     * https://issues.apache.org/bugzilla/show_bug.cgi?id=45396
     *
     * Using a buffer size of 8 kB proved to be a good compromise
     */
    private static final int DEFLATER_BLOCK_SIZE = 8192;

    private final Deflater def;

    private final CRC32 crc = new CRC32();

    private long writtenToOutputStreamForLastEntry = 0;
    private long sourcePayloadLength = 0;
    private long totalWrittenToOutputStream = 0;

    private final int bufferSize = 4096;
    private final byte[] outputBuffer = new byte[bufferSize];
    private final byte[] readerBuf = new byte[bufferSize];

    StreamCompressor(Deflater deflater) {
        this.def = deflater;
    }

    /**
     * Create a stream compressor with the given compression level.
     *
     * @param os       The #OutputStream stream to receive output
     * @param deflater The deflater to use
     * @return A stream compressor
     */
    static StreamCompressor create(OutputStream os, Deflater deflater) {
        return new OutputStreamCompressor(deflater, os);
    }

    /**
     * Create a stream compressor with the default compression level.
     *
     * @param os The #OutputStream stream to receive output
     * @return A stream compressor
     */
    static StreamCompressor create(OutputStream os) {
        return create(os, new Deflater(Deflater.DEFAULT_COMPRESSION, true));
    }

    /**
     * Create a stream compressor with the given compression level.
     *
     * @param os       The #DataOutput to receive output
     * @param deflater The deflater to use for the compressor
     * @return A stream compressor
     */
    static StreamCompressor create(DataOutput os, Deflater deflater) {
        return new DataOutputCompressor(deflater, os);
    }

    /**
     * Create a stream compressor with the given compression level.
     *
     * @param compressionLevel The #Deflater compression level
     * @param bs               The #ScatterGatherBackingStore to receive output
     * @return A stream compressor
     */
    public static StreamCompressor create(int compressionLevel, ScatterGatherBackingStore bs) {
        final Deflater deflater = new Deflater(compressionLevel, true);
        return new ScatterGatherBackingStoreCompressor(deflater, bs);
    }

    /**
     * Create a stream compressor with the default compression level.
     *
     * @param bs The #ScatterGatherBackingStore to receive output
     * @return A stream compressor
     */
    public static StreamCompressor create(ScatterGatherBackingStore bs) {
        return create(Deflater.DEFAULT_COMPRESSION, bs);
    }

    /**
     * The crc32 of the last deflated file
     *
     * @return the crc32
     */

    public long getCrc32() {
        return crc.getValue();
    }

    /**
     * Return the number of bytes read from the source stream
     *
     * @return The number of bytes read, never negative
     */
    public long getBytesRead() {
        return sourcePayloadLength;
    }

    /**
     * The number of bytes written to the output for the last entry
     *
     * @return The number of bytes, never negative
     */
    public long getBytesWrittenForLastEntry() {
        return writtenToOutputStreamForLastEntry;
    }

    /**
     * The total number of bytes written to the output for all files
     *
     * @return The number of bytes, never negative
     */
    public long getTotalBytesWritten() {
        return totalWrittenToOutputStream;
    }


    /**
     * Deflate the given source using the supplied compression method
     *
     * @param source The source to compress
     * @param method The #ZipArchiveEntry compression method
     * @throws IOException When failures happen
     */

    public void deflate(InputStream source, int method) throws IOException {
        reset();
        int length;

        while ((length = source.read(readerBuf, 0, readerBuf.length)) >= 0) {
            write(readerBuf, 0, length, method);
        }
        if (method == ZipArchiveEntry.DEFLATED) {
            flushDeflater();
        }
    }

    /**
     * Writes bytes to ZIP entry.
     *
     * @param b      the byte array to write
     * @param offset the start position to write from
     * @param length the number of bytes to write
     * @param method the comrpession method to use
     * @return the number of bytes written to the stream this time
     * @throws IOException on error
     */
    long write(byte[] b, int offset, int length, int method) throws IOException {
        long current = writtenToOutputStreamForLastEntry;
        crc.update(b, offset, length);
        if (method == ZipArchiveEntry.DEFLATED) {
            writeDeflated(b, offset, length);
        } else {
            writeCounted(b, offset, length);
        }
        sourcePayloadLength += length;
        return writtenToOutputStreamForLastEntry - current;
    }


    void reset() {
        crc.reset();
        def.reset();
        sourcePayloadLength = 0;
        writtenToOutputStreamForLastEntry = 0;
    }

    public void close() throws IOException {
        def.end();
    }

    void flushDeflater() throws IOException {
        def.finish();
        while (!def.finished()) {
            deflate();
        }
    }

    private void writeDeflated(byte[] b, int offset, int length)
            throws IOException {
        if (length > 0 && !def.finished()) {
            if (length <= DEFLATER_BLOCK_SIZE) {
                def.setInput(b, offset, length);
                deflateUntilInputIsNeeded();
            } else {
                final int fullblocks = length / DEFLATER_BLOCK_SIZE;
                for (int i = 0; i < fullblocks; i++) {
                    def.setInput(b, offset + i * DEFLATER_BLOCK_SIZE,
                            DEFLATER_BLOCK_SIZE);
                    deflateUntilInputIsNeeded();
                }
                final int done = fullblocks * DEFLATER_BLOCK_SIZE;
                if (done < length) {
                    def.setInput(b, offset + done, length - done);
                    deflateUntilInputIsNeeded();
                }
            }
        }
    }

    private void deflateUntilInputIsNeeded() throws IOException {
        while (!def.needsInput()) {
            deflate();
        }
    }

    void deflate() throws IOException {
        int len = def.deflate(outputBuffer, 0, outputBuffer.length);
        if (len > 0) {
            writeCounted(outputBuffer, 0, len);
        }
    }

    public void writeCounted(byte[] data) throws IOException {
        writeCounted(data, 0, data.length);
    }

    public void writeCounted(byte[] data, int offset, int length) throws IOException {
        writeOut(data, offset, length);
        writtenToOutputStreamForLastEntry += length;
        totalWrittenToOutputStream += length;
    }

    protected abstract void writeOut(byte[] data, int offset, int length) throws IOException;

    private static final class ScatterGatherBackingStoreCompressor extends StreamCompressor {
        private final ScatterGatherBackingStore bs;

        public ScatterGatherBackingStoreCompressor(Deflater deflater, ScatterGatherBackingStore bs) {
            super(deflater);
            this.bs = bs;
        }

        protected final void writeOut(byte[] data, int offset, int length)
                throws IOException {
            bs.writeOut(data, offset, length);
        }
    }

    private static final class OutputStreamCompressor extends StreamCompressor {
        private final OutputStream os;

        public OutputStreamCompressor(Deflater deflater, OutputStream os) {
            super(deflater);
            this.os = os;
        }

        protected final void writeOut(byte[] data, int offset, int length)
                throws IOException {
            os.write(data, offset, length);
        }
    }

    private static final class DataOutputCompressor extends StreamCompressor {
        private final DataOutput raf;

        public DataOutputCompressor(Deflater deflater, DataOutput raf) {
            super(deflater);
            this.raf = raf;
        }

        protected final void writeOut(byte[] data, int offset, int length)
                throws IOException {
            raf.write(data, offset, length);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13420.java