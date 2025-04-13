error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5256.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5256.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5256.java
text:
```scala
F@@lowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo);

package org.eclipse.jdt.internal.compiler.ast;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class AND_AND_Expression extends BinaryExpression {
	//dedicated treatment for the &&
	int rightInitStateIndex = -1;
	int mergedInitStateIndex = -1;

public AND_AND_Expression(Expression left, Expression right,int operator) {
	super(left,right,operator);
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

	Constant opConstant;
	if ((opConstant = left.constant) != NotAConstant) {
		if (opConstant.booleanValue() == true) { 
			// TRUE && anything
			FlowInfo mergedInfo = right.analyseCode(currentScope, flowContext, flowInfo);
			mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
			return mergedInfo;
		} else { 
			// FALSE && anything
			return flowInfo;
		}
	}
	if ((opConstant = right.constant) != NotAConstant) {
		if (opConstant.booleanValue() == true) { 
			// anything && TRUE
			FlowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo);
			mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
			return mergedInfo;
		} else { 
			// anything && FALSE
			// whatever is on the left, we will fail, so the result must merge the left inits when answering
			// initsWhenFalse. the initsWhenTrue are undetermined, since this path will be fake reachable...
			FlowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
			mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
			return mergedInfo;			
		}
	}
	if ((opConstant = left.conditionalConstant()) != NotAConstant){
		if (opConstant.booleanValue() == false){ 
			// something eq. FALSE && anything
			FlowInfo mergedInfo = left.analyseCode(currentScope, flowContext, flowInfo);
			mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
			right.analyseCode(currentScope, flowContext, mergedInfo.copy().markAsFakeReachable(true));
			return mergedInfo;
		} 
	}
	FlowInfo leftInfo = left.analyseCode(currentScope, flowContext, flowInfo);
	FlowInfo rightInfo = leftInfo.initsWhenTrue().copy();
	rightInitStateIndex = currentScope.methodScope().recordInitializationStates(rightInfo);
	rightInfo = right.analyseCode(currentScope, flowContext, rightInfo);
	FlowInfo mergedInfo = FlowInfo.conditional(
													rightInfo.initsWhenTrue(), 
													leftInfo.initsWhenFalse().unconditionalInits().mergedWith(rightInfo.initsWhenFalse().copy().unconditionalInits()));
	mergedInitStateIndex = currentScope.methodScope().recordInitializationStates(mergedInfo);
	return mergedInfo;
}
public void computeConstant(BlockScope scope, int leftId, int rightId) {
	//the TC has been done so leftId and rightId are both equal to T_boolean

	Constant cst;
	if ((cst = left.constant) != NotAConstant) {
		if (cst.booleanValue() == false) { // false && x --> false
			constant = cst; // inlined to constant(false)
		} else { // true && x --> x
			if ((constant = right.constant) == NotAConstant) {
				// compute conditionalConstant
				optimizedBooleanConstant(leftId, (bits & OperatorMASK) >> OperatorSHIFT, rightId);
			}
		}
	} else {
		constant = NotAConstant;
		// compute conditionalConstant
		optimizedBooleanConstant(leftId, (bits & OperatorMASK) >> OperatorSHIFT, rightId);
	}
}
/**
 * Code generation for a binary operation
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	Label falseLabel, endLabel;
	if (constant != Constant.NotAConstant) {
		// inlined value
		if (valueRequired)
			codeStream.generateConstant(constant, implicitConversion);
		codeStream.recordPositionsFrom(pc, this);
		return;
	}
	bits |= OnlyValueRequiredMASK;
	generateOptimizedConditionalAnd(currentScope, codeStream, null, (falseLabel = new Label(codeStream)), valueRequired);
	/* improving code gen for such a case: boolean b = i < 0 && false
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
	codeStream.recordPositionsFrom(pc, this);
}
/**
 * Boolean operator code generation
 *	Optimized operations are: &&
 */
public void generateOptimizedBoolean(BlockScope currentScope, CodeStream codeStream, Label trueLabel, Label falseLabel, boolean valueRequired) {
	if ((constant != Constant.NotAConstant) && (constant.typeID() == T_boolean)) {
		int pc = codeStream.position;
		if (constant.booleanValue() == true) {
			// constant == true
			if (valueRequired) {
				if (falseLabel == null) {
					// implicit falling through the FALSE case
					if (trueLabel != null) {
						codeStream.goto_(trueLabel);
					}
				}
			}
		} else {
			if (valueRequired) {
				if (falseLabel != null) {
					// implicit falling through the TRUE case
					if (trueLabel == null) {
						codeStream.goto_(falseLabel);
					}
				}
			}
		}
		codeStream.recordPositionsFrom(pc, this);
		return;
	}
	generateOptimizedConditionalAnd(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
}
/**
 * Boolean generation for &&
 */
public void generateOptimizedConditionalAnd(BlockScope currentScope, CodeStream codeStream, Label trueLabel, Label falseLabel, boolean valueRequired) {

	int pc = codeStream.position;
	Constant condConst;
	if ((condConst = left.conditionalConstant()) != NotAConstant) {
		if (condConst.booleanValue() == true) {
			// <something equivalent to true> && x
			left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, false);
			if (rightInitStateIndex != -1){
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			if ((bits & OnlyValueRequiredMASK) != 0){
				right.generateCode(currentScope, codeStream, valueRequired);
			} else {
				right.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
			}
		} else {
			// <something equivalent to false> && x
			left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, false);
			if (valueRequired) {
				if ((bits & OnlyValueRequiredMASK) != 0) {
					codeStream.iconst_0();
				} else {
					if (falseLabel != null) {
						// implicit falling through the TRUE case
						codeStream.goto_(falseLabel);
					}
				}
			}
		}
		codeStream.recordPositionsFrom(pc, this);
		if (mergedInitStateIndex != -1){
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
		}
		return;
	}
	if ((condConst = right.conditionalConstant()) != NotAConstant) {
		if (condConst.booleanValue() == true) {
			// x && <something equivalent to true>
			if ((bits & OnlyValueRequiredMASK) != 0){
				left.generateCode(currentScope, codeStream, valueRequired);
			} else {
				left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, valueRequired);
			}
			if (rightInitStateIndex != -1){
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			right.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, false);
		} else {
			// x && <something equivalent to false>
			left.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, false);
			if (rightInitStateIndex != -1){
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			right.generateOptimizedBoolean(currentScope, codeStream, trueLabel, falseLabel, false);
			if (valueRequired) {
				if ((bits & OnlyValueRequiredMASK) != 0) {
					codeStream.iconst_0();
				} else {
					if (falseLabel != null) {
						// implicit falling through the TRUE case
						codeStream.goto_(falseLabel);
					}
				}
			}
		}
		codeStream.recordPositionsFrom(pc, this);
		if (mergedInitStateIndex != -1){
			codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
		}
		return;
	}
	// default case
	if (falseLabel == null) {
		if (trueLabel != null) {
			// implicit falling through the FALSE case
			Label internalFalseLabel = new Label(codeStream);
			left.generateOptimizedBoolean(currentScope, codeStream, null, internalFalseLabel, true);
			if (rightInitStateIndex != -1){
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			right.generateOptimizedBoolean(currentScope, codeStream, trueLabel, null, valueRequired);
			internalFalseLabel.place();
		}
	} else {
		// implicit falling through the TRUE case
		if (trueLabel == null) {
			left.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel, true);
			if (rightInitStateIndex != -1){
				codeStream.addDefinitelyAssignedVariables(currentScope, rightInitStateIndex);
			}
			right.generateOptimizedBoolean(currentScope, codeStream, null, falseLabel, valueRequired);
		} else {
			// no implicit fall through TRUE/FALSE --> should never occur
		}
	}
	codeStream.recordPositionsFrom(pc, this);
	if (mergedInitStateIndex != -1){
		codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5256.java