error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/359.java
text:
```scala
c@@ontent = FileUtils.readFully(rdr);

/*
 * Copyright  2003-2004 The Apache Software Foundation
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

package org.apache.tools.ant.filters;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.util.FileUtils;

/**
 * JUnit Testcases for ConcatReader
 */
public class ConcatFilterTest extends BuildFileTest {

    private static FileUtils fu = FileUtils.newFileUtils();
    private static final String lSep = 
        Os.isFamily("mac") ? "\r" : System.getProperty("line.separator");

    private static final String FILE_PREPEND_WITH =
          "this-should-be-the-first-line" + lSep
        + "Line  1" + lSep
        + "Line  2" + lSep
        + "Line  3" + lSep
        + "Line  4" + lSep
    ;

    private static final String FILE_PREPEND =
          "Line  1" + lSep
        + "Line  2" + lSep
        + "Line  3" + lSep
        + "Line  4" + lSep
        + "Line  5" + lSep
    ;

    private static final String FILE_APPEND_WITH =
          "Line 57" + lSep
        + "Line 58" + lSep
        + "Line 59" + lSep
        + "Line 60" + lSep
        + "this-should-be-the-last-line" + lSep
    ;

    private static final String FILE_APPEND =
          "Line 56" + lSep
        + "Line 57" + lSep
        + "Line 58" + lSep
        + "Line 59" + lSep
        + "Line 60" + lSep
    ;


    public ConcatFilterTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/filters/concat.xml");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void testFilterReaderNoArgs() throws IOException {
        executeTarget("testFilterReaderNoArgs");
        File expected = getProject().resolveFile("input/concatfilter.test");
        File result = getProject().resolveFile("result/concat.FilterReaderNoArgs.test");
        assertTrue("testFilterReaderNoArgs: Result not like expected", fu.contentEquals(expected, result));
    }

    public void testFilterReaderBefore() {
        doTest("testFilterReaderPrepend", FILE_PREPEND_WITH, FILE_APPEND);
    }

    public void testFilterReaderAfter() {
        doTest("testFilterReaderAppend", FILE_PREPEND, FILE_APPEND_WITH);
    }

    public void testFilterReaderBeforeAfter() {
        doTest("testFilterReaderPrependAppend", FILE_PREPEND_WITH, FILE_APPEND_WITH);
    }

    public void testConcatFilter() {
        doTest("testConcatFilter", FILE_PREPEND, FILE_APPEND);
    }

    public void testConcatFilterBefore() {
        doTest("testConcatFilterPrepend", FILE_PREPEND_WITH, FILE_APPEND);
    }

    public void testConcatFilterAfter() {
        doTest("testConcatFilterAppend", FILE_PREPEND, FILE_APPEND_WITH);
    }

    public void testConcatFilterBeforeAfter() {
        doTest("testConcatFilterPrependAppend", FILE_PREPEND_WITH, FILE_APPEND_WITH);
    }


    /**
     * Executes a target and checks the beginning and the ending of a file.
     * The filename depends on the target name: target name <i>testHelloWorld</i>
     * will search for a file <i>result/concat.HelloWorld.test</i>.
     * @param target The target to invoke
     * @param expectedStart The string which should be at the beginning of the file
     * @param expectedEnd The string which should be at the end of the file
     */
    protected void doTest(String target, String expectedStart, String expectedEnd) {
        executeTarget(target);
        String resultContent = read("result/concat." + target.substring(4) + ".test");
        assertTrue("First 5 lines differs.", resultContent.startsWith(expectedStart));
        assertTrue("Last 5 lines differs.", resultContent.endsWith(expectedEnd));
    }


    /**
     * Wrapper for FileUtils.readFully().
     * Additionally it resolves the filename according the the projects basedir
     * and closes the used reader.
     * @param filename The name of the file to read
     * @return the content of the file or <i>null</i> if something goes wrong
     */
    protected String read(String filename) {
        String content = null;
        try {
            File file = getProject().resolveFile(filename);
            java.io.FileReader rdr = new java.io.FileReader(file);
            content = fu.readFully(rdr);
            rdr.close();
            rdr = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/359.java