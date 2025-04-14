error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3425.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3425.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3425.java
text:
```scala
Q@@ueryUtils.check(random, q,s);

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

import java.util.HashMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test FieldScoreQuery search.
 * <p>
 * Tests here create an index with a few documents, each having
 * an int value indexed  field and a float value indexed field.
 * The values of these fields are later used for scoring.
 * <p>
 * The rank tests use Hits to verify that docs are ordered (by score) as expected.
 * <p>
 * The exact score tests use TopDocs top to verify the exact score.  
 */
public class TestFieldScoreQuery extends FunctionTestSetup {

  /* @override constructor */
  public TestFieldScoreQuery() {
    super(true);
  }

  /** Test that FieldScoreQuery of Type.BYTE returns docs in expected order. */
  @Test
  public void testRankByte () throws Exception {
    // INT field values are small enough to be parsed as byte
    doTestRank(INT_FIELD,FieldScoreQuery.Type.BYTE);
  }

  /** Test that FieldScoreQuery of Type.SHORT returns docs in expected order. */
  @Test
  public void testRankShort () throws Exception {
    // INT field values are small enough to be parsed as short
    doTestRank(INT_FIELD,FieldScoreQuery.Type.SHORT);
  }

  /** Test that FieldScoreQuery of Type.INT returns docs in expected order. */
  @Test
  public void testRankInt () throws Exception {
    doTestRank(INT_FIELD,FieldScoreQuery.Type.INT);
  }

  /** Test that FieldScoreQuery of Type.FLOAT returns docs in expected order. */
  @Test
  public void testRankFloat () throws Exception {
    // INT field can be parsed as float
    doTestRank(INT_FIELD,FieldScoreQuery.Type.FLOAT);
    // same values, but in flot format
    doTestRank(FLOAT_FIELD,FieldScoreQuery.Type.FLOAT);
  }

  // Test that FieldScoreQuery returns docs in expected order.
  private void doTestRank (String field, FieldScoreQuery.Type tp) throws Exception {
    IndexSearcher s = new IndexSearcher(dir, true);
    Query q = new FieldScoreQuery(field,tp);
    log("test: "+q);
    QueryUtils.check(q,s);
    ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!",N_DOCS,h.length);
    String prevID = "ID"+(N_DOCS+1); // greater than all ids of docs in this test
    for (int i=0; i<h.length; i++) {
      String resID = s.doc(h[i].doc).get(ID_FIELD);
      log(i+".   score="+h[i].score+"  -  "+resID);
      log(s.explain(q,h[i].doc));
      assertTrue("res id "+resID+" should be < prev res id "+prevID, resID.compareTo(prevID)<0);
      prevID = resID;
    }
    s.close();
  }

  /** Test that FieldScoreQuery of Type.BYTE returns the expected scores. */
  @Test
  public void testExactScoreByte () throws Exception {
    // INT field values are small enough to be parsed as byte
    doTestExactScore(INT_FIELD,FieldScoreQuery.Type.BYTE);
  }

  /** Test that FieldScoreQuery of Type.SHORT returns the expected scores. */
  @Test
  public void testExactScoreShort () throws  Exception {
    // INT field values are small enough to be parsed as short
    doTestExactScore(INT_FIELD,FieldScoreQuery.Type.SHORT);
  }

  /** Test that FieldScoreQuery of Type.INT returns the expected scores. */
  @Test
  public void testExactScoreInt () throws  Exception {
    doTestExactScore(INT_FIELD,FieldScoreQuery.Type.INT);
  }

  /** Test that FieldScoreQuery of Type.FLOAT returns the expected scores. */
  @Test
  public void testExactScoreFloat () throws  Exception {
    // INT field can be parsed as float
    doTestExactScore(INT_FIELD,FieldScoreQuery.Type.FLOAT);
    // same values, but in flot format
    doTestExactScore(FLOAT_FIELD,FieldScoreQuery.Type.FLOAT);
  }

  // Test that FieldScoreQuery returns docs with expected score.
  private void doTestExactScore (String field, FieldScoreQuery.Type tp) throws Exception {
    IndexSearcher s = new IndexSearcher(dir, true);
    Query q = new FieldScoreQuery(field,tp);
    TopDocs td = s.search(q,null,1000);
    assertEquals("All docs should be matched!",N_DOCS,td.totalHits);
    ScoreDoc sd[] = td.scoreDocs;
    for (ScoreDoc aSd : sd) {
      float score = aSd.score;
      log(s.explain(q, aSd.doc));
      String id = s.getIndexReader().document(aSd.doc).get(ID_FIELD);
      float expectedScore = expectedFieldScore(id); // "ID7" --> 7.0
      assertEquals("score of " + id + " shuould be " + expectedScore + " != " + score, expectedScore, score, TEST_SCORE_TOLERANCE_DELTA);
    }
    s.close();
  }

  /** Test that FieldScoreQuery of Type.BYTE caches/reuses loaded values and consumes the proper RAM resources. */
  @Test
  public void testCachingByte () throws  Exception {
    // INT field values are small enough to be parsed as byte
    doTestCaching(INT_FIELD,FieldScoreQuery.Type.BYTE);
  }

  /** Test that FieldScoreQuery of Type.SHORT caches/reuses loaded values and consumes the proper RAM resources. */
  @Test
  public void testCachingShort () throws  Exception {
    // INT field values are small enough to be parsed as short
    doTestCaching(INT_FIELD,FieldScoreQuery.Type.SHORT);
  }

  /** Test that FieldScoreQuery of Type.INT caches/reuses loaded values and consumes the proper RAM resources. */
  @Test
  public void testCachingInt () throws Exception {
    doTestCaching(INT_FIELD,FieldScoreQuery.Type.INT);
  }

  /** Test that FieldScoreQuery of Type.FLOAT caches/reuses loaded values and consumes the proper RAM resources. */
  @Test
  public void testCachingFloat () throws  Exception {
    // INT field values can be parsed as float
    doTestCaching(INT_FIELD,FieldScoreQuery.Type.FLOAT);
    // same values, but in flot format
    doTestCaching(FLOAT_FIELD,FieldScoreQuery.Type.FLOAT);
  }

  // Test that values loaded for FieldScoreQuery are cached properly and consumes the proper RAM resources.
  private void doTestCaching (String field, FieldScoreQuery.Type tp) throws Exception {
    // prepare expected array types for comparison
    HashMap<FieldScoreQuery.Type,Object> expectedArrayTypes = new HashMap<FieldScoreQuery.Type,Object>();
    expectedArrayTypes.put(FieldScoreQuery.Type.BYTE, new byte[0]);
    expectedArrayTypes.put(FieldScoreQuery.Type.SHORT, new short[0]);
    expectedArrayTypes.put(FieldScoreQuery.Type.INT, new int[0]);
    expectedArrayTypes.put(FieldScoreQuery.Type.FLOAT, new float[0]);
    
    IndexSearcher s = new IndexSearcher(dir, true);
    Object[] innerArray = new Object[s.getIndexReader().getSequentialSubReaders().length];

    boolean warned = false; // print warning once.
    for (int i=0; i<10; i++) {
      FieldScoreQuery q = new FieldScoreQuery(field,tp);
      ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
      assertEquals("All docs should be matched!",N_DOCS,h.length);
      IndexReader[] readers = s.getIndexReader().getSequentialSubReaders();
      for (int j = 0; j < readers.length; j++) {
        IndexReader reader = readers[j];
        try {
          if (i == 0) {
            innerArray[j] = q.valSrc.getValues(reader).getInnerArray();
            log(i + ".  compare: " + innerArray[j].getClass() + " to "
                + expectedArrayTypes.get(tp).getClass());
            assertEquals(
                "field values should be cached in the correct array type!",
                innerArray[j].getClass(), expectedArrayTypes.get(tp).getClass());
          } else {
            log(i + ".  compare: " + innerArray[j] + " to "
                + q.valSrc.getValues(reader).getInnerArray());
            assertSame("field values should be cached and reused!", innerArray[j],
                q.valSrc.getValues(reader).getInnerArray());
          }
        } catch (UnsupportedOperationException e) {
          if (!warned) {
            System.err.println("WARNING: " + testName()
                + " cannot fully test values of " + q);
            warned = true;
          }
        }
      }
    }
    s.close();
    // verify new values are reloaded (not reused) for a new reader
    s = new IndexSearcher(dir, true);
    FieldScoreQuery q = new FieldScoreQuery(field,tp);
    ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!",N_DOCS,h.length);
    IndexReader[] readers = s.getIndexReader().getSequentialSubReaders();
    for (int j = 0; j < readers.length; j++) {
      IndexReader reader = readers[j];
      try {
        log("compare: " + innerArray + " to "
            + q.valSrc.getValues(reader).getInnerArray());
        assertNotSame(
            "cached field values should not be reused if reader as changed!",
            innerArray, q.valSrc.getValues(reader).getInnerArray());
      } catch (UnsupportedOperationException e) {
        if (!warned) {
          System.err.println("WARNING: " + testName()
              + " cannot fully test values of " + q);
          warned = true;
        }
      }
    }
    s.close();
  }

  private String testName() {
    return getClass().getName()+"."+ getName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3425.java