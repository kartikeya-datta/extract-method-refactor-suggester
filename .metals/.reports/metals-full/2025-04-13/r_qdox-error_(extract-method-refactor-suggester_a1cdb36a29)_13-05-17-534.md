error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13979.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13979.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13979.java
text:
```scala
F@@ile aspect1 = new File("testdata/coverage/foo/UseThisAspectForLinkCheck.aj");

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
public class CoverageTestCase extends TestCase {
	
	public void testCoverage() {
		  
//		System.err.println(new File("testdata/figures-demo").exists());
		File file1 = new File("testdata/coverage/foo/ClassA.java");
		File aspect1 = new File("testdata/coverage/foo/UseThisAspectForLinkCheck.java");
		File file2 = new File("testdata/coverage/foo/InterfaceI.java");
		File file3 = new File("testdata/coverage/foo/PlainJava.java");
		File file4 = new File("testdata/coverage/foo/ModelCoverage.java");
		File file5 = new File("testdata/coverage/fluffy/Fluffy.java");
		File file6 = new File("testdata/coverage/fluffy/bunny/Bunny.java");
		File file7 = new File("testdata/coverage/fluffy/bunny/rocks/Rocks.java");
		File file8 = new File("testdata/coverage/fluffy/bunny/rocks/UseThisAspectForLinkCheckToo.java");
		File outdir = new File("testdata/coverage/doc");
		
		String[] args = { 
//			"-XajdocDebug",
			"-source", 
			"1.4",
			"-private",
			"-d", 
			outdir.getAbsolutePath(),
			aspect1.getAbsolutePath(),
			file1.getAbsolutePath(), 
			file2.getAbsolutePath(),
			file3.getAbsolutePath(),
			file4.getAbsolutePath(),
			file5.getAbsolutePath(),
			file6.getAbsolutePath(),
			file7.getAbsolutePath(),
			file8.getAbsolutePath()
		};
		
		org.aspectj.tools.ajdoc.Main.main(args);
	}

//	public void testPlainJava() {
//		File file1 = new File("testdata/coverage/foo/PlainJava.java");
//		File outdir = new File("testdata/coverage/doc");
//		
//		String[] args = { "-d", 
//				outdir.getAbsolutePath(),
//				file1.getAbsolutePath() };
//		
//		org.aspectj.tools.ajdoc.Main.main(args);
//	}
	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13979.java