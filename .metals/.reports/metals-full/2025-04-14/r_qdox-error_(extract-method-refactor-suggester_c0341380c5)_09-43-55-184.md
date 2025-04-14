error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/726.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/726.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/726.java
text:
```scala
i@@f (this.breakLabel.forwardReferenceCount() > 0) {

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
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class DoStatement extends Statement {

	public Expression condition;
	public Statement action;

	private BranchLabel breakLabel, continueLabel;

	// for local variables table attributes
	int mergedInitStateIndex = -1;

public DoStatement(Expression condition, Statement action, int s, int e) {

	this.sourceStart = s;
	this.sourceEnd = e;
	this.condition = condition;
	this.action = action;
	// remember useful empty statement
	if (action instanceof EmptyStatement) action.bits |= ASTNode.IsUsefulEmptyStatement;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	this.breakLabel = new BranchLabel();
	this.continueLabel = new BranchLabel();
	LoopingFlowContext loopingContext =
		new LoopingFlowContext(
			flowContext,
			flowInfo,
			this,
			this.breakLabel,
			this.continueLabel,
			currentScope);

	Constant cst = this.condition.constant;
	boolean isConditionTrue = cst != Constant.NotAConstant && cst.booleanValue() == true;
	cst = this.condition.optimizedBooleanConstant();
	boolean isConditionOptimizedTrue = cst != Constant.NotAConstant && cst.booleanValue() == true;
	boolean isConditionOptimizedFalse = cst != Constant.NotAConstant && cst.booleanValue() == false;

	int previousMode = flowInfo.reachMode();
			
	UnconditionalFlowInfo actionInfo = flowInfo.nullInfoLessUnconditionalCopy();
	// we need to collect the contribution to nulls of the coming paths through the
	// loop, be they falling through normally or branched to break, continue labels
	// or catch blocks
	if ((this.action != null) && !this.action.isEmptyBlock()) {
		actionInfo = this.action.
			analyseCode(currentScope, loopingContext, actionInfo).
			unconditionalInits();

		// code generation can be optimized when no need to continue in the loop
		if ((actionInfo.tagBits & 
				loopingContext.initsOnContinue.tagBits & 
				FlowInfo.UNREACHABLE) != 0) {
			this.continueLabel = null;
		}
	}
	/* Reset reach mode, to address following scenario.
	 *   final blank;
	 *   do { if (true) break; else blank = 0; } while(false);
	 *   blank = 1; // may be initialized already 
	 */
	actionInfo.setReachMode(previousMode);
	
	LoopingFlowContext condLoopContext;
	FlowInfo condInfo =
		this.condition.analyseCode(
			currentScope,
			(condLoopContext =
				new LoopingFlowContext(flowContext,	flowInfo, this, null, 
					null, currentScope)),
			(this.action == null
				? actionInfo
				: (actionInfo.mergedWith(loopingContext.initsOnContinue))).copy());
	if (!isConditionOptimizedFalse && this.continueLabel != null) {
		loopingContext.complainOnDeferredFinalChecks(currentScope, condInfo);
		condLoopContext.complainOnDeferredFinalChecks(currentScope, condInfo);
		UnconditionalFlowInfo checkFlowInfo;
		loopingContext.complainOnDeferredNullChecks(currentScope, 
				checkFlowInfo = actionInfo.
					addPotentialNullInfoFrom(
					  condInfo.initsWhenTrue().unconditionalInits()));
		condLoopContext.complainOnDeferredNullChecks(currentScope, 
				checkFlowInfo);
	}

	// end of loop
	FlowInfo mergedInfo = FlowInfo.mergedOptimizedBranches(
			(loopingContext.initsOnBreak.tagBits &
				FlowInfo.UNREACHABLE) != 0 ?
				loopingContext.initsOnBreak :
				flowInfo.unconditionalCopy().addInitializationsFrom(loopingContext.initsOnBreak), 
					// recover upstream null info
			isConditionOptimizedTrue,
			(condInfo.tagBits & FlowInfo.UNREACHABLE) == 0 ?
					flowInfo.addInitializationsFrom(condInfo.initsWhenFalse()) : condInfo, 
				// recover null inits from before condition analysis
			false, // never consider opt false case for DO loop, since break can always occur (47776)
			!isConditionTrue /*do{}while(true); unreachable(); */);
	this.mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
	return mergedInfo;
}

/**
 * Do statement code generation
 *
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	if ((this.bits & ASTNode.IsReachable) == 0) {
		return;
	}
	int pc = codeStream.position;

	// labels management
	BranchLabel actionLabel = new BranchLabel(codeStream);
	if (this.action != null) actionLabel.tagBits |= BranchLabel.USED;
	actionLabel.place();
	this.breakLabel.initialize(codeStream);
	boolean hasContinueLabel = this.continueLabel != null;
	if (hasContinueLabel) {
		this.continueLabel.initialize(codeStream);
	}

	// generate action
	if (this.action != null) {
		this.action.generateCode(currentScope, codeStream);
	}
	// continue label (135602)
	if (hasContinueLabel) {
		this.continueLabel.place();
	}
	// generate condition
	Constant cst = this.condition.optimizedBooleanConstant();
	boolean isConditionOptimizedFalse = cst != Constant.NotAConstant && cst.booleanValue() == false;		
	if (isConditionOptimizedFalse){
		this.condition.generateCode(currentScope, codeStream, false);
	} else if (hasContinueLabel) {
		this.condition.generateOptimizedBoolean(
			currentScope,
			codeStream,
			actionLabel,
			null,
			true);
	}
	// May loose some local variable initializations : affecting the local variable attributes
	if (this.mergedInitStateIndex != -1) {
		codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
		codeStream.addDefinitelyAssignedVariables(currentScope, this.mergedInitStateIndex);
	}
	if (this.breakLabel.forwardReferenceCount > 0) {
		this.breakLabel.place();
	}

	codeStream.recordPositionsFrom(pc, this.sourceStart);
}

public StringBuffer printStatement(int indent, StringBuffer output) {
	printIndent(indent, output).append("do"); //$NON-NLS-1$
	if (this.action == null)
		output.append(" ;\n"); //$NON-NLS-1$
	else {
		output.append('\n');
		this.action.printStatement(indent + 1, output).append('\n');
	}
	output.append("while ("); //$NON-NLS-1$
	return this.condition.printExpression(0, output).append(");"); //$NON-NLS-1$
}

public void resolve(BlockScope scope) {
	TypeBinding type = this.condition.resolveTypeExpecting(scope, TypeBinding.BOOLEAN);
	this.condition.computeConversion(scope, type, type);
	if (this.action != null)
		this.action.resolve(scope);
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		if (this.action != null) {
			this.action.traverse(visitor, scope);
		}
		this.condition.traverse(visitor, scope);
	}
	visitor.endVisit(this, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/726.java