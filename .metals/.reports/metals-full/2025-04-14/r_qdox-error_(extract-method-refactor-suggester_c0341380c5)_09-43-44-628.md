error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3356.java
text:
```scala
final D@@ocIdSet parents = parentsFilter.getDocIdSet(readerContext, readerContext.reader.getLiveDocs());

package org.apache.lucene.search.join;

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
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.lucene.index.IndexReader.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;       // javadocs
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Scorer.ChildScorer;
import org.apache.lucene.search.Weight;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.FixedBitSet;

/**
 * This query requires that you index
 * children and parent docs as a single block, using the
 * {@link IndexWriter#addDocuments} or {@link
 * IndexWriter#updateDocuments} API.  In each block, the
 * child documents must appear first, ending with the parent
 * document.  At search time you provide a Filter
 * identifying the parents, however this Filter must provide
 * an {@link FixedBitSet} per sub-reader.
 *
 * <p>Once the block index is built, use this query to wrap
 * any sub-query matching only child docs and join matches in that
 * child document space up to the parent document space.
 * You can then use this Query as a clause with
 * other queries in the parent document space.</p>
 *
 * <p>The child documents must be orthogonal to the parent
 * documents: the wrapped child query must never
 * return a parent document.</p>
 *
 * If you'd like to retrieve {@link TopGroups} for the
 * resulting query, use the {@link BlockJoinCollector}.
 * Note that this is not necessary, ie, if you simply want
 * to collect the parent documents and don't need to see
 * which child documents matched under that parent, then
 * you can use any collector.
 *
 * <p><b>NOTE</b>: If the overall query contains parent-only
 * matches, for example you OR a parent-only query with a
 * joined child-only query, then the resulting collected documents
 * will be correct, however the {@link TopGroups} you get
 * from {@link BlockJoinCollector} will not contain every
 * child for parents that had matched.
 *
 * <p>See {@link org.apache.lucene.search.join} for an
 * overview. </p>
 *
 * @lucene.experimental
 */

public class BlockJoinQuery extends Query {

  public static enum ScoreMode {None, Avg, Max, Total};

  private final Filter parentsFilter;
  private final Query childQuery;

  // If we are rewritten, this is the original childQuery we
  // were passed; we use this for .equals() and
  // .hashCode().  This makes rewritten query equal the
  // original, so that user does not have to .rewrite() their
  // query before searching:
  private final Query origChildQuery;
  private final ScoreMode scoreMode;

  public BlockJoinQuery(Query childQuery, Filter parentsFilter, ScoreMode scoreMode) {
    super();
    this.origChildQuery = childQuery;
    this.childQuery = childQuery;
    this.parentsFilter = parentsFilter;
    this.scoreMode = scoreMode;
  }

  private BlockJoinQuery(Query origChildQuery, Query childQuery, Filter parentsFilter, ScoreMode scoreMode) {
    super();
    this.origChildQuery = origChildQuery;
    this.childQuery = childQuery;
    this.parentsFilter = parentsFilter;
    this.scoreMode = scoreMode;
  }

  @Override
  public Weight createWeight(IndexSearcher searcher) throws IOException {
    return new BlockJoinWeight(this, childQuery.createWeight(searcher), parentsFilter, scoreMode);
  }

  private static class BlockJoinWeight extends Weight {
    private final Query joinQuery;
    private final Weight childWeight;
    private final Filter parentsFilter;
    private final ScoreMode scoreMode;

    public BlockJoinWeight(Query joinQuery, Weight childWeight, Filter parentsFilter, ScoreMode scoreMode) {
      super();
      this.joinQuery = joinQuery;
      this.childWeight = childWeight;
      this.parentsFilter = parentsFilter;
      this.scoreMode = scoreMode;
    }

    @Override
    public Query getQuery() {
      return joinQuery;
    }

    @Override
    public float getValueForNormalization() throws IOException {
      return childWeight.getValueForNormalization() * joinQuery.getBoost() * joinQuery.getBoost();
    }

    @Override
    public void normalize(float norm, float topLevelBoost) {
      childWeight.normalize(norm, topLevelBoost * joinQuery.getBoost());
    }

    @Override
    public Scorer scorer(AtomicReaderContext readerContext, boolean scoreDocsInOrder,
        boolean topScorer, Bits acceptDocs) throws IOException {
      // Pass scoreDocsInOrder true, topScorer false to our sub:
      final Scorer childScorer = childWeight.scorer(readerContext, true, false, acceptDocs);

      if (childScorer == null) {
        // No matches
        return null;
      }

      final int firstChildDoc = childScorer.nextDoc();
      if (firstChildDoc == DocIdSetIterator.NO_MORE_DOCS) {
        // No matches
        return null;
      }

      final DocIdSet parents = parentsFilter.getDocIdSet(readerContext);
      // TODO: once we do random-access filters we can
      // generalize this:
      if (parents == null) {
        // No matches
        return null;
      }
      if (!(parents instanceof FixedBitSet)) {
        throw new IllegalStateException("parentFilter must return FixedBitSet; got " + parents);
      }

      return new BlockJoinScorer(this, childScorer, (FixedBitSet) parents, firstChildDoc, scoreMode);
    }

    @Override
    public Explanation explain(AtomicReaderContext reader, int doc) throws IOException {
      // TODO
      throw new UnsupportedOperationException(getClass().getName() +
                                              " cannot explain match on parent document");
    }

    @Override
    public boolean scoresDocsOutOfOrder() {
      return false;
    }
  }

  static class BlockJoinScorer extends Scorer {
    private final Scorer childScorer;
    private final FixedBitSet parentBits;
    private final ScoreMode scoreMode;
    private int parentDoc;
    private float parentScore;
    private int nextChildDoc;

    private int[] pendingChildDocs = new int[5];
    private float[] pendingChildScores;
    private int childDocUpto;

    public BlockJoinScorer(Weight weight, Scorer childScorer, FixedBitSet parentBits, int firstChildDoc, ScoreMode scoreMode) {
      super(weight);
      //System.out.println("Q.init firstChildDoc=" + firstChildDoc);
      this.parentBits = parentBits;
      this.childScorer = childScorer;
      this.scoreMode = scoreMode;
      if (scoreMode != ScoreMode.None) {
        pendingChildScores = new float[5];
      }
      nextChildDoc = firstChildDoc;
    }

    @Override
    public Collection<ChildScorer> getChildren() {
      return Collections.singletonList(new ChildScorer(childScorer, "BLOCK_JOIN"));
    }

    int getChildCount() {
      return childDocUpto;
    }

    int[] swapChildDocs(int[] other) {
      final int[] ret = pendingChildDocs;
      if (other == null) {
        pendingChildDocs = new int[5];
      } else {
        pendingChildDocs = other;
      }
      return ret;
    }

    float[] swapChildScores(float[] other) {
      if (scoreMode == ScoreMode.None) {
        throw new IllegalStateException("ScoreMode is None");
      }
      final float[] ret = pendingChildScores;
      if (other == null) {
        pendingChildScores = new float[5];
      } else {
        pendingChildScores = other;
      }
      return ret;
    }

    @Override
    public int nextDoc() throws IOException {
      //System.out.println("Q.nextDoc() nextChildDoc=" + nextChildDoc);

      if (nextChildDoc == NO_MORE_DOCS) {
        //System.out.println("  end");
        return parentDoc = NO_MORE_DOCS;
      }

      // Gather all children sharing the same parent as nextChildDoc
      parentDoc = parentBits.nextSetBit(nextChildDoc);
      //System.out.println("  parentDoc=" + parentDoc);
      assert parentDoc != -1;

      float totalScore = 0;
      float maxScore = Float.NEGATIVE_INFINITY;

      childDocUpto = 0;
      do {
        //System.out.println("  c=" + nextChildDoc);
        if (pendingChildDocs.length == childDocUpto) {
          pendingChildDocs = ArrayUtil.grow(pendingChildDocs);
          if (scoreMode != ScoreMode.None) {
            pendingChildScores = ArrayUtil.grow(pendingChildScores);
          }
        }
        pendingChildDocs[childDocUpto] = nextChildDoc;
        if (scoreMode != ScoreMode.None) {
          // TODO: specialize this into dedicated classes per-scoreMode
          final float childScore = childScorer.score();
          pendingChildScores[childDocUpto] = childScore;
          maxScore = Math.max(childScore, maxScore);
          totalScore += childScore;
        }
        childDocUpto++;
        nextChildDoc = childScorer.nextDoc();
      } while (nextChildDoc < parentDoc);
      //System.out.println("  nextChildDoc=" + nextChildDoc);

      // Parent & child docs are supposed to be orthogonal:
      assert nextChildDoc != parentDoc;

      switch(scoreMode) {
      case Avg:
        parentScore = totalScore / childDocUpto;
        break;
      case Max:
        parentScore = maxScore;
        break;
      case Total:
        parentScore = totalScore;
        break;
      case None:
        break;
      }

      //System.out.println("  return parentDoc=" + parentDoc);
      return parentDoc;
    }

    @Override
    public int docID() {
      return parentDoc;
    }

    @Override
    public float score() throws IOException {
      return parentScore;
    }

    @Override
    public int advance(int parentTarget) throws IOException {

      //System.out.println("Q.advance parentTarget=" + parentTarget);
      if (parentTarget == NO_MORE_DOCS) {
        return parentDoc = NO_MORE_DOCS;
      }

      // Every parent must have at least one child:
      assert parentTarget != 0;

      final int prevParentDoc = parentBits.prevSetBit(parentTarget-1);

      //System.out.println("  rolled back to prevParentDoc=" + prevParentDoc + " vs parentDoc=" + parentDoc);
      assert prevParentDoc >= parentDoc;
      if (prevParentDoc > nextChildDoc) {
        nextChildDoc = childScorer.advance(prevParentDoc);
        // System.out.println("  childScorer advanced to child docID=" + nextChildDoc);
      //} else {
        //System.out.println("  skip childScorer advance");
      }

      // Parent & child docs are supposed to be orthogonal:
      assert nextChildDoc != prevParentDoc;

      final int nd = nextDoc();
      //System.out.println("  return nextParentDoc=" + nd);
      return nd;
    }
  }

  @Override
  public void extractTerms(Set<Term> terms) {
    childQuery.extractTerms(terms);
  }

  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    final Query childRewrite = childQuery.rewrite(reader);
    if (childRewrite != childQuery) {
      Query rewritten = new BlockJoinQuery(childQuery,
                                childRewrite,
                                parentsFilter,
                                scoreMode);
      rewritten.setBoost(getBoost());
      return rewritten;
    } else {
      return this;
    }
  }

  @Override
  public String toString(String field) {
    return "BlockJoinQuery ("+childQuery.toString()+")";
  }

  @Override
  public boolean equals(Object _other) {
    if (_other instanceof BlockJoinQuery) {
      final BlockJoinQuery other = (BlockJoinQuery) _other;
      return origChildQuery.equals(other.origChildQuery) &&
        parentsFilter.equals(other.parentsFilter) &&
        scoreMode == other.scoreMode;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 1;
    hash = prime * hash + origChildQuery.hashCode();
    hash = prime * hash + scoreMode.hashCode();
    hash = prime * hash + parentsFilter.hashCode();
    return hash;
  }

  @Override
  public Object clone() {
    return new BlockJoinQuery((Query) origChildQuery.clone(),
                              parentsFilter,
                              scoreMode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3356.java