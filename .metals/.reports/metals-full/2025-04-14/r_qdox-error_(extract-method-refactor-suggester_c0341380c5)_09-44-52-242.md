error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3400.java
text:
```scala
d@@oTest(10,10);

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

import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.*;

import java.util.Random;
import java.util.List;
import java.io.IOException;

public class TestThreadSafe extends LuceneTestCase {
  Directory dir1;

  IndexReader ir1;

  String failure=null;


  class Thr extends Thread {
    final int iter;
    final Random rand;
    // pass in random in case we want to make things reproducable
    public Thr(int iter, Random rand) {
      this.iter = iter;
      this.rand = rand;
    }

    @Override
    public void run() {
      try {
        for (int i=0; i<iter; i++) {
          /*** future
           // pick a random index reader... a shared one, or create your own
           IndexReader ir;
           ***/

          switch(rand.nextInt(1)) {
            case 0: loadDoc(ir1); break;
          }

        }
      } catch (Throwable th) {
        failure=th.toString();
        fail(failure);
      }
    }


    void loadDoc(IndexReader ir) throws IOException {
      // beware of deleted docs in the future
      Document doc = ir.document(rand.nextInt(ir.maxDoc()),
                new FieldSelector() {
                  public FieldSelectorResult accept(String fieldName) {
                    switch(rand.nextInt(2)) {
                      case 0: return FieldSelectorResult.LAZY_LOAD;
                      case 1: return FieldSelectorResult.LOAD;
                      // TODO: add other options
                      default: return FieldSelectorResult.LOAD;
                    }
                  }
                }
              );

      List<Fieldable> fields = doc.getFields();
      for (final Fieldable f : fields ) {
        validateField(f);
      }

    }

  }


  void validateField(Fieldable f) {
    String val = f.stringValue();
    if (!val.startsWith("^") || !val.endsWith("$")) {
      throw new RuntimeException("Invalid field:" + f.toString() + " val=" +val);
    }
  }

  String[] words = "now is the time for all good men to come to the aid of their country".split(" ");

  void buildDir(Directory dir, int nDocs, int maxFields, int maxFieldLen) throws IOException {
    IndexWriter iw = new IndexWriter(dir, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer()).setOpenMode(OpenMode.CREATE).setMaxBufferedDocs(10));
    for (int j=0; j<nDocs; j++) {
      Document d = new Document();
      int nFields = random.nextInt(maxFields);
      for (int i=0; i<nFields; i++) {
        int flen = random.nextInt(maxFieldLen);
        StringBuilder sb = new StringBuilder("^ ");
        while (sb.length() < flen) sb.append(' ').append(words[random.nextInt(words.length)]);
        sb.append(" $");
        Field.Store store = Field.Store.YES;  // make random later
        Field.Index index = Field.Index.ANALYZED;  // make random later
        d.add(newField("f"+i, sb.toString(), store, index));
      }
      iw.addDocument(d);
    }
    iw.close();
  }


  void doTest(int iter, int nThreads) throws Exception {
    Thr[] tarr = new Thr[nThreads];
    for (int i=0; i<nThreads; i++) {
      tarr[i] = new Thr(iter, new Random(random.nextLong()));
      tarr[i].start();
    }
    for (int i=0; i<nThreads; i++) {
      tarr[i].join();
    }
    if (failure!=null) {
      fail(failure);
    }
  }

  public void testLazyLoadThreadSafety() throws Exception{
    dir1 = newDirectory();
    // test w/ field sizes bigger than the buffer of an index input
    buildDir(dir1, 15, 5, 2000);

    // do many small tests so the thread locals go away inbetween
    int num = 100 * RANDOM_MULTIPLIER;
    for (int i = 0; i < num; i++) {
      ir1 = IndexReader.open(dir1, false);
      doTest(10,100);
      ir1.close();
    }
    dir1.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3400.java