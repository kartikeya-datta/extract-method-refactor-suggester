error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13276.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13276.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13276.java
text:
```scala
t@@his.timeout = timeoutMillis < 0 ? 0 : timeoutMillis;

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
package org.apache.commons.collections.buffer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;

/**
 * Decorates another {@link Buffer} to make {@link #get()} and
 * {@link #remove()} block when the <code>Buffer</code> is empty.
 * <p>
 * If either <code>get</code> or <code>remove</code> is called on an empty
 * {@link Buffer}, the calling thread waits for notification that
 * an <code>add</code> or <code>addAll</code> operation has completed.
 * <p>
 * When one or more entries are added to an empty {@link Buffer},
 * all threads blocked in <code>get</code> or <code>remove</code> are notified.
 * There is no guarantee that concurrent blocked <code>get</code> or
 * <code>remove</code> requests will be "unblocked" and receive data in the
 * order that they arrive.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * This class contains an extra field in 3.2, however the serialization
 * specification will handle this gracefully.
 *
 * @param <E> the type of the elements in the buffer
 * @version $Id$
 * @since 3.0
 */
public class BlockingBuffer<E> extends SynchronizedBuffer<E> {

    /** Serialization version. */
    private static final long serialVersionUID = 1719328905017860541L;
    /** The timeout value in milliseconds. */
    private final long timeout;

    /**
     * Factory method to create a blocking buffer.
     *
     * @param <E> the type of the elements in the buffer
     * @param buffer the buffer to decorate, must not be null
     * @return a new blocking Buffer
     * @throws IllegalArgumentException if buffer is null
     */
    public static <E> BlockingBuffer<E> blockingBuffer(Buffer<E> buffer) {
        return new BlockingBuffer<E>(buffer);
    }

    /**
     * Factory method to create a blocking buffer with a timeout value.
     *
     * @param <E> the type of the elements in the buffer
     * @param buffer  the buffer to decorate, must not be null
     * @param timeoutMillis  the timeout value in milliseconds, zero or less for no timeout
     * @return a new blocking buffer
     * @throws IllegalArgumentException if the buffer is null
     * @since 3.2
     */
    public static <E> BlockingBuffer<E> blockingBuffer(Buffer<E> buffer, long timeoutMillis) {
        return new BlockingBuffer<E>(buffer, timeoutMillis);
    }

    //-----------------------------------------------------------------------    
    /**
     * Constructor that wraps (not copies).
     *
     * @param buffer the buffer to decorate, must not be null
     * @throws IllegalArgumentException if the buffer is null
     */
    protected BlockingBuffer(Buffer<E> buffer) {
        super(buffer);
        this.timeout = 0;
    }

    /**
     * Constructor that wraps (not copies).
     *
     * @param buffer  the buffer to decorate, must not be null
     * @param timeoutMillis  the timeout value in milliseconds, zero or less for no timeout
     * @throws IllegalArgumentException if the buffer is null
     * @since 3.2
     */
    protected BlockingBuffer(Buffer<E> buffer, long timeoutMillis) {
        super(buffer);
        this.timeout = (timeoutMillis < 0 ? 0 : timeoutMillis);
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean add(E o) {
        synchronized (lock) {
            boolean result = collection.add(o);
            lock.notifyAll();
            return result;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        synchronized (lock) {
            boolean result = collection.addAll(c);
            lock.notifyAll();
            return result;
        }
    }

    /**
     * Gets the next value from the buffer, waiting until an object is
     * added if the buffer is empty. This method uses the default timeout
     * set in the constructor.
     *
     * @throws BufferUnderflowException if an interrupt is received
     * {@inheritDoc}
     */
    @Override
    public E get() {
        synchronized (lock) {
            while (collection.isEmpty()) {
                try {
                    if (timeout <= 0) {
                        lock.wait();
                    } else {
                        return get(timeout);
                    }
                } catch (InterruptedException e) {
                    PrintWriter out = new PrintWriter(new StringWriter());
                    e.printStackTrace(out);
                    throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
                }
            }
            return decorated().get();
        }
    }

    /**
     * Gets the next value from the buffer, waiting until an object is
     * added for up to the specified timeout value if the buffer is empty.
     *
     * @param timeout  the timeout value in milliseconds
     * @return the next object in the buffer
     * @throws BufferUnderflowException if an interrupt is received
     * @throws BufferUnderflowException if the timeout expires
     * @since 3.2
     */
    public E get(final long timeout) {
        synchronized (lock) {
            final long expiration = System.currentTimeMillis() + timeout;
            long timeLeft = expiration - System.currentTimeMillis();
            while (timeLeft > 0 && collection.isEmpty()) {
                try {
                    lock.wait(timeLeft);
                    timeLeft = expiration - System.currentTimeMillis();
                } catch(InterruptedException e) {
                    PrintWriter out = new PrintWriter(new StringWriter());
                    e.printStackTrace(out);
                    throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
                }
            }
            if (collection.isEmpty()) {
                throw new BufferUnderflowException("Timeout expired");
            }
            return decorated().get();
        }
    }

    /**
     * Removes the next value from the buffer, waiting until an object is
     * added if the buffer is empty. This method uses the default timeout
     * set in the constructor.
     *
     * @throws BufferUnderflowException if an interrupt is received
     * {@inheritDoc}
     */
    @Override
    public E remove() {
        synchronized (lock) {
            while (collection.isEmpty()) {
                try {
                    if (timeout <= 0) {
                        lock.wait();
                    } else {
                        return remove(timeout);
                    }
                } catch (InterruptedException e) {
                    PrintWriter out = new PrintWriter(new StringWriter());
                    e.printStackTrace(out);
                    throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
                }
            }
            return decorated().remove();
        }
    }

    /**
     * Removes the next value from the buffer, waiting until an object is
     * added for up to the specified timeout value if the buffer is empty.
     *
     * @param timeout  the timeout value in milliseconds
     * @return the next object in the buffer, which is also removed
     * @throws BufferUnderflowException if an interrupt is received
     * @throws BufferUnderflowException if the timeout expires
     * @since 3.2
     */
    public E remove(final long timeout) {
        synchronized (lock) {
            final long expiration = System.currentTimeMillis() + timeout;
            long timeLeft = expiration - System.currentTimeMillis();
            while (timeLeft > 0 && collection.isEmpty()) {
                try {
                    lock.wait(timeLeft);
                    timeLeft = expiration - System.currentTimeMillis();
                } catch(InterruptedException e) {
                    PrintWriter out = new PrintWriter(new StringWriter());
                    e.printStackTrace(out);
                    throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
                }
            }
            if (collection.isEmpty()) {
                throw new BufferUnderflowException("Timeout expired");
            }
            return decorated().remove();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13276.java