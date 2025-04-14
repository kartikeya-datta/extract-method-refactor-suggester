error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4780.java
text:
```scala
V@@ersionedDataInputStream in = new VersionedDataInputStream(bi);

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


package org.aspectj.weaver.patterns;

import java.io.*;

import junit.framework.TestCase;

import org.aspectj.weaver.*;
import org.aspectj.weaver.bcel.BcelWorld;
import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.reflect.Factory;
import org.aspectj.util.FuzzyBoolean;

public class WithinTestCase extends TestCase {		

	World world = new BcelWorld();

	public WithinTestCase(String name) {
		super(name);
	}
	
	public void testMatch() throws IOException {
		Shadow getOutFromArrayList = new TestShadow(
			Shadow.FieldGet, 
			Member.fieldFromString("java.io.PrintStream java.lang.System.out"),
			TypeX.forName("java.util.ArrayList"),
			world);

		checkMatch(makePointcut("within(*)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.*)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.lang.*)"), getOutFromArrayList, FuzzyBoolean.NO);
		checkMatch(makePointcut("within(java.util.List+)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.uti*.List+)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.uti*..*)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.*List)"), getOutFromArrayList, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.List*)"), getOutFromArrayList, FuzzyBoolean.NO);
		
		
		Shadow getOutFromEntry = new TestShadow(
			Shadow.FieldGet, 
			Member.fieldFromString("java.io.PrintStream java.lang.System.out"),
			TypeX.forName("java.util.Map$Entry"),
			world);
			
		checkMatch(makePointcut("within(*)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.*)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.Map.*)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util..*)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.util.Map..*)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.lang.*)"), getOutFromEntry, FuzzyBoolean.NO);
		checkMatch(makePointcut("within(java.util.List+)"), getOutFromEntry, FuzzyBoolean.NO);
		checkMatch(makePointcut("within(java.util.Map+)"), getOutFromEntry, FuzzyBoolean.YES);
		checkMatch(makePointcut("within(java.lang.Object+)"), getOutFromEntry, FuzzyBoolean.YES);
		
		//this is something we should in type patterns tests
		//checkMatch(makePointcut("within(*List)"), getOut, FuzzyBoolean.NO);

	}

	
	public void testMatchJP() {
		Factory f = new Factory("WithinTestCase.java",WithinTestCase.class);
		
		JoinPoint.StaticPart inString = f.makeSJP(JoinPoint.CONSTRUCTOR_EXECUTION,f.makeConstructorSig(0,String.class,new Class[] {String.class},new String[]{"s"},new Class[0]),1);
		JoinPoint.StaticPart inObject = f.makeSJP(JoinPoint.CONSTRUCTOR_EXECUTION,f.makeConstructorSig(0,Object.class,new Class[] {},new String[]{},new Class[0]),1);

		Pointcut withinString = new PatternParser("within(String)").parsePointcut().resolve();
		Pointcut withinObject = new PatternParser("within(Object)").parsePointcut().resolve();
		Pointcut withinObjectPlus = new PatternParser("within(Object+)").parsePointcut().resolve();
		
		checkMatches(withinString,inString,FuzzyBoolean.YES);
		checkMatches(withinString,inObject,FuzzyBoolean.NO);
		checkMatches(withinObject,inString,FuzzyBoolean.NO);
		checkMatches(withinObject,inObject, FuzzyBoolean.YES);
		checkMatches(withinObjectPlus,inString,FuzzyBoolean.YES);
		checkMatches(withinObjectPlus,inObject,FuzzyBoolean.YES);
	}
	
	private void checkMatches(Pointcut p, JoinPoint.StaticPart jpsp, FuzzyBoolean expected) {
		assertEquals(expected,p.match(null,jpsp));
	}
	
	public Pointcut makePointcut(String pattern) {
		Pointcut pointcut0 = Pointcut.fromString(pattern);
		
		Bindings bindingTable = new Bindings(0);
        IScope scope = new SimpleScope(world, FormalBinding.NONE);
        
        pointcut0.resolveBindings(scope, bindingTable);		
		Pointcut pointcut1 = pointcut0;
		return pointcut1.concretize1(null, new IntMap());
	}

	
	private void checkMatch(Pointcut p, Shadow s, FuzzyBoolean shouldMatch) throws IOException {
		FuzzyBoolean doesMatch = p.match(s);
		assertEquals(p + " matches " + s, shouldMatch, doesMatch);
		checkSerialization(p);
	}
	
	private void checkSerialization(Pointcut p) throws IOException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bo);
		p.write(out);
		out.close();
		
		ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
		DataInputStream in = new DataInputStream(bi);
		Pointcut newP = Pointcut.read(in, null);
		
		assertEquals("write/read", p, newP);	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4780.java