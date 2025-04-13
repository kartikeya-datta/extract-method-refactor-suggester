error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5372.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5372.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5372.java
text:
```scala
public static <@@T> Collection<T> predicatedCollection(Collection<T> coll, Predicate<? super T> predicate) {

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
package org.apache.commons.collections.collection;

import java.util.Collection;

import org.apache.commons.collections.Predicate;

/**
 * Decorates another <code>Collection</code> to validate that additions
 * match a specified predicate.
 * <p>
 * This collection exists to provide validation for the decorated collection.
 * It is normally created to decorate an empty collection.
 * If an object cannot be added to the collection, an IllegalArgumentException is thrown.
 * <p>
 * One usage would be to ensure that no null entries are added to the collection.
 * <pre>Collection coll = PredicatedCollection.decorate(new ArrayList(), NotNullPredicate.INSTANCE);</pre>
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @param <E> the type of the elements in the collection
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 * @author Paul Jack
 */
public class PredicatedCollection<E> extends AbstractCollectionDecorator<E> {

    /** Serialization version */
    private static final long serialVersionUID = -5259182142076705162L;

    /** The predicate to use */
    protected final Predicate<? super E> predicate;

    /**
     * Factory method to create a predicated (validating) collection.
     * <p>
     * If there are any elements already in the collection being decorated, they
     * are validated.
     * 
     * @param <T> the type of the elements in the collection
     * @param coll  the collection to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @return a new predicated collection
     * @throws IllegalArgumentException if collection or predicate is null
     * @throws IllegalArgumentException if the collection contains invalid elements
     */
    public static <T> Collection<T> decorate(Collection<T> coll, Predicate<? super T> predicate) {
        return new PredicatedCollection<T>(coll, predicate);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the collection being decorated, they
     * are validated.
     * 
     * @param coll  the collection to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @throws IllegalArgumentException if collection or predicate is null
     * @throws IllegalArgumentException if the collection contains invalid elements
     */
    protected PredicatedCollection(Collection<E> coll, Predicate<? super E> predicate) {
        super(coll);
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate must not be null");
        }
        this.predicate = predicate;
        for (E item : coll) {
            validate(item);
        }
    }

    /**
     * Validates the object being added to ensure it matches the predicate.
     * <p>
     * The predicate itself should not throw an exception, but return false to
     * indicate that the object cannot be added.
     * 
     * @param object  the object being added
     * @throws IllegalArgumentException if the add is invalid
     */
    protected void validate(E object) {
        if (predicate.evaluate(object) == false) {
            throw new IllegalArgumentException("Cannot add Object '" + object + "' - Predicate '" + predicate + "' rejected it");
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Override to validate the object being added to ensure it matches
     * the predicate.
     * 
     * @param object  the object being added
     * @return the result of adding to the underlying collection
     * @throws IllegalArgumentException if the add is invalid
     */
    @Override
    public boolean add(E object) {
        validate(object);
        return decorated().add(object);
    }

    /**
     * Override to validate the objects being added to ensure they match
     * the predicate. If any one fails, no update is made to the underlying
     * collection.
     * 
     * @param coll  the collection being added
     * @return the result of adding to the underlying collection
     * @throws IllegalArgumentException if the add is invalid
     */
    @Override
    public boolean addAll(Collection<? extends E> coll) {
        for (E item : coll) {
            validate(item);
        }
        return decorated().addAll(coll);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5372.java