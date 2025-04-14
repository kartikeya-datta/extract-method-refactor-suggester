error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7437.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7437.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7437.java
text:
```scala
L@@ist<V<byte[]>> vals = new ArrayList<>(limit);

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

package org.elasticsearch.common.recycler;

import org.elasticsearch.ElasticsearchIllegalStateException;
import org.elasticsearch.common.recycler.Recycler.V;
import org.elasticsearch.test.ElasticsearchTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractRecyclerTests extends ElasticsearchTestCase {

    // marker states for data
    protected static final byte FRESH = 1;
    protected static final byte RECYCLED = 2;
    protected static final byte DEAD = 42;

    protected static final Recycler.C<byte[]> RECYCLER_C = new AbstractRecyclerC<byte[]>() {

        @Override
        public byte[] newInstance(int sizing) {
            byte[] value = new byte[10];
            // "fresh" is intentionally not 0 to ensure we covered this code path
            Arrays.fill(value, FRESH);
            return value;
        }

        @Override
        public void recycle(byte[] value) {
            Arrays.fill(value, RECYCLED);
        }

        @Override
        public void destroy(byte[] value) {
            // we cannot really free the internals of a byte[], so mark it for verification
            Arrays.fill(value, DEAD);
        }

    };

    protected void assertFresh(byte[] data) {
        assertNotNull(data);
        for (int i = 0; i < data.length; ++i) {
            assertEquals(FRESH, data[i]);
        }
    }

    protected void assertRecycled(byte[] data) {
        assertNotNull(data);
        for (int i = 0; i < data.length; ++i) {
            assertEquals(RECYCLED, data[i]);
        }
    }

    protected void assertDead(byte[] data) {
        assertNotNull(data);
        for (int i = 0; i < data.length; ++i) {
            assertEquals(DEAD, data[i]);
        }
    }

    protected abstract Recycler<byte[]> newRecycler(int limit);

    protected int limit = randomIntBetween(5, 10);

    public void testReuse() {
        Recycler<byte[]> r = newRecycler(limit);
        Recycler.V<byte[]> o = r.obtain();
        assertFalse(o.isRecycled());
        final byte[] b1 = o.v();
        assertFresh(b1);
        o.release();
        assertRecycled(b1);
        o = r.obtain();
        final byte[] b2 = o.v();
        if (o.isRecycled()) {
            assertRecycled(b2);
            assertSame(b1, b2);
        } else {
            assertFresh(b2);
            assertNotSame(b1, b2);
        }
        o.release();
        r.close();
    }

    public void testRecycle() {
        Recycler<byte[]> r = newRecycler(limit);
        Recycler.V<byte[]> o = r.obtain();
        assertFresh(o.v());
        getRandom().nextBytes(o.v());
        o.release();
        o = r.obtain();
        assertRecycled(o.v());
        o.release();
        r.close();
    }

    public void testDoubleRelease() {
        final Recycler<byte[]> r = newRecycler(limit);
        final Recycler.V<byte[]> v1 = r.obtain();
        v1.release();
        try {
            v1.release();
        } catch (ElasticsearchIllegalStateException e) {
            // impl has protection against double release: ok
            return;
        }
        // otherwise ensure that the impl may not be returned twice
        final Recycler.V<byte[]> v2 = r.obtain();
        final Recycler.V<byte[]> v3 = r.obtain();
        assertNotSame(v2.v(), v3.v());
        r.close();
    }

    public void testDestroyWhenOverCapacity() {
        Recycler<byte[]> r = newRecycler(limit);

        // get & keep reference to new/recycled data
        Recycler.V<byte[]> o = r.obtain();
        byte[] data = o.v();
        assertFresh(data);

        // now exhaust the recycler
        List<V<byte[]>> vals = new ArrayList<V<byte[]>>(limit);
        for (int i = 0; i < limit ; ++i) {
            vals.add(r.obtain());
        }
        // Recycler size increases on release, not on obtain!
        for (V<byte[]> v: vals) {
            v.release();
        }

        // release first ref, verify for destruction
        o.release();
        assertDead(data);

        // close the rest
        r.close();
    }

    public void testClose() {
        Recycler<byte[]> r = newRecycler(limit);

        // get & keep reference to pooled data
        Recycler.V<byte[]> o = r.obtain();
        byte[] data = o.v();
        assertFresh(data);

        // randomize & return to pool
        getRandom().nextBytes(data);
        o.release();

        // verify that recycle() ran
        assertRecycled(data);

        // closing the recycler should mark recycled instances via destroy()
        r.close();
        assertDead(data);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7437.java