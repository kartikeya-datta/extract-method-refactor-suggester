error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1004.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1004.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1004.java
text:
```scala
r@@eturn ListOrderedMap.listOrderedMap(new HashMap<K, V>());

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
package org.apache.commons.collections.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.Test;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.list.AbstractTestList;

/**
 * Extension of {@link AbstractTestOrderedMap} for exercising the {@link ListOrderedMap}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$
 *
 * @author Stephen Colebourne
 * @author Matt Benson
 */
public class TestListOrderedMap<K, V> extends AbstractTestOrderedMap<K, V> {

    public TestListOrderedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestListOrderedMap.class);
    }

    @Override
    public ListOrderedMap<K, V> makeObject() {
        return (ListOrderedMap<K, V>) ListOrderedMap.listOrderedMap(new HashMap<K, V>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListOrderedMap<K, V> makeFullMap() {
        return (ListOrderedMap<K, V>) super.makeFullMap();
    }

    //-----------------------------------------------------------------------
    public void testGetByIndex() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();
        try {
            lom.get(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.get(-1);
        } catch (IndexOutOfBoundsException ex) {}

        resetFull();
        lom = getMap();
        try {
            lom.get(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.get(lom.size());
        } catch (IndexOutOfBoundsException ex) {}

        int i = 0;
        for (MapIterator<K, V> it = lom.mapIterator(); it.hasNext(); i++) {
            assertSame(it.next(), lom.get(i));
        }
    }

    public void testGetValueByIndex() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();
        try {
            lom.getValue(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}

        resetFull();
        lom = getMap();
        try {
            lom.getValue(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.getValue(lom.size());
        } catch (IndexOutOfBoundsException ex) {}

        int i = 0;
        for (MapIterator<K, V> it = lom.mapIterator(); it.hasNext(); i++) {
            it.next();
            assertSame(it.getValue(), lom.getValue(i));
        }
    }

    public void testIndexOf() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();
        assertEquals(-1, lom.indexOf(getOtherKeys()));

        resetFull();
        lom = getMap();
        List<K> list = new ArrayList<K>();
        for (MapIterator<K, V> it = lom.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            assertEquals(i, lom.indexOf(list.get(i)));
        }
    }

    @SuppressWarnings("unchecked")
    public void testSetValueByIndex() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();
        try {
            lom.setValue(0, (V) "");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.setValue(-1, (V) "");
        } catch (IndexOutOfBoundsException ex) {}

        resetFull();
        lom = getMap();
        try {
            lom.setValue(-1, (V) "");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.setValue(lom.size(), (V) "");
        } catch (IndexOutOfBoundsException ex) {}

        for (int i = 0; i < lom.size(); i++) {
            V value = lom.getValue(i);
            Object input = new Integer(i);
            assertEquals(value, lom.setValue(i, (V) input));
            assertEquals(input, lom.getValue(i));
        }
    }

    public void testRemoveByIndex() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();
        try {
            lom.remove(0);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}

        resetFull();
        lom = getMap();
        try {
            lom.remove(-1);
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.remove(lom.size());
        } catch (IndexOutOfBoundsException ex) {}

        List<K> list = new ArrayList<K>();
        for (MapIterator<K, V> it = lom.mapIterator(); it.hasNext();) {
            list.add(it.next());
        }
        for (int i = 0; i < list.size(); i++) {
            Object key = list.get(i);
            Object value = lom.get(key);
            assertEquals(value, lom.remove(i));
            list.remove(i);
            assertEquals(false, lom.containsKey(key));
        }
    }

    @SuppressWarnings("unchecked")
    public void testPut_intObjectObject() {
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();

        try {
            lom.put(1, (K) "testInsert1", (V) "testInsert1v");
            fail("should not be able to insert at pos 1 in empty Map");
        } catch (IndexOutOfBoundsException ex) {}
        try {
            lom.put(-1, (K) "testInsert-1", (V) "testInsert-1v");
            fail("should not be able to insert at pos -1 in empty Map");
        } catch (IndexOutOfBoundsException ex) {}

        // put where key doesn't exist
        lom.put(0, (K) "testInsert1", (V) "testInsert1v");
        assertEquals("testInsert1v", lom.getValue(0));

        lom.put((K) "testInsertPut", (V) "testInsertPutv");
        assertEquals("testInsert1v", lom.getValue(0));
        assertEquals("testInsertPutv", lom.getValue(1));

        lom.put(0, (K) "testInsert0", (V) "testInsert0v");
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("testInsert1v", lom.getValue(1));
        assertEquals("testInsertPutv", lom.getValue(2));

        lom.put(3, (K) "testInsert3", (V) "testInsert3v");
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("testInsert1v", lom.getValue(1));
        assertEquals("testInsertPutv", lom.getValue(2));
        assertEquals("testInsert3v", lom.getValue(3));

        // put in a full map
        resetFull();
        lom = getMap();
        ListOrderedMap<K, V> lom2 = new ListOrderedMap<K, V>();
        lom2.putAll(lom);

        lom2.put(0, (K) "testInsert0", (V) "testInsert0v");
        assertEquals("testInsert0v", lom2.getValue(0));
        for (int i = 0; i < lom.size(); i++) {
            assertEquals(lom2.getValue(i + 1), lom.getValue(i));
        }

        // put where key does exist
        Integer i1 = new Integer(1);
        Integer i1b = new Integer(1);
        Integer i2 = new Integer(2);
        Integer i3 = new Integer(3);

        resetEmpty();
        lom = getMap();
        lom.put((K) i1, (V) "1");
        lom.put((K) i2, (V) "2");
        lom.put((K) i3, (V) "3");
        lom.put(0, (K) i1, (V) "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertSame(i1, lom.get(0));

        resetEmpty();
        lom = getMap();
        lom.put((K) i1, (V) "1");
        lom.put((K) i2, (V) "2");
        lom.put((K) i3, (V) "3");
        lom.put(0, (K) i1b, (V) "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertEquals("2", lom.getValue(1));
        assertEquals("3", lom.getValue(2));
        assertSame(i1b, lom.get(0));

        resetEmpty();
        lom = getMap();
        lom.put((K) i1, (V) "1");
        lom.put((K) i2, (V) "2");
        lom.put((K) i3, (V) "3");
        lom.put(1, (K) i1b, (V) "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("One", lom.getValue(0));
        assertEquals("2", lom.getValue(1));
        assertEquals("3", lom.getValue(2));

        resetEmpty();
        lom = getMap();
        lom.put((K) i1, (V) "1");
        lom.put((K) i2, (V) "2");
        lom.put((K) i3, (V) "3");
        lom.put(2, (K) i1b, (V) "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("2", lom.getValue(0));
        assertEquals("One", lom.getValue(1));
        assertEquals("3", lom.getValue(2));

        resetEmpty();
        lom = getMap();
        lom.put((K) i1, (V) "1");
        lom.put((K) i2, (V) "2");
        lom.put((K) i3, (V) "3");
        lom.put(3, (K) i1b, (V) "One");
        assertEquals(3, lom.size());
        assertEquals(3, lom.map.size());
        assertEquals(3, lom.insertOrder.size());
        assertEquals("2", lom.getValue(0));
        assertEquals("3", lom.getValue(1));
        assertEquals("One", lom.getValue(2));
    }

    public void testPutAllWithIndex() {
        resetEmpty();
        ListOrderedMap<String, String> lom = (ListOrderedMap<String, String>) map;

        // Create Initial Data
        lom.put("testInsert0", "testInsert0v");
        lom.put("testInsert1", "testInsert1v");
        lom.put("testInsert2", "testInsert2v");
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("testInsert1v", lom.getValue(1));
        assertEquals("testInsert2v", lom.getValue(2));

        // Create New Test Map and Add using putAll(int, Object, Object)
        Map<String, String> values = new ListOrderedMap<String, String>();
        values.put("NewInsert0", "NewInsert0v");
        values.put("NewInsert1", "NewInsert1v");
        lom.putAll(1, values);

        // Perform Asserts
        assertEquals("testInsert0v", lom.getValue(0));
        assertEquals("NewInsert0v", lom.getValue(1));
        assertEquals("NewInsert1v", lom.getValue(2));
        assertEquals("testInsert1v", lom.getValue(3));
        assertEquals("testInsert2v", lom.getValue(4));
    }

    public void testPutAllWithIndexBug441() {
        // see COLLECTIONS-441
        resetEmpty();
        ListOrderedMap<K, V> lom = getMap();

        int size = 5;
        for (int i = 0; i < size; i++) {
            lom.put((K) Integer.valueOf(i), (V) Boolean.valueOf(true));
        }

        Map<K, V> map = new TreeMap<K, V>();
        for (int i = 0; i < size; i++) {
            map.put((K) Integer.valueOf(i), (V) Boolean.valueOf(false));
        }

        lom.putAll(3, map);
        
        List<K> orderedList = lom.asList();
        for (int i = 0; i < size; i++) {
            assertEquals(i, orderedList.get(i));
        }
    }
    
    //-----------------------------------------------------------------------
    public void testValueList_getByIndex() {
        resetFull();
        ListOrderedMap<K, V> lom = getMap();
        for (int i = 0; i < lom.size(); i++) {
            V expected = lom.getValue(i);
            assertEquals(expected, lom.valueList().get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public void testValueList_setByIndex() {
        resetFull();
        ListOrderedMap<K, V> lom = getMap();
        for (int i = 0; i < lom.size(); i++) {
            Object input = new Integer(i);
            V expected = lom.getValue(i);
            assertEquals(expected, lom.valueList().set(i, (V) input));
            assertEquals(input, lom.getValue(i));
            assertEquals(input, lom.valueList().get(i));
        }
    }

    public void testValueList_removeByIndex() {
        resetFull();
        ListOrderedMap<K, V> lom = getMap();
        while (lom.size() > 1) {
            V expected = lom.getValue(1);
            assertEquals(expected, lom.valueList().remove(1));
        }
    }

    //-----------------------------------------------------------------------
    public BulkTest bulkTestKeyListView() {
        return new TestKeyListView();
    }

    public BulkTest bulkTestValueListView() {
        return new TestValueListView();
    }

    //-----------------------------------------------------------------------
    public class TestKeyListView extends AbstractTestList<K> {
        TestKeyListView() {
            super("TestKeyListView");
        }

        @Override
        public List<K> makeObject() {
            return TestListOrderedMap.this.makeObject().keyList();
        }
        @Override
        public List<K> makeFullCollection() {
            return TestListOrderedMap.this.makeFullMap().keyList();
        }

        @Override
        public K[] getFullElements() {
            return TestListOrderedMap.this.getSampleKeys();
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
            return TestListOrderedMap.this.isAllowNullKey();
        }
        @Override
        public boolean isTestSerialization() {
            return false;
        }
    }

    //-----------------------------------------------------------------------
    public class TestValueListView extends AbstractTestList<V> {
        TestValueListView() {
            super("TestValueListView");
        }

        @Override
        public List<V> makeObject() {
            return TestListOrderedMap.this.makeObject().valueList();
        }
        @Override
        public List<V> makeFullCollection() {
            return TestListOrderedMap.this.makeFullMap().valueList();
        }

        @Override
        public V[] getFullElements() {
            return TestListOrderedMap.this.getSampleValues();
        }
        @Override
        public boolean isAddSupported() {
            return false;
        }
        @Override
        public boolean isRemoveSupported() {
            return true;
        }
        @Override
        public boolean isSetSupported() {
            return true;
        }
        @Override
        public boolean isNullSupported() {
            return TestListOrderedMap.this.isAllowNullKey();
        }
        @Override
        public boolean isTestSerialization() {
            return false;
        }
    }

    //-----------------------------------------------------------------------
    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/ListOrderedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/ListOrderedMap.fullCollection.version3.1.obj");
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListOrderedMap<K, V> getMap() {
        return (ListOrderedMap<K, V>) super.getMap();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1004.java