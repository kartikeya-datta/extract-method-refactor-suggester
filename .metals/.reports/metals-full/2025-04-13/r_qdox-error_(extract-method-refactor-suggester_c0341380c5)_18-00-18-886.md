error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4967.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4967.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4967.java
text:
```scala
protected final M@@ap<String, ByteBufferFile> files = new ConcurrentHashMap<String, ByteBufferFile>();

/*
 * Licensed to Elastic Search and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Elastic Search licenses this
 * file to you under the Apache License, Version 2.0 (the
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

package org.apache.lucene.store.bytebuffer;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.SingleInstanceLockFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A memory based directory that uses {@link java.nio.ByteBuffer} in order to store the directory content.
 *
 * <p>The benefit of using {@link java.nio.ByteBuffer} is the fact that it can be stored in "native" memory
 * outside of the JVM heap, thus not incurring the GC overhead of large in memory index.
 *
 * <p>Each "file" is segmented into one or more byte buffers.
 *
 * <p>If constructed with {@link ByteBufferAllocator}, it allows to control the allocation and release of
 * byte buffer. For example, custom implementations can include caching of byte buffers.
 *
 * @author kimchy (shay.banon)
 */
public class ByteBufferDirectory extends Directory {

    private final Map<String, ByteBufferFile> files = new ConcurrentHashMap<String, ByteBufferFile>();

    private final ByteBufferAllocator allocator;

    private final boolean internalAllocator;

    /**
     * Constructs a new directory using {@link PlainByteBufferAllocator}.
     */
    public ByteBufferDirectory() {
        this.allocator = new PlainByteBufferAllocator(false, 1024, 1024 * 10);
        this.internalAllocator = true;
        try {
            setLockFactory(new SingleInstanceLockFactory());
        } catch (IOException e) {
            // will not happen
        }
    }

    /**
     * Constructs a new byte buffer directory with a custom allocator.
     */
    public ByteBufferDirectory(ByteBufferAllocator allocator) {
        this.allocator = allocator;
        this.internalAllocator = false;
        try {
            setLockFactory(new SingleInstanceLockFactory());
        } catch (IOException e) {
            // will not happen
        }
    }

    public void sync(Collection<String> names) throws IOException {
        // nothing to do here
    }

    @Override
    public String[] listAll() throws IOException {
        return files.keySet().toArray(new String[0]);
    }

    @Override
    public boolean fileExists(String name) throws IOException {
        return files.containsKey(name);
    }

    @Override
    public long fileModified(String name) throws IOException {
        ByteBufferFile file = files.get(name);
        if (file == null)
            throw new FileNotFoundException(name);
        return file.getLastModified();
    }

    @Override
    public void touchFile(String name) throws IOException {
        ByteBufferFile file = files.get(name);
        if (file == null)
            throw new FileNotFoundException(name);

        long ts2, ts1 = System.currentTimeMillis();
        do {
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ie) {
                // In 3.0 we will change this to throw
                // InterruptedException instead
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
            ts2 = System.currentTimeMillis();
        } while (ts1 == ts2);

        file.setLastModified(ts2);
    }

    @Override
    public void deleteFile(String name) throws IOException {
        ByteBufferFile file = files.remove(name);
        if (file == null)
            throw new FileNotFoundException(name);
        file.clean();
    }

    @Override
    public long fileLength(String name) throws IOException {
        ByteBufferFile file = files.get(name);
        if (file == null)
            throw new FileNotFoundException(name);
        return file.getLength();
    }

    @Override
    public IndexOutput createOutput(String name) throws IOException {
        ByteBufferAllocator.Type allocatorType = ByteBufferAllocator.Type.LARGE;
        if (name.contains("segments") || name.endsWith(".del")) {
            allocatorType = ByteBufferAllocator.Type.SMALL;
        }
        ByteBufferFile file = new ByteBufferFile(this, allocator.sizeInBytes(allocatorType));
        ByteBufferFile existing = files.put(name, file);
        if (existing != null) {
            existing.clean();
        }
        return new ByteBufferIndexOutput(allocator, allocatorType, file);
    }

    @Override
    public IndexInput openInput(String name) throws IOException {
        ByteBufferFile file = files.get(name);
        if (file == null)
            throw new FileNotFoundException(name);
        return new ByteBufferIndexInput(file);
    }

    @Override
    public void close() throws IOException {
        String[] files = listAll();
        for (String file : files) {
            deleteFile(file);
        }
        if (internalAllocator) {
            allocator.close();
        }
    }

    void releaseBuffer(ByteBuffer byteBuffer) {
        allocator.release(byteBuffer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4967.java