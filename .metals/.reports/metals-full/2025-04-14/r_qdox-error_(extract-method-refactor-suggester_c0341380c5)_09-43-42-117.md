error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8558.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8558.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8558.java
text:
```scala
protected M@@ultiMapUtils() {

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
package org.apache.commons.collections4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.multimap.MultiValuedHashMap;
import org.apache.commons.collections4.multimap.TransformedMultiValuedMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

/**
 * Provides utility methods and decorators for {@link MultiValuedMap} instances.
 * <p>
 * It contains various type safe and null safe methods.
 * <p>
 * It also provides the following decorators:
 *
 * <ul>
 *   <li>{@link #unmodifiableMultiValuedMap(MultiValuedMap)}</li>
 *   <li>{@link #transformedMultiValuedMap(MultiValuedMap, Transformer, Transformer)}</li>
 * </ul>
 *
 * @since 4.1
 * @version $Id$
 */
public class MultiMapUtils {

    /**
     * <code>MultiMapUtils</code> should not normally be instantiated.
     */
    private MultiMapUtils() {
    }

    /**
     * An empty {@link UnmodifiableMultiValuedMap}.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static final MultiValuedMap EMPTY_MULTI_VALUED_MAP =
            UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(new MultiValuedHashMap());

    /**
     * Returns immutable EMPTY_MULTI_VALUED_MAP with generic type safety.
     *
     * @param <K> the type of key in the map
     * @param <V> the type of value in the map
     * @return immutable and empty <code>MultiValuedMap</code>
     */
    @SuppressWarnings("unchecked")
    public static <K, V> MultiValuedMap<K, V> emptyMultiValuedMap() {
        return EMPTY_MULTI_VALUED_MAP;
    }

    // Null safe methods

    /**
     * Returns an immutable empty <code>MultiValuedMap</code> if the argument is
     * <code>null</code>, or the argument itself otherwise.
     *
     * @param <K> the type of key in the map
     * @param <V> the type of value in the map
     * @param map the map, possibly <code>null</code>
     * @return an empty <code>MultiValuedMap</code> if the argument is <code>null</code>
     */
    @SuppressWarnings("unchecked")
    public static <K, V> MultiValuedMap<K, V> emptyIfNull(final MultiValuedMap<K, V> map) {
        return map == null ? EMPTY_MULTI_VALUED_MAP : map;
    }

    /**
     * Null-safe check if the specified <code>MultiValuedMap</code> is empty.
     * <p>
     * Null returns true.
     *
     * @param map the map to check, may be null
     * @return true if empty or null
     */
    public static boolean isEmpty(final MultiValuedMap<?, ?> map) {
        return map == null || map.isEmpty();
    }

    // Null safe getters
    // -------------------------------------------------------------------------

    /**
     * Gets a Collection from <code>MultiValuedMap</code> in a null-safe manner.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to use
     * @param key the key to look up
     * @return the Collection in the <code>MultiValuedMap</code>, <code>null</code> if map input is null
     */
    public static <K, V> Collection<V> getCollection(final MultiValuedMap<K, V> map, final K key) {
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    /**
     * Gets a List from <code>MultiValuedMap</code> in a null-safe manner.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to use
     * @param key the key to look up
     * @return the Collection in the <code>MultiValuedMap</code> as List,
     *         <code>null</code> if map input is null
     */
    public static <K, V> List<V> getList(MultiValuedMap<K, V> map, K key) {
        if (map != null) {
            Collection<V> col = map.get(key);
            if (col instanceof List) {
                return (List<V>) col;
            }
            return new ArrayList<V>(col);
        }
        return null;
    }

    /**
     * Gets a Set from <code>MultiValuedMap</code> in a null-safe manner.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to use
     * @param key the key to look up
     * @return the Collection in the <code>MultiValuedMap</code> as Set,
     *         <code>null</code> if map input is null
     */
    public static <K, V> Set<V> getSet(MultiValuedMap<K, V> map, K key) {
        if (map != null) {
            Collection<V> col =  map.get(key);
            if (col instanceof Set) {
                return (Set<V>) col;
            }
            return new HashSet<V>(col);
        }
        return null;
    }

    /**
     * Gets a Bag from <code>MultiValuedMap</code> in a null-safe manner.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to use
     * @param key the key to look up
     * @return the Collection in the <code>MultiValuedMap</code> as Bag,
     *         <code>null</code> if map input is null
     */
    public static <K, V> Bag<V> getBag(MultiValuedMap<K, V> map, K key) {
        if (map != null) {
            Collection<V> col = map.get(key);
            if (col instanceof Bag) {
                return (Bag<V>) col;
            }
            return new HashBag<V>(col);
        }
        return null;
    }

    // Factory Methods
    // -----------------------------------------------------------------------

    /**
     * Creates a {@link ListValuedMap} with a {@link HashMap} as its internal storage.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a new <code>ListValuedMap</code>
     */
    public static <K, V> ListValuedMap<K, V> createListValuedHashMap() {
        return MultiValuedHashMap.<K, V>listValuedHashMap();
    }

    /**
     * Creates a {@link ListValuedMap} with a {@link HashMap} as its internal
     * storage which maps the keys to list of type <code>listClass</code>.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param <C> the List class type
     * @param listClass the class of the list
     * @return a new <code>ListValuedMap</code>
     */
    public static <K, V, C extends List<V>> ListValuedMap<K, V> createListValuedHashMap(final Class<C> listClass) {
        return MultiValuedHashMap.<K, V, C>listValuedHashMap(listClass);
    }

    /**
     * Creates a {@link SetValuedMap} with a {@link HashMap} as its internal
     * storage
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return a new <code>SetValuedMap</code>
     */
    public static <K, V> SetValuedMap<K, V> createSetValuedHashMap() {
        return MultiValuedHashMap.<K, V>setValuedHashMap();
    }

    /**
     * Creates a {@link SetValuedMap} with a {@link HashMap} as its internal
     * storage which maps the keys to a set of type <code>setClass</code>
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param <C> the Set class type
     * @param setClass the class of the set
     * @return a new <code>SetValuedMap</code>
     */
    public static <K, V, C extends Set<V>> SetValuedMap<K, V> createSetValuedHashMap(final Class<C> setClass) {
        return MultiValuedHashMap.<K, V, C>setValuedHashMap(setClass);
    }

    // MultiValuedMap Decorators
    // -----------------------------------------------------------------------

    /**
     * Returns an <code>UnmodifiableMultiValuedMap</code> backed by the given
     * map.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to make unmodifiable, must not
     *        be null
     * @return an <code>UnmodifiableMultiValuedMap</code> backed by the given
     *         map
     * @throws IllegalArgumentException if the map is null
     */
    public static <K, V> MultiValuedMap<K, V> unmodifiableMultiValuedMap(
            final MultiValuedMap<? extends K, ? extends V> map) {
        return UnmodifiableMultiValuedMap.<K, V>unmodifiableMultiValuedMap(map);
    }

    /**
     * Returns a <code>TransformedMultiValuedMap</code> backed by the given map.
     * <p>
     * This method returns a new <code>MultiValuedMap</code> (decorating the
     * specified map) that will transform any new entries added to it. Existing
     * entries in the specified map will not be transformed. If you want that
     * behaviour, see {@link TransformedMultiValuedMap#transformedMap}.
     * <p>
     * Each object is passed through the transformers as it is added to the Map.
     * It is important not to use the original map after invoking this method,
     * as it is a back door for adding untransformed objects.
     * <p>
     * If there are any elements already in the map being decorated, they are
     * NOT transformed.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the <code>MultiValuedMap</code> to transform, must not be
     *        null, typically empty
     * @param keyTransformer the transformer for the map keys, null means no
     *        transformation
     * @param valueTransformer the transformer for the map values, null means no
     *        transformation
     * @return a transformed <code>MultiValuedMap</code> backed by the given map
     * @throws IllegalArgumentException if the <code>MultiValuedMap</code> is
     *         null
     */
    public static <K, V> MultiValuedMap<K, V> transformedMultiValuedMap(final MultiValuedMap<K, V> map,
            final Transformer<? super K, ? extends K> keyTransformer,
            final Transformer<? super V, ? extends V> valueTransformer) {
        return TransformedMultiValuedMap.transformingMap(map, keyTransformer, valueTransformer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8558.java