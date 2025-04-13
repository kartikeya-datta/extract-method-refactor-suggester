error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3078.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3078.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3078.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.search.spans;

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
import org.apache.lucene.analysis.MockTokenFilter;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.DefaultSimilarity;

/*******************************************************************************
 * Some expanded tests to make sure my patch doesn't break other SpanTermQuery
 * functionality.
 * 
 */
public class TestSpansAdvanced2 extends TestSpansAdvanced {
  IndexSearcher searcher2;
  IndexReader reader2;
  
  /**
   * Initializes the tests by adding documents to the index.
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
    
    // create test index
    final RandomIndexWriter writer = new RandomIndexWriter(random(), mDirectory,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random(),
            MockTokenizer.SIMPLE, true, MockTokenFilter.ENGLISH_STOPSET))
            .setOpenMode(OpenMode.APPEND).setMergePolicy(newLogMergePolicy())
            .setSimilarity(new DefaultSimilarity()));
    addDocument(writer, "A", "Should we, could we, would we?");
    addDocument(writer, "B", "It should.  Should it?");
    addDocument(writer, "C", "It shouldn't.");
    addDocument(writer, "D", "Should we, should we, should we.");
    reader2 = writer.getReader();
    writer.close();
    
    // re-open the searcher since we added more docs
    searcher2 = newSearcher(reader2);
    searcher2.setSimilarity(new DefaultSimilarity());
  }
  
  @Override
  public void tearDown() throws Exception {
    reader2.close();
    super.tearDown();
  }
  
  /**
   * Verifies that the index has the correct number of documents.
   */
  public void testVerifyIndex() throws Exception {
    final IndexReader reader = DirectoryReader.open(mDirectory);
    assertEquals(8, reader.numDocs());
    reader.close();
  }
  
  /**
   * Tests a single span query that matches multiple documents.
   */
  public void testSingleSpanQuery() throws IOException {
    
    final Query spanQuery = new SpanTermQuery(new Term(FIELD_TEXT, "should"));
    final String[] expectedIds = new String[] {"B", "D", "1", "2", "3", "4",
        "A"};
    final float[] expectedScores = new float[] {0.625f, 0.45927936f,
        0.35355338f, 0.35355338f, 0.35355338f, 0.35355338f, 0.26516503f,};
    assertHits(searcher2, spanQuery, "single span query", expectedIds,
        expectedScores);
  }
  
  /**
   * Tests a single span query that matches multiple documents.
   */
  public void testMultipleDifferentSpanQueries() throws IOException {
    
    final Query spanQuery1 = new SpanTermQuery(new Term(FIELD_TEXT, "should"));
    final Query spanQuery2 = new SpanTermQuery(new Term(FIELD_TEXT, "we"));
    final BooleanQuery query = new BooleanQuery();
    query.add(spanQuery1, BooleanClause.Occur.MUST);
    query.add(spanQuery2, BooleanClause.Occur.MUST);
    final String[] expectedIds = new String[] {"D", "A"};
    // these values were pre LUCENE-413
    // final float[] expectedScores = new float[] { 0.93163157f, 0.20698164f };
    final float[] expectedScores = new float[] {1.0191123f, 0.93163157f};
    assertHits(searcher2, query, "multiple different span queries",
        expectedIds, expectedScores);
  }
  
  /**
   * Tests two span queries.
   */
  @Override
  public void testBooleanQueryWithSpanQueries() throws IOException {
    
    doTestBooleanQueryWithSpanQueries(searcher2, 0.73500174f);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3078.java