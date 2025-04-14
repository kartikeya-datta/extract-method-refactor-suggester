error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13277.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13277.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13277.java
text:
```scala
r@@eturn size() == maxSize();

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
import java.util.Iterator;

import org.apache.commons.collections.BoundedCollection;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;

/**
 * Decorates another {@link Buffer} to ensure a fixed maximum size.
 * <p>
 * Note: This class should only be used if you need to add bounded
 * behaviour to another buffer. If you just want a bounded buffer then
 * you should use {@link BoundedFifoBuffer} or {@link CircularFifoBuffer}.
 * <p>
 * The decoration methods allow you to specify a timeout value.
 * This alters the behaviour of the add methods when the buffer is full.
 * Normally, when the buffer is full, the add method will throw an exception.
 * With a timeout, the add methods will wait for up to the timeout period
 * to try and add the elements.
 *
 * @since 3.2
 * @version $Id$
 */
public class BoundedBuffer<E> extends SynchronizedBuffer<E> implements BoundedCollection<E> {

    /** The serialization version. */
    private static final long serialVersionUID = 1536432911093974264L;

    /** The maximum size. */
    private final int maximumSize;
    /** The timeout milliseconds. */
    private final long timeout;

    /**
     * Factory method to create a bounded buffer.
     * <p>
     * When the buffer is full, it will immediately throw a
     * <code>BufferOverflowException</code> on calling {@link #add(Object)}.
     *
     * @param <E> the type of the elements in the buffer
     * @param buffer  the buffer to decorate, must not be null
     * @param maximumSize  the maximum size, must be size one or greater
     * @return a new bounded buffer
     * @throws IllegalArgumentException if the buffer is null
     * @throws IllegalArgumentException if the maximum size is zero or less
     */
    public static <E> BoundedBuffer<E> boundedBuffer(Buffer<E> buffer, int maximumSize) {
        return new BoundedBuffer<E>(buffer, maximumSize, 0L);
    }

    /**
     * Factory method to create a bounded buffer that blocks for a maximum
     * amount of time.
     *
     * @param <E> the type of the elements in the buffer
     * @param buffer  the buffer to decorate, must not be null
     * @param maximumSize  the maximum size, must be size one or greater
     * @param timeout  the maximum amount of time to wait in milliseconds
     * @return a new bounded buffer
     * @throws IllegalArgumentException if the buffer is null
     * @throws IllegalArgumentException if the maximum size is zero or less
     */
    public static <E> BoundedBuffer<E> boundedBuffer(Buffer<E> buffer, int maximumSize, long timeout) {
        return new BoundedBuffer<E>(buffer, maximumSize, timeout);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies) another buffer, making it bounded
     * waiting only up to a maximum amount of time.
     *
     * @param buffer  the buffer to wrap, must not be null
     * @param maximumSize  the maximum size, must be size one or greater
     * @param timeout  the maximum amount of time to wait
     * @throws IllegalArgumentException if the buffer is null
     * @throws IllegalArgumentException if the maximum size is zero or less
     */
    protected BoundedBuffer(Buffer<E> buffer, int maximumSize, long timeout) {
        super(buffer);
        if (maximumSize < 1) {
            throw new IllegalArgumentException();
        }
        this.maximumSize = maximumSize;
        this.timeout = timeout;
    }

    //-----------------------------------------------------------------------
    @Override
    public E remove() {
        synchronized (lock) {
            E returnValue = decorated().remove();
            lock.notifyAll();
            return returnValue;
        }
    }

    @Override
    public boolean add(E o) {
        synchronized (lock) {
            timeoutWait(1);
            return decorated().add(o);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        synchronized (lock) {
            timeoutWait(c.size());
            return decorated().addAll(c);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new NotifyingIterator(collection.iterator());
    }

    /**
     * Waits up to the specified timeout period that the given number of additions
     * can be made to the buffer.
     *
     * @param nAdditions the number of additions
     * @throws BufferOverflowException if the number of additions would overflow the buffer,
     * or the timeout has expired
     */
    private void timeoutWait(final int nAdditions) {
        // method synchronized by callers
        if (nAdditions > maximumSize) {
            throw new BufferOverflowException(
                    "Buffer size cannot exceed " + maximumSize);
        }
        if (timeout <= 0) {
            // no wait period (immediate timeout)
            if (decorated().size() + nAdditions > maximumSize) {
                throw new BufferOverflowException(
                        "Buffer size cannot exceed " + maximumSize);
            }
            return;
        }
        final long expiration = System.currentTimeMillis() + timeout;
        long timeLeft = expiration - System.currentTimeMillis();
        while (timeLeft > 0 && decorated().size() + nAdditions > maximumSize) {
            try {
                lock.wait(timeLeft);
                timeLeft = expiration - System.currentTimeMillis();
            } catch (InterruptedException ex) {
                PrintWriter out = new PrintWriter(new StringWriter());
                ex.printStackTrace(out);
                throw new BufferUnderflowException(
                    "Caused by InterruptedException: " + out.toString());
            }
        }
        if (decorated().size() + nAdditions > maximumSize) {
            throw new BufferOverflowException("Timeout expired");
        }
    }

    public boolean isFull() {
        // size() is synchronized
        return (size() == maxSize());
    }

    public int maxSize() {
        return maximumSize;
    }

    //-----------------------------------------------------------------------
    /**
     * BoundedBuffer iterator.
     */
    private class NotifyingIterator extends AbstractIteratorDecorator<E> {

        /**
         * Create a new {@link NotifyingIterator}.
         * 
         * @param it the decorated {@link Iterator}
         */
        public NotifyingIterator(Iterator<E> it) {
            super(it);
        }

        @Override
        public void remove() {
            synchronized (lock) {
                iterator.remove();
                lock.notifyAll();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13277.java