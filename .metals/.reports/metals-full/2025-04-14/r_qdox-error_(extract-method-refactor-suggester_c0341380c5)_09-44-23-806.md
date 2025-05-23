error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3575.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3575.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3575.java
text:
```scala
k@@ey2 = isKeyType(ReferenceStrength.HARD) ? key2 : ((Reference<?>) key2).get();

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
package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;

/**
 * A <code>Map</code> implementation that allows mappings to be
 * removed by the garbage collector and matches keys and values based
 * on <code>==</code> not <code>equals()</code>.
 * <p>
 * <p>
 * When you construct a <code>ReferenceIdentityMap</code>, you can specify what kind
 * of references are used to store the map's keys and values.
 * If non-hard references are used, then the garbage collector can remove
 * mappings if a key or value becomes unreachable, or if the JVM's memory is
 * running low. For information on how the different reference types behave,
 * see {@link Reference}.
 * <p>
 * Different types of references can be specified for keys and values.
 * The default constructor uses hard keys and soft values, providing a
 * memory-sensitive cache.
 * <p>
 * This map is similar to
 * {@link org.apache.commons.collections4.map.ReferenceMap ReferenceMap}.
 * It differs in that keys and values in this class are compared using <code>==</code>.
 * <p>
 * This map will violate the detail of various Map and map view contracts.
 * As a general rule, don't compare this map to other maps.
 * <p>
 * This {@link java.util.Map Map} implementation does <i>not</i> allow null elements.
 * Attempting to add a null key or value to the map will raise a <code>NullPointerException</code>.
 * <p>
 * This implementation is not synchronized.
 * You can use {@link java.util.Collections#synchronizedMap} to 
 * provide synchronized access to a <code>ReferenceIdentityMap</code>.
 * Remember that synchronization will not stop the garbage collector removing entries.
 * <p>
 * All the available iterators can be reset back to the start by casting to
 * <code>ResettableIterator</code> and calling <code>reset()</code>.
 * <p>
 * <strong>Note that ReferenceIdentityMap is not synchronized and is not thread-safe.</strong>
 * If you wish to use this map from multiple threads concurrently, you must use
 * appropriate synchronization. The simplest approach is to wrap this map
 * using {@link java.util.Collections#synchronizedMap}. This class may throw 
 * exceptions when accessed by concurrent threads without synchronization.
 *
 * @see java.lang.ref.Reference
 *
 * @since 3.0 (previously in main package v2.1)
 * @version $Id$
 */
public class ReferenceIdentityMap<K, V> extends AbstractReferenceMap<K, V> implements Serializable {

    /** Serialization version */
    private static final long serialVersionUID = -1266190134568365852L;

    /**
     * Constructs a new <code>ReferenceIdentityMap</code> that will
     * use hard references to keys and soft references to values.
     */
    public ReferenceIdentityMap() {
        super(ReferenceStrength.HARD, ReferenceStrength.SOFT, DEFAULT_CAPACITY,
                DEFAULT_LOAD_FACTOR, false);
    }

    /**
     * Constructs a new <code>ReferenceIdentityMap</code> that will
     * use the specified types of references.
     *
     * @param keyType  the type of reference to use for keys;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param valueType  the type of reference to use for values;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD},
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT},
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     */
    public ReferenceIdentityMap(final ReferenceStrength keyType, final ReferenceStrength valueType) {
        super(keyType, valueType, DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, false);
    }

    /**
     * Constructs a new <code>ReferenceIdentityMap</code> that will
     * use the specified types of references.
     *
     * @param keyType  the type of reference to use for keys;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param valueType  the type of reference to use for values;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD},
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT},
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param purgeValues should the value be automatically purged when the 
     *   key is garbage collected 
     */
    public ReferenceIdentityMap(final ReferenceStrength keyType, final ReferenceStrength valueType,
            final boolean purgeValues) {
        super(keyType, valueType, DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, purgeValues);
    }

    /**
     * Constructs a new <code>ReferenceIdentityMap</code> with the
     * specified reference types, load factor and initial capacity.
     *
     * @param keyType  the type of reference to use for keys;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param valueType  the type of reference to use for values;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD},
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT},
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param capacity  the initial capacity for the map
     * @param loadFactor  the load factor for the map
     */
    public ReferenceIdentityMap(final ReferenceStrength keyType, final ReferenceStrength valueType,
            final int capacity, final float loadFactor) {
        super(keyType, valueType, capacity, loadFactor, false);
    }

    /**
     * Constructs a new <code>ReferenceIdentityMap</code> with the
     * specified reference types, load factor and initial capacity.
     *
     * @param keyType  the type of reference to use for keys;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT}, 
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param valueType  the type of reference to use for values;
     *   must be {@link AbstractReferenceMap.ReferenceStrength#HARD HARD},
     *   {@link AbstractReferenceMap.ReferenceStrength#SOFT SOFT},
     *   {@link AbstractReferenceMap.ReferenceStrength#WEAK WEAK}
     * @param capacity  the initial capacity for the map
     * @param loadFactor  the load factor for the map
     * @param purgeValues  should the value be automatically purged when the 
     *   key is garbage collected 
     */
    public ReferenceIdentityMap(final ReferenceStrength keyType, final ReferenceStrength valueType,
            final int capacity, final float loadFactor, final boolean purgeValues) {
        super(keyType, valueType, capacity, loadFactor, purgeValues);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the hash code for the key specified.
     * <p>
     * This implementation uses the identity hash code.
     * 
     * @param key  the key to get a hash code for
     * @return the hash code
     */
    @Override
    protected int hash(final Object key) {
        return System.identityHashCode(key);
    }

    /**
     * Gets the hash code for a MapEntry.
     * <p>
     * This implementation uses the identity hash code.
     * 
     * @param key  the key to get a hash code for, may be null
     * @param value  the value to get a hash code for, may be null
     * @return the hash code, as per the MapEntry specification
     */
    @Override
    protected int hashEntry(final Object key, final Object value) {
        return System.identityHashCode(key) ^
               System.identityHashCode(value);
    }

    /**
     * Compares two keys for equals.
     * <p>
     * This implementation converts the key from the entry to a real reference
     * before comparison and uses <code>==</code>.
     * 
     * @param key1  the first key to compare passed in from outside
     * @param key2  the second key extracted from the entry via <code>entry.key</code>
     * @return true if equal by identity
     */
    @Override
    protected boolean isEqualKey(final Object key1, Object key2) {
        key2 = keyType == ReferenceStrength.HARD ? key2 : ((Reference<?>) key2).get();
        return key1 == key2;
    }

    /**
     * Compares two values for equals.
     * <p>
     * This implementation uses <code>==</code>.
     * 
     * @param value1  the first value to compare passed in from outside
     * @param value2  the second value extracted from the entry via <code>getValue()</code>
     * @return true if equal by identity
     */
    @Override
    protected boolean isEqualValue(final Object value1, final Object value2) {
        return value1 == value2;
    }

    //-----------------------------------------------------------------------
    /**
     * Write the map out using a custom routine.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        doWriteObject(out);
    }

    /**
     * Read the map in using a custom routine.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        doReadObject(in);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3575.java