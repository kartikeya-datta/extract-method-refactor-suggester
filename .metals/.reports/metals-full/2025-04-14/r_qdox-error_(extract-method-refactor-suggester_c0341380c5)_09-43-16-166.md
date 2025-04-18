error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3700.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3700.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3700.java
text:
```scala
final i@@nt iters = scaledRandomIntBetween(100000, 500000);

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

package org.elasticsearch.search.aggregations.metrics.cardinality;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.hash.MurmurHash3;
import org.elasticsearch.common.util.BigArrays;
import org.elasticsearch.test.ElasticsearchTestCase;
import org.junit.Test;

import static org.elasticsearch.search.aggregations.metrics.cardinality.HyperLogLogPlusPlus.MAX_PRECISION;
import static org.elasticsearch.search.aggregations.metrics.cardinality.HyperLogLogPlusPlus.MIN_PRECISION;
import static org.hamcrest.Matchers.closeTo;

public class HyperLogLogPlusPlusTests extends ElasticsearchTestCase {

    @Test
    public void encodeDecode() {
        final int iters = atLeast(100000);
        // random hashes
        for (int i = 0; i < iters; ++i) {
            final int p1 = randomIntBetween(4, 24);
            final long hash = randomLong();
            testEncodeDecode(p1, hash);
        }
        // special cases
        for (int p1 = MIN_PRECISION; p1 <= MAX_PRECISION; ++p1) {
            testEncodeDecode(p1, 0);
            testEncodeDecode(p1, 1);
            testEncodeDecode(p1, ~0L);
        }
    }

    private void testEncodeDecode(int p1, long hash) {
        final long index = HyperLogLogPlusPlus.index(hash, p1);
        final int runLen = HyperLogLogPlusPlus.runLen(hash, p1);
        final int encoded = HyperLogLogPlusPlus.encodeHash(hash, p1);
        assertEquals(index, HyperLogLogPlusPlus.decodeIndex(encoded, p1));
        assertEquals(runLen, HyperLogLogPlusPlus.decodeRunLen(encoded, p1));
    }

    @Test
    public void accuracy() {
        final long bucket = randomInt(20);
        final int numValues = randomIntBetween(1, 1000000);
        final int maxValue = randomIntBetween(1, randomBoolean() ? 1000: 1000000);
        final int p = randomIntBetween(14, MAX_PRECISION);
        IntOpenHashSet set = new IntOpenHashSet();
        HyperLogLogPlusPlus e = new HyperLogLogPlusPlus(p, BigArrays.NON_RECYCLING_INSTANCE, 1);
        for (int i = 0; i < numValues; ++i) {
            final int n = randomInt(maxValue);
            set.add(n);
            final long hash = MurmurHash3.hash((long) n);
            e.collect(bucket, hash);
            if (randomInt(100) == 0) {
                //System.out.println(e.cardinality(bucket) + " <> " + set.size());
                assertThat((double) e.cardinality(bucket), closeTo(set.size(), 0.1 * set.size()));
            }
        }
        assertThat((double) e.cardinality(bucket), closeTo(set.size(), 0.1 * set.size()));
    }

    @Test
    public void merge() {
        final int p = randomIntBetween(MIN_PRECISION, MAX_PRECISION);
        final HyperLogLogPlusPlus single = new HyperLogLogPlusPlus(p, BigArrays.NON_RECYCLING_INSTANCE, 0);
        final HyperLogLogPlusPlus[] multi = new HyperLogLogPlusPlus[randomIntBetween(2, 100)];
        final long[] bucketOrds = new long[multi.length];
        for (int i = 0; i < multi.length; ++i) {
            bucketOrds[i] = randomInt(20);
            multi[i] = new HyperLogLogPlusPlus(p, BigArrays.NON_RECYCLING_INSTANCE, 5);
        }
        final int numValues = randomIntBetween(1, 100000);
        final int maxValue = randomIntBetween(1, randomBoolean() ? 1000: 1000000);
        for (int i = 0; i < numValues; ++i) {
            final int n = randomInt(maxValue);
            final long hash = MurmurHash3.hash((long) n);
            single.collect(0, hash);
            // use a gaussian so that all instances don't collect as many hashes
            final int index = (int) (Math.pow(randomDouble(), 2));
            multi[index].collect(bucketOrds[index], hash);
            if (randomInt(100) == 0) {
                HyperLogLogPlusPlus merged = new HyperLogLogPlusPlus(p, BigArrays.NON_RECYCLING_INSTANCE, 0);
                for (int j = 0; j < multi.length; ++j) {
                    merged.merge(0, multi[j], bucketOrds[j]);
                }
                assertEquals(single.cardinality(0), merged.cardinality(0));
            }
        }
    }

    @Test
    public void fakeHashes() {
        // hashes with lots of leading zeros trigger different paths in the code that we try to go through here
        final int p = randomIntBetween(MIN_PRECISION, MAX_PRECISION);
        final HyperLogLogPlusPlus counts = new HyperLogLogPlusPlus(p, BigArrays.NON_RECYCLING_INSTANCE, 0);

        counts.collect(0, 0);
        assertEquals(1, counts.cardinality(0));
        if (randomBoolean()) {
            counts.collect(0, 1);
            assertEquals(2, counts.cardinality(0));
        }
        counts.upgradeToHll(0);
        // all hashes felt into the same bucket so hll would expect a count of 1
        assertEquals(1, counts.cardinality(0));
    }

    @Test
    public void precisionFromThreshold() {
        assertEquals(4, HyperLogLogPlusPlus.precisionFromThreshold(0));
        assertEquals(6, HyperLogLogPlusPlus.precisionFromThreshold(10));
        assertEquals(10, HyperLogLogPlusPlus.precisionFromThreshold(100));
        assertEquals(13, HyperLogLogPlusPlus.precisionFromThreshold(1000));
        assertEquals(16, HyperLogLogPlusPlus.precisionFromThreshold(10000));
        assertEquals(18, HyperLogLogPlusPlus.precisionFromThreshold(100000));
        assertEquals(18, HyperLogLogPlusPlus.precisionFromThreshold(1000000));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3700.java