error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11451.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11451.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11451.java
text:
```scala
i@@f (!isPutAddSupported() || !isPutChangeSupported()) {

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
package org.apache.commons.collections4.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import org.apache.commons.collections4.BulkTest;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.ResettableIterator;
import org.apache.commons.collections4.list.AbstractListTest;

/**
 * JUnit tests.
 *
 * @version $Id$
 */
public class LinkedMapTest<K, V> extends AbstractOrderedMapTest<K, V> {

    public LinkedMapTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(LinkedMapTest.class);
    }

    @Override
    public LinkedMap<K, V> makeObject() {
        return new LinkedMap<K, V>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedMap<K, V> makeFullMap() {
        return (LinkedMap<K, V>) super.makeFullMap();
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testReset() {
        resetEmpty();
        OrderedMap<K, V> ordered = getMap();
        ((ResettableIterator<K>) ordered.mapIterator()).reset();

        resetFull();
        ordered = getMap();
        final List<K> list = new ArrayList<K>(ordered.keySet());
        final ResettableIterator<K> it = (ResettableIterator<K>) ordered.mapIterator();
        assertSame(list.get(0), it.next());
        assertSame(list.get(1), it.next());
        it.reset();
        assertSame(list.get(0), it.next());
    }

    //-----------------------------------------------------------------------
    public void testInsertionOrder() {
        if (isPutAddSupported() == false || isPutChangeSupported() == false) {
            return;
        }
        final K[] keys = getSampleKeys();
        final V[] values = getSampleValues();
        Iterator<K> keyIter;
        Iterator<V> valueIter;

        resetEmpty();
        map.put(keys[0], values[0]);
        map.put(keys[1], values[1]);
        keyIter = map.keySet().iterator();
        assertSame(keys[0], keyIter.next());
        assertSame(keys[1], keyIter.next());
        valueIter = map.values().iterator();
        assertSame(values[0], valueIter.next());
        assertSame(values[1], valueIter.next());

        // no change to order
        map.put(keys[1], values[1]);
        keyIter = map.keySet().iterator();
        assertSame(keys[0], keyIter.next());
        assertSame(keys[1], keyIter.next());
        valueIter = map.values().iterator();
        assertSame(values[0], valueIter.next());
        assertSame(values[1], valueIter.next());

        // no change to order
        map.put(keys[1], values[2]);
        keyIter = map.keySet().iterator();
        assertSame(keys[0], keyIter.next());
        assertSame(keys[1], keyIter.next());
        valueIter = map.values().iterator();
        assertSame(values[0], valueIter.next());
        assertSame(values[2], valueIter.next());

        // no change to order
        map.put(keys[0], values[3]);
        keyIter = map.keySet().iterator();
        assertSame(keys[0], keyIter.next());
        assertSame(keys[1], keyIter.next());
        valueIter = map.values().iterator();
        assertSame(values[3], valueIter.next());
        assertSame(values[2], valueIter.next());
    }

    //-----------------------------------------------------------------------
    public void testGetByIndex() {
        resetEmpty();
        LinkedMap<K, V> lm = getMap();
        try {
            lm.get(0);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.get(-1);
        } catch (final IndexOutOfBoundsException ex) {}

        resetFull();
        lm = getMap();
        try {
            lm.get(-1);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.get(lm.size());
        } catch (final IndexOutOfBoundsException ex) {}

        int i = 0;
        for (final MapIterator<K, V> it = lm.mapIterator(); it.hasNext(); i++) {
            assertSame(it.next(), lm.get(i));
        }
    }

    public void testGetValueByIndex() {
        resetEmpty();
        LinkedMap<K, V> lm = getMap();
        try {
            lm.getValue(0);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.getValue(-1);
        } catch (final IndexOutOfBoundsException ex) {}

        resetFull();
        lm = getMap();
        try {
            lm.getValue(-1);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.getValue(lm.size());
        } catch (final IndexOutOfBoundsException ex) {}

        int i = 0;
        for (final MapIterator<K, V> it = lm.mapIterator(); it.hasNext(); i++) {
            it.next();
            assertSame(it.getValue(), lm.getValue(i));
        }
    }

    public void testIndexOf() {
        resetEmpty();
        LinkedMap<K, V> lm = getMap();
        assertEquals(-1, lm.indexOf(getOtherKeys()));

        resetFull();
        lm = getMap();
        final List<K> list = new ArrayList<K>();
        for (final MapIterator<K, V> it = lm.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, lm.indexOf(list.get(i)));
        }
    }

    public void testRemoveByIndex() {
        resetEmpty();
        LinkedMap<K, V> lm = getMap();
        try {
            lm.remove(0);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.remove(-1);
        } catch (final IndexOutOfBoundsException ex) {}

        resetFull();
        lm = getMap();
        try {
            lm.remove(-1);
        } catch (final IndexOutOfBoundsException ex) {}
        try {
            lm.remove(lm.size());
        } catch (final IndexOutOfBoundsException ex) {}

        final List<K> list = new ArrayList<K>();
        for (final MapIterator<K, V> it = lm.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            final Object key = list.get(i);
            final Object value = lm.get(key);
            assertEquals(value, lm.remove(i));
            list.remove(i);
            assertEquals(false, lm.containsKey(key));
        }
    }

    public BulkTest bulkTestListView() {
        return new TestListView();
    }

    public class TestListView extends AbstractListTest<K> {

        TestListView() {
            super("TestListView");
        }

        @Override
        public List<K> makeObject() {
            return LinkedMapTest.this.makeObject().asList();
        }

        @Override
        public List<K> makeFullCollection() {
            return LinkedMapTest.this.makeFullMap().asList();
        }

        @Override
        public K[] getFullElements() {
            return LinkedMapTest.this.getSampleKeys();
        }
        @Override
        public boolean isAddSupported() {
            return false;
        }
        @Override
        public boolean isRemoveSupported() {
            return false;
        }
        @Override
        public boolean isSetSupported() {
            return false;
        }
        @Override
        public boolean isNullSupported() {
            return LinkedMapTest.this.isAllowNullKey();
        }
        @Override
        public boolean isTestSerialization() {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void testClone() {
        final LinkedMap<K, V> map = new LinkedMap<K, V>(10);
        map.put((K) "1", (V) "1");
        final Map<K, V> cloned = map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get("1"), cloned.get("1"));
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) map, "src/test/resources/data/test/LinkedMap.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) map, "src/test/resources/data/test/LinkedMap.fullCollection.version4.obj");
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedMap<K, V> getMap() {
        return (LinkedMap<K, V>) super.getMap();
    }

    /**
     * Test for <a href="https://issues.apache.org/jira/browse/COLLECTIONS-323">COLLECTIONS-323</a>.
     */
    public void testInitialCapacityZero() {
        final LinkedMap<String,String> map = new LinkedMap<String,String>(0);
        assertEquals(1, map.data.length);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11451.java