error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6500.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6500.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6500.java
text:
```scala
i@@f (Os.isFamily("mac") && JavaEnvUtils.getJavaVersionNumber() <= JavaEnvUtils.VERSION_1_6) {

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
package org.apache.tools.ant.util;

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.tools.ant.taskdefs.condition.Os;

/**
 * TestCase for JavaEnvUtils.
 *
 */
public class JavaEnvUtilsTest extends TestCase {

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    public JavaEnvUtilsTest(String s) {
        super(s);
    }

    public void testGetExecutableNetware() {
        if (Os.isName("netware")) {
            assertEquals("java", JavaEnvUtils.getJreExecutable("java"));
            assertEquals("javac", JavaEnvUtils.getJdkExecutable("javac"));
            assertEquals("foo", JavaEnvUtils.getJreExecutable("foo"));
            assertEquals("foo", JavaEnvUtils.getJdkExecutable("foo"));
        }
    }

    public void testGetExecutableWindows() {
        if (Os.isFamily("windows")) {
            String javaHome =
                FILE_UTILS.normalize(System.getProperty("java.home"))
                .getAbsolutePath();

            String j = JavaEnvUtils.getJreExecutable("java");
            assertTrue(j.endsWith(".exe"));
            assertTrue(j+" is absolute", (new File(j)).isAbsolute());
            try {
                assertTrue(j+" is normalized and in the JRE dir",
                           j.startsWith(javaHome));
            } catch (AssertionFailedError e) {
                // java.home is bogus
                assertEquals("java.exe", j);
            }

            j = JavaEnvUtils.getJdkExecutable("javac");
            assertTrue(j.endsWith(".exe"));
            try {
                assertTrue(j+" is absolute", (new File(j)).isAbsolute());
                String javaHomeParent =
                    FILE_UTILS.normalize(javaHome+"/..").getAbsolutePath();
                assertTrue(j+" is normalized and in the JDK dir",
                           j.startsWith(javaHomeParent));
                assertTrue(j+" is normalized and not in the JRE dir",
                           !j.startsWith(javaHome));

            } catch (AssertionFailedError e) {
                // java.home is bogus
                assertEquals("javac.exe", j);
            }

            assertEquals("foo.exe", JavaEnvUtils.getJreExecutable("foo"));
            assertEquals("foo.exe", JavaEnvUtils.getJdkExecutable("foo"));
        }
    }

    public void testGetExecutableMostPlatforms() {
        if (!Os.isName("netware") && !Os.isFamily("windows")) {
            String javaHome =
                FILE_UTILS.normalize(System.getProperty("java.home"))
                .getAbsolutePath();

            // could still be OS/2
            String extension = Os.isFamily("dos") ? ".exe" : "";

            String j = JavaEnvUtils.getJreExecutable("java");
            if (!extension.equals("")) {
                assertTrue(j.endsWith(extension));
            }
            assertTrue(j+" is absolute", (new File(j)).isAbsolute());
            assertTrue(j+" is normalized and in the JRE dir",
                       j.startsWith(javaHome));

            j = JavaEnvUtils.getJdkExecutable("javac");
            if (!extension.equals("")) {
                assertTrue(j.endsWith(extension));
            }
            assertTrue(j+" is absolute", (new File(j)).isAbsolute());

            String javaHomeParent =
                FILE_UTILS.normalize(javaHome+"/..").getAbsolutePath();
            assertTrue(j+" is normalized and in the JDK dir",
                       j.startsWith(javaHomeParent));

            if (Os.isFamily("mac")) {
                assertTrue(j+" is normalized and in the JRE dir",
                           j.startsWith(javaHome));
            } else {
                assertTrue(j+" is normalized and not in the JRE dir",
                           !j.startsWith(javaHome));
            }

            assertEquals("foo"+extension,
                         JavaEnvUtils.getJreExecutable("foo"));
            assertEquals("foo"+extension,
                         JavaEnvUtils.getJdkExecutable("foo"));
        }

    }

    public void testIsAtLeastJavaVersion()
    {
        assertTrue(
                "Current java version is not at least the current java version...",
                JavaEnvUtils.isAtLeastJavaVersion(JavaEnvUtils.getJavaVersion()));
        assertFalse(
                "In case the current java version is higher than 9.0 definitely a new algorithem will be needed",
                JavaEnvUtils.isAtLeastJavaVersion("9.0"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6500.java