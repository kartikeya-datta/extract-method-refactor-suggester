error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3639.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3639.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3639.java
text:
```scala
i@@f (castTb != expressionTb && (Scope.compareTypes(castTb, expressionTb) == NotRelated)) {

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
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.*;

public class InstanceOfExpression extends OperatorExpression {
	public Expression expression;
	public TypeReference type;
public InstanceOfExpression(Expression expression, TypeReference type, int operator) {
	this.expression = expression;
	this.type = type;
	this.bits |= operator << OperatorSHIFT;
	this.sourceStart = expression.sourceStart;
	this.sourceEnd = type.sourceEnd;
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	return expression.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
}
public final boolean areTypesCastCompatible(BlockScope scope, TypeBinding castTb, TypeBinding expressionTb) {
	//	see specifications p.68
	//A more cpmplete version of this method is provided on
	//CastExpression (it deals with constant and need runtime checkcast)

	//by grammatical construction, the first test is ALWAYS false
	//if (castTb.isBaseType())
	//{	if (expressionTb.isBaseType())
	//	{	if (expression.isConstantValueOfTypeAssignableToType(expressionTb,castTb))
	//		{	return true;}
	//		else
	//		{	if (expressionTb==castTb)
	//			{	return true;}
	//			else 
	//			{	if (scope.areTypesCompatible(expressionTb,castTb))
	//				{	return true; }
	//				
	//				if (BaseTypeBinding.isNarrowing(castTb.id,expressionTb.id))
	//				{	return true;}
	//				return false;}}}
	//	else
	//	{	return false; }}
	//else
	{ //-------------checkcast to something which is NOT a basetype----------------------------------	

		if (NullBinding == expressionTb)
			//null is compatible with every thing .... 
			{
			return true;
		}
		if (expressionTb.isArrayType()) {
			if (castTb.isArrayType()) { //------- (castTb.isArray) expressionTb.isArray -----------
				TypeBinding expressionEltTb = ((ArrayBinding) expressionTb).elementsType(scope);
				if (expressionEltTb.isBaseType())
					// <---stop the recursion------- 
					return ((ArrayBinding) castTb).elementsType(scope) == expressionEltTb;
				//recursivly on the elts...
				return areTypesCastCompatible(scope, ((ArrayBinding) castTb).elementsType(scope), expressionEltTb);
			}
			if (castTb.isClass()) { //------(castTb.isClass) expressionTb.isArray ---------------	
				if (scope.isJavaLangObject(castTb))
					return true;
				return false;
			}
			if (castTb.isInterface()) { //------- (castTb.isInterface) expressionTb.isArray -----------
				if (scope.isJavaLangCloneable(castTb) || scope.isJavaIoSerializable(castTb)) {
					return true;
				}
				return false;
			}

			//=========hoops=============
			return false;
		}
		if (expressionTb.isBaseType()) {
			return false;
		}
		if (expressionTb.isClass()) {
			if (castTb.isArrayType()) { // ---- (castTb.isArray) expressionTb.isClass -------
				if (scope.isJavaLangObject(expressionTb)) {
					return true;
				} else {
					return false;
				}
			}
			if (castTb.isClass()) { // ----- (castTb.isClass) expressionTb.isClass ------ 
				if (scope.areTypesCompatible(expressionTb, castTb))
					return true;
				else {
					if (scope.areTypesCompatible(castTb, expressionTb)) {
						return true;
					}
					return false;
				}
			}
			if (castTb.isInterface()) { // ----- (castTb.isInterface) expressionTb.isClass -------  
				if (((ReferenceBinding) expressionTb).isFinal()) { //no subclass for expressionTb, thus compile-time check is valid
					if (scope.areTypesCompatible(expressionTb, castTb))
						return true;
					return false;
				} else {
					return true;
				}
			}

			//=========hoops==============
			return false;
		}
		if (expressionTb.isInterface()) {
			if (castTb.isArrayType()) { // ----- (castTb.isArray) expressionTb.isInterface ------
				if (scope.isJavaLangCloneable(expressionTb) || scope.isJavaIoSerializable(expressionTb))
					//potential runtime error
					{
					return true;
				}
				return false;
			}
			if (castTb.isClass()) { // ----- (castTb.isClass) expressionTb.isInterface --------
				if (scope.isJavaLangObject(castTb))
					return true;
				if (((ReferenceBinding) castTb).isFinal()) { //no subclass for castTb, thus compile-time check is valid
					if (scope.areTypesCompatible(castTb, expressionTb)) {
						return true;
					}
					return false;
				}
				return true;
			}
			if (castTb.isInterface()) { // ----- (castTb.isInterface) expressionTb.isInterface -------
				if (castTb != expressionTb && (scope.compareTypes(castTb, expressionTb) == NotRelated)) {
					MethodBinding[] castTbMethods = ((ReferenceBinding) castTb).methods();
					int castTbMethodsLength = castTbMethods.length;
					MethodBinding[] expressionTbMethods = ((ReferenceBinding) expressionTb).methods();
					int expressionTbMethodsLength = expressionTbMethods.length;
					for (int i = 0; i < castTbMethodsLength; i++) {
						for (int j = 0; j < expressionTbMethodsLength; j++) {
							if (castTbMethods[i].selector == expressionTbMethods[j].selector) {
								if (castTbMethods[i].returnType != expressionTbMethods[j].returnType) {
									if (castTbMethods[i].areParametersEqual(expressionTbMethods[j])) {
										return false;
									}
								}
							}
						}
					}
				}
				return true;
			}

			//============hoops===========	
			return false;
		} // true;}

		//=======hoops==========
		return false;
	}
}
/**
 * Code generation for instanceOfExpression
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
*/ 
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	int pc = codeStream.position;
	expression.generateCode(currentScope, codeStream, true);
	codeStream.instance_of(type.binding);
	if (!valueRequired)
		codeStream.pop();
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}
public TypeBinding resolveType(BlockScope scope) {
	constant = NotAConstant;
	TypeBinding expressionTb = expression.resolveType(scope);
	TypeBinding checkTb = type.resolveType(scope);
	if (expressionTb == null || checkTb == null)
		return null;

	//===== by grammatical construction, the next test is always false =====
	//if (checkTb.isBaseType()) {
	//	scope.problemReporter().invalidTypeError(type,checkTb);
	//	return null;
	//}

	if (!areTypesCastCompatible(scope, checkTb, expressionTb)) {
		scope.problemReporter().notCompatibleTypesError(this, expressionTb, checkTb);
		return null;
	}
	return BooleanBinding;
}
public String toStringExpressionNoParenthesis(){
	/* slow code*/

	return	expression.toStringExpression() + " instanceof " + //$NON-NLS-1$
			type.toString(0) ; }
public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		expression.traverse(visitor, scope);
		type.traverse(visitor, scope);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3639.java