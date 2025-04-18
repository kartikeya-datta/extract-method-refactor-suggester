error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5443.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5443.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5443.java
text:
```scala
t@@ransformedMap = TransformedMap.transformingMap(backingMap, NOPTransformer.<String> nopTransformer(),

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
package org.apache.commons.collections.splitmap;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.BulkTest;
import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.Put;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.Unmodifiable;
import org.apache.commons.collections.functors.NOPTransformer;
import org.apache.commons.collections.map.HashedMap;

/**
 * Tests for {@link TransformedMap}
 *
 * @since Commons Collections 5
 * @TODO fix version
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 * @author Matt Benson
 */
@SuppressWarnings("boxing")
public class TestSplitMapUtils extends BulkTest {
    private Map<String, Integer> backingMap;
    private TransformedMap<String, String, String, Integer> transformedMap;

    private Transformer<String, Integer> stringToInt = new Transformer<String, Integer>() {
        public Integer transform(String input) {
            return Integer.valueOf(input);
        }
    };

    public TestSplitMapUtils(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        backingMap = new HashMap<String, Integer>();
        transformedMap = TransformedMap.decorate(backingMap, NOPTransformer.<String> getInstance(),
                stringToInt);
        for (int i = 0; i < 10; i++) {
            transformedMap.put(String.valueOf(i), String.valueOf(i));
        }
    }

    // -----------------------------------------------------------------------

    public void testReadableMap() {
        final IterableMap<String, Integer> map = SplitMapUtils.readableMap(transformedMap);

        // basic
        for (int i = 0; i < 10; i++) {
            assertFalse(map.containsValue(String.valueOf(i)));
            assertEquals(i, map.get(String.valueOf(i)).intValue());
        }

        // mapIterator
        MapIterator<String, Integer> it = map.mapIterator();
        while (it.hasNext()) {
            String k = it.next();
            assertEquals(k, it.getKey());
            assertEquals(Integer.valueOf(k), it.getValue());
        }

        // unmodifiable
        assertTrue(map instanceof Unmodifiable);

        // check individual operations
        int sz = map.size();

        attemptPutOperation(new Runnable() {
            public void run() {
                map.clear();
            }
        });

        assertEquals(sz, map.size());

        attemptPutOperation(new Runnable() {
            public void run() {
                map.put("foo", 100);
            }
        });

        final HashMap<String, Integer> m = new HashMap<String, Integer>();
        m.put("foo", 100);
        m.put("bar", 200);
        m.put("baz", 300);
        attemptPutOperation(new Runnable() {
            public void run() {
                map.putAll(m);
            }
        });

        // equals, hashcode
        IterableMap<String, Integer> other = SplitMapUtils.readableMap(transformedMap);
        assertEquals(other, map);
        assertEquals(other.hashCode(), map.hashCode());

        // remove
        for (int i = 0; i < 10; i++) {
            assertEquals(i, map.remove(String.valueOf(i)).intValue());
            assertEquals(--sz, map.size());
        }
        assertTrue(map.isEmpty());
        assertSame(map, SplitMapUtils.readableMap(map));
    }

    public void testAlreadyReadableMap() {
        HashedMap<String, Integer> hashedMap = new HashedMap<String, Integer>();
        assertSame(hashedMap, SplitMapUtils.readableMap(hashedMap));
    }

    @SuppressWarnings("unchecked")
    public void testWritableMap() {
        final Map<String, String> map = SplitMapUtils.writableMap(transformedMap);
        attemptGetOperation(new Runnable() {
            public void run() {
                map.get(null);
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.entrySet();
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.keySet();
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.values();
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.size();
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.isEmpty();
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.containsKey(null);
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.containsValue(null);
            }
        });
        attemptGetOperation(new Runnable() {
            public void run() {
                map.remove(null);
            }
        });

        // equals, hashcode
        Map<String, String> other = SplitMapUtils.writableMap(transformedMap);
        assertEquals(other, map);
        assertEquals(other.hashCode(), map.hashCode());

        // put
        int sz = backingMap.size();
        assertFalse(backingMap.containsKey("foo"));
        map.put("new", "66");
        assertEquals(++sz, backingMap.size());

        // putall
        Map<String, String> more = new HashMap<String, String>();
        more.put("foo", "77");
        more.put("bar", "88");
        more.put("baz", "99");
        map.putAll(more);
        assertEquals(sz + more.size(), backingMap.size());

        // clear
        map.clear();
        assertTrue(backingMap.isEmpty());
        assertSame(map, SplitMapUtils.writableMap((Put<String, String>) map));
    }

    public void testAlreadyWritableMap() {
        HashedMap<String, String> hashedMap = new HashedMap<String, String>();
        assertSame(hashedMap, SplitMapUtils.writableMap(hashedMap));
    }

    private void attemptGetOperation(Runnable r) {
        attemptMapOperation("Put exposed as writable Map must not allow Get operations", r);
    }

    private void attemptPutOperation(Runnable r) {
        attemptMapOperation("Get exposed as writable Map must not allow Put operations", r);
    }

    private void attemptMapOperation(String s, Runnable r) {
        try {
            r.run();
            fail(s);
        } catch (UnsupportedOperationException e) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5443.java