error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2569.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2569.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2569.java
text:
```scala
F@@ile srcdir = new File("../../docs/sandbox/ubc-design-patterns/src");

/* *******************************************************************
 * Copyright (c) 2003 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Mik Kersten     initial implementation 
 * ******************************************************************/

package org.aspectj.tools.ajdoc;

import java.io.File;

import junit.framework.TestCase;

/**
 * A long way to go until full coverage, but this is the place to add more.
 * 
 * @author Mik Kersten
 */
public class PatternsTestCase extends TestCase {
	
	public void testSimpleExample() {
		  
//		System.err.println(new File("testdata.figures-demo").exists());
//		File file1 = new File("testdata/patterns/allPatterns.lst");
		File outdir = new File("testdata/patterns/doc");
		File srcdir = new File("../docs/sandbox/ubc-design-patterns/src");
		
		String[] args = { 
//			"-XajdocDebug", 
                "-classpath",
                AjdocTests.ASPECTJRT_PATH.getPath(),
			"-d", 
			outdir.getAbsolutePath(),
			"-sourcepath", 
			srcdir.getAbsolutePath(),
			"ca.ubc.cs.spl.aspectPatterns.patternLibrary",
			"ca.ubc.cs.spl.aspectPatterns.examples.abstractFactory.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.abstractFactory.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.builder.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.builder.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.factoryMethod.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.factoryMethod.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.prototype.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.prototype.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.singleton.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.singleton.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.adapter.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.adapter.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.bridge.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.bridge.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.composite.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.composite.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.decorator.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.decorator.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.facade.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.facade.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.flyweight.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.flyweight.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.proxy.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.proxy.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.chainOfResponsibility.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.chainOfResponsibility.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.command.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.command.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.interpreter.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.interpreter.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.iterator.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.iterator.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.mediator.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.mediator.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.memento.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.memento.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.observer.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.observer.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.state.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.state.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.strategy.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.strategy.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.templateMethod.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.templateMethod.aspectj",
			"ca.ubc.cs.spl.aspectPatterns.examples.visitor.java",
			"ca.ubc.cs.spl.aspectPatterns.examples.visitor.aspectj"
		};
		
		org.aspectj.tools.ajdoc.Main.main(args);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2569.java