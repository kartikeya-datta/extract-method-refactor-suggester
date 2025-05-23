error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8431.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8431.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8431.java
text:
```scala
i@@f (leftField != null &&  rhsType != TypeBinding.NULL && (lhsType.kind() == Binding.WILDCARD_TYPE) && ((WildcardBinding)lhsType).boundKind != Wildcard.SUPER) {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Genady Beriozkin - added support for reporting assignment with no effect
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class Assignment extends Expression {

	public Expression lhs;
	public Expression expression;
		
public Assignment(Expression lhs, Expression expression, int sourceEnd) {
	//lhs is always a reference by construction ,
	//but is build as an expression ==> the checkcast cannot fail
	this.lhs = lhs;
	lhs.bits |= IsStrictlyAssigned; // tag lhs as assigned
	this.expression = expression;
	this.sourceStart = lhs.sourceStart;
	this.sourceEnd = sourceEnd;
}

public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
	// record setting a variable: various scenarii are possible, setting an array reference, 
// a field reference, a blank final field reference, a field of an enclosing instance or 
// just a local variable.
	LocalVariableBinding local = this.lhs.localVariableBinding();
	int nullStatus = this.expression.nullStatus(flowInfo);
	if (local != null && (local.type.tagBits & TagBits.IsBaseType) == 0) {
		if (nullStatus == FlowInfo.NULL) {
			flowContext.recordUsingNullReference(currentScope, local, this.lhs, 
				FlowContext.CAN_ONLY_NULL | FlowContext.IN_ASSIGNMENT, flowInfo);
		}
	}
	flowInfo = ((Reference) lhs)
		.analyseAssignment(currentScope, flowContext, flowInfo, this, false)
		.unconditionalInits();
	if (local != null && (local.type.tagBits & TagBits.IsBaseType) == 0) {
		switch(nullStatus) {
			case FlowInfo.NULL :
				flowInfo.markAsDefinitelyNull(local);
				break;
			case FlowInfo.NON_NULL :
				flowInfo.markAsDefinitelyNonNull(local);
				break;
			default:
				flowInfo.markAsDefinitelyUnknown(local);
		}
		if (flowContext.initsOnFinally != null) {
			switch(nullStatus) {
				case FlowInfo.NULL :
					flowContext.initsOnFinally.markAsDefinitelyNull(local);
					break;
				case FlowInfo.NON_NULL :
					flowContext.initsOnFinally.markAsDefinitelyNonNull(local);
					break;
				default:
					flowContext.initsOnFinally.markAsDefinitelyUnknown(local);
			}
		}
	}		
	return flowInfo;
}

void checkAssignment(BlockScope scope, TypeBinding lhsType, TypeBinding rhsType) {
	FieldBinding leftField = getLastField(this.lhs);
	if (leftField != null &&  rhsType != TypeBinding.NULL && lhsType.isWildcard() && ((WildcardBinding)lhsType).boundKind != Wildcard.SUPER) {
	    scope.problemReporter().wildcardAssignment(lhsType, rhsType, this.expression);
	} else if (leftField != null && !leftField.isStatic() && leftField.declaringClass != null /*length pseudo field*/&& leftField.declaringClass.isRawType()) {
	    scope.problemReporter().unsafeRawFieldAssignment(leftField, rhsType, this.lhs);
	} else if (rhsType.needsUncheckedConversion(lhsType)) {
	    scope.problemReporter().unsafeTypeConversion(this.expression, rhsType, lhsType);
	}		
}

public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
	// various scenarii are possible, setting an array reference, 
	// a field reference, a blank final field reference, a field of an enclosing instance or 
	// just a local variable.

	int pc = codeStream.position;
	 ((Reference) lhs).generateAssignment(currentScope, codeStream, this, valueRequired);
	// variable may have been optimized out
	// the lhs is responsible to perform the implicitConversion generation for the assignment since optimized for unused local assignment.
	codeStream.recordPositionsFrom(pc, this.sourceStart);
}

public static Binding getDirectBinding(Expression someExpression) {
	if ((someExpression.bits & ASTNode.IgnoreNoEffectAssignCheck) != 0) {
		return null;
	}
	if (someExpression instanceof SingleNameReference) {
		return ((SingleNameReference)someExpression).binding;
	} else if (someExpression instanceof FieldReference) {
		FieldReference fieldRef = (FieldReference)someExpression;
		if (fieldRef.receiver.isThis() && !(fieldRef.receiver instanceof QualifiedThisReference)) {
			return fieldRef.binding;
		}			
	} else if (someExpression instanceof Assignment) {
		Expression lhs = ((Assignment)someExpression).lhs;
		if ((lhs.bits & ASTNode.IsStrictlyAssigned) != 0) {
			// i = i = ...; // eq to int i = ...;
			return getDirectBinding (((Assignment)someExpression).lhs);
		} else if (someExpression instanceof PrefixExpression) {
			// i = i++; // eq to ++i;
			return getDirectBinding (((Assignment)someExpression).lhs);
		}
	}
//		} else if (someExpression instanceof PostfixExpression) { // recurse for postfix: i++ --> i
//			// note: "b = b++" is equivalent to doing nothing, not to "b++"
//			return getDirectBinding(((PostfixExpression) someExpression).lhs);
	return null;
}

FieldBinding getLastField(Expression someExpression) {
    if (someExpression instanceof SingleNameReference) {
        if ((someExpression.bits & RestrictiveFlagMASK) == Binding.FIELD) {
            return (FieldBinding) ((SingleNameReference)someExpression).binding;
        }
    } else if (someExpression instanceof FieldReference) {
        return ((FieldReference)someExpression).binding;
    } else if (someExpression instanceof QualifiedNameReference) {
        QualifiedNameReference qName = (QualifiedNameReference) someExpression;
        if (qName.otherBindings == null) {
        	if ((someExpression.bits & RestrictiveFlagMASK) == Binding.FIELD) {
        		return (FieldBinding)qName.binding;
        	}
        } else {
            return qName.otherBindings[qName.otherBindings.length - 1];
        }
    }
    return null;
}	

public int nullStatus(FlowInfo flowInfo) {
	return this.expression.nullStatus(flowInfo);
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
	this.constant = Constant.NotAConstant;
	if (!(this.lhs instanceof Reference) || this.lhs.isThis()) {
		scope.problemReporter().expressionShouldBeAVariable(this.lhs);
		return null;
	}
	TypeBinding lhsType = lhs.resolveType(scope);
	this.expression.setExpectedType(lhsType); // needed in case of generic method invocation
	if (lhsType != null) 
		this.resolvedType = lhsType.capture(scope, this.sourceEnd);
	TypeBinding rhsType = this.expression.resolveType(scope);
	if (lhsType == null || rhsType == null) {
		return null;
	}
	
	// check for assignment with no effect
	Binding left = getDirectBinding(this.lhs);
	if (left != null && left == getDirectBinding(this.expression)) {
		scope.problemReporter().assignmentHasNoEffect(this, left.shortReadableName());
	}

	// Compile-time conversion of base-types : implicit narrowing integer into byte/short/character
	// may require to widen the rhs expression at runtime
	if (lhsType != rhsType) // must call before computeConversion() and typeMismatchError()
		scope.compilationUnitScope().recordTypeConversion(lhsType, rhsType);
	if ((this.expression.isConstantValueOfTypeAssignableToType(rhsType, lhsType)
 (lhsType.isBaseType() && BaseTypeBinding.isWidening(lhsType.id, rhsType.id)))
 rhsType.isCompatibleWith(lhsType)) {
		this.expression.computeConversion(scope, lhsType, rhsType);
		checkAssignment(scope, lhsType, rhsType);
		if (this.expression instanceof CastExpression 
				&& (this.expression.bits & ASTNode.UnnecessaryCast) == 0) {
			CastExpression.checkNeedForAssignedCast(scope, lhsType, (CastExpression) this.expression);
		}			
		return this.resolvedType;
	} else if (scope.isBoxingCompatibleWith(rhsType, lhsType) 
 (rhsType.isBaseType()  // narrowing then boxing ?
								&& scope.compilerOptions().sourceLevel >= ClassFileConstants.JDK1_5 // autoboxing
								&& !lhsType.isBaseType()
								&& this.expression.isConstantValueOfTypeAssignableToType(rhsType, scope.environment().computeBoxingType(lhsType)))) {
		this.expression.computeConversion(scope, lhsType, rhsType);
		if (this.expression instanceof CastExpression 
				&& (this.expression.bits & ASTNode.UnnecessaryCast) == 0) {
			CastExpression.checkNeedForAssignedCast(scope, lhsType, (CastExpression) this.expression);
		}			
		return this.resolvedType;
	} 
	scope.problemReporter().typeMismatchError(rhsType, lhsType, this.expression);
	return lhsType;
}

/**
 * @see org.eclipse.jdt.internal.compiler.ast.Expression#resolveTypeExpecting(org.eclipse.jdt.internal.compiler.lookup.BlockScope, org.eclipse.jdt.internal.compiler.lookup.TypeBinding)
 */
public TypeBinding resolveTypeExpecting(BlockScope scope, TypeBinding expectedType) {

	TypeBinding type = super.resolveTypeExpecting(scope, expectedType);
	if (type == null) return null;
	TypeBinding lhsType = this.resolvedType; 
	TypeBinding rhsType = this.expression.resolvedType;
	// signal possible accidental boolean assignment (instead of using '==' operator)
	if (expectedType == TypeBinding.BOOLEAN 
			&& lhsType == TypeBinding.BOOLEAN 
			&& (this.lhs.bits & IsStrictlyAssigned) != 0) {
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
public LocalVariableBinding localVariableBinding() {
	return lhs.localVariableBinding();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8431.java