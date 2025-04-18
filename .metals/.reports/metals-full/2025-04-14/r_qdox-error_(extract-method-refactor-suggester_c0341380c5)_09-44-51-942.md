error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13294.java
text:
```scala
r@@eturn result ? value : null;

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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.FunctorException;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.IteratorChain;

/**
 * A MultiValueMap decorates another map, allowing it to have
 * more than one value for a key.
 * <p>
 * A <code>MultiMap</code> is a Map with slightly different semantics.
 * Putting a value into the map will add the value to a Collection at that key.
 * Getting a value will return a Collection, holding all the values put to that key.
 * <p>
 * This implementation is a decorator, allowing any Map implementation
 * to be used as the base.
 * <p>
 * In addition, this implementation allows the type of collection used
 * for the values to be controlled. By default, an <code>ArrayList</code>
 * is used, however a <code>Class</code> to instantiate may be specified,
 * or a factory that returns a <code>Collection</code> instance.
 * <p>
 * <strong>Note that MultiValueMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. This class may throw exceptions when accessed
 * by concurrent threads without synchronization.
 *
 * @since 3.2
 * @version $Id$
 */
public class MultiValueMap<K, V> extends AbstractMapDecorator<K, Object> implements MultiMap<K, V>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -2214159910087182007L;

    /** The factory for creating value collections. */
    private final Factory<? extends Collection<V>> collectionFactory;
    /** The cached values. */
    private transient Collection<V> valuesView;

    /**
     * Creates a map which wraps the given map and
     * maps keys to ArrayLists.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param map  the map to wrap
     * @return a new multi-value map
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <K, V> MultiValueMap<K, V> multiValueMap(Map<K, ? super Collection<V>> map) {
        return MultiValueMap.<K, V, ArrayList> multiValueMap((Map<K, ? super Collection>) map, ArrayList.class);
    }

    /**
     * Creates a map which decorates the given <code>map</code> and
     * maps keys to collections of type <code>collectionClass</code>.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param map  the map to wrap
     * @param collectionClass  the type of the collection class
     * @return a new multi-value map
     */
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, ? super C> map,
                                                                                    Class<C> collectionClass) {
        return new MultiValueMap<K, V>(map, new ReflectionFactory<C>(collectionClass));
    }

    /**
     * Creates a map which decorates the given <code>map</code> and
     * creates the value collections using the supplied <code>collectionFactory</code>.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param map  the map to decorate
     * @param collectionFactory  the collection factory (must return a Collection object).
     * @return a new multi-value map
     */
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, ? super C> map,
                                                                                    Factory<C> collectionFactory) {
        return new MultiValueMap<K, V>(map, collectionFactory);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a MultiValueMap based on a <code>HashMap</code> and
     * storing the multiple values in an <code>ArrayList</code>.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MultiValueMap() {
        this(new HashMap<K, V>(), new ReflectionFactory(ArrayList.class));
    }

    /**
     * Creates a MultiValueMap which decorates the given <code>map</code> and
     * creates the value collections using the supplied <code>collectionFactory</code>.
     *
     * @param map  the map to decorate
     * @param collectionFactory  the collection factory which must return a Collection instance
     */
    @SuppressWarnings("unchecked")
    protected <C extends Collection<V>> MultiValueMap(Map<K, ? super C> map, Factory<C> collectionFactory) {
        super((Map<K, Object>) map);
        if (collectionFactory == null) {
            throw new IllegalArgumentException("The factory must not be null");
        }
        this.collectionFactory = collectionFactory;
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
     * @since 3.3
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(map);
    }

    /**
     * Read the map in using a custom routine.
     * 
     * @param in  the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     * @since 3.3
     */
    @SuppressWarnings("unchecked") // (1) should only fail if input stream is incorrect 
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map<K, Object>) in.readObject(); // (1)
    }

    //-----------------------------------------------------------------------
    /**
     * Clear the map.
     */
    @Override
    public void clear() {
        // If you believe that you have GC issues here, try uncommenting this code
//        Set pairs = getMap().entrySet();
//        Iterator pairsIterator = pairs.iterator();
//        while (pairsIterator.hasNext()) {
//            Map.Entry keyValuePair = (Map.Entry) pairsIterator.next();
//            Collection coll = (Collection) keyValuePair.getValue();
//            coll.clear();
//        }
        decorated().clear();
    }

    /**
     * Removes a specific value from map.
     * <p>
     * The item is removed from the collection mapped to the specified key.
     * Other values attached to that key are unaffected.
     * <p>
     * If the last value for a key is removed, <code>null</code> will be returned
     * from a subsequant <code>get(key)</code>.
     *
     * @param key  the key to remove from
     * @param value the value to remove
     * @return the value removed (which was passed in), null if nothing removed
     */
    @SuppressWarnings("unchecked")
    public V remove(Object key, Object value) {
        Collection<V> valuesForKey = getCollection(key);
        if (valuesForKey == null) {
            return null;
        }
        boolean removed = valuesForKey.remove(value);
        if (removed == false) {
            return null;
        }
        if (valuesForKey.isEmpty()) {
            remove(key);
        }
        return (V) value;
    }

    /**
     * Checks whether the map contains the value specified.
     * <p>
     * This checks all collections against all keys for the value, and thus could be slow.
     *
     * @param value  the value to search for
     * @return true if the map contains the value
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        Set<Map.Entry<K, Object>> pairs = decorated().entrySet();
        if (pairs != null) {
            for (Map.Entry<K, Object> entry : pairs) {
                if (((Collection<V>) entry.getValue()).contains(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Adds the value to the collection associated with the specified key.
     * <p>
     * Unlike a normal <code>Map</code> the previous value is not replaced.
     * Instead the new value is added to the collection stored against the key.
     *
     * @param key  the key to store against
     * @param value  the value to add to the collection at the key
     * @return the value added if the map changed and null if the map did not change
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object put(K key, Object value) {
        boolean result = false;
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(1);  // might produce a non-empty collection
            coll.add((V) value);
            if (coll.size() > 0) {
                // only add if non-zero size to maintain class state
                decorated().put(key, coll);
                result = true;  // map definitely changed
            }
        } else {
            result = coll.add((V) value);
        }
        return (result ? value : null);
    }

    /**
     * Override superclass to ensure that MultiMap instances are
     * correctly handled.
     * <p>
     * If you call this method with a normal map, each entry is
     * added using <code>put(Object,Object)</code>.
     * If you call this method with a multi map, each entry is
     * added using <code>putAll(Object,Collection)</code>.
     *
     * @param map  the map to copy (either a normal or multi map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ?> map) {
        if (map instanceof MultiMap) {
            for (Map.Entry<? extends K, Object> entry : ((MultiMap<? extends K, V>) map).entrySet()) {
                putAll(entry.getKey(), (Collection<V>) entry.getValue());
            }
        } else {
            for (Map.Entry<? extends K, ?> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Gets a collection containing all the values in the map.
     * <p>
     * This returns a collection containing the combination of values from all keys.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Object> values() {
        Collection<V> vs = valuesView;
        return (Collection<Object>) (vs != null ? vs : (valuesView = new Values()));
    }

    /**
     * Checks whether the collection at the specified key contains the value.
     *
     * @param value  the value to search for
     * @return true if the map contains the value
     */
    public boolean containsValue(Object key, Object value) {
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            return false;
        }
        return coll.contains(value);
    }

    /**
     * Gets the collection mapped to the specified key.
     * This method is a convenience method to typecast the result of <code>get(key)</code>.
     *
     * @param key  the key to retrieve
     * @return the collection mapped to the key, null if no mapping
     */
    @SuppressWarnings("unchecked")
    public Collection<V> getCollection(Object key) {
        return (Collection<V>) decorated().get(key);
    }

    /**
     * Gets the size of the collection mapped to the specified key.
     *
     * @param key  the key to get size for
     * @return the size of the collection at the key, zero if key not in map
     */
    public int size(Object key) {
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            return 0;
        }
        return coll.size();
    }

    /**
     * Adds a collection of values to the collection associated with
     * the specified key.
     *
     * @param key  the key to store against
     * @param values  the values to add to the collection at the key, null ignored
     * @return true if this map changed
     */
    public boolean putAll(K key, Collection<V> values) {
        if (values == null || values.size() == 0) {
            return false;
        }
        boolean result = false;
        Collection<V> coll = getCollection(key);
        if (coll == null) {
            coll = createCollection(values.size());  // might produce a non-empty collection
            coll.addAll(values);
            if (coll.size() > 0) {
                // only add if non-zero size to maintain class state
                decorated().put(key, coll);
                result = true;  // map definitely changed
            }
        } else {
            result = coll.addAll(values);
        }
        return result;
    }

    /**
     * Gets an iterator for the collection mapped to the specified key.
     *
     * @param key  the key to get an iterator for
     * @return the iterator of the collection at the key, empty iterator if key not in map
     */
    public Iterator<V> iterator(Object key) {
        if (!containsKey(key)) {
            return EmptyIterator.<V>emptyIterator();
        }
        return new ValuesIterator(key);
    }

    /**
     * Gets the total size of the map by counting all the values.
     *
     * @return the total size of the map counting all values
     */
    public int totalSize() {
        int total = 0;
        for (Object v : decorated().values()) {
            total += CollectionUtils.size(v);
        }
        return total;
    }

    /**
     * Creates a new instance of the map value Collection container
     * using the factory.
     * <p>
     * This method can be overridden to perform your own processing
     * instead of using the factory.
     *
     * @param size  the collection size that is about to be added
     * @return the new collection
     */
    protected Collection<V> createCollection(int size) {
        return collectionFactory.create();
    }

    //-----------------------------------------------------------------------
    /**
     * Inner class that provides the values view.
     */
    private class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            final IteratorChain<V> chain = new IteratorChain<V>();
            for (K k : keySet()) {
                chain.addIterator(new ValuesIterator(k));
            }
            return chain;
        }

        @Override
        public int size() {
            return totalSize();
        }

        @Override
        public void clear() {
            MultiValueMap.this.clear();
        }
    }

    /**
     * Inner class that provides the values iterator.
     */
    private class ValuesIterator implements Iterator<V> {
        private final Object key;
        private final Collection<V> values;
        private final Iterator<V> iterator;

        public ValuesIterator(Object key) {
            this.key = key;
            this.values = getCollection(key);
            this.iterator = values.iterator();
        }

        public void remove() {
            iterator.remove();
            if (values.isEmpty()) {
                MultiValueMap.this.remove(key);
            }
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public V next() {
            return iterator.next();
        }
    }

    /**
     * Inner class that provides a simple reflection factory.
     */
    private static class ReflectionFactory<T extends Collection<?>> implements Factory<T>, Serializable {

        /** Serialization version */
        private static final long serialVersionUID = 2986114157496788874L;

        private final Class<T> clazz;

        public ReflectionFactory(Class<T> clazz) {
            this.clazz = clazz;
        }

        public T create() {
            try {
                return clazz.newInstance();
            } catch (Exception ex) {
                throw new FunctorException("Cannot instantiate class: " + clazz, ex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13294.java