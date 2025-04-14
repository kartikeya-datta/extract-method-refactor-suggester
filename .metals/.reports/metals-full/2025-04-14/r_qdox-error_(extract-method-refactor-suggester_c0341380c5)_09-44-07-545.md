error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3136.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3136.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3136.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.index;

/*
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
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

public class TestIndexWriterNRTIsCurrent extends LuceneTestCase {

  public static class ReaderHolder {
    volatile DirectoryReader reader;
    volatile boolean stop = false;
  }

  public void testIsCurrentWithThreads() throws
      IOException, InterruptedException {
    Directory dir = newDirectory();
    IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random()));
    IndexWriter writer = new IndexWriter(dir, conf);
    ReaderHolder holder = new ReaderHolder();
    ReaderThread[] threads = new ReaderThread[atLeast(3)];
    final CountDownLatch latch = new CountDownLatch(1);
    WriterThread writerThread = new WriterThread(holder, writer,
        atLeast(500), random(), latch);
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new ReaderThread(holder, latch);
      threads[i].start();
    }
    writerThread.start();

    writerThread.join();
    boolean failed = writerThread.failed != null;
    if (failed)
      writerThread.failed.printStackTrace();
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
      if (threads[i].failed != null) {
        threads[i].failed.printStackTrace();
        failed = true;
      }
    }
    assertFalse(failed);
    writer.close();
    dir.close();

  }

  public static class WriterThread extends Thread {
    private final ReaderHolder holder;
    private final IndexWriter writer;
    private final int numOps;
    private boolean countdown = true;
    private final CountDownLatch latch;
    Throwable failed;

    WriterThread(ReaderHolder holder, IndexWriter writer, int numOps,
        Random random, CountDownLatch latch) {
      super();
      this.holder = holder;
      this.writer = writer;
      this.numOps = numOps;
      this.latch = latch;
    }

    @Override
    public void run() {
      DirectoryReader currentReader = null;
      Random random = LuceneTestCase.random();
      try {
        Document doc = new Document();
        doc.add(new TextField("id", "1", Field.Store.NO));
        writer.addDocument(doc);
        holder.reader = currentReader = writer.getReader(true);
        Term term = new Term("id");
        for (int i = 0; i < numOps && !holder.stop; i++) {
          float nextOp = random.nextFloat();
          if (nextOp < 0.3) {
            term.set("id", new BytesRef("1"));
            writer.updateDocument(term, doc);
          } else if (nextOp < 0.5) {
            writer.addDocument(doc);
          } else {
            term.set("id", new BytesRef("1"));
            writer.deleteDocuments(term);
          }
          if (holder.reader != currentReader) {
            holder.reader = currentReader;
            if (countdown) {
              countdown = false;
              latch.countDown();
            }
          }
          if (random.nextBoolean()) {
            writer.commit();
            final DirectoryReader newReader = DirectoryReader
                .openIfChanged(currentReader);
            if (newReader != null) { 
              currentReader.decRef();
              currentReader = newReader;
            }
            if (currentReader.numDocs() == 0) {
              writer.addDocument(doc);
            }
          }
        }
      } catch (Throwable e) {
        failed = e;
      } finally {
        holder.reader = null;
        if (countdown) {
          latch.countDown();
        }
        if (currentReader != null) {
          try {
            currentReader.decRef();
          } catch (IOException e) {
          }
        }
      }
      if (VERBOSE) {
        System.out.println("writer stopped - forced by reader: " + holder.stop);
      }
    }
    
  }

  public static final class ReaderThread extends Thread {
    private final ReaderHolder holder;
    private final CountDownLatch latch;
    Throwable failed;

    ReaderThread(ReaderHolder holder, CountDownLatch latch) {
      super();
      this.holder = holder;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        latch.await();
      } catch (InterruptedException e) {
        failed = e;
        return;
      }
      DirectoryReader reader;
      while ((reader = holder.reader) != null) {
        if (reader.tryIncRef()) {
          try {
            boolean current = reader.isCurrent();
            if (VERBOSE) {
              System.out.println("Thread: " + Thread.currentThread() + " Reader: " + reader + " isCurrent:" + current);
            }

            assertFalse(current);
          } catch (Throwable e) {
            if (VERBOSE) {
              System.out.println("FAILED Thread: " + Thread.currentThread() + " Reader: " + reader + " isCurrent: false");
            }
            failed = e;
            holder.stop = true;
            return;
          } finally {
            try {
              reader.decRef();
            } catch (IOException e) {
              if (failed == null) {
                failed = e;
              }
              return;
            }
          }
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3136.java