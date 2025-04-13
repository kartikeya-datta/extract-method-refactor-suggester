error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3211.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3211.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3211.java
text:
```scala
w@@riter.shutdown();

package org.apache.lucene.queries.mlt;

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
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryUtils;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestMoreLikeThis extends LuceneTestCase {
  private Directory directory;
  private IndexReader reader;
  private IndexSearcher searcher;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    RandomIndexWriter writer = new RandomIndexWriter(random(), directory);
    
    // Add series of docs with specific information for MoreLikeThis
    addDoc(writer, "lucene");
    addDoc(writer, "lucene release");

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
  
  private void addDoc(RandomIndexWriter writer, String text) throws IOException {
    Document doc = new Document();
    doc.add(newTextField("text", text, Field.Store.YES));
    writer.addDocument(doc);
  }
  
  public void testBoostFactor() throws Throwable {
    Map<String,Float> originalValues = getOriginalValues();
    
    MoreLikeThis mlt = new MoreLikeThis(reader);
    mlt.setAnalyzer(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false));
    mlt.setMinDocFreq(1);
    mlt.setMinTermFreq(1);
    mlt.setMinWordLen(1);
    mlt.setFieldNames(new String[] {"text"});
    mlt.setBoost(true);
    
    // this mean that every term boost factor will be multiplied by this
    // number
    float boostFactor = 5;
    mlt.setBoostFactor(boostFactor);
    
    BooleanQuery query = (BooleanQuery) mlt.like(new StringReader(
        "lucene release"), "text");
    List<BooleanClause> clauses = query.clauses();
    
    assertEquals("Expected " + originalValues.size() + " clauses.",
        originalValues.size(), clauses.size());

    for (BooleanClause clause : clauses) {
      TermQuery tq = (TermQuery) clause.getQuery();
      Float termBoost = originalValues.get(tq.getTerm().text());
      assertNotNull("Expected term " + tq.getTerm().text(), termBoost);

      float totalBoost = termBoost * boostFactor;
      assertEquals("Expected boost of " + totalBoost + " for term '"
          + tq.getTerm().text() + "' got " + tq.getBoost(), totalBoost, tq
          .getBoost(), 0.0001);
    }
  }
  
  private Map<String,Float> getOriginalValues() throws IOException {
    Map<String,Float> originalValues = new HashMap<>();
    MoreLikeThis mlt = new MoreLikeThis(reader);
    mlt.setAnalyzer(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false));
    mlt.setMinDocFreq(1);
    mlt.setMinTermFreq(1);
    mlt.setMinWordLen(1);
    mlt.setFieldNames(new String[] {"text"});
    mlt.setBoost(true);
    BooleanQuery query = (BooleanQuery) mlt.like(new StringReader(
        "lucene release"), "text");
    List<BooleanClause> clauses = query.clauses();

    for (BooleanClause clause : clauses) {
      TermQuery tq = (TermQuery) clause.getQuery();
      originalValues.put(tq.getTerm().text(), tq.getBoost());
    }
    return originalValues;
  }
  
  // LUCENE-3326
  public void testMultiFields() throws Exception {
    MoreLikeThis mlt = new MoreLikeThis(reader);
    mlt.setAnalyzer(new MockAnalyzer(random(), MockTokenizer.WHITESPACE, false));
    mlt.setMinDocFreq(1);
    mlt.setMinTermFreq(1);
    mlt.setMinWordLen(1);
    mlt.setFieldNames(new String[] {"text", "foobar"});
    mlt.like(new StringReader("this is a test"), "foobar");
  }
  
  // just basic equals/hashcode etc
  public void testMoreLikeThisQuery() throws Exception {
    Query query = new MoreLikeThisQuery("this is a test", new String[] { "text" }, new MockAnalyzer(random()), "text");
    QueryUtils.check(random(), query, searcher);
  }
  // TODO: add tests for the MoreLikeThisQuery
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3211.java