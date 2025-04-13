error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7060.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7060.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7060.java
text:
```scala
s@@et.setMutator( new EmptySetMutator<E>(contained) );

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
package org.apache.commons.collections.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.collection.CompositeCollection;

/**
 * Extension of {@link AbstractTestSet} for exercising the
 * {@link CompositeSet} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Brian McCallister
 * @author Phil Steitz
 */

public class TestCompositeSet<E> extends AbstractTestSet<E> {
    public TestCompositeSet(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestCompositeSet.class);
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<E>();
        CompositeSet<E> set = new CompositeSet<E>(contained);
        set.setMutator( new EmptySetMutator(contained) );
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        HashSet<E> set = new HashSet<E>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildTwo() {
        HashSet<E> set = new HashSet<E>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
    }

    @SuppressWarnings("unchecked")
    public void testContains() {
        CompositeSet<E> set = new CompositeSet<E>(new Set[]{ buildOne(), buildTwo() });
        assertTrue(set.contains("1"));
    }

    @SuppressWarnings("unchecked")
    public void testRemoveUnderlying() {
        Set<E> one = buildOne();
        Set<E> two = buildTwo();
        CompositeSet<E> set = new CompositeSet<E>(new Set[] { one, two });
        one.remove("1");
        assertFalse(set.contains("1"));

        two.remove("3");
        assertFalse(set.contains("3"));
    }

    @SuppressWarnings("unchecked")
    public void testRemoveComposited() {
        Set<E> one = buildOne();
        Set<E> two = buildTwo();
        CompositeSet<E> set = new CompositeSet<E>(new Set[] { one, two });
        set.remove("1");
        assertFalse(one.contains("1"));

        set.remove("3");
        assertFalse(one.contains("3"));
    }

    @SuppressWarnings("unchecked")
    public void testFailedCollisionResolution() {
        Set<E> one = buildOne();
        Set<E> two = buildTwo();
        CompositeSet<E> set = new CompositeSet<E>(new Set[] { one, two });
        set.setMutator(new CompositeSet.SetMutator<E>() {
            public void resolveCollision(CompositeSet<E> comp, Set<E> existing,
                Set<E> added, Collection<E> intersects) {
                //noop
            }

            public boolean add(CompositeCollection<E> composite,
                    List<Collection<E>> collections, E obj) {
                throw new UnsupportedOperationException();
            }

            public boolean addAll(CompositeCollection<E> composite,
                    List<Collection<E>> collections, Collection<? extends E> coll) {
                throw new UnsupportedOperationException();
            }

            public boolean remove(CompositeCollection<E> composite,
                    List<Collection<E>> collections, Object obj) {
                throw new UnsupportedOperationException();
            }
        });

        HashSet<E> three = new HashSet<E>();
        three.add((E) "1");
        try {
            set.addComposited(three);
            fail("IllegalArgumentException should have been thrown");
        }
        catch (IllegalArgumentException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    public void testAddComposited() {
        Set<E> one = buildOne();
        Set<E> two = buildTwo();
        CompositeSet<E> set = new CompositeSet<E>();
        set.addComposited(one, two);
        CompositeSet<E> set2 = new CompositeSet<E>(buildOne());
        set2.addComposited(buildTwo());
        assertTrue(set.equals(set2));
        HashSet<E> set3 = new HashSet<E>();
        set3.add((E) "1");
        set3.add((E) "2");
        set3.add((E) "3");
        HashSet<E> set4 = new HashSet<E>();
        set4.add((E) "4");
        CompositeSet<E> set5 = new CompositeSet<E>(set3);
        set5.addComposited(set4);
        assertTrue(set.equals(set5));
        try {
            set.addComposited(set3);
            fail("Expecting UnsupportedOperationException.");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.3";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "/tmp/CompositeSet.emptyCollection.version3.3.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "/tmp/CompositeSet.fullCollection.version3.3.obj");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7060.java