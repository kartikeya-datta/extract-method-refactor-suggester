error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12913.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12913.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12913.java
text:
```scala
m@@ap = (Map<K, V>) in.readObject();

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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.IterableMap;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.FactoryTransformer;

/**
 * Decorates another <code>Map</code> returning a default value if the map
 * does not contain the requested key.
 * <p>
 * When the {@link #get(Object)} method is called with a key that does not
 * exist in the map, this map will return the default value specified in
 * the constructor/factory. Only the get method is altered, so the
 * {@link Map#containsKey(Object)} can be used to determine if a key really
 * is in the map or not.
 * <p>
 * The defaulted value is not added to the map.
 * Compare this behaviour with {@link LazyMap}, which does add the value
 * to the map (via a Transformer).
 * <p>
 * For instance:
 * <pre>
 * Map map = new DefaultedMap("NULL");
 * Object obj = map.get("Surname");
 * // obj == "NULL"
 * </pre>
 * After the above code is executed the map is still empty.
 * <p>
 * <strong>Note that DefaultedMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap(Map)}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 *
 * @since Commons Collections 3.2
 * @version $Revision: 1.7 $ $Date$
 *
 * @author Stephen Colebourne
 * @author Rafael U.C. Afonso
 * @see LazyMap
 */
public class DefaultedMap<K, V> extends AbstractMapDecorator<K, V> implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 19698628745827L;

    /** The transformer to use if the map does not contain a key */
    private final Transformer<? super K, ? extends V> value;

    //-----------------------------------------------------------------------
    /**
     * Factory method to create a defaulting map.
     * <p>
     * The value specified is returned when a missing key is found.
     * 
     * @param map  the map to decorate, must not be null
     * @param defaultValue  the default value to return when the key is not found
     * @throws IllegalArgumentException if map is null
     */
    public static <K, V> Map<K, V> decorate(Map<K, V> map, V defaultValue) {
        return new DefaultedMap<K, V>(map, ConstantTransformer.getInstance(defaultValue));
    }

    /**
     * Factory method to create a defaulting map.
     * <p>
     * The factory specified is called when a missing key is found.
     * The result will be returned as the result of the map get(key) method.
     * 
     * @param map  the map to decorate, must not be null
     * @param factory  the factory to use to create entries, must not be null
     * @throws IllegalArgumentException if map or factory is null
     */
    public static <K, V> IterableMap<K, V> decorate(Map<K, V> map, Factory<? extends V> factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        return new DefaultedMap<K, V>(map, FactoryTransformer.getInstance(factory));
    }

    /**
     * Factory method to create a defaulting map.
     * <p>
     * The transformer specified is called when a missing key is found.
     * The key is passed to the transformer as the input, and the result
     * will be returned as the result of the map get(key) method.
     * 
     * @param map  the map to decorate, must not be null
     * @param transformer  the transformer to use as a factory to create entries, must not be null
     * @throws IllegalArgumentException if map or factory is null
     */
    public static <K, V> Map<K, V> decorate(Map<K, V> map, Transformer<? super K, ? extends V> transformer) {
        if (transformer == null) {
           throw new IllegalArgumentException("Transformer must not be null");
       }
       return new DefaultedMap<K, V>(map, transformer);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs a new empty <code>DefaultedMap</code> that decorates
     * a <code>HashMap</code>.
     * <p>
     * The object passed in will be returned by the map whenever an
     * unknown key is requested.
     * 
     * @param defaultValue  the default value to return when the key is not found
     */
    public DefaultedMap(V defaultValue) {
        this(ConstantTransformer.getInstance(defaultValue));
    }

    /**
     * Constructs a new empty <code>DefaultedMap</code> that decorates
     * a <code>HashMap</code>.
     * <p>
     * @param defaultValueTransformer transformer to use to generate missing values.
     */
    public DefaultedMap(Transformer<? super K, ? extends V> defaultValueTransformer) {
        this(new HashMap<K, V>(), defaultValueTransformer);
    }

    /**
     * Constructor that wraps (not copies).
     * 
     * @param map  the map to decorate, must not be null
     * @param value  the value to use
     * @throws IllegalArgumentException if map or transformer is null
     */
    protected DefaultedMap(Map<K, V> map, Transformer<? super K, ? extends V> defaultValueTransformer) {
        super(map);
        this.value = defaultValueTransformer;
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     * 
     * @param out  the output stream
     * @throws IOException
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
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map) in.readObject();
    }

    //-----------------------------------------------------------------------
    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        // create value for key if key is not currently in the map
        if (map.containsKey(key) == false) {
            return value.transform((K) key);
        }
        return map.get(key);
    }

    // no need to wrap keySet, entrySet or values as they are views of
    // existing map entries - you can't do a map-style get on them.
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12913.java