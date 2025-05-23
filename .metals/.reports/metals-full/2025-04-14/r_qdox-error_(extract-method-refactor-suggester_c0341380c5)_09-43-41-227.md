error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2213.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2213.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2213.java
text:
```scala
c@@odeStream.newArray((ArrayBinding)this.resolvedType);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

public class ArrayAllocationExpression extends Expression {

	public TypeReference type;

	//dimensions.length gives the number of dimensions, but the
	// last ones may be nulled as in new int[4][5][][]
	public Expression[] dimensions;
	public ArrayInitializer initializer;

	/**
	 * ArrayAllocationExpression constructor comment.
	 */
	public ArrayAllocationExpression() {
		super();
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
		for (int i = 0, max = dimensions.length; i < max; i++) {
			Expression dim;
			if ((dim = dimensions[i]) != null) {
				flowInfo = dim.analyseCode(currentScope, flowContext, flowInfo);
			}
		}
		if (initializer != null) {
			return initializer.analyseCode(currentScope, flowContext, flowInfo);
		}
		return flowInfo;
	}

	/**
	 * Code generation for a array allocation expression
	 */
	public void generateCode(
		BlockScope currentScope,
		CodeStream codeStream,
		boolean valueRequired) {

		int pc = codeStream.position;

		if (initializer != null) {
			initializer.generateCode(currentScope, codeStream, valueRequired);
			return;
		}

		int nonNullDimensionsLength = 0;
		for (int i = 0, max = dimensions.length; i < max; i++)
			if (dimensions[i] != null) {
				dimensions[i].generateCode(currentScope, codeStream, true);
				nonNullDimensionsLength++;
			}

		// Generate a sequence of bytecodes corresponding to an array allocation
		if (this.resolvedType.dimensions() == 1) {
			// Mono-dimensional array
			codeStream.newArray(currentScope, (ArrayBinding)this.resolvedType);
		} else {
			// Multi-dimensional array
			codeStream.multianewarray(this.resolvedType, nonNullDimensionsLength);
		}

		if (valueRequired) {
			codeStream.generateImplicitConversion(implicitConversion);
		} else {
			codeStream.pop();
		}

		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}


	public StringBuffer printExpression(int indent, StringBuffer output) {

		output.append("new "); //$NON-NLS-1$
		type.print(0, output); 
		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i] == null)
				output.append("[]"); //$NON-NLS-1$
			else {
				output.append('[');
				dimensions[i].printExpression(0, output);
				output.append(']');
			}
		} 
		if (initializer != null) initializer.printExpression(0, output);
		return output;
	}
	
	public TypeBinding resolveType(BlockScope scope) {

		// Build an array type reference using the current dimensions
		// The parser does not check for the fact that dimension may be null
		// only at the -end- like new int [4][][]. The parser allows new int[][4][]
		// so this must be checked here......(this comes from a reduction to LL1 grammar)

		TypeBinding referenceType = type.resolveType(scope);
		
		// will check for null after dimensions are checked
		constant = Constant.NotAConstant;
		if (referenceType == VoidBinding) {
			scope.problemReporter().cannotAllocateVoidArray(this);
			referenceType = null;
		}

		// check the validity of the dimension syntax (and test for all null dimensions)
		int explicitDimIndex = -1;
		for (int i = dimensions.length; --i >= 0;) {
			if (dimensions[i] != null) {
				if (explicitDimIndex < 0) explicitDimIndex = i;
			} else if (explicitDimIndex> 0) {
				// should not have an empty dimension before an non-empty one
				scope.problemReporter().incorrectLocationForEmptyDimension(this, i);
			}
		}

		// explicitDimIndex < 0 says if all dimensions are nulled
		// when an initializer is given, no dimension must be specified
		if (initializer == null) {
			if (explicitDimIndex < 0) {
				scope.problemReporter().mustDefineDimensionsOrInitializer(this);
			}
		} else if (explicitDimIndex >= 0) {
			scope.problemReporter().cannotDefineDimensionsAndInitializer(this);
		}

		// dimensions resolution 
		for (int i = 0; i <= explicitDimIndex; i++) {
			if (dimensions[i] != null) {
				TypeBinding dimensionType = dimensions[i].resolveTypeExpecting(scope, IntBinding);
				if (dimensionType != null) {
					dimensions[i].computeConversion(scope, IntBinding, dimensionType);
				}
			}
		}

		// building the array binding
		if (referenceType != null) {
			if (dimensions.length > 255) {
				scope.problemReporter().tooManyDimensions(this);
			}
			// allow new List<?>[5]
			if (referenceType.isBoundParameterizedType() || referenceType.isGenericType() || referenceType.isTypeVariable()) {
			    scope.problemReporter().illegalGenericArray(referenceType, this);
			}
			this.resolvedType = scope.createArrayType(referenceType, dimensions.length);

			// check the initializer
			if (initializer != null) {
				if ((initializer.resolveTypeExpecting(scope, this.resolvedType)) != null)
					initializer.binding = (ArrayBinding)this.resolvedType;
			}
		}
		return this.resolvedType;
	}


	public void traverse(ASTVisitor visitor, BlockScope scope) {

		if (visitor.visit(this, scope)) {
			int dimensionsLength = dimensions.length;
			type.traverse(visitor, scope);
			for (int i = 0; i < dimensionsLength; i++) {
				if (dimensions[i] != null)
					dimensions[i].traverse(visitor, scope);
			}
			if (initializer != null)
				initializer.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2213.java