error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2506.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2506.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2506.java
text:
```scala
d@@irectory = FSDirectory.getDirectory(dirName);

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

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MockRAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.util.English;

import org.apache.lucene.util.LuceneTestCase;

import java.io.IOException;
import java.io.File;

public class TestThreadedOptimize extends LuceneTestCase {
  
  private static final Analyzer ANALYZER = new SimpleAnalyzer();

  private final static int NUM_THREADS = 3;
  //private final static int NUM_THREADS = 5;

  private final static int NUM_ITER = 2;
  //private final static int NUM_ITER = 10;

  private final static int NUM_ITER2 = 2;
  //private final static int NUM_ITER2 = 5;

  private boolean failed;

  private void setFailed() {
    failed = true;
  }

  public void runTest(Directory directory, boolean autoCommit, MergeScheduler merger) throws Exception {

    IndexWriter writer = new IndexWriter(directory, autoCommit, ANALYZER, true);
    writer.setMaxBufferedDocs(2);
    if (merger != null)
      writer.setMergeScheduler(merger);

    for(int iter=0;iter<NUM_ITER;iter++) {
      final int iterFinal = iter;

      writer.setMergeFactor(1000);

      for(int i=0;i<200;i++) {
        Document d = new Document();
        d.add(new Field("id", Integer.toString(i), Field.Store.YES, Field.Index.UN_TOKENIZED));
        d.add(new Field("contents", English.intToEnglish(i), Field.Store.NO, Field.Index.TOKENIZED));
        writer.addDocument(d);
      }

      writer.setMergeFactor(4);
      //writer.setInfoStream(System.out);

      final int docCount = writer.docCount();

      Thread[] threads = new Thread[NUM_THREADS];
      
      for(int i=0;i<NUM_THREADS;i++) {
        final int iFinal = i;
        final IndexWriter writerFinal = writer;
        threads[i] = new Thread() {
          public void run() {
            try {
              for(int j=0;j<NUM_ITER2;j++) {
                writerFinal.optimize(false);
                for(int k=0;k<17*(1+iFinal);k++) {
                  Document d = new Document();
                  d.add(new Field("id", iterFinal + "_" + iFinal + "_" + j + "_" + k, Field.Store.YES, Field.Index.UN_TOKENIZED));
                  d.add(new Field("contents", English.intToEnglish(iFinal+k), Field.Store.NO, Field.Index.TOKENIZED));
                  writerFinal.addDocument(d);
                }
                for(int k=0;k<9*(1+iFinal);k++)
                  writerFinal.deleteDocuments(new Term("id", iterFinal + "_" + iFinal + "_" + j + "_" + k));
                writerFinal.optimize();
              }
            } catch (Throwable t) {
              setFailed();
              System.out.println(Thread.currentThread().getName() + ": hit exception");
              t.printStackTrace(System.out);
            }
          }
        };
      }

      for(int i=0;i<NUM_THREADS;i++)
        threads[i].start();

      for(int i=0;i<NUM_THREADS;i++)
        threads[i].join();

      assertTrue(!failed);

      final int expectedDocCount = (int) ((1+iter)*(200+8*NUM_ITER2*(NUM_THREADS/2.0)*(1+NUM_THREADS)));

      // System.out.println("TEST: now index=" + writer.segString());

      assertEquals(expectedDocCount, writer.docCount());

      if (!autoCommit) {
        writer.close();
        writer = new IndexWriter(directory, autoCommit, ANALYZER, false);
        writer.setMaxBufferedDocs(2);
      }

      IndexReader reader = IndexReader.open(directory);
      assertTrue(reader.isOptimized());
      assertEquals(expectedDocCount, reader.numDocs());
      reader.close();
    }
    writer.close();
  }

  /*
    Run above stress test against RAMDirectory and then
    FSDirectory.
  */
  public void testThreadedOptimize() throws Exception {
    Directory directory = new MockRAMDirectory();
    runTest(directory, false, null);
    runTest(directory, true, null);
    runTest(directory, false, new ConcurrentMergeScheduler());
    runTest(directory, true, new ConcurrentMergeScheduler());
    directory.close();

    String tempDir = System.getProperty("tempDir");
    if (tempDir == null)
      throw new IOException("tempDir undefined, cannot run test");

    String dirName = tempDir + "/luceneTestThreadedOptimize";
    directory = FSDirectory.getDirectory(dirName, null, false);
    runTest(directory, false, null);
    runTest(directory, true, null);
    runTest(directory, false, new ConcurrentMergeScheduler());
    runTest(directory, true, new ConcurrentMergeScheduler());
    directory.close();
    _TestUtil.rmDir(dirName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2506.java