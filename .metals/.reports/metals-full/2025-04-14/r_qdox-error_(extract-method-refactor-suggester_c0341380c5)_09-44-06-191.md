error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9437.java
text:
```scala
public b@@oolean canWriteEntryData(ArchiveEntry ae) {

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
package org.apache.commons.compress.archivers;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Archive output stream implementations are expected to override the
 * {@link #write(byte[], int, int)} method to improve performance.
 * They should also override {@link #close()} to ensure that any necessary
 * trailers are added.
 * 
 * <p>
 * The normal sequence of calls for working with ArchiveOutputStreams is:
 * + create ArchiveOutputStream object
 * + write SFX header (optional, Zip only)
 * + repeat as needed:
 *      - putArchiveEntry() (writes entry header)
 *      - write() (writes entry data)
 *      - closeArchiveEntry() (closes entry)
 * + finish() (ends the addition of entries)
 * + write additional data if format supports it (optional)
 * + close()
 * </p>
 * 
 * <p>
 * Example usage:<br/>
 * TBA
 * </p>
 */
public abstract class ArchiveOutputStream extends OutputStream {
    
    /** Temporary buffer used for the {@link #write(int)} method */
    private final byte[] oneByte = new byte[1];
    static final int BYTE_MASK = 0xFF;

    /** holds the number of bytes written to this stream */
    private long bytesWritten = 0;
    // Methods specific to ArchiveOutputStream
    
    /**
     * Writes the headers for an archive entry to the output stream.
     * The caller must then write the content to the stream and call
     * {@link #closeArchiveEntry()} to complete the process.
     * 
     * @param entry describes the entry
     * @throws IOException
     */
    public abstract void putArchiveEntry(ArchiveEntry entry) throws IOException;

    /**
     * Closes the archive entry, writing any trailer information that may
     * be required.
     * @throws IOException
     */
    public abstract void closeArchiveEntry() throws IOException;
    
    /**
     * Finishes the addition of entries to this stream, without closing it.
     * Additional data can be written, if the format supports it.
     * 
     * The finish() method throws an Exception if the user forgets to close the entry
     * .
     * @throws IOException
     */
    public abstract void finish() throws IOException;

    /**
     * Create an archive entry using the inputFile and entryName provided.
     * 
     * @param inputFile
     * @param entryName 
     * @return the ArchiveEntry set up with details from the file
     * 
     * @throws IOException
     */
    public abstract ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException;
    
    // Generic implementations of OutputStream methods that may be useful to sub-classes
    
    /**
     * Writes a byte to the current archive entry.
     *
     * This method simply calls write( byte[], 0, 1 ).
     *
     * MUST be overridden if the {@link #write(byte[], int, int)} method
     * is not overridden; may be overridden otherwise.
     * 
     * @param b The byte to be written.
     * @throws IOException on error
     */
    public void write(int b) throws IOException {
        oneByte[0] = (byte) (b & BYTE_MASK);
        write(oneByte, 0, 1);
    }

    /**
     * Increments the counter of already written bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     * 
     * @param written the number of bytes written
     */
    protected void count(int written) {
        count((long) written);
    }

    /**
     * Increments the counter of already written bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     * 
     * @param written the number of bytes written
     * @since Apache Commons Compress 1.1
     */
    protected void count(long written) {
        if (written != -1) {
            bytesWritten = bytesWritten + written;
        }
    }
    
    /**
     * Returns the current number of bytes written to this stream.
     * @return the number of written bytes
     * @deprecated this method may yield wrong results for large
     * archives, use #getBytesWritten instead
     */
    public int getCount() {
        return (int) bytesWritten;
    }

    /**
     * Returns the current number of bytes written to this stream.
     * @return the number of written bytes
     * @since Apache Commons Compress 1.1
     */
    public long getBytesWritten() {
        return bytesWritten;
    }

    /**
     * Whether this stream is able to write the given entry.
     *
     * <p>Some archive formats support variants or details that are
     * not supported (yet).</p>
     *
     * <p>This implementation always returns true.
     * @since Apache Commons Compress 1.1
     */
    public boolean canWrite(ArchiveEntry ae) {
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9437.java