error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/21.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/21.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/21.java
text:
```scala
a@@ssertEquals(j+1, queue.numGlobalTermDeletes());

package org.apache.lucene.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.lucene.index.DocumentsWriterDeleteQueue.DeleteSlice;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.ThreadInterruptedException;

/**
 * Unit test for {@link DocumentsWriterDeleteQueue}
 */
public class TestDocumentsWriterDeleteQueue extends LuceneTestCase {

  public void testUpdateDelteSlices() {
    DocumentsWriterDeleteQueue queue = new DocumentsWriterDeleteQueue();
    final int size = 200 + random().nextInt(500) * RANDOM_MULTIPLIER;
    Integer[] ids = new Integer[size];
    for (int i = 0; i < ids.length; i++) {
      ids[i] = random().nextInt();
    }
    DeleteSlice slice1 = queue.newSlice();
    DeleteSlice slice2 = queue.newSlice();
    BufferedDeletes bd1 = new BufferedDeletes();
    BufferedDeletes bd2 = new BufferedDeletes();
    int last1 = 0;
    int last2 = 0;
    Set<Term> uniqueValues = new HashSet<Term>();
    for (int j = 0; j < ids.length; j++) {
      Integer i = ids[j];
      // create an array here since we compare identity below against tailItem
      Term[] term = new Term[] {new Term("id", i.toString())};
      uniqueValues.add(term[0]);
      queue.addDelete(term);
      if (random().nextInt(20) == 0 || j == ids.length - 1) {
        queue.updateSlice(slice1);
        assertTrue(slice1.isTailItem(term));
        slice1.apply(bd1, j);
        assertAllBetween(last1, j, bd1, ids);
        last1 = j + 1;
      }
      if (random().nextInt(10) == 5 || j == ids.length - 1) {
        queue.updateSlice(slice2);
        assertTrue(slice2.isTailItem(term));
        slice2.apply(bd2, j);
        assertAllBetween(last2, j, bd2, ids);
        last2 = j + 1;
      }
      assertEquals(uniqueValues.size(), queue.numGlobalTermDeletes());
    }
    assertEquals(uniqueValues, bd1.terms.keySet());
    assertEquals(uniqueValues, bd2.terms.keySet());
    HashSet<Term> frozenSet = new HashSet<Term>();
    for (Term t : queue.freezeGlobalBuffer(null).termsIterable()) {
      BytesRef bytesRef = new BytesRef();
      bytesRef.copyBytes(t.bytes);
      frozenSet.add(new Term(t.field, bytesRef));
    }
    assertEquals(uniqueValues, frozenSet);
    assertEquals("num deletes must be 0 after freeze", 0, queue
        .numGlobalTermDeletes());
  }

  private void assertAllBetween(int start, int end, BufferedDeletes deletes,
      Integer[] ids) {
    for (int i = start; i <= end; i++) {
      assertEquals(Integer.valueOf(end), deletes.terms.get(new Term("id",
                                                                    ids[i].toString())));
    }
  }
  
  public void testClear() {
    DocumentsWriterDeleteQueue queue = new DocumentsWriterDeleteQueue();
    assertFalse(queue.anyChanges());
    queue.clear();
    assertFalse(queue.anyChanges());
    final int size = 200 + random().nextInt(500) * RANDOM_MULTIPLIER;
    int termsSinceFreeze = 0;
    int queriesSinceFreeze = 0;
    for (int i = 0; i < size; i++) {
      Term term = new Term("id", "" + i);
      if (random().nextInt(10) == 0) {
        queue.addDelete(new TermQuery(term));
        queriesSinceFreeze++;
      } else {
        queue.addDelete(term);
        termsSinceFreeze++;
      }
      assertTrue(queue.anyChanges());
      if (random().nextInt(10) == 0) {
        queue.clear();
        queue.tryApplyGlobalSlice();
        assertFalse(queue.anyChanges());
      }
    }
    
  }

  public void testAnyChanges() {
    DocumentsWriterDeleteQueue queue = new DocumentsWriterDeleteQueue();
    final int size = 200 + random().nextInt(500) * RANDOM_MULTIPLIER;
    int termsSinceFreeze = 0;
    int queriesSinceFreeze = 0;
    for (int i = 0; i < size; i++) {
      Term term = new Term("id", "" + i);
      if (random().nextInt(10) == 0) {
        queue.addDelete(new TermQuery(term));
        queriesSinceFreeze++;
      } else {
        queue.addDelete(term);
        termsSinceFreeze++;
      }
      assertTrue(queue.anyChanges());
      if (random().nextInt(5) == 0) {
        FrozenBufferedDeletes freezeGlobalBuffer = queue
            .freezeGlobalBuffer(null);
        assertEquals(termsSinceFreeze, freezeGlobalBuffer.termCount);
        assertEquals(queriesSinceFreeze, freezeGlobalBuffer.queries.length);
        queriesSinceFreeze = 0;
        termsSinceFreeze = 0;
        assertFalse(queue.anyChanges());
      }
    }
  }
  
  public void testPartiallyAppliedGlobalSlice() throws SecurityException,
      NoSuchFieldException, IllegalArgumentException, IllegalAccessException,
      InterruptedException {
    final DocumentsWriterDeleteQueue queue = new DocumentsWriterDeleteQueue();
    Field field = DocumentsWriterDeleteQueue.class
        .getDeclaredField("globalBufferLock");
    field.setAccessible(true);
    ReentrantLock lock = (ReentrantLock) field.get(queue);
    lock.lock();
    Thread t = new Thread() {
      public void run() {
        queue.addDelete(new Term("foo", "bar"));
      }
    };
    t.start();
    t.join();
    lock.unlock();
    assertTrue("changes in del queue but not in slice yet", queue.anyChanges());
    queue.tryApplyGlobalSlice();
    assertTrue("changes in global buffer", queue.anyChanges());
    FrozenBufferedDeletes freezeGlobalBuffer = queue.freezeGlobalBuffer(null);
    assertTrue(freezeGlobalBuffer.any());
    assertEquals(1, freezeGlobalBuffer.termCount);
    assertFalse("all changes applied", queue.anyChanges());
  }

  public void testStressDeleteQueue() throws InterruptedException {
    DocumentsWriterDeleteQueue queue = new DocumentsWriterDeleteQueue();
    Set<Term> uniqueValues = new HashSet<Term>();
    final int size = 10000 + random().nextInt(500) * RANDOM_MULTIPLIER;
    Integer[] ids = new Integer[size];
    for (int i = 0; i < ids.length; i++) {
      ids[i] = random().nextInt();
      uniqueValues.add(new Term("id", ids[i].toString()));
    }
    CountDownLatch latch = new CountDownLatch(1);
    AtomicInteger index = new AtomicInteger(0);
    final int numThreads = 2 + random().nextInt(5);
    UpdateThread[] threads = new UpdateThread[numThreads];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new UpdateThread(queue, index, ids, latch);
      threads[i].start();
    }
    latch.countDown();
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }

    for (UpdateThread updateThread : threads) {
      DeleteSlice slice = updateThread.slice;
      queue.updateSlice(slice);
      BufferedDeletes deletes = updateThread.deletes;
      slice.apply(deletes, BufferedDeletes.MAX_INT);
      assertEquals(uniqueValues, deletes.terms.keySet());
    }
    queue.tryApplyGlobalSlice();
    Set<Term> frozenSet = new HashSet<Term>();
    for (Term t : queue.freezeGlobalBuffer(null).termsIterable()) {
      BytesRef bytesRef = new BytesRef();
      bytesRef.copyBytes(t.bytes);
      frozenSet.add(new Term(t.field, bytesRef));
    }
    assertEquals("num deletes must be 0 after freeze", 0, queue
        .numGlobalTermDeletes());
    assertEquals(uniqueValues.size(), frozenSet.size());
    assertEquals(uniqueValues, frozenSet);
   
  }

  private static class UpdateThread extends Thread {
    final DocumentsWriterDeleteQueue queue;
    final AtomicInteger index;
    final Integer[] ids;
    final DeleteSlice slice;
    final BufferedDeletes deletes;
    final CountDownLatch latch;

    protected UpdateThread(DocumentsWriterDeleteQueue queue,
        AtomicInteger index, Integer[] ids, CountDownLatch latch) {
      this.queue = queue;
      this.index = index;
      this.ids = ids;
      this.slice = queue.newSlice();
      deletes = new BufferedDeletes();
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        latch.await();
      } catch (InterruptedException e) {
        throw new ThreadInterruptedException(e);
      }
      int i = 0;
      while ((i = index.getAndIncrement()) < ids.length) {
        Term term = new Term("id", ids[i].toString());
        queue.add(term, slice);
        assertTrue(slice.isTailItem(term));
        slice.apply(deletes, BufferedDeletes.MAX_INT);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/21.java