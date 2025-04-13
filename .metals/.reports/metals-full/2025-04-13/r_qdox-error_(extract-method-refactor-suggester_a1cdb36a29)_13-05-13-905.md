error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,56]

error in qdox parser
file content:
```java
offset: 56
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6431.java
text:
```scala
"You must not specify nested elements when using refid")@@;

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
package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildFileTest;

/**
 * test assertion handling
 */
public class AssertionsTest extends BuildFileTest {

    public AssertionsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        configureProject("src/etc/testcases/types/assertions.xml");
    }

    protected void tearDown() throws Exception {
        executeTarget("teardown");
    }

    /**
     * runs a test and expects an assertion thrown in forked code
     * @param target
     */
    protected void expectAssertion(String target) {
        expectBuildExceptionContaining(target,
                "assertion not thrown in "+target,
                "Java returned: 1");
    }

    public void testClassname() {
        expectAssertion("test-classname");
    }

    public void testPackage() {
        expectAssertion("test-package");
    }

    public void testEmptyAssertions() {
        executeTarget("test-empty-assertions");
    }

    public void testDisable() {
        executeTarget("test-disable");
    }

    public void testOverride() {
        expectAssertion("test-override");
    }

    public void testOverride2() {
        executeTarget("test-override2");
    }
    public void testReferences() {
        expectAssertion("test-references");
    }

    public void testMultipleAssertions() {
        expectBuildExceptionContaining("test-multiple-assertions",
                "multiple assertions rejected",
                "Only one assertion declaration is allowed");
    }

    public void testReferenceAbuse() {
        expectBuildExceptionContaining("test-reference-abuse",
                "reference abuse rejected",
                "You must not specify more than one attribute when using refid");
    }

    public void testNofork() {
        if (AssertionsTest.class.desiredAssertionStatus()) {
            return; // ran Ant tests with -ea and this would fail spuriously
        }
        expectLogContaining("test-nofork",
                "Assertion statements are currently ignored in non-forked mode");
    }


    public void testJunit() {
        executeTarget("test-junit");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6431.java