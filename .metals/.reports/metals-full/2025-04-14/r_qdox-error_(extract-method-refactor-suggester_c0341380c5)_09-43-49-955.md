error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/762.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/762.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/762.java
text:
```scala
i@@f (subScorer != null) {

package org.apache.lucene.search;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Bits;

/**
 * A query that generates the union of documents produced by its subqueries, and that scores each document with the maximum
 * score for that document as produced by any subquery, plus a tie breaking increment for any additional matching subqueries.
 * This is useful when searching for a word in multiple fields with different boost factors (so that the fields cannot be
 * combined equivalently into a single search field).  We want the primary score to be the one associated with the highest boost,
 * not the sum of the field scores (as BooleanQuery would give).
 * If the query is "albino elephant" this ensures that "albino" matching one field and "elephant" matching
 * another gets a higher score than "albino" matching both fields.
 * To get this result, use both BooleanQuery and DisjunctionMaxQuery:  for each term a DisjunctionMaxQuery searches for it in
 * each field, while the set of these DisjunctionMaxQuery's is combined into a BooleanQuery.
 * The tie breaker capability allows results that include the same term in multiple fields to be judged better than results that
 * include this term in only the best of those multiple fields, without confusing this with the better case of two different terms
 * in the multiple fields.
 */
public class DisjunctionMaxQuery extends Query implements Iterable<Query> {

  /* The subqueries */
  private ArrayList<Query> disjuncts = new ArrayList<Query>();

  /* Multiple of the non-max disjunct scores added into our final score.  Non-zero values support tie-breaking. */
  private float tieBreakerMultiplier = 0.0f;

  /** Creates a new empty DisjunctionMaxQuery.  Use add() to add the subqueries.
   * @param tieBreakerMultiplier the score of each non-maximum disjunct for a document is multiplied by this weight
   *        and added into the final score.  If non-zero, the value should be small, on the order of 0.1, which says that
   *        10 occurrences of word in a lower-scored field that is also in a higher scored field is just as good as a unique
   *        word in the lower scored field (i.e., one that is not in any higher scored field.
   */
  public DisjunctionMaxQuery(float tieBreakerMultiplier) {
    this.tieBreakerMultiplier = tieBreakerMultiplier;
  }

  /**
   * Creates a new DisjunctionMaxQuery
   * @param disjuncts a Collection<Query> of all the disjuncts to add
   * @param tieBreakerMultiplier   the weight to give to each matching non-maximum disjunct
   */
  public DisjunctionMaxQuery(Collection<Query> disjuncts, float tieBreakerMultiplier) {
    this.tieBreakerMultiplier = tieBreakerMultiplier;
    add(disjuncts);
  }

  /** Add a subquery to this disjunction
   * @param query the disjunct added
   */
  public void add(Query query) {
    disjuncts.add(query);
  }

  /** Add a collection of disjuncts to this disjunction
   * via Iterable<Query>
   * @param disjuncts a collection of queries to add as disjuncts.
   */
  public void add(Collection<Query> disjuncts) {
    this.disjuncts.addAll(disjuncts);
  }

  /** @return An Iterator<Query> over the disjuncts */
  public Iterator<Query> iterator() {
    return disjuncts.iterator();
  }
  
  /**
   * @return the disjuncts.
   */
  public ArrayList<Query> getDisjuncts() {
    return disjuncts;
  }

  /**
   * @return tie breaker value for multiple matches.
   */
  public float getTieBreakerMultiplier() {
    return tieBreakerMultiplier;
  }

  /**
   * Expert: the Weight for DisjunctionMaxQuery, used to
   * normalize, score and explain these queries.
   *
   * <p>NOTE: this API and implementation is subject to
   * change suddenly in the next release.</p>
   */
  protected class DisjunctionMaxWeight extends Weight {

    /** The Weights for our subqueries, in 1-1 correspondence with disjuncts */
    protected ArrayList<Weight> weights = new ArrayList<Weight>();  // The Weight's for our subqueries, in 1-1 correspondence with disjuncts

    /** Construct the Weight for this Query searched by searcher.  Recursively construct subquery weights. */
    public DisjunctionMaxWeight(IndexSearcher searcher) throws IOException {
      for (Query disjunctQuery : disjuncts) {
        weights.add(disjunctQuery.createWeight(searcher));
      }
    }

    /** Return our associated DisjunctionMaxQuery */
    @Override
    public Query getQuery() { return DisjunctionMaxQuery.this; }

    /** Compute the sub of squared weights of us applied to our subqueries.  Used for normalization. */
    @Override
    public float getValueForNormalization() throws IOException {
      float max = 0.0f, sum = 0.0f;
      for (Weight currentWeight : weights) {
        float sub = currentWeight.getValueForNormalization();
        sum += sub;
        max = Math.max(max, sub);
        
      }
      float boost = getBoost();
      return (((sum - max) * tieBreakerMultiplier * tieBreakerMultiplier) + max) * boost * boost;
    }

    /** Apply the computed normalization factor to our subqueries */
    @Override
    public void normalize(float norm, float topLevelBoost) {
      topLevelBoost *= getBoost();  // Incorporate our boost
      for (Weight wt : weights) {
        wt.normalize(norm, topLevelBoost);
      }
    }

    /** Create the scorer used to score our associated DisjunctionMaxQuery */
    @Override
    public Scorer scorer(AtomicReaderContext context, boolean scoreDocsInOrder,
        boolean topScorer, Bits acceptDocs) throws IOException {
      Scorer[] scorers = new Scorer[weights.size()];
      int idx = 0;
      for (Weight w : weights) {
        // we will advance() subscorers
        Scorer subScorer = w.scorer(context, true, false, acceptDocs);
        if (subScorer != null && subScorer.nextDoc() != DocIdSetIterator.NO_MORE_DOCS) {
          scorers[idx++] = subScorer;
        }
      }
      if (idx == 0) return null; // all scorers did not have documents
      DisjunctionMaxScorer result = new DisjunctionMaxScorer(this, tieBreakerMultiplier, scorers, idx);
      return result;
    }

    /** Explain the score we computed for doc */
    @Override
    public Explanation explain(AtomicReaderContext context, int doc) throws IOException {
      if (disjuncts.size() == 1) return weights.get(0).explain(context,doc);
      ComplexExplanation result = new ComplexExplanation();
      float max = 0.0f, sum = 0.0f;
      result.setDescription(tieBreakerMultiplier == 0.0f ? "max of:" : "max plus " + tieBreakerMultiplier + " times others of:");
      for (Weight wt : weights) {
        Explanation e = wt.explain(context, doc);
        if (e.isMatch()) {
          result.setMatch(Boolean.TRUE);
          result.addDetail(e);
          sum += e.getValue();
          max = Math.max(max, e.getValue());
        }
      }
      result.setValue(max + (sum - max) * tieBreakerMultiplier);
      return result;
    }
    
  }  // end of DisjunctionMaxWeight inner class

  /** Create the Weight used to score us */
  @Override
  public Weight createWeight(IndexSearcher searcher) throws IOException {
    return new DisjunctionMaxWeight(searcher);
  }

  /** Optimize our representation and our subqueries representations
   * @param reader the IndexReader we query
   * @return an optimized copy of us (which may not be a copy if there is nothing to optimize) */
  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    int numDisjunctions = disjuncts.size();
    if (numDisjunctions == 1) {
      Query singleton = disjuncts.get(0);
      Query result = singleton.rewrite(reader);
      if (getBoost() != 1.0f) {
        if (result == singleton) result = result.clone();
        result.setBoost(getBoost() * result.getBoost());
      }
      return result;
    }
    DisjunctionMaxQuery clone = null;
    for (int i = 0 ; i < numDisjunctions; i++) {
      Query clause = disjuncts.get(i);
      Query rewrite = clause.rewrite(reader);
      if (rewrite != clause) {
        if (clone == null) clone = this.clone();
        clone.disjuncts.set(i, rewrite);
      }
    }
    if (clone != null) return clone;
    else return this;
  }

  /** Create a shallow copy of us -- used in rewriting if necessary
   * @return a copy of us (but reuse, don't copy, our subqueries) */
  @Override @SuppressWarnings("unchecked")
  public DisjunctionMaxQuery clone() {
    DisjunctionMaxQuery clone = (DisjunctionMaxQuery)super.clone();
    clone.disjuncts = (ArrayList<Query>) this.disjuncts.clone();
    return clone;
  }

  // inherit javadoc
  @Override
  public void extractTerms(Set<Term> terms) {
    for (Query query : disjuncts) {
      query.extractTerms(terms);
    }
  }

  /** Prettyprint us.
   * @param field the field to which we are applied
   * @return a string that shows what we do, of the form "(disjunct1 | disjunct2 | ... | disjunctn)^boost"
   */
  @Override
  public String toString(String field) {
    StringBuilder buffer = new StringBuilder();
    buffer.append("(");
    int numDisjunctions = disjuncts.size();
    for (int i = 0 ; i < numDisjunctions; i++) {
      Query subquery = disjuncts.get(i);
      if (subquery instanceof BooleanQuery) {   // wrap sub-bools in parens
        buffer.append("(");
        buffer.append(subquery.toString(field));
        buffer.append(")");
      }
      else buffer.append(subquery.toString(field));
      if (i != numDisjunctions-1) buffer.append(" | ");
    }
    buffer.append(")");
    if (tieBreakerMultiplier != 0.0f) {
      buffer.append("~");
      buffer.append(tieBreakerMultiplier);
    }
    if (getBoost() != 1.0) {
      buffer.append("^");
      buffer.append(getBoost());
    }
    return buffer.toString();
  }

  /** Return true iff we represent the same query as o
   * @param o another object
   * @return true iff o is a DisjunctionMaxQuery with the same boost and the same subqueries, in the same order, as us
   */
  @Override
  public boolean equals(Object o) {
    if (! (o instanceof DisjunctionMaxQuery) ) return false;
    DisjunctionMaxQuery other = (DisjunctionMaxQuery)o;
    return this.getBoost() == other.getBoost()
            && this.tieBreakerMultiplier == other.tieBreakerMultiplier
            && this.disjuncts.equals(other.disjuncts);
  }

  /** Compute a hash code for hashing us
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Float.floatToIntBits(getBoost())
            + Float.floatToIntBits(tieBreakerMultiplier)
            + disjuncts.hashCode();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/762.java