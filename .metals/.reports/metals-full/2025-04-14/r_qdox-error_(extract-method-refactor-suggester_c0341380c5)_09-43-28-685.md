error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4764.java
text:
```scala
i@@mplements IterableMap<MultiKey<? extends K>, V>, Serializable, Cloneable {

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
import java.util.Map;

import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;

/**
 * A <code>Map</code> implementation that uses multiple keys to map the value.
 * <p>
 * This class is the most efficient way to uses multiple keys to map to a value.
 * The best way to use this class is via the additional map-style methods.
 * These provide <code>get</code>, <code>containsKey</code>, <code>put</code> and
 * <code>remove</code> for individual keys which operate without extra object creation.
 * <p>
 * The additional methods are the main interface of this map.
 * As such, you will not normally hold this map in a variable of type <code>Map</code>.
 * <p>
 * The normal map methods take in and return a {@link MultiKey}.
 * If you try to use <code>put()</code> with any other object type a
 * <code>ClassCastException</code> is thrown. If you try to use <code>null</code> as
 * the key in <code>put()</code> a <code>NullPointerException</code> is thrown.
 * <p>
 * This map is implemented as a decorator of a <code>AbstractHashedMap</code> which
 * enables extra behaviour to be added easily.
 * <ul>
 * <li><code>MultiKeyMap.decorate(new LinkedMap())</code> creates an ordered map.
 * <li><code>MultiKeyMap.decorate(new LRUMap())</code> creates an least recently used map.
 * <li><code>MultiKeyMap.decorate(new ReferenceMap())</code> creates a garbage collector sensitive map.
 * </ul>
 * Note that <code>IdentityMap</code> and <code>ReferenceIdentityMap</code> are unsuitable
 * for use as the key comparison would work on the whole MultiKey, not the elements within.
 * <p>
 * As an example, consider a least recently used cache that uses a String airline code
 * and a Locale to lookup the airline's name:
 * <pre>
 * private MultiKeyMap cache = MultiKeyMap.multiKeyMap(new LRUMap(50));
 *
 * public String getAirlineName(String code, String locale) {
 *   String name = (String) cache.get(code, locale);
 *   if (name == null) {
 *     name = getAirlineNameFromDB(code, locale);
 *     cache.put(code, locale, name);
 *   }
 *   return name;
 * }
 * </pre>
 * <p>
 * <strong>Note that MultiKeyMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. This class may throw exceptions when accessed
 * by concurrent threads without synchronization.
 *
 * @since 3.1
 * @version $Id$
 */
public class MultiKeyMap<K, V> extends AbstractMapDecorator<MultiKey<? extends K>, V>
        implements IterableMap<MultiKey<? extends K>, V>, Serializable {

    /** Serialisation version */
    private static final long serialVersionUID = -1788199231038721040L;

    /** The decorated map */
    //keep this member around for serialization BC with older Collections releases assuming we want to do that
    protected AbstractHashedMap<MultiKey<? extends K>, V> map;

    //-----------------------------------------------------------------------
    /**
     * Decorates the specified map to add the MultiKeyMap API and fast query.
     * The map must not be null and must be empty.
     *
     * @param <K>  the key type
     * @param <V>  the value type
     * @param map  the map to decorate, not null
     * @return a new multi key map
     * @throws IllegalArgumentException if the map is null or not empty
     */
    public static <K, V> MultiKeyMap<K, V> multiKeyMap(final AbstractHashedMap<MultiKey<? extends K>, V> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        if (map.size() > 0) {
            throw new IllegalArgumentException("Map must be empty");
        }
        return new MultiKeyMap<K, V>(map);
    }

    //-----------------------------------------------------------------------    
    /**
     * Constructs a new MultiKeyMap that decorates a <code>HashedMap</code>.
     */
    public MultiKeyMap() {
        this(new HashedMap<MultiKey<? extends K>, V>());
    }

    /**
     * Constructor that decorates the specified map and is called from
     * {@link #multiKeyMap(AbstractHashedMap)}.
     * The map must not be null and should be empty or only contain valid keys.
     * This constructor performs no validation.
     *
     * @param map  the map to decorate
     */
    protected MultiKeyMap(final AbstractHashedMap<MultiKey<? extends K>, V> map) {
        super(map);
        this.map = map;
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @return the mapped value, null if no match
     */
    public V get(final Object key1, final Object key2) {
        final int hashCode = hash(key1, key2);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Checks whether the map contains the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @return true if the map contains the key
     */
    public boolean containsKey(final Object key1, final Object key2) {
        final int hashCode = hash(key1, key2);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Stores the value against the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param value  the value to store
     * @return the value previously mapped to this combined key, null if none
     */
    public V put(final K key1, final K key2, final V value) {
        final int hashCode = hash(key1, key2);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                final V oldValue = entry.getValue();
                decorated().updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        decorated().addMapping(index, hashCode, new MultiKey<K>(key1, key2), value);
        return null;
    }

    /**
     * Removes the specified multi-key from this map.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @return the value mapped to the removed key, null if key not in map
     */
    public V remove(final Object key1, final Object key2) {
        final int hashCode = hash(key1, key2);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
                final V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Gets the hash code for the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @return the hash code
     */
    protected int hash(final Object key1, final Object key2) {
        int h = 0;
        if (key1 != null) {
            h ^= key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        h += ~(h << 9);
        h ^=  h >>> 14;
        h +=  h << 4;
        h ^=  h >>> 10;
        return h;
    }

    /**
     * Is the key equal to the combined key.
     * 
     * @param entry  the entry to compare to
     * @param key1  the first key
     * @param key2  the second key
     * @return true if the key matches
     */
    protected boolean isEqualKey(final AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry,
            final Object key1, final Object key2) {
        final MultiKey<? extends K> multi = entry.getKey();
        return
            multi.size() == 2 &&
            (key1 == multi.getKey(0) || key1 != null && key1.equals(multi.getKey(0))) &&
            (key2 == multi.getKey(1) || key1 != null && key2.equals(multi.getKey(1)));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return the mapped value, null if no match
     */
    public V get(final Object key1, final Object key2, final Object key3) {
        final int hashCode = hash(key1, key2, key3);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Checks whether the map contains the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return true if the map contains the key
     */
    public boolean containsKey(final Object key1, final Object key2, final Object key3) {
        final int hashCode = hash(key1, key2, key3);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Stores the value against the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param value  the value to store
     * @return the value previously mapped to this combined key, null if none
     */
    public V put(final K key1, final K key2, final K key3, final V value) {
        final int hashCode = hash(key1, key2, key3);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                final V oldValue = entry.getValue();
                decorated().updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        decorated().addMapping(index, hashCode, new MultiKey<K>(key1, key2, key3), value);
        return null;
    }

    /**
     * Removes the specified multi-key from this map.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return the value mapped to the removed key, null if key not in map
     */
    public V remove(final Object key1, final Object key2, final Object key3) {
        final int hashCode = hash(key1, key2, key3);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
                final V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Gets the hash code for the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return the hash code
     */
    protected int hash(final Object key1, final Object key2, final Object key3) {
        int h = 0;
        if (key1 != null) {
            h ^= key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        h += ~(h << 9);
        h ^=  h >>> 14;
        h +=  h << 4;
        h ^=  h >>> 10;
        return h;
    }

    /**
     * Is the key equal to the combined key.
     * 
     * @param entry  the entry to compare to
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return true if the key matches
     */
    protected boolean isEqualKey(final AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry,
                                 final Object key1, final Object key2, final Object key3) {
        final MultiKey<? extends K> multi = entry.getKey();
        return
            multi.size() == 3 &&
            (key1 == multi.getKey(0) || key1 != null && key1.equals(multi.getKey(0))) &&
            (key2 == multi.getKey(1) || key2 != null && key2.equals(multi.getKey(1))) &&
            (key3 == multi.getKey(2) || key3 != null && key3.equals(multi.getKey(2)));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return the mapped value, null if no match
     */
    public V get(final Object key1, final Object key2, final Object key3, final Object key4) {
        final int hashCode = hash(key1, key2, key3, key4);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Checks whether the map contains the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return true if the map contains the key
     */
    public boolean containsKey(final Object key1, final Object key2, final Object key3, final Object key4) {
        final int hashCode = hash(key1, key2, key3, key4);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Stores the value against the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param value  the value to store
     * @return the value previously mapped to this combined key, null if none
     */
    public V put(final K key1, final K key2, final K key3, final K key4, final V value) {
        final int hashCode = hash(key1, key2, key3, key4);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                final V oldValue = entry.getValue();
                decorated().updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        decorated().addMapping(index, hashCode, new MultiKey<K>(key1, key2, key3, key4), value);
        return null;
    }

    /**
     * Removes the specified multi-key from this map.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return the value mapped to the removed key, null if key not in map
     */
    public V remove(final Object key1, final Object key2, final Object key3, final Object key4) {
        final int hashCode = hash(key1, key2, key3, key4);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
                final V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Gets the hash code for the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return the hash code
     */
    protected int hash(final Object key1, final Object key2, final Object key3, final Object key4) {
        int h = 0;
        if (key1 != null) {
            h ^= key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        if (key4 != null) {
            h ^= key4.hashCode();
        }
        h += ~(h << 9);
        h ^=  h >>> 14;
        h +=  h << 4;
        h ^=  h >>> 10;
        return h;
    }

    /**
     * Is the key equal to the combined key.
     * 
     * @param entry  the entry to compare to
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return true if the key matches
     */
    protected boolean isEqualKey(final AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry,
                                 final Object key1, final Object key2, final Object key3, final Object key4) {
        final MultiKey<? extends K> multi = entry.getKey();
        return
            multi.size() == 4 &&
            (key1 == multi.getKey(0) || key1 != null && key1.equals(multi.getKey(0))) &&
            (key2 == multi.getKey(1) || key2 != null && key2.equals(multi.getKey(1))) &&
            (key3 == multi.getKey(2) || key3 != null && key3.equals(multi.getKey(2))) &&
            (key4 == multi.getKey(3) || key4 != null && key4.equals(multi.getKey(3)));
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the value mapped to the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @return the mapped value, null if no match
     */
    public V get(final Object key1, final Object key2, final Object key3, final Object key4, final Object key5) {
        final int hashCode = hash(key1, key2, key3, key4, key5);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                return entry.getValue();
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Checks whether the map contains the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @return true if the map contains the key
     */
    public boolean containsKey(final Object key1, final Object key2, final Object key3,
                               final Object key4, final Object key5) {
        final int hashCode = hash(key1, key2, key3, key4, key5);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry =
                decorated().data[decorated().hashIndex(hashCode, decorated().data.length)];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                return true;
            }
            entry = entry.next;
        }
        return false;
    }

    /**
     * Stores the value against the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @param value  the value to store
     * @return the value previously mapped to this combined key, null if none
     */
    public V put(final K key1, final K key2, final K key3, final K key4, final K key5, final V value) {
        final int hashCode = hash(key1, key2, key3, key4, key5);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                final V oldValue = entry.getValue();
                decorated().updateEntry(entry, value);
                return oldValue;
            }
            entry = entry.next;
        }
        decorated().addMapping(index, hashCode, new MultiKey<K>(key1, key2, key3, key4, key5), value);
        return null;
    }

    /**
     * Removes the specified multi-key from this map.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @return the value mapped to the removed key, null if key not in map
     */
    public V remove(final Object key1, final Object key2, final Object key3, final Object key4, final Object key5) {
        final int hashCode = hash(key1, key2, key3, key4, key5);
        final int index = decorated().hashIndex(hashCode, decorated().data.length);
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = decorated().data[index];
        AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
        while (entry != null) {
            if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
                final V oldValue = entry.getValue();
                decorated().removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
            entry = entry.next;
        }
        return null;
    }

    /**
     * Gets the hash code for the specified multi-key.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @return the hash code
     */
    protected int hash(final Object key1, final Object key2, final Object key3, final Object key4, final Object key5) {
        int h = 0;
        if (key1 != null) {
            h ^= key1.hashCode();
        }
        if (key2 != null) {
            h ^= key2.hashCode();
        }
        if (key3 != null) {
            h ^= key3.hashCode();
        }
        if (key4 != null) {
            h ^= key4.hashCode();
        }
        if (key5 != null) {
            h ^= key5.hashCode();
        }
        h += ~(h << 9);
        h ^=  h >>> 14;
        h +=  h << 4;
        h ^=  h >>> 10;
        return h;
    }

    /**
     * Is the key equal to the combined key.
     * 
     * @param entry  the entry to compare to
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @param key5  the fifth key
     * @return true if the key matches
     */
    protected boolean isEqualKey(final AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry,
            final Object key1, final Object key2, final Object key3, final Object key4, final Object key5) {
        final MultiKey<? extends K> multi = entry.getKey();
        return
            multi.size() == 5 &&
            (key1 == multi.getKey(0) || key1 != null && key1.equals(multi.getKey(0))) &&
            (key2 == multi.getKey(1) || key2 != null && key2.equals(multi.getKey(1))) &&
            (key3 == multi.getKey(2) || key3 != null && key3.equals(multi.getKey(2))) &&
            (key4 == multi.getKey(3) || key4 != null && key4.equals(multi.getKey(3))) &&
            (key5 == multi.getKey(4) || key5 != null && key5.equals(multi.getKey(4)));
    }

    //-----------------------------------------------------------------------
    /**
     * Removes all mappings where the first key is that specified.
     * <p>
     * This method removes all the mappings where the <code>MultiKey</code>
     * has one or more keys, and the first matches that specified.
     * 
     * @param key1  the first key
     * @return true if any elements were removed
     */
    public boolean removeAll(final Object key1) {
        boolean modified = false;
        final MapIterator<MultiKey<? extends K>, V> it = mapIterator();
        while (it.hasNext()) {
            final MultiKey<? extends K> multi = it.next();
            if (multi.size() >= 1 &&
                (key1 == null ? multi.getKey(0) == null : key1.equals(multi.getKey(0)))) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all mappings where the first two keys are those specified.
     * <p>
     * This method removes all the mappings where the <code>MultiKey</code>
     * has two or more keys, and the first two match those specified.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @return true if any elements were removed
     */
    public boolean removeAll(final Object key1, final Object key2) {
        boolean modified = false;
        final MapIterator<MultiKey<? extends K>, V> it = mapIterator();
        while (it.hasNext()) {
            final MultiKey<? extends K> multi = it.next();
            if (multi.size() >= 2 &&
                (key1 == null ? multi.getKey(0) == null : key1.equals(multi.getKey(0))) &&
                (key2 == null ? multi.getKey(1) == null : key2.equals(multi.getKey(1)))) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all mappings where the first three keys are those specified.
     * <p>
     * This method removes all the mappings where the <code>MultiKey</code>
     * has three or more keys, and the first three match those specified.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @return true if any elements were removed
     */
    public boolean removeAll(final Object key1, final Object key2, final Object key3) {
        boolean modified = false;
        final MapIterator<MultiKey<? extends K>, V> it = mapIterator();
        while (it.hasNext()) {
            final MultiKey<? extends K> multi = it.next();
            if (multi.size() >= 3 &&
                (key1 == null ? multi.getKey(0) == null : key1.equals(multi.getKey(0))) &&
                (key2 == null ? multi.getKey(1) == null : key2.equals(multi.getKey(1))) &&
                (key3 == null ? multi.getKey(2) == null : key3.equals(multi.getKey(2)))) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Removes all mappings where the first four keys are those specified.
     * <p>
     * This method removes all the mappings where the <code>MultiKey</code>
     * has four or more keys, and the first four match those specified.
     * 
     * @param key1  the first key
     * @param key2  the second key
     * @param key3  the third key
     * @param key4  the fourth key
     * @return true if any elements were removed
     */
    public boolean removeAll(final Object key1, final Object key2, final Object key3, final Object key4) {
        boolean modified = false;
        final MapIterator<MultiKey<? extends K>, V> it = mapIterator();
        while (it.hasNext()) {
            final MultiKey<? extends K> multi = it.next();
            if (multi.size() >= 4 &&
                (key1 == null ? multi.getKey(0) == null : key1.equals(multi.getKey(0))) &&
                (key2 == null ? multi.getKey(1) == null : key2.equals(multi.getKey(1))) &&
                (key3 == null ? multi.getKey(2) == null : key3.equals(multi.getKey(2))) &&
                (key4 == null ? multi.getKey(3) == null : key4.equals(multi.getKey(3)))) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    //-----------------------------------------------------------------------
    /**
     * Check to ensure that input keys are valid MultiKey objects.
     * 
     * @param key  the key to check
     */
    protected void checkKey(final MultiKey<?> key) {
        if (key == null) {
            throw new NullPointerException("Key must not be null");
        }
    }

    /**
     * Clones the map without cloning the keys or values.
     *
     * @return a shallow clone
     */
    @Override
    public MultiKeyMap<K, V> clone() {
        return new MultiKeyMap<K, V>(decorated().clone());
    }

    /**
     * Puts the key and value into the map, where the key must be a non-null
     * MultiKey object.
     * 
     * @param key  the non-null MultiKey object
     * @param value  the value to store
     * @return the previous value for the key
     * @throws NullPointerException if the key is null
     * @throws ClassCastException if the key is not a MultiKey
     */
    @Override
    public V put(final MultiKey<? extends K> key, final V value) {
        checkKey(key);
        return super.put(key, value);
    }

    /**
     * Copies all of the keys and values from the specified map to this map.
     * Each key must be non-null and a MultiKey object.
     * 
     * @param mapToCopy  to this map
     * @throws NullPointerException if the mapToCopy or any key within is null
     * @throws ClassCastException if any key in mapToCopy is not a MultiKey
     */
    @Override
    public void putAll(final Map<? extends MultiKey<? extends K>, ? extends V> mapToCopy) {
        for (final MultiKey<? extends K> key : mapToCopy.keySet()) {
            checkKey(key);
        }
        super.putAll(mapToCopy);
    }

    //-----------------------------------------------------------------------
    @Override
    public MapIterator<MultiKey<? extends K>, V> mapIterator() {
        return decorated().mapIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractHashedMap<MultiKey<? extends K>, V> decorated() {
        return map;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4764.java