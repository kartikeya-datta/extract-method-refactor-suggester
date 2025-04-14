error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9027.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9027.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9027.java
text:
```scala
c@@oll = ((SynchronizedCollection<E>) coll).decorated();

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
package org.apache.commons.collections4.collection;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections4.BoundedCollection;
import org.apache.commons.collections4.iterators.UnmodifiableIterator;

/**
 * {@link UnmodifiableBoundedCollection} decorates another
 * {@link BoundedCollection} to ensure it can't be altered.
 * <p>
 * If a BoundedCollection is first wrapped in some other collection decorator,
 * such as synchronized or predicated, the BoundedCollection methods are no
 * longer accessible.
 * The factory on this class will attempt to retrieve the bounded nature by
 * examining the package scope variables.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 * <p>
 * Attempts to modify it will result in an UnsupportedOperationException. 
 *
 * @since 3.0
 * @version $Id$
 */
public final class UnmodifiableBoundedCollection<E> extends AbstractCollectionDecorator<E>
        implements BoundedCollection<E> {

    /** Serialization version */
    private static final long serialVersionUID = -7112672385450340330L;

    /**
     * Factory method to create an unmodifiable bounded collection.
     *
     * @param <E> the type of the elements in the collection
     * @param coll  the <code>BoundedCollection</code> to decorate, must not be null
     * @return a new unmodifiable bounded collection
     * @throws IllegalArgumentException if {@code coll} is {@code null}
     */
    public static <E> BoundedCollection<E> unmodifiableBoundedCollection(final BoundedCollection<E> coll) {
        return new UnmodifiableBoundedCollection<E>(coll);
    }

    /**
     * Factory method to create an unmodifiable bounded collection.
     * <p>
     * This method is capable of drilling down through up to 1000 other decorators
     * to find a suitable BoundedCollection.
     *
     * @param <E> the type of the elements in the collection
     * @param coll  the <code>BoundedCollection</code> to decorate, must not be null
     * @return a new unmodifiable bounded collection
     * @throws IllegalArgumentException if {@code coll} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <E> BoundedCollection<E> unmodifiableBoundedCollection(Collection<? extends E> coll) {
        if (coll == null) {
            throw new IllegalArgumentException("The collection must not be null");
        }

        // handle decorators
        for (int i = 0; i < 1000; i++) {  // counter to prevent infinite looping
            if (coll instanceof BoundedCollection) {
                break;  // normal loop exit
            }
            if (coll instanceof AbstractCollectionDecorator) {
                coll = ((AbstractCollectionDecorator<E>) coll).collection;
            } else if (coll instanceof SynchronizedCollection) {
                coll = ((SynchronizedCollection<E>) coll).collection;
            }
        }

        if (coll instanceof BoundedCollection == false) {
            throw new IllegalArgumentException("The collection is not a bounded collection");
        }
        return new UnmodifiableBoundedCollection<E>((BoundedCollection<E>) coll);
    }

    /**
     * Constructor that wraps (not copies).
     *
     * @param coll  the collection to decorate, must not be null
     * @throws IllegalArgumentException if coll is null
     */
    private UnmodifiableBoundedCollection(final BoundedCollection<E> coll) {
        super(coll);
    }

    //-----------------------------------------------------------------------
    @Override
    public Iterator<E> iterator() {
        return UnmodifiableIterator.unmodifiableIterator(decorated().iterator());
    }

    @Override
    public boolean add(final E object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> coll) {
        throw new UnsupportedOperationException();
    }

    //-----------------------------------------------------------------------
    public boolean isFull() {
        return decorated().isFull();
    }

    public int maxSize() {
        return decorated().maxSize();
    }

    @Override
    protected BoundedCollection<E> decorated() {
        return (BoundedCollection<E>) super.decorated();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9027.java