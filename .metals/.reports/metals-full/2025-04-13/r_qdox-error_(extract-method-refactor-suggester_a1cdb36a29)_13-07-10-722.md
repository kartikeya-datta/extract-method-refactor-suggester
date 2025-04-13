error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5437.java
text:
```scala
r@@eturn UnmodifiableOrderedMap.unmodifiableOrderedMap(ListOrderedMap.listOrderedMap(new HashMap<K, V>()));

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

import java.util.HashMap;

import junit.framework.Test;
import org.apache.commons.collections.BoundedMap;
import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.OrderedMap;

/**
 * JUnit tests.
 *
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class TestSingletonMap<K, V> extends AbstractTestOrderedMap<K, V> {

    private static final Integer ONE = new Integer(1);
    private static final Integer TWO = new Integer(2);
    private static final String TEN = "10";

    public TestSingletonMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestSingletonMap.class);
    }

    //-----------------------------------------------------------------------
    @Override
    public OrderedMap<K, V> makeObject() {
        // need an empty singleton map, but thats not possible
        // use a ridiculous fake instead to make the tests pass
        return UnmodifiableOrderedMap.decorate(ListOrderedMap.decorate(new HashMap<K, V>()));
    }

    @Override
    public String[] ignoredTests() {
        // the ridiculous map above still doesn't pass these tests
        // but its not relevant, so we ignore them
        return new String[] {
            "TestSingletonMap.bulkTestMapIterator.testEmptyMapIterator",
            "TestSingletonMap.bulkTestOrderedMapIterator.testEmptyMapIterator",
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public SingletonMap<K, V> makeFullMap() {
        return new SingletonMap<K, V>((K) ONE, (V) TWO);
    }

    @Override
    public boolean isPutAddSupported() {
        return false;
    }

    @Override
    public boolean isRemoveSupported() {
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public K[] getSampleKeys() {
        return (K[]) new Object[] { ONE };
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] getSampleValues() {
        return (V[]) new Object[] { TWO };
    }

    @Override
    @SuppressWarnings("unchecked")
    public V[] getNewSampleValues() {
        return (V[]) new Object[] { TEN };
    }

    //-----------------------------------------------------------------------
    public void testClone() {
        SingletonMap<K, V> map = makeFullMap();
        assertEquals(1, map.size());
        SingletonMap<K, V> cloned = map.clone();
        assertEquals(1, cloned.size());
        assertEquals(true, cloned.containsKey(ONE));
        assertEquals(true, cloned.containsValue(TWO));
    }

    public void testKeyValue() {
        SingletonMap<K, V> map = makeFullMap();
        assertEquals(1, map.size());
        assertEquals(ONE, map.getKey());
        assertEquals(TWO, map.getValue());
        assertTrue(map instanceof KeyValue);
    }

    public void testBoundedMap() {
        SingletonMap<K, V> map = makeFullMap();
        assertEquals(1, map.size());
        assertEquals(true, map.isFull());
        assertEquals(1, map.maxSize());
        assertTrue(map instanceof BoundedMap);
    }

    //-----------------------------------------------------------------------
//    public BulkTest bulkTestMapIterator() {
//        return new TestFlatMapIterator();
//    }
//
//    public class TestFlatMapIterator extends AbstractTestOrderedMapIterator {
//        public TestFlatMapIterator() {
//            super("TestFlatMapIterator");
//        }
//
//        public Object[] addSetValues() {
//            return TestSingletonMap.this.getNewSampleValues();
//        }
//
//        public boolean supportsRemove() {
//            return TestSingletonMap.this.isRemoveSupported();
//        }
//
//        public boolean supportsSetValue() {
//            return TestSingletonMap.this.isSetValueSupported();
//        }
//
//        public MapIterator makeEmptyMapIterator() {
//            resetEmpty();
//            return ((Flat3Map) TestSingletonMap.this.map).mapIterator();
//        }
//
//        public MapIterator makeFullMapIterator() {
//            resetFull();
//            return ((Flat3Map) TestSingletonMap.this.map).mapIterator();
//        }
//
//        public Map getMap() {
//            // assumes makeFullMapIterator() called first
//            return TestSingletonMap.this.map;
//        }
//
//        public Map getConfirmedMap() {
//            // assumes makeFullMapIterator() called first
//            return TestSingletonMap.this.confirmed;
//        }
//
//        public void verify() {
//            super.verify();
//            TestSingletonMap.this.verify();
//        }
//    }

    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/SingletonMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/SingletonMap.fullCollection.version3.1.obj");
//    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5437.java