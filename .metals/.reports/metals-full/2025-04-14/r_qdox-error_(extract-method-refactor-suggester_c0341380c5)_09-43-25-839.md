error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9067.java
text:
```scala
r@@eturn new File("../tests/src/org/aspectj/systemtest/ajc150/ataspectj/annotationgen.xml");

/*******************************************************************************
 * Copyright (c) 2005 Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 * initial development             Jonas Bonér, Alexandre Vasseur 
 *******************************************************************************/
package org.aspectj.systemtest.ajc150.ataspectj;

import java.io.File;

import junit.framework.Test;

import org.aspectj.testing.XMLBasedAjcTestCase;

/**
 * A suite for @AspectJ aspects located in java5/ataspectj
 *
 * @author <a href="mailto:alex AT gnilux DOT com">Alexandre Vasseur</a>
 */
public class AtAjAnnotationGenTests extends XMLBasedAjcTestCase {
	
	public static Test suite() {
	    return XMLBasedAjcTestCase.loadSuite(AtAjAnnotationGenTests.class);
	}

	protected File getSpecFile() {
	  return new File("../tests/src/org/aspectj/systemtest/ajc150/ataspectj/atajc150.xml");
	}
	
	public void testSimpleAspect() {
		runTest("annotation gen for simple aspect");
	}
	
	public void testSimpleAspectIn14Mode() {
		runTest("annotation gen for simple aspect pre 1.5");
	}
	
	public void testAspectAlreadyAnnotated() {
		runTest("annotation gen for simple annotated aspect");		
	}
	
	public void testPrivilegedAspect() {
		runTest("annotation gen for privileged aspect");
	}
	
	public void testPerThisAspect() {
		runTest("annotation gen for perthis aspect");
	}
	
	public void testPerTargetAspect() {
		runTest("annotation gen for pertarget aspect");
	}
	
	public void testPerCflowAspect() {
		runTest("annotation gen for percflow aspect");
	}
	
	public void testPerCflowbelowAspect() {
		runTest("annotation gen for percflowbelow aspect");
	}
	
	public void testPertypewithinAspect() {
		runTest("annotation gen for pertypewithin aspect");
	}
  
	public void testInnerAspectOfClass() {
		runTest("annotation gen for inner aspect of aspect");
	}
	
	public void testInnerAspectOfAspect() {
		runTest("annotation gen for inner aspect of class");
	}
	
	public void testAdvice() {
		runTest("annotation gen for advice declarations");
	}

	public void testSimplePointcut() {
		runTest("annotation gen for simple pointcut");
	}

	public void testPointcutModifiers() {
		runTest("annotation gen for pointcut modifiers");
	}

	public void testPointcutParams() {
		runTest("annotation gen for pointcut params");		
	}

	public void testPointcutRefs() {
		runTest("annotation gen for pointcut refs");		
	}
	
	public void testBeforeWithBadReturn() {
		runTest("before ann with non-void return");
	}
	
	public void testTwoAnnotationsOnSameElement() {
		runTest("two anns on same element");
	}
	
	public void testBadPcutInAdvice() {
		runTest("bad pcut in after advice");
	}
	
	public void testBadParameterBinding() {
		runTest("bad parameter binding in advice");
	}
	
	public void testSimpleAtPointcut() {
		runTest("simple pointcut no params");
	}
	
	public void testPointcutMedley() {
		runTest("pointcut medley");
	}
	
	public void testAdviceDeclaredInClass() {
		runTest("advice in a class");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9067.java