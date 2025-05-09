error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8821.java
text:
```scala
g@@enerateReturnBytecode(codeStream);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public class ReturnStatement extends Statement {
		
	public Expression expression;
	public SubRoutineStatement[] subroutines;
	public LocalVariableBinding saveValueVariable;
	public int initStateIndex = -1;
	
public ReturnStatement(Expression expression, int sourceStart, int sourceEnd) {
	this.sourceStart = sourceStart;
	this.sourceEnd = sourceEnd;
	this.expression = expression ;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {	// here requires to generate a sequence of finally blocks invocations depending corresponding
	// to each of the traversed try statements, so that execution will terminate properly.

	// lookup the label, this should answer the returnContext

	if (this.expression != null) {
		flowInfo = this.expression.analyseCode(currentScope, flowContext, flowInfo);
	}
	this.initStateIndex =
		currentScope.methodScope().recordInitializationStates(flowInfo);
	// compute the return sequence (running the finally blocks)
	FlowContext traversedContext = flowContext;
	int subCount = 0;
	boolean saveValueNeeded = false;
	boolean hasValueToSave = needValueStore();
	do {
		SubRoutineStatement sub;
		if ((sub = traversedContext.subroutine()) != null) {
			if (this.subroutines == null){
				this.subroutines = new SubRoutineStatement[5];
			}
			if (subCount == this.subroutines.length) {
				System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount*2]), 0, subCount); // grow
			}
			this.subroutines[subCount++] = sub;
			if (sub.isSubRoutineEscaping()) {
				saveValueNeeded = false;
				this.bits |= ASTNode.IsAnySubRoutineEscaping;
				break;
			}
		}
		traversedContext.recordReturnFrom(flowInfo.unconditionalInits());

		if (traversedContext instanceof InsideSubRoutineFlowContext) {
			ASTNode node = traversedContext.associatedNode;
			if (node instanceof SynchronizedStatement) {
				this.bits |= ASTNode.IsSynchronized;
			} else if (node instanceof TryStatement) {
				TryStatement tryStatement = (TryStatement) node;
				flowInfo.addInitializationsFrom(tryStatement.subRoutineInits); // collect inits
				if (hasValueToSave) {
					if (this.saveValueVariable == null){ // closest subroutine secret variable is used
						prepareSaveValueLocation(tryStatement);
					}
					saveValueNeeded = true;
				}
			}
		} else if (traversedContext instanceof InitializationFlowContext) {
				currentScope.problemReporter().cannotReturnInInitializer(this);
				return FlowInfo.DEAD_END;
		}
	} while ((traversedContext = traversedContext.parent) != null);
	
	// resize subroutines
	if ((this.subroutines != null) && (subCount != this.subroutines.length)) {
		System.arraycopy(this.subroutines, 0, (this.subroutines = new SubRoutineStatement[subCount]), 0, subCount);
	}

	// secret local variable for return value (note that this can only occur in a real method)
	if (saveValueNeeded) {
		if (this.saveValueVariable != null) {
			this.saveValueVariable.useFlag = LocalVariableBinding.USED;
		}
	} else {
		this.saveValueVariable = null;
		if (((this.bits & ASTNode.IsSynchronized) == 0) && this.expression != null && this.expression.resolvedType == TypeBinding.BOOLEAN) {
			this.expression.bits |= ASTNode.IsReturnedValue;
		}
	}
	return FlowInfo.DEAD_END;
}
 
/**
 * Retrun statement code generation
 *
 *   generate the finallyInvocationSequence.
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	if ((this.bits & ASTNode.IsReachable) == 0) {
		return;
	}
	int pc = codeStream.position;
	boolean alreadyGeneratedExpression = false;
	// generate the expression
	if (needValueStore()) {
		alreadyGeneratedExpression = true;
		this.expression.generateCode(currentScope, codeStream, needValue()); // no value needed if non-returning subroutine
		generateStoreSaveValueIfNecessary(codeStream);
	}
	
	// generation of code responsible for invoking the finally blocks in sequence
	if (this.subroutines != null) {
		Object reusableJSRTarget = this.expression == null ? (Object)TypeBinding.VOID : this.expression.reusableJSRTarget();
		for (int i = 0, max = this.subroutines.length; i < max; i++) {
			SubRoutineStatement sub = this.subroutines[i];
			boolean didEscape = sub.generateSubRoutineInvocation(currentScope, codeStream, reusableJSRTarget, this.initStateIndex, this.saveValueVariable);
			if (didEscape) {
					codeStream.recordPositionsFrom(pc, this.sourceStart);
					SubRoutineStatement.reenterAllExceptionHandlers(this.subroutines, i, codeStream);
					return;
			}
		}
	}
	if (this.saveValueVariable != null) {
		codeStream.addVariable(this.saveValueVariable);
		codeStream.load(this.saveValueVariable);
	}
	if (this.expression != null && !alreadyGeneratedExpression) {
		this.expression.generateCode(currentScope, codeStream, true);
		generateStoreSaveValueIfNecessary(codeStream);
	}
	// output the suitable return bytecode or wrap the value inside a descriptor for doits
	this.generateReturnBytecode(codeStream);
	if (this.saveValueVariable != null) {
		codeStream.removeVariable(this.saveValueVariable);
	}	
	if (this.initStateIndex != -1) {
		codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.initStateIndex);
		codeStream.addDefinitelyAssignedVariables(currentScope, this.initStateIndex);
	}	
	codeStream.recordPositionsFrom(pc, this.sourceStart);
	SubRoutineStatement.reenterAllExceptionHandlers(this.subroutines, -1, codeStream);
}

/**
 * Dump the suitable return bytecode for a return statement
 *
 */
public void generateReturnBytecode(CodeStream codeStream) {
	codeStream.generateReturnBytecode(this.expression);
}

public void generateStoreSaveValueIfNecessary(CodeStream codeStream){
	if (this.saveValueVariable != null) {
		codeStream.store(this.saveValueVariable, false);
	}
}

private boolean needValueStore() {
	return this.expression != null 
					&& (this.expression.constant == Constant.NotAConstant || (this.expression.implicitConversion & TypeIds.BOXING)!= 0)
					&& !(this.expression instanceof NullLiteral);
}

public boolean needValue() {
	return this.saveValueVariable != null 
 (this.bits & ASTNode.IsSynchronized) != 0
 ((this.bits & ASTNode.IsAnySubRoutineEscaping) == 0);
}

public void prepareSaveValueLocation(TryStatement targetTryStatement){
	this.saveValueVariable = targetTryStatement.secretReturnValue;
}

public StringBuffer printStatement(int tab, StringBuffer output){
	printIndent(tab, output).append("return "); //$NON-NLS-1$
	if (this.expression != null )
		this.expression.printExpression(0, output) ;
	return output.append(';');
}

public void resolve(BlockScope scope) {
	MethodScope methodScope = scope.methodScope();
	MethodBinding methodBinding;
	TypeBinding methodType =
		(methodScope.referenceContext instanceof AbstractMethodDeclaration)
			? ((methodBinding = ((AbstractMethodDeclaration) methodScope.referenceContext).binding) == null 
				? null 
				: methodBinding.returnType)
			: TypeBinding.VOID;
	TypeBinding expressionType;
	if (methodType == TypeBinding.VOID) {
		// the expression should be null
		if (this.expression == null)
			return;
		if ((expressionType = this.expression.resolveType(scope)) != null)
			scope.problemReporter().attemptToReturnNonVoidExpression(this, expressionType);
		return;
	}
	if (this.expression == null) {
		if (methodType != null) scope.problemReporter().shouldReturn(methodType, this);
		return;
	}
	this.expression.setExpectedType(methodType); // needed in case of generic method invocation
	if ((expressionType = this.expression.resolveType(scope)) == null) return;
	if (expressionType == TypeBinding.VOID) {
		scope.problemReporter().attemptToReturnVoidValue(this);
		return;
	}
	if (methodType == null) 
		return;

	if (methodType != expressionType) // must call before computeConversion() and typeMismatchError()
		scope.compilationUnitScope().recordTypeConversion(methodType, expressionType);
	if (this.expression.isConstantValueOfTypeAssignableToType(expressionType, methodType)
 expressionType.isCompatibleWith(methodType)) {

		this.expression.computeConversion(scope, methodType, expressionType);
		if (expressionType.needsUncheckedConversion(methodType)) {
		    scope.problemReporter().unsafeTypeConversion(this.expression, expressionType, methodType);
		}
		if (this.expression instanceof CastExpression 
				&& (this.expression.bits & (ASTNode.UnnecessaryCast|ASTNode.DisableUnnecessaryCastCheck)) == 0) {
			CastExpression.checkNeedForAssignedCast(scope, methodType, (CastExpression) this.expression);
		}			
		return;
	} else if (scope.isBoxingCompatibleWith(expressionType, methodType)
 (expressionType.isBaseType()  // narrowing then boxing ?
								&& scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5 // autoboxing
								&& !methodType.isBaseType()
								&& this.expression.isConstantValueOfTypeAssignableToType(expressionType, scope.environment().computeBoxingType(methodType)))) {
		this.expression.computeConversion(scope, methodType, expressionType);
		if (this.expression instanceof CastExpression 
				&& (this.expression.bits & (ASTNode.UnnecessaryCast|ASTNode.DisableUnnecessaryCastCheck)) == 0) {
			CastExpression.checkNeedForAssignedCast(scope, methodType, (CastExpression) this.expression);
		}			return;
	}
	if ((methodType.tagBits & TagBits.HasMissingType) == 0) {
		// no need to complain if return type was missing (avoid secondary error : 220967)
		scope.problemReporter().typeMismatchError(expressionType, methodType, this.expression, null);
	}
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		if (this.expression != null)
			this.expression.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8821.java