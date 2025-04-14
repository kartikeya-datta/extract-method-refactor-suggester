error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/501.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/501.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[122,2]

error in qdox parser
file content:
```java
offset: 3189
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/501.java
text:
```scala
// org.aspectj.asm.AsmManager.setReporting("debug.txt",true,true,true,true);

/*******************************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution and is available at
 * http://eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * initial implementation              Alexandre Vasseur
 *******************************************************************************/
package org.aspectj.systemtest.ajc150.ataspectj;

import junit.framework.Test;
import org.aspectj.testing.XMLBasedAjcTestCase;

import java.io.File;

/**
 * A suite for @AspectJ aspects located in java5/ataspectj
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AtAjSyntaxTests extends XMLBasedAjcTestCase {

    public static Test suite() {
        return XMLBasedAjcTestCase.loadSuite(AtAjSyntaxTests.class);
    }

    protected File getSpecFile() {
        return new File("../tests/src/org/aspectj/systemtest/ajc150/ataspectj/syntax.xml");
    }

    public void testSimpleBefore() {
        runTest("SimpleBefore");
    }

    public void testSimpleAfter() {
        runTest("SimpleAfter");
    }

    public void testSingletonAspectBindings() {
        //Note AV: uncomment setReporting to get it in modules/tests folder
        org.aspectj.asm.AsmManager.setReporting("debug.txt",true,true,true,true);
        runTest("singletonAspectBindings");
        // same stuff with AJ
        //org.aspectj.asm.AsmManager.setReporting("debug-aj.txt",true,true,true,true);
        //runTest("singletonAspectBindings2");

    }

    public void testCflowTest() {
        runTest("CflowTest");
    }

    public void testPointcutReferenceTest() {
        runTest("PointcutReferenceTest");
    }

    public void testXXJoinPointTest() {
        runTest("XXJoinPointTest");
    }

    public void testPrecedenceTest() {
        runTest("PrecedenceTest");
    }

    public void testAfterXTest() {
        runTest("AfterXTest");
    }

    public void testBindingTest() {
        runTest("BindingTest");
    }

    public void testBindingTestNoInline() {
        runTest("BindingTest no inline");
    }

    public void testPerClause() {
        runTest("PerClause");
    }

    public void testAroundInlineMunger_XnoInline() {
        runTest("AroundInlineMunger -XnoInline");
    }

    public void testAroundInlineMunger() {
        runTest("AroundInlineMunger");
    }

    public void testAroundInlineMunger2() {
        runTest("AroundInlineMunger2");
    }

    public void testDeow() {
        runTest("Deow");
    }

    public void testSingletonInheritance() {
        runTest("singletonInheritance");
    }

    public void testPerClauseInheritance() {
        runTest("perClauseInheritance");
    }

    public void testIfPointcut() {
        runTest("IfPointcutTest");
    }

    public void testIfPointcut2() {
        runTest("IfPointcut2Test");
    }

    public void testMultipleBinding() {
        runTest("MultipleBinding");
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/501.java