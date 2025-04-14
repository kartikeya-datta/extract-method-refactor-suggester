error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5411.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5411.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5411.java
text:
```scala
public static <@@E> List<E> growthList(List<E> list) {

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
package org.apache.commons.collections.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Decorates another <code>List</code> to make it seamlessly grow when
 * indices larger than the list size are used on add and set,
 * avoiding most IndexOutOfBoundsExceptions.
 * <p>
 * This class avoids errors by growing when a set or add method would
 * normally throw an IndexOutOfBoundsException.
 * Note that IndexOutOfBoundsException IS returned for invalid negative indices.
 * <p>
 * Trying to set or add to an index larger than the size will cause the list
 * to grow (using <code>null</code> elements). Clearly, care must be taken
 * not to use excessively large indices, as the internal list will grow to
 * match.
 * <p>
 * Trying to use any method other than add or set with an invalid index will
 * call the underlying list and probably result in an IndexOutOfBoundsException.
 * <p>
 * Take care when using this list with <code>null</code> values, as
 * <code>null</code> is the value added when growing the list.
 * <p>
 * All sub-lists will access the underlying list directly, and will throw
 * IndexOutOfBoundsExceptions.
 * <p>
 * This class differs from {@link LazyList} because here growth occurs on
 * set and add, where <code>LazyList</code> grows on get. However, they
 * can be used together by decorating twice.
 *
 * @see LazyList
 * @since Commons Collections 3.2
 * @version $Revision: 155406 $ $Date$
 *
 * @author Stephen Colebourne
 * @author Paul Legato
 */
public class GrowthList<E> extends AbstractSerializableListDecorator<E> {

    /** Serialization version */
    private static final long serialVersionUID = -3620001881672L;

    /**
     * Factory method to create a growth list.
     *
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    public static <E> List<E> decorate(List<E> list) {
        return new GrowthList<E>(list);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that uses an ArrayList internally.
     */
    public GrowthList() {
        super(new ArrayList<E>());
    }

    /**
     * Constructor that uses an ArrayList internally.
     *
     * @param initialSize  the initial size of the ArrayList
     * @throws IllegalArgumentException if initial size is invalid
     */
    public GrowthList(int initialSize) {
        super(new ArrayList<E>(initialSize));
    }

    /**
     * Constructor that wraps (not copies).
     *
     * @param list  the list to decorate, must not be null
     * @throws IllegalArgumentException if list is null
     */
    protected GrowthList(List<E> list) {
        super(list);
    }

    //-----------------------------------------------------------------------
    /**
     * Decorate the add method to perform the growth behaviour.
     * <p>
     * If the requested index is greater than the current size, the list will
     * grow to the new size. Indices between the old size and the requested
     * size will be filled with <code>null</code>.
     * <p>
     * If the index is less than the current size, the value will be added to
     * the underlying list directly.
     * If the index is less than zero, the underlying list is called, which
     * will probably throw an IndexOutOfBoundsException.
     *
     * @param index  the index to add at
     * @param element  the object to add at the specified index
     * @throws UnsupportedOperationException if the underlying list doesn't implement set
     * @throws ClassCastException if the underlying list rejects the element
     * @throws IllegalArgumentException if the underlying list rejects the element
     */
    @Override
    public void add(int index, E element) {
        int size = decorated().size();
        if (index > size) {
            decorated().addAll(Collections.<E>nCopies(index - size, null));
        }
        decorated().add(index, element);
    }

    //-----------------------------------------------------------------------
    /**
     * Decorate the addAll method to perform the growth behaviour.
     * <p>
     * If the requested index is greater than the current size, the list will
     * grow to the new size. Indices between the old size and the requested
     * size will be filled with <code>null</code>.
     * <p>
     * If the index is less than the current size, the values will be added to
     * the underlying list directly.
     * If the index is less than zero, the underlying list is called, which
     * will probably throw an IndexOutOfBoundsException.
     *
     * @param index  the index to add at
     * @param coll  the collection to add at the specified index
     * @return true if the list changed
     * @throws UnsupportedOperationException if the underlying list doesn't implement set
     * @throws ClassCastException if the underlying list rejects the element
     * @throws IllegalArgumentException if the underlying list rejects the element
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> coll) {
        int size = decorated().size();
        boolean result = false;
        if (index > size) {
            decorated().addAll(Collections.<E>nCopies(index - size, null));
            result = true;
        }
        return (decorated().addAll(index, coll) | result);
    }

    //-----------------------------------------------------------------------
    /**
     * Decorate the set method to perform the growth behaviour.
     * <p>
     * If the requested index is greater than the current size, the list will
     * grow to the new size. Indices between the old size and the requested
     * size will be filled with <code>null</code>.
     * <p>
     * If the index is less than the current size, the value will be set onto
     * the underlying list directly.
     * If the index is less than zero, the underlying list is called, which
     * will probably throw an IndexOutOfBoundsException.
     *
     * @param index  the index to set
     * @param element  the object to set at the specified index
     * @return the object previously at that index
     * @throws UnsupportedOperationException if the underlying list doesn't implement set
     * @throws ClassCastException if the underlying list rejects the element
     * @throws IllegalArgumentException if the underlying list rejects the element
     */
    @Override
    public E set(int index, E element) {
        int size = decorated().size();
        if (index >= size) {
            decorated().addAll(Collections.<E>nCopies((index - size) + 1, null));
        }
        return decorated().set(index, element);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5411.java