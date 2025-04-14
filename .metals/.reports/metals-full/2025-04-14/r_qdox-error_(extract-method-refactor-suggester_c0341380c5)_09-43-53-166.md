error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3124.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3124.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3124.java
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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.English;
import org.apache.lucene.util.LuceneTestCase;

public class TestMultiThreadTermVectors extends LuceneTestCase {
  private Directory directory;
  public int numDocs = 100;
  public int numThreads = 3;
  
  @Override
  public void setUp() throws Exception {
    super.setUp();
    directory = newDirectory();
    IndexWriter writer = new IndexWriter(directory, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy()));
    //writer.setNoCFSRatio(0.0);
    //writer.infoStream = System.out;
    FieldType customType = new FieldType(TextField.TYPE_STORED);
    customType.setTokenized(false);
    customType.setStoreTermVectors(true);
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      Field fld = newField("field", English.intToEnglish(i), customType);
      doc.add(fld);
      writer.addDocument(doc);
    }
    writer.close();
    
  }
  
  @Override
  public void tearDown() throws Exception {
    directory.close();
    super.tearDown();
  }
  
  public void test() throws Exception {
    
    IndexReader reader = null;
    
    try {
      reader = DirectoryReader.open(directory);
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
  
  @Override
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
      Fields vectors = reader.getTermVectors(docId);
      timeElapsed += System.currentTimeMillis()-start;
      
      // verify vectors result
      verifyVectors(vectors, docId);
      
      start = System.currentTimeMillis();
      Terms vector = reader.getTermVectors(docId).terms("field");
      timeElapsed += System.currentTimeMillis()-start;
      
      verifyVector(vector.iterator(null), docId);
    }
  }
  
  private void verifyVectors(Fields vectors, int num) throws IOException {
    for (String field : vectors) {
      Terms terms = vectors.terms(field);
      assert terms != null;
      verifyVector(terms.iterator(null), num);
    }
  }

  private void verifyVector(TermsEnum vector, int num) throws IOException {
    StringBuilder temp = new StringBuilder();
    while(vector.next() != null) {
      temp.append(vector.term().utf8ToString());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3124.java