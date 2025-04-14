error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1629.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1629.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1629.java
text:
```scala
private final F@@actory<? extends E> factory;

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
package org.apache.commons.collections4.list;

import java.util.List;

import org.apache.commons.collections4.Factory;

/**
 * Decorates another <code>List</code> to create objects in the list on demand.
 * <p>
 * When the {@link #get(int)} method is called with an index greater than
 * the size of the list, the list will automatically grow in size and return
 * a new object from the specified factory. The gaps will be filled by null.
 * If a get method call encounters a null, it will be replaced with a new
 * object from the factory. Thus this list is unsuitable for storing null
 * objects.
 * <p>
 * For instance:
 *
 * <pre>
 * Factory&lt;Date&gt; factory = new Factory&lt;Date&gt;() {
 *     public Date create() {
 *         return new Date();
 *     }
 * }
 * List&lt;Date&gt; lazy = LazyList.decorate(new ArrayList&lt;Date&gt;(), factory);
 * Date date = lazy.get(3);
 * </pre>
 *
 * After the above code is executed, <code>date</code> will contain
 * a new <code>Date</code> instance.  Furthermore, that <code>Date</code>
 * instance is the fourth element in the list.  The first, second, 
 * and third element are all set to <code>null</code>.
 * <p>
 * This class differs from {@link GrowthList} because here growth occurs on
 * get, where <code>GrowthList</code> grows on set and add. However, they
 * could easily be used together by decorating twice.
 * <p>
 * This class is Serializable from Commons Collections 3.1.
 *
 * @see GrowthList
 * @since 3.0
 * @version $Id$
 */
public class LazyList<E> extends AbstractSerializableListDecorator<E> {

    /** Serialization version */
    private static final long serialVersionUID = -1708388017160694542L;

    /** The factory to use to lazily instantiate the objects */
    protected final Factory<? extends E> factory;

    /**
     * Factory method to create a lazily instantiating list.
     * 
     * @param <E> the type of the elements in the list
     * @param list  the list to decorate, must not be null
     * @param factory  the factory to use for creation, must not be null
     * @return a new lazy list
     * @throws IllegalArgumentException if list or factory is null
     */
    public static <E> LazyList<E> lazyList(final List<E> list, final Factory<? extends E> factory) {
        return new LazyList<E>(list, factory);
    }
    
    //-----------------------------------------------------------------------
    /**
     * Constructor that wraps (not copies).
     * 
     * @param list  the list to decorate, must not be null
     * @param factory  the factory to use for creation, must not be null
     * @throws IllegalArgumentException if list or factory is null
     */
    protected LazyList(final List<E> list, final Factory<? extends E> factory) {
        super(list);
        if (factory == null) {
            throw new IllegalArgumentException("Factory must not be null");
        }
        this.factory = factory;
    }

    //-----------------------------------------------------------------------
    /**
     * Decorate the get method to perform the lazy behaviour.
     * <p>
     * If the requested index is greater than the current size, the list will 
     * grow to the new size and a new object will be returned from the factory.
     * Indexes in-between the old size and the requested size are left with a 
     * placeholder that is replaced with a factory object when requested.
     * 
     * @param index  the index to retrieve
     * @return the element at the given index
     */
    @Override
    public E get(final int index) {
        final int size = decorated().size();
        if (index < size) {
            // within bounds, get the object
            E object = decorated().get(index);
            if (object == null) {
                // item is a place holder, create new one, set and return
                object = factory.create();
                decorated().set(index, object);
                return object;
            }
            // good and ready to go
            return object;
        }
        // we have to grow the list
        for (int i = size; i < index; i++) {
            decorated().add(null);
        }
        // create our last object, set and return
        final E object = factory.create();
        decorated().add(object);
        return object;
    }

    @Override
    public List<E> subList(final int fromIndex, final int toIndex) {
        final List<E> sub = decorated().subList(fromIndex, toIndex);
        return new LazyList<E>(sub, factory);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1629.java