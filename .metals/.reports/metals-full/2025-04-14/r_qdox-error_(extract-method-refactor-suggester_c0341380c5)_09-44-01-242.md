error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1930.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1930.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1930.java
text:
```scala
s@@ = s + "return "/*nonNLS*/;

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

public class ReturnStatement extends Statement {
	public Expression expression;

	public TypeBinding expressionType;
	public boolean isSynchronized;
	public AstNode[] subroutines;
	public LocalVariableBinding saveValueVariable;

public ReturnStatement(Expression expr, int s, int e ) {
	sourceStart = s;
	sourceEnd = e;
	expression = expr ;
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {

	// here requires to generate a sequence of finally blocks invocations depending corresponding
	// to each of the traversed try statements, so that execution will terminate properly.

	// lookup the label, this should answer the returnContext

	if (expression != null) {
		flowInfo = expression.analyseCode(currentScope, flowContext, flowInfo);
	}
	// compute the return sequence (running the finally blocks)
	FlowContext targetContext;
	FlowContext traversedContext = flowContext;
	int subIndex = 0, maxSub = 5;
	boolean saveValueNeeded = false;
	while (true) {
		AstNode sub;
		if ((sub = traversedContext.subRoutine()) != null) {
			if (subroutines == null){
				subroutines = new AstNode[maxSub];
			}
			if (subIndex == maxSub) {
				System.arraycopy(subroutines, 0, (subroutines = new AstNode[maxSub *= 2]), 0, subIndex); // grow
			}
			subroutines[subIndex++] = sub;
			if (sub.cannotReturn()) {
				saveValueNeeded = false;
				break;
			}
		}
		AstNode node;
		if ((node = traversedContext.associatedNode) instanceof SynchronizedStatement) {
			isSynchronized = true;
		} else {
			if ((expression != null) && (node instanceof TryStatement)) {
				saveValueNeeded = true;
			} else {
				if (traversedContext instanceof InitializationFlowContext) {
					currentScope.problemReporter().cannotReturnInInitializer(this);
					return FlowInfo.DeadEnd;
				}
			}
		}
		FlowContext parentContext;
		if ((parentContext = traversedContext.parent) == null) { // top-context
			break;
		} else {
			traversedContext = parentContext;
		}
	}
	// resize subroutines
	if ((subroutines != null) && (subIndex != maxSub)) {
		System.arraycopy(subroutines, 0, (subroutines = new AstNode[subIndex]), 0, subIndex);
	}
	// the top returning context may want to remember the initialization at this
	// point for dealing with blank final fields.
	traversedContext.recordReturnFrom(flowInfo.unconditionalInits());

	// no need to save a constant value
	if((expression != null) && (expression.constant != NotAConstant)){
		saveValueNeeded = false;
	}
	// secret local variable for return value (note that this can only occur in a real method)
	if (saveValueNeeded) {
		prepareSaveValueLocation(currentScope);
	} else {
		MethodScope methodScope;
		if ((!isSynchronized) && (expressionType == BooleanBinding)) {
			expression.bits |= ValueForReturnMASK;
		}
	}
	return FlowInfo.DeadEnd;
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
	if ((bits & IsReachableMASK) == 0) {
		return;
	}
	int pc = codeStream.position;
	// generate the expression
	if ((expression != null) && (expression.constant == NotAConstant)) {
		expression.generateCode(currentScope, codeStream, needValue()); // no value needed if non-returning subroutine
		generateStoreSaveValueIfNecessary(currentScope, codeStream);
	}
	
	// generation of code responsible for invoking the finally blocks in sequence
	if (subroutines != null) {
		for (int i = 0, max = subroutines.length; i < max; i++) {
			AstNode sub;
			if ((sub = subroutines[i]) instanceof SynchronizedStatement) {
				codeStream.load(((SynchronizedStatement) sub).synchroVariable);
				codeStream.monitorexit();
			} else {
				TryStatement trySub = (TryStatement) sub;
				if (trySub.subRoutineCannotReturn) {
					codeStream.goto_(trySub.subRoutineStartLabel);
					codeStream.recordPositionsFrom(pc, this);
					return;
				} else {
					codeStream.jsr(trySub.subRoutineStartLabel);
				}
			}
		}
	}
	if (saveValueVariable != null) codeStream.load(saveValueVariable);
	
	if ((expression != null) && (expression.constant != NotAConstant)) {
		codeStream.generateConstant(expression.constant, expression.implicitConversion);
		generateStoreSaveValueIfNecessary(currentScope, codeStream);		
	}
	// output the suitable return bytecode or wrap the value inside a descriptor for doits
	this.generateReturnBytecode(currentScope, codeStream);
	
	codeStream.recordPositionsFrom(pc, this);
}
/**
 * Dump the suitable return bytecode for a return statement
 *
 */
public void generateReturnBytecode(BlockScope currentScope, CodeStream codeStream) {

	if (expression == null) {
		codeStream.return_();
	} else {
		switch (expression.implicitConversion >> 4) {
			case T_boolean :
			case T_int :
				codeStream.ireturn();
				break;
			case T_float :
				codeStream.freturn();
				break;
			case T_long :
				codeStream.lreturn();
				break;
			case T_double :
				codeStream.dreturn();
				break;
			default :
				codeStream.areturn();
		}
	}
}
public void generateStoreSaveValueIfNecessary(BlockScope currentScope, CodeStream codeStream){

	if (saveValueVariable != null) codeStream.store(saveValueVariable, false);
}
public boolean needValue(){
	return (subroutines == null) || (saveValueVariable != null) || isSynchronized;
}
public void prepareSaveValueLocation(BlockScope currentScope){
		
	saveValueVariable = ((AbstractMethodDeclaration) currentScope.methodScope().referenceContext).secretReturnValue;
	saveValueVariable.used = true;
}
public void resolve(BlockScope scope) {
	MethodScope methodScope = scope.methodScope();
	TypeBinding methodType =
		(methodScope.referenceContext instanceof AbstractMethodDeclaration)
			? ((AbstractMethodDeclaration) methodScope.referenceContext).binding.returnType
			: VoidBinding;
	if (methodType == VoidBinding) {
		// the expression should be null
		if (expression == null)
			return;
		if ((expressionType = expression.resolveType(scope)) != null)
			scope.problemReporter().attemptToReturnNonVoidExpression(this, expressionType);
		return;
	}
	if (expression == null) {
		scope.problemReporter().shouldReturn(methodType, this);
		return;
	}
	if ((expressionType = expression.resolveType(scope)) == null)
		return;

	if (expression.isConstantValueOfTypeAssignableToType(expressionType, methodType)) {
		// dealing with constant
		expression.implicitWidening(methodType, expressionType);
		return;
	}
	if (expressionType == VoidBinding) {
		scope.problemReporter().attemptToReturnVoidValue(this);
		return;
	}
	if (scope.areTypesCompatible(expressionType, methodType)) {
		expression.implicitWidening(methodType, expressionType);
		return;
	}
	scope.problemReporter().typeMismatchErrorActualTypeExpectedType(expression, expressionType, methodType);
}
public String toString(int tab){
	/* slow code */

	String s = tabString(tab) ;
	s = s + "return "; //$NON-NLS-1$
	if (expression != null )
		s = s + expression.toStringExpression() ;
	return s;
}
public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		if (expression != null)
			expression.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1930.java