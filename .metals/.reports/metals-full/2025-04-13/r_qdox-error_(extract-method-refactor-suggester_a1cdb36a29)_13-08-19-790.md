error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1988.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1988.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[60,2]

error in qdox parser
file content:
```java
offset: 3301
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1988.java
text:
```scala
// public void testAmbiguousBinding_pr121805() { runTest("ambiguous binding");}

/*******************************************************************************
 * Copyright (c) 2006 IBM 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andy Clement - initial API and implementation
 *******************************************************************************/
package org.aspectj.systemtest.ajc153;

import java.io.File;

import junit.framework.Test;

import org.aspectj.testing.Utils;
import org.aspectj.testing.XMLBasedAjcTestCase;

public class Ajc153Tests extends org.aspectj.testing.XMLBasedAjcTestCase {

  // public void testMissingLineNumbersInStacktrace_pr145442() { runTest("missing line numbers in stacktrace");}
  // public void testArgnamesAndJavac_pr148381() { runTest("argNames and javac");}
  // public void testCFlowXMLAspectLTW_pr149096() { runTest("cflow xml concrete aspect"); }
  public void testAmbiguousBinding_pr121805() { runTest("ambiguous binding");}
  public void testGenericInheritanceDecp_pr150095() { runTest("generics, inheritance and decp");}
  public void testIllegalStateException_pr148737() { runTest("illegalstateexception for non generic type");}
  public void testAtajInheritance_pr149305_1()     { runTest("ataj inheritance - 1");}
  public void testAtajInheritance_pr149305_2()     { runTest("ataj inheritance - 2");}
  public void testAtajInheritance_pr149305_3()     { runTest("ataj inheritance - 3");}
  public void testVerificationFailureForAspectOf_pr148693() {
	runTest("verification problem");   // build the code
	Utils.verifyClass(ajc,"mypackage.MyAspect"); // verify it <<< BRAND NEW VERIFY UTILITY FOR EVERYONE TO TRY ;)
  }
  public void testIncorrectAnnotationValue_pr148537()          { runTest("incorrect annotation value");}
  public void testVerifyErrNoTypeCflowField_pr145693_1()       { runTest("verifyErrNoTypeCflowField"); }
  public void testVerifyErrInpathNoTypeCflowField_pr145693_2() { runTest("verifyErrInpathNoTypeCflowField"); }
  public void testCpathNoTypeCflowField_pr145693_3()           { runTest("cpathNoTypeCflowField"); }
  // public void testVisibilityProblem_pr149071()                 { runTest("visibility problem");}
  // public void testAdviceNotWovenAspectPath_pr147841()          { runTest("advice not woven on aspectpath");}
  public void testGenericSignatures_pr148409()                 { runTest("generic signature problem"); }
//  public void testBrokenIfArgsCflowAtAj_pr145018() { runTest("ataj crashing with cflow, if and args");}
  public void testCantFindType_pr149322_01() {runTest("can't find type on interface call 1");}
  public void testCantFindType_pr149322_02() {runTest("can't find type on interface call 2");}
  public void testCantFindType_pr149322_03() {runTest("can't find type on interface call 3");}

  /////////////////////////////////////////
  public static Test suite() {
    return XMLBasedAjcTestCase.loadSuite(Ajc153Tests.class);
  }

  protected File getSpecFile() {
    return new File("../tests/src/org/aspectj/systemtest/ajc153/ajc153.xml");
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1988.java