error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3945.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3945.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3945.java
text:
```scala
T@@ypeBinding type = condition.resolveTypeExpecting(scope, TypeBinding.BOOLEAN);

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
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class ForStatement extends Statement {
	
	public Statement[] initializations;
	public Expression condition;
	public Statement[] increments;
	public Statement action;

	//when there is no local declaration, there is no need of a new scope
	//scope is positionned either to a new scope, or to the "upper"scope (see resolveType)
	public boolean neededScope;
	public BlockScope scope;

	private Label breakLabel, continueLabel;

	// for local variables table attributes
	int preCondInitStateIndex = -1;
	int condIfTrueInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public ForStatement(
		Statement[] initializations,
		Expression condition,
		Statement[] increments,
		Statement action,
		boolean neededScope,
		int s,
		int e) {

		this.sourceStart = s;
		this.sourceEnd = e;
		this.initializations = initializations;
		this.condition = condition;
		this.increments = increments;
		this.action = action;
		// remember useful empty statement
		if (action instanceof EmptyStatement) action.bits |= IsUsefulEmptyStatement;
		this.neededScope = neededScope;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
			
		breakLabel = new Label();
		continueLabel = new Label();

		// process the initializations
		if (initializations != null) {
			for (int i = 0, count = initializations.length; i < count; i++) {
				flowInfo = initializations[i].analyseCode(scope, flowContext, flowInfo);
			}
		}
		preCondInitStateIndex =
			currentScope.methodScope().recordInitializationStates(flowInfo);

		Constant cst = this.condition == null ? null : this.condition.constant;
		boolean isConditionTrue = cst == null || (cst != Constant.NotAConstant && cst.booleanValue() == true);
		boolean isConditionFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);

		cst = this.condition == null ? null : this.condition.optimizedBooleanConstant();
		boolean isConditionOptimizedTrue = cst == null ||  (cst != Constant.NotAConstant && cst.booleanValue() == true);
		boolean isConditionOptimizedFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);
		
		// process the condition
		LoopingFlowContext condLoopContext = null;
		FlowInfo condInfo = flowInfo.copy().unconditionalInits().discardNullRelatedInitializations();
		if (condition != null) {
			if (!isConditionTrue) {
				condInfo =
					condition.analyseCode(
						scope,
						(condLoopContext =
							new LoopingFlowContext(flowContext, this, null, null, scope)),
						condInfo);
			}
		}

		// process the action
		LoopingFlowContext loopingContext;
		FlowInfo actionInfo;
		if (action == null 
 (action.isEmptyBlock() && currentScope.compilerOptions().complianceLevel <= ClassFileConstants.JDK1_3)) {
			if (condLoopContext != null)
				condLoopContext.complainOnDeferredChecks(scope, condInfo);
			if (isConditionTrue) {
				return FlowInfo.DEAD_END;
			} else {
				if (isConditionFalse){
					continueLabel = null; // for(;false;p());
				}
				actionInfo = condInfo.initsWhenTrue().copy().unconditionalInits().discardNullRelatedInitializations();
				loopingContext =
					new LoopingFlowContext(flowContext, this, breakLabel, continueLabel, scope);
			}
		} else {
			loopingContext =
				new LoopingFlowContext(flowContext, this, breakLabel, continueLabel, scope);
			FlowInfo initsWhenTrue = condInfo.initsWhenTrue();
			condIfTrueInitStateIndex =
				currentScope.methodScope().recordInitializationStates(initsWhenTrue);

				if (isConditionFalse) {
					actionInfo = FlowInfo.DEAD_END;
				} else {
					actionInfo = initsWhenTrue.copy().unconditionalInits().discardNullRelatedInitializations();
					if (isConditionOptimizedFalse){
						actionInfo.setReachMode(FlowInfo.UNREACHABLE);
					}
				}
			if (!this.action.complainIfUnreachable(actionInfo, scope, false)) {
				actionInfo = action.analyseCode(scope, loopingContext, actionInfo);
			}

			// code generation can be optimized when no need to continue in the loop
			if (!actionInfo.isReachable() && !loopingContext.initsOnContinue.isReachable()) {
				continueLabel = null;
			} else {
				if (condLoopContext != null)
					condLoopContext.complainOnDeferredChecks(scope, condInfo);
				actionInfo = actionInfo.mergedWith(loopingContext.initsOnContinue.unconditionalInits());
				loopingContext.complainOnDeferredChecks(scope, actionInfo);
			}
		}
		// for increments
		FlowInfo exitBranch = condInfo.initsWhenFalse();
		exitBranch.addInitializationsFrom(flowInfo); // recover null inits from before condition analysis
		if (continueLabel != null) {
			if (increments != null) {
				LoopingFlowContext loopContext =
					new LoopingFlowContext(flowContext, this, null, null, scope);
				for (int i = 0, count = increments.length; i < count; i++) {
					actionInfo = increments[i].analyseCode(scope, loopContext, actionInfo);
				}
				loopContext.complainOnDeferredChecks(scope, actionInfo);
			}
			exitBranch.addPotentialInitializationsFrom(actionInfo.unconditionalInits());
		}

		//end of loop
		FlowInfo mergedInfo = FlowInfo.mergedOptimizedBranches(
				loopingContext.initsOnBreak, 
				isConditionOptimizedTrue, 
				exitBranch, 
				isConditionOptimizedFalse, 
				!isConditionTrue /*for(;;){}while(true); unreachable(); */);
		mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
		return mergedInfo;
	}

	/**
	 * For statement code generation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((bits & IsReachable) == 0) {
			return;
		}
		int pc = codeStream.position;

		// generate the initializations
		if (initializations != null) {
			for (int i = 0, max = initializations.length; i < max; i++) {
				initializations[i].generateCode(scope, codeStream);
			}
		}
		Constant cst = this.condition == null ? null : this.condition.optimizedBooleanConstant();
		boolean isConditionOptimizedFalse = cst != null && (cst != Constant.NotAConstant && cst.booleanValue() == false);
		if (isConditionOptimizedFalse) {
			condition.generateCode(scope, codeStream, false);
			// May loose some local variable initializations : affecting the local variable attributes
			if (neededScope) {
				codeStream.exitUserScope(scope);
			}
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
				codeStream.addDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
			}
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		
		// label management
		Label actionLabel = new Label(codeStream);
		Label conditionLabel = new Label(codeStream);
		breakLabel.initialize(codeStream);
		if (continueLabel != null) {
			continueLabel.initialize(codeStream);
		}
		// jump over the actionBlock
		if ((condition != null)
			&& (condition.constant == Constant.NotAConstant)
			&& !((action == null || action.isEmptyBlock()) && (increments == null))) {
			int jumpPC = codeStream.position;
			codeStream.goto_(conditionLabel);
			codeStream.recordPositionsFrom(jumpPC, condition.sourceStart);
		}
		// generate the loop action
		actionLabel.place();
		if (action != null) {
			// Required to fix 1PR0XVS: LFRE:WINNT - Compiler: variable table for method appears incorrect
			if (condIfTrueInitStateIndex != -1) {
				// insert all locals initialized inside the condition into the action generated prior to the condition
				codeStream.addDefinitelyAssignedVariables(
					currentScope,
					condIfTrueInitStateIndex);
			}
			action.generateCode(scope, codeStream);
		}
		// continuation point
		if (continueLabel != null) {
			continueLabel.place();
			// generate the increments for next iteration
			if (increments != null) {
				for (int i = 0, max = increments.length; i < max; i++) {
					increments[i].generateCode(scope, codeStream);
				}
			}
		}

		// May loose some local variable initializations : affecting the local variable attributes
		if (preCondInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, preCondInitStateIndex);
		}

		// generate the condition
		conditionLabel.place();
		if ((condition != null) && (condition.constant == Constant.NotAConstant)) {
			condition.generateOptimizedBoolean(scope, codeStream, actionLabel, null, true);
		} else {
			if (continueLabel != null) {
				codeStream.goto_(actionLabel);
			}
		}
		breakLabel.place();

		// May loose some local variable initializations : affecting the local variable attributes
		if (neededScope) {
			codeStream.exitUserScope(scope);
		}
		if (mergedInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
			codeStream.addDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public StringBuffer printStatement(int tab, StringBuffer output) {

		printIndent(tab, output).append("for ("); //$NON-NLS-1$
		//inits
		if (initializations != null) {
			for (int i = 0; i < initializations.length; i++) {
				//nice only with expressions
				if (i > 0) output.append(", "); //$NON-NLS-1$
				initializations[i].print(0, output);
			}
		}
		output.append("; "); //$NON-NLS-1$
		//cond
		if (condition != null) condition.printExpression(0, output);
		output.append("; "); //$NON-NLS-1$
		//updates
		if (increments != null) {
			for (int i = 0; i < increments.length; i++) {
				if (i > 0) output.append(", "); //$NON-NLS-1$
				increments[i].print(0, output);
			}
		}
		output.append(") "); //$NON-NLS-1$
		//block
		if (action == null)
			output.append(';');
		else {
			output.append('\n');
			action.printStatement(tab + 1, output);
		}
		return output.append(';');
	}

	public void resolve(BlockScope upperScope) {

		// use the scope that will hold the init declarations
		scope = neededScope ? new BlockScope(upperScope) : upperScope;
		if (initializations != null)
			for (int i = 0, length = initializations.length; i < length; i++)
				initializations[i].resolve(scope);
		if (condition != null) {
			TypeBinding type = condition.resolveTypeExpecting(scope, BooleanBinding);
			condition.computeConversion(scope, type, type);
		}
		if (increments != null)
			for (int i = 0, length = increments.length; i < length; i++)
				increments[i].resolve(scope);
		if (action != null)
			action.resolve(scope);
	}
	
	public void traverse(
		ASTVisitor visitor,
		BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			if (initializations != null) {
				int initializationsLength = initializations.length;
				for (int i = 0; i < initializationsLength; i++)
					initializations[i].traverse(visitor, scope);
			}

			if (condition != null)
				condition.traverse(visitor, scope);

			if (increments != null) {
				int incrementsLength = increments.length;
				for (int i = 0; i < incrementsLength; i++)
					increments[i].traverse(visitor, scope);
			}

			if (action != null)
				action.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3945.java