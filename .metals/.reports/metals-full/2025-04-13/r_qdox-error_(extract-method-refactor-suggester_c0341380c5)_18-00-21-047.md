error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3141.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3141.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3141.java
text:
```scala
i@@w.shutdown();

package org.apache.lucene.index;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.LuceneTestCase.SuppressCodecs;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util.TestUtil;
import org.apache.lucene.util.TestUtil;

/**
 * Simple test that adds numeric terms, where each term has the 
 * docFreq of its integer value, and checks that the docFreq is correct. 
 */
@SuppressCodecs({"Direct", "Memory"}) // at night this makes like 200k/300k docs and will make Direct's heart beat!
public class TestBagOfPostings extends LuceneTestCase {
  public void test() throws Exception {
    List<String> postingsList = new ArrayList<>();
    int numTerms = atLeast(300);
    final int maxTermsPerDoc = TestUtil.nextInt(random(), 10, 20);

    boolean isSimpleText = "SimpleText".equals(TestUtil.getPostingsFormat("field"));

    IndexWriterConfig iwc = newIndexWriterConfig(random(), TEST_VERSION_CURRENT, new MockAnalyzer(random()));

    if ((isSimpleText || iwc.getMergePolicy() instanceof MockRandomMergePolicy) && (TEST_NIGHTLY || RANDOM_MULTIPLIER > 1)) {
      // Otherwise test can take way too long (> 2 hours)
      numTerms /= 2;
    }

    if (VERBOSE) {
      System.out.println("maxTermsPerDoc=" + maxTermsPerDoc);
      System.out.println("numTerms=" + numTerms);
    }

    for (int i = 0; i < numTerms; i++) {
      String term = Integer.toString(i);
      for (int j = 0; j < i; j++) {
        postingsList.add(term);
      }
    }
    Collections.shuffle(postingsList, random());

    final ConcurrentLinkedQueue<String> postings = new ConcurrentLinkedQueue<>(postingsList);

    Directory dir = newFSDirectory(createTempDir("bagofpostings"));
    final RandomIndexWriter iw = new RandomIndexWriter(random(), dir, iwc);

    int threadCount = TestUtil.nextInt(random(), 1, 5);
    if (VERBOSE) {
      System.out.println("config: " + iw.w.getConfig());
      System.out.println("threadCount=" + threadCount);
    }

    Thread[] threads = new Thread[threadCount];
    final CountDownLatch startingGun = new CountDownLatch(1);

    for(int threadID=0;threadID<threadCount;threadID++) {
      threads[threadID] = new Thread() {
          @Override
          public void run() {
            try {
              Document document = new Document();
              Field field = newTextField("field", "", Field.Store.NO);
              document.add(field);
              startingGun.await();
              while (!postings.isEmpty()) {
                StringBuilder text = new StringBuilder();
                Set<String> visited = new HashSet<>();
                for (int i = 0; i < maxTermsPerDoc; i++) {
                  String token = postings.poll();
                  if (token == null) {
                    break;
                  }
                  if (visited.contains(token)) {
                    // Put it back:
                    postings.add(token);
                    break;
                  }
                  text.append(' ');
                  text.append(token);
                  visited.add(token);
                }
                field.setStringValue(text.toString());
                iw.addDocument(document);
              }
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }
        };
      threads[threadID].start();
    }
    startingGun.countDown();
    for(Thread t : threads) {
      t.join();
    }
    
    iw.forceMerge(1);
    DirectoryReader ir = iw.getReader();
    assertEquals(1, ir.leaves().size());
    AtomicReader air = ir.leaves().get(0).reader();
    Terms terms = air.terms("field");
    // numTerms-1 because there cannot be a term 0 with 0 postings:
    assertEquals(numTerms-1, terms.size());
    TermsEnum termsEnum = terms.iterator(null);
    BytesRef term;
    while ((term = termsEnum.next()) != null) {
      int value = Integer.parseInt(term.utf8ToString());
      assertEquals(value, termsEnum.docFreq());
      // don't really need to check more than this, as CheckIndex
      // will verify that docFreq == actual number of documents seen
      // from a docsAndPositionsEnum.
    }
    ir.close();
    iw.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3141.java