error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8836.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8836.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8836.java
text:
```scala
public v@@oid testSuperCallsInAtAspectJAdvice_pr139749() { runTest("Super calls in @AspectJ advice");}

/*******************************************************************************
 * Copyright (c) 2006 IBM 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *    Andy Clement - initial API and implementation
 *******************************************************************************/
package org.aspectj.systemtest.ajc152;

import java.io.File;
import junit.framework.Test;

import org.aspectj.testing.XMLBasedAjcTestCase;

public class Ajc152Tests extends org.aspectj.testing.XMLBasedAjcTestCase {
	
  public void testNotAtWithincode_pr138158_1() { runTest("not at withincode - 1");}
  public void testNotAtWithincode_pr138158_2() { runTest("not at withincode - 2");}
  public void testNotAtWithincode_pr138158_3() { runTest("not at within - 3");}
//  public void testComplexGenericDecl_pr137568() { runTest("complicated generics declaration");}
  public void testNpeOnDup_pr138143() { runTest("npe on duplicate method with ataj");}
  public void testPointcutsAndGenerics_pr137496_1() { runTest("pointcuts and generics - B");}
  public void testPointcutsAndGenerics_pr137496_2() { runTest("pointcuts and generics - D");}
  public void testPointcutsAndGenerics_pr137496_3() { runTest("pointcuts and generics - E");}
  public void testPointcutsAndGenerics_pr137496_4() { runTest("pointcuts and generics - F");}
  public void testPointcutsAndGenerics_pr137496_5() { runTest("pointcuts and generics - G");}
  public void testPointcutsAndGenerics_pr137496_6() { runTest("pointcuts and generics - H");}
  public void testAspectLibrariesAndASM_pr135001() { runTest("aspect libraries and asm");}
  public void testStackOverflow_pr136258() { runTest("stack overflow");}
  public void testIncorrectOverridesEvaluation13() { runTest("incorrect overrides evaluation - 1.3"); }
  public void testIncorrectOverridesEvaluation15() { runTest("incorrect overrides evaluation - 1.5"); }
  public void testAtWithinCodeBug_pr138798() { runTest("atWithinCodeBug"); }

  // known failures, uncomment when working.
  public void testReferencePCutInDeclareWarning_pr138215() { runTest("Reference pointcut fails inside @DeclareWarning");}
//  public void testReferencePCutInPerClause_pr138219() { runTest("Can't use a FQ Reference pointcut in any pointcut expression referenced by a per-clause");}
//  public void testDoubleAnnotationMatching_pr138223() { runTest("Double at annotation matching (no binding)");}
//  public void testSuperCallsInAtAspectJAdvice_pr139749() { runTest("Super calls in @AspectJ advice");}

  public void testNoClassCastExceptionWithPerThis_pr138286() { runTest("No ClassCastException with perThis");}
  
// this next one reported as a bug by Rob Harrop, but I can't reproduce the failure yet...
//public void testAtAspectWithReferencePCPerClause_pr138220() { runTest("@Aspect with reference pointcut in perclause");}  

  /////////////////////////////////////////
  public static Test suite() {
    return XMLBasedAjcTestCase.loadSuite(Ajc152Tests.class);
  }

  protected File getSpecFile() {
    return new File("../tests/src/org/aspectj/systemtest/ajc152/ajc152.xml");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8836.java