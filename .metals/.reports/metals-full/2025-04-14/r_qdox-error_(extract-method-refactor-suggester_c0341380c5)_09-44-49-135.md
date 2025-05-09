error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2197.java
text:
```scala
t@@his.reader = lastReader[0] = reader;

package org.apache.lucene.search;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.store.RAMDirectory;
import static org.apache.lucene.util.LuceneTestCaseJ4.TEST_VERSION_CURRENT;

/**
 * Copyright 2005 Apache Software Foundation
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



public class QueryUtils {

  /** Check the types of things query objects should be able to do. */
  public static void check(Query q) {
    checkHashEquals(q);
  }

  /** check very basic hashCode and equals */
  public static void checkHashEquals(Query q) {
    Query q2 = (Query)q.clone();
    checkEqual(q,q2);

    Query q3 = (Query)q.clone();
    q3.setBoost(7.21792348f);
    checkUnequal(q,q3);

    // test that a class check is done so that no exception is thrown
    // in the implementation of equals()
    Query whacky = new Query() {
      @Override
      public String toString(String field) {
        return "My Whacky Query";
      }
    };
    whacky.setBoost(q.getBoost());
    checkUnequal(q, whacky);
  }

  public static void checkEqual(Query q1, Query q2) {
    Assert.assertEquals(q1, q2);
    Assert.assertEquals(q1.hashCode(), q2.hashCode());
  }

  public static void checkUnequal(Query q1, Query q2) {
    Assert.assertTrue(!q1.equals(q2));
    Assert.assertTrue(!q2.equals(q1));

    // possible this test can fail on a hash collision... if that
    // happens, please change test to use a different example.
    Assert.assertTrue(q1.hashCode() != q2.hashCode());
  }
  
  /** deep check that explanations of a query 'score' correctly */
  public static void checkExplanations (final Query q, final Searcher s) throws IOException {
    CheckHits.checkExplanations(q, null, s, true);
  }
  
  /** 
   * Various query sanity checks on a searcher, some checks are only done for
   * instanceof IndexSearcher.
   *
   * @see #check(Query)
   * @see #checkFirstSkipTo
   * @see #checkSkipTo
   * @see #checkExplanations
   * @see #checkSerialization
   * @see #checkEqual
   */
  public static void check(Query q1, Searcher s) {
    check(q1, s, true);
  }
  private static void check(Query q1, Searcher s, boolean wrap) {
    try {
      check(q1);
      if (s!=null) {
        if (s instanceof IndexSearcher) {
          IndexSearcher is = (IndexSearcher)s;
          checkFirstSkipTo(q1,is);
          checkSkipTo(q1,is);
          if (wrap) {
            check(q1, wrapUnderlyingReader(is, -1), false);
            check(q1, wrapUnderlyingReader(is,  0), false);
            check(q1, wrapUnderlyingReader(is, +1), false);
          }
        }
        if (wrap) {
          check(q1, wrapSearcher(s, -1), false);
          check(q1, wrapSearcher(s,  0), false);
          check(q1, wrapSearcher(s, +1), false);
        }
        checkExplanations(q1,s);
        checkSerialization(q1,s);
        
        Query q2 = (Query)q1.clone();
        checkEqual(s.rewrite(q1),
                   s.rewrite(q2));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Given an IndexSearcher, returns a new IndexSearcher whose IndexReader 
   * is a MultiReader containing the Reader of the original IndexSearcher, 
   * as well as several "empty" IndexReaders -- some of which will have 
   * deleted documents in them.  This new IndexSearcher should 
   * behave exactly the same as the original IndexSearcher.
   * @param s the searcher to wrap
   * @param edge if negative, s will be the first sub; if 0, s will be in the middle, if positive s will be the last sub
   */
  public static IndexSearcher wrapUnderlyingReader(final IndexSearcher s, final int edge) 
    throws IOException {

    IndexReader r = s.getIndexReader();

    // we can't put deleted docs before the nested reader, because
    // it will throw off the docIds
    IndexReader[] readers = new IndexReader[] {
      edge < 0 ? r : IndexReader.open(makeEmptyIndex(0), true),
      IndexReader.open(makeEmptyIndex(0), true),
      new MultiReader(new IndexReader[] {
        IndexReader.open(makeEmptyIndex(edge < 0 ? 4 : 0), true),
        IndexReader.open(makeEmptyIndex(0), true),
        0 == edge ? r : IndexReader.open(makeEmptyIndex(0), true)
      }),
      IndexReader.open(makeEmptyIndex(0 < edge ? 0 : 7), true),
      IndexReader.open(makeEmptyIndex(0), true),
      new MultiReader(new IndexReader[] {
        IndexReader.open(makeEmptyIndex(0 < edge ? 0 : 5), true),
        IndexReader.open(makeEmptyIndex(0), true),
        0 < edge ? r : IndexReader.open(makeEmptyIndex(0), true)
      })
    };
    IndexSearcher out = new IndexSearcher(new MultiReader(readers));
    out.setSimilarity(s.getSimilarity());
    return out;
  }
  /**
   * Given a Searcher, returns a new MultiSearcher wrapping the  
   * the original Searcher, 
   * as well as several "empty" IndexSearchers -- some of which will have
   * deleted documents in them.  This new MultiSearcher 
   * should behave exactly the same as the original Searcher.
   * @param s the Searcher to wrap
   * @param edge if negative, s will be the first sub; if 0, s will be in hte middle, if positive s will be the last sub
   */
  public static MultiSearcher wrapSearcher(final Searcher s, final int edge) 
    throws IOException {

    // we can't put deleted docs before the nested reader, because
    // it will through off the docIds
    Searcher[] searchers = new Searcher[] {
      edge < 0 ? s : new IndexSearcher(makeEmptyIndex(0), true),
      new MultiSearcher(new Searcher[] {
        new IndexSearcher(makeEmptyIndex(edge < 0 ? 65 : 0), true),
        new IndexSearcher(makeEmptyIndex(0), true),
        0 == edge ? s : new IndexSearcher(makeEmptyIndex(0), true)
      }),
      new IndexSearcher(makeEmptyIndex(0 < edge ? 0 : 3), true),
      new IndexSearcher(makeEmptyIndex(0), true),
      new MultiSearcher(new Searcher[] {
        new IndexSearcher(makeEmptyIndex(0 < edge ? 0 : 5), true),
        new IndexSearcher(makeEmptyIndex(0), true),
        0 < edge ? s : new IndexSearcher(makeEmptyIndex(0), true)
      })
    };
    MultiSearcher out = new MultiSearcher(searchers);
    out.setSimilarity(s.getSimilarity());
    return out;
  }

  private static RAMDirectory makeEmptyIndex(final int numDeletedDocs) 
    throws IOException {
      RAMDirectory d = new RAMDirectory();
      IndexWriter w = new IndexWriter(d, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer()));
      for (int i = 0; i < numDeletedDocs; i++) {
        w.addDocument(new Document());
      }
      w.commit();
      w.deleteDocuments( new MatchAllDocsQuery() );
      w.commit();

      if (0 < numDeletedDocs)
        Assert.assertTrue("writer has no deletions", w.hasDeletions());

      Assert.assertEquals("writer is missing some deleted docs", 
                          numDeletedDocs, w.maxDoc());
      Assert.assertEquals("writer has non-deleted docs", 
                          0, w.numDocs());
      w.close();
      IndexReader r = IndexReader.open(d, true);
      Assert.assertEquals("reader has wrong number of deleted docs", 
                          numDeletedDocs, r.numDeletedDocs());
      r.close();
      return d;
  }
  

  /** check that the query weight is serializable. 
   * @throws IOException if serialization check fail. 
   */
  private static void checkSerialization(Query q, Searcher s) throws IOException {
    Weight w = q.weight(s);
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(w);
      oos.close();
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
      ois.readObject();
      ois.close();
      
      //skip equals() test for now - most weights don't override equals() and we won't add this just for the tests.
      //TestCase.assertEquals("writeObject(w) != w.  ("+w+")",w2,w);   
      
    } catch (Exception e) {
      IOException e2 = new IOException("Serialization failed for "+w);
      e2.initCause(e);
      throw e2;
    }
  }


  /** alternate scorer skipTo(),skipTo(),next(),next(),skipTo(),skipTo(), etc
   * and ensure a hitcollector receives same docs and scores
   */
  public static void checkSkipTo(final Query q, final IndexSearcher s) throws IOException {
    //System.out.println("Checking "+q);
    
    if (q.weight(s).scoresDocsOutOfOrder()) return;  // in this case order of skipTo() might differ from that of next().

    final int skip_op = 0;
    final int next_op = 1;
    final int orders [][] = {
        {next_op},
        {skip_op},
        {skip_op, next_op},
        {next_op, skip_op},
        {skip_op, skip_op, next_op, next_op},
        {next_op, next_op, skip_op, skip_op},
        {skip_op, skip_op, skip_op, next_op, next_op},
    };
    for (int k = 0; k < orders.length; k++) {

        final int order[] = orders[k];
        // System.out.print("Order:");for (int i = 0; i < order.length; i++)
        // System.out.print(order[i]==skip_op ? " skip()":" next()");
        // System.out.println();
        final int opidx[] = { 0 };
        final int lastDoc[] = {-1};

        // FUTURE: ensure scorer.doc()==-1

        final float maxDiff = 1e-5f;
        final IndexReader lastReader[] = {null};

        s.search(q, new Collector() {
          private Scorer sc;
          private IndexReader reader;
          private Scorer scorer;

          @Override
          public void setScorer(Scorer scorer) throws IOException {
            this.sc = scorer;
          }

          @Override
          public void collect(int doc) throws IOException {
            float score = sc.score();
            lastDoc[0] = doc;
            try {
              if (scorer == null) {
                Weight w = q.weight(s);
                scorer = w.scorer(reader, true, false);
              }
              
              int op = order[(opidx[0]++) % order.length];
              // System.out.println(op==skip_op ?
              // "skip("+(sdoc[0]+1)+")":"next()");
              boolean more = op == skip_op ? scorer.advance(scorer.docID() + 1) != DocIdSetIterator.NO_MORE_DOCS
                  : scorer.nextDoc() != DocIdSetIterator.NO_MORE_DOCS;
              int scorerDoc = scorer.docID();
              float scorerScore = scorer.score();
              float scorerScore2 = scorer.score();
              float scoreDiff = Math.abs(score - scorerScore);
              float scorerDiff = Math.abs(scorerScore2 - scorerScore);
              if (!more || doc != scorerDoc || scoreDiff > maxDiff
 scorerDiff > maxDiff) {
                StringBuilder sbord = new StringBuilder();
                for (int i = 0; i < order.length; i++)
                  sbord.append(order[i] == skip_op ? " skip()" : " next()");
                throw new RuntimeException("ERROR matching docs:" + "\n\t"
                    + (doc != scorerDoc ? "--> " : "") + "doc=" + doc + ", scorerDoc=" + scorerDoc
                    + "\n\t" + (!more ? "--> " : "") + "tscorer.more=" + more
                    + "\n\t" + (scoreDiff > maxDiff ? "--> " : "")
                    + "scorerScore=" + scorerScore + " scoreDiff=" + scoreDiff
                    + " maxDiff=" + maxDiff + "\n\t"
                    + (scorerDiff > maxDiff ? "--> " : "") + "scorerScore2="
                    + scorerScore2 + " scorerDiff=" + scorerDiff
                    + "\n\thitCollector.doc=" + doc + " score=" + score
                    + "\n\t Scorer=" + scorer + "\n\t Query=" + q + "  "
                    + q.getClass().getName() + "\n\t Searcher=" + s
                    + "\n\t Order=" + sbord + "\n\t Op="
                    + (op == skip_op ? " skip()" : " next()"));
              }
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }

          @Override
          public void setNextReader(IndexReader reader, int docBase) throws IOException {
            // confirm that skipping beyond the last doc, on the
            // previous reader, hits NO_MORE_DOCS
            if (lastReader[0] != null) {
              final IndexReader previousReader = lastReader[0];
              Weight w = q.weight(new IndexSearcher(previousReader));
              Scorer scorer = w.scorer(previousReader, true, false);
              if (scorer != null) {
                boolean more = scorer.advance(lastDoc[0] + 1) != DocIdSetIterator.NO_MORE_DOCS;
                Assert.assertFalse("query's last doc was "+ lastDoc[0] +" but skipTo("+(lastDoc[0]+1)+") got to "+scorer.docID(),more);
              }
            }
            this.reader = reader;
            this.scorer = null;
            lastDoc[0] = -1;
          }

          @Override
          public boolean acceptsDocsOutOfOrder() {
            return true;
          }
        });

        if (lastReader[0] != null) {
          // confirm that skipping beyond the last doc, on the
          // previous reader, hits NO_MORE_DOCS
          final IndexReader previousReader = lastReader[0];
          Weight w = q.weight(new IndexSearcher(previousReader));
          Scorer scorer = w.scorer(previousReader, true, false);
          if (scorer != null) {
            boolean more = scorer.advance(lastDoc[0] + 1) != DocIdSetIterator.NO_MORE_DOCS;
            Assert.assertFalse("query's last doc was "+ lastDoc[0] +" but skipTo("+(lastDoc[0]+1)+") got to "+scorer.docID(),more);
          }
        }
      }
  }
    
  // check that first skip on just created scorers always goes to the right doc
  private static void checkFirstSkipTo(final Query q, final IndexSearcher s) throws IOException {
    //System.out.println("checkFirstSkipTo: "+q);
    final float maxDiff = 1e-5f;
    final int lastDoc[] = {-1};
    final IndexReader lastReader[] = {null};

    s.search(q,new Collector() {
      private Scorer scorer;
      private IndexReader reader;
      @Override
      public void setScorer(Scorer scorer) throws IOException {
        this.scorer = scorer;
      }
      @Override
      public void collect(int doc) throws IOException {
        float score = scorer.score();
        try {
          
          for (int i=lastDoc[0]+1; i<=doc; i++) {
            Weight w = q.weight(s);
            Scorer scorer = w.scorer(reader, true, false);
            Assert.assertTrue("query collected "+doc+" but skipTo("+i+") says no more docs!",scorer.advance(i) != DocIdSetIterator.NO_MORE_DOCS);
            Assert.assertEquals("query collected "+doc+" but skipTo("+i+") got to "+scorer.docID(),doc,scorer.docID());
            float skipToScore = scorer.score();
            Assert.assertEquals("unstable skipTo("+i+") score!",skipToScore,scorer.score(),maxDiff); 
            Assert.assertEquals("query assigned doc "+doc+" a score of <"+score+"> but skipTo("+i+") has <"+skipToScore+">!",score,skipToScore,maxDiff);
          }
          lastDoc[0] = doc;
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public void setNextReader(IndexReader reader, int docBase) throws IOException {
        // confirm that skipping beyond the last doc, on the
        // previous reader, hits NO_MORE_DOCS
        if (lastReader[0] != null) {
          final IndexReader previousReader = lastReader[0];
          Weight w = q.weight(new IndexSearcher(previousReader));
          Scorer scorer = w.scorer(previousReader, true, false);
          if (scorer != null) {
            boolean more = scorer.advance(lastDoc[0] + 1) != DocIdSetIterator.NO_MORE_DOCS;
            Assert.assertFalse("query's last doc was "+ lastDoc[0] +" but skipTo("+(lastDoc[0]+1)+") got to "+scorer.docID(),more);
          }
        }

        this.reader = lastReader[0] = reader;
        lastDoc[0] = -1;
      }
      @Override
      public boolean acceptsDocsOutOfOrder() {
        return false;
      }
    });

    if (lastReader[0] != null) {
      // confirm that skipping beyond the last doc, on the
      // previous reader, hits NO_MORE_DOCS
      final IndexReader previousReader = lastReader[0];
      Weight w = q.weight(new IndexSearcher(previousReader));
      Scorer scorer = w.scorer(previousReader, true, false);
      if (scorer != null) {
        boolean more = scorer.advance(lastDoc[0] + 1) != DocIdSetIterator.NO_MORE_DOCS;
        Assert.assertFalse("query's last doc was "+ lastDoc[0] +" but skipTo("+(lastDoc[0]+1)+") got to "+scorer.docID(),more);
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2197.java