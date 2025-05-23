error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/431.java
text:
```scala
public v@@oid setScorer(Scorer scorer) {

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
import java.util.List;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestTermScorer extends LuceneTestCase {
  protected Directory directory;
  private static final String FIELD = "field";
  
  protected String[] values = new String[] {"all", "dogs dogs", "like",
      "playing", "fetch", "all"};
  protected IndexSearcher indexSearcher;
  protected IndexReader indexReader;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory, 
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
        .setMergePolicy(newLogMergePolicy())
        .setSimilarity(new DefaultSimilarity()));
    for (int i = 0; i < values.length; i++) {
      Document doc = new Document();
      doc
          .add(newTextField(FIELD, values[i], Field.Store.YES));
      writer.addDocument(doc);
    }
    indexReader = SlowCompositeReaderWrapper.wrap(writer.getReader());
    writer.close();
    indexSearcher = newSearcher(indexReader);
    indexSearcher.setSimilarity(new DefaultSimilarity());
  }
  
  @Override
  public void tearDown() throws Exception {
    indexReader.close();
    directory.close();
    super.tearDown();
  }

  public void test() throws IOException {
    
    Term allTerm = new Term(FIELD, "all");
    TermQuery termQuery = new TermQuery(allTerm);
    
    Weight weight = indexSearcher.createNormalizedWeight(termQuery);
    assertTrue(indexSearcher.getTopReaderContext() instanceof AtomicReaderContext);
    AtomicReaderContext context = (AtomicReaderContext)indexSearcher.getTopReaderContext();
    Scorer ts = weight.scorer(context, true, true, context.reader().getLiveDocs());
    // we have 2 documents with the term all in them, one document for all the
    // other values
    final List<TestHit> docs = new ArrayList<TestHit>();
    // must call next first
    
    ts.score(new Collector() {
      private int base = 0;
      private Scorer scorer;
      
      @Override
      public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
      }
      
      @Override
      public void collect(int doc) throws IOException {
        float score = scorer.score();
        doc = doc + base;
        docs.add(new TestHit(doc, score));
        assertTrue("score " + score + " is not greater than 0", score > 0);
        assertTrue("Doc: " + doc + " does not equal 0 or doc does not equal 5",
            doc == 0 || doc == 5);
      }
      
      @Override
      public void setNextReader(AtomicReaderContext context) {
        base = context.docBase;
      }
      
      @Override
      public boolean acceptsDocsOutOfOrder() {
        return true;
      }
    });
    assertTrue("docs Size: " + docs.size() + " is not: " + 2, docs.size() == 2);
    TestHit doc0 = docs.get(0);
    TestHit doc5 = docs.get(1);
    // The scores should be the same
    assertTrue(doc0.score + " does not equal: " + doc5.score,
        doc0.score == doc5.score);
    /*
     * Score should be (based on Default Sim.: All floats are approximate tf = 1
     * numDocs = 6 docFreq(all) = 2 idf = ln(6/3) + 1 = 1.693147 idf ^ 2 =
     * 2.8667 boost = 1 lengthNorm = 1 //there is 1 term in every document coord
     * = 1 sumOfSquaredWeights = (idf * boost) ^ 2 = 1.693147 ^ 2 = 2.8667
     * queryNorm = 1 / (sumOfSquaredWeights)^0.5 = 1 /(1.693147) = 0.590
     * 
     * score = 1 * 2.8667 * 1 * 1 * 0.590 = 1.69
     */
    assertTrue(doc0.score + " does not equal: " + 1.6931472f,
        doc0.score == 1.6931472f);
  }
  
  public void testNext() throws Exception {
    
    Term allTerm = new Term(FIELD, "all");
    TermQuery termQuery = new TermQuery(allTerm);
    
    Weight weight = indexSearcher.createNormalizedWeight(termQuery);
    assertTrue(indexSearcher.getTopReaderContext() instanceof AtomicReaderContext);
    AtomicReaderContext context = (AtomicReaderContext) indexSearcher.getTopReaderContext();
    Scorer ts = weight.scorer(context, true, true, context.reader().getLiveDocs());
    assertTrue("next did not return a doc",
        ts.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertTrue("score is not correct", ts.score() == 1.6931472f);
    assertTrue("next did not return a doc",
        ts.nextDoc() != DocIdSetIterator.NO_MORE_DOCS);
    assertTrue("score is not correct", ts.score() == 1.6931472f);
    assertTrue("next returned a doc and it should not have",
        ts.nextDoc() == DocIdSetIterator.NO_MORE_DOCS);
  }
  
  public void testAdvance() throws Exception {
    
    Term allTerm = new Term(FIELD, "all");
    TermQuery termQuery = new TermQuery(allTerm);
    
    Weight weight = indexSearcher.createNormalizedWeight(termQuery);
    assertTrue(indexSearcher.getTopReaderContext() instanceof AtomicReaderContext);
    AtomicReaderContext context = (AtomicReaderContext) indexSearcher.getTopReaderContext();
    Scorer ts = weight.scorer(context, true, true, context.reader().getLiveDocs());
    assertTrue("Didn't skip", ts.advance(3) != DocIdSetIterator.NO_MORE_DOCS);
    // The next doc should be doc 5
    assertTrue("doc should be number 5", ts.docID() == 5);
  }
  
  private class TestHit {
    public int doc;
    public float score;
    
    public TestHit(int doc, float score) {
      this.doc = doc;
      this.score = score;
    }
    
    @Override
    public String toString() {
      return "TestHit{" + "doc=" + doc + ", score=" + score + "}";
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/431.java