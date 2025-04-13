error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9502.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9502.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9502.java
text:
```scala
S@@tring property = System.getProperty("es.cache.recycle");

/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.common;

import gnu.trove.map.hash.*;
import gnu.trove.set.hash.THashSet;

import org.elasticsearch.common.trove.ExtTDoubleObjectHashMap;
import org.elasticsearch.common.trove.ExtTHashMap;
import org.elasticsearch.common.trove.ExtTLongObjectHashMap;

public final class CacheRecycler  {

    private static final Recycler INSTANCE;
    static {
        String property = System.getProperty("ES.RECYCLE");
        if (property != null && !Boolean.parseBoolean(property)) {
            INSTANCE = new NoCacheCacheRecycler();
        } else {
            INSTANCE = new DefaultCacheRecycler();
        }
    }
    
    private CacheRecycler() {
        // no instance
    }

    public static void clear() {
        INSTANCE.clear();
    }

    public static <K, V> ExtTHashMap<K, V> popHashMap() {
        return INSTANCE.popHashMap();
    }

    public static void pushHashMap(ExtTHashMap map) {
        INSTANCE.pushHashMap(map);
    }

    public static <T> THashSet<T> popHashSet() {
        return INSTANCE.popHashSet();
    }

    public static void pushHashSet(THashSet map) {
        INSTANCE.pushHashSet(map);
    }

    public static <T> ExtTDoubleObjectHashMap<T> popDoubleObjectMap() {
        return INSTANCE.popDoubleObjectMap();
    }

    public static void pushDoubleObjectMap(ExtTDoubleObjectHashMap map) {
        INSTANCE.pushDoubleObjectMap(map);
    }

    public static <T> ExtTLongObjectHashMap<T> popLongObjectMap() {
        return INSTANCE.popLongObjectMap();
    }

    public static void pushLongObjectMap(ExtTLongObjectHashMap map) {
        INSTANCE.pushLongObjectMap(map);
    }

    public static TLongLongHashMap popLongLongMap() {
        return INSTANCE.popLongLongMap();
    }

    public static void pushLongLongMap(TLongLongHashMap map) {
        INSTANCE.pushLongLongMap(map);
    }

    public static TIntIntHashMap popIntIntMap() {
        return INSTANCE.popIntIntMap();
    }

    public static void pushIntIntMap(TIntIntHashMap map) {
        INSTANCE.pushIntIntMap(map);
    }

    public static TFloatIntHashMap popFloatIntMap() {
        return INSTANCE.popFloatIntMap();
    }

    public static void pushFloatIntMap(TFloatIntHashMap map) {
        INSTANCE.pushFloatIntMap(map);
    }

    public static TDoubleIntHashMap popDoubleIntMap() {
        return INSTANCE.popDoubleIntMap();
    }

    public static void pushDoubleIntMap(TDoubleIntHashMap map) {
        INSTANCE.pushDoubleIntMap(map);
    }

    public static TByteIntHashMap popByteIntMap() {
        return INSTANCE.popByteIntMap();
    }

    public static void pushByteIntMap(TByteIntHashMap map) {
        INSTANCE.pushByteIntMap(map);
    }

    public static TShortIntHashMap popShortIntMap() {
        return INSTANCE.popShortIntMap();
    }

    public static void pushShortIntMap(TShortIntHashMap map) {
        INSTANCE.pushShortIntMap(map);
    }

    public static TLongIntHashMap popLongIntMap() {
        return INSTANCE.popLongIntMap();
    }

    public static void pushLongIntMap(TLongIntHashMap map) {
        INSTANCE.pushLongIntMap(map);
    }

    public static <T> TObjectIntHashMap<T> popObjectIntMap() {
        return INSTANCE.popObjectIntMap();
    }

    public static <T> void pushObjectIntMap(TObjectIntHashMap<T> map) {
        INSTANCE.pushObjectIntMap(map);
    }

    public static <T> TIntObjectHashMap<T> popIntObjectMap() {
        return INSTANCE.popIntObjectMap();
    }

    public static <T> void pushIntObjectMap(TIntObjectHashMap<T> map) {
        INSTANCE.pushIntObjectMap(map);
    }

    public static <T> TObjectFloatHashMap<T> popObjectFloatMap() {
        return INSTANCE.popObjectFloatMap();
    }

    public static <T> void pushObjectFloatMap(TObjectFloatHashMap<T> map) {
        INSTANCE.pushObjectFloatMap(map);
    }

    public static Object[] popObjectArray(int size) {
        return INSTANCE.popObjectArray(size);
    }

    public static void pushObjectArray(Object[] objects) {
        INSTANCE.pushObjectArray(objects);
    }

    public static int[] popIntArray(int size) {
        return INSTANCE.popIntArray(size);
    }

    public static int[] popIntArray(int size, int sentinal) {
        return INSTANCE.popIntArray(size, sentinal);
    }

    public static void pushIntArray(int[] ints) {
        INSTANCE.pushIntArray(ints);
    }

    public static void pushIntArray(int[] ints, int sentinal) {
        INSTANCE.pushIntArray(ints, sentinal);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9502.java