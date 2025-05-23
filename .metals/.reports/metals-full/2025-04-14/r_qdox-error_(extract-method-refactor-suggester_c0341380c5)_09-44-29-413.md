error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2109.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2109.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2109.java
text:
```scala
i@@f (r2 != null) {

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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.codecs.Codec;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.ThreadInterruptedException;
import java.util.concurrent.atomic.AtomicInteger;

public class TestIndexWriterReader extends LuceneTestCase {
  static PrintStream infoStream = VERBOSE ? System.out : null;
  
  private final int numThreads = TEST_NIGHTLY ? 5 : 3;
  
  public static int count(Term t, IndexReader r) throws IOException {
    int count = 0;
    DocsEnum td = MultiFields.getTermDocsEnum(r,
                                              MultiFields.getLiveDocs(r),
                                              t.field(), new BytesRef(t.text()));

    if (td != null) {
      while (td.nextDoc() != DocsEnum.NO_MORE_DOCS) {
        td.docID();
        count++;
      }
    }
    return count;
  }
  
  public void testAddCloseOpen() throws IOException {
    Directory dir1 = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    
    IndexWriter writer = new IndexWriter(dir1, iwc);
    for (int i = 0; i < 97 ; i++) {
      IndexReader reader = writer.getReader();
      if (i == 0) {
        writer.addDocument(DocHelper.createDocument(i, "x", 1 + random.nextInt(5)));
      } else {
        int previous = random.nextInt(i);
        // a check if the reader is current here could fail since there might be
        // merges going on.
        switch (random.nextInt(5)) {
        case 0:
        case 1:
        case 2:
          writer.addDocument(DocHelper.createDocument(i, "x", 1 + random.nextInt(5)));
          break;
        case 3:
          writer.updateDocument(new Term("id", "" + previous), DocHelper.createDocument(
              previous, "x", 1 + random.nextInt(5)));
          break;
        case 4:
          writer.deleteDocuments(new Term("id", "" + previous));
        }
      }
      assertFalse(reader.isCurrent());
      reader.close();
    }
    writer.optimize(); // make sure all merging is done etc.
    IndexReader reader = writer.getReader();
    writer.commit(); // no changes that are not visible to the reader
    assertTrue(reader.isCurrent());
    writer.close();
    assertTrue(reader.isCurrent()); // all changes are visible to the reader
    iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    writer = new IndexWriter(dir1, iwc);
    assertTrue(reader.isCurrent());
    writer.addDocument(DocHelper.createDocument(1, "x", 1+random.nextInt(5)));
    assertTrue(reader.isCurrent()); // segments in ram but IW is different to the readers one
    writer.close();
    assertFalse(reader.isCurrent()); // segments written
    reader.close();
    dir1.close();
  }
  
  public void testUpdateDocument() throws Exception {
    boolean optimize = true;

    Directory dir1 = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    if (iwc.getMaxBufferedDocs() < 20) {
      iwc.setMaxBufferedDocs(20);
    }
    // no merging
    if (random.nextBoolean()) {
      iwc.setMergePolicy(NoMergePolicy.NO_COMPOUND_FILES);
    } else {
      iwc.setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    }
    if (VERBOSE) {
      System.out.println("TEST: make index");
    }
    IndexWriter writer = new IndexWriter(dir1, iwc);
    writer.setInfoStream(VERBOSE ? System.out : null);

    // create the index
    createIndexNoClose(!optimize, "index1", writer);

    // writer.flush(false, true, true);

    // get a reader
    IndexReader r1 = writer.getReader();
    assertTrue(r1.isCurrent());

    String id10 = r1.document(10).getField("id").stringValue();
    
    Document newDoc = r1.document(10);
    newDoc.removeField("id");
    newDoc.add(newField("id", Integer.toString(8000), StringField.TYPE_STORED));
    writer.updateDocument(new Term("id", id10), newDoc);
    assertFalse(r1.isCurrent());

    IndexReader r2 = writer.getReader();
    assertTrue(r2.isCurrent());
    assertEquals(0, count(new Term("id", id10), r2));
    if (VERBOSE) {
      System.out.println("TEST: verify id");
    }
    assertEquals(1, count(new Term("id", Integer.toString(8000)), r2));
    
    r1.close();
    writer.close();
    assertTrue(r2.isCurrent());
    
    IndexReader r3 = IndexReader.open(dir1, true);
    assertTrue(r3.isCurrent());
    assertTrue(r2.isCurrent());
    assertEquals(0, count(new Term("id", id10), r3));
    assertEquals(1, count(new Term("id", Integer.toString(8000)), r3));

    writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    Document doc = new Document();
    doc.add(newField("field", "a b c", TextField.TYPE_UNSTORED));
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
  
  public void testIsCurrent() throws IOException {
    Directory dir = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    
    IndexWriter writer = new IndexWriter(dir, iwc);
    Document doc = new Document();
    doc.add(newField("field", "a b c", TextField.TYPE_UNSTORED));
    writer.addDocument(doc);
    writer.close();
    
    iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    writer = new IndexWriter(dir, iwc);
    doc = new Document();
    doc.add(newField("field", "a b c", TextField.TYPE_UNSTORED));
    IndexReader nrtReader = writer.getReader();
    assertTrue(nrtReader.isCurrent());
    writer.addDocument(doc);
    assertFalse(nrtReader.isCurrent()); // should see the changes
    writer.optimize(); // make sure we don't have a merge going on
    assertFalse(nrtReader.isCurrent());
    nrtReader.close();
    
    IndexReader dirReader = IndexReader.open(dir);
    nrtReader = writer.getReader();
    
    assertTrue(dirReader.isCurrent());
    assertTrue(nrtReader.isCurrent()); // nothing was committed yet so we are still current
    assertEquals(2, nrtReader.maxDoc()); // sees the actual document added
    assertEquals(1, dirReader.maxDoc());
    writer.close(); // close is actually a commit both should see the changes
    assertTrue(nrtReader.isCurrent()); 
    assertFalse(dirReader.isCurrent()); // this reader has been opened before the writer was closed / committed
    
    dirReader.close();
    nrtReader.close();
    dir.close();
  }
  
  /**
   * Test using IW.addIndexes
   * 
   * @throws Exception
   */
  public void testAddIndexes() throws Exception {
    boolean optimize = false;

    Directory dir1 = newDirectory();
    IndexWriterConfig iwc = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random));
    if (iwc.getMaxBufferedDocs() < 20) {
      iwc.setMaxBufferedDocs(20);
    }
    // no merging
    if (random.nextBoolean()) {
      iwc.setMergePolicy(NoMergePolicy.NO_COMPOUND_FILES);
    } else {
      iwc.setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    }
    IndexWriter writer = new IndexWriter(dir1, iwc);

    writer.setInfoStream(infoStream);
    // create the index
    createIndexNoClose(!optimize, "index1", writer);
    writer.flush(false, true);

    // create a 2nd index
    Directory dir2 = newDirectory();
    IndexWriter writer2 = new IndexWriter(dir2, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    writer2.setInfoStream(infoStream);
    createIndexNoClose(!optimize, "index2", writer2);
    writer2.close();

    IndexReader r0 = writer.getReader();
    assertTrue(r0.isCurrent());
    writer.addIndexes(dir2);
    assertFalse(r0.isCurrent());
    r0.close();

    IndexReader r1 = writer.getReader();
    assertTrue(r1.isCurrent());

    writer.commit();
    assertTrue(r1.isCurrent()); // we have seen all changes - no change after opening the NRT reader

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
    dir2.close();
  }
  
  public void testAddIndexes2() throws Exception {
    boolean optimize = false;

    Directory dir1 = newDirectory();
    IndexWriter writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    writer.setInfoStream(infoStream);

    // create a 2nd index
    Directory dir2 = newDirectory();
    IndexWriter writer2 = new IndexWriter(dir2, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    writer2.setInfoStream(infoStream);
    createIndexNoClose(!optimize, "index2", writer2);
    writer2.close();

    writer.addIndexes(dir2);
    writer.addIndexes(dir2);
    writer.addIndexes(dir2);
    writer.addIndexes(dir2);
    writer.addIndexes(dir2);

    IndexReader r1 = writer.getReader();
    assertEquals(500, r1.maxDoc());
    
    r1.close();
    writer.close();
    dir1.close();
    dir2.close();
  }

  /**
   * Deletes using IW.deleteDocuments
   * 
   * @throws Exception
   */
  public void testDeleteFromIndexWriter() throws Exception {
    boolean optimize = true;

    Directory dir1 = newDirectory();
    IndexWriter writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).setReaderTermsIndexDivisor(2));
    writer.setInfoStream(infoStream);
    // create the index
    createIndexNoClose(!optimize, "index1", writer);
    writer.flush(false, true);
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
    writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
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
    
    Directory mainDir = newDirectory();
    IndexWriter mainWriter = new IndexWriter(mainDir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMergePolicy(newLogMergePolicy()));
    _TestUtil.reduceOpenFiles(mainWriter);

    mainWriter.setInfoStream(infoStream);
    AddDirectoriesThreads addDirThreads = new AddDirectoriesThreads(numIter, mainWriter);
    addDirThreads.launchThreads(numDirs);
    addDirThreads.joinThreads();
    
    //assertEquals(100 + numDirs * (3 * numIter / 4) * addDirThreads.numThreads
    //    * addDirThreads.NUM_INIT_DOCS, addDirThreads.mainWriter.numDocs());
    assertEquals(addDirThreads.count.intValue(), addDirThreads.mainWriter.numDocs());

    addDirThreads.close(true);
    
    assertTrue(addDirThreads.failures.size() == 0);

    _TestUtil.checkIndex(mainDir);

    IndexReader reader = IndexReader.open(mainDir, true);
    assertEquals(addDirThreads.count.intValue(), reader.numDocs());
    //assertEquals(100 + numDirs * (3 * numIter / 4) * addDirThreads.numThreads
    //    * addDirThreads.NUM_INIT_DOCS, reader.numDocs());
    reader.close();

    addDirThreads.closeDir();
    mainDir.close();
  }
  
  private class AddDirectoriesThreads {
    Directory addDir;
    final static int NUM_INIT_DOCS = 100;
    int numDirs;
    final Thread[] threads = new Thread[numThreads];
    IndexWriter mainWriter;
    final List<Throwable> failures = new ArrayList<Throwable>();
    IndexReader[] readers;
    boolean didClose = false;
    AtomicInteger count = new AtomicInteger(0);
    AtomicInteger numaddIndexes = new AtomicInteger(0);
    
    public AddDirectoriesThreads(int numDirs, IndexWriter mainWriter) throws Throwable {
      this.numDirs = numDirs;
      this.mainWriter = mainWriter;
      addDir = newDirectory();
      IndexWriter writer = new IndexWriter(addDir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMaxBufferedDocs(2));
      for (int i = 0; i < NUM_INIT_DOCS; i++) {
        Document doc = DocHelper.createDocument(i, "addindex", 4);
        writer.addDocument(doc);
      }
        
      writer.close();
      
      readers = new IndexReader[numDirs];
      for (int i = 0; i < numDirs; i++)
        readers[i] = IndexReader.open(addDir, false);
    }
    
    void joinThreads() {
      for (int i = 0; i < numThreads; i++)
        try {
          threads[i].join();
        } catch (InterruptedException ie) {
          throw new ThreadInterruptedException(ie);
        }
    }

    void close(boolean doWait) throws Throwable {
      didClose = true;
      if (doWait) {
        mainWriter.waitForMerges();
      }
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
      for (int i = 0; i < numThreads; i++) {
        threads[i] = new Thread() {
          @Override
          public void run() {
            try {
              final Directory[] dirs = new Directory[numDirs];
              for (int k = 0; k < numDirs; k++)
                dirs[k] = new MockDirectoryWrapper(random, new RAMDirectory(addDir, newIOContext(random)));
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
      for (int i = 0; i < numThreads; i++)
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
    Directory dir1 = newDirectory();
    IndexWriter writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    writer.setInfoStream(infoStream);
    IndexReader r1 = writer.getReader();
    assertEquals(0, r1.maxDoc());
    createIndexNoClose(false, "index1", writer);
    writer.flush(!optimize, true);

    IndexReader iwr1 = writer.getReader();
    assertEquals(100, iwr1.maxDoc());

    IndexReader r2 = writer.getReader();
    assertEquals(r2.maxDoc(), 100);
    // add 100 documents
    for (int x = 10000; x < 10000 + 100; x++) {
      Document d = DocHelper.createDocument(x, "index1", 5);
      writer.addDocument(d);
    }
    writer.flush(false, true);
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
    writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    IndexReader w2r1 = writer.getReader();
    // insure the deletes were actually flushed to the directory
    assertEquals(200, w2r1.maxDoc());
    w2r1.close();
    writer.close();

    dir1.close();
  }
 
  /*
   * Delete a document by term and return the doc id
   * 
   * public static int deleteDocument(Term term, IndexWriter writer) throws
   * IOException { IndexReader reader = writer.getReader(); TermDocs td =
   * reader.termDocs(term); int doc = -1; //if (td.next()) { // doc = td.doc();
   * //} //writer.deleteDocuments(term); td.close(); return doc; }
   */
  
  public static void createIndex(Random random, Directory dir1, String indexName,
      boolean multiSegment) throws IOException {
    IndexWriter w = new IndexWriter(dir1, LuceneTestCase.newIndexWriterConfig(random,
        TEST_VERSION_CURRENT, new MockAnalyzer(random))
        .setMergePolicy(new LogDocMergePolicy()));
    for (int i = 0; i < 100; i++) {
      w.addDocument(DocHelper.createDocument(i, indexName, 4));
    }
    if (!multiSegment) {
      w.optimize();
    }
    w.close();
  }

  public static void createIndexNoClose(boolean multiSegment, String indexName,
      IndexWriter w) throws IOException {
    for (int i = 0; i < 100; i++) {
      w.addDocument(DocHelper.createDocument(i, indexName, 4));
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

    Directory dir1 = newDirectory();
    // Enroll warmer
    MyWarmer warmer = new MyWarmer();
    IndexWriter writer = new IndexWriter(
        dir1,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).
            setMaxBufferedDocs(2).
            setMergedSegmentWarmer(warmer).
            setMergeScheduler(new ConcurrentMergeScheduler()).
            setMergePolicy(newLogMergePolicy())
    );
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);

    // get a reader to put writer into near real-time mode
    IndexReader r1 = writer.getReader();
    
    ((LogMergePolicy) writer.getConfig().getMergePolicy()).setMergeFactor(2);

    int num = atLeast(100);
    for (int i = 0; i < num; i++) {
      writer.addDocument(DocHelper.createDocument(i, "test", 4));
    }
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).sync();

    assertTrue(warmer.warmCount > 0);
    final int count = warmer.warmCount;

    writer.addDocument(DocHelper.createDocument(17, "test", 4));
    writer.optimize();
    assertTrue(warmer.warmCount > count);
    
    writer.close();
    r1.close();
    dir1.close();
  }

  public void testAfterCommit() throws Exception {
    Directory dir1 = newDirectory();
    IndexWriter writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMergeScheduler(new ConcurrentMergeScheduler()));
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
      writer.addDocument(DocHelper.createDocument(i, "test", 4));
    }
    ((ConcurrentMergeScheduler) writer.getConfig().getMergeScheduler()).sync();

    IndexReader r2 = IndexReader.openIfChanged(r1);
    if (r2 != null) {
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
    Directory dir1 = newDirectory();
    IndexWriter writer = new IndexWriter(dir1, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);

    IndexReader r = writer.getReader();
    writer.close();

    _TestUtil.checkIndex(dir1);

    // reader should remain usable even after IndexWriter is closed:
    assertEquals(100, r.numDocs());
    Query q = new TermQuery(new Term("indexname", "test"));
    IndexSearcher searcher = newSearcher(r);
    assertEquals(100, searcher.search(q, 10).totalHits);
    searcher.close();
    try {
      IndexReader.openIfChanged(r);
      fail("failed to hit AlreadyClosedException");
    } catch (AlreadyClosedException ace) {
      // expected
    }
    r.close();
    dir1.close();
  }

  // Stress test reopen during addIndexes
  public void testDuringAddIndexes() throws Exception {
    MockDirectoryWrapper dir1 = newDirectory();
    final IndexWriter writer = new IndexWriter(
        dir1,
        newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).
            setMergePolicy(newLogMergePolicy(2))
    );
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);
    writer.commit();

    final Directory[] dirs = new Directory[10];
    for (int i=0;i<10;i++) {
      dirs[i] = new MockDirectoryWrapper(random, new RAMDirectory(dir1, newIOContext(random)));
    }

    IndexReader r = writer.getReader();

    final float SECONDS = 0.5f;

    final long endTime = (long) (System.currentTimeMillis() + 1000.*SECONDS);
    final List<Throwable> excs = Collections.synchronizedList(new ArrayList<Throwable>());

    final Thread[] threads = new Thread[numThreads];
    for(int i=0;i<numThreads;i++) {
      threads[i] = new Thread() {
          @Override
          public void run() {
            do {
              try {
                writer.addIndexes(dirs);
                writer.maybeMerge();
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
      IndexReader r2 = IndexReader.openIfChanged(r);
      if (r2 != null) {
        r.close();
        r = r2;
      }
      Query q = new TermQuery(new Term("indexname", "test"));
      IndexSearcher searcher = newSearcher(r);
      final int count = searcher.search(q, 10).totalHits;
      searcher.close();
      assertTrue(count >= lastCount);
      lastCount = count;
    }

    for(int i=0;i<numThreads;i++) {
      threads[i].join();
    }
    // final check
    IndexReader r2 = IndexReader.openIfChanged(r);
    if (r2 != r) {
      r.close();
      r = r2;
    }
    Query q = new TermQuery(new Term("indexname", "test"));
    IndexSearcher searcher = newSearcher(r);
    final int count = searcher.search(q, 10).totalHits;
    searcher.close();
    assertTrue(count >= lastCount);

    assertEquals(0, excs.size());
    r.close();
    assertEquals(0, dir1.getOpenDeletedFiles().size());

    writer.close();

    dir1.close();
  }

  // Stress test reopen during add/delete
  public void testDuringAddDelete() throws Exception {
    Directory dir1 = newDirectory();
    final IndexWriter writer = new IndexWriter(
        dir1,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).
            setMergePolicy(newLogMergePolicy(2))
    );
    writer.setInfoStream(infoStream);

    // create the index
    createIndexNoClose(false, "test", writer);
    writer.commit();

    IndexReader r = writer.getReader();

    final float SECONDS = 0.5f;

    final long endTime = (long) (System.currentTimeMillis() + 1000.*SECONDS);
    final List<Throwable> excs = Collections.synchronizedList(new ArrayList<Throwable>());

    final Thread[] threads = new Thread[numThreads];
    for(int i=0;i<numThreads;i++) {
      threads[i] = new Thread() {
          final Random r = new Random(random.nextLong());

          @Override
          public void run() {
            int count = 0;
            do {
              try {
                for(int docUpto=0;docUpto<10;docUpto++) {
                  writer.addDocument(DocHelper.createDocument(10*count+docUpto, "test", 4));
                }
                count++;
                final int limit = count*10;
                for(int delUpto=0;delUpto<5;delUpto++) {
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
      IndexReader r2 = IndexReader.openIfChanged(r);
      if (r2 != null) {
        r.close();
        r = r2;
      }
      Query q = new TermQuery(new Term("indexname", "test"));
      IndexSearcher searcher = newSearcher(r);
      sum += searcher.search(q, 10).totalHits;
      searcher.close();
    }

    for(int i=0;i<numThreads;i++) {
      threads[i].join();
    }
    // at least search once
    IndexReader r2 = IndexReader.openIfChanged(r);
    if (r2 != null) {
      r.close();
      r = r2;
    }
    Query q = new TermQuery(new Term("indexname", "test"));
    IndexSearcher searcher = newSearcher(r);
    sum += searcher.search(q, 10).totalHits;
    searcher.close();
    assertTrue("no documents found at all", sum > 0);

    assertEquals(0, excs.size());
    writer.close();

    r.close();
    dir1.close();
  }

  public void testExpungeDeletes() throws Throwable {
    Directory dir = newDirectory();
    final IndexWriter w = new IndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).setMergePolicy(newLogMergePolicy()));
    Document doc = new Document();
    doc.add(newField("field", "a b c", TextField.TYPE_UNSTORED));
    Field id = newField("id", "", StringField.TYPE_UNSTORED);
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
    Directory dir = newDirectory();
    final IndexWriter w = new IndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    Document doc = new Document();
    doc.add(newField("field", "a b c", TextField.TYPE_UNSTORED));
    Field id = newField("id", "", StringField.TYPE_UNSTORED);
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
    Directory dir = newDirectory();
    IndexWriter w = new IndexWriter(dir, newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    IndexReader r = w.getReader();
    assertEquals(0, r.numDocs());
    r.close();
    w.close();
    dir.close();
  }

  public void testSegmentWarmer() throws Exception {
    Directory dir = newDirectory();
    final AtomicBoolean didWarm = new AtomicBoolean();
    IndexWriter w = new IndexWriter(
        dir,
        newIndexWriterConfig( TEST_VERSION_CURRENT, new MockAnalyzer(random)).
            setMaxBufferedDocs(2).
            setReaderPooling(true).
            setMergedSegmentWarmer(new IndexWriter.IndexReaderWarmer() {
              @Override
              public void warm(IndexReader r) throws IOException {
                IndexSearcher s = newSearcher(r);
                TopDocs hits = s.search(new TermQuery(new Term("foo", "bar")), 10);
                assertEquals(20, hits.totalHits);
                didWarm.set(true);
                s.close();
              }
            }).
            setMergePolicy(newLogMergePolicy(10))
    );

    Document doc = new Document();
    doc.add(newField("foo", "bar", StringField.TYPE_UNSTORED));
    for(int i=0;i<20;i++) {
      w.addDocument(doc);
    }
    w.waitForMerges();
    w.close();
    dir.close();
    assertTrue(didWarm.get());
  }
  
  public void testNoTermsIndex() throws Exception {
    // Some Codecs don't honor the ReaderTermsIndexDivisor, so skip the test if
    // they're picked.
    assumeFalse("PreFlex codec does not support ReaderTermsIndexDivisor!", 
        "Lucene3x".equals(Codec.getDefault().getName()));

    IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random)).setReaderTermsIndexDivisor(-1);
    
    // Don't proceed if picked Codec is in the list of illegal ones.
    final String format = _TestUtil.getPostingsFormat("f");
    assumeFalse("Format: " + format + " does not support ReaderTermsIndexDivisor!",
        (format.equals("SimpleText") || format.equals("Memory")));

    Directory dir = newDirectory();
    IndexWriter w = new IndexWriter(dir, conf);
    Document doc = new Document();
    doc.add(new TextField("f", "val"));
    w.addDocument(doc);
    IndexReader r = IndexReader.open(w, true).getSequentialSubReaders()[0];
    try {
      r.termDocsEnum(null, "f", new BytesRef("val"));
      fail("should have failed to seek since terms index was not loaded.");
    } catch (IllegalStateException e) {
      // expected - we didn't load the term index
    } finally {
      r.close();
      w.close();
      dir.close();
    }
  }
  
  public void testReopenAfterNoRealChange() throws Exception {
    Directory d = newDirectory();
    IndexWriter w = new IndexWriter(
        d,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)));
    w.setInfoStream(VERBOSE ? System.out : null);

    IndexReader r = w.getReader(); // start pooling readers

    IndexReader r2 = IndexReader.openIfChanged(r);
    assertNull(r2);
    
    w.addDocument(new Document());
    IndexReader r3 = IndexReader.openIfChanged(r);
    assertNotNull(r3);
    assertTrue(r3.getVersion() != r.getVersion());
    assertTrue(r3.isCurrent());

    // Deletes nothing in reality...:
    w.deleteDocuments(new Term("foo", "bar"));

    // ... but IW marks this as not current:
    assertFalse(r3.isCurrent());
    IndexReader r4 = IndexReader.openIfChanged(r3);
    assertNull(r4);

    // Deletes nothing in reality...:
    w.deleteDocuments(new Term("foo", "bar"));
    IndexReader r5 = IndexReader.openIfChanged(r3, w, true);
    assertNull(r5);

    r3.close();

    w.close();
    d.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2109.java