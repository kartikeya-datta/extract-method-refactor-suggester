error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7057.java
text:
```scala
C@@aseInsensitiveMap<String, String> map = new CaseInsensitiveMap<String, String>();

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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.apache.commons.collections.BulkTest;

/**
 * Tests for the {@link CaseInsensitiveMap} implementation.
 *
 * @version $Revision$ $Date$
 *
 * @author Commons-Collections team
 */
public class TestCaseInsensitiveMap<K, V> extends AbstractTestIterableMap<K, V> {

    public TestCaseInsensitiveMap(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestCaseInsensitiveMap.class);
    }

    @Override
    public CaseInsensitiveMap<K, V> makeObject() {
        return new CaseInsensitiveMap<K, V>();
    }

    @Override
    public String getCompatibilityVersion() {
        return "3";
    }

    //-------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void testCaseInsensitive() {
        Map<K, V> map = makeObject();
        map.put((K) "One", (V) "One");
        map.put((K) "Two", (V) "Two");
        assertEquals("One", map.get("one"));
        assertEquals("One", map.get("oNe"));
        map.put((K) "two", (V) "Three");
        assertEquals("Three", map.get("Two"));
    }

    @SuppressWarnings("unchecked")
    public void testNullHandling() {
        Map<K, V> map = makeObject();
        map.put((K) "One", (V) "One");
        map.put((K) "Two", (V) "Two");
        map.put(null, (V) "Three");
        assertEquals("Three", map.get(null));
        map.put(null, (V) "Four");
        assertEquals("Four", map.get(null));
        Set<K> keys = map.keySet();
        assertTrue(keys.contains("one"));
        assertTrue(keys.contains("two"));
        assertTrue(keys.contains(null));
        assertEquals(3, keys.size());
    }

    public void testPutAll() {
        Map<Object, String> map = new HashMap<Object, String>();
        map.put("One", "One");
        map.put("Two", "Two");
        map.put("one", "Three");
        map.put(null, "Four");
        map.put(new Integer(20), "Five");
        Map<Object, String> caseInsensitiveMap = new CaseInsensitiveMap<Object, String>(map);
        assertEquals(4, caseInsensitiveMap.size()); // ones collapsed
        Set<Object> keys = caseInsensitiveMap.keySet();
        assertTrue(keys.contains("one"));
        assertTrue(keys.contains("two"));
        assertTrue(keys.contains(null));
        assertTrue(keys.contains(Integer.toString(20)));
        assertEquals(4, keys.size());
        assertTrue(!caseInsensitiveMap.containsValue("One")
 !caseInsensitiveMap.containsValue("Three")); // ones collaped
        assertEquals("Four", caseInsensitiveMap.get(null));
    }

    @SuppressWarnings("unchecked")
    public void testClone() {
        CaseInsensitiveMap<K, V> map = new CaseInsensitiveMap<K, V>(10);
        map.put((K) "1", (V) "1");
        CaseInsensitiveMap<K, V> cloned = map.clone();
        assertEquals(map.size(), cloned.size());
        assertSame(map.get("1"), cloned.get("1"));
    }

    /*
    public void testCreate() throws Exception {
        resetEmpty();
        writeExternalFormToDisk((java.io.Serializable) map, "/home/phil/jakarta-commons/collections/data/test/CaseInsensitiveMap.emptyCollection.version3.obj");
        resetFull();
        writeExternalFormToDisk((java.io.Serializable) map, "/home/phil/jakarta-commons/collections/data/test/CaseInsensitiveMap.fullCollection.version3.obj");
    }
     */

    // COLLECTIONS-294
    public void testLocaleIndependence() {
        Locale orig = Locale.getDefault();

        Locale[] locales = { Locale.ENGLISH, new Locale("tr", "", ""), Locale.getDefault() };

        String[][] data = { 
            { "i", "I" },
            { "\u03C2", "\u03C3" },
            { "\u03A3", "\u03C2" },
            { "\u03A3", "\u03C3" },
        };

        try {
            for (int i = 0; i < locales.length; i++) {
                Locale.setDefault(locales[i]);
                for (int j = 0; j < data.length; j++) {
                    assertTrue("Test data corrupt: " + j, data[j][0].equalsIgnoreCase(data[j][1]));
                    CaseInsensitiveMap map = new CaseInsensitiveMap();
                    map.put(data[j][0], "value");
                    assertEquals(Locale.getDefault() + ": " + j, "value", map.get(data[j][1]));
                }
            }
        } finally {
            Locale.setDefault(orig);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7057.java