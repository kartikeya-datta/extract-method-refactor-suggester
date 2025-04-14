error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/461.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/461.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/461.java
text:
```scala
public v@@oid setNextEnum(TermsEnum termsEnum) {

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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermContext;
import org.apache.lucene.index.TermState;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.MultiTermQuery.RewriteMethod;

import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.ByteBlockPool;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefHash;
import org.apache.lucene.util.RamUsageEstimator;
import org.apache.lucene.util.BytesRefHash.DirectBytesStartArray;

/** @lucene.internal Only public to be accessible by spans package. */
public abstract class ScoringRewrite<Q extends Query> extends TermCollectingRewrite<Q> {

  /** A rewrite method that first translates each term into
   *  {@link BooleanClause.Occur#SHOULD} clause in a
   *  BooleanQuery, and keeps the scores as computed by the
   *  query.  Note that typically such scores are
   *  meaningless to the user, and require non-trivial CPU
   *  to compute, so it's almost always better to use {@link
   *  MultiTermQuery#CONSTANT_SCORE_AUTO_REWRITE_DEFAULT} instead.
   *
   *  <p><b>NOTE</b>: This rewrite method will hit {@link
   *  BooleanQuery.TooManyClauses} if the number of terms
   *  exceeds {@link BooleanQuery#getMaxClauseCount}.
   *
   *  @see MultiTermQuery#setRewriteMethod */
  public final static ScoringRewrite<BooleanQuery> SCORING_BOOLEAN_QUERY_REWRITE = new ScoringRewrite<BooleanQuery>() {
    @Override
    protected BooleanQuery getTopLevelQuery() {
      return new BooleanQuery(true);
    }
    
    @Override
    protected void addClause(BooleanQuery topLevel, Term term, int docCount,
        float boost, TermContext states) {
      final TermQuery tq = new TermQuery(term, states);
      tq.setBoost(boost);
      topLevel.add(tq, BooleanClause.Occur.SHOULD);
    }
    
    @Override
    protected void checkMaxClauseCount(int count) {
      if (count > BooleanQuery.getMaxClauseCount())
        throw new BooleanQuery.TooManyClauses();
    }
  };
  
  /** Like {@link #SCORING_BOOLEAN_QUERY_REWRITE} except
   *  scores are not computed.  Instead, each matching
   *  document receives a constant score equal to the
   *  query's boost.
   * 
   *  <p><b>NOTE</b>: This rewrite method will hit {@link
   *  BooleanQuery.TooManyClauses} if the number of terms
   *  exceeds {@link BooleanQuery#getMaxClauseCount}.
   *
   *  @see MultiTermQuery#setRewriteMethod */
  public final static RewriteMethod CONSTANT_SCORE_BOOLEAN_QUERY_REWRITE = new RewriteMethod() {
    @Override
    public Query rewrite(IndexReader reader, MultiTermQuery query) throws IOException {
      final BooleanQuery bq = SCORING_BOOLEAN_QUERY_REWRITE.rewrite(reader, query);
      // TODO: if empty boolean query return NullQuery?
      if (bq.clauses().isEmpty())
        return bq;
      // strip the scores off
      final Query result = new ConstantScoreQuery(bq);
      result.setBoost(query.getBoost());
      return result;
    }
  };

  /** This method is called after every new term to check if the number of max clauses
   * (e.g. in BooleanQuery) is not exceeded. Throws the corresponding {@link RuntimeException}. */
  protected abstract void checkMaxClauseCount(int count) throws IOException;
  
  @Override
  public final Q rewrite(final IndexReader reader, final MultiTermQuery query) throws IOException {
    final Q result = getTopLevelQuery();
    final ParallelArraysTermCollector col = new ParallelArraysTermCollector();
    collectTerms(reader, query, col);
    
    final int size = col.terms.size();
    if (size > 0) {
      final int sort[] = col.terms.sort(col.termsEnum.getComparator());
      final float[] boost = col.array.boost;
      final TermContext[] termStates = col.array.termState;
      for (int i = 0; i < size; i++) {
        final int pos = sort[i];
        final Term term = new Term(query.getField(), col.terms.get(pos, new BytesRef()));
        assert reader.docFreq(term) == termStates[pos].docFreq();
        addClause(result, term, termStates[pos].docFreq(), query.getBoost() * boost[pos], termStates[pos]);
      }
    }
    return result;
  }

  final class ParallelArraysTermCollector extends TermCollector {
    final TermFreqBoostByteStart array = new TermFreqBoostByteStart(16);
    final BytesRefHash terms = new BytesRefHash(new ByteBlockPool(new ByteBlockPool.DirectAllocator()), 16, array);
    TermsEnum termsEnum;

    private BoostAttribute boostAtt;
    
    @Override
    public void setNextEnum(TermsEnum termsEnum) throws IOException {
      this.termsEnum = termsEnum;
      this.boostAtt = termsEnum.attributes().addAttribute(BoostAttribute.class);
    }
  
    @Override
    public boolean collect(BytesRef bytes) throws IOException {
      final int e = terms.add(bytes);
      final TermState state = termsEnum.termState();
      assert state != null; 
      if (e < 0 ) {
        // duplicate term: update docFreq
        final int pos = (-e)-1;
        array.termState[pos].register(state, readerContext.ord, termsEnum.docFreq(), termsEnum.totalTermFreq());
        assert array.boost[pos] == boostAtt.getBoost() : "boost should be equal in all segment TermsEnums";
      } else {
        // new entry: we populate the entry initially
        array.boost[e] = boostAtt.getBoost();
        array.termState[e] = new TermContext(topReaderContext, state, readerContext.ord, termsEnum.docFreq(), termsEnum.totalTermFreq());
        ScoringRewrite.this.checkMaxClauseCount(terms.size());
      }
      return true;
    }
  }
  
  /** Special implementation of BytesStartArray that keeps parallel arrays for boost and docFreq */
  static final class TermFreqBoostByteStart extends DirectBytesStartArray  {
    float[] boost;
    TermContext[] termState;
    
    public TermFreqBoostByteStart(int initSize) {
      super(initSize);
    }

    @Override
    public int[] init() {
      final int[] ord = super.init();
      boost = new float[ArrayUtil.oversize(ord.length, RamUsageEstimator.NUM_BYTES_FLOAT)];
      termState = new TermContext[ArrayUtil.oversize(ord.length, RamUsageEstimator.NUM_BYTES_OBJECT_REF)];
      assert termState.length >= ord.length && boost.length >= ord.length;
      return ord;
    }

    @Override
    public int[] grow() {
      final int[] ord = super.grow();
      boost = ArrayUtil.grow(boost, ord.length);
      if (termState.length < ord.length) {
        TermContext[] tmpTermState = new TermContext[ArrayUtil.oversize(ord.length, RamUsageEstimator.NUM_BYTES_OBJECT_REF)];
        System.arraycopy(termState, 0, tmpTermState, 0, termState.length);
        termState = tmpTermState;
      }     
      assert termState.length >= ord.length && boost.length >= ord.length;
      return ord;
    }

    @Override
    public int[] clear() {
     boost = null;
     termState = null;
     return super.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/461.java