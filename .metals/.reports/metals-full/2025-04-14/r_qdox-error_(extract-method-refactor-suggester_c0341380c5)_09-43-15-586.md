error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14400.java
text:
```scala
public v@@oid testRootFilesInIndex() throws IOException {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.BuildFileTest;

/**
 * @author Erik Meade <emeade@geekfarm.org>
 */
public class JarTest extends BuildFileTest {

    private static String tempJar = "tmp.jar";
    private static String tempDir = "jartmp/";
    private Reader r1, r2;

    public JarTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/jar.xml");
    }

    public void tearDown() {
        if (r1 != null) {
            try {
                r1.close();
            } catch (IOException e) {
            }
        }
        if (r2 != null) {
            try {
                r2.close();
            } catch (IOException e) {
            }
        }
        
        executeTarget("cleanup");
    }

    public void test1() {
        expectBuildException("test1", "required argument not specified");
    }

    public void test2() {
        expectBuildException("test2", "manifest file does not exist");
    }

    public void test3() {
        expectBuildException("test3", "Unrecognized whenempty attribute: format C: /y");
    }

    public void test4() {
        executeTarget("test4");
        File jarFile = new File(getProjectDir(), tempJar);
        assertTrue(jarFile.exists());
    }

    public void testNoRecreateWithoutUpdate() {
        testNoRecreate("test4");
    }

    public void testNoRecreateWithUpdate() {
        testNoRecreate("testNoRecreateWithUpdate");
    }

    private void testNoRecreate(String secondTarget) {
        executeTarget("test4");
        File jarFile = new File(getProjectDir(), tempJar);
        long jarModifiedDate = jarFile.lastModified();
        try {
            Thread.currentThread().sleep(2500);
        } catch (InterruptedException e) {
        } // end of try-catch
        executeTarget(secondTarget);
        assertEquals("jar has not been recreated in " + secondTarget,
                     jarModifiedDate, jarFile.lastModified());
    }

    public void testRecreateWithoutUpdateAdditionalFiles() {
        testRecreate("test4", "testRecreateWithoutUpdateAdditionalFiles");
    }

    public void testRecreateWithUpdateAdditionalFiles() {
        testRecreate("test4", "testRecreateWithUpdateAdditionalFiles");
    }

    public void testRecreateWithoutUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateWithoutUpdateNewerFile");
    }

    public void testRecreateWithUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateWithUpdateNewerFile");
    }

    private void testRecreate(String firstTarget, String secondTarget) {
        executeTarget(firstTarget);
        try {
            Thread.currentThread().sleep(2500);
        } catch (InterruptedException e) {
        } // end of try-catch
        File jarFile = new File(getProjectDir(), tempJar);
        long jarModifiedDate = jarFile.lastModified();
        executeTarget(secondTarget);
        jarFile = new File(getProjectDir(), tempJar);
        assertTrue("jar has been recreated in " + secondTarget,
                   jarModifiedDate < jarFile.lastModified());
    }

    public void testManifestStaysIntact() 
        throws IOException, ManifestException {
        executeTarget("testManifestStaysIntact");

        r1 = new FileReader(getProject()
                            .resolveFile(tempDir + "manifest"));
        r2 = new FileReader(getProject()
                            .resolveFile(tempDir + "META-INF/MANIFEST.MF"));
        Manifest mf1 = new Manifest(r1);
        Manifest mf2 = new Manifest(r2);
        assertEquals(mf1, mf2);
    }

    public void testNoRecreateBasedirExcludesWithUpdate() {
        testNoRecreate("testNoRecreateBasedirExcludesWithUpdate");
    }

    public void testNoRecreateBasedirExcludesWithoutUpdate() {
        testNoRecreate("testNoRecreateBasedirExcludesWithoutUpdate");
    }

    public void testNoRecreateZipfilesetExcludesWithUpdate() {
        testNoRecreate("testNoRecreateZipfilesetExcludesWithUpdate");
    }

    public void testNoRecreateZipfilesetExcludesWithoutUpdate() {
        testNoRecreate("testNoRecreateZipfilesetExcludesWithoutUpdate");
    }

    public void testRecreateZipfilesetWithoutUpdateAdditionalFiles() {
        testRecreate("test4",
                     "testRecreateZipfilesetWithoutUpdateAdditionalFiles");
    }

    public void testRecreateZipfilesetWithUpdateAdditionalFiles() {
        testRecreate("test4",
                     "testRecreateZipfilesetWithUpdateAdditionalFiles");
    }

    public void testRecreateZipfilesetWithoutUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateZipfilesetWithoutUpdateNewerFile");
    }

    public void testRecreateZipfilesetWithUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateZipfilesetWithUpdateNewerFile");
    }

    public void testCreateWithEmptyFileset() {
        executeTarget("testCreateWithEmptyFilesetSetUp");
        executeTarget("testCreateWithEmptyFileset");
        executeTarget("testCreateWithEmptyFileset");
    }

    public void testUpdateIfOnlyManifestHasChanged() {
        executeTarget("testUpdateIfOnlyManifestHasChanged");
        File jarXml = getProject().resolveFile(tempDir + "jar.xml");
        assertTrue(jarXml.exists());
    }

    // bugzilla report 10262
    public void testNoDuplicateIndex() throws IOException {
        ZipFile archive = null;
        try {
            executeTarget("testIndexTests");
            archive = new ZipFile(getProject().resolveFile(tempJar));
            Enumeration enum = archive.entries();
            int numberOfIndexLists = 0;
            while (enum.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) enum.nextElement();
                if (ze.getName().equals("META-INF/INDEX.LIST")) {
                    numberOfIndexLists++;
                }
            }
            assertEquals(1, numberOfIndexLists);
        } finally {
            if (archive != null) {
                archive.close();
            }
        }
    }

    // bugzilla report 16972
    public void XtestRootFilesInIndex() throws IOException {
        ZipFile archive = null;
        try {
            executeTarget("testIndexTests");
            archive = new ZipFile(getProject().resolveFile(tempJar));
            ZipEntry ze = archive.getEntry("META-INF/INDEX.LIST");
            InputStream is = archive.getInputStream(ze);
            BufferedReader r = new BufferedReader(new InputStreamReader(is,
                                                                        "UTF8"));
            boolean foundSub = false;
            boolean foundSubFoo = false;
            boolean foundFoo = false;
            
            String line = r.readLine();
            while (line != null) {
                if (line.equals("foo")) {
                    foundFoo = true;
                } else if (line.equals("sub")) {
                    foundSub = true;
                } else if (line.equals("sub/foo")) {
                    foundSubFoo = true;
                }
                line = r.readLine();
            }

            assertTrue(foundSub);
            assertTrue(!foundSubFoo);
            assertTrue(foundFoo);
        } finally {
            if (archive != null) {
                archive.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14400.java