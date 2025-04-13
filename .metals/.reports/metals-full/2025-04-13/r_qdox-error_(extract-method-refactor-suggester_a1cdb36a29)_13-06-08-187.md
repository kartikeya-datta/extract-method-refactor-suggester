error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13288.java
text:
```scala
r@@eturn collection.size() > 0;

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
package org.apache.commons.collections.iterators;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections.ResettableIterator;

/**
 * An Iterator that restarts when it reaches the end.
 * <p>
 * The iterator will loop continuously around the provided elements, unless 
 * there are no elements in the collection to begin with, or all the elements
 * have been {@link #remove removed}.
 * <p>
 * Concurrent modifications are not directly supported, and for most collection
 * implementations will throw a ConcurrentModificationException. 
 *
 * @since 3.0
 * @version $Id$
 */
public class LoopingIterator<E> implements ResettableIterator<E> {
    
    /** The collection to base the iterator on */
    private Collection<? extends E> collection;
    /** The current iterator */
    private Iterator<? extends E> iterator;

    /**
     * Constructor that wraps a collection.
     * <p>
     * There is no way to reset an Iterator instance without recreating it from
     * the original source, so the Collection must be passed in.
     * 
     * @param coll  the collection to wrap
     * @throws NullPointerException if the collection is null
     */
    public LoopingIterator(Collection<? extends E> coll) {
        if (coll == null) {
            throw new NullPointerException("The collection must not be null");
        }
        collection = coll;
        reset();
    }

    /** 
     * Has the iterator any more elements.
     * <p>
     * Returns false only if the collection originally had zero elements, or
     * all the elements have been {@link #remove removed}.
     * 
     * @return <code>true</code> if there are more elements
     */
    public boolean hasNext() {
        return (collection.size() > 0);
    }

    /**
     * Returns the next object in the collection.
     * <p>
     * If at the end of the collection, return the first element.
     * 
     * @return the next object
     * @throws NoSuchElementException if there are no elements
     *         at all.  Use {@link #hasNext} to avoid this error.
     */
    public E next() {
        if (collection.size() == 0) {
            throw new NoSuchElementException("There are no elements for this iterator to loop on");
        }
        if (iterator.hasNext() == false) {
            reset();
        }
        return iterator.next();
    }

    /**
     * Removes the previously retrieved item from the underlying collection.
     * <p>
     * This feature is only supported if the underlying collection's 
     * {@link Collection#iterator iterator} method returns an implementation 
     * that supports it.
     * <p>
     * This method can only be called after at least one {@link #next} method call.
     * After a removal, the remove method may not be called again until another
     * next has been performed. If the {@link #reset} is called, then remove may
     * not be called until {@link #next} is called again.
     */
    public void remove() {
        iterator.remove();
    }

    /**
     * Resets the iterator back to the start of the collection.
     */
    public void reset() {
        iterator = collection.iterator();
    }

    /**
     * Gets the size of the collection underlying the iterator.
     * 
     * @return the current collection size
     */
    public int size() {
        return collection.size();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13288.java