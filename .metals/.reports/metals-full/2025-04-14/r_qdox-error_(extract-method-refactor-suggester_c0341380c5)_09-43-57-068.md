error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7619.java
text:
```scala
C@@onstant condConstant = this.condition.constant;

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
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class IfStatement extends Statement {
	
	//this class represents the case of only one statement in 
	//either else and/or then branches.

	public Expression condition;
	public Statement thenStatement;
	public Statement elseStatement;

	boolean thenExit;

	// for local variables table attributes
	int thenInitStateIndex = -1;
	int elseInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public IfStatement(
		Expression condition,
		Statement thenStatement,
		int s,
		int e) {

		this.condition = condition;
		this.thenStatement = thenStatement;
		sourceStart = s;
		sourceEnd = e;
	}

	public IfStatement(
		Expression condition,
		Statement thenStatement,
		Statement elseStatement,
		int s,
		int e) {

		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
		sourceEnd = e;
		sourceStart = s;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		FlowInfo thenFlowInfo, elseFlowInfo;

		// process the condition
		flowInfo = condition.analyseCode(currentScope, flowContext, flowInfo);
		Constant condConstant = this.condition.optimizedBooleanConstant(); //TODO: wrong, cannot use optimizedBoolean here (initializationTest#71).

		// process the THEN part
		if (this.thenStatement == null) {
			thenFlowInfo = flowInfo.initsWhenTrue();
		} else {
			thenFlowInfo =
				(condConstant != NotAConstant && condConstant.booleanValue() == false)
					? flowInfo.initsWhenTrue().copy().markAsFakeReachable(true)
					: flowInfo.initsWhenTrue().copy();
			// Save info for code gen
			thenInitStateIndex =
				currentScope.methodScope().recordInitializationStates(thenFlowInfo);
			if (!thenFlowInfo.complainIfUnreachable(thenStatement, currentScope, false)) {
				thenFlowInfo =
					thenStatement.analyseCode(currentScope, flowContext, thenFlowInfo);
			}
		};
		// optimizing the jump around the ELSE part
		this.thenExit = (thenFlowInfo == FlowInfo.DeadEnd) || thenFlowInfo.isFakeReachable();

		// process the ELSE part
		if (this.elseStatement == null) {
			elseFlowInfo = flowInfo.initsWhenFalse();
		} else {
			elseFlowInfo =
				(condConstant != NotAConstant && condConstant.booleanValue() == true)
					? flowInfo.initsWhenFalse().copy().markAsFakeReachable(true)
					: flowInfo.initsWhenFalse().copy();
			// Save info for code gen
			elseInitStateIndex =
				currentScope.methodScope().recordInitializationStates(elseFlowInfo);
			if (!elseFlowInfo.complainIfUnreachable(elseStatement, currentScope, false)) {
				elseFlowInfo =
					elseStatement.analyseCode(currentScope, flowContext, elseFlowInfo);
			}
		}

		// merge THEN & ELSE initializations
		FlowInfo mergedInfo;
		if (condConstant != NotAConstant && condConstant.booleanValue() == true) {
			// IF (TRUE)
			if (this.thenExit) {
				mergedInfo = elseFlowInfo.markAsFakeReachable(true);
				mergedInitStateIndex =
					currentScope.methodScope().recordInitializationStates(mergedInfo);
				return mergedInfo;
			} else {
				mergedInitStateIndex =
					currentScope.methodScope().recordInitializationStates(thenFlowInfo);
				return thenFlowInfo;
			}
		} else {
			// IF (FALSE)
			if (condConstant != NotAConstant && condConstant.booleanValue() == false) {
				if (elseFlowInfo == FlowInfo.DeadEnd || elseFlowInfo.isFakeReachable()) {
					mergedInfo = thenFlowInfo.markAsFakeReachable(true);
					mergedInitStateIndex =
						currentScope.methodScope().recordInitializationStates(mergedInfo);
					return mergedInfo;
				} else {
					mergedInitStateIndex =
						currentScope.methodScope().recordInitializationStates(elseFlowInfo);
					return elseFlowInfo;
				}
			}
		}
		mergedInfo = thenFlowInfo.mergedWith(elseFlowInfo.unconditionalInits());
		mergedInitStateIndex =
			currentScope.methodScope().recordInitializationStates(mergedInfo);
		return mergedInfo;
	}

	/**
	 * If code generation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((this.bits & IsReachableMASK) == 0) {
			return;
		}
		int pc = codeStream.position;
		Label endifLabel = new Label(codeStream);

		// optimizing the then/else part code gen
		Constant cst;
		boolean hasThenPart = 
			!(((cst = this.condition.optimizedBooleanConstant()) != NotAConstant
					&& cst.booleanValue() == false)
 this.thenStatement == null
 this.thenStatement.isEmptyBlock());
		boolean hasElsePart =
			!((cst != NotAConstant && cst.booleanValue() == true)
 this.elseStatement == null
 this.elseStatement.isEmptyBlock());

		if (hasThenPart) {
			Label falseLabel;
			// generate boolean condition
			this.condition.generateOptimizedBoolean(
				currentScope,
				codeStream,
				null,
				(falseLabel = new Label(codeStream)),
				true);
			// May loose some local variable initializations : affecting the local variable attributes
			if (thenInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(
					currentScope,
					thenInitStateIndex);
				codeStream.addDefinitelyAssignedVariables(currentScope, thenInitStateIndex);
			}
			// generate then statement
			this.thenStatement.generateCode(currentScope, codeStream);
			// jump around the else statement
			if (hasElsePart && !thenExit) {
				this.thenStatement.branchChainTo(endifLabel);
				int position = codeStream.position;
				codeStream.goto_(endifLabel);
				codeStream.updateLastRecordedEndPC(position);
				//goto is tagged as part of the thenAction block
			}
			falseLabel.place();
		} else {
			if (hasElsePart) {
				// generate boolean condition
				this.condition.generateOptimizedBoolean(
					currentScope,
					codeStream,
					endifLabel,
					null,
					true);
			} else {
				// generate condition side-effects
				this.condition.generateCode(currentScope, codeStream, false);
				codeStream.recordPositionsFrom(pc, this.sourceStart);
			}
		}
		// generate else statement
		if (hasElsePart) {
			// May loose some local variable initializations : affecting the local variable attributes
			if (elseInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(
					currentScope,
					elseInitStateIndex);
				codeStream.addDefinitelyAssignedVariables(currentScope, elseInitStateIndex);
			}
			this.elseStatement.generateCode(currentScope, codeStream);
		}
		endifLabel.place();
		// May loose some local variable initializations : affecting the local variable attributes
		if (mergedInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(
				currentScope,
				mergedInitStateIndex);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public void resolve(BlockScope scope) {

		TypeBinding type = condition.resolveTypeExpecting(scope, BooleanBinding);
		condition.implicitWidening(type, type);
		if (thenStatement != null)
			thenStatement.resolve(scope);
		if (elseStatement != null)
			elseStatement.resolve(scope);
	}

	public String toString(int tab) {

		String inFront, s = tabString(tab);
		inFront = s;
		s = s + "if (" + condition.toStringExpression() + ") \n";	//$NON-NLS-1$ //$NON-NLS-2$
		s = s + thenStatement.toString(tab + 2) + ";"; //$NON-NLS-1$
		if (elseStatement != null)
			s = s + "\n" + inFront + "else\n" + elseStatement.toString(tab + 2) + ";"; //$NON-NLS-2$ //$NON-NLS-1$ //$NON-NLS-3$
		return s;
	}

	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			condition.traverse(visitor, blockScope);
			if (thenStatement != null)
				thenStatement.traverse(visitor, blockScope);
			if (elseStatement != null)
				elseStatement.traverse(visitor, blockScope);
		}
		visitor.endVisit(this, blockScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7619.java