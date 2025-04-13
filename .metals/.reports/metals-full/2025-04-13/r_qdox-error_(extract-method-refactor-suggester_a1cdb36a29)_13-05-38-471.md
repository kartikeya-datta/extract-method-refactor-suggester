error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4355.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4355.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4355.java
text:
```scala
i@@f (falseLabel.forwardReferenceCount() > 0) {

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

//dedicated treatment for the &&
public class AND_AND_Expression extends BinaryExpression {

	int rightInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public AND_AND_Expression(Expression left, Expression right, int operator) {
		super(left, right, operator);
	}

	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

		Constant cst = this.left.optimizedBooleanConstant();
		boolean isLeftOptimizedTrue = cst != Constant.NotAConstant && cst.booleanValue() == true;
		boolean isLeftOptimizedFalse = cst != Constant.NotAConstant && cst.booleanValue() == false;

		if (isLeftOptimizedTrue) {
			// TRUE && anything
			// need to be careful of scenario:
			//  (x && y) && !z, if passing the left info to the right, it would
			// be swapped by the !
			FlowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo)
					.unconditionalInits();
			mergedInfo = right.analyseCode(currentScope, flowContext, mergedInfo);
			mergedInitStateIndex = currentScope.methodScope()
					.recordInitializationStates(mergedInfo);
			return mergedInfo;
		}

		FlowInfo leftInfo = left.analyseCode(currentScope, flowContext, flowInfo);
		// need to be careful of scenario:
		//  (x && y) && !z, if passing the left info to the right, it would be
		// swapped by the !
		FlowInfo rightInfo = leftInfo.initsWhenTrue().unconditionalCopy();
		rightInitStateIndex = currentScope.methodScope().recordInitializationStates(rightInfo);

		int previousMode = rightInfo.reachMode();
		if (isLeftOptimizedFalse) {
			rightInfo.setReachMode(FlowInfo.UNREACHABLE);
		}
		rightInfo = right.analyseCode(currentScope, flowContext, rightInfo);
		FlowInfo mergedInfo = FlowInfo.conditional(
				rightInfo.safeInitsWhenTrue(), 
				leftInfo.initsWhenFalse().unconditionalInits().mergedWith(
						rightInfo.initsWhenFalse().setReachMode(previousMode).unconditionalInits()));
		// reset after trueMergedInfo got extracted
		mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
		return mergedInfo;
	}

	/**
	 * Code generation for a binary operation
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

		int pc = codeStream.position;
		if (constant != Constant.NotAConstant) {
			// inlined value
			if (valueRequired)
				codeStream.generateConstant(constant, implicitConversion);
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		Constant cst = right.constant;
		if (cst != Constant.NotAConstant) {
			// <expr> && true --> <expr>
			if (cst.booleanValue() == true) {
				this.left.generateCode(currentScope, codeStream, valueRequired);
			} else {
				// <expr> && false --> false
				this.left.generateCode(currentScope, codeStream, false);
				if (valueRequired) codeStream.iconst_0();
			}
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
			}			
			codeStream.generateImplicitConversion(implicitConversion);
			codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		
		BranchLabel falseLabel = new BranchLabel(codeStream), endLabel;
		cst = left.optimizedBooleanConstant();
		boolean leftIsConst = cst != Constant.NotAConstant;
		boolean leftIsTrue = leftIsConst && cst.booleanValue() == true;

		cst = right.optimizedBooleanConstant();
		boolean rightIsConst = cst != Constant.NotAConstant;
		boolean rightIsTrue = rightIsConst && cst.booleanValue() == true;

		generateOperands : {
			if (leftIsConst) {
				left.generateCode(currentScope, codeStream, false);
				if (!leftIsTrue) {
					break generateOperands; // no need to generate right operand
				}
			} else {
				left.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel, true); 
				// need value, e.g. if (a == 1 && ((b = 2) > 0)) {} -> shouldn't initialize 'b' if a!=1 
			}
			if (rightInitStateIndex != -1) {
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			if (rightIsConst) {
				right.generateCode(currentScope, codeStream, false);
			} else {
				right.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel, valueRequired);
			}
		}
		if (mergedInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
		}
		/*
		 * improving code gen for such a case: boolean b = i < 0 && false since
		 * the label has never been used, we have the inlined value on the
		 * stack.
		 */
		if (valueRequired) {
			if (leftIsConst && !leftIsTrue) {
				codeStream.iconst_0();
				codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
			} else {
				if (rightIsConst && !rightIsTrue) {
					codeStream.iconst_0();
					codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
				} else {
					codeStream.iconst_1();
				}
				if (falseLabel.forwardReferenceCount > 0) {
					if ((bits & IsReturnedValue) != 0) {
						codeStream.generateImplicitConversion(this.implicitConversion);
						codeStream.generateReturnBytecode(this);
						falseLabel.place();
						codeStream.iconst_0();
					} else {
						codeStream.goto_(endLabel = new BranchLabel(codeStream));
						codeStream.decrStackSize(1);
						falseLabel.place();
						codeStream.iconst_0();
						endLabel.place();
					}
				} else {
					falseLabel.place();
				}
			}
			codeStream.generateImplicitConversion(implicitConversion);
			codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
		} else {
			falseLabel.place();
		}
	}

	/**
	 * Boolean operator code generation Optimized operations are: &&
	 */
	public void generateOptimizedBoolean(BlockScope currentScope, CodeStream codeStream,
			BranchLabel trueLabel, BranchLabel falseLabel, boolean valueRequired) {

		if (constant != Constant.NotAConstant) {
			super.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel,
					valueRequired);
			return;
		}

		// <expr> && true --> <expr>
		Constant cst = right.constant;
		if (cst != Constant.NotAConstant && cst.booleanValue() == true) {
			int pc = codeStream.position;
			this.left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
			}			
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		cst = left.optimizedBooleanConstant();
		boolean leftIsConst = cst != Constant.NotAConstant;
		boolean leftIsTrue = leftIsConst && cst.booleanValue() == true;

		cst = right.optimizedBooleanConstant();
		boolean rightIsConst = cst != Constant.NotAConstant;
		boolean rightIsTrue = rightIsConst && cst.booleanValue() == true;

		// default case
		generateOperands : {
			if (falseLabel == null) {
				if (trueLabel != null) {
					// implicit falling through the FALSE case
					BranchLabel internalFalseLabel = new BranchLabel(codeStream);
					left.generateOptimizedBoolean(currentScope, codeStream, null,
							internalFalseLabel, !leftIsConst); 
					// need value, e.g. if (a == 1 && ((b = 2) > 0)) {} -> shouldn't initialize 'b' if a!=1
					if (leftIsConst && !leftIsTrue) {
						internalFalseLabel.place();
						break generateOperands; // no need to generate right operand
					}
					if (rightInitStateIndex != -1) {
						codeStream
								.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
					}
					right.generateOptimizedBoolean(currentScope, codeStream, trueLabel, null,
							valueRequired && !rightIsConst);
					if (valueRequired && rightIsConst && rightIsTrue) {
						codeStream.goto_(trueLabel);
						codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
					}
					internalFalseLabel.place();
				}
			} else {
				// implicit falling through the TRUE case
				if (trueLabel == null) {
					left.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel, !leftIsConst); 
					// need value, e.g. if (a == 1 && ((b = 2) > 0)) {} -> shouldn't initialize 'b' if a!=1
					if (leftIsConst && !leftIsTrue) {
						codeStream.goto_(falseLabel);
						codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
						break generateOperands; // no need to generate right operand
					}
					if (rightInitStateIndex != -1) {
						codeStream
								.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
					}
					right.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel,
							valueRequired && !rightIsConst);
					if (valueRequired && rightIsConst && !rightIsTrue) {
						codeStream.goto_(falseLabel);
						codeStream.updateLastRecordedEndPC(currentScope, codeStream.position);
					}
				} else {
					// no implicit fall through TRUE/FALSE --> should never occur
				}
			}
		}
		if (mergedInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
		}
	}

	public boolean isCompactableOperation() {
		return false;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		if (visitor.visit(this, scope)) {
			left.traverse(visitor, scope);
			right.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4355.java