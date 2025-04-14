error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11992.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11992.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11992.java
text:
```scala
final E@@[] values = (E[]) queue.toArray(); // NOPMD - false positive for generics

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
package org.apache.commons.collections4.queue;

import java.util.Queue;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollection;

/**
 * Decorates another {@link Queue} to transform objects that are added.
 * <p>
 * The add/offer methods are affected by this class.
 * Thus objects must be removed or searched for using their transformed form.
 * For example, if the transformation converts Strings to Integers, you must
 * use the Integer form to remove objects.
 *
 * @since 4.0
 * @version $Id$
 */
public class TransformedQueue<E> extends TransformedCollection<E> implements Queue<E> {

    /** Serialization version */
    private static final long serialVersionUID = -7901091318986132033L;

    /**
     * Factory method to create a transforming queue.
     * <p>
     * If there are any elements already in the queue being decorated, they
     * are NOT transformed.
     * Contrast this with {@link #transformedQueue(Queue, Transformer)}.
     * 
     * @param <E> the type of the elements in the queue
     * @param queue  the queue to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed Queue
     * @throws IllegalArgumentException if queue or transformer is null
     */
    public static <E> TransformedQueue<E> transformingQueue(final Queue<E> queue,
                                                            final Transformer<? super E, ? extends E> transformer) {
        return new TransformedQueue<E>(queue, transformer);
    }
    
    /**
     * Factory method to create a transforming queue that will transform
     * existing contents of the specified queue.
     * <p>
     * If there are any elements already in the queue being decorated, they
     * will be transformed by this method.
     * Contrast this with {@link #transformingQueue(Queue, Transformer)}.
     * 
     * @param <E> the type of the elements in the queue
     * @param queue  the queue to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed Queue
     * @throws IllegalArgumentException if queue or transformer is null
     * @since 4.0
     */
    public static <E> TransformedQueue<E> transformedQueue(final Queue<E> queue,
                                                           final Transformer<? super E, ? extends E> transformer) {
        // throws IAE if queue or transformer is null
        final TransformedQueue<E> decorated = new TransformedQueue<E>(queue, transformer); 
        if (queue.size() > 0) {
            @SuppressWarnings("unchecked") // queue is type <E>
            final E[] values = (E[]) queue.toArray();
            queue.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.transform(value));
            }
        }
        return decorated;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the queue being decorated, they
     * are NOT transformed.
     * 
     * @param queue  the queue to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @throws IllegalArgumentException if queue or transformer is null
     */
    protected TransformedQueue(final Queue<E> queue, final Transformer<? super E, ? extends E> transformer) {
        super(queue, transformer);
    }

    /**
     * Gets the decorated queue.
     * 
     * @return the decorated queue
     */
    protected Queue<E> getQueue() {
        return (Queue<E>) collection;
    }

    //-----------------------------------------------------------------------

    public boolean offer(final E obj) {
        return getQueue().offer(transform(obj));
    }

    public E poll() {
        return getQueue().poll();
    }

    public E peek() {
        return getQueue().peek();
    }

    public E element() {
        return getQueue().element();
    }
    
    public E remove() {
        return getQueue().remove();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11992.java