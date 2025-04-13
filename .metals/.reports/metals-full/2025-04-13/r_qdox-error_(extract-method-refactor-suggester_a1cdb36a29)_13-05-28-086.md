error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2940.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2940.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2940.java
text:
```scala
I@@ndexSearcher indexSearcher = new IndexSearcher(directory, true);

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

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;

import org.apache.lucene.util.LuceneTestCase;

/**
 *
 * @version $rcs = ' $Id$ ' ;
 */
public class TestBooleanScorer extends LuceneTestCase
{

  public TestBooleanScorer(String name) {
    super(name);
  }

  private static final String FIELD = "category";
  
  public void testMethod() {
    RAMDirectory directory = new RAMDirectory();

    String[] values = new String[] { "1", "2", "3", "4" };

    try {
      IndexWriter writer = new IndexWriter(directory, new WhitespaceAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
      for (int i = 0; i < values.length; i++) {
        Document doc = new Document();
        doc.add(new Field(FIELD, values[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
        writer.addDocument(doc);
      }
      writer.close();

      BooleanQuery booleanQuery1 = new BooleanQuery();
      booleanQuery1.add(new TermQuery(new Term(FIELD, "1")), BooleanClause.Occur.SHOULD);
      booleanQuery1.add(new TermQuery(new Term(FIELD, "2")), BooleanClause.Occur.SHOULD);

      BooleanQuery query = new BooleanQuery();
      query.add(booleanQuery1, BooleanClause.Occur.MUST);
      query.add(new TermQuery(new Term(FIELD, "9")), BooleanClause.Occur.MUST_NOT);

      IndexSearcher indexSearcher = new IndexSearcher(directory);
      ScoreDoc[] hits = indexSearcher.search(query, null, 1000).scoreDocs;
      assertEquals("Number of matched documents", 2, hits.length);

    }
    catch (IOException e) {
      fail(e.getMessage());
    }

  }
  
  public void testEmptyBucketWithMoreDocs() throws Exception {
    // This test checks the logic of nextDoc() when all sub scorers have docs
    // beyond the first bucket (for example). Currently, the code relies on the
    // 'more' variable to work properly, and this test ensures that if the logic
    // changes, we have a test to back it up.
    
    Similarity sim = Similarity.getDefault();
    Scorer[] scorers = new Scorer[] {new Scorer(sim) {
      private int doc = -1;
      public Explanation explain(int doc) throws IOException { return null; }
      public float score() throws IOException { return 0; }
      /** @deprecated delete in 3.0. */
      public int doc() { return 3000; }
      public int docID() { return doc; }
      /** @deprecated delete in 3.0 */
      public boolean next() throws IOException { return nextDoc() != NO_MORE_DOCS; }
      
      public int nextDoc() throws IOException {
        return doc = doc == -1 ? 3000 : NO_MORE_DOCS;
      }

      /** @deprecated delete in 3.0 */
      public boolean skipTo(int target) throws IOException {
        return advance(target) != NO_MORE_DOCS;
      }
      
      public int advance(int target) throws IOException {
        return doc = target <= 3000 ? 3000 : NO_MORE_DOCS;
      }
      
    }};
    BooleanScorer bs = new BooleanScorer(sim, 1, Arrays.asList(scorers), null);
    
    assertEquals("should have received 3000", 3000, bs.nextDoc());
    assertEquals("should have received NO_MORE_DOCS", DocIdSetIterator.NO_MORE_DOCS, bs.nextDoc());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2940.java