error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5800.java
text:
```scala
final C@@omparator<? super E> comparator = comparators.next();

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
package org.apache.commons.collections4.comparators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * A ComparatorChain is a Comparator that wraps one or more Comparators in
 * sequence. The ComparatorChain calls each Comparator in sequence until either
 * 1) any single Comparator returns a non-zero result (and that result is then
 * returned), or 2) the ComparatorChain is exhausted (and zero is returned).
 * This type of sorting is very similar to multi-column sorting in SQL, and this
 * class allows Java classes to emulate that kind of behaviour when sorting a
 * List.
 * <p>
 * To further facilitate SQL-like sorting, the order of any single Comparator in
 * the list can be reversed.
 * <p>
 * Calling a method that adds new Comparators or changes the ascend/descend sort
 * <i>after compare(Object, Object) has been called</i> will result in an
 * UnsupportedOperationException. However, <i>take care</i> to not alter the
 * underlying List of Comparators or the BitSet that defines the sort order.
 * <p>
 * Instances of ComparatorChain are not synchronized. The class is not
 * thread-safe at construction time, but it <i>is</i> thread-safe to perform
 * multiple comparisons after all the setup operations are complete.
 *
 * @since 2.0
 * @version $Id$
 */
public class ComparatorChain<E> implements Comparator<E>, Serializable {

    /** Serialization version from Collections 2.0. */
    private static final long serialVersionUID = -721644942746081630L;

    /** The list of comparators in the chain. */
    private final List<Comparator<E>> comparatorChain;
    /** Order - false (clear) = ascend; true (set) = descend. */
    private BitSet orderingBits = null;
   /** Whether the chain has been "locked". */
    private boolean isLocked = false;

    //-----------------------------------------------------------------------
    /**
     * Construct a ComparatorChain with no Comparators.
     * You must add at least one Comparator before calling
     * the compare(Object,Object) method, or an
     * UnsupportedOperationException is thrown
     */
    public ComparatorChain() {
        this(new ArrayList<Comparator<E>>(), new BitSet());
    }

    /**
     * Construct a ComparatorChain with a single Comparator,
     * sorting in the forward order
     *
     * @param comparator First comparator in the Comparator chain
     */
    public ComparatorChain(final Comparator<E> comparator) {
        this(comparator, false);
    }

    /**
     * Construct a Comparator chain with a single Comparator,
     * sorting in the given order
     *
     * @param comparator First Comparator in the ComparatorChain
     * @param reverse    false = forward sort; true = reverse sort
     */
    public ComparatorChain(final Comparator<E> comparator, final boolean reverse) {
        comparatorChain = new ArrayList<Comparator<E>>(1);
        comparatorChain.add(comparator);
        orderingBits = new BitSet(1);
        if (reverse == true) {
            orderingBits.set(0);
        }
    }

    /**
     * Construct a ComparatorChain from the Comparators in the
     * List.  All Comparators will default to the forward
     * sort order.
     *
     * @param list   List of Comparators
     * @see #ComparatorChain(List,BitSet)
     */
    public ComparatorChain(final List<Comparator<E>> list) {
        this(list, new BitSet(list.size()));
    }

    /**
     * Construct a ComparatorChain from the Comparators in the
     * given List.  The sort order of each column will be
     * drawn from the given BitSet.  When determining the sort
     * order for Comparator at index <i>i</i> in the List,
     * the ComparatorChain will call BitSet.get(<i>i</i>).
     * If that method returns <i>false</i>, the forward
     * sort order is used; a return value of <i>true</i>
     * indicates reverse sort order.
     *
     * @param list   List of Comparators.  NOTE: This constructor does not perform a
     *               defensive copy of the list
     * @param bits   Sort order for each Comparator.  Extra bits are ignored,
     *               unless extra Comparators are added by another method.
     */
    public ComparatorChain(final List<Comparator<E>> list, final BitSet bits) {
        comparatorChain = list;
        orderingBits = bits;
    }

    //-----------------------------------------------------------------------
    /**
     * Add a Comparator to the end of the chain using the
     * forward sort order
     *
     * @param comparator Comparator with the forward sort order
     */
    public void addComparator(final Comparator<E> comparator) {
        addComparator(comparator, false);
    }

    /**
     * Add a Comparator to the end of the chain using the
     * given sort order
     *
     * @param comparator Comparator to add to the end of the chain
     * @param reverse    false = forward sort order; true = reverse sort order
     */
    public void addComparator(final Comparator<E> comparator, final boolean reverse) {
        checkLocked();

        comparatorChain.add(comparator);
        if (reverse == true) {
            orderingBits.set(comparatorChain.size() - 1);
        }
    }

    /**
     * Replace the Comparator at the given index, maintaining
     * the existing sort order.
     *
     * @param index      index of the Comparator to replace
     * @param comparator Comparator to place at the given index
     * @exception IndexOutOfBoundsException
     *                   if index &lt; 0 or index &gt;= size()
     */
    public void setComparator(final int index, final Comparator<E> comparator) throws IndexOutOfBoundsException {
        setComparator(index, comparator, false);
    }

    /**
     * Replace the Comparator at the given index in the
     * ComparatorChain, using the given sort order
     *
     * @param index      index of the Comparator to replace
     * @param comparator Comparator to set
     * @param reverse    false = forward sort order; true = reverse sort order
     */
    public void setComparator(final int index, final Comparator<E> comparator, final boolean reverse) {
        checkLocked();

        comparatorChain.set(index,comparator);
        if (reverse == true) {
            orderingBits.set(index);
        } else {
            orderingBits.clear(index);
        }
    }

    /**
     * Change the sort order at the given index in the
     * ComparatorChain to a forward sort.
     *
     * @param index  Index of the ComparatorChain
     */
    public void setForwardSort(final int index) {
        checkLocked();
        orderingBits.clear(index);
    }

    /**
     * Change the sort order at the given index in the
     * ComparatorChain to a reverse sort.
     *
     * @param index  Index of the ComparatorChain
     */
    public void setReverseSort(final int index) {
        checkLocked();
        orderingBits.set(index);
    }

    /**
     * Number of Comparators in the current ComparatorChain.
     *
     * @return Comparator count
     */
    public int size() {
        return comparatorChain.size();
    }

    /**
     * Determine if modifications can still be made to the
     * ComparatorChain.  ComparatorChains cannot be modified
     * once they have performed a comparison.
     *
     * @return true = ComparatorChain cannot be modified; false =
     *         ComparatorChain can still be modified.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Throws an exception if the {@link ComparatorChain} is locked.
     *
     * @throws UnsupportedOperationException if the {@link ComparatorChain} is locked
     */
    private void checkLocked() {
        if (isLocked == true) {
            throw new UnsupportedOperationException(
                    "Comparator ordering cannot be changed after the first comparison is performed");
        }
    }

    /**
     * Throws an exception if the {@link ComparatorChain} is empty.
     *
     * @throws UnsupportedOperationException if the {@link ComparatorChain} is empty
     */
    private void checkChainIntegrity() {
        if (comparatorChain.size() == 0) {
            throw new UnsupportedOperationException("ComparatorChains must contain at least one Comparator");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Perform comparisons on the Objects as per
     * Comparator.compare(o1,o2).
     *
     * @param o1  the first object to compare
     * @param o2  the second object to compare
     * @return -1, 0, or 1
     * @throws UnsupportedOperationException if the ComparatorChain does not contain at least one Comparator
     */
    public int compare(final E o1, final E o2) throws UnsupportedOperationException {
        if (isLocked == false) {
            checkChainIntegrity();
            isLocked = true;
        }

        // iterate over all comparators in the chain
        final Iterator<Comparator<E>> comparators = comparatorChain.iterator();
        for (int comparatorIndex = 0; comparators.hasNext(); ++comparatorIndex) {

            final Comparator<E> comparator = comparators.next();
            int retval = comparator.compare(o1,o2);
            if (retval != 0) {
                // invert the order if it is a reverse sort
                if (orderingBits.get(comparatorIndex) == true) {
                    if (retval > 0) {
                        retval = -1;
                    } else {
                        retval = 1;
                    }
                }
                return retval;
            }
        }

        // if comparators are exhausted, return 0
        return 0;
    }

    //-----------------------------------------------------------------------
    /**
     * Implement a hash code for this comparator that is consistent with
     * {@link #equals(Object) equals}.
     *
     * @return a suitable hash code
     * @since 3.0
     */
    @Override
    public int hashCode() {
        int hash = 0;
        if (null != comparatorChain) {
            hash ^= comparatorChain.hashCode();
        }
        if (null != orderingBits) {
            hash ^= orderingBits.hashCode();
        }
        return hash;
    }

    /**
     * Returns <code>true</code> iff <i>that</i> Object is
     * is a {@link Comparator} whose ordering is known to be
     * equivalent to mine.
     * <p>
     * This implementation returns <code>true</code>
     * iff <code><i>object</i>.{@link Object#getClass() getClass()}</code>
     * equals <code>this.getClass()</code>, and the underlying
     * comparators and order bits are equal.
     * Subclasses may want to override this behavior to remain consistent
     * with the {@link Comparator#equals(Object)} contract.
     *
     * @param object  the object to compare with
     * @return true if equal
     * @since 3.0
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (null == object) {
            return false;
        }
        if (object.getClass().equals(this.getClass())) {
            final ComparatorChain<?> chain = (ComparatorChain<?>) object;
            return (null == orderingBits ? null == chain.orderingBits : orderingBits.equals(chain.orderingBits)) &&
                   (null == comparatorChain ? null == chain.comparatorChain :
                                              comparatorChain.equals(chain.comparatorChain));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5800.java