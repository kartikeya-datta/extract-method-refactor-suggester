error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5475.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5475.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5475.java
text:
```scala
public I@@ndexInput clone() {

package org.apache.lucene.store.bytebuffer;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.store.IndexInput;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 */
public class ByteBufferIndexInput extends IndexInput {

    private final static ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0).asReadOnlyBuffer();

    private final ByteBufferFile file;
    private final long length;

    private ByteBuffer currentBuffer;
    private int currentBufferIndex;

    private long bufferStart;
    private final int BUFFER_SIZE;

    private volatile boolean closed = false;

    public ByteBufferIndexInput(String name, ByteBufferFile file) throws IOException {
        super("BBIndexInput(name=" + name + ")");
        this.file = file;
        this.file.incRef();
        this.length = file.getLength();
        this.BUFFER_SIZE = file.bufferSize;

        // make sure that we switch to the
        // first needed buffer lazily
        currentBufferIndex = -1;
        currentBuffer = EMPTY_BUFFER;
    }

    @Override
    public void close() {
        // we protected from double closing the index input since
        // some tests do that...
        if (closed) {
            return;
        }
        closed = true;
        file.decRef();
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public short readShort() throws IOException {
        try {
            currentBuffer.mark();
            return currentBuffer.getShort();
        } catch (BufferUnderflowException e) {
            currentBuffer.reset();
            return super.readShort();
        }
    }

    @Override
    public int readInt() throws IOException {
        try {
            currentBuffer.mark();
            return currentBuffer.getInt();
        } catch (BufferUnderflowException e) {
            currentBuffer.reset();
            return super.readInt();
        }
    }

    @Override
    public long readLong() throws IOException {
        try {
            currentBuffer.mark();
            return currentBuffer.getLong();
        } catch (BufferUnderflowException e) {
            currentBuffer.reset();
            return super.readLong();
        }
    }

    @Override
    public byte readByte() throws IOException {
        if (!currentBuffer.hasRemaining()) {
            currentBufferIndex++;
            switchCurrentBuffer(true);
        }
        return currentBuffer.get();
    }

    @Override
    public void readBytes(byte[] b, int offset, int len) throws IOException {
        while (len > 0) {
            if (!currentBuffer.hasRemaining()) {
                currentBufferIndex++;
                switchCurrentBuffer(true);
            }

            int remainInBuffer = currentBuffer.remaining();
            int bytesToCopy = len < remainInBuffer ? len : remainInBuffer;
            currentBuffer.get(b, offset, bytesToCopy);
            offset += bytesToCopy;
            len -= bytesToCopy;
        }
    }

    @Override
    public long getFilePointer() {
        return currentBufferIndex < 0 ? 0 : bufferStart + currentBuffer.position();
    }

    @Override
    public void seek(long pos) throws IOException {
        if (currentBuffer == EMPTY_BUFFER || pos < bufferStart || pos >= bufferStart + BUFFER_SIZE) {
            currentBufferIndex = (int) (pos / BUFFER_SIZE);
            switchCurrentBuffer(false);
        }
        try {
            currentBuffer.position((int) (pos % BUFFER_SIZE));
            // Grrr, need to wrap in IllegalArgumentException since tests (if not other places)
            // expect an IOException...
        } catch (IllegalArgumentException e) {
            IOException ioException = new IOException("seeking past position");
            ioException.initCause(e);
            throw ioException;
        }
    }

    private void switchCurrentBuffer(boolean enforceEOF) throws IOException {
        if (currentBufferIndex >= file.numBuffers()) {
            // end of file reached, no more buffers left
            if (enforceEOF) {
                throw new EOFException("Read past EOF (resource: " + this + ")");
            } else {
                // Force EOF if a read takes place at this position
                currentBufferIndex--;
                currentBuffer.position(currentBuffer.limit());
            }
        } else {
            ByteBuffer buffer = file.getBuffer(currentBufferIndex);
            // we must duplicate (and make it read only while we are at it) since we need position and such to be independent
            currentBuffer = buffer.asReadOnlyBuffer();
            currentBuffer.position(0);
            bufferStart = (long) BUFFER_SIZE * (long) currentBufferIndex;
            // if we are at the tip, limit the current buffer to only whats available to read
            long buflen = length - bufferStart;
            if (buflen < BUFFER_SIZE) {
                currentBuffer.limit((int) buflen);
            }

            // we need to enforce EOF here as well...
            if (!currentBuffer.hasRemaining()) {
                if (enforceEOF) {
                    throw new EOFException("Read past EOF (resource: " + this + ")");
                } else {
                    // Force EOF if a read takes place at this position
                    currentBufferIndex--;
                    currentBuffer.position(currentBuffer.limit());
                }
            }
        }
    }

    @Override
    public Object clone() {
        ByteBufferIndexInput cloned = (ByteBufferIndexInput) super.clone();
        cloned.file.incRef(); // inc ref on cloned one
        if (currentBuffer != EMPTY_BUFFER) {
            cloned.currentBuffer = currentBuffer.asReadOnlyBuffer();
            cloned.currentBuffer.position(currentBuffer.position());
        }
        return cloned;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5475.java