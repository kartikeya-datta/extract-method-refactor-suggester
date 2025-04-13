error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15612.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15612.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15612.java
text:
```scala
a@@ssertFileContains("out/out/one.txt",      "filedir ='.'");

/*
 * Copyright  2003-2006 The Apache Software Foundation
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


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.taskdefs.optional.XsltTest;
import org.apache.tools.ant.util.FileUtils;


/**
 * TestCases for {@link XSLTProcess} task.
 * XXX merge with {@link XsltTest}?
 * @version 2003-08-05
 */
public class StyleTest extends BuildFileTest {

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    public StyleTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        configureProject("src/etc/testcases/taskdefs/style/build.xml");
        //executeTarget("setup");
        //commented out for performance while target is empty
    }

    protected void tearDown() throws Exception {
        executeTarget("teardown");
    }

    public void testStyleIsSet() throws Exception {
        expectSpecificBuildException("testStyleIsSet",
                "no stylesheet specified", "specify the " +
                "stylesheet either as a filename in style " +
                "attribute or as a nested resource");
    }

    public void testTransferParameterSet() throws Exception {
        expectFileContains("testTransferParameterSet",  // target
                           "out/out.xml",               // file
                           "set='myvalue'");            // exptected string
    }

    public void testTransferParameterEmpty() throws Exception {
        expectFileContains("testTransferParameterEmpty",
                           "out/out.xml",
                           "empty=''");
    }

    public void testTransferParameterUnset() throws Exception {
        expectFileContains("testTransferParameterUnset",
                           "out/out.xml",
                           "undefined='${value}'");
    }

    public void testTransferParameterUnsetWithIf() throws Exception {
        expectFileContains("testTransferParameterUnsetWithIf",
                           "out/out.xml",
                           "undefined='undefined default value'");
    }

    public void testNewerStylesheet() throws Exception {
        expectFileContains("testNewerStylesheet",
                           "out/out.xml",
                           "new-value");
    }

    public void testDefaultMapper() throws Exception {
        testDefaultMapper("testDefaultMapper");
    }

    public void testExplicitFileset() throws Exception {
        testDefaultMapper("testExplicitFileset");
    }

    public void testDefaultMapper(String target) throws Exception {
        assertTrue(!(FileUtils.getFileUtils().resolveFile(
                getProject().getBaseDir(),"out/data.html")).exists());
        expectFileContains(target,
                           "out/data.html",
                           "set='myvalue'");
    }

    public void testCustomMapper() throws Exception {
        assertTrue(!FILE_UTILS.resolveFile(
                getProject().getBaseDir(), "out/out.xml").exists());
        expectFileContains("testCustomMapper",
                           "out/out.xml",
                           "set='myvalue'");
    }

    public void testTypedMapper() throws Exception {
        assertTrue(!FILE_UTILS.resolveFile(
                getProject().getBaseDir(), "out/out.xml").exists());
        expectFileContains("testTypedMapper",
                           "out/out.xml",
                           "set='myvalue'");
    }

    public void testDirectoryHierarchyWithDirMatching() throws Exception {
        executeTarget("testDirectoryHierarchyWithDirMatching");
        assertTrue(FILE_UTILS.resolveFile(
                getProject().getBaseDir(), "out/dest/level1/data.html")
                   .exists());
    }

    public void testDirsWithSpaces() throws Exception {
        executeTarget("testDirsWithSpaces");
        assertTrue(FILE_UTILS.resolveFile(
                getProject().getBaseDir(), "out/d est/data.html")
                   .exists());
    }

    public void testWithStyleAttrAndResource() throws Exception {
        expectSpecificBuildException("testWithStyleAttrAndResource",
                "Must throws a BuildException", "specify the " +
                "stylesheet either as a filename in style " +
                "attribute or as a nested resource but not " +
                "as both");
    }

    public void testWithFileResource() throws Exception {
        expectFileContains("testWithFileResource", "out/out.xml", "set='value'");
    }

    public void testWithUrlResource() throws Exception {
        expectFileContains("testWithUrlResource", "out/out.xml", "set='value'");
    }

    public void testFilenameAsParam() throws Exception {
        executeTarget("testFilenameAsParam");
        assertFileContains("out/out/one.txt",      "filename='one.xml'");
        assertFileContains("out/out/two.txt",      "filename='two.xml'");
        assertFileContains("out/out/three.txt",    "filename='three.xml'");
        assertFileContains("out/out/dir/four.txt", "filename='four.xml'");
        assertFileContains("out/out/dir/four.txt", "filedir ='-not-set-'");
    }

    public void testFilenameAsParamNoSetting() throws Exception {
        executeTarget("testFilenameAsParamNoSetting");
        assertFileContains("out/out/one.txt",      "filename='-not-set-'");
        assertFileContains("out/out/two.txt",      "filename='-not-set-'");
        assertFileContains("out/out/three.txt",    "filename='-not-set-'");
        assertFileContains("out/out/dir/four.txt", "filename='-not-set-'");
    }

    public void testFilenameAndFiledirAsParam() throws Exception {
        executeTarget("testFilenameAndFiledirAsParam");
        assertFileContains("out/out/one.txt",      "filename='one.xml'");
        assertFileContains("out/out/one.txt",      "filedir =''");
        assertFileContains("out/out/dir/four.txt", "filename='four.xml'");
        assertFileContains("out/out/dir/four.txt", "filedir ='dir'");
    }


    // *************  copied from ConcatTest  *************

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
            FileUtils.close(r);
        }
    }

    private void expectFileContains(
        String target, String filename, String contains)
        throws IOException
    {
        executeTarget(target);
        assertFileContains(filename, contains);
    }

    private void assertFileContains(String filename, String contains) throws IOException {
        String content = getFileString(filename);
        assertTrue(
              "expecting file " + filename
            + " to contain " + contains
            + " but got " + content,
            content.indexOf(contains) > -1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15612.java