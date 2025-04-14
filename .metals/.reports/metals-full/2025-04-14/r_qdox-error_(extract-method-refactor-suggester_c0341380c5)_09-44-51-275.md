error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15020.java
text:
```scala
n@@ew java.io.File("D:/foo.txt").getCanonicalPath();

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
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

/**
 * Tests &lt;bm:manifestclasspath&gt;.
 */
public class ManifestClassPathTest
             extends BuildFileTest {

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/manifestclasspath.xml");
    }

    public void testBadDirectory() {
        expectBuildExceptionContaining("test-bad-directory", "bad-jar-dir",
                                       "Jar's directory not found:");
        assertPropertyUnset("jar.classpath");
    }

    public void testBadNoProperty() {
        expectBuildExceptionContaining("test-bad-no-property", "no-property",
                                       "Missing 'property' attribute!");
        assertPropertyUnset("jar.classpath");
    }

    public void testBadPropertyExists() {
        expectBuildExceptionContaining("test-bad-property-exists",
            "property-exits", "Property 'jar.classpath' already set!");
        assertPropertyEquals("jar.classpath", "exists");
    }

    public void testBadNoJarfile() {
        expectBuildExceptionContaining("test-bad-no-jarfile", "no-jarfile",
                                       "Missing 'jarfile' attribute!");
        assertPropertyUnset("jar.classpath");
    }

    public void testBadNoClassPath() {
        expectBuildExceptionContaining("test-bad-no-classpath", "no-classpath",
                                       "Missing nested <classpath>!");
        assertPropertyUnset("jar.classpath");
    }

    public void testParentLevel1() {
        executeTarget("test-parent-level1");

        assertPropertyEquals("jar.classpath", "dsp-core/ " +
                                              "dsp-pres/ " +
                                              "dsp-void/ " +
                                              "../generated/dsp-core/ " +
                                              "../generated/dsp-pres/ " +
                                              "../generated/dsp-void/ " +
                                              "../resources/dsp-core/ " +
                                              "../resources/dsp-pres/ " +
                                              "../resources/dsp-void/");
    }

    public void testParentLevel2() {
        executeTarget("test-parent-level2");

        assertPropertyEquals("jar.classpath", "../dsp-core/ " +
                                              "../dsp-pres/ " +
                                              "../dsp-void/ " +
                                              "../../generated/dsp-core/ " +
                                              "../../generated/dsp-pres/ " +
                                              "../../generated/dsp-void/ " +
                                              "../../resources/dsp-core/ " +
                                              "../../resources/dsp-pres/ " +
                                              "../../resources/dsp-void/");
    }

    public void testParentLevel2TooDeep() {
        expectBuildExceptionContaining("test-parent-level2-too-deep", "nopath",
                                       "No suitable relative path from ");
        assertPropertyUnset("jar.classpath");
    }

    public void testPseudoTahoeRefid() {
        if (!RegexpMatcherFactory.regexpMatcherPresent(project)) {
            System.out.println("Test 'testPseudoTahoeRefid' skipped because no regexp matcher is present.");
            return;
        }
        executeTarget("test-pseudo-tahoe-refid");
        assertPropertyEquals("jar.classpath", "classes/dsp-core/ " +
                                              "classes/dsp-pres/ " +
                                              "classes/dsp-void/ " +
                                              "generated/dsp-core/ " +
                                              "resources/dsp-core/ " +
                                              "resources/dsp-pres/");
    }

    public void testPseudoTahoeNested() {
        if (!RegexpMatcherFactory.regexpMatcherPresent(project)) {
            System.out.println("Test 'testPseudoTahoeNested' skipped because no regexp matcher is present.");
            return;
        }
        executeTarget("test-pseudo-tahoe-nested");
        assertPropertyEquals("jar.classpath", "classes/dsp-core/ " +
                                              "classes/dsp-pres/ " +
                                              "classes/dsp-void/ " +
                                              "generated/dsp-core/ " +
                                              "resources/dsp-core/ " +
                                              "resources/dsp-pres/");
    }

    public void testParentLevel2WithJars() {
        executeTarget("test-parent-level2-with-jars");

        assertPropertyEquals("jar.classpath", "../../lib/acme-core.jar " +
                                              "../../lib/acme-pres.jar " +
                                              "../dsp-core/ " +
                                              "../dsp-pres/ " +
                                              "../dsp-void/ " +
                                              "../../generated/dsp-core/ " +
                                              "../../generated/dsp-pres/ " +
                                              "../../generated/dsp-void/ " +
                                              "../../resources/dsp-core/ " +
                                              "../../resources/dsp-pres/ " +
                                              "../../resources/dsp-void/");
    }
    public void testInternationalGerman() {
        executeTarget("international-german");
        expectLogContaining("run-two-jars", "beta alpha");

    }
    public void testInternationalHebrew() {
        if (!Os.isFamily("windows")) {
            executeTarget("international-hebrew");
            expectLogContaining("run-two-jars", "beta alpha");
        } else {
            System.out.println("Test with hebrew path not attempted under Windows");
        }

    }

    public void testSameWindowsDrive() {
        if (!Os.isFamily("windows")) {
            System.out.println("Test with drive letters only run on windows");
        } else {
            executeTarget("testSameDrive");
            assertPropertyEquals("cp", "../a/b/x.jar");
        }
    }

    public void testDifferentWindowsDrive() {
        if (!Os.isFamily("windows")) {
            System.out.println("Test with drive letters only run on windows");
        } else {
            try {
                new java.io.File("D:/").getCanonicalPath();
            } catch (java.io.IOException e) {
                System.out.println("drive d: doesn't exist or is not ready,"
                                   + " skipping test");
                return;
            }

            expectBuildExceptionContaining("testDifferentDrive",
                                           "different drive",
                                           "No suitable relative path from ");
            assertPropertyUnset("cp");
        }
    }
} // END class ManifestClassPathTest

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15020.java