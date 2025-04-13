error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[90,2]

error in qdox parser
file content:
```java
offset: 3942
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11026.java
text:
```scala
//import org.aspectj.ajdt.internal.compiler.AjPipeliningCompilerAdapter;

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

import org.aspectj.ajdt.internal.compiler.AjPipeliningCompilerAdapter;
import org.aspectj.testing.XMLBasedAjcTestCase;

/**
 * testplan: (x = complete)
 * 
 * x @AspectJ aspects - are they recognized and sorted correctly ?
 * x compiling classes (various orderings)
 * x compiling classes (inheritance relationships)
 * x compiling aspects and classes (various orderings - aspects first/last)
 * x eclipse annotation transformation logic
 * x aspects extending classes
 * x nested types (and aspect inside a regular class)
 * x set of files that are only aspects
 * x pointcuts in super classes
 * - classes with errors
 * - aspects with errors
 * - Xterminate after compilation (now == skip weaving??)
 * 
 * That this pipeline works OK for large systems is kind of confirmed by using it to build shadows!
 *
 */
public class PipeliningTests extends org.aspectj.testing.XMLBasedAjcTestCase {

	// straightforward compilation
	public void testBuildTwoClasses() { runTest("build two classes");}
	public void testBuildOneAspectTwoClasses() { runTest("build one aspect and two classes");}
	public void testBuildTwoClassesOneAspect() { runTest("build two classes and one aspect");}
	public void testBuildTwoAspects() { runTest("build two aspects");}
	
	public void testAspectExtendsClass() { runTest("aspect extends class"); }
	
	// verifying the type sorting
	/*public void testRecognizingAnnotationStyleAspects1() { 
		AjPipeliningCompilerAdapter.pipelineTesting=true;
		runTest("recognizing annotation style aspects - 1");
		
		String filesContainingAspects = AjPipeliningCompilerAdapter.getPipelineDebugOutput("filesContainingAspects");
		assertTrue("Should be one file containing aspects but it thinks there are "+filesContainingAspects,filesContainingAspects.equals("1"));
				
		String weaveOrder = AjPipeliningCompilerAdapter.getPipelineDebugOutput("weaveOrder");
		String expectedOrder="[AtAJAspect.java,ClassOne.java]";
		assertTrue("Expected weaving order to be "+expectedOrder+" but was "+weaveOrder,weaveOrder.equals(expectedOrder));
	} 
	public void testRecognizingAnnotationStyleAspects2() { 
		AjPipeliningCompilerAdapter.pipelineTesting=true;
		runTest("recognizing annotation style aspects - 2");

		String filesContainingAspects = AjPipeliningCompilerAdapter.getPipelineDebugOutput("filesContainingAspects");
		assertTrue("Should be one file containing aspects but it thinks there are "+filesContainingAspects,filesContainingAspects.equals("1"));
				
		String weaveOrder = AjPipeliningCompilerAdapter.getPipelineDebugOutput("weaveOrder");
		String expectedOrder="[AtInnerAJAspect.java,ClassOne.java]";
		assertTrue("Expected weaving order to be "+expectedOrder+" but was "+weaveOrder,weaveOrder.equals(expectedOrder));
	}*/
	
	// verifying the new code for transforming Eclipse Annotations into AspectJ ones
	public void testAnnotationTransformation() { runTest("annotation transformation"); }

  // --
  protected void tearDown() throws Exception {
		super.tearDown();
		//AjPipeliningCompilerAdapter.pipelineTesting=false;
  }
  public static Test suite() {
    return XMLBasedAjcTestCase.loadSuite(PipeliningTests.class);
  }
  protected File getSpecFile() {
    return new File("../tests/src/org/aspectj/systemtest/ajc153/pipelining.xml");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11026.java