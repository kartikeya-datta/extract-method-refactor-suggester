error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12813.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12813.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12813.java
text:
```scala
protected b@@oolean handleDirectory(File directory, int depth, Collection results) {

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
import java.io.FileFilter;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;

/**
 * This is used to test DirectoryWalker for correctness.
 *
 * @version $Id$
 * @see DirectoryWalker
 *
 */
public class DirectoryWalkerTestCase extends TestCase {

    // Directories
    private static final File current      = new File(".");
    private static final File javaDir      = new File("src/java");
    private static final File orgDir       = new File(javaDir, "org");
    private static final File apacheDir    = new File(orgDir, "apache");
    private static final File commonsDir   = new File(apacheDir, "commons");
    private static final File ioDir        = new File(commonsDir, "io");
    private static final File outputDir    = new File(ioDir, "output");
    private static final File[] dirs       = new File[] {orgDir, apacheDir, commonsDir, ioDir, outputDir};

    // Files
    private static final File copyUtils     = new File(ioDir, "CopyUtils.java");
    private static final File ioUtils       = new File(ioDir, "IOUtils.java");
    private static final File proxyWriter   = new File(outputDir, "ProxyWriter.java");
    private static final File nullStream    = new File(outputDir, "NullOutputStream.java");
    private static final File[] ioFiles     = new File[] {copyUtils, ioUtils};
    private static final File[] outputFiles = new File[] {proxyWriter, nullStream};
    
    // Filters
    private static final IOFileFilter dirsFilter        = createNameFilter(dirs);
    private static final IOFileFilter iofilesFilter     = createNameFilter(ioFiles);
    private static final IOFileFilter outputFilesFilter = createNameFilter(outputFiles);
    private static final IOFileFilter ioDirAndFilesFilter = new OrFileFilter(dirsFilter, iofilesFilter);
    private static final IOFileFilter dirsAndFilesFilter = new OrFileFilter(ioDirAndFilesFilter, outputFilesFilter);

    // Filter to exclude SVN files
    private static final IOFileFilter NOT_SVN = FileFilterUtils.makeSVNAware(null);

    public static Test suite() {
        return new TestSuite(DirectoryWalkerTestCase.class);
    }

    /** Construct the TestCase using the name */
    public DirectoryWalkerTestCase(String name) {
        super(name);
    }

    /** Set Up */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** Tear Down */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //-----------------------------------------------------------------------

    /**
     * Test Filtering
     */
    public void testFilter() {
        List results = new TestFileFinder(dirsAndFilesFilter, -1).find(javaDir);
        assertEquals("Result Size", (1 + dirs.length + ioFiles.length + outputFiles.length), results.size());
        assertTrue("Start Dir", results.contains(javaDir));
        checkContainsFiles("Dir", dirs, results);
        checkContainsFiles("IO File", ioFiles, results);
        checkContainsFiles("Output File", outputFiles, results);
    }

    /**
     * Test Filtering and limit to depth 0
     */
    public void testFilterAndLimitA() {
        List results = new TestFileFinder(NOT_SVN, 0).find(javaDir);
        assertEquals("[A] Result Size", 1, results.size());
        assertTrue("[A] Start Dir",   results.contains(javaDir));
    }

    /**
     * Test Filtering and limit to depth 1
     */
    public void testFilterAndLimitB() {
        List results = new TestFileFinder(NOT_SVN, 1).find(javaDir);
        assertEquals("[B] Result Size", 2, results.size());
        assertTrue("[B] Start Dir",   results.contains(javaDir));
        assertTrue("[B] Org Dir",     results.contains(orgDir));
    }

    /**
     * Test Filtering and limit to depth 3
     */
    public void testFilterAndLimitC() {
        List results = new TestFileFinder(NOT_SVN, 3).find(javaDir);
        assertEquals("[A] Result Size", 4, results.size());
        assertTrue("[A] Start Dir",   results.contains(javaDir));
        assertTrue("[A] Org Dir",     results.contains(orgDir));
        assertTrue("[A] Apache Dir",  results.contains(apacheDir));
        assertTrue("[A] Commons Dir", results.contains(commonsDir));
    }

    /**
     * Test Filtering and limit to depth 5
     */
    public void testFilterAndLimitD() {
        List results = new TestFileFinder(dirsAndFilesFilter, 5).find(javaDir);
        assertEquals("[D] Result Size", (1 + dirs.length + ioFiles.length), results.size());
        assertTrue("[D] Start Dir", results.contains(javaDir));
        checkContainsFiles("[D] Dir", dirs, results);
        checkContainsFiles("[D] File", ioFiles, results);
    }

    /**
     * Test Limiting to current directory
     */
    public void testLimitToCurrent() {
        List results = new TestFileFinder(null, 0).find(current);
        assertEquals("Result Size", 1, results.size());
        assertTrue("Current Dir", results.contains(new File(".")));
    }

    /**
     * test an invalid start directory
     */
    public void testMissingStartDirectory() {

        // TODO is this what we want with invalid directory?
        File invalidDir = new File("invalid-dir");
        List results = new TestFileFinder(null, -1).find(invalidDir);
        assertEquals("Result Size", 1, results.size());
        assertTrue("Current Dir", results.contains(invalidDir));
 
        // TODO is this what we want with Null directory?
        try {
            new TestFileFinder(null, -1).find(null);
            fail("Null start directory didn't throw Exception");
        } catch (NullPointerException ignore) {
            // expected result
        }
    }

    /**
     * test an invalid start directory
     */
    public void testHandleStartDirectoryFalse() {

        List results = new TestFalseFileFinder(null, -1).find(current);
        assertEquals("Result Size", 0, results.size());

    }

    // ------------ Convenience Test Methods ------------------------------------

    /**
     * Check the files in the array are in the results list.
     */
    private void checkContainsFiles(String prefix, File[] files, Collection results) {
        for (int i = 0; i < files.length; i++) {
            assertTrue(prefix + "["+i+"] " + files[i], results.contains(files[i]));
        }
    }

    /**
     * Create an name filter containg the names of the files
     * in the array.
     */
    private static IOFileFilter createNameFilter(File[] files) {
        String[] names = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
        }
        return new NameFileFilter(names);
    }

    // ------------ Test DirectoryWalker implementation --------------------------

    /**
     * Test DirectoryWalker implementation that finds files in a directory hierarchy
     * applying a file filter.
     */
    private static class TestFileFinder extends DirectoryWalker {

        protected TestFileFinder(FileFilter filter, int depthLimit) {
            super(filter, depthLimit);
        }

        /** find files. */
        protected List find(File startDirectory) {
           List results = new ArrayList();
           walk(startDirectory, results);
           return results;
        }

        /** Handles a directory end by adding the File to the result set. */
        protected void handleDirectoryEnd(File directory, int depth, Collection results) {
            results.add(directory);
        }

        /** Handles a file by adding the File to the result set. */
        protected void handleFile(File file, int depth, Collection results) {
            results.add(file);
        }
    }

    // ------------ Test DirectoryWalker implementation --------------------------

    /**
     * Test DirectoryWalker implementation that always returns false
     * from handleDirectoryStart()
     */
    private static class TestFalseFileFinder extends TestFileFinder {

        protected TestFalseFileFinder(FileFilter filter, int depthLimit) {
            super(filter, depthLimit);
        }

        /** Always returns false. */
        protected boolean handleDirectoryStart(File directory, int depth, Collection results) {
            return false;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12813.java