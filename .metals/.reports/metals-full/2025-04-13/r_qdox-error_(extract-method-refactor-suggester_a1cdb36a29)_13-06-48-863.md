error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3079.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3079.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3079.java
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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CheckHits;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestNearSpansOrdered extends LuceneTestCase {
  protected IndexSearcher searcher;
  protected Directory directory;
  protected IndexReader reader;

  public static final String FIELD = "field";

  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter writer= new RandomIndexWriter(random(), directory, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
    for (int i = 0; i < docFields.length; i++) {
      Document doc = new Document();
      doc.add(newTextField(FIELD, docFields[i], Field.Store.NO));
      writer.addDocument(doc);
    }
    reader = writer.getReader();
    writer.close();
    searcher = newSearcher(reader);
  }

  protected String[] docFields = {
    "w1 w2 w3 w4 w5",
    "w1 w3 w2 w3 zz",
    "w1 xx w2 yy w3",
    "w1 w3 xx w2 yy w3 zz"
  };

  protected SpanNearQuery makeQuery(String s1, String s2, String s3,
                                    int slop, boolean inOrder) {
    return new SpanNearQuery
      (new SpanQuery[] {
        new SpanTermQuery(new Term(FIELD, s1)),
        new SpanTermQuery(new Term(FIELD, s2)),
        new SpanTermQuery(new Term(FIELD, s3)) },
       slop,
       inOrder);
  }
  protected SpanNearQuery makeQuery() {
    return makeQuery("w1","w2","w3",1,true);
  }
  
  public void testSpanNearQuery() throws Exception {
    SpanNearQuery q = makeQuery();
    CheckHits.checkHits(random(), q, FIELD, searcher, new int[] {0,1});
  }

  public String s(Spans span) {
    return s(span.doc(), span.start(), span.end());
  }
  public String s(int doc, int start, int end) {
    return "s(" + doc + "," + start + "," + end +")";
  }
  
  public void testNearSpansNext() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span = MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.next());
    assertEquals(s(0,0,3), s(span));
    assertEquals(true, span.next());
    assertEquals(s(1,0,4), s(span));
    assertEquals(false, span.next());
  }

  /**
   * test does not imply that skipTo(doc+1) should work exactly the
   * same as next -- it's only applicable in this case since we know doc
   * does not contain more than one span
   */
  public void testNearSpansSkipToLikeNext() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span =  MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.skipTo(0));
    assertEquals(s(0,0,3), s(span));
    assertEquals(true, span.skipTo(1));
    assertEquals(s(1,0,4), s(span));
    assertEquals(false, span.skipTo(2));
  }
  
  public void testNearSpansNextThenSkipTo() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span =  MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.next());
    assertEquals(s(0,0,3), s(span));
    assertEquals(true, span.skipTo(1));
    assertEquals(s(1,0,4), s(span));
    assertEquals(false, span.next());
  }
  
  public void testNearSpansNextThenSkipPast() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span =  MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.next());
    assertEquals(s(0,0,3), s(span));
    assertEquals(false, span.skipTo(2));
  }
  
  public void testNearSpansSkipPast() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span =  MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(false, span.skipTo(2));
  }
  
  public void testNearSpansSkipTo0() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span = MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.skipTo(0));
    assertEquals(s(0,0,3), s(span));
  }

  public void testNearSpansSkipTo1() throws Exception {
    SpanNearQuery q = makeQuery();
    Spans span =  MultiSpansWrapper.wrap(searcher.getTopReaderContext(), q);
    assertEquals(true, span.skipTo(1));
    assertEquals(s(1,0,4), s(span));
  }

  /**
   * not a direct test of NearSpans, but a demonstration of how/when
   * this causes problems
   */
  public void testSpanNearScorerSkipTo1() throws Exception {
    SpanNearQuery q = makeQuery();
    Weight w = searcher.createNormalizedWeight(q);
    IndexReaderContext topReaderContext = searcher.getTopReaderContext();
    AtomicReaderContext leave = topReaderContext.leaves().get(0);
    Scorer s = w.scorer(leave, leave.reader().getLiveDocs());
    assertEquals(1, s.advance(1));
  }
  
  /**
   * not a direct test of NearSpans, but a demonstration of how/when
   * this causes problems
   */
  public void testSpanNearScorerExplain() throws Exception {
    SpanNearQuery q = makeQuery();
    Explanation e = searcher.explain(q, 1);
    assertTrue("Scorer explanation value for doc#1 isn't positive: "
               + e.toString(),
               0.0f < e.getValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3079.java