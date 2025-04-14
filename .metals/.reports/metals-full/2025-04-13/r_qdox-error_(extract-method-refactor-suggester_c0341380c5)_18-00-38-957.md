error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/184.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/184.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/184.java
text:
```scala
f@@loat expectedScore = N_DOCS - i - 1;

package org.apache.lucene.queries.function;

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

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.function.valuesource.OrdFieldSource;
import org.apache.lucene.queries.function.valuesource.ReverseOrdFieldSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test search based on OrdFieldSource and ReverseOrdFieldSource.
 * <p/>
 * Tests here create an index with a few documents, each having
 * an indexed "id" field.
 * The ord values of this field are later used for scoring.
 * <p/>
 * The order tests use Hits to verify that docs are ordered as expected.
 * <p/>
 * The exact score tests use TopDocs top to verify the exact score.
 */
public class TestOrdValues extends FunctionTestSetup {

  @BeforeClass
  public static void beforeClass() throws Exception {
    createIndex(false);
  }

  /**
   * Test OrdFieldSource
   */
  @Test
  public void testOrdFieldRank() throws Exception {
    doTestRank(ID_FIELD, true);
  }

  /**
   * Test ReverseOrdFieldSource
   */
  @Test
  public void testReverseOrdFieldRank() throws Exception {
    doTestRank(ID_FIELD, false);
  }

  // Test that queries based on reverse/ordFieldScore scores correctly
  private void doTestRank(String field, boolean inOrder) throws Exception {
    IndexReader r = DirectoryReader.open(dir);
    IndexSearcher s = new IndexSearcher(r);
    ValueSource vs;
    if (inOrder) {
      vs = new OrdFieldSource(field);
    } else {
      vs = new ReverseOrdFieldSource(field);
    }

    Query q = new FunctionQuery(vs);
    log("test: " + q);
    QueryUtils.check(random(), q, s);
    ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!", N_DOCS, h.length);
    String prevID = inOrder
            ? "IE"   // greater than all ids of docs in this test ("ID0001", etc.)
            : "IC";  // smaller than all ids of docs in this test ("ID0001", etc.)

    for (int i = 0; i < h.length; i++) {
      String resID = s.doc(h[i].doc).get(ID_FIELD);
      log(i + ".   score=" + h[i].score + "  -  " + resID);
      log(s.explain(q, h[i].doc));
      if (inOrder) {
        assertTrue("res id " + resID + " should be < prev res id " + prevID, resID.compareTo(prevID) < 0);
      } else {
        assertTrue("res id " + resID + " should be > prev res id " + prevID, resID.compareTo(prevID) > 0);
      }
      prevID = resID;
    }
    r.close();
  }

  /**
   * Test exact score for OrdFieldSource
   */
  @Test
  public void testOrdFieldExactScore() throws Exception {
    doTestExactScore(ID_FIELD, true);
  }

  /**
   * Test exact score for ReverseOrdFieldSource
   */
  @Test
  public void testReverseOrdFieldExactScore() throws Exception {
    doTestExactScore(ID_FIELD, false);
  }


  // Test that queries based on reverse/ordFieldScore returns docs with expected score.
  private void doTestExactScore(String field, boolean inOrder) throws Exception {
    IndexReader r = DirectoryReader.open(dir);
    IndexSearcher s = new IndexSearcher(r);
    ValueSource vs;
    if (inOrder) {
      vs = new OrdFieldSource(field);
    } else {
      vs = new ReverseOrdFieldSource(field);
    }
    Query q = new FunctionQuery(vs);
    TopDocs td = s.search(q, null, 1000);
    assertEquals("All docs should be matched!", N_DOCS, td.totalHits);
    ScoreDoc sd[] = td.scoreDocs;
    for (int i = 0; i < sd.length; i++) {
      float score = sd[i].score;
      String id = s.getIndexReader().document(sd[i].doc).get(ID_FIELD);
      log("-------- " + i + ". Explain doc " + id);
      log(s.explain(q, sd[i].doc));
      float expectedScore = N_DOCS - i;
      assertEquals("score of result " + i + " shuould be " + expectedScore + " != " + score, expectedScore, score, TEST_SCORE_TOLERANCE_DELTA);
      String expectedId = inOrder
              ? id2String(N_DOCS - i) // in-order ==> larger  values first
              : id2String(i + 1);     // reverse  ==> smaller values first
      assertTrue("id of result " + i + " shuould be " + expectedId + " != " + score, expectedId.equals(id));
    }
    r.close();
  }
  
  // LUCENE-1250
  public void testEqualsNull() throws Exception {
    OrdFieldSource ofs = new OrdFieldSource("f");
    assertFalse(ofs.equals(null));
    
    ReverseOrdFieldSource rofs = new ReverseOrdFieldSource("f");
    assertFalse(rofs.equals(null));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/184.java