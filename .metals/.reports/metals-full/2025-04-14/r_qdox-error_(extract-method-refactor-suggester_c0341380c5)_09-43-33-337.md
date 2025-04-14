error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3100.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3100.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3100.java
text:
```scala
w@@riter.shutdown(); swriter1.shutdown(); swriter2.shutdown();

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;

public class TestMultiTermQueryRewrites extends LuceneTestCase {

  static Directory dir, sdir1, sdir2;
  static IndexReader reader, multiReader, multiReaderDupls;
  static IndexSearcher searcher, multiSearcher, multiSearcherDupls;

  @BeforeClass
  public static void beforeClass() throws Exception {
    dir = newDirectory();
    sdir1 = newDirectory();
    sdir2 = newDirectory();
    final RandomIndexWriter writer = new RandomIndexWriter(random(), dir, new MockAnalyzer(random()));
    final RandomIndexWriter swriter1 = new RandomIndexWriter(random(), sdir1, new MockAnalyzer(random()));
    final RandomIndexWriter swriter2 = new RandomIndexWriter(random(), sdir2, new MockAnalyzer(random()));

    for (int i = 0; i < 10; i++) {
      Document doc = new Document();
      doc.add(newStringField("data", Integer.toString(i), Field.Store.NO));
      writer.addDocument(doc);
      ((i % 2 == 0) ? swriter1 : swriter2).addDocument(doc);
    }
    writer.forceMerge(1); swriter1.forceMerge(1); swriter2.forceMerge(1);
    writer.close(); swriter1.close(); swriter2.close();
    
    reader = DirectoryReader.open(dir);
    searcher = newSearcher(reader);
    
    multiReader = new MultiReader(new IndexReader[] {
      DirectoryReader.open(sdir1), DirectoryReader.open(sdir2) 
    }, true);
    multiSearcher = newSearcher(multiReader);
    
    multiReaderDupls = new MultiReader(new IndexReader[] {
      DirectoryReader.open(sdir1), DirectoryReader.open(dir) 
    }, true);
    multiSearcherDupls = newSearcher(multiReaderDupls);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    reader.close();
    multiReader.close();
    multiReaderDupls.close();
    dir.close(); sdir1.close(); sdir2.close();
    reader = multiReader = multiReaderDupls = null;
    searcher = multiSearcher = multiSearcherDupls = null;
    dir = sdir1 = sdir2 = null;
  }
  
  private Query extractInnerQuery(Query q) {
    if (q instanceof ConstantScoreQuery) {
      // wrapped as ConstantScoreQuery
      q = ((ConstantScoreQuery) q).getQuery();
    }
    return q;
  }
  
  private Term extractTerm(Query q) {
    q = extractInnerQuery(q);
    return ((TermQuery) q).getTerm();
  }
  
  private void checkBooleanQueryOrder(Query q) {
    q = extractInnerQuery(q);
    final BooleanQuery bq = (BooleanQuery) q;
    Term last = null, act;
    for (BooleanClause clause : bq.clauses()) {
      act = extractTerm(clause.getQuery());
      if (last != null) {
        assertTrue("sort order of terms in BQ violated", last.compareTo(act) < 0);
      }
      last = act;
    }
  }
  
  private void checkDuplicateTerms(MultiTermQuery.RewriteMethod method) throws Exception {
    final MultiTermQuery mtq = TermRangeQuery.newStringRange("data", "2", "7", true, true);
    mtq.setRewriteMethod(method);
    final Query q1 = searcher.rewrite(mtq);
    final Query q2 = multiSearcher.rewrite(mtq);
    final Query q3 = multiSearcherDupls.rewrite(mtq);
    if (VERBOSE) {
      System.out.println();
      System.out.println("single segment: " + q1);
      System.out.println("multi segment: " + q2);
      System.out.println("multi segment with duplicates: " + q3);
    }
    assertEquals("The multi-segment case must produce same rewritten query", q1, q2);
    assertEquals("The multi-segment case with duplicates must produce same rewritten query", q1, q3);
    checkBooleanQueryOrder(q1);
    checkBooleanQueryOrder(q2);
    checkBooleanQueryOrder(q3);
  }
  
  public void testRewritesWithDuplicateTerms() throws Exception {
    checkDuplicateTerms(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
    
    checkDuplicateTerms(MultiTermQuery.CONSTANT_SCORE_BOOLEAN_QUERY_REWRITE);
    
    // use a large PQ here to only test duplicate terms and dont mix up when all scores are equal
    checkDuplicateTerms(new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(1024));
    checkDuplicateTerms(new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(1024));
    
    // Test auto rewrite (but only boolean mode), so we set the limits to large values to always get a BQ
    final MultiTermQuery.ConstantScoreAutoRewrite rewrite = new MultiTermQuery.ConstantScoreAutoRewrite();
    rewrite.setTermCountCutoff(Integer.MAX_VALUE);
    rewrite.setDocCountPercent(100.);
    checkDuplicateTerms(rewrite);
  }
  
  private void checkBooleanQueryBoosts(BooleanQuery bq) {
    for (BooleanClause clause : bq.clauses()) {
      final TermQuery mtq = (TermQuery) clause.getQuery();
      assertEquals("Parallel sorting of boosts in rewrite mode broken",
        Float.parseFloat(mtq.getTerm().text()), mtq.getBoost(), 0);
    }
  }
  
  private void checkBoosts(MultiTermQuery.RewriteMethod method) throws Exception {
    final MultiTermQuery mtq = new MultiTermQuery("data") {
      @Override
      protected TermsEnum getTermsEnum(Terms terms, AttributeSource atts) throws IOException {
        return new TermRangeTermsEnum(terms.iterator(null), new BytesRef("2"), new BytesRef("7"), true, true) {
          final BoostAttribute boostAtt =
            attributes().addAttribute(BoostAttribute.class);
        
          @Override
          protected AcceptStatus accept(BytesRef term) {
            boostAtt.setBoost(Float.parseFloat(term.utf8ToString()));
            return super.accept(term);
          }
        };
      }
      
      @Override
      public String toString(String field) {
        return "dummy";
      }
    };
    mtq.setRewriteMethod(method);
    final Query q1 = searcher.rewrite(mtq);
    final Query q2 = multiSearcher.rewrite(mtq);
    final Query q3 = multiSearcherDupls.rewrite(mtq);
    if (VERBOSE) {
      System.out.println();
      System.out.println("single segment: " + q1);
      System.out.println("multi segment: " + q2);
      System.out.println("multi segment with duplicates: " + q3);
    }
    assertEquals("The multi-segment case must produce same rewritten query", q1, q2);
    assertEquals("The multi-segment case with duplicates must produce same rewritten query", q1, q3);
    checkBooleanQueryBoosts((BooleanQuery) q1);
    checkBooleanQueryBoosts((BooleanQuery) q2);
    checkBooleanQueryBoosts((BooleanQuery) q3);
  }
  
  public void testBoosts() throws Exception {
    checkBoosts(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);

    // use a large PQ here to only test boosts and dont mix up when all scores are equal
    checkBoosts(new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(1024));
  }
  
  private void checkMaxClauseLimitation(MultiTermQuery.RewriteMethod method) throws Exception {
    int savedMaxClauseCount = BooleanQuery.getMaxClauseCount();
    BooleanQuery.setMaxClauseCount(3);
    
    final MultiTermQuery mtq = TermRangeQuery.newStringRange("data", "2", "7", true, true);
    mtq.setRewriteMethod(method);
    try {
      multiSearcherDupls.rewrite(mtq);
      fail("Should throw BooleanQuery.TooManyClauses");
    } catch (BooleanQuery.TooManyClauses e) {
      //  Maybe remove this assert in later versions, when internal API changes:
      assertEquals("Should throw BooleanQuery.TooManyClauses with a stacktrace containing checkMaxClauseCount()",
        "checkMaxClauseCount", e.getStackTrace()[0].getMethodName());
    } finally {
      BooleanQuery.setMaxClauseCount(savedMaxClauseCount);
    }
  }
  
  private void checkNoMaxClauseLimitation(MultiTermQuery.RewriteMethod method) throws Exception {
    int savedMaxClauseCount = BooleanQuery.getMaxClauseCount();
    BooleanQuery.setMaxClauseCount(3);
    
    final MultiTermQuery mtq = TermRangeQuery.newStringRange("data", "2", "7", true, true);
    mtq.setRewriteMethod(method);
    try {
      multiSearcherDupls.rewrite(mtq);
    } finally {
      BooleanQuery.setMaxClauseCount(savedMaxClauseCount);
    }
  }
  
  public void testMaxClauseLimitations() throws Exception {
    checkMaxClauseLimitation(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);
    checkMaxClauseLimitation(MultiTermQuery.CONSTANT_SCORE_BOOLEAN_QUERY_REWRITE);
    
    checkNoMaxClauseLimitation(MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE);
    checkNoMaxClauseLimitation(MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT);
    checkNoMaxClauseLimitation(new MultiTermQuery.TopTermsScoringBooleanQueryRewrite(1024));
    checkNoMaxClauseLimitation(new MultiTermQuery.TopTermsBoostOnlyBooleanQueryRewrite(1024));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3100.java