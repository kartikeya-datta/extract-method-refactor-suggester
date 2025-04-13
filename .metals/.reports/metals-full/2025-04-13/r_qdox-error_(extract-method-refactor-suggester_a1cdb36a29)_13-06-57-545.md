error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4819.java
text:
```scala
private static final b@@oolean DUMP_STACK = true;

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

 
 package org.aspectj.ajdt.internal.compiler.problem;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.aspectj.ajdt.internal.compiler.ast.*;
import org.aspectj.ajdt.internal.compiler.ast.Proceed;
import org.aspectj.ajdt.internal.compiler.lookup.EclipseWorld;
import org.aspectj.util.FuzzyBoolean;
import org.aspectj.weaver.*;
import org.aspectj.weaver.Shadow;
import org.aspectj.weaver.patterns.DeclareSoft;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

/**
 * Extends problem reporter to support compiler-side implementation of declare soft. 
 * Also overrides error reporting for the need to implement abstract methods to
 * account for inter-type declarations and pointcut declarations.  This second
 * job might be better done directly in the SourceTypeBinding/ClassScope classes.
 * 
 * @author Jim Hugunin
 */
public class AjProblemReporter extends ProblemReporter {
	private static final boolean DUMP_STACK = false;
	
	public EclipseWorld world;

	public AjProblemReporter(
		IErrorHandlingPolicy policy,
		CompilerOptions options,
		IProblemFactory problemFactory) {
		super(policy, options, problemFactory);
	}
	
	

	public void unhandledException(
		TypeBinding exceptionType,
		AstNode location)
	{
		if (!world.getDeclareSoft().isEmpty()) {
			Shadow callSite = world.makeShadow(location, referenceContext);
			if (callSite == null) {
				super.unhandledException(exceptionType, location);
				return;
			}
			Shadow enclosingExec = world.makeShadow(referenceContext);
	//		System.err.println("about to show error for unhandled exception: "  + exceptionType + 
	//				" at " + location + " in " + referenceContext);
			
			
			for (Iterator i = world.getDeclareSoft().iterator(); i.hasNext(); ) {
				DeclareSoft d = (DeclareSoft)i.next();
				FuzzyBoolean match = d.getPointcut().match(callSite);
				if (match.alwaysTrue()) {
					//System.err.println("matched callSite: "  + callSite + " with " + d);
					return;
				} else if (!match.alwaysFalse()) {
					throw new RuntimeException("unimplemented, shouldn't have fuzzy match here");
				}
				
				match = d.getPointcut().match(enclosingExec);
				if (match.alwaysTrue()) {
					//System.err.println("matched enclosingExec: "  + enclosingExec + " with " + d);
					return;
				} else if (!match.alwaysFalse()) {
					throw new RuntimeException("unimplemented, shouldn't have fuzzy match here");
				}
			}
		}
		
		//??? is this always correct
		if (location instanceof Proceed) {
			return;
		}

		super.unhandledException(exceptionType, location);
	}

	private boolean isPointcutDeclaration(MethodBinding binding) {
		return CharOperation.startsWith(binding.selector, PointcutDeclaration.mangledPrefix);
	}

	public void abstractMethodCannotBeOverridden(
		SourceTypeBinding type,
		MethodBinding concreteMethod)
	{
		if (isPointcutDeclaration(concreteMethod)) {
			return;
		}
		super.abstractMethodCannotBeOverridden(type, concreteMethod);
	}



	public void abstractMethodMustBeImplemented(
		SourceTypeBinding type,
		MethodBinding abstractMethod)
	{
		// if this is a PointcutDeclaration then there is no error
		if (isPointcutDeclaration(abstractMethod)) {
			return;
		}
		
		
		// if we implemented this method by an inter-type declaration, then there is no error
		//??? be sure this is always right
		ResolvedTypeX onTypeX = world.fromEclipse(type); //abstractMethod.declaringClass);
		for (Iterator i = onTypeX.getInterTypeMungers().iterator(); i.hasNext(); ) {
			ConcreteTypeMunger m = (ConcreteTypeMunger)i.next();
			if (m.matches(onTypeX)) {
				ResolvedMember sig = m.getSignature();
				if (Modifier.isPublic(sig.getModifiers()) && !Modifier.isAbstract(sig.getModifiers())) {
					if (ResolvedTypeX.matches(sig, world.makeResolvedMember(abstractMethod))) {
						return;
					}
				}
			}
		}

		super.abstractMethodMustBeImplemented(type, abstractMethod);
	}

	public void handle(
		int problemId,
		String[] problemArguments,
		int severity,
		int problemStartPosition,
		int problemEndPosition,
		ReferenceContext referenceContext,
		CompilationResult unitResult) {
			
		if (severity != Ignore && DUMP_STACK) {
			Thread.currentThread().dumpStack();
		}
		super.handle(
			problemId,
			problemArguments,
			severity,
			problemStartPosition,
			problemEndPosition,
			referenceContext,
			unitResult);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4819.java