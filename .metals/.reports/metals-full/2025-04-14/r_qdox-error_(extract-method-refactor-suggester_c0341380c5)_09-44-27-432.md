error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3203.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3203.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3203.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.sandbox.queries;

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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.StoredDocument;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

import java.io.IOException;
import java.util.HashSet;

public class FuzzyLikeThisQueryTest extends LuceneTestCase {
  private Directory directory;
  private IndexSearcher searcher;
  private IndexReader reader;
  private Analyzer analyzer;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    analyzer = new MockAnalyzer(random());
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));

    //Add series of docs with misspelt names
    addDoc(writer, "jonathon smythe", "1");
    addDoc(writer, "jonathan smith", "2");
    addDoc(writer, "johnathon smyth", "3");
    addDoc(writer, "johnny smith", "4");
    addDoc(writer, "jonny smith", "5");
    addDoc(writer, "johnathon smythe", "6");
    reader = writer.getReader();
    writer.close();
    searcher = newSearcher(reader);
  }

  @Override
  public void tearDown() throws Exception {
    reader.close();
    directory.close();
    super.tearDown();
  }

  private void addDoc(RandomIndexWriter writer, String name, String id) throws IOException {
    Document doc = new Document();
    doc.add(newTextField("name", name, Field.Store.YES));
    doc.add(newTextField("id", id, Field.Store.YES));
    writer.addDocument(doc);
  }


  //Tests that idf ranking is not favouring rare mis-spellings over a strong edit-distance match
  public void testClosestEditDistanceMatchComesFirst() throws Throwable {
    FuzzyLikeThisQuery flt = new FuzzyLikeThisQuery(10, analyzer);
    flt.addTerms("smith", "name", 0.3f, 1);
    Query q = flt.rewrite(searcher.getIndexReader());
    HashSet<Term> queryTerms = new HashSet<>();
    q.extractTerms(queryTerms);
    assertTrue("Should have variant smythe", queryTerms.contains(new Term("name", "smythe")));
    assertTrue("Should have variant smith", queryTerms.contains(new Term("name", "smith")));
    assertTrue("Should have variant smyth", queryTerms.contains(new Term("name", "smyth")));
    TopDocs topDocs = searcher.search(flt, 1);
    ScoreDoc[] sd = topDocs.scoreDocs;
    assertTrue("score docs must match 1 doc", (sd != null) && (sd.length > 0));
    StoredDocument doc = searcher.doc(sd[0].doc);
    assertEquals("Should match most similar not most rare variant", "2", doc.get("id"));
  }

  //Test multiple input words are having variants produced
  public void testMultiWord() throws Throwable {
    FuzzyLikeThisQuery flt = new FuzzyLikeThisQuery(10, analyzer);
    flt.addTerms("jonathin smoth", "name", 0.3f, 1);
    Query q = flt.rewrite(searcher.getIndexReader());
    HashSet<Term> queryTerms = new HashSet<>();
    q.extractTerms(queryTerms);
    assertTrue("Should have variant jonathan", queryTerms.contains(new Term("name", "jonathan")));
    assertTrue("Should have variant smith", queryTerms.contains(new Term("name", "smith")));
    TopDocs topDocs = searcher.search(flt, 1);
    ScoreDoc[] sd = topDocs.scoreDocs;
    assertTrue("score docs must match 1 doc", (sd != null) && (sd.length > 0));
    StoredDocument doc = searcher.doc(sd[0].doc);
    assertEquals("Should match most similar when using 2 words", "2", doc.get("id"));
  }
  
  // LUCENE-4809
  public void testNonExistingField() throws Throwable {
    FuzzyLikeThisQuery flt = new FuzzyLikeThisQuery(10, analyzer);
    flt.addTerms("jonathin smoth", "name", 0.3f, 1);
    flt.addTerms("jonathin smoth", "this field does not exist", 0.3f, 1);
    // don't fail here just because the field doesn't exits
    Query q = flt.rewrite(searcher.getIndexReader());
    HashSet<Term> queryTerms = new HashSet<>();
    q.extractTerms(queryTerms);
    assertTrue("Should have variant jonathan", queryTerms.contains(new Term("name", "jonathan")));
    assertTrue("Should have variant smith", queryTerms.contains(new Term("name", "smith")));
    TopDocs topDocs = searcher.search(flt, 1);
    ScoreDoc[] sd = topDocs.scoreDocs;
    assertTrue("score docs must match 1 doc", (sd != null) && (sd.length > 0));
    StoredDocument doc = searcher.doc(sd[0].doc);
    assertEquals("Should match most similar when using 2 words", "2", doc.get("id"));
  }


  //Test bug found when first query word does not match anything
  public void testNoMatchFirstWordBug() throws Throwable {
    FuzzyLikeThisQuery flt = new FuzzyLikeThisQuery(10, analyzer);
    flt.addTerms("fernando smith", "name", 0.3f, 1);
    Query q = flt.rewrite(searcher.getIndexReader());
    HashSet<Term> queryTerms = new HashSet<>();
    q.extractTerms(queryTerms);
    assertTrue("Should have variant smith", queryTerms.contains(new Term("name", "smith")));
    TopDocs topDocs = searcher.search(flt, 1);
    ScoreDoc[] sd = topDocs.scoreDocs;
    assertTrue("score docs must match 1 doc", (sd != null) && (sd.length > 0));
    StoredDocument doc = searcher.doc(sd[0].doc);
    assertEquals("Should match most similar when using 2 words", "2", doc.get("id"));
  }

  public void testFuzzyLikeThisQueryEquals() {
    Analyzer analyzer = new MockAnalyzer(random());
    FuzzyLikeThisQuery fltq1 = new FuzzyLikeThisQuery(10, analyzer);
    fltq1.addTerms("javi", "subject", 0.5f, 2);
    FuzzyLikeThisQuery fltq2 = new FuzzyLikeThisQuery(10, analyzer);
    fltq2.addTerms("javi", "subject", 0.5f, 2);
    assertEquals("FuzzyLikeThisQuery with same attributes is not equal", fltq1,
        fltq2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3203.java