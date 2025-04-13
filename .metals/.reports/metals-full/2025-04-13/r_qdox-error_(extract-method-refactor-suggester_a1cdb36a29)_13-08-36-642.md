error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3081.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3081.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3081.java
text:
```scala
i@@w.shutdown();

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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;

/**
 * Tests the use of indexdocvalues in scoring.
 * 
 * In the example, a docvalues field is used as a per-document boost (separate from the norm)
 * @lucene.experimental
 */
public class TestDocValuesScoring extends LuceneTestCase {
  private static final float SCORE_EPSILON = 0.001f; /* for comparing floats */

  public void testSimple() throws Exception {    
    Directory dir = newDirectory();
    RandomIndexWriter iw = new RandomIndexWriter(random(), dir);
    Document doc = new Document();
    Field field = newTextField("foo", "", Field.Store.NO);
    doc.add(field);
    Field dvField = new FloatDocValuesField("foo_boost", 0.0F);
    doc.add(dvField);
    Field field2 = newTextField("bar", "", Field.Store.NO);
    doc.add(field2);
    
    field.setStringValue("quick brown fox");
    field2.setStringValue("quick brown fox");
    dvField.setFloatValue(2f); // boost x2
    iw.addDocument(doc);
    field.setStringValue("jumps over lazy brown dog");
    field2.setStringValue("jumps over lazy brown dog");
    dvField.setFloatValue(4f); // boost x4
    iw.addDocument(doc);
    IndexReader ir = iw.getReader();
    iw.close();
    
    // no boosting
    IndexSearcher searcher1 = newSearcher(ir, false);
    final Similarity base = searcher1.getSimilarity();
    // boosting
    IndexSearcher searcher2 = newSearcher(ir, false);
    searcher2.setSimilarity(new PerFieldSimilarityWrapper() {
      final Similarity fooSim = new BoostingSimilarity(base, "foo_boost");

      @Override
      public Similarity get(String field) {
        return "foo".equals(field) ? fooSim : base;
      }

      @Override
      public float coord(int overlap, int maxOverlap) {
        return base.coord(overlap, maxOverlap);
      }

      @Override
      public float queryNorm(float sumOfSquaredWeights) {
        return base.queryNorm(sumOfSquaredWeights);
      }
    });
    
    // in this case, we searched on field "foo". first document should have 2x the score.
    TermQuery tq = new TermQuery(new Term("foo", "quick"));
    QueryUtils.check(random(), tq, searcher1);
    QueryUtils.check(random(), tq, searcher2);
    
    TopDocs noboost = searcher1.search(tq, 10);
    TopDocs boost = searcher2.search(tq, 10);
    assertEquals(1, noboost.totalHits);
    assertEquals(1, boost.totalHits);
    
    //System.out.println(searcher2.explain(tq, boost.scoreDocs[0].doc));
    assertEquals(boost.scoreDocs[0].score, noboost.scoreDocs[0].score*2f, SCORE_EPSILON);
    
    // this query matches only the second document, which should have 4x the score.
    tq = new TermQuery(new Term("foo", "jumps"));
    QueryUtils.check(random(), tq, searcher1);
    QueryUtils.check(random(), tq, searcher2);
    
    noboost = searcher1.search(tq, 10);
    boost = searcher2.search(tq, 10);
    assertEquals(1, noboost.totalHits);
    assertEquals(1, boost.totalHits);
    
    assertEquals(boost.scoreDocs[0].score, noboost.scoreDocs[0].score*4f, SCORE_EPSILON);
    
    // search on on field bar just for kicks, nothing should happen, since we setup
    // our sim provider to only use foo_boost for field foo.
    tq = new TermQuery(new Term("bar", "quick"));
    QueryUtils.check(random(), tq, searcher1);
    QueryUtils.check(random(), tq, searcher2);
    
    noboost = searcher1.search(tq, 10);
    boost = searcher2.search(tq, 10);
    assertEquals(1, noboost.totalHits);
    assertEquals(1, boost.totalHits);
    
    assertEquals(boost.scoreDocs[0].score, noboost.scoreDocs[0].score, SCORE_EPSILON);

    ir.close();
    dir.close();
  }
  
  /**
   * Similarity that wraps another similarity and boosts the final score
   * according to whats in a docvalues field.
   * 
   * @lucene.experimental
   */
  static class BoostingSimilarity extends Similarity {
    private final Similarity sim;
    private final String boostField;
    
    public BoostingSimilarity(Similarity sim, String boostField) {
      this.sim = sim;
      this.boostField = boostField;
    }
    
    @Override
    public long computeNorm(FieldInvertState state) {
      return sim.computeNorm(state);
    }

    @Override
    public SimWeight computeWeight(float queryBoost, CollectionStatistics collectionStats, TermStatistics... termStats) {
      return sim.computeWeight(queryBoost, collectionStats, termStats);
    }

    @Override
    public SimScorer simScorer(SimWeight stats, AtomicReaderContext context) throws IOException {
      final SimScorer sub = sim.simScorer(stats, context);
      final FieldCache.Floats values = FieldCache.DEFAULT.getFloats(context.reader(), boostField, false);
      
      return new SimScorer() {
        @Override
        public float score(int doc, float freq) {
          return values.get(doc) * sub.score(doc, freq);
        }
        
        @Override
        public float computeSlopFactor(int distance) {
          return sub.computeSlopFactor(distance);
        }

        @Override
        public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
          return sub.computePayloadFactor(doc, start, end, payload);
        }

        @Override
        public Explanation explain(int doc, Explanation freq) {
          Explanation boostExplanation = new Explanation(values.get(doc), "indexDocValue(" + boostField + ")");
          Explanation simExplanation = sub.explain(doc, freq);
          Explanation expl = new Explanation(boostExplanation.getValue() * simExplanation.getValue(), "product of:");
          expl.addDetail(boostExplanation);
          expl.addDetail(simExplanation);
          return expl;
        }
      };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3081.java