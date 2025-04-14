error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11448.java
text:
```scala
w@@hile (keys.length > 1 && !isInNewOrder) {

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

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Test class for FixedOrderComparator.
 *
 * @version $Id$
 */
public class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     */
    private static final String topCities[] = new String[] {
        "Tokyo",
        "Mexico City",
        "Mumbai",
        "Sao Paulo",
        "New York",
        "Shanghai",
        "Lagos",
        "Los Angeles",
        "Calcutta",
        "Buenos Aires"
    };

    //
    // Initialization and busywork
    //

    public FixedOrderComparatorTest(final String name) {
        super(name);
    }

    //
    // Set up and tear down
    //

    @Override
    public Comparator<String> makeObject() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCities);
        return comparator;
    }

    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(topCities);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

//    public void testCreate() throws Exception {
//        writeExternalFormToDisk((java.io.Serializable) makeObject(), "src/test/resources/data/test/FixedOrderComparator.version4.obj");
//    }

    //
    // The tests
    //

    /**
     * Tests that the constructor plus add method compares items properly.
     */
    @Test
    public void testConstructorPlusAdd() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>();
        for (final String topCitie : topCities) {
            comparator.add(topCitie);
        }
        final String[] keys = topCities.clone();
        assertComparatorYieldsOrder(keys, comparator);
    }

    /**
     * Tests that the array constructor compares items properly.
     */
    @Test
    public void testArrayConstructor() {
        final String[] keys = topCities.clone();
        final String[] topCitiesForTest = topCities.clone();
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCitiesForTest);
        assertComparatorYieldsOrder(keys, comparator);
        // test that changing input after constructor has no effect
        topCitiesForTest[0] = "Brighton";
        assertComparatorYieldsOrder(keys, comparator);
    }

    /**
     * Tests the list constructor.
     */
    @Test
    public void testListConstructor() {
        final String[] keys = topCities.clone();
        final List<String> topCitiesForTest = new LinkedList<String>(Arrays.asList(topCities));
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCitiesForTest);
        assertComparatorYieldsOrder(keys, comparator);
        // test that changing input after constructor has no effect
        topCitiesForTest.set(0, "Brighton");
        assertComparatorYieldsOrder(keys, comparator);
    }

    /**
     * Tests addAsEqual method.
     */
    @Test
    public void testAddAsEqual() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCities);
        comparator.addAsEqual("New York", "Minneapolis");
        assertEquals(0, comparator.compare("New York", "Minneapolis"));
        assertEquals(-1, comparator.compare("Tokyo", "Minneapolis"));
        assertEquals(1, comparator.compare("Shanghai", "Minneapolis"));
    }

    /**
     * Tests whether or not updates are disabled after a comparison is made.
     */
    @Test
    public void testLock() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCities);
        assertEquals(false, comparator.isLocked());
        comparator.compare("New York", "Tokyo");
        assertEquals(true, comparator.isLocked());
        try {
            comparator.add("Minneapolis");
            fail("Should have thrown an UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // success -- ignore
        }

        try {
            comparator.addAsEqual("New York", "Minneapolis");
            fail("Should have thrown an UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // success -- ignore
        }
    }

    @Test
    public void testUnknownObjectBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<String>(topCities);
        try {
            comparator.compare("New York", "Minneapolis");
            fail("Should have thrown a IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // success-- ignore
        }
        try {
            comparator.compare("Minneapolis", "New York");
            fail("Should have thrown a IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // success-- ignore
        }
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());

        comparator = new FixedOrderComparator<String>(topCities);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.BEFORE, comparator.getUnknownObjectBehavior());
        LinkedList<String> keys = new LinkedList<String>(Arrays.asList(topCities));
        keys.addFirst("Minneapolis");
        assertComparatorYieldsOrder(keys.toArray(new String[0]), comparator);

        assertEquals(-1, comparator.compare("Minneapolis", "New York"));
        assertEquals( 1, comparator.compare("New York", "Minneapolis"));
        assertEquals( 0, comparator.compare("Minneapolis", "St Paul"));

        comparator = new FixedOrderComparator<String>(topCities);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        keys = new LinkedList<String>(Arrays.asList(topCities));
        keys.add("Minneapolis");
        assertComparatorYieldsOrder(keys.toArray(new String[0]), comparator);

        assertEquals( 1, comparator.compare("Minneapolis", "New York"));
        assertEquals(-1, comparator.compare("New York", "Minneapolis"));
        assertEquals( 0, comparator.compare("Minneapolis", "St Paul"));

    }

    //
    // Helper methods
    //

    /** Shuffles the keys and asserts that the comparator sorts them back to
     * their original order.
     */
    private void assertComparatorYieldsOrder(final String[] orderedObjects,
                                             final Comparator<String> comparator) {
        final String[] keys = orderedObjects.clone();

        // shuffle until the order changes.  It's extremely rare that
        // this requires more than one shuffle.

        boolean isInNewOrder = false;
        final Random rand = new Random();
        while (keys.length > 1 && isInNewOrder == false) {
            // shuffle:
            for (int i = keys.length-1; i > 0; i--) {
                final String swap = keys[i];
                final int j = rand.nextInt(i+1);
                keys[i] = keys[j];
                keys[j] = swap;
            }

            // testShuffle
            for (int i = 0; i < keys.length && !isInNewOrder; i++) {
                if( !orderedObjects[i].equals(keys[i])) {
                    isInNewOrder = true;
                }
            }
        }

        // The real test:  sort and make sure they come out right.

        Arrays.sort(keys, comparator);

        for (int i = 0; i < orderedObjects.length; i++) {
            assertEquals(orderedObjects[i], keys[i]);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11448.java