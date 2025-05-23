error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8593.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8593.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8593.java
text:
```scala
public b@@oolean add(final CompositeCollection<E> composite, final List<Collection<E>> collections, final E obj) {

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Extension of {@link AbstractCollectionTest} for exercising the
 * {@link CompositeCollection} implementation.
 *
 * @since 3.0
 * @version $Id$
 */
public class CompositeCollectionTest<E> extends AbstractCollectionTest<E> {

    public CompositeCollectionTest(final String name) {
        super(name);
    }

 //-----------------------------------------------------------------------------
    /**
     * Run stock collection tests without Mutator, so turn off add, remove
     */
    @Override
    public boolean isAddSupported() {
        return false;
    }

    @Override
    public boolean isRemoveSupported() {
        return false;
    }

    /**
     * Empty collection is empty composite
     */
    @Override
    public Collection<E> makeObject() {
        return new CompositeCollection<E>();
    }

    @Override
    public Collection<E> makeConfirmedCollection() {
        return new HashSet<E>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] getFullElements() {
        return (E[]) new Object[] { "1", "2", "3", "4" };
    }

    /**
     * Full collection consists of 4 collections, each with one element
     */
    @Override
    public Collection<E> makeFullCollection() {
        final CompositeCollection<E> compositeCollection = new CompositeCollection<E>();
        final E[] elements = getFullElements();
        for (final E element : elements) {
            final Collection<E> summand = new HashSet<E>();
            summand.add(element);
            compositeCollection.addComposited(summand);
        }
        return compositeCollection;
    }

    /**
     * Full collection should look like a collection with 4 elements
     */
    @Override
    public Collection<E> makeConfirmedFullCollection() {
        final Collection<E> collection = new HashSet<E>();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Override testUnsupportedRemove, since the default impl expects removeAll,
     * retainAll and iterator().remove to throw
     */
    @Override
    public void testUnsupportedRemove() {
        resetFull();
        try {
            getCollection().remove(null);
            fail("remove should raise UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            // expected
        }
        verify();
    }

    //--------------------------------------------------------------------------

    protected CompositeCollection<E> c;
    protected Collection<E> one;
    protected Collection<E> two;

    protected void setUpTest() {
        c = new CompositeCollection<E>();
        one = new HashSet<E>();
        two = new HashSet<E>();
    }

    @SuppressWarnings("serial")
    protected void setUpMutatorTest() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator<E>() {
            
            public boolean add(CompositeCollection<E> composite, List<Collection<E>> collections, E obj) {
                for (final Collection<E> coll : collections) {
                    coll.add(obj);
                }
                return true;
            }

            public boolean addAll(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Collection<? extends E> coll) {
                for (final Collection<E> collection : collections) {
                    collection.addAll(coll);
                }
                return true;
            }

            public boolean remove(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Object obj) {
                for (final Collection<E> collection : collections) {
                    collection.remove(obj);
                }
                return true;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void testSize() {
        setUpTest();
        final HashSet<E> set = new HashSet<E>();
        set.add((E) "a");
        set.add((E) "b");
        c.addComposited(set);
        assertEquals(set.size(), c.size());
    }

    @SuppressWarnings("unchecked")
    public void testMultipleCollectionsSize() {
        setUpTest();
        final HashSet<E> set = new HashSet<E>();
        set.add((E) "a");
        set.add((E) "b");
        c.addComposited(set);
        final HashSet<E> other = new HashSet<E>();
        other.add((E) "c");
        c.addComposited(other);
        assertEquals(set.size() + other.size(), c.size());
    }

    @SuppressWarnings("unchecked")
    public void testIsEmpty() {
        setUpTest();
        assertTrue(c.isEmpty());
        final HashSet<E> empty = new HashSet<E>();
        c.addComposited(empty);
        assertTrue(c.isEmpty());
        empty.add((E) "a");
        assertTrue(!c.isEmpty());
    }


    @SuppressWarnings("unchecked")
    public void testIterator() {
        setUpTest();
        one.add((E) "1");
        two.add((E) "2");
        c.addComposited(one);
        c.addComposited(two);
        final Iterator<E> i = c.iterator();
        E next = i.next();
        assertTrue(c.contains(next));
        assertTrue(one.contains(next));
        next = i.next();
        i.remove();
        assertTrue(!c.contains(next));
        assertTrue(!two.contains(next));
    }

    @SuppressWarnings("unchecked")
    public void testClear() {
        setUpTest();
        one.add((E) "1");
        two.add((E) "2");
        c.addComposited(one, two);
        c.clear();
        assertTrue(one.isEmpty());
        assertTrue(two.isEmpty());
        assertTrue(c.isEmpty());
    }

    @SuppressWarnings("unchecked")
    public void testContainsAll() {
        setUpTest();
        one.add((E) "1");
        two.add((E) "1");
        c.addComposited(one);
        assertTrue(c.containsAll(two));
    }

    @SuppressWarnings("unchecked")
    public void testRetainAll() {
        setUpTest();
        one.add((E) "1");
        one.add((E) "2");
        two.add((E) "1");
        c.addComposited(one);
        c.retainAll(two);
        assertTrue(!c.contains("2"));
        assertTrue(!one.contains("2"));
        assertTrue(c.contains("1"));
        assertTrue(one.contains("1"));
    }

    @SuppressWarnings({ "unchecked", "serial" })
    public void testAddAllMutator() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator<E>() {
            public boolean add(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final E obj) {
                for (final Collection<E> collection : collections) {
                    collection.add(obj);
                }
                return true;
            }

            public boolean addAll(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Collection<? extends E> coll) {
                for (final Collection<E> collection : collections) {
                    collection.addAll(coll);
                }
                return true;
            }

            public boolean remove(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Object obj) {
                return false;
            }
        });

        c.addComposited(one);
        two.add((E) "foo");
        c.addAll(two);
        assertTrue(c.contains("foo"));
        assertTrue(one.contains("foo"));
    }

    @SuppressWarnings({ "unchecked", "serial" })
    public void testAddMutator() {
        setUpTest();
        c.setMutator(new CompositeCollection.CollectionMutator<E>() {
            public boolean add(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final E obj) {
                for (final Collection<E> collection : collections) {
                    collection.add(obj);
                }
                return true;
            }

            public boolean addAll(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Collection<? extends E> coll) {
                for (final Collection<E> collection : collections) {
                    collection.addAll(coll);
                }
                return true;
            }

            public boolean remove(final CompositeCollection<E> composite,
                    final List<Collection<E>> collections, final Object obj) {
                return false;
            }
        });

        c.addComposited(one);
        c.add((E) "foo");
        assertTrue(c.contains("foo"));
        assertTrue(one.contains("foo"));
    }

    @SuppressWarnings("unchecked")
    public void testToCollection() {
        setUpTest();
        one.add((E) "1");
        two.add((E) "2");
        c.addComposited(one, two);
        final Collection<E> foo = c.toCollection();
        assertTrue(foo.containsAll(c));
        assertEquals(c.size(), foo.size());
        one.add((E) "3");
        assertTrue(!foo.containsAll(c));
    }

    @SuppressWarnings("unchecked")
    public void testAddAllToCollection() {
        setUpTest();
        one.add((E) "1");
        two.add((E) "2");
        c.addComposited(one, two);
        final Collection<E> toCollection = new HashSet<E>();
        toCollection.addAll(c);
        assertTrue(toCollection.containsAll(c));
        assertEquals(c.size(), toCollection.size());
    }

    @SuppressWarnings("unchecked")
    public void testRemove() {
        setUpMutatorTest();
        one.add((E) "1");
        two.add((E) "2");
        two.add((E) "1");
        c.addComposited(one, two);
        c.remove("1");
        assertTrue(!c.contains("1"));
        assertTrue(!one.contains("1"));
        assertTrue(!two.contains("1"));
    }

    @SuppressWarnings("unchecked")
    public void testRemoveAll() {
        setUpMutatorTest();
        one.add((E) "1");
        two.add((E) "2");
        two.add((E) "1");
        // need separate list to remove, as otherwise one clears itself
        final Collection<E> removing = new ArrayList<E>(one);
        c.addComposited(one, two);
        c.removeAll(removing);
        assertTrue(!c.contains("1"));
        assertTrue(!one.contains("1"));
        assertTrue(!two.contains("1"));
    }

    @SuppressWarnings("unchecked")
    public void testRemoveComposited() {
        setUpMutatorTest();
        one.add((E) "1");
        two.add((E) "2");
        two.add((E) "1");
        c.addComposited(one, two);
        c.removeComposited(one);
        assertTrue(c.contains("1"));
        assertEquals(2, c.size());
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.3";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "/tmp/CompositeCollection.emptyCollection.version3.3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "/tmp/CompositeCollection.fullCollection.version3.3.obj");
//    }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8593.java