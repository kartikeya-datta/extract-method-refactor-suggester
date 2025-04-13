error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/538.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/538.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/538.java
text:
```scala
M@@emberImpl.method(declaringAspect, 0, "foo", "()V"),

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

import org.aspectj.weaver.patterns.*;
import org.aspectj.weaver.*;

/**.
 */
public class WeaveOrderTestCase extends WeaveTestCase {
	{
		regenerate = false;
	}

	public WeaveOrderTestCase(String name) {
		super(name);
	}
    

	public void testLexicalOrder() {
		Advice a1 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 1);
		Advice a2 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.THROWABLE, 2);
		
		assertEquals(-1, a2.compareTo(a1));
		assertEquals(+1, a1.compareTo(a2));
	}

	public void testLexicalOrderWithAfter() {
		Advice a1 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 1);
		Advice a2 =
			makeConcreteAdvice(AdviceKind.After, UnresolvedType.OBJECT, UnresolvedType.THROWABLE, 2);
		
		assertEquals(+1, a2.compareTo(a1));
		assertEquals(-1, a1.compareTo(a2));

		a1 =
			makeConcreteAdvice(AdviceKind.After, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 1);
		a2 =
			makeConcreteAdvice(AdviceKind.After, UnresolvedType.OBJECT, UnresolvedType.THROWABLE, 2);
		
		assertEquals(+1, a2.compareTo(a1));
		assertEquals(-1, a1.compareTo(a2));
	}
	
	public void testSubtypes() {
		Advice a1 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 1);
		Advice a2 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.THROWABLE, UnresolvedType.OBJECT, 1);
		Advice a3 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.forName("java.lang.String"), UnresolvedType.OBJECT, 1);
			
		assertEquals(+1, a2.compareTo(a1));
		assertEquals(-1, a1.compareTo(a2));

		assertEquals(+1, a3.compareTo(a1));
		assertEquals(-1, a1.compareTo(a3));

		assertEquals(0, a3.compareTo(a2));
		assertEquals(0, a2.compareTo(a3));
	}


	public void testDominates() {
		Declare dom =
			new PatternParser("declare precedence: java.lang.String, java.lang.Throwable").parseDeclare();
		//??? concretize dom
		ResolvedType aType =  world.resolve("Aspect");
		CrosscuttingMembers xcut = new CrosscuttingMembers(aType);
		aType.crosscuttingMembers = xcut;
		xcut.addDeclare(dom);
		world.getCrosscuttingMembersSet().addFixedCrosscuttingMembers(aType);
		
		Advice a1 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 1);
		Advice a2 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.THROWABLE, 2);
		Advice a3 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.forName("java.lang.String"), 2);
		
		assertEquals(-1, a2.compareTo(a1));
		assertEquals(+1, a1.compareTo(a2));

		assertEquals(-1, a3.compareTo(a1));
		assertEquals(+1, a1.compareTo(a3));
		
		
		assertEquals(+1, a3.compareTo(a2));
		assertEquals(-1, a2.compareTo(a3));
	}
	
	public void testDominatesHarder() {
		Declare dom =
			new PatternParser("declare precedence: *, java.lang.String, java.lang.Throwable").parseDeclare();
		//??? concretize dom
		ResolvedType aType =  world.resolve("Aspect");
		CrosscuttingMembers xcut = new CrosscuttingMembers(aType);
		aType.crosscuttingMembers = xcut;
		xcut.addDeclare(dom);
		world.getCrosscuttingMembersSet().addFixedCrosscuttingMembers(aType);
		
		Advice a1 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.OBJECT, 2);
		Advice a2 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.THROWABLE, 1);
		Advice a3 =
			makeConcreteAdvice(AdviceKind.Before, UnresolvedType.OBJECT, UnresolvedType.forName("java.lang.String"), 1);
		
		assertEquals(-1, a2.compareTo(a1));
		assertEquals(+1, a1.compareTo(a2));

		assertEquals(-1, a3.compareTo(a1));
		assertEquals(+1, a1.compareTo(a3));
		
		
		assertEquals(+1, a3.compareTo(a2));
		assertEquals(-1, a2.compareTo(a3));
	}
	
	


	private Advice makeConcreteAdvice(AdviceKind kind, UnresolvedType declaringAspect, 
				UnresolvedType concreteAspect, int lexicalPosition)
	{
		Advice a1 = new BcelAdvice(kind, makeResolvedPointcut("this(*)"),  
				Member.method(declaringAspect, 0, "foo", "()V"),
				0, lexicalPosition, lexicalPosition, null, null);
		a1 = (Advice)a1.concretize(concreteAspect.resolve(world), world, null);
		return a1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/538.java