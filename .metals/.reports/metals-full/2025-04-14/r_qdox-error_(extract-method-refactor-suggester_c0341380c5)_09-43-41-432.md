error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2286.java
text:
```scala
n@@umTerms = state.getLength();

package org.apache.lucene.search.similarities;

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

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.SmallFloat;

/**
 * A subclass of {@code Similarity} that provides a simplified API for its
 * descendants. Subclasses are only required to implement the {@link #score}
 * and {@link #toString()} methods. Implementing
 * {@link #explain(Explanation, BasicStats, int, float, float)} is optional,
 * inasmuch as SimilarityBase already provides a basic explanation of the score
 * and the term frequency. However, implementers of a subclass are encouraged to
 * include as much detail about the scoring method as possible.
 * <p>
 * Note: multi-word queries such as phrase queries are scored in a different way
 * than Lucene's default ranking algorithm: whereas it "fakes" an IDF value for
 * the phrase as a whole (since it does not know it), this class instead scores
 * phrases as a summation of the individual term scores.
 * @lucene.experimental
 */
public abstract class SimilarityBase extends Similarity {
  /** For {@link #log2(double)}. Precomputed for efficiency reasons. */
  private static final double LOG_2 = Math.log(2);
  
  /** 
   * True if overlap tokens (tokens with a position of increment of zero) are
   * discounted from the document's length.
   */
  protected boolean discountOverlaps = true;
  
  /**
   * Sole constructor. (For invocation by subclass 
   * constructors, typically implicit.)
   */
  public SimilarityBase() {}
  
  /** Determines whether overlap tokens (Tokens with
   *  0 position increment) are ignored when computing
   *  norm.  By default this is true, meaning overlap
   *  tokens do not count when computing norms.
   *
   *  @lucene.experimental
   *
   *  @see #computeNorm
   */
  public void setDiscountOverlaps(boolean v) {
    discountOverlaps = v;
  }

  /**
   * Returns true if overlap tokens are discounted from the document's length. 
   * @see #setDiscountOverlaps 
   */
  public boolean getDiscountOverlaps() {
    return discountOverlaps;
  }
  
  @Override
  public final SimWeight computeWeight(float queryBoost, CollectionStatistics collectionStats, TermStatistics... termStats) {
    BasicStats stats[] = new BasicStats[termStats.length];
    for (int i = 0; i < termStats.length; i++) {
      stats[i] = newStats(collectionStats.field(), queryBoost);
      fillBasicStats(stats[i], collectionStats, termStats[i]);
    }
    return stats.length == 1 ? stats[0] : new MultiSimilarity.MultiStats(stats);
  }
  
  /** Factory method to return a custom stats object */
  protected BasicStats newStats(String field, float queryBoost) {
    return new BasicStats(field, queryBoost);
  }
  
  /** Fills all member fields defined in {@code BasicStats} in {@code stats}. 
   *  Subclasses can override this method to fill additional stats. */
  protected void fillBasicStats(BasicStats stats, CollectionStatistics collectionStats, TermStatistics termStats) {
    // #positions(field) must be >= #positions(term)
    assert collectionStats.sumTotalTermFreq() == -1 || collectionStats.sumTotalTermFreq() >= termStats.totalTermFreq();
    long numberOfDocuments = collectionStats.maxDoc();
    
    long docFreq = termStats.docFreq();
    long totalTermFreq = termStats.totalTermFreq();

    // codec does not supply totalTermFreq: substitute docFreq
    if (totalTermFreq == -1) {
      totalTermFreq = docFreq;
    }

    final long numberOfFieldTokens;
    final float avgFieldLength;

    long sumTotalTermFreq = collectionStats.sumTotalTermFreq();

    if (sumTotalTermFreq <= 0) {
      // field does not exist;
      // We have to provide something if codec doesnt supply these measures,
      // or if someone omitted frequencies for the field... negative values cause
      // NaN/Inf for some scorers.
      numberOfFieldTokens = docFreq;
      avgFieldLength = 1;
    } else {
      numberOfFieldTokens = sumTotalTermFreq;
      avgFieldLength = (float)numberOfFieldTokens / numberOfDocuments;
    }
 
    // TODO: add sumDocFreq for field (numberOfFieldPostings)
    stats.setNumberOfDocuments(numberOfDocuments);
    stats.setNumberOfFieldTokens(numberOfFieldTokens);
    stats.setAvgFieldLength(avgFieldLength);
    stats.setDocFreq(docFreq);
    stats.setTotalTermFreq(totalTermFreq);
  }
  
  /**
   * Scores the document {@code doc}.
   * <p>Subclasses must apply their scoring formula in this class.</p>
   * @param stats the corpus level statistics.
   * @param freq the term frequency.
   * @param docLen the document length.
   * @return the score.
   */
  protected abstract float score(BasicStats stats, float freq, float docLen);
  
  /**
   * Subclasses should implement this method to explain the score. {@code expl}
   * already contains the score, the name of the class and the doc id, as well
   * as the term frequency and its explanation; subclasses can add additional
   * clauses to explain details of their scoring formulae.
   * <p>The default implementation does nothing.</p>
   * 
   * @param expl the explanation to extend with details.
   * @param stats the corpus level statistics.
   * @param doc the document id.
   * @param freq the term frequency.
   * @param docLen the document length.
   */
  protected void explain(
      Explanation expl, BasicStats stats, int doc, float freq, float docLen) {}
  
  /**
   * Explains the score. The implementation here provides a basic explanation
   * in the format <em>score(name-of-similarity, doc=doc-id,
   * freq=term-frequency), computed from:</em>, and
   * attaches the score (computed via the {@link #score(BasicStats, float, float)}
   * method) and the explanation for the term frequency. Subclasses content with
   * this format may add additional details in
   * {@link #explain(Explanation, BasicStats, int, float, float)}.
   *  
   * @param stats the corpus level statistics.
   * @param doc the document id.
   * @param freq the term frequency and its explanation.
   * @param docLen the document length.
   * @return the explanation.
   */
  protected Explanation explain(
      BasicStats stats, int doc, Explanation freq, float docLen) {
    Explanation result = new Explanation(); 
    result.setValue(score(stats, freq.getValue(), docLen));
    result.setDescription("score(" + getClass().getSimpleName() +
        ", doc=" + doc + ", freq=" + freq.getValue() +"), computed from:");
    result.addDetail(freq);
    
    explain(result, stats, doc, freq.getValue(), docLen);
    
    return result;
  }
  
  @Override
  public SimScorer simScorer(SimWeight stats, AtomicReaderContext context) throws IOException {
    if (stats instanceof MultiSimilarity.MultiStats) {
      // a multi term query (e.g. phrase). return the summation, 
      // scoring almost as if it were boolean query
      SimWeight subStats[] = ((MultiSimilarity.MultiStats) stats).subStats;
      SimScorer subScorers[] = new SimScorer[subStats.length];
      for (int i = 0; i < subScorers.length; i++) {
        BasicStats basicstats = (BasicStats) subStats[i];
        subScorers[i] = new BasicSimScorer(basicstats, context.reader().getNormValues(basicstats.field));
      }
      return new MultiSimilarity.MultiSimScorer(subScorers);
    } else {
      BasicStats basicstats = (BasicStats) stats;
      return new BasicSimScorer(basicstats, context.reader().getNormValues(basicstats.field));
    }
  }
  
  /**
   * Subclasses must override this method to return the name of the Similarity
   * and preferably the values of parameters (if any) as well.
   */
  @Override
  public abstract String toString();

  // ------------------------------ Norm handling ------------------------------
  
  /** Norm -> document length map. */
  private static final float[] NORM_TABLE = new float[256];

  static {
    for (int i = 0; i < 256; i++) {
      float floatNorm = SmallFloat.byte315ToFloat((byte)i);
      NORM_TABLE[i] = 1.0f / (floatNorm * floatNorm);
    }
  }

  /** Encodes the document length in the same way as {@link TFIDFSimilarity}. */
  @Override
  public long computeNorm(FieldInvertState state) {
    final float numTerms;
    if (discountOverlaps)
      numTerms = state.getLength() - state.getNumOverlap();
    else
      numTerms = state.getLength() / state.getBoost();
    return encodeNormValue(state.getBoost(), numTerms);
  }
  
  /** Decodes a normalization factor (document length) stored in an index.
   * @see #encodeNormValue(float,float)
   */
  protected float decodeNormValue(byte norm) {
    return NORM_TABLE[norm & 0xFF];  // & 0xFF maps negative bytes to positive above 127
  }
  
  /** Encodes the length to a byte via SmallFloat. */
  protected byte encodeNormValue(float boost, float length) {
    return SmallFloat.floatToByte315((boost / (float) Math.sqrt(length)));
  }
  
  // ----------------------------- Static methods ------------------------------
  
  /** Returns the base two logarithm of {@code x}. */
  public static double log2(double x) {
    // Put this to a 'util' class if we need more of these.
    return Math.log(x) / LOG_2;
  }
  
  // --------------------------------- Classes ---------------------------------
  
  /** Delegates the {@link #score(int, float)} and
   * {@link #explain(int, Explanation)} methods to
   * {@link SimilarityBase#score(BasicStats, float, float)} and
   * {@link SimilarityBase#explain(BasicStats, int, Explanation, float)},
   * respectively.
   */
  private class BasicSimScorer extends SimScorer {
    private final BasicStats stats;
    private final NumericDocValues norms;
    
    BasicSimScorer(BasicStats stats, NumericDocValues norms) throws IOException {
      this.stats = stats;
      this.norms = norms;
    }
    
    @Override
    public float score(int doc, float freq) {
      // We have to supply something in case norms are omitted
      return SimilarityBase.this.score(stats, freq,
          norms == null ? 1F : decodeNormValue((byte)norms.get(doc)));
    }
    @Override
    public Explanation explain(int doc, Explanation freq) {
      return SimilarityBase.this.explain(stats, doc, freq,
          norms == null ? 1F : decodeNormValue((byte)norms.get(doc)));
    }

    @Override
    public float computeSlopFactor(int distance) {
      return 1.0f / (distance + 1);
    }

    @Override
    public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
      return 1f;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2286.java