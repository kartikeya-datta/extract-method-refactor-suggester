error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11994.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11994.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11994.java
text:
```scala
final E@@[] values = (E[]) set.toArray(); // NOPMD - false positive for generics

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
package org.apache.commons.collections4.set;

import java.util.Comparator;
import java.util.SortedSet;

import org.apache.commons.collections4.Transformer;

/**
 * Decorates another <code>SortedSet</code> to transform objects that are added.
 * <p>
 * The add methods are affected by this class.
 * Thus objects must be removed or searched for using their transformed form.
 * For example, if the transformation converts Strings to Integers, you must
 * use the Integer form to remove objects.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since 3.0
 * @version $Id$
 */
public class TransformedSortedSet<E> extends TransformedSet<E> implements SortedSet<E> {

    /** Serialization version */
    private static final long serialVersionUID = -1675486811351124386L;

    /**
     * Factory method to create a transforming sorted set.
     * <p>
     * If there are any elements already in the set being decorated, they
     * are NOT transformed.
     * Contrast this with {@link #transformedSortedSet(SortedSet, Transformer)}.
     * 
     * @param <E> the element type
     * @param set  the set to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed {@link SortedSet}
     * @throws IllegalArgumentException if set or transformer is null
     */
    public static <E> TransformedSortedSet<E> transformingSortedSet(final SortedSet<E> set,
            final Transformer<? super E, ? extends E> transformer) {
        return new TransformedSortedSet<E>(set, transformer);
    }
    
    /**
     * Factory method to create a transforming sorted set that will transform
     * existing contents of the specified sorted set.
     * <p>
     * If there are any elements already in the set being decorated, they
     * will be transformed by this method.
     * Contrast this with {@link #transformingSortedSet(SortedSet, Transformer)}.
     * 
     * @param <E> the element type
     * @param set  the set to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed {@link SortedSet}
     * @throws IllegalArgumentException if set or transformer is null
     * @since 4.0
     */
    public static <E> TransformedSortedSet<E> transformedSortedSet(final SortedSet<E> set,
            final Transformer<? super E, ? extends E> transformer) {

        final TransformedSortedSet<E> decorated = new TransformedSortedSet<E>(set, transformer);
        if (transformer != null && set != null && set.size() > 0) {
            @SuppressWarnings("unchecked") // set is type E
            final E[] values = (E[]) set.toArray();
            set.clear();
            for (final E value : values) {
                decorated.decorated().add(transformer.transform(value));
            }
        }
        return decorated;
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the set being decorated, they
     * are NOT transformed.
     * 
     * @param set  the set to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @throws IllegalArgumentException if set or transformer is null
     */
    protected TransformedSortedSet(final SortedSet<E> set, final Transformer<? super E, ? extends E> transformer) {
        super(set, transformer);
    }

    /**
     * Gets the decorated set.
     * 
     * @return the decorated set
     */
    protected SortedSet<E> getSortedSet() {
        return (SortedSet<E>) collection;
    }

    //-----------------------------------------------------------------------
    public E first() {
        return getSortedSet().first();
    }

    public E last() {
        return getSortedSet().last();
    }

    public Comparator<? super E> comparator() {
        return getSortedSet().comparator();
    }

    //-----------------------------------------------------------------------
    public SortedSet<E> subSet(final E fromElement, final E toElement) {
        final SortedSet<E> set = getSortedSet().subSet(fromElement, toElement);
        return new TransformedSortedSet<E>(set, transformer);
    }

    public SortedSet<E> headSet(final E toElement) {
        final SortedSet<E> set = getSortedSet().headSet(toElement);
        return new TransformedSortedSet<E>(set, transformer);
    }

    public SortedSet<E> tailSet(final E fromElement) {
        final SortedSet<E> set = getSortedSet().tailSet(fromElement);
        return new TransformedSortedSet<E>(set, transformer);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11994.java