error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11012.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11012.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11012.java
text:
```scala
T@@hread.sleep(5000);

/*
 * Copyright  2001-2004 The Apache Software Foundation
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

import java.io.*;

import junit.framework.AssertionFailedError;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

/**
 */
public class FixCrLfTest extends BuildFileTest {

    public FixCrLfTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/fixcrlf/build.xml");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void test1() throws IOException {
        executeTarget("test1");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk1.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk1.java"));
    }

    public void test2() throws IOException {
        executeTarget("test2");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk2.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk2.java"));
    }

    public void test3() throws IOException {
        executeTarget("test3");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk3.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk3.java"));
    }

    public void test4() throws IOException {
        executeTarget("test4");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk4.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk4.java"));
    }

    public void test5() throws IOException {
        executeTarget("test5");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk5.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk5.java"));
    }

    public void test6() throws IOException {
        executeTarget("test6");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk6.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk6.java"));
    }

    public void test7() throws IOException {
        executeTarget("test7");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk7.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk7.java"));
    }

    public void test8() throws IOException {
        executeTarget("test8");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk8.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk8.java"));
    }

    public void test9() throws IOException {
        executeTarget("test9");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Junk9.java"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk9.java"));
    }

    public void testMacLines() throws IOException {
        executeTarget("testMacLines");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/Mac2Unix"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Mac2Unix"));
    }

    public void testNoOverwrite() throws IOException {
        executeTarget("test1");
        File result =
            new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk1.java");
        long modTime = result.lastModified();

        /*
         * Sleep for some time to make sure a newer file would get a
         * more recent timestamp according to the file system's
         * granularity (should be > 2s to account for Windows FAT).
         */
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException ie) {
            fail(ie.getMessage());
        } // end of try-catch

        /*
         * make sure we get a new Project instance or the target won't get run
         * a second time.
         */
        configureProject("src/etc/testcases/taskdefs/fixcrlf/build.xml");

        executeTarget("test1");
        result =
            new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/Junk1.java");
        assertEquals(modTime, result.lastModified());
    }

    public void testEncoding() throws IOException {
        if (JavaEnvUtils.isJavaVersion(JavaEnvUtils.JAVA_1_1)) {
            // UTF16 is not supported in JDK 1.1
            return;
        }
        executeTarget("testEncoding");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/input.lf.utf16"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/input.crlf.utf16"));
    }

    public void testLongLines() throws IOException {
        executeTarget("testLongLines");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/longlines.lf"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/longlines.crlf"));
    }

    public void testCrCrLfSequenceUnix() throws IOException {
        executeTarget("testCrCrLfSequence-unix");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/crcrlf.unix"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/crcrlf"));
    }

    public void testCrCrLfSequenceDos() throws IOException {
        executeTarget("testCrCrLfSequence-dos");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/crcrlf.dos"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/crcrlf"));
    }

    public void testCrCrLfSequenceMac() throws IOException {
        executeTarget("testCrCrLfSequence-mac");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/crcrlf.mac"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/crcrlf"));
    }

    public void testFixlastDos() throws IOException {
        executeTarget("testFixlastDos");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/fixlast.dos"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/fixlastfalse.lf"));
    }

    public void testFixlastFalseMac() throws IOException {
        executeTarget("testFixlastFalseMac");
        assertEqualContent(new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/expected/fixlastfalse.mac"),
                           new File(System.getProperty("root"), "src/etc/testcases/taskdefs/fixcrlf/result/fixlastfalse.lf"));
    }

    /**
     * Bugzilla Report 20840
     *
     * Will fail with an exception if the parent directories do not
     * get created.
     */
    public void testCreateParentDirs() {
        executeTarget("createParentDirs");
    }

    public void assertEqualContent(File expect, File result)
        throws AssertionFailedError, IOException {
        if (!result.exists()) {
            fail("Expected file "+result+" doesn\'t exist");
        }

        InputStream inExpect = null;
        InputStream inResult = null;
        try {
            inExpect = new BufferedInputStream(new FileInputStream(expect));
            inResult = new BufferedInputStream(new FileInputStream(result));

            int expectedByte = inExpect.read();
            while (expectedByte != -1) {
                assertEquals(expectedByte, inResult.read());
                expectedByte = inExpect.read();
            }
            assertEquals("End of file", -1, inResult.read());
        } finally {
            if (inResult != null) {
                inResult.close();
            }
            if (inExpect != null) {
                inExpect.close();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11012.java