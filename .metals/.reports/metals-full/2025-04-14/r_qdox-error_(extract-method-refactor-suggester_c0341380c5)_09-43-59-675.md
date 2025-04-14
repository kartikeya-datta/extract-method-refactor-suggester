error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6286.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6286.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6286.java
text:
```scala
c@@odeStream.recordPositionsFrom(pc, this.sourceStart);

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

public class ArrayAllocationExpression extends Expression {
	public TypeReference type;

	//dimensions.length gives the number of dimensions, but the
	// last ones may be nulled as in new int[4][5][][]
	public Expression[] dimensions;
	public ArrayInitializer initializer;

	public ArrayBinding arrayTb;
/**
 * ArrayAllocationExpression constructor comment.
 */
public ArrayAllocationExpression() {
	super();
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	for (int i = 0, max = dimensions.length; i < max; i++) {
		Expression dim;
		if ((dim = dimensions[i]) != null) {
			flowInfo = dim.analyseCode(currentScope, flowContext, flowInfo);
		}
	}
	if (initializer != null) {
		return initializer.analyseCode(currentScope, flowContext, flowInfo);
	} else {
		return flowInfo;
	}
}
/**
 * Code generation for a array allocation expression
 */
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

	int pc = codeStream.position;
	ArrayBinding arrayBinding;
	
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
	if ((arrayTb.isArrayType()) && ((arrayBinding = (ArrayBinding) arrayTb).dimensions == 1)) {
		// Mono-dimensional array
		codeStream.newArray(currentScope, arrayBinding);
	} else {
		// Multi-dimensional array
		codeStream.multianewarray(arrayTb, nonNullDimensionsLength);
	}
 
	if (valueRequired) {
		codeStream.generateImplicitConversion(implicitConversion);
	} else {
		codeStream.pop();
	}

	codeStream.recordPositionsFrom(pc, this);
}
public TypeBinding resolveType(BlockScope scope) {
	// Build an array type reference using the current dimensions
	// The parser does not check for the fact that dimension may be null
	// only at the -end- like new int [4][][]. The parser allows new int[][4][]
	// so this must be checked here......(this comes from a reduction to LL1 grammar)

	TypeBinding referenceTb = type.resolveType(scope); // will check for null after dimensions are checked
	constant = Constant.NotAConstant;
	if (referenceTb == VoidBinding) {
		scope.problemReporter().cannotAllocateVoidArray(this);
		referenceTb = null; // will return below
	}

	// check the validity of the dimension syntax (and test for all null dimensions)
	int lengthDim = -1;
	for (int i = dimensions.length; --i >= 0;) {
		if (dimensions[i] != null) {
			if (lengthDim == -1)
				lengthDim = i;
		} else if (lengthDim != -1) { // should not have an empty dimension before an non-empty one
			scope.problemReporter().incorrectLocationForEmptyDimension(this, i);
			return null;
		}
	}
	if (referenceTb == null)
		return null;

	// lengthDim == -1 says if all dimensions are nulled
	// when an initializer is given, no dimension must be specified
	if (initializer == null) {
		if (lengthDim == -1) {
			scope.problemReporter().mustDefineDimensionsOrInitializer(this);
			return null;
		}
	} else if (lengthDim != -1) {
		scope.problemReporter().cannotDefineDimensionsAndInitializer(this);
		return null;
	}

	// dimensions resolution 
	for (int i = 0; i <= lengthDim; i++) {
		TypeBinding dimTb = dimensions[i].resolveTypeExpecting(scope, IntBinding);
		if (dimTb == null)
			return null;
		dimensions[i].implicitWidening(IntBinding, dimTb);
	}

	// building the array binding
	arrayTb = scope.createArray(referenceTb, dimensions.length);

	// check the initializer
	if (initializer != null)
		if ((initializer.resolveTypeExpecting(scope, arrayTb)) != null)
			initializer.binding = arrayTb;
	return arrayTb;
}
public String toStringExpression() {
	/* slow code */

	String s = "new " + type.toString(0); //$NON-NLS-1$
	for (int i = 0 ; i < dimensions.length ; i++)
	{	if (dimensions[i] == null)
			s = s + "[]"; //$NON-NLS-1$
		else
			s = s + "[" + dimensions[i].toStringExpression() + "]" ;} //$NON-NLS-2$ //$NON-NLS-1$
	if (initializer != null)
		s = s + initializer.toStringExpression();
	return s;}
public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		int dimensionsLength = dimensions.length;
		type.traverse(visitor, scope);
		for (int i = 0; i < dimensionsLength; i++) {
			if (dimensions[i] != null) dimensions[i].traverse(visitor, scope);
		}
		if (initializer != null) initializer.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6286.java