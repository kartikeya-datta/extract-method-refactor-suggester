error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3057.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3057.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3057.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.store;

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

import java.io.File;
import java.util.Random;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.store.Directory.IndexInputSlicer;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

/**
 * Tests MMapDirectory's MultiMMapIndexInput
 * <p>
 * Because Java's ByteBuffer uses an int to address the
 * values, it's necessary to access a file >
 * Integer.MAX_VALUE in size using multiple byte buffers.
 */
public class TestMultiMMap extends LuceneTestCase {
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    assumeTrue("test requires a jre that supports unmapping", MMapDirectory.UNMAP_SUPPORTED);
  }
  
  public void testCloneSafety() throws Exception {
    MMapDirectory mmapDir = new MMapDirectory(createTempDir("testCloneSafety"));
    IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
    io.writeVInt(5);
    io.close();
    IndexInput one = mmapDir.openInput("bytes", IOContext.DEFAULT);
    IndexInput two = one.clone();
    IndexInput three = two.clone(); // clone of clone
    one.close();
    try {
      one.readVInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    try {
      two.readVInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    try {
      three.readVInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    two.close();
    three.close();
    // test double close of master:
    one.close();
    mmapDir.close();
  }
  
  public void testCloneClose() throws Exception {
    MMapDirectory mmapDir = new MMapDirectory(createTempDir("testCloneClose"));
    IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
    io.writeVInt(5);
    io.close();
    IndexInput one = mmapDir.openInput("bytes", IOContext.DEFAULT);
    IndexInput two = one.clone();
    IndexInput three = two.clone(); // clone of clone
    two.close();
    assertEquals(5, one.readVInt());
    try {
      two.readVInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    assertEquals(5, three.readVInt());
    one.close();
    three.close();
    mmapDir.close();
  }
  
  public void testCloneSliceSafety() throws Exception {
    MMapDirectory mmapDir = new MMapDirectory(createTempDir("testCloneSliceSafety"));
    IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
    io.writeInt(1);
    io.writeInt(2);
    io.close();
    IndexInputSlicer slicer = mmapDir.createSlicer("bytes", newIOContext(random()));
    IndexInput one = slicer.openSlice("first int", 0, 4);
    IndexInput two = slicer.openSlice("second int", 4, 4);
    IndexInput three = one.clone(); // clone of clone
    IndexInput four = two.clone(); // clone of clone
    slicer.close();
    try {
      one.readInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    try {
      two.readInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    try {
      three.readInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    try {
      four.readInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    one.close();
    two.close();
    three.close();
    four.close();
    // test double-close of slicer:
    slicer.close();
    mmapDir.close();
  }

  public void testCloneSliceClose() throws Exception {
    MMapDirectory mmapDir = new MMapDirectory(createTempDir("testCloneSliceClose"));
    IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
    io.writeInt(1);
    io.writeInt(2);
    io.close();
    IndexInputSlicer slicer = mmapDir.createSlicer("bytes", newIOContext(random()));
    IndexInput one = slicer.openSlice("first int", 0, 4);
    IndexInput two = slicer.openSlice("second int", 4, 4);
    one.close();
    try {
      one.readInt();
      fail("Must throw AlreadyClosedException");
    } catch (AlreadyClosedException ignore) {
      // pass
    }
    assertEquals(2, two.readInt());
    // reopen a new slice "one":
    one = slicer.openSlice("first int", 0, 4);
    assertEquals(1, one.readInt());
    one.close();
    two.close();
    slicer.close();
    mmapDir.close();
  }

  public void testSeekZero() throws Exception {
    for (int i = 0; i < 31; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSeekZero"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("zeroBytes", newIOContext(random()));
      io.close();
      IndexInput ii = mmapDir.openInput("zeroBytes", newIOContext(random()));
      ii.seek(0L);
      ii.close();
      mmapDir.close();
    }
  }
  
  public void testSeekSliceZero() throws Exception {
    for (int i = 0; i < 31; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSeekSliceZero"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("zeroBytes", newIOContext(random()));
      io.close();
      IndexInputSlicer slicer = mmapDir.createSlicer("zeroBytes", newIOContext(random()));
      IndexInput ii = slicer.openSlice("zero-length slice", 0, 0);
      ii.seek(0L);
      ii.close();
      slicer.close();
      mmapDir.close();
    }
  }
  
  public void testSeekEnd() throws Exception {
    for (int i = 0; i < 17; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSeekEnd"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
      byte bytes[] = new byte[1<<i];
      random().nextBytes(bytes);
      io.writeBytes(bytes, bytes.length);
      io.close();
      IndexInput ii = mmapDir.openInput("bytes", newIOContext(random()));
      byte actual[] = new byte[1<<i];
      ii.readBytes(actual, 0, actual.length);
      assertEquals(new BytesRef(bytes), new BytesRef(actual));
      ii.seek(1<<i);
      ii.close();
      mmapDir.close();
    }
  }
  
  public void testSeekSliceEnd() throws Exception {
    for (int i = 0; i < 17; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSeekSliceEnd"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
      byte bytes[] = new byte[1<<i];
      random().nextBytes(bytes);
      io.writeBytes(bytes, bytes.length);
      io.close();
      IndexInputSlicer slicer = mmapDir.createSlicer("bytes", newIOContext(random()));
      IndexInput ii = slicer.openSlice("full slice", 0, bytes.length);
      byte actual[] = new byte[1<<i];
      ii.readBytes(actual, 0, actual.length);
      assertEquals(new BytesRef(bytes), new BytesRef(actual));
      ii.seek(1<<i);
      ii.close();
      slicer.close();
      mmapDir.close();
    }
  }
  
  public void testSeeking() throws Exception {
    for (int i = 0; i < 10; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSeeking"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
      byte bytes[] = new byte[1<<(i+1)]; // make sure we switch buffers
      random().nextBytes(bytes);
      io.writeBytes(bytes, bytes.length);
      io.close();
      IndexInput ii = mmapDir.openInput("bytes", newIOContext(random()));
      byte actual[] = new byte[1<<(i+1)]; // first read all bytes
      ii.readBytes(actual, 0, actual.length);
      assertEquals(new BytesRef(bytes), new BytesRef(actual));
      for (int sliceStart = 0; sliceStart < bytes.length; sliceStart++) {
        for (int sliceLength = 0; sliceLength < bytes.length - sliceStart; sliceLength++) {
          byte slice[] = new byte[sliceLength];
          ii.seek(sliceStart);
          ii.readBytes(slice, 0, slice.length);
          assertEquals(new BytesRef(bytes, sliceStart, sliceLength), new BytesRef(slice));
        }
      }
      ii.close();
      mmapDir.close();
    }
  }
  
  // note instead of seeking to offset and reading length, this opens slices at the 
  // the various offset+length and just does readBytes.
  public void testSlicedSeeking() throws Exception {
    for (int i = 0; i < 10; i++) {
      MMapDirectory mmapDir = new MMapDirectory(createTempDir("testSlicedSeeking"), null, 1<<i);
      IndexOutput io = mmapDir.createOutput("bytes", newIOContext(random()));
      byte bytes[] = new byte[1<<(i+1)]; // make sure we switch buffers
      random().nextBytes(bytes);
      io.writeBytes(bytes, bytes.length);
      io.close();
      IndexInput ii = mmapDir.openInput("bytes", newIOContext(random()));
      byte actual[] = new byte[1<<(i+1)]; // first read all bytes
      ii.readBytes(actual, 0, actual.length);
      ii.close();
      assertEquals(new BytesRef(bytes), new BytesRef(actual));
      IndexInputSlicer slicer = mmapDir.createSlicer("bytes", newIOContext(random()));
      for (int sliceStart = 0; sliceStart < bytes.length; sliceStart++) {
        for (int sliceLength = 0; sliceLength < bytes.length - sliceStart; sliceLength++) {
          byte slice[] = new byte[sliceLength];
          IndexInput input = slicer.openSlice("bytesSlice", sliceStart, slice.length);
          input.readBytes(slice, 0, slice.length);
          input.close();
          assertEquals(new BytesRef(bytes, sliceStart, sliceLength), new BytesRef(slice));
        }
      }
      slicer.close();
      mmapDir.close();
    }
  }
  
  public void testRandomChunkSizes() throws Exception {
    int num = atLeast(10);
    for (int i = 0; i < num; i++)
      assertChunking(random(), TestUtil.nextInt(random(), 20, 100));
  }
  
  private void assertChunking(Random random, int chunkSize) throws Exception {
    File path = createTempDir("mmap" + chunkSize);
    MMapDirectory mmapDir = new MMapDirectory(path, null, chunkSize);
    // we will map a lot, try to turn on the unmap hack
    if (MMapDirectory.UNMAP_SUPPORTED)
      mmapDir.setUseUnmap(true);
    MockDirectoryWrapper dir = new MockDirectoryWrapper(random, mmapDir);
    RandomIndexWriter writer = new RandomIndexWriter(random, dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMergePolicy(newLogMergePolicy()));
    Document doc = new Document();
    Field docid = newStringField("docid", "0", Field.Store.YES);
    Field junk = newStringField("junk", "", Field.Store.YES);
    doc.add(docid);
    doc.add(junk);
    
    int numDocs = 100;
    for (int i = 0; i < numDocs; i++) {
      docid.setStringValue("" + i);
      junk.setStringValue(TestUtil.randomUnicodeString(random));
      writer.addDocument(doc);
    }
    IndexReader reader = writer.getReader();
    writer.close();
    
    int numAsserts = atLeast(100);
    for (int i = 0; i < numAsserts; i++) {
      int docID = random.nextInt(numDocs);
      assertEquals("" + docID, reader.document(docID).get("docid"));
    }
    reader.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3057.java