error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1742.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1742.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1742.java
text:
```scala
o@@rg.eclipse.jdt.internal.compiler.lookup.MethodBinding exactBinding = declaringClass.getExactMethod(methodBinding.selector, methodBinding.parameters, null);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jdt.core.dom;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocImplicitTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.JavadocFieldReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocMessageSend;
import org.eclipse.jdt.internal.compiler.ast.JavadocQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Literal;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.OperatorExpression;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteralConcatenation;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypes;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.ProblemFieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

/**
 * Internal class for resolving bindings using old ASTs.
 * <p>
 * IMPORTANT: The methods on this class are synchronized. This is required
 * because there may be multiple clients in separate threads concurrently
 * reading an AST and asking for bindings for its nodes. These requests all
 * end up invoking instance methods on this class. There are various internal
 * tables and caches which are built and maintained in the course of looking
 * up bindings. To ensure that they remain coherent in the presence of multiple
 * threads, the methods are synchronized on the DefaultBindingResolver instance.
 * </p>
 */
class DefaultBindingResolver extends BindingResolver {
	
	/*
	 * Holds on binding tables that can be shared by several ASTs.
	 */
	static class BindingTables {
	
		/**
		 * This map is used to get a binding from its binding key.
		 */
		Map bindingKeysToBindings;
		/**
		 * This map is used to keep the correspondance between new bindings and the 
		 * compiler bindings. This is an identity map. We should only create one object
		 * for one binding.
		 */
		Map compilerBindingsToASTBindings;
		
		BindingTables() {
			this.compilerBindingsToASTBindings = new HashMap();
			this.bindingKeysToBindings = new HashMap();
		}
	
	}
	/**
	 * This map is used to retrieve the corresponding block scope for a ast node
	 */
	Map astNodesToBlockScope;
	
	/**
	 * This map is used to get an ast node from its binding (new binding)
	 */
	Map bindingsToAstNodes;
	
	/*
	 * The shared binding tables accros ASTs.
	 */
	BindingTables bindingTables;
	
	/**
	 * This map is used to retrieve an old ast node using the new ast node. This is not an
	 * identity map.
	 */
	Map newAstToOldAst;
	
	/**
	 * Compilation unit scope
	 */
	private CompilationUnitScope scope;
	
	/**
	 * The working copy owner that defines the context in which this resolver is creating the bindings.
	 */
	WorkingCopyOwner workingCopyOwner;
	
	/**
	 * Constructor for DefaultBindingResolver.
	 */
	DefaultBindingResolver(CompilationUnitScope scope, WorkingCopyOwner workingCopyOwner, BindingTables bindingTables) {
		this.newAstToOldAst = new HashMap();
		this.astNodesToBlockScope = new HashMap();
		this.bindingsToAstNodes = new HashMap();
		this.bindingTables = bindingTables;
		this.scope = scope;
		this.workingCopyOwner = workingCopyOwner;
	}

	DefaultBindingResolver(LookupEnvironment lookupEnvironment, WorkingCopyOwner workingCopyOwner, BindingTables bindingTables) {
		this.newAstToOldAst = new HashMap();
		this.astNodesToBlockScope = new HashMap();
		this.bindingsToAstNodes = new HashMap();
		this.bindingTables = bindingTables;
		this.scope = new CompilationUnitScope(new CompilationUnitDeclaration(null, null, -1), lookupEnvironment);
		this.workingCopyOwner = workingCopyOwner;
	}

	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ASTNode findDeclaringNode(IBinding binding) {
		if (binding == null) {
			return null;
		}
		if (binding instanceof IMethodBinding) {
			IMethodBinding methodBinding = (IMethodBinding) binding;
			return (ASTNode) this.bindingsToAstNodes.get(methodBinding.getMethodDeclaration());
		} else if (binding instanceof ITypeBinding) {
			ITypeBinding typeBinding = (ITypeBinding) binding;
			return (ASTNode) this.bindingsToAstNodes.get(typeBinding.getTypeDeclaration());
		} else if (binding instanceof IVariableBinding) {
			IVariableBinding variableBinding = (IVariableBinding) binding;
			return (ASTNode) this.bindingsToAstNodes.get(variableBinding.getVariableDeclaration());
		}
		return (ASTNode) this.bindingsToAstNodes.get(binding);
	}
	
	synchronized ASTNode findDeclaringNode(String bindingKey) {
		if (bindingKey == null) {
			return null;
		}
		Object binding = this.bindingTables.bindingKeysToBindings.get(bindingKey);
		if (binding == null) 
			return null;
		return (ASTNode) this.bindingsToAstNodes.get(binding);
	}
	
	IBinding getBinding(org.eclipse.jdt.internal.compiler.lookup.Binding binding) {
		switch (binding.kind()) {
			case Binding.PACKAGE:
				return getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding) binding);
			case Binding.TYPE:
			case Binding.GENERIC_TYPE:
			case Binding.PARAMETERIZED_TYPE:
			case Binding.RAW_TYPE:
				return getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
			case Binding.ARRAY_TYPE:
			case Binding.TYPE_PARAMETER:
				return new TypeBinding(this, (org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
			case Binding.METHOD:
				return getMethodBinding((org.eclipse.jdt.internal.compiler.lookup.MethodBinding) binding);
			case Binding.FIELD:
			case Binding.LOCAL:
				return getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.VariableBinding) binding);
		}
		return null;
	}

	synchronized org.eclipse.jdt.internal.compiler.ast.ASTNode getCorrespondingNode(ASTNode currentNode) {
		return (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(currentNode);
	} 
	
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IMethodBinding getMethodBinding(org.eclipse.jdt.internal.compiler.lookup.MethodBinding methodBinding) {
		if (methodBinding != null) {
			if (methodBinding.isValidBinding()) {
				IMethodBinding binding = (IMethodBinding) this.bindingTables.compilerBindingsToASTBindings.get(methodBinding);
				if (binding != null) {
					return binding;
				}
				binding = new MethodBinding(this, methodBinding);
				this.bindingTables.compilerBindingsToASTBindings.put(methodBinding, binding);
				return binding;
			} else {
				/*
				 * http://dev.eclipse.org/bugs/show_bug.cgi?id=23597
				 */
				switch(methodBinding.problemId()) {
					case ProblemReasons.NotVisible : 
					case ProblemReasons.NonStaticReferenceInStaticContext :
					case ProblemReasons.NonStaticReferenceInConstructorInvocation :
						ReferenceBinding declaringClass = methodBinding.declaringClass;
						if (declaringClass != null) {
							org.eclipse.jdt.internal.compiler.lookup.MethodBinding exactBinding = declaringClass.getExactMethod(methodBinding.selector, methodBinding.parameters);
							if (exactBinding != null) {
								IMethodBinding binding = (IMethodBinding) this.bindingTables.compilerBindingsToASTBindings.get(exactBinding);
								if (binding != null) {
									return binding;
								}
								binding = new MethodBinding(this, exactBinding);
								this.bindingTables.compilerBindingsToASTBindings.put(exactBinding, binding);
								return binding;
							}
						}
						break;
				}
			}
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IPackageBinding getPackageBinding(org.eclipse.jdt.internal.compiler.lookup.PackageBinding packageBinding) {
		if (packageBinding == null || !packageBinding.isValidBinding()) {
			return null;
		}
		IPackageBinding binding = (IPackageBinding) this.bindingTables.compilerBindingsToASTBindings.get(packageBinding);
		if (binding != null) {
			return binding;
		}
		binding = new PackageBinding(packageBinding);
		this.bindingTables.compilerBindingsToASTBindings.put(packageBinding, binding);
		return binding;
	}
	private int getTypeArguments(ParameterizedQualifiedTypeReference typeReference) {
		TypeReference[][] typeArguments = typeReference.typeArguments;
		int value = 0;
		for (int i = 0, max = typeArguments.length; i < max; i++) {
			if ((typeArguments[i] != null) || (value != 0)) {
				value++;
			}
		}
		return value;
	}
		
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ITypeBinding getTypeBinding(org.eclipse.jdt.internal.compiler.lookup.TypeBinding referenceBinding) {
		if (referenceBinding == null) {
			return null;
		} else if (!referenceBinding.isValidBinding()) {
			switch(referenceBinding.problemId()) {
				case ProblemReasons.NotVisible : 
				case ProblemReasons.NonStaticReferenceInStaticContext :
					if (referenceBinding instanceof ProblemReferenceBinding) {
						ProblemReferenceBinding problemReferenceBinding = (ProblemReferenceBinding) referenceBinding;
						Binding binding2 = problemReferenceBinding.original;
						if (binding2 != null && binding2 instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
							TypeBinding binding = (TypeBinding) this.bindingTables.compilerBindingsToASTBindings.get(binding2);
							if (binding != null) {
								return binding;
							}
							binding = new TypeBinding(this, (org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding2);
							this.bindingTables.compilerBindingsToASTBindings.put(binding2, binding);
							return binding;
						} 
					}
			}
			return null;
		} else {
			TypeBinding binding = (TypeBinding) this.bindingTables.compilerBindingsToASTBindings.get(referenceBinding);
			if (binding != null) {
				return binding;
			}
			binding = new TypeBinding(this, referenceBinding);
			this.bindingTables.compilerBindingsToASTBindings.put(referenceBinding, binding);
			return binding;
		}
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IVariableBinding getVariableBinding(org.eclipse.jdt.internal.compiler.lookup.VariableBinding variableBinding) {
 		if (variableBinding != null) {
	 		if (variableBinding.isValidBinding()) {
				IVariableBinding binding = (IVariableBinding) this.bindingTables.compilerBindingsToASTBindings.get(variableBinding);
				if (binding != null) {
					return binding;
				}
				binding = new VariableBinding(this, variableBinding);
				this.bindingTables.compilerBindingsToASTBindings.put(variableBinding, binding);
				return binding;
	 		} else {
				/*
				 * http://dev.eclipse.org/bugs/show_bug.cgi?id=24449
				 */
				if (variableBinding instanceof ProblemFieldBinding) {
					ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) variableBinding;
					switch(problemFieldBinding.problemId()) {
						case ProblemReasons.NotVisible : 
						case ProblemReasons.NonStaticReferenceInStaticContext :
						case ProblemReasons.NonStaticReferenceInConstructorInvocation :
							ReferenceBinding declaringClass = problemFieldBinding.declaringClass;
							FieldBinding exactBinding = declaringClass.getField(problemFieldBinding.name, true /*resolve*/);
							if (exactBinding != null) {
								IVariableBinding variableBinding2 = (IVariableBinding) this.bindingTables.compilerBindingsToASTBindings.get(exactBinding);
								if (variableBinding2 != null) {
									return variableBinding2;
								}
								variableBinding2 = new VariableBinding(this, exactBinding);
								this.bindingTables.compilerBindingsToASTBindings.put(exactBinding, variableBinding2);
								return variableBinding2;
							}
							break;
					}
				}
	 		}
 		}
		return null;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	LookupEnvironment lookupEnvironment() {
		return this.scope.environment();
	}
	
	/**
	 * @see org.eclipse.jdt.core.dom.BindingResolver#recordScope(ASTNode, BlockScope)
	 */
	synchronized void recordScope(ASTNode astNode, BlockScope blockScope) {
		this.astNodesToBlockScope.put(astNode, blockScope);
	}
	
	/*
	 * @see BindingResolver#resolveBoxing(Expression)
	 */
	boolean resolveBoxing(Expression expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node != null && (node instanceof org.eclipse.jdt.internal.compiler.ast.Expression)) {
			org.eclipse.jdt.internal.compiler.ast.Expression compilerExpression = (org.eclipse.jdt.internal.compiler.ast.Expression) node;
			return (compilerExpression.implicitConversion & TypeIds.BOXING) != 0;
		}
		return false;
	}
	
	/*
	 * @see BindingResolver#resolveUnboxing(Expression)
	 */
	boolean resolveUnboxing(Expression expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node != null && (node instanceof org.eclipse.jdt.internal.compiler.ast.Expression)) {
			org.eclipse.jdt.internal.compiler.ast.Expression compilerExpression = (org.eclipse.jdt.internal.compiler.ast.Expression) node;
			return (compilerExpression.implicitConversion & TypeIds.UNBOXING) != 0;
		}
		return false;
	}
	
	/*
	 * @see BindingResolver#resolveConstantExpressionValue(Expression)
	 */
	Object resolveConstantExpressionValue(Expression expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node != null && (node instanceof org.eclipse.jdt.internal.compiler.ast.Expression)) {
			org.eclipse.jdt.internal.compiler.ast.Expression compilerExpression = (org.eclipse.jdt.internal.compiler.ast.Expression) node;
			Constant constant = compilerExpression.constant;
			if (constant != null && constant != Constant.NotAConstant) {
				switch (constant.typeID()) {
					case TypeIds.T_int : return new Integer(constant.intValue());
					case TypeIds.T_byte : return new Byte(constant.byteValue());
					case TypeIds.T_short : return new Short(constant.shortValue());
					case TypeIds.T_char : return new Character(constant.charValue());
					case TypeIds.T_float : return new Float(constant.floatValue());
					case TypeIds.T_double : return new Double(constant.doubleValue());
					case TypeIds.T_boolean : return constant.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
					case TypeIds.T_long : return new Long(constant.longValue());
					case TypeIds.T_JavaLangString : return constant.stringValue();
				}
				return null;
			}
		}
		return null;
	}
	
	/*
	 * @see BindingResolver#resolveConstructor(ClassInstanceCreation)
	 */
	synchronized IMethodBinding resolveConstructor(ClassInstanceCreation expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node != null && (node.bits & org.eclipse.jdt.internal.compiler.ast.ASTNode.IsAnonymousTypeMASK) != 0) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration anonymousLocalTypeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			return this.getMethodBinding(anonymousLocalTypeDeclaration.allocation.binding);
		} else if (node instanceof AllocationExpression) {
			return this.getMethodBinding(((AllocationExpression)node).binding);
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveConstructor(ConstructorInvocation)
	 */
	synchronized IMethodBinding resolveConstructor(ConstructorInvocation expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node instanceof ExplicitConstructorCall) {
			ExplicitConstructorCall explicitConstructorCall = (ExplicitConstructorCall) node;
			return this.getMethodBinding(explicitConstructorCall.binding);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.BindingResolver#resolveConstructor(org.eclipse.jdt.core.dom.EnumConstantDeclaration)
	 */
	IMethodBinding resolveConstructor(EnumConstantDeclaration enumConstantDeclaration) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(enumConstantDeclaration);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) node; 
			if (fieldDeclaration.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT && fieldDeclaration.initialization != null) {
				AllocationExpression allocationExpression = (AllocationExpression) fieldDeclaration.initialization;
				return this.getMethodBinding(allocationExpression.binding);
			}
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveConstructor(SuperConstructorInvocation)
	 */
	synchronized IMethodBinding resolveConstructor(SuperConstructorInvocation expression) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
		if (node instanceof ExplicitConstructorCall) {
			ExplicitConstructorCall explicitConstructorCall = (ExplicitConstructorCall) node;
			return this.getMethodBinding(explicitConstructorCall.binding);
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ITypeBinding resolveExpressionType(Expression expression) {
		if (expression instanceof ClassInstanceCreation) {
			org.eclipse.jdt.internal.compiler.ast.ASTNode astNode = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(expression);
			if (astNode instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
				org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) astNode;
				if (typeDeclaration != null) {
					ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
					if (typeBinding == null) {
						return null;
					}
					return typeBinding;
				}
			} else {
				// should be an AllocationExpression
				AllocationExpression allocationExpression = (AllocationExpression) astNode;
				IMethodBinding methodBinding = this.getMethodBinding(allocationExpression.binding);
				if (methodBinding == null) {
					return null;
				} else {
					return methodBinding.getDeclaringClass();
				}
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
				case IBinding.METHOD :
					return ((IMethodBinding) binding).getReturnType();
			}
		} else if (expression instanceof ArrayInitializer) {
			org.eclipse.jdt.internal.compiler.ast.ArrayInitializer oldAst = (org.eclipse.jdt.internal.compiler.ast.ArrayInitializer) this.newAstToOldAst.get(expression);
			if (oldAst == null || oldAst.binding == null) {
				return null;
			}
			return this.getTypeBinding(oldAst.binding);
		} else if (expression instanceof ArrayCreation) {
			ArrayAllocationExpression arrayAllocationExpression = (ArrayAllocationExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(arrayAllocationExpression.resolvedType);
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
			return this.getTypeBinding(castExpression.resolvedType);
		} else if (expression instanceof StringLiteral) {
			return this.getTypeBinding(this.scope.getJavaLangString());
		} else if (expression instanceof TypeLiteral) {
			ClassLiteralAccess classLiteralAccess = (ClassLiteralAccess) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(classLiteralAccess.resolvedType);
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
			Object node = this.newAstToOldAst.get(expression);
			if (node instanceof OperatorExpression) {
				OperatorExpression operatorExpression = (OperatorExpression) node;
				return this.getTypeBinding(operatorExpression.resolvedType);
			} else if (node instanceof StringLiteralConcatenation) {
				StringLiteralConcatenation stringLiteralConcatenation = (StringLiteralConcatenation) node;
				return this.getTypeBinding(stringLiteralConcatenation.resolvedType);
			}
		} else if (expression instanceof InstanceofExpression) {
			org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression instanceOfExpression = (org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(instanceOfExpression.resolvedType);
		} else if (expression instanceof FieldAccess) {
			FieldReference fieldReference = (FieldReference) this.newAstToOldAst.get(expression);
			IVariableBinding variableBinding = this.getVariableBinding(fieldReference.binding);
			if (variableBinding == null) {
				return null;
			} else {
				return variableBinding.getType();
			}
		} else if (expression instanceof SuperFieldAccess) {
			FieldReference fieldReference = (FieldReference) this.newAstToOldAst.get(expression);
			IVariableBinding variableBinding = this.getVariableBinding(fieldReference.binding);
			if (variableBinding == null) {
				return null;
			} else {
				return variableBinding.getType();
			}
		} else if (expression instanceof ArrayAccess) {
			ArrayReference arrayReference = (ArrayReference) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(arrayReference.resolvedType);
		} else if (expression instanceof ThisExpression) {
			ThisReference thisReference = (ThisReference) this.newAstToOldAst.get(expression);
			BlockScope blockScope = (BlockScope) this.astNodesToBlockScope.get(expression);
			if (blockScope == null) {
				return null;
			}
			return this.getTypeBinding(thisReference.resolveType(blockScope));
		} else if (expression instanceof MethodInvocation
 expression instanceof SuperMethodInvocation) {
			MessageSend messageSend = (MessageSend)  this.newAstToOldAst.get(expression);
			IMethodBinding methodBinding = this.getMethodBinding(messageSend.binding);
			if (methodBinding == null) {
				return null;
			} else {
				return methodBinding.getReturnType();
			}
		} else if (expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) expression;
			return this.resolveExpressionType(parenthesizedExpression.getExpression());
		} else if (expression instanceof ConditionalExpression) {
			org.eclipse.jdt.internal.compiler.ast.ConditionalExpression conditionalExpression = (org.eclipse.jdt.internal.compiler.ast.ConditionalExpression) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(conditionalExpression.resolvedType);
		} else if (expression instanceof VariableDeclarationExpression) {
			VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression) expression;
			Type type = variableDeclarationExpression.getType();
			if (type != null) {
				return type.resolveBinding();
			}
		} else if (expression instanceof MarkerAnnotation) {
			org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation markerAnnotation = (org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(markerAnnotation.resolvedType);
		} else if (expression instanceof NormalAnnotation) {
			org.eclipse.jdt.internal.compiler.ast.NormalAnnotation normalAnnotation = (org.eclipse.jdt.internal.compiler.ast.NormalAnnotation) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(normalAnnotation.resolvedType);
		} else if (expression instanceof SingleMemberAnnotation) {
			org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation singleMemberAnnotation = (org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation) this.newAstToOldAst.get(expression);
			return this.getTypeBinding(singleMemberAnnotation.resolvedType);
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveField(FieldAccess)
	 */
	synchronized IVariableBinding resolveField(FieldAccess fieldAccess) {
		Object oldNode = this.newAstToOldAst.get(fieldAccess);
		if (oldNode instanceof FieldReference) {
			FieldReference fieldReference = (FieldReference) oldNode;
			if (fieldReference != null) {
				return this.getVariableBinding(fieldReference.binding);
			}
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveField(SuperFieldAccess)
	 */
	synchronized IVariableBinding resolveField(SuperFieldAccess fieldAccess) {
		Object oldNode = this.newAstToOldAst.get(fieldAccess);
		if (oldNode instanceof FieldReference) {
			FieldReference fieldReference = (FieldReference) oldNode;
			if (fieldReference != null) {
				return this.getVariableBinding(fieldReference.binding);
			}
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolveImport(ImportDeclaration)
	 */
	synchronized IBinding resolveImport(ImportDeclaration importDeclaration) {
		try {
			org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(importDeclaration);
			if (node instanceof ImportReference) {
				ImportReference importReference = (ImportReference) node;
				final boolean isStatic = importReference.isStatic();
				if (importReference.onDemand) {
					Binding binding = this.scope.getImport(CharOperation.subarray(importReference.tokens, 0, importReference.tokens.length), true, isStatic);
					if (binding != null) {
						if (isStatic) {
							if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
								ITypeBinding typeBinding = this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
								return typeBinding == null ? null : typeBinding;								
							}
						} else {
							if ((binding.kind() & Binding.PACKAGE) != 0) {
								IPackageBinding packageBinding = this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding) binding);
								if (packageBinding == null) {
									return null;
								}
								return packageBinding;
							} else {
								// if it is not a package, it has to be a type
								ITypeBinding typeBinding = this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
								if (typeBinding == null) {
									return null;
								}
								return typeBinding;
							}
						}
					}
				} else {
					Binding binding = this.scope.getImport(importReference.tokens, false, isStatic);
					if (binding != null) {
						if (isStatic) {
							if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
								ITypeBinding typeBinding = this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
								return typeBinding == null ? null : typeBinding;								
							} else if (binding instanceof FieldBinding) {
								IVariableBinding variableBinding = this.getVariableBinding((FieldBinding) binding);
								return variableBinding == null ? null : variableBinding;								
							} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.MethodBinding) {
								// it is a type
								return this.getMethodBinding((org.eclipse.jdt.internal.compiler.lookup.MethodBinding)binding);						
							}
						} else {
							if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
								ITypeBinding typeBinding = this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding) binding);
								return typeBinding == null ? null : typeBinding;
							}
						}
					}
				}
			}
		} catch(RuntimeException e) {
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=63550
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=64299
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.BindingResolver#resolveMember(org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration)
	 */
	IMethodBinding resolveMember(AnnotationTypeMemberDeclaration declaration) {
		Object oldNode = this.newAstToOldAst.get(declaration);
		if (oldNode instanceof AbstractMethodDeclaration) {
			AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) oldNode;
			if (methodDeclaration != null) {
				IMethodBinding methodBinding = this.getMethodBinding(methodDeclaration.binding);
				if (methodBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(methodBinding, declaration);
				String key = methodBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, methodBinding);				
				}
				return methodBinding;
			}
		}
		return null;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IMethodBinding resolveMethod(MethodDeclaration method) {
		Object oldNode = this.newAstToOldAst.get(method);
		if (oldNode instanceof AbstractMethodDeclaration) {
			AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) oldNode;
			if (methodDeclaration != null) {
				IMethodBinding methodBinding = this.getMethodBinding(methodDeclaration.binding);
				if (methodBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(methodBinding, method);
				String key = methodBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, methodBinding);				
				}
				return methodBinding;
			}
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IMethodBinding resolveMethod(MethodInvocation method) {
		Object oldNode = this.newAstToOldAst.get(method);
		if (oldNode instanceof MessageSend) {
			MessageSend messageSend = (MessageSend) oldNode;
			if (messageSend != null) {
				return this.getMethodBinding(messageSend.binding);
			}
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IMethodBinding resolveMethod(SuperMethodInvocation method) {
		Object oldNode = this.newAstToOldAst.get(method);
		if (oldNode instanceof MessageSend) {
			MessageSend messageSend = (MessageSend) oldNode;
			if (messageSend != null) {
				return this.getMethodBinding(messageSend.binding);
			}
		}
		return null;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IBinding resolveName(Name name) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(name);
		int index = name.index;
		if (node instanceof QualifiedNameReference) {
			QualifiedNameReference qualifiedNameReference = (QualifiedNameReference) node;
			final char[][] tokens = qualifiedNameReference.tokens;
			int indexOfFirstFieldBinding = qualifiedNameReference.indexOfFirstFieldBinding; // one-based
			if (index < indexOfFirstFieldBinding) {
				// a extra lookup is required
				BlockScope internalScope = (BlockScope) this.astNodesToBlockScope.get(name);
				Binding binding = null;
				try {
					if (internalScope == null) {
						binding = this.scope.getTypeOrPackage(CharOperation.subarray(tokens, 0, index));
					} else {
						binding = internalScope.getTypeOrPackage(CharOperation.subarray(tokens, 0, index));
					}
				} catch (RuntimeException e) {
					// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
					// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=63550
					// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=64299
				}
				if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.PackageBinding) {
					return this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding)binding);
				} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
					// it is a type
					return this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding)binding);
				}
			} else if (index == indexOfFirstFieldBinding) {
				if (qualifiedNameReference.isTypeReference()) {
					return this.getTypeBinding(qualifiedNameReference.resolvedType);
				} else {
					Binding binding = qualifiedNameReference.binding;
					if (binding != null) {
						if (binding.isValidBinding()) {
							return this.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.VariableBinding) binding);				
						} else  if (binding instanceof ProblemFieldBinding) {
							ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) binding;
							switch(problemFieldBinding.problemId()) {
								case ProblemReasons.NotVisible : 
								case ProblemReasons.NonStaticReferenceInStaticContext :
									ReferenceBinding declaringClass = problemFieldBinding.declaringClass;
									if (declaringClass != null) {
										FieldBinding exactBinding = declaringClass.getField(tokens[tokens.length - 1], true /*resolve*/);
										if (exactBinding != null) {
											IVariableBinding variableBinding = (IVariableBinding) this.bindingTables.compilerBindingsToASTBindings.get(exactBinding);
											if (variableBinding != null) {
												return variableBinding;
											}
											variableBinding = new VariableBinding(this, exactBinding);
											this.bindingTables.compilerBindingsToASTBindings.put(exactBinding, variableBinding);
											return variableBinding;
										}
									}
									break;
							}
						}
					}
				}
			} else {
				/* This is the case for a name which is part of a qualified name that
				 * cannot be resolved. See PR 13063.
				 */
				if (qualifiedNameReference.otherBindings == null || (index - indexOfFirstFieldBinding - 1) < 0) {
					return null;
				} else {
					return this.getVariableBinding(qualifiedNameReference.otherBindings[index - indexOfFirstFieldBinding - 1]);				
				}
			}
		} else if (node instanceof QualifiedTypeReference) {
			QualifiedTypeReference qualifiedTypeReference = (QualifiedTypeReference) node;
			if (qualifiedTypeReference.resolvedType == null) {
				return null;
			}
			if (index == qualifiedTypeReference.tokens.length) {
				if (!qualifiedTypeReference.resolvedType.isValidBinding() && qualifiedTypeReference instanceof JavadocQualifiedTypeReference) {
					JavadocQualifiedTypeReference typeRef = (JavadocQualifiedTypeReference) node;
					if (typeRef.packageBinding != null) {
						return getPackageBinding(typeRef.packageBinding);
					}
				}
				return this.getTypeBinding(qualifiedTypeReference.resolvedType.leafComponentType());
			} else {
				if (index >= 0) {
					BlockScope internalScope = (BlockScope) this.astNodesToBlockScope.get(name);
					Binding binding = null;
					try {
						if (internalScope == null) {
							binding = this.scope.getTypeOrPackage(CharOperation.subarray(qualifiedTypeReference.tokens, 0, index));
						} else {
							binding = internalScope.getTypeOrPackage(CharOperation.subarray(qualifiedTypeReference.tokens, 0, index));
						}
					} catch (RuntimeException e) {
						// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
					}
					if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.PackageBinding) {
						return this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding)binding);
					} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
						// it is a type
						return this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding)binding);
					} else {
						return null;
					}
				}
			}
		} else if (node instanceof ImportReference) {
			ImportReference importReference = (ImportReference) node;
			int importReferenceLength = importReference.tokens.length;
			if (index >= 0) {
				Binding binding = null;
				if (importReferenceLength == index) {
					try {
						binding = this.scope.getImport(CharOperation.subarray(importReference.tokens, 0, index), importReference.onDemand, importReference.isStatic());
					} catch (RuntimeException e) {
						// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
					}
				} else {
					try {
						binding = this.scope.getImport(CharOperation.subarray(importReference.tokens, 0, index), true, importReference.isStatic());
					} catch (RuntimeException e) {
						// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
					}
				}
				if (binding != null) {
					if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.PackageBinding) {
						return this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding)binding);
					} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.TypeBinding) {
						// it is a type
						return this.getTypeBinding((org.eclipse.jdt.internal.compiler.lookup.TypeBinding)binding);
					} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.FieldBinding) {
						// it is a type
						return this.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.FieldBinding)binding);						
					} else if (binding instanceof org.eclipse.jdt.internal.compiler.lookup.MethodBinding) {
						// it is a type
						return this.getMethodBinding((org.eclipse.jdt.internal.compiler.lookup.MethodBinding)binding);						
					} else {
						return null;
					}
				}
			}
		} else if (node instanceof CompilationUnitDeclaration) {
			CompilationUnitDeclaration compilationUnitDeclaration = (CompilationUnitDeclaration) node;
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration[] types = compilationUnitDeclaration.types;
			if (types == null || types.length == 0) {
				return null;
			}
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration type = types[0];
			if (type != null) {
				ITypeBinding typeBinding = this.getTypeBinding(type.binding);
				if (typeBinding != null) {
					return typeBinding.getPackage();
				}
			}
		} else if (node instanceof AbstractMethodDeclaration) {
			AbstractMethodDeclaration methodDeclaration = (AbstractMethodDeclaration) node;
			if (methodDeclaration != null) {
				IMethodBinding methodBinding = this.getMethodBinding(methodDeclaration.binding);
				if (methodBinding != null) {
					return methodBinding;
				}
			}
		} else if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
			if (typeBinding != null) {
				return typeBinding;
			}
		} if (node instanceof SingleNameReference) {
			SingleNameReference singleNameReference = (SingleNameReference) node;
			if (singleNameReference.isTypeReference()) {
				return this.getTypeBinding(singleNameReference.resolvedType);
			} else {
				// this is a variable or a field
				Binding binding = singleNameReference.binding;
				if (binding != null) {
					if (binding.isValidBinding()) {
						return this.getVariableBinding((org.eclipse.jdt.internal.compiler.lookup.VariableBinding) binding);				
					} else {
						/*
						 * http://dev.eclipse.org/bugs/show_bug.cgi?id=24449
						 */
						if (binding instanceof ProblemFieldBinding) {
							ProblemFieldBinding problemFieldBinding = (ProblemFieldBinding) binding;
							switch(problemFieldBinding.problemId()) {
								case ProblemReasons.NotVisible : 
								case ProblemReasons.NonStaticReferenceInStaticContext :
								case ProblemReasons.NonStaticReferenceInConstructorInvocation :
									ReferenceBinding declaringClass = problemFieldBinding.declaringClass;
									FieldBinding exactBinding = declaringClass.getField(problemFieldBinding.name, true /*resolve*/);
									if (exactBinding != null) {
										IVariableBinding variableBinding2 = (IVariableBinding) this.bindingTables.compilerBindingsToASTBindings.get(exactBinding);
										if (variableBinding2 != null) {
											return variableBinding2;
										}
										variableBinding2 = new VariableBinding(this, exactBinding);
										this.bindingTables.compilerBindingsToASTBindings.put(exactBinding, variableBinding2);
										return variableBinding2;
									}
									break;
							}
						}
					}
	 			}				
			}
		} else if (node instanceof QualifiedSuperReference) {
			QualifiedSuperReference qualifiedSuperReference = (QualifiedSuperReference) node;
			return this.getTypeBinding(qualifiedSuperReference.qualification.resolvedType);
		} else if (node instanceof LocalDeclaration) {
			return this.getVariableBinding(((LocalDeclaration)node).binding);
		} else if (node instanceof JavadocFieldReference) {
			JavadocFieldReference fieldRef = (JavadocFieldReference) node;
			if (fieldRef.methodBinding != null) {
				return getMethodBinding(fieldRef.methodBinding);
			}
			return getVariableBinding(fieldRef.binding);
		} else if (node instanceof FieldReference) {
			return getVariableBinding(((FieldReference) node).binding);
		} else if (node instanceof SingleTypeReference) {
			SingleTypeReference singleTypeReference = (SingleTypeReference) node;
			org.eclipse.jdt.internal.compiler.lookup.TypeBinding binding = singleTypeReference.resolvedType;
			if (binding != null) {
				if (!binding.isValidBinding() && node instanceof JavadocSingleTypeReference) {
					JavadocSingleTypeReference typeRef = (JavadocSingleTypeReference) node;
					if (typeRef.packageBinding != null) {
						return getPackageBinding(typeRef.packageBinding);
					}
				}
				return this.getTypeBinding(binding.leafComponentType());
			}
		} else if (node instanceof org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) node;
			return this.getVariableBinding(fieldDeclaration.binding);
		} else if (node instanceof MessageSend) {
			MessageSend messageSend = (MessageSend) node;
			return getMethodBinding(messageSend.binding);
		} else if (node instanceof AllocationExpression) {
			AllocationExpression allocation = (AllocationExpression) node;
			return getMethodBinding(allocation.binding);
		} else if (node instanceof JavadocImplicitTypeReference) {
			JavadocImplicitTypeReference implicitRef = (JavadocImplicitTypeReference) node;
			return getTypeBinding(implicitRef.resolvedType);
		} else if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeParameter) {
			org.eclipse.jdt.internal.compiler.ast.TypeParameter typeParameter = (org.eclipse.jdt.internal.compiler.ast.TypeParameter) node;
			return this.getTypeBinding(typeParameter.binding);
		} else if (node instanceof org.eclipse.jdt.internal.compiler.ast.MemberValuePair) {
			org.eclipse.jdt.internal.compiler.ast.MemberValuePair memberValuePair = (org.eclipse.jdt.internal.compiler.ast.MemberValuePair) node;
			return getMethodBinding(memberValuePair.binding);
		}
		return null;
	}

	/*
	 * @see BindingResolver#resolvePackage(PackageDeclaration)
	 */
	synchronized IPackageBinding resolvePackage(PackageDeclaration pkg) {
		try {
			org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(pkg);
			if (node instanceof ImportReference) {
				ImportReference importReference = (ImportReference) node;
				Binding binding = this.scope.getTypeOrPackage(CharOperation.subarray(importReference.tokens, 0, importReference.tokens.length));
				if ((binding != null) && (binding.isValidBinding())) {
					IPackageBinding packageBinding = this.getPackageBinding((org.eclipse.jdt.internal.compiler.lookup.PackageBinding) binding);
					if (packageBinding == null) {
						return null;
					}
					this.bindingsToAstNodes.put(packageBinding, pkg);
					String key = packageBinding.getKey();
					if (key != null) {
						this.bindingTables.bindingKeysToBindings.put(key, packageBinding);				
					}
					return packageBinding;
				}
			}
		} catch (RuntimeException e) {
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=53357
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=63550
			// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=64299
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see BindingResolver#resolveReference(MemberRef)
     * @since 3.0
	 */
	synchronized IBinding resolveReference(MemberRef ref) {
		org.eclipse.jdt.internal.compiler.ast.Expression expression = (org.eclipse.jdt.internal.compiler.ast.Expression) this.newAstToOldAst.get(ref);
		if (expression instanceof TypeReference) {
			return getTypeBinding(expression.resolvedType);
		}
		else if (expression instanceof JavadocFieldReference) {
			JavadocFieldReference fieldRef = (JavadocFieldReference) expression;
			if (fieldRef.methodBinding != null) {
				return getMethodBinding(fieldRef.methodBinding);
			}
			return getVariableBinding(fieldRef.binding);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see BindingResolver#resolveReference(MethodRef)
     * @since 3.0
	 */
	synchronized IBinding resolveReference(MethodRef ref) {
		org.eclipse.jdt.internal.compiler.ast.Expression expression = (org.eclipse.jdt.internal.compiler.ast.Expression) this.newAstToOldAst.get(ref);
		if (expression instanceof JavadocMessageSend) {
			return this.getMethodBinding(((JavadocMessageSend)expression).binding);
		}
		else if (expression instanceof JavadocAllocationExpression) {
			return this.getMethodBinding(((JavadocAllocationExpression)expression).binding);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.BindingResolver#resolveType(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
	 */
	ITypeBinding resolveType(AnnotationTypeDeclaration type) {
		final Object node = this.newAstToOldAst.get(type);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			if (typeDeclaration != null) {
				ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
				if (typeBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(typeBinding, type);
				String key = typeBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, typeBinding);
				}
				return typeBinding;
			}
		}
		return null;
	}
	/*
	 * @see BindingResolver#resolveType(AnonymousClassDeclaration)
	 */
	synchronized ITypeBinding resolveType(AnonymousClassDeclaration type) {
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(type);
		if (node != null && (node.bits & org.eclipse.jdt.internal.compiler.ast.ASTNode.IsAnonymousTypeMASK) != 0) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration anonymousLocalTypeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			if (anonymousLocalTypeDeclaration != null) {
				ITypeBinding typeBinding = this.getTypeBinding(anonymousLocalTypeDeclaration.binding);
				if (typeBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(typeBinding, type);
				String key = typeBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, typeBinding);				
				}
				return typeBinding;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.BindingResolver#resolveType(org.eclipse.jdt.core.dom.EnumDeclaration)
	 */
	ITypeBinding resolveType(EnumDeclaration type) {
		final Object node = this.newAstToOldAst.get(type);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			if (typeDeclaration != null) {
				ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
				if (typeBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(typeBinding, type);
				String key = typeBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, typeBinding);
				}
				return typeBinding;
			}
		}
		return null;
	}

	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ITypeBinding resolveType(Type type) {
		// retrieve the old ast node
		org.eclipse.jdt.internal.compiler.ast.ASTNode node = (org.eclipse.jdt.internal.compiler.ast.ASTNode) this.newAstToOldAst.get(type);
		org.eclipse.jdt.internal.compiler.lookup.TypeBinding binding = null;
		if (node != null) {
            if (node instanceof ParameterizedQualifiedTypeReference) {
 				ParameterizedQualifiedTypeReference typeReference = (ParameterizedQualifiedTypeReference) node;
 				org.eclipse.jdt.internal.compiler.lookup.TypeBinding typeBinding = typeReference.resolvedType;
 				int index;
 				if (type.isQualifiedType()) {
 					index = ((QualifiedType) type).index;
 				} else if (type.isParameterizedType()) {
 					index = ((ParameterizedType) type).index;
 				} else {
 					index = 1;
 				}
 				final int numberOfTypeArgumentsNotNull = getTypeArguments(typeReference);
 				if (index != numberOfTypeArgumentsNotNull) {
	 				int  i = numberOfTypeArgumentsNotNull;
	 				while (i != index) {
	 					typeBinding = typeBinding.enclosingType();
	 					i --;
	 				}
	 				binding = typeBinding;
 				} else {
					binding = typeBinding;
 				}
            } else if (node instanceof TypeReference) {
				TypeReference typeReference = (TypeReference) node;
				binding = typeReference.resolvedType;
			} else if (node instanceof SingleNameReference && ((SingleNameReference)node).isTypeReference()) {
				binding = (((SingleNameReference)node).resolvedType);
			} else if (node instanceof QualifiedNameReference && ((QualifiedNameReference)node).isTypeReference()) {
				binding = (((QualifiedNameReference)node).resolvedType);
			} else if (node instanceof ArrayAllocationExpression) {
				binding = ((ArrayAllocationExpression) node).resolvedType;
			}
			if (binding != null) {
				if (type.isArrayType()) {
					ArrayType arrayType = (ArrayType) type;
					if (binding.isArrayType()) {
						ArrayBinding arrayBinding = (ArrayBinding) binding;
						return getTypeBinding(this.scope.createArrayType(arrayBinding.leafComponentType, arrayType.getDimensions()));
					} else {
						return getTypeBinding(this.scope.createArrayType(binding, arrayType.getDimensions()));
					}
				} else {
					if (binding.isArrayType()) {
						ArrayBinding arrayBinding = (ArrayBinding) binding;
						return getTypeBinding(arrayBinding.leafComponentType);
					} else {
						return getTypeBinding(binding);
					}
				}
			}
		} else if (type.isPrimitiveType()) {
			/* Handle the void primitive type returned by getReturnType for a method declaration 
			 * that is a constructor declaration. It prevents null from being returned
			 */
			if (((PrimitiveType) type).getPrimitiveTypeCode() == PrimitiveType.VOID) {
				return this.getTypeBinding(BaseTypes.VoidBinding);
			}
		}
		return null;
	}

	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ITypeBinding resolveType(TypeDeclaration type) {
		final Object node = this.newAstToOldAst.get(type);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.TypeDeclaration typeDeclaration = (org.eclipse.jdt.internal.compiler.ast.TypeDeclaration) node;
			if (typeDeclaration != null) {
				ITypeBinding typeBinding = this.getTypeBinding(typeDeclaration.binding);
				if (typeBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(typeBinding, type);
				String key = typeBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, typeBinding);				
				}
				return typeBinding;
			}
		}
		return null;
	}
	
	synchronized ITypeBinding resolveTypeParameter(TypeParameter typeParameter) {
		final Object node = this.newAstToOldAst.get(typeParameter);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.TypeParameter) {
			org.eclipse.jdt.internal.compiler.ast.TypeParameter typeParameter2 = (org.eclipse.jdt.internal.compiler.ast.TypeParameter) node;
			if (typeParameter2 != null) {
				ITypeBinding typeBinding = this.getTypeBinding(typeParameter2.binding);
				if (typeBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(typeBinding, typeParameter);
				String key = typeBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, typeBinding);				
				}
				return typeBinding;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jdt.core.dom.BindingResolver#resolveVariable(org.eclipse.jdt.core.dom.EnumConstantDeclaration)
	 */
	synchronized IVariableBinding resolveVariable(EnumConstantDeclaration enumConstant) {
		final Object node = this.newAstToOldAst.get(enumConstant);
		if (node instanceof org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) {
			org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) node;
			IVariableBinding variableBinding = this.getVariableBinding(fieldDeclaration.binding);
			if (variableBinding == null) {
				return null;
			}
			this.bindingsToAstNodes.put(variableBinding, enumConstant);
			String key = variableBinding.getKey();
			if (key != null) {
				this.bindingTables.bindingKeysToBindings.put(key, variableBinding);				
			}
			return variableBinding;
		}
		return null;
	}
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized IVariableBinding resolveVariable(VariableDeclaration variable) {
		final Object node = this.newAstToOldAst.get(variable);
		if (node instanceof AbstractVariableDeclaration) {
			AbstractVariableDeclaration abstractVariableDeclaration = (AbstractVariableDeclaration) node;
			if (abstractVariableDeclaration instanceof org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) {
				org.eclipse.jdt.internal.compiler.ast.FieldDeclaration fieldDeclaration = (org.eclipse.jdt.internal.compiler.ast.FieldDeclaration) abstractVariableDeclaration;
				IVariableBinding variableBinding = this.getVariableBinding(fieldDeclaration.binding);
				if (variableBinding == null) {
					return null;
				}
				this.bindingsToAstNodes.put(variableBinding, variable);
				String key = variableBinding.getKey();
				if (key != null) {
					this.bindingTables.bindingKeysToBindings.put(key, variableBinding);				
				}
				return variableBinding;
			}
			IVariableBinding variableBinding = this.getVariableBinding(((LocalDeclaration) abstractVariableDeclaration).binding);
			if (variableBinding == null) {
				return null;
			}
			this.bindingsToAstNodes.put(variableBinding, variable);
			String key = variableBinding.getKey();
			if (key != null) {
				this.bindingTables.bindingKeysToBindings.put(key, variableBinding);				
			}
			return variableBinding;
		}
		return null;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized ITypeBinding resolveWellKnownType(String name) {
		try {
			if (("boolean".equals(name))//$NON-NLS-1$
 ("char".equals(name))//$NON-NLS-1$
 ("byte".equals(name))//$NON-NLS-1$
 ("short".equals(name))//$NON-NLS-1$
 ("int".equals(name))//$NON-NLS-1$
 ("long".equals(name))//$NON-NLS-1$
 ("float".equals(name))//$NON-NLS-1$
 ("double".equals(name))//$NON-NLS-1$
 ("void".equals(name))) {//$NON-NLS-1$
				return this.getTypeBinding(Scope.getBaseType(name.toCharArray()));
			} else if ("java.lang.Object".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getJavaLangObject());
			} else if ("java.lang.String".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getJavaLangString());
			} else if ("java.lang.StringBuffer".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_STRINGBUFFER, 3));
			} else if ("java.lang.Throwable".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getJavaLangThrowable());
			} else if ("java.lang.Exception".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_EXCEPTION, 3));
			} else if ("java.lang.RuntimeException".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getJavaLangRuntimeException());
			} else if ("java.lang.Error".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getJavaLangError());
			} else if ("java.lang.Class".equals(name)) {//$NON-NLS-1$ 
				return this.getTypeBinding(this.scope.getJavaLangClass());
			} else if ("java.lang.Cloneable".equals(name)) {//$NON-NLS-1$ 
				return this.getTypeBinding(this.scope.getJavaLangCloneable());
			} else if ("java.io.Serializable".equals(name)) {//$NON-NLS-1$ 
				return this.getTypeBinding(this.scope.getJavaIoSerializable());
			} else if ("java.lang.Boolean".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_BOOLEAN, 3));
			} else if ("java.lang.Byte".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_BYTE, 3));
			} else if ("java.lang.Character".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_CHARACTER, 3));
			} else if ("java.lang.Double".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_DOUBLE, 3));
			} else if ("java.lang.Float".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_FLOAT, 3));
			} else if ("java.lang.Integer".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_INTEGER, 3));
			} else if ("java.lang.Long".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_LONG, 3));
			} else if ("java.lang.Short".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_SHORT, 3));
			} else if ("java.lang.Void".equals(name)) {//$NON-NLS-1$
				return this.getTypeBinding(this.scope.getType(TypeConstants.JAVA_LANG_VOID, 3));
			}
		} catch (AbortCompilation e) {
			// ignore missing types
		}
		return null;
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	public CompilationUnitScope scope() {
		return this.scope;
	}
		
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized void store(ASTNode node, org.eclipse.jdt.internal.compiler.ast.ASTNode oldASTNode) {
		this.newAstToOldAst.put(node, oldASTNode);
	}
	
	/*
	 * Method declared on BindingResolver.
	 */
	synchronized void updateKey(ASTNode node, ASTNode newNode) {
		Object astNode = this.newAstToOldAst.remove(node);
		if (astNode != null) {
			this.newAstToOldAst.put(newNode, astNode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1742.java