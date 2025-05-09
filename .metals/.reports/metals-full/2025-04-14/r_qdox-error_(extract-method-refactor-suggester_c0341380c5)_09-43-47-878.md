error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1859.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1859.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1859.java
text:
```scala
i@@f (this.javadocParser.checkDocComment && this.javadoc != null && this.patternFineGrain == 0 /* there's no fine grain concerning Javadoc*/) {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

/**
 * A parser that locates ast nodes that match a given search pattern.
 */
public class MatchLocatorParser extends Parser {

	MatchingNodeSet nodeSet;
	PatternLocator patternLocator;
	private ASTVisitor localDeclarationVisitor;
	final int patternFineGrain;

public static MatchLocatorParser createParser(ProblemReporter problemReporter, MatchLocator locator) {
	if ((locator.matchContainer & PatternLocator.COMPILATION_UNIT_CONTAINER) != 0) {
		return new ImportMatchLocatorParser(problemReporter, locator);
	}
	return new MatchLocatorParser(problemReporter, locator);
}

/**
 * An ast visitor that visits local type declarations.
 */
public class NoClassNoMethodDeclarationVisitor extends ASTVisitor {
	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		return (constructorDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		return (fieldDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type;
	}
	public boolean visit(Initializer initializer, MethodScope scope) {
		return (initializer.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		return (methodDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
}
public class MethodButNoClassDeclarationVisitor extends NoClassNoMethodDeclarationVisitor {
	public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
		MatchLocatorParser.this.patternLocator.match(localTypeDeclaration, MatchLocatorParser.this.nodeSet);
		return true;
	}
}
public class ClassButNoMethodDeclarationVisitor extends ASTVisitor {
	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		MatchLocatorParser.this.patternLocator.match(constructorDeclaration, MatchLocatorParser.this.nodeSet);
		return (constructorDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		MatchLocatorParser.this.patternLocator.match(fieldDeclaration, MatchLocatorParser.this.nodeSet);
		return (fieldDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type;
	}
	public boolean visit(Initializer initializer, MethodScope scope) {
		MatchLocatorParser.this.patternLocator.match(initializer, MatchLocatorParser.this.nodeSet);
		return (initializer.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
	public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
		MatchLocatorParser.this.patternLocator.match(memberTypeDeclaration, MatchLocatorParser.this.nodeSet);
		return true;
	}
	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		MatchLocatorParser.this.patternLocator.match(methodDeclaration, MatchLocatorParser.this.nodeSet);
		return (methodDeclaration.bits & ASTNode.HasLocalType) != 0; // continue only if it has local type
	}
	public boolean visit(AnnotationMethodDeclaration methodDeclaration, ClassScope scope) {
		MatchLocatorParser.this.patternLocator.match(methodDeclaration, MatchLocatorParser.this.nodeSet);
		return false; // no local type for annotation type members
	}
}
public class ClassAndMethodDeclarationVisitor extends ClassButNoMethodDeclarationVisitor {
	public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
		MatchLocatorParser.this.patternLocator.match(localTypeDeclaration, MatchLocatorParser.this.nodeSet);
		return true;
	}
}

protected MatchLocatorParser(ProblemReporter problemReporter, MatchLocator locator) {
	super(problemReporter, true);
	this.reportOnlyOneSyntaxError = true;
	this.patternLocator = locator.patternLocator;
	if ((locator.matchContainer & PatternLocator.CLASS_CONTAINER) != 0) {
		this.localDeclarationVisitor = (locator.matchContainer & PatternLocator.METHOD_CONTAINER) != 0
			? new ClassAndMethodDeclarationVisitor()
			: new ClassButNoMethodDeclarationVisitor();
	} else {
		this.localDeclarationVisitor = (locator.matchContainer & PatternLocator.METHOD_CONTAINER) != 0
			? new MethodButNoClassDeclarationVisitor()
			: new NoClassNoMethodDeclarationVisitor();
	}
	this.patternFineGrain = this.patternLocator.fineGrain();
}
public void checkComment() {
	super.checkComment();
	if (this.javadocParser.checkDocComment && this.javadoc != null) {

		// Search for pattern locator matches in javadoc comment parameters @param tags
		JavadocSingleNameReference[] paramReferences = this.javadoc.paramReferences;
		if (paramReferences != null) {
			for (int i=0, length=paramReferences.length; i < length; i++) {
				this.patternLocator.match(paramReferences[i], this.nodeSet);
			}
		}

		// Search for pattern locator matches in javadoc comment type parameters @param tags
		JavadocSingleTypeReference[] paramTypeParameters = this.javadoc.paramTypeParameters;
		if (paramTypeParameters != null) {
			for (int i=0, length=paramTypeParameters.length; i < length; i++) {
				this.patternLocator.match(paramTypeParameters[i], this.nodeSet);
			}
		}

		// Search for pattern locator matches in javadoc comment @throws/@exception tags
		TypeReference[] thrownExceptions = this.javadoc.exceptionReferences;
		if (thrownExceptions != null) {
			for (int i=0, length=thrownExceptions.length; i < length; i++) {
				this.patternLocator.match(thrownExceptions[i], this.nodeSet);
			}
		}

		// Search for pattern locator matches in javadoc comment @see tags
		Expression[] references = this.javadoc.seeReferences;
		if (references != null) {
			for (int i=0, length=references.length; i < length; i++) {
				Expression reference = references[i];
				if (reference instanceof TypeReference) {
					TypeReference typeRef = (TypeReference) reference;
					this.patternLocator.match(typeRef, this.nodeSet);
				} else if (reference instanceof JavadocFieldReference) {
					JavadocFieldReference fieldRef = (JavadocFieldReference) reference;
					this.patternLocator.match(fieldRef, this.nodeSet);
					if (fieldRef.receiver instanceof TypeReference && !fieldRef.receiver.isThis()) {
						TypeReference typeRef = (TypeReference) fieldRef.receiver;
						this.patternLocator.match(typeRef, this.nodeSet);
					}
				} else if (reference instanceof JavadocMessageSend) {
					JavadocMessageSend messageSend = (JavadocMessageSend) reference;
					this.patternLocator.match(messageSend, this.nodeSet);
					if (messageSend.receiver instanceof TypeReference && !messageSend.receiver.isThis()) {
						TypeReference typeRef = (TypeReference) messageSend.receiver;
						this.patternLocator.match(typeRef, this.nodeSet);
					}
					if (messageSend.arguments != null) {
						for (int a=0,al=messageSend.arguments.length; a<al; a++) {
							JavadocArgumentExpression argument = (JavadocArgumentExpression) messageSend.arguments[a];
							if (argument.argument != null && argument.argument.type != null) {
								this.patternLocator.match(argument.argument.type, this.nodeSet);
							}
						}
					}
				} else if (reference instanceof JavadocAllocationExpression) {
					JavadocAllocationExpression constructor = (JavadocAllocationExpression) reference;
					this.patternLocator.match(constructor, this.nodeSet);
					if (constructor.type != null && !constructor.type.isThis()) {
						this.patternLocator.match(constructor.type, this.nodeSet);
					}
					if (constructor.arguments != null) {
						for (int a=0,al=constructor.arguments.length; a<al; a++) {
							this.patternLocator.match(constructor.arguments[a], this.nodeSet);
							JavadocArgumentExpression argument = (JavadocArgumentExpression) constructor.arguments[a];
							if (argument.argument != null && argument.argument.type != null) {
								this.patternLocator.match(argument.argument.type, this.nodeSet);
							}
						}
					}
				}
			}
		}
	}
}

protected void classInstanceCreation(boolean alwaysQualified) {
	super.classInstanceCreation(alwaysQualified);
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(this.expressionStack[this.expressionPtr], this.nodeSet);
	} else if ((this.patternFineGrain & IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE) != 0) {
		AllocationExpression allocation = (AllocationExpression) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(allocation.type, this.nodeSet);
	}
}

protected void consumeAdditionalBound() {
	super.consumeAdditionalBound();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_VARIABLE_BOUND_TYPE_REFERENCE) != 0) {
		TypeReference typeReference = (TypeReference) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(typeReference, this.nodeSet);
	}
}

protected void consumeAssignment() {
	super.consumeAssignment();
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeCastExpressionLL1() {
	super.consumeCastExpressionLL1();
	if ((this.patternFineGrain & IJavaSearchConstants.CAST_TYPE_REFERENCE) != 0) {
		CastExpression castExpression = (CastExpression) this.expressionStack[this.expressionPtr];
		if (castExpression.type instanceof Reference) {
			this.patternLocator.match((Reference) castExpression.type, this.nodeSet);
        }
	}
}
protected void consumeCastExpressionWithGenericsArray() {
	super.consumeCastExpressionWithGenericsArray();
	if ((this.patternFineGrain & IJavaSearchConstants.CAST_TYPE_REFERENCE) != 0) {
		CastExpression castExpression = (CastExpression) this.expressionStack[this.expressionPtr];
		if (castExpression.type instanceof Reference) {
			this.patternLocator.match((Reference) castExpression.type, this.nodeSet);
        }
	}
}
protected void consumeCastExpressionWithNameArray() {
	super.consumeCastExpressionWithNameArray();
	if ((this.patternFineGrain & IJavaSearchConstants.CAST_TYPE_REFERENCE) != 0) {
		CastExpression castExpression = (CastExpression) this.expressionStack[this.expressionPtr];
		if (castExpression.type instanceof Reference) {
			this.patternLocator.match((Reference) castExpression.type, this.nodeSet);
        }
	}
}
protected void consumeCastExpressionWithPrimitiveType() {
	super.consumeCastExpressionWithPrimitiveType();
	if ((this.patternFineGrain & IJavaSearchConstants.CAST_TYPE_REFERENCE) != 0) {
		CastExpression castExpression = (CastExpression) this.expressionStack[this.expressionPtr];
		if (castExpression.type instanceof Reference) {
			this.patternLocator.match((Reference) castExpression.type, this.nodeSet);
        }
	}
}
protected void consumeCastExpressionWithQualifiedGenericsArray() {
	super.consumeCastExpressionWithQualifiedGenericsArray();
	if ((this.patternFineGrain & IJavaSearchConstants.CAST_TYPE_REFERENCE) != 0) {
		CastExpression castExpression = (CastExpression) this.expressionStack[this.expressionPtr];
		if (castExpression.type instanceof Reference) {
			this.patternLocator.match((Reference) castExpression.type, this.nodeSet);
        }
	}
}

protected void consumeClassHeaderExtends() {
	super.consumeClassHeaderExtends();
	if ((this.patternFineGrain & IJavaSearchConstants.SUPERTYPE_TYPE_REFERENCE) != 0) {
		TypeDeclaration typeDeclaration = (TypeDeclaration) this.astStack[this.astPtr];
		this.patternLocator.match(typeDeclaration.superclass, this.nodeSet);
	}
}

protected void consumeClassInstanceCreationExpressionQualifiedWithTypeArguments() {
	super.consumeClassInstanceCreationExpressionWithTypeArguments();
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(this.expressionStack[this.expressionPtr], this.nodeSet);
	} else if ((this.patternFineGrain & IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE) != 0) {
		AllocationExpression allocation = (AllocationExpression) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(allocation.type, this.nodeSet);
	}
}

protected void consumeClassInstanceCreationExpressionWithTypeArguments() {
	super.consumeClassInstanceCreationExpressionWithTypeArguments();
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(this.expressionStack[this.expressionPtr], this.nodeSet);
	} else if ((this.patternFineGrain & IJavaSearchConstants.CLASS_INSTANCE_CREATION_TYPE_REFERENCE) != 0) {
		AllocationExpression allocation = (AllocationExpression) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(allocation.type, this.nodeSet);
	}
}

protected void consumeEnterVariable() {
	boolean isLocalDeclaration = this.nestedMethod[this.nestedType] != 0; 
	super.consumeEnterVariable();
	if (isLocalDeclaration) {
		if ((this.patternFineGrain & IJavaSearchConstants.LOCAL_VARIABLE_DECLARATION_TYPE_REFERENCE) != 0) {
			LocalDeclaration localDeclaration = (LocalDeclaration) this.astStack[this.astPtr];
			this.patternLocator.match(localDeclaration.type, this.nodeSet);
		}
	} else {
		if ((this.patternFineGrain & IJavaSearchConstants.FIELD_DECLARATION_TYPE_REFERENCE) != 0) {
			FieldDeclaration fieldDeclaration = (FieldDeclaration) this.astStack[this.astPtr];
			this.patternLocator.match(fieldDeclaration.type, this.nodeSet);
		}
	}
}

protected void consumeExplicitConstructorInvocation(int flag, int recFlag) {
	super.consumeExplicitConstructorInvocation(flag, recFlag);
	this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
}
protected void consumeExplicitConstructorInvocationWithTypeArguments(int flag, int recFlag) {
	super.consumeExplicitConstructorInvocationWithTypeArguments(flag, recFlag);
	this.patternLocator.match(this.astStack[this.astPtr], this.nodeSet);
}
protected void consumeFieldAccess(boolean isSuperAccess) {
	super.consumeFieldAccess(isSuperAccess);

	int fineGrain = isSuperAccess ? IJavaSearchConstants.SUPER_REFERENCE : IJavaSearchConstants.THIS_REFERENCE;
	if (this.patternFineGrain == 0 || (this.patternFineGrain & fineGrain) != 0) {
		// this is always a Reference
		this.patternLocator.match((Reference) this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeFormalParameter(boolean isVarArgs) {
	super.consumeFormalParameter(isVarArgs);
	this.patternLocator.match((LocalDeclaration) this.astStack[this.astPtr], this.nodeSet);
}

protected void consumeInterfaceType() {
	super.consumeInterfaceType();
	if ((this.patternFineGrain & IJavaSearchConstants.SUPERTYPE_TYPE_REFERENCE) != 0) {
		TypeReference typeReference = (TypeReference) this.astStack[this.astPtr];
		this.patternLocator.match(typeReference, this.nodeSet);
	}
}

protected void consumeLocalVariableDeclaration() {
	super.consumeLocalVariableDeclaration();
	this.patternLocator.match((LocalDeclaration) this.astStack[this.astPtr], this.nodeSet);
}

protected void consumeMarkerAnnotation() {
	super.consumeMarkerAnnotation();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE) != 0) {
		Annotation annotation = (Annotation) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(annotation, this.nodeSet);
	}
}
protected void consumeMemberValuePair() {
	super.consumeMemberValuePair();
	this.patternLocator.match((MemberValuePair) this.astStack[this.astPtr], this.nodeSet);
}

protected void consumeMethodHeaderName(boolean isAnnotationMethod) {
	super.consumeMethodHeaderName(isAnnotationMethod);
	if ((this.patternFineGrain & IJavaSearchConstants.RETURN_TYPE_REFERENCE) != 0) {
		// when no fine grain flag is set, type reference match is evaluated in getTypeReference(int) method
		MethodDeclaration methodDeclaration = (MethodDeclaration) this.astStack[this.astPtr];
		this.patternLocator.match(methodDeclaration.returnType, this.nodeSet);
	}
}
protected void consumeMethodHeaderRightParen() {
	super.consumeMethodHeaderRightParen();
	if ((this.patternFineGrain & IJavaSearchConstants.PARAMETER_DECLARATION_TYPE_REFERENCE) != 0) {
		// when no fine grain flag is set, type reference match is evaluated in getTypeReference(int) method
		AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) this.astStack[this.astPtr];
		Argument[] arguments = methodDeclaration.arguments;
		if (arguments != null) {
			int argLength = arguments.length;
			for (int i=0; i<argLength; i++) {
				this.patternLocator.match(arguments[i].type, this.nodeSet);
			}
		}
	}
}
protected void consumeMethodHeaderThrowsClause() {
	super.consumeMethodHeaderThrowsClause();
	if ((this.patternFineGrain & IJavaSearchConstants.THROWS_CLAUSE_TYPE_REFERENCE) != 0) {
		// when no fine grain flag is set, type reference match is evaluated in getTypeReference(int) method
		AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) this.astStack[this.astPtr];
		TypeReference[] thrownExceptions = methodDeclaration.thrownExceptions;
		if (thrownExceptions != null) {
			int thrownLength = thrownExceptions.length;
			for (int i=0; i<thrownLength; i++) {
				this.patternLocator.match(thrownExceptions[i], this.nodeSet);
			}
		}
	}
}

protected void consumeMethodInvocationName() {
	super.consumeMethodInvocationName();
	MessageSend messageSend = (MessageSend) this.expressionStack[this.expressionPtr];
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(messageSend, this.nodeSet);
	} else {
		if (messageSend.receiver.isThis()) {
			if ((this.patternFineGrain & IJavaSearchConstants.IMPLICIT_THIS_REFERENCE) != 0) {
				this.patternLocator.match(messageSend, this.nodeSet);
			}
		} else {
			if ((this.patternFineGrain & IJavaSearchConstants.QUALIFIED_REFERENCE) != 0) {
				this.patternLocator.match(messageSend, this.nodeSet);
			}
		}
	}
}

protected void consumeMethodInvocationNameWithTypeArguments() {
	super.consumeMethodInvocationNameWithTypeArguments();
	MessageSend messageSend = (MessageSend) this.expressionStack[this.expressionPtr];
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(messageSend, this.nodeSet);
	} else {
		if (messageSend.receiver.isThis()) {
			if ((this.patternFineGrain & IJavaSearchConstants.IMPLICIT_THIS_REFERENCE) != 0) {
				this.patternLocator.match(messageSend, this.nodeSet);
			}
		} else {
			if ((this.patternFineGrain & IJavaSearchConstants.QUALIFIED_REFERENCE) != 0) {
				this.patternLocator.match(messageSend, this.nodeSet);
			}
		}
	}
}

protected void consumeMethodInvocationPrimary() {
	super.consumeMethodInvocationPrimary(); 
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.THIS_REFERENCE) != 0) {
		this.patternLocator.match((MessageSend) this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeMethodInvocationPrimaryWithTypeArguments() {
	super.consumeMethodInvocationPrimaryWithTypeArguments();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.THIS_REFERENCE) != 0) {
		this.patternLocator.match((MessageSend) this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeMethodInvocationSuper() {
	super.consumeMethodInvocationSuper();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.SUPER_REFERENCE) != 0) {
		this.patternLocator.match((MessageSend) this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeMethodInvocationSuperWithTypeArguments() {
	super.consumeMethodInvocationSuperWithTypeArguments();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.SUPER_REFERENCE) != 0) {
		this.patternLocator.match((MessageSend) this.expressionStack[this.expressionPtr], this.nodeSet);
	}
}

protected void consumeNormalAnnotation() {
	super.consumeNormalAnnotation();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE) != 0) {
		// this is always an Annotation
		Annotation annotation = (Annotation) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(annotation, this.nodeSet);
	}
}

protected void consumeOnlyTypeArguments() {
	super.consumeOnlyTypeArguments();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		int length = this.genericsLengthStack[this.genericsLengthPtr];
		if (length == 1) {
			TypeReference typeReference = (TypeReference)this.genericsStack[this.genericsPtr];
			if (!(typeReference instanceof Wildcard)) {
				this.patternLocator.match(typeReference, this.nodeSet);
            }
		}
	}
}

protected void consumePrimaryNoNewArray() {
	// pop parenthesis positions (and don't update expression positions
	// (see http://bugs.eclipse.org/bugs/show_bug.cgi?id=23329)
	this.intPtr--;
	this.intPtr--;
}

protected void consumePrimaryNoNewArrayWithName() {
	// PrimaryNoNewArray ::=  PushLPAREN Expression PushRPAREN 
	pushOnExpressionStack(getUnspecifiedReferenceOptimized());
	// pop parenthesis positions (and don't update expression positions
	// (see http://bugs.eclipse.org/bugs/show_bug.cgi?id=23329)
	this.intPtr--;
	this.intPtr--;
}

protected void consumeSingleMemberAnnotation() {
	super.consumeSingleMemberAnnotation();
	if (this.patternFineGrain == 0 || (this.patternFineGrain & IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE) != 0) {
		// this is always an Annotation
		Annotation annotation = (Annotation) this.expressionStack[this.expressionPtr];
		this.patternLocator.match(annotation, this.nodeSet);
	}
}

protected void consumeStatementCatch() {
	super.consumeStatementCatch();
	if ((this.patternFineGrain & IJavaSearchConstants.CATCH_TYPE_REFERENCE) != 0) {
		// when no fine grain flag is set, type reference match is evaluated in getTypeReference(int) method
		LocalDeclaration localDeclaration = (LocalDeclaration) this.astStack[this.astPtr-1];
		this.patternLocator.match(localDeclaration.type, this.nodeSet);
	}
}

protected void consumeTypeArgumentList1() {
	super.consumeTypeArgumentList1();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		for (int i=this.genericsPtr-this.genericsLengthStack[this.genericsLengthPtr]+1; i<=this.genericsPtr; i++) {
			TypeReference typeReference = (TypeReference)this.genericsStack[i];
			if (!(typeReference instanceof Wildcard)) {
				this.patternLocator.match(typeReference, this.nodeSet);
            }
		}
	}
}

protected void consumeTypeArgumentList2() {
	super.consumeTypeArgumentList2();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		for (int i=this.genericsPtr-this.genericsLengthStack[this.genericsLengthPtr]+1; i<=this.genericsPtr; i++) {
			TypeReference typeReference = (TypeReference)this.genericsStack[i];
			if (!(typeReference instanceof Wildcard)) {
				this.patternLocator.match(typeReference, this.nodeSet);
            }
		}
	}
}

protected void consumeTypeArgumentList3() {
	super.consumeTypeArgumentList3();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		for (int i=this.genericsPtr-this.genericsLengthStack[this.genericsLengthPtr]+1; i<=this.genericsPtr; i++) {
			TypeReference typeReference = (TypeReference)this.genericsStack[i];
			if (!(typeReference instanceof Wildcard)) {
				this.patternLocator.match(typeReference, this.nodeSet);
            }
		}
	}
}

protected void consumeTypeArgumentReferenceType1() {
	super.consumeTypeArgumentReferenceType1();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		int length = this.genericsLengthStack[this.genericsLengthPtr];
		if (length == 1) {
			TypeReference typeReference = (TypeReference)this.genericsStack[this.genericsPtr];
			TypeReference[] typeArguments = null;
			if (typeReference instanceof ParameterizedSingleTypeReference) {
	            typeArguments = ((ParameterizedSingleTypeReference) typeReference).typeArguments;
            } else if (typeReference instanceof ParameterizedQualifiedTypeReference) {
	            TypeReference[][] allTypeArguments = ((ParameterizedQualifiedTypeReference) typeReference).typeArguments;
	            typeArguments = allTypeArguments[allTypeArguments.length-1];
            }
			if (typeArguments != null) {
	            for (int i=0, ln=typeArguments.length; i<ln; i++) {
	            	if (!(typeArguments[i] instanceof Wildcard)) {
						this.patternLocator.match(typeArguments[i], this.nodeSet);
	            	}
	            }
			}
		}
	}
}

protected void consumeTypeArgumentReferenceType2() {
	super.consumeTypeArgumentReferenceType2();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		int length = this.genericsLengthStack[this.genericsLengthPtr];
		if (length == 1) {
			TypeReference typeReference = (TypeReference)this.genericsStack[this.genericsPtr];
			TypeReference[] typeArguments = null;
			if (typeReference instanceof ParameterizedSingleTypeReference) {
	            typeArguments = ((ParameterizedSingleTypeReference) typeReference).typeArguments;
            } else if (typeReference instanceof ParameterizedQualifiedTypeReference) {
	            TypeReference[][] allTypeArguments = ((ParameterizedQualifiedTypeReference) typeReference).typeArguments;
	            typeArguments = allTypeArguments[allTypeArguments.length-1];
            }
			if (typeArguments != null) {
	            for (int i=0, ln=typeArguments.length; i<ln; i++) {
	            	if (!(typeArguments[i] instanceof Wildcard)) {
						this.patternLocator.match(typeArguments[i], this.nodeSet);
	            	}
	            }
			}
		}
	}
}

protected void consumeTypeArguments() {
	super.consumeTypeArguments();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_ARGUMENT_TYPE_REFERENCE) != 0) {
		int length = this.genericsLengthStack[this.genericsLengthPtr];
		if (length == 1) {
			TypeReference typeReference = (TypeReference)this.genericsStack[this.genericsPtr];
			if (!(typeReference instanceof Wildcard)) {
				this.patternLocator.match(typeReference, this.nodeSet);
            }
		}
	}
}

protected void consumeTypeParameter1WithExtends() {
	super.consumeTypeParameter1WithExtends();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_VARIABLE_BOUND_TYPE_REFERENCE) != 0) {
		TypeParameter typeParameter = (TypeParameter) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(typeParameter.type, this.nodeSet);
	}
}

protected void consumeTypeParameter1WithExtendsAndBounds() {
	super.consumeTypeParameter1WithExtendsAndBounds();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_VARIABLE_BOUND_TYPE_REFERENCE) != 0) {
		TypeParameter typeParameter = (TypeParameter) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(typeParameter.type, this.nodeSet);
	}
}

protected void consumeTypeParameterHeader() {
	super.consumeTypeParameterHeader();
	this.patternLocator.match((TypeParameter)this.genericsStack[this.genericsPtr], this.nodeSet);
}

protected void consumeTypeParameterWithExtends() {
	super.consumeTypeParameterWithExtends();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_VARIABLE_BOUND_TYPE_REFERENCE) != 0) {
		TypeParameter typeParameter = (TypeParameter) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(typeParameter.type, this.nodeSet);
	}
}

protected void consumeTypeParameterWithExtendsAndBounds() {
	super.consumeTypeParameterWithExtendsAndBounds();
	if ((this.patternFineGrain & IJavaSearchConstants.TYPE_VARIABLE_BOUND_TYPE_REFERENCE) != 0) {
		TypeParameter typeParameter = (TypeParameter) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(typeParameter.type, this.nodeSet);
	}
}

protected void consumeUnaryExpression(int op, boolean post) {
	super.consumeUnaryExpression(op, post);
	this.patternLocator.match(this.expressionStack[this.expressionPtr], this.nodeSet);
}

protected void consumeWildcardBounds1Extends() {
	super.consumeWildcardBounds1Extends();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBounds1Super() {
	super.consumeWildcardBounds1Super();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBounds2Extends() {
	super.consumeWildcardBounds2Extends();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBounds2Super() {
	super.consumeWildcardBounds2Super();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBounds3Extends() {
	super.consumeWildcardBounds3Extends();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBounds3Super() {
	super.consumeWildcardBounds3Super();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBoundsExtends() {
	super.consumeWildcardBoundsExtends();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected void consumeWildcardBoundsSuper() {
	super.consumeWildcardBoundsSuper();
	if ((this.patternFineGrain & IJavaSearchConstants.WILDCARD_BOUND_TYPE_REFERENCE) != 0) {
		Wildcard wildcard = (Wildcard) this.genericsStack[this.genericsPtr];
		this.patternLocator.match(wildcard.bound, this.nodeSet);
	}
}

protected TypeReference copyDims(TypeReference typeRef, int dim) {
	TypeReference result = super.copyDims(typeRef, dim);
	 if (this.nodeSet.removePossibleMatch(typeRef) != null)
		this.nodeSet.addPossibleMatch(result);
	 else if (this.nodeSet.removeTrustedMatch(typeRef) != null)
		this.nodeSet.addTrustedMatch(result, true);
	return result;
}
protected TypeReference getTypeReference(int dim) {
	TypeReference typeRef = super.getTypeReference(dim);
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(typeRef, this.nodeSet); // NB: Don't check container since type reference can happen anywhere
	}
	return typeRef;
}
protected NameReference getUnspecifiedReference() {
	NameReference nameRef = super.getUnspecifiedReference();
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(nameRef, this.nodeSet); // NB: Don't check container since unspecified reference can happen anywhere
	} else if ((this.patternFineGrain & IJavaSearchConstants.QUALIFIED_REFERENCE) != 0) {
		if (nameRef instanceof QualifiedNameReference) {
			this.patternLocator.match(nameRef, this.nodeSet);
		}
	} else if ((this.patternFineGrain & IJavaSearchConstants.IMPLICIT_THIS_REFERENCE) != 0) {
		if (nameRef instanceof SingleNameReference) {
			this.patternLocator.match(nameRef, this.nodeSet);
		}
	}
	return nameRef;
}
protected NameReference getUnspecifiedReferenceOptimized() {
	NameReference nameRef = super.getUnspecifiedReferenceOptimized();
	if (this.patternFineGrain == 0) {
		this.patternLocator.match(nameRef, this.nodeSet); // NB: Don't check container since unspecified reference can happen anywhere
	} else {
		boolean flagQualifiedRef = (this.patternFineGrain & IJavaSearchConstants.QUALIFIED_REFERENCE) != 0;
		boolean flagImplicitThis = (this.patternFineGrain & IJavaSearchConstants.IMPLICIT_THIS_REFERENCE) != 0;
		if (flagQualifiedRef && flagImplicitThis) {
			this.patternLocator.match(nameRef, this.nodeSet);
		} else if (flagQualifiedRef) {
			if (nameRef instanceof QualifiedNameReference) {
				this.patternLocator.match(nameRef, this.nodeSet);
			}
		} else if (flagImplicitThis) {
			if (nameRef instanceof SingleNameReference) {
				this.patternLocator.match(nameRef, this.nodeSet);
			}
		}
	}
	return nameRef;
}
/**
 * Parses the method bodies in the given compilation unit
 * @param unit CompilationUnitDeclaration
 */
public void parseBodies(CompilationUnitDeclaration unit) {
	TypeDeclaration[] types = unit.types;
	if (types == null) return;

	for (int i = 0; i < types.length; i++) {
		TypeDeclaration type = types[i];
		this.patternLocator.match(type, this.nodeSet);
		this.parseBodies(type, unit);
	}
}
/**
 * Parses the member bodies in the given type.
 * @param type TypeDeclaration
 * @param unit CompilationUnitDeclaration
 */
protected void parseBodies(TypeDeclaration type, CompilationUnitDeclaration unit) {
	FieldDeclaration[] fields = type.fields;
	if (fields != null) {
		for (int i = 0; i < fields.length; i++) {
			FieldDeclaration field = fields[i];
			if (field instanceof Initializer)
				this.parse((Initializer) field, type, unit);
			field.traverse(this.localDeclarationVisitor, null);
		}
	}

	AbstractMethodDeclaration[] methods = type.methods;
	if (methods != null) {
		for (int i = 0; i < methods.length; i++) {
			AbstractMethodDeclaration method = methods[i];
			if (method.sourceStart >= type.bodyStart) { // if not synthetic
				if (method instanceof MethodDeclaration) {
					MethodDeclaration methodDeclaration = (MethodDeclaration) method;
					this.parse(methodDeclaration, unit);
					methodDeclaration.traverse(this.localDeclarationVisitor, (ClassScope) null);
				} else if (method instanceof ConstructorDeclaration) {
					ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) method;
					this.parse(constructorDeclaration, unit, false);
					constructorDeclaration.traverse(this.localDeclarationVisitor, (ClassScope) null);
				}
			} else if (method.isDefaultConstructor()) {
				method.parseStatements(this, unit);
			}
		}
	}

	TypeDeclaration[] memberTypes = type.memberTypes;
	if (memberTypes != null) {
		for (int i = 0; i < memberTypes.length; i++) {
			TypeDeclaration memberType = memberTypes[i];
			this.parseBodies(memberType, unit);
			memberType.traverse(this.localDeclarationVisitor, (ClassScope) null);
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1859.java