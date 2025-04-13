error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/987.java
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

public class Assignment extends Expression {
	public Reference lhs ;
	public Expression expression ;
public Assignment(Expression lhs, Expression expression, int sourceEnd) {
	//lhs is always a reference by construction ,
	//but is build as an expression ==> the checkcast cannot fail

	this.lhs = (Reference) lhs;
	this.expression = expression ;

	this.sourceStart = lhs.sourceStart;
	this.sourceEnd = sourceEnd;
}
public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	// record setting a variable: various scenarii are possible, setting an array reference, 
	// a field reference, a blank final field reference, a field of an enclosing instance or 
	// just a local variable.

	return lhs.analyseAssignment(currentScope, flowContext, flowInfo, this, false).unconditionalInits();
}
public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {

	// various scenarii are possible, setting an array reference, 
	// a field reference, a blank final field reference, a field of an enclosing instance or 
	// just a local variable.

	int pc = codeStream.position;
	lhs.generateAssignment(currentScope, codeStream, this, valueRequired); // variable may have been optimized out
	// the lhs is responsible to perform the implicitConversion generation for the assignment since optimized for unused local assignment.
	codeStream.recordPositionsFrom(pc, this);
}
public TypeBinding resolveType(BlockScope scope) {
	// due to syntax lhs may be only a NameReference, a FieldReference or an ArrayReference

	constant = NotAConstant;
	TypeBinding lhsTb = lhs.resolveType(scope);
	TypeBinding expressionTb = expression.resolveType(scope);
	if (lhsTb == null || expressionTb == null)
		return null;

	// Compile-time conversion of base-types : implicit narrowing integer into byte/short/character
	// may require to widen the rhs expression at runtime
	if ((expression.isConstantValueOfTypeAssignableToType(expressionTb, lhsTb) ||
		(lhsTb.isBaseType() && BaseTypeBinding.isWidening(lhsTb.id, expressionTb.id))) ||
		(scope.areTypesCompatible(expressionTb, lhsTb))) {
			expression.implicitWidening(lhsTb, expressionTb);
			return lhsTb;
		}
	scope.problemReporter().typeMismatchErrorActualTypeExpectedType(expression, expressionTb, lhsTb);
	return null;


	/*------------code deported to the flow analysis-------------------
	if (lhs.isFieldReference()) {
		// cover also the case of a nameReference that refers to a field...(of course !...)
		if (lhsTb.isFinal()) {
			// if the field is final, then the assignment may be done only in constructors/initializers
			// (this does not insure that the assignment is valid....the flow analysis will tell so)
			if (scope.enclosingType() == lhs.fieldBinding().declaringClass) {
				scope.problemReporter().cannotAssignToFinalField(this, lhs.fieldBinding());
				return null;
			}
			if (!scope.enclosingMethod().isConstructorOrInintilizer()) {
				scope.problemReporter().cannotAssignToFinalField(this, lhs.fieldBinding());
				return null;
			}
		}
	}
	-- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- * /

	/*----------------------------------------------------------------
	the code that test if the local is an outer local (it is by definition final)
	and thus cannot be assigned , has been moved to flow analysis		
	------------------------------------------------------------------*/

	/*-----------------------------------------------------------------
	The code that detect the a.b = a.b + 1 ; is done by the flow analysis too
	-------------------------------------------------------------------*/
}
public String toString(int tab){
	/* slow code*/

	//no () when used as a statement 
	
	return	tabString(tab) + toStringExpressionNoParenthesis() ; }
public String toStringExpression(){
	/* slow code*/

	//subclass redefine toStringExpressionNoParenthesis()
	
	return	"(" + toStringExpressionNoParenthesis() + ")"; } //$NON-NLS-2$ //$NON-NLS-1$
public String toStringExpressionNoParenthesis() {

	return 	lhs.toStringExpression() + " " + //$NON-NLS-1$
			"=" +  //$NON-NLS-1$
			
			( (expression.constant != null ) && (expression.constant != NotAConstant) ?
			 	" /*cst:"+expression.constant.toString()+"*/ " : //$NON-NLS-1$ //$NON-NLS-2$
			 	" " ) + //$NON-NLS-1$
				
			expression.toStringExpression() ; }
public void traverse(IAbstractSyntaxTreeVisitor visitor, BlockScope scope) {
	if (visitor.visit(this, scope)) {
		lhs.traverse(visitor, scope);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/987.java