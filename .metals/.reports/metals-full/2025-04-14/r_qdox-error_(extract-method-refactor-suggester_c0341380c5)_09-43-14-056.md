error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14369.java
text:
```scala
public b@@oolean put(K key, V value) {

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
package org.apache.commons.collections4.multimap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LinkedMap;

/**
 * Decorates another <code>MultiValuedMap</code> to transform objects that are added.
 * <p>
 * This class affects the MultiValuedMap put methods. Thus objects must be
 * removed or searched for using their transformed form. For example, if the
 * transformation converts Strings to Integers, you must use the Integer form to
 * remove objects.
 * <p>
 * <strong>Note that TransformedMultiValuedMap is not synchronized and is not thread-safe.</strong>
 *
 * @since 4.1
 * @version $Id$
 */
public class TransformedMultiValuedMap<K, V> extends AbstractMultiValuedMapDecorator<K, V> {

    /** Serialization Version */
    private static final long serialVersionUID = -1254147899086470720L;

    private final Transformer<? super K, ? extends K> keyTransformer;

    private final Transformer<? super V, ? extends V> valueTransformer;

    /**
     * Factory method to create a transforming MultiValuedMap.
     * <p>
     * If there are any elements already in the map being decorated, they are
     * NOT transformed. Contrast this with
     * {@link #transformedMap(MultiValuedMap, Transformer, Transformer)}.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the MultiValuedMap to decorate, must not be null
     * @param keyTransformer the transformer to use for key conversion, null
     *        means no transformation
     * @param valueTransformer the transformer to use for value conversion, null
     *        means no transformation
     * @return a new transformed MultiValuedMap
     * @throws IllegalArgumentException if map is null
     */
    public static <K, V> TransformedMultiValuedMap<K, V> transformingMap(final MultiValuedMap<K, V> map,
            final Transformer<? super K, ? extends K> keyTransformer,
            final Transformer<? super V, ? extends V> valueTransformer) {
        return new TransformedMultiValuedMap<K, V>(map, keyTransformer, valueTransformer);
    }

    /**
     * Factory method to create a transforming MultiValuedMap that will
     * transform existing contents of the specified map.
     * <p>
     * If there are any elements already in the map being decorated, they will
     * be transformed by this method. Contrast this with
     * {@link #transformingMap(MultiValuedMap, Transformer, Transformer)}.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the MultiValuedMap to decorate, must not be null
     * @param keyTransformer the transformer to use for key conversion, null
     *        means no transformation
     * @param valueTransformer the transformer to use for value conversion, null
     *        means no transformation
     * @return a new transformed MultiValuedMap
     * @throws IllegalArgumentException if map is null
     */
    public static <K, V> TransformedMultiValuedMap<K, V> transformedMap(final MultiValuedMap<K, V> map,
            final Transformer<? super K, ? extends K> keyTransformer,
            final Transformer<? super V, ? extends V> valueTransformer) {
        final TransformedMultiValuedMap<K, V> decorated =
                new TransformedMultiValuedMap<K, V>(map, keyTransformer, valueTransformer);
        if (map.size() > 0) {
            MultiValuedMap<K, V> transformed = decorated.transformMultiValuedMap(map);
            decorated.clear();
            // to avoid double transform
            decorated.decorated().putAll(transformed);
        }
        return decorated;
    }

    // -----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the collection being decorated, they
     * are NOT transformed.
     *
     * @param map the MultiValuedMap to decorate, must not be null
     * @param keyTransformer the transformer to use for key conversion, null
     *        means no conversion
     * @param valueTransformer the transformer to use for value conversion, null
     *        means no conversion
     * @throws IllegalArgumentException if map is null
     */
    protected TransformedMultiValuedMap(MultiValuedMap<K, V> map,
            Transformer<? super K, ? extends K> keyTransformer, Transformer<? super V, ? extends V> valueTransformer) {
        super(map);
        this.keyTransformer = keyTransformer;
        this.valueTransformer = valueTransformer;
    }

    /**
     * Transforms a key.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param object the object to transform
     * @return the transformed object
     */
    protected K transformKey(final K object) {
        if (keyTransformer == null) {
            return object;
        }
        return keyTransformer.transform(object);
    }

    /**
     * Transforms a value.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param object the object to transform
     * @return the transformed object
     */
    protected V transformValue(final V object) {
        if (valueTransformer == null) {
            return object;
        }
        return valueTransformer.transform(object);
    }

    /**
     * Transforms a map.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param map the map to transform
     * @return the transformed object
     */
    @SuppressWarnings("unchecked")
    protected Map<K, V> transformMap(final Map<? extends K, ? extends V> map) {
        if (map.isEmpty()) {
            return (Map<K, V>) map;
        }
        final Map<K, V> result = new LinkedMap<K, V>(map.size());

        for (final Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result.put(transformKey(entry.getKey()), transformValue(entry.getValue()));
        }
        return result;
    }

    /**
     * Transforms a MultiValuedMap.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param map the MultiValuedMap to transform
     * @return the transformed object
     */
    @SuppressWarnings("unchecked")
    protected MultiValuedMap<K, V> transformMultiValuedMap(final MultiValuedMap<? extends K, ? extends V> map) {
        if (map.isEmpty()) {
            return (MultiValuedMap<K, V>) map;
        }
        final MultiValuedMap<K, V> result = new MultiValuedHashMap<K, V>();

        for (final Map.Entry<? extends K, ? extends V> entry : map.entries()) {
            result.put(transformKey(entry.getKey()), transformValue(entry.getValue()));
        }
        return result;
    }

    @Override
    public V put(K key, V value) {
        K transformedKey = transformKey(key);
        V transformedValue = transformValue(value);
        return decorated().put(transformedKey, transformedValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean putAll(K key, Iterable<? extends V> values) {
        if (values == null || values.iterator() == null || !values.iterator().hasNext()) {
            return false;
        }
        K transformedKey = transformKey(key);
        List<V> transformedValues = new LinkedList<V>();
        Iterator<V> it = (Iterator<V>) values.iterator();
        while (it.hasNext()) {
            transformedValues.add(transformValue(it.next()));
        }
        return decorated().putAll(transformedKey, transformedValues);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) {
            return;
        }
        decorated().putAll(transformMap(m));
    }

    @Override
    public void putAll(MultiValuedMap<? extends K, ? extends V> m) {
        if (m == null) {
            return;
        }
        decorated().putAll(transformMultiValuedMap(m));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14369.java