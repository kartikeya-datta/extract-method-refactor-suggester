error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13008.java
text:
```scala
public v@@oid testOutjarDeletedOnError () {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Matthew Webster - initial implementation
 *******************************************************************************/
package org.aspectj.ajdt.internal.core.builder;

import java.io.File;

import org.aspectj.tools.ajc.AjcTestCase;
import org.aspectj.tools.ajc.CompilationResult;
import org.aspectj.weaver.WeaverMessages;

public class OutjarTest extends AjcTestCase {

	public static final String PROJECT_DIR = "OutjarTest";
	
	public static final String injarName  = "child.jar";
	public static final String aspectjarName  = "aspects.jar";
	public static final String outjarName = "outjar.jar";

	private File baseDir;
	
	/**
	 * Make copies of JARs used for -injars/-inpath and -aspectpath because so
	 * they are not overwritten when a test fails.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		baseDir = new File("../org.aspectj.ajdt.core/testdata",PROJECT_DIR);
	}
	
	/**
	 * Aim: Check that -outjar does not coincide with a member of -injars. This
	 *      is because if a binary weave fails -outjar is deleted.
	 * 
	 * Inputs to the compiler:
	 *   -injar
	 *   -aspectpath
	 *   -outjar 
	 * 
	 * Expected result = Compile aborts with error message.
	 */
	public void testOutjarInInjars () {
		String[] args = new String[] {"-aspectpath", aspectjarName, "-injars", injarName, "-outjar", injarName};
		Message error = new Message(WeaverMessages.format(WeaverMessages.OUTJAR_IN_INPUT_PATH));
		Message fail = new Message("Usage:");
		MessageSpec spec = new MessageSpec(null,null,newMessageList(error),newMessageList(fail));
		CompilationResult result = ajc(baseDir,args);
//		System.out.println(result);
		assertMessages(result,spec);
	}
	
	/**
	 * Aim: Check that -outjar does not coincide with a member of -inpath. This
	 *      is because if a binary weave fails -outjar is deleted.
	 * 
	 * Inputs to the compiler:
	 *   -injar
	 *   -aspectpath
	 *   -outjar 
	 * 
	 * Expected result = Compile aborts with error message.
	 */
	public void testOutjarInInpath () {
		String[] args = new String[] {"-aspectpath", aspectjarName, "-inpath", injarName, "-outjar", injarName};
		Message error = new Message(WeaverMessages.format(WeaverMessages.OUTJAR_IN_INPUT_PATH));
		Message fail = new Message("Usage:");
		MessageSpec spec = new MessageSpec(null,null,newMessageList(error),newMessageList(fail));
		CompilationResult result = ajc(baseDir,args);
//		System.out.println(result);
		assertMessages(result,spec);
	}
	
	/**
	 * Aim: Check that -outjar does not coincide with a member of -aspectpath. This
	 *      is because if a binary weave fails -outjar is deleted.
	 * 
	 * Inputs to the compiler:
	 *   -injar
	 *   -aspectpath
	 *   -outjar 
	 * 
	 * Expected result = Compile aborts with error message.
	 */
	public void testOutjarInAspectpath () {
		String[] args = new String[] {"-aspectpath", aspectjarName, "-inpath", injarName, "-outjar", aspectjarName};
		Message error = new Message(WeaverMessages.format(WeaverMessages.OUTJAR_IN_INPUT_PATH));
		Message fail = new Message("Usage:");
		MessageSpec spec = new MessageSpec(null,null,newMessageList(error),newMessageList(fail));
		CompilationResult result = ajc(baseDir,args);
//		System.out.println(result);
		assertMessages(result,spec);
	}
	
	/**
	 * Aim: Check that -outjar is not present when compile fails.
	 * 
	 * Inputs to the compiler:
	 *   -injar
	 *   -aspectpath
	 *   -outjar 
	 * 
	 * Expected result = Compile fails with error message.
	 */
	public void xtestOutjarDeletedOnError () {
		String[] args = new String[] {"-aspectpath", aspectjarName, "-injars", injarName, "-outjar", outjarName};
		Message error = new Message(WeaverMessages.format(WeaverMessages.CANT_FIND_TYPE,"jar1.Parent"));
		MessageSpec spec = new MessageSpec(null,newMessageList(error));
		CompilationResult result = ajc(baseDir,args);
//		System.out.println(result);
		assertMessages(result,spec);
		File outjar = new File(ajc.getSandboxDirectory(),outjarName);
		assertFalse("-outjar " + outjar.getPath() + " should be deleted",outjar.exists());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13008.java