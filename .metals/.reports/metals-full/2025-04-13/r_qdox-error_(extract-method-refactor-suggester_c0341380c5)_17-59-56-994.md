error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3855.java
text:
```scala
s@@treamCompressor.getBytesWrittenForLastEntry(), method,

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


import org.apache.commons.compress.utils.BoundedInputStream;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.zip.Deflater;

/**
 * A zip output stream that is optimized for multi-threaded scatter/gather construction of zip files.
 * <p>
 * The internal data format of the entries used by this class are entirely private to this class
 * and are not part of any public api whatsoever.
 * </p>
 * <p>It is possible to extend this class to support different kinds of backing storage, the default
 * implementation only supports file-based backing.
 * </p>
 * Thread safety: This class supports multiple threads. But the "writeTo" method must be called
 * by the thread that originally created the ZipArchiveEntry.
 *
 * @since 1.10
 */
public class ScatterZipOutputStream implements Closeable {
    private final Queue<CompressedEntry> items = new ConcurrentLinkedQueue<CompressedEntry>();
    private final ScatterGatherBackingStore backingStore;
    private final StreamCompressor streamCompressor;

    private static class CompressedEntry {
        final ZipArchiveEntry entry;
        final long crc;
        final long compressedSize;
        final int method;
        final long size;

        public CompressedEntry(ZipArchiveEntry entry, long crc, long compressedSize, int method, long size) {
            this.entry = entry;
            this.crc = crc;
            this.compressedSize = compressedSize;
            this.method = method;
            this.size = size;
        }

        public ZipArchiveEntry transferToArchiveEntry(){
            entry.setCompressedSize(compressedSize);
            entry.setSize(size);
            entry.setCrc(crc);
            entry.setMethod(method);
            return entry;
        }
    }

    public ScatterZipOutputStream(ScatterGatherBackingStore backingStore,
                                  StreamCompressor streamCompressor) {
        this.backingStore = backingStore;
        this.streamCompressor = streamCompressor;
    }

    /**
     * Add an archive entry to this scatter stream.
     *
     * @param zipArchiveEntry The entry to write
     * @param payload         The content to write for the entry. The caller is responsible for closing this.
     * @param method          The compression method
     * @throws IOException    If writing fails
     */
    public void addArchiveEntry(ZipArchiveEntry zipArchiveEntry, InputStream payload, int method) throws IOException {
        streamCompressor.deflate(payload, method);
        items.add(new CompressedEntry(zipArchiveEntry, streamCompressor.getCrc32(),
                                      streamCompressor.getBytesWritten(), method,
                                      streamCompressor.getBytesRead()));
    }

    /**
     * Write the contents of this scatter stream to a target archive.
     *
     * @param target The archive to receive the contents of this #ScatterZipOutputStream
     * @throws IOException If writing fails
     */
    public void writeTo(ZipArchiveOutputStream target) throws IOException {
        backingStore.closeForWriting();
        InputStream data = backingStore.getInputStream();
        for (CompressedEntry compressedEntry : items) {
            final BoundedInputStream rawStream = new BoundedInputStream(data, compressedEntry.compressedSize);
            target.addRawArchiveEntry(compressedEntry.transferToArchiveEntry(), rawStream);
            rawStream.close();
        }
        data.close();
    }


    /**
     * Closes this stream, freeing all resources involved in the creation of this stream.
     * @throws IOException If closing fails
     */
    public void close() throws IOException {
        backingStore.close();
    }

    /**
     * Create a ScatterZipOutputStream with default compression level that is backed by a file
     *
     * @param file The file to offload compressed data into.
     * @return A  ScatterZipOutputStream that is ready for use.
     * @throws FileNotFoundException
     */
    public static ScatterZipOutputStream fileBased(File file) throws FileNotFoundException {
        return fileBased(file, Deflater.DEFAULT_COMPRESSION);
    }

    /**
     * Create a ScatterZipOutputStream that is backed by a file
     *
     * @param file             The file to offload compressed data into.
     * @param compressionLevel The compression level to use, @see #Deflater
     * @return A  ScatterZipOutputStream that is ready for use.
     * @throws FileNotFoundException
     */
    public static ScatterZipOutputStream fileBased(File file, int compressionLevel) throws FileNotFoundException {
        ScatterGatherBackingStore bs = new FileBasedScatterGatherBackingStore(file);
        StreamCompressor sc = StreamCompressor.create(compressionLevel, bs);
        return new ScatterZipOutputStream(bs, sc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3855.java