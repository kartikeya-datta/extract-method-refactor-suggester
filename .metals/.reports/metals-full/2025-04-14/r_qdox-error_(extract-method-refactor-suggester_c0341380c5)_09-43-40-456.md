error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1110.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1110.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1110.java
text:
```scala
d@@oc.add(newStringField("id", "test", Field.Store.NO));

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.memory.MemoryPostingsFormat;
import org.apache.lucene.document.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.*;
import org.junit.Test;

public class TestRollingUpdates extends LuceneTestCase {

  // Just updates the same set of N docs over and over, to
  // stress out deletions

  @Test
  public void testRollingUpdates() throws Exception {
    Random random = new Random(random().nextLong());
    final MockDirectoryWrapper dir = newDirectory();
    dir.setCheckIndexOnClose(false); // we use a custom codec provider
    final LineFileDocs docs = new LineFileDocs(random, true);

    //provider.register(new MemoryCodec());
    if (random().nextBoolean()) {
      Codec.setDefault(_TestUtil.alwaysPostingsFormat(new MemoryPostingsFormat(random().nextBoolean())));
    }

    final IndexWriter w = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    final int SIZE = atLeast(20);
    int id = 0;
    IndexReader r = null;
    final int numUpdates = (int) (SIZE * (2+(TEST_NIGHTLY ? 200*random().nextDouble() : 5*random().nextDouble())));
    if (VERBOSE) {
      System.out.println("TEST: numUpdates=" + numUpdates);
    }
    for(int docIter=0;docIter<numUpdates;docIter++) {
      final Document doc = docs.nextDoc();
      final String myID = ""+id;
      if (id == SIZE-1) {
        id = 0;
      } else {
        id++;
      }
      ((Field) doc.getField("docid")).setStringValue(myID);
      w.updateDocument(new Term("docid", myID), doc);

      if (docIter >= SIZE && random().nextInt(50) == 17) {
        if (r != null) {
          r.close();
        }
        final boolean applyDeletions = random().nextBoolean();
        r = w.getReader(applyDeletions);
        assertTrue("applyDeletions=" + applyDeletions + " r.numDocs()=" + r.numDocs() + " vs SIZE=" + SIZE, !applyDeletions || r.numDocs() == SIZE);
      }
    }

    if (r != null) {
      r.close();
    }

    w.commit();
    assertEquals(SIZE, w.numDocs());

    w.close();
    docs.close();
    
    _TestUtil.checkIndex(dir);
    dir.close();
  }
  
  
  public void testUpdateSameDoc() throws Exception {
    final Directory dir = newDirectory();

    final LineFileDocs docs = new LineFileDocs(random());
    for (int r = 0; r < 3; r++) {
      final IndexWriter w = new IndexWriter(dir, newIndexWriterConfig(
          TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMaxBufferedDocs(2));
      final int numUpdates = atLeast(20);
      int numThreads = _TestUtil.nextInt(random(), 2, 6);
      IndexingThread[] threads = new IndexingThread[numThreads];
      for (int i = 0; i < numThreads; i++) {
        threads[i] = new IndexingThread(docs, w, numUpdates);
        threads[i].start();
      }

      for (int i = 0; i < numThreads; i++) {
        threads[i].join();
      }

      w.close();
    }

    IndexReader open = DirectoryReader.open(dir);
    assertEquals(1, open.numDocs());
    open.close();
    docs.close();
    dir.close();
  }
  
  static class IndexingThread extends Thread {
    final LineFileDocs docs;
    final IndexWriter writer;
    final int num;
    
    public IndexingThread(LineFileDocs docs, IndexWriter writer, int num) {
      super();
      this.docs = docs;
      this.writer = writer;
      this.num = num;
    }

    public void run() {
      try {
        DirectoryReader open = null;
        for (int i = 0; i < num; i++) {
          Document doc = new Document();// docs.nextDoc();
          doc.add(newField("id", "test", StringField.TYPE_UNSTORED));
          writer.updateDocument(new Term("id", "test"), doc);
          if (random().nextInt(3) == 0) {
            if (open == null) {
              open = DirectoryReader.open(writer, true);
            }
            DirectoryReader reader = DirectoryReader.openIfChanged(open);
            if (reader != null) {
              open.close();
              open = reader;
            }
            assertEquals("iter: " + i + " numDocs: "+ open.numDocs() + " del: " + open.numDeletedDocs() + " max: " + open.maxDoc(), 1, open.numDocs());
          }
        }
        if (open != null) {
          open.close();
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1110.java