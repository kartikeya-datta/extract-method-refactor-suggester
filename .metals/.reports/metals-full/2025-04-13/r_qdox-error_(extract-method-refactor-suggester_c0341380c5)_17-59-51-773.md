error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13313.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13313.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13313.java
text:
```scala
r@@eturn o instanceof String;

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
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;

/**
 * Extension of {@link AbstractMapTest} for exercising the 
 * {@link PredicatedMap} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$
 *
 * @author Phil Steitz
 */
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    protected static final Predicate<Object> truePredicate = TruePredicate.<Object>truePredicate();

    protected static final Predicate<Object> testPredicate = new Predicate<Object>() {
        public boolean evaluate(Object o) {
            return (o instanceof String);
        }
    };

    public PredicatedMapTest(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    protected IterableMap<K, V> decorateMap(Map<K, V> map, Predicate<? super K> keyPredicate,
        Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<K, V>(), truePredicate, truePredicate);
    }

    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<K, V>(), testPredicate, testPredicate);
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testEntrySet() {
        Map<K, V> map = makeTestMap();
        assertTrue("returned entryset should not be null",
            map.entrySet() != null);
        map = decorateMap(new HashMap<K, V>(), null, null);
        map.put((K) "oneKey", (V) "oneValue");
        assertTrue("returned entryset should contain one entry",
            map.entrySet().size() == 1);
        map = decorateMap(map, null, null);
    }

    @SuppressWarnings("unchecked")
    public void testPut() {
        Map<K, V> map = makeTestMap();
        try {
            map.put((K) "Hi", (V) new Integer(3));
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            map.put((K) new Integer(3), (V) "Hi");
            fail("Illegal key should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        assertTrue(!map.containsKey(new Integer(3)));
        assertTrue(!map.containsValue(new Integer(3)));

        Map<K, V> map2 = new HashMap<K, V>();
        map2.put((K) "A", (V) "a");
        map2.put((K) "B", (V) "b");
        map2.put((K) "C", (V) "c");
        map2.put((K) "c", (V) new Integer(3));

        try {
            map.putAll(map2);
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        map.put((K) "E", (V) "e");
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        try {
            Map.Entry<K, V> entry = iterator.next();
            entry.setValue((V) new Integer(3));
            fail("Illegal value should raise IllegalArgument");
        } catch (IllegalArgumentException e) {
            // expected
        }

        map.put((K) "F", (V) "f");
        iterator = map.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();
        entry.setValue((V) "x");

    }

    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/PredicatedMap.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "D:/dev/collections/data/test/PredicatedMap.fullCollection.version3.1.obj");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13313.java