error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3154.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3154.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3154.java
text:
```scala
w@@riter.shutdown();

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

public class TestSegmentTermDocs extends LuceneTestCase {
  private Document testDoc = new Document();
  private Directory dir;
  private SegmentCommitInfo info;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    DocHelper.setupDoc(testDoc);
    info = DocHelper.writeDoc(random(), dir, testDoc);
  }
  
  @Override
  public void tearDown() throws Exception {
    dir.close();
    super.tearDown();
  }

  public void test() {
    assertTrue(dir != null);
  }

  public void testTermDocs() throws IOException {
    //After adding the document, we should be able to read it back in
    SegmentReader reader = new SegmentReader(info, newIOContext(random()));
    assertTrue(reader != null);

    TermsEnum terms = reader.fields().terms(DocHelper.TEXT_FIELD_2_KEY).iterator(null);
    terms.seekCeil(new BytesRef("field"));
    DocsEnum termDocs = TestUtil.docs(random(), terms, reader.getLiveDocs(), null, DocsEnum.FLAG_FREQS);
    if (termDocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS)    {
      int docId = termDocs.docID();
      assertTrue(docId == 0);
      int freq = termDocs.freq();
      assertTrue(freq == 3);  
    }
    reader.close();
  }  
  
  public void testBadSeek() throws IOException {
    {
      //After adding the document, we should be able to read it back in
      SegmentReader reader = new SegmentReader(info, newIOContext(random()));
      assertTrue(reader != null);
      DocsEnum termDocs = TestUtil.docs(random(), reader,
          "textField2",
          new BytesRef("bad"),
          reader.getLiveDocs(),
          null,
          0);

      assertNull(termDocs);
      reader.close();
    }
    {
      //After adding the document, we should be able to read it back in
      SegmentReader reader = new SegmentReader(info, newIOContext(random()));
      assertTrue(reader != null);
      DocsEnum termDocs = TestUtil.docs(random(), reader,
          "junk",
          new BytesRef("bad"),
          reader.getLiveDocs(),
          null,
          0);
      assertNull(termDocs);
      reader.close();
    }
  }
  
  public void testSkipTo() throws IOException {
    Directory dir = newDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
    
    Term ta = new Term("content","aaa");
    for(int i = 0; i < 10; i++)
      addDoc(writer, "aaa aaa aaa aaa");
      
    Term tb = new Term("content","bbb");
    for(int i = 0; i < 16; i++)
      addDoc(writer, "bbb bbb bbb bbb");
      
    Term tc = new Term("content","ccc");
    for(int i = 0; i < 50; i++)
      addDoc(writer, "ccc ccc ccc ccc");
      
    // assure that we deal with a single segment  
    writer.forceMerge(1);
    writer.close();
    
    IndexReader reader = DirectoryReader.open(dir);

    DocsEnum tdocs = TestUtil.docs(random(), reader,
        ta.field(),
        new BytesRef(ta.text()),
        MultiFields.getLiveDocs(reader),
        null,
        DocsEnum.FLAG_FREQS);
    
    // without optimization (assumption skipInterval == 16)
    
    // with next
    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(0, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(1, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.advance(2) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(2, tdocs.docID());
    assertTrue(tdocs.advance(4) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(4, tdocs.docID());
    assertTrue(tdocs.advance(9) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(9, tdocs.docID());
    assertFalse(tdocs.advance(10) != DocIdSetIterator.NO_MORE_DOCS);
    
    // without next
    tdocs = TestUtil.docs(random(), reader,
        ta.field(),
        new BytesRef(ta.text()),
        MultiFields.getLiveDocs(reader),
        null,
        0);
    
    assertTrue(tdocs.advance(0) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(0, tdocs.docID());
    assertTrue(tdocs.advance(4) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(4, tdocs.docID());
    assertTrue(tdocs.advance(9) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(9, tdocs.docID());
    assertFalse(tdocs.advance(10) != DocIdSetIterator.NO_MORE_DOCS);
    
    // exactly skipInterval documents and therefore with optimization
    
    // with next
    tdocs = TestUtil.docs(random(), reader,
        tb.field(),
        new BytesRef(tb.text()),
        MultiFields.getLiveDocs(reader),
        null,
        DocsEnum.FLAG_FREQS);

    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(10, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(11, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.advance(12) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(12, tdocs.docID());
    assertTrue(tdocs.advance(15) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(15, tdocs.docID());
    assertTrue(tdocs.advance(24) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(24, tdocs.docID());
    assertTrue(tdocs.advance(25) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(25, tdocs.docID());
    assertFalse(tdocs.advance(26) != DocIdSetIterator.NO_MORE_DOCS);
    
    // without next
    tdocs = TestUtil.docs(random(), reader,
        tb.field(),
        new BytesRef(tb.text()),
        MultiFields.getLiveDocs(reader),
        null,
        DocsEnum.FLAG_FREQS);
    
    assertTrue(tdocs.advance(5) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(10, tdocs.docID());
    assertTrue(tdocs.advance(15) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(15, tdocs.docID());
    assertTrue(tdocs.advance(24) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(24, tdocs.docID());
    assertTrue(tdocs.advance(25) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(25, tdocs.docID());
    assertFalse(tdocs.advance(26) != DocIdSetIterator.NO_MORE_DOCS);
    
    // much more than skipInterval documents and therefore with optimization
    
    // with next
    tdocs = TestUtil.docs(random(), reader,
        tc.field(),
        new BytesRef(tc.text()),
        MultiFields.getLiveDocs(reader),
        null,
        DocsEnum.FLAG_FREQS);

    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(26, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(27, tdocs.docID());
    assertEquals(4, tdocs.freq());
    assertTrue(tdocs.advance(28) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(28, tdocs.docID());
    assertTrue(tdocs.advance(40) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(40, tdocs.docID());
    assertTrue(tdocs.advance(57) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(57, tdocs.docID());
    assertTrue(tdocs.advance(74) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(74, tdocs.docID());
    assertTrue(tdocs.advance(75) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(75, tdocs.docID());
    assertFalse(tdocs.advance(76) != DocIdSetIterator.NO_MORE_DOCS);
    
    //without next
    tdocs = TestUtil.docs(random(), reader,
        tc.field(),
        new BytesRef(tc.text()),
        MultiFields.getLiveDocs(reader),
        null,
        0);
    assertTrue(tdocs.advance(5) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(26, tdocs.docID());
    assertTrue(tdocs.advance(40) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(40, tdocs.docID());
    assertTrue(tdocs.advance(57) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(57, tdocs.docID());
    assertTrue(tdocs.advance(74) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(74, tdocs.docID());
    assertTrue(tdocs.advance(75) != DocIdSetIterator.NO_MORE_DOCS);
    assertEquals(75, tdocs.docID());
    assertFalse(tdocs.advance(76) != DocIdSetIterator.NO_MORE_DOCS);
    
    reader.close();
    dir.close();
  }


  private void addDoc(IndexWriter writer, String value) throws IOException
  {
      Document doc = new Document();
      doc.add(newTextField("content", value, Field.Store.NO));
      writer.addDocument(doc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3154.java