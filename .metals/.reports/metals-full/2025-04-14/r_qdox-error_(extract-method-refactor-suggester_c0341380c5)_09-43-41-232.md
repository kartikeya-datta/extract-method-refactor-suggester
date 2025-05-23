error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12619.java
text:
```scala
S@@tringBuilder buffer = new StringBuilder();

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.collection.AbstractTestCollection;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

/**
 * Tests the PriorityBuffer.
 *
 * @version $Revision$ $Date$
 *
 * @author Michael A. Smith
 * @author Steve Phelps
 */
public class TestPriorityBuffer<E> extends AbstractTestCollection<E> {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestPriorityBuffer.class);
    }

    public TestPriorityBuffer(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void verify() {
        super.verify();
        PriorityBuffer<E> heap = getCollection();

        Comparator<? super E> c = heap.comparator;
        if (c == null) {
            c = ComparatorUtils.NATURAL_COMPARATOR;
        }
        if (!heap.ascendingOrder) {
            c = ComparatorUtils.reversedComparator(c);
        }

        E[] tree = heap.elements;
        for (int i = 1; i <= heap.size; i++) {
            E parent = tree[i];
            if (i * 2 <= heap.size) {
                assertTrue("Parent is less than or equal to its left child", c.compare(parent, tree[i * 2]) <= 0);
            }
            if (i * 2 + 1 < heap.size) {
                assertTrue("Parent is less than or equal to its right child", c.compare(parent, tree[i * 2 + 1]) <= 0);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Overridden because BinaryBuffer isn't fail fast.
     * @return false
     */
    public boolean isFailFastSupported() {
        return false;
    }

    //-----------------------------------------------------------------------
    public Collection<E> makeConfirmedCollection() {
        return new ArrayList<E>();
    }

    public Collection<E> makeConfirmedFullCollection() {
        ArrayList<E> list = new ArrayList<E>();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    /**
     * Return a new, empty {@link Object} to used for testing.
     */
    public Buffer<E> makeObject() {
        return new PriorityBuffer<E>();
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public E[] getFullElements() {
        return (E[]) getFullNonNullStringElements();
    }

    @SuppressWarnings("unchecked")
    public E[] getOtherElements() {
        return (E[]) getOtherNonNullStringElements();
    }

    //-----------------------------------------------------------------------
    public void testBufferEmpty() {
        resetEmpty();
        Buffer<E> buffer = getCollection();

        assertEquals(0, buffer.size());
        assertEquals(true, buffer.isEmpty());
        try {
            buffer.get();
            fail();
        } catch (BufferUnderflowException ex) {}

        try {
            buffer.remove();
            fail();
        } catch (BufferUnderflowException ex) {}
    }

    @SuppressWarnings("unchecked")
    public void testBasicOps() {
        PriorityBuffer<E> heap = new PriorityBuffer<E>();
        heap.add((E) "a");
        heap.add((E) "c");
        heap.add((E) "e");
        heap.add((E) "b");
        heap.add((E) "d");
        heap.add((E) "n");
        heap.add((E) "m");
        heap.add((E) "l");
        heap.add((E) "k");
        heap.add((E) "j");
        heap.add((E) "i");
        heap.add((E) "h");
        heap.add((E) "g");
        heap.add((E) "f");

        assertTrue("heap should not be empty after adds", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {
            assertEquals(
                "get using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.get());

            assertEquals(
                "remove using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('a' + i)),
                heap.remove());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are removed", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are removed", heap.isEmpty());
            }
        }

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}
    }

    @SuppressWarnings("unchecked")
    public void testBasicComparatorOps() {
        PriorityBuffer<E> heap = new PriorityBuffer<E>(new ReverseComparator<E>((Comparator<E>) ComparableComparator.INSTANCE));

        assertTrue("heap should be empty after create", heap.isEmpty());

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called before any elements are added");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called before any elements are added");
        } catch (BufferUnderflowException ex) {}

        heap.add((E) "a");
        heap.add((E) "c");
        heap.add((E) "e");
        heap.add((E) "b");
        heap.add((E) "d");
        heap.add((E) "n");
        heap.add((E) "m");
        heap.add((E) "l");
        heap.add((E) "k");
        heap.add((E) "j");
        heap.add((E) "i");
        heap.add((E) "h");
        heap.add((E) "g");
        heap.add((E) "f");

        assertTrue("heap should not be empty after adds", !heap.isEmpty());

        for (int i = 0; i < 14; i++) {

            // note: since we're using a comparator that reverses items, the
            // "minimum" item is "n", and the "maximum" item is "a".

            assertEquals(
                "get using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.get());

            assertEquals(
                "remove using default constructor should return minimum value in the binary heap",
                String.valueOf((char) ('n' - i)),
                heap.remove());

            if (i + 1 < 14) {
                assertTrue("heap should not be empty before all elements are removed", !heap.isEmpty());
            } else {
                assertTrue("heap should be empty after all elements are removed", heap.isEmpty());
            }
        }

        try {
            heap.get();
            fail("NoSuchElementException should be thrown if get is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}

        try {
            heap.remove();
            fail("NoSuchElementException should be thrown if remove is called after all elements are removed");
        } catch (BufferUnderflowException ex) {}
    }

    /**
     * Illustrates bad internal heap state reported in Bugzilla PR #235818.
     */
    @SuppressWarnings("unchecked")
    public void testAddRemove() {
        resetEmpty();
        PriorityBuffer heap = getCollection();
        heap.add(0);
        heap.add(2);
        heap.add(4);
        heap.add(3);
        heap.add(8);
        heap.add(10);
        heap.add(12);
        heap.add(3);
        getConfirmed().addAll(heap);
        // System.out.println(heap);
        heap.remove(10);
        getConfirmed().remove(10);
        // System.out.println(heap);
        verify();
    }

    /**
     * Generate heaps staring with Integers from 0 - heapSize - 1.
     * Then perform random add / remove operations, checking
     * heap order after modifications. Alternates minHeaps, maxHeaps.
     *
     * Based on code provided by Steve Phelps in PR #25818
     *
     */
    public void testRandom() {
        int iterations = 500;
        int heapSize = 100;
        int operations = 20;
        Random randGenerator = new Random();
        PriorityBuffer<Integer> h = null;
        for (int i = 0; i < iterations; i++) {
            if (i < iterations / 2) {
                h = new PriorityBuffer<Integer>(true);
            } else {
                h = new PriorityBuffer<Integer>(false);
            }
            for (int r = 0; r < heapSize; r++) {
                h.add(randGenerator.nextInt(heapSize));
            }
            for (int r = 0; r < operations; r++) {
                h.remove(new Integer(r));
                h.add(randGenerator.nextInt(heapSize));
            }
            checkOrder(h);
        }
    }

    /**
     * Pops all elements from the heap and verifies that the elements come off
     * in the correct order.  NOTE: this method empties the heap.
     */
    protected void checkOrder(PriorityBuffer<?> h) {
        Integer lastNum = null;
        Integer num = null;
        while (!h.isEmpty()) {
            num = (Integer) h.remove();
            if (h.ascendingOrder) {
                assertTrue(lastNum == null || num.intValue() >= lastNum.intValue());
            } else { // max heap
                assertTrue(lastNum == null || num.intValue() <= lastNum.intValue());
            }
            lastNum = num;
            num = null;
        }
    }

    /**
     * Returns a string showing the contents of the heap formatted as a tree.
     * Makes no attempt at padding levels or handling wrapping.
     */
    protected String showTree(PriorityBuffer<?> h) {
        int count = 1;
        StringBuffer buffer = new StringBuffer();
        for (int offset = 1; count < h.size() + 1; offset *= 2) {
            for (int i = offset; i < offset * 2; i++) {
                if (i < h.elements.length && h.elements[i] != null)
                    buffer.append(h.elements[i] + " ");
                count++;
            }
            buffer.append('\n');
        }
        return buffer.toString();
    }

    /**
     * Generates 500 randomly initialized heaps of size 100
     * and tests that after serializing and restoring them to a byte array
     * that the following conditions hold:
     *
     *  - the size of the restored heap is the same
     *      as the size of the orignal heap
     *
     *  - all elements in the original heap are present in the restored heap
     *
     *  - the heap order of the restored heap is intact as
     *      verified by checkOrder()
     */
    @SuppressWarnings("unchecked")
    public void testSerialization() {
        int iterations = 500;
        int heapSize = 100;
        PriorityBuffer h;
        Random randGenerator = new Random();
        for (int i = 0; i < iterations; i++) {
            if (i < iterations / 2) {
                h = new PriorityBuffer<E>(true);
            } else {
                h = new PriorityBuffer<E>(false);
            }
            for (int r = 0; r < heapSize; r++) {
                h.add(new Integer(randGenerator.nextInt(heapSize)));
            }
            assertTrue(h.size() == heapSize);
            PriorityBuffer<?> h1 = serializeAndRestore(h);
            assertTrue(h1.size() == heapSize);
            Iterator<?> hit = h.iterator();
            while (hit.hasNext()) {
                Integer n = (Integer) hit.next();
                assertTrue(h1.contains(n));
            }
            checkOrder(h1);
        }
    }

    public PriorityBuffer<?> serializeAndRestore(PriorityBuffer<E> h) {
        PriorityBuffer<?> h1 = null;
        try {
            byte[] objekt = writeExternalFormToBytes(h);
            h1 = (PriorityBuffer<?>) readExternalFormFromBytes(objekt);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.toString());
        }
        return h1;
    }

    public String getCompatibilityVersion() {
        return "3.2";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/PriorityBuffer.emptyCollection.version3.2.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "C:/commons/collections/data/test/PriorityBuffer.fullCollection.version3.2.obj");
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PriorityBuffer<E> getCollection() {
        return (PriorityBuffer<E>) super.getCollection();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12619.java