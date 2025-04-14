error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2968.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2968.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2968.java
text:
```scala
(@@new IndexSearcher(directory, true)).close();

package org.apache.lucene.index;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.util.*;
import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.search.*;
import org.apache.lucene.queryParser.*;

import java.util.Random;
import java.io.File;

public class TestStressIndexing extends LuceneTestCase {
  private static final Analyzer ANALYZER = new SimpleAnalyzer();
  private Random RANDOM;

  private static abstract class TimedThread extends Thread {
    boolean failed;
    int count;
    private static int RUN_TIME_SEC = 6;
    private TimedThread[] allThreads;

    abstract public void doWork() throws Throwable;

    TimedThread(TimedThread[] threads) {
      this.allThreads = threads;
    }

    public void run() {
      final long stopTime = System.currentTimeMillis() + 1000*RUN_TIME_SEC;

      count = 0;

      try {
        while(System.currentTimeMillis() < stopTime && !anyErrors()) {
          doWork();
          count++;
        }
      } catch (Throwable e) {
        System.out.println(Thread.currentThread() + ": exc");
        e.printStackTrace(System.out);
        failed = true;
      }
    }

    private boolean anyErrors() {
      for(int i=0;i<allThreads.length;i++)
        if (allThreads[i] != null && allThreads[i].failed)
          return true;
      return false;
    }
  }

  private class IndexerThread extends TimedThread {
    IndexWriter writer;
    public int count;
    int nextID;

    public IndexerThread(IndexWriter writer, TimedThread[] threads) {
      super(threads);
      this.writer = writer;
    }

    public void doWork() throws Exception {
      // Add 10 docs:
      for(int j=0; j<10; j++) {
        Document d = new Document();
        int n = RANDOM.nextInt();
        d.add(new Field("id", Integer.toString(nextID++), Field.Store.YES, Field.Index.NOT_ANALYZED));
        d.add(new Field("contents", English.intToEnglish(n), Field.Store.NO, Field.Index.ANALYZED));
        writer.addDocument(d);
      }

      // Delete 5 docs:
      int deleteID = nextID-1;
      for(int j=0; j<5; j++) {
        writer.deleteDocuments(new Term("id", ""+deleteID));
        deleteID -= 2;
      }
    }
  }

  private static class SearcherThread extends TimedThread {
    private Directory directory;

    public SearcherThread(Directory directory, TimedThread[] threads) {
      super(threads);
      this.directory = directory;
    }

    public void doWork() throws Throwable {
      for (int i=0; i<100; i++)
        (new IndexSearcher(directory)).close();
      count += 100;
    }
  }

  /*
    Run one indexer and 2 searchers against single index as
    stress test.
  */
  public void runStressTest(Directory directory, boolean autoCommit, MergeScheduler mergeScheduler) throws Exception {
    IndexWriter modifier = new IndexWriter(directory, autoCommit, ANALYZER, true);

    modifier.setMaxBufferedDocs(10);

    TimedThread[] threads = new TimedThread[4];
    int numThread = 0;

    if (mergeScheduler != null)
      modifier.setMergeScheduler(mergeScheduler);

    // One modifier that writes 10 docs then removes 5, over
    // and over:
    IndexerThread indexerThread = new IndexerThread(modifier, threads);
    threads[numThread++] = indexerThread;
    indexerThread.start();
    
    IndexerThread indexerThread2 = new IndexerThread(modifier, threads);
    threads[numThread++] = indexerThread2;
    indexerThread2.start();
      
    // Two searchers that constantly just re-instantiate the
    // searcher:
    SearcherThread searcherThread1 = new SearcherThread(directory, threads);
    threads[numThread++] = searcherThread1;
    searcherThread1.start();

    SearcherThread searcherThread2 = new SearcherThread(directory, threads);
    threads[numThread++] = searcherThread2;
    searcherThread2.start();

    for(int i=0;i<numThread;i++)
      threads[i].join();

    modifier.close();

    for(int i=0;i<numThread;i++)
      assertTrue(!((TimedThread) threads[i]).failed);

    //System.out.println("    Writer: " + indexerThread.count + " iterations");
    //System.out.println("Searcher 1: " + searcherThread1.count + " searchers created");
    //System.out.println("Searcher 2: " + searcherThread2.count + " searchers created");
  }

  /*
    Run above stress test against RAMDirectory and then
    FSDirectory.
  */
  public void testStressIndexAndSearching() throws Exception {
    RANDOM = newRandom();

    // RAMDir
    Directory directory = new MockRAMDirectory();
    runStressTest(directory, true, null);
    directory.close();

    // FSDir
    File dirPath = _TestUtil.getTempDir("lucene.test.stress");
    directory = FSDirectory.open(dirPath);
    runStressTest(directory, true, null);
    directory.close();

    // With ConcurrentMergeScheduler, in RAMDir
    directory = new MockRAMDirectory();
    runStressTest(directory, true, new ConcurrentMergeScheduler());
    directory.close();

    // With ConcurrentMergeScheduler, in FSDir
    directory = FSDirectory.open(dirPath);
    runStressTest(directory, true, new ConcurrentMergeScheduler());
    directory.close();

    // With ConcurrentMergeScheduler and autoCommit=false, in RAMDir
    directory = new MockRAMDirectory();
    runStressTest(directory, false, new ConcurrentMergeScheduler());
    directory.close();

    // With ConcurrentMergeScheduler and autoCommit=false, in FSDir
    directory = FSDirectory.open(dirPath);
    runStressTest(directory, false, new ConcurrentMergeScheduler());
    directory.close();

    _TestUtil.rmDir(dirPath);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2968.java