error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/134.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/134.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/134.java
text:
```scala
c@@heckDirectoryFilter(FSDirectory.open(new File(System.getProperty("tempDir"),"test")));

package org.apache.lucene.store;

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
import org.apache.lucene.util._TestUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestDirectory extends LuceneTestCase {

  public void testDetectClose() throws Throwable {
    Directory dir = new RAMDirectory();
    dir.close();
    try {
      dir.createOutput("test");
      fail("did not hit expected exception");
    } catch (AlreadyClosedException ace) {
    }

    dir = FSDirectory.open(new File(System.getProperty("tempDir")));
    dir.close();
    try {
      dir.createOutput("test");
      fail("did not hit expected exception");
    } catch (AlreadyClosedException ace) {
    }
  }


  // Test that different instances of FSDirectory can coexist on the same
  // path, can read, write, and lock files.
  public void testDirectInstantiation() throws Exception {
    File path = new File(System.getProperty("tempDir"));

    int sz = 3;
    Directory[] dirs = new Directory[sz];

    dirs[0] = new SimpleFSDirectory(path, null);
    dirs[1] = new NIOFSDirectory(path, null);
    dirs[2] = new MMapDirectory(path, null);

    for (int i=0; i<sz; i++) {
      Directory dir = dirs[i];
      dir.ensureOpen();
      String fname = "foo." + i;
      String lockname = "foo" + i + ".lck";
      IndexOutput out = dir.createOutput(fname);
      out.writeByte((byte)i);
      out.close();

      for (int j=0; j<sz; j++) {
        Directory d2 = dirs[j];
        d2.ensureOpen();
        assertTrue(d2.fileExists(fname));
        assertEquals(1, d2.fileLength(fname));

        // don't test read on MMapDirectory, since it can't really be
        // closed and will cause a failure to delete the file.
        if (d2 instanceof MMapDirectory) continue;
        
        IndexInput input = d2.openInput(fname);
        assertEquals((byte)i, input.readByte());
        input.close();
      }

      // delete with a different dir
      dirs[(i+1)%sz].deleteFile(fname);

      for (int j=0; j<sz; j++) {
        Directory d2 = dirs[j];
        assertFalse(d2.fileExists(fname));
      }

      Lock lock = dir.makeLock(lockname);
      assertTrue(lock.obtain());

      for (int j=0; j<sz; j++) {
        Directory d2 = dirs[j];
        Lock lock2 = d2.makeLock(lockname);
        try {
          assertFalse(lock2.obtain(1));
        } catch (LockObtainFailedException e) {
          // OK
        }
      }

      lock.release();
      
      // now lock with different dir
      lock = dirs[(i+1)%sz].makeLock(lockname);
      assertTrue(lock.obtain());
      lock.release();
    }

    for (int i=0; i<sz; i++) {
      Directory dir = dirs[i];
      dir.ensureOpen();
      dir.close();
      assertFalse(dir.isOpen);
    }
  }

  // LUCENE-1464
  public void testDontCreate() throws Throwable {
    File path = new File(System.getProperty("tempDir"), "doesnotexist");
    try {
      assertTrue(!path.exists());
      Directory dir = new SimpleFSDirectory(path, null);
      assertTrue(!path.exists());
      dir.close();
    } finally {
      _TestUtil.rmDir(path);
    }
  }

  // LUCENE-1468
  public void testRAMDirectoryFilter() throws IOException {
    checkDirectoryFilter(new RAMDirectory());
  }

  // LUCENE-1468
  public void testFSDirectoryFilter() throws IOException {
    checkDirectoryFilter(FSDirectory.open(new File("test")));
  }

  // LUCENE-1468
  private void checkDirectoryFilter(Directory dir) throws IOException {
    String name = "file";
    try {
      dir.createOutput(name).close();
      assertTrue(dir.fileExists(name));
      assertTrue(Arrays.asList(dir.listAll()).contains(name));
    } finally {
      dir.close();
    }
  }

  // LUCENE-1468
  public void testCopySubdir() throws Throwable {
    File path = new File(System.getProperty("tempDir"), "testsubdir");
    try {
      path.mkdirs();
      new File(path, "subdir").mkdirs();
      Directory fsDir = new SimpleFSDirectory(path, null);
      assertEquals(0, new RAMDirectory(fsDir).listAll().length);
    } finally {
      _TestUtil.rmDir(path);
    }
  }

  // LUCENE-1468
  public void testNotDirectory() throws Throwable {
    File path = new File(System.getProperty("tempDir"), "testnotdir");
    Directory fsDir = new SimpleFSDirectory(path, null);
    try {
      IndexOutput out = fsDir.createOutput("afile");
      out.close();
      assertTrue(fsDir.fileExists("afile"));
      try {
        new SimpleFSDirectory(new File(path, "afile"), null);
        fail("did not hit expected exception");
      } catch (NoSuchDirectoryException nsde) {
        // Expected
      }
    } finally {
      fsDir.close();
      _TestUtil.rmDir(path);
    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/134.java