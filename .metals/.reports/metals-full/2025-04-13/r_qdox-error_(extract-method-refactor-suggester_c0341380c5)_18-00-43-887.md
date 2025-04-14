error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3345.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3345.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[13,1]

error in qdox parser
file content:
```java
offset: 577
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3345.java
text:
```scala
class DOMParser extends Parser {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
p@@ackage org.eclipse.jdt.core.dom;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

/**
 * Specific parser to use while parsing source to create DOM AST nodes.
 */
public class DOMParser extends Parser {
	
	// support for comments
	int[] commentStops = new int[10];
	int[] commentStarts = new int[10];
	int commentPtr = -1; // no comment test with commentPtr value -1
	protected final static int CommentIncrement = 100;
	
	/**
	 * @param problemReporter
	 * @param optimizeStringLiterals
	 */
	public DOMParser(ProblemReporter problemReporter, boolean optimizeStringLiterals) {
		super(problemReporter, optimizeStringLiterals);
		this.javadocParser.checkDocComment = true;
	}

	// old javadoc style check which doesn't include all leading comments into declaration
	// for backward compatibility with 2.1 DOM 
	public void checkComment() {

		if (this.currentElement != null && this.scanner.commentPtr >= 0) {
			flushCommentsDefinedPriorTo(this.endStatementPosition); // discard obsolete comments
		}
		boolean deprecated = false;
		boolean checkDeprecated = false;
		int lastCommentIndex = -1;
		
		// 
		
		//since jdk1.2 look only in the last java doc comment...
		nextComment : for (lastCommentIndex = this.scanner.commentPtr; lastCommentIndex >= 0; lastCommentIndex--){
			//look for @deprecated into the first javadoc comment preceeding the declaration
			int commentSourceStart = this.scanner.commentStarts[lastCommentIndex];
			// javadoc only (non javadoc comment have negative end positions.)
			if ((commentSourceStart < 0) ||
				(this.modifiersSourceStart != -1 && this.modifiersSourceStart < commentSourceStart) ||
				(this.scanner.commentStops[lastCommentIndex] < 0))
			{
				continue nextComment;
			}
			checkDeprecated = true;
			int commentSourceEnd = this.scanner.commentStops[lastCommentIndex] - 1; //stop is one over
			
			deprecated = this.javadocParser.checkDeprecation(commentSourceStart, commentSourceEnd);
			this.javadoc = this.javadocParser.docComment;
			break nextComment;
		}
		if (deprecated) {
			checkAndSetModifiers(AccDeprecated);
		}
		// modify the modifier source start to point at the first comment
		if (lastCommentIndex >= 0 && checkDeprecated) {
			this.modifiersSourceStart = this.scanner.commentStarts[lastCommentIndex]; 
			if (this.modifiersSourceStart < 0) {
				this.modifiersSourceStart = -this.modifiersSourceStart;
			}
		}

	}

	
	/* (non-Javadoc)
	 * Save all source comments currently stored before flushing them.
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#flushCommentsDefinedPriorTo(int)
	 */
	public int flushCommentsDefinedPriorTo(int position) {

		int lastCommentIndex = this.scanner.commentPtr;
		if (lastCommentIndex < 0) return position; // no comment
	
		// compute the index of the first obsolete comment
		int index = lastCommentIndex;
		int validCount = 0;
		while (index >= 0){
			int commentEnd = this.scanner.commentStops[index];
			if (commentEnd < 0) commentEnd = -commentEnd; // negative end position for non-javadoc comments
			if (commentEnd <= position){
				break;
			}
			index--;
			validCount++;
		}
		// if the source at <position> is immediately followed by a line comment, then
		// flush this comment and shift <position> to the comment end.
		if (validCount > 0){
			int immediateCommentEnd = -this.scanner.commentStops[index+1]; //non-javadoc comment end positions are negative
			if (immediateCommentEnd > 0){ // only tolerating non-javadoc comments
				// is there any line break until the end of the immediate comment ? (thus only tolerating line comment)
				immediateCommentEnd--; // comment end in one char too far
				if (this.scanner.getLineNumber(position) == this.scanner.getLineNumber(immediateCommentEnd)){
					position = immediateCommentEnd;
					validCount--; // flush this comment
					index++;
				}
			}
		}
	
		if (index < 0) return position; // no obsolete comment
		pushOnCommentsStack(0, index); // store comment before flushing them

		if (validCount > 0){ // move valid comment infos, overriding obsolete comment infos
			System.arraycopy(this.scanner.commentStarts, index + 1, this.scanner.commentStarts, 0, validCount);
			System.arraycopy(this.scanner.commentStops, index + 1, this.scanner.commentStops, 0, validCount);		
		}
		this.scanner.commentPtr = validCount - 1;
		return position;
	}

	/*
	 * Push all stored comments in stack.
	 */
	private void pushOnCommentsStack(int start, int end) {

		for (int i=start; i<=end; i++) {
			// First see if comment hasn't been already stored
			if (this.commentPtr == -1 || this.scanner.commentStarts[i] > this.commentStarts[this.commentPtr]) {
				try {
					this.commentPtr++;
					this.commentStarts[this.commentPtr] = this.scanner.commentStarts[i];
					this.commentStops[this.commentPtr] = this.scanner.commentStops[i];
				} catch (IndexOutOfBoundsException e) {
					// this.commentPtr is still correct 
					int oldStackLength = this.commentStarts.length;
					int oldCommentStarts[] = this.commentStarts;
					this.commentStarts = new int[oldStackLength + CommentIncrement];
					System.arraycopy(oldCommentStarts, 0, this.commentStarts, 0, oldStackLength);
					this.commentStarts[this.commentPtr] = this.scanner.commentStarts[i];
					int oldCommentStops[] = this.commentStops;
					this.commentStops = new int[oldStackLength + CommentIncrement];
					System.arraycopy(oldCommentStops, 0, this.commentStops, 0, oldStackLength);
					this.commentStops[this.commentPtr] = this.scanner.commentStops[i];
				}
			}
		}
	}
	/* (non-Javadoc)
	 * Save all source comments currently stored before flushing them.
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#resetModifiers()
	 */
	protected void resetModifiers() {
		pushOnCommentsStack(0, this.scanner.commentPtr);
		super.resetModifiers();
	}

	/**
	 * Store comments positions saved in stack in compilation unit declaration.
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#dietParse(org.eclipse.jdt.internal.compiler.env.ICompilationUnit, org.eclipse.jdt.internal.compiler.CompilationResult)
	 */
	public CompilationUnitDeclaration dietParse(ICompilationUnit sourceUnit, CompilationResult compilationResult) {
		CompilationUnitDeclaration unit = super.dietParse(sourceUnit, compilationResult);
		unit.comments = getCommentsPositions();
		return unit;
	}

	/**
	 * Insure that start position is always positive.
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#containsComment(int, int)
	 */
	public boolean containsComment(int sourceStart, int sourceEnd) {
		int iComment = this.scanner.commentPtr;
		for (; iComment >= 0; iComment--) {
			int commentStart = this.scanner.commentStarts[iComment];
			if (commentStart < 0) {
				commentStart = -commentStart;
			}
			// ignore comments before start
			if (commentStart < sourceStart) continue;
			// ignore comments after end
			if (commentStart > sourceEnd) continue;
			return true;
		}
		return false;
	}

	/*
	 * Build a n*2 matrix of comments positions.
	 * For each position, 0 is for start position and 1 for end position of the comment.
	 */
	public int[][] getCommentsPositions() {
		int[][] positions = new int[this.commentPtr+1][2];
		for (int i = 0, max = this.commentPtr; i <= max; i++){
			positions[i][0] = this.commentStarts[i];
			positions[i][1] = this.commentStops[i];
		}
		return positions;
	}
	
	/* (non-Javadoc)
	 * Create and store a specific DOM scanner.
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#initializeScanner()
	 */
	public void initializeScanner() {
		this.scanner = new DOMScanner(
				false /*comment*/, 
				false /*whitespace*/, 
				this.options.getSeverity(CompilerOptions.NonExternalizedString) != ProblemSeverities.Ignore /*nls*/, 
				this.options.sourceLevel /*sourceLevel*/, 
				this.options.taskTags/*taskTags*/,
				this.options.taskPriorites/*taskPriorities*/);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.parser.Parser#initialize()
	 */
	public void initialize() {
		super.initialize();
		this.commentPtr = -1;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3345.java