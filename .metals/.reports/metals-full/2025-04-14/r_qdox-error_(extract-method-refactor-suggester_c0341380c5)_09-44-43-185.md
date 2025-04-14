error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2954.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2954.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2954.java
text:
```scala
r@@eader = IndexReader.open(directory, true);

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
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.English;

import java.io.IOException;

/**
 *
 * @version $rcs = ' $Id$ ' ;
 */
public class TestMultiThreadTermVectors extends LuceneTestCase {
  private RAMDirectory directory = new RAMDirectory();
  public int numDocs = 100;
  public int numThreads = 3;
  
  public TestMultiThreadTermVectors(String s) {
    super(s);
  }
  
  public void setUp() throws Exception {
    super.setUp();
    IndexWriter writer
            = new IndexWriter(directory, new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.LIMITED);
    //writer.setUseCompoundFile(false);
    //writer.infoStream = System.out;
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      Fieldable fld = new Field("field", English.intToEnglish(i), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES);
      doc.add(fld);
      writer.addDocument(doc);
    }
    writer.close();
    
  }
  
  public void test() throws Exception {
    
    IndexReader reader = null;
    
    try {
      reader = IndexReader.open(directory);
      for(int i = 1; i <= numThreads; i++)
        testTermPositionVectors(reader, i);
      
      
    }
    catch (IOException ioe) {
      fail(ioe.getMessage());
    }
    finally {
      if (reader != null) {
        try {
          /** close the opened reader */
          reader.close();
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    }
  }
  
  public void testTermPositionVectors(final IndexReader reader, int threadCount) throws Exception {
    MultiThreadTermVectorsReader[] mtr = new MultiThreadTermVectorsReader[threadCount];
    for (int i = 0; i < threadCount; i++) {
      mtr[i] = new MultiThreadTermVectorsReader();
      mtr[i].init(reader);
    }
    
    
    /** run until all threads finished */ 
    int threadsAlive = mtr.length;
    while (threadsAlive > 0) {
        //System.out.println("Threads alive");
        Thread.sleep(10);
        threadsAlive = mtr.length;
        for (int i = 0; i < mtr.length; i++) {
          if (mtr[i].isAlive() == true) {
            break;
          }
          
          threadsAlive--; 
        }
    }
    
    long totalTime = 0L;
    for (int i = 0; i < mtr.length; i++) {
      totalTime += mtr[i].timeElapsed;
      mtr[i] = null;
    }
    
    //System.out.println("threadcount: " + mtr.length + " average term vector time: " + totalTime/mtr.length);
    
  }
  
}

class MultiThreadTermVectorsReader implements Runnable {
  
  private IndexReader reader = null;
  private Thread t = null;
  
  private final int runsToDo = 100;
  long timeElapsed = 0;
  
  
  public void init(IndexReader reader) {
    this.reader = reader;
    timeElapsed = 0;
    t=new Thread(this);
    t.start();
  }
    
  public boolean isAlive() {
    if (t == null) return false;
    
    return t.isAlive();
  }
  
  public void run() {
      try {
        // run the test 100 times
        for (int i = 0; i < runsToDo; i++)
          testTermVectors();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return;
  }
  
  private void testTermVectors() throws Exception {
    // check:
    int numDocs = reader.numDocs();
    long start = 0L;
    for (int docId = 0; docId < numDocs; docId++) {
      start = System.currentTimeMillis();
      TermFreqVector [] vectors = reader.getTermFreqVectors(docId);
      timeElapsed += System.currentTimeMillis()-start;
      
      // verify vectors result
      verifyVectors(vectors, docId);
      
      start = System.currentTimeMillis();
      TermFreqVector vector = reader.getTermFreqVector(docId, "field");
      timeElapsed += System.currentTimeMillis()-start;
      
      vectors = new TermFreqVector[1];
      vectors[0] = vector;
      
      verifyVectors(vectors, docId);
      
    }
  }
  
  private void verifyVectors(TermFreqVector[] vectors, int num) {
    StringBuffer temp = new StringBuffer();
    String[] terms = null;
    for (int i = 0; i < vectors.length; i++) {
      terms = vectors[i].getTerms();
      for (int z = 0; z < terms.length; z++) {
        temp.append(terms[z]);
      }
    }
    
    if (!English.intToEnglish(num).trim().equals(temp.toString().trim()))
        System.out.println("wrong term result");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2954.java