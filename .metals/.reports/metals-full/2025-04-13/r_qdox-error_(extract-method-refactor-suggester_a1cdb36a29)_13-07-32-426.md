error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7284.java
text:
```scala
c@@ursor = new Cursor<>();

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


import com.google.common.collect.UnmodifiableIterator;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.lease.Releasables;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A hash table from native longs to objects. This implementation resolves collisions
 * using open-addressing and does not support null values. This class is not thread-safe.
 */
public class LongObjectPagedHashMap<T> extends AbstractPagedHashMap implements Iterable<LongObjectPagedHashMap.Cursor<T>> {

    private LongArray keys;
    private ObjectArray<T> values;

    public LongObjectPagedHashMap(BigArrays bigArrays) {
        this(16, bigArrays);
    }

    public LongObjectPagedHashMap(long capacity, BigArrays bigArrays) {
        this(capacity, DEFAULT_MAX_LOAD_FACTOR, bigArrays);
    }

    public LongObjectPagedHashMap(long capacity, float maxLoadFactor, BigArrays bigArrays) {
        super(capacity, maxLoadFactor, bigArrays);
        keys = bigArrays.newLongArray(capacity(), false);
        values = bigArrays.newObjectArray(capacity());
    }

    /**
     * Get the value that is associated with <code>key</code> or null if <code>key</code>
     * was not present in the hash table.
     */
    public T get(long key) {
        for (long i = slot(hash(key), mask); ; i = nextSlot(i, mask)) {
            final T value = values.get(i);
            if (value == null) {
                return null;
            } else if (keys.get(i) == key) {
                return value;
            }
        }
    }

    /**
     * Put this new (key, value) pair into this hash table and return the value
     * that was previously associated with <code>key</code> or null in case of
     * an insertion.
     */
    public T put(long key, T value) {
        if (size >= maxSize) {
            assert size == maxSize;
            grow();
        }
        assert size < maxSize;
        return set(key, value);
    }

    /**
     * Remove the entry which has this key in the hash table and return the
     * associated value or null if there was no entry associated with this key.
     */
    public T remove(long key) {
        for (long i = slot(hash(key), mask); ; i = nextSlot(i, mask)) {
            final T previous = values.set(i, null);
            if (previous == null) {
                return null;
            } else if (keys.get(i) == key) {
                --size;
                for (long j = nextSlot(i, mask); used(j); j = nextSlot(j, mask)) {
                    removeAndAdd(j);
                }
                return previous;
            } else {
                // repair and continue
                values.set(i, previous);
            }
        }
    }

    private T set(long key, T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not supported");
        }
        for (long i = slot(hash(key), mask); ; i = nextSlot(i, mask)) {
            final T previous = values.set(i, value);
            if (previous == null) {
                // slot was free
                keys.set(i, key);
                ++size;
                return null;
            } else if (key == keys.get(i)) {
                // we just updated the value
                return previous;
            } else {
                // not the right key, repair and continue
                values.set(i, previous);
            }
        }
    }

    @Override
    public Iterator<Cursor<T>> iterator() {
        return new UnmodifiableIterator<Cursor<T>>() {

            boolean cached;
            final Cursor<T> cursor;
            {
                cursor = new Cursor<T>();
                cursor.index = -1;
                cached = false;
            }

            @Override
            public boolean hasNext() {
                if (!cached) {
                    while (true) {
                        ++cursor.index;
                        if (cursor.index >= capacity()) {
                            break;
                        } else if (used(cursor.index)) {
                            cursor.key = keys.get(cursor.index);
                            cursor.value = values.get(cursor.index);
                            break;
                        }
                    }
                    cached = true;
                }
                return cursor.index < capacity();
            }

            @Override
            public Cursor<T> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                cached = false;
                return cursor;
            }

        };
    }

    @Override
    public boolean release() throws ElasticsearchException {
        Releasables.release(keys, values);
        return true;
    }

    @Override
    protected void resize(long capacity) {
        keys = bigArrays.resize(keys, capacity);
        values = bigArrays.resize(values, capacity);
    }

    @Override
    protected boolean used(long bucket) {
        return values.get(bucket) != null;
    }

    @Override
    protected void removeAndAdd(long index) {
        final long key = keys.get(index);
        final T value = values.set(index, null);
        --size;
        final T removed = set(key, value);
        assert removed == null;
    }

    public static final class Cursor<T> {
        public long index;
        public long key;
        public T value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7284.java