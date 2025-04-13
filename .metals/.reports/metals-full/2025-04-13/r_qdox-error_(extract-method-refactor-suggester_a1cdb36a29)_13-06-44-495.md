error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/223.java
text:
```scala
i@@f ((bits & IsReachable) == 0) {

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

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.ASTVisitor;

public class AssertStatement extends Statement {
	
	public Expression assertExpression, exceptionArgument;

	// for local variable attribute
	int preAssertInitStateIndex = -1;
	private FieldBinding assertionSyntheticFieldBinding;
	
	public AssertStatement(
		Expression exceptionArgument,
		Expression assertExpression,
		int startPosition) {
			
		this.assertExpression = assertExpression;
		this.exceptionArgument = exceptionArgument;
		sourceStart = startPosition;
		sourceEnd = exceptionArgument.sourceEnd;
	}

	public AssertStatement(Expression assertExpression, int startPosition) {

		this.assertExpression = assertExpression;
		sourceStart = startPosition;
		sourceEnd = assertExpression.sourceEnd;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
			
		preAssertInitStateIndex = currentScope.methodScope().recordInitializationStates(flowInfo);

		Constant cst = this.assertExpression.optimizedBooleanConstant();		
		boolean isOptimizedTrueAssertion = cst != NotAConstant && cst.booleanValue() == true;
		boolean isOptimizedFalseAssertion = cst != NotAConstant && cst.booleanValue() == false;

		FlowInfo assertInfo = flowInfo.copy();
		if (isOptimizedTrueAssertion) {
			assertInfo.setReachMode(FlowInfo.UNREACHABLE);
		}
		assertInfo = assertExpression.analyseCode(currentScope, flowContext, assertInfo).unconditionalInits();
		
		if (exceptionArgument != null) {
			// only gets evaluated when escaping - results are not taken into account
			FlowInfo exceptionInfo = exceptionArgument.analyseCode(currentScope, flowContext, assertInfo.copy()); 
			
			if (!isOptimizedTrueAssertion){
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
		} else {
			return flowInfo.mergedWith(assertInfo.unconditionalInits()); 
		}
	}

	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

		if ((bits & IsReachableMASK) == 0) {
			return;
		}
		int pc = codeStream.position;
	
		if (this.assertionSyntheticFieldBinding != null) {
			Label assertionActivationLabel = new Label(codeStream);
			codeStream.getstatic(this.assertionSyntheticFieldBinding);
			codeStream.ifne(assertionActivationLabel);
			
			Label falseLabel = new Label(codeStream);
			this.assertExpression.generateOptimizedBoolean(currentScope, codeStream, (falseLabel = new Label(codeStream)), null , true);
			codeStream.newJavaLangAssertionError();
			codeStream.dup();
			if (exceptionArgument != null) {
				exceptionArgument.generateCode(currentScope, codeStream, true);
				codeStream.invokeJavaLangAssertionErrorConstructor(exceptionArgument.implicitConversion & 0xF);
			} else {
				codeStream.invokeJavaLangAssertionErrorDefaultConstructor();
			}
			codeStream.athrow();
			falseLabel.place();
			assertionActivationLabel.place();
		}
		
		// May loose some local variable initializations : affecting the local variable attributes
		if (preAssertInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, preAssertInitStateIndex);
		}	
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public void resolve(BlockScope scope) {

		assertExpression.resolveTypeExpecting(scope, BooleanBinding);
		if (exceptionArgument != null) {
			TypeBinding exceptionArgumentType = exceptionArgument.resolveType(scope);
			if (exceptionArgumentType != null){
			    int id = exceptionArgumentType.id;
			    switch(id) {
					case T_void :
						scope.problemReporter().illegalVoidExpression(exceptionArgument);
					default:
					    id = T_JavaLangObject;
					case T_boolean :
					case T_byte :
					case T_char :
					case T_short :
					case T_double :
					case T_float :
					case T_int :
					case T_long :
					case T_JavaLangString :
						exceptionArgument.implicitConversion = (id << 4) + id;
				}
			}
		}
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {

		if (visitor.visit(this, scope)) {
			assertExpression.traverse(visitor, scope);
			if (exceptionArgument != null) {
				exceptionArgument.traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, scope);
	}	
	
	public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo) {

		if (!flowInfo.isReachable()) return;
		
		// need assertion flag: $assertionsDisabled on outer most source clas
		// (in case of static member of interface, will use the outermost static member - bug 22334)
		SourceTypeBinding outerMostClass = currentScope.enclosingSourceType();
		while (outerMostClass.isLocalType()){
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
				((Clinit) method).setAssertionSupport(assertionSyntheticFieldBinding, currentScope.compilerOptions().sourceLevel < ClassFileConstants.JDK1_5);
				break;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/223.java