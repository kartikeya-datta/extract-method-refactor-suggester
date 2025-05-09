error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/771.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/771.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/771.java
text:
```scala
M@@ockDirectoryWrapper dir = newMockDirectory();

package org.apache.lucene.index;

/*
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DocumentsWriterPerThreadPool.ThreadState;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.util.LineFileDocs;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestFlushByRamOrCountsPolicy extends LuceneTestCase {

  private static LineFileDocs lineDocFile;

  @BeforeClass
  public static void beforeClass() throws Exception {
    lineDocFile = new LineFileDocs(random(), defaultCodecSupportsDocValues());
  }
  
  @AfterClass
  public static void afterClass() throws Exception {
    lineDocFile.close();
    lineDocFile = null;
  }

  public void testFlushByRam() throws IOException, InterruptedException {
    final double ramBuffer = (TEST_NIGHTLY ? 1 : 10) + atLeast(2)
        + random().nextDouble();
    runFlushByRam(1 + random().nextInt(TEST_NIGHTLY ? 5 : 1), ramBuffer, false);
  }
  
  public void testFlushByRamLargeBuffer() throws IOException, InterruptedException {
    // with a 256 mb ram buffer we should never stall
    runFlushByRam(1 + random().nextInt(TEST_NIGHTLY ? 5 : 1), 256.d, true);
  }

  protected void runFlushByRam(int numThreads, double maxRamMB,
      boolean ensureNotStalled) throws IOException, InterruptedException {
    final int numDocumentsToIndex = 10 + atLeast(30);
    AtomicInteger numDocs = new AtomicInteger(numDocumentsToIndex);
    Directory dir = newDirectory();
    MockDefaultFlushPolicy flushPolicy = new MockDefaultFlushPolicy();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random())).setFlushPolicy(flushPolicy);
    final int numDWPT = 1 + atLeast(2);
    DocumentsWriterPerThreadPool threadPool = new ThreadAffinityDocumentsWriterThreadPool(
        numDWPT);
    iwc.setIndexerThreadPool(threadPool);
    iwc.setRAMBufferSizeMB(maxRamMB);
    iwc.setMaxBufferedDocs(IndexWriterConfig.DISABLE_AUTO_FLUSH);
    iwc.setMaxBufferedDeleteTerms(IndexWriterConfig.DISABLE_AUTO_FLUSH);
    IndexWriter writer = new IndexWriter(dir, iwc);
    flushPolicy = (MockDefaultFlushPolicy) writer.getConfig().getFlushPolicy();
    assertFalse(flushPolicy.flushOnDocCount());
    assertFalse(flushPolicy.flushOnDeleteTerms());
    assertTrue(flushPolicy.flushOnRAM());
    DocumentsWriter docsWriter = writer.getDocsWriter();
    assertNotNull(docsWriter);
    DocumentsWriterFlushControl flushControl = docsWriter.flushControl;
    assertEquals(" bytes must be 0 after init", 0, flushControl.flushBytes());

    IndexThread[] threads = new IndexThread[numThreads];
    for (int x = 0; x < threads.length; x++) {
      threads[x] = new IndexThread(numDocs, numThreads, writer, lineDocFile,
          false);
      threads[x].start();
    }

    for (int x = 0; x < threads.length; x++) {
      threads[x].join();
    }
    final long maxRAMBytes = (long) (iwc.getRAMBufferSizeMB() * 1024. * 1024.);
    assertEquals(" all flushes must be due numThreads=" + numThreads, 0,
        flushControl.flushBytes());
    assertEquals(numDocumentsToIndex, writer.numDocs());
    assertEquals(numDocumentsToIndex, writer.maxDoc());
    assertTrue("peak bytes without flush exceeded watermark",
        flushPolicy.peakBytesWithoutFlush <= maxRAMBytes);
    assertActiveBytesAfter(flushControl);
    if (flushPolicy.hasMarkedPending) {
      assertTrue(maxRAMBytes < flushControl.peakActiveBytes);
    }
    if (ensureNotStalled) {
      assertFalse(docsWriter.flushControl.stallControl.wasStalled());
    }
    writer.close();
    assertEquals(0, flushControl.activeBytes());
    dir.close();
  }

  public void testFlushDocCount() throws IOException, InterruptedException {
    int[] numThreads = new int[] { 2 + atLeast(1), 1 };
    for (int i = 0; i < numThreads.length; i++) {

      final int numDocumentsToIndex =  50 + atLeast(30);
      AtomicInteger numDocs = new AtomicInteger(numDocumentsToIndex);
      Directory dir = newDirectory();
      MockDefaultFlushPolicy flushPolicy = new MockDefaultFlushPolicy();
      IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT,
          new MockAnalyzer(random())).setFlushPolicy(flushPolicy);

      final int numDWPT = 1 + atLeast(2);
      DocumentsWriterPerThreadPool threadPool = new ThreadAffinityDocumentsWriterThreadPool(
          numDWPT);
      iwc.setIndexerThreadPool(threadPool);
      iwc.setMaxBufferedDocs(2 + atLeast(10));
      iwc.setRAMBufferSizeMB(IndexWriterConfig.DISABLE_AUTO_FLUSH);
      iwc.setMaxBufferedDeleteTerms(IndexWriterConfig.DISABLE_AUTO_FLUSH);
      IndexWriter writer = new IndexWriter(dir, iwc);
      flushPolicy = (MockDefaultFlushPolicy) writer.getConfig().getFlushPolicy();
      assertTrue(flushPolicy.flushOnDocCount());
      assertFalse(flushPolicy.flushOnDeleteTerms());
      assertFalse(flushPolicy.flushOnRAM());
      DocumentsWriter docsWriter = writer.getDocsWriter();
      assertNotNull(docsWriter);
      DocumentsWriterFlushControl flushControl = docsWriter.flushControl;
      assertEquals(" bytes must be 0 after init", 0, flushControl.flushBytes());

      IndexThread[] threads = new IndexThread[numThreads[i]];
      for (int x = 0; x < threads.length; x++) {
        threads[x] = new IndexThread(numDocs, numThreads[i], writer,
            lineDocFile, false);
        threads[x].start();
      }

      for (int x = 0; x < threads.length; x++) {
        threads[x].join();
      }

      assertEquals(" all flushes must be due numThreads=" + numThreads[i], 0,
          flushControl.flushBytes());
      assertEquals(numDocumentsToIndex, writer.numDocs());
      assertEquals(numDocumentsToIndex, writer.maxDoc());
      assertTrue("peak bytes without flush exceeded watermark",
          flushPolicy.peakDocCountWithoutFlush <= iwc.getMaxBufferedDocs());
      assertActiveBytesAfter(flushControl);
      writer.close();
      assertEquals(0, flushControl.activeBytes());
      dir.close();
    }
  }

  public void testRandom() throws IOException, InterruptedException {
    final int numThreads = 1 + random().nextInt(8);
    final int numDocumentsToIndex = 50 + atLeast(70);
    AtomicInteger numDocs = new AtomicInteger(numDocumentsToIndex);
    Directory dir = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random()));
    MockDefaultFlushPolicy flushPolicy = new MockDefaultFlushPolicy();
    iwc.setFlushPolicy(flushPolicy);

    final int numDWPT = 1 + random().nextInt(8);
    DocumentsWriterPerThreadPool threadPool = new ThreadAffinityDocumentsWriterThreadPool(
        numDWPT);
    iwc.setIndexerThreadPool(threadPool);

    IndexWriter writer = new IndexWriter(dir, iwc);
    flushPolicy = (MockDefaultFlushPolicy) writer.getConfig().getFlushPolicy();
    DocumentsWriter docsWriter = writer.getDocsWriter();
    assertNotNull(docsWriter);
    DocumentsWriterFlushControl flushControl = docsWriter.flushControl;

    assertEquals(" bytes must be 0 after init", 0, flushControl.flushBytes());

    IndexThread[] threads = new IndexThread[numThreads];
    for (int x = 0; x < threads.length; x++) {
      threads[x] = new IndexThread(numDocs, numThreads, writer, lineDocFile,
          true);
      threads[x].start();
    }

    for (int x = 0; x < threads.length; x++) {
      threads[x].join();
    }
    assertEquals(" all flushes must be due", 0, flushControl.flushBytes());
    assertEquals(numDocumentsToIndex, writer.numDocs());
    assertEquals(numDocumentsToIndex, writer.maxDoc());
    if (flushPolicy.flushOnRAM() && !flushPolicy.flushOnDocCount()
        && !flushPolicy.flushOnDeleteTerms()) {
      final long maxRAMBytes = (long) (iwc.getRAMBufferSizeMB() * 1024. * 1024.);
      assertTrue("peak bytes without flush exceeded watermark",
          flushPolicy.peakBytesWithoutFlush <= maxRAMBytes);
      if (flushPolicy.hasMarkedPending) {
        assertTrue("max: " + maxRAMBytes + " " + flushControl.peakActiveBytes,
            maxRAMBytes <= flushControl.peakActiveBytes);
      }
    }
    assertActiveBytesAfter(flushControl);
    writer.commit();
    assertEquals(0, flushControl.activeBytes());
    IndexReader r = DirectoryReader.open(dir);
    assertEquals(numDocumentsToIndex, r.numDocs());
    assertEquals(numDocumentsToIndex, r.maxDoc());
    if (!flushPolicy.flushOnRAM()) {
      assertFalse("never stall if we don't flush on RAM", docsWriter.flushControl.stallControl.wasStalled());
      assertFalse("never block if we don't flush on RAM", docsWriter.flushControl.stallControl.hasBlocked());
    }
    r.close();
    writer.close();
    dir.close();
  }

  public void testStallControl() throws InterruptedException, IOException {

    int[] numThreads = new int[] { 4 + random().nextInt(8), 1 };
    final int numDocumentsToIndex = 50 + random().nextInt(50);
    for (int i = 0; i < numThreads.length; i++) {
      AtomicInteger numDocs = new AtomicInteger(numDocumentsToIndex);
      MockDirectoryWrapper dir = newDirectory();
      // mock a very slow harddisk sometimes here so that flushing is very slow
      dir.setThrottling(MockDirectoryWrapper.Throttling.SOMETIMES);
      IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT,
          new MockAnalyzer(random()));
      iwc.setMaxBufferedDocs(IndexWriterConfig.DISABLE_AUTO_FLUSH);
      iwc.setMaxBufferedDeleteTerms(IndexWriterConfig.DISABLE_AUTO_FLUSH);
      FlushPolicy flushPolicy = new FlushByRamOrCountsPolicy();
      iwc.setFlushPolicy(flushPolicy);
      
      DocumentsWriterPerThreadPool threadPool = new ThreadAffinityDocumentsWriterThreadPool(
          numThreads[i]== 1 ? 1 : 2);
      iwc.setIndexerThreadPool(threadPool);
      // with such a small ram buffer we should be stalled quiet quickly
      iwc.setRAMBufferSizeMB(0.25);
      IndexWriter writer = new IndexWriter(dir, iwc);
      IndexThread[] threads = new IndexThread[numThreads[i]];
      for (int x = 0; x < threads.length; x++) {
        threads[x] = new IndexThread(numDocs, numThreads[i], writer,
            lineDocFile, false);
        threads[x].start();
      }

      for (int x = 0; x < threads.length; x++) {
        threads[x].join();
      }
      DocumentsWriter docsWriter = writer.getDocsWriter();
      assertNotNull(docsWriter);
      DocumentsWriterFlushControl flushControl = docsWriter.flushControl;
      assertEquals(" all flushes must be due", 0, flushControl.flushBytes());
      assertEquals(numDocumentsToIndex, writer.numDocs());
      assertEquals(numDocumentsToIndex, writer.maxDoc());
      if (numThreads[i] == 1) {
        assertFalse(
            "single thread must not block numThreads: " + numThreads[i],
            docsWriter.flushControl.stallControl.hasBlocked());
      }
      if (docsWriter.flushControl.peakNetBytes > (2.d * iwc.getRAMBufferSizeMB() * 1024.d * 1024.d)) {
        assertTrue(docsWriter.flushControl.stallControl.wasStalled());
      }
      assertActiveBytesAfter(flushControl);
      writer.close(true);
      dir.close();
    }
  }

  protected void assertActiveBytesAfter(DocumentsWriterFlushControl flushControl) {
    Iterator<ThreadState> allActiveThreads = flushControl.allActiveThreadStates();
    long bytesUsed = 0;
    while (allActiveThreads.hasNext()) {
      bytesUsed += allActiveThreads.next().dwpt.bytesUsed();
    }
    assertEquals(bytesUsed, flushControl.activeBytes());
  }

  public class IndexThread extends Thread {
    IndexWriter writer;
    LiveIndexWriterConfig iwc;
    LineFileDocs docs;
    private AtomicInteger pendingDocs;
    private final boolean doRandomCommit;

    public IndexThread(AtomicInteger pendingDocs, int numThreads,
        IndexWriter writer, LineFileDocs docs, boolean doRandomCommit) {
      this.pendingDocs = pendingDocs;
      this.writer = writer;
      iwc = writer.getConfig();
      this.docs = docs;
      this.doRandomCommit = doRandomCommit;
    }

    public void run() {
      try {
        long ramSize = 0;
        while (pendingDocs.decrementAndGet() > -1) {
          Document doc = docs.nextDoc();
          writer.addDocument(doc);
          long newRamSize = writer.ramSizeInBytes();
          if (newRamSize != ramSize) {
            ramSize = newRamSize;
          }
          if (doRandomCommit) {
            if (rarely()) {
              writer.commit();
            }
          }
        }
        writer.commit();
      } catch (Throwable ex) {
        System.out.println("FAILED exc:");
        ex.printStackTrace(System.out);
        throw new RuntimeException(ex);
      }
    }
  }

  private static class MockDefaultFlushPolicy extends FlushByRamOrCountsPolicy {
    long peakBytesWithoutFlush = Integer.MIN_VALUE;
    long peakDocCountWithoutFlush = Integer.MIN_VALUE;
    boolean hasMarkedPending = false;

    @Override
    public void onDelete(DocumentsWriterFlushControl control, ThreadState state) {
      final ArrayList<ThreadState> pending = new ArrayList<DocumentsWriterPerThreadPool.ThreadState>();
      final ArrayList<ThreadState> notPending = new ArrayList<DocumentsWriterPerThreadPool.ThreadState>();
      findPending(control, pending, notPending);
      final boolean flushCurrent = state.flushPending;
      final ThreadState toFlush;
      if (state.flushPending) {
        toFlush = state;
      } else if (flushOnDeleteTerms()
          && state.dwpt.pendingDeletes.numTermDeletes.get() >= indexWriterConfig
              .getMaxBufferedDeleteTerms()) {
        toFlush = state;
      } else {
        toFlush = null;
      }
      super.onDelete(control, state);
      if (toFlush != null) {
        if (flushCurrent) {
          assertTrue(pending.remove(toFlush));
        } else {
          assertTrue(notPending.remove(toFlush));
        }
        assertTrue(toFlush.flushPending);
        hasMarkedPending = true;
      }

      for (ThreadState threadState : notPending) {
        assertFalse(threadState.flushPending);
      }
    }

    @Override
    public void onInsert(DocumentsWriterFlushControl control, ThreadState state) {
      final ArrayList<ThreadState> pending = new ArrayList<DocumentsWriterPerThreadPool.ThreadState>();
      final ArrayList<ThreadState> notPending = new ArrayList<DocumentsWriterPerThreadPool.ThreadState>();
      findPending(control, pending, notPending);
      final boolean flushCurrent = state.flushPending;
      long activeBytes = control.activeBytes();
      final ThreadState toFlush;
      if (state.flushPending) {
        toFlush = state;
      } else if (flushOnDocCount()
          && state.dwpt.getNumDocsInRAM() >= indexWriterConfig
              .getMaxBufferedDocs()) {
        toFlush = state;
      } else if (flushOnRAM()
          && activeBytes >= (long) (indexWriterConfig.getRAMBufferSizeMB() * 1024. * 1024.)) {
        toFlush = findLargestNonPendingWriter(control, state);
        assertFalse(toFlush.flushPending);
      } else {
        toFlush = null;
      }
      super.onInsert(control, state);
      if (toFlush != null) {
        if (flushCurrent) {
          assertTrue(pending.remove(toFlush));
        } else {
          assertTrue(notPending.remove(toFlush));
        }
        assertTrue(toFlush.flushPending);
        hasMarkedPending = true;
      } else {
        peakBytesWithoutFlush = Math.max(activeBytes, peakBytesWithoutFlush);
        peakDocCountWithoutFlush = Math.max(state.dwpt.getNumDocsInRAM(),
            peakDocCountWithoutFlush);
      }

      for (ThreadState threadState : notPending) {
        assertFalse(threadState.flushPending);
      }
    }
  }

  static void findPending(DocumentsWriterFlushControl flushControl,
      ArrayList<ThreadState> pending, ArrayList<ThreadState> notPending) {
    Iterator<ThreadState> allActiveThreads = flushControl.allActiveThreadStates();
    while (allActiveThreads.hasNext()) {
      ThreadState next = allActiveThreads.next();
      if (next.flushPending) {
        pending.add(next);
      } else {
        notPending.add(next);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/771.java