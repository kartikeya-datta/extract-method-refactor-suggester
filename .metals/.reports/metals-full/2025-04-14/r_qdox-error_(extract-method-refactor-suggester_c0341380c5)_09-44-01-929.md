error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12618.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12618.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12618.java
text:
```scala
r@@eturn new StringBuilder(128)

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

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections.BoundedMap;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.commons.collections.keyvalue.TiedMapEntry;

/**
 * A <code>Map</code> implementation that holds a single item and is fixed size.
 * <p>
 * The single key/value pair is specified at creation.
 * The map is fixed size so any action that would change the size is disallowed.
 * However, the <code>put</code> or <code>setValue</code> methods can <i>change</i>
 * the value associated with the key.
 * <p>
 * If trying to remove or clear the map, an UnsupportedOperationException is thrown.
 * If trying to put a new mapping into the map, an  IllegalArgumentException is thrown.
 * The put method will only suceed if the key specified is the same as the 
 * singleton key.
 * <p>
 * The key and value can be obtained by:
 * <ul>
 * <li>normal Map methods and views
 * <li>the <code>MapIterator</code>, see {@link #mapIterator()}
 * <li>the <code>KeyValue</code> interface (just cast - no object creation)
 * </ul>
 *
 * @since Commons Collections 3.1
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class SingletonMap<K, V>
        implements OrderedMap<K, V>, BoundedMap<K, V>, KeyValue<K, V>, Serializable, Cloneable {

    /** Serialization version */
    private static final long serialVersionUID = -8931271118676803261L;

    /** Singleton key */
    private final K key;
    /** Singleton value */
    private V value;

    /**
     * Constructor that creates a map of <code>null</code> to <code>null</code>.
     */
    public SingletonMap() {
        super();
        this.key = null;
    }

    /**
     * Constructor specifying the key and value.
     *
     * @param key  the key to use
     * @param value  the value to use
     */
    public SingletonMap(K key, V value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * Constructor specifying the key and value as a <code>KeyValue</code>.
     *
     * @param keyValue  the key value pair to use
     */
    public SingletonMap(KeyValue<K, V> keyValue) {
        super();
        this.key = keyValue.getKey();
        this.value = keyValue.getValue();
    }

    /**
     * Constructor specifying the key and value as a <code>MapEntry</code>.
     *
     * @param mapEntry  the mapEntry to use
     */
    public SingletonMap(Map.Entry<K, V> mapEntry) {
        super();
        this.key = mapEntry.getKey();
        this.value = mapEntry.getValue();
    }

    /**
     * Constructor copying elements from another map.
     *
     * @param map  the map to copy, must be size 1
     * @throws NullPointerException if the map is null
     * @throws IllegalArgumentException if the size is not 1
     */
    public SingletonMap(Map<K, V> map) {
        super();
        if (map.size() != 1) {
            throw new IllegalArgumentException("The map size must be 1");
        }
        Map.Entry<K, V> entry = map.entrySet().iterator().next();
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    // KeyValue
    //-----------------------------------------------------------------------
    /**
     * Gets the key.
     *
     * @return the key 
     */
    public K getKey() {
        return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value  the new value to set
     * @return the old value
     */
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    // BoundedMap
    //-----------------------------------------------------------------------
    /**
     * Is the map currently full, always true.
     *
     * @return true always
     */
    public boolean isFull() {
        return true;
    }

    /**
     * Gets the maximum size of the map, always 1.
     * 
     * @return 1 always
     */
    public int maxSize() {
        return 1;
    }

    // Map
    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the key specified.
     * 
     * @param key  the key
     * @return the mapped value, null if no match
     */
    public V get(Object key) {
        if (isEqualKey(key)) {
            return value;
        }
        return null;
    }

    /**
     * Gets the size of the map, always 1.
     * 
     * @return the size of 1
     */
    public int size() {
        return 1;
    }

    /**
     * Checks whether the map is currently empty, which it never is.
     * 
     * @return false always
     */
    public boolean isEmpty() {
        return false;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the map contains the specified key.
     * 
     * @param key  the key to search for
     * @return true if the map contains the key
     */
    public boolean containsKey(Object key) {
        return (isEqualKey(key));
    }

    /**
     * Checks whether the map contains the specified value.
     * 
     * @param value  the value to search for
     * @return true if the map contains the key
     */
    public boolean containsValue(Object value) {
        return (isEqualValue(value));
    }

    //-----------------------------------------------------------------------
    /**
     * Puts a key-value mapping into this map where the key must match the existing key.
     * <p>
     * An IllegalArgumentException is thrown if the key does not match as the map
     * is fixed size.
     * 
     * @param key  the key to set, must be the key of the map
     * @param value  the value to set
     * @return the value previously mapped to this key, null if none
     * @throws IllegalArgumentException if the key does not match
     */
    public V put(K key, V value) {
        if (isEqualKey(key)) {
            return setValue(value);
        }
        throw new IllegalArgumentException("Cannot put new key/value pair - Map is fixed size singleton");
    }

    /**
     * Puts the values from the specified map into this map.
     * <p>
     * The map must be of size 0 or size 1.
     * If it is size 1, the key must match the key of this map otherwise an
     * IllegalArgumentException is thrown.
     * 
     * @param map  the map to add, must be size 0 or 1, and the key must match
     * @throws NullPointerException if the map is null
     * @throws IllegalArgumentException if the key does not match
     */
    public void putAll(Map<? extends K, ? extends V> map) {
        switch (map.size()) {
            case 0:
                return;

            case 1:
                Map.Entry<? extends K, ? extends V> entry = map.entrySet().iterator().next();
                put(entry.getKey(), entry.getValue());
                return;

            default:
                throw new IllegalArgumentException("The map size must be 0 or 1");
        }
    }
    
    /**
     * Unsupported operation.
     * 
     * @param key  the mapping to remove
     * @return the value mapped to the removed key, null if key not in map
     * @throws UnsupportedOperationException always
     */
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported operation.
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the entrySet view of the map.
     * Changes made via <code>setValue</code> affect this map.
     * To simply iterate through the entries, use {@link #mapIterator()}.
     * 
     * @return the entrySet view
     */
    public Set<Map.Entry<K, V>> entrySet() {
        Map.Entry<K, V> entry = new TiedMapEntry<K, V>(this, getKey());
        return Collections.singleton(entry);
    }
    
    /**
     * Gets the unmodifiable keySet view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the keys, use {@link #mapIterator()}.
     * 
     * @return the keySet view
     */
    public Set<K> keySet() {
        return Collections.singleton(key);
    }

    /**
     * Gets the unmodifiable values view of the map.
     * Changes made to the view affect this map.
     * To simply iterate through the values, use {@link #mapIterator()}.
     * 
     * @return the values view
     */
    public Collection<V> values() {
        return new SingletonValues<V>(this);
    }

    /**
     * {@inheritDoc}
     */
    public OrderedMapIterator<K, V> mapIterator() {
        return new SingletonMapIterator<K, V>(this);
    }

    /**
     * Gets the first (and only) key in the map.
     * 
     * @return the key
     */
    public K firstKey() {
        return getKey();
    }

    /**
     * Gets the last (and only) key in the map.
     * 
     * @return the key
     */
    public K lastKey() {
        return getKey();
    }

    /**
     * Gets the next key after the key specified, always null.
     * 
     * @param key  the next key
     * @return null always
     */
    public K nextKey(K key) {
        return null;
    }

    /**
     * Gets the previous key before the key specified, always null.
     * 
     * @param key  the next key
     * @return null always
     */
    public K previousKey(K key) {
        return null;
    }

    //-----------------------------------------------------------------------
    /**
     * Compares the specified key to the stored key.
     * 
     * @param key  the key to compare
     * @return true if equal
     */
    protected boolean isEqualKey(Object key) {
        return (key == null ? getKey() == null : key.equals(getKey()));
    }

    /**
     * Compares the specified value to the stored value.
     * 
     * @param value  the value to compare
     * @return true if equal
     */
    protected boolean isEqualValue(Object value) {
        return (value == null ? getValue() == null : value.equals(getValue()));
    }

    //-----------------------------------------------------------------------
    /**
     * SingletonMapIterator.
     */
    static class SingletonMapIterator<K, V> implements OrderedMapIterator<K, V>, ResettableIterator<K> {
        private final SingletonMap<K, V> parent;
        private boolean hasNext = true;
        private boolean canGetSet = false;
        
        SingletonMapIterator(SingletonMap<K, V> parent) {
            super();
            this.parent = parent;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public K next() {
            if (hasNext == false) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            hasNext = false;
            canGetSet = true;
            return parent.getKey();
        }

        public boolean hasPrevious() {
            return (hasNext == false);
        }

        public K previous() {
            if (hasNext == true) {
                throw new NoSuchElementException(AbstractHashedMap.NO_PREVIOUS_ENTRY);
            }
            hasNext = true;
            return parent.getKey();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public K getKey() {
            if (canGetSet == false) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return parent.getKey();
        }

        public V getValue() {
            if (canGetSet == false) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return parent.getValue();
        }

        public V setValue(V value) {
            if (canGetSet == false) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return parent.setValue(value);
        }
        
        public void reset() {
            hasNext = true;
        }
        
        public String toString() {
            if (hasNext) {
                return "Iterator[]";
            }
            return "Iterator[" + getKey() + "=" + getValue() + "]";
        }
    }
    
    /**
     * Values implementation for the SingletonMap.
     * This class is needed as values is a view that must update as the map updates.
     */
    static class SingletonValues<V> extends AbstractSet<V> implements Serializable {
        private static final long serialVersionUID = -3689524741863047872L;
        private final SingletonMap<?, V> parent;

        SingletonValues(SingletonMap<?, V> parent) {
            super();
            this.parent = parent;
        }

        public int size() {
            return 1;
        }
        public boolean isEmpty() {
            return false;
        }
        public boolean contains(Object object) {
            return parent.containsValue(object);
        }
        public void clear() {
            throw new UnsupportedOperationException();
        }
        public Iterator<V> iterator() {
            return new SingletonIterator<V>(parent.getValue(), false);
        }
    }
    
    //-----------------------------------------------------------------------
    /**
     * Clones the map without cloning the key or value.
     *
     * @return a shallow clone
     */
    @SuppressWarnings("unchecked")
    public SingletonMap<K, V> clone() {
        try {
            return (SingletonMap<K, V>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

    /**
     * Compares this map with another.
     * 
     * @param obj  the object to compare to
     * @return true if equal
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map == false) {
            return false;
        }
        Map other = (Map) obj;
        if (other.size() != 1) {
            return false;
        }
        Map.Entry entry = (Map.Entry) other.entrySet().iterator().next();
        return isEqualKey(entry.getKey()) && isEqualValue(entry.getValue());
    }

    /**
     * Gets the standard Map hashCode.
     * 
     * @return the hash code defined in the Map interface
     */
    public int hashCode() {
        return (getKey() == null ? 0 : getKey().hashCode()) ^
               (getValue() == null ? 0 : getValue().hashCode()); 
    }

    /**
     * Gets the map as a String.
     * 
     * @return a string version of the map
     */
    public String toString() {
        return new StringBuffer(128)
            .append('{')
            .append((getKey() == this ? "(this Map)" : getKey()))
            .append('=')
            .append((getValue() == this ? "(this Map)" : getValue()))
            .append('}')
            .toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12618.java