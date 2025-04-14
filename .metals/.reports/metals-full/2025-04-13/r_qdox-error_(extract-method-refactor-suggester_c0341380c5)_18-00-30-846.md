error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9436.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9436.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9436.java
text:
```scala
public b@@oolean canReadEntryData(ArchiveEntry ae) {

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

import java.io.IOException;
import java.io.InputStream;

/**
 * Archive input streams <b>MUST</b> override the 
 * {@link #read(byte[], int, int)} - or {@link #read()} -
 * method so that reading from the stream generates EOF for the end of
 * data in each entry as well as at the end of the file proper.
 * <p>
 * The {@link #getNextEntry()} method is used to reset the input stream
 * ready for reading the data from the next entry.
 * <p>
 * The input stream classes must also implement a method with the signature:
 * <pre>
 * public static boolean matches(byte[] signature, int length)
 * </pre>
 * which is used by the {@link ArchiveStreamFactory} to autodetect
 * the archive type from the first few bytes of a stream. 
 */
public abstract class ArchiveInputStream extends InputStream {

    private byte[] SINGLE = new byte[1];
    private static final int BYTE_MASK = 0xFF;
    
    /** holds the number of bytes read in this stream */
    private long bytesRead = 0;

    /**
     * Returns the next Archive Entry in this Stream.
     *
     * @return the next entry,
     *         or <code>null</code> if there are no more entries
     * @throws IOException if the next entry could not be read
     */
    public abstract ArchiveEntry getNextEntry() throws IOException;

    /*
     * Note that subclasses also implement specific get() methods which
     * return the appropriate class without need for a cast.
     * See SVN revision r743259
     * @return
     * @throws IOException
     */
    // public abstract XXXArchiveEntry getNextXXXEntry() throws IOException;

    /**
     * Reads a byte of data. This method will block until enough input is
     * available.
     * 
     * Simply calls the {@link #read(byte[], int, int)} method.
     * 
     * MUST be overridden if the {@link #read(byte[], int, int)} method
     * is not overridden; may be overridden otherwise.
     * 
     * @return the byte read, or -1 if end of input is reached
     * @throws IOException
     *             if an I/O error has occurred
     */
    public int read() throws IOException {
        int num = read(SINGLE, 0, 1);
        return num == -1 ? -1 : SINGLE[0] & BYTE_MASK;
    }
    
    /**
     * Increments the counter of already read bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     * 
     * @param read the number of bytes read
     */
    protected void count(int read) {
        count((long) read);
    }

    /**
     * Increments the counter of already read bytes.
     * Doesn't increment if the EOF has been hit (read == -1)
     * 
     * @param read the number of bytes read
     * @since Apache Commons Compress 1.1
     */
    protected void count(long read) {
        if (read != -1) {
            bytesRead = bytesRead + read;
        }
    }
    
    /**
     * Decrements the counter of already read bytes.
     * 
     * @param read the number of bytes pushed back.
     * @since Apache Commons Compress 1.1
     */
    protected void pushedBackBytes(long pushedBack) {
        bytesRead -= pushedBack;
    }
    
    /**
     * Returns the current number of bytes read from this stream.
     * @return the number of read bytes
     * @deprecated this method may yield wrong results for large
     * archives, use #getBytesRead instead
     */
    public int getCount() {
        return (int) bytesRead;
    }

    /**
     * Returns the current number of bytes read from this stream.
     * @return the number of read bytes
     * @since Apache Commons Compress 1.1
     */
    public long getBytesRead() {
        return bytesRead;
    }

    /**
     * Whether this stream is able to read the given entry.
     *
     * <p>Some archive formats support variants or details that are
     * not supported (yet).</p>
     *
     * <p>This implementation always returns true.
     *
     * @since Apache Commons Compress 1.1
     */
    public boolean canRead(ArchiveEntry ae) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9436.java