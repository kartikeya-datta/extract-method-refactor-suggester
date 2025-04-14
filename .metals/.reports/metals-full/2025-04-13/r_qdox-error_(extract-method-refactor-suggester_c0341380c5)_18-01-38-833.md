error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8987.java
text:
```scala
public v@@oid testMultilineAssertsFork() {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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
package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.JavaEnvUtils;

public class JUnitTaskTest extends BuildFileTest {

    /**
     * Constructor for the JUnitTaskTest object.
     */
    public JUnitTaskTest(String name) {
        super(name);
    }

    /**
     * The JUnit setup method.
     */
    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/optional/junit.xml");
    }

    /**
     * The teardown method for JUnit.
     */
    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testCrash() {
       expectPropertySet("crash", "crashed");
    }

    public void testNoCrash() {
       expectPropertyUnset("nocrash", "crashed");
    }

    public void testTimeout() {
       expectPropertySet("timeout", "timeout");
    }

    public void testNoTimeout() {
       expectPropertyUnset("notimeout", "timeout");
    }

    public void testNonForkedCapture() throws IOException {
        executeTarget("capture");
        assertNoPrint(getLog(), "log");
        assertNoPrint(getFullLog(), "debug log");
    }

    public void testForkedCapture() throws IOException {
        getProject().setProperty("fork", "true");
        testNonForkedCapture();
        // those would fail because of the way BuildFileTest captures output
        assertNoPrint(getOutput(), "output");
        assertNoPrint(getError(), "error output");
        assertOutput();
    }

    public void testBatchTestForkOnceToDir() {
        assertResultFilesExist("testBatchTestForkOnceToDir", ".xml");
    }

    /** Bugzilla Report 32973 */
    public void testBatchTestForkOnceExtension() {
        assertResultFilesExist("testBatchTestForkOnceExtension", ".foo");
    }


    /* Bugzilla Report 42984 */
    //TODO This scenario works from command line, but not from JUnit ...
    //     Running these steps from the junit.xml-directory work
    //     $ ant -f junit.xml failureRecorder.prepare
    //     $ ant -f junit.xml failureRecorder.runtest
    //     $ ant -f junit.xml failureRecorder.runtest
    //     $ ant -f junit.xml failureRecorder.fixing
    //     $ ant -f junit.xml failureRecorder.runtest
    //     $ ant -f junit.xml failureRecorder.runtest
    //     But running the JUnit testcase fails in 4th run.
    public void testFailureRecorder() {
        if (JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.JAVA_1_5)) {
            try {
                Class.forName("junit.framework.JUnit4TestAdapter");
                System.err.println("skipping tests since it fails when"
                                   + " using JUnit 4");
                return;
            } catch (ClassNotFoundException e) {
                // OK, this is JUnit3, can run test
            }
        }

        try {
            File testDir = new File(getProjectDir(), "out");
            File collectorFile = new File(getProjectDir(),
                                          "out/FailedTests.java");
        
            // ensure that there is a clean test environment
            assertFalse("Test directory '" + testDir.getAbsolutePath()
                        + "' must not exist before the test preparation.", 
                        testDir.exists());
            assertFalse("The collector file '"
                        + collectorFile.getAbsolutePath()
                        + "'must not exist before the test preparation.", 
                        collectorFile.exists());

        
            // prepare the test environment
            executeTarget("failureRecorder.prepare");
            assertTrue("Test directory '" + testDir.getAbsolutePath()
                       + "' was not created.", testDir.exists());
            assertTrue("There should be one class.",
                       (new File(testDir, "A.class")).exists());
            assertFalse("The collector file '"
                        + collectorFile.getAbsolutePath() 
                        + "' should not exist before the 1st run.",
                        collectorFile.exists());
        
        
            // 1st junit run: should do all tests - failing and not failing tests
            executeTarget("failureRecorder.runtest");
            assertTrue("The collector file '" + collectorFile.getAbsolutePath() 
                       + "' should exist after the 1st run.",
                       collectorFile.exists());
            // the passing test cases
            assertOutputContaining("1st run: should run A.test01", "A.test01");
            assertOutputContaining("1st run: should run B.test05", "B.test05");
            assertOutputContaining("1st run: should run B.test06", "B.test06");
            assertOutputContaining("1st run: should run C.test07", "C.test07");
            assertOutputContaining("1st run: should run C.test08", "C.test08");
            assertOutputContaining("1st run: should run C.test09", "C.test09");
            // the failing test cases
            assertOutputContaining("1st run: should run A.test02", "A.test02");
            assertOutputContaining("1st run: should run A.test03", "A.test03");
            assertOutputContaining("1st run: should run B.test04", "B.test04");
            assertOutputContaining("1st run: should run D.test10", "D.test10");

        
            // 2nd junit run: should do only failing tests
            executeTarget("failureRecorder.runtest");
            assertTrue("The collector file '" + collectorFile.getAbsolutePath() 
                       + "' should exist after the 2nd run.",
                       collectorFile.exists());
            // the passing test cases
            assertOutputNotContaining("2nd run: should not run A.test01",
                                      "A.test01");
            assertOutputNotContaining("2nd run: should not run A.test05",
                                      "B.test05");
            assertOutputNotContaining("2nd run: should not run B.test06",
                                      "B.test06");
            assertOutputNotContaining("2nd run: should not run C.test07",
                                      "C.test07");
            assertOutputNotContaining("2nd run: should not run C.test08",
                                      "C.test08");
            assertOutputNotContaining("2nd run: should not run C.test09",
                                      "C.test09");
            // the failing test cases
            assertOutputContaining("2nd run: should run A.test02", "A.test02");
            assertOutputContaining("2nd run: should run A.test03", "A.test03");
            assertOutputContaining("2nd run: should run B.test04", "B.test04");
            assertOutputContaining("2nd run: should run D.test10", "D.test10");
        
        
            // "fix" errors in class A
            executeTarget("failureRecorder.fixing");
        
            // 3rd run: four running tests with two errors
            executeTarget("failureRecorder.runtest");
            assertTrue("The collector file '" + collectorFile.getAbsolutePath() 
                       + "' should exist after the 3rd run.",
                       collectorFile.exists());
            assertOutputContaining("3rd run: should run A.test02", "A.test02");
            assertOutputContaining("3rd run: should run A.test03", "A.test03");
            assertOutputContaining("3rd run: should run B.test04", "B.test04");
            assertOutputContaining("3rd run: should run D.test10", "D.test10");
        
        
            // 4rd run: two running tests with errors
            executeTarget("failureRecorder.runtest");
            assertTrue("The collector file '" + collectorFile.getAbsolutePath() 
                       + "' should exist after the 4th run.",
                       collectorFile.exists());
            //TODO: these two statements fail
            //assertOutputNotContaining("4th run: should not run A.test02", "A.test02");
            //assertOutputNotContaining("4th run: should not run A.test03", "A.test03");
            assertOutputContaining("4th run: should run B.test04", "B.test04");
            assertOutputContaining("4th run: should run D.test10", "D.test10");
        } catch (BuildException be) {
            be.printStackTrace();
            System.err.println("nested build's log: " + getLog());
            System.err.println("nested build's System.out: " + getOutput());
            System.err.println("nested build's System.err: " + getError());
            fail("Ant execution failed: " + be.getMessage());
        }
    }

    public void testBatchTestForkOnceCustomFormatter() {
        assertResultFilesExist("testBatchTestForkOnceCustomFormatter", "foo");
    }

    // Bugzilla Issue 45411
    public void testMultilineAssertsNoFork() {
        expectLogNotContaining("testMultilineAssertsNoFork", "messed up)");
        assertLogNotContaining("crashed)");
    }

    // Bugzilla Issue 45411
    public void XtestMultilineAssertsFork() {
        expectLogNotContaining("testMultilineAssertsFork", "messed up)");
        assertLogNotContaining("crashed)");
    }

    private void assertResultFilesExist(String target, String extension) {
        executeTarget(target);
        assertResultFileExists("JUnitClassLoader", extension);
        assertResultFileExists("JUnitTestRunner", extension);
        assertResultFileExists("JUnitVersionHelper", extension);
    }

    private void assertResultFileExists(String classNameFragment, String ext) {
        assertTrue("result for " + classNameFragment + "Test" + ext + " exists",
                   getProject().resolveFile("out/TEST-org.apache.tools.ant."
                                            + "taskdefs.optional.junit."
                                            + classNameFragment + "Test" + ext)
                   .exists());
    }

    private void assertNoPrint(String result, String where) {
        assertTrue(where + " '" + result + "' must not contain print statement",
                   result.indexOf("print to System.") == -1);
    }

    private void assertOutput() throws IOException {
        FileReader inner = new FileReader(getProject()
                                          .resolveFile("testlog.txt"));
        BufferedReader reader = new BufferedReader(inner);
        try {
            String line = reader.readLine();
            assertEquals("Testsuite: org.apache.tools.ant.taskdefs.optional.junit.Printer",
                         line);
            line = reader.readLine();
            assertNotNull(line);
            assertTrue(line.startsWith("Tests run: 1, Failures: 0, Errors: 0, Time elapsed:"));
            line = reader.readLine();
            assertEquals("------------- Standard Output ---------------",
                         line);
            assertPrint(reader.readLine(), "static", "out");
            assertPrint(reader.readLine(), "constructor", "out");
            assertPrint(reader.readLine(), "method", "out");
            line = reader.readLine();
            assertEquals("------------- ---------------- ---------------",
                         line);
            line = reader.readLine();
            assertEquals("------------- Standard Error -----------------",
                         line);
            assertPrint(reader.readLine(), "static", "err");
            assertPrint(reader.readLine(), "constructor", "err");
            assertPrint(reader.readLine(), "method", "err");
            line = reader.readLine();
            assertEquals("------------- ---------------- ---------------",
                         line);
            line = reader.readLine();
            assertEquals("", line);
            line = reader.readLine();
            assertNotNull(line);
            assertTrue(line.startsWith("Testcase: testNoCrash took "));
        } finally {
            inner.close();
        }
    }

    private void assertPrint(String line, String from, String to) {
        String search = from + " print to System." + to;
        assertEquals(search, line);
    }

}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8987.java