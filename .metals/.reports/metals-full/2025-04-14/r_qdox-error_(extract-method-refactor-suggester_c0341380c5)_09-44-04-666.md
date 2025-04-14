error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/8.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/8.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/8.java
text:
```scala
s@@uper(name, false);

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

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Test search based on OrdFieldSource and ReverseOrdFieldSource.
 * <p>
 * Tests here create an index with a few documents, each having
 * an indexed "id" field.
 * The ord values of this field are later used for scoring.
 * <p>
 * The order tests use Hits to verify that docs are ordered as expected.
 * <p>
 * The exact score tests use TopDocs top to verify the exact score.  
 */
public class TestOrdValues extends FunctionTestSetup {

  /* @override constructor */
  public TestOrdValues(String name) {
    super(name);
  }

  /** Test OrdFieldSource */
  public void testOrdFieldRank () throws CorruptIndexException, Exception {
    doTestRank(ID_FIELD,true);
  }

  /** Test ReverseOrdFieldSource */
  public void testReverseOrdFieldRank () throws CorruptIndexException, Exception {
    doTestRank(ID_FIELD,false);
  }

  // Test that queries based on reverse/ordFieldScore scores correctly
  private void doTestRank (String field, boolean inOrder) throws CorruptIndexException, Exception {
    IndexSearcher s = new IndexSearcher(dir);
    ValueSource vs;
    if (inOrder) {
      vs = new MultiValueSource(new OrdFieldSource(field));
    } else {
      vs = new MultiValueSource(new ReverseOrdFieldSource(field));
    }
        
    Query q = new ValueSourceQuery(vs);
    log("test: "+q);
    QueryUtils.check(q,s);
    ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!",N_DOCS,h.length);
    String prevID = inOrder
      ? "IE"   // greater than all ids of docs in this test ("ID0001", etc.)
      : "IC";  // smaller than all ids of docs in this test ("ID0001", etc.)
          
    for (int i=0; i<h.length; i++) {
      String resID = s.doc(h[i].doc).get(ID_FIELD);
      log(i+".   score="+h[i].score+"  -  "+resID);
      log(s.explain(q,h[i].doc));
      if (inOrder) {
        assertTrue("res id "+resID+" should be < prev res id "+prevID, resID.compareTo(prevID)<0);
      } else {
        assertTrue("res id "+resID+" should be > prev res id "+prevID, resID.compareTo(prevID)>0);
      }
      prevID = resID;
    }
  }

  /** Test exact score for OrdFieldSource */
  public void testOrdFieldExactScore () throws CorruptIndexException, Exception {
    doTestExactScore(ID_FIELD,true);
  }

  /** Test exact score for ReverseOrdFieldSource */
  public void testReverseOrdFieldExactScore () throws CorruptIndexException, Exception {
    doTestExactScore(ID_FIELD,false);
  }

  
  // Test that queries based on reverse/ordFieldScore returns docs with expected score.
  private void doTestExactScore (String field, boolean inOrder) throws CorruptIndexException, Exception {
    IndexSearcher s = new IndexSearcher(dir);
    ValueSource vs;
    if (inOrder) {
      vs = new OrdFieldSource(field);
    } else {
      vs = new ReverseOrdFieldSource(field);
    }
    Query q = new ValueSourceQuery(vs);
    TopDocs td = s.search(q,null,1000);
    assertEquals("All docs should be matched!",N_DOCS,td.totalHits);
    ScoreDoc sd[] = td.scoreDocs;
    for (int i=0; i<sd.length; i++) {
      float score = sd[i].score;
      String id = s.getIndexReader().document(sd[i].doc).get(ID_FIELD);
      log("-------- "+i+". Explain doc "+id);
      log(s.explain(q,sd[i].doc));
      float expectedScore =  N_DOCS-i;
      assertEquals("score of result "+i+" shuould be "+expectedScore+" != "+score, expectedScore, score, TEST_SCORE_TOLERANCE_DELTA);
      String expectedId =  inOrder 
        ? id2String(N_DOCS-i) // in-order ==> larger  values first 
        : id2String(i+1);     // reverse  ==> smaller values first 
      assertTrue("id of result "+i+" shuould be "+expectedId+" != "+score, expectedId.equals(id));
    }
  }
  
  /** Test caching OrdFieldSource */
  public void testCachingOrd () throws CorruptIndexException, Exception {
    doTestCaching(ID_FIELD,true);
  }
  
  /** Test caching for ReverseOrdFieldSource */
  public void tesCachingReverseOrd () throws CorruptIndexException, Exception {
    doTestCaching(ID_FIELD,false);
  }

  // Test that values loaded for FieldScoreQuery are cached properly and consumes the proper RAM resources.
  private void doTestCaching (String field, boolean inOrder) throws CorruptIndexException, Exception {
    IndexSearcher s = new IndexSearcher(dir);
    Object innerArray = null;

    boolean warned = false; // print warning once
    
    for (int i=0; i<10; i++) {
      ValueSource vs;
      if (inOrder) {
        vs = new OrdFieldSource(field);
      } else {
        vs = new ReverseOrdFieldSource(field);
      }
      ValueSourceQuery q = new ValueSourceQuery(vs);
      ScoreDoc[] h = s.search(q, null, 1000).scoreDocs;
      try {
        assertEquals("All docs should be matched!",N_DOCS,h.length);
        IndexReader[] readers = s.getIndexReader().getSequentialSubReaders();

        for(int j = 0; j < readers.length; j++) {
          IndexReader reader = readers[j];
          if (i==0) {
            innerArray = q.valSrc.getValues(reader).getInnerArray();
          } else {
            log(i+".  compare: "+innerArray+" to "+q.valSrc.getValues(reader).getInnerArray());
            assertSame("field values should be cached and reused!", innerArray, q.valSrc.getValues(reader).getInnerArray());
          }
        }
      } catch (UnsupportedOperationException e) {
        if (!warned) {
          System.err.println("WARNING: "+testName()+" cannot fully test values of "+q);
          warned = true;
        }
      }
    }
    
    ValueSource vs;
    ValueSourceQuery q;
    ScoreDoc[] h;
    
    // verify that different values are loaded for a different field
    String field2 = INT_FIELD;
    assertFalse(field.equals(field2)); // otherwise this test is meaningless.
    if (inOrder) {
      vs = new OrdFieldSource(field2);
    } else {
      vs = new ReverseOrdFieldSource(field2);
    }
    q = new ValueSourceQuery(vs);
    h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!",N_DOCS,h.length);
    IndexReader[] readers = s.getIndexReader().getSequentialSubReaders();

    for (int j = 0; j < readers.length; j++) {
      IndexReader reader = readers[j];
      try {
        log("compare (should differ): " + innerArray + " to "
            + q.valSrc.getValues(reader).getInnerArray());
        assertNotSame(
            "different values shuold be loaded for a different field!",
            innerArray, q.valSrc.getValues(reader).getInnerArray());
      } catch (UnsupportedOperationException e) {
        if (!warned) {
          System.err.println("WARNING: " + testName()
              + " cannot fully test values of " + q);
          warned = true;
        }
      }
    }

    // verify new values are reloaded (not reused) for a new reader
    s = new IndexSearcher(dir);
    if (inOrder) {
      vs = new OrdFieldSource(field);
    } else {
      vs = new ReverseOrdFieldSource(field);
    }
    q = new ValueSourceQuery(vs);
    h = s.search(q, null, 1000).scoreDocs;
    assertEquals("All docs should be matched!",N_DOCS,h.length);
    readers = s.getIndexReader().getSequentialSubReaders();

    for (int j = 0; j < readers.length; j++) {
      IndexReader reader = readers[j];
      try {
        log("compare (should differ): " + innerArray + " to "
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
  }

  private String testName() {
    return getClass().getName()+"."+getName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/8.java