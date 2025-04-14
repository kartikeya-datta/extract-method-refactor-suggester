error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7600.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7600.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7600.java
text:
```scala
n@@ew int[] {5, 15, 16, 22, 25});

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

import java.util.*;

import org.aspectj.ajdt.ajc.*;
import org.aspectj.bridge.*;

import junit.framework.*;

/**
 * @author hugunin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BasicCommandTestCase extends CommandTestCase {

	/**
	 * Constructor for CommandTestCase.
	 * @param name
	 */
	public BasicCommandTestCase(String name) {
		super(name);
	}
	
	public void testA() {
		checkCompile("src1/A.java", NO_ERRORS);
	}
	
	public void testA1() {
		checkCompile("src1/A1.java", NO_ERRORS);
	}
	
	public void testBadA() {
		checkCompile("src1/BadA.java", new int[] {7, 8});
	}
	
	public void testHello() {
		checkCompile("src1/Hello.java", NO_ERRORS);
	}

	public void testBadHello() {
		checkCompile("src1/BadHello.java", new int[] {5});
	}

	public void testMissingHello() {
		checkCompile("src1/MissingHello.java", TOP_ERROR);
	}
	
	public void testBadBinding() {
		checkCompile("src1/BadBinding.java", new int[] {2, 4, 8, 10, 13, 16, 19});
	}
	public void testThisAndModifiers() {
		checkCompile("src1/ThisAndModifiers.java", NO_ERRORS);
	}
	public void testDeclares() {
		checkCompile("src1/Declares.java", new int[] {3});
	}	
	
	public void testDeclareWarning() {
		checkCompile("src1/DeclareWarning.java", NO_ERRORS);
	}	
	
	
	public void testP1() {
		checkCompile("src1/p1/Foo.java", NO_ERRORS);
	}
	
	public void testUnimplementedSyntax() {
		checkCompile("src1/UnimplementedSyntax.java", 
			new int[] {5, 15, 16, 23});
	}
	public void testXlintWarn() {
		checkCompile("src1/Xlint.java", NO_ERRORS);
	}
	public void testXlintError() {
		List args = new ArrayList();

		args.add("-d");
		args.add("out");
		
		args.add("-classpath");
		args.add("../runtime/bin;../lib/junit/junit.jar;../testing-client/bin");
		args.add("-Xlint:error");
		args.add("testdata/src1/Xlint.java");
		
		runCompiler(args, new int[] {2});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7600.java