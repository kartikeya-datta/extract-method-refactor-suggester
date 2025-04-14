error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3419.java
text:
```scala
C@@heckHits.checkHitCollector(random, q, null, searcher, docs);

package org.apache.lucene.search.spans;

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

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.SlowMultiReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.CheckHits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestFieldMaskingSpanQuery extends LuceneTestCase {

  protected static Document doc(Field[] fields) {
    Document doc = new Document();
    for (int i = 0; i < fields.length; i++) {
      doc.add(fields[i]);
    }
    return doc;
  }
  
  protected Field field(String name, String value) {
    return newField(name, value, Field.Store.NO, Field.Index.ANALYZED);
  }

  protected IndexSearcher searcher;
  protected Directory directory;
  protected IndexReader reader;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter writer= new RandomIndexWriter(random, directory);
    
    writer.addDocument(doc(new Field[] { field("id", "0")
                                         ,
                                         field("gender", "male"),
                                         field("first",  "james"),
                                         field("last",   "jones")     }));
                                               
    writer.addDocument(doc(new Field[] { field("id", "1")
                                         ,
                                         field("gender", "male"),
                                         field("first",  "james"),
                                         field("last",   "smith")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "sally"),
                                         field("last",   "jones")     }));
    
    writer.addDocument(doc(new Field[] { field("id", "2")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "greta"),
                                         field("last",   "jones")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "sally"),
                                         field("last",   "smith")
                                         ,
                                         field("gender", "male"),
                                         field("first",  "james"),
                                         field("last",   "jones")     }));
     
    writer.addDocument(doc(new Field[] { field("id", "3")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "lisa"),
                                         field("last",   "jones")
                                         ,
                                         field("gender", "male"),
                                         field("first",  "bob"),
                                         field("last",   "costas")     }));
    
    writer.addDocument(doc(new Field[] { field("id", "4")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "sally"),
                                         field("last",   "smith")
                                         ,
                                         field("gender", "female"),
                                         field("first",  "linda"),
                                         field("last",   "dixit")
                                         ,
                                         field("gender", "male"),
                                         field("first",  "bubba"),
                                         field("last",   "jones")     }));
    reader = writer.getReader();
    writer.close();
    searcher = new IndexSearcher(SlowMultiReaderWrapper.wrap(reader));
  }

  @Override
  public void tearDown() throws Exception {
    searcher.close();
    reader.close();
    directory.close();
    super.tearDown();
  }

  protected void check(SpanQuery q, int[] docs) throws Exception {
    CheckHits.checkHitCollector(q, null, searcher, docs);
  }

  public void testRewrite0() throws Exception {
    SpanQuery q = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "first");
    q.setBoost(8.7654321f);
    SpanQuery qr = (SpanQuery) searcher.rewrite(q);

    QueryUtils.checkEqual(q, qr);

    Set<Term> terms = new HashSet<Term>();
    qr.extractTerms(terms);
    assertEquals(1, terms.size());
  }
  
  public void testRewrite1() throws Exception {
    // mask an anon SpanQuery class that rewrites to something else.
    SpanQuery q = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) {
          @Override
          public Query rewrite(IndexReader reader) {
            return new SpanOrQuery(new SpanQuery[] {
              new SpanTermQuery(new Term("first", "sally")),
              new SpanTermQuery(new Term("first", "james")) });
          }
        }, "first");

    SpanQuery qr = (SpanQuery) searcher.rewrite(q);

    QueryUtils.checkUnequal(q, qr);

    Set<Term> terms = new HashSet<Term>();
    qr.extractTerms(terms);
    assertEquals(2, terms.size());
  }
  
  public void testRewrite2() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("last", "smith"));
    SpanQuery q2 = new SpanTermQuery(new Term("last", "jones"));
    SpanQuery q = new SpanNearQuery(new SpanQuery[]
      { q1, new FieldMaskingSpanQuery(q2, "last")}, 1, true );
    Query qr = searcher.rewrite(q);

    QueryUtils.checkEqual(q, qr);

    HashSet<Term> set = new HashSet<Term>();
    qr.extractTerms(set);
    assertEquals(2, set.size());
  }
  
  public void testEquality1() {
    SpanQuery q1 = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "first");
    SpanQuery q2 = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "first");
    SpanQuery q3 = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "XXXXX");
    SpanQuery q4 = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "XXXXX")) , "first");
    SpanQuery q5 = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("xXXX", "sally")) , "first");
    QueryUtils.checkEqual(q1, q2);
    QueryUtils.checkUnequal(q1, q3);
    QueryUtils.checkUnequal(q1, q4);
    QueryUtils.checkUnequal(q1, q5);
    
    SpanQuery qA = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "first");
    qA.setBoost(9f);
    SpanQuery qB = new FieldMaskingSpanQuery
      (new SpanTermQuery(new Term("last", "sally")) , "first");
    QueryUtils.checkUnequal(qA, qB);
    qB.setBoost(9f);
    QueryUtils.checkEqual(qA, qB);
    
  }
  
  public void testNoop0() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("last", "sally"));
    SpanQuery q = new FieldMaskingSpanQuery(q1, "first");
    check(q, new int[] { /* :EMPTY: */ });
  }
  public void testNoop1() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("last", "smith"));
    SpanQuery q2 = new SpanTermQuery(new Term("last", "jones"));
    SpanQuery q = new SpanNearQuery(new SpanQuery[]
      { q1, new FieldMaskingSpanQuery(q2, "last")}, 0, true );
    check(q, new int[] { 1, 2 });
    q = new SpanNearQuery(new SpanQuery[]
      { new FieldMaskingSpanQuery(q1, "last"),
        new FieldMaskingSpanQuery(q2, "last")}, 0, true );
    check(q, new int[] { 1, 2 });
  }
  
  public void testSimple1() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("first", "james"));
    SpanQuery q2 = new SpanTermQuery(new Term("last", "jones"));
    SpanQuery q = new SpanNearQuery(new SpanQuery[]
      { q1, new FieldMaskingSpanQuery(q2, "first")}, -1, false );
    check(q, new int[] { 0, 2 });
    q = new SpanNearQuery(new SpanQuery[]
      { new FieldMaskingSpanQuery(q2, "first"), q1}, -1, false );
    check(q, new int[] { 0, 2 });
    q = new SpanNearQuery(new SpanQuery[]
      { q2, new FieldMaskingSpanQuery(q1, "last")}, -1, false );
    check(q, new int[] { 0, 2 });
    q = new SpanNearQuery(new SpanQuery[]
      { new FieldMaskingSpanQuery(q1, "last"), q2}, -1, false );
    check(q, new int[] { 0, 2 });

  }
  
  public void testSimple2() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("gender", "female"));
    SpanQuery q2 = new SpanTermQuery(new Term("last", "smith"));
    SpanQuery q = new SpanNearQuery(new SpanQuery[]
      { q1, new FieldMaskingSpanQuery(q2, "gender")}, -1, false );
    check(q, new int[] { 2, 4 });
    q = new SpanNearQuery(new SpanQuery[]
      { new FieldMaskingSpanQuery(q1, "id"),
        new FieldMaskingSpanQuery(q2, "id") }, -1, false );
    check(q, new int[] { 2, 4 });
  }

  public void testSpans0() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("gender", "female"));
    SpanQuery q2 = new SpanTermQuery(new Term("first",  "james"));
    SpanQuery q  = new SpanOrQuery(new SpanQuery[]
      { q1, new FieldMaskingSpanQuery(q2, "gender")});
    check(q, new int[] { 0, 1, 2, 3, 4 });
  
    Spans span = q.getSpans(searcher.getIndexReader());
    
    assertEquals(true, span.next());
    assertEquals(s(0,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(1,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(1,1,2), s(span));

    assertEquals(true, span.next());
    assertEquals(s(2,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(2,1,2), s(span));

    assertEquals(true, span.next());
    assertEquals(s(2,2,3), s(span));

    assertEquals(true, span.next());
    assertEquals(s(3,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(4,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(4,1,2), s(span));

    assertEquals(false, span.next());
  }
  
  public void testSpans1() throws Exception {
    SpanQuery q1 = new SpanTermQuery(new Term("first", "sally"));
    SpanQuery q2 = new SpanTermQuery(new Term("first", "james"));
    SpanQuery qA = new SpanOrQuery(new SpanQuery[] { q1, q2 });
    SpanQuery qB = new FieldMaskingSpanQuery(qA, "id");
                                            
    check(qA, new int[] { 0, 1, 2, 4 });
    check(qB, new int[] { 0, 1, 2, 4 });
  
    Spans spanA = qA.getSpans(searcher.getIndexReader());
    Spans spanB = qB.getSpans(searcher.getIndexReader());
    
    while (spanA.next()) {
      assertTrue("spanB not still going", spanB.next());
      assertEquals("spanA not equal spanB", s(spanA), s(spanB));
    }
    assertTrue("spanB still going even tough spanA is done", !(spanB.next()));

  }
  
  public void testSpans2() throws Exception {
    SpanQuery qA1 = new SpanTermQuery(new Term("gender", "female"));
    SpanQuery qA2 = new SpanTermQuery(new Term("first",  "james"));
    SpanQuery qA  = new SpanOrQuery(new SpanQuery[]
      { qA1, new FieldMaskingSpanQuery(qA2, "gender")});
    SpanQuery qB  = new SpanTermQuery(new Term("last",   "jones"));
    SpanQuery q   = new SpanNearQuery(new SpanQuery[]
      { new FieldMaskingSpanQuery(qA, "id"),
        new FieldMaskingSpanQuery(qB, "id") }, -1, false );
    check(q, new int[] { 0, 1, 2, 3 });
  
    Spans span = q.getSpans(searcher.getIndexReader());
    
    assertEquals(true, span.next());
    assertEquals(s(0,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(1,1,2), s(span));

    assertEquals(true, span.next());
    assertEquals(s(2,0,1), s(span));

    assertEquals(true, span.next());
    assertEquals(s(2,2,3), s(span));

    assertEquals(true, span.next());
    assertEquals(s(3,0,1), s(span));

    assertEquals(false, span.next());
  }
  
  public String s(Spans span) {
    return s(span.doc(), span.start(), span.end());
  }
  public String s(int doc, int start, int end) {
    return "s(" + doc + "," + start + "," + end +")";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3419.java