error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5802.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5802.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5802.java
text:
```scala
public P@@ermutationIterator(final Collection<? extends E> coll) {

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
package org.apache.commons.collections4.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This iterator creates permutations of an input collection, using the
 * Steinhaus-Johnson-Trotter algorithm (also called plain changes).
 * <p>
 * The iterator will return exactly n! permutations of the input collection.
 * The {@code remove()} operation is not supported, and will throw an
 * {@code UnsupportedOperationException}.
 * <p>
 * NOTE: in case an empty collection is provided, the iterator will
 * return exactly one empty list as result, as 0! = 1.
 *
 * @param <E>  the type of the objects being permuted
 *
 * @version $Id$
 * @since 4.0
 */
public class PermutationIterator<E> implements Iterator<List<E>> {

    /**
     * Permutation is done on theses keys to handle equal objects.
     */
    private int[] keys;

    /**
     * Mapping between keys and objects.
     */
    private Map<Integer, E> objectMap;

    /**
     * Direction table used in the algorithm:
     * <ul>
     *   <li>false is left</li>
     *   <li>true is right</li>
     * </ul>
     */
    private boolean[] direction;

    /**
     * Next permutation to return. When a permutation is requested
     * this instance is provided and the next one is computed.
     */
    private List<E> nextPermutation;

    /**
     * Standard constructor for this class.
     * @param coll  the collection to generate permutations for
     * @throws NullPointerException if coll is null
     */
    public PermutationIterator(final Collection<E> coll) {
        if (coll == null) {
            throw new NullPointerException("The collection must not be null");
        }

        keys = new int[coll.size()];
        direction = new boolean[coll.size()];
        Arrays.fill(direction, false);
        int value = 1;
        objectMap = new HashMap<Integer, E>();
        for (E e : coll) {
            objectMap.put(Integer.valueOf(value), e);
            keys[value - 1] = value;
            value++;
        }
        nextPermutation = new ArrayList<E>(coll);
    }

    /**
     * Indicates if there are more permutation available.
     * @return true if there are more permutations, otherwise false
     */
    public boolean hasNext() {
        return nextPermutation != null;
    }

    /**
     * Returns the next permutation of the input collection.
     * @return a list of the permutator's elements representing a permutation
     * @throws NoSuchElementException if there are no more permutations
     */
    public List<E> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        // find the largest mobile integer k
        int indexOfLargestMobileInteger = -1;
        int largestKey = -1;
        for (int i = 0; i < keys.length; i++) {
            if ((direction[i] && i < keys.length - 1 && keys[i] > keys[i + 1]) ||
                (!direction[i] && i > 0 && keys[i] > keys[i - 1])) {
                if (keys[i] > largestKey) {
                    largestKey = keys[i];
                    indexOfLargestMobileInteger = i;
                }
            }
        }
        if (largestKey == -1) {
            List<E> toReturn = nextPermutation;
            nextPermutation = null;
            return toReturn;
        }

        // swap k and the adjacent integer it is looking at
        final int offset = direction[indexOfLargestMobileInteger] ? 1 : -1;
        final int tmpKey = keys[indexOfLargestMobileInteger];
        keys[indexOfLargestMobileInteger] = keys[indexOfLargestMobileInteger + offset];
        keys[indexOfLargestMobileInteger + offset] = tmpKey;
        boolean tmpDirection = direction[indexOfLargestMobileInteger];
        direction[indexOfLargestMobileInteger] = direction[indexOfLargestMobileInteger + offset];
        direction[indexOfLargestMobileInteger + offset] = tmpDirection;

        // reverse the direction of all integers larger than k and build the result
        final List<E> nextP = new ArrayList<E>();
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] > largestKey) {
                direction[i] = !direction[i];
            }
            nextP.add(objectMap.get(Integer.valueOf(keys[i])));
        }
        final List<E> result = nextPermutation;
        nextPermutation = nextP;
        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException("remove() is not supported");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5802.java