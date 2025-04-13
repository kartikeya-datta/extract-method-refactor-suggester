error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3103.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3103.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3103.java
text:
```scala
w@@riter.shutdown();

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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Scorer.ChildScorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

// TODO: refactor to a base class, that collects freqs from the scorer tree
// and test all queries with it
public class TestBooleanQueryVisitSubscorers extends LuceneTestCase {
  Analyzer analyzer;
  IndexReader reader;
  IndexSearcher searcher;
  Directory dir;
  
  static final String F1 = "title";
  static final String F2 = "body";
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    analyzer = new MockAnalyzer(random());
    dir = newDirectory();
    IndexWriterConfig config = newIndexWriterConfig(TEST_VERSION_CURRENT, analyzer);
    config.setMergePolicy(newLogMergePolicy()); // we will use docids to validate
    RandomIndexWriter writer = new RandomIndexWriter(random(), dir, config);
    writer.addDocument(doc("lucene", "lucene is a very popular search engine library"));
    writer.addDocument(doc("solr", "solr is a very popular search server and is using lucene"));
    writer.addDocument(doc("nutch", "nutch is an internet search engine with web crawler and is using lucene and hadoop"));
    reader = writer.getReader();
    writer.close();
    searcher = newSearcher(reader);
  }
  
  @Override
  public void tearDown() throws Exception {
    reader.close();
    dir.close();
    super.tearDown();
  }

  public void testDisjunctions() throws IOException {
    BooleanQuery bq = new BooleanQuery();
    bq.add(new TermQuery(new Term(F1, "lucene")), BooleanClause.Occur.SHOULD);
    bq.add(new TermQuery(new Term(F2, "lucene")), BooleanClause.Occur.SHOULD);
    bq.add(new TermQuery(new Term(F2, "search")), BooleanClause.Occur.SHOULD);
    Map<Integer,Integer> tfs = getDocCounts(searcher, bq);
    assertEquals(3, tfs.size()); // 3 documents
    assertEquals(3, tfs.get(0).intValue()); // f1:lucene + f2:lucene + f2:search
    assertEquals(2, tfs.get(1).intValue()); // f2:search + f2:lucene
    assertEquals(2, tfs.get(2).intValue()); // f2:search + f2:lucene
  }
  
  public void testNestedDisjunctions() throws IOException {
    BooleanQuery bq = new BooleanQuery();
    bq.add(new TermQuery(new Term(F1, "lucene")), BooleanClause.Occur.SHOULD);
    BooleanQuery bq2 = new BooleanQuery();
    bq2.add(new TermQuery(new Term(F2, "lucene")), BooleanClause.Occur.SHOULD);
    bq2.add(new TermQuery(new Term(F2, "search")), BooleanClause.Occur.SHOULD);
    bq.add(bq2, BooleanClause.Occur.SHOULD);
    Map<Integer,Integer> tfs = getDocCounts(searcher, bq);
    assertEquals(3, tfs.size()); // 3 documents
    assertEquals(3, tfs.get(0).intValue()); // f1:lucene + f2:lucene + f2:search
    assertEquals(2, tfs.get(1).intValue()); // f2:search + f2:lucene
    assertEquals(2, tfs.get(2).intValue()); // f2:search + f2:lucene
  }
  
  public void testConjunctions() throws IOException {
    BooleanQuery bq = new BooleanQuery();
    bq.add(new TermQuery(new Term(F2, "lucene")), BooleanClause.Occur.MUST);
    bq.add(new TermQuery(new Term(F2, "is")), BooleanClause.Occur.MUST);
    Map<Integer,Integer> tfs = getDocCounts(searcher, bq);
    assertEquals(3, tfs.size()); // 3 documents
    assertEquals(2, tfs.get(0).intValue()); // f2:lucene + f2:is
    assertEquals(3, tfs.get(1).intValue()); // f2:is + f2:is + f2:lucene
    assertEquals(3, tfs.get(2).intValue()); // f2:is + f2:is + f2:lucene
  }
  
  static Document doc(String v1, String v2) {
    Document doc = new Document();
    doc.add(new TextField(F1, v1, Store.YES));
    doc.add(new TextField(F2, v2, Store.YES));
    return doc;
  }
  
  static Map<Integer,Integer> getDocCounts(IndexSearcher searcher, Query query) throws IOException {
    MyCollector collector = new MyCollector();
    searcher.search(query, collector);
    return collector.docCounts;
  }
  
  static class MyCollector extends FilterCollector {

    public final Map<Integer,Integer> docCounts = new HashMap<>();
    private final Set<Scorer> tqsSet = new HashSet<>();
    
    MyCollector() {
      super(TopScoreDocCollector.create(10, true));
    }

    public LeafCollector getLeafCollector(AtomicReaderContext context)
        throws IOException {
      final int docBase = context.docBase;
      return new FilterLeafCollector(super.getLeafCollector(context)) {
        
        @Override
        public boolean acceptsDocsOutOfOrder() {
          return false;
        }
        
        @Override
        public void setScorer(Scorer scorer) throws IOException {
          super.setScorer(scorer);
          tqsSet.clear();
          fillLeaves(scorer, tqsSet);
        }
        
        @Override
        public void collect(int doc) throws IOException {
          int freq = 0;
          for(Scorer scorer : tqsSet) {
            if (doc == scorer.docID()) {
              freq += scorer.freq();
            }
          }
          docCounts.put(doc + docBase, freq);
          super.collect(doc);
        }
        
      };
    }
    
    private void fillLeaves(Scorer scorer, Set<Scorer> set) {
      if (scorer.getWeight().getQuery() instanceof TermQuery) {
        set.add(scorer);
      } else {
        for (ChildScorer child : scorer.getChildren()) {
          fillLeaves(child.child, set);
        }
      }
    }
    
    public TopDocs topDocs(){
      return ((TopDocsCollector<?>) in).topDocs();
    }
    
    public int freq(int doc) throws IOException {
      return docCounts.get(doc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3103.java