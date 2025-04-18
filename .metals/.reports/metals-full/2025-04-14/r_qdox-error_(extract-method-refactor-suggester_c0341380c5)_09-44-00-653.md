error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5434.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5434.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5434.java
text:
```scala
M@@ultiKeyMap<K, V> map = MultiKeyMap.multiKeyMap(new LRUMap<MultiKey<? extends K>, V>(2));

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

import java.util.Map;

import junit.framework.Test;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * JUnit tests.
 *
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class TestMultiKeyMap<K, V> extends AbstractTestIterableMap<MultiKey<? extends K>, V> {

    static final Integer I1 = new Integer(1);
    static final Integer I2 = new Integer(2);
    static final Integer I3 = new Integer(3);
    static final Integer I4 = new Integer(4);
    static final Integer I5 = new Integer(5);
    static final Integer I6 = new Integer(6);
    static final Integer I7 = new Integer(7);
    static final Integer I8 = new Integer(8);

    public TestMultiKeyMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestMultiKeyMap.class);
    }

    @Override
    public MultiKeyMap<K, V> makeObject() {
        return new MultiKeyMap<K, V>();
    }

    @Override
    public MultiKey<K>[] getSampleKeys() {
        return getMultiKeyKeys();
    }

    @SuppressWarnings("unchecked")
    private MultiKey<K>[] getMultiKeyKeys() {
        return new MultiKey[] {
            new MultiKey<Integer>(I1, I2),
            new MultiKey<Integer>(I2, I3),
            new MultiKey<Integer>(I3, I4),
            new MultiKey<Integer>(I1, I1, I2),
            new MultiKey<Integer>(I2, I3, I4),
            new MultiKey<Integer>(I3, I7, I6),
            new MultiKey<Integer>(I1, I1, I2, I3),
            new MultiKey<Integer>(I2, I4, I5, I6),
            new MultiKey<Integer>(I3, I6, I7, I8),
            new MultiKey<Integer>(I1, I1, I2, I3, I4),
            new MultiKey<Integer>(I2, I3, I4, I5, I6),
            new MultiKey<Integer>(I3, I5, I6, I7, I8),
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] getSampleValues() {
        return (V[]) new Object[] {
            "2A", "2B", "2C",
            "3D", "3E", "3F",
            "4G", "4H", "4I",
            "5J", "5K", "5L",
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] getNewSampleValues() {
        return (V[]) new Object[] {
            "1a", "1b", "1c",
            "2d", "2e", "2f",
            "3g", "3h", "3i",
            "4j", "4k", "4l",
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public MultiKey<K>[] getOtherKeys() {
        return new MultiKey[] {
            new MultiKey<Integer>(I1, I7),
            new MultiKey<Integer>(I1, I8),
            new MultiKey<Integer>(I2, I4),
            new MultiKey<Integer>(I2, I5),
        };
    }

    @Override
    public boolean isAllowNullKey() {
        return false;
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testNullHandling() {
        resetFull();
        assertEquals(null, map.get(null));
        assertEquals(false, map.containsKey(null));
        assertEquals(false, map.containsValue(null));
        assertEquals(null, map.remove(null));
        assertEquals(false, map.entrySet().contains(null));
        assertEquals(false, map.keySet().contains(null));
        assertEquals(false, map.values().contains(null));
        try {
            map.put(null, null);
            fail();
        } catch (NullPointerException ex) {}
        assertEquals(null, map.put(new MultiKey<K>(null, null), null));
        try {
            map.put(null, (V) new Object());
            fail();
        } catch (NullPointerException ex) {}
    }

    //-----------------------------------------------------------------------
    public void testMultiKeyGet() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        MultiKey<K>[] keys = getMultiKeyKeys();
        V[] values = getSampleValues();

        for (int i = 0; i < keys.length; i++) {
            MultiKey<K> key = keys[i];
            V value = values[i];

            switch (key.size()) {
                case 2:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(null, multimap.get(null, key.getKey(1)));
                assertEquals(null, multimap.get(key.getKey(0), null));
                assertEquals(null, multimap.get(null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, null, null));
                break;
                case 3:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null));
                assertEquals(null, multimap.get(null, null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null, null));
                break;
                case 4:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2), key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, key.getKey(3)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(null, multimap.get(null, null, null, null));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                break;
                case 5:
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(null, key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), null, key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), null, key.getKey(3), key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), null, key.getKey(4)));
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(null, multimap.get(null, null, null, null, null));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }

    public void testMultiKeyContainsKey() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        MultiKey<K>[] keys = getMultiKeyKeys();

        for (int i = 0; i < keys.length; i++) {
            MultiKey<K> key = keys[i];

            switch (key.size()) {
                case 2:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null));
                assertEquals(false, multimap.containsKey(null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, null, null));
                break;
                case 3:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null));
                assertEquals(false, multimap.containsKey(null, null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null, null));
                break;
                case 4:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(false, multimap.containsKey(null, null, null, null));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                break;
                case 5:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(null, key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), null, key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), null, key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), null, key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(false, multimap.containsKey(null, null, null, null, null));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }

    public void testMultiKeyPut() {
        MultiKey<K>[] keys = getMultiKeyKeys();
        V[] values = getSampleValues();

        for (int i = 0; i < keys.length; i++) {
            MultiKeyMap<K, V> multimap = new MultiKeyMap<K, V>();

            MultiKey<K> key = keys[i];
            V value = values[i];

            switch (key.size()) {
                case 2:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(new MultiKey<K>(key.getKey(0), key.getKey(1))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                break;
                case 3:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(new MultiKey<K>(key.getKey(0), key.getKey(1), key.getKey(2))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                break;
                case 4:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(new MultiKey<K>(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                break;
                case 5:
                assertEquals(null, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4), value));
                assertEquals(1, multimap.size());
                assertEquals(value, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(new MultiKey<K>(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4))));
                assertEquals(value, multimap.put(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4), null));
                assertEquals(1, multimap.size());
                assertEquals(null, multimap.get(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }

    public void testMultiKeyRemove() {
        MultiKey<K>[] keys = getMultiKeyKeys();
        V[] values = getSampleValues();

        for (int i = 0; i < keys.length; i++) {
            resetFull();
            MultiKeyMap<K, V> multimap = getMap();
            int size = multimap.size();

            MultiKey<K> key = keys[i];
            V value = values[i];

            switch (key.size()) {
                case 2:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1)));
                break;
                case 3:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2)));
                break;
                case 4:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3)));
                break;
                case 5:
                assertEquals(true, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(value, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(size - 1, multimap.size());
                assertEquals(null, multimap.remove(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                assertEquals(false, multimap.containsKey(key.getKey(0), key.getKey(1), key.getKey(2), key.getKey(3), key.getKey(4)));
                break;
                default:
                fail("Invalid key size");
            }
        }
    }

    public void testMultiKeyRemoveAll1() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        assertEquals(12, multimap.size());

        multimap.removeAll(I1);
        assertEquals(8, multimap.size());
        for (MapIterator<MultiKey<? extends K>, V> it = multimap.mapIterator(); it.hasNext();) {
            MultiKey<? extends K> key = it.next();
            assertEquals(false, I1.equals(key.getKey(0)));
        }
    }

    public void testMultiKeyRemoveAll2() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        assertEquals(12, multimap.size());

        multimap.removeAll(I2, I3);
        assertEquals(9, multimap.size());
        for (MapIterator<MultiKey<? extends K>, V> it = multimap.mapIterator(); it.hasNext();) {
            MultiKey<? extends K> key = it.next();
            assertEquals(false, I2.equals(key.getKey(0)) && I3.equals(key.getKey(1)));
        }
    }

    public void testMultiKeyRemoveAll3() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        assertEquals(12, multimap.size());

        multimap.removeAll(I1, I1, I2);
        assertEquals(9, multimap.size());
        for (MapIterator<MultiKey<? extends K>, V> it = multimap.mapIterator(); it.hasNext();) {
            MultiKey<? extends K> key = it.next();
            assertEquals(false, I1.equals(key.getKey(0)) && I1.equals(key.getKey(1)) && I2.equals(key.getKey(2)));
        }
    }

    public void testMultiKeyRemoveAll4() {
        resetFull();
        MultiKeyMap<K, V> multimap = getMap();
        assertEquals(12, multimap.size());

        multimap.removeAll(I1, I1, I2, I3);
        assertEquals(10, multimap.size());
        for (MapIterator<MultiKey<? extends K>, V> it = multimap.mapIterator(); it.hasNext();) {
            MultiKey<? extends K> key = it.next();
            assertEquals(false, I1.equals(key.getKey(0)) && I1.equals(key.getKey(1)) && I2.equals(key.getKey(2)) && key.size() >= 4 && I3.equals(key.getKey(3)));
        }
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testClone() {
        MultiKeyMap<K, V> map = new MultiKeyMap<K, V>();
        map.put(new MultiKey<K>((K) I1, (K) I2), (V) "1-2");
        Map<MultiKey<? extends K>, V> cloned = map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get(new MultiKey<K>((K) I1, (K) I2)), cloned.get(new MultiKey<K>((K) I1, (K) I2)));
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testLRUMultiKeyMap() {
        MultiKeyMap<K, V> map = MultiKeyMap.decorate(new LRUMap<MultiKey<? extends K>, V>(2));
        map.put((K) I1, (K) I2, (V) "1-2");
        map.put((K) I1, (K) I3, (V) "1-1");
        assertEquals(2, map.size());
        map.put((K) I1, (K) I4, (V) "1-4");
        assertEquals(2, map.size());
        assertEquals(true, map.containsKey(I1, I3));
        assertEquals(true, map.containsKey(I1, I4));
        assertEquals(false, map.containsKey(I1, I2));

        MultiKeyMap<K, V> cloned = map.clone();
        assertEquals(2, map.size());
        assertEquals(true, cloned.containsKey(I1, I3));
        assertEquals(true, cloned.containsKey(I1, I4));
        assertEquals(false, cloned.containsKey(I1, I2));
        cloned.put((K) I1, (K) I5, (V) "1-5");
        assertEquals(2, cloned.size());
        assertEquals(true, cloned.containsKey(I1, I4));
        assertEquals(true, cloned.containsKey(I1, I5));
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
//            "D:/dev/collections/data/test/MultiKeyMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/MultiKeyMap.fullCollection.version3.1.obj");
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultiKeyMap<K, V> getMap() {
        return (MultiKeyMap<K, V>) super.getMap();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5434.java