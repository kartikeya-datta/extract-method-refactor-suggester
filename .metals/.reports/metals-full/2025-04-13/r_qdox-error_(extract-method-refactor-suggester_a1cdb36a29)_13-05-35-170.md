error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3601.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3601.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3601.java
text:
```scala
C@@ommandTestCase.checkCompile("src1/ParentsFail.java", new int[] {3, 11, 19});

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.ajdt.internal.compiler.batch;

import java.io.IOException;

import org.aspectj.testing.util.TestUtil;


public class CompileAndRunTestCase extends CommandTestCase {

	public CompileAndRunTestCase(String name) {
		super(name);
	}

	public void testAround() throws IOException {
		checkCompile("src1/AroundA.java", NO_ERRORS);
		TestUtil.runMain("out", "AroundAMain");
	}
	
	public void testInterType() throws IOException {
		checkCompile("src1/InterType.java", NO_ERRORS);
		TestUtil.runMain("out", "InterType");
	}
	
	public void testInterTypeMethods() throws IOException {
		checkCompile("src1/InterTypeMethods.java", NO_ERRORS);
		TestUtil.runMain("out", "InterTypeMethods");
	}
	
	public void testIf() throws IOException {
		CommandTestCase.checkCompile("src1/IfPcd.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "IfPcd");
	}
	
	public void testDeclareParentsFail() throws IOException {
		CommandTestCase.checkCompile("src1/ParentsFail.java", new int[] {3, 11, 19, 21});
	}
	
	public void testDeclareParents() throws IOException {
		CommandTestCase.checkCompile("src1/Parents.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "Parents");
	}
	
	public void testPerCflow() throws IOException {
		CommandTestCase.checkCompile("src1/PerCflow.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "PerCflow");
	}
		
	public void testPerObject() throws IOException {
		CommandTestCase.checkCompile("src1/PerObject.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "PerObject");
	}
		
	public void testDeclareSoft() throws IOException {
		CommandTestCase.checkCompile("src1/DeclareSoft.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "DeclareSoft");
	}
		
	public void testPrivileged() throws IOException {
		CommandTestCase.checkCompile("src1/Privileged.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "Privileged");
	}
		
	public void testHandler() throws IOException {
		CommandTestCase.checkCompile("src1/Handler.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "Handler");
	}
		
	public void testInterConstructors() throws IOException {
		CommandTestCase.checkCompile("src1/InterTypeConstructors.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "InterTypeConstructors");
	}
		
	public void testAroundA1() throws IOException {
		CommandTestCase.checkCompile("src1/AroundA1.java", CommandTestCase.NO_ERRORS);
		TestUtil.runMain("out", "AroundA1");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3601.java