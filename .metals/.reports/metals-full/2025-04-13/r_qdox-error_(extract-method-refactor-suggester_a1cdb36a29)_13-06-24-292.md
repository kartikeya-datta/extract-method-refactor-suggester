error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3172.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene;

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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import org.apache.lucene.store.*;
import org.apache.lucene.document.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.util.LuceneTestCase;

public class TestSearchForDuplicates extends LuceneTestCase {

  static final String PRIORITY_FIELD ="priority";
  static final String ID_FIELD ="id";
  static final String HIGH_PRIORITY ="high";
  static final String MED_PRIORITY ="medium";
  static final String LOW_PRIORITY ="low";


  /** This test compares search results when using and not using compound
   *  files.
   *
   *  TODO: There is rudimentary search result validation as well, but it is
   *        simply based on asserting the output observed in the old test case,
   *        without really knowing if the output is correct. Someone needs to
   *        validate this output and make any changes to the checkHits method.
   */
  public void testRun() throws Exception {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw, true);
      final int MAX_DOCS = atLeast(225);
      doTest(random(), pw, false, MAX_DOCS);
      pw.close();
      sw.close();
      String multiFileOutput = sw.toString();
      //System.out.println(multiFileOutput);

      sw = new StringWriter();
      pw = new PrintWriter(sw, true);
      doTest(random(), pw, true, MAX_DOCS);
      pw.close();
      sw.close();
      String singleFileOutput = sw.toString();

      assertEquals(multiFileOutput, singleFileOutput);
  }


  private void doTest(Random random, PrintWriter out, boolean useCompoundFiles, int MAX_DOCS) throws Exception {
      Directory directory = newDirectory();
      Analyzer analyzer = new MockAnalyzer(random);
      IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer);
      final MergePolicy mp = conf.getMergePolicy();
      mp.setNoCFSRatio(useCompoundFiles ? 1.0 : 0.0);
      IndexWriter writer = new IndexWriter(directory, conf);
      if (VERBOSE) {
        System.out.println("TEST: now build index MAX_DOCS=" + MAX_DOCS);
      }

      for (int j = 0; j < MAX_DOCS; j++) {
        Document d = new Document();
        d.add(newTextField(PRIORITY_FIELD, HIGH_PRIORITY, Field.Store.YES));
        d.add(new IntField(ID_FIELD, j, Field.Store.YES));
        writer.addDocument(d);
      }
      writer.close();

      // try a search without OR
      IndexReader reader = DirectoryReader.open(directory);
      IndexSearcher searcher = newSearcher(reader);

      Query query = new TermQuery(new Term(PRIORITY_FIELD, HIGH_PRIORITY));
      out.println("Query: " + query.toString(PRIORITY_FIELD));
      if (VERBOSE) {
        System.out.println("TEST: search query=" + query);
      }

      final Sort sort = new Sort(SortField.FIELD_SCORE,
                                 new SortField(ID_FIELD, SortField.Type.INT));

      ScoreDoc[] hits = searcher.search(query, null, MAX_DOCS, sort).scoreDocs;
      printHits(out, hits, searcher);
      checkHits(hits, MAX_DOCS, searcher);

      // try a new search with OR
      searcher = newSearcher(reader);
      hits = null;

      BooleanQuery booleanQuery = new BooleanQuery();
      booleanQuery.add(new TermQuery(new Term(PRIORITY_FIELD, HIGH_PRIORITY)), BooleanClause.Occur.SHOULD);
      booleanQuery.add(new TermQuery(new Term(PRIORITY_FIELD, MED_PRIORITY)), BooleanClause.Occur.SHOULD);
      out.println("Query: " + booleanQuery.toString(PRIORITY_FIELD));

      hits = searcher.search(booleanQuery, null, MAX_DOCS, sort).scoreDocs;
      printHits(out, hits, searcher);
      checkHits(hits, MAX_DOCS, searcher);

      reader.close();
      directory.close();
  }


  private void printHits(PrintWriter out, ScoreDoc[] hits, IndexSearcher searcher) throws IOException {
    out.println(hits.length + " total results\n");
    for (int i = 0 ; i < hits.length; i++) {
      if ( i < 10 || (i > 94 && i < 105) ) {
        StoredDocument d = searcher.doc(hits[i].doc);
        out.println(i + " " + d.get(ID_FIELD));
      }
    }
  }

  private void checkHits(ScoreDoc[] hits, int expectedCount, IndexSearcher searcher) throws IOException {
    assertEquals("total results", expectedCount, hits.length);
    for (int i = 0 ; i < hits.length; i++) {
      if (i < 10 || (i > 94 && i < 105) ) {
        StoredDocument d = searcher.doc(hits[i].doc);
        assertEquals("check " + i, String.valueOf(i), d.get(ID_FIELD));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3172.java