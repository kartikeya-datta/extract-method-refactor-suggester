error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11013.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11013.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11013.java
text:
```scala
T@@hread.sleep(3000);

/*
 * Copyright  2000-2004 The Apache Software Foundation
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

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Enumeration;

/**
 */
public class ZipTest extends BuildFileTest {
    //instance variable to allow cleanup
    ZipFile zfPrefixAddsDir = null;
    public ZipTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/zip.xml");
    }

    public void test1() {
        expectBuildException("test1", "required argument not specified");
    }

    public void test2() {
        expectBuildException("test2", "required argument not specified");
    }

    public void test3() {
        expectBuildException("test3", "zip cannot include itself");
    }

//    public void test4() {
//        expectBuildException("test4", "zip cannot include itself");
//    }

    public void tearDown() {
        try {
            if ( zfPrefixAddsDir != null) {
                zfPrefixAddsDir.close();
            }

        } catch (IOException e) {
            //ignored
        }
        executeTarget("cleanup");
    }

    public void test5() {
        executeTarget("test5");
    }


    public void test6() {
        executeTarget("test6");
    }


    public void test7() {
        executeTarget("test7");
    }

    public void test8() {
        executeTarget("test8");
    }

    public void testZipgroupfileset() throws IOException {
        executeTarget("testZipgroupfileset");

        ZipFile zipFile = new ZipFile(new File(getProjectDir(), "zipgroupfileset.zip"));

        assertTrue(zipFile.getEntry("ant.xml") != null);
        assertTrue(zipFile.getEntry("optional/jspc.xml") != null);
        assertTrue(zipFile.getEntry("zip/zipgroupfileset3.zip") != null);

        assertTrue(zipFile.getEntry("test6.mf") == null);
        assertTrue(zipFile.getEntry("test7.mf") == null);

        zipFile.close();
    }

    public void testUpdateNotNecessary() {
        executeTarget("testUpdateNotNecessary");
        assertEquals(-1, getLog().indexOf("Updating"));
    }

    public void testUpdateIsNecessary() {
        expectLogContaining("testUpdateIsNecessary", "Updating");
    }

    // Bugzilla Report 18403
    public void testPrefixAddsDir() throws IOException {
        executeTarget("testPrefixAddsDir");
        File archive = getProject().resolveFile("test3.zip");
        zfPrefixAddsDir = new ZipFile(archive);
        ZipEntry ze = zfPrefixAddsDir.getEntry("test/");
        assertNotNull("test/ has been added", ze);

    }

    // Bugzilla Report 19449
    public void testFilesOnlyDoesntCauseRecreate()
        throws InterruptedException {
        executeTarget("testFilesOnlyDoesntCauseRecreateSetup");
        long l = getProject().resolveFile("test3.zip").lastModified();
        Thread.currentThread().sleep(3000);
        executeTarget("testFilesOnlyDoesntCauseRecreate");
        assertEquals(l, getProject().resolveFile("test3.zip").lastModified());
    }

    // Bugzilla Report 22865
    public void testEmptySkip() {
        executeTarget("testEmptySkip");
        assertTrue("archive should get skipped",
                   !getProject().resolveFile("test3.zip").exists());
    }
    // Bugzilla Report 30365
    public void testZipEmptyDir() {
        executeTarget("zipEmptyDir");
        assertTrue("archive should be created",
                   getProject().resolveFile("test3.zip").exists());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11013.java