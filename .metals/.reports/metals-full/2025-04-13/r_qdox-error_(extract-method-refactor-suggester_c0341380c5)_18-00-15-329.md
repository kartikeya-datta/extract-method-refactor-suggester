error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/768.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/768.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/768.java
text:
```scala
M@@ockDirectoryWrapper dir = newMockDirectory();

package org.apache.lucene;

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

import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.MergePolicy;
import org.apache.lucene.index.ConcurrentMergeScheduler;
import org.apache.lucene.index.MergeScheduler;
import org.apache.lucene.index.MergePolicy.OneMerge;
import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Holds tests cases to verify external APIs are accessible
 * while not being in org.apache.lucene.index package.
 */
public class TestMergeSchedulerExternal extends LuceneTestCase {

  volatile boolean mergeCalled;
  volatile boolean mergeThreadCreated;
  volatile boolean excCalled;

  private class MyMergeScheduler extends ConcurrentMergeScheduler {

    private class MyMergeThread extends ConcurrentMergeScheduler.MergeThread {
      public MyMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) {
        super(writer, merge);
        mergeThreadCreated = true;
      }
    }

    @Override
    protected MergeThread getMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException {
      MergeThread thread = new MyMergeThread(writer, merge);
      thread.setThreadPriority(getMergeThreadPriority());
      thread.setDaemon(true);
      thread.setName("MyMergeThread");
      return thread;
    }

    @Override
    protected void handleMergeException(Throwable t) {
      excCalled = true;
    }

    @Override
    protected void doMerge(MergePolicy.OneMerge merge) throws IOException {
      mergeCalled = true;
      super.doMerge(merge);
    }
  }

  private static class FailOnlyOnMerge extends MockDirectoryWrapper.Failure {
    @Override
    public void eval(MockDirectoryWrapper dir)  throws IOException {
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 0; i < trace.length; i++) {
        if ("doMerge".equals(trace[i].getMethodName()))
          throw new IOException("now failing during merge");
      }
    }
  }

  public void testSubclassConcurrentMergeScheduler() throws IOException {
    MockDirectoryWrapper dir = newDirectory();
    dir.failOn(new FailOnlyOnMerge());

    Document doc = new Document();
    Field idField = newStringField("id", "", Field.Store.YES);
    doc.add(idField);
    
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergeScheduler(new MyMergeScheduler())
        .setMaxBufferedDocs(2).setRAMBufferSizeMB(IndexWriterConfig.DISABLE_AUTO_FLUSH)
        .setMergePolicy(newLogMergePolicy()));
    LogMergePolicy logMP = (LogMergePolicy) writer.getConfig().getMergePolicy();
    logMP.setMergeFactor(10);
    for(int i=0;i<20;i++)
      writer.addDocument(doc);

    ((MyMergeScheduler) writer.getConfig().getMergeScheduler()).sync();
    writer.close();
    
    assertTrue(mergeThreadCreated);
    assertTrue(mergeCalled);
    assertTrue(excCalled);
    dir.close();
  }
  
  private static class ReportingMergeScheduler extends MergeScheduler {

    @Override
    public void merge(IndexWriter writer) throws IOException {
      OneMerge merge = null;
      while ((merge = writer.getNextMerge()) != null) {
        if (VERBOSE) {
          System.out.println("executing merge " + merge.segString(writer.getDirectory()));
        }
        writer.merge(merge);
      }
    }

    @Override
    public void close() throws IOException {}
    
  }

  public void testCustomMergeScheduler() throws Exception {
    // we don't really need to execute anything, just to make sure the custom MS
    // compiles. But ensure that it can be used as well, e.g., no other hidden
    // dependencies or something. Therefore, don't use any random API !
    Directory dir = new RAMDirectory();
    IndexWriterConfig conf = new IndexWriterConfig(TEST_VERSION_CURRENT, null);
    conf.setMergeScheduler(new ReportingMergeScheduler());
    IndexWriter writer = new IndexWriter(dir, conf);
    writer.addDocument(new Document());
    writer.commit(); // trigger flush
    writer.addDocument(new Document());
    writer.commit(); // trigger flush
    writer.forceMerge(1);
    writer.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/768.java