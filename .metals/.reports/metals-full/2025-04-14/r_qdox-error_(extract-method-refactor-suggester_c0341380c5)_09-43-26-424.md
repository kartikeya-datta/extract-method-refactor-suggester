error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15756.java
text:
```scala
i@@f (p.couldMatchKinds()==Shadow.NO_SHADOW_KINDS_BITS) return MATCHES_NOTHING;

/* *******************************************************************
 * Copyright (c) 2004 IBM Corporation.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * ******************************************************************/
package org.aspectj.weaver.patterns;

import java.util.Comparator;

import org.aspectj.weaver.Shadow;

public class PointcutEvaluationExpenseComparator implements Comparator {

	private static final int MATCHES_NOTHING = -1;
	private static final int WITHIN = 1;
	private static final int ATWITHIN = 2;
	private static final int STATICINIT = 3;
	private static final int ADVICEEXECUTION = 4;
	private static final int HANDLER = 5;
	private static final int GET_OR_SET = 6;
	private static final int WITHINCODE = 7;
	private static final int ATWITHINCODE = 8;
	private static final int EXE_INIT_PREINIT = 9;
	private static final int CALL = 10;
	private static final int ANNOTATION = 11;
	private static final int THIS_OR_TARGET = 12;
	private static final int AT_THIS_OR_TARGET = 13;
	private static final int ARGS = 14;
	private static final int AT_ARGS = 15;
	private static final int CFLOW = 16;
	private static final int IF = 17;
	private static final int OTHER = 20;
	
	/**
	 * Compare 2 pointcuts based on an estimate of how expensive they may be
	 * to evaluate.
	 * 
	 * within
	 * @within
	 * staticinitialization  [make sure this has a fast match method]
	 * adviceexecution
	 * handler
	 * get, set
	 * withincode
	 * @withincode
	 * execution, initialization, preinitialization
	 * call
	 * @annotation
	 * this, target
	 * @this, @target
	 * args
	 * @args
	 * cflow, cflowbelow
	 * if
	 */
	public int compare(Object o1, Object o2) {
		Pointcut p1 = (Pointcut) o1;
		Pointcut p2 = (Pointcut) o2;
		
		// important property for a well-defined comparator
		if (p1.equals(p2)) return 0;		
		int result = getScore(p1) - getScore(p2);
		if (result == 0) {
			// they have the same evaluation expense, but are not 'equal'
			// sort by hashCode
			result = p1.hashCode() - p2.hashCode();
			if (result == 0) /*not allowed if ne*/ return -1;
		}
		return result;
	}

	// a higher score means a more expensive evaluation
	private int getScore(Pointcut p) {
		if (p.couldMatchKinds().isEmpty()) return MATCHES_NOTHING;
		if (p instanceof WithinPointcut) return WITHIN;
		if (p instanceof WithinAnnotationPointcut) return ATWITHIN;
		if (p instanceof KindedPointcut) {
			KindedPointcut kp = (KindedPointcut) p;
			Shadow.Kind kind = kp.getKind();
			if (kind == Shadow.AdviceExecution) {
				return ADVICEEXECUTION;
			} else if ((kind == Shadow.ConstructorCall) || (kind == Shadow.MethodCall)) {
				return CALL;
			} else if ((kind == Shadow.ConstructorExecution) || (kind == Shadow.MethodExecution) ||
					   (kind == Shadow.Initialization) || (kind == Shadow.PreInitialization)) {
				return EXE_INIT_PREINIT;
			} else if (kind == Shadow.ExceptionHandler) {
				return HANDLER;
			} else if ((kind == Shadow.FieldGet) || (kind == Shadow.FieldSet)) {
				return GET_OR_SET;
			} else if (kind == Shadow.StaticInitialization) {
				return STATICINIT;
			} else return OTHER;
 		}
		if (p instanceof AnnotationPointcut) return ANNOTATION;
		if (p instanceof ArgsPointcut) return ARGS;
		if (p instanceof ArgsAnnotationPointcut) return AT_ARGS;
		if (p instanceof CflowPointcut) return CFLOW;
		if (p instanceof HandlerPointcut) return HANDLER;
		if (p instanceof IfPointcut) return IF;
		if (p instanceof ThisOrTargetPointcut) return THIS_OR_TARGET;
		if (p instanceof ThisOrTargetAnnotationPointcut) return AT_THIS_OR_TARGET;
		if (p instanceof WithincodePointcut) return WITHINCODE;
		if (p instanceof WithinCodeAnnotationPointcut) return ATWITHINCODE;
		if (p instanceof NotPointcut) return getScore(((NotPointcut)p).getNegatedPointcut());
		if (p instanceof AndPointcut) return getScore(((AndPointcut)p).getLeft());
		if (p instanceof OrPointcut) return getScore(((OrPointcut)p).getLeft());
		return OTHER;		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15756.java