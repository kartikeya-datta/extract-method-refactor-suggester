error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[75,2]

error in qdox parser
file content:
```java
offset: 2960
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6652.java
text:
```scala
//System.err.println(cR.getStandardError());

/*******************************************************************************
 * Copyright (c) 2004 IBM 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *    Andy Clement - initial API and implementation
 *******************************************************************************/
package org.aspectj.systemtest.ajc150;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.tools.ajc.CompilationResult;

public class Annotations extends TestUtils {
	
  protected void setUp() throws Exception {
	super.setUp();
	baseDir = new File("../tests/java5/annotations");
  }
  
  public void testCompilingAnnotation() {
  	CompilationResult cR = ajc(baseDir,new String[]{"SimpleAnnotation.java","-1.5"});
  	MessageSpec ms = new MessageSpec(null,null);
  	assertMessages(cR,ms);
  }
  
  public void testCompilingAnnotatedFile() {
  	CompilationResult cR = ajc(baseDir,new String[]{"AnnotatedType.java","SimpleAnnotation.java","-1.5"});
  	MessageSpec ms = new MessageSpec(null,null);
  	assertMessages(cR,ms);
  }
  
  public void testCompilingUsingWithinAndAnnotationTypePattern() {
  	CompilationResult cR = ajc(new File(baseDir+File.separator+"within"),
  			new String[]{"PlainWithin.java","PlainWithinTests.java","-1.5"});
  	List expectedInfoMessages = new ArrayList();
  	expectedInfoMessages.add(new Message(21,"positive within match on annotation"));
	expectedInfoMessages.add(new Message(25,"negative within match on annotation"));
  	MessageSpec ms = new MessageSpec(expectedInfoMessages,null);
  	assertMessages(cR,ms);
  }
  
  /**
   * We had a bug where annotations were not present in the output class file for methods
   * that got woven.  This was due to unpacking bugs in LazyMethodGen.  This test compiles
   * a simple program then checks the annotations were copied across.
   */
  public void testBugWithAnnotationsLostOnWovenMethods() throws ClassNotFoundException {
  	CompilationResult cR = ajc(new File(baseDir+File.separator+"attarget"),
  			new String[]{"Program.java","AtTargetAspect.java","-1.5"});
  	System.err.println(cR.getStandardError());
  	List expectedInfoMessages = new ArrayList();
  	MessageSpec ms = new MessageSpec(null,null);
  	assertMessages(cR,ms);
  	
  	JavaClass jc = getClassFrom(ajc.getSandboxDirectory(),"Program");
    Method[] meths = jc.getMethods();
    for (int i = 0; i < meths.length; i++) {
		Method method = meths[i];
		if (method.getName().equals("m1")) {
			assertTrue("Didn't have annotations - were they lost? method="+method.getName(),method.getAnnotations().length==1);
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6652.java