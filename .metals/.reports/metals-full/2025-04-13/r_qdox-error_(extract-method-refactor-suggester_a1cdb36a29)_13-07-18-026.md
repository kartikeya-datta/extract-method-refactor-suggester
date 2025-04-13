error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7258.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7258.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7258.java
text:
```scala
r@@eturn new ObjectObjectOpenHashMap<>(capacity);

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

package org.elasticsearch.common.collect;

import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.ObjectLookupContainer;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import org.elasticsearch.ElasticsearchIllegalArgumentException;

import java.util.Iterator;

/**
 */
public final class HppcMaps {

    private HppcMaps() {
    }

    /**
     * Returns a new map with the given initial capacity
     */
    public static <K, V> ObjectObjectOpenHashMap<K, V> newMap(int capacity) {
        return new ObjectObjectOpenHashMap<K, V>(capacity);
    }

    /**
     * Returns a new map with a default initial capacity of
     * {@value com.carrotsearch.hppc.HashContainerUtils#DEFAULT_CAPACITY}
     */
    public static <K, V> ObjectObjectOpenHashMap<K, V> newMap() {
        return newMap(16);
    }

    /**
     * Returns a map like {@link #newMap()} that does not accept <code>null</code> keys
     */
    public static <K, V> ObjectObjectOpenHashMap<K, V> newNoNullKeysMap() {
        return ensureNoNullKeys(16);
    }

    /**
     * Returns a map like {@link #newMap(int)} that does not accept <code>null</code> keys
     */
    public static <K, V> ObjectObjectOpenHashMap<K, V> newNoNullKeysMap(int capacity) {
        return ensureNoNullKeys(capacity);
    }

    /**
     * Wraps the given map and prevent adding of <code>null</code> keys.
     */
    public static <K, V> ObjectObjectOpenHashMap<K, V> ensureNoNullKeys(int capacity) {
        return new ObjectObjectOpenHashMap<K, V>(capacity) {

            @Override
            public V put(K key, V value) {
                if (key == null) {
                    throw new ElasticsearchIllegalArgumentException("Map key must not be null");
                }
                return super.put(key, value);
            }

        };
    }

    /**
     * @return an intersection view over the two specified containers (which can be KeyContainer or ObjectOpenHashSet).
     */
    // Hppc has forEach, but this means we need to build an intermediate set, with this method we just iterate
    // over each unique value without creating a third set.
    public static <T> Iterable<T> intersection(ObjectLookupContainer<T> container1, final ObjectLookupContainer<T> container2) {
        assert container1 != null && container2 != null;
        final Iterator<ObjectCursor<T>> iterator = container1.iterator();
        final Iterator<T> intersection = new Iterator<T>() {

            T current;

            @Override
            public boolean hasNext() {
                if (iterator.hasNext()) {
                    do {
                        T next = iterator.next().value;
                        if (container2.contains(next)) {
                            current = next;
                            return true;
                        }
                    } while (iterator.hasNext());
                }
                return false;
            }

            @Override
            public T next() {
                return current;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return intersection;
            }
        };
    }

    public final static class Object {

        public final static class Integer {

            public static <V> ObjectIntOpenHashMap<V> ensureNoNullKeys(int capacity, float loadFactor) {
                return new ObjectIntOpenHashMap<V>(capacity, loadFactor) {

                    @Override
                    public int put(V key, int value) {
                        if (key == null) {
                            throw new ElasticsearchIllegalArgumentException("Map key must not be null");
                        }
                        return super.put(key, value);
                    }
                };
            }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/7258.java