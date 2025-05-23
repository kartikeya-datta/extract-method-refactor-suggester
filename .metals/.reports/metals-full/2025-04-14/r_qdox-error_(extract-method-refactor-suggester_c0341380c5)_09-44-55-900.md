error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2680.java
text:
```scala
I@@ndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).setReaderTermsIndexDivisor(2));

package org.apache.lucene.index;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockRAMDirectory;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.util.ThreadInterruptedException;

public class TestIndexWriterReader extends LuceneTestCase {
  static PrintStream infoStream;

  public static class HeavyAtomicInt {
    private int value;
    public HeavyAtomicInt(int start) {
      value = start;
    }
    public synchronized int addAndGet(int inc) {
      value += inc;
      return value;
    }
    public synchronized int incrementAndGet() {
      value++;
      return value;
    }
    public synchronized int intValue() {
      return value;
    }
  }
  
  public static int count(Term t, IndexReader r) throws IOException {
    int count = 0;
    TermDocs td = r.termDocs(t);
    while (td.next()) {
      td.doc();
      count++;
    }
    td.close();
    return count;
  }

  public void testUpdateDocument() throws Exception {
    boolean optimize = true;

    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));

    // create the index
    createIndexNoClose(!optimize, "index1", writer);

    // writer.flush(false, true, true);

    // get a reader
    IndexReader r1 = writer.getReader();
    assertTrue(r1.isCurrent());

    String id10 = r1.document(10).getField("id").stringValue();
    
    Document newDoc = r1.document(10);
    newDoc.removeField("id");
    newDoc.add(new Field("id", Integer.toString(8000), Store.YES, Index.NOT_ANALYZED));
    writer.updateDocument(new Term("id", id10), newDoc);
    assertFalse(r1.isCurrent());

    IndexReader r2 = writer.getReader();
    assertTrue(r2.isCurrent());
    assertEquals(0, count(new Term("id", id10), r2));
    assertEquals(1, count(new Term("id", Integer.toString(8000)), r2));
    
    r1.close();
    writer.close();
    assertTrue(r2.isCurrent());
    
    IndexReader r3 = IndexReader.open(dir1, true);
    assertTrue(r3.isCurrent());
    assertTrue(r2.isCurrent());
    assertEquals(0, count(new Term("id", id10), r3));
    assertEquals(1, count(new Term("id", Integer.toString(8000)), r3));

    writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    Document doc = new Document();
    doc.add(new Field("field", "a b c", Field.Store.NO, Field.Index.ANALYZED));
    writer.addDocument(doc);
    assertTrue(r2.isCurrent());
    assertTrue(r3.isCurrent());

    writer.close();

    assertFalse(r2.isCurrent());
    assertTrue(!r3.isCurrent());

    r2.close();
    r3.close();
    
    dir1.close();
  }
  
  /**
   * Test using IW.addIndexes
   * 
   * @throws Exception
   */
  public void testAddIndexes() throws Exception {
    boolean optimize = false;

    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    // create the index
    createIndexNoClose(!optimize, "index1", writer);
    writer.flush(false, true, true);

    // create a 2nd index
    Directory dir2 = new MockRAMDirectory();
    IndexWriter writer2 = new IndexWriter(dir2, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer2.setInfoStream(infoStream);
    createIndexNoClose(!optimize, "index2", writer2);
    writer2.close();

    IndexReader r0 = writer.getReader();
    assertTrue(r0.isCurrent());
    writer.addIndexes(new Directory[] { dir2 });
    assertFalse(r0.isCurrent());
    r0.close();

    IndexReader r1 = writer.getReader();
    assertTrue(r1.isCurrent());

    writer.commit();
    assertTrue(r1.isCurrent());

    assertEquals(200, r1.maxDoc());

    int index2df = r1.docFreq(new Term("indexname", "index2"));

    assertEquals(100, index2df);

    // verify the docs are from different indexes
    Document doc5 = r1.document(5);
    assertEquals("index1", doc5.get("indexname"));
    Document doc150 = r1.document(150);
    assertEquals("index2", doc150.get("indexname"));
    r1.close();
    writer.close();
    dir1.close();
  }
  
  public void testAddIndexes2() throws Exception {
    boolean optimize = false;

    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);

    // create a 2nd index
    Directory dir2 = new MockRAMDirectory();
    IndexWriter writer2 = new IndexWriter(dir2, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer2.setInfoStream(infoStream);
    createIndexNoClose(!optimize, "index2", writer2);
    writer2.close();

    writer.addIndexes(new Directory[] { dir2 });
    writer.addIndexes(new Directory[] { dir2 });
    writer.addIndexes(new Directory[] { dir2 });
    writer.addIndexes(new Directory[] { dir2 });
    writer.addIndexes(new Directory[] { dir2 });

    IndexReader r1 = writer.getReader();
    assertEquals(500, r1.maxDoc());
    
    r1.close();
    writer.close();
    dir1.close();
  }

  /**
   * Deletes using IW.deleteDocuments
   * 
   * @throws Exception
   */
  public void testDeleteFromIndexWriter() throws Exception {
    boolean optimize = true;

    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    // create the index
    createIndexNoClose(!optimize, "index1", writer);
    writer.flush(false, true, true);
    // get a reader
    IndexReader r1 = writer.getReader();

    String id10 = r1.document(10).getField("id").stringValue();

    // deleted IW docs should not show up in the next getReader
    writer.deleteDocuments(new Term("id", id10));
    IndexReader r2 = writer.getReader();
    assertEquals(1, count(new Term("id", id10), r1));
    assertEquals(0, count(new Term("id", id10), r2));
    
    String id50 = r1.document(50).getField("id").stringValue();
    assertEquals(1, count(new Term("id", id50), r1));
    
    writer.deleteDocuments(new Term("id", id50));
    
    IndexReader r3 = writer.getReader();
    assertEquals(0, count(new Term("id", id10), r3));
    assertEquals(0, count(new Term("id", id50), r3));
    
    String id75 = r1.document(75).getField("id").stringValue();
    writer.deleteDocuments(new TermQuery(new Term("id", id75)));
    IndexReader r4 = writer.getReader();
    assertEquals(1, count(new Term("id", id75), r3));
    assertEquals(0, count(new Term("id", id75), r4));
    
    r1.close();
    r2.close();
    r3.close();
    r4.close();
    writer.close();
        
    // reopen the writer to verify the delete made it to the directory
    writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    IndexReader w2r1 = writer.getReader();
    assertEquals(0, count(new Term("id", id10), w2r1));
    w2r1.close();
    writer.close();
    dir1.close();
  }

  public void testAddIndexesAndDoDeletesThreads() throws Throwable {
    final int numIter = 2;
    int numDirs = 3;
    
    Directory mainDir = new MockRAMDirectory();
    IndexWriter mainWriter = new IndexWriter(mainDir, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    mainWriter.setInfoStream(infoStream);
    AddDirectoriesThreads addDirThreads = new AddDirectoriesThreads(numIter, mainWriter);
    addDirThreads.launchThreads(numDirs);
    addDirThreads.joinThreads();
    
    //assertEquals(100 + numDirs * (3 * numIter / 4) * addDirThreads.NUM_THREADS
    //    * addDirThreads.NUM_INIT_DOCS, addDirThreads.mainWriter.numDocs());
    assertEquals(addDirThreads.count.intValue(), addDirThreads.mainWriter.numDocs());

    addDirThreads.close(true);
    
    assertTrue(addDirThreads.failures.size() == 0);

    _TestUtil.checkIndex(mainDir);

    IndexReader reader = IndexReader.open(mainDir, true);
    assertEquals(addDirThreads.count.intValue(), reader.numDocs());
    //assertEquals(100 + numDirs * (3 * numIter / 4) * addDirThreads.NUM_THREADS
    //    * addDirThreads.NUM_INIT_DOCS, reader.numDocs());
    reader.close();

    addDirThreads.closeDir();
    mainDir.close();
  }
  
  private class AddDirectoriesThreads {
    Directory addDir;
    final static int NUM_THREADS = 5;
    final static int NUM_INIT_DOCS = 100;
    int numDirs;
    final Thread[] threads = new Thread[NUM_THREADS];
    IndexWriter mainWriter;
    final List<Throwable> failures = new ArrayList<Throwable>();
    IndexReader[] readers;
    boolean didClose = false;
    HeavyAtomicInt count = new HeavyAtomicInt(0);
    HeavyAtomicInt numaddIndexes = new HeavyAtomicInt(0);
    
    public AddDirectoriesThreads(int numDirs, IndexWriter mainWriter) throws Throwable {
      this.numDirs = numDirs;
      this.mainWriter = mainWriter;
      addDir = new MockRAMDirectory();
      IndexWriter writer = new IndexWriter(addDir, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)).setMaxBufferedDocs(2));
      for (int i = 0; i < NUM_INIT_DOCS; i++) {
        Document doc = createDocument(i, "addindex", 4);
        writer.addDocument(doc);
      }
        
      writer.close();
      
      readers = new IndexReader[numDirs];
      for (int i = 0; i < numDirs; i++)
        readers[i] = IndexReader.open(addDir, false);
    }
    
    void joinThreads() {
      for (int i = 0; i < NUM_THREADS; i++)
        try {
          threads[i].join();
        } catch (InterruptedException ie) {
          throw new ThreadInterruptedException(ie);
        }
    }

    void close(boolean doWait) throws Throwable {
      didClose = true;
      mainWriter.close(doWait);
    }

    void closeDir() throws Throwable {
      for (int i = 0; i < numDirs; i++)
        readers[i].close();
      addDir.close();
    }
    
    void handle(Throwable t) {
      t.printStackTrace(System.out);
      synchronized (failures) {
        failures.add(t);
      }
    }
    
    void launchThreads(final int numIter) {
      for (int i = 0; i < NUM_THREADS; i++) {
        threads[i] = new Thread() {
          @Override
          public void run() {
            try {
              final Directory[] dirs = new Directory[numDirs];
              for (int k = 0; k < numDirs; k++)
                dirs[k] = new MockRAMDirectory(addDir);
              //int j = 0;
              //while (true) {
                // System.out.println(Thread.currentThread().getName() + ": iter
                // j=" + j);
                for (int x=0; x < numIter; x++) {
                  // only do addIndexes
                  doBody(x, dirs);
                }
                //if (numIter > 0 && j == numIter)
                //  break;
                //doBody(j++, dirs);
                //doBody(5, dirs);
              //}
            } catch (Throwable t) {
              handle(t);
            }
          }
        };
      }
      for (int i = 0; i < NUM_THREADS; i++)
        threads[i].start();
    }
    
    void doBody(int j, Directory[] dirs) throws Throwable {
      switch (j % 4) {
        case 0:
          mainWriter.addIndexes(dirs);
          mainWriter.optimize();
          break;
        case 1:
          mainWriter.addIndexes(dirs);
          numaddIndexes.incrementAndGet();
          break;
        case 2:
          mainWriter.addIndexes(readers);
          break;
        case 3:
          mainWriter.commit();
      }
      count.addAndGet(dirs.length*NUM_INIT_DOCS);
    }
  }

  public void testIndexWriterReopenSegmentOptimize() throws Exception {
    doTestIndexWriterReopenSegment(true);
  }

  public void testIndexWriterReopenSegment() throws Exception {
    doTestIndexWriterReopenSegment(false);
  }

  /**
   * Tests creating a segment, then check to insure the segment can be seen via
   * IW.getReader
   */
  public void doTestIndexWriterReopenSegment(boolean optimize) throws Exception {
    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    IndexReader r1 = writer.getReader();
    assertEquals(0, r1.maxDoc());
    createIndexNoClose(false, "index1", writer);
    writer.flush(!optimize, true, true);

    IndexReader iwr1 = writer.getReader();
    assertEquals(100, iwr1.maxDoc());

    IndexReader r2 = writer.getReader();
    assertEquals(r2.maxDoc(), 100);
    // add 100 documents
    for (int x = 10000; x < 10000 + 100; x++) {
      Document d = createDocument(x, "index1", 5);
      writer.addDocument(d);
    }
    writer.flush(false, true, true);
    // verify the reader was reopened internally
    IndexReader iwr2 = writer.getReader();
    assertTrue(iwr2 != r1);
    assertEquals(200, iwr2.maxDoc());
    // should have flushed out a segment
    IndexReader r3 = writer.getReader();
    assertTrue(r2 != r3);
    assertEquals(200, r3.maxDoc());

    // dec ref the readers rather than close them because
    // closing flushes changes to the writer
    r1.close();
    iwr1.close();
    r2.close();
    r3.close();
    iwr2.close();
    writer.close();

    // test whether the changes made it to the directory
    writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    IndexReader w2r1 = writer.getReader();
    // insure the deletes were actually flushed to the directory
    assertEquals(200, w2r1.maxDoc());
    w2r1.close();
    writer.close();

    dir1.close();
  }

  
  public static Document createDocument(int n, String indexName, int numFields) {
    StringBuilder sb = new StringBuilder();
    Document doc = new Document();
    doc.add(new Field("id", Integer.toString(n), Store.YES, Index.NOT_ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));
    doc.add(new Field("indexname", indexName, Store.YES, Index.NOT_ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));
    sb.append("a");
    sb.append(n);
    doc.add(new Field("field1", sb.toString(), Store.YES, Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));
    sb.append(" b");
    sb.append(n);
    for (int i = 1; i < numFields; i++) {
      doc.add(new Field("field" + (i + 1), sb.toString(), Store.YES,
                        Index.ANALYZED, TermVector.WITH_POSITIONS_OFFSETS));
    }
    return doc;
  }

  /*
   * Delete a document by term and return the doc id
   * 
   * public static int deleteDocument(Term term, IndexWriter writer) throws
   * IOException { IndexReader reader = writer.getReader(); TermDocs td =
   * reader.termDocs(term); int doc = -1; //if (td.next()) { // doc = td.doc();
   * //} //writer.deleteDocuments(term); td.close(); return doc; }
   */
  
  public static void createIndex(Directory dir1, String indexName,
      boolean multiSegment) throws IOException {
    IndexWriter w = new IndexWriter(dir1, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT))
        .setMergePolicy(new LogDocMergePolicy()));
    for (int i = 0; i < 100; i++) {
      w.addDocument(createDocument(i, indexName, 4));
      if (multiSegment && (i % 10) == 0) {
      }
    }
    if (!multiSegment) {
      w.optimize();
    }
    w.close();
  }

  public static void createIndexNoClose(boolean multiSegment, String indexName,
      IndexWriter w) throws IOException {
    for (int i = 0; i < 100; i++) {
      w.addDocument(createDocument(i, indexName, 4));
    }
    if (!multiSegment) {
      w.optimize();
    }
  }

  private static class MyWarmer extends IndexWriter.IndexReaderWarmer {
    int warmCount;
    @Override
    public void warm(IndexReader reader) throws IOException {
      warmCount++;
    }
  }

  public void testMergeWarmer() throws Exception {

    Directory dir1 = new MockRAMDirectory();
    // Enroll warmer
    MyWarmer warmer = new MyWarmer();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(
        TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT))
        .setMaxBufferedDocs(2).setMergedSegmentWarmer(warmer));
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);

    // get a reader to put writer into near real-time mode
    IndexReader r1 = writer.getReader();
    
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(2);

    for (int i = 0; i < 10; i++) {
      writer.addDocument(createDocument(i, "test", 4));
    }
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).sync();

    assertTrue(warmer.warmCount > 0);
    final int count = warmer.warmCount;

    writer.addDocument(createDocument(17, "test", 4));
    writer.optimize();
    assertTrue(warmer.warmCount > count);
    
    writer.close();
    r1.close();
    dir1.close();
  }

  public void testAfterCommit() throws Exception {
    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.commit();
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);

    // get a reader to put writer into near real-time mode
    IndexReader r1 = writer.getReader();
    _TestUtil.checkIndex(dir1);
    writer.commit();
    _TestUtil.checkIndex(dir1);
    assertEquals(100, r1.numDocs());

    for (int i = 0; i < 10; i++) {
      writer.addDocument(createDocument(i, "test", 4));
    }
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).sync();

    IndexReader r2 = r1.reopen();
    if (r2 != r1) {
      r1.close();
      r1 = r2;
    }
    assertEquals(110, r1.numDocs());
    writer.close();
    r1.close();
    dir1.close();
  }

  // Make sure reader remains usable even if IndexWriter closes
  public void testAfterClose() throws Exception {
    Directory dir1 = new MockRAMDirectory();
    IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);

    IndexReader r = writer.getReader();
    writer.close();

    _TestUtil.checkIndex(dir1);

    // reader should remain usable even after IndexWriter is closed:
    assertEquals(100, r.numDocs());
    Query q = new TermQuery(new Term("indexname", "test"));
    assertEquals(100, new IndexSearcher(r).search(q, 10).totalHits);

    try {
      r.reopen();
      fail("failed to hit AlreadyClosedException");
    } catch (AlreadyClosedException ace) {
      // expected
    }
    r.close();
    dir1.close();
  }

  // Stress test reopen during addIndexes
  public void testDuringAddIndexes() throws Exception {
    Directory dir1 = new MockRAMDirectory();
    final IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(2);

    // create the index
    createIndexNoClose(false, "test", writer);
    writer.commit();

    final Directory[] dirs = new Directory[10];
    for (int i=0;i<10;i++) {
      dirs[i] = new MockRAMDirectory(dir1);
    }

    IndexReader r = writer.getReader();

    final int NUM_THREAD = 5;
    final float SECONDS = 0.5f;

    final long endTime = (long) (System.currentTimeMillis() + 1000.*SECONDS);
    final List<Throwable> excs = Collections.synchronizedList(new ArrayList<Throwable>());

    final Thread[] threads = new Thread[NUM_THREAD];
    for(int i=0;i<NUM_THREAD;i++) {
      threads[i] = new Thread() {
          @Override
          public void run() {
            do {
              try {
                writer.addIndexes(dirs);
              } catch (Throwable t) {
                excs.add(t);
                throw new RuntimeException(t);
              }
            } while(System.currentTimeMillis() < endTime);
          }
        };
      threads[i].setDaemon(true);
      threads[i].start();
    }

    int lastCount = 0;
    while(System.currentTimeMillis() < endTime) {
      IndexReader r2 = r.reopen();
      if (r2 != r) {
        r.close();
        r = r2;
      }
      Query q = new TermQuery(new Term("indexname", "test"));
      final int count = new IndexSearcher(r).search(q, 10).totalHits;
      assertTrue(count >= lastCount);
      lastCount = count;
    }

    for(int i=0;i<NUM_THREAD;i++) {
      threads[i].join();
    }
    // final check
    IndexReader r2 = r.reopen();
    if (r2 != r) {
      r.close();
      r = r2;
    }
    Query q = new TermQuery(new Term("indexname", "test"));
    final int count = new IndexSearcher(r).search(q, 10).totalHits;
    assertTrue(count >= lastCount);

    assertEquals(0, excs.size());
    writer.close();

    _TestUtil.checkIndex(dir1);
    r.close();
    dir1.close();
  }

  // Stress test reopen during add/delete
  public void testDuringAddDelete() throws Exception {
    Directory dir1 = new MockRAMDirectory();
    final IndexWriter writer = new IndexWriter(dir1, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    writer.setInfoStream(infoStream);
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(2);

    // create the index
    createIndexNoClose(false, "test", writer);
    writer.commit();

    IndexReader r = writer.getReader();

    final int NUM_THREAD = 5;
    final float SECONDS = 0.5f;

    final long endTime = (long) (System.currentTimeMillis() + 1000.*SECONDS);
    final List<Throwable> excs = Collections.synchronizedList(new ArrayList<Throwable>());

    final Thread[] threads = new Thread[NUM_THREAD];
    for(int i=0;i<NUM_THREAD;i++) {
      threads[i] = new Thread() {
          @Override
          public void run() {
            int count = 0;
            final Random r = new Random();
            do {
              try {
                for(int i=0;i<10;i++) {
                  writer.addDocument(createDocument(10*count+i, "test", 4));
                }
                count++;
                final int limit = count*10;
                for(int i=0;i<5;i++) {
                  int x = r.nextInt(limit);
                  writer.deleteDocuments(new Term("field3", "b"+x));
                }
              } catch (Throwable t) {
                excs.add(t);
                throw new RuntimeException(t);
              }
            } while(System.currentTimeMillis() < endTime);
          }
        };
      threads[i].setDaemon(true);
      threads[i].start();
    }

    int sum = 0;
    while(System.currentTimeMillis() < endTime) {
      IndexReader r2 = r.reopen();
      if (r2 != r) {
        r.close();
        r = r2;
      }
      Query q = new TermQuery(new Term("indexname", "test"));
      sum += new IndexSearcher(r).search(q, 10).totalHits;
    }

    for(int i=0;i<NUM_THREAD;i++) {
      threads[i].join();
    }
    // at least search once
    IndexReader r2 = r.reopen();
    if (r2 != r) {
      r.close();
      r = r2;
    }
    Query q = new TermQuery(new Term("indexname", "test"));
    sum += new IndexSearcher(r).search(q, 10).totalHits;

    assertTrue("no documents found at all", sum > 0);

    assertEquals(0, excs.size());
    writer.close();

    _TestUtil.checkIndex(dir1);
    r.close();
    dir1.close();
  }

  public void testExpungeDeletes() throws Throwable {
    Directory dir = new MockRAMDirectory();
    final IndexWriter w = new IndexWriter(dir, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    Document doc = new Document();
    doc.add(new Field("field", "a b c", Field.Store.NO, Field.Index.ANALYZED));
    Field id = new Field("id", "", Field.Store.NO, Field.Index.NOT_ANALYZED);
    doc.add(id);
    id.setValue("0");
    w.addDocument(doc);
    id.setValue("1");
    w.addDocument(doc);
    w.deleteDocuments(new Term("id", "0"));

    IndexReader r = w.getReader();
    w.expungeDeletes();
    w.close();
    r.close();
    r = IndexReader.open(dir, true);
    assertEquals(1, r.numDocs());
    assertFalse(r.hasDeletions());
    r.close();
    dir.close();
  }

  public void testDeletesNumDocs() throws Throwable {
    Directory dir = new MockRAMDirectory();
    final IndexWriter w = new IndexWriter(dir, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    Document doc = new Document();
    doc.add(new Field("field", "a b c", Field.Store.NO, Field.Index.ANALYZED));
    Field id = new Field("id", "", Field.Store.NO, Field.Index.NOT_ANALYZED);
    doc.add(id);
    id.setValue("0");
    w.addDocument(doc);
    id.setValue("1");
    w.addDocument(doc);
    IndexReader r = w.getReader();
    assertEquals(2, r.numDocs());
    r.close();

    w.deleteDocuments(new Term("id", "0"));
    r = w.getReader();
    assertEquals(1, r.numDocs());
    r.close();

    w.deleteDocuments(new Term("id", "1"));
    r = w.getReader();
    assertEquals(0, r.numDocs());
    r.close();

    w.close();
    dir.close();
  }
  
  public void testEmptyIndex() throws Exception {
    // Ensures that getReader works on an empty index, which hasn't been committed yet.
    Directory dir = new MockRAMDirectory();
    IndexWriter w = new IndexWriter(dir, new IndexWriterConfig(TEST_VERSION_CURRENT, new WhitespaceAnalyzer(TEST_VERSION_CURRENT)));
    IndexReader r = w.getReader();
    assertEquals(0, r.numDocs());
    r.close();
    w.close();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2680.java