error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2853.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2853.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2853.java
text:
```scala
public v@@oid merge(MergePolicy.OneMerge merge) throws IOException {

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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.LineFileDocs;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestForceMergeForever extends LuceneTestCase {

  // Just counts how many merges are done
  private static class MyIndexWriter extends IndexWriter {

    AtomicInteger mergeCount = new AtomicInteger();
    private boolean first;

    public MyIndexWriter(Directory dir, IndexWriterConfig conf) throws Exception {
      super(dir, conf);
    }

    @Override
    public void merge(MergePolicy.OneMerge merge) throws CorruptIndexException, IOException {
      if (merge.maxNumSegments != -1 && (first || merge.segments.size() == 1)) {
        first = false;
        if (VERBOSE) {
          System.out.println("TEST: maxNumSegments merge");
        }
        mergeCount.incrementAndGet();
      }
      super.merge(merge);
    }
  }

  public void test() throws Exception {
    final Directory d = newDirectory();
    final MyIndexWriter w = new MyIndexWriter(d, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));

    // Try to make an index that requires merging:
    w.getConfig().setMaxBufferedDocs(_TestUtil.nextInt(random(), 2, 11));
    final int numStartDocs = atLeast(20);
    final LineFileDocs docs = new LineFileDocs(random(), defaultCodecSupportsDocValues());
    for(int docIDX=0;docIDX<numStartDocs;docIDX++) {
      w.addDocument(docs.nextDoc());
    }
    MergePolicy mp = w.getConfig().getMergePolicy();
    final int mergeAtOnce = 1+w.segmentInfos.size();
    if (mp instanceof TieredMergePolicy) {
      ((TieredMergePolicy) mp).setMaxMergeAtOnce(mergeAtOnce);
    } else if (mp instanceof LogMergePolicy) {
      ((LogMergePolicy) mp).setMergeFactor(mergeAtOnce);
    } else {
      // skip test
      w.close();
      d.close();
      return;
    }

    final AtomicBoolean doStop = new AtomicBoolean();
    w.getConfig().setMaxBufferedDocs(2);
    Thread t = new Thread() {
      @Override
      public void run() {
        try {
          while (!doStop.get()) {
            w.updateDocument(new Term("docid", "" + random().nextInt(numStartDocs)),
                             docs.nextDoc());
            // Force deletes to apply
            w.getReader().close();
          }
        } catch (Throwable t) {
          throw new RuntimeException(t);
        }
      }
      };
    t.start();
    w.forceMerge(1);
    doStop.set(true);
    t.join();
    assertTrue("merge count is " + w.mergeCount.get(), w.mergeCount.get() <= 1);
    w.close();
    d.close();
    docs.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2853.java