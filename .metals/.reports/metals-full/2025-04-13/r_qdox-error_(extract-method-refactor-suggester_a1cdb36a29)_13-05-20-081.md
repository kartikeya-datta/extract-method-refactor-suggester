error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1433.java
text:
```scala
F@@ixedBitSet verify = bits.clone();

package org.apache.lucene.facet.old;

import java.io.IOException;
import java.util.Random;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.facet.FacetTestCase;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.FixedBitSet;
import org.junit.Test;

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

public class TestScoredDocIDsUtils extends FacetTestCase {

  @Test
  public void testComplementIterator() throws Exception {
    final int n = atLeast(10000);
    final FixedBitSet bits = new FixedBitSet(n);
    Random random = random();
    for (int i = 0; i < n; i++) {
      int idx = random.nextInt(n);
      bits.flip(idx, idx + 1);
    }
    
    FixedBitSet verify = new FixedBitSet(bits);

    ScoredDocIDs scoredDocIDs = ScoredDocIdsUtils.createScoredDocIds(bits, n); 

    Directory dir = newDirectory();
    IndexReader reader = createReaderWithNDocs(random, n, dir);
    try { 
      assertEquals(n - verify.cardinality(), ScoredDocIdsUtils.getComplementSet(scoredDocIDs, reader).size());
    } finally {
      reader.close();
      dir.close();
    }
  }

  @Test
  public void testAllDocs() throws Exception {
    int maxDoc = 3;
    Directory dir = newDirectory();
    IndexReader reader = createReaderWithNDocs(random(), maxDoc, dir);
    try {
      ScoredDocIDs all = ScoredDocIdsUtils.createAllDocsScoredDocIDs(reader);
      assertEquals("invalid size", maxDoc, all.size());
      ScoredDocIDsIterator iter = all.iterator();
      int doc = 0;
      while (iter.next()) {
        assertEquals("invalid doc ID: " + iter.getDocID(), doc++, iter.getDocID());
        assertEquals("invalid score: " + iter.getScore(), ScoredDocIDsIterator.DEFAULT_SCORE, iter.getScore(), 0.0f);
      }
      assertEquals("invalid maxDoc: " + doc, maxDoc, doc);
      
      DocIdSet docIDs = all.getDocIDs();
      assertTrue("should be cacheable", docIDs.isCacheable());
      DocIdSetIterator docIDsIter = docIDs.iterator();
      assertEquals("nextDoc() hasn't been called yet", -1, docIDsIter.docID());
      assertEquals(0, docIDsIter.nextDoc());
      assertEquals(1, docIDsIter.advance(1));
      // if advance is smaller than current doc, advance to cur+1.
      assertEquals(2, docIDsIter.advance(0));
    } finally {
      reader.close();
      dir.close();
    }
  }
  
  /**
   * Creates an index with n documents, this method is meant for testing purposes ONLY
   */
  static IndexReader createReaderWithNDocs(Random random, int nDocs, Directory directory) throws IOException {
    return createReaderWithNDocs(random, nDocs, new DocumentFactory(nDocs), directory);
  }

  private static class DocumentFactory {
    protected final static String field = "content";
    protected final static String delTxt = "delete";
    protected final static String alphaTxt = "alpha";
    
    private final static Field deletionMark = new StringField(field, delTxt, Field.Store.NO);
    private final static Field alphaContent = new StringField(field, alphaTxt, Field.Store.NO);
    
    public DocumentFactory(int totalNumDocs) {
    }
    
    public boolean markedDeleted(int docNum) {
      return false;
    }

    public Document getDoc(int docNum) {
      Document doc = new Document();
      if (markedDeleted(docNum)) {
        doc.add(deletionMark);
        // Add a special field for docs that are marked for deletion. Later we
        // assert that those docs are not returned by all-scored-doc-IDs.
        FieldType ft = new FieldType();
        ft.setStored(true);
        doc.add(new Field("del", Integer.toString(docNum), ft));
      }

      if (haveAlpha(docNum)) {
        doc.add(alphaContent);
      }
      return doc;
    }

    public boolean haveAlpha(int docNum) {
      return false;
    }
  }

  static IndexReader createReaderWithNDocs(Random random, int nDocs, DocumentFactory docFactory, Directory dir) throws IOException {
    RandomIndexWriter writer = new RandomIndexWriter(random, dir,
        newIndexWriterConfig(random, TEST_VERSION_CURRENT,
            new MockAnalyzer(random, MockTokenizer.KEYWORD, false)));
    for (int docNum = 0; docNum < nDocs; docNum++) {
      writer.addDocument(docFactory.getDoc(docNum));
    }
    // Delete documents marked for deletion
    writer.deleteDocuments(new Term(DocumentFactory.field, DocumentFactory.delTxt));
    writer.close();

    // Open a fresh read-only reader with the deletions in place
    return DirectoryReader.open(dir);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1433.java