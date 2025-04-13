error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4598.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4598.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4598.java
text:
```scala
i@@f (max >= 0 && pos >= max) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is a stream that will only supply bytes up to a certain length - if its
 * position goes above that, it will stop.
 * <p>
 * This is useful to wrap ServletInputStreams. The ServletInputStream will block
 * if you try to read content from it that isn't there, because it doesn't know
 * whether the content hasn't arrived yet or whether the content has finished.
 * So, one of these, initialized with the Content-length sent in the
 * ServletInputStream's header, will stop it blocking, providing it's been sent
 * with a correct content length.
 *
 * @version $Id$
 * @since Commons IO 2.0
 */
public class BoundedInputStream extends InputStream {

    /** the wrapped input stream */
    private final InputStream in;

    /** the max length to provide */
    private final long max;

    /** the number of bytes already returned */
    private long pos = 0;

    /** the marked position */
    private long mark = -1;

    /** flag if close shoud be propagated */
    private boolean propagateClose = true;

    /**
     * Creates a new <code>BoundedInputStream</code> that wraps the given input
     * stream and limits it to a certain size.
     *
     * @param in The wrapped input stream
     * @param size The maximum number of bytes to return
     */
    public BoundedInputStream(InputStream in, long size) {
        // Some badly designed methods - eg the servlet API - overload length
        // such that "-1" means stream finished
        this.max = size;
        this.in = in;
    }

    /**
     * Creates a new <code>BoundedInputStream</code> that wraps the given input
     * stream and is unlimited.
     *
     * @param in The wrapped input stream
     */
    public BoundedInputStream(InputStream in) {
        this(in, -1);
    }

    /**
     * Invokes the delegate's <code>read()</code> method if
     * the current position is less than the limit.
     * @return the byte read or -1 if the end of stream or
     * the limit has been reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        if (max>=0 && pos==max) {
            return -1;
        }
        int result = in.read();
        pos++;
        return result;
    }

    /**
     * Invokes the delegate's <code>read(byte[])</code> method.
     * @param b the buffer to read the bytes into
     * @return the number of bytes read or -1 if the end of stream or
     * the limit has been reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    /**
     * Invokes the delegate's <code>read(byte[], int, int)</code> method.
     * @param b the buffer to read the bytes into
     * @param off The start offset
     * @param len The number of bytes to read
     * @return the number of bytes read or -1 if the end of stream or
     * the limit has been reached.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (max>=0 && pos>=max) {
            return -1;
        }
        long maxRead = max>=0 ? Math.min(len, max-pos) : len;
        int bytesRead = in.read(b, off, (int)maxRead);

        if (bytesRead==-1) {
            return -1;
        }

        pos+=bytesRead;
        return bytesRead;
    }

    /**
     * Invokes the delegate's <code>skip(long)</code> method.
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped
     * @throws IOException if an I/O error occurs
     */
    @Override
    public long skip(long n) throws IOException {
        long toSkip = max>=0 ? Math.min(n, max-pos) : n;
        long skippedBytes = in.skip(toSkip);
        pos+=skippedBytes;
        return skippedBytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int available() throws IOException {
        if (max>=0 && pos>=max) {
            return 0;
        }
        return in.available();
    }

    /**
     * Invokes the delegate's <code>toString()</code> method.
     * @return the delegate's <code>toString()</code>
     */
    @Override
    public String toString() {
        return in.toString();
    }

    /**
     * Invokes the delegate's <code>close()</code> method
     * if {@link #isPropagateClose()} is <code>true</code>.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        if (propagateClose) {
            in.close();
        }
    }

    /**
     * Invokes the delegate's <code>reset()</code> method.
     * @throws IOException if an I/O error occurs
     */
    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        pos = mark;
    }

    /**
     * Invokes the delegate's <code>mark(int)</code> method.
     * @param readlimit read ahead limit
     */
    @Override
    public synchronized void mark(int readlimit) {
        in.mark(readlimit);
        mark = pos;
    }

    /**
     * Invokes the delegate's <code>markSupported()</code> method.
     * @return true if mark is supported, otherwise false
     */
    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    /**
     * Indicates whether the {@link #close()} method
     * should propagate to the underling {@link InputStream}.
     *
     * @return <code>true</code> if calling {@link #close()}
     * propagates to the <code>close()</code> method of the
     * underlying stream or <code>false</code> if it does not.
     */
    public boolean isPropagateClose() {
        return propagateClose;
    }

    /**
     * Set whether the {@link #close()} method
     * should propagate to the underling {@link InputStream}.
     *
     * @param propagateClose <code>true</code> if calling
     * {@link #close()} propagates to the <code>close()</code>
     * method of the underlying stream or
     * <code>false</code> if it does not.
     */
    public void setPropagateClose(boolean propagateClose) {
        this.propagateClose = propagateClose;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4598.java