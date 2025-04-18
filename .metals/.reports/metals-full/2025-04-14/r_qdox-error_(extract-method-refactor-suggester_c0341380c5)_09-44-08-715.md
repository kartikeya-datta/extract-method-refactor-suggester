error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1036.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1036.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1036.java
text:
```scala
t@@his.subQueryWeight = subQuery.createWeight(searcher);

package org.apache.lucene.search.function;

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
import java.util.Set;
import java.util.Arrays;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.util.ToStringUtils;

/**
 * Query that sets document score as a programmatic function of several (sub) scores:
 * <ol>
 *    <li>the score of its subQuery (any query)</li>
 *    <li>(optional) the score of its ValueSourceQuery (or queries).
 *        For most simple/convenient use cases this query is likely to be a 
 *        {@link org.apache.lucene.search.function.FieldScoreQuery FieldScoreQuery}</li>
 * </ol>
 * Subclasses can modify the computation by overriding {@link #getCustomScoreProvider}.
 * 
 * @lucene.experimental
 */
public class CustomScoreQuery extends Query {

  private Query subQuery;
  private ValueSourceQuery[] valSrcQueries; // never null (empty array if there are no valSrcQueries).
  private boolean strict = false; // if true, valueSource part of query does not take part in weights normalization.  
  
  /**
   * Create a CustomScoreQuery over input subQuery.
   * @param subQuery the sub query whose scored is being customized. Must not be null. 
   */
  public CustomScoreQuery(Query subQuery) {
    this(subQuery, new ValueSourceQuery[0]);
  }

  /**
   * Create a CustomScoreQuery over input subQuery and a {@link ValueSourceQuery}.
   * @param subQuery the sub query whose score is being customized. Must not be null.
   * @param valSrcQuery a value source query whose scores are used in the custom score
   * computation. For most simple/convenient use case this would be a 
   * {@link org.apache.lucene.search.function.FieldScoreQuery FieldScoreQuery}.
   * This parameter is optional - it can be null.
   */
  public CustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery) {
	  this(subQuery, valSrcQuery!=null ? // don't want an array that contains a single null.. 
        new ValueSourceQuery[] {valSrcQuery} : new ValueSourceQuery[0]);
  }

  /**
   * Create a CustomScoreQuery over input subQuery and a {@link ValueSourceQuery}.
   * @param subQuery the sub query whose score is being customized. Must not be null.
   * @param valSrcQueries value source queries whose scores are used in the custom score
   * computation. For most simple/convenient use case these would be 
   * {@link org.apache.lucene.search.function.FieldScoreQuery FieldScoreQueries}.
   * This parameter is optional - it can be null or even an empty array.
   */
  public CustomScoreQuery(Query subQuery, ValueSourceQuery... valSrcQueries) {
    this.subQuery = subQuery;
    this.valSrcQueries = valSrcQueries!=null?
        valSrcQueries : new ValueSourceQuery[0];
    if (subQuery == null) throw new IllegalArgumentException("<subquery> must not be null!");
  }

  /*(non-Javadoc) @see org.apache.lucene.search.Query#rewrite(org.apache.lucene.index.IndexReader) */
  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    CustomScoreQuery clone = null;
    
    final Query sq = subQuery.rewrite(reader);
    if (sq != subQuery) {
      clone = (CustomScoreQuery) clone();
      clone.subQuery = sq;
    }

    for(int i = 0; i < valSrcQueries.length; i++) {
      final ValueSourceQuery v = (ValueSourceQuery) valSrcQueries[i].rewrite(reader);
      if (v != valSrcQueries[i]) {
        if (clone == null) clone = (CustomScoreQuery) clone();
        clone.valSrcQueries[i] = v;
      }
    }
    
    return (clone == null) ? this : clone;
  }

  /*(non-Javadoc) @see org.apache.lucene.search.Query#extractTerms(java.util.Set) */
  @Override
  public void extractTerms(Set<Term> terms) {
    subQuery.extractTerms(terms);
    for(int i = 0; i < valSrcQueries.length; i++) {
      valSrcQueries[i].extractTerms(terms);
    }
  }

  /*(non-Javadoc) @see org.apache.lucene.search.Query#clone() */
  @Override
  public Object clone() {
    CustomScoreQuery clone = (CustomScoreQuery)super.clone();
    clone.subQuery = (Query) subQuery.clone();
    clone.valSrcQueries = new ValueSourceQuery[valSrcQueries.length];
    for(int i = 0; i < valSrcQueries.length; i++) {
      clone.valSrcQueries[i] = (ValueSourceQuery) valSrcQueries[i].clone();
    }
    return clone;
  }

  /* (non-Javadoc) @see org.apache.lucene.search.Query#toString(java.lang.String) */
  @Override
  public String toString(String field) {
    StringBuilder sb = new StringBuilder(name()).append("(");
    sb.append(subQuery.toString(field));
    for(int i = 0; i < valSrcQueries.length; i++) {
      sb.append(", ").append(valSrcQueries[i].toString(field));
    }
    sb.append(")");
    sb.append(strict?" STRICT" : "");
    return sb.toString() + ToStringUtils.boost(getBoost());
  }

  /** Returns true if <code>o</code> is equal to this. */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!super.equals(o))
      return false;
    if (getClass() != o.getClass()) {
      return false;
    }
    CustomScoreQuery other = (CustomScoreQuery)o;
    if (this.getBoost() != other.getBoost() ||
        !this.subQuery.equals(other.subQuery) ||
        this.strict != other.strict ||
        this.valSrcQueries.length != other.valSrcQueries.length) {
      return false;
    }
    return Arrays.equals(valSrcQueries, other.valSrcQueries);
  }

  /** Returns a hash code value for this object. */
  @Override
  public int hashCode() {
    return (getClass().hashCode() + subQuery.hashCode() + Arrays.hashCode(valSrcQueries))
      ^ Float.floatToIntBits(getBoost()) ^ (strict ? 1234 : 4321);
  }
  
  /**
   * Returns a {@link CustomScoreProvider} that calculates the custom scores
   * for the given {@link IndexReader}. The default implementation returns a default
   * implementation as specified in the docs of {@link CustomScoreProvider}.
   * @since 2.9.2
   */
  protected CustomScoreProvider getCustomScoreProvider(IndexReader reader) throws IOException {
    return new CustomScoreProvider(reader);
  }

  //=========================== W E I G H T ============================
  
  private class CustomWeight extends Weight {
    Similarity similarity;
    Weight subQueryWeight;
    Weight[] valSrcWeights;
    boolean qStrict;

    public CustomWeight(Searcher searcher) throws IOException {
      this.similarity = getSimilarity(searcher);
      this.subQueryWeight = subQuery.weight(searcher);
      this.valSrcWeights = new Weight[valSrcQueries.length];
      for(int i = 0; i < valSrcQueries.length; i++) {
        this.valSrcWeights[i] = valSrcQueries[i].createWeight(searcher);
      }
      this.qStrict = strict;
    }

    /*(non-Javadoc) @see org.apache.lucene.search.Weight#getQuery() */
    @Override
    public Query getQuery() {
      return CustomScoreQuery.this;
    }

    /*(non-Javadoc) @see org.apache.lucene.search.Weight#getValue() */
    @Override
    public float getValue() {
      return getBoost();
    }

    /*(non-Javadoc) @see org.apache.lucene.search.Weight#sumOfSquaredWeights() */
    @Override
    public float sumOfSquaredWeights() throws IOException {
      float sum = subQueryWeight.sumOfSquaredWeights();
      for(int i = 0; i < valSrcWeights.length; i++) {
        if (qStrict) {
          valSrcWeights[i].sumOfSquaredWeights(); // do not include ValueSource part in the query normalization
        } else {
          sum += valSrcWeights[i].sumOfSquaredWeights();
        }
      }
      sum *= getBoost() * getBoost(); // boost each sub-weight
      return sum ;
    }

    /*(non-Javadoc) @see org.apache.lucene.search.Weight#normalize(float) */
    @Override
    public void normalize(float norm) {
      norm *= getBoost(); // incorporate boost
      subQueryWeight.normalize(norm);
      for(int i = 0; i < valSrcWeights.length; i++) {
        if (qStrict) {
          valSrcWeights[i].normalize(1); // do not normalize the ValueSource part
        } else {
          valSrcWeights[i].normalize(norm);
        }
      }
    }

    @Override
    public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException {
      // Pass true for "scoresDocsInOrder", because we
      // require in-order scoring, even if caller does not,
      // since we call advance on the valSrcScorers.  Pass
      // false for "topScorer" because we will not invoke
      // score(Collector) on these scorers:
      Scorer subQueryScorer = subQueryWeight.scorer(reader, true, false);
      if (subQueryScorer == null) {
        return null;
      }
      Scorer[] valSrcScorers = new Scorer[valSrcWeights.length];
      for(int i = 0; i < valSrcScorers.length; i++) {
         valSrcScorers[i] = valSrcWeights[i].scorer(reader, true, topScorer);
      }
      return new CustomScorer(similarity, reader, this, subQueryScorer, valSrcScorers);
    }

    @Override
    public Explanation explain(IndexReader reader, int doc) throws IOException {
      Explanation explain = doExplain(reader, doc);
      return explain == null ? new Explanation(0.0f, "no matching docs") : explain;
    }
    
    private Explanation doExplain(IndexReader reader, int doc) throws IOException {
      Explanation subQueryExpl = subQueryWeight.explain(reader, doc);
      if (!subQueryExpl.isMatch()) {
        return subQueryExpl;
      }
      // match
      Explanation[] valSrcExpls = new Explanation[valSrcWeights.length];
      for(int i = 0; i < valSrcWeights.length; i++) {
        valSrcExpls[i] = valSrcWeights[i].explain(reader, doc);
      }
      Explanation customExp = CustomScoreQuery.this.getCustomScoreProvider(reader).customExplain(doc,subQueryExpl,valSrcExpls);
      float sc = getValue() * customExp.getValue();
      Explanation res = new ComplexExplanation(
        true, sc, CustomScoreQuery.this.toString() + ", product of:");
      res.addDetail(customExp);
      res.addDetail(new Explanation(getValue(), "queryBoost")); // actually using the q boost as q weight (== weight value)
      return res;
    }

    @Override
    public boolean scoresDocsOutOfOrder() {
      return false;
    }
    
  }


  //=========================== S C O R E R ============================
  
  /**
   * A scorer that applies a (callback) function on scores of the subQuery.
   */
  private class CustomScorer extends Scorer {
    private final float qWeight;
    private Scorer subQueryScorer;
    private Scorer[] valSrcScorers;
    private final CustomScoreProvider provider;
    private float vScores[]; // reused in score() to avoid allocating this array for each doc 

    // constructor
    private CustomScorer(Similarity similarity, IndexReader reader, CustomWeight w,
        Scorer subQueryScorer, Scorer[] valSrcScorers) throws IOException {
      super(similarity,w);
      this.qWeight = w.getValue();
      this.subQueryScorer = subQueryScorer;
      this.valSrcScorers = valSrcScorers;
      this.vScores = new float[valSrcScorers.length];
      this.provider = CustomScoreQuery.this.getCustomScoreProvider(reader);
    }

    @Override
    public int nextDoc() throws IOException {
      int doc = subQueryScorer.nextDoc();
      if (doc != NO_MORE_DOCS) {
        for (int i = 0; i < valSrcScorers.length; i++) {
          valSrcScorers[i].advance(doc);
        }
      }
      return doc;
    }

    @Override
    public int docID() {
      return subQueryScorer.docID();
    }
    
    /*(non-Javadoc) @see org.apache.lucene.search.Scorer#score() */
    @Override
    public float score() throws IOException {
      for (int i = 0; i < valSrcScorers.length; i++) {
        vScores[i] = valSrcScorers[i].score();
      }
      return qWeight * provider.customScore(subQueryScorer.docID(), subQueryScorer.score(), vScores);
    }

    @Override
    public int advance(int target) throws IOException {
      int doc = subQueryScorer.advance(target);
      if (doc != NO_MORE_DOCS) {
        for (int i = 0; i < valSrcScorers.length; i++) {
          valSrcScorers[i].advance(doc);
        }
      }
      return doc;
    }
  }

  @Override
  public Weight createWeight(Searcher searcher) throws IOException {
    return new CustomWeight(searcher);
  }

  /**
   * Checks if this is strict custom scoring.
   * In strict custom scoring, the ValueSource part does not participate in weight normalization.
   * This may be useful when one wants full control over how scores are modified, and does 
   * not care about normalizing by the ValueSource part.
   * One particular case where this is useful if for testing this query.   
   * <P>
   * Note: only has effect when the ValueSource part is not null.
   */
  public boolean isStrict() {
    return strict;
  }

  /**
   * Set the strict mode of this query. 
   * @param strict The strict mode to set.
   * @see #isStrict()
   */
  public void setStrict(boolean strict) {
    this.strict = strict;
  }

  /**
   * A short name of this query, used in {@link #toString(String)}.
   */
  public String name() {
    return "custom";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1036.java