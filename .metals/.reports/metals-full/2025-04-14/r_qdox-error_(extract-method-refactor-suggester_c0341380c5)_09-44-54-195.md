error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7895.java
text:
```scala
i@@nt result = OperatorSignatures[tableId][(expressionId << 4) + expressionId];

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.IAbstractSyntaxTreeVisitor;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class UnaryExpression extends OperatorExpression {
	
	public Expression expression;
	public Constant optimizedBooleanConstant;

	public UnaryExpression(Expression expression, int operator) {
		this.expression = expression;
		this.bits |= operator << OperatorSHIFT; // encode operator
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
			
		if (((bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
			return this.expression
				.analyseCode(currentScope, flowContext, flowInfo)
				.asNegatedCondition();
		} else {
			return this.expression.analyseCode(currentScope, flowContext, flowInfo);
		}
	}

	public Constant optimizedBooleanConstant() {
		
		return this.optimizedBooleanConstant == null 
				? this.constant 
				: this.optimizedBooleanConstant;
	}

	/**
	 * Code generation for an unary operation
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
		Label falseLabel, endifLabel;
		if (this.constant != Constant.NotAConstant) {
			// inlined value
			if (valueRequired) {
				codeStream.generateConstant(this.constant, this.implicitConversion);
			}
			codeStream.recordPositionsFrom(pc, this.sourceStart);
			return;
		}
		switch ((bits & OperatorMASK) >> OperatorSHIFT) {
			case NOT :
				switch (this.expression.implicitConversion >> 4) /* runtime type */ {
					case T_boolean :
						// ! <boolean>
						// Generate code for the condition
						this.expression.generateOptimizedBoolean(
							currentScope,
							codeStream,
							null,
							(falseLabel = new Label(codeStream)),
							valueRequired);
						if (valueRequired) {
							codeStream.iconst_0();
							if (falseLabel.hasForwardReferences()) {
								codeStream.goto_(endifLabel = new Label(codeStream));
								codeStream.decrStackSize(1);
								falseLabel.place();
								codeStream.iconst_1();
								endifLabel.place();
							}
						} else { // 6596: if (!(a && b)){} - must still place falseLabel
							falseLabel.place();
						}						
						break;
				}
				break;
			case TWIDDLE :
				switch (this.expression.implicitConversion >> 4 /* runtime */
					) {
					case T_int :
						// ~int
						this.expression.generateCode(currentScope, codeStream, valueRequired);
						if (valueRequired) {
							codeStream.iconst_m1();
							codeStream.ixor();
						}
						break;
					case T_long :
						this.expression.generateCode(currentScope, codeStream, valueRequired);
						if (valueRequired) {
							codeStream.ldc2_w(-1L);
							codeStream.lxor();
						}
				}
				break;
			case MINUS :
				// - <num>
				if (this.constant != NotAConstant) {
					if (valueRequired) {
						switch (this.expression.implicitConversion >> 4){ /* runtime */
							case T_int :
								codeStream.generateInlinedValue(this.constant.intValue() * -1);
								break;
							case T_float :
								codeStream.generateInlinedValue(this.constant.floatValue() * -1.0f);
								break;
							case T_long :
								codeStream.generateInlinedValue(this.constant.longValue() * -1L);
								break;
							case T_double :
								codeStream.generateInlinedValue(this.constant.doubleValue() * -1.0);
						}
					}
				} else {
					this.expression.generateCode(currentScope, codeStream, valueRequired);
					if (valueRequired) {
						switch (expression.implicitConversion >> 4){ /* runtime type */
							case T_int :
								codeStream.ineg();
								break;
							case T_float :
								codeStream.fneg();
								break;
							case T_long :
								codeStream.lneg();
								break;
							case T_double :
								codeStream.dneg();
						}
					}
				}
				break;
			case PLUS :
				this.expression.generateCode(currentScope, codeStream, valueRequired);
		}
		if (valueRequired) {
			codeStream.generateImplicitConversion(this.implicitConversion);
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	/**
	 * Boolean operator code generation
	 *	Optimized operations are: &&, ||, <, <=, >, >=, &, |, ^
	 */
	public void generateOptimizedBoolean(
		BlockScope currentScope,
		CodeStream codeStream,
		Label trueLabel,
		Label falseLabel,
		boolean valueRequired) {

		if ((this.constant != Constant.NotAConstant) && (this.constant.typeID() == T_boolean)) {
			super.generateOptimizedBoolean(
				currentScope,
				codeStream,
				trueLabel,
				falseLabel,
				valueRequired);
			return;
		}
		if (((this.bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
			this.expression.generateOptimizedBoolean(
				currentScope,
				codeStream,
				falseLabel,
				trueLabel,
				valueRequired);
		} else {
			super.generateOptimizedBoolean(
				currentScope,
				codeStream,
				trueLabel,
				falseLabel,
				valueRequired);
		}
	}

	public StringBuffer printExpressionNoParenthesis(int indent, StringBuffer output) {
		
		output.append(operatorToString()).append(' ');
		return this.expression.printExpression(0, output);
	} 
	
	public TypeBinding resolveType(BlockScope scope) {
		
		TypeBinding expressionType = this.expression.resolveType(scope);
		if (expressionType == null) {
			this.constant = NotAConstant;
			return null;
		}
		int expressionId = expressionType.id;
		if (expressionId > 15) {
			this.constant = NotAConstant;
			scope.problemReporter().invalidOperator(this, expressionType);
			return null;
		}

		int tableId;
		switch ((bits & OperatorMASK) >> OperatorSHIFT) {
			case NOT :
				tableId = AND_AND;
				break;
			case TWIDDLE :
				tableId = LEFT_SHIFT;
				break;
			default :
				tableId = MINUS;
		} //+ and - cases

		// the code is an int
		// (cast)  left   Op (cast)  rigth --> result
		//  0000   0000       0000   0000      0000
		//  <<16   <<12       <<8    <<4       <<0
		int result = ResolveTypeTables[tableId][(expressionId << 4) + expressionId];
		this.expression.implicitConversion = result >>> 12;
		this.bits |= result & 0xF;
		switch (result & 0xF) { // only switch on possible result type.....
			case T_boolean :
				this.resolvedType = BooleanBinding;
				break;
			case T_byte :
				this.resolvedType = ByteBinding;
				break;
			case T_char :
				this.resolvedType = CharBinding;
				break;
			case T_double :
				this.resolvedType = DoubleBinding;
				break;
			case T_float :
				this.resolvedType = FloatBinding;
				break;
			case T_int :
				this.resolvedType = IntBinding;
				break;
			case T_long :
				this.resolvedType = LongBinding;
				break;
			default : //error........
				this.constant = Constant.NotAConstant;
				if (expressionId != T_undefined)
					scope.problemReporter().invalidOperator(this, expressionType);
				return null;
		}
		// compute the constant when valid
		if (this.expression.constant != Constant.NotAConstant) {
			this.constant =
				Constant.computeConstantOperation(
					this.expression.constant,
					expressionId,
					(bits & OperatorMASK) >> OperatorSHIFT);
		} else {
			this.constant = Constant.NotAConstant;
			if (((bits & OperatorMASK) >> OperatorSHIFT) == NOT) {
				Constant cst = expression.optimizedBooleanConstant();
				if (cst != Constant.NotAConstant) 
					this.optimizedBooleanConstant = Constant.fromValue(!cst.booleanValue());
			}
		}
		return this.resolvedType;
	}

	public void traverse(
		IAbstractSyntaxTreeVisitor visitor,
		BlockScope blockScope) {
			
		if (visitor.visit(this, blockScope)) {
			this.expression.traverse(visitor, blockScope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7895.java