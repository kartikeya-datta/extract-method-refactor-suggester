error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12617.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12617.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12617.java
text:
```scala
S@@tringBuilder buf = new StringBuilder();

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
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.AbstractUntypedIteratorDecorator;
import org.apache.commons.collections.keyvalue.AbstractMapEntry;
import org.apache.commons.collections.list.UnmodifiableList;

/**
 * Decorates a <code>Map</code> to ensure that the order of addition is retained
 * using a <code>List</code> to maintain order.
 * <p>
 * The order will be used via the iterators and toArray methods on the views.
 * The order is also returned by the <code>MapIterator</code>.
 * The <code>orderedMapIterator()</code> method accesses an iterator that can
 * iterate both forwards and backwards through the map.
 * In addition, non-interface methods are provided to access the map by index.
 * <p>
 * If an object is added to the Map for a second time, it will remain in the
 * original position in the iteration.
 * <p>
 * <strong>Note that ListOrderedMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap(Map)}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 * @author Matt Benson
 * @author Dave Meikle
 */
public class ListOrderedMap<K, V>
        extends AbstractMapDecorator<K, V>
        implements OrderedMap<K, V>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 2728177751851003750L;

    /** Internal list to hold the sequence of objects */
    protected final List<K> insertOrder = new ArrayList<K>();

    /**
     * Factory method to create an ordered map.
     * <p>
     * An <code>ArrayList</code> is used to retain order.
     * 
     * @param map  the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    public static <K, V> OrderedMap<K, V> decorate(Map<K, V> map) {
        return new ListOrderedMap<K, V>(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a new empty <code>ListOrderedMap</code> that decorates
     * a <code>HashMap</code>.
     * 
     * @since Commons Collections 3.1
     */
    public ListOrderedMap() {
        this(new HashMap<K, V>());
    }

    /**
     * Constructor that wraps (not copies).
     * 
     * @param map  the map to decorate, must not be null
     * @throws IllegalArgumentException if map is null
     */
    protected ListOrderedMap(Map<K, V> map) {
        super(map);
        insertOrder.addAll(decorated().keySet());
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
     * @since Commons Collections 3.1
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
     * @since Commons Collections 3.1
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map) in.readObject();
    }

    // Implement OrderedMap
    //-----------------------------------------------------------------------
    public OrderedMapIterator<K, V> mapIterator() {
        return new ListOrderedMapIterator<K, V>(this);
    }

    /**
     * Gets the first key in this map by insert order.
     *
     * @return the first key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public K firstKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(0);
    }

    /**
     * Gets the last key in this map by insert order.
     *
     * @return the last key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public K lastKey() {
        if (size() == 0) {
            throw new NoSuchElementException("Map is empty");
        }
        return insertOrder.get(size() - 1);
    }
    
    /**
     * Gets the next key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     * 
     * @param key  the key to find previous for
     * @return the next key, null if no match or at start
     */
    public K nextKey(Object key) {
        int index = insertOrder.indexOf(key);
        if (index >= 0 && index < size() - 1) {
            return insertOrder.get(index + 1);
        }
        return null;
    }

    /**
     * Gets the previous key to the one specified using insert order.
     * This method performs a list search to find the key and is O(n).
     * 
     * @param key  the key to find previous for
     * @return the previous key, null if no match or at start
     */
    public K previousKey(Object key) {
        int index = insertOrder.indexOf(key);
        if (index > 0) {
            return insertOrder.get(index - 1);
        }
        return null;
    }

    //-----------------------------------------------------------------------
    public V put(K key, V value) {
        if (decorated().containsKey(key)) {
            // re-adding doesn't change order
            return decorated().put(key, value);
        } else {
            // first add, so add to both map and list
            V result = decorated().put(key, value);
            insertOrder.add(key);
            return result;
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Puts the values contained in a supplied Map into the Map starting at
     * the specified index.
     *
     * @param index the index in the Map to start at.
     * @param map the Map containing the values to be added.
     */
    public void putAll(int index, Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(index, entry.getKey(), entry.getValue());
            index++;
        }
    }

    public V remove(Object key) {
        V result = decorated().remove(key);
        insertOrder.remove(key);
        return result;
    }

    public void clear() {
        decorated().clear();
        insertOrder.clear();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets a view over the keys in the map.
     * <p>
     * The Collection will be ordered by object insertion into the map.
     *
     * @see #keyList()
     * @return the fully modifiable collection view over the keys
     */
    public Set<K> keySet() {
        return new KeySetView<K>(this);
    }

    /**
     * Gets a view over the keys in the map as a List.
     * <p>
     * The List will be ordered by object insertion into the map.
     * The List is unmodifiable.
     *
     * @see #keySet()
     * @return the unmodifiable list view over the keys
     * @since Commons Collections 3.2
     */
    public List<K> keyList() {
        return UnmodifiableList.decorate(insertOrder);
    }

    /**
     * Gets a view over the values in the map.
     * <p>
     * The Collection will be ordered by object insertion into the map.
     * <p>
     * From Commons Collections 3.2, this Collection can be cast
     * to a list, see {@link #valueList()}
     *
     * @see #valueList()
     * @return the fully modifiable collection view over the values
     */
    public Collection<V> values() {
        return new ValuesView<V>(this);
    }

    /**
     * Gets a view over the values in the map as a List.
     * <p>
     * The List will be ordered by object insertion into the map.
     * The List supports remove and set, but does not support add.
     *
     * @see #values()
     * @return the partially modifiable list view over the values
     * @since Commons Collections 3.2
     */
    public List<V> valueList() {
        return new ValuesView<V>(this);
    }

    /**
     * Gets a view over the entries in the map.
     * <p>
     * The Set will be ordered by object insertion into the map.
     *
     * @return the fully modifiable set view over the entries
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySetView<K, V>(this, this.insertOrder);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the Map as a string.
     * 
     * @return the Map as a String
     */
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuffer buf = new StringBuffer();
        buf.append('{');
        boolean first = true;
        for (Map.Entry<K, V> entry : entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (first) {
                first = false;
            } else {
                buf.append(", ");
            }
            buf.append(key == this ? "(this Map)" : key);
            buf.append('=');
            buf.append(value == this ? "(this Map)" : value);
        }
        buf.append('}');
        return buf.toString();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the key at the specified index.
     * 
     * @param index  the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public K get(int index) {
        return insertOrder.get(index);
    }
    
    /**
     * Gets the value at the specified index.
     * 
     * @param index  the index to retrieve
     * @return the key at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public V getValue(int index) {
        return get(insertOrder.get(index));
    }
    
    /**
     * Gets the index of the specified key.
     * 
     * @param key  the key to find the index of
     * @return the index, or -1 if not found
     */
    public int indexOf(Object key) {
        return insertOrder.indexOf(key);
    }

    /**
     * Sets the value at the specified index.
     *
     * @param index  the index of the value to set
     * @return the previous value at that index
     * @throws IndexOutOfBoundsException if the index is invalid
     * @since Commons Collections 3.2
     */
    public V setValue(int index, V value) {
        K key = insertOrder.get(index);
        return put(key, value);
    }

    /**
     * Puts a key-value mapping into the map at the specified index.
     * <p>
     * If the map already contains the key, then the original mapping
     * is removed and the new mapping added at the specified index.
     * The remove may change the effect of the index. The index is
     * always calculated relative to the original state of the map.
     * <p>
     * Thus the steps are: (1) remove the existing key-value mapping,
     * then (2) insert the new key-value mapping at the position it
     * would have been inserted had the remove not ocurred.
     *
     * @param index  the index at which the mapping should be inserted
     * @param key  the key
     * @param value  the value
     * @return the value previously mapped to the key
     * @throws IndexOutOfBoundsException if the index is out of range
     * @since Commons Collections 3.2
     */
    public V put(int index, K key, V value) {
        Map<K, V> m = decorated();
        if (m.containsKey(key)) {
            V result = m.remove(key);
            int pos = insertOrder.indexOf(key);
            insertOrder.remove(pos);
            if (pos < index) {
                index--;
            }
            insertOrder.add(index, key);
            m.put(key, value);
            return result;
        } else {
            insertOrder.add(index, key);
            m.put(key, value);
            return null;
        }
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index  the index of the object to remove
     * @return the removed value, or <code>null</code> if none existed
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public V remove(int index) {
        return remove(get(index));
    }

    /**
     * Gets an unmodifiable List view of the keys which changes as the map changes.
     * <p>
     * The returned list is unmodifiable because changes to the values of
     * the list (using {@link java.util.ListIterator#set(Object)}) will
     * effectively remove the value from the list and reinsert that value at
     * the end of the list, which is an unexpected side effect of changing the
     * value of a list.  This occurs because changing the key, changes when the
     * mapping is added to the map and thus where it appears in the list.
     * <p>
     * An alternative to this method is to use the better named
     * {@link #keyList()} or {@link #keySet()}.
     *
     * @see #keyList()
     * @see #keySet()
     * @return The ordered list of keys.  
     */
    public List<K> asList() {
        return keyList();
    }

    //-----------------------------------------------------------------------
    static class ValuesView<V> extends AbstractList<V> {
        private final ListOrderedMap<Object, V> parent;

        @SuppressWarnings("unchecked")
        ValuesView(ListOrderedMap<?, V> parent) {
            super();
            this.parent = (ListOrderedMap<Object, V>) parent;
        }

        public int size() {
            return this.parent.size();
        }

        public boolean contains(Object value) {
            return this.parent.containsValue(value);
        }

        public void clear() {
            this.parent.clear();
        }

        public Iterator<V> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<Object, V>, V>(parent.entrySet().iterator()) {
                public V next() {
                    return getIterator().next().getValue();
                }
            };
        }

        public V get(int index) {
            return this.parent.getValue(index);
        }

        public V set(int index, V value) {
            return this.parent.setValue(index, value);
        }

        public V remove(int index) {
            return this.parent.remove(index);
        }
    }

    //-----------------------------------------------------------------------
    static class KeySetView<K> extends AbstractSet<K> {
        private final ListOrderedMap<K, Object> parent;

        @SuppressWarnings("unchecked")
        KeySetView(ListOrderedMap<K, ?> parent) {
            super();
            this.parent = (ListOrderedMap<K, Object>) parent;
        }

        public int size() {
            return this.parent.size();
        }

        public boolean contains(Object value) {
            return this.parent.containsKey(value);
        }

        public void clear() {
            this.parent.clear();
        }

        public Iterator<K> iterator() {
            return new AbstractUntypedIteratorDecorator<Map.Entry<K, Object>, K>(parent.entrySet().iterator()) {
                public K next() {
                    return getIterator().next().getKey();
                }
            };
        }
    }

    //-----------------------------------------------------------------------    
    static class EntrySetView<K, V> extends AbstractSet<Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private final List<K> insertOrder;
        private Set<Map.Entry<K, V>> entrySet;

        public EntrySetView(ListOrderedMap<K, V> parent, List<K> insertOrder) {
            super();
            this.parent = parent;
            this.insertOrder = insertOrder;
        }

        private Set<Map.Entry<K, V>> getEntrySet() {
            if (entrySet == null) {
                entrySet = parent.decorated().entrySet();
            }
            return entrySet;
        }
        
        public int size() {
            return this.parent.size();
        }
        public boolean isEmpty() {
            return this.parent.isEmpty();
        }

        public boolean contains(Object obj) {
            return getEntrySet().contains(obj);
        }

        public boolean containsAll(Collection<?> coll) {
            return getEntrySet().containsAll(coll);
        }

        @SuppressWarnings("unchecked")
        public boolean remove(Object obj) {
            if (obj instanceof Map.Entry == false) {
                return false;
            }
            if (getEntrySet().contains(obj)) {
                Object key = ((Map.Entry<K, V>) obj).getKey();
                parent.remove(key);
                return true;
            }
            return false;
        }

        public void clear() {
            this.parent.clear();
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            return getEntrySet().equals(obj);
        }

        public int hashCode() {
            return getEntrySet().hashCode();
        }

        public String toString() {
            return getEntrySet().toString();
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new ListOrderedIterator<K, V>(parent, insertOrder);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedIterator<K, V> extends AbstractUntypedIteratorDecorator<K, Map.Entry<K, V>> {
        private final ListOrderedMap<K, V> parent;
        private K last = null;
        
        ListOrderedIterator(ListOrderedMap<K, V> parent, List<K> insertOrder) {
            super(insertOrder.iterator());
            this.parent = parent;
        }

        public Map.Entry<K, V> next() {
            last = getIterator().next();
            return new ListOrderedMapEntry<K, V>(parent, last);
        }

        public void remove() {
            super.remove();
            parent.decorated().remove(last);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapEntry<K, V> extends AbstractMapEntry<K, V> {
        private final ListOrderedMap<K, V> parent;

        ListOrderedMapEntry(ListOrderedMap<K, V> parent, K key) {
            super(key, null);
            this.parent = parent;
        }

        public V getValue() {
            return parent.get(key);
        }

        public V setValue(V value) {
            return parent.decorated().put(key, value);
        }
    }

    //-----------------------------------------------------------------------
    static class ListOrderedMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {
        private final ListOrderedMap<K, V> parent;
        private ListIterator<K> iterator;
        private K last = null;
        private boolean readable = false;

        ListOrderedMapIterator(ListOrderedMap<K, V> parent) {
            super();
            this.parent = parent;
            this.iterator = parent.insertOrder.listIterator();
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public K next() {
            last = iterator.next();
            readable = true;
            return last;
        }
        
        public boolean hasPrevious() {
            return iterator.hasPrevious();
        }

        public K previous() {
            last = iterator.previous();
            readable = true;
            return last;
        }

        public void remove() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            iterator.remove();
            parent.map.remove(last);
            readable = false;
        }

        public K getKey() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return last;
        }

        public V getValue() {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return parent.get(last);
        }

        public V setValue(V value) {
            if (readable == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return parent.map.put(last, value);
        }

        public void reset() {
            iterator = parent.insertOrder.listIterator();
            last = null;
            readable = false;
        }

        public String toString() {
            if (readable == true) {
                return "Iterator[" + getKey() + "=" + getValue() + "]";
            }
            return "Iterator[]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12617.java