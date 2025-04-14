error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1842.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1842.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1842.java
text:
```scala
D@@ocument doc = DocHelper.createDocument(i, "index1", 10);

package org.apache.lucene.index;

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

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LuceneTestCase;

public class TestNRTReaderWithThreads extends LuceneTestCase {
  AtomicInteger seq = new AtomicInteger(1);

  public void testIndexing() throws Exception {
    Directory mainDir = newDirectory();
    IndexWriter writer = new IndexWriter(
        mainDir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).
            setMaxBufferedDocs(10).
            setMergePolicy(newLogMergePolicy(false,2))
    );
    writer.setInfoStream(VERBOSE ? System.out : null);
    IndexReader reader = writer.getReader(); // start pooling readers
    reader.close();
    RunThread[] indexThreads = new RunThread[4];
    for (int x=0; x < indexThreads.length; x++) {
      indexThreads[x] = new RunThread(x % 2, writer);
      indexThreads[x].setName("Thread " + x);
      indexThreads[x].start();
    }    
    long startTime = System.currentTimeMillis();
    long duration = 1000;
    while ((System.currentTimeMillis() - startTime) < duration) {
      Thread.sleep(100);
    }
    int delCount = 0;
    int addCount = 0;
    for (int x=0; x < indexThreads.length; x++) {
      indexThreads[x].run = false;
      assertNull("Exception thrown: "+indexThreads[x].ex, indexThreads[x].ex);
      addCount += indexThreads[x].addCount;
      delCount += indexThreads[x].delCount;
    }
    for (int x=0; x < indexThreads.length; x++) {
      indexThreads[x].join();
    }
    for (int x=0; x < indexThreads.length; x++) {
      assertNull("Exception thrown: "+indexThreads[x].ex, indexThreads[x].ex);
    }
    //System.out.println("addCount:"+addCount);
    //System.out.println("delCount:"+delCount);
    writer.close();
    mainDir.close();
  }

  public class RunThread extends Thread {
    IndexWriter writer;
    volatile boolean run = true;
    volatile Throwable ex;
    int delCount = 0;
    int addCount = 0;
    int type;
    final Random r = new Random(random.nextLong());
    
    public RunThread(int type, IndexWriter writer) {
      this.type = type;
      this.writer = writer;
    }

    @Override
    public void run() {
      try {
        while (run) {
          //int n = random.nextInt(2);
          if (type == 0) {
            int i = seq.addAndGet(1);
            Document doc = TestIndexWriterReader.createDocument(i, "index1", 10);
            writer.addDocument(doc);
            addCount++;
          } else if (type == 1) {
            // we may or may not delete because the term may not exist,
            // however we're opening and closing the reader rapidly
            IndexReader reader = writer.getReader();
            int id = r.nextInt(seq.intValue());
            Term term = new Term("id", Integer.toString(id));
            int count = TestIndexWriterReader.count(term, reader);
            writer.deleteDocuments(term);
            reader.close();
            delCount += count;
          }
        }
      } catch (Throwable ex) {
        ex.printStackTrace(System.out);
        this.ex = ex;
        run = false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1842.java