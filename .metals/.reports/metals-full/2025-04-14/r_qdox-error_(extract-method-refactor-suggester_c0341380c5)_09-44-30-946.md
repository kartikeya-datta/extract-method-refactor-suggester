error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1856.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1856.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1856.java
text:
```scala
public b@@oolean score(Collector collector, int max, int firstDocID) throws IOException {

package org.apache.lucene.search;

/**
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

import org.apache.lucene.search.BooleanClause.Occur;

/* See the description in BooleanScorer.java, comparing
 * BooleanScorer & BooleanScorer2 */

/** An alternative to BooleanScorer that also allows a minimum number
 * of optional scorers that should match.
 * <br>Implements skipTo(), and has no limitations on the numbers of added scorers.
 * <br>Uses ConjunctionScorer, DisjunctionScorer, ReqOptScorer and ReqExclScorer.
 */
class BooleanScorer2 extends Scorer {
  
  private final List<Scorer> requiredScorers;
  private final List<Scorer> optionalScorers;
  private final List<Scorer> prohibitedScorers;

  private class Coordinator {
    float[] coordFactors = null;
    int maxCoord = 0; // to be increased for each non prohibited scorer
    int nrMatchers; // to be increased by score() of match counting scorers.
    
    void init(Similarity sim, boolean disableCoord) { // use after all scorers have been added.
      coordFactors = new float[optionalScorers.size() + requiredScorers.size() + 1];
      for (int i = 0; i < coordFactors.length; i++) {
        coordFactors[i] = disableCoord ? 1.0f : sim.coord(i, maxCoord);
      }
    }
  }

  private final Coordinator coordinator;

  /** The scorer to which all scoring will be delegated,
   * except for computing and using the coordination factor.
   */
  private final Scorer countingSumScorer;

  /** The number of optionalScorers that need to match (if there are any) */
  private final int minNrShouldMatch;

  private int doc = -1;

  /**
   * Creates a {@link Scorer} with the given similarity and lists of required,
   * prohibited and optional scorers. In no required scorers are added, at least
   * one of the optional scorers will have to match during the search.
   * 
   * @param similarity
   *          The similarity to be used.
   * @param minNrShouldMatch
   *          The minimum number of optional added scorers that should match
   *          during the search. In case no required scorers are added, at least
   *          one of the optional scorers will have to match during the search.
   * @param required
   *          the list of required scorers.
   * @param prohibited
   *          the list of prohibited scorers.
   * @param optional
   *          the list of optional scorers.
   */
  public BooleanScorer2(Weight weight, boolean disableCoord, Similarity similarity, int minNrShouldMatch,
      List<Scorer> required, List<Scorer> prohibited, List<Scorer> optional, int maxCoord) throws IOException {
    super(null, weight);   // Similarity not used
    if (minNrShouldMatch < 0) {
      throw new IllegalArgumentException("Minimum number of optional scorers should not be negative");
    }
    coordinator = new Coordinator();
    this.minNrShouldMatch = minNrShouldMatch;
    coordinator.maxCoord = maxCoord;

    optionalScorers = optional;
    requiredScorers = required;    
    prohibitedScorers = prohibited;
    
    coordinator.init(similarity, disableCoord);
    countingSumScorer = makeCountingSumScorer(disableCoord, similarity);
  }
  
  /** Count a scorer as a single match. */
  private class SingleMatchScorer extends Scorer {
    private Scorer scorer;
    private int lastScoredDoc = -1;
    // Save the score of lastScoredDoc, so that we don't compute it more than
    // once in score().
    private float lastDocScore = Float.NaN;

    SingleMatchScorer(Scorer scorer) {
      super(null); // No similarity used.
      this.scorer = scorer;
    }

    @Override
    public float score() throws IOException {
      int doc = docID();
      if (doc >= lastScoredDoc) {
        if (doc > lastScoredDoc) {
          lastDocScore = scorer.score();
          lastScoredDoc = doc;
        }
        coordinator.nrMatchers++;
      }
      return lastDocScore;
    }

    @Override
    public int docID() {
      return scorer.docID();
    }

    @Override
    public int nextDoc() throws IOException {
      return scorer.nextDoc();
    }

    @Override
    public int advance(int target) throws IOException {
      return scorer.advance(target);
    }
  }

  private Scorer countingDisjunctionSumScorer(final List<Scorer> scorers,
      int minNrShouldMatch) throws IOException {
    // each scorer from the list counted as a single matcher
    return new DisjunctionSumScorer(scorers, minNrShouldMatch) {
      private int lastScoredDoc = -1;
      // Save the score of lastScoredDoc, so that we don't compute it more than
      // once in score().
      private float lastDocScore = Float.NaN;
      @Override public float score() throws IOException {
        int doc = docID();
        if (doc >= lastScoredDoc) {
          if (doc > lastScoredDoc) {
            lastDocScore = super.score();
            lastScoredDoc = doc;
          }
          coordinator.nrMatchers += super.nrMatchers;
        }
        return lastDocScore;
      }
    };
  }

  private Scorer countingConjunctionSumScorer(boolean disableCoord,
                                              Similarity similarity,
                                              List<Scorer> requiredScorers) throws IOException {
    // each scorer from the list counted as a single matcher
    final int requiredNrMatchers = requiredScorers.size();
    return new ConjunctionScorer(disableCoord ? 1.0f : similarity.coord(requiredScorers.size(), requiredScorers.size()), requiredScorers) {
      private int lastScoredDoc = -1;
      // Save the score of lastScoredDoc, so that we don't compute it more than
      // once in score().
      private float lastDocScore = Float.NaN;
      @Override public float score() throws IOException {
        int doc = docID();
        if (doc >= lastScoredDoc) {
          if (doc > lastScoredDoc) {
            lastDocScore = super.score();
            lastScoredDoc = doc;
          }
          coordinator.nrMatchers += requiredNrMatchers;
        }
        // All scorers match, so defaultSimilarity super.score() always has 1 as
        // the coordination factor.
        // Therefore the sum of the scores of the requiredScorers
        // is used as score.
        return lastDocScore;
      }
    };
  }

  private Scorer dualConjunctionSumScorer(boolean disableCoord,
                                          Similarity similarity,
                                          Scorer req1, Scorer req2) throws IOException { // non counting.
    return new ConjunctionScorer(disableCoord ? 1.0f : similarity.coord(2, 2), req1, req2);
    // All scorers match, so defaultSimilarity always has 1 as
    // the coordination factor.
    // Therefore the sum of the scores of two scorers
    // is used as score.
  }

  /** Returns the scorer to be used for match counting and score summing.
   * Uses requiredScorers, optionalScorers and prohibitedScorers.
   */
  private Scorer makeCountingSumScorer(boolean disableCoord,
                                       Similarity similarity) throws IOException { // each scorer counted as a single matcher
    return (requiredScorers.size() == 0)
      ? makeCountingSumScorerNoReq(disableCoord, similarity)
      : makeCountingSumScorerSomeReq(disableCoord, similarity);
  }

  private Scorer makeCountingSumScorerNoReq(boolean disableCoord, Similarity similarity) throws IOException { // No required scorers
    // minNrShouldMatch optional scorers are required, but at least 1
    int nrOptRequired = (minNrShouldMatch < 1) ? 1 : minNrShouldMatch;
    Scorer requiredCountingSumScorer;
    if (optionalScorers.size() > nrOptRequired)
      requiredCountingSumScorer = countingDisjunctionSumScorer(optionalScorers, nrOptRequired);
    else if (optionalScorers.size() == 1)
      requiredCountingSumScorer = new SingleMatchScorer(optionalScorers.get(0));
    else {
      requiredCountingSumScorer = countingConjunctionSumScorer(disableCoord, similarity, optionalScorers);
    }
    return addProhibitedScorers(requiredCountingSumScorer);
  }

  private Scorer makeCountingSumScorerSomeReq(boolean disableCoord, Similarity similarity) throws IOException { // At least one required scorer.
    if (optionalScorers.size() == minNrShouldMatch) { // all optional scorers also required.
      ArrayList<Scorer> allReq = new ArrayList<Scorer>(requiredScorers);
      allReq.addAll(optionalScorers);
      return addProhibitedScorers(countingConjunctionSumScorer(disableCoord, similarity, allReq));
    } else { // optionalScorers.size() > minNrShouldMatch, and at least one required scorer
      Scorer requiredCountingSumScorer =
            requiredScorers.size() == 1
            ? new SingleMatchScorer(requiredScorers.get(0))
            : countingConjunctionSumScorer(disableCoord, similarity, requiredScorers);
      if (minNrShouldMatch > 0) { // use a required disjunction scorer over the optional scorers
        return addProhibitedScorers( 
                      dualConjunctionSumScorer( // non counting
                              disableCoord,
                              similarity,
                              requiredCountingSumScorer,
                              countingDisjunctionSumScorer(
                                      optionalScorers,
                                      minNrShouldMatch)));
      } else { // minNrShouldMatch == 0
        return new ReqOptSumScorer(
                      addProhibitedScorers(requiredCountingSumScorer),
                      optionalScorers.size() == 1
                        ? new SingleMatchScorer(optionalScorers.get(0))
                        // require 1 in combined, optional scorer.
                        : countingDisjunctionSumScorer(optionalScorers, 1));
      }
    }
  }
  
  /** Returns the scorer to be used for match counting and score summing.
   * Uses the given required scorer and the prohibitedScorers.
   * @param requiredCountingSumScorer A required scorer already built.
   */
  private Scorer addProhibitedScorers(Scorer requiredCountingSumScorer) throws IOException
  {
    return (prohibitedScorers.size() == 0)
          ? requiredCountingSumScorer // no prohibited
          : new ReqExclScorer(requiredCountingSumScorer,
                              ((prohibitedScorers.size() == 1)
                                ? prohibitedScorers.get(0)
                                : new DisjunctionSumScorer(prohibitedScorers)));
  }

  /** Scores and collects all matching documents.
   * @param collector The collector to which all matching documents are passed through.
   */
  @Override
  public void score(Collector collector) throws IOException {
    collector.setScorer(this);
    while ((doc = countingSumScorer.nextDoc()) != NO_MORE_DOCS) {
      collector.collect(doc);
    }
  }
  
  @Override
  protected boolean score(Collector collector, int max, int firstDocID) throws IOException {
    doc = firstDocID;
    collector.setScorer(this);
    while (doc < max) {
      collector.collect(doc);
      doc = countingSumScorer.nextDoc();
    }
    return doc != NO_MORE_DOCS;
  }

  @Override
  public int docID() {
    return doc;
  }
  
  @Override
  public int nextDoc() throws IOException {
    return doc = countingSumScorer.nextDoc();
  }
  
  @Override
  public float score() throws IOException {
    coordinator.nrMatchers = 0;
    float sum = countingSumScorer.score();
    return sum * coordinator.coordFactors[coordinator.nrMatchers];
  }

  @Override
  public float freq() {
    return coordinator.nrMatchers;
  }

  @Override
  public int advance(int target) throws IOException {
    return doc = countingSumScorer.advance(target);
  }

  @Override
  protected void visitSubScorers(Query parent, Occur relationship, ScorerVisitor<Query, Query, Scorer> visitor) {
    super.visitSubScorers(parent, relationship, visitor);
    final Query q = weight.getQuery();
    for (Scorer s : optionalScorers) {
      s.visitSubScorers(q, Occur.SHOULD, visitor);
    }
    for (Scorer s : prohibitedScorers) {
      s.visitSubScorers(q, Occur.MUST_NOT, visitor);
    }
    for (Scorer s : requiredScorers) {
      s.visitSubScorers(q, Occur.MUST, visitor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1856.java