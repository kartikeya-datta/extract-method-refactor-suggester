error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1726.java
text:
```scala
S@@ystem.out.println(Thread.currentThread().getName() + ": EXC: ");

package org.apache.lucene.index;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Random;
import java.io.IOException;

import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class TestIndexWriterExceptions extends LuceneTestCase {

  private class IndexerThread extends Thread {

    IndexWriter writer;

    final Random r = new java.util.Random(47);
    Throwable failure;

    public IndexerThread(int i, IndexWriter writer) {
      setName("Indexer " + i);
      this.writer = writer;
    }

    @Override
    public void run() {

      final Document doc = new Document();

      doc.add(newField("content1", "aaa bbb ccc ddd", Field.Store.YES, Field.Index.ANALYZED));
      doc.add(newField("content6", "aaa bbb ccc ddd", Field.Store.NO, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
      doc.add(newField("content2", "aaa bbb ccc ddd", Field.Store.YES, Field.Index.NOT_ANALYZED));
      doc.add(newField("content3", "aaa bbb ccc ddd", Field.Store.YES, Field.Index.NO));

      doc.add(newField("content4", "aaa bbb ccc ddd", Field.Store.NO, Field.Index.ANALYZED));
      doc.add(newField("content5", "aaa bbb ccc ddd", Field.Store.NO, Field.Index.NOT_ANALYZED));

      doc.add(newField("content7", "aaa bbb ccc ddd", Field.Store.NO, Field.Index.NOT_ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));

      final Field idField = newField("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED);
      doc.add(idField);

      final long stopTime = System.currentTimeMillis() + 500;

      do {
        doFail.set(this);
        final String id = ""+r.nextInt(50);
        idField.setValue(id);
        Term idTerm = new Term("id", id);
        try {
          writer.updateDocument(idTerm, doc);
        } catch (RuntimeException re) {
          if (VERBOSE) {
            System.out.println("EXC: ");
            re.printStackTrace(System.out);
          }
          try {
            _TestUtil.checkIndex(writer.getDirectory());
          } catch (IOException ioe) {
            System.out.println(Thread.currentThread().getName() + ": unexpected exception1");
            ioe.printStackTrace(System.out);
            failure = ioe;
            break;
          }
        } catch (Throwable t) {
          System.out.println(Thread.currentThread().getName() + ": unexpected exception2");
          t.printStackTrace(System.out);
          failure = t;
          break;
        }

        doFail.set(null);

        // After a possible exception (above) I should be able
        // to add a new document without hitting an
        // exception:
        try {
          writer.updateDocument(idTerm, doc);
        } catch (Throwable t) {
          System.out.println(Thread.currentThread().getName() + ": unexpected exception3");
          t.printStackTrace(System.out);
          failure = t;
          break;
        }
      } while(System.currentTimeMillis() < stopTime);
    }
  }

  ThreadLocal<Thread> doFail = new ThreadLocal<Thread>();

  private class MockIndexWriter extends IndexWriter {
    Random r = new java.util.Random(17);

    public MockIndexWriter(Directory dir, IndexWriterConfig conf) throws IOException {
      super(dir, conf);
    }

    @Override
    boolean testPoint(String name) {
      if (doFail.get() != null && !name.equals("startDoFlush") && r.nextInt(20) == 17) {
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": NOW FAIL: " + name);
          //new Throwable().printStackTrace(System.out);
        }
        throw new RuntimeException(Thread.currentThread().getName() + ": intentionally failing at " + name);
      }
      return true;
    }
  }

  public void testRandomExceptions() throws Throwable {
    MockDirectoryWrapper dir = newDirectory();

    MockIndexWriter writer  = new MockIndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer())
        .setRAMBufferSizeMB(0.1).setMergeScheduler(new ConcurrentMergeScheduler()));
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).setSuppressExceptions();
    //writer.setMaxBufferedDocs(10);
    writer.commit();

    if (VERBOSE)
      writer.setInfoStream(System.out);

    IndexerThread thread = new IndexerThread(0, writer);
    thread.run();
    if (thread.failure != null) {
      thread.failure.printStackTrace(System.out);
      fail("thread " + thread.getName() + ": hit unexpected failure");
    }

    writer.commit();

    try {
      writer.close();
    } catch (Throwable t) {
      System.out.println("exception during close:");
      t.printStackTrace(System.out);
      writer.rollback();
    }

    // Confirm that when doc hits exception partway through tokenization, it's deleted:
    IndexReader r2 = IndexReader.open(dir, true);
    final int count = r2.docFreq(new Term("content4", "aaa"));
    final int count2 = r2.docFreq(new Term("content4", "ddd"));
    assertEquals(count, count2);
    r2.close();

    _TestUtil.checkIndex(dir);
    dir.close();
  }

  public void testRandomExceptionsThreads() throws Throwable {
    MockDirectoryWrapper dir = newDirectory();
    MockIndexWriter writer  = new MockIndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer())
        .setRAMBufferSizeMB(0.2).setMergeScheduler(new ConcurrentMergeScheduler()));
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).setSuppressExceptions();
    //writer.setMaxBufferedDocs(10);
    writer.commit();

    if (VERBOSE)
      writer.setInfoStream(System.out);

    final int NUM_THREADS = 4;

    final IndexerThread[] threads = new IndexerThread[NUM_THREADS];
    for(int i=0;i<NUM_THREADS;i++) {
      threads[i] = new IndexerThread(i, writer);
      threads[i].start();
    }

    for(int i=0;i<NUM_THREADS;i++)
      threads[i].join();

    for(int i=0;i<NUM_THREADS;i++)
      if (threads[i].failure != null)
        fail("thread " + threads[i].getName() + ": hit unexpected failure");

    writer.commit();

    try {
      writer.close();
    } catch (Throwable t) {
      System.out.println("exception during close:");
      t.printStackTrace(System.out);
      writer.rollback();
    }

    // Confirm that when doc hits exception partway through tokenization, it's deleted:
    IndexReader r2 = IndexReader.open(dir, true);
    final int count = r2.docFreq(new Term("content4", "aaa"));
    final int count2 = r2.docFreq(new Term("content4", "ddd"));
    assertEquals(count, count2);
    r2.close();

    _TestUtil.checkIndex(dir);
    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1726.java