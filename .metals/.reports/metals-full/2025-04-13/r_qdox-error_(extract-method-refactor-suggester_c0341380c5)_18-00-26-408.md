error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3107.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3107.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3107.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search;

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
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedSetDocValuesField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.UnicodeUtil;

/**
 * Tests the DocTermOrdsRangeFilter
 */
public class TestDocTermOrdsRangeFilter extends LuceneTestCase {
  protected IndexSearcher searcher1;
  protected IndexSearcher searcher2;
  private IndexReader reader;
  private Directory dir;
  protected String fieldName;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    dir = newDirectory();
    fieldName = random().nextBoolean() ? "field" : ""; // sometimes use an empty string as field name
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir, 
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random(), MockTokenizer.KEYWORD, false))
        .setMaxBufferedDocs(TestUtil.nextInt(random(), 50, 1000)));
    List<String> terms = new ArrayList<>();
    int num = atLeast(200);
    for (int i = 0; i < num; i++) {
      Document doc = new Document();
      doc.add(newStringField("id", Integer.toString(i), Field.Store.NO));
      int numTerms = random().nextInt(4);
      for (int j = 0; j < numTerms; j++) {
        String s = TestUtil.randomUnicodeString(random());
        doc.add(newStringField(fieldName, s, Field.Store.NO));
        // if the default codec doesn't support sortedset, we will uninvert at search time
        if (defaultCodecSupportsSortedSet()) {
          doc.add(new SortedSetDocValuesField(fieldName, new BytesRef(s)));
        }
        terms.add(s);
      }
      writer.addDocument(doc);
    }
    
    if (VERBOSE) {
      // utf16 order
      Collections.sort(terms);
      System.out.println("UTF16 order:");
      for(String s : terms) {
        System.out.println("  " + UnicodeUtil.toHexString(s));
      }
    }
    
    int numDeletions = random().nextInt(num/10);
    for (int i = 0; i < numDeletions; i++) {
      writer.deleteDocuments(new Term("id", Integer.toString(random().nextInt(num))));
    }
    
    reader = writer.getReader();
    searcher1 = newSearcher(reader);
    searcher2 = newSearcher(reader);
    writer.close();
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    super.tearDown();
  }
  
  /** test a bunch of random ranges */
  public void testRanges() throws Exception {
    int num = atLeast(1000);
    for (int i = 0; i < num; i++) {
      BytesRef lowerVal = new BytesRef(TestUtil.randomUnicodeString(random()));
      BytesRef upperVal = new BytesRef(TestUtil.randomUnicodeString(random()));
      if (upperVal.compareTo(lowerVal) < 0) {
        assertSame(upperVal, lowerVal, random().nextBoolean(), random().nextBoolean());
      } else {
        assertSame(lowerVal, upperVal, random().nextBoolean(), random().nextBoolean());
      }
    }
  }
  
  /** check that the # of hits is the same as if the query
   * is run against the inverted index
   */
  protected void assertSame(BytesRef lowerVal, BytesRef upperVal, boolean includeLower, boolean includeUpper) throws IOException {   
    Query docValues = new ConstantScoreQuery(DocTermOrdsRangeFilter.newBytesRefRange(fieldName, lowerVal, upperVal, includeLower, includeUpper));
    MultiTermQuery inverted = new TermRangeQuery(fieldName, lowerVal, upperVal, includeLower, includeUpper);
    inverted.setRewriteMethod(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
   
    TopDocs invertedDocs = searcher1.search(inverted, 25);
    TopDocs docValuesDocs = searcher2.search(docValues, 25);

    CheckHits.checkEqual(inverted, invertedDocs.scoreDocs, docValuesDocs.scoreDocs);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3107.java