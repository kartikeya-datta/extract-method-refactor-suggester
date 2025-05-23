error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5171.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5171.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5171.java
text:
```scala
.@@put(HierarchyCircuitBreakerService.REQUEST_CIRCUIT_BREAKER_LIMIT_SETTING, size - 1)

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

import org.apache.lucene.util.BytesRef;
import org.elasticsearch.cache.recycler.PageCacheRecycler;
import org.elasticsearch.common.breaker.CircuitBreaker;
import org.elasticsearch.common.breaker.CircuitBreakingException;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.indices.breaker.HierarchyCircuitBreakerService;
import org.elasticsearch.indices.breaker.NoneCircuitBreakerService;
import org.elasticsearch.node.settings.NodeSettingsService;
import org.elasticsearch.test.ElasticsearchSingleNodeTest;
import org.elasticsearch.test.cache.recycler.MockBigArrays;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class BigArraysTests extends ElasticsearchSingleNodeTest {

    public static BigArrays randombigArrays() {
        final PageCacheRecycler recycler = randomBoolean() ? null : ElasticsearchSingleNodeTest.getInstanceFromNode(PageCacheRecycler.class);
        return new MockBigArrays(ImmutableSettings.EMPTY, recycler, new NoneCircuitBreakerService());
    }

    private BigArrays bigArrays;

    @Before
    public void init() {
        bigArrays = randombigArrays();
    }

    public void testByteArrayGrowth() {
        final int totalLen = randomIntBetween(1, 4000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        ByteArray array = bigArrays.newByteArray(startLen, randomBoolean());
        byte[] ref = new byte[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomByte();
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertEquals(ref[i], array.get(i));
        }
        array.close();
    }

    public void testIntArrayGrowth() {
        final int totalLen = randomIntBetween(1, 1000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        IntArray array = bigArrays.newIntArray(startLen, randomBoolean());
        int[] ref = new int[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomInt();
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertEquals(ref[i], array.get(i));
        }
        array.close();
    }

    public void testLongArrayGrowth() {
        final int totalLen = randomIntBetween(1, 1000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        LongArray array = bigArrays.newLongArray(startLen, randomBoolean());
        long[] ref = new long[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomLong();
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertEquals(ref[i], array.get(i));
        }
        array.close();
    }

    public void testFloatArrayGrowth() {
        final int totalLen = randomIntBetween(1, 1000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        FloatArray array = bigArrays.newFloatArray(startLen, randomBoolean());
        float[] ref = new float[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomFloat();
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertEquals(ref[i], array.get(i), 0.001d);
        }
        array.close();
    }

    public void testDoubleArrayGrowth() {
        final int totalLen = randomIntBetween(1, 1000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        DoubleArray array = bigArrays.newDoubleArray(startLen, randomBoolean());
        double[] ref = new double[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomDouble();
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertEquals(ref[i], array.get(i), 0.001d);
        }
        array.close();
    }

    public void testObjectArrayGrowth() {
        final int totalLen = randomIntBetween(1, 1000000);
        final int startLen = randomIntBetween(1, randomBoolean() ? 1000 : totalLen);
        ObjectArray<Object> array = bigArrays.newObjectArray(startLen);
        final Object[] pool = new Object[100];
        for (int i = 0; i < pool.length; ++i) {
            pool[i] = new Object();
        }
        Object[] ref = new Object[totalLen];
        for (int i = 0; i < totalLen; ++i) {
            ref[i] = randomFrom(pool);
            array = bigArrays.grow(array, i + 1);
            array.set(i, ref[i]);
        }
        for (int i = 0; i < totalLen; ++i) {
            assertSame(ref[i], array.get(i));
        }
        array.close();
    }

    public void testByteArrayFill() {
        final int len = randomIntBetween(1, 100000);
        final int fromIndex = randomIntBetween(0, len - 1);
        final int toIndex = randomBoolean()
            ? Math.min(fromIndex + randomInt(100), len) // single page
            : randomIntBetween(fromIndex, len); // likely multiple pages
        final ByteArray array2 = bigArrays.newByteArray(len, randomBoolean());
        final byte[] array1 = new byte[len];
        for (int i = 0; i < len; ++i) {
            array1[i] = randomByte();
            array2.set(i, array1[i]);
        }
        final byte rand = randomByte();
        Arrays.fill(array1, fromIndex, toIndex, rand);
        array2.fill(fromIndex, toIndex, rand);
        for (int i = 0; i < len; ++i) {
            assertEquals(array1[i], array2.get(i), 0.001d);
        }
        array2.close();
    }

    public void testFloatArrayFill() {
        final int len = randomIntBetween(1, 100000);
        final int fromIndex = randomIntBetween(0, len - 1);
        final int toIndex = randomBoolean()
            ? Math.min(fromIndex + randomInt(100), len) // single page
            : randomIntBetween(fromIndex, len); // likely multiple pages
        final FloatArray array2 = bigArrays.newFloatArray(len, randomBoolean());
        final float[] array1 = new float[len];
        for (int i = 0; i < len; ++i) {
            array1[i] = randomFloat();
            array2.set(i, array1[i]);
        }
        final float rand = randomFloat();
        Arrays.fill(array1, fromIndex, toIndex, rand);
        array2.fill(fromIndex, toIndex, rand);
        for (int i = 0; i < len; ++i) {
            assertEquals(array1[i], array2.get(i), 0.001d);
        }
        array2.close();
    }

    public void testDoubleArrayFill() {
        final int len = randomIntBetween(1, 100000);
        final int fromIndex = randomIntBetween(0, len - 1);
        final int toIndex = randomBoolean()
            ? Math.min(fromIndex + randomInt(100), len) // single page
            : randomIntBetween(fromIndex, len); // likely multiple pages
        final DoubleArray array2 = bigArrays.newDoubleArray(len, randomBoolean());
        final double[] array1 = new double[len];
        for (int i = 0; i < len; ++i) {
            array1[i] = randomDouble();
            array2.set(i, array1[i]);
        }
        final double rand = randomDouble();
        Arrays.fill(array1, fromIndex, toIndex, rand);
        array2.fill(fromIndex, toIndex, rand);
        for (int i = 0; i < len; ++i) {
            assertEquals(array1[i], array2.get(i), 0.001d);
        }
        array2.close();
    }

    public void testLongArrayFill() {
        final int len = randomIntBetween(1, 100000);
        final int fromIndex = randomIntBetween(0, len - 1);
        final int toIndex = randomBoolean()
            ? Math.min(fromIndex + randomInt(100), len) // single page
            : randomIntBetween(fromIndex, len); // likely multiple pages
        final LongArray array2 = bigArrays.newLongArray(len, randomBoolean());
        final long[] array1 = new long[len];
        for (int i = 0; i < len; ++i) {
            array1[i] = randomLong();
            array2.set(i, array1[i]);
        }
        final long rand = randomLong();
        Arrays.fill(array1, fromIndex, toIndex, rand);
        array2.fill(fromIndex, toIndex, rand);
        for (int i = 0; i < len; ++i) {
            assertEquals(array1[i], array2.get(i));
        }
        array2.close();
    }

    public void testByteArrayBulkGet() {
        final byte[] array1 = new byte[randomIntBetween(1, 4000000)];
        getRandom().nextBytes(array1);
        final ByteArray array2 = bigArrays.newByteArray(array1.length, randomBoolean());
        for (int i = 0; i < array1.length; ++i) {
            array2.set(i, array1[i]);
        }
        final BytesRef ref = new BytesRef();
        for (int i = 0; i < 1000; ++i) {
            final int offset = randomInt(array1.length - 1);
            final int len = randomInt(Math.min(randomBoolean() ? 10 : Integer.MAX_VALUE, array1.length - offset));
            array2.get(offset, len, ref);
            assertEquals(new BytesRef(array1, offset, len), ref);
        }
        array2.close();
    }

    public void testByteArrayBulkSet() {
        final byte[] array1 = new byte[randomIntBetween(1, 4000000)];
        getRandom().nextBytes(array1);
        final ByteArray array2 = bigArrays.newByteArray(array1.length, randomBoolean());
        for (int i = 0; i < array1.length; ) {
            final int len = Math.min(array1.length - i, randomBoolean() ? randomInt(10) : randomInt(3 * BigArrays.BYTE_PAGE_SIZE));
            array2.set(i, array1, i, len);
            i += len;
        }
        for (int i = 0; i < array1.length; ++i) {
            assertEquals(array1[i], array2.get(i));
        }
        array2.close();
    }

    public void testByteArrayEquals() {
        final ByteArray empty1 = byteArrayWithBytes(BytesRef.EMPTY_BYTES);
        final ByteArray empty2 = byteArrayWithBytes(BytesRef.EMPTY_BYTES);

        // identity = equality
        assertTrue(bigArrays.equals(empty1, empty1));
        // equality: both empty
        assertTrue(bigArrays.equals(empty1, empty2));
        empty1.close();
        empty2.close();

        // not equal: contents differ
        final ByteArray a1 = byteArrayWithBytes(new byte[]{0});
        final ByteArray a2 = byteArrayWithBytes(new byte[]{1});
        assertFalse(bigArrays.equals(a1, a2));
        a1.close();
        a2.close();

        // not equal: contents differ
        final ByteArray a3 = byteArrayWithBytes(new byte[]{1,2,3});
        final ByteArray a4 = byteArrayWithBytes(new byte[]{1, 1, 3});
        assertFalse(bigArrays.equals(a3, a4));
        a3.close();
        a4.close();

        // not equal: contents differ
        final ByteArray a5 = byteArrayWithBytes(new byte[]{1,2,3});
        final ByteArray a6 = byteArrayWithBytes(new byte[]{1,2,4});
        assertFalse(bigArrays.equals(a5, a6));
        a5.close();
        a6.close();
    }

    public void testByteArrayHashCode() {
        // null arg has hashCode 0
        assertEquals(0, bigArrays.hashCode(null));

        // empty array should have equal hash
        final int emptyHash = Arrays.hashCode(BytesRef.EMPTY_BYTES);
        final ByteArray emptyByteArray = byteArrayWithBytes(BytesRef.EMPTY_BYTES);
        final int emptyByteArrayHash = bigArrays.hashCode(emptyByteArray);
        assertEquals(emptyHash, emptyByteArrayHash);
        emptyByteArray.close();

        // FUN FACT: Arrays.hashCode() and BytesReference.bytesHashCode() are inconsistent for empty byte[]
        // final int emptyHash3 = new BytesArray(BytesRef.EMPTY_BYTES).hashCode();
        // assertEquals(emptyHash1, emptyHash3); -> fail (1 vs. 0)

        // large arrays should be different
        final byte[] array1 = new byte[randomIntBetween(1, 4000000)];
        getRandom().nextBytes(array1);
        final int array1Hash = Arrays.hashCode(array1);
        final ByteArray array2 = byteArrayWithBytes(array1);
        final int array2Hash = bigArrays.hashCode(array2);
        assertEquals(array1Hash, array2Hash);
        array2.close();
    }

    private ByteArray byteArrayWithBytes(byte[] bytes) {
        ByteArray bytearray = bigArrays.newByteArray(bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            bytearray.set(i, bytes[i]);
        }
        return bytearray;
    }

    public void testMaxSizeExceededOnNew() throws Exception {
        final int size = scaledRandomIntBetween(5, 1 << 22);
        for (String type : Arrays.asList("Byte", "Int", "Long", "Float", "Double", "Object")) {
            HierarchyCircuitBreakerService hcbs = new HierarchyCircuitBreakerService(
                    ImmutableSettings.builder()
                            .put(HierarchyCircuitBreakerService.REQUEST_CIRCUIT_BREAKER_LIMIT_SETTING, size)
                            .build(),
                    new NodeSettingsService(ImmutableSettings.EMPTY));
            BigArrays bigArrays = new BigArrays(ImmutableSettings.EMPTY, null, hcbs).withCircuitBreaking();
            Method create = BigArrays.class.getMethod("new" + type + "Array", long.class);
            try {
                create.invoke(bigArrays, size);
                fail("expected an exception on " + create);
            } catch (InvocationTargetException e) {
                assertTrue(e.getCause() instanceof CircuitBreakingException);
            }
            assertEquals(0, hcbs.getBreaker(CircuitBreaker.Name.REQUEST).getUsed());
        }
    }

    public void testMaxSizeExceededOnResize() throws Exception {
        for (String type : Arrays.asList("Byte", "Int", "Long", "Float", "Double", "Object")) {
            final long maxSize = randomIntBetween(1 << 10, 1 << 22);
            HierarchyCircuitBreakerService hcbs = new HierarchyCircuitBreakerService(
                    ImmutableSettings.builder()
                            .put(HierarchyCircuitBreakerService.REQUEST_CIRCUIT_BREAKER_LIMIT_SETTING, maxSize)
                            .build(),
                    new NodeSettingsService(ImmutableSettings.EMPTY));
            BigArrays bigArrays = new BigArrays(ImmutableSettings.EMPTY, null, hcbs).withCircuitBreaking();
            Method create = BigArrays.class.getMethod("new" + type + "Array", long.class);
            final int size = scaledRandomIntBetween(1, 20);
            BigArray array = (BigArray) create.invoke(bigArrays, size);
            Method resize = BigArrays.class.getMethod("resize", array.getClass().getInterfaces()[0], long.class);
            while (true) {
                long newSize = array.size() * 2;
                try {
                    array = (BigArray) resize.invoke(bigArrays, array, newSize);
                } catch (InvocationTargetException e) {
                    assertTrue(e.getCause() instanceof CircuitBreakingException);
                    break;
                }
            }
            assertEquals(array.ramBytesUsed(), hcbs.getBreaker(CircuitBreaker.Name.REQUEST).getUsed());
            array.close();
            assertEquals(0, hcbs.getBreaker(CircuitBreaker.Name.REQUEST).getUsed());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/5171.java