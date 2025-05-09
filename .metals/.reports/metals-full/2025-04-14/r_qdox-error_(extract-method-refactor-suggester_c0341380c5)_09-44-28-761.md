error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3085.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3085.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3085.java
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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestTopDocsCollector extends LuceneTestCase {

  private static final class MyTopsDocCollector extends TopDocsCollector<ScoreDoc> {

    private int idx = 0;
    private int base = 0;
    
    public MyTopsDocCollector(int size) {
      super(new HitQueue(size, false));
    }
    
    @Override
    protected TopDocs newTopDocs(ScoreDoc[] results, int start) {
      if (results == null) {
        return EMPTY_TOPDOCS;
      }
      
      float maxScore = Float.NaN;
      if (start == 0) {
        maxScore = results[0].score;
      } else {
        for (int i = pq.size(); i > 1; i--) { pq.pop(); }
        maxScore = pq.pop().score;
      }
      
      return new TopDocs(totalHits, results, maxScore);
    }
    
    @Override
    public void collect(int doc) {
      ++totalHits;
      pq.insertWithOverflow(new ScoreDoc(doc + base, scores[idx++]));
    }

    @Override
    protected void doSetNextReader(AtomicReaderContext context) throws IOException {
      base = context.docBase;
    }

    @Override
    public void setScorer(Scorer scorer) {
      // Don't do anything. Assign scores in random
    }
    
    @Override
    public boolean acceptsDocsOutOfOrder() {
      return true;
    }

  }

  // Scores array to be used by MyTopDocsCollector. If it is changed, MAX_SCORE
  // must also change.
  private static final float[] scores = new float[] {
    0.7767749f, 1.7839992f, 8.9925785f, 7.9608946f, 0.07948637f, 2.6356435f, 
    7.4950366f, 7.1490803f, 8.108544f, 4.961808f, 2.2423935f, 7.285586f, 4.6699767f,
    2.9655676f, 6.953706f, 5.383931f, 6.9916306f, 8.365894f, 7.888485f, 8.723962f,
    3.1796896f, 0.39971232f, 1.3077754f, 6.8489285f, 9.17561f, 5.060466f, 7.9793315f,
    8.601509f, 4.1858315f, 0.28146625f
  };
  
  private static final float MAX_SCORE = 9.17561f;
  
  private Directory dir;
  private IndexReader reader;

  private TopDocsCollector<ScoreDoc> doSearch(int numResults) throws IOException {
    Query q = new MatchAllDocsQuery();
    IndexSearcher searcher = newSearcher(reader);
    TopDocsCollector<ScoreDoc> tdc = new MyTopsDocCollector(numResults);
    searcher.search(q, tdc);
    return tdc;
  }
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    
    // populate an index with 30 documents, this should be enough for the test.
    // The documents have no content - the test uses MatchAllDocsQuery().
    dir = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir);
    for (int i = 0; i < 30; i++) {
      writer.addDocument(new Document());
    }
    reader = writer.getReader();
    writer.close();
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    dir = null;
    super.tearDown();
  }
  
  public void testInvalidArguments() throws Exception {
    int numResults = 5;
    TopDocsCollector<ScoreDoc> tdc = doSearch(numResults);
    
    // start < 0
    assertEquals(0, tdc.topDocs(-1).scoreDocs.length);
    
    // start > pq.size()
    assertEquals(0, tdc.topDocs(numResults + 1).scoreDocs.length);
    
    // start == pq.size()
    assertEquals(0, tdc.topDocs(numResults).scoreDocs.length);
    
    // howMany < 0
    assertEquals(0, tdc.topDocs(0, -1).scoreDocs.length);
    
    // howMany == 0
    assertEquals(0, tdc.topDocs(0, 0).scoreDocs.length);
    
  }
  
  public void testZeroResults() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = new MyTopsDocCollector(5);
    assertEquals(0, tdc.topDocs(0, 1).scoreDocs.length);
  }
  
  public void testFirstResultsPage() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    assertEquals(10, tdc.topDocs(0, 10).scoreDocs.length);
  }
  
  public void testSecondResultsPages() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    // ask for more results than are available
    assertEquals(5, tdc.topDocs(10, 10).scoreDocs.length);
    
    // ask for 5 results (exactly what there should be
    tdc = doSearch(15);
    assertEquals(5, tdc.topDocs(10, 5).scoreDocs.length);
    
    // ask for less results than there are
    tdc = doSearch(15);
    assertEquals(4, tdc.topDocs(10, 4).scoreDocs.length);
  }
  
  public void testGetAllResults() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    assertEquals(15, tdc.topDocs().scoreDocs.length);
  }
  
  public void testGetResultsFromStart() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    // should bring all results
    assertEquals(15, tdc.topDocs(0).scoreDocs.length);
    
    tdc = doSearch(15);
    // get the last 5 only.
    assertEquals(5, tdc.topDocs(10).scoreDocs.length);
  }
  
  public void testMaxScore() throws Exception {
    // ask for all results
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    TopDocs td = tdc.topDocs();
    assertEquals(MAX_SCORE, td.getMaxScore(), 0f);
    
    // ask for 5 last results
    tdc = doSearch(15);
    td = tdc.topDocs(10);
    assertEquals(MAX_SCORE, td.getMaxScore(), 0f);
  }
  
  // This does not test the PQ's correctness, but whether topDocs()
  // implementations return the results in decreasing score order.
  public void testResultsOrder() throws Exception {
    TopDocsCollector<ScoreDoc> tdc = doSearch(15);
    ScoreDoc[] sd = tdc.topDocs().scoreDocs;
    
    assertEquals(MAX_SCORE, sd[0].score, 0f);
    for (int i = 1; i < sd.length; i++) {
      assertTrue(sd[i - 1].score >= sd[i].score);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3085.java