error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3825.java
text:
```scala
d@@ouble min = sis.info(0).sizeInBytes(true);

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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;

public class TestSizeBoundedOptimize extends LuceneTestCase {

  private void addDocs(IndexWriter writer, int numDocs) throws IOException {
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      writer.addDocument(doc);
    }
    writer.commit();
  }
  
  private static IndexWriterConfig newWriterConfig() throws IOException {
    IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT, null);
    conf.setMaxBufferedDocs(IndexWriterConfig.DISABLE_AUTO_FLUSH);
    conf.setRAMBufferSizeMB(IndexWriterConfig.DEFAULT_RAM_BUFFER_SIZE_MB);
    // prevent any merges by default.
    conf.setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    return conf;
  }
  
  public void testByteSizeLimit() throws Exception {
    // tests that the max merge size constraint is applied during optimize.
    Directory dir = new RAMDirectory();

    // Prepare an index w/ several small segments and a large one.
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    final int numSegments = 15;
    for (int i = 0; i < numSegments; i++) {
      int numDocs = i == 7 ? 30 : 1;
      addDocs(writer, numDocs);
    }
    writer.close();
    
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    double min = sis.info(0).sizeInBytes();
    
    conf = newWriterConfig();
    LogByteSizeMergePolicy lmp = new LogByteSizeMergePolicy();
    lmp.setMaxMergeMB((min + 1) / (1 << 20));
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();

    // Should only be 3 segments in the index, because one of them exceeds the size limit
    sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(3, sis.size());
  }

  public void testNumDocsLimit() throws Exception {
    // tests that the max merge docs constraint is applied during optimize.
    Directory dir = new RAMDirectory();

    // Prepare an index w/ several small segments and a large one.
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);

    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 5);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    
    writer.close();

    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();

    // Should only be 3 segments in the index, because one of them exceeds the size limit
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(3, sis.size());
  }

  public void testLastSegmentTooLarge() throws Exception {
    Directory dir = new RAMDirectory();

    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);

    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 5);
    
    writer.close();

    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();

    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(2, sis.size());
  }
  
  public void testFirstSegmentTooLarge() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 5);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(2, sis.size());
  }
  
  public void testAllSegmentsSmall() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(1, sis.size());
  }
  
  public void testAllSegmentsLarge() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(2);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(3, sis.size());
  }
  
  public void testOneLargeOneSmall() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    addDocs(writer, 5);
    addDocs(writer, 3);
    addDocs(writer, 5);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(4, sis.size());
  }
  
  public void testMergeFactor() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 3);
    addDocs(writer, 5);
    addDocs(writer, 3);
    addDocs(writer, 3);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    lmp.setMergeFactor(2);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    // Should only be 4 segments in the index, because of the merge factor and
    // max merge docs settings.
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(4, sis.size());
  }
  
  public void testSingleNonOptimizedSegment() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    addDocs(writer, 5);
    addDocs(writer, 3);
    
    writer.close();
  
    // delete the last document, so that the last segment is optimized.
    IndexReader r = IndexReader.open(dir, false);
    r.deleteDocument(r.numDocs() - 1);
    r.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    // Verify that the last segment does not have deletions.
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(3, sis.size());
    assertFalse(sis.info(2).hasDeletions());
  }
  
  public void testSingleOptimizedSegment() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 3);
    
    writer.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(3);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    // Verify that the last segment does not have deletions.
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(1, sis.size());
  }

  public void testSingleNonOptimizedTooLargeSegment() throws Exception {
    Directory dir = new RAMDirectory();
    
    IndexWriterConfig conf = newWriterConfig();
    IndexWriter writer = new IndexWriter(dir, conf);
    
    addDocs(writer, 5);
    
    writer.close();
  
    // delete the last document
    IndexReader r = IndexReader.open(dir, false);
    r.deleteDocument(r.numDocs() - 1);
    r.close();
    
    conf = newWriterConfig();
    LogMergePolicy lmp = new LogDocMergePolicy();
    lmp.setMaxMergeDocs(2);
    conf.setMergePolicy(lmp);
    
    writer = new IndexWriter(dir, conf);
    writer.optimize();
    writer.close();
    
    // Verify that the last segment does not have deletions.
    SegmentInfos sis = new SegmentInfos();
    sis.read(dir);
    assertEquals(1, sis.size());
    assertTrue(sis.info(0).hasDeletions());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3825.java