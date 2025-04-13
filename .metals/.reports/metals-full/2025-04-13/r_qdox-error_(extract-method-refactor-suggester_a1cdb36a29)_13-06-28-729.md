error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6670.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6670.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6670.java
text:
```scala
i@@f (newProblems[i].getID() != IProblem.Task)//TODO: (kent) aren't tasks filtered out from problems here?

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.core.Util;

public class BuildNotifier {

protected IProgressMonitor monitor;
protected boolean cancelling;
protected float percentComplete;
protected float progressPerCompilationUnit;
protected int newErrorCount;
protected int fixedErrorCount;
protected int newWarningCount;
protected int fixedWarningCount;
protected int workDone;
protected int totalWork;
protected String previousSubtask;

public BuildNotifier(IProgressMonitor monitor, IProject project) {
	this.monitor = monitor;
	this.cancelling = false;
	this.newErrorCount = 0;
	this.fixedErrorCount = 0;
	this.newWarningCount = 0;
	this.fixedWarningCount = 0;
	this.workDone = 0;
	this.totalWork = 1000000;
}

/**
 * Notification before a compile that a unit is about to be compiled.
 */
public void aboutToCompile(SourceFile unit) {
	String message = Util.bind("build.compiling", unit.resource.getFullPath().removeLastSegments(1).makeRelative().toString()); //$NON-NLS-1$
	subTask(message);
}

public void begin() {
	if (monitor != null)
		monitor.beginTask("", totalWork); //$NON-NLS-1$
	this.previousSubtask = null;
}

/**
 * Check whether the build has been canceled.
 */
public void checkCancel() {
	if (monitor != null && monitor.isCanceled())
		throw new OperationCanceledException();
}

/**
 * Check whether the build has been canceled.
 * Must use this call instead of checkCancel() when within the compiler.
 */
public void checkCancelWithinCompiler() {
	if (monitor != null && monitor.isCanceled() && !cancelling) {
		// Once the compiler has been canceled, don't check again.
		setCancelling(true);
		// Only AbortCompilation can stop the compiler cleanly.
		// We check cancelation again following the call to compile.
		throw new AbortCompilation(true, null); 
	}
}

/**
 * Notification while within a compile that a unit has finished being compiled.
 */
public void compiled(SourceFile unit) {
	String message = Util.bind("build.compiling", unit.resource.getFullPath().removeLastSegments(1).makeRelative().toString()); //$NON-NLS-1$
	subTask(message);
	updateProgressDelta(progressPerCompilationUnit);
	checkCancelWithinCompiler();
}

public void done() {
	updateProgress(1.0f);
	subTask(Util.bind("build.done")); //$NON-NLS-1$
	if (monitor != null)
		monitor.done();
	this.previousSubtask = null;
}

/**
 * Returns a string describing the problems.
 */
protected String problemsMessage() {
	int numNew = newErrorCount + newWarningCount;
	int numFixed = fixedErrorCount + fixedWarningCount;
	if (numNew == 0 && numFixed == 0) return ""; //$NON-NLS-1$
	if (numFixed == 0)
		return '(' + (numNew == 1
			? Util.bind("build.oneProblemFound", String.valueOf(numNew)) //$NON-NLS-1$
			: Util.bind("build.problemsFound", String.valueOf(numNew))) + ')'; //$NON-NLS-1$
	if (numNew == 0)
		return '(' + (numFixed == 1
			? Util.bind("build.oneProblemFixed", String.valueOf(numFixed)) //$NON-NLS-1$
			: Util.bind("build.problemsFixed", String.valueOf(numFixed))) + ')'; //$NON-NLS-1$
	return
		'(' + (numFixed == 1
			? Util.bind("build.oneProblemFixed", String.valueOf(numFixed)) //$NON-NLS-1$
			: Util.bind("build.problemsFixed", String.valueOf(numFixed))) //$NON-NLS-1$
		+ ", " //$NON-NLS-1$
		+ (numNew == 1
			? Util.bind("build.oneProblemFound", String.valueOf(numNew)) //$NON-NLS-1$
			: Util.bind("build.problemsFound", String.valueOf(numNew))) + ')'; //$NON-NLS-1$
}

/**
 * Sets the cancelling flag, which indicates we are in the middle
 * of being cancelled.  Certain places (those callable indirectly from the compiler)
 * should not check cancel again while this is true, to avoid OperationCanceledException
 * being thrown at an inopportune time.
 */
public void setCancelling(boolean cancelling) {
	this.cancelling = cancelling;
}

/**
 * Sets the amount of progress to report for compiling each compilation unit.
 */
public void setProgressPerCompilationUnit(float progress) {
	this.progressPerCompilationUnit = progress;
}

public void subTask(String message) {
	String pm = problemsMessage();
	String msg = pm.length() == 0 ? message : pm + " " + message; //$NON-NLS-1$

	if (msg.equals(this.previousSubtask)) return; // avoid refreshing with same one
	//if (JavaBuilder.DEBUG) System.out.println(msg);
	if (monitor != null)
		monitor.subTask(msg);

	this.previousSubtask = msg;
}

protected void updateProblemCounts(IProblem[] newProblems) {
	for (int i = 0, l = newProblems.length; i < l; i++)
		if (newProblems[i].getID() != IProblem.Task)
			if (newProblems[i].isError()) newErrorCount++; else newWarningCount++;
}

/**
 * Update the problem counts from one compilation result given the old and new problems,
 * either of which may be null.
 */
protected void updateProblemCounts(IMarker[] oldProblems, IProblem[] newProblems) {
	if (newProblems != null) {
		next : for (int i = 0, l = newProblems.length; i < l; i++) {
			IProblem newProblem = newProblems[i];
			if (newProblem.getID() == IProblem.Task) continue; // skip task
			boolean isError = newProblem.isError();
			String message = newProblem.getMessage();

			if (oldProblems != null) {
				for (int j = 0, m = oldProblems.length; j < m; j++) {
					IMarker pb = oldProblems[j];
					if (pb == null) continue; // already matched up with a new problem
					boolean wasError = IMarker.SEVERITY_ERROR
						== pb.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					if (isError == wasError && message.equals(pb.getAttribute(IMarker.MESSAGE, ""))) { //$NON-NLS-1$
						oldProblems[j] = null;
						continue next;
					}
				}
			}
			if (isError) newErrorCount++; else newWarningCount++;
		}
	}
	if (oldProblems != null) {
		next : for (int i = 0, l = oldProblems.length; i < l; i++) {
			IMarker oldProblem = oldProblems[i];
			if (oldProblem == null) continue next; // already matched up with a new problem
			boolean wasError = IMarker.SEVERITY_ERROR
				== oldProblem.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			String message = oldProblem.getAttribute(IMarker.MESSAGE, ""); //$NON-NLS-1$

			if (newProblems != null) {
				for (int j = 0, m = newProblems.length; j < m; j++) {
					IProblem pb = newProblems[j];
					if (pb.getID() == IProblem.Task) continue; // skip task
					if (wasError == pb.isError() && message.equals(pb.getMessage()))
						continue next;
				}
			}
			if (wasError) fixedErrorCount++; else fixedWarningCount++;
		}
	}
}

public void updateProgress(float percentComplete) {
	if (percentComplete > this.percentComplete) {
		this.percentComplete = Math.min(percentComplete, 1.0f);
		int work = Math.round(this.percentComplete * this.totalWork);
		if (work > this.workDone) {
			if (monitor != null)
				monitor.worked(work - this.workDone);
			//if (JavaBuilder.DEBUG)
				//System.out.println(java.text.NumberFormat.getPercentInstance().format(this.percentComplete));
			this.workDone = work;
		}
	}
}

public void updateProgressDelta(float percentWorked) {
	updateProgress(percentComplete + percentWorked);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6670.java