error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2935.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2935.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2935.java
text:
```scala
I@@ndexSearcher searcher = new IndexSearcher(ramDir, true);

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

import org.apache.lucene.util.LuceneTestCase;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.store.RAMDirectory;

public class TestSloppyPhraseQuery extends LuceneTestCase {

  private static final String S_1 = "A A A";
  private static final String S_2 = "A 1 2 3 A 4 5 6 A";

  private static final Document DOC_1 = makeDocument("X " + S_1 + " Y");
  private static final Document DOC_2 = makeDocument("X " + S_2 + " Y");
  private static final Document DOC_3 = makeDocument("X " + S_1 + " A Y");
  private static final Document DOC_1_B = makeDocument("X " + S_1 + " Y N N N N " + S_1 + " Z");
  private static final Document DOC_2_B = makeDocument("X " + S_2 + " Y N N N N " + S_2 + " Z");
  private static final Document DOC_3_B = makeDocument("X " + S_1 + " A Y N N N N " + S_1 + " A Y");
  private static final Document DOC_4 = makeDocument("A A X A X B A X B B A A X B A A");

  private static final PhraseQuery QUERY_1 = makePhraseQuery( S_1 );
  private static final PhraseQuery QUERY_2 = makePhraseQuery( S_2 );
  private static final PhraseQuery QUERY_4 = makePhraseQuery( "X A A");


  /**
   * Test DOC_4 and QUERY_4.
   * QUERY_4 has a fuzzy (len=1) match to DOC_4, so all slop values > 0 should succeed.
   * But only the 3rd sequence of A's in DOC_4 will do.
   */
  public void testDoc4_Query4_All_Slops_Should_match() throws Exception {
    for (int slop=0; slop<30; slop++) {
      int numResultsExpected = slop<1 ? 0 : 1;
      checkPhraseQuery(DOC_4, QUERY_4, slop, numResultsExpected);
    }
  }

  /**
   * Test DOC_1 and QUERY_1.
   * QUERY_1 has an exact match to DOC_1, so all slop values should succeed.
   * Before LUCENE-1310, a slop value of 1 did not succeed.
   */
  public void testDoc1_Query1_All_Slops_Should_match() throws Exception {
    for (int slop=0; slop<30; slop++) {
      float score1 = checkPhraseQuery(DOC_1, QUERY_1, slop, 1);
      float score2 = checkPhraseQuery(DOC_1_B, QUERY_1, slop, 1);
      assertTrue("slop="+slop+" score2="+score2+" should be greater than score1 "+score1, score2>score1);
    }
  }

  /**
   * Test DOC_2 and QUERY_1.
   * 6 should be the minimum slop to make QUERY_1 match DOC_2.
   * Before LUCENE-1310, 7 was the minimum.
   */
  public void testDoc2_Query1_Slop_6_or_more_Should_match() throws Exception {
    for (int slop=0; slop<30; slop++) {
      int numResultsExpected = slop<6 ? 0 : 1;
      float score1 = checkPhraseQuery(DOC_2, QUERY_1, slop, numResultsExpected);
      if (numResultsExpected>0) {
        float score2 = checkPhraseQuery(DOC_2_B, QUERY_1, slop, 1);
        assertTrue("slop="+slop+" score2="+score2+" should be greater than score1 "+score1, score2>score1);
      }
    }
  }

  /**
   * Test DOC_2 and QUERY_2.
   * QUERY_2 has an exact match to DOC_2, so all slop values should succeed.
   * Before LUCENE-1310, 0 succeeds, 1 through 7 fail, and 8 or greater succeeds.
   */
  public void testDoc2_Query2_All_Slops_Should_match() throws Exception {
    for (int slop=0; slop<30; slop++) {
      float score1 = checkPhraseQuery(DOC_2, QUERY_2, slop, 1);
      float score2 = checkPhraseQuery(DOC_2_B, QUERY_2, slop, 1);
      assertTrue("slop="+slop+" score2="+score2+" should be greater than score1 "+score1, score2>score1);
    }
  }

  /**
   * Test DOC_3 and QUERY_1.
   * QUERY_1 has an exact match to DOC_3, so all slop values should succeed.
   */
  public void testDoc3_Query1_All_Slops_Should_match() throws Exception {
    for (int slop=0; slop<30; slop++) {
      float score1 = checkPhraseQuery(DOC_3, QUERY_1, slop, 1);
      float score2 = checkPhraseQuery(DOC_3_B, QUERY_1, slop, 1);
      assertTrue("slop="+slop+" score2="+score2+" should be greater than score1 "+score1, score2>score1);
    }
  }

  private float  checkPhraseQuery(Document doc, PhraseQuery query, int slop, int expectedNumResults) throws Exception {
    query.setSlop(slop);

    RAMDirectory ramDir = new RAMDirectory();
    WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
    IndexWriter writer = new IndexWriter(ramDir, analyzer, MaxFieldLength.UNLIMITED);
    writer.addDocument(doc);
    writer.close();

    IndexSearcher searcher = new IndexSearcher(ramDir);
    TopDocs td = searcher.search(query,null,10);
    //System.out.println("slop: "+slop+"  query: "+query+"  doc: "+doc+"  Expecting number of hits: "+expectedNumResults+" maxScore="+td.getMaxScore());
    assertEquals("slop: "+slop+"  query: "+query+"  doc: "+doc+"  Wrong number of hits", expectedNumResults, td.totalHits);

    //QueryUtils.check(query,searcher);

    searcher.close();
    ramDir.close();

    return td.getMaxScore();
  }

  private static Document makeDocument(String docText) {
    Document doc = new Document();
    Field f = new Field("f", docText, Field.Store.NO, Field.Index.ANALYZED);
    f.setOmitNorms(true);
    doc.add(f);
    return doc;
  }

  private static PhraseQuery makePhraseQuery(String terms) {
    PhraseQuery query = new PhraseQuery();
    String[] t = terms.split(" +");
    for (int i=0; i<t.length; i++) {
      query.add(new Term("f", t[i]));
    }
    return query;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2935.java