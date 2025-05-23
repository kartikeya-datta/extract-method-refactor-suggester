error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2505.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2505.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2505.java
text:
```scala
D@@irectory rd = FSDirectory.getDirectory(indexDir);

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

import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

/**
 * Tests for the "IndexModifier" class, including accesses from two threads at the
 * same time.
 * 
 * @author Daniel Naber
 * @deprecated
 */
public class TestIndexModifier extends LuceneTestCase {

  private int docCount = 0;
  
  private final Term allDocTerm = new Term("all", "x");

  public void testIndex() throws IOException {
    Directory ramDir = new RAMDirectory();
    IndexModifier i = new IndexModifier(ramDir, new StandardAnalyzer(), true);
    i.addDocument(getDoc());
    assertEquals(1, i.docCount());
    i.flush();
    i.addDocument(getDoc(), new SimpleAnalyzer());
    assertEquals(2, i.docCount());
    i.optimize();
    assertEquals(2, i.docCount());
    i.flush();
    i.deleteDocument(0);
    assertEquals(1, i.docCount());
    i.flush();
    assertEquals(1, i.docCount());
    i.addDocument(getDoc());
    i.addDocument(getDoc());
    i.flush();
    // depend on merge policy - assertEquals(3, i.docCount());
    i.deleteDocuments(allDocTerm);
    assertEquals(0, i.docCount());
    i.optimize();
    assertEquals(0, i.docCount());
    
    //  Lucene defaults:
    assertNull(i.getInfoStream());
    assertTrue(i.getUseCompoundFile());
    assertEquals(IndexWriter.DISABLE_AUTO_FLUSH, i.getMaxBufferedDocs());
    assertEquals(10000, i.getMaxFieldLength());
    assertEquals(10, i.getMergeFactor());
    // test setting properties:
    i.setMaxBufferedDocs(100);
    i.setMergeFactor(25);
    i.setMaxFieldLength(250000);
    i.addDocument(getDoc());
    i.setUseCompoundFile(false);
    i.flush();
    assertEquals(100, i.getMaxBufferedDocs());
    assertEquals(25, i.getMergeFactor());
    assertEquals(250000, i.getMaxFieldLength());
    assertFalse(i.getUseCompoundFile());

    // test setting properties when internally the reader is opened:
    i.deleteDocuments(allDocTerm);
    i.setMaxBufferedDocs(100);
    i.setMergeFactor(25);
    i.setMaxFieldLength(250000);
    i.addDocument(getDoc());
    i.setUseCompoundFile(false);
    i.optimize();
    assertEquals(100, i.getMaxBufferedDocs());
    assertEquals(25, i.getMergeFactor());
    assertEquals(250000, i.getMaxFieldLength());
    assertFalse(i.getUseCompoundFile());

    i.close();
    try {
      i.docCount();
      fail();
    } catch (IllegalStateException e) {
      // expected exception
    }
  }

  public void testExtendedIndex() throws IOException {
    Directory ramDir = new RAMDirectory();
    PowerIndex powerIndex = new PowerIndex(ramDir, new StandardAnalyzer(), true);
    powerIndex.addDocument(getDoc());
    powerIndex.addDocument(getDoc());
    powerIndex.addDocument(getDoc());
    powerIndex.addDocument(getDoc());
    powerIndex.addDocument(getDoc());
    powerIndex.flush();
    assertEquals(5, powerIndex.docFreq(allDocTerm));
    powerIndex.close();
  }
  
  private Document getDoc() {
    Document doc = new Document();
    doc.add(new Field("body", Integer.toString(docCount), Field.Store.YES, Field.Index.UN_TOKENIZED));
    doc.add(new Field("all", "x", Field.Store.YES, Field.Index.UN_TOKENIZED));
    docCount++;
    return doc;
  }
  
  public void testIndexWithThreads() throws IOException {
    testIndexInternal(0);
    testIndexInternal(10);
    testIndexInternal(50);
  }
  
  private void testIndexInternal(int maxWait) throws IOException {
    final boolean create = true;
    //Directory rd = new RAMDirectory();
    // work on disk to make sure potential lock problems are tested:
    String tempDir = System.getProperty("java.io.tmpdir");
    if (tempDir == null)
      throw new IOException("java.io.tmpdir undefined, cannot run test");
    File indexDir = new File(tempDir, "lucenetestindex");
    Directory rd = FSDirectory.getDirectory(indexDir, null, false);
    IndexThread.id = 0;
    IndexThread.idStack.clear();
    IndexModifier index = new IndexModifier(rd, new StandardAnalyzer(), create);
    IndexThread thread1 = new IndexThread(index, maxWait, 1);
    thread1.start();
    IndexThread thread2 = new IndexThread(index, maxWait, 2);
    thread2.start();
    while(thread1.isAlive() || thread2.isAlive()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    index.optimize();
    int added = thread1.added + thread2.added;
    int deleted = thread1.deleted + thread2.deleted;
    assertEquals(added-deleted, index.docCount());
    index.close();
    
    try {
      index.close();
      fail();
    } catch(IllegalStateException e) {
      // expected exception
    }
    rmDir(indexDir);
  }
  
  private void rmDir(File dir) {
    File[] files = dir.listFiles();
    for (int i = 0; i < files.length; i++) {
      files[i].delete();
    }
    dir.delete();
  }
  
  private class PowerIndex extends IndexModifier {
    public PowerIndex(Directory dir, Analyzer analyzer, boolean create) throws IOException {
      super(dir, analyzer, create);
    }
    public int docFreq(Term term) throws IOException {
      synchronized(directory) {
        assureOpen();
        createIndexReader();
        return indexReader.docFreq(term);
      }
    }
  }
  
}

class IndexThread extends Thread {

  private final static int ITERATIONS = 500;       // iterations of thread test

  static int id = 0;
  static Stack idStack = new Stack();

  int added = 0;
  int deleted = 0;

  private int maxWait = 10;
  private IndexModifier index;
  private int threadNumber;
  private Random random;
  
  IndexThread(IndexModifier index, int maxWait, int threadNumber) {
    this.index = index;
    this.maxWait = maxWait;
    this.threadNumber = threadNumber;
    // TODO: test case is not reproducible despite pseudo-random numbers:
    random = new Random(101+threadNumber);        // constant seed for better reproducability
  }
  
  public void run() {
    try {
      for(int i = 0; i < ITERATIONS; i++) {
        int rand = random.nextInt(101);
        if (rand < 5) {
          index.optimize();
        } else if (rand < 60) {
          Document doc = getDocument();
          index.addDocument(doc);
          idStack.push(doc.get("id"));
          added++;
        } else {
          // we just delete the last document added and remove it
          // from the id stack so that it won't be removed twice:
          String delId = null;
          try {
            delId = (String)idStack.pop();
          } catch (EmptyStackException e) {
            continue;
          }
          Term delTerm = new Term("id", new Integer(delId).toString());
          int delCount = index.deleteDocuments(delTerm);
          if (delCount != 1) {
            throw new RuntimeException("Internal error: " + threadNumber + " deleted " + delCount + 
                " documents, term=" + delTerm);
          }
          deleted++;
        }
        if (maxWait > 0) {
          try {
            rand = random.nextInt(maxWait);
            //System.out.println("waiting " + rand + "ms");
            Thread.sleep(rand);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Document getDocument() {
    Document doc = new Document();
    synchronized (getClass()) {
      doc.add(new Field("id", Integer.toString(id), Field.Store.YES,
          Field.Index.UN_TOKENIZED));
      id++;
    }
    // add random stuff:
    doc.add(new Field("content", Integer.toString(random.nextInt(1000)), Field.Store.YES,
        Field.Index.TOKENIZED));
    doc.add(new Field("content", Integer.toString(random.nextInt(1000)), Field.Store.YES,
        Field.Index.TOKENIZED));
    doc.add(new Field("all", "x", Field.Store.YES, Field.Index.TOKENIZED));
    return doc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2505.java