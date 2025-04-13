error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2441.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2441.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 882
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2441.java
text:
```scala
public class SingletonListIterator<E> implements ResettableListIterator<E> {

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
p@@ackage org.apache.commons.collections4.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.ResettableListIterator;

/**
 * <code>SingletonIterator</code> is an {@link ListIterator} over a single
 * object instance.
 *
 * @since 2.1
 * @version $Id$
 */
public class SingletonListIterator<E> implements ListIterator<E>, ResettableListIterator<E> {

    private boolean beforeFirst = true;
    private boolean nextCalled = false;
    private boolean removed = false;
    private E object;

    /**
     * Constructs a new <code>SingletonListIterator</code>.
     *
     * @param object  the single object to return from the iterator
     */
    public SingletonListIterator(final E object) {
        super();
        this.object = object;
    }

    /**
     * Is another object available from the iterator?
     * <p>
     * This returns true if the single object hasn't been returned yet.
     *
     * @return true if the single object hasn't been returned yet
     */
    public boolean hasNext() {
        return beforeFirst && !removed;
    }

    /**
     * Is a previous object available from the iterator?
     * <p>
     * This returns true if the single object has been returned.
     *
     * @return true if the single object has been returned
     */
    public boolean hasPrevious() {
        return !beforeFirst && !removed;
    }

    /**
     * Returns the index of the element that would be returned by a subsequent
     * call to <tt>next</tt>.
     *
     * @return 0 or 1 depending on current state.
     */
    public int nextIndex() {
        return beforeFirst ? 0 : 1;
    }

    /**
     * Returns the index of the element that would be returned by a subsequent
     * call to <tt>previous</tt>. A return value of -1 indicates that the iterator is currently at
     * the start.
     *
     * @return 0 or -1 depending on current state.
     */
    public int previousIndex() {
        return beforeFirst ? -1 : 0;
    }

    /**
     * Get the next object from the iterator.
     * <p>
     * This returns the single object if it hasn't been returned yet.
     *
     * @return the single object
     * @throws NoSuchElementException if the single object has already
     *    been returned
     */
    public E next() {
        if (!beforeFirst || removed) {
            throw new NoSuchElementException();
        }
        beforeFirst = false;
        nextCalled = true;
        return object;
    }

    /**
     * Get the previous object from the iterator.
     * <p>
     * This returns the single object if it has been returned.
     *
     * @return the single object
     * @throws NoSuchElementException if the single object has not already
     *    been returned
     */
    public E previous() {
        if (beforeFirst || removed) {
            throw new NoSuchElementException();
        }
        beforeFirst = true;
        return object;
    }

    /**
     * Remove the object from this iterator.
     * @throws IllegalStateException if the <tt>next</tt> or <tt>previous</tt>
     *        method has not yet been called, or the <tt>remove</tt> method
     *        has already been called after the last call to <tt>next</tt>
     *        or <tt>previous</tt>.
     */
    public void remove() {
        if(!nextCalled || removed) {
            throw new IllegalStateException();
        } else {
            object = null;
            removed = true;
        }
    }

    /**
     * Add always throws {@link UnsupportedOperationException}.
     *
     * @param obj  the object to add
     * @throws UnsupportedOperationException always
     */
    public void add(final E obj) {
        throw new UnsupportedOperationException("add() is not supported by this iterator");
    }

    /**
     * Set sets the value of the singleton.
     *
     * @param obj  the object to set
     * @throws IllegalStateException if <tt>next</tt> has not been called
     *          or the object has been removed
     */
    public void set(final E obj) {
        if (!nextCalled || removed) {
            throw new IllegalStateException();
        }
        this.object = obj;
    }

    /**
     * Reset the iterator back to the start.
     */
    public void reset() {
        beforeFirst = true;
        nextCalled = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2441.java