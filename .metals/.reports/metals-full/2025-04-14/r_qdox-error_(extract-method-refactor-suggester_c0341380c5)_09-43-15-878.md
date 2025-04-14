error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3126.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3126.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3126.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredDocument;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;

public class TestLiveFieldValues extends LuceneTestCase {
  public void test() throws Exception {

    Directory dir = newFSDirectory(createTempDir("livefieldupdates"));
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));

    final IndexWriter w = new IndexWriter(dir, iwc);

    final SearcherManager mgr = new SearcherManager(w, true, new SearcherFactory() {
        @Override
        public IndexSearcher newSearcher(IndexReader r) {
          return new IndexSearcher(r);
        }
      });

    final Integer missing = -1;

    final LiveFieldValues<IndexSearcher,Integer> rt = new LiveFieldValues<IndexSearcher,Integer>(mgr, missing) {
        @Override
        protected Integer lookupFromSearcher(IndexSearcher s, String id) throws IOException {
          TermQuery tq = new TermQuery(new Term("id", id));
          TopDocs hits = s.search(tq, 1);
          assertTrue(hits.totalHits <= 1);
          if (hits.totalHits == 0) {
            return null;
          } else {
            StoredDocument doc = s.doc(hits.scoreDocs[0].doc);
            return (Integer) doc.getField("field").numericValue();
          }
        }
    };

    int numThreads = TestUtil.nextInt(random(), 2, 5);
    if (VERBOSE) {
      System.out.println(numThreads + " threads");
    }

    final CountDownLatch startingGun = new CountDownLatch(1);
    List<Thread> threads = new ArrayList<>();

    final int iters = atLeast(1000);
    final int idCount = TestUtil.nextInt(random(), 100, 10000);

    final double reopenChance = random().nextDouble()*0.01;
    final double deleteChance = random().nextDouble()*0.25;
    final double addChance = random().nextDouble()*0.5;
    
    for(int t=0;t<numThreads;t++) {
      final int threadID = t;
      final Random threadRandom = new Random(random().nextLong());
      Thread thread = new Thread() {

          @Override
          public void run() {
            try {
              Map<String,Integer> values = new HashMap<>();
              List<String> allIDs = Collections.synchronizedList(new ArrayList<String>());

              startingGun.await();
              for(int iter=0; iter<iters;iter++) {
                // Add/update a document
                Document doc = new Document();
                // Threads must not update the same id at the
                // same time:
                if (threadRandom.nextDouble() <= addChance) {
                  String id = String.format(Locale.ROOT, "%d_%04x", threadID, threadRandom.nextInt(idCount));
                  Integer field = threadRandom.nextInt(Integer.MAX_VALUE);
                  doc.add(new StringField("id", id, Field.Store.YES));
                  doc.add(new IntField("field", field.intValue(), Field.Store.YES));
                  w.updateDocument(new Term("id", id), doc);
                  rt.add(id, field);
                  if (values.put(id, field) == null) {
                    allIDs.add(id);
                  }
                }

                if (allIDs.size() > 0 && threadRandom.nextDouble() <= deleteChance) {
                  String randomID = allIDs.get(threadRandom.nextInt(allIDs.size()));
                  w.deleteDocuments(new Term("id", randomID));
                  rt.delete(randomID);
                  values.put(randomID, missing);
                }

                if (threadRandom.nextDouble() <= reopenChance || rt.size() > 10000) {
                  //System.out.println("refresh @ " + rt.size());
                  mgr.maybeRefresh();
                  if (VERBOSE) {
                    IndexSearcher s = mgr.acquire();
                    try {
                      System.out.println("TEST: reopen " + s);
                    } finally {
                      mgr.release(s);
                    }
                    System.out.println("TEST: " + values.size() + " values");
                  }
                }

                if (threadRandom.nextInt(10) == 7) {
                  assertEquals(null, rt.get("foo"));
                }

                if (allIDs.size() > 0) {
                  String randomID = allIDs.get(threadRandom.nextInt(allIDs.size()));
                  Integer expected = values.get(randomID);
                  if (expected == missing) {
                    expected = null;
                  }
                  assertEquals("id=" + randomID, expected, rt.get(randomID));
                }
              }
            } catch (Throwable t) {
              throw new RuntimeException(t);
            }
          }
        };
      threads.add(thread);
      thread.start();
    }

    startingGun.countDown();

    for(Thread thread : threads) {
      thread.join();
    }
    mgr.maybeRefresh();
    assertEquals(0, rt.size());

    rt.close();
    mgr.close();
    w.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3126.java