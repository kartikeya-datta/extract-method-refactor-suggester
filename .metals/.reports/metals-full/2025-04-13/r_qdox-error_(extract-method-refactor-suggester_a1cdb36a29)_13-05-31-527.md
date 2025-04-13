error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7197.java
text:
```scala
t@@his.resolvedType = this.binding = (ArrayBinding) expectedTb;

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
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class ArrayInitializer extends Expression {
		
	public Expression[] expressions;
	public ArrayBinding binding; //the type of the { , , , }
	
	/**
	 * ArrayInitializer constructor comment.
	 */
	public ArrayInitializer() {

		super();
	}

	public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

		if (expressions != null) {
			for (int i = 0, max = expressions.length; i < max; i++) {
				flowInfo = expressions[i].analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
			}
		}
		return flowInfo;
	}

	/**
	 * Code generation for a array initializer
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

		// Flatten the values and compute the dimensions, by iterating in depth into nested array initializers
		int pc = codeStream.position;
		int expressionLength = (expressions == null) ? 0: expressions.length;
		codeStream.generateInlinedValue(expressionLength);
		codeStream.newArray(binding);
		if (expressions != null) {
			// binding is an ArrayType, so I can just deal with the dimension
			int elementsTypeID = binding.dimensions > 1 ? -1 : binding.leafComponentType.id;
			for (int i = 0; i < expressionLength; i++) {
				Expression expr;
				if ((expr = expressions[i]).constant != NotAConstant) {
					switch (elementsTypeID) { // filter out initializations to default values
						case T_int :
						case T_short :
						case T_byte :
						case T_char :
						case T_long :
							if (expr.constant.longValue() != 0) {
								codeStream.dup();
								codeStream.generateInlinedValue(i);
								expr.generateCode(currentScope, codeStream, true);
								codeStream.arrayAtPut(elementsTypeID, false);
							}
							break;
						case T_float :
						case T_double :
							double constantValue = expr.constant.doubleValue();
							if (constantValue == -0.0 || constantValue != 0) {
								codeStream.dup();
								codeStream.generateInlinedValue(i);
								expr.generateCode(currentScope, codeStream, true);
								codeStream.arrayAtPut(elementsTypeID, false);
							}
							break;
						case T_boolean :
							if (expr.constant.booleanValue() != false) {
								codeStream.dup();
								codeStream.generateInlinedValue(i);
								expr.generateCode(currentScope, codeStream, true);
								codeStream.arrayAtPut(elementsTypeID, false);
							}
							break;
						default :
							if (!(expr instanceof NullLiteral)) {
								codeStream.dup();
								codeStream.generateInlinedValue(i);
								expr.generateCode(currentScope, codeStream, true);
								codeStream.arrayAtPut(elementsTypeID, false);
							}
					}
				} else if (!(expr instanceof NullLiteral)) {
					codeStream.dup();
					codeStream.generateInlinedValue(i);
					expr.generateCode(currentScope, codeStream, true);
					codeStream.arrayAtPut(elementsTypeID, false);
				}
			}
		}
		if (valueRequired) {
			codeStream.generateImplicitConversion(this.implicitConversion);
		} else {
			codeStream.pop();
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	public StringBuffer printExpression(int indent, StringBuffer output) {
	
		output.append('{');
		if (expressions != null) { 	
			int j = 20 ; 
			for (int i = 0 ; i < expressions.length ; i++) {	
				if (i > 0) output.append(", "); //$NON-NLS-1$
				expressions[i].printExpression(0, output);
				j -- ;
				if (j == 0) {
					output.append('\n');
					printIndent(indent+1, output);
					j = 20;
				}
			}
		}
		return output.append('}');
	}

	public TypeBinding resolveTypeExpecting(BlockScope scope, TypeBinding expectedTb) {
		// Array initializers can only occur on the right hand side of an assignment
		// expression, therefore the expected type contains the valid information
		// concerning the type that must be enforced by the elements of the array initializer.
	
		// this method is recursive... (the test on isArrayType is the stop case)
	
		this.constant = NotAConstant;
		
		// allow new List<?>[5]
		TypeBinding leafComponentType = expectedTb.leafComponentType();
		if (leafComponentType.isBoundParameterizedType() || leafComponentType.isGenericType() || leafComponentType.isTypeVariable()) {
		    scope.problemReporter().illegalGenericArray(leafComponentType, this);
		}
			
		if (expectedTb.isArrayType()) {
			binding = (ArrayBinding) expectedTb;
			if (expressions == null)
				return binding;
			TypeBinding expectedElementsTb = binding.elementsType();
			if (expectedElementsTb.isBaseType()) {
				for (int i = 0, length = expressions.length; i < length; i++) {
					Expression expression = expressions[i];
					TypeBinding expressionTb =
						(expression instanceof ArrayInitializer)
							? expression.resolveTypeExpecting(scope, expectedElementsTb)
							: expression.resolveType(scope);
					if (expressionTb == null)
						return null;
	
					// Compile-time conversion required?
					if (expectedElementsTb != expressionTb) // must call before computeConversion() and typeMismatchError()
						scope.compilationUnitScope().recordTypeConversion(expectedElementsTb, expressionTb);
					if (expression.isConstantValueOfTypeAssignableToType(expressionTb, expectedElementsTb)
 BaseTypeBinding.isWidening(expectedElementsTb.id, expressionTb.id)
 scope.isBoxingCompatibleWith(expressionTb, expectedElementsTb)) {
							expression.computeConversion(scope, expectedElementsTb, expressionTb);
					} else {
						scope.problemReporter().typeMismatchError(expressionTb, expectedElementsTb, expression);
						return null;
					}
				}
			} else {
				for (int i = 0, length = expressions.length; i < length; i++)
					if (expressions[i].resolveTypeExpecting(scope, expectedElementsTb) == null)
						return null;
			}
			return binding;
		}
		
		// infer initializer type for error reporting based on first element
		TypeBinding leafElementType = null;
		int dim = 1;
		if (expressions == null) {
			leafElementType = scope.getJavaLangObject();
		} else {
			Expression currentExpression = expressions[0];
			while(currentExpression != null && currentExpression instanceof ArrayInitializer) {
				dim++;
				Expression[] subExprs = ((ArrayInitializer) currentExpression).expressions;
				if (subExprs == null){
					leafElementType = scope.getJavaLangObject();
					currentExpression = null;
					break;
				}
				currentExpression = ((ArrayInitializer) currentExpression).expressions[0];
			}
			if (currentExpression != null) {
				leafElementType = currentExpression.resolveType(scope);
			}
		}
		if (leafElementType != null) {
			TypeBinding probableTb = scope.createArrayType(leafElementType, dim);
			scope.problemReporter().typeMismatchError(probableTb, expectedTb, this);
		}
		return null;
	}
	
	public void traverse(ASTVisitor visitor, BlockScope scope) {

		if (visitor.visit(this, scope)) {
			if (expressions != null) {
				int expressionsLength = expressions.length;
				for (int i = 0; i < expressionsLength; i++)
					expressions[i].traverse(visitor, scope);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7197.java