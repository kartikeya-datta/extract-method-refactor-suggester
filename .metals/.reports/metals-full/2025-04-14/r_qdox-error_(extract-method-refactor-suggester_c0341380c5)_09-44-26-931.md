error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8124.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8124.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8124.java
text:
```scala
(@@(CodeSnippetCodeStream)codeStream).generateObjectWrapperForType(this.expressionType);

package org.eclipse.jdt.internal.eval;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.impl.NullConstant;

/**
 * A return statement inside a code snippet. During the code gen,
 * it uses a macro to set the result of the code snippet instead
 * of returning it.
 */
public class CodeSnippetReturnStatement extends ReturnStatement implements InvocationSite, EvaluationConstants {
	MethodBinding setResultMethod;
public CodeSnippetReturnStatement(Expression expr, int s, int e, EvaluationContext evaluationContext) {
	super(expr, s, e);
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	FlowInfo info = super.analyseCode(currentScope, flowContext, flowInfo);
	// we need to remove this optimization in order to prevent the inlining of the return bytecode
	// 1GH0AU7: ITPJCORE:ALL - Eval - VerifyError in scrapbook page
	this.expression.bits &= ~ValueForReturnMASK;
	return info;
}

/**
 * Dump the suitable return bytecode for a return statement
 *
 */
public void generateReturnBytecode(BlockScope currentScope, CodeStream codeStream) {
	
	// output the return bytecode
	codeStream.return_();
}
public void generateStoreSaveValueIfNecessary(BlockScope currentScope, CodeStream codeStream){

	// push receiver
	codeStream.aload_0();

	// push the 2 parameters of "setResult(Object, Class)"
	if (this.expression == null || this.expressionType == VoidBinding) { // expressionType == VoidBinding if code snippet is the expression "System.out.println()"
		// push null
		codeStream.aconst_null();

		// void.class
		codeStream.generateClassLiteralAccessForType(VoidBinding, null);
	} else {
		// swap with expression
		int valueTypeID = this.expressionType.id;
		if (valueTypeID == T_long || valueTypeID == T_double) {
			codeStream.dup_x2();
			codeStream.pop();
		} else {
			codeStream.swap();
		}

		// generate wrapper if needed
		if (this.expressionType.isBaseType()) { 
			codeStream.generateObjectWrapperForType(this.expressionType);
		}

		// generate the expression type
		codeStream.generateClassLiteralAccessForType(this.expressionType, null);
	}

	// generate the invoke virtual to "setResult(Object,Class)"
	codeStream.invokevirtual(this.setResultMethod);
}
public boolean isSuperAccess() {
	return false;
}
public boolean isTypeAccess() {
	return false;
}
public boolean needValue(){
	return true;
}
public void prepareSaveValueLocation(BlockScope currentScope){
		
	// do nothing: no storage is necessary for snippets
}
public void resolve(BlockScope scope) {
	if (this.expression != null) {
		if ((this.expressionType = this.expression.resolveType(scope)) != null) {
			TypeBinding javaLangClass = scope.getJavaLangClass();
			if (!javaLangClass.isValidBinding()) {
				scope.problemReporter().codeSnippetMissingClass("java.lang.Class", this.sourceStart, this.sourceEnd); //$NON-NLS-1$
				return;
			}
			TypeBinding javaLangObject = scope.getJavaLangObject();
			if (!javaLangObject.isValidBinding()) {
				scope.problemReporter().codeSnippetMissingClass("java.lang.Object", this.sourceStart, this.sourceEnd); //$NON-NLS-1$
				return;
			}
			TypeBinding[] argumentTypes = new TypeBinding[] {javaLangObject, javaLangClass};
			this.setResultMethod = scope.getImplicitMethod(SETRESULT_SELECTOR, argumentTypes, this);
			if (!this.setResultMethod.isValidBinding()) {
				scope.problemReporter().codeSnippetMissingMethod(ROOT_FULL_CLASS_NAME, new String(SETRESULT_SELECTOR), new String(SETRESULT_ARGUMENTS), this.sourceStart, this.sourceEnd);
				return;
			}
			// in constant case, the implicit conversion cannot be left uninitialized
			if (this.expression.constant != NotAConstant) {
				// fake 'no implicit conversion' (the return type is always void)
				this.expression.implicitConversion = this.expression.constant.typeID() << 4;
			}
		}
	}
}
public void setDepth(int depth) {
}
public void setFieldIndex(int depth) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8124.java