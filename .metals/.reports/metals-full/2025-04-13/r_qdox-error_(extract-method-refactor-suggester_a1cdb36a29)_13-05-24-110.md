error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1037.java
text:
```scala
S@@tring file1Content = FileUtils.readFully(new FileReader(file1));

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

package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.FileUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Tests FileSet using the Copy task.
 *
 */
public class CopyTest extends BuildFileTest {

    /** Utilities used for file operations */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    public CopyTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/copy.xml");
    }

    public void test1() {
        executeTarget("test1");
        File f = new File(getProjectDir(), "copytest1.tmp");
        if ( !f.exists()) {
            fail("Copy failed");
        }
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    public void test2() {
        executeTarget("test2");
        File f = new File(getProjectDir(), "copytest1dir/copy.xml");
        if ( !f.exists()) {
            fail("Copy failed");
        }
    }

    public void test3() {
        executeTarget("test3");
        File file3  = new File(getProjectDir(), "copytest3.tmp");
        assertTrue(file3.exists());
        File file3a = new File(getProjectDir(), "copytest3a.tmp");
        assertTrue(file3a.exists());
        File file3b = new File(getProjectDir(), "copytest3b.tmp");
        assertTrue(file3b.exists());
        File file3c = new File(getProjectDir(), "copytest3c.tmp");
        assertTrue(file3c.exists());

        //file length checks rely on touch generating a zero byte file
        if(file3.length()==0) {
            fail("could not overwrite an existing, older file");
        }
        if(file3c.length()!=0) {
            fail("could not force overwrite an existing, newer file");
        }
        if(file3b.length()==0) {
            fail("unexpectedly overwrote an existing, newer file");
        }

        //file time checks for java1.2+
        assertTrue(file3a.lastModified()==file3.lastModified());
        assertTrue(file3c.lastModified()<file3a.lastModified());

    }

    public void testFilterTest() {
        executeTarget("filtertest");
        assertTrue(getOutput().indexOf("loop in tokens") == -1);
    }

    public void testInfiniteFilter() {
        executeTarget("infinitetest");
        assertTrue(getOutput().indexOf("loop in tokens") != -1);
    }

    public void testFilterSet() throws IOException {
        executeTarget("testFilterSet");
        File tmp  = new File(getProjectDir(), "copy.filterset.tmp");
        File check  = new File(getProjectDir(), "expected/copy.filterset.filtered");
        assertTrue(tmp.exists());
        assertTrue(FILE_UTILS.contentEquals(tmp, check));
    }

    public void testFilterChain() throws IOException {
        executeTarget("testFilterChain");
        File tmp  = new File(getProjectDir(), "copy.filterchain.tmp");
        File check  = new File(getProjectDir(), "expected/copy.filterset.filtered");
        assertTrue(tmp.exists());
        assertTrue(FILE_UTILS.contentEquals(tmp, check));
    }

    public void testSingleFileFileset() {
        executeTarget("test_single_file_fileset");
        File file  = new File(getProjectDir(),
                                        "copytest_single_file_fileset.tmp");
        assertTrue(file.exists());
    }

    public void testSingleFilePath() {
        executeTarget("test_single_file_path");
        File file  = new File(getProjectDir(),
                                        "copytest_single_file_path.tmp");
        assertTrue(file.exists());
    }

    public void testTranscoding() throws IOException {
        executeTarget("testTranscoding");
        File f1 = getProject().resolveFile("copy/expected/utf-8");
        File f2 = getProject().resolveFile("copytest1.tmp");
        assertTrue(FILE_UTILS.contentEquals(f1, f2));
    }

    public void testMissingFileIgnore() {
        expectLogContaining("testMissingFileIgnore",
                            "Warning: Could not find file ");
    }

    public void testMissingFileBail() {
        expectBuildException("testMissingFileBail", "not-there doesn't exist");
        assertTrue(getBuildException().getMessage()
                   .startsWith("Warning: Could not find file "));
    }

    public void testMissingDirIgnore() {
        expectLogContaining("testMissingDirIgnore", "Warning: ");
    }

    public void testMissingDirBail() {
        expectBuildException("testMissingDirBail", "not-there doesn't exist");
        assertTrue(getBuildException().getMessage().endsWith(" not found."));
    }
    
    public void testFileResourcePlain() {
        executeTarget("testFileResourcePlain");
        File file1 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file1.txt");
        File file2 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file2.txt");
        File file3 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file3.txt");
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
    }
    
    public void _testFileResourceWithMapper() {
        executeTarget("testFileResourceWithMapper");
        File file1 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file1.txt.bak");
        File file2 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file2.txt.bak");
        File file3 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file3.txt.bak");
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
    }
    
    public void testFileResourceWithFilter() {
        executeTarget("testFileResourceWithFilter");
        File file1 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/fileNR.txt");
        assertTrue(file1.exists());
        try {
            String file1Content = FILE_UTILS.readFully(new FileReader(file1));
            assertEquals("This is file 42", file1Content);
        } catch (IOException e) {
            // no-op: not a real business error
        }
    }
    
    public void testPathAsResource() {
        executeTarget("testPathAsResource");
        File file1 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file1.txt");
        File file2 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file2.txt");
        File file3 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file3.txt");
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
    }
    
    public void testZipfileset() {
        executeTarget("testZipfileset");
        File file1 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file1.txt");
        File file2 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file2.txt");
        File file3 = new File(getProjectDir(), getProject().getProperty("to.dir")+"/file3.txt");
        assertTrue(file1.exists());
        assertTrue(file2.exists());
        assertTrue(file3.exists());
    }

    public void testDirset() {
        executeTarget("testDirset");
    }
    
    public void _testResourcePlain() {
        executeTarget("testResourcePlain");
    }
    
    public void _testResourcePlainWithMapper() {
        executeTarget("testResourcePlainWithMapper");
    }
    
    public void _testResourcePlainWithFilter() {
        executeTarget("testResourcePlainWithFilter");
    }
    
    public void _testOnlineResources() {
        executeTarget("testOnlineResources");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1037.java