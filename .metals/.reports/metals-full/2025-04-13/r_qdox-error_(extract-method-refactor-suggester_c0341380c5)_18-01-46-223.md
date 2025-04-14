error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2412.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2412.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2412.java
text:
```scala
f@@or (Iterator iteraror = this.problems.values().iterator(); iteraror.hasNext();) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Reconcile a working copy and signal the changes through a delta.
 */
public class ReconcileWorkingCopyOperation extends JavaModelOperation {
	public static boolean PERF = false;
	
	public int astLevel;
	public boolean resolveBindings;
	public HashMap problems;
	boolean forceProblemDetection;
	WorkingCopyOwner workingCopyOwner;
	public org.eclipse.jdt.core.dom.CompilationUnit ast;
	public JavaElementDeltaBuilder deltaBuilder;
	
	public ReconcileWorkingCopyOperation(IJavaElement workingCopy, int astLevel, boolean forceProblemDetection, WorkingCopyOwner workingCopyOwner) {
		super(new IJavaElement[] {workingCopy});
		this.astLevel = astLevel;
		this.forceProblemDetection = forceProblemDetection;
		this.workingCopyOwner = workingCopyOwner;
	}
	
	/**
	 * @exception JavaModelException if setting the source
	 * 	of the original compilation unit fails
	 */
	protected void executeOperation() throws JavaModelException {
		if (this.progressMonitor != null) {
			if (this.progressMonitor.isCanceled()) 
				throw new OperationCanceledException();
			this.progressMonitor.beginTask(Messages.element_reconciling, 2); 
		}
	
		CompilationUnit workingCopy = getWorkingCopy();
		IProblemRequestor problemRequestor = workingCopy.getPerWorkingCopyInfo();
		this.resolveBindings |= problemRequestor != null && problemRequestor.isActive();
		
		// create the delta builder (this remembers the current content of the cu)
		this.deltaBuilder = new JavaElementDeltaBuilder(workingCopy);
		
		// make working copy consistent if needed and compute AST if needed
		makeConsistent(workingCopy, problemRequestor);
		
		// notify reconcile participants
		notifyParticipants(workingCopy);
		
		// recreate ast if needed
		if (this.ast == null && (this.astLevel > ICompilationUnit.NO_AST || this.resolveBindings))
			makeConsistent(workingCopy, problemRequestor);
	
		// report problems
		if (this.problems != null) {
			try {
				problemRequestor.beginReporting();
				for (Iterator iteraror = problems.values().iterator(); iteraror.hasNext();) {
					CategorizedProblem[] categorizedProblems = (CategorizedProblem[]) iteraror.next();
					if (categorizedProblems == null) continue;
					for (int i = 0, length = categorizedProblems.length; i < length; i++) {
						CategorizedProblem problem = categorizedProblems[i];
						if (JavaModelManager.VERBOSE){
							System.out.println("PROBLEM FOUND while reconciling : " + problem.getMessage());//$NON-NLS-1$
						}
						if (this.progressMonitor != null && this.progressMonitor.isCanceled()) break;
						problemRequestor.acceptProblem(problem);
					}
				}
			} finally {
				problemRequestor.endReporting();
			}
		}
		
		// report delta
		try {
			JavaElementDelta delta = this.deltaBuilder.delta;
			if (delta != null) {
				addReconcileDelta(workingCopy, delta);
			}
		} finally {
			if (this.progressMonitor != null) this.progressMonitor.done();
		}
	}
	/**
	 * Returns the working copy this operation is working on.
	 */
	protected CompilationUnit getWorkingCopy() {
		return (CompilationUnit)getElementToProcess();
	}
	/**
	 * @see JavaModelOperation#isReadOnly
	 */
	public boolean isReadOnly() {
		return true;
	}
	/*
	 * Makes the given working copy consistent, computes the delta and computes an AST if needed.
	 * Returns the AST.
	 */
	public org.eclipse.jdt.core.dom.CompilationUnit makeConsistent(CompilationUnit workingCopy, IProblemRequestor problemRequestor) throws JavaModelException {
		if (!workingCopy.isConsistent()) {
			// make working copy consistent
			if (this.problems == null) this.problems = new HashMap();
			this.ast = workingCopy.makeConsistent(this.astLevel, this.resolveBindings, this.problems, this.progressMonitor);
			this.deltaBuilder.buildDeltas();
			if (this.ast != null && this.deltaBuilder.delta != null)
				this.deltaBuilder.delta.changedAST(this.ast);
			return this.ast;
		} 
		if (this.ast != null) return this.ast; // no need to recompute AST if known already
		if (this.forceProblemDetection && this.resolveBindings) {
			if (JavaProject.hasJavaNature(workingCopy.getJavaProject().getProject())) {
				if (this.problems == null) this.problems = new HashMap();
			    CompilationUnitDeclaration unit = null;
			    try {
			    	// find problems
					char[] contents = workingCopy.getContents();
					unit = 
						CompilationUnitProblemFinder.process(
							workingCopy, 
							contents, 
							this.workingCopyOwner, 
							this.problems, 
							this.astLevel != ICompilationUnit.NO_AST/*creating AST if level is not NO_AST */, 
							this.progressMonitor);
					if (this.progressMonitor != null) this.progressMonitor.worked(1);
					
					// create AST if needed
					if (this.astLevel != ICompilationUnit.NO_AST && unit != null) {
						Map options = workingCopy.getJavaProject().getOptions(true);
						this.ast = 
							AST.convertCompilationUnit(
								this.astLevel, 
								unit, 
								contents, 
								options, 
								true/*isResolved*/, 
								workingCopy, 
								this.progressMonitor);
						if (this.ast != null) {
							this.deltaBuilder.delta = new JavaElementDelta(workingCopy);
							this.deltaBuilder.delta.changedAST(this.ast);
						}
						if (this.progressMonitor != null) this.progressMonitor.worked(1);
					}
			    } finally {
			        if (unit != null) {
			            unit.cleanUp();
			        }
			    }
			} // else working copy not in a Java project
			return this.ast;
		} 
		return null;
	}
	private void notifyParticipants(final CompilationUnit workingCopy) {
		IJavaProject javaProject = getWorkingCopy().getJavaProject();
		CompilationParticipant[] participants = JavaModelManager.getJavaModelManager().compilationParticipants.getCompilationParticipants(javaProject);	
		if (participants == null) return;

		final ReconcileContext context = new ReconcileContext(this, workingCopy);
		for (int i = 0, length = participants.length; i < length; i++) {
			final CompilationParticipant participant = participants[i];
			Platform.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					Util.log(exception, "Exception occurred in pre-reconcile participant"); //$NON-NLS-1$
				}
				public void run() throws Exception {
					participant.reconcile(context);
				}
			});
		}
	}
	protected IJavaModelStatus verify() {
		IJavaModelStatus status = super.verify();
		if (!status.isOK()) {
			return status;
		}
		CompilationUnit workingCopy = getWorkingCopy();
		if (!workingCopy.isWorkingCopy()) {
			return new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, workingCopy); //was destroyed
		}
		return status;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2412.java