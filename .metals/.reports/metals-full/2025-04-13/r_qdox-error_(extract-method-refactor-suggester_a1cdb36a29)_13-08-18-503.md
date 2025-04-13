error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6999.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6999.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6999.java
text:
```scala
h@@ash = new BytesRefHash(randomIntBetween(0, 100), maxLoadFactor, BigArraysTests.randombigArrays());

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common.util;

import com.carrotsearch.hppc.ObjectLongMap;
import com.carrotsearch.hppc.ObjectLongOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectLongCursor;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util._TestUtil;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;

public class BytesRefHashTests extends ElasticsearchTestCase {

    BytesRefHash hash;

    private void newHash() {
        if (hash != null) {
            hash.release();
        }
        // Test high load factors to make sure that collision resolution works fine
        final float maxLoadFactor = 0.6f + randomFloat() * 0.39f;
        hash = new BytesRefHash(randomIntBetween(0, 100), maxLoadFactor, BigArraysTests.randomCacheRecycler());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        newHash();
    }

    public void testDuell() {
        final int len = randomIntBetween(1, 100000);
        final BytesRef[] values = new BytesRef[len];
        for (int i = 0; i < values.length; ++i) {
            values[i] = new BytesRef(randomAsciiOfLength(5));
        }
        final ObjectLongMap<BytesRef> valueToId = new ObjectLongOpenHashMap<BytesRef>();
        final BytesRef[] idToValue = new BytesRef[values.length];
        final int iters = randomInt(1000000);
        for (int i = 0; i < iters; ++i) {
            final BytesRef value = randomFrom(values);
            if (valueToId.containsKey(value)) {
                assertEquals(- 1 - valueToId.get(value), hash.add(value, value.hashCode()));
            } else {
                assertEquals(valueToId.size(), hash.add(value, value.hashCode()));
                idToValue[valueToId.size()] = value;
                valueToId.put(value, valueToId.size());
            }
        }

        assertEquals(valueToId.size(), hash.size());
        for (Iterator<ObjectLongCursor<BytesRef>> iterator = valueToId.iterator(); iterator.hasNext(); ) {
            final ObjectLongCursor<BytesRef> next = iterator.next();
            assertEquals(next.value, hash.find(next.key, next.key.hashCode()));
        }

        for (long i = 0; i < hash.capacity(); ++i) {
            final long id = hash.id(i);
            BytesRef spare = new BytesRef();
            if (id >= 0) {
                hash.get(id, spare);
                assertEquals(idToValue[(int) id], spare);
            }
        }
        hash.release();
    }

    // START - tests borrowed from LUCENE

    /**
     * Test method for {@link org.apache.lucene.util.BytesRefHash#size()}.
     */
    @Test
    public void testSize() {
        BytesRef ref = new BytesRef();
        int num = atLeast(2);
        for (int j = 0; j < num; j++) {
            final int mod = 1+randomInt(40);
            for (int i = 0; i < 797; i++) {
                String str;
                do {
                    str = _TestUtil.randomRealisticUnicodeString(getRandom(), 1000);
                } while (str.length() == 0);
                ref.copyChars(str);
                long count = hash.size();
                long key = hash.add(ref);
                if (key < 0)
                    assertEquals(hash.size(), count);
                else
                    assertEquals(hash.size(), count + 1);
                if(i % mod == 0) {
                    newHash();
                }
            }
        }
        hash.release();
    }

    /**
     * Test method for
     * {@link org.apache.lucene.util.BytesRefHash#get(int, BytesRef)}
     * .
     */
    @Test
    public void testGet() {
        BytesRef ref = new BytesRef();
        BytesRef scratch = new BytesRef();
        int num = atLeast(2);
        for (int j = 0; j < num; j++) {
            Map<String, Long> strings = new HashMap<String, Long>();
            int uniqueCount = 0;
            for (int i = 0; i < 797; i++) {
                String str;
                do {
                    str = _TestUtil.randomRealisticUnicodeString(getRandom(), 1000);
                } while (str.length() == 0);
                ref.copyChars(str);
                long count = hash.size();
                long key = hash.add(ref);
                if (key >= 0) {
                    assertNull(strings.put(str, Long.valueOf(key)));
                    assertEquals(uniqueCount, key);
                    uniqueCount++;
                    assertEquals(hash.size(), count + 1);
                } else {
                    assertTrue((-key)-1 < count);
                    assertEquals(hash.size(), count);
                }
            }
            for (Entry<String, Long> entry : strings.entrySet()) {
                ref.copyChars(entry.getKey());
                assertEquals(ref, hash.get(entry.getValue().longValue(), scratch));
            }
            newHash();
        }
        hash.release();
    }

    /**
     * Test method for
     * {@link org.apache.lucene.util.BytesRefHash#add(org.apache.lucene.util.BytesRef)}
     * .
     */
    @Test
    public void testAdd() {
        BytesRef ref = new BytesRef();
        BytesRef scratch = new BytesRef();
        int num = atLeast(2);
        for (int j = 0; j < num; j++) {
            Set<String> strings = new HashSet<String>();
            int uniqueCount = 0;
            for (int i = 0; i < 797; i++) {
                String str;
                do {
                    str = _TestUtil.randomRealisticUnicodeString(getRandom(), 1000);
                } while (str.length() == 0);
                ref.copyChars(str);
                long count = hash.size();
                long key = hash.add(ref);

                if (key >=0) {
                    assertTrue(strings.add(str));
                    assertEquals(uniqueCount, key);
                    assertEquals(hash.size(), count + 1);
                    uniqueCount++;
                } else {
                    assertFalse(strings.add(str));
                    assertTrue((-key)-1 < count);
                    assertEquals(str, hash.get((-key)-1, scratch).utf8ToString());
                    assertEquals(count, hash.size());
                }
            }

            assertAllIn(strings, hash);
            newHash();
        }
        hash.release();
    }

    @Test
    public void testFind() throws Exception {
        BytesRef ref = new BytesRef();
        BytesRef scratch = new BytesRef();
        int num = atLeast(2);
        for (int j = 0; j < num; j++) {
            Set<String> strings = new HashSet<String>();
            int uniqueCount = 0;
            for (int i = 0; i < 797; i++) {
                String str;
                do {
                    str = _TestUtil.randomRealisticUnicodeString(getRandom(), 1000);
                } while (str.length() == 0);
                ref.copyChars(str);
                long count = hash.size();
                long key = hash.find(ref); //hash.add(ref);
                if (key >= 0) { // string found in hash
                    assertFalse(strings.add(str));
                    assertTrue(key < count);
                    assertEquals(str, hash.get(key, scratch).utf8ToString());
                    assertEquals(count, hash.size());
                } else {
                    key = hash.add(ref);
                    assertTrue(strings.add(str));
                    assertEquals(uniqueCount, key);
                    assertEquals(hash.size(), count + 1);
                    uniqueCount++;
                }
            }

            assertAllIn(strings, hash);
            newHash();
        }
        hash.release();
    }

    private void assertAllIn(Set<String> strings, BytesRefHash hash) {
        BytesRef ref = new BytesRef();
        BytesRef scratch = new BytesRef();
        long count = hash.size();
        for (String string : strings) {
            ref.copyChars(string);
            long key  =  hash.add(ref); // add again to check duplicates
            assertEquals(string, hash.get((-key)-1, scratch).utf8ToString());
            assertEquals(count, hash.size());
            assertTrue("key: " + key + " count: " + count + " string: " + string,
                    key < count);
        }
    }

    // END - tests borrowed from LUCENE

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6999.java