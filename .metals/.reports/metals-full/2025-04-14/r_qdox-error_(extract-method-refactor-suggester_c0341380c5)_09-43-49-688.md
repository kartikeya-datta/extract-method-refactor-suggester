error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2096.java
text:
```scala
public static <@@K, V> FixedSizeSortedMap<K, V> fixedSizeSortedMap(SortedMap<K, V> map) {

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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.collections.BoundedMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.collection.UnmodifiableCollection;
import org.apache.commons.collections.set.UnmodifiableSet;

/**
 * Decorates another <code>SortedMap</code> to fix the size blocking add/remove.
 * <p>
 * Any action that would change the size of the map is disallowed.
 * The put method is allowed to change the value associated with an existing
 * key however.
 * <p>
 * If trying to remove or clear the map, an UnsupportedOperationException is
 * thrown. If trying to put a new mapping into the map, an 
 * IllegalArgumentException is thrown. This is because the put method can 
 * succeed if the mapping's key already exists in the map, so the put method
 * is not always unsupported.
 * <p>
 * <strong>Note that FixedSizeSortedMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedSortedMap}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision$
 *
 * @author Stephen Colebourne
 * @author Paul Jack
 */
public class FixedSizeSortedMap<K, V>
        extends AbstractSortedMapDecorator<K, V>
        implements SortedMap<K, V>, BoundedMap<K, V>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 3126019624511683653L;

    /**
     * Factory method to create a fixed size sorted map.
     * 
     * @param map  the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    public static <K, V> SortedMap<K, V> fixedSizeSortedMap(SortedMap<K, V> map) {
        return new FixedSizeSortedMap<K, V>(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * 
     * @param map  the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    protected FixedSizeSortedMap(SortedMap<K, V> map) {
        super(map);
    }

    /**
     * Gets the map being decorated.
     * 
     * @return the decorated map
     */
    protected SortedMap<K, V> getSortedMap() {
        return (SortedMap<K, V>) map;
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }

    /**
     * Read the map in using a custom routine.
     */
    @SuppressWarnings("unchecked") // (1) should only fail if input stream is incorrect 
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, V>) in.readObject(); // (1)
    }

    //-----------------------------------------------------------------------
    @Override
    public V put(K key, V value) {
        if (map.containsKey(key) == false) {
            throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
        }
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> mapToCopy) {
        if (CollectionUtils.isSubCollection(mapToCopy.keySet(), keySet())) {
            throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size");
        }
        map.putAll(mapToCopy);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Map is fixed size");
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Map is fixed size");
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return UnmodifiableSet.unmodifiableSet(map.entrySet());
    }

    @Override
    public Set<K> keySet() {
        return UnmodifiableSet.unmodifiableSet(map.keySet());
    }

    @Override
    public Collection<V> values() {
        return UnmodifiableCollection.unmodifiableCollection(map.values());
    }

    //-----------------------------------------------------------------------
    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return new FixedSizeSortedMap<K, V>(getSortedMap().subMap(fromKey, toKey));
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return new FixedSizeSortedMap<K, V>(getSortedMap().headMap(toKey));
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return new FixedSizeSortedMap<K, V>(getSortedMap().tailMap(fromKey));
    }

    public boolean isFull() {
        return true;
    }

    public int maxSize() {
        return size();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2096.java