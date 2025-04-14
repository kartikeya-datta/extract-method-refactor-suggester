error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5431.java
text:
```scala
r@@eturn PredicatedList.predicatedList(list, predicate);

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
import java.util.List;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.TruePredicate;

/**
 * Extension of {@link AbstractTestList} for exercising the 
 * {@link PredicatedList} implementation.
 *
 * @since Commons Collections 3.0
 * @version $Revision$ $Date$
 *
 * @author Phil Steitz
 */
public class TestPredicatedList<E> extends AbstractTestList<E> {

    public TestPredicatedList(String testName) {
        super(testName);
    }

 //-------------------------------------------------------------------

    protected Predicate<E> truePredicate = TruePredicate.<E>truePredicate();

    protected List<E> decorateList(List<E> list, Predicate<E> predicate) {
        return PredicatedList.decorate(list, predicate);
    }

    @Override
    public List<E> makeObject() {
        return decorateList(new ArrayList<E>(), truePredicate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] getFullElements() {
        return (E[]) new Object[] { "1", "3", "5", "7", "2", "4", "6" };
    }

//--------------------------------------------------------------------

    protected Predicate<E> testPredicate =
        new Predicate<E>() {
            public boolean evaluate(E o) {
                return o instanceof String;
            }
        };

    public List<E> makeTestList() {
        return decorateList(new ArrayList<E>(), testPredicate);
    }

    @SuppressWarnings("unchecked")
    public void testIllegalAdd() {
        List<E> list = makeTestList();
        Integer i = new Integer(3);
        try {
            list.add((E) i);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("Collection shouldn't contain illegal element",
         !list.contains(i));
    }

    @SuppressWarnings("unchecked")
    public void testIllegalAddAll() {
        List<E> list = makeTestList();
        List<E> elements = new ArrayList<E>();
        elements.add((E) "one");
        elements.add((E) "two");
        elements.add((E) new Integer(3));
        elements.add((E) "four");
        try {
            list.addAll(0, elements);
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue("List shouldn't contain illegal element",
         !list.contains("one"));
        assertTrue("List shouldn't contain illegal element",
         !list.contains("two"));
        assertTrue("List shouldn't contain illegal element",
         !list.contains(new Integer(3)));
        assertTrue("List shouldn't contain illegal element",
         !list.contains("four"));
    }

    @SuppressWarnings("unchecked")
    public void testIllegalSet() {
        List<E> list = makeTestList();
        try {
            list.set(0, (E) new Integer(3));
            fail("Integer should fail string predicate.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @SuppressWarnings("unchecked")
    public void testLegalAddAll() {
        List<E> list = makeTestList();
        list.add((E) "zero");
        List<E> elements = new ArrayList<E>();
        elements.add((E) "one");
        elements.add((E) "two");
        elements.add((E) "three");
        list.addAll(1,elements);
        assertTrue("List should contain legal element",
         list.contains("zero"));
        assertTrue("List should contain legal element",
         list.contains("one"));
        assertTrue("List should contain legal element",
         list.contains("two"));
        assertTrue("List should contain legal element",
         list.contains("three"));
    }

    @Override
    public String getCompatibilityVersion() {
        return "3.1";
    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedList.emptyCollection.version3.1.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) collection, "D:/dev/collections/data/test/PredicatedList.fullCollection.version3.1.obj");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5431.java