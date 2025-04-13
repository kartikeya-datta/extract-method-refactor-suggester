error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1767.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1767.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1767.java
text:
```scala
i@@f ("flush".equals(trace[i].getMethodName())) {

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

import org.apache.lucene.store.MockRAMDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

import org.apache.lucene.util.LuceneTestCase;
import java.io.IOException;

public class TestConcurrentMergeScheduler extends LuceneTestCase {
  
  private static class FailOnlyOnFlush extends MockRAMDirectory.Failure {
    boolean doFail;
    boolean hitExc;

    @Override
    public void setDoFail() {
      this.doFail = true;
      hitExc = false;
    }
    @Override
    public void clearDoFail() {
      this.doFail = false;
    }

    @Override
    public void eval(MockRAMDirectory dir)  throws IOException {
      if (doFail && Thread.currentThread().getName().equals("main")) {
        StackTraceElement[] trace = new Exception().getStackTrace();
        for (int i = 0; i < trace.length; i++) {
          if ("doFlush".equals(trace[i].getMethodName())) {
            hitExc = true;
            throw new IOException("now failing during flush");
          }
        }
      }
    }
  }

  // Make sure running BG merges still work fine even when
  // we are hitting exceptions during flushing.
  public void testFlushExceptions() throws IOException {

    MockRAMDirectory directory = new MockRAMDirectory();
    FailOnlyOnFlush failure = new FailOnlyOnFlush();
    directory.failOn(failure);

    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer()).setMaxBufferedDocs(2));
    Document doc = new Document();
    Field idField = new Field("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED);
    doc.add(idField);
    int extraCount = 0;

    for(int i=0;i<10;i++) {
      for(int j=0;j<20;j++) {
        idField.setValue(Integer.toString(i*20+j));
        writer.addDocument(doc);
      }

      // must cycle here because sometimes the merge flushes
      // the doc we just added and so there's nothing to
      // flush, and we don't hit the exception
      while(true) {
        writer.addDocument(doc);
        failure.setDoFail();
        try {
          writer.flush(true, true);
          if (failure.hitExc) {
            fail("failed to hit IOException");
          }
          extraCount++;
        } catch (IOException ioe) {
          failure.clearDoFail();
          break;
        }
      }
    }

    writer.close();
    IndexReader reader = IndexReader.open(directory, true);
    assertEquals(200+extraCount, reader.numDocs());
    reader.close();
    directory.close();
  }

  // Test that deletes committed after a merge started and
  // before it finishes, are correctly merged back:
  public void testDeleteMerging() throws IOException {

    RAMDirectory directory = new MockRAMDirectory();

    LogDocMergePolicy mp = new LogDocMergePolicy();
    // Force degenerate merging so we can get a mix of
    // merging of segments with and without deletes at the
    // start:
    mp.setMinMergeDocs(1000);
    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer())
        .setMergePolicy(mp));

    Document doc = new Document();
    Field idField = new Field("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED);
    doc.add(idField);
    for(int i=0;i<10;i++) {
      for(int j=0;j<100;j++) {
        idField.setValue(Integer.toString(i*100+j));
        writer.addDocument(doc);
      }

      int delID = i;
      while(delID < 100*(1+i)) {
        writer.deleteDocuments(new Term("id", ""+delID));
        delID += 10;
      }

      writer.commit();
    }

    writer.close();
    IndexReader reader = IndexReader.open(directory, true);
    // Verify that we did not lose any deletes...
    assertEquals(450, reader.numDocs());
    reader.close();
    directory.close();
  }

  public void testNoExtraFiles() throws IOException {

    RAMDirectory directory = new MockRAMDirectory();

    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer())
        .setMaxBufferedDocs(2));

    for(int iter=0;iter<7;iter++) {

      for(int j=0;j<21;j++) {
        Document doc = new Document();
        doc.add(new Field("content", "a b c", Field.Store.NO, Field.Index.ANALYZED));
        writer.addDocument(doc);
      }
        
      writer.close();
      TestIndexWriter.assertNoUnreferencedFiles(directory, "testNoExtraFiles");

      // Reopen
      writer = new IndexWriter(directory, new IndexWriterConfig(
          TEST_VERSION_CURRENT, new MockAnalyzer())
          .setOpenMode(OpenMode.APPEND).setMaxBufferedDocs(2));
    }

    writer.close();

    directory.close();
  }

  public void testNoWaitClose() throws IOException {
    RAMDirectory directory = new MockRAMDirectory();

    Document doc = new Document();
    Field idField = new Field("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED);
    doc.add(idField);

    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer()).setMaxBufferedDocs(2));
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(100);

    for(int iter=0;iter<10;iter++) {

      for(int j=0;j<201;j++) {
        idField.setValue(Integer.toString(iter*201+j));
        writer.addDocument(doc);
      }

      int delID = iter*201;
      for(int j=0;j<20;j++) {
        writer.deleteDocuments(new Term("id", Integer.toString(delID)));
        delID += 5;
      }

      // Force a bunch of merge threads to kick off so we
      // stress out aborting them on close:
      ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(3);
      writer.addDocument(doc);
      writer.commit();

      writer.close(false);

      IndexReader reader = IndexReader.open(directory, true);
      assertEquals((1+iter)*182, reader.numDocs());
      reader.close();

      // Reopen
      writer = new IndexWriter(directory, new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer()).setOpenMode(OpenMode.APPEND));
      ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(100);
    }
    writer.close();

    directory.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1767.java