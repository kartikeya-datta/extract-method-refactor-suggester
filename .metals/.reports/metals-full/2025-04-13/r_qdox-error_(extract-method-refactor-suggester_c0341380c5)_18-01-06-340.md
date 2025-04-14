error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7479.java
text:
```scala
F@@ile reportFile = new File(System.getProperty("root"), "src/etc/testcases/taskdefs/optional/junitreport/test/html# $%\u00A7&-!report/index.html");

/*
 * Copyright  2002,2005-2006 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.FileUtils;

/**
 * Small testcase for the junitreporttask.
 * First test added to reproduce an fault, still a lot to improve
 *
 */
public class JUnitReportTest extends BuildFileTest {

    public JUnitReportTest(String name){
        super(name);
    }

    protected void setUp() {
        configureProject("src/etc/testcases/taskdefs/optional/junitreport.xml");
    }

    protected void tearDown() {
        executeTarget("clean");
    }

    /**
     * Verifies that no empty junit-noframes.html is generated when frames
     * output is selected via the default.
     * Needs reports1 task from junitreport.xml.
     */
    public void testNoFileJUnitNoFrames() {
        executeTarget("reports1");
        if (new File(System.getProperty("root"), "src/etc/testcases/taskdefs/optional/junitreport/test/html/junit-noframes.html").exists())
        {
            fail("No file junit-noframes.html expected");
        }
    }

    public void assertIndexCreated() {
        if (!new File(System.getProperty("root"),
                "src/etc/testcases/taskdefs/optional/junitreport/test/html/index.html").exists()) {
            fail("No file index file found");
        }

    }

    /**
     * run a target, assert the index file is there, look for text in the log
     * @param targetName target
     * @param text optional text to look for
     */
    private void expectReportWithText(String targetName, String text) {
        executeTarget(targetName);
        assertIndexCreated();
        if(text!=null) {
            assertLogContaining(text);
        }
    }


    public void testEmptyFile() throws Exception {
        expectReportWithText("testEmptyFile",
                XMLResultAggregator.WARNING_EMPTY_FILE);
    }

    public void testIncompleteFile() throws Exception {
        expectReportWithText("testIncompleteFile",
                XMLResultAggregator.WARNING_IS_POSSIBLY_CORRUPTED);
    }
    public void testWrongElement() throws Exception {
        expectReportWithText("testWrongElement",
                XMLResultAggregator.WARNING_INVALID_ROOT_ELEMENT);
    }

    // Bugzilla Report 34963
    public void testStackTraceLineBreaks() throws Exception {
        expectReportWithText("testStackTraceLineBreaks", null);
        FileReader r = null;
        try {
            r = new FileReader(new File(System.getProperty("root"),
                                        "src/etc/testcases/taskdefs/optional/junitreport/test/html/sampleproject/coins/0_CoinTest.html"));
            String report = FileUtils.readFully(r);
            assertTrue("output must contain <br>",
                       report.indexOf("junit.framework.AssertionFailedError: DOEG<br/>")
                   > -1);
        } finally {
            FileUtils.close(r);
        }
    }


    // Bugzilla Report 38477
    public void testSpecialSignsInSrcPath() throws Exception {
        executeTarget("testSpecialSignsInSrcPath");
        File reportFile = new File(System.getProperty("root"), "src/etc/testcases/taskdefs/optional/junitreport/test/html/index.html");
        // tests one the file object
        assertTrue("No index.html present. Not generated?", reportFile.exists() );
        assertTrue("Cant read the report file.", reportFile.canRead() );
        assertTrue("File shouldnt be empty.", reportFile.length() > 0 );
        // conversion to URL via FileUtils like in XMLResultAggregator, not as suggested in the bug report
        URL reportUrl = new URL( FileUtils.getFileUtils().toURI(reportFile.getAbsolutePath()) );
        InputStream reportStream = reportUrl.openStream();
        assertTrue("This shouldnt be an empty stream.", reportStream.available() > 0);
    }
    public void testSpecialSignsInHtmlPath() throws Exception {
        executeTarget("testSpecialSignsInHtmlPath");
        File reportFile = new File(System.getProperty("root"), "src/etc/testcases/taskdefs/optional/junitreport/test/html# $%§&-!report/index.html");
        // tests one the file object
        assertTrue("No index.html present. Not generated?", reportFile.exists() );
        assertTrue("Cant read the report file.", reportFile.canRead() );
        assertTrue("File shouldnt be empty.", reportFile.length() > 0 );
        // conversion to URL via FileUtils like in XMLResultAggregator, not as suggested in the bug report
        URL reportUrl = new URL( FileUtils.getFileUtils().toURI(reportFile.getAbsolutePath()) );
        InputStream reportStream = reportUrl.openStream();
        assertTrue("This shouldnt be an empty stream.", reportStream.available() > 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7479.java