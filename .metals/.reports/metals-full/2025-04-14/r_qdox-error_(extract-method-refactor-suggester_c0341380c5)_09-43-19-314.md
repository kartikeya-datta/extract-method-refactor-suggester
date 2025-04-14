error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12919.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12919.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12919.java
text:
```scala
r@@esult.put(transformKey(entry.getKey()), transformValue(entry.getValue()));

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
package org.apache.commons.collections.splitmap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.Get;
import org.apache.commons.collections.Put;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LinkedMap;

/**
 * Decorates another <code>Map</code> to transform objects that are added.
 * <p>
 * The Map put methods and Map.Entry setValue method are affected by this class.
 * Thus objects must be removed or searched for using their transformed form.
 * For example, if the transformation converts Strings to Integers, you must use
 * the Integer form to remove objects.
 * <p>
 * <strong>Note that TransformedMap is not synchronized and is not
 * thread-safe.</strong> If you wish to use this map from multiple threads
 * concurrently, you must use appropriate synchronization. The simplest approach
 * is to wrap this map using {@link java.util.Collections#synchronizedMap(Map)}.
 * This class may throw exceptions when accessed by concurrent threads without
 * synchronization.
 * <p>
 * The "put" and "get" type constraints of this class are mutually independent;
 * contrast with {@link org.apache.commons.collections.map.TransformedMap} which,
 * by virtue of its implementing {@link Map}&lt;K, V&gt;, must be constructed in such
 * a way that its read and write parameters are generalized to a common (super-)type.
 * In practice this would often mean <code>&gt;Object, Object&gt;</code>, defeating
 * much of the usefulness of having parameterized types.
 * <p>
 * On the downside, this class is not a drop-in replacement for {@link java.util.Map}
 * but is intended to be worked with either directly or by {@link Put} and {@link Get}
 * generalizations.
 *
 * @since Commons Collections 5
 * @TODO fix version
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 * @author Matt Benson
 */
public class TransformedMap<J, K, U, V> extends AbstractIterableGetMapDecorator<K, V> implements
        Put<J, U>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 5966875321133456994L;

    /** The transformer to use for the key */
    private final Transformer<? super J, ? extends K> keyTransformer;
    /** The transformer to use for the value */
    private final Transformer<? super U, ? extends V> valueTransformer;

    /**
     * Factory method to create a transforming map.
     * <p>
     * If there are any elements already in the map being decorated, they are
     * NOT transformed.
     *
     * @param map the map to decorate, must not be null
     * @param keyTransformer the transformer to use for key conversion, null
     * means no transformation
     * @param valueTransformer the transformer to use for value conversion, null
     * means no transformation
     * @throws IllegalArgumentException if map is null
     */
    public static <J, K, U, V> TransformedMap<J, K, U, V> decorate(Map<K, V> map,
            Transformer<? super J, ? extends K> keyTransformer,
            Transformer<? super U, ? extends V> valueTransformer) {
        return new TransformedMap<J, K, U, V>(map, keyTransformer, valueTransformer);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the collection being decorated, they
     * are NOT transformed.
     *
     * @param map the map to decorate, must not be null
     * @param keyTransformer the transformer to use for key conversion, null
     * means no conversion
     * @param valueTransformer the transformer to use for value conversion, null
     * means no conversion
     * @throws IllegalArgumentException if map is null
     */
    protected TransformedMap(Map<K, V> map, Transformer<? super J, ? extends K> keyTransformer,
            Transformer<? super U, ? extends V> valueTransformer) {
        super(map);
        if (keyTransformer == null) {
            throw new IllegalArgumentException("keyTransformer cannot be null");
        }
        this.keyTransformer = keyTransformer;
        if (valueTransformer == null) {
            throw new IllegalArgumentException("valueTransformer cannot be null");
        }
        this.valueTransformer = valueTransformer;
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     *
     * @param out the output stream
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(decorated());
    }

    /**
     * Read the map in using a custom routine.
     *
     * @param in the input stream
     * @throws IOException
     * @throws ClassNotFoundException
     * @since Commons Collections 3.1
     */
    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        map = (Map) in.readObject();
    }

    //-----------------------------------------------------------------------
    /**
     * Transforms a key.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param object the object to transform
     * @throws the transformed object
     */
    protected K transformKey(J object) {
        return keyTransformer.transform(object);
    }

    /**
     * Transforms a value.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param object the object to transform
     * @throws the transformed object
     */
    protected V transformValue(U object) {
        return valueTransformer.transform(object);
    }

    /**
     * Transforms a map.
     * <p>
     * The transformer itself may throw an exception if necessary.
     *
     * @param map the map to transform
     * @throws the transformed object
     */
    @SuppressWarnings("unchecked")
    protected Map<K, V> transformMap(Map<? extends J, ? extends U> map) {
        if (map.isEmpty()) {
            return (Map<K, V>) map;
        }
        Map<K, V> result = new LinkedMap<K, V>(map.size());

        for (Map.Entry<? extends J, ? extends U> entry : map.entrySet()) {
            result.put((K) transformKey(entry.getKey()), transformValue(entry.getValue()));
        }
        return result;
    }

    /**
     * Override to transform the value when using <code>setValue</code>.
     *
     * @param value the value to transform
     * @return the transformed value
     */
    protected V checkSetValue(U value) {
        return valueTransformer.transform(value);
    }

    //-----------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    public V put(J key, U value) {
        return decorated().put(transformKey(key), transformValue(value));
    }

    /**
     * {@inheritDoc}
     */
    public void putAll(Map<? extends J, ? extends U> mapToCopy) {
        decorated().putAll(transformMap(mapToCopy));
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        decorated().clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12919.java