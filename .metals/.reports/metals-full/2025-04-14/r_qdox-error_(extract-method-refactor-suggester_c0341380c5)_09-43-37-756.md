error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/791.java
text:
```scala
a@@ssertTrue("Couldnt find type Aspect",!rtx.isMissing());

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


package org.aspectj.weaver.bcel;

import java.io.IOException;
import java.util.Arrays;

import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AdviceKind;
import org.aspectj.weaver.MemberImpl;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;

public class TjpWeaveTestCase extends WeaveTestCase {
	{
		regenerate = false;
	}

	public TjpWeaveTestCase(String name) {
		super(name);
	}
    

    public void testStaticTjp() throws IOException {
    	BcelAdvice munger = new BcelAdvice(
    		AdviceKind.stringToKind("before"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static void Aspect.ajc_before(org.aspectj.lang.JoinPoint$StaticPart)"),
    		Advice.ThisJoinPointStaticPart, -1, -1, null, null);
    	
        weaveTest("HelloWorld", "StaticTjpBeforeHelloWorld", munger);
    } 
    
    
    public void testEnclosingStaticTjp() throws IOException {
    	BcelAdvice munger = new BcelAdvice(
    		AdviceKind.stringToKind("before"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static void Aspect.ajc_before(org.aspectj.lang.JoinPoint$StaticPart)"),
    		Advice.ThisEnclosingJoinPointStaticPart, -1, -1, null, null);
    	
        weaveTest("HelloWorld", "StaticEnclosingTjpBeforeHelloWorld", munger);
    } 


    public void testTjp() throws IOException {
    	BcelAdvice munger = new BcelAdvice(
    		AdviceKind.stringToKind("before"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static void Aspect.ajc_before(org.aspectj.lang.JoinPoint)"),
    		Advice.ThisJoinPoint, -1, -1, null, null);
    	
        weaveTest("HelloWorld", "TjpBeforeHelloWorld", munger);
    } 

    public void testAroundTjp() throws IOException {
    	BcelAdvice munger = new BcelAdvice(
    		AdviceKind.stringToKind("around"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static java.lang.Object Aspect.ajc_around(org.aspectj.runtime.internal.AroundClosure, org.aspectj.lang.JoinPoint)"),
    		Advice.ThisJoinPoint | Advice.ExtraArgument, -1, -1, null, null);
    	
        weaveTest("HelloWorld", "TjpAroundHelloWorld", munger);
    } 

    public void testAround2Tjp() throws IOException {
    	ResolvedType rtx = world.resolve(UnresolvedType.forName("Aspect"),true);
    	assertTrue("Couldnt find type Aspect",rtx!=ResolvedType.MISSING);
    	BcelAdvice munger1 = new BcelAdvice(
    		AdviceKind.stringToKind("around"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static java.lang.Object Aspect.ajc_around(org.aspectj.runtime.internal.AroundClosure, org.aspectj.lang.JoinPoint)"),
    		Advice.ThisJoinPoint | Advice.ExtraArgument, -1, -1, null, 
    		rtx);
    	
    	BcelAdvice munger2 = new BcelAdvice(
    		AdviceKind.stringToKind("around"),
    		makePointcutAll(), 
      		MemberImpl.methodFromString("static java.lang.Object Aspect.ajc_around(org.aspectj.runtime.internal.AroundClosure, org.aspectj.lang.JoinPoint)"),
    		Advice.ThisJoinPoint | Advice.ExtraArgument, -1, -1, null, 
    		rtx);
    	
        weaveTest("HelloWorld", "TjpAround2HelloWorld", Arrays.asList(new BcelAdvice[] {munger1, munger2}));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/791.java