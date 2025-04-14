error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11449.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11449.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11449.java
text:
```scala
i@@f (!isRemoveSupported() || !isAddSupported()) {

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

import java.util.Arrays;
import java.util.LinkedList;

import junit.framework.Test;

import org.apache.commons.collections4.BulkTest;

/**
 * Test class for NodeCachingLinkedList, a performance optimised LinkedList.
 *
 * @version $Id$
 */
public class NodeCachingLinkedListTest<E> extends AbstractLinkedListTest<E> {

    public NodeCachingLinkedListTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(NodeCachingLinkedListTest.class);
    }

    //-----------------------------------------------------------------------
    @Override
    public NodeCachingLinkedList<E> makeObject() {
        return new NodeCachingLinkedList<E>();
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //-----------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void testShrinkCache() {
        if (isRemoveSupported() == false || isAddSupported() == false) {
            return;
        }
        resetEmpty();
        final NodeCachingLinkedList<E> list = getCollection();

        list.addAll(Arrays.asList((E[]) new String[] { "1", "2", "3", "4" }));
        list.removeAllNodes(); // Will dump all 4 elements into cache
        list.setMaximumCacheSize(2); // shrink cache
        list.addAll(Arrays.asList((E[]) new String[] { "1", "2", "3", "4" }));
        checkNodes();
        list.removeNode(list.getNode(0, false)); // no room in cache
        list.removeNode(list.getNode(0, false));
        list.removeNode(list.getNode(0, false));
        checkNodes();
        list.addAll(Arrays.asList((E[]) new String[] { "1", "2", "3", "4" }));
        checkNodes();
    }

    //-----------------------------------------------------------------------
    public static void compareSpeed() {
        final NodeCachingLinkedList<Object> ncll = new NodeCachingLinkedList<Object>();
        final LinkedList<Object> ll = new LinkedList<Object>();

        final Object o1 = new Object();
        final Object o2 = new Object();

        final int loopCount = 4000000;

        long startTime, endTime;

        System.out.println("Testing relative execution time of commonly-used methods...");

        startTime = System.currentTimeMillis();
        for(int x = loopCount; x > 0; x--) {
            // unrolled a few times to minimize effect of loop
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            //
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time with LinkedList: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        for(int x = loopCount; x > 0; x--) {
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            //
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time with NodeCachingLinkedList: " + (endTime - startTime) + " ms");

    }

//    public void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(),
//           "src/test/resources/data/test/NodeCachingLinkedList.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(),
//            "src/test/resources/data/test/NodeCachingLinkedList.fullCollection.version4.obj");
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeCachingLinkedList<E> getCollection() {
        return (NodeCachingLinkedList<E>) super.getCollection();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11449.java