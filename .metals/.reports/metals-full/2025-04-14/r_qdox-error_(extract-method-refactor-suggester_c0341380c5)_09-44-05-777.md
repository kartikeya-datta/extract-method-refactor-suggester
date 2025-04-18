error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5400.java
text:
```scala
r@@eturn UnmodifiableList.unmodifiableList(iterators);

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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections.list.UnmodifiableList;


/**
 * Provides an ordered iteration over the elements contained in a collection of
 * ordered Iterators.
 * <p>
 * Given two ordered {@link Iterator} instances <code>A</code> and
 * <code>B</code>, the {@link #next} method on this iterator will return the
 * lesser of <code>A.next()</code> and <code>B.next()</code>.
 *
 * @since Commons Collections 2.1
 * @version $Revision$ $Date$
 *
 * @author Rodney Waldhoff
 * @author Stephen Colebourne
 */
public class CollatingIterator<E> implements Iterator<E> {

    /** The {@link Comparator} used to evaluate order. */
    private Comparator<? super E> comparator = null;

    /** The list of {@link Iterator}s to evaluate. */
    private ArrayList<Iterator<? extends E>> iterators = null;

    /** {@link Iterator#next Next} objects peeked from each iterator. */
    private ArrayList<E> values = null;

    /** Whether or not each {@link #values} element has been set. */
    private BitSet valueSet = null;

    /**
     * Index of the {@link #iterators iterator} from whom the last returned
     * value was obtained.
     */
    private int lastReturned = -1;

    // Constructors
    // ----------------------------------------------------------------------
    /**
     * Constructs a new <code>CollatingIterator</code>. A comparator must be
     * set by calling {@link #setComparator(Comparator)} before invoking
     * {@link #hasNext()}, or {@link #next()} for the first time. Child
     * iterators will have to be manually added using the
     * {@link #addIterator(Iterator)} method.
     */
    public CollatingIterator() {
        this(null, 2);
    }

    /**
     * Constructs a new <code>CollatingIterator</code> that will used the
     * specified comparator for ordering. Child iterators will have to be
     * manually added using the {@link #addIterator(Iterator)} method.
     * 
     * @param comp the comparator to use to sort; must not be null,
     *   unless you'll be invoking {@link #setComparator(Comparator)}
     *   later on.
     */
    public CollatingIterator(final Comparator<? super E> comp) {
        this(comp, 2);
    }

    /**
     * Constructs a new <code>CollatingIterator</code> that will used the
     * specified comparator for ordering and have the specified initial
     * capacity. Child iterators will have to be manually added using the
     * {@link #addIterator(Iterator)} method.
     * 
     * @param comp the comparator to use to sort; must not be null,
     *   unless you'll be invoking {@link #setComparator(Comparator)}
     *   later on.
     * @param initIterCapacity the initial capacity for the internal list of
     * child iterators
     */
    public CollatingIterator(final Comparator<? super E> comp, final int initIterCapacity) {
        iterators = new ArrayList<Iterator<? extends E>>(initIterCapacity);
        setComparator(comp);
    }

    /**
     * Constructs a new <code>CollatingIterator</code> that will use the
     * specified comparator to provide ordered iteration over the two given
     * iterators.
     * 
     * @param comp the comparator to use to sort; must not be null,
     *   unless you'll be invoking {@link #setComparator(Comparator)}
     *   later on.
     * @param a the first child ordered iterator
     * @param b the second child ordered iterator
     * @throws NullPointerException if either iterator is null
     */
    public CollatingIterator(final Comparator<? super E> comp, final Iterator<? extends E> a, final Iterator<? extends E> b) {
        this(comp, 2);
        addIterator(a);
        addIterator(b);
    }

    /**
     * Constructs a new <code>CollatingIterator</code> that will use the
     * specified comparator to provide ordered iteration over the array of
     * iterators.
     * 
     * @param comp the comparator to use to sort; must not be null,
     *   unless you'll be invoking {@link #setComparator(Comparator)}
     *   later on.
     * @param iterators the array of iterators
     * @throws NullPointerException if iterators array is or contains null
     */
    public CollatingIterator(final Comparator<? super E> comp, final Iterator<? extends E>[] iterators) {
        this(comp, iterators.length);
        for (int i = 0; i < iterators.length; i++) {
            addIterator(iterators[i]);
        }
    }

    /**
     * Constructs a new <code>CollatingIterator</code> that will use the
     * specified comparator to provide ordered iteration over the collection of
     * iterators.
     * 
     * @param comp the comparator to use to sort; must not be null,
     *   unless you'll be invoking {@link #setComparator(Comparator)}
     *   later on.
     * @param iterators the collection of iterators
     * @throws NullPointerException if the iterators collection is or contains
     * null
     * @throws ClassCastException if the iterators collection contains an
     * element that's not an {@link Iterator}
     */
    public CollatingIterator(final Comparator<? super E> comp, final Collection<Iterator<? extends E>> iterators) {
        this(comp, iterators.size());
        for (Iterator<? extends E> iterator : iterators) {
            addIterator(iterator);
        }
    }

    // Public Methods
    // ----------------------------------------------------------------------
    /**
     * Adds the given {@link Iterator} to the iterators being collated.
     * 
     * @param iterator the iterator to add to the collation, must not be null
     * @throws IllegalStateException if iteration has started
     * @throws NullPointerException if the iterator is null
     */
    public void addIterator(final Iterator<? extends E> iterator) {
        checkNotStarted();
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        iterators.add(iterator);
    }

    /**
     * Sets the iterator at the given index.
     * 
     * @param index index of the Iterator to replace
     * @param iterator Iterator to place at the given index
     * @throws IndexOutOfBoundsException if index &lt; 0 or index &gt; size()
     * @throws IllegalStateException if iteration has started
     * @throws NullPointerException if the iterator is null
     */
    public void setIterator(final int index, final Iterator<? extends E> iterator) {
        checkNotStarted();
        if (iterator == null) {
            throw new NullPointerException("Iterator must not be null");
        }
        iterators.set(index, iterator);
    }

    /**
     * Gets the list of Iterators (unmodifiable).
     * 
     * @return the unmodifiable list of iterators added
     */
    public List<Iterator<? extends E>> getIterators() {
        return UnmodifiableList.decorate(iterators);
    }

    /**
     * Gets the {@link Comparator} by which collatation occurs.
     */
    public Comparator<? super E> getComparator() {
        return comparator;
    }

    /**
     * Sets the {@link Comparator} by which collation occurs. If you
     * would like to use the natural sort order (or, in other words,
     * if the elements in the iterators are implementing the
     * {@link java.lang.Comparable} interface), then use the
     * {@link org.apache.commons.collections.comparators.ComparableComparator}.
     * 
     * @throws IllegalStateException if iteration has started
     */
    public void setComparator(final Comparator<? super E> comp) {
        checkNotStarted();
        comparator = comp;
    }

    // Iterator Methods
    // -------------------------------------------------------------------
    /**
     * Returns <code>true</code> if any child iterator has remaining elements.
     * 
     * @return true if this iterator has remaining elements
     */
    public boolean hasNext() {
        start();
        return anyValueSet(valueSet) || anyHasNext(iterators);
    }

    /**
     * Returns the next ordered element from a child iterator.
     * 
     * @return the next ordered element
     * @throws NoSuchElementException if no child iterator has any more elements
     */
    public E next() throws NoSuchElementException {
        if (hasNext() == false) {
            throw new NoSuchElementException();
        }
        int leastIndex = least();
        if (leastIndex == -1) {
            throw new NoSuchElementException();
        }
        E val = values.get(leastIndex);
        clear(leastIndex);
        lastReturned = leastIndex;
        return val;
    }

    /**
     * Removes the last returned element from the child iterator that produced
     * it.
     * 
     * @throws IllegalStateException if there is no last returned element, or if
     * the last returned element has already been removed
     */
    public void remove() {
        if (lastReturned == -1) {
            throw new IllegalStateException("No value can be removed at present");
        }
        iterators.get(lastReturned).remove();
    }

    /**
     * Returns the index of the iterator that returned the last element.
     * 
     * @return the index of the iterator that returned the last element
     * @throws IllegalStateException if there is no last returned element
     */
    public int getIteratorIndex() {
        if (lastReturned == -1) {
            throw new IllegalStateException("No value has been returned yet");
        }
        
        return lastReturned;
    }
    
    // Private Methods
    // -------------------------------------------------------------------
    /**
     * Initializes the collating state if it hasn't been already.
     */
    private void start() {
        if (values == null) {
            values = new ArrayList<E>(iterators.size());
            valueSet = new BitSet(iterators.size());
            for (int i = 0; i < iterators.size(); i++) {
                values.add(null);
                valueSet.clear(i);
            }
        }
    }

    /**
     * Sets the {@link #values} and {@link #valueSet} attributes at position
     * <i>i</i> to the next value of the {@link #iterators iterator} at position
     * <i>i</i>, or clear them if the <i>i</i><sup>th</sup> iterator has no next
     * value.
     * 
     * @return <tt>false</tt> iff there was no value to set
     */
    private boolean set(int i) {
        Iterator<? extends E> it = iterators.get(i);
        if (it.hasNext()) {
            values.set(i, it.next());
            valueSet.set(i);
            return true;
        }
        values.set(i, null);
        valueSet.clear(i);
        return false;
    }

    /**
     * Clears the {@link #values} and {@link #valueSet} attributes at position
     * <i>i</i>.
     */
    private void clear(int i) {
        values.set(i, null);
        valueSet.clear(i);
    }

    /**
     * Throws {@link IllegalStateException} if iteration has started via
     * {@link #start}.
     * 
     * @throws IllegalStateException if iteration started
     */
    private void checkNotStarted() throws IllegalStateException {
        if (values != null) {
            throw new IllegalStateException("Can't do that after next or hasNext has been called.");
        }
    }

    /**
     * Returns the index of the least element in {@link #values},
     * {@link #set(int) setting} any uninitialized values.
     * 
     * @throws IllegalStateException
     */
    private int least() {
        int leastIndex = -1;
        E leastObject = null;
        for (int i = 0; i < values.size(); i++) {
            if (valueSet.get(i) == false) {
                set(i);
            }
            if (valueSet.get(i)) {
                if (leastIndex == -1) {
                    leastIndex = i;
                    leastObject = values.get(i);
                } else {
                    E curObject = values.get(i);
                    if (comparator == null) {
                        throw new NullPointerException("You must invoke setComparator() to set a compator first.");
                    }
                    if (comparator.compare(curObject, leastObject) < 0) {
                        leastObject = curObject;
                        leastIndex = i;
                    }
                }
            }
        }
        return leastIndex;
    }

    /**
     * Returns <code>true</code> iff any bit in the given set is
     * <code>true</code>.
     */
    private boolean anyValueSet(BitSet set) {
        for (int i = 0; i < set.size(); i++) {
            if (set.get(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> iff any {@link Iterator} in the given list has
     * a next value.
     */
    private boolean anyHasNext(ArrayList<Iterator<? extends E>> iters) {
        for (Iterator<? extends E> iterator : iters) {
            if (iterator.hasNext()) {
                return true;
            }
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5400.java