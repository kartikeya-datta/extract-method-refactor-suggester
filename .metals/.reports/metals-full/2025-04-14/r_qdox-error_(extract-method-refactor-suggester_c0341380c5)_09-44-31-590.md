error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14954.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14954.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14954.java
text:
```scala
r@@eturn Boolean.valueOf((String) answer);

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
package org.apache.commons.collections;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.map.AbstractMapDecorator;
import org.apache.commons.collections.map.AbstractSortedMapDecorator;
import org.apache.commons.collections.map.FixedSizeMap;
import org.apache.commons.collections.map.FixedSizeSortedMap;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.LazySortedMap;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections.map.PredicatedMap;
import org.apache.commons.collections.map.PredicatedSortedMap;
import org.apache.commons.collections.map.TransformedMap;
import org.apache.commons.collections.map.TransformedSortedMap;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.apache.commons.collections.map.UnmodifiableSortedMap;

/**
 * Provides utility methods and decorators for
 * {@link Map} and {@link SortedMap} instances.
 * <p>
 * It contains various type safe methods
 * as well as other useful features like deep copying.
 * <p>
 * It also provides the following decorators:
 *
 *  <ul>
 *  <li>{@link #fixedSizeMap(Map)}
 *  <li>{@link #fixedSizeSortedMap(SortedMap)}
 *  <li>{@link #lazyMap(Map,Factory)}
 *  <li>{@link #lazyMap(Map,Transformer)}
 *  <li>{@link #lazySortedMap(SortedMap,Factory)}
 *  <li>{@link #lazySortedMap(SortedMap,Transformer)}
 *  <li>{@link #predicatedMap(Map,Predicate,Predicate)}
 *  <li>{@link #predicatedSortedMap(SortedMap,Predicate,Predicate)}
 *  <li>{@link #transformedMap(Map, Transformer, Transformer)}
 *  <li>{@link #transformedSortedMap(SortedMap, Transformer, Transformer)}
 *  <li>{@link #multiValueMap( Map )}
 *  <li>{@link #multiValueMap( Map, Class )}
 *  <li>{@link #multiValueMap( Map, Factory )}
 *  </ul>
 *
 * @since Commons Collections 1.0
 * @version $Revision$ $Date$
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @author <a href="mailto:nissim@nksystems.com">Nissim Karpenstein</a>
 * @author <a href="mailto:knielsen@apache.org">Kasper Nielsen</a>
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Matthew Hawthorne
 * @author Arun Mammen Thomas
 * @author Janek Bogucki
 * @author Max Rydahl Andersen
 * @author <a href="mailto:equinus100@hotmail.com">Ashwin S</a>
 * @author <a href="mailto:jcarman@apache.org">James Carman</a>
 * @author Neil O'Toole
 */
public class MapUtils {

    /**
     * An empty unmodifiable map.
     * This was not provided in JDK1.2.
     */
    public static final Map<Object, Object> EMPTY_MAP = UnmodifiableMap.decorate(new HashMap<Object, Object>(1));

    /**
     * An empty unmodifiable sorted map.
     * This is not provided in the JDK.
     */
    public static final SortedMap<Object, Object> EMPTY_SORTED_MAP = UnmodifiableSortedMap.decorate(new TreeMap<Object, Object>());

    /**
     * String used to indent the verbose and debug Map prints.
     */
    private static final String INDENT_STRING = "    ";

    /**
     * <code>MapUtils</code> should not normally be instantiated.
     */
    public MapUtils() {
    }

    // Type safe getters
    //-------------------------------------------------------------------------
    /**
     * Gets from a Map in a null-safe manner.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map, <code>null</code> if null map input
     */
    public static <K, V> V getObject(final Map<? super K, V> map, final K key) {
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    /**
     * Gets a String from a Map in a null-safe manner.
     * <p>
     * The String is obtained via <code>toString</code>.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a String, <code>null</code> if null map input
     */
    public static <K> String getString(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * Gets a Boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> it is returned directly.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>null</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Boolean, <code>null</code> if null map input
     */
    public static <K> Boolean getBoolean(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean) answer;
                }
                if (answer instanceof String) {
                    return new Boolean((String) answer);
                }
                if (answer instanceof Number) {
                    Number n = (Number) answer;
                    return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }

    /**
     * Gets a Number from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Number</code> it is returned directly.
     * If the value is a <code>String</code> it is converted using
     * {@link NumberFormat#parse(String)} on the system default formatter
     * returning <code>null</code> if the conversion fails.
     * Otherwise, <code>null</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Number, <code>null</code> if null map input
     */
    public static <K> Number getNumber(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                }
                if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                    } catch (ParseException e) {
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets a Byte from a Map in a null-safe manner.
     * <p>
     * The Byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Byte, <code>null</code> if null map input
     */
    public static <K> Byte getByte(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Byte) {
            return (Byte) answer;
        }
        return new Byte(answer.byteValue());
    }

    /**
     * Gets a Short from a Map in a null-safe manner.
     * <p>
     * The Short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Short, <code>null</code> if null map input
     */
    public static <K> Short getShort(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Short) {
            return (Short) answer;
        }
        return new Short(answer.shortValue());
    }

    /**
     * Gets a Integer from a Map in a null-safe manner.
     * <p>
     * The Integer is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Integer, <code>null</code> if null map input
     */
    public static <K> Integer getInteger(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return new Integer(answer.intValue());
    }

    /**
     * Gets a Long from a Map in a null-safe manner.
     * <p>
     * The Long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Long, <code>null</code> if null map input
     */
    public static <K> Long getLong(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Long) {
            return (Long) answer;
        }
        return new Long(answer.longValue());
    }

    /**
     * Gets a Float from a Map in a null-safe manner.
     * <p>
     * The Float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Float, <code>null</code> if null map input
     */
    public static <K> Float getFloat(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Float) {
            return (Float) answer;
        }
        return new Float(answer.floatValue());
    }

    /**
     * Gets a Double from a Map in a null-safe manner.
     * <p>
     * The Double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Double, <code>null</code> if null map input
     */
    public static <K> Double getDouble(final Map<? super K, ?> map, final K key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        }
        if (answer instanceof Double) {
            return (Double) answer;
        }
        return new Double(answer.doubleValue());
    }

    /**
     * Gets a Map from a Map in a null-safe manner.
     * <p>
     * If the value returned from the specified map is not a Map then
     * <code>null</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Map, <code>null</code> if null map input
     */
    public static <K> Map<?, ?> getMap(final Map<? super K, ?> map, final K key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null && answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        return null;
    }

    // Type safe getters with default values
    //-------------------------------------------------------------------------
    /**
     *  Looks up the given key in the given map, converting null into the
     *  given default value.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null
     *  @return  the value in the map, or defaultValue if the original value
     *    is null or the map is null
     */
    public static <K, V> V getObject(Map<K, V> map, K key, V defaultValue) {
        if (map != null) {
            V answer = map.get(key);
            if (answer != null) {
                return answer;
            }
        }
        return defaultValue;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a string, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a string, or defaultValue if the
     *    original value is null, the map is null or the string conversion
     *    fails
     */
    public static <K> String getString(Map<? super K, ?> map, K key, String defaultValue) {
        String answer = getString(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a boolean, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a boolean, or defaultValue if the
     *    original value is null, the map is null or the boolean conversion
     *    fails
     */
    public static <K> Boolean getBoolean(Map<? super K, ?> map, K key, Boolean defaultValue) {
        Boolean answer = getBoolean(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a number, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Number getNumber(Map<? super K, ?> map, K key, Number defaultValue) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a byte, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Byte getByte(Map<? super K, ?> map, K key, Byte defaultValue) {
        Byte answer = getByte(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a short, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Short getShort(Map<? super K, ?> map, K key, Short defaultValue) {
        Short answer = getShort(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  an integer, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Integer getInteger(Map<? super K, ?> map, K key, Integer defaultValue) {
        Integer answer = getInteger(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a long, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Long getLong(Map<? super K, ?> map, K key, Long defaultValue) {
        Long answer = getLong(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a float, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Float getFloat(Map<? super K, ?> map, K key, Float defaultValue) {
        Float answer = getFloat(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a double, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the number conversion
     *    fails
     */
    public static <K> Double getDouble(Map<? super K, ?> map, K key, Double defaultValue) {
        Double answer = getDouble(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    /**
     *  Looks up the given key in the given map, converting the result into
     *  a map, using the default value if the the conversion fails.
     *
     *  @param map  the map whose value to look up
     *  @param key  the key of the value to look up in that map
     *  @param defaultValue  what to return if the value is null or if the
     *     conversion fails
     *  @return  the value in the map as a number, or defaultValue if the
     *    original value is null, the map is null or the map conversion
     *    fails
     */
    public static <K> Map<?, ?> getMap(Map<? super K, ?> map, K key, Map<?, ?> defaultValue) {
        Map<?, ?> answer = getMap(map, key);
        if (answer == null) {
            answer = defaultValue;
        }
        return answer;
    }

    // Type safe primitive getters
    //-------------------------------------------------------------------------
    /**
     * Gets a boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> its value is returned.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>false</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Boolean, <code>false</code> if null map input
     */
    public static <K> boolean getBooleanValue(final Map<? super K, ?> map, final K key) {
        return Boolean.TRUE.equals(getBoolean(map, key));
    }

    /**
     * Gets a byte from a Map in a null-safe manner.
     * <p>
     * The byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a byte, <code>0</code> if null map input
     */
    public static <K> byte getByteValue(final Map<? super K, ?> map, final K key) {
        Byte byteObject = getByte(map, key);
        if (byteObject == null) {
            return 0;
        }
        return byteObject.byteValue();
    }

    /**
     * Gets a short from a Map in a null-safe manner.
     * <p>
     * The short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a short, <code>0</code> if null map input
     */
    public static <K> short getShortValue(final Map<? super K, ?> map, final K key) {
        Short shortObject = getShort(map, key);
        if (shortObject == null) {
            return 0;
        }
        return shortObject.shortValue();
    }

    /**
     * Gets an int from a Map in a null-safe manner.
     * <p>
     * The int is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as an int, <code>0</code> if null map input
     */
    public static <K> int getIntValue(final Map<? super K, ?> map, final K key) {
        Integer integerObject = getInteger(map, key);
        if (integerObject == null) {
            return 0;
        }
        return integerObject.intValue();
    }

    /**
     * Gets a long from a Map in a null-safe manner.
     * <p>
     * The long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a long, <code>0L</code> if null map input
     */
    public static <K> long getLongValue(final Map<? super K, ?> map, final K key) {
        Long longObject = getLong(map, key);
        if (longObject == null) {
            return 0L;
        }
        return longObject.longValue();
    }

    /**
     * Gets a float from a Map in a null-safe manner.
     * <p>
     * The float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a float, <code>0.0F</code> if null map input
     */
    public static <K> float getFloatValue(final Map<? super K, ?> map, final K key) {
        Float floatObject = getFloat(map, key);
        if (floatObject == null) {
            return 0f;
        }
        return floatObject.floatValue();
    }

    /**
     * Gets a double from a Map in a null-safe manner.
     * <p>
     * The double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a double, <code>0.0</code> if null map input
     */
    public static <K> double getDoubleValue(final Map<? super K, ?> map, final K key) {
        Double doubleObject = getDouble(map, key);
        if (doubleObject == null) {
            return 0d;
        }
        return doubleObject.doubleValue();
    }

    // Type safe primitive getters with default values
    //-------------------------------------------------------------------------
    /**
     * Gets a boolean from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * If the value is a <code>Boolean</code> its value is returned.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>defaultValue</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a Boolean, <code>defaultValue</code> if null map input
     */
    public static <K> boolean getBooleanValue(final Map<? super K, ?> map, final K key, boolean defaultValue) {
        Boolean booleanObject = getBoolean(map, key);
        if (booleanObject == null) {
            return defaultValue;
        }
        return booleanObject.booleanValue();
    }

    /**
     * Gets a byte from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The byte is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a byte, <code>defaultValue</code> if null map input
     */
    public static <K> byte getByteValue(final Map<? super K, ?> map, final K key, byte defaultValue) {
        Byte byteObject = getByte(map, key);
        if (byteObject == null) {
            return defaultValue;
        }
        return byteObject.byteValue();
    }

    /**
     * Gets a short from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The short is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a short, <code>defaultValue</code> if null map input
     */
    public static <K> short getShortValue(final Map<? super K, ?> map, final K key, short defaultValue) {
        Short shortObject = getShort(map, key);
        if (shortObject == null) {
            return defaultValue;
        }
        return shortObject.shortValue();
    }

    /**
     * Gets an int from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The int is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as an int, <code>defaultValue</code> if null map input
     */
    public static <K> int getIntValue(final Map<? super K, ?> map, final K key, int defaultValue) {
        Integer integerObject = getInteger(map, key);
        if (integerObject == null) {
            return defaultValue;
        }
        return integerObject.intValue();
    }

    /**
     * Gets a long from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a long, <code>defaultValue</code> if null map input
     */
    public static <K> long getLongValue(final Map<? super K, ?> map, final K key, long defaultValue) {
        Long longObject = getLong(map, key);
        if (longObject == null) {
            return defaultValue;
        }
        return longObject.longValue();
    }

    /**
     * Gets a float from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The float is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a float, <code>defaultValue</code> if null map input
     */
    public static <K> float getFloatValue(final Map<? super K, ?> map, final K key, float defaultValue) {
        Float floatObject = getFloat(map, key);
        if (floatObject == null) {
            return defaultValue;
        }
        return floatObject.floatValue();
    }

    /**
     * Gets a double from a Map in a null-safe manner,
     * using the default value if the the conversion fails.
     * <p>
     * The double is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @param defaultValue  return if the value is null or if the
     *     conversion fails
     * @return the value in the Map as a double, <code>defaultValue</code> if null map input
     */
    public static <K> double getDoubleValue(final Map<? super K, ?> map, final K key, double defaultValue) {
        Double doubleObject = getDouble(map, key);
        if (doubleObject == null) {
            return defaultValue;
        }
        return doubleObject.doubleValue();
    }

    // Conversion methods
    //-------------------------------------------------------------------------
    /**
     * Gets a new Properties object initialised with the values from a Map.
     * A null input will return an empty properties object.
     *
     * @param map  the map to convert to a Properties object, may not be null
     * @return the properties object
     */
    public static <K, V> Properties toProperties(final Map<K, V> map) {
        Properties answer = new Properties();
        if (map != null) {
            for (Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<?, ?> entry = iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                answer.put(key, value);
            }
        }
        return answer;
    }

    /**
     * Creates a new HashMap using data copied from a ResourceBundle.
     *
     * @param resourceBundle  the resource bundle to convert, may not be null
     * @return the hashmap containing the data
     * @throws NullPointerException if the bundle is null
     */
    public static Map<String, Object> toMap(final ResourceBundle resourceBundle) {
        Enumeration<String> enumeration = resourceBundle.getKeys();
        Map<String, Object> map = new HashMap<String, Object>();

        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            Object value = resourceBundle.getObject(key);
            map.put(key, value);
        }

        return map;
    }

    // Printing methods
    //-------------------------------------------------------------------------
    /**
     * Prints the given map with nice line breaks.
     * <p>
     * This method prints a nicely formatted String describing the Map.
     * Each map entry will be printed with key and value.
     * When the value is a Map, recursive behaviour occurs.
     * <p>
     * This method is NOT thread-safe in any special way. You must manually
     * synchronize on either this class or the stream as required.
     *
     * @param out  the stream to print to, must not be null
     * @param label  The label to be used, may be <code>null</code>.
     *  If <code>null</code>, the label is not output.
     *  It typically represents the name of the property in a bean or similar.
     * @param map  The map to print, may be <code>null</code>.
     *  If <code>null</code>, the text 'null' is output.
     * @throws NullPointerException if the stream is <code>null</code>
     */
    public static void verbosePrint(
        final PrintStream out,
        final Object label,
        final Map<?, ?> map) {

        verbosePrintInternal(out, label, map, new ArrayStack<Map<?, ?>>(), false);
    }

    /**
     * Prints the given map with nice line breaks.
     * <p>
     * This method prints a nicely formatted String describing the Map.
     * Each map entry will be printed with key, value and value classname.
     * When the value is a Map, recursive behaviour occurs.
     * <p>
     * This method is NOT thread-safe in any special way. You must manually
     * synchronize on either this class or the stream as required.
     *
     * @param out  the stream to print to, must not be null
     * @param label  The label to be used, may be <code>null</code>.
     *  If <code>null</code>, the label is not output.
     *  It typically represents the name of the property in a bean or similar.
     * @param map  The map to print, may be <code>null</code>.
     *  If <code>null</code>, the text 'null' is output.
     * @throws NullPointerException if the stream is <code>null</code>
     */
    public static void debugPrint(
        final PrintStream out,
        final Object label,
        final Map<?, ?> map) {

        verbosePrintInternal(out, label, map, new ArrayStack<Map<?, ?>>(), true);
    }

    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     * Implementation providing functionality for {@link #debugPrint} and for
     * {@link #verbosePrint}.  This prints the given map with nice line breaks.
     * If the debug flag is true, it additionally prints the type of the object
     * value.  If the contents of a map include the map itself, then the text
     * <em>(this Map)</em> is printed out.  If the contents include a
     * parent container of the map, the the text <em>(ancestor[i] Map)</em> is
     * printed, where i actually indicates the number of levels which must be
     * traversed in the sequential list of ancestors (e.g. father, grandfather,
     * great-grandfather, etc).
     *
     * @param out  the stream to print to
     * @param label  the label to be used, may be <code>null</code>.
     *  If <code>null</code>, the label is not output.
     *  It typically represents the name of the property in a bean or similar.
     * @param map  the map to print, may be <code>null</code>.
     *  If <code>null</code>, the text 'null' is output
     * @param lineage  a stack consisting of any maps in which the previous
     *  argument is contained. This is checked to avoid infinite recursion when
     *  printing the output
     * @param debug  flag indicating whether type names should be output.
     * @throws NullPointerException if the stream is <code>null</code>
     */
    private static void verbosePrintInternal(
        final PrintStream out,
        final Object label,
        final Map<?, ?> map,
        final ArrayStack<Map<?, ?>> lineage,
        final boolean debug) {

        printIndent(out, lineage.size());

        if (map == null) {
            if (label != null) {
                out.print(label);
                out.print(" = ");
            }
            out.println("null");
            return;
        }
        if (label != null) {
            out.print(label);
            out.println(" = ");
        }

        printIndent(out, lineage.size());
        out.println("{");

        lineage.push(map);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object childKey = entry.getKey();
            Object childValue = entry.getValue();
            if (childValue instanceof Map && !lineage.contains(childValue)) {
                verbosePrintInternal(
                    out,
                    (childKey == null ? "null" : childKey),
                    (Map<?, ?>) childValue,
                    lineage,
                    debug);
            } else {
                printIndent(out, lineage.size());
                out.print(childKey);
                out.print(" = ");

                final int lineageIndex = lineage.indexOf(childValue);
                if (lineageIndex == -1) {
                    out.print(childValue);
                } else if (lineage.size() - 1 == lineageIndex) {
                    out.print("(this Map)");
                } else {
                    out.print(
                        "(ancestor["
                            + (lineage.size() - 1 - lineageIndex - 1)
                            + "] Map)");
                }

                if (debug && childValue != null) {
                    out.print(' ');
                    out.println(childValue.getClass().getName());
                } else {
                    out.println();
                }
            }
        }

        lineage.pop();

        printIndent(out, lineage.size());
        out.println(debug ? "} " + map.getClass().getName() : "}");
    }

    /**
     * Writes indentation to the given stream.
     *
     * @param out  the stream to indent
     */
    private static void printIndent(final PrintStream out, final int indent) {
        for (int i = 0; i < indent; i++) {
            out.print(INDENT_STRING);
        }
    }

    // Misc
    //-----------------------------------------------------------------------
    /**
     * Inverts the supplied map returning a new HashMap such that the keys of
     * the input are swapped with the values.
     * <p>
     * This operation assumes that the inverse mapping is well defined.
     * If the input map had multiple entries with the same value mapped to
     * different keys, the returned map will map one of those keys to the
     * value, but the exact key which will be mapped is undefined.
     *
     * @param map  the map to invert, may not be null
     * @return a new HashMap containing the inverted data
     * @throws NullPointerException if the map is null
     */
    public static <K, V> Map<V, K> invertMap(Map<K, V> map) {
        Map<V, K> out = new HashMap<V, K>(map.size());
        for (Iterator<Map.Entry<K, V>> it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry<K, V> entry = it.next();
            out.put(entry.getValue(), entry.getKey());
        }
        return out;
    }

    //-----------------------------------------------------------------------
    /**
     * Protects against adding null values to a map.
     * <p>
     * This method checks the value being added to the map, and if it is null
     * it is replaced by an empty string.
     * <p>
     * This could be useful if the map does not accept null values, or for
     * receiving data from a source that may provide null or empty string
     * which should be held in the same way in the map.
     * <p>
     * Keys are not validated.
     * Note that this method can be used to circumvent the map's
     * value type at runtime.
     *
     * @param map  the map to add to, may not be null
     * @param key  the key
     * @param value  the value, null converted to ""
     * @throws NullPointerException if the map is null
     */
    public static <K> void safeAddToMap(Map<? super K, Object> map, K key, Object value) throws NullPointerException {
        map.put(key, value == null ? "" : value);
    }

    //-----------------------------------------------------------------------
    /**
     * Puts all the keys and values from the specified array into the map.
     * <p>
     * This method is an alternative to the {@link java.util.Map#putAll(java.util.Map)}
     * method and constructors. It allows you to build a map from an object array
     * of various possible styles.
     * <p>
     * If the first entry in the object array implements {@link java.util.Map.Entry}
     * or {@link KeyValue} then the key and value are added from that object.
     * If the first entry in the object array is an object array itself, then
     * it is assumed that index 0 in the sub-array is the key and index 1 is the value.
     * Otherwise, the array is treated as keys and values in alternate indices.
     * <p>
     * For example, to create a color map:
     * <pre>
     * Map colorMap = MapUtils.putAll(new HashMap(), new String[][] {
     *     {"RED", "#FF0000"},
     *     {"GREEN", "#00FF00"},
     *     {"BLUE", "#0000FF"}
     * });
     * </pre>
     * or:
     * <pre>
     * Map colorMap = MapUtils.putAll(new HashMap(), new String[] {
     *     "RED", "#FF0000",
     *     "GREEN", "#00FF00",
     *     "BLUE", "#0000FF"
     * });
     * </pre>
     * or:
     * <pre>
     * Map colorMap = MapUtils.putAll(new HashMap(), new Map.Entry[] {
     *     new DefaultMapEntry("RED", "#FF0000"),
     *     new DefaultMapEntry("GREEN", "#00FF00"),
     *     new DefaultMapEntry("BLUE", "#0000FF")
     * });
     * </pre>
     *
     * @param map  the map to populate, must not be null
     * @param array  an array to populate from, null ignored
     * @return the input map
     * @throws NullPointerException  if map is null
     * @throws IllegalArgumentException  if sub-array or entry matching used and an
     *  entry is invalid
     * @throws ClassCastException if the array contents is mixed
     * @since Commons Collections 3.2
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> putAll(Map<K, V> map, Object[] array) {
        map.size();  // force NPE
        if (array == null || array.length == 0) {
            return map;
        }
        Object obj = array[0];
        if (obj instanceof Map.Entry) {
            for (int i = 0; i < array.length; i++) {
                Map.Entry<K, V> entry = (Map.Entry<K, V>) array[i];
                map.put(entry.getKey(), entry.getValue());
            }
        } else if (obj instanceof KeyValue) {
            for (int i = 0; i < array.length; i++) {
                KeyValue<K, V> keyval = (KeyValue<K, V>) array[i];
                map.put(keyval.getKey(), keyval.getValue());
            }
        } else if (obj instanceof Object[]) {
            for (int i = 0; i < array.length; i++) {
                Object[] sub = (Object[]) array[i];
                if (sub == null || sub.length < 2) {
                    throw new IllegalArgumentException("Invalid array element: " + i);
                }
                map.put((K) sub[0], (V) sub[1]);
            }
        } else {
            for (int i = 0; i < array.length - 1;) {
                map.put((K) array[i++], (V) array[i++]);
            }
        }
        return map;
    }

    //-----------------------------------------------------------------------
    /**
     * Null-safe check if the specified map is empty.
     * <p>
     * Null returns true.
     *
     * @param map  the map to check, may be null
     * @return true if empty or null
     * @since Commons Collections 3.2
     */
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Map map) {
        return (map == null || map.isEmpty());
    }

    /**
     * Null-safe check if the specified map is not empty.
     * <p>
     * Null returns false.
     *
     * @param map  the map to check, may be null
     * @return true if non-null and non-empty
     * @since Commons Collections 3.2
     */
    @SuppressWarnings("unchecked")
    public static boolean isNotEmpty(Map map) {
        return !MapUtils.isEmpty(map);
    }

    // Map decorators
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized map backed by the given map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Map m = MapUtils.synchronizedMap(myMap);
     * Set s = m.keySet();  // outside synchronized block
     * synchronized (m) {  // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()) {
     *         process (i.next());
     *     }
     * }
     * </pre>
     *
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param map  the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static <K, V> Map<K, V> synchronizedMap(Map<K, V> map) {
        return Collections.synchronizedMap(map);
    }

    /**
     * Returns an unmodifiable map backed by the given map.
     * <p>
     * This method uses the implementation in the decorators subpackage.
     *
     * @param map  the map to make unmodifiable, must not be null
     * @return an unmodifiable map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        return UnmodifiableMap.decorate(map);
    }

    /**
     * Returns a predicated (validating) map backed by the given map.
     * <p>
     * Only objects that pass the tests in the given predicates can be added to the map.
     * Trying to add an invalid object results in an IllegalArgumentException.
     * Keys must pass the key predicate, values must pass the value predicate.
     * It is important not to use the original map after invoking this method,
     * as it is a backdoor for adding invalid objects.
     *
     * @param map  the map to predicate, must not be null
     * @param keyPred  the predicate for keys, null means no check
     * @param valuePred  the predicate for values, null means no check
     * @return a predicated map backed by the given map
     * @throws IllegalArgumentException  if the Map is null
     */
    public static <K, V> IterableMap<K, V> predicatedMap(Map<K, V> map, Predicate<? super K> keyPred, Predicate<? super V> valuePred) {
        return PredicatedMap.decorate(map, keyPred, valuePred);
    }

    /**
     * Returns a transformed map backed by the given map.
     * <p>
     * This method returns a new map (decorating the specified map) that
     * will transform any new entries added to it.
     * Existing entries in the specified map will not be transformed.
     * If you want that behaviour, see {@link TransformedMap#decorateTransform}.
     * <p>
     * Each object is passed through the transformers as it is added to the
     * Map. It is important not to use the original map after invoking this
     * method, as it is a backdoor for adding untransformed objects.
     * <p>
     * If there are any elements already in the map being decorated, they
     * are NOT transformed.
     *
     * @param map  the map to transform, must not be null, typically empty
     * @param keyTransformer  the transformer for the map keys, null means no transformation
     * @param valueTransformer  the transformer for the map values, null means no transformation
     * @return a transformed map backed by the given map
     * @throws IllegalArgumentException  if the Map is null
     */
    public static <K, V> IterableMap<K, V> transformedMap(Map<K, V> map,
            Transformer<? super K, ? extends K> keyTransformer,
            Transformer<? super V, ? extends V> valueTransformer) {
        return TransformedMap.decorate(map, keyTransformer, valueTransformer);
    }

    /**
     * Returns a fixed-sized map backed by the given map.
     * Elements may not be added or removed from the returned map, but
     * existing elements can be changed (for instance, via the
     * {@link Map#put(Object,Object)} method).
     *
     * @param map  the map whose size to fix, must not be null
     * @return a fixed-size map backed by that map
     * @throws IllegalArgumentException  if the Map is null
     */
    public static <K, V> IterableMap<K, V> fixedSizeMap(Map<K, V> map) {
        return FixedSizeMap.decorate(map);
    }

    /**
     * Returns a "lazy" map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key.
     * <p>
     * For instance:
     * <pre>
     * Factory factory = new Factory() {
     *     public Object create() {
     *         return new Date();
     *     }
     * }
     * Map lazyMap = MapUtils.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("test");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
     * instance is the value for the <code>"test"</code> key in the map.
     *
     * @param map  the map to make lazy, must not be null
     * @param factory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Factory is null
     */
    public static <K, V> IterableMap<K, V> lazyMap(Map<K, V> map, Factory<? extends V> factory) {
        return LazyMap.getLazyMap(map, factory);
    }

    /**
     * Returns a "lazy" map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key. The factory is a {@link Transformer}
     * that will be passed the key which it must transform into the value.
     * <p>
     * For instance:
     * <pre>
     * Transformer factory = new Transformer() {
     *     public Object transform(Object mapKey) {
     *         return new File(mapKey);
     *     }
     * }
     * Map lazyMap = MapUtils.lazyMap(new HashMap(), factory);
     * Object obj = lazyMap.get("C:/dev");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>File</code> instance for the C drive dev directory.
     * Furthermore, that <code>File</code> instance is the value for the
     * <code>"C:/dev"</code> key in the map.
     * <p>
     * If a lazy map is wrapped by a synchronized map, the result is a simple
     * synchronized cache. When an object is not is the cache, the cache itself
     * calls back to the factory Transformer to populate itself, all within the
     * same synchronized block.
     *
     * @param map  the map to make lazy, must not be null
     * @param transformerFactory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Transformer is null
     */
    public static <K, V> IterableMap<K, V> lazyMap(Map<K, V> map, Transformer<? super K, ? extends V> transformerFactory) {
        return LazyMap.getLazyMap(map, transformerFactory);
    }

    /**
     * Returns a map that maintains the order of keys that are added
     * backed by the given map.
     * <p>
     * If a key is added twice, the order is determined by the first add.
     * The order is observed through the keySet, values and entrySet.
     *
     * @param map  the map to order, must not be null
     * @return an ordered map backed by the given map
     * @throws IllegalArgumentException  if the Map is null
     */
    public static <K, V> OrderedMap<K, V> orderedMap(Map<K, V> map) {
        return ListOrderedMap.decorate(map);
    }

    /**
     * Creates a mult-value map backed by the given map which returns
     * collections of type ArrayList.
     *
     * @param map  the map to decorate
     * @return a multi-value map backed by the given map which returns ArrayLists of values.
     * @see MultiValueMap
     * @since Commons Collections 3.2
     */
    public static <K, V> MultiValueMap<K, V> multiValueMap(Map<K, ? super Collection<V>> map) {
        return MultiValueMap.<K, V>decorate(map);
    }

    /**
     * Creates a multi-value map backed by the given map which returns
     * collections of the specified type.
     *
     * @param map  the map to decorate
     * @param collectionClass  the type of collections to return from the map (must contain public no-arg constructor
     *  and extend Collection).
     * @return a multi-value map backed by the given map which returns collections of the specified type
     * @see MultiValueMap
     * @since Commons Collections 3.2
     */
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, C> map, Class<C> collectionClass) {
        return MultiValueMap.decorate(map, collectionClass);
    }

    /**
     * Creates a multi-value map backed by the given map which returns
     * collections created by the specified collection factory.
     *
     * @param map  the map to decorate
     * @param collectionFactory  a factor which creates collection objects
     * @return a multi-value map backed by the given map which returns collections
     * created by the specified collection factory
     * @see MultiValueMap
     * @since Commons Collections 3.2
     */
    public static <K, V, C extends Collection<V>> MultiValueMap<K, V> multiValueMap(Map<K, C> map, Factory<C> collectionFactory) {
        return MultiValueMap.decorate(map, collectionFactory);
    }

    // SortedMap decorators
    //-----------------------------------------------------------------------
    /**
     * Returns a synchronized sorted map backed by the given sorted map.
     * <p>
     * You must manually synchronize on the returned buffer's iterator to
     * avoid non-deterministic behavior:
     *
     * <pre>
     * Map m = MapUtils.synchronizedSortedMap(myMap);
     * Set s = m.keySet();  // outside synchronized block
     * synchronized (m) {  // synchronized on MAP!
     *     Iterator i = s.iterator();
     *     while (i.hasNext()) {
     *         process (i.next());
     *     }
     * }
     * </pre>
     *
     * This method uses the implementation in {@link java.util.Collections Collections}.
     *
     * @param map  the map to synchronize, must not be null
     * @return a synchronized map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static <K, V> SortedMap<K, V> synchronizedSortedMap(SortedMap<K, V> map) {
        return Collections.synchronizedSortedMap(map);
    }

    /**
     * Returns an unmodifiable sorted map backed by the given sorted map.
     * <p>
     * This method uses the implementation in the decorators subpackage.
     *
     * @param map  the sorted map to make unmodifiable, must not be null
     * @return an unmodifiable map backed by the given map
     * @throws IllegalArgumentException  if the map is null
     */
    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(SortedMap<K, V> map) {
        return UnmodifiableSortedMap.decorate(map);
    }

    /**
     * Returns a predicated (validating) sorted map backed by the given map.
     * <p>
     * Only objects that pass the tests in the given predicates can be added to the map.
     * Trying to add an invalid object results in an IllegalArgumentException.
     * Keys must pass the key predicate, values must pass the value predicate.
     * It is important not to use the original map after invoking this method,
     * as it is a backdoor for adding invalid objects.
     *
     * @param map  the map to predicate, must not be null
     * @param keyPred  the predicate for keys, null means no check
     * @param valuePred  the predicate for values, null means no check
     * @return a predicated map backed by the given map
     * @throws IllegalArgumentException  if the SortedMap is null
     */
    public static <K, V> SortedMap<K, V> predicatedSortedMap(SortedMap<K, V> map,
            Predicate<? super K> keyPred, Predicate<? super V> valuePred) {
        return PredicatedSortedMap.decorate(map, keyPred, valuePred);
    }

    /**
     * Returns a transformed sorted map backed by the given map.
     * <p>
     * This method returns a new sorted map (decorating the specified map) that
     * will transform any new entries added to it.
     * Existing entries in the specified map will not be transformed.
     * If you want that behaviour, see {@link TransformedSortedMap#decorateTransform}.
     * <p>
     * Each object is passed through the transformers as it is added to the
     * Map. It is important not to use the original map after invoking this
     * method, as it is a backdoor for adding untransformed objects.
     * <p>
     * If there are any elements already in the map being decorated, they
     * are NOT transformed.
     *
     * @param map  the map to transform, must not be null, typically empty
     * @param keyTransformer  the transformer for the map keys, null means no transformation
     * @param valueTransformer  the transformer for the map values, null means no transformation
     * @return a transformed map backed by the given map
     * @throws IllegalArgumentException  if the SortedMap is null
     */
    public static <K, V> SortedMap<K, V> transformedSortedMap(SortedMap<K, V> map,
            Transformer<? super K, ? extends K> keyTransformer,
            Transformer<? super V, ? extends V> valueTransformer) {
        return TransformedSortedMap.decorate(map, keyTransformer, valueTransformer);
    }

    /**
     * Returns a fixed-sized sorted map backed by the given sorted map.
     * Elements may not be added or removed from the returned map, but
     * existing elements can be changed (for instance, via the
     * {@link Map#put(Object,Object)} method).
     *
     * @param map  the map whose size to fix, must not be null
     * @return a fixed-size map backed by that map
     * @throws IllegalArgumentException  if the SortedMap is null
     */
    public static <K, V> SortedMap<K, V> fixedSizeSortedMap(SortedMap<K, V> map) {
        return FixedSizeSortedMap.decorate(map);
    }

    /**
     * Returns a "lazy" sorted map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key.
     * <p>
     * For instance:
     *
     * <pre>
     * Factory factory = new Factory() {
     *     public Object create() {
     *         return new Date();
     *     }
     * }
     * SortedMap lazy = MapUtils.lazySortedMap(new TreeMap(), factory);
     * Object obj = lazy.get("test");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
     * instance is the value for the <code>"test"</code> key.
     *
     * @param map  the map to make lazy, must not be null
     * @param factory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the SortedMap or Factory is null
     */
    public static <K, V> SortedMap<K, V> lazySortedMap(SortedMap<K, V> map,
            Factory<? extends V> factory) {
        return LazySortedMap.getLazySortedMap(map, factory);
    }

    /**
     * Returns a "lazy" sorted map whose values will be created on demand.
     * <p>
     * When the key passed to the returned map's {@link Map#get(Object)}
     * method is not present in the map, then the factory will be used
     * to create a new object and that object will become the value
     * associated with that key. The factory is a {@link Transformer}
     * that will be passed the key which it must transform into the value.
     * <p>
     * For instance:
     * <pre>
     * Transformer factory = new Transformer() {
     *     public Object transform(Object mapKey) {
     *         return new File(mapKey);
     *     }
     * }
     * SortedMap lazy = MapUtils.lazySortedMap(new TreeMap(), factory);
     * Object obj = lazy.get("C:/dev");
     * </pre>
     *
     * After the above code is executed, <code>obj</code> will contain
     * a new <code>File</code> instance for the C drive dev directory.
     * Furthermore, that <code>File</code> instance is the value for the
     * <code>"C:/dev"</code> key in the map.
     * <p>
     * If a lazy map is wrapped by a synchronized map, the result is a simple
     * synchronized cache. When an object is not is the cache, the cache itself
     * calls back to the factory Transformer to populate itself, all within the
     * same synchronized block.
     *
     * @param map  the map to make lazy, must not be null
     * @param transformerFactory  the factory for creating new objects, must not be null
     * @return a lazy map backed by the given map
     * @throws IllegalArgumentException  if the Map or Transformer is null
     */
    public static <K, V> SortedMap<K, V> lazySortedMap(SortedMap<K, V> map,
            Transformer<? super K, ? extends V> transformerFactory) {
        return LazySortedMap.getLazySortedMap(map, transformerFactory);
    }

    /**
     * <p>
     * Populates a Map using the supplied <code>Transformer</code> to transform the collection
     * values into keys, using the unaltered collection value as the value in the <code>Map</code>.
     * </p>
     * @param map the <code>Map</code> to populate.
     * @param collection the <code>Collection</code> to use as input values for the map.
     * @param keyTransformer the <code>Transformer</code> used to transform the collection value into a key value
     * @throws NullPointerException if the map, collection or transformer are null
     */
    // TODO: Generics
    public static void populateMap(Map map, Collection collection, Transformer keyTransformer) {
        populateMap(map, collection, keyTransformer, TransformerUtils.nopTransformer());
    }

    /**
     * <p>
     * Populates a Map using the supplied <code>Transformer</code>s to transform the collection
     * values into keys and values.
     * </p>
     * @param map the <code>Map</code> to populate.
     * @param collection the <code>Collection</code> to use as input values for the map.
     * @param keyTransformer the <code>Transformer</code> used to transform the collection value into a key value
     * @param valueTransformer the <code>Transformer</code> used to transform the collection value into a value
     * @throws NullPointerException if the map, collection or transformers are null
     */
    // TODO: Generics
    public static void populateMap(Map map, Collection collection, Transformer keyTransformer, Transformer valueTransformer) {
        Iterator iter = collection.iterator();
        while (iter.hasNext()) {
            Object temp = iter.next();
            map.put(keyTransformer.transform(temp), valueTransformer.transform(temp));
        }
    }

    /**
     * Get the specified {@link Map} as an {@link IterableMap}.
     * @param <K>
     * @param <V>
     * @param map to wrap if necessary.
     * @return IterableMap<K, V>
     * @since Commons Collections 5
     * @TODO fix version
     */
    public static <K, V> IterableMap<K, V> iterableMap(Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        return map instanceof IterableMap ? (IterableMap<K, V>) map
                : new AbstractMapDecorator<K, V>(map) {
                };
    }

    /**
     * Get the specified {@link SortedMap} as an {@link IterableSortedMap}.
     * @param <K>
     * @param <V>
     * @param sortedMap to wrap if necessary
     * @return {@link IterableSortedMap}<K, V>
     * @since Commons Collections 5
     * @TODO fix version
     */
    public static <K, V> IterableSortedMap<K, V> iterableSortedMap(SortedMap<K, V> sortedMap) {
        if (sortedMap == null) {
            throw new IllegalArgumentException("Map must not be null");
        }
        return sortedMap instanceof IterableSortedMap ? (IterableSortedMap<K, V>) sortedMap
                : new AbstractSortedMapDecorator<K, V>(sortedMap) {
                };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14954.java