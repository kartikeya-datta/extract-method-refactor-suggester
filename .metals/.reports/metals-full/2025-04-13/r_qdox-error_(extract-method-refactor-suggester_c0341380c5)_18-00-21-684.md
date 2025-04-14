error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1860.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1860.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 873
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1860.java
text:
```scala
public class TransformedQueueTest<E> extends AbstractQueueTest<E> {

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
p@@ackage org.apache.commons.collections4.queue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;

/**
 * Extension of {@link AbstractCollectionTest} for exercising the
 * {@link TransformedQueue} implementation.
 *
 * @since 4.0
 * @version $Id$
 */
public class TransformedQueueTest<E> extends AbstractCollectionTest<E> {
    
    public TransformedQueueTest(final String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    @Override
    public Queue<E> makeConfirmedCollection() {
        return new LinkedList<E>();
    }

    @Override
    public Queue<E> makeConfirmedFullCollection() {
        final Queue<E> list = new LinkedList<E>();
        list.addAll(Arrays.asList(getFullElements()));
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Queue<E> makeObject() {
        return TransformedQueue.transformingQueue(new LinkedList<E>(),
                (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Queue<E> makeFullCollection() {
        final Queue<E> list = new LinkedList<E>();
        list.addAll(Arrays.asList(getFullElements()));
        return TransformedQueue.transformingQueue(list, (Transformer<E, E>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    //-----------------------------------------------------------------------
    public void testTransformedQueue() {
        final Queue<Object> queue = TransformedQueue.transformingQueue(new LinkedList<Object>(),
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(0, queue.size());
        final Object[] els = new Object[] { "1", "3", "5", "7", "2", "4", "6" };
        for (int i = 0; i < els.length; i++) {
            queue.add(els[i]);
            assertEquals(i + 1, queue.size());
            assertEquals(true, queue.contains(new Integer((String) els[i])));
            assertEquals(false, queue.contains(els[i]));
        }
        
        assertEquals(false, queue.remove(els[0]));
        assertEquals(true, queue.remove(new Integer((String) els[0])));
        
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testTransformedQueue_decorateTransform() {
        final Queue originalQueue = new LinkedList();
        final Object[] els = new Object[] {"1", "3", "5", "7", "2", "4", "6"};
        for (final Object el : els) {
            originalQueue.add(el);
        }
        final Queue<?> queue = TransformedQueue.transformedQueue(originalQueue,
                TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        assertEquals(els.length, queue.size());
        for (final Object el : els) {
            assertEquals(true, queue.contains(new Integer((String) el)));
            assertEquals(false, queue.contains(el));
        }
        
        assertEquals(false, queue.remove(els[0]));
        assertEquals(true, queue.remove(new Integer((String) els[0])));
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }
    
//  public void testCreate() throws Exception {
//      resetEmpty();
//      writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/TransformedQueue.emptyCollection.version4.obj");
//      resetFull();
//      writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/TransformedQueue.fullCollection.version4.obj");
//  }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1860.java