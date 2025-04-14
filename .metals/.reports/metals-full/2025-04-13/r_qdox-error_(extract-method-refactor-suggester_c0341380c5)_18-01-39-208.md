error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5424.java
text:
```scala
r@@eturn PredicatedBag.predicatedBag(bag, predicate);

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
package org.apache.commons.collections.bag;

import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;

/**
 * Extension of {@link AbstractTestBag} for exercising the {@link PredicatedBag}
 * implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Phil Steitz
 */
public class TestPredicatedBag<T> extends AbstractTestBag<T> {

    public TestPredicatedBag(String testName) {
        super(testName);
    }

    //--------------------------------------------------------------------------

    protected Predicate<T> stringPredicate() {
        return new Predicate<T>() {
            public boolean evaluate(T o) {
                return o instanceof String;
            }
        };
    }

    protected Predicate<T> truePredicate = TruePredicate.<T>truePredicate();

    protected Bag<T> decorateBag(HashBag<T> bag, Predicate<T> predicate) {
        return PredicatedBag.decorate(bag, predicate);
    }

    @Override
    public Bag<T> makeObject() {
        return decorateBag(new HashBag<T>(), truePredicate);
    }

    protected Bag<T> makeTestBag() {
        return decorateBag(new HashBag<T>(), stringPredicate());
    }

    //--------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void testlegalAddRemove() {
        Bag<T> bag = makeTestBag();
        assertEquals(0, bag.size());
        T[] els = (T[]) new Object[] { "1", "3", "5", "7", "2", "4", "1" };
        for (int i = 0; i < els.length; i++) {
            bag.add(els[i]);
            assertEquals(i + 1, bag.size());
            assertEquals(true, bag.contains(els[i]));
        }
        Set<T> set = ((PredicatedBag<T>) bag).uniqueSet();
        assertTrue("Unique set contains the first element",set.contains(els[0]));
        assertEquals(true, bag.remove(els[0]));
        set = ((PredicatedBag<T>) bag).uniqueSet();
        assertTrue("Unique set now does not contain the first element",
            !set.contains(els[0]));
    }

    @SuppressWarnings("unchecked")
    public void testIllegalAdd() {
        Bag<T> bag = makeTestBag();
        Integer i = new Integer(3);
        try {
            bag.add((T) i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element",
         !bag.contains(i));
    }

    @SuppressWarnings("unchecked")
    public void testIllegalDecorate() {
        HashBag<Object> elements = new HashBag<Object>();
        elements.add("one");
        elements.add("two");
        elements.add(new Integer(3));
        elements.add("four");
        try {
            decorateBag((HashBag<T>) elements, stringPredicate());
            fail("Bag contains an element that should fail the predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            decorateBag(new HashBag<T>(), null);
            fail("Expectiing IllegalArgumentException for null predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        Bag bag = makeBag();
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedBag.emptyCollection.version3.1.obj");
//        bag = makeBag();
//        bag.add("A");
//        bag.add("A");
//        bag.add("B");
//        bag.add("B");
//        bag.add("C");
//        writeExternalFormToDisk((java.io.Serializable) bag, "D:/dev/collections/data/test/PredicatedBag.fullCollection.version3.1.obj");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5424.java