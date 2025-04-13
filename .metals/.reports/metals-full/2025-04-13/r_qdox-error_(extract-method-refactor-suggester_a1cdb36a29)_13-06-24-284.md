error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3120.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3120.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3120.java
text:
```scala
w@@.shutdown();

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.RandomIndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LineFileDocs;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

public class TestSameScoresWithThreads extends LuceneTestCase {

  public void test() throws Exception {
    final Directory dir = newDirectory();
    MockAnalyzer analyzer = new MockAnalyzer(random());
    analyzer.setMaxTokenLength(TestUtil.nextInt(random(), 1, IndexWriter.MAX_TERM_LENGTH));
    final RandomIndexWriter w = new RandomIndexWriter(random(), dir, analyzer);
    LineFileDocs docs = new LineFileDocs(random());
    int charsToIndex = atLeast(100000);
    int charsIndexed = 0;
    //System.out.println("bytesToIndex=" + charsToIndex);
    while(charsIndexed < charsToIndex) {
      Document doc = docs.nextDoc();
      charsIndexed += doc.get("body").length();
      w.addDocument(doc);
      //System.out.println("  bytes=" + charsIndexed + " add: " + doc);
    }
    IndexReader r = w.getReader();
    //System.out.println("numDocs=" + r.numDocs());
    w.close();

    final IndexSearcher s = newSearcher(r);
    Terms terms = MultiFields.getFields(r).terms("body");
    int termCount = 0;
    TermsEnum termsEnum = terms.iterator(null);
    while(termsEnum.next() != null) {
      termCount++;
    }
    assertTrue(termCount > 0);
    
    // Target ~10 terms to search:
    double chance = 10.0 / termCount;
    termsEnum = terms.iterator(termsEnum);
    final Map<BytesRef,TopDocs> answers = new HashMap<>();
    while(termsEnum.next() != null) {
      if (random().nextDouble() <= chance) {
        BytesRef term = BytesRef.deepCopyOf(termsEnum.term());
        answers.put(term,
                    s.search(new TermQuery(new Term("body", term)), 100));
      }
    }

    if (!answers.isEmpty()) {
      final CountDownLatch startingGun = new CountDownLatch(1);
      int numThreads = TestUtil.nextInt(random(), 2, 5);
      Thread[] threads = new Thread[numThreads];
      for(int threadID=0;threadID<numThreads;threadID++) {
        Thread thread = new Thread() {
            @Override
            public void run() {
              try {
                startingGun.await();
                for(int i=0;i<20;i++) {
                  List<Map.Entry<BytesRef,TopDocs>> shuffled = new ArrayList<>(answers.entrySet());
                  Collections.shuffle(shuffled);
                  for(Map.Entry<BytesRef,TopDocs> ent : shuffled) {
                    TopDocs actual = s.search(new TermQuery(new Term("body", ent.getKey())), 100);
                    TopDocs expected = ent.getValue();
                    assertEquals(expected.totalHits, actual.totalHits);
                    assertEquals("query=" + ent.getKey().utf8ToString(), expected.scoreDocs.length, actual.scoreDocs.length);
                    for(int hit=0;hit<expected.scoreDocs.length;hit++) {
                      assertEquals(expected.scoreDocs[hit].doc, actual.scoreDocs[hit].doc);
                      // Floats really should be identical:
                      assertTrue(expected.scoreDocs[hit].score == actual.scoreDocs[hit].score);
                    }
                  }
                }
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            }
          };
        threads[threadID] = thread;
        thread.start();
      }
      startingGun.countDown();
      for(Thread thread : threads) {
        thread.join();
      }
    }
    r.close();
    dir.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3120.java