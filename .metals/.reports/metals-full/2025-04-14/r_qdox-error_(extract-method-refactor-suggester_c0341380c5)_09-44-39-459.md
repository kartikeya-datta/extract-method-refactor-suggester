error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1672.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1672.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1672.java
text:
```scala
D@@ocsEnum docsEnum = _TestUtil.docs(random(), reader, term.field, term.bytes, null, null, 0);

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.DocValuesFormat;
import org.apache.lucene.codecs.FieldInfosFormat;
import org.apache.lucene.codecs.LiveDocsFormat;
import org.apache.lucene.codecs.NormsFormat;
import org.apache.lucene.codecs.PostingsFormat;
import org.apache.lucene.codecs.SegmentInfoFormat;
import org.apache.lucene.codecs.StoredFieldsFormat;
import org.apache.lucene.codecs.TermVectorsFormat;
import org.apache.lucene.codecs.lucene40.Lucene40Codec;
import org.apache.lucene.codecs.lucene40.Lucene40DocValuesFormat;
import org.apache.lucene.codecs.lucene40.Lucene40FieldInfosFormat;
import org.apache.lucene.codecs.lucene40.Lucene40LiveDocsFormat;
import org.apache.lucene.codecs.lucene40.Lucene40NormsFormat;
import org.apache.lucene.codecs.lucene40.Lucene40SegmentInfoFormat;
import org.apache.lucene.codecs.lucene40.Lucene40StoredFieldsFormat;
import org.apache.lucene.codecs.lucene40.Lucene40TermVectorsFormat;
import org.apache.lucene.codecs.pulsing.Pulsing40PostingsFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.BaseDirectoryWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

public class TestAddIndexes extends LuceneTestCase {
  
  public void testSimpleCase() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // two auxiliary directories
    Directory aux = newDirectory();
    Directory aux2 = newDirectory();

    IndexWriter writer = null;

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random()))
        .setOpenMode(OpenMode.CREATE));
    // add 100 documents
    addDocs(writer, 100);
    assertEquals(100, writer.maxDoc());
    writer.close();
    _TestUtil.checkIndex(dir);

    writer = newWriter(
        aux,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setMergePolicy(newLogMergePolicy(false))
    );
    // add 40 documents in separate files
    addDocs(writer, 40);
    assertEquals(40, writer.maxDoc());
    writer.close();

    writer = newWriter(aux2, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.CREATE));
    // add 50 documents in compound files
    addDocs2(writer, 50);
    assertEquals(50, writer.maxDoc());
    writer.close();

    // test doc count before segments are merged
    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    assertEquals(100, writer.maxDoc());
    writer.addIndexes(aux, aux2);
    assertEquals(190, writer.maxDoc());
    writer.close();
    _TestUtil.checkIndex(dir);

    // make sure the old index is correct
    verifyNumDocs(aux, 40);

    // make sure the new index is correct
    verifyNumDocs(dir, 190);

    // now add another set in.
    Directory aux3 = newDirectory();
    writer = newWriter(aux3, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    // add 40 documents
    addDocs(writer, 40);
    assertEquals(40, writer.maxDoc());
    writer.close();

    // test doc count before segments are merged
    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    assertEquals(190, writer.maxDoc());
    writer.addIndexes(aux3);
    assertEquals(230, writer.maxDoc());
    writer.close();

    // make sure the new index is correct
    verifyNumDocs(dir, 230);

    verifyTermDocs(dir, new Term("content", "aaa"), 180);

    verifyTermDocs(dir, new Term("content", "bbb"), 50);

    // now fully merge it.
    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    writer.forceMerge(1);
    writer.close();

    // make sure the new index is correct
    verifyNumDocs(dir, 230);

    verifyTermDocs(dir, new Term("content", "aaa"), 180);

    verifyTermDocs(dir, new Term("content", "bbb"), 50);

    // now add a single document
    Directory aux4 = newDirectory();
    writer = newWriter(aux4, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    addDocs2(writer, 1);
    writer.close();

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    assertEquals(230, writer.maxDoc());
    writer.addIndexes(aux4);
    assertEquals(231, writer.maxDoc());
    writer.close();

    verifyNumDocs(dir, 231);

    verifyTermDocs(dir, new Term("content", "bbb"), 51);
    dir.close();
    aux.close();
    aux2.close();
    aux3.close();
    aux4.close();
  }

  public void testWithPendingDeletes() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);
    IndexWriter writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    writer.addIndexes(aux);

    // Adds 10 docs, then replaces them with another 10
    // docs, so 10 pending deletes:
    for (int i = 0; i < 20; i++) {
      Document doc = new Document();
      doc.add(newStringField("id", "" + (i % 10), Field.Store.NO));
      doc.add(newTextField("content", "bbb " + i, Field.Store.NO));
      writer.updateDocument(new Term("id", "" + (i%10)), doc);
    }
    // Deletes one of the 10 added docs, leaving 9:
    PhraseQuery q = new PhraseQuery();
    q.add(new Term("content", "bbb"));
    q.add(new Term("content", "14"));
    writer.deleteDocuments(q);

    writer.forceMerge(1);
    writer.commit();

    verifyNumDocs(dir, 1039);
    verifyTermDocs(dir, new Term("content", "aaa"), 1030);
    verifyTermDocs(dir, new Term("content", "bbb"), 9);

    writer.close();
    dir.close();
    aux.close();
  }

  public void testWithPendingDeletes2() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);
    IndexWriter writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));

    // Adds 10 docs, then replaces them with another 10
    // docs, so 10 pending deletes:
    for (int i = 0; i < 20; i++) {
      Document doc = new Document();
      doc.add(newStringField("id", "" + (i % 10), Field.Store.NO));
      doc.add(newTextField("content", "bbb " + i, Field.Store.NO));
      writer.updateDocument(new Term("id", "" + (i%10)), doc);
    }
    
    writer.addIndexes(aux);
    
    // Deletes one of the 10 added docs, leaving 9:
    PhraseQuery q = new PhraseQuery();
    q.add(new Term("content", "bbb"));
    q.add(new Term("content", "14"));
    writer.deleteDocuments(q);

    writer.forceMerge(1);
    writer.commit();

    verifyNumDocs(dir, 1039);
    verifyTermDocs(dir, new Term("content", "aaa"), 1030);
    verifyTermDocs(dir, new Term("content", "bbb"), 9);

    writer.close();
    dir.close();
    aux.close();
  }

  public void testWithPendingDeletes3() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);
    IndexWriter writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));

    // Adds 10 docs, then replaces them with another 10
    // docs, so 10 pending deletes:
    for (int i = 0; i < 20; i++) {
      Document doc = new Document();
      doc.add(newStringField("id", "" + (i % 10), Field.Store.NO));
      doc.add(newTextField("content", "bbb " + i, Field.Store.NO));
      writer.updateDocument(new Term("id", "" + (i%10)), doc);
    }

    // Deletes one of the 10 added docs, leaving 9:
    PhraseQuery q = new PhraseQuery();
    q.add(new Term("content", "bbb"));
    q.add(new Term("content", "14"));
    writer.deleteDocuments(q);

    writer.addIndexes(aux);

    writer.forceMerge(1);
    writer.commit();

    verifyNumDocs(dir, 1039);
    verifyTermDocs(dir, new Term("content", "aaa"), 1030);
    verifyTermDocs(dir, new Term("content", "bbb"), 9);

    writer.close();
    dir.close();
    aux.close();
  }

  // case 0: add self or exceed maxMergeDocs, expect exception
  public void testAddSelf() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    IndexWriter writer = null;

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    // add 100 documents
    addDocs(writer, 100);
    assertEquals(100, writer.maxDoc());
    writer.close();

    writer = newWriter(
        aux,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setMaxBufferedDocs(1000).
            setMergePolicy(newLogMergePolicy(false))
    );
    // add 140 documents in separate files
    addDocs(writer, 40);
    writer.close();
    writer = newWriter(
        aux,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setMaxBufferedDocs(1000).
            setMergePolicy(newLogMergePolicy(false))
    );
    addDocs(writer, 100);
    writer.close();

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND));
    try {
      // cannot add self
      writer.addIndexes(aux, dir);
      assertTrue(false);
    }
    catch (IllegalArgumentException e) {
      assertEquals(100, writer.maxDoc());
    }
    writer.close();

    // make sure the index is correct
    verifyNumDocs(dir, 100);
    dir.close();
    aux.close();
  }

  // in all the remaining tests, make the doc count of the oldest segment
  // in dir large so that it is never merged in addIndexes()
  // case 1: no tail segments
  public void testNoTailSegments() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);

    IndexWriter writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(10).
            setMergePolicy(newLogMergePolicy(4))
    );
    addDocs(writer, 10);

    writer.addIndexes(aux);
    assertEquals(1040, writer.maxDoc());
    assertEquals(1000, writer.getDocCount(0));
    writer.close();

    // make sure the index is correct
    verifyNumDocs(dir, 1040);
    dir.close();
    aux.close();
  }

  // case 2: tail segments, invariants hold, no copy
  public void testNoCopySegments() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);

    IndexWriter writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(9).
            setMergePolicy(newLogMergePolicy(4))
    );
    addDocs(writer, 2);

    writer.addIndexes(aux);
    assertEquals(1032, writer.maxDoc());
    assertEquals(1000, writer.getDocCount(0));
    writer.close();

    // make sure the index is correct
    verifyNumDocs(dir, 1032);
    dir.close();
    aux.close();
  }

  // case 3: tail segments, invariants hold, copy, invariants hold
  public void testNoMergeAfterCopy() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux);

    IndexWriter writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(10).
            setMergePolicy(newLogMergePolicy(4))
    );

    writer.addIndexes(aux, new MockDirectoryWrapper(random(), new RAMDirectory(aux, newIOContext(random()))));
    assertEquals(1060, writer.maxDoc());
    assertEquals(1000, writer.getDocCount(0));
    writer.close();

    // make sure the index is correct
    verifyNumDocs(dir, 1060);
    dir.close();
    aux.close();
  }

  // case 4: tail segments, invariants hold, copy, invariants not hold
  public void testMergeAfterCopy() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();

    setUpDirs(dir, aux, true);

    IndexWriterConfig dontMergeConfig = new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
      .setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    IndexWriter writer = new IndexWriter(aux, dontMergeConfig);
    for (int i = 0; i < 20; i++) {
      writer.deleteDocuments(new Term("id", "" + i));
    }
    writer.close();
    IndexReader reader = DirectoryReader.open(aux);
    assertEquals(10, reader.numDocs());
    reader.close();

    writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(4).
            setMergePolicy(newLogMergePolicy(4))
    );

    if (VERBOSE) {
      System.out.println("\nTEST: now addIndexes");
    }
    writer.addIndexes(aux, new MockDirectoryWrapper(random(), new RAMDirectory(aux, newIOContext(random()))));
    assertEquals(1020, writer.maxDoc());
    assertEquals(1000, writer.getDocCount(0));
    writer.close();
    dir.close();
    aux.close();
  }

  // case 5: tail segments, invariants not hold
  public void testMoreMerges() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // auxiliary directory
    Directory aux = newDirectory();
    Directory aux2 = newDirectory();

    setUpDirs(dir, aux, true);

    IndexWriter writer = newWriter(
        aux2,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setMaxBufferedDocs(100).
            setMergePolicy(newLogMergePolicy(10))
    );
    writer.addIndexes(aux);
    assertEquals(30, writer.maxDoc());
    assertEquals(3, writer.getSegmentCount());
    writer.close();

    IndexWriterConfig dontMergeConfig = new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
      .setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    writer = new IndexWriter(aux, dontMergeConfig);
    for (int i = 0; i < 27; i++) {
      writer.deleteDocuments(new Term("id", "" + i));
    }
    writer.close();
    IndexReader reader = DirectoryReader.open(aux);
    assertEquals(3, reader.numDocs());
    reader.close();

    dontMergeConfig = new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
    .setMergePolicy(NoMergePolicy.COMPOUND_FILES);
    writer = new IndexWriter(aux2, dontMergeConfig);
    for (int i = 0; i < 8; i++) {
      writer.deleteDocuments(new Term("id", "" + i));
    }
    writer.close();
    reader = DirectoryReader.open(aux2);
    assertEquals(22, reader.numDocs());
    reader.close();

    writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setMaxBufferedDocs(6).
            setMergePolicy(newLogMergePolicy(4))
    );

    writer.addIndexes(aux, aux2);
    assertEquals(1040, writer.maxDoc());
    assertEquals(1000, writer.getDocCount(0));
    writer.close();
    dir.close();
    aux.close();
    aux2.close();
  }

  private IndexWriter newWriter(Directory dir, IndexWriterConfig conf)
      throws IOException {
    conf.setMergePolicy(new LogDocMergePolicy());
    final IndexWriter writer = new IndexWriter(dir, conf);
    return writer;
  }

  private void addDocs(IndexWriter writer, int numDocs) throws IOException {
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      doc.add(newTextField("content", "aaa", Field.Store.NO));
      writer.addDocument(doc);
    }
  }

  private void addDocs2(IndexWriter writer, int numDocs) throws IOException {
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      doc.add(newTextField("content", "bbb", Field.Store.NO));
      writer.addDocument(doc);
    }
  }

  private void verifyNumDocs(Directory dir, int numDocs) throws IOException {
    IndexReader reader = DirectoryReader.open(dir);
    assertEquals(numDocs, reader.maxDoc());
    assertEquals(numDocs, reader.numDocs());
    reader.close();
  }

  private void verifyTermDocs(Directory dir, Term term, int numDocs)
      throws IOException {
    IndexReader reader = DirectoryReader.open(dir);
    DocsEnum docsEnum = _TestUtil.docs(random(), reader, term.field, term.bytes, null, null, false);
    int count = 0;
    while (docsEnum.nextDoc() != DocIdSetIterator.NO_MORE_DOCS)
      count++;
    assertEquals(numDocs, count);
    reader.close();
  }

  private void setUpDirs(Directory dir, Directory aux) throws IOException {
    setUpDirs(dir, aux, false);
  }
  
  private void setUpDirs(Directory dir, Directory aux, boolean withID) throws IOException {
    IndexWriter writer = null;

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.CREATE).setMaxBufferedDocs(1000));
    // add 1000 documents in 1 segment
    if (withID) {
      addDocsWithID(writer, 1000, 0);
    } else {
      addDocs(writer, 1000);
    }
    assertEquals(1000, writer.maxDoc());
    assertEquals(1, writer.getSegmentCount());
    writer.close();

    writer = newWriter(
        aux,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setMaxBufferedDocs(1000).
            setMergePolicy(newLogMergePolicy(false, 10))
    );
    // add 30 documents in 3 segments
    for (int i = 0; i < 3; i++) {
      if (withID) {
        addDocsWithID(writer, 10, 10*i);
      } else {
        addDocs(writer, 10);
      }
      writer.close();
      writer = newWriter(
          aux,
          newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
              setOpenMode(OpenMode.APPEND).
              setMaxBufferedDocs(1000).
              setMergePolicy(newLogMergePolicy(false, 10))
      );
    }
    assertEquals(30, writer.maxDoc());
    assertEquals(3, writer.getSegmentCount());
    writer.close();
  }

  // LUCENE-1270
  public void testHangOnClose() throws IOException {

    Directory dir = newDirectory();
    LogByteSizeMergePolicy lmp = new LogByteSizeMergePolicy();
    lmp.setUseCompoundFile(false);
    lmp.setMergeFactor(100);
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(
        TEST_VERSION_CURRENT, new MockAnalyzer(random()))
        .setMaxBufferedDocs(5).setMergePolicy(lmp));

    Document doc = new Document();
    FieldType customType = new FieldType(TextField.TYPE_STORED);
    customType.setStoreTermVectors(true);
    customType.setStoreTermVectorPositions(true);
    customType.setStoreTermVectorOffsets(true);
    doc.add(newField("content", "aaa bbb ccc ddd eee fff ggg hhh iii", customType));
    for(int i=0;i<60;i++)
      writer.addDocument(doc);

    Document doc2 = new Document();
    FieldType customType2 = new FieldType();
    customType2.setStored(true);
    doc2.add(newField("content", "aaa bbb ccc ddd eee fff ggg hhh iii", customType2));
    doc2.add(newField("content", "aaa bbb ccc ddd eee fff ggg hhh iii", customType2));
    doc2.add(newField("content", "aaa bbb ccc ddd eee fff ggg hhh iii", customType2));
    doc2.add(newField("content", "aaa bbb ccc ddd eee fff ggg hhh iii", customType2));
    for(int i=0;i<10;i++)
      writer.addDocument(doc2);
    writer.close();

    Directory dir2 = newDirectory();
    lmp = new LogByteSizeMergePolicy();
    lmp.setMinMergeMB(0.0001);
    lmp.setUseCompoundFile(false);
    lmp.setMergeFactor(4);
    writer = new IndexWriter(dir2, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random()))
        .setMergeScheduler(new SerialMergeScheduler()).setMergePolicy(lmp));
    writer.addIndexes(dir);
    writer.close();
    dir.close();
    dir2.close();
  }

  // TODO: these are also in TestIndexWriter... add a simple doc-writing method
  // like this to LuceneTestCase?
  private void addDoc(IndexWriter writer) throws IOException
  {
      Document doc = new Document();
      doc.add(newTextField("content", "aaa", Field.Store.NO));
      writer.addDocument(doc);
  }
  
  private abstract class RunAddIndexesThreads {

    Directory dir, dir2;
    final static int NUM_INIT_DOCS = 17;
    IndexWriter writer2;
    final List<Throwable> failures = new ArrayList<Throwable>();
    volatile boolean didClose;
    final IndexReader[] readers;
    final int NUM_COPY;
    final static int NUM_THREADS = 5;
    final Thread[] threads = new Thread[NUM_THREADS];

    public RunAddIndexesThreads(int numCopy) throws Throwable {
      NUM_COPY = numCopy;
      dir = new MockDirectoryWrapper(random(), new RAMDirectory());
      IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(
          TEST_VERSION_CURRENT, new MockAnalyzer(random()))
          .setMaxBufferedDocs(2));
      for (int i = 0; i < NUM_INIT_DOCS; i++)
        addDoc(writer);
      writer.close();

      dir2 = newDirectory();
      writer2 = new IndexWriter(dir2, new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      writer2.commit();
      

      readers = new IndexReader[NUM_COPY];
      for(int i=0;i<NUM_COPY;i++)
        readers[i] = DirectoryReader.open(dir);
    }

    void launchThreads(final int numIter) {

      for(int i=0;i<NUM_THREADS;i++) {
        threads[i] = new Thread() {
            @Override
            public void run() {
              try {

                final Directory[] dirs = new Directory[NUM_COPY];
                for(int k=0;k<NUM_COPY;k++)
                  dirs[k] = new MockDirectoryWrapper(random(), new RAMDirectory(dir, newIOContext(random())));

                int j=0;

                while(true) {
                  // System.out.println(Thread.currentThread().getName() + ": iter j=" + j);
                  if (numIter > 0 && j == numIter)
                    break;
                  doBody(j++, dirs);
                }
              } catch (Throwable t) {
                handle(t);
              }
            }
          };
      }

      for(int i=0;i<NUM_THREADS;i++)
        threads[i].start();
    }

    void joinThreads() throws Exception {
      for(int i=0;i<NUM_THREADS;i++)
        threads[i].join();
    }

    void close(boolean doWait) throws Throwable {
      didClose = true;
      writer2.close(doWait);
    }

    void closeDir() throws Throwable {
      for(int i=0;i<NUM_COPY;i++)
        readers[i].close();
      dir2.close();
    }

    abstract void doBody(int j, Directory[] dirs) throws Throwable;
    abstract void handle(Throwable t);
  }

  private class CommitAndAddIndexes extends RunAddIndexesThreads {
    public CommitAndAddIndexes(int numCopy) throws Throwable {
      super(numCopy);
    }

    @Override
    void handle(Throwable t) {
      t.printStackTrace(System.out);
      synchronized(failures) {
        failures.add(t);
      }
    }

    @Override
    void doBody(int j, Directory[] dirs) throws Throwable {
      switch(j%5) {
      case 0:
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": TEST: addIndexes(Dir[]) then full merge");
        }
        writer2.addIndexes(dirs);
        writer2.forceMerge(1);
        break;
      case 1:
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": TEST: addIndexes(Dir[])");
        }
        writer2.addIndexes(dirs);
        break;
      case 2:
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": TEST: addIndexes(IndexReader[])");
        }
        writer2.addIndexes(readers);
        break;
      case 3:
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": TEST: addIndexes(Dir[]) then maybeMerge");
        }
        writer2.addIndexes(dirs);
        writer2.maybeMerge();
        break;
      case 4:
        if (VERBOSE) {
          System.out.println(Thread.currentThread().getName() + ": TEST: commit");
        }
        writer2.commit();
      }
    }
  }
  
  // LUCENE-1335: test simultaneous addIndexes & commits
  // from multiple threads
  public void testAddIndexesWithThreads() throws Throwable {

    final int NUM_ITER = TEST_NIGHTLY ? 15 : 5;
    final int NUM_COPY = 3;
    CommitAndAddIndexes c = new CommitAndAddIndexes(NUM_COPY);
    c.launchThreads(NUM_ITER);

    for(int i=0;i<100;i++)
      addDoc(c.writer2);

    c.joinThreads();

    int expectedNumDocs = 100+NUM_COPY*(4*NUM_ITER/5)*RunAddIndexesThreads.NUM_THREADS*RunAddIndexesThreads.NUM_INIT_DOCS;
    assertEquals("expected num docs don't match - failures: " + c.failures, expectedNumDocs, c.writer2.numDocs());

    c.close(true);

    assertTrue("found unexpected failures: " + c.failures, c.failures.isEmpty());

    IndexReader reader = DirectoryReader.open(c.dir2);
    assertEquals(expectedNumDocs, reader.numDocs());
    reader.close();

    c.closeDir();
  }

  private class CommitAndAddIndexes2 extends CommitAndAddIndexes {
    public CommitAndAddIndexes2(int numCopy) throws Throwable {
      super(numCopy);
    }

    @Override
    void handle(Throwable t) {
      if (!(t instanceof AlreadyClosedException) && !(t instanceof NullPointerException)) {
        t.printStackTrace(System.out);
        synchronized(failures) {
          failures.add(t);
        }
      }
    }
  }

  // LUCENE-1335: test simultaneous addIndexes & close
  public void testAddIndexesWithClose() throws Throwable {
    final int NUM_COPY = 3;
    CommitAndAddIndexes2 c = new CommitAndAddIndexes2(NUM_COPY);
    //c.writer2.setInfoStream(System.out);
    c.launchThreads(-1);

    // Close w/o first stopping/joining the threads
    c.close(true);
    //c.writer2.close();

    c.joinThreads();

    c.closeDir();

    assertTrue(c.failures.size() == 0);
  }

  private class CommitAndAddIndexes3 extends RunAddIndexesThreads {
    public CommitAndAddIndexes3(int numCopy) throws Throwable {
      super(numCopy);
    }

    @Override
    void doBody(int j, Directory[] dirs) throws Throwable {
      switch(j%5) {
      case 0:
        if (VERBOSE) {
          System.out.println("TEST: " + Thread.currentThread().getName() + ": addIndexes + full merge");
        }
        writer2.addIndexes(dirs);
        writer2.forceMerge(1);
        break;
      case 1:
        if (VERBOSE) {
          System.out.println("TEST: " + Thread.currentThread().getName() + ": addIndexes");
        }
        writer2.addIndexes(dirs);
        break;
      case 2:
        if (VERBOSE) {
          System.out.println("TEST: " + Thread.currentThread().getName() + ": addIndexes(IR[])");
        }
        writer2.addIndexes(readers);
        break;
      case 3:
        if (VERBOSE) {
          System.out.println("TEST: " + Thread.currentThread().getName() + ": full merge");
        }
        writer2.forceMerge(1);
        break;
      case 4:
        if (VERBOSE) {
          System.out.println("TEST: " + Thread.currentThread().getName() + ": commit");
        }
        writer2.commit();
      }
    }

    @Override
    void handle(Throwable t) {
      boolean report = true;

      if (t instanceof AlreadyClosedException || t instanceof MergePolicy.MergeAbortedException || t instanceof NullPointerException) {
        report = !didClose;
      } else if (t instanceof FileNotFoundException)  {
        report = !didClose;
      } else if (t instanceof IOException)  {
        Throwable t2 = t.getCause();
        if (t2 instanceof MergePolicy.MergeAbortedException) {
          report = !didClose;
        }
      }
      if (report) {
        t.printStackTrace(System.out);
        synchronized(failures) {
          failures.add(t);
        }
      }
    }
  }

  // LUCENE-1335: test simultaneous addIndexes & close
  public void testAddIndexesWithCloseNoWait() throws Throwable {

    final int NUM_COPY = 50;
    CommitAndAddIndexes3 c = new CommitAndAddIndexes3(NUM_COPY);
    c.launchThreads(-1);

    Thread.sleep(_TestUtil.nextInt(random(), 10, 500));

    // Close w/o first stopping/joining the threads
    if (VERBOSE) {
      System.out.println("TEST: now close(false)");
    }
    c.close(false);

    c.joinThreads();

    if (VERBOSE) {
      System.out.println("TEST: done join threads");
    }
    c.closeDir();

    assertTrue(c.failures.size() == 0);
  }

  // LUCENE-1335: test simultaneous addIndexes & close
  public void testAddIndexesWithRollback() throws Throwable {

    final int NUM_COPY = TEST_NIGHTLY ? 50 : 5;
    CommitAndAddIndexes3 c = new CommitAndAddIndexes3(NUM_COPY);
    c.launchThreads(-1);

    Thread.sleep(_TestUtil.nextInt(random(), 10, 500));

    // Close w/o first stopping/joining the threads
    if (VERBOSE) {
      System.out.println("TEST: now force rollback");
    }
    c.didClose = true;
    c.writer2.rollback();

    c.joinThreads();

    c.closeDir();

    assertTrue(c.failures.size() == 0);
  }
 
  // LUCENE-2996: tests that addIndexes(IndexReader) applies existing deletes correctly.
  public void testExistingDeletes() throws Exception {
    Directory[] dirs = new Directory[2];
    for (int i = 0; i < dirs.length; i++) {
      dirs[i] = newDirectory();
      IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
      IndexWriter writer = new IndexWriter(dirs[i], conf);
      Document doc = new Document();
      doc.add(new StringField("id", "myid", Field.Store.NO));
      writer.addDocument(doc);
      writer.close();
    }

    IndexWriterConfig conf = new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()));
    IndexWriter writer = new IndexWriter(dirs[0], conf);

    // Now delete the document
    writer.deleteDocuments(new Term("id", "myid"));
    IndexReader r = DirectoryReader.open(dirs[1]);
    try {
      writer.addIndexes(r);
    } finally {
      r.close();
    }
    writer.commit();
    assertEquals("Documents from the incoming index should not have been deleted", 1, writer.numDocs());
    writer.close();

    for (Directory dir : dirs) {
      dir.close();
    }

  }
  
  // just like addDocs but with ID, starting from docStart
  private void addDocsWithID(IndexWriter writer, int numDocs, int docStart) throws IOException {
    for (int i = 0; i < numDocs; i++) {
      Document doc = new Document();
      doc.add(newTextField("content", "aaa", Field.Store.NO));
      doc.add(newTextField("id", "" + (docStart + i), Field.Store.YES));
      writer.addDocument(doc);
    }
  }

  public void testSimpleCaseCustomCodec() throws IOException {
    // main directory
    Directory dir = newDirectory();
    // two auxiliary directories
    Directory aux = newDirectory();
    Directory aux2 = newDirectory();
    Codec codec = new CustomPerFieldCodec();
    IndexWriter writer = null;

    writer = newWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT,
        new MockAnalyzer(random())).setOpenMode(OpenMode.CREATE).setCodec(codec));
    // add 100 documents
    addDocsWithID(writer, 100, 0);
    assertEquals(100, writer.maxDoc());
    writer.commit();
    writer.close();
    _TestUtil.checkIndex(dir);

    writer = newWriter(
        aux,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setCodec(codec).
            setMaxBufferedDocs(10).
            setMergePolicy(newLogMergePolicy(false))
    );
    // add 40 documents in separate files
    addDocs(writer, 40);
    assertEquals(40, writer.maxDoc());
    writer.commit();
    writer.close();

    writer = newWriter(
        aux2,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.CREATE).
            setCodec(codec)
    );
    // add 40 documents in compound files
    addDocs2(writer, 50);
    assertEquals(50, writer.maxDoc());
    writer.commit();
    writer.close();

    // test doc count before segments are merged
    writer = newWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setOpenMode(OpenMode.APPEND).
            setCodec(codec)
    );
    assertEquals(100, writer.maxDoc());
    writer.addIndexes(aux, aux2);
    assertEquals(190, writer.maxDoc());
    writer.close();

    dir.close();
    aux.close();
    aux2.close();
  }

  private static final class CustomPerFieldCodec extends Lucene40Codec {
    private final PostingsFormat simpleTextFormat = PostingsFormat.forName("SimpleText");
    private final PostingsFormat defaultFormat = PostingsFormat.forName("Lucene40");
    private final PostingsFormat mockSepFormat = PostingsFormat.forName("MockSep");

    @Override
    public PostingsFormat getPostingsFormatForField(String field) {
      if (field.equals("id")) {
        return simpleTextFormat;
      } else if (field.equals("content")) {
        return mockSepFormat;
      } else {
        return defaultFormat;
      }
    }
  }


  // LUCENE-2790: tests that the non CFS files were deleted by addIndexes
  public void testNonCFSLeftovers() throws Exception {
    Directory[] dirs = new Directory[2];
    for (int i = 0; i < dirs.length; i++) {
      dirs[i] = new RAMDirectory();
      IndexWriter w = new IndexWriter(dirs[i], new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      Document d = new Document();
      FieldType customType = new FieldType(TextField.TYPE_STORED);
      customType.setStoreTermVectors(true);
      d.add(new Field("c", "v", customType));
      w.addDocument(d);
      w.close();
    }
    
    IndexReader[] readers = new IndexReader[] { DirectoryReader.open(dirs[0]), DirectoryReader.open(dirs[1]) };
    
    Directory dir = new MockDirectoryWrapper(random(), new RAMDirectory());
    IndexWriterConfig conf = new IndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setMergePolicy(newLogMergePolicy());
    LogMergePolicy lmp = (LogMergePolicy) conf.getMergePolicy();
    lmp.setUseCompoundFile(true);
    lmp.setNoCFSRatio(1.0); // Force creation of CFS
    IndexWriter w3 = new IndexWriter(dir, conf);
    w3.addIndexes(readers);
    w3.close();
    // we should now see segments_X,
    // segments.gen,_Y.cfs,_Y.cfe, _Z.si
    assertEquals("Only one compound segment should exist, but got: " + Arrays.toString(dir.listAll()), 5, dir.listAll().length);
    dir.close();
  }
  
  private static class UnRegisteredCodec extends Codec {
    public UnRegisteredCodec() {
      super("NotRegistered");
    }

    @Override
    public PostingsFormat postingsFormat() {
      return PostingsFormat.forName("Lucene40");
    }

    @Override
    public DocValuesFormat docValuesFormat() {
      return new Lucene40DocValuesFormat();
    }

    @Override
    public StoredFieldsFormat storedFieldsFormat() {
      return new Lucene40StoredFieldsFormat();
    }
    
    @Override
    public TermVectorsFormat termVectorsFormat() {
      return new Lucene40TermVectorsFormat();
    }
    
    @Override
    public FieldInfosFormat fieldInfosFormat() {
      return new Lucene40FieldInfosFormat();
    }

    @Override
    public SegmentInfoFormat segmentInfoFormat() {
      return new Lucene40SegmentInfoFormat();
    }

    @Override
    public NormsFormat normsFormat() {
      return new Lucene40NormsFormat();
    }
    
    @Override
    public LiveDocsFormat liveDocsFormat() {
      return new Lucene40LiveDocsFormat();
    }
  }
  
  /*
   * simple test that ensures we getting expected exceptions 
   */
  public void testAddIndexMissingCodec() throws IOException {
    BaseDirectoryWrapper toAdd = newDirectory();
    // Disable checkIndex, else we get an exception because
    // of the unregistered codec:
    toAdd.setCheckIndexOnClose(false);
    {
      IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT,
          new MockAnalyzer(random()));
      conf.setCodec(new UnRegisteredCodec());
      IndexWriter w = new IndexWriter(toAdd, conf);
      Document doc = new Document();
      FieldType customType = new FieldType();
      customType.setIndexed(true); 
      doc.add(newField("foo", "bar", customType));
      w.addDocument(doc);
      w.close();
    }

    {
      Directory dir = newDirectory();
      IndexWriterConfig conf = newIndexWriterConfig(TEST_VERSION_CURRENT,
          new MockAnalyzer(random()));
      conf.setCodec(_TestUtil.alwaysPostingsFormat(new Pulsing40PostingsFormat(1 + random().nextInt(20))));
      IndexWriter w = new IndexWriter(dir, conf);
      try {
        w.addIndexes(toAdd);
        fail("no such codec");
      } catch (IllegalArgumentException ex) {
        // expected
      }
      w.close();
      IndexReader open = DirectoryReader.open(dir);
      assertEquals(0, open.numDocs());
      open.close();
      dir.close();
    }

    try {
      DirectoryReader.open(toAdd);
      fail("no such codec");
    } catch (IllegalArgumentException ex) {
      // expected
    }
    toAdd.close();
  }

  // LUCENE-3575
  public void testFieldNamesChanged() throws IOException {
    Directory d1 = newDirectory();
    RandomIndexWriter w = new RandomIndexWriter(random(), d1);
    Document doc = new Document();
    doc.add(newStringField("f1", "doc1 field1", Field.Store.YES));
    doc.add(newStringField("id", "1", Field.Store.YES));
    w.addDocument(doc);
    IndexReader r1 = w.getReader();
    w.close();

    Directory d2 = newDirectory();
    w = new RandomIndexWriter(random(), d2);
    doc = new Document();
    doc.add(newStringField("f2", "doc2 field2", Field.Store.YES));
    doc.add(newStringField("id", "2", Field.Store.YES));
    w.addDocument(doc);
    IndexReader r2 = w.getReader();
    w.close();

    Directory d3 = newDirectory();
    w = new RandomIndexWriter(random(), d3);
    w.addIndexes(r1, r2);
    r1.close();
    d1.close();
    r2.close();
    d2.close();

    IndexReader r3 = w.getReader();
    w.close();
    assertEquals(2, r3.numDocs());
    for(int docID=0;docID<2;docID++) {
      Document d = r3.document(docID);
      if (d.get("id").equals("1")) {
        assertEquals("doc1 field1", d.get("f1"));
      } else {
        assertEquals("doc2 field2", d.get("f2"));
      }
    }
    r3.close();
    d3.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1672.java