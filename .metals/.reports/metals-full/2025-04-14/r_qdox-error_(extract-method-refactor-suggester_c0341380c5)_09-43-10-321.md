error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11990.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11990.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11990.java
text:
```scala
final E@@[] values = (E[]) collection.toArray(); // NOPMD - false positive for generics

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Transformer;

/**
 * Decorates another {@link Collection} to transform objects that are added.
 * <p>
 * The add methods are affected by this class.
 * Thus objects must be removed or searched for using their transformed form.
 * For example, if the transformation converts Strings to Integers, you must
 * use the Integer form to remove objects.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @param <E> the type of the elements in the collection
 * @since 3.0
 * @version $Id$
 */
public class TransformedCollection<E> extends AbstractCollectionDecorator<E> {

    /** Serialization version */
    private static final long serialVersionUID = 8692300188161871514L;

    /** The transformer to use */
    protected final Transformer<? super E, ? extends E> transformer;

    /**
     * Factory method to create a transforming collection.
     * <p>
     * If there are any elements already in the collection being decorated, they
     * are NOT transformed.
     * Contrast this with {@link #transformedCollection(Collection, Transformer)}.
     * 
     * @param <E> the type of the elements in the collection
     * @param coll  the collection to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed collection
     * @throws IllegalArgumentException if collection or transformer is null
     */
    public static <E> TransformedCollection<E> transformingCollection(final Collection<E> coll,
            final Transformer<? super E, ? extends E> transformer) {
        return new TransformedCollection<E>(coll, transformer);
    }

    /**
     * Factory method to create a transforming collection that will transform
     * existing contents of the specified collection.
     * <p>
     * If there are any elements already in the collection being decorated, they
     * will be transformed by this method.
     * Contrast this with {@link #transformingCollection(Collection, Transformer)}.
     * 
     * @param <E> the type of the elements in the collection
     * @param collection  the collection to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @return a new transformed Collection
     * @throws IllegalArgumentException if collection or transformer is null
     * @since 4.0
     */
    public static <E> TransformedCollection<E> transformedCollection(final Collection<E> collection,
            final Transformer<? super E, ? extends E> transformer) {

        final TransformedCollection<E> decorated = new TransformedCollection<E>(collection, transformer);
        // null collection & transformer are disallowed by the constructor call above 
        if (collection.size() > 0) {
            @SuppressWarnings("unchecked") // collection is of type E
            final E[] values = (E[]) collection.toArray();
            collection.clear();
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
     * If there are any elements already in the collection being decorated, they
     * are NOT transformed.
     * 
     * @param coll  the collection to decorate, must not be null
     * @param transformer  the transformer to use for conversion, must not be null
     * @throws IllegalArgumentException if collection or transformer is null
     */
    protected TransformedCollection(final Collection<E> coll, final Transformer<? super E, ? extends E> transformer) {
        super(coll);
        if (transformer == null) {
            throw new IllegalArgumentException("Transformer must not be null");
        }
        this.transformer = transformer;
    }

    /**
     * Transforms an object.
     * <p>
     * The transformer itself may throw an exception if necessary.
     * 
     * @param object  the object to transform
     * @return a transformed object
     */
    protected E transform(final E object) {
        return transformer.transform(object);
    }

    /**
     * Transforms a collection.
     * <p>
     * The transformer itself may throw an exception if necessary.
     * 
     * @param coll  the collection to transform
     * @return a transformed object
     */
    protected Collection<E> transform(final Collection<? extends E> coll) {
        final List<E> list = new ArrayList<E>(coll.size());
        for (final E item : coll) {
            list.add(transform(item));
        }
        return list;
    }

    //-----------------------------------------------------------------------
    @Override
    public boolean add(final E object) {
        return decorated().add(transform(object));
    }

    @Override
    public boolean addAll(final Collection<? extends E> coll) {
        return decorated().addAll(transform(coll));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11990.java