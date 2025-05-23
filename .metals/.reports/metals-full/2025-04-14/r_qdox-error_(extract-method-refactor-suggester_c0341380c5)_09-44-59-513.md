error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1972.java
text:
```scala
w@@riter = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND).setMergePolicy(newLogMergePolicy(false)));

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

import org.apache.lucene.analysis.MockAnalyzer;
import org.apache.lucene.codecs.LiveDocsFormat;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MockDirectoryWrapper;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

import static org.apache.lucene.index.TestIndexWriter.assertNoUnreferencedFiles;

/**
 * Tests for IndexWriter when the disk runs out of space
 */
public class TestIndexWriterOnDiskFull extends LuceneTestCase {

  /*
   * Make sure IndexWriter cleans up on hitting a disk
   * full exception in addDocument.
   * TODO: how to do this on windows with FSDirectory?
   */
  public void testAddDocumentOnDiskFull() throws IOException {

    for(int pass=0;pass<2;pass++) {
      if (VERBOSE) {
        System.out.println("TEST: pass=" + pass);
      }
      boolean doAbort = pass == 1;
      long diskFree = _TestUtil.nextInt(random(), 100, 300);
      while(true) {
        if (VERBOSE) {
          System.out.println("TEST: cycle: diskFree=" + diskFree);
        }
        MockDirectoryWrapper dir = new MockDirectoryWrapper(random(), new RAMDirectory());
        dir.setMaxSizeInBytes(diskFree);
        IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
        MergeScheduler ms = writer.getConfig().getMergeScheduler();
        if (ms instanceof ConcurrentMergeScheduler) {
          // This test intentionally produces exceptions
          // in the threads that CMS launches; we don't
          // want to pollute test output with these.
          ((ConcurrentMergeScheduler) ms).setSuppressExceptions();
        }

        boolean hitError = false;
        try {
          for(int i=0;i<200;i++) {
            addDoc(writer);
          }
          if (VERBOSE) {
            System.out.println("TEST: done adding docs; now commit");
          }
          writer.commit();
        } catch (IOException e) {
          if (VERBOSE) {
            System.out.println("TEST: exception on addDoc");
            e.printStackTrace(System.out);
          }
          hitError = true;
        }

        if (hitError) {
          if (doAbort) {
            if (VERBOSE) {
              System.out.println("TEST: now rollback");
            }
            writer.rollback();
          } else {
            try {
              if (VERBOSE) {
                System.out.println("TEST: now close");
              }
              writer.close();
            } catch (IOException e) {
              if (VERBOSE) {
                System.out.println("TEST: exception on close; retry w/ no disk space limit");
                e.printStackTrace(System.out);
              }
              dir.setMaxSizeInBytes(0);
              writer.close();
            }
          }

          //_TestUtil.syncConcurrentMerges(ms);

          if (_TestUtil.anyFilesExceptWriteLock(dir)) {
            assertNoUnreferencedFiles(dir, "after disk full during addDocument");
            
            // Make sure reader can open the index:
            DirectoryReader.open(dir).close();
          }
            
          dir.close();
          // Now try again w/ more space:

          diskFree += TEST_NIGHTLY ? _TestUtil.nextInt(random(), 400, 600) : _TestUtil.nextInt(random(), 3000, 5000);
        } else {
          //_TestUtil.syncConcurrentMerges(writer);
          dir.setMaxSizeInBytes(0);
          writer.close();
          dir.close();
          break;
        }
      }
    }
  }

  // TODO: make @Nightly variant that provokes more disk
  // fulls

  // TODO: have test fail if on any given top
  // iter there was not a single IOE hit

  /*
  Test: make sure when we run out of disk space or hit
  random IOExceptions in any of the addIndexes(*) calls
  that 1) index is not corrupt (searcher can open/search
  it) and 2) transactional semantics are followed:
  either all or none of the incoming documents were in
  fact added.
   */
  public void testAddIndexOnDiskFull() throws IOException
  {
    // MemoryCodec, since it uses FST, is not necessarily
    // "additive", ie if you add up N small FSTs, then merge
    // them, the merged result can easily be larger than the
    // sum because the merged FST may use array encoding for
    // some arcs (which uses more space):

    final String idFormat = _TestUtil.getPostingsFormat("id");
    final String contentFormat = _TestUtil.getPostingsFormat("content");
    assumeFalse("This test cannot run with Memory codec", idFormat.equals("Memory") || contentFormat.equals("Memory"));

    int START_COUNT = 57;
    int NUM_DIR = TEST_NIGHTLY ? 50 : 5;
    int END_COUNT = START_COUNT + NUM_DIR* (TEST_NIGHTLY ? 25 : 5);
    
    // Build up a bunch of dirs that have indexes which we
    // will then merge together by calling addIndexes(*):
    Directory[] dirs = new Directory[NUM_DIR];
    long inputDiskUsage = 0;
    for(int i=0;i<NUM_DIR;i++) {
      dirs[i] = newDirectory();
      IndexWriter writer = new IndexWriter(dirs[i], newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
      for(int j=0;j<25;j++) {
        addDocWithIndex(writer, 25*i+j);
      }
      writer.close();
      String[] files = dirs[i].listAll();
      for(int j=0;j<files.length;j++) {
        inputDiskUsage += dirs[i].fileLength(files[j]);
      }
    }
    
    // Now, build a starting index that has START_COUNT docs.  We
    // will then try to addIndexes into a copy of this:
    MockDirectoryWrapper startDir = newMockDirectory();
    IndexWriter writer = new IndexWriter(startDir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())));
    for(int j=0;j<START_COUNT;j++) {
      addDocWithIndex(writer, j);
    }
    writer.close();
    
    // Make sure starting index seems to be working properly:
    Term searchTerm = new Term("content", "aaa");        
    IndexReader reader = DirectoryReader.open(startDir);
    assertEquals("first docFreq", 57, reader.docFreq(searchTerm));
    
    IndexSearcher searcher = newSearcher(reader);
    ScoreDoc[] hits = searcher.search(new TermQuery(searchTerm), null, 1000).scoreDocs;
    assertEquals("first number of hits", 57, hits.length);
    reader.close();
    
    // Iterate with larger and larger amounts of free
    // disk space.  With little free disk space,
    // addIndexes will certainly run out of space &
    // fail.  Verify that when this happens, index is
    // not corrupt and index in fact has added no
    // documents.  Then, we increase disk space by 2000
    // bytes each iteration.  At some point there is
    // enough free disk space and addIndexes should
    // succeed and index should show all documents were
    // added.
    
    // String[] files = startDir.listAll();
    long diskUsage = startDir.sizeInBytes();
    
    long startDiskUsage = 0;
    String[] files = startDir.listAll();
    for(int i=0;i<files.length;i++) {
      startDiskUsage += startDir.fileLength(files[i]);
    }

    for(int iter=0;iter<3;iter++) {
      
      if (VERBOSE) {
        System.out.println("TEST: iter=" + iter);
      }
      
      // Start with 100 bytes more than we are currently using:
      long diskFree = diskUsage+_TestUtil.nextInt(random(), 50, 200);
      
      int method = iter;
      
      boolean success = false;
      boolean done = false;
      
      String methodName;
      if (0 == method) {
        methodName = "addIndexes(Directory[]) + forceMerge(1)";
      } else if (1 == method) {
        methodName = "addIndexes(IndexReader[])";
      } else {
        methodName = "addIndexes(Directory[])";
      }
      
      while(!done) {
        if (VERBOSE) {
          System.out.println("TEST: cycle...");
        }
        
        // Make a new dir that will enforce disk usage:
        MockDirectoryWrapper dir = new MockDirectoryWrapper(random(), new RAMDirectory(startDir, newIOContext(random())));
        writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).setOpenMode(OpenMode.APPEND).setMergePolicy(newLogMergePolicy()));
        IOException err = null;

        MergeScheduler ms = writer.getConfig().getMergeScheduler();
        for(int x=0;x<2;x++) {
          if (ms instanceof ConcurrentMergeScheduler)
            // This test intentionally produces exceptions
            // in the threads that CMS launches; we don't
            // want to pollute test output with these.
            if (0 == x)
              ((ConcurrentMergeScheduler) ms).setSuppressExceptions();
            else
              ((ConcurrentMergeScheduler) ms).clearSuppressExceptions();
          
          // Two loops: first time, limit disk space &
          // throw random IOExceptions; second time, no
          // disk space limit:
          
          double rate = 0.05;
          double diskRatio = ((double) diskFree)/diskUsage;
          long thisDiskFree;
          
          String testName = null;
          
          if (0 == x) {
            thisDiskFree = diskFree;
            if (diskRatio >= 2.0) {
              rate /= 2;
            }
            if (diskRatio >= 4.0) {
              rate /= 2;
            }
            if (diskRatio >= 6.0) {
              rate = 0.0;
            }
            if (VERBOSE)
              testName = "disk full test " + methodName + " with disk full at " + diskFree + " bytes";
          } else {
            thisDiskFree = 0;
            rate = 0.0;
            if (VERBOSE)
              testName = "disk full test " + methodName + " with unlimited disk space";
          }
          
          if (VERBOSE)
            System.out.println("\ncycle: " + testName);
          
          dir.setTrackDiskUsage(true);
          dir.setMaxSizeInBytes(thisDiskFree);
          dir.setRandomIOExceptionRate(rate);
          
          try {
            
            if (0 == method) {
              if (VERBOSE) {
                System.out.println("TEST: now addIndexes count=" + dirs.length);
              }
              writer.addIndexes(dirs);
              if (VERBOSE) {
                System.out.println("TEST: now forceMerge");
              }
              writer.forceMerge(1);
            } else if (1 == method) {
              IndexReader readers[] = new IndexReader[dirs.length];
              for(int i=0;i<dirs.length;i++) {
                readers[i] = DirectoryReader.open(dirs[i]);
              }
              try {
                writer.addIndexes(readers);
              } finally {
                for(int i=0;i<dirs.length;i++) {
                  readers[i].close();
                }
              }
            } else {
              writer.addIndexes(dirs);
            }
            
            success = true;
            if (VERBOSE) {
              System.out.println("  success!");
            }
            
            if (0 == x) {
              done = true;
            }
            
          } catch (IOException e) {
            success = false;
            err = e;
            if (VERBOSE) {
              System.out.println("  hit IOException: " + e);
              e.printStackTrace(System.out);
            }
            
            if (1 == x) {
              e.printStackTrace(System.out);
              fail(methodName + " hit IOException after disk space was freed up");
            }
          }
          
          // Make sure all threads from
          // ConcurrentMergeScheduler are done
          _TestUtil.syncConcurrentMerges(writer);
          
          if (VERBOSE) {
            System.out.println("  now test readers");
          }
          
          // Finally, verify index is not corrupt, and, if
          // we succeeded, we see all docs added, and if we
          // failed, we see either all docs or no docs added
          // (transactional semantics):
          try {
            reader = DirectoryReader.open(dir);
          } catch (IOException e) {
            e.printStackTrace(System.out);
            fail(testName + ": exception when creating IndexReader: " + e);
          }
          int result = reader.docFreq(searchTerm);
          if (success) {
            if (result != START_COUNT) {
              fail(testName + ": method did not throw exception but docFreq('aaa') is " + result + " instead of expected " + START_COUNT);
            }
          } else {
            // On hitting exception we still may have added
            // all docs:
            if (result != START_COUNT && result != END_COUNT) {
              err.printStackTrace(System.out);
              fail(testName + ": method did throw exception but docFreq('aaa') is " + result + " instead of expected " + START_COUNT + " or " + END_COUNT);
            }
          }
          
          searcher = newSearcher(reader);
          try {
            hits = searcher.search(new TermQuery(searchTerm), null, END_COUNT).scoreDocs;
          } catch (IOException e) {
            e.printStackTrace(System.out);
            fail(testName + ": exception when searching: " + e);
          }
          int result2 = hits.length;
          if (success) {
            if (result2 != result) {
              fail(testName + ": method did not throw exception but hits.length for search on term 'aaa' is " + result2 + " instead of expected " + result);
            }
          } else {
            // On hitting exception we still may have added
            // all docs:
            if (result2 != result) {
              err.printStackTrace(System.out);
              fail(testName + ": method did throw exception but hits.length for search on term 'aaa' is " + result2 + " instead of expected " + result);
            }
          }
          
          reader.close();
          if (VERBOSE) {
            System.out.println("  count is " + result);
          }
          
          if (done || result == END_COUNT) {
            break;
          }
        }
        
        if (VERBOSE) {
          System.out.println("  start disk = " + startDiskUsage + "; input disk = " + inputDiskUsage + "; max used = " + dir.getMaxUsedSizeInBytes());
        }
        
        if (done) {
          // Javadocs state that temp free Directory space
          // required is at most 2X total input size of
          // indices so let's make sure:
          assertTrue("max free Directory space required exceeded 1X the total input index sizes during " + methodName +
                     ": max temp usage = " + (dir.getMaxUsedSizeInBytes()-startDiskUsage) + " bytes vs limit=" + (2*(startDiskUsage + inputDiskUsage)) +
                     "; starting disk usage = " + startDiskUsage + " bytes; " +
                     "input index disk usage = " + inputDiskUsage + " bytes",
                     (dir.getMaxUsedSizeInBytes()-startDiskUsage) < 2*(startDiskUsage + inputDiskUsage));
        }
        
        // Make sure we don't hit disk full during close below:
        dir.setMaxSizeInBytes(0);
        dir.setRandomIOExceptionRate(0.0);
        
        writer.close();
        
        // Wait for all BG threads to finish else
        // dir.close() will throw IOException because
        // there are still open files
        _TestUtil.syncConcurrentMerges(ms);
        
        dir.close();
        
        // Try again with more free space:
        diskFree += TEST_NIGHTLY ? _TestUtil.nextInt(random(), 4000, 8000) : _TestUtil.nextInt(random(), 40000, 80000);
      }
    }
    
    startDir.close();
    for (Directory dir : dirs)
      dir.close();
  }
  
  private static class FailTwiceDuringMerge extends MockDirectoryWrapper.Failure {
    public boolean didFail1;
    public boolean didFail2;

    @Override
    public void eval(MockDirectoryWrapper dir)  throws IOException {
      if (!doFail) {
        return;
      }
      StackTraceElement[] trace = new Exception().getStackTrace();
      for (int i = 0; i < trace.length; i++) {
        if (SegmentMerger.class.getName().equals(trace[i].getClassName()) && "mergeTerms".equals(trace[i].getMethodName()) && !didFail1) {
          didFail1 = true;
          throw new IOException("fake disk full during mergeTerms");
        }
        if (LiveDocsFormat.class.getName().equals(trace[i].getClassName()) && "writeLiveDocs".equals(trace[i].getMethodName()) && !didFail2) {
          didFail2 = true;
          throw new IOException("fake disk full while writing LiveDocs");
        }
      }
    }
  }
  
  // LUCENE-2593
  public void testCorruptionAfterDiskFullDuringMerge() throws IOException {
    MockDirectoryWrapper dir = newMockDirectory();
    //IndexWriter w = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random)).setReaderPooling(true));
    IndexWriter w = new IndexWriter(
        dir,
        newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random())).
            setMergeScheduler(new SerialMergeScheduler()).
            setReaderPooling(true).
            setMergePolicy(newLogMergePolicy(2))
    );
    _TestUtil.keepFullyDeletedSegments(w);

    Document doc = new Document();

    doc.add(newTextField("f", "doctor who", Field.Store.NO));
    w.addDocument(doc);
    w.commit();

    w.deleteDocuments(new Term("f", "who"));
    w.addDocument(doc);
    
    // disk fills up!
    FailTwiceDuringMerge ftdm = new FailTwiceDuringMerge();
    ftdm.setDoFail();
    dir.failOn(ftdm);

    try {
      w.commit();
      fail("fake disk full IOExceptions not hit");
    } catch (IOException ioe) {
      // expected
      assertTrue(ftdm.didFail1 || ftdm.didFail2);
    }
    _TestUtil.checkIndex(dir);
    ftdm.clearDoFail();
    w.addDocument(doc);
    w.close();

    dir.close();
  }
  
  // LUCENE-1130: make sure immeidate disk full on creating
  // an IndexWriter (hit during DW.ThreadState.init()) is
  // OK:
  public void testImmediateDiskFull() throws IOException {
    MockDirectoryWrapper dir = newMockDirectory();
    IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(TEST_VERSION_CURRENT, new MockAnalyzer(random()))
        .setMaxBufferedDocs(2).setMergeScheduler(new ConcurrentMergeScheduler()));
    dir.setMaxSizeInBytes(Math.max(1, dir.getRecomputedActualSizeInBytes()));
    final Document doc = new Document();
    FieldType customType = new FieldType(TextField.TYPE_STORED);
    doc.add(newField("field", "aaa bbb ccc ddd eee fff ggg hhh iii jjj", customType));
    try {
      writer.addDocument(doc);
      fail("did not hit disk full");
    } catch (IOException ioe) {
    }
    // Without fix for LUCENE-1130: this call will hang:
    try {
      writer.addDocument(doc);
      fail("did not hit disk full");
    } catch (IOException ioe) {
    }
    try {
      writer.close(false);
      fail("did not hit disk full");
    } catch (IOException ioe) {
    }

    // Make sure once disk space is avail again, we can
    // cleanly close:
    dir.setMaxSizeInBytes(0);
    writer.close(false);
    dir.close();
  }
  
  // TODO: these are also in TestIndexWriter... add a simple doc-writing method
  // like this to LuceneTestCase?
  private void addDoc(IndexWriter writer) throws IOException
  {
      Document doc = new Document();
      doc.add(newTextField("content", "aaa", Field.Store.NO));
      writer.addDocument(doc);
  }
  
  private void addDocWithIndex(IndexWriter writer, int index) throws IOException
  {
      Document doc = new Document();
      doc.add(newTextField("content", "aaa " + index, Field.Store.NO));
      doc.add(newTextField("id", "" + index, Field.Store.NO));
      writer.addDocument(doc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1972.java