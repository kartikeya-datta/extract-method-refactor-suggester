error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13749.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13749.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13749.java
text:
```scala
F@@ileUtils.close(r);

/*
 * Copyright  2002-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * A test class for the 'concat' task, used to concatenate a series of
 * files into a single stream.
 *
 */
public class ConcatTest
    extends BuildFileTest {

    /**
     * The name of the temporary file.
     */
    private static final String tempFile = "concat.tmp";

    /**
     * The name of the temporary file.
     */
    private static final String tempFile2 = "concat.tmp.2";

    /** Utilities used for file operations */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * Required constructor.
     */
    public ConcatTest(String name) {
        super(name);
    }

    /**
     * Test set up, called by the unit test framework prior to each
     * test.
     */
    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/concat.xml");
    }

    /**
     * Test tear down, called by the unit test framework prior to each
     * test.
     */
    public void tearDown() {
        executeTarget("cleanup");
    }

    /**
     * Expect an exception when insufficient information is provided.
     */
    public void test1() {
        expectBuildException("test1", "Insufficient information.");
    }

    /**
     * Expect an exception when the destination file is invalid.
     */
    public void test2() {
        expectBuildException("test2", "Invalid destination file.");
    }

    /**
     * Cats the string 'Hello, World!' to a temporary file.
     */
    public void test3() {

        File file = new File(getProjectDir(), tempFile);
        if (file.exists()) {
            file.delete();
        }

        executeTarget("test3");

        assertTrue(file.exists());
    }

    /**
     * Cats the file created in test3 three times.
     */
    public void test4() {
        test3();

        File file = new File(getProjectDir(), tempFile);
        final long origSize = file.length();

        executeTarget("test4");

        File file2 = new File(getProjectDir(), tempFile2);
        final long newSize = file2.length();

        assertEquals(origSize * 3, newSize);
    }

    /**
     * Cats the string 'Hello, World!' to the console.
     */
    public void test5() {
        expectLog("test5", "Hello, World!");
    }

    public void test6() {
        String filename = "src/etc/testcases/taskdefs/thisfiledoesnotexist"
            .replace('/', File.separatorChar);
        expectLogContaining("test6", filename +" does not exist.");
    }

    public void testConcatNoNewline() {
        expectLog("testConcatNoNewline", "ab");
    }

    public void testConcatNoNewlineEncoding() {
        expectLog("testConcatNoNewlineEncoding", "ab");
    }

    public void testPath() {
        test3();

        File file = new File(getProjectDir(), tempFile);
        final long origSize = file.length();

        executeTarget("testPath");

        File file2 = new File(getProjectDir(), tempFile2);
        final long newSize = file2.length();

        assertEquals(origSize, newSize);

    }
    public void testAppend() {
        test3();

        File file = new File(getProjectDir(), tempFile);
        final long origSize = file.length();

        executeTarget("testAppend");

        File file2 = new File(getProjectDir(), tempFile2);
        final long newSize = file2.length();

        assertEquals(origSize*2, newSize);

    }

    public void testFilter() {
        executeTarget("testfilter");
        assertTrue(getLog().indexOf("REPLACED") > -1);
    }

    public void testNoOverwrite() {
        executeTarget("testnooverwrite");
        File file2 = new File(getProjectDir(), tempFile2);
        long size = file2.length();
        assertEquals(size, 0);
    }

    public void testheaderfooter() {
        test3();
        expectLog("testheaderfooter", "headerHello, World!footer");
    }

    public void testfileheader() {
        test3();
        expectLog("testfileheader", "Hello, World!Hello, World!");
    }

    /**
     * Expect an exception when attempting to cat an file to itself
     */
    public void testsame() {
        expectBuildException("samefile", "output file same as input");
    }

    /**
     * Check if filter inline works
     */
    public void testfilterinline() {
        executeTarget("testfilterinline");
        assertTrue(getLog().indexOf("REPLACED") > -1);
    }

    /**
     * Check if multireader works
     */
    public void testmultireader() {
        executeTarget("testmultireader");
        assertTrue(getLog().indexOf("Bye") > -1);
        assertTrue(getLog().indexOf("Hello") == -1);
    }
    /**
     * Check if fixlastline works
     */
    public void testfixlastline()
        throws IOException
    {
        expectFileContains(
            "testfixlastline", "concat.line4",
            "end of line" + System.getProperty("line.separator")
            + "This has");
    }

    /**
     * Check if fixlastline works with eol
     */
    public void testfixlastlineeol()
        throws IOException
    {
        expectFileContains(
            "testfixlastlineeol", "concat.linecr",
            "end of line\rThis has");
    }

    // ------------------------------------------------------
    //   Helper methods - should be in BuildFileTest
    // -----------------------------------------------------

    private String getFileString(String filename)
        throws IOException
    {
        Reader r = null;
        try {
            r = new FileReader(getProject().resolveFile(filename));
            return  FileUtils.readFully(r);
        }
        finally {
            try {r.close();} catch (Throwable ignore) {}
        }

    }

    private String getFileString(String target, String filename)
        throws IOException
    {
        executeTarget(target);
        return getFileString(filename);
    }

    private void expectFileContains(
        String target, String filename, String contains)
        throws IOException
    {
        String content = getFileString(target, filename);
        assertTrue(
            "expecting file " + filename + " to contain " +
            contains +
            " but got " + content, content.indexOf(contains) > -1);
    }

    public void testTranscoding() throws IOException {
        executeTarget("testTranscoding");
        File f1 = getProject().resolveFile("copy/expected/utf-8");
        File f2 = getProject().resolveFile("concat.utf8");
        assertTrue(FILE_UTILS.contentEquals(f1, f2));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13749.java