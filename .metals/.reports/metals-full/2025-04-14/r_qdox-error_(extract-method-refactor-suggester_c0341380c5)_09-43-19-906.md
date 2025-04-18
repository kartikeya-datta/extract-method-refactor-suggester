error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5619.java
text:
```scala
r@@eturn this.getTypeBinding(retrieveCompilationUnitScope(expression).getJavaLangClass());

/*******************************************************************************
 * Copyright (c) 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.AnonymousLocalTypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.AstNode;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.Literal;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.OperatorExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BindingIds;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

/**
 * Internal class for resolving bindings using old ASTs.
 */
class DefaultBindingResolver extends BindingResolver {
	/**
	 * This map is used to keep the correspondance between new bindings and the 
	 * compiler bindings. This is an identity map. We should only create one object
	 * for one binding.
	 */
	Map compilerBindingsToASTBindings;
	
	/**
	 * This map is used to retrieve an old ast node using the new ast node. This is not an
	 * identity map.
	 */
	Map newAstToOldAst;
	
	/**
	 * This map is used to get an ast node from its binding (new binding)
	 */
	Map bindingsToAstNodes;
	
	/**
	 * This map is used to get a binding from its ast node
	 */
	Map astNodesToBindings;
	/**
	 * Constructor for DefaultBindingResolver.
	 */
	DefaultBindingResolver() {
		this.newAstToOldAst = new HashMap();
		this.compilerBindingsToASTBindings = new HashMap();
		this.bindingsToAstNodes = new HashMap();
		this.astNodesToBindings = new HashMap();
	}
	/*
	 * Method declared on BindingResolver.
	 */
	IBinding resolveName(Name name) {
		ASTNode parent = name.getParent();
		if (parent instanceof MethodInvocation
 parent instanceof SuperMethodInvocation) {
			return internalResolveNameForMethodInvocation(name);
		}
		if (parent instanceof FieldAccess
 parent instanceof SuperFieldAccess) {
			return internalResolveNameForFieldAccess(name);
		}
		if (parent instanceof PackageDeclaration) {
			return internalResolveNameForPackageDeclaration(name);
		}
		if (parent instanceof SimpleType) {
			return internalResolveNameForSimpleType(name);
		}
		if (parent instanceof ThisExpression) {
			return internalResolveNameForThisExpression(name);
		}
		if (name instanceof QualifiedName) {
			return internalResolveNameForQualifiedName(name);
		}
		if (name instanceof SimpleName) {
			return internalResolveNameForSimpleName(name);
		}
		return super.resolveName(name);
	}

	private IBinding internalResolveNameForPackageDeclaration(Name name) {
		PackageDeclaration packageDeclaration = (PackageDeclaration) name.getParent();
		CompilationUnit unit = (CompilationUnit) packageDeclaration.getParent();
		List types = unit.types();
		if (types.size() == 0) {
			return super.resolveName(name);
		}
		TypeDeclaration type = (TypeDeclaration) types.get(0);
		ITypeBinding typeBinding = type.resolveBinding();
		return typeBinding.getPackage();
	}
	/*
	 * Method declared on BindingResolver.
	 */
	ITypeBinding resolveType(Type type) {
		// retrieve the old ast node
		AstNode node = (AstNode) this.newAstToOldAst.get(type);
		if (node != null) {
			if (node instanceof TypeReference) {
				TypeReference typeReference = (TypeReference) node;
				return this.getTypeBinding(typeReference.binding);
			} else if (node instanceof SingleNameReference) {
				SingleNameReference singleNameReference = (SingleNameReference) node;
				if (singleNameReference.isTypeReference()) {
					return this.getTypeBinding((ReferenceBinding)singleNameReference.binding);
				} else {
					// it should be a type reference
					return null;
				}
			} else if (node instanceof QualifiedNameReference) {
				QualifiedNameReference qualifiedNameReference = (QualifiedNameReference) node;
				if (qualifiedNameReference.isTypeReference()) {
					return this.getTypeBinding((ReferenceBinding)qualifiedNameReference.binding);
				} else {
					// it should be a type reference
					return null;
				}
			}
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	ITypeBinding resolveWellKnownType(String name) {
		return super.resolveWellKnownType(name);
	}
	/*
	 * Method declared on BindingResolver.
	 */
	ITypeBinding resolveType(TypeDeclaration type) {
		org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) this.newAstToOldAst.get(type);
		if (typeDeclaration != null) {
			ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
			this.bindingsToAstNodes.put(typeBinding, type);
			return typeBinding;
		}
		return super.resolveType(type);
	}
	/*
	 * Method declared on BindingResolver.
	 */
	IMethodBinding resolveMethod(MethodDeclaration method) {
		AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) this.newAstToOldAst.get(method);
		if (methodDeclaration != null) {
			IMethodBinding methodBinding = this.getMethodBinding(methodDeclaration.binding);
			this.bindingsToAstNodes.put(methodBinding, method);
			return methodBinding;
		}
		return super.resolveMethod(method);
	}
	/*
	 * Method declared on BindingResolver.
	 */
	IVariableBinding resolveVariable(VariableDeclaration variable) {
		AbstractVariableDeclaration abstractVariableDeclaration = (AbstractVariableDeclaration) this.newAstToOldAst.get(variable);
		if (abstractVariableDeclaration instanceof org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) abstractVariableDeclaration;
			IVariableBinding variableBinding = this.getVariableBinding(fieldDeclaration.binding);
			this.bindingsToAstNodes.put(variableBinding, variable);
			return variableBinding;
		}
		IVariableBinding variableBinding = this.getVariableBinding(((LocalDeclaration) abstractVariableDeclaration).binding);
		this.bindingsToAstNodes.put(variableBinding, variable);
		return variableBinding;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	IVariableBinding resolveVariable(FieldDeclaration variable) {
		org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) this.newAstToOldAst.get(variable);
		IVariableBinding variableBinding = this.getVariableBinding(fieldDeclaration.binding);
		this.bindingsToAstNodes.put(variableBinding, variable);
		return variableBinding;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	ITypeBinding resolveExpressionType(Expression expression) {
		if (expression instanceof ClassInstanceCreation) {
			AstNode astNode = (AstNode) this.newAstToOldAst.get(expression);
			if (astNode instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
				org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) astNode;
				if (typeDeclaration != null) {
					ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
					this.bindingsToAstNodes.put(typeBinding, expression);
					return typeBinding;
				}
			} else {
				// should be an AllocationExpression
				AllocationExpression allocationExpression = (AllocationExpression) astNode;
				return this.getMethodBinding(allocationExpression.binding).getDeclaringClass();
			}
		} else if (expression instanceof Name) {
			IBinding binding = this.resolveName((Name) expression);
			if (binding == null) {
				return null;
			}
			switch(binding.getKind()) {
				case IBinding.TYPE :
					return (ITypeBinding) binding;
				case IBinding.VARIABLE :
					return ((IVariableBinding) binding).getType();
			}
		} else if (expression instanceof ArrayInitializer) {
			org.eclipse.jdt.internal.compiler.ast.ArrayInitializer oldAst = (org.eclipse.jdt.internal.compiler.ast.ArrayInitializer) this.newAstToOldAst.get(expression);
			if (oldAst == null || oldAst.binding == null) {
				return super.resolveExpressionType(expression);
			}
			return this.getTypeBinding(oldAst.binding);
		} else if (expression instanceof ArrayCreation) {
			ArrayAllocationExpression arrayAllocationExpression = (ArrayAllocationExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(arrayAllocationExpression.arrayTb);
		} else if (expression instanceof Assignment) {
			Assignment assignment = (Assignment) expression;
			return this.resolveExpressionType(assignment.getLeftHandSide());
		} else if (expression instanceof PostfixExpression) {
			PostfixExpression postFixExpression = (PostfixExpression) expression;
			return this.resolveExpressionType(postFixExpression.getOperand());
		} else if (expression instanceof PrefixExpression) {
			PrefixExpression preFixExpression = (PrefixExpression) expression;
			return this.resolveExpressionType(preFixExpression.getOperand());
		} else if (expression instanceof CastExpression) {
			org.eclipse.jdt.internal.compiler.ast.CastExpression castExpression = (org.eclipse.jdt.internal.compiler.ast.CastExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(castExpression.castTb);
		} else if (expression instanceof StringLiteral) {
			org.eclipse.jdt.internal.compiler.ast.StringLiteral stringLiteral = (org.eclipse.jdt.internal.compiler.ast.StringLiteral) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(stringLiteral.literalType(this.retrieveEnclosingScope(expression)));
		} else if (expression instanceof TypeLiteral) {
			ClassLiteralAccess classLiteralAccess = (ClassLiteralAccess) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(classLiteralAccess.targetType);
		} else if (expression instanceof BooleanLiteral) {
			BooleanLiteral booleanLiteral = (BooleanLiteral) expression;
			if (booleanLiteral.booleanValue()) {
				TrueLiteral trueLiteral = (TrueLiteral) this.newAstToOldAst.get(booleanLiteral);
				return this.getTypeBinding(trueLiteral.literalType(null));
			} else {
				FalseLiteral falseLiteral = (FalseLiteral) this.newAstToOldAst.get(booleanLiteral);
				return this.getTypeBinding(falseLiteral.literalType(null));
			}
		} else if (expression instanceof NullLiteral) {
			org.eclipse.jdt.internal.compiler.ast.NullLiteral nullLiteral = (org.eclipse.jdt.internal.compiler.ast.NullLiteral) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(nullLiteral.literalType(null));
		} else if (expression instanceof CharacterLiteral) {
			CharLiteral charLiteral = (CharLiteral) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(charLiteral.literalType(null));
		} else if (expression instanceof NumberLiteral) {
			Literal literal = (Literal) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(literal.literalType(null));
		} else if (expression instanceof InfixExpression) {
			OperatorExpression operatorExpression = (OperatorExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(operatorExpression.typeBinding);
		} else if (expression instanceof FieldAccess) {
			FieldReference fieldReference = (FieldReference) this.newAstToOldAst.get(expression);
			return this.getVariableBinding(fieldReference.binding).getType();
		} else if (expression instanceof SuperFieldAccess) {
			FieldReference fieldReference = (FieldReference) this.newAstToOldAst.get(expression);
			return this.getVariableBinding(fieldReference.binding).getType();
		} else if (expression instanceof ArrayAccess) {
			ArrayReference arrayReference = (ArrayReference) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(arrayReference.arrayElementBinding);
		} else if (expression instanceof ThisExpression) {
			ThisReference thisReference = (ThisReference) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(thisReference.resolveType(this.retrieveEnclosingScope(expression)));
		} else if (expression instanceof MethodInvocation) {
			MessageSend messageSend = (MessageSend)  this.newAstToOldAst.get(expression);
			return this.getMethodBinding(messageSend.binding).getReturnType();
		} else if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) expression;
			return this.resolveExpressionType(parenthesizedExpression.getExpression());
		}
		return super.resolveExpressionType(expression);
	}

	/*
	 * @see BindingResolver#resolveImport(ImportDeclaration)
	 */
	IBinding resolveImport(ImportDeclaration importDeclaration) {
		Scope scope = retrieveCompilationUnitScope(importDeclaration);
		AstNode node = (AstNode) this.newAstToOldAst.get(importDeclaration);
		if (node instanceof ImportReference) {
			ImportReference importReference = (ImportReference) node;
			if (importReference.onDemand) {
				Binding binding = scope.getTypeOrPackage(CharOperation.subarray(importReference.tokens, 0, importReference.tokens.length));
				if ((binding != null) && (binding.isValidBinding())) {
					IPackageBinding packageBinding = this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding) binding);
					this.bindingsToAstNodes.put(packageBinding, importDeclaration);
					return packageBinding;
				}
			} else {
				Binding binding = scope.getTypeOrPackage(importReference.tokens);
				if (binding != null && binding.isValidBinding()) {
					ITypeBinding typeBinding = this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
					this.bindingsToAstNodes.put(typeBinding, importDeclaration);
					return typeBinding;
				}
			}
		}
		return super.resolveImport(importDeclaration);
	}

	/*
	 * @see BindingResolver#resolvePackage(PackageDeclaration)
	 */
	IPackageBinding resolvePackage(PackageDeclaration pkg) {
		Scope scope = retrieveCompilationUnitScope(pkg);
		AstNode node = (AstNode) this.newAstToOldAst.get(pkg);
		if (node instanceof ImportReference) {
			ImportReference importReference = (ImportReference) node;
			Binding binding = scope.getTypeOrPackage(CharOperation.subarray(importReference.tokens, 0, importReference.tokens.length));
			if ((binding != null) && (binding.isValidBinding())) {
				IPackageBinding packageBinding = this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding) binding);
				this.bindingsToAstNodes.put(packageBinding, pkg);
				return packageBinding;
			}
		}
		return super.resolvePackage(pkg);
	}

	/*
	 * Method declared on BindingResolver.
	 */
	public ASTNode findDeclaringNode(IBinding binding) {
		return (ASTNode) this.bindingsToAstNodes.get(binding);
	}
	/*
	 * Method declared on BindingResolver.
	 */
	void store(ASTNode node, AstNode oldASTNode) {
		this.newAstToOldAst.put(node, oldASTNode);
	}
	/*
	 * Method declared on BindingResolver.
	 */
	protected ITypeBinding getTypeBinding(org.eclipse.jdt.internal.compiler.lookup.TypeBinding referenceBinding) {
		if (referenceBinding == null || !referenceBinding.isValidBinding()) {
			return null;
		}
		TypeBinding binding = (TypeBinding) this.compilerBindingsToASTBindings.get(referenceBinding);
		if (binding != null) {
			return binding;
		}
		binding = new TypeBinding(this, referenceBinding);
		this.compilerBindingsToASTBindings.put(referenceBinding, binding);
		return binding;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	protected IPackageBinding getPackageBinding(org.eclipse.jdt.internal.compiler.lookup.PackageBinding packageBinding) {
		if (packageBinding == null || !packageBinding.isValidBinding()) {
			return null;
		}
		IPackageBinding binding = (IPackageBinding) this.compilerBindingsToASTBindings.get(packageBinding);
		if (binding != null) {
			return binding;
		}
		binding = new PackageBinding(this, packageBinding);
		this.compilerBindingsToASTBindings.put(packageBinding, binding);
		return binding;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	protected IVariableBinding getVariableBinding(org.eclipse.jdt.internal.compiler.lookup.VariableBinding variableBinding) {
		if (variableBinding == null || !variableBinding.isValidBinding()) {
			return null;
		}
		IVariableBinding binding = (IVariableBinding) this.compilerBindingsToASTBindings.get(variableBinding);
		if (binding != null) {
			return binding;
		}
		binding = new VariableBinding(this, variableBinding);
		this.compilerBindingsToASTBindings.put(variableBinding, binding);
		return binding;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	protected IMethodBinding getMethodBinding(org.eclipse.jdt.internal.compiler.lookup.MethodBinding methodBinding) {
		if (methodBinding == null || !methodBinding.isValidBinding()) {
			return null;
		}
		IMethodBinding binding = (IMethodBinding) this.compilerBindingsToASTBindings.get(methodBinding);
		if (binding != null) {
			return binding;
		}
		binding = new MethodBinding(this, methodBinding);
		this.compilerBindingsToASTBindings.put(methodBinding, binding);
		return binding;
	}


	private Scope retrieveCompilationUnitScope(ASTNode node) {
		ASTNode currentNode = node;
		while(!(currentNode instanceof CompilationUnit)) {
			currentNode = currentNode.getParent();
		}
		CompilationUnitDeclaration compilationUnitDeclaration = (CompilationUnitDeclaration) this.newAstToOldAst.get(currentNode);
		return compilationUnitDeclaration.scope;
	}

	private BlockScope retrieveEnclosingScope(ASTNode node) {
		ASTNode currentNode = node;
		while(!(currentNode instanceof MethodDeclaration) && !(currentNode instanceof Initializer)) {
			currentNode = currentNode.getParent();
		}
		if (currentNode instanceof Initializer) {
			Initializer initializer = (Initializer) currentNode;
			while(!(currentNode instanceof TypeDeclaration)) {
				currentNode = currentNode.getParent();
			}
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDecl = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) this.newAstToOldAst.get(currentNode);
			if ((initializer.getModifiers() & Modifier.STATIC) != 0) {
				return typeDecl.staticInitializerScope;
			} else {
				return typeDecl.initializerScope;
			}
		}
		// this is a MethodDeclaration
		AbstractMethodDeclaration abstractMethodDeclaration = (AbstractMethodDeclaration) this.newAstToOldAst.get(currentNode);
		return abstractMethodDeclaration.scope;
	}	
	
	private IBinding internalResolveNameForQualifiedName(Name name) {
		QualifiedName qualifiedName = (QualifiedName) name;
		ASTNode parent = qualifiedName.getParent();
		int index = 0;
		while (parent instanceof QualifiedName) {
			qualifiedName = (QualifiedName) parent;
			parent = parent.getParent();
			index++;
		}
		return returnBindingForQualifiedNamePart(qualifiedName, index);
	}

	private IBinding returnBindingForQualifiedNamePart(ASTNode parent, int index) {
		// now we can retrieve the compiler's node
		AstNode node = (AstNode) this.newAstToOldAst.get(parent);
		if (node instanceof QualifiedNameReference) {
			QualifiedNameReference qualifiedNameReference = (QualifiedNameReference) node;
			if (qualifiedNameReference.isTypeReference()) {
				return this.getTypeBinding((ReferenceBinding)qualifiedNameReference.binding);
			} else {
				int qualifiedNameLength = qualifiedNameReference.tokens.length;
				int indexInQualifiedName = qualifiedNameLength - index; // one-based
				int indexOfFirstFieldBinding = qualifiedNameReference.indexOfFirstFieldBinding; // one-based
				int otherBindingLength = qualifiedNameLength - indexOfFirstFieldBinding;
				if (indexInQualifiedName < indexOfFirstFieldBinding) {
					// a extra lookup is required`
					Scope scope = retrieveEnclosingScope(parent);
					Binding binding = scope.getTypeOrPackage(CharOperation.subarray(qualifiedNameReference.tokens, 0, indexInQualifiedName));
					if (binding != null && binding.isValidBinding()) {
						if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.PackageBinding) {
							return this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding)binding);
						} else {
							// it is a type
							return this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding)binding);
						}
					}
					return null;
				} else {
					if (indexInQualifiedName == indexOfFirstFieldBinding) {
						Binding binding = qualifiedNameReference.binding;
						if (binding != null && binding.isValidBinding()) {
							return this.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.VariableBinding) binding);				
						} else {
							return null;
						}
					} else {
						return this.getVariableBinding(qualifiedNameReference.otherBindings[otherBindingLength - index - 1]);				
					}
				}
			}
		}
		return null;
	}

	private IBinding internalResolveNameForSimpleName(Name name) {
		AstNode node = (AstNode) this.newAstToOldAst.get(name);
		if (node == null) {
			ASTNode parent = name.getParent();
			if (parent instanceof QualifiedName) {
				// retrieve the qualified name and remember at which position is the simple name
				QualifiedName qualifiedName = (QualifiedName) parent;
				int index = -1;
				if (qualifiedName.getQualifier() == name) {
					index++;
				}
				while (parent instanceof QualifiedName) {
					qualifiedName = (QualifiedName) parent;
					parent = parent.getParent();
					index++;
				}
				return returnBindingForQualifiedNamePart(qualifiedName, index);
			}
		}
		if (node instanceof SingleNameReference) {
			SingleNameReference singleNameReference = (SingleNameReference) node;
			if (singleNameReference.isTypeReference()) {
				return this.getTypeBinding((ReferenceBinding)singleNameReference.binding);
			} else {
				// this is a variable or a field
				Binding binding = singleNameReference.binding;
				if (binding != null && binding.isValidBinding()) {
					return this.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.VariableBinding) binding);				
				} else {
					return null;
				}
			}
		} else if (node instanceof QualifiedSuperReference) {
			QualifiedSuperReference qualifiedSuperReference = (QualifiedSuperReference) node;
			return this.getTypeBinding(qualifiedSuperReference.qualification.binding);
		} else if (node instanceof LocalDeclaration) {
			return this.getVariableBinding(((LocalDeclaration)node).binding);
		} else if (node instanceof FieldReference) {
			return getVariableBinding(((FieldReference) node).binding);
		}
		return null;
	}

	private IBinding internalResolveNameForMethodInvocation(Name name) {
		ASTNode parent = name.getParent();
		if (parent instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation) parent;
			if (name == methodInvocation.getExpression()) {
				if (name.isQualifiedName()) {
					return this.internalResolveNameForQualifiedName(name);
				} else {
					return this.internalResolveNameForSimpleName(name);
				}
			} else {
				AstNode node = (AstNode) this.newAstToOldAst.get(name);
				if (node instanceof MessageSend) {
					MessageSend messageSend = (MessageSend) node;
					return getMethodBinding(messageSend.binding);
				} else if (name.isQualifiedName()) {
					return this.internalResolveNameForQualifiedName(name);
				} else {
					return this.internalResolveNameForSimpleName(name);
				}
			}
		} else {
			SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation) parent;
			if (name == superMethodInvocation.getQualifier()) {
				if (name.isQualifiedName()) {
					return this.internalResolveNameForQualifiedName(name);
				} else {
					return this.internalResolveNameForSimpleName(name);
				}
			} else {
				AstNode node = (AstNode) this.newAstToOldAst.get(name);
				if (node instanceof MessageSend) {
					MessageSend messageSend = (MessageSend) node;
					return getMethodBinding(messageSend.binding);
				} else if (name.isQualifiedName()) {
					return this.internalResolveNameForQualifiedName(name);
				} else {
					return this.internalResolveNameForSimpleName(name);
				}
			}
		}
	}
	
	private IBinding internalResolveNameForFieldAccess(Name name) {
		if (name.isQualifiedName()) {
			return this.internalResolveNameForQualifiedName(name);
		} else {
			return this.internalResolveNameForSimpleName(name);
		}
	}

	private IBinding internalResolveNameForSimpleType(Name name) {
		AstNode node = (AstNode) this.newAstToOldAst.get(name);
		if (node instanceof TypeReference) {
			return this.getTypeBinding(((TypeReference) node).binding);
		} else if (node instanceof NameReference) {
			NameReference nameReference = (NameReference) node;
			if (nameReference.isTypeReference()) {
				return this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) nameReference.binding);
			}
		}
		return null;
	}	

	private IBinding internalResolveNameForThisExpression(Name name) {
		AstNode node = (AstNode) this.newAstToOldAst.get(name);
		if (node instanceof TypeReference) {
			return this.getTypeBinding(((TypeReference) node).binding);
		}
		return null;
	}
	
	/*
	 * @see BindingResolver#resolveConstructor(ClassInstanceCreation)
	 */
	IMethodBinding resolveConstructor(ClassInstanceCreation expression) {
		AstNode node = (AstNode) this.newAstToOldAst.get(expression);
		if (node instanceof AnonymousLocalTypeDeclaration) {
			AnonymousLocalTypeDeclaration anonymousLocalTypeDeclaration = (AnonymousLocalTypeDeclaration) node;
			return this.getMethodBinding(anonymousLocalTypeDeclaration.allocation.binding);
		} else if (node instanceof AllocationExpression) {
			return this.getMethodBinding(((AllocationExpression)node).binding);
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveConstructor(ConstructorInvocation)
	 */
	IMethodBinding resolveConstructor(ConstructorInvocation expression) {
		AstNode node = (AstNode) this.newAstToOldAst.get(expression);
		if (node instanceof MessageSend) {
			MessageSend messageSend = (MessageSend) node;
			return this.getMethodBinding(messageSend.binding);
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveConstructor(SuperConstructorInvocation)
	 */
	IMethodBinding resolveConstructor(SuperConstructorInvocation expression) {
		AstNode node = (AstNode) this.newAstToOldAst.get(expression);
		if (node instanceof MessageSend) {
			MessageSend messageSend = (MessageSend) node;
			return this.getMethodBinding(messageSend.binding);
		}
		return null;
	}
	/*
	 * @see BindingResolver#resolveType(AnonymousClassDeclaration)
	 */
	ITypeBinding resolveType(AnonymousClassDeclaration type) {
		org.eclipse.jdt.internal.compiler.ast.AnonymousLocalTypeDeclaration anonymousLocalTypeDeclaration = (org.eclipse.jdt.internal.compiler.ast.AnonymousLocalTypeDeclaration) this.newAstToOldAst.get(type);
		if (anonymousLocalTypeDeclaration != null) {
			ITypeBinding typeBinding = this.getTypeBinding(anonymousLocalTypeDeclaration.binding);
			this.bindingsToAstNodes.put(typeBinding, type);
			return typeBinding;
		}
		return super.resolveType(type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5619.java