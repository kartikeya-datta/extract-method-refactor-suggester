error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1812.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1812.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1812.java
text:
```scala
t@@his.qWeight = q.createWeight(searcher);

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

package org.apache.solr.search.function;

import org.apache.lucene.search.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.util.ToStringUtils;

import java.io.IOException;
import java.util.Set;
import java.util.Map;

/**
 * Query that is boosted by a ValueSource
 */
public class BoostedQuery extends Query {
  private Query q;
  private ValueSource boostVal; // optional, can be null

  public BoostedQuery(Query subQuery, ValueSource boostVal) {
    this.q = subQuery;
    this.boostVal = boostVal;
  }

  public Query getQuery() { return q; }
  public ValueSource getValueSource() { return boostVal; }

  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    Query newQ = q.rewrite(reader);
    if (newQ == q) return this;
    BoostedQuery bq = (BoostedQuery)this.clone();
    bq.q = newQ;
    return bq;
  }

  @Override
  public void extractTerms(Set terms) {
    q.extractTerms(terms);
  }

  @Override
  public Weight createWeight(IndexSearcher searcher) throws IOException {
    return new BoostedQuery.BoostedWeight(searcher);
  }

  private class BoostedWeight extends Weight {
    IndexSearcher searcher;
    Weight qWeight;
    Map fcontext;

    public BoostedWeight(IndexSearcher searcher) throws IOException {
      this.searcher = searcher;
      this.qWeight = q.weight(searcher);
      this.fcontext = boostVal.newContext(searcher);
      boostVal.createWeight(fcontext,searcher);
    }

    @Override
    public Query getQuery() {
      return BoostedQuery.this;
    }

    @Override
    public float getValue() {
      return getBoost();
    }

    @Override
    public float sumOfSquaredWeights() throws IOException {
      float sum = qWeight.sumOfSquaredWeights();
      sum *= getBoost() * getBoost();
      return sum ;
    }

    @Override
    public void normalize(float norm) {
      norm *= getBoost();
      qWeight.normalize(norm);
    }

    @Override
    public Scorer scorer(AtomicReaderContext context, ScorerContext scorerContext) throws IOException {
      Scorer subQueryScorer = qWeight.scorer(context, ScorerContext.def());
      if(subQueryScorer == null) {
        return null;
      }
      return new BoostedQuery.CustomScorer(context, this, subQueryScorer, boostVal);
    }

    @Override
    public Explanation explain(AtomicReaderContext readerContext, int doc) throws IOException {
      Explanation subQueryExpl = qWeight.explain(readerContext,doc);
      if (!subQueryExpl.isMatch()) {
        return subQueryExpl;
      }
      DocValues vals = boostVal.getValues(fcontext, readerContext);
      float sc = subQueryExpl.getValue() * vals.floatVal(doc);
      Explanation res = new ComplexExplanation(
        true, sc, BoostedQuery.this.toString() + ", product of:");
      res.addDetail(subQueryExpl);
      res.addDetail(vals.explain(doc));
      return res;
    }
  }


  private class CustomScorer extends Scorer {
    private final BoostedQuery.BoostedWeight weight;
    private final float qWeight;
    private final Scorer scorer;
    private final DocValues vals;
    private final AtomicReaderContext readerContext;

    private CustomScorer(AtomicReaderContext readerContext, BoostedQuery.BoostedWeight w,
        Scorer scorer, ValueSource vs) throws IOException {
      super(w);
      this.weight = w;
      this.qWeight = w.getValue();
      this.scorer = scorer;
      this.readerContext = readerContext;
      this.vals = vs.getValues(weight.fcontext, readerContext);
    }

    @Override
    public int docID() {
      return scorer.docID();
    }

    @Override
    public int advance(int target) throws IOException {
      return scorer.advance(target);
    }

    @Override
    public int nextDoc() throws IOException {
      return scorer.nextDoc();
    }

    @Override   
    public float score() throws IOException {
      float score = qWeight * scorer.score() * vals.floatVal(scorer.docID());

      // Current Lucene priority queues can't handle NaN and -Infinity, so
      // map to -Float.MAX_VALUE. This conditional handles both -infinity
      // and NaN since comparisons with NaN are always false.
      return score>Float.NEGATIVE_INFINITY ? score : -Float.MAX_VALUE;
    }

    public Explanation explain(int doc) throws IOException {
      Explanation subQueryExpl = weight.qWeight.explain(readerContext ,doc);
      if (!subQueryExpl.isMatch()) {
        return subQueryExpl;
      }
      float sc = subQueryExpl.getValue() * vals.floatVal(doc);
      Explanation res = new ComplexExplanation(
        true, sc, BoostedQuery.this.toString() + ", product of:");
      res.addDetail(subQueryExpl);
      res.addDetail(vals.explain(doc));
      return res;
    }
  }


  @Override
  public String toString(String field) {
    StringBuilder sb = new StringBuilder();
    sb.append("boost(").append(q.toString(field)).append(',').append(boostVal).append(')');
    sb.append(ToStringUtils.boost(getBoost()));
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (getClass() != o.getClass()) return false;
    BoostedQuery other = (BoostedQuery)o;
    return this.getBoost() == other.getBoost()
           && this.q.equals(other.q)
           && this.boostVal.equals(other.boostVal);
  }

  @Override
  public int hashCode() {
    int h = q.hashCode();
    h ^= (h << 17) | (h >>> 16);
    h += boostVal.hashCode();
    h ^= (h << 8) | (h >>> 25);
    h += Float.floatToIntBits(getBoost());
    return h;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1812.java