error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8972.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8972.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8972.java
text:
```scala
private static final F@@ile javaDir      = new File("src/main/java");

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

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
    private static final File filenameUtils = new File(ioDir, "FilenameUtils.java");
    private static final File ioUtils       = new File(ioDir, "IOUtils.java");
    private static final File proxyWriter   = new File(outputDir, "ProxyWriter.java");
    private static final File nullStream    = new File(outputDir, "NullOutputStream.java");
    private static final File[] ioFiles     = new File[] {filenameUtils, ioUtils};
    private static final File[] outputFiles = new File[] {proxyWriter, nullStream};
    
    // Filters
    private static final IOFileFilter dirsFilter        = createNameFilter(dirs);
    private static final IOFileFilter iofilesFilter     = createNameFilter(ioFiles);
    private static final IOFileFilter outputFilesFilter = createNameFilter(outputFiles);
    private static final IOFileFilter ioDirAndFilesFilter = new OrFileFilter(dirsFilter, iofilesFilter);
    private static final IOFileFilter dirsAndFilesFilter = new OrFileFilter(ioDirAndFilesFilter, outputFilesFilter);

    // Filter to exclude SVN files
    private static final IOFileFilter NOT_SVN = FileFilterUtils.makeSVNAware(null);

    /** Construct the TestCase using the name */
    public DirectoryWalkerTestCase(String name) {
        super(name);
    }

    /** Set Up */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** Tear Down */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //-----------------------------------------------------------------------

    /**
     * Test Filtering
     */
    public void testFilter() {
        List<File> results = new TestFileFinder(dirsAndFilesFilter, -1).find(javaDir);
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
        List<File> results = new TestFileFinder(NOT_SVN, 0).find(javaDir);
        assertEquals("[A] Result Size", 1, results.size());
        assertTrue("[A] Start Dir",   results.contains(javaDir));
    }

    /**
     * Test Filtering and limit to depth 1
     */
    public void testFilterAndLimitB() {
        List<File> results = new TestFileFinder(NOT_SVN, 1).find(javaDir);
        assertEquals("[B] Result Size", 2, results.size());
        assertTrue("[B] Start Dir",   results.contains(javaDir));
        assertTrue("[B] Org Dir",     results.contains(orgDir));
    }

    /**
     * Test Filtering and limit to depth 3
     */
    public void testFilterAndLimitC() {
        List<File> results = new TestFileFinder(NOT_SVN, 3).find(javaDir);
        assertEquals("[C] Result Size", 4, results.size());
        assertTrue("[C] Start Dir",   results.contains(javaDir));
        assertTrue("[C] Org Dir",     results.contains(orgDir));
        assertTrue("[C] Apache Dir",  results.contains(apacheDir));
        assertTrue("[C] Commons Dir", results.contains(commonsDir));
    }

    /**
     * Test Filtering and limit to depth 5
     */
    public void testFilterAndLimitD() {
        List<File> results = new TestFileFinder(dirsAndFilesFilter, 5).find(javaDir);
        assertEquals("[D] Result Size", (1 + dirs.length + ioFiles.length), results.size());
        assertTrue("[D] Start Dir", results.contains(javaDir));
        checkContainsFiles("[D] Dir", dirs, results);
        checkContainsFiles("[D] File", ioFiles, results);
    }

    /**
     * Test separate dir and file filters
     */
    public void testFilterDirAndFile1() {
        List<File> results = new TestFileFinder(dirsFilter, iofilesFilter, -1).find(javaDir);
        assertEquals("[DirAndFile1] Result Size", (1 + dirs.length + ioFiles.length), results.size());
        assertTrue("[DirAndFile1] Start Dir", results.contains(javaDir));
        checkContainsFiles("[DirAndFile1] Dir", dirs, results);
        checkContainsFiles("[DirAndFile1] File", ioFiles, results);
    }

    /**
     * Test separate dir and file filters
     */
    public void testFilterDirAndFile2() {
        List<File> results = new TestFileFinder((IOFileFilter) null, (IOFileFilter) null, -1).find(javaDir);
        assertTrue("[DirAndFile2] Result Size", results.size() > (1 + dirs.length + ioFiles.length));
        assertTrue("[DirAndFile2] Start Dir", results.contains(javaDir));
        checkContainsFiles("[DirAndFile2] Dir", dirs, results);
        checkContainsFiles("[DirAndFile2] File", ioFiles, results);
    }

    /**
     * Test separate dir and file filters
     */
    public void testFilterDirAndFile3() {
        List<File> results = new TestFileFinder(dirsFilter, (IOFileFilter) null, -1).find(javaDir);
        List<File> resultDirs = directoriesOnly(results);
        assertEquals("[DirAndFile3] Result Size", (1 + dirs.length), resultDirs.size());
        assertTrue("[DirAndFile3] Start Dir", results.contains(javaDir));
        checkContainsFiles("[DirAndFile3] Dir", dirs, resultDirs);
    }

    /**
     * Test separate dir and file filters
     */
    public void testFilterDirAndFile4() {
        List<File> results = new TestFileFinder((IOFileFilter) null, iofilesFilter, -1).find(javaDir);
        List<File> resultFiles = filesOnly(results);
        assertEquals("[DirAndFile4] Result Size", ioFiles.length, resultFiles.size());
        assertTrue("[DirAndFile4] Start Dir", results.contains(javaDir));
        checkContainsFiles("[DirAndFile4] File", ioFiles, resultFiles);
    }

    /**
     * Test Limiting to current directory
     */
    public void testLimitToCurrent() {
        List<File> results = new TestFileFinder(null, 0).find(current);
        assertEquals("Result Size", 1, results.size());
        assertTrue("Current Dir", results.contains(new File(".")));
    }

    /**
     * test an invalid start directory
     */
    public void testMissingStartDirectory() {

        // TODO is this what we want with invalid directory?
        File invalidDir = new File("invalid-dir");
        List<File> results = new TestFileFinder(null, -1).find(invalidDir);
        assertEquals("Result Size", 1, results.size());
        assertTrue("Current Dir", results.contains(invalidDir));
 
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

        List<File> results = new TestFalseFileFinder(null, -1).find(current);
        assertEquals("Result Size", 0, results.size());

    }

    // ------------ Convenience Test Methods ------------------------------------

    /**
     * Check the files in the array are in the results list.
     */
    private void checkContainsFiles(String prefix, File[] files, Collection<File> results) {
        for (int i = 0; i < files.length; i++) {
            assertTrue(prefix + "["+i+"] " + files[i], results.contains(files[i]));
        }
    }

    private void checkContainsString(String prefix, File[] files, Collection<String> results) {
        for (int i = 0; i < files.length; i++) {
            assertTrue(prefix + "["+i+"] " + files[i], results.contains(files[i].toString()));
        }
    }

    /**
     * Extract the directories.
     */
    private List<File> directoriesOnly(Collection<File> results) {
        List<File> list = new ArrayList<File>(results.size());
        for (Iterator<File> it = results.iterator(); it.hasNext(); ) {
            File file = it.next();
            if (file.isDirectory()) {
                list.add(file);
            }
        }
        return list;
    }

    /**
     * Extract the files.
     */
    private List<File> filesOnly(Collection<File> results) {
        List<File> list = new ArrayList<File>(results.size());
        for (Iterator<File> it = results.iterator(); it.hasNext(); ) {
            File file = it.next();
            if (file.isFile()) {
                list.add(file);
            }
        }
        return list;
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

    /**
     * Test Cancel
     */
    public void testCancel() {
        String cancelName = null;

        // Cancel on a file
        try {
            cancelName = "DirectoryWalker.java";
            new TestCancelWalker(cancelName, false).find(javaDir);
            fail("CancelException not thrown for '" + cancelName + "'");
        } catch (DirectoryWalker.CancelException cancel) {
            assertEquals("File:  " + cancelName,   cancelName, cancel.getFile().getName());
            assertEquals("Depth: " + cancelName,  5, cancel.getDepth());
        } catch(IOException ex) {
            fail("IOException: " + cancelName + " " + ex);
        }

        // Cancel on a directory
        try {
            cancelName = "commons";
            new TestCancelWalker(cancelName, false).find(javaDir);
            fail("CancelException not thrown for '" + cancelName + "'");
        } catch (DirectoryWalker.CancelException cancel) {
            assertEquals("File:  " + cancelName,   cancelName, cancel.getFile().getName());
            assertEquals("Depth: " + cancelName,  3, cancel.getDepth());
        } catch(IOException ex) {
            fail("IOException: " + cancelName + " " + ex);
        }

        // Suppress CancelException (use same file name as preceeding test)
        try {
            List<File> results = new TestCancelWalker(cancelName, true).find(javaDir);
            File lastFile = results.get(results.size() - 1);
            assertEquals("Suppress:  " + cancelName,   cancelName, lastFile.getName());
        } catch(IOException ex) {
            fail("Suppress threw " + ex);
        }

    }

    /**
     * Test Cancel
     */
    public void testMultiThreadCancel() {
        String cancelName = "DirectoryWalker.java";
        TestMultiThreadCancelWalker walker = new TestMultiThreadCancelWalker(cancelName, false);
        // Cancel on a file
        try {
            walker.find(javaDir);
            fail("CancelException not thrown for '" + cancelName + "'");
        } catch (DirectoryWalker.CancelException cancel) {
            File last = walker.results.get(walker.results.size() - 1);
            assertEquals(cancelName, last.getName());
            assertEquals("Depth: " + cancelName,  5, cancel.getDepth());
        } catch(IOException ex) {
            fail("IOException: " + cancelName + " " + ex);
        }
        
        // Cancel on a directory
        try {
            cancelName = "commons";
            walker = new TestMultiThreadCancelWalker(cancelName, false);
            walker.find(javaDir);
            fail("CancelException not thrown for '" + cancelName + "'");
        } catch (DirectoryWalker.CancelException cancel) {
            assertEquals("File:  " + cancelName,   cancelName, cancel.getFile().getName());
            assertEquals("Depth: " + cancelName,  3, cancel.getDepth());
        } catch(IOException ex) {
            fail("IOException: " + cancelName + " " + ex);
        }
        
        // Suppress CancelException (use same file name as preceeding test)
        try {
            walker = new TestMultiThreadCancelWalker(cancelName, true);
            List<File> results = walker.find(javaDir);
            File lastFile = results.get(results.size() - 1);
            assertEquals("Suppress:  " + cancelName, cancelName, lastFile.getName());
        } catch(IOException ex) {
            fail("Suppress threw " + ex);
        }

    }

    /**
     * Test Filtering
     */
    public void testFilterString() {
        List<String> results = new TestFileFinderString(dirsAndFilesFilter, -1).find(javaDir);
        assertEquals("Result Size", (outputFiles.length + ioFiles.length), results.size());
        checkContainsString("IO File", ioFiles, results);
        checkContainsString("Output File", outputFiles, results);
    }

    // ------------ Test DirectoryWalker implementation --------------------------

    /**
     * Test DirectoryWalker implementation that finds files in a directory hierarchy
     * applying a file filter.
     */
    private static class TestFileFinder extends DirectoryWalker<File> {

        protected TestFileFinder(FileFilter filter, int depthLimit) {
            super(filter, depthLimit);
        }

        protected TestFileFinder(IOFileFilter dirFilter, IOFileFilter fileFilter, int depthLimit) {
            super(dirFilter, fileFilter, depthLimit);
        }

        /** find files. */
        protected List<File> find(File startDirectory) {
           List<File> results = new ArrayList<File>();
           try {
               walk(startDirectory, results);
           } catch(IOException ex) {
               Assert.fail(ex.toString());
           }
           return results;
        }

        /** Handles a directory end by adding the File to the result set. */
        @Override
        protected void handleDirectoryEnd(File directory, int depth, Collection<File> results) {
            results.add(directory);
        }

        /** Handles a file by adding the File to the result set. */
        @Override
        protected void handleFile(File file, int depth, Collection<File> results) {
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
        @Override
        protected boolean handleDirectory(File directory, int depth, Collection<File> results) {
            return false;
        }
    }

    // ------------ Test DirectoryWalker implementation --------------------------

    /**
     * Test DirectoryWalker implementation that finds files in a directory hierarchy
     * applying a file filter.
     */
    static class TestCancelWalker extends DirectoryWalker<File> {
        private String cancelFileName;
        private boolean suppressCancel;

        TestCancelWalker(String cancelFileName,boolean suppressCancel) {
            super();
            this.cancelFileName = cancelFileName;
            this.suppressCancel = suppressCancel;
        }

        /** find files. */
        protected List<File> find(File startDirectory) throws IOException {
           List<File> results = new ArrayList<File>();
           walk(startDirectory, results);
           return results;
        }

        /** Handles a directory end by adding the File to the result set. */
        @Override
        protected void handleDirectoryEnd(File directory, int depth, Collection<File> results) throws IOException {
            results.add(directory);
            if (cancelFileName.equals(directory.getName())) {
                throw new CancelException(directory, depth);
            }
        }

        /** Handles a file by adding the File to the result set. */
        @Override
        protected void handleFile(File file, int depth, Collection<File> results) throws IOException {
            results.add(file);
            if (cancelFileName.equals(file.getName())) {
                throw new CancelException(file, depth);
            }
        }

        /** Handles Cancel. */
        @Override
        protected void handleCancelled(File startDirectory, Collection<File> results,
                       CancelException cancel) throws IOException {
            if (!suppressCancel) {
                super.handleCancelled(startDirectory, results, cancel);
            }
        }
    }

    /**
     * Test DirectoryWalker implementation that finds files in a directory hierarchy
     * applying a file filter.
     */
    static class TestMultiThreadCancelWalker extends DirectoryWalker<File> {
        private String cancelFileName;
        private boolean suppressCancel;
        private boolean cancelled;
        public List<File> results;

        TestMultiThreadCancelWalker(String cancelFileName, boolean suppressCancel) {
            super();
            this.cancelFileName = cancelFileName;
            this.suppressCancel = suppressCancel;
        }

        /** find files. */
        protected List<File> find(File startDirectory) throws IOException {
           results = new ArrayList<File>();
           walk(startDirectory, results);
           return results;
        }

        /** Handles a directory end by adding the File to the result set. */
        @Override
        protected void handleDirectoryEnd(File directory, int depth, Collection<File> results) throws IOException {
            results.add(directory);
            assertEquals(false, cancelled);
            if (cancelFileName.equals(directory.getName())) {
                cancelled = true;
            }
        }

        /** Handles a file by adding the File to the result set. */
        @Override
        protected void handleFile(File file, int depth, Collection<File> results) throws IOException {
            results.add(file);
            assertEquals(false, cancelled);
            if (cancelFileName.equals(file.getName())) {
                cancelled = true;
            }
        }

        /** Handles Cancelled. */
        @Override
        protected boolean handleIsCancelled(File file, int depth, Collection<File> results) throws IOException {
            return cancelled;
        }

        /** Handles Cancel. */
        @Override
        protected void handleCancelled(File startDirectory, Collection<File> results,
                       CancelException cancel) throws IOException {
            if (!suppressCancel) {
                super.handleCancelled(startDirectory, results, cancel);
            }
        }
    }

    /**
     * Test DirectoryWalker implementation that finds files in a directory hierarchy
     * applying a file filter.
     */
    private static class TestFileFinderString extends DirectoryWalker<String> {

        protected TestFileFinderString(FileFilter filter, int depthLimit) {
            super(filter, depthLimit);
        }

        /** find files. */
        protected List<String> find(File startDirectory) {
           List<String> results = new ArrayList<String>();
           try {
               walk(startDirectory, results);
           } catch(IOException ex) {
               Assert.fail(ex.toString());
           }
           return results;
        }

        /** Handles a file by adding the File to the result set. */
        @Override
        protected void handleFile(File file, int depth, Collection<String> results) {
            results.add(file.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8972.java