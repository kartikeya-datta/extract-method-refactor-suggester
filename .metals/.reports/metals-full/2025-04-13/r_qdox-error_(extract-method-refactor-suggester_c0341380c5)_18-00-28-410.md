error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5413.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5413.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5413.java
text:
```scala
public static <@@T> List<T> predicatedList(List<T> list, Predicate<? super T> predicate) {

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

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.collection.PredicatedCollection;
import org.apache.commons.collections.iterators.AbstractListIteratorDecorator;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Decorates another <code>List</code> to validate that all additions
 * match a specified predicate.
 * <p>
 * This list exists to provide validation for the decorated list.
 * It is normally created to decorate an empty list.
 * If an object cannot be added to the list, an IllegalArgumentException is thrown.
 * <p>
 * One usage would be to ensure that no null entries are added to the list.
 * <pre>List list = PredicatedList.decorate(new ArrayList(), NotNullPredicate.INSTANCE);</pre>
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 * @author Paul Jack
 */
public class PredicatedList<E> extends PredicatedCollection<E> implements List<E> {

    /** Serialization version */
    private static final long serialVersionUID = -5722039223898659102L;

    /**
     * Factory method to create a predicated (validating) list.
     * <p>
     * If there are any elements already in the list being decorated, they
     * are validated.
     * 
     * @param list  the list to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @return the decorated list
     * @throws IllegalArgumentException if list or predicate is null
     * @throws IllegalArgumentException if the list contains invalid elements
     */
    public static <T> List<T> decorate(List<T> list, Predicate<? super T> predicate) {
        return new PredicatedList<T>(list, predicate);
    }

    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * <p>
     * If there are any elements already in the list being decorated, they
     * are validated.
     * 
     * @param list  the list to decorate, must not be null
     * @param predicate  the predicate to use for validation, must not be null
     * @throws IllegalArgumentException if list or predicate is null
     * @throws IllegalArgumentException if the list contains invalid elements
     */
    protected PredicatedList(List<E> list, Predicate<? super E> predicate) {
        super(list, predicate);
    }

    /**
     * Gets the list being decorated.
     * 
     * @return the decorated list
     */
    @Override
    protected List<E> decorated() {
        return (List<E>) super.decorated();
    }

    //-----------------------------------------------------------------------
    public E get(int index) {
        return decorated().get(index);
    }

    public int indexOf(Object object) {
        return decorated().indexOf(object);
    }

    public int lastIndexOf(Object object) {
        return decorated().lastIndexOf(object);
    }

    public E remove(int index) {
        return decorated().remove(index);
    }

    //-----------------------------------------------------------------------
    public void add(int index, E object) {
        validate(object);
        decorated().add(index, object);
    }

    public boolean addAll(int index, Collection<? extends E> coll) {
        for (E aColl : coll) {
            validate(aColl);
        }
        return decorated().addAll(index, coll);
    }

    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ListIterator<E> listIterator(int i) {
        return new PredicatedListIterator(decorated().listIterator(i));
    }

    public E set(int index, E object) {
        validate(object);
        return decorated().set(index, object);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        List<E> sub = decorated().subList(fromIndex, toIndex);
        return new PredicatedList<E>(sub, predicate);
    }

    /**
     * Inner class Iterator for the PredicatedList
     */
    protected class PredicatedListIterator extends AbstractListIteratorDecorator<E> {
        
        protected PredicatedListIterator(ListIterator<E> iterator) {
            super(iterator);
        }
        
        @Override
        public void add(E object) {
            validate(object);
            iterator.add(object);
        }
        
        @Override
        public void set(E object) {
            validate(object);
            iterator.set(object);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5413.java