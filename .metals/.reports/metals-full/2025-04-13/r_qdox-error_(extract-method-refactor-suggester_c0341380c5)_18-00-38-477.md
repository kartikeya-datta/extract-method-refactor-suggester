error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7450.java
text:
```scala
r@@ightInfo.setReachMode(mode); // reset after falseInfo got extracted

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

//dedicated treatment for the ||
public class OR_OR_Expression extends BinaryExpression {

	int rightInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public OR_OR_Expression(Expression left, Expression right, int operator) {
		super(left, right, operator);
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {

		Constant cst = this.left.optimizedBooleanConstant();
		boolean isLeftOptimizedTrue = cst != NotAConstant && cst.booleanValue() == true;
		boolean isLeftOptimizedFalse = cst != NotAConstant && cst.booleanValue() == false;

		if (isLeftOptimizedFalse) {
			// FALSE || anything
			 // need to be careful of scenario:
			//		(x || y) || !z, if passing the left info to the right, it would be swapped by the !
			FlowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
			mergedInfo = right.analyseCode(currentScope, flowContext, mergedInfo);
			mergedInitStateIndex =
				currentScope.methodScope().recordInitializationStates(mergedInfo);
			return mergedInfo;
		}

		FlowInfo leftInfo = left.analyseCode(currentScope, flowContext, flowInfo);
	
		 // need to be careful of scenario:
		//		(x || y) || !z, if passing the left info to the right, it would be swapped by the !
		FlowInfo rightInfo = leftInfo.initsWhenFalse().unconditionalInits().copy();
		rightInitStateIndex =
			currentScope.methodScope().recordInitializationStates(rightInfo);

		int mode = rightInfo.reachMode();
		if (isLeftOptimizedTrue){
			rightInfo.setReachMode(FlowInfo.UNREACHABLE); 
		}
		rightInfo = right.analyseCode(currentScope, flowContext, rightInfo);
		FlowInfo falseInfo = rightInfo.initsWhenFalse().copy();
		falseInfo.setReachMode(rightInfo.reachMode()); // so merge works fine
		rightInfo.setReachMode(mode); // reset after trueInfo got extracted

		FlowInfo mergedInfo = FlowInfo.conditional(
					// merging two true initInfos for such a negative case: if ((t && (b = t)) || f) r = b; // b may not have been initialized
					leftInfo.initsWhenTrue().copy().unconditionalInits().mergedWith(
						rightInfo.initsWhenTrue().copy().unconditionalInits()),
					rightInfo.initsWhenFalse().copy());
		mergedInitStateIndex =
			currentScope.methodScope().recordInitializationStates(mergedInfo);
		return mergedInfo;
	}

	/**
	 * Code generation for a binary operation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 * @param valueRequired boolean
	 */
	public void generateCode(
		BlockScope currentScope,
		CodeStream codeStream,
		boolean valueRequired) {
		int pc = codeStream.position;
		Label falseLabel, endLabel;
		if (constant != Constant.NotAConstant) {
			if (valueRequired)
				codeStream.generateConstant(constant, implicitConversion);
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		bits |= OnlyValueRequiredMASK;
		generateOptimizedBoolean(
			currentScope,
			codeStream,
			null,
			(falseLabel = new Label(codeStream)),
			valueRequired);
		/*  improving code gen for such a case:		boolean b = i < 0 || true; 
		 * since the label has never been used, we have the inlined value on the stack. */
		if (falseLabel.hasForwardReferences()) {
			if (valueRequired) {
				codeStream.iconst_1();
				if ((bits & ValueForReturnMASK) != 0) {
					codeStream.ireturn();
					falseLabel.place();
					codeStream.iconst_0();
				} else {
					codeStream.goto_(endLabel = new Label(codeStream));
					codeStream.decrStackSize(1);
					falseLabel.place();
					codeStream.iconst_0();
					endLabel.place();
				}
			} else {
				falseLabel.place();
			}
		}
		if (valueRequired) {
			codeStream.generateImplicitConversion(implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/**
	 * Boolean operator code generation
	 *	Optimized operations are: ||
	 */
	public void generateOptimizedBoolean(
		BlockScope currentScope,
		CodeStream codeStream,
		Label trueLabel,
		Label falseLabel,
		boolean valueRequired) {
		if (constant != Constant.NotAConstant) {
			super.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
			return;
		}
		Constant condConst;
		if ((condConst = left.optimizedBooleanConstant()) != NotAConstant) {
			if (condConst.booleanValue() == true) {
				// <something equivalent to true> || x
				left.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					falseLabel,
					false);
				if (valueRequired) {
					if ((bits & OnlyValueRequiredMASK) != 0) {
						codeStream.iconst_1();
					} else {
						if (trueLabel != null) {
							codeStream.goto_(trueLabel);
						}
					}
				}
				// reposition the endPC
				codeStream.updateLastRecordedEndPC(codeStream.position);					
			} else {
				// <something equivalent to false> || x
				left.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					falseLabel,
					false);
				if (rightInitStateIndex != -1) {
					codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
				}
				if ((bits & OnlyValueRequiredMASK) != 0) {
					right.generateCode(currentScope, codeStream, valueRequired);
				} else {
					right.generateOptimizedBoolean(
						currentScope,
						codeStream,
						trueLabel,
						falseLabel,
						valueRequired);
				}
			}
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(
					currentScope,
					mergedInitStateIndex);
			}
			return;
		}
		if ((condConst = right.optimizedBooleanConstant()) != NotAConstant) {
			if (condConst.booleanValue() == true) {
				// x || <something equivalent to true>
				left.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					falseLabel,
					false);
				if (rightInitStateIndex != -1) {
					codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
				}
				right.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					falseLabel,
					false);
				if (valueRequired) {
					if ((bits & OnlyValueRequiredMASK) != 0) {
						codeStream.iconst_1();
					} else {
						if (trueLabel != null) {
							codeStream.goto_(trueLabel);
						}
					}
				}
				// reposition the endPC
				codeStream.updateLastRecordedEndPC(codeStream.position);					
			} else {
				// x || <something equivalent to false>
				if ((bits & OnlyValueRequiredMASK) != 0) {
					left.generateCode(currentScope, codeStream, valueRequired);
				} else {
					left.generateOptimizedBoolean(
						currentScope,
						codeStream,
						trueLabel,
						falseLabel,
						valueRequired);
				}
				if (rightInitStateIndex != -1) {
					codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
				}
				right.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					falseLabel,
					false);
			}
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(
					currentScope,
					mergedInitStateIndex);
			}
			return;
		}
		// default case
		if (falseLabel == null) {
			if (trueLabel != null) {
				// implicit falling through the FALSE case
				left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, null, true);
				right.generateOptimizedBoolean(
					currentScope,
					codeStream,
					trueLabel,
					null,
					valueRequired);
			}
		} else {
			// implicit falling through the TRUE case
			if (trueLabel == null) {
				Label internalTrueLabel = new Label(codeStream);
				left.generateOptimizedBoolean(
					currentScope,
					codeStream,
					internalTrueLabel,
					null,
					true);
				if (rightInitStateIndex != -1) {
					codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
				}
				right.generateOptimizedBoolean(
					currentScope,
					codeStream,
					null,
					falseLabel,
					valueRequired);
				internalTrueLabel.place();
			} else {
				// no implicit fall through TRUE/FALSE --> should never occur
			}
		}
		if (mergedInitStateIndex != -1) {
			codeStream.removeNotDefinitelyAssignedVariables(
				currentScope,
				mergedInitStateIndex);
		}
	}

	public boolean isCompactableOperation() {
		return false;
	}

	public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7450.java