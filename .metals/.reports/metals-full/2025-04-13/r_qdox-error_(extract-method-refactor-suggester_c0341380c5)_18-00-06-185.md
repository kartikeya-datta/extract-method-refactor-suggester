error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1855.java
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
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause.Occur;

/* Description from Doug Cutting (excerpted from
 * LUCENE-1483):
 *
 * BooleanScorer uses a ~16k array to score windows of
 * docs. So it scores docs 0-16k first, then docs 16-32k,
 * etc. For each window it iterates through all query terms
 * and accumulates a score in table[doc%16k]. It also stores
 * in the table a bitmask representing which terms
 * contributed to the score. Non-zero scores are chained in
 * a linked list. At the end of scoring each window it then
 * iterates through the linked list and, if the bitmask
 * matches the boolean constraints, collects a hit. For
 * boolean queries with lots of frequent terms this can be
 * much faster, since it does not need to update a priority
 * queue for each posting, instead performing constant-time
 * operations per posting. The only downside is that it
 * results in hits being delivered out-of-order within the
 * window, which means it cannot be nested within other
 * scorers. But it works well as a top-level scorer.
 *
 * The new BooleanScorer2 implementation instead works by
 * merging priority queues of postings, albeit with some
 * clever tricks. For example, a pure conjunction (all terms
 * required) does not require a priority queue. Instead it
 * sorts the posting streams at the start, then repeatedly
 * skips the first to to the last. If the first ever equals
 * the last, then there's a hit. When some terms are
 * required and some terms are optional, the conjunction can
 * be evaluated first, then the optional terms can all skip
 * to the match and be added to the score. Thus the
 * conjunction can reduce the number of priority queue
 * updates for the optional terms. */

final class BooleanScorer extends Scorer {
  
  private static final class BooleanScorerCollector extends Collector {
    private BucketTable bucketTable;
    private int mask;
    private Scorer scorer;
    
    public BooleanScorerCollector(int mask, BucketTable bucketTable) {
      this.mask = mask;
      this.bucketTable = bucketTable;
    }
    
    @Override
    public void collect(final int doc) throws IOException {
      final BucketTable table = bucketTable;
      final int i = doc & BucketTable.MASK;
      Bucket bucket = table.buckets[i];
      if (bucket == null)
        table.buckets[i] = bucket = new Bucket();
      
      if (bucket.doc != doc) {                    // invalid bucket
        bucket.doc = doc;                         // set doc
        bucket.score = scorer.score();            // initialize score
        bucket.bits = mask;                       // initialize mask
        bucket.coord = 1;                         // initialize coord

        bucket.next = table.first;                // push onto valid list
        table.first = bucket;
      } else {                                    // valid bucket
        bucket.score += scorer.score();           // increment score
        bucket.bits |= mask;                      // add bits in mask
        bucket.coord++;                           // increment coord
      }
    }
    
    @Override
    public void setNextReader(IndexReader reader, int docBase) {
      // not needed by this implementation
    }
    
    @Override
    public void setScorer(Scorer scorer) throws IOException {
      this.scorer = scorer;
    }
    
    @Override
    public boolean acceptsDocsOutOfOrder() {
      return true;
    }

  }
  
  // An internal class which is used in score(Collector, int) for setting the
  // current score. This is required since Collector exposes a setScorer method
  // and implementations that need the score will call scorer.score().
  // Therefore the only methods that are implemented are score() and doc().
  private static final class BucketScorer extends Scorer {

    float score;
    int doc = NO_MORE_DOCS;
    int freq;
    
    public BucketScorer() { super(null); }
    
    @Override
    public int advance(int target) throws IOException { return NO_MORE_DOCS; }

    @Override
    public int docID() { return doc; }

    @Override
    public float freq() { return freq; }

    @Override
    public int nextDoc() throws IOException { return NO_MORE_DOCS; }
    
    @Override
    public float score() throws IOException { return score; }
    
  }

  static final class Bucket {
    int doc = -1;            // tells if bucket is valid
    float score;             // incremental score
    int bits;                // used for bool constraints
    int coord;               // count of terms in score
    Bucket next;             // next valid bucket
  }
  
  /** A simple hash table of document scores within a range. */
  static final class BucketTable {
    public static final int SIZE = 1 << 11;
    public static final int MASK = SIZE - 1;

    final Bucket[] buckets = new Bucket[SIZE];
    Bucket first = null;                          // head of valid list
  
    public BucketTable() {}

    public Collector newCollector(int mask) {
      return new BooleanScorerCollector(mask, this);
    }

    public int size() { return SIZE; }
  }

  static final class SubScorer {
    public Scorer scorer;
    // TODO: re-enable this if BQ ever sends us required clauses
    //public boolean required = false;
    public boolean prohibited = false;
    public Collector collector;
    public SubScorer next;

    public SubScorer(Scorer scorer, boolean required, boolean prohibited,
        Collector collector, SubScorer next)
      throws IOException {
      if (required) {
        throw new IllegalArgumentException("this scorer cannot handle required=true");
      }
      this.scorer = scorer;
      // TODO: re-enable this if BQ ever sends us required clauses
      //this.required = required;
      this.prohibited = prohibited;
      this.collector = collector;
      this.next = next;
    }
  }
  
  private SubScorer scorers = null;
  private BucketTable bucketTable = new BucketTable();
  private final float[] coordFactors;
  // TODO: re-enable this if BQ ever sends us required clauses
  //private int requiredMask = 0;
  private int prohibitedMask = 0;
  private int nextMask = 1;
  private final int minNrShouldMatch;
  private int end;
  private Bucket current;
  private int doc = -1;
  
  BooleanScorer(Weight weight, boolean disableCoord, Similarity similarity, int minNrShouldMatch,
      List<Scorer> optionalScorers, List<Scorer> prohibitedScorers, int maxCoord) throws IOException {
    super(null, weight);   // Similarity not used
    this.minNrShouldMatch = minNrShouldMatch;

    if (optionalScorers != null && optionalScorers.size() > 0) {
      for (Scorer scorer : optionalScorers) {
        if (scorer.nextDoc() != NO_MORE_DOCS) {
          scorers = new SubScorer(scorer, false, false, bucketTable.newCollector(0), scorers);
        }
      }
    }
    
    if (prohibitedScorers != null && prohibitedScorers.size() > 0) {
      for (Scorer scorer : prohibitedScorers) {
        int mask = nextMask;
        nextMask = nextMask << 1;
        prohibitedMask |= mask;                     // update prohibited mask
        if (scorer.nextDoc() != NO_MORE_DOCS) {
          scorers = new SubScorer(scorer, false, true, bucketTable.newCollector(mask), scorers);
        }
      }
    }

    coordFactors = new float[optionalScorers.size() + 1];
    for (int i = 0; i < coordFactors.length; i++) {
      coordFactors[i] = disableCoord ? 1.0f : similarity.coord(i, maxCoord); 
    }
  }

  // firstDocID is ignored since nextDoc() initializes 'current'
  @Override
  protected boolean score(Collector collector, int max, int firstDocID) throws IOException {
    boolean more;
    Bucket tmp;
    BucketScorer bs = new BucketScorer();
    // The internal loop will set the score and doc before calling collect.
    collector.setScorer(bs);
    do {
      bucketTable.first = null;
      
      while (current != null) {         // more queued 

        // check prohibited & required
        if ((current.bits & prohibitedMask) == 0) {

            // TODO: re-enable this if BQ ever sends us required
            // clauses
            //&& (current.bits & requiredMask) == requiredMask) {
          
          if (current.doc >= max){
            tmp = current;
            current = current.next;
            tmp.next = bucketTable.first;
            bucketTable.first = tmp;
            continue;
          }
          
          if (current.coord >= minNrShouldMatch) {
            bs.score = current.score * coordFactors[current.coord];
            bs.doc = current.doc;
            bs.freq = current.coord;
            collector.collect(current.doc);
          }
        }
        
        current = current.next;         // pop the queue
      }
      
      if (bucketTable.first != null){
        current = bucketTable.first;
        bucketTable.first = current.next;
        return true;
      }

      // refill the queue
      more = false;
      end += BucketTable.SIZE;
      for (SubScorer sub = scorers; sub != null; sub = sub.next) {
        int subScorerDocID = sub.scorer.docID();
        if (subScorerDocID != NO_MORE_DOCS) {
          more |= sub.scorer.score(sub.collector, end, subScorerDocID);
        }
      }
      current = bucketTable.first;
      
    } while (current != null || more);

    return false;
  }
  
  @Override
  public int advance(int target) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int docID() {
    return doc;
  }

  @Override
  public int nextDoc() throws IOException {
    boolean more;
    do {
      while (bucketTable.first != null) {         // more queued
        current = bucketTable.first;
        bucketTable.first = current.next;         // pop the queue

        // check prohibited & required, and minNrShouldMatch
        if ((current.bits & prohibitedMask) == 0 &&
            current.coord >= minNrShouldMatch) {
          // TODO: re-enable this if BQ ever sends us required clauses
          // (current.bits & requiredMask) == requiredMask &&
          return doc = current.doc;
        }
      }

      // refill the queue
      more = false;
      end += BucketTable.SIZE;
      for (SubScorer sub = scorers; sub != null; sub = sub.next) {
        int subScorerDocID = sub.scorer.docID();
        if (subScorerDocID != NO_MORE_DOCS) {
          more |= sub.scorer.score(sub.collector, end, subScorerDocID);
        }
      }
    } while (bucketTable.first != null || more);

    return doc = NO_MORE_DOCS;
  }

  @Override
  public float score() {
    return current.score * coordFactors[current.coord];
  }

  @Override
  public void score(Collector collector) throws IOException {
    score(collector, Integer.MAX_VALUE, nextDoc());
  }
  
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("boolean(");
    for (SubScorer sub = scorers; sub != null; sub = sub.next) {
      buffer.append(sub.scorer.toString());
      buffer.append(" ");
    }
    buffer.append(")");
    return buffer.toString();
  }
  
  @Override
  protected void visitSubScorers(Query parent, Occur relationship, ScorerVisitor<Query, Query, Scorer> visitor) {
    super.visitSubScorers(parent, relationship, visitor);
    final Query q = weight.getQuery();
    SubScorer sub = scorers;
    while(sub != null) {
      // TODO: re-enable this if BQ ever sends us required
      //clauses
      //if (sub.required) {
      //relationship = Occur.MUST;
      if (!sub.prohibited) {
        relationship = Occur.SHOULD;
      } else {
        // TODO: maybe it's pointless to do this, but, it is
        // possible the doc may still be collected, eg foo
        // OR (bar -fee)
        relationship = Occur.MUST_NOT;
      }
      sub.scorer.visitSubScorers(q, relationship, visitor);
      sub = sub.next;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1855.java