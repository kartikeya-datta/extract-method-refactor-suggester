error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15300.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15300.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15300.java
text:
```scala
public v@@oid testManifestStaysIntact()

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
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import org.apache.tools.ant.BuildFileTest;

/**
 * @author Erik Meade <emeade@geekfarm.org>
 */
public class JarTest extends BuildFileTest {

    private static String tempJar = "tmp.jar";

    public JarTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/jar.xml");
    }

    public void tearDown() {
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
            // give Windows a chance
            Thread.currentThread().sleep(2500);
        } catch (InterruptedException e) {
        } // end of try-catch
        executeTarget(secondTarget);
        assertEquals("jar has not been recreated in " + secondTarget,
                     jarModifiedDate, jarFile.lastModified());
    }

    public void XtestRecreateWithoutUpdateAdditionalFiles() {
        testRecreate("test4", "testRecreateWithoutUpdateAdditionalFiles");
    }

    public void XtestRecreateWithUpdateAdditionalFiles() {
        testRecreate("test4", "testRecreateWithUpdateAdditionalFiles");
    }

    public void XtestRecreateWithoutUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateWithoutUpdateNewerFile");
    }

    public void XtestRecreateWithUpdateNewerFile() {
        testRecreate("testRecreateNewerFileSetup",
                     "testRecreateWithUpdateNewerFile");
    }

    private void testRecreate(String firstTarget, String secondTarget) {
        executeTarget(firstTarget);
        try {
            // give Windows a chance
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

    public void XtestManifestStaysIntact() 
        throws IOException, ManifestException {
        executeTarget("testManifestStaysIntact");
        Manifest mf1 = 
            new Manifest(new FileReader(getProject()
                                        .resolveFile("jartmp/manifest")));
        Manifest mf2 = 
            new Manifest(new FileReader(getProject()
                                        .resolveFile("jartmp/META-INF/MANIFEST.MF")));
        assertEquals(mf1, mf2);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15300.java