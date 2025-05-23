error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6374.java
text:
```scala
public b@@oolean hasAnnotations = false;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

public class CompilationResult {
	private static final int[] EMPTY_LINE_ENDS = new int[0];
	
	public IProblem problems[];
	public IProblem tasks[];
	public int problemCount;
	public int taskCount;
	public ICompilationUnit compilationUnit;
	private Map problemsMap;
	private Set firstErrors;
	private int maxProblemPerUnit;
	public char[][][] qualifiedReferences;
	public char[][] simpleNameReferences;
	public boolean declaresAnnotations = false;

	public int lineSeparatorPositions[];
	public Map compiledTypes = new Hashtable(11);
	public int unitIndex, totalUnitsKnown;
	public boolean hasBeenAccepted = false;
	public char[] fileName;
	public boolean hasInconsistentToplevelHierarchies = false; // record the fact some toplevel types have inconsistent hierarchies
	public boolean hasSyntaxError = false;
	long[] suppressWarningIrritants;  // irritant for suppressed warnings
	long[] suppressWarningScopePositions; // (start << 32) + end 
	int suppressWarningsCount;
	
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

	private int computePriority(IProblem problem){
	
		final int P_STATIC = 10000;
		final int P_OUTSIDE_METHOD = 40000;
		final int P_FIRST_ERROR = 20000;
		final int P_ERROR = 100000;
		
		int priority = 10000 - problem.getSourceLineNumber(); // early problems first
		if (priority < 0) priority = 0;
		if (problem.isError()){
			priority += P_ERROR;
		}
		ReferenceContext context = problemsMap == null ? null : (ReferenceContext) problemsMap.get(problem);
		if (context != null){
			if (context instanceof AbstractMethodDeclaration){
				AbstractMethodDeclaration method = (AbstractMethodDeclaration) context;
				if (method.isStatic()) {
					priority += P_STATIC;
				}
			} else {
				priority += P_OUTSIDE_METHOD;
			}
			if (firstErrors.contains(problem)){ // if context is null, firstErrors is null too
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
			IProblem problem = this.problems[i];
			int problemID = problem.getID();
			if (!problem.isWarning()) {
				switch (problemID) {
					case IProblem.NonExternalizedStringLiteral :
					case IProblem.UnnecessaryNLSTag :
						break;
					default :
						continue nextProblem;
				}
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
				problems[i] = null;
				if (problemsMap != null) problemsMap.remove(problem);
				if (firstErrors != null) firstErrors.remove(problem);
				continue nextProblem;
			}
		}
		if (removed > 0) {
			for (int i = 0, index = 0; i < this.problemCount; i++) {
				IProblem problem;
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
	
	public IProblem[] getAllProblems() {
		IProblem[] onlyProblems = this.getProblems();
		int onlyProblemCount = onlyProblems != null ? onlyProblems.length : 0;
		IProblem[] onlyTasks = this.getTasks();
		int onlyTaskCount = onlyTasks != null ? onlyTasks.length : 0;
		if (onlyTaskCount == 0) {
			return onlyProblems;
		}
		if (onlyProblemCount == 0) {
			return onlyTasks;
		}

		int totalNumberOfProblem = onlyProblemCount + onlyTaskCount;
		IProblem[] allProblems = new IProblem[totalNumberOfProblem];
		int allProblemIndex = 0;
		int taskIndex = 0;
		int problemIndex = 0;
		while (taskIndex + problemIndex < totalNumberOfProblem) {
			IProblem nextTask = null;
			IProblem nextProblem = null;
			if (taskIndex < onlyTaskCount) {
				nextTask = onlyTasks[taskIndex];
			}
			if (problemIndex < onlyProblemCount) {
				nextProblem = onlyProblems[problemIndex];
			}
			// select the next problem
			IProblem currentProblem = null;
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
		ClassFile[] classFiles = new ClassFile[compiledTypes.size()];
		compiledTypes.values().toArray(classFiles);
		return classFiles;	
	}

	/**
	 * Answer the initial compilation unit corresponding to the present compilation result
	 */
	public ICompilationUnit getCompilationUnit(){
		return compilationUnit;
	}

	/**
	 * Answer the initial file name
	 */
	public char[] getFileName(){
		return fileName;
	}
	
	/**
	 * Answer the errors encountered during compilation.
	 */
	public IProblem[] getErrors() {
	
		IProblem[] reportedProblems = getProblems();
		int errorCount = 0;
		for (int i = 0; i < this.problemCount; i++) {
			if (reportedProblems[i].isError()) errorCount++;
		}
		if (errorCount == this.problemCount) return reportedProblems;
		IProblem[] errors = new IProblem[errorCount];
		int index = 0;
		for (int i = 0; i < this.problemCount; i++) {
			if (reportedProblems[i].isError()) errors[index++] = reportedProblems[i];
		}
		return errors;
	}
	

	public int[] getLineSeparatorPositions() {
		return this.lineSeparatorPositions == null ? EMPTY_LINE_ENDS : this.lineSeparatorPositions;
	}

	/**
	 * Answer the problems (errors and warnings) encountered during compilation.
	 *
	 * This is not a compiler internal API - it has side-effects !
	 * It is intended to be used only once all problems have been detected,
	 * and makes sure the problems slot as the exact size of the number of
	 * problems.
	 */
	public IProblem[] getProblems() {
		
		// Re-adjust the size of the problems if necessary.
		if (problems != null) {
			discardSuppressedWarnings();
	
			if (this.problemCount != problems.length) {
				System.arraycopy(problems, 0, (problems = new IProblem[problemCount]), 0, problemCount);
			}
	
			if (this.maxProblemPerUnit > 0 && this.problemCount > this.maxProblemPerUnit){
				quickPrioritize(problems, 0, problemCount - 1);
				this.problemCount = this.maxProblemPerUnit;
				System.arraycopy(problems, 0, (problems = new IProblem[problemCount]), 0, problemCount);
			}
	
			// Sort problems per source positions.
			quickSort(problems, 0, problems.length-1);
		}
		return problems;
	}

	/**
	 * Answer the tasks (TO-DO, ...) encountered during compilation.
	 *
	 * This is not a compiler internal API - it has side-effects !
	 * It is intended to be used only once all problems have been detected,
	 * and makes sure the problems slot as the exact size of the number of
	 * problems.
	 */
	public IProblem[] getTasks() {
		
		// Re-adjust the size of the tasks if necessary.
		if (this.tasks != null) {
	
			if (this.taskCount != this.tasks.length) {
				System.arraycopy(this.tasks, 0, (this.tasks = new IProblem[this.taskCount]), 0, this.taskCount);
			}
			quickSort(tasks, 0, tasks.length-1);
		}
		return this.tasks;
	}
	
	public boolean hasErrors() {

		if (problems != null)
			for (int i = 0; i < problemCount; i++) {
				if (problems[i].isError())
					return true;
			}
		return false;
	}

	public boolean hasProblems() {

		return problemCount != 0;
	}

	public boolean hasTasks() {
		return this.taskCount != 0;
	}
	
	public boolean hasWarnings() {

		if (problems != null)
			for (int i = 0; i < problemCount; i++) {
				if (problems[i].isWarning())
					return true;
			}
		return false;
	}
	
	private static void quickSort(IProblem[] list, int left, int right) {

		if (left >= right) return;
	
		// sort the problems by their source start position... starting with 0
		int original_left = left;
		int original_right = right;
		int mid = list[(left + right) / 2].getSourceStart();
		do {
			while (list[left].getSourceStart() < mid)
				left++;
			while (mid < list[right].getSourceStart())
				right--;
			if (left <= right) {
				IProblem tmp = list[left];
				list[left] = list[right];
				list[right] = tmp;
				left++;
				right--;
			}
		} while (left <= right);
		if (original_left < right)
			quickSort(list, original_left, right);
		if (left < original_right)
			quickSort(list, left, original_right);
	}
	
	private void quickPrioritize(IProblem[] list, int left, int right) {
		
		if (left >= right) return;
	
		// sort the problems by their priority... starting with the highest priority
		int original_left = left;
		int original_right = right;
		int mid = computePriority(list[(left + right) / 2]);
		do {
			while (computePriority(list[right]) < mid)
				right--;
			while (mid < computePriority(list[left]))
				left++;
			if (left <= right) {
				IProblem tmp = list[left];
				list[left] = list[right];
				list[right] = tmp;
				left++;
				right--;
			}
		} while (left <= right);
		if (original_left < right)
			quickPrioritize(list, original_left, right);
		if (left < original_right)
			quickPrioritize(list, left, original_right);
	}
	
	/**
	 * For now, remember the compiled type using its compound name.
	 */
	public void record(char[] typeName, ClassFile classFile) {

	    SourceTypeBinding sourceType = classFile.referenceBinding;
	    if (!sourceType.isLocalType() && sourceType.isHierarchyInconsistent()) {
	        this.hasInconsistentToplevelHierarchies = true;
	    }
		compiledTypes.put(typeName, classFile);
	}

	public void record(IProblem newProblem, ReferenceContext referenceContext) {

		//new Exception("VERBOSE PROBLEM REPORTING").printStackTrace();
		if(newProblem.getID() == IProblem.Task) {
				recordTask(newProblem);
				return;
		}
		if (problemCount == 0) {
			problems = new IProblem[5];
		} else if (problemCount == problems.length) {
			System.arraycopy(problems, 0, (problems = new IProblem[problemCount * 2]), 0, problemCount);
		}
		problems[problemCount++] = newProblem;
		if (referenceContext != null){
			if (problemsMap == null) problemsMap = new HashMap(5);
			if (firstErrors == null) firstErrors = new HashSet(5);
			if (newProblem.isError() && !referenceContext.hasErrors()) firstErrors.add(newProblem);
			problemsMap.put(newProblem, referenceContext);
		}
		if ((newProblem.getID() & IProblem.Syntax) != 0 && newProblem.isError())
			this.hasSyntaxError = true;
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

	private void recordTask(IProblem newProblem) {
		if (this.taskCount == 0) {
			this.tasks = new IProblem[5];
		} else if (this.taskCount == this.tasks.length) {
			System.arraycopy(this.tasks, 0, (this.tasks = new IProblem[this.taskCount * 2]), 0, this.taskCount);
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
		if (problems != null){
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6374.java