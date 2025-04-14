error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/565.java
text:
```scala
a@@ssertInfo.setReachMode(FlowInfo.UNREACHABLE_OR_DEAD);

/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Stephan Herrmann - Contribution for bug 319201 - [null] no warning when unboxing SingleNameReference causes NPE
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.ASTVisitor;

public class AssertStatement extends Statement {

	public Expression assertExpression, exceptionArgument;

	// for local variable attribute
	int preAssertInitStateIndex = -1;
	private FieldBinding assertionSyntheticFieldBinding;

public AssertStatement(	Expression exceptionArgument, Expression assertExpression, int startPosition) {
	this.assertExpression = assertExpression;
	this.exceptionArgument = exceptionArgument;
	this.sourceStart = startPosition;
	this.sourceEnd = exceptionArgument.sourceEnd;
}

public AssertStatement(Expression assertExpression, int startPosition) {
	this.assertExpression = assertExpression;
	this.sourceStart = startPosition;
	this.sourceEnd = assertExpression.sourceEnd;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	this.preAssertInitStateIndex = currentScope.methodScope().recordInitializationStates(flowInfo);

	Constant cst = this.assertExpression.optimizedBooleanConstant();
	if ((this.assertExpression.implicitConversion & TypeIds.UNBOXING) != 0) {
		this.assertExpression.checkNPE(currentScope, flowContext, flowInfo);
	}
	boolean isOptimizedTrueAssertion = cst != Constant.NotAConstant && cst.booleanValue() == true;
	boolean isOptimizedFalseAssertion = cst != Constant.NotAConstant && cst.booleanValue() == false;
	
	flowContext.tagBits |= FlowContext.HIDE_NULL_COMPARISON_WARNING;
	FlowInfo conditionFlowInfo = this.assertExpression.analyseCode(currentScope, flowContext, flowInfo.copy());
	flowContext.tagBits &= ~FlowContext.HIDE_NULL_COMPARISON_WARNING;
	UnconditionalFlowInfo assertWhenTrueInfo = conditionFlowInfo.initsWhenTrue().unconditionalInits();
	FlowInfo assertInfo = conditionFlowInfo.initsWhenFalse();
	if (isOptimizedTrueAssertion) {
		assertInfo.setReachMode(FlowInfo.UNREACHABLE);
	}

	if (this.exceptionArgument != null) {
		// only gets evaluated when escaping - results are not taken into account
		FlowInfo exceptionInfo = this.exceptionArgument.analyseCode(currentScope, flowContext, assertInfo.copy());

		if (isOptimizedTrueAssertion){
			currentScope.problemReporter().fakeReachable(this.exceptionArgument);
		} else {
			flowContext.checkExceptionHandlers(
				currentScope.getJavaLangAssertionError(),
				this,
				exceptionInfo,
				currentScope);
		}
	}

	if (!isOptimizedTrueAssertion){
		// add the assert support in the clinit
		manageSyntheticAccessIfNecessary(currentScope, flowInfo);
	}
	if (isOptimizedFalseAssertion) {
		return flowInfo; // if assertions are enabled, the following code will be unreachable
		// change this if we need to carry null analysis results of the assert
		// expression downstream
	} else {
		CompilerOptions compilerOptions = currentScope.compilerOptions();
		if (!compilerOptions.includeNullInfoFromAsserts) {
			// keep just the initializations info, don't include assert's null info
			// merge initialization info's and then add back the null info from flowInfo to
			// make sure that the empty null info of assertInfo doesnt change flowInfo's null info.
			return ((flowInfo.nullInfoLessUnconditionalCopy()).mergedWith(assertInfo.nullInfoLessUnconditionalCopy())).addNullInfoFrom(flowInfo);
		}
		return flowInfo.mergedWith(assertInfo.nullInfoLessUnconditionalCopy()).
			addInitializationsFrom(assertWhenTrueInfo.discardInitializationInfo());
		// keep the merge from the initial code for the definite assignment
		// analysis, tweak the null part to influence nulls downstream
	}
}

public void generateCode(BlockScope currentScope, CodeStream codeStream) {
	if ((this.bits & IsReachable) == 0) {
		return;
	}
	int pc = codeStream.position;

	if (this.assertionSyntheticFieldBinding != null) {
		BranchLabel assertionActivationLabel = new BranchLabel(codeStream);
		codeStream.fieldAccess(Opcodes.OPC_getstatic, this.assertionSyntheticFieldBinding, null /* default declaringClass */);
		codeStream.ifne(assertionActivationLabel);

		BranchLabel falseLabel;
		this.assertExpression.generateOptimizedBoolean(currentScope, codeStream, (falseLabel = new BranchLabel(codeStream)), null , true);
		codeStream.newJavaLangAssertionError();
		codeStream.dup();
		if (this.exceptionArgument != null) {
			this.exceptionArgument.generateCode(currentScope, codeStream, true);
			codeStream.invokeJavaLangAssertionErrorConstructor(this.exceptionArgument.implicitConversion & 0xF);
		} else {
			codeStream.invokeJavaLangAssertionErrorDefaultConstructor();
		}
		codeStream.athrow();

		// May loose some local variable initializations : affecting the local variable attributes
		if (this.preAssertInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.preAssertInitStateIndex);
		}
		falseLabel.place();
		assertionActivationLabel.place();
	} else {
		// May loose some local variable initializations : affecting the local variable attributes
		if (this.preAssertInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, this.preAssertInitStateIndex);
		}
	}
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}

public void resolve(BlockScope scope) {
	this.assertExpression.resolveTypeExpecting(scope, TypeBinding.BOOLEAN);
	if (this.exceptionArgument != null) {
		TypeBinding exceptionArgumentType = this.exceptionArgument.resolveType(scope);
		if (exceptionArgumentType != null){
		    int id = exceptionArgumentType.id;
		    switch(id) {
				case T_void :
					scope.problemReporter().illegalVoidExpression(this.exceptionArgument);
					//$FALL-THROUGH$
				default:
				    id = T_JavaLangObject;
				//$FALL-THROUGH$
				case T_boolean :
				case T_byte :
				case T_char :
				case T_short :
				case T_double :
				case T_float :
				case T_int :
				case T_long :
				case T_JavaLangString :
					this.exceptionArgument.implicitConversion = (id << 4) + id;
			}
		}
	}
}

public void traverse(ASTVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		this.assertExpression.traverse(visitor, scope);
		if (this.exceptionArgument != null) {
			this.exceptionArgument.traverse(visitor, scope);
		}
	}
	visitor.endVisit(this, scope);
}

public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {
	if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
		// need assertion flag: $assertionsDisabled on outer most source clas
		// (in case of static member of interface, will use the outermost static member - bug 22334)
		SourceTypeBinding outerMostClass = currentScope.enclosingSourceType();
		while (outerMostClass.isLocalType()) {
			ReferenceBinding enclosing = outerMostClass.enclosingType();
			if (enclosing == null || enclosing.isInterface()) break;
			outerMostClass = (SourceTypeBinding) enclosing;
		}
		this.assertionSyntheticFieldBinding = outerMostClass.addSyntheticFieldForAssert(currentScope);

		// find <clinit> and enable assertion support
		TypeDeclaration typeDeclaration = outerMostClass.scope.referenceType();
		AbstractMethodDeclaration[] methods = typeDeclaration.methods;
		for (int i = 0, max = methods.length; i < max; i++) {
			AbstractMethodDeclaration method = methods[i];
			if (method.isClinit()) {
				((Clinit) method).setAssertionSupport(this.assertionSyntheticFieldBinding, currentScope.compilerOptions().sourceLevel < ClassFileConstants.JDK1_5);
				break;
			}
		}
	}
}

public StringBuffer printStatement(int tab, StringBuffer output) {
	printIndent(tab, output);
	output.append("assert "); //$NON-NLS-1$
	this.assertExpression.printExpression(0, output);
	if (this.exceptionArgument != null) {
		output.append(": "); //$NON-NLS-1$
		this.exceptionArgument.printExpression(0, output);
	}
	return output.append(';');
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/565.java