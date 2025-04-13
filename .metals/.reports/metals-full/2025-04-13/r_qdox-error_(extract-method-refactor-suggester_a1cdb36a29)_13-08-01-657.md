error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10564.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10564.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10564.java
text:
```scala
C@@ompilationResult cR = binaryWeave("testcode.jar","AnnotationAspect04.aj",3,0,true,new String[]{"-source","1.5"});

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

import org.aspectj.bridge.IMessage;
import org.aspectj.tools.ajc.CompilationResult;


/**
 * Annotations, the rules/tests:
 * 
 * 1. cannot make ITD (C,M or F) on an annotation
 * 2. cannot use declare parents to change the super type of an annotation
 * 3. cannot use decp to make an annotation type implement an interface
 * 4. cannot use decp to dec java.lang.annotation.Annotation as the parent of any type
 * 5. cannot extend set of values in an annotation via an ITD like construct
 * 6. Compilation error if you explicitly identify an Annotation type.  
 * 7. Lint warning if a non-explicit type pattern would match an annotation type.
 */
public class Annotations extends TestUtils {
	
  protected void setUp() throws Exception {
	super.setUp();
	baseDir = new File("../tests/java5/annotations");
  }
	
  // Cannot make ITD (c/m/f) on an annotation
  public void test001_itdsOnAnnotationsNotAllowed() {
  	CompilationResult cR = binaryWeave("testcode.jar","AnnotationAspect01.aj",3,0);
  	assertTrue("Expected three message about ITDs not allowed on Annotations but got: #"+
  			cR.getErrorMessages().size()+": \n"+cR.getErrorMessages(),
  			cR.getErrorMessages().size()==3);
  	IMessage msg1_ctor   = (IMessage)cR.getErrorMessages().get(0);
  	IMessage msg2_method = (IMessage)cR.getErrorMessages().get(1);
  	IMessage msg3_field  = (IMessage)cR.getErrorMessages().get(2);
  	assertTrue("Expected message about ITDCs on annotations not allowed, but got: \n"+msg1_ctor,
  			msg1_ctor.toString().indexOf("can't make inter-type constructor declarations")!=-1);
  	assertTrue("Expected message about ITDMs on annotations not allowed, but got: \n"+msg2_method,
  			msg2_method.toString().indexOf("can't make inter-type method declarations")!=-1);
  	assertTrue("Expected message about ITDFs on annotations not allowed, but got: \n"+msg3_field,
  			msg3_field.toString().indexOf("can't make inter-type field declarations")!=-1);
  	verifyWeavingMessagesOutput(cR,new String[]{});
  }
  
  // Deals with the cases where an explicit type is specified and it is an annotation type
  public void test002_decpOnAnnotationNotAllowed_errors() {
  	CompilationResult cR = binaryWeave("testcode.jar","AnnotationAspect04.aj",3,0,true);
  	IMessage msg = (IMessage)cR.getErrorMessages().get(1);
  	assertTrue("Expected a message about can't use decp to alter supertype of an annotation: "+msg,
  			msg.toString().indexOf("to alter supertype of annotation type")!=-1);
  	msg = (IMessage)cR.getErrorMessages().get(2);
  	assertTrue("Expected a message about can't use decp to make annotation implement interface: "+msg,
  			msg.toString().indexOf("implement an interface")!=-1);
  	msg = (IMessage)cR.getErrorMessages().get(0);
  	assertTrue("Expected a message about can't use decp to make Annotation parent of another type: "+msg,
  			msg.toString().indexOf("the parent of type")!=-1);
  	verifyWeavingMessagesOutput(cR,new String[]{});
  }
  
  //Deals with the cases where an wild type pattern is specified and it hits an annotation type
  public void test004_decpOnAnnotationNotAllowed_xlints() {
  	CompilationResult cR = binaryWeave("testcode.jar","AnnotationAspect05.aj",0,2,false);
  	IMessage msg = (IMessage)cR.getWarningMessages().get(0);
  	assertTrue("Expected a message about an annotation type matching a declare parents but being ignored: "+msg,
  			msg.toString().indexOf("matches a declare parents type pattern")!=-1);
  	msg = (IMessage)cR.getWarningMessages().get(1);
  	assertTrue("Expected a message about an annotation type matching a declare parents but being ignored: "+msg,
  			msg.toString().indexOf("matches a declare parents type pattern")!=-1);
  	verifyWeavingMessagesOutput(cR,new String[]{});
  }
  
  // TODO extra tests:
  // declare parents with annotation pattern
  // declare soft with annotation pattern
  // declare warning with annotation pattern
  // declare precedence with annotation pattern
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10564.java