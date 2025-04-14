error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3058.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3058.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3058.java
text:
```scala
w@@.shutdown();

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LineFileDocs;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

public class TestNRTCachingDirectory extends LuceneTestCase {

  public void testNRTAndCommit() throws Exception {
    Directory dir = newDirectory();
    NRTCachingDirectory cachedDir = new NRTCachingDirectory(dir, 2.0, 25.0);
    MockAnalyzer analyzer = new MockAnalyzer(random());
    analyzer.setMaxTokenLength(TestUtil.nextInt(random(), 1, IndexWriter.MAX_TERM_LENGTH));
    IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer);
    RandomIndexWriter w = new RandomIndexWriter(random(), cachedDir, conf);
    final LineFileDocs docs = new LineFileDocs(random(), true);
    final int numDocs = TestUtil.nextInt(random(), 100, 400);

    if (VERBOSE) {
      System.out.println("TEST: numDocs=" + numDocs);
    }

    final List<BytesRef> ids = new ArrayList<>();
    DirectoryReader r = null;
    for(int docCount=0;docCount<numDocs;docCount++) {
      final Document doc = docs.nextDoc();
      ids.add(new BytesRef(doc.get("docid")));
      w.addDocument(doc);
      if (random().nextInt(20) == 17) {
        if (r == null) {
          r = DirectoryReader.open(w.w, false);
        } else {
          final DirectoryReader r2 = DirectoryReader.openIfChanged(r);
          if (r2 != null) {
            r.close();
            r = r2;
          }
        }
        assertEquals(1+docCount, r.numDocs());
        final IndexSearcher s = newSearcher(r);
        // Just make sure search can run; we can't assert
        // totHits since it could be 0
        TopDocs hits = s.search(new TermQuery(new Term("body", "the")), 10);
        // System.out.println("tot hits " + hits.totalHits);
      }
    }

    if (r != null) {
      r.close();
    }

    // Close should force cache to clear since all files are sync'd
    w.close();

    final String[] cachedFiles = cachedDir.listCachedFiles();
    for(String file : cachedFiles) {
      System.out.println("FAIL: cached file " + file + " remains after sync");
    }
    assertEquals(0, cachedFiles.length);
    
    r = DirectoryReader.open(dir);
    for(BytesRef id : ids) {
      assertEquals(1, r.docFreq(new Term("docid", id)));
    }
    r.close();
    cachedDir.close();
    docs.close();
  }

  // NOTE: not a test; just here to make sure the code frag
  // in the javadocs is correct!
  public void verifyCompiles() throws Exception {
    Analyzer analyzer = null;

    Directory fsDir = FSDirectory.open(new File("/path/to/index"));
    NRTCachingDirectory cachedFSDir = new NRTCachingDirectory(fsDir, 2.0, 25.0);
    IndexWriterConfig conf = new IndexWriterConfig(TEST_VERSION_CURRENT, analyzer);
    IndexWriter writer = new IndexWriter(cachedFSDir, conf);
  }

  public void testDeleteFile() throws Exception {
    Directory dir = new NRTCachingDirectory(newDirectory(), 2.0, 25.0);
    dir.createOutput("foo.txt", IOContext.DEFAULT).close();
    dir.deleteFile("foo.txt");
    assertEquals(0, dir.listAll().length);
    dir.close();
  }
  
  // LUCENE-3382 -- make sure we get exception if the directory really does not exist.
  public void testNoDir() throws Throwable {
    File tempDir = createTempDir("doesnotexist");
    TestUtil.rm(tempDir);
    Directory dir = new NRTCachingDirectory(newFSDirectory(tempDir), 2.0, 25.0);
    try {
      DirectoryReader.open(dir);
      fail("did not hit expected exception");
    } catch (NoSuchDirectoryException nsde) {
      // expected
    }
    dir.close();
  }
  
  // LUCENE-3382 test that we can add a file, and then when we call list() we get it back
  public void testDirectoryFilter() throws IOException {
    Directory dir = new NRTCachingDirectory(newFSDirectory(createTempDir("foo")), 2.0, 25.0);
    String name = "file";
    try {
      dir.createOutput(name, newIOContext(random())).close();
      assertTrue(slowFileExists(dir, name));
      assertTrue(Arrays.asList(dir.listAll()).contains(name));
    } finally {
      dir.close();
    }
  }
  
  // LUCENE-3382 test that delegate compound files correctly.
  public void testCompoundFileAppendTwice() throws IOException {
    Directory newDir = new NRTCachingDirectory(newDirectory(), 2.0, 25.0);
    CompoundFileDirectory csw = new CompoundFileDirectory(newDir, "d.cfs", newIOContext(random()), true);
    createSequenceFile(newDir, "d1", (byte) 0, 15);
    IndexOutput out = csw.createOutput("d.xyz", newIOContext(random()));
    out.writeInt(0);
    out.close();
    assertEquals(1, csw.listAll().length);
    assertEquals("d.xyz", csw.listAll()[0]);
   
    csw.close();

    CompoundFileDirectory cfr = new CompoundFileDirectory(newDir, "d.cfs", newIOContext(random()), false);
    assertEquals(1, cfr.listAll().length);
    assertEquals("d.xyz", cfr.listAll()[0]);
    cfr.close();
    newDir.close();
  }
  
  /** Creates a file of the specified size with sequential data. The first
   *  byte is written as the start byte provided. All subsequent bytes are
   *  computed as start + offset where offset is the number of the byte.
   */
  private void createSequenceFile(Directory dir, String name, byte start, int size) throws IOException {
      IndexOutput os = dir.createOutput(name, newIOContext(random()));
      for (int i=0; i < size; i++) {
          os.writeByte(start);
          start ++;
      }
      os.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3058.java