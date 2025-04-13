error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9799.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9799.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9799.java
text:
```scala
t@@heInstance.q = new ReferenceQueue<Object>();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.io.testtools.FileBasedTestCase;

/**
 * This is used to test {@link FileCleaningTracker} for correctness.
 *
 * @author Noel Bergman
 * @author Martin Cooper
 *
 * @version $Id$

 * @see FileCleaner
 */
public class FileCleaningTrackerTestCase extends FileBasedTestCase {
    protected FileCleaningTracker newInstance() {
        return new FileCleaningTracker();
    }

    private File testFile;
    private FileCleaningTracker theInstance;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(FileCleaningTrackerTestCase.class);
    }

    public FileCleaningTrackerTestCase(String name) {
        super(name);
        testFile = new File(getTestDirectory(), "file-test.txt");
    }

    /** @see junit.framework.TestCase#setUp() */
    @Override
    protected void setUp() throws Exception {
        theInstance = newInstance();
        getTestDirectory().mkdirs();
    }

    /** @see junit.framework.TestCase#tearDown() */
    @Override
    protected void tearDown() throws Exception {
        FileUtils.deleteDirectory(getTestDirectory());
        
        // reset file cleaner class, so as not to break other tests

        /**
         * The following block of code can possibly be removed when the
         * deprecated {@link FileCleaner} is gone. The question is, whether
         * we want to support reuse of {@link FileCleaningTracker} instances,
         * which we should, IMO, not.
         */
        {
            theInstance.q = new ReferenceQueue();
            theInstance.trackers.clear();
            theInstance.exitWhenFinished = false;
            theInstance.reaper = null;
        }
        
        theInstance = null;
    }

    //-----------------------------------------------------------------------
    public void testFileCleanerFile() throws Exception {
        String path = testFile.getPath();
        
        assertEquals(false, testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertEquals(true, testFile.exists());
        
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals(1, theInstance.getTrackCount());
        
        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        
        assertEquals(0, theInstance.getTrackCount());
        assertEquals(false, new File(path).exists());
    }

    public void testFileCleanerDirectory() throws Exception {
        createFile(testFile, 100);
        assertEquals(true, testFile.exists());
        assertEquals(true, getTestDirectory().exists());
        
        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj);
        assertEquals(1, theInstance.getTrackCount());
        
        obj = null;

        waitUntilTrackCount();

        assertEquals(0, theInstance.getTrackCount());
        assertEquals(true, testFile.exists());  // not deleted, as dir not empty
        assertEquals(true, testFile.getParentFile().exists());  // not deleted, as dir not empty
    }

    public void testFileCleanerDirectory_NullStrategy() throws Exception {
        createFile(testFile, 100);
        assertEquals(true, testFile.exists());
        assertEquals(true, getTestDirectory().exists());
        
        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj, (FileDeleteStrategy) null);
        assertEquals(1, theInstance.getTrackCount());
        
        obj = null;

        waitUntilTrackCount();
        
        assertEquals(0, theInstance.getTrackCount());
        assertEquals(true, testFile.exists());  // not deleted, as dir not empty
        assertEquals(true, testFile.getParentFile().exists());  // not deleted, as dir not empty
    }

    public void testFileCleanerDirectory_ForceStrategy() throws Exception {
        createFile(testFile, 100);
        assertEquals(true, testFile.exists());
        assertEquals(true, getTestDirectory().exists());
        
        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj, FileDeleteStrategy.FORCE);
        assertEquals(1, theInstance.getTrackCount());
        
        obj = null;

        waitUntilTrackCount();
        
        assertEquals(0, theInstance.getTrackCount());
        assertEquals(false, testFile.exists());
        assertEquals(false, testFile.getParentFile().exists());
    }

    public void testFileCleanerNull() throws Exception {
        try {
            theInstance.track((File) null, new Object());
            fail();
        } catch (NullPointerException ex) {
            // expected
        }
        try {
            theInstance.track((File) null, new Object(), FileDeleteStrategy.NORMAL);
            fail();
        } catch (NullPointerException ex) {
            // expected
        }
        try {
            theInstance.track((String) null, new Object());
            fail();
        } catch (NullPointerException ex) {
            // expected
        }
        try {
            theInstance.track((String) null, new Object(), FileDeleteStrategy.NORMAL);
            fail();
        } catch (NullPointerException ex) {
            // expected
        }
    }

    public void testFileCleanerExitWhenFinishedFirst() throws Exception {
        assertEquals(false, theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertEquals(true, theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
        
        waitUntilTrackCount();
        
        assertEquals(0, theInstance.getTrackCount());
        assertEquals(true, theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
    }

    public void testFileCleanerExitWhenFinished_NoTrackAfter() throws Exception {
        assertEquals(false, theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertEquals(true, theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
        
        String path = testFile.getPath();
        Object marker = new Object();
        try {
            theInstance.track(path, marker);
            fail();
        } catch (IllegalStateException ex) {
            // expected
        }
        assertEquals(true, theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
    }

    public void testFileCleanerExitWhenFinished1() throws Exception {
        String path = testFile.getPath();
        
        assertEquals("1-testFile exists", false, testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertEquals("2-testFile exists", true, testFile.exists());
        
        assertEquals("3-Track Count", 0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals("4-Track Count", 1, theInstance.getTrackCount());
        assertEquals("5-exitWhenFinished", false, theInstance.exitWhenFinished);
        assertEquals("6-reaper.isAlive", true, theInstance.reaper.isAlive());
        
        assertEquals("7-exitWhenFinished", false, theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertEquals("8-exitWhenFinished", true, theInstance.exitWhenFinished);
        assertEquals("9-reaper.isAlive", true, theInstance.reaper.isAlive());
        
        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        
        assertEquals("10-Track Count", 0, theInstance.getTrackCount());
        assertEquals("11-testFile exists", false, new File(path).exists());
        assertEquals("12-exitWhenFinished", true, theInstance.exitWhenFinished);
        assertEquals("13-reaper.isAlive", false, theInstance.reaper.isAlive());
    }

    public void testFileCleanerExitWhenFinished2() throws Exception {
        String path = testFile.getPath();
        
        assertEquals(false, testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertEquals(true, testFile.exists());
        
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals(1, theInstance.getTrackCount());
        assertEquals(false, theInstance.exitWhenFinished);
        assertEquals(true, theInstance.reaper.isAlive());
        
        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        
        assertEquals(0, theInstance.getTrackCount());
        assertEquals(false, new File(path).exists());
        assertEquals(false, theInstance.exitWhenFinished);
        assertEquals(true, theInstance.reaper.isAlive());
        
        assertEquals(false, theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        for (int i = 0; i < 20 && theInstance.reaper.isAlive(); i++) {
            Thread.sleep(500L);  // allow reaper thread to die
        }
        assertEquals(true, theInstance.exitWhenFinished);
        assertEquals(false, theInstance.reaper.isAlive());
    }

    //-----------------------------------------------------------------------
    private void waitUntilTrackCount() throws Exception {
        int count = 0;
        while(theInstance.getTrackCount() != 0 && count++ < 5) {
            List<String> list = new ArrayList<String>();
            try {
                long i = 0;
                while (true && theInstance.getTrackCount() != 0) {
                    list.add("A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String " + (i++));
                }
            } catch (Throwable ignored) {
            }
            list.clear();
            list = null;
            System.gc(); 
            Thread.sleep(1000);
        }
        if (theInstance.getTrackCount() != 0) {
            throw new IllegalStateException("Your JVM is not releasing SoftReference, try running the testcase with less memory (-Xmx)");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9799.java