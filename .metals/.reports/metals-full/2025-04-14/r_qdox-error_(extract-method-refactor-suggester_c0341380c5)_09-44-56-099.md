error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6711.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6711.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6711.java
text:
```scala
i@@nt mid = computePriority(problemList[left + (right - left) / 2]);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler;

/**
 * A compilation result consists of all information returned by the compiler for 
 * a single compiled compilation source unit.  This includes:
 * <ul>
 * <li> the compilation unit that was compiled
 * <li> for each type produced by compiling the compilation unit, its binary and optionally its principal structure
 * <li> any problems (errors or warnings) produced
 * <li> dependency info
 * </ul>
 *
 * The principle structure and binary may be null if the compiler could not produce them.
 * If neither could be produced, there is no corresponding entry for the type.
 *
 * The dependency info includes type references such as supertypes, field types, method
 * parameter and return types, local variable types, types of intermediate expressions, etc.
 * It also includes the namespaces (packages) in which names were looked up.
 * It does <em>not</em> include finer grained dependencies such as information about
 * specific fields and methods which were referenced, but does contain their 
 * declaring types and any other types used to locate such fields or methods.
 */
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.parser.RecoveryScannerData;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.Util;

public class CompilationResult {
	
	public CategorizedProblem problems[];
	public CategorizedProblem tasks[];
	public int problemCount;
	public int taskCount;
	public ICompilationUnit compilationUnit;
	private Map problemsMap;
	private Set firstErrors;
	private int maxProblemPerUnit;
	public char[][][] qualifiedReferences;
	public char[][] simpleNameReferences;
	public boolean hasAnnotations = false;
	public int lineSeparatorPositions[];
	public RecoveryScannerData recoveryScannerData;
	public Map compiledTypes = new Hashtable(11);
	public int unitIndex, totalUnitsKnown;
	public boolean hasBeenAccepted = false;
	public char[] fileName;
	public boolean hasInconsistentToplevelHierarchies = false; // record the fact some toplevel types have inconsistent hierarchies
	public boolean hasSyntaxError = false;
	long[] suppressWarningIrritants;  // irritant for suppressed warnings
	long[] suppressWarningScopePositions; // (start << 32) + end 
	int suppressWarningsCount;
	public char[][] packageName;
	
private static final int[] EMPTY_LINE_ENDS = Util.EMPTY_INT_ARRAY;
private static final Comparator PROBLEM_COMPARATOR = new Comparator() {
	public int compare(Object o1, Object o2) {
		return ((CategorizedProblem) o1).getSourceStart() - ((CategorizedProblem) o2).getSourceStart();
	}
};

public CompilationResult(
		char[] fileName,
		int unitIndex, 
		int totalUnitsKnown,
		int maxProblemPerUnit){

	this.fileName = fileName;
	this.unitIndex = unitIndex;
	this.totalUnitsKnown = totalUnitsKnown;
	this.maxProblemPerUnit = maxProblemPerUnit;
}

public CompilationResult(
		ICompilationUnit compilationUnit,
		int unitIndex, 
		int totalUnitsKnown,
		int maxProblemPerUnit){

	this.fileName = compilationUnit.getFileName();
	this.compilationUnit = compilationUnit;
	this.unitIndex = unitIndex;
	this.totalUnitsKnown = totalUnitsKnown;
	this.maxProblemPerUnit = maxProblemPerUnit;
}

private int computePriority(CategorizedProblem problem){
	final int P_STATIC = 10000;
	final int P_OUTSIDE_METHOD = 40000;
	final int P_FIRST_ERROR = 20000;
	final int P_ERROR = 100000;
	
	int priority = 10000 - problem.getSourceLineNumber(); // early problems first
	if (priority < 0) priority = 0;
	if (problem.isError()){
		priority += P_ERROR;
	}
	ReferenceContext context = this.problemsMap == null ? null : (ReferenceContext) this.problemsMap.get(problem);
	if (context != null){
		if (context instanceof AbstractMethodDeclaration){
			AbstractMethodDeclaration method = (AbstractMethodDeclaration) context;
			if (method.isStatic()) {
				priority += P_STATIC;
			}
		} else {
			priority += P_OUTSIDE_METHOD;
		}
		if (this.firstErrors.contains(problem)){ // if context is null, firstErrors is null too
		  priority += P_FIRST_ERROR;
	    }
	} else {
		priority += P_OUTSIDE_METHOD;
	}
	return priority;
}

public void discardSuppressedWarnings() {
	if (this.suppressWarningsCount == 0) return;
	int removed = 0;
	nextProblem: for (int i = 0, length = this.problemCount; i < length; i++) {
		CategorizedProblem problem = this.problems[i];
		int problemID = problem.getID();
		if (!problem.isWarning()) {
			continue nextProblem;
		}
		int start = problem.getSourceStart();
		int end = problem.getSourceEnd();
		nextSuppress: for (int j = 0, max = this.suppressWarningsCount; j < max; j++) {
			long position = this.suppressWarningScopePositions[j];
			int startSuppress = (int) (position >>> 32);
			int endSuppress = (int) position;
			if (start < startSuppress) continue nextSuppress;
			if (end > endSuppress) continue nextSuppress;
			if ((ProblemReporter.getIrritant(problemID) & this.suppressWarningIrritants[j]) == 0)
				continue nextSuppress;
			// discard suppressed warning
			removed++;
			this.problems[i] = null;
			if (this.problemsMap != null) this.problemsMap.remove(problem);
			if (this.firstErrors != null) this.firstErrors.remove(problem);
			continue nextProblem;
		}
	}
	if (removed > 0) {
		for (int i = 0, index = 0; i < this.problemCount; i++) {
			CategorizedProblem problem;
			if ((problem = this.problems[i]) != null) {
				if (i > index) {
					this.problems[index++] = problem;
				} else {
					index++;
				}
			}
		}
		this.problemCount -= removed;
	}
}

public CategorizedProblem[] getAllProblems() {
	CategorizedProblem[] onlyProblems = this.getProblems();
	int onlyProblemCount = onlyProblems != null ? onlyProblems.length : 0;
	CategorizedProblem[] onlyTasks = this.getTasks();
	int onlyTaskCount = onlyTasks != null ? onlyTasks.length : 0;
	if (onlyTaskCount == 0) {
		return onlyProblems;
	}
	if (onlyProblemCount == 0) {
		return onlyTasks;
	}

	int totalNumberOfProblem = onlyProblemCount + onlyTaskCount;
	CategorizedProblem[] allProblems = new CategorizedProblem[totalNumberOfProblem];
	int allProblemIndex = 0;
	int taskIndex = 0;
	int problemIndex = 0;
	while (taskIndex + problemIndex < totalNumberOfProblem) {
		CategorizedProblem nextTask = null;
		CategorizedProblem nextProblem = null;
		if (taskIndex < onlyTaskCount) {
			nextTask = onlyTasks[taskIndex];
		}
		if (problemIndex < onlyProblemCount) {
			nextProblem = onlyProblems[problemIndex];
		}
		// select the next problem
		CategorizedProblem currentProblem = null;
		if (nextProblem != null) {
			if (nextTask != null) {
				if (nextProblem.getSourceStart() < nextTask.getSourceStart()) {
					currentProblem = nextProblem;
					problemIndex++;
				} else {
					currentProblem = nextTask;
					taskIndex++;
				}
			} else {
				currentProblem = nextProblem;
				problemIndex++;
			}
		} else {
			if (nextTask != null) {
				currentProblem = nextTask;
				taskIndex++;
			}
		}
		allProblems[allProblemIndex++] = currentProblem;
	}
	return allProblems;
}

public ClassFile[] getClassFiles() {
	ClassFile[] classFiles = new ClassFile[this.compiledTypes.size()];
	this.compiledTypes.values().toArray(classFiles);
	return classFiles;	
}

/**
 * Answer the initial compilation unit corresponding to the present compilation result
 */
public ICompilationUnit getCompilationUnit(){
	return this.compilationUnit;
}

/**
 * Answer the errors encountered during compilation.
 */
public CategorizedProblem[] getErrors() {
	CategorizedProblem[] reportedProblems = getProblems();
	int errorCount = 0;
	for (int i = 0; i < this.problemCount; i++) {
		if (reportedProblems[i].isError()) errorCount++;
	}
	if (errorCount == this.problemCount) return reportedProblems;
	CategorizedProblem[] errors = new CategorizedProblem[errorCount];
	int index = 0;
	for (int i = 0; i < this.problemCount; i++) {
		if (reportedProblems[i].isError()) errors[index++] = reportedProblems[i];
	}
	return errors;
}


/**
 * Answer the initial file name
 */
public char[] getFileName(){
	return this.fileName;
}

public int[] getLineSeparatorPositions() {
	return this.lineSeparatorPositions == null ? CompilationResult.EMPTY_LINE_ENDS : this.lineSeparatorPositions;
}

/**
 * Answer the problems (errors and warnings) encountered during compilation.
 *
 * This is not a compiler internal API - it has side-effects !
 * It is intended to be used only once all problems have been detected,
 * and makes sure the problems slot as the exact size of the number of
 * problems.
 */
public CategorizedProblem[] getProblems() {
	// Re-adjust the size of the problems if necessary.
	if (this.problems != null) {
		discardSuppressedWarnings();

		if (this.problemCount != this.problems.length) {
			System.arraycopy(this.problems, 0, (this.problems = new CategorizedProblem[this.problemCount]), 0, this.problemCount);
		}

		if (this.maxProblemPerUnit > 0 && this.problemCount > this.maxProblemPerUnit){
			quickPrioritize(this.problems, 0, this.problemCount - 1);
			this.problemCount = this.maxProblemPerUnit;
			System.arraycopy(this.problems, 0, (this.problems = new CategorizedProblem[this.problemCount]), 0, this.problemCount);
		}

		// Stable sort problems per source positions.
		Arrays.sort(this.problems, 0, this.problems.length, CompilationResult.PROBLEM_COMPARATOR);
		//quickSort(problems, 0, problems.length-1);
	}
	return this.problems;
}

/**
 * Answer the tasks (TO-DO, ...) encountered during compilation.
 *
 * This is not a compiler internal API - it has side-effects !
 * It is intended to be used only once all problems have been detected,
 * and makes sure the problems slot as the exact size of the number of
 * problems.
 */
public CategorizedProblem[] getTasks() {
	// Re-adjust the size of the tasks if necessary.
	if (this.tasks != null) {

		if (this.taskCount != this.tasks.length) {
			System.arraycopy(this.tasks, 0, (this.tasks = new CategorizedProblem[this.taskCount]), 0, this.taskCount);
		}
		// Stable sort problems per source positions.
		Arrays.sort(this.tasks, 0, this.tasks.length, CompilationResult.PROBLEM_COMPARATOR);
		//quickSort(tasks, 0, tasks.length-1);
	}
	return this.tasks;
}

public boolean hasErrors() {
	if (this.problems != null)
		for (int i = 0; i < this.problemCount; i++) {
			if (this.problems[i].isError())
				return true;
		}
	return false;
}

public boolean hasProblems() {
	return this.problemCount != 0;
}

public boolean hasTasks() {
	return this.taskCount != 0;
}

public boolean hasWarnings() {
	if (this.problems != null)
		for (int i = 0; i < this.problemCount; i++) {
			if (this.problems[i].isWarning())
				return true;
		}
	return false;
}

private void quickPrioritize(CategorizedProblem[] problemList, int left, int right) {
	if (left >= right) return;

	// sort the problems by their priority... starting with the highest priority
	int original_left = left;
	int original_right = right;
	int mid = computePriority(problemList[(left + right) / 2]);
	do {
		while (computePriority(problemList[right]) < mid)
			right--;
		while (mid < computePriority(problemList[left]))
			left++;
		if (left <= right) {
			CategorizedProblem tmp = problemList[left];
			problemList[left] = problemList[right];
			problemList[right] = tmp;
			left++;
			right--;
		}
	} while (left <= right);
	if (original_left < right)
		quickPrioritize(problemList, original_left, right);
	if (left < original_right)
		quickPrioritize(problemList, left, original_right);
}
/*
 * Record the compilation unit result's package name
 */
public void recordPackageName(char[][] packName) {
	this.packageName = packName;
}
public void record(CategorizedProblem newProblem, ReferenceContext referenceContext) {
	//new Exception("VERBOSE PROBLEM REPORTING").printStackTrace();
	if(newProblem.getID() == IProblem.Task) {
			recordTask(newProblem);
			return;
	}
	if (this.problemCount == 0) {
		this.problems = new CategorizedProblem[5];
	} else if (this.problemCount == this.problems.length) {
		System.arraycopy(this.problems, 0, (this.problems = new CategorizedProblem[this.problemCount * 2]), 0, this.problemCount);
	}
	this.problems[this.problemCount++] = newProblem;
	if (referenceContext != null){
		if (this.problemsMap == null) this.problemsMap = new HashMap(5);
		if (this.firstErrors == null) this.firstErrors = new HashSet(5);
		if (newProblem.isError() && !referenceContext.hasErrors()) this.firstErrors.add(newProblem);
		this.problemsMap.put(newProblem, referenceContext);
	}
	if ((newProblem.getID() & IProblem.Syntax) != 0 && newProblem.isError())
		this.hasSyntaxError = true;
}

/**
 * For now, remember the compiled type using its compound name.
 */
public void record(char[] typeName, ClassFile classFile) {
    SourceTypeBinding sourceType = classFile.referenceBinding;
    if (!sourceType.isLocalType() && sourceType.isHierarchyInconsistent()) {
        this.hasInconsistentToplevelHierarchies = true;
    }
	this.compiledTypes.put(typeName, classFile);
}

public void recordSuppressWarnings(long irritant, int scopeStart, int scopeEnd) {
	if (this.suppressWarningIrritants == null) {
		this.suppressWarningIrritants = new long[3];
		this.suppressWarningScopePositions = new long[3];
	} else if (this.suppressWarningIrritants.length == this.suppressWarningsCount) {
		System.arraycopy(this.suppressWarningIrritants, 0,this.suppressWarningIrritants = new long[2*this.suppressWarningsCount], 0, this.suppressWarningsCount);
		System.arraycopy(this.suppressWarningScopePositions, 0,this.suppressWarningScopePositions = new long[2*this.suppressWarningsCount], 0, this.suppressWarningsCount);
	}
	this.suppressWarningIrritants[this.suppressWarningsCount] = irritant;
	this.suppressWarningScopePositions[this.suppressWarningsCount++] = ((long)scopeStart<<32) + scopeEnd;
}

private void recordTask(CategorizedProblem newProblem) {
	if (this.taskCount == 0) {
		this.tasks = new CategorizedProblem[5];
	} else if (this.taskCount == this.tasks.length) {
		System.arraycopy(this.tasks, 0, (this.tasks = new CategorizedProblem[this.taskCount * 2]), 0, this.taskCount);
	}
	this.tasks[this.taskCount++] = newProblem;
}

public CompilationResult tagAsAccepted(){
	this.hasBeenAccepted = true;
	this.problemsMap = null; // flush
	this.firstErrors = null; // flush
	return this;
}

public String toString(){
	StringBuffer buffer = new StringBuffer();
	if (this.fileName != null){
		buffer.append("Filename : ").append(this.fileName).append('\n'); //$NON-NLS-1$
	}
	if (this.compiledTypes != null){
		buffer.append("COMPILED type(s)	\n");  //$NON-NLS-1$
		Iterator keys = this.compiledTypes.keySet().iterator();
		while (keys.hasNext()) {
			char[] typeName = (char[]) keys.next();
			buffer.append("\t - ").append(typeName).append('\n');   //$NON-NLS-1$
			
		}
	} else {
		buffer.append("No COMPILED type\n");  //$NON-NLS-1$
	}
	if (this.problems != null){
		buffer.append(this.problemCount).append(" PROBLEM(s) detected \n"); //$NON-NLS-1$
		for (int i = 0; i < this.problemCount; i++){
			buffer.append("\t - ").append(this.problems[i]).append('\n'); //$NON-NLS-1$
		}
	} else {
		buffer.append("No PROBLEM\n"); //$NON-NLS-1$
	} 
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6711.java