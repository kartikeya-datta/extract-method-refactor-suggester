error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7880.java
text:
```scala
i@@f (leftField != null &&  rhsType != NullBinding && lhsType.isWildcard() && ((WildcardBinding)lhsType).kind != Wildcard.SUPER) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Genady Beriozkin - added support for reporting assignment with no effect
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class Assignment extends Expression {

	public Expression lhs;
	public Expression expression;
		
	public Assignment(Expression lhs, Expression expression, int sourceEnd) {
		//lhs is always a reference by construction ,
		//but is build as an expression ==> the checkcast cannot fail

		this.lhs = lhs;
		lhs.bits |= IsStrictlyAssignedMASK; // tag lhs as assigned
		
		this.expression = expression;

		this.sourceStart = lhs.sourceStart;
		this.sourceEnd = sourceEnd;
	}

	public FlowInfo analyseCode(
		BlockScope currentScope,
		FlowContext flowContext,
		FlowInfo flowInfo) {
		// record setting a variable: various scenarii are possible, setting an array reference, 
		// a field reference, a blank final field reference, a field of an enclosing instance or 
		// just a local variable.

		return ((Reference) lhs)
			.analyseAssignment(currentScope, flowContext, flowInfo, this, false)
			.unconditionalInits();
	}

	void checkAssignmentEffect(BlockScope scope) {
		
		Binding left = getDirectBinding(this.lhs);
		if (left != null && left == getDirectBinding(this.expression)) {
			scope.problemReporter().assignmentHasNoEffect(this, left.shortReadableName());
			this.bits |= IsAssignmentWithNoEffectMASK; // record assignment has no effect
		}
	}

	void checkAssignment(BlockScope scope, TypeBinding lhsType, TypeBinding rhsType) {
		
		FieldBinding leftField = getLastField(this.lhs);
		if (leftField != null && lhsType.isWildcard() && rhsType != NullBinding) {
		    scope.problemReporter().unsafeWildcardAssignment(lhsType, rhsType, this.expression);
		} else if (leftField != null && leftField.declaringClass.isRawType() 
		        && (rhsType.isParameterizedType() || rhsType.isGenericType())) {
		    scope.problemReporter().unsafeRawFieldAssignment(leftField, rhsType, this.lhs);
		} else if (rhsType.isRawType() && (lhsType.isParameterizedType() || lhsType.isGenericType())) {
		    scope.problemReporter().unsafeRawAssignment(this.expression, rhsType, lhsType);
		}		
	}
	
	public void generateCode(
		BlockScope currentScope,
		CodeStream codeStream,
		boolean valueRequired) {

		// various scenarii are possible, setting an array reference, 
		// a field reference, a blank final field reference, a field of an enclosing instance or 
		// just a local variable.

		int pc = codeStream.position;
		if ((this.bits & IsAssignmentWithNoEffectMASK) != 0) {
			if (valueRequired) {
				this.expression.generateCode(currentScope, codeStream, true);
			}
		} else {
			 ((Reference) lhs).generateAssignment(currentScope, codeStream, this, valueRequired);
			// variable may have been optimized out
			// the lhs is responsible to perform the implicitConversion generation for the assignment since optimized for unused local assignment.
		}
		codeStream.recordPositionsFrom(pc, this.sourceStart);
	}

	Binding getDirectBinding(Expression someExpression) {
		if (someExpression instanceof SingleNameReference) {
			return ((SingleNameReference)someExpression).binding;
		} else if (someExpression instanceof FieldReference) {
			FieldReference fieldRef = (FieldReference)someExpression;
			if (fieldRef.receiver.isThis() && !(fieldRef.receiver instanceof QualifiedThisReference)) {
				return fieldRef.binding;
			}			
		}
		return null;
	}
	FieldBinding getLastField(Expression someExpression) {
	    if (someExpression instanceof SingleNameReference) {
	        if ((someExpression.bits & RestrictiveFlagMASK) == BindingIds.FIELD) {
	            return (FieldBinding) ((SingleNameReference)someExpression).binding;
	        }
	    } else if (someExpression instanceof FieldReference) {
	        return ((FieldReference)someExpression).binding;
	    } else if (someExpression instanceof QualifiedNameReference) {
	        QualifiedNameReference qName = (QualifiedNameReference) someExpression;
	        if (qName.otherBindings == null && ((someExpression.bits & RestrictiveFlagMASK) == BindingIds.FIELD)) {
	            return (FieldBinding)qName.binding;
	        } else {
	            return qName.otherBindings[qName.otherBindings.length - 1];
	        }
	    }
	    return null;
	}	
	public StringBuffer print(int indent, StringBuffer output) {

		//no () when used as a statement 
		printIndent(indent, output);
		return printExpressionNoParenthesis(indent, output);
	}
	public StringBuffer printExpression(int indent, StringBuffer output) {

		//subclass redefine printExpressionNoParenthesis()
		output.append('(');
		return printExpressionNoParenthesis(0, output).append(')');
	} 

	public StringBuffer printExpressionNoParenthesis(int indent, StringBuffer output) {

		lhs.printExpression(indent, output).append(" = "); //$NON-NLS-1$
		return expression.printExpression(0, output);
	}
	
	public StringBuffer printStatement(int indent, StringBuffer output) {

		//no () when used as a statement 
		return print(indent, output).append(';');
	}

	public TypeBinding resolveType(BlockScope scope) {

		// due to syntax lhs may be only a NameReference, a FieldReference or an ArrayReference
		constant = NotAConstant;
		if (!(this.lhs instanceof Reference) || this.lhs.isThis()) {
			scope.problemReporter().expressionShouldBeAVariable(this.lhs);
			return null;
		}
		TypeBinding lhsType = this.resolvedType = lhs.resolveType(scope);
		expression.setExpectedType(lhsType); // needed in case of generic method invocation
		TypeBinding rhsType = expression.resolveType(scope);
		if (lhsType == null || rhsType == null) {
			return null;
		}
		checkAssignmentEffect(scope);

		// Compile-time conversion of base-types : implicit narrowing integer into byte/short/character
		// may require to widen the rhs expression at runtime
		if ((expression.isConstantValueOfTypeAssignableToType(rhsType, lhsType)
 (lhsType.isBaseType() && BaseTypeBinding.isWidening(lhsType.id, rhsType.id)))
 rhsType.isCompatibleWith(lhsType)) {
			expression.computeConversion(scope, lhsType, rhsType);
			checkAssignment(scope, lhsType, rhsType);
			return this.resolvedType;
		}
		scope.problemReporter().typeMismatchError(rhsType, lhsType, expression);
		return lhsType;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.internal.compiler.ast.Expression#resolveTypeExpecting(org.eclipse.jdt.internal.compiler.lookup.BlockScope, org.eclipse.jdt.internal.compiler.lookup.TypeBinding)
	 */
	public TypeBinding resolveTypeExpecting(
			BlockScope scope,
			TypeBinding expectedType) {

		TypeBinding type = super.resolveTypeExpecting(scope, expectedType);
		TypeBinding lhsType = this.resolvedType; 
		TypeBinding rhsType = this.expression.resolvedType;
		// signal possible accidental boolean assignment (instead of using '==' operator)
		if (expectedType == BooleanBinding 
				&& lhsType == BooleanBinding 
				&& (this.lhs.bits & IsStrictlyAssignedMASK) != 0) {
			scope.problemReporter().possibleAccidentalBooleanAssignment(this);
		}
		checkAssignment(scope, lhsType, rhsType);
		return type;
	}

	public void traverse(ASTVisitor visitor, BlockScope scope) {
		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7880.java