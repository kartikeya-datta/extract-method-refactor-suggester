error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6472.java
text:
```scala
?@@ searchLineNumber(unitResult.getLineSeparatorPositions(), problemStartPosition)

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.problem;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;

/*
 * Compiler error handler, responsible to determine whether
 * a problem is actually a warning or an error; also will
 * decide whether the compilation task can be processed further or not.
 *
 * Behavior : will request its current policy if need to stop on
 *	first error, and if should proceed (persist) with problems.
 */

public class ProblemHandler implements ProblemSeverities {

	public final static String[] NoArgument = new String[0];
	
	final public IErrorHandlingPolicy policy;
	public final IProblemFactory problemFactory;
	public final CompilerOptions options;
/*
 * Problem handler can be supplied with a policy to specify
 * its behavior in error handling. Also see static methods for
 * built-in policies.
 *
 */
public ProblemHandler(IErrorHandlingPolicy policy, CompilerOptions options, IProblemFactory problemFactory) {
	this.policy = policy;
	this.problemFactory = problemFactory;
	this.options = options;
}
/*
 * Given the current configuration, answers which category the problem
 * falls into:
 *		Error | Warning | Ignore
 */
public int computeSeverity(int problemId){
	
	return Error; // by default all problems are errors
}
public IProblem createProblem(
	char[] fileName, 
	int problemId, 
	String[] problemArguments, 
	String[] messageArguments,
	int severity, 
	int problemStartPosition, 
	int problemEndPosition, 
	int lineNumber) {

	return this.problemFactory.createProblem(
		fileName, 
		problemId, 
		problemArguments, 
		messageArguments,
		severity, 
		problemStartPosition, 
		problemEndPosition, 
		lineNumber); 
}
public void handle(
	int problemId, 
	String[] problemArguments, 
	String[] messageArguments,
	int severity, 
	int problemStartPosition, 
	int problemEndPosition, 
	ReferenceContext referenceContext, 
	CompilationResult unitResult) {

	if (severity == Ignore)
		return;

	// if no reference context, we need to abort from the current compilation process
	if (referenceContext == null) {
		if ((severity & Error) != 0) { // non reportable error is fatal
			IProblem problem = this.createProblem(null, 	problemId, 	problemArguments, messageArguments, severity, 0, 0, 0);			
			throw new AbortCompilation(null, problem);
		} else {
			return; // ignore non reportable warning
		}
	}

	IProblem problem = 
		this.createProblem(
			unitResult.getFileName(), 
			problemId, 
			problemArguments, 
			messageArguments,
			severity, 
			problemStartPosition, 
			problemEndPosition, 
			problemStartPosition >= 0
				? searchLineNumber(unitResult.lineSeparatorPositions, problemStartPosition)
				: 0);
	if (problem == null) return; // problem couldn't be created, ignore
	
	switch (severity & Error) {
		case Error :
			this.record(problem, unitResult, referenceContext);
			referenceContext.tagAsHavingErrors();

			// should abort ?
			int abortLevel;
			if ((abortLevel = 
				(this.policy.stopOnFirstError() ? AbortCompilation : severity & Abort)) != 0) {

				referenceContext.abort(abortLevel, problem);
			}
			break;
		case Warning :
			this.record(problem, unitResult, referenceContext);
			break;
	}
}
/**
 * Standard problem handling API, the actual severity (warning/error/ignore) is deducted
 * from the problem ID and the current compiler options.
 */
public void handle(
	int problemId, 
	String[] problemArguments, 
	String[] messageArguments,
	int problemStartPosition, 
	int problemEndPosition, 
	ReferenceContext referenceContext, 
	CompilationResult unitResult) {

	this.handle(
		problemId,
		problemArguments,
		messageArguments,
		this.computeSeverity(problemId), // severity inferred using the ID
		problemStartPosition,
		problemEndPosition,
		referenceContext,
		unitResult);
}
public void record(IProblem problem, CompilationResult unitResult, ReferenceContext referenceContext) {
	unitResult.record(problem, referenceContext);
}
/**
 * Search the line number corresponding to a specific position
 */
public static final int searchLineNumber(int[] startLineIndexes, int position) {
	if (startLineIndexes == null)
		return 1;
	int length = startLineIndexes.length;
	if (length == 0)
		return 1;
	int g = 0, d = length - 1;
	int m = 0;
	while (g <= d) {
		m = (g + d) /2;
		if (position < startLineIndexes[m]) {
			d = m-1;
		} else if (position > startLineIndexes[m]) {
			g = m+1;
		} else {
			return m + 1;
		}
	}
	if (position < startLineIndexes[m]) {
		return m+1;
	}
	return m+2;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6472.java