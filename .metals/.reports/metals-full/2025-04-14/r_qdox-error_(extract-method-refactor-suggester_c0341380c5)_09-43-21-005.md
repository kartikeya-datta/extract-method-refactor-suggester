error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9221.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9221.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9221.java
text:
```scala
i@@f (possible != null && possible != foundMethod) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.jdt.internal.compiler.util.ObjectVector;

public abstract class Scope
	implements BaseTypes, CompilerModifiers, ProblemReasons, TagBits, TypeConstants, TypeIds {

	public final static int BLOCK_SCOPE = 1;
	public final static int CLASS_SCOPE = 3;
	public final static int COMPILATION_UNIT_SCOPE = 4;
	public final static int METHOD_SCOPE = 2;

	public final static int NOT_COMPATIBLE = -1;
	public final static int COMPATIBLE = 0;
	public final static int AUTOBOX_COMPATIBLE = 1;
	public final static int VARARGS_COMPATIBLE = 2;

   /* Answer an int describing the relationship between the given types.
	*
	* 		NotRelated 
	* 		EqualOrMoreSpecific : left is compatible with right
	* 		MoreGeneric : right is compatible with left
	*/
	public static int compareTypes(TypeBinding left, TypeBinding right) {
		if (left.isCompatibleWith(right))
			return EqualOrMoreSpecific;
		if (right.isCompatibleWith(left))
			return MoreGeneric;
		return NotRelated;
	}

	/**
	 * Returns a type, where original type was substituted using the receiver
	 * parameterized type.
	 * In raw mode, all parameterized type denoting same original type are converted
	 * to raw types. e.g. 
	 * class X <T> {
	 *   X<T> foo;
	 *   X<String> bar;
	 * } when used in raw fashion, then type of both foo and bar is raw type X.
	 * 
	 */
	public static TypeBinding substitute(Substitution substitution, TypeBinding originalType) {
		
		switch (originalType.kind()) {
			
			case Binding.TYPE_PARAMETER:
				return substitution.substitute( (TypeVariableBinding) originalType);
				
			case Binding.PARAMETERIZED_TYPE:
				ParameterizedTypeBinding originalParameterizedType = (ParameterizedTypeBinding) originalType;
				ReferenceBinding originalEnclosing = originalType.enclosingType();
				ReferenceBinding substitutedEnclosing = originalEnclosing;
				if (originalEnclosing != null) {
					substitutedEnclosing = (ReferenceBinding) substitute(substitution, originalEnclosing);
				}
				if (substitution.isRawSubstitution()) {
					return originalParameterizedType.environment.createRawType(originalParameterizedType.type, substitutedEnclosing);
				}				
				TypeBinding[] originalArguments = originalParameterizedType.arguments;
				TypeBinding[] substitutedArguments = originalArguments;
				if (originalArguments != null) {
					substitutedArguments = substitute(substitution, originalArguments);
				}
				if (substitutedArguments != originalArguments || substitutedEnclosing != originalEnclosing) {
					identicalVariables: { // if substituted with original variables, then answer the generic type itself
						if (substitutedEnclosing != originalEnclosing) break identicalVariables;
						TypeVariableBinding[] originalVariables = originalParameterizedType.type.typeVariables();
						for (int i = 0, length = originalVariables.length; i < length; i++) {
							if (substitutedArguments[i] != originalVariables[i]) break identicalVariables;
						}
						return originalParameterizedType.type;
					}
					return originalParameterizedType.environment.createParameterizedType(
							originalParameterizedType.type, substitutedArguments, substitutedEnclosing);
				}
				break;
				
			case Binding.ARRAY_TYPE:
				ArrayBinding originalArrayType = (ArrayBinding) originalType;
				TypeBinding originalLeafComponentType = originalArrayType.leafComponentType;
				TypeBinding substitute = substitute(substitution, originalLeafComponentType); // substitute could itself be array type
				if (substitute != originalLeafComponentType) {
					return originalArrayType.environment.createArrayType(substitute.leafComponentType(), substitute.dimensions() + originalType.dimensions());
				}
				break;

			case Binding.WILDCARD_TYPE:
		        WildcardBinding wildcard = (WildcardBinding) originalType;
		        if (wildcard.kind != Wildcard.UNBOUND) {
			        TypeBinding originalBound = wildcard.bound;
			        TypeBinding substitutedBound = substitute(substitution, originalBound);
			        TypeBinding[] originalOtherBounds = wildcard.otherBounds;
			        TypeBinding[] substitutedOtherBounds = substitute(substitution, originalOtherBounds);
			        if (substitutedBound != originalBound || originalOtherBounds != substitutedOtherBounds) {
		        		return wildcard.environment.createWildcard(wildcard.genericType, wildcard.rank, substitutedBound, substitutedOtherBounds, wildcard.kind);
			        }
		        }
				break;

			case Binding.TYPE:
				if (!originalType.isMemberType()) break;
				// fall thru in case enclosing is generic
			case Binding.GENERIC_TYPE:
				ReferenceBinding originalGenericType = (ReferenceBinding) originalType;
				originalEnclosing = originalType.enclosingType();
				substitutedEnclosing = originalEnclosing;
				if (originalEnclosing != null) {
					substitutedEnclosing = (ReferenceBinding) substitute(substitution, originalEnclosing);
				}
				if (substitution.isRawSubstitution()) {
		            return substitution.environment().createRawType(originalGenericType, substitutedEnclosing);
	            }
			    // treat as if parameterized with its type variables
				TypeVariableBinding[] originalVariables = originalGenericType.typeVariables();
				int length = originalVariables.length;
				System.arraycopy(originalVariables, 0, originalArguments = new TypeBinding[length], 0, length);
				substitutedArguments = substitute(substitution, originalArguments);
				if (substitutedArguments != originalArguments || substitutedEnclosing != originalEnclosing) {
					return substitution.environment().createParameterizedType(
							originalGenericType, substitutedArguments, substitutedEnclosing);
				}
				break;
		}
		return originalType;
	}	
	
	/**
	 * Returns an array of types, where original types got substituted given a substitution.
	 * Only allocate an array if anything is different.
	 */
	public static ReferenceBinding[] substitute(Substitution substitution, ReferenceBinding[] originalTypes) {
		if (originalTypes == null) return null;
	    ReferenceBinding[] substitutedTypes = originalTypes;
	    for (int i = 0, length = originalTypes.length; i < length; i++) {
	        ReferenceBinding originalType = originalTypes[i];
	        ReferenceBinding substitutedParameter = (ReferenceBinding)substitute(substitution, originalType);
	        if (substitutedParameter != originalType) {
	            if (substitutedTypes == originalTypes) {
	                System.arraycopy(originalTypes, 0, substitutedTypes = new ReferenceBinding[length], 0, i);
	            }
	            substitutedTypes[i] = substitutedParameter;
	        } else if (substitutedTypes != originalTypes) {
	            substitutedTypes[i] = originalType;
	        }
	    }
	    return substitutedTypes;
	}

	/**
	 * Returns an array of types, where original types got substituted given a substitution.
	 * Only allocate an array if anything is different.
	 */
	public static TypeBinding[] substitute(Substitution substitution, TypeBinding[] originalTypes) {
		if (originalTypes == null) return null;
	    TypeBinding[] substitutedTypes = originalTypes;
	    for (int i = 0, length = originalTypes.length; i < length; i++) {
	        TypeBinding originalType = originalTypes[i];
	        TypeBinding substitutedParameter = substitute(substitution, originalType);
	        if (substitutedParameter != originalType) {
	            if (substitutedTypes == originalTypes) {
	                System.arraycopy(originalTypes, 0, substitutedTypes = new TypeBinding[length], 0, i);
	            }
	            substitutedTypes[i] = substitutedParameter;
	        } else if (substitutedTypes != originalTypes) {
	            substitutedTypes[i] = originalType;
	        }
	    }
	    return substitutedTypes;
	}

	public int kind;
	public Scope parent;

	protected Scope(int kind, Scope parent) {
		this.kind = kind;
		this.parent = parent;
	}

	/*
	 * Boxing primitive
	 */
	public TypeBinding boxing(TypeBinding type) {
		if (type.isBaseType())
			return environment().computeBoxingType(type);
		return type;
	}	

	public final ClassScope classScope() {
		Scope scope = this;
		do {
			if (scope instanceof ClassScope)
				return (ClassScope) scope;
			scope = scope.parent;
		} while (scope != null);
		return null;
	}	

	/* Answer an int describing the relationship between the given type and unchecked exceptions.
	*
	* 	NotRelated 
	* 	EqualOrMoreSpecific : type is known for sure to be an unchecked exception type
	* 	MoreGeneric : type is a supertype of an actual unchecked exception type
	*/
	public int compareUncheckedException(ReferenceBinding type) {
		int comparison = compareTypes(type, getJavaLangRuntimeException());
		if (comparison != 0) return comparison;
		return compareTypes(type, getJavaLangError());
	}

	public final CompilationUnitScope compilationUnitScope() {
		Scope lastScope = null;
		Scope scope = this;
		do {
			lastScope = scope;
			scope = scope.parent;
		} while (scope != null);
		return (CompilationUnitScope) lastScope;
	}

	/**
	 * Internal use only
	 * Given a method, returns null if arguments cannot be converted to parameters.
	 * Will answer a subsituted method in case the method was generic and type inference got triggered;
	 * in case the method was originally compatible, then simply answer it back.
	 */
	protected final MethodBinding computeCompatibleMethod(MethodBinding method, TypeBinding[] arguments, InvocationSite invocationSite) {

		TypeBinding[] genericTypeArguments = invocationSite.genericTypeArguments();
		TypeBinding[] parameters = method.parameters;
		TypeVariableBinding[] typeVariables = method.typeVariables;
		if (parameters == arguments
			&& (method.returnType.tagBits & HasTypeVariable) == 0 
			&& genericTypeArguments == null
			&& typeVariables == NoTypeVariables)
				return method;

		int argLength = arguments.length;
		int paramLength = parameters.length;
		boolean isVarArgs = method.isVarargs();
		if (argLength != paramLength)
			if (!isVarArgs || argLength < paramLength - 1)
				return null; // incompatible

		if (typeVariables != NoTypeVariables) { // generic method
			TypeBinding[] newArgs = null;
			for (int i = 0; i < argLength; i++) {
				TypeBinding param = i < paramLength ? parameters[i] : parameters[paramLength - 1];
				if (arguments[i].isBaseType() != param.isBaseType()) {
					if (newArgs == null) {
						newArgs = new TypeBinding[argLength];
						System.arraycopy(arguments, 0, newArgs, 0, argLength);
					}
					newArgs[i] = environment().computeBoxingType(arguments[i]);
				}	
			}
			if (newArgs != null)
				arguments = newArgs;
			method = ParameterizedGenericMethodBinding.computeCompatibleMethod(method, arguments, this, invocationSite);
			if (method == null) return null; // incompatible
			if (!method.isValidBinding()) return method; // bound check issue is taking precedence
			parameters = method.parameters; // reacquire them after type inference has performed
		} else if (genericTypeArguments != null) {
			if (method instanceof ParameterizedGenericMethodBinding) {
				if (!((ParameterizedGenericMethodBinding) method).wasInferred) {
					// attempt to invoke generic method of raw type with type hints <String>foo()
					return new ProblemMethodBinding(method, method.selector, genericTypeArguments, TypeArgumentsForRawGenericMethod);
				}
			} else {
				return new ProblemMethodBinding(method, method.selector, genericTypeArguments, TypeParameterArityMismatch);
			}
		}

		if (parameterCompatibilityLevel(method, arguments) > NOT_COMPATIBLE)
			return method;
		if (genericTypeArguments != null)
			return new ProblemMethodBinding(method, method.selector, arguments, ParameterizedMethodTypeMismatch);
		return null; // incompatible
	}
	
	protected boolean connectTypeVariables(TypeParameter[] typeParameters) {
		boolean noProblems = true;
		if (typeParameters == null || environment().options.sourceLevel < ClassFileConstants.JDK1_5) return true;

		nextVariable : for (int i = 0, paramLength = typeParameters.length; i < paramLength; i++) {
			TypeParameter typeParameter = typeParameters[i];
			TypeVariableBinding typeVariable = typeParameter.binding;
			if (typeVariable == null) return false;

			typeVariable.superclass = getJavaLangObject();
			typeVariable.superInterfaces = NoSuperInterfaces;
			// set firstBound to the binding of the first explicit bound in parameter declaration
			typeVariable.firstBound = null; // first bound used to compute erasure

			TypeReference typeRef = typeParameter.type;
			if (typeRef == null)
				continue nextVariable;
			TypeBinding superType = this.kind == METHOD_SCOPE
				? typeRef.resolveType((BlockScope)this, false/*no bound check*/)
				: typeRef.resolveType((ClassScope)this);
			if (superType == null) {
				typeVariable.tagBits |= HierarchyHasProblems;
				noProblems = false;
				continue nextVariable;
			}
			typeRef.resolvedType = superType; // hold onto the problem type
			if (superType.isArrayType()) {
				problemReporter().boundCannotBeArray(typeRef, superType);
				continue nextVariable;
			}
			if (superType.isTypeVariable()) {
				TypeVariableBinding varSuperType = (TypeVariableBinding) superType;
				if (varSuperType.rank >= typeVariable.rank && varSuperType.declaringElement == typeVariable.declaringElement) {
					problemReporter().forwardTypeVariableReference(typeParameter, varSuperType);
					typeVariable.tagBits |= HierarchyHasProblems;
					noProblems = false;
					continue nextVariable;
				}
			}
			ReferenceBinding superRefType = (ReferenceBinding) superType;
			if (superRefType.isFinal())
				problemReporter().finalVariableBound(typeVariable, typeRef);
			if (superType.isClass()) {
				typeVariable.superclass = superRefType;
			} else {
				typeVariable.superInterfaces = new ReferenceBinding[] {superRefType};
				typeVariable.modifiers |= AccInterface;
			}
			typeVariable.firstBound = superRefType; // first bound used to compute erasure

			TypeReference[] boundRefs = typeParameter.bounds;
			if (boundRefs != null) {
				for (int j = 0, k = boundRefs.length; j < k; j++) {
					typeRef = boundRefs[j];
					superType = this.kind == METHOD_SCOPE
						? typeRef.resolveType((BlockScope)this, false)
						: typeRef.resolveType((ClassScope)this);
					if (superType == null) {
						typeVariable.tagBits |= HierarchyHasProblems;
						noProblems = false;
						continue nextVariable;
					}
					typeRef.resolvedType = superType; // hold onto the problem type
					if (superType.isArrayType()) {
						problemReporter().boundCannotBeArray(typeRef, superType);
						continue nextVariable;
					}
					superRefType = (ReferenceBinding) superType;
					if (superType.isClass()) {
						problemReporter().boundMustBeAnInterface(typeRef, superType);
						typeVariable.tagBits |= HierarchyHasProblems;
						noProblems = false;
						continue nextVariable;
					}
					if (superType.isParameterizedType()) {
						ReferenceBinding match = typeVariable.superclass.findSuperTypeErasingTo((ReferenceBinding) superType.erasure());
						boolean isCollision = match != null && match != superType;
						for (int index = typeVariable.superInterfaces.length; !isCollision && --index >= 0;) {
							ReferenceBinding temp = typeVariable.superInterfaces[index];
							isCollision = superType != temp && superType.erasure() == temp.erasure();
						}
						if (isCollision) {
							problemReporter().boundHasConflictingArguments(typeRef, superType);
							typeVariable.tagBits |= HierarchyHasProblems;
							noProblems = false;
							continue nextVariable;
						}
					}
					for (int index = typeVariable.superInterfaces.length; --index >= 0;) {
						if (superType.erasure() == typeVariable.superInterfaces[index].erasure()) {
							problemReporter().duplicateBounds(typeRef, superType);
							typeVariable.tagBits |= HierarchyHasProblems;
							noProblems = false;
							continue nextVariable;
						}
					}
					int size = typeVariable.superInterfaces.length;
					System.arraycopy(typeVariable.superInterfaces, 0, typeVariable.superInterfaces = new ReferenceBinding[size + 1], 0, size);
					typeVariable.superInterfaces[size] = superRefType;
				}
			}
		}
		return noProblems;
	}

	public TypeBinding convertToRawType(TypeBinding type) {
		int dimension = type.dimensions();
		TypeBinding originalType = type.leafComponentType();
		if (originalType instanceof ReferenceBinding) {
			ReferenceBinding convertedType = (ReferenceBinding) originalType;
			ReferenceBinding originalEnclosing = originalType.enclosingType();
			ReferenceBinding convertedEnclosing = originalEnclosing;
			if (originalEnclosing != null && convertedType.isStatic() && originalEnclosing.isGenericType()) {
				convertedEnclosing = (ReferenceBinding) convertToRawType(originalEnclosing);
			}
			if (originalType.isGenericType()) {
				convertedType = environment().createRawType(convertedType, convertedEnclosing);
			} else if (originalEnclosing != convertedEnclosing) {
				convertedType = createParameterizedType(convertedType, null, convertedEnclosing);
			}
			if (originalType != convertedType) {
				return dimension > 0 ? (TypeBinding)createArrayType(convertedType, dimension) : convertedType;
			}
		}
		return type;
	}
//		TypeBinding leafType = type.leafComponentType();
//		int dimension = type.dimensions();
//		ReferenceBinding originalEnclosing = leafType.enclosingType();
//		ReferenceBinding convertedEnclosing = originalEnclosing;
//		if (originalEnclosing != null && ((ReferenceBinding)leafType).isStatic()) {
//			convertedEnclosing = (ReferenceBinding) convertToRawType(originalEnclosing);
//		}
//		if (leafType.isGenericType()) {
//			type = environment().createRawType((ReferenceBinding) leafType, convertedEnclosing);
//			if (dimension > 0) 
//				type = createArrayType(type, dimension);
//		} else if (originalEnclosing != convertedEnclosing) {
//			type = createParameterizedType((ReferenceBinding)leafType, null, convertedEnclosing);
//			if (dimension > 0) 
//				type = createArrayType(type, dimension);
//		}
//	    return type;

	public ArrayBinding createArrayType(TypeBinding type, int dimension) {
		if (type.isValidBinding())
			return environment().createArrayType(type, dimension);
		// do not cache obvious invalid types
		return new ArrayBinding(type, dimension, environment());
	}
	
	public ParameterizedTypeBinding createParameterizedType(ReferenceBinding genericType, TypeBinding[] arguments, ReferenceBinding enclosingType) {
		valid: {
			if (!genericType.isValidBinding()) break valid;
			for (int i = 0, max = arguments == null ? 0 : arguments.length; i < max; i++)
				if (!arguments[i].isValidBinding()) break valid;
			return environment().createParameterizedType(genericType, arguments, enclosingType);
		}
		return new ParameterizedTypeBinding(genericType, arguments, enclosingType, environment());
	}
	
	public TypeVariableBinding[] createTypeVariables(TypeParameter[] typeParameters, Binding declaringElement) {
		// do not construct type variables if source < 1.5
		if (typeParameters == null || environment().options.sourceLevel < ClassFileConstants.JDK1_5)
			return NoTypeVariables;

		TypeVariableBinding[] typeVariableBindings = NoTypeVariables;
		PackageBinding unitPackage = compilationUnitScope().fPackage;
		int length = typeParameters.length;
		typeVariableBindings = new TypeVariableBinding[length];
		int count = 0;
		for (int i = 0; i < length; i++) {
			TypeParameter typeParameter = typeParameters[i];
			TypeVariableBinding parameterBinding = new TypeVariableBinding(typeParameter.name, declaringElement, i);
			parameterBinding.fPackage = unitPackage;
			typeParameter.binding = parameterBinding;

			// detect duplicates, but keep each variable to reduce secondary errors with instantiating this generic type (assume number of variables is correct)
			for (int j = 0; j < count; j++) {
				TypeVariableBinding knownVar = typeVariableBindings[j];
				if (CharOperation.equals(knownVar.sourceName, typeParameter.name))
					problemReporter().duplicateTypeParameterInType(typeParameter);
			}
			typeVariableBindings[count++] = parameterBinding;
//				TODO should offer warnings to inform about hiding declaring, enclosing or member types				
//				ReferenceBinding type = sourceType;
//				// check that the member does not conflict with an enclosing type
//				do {
//					if (CharOperation.equals(type.sourceName, memberContext.name)) {
//						problemReporter().hidingEnclosingType(memberContext);
//						continue nextParameter;
//					}
//					type = type.enclosingType();
//				} while (type != null);
//				// check that the member type does not conflict with another sibling member type
//				for (int j = 0; j < i; j++) {
//					if (CharOperation.equals(referenceContext.memberTypes[j].name, memberContext.name)) {
//						problemReporter().duplicateNestedType(memberContext);
//						continue nextParameter;
//					}
//				}
		}
		if (count != length)
			System.arraycopy(typeVariableBindings, 0, typeVariableBindings = new TypeVariableBinding[count], 0, count);
		return typeVariableBindings;
	}

	public final ClassScope enclosingClassScope() {
		Scope scope = this;
		while ((scope = scope.parent) != null) {
			if (scope instanceof ClassScope) return (ClassScope) scope;
		}
		return null; // may answer null if no type around
	}

	public final MethodScope enclosingMethodScope() {
		Scope scope = this;
		while ((scope = scope.parent) != null) {
			if (scope instanceof MethodScope) return (MethodScope) scope;
		}
		return null; // may answer null if no method around
	}

	/* Answer the receiver's enclosing source type.
	*/
	public final SourceTypeBinding enclosingSourceType() {
		Scope scope = this;
		do {
			if (scope instanceof ClassScope)
				return ((ClassScope) scope).referenceContext.binding;
			scope = scope.parent;
		} while (scope != null);
		return null;
	}
	public final LookupEnvironment environment() {
		Scope scope, unitScope = this;
		while ((scope = unitScope.parent) != null)
			unitScope = scope;
		return ((CompilationUnitScope) unitScope).environment;
	}

	// abstract method lookup lookup (since maybe missing default abstract methods)
	public MethodBinding findDefaultAbstractMethod(
		ReferenceBinding receiverType, 
		char[] selector,
		TypeBinding[] argumentTypes,
		InvocationSite invocationSite,
		ReferenceBinding classHierarchyStart,
		MethodBinding matchingMethod,
		ObjectVector found) {

		int startFoundSize = found.size;
		ReferenceBinding currentType = classHierarchyStart;
		while (currentType != null) {
			matchingMethod = findMethodInSuperInterfaces(currentType, selector, found, matchingMethod);
			currentType = currentType.superclass();
		}
		CompilationUnitScope unitScope = compilationUnitScope();
		int foundSize = found.size;
		if (foundSize == startFoundSize) {
			if (matchingMethod != null)
				unitScope.recordTypeReferences(matchingMethod.thrownExceptions);
			return matchingMethod; // maybe null
		}
		MethodBinding[] candidates = new MethodBinding[foundSize - startFoundSize];
		int candidatesCount = 0;
		MethodBinding problemMethod = null;
		// argument type compatibility check
		for (int i = startFoundSize; i < foundSize; i++) {
			MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
			MethodBinding compatibleMethod = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);
			if (compatibleMethod != null) {
				if (compatibleMethod.isValidBinding())
					candidates[candidatesCount++] = compatibleMethod;
				else if (problemMethod == null)
					problemMethod = compatibleMethod;
			}
		}

		if (candidatesCount == 1) {
			unitScope.recordTypeReferences(candidates[0].thrownExceptions);
			return candidates[0]; 
		}
		if (candidatesCount == 0) { // try to find a close match when the parameter order is wrong or missing some parameters
			if (problemMethod != null) return problemMethod;
			int argLength = argumentTypes.length;
			nextMethod : for (int i = 0; i < foundSize; i++) {
				MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
				TypeBinding[] params = methodBinding.parameters;
				int paramLength = params.length;
				nextArg: for (int a = 0; a < argLength; a++) {
					TypeBinding arg = argumentTypes[a];
					for (int p = 0; p < paramLength; p++)
						if (params[p] == arg)
							continue nextArg;
					continue nextMethod;
				}
				return methodBinding;
			}
			return (MethodBinding) found.elementAt(0); // no good match so just use the first one found
		}
		// no need to check for visibility - interface methods are public
		boolean isCompliant14 = unitScope.environment.options.complianceLevel >= ClassFileConstants.JDK1_4;
		if (isCompliant14)
			return mostSpecificMethodBinding(candidates, candidatesCount, argumentTypes, invocationSite);
		return mostSpecificInterfaceMethodBinding(candidates, candidatesCount, invocationSite);
	}

	// Internal use only
	public ReferenceBinding findDirectMemberType(char[] typeName, ReferenceBinding enclosingType) {
		if ((enclosingType.tagBits & HasNoMemberTypes) != 0)
			return null; // know it has no member types (nor inherited member types)

		SourceTypeBinding enclosingSourceType = enclosingSourceType();
		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordReference(enclosingType, typeName);
		ReferenceBinding memberType = enclosingType.getMemberType(typeName);
		if (memberType != null) {
			unitScope.recordTypeReference(memberType);
			if (enclosingSourceType == null
				? memberType.canBeSeenBy(getCurrentPackage())
				: memberType.canBeSeenBy(enclosingType, enclosingSourceType))
					return memberType;
			return new ProblemReferenceBinding(typeName, memberType, NotVisible);
		}
		return null;
	}

	// Internal use only
	public MethodBinding findExactMethod(
		ReferenceBinding receiverType,
		char[] selector,
		TypeBinding[] argumentTypes,
		InvocationSite invocationSite) {

		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordTypeReferences(argumentTypes);
		MethodBinding exactMethod = receiverType.getExactMethod(selector, argumentTypes, unitScope);
		if (exactMethod != null) {
			unitScope.recordTypeReferences(exactMethod.thrownExceptions);
			// special treatment for Object.getClass() in 1.5 mode (substitute parameterized return type)
			if (receiverType.isInterface() || exactMethod.canBeSeenBy(receiverType, invocationSite, this)) {
				if (receiverType.id != T_JavaLangObject
					&& argumentTypes == NoParameters
				    && CharOperation.equals(selector, GETCLASS)
				    && exactMethod.returnType.isParameterizedType()/*1.5*/) {
						return ParameterizedMethodBinding.instantiateGetClass(receiverType, exactMethod, this);
			    }
				// targeting a generic method could find an exact match with variable return type
				if (exactMethod.typeVariables != NoTypeVariables || invocationSite.genericTypeArguments() != null)
					exactMethod = computeCompatibleMethod(exactMethod, argumentTypes, invocationSite);
				return exactMethod;
			}
		}
		return null;
	}

	// Internal use only
	/*	Answer the field binding that corresponds to fieldName.
		Start the lookup at the receiverType.
		InvocationSite implements
			isSuperAccess(); this is used to determine if the discovered field is visible.
		Only fields defined by the receiverType or its supertypes are answered;
		a field of an enclosing type will not be found using this API.
	
		If no visible field is discovered, null is answered.
	*/
	public FieldBinding findField(TypeBinding receiverType, char[] fieldName, InvocationSite invocationSite, boolean needResolve) {
		if (receiverType.isBaseType()) return null;

		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordTypeReference(receiverType);
		if (receiverType.isArrayType()) {
			TypeBinding leafType = receiverType.leafComponentType();
			if (leafType instanceof ReferenceBinding)
				if (!((ReferenceBinding) leafType).canBeSeenBy(this))
					return new ProblemFieldBinding((ReferenceBinding)leafType, fieldName, ReceiverTypeNotVisible);
			if (CharOperation.equals(fieldName, LENGTH))
				return ArrayBinding.ArrayLength;
			return null;
		}

		ReferenceBinding currentType = (ReferenceBinding) receiverType;
		if (!currentType.canBeSeenBy(this))
			return new ProblemFieldBinding(currentType, fieldName, ReceiverTypeNotVisible);

		FieldBinding field = currentType.getField(fieldName, true /*resolve*/);
		if (field != null) {
			if (invocationSite == null
				? field.canBeSeenBy(getCurrentPackage())
				: field.canBeSeenBy(currentType, invocationSite, this))
					return field;
			return new ProblemFieldBinding(field /* closest match*/, field.declaringClass, fieldName, NotVisible);
		}
		// collect all superinterfaces of receiverType until the field is found in a supertype
		ReferenceBinding[][] interfacesToVisit = null;
		int lastPosition = -1;
		FieldBinding visibleField = null;
		boolean keepLooking = true;
		FieldBinding notVisibleField = null;
		// we could hold onto the not visible field for extra error reporting
		while (keepLooking) {
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces == null) { // needed for statically imported types which don't know their hierarchy yet
				((SourceTypeBinding) currentType).scope.connectTypeHierarchy();
				itsInterfaces = currentType.superInterfaces();
			}
			if (itsInterfaces != NoSuperInterfaces) {
				if (interfacesToVisit == null)
					interfacesToVisit = new ReferenceBinding[5][];
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
			if ((currentType = currentType.superclass()) == null)
				break;

			unitScope.recordTypeReference(currentType);
			if ((field = currentType.getField(fieldName, needResolve)) != null) {
				keepLooking = false;
				if (field.canBeSeenBy(receiverType, invocationSite, this)) {
					if (visibleField == null)
						visibleField = field;
					else
						return new ProblemFieldBinding(visibleField /* closest match*/, visibleField.declaringClass, fieldName, Ambiguous);
				} else {
					if (notVisibleField == null)
						notVisibleField = field;
				}
			}
		}

		// walk all visible interfaces to find ambiguous references
		if (interfacesToVisit != null) {
			ProblemFieldBinding ambiguous = null;
			done : for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++) {
					ReferenceBinding anInterface = interfaces[j];
					if ((anInterface.tagBits & InterfaceVisited) == 0) {
						// if interface as not already been visited
						anInterface.tagBits |= InterfaceVisited;
						unitScope.recordTypeReference(anInterface);
						if ((field = anInterface.getField(fieldName, true /*resolve*/)) != null) {
							if (visibleField == null) {
								visibleField = field;
							} else {
								ambiguous = new ProblemFieldBinding(visibleField /* closest match*/, visibleField.declaringClass, fieldName, Ambiguous);
								break done;
							}
						} else {
							ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
							if (itsInterfaces != NoSuperInterfaces) {
								if (++lastPosition == interfacesToVisit.length)
									System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
								interfacesToVisit[lastPosition] = itsInterfaces;
							}
						}
					}
				}
			}

			// bit reinitialization
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++)
					interfaces[j].tagBits &= ~InterfaceVisited;
			}
			if (ambiguous != null)
				return ambiguous;
		}

		if (visibleField != null)
			return visibleField;
		if (notVisibleField != null)
			return new ProblemFieldBinding(notVisibleField, currentType, fieldName, NotVisible);
		return null;
	}

	// Internal use only
	public ReferenceBinding findMemberType(char[] typeName, ReferenceBinding enclosingType) {
		if ((enclosingType.tagBits & HasNoMemberTypes) != 0)
			return null; // know it has no member types (nor inherited member types)

		SourceTypeBinding enclosingSourceType = enclosingSourceType();
		PackageBinding currentPackage = getCurrentPackage();
		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordReference(enclosingType, typeName);
		ReferenceBinding memberType = enclosingType.getMemberType(typeName);
		if (memberType != null) {
			unitScope.recordTypeReference(memberType);
			if (enclosingSourceType == null
				? memberType.canBeSeenBy(currentPackage)
				: memberType.canBeSeenBy(enclosingType, enclosingSourceType))
					return memberType;
			return new ProblemReferenceBinding(typeName, memberType, NotVisible);
		}

		// collect all superinterfaces of receiverType until the memberType is found in a supertype
		ReferenceBinding currentType = enclosingType;
		ReferenceBinding[][] interfacesToVisit = null;
		int lastPosition = -1;
		ReferenceBinding visibleMemberType = null;
		boolean keepLooking = true;
		ReferenceBinding notVisible = null;
		// we could hold onto the not visible field for extra error reporting
		while (keepLooking) {
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces == null) { // needed for statically imported types which don't know their hierarchy yet
				((SourceTypeBinding) currentType).scope.connectTypeHierarchy();
				itsInterfaces = currentType.superInterfaces();
			}
			if (itsInterfaces != NoSuperInterfaces) {
				if (interfacesToVisit == null)
					interfacesToVisit = new ReferenceBinding[5][];
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
			if ((currentType = currentType.superclass()) == null)
				break;

			unitScope.recordReference(currentType, typeName);
			if ((memberType = currentType.getMemberType(typeName)) != null) {
				unitScope.recordTypeReference(memberType);
				keepLooking = false;
				if (enclosingSourceType == null
					? memberType.canBeSeenBy(currentPackage)
					: memberType.canBeSeenBy(enclosingType, enclosingSourceType)) {
						if (visibleMemberType == null)
							visibleMemberType = memberType;
						else
							return new ProblemReferenceBinding(typeName, Ambiguous);
				} else {
					notVisible = memberType;
				}
			}
		}
		// walk all visible interfaces to find ambiguous references
		if (interfacesToVisit != null) {
			ProblemReferenceBinding ambiguous = null;
			done : for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++) {
					ReferenceBinding anInterface = interfaces[j];
					if ((anInterface.tagBits & InterfaceVisited) == 0) {
						// if interface as not already been visited
						anInterface.tagBits |= InterfaceVisited;
						unitScope.recordReference(anInterface, typeName);
						if ((memberType = anInterface.getMemberType(typeName)) != null) {
							unitScope.recordTypeReference(memberType);
							if (visibleMemberType == null) {
								visibleMemberType = memberType;
							} else {
								ambiguous = new ProblemReferenceBinding(typeName, Ambiguous);
								break done;
							}
						} else {
							ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
							if (itsInterfaces != NoSuperInterfaces) {
								if (++lastPosition == interfacesToVisit.length)
									System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
								interfacesToVisit[lastPosition] = itsInterfaces;
							}
						}
					}
				}
			}

			// bit reinitialization
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++)
					interfaces[j].tagBits &= ~InterfaceVisited;
			}
			if (ambiguous != null)
				return ambiguous;
		}
		if (visibleMemberType != null)
			return visibleMemberType;
		if (notVisible != null)
			return new ProblemReferenceBinding(typeName, notVisible, NotVisible);
		return null;
	}

	// Internal use only
	public MethodBinding findMethod(
		ReferenceBinding receiverType,
		char[] selector,
		TypeBinding[] argumentTypes,
		InvocationSite invocationSite) {

		ReferenceBinding currentType = receiverType;
		MethodBinding matchingMethod = null;
		ObjectVector found = new ObjectVector(); //TODO (kent) should rewrite to remove #matchingMethod since found is allocated anyway

		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordTypeReferences(argumentTypes);

		if (currentType.isInterface()) {
			unitScope.recordTypeReference(currentType);
			MethodBinding[] currentMethods = currentType.getMethods(selector);
			int currentLength = currentMethods.length;
			if (currentLength == 1) {
				matchingMethod = currentMethods[0];
			} else if (currentLength > 1) {
				found.addAll(currentMethods);
			}
			matchingMethod = findMethodInSuperInterfaces(currentType, selector, found, matchingMethod);
			currentType = getJavaLangObject();
		}

		boolean isCompliant14 = unitScope.environment.options.complianceLevel >= ClassFileConstants.JDK1_4;
		// superclass lookup
		ReferenceBinding classHierarchyStart = currentType;
		while (currentType != null) {
			unitScope.recordTypeReference(currentType);
			MethodBinding[] currentMethods = currentType.getMethods(selector);
			int currentLength = currentMethods.length;

			if (isCompliant14 && matchingMethod != null || found.size > 0) {
				nextMethod: for (int i = 0; i < currentLength; i++) {
					MethodBinding currentMethod = currentMethods[i];
					// if 1.4 compliant, must filter out redundant protected methods from superclasses
					// protected method need to be checked only - default access is already dealt with in #canBeSeen implementation
					// when checking that p.C -> q.B -> p.A cannot see default access members from A through B.
					// if ((currentMethod.modifiers & AccProtected) == 0) continue nextMethod;
					// BUT we can also ignore any overridden method since we already know the better match (fixes 80028)
					if (matchingMethod != null) {
						if (currentMethod.areParametersEqual(matchingMethod)) {
							currentLength--;
							currentMethods[i] = null; // discard this match
							continue nextMethod;
						}
					} else {
						for (int j = 0, max = found.size; j < max; j++) {
							if (((MethodBinding) found.elementAt(j)).areParametersEqual(currentMethod)) {
								currentLength--;
								currentMethods[i] = null;
								continue nextMethod;
							}
						}
					}
				}
			}

			if (currentLength == 1 && matchingMethod == null && found.size == 0) {
				matchingMethod = currentMethods[0];
			} else if (currentLength > 0) {
				if (matchingMethod != null) {
					found.add(matchingMethod);
					matchingMethod = null;
				}
				// append currentMethods, filtering out null entries
				int maxMethod = currentMethods.length;
				if (maxMethod == currentLength) { // no method was eliminated for 1.4 compliance (see above)
					found.addAll(currentMethods);
				} else {
					for (int i = 0, max = currentMethods.length; i < max; i++) {
						MethodBinding currentMethod = currentMethods[i];
						if (currentMethod != null) found.add(currentMethod);
					}
				}
			}
			currentType = currentType.superclass();
		}

		// if found several candidates, then eliminate those not matching argument types
		int foundSize = found.size;
		MethodBinding[] candidates = null;
		int candidatesCount = 0;
		boolean checkedMatchingMethod = false; // is matchingMethod meeting argument expectation ?
		MethodBinding problemMethod = null;
		if (foundSize > 0) {
			// argument type compatibility check
			for (int i = 0; i < foundSize; i++) {
				MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
				MethodBinding compatibleMethod = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);
				if (compatibleMethod != null) {
					if (compatibleMethod.isValidBinding()) {
						switch (candidatesCount) {
							case 0: 
								matchingMethod = compatibleMethod; // if only one match, reuse matchingMethod
								checkedMatchingMethod = true; // matchingMethod is known to exist and match params here
								break;
							case 1:
								candidates = new MethodBinding[foundSize]; // only lazily created if more than one match
								candidates[0] = matchingMethod; // copy back
								matchingMethod = null;
								// fall through
							default:
								candidates[candidatesCount] = compatibleMethod;
						}
						candidatesCount++;
					} else if (problemMethod == null) {
						problemMethod = compatibleMethod;
					}
				}
			}
		}
		if (candidatesCount > 0)
			problemMethod = null; // forget the problem method if candidates were found

		// if only one matching method left (either from start or due to elimination of rivals), then match is in matchingMethod
		if (matchingMethod != null) {
			if (!checkedMatchingMethod) {
				MethodBinding compatibleMethod = computeCompatibleMethod(matchingMethod, argumentTypes, invocationSite);
				if (compatibleMethod != null) {
					if (compatibleMethod.isValidBinding()) {
						matchingMethod = compatibleMethod;
						checkedMatchingMethod = true;
					} else {
						problemMethod = compatibleMethod;
					}
				}
			}
			if (checkedMatchingMethod) {
				// (if no default abstract) must explicitly look for one instead, which could be a better match
				if (!matchingMethod.canBeSeenBy(receiverType, invocationSite, this)) {
					// ignore matching method (to be consistent with multiple matches, none visible (matching method is then null)
					MethodBinding interfaceMethod =
						findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, null, found);						
					if (interfaceMethod != null) return interfaceMethod;
					unitScope.recordTypeReferences(matchingMethod.thrownExceptions);
					return matchingMethod;
				}
			} 
			matchingMethod =
				findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
			if (matchingMethod != null) return matchingMethod;
			return problemMethod;
		}

		// no match was found, try to find a close match when the parameter order is wrong or missing some parameters
		if (candidatesCount == 0) {
			MethodBinding interfaceMethod =
				findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
			if (interfaceMethod != null) return interfaceMethod;
			if (problemMethod != null) return problemMethod;

			int argLength = argumentTypes.length;
			foundSize = found.size;
			nextMethod : for (int i = 0; i < foundSize; i++) {
				MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
				TypeBinding[] params = methodBinding.parameters;
				int paramLength = params.length;
				nextArg: for (int a = 0; a < argLength; a++) {
					TypeBinding arg = argumentTypes[a];
					for (int p = 0; p < paramLength; p++)
						if (params[p] == arg)
							continue nextArg;
					continue nextMethod;
				}
				return methodBinding;
			}
			if (found.size == 0) return null;
			return (MethodBinding) found.elementAt(0); // no good match so just use the first one found
		}

		// check for duplicate parameterized methods
		if (unitScope.environment.options.sourceLevel >= ClassFileConstants.JDK1_5) {
			for (int i = 0; i < candidatesCount; i++) {
				MethodBinding current = candidates[i];
				if (current instanceof ParameterizedGenericMethodBinding)
					current = ((ParameterizedGenericMethodBinding) current).originalMethod;
				if (current instanceof ParameterizedMethodBinding)
					for (int j = i + 1; j < candidatesCount; j++)
						if (current.declaringClass == candidates[j].declaringClass && current.areParametersEqual(candidates[j]))
							return new ProblemMethodBinding(candidates[i].selector, candidates[i].parameters, Ambiguous);
			}
		}

		// tiebreak using visibility check
		int visiblesCount = 0;
		for (int i = 0; i < candidatesCount; i++) {
			MethodBinding methodBinding = candidates[i];
			if (methodBinding.canBeSeenBy(receiverType, invocationSite, this)) {
				if (visiblesCount != i) {
					candidates[i] = null;
					candidates[visiblesCount] = methodBinding;
				}
				visiblesCount++;
			}
		}
		if (visiblesCount == 1) {
			unitScope.recordTypeReferences(candidates[0].thrownExceptions);
			return candidates[0];
		}
		if (visiblesCount == 0) {
			MethodBinding interfaceMethod =
				findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
			if (interfaceMethod != null) return interfaceMethod;
			return new ProblemMethodBinding(candidates[0], candidates[0].selector, candidates[0].parameters, NotVisible);
		}
		if (isCompliant14)
			return mostSpecificMethodBinding(candidates, visiblesCount, argumentTypes, invocationSite);
		return candidates[0].declaringClass.isClass()
			? mostSpecificClassMethodBinding(candidates, visiblesCount, invocationSite)
			: mostSpecificInterfaceMethodBinding(candidates, visiblesCount, invocationSite);
	}
	
	// Internal use only
	public MethodBinding findMethodForArray(
		ArrayBinding receiverType,
		char[] selector,
		TypeBinding[] argumentTypes,
		InvocationSite invocationSite) {

		TypeBinding leafType = receiverType.leafComponentType();
		if (leafType instanceof ReferenceBinding) {
			if (!((ReferenceBinding) leafType).canBeSeenBy(this))
				return new ProblemMethodBinding(selector, TypeConstants.NoParameters, (ReferenceBinding)leafType, ReceiverTypeNotVisible);
		}

		ReferenceBinding object = getJavaLangObject();
		MethodBinding methodBinding = object.getExactMethod(selector, argumentTypes);
		if (methodBinding != null) {
			// handle the method clone() specially... cannot be protected or throw exceptions
			if (argumentTypes == NoParameters) {
			    switch (selector[0]) {
			        case 'c': 
			            if (CharOperation.equals(selector, CLONE)) {
							return new UpdatedMethodBinding(
								environment().options.targetJDK >= ClassFileConstants.JDK1_4 ? (TypeBinding)receiverType : (TypeBinding)object, // remember its array type for codegen purpose on target>=1.4.0
								(methodBinding.modifiers & ~AccProtected) | AccPublic,
								CLONE,
								methodBinding.returnType,
								argumentTypes,
								null,
								object);
			            }
			            break;
			        case 'g': 
			            if (CharOperation.equals(selector, GETCLASS) && methodBinding.returnType.isParameterizedType()/*1.5*/) {
							return ParameterizedMethodBinding.instantiateGetClass(receiverType, methodBinding, this);
			            }
			            break;
			    }
			}
			if (methodBinding.canBeSeenBy(receiverType, invocationSite, this))
				return methodBinding;
		}
		// answers closest approximation, may not check argumentTypes or visibility
		methodBinding = findMethod(object, selector, argumentTypes, invocationSite);
		if (methodBinding == null)
			return new ProblemMethodBinding(selector, argumentTypes, NotFound);
		if (methodBinding.isValidBinding()) {
			MethodBinding compatibleMethod = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);
			if (compatibleMethod == null)
				return new ProblemMethodBinding(methodBinding, selector, argumentTypes, NotFound);
			if (!compatibleMethod.isValidBinding())
				return compatibleMethod;

			methodBinding = compatibleMethod;
			if (!methodBinding.canBeSeenBy(receiverType, invocationSite, this))
				return new ProblemMethodBinding(methodBinding, selector, methodBinding.parameters, NotVisible);
		}
		return methodBinding;
	}

	public MethodBinding findMethodInSuperInterfaces(
		ReferenceBinding currentType,
		char[] selector,
		ObjectVector found,
		MethodBinding matchingMethod) {

		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			ReferenceBinding[][] interfacesToVisit = new ReferenceBinding[5][];
			int lastPosition = -1;
			if (++lastPosition == interfacesToVisit.length)
				System.arraycopy(
					interfacesToVisit, 0,
					interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0,
					lastPosition);
			interfacesToVisit[lastPosition] = itsInterfaces;

			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++) {
					currentType = interfaces[j];
					if ((currentType.tagBits & InterfaceVisited) == 0) {
						// if interface as not already been visited
						currentType.tagBits |= InterfaceVisited;

						compilationUnitScope().recordTypeReference(currentType);
						MethodBinding[] currentMethods = currentType.getMethods(selector);
						int currentLength = currentMethods.length;
						if (currentLength == 1 && matchingMethod == null && found.size == 0) {
							matchingMethod = currentMethods[0];
						} else if (currentLength > 0) {
							if (matchingMethod != null) {
								found.add(matchingMethod);
								matchingMethod = null;
							}
							found.addAll(currentMethods);
						}
						itsInterfaces = currentType.superInterfaces();
						if (itsInterfaces != NoSuperInterfaces) {
							if (++lastPosition == interfacesToVisit.length)
								System.arraycopy(
									interfacesToVisit, 0,
									interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0,
									lastPosition);
							interfacesToVisit[lastPosition] = itsInterfaces;
						}
					}
				}
			}

			// bit reinitialization
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++)
					interfaces[j].tagBits &= ~InterfaceVisited;
			}
		}
		return matchingMethod;
	}

	// Internal use only
	public ReferenceBinding findType(
		char[] typeName,
		PackageBinding declarationPackage,
		PackageBinding invocationPackage) {

		compilationUnitScope().recordReference(declarationPackage.compoundName, typeName);
		ReferenceBinding typeBinding = declarationPackage.getType(typeName);
		if (typeBinding == null)
			return null;

		if (typeBinding.isValidBinding()) {
			if (declarationPackage != invocationPackage && !typeBinding.canBeSeenBy(invocationPackage))
				return new ProblemReferenceBinding(typeName, typeBinding, NotVisible);
		}
		return typeBinding;
	}

	public LocalVariableBinding findVariable(char[] variable) {

		return null;
	}
	
	public static TypeBinding getBaseType(char[] name) {
		// list should be optimized (with most often used first)
		int length = name.length;
		if (length > 2 && length < 8) {
			switch (name[0]) {
				case 'i' :
					if (length == 3 && name[1] == 'n' && name[2] == 't')
						return IntBinding;
					break;
				case 'v' :
					if (length == 4 && name[1] == 'o' && name[2] == 'i' && name[3] == 'd')
						return VoidBinding;
					break;
				case 'b' :
					if (length == 7
						&& name[1] == 'o'
						&& name[2] == 'o'
						&& name[3] == 'l'
						&& name[4] == 'e'
						&& name[5] == 'a'
						&& name[6] == 'n')
						return BooleanBinding;
					if (length == 4 && name[1] == 'y' && name[2] == 't' && name[3] == 'e')
						return ByteBinding;
					break;
				case 'c' :
					if (length == 4 && name[1] == 'h' && name[2] == 'a' && name[3] == 'r')
						return CharBinding;
					break;
				case 'd' :
					if (length == 6
						&& name[1] == 'o'
						&& name[2] == 'u'
						&& name[3] == 'b'
						&& name[4] == 'l'
						&& name[5] == 'e')
						return DoubleBinding;
					break;
				case 'f' :
					if (length == 5
						&& name[1] == 'l'
						&& name[2] == 'o'
						&& name[3] == 'a'
						&& name[4] == 't')
						return FloatBinding;
					break;
				case 'l' :
					if (length == 4 && name[1] == 'o' && name[2] == 'n' && name[3] == 'g')
						return LongBinding;
					break;
				case 's' :
					if (length == 5
						&& name[1] == 'h'
						&& name[2] == 'o'
						&& name[3] == 'r'
						&& name[4] == 't')
						return ShortBinding;
			}
		}
		return null;
	}

	/* API
	 *	
	 *	Answer the binding that corresponds to the argument name.
	 *	flag is a mask of the following values VARIABLE (= FIELD or LOCAL), TYPE, PACKAGE.
	 *	Only bindings corresponding to the mask can be answered.
	 *
	 *	For example, getBinding("foo", VARIABLE, site) will answer
	 *	the binding for the field or local named "foo" (or an error binding if none exists).
	 *	If a type named "foo" exists, it will not be detected (and an error binding will be answered)
	 *
	 *	The VARIABLE mask has precedence over the TYPE mask.
	 *
	 *	If the VARIABLE mask is not set, neither fields nor locals will be looked for.
	 *
	 *	InvocationSite implements:
	 *		isSuperAccess(); this is used to determine if the discovered field is visible.
	 *
	 *	Limitations: cannot request FIELD independently of LOCAL, or vice versa
	 */
	public Binding getBinding(char[] name, int mask, InvocationSite invocationSite, boolean needResolve) {

		try {
			Binding binding = null;
			FieldBinding problemField = null;
			if ((mask & Binding.VARIABLE) != 0) {
				boolean insideStaticContext = false;
				boolean insideConstructorCall = false;

				FieldBinding foundField = null;
				// can be a problem field which is answered if a valid field is not found
				ProblemFieldBinding foundInsideProblem = null;
				// inside Constructor call or inside static context
				Scope scope = this;
				int depth = 0;
				int foundDepth = 0;
				ReferenceBinding foundActualReceiverType = null;
				done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
					switch (scope.kind) {
						case METHOD_SCOPE :
							MethodScope methodScope = (MethodScope) scope;
							insideStaticContext |= methodScope.isStatic;
							insideConstructorCall |= methodScope.isConstructorCall;
							// Fall through... could duplicate the code below to save a cast - questionable optimization
						case BLOCK_SCOPE :
							LocalVariableBinding variableBinding = scope.findVariable(name);
							// looks in this scope only
							if (variableBinding != null) {
								if (foundField != null && foundField.isValidBinding())
									return new ProblemFieldBinding(
										foundField, // closest match
										foundField.declaringClass,
										name,
										InheritedNameHidesEnclosingName);
								if (depth > 0)
									invocationSite.setDepth(depth);
								return variableBinding;
							}
							break;
						case CLASS_SCOPE :
							ClassScope classScope = (ClassScope) scope;
							SourceTypeBinding enclosingType = classScope.referenceContext.binding;
							FieldBinding fieldBinding = classScope.findField(enclosingType, name, invocationSite, needResolve);
							// Use next line instead if willing to enable protected access accross inner types
							// FieldBinding fieldBinding = findField(enclosingType, name, invocationSite);
							
//							SourceTypeBinding initialType = this.enclosingSourceType();
//							if ((fieldBinding == null || !fieldBinding.isValidBinding()) && enclosingType.hasMemberTypes()) { // check member enums
//								ReferenceBinding[] memberTypes = enclosingType.memberTypes();
//								for (int i = 0, length = memberTypes.length; i < length; i++) {
//									ReferenceBinding memberType = memberTypes[i];
//									if (memberType != initialType && memberType.isEnum()) { // do not find one's field through its enclosing members
//										FieldBinding enumField = ((SourceTypeBinding)memberType).scope.findField(memberType, name, invocationSite, needResolve);
//										if (enumField != null && (enumField.modifiers & AccEnum) != 0) {
//											// grant access to enum constants of enclosing members
//											// TODO (kent) need to revisit to see whether should walk sibling enums and issue an ambiguous match
//											return enumField;
//										}
//									}
//								}
//							}
							if (fieldBinding != null) { // skip it if we did not find anything
								if (fieldBinding.problemId() == Ambiguous) {
									if (foundField == null || foundField.problemId() == NotVisible)
										// supercedes any potential InheritedNameHidesEnclosingName problem
										return fieldBinding;
									// make the user qualify the field, likely wants the first inherited field (javac generates an ambiguous error instead)
									return new ProblemFieldBinding(
										foundField, // closest match
										foundField.declaringClass,
										name,
										InheritedNameHidesEnclosingName);
								}

								ProblemFieldBinding insideProblem = null;
								if (fieldBinding.isValidBinding()) {
									if (!fieldBinding.isStatic()) {
										if (insideConstructorCall) {
											insideProblem =
												new ProblemFieldBinding(
													fieldBinding, // closest match
													fieldBinding.declaringClass,
													name,
													NonStaticReferenceInConstructorInvocation);
										} else if (insideStaticContext) {
											insideProblem =
												new ProblemFieldBinding(
													fieldBinding, // closest match
													fieldBinding.declaringClass,
													name,
													NonStaticReferenceInStaticContext);
										}
									}
									if (enclosingType == fieldBinding.declaringClass || environment().options.complianceLevel >= ClassFileConstants.JDK1_4) {
										// found a valid field in the 'immediate' scope (ie. not inherited)
										// OR in 1.4 mode (inherited shadows enclosing)
										if (foundField == null) {
											if (depth > 0){
												invocationSite.setDepth(depth);
												invocationSite.setActualReceiverType(enclosingType);
											}
											// return the fieldBinding if it is not declared in a superclass of the scope's binding (that is, inherited)
											return insideProblem == null ? fieldBinding : insideProblem;
										}
										if (foundField.isValidBinding())
											// if a valid field was found, complain when another is found in an 'immediate' enclosing type (that is, not inherited)
											if (foundField.declaringClass != fieldBinding.declaringClass)
												// ie. have we found the same field - do not trust field identity yet
												return new ProblemFieldBinding(
													foundField, // closest match
													foundField.declaringClass,
													name,
													InheritedNameHidesEnclosingName);
									}
								}

								if (foundField == null || (foundField.problemId() == NotVisible && fieldBinding.problemId() != NotVisible)) {
									// only remember the fieldBinding if its the first one found or the previous one was not visible & fieldBinding is...
									foundDepth = depth;
									foundActualReceiverType = enclosingType;
									foundInsideProblem = insideProblem;
									foundField = fieldBinding;
								}
							}
							depth++;
							insideStaticContext |= enclosingType.isStatic();
							// 1EX5I8Z - accessing outer fields within a constructor call is permitted
							// in order to do so, we change the flag as we exit from the type, not the method
							// itself, because the class scope is used to retrieve the fields.
							MethodScope enclosingMethodScope = scope.methodScope();
							insideConstructorCall =
								enclosingMethodScope == null ? false : enclosingMethodScope.isConstructorCall;
							break;
						case COMPILATION_UNIT_SCOPE :
							break done;
					}
					scope = scope.parent;
				}

				if (foundInsideProblem != null)
					return foundInsideProblem;
				if (foundField != null) {
					if (foundField.isValidBinding()) {
						if (foundDepth > 0) {
							invocationSite.setDepth(foundDepth);
							invocationSite.setActualReceiverType(foundActualReceiverType);
						}
						return foundField;
					}
					problemField = foundField;
				}

				if (environment().options.sourceLevel >= ClassFileConstants.JDK1_5) {
					// at this point the scope is a compilation unit scope & need to check for imported static fields
					CompilationUnitScope unitScope = (CompilationUnitScope) scope;
					ImportBinding[] imports = unitScope.imports;
					if (imports != null) {
						// check single static imports
						for (int i = 0, length = imports.length; i < length; i++) {
							ImportBinding importBinding = imports[i];
							if (importBinding.isStatic() && !importBinding.onDemand) {
								if (CharOperation.equals(importBinding.compoundName[importBinding.compoundName.length - 1], name)) {
									if (unitScope.resolveSingleImport(importBinding) != null && importBinding.resolvedImport instanceof FieldBinding) {
										ReferenceBinding declaringClass = ((FieldBinding) importBinding.resolvedImport).declaringClass;
										if (declaringClass.canBeSeenBy(this)) {
											ImportReference importReference = importBinding.reference;
											if (importReference != null) importReference.used = true;
											return importBinding.resolvedImport;
										}
										problemField = new ProblemFieldBinding(declaringClass, name, ReceiverTypeNotVisible);
									}
								}
							}
						}
						// check on demand imports
						boolean foundInImport = false;
						for (int i = 0, length = imports.length; i < length; i++) {
							ImportBinding importBinding = imports[i];
							if (importBinding.isStatic() && importBinding.onDemand) {
								Binding resolvedImport = importBinding.resolvedImport;
								if (resolvedImport instanceof ReferenceBinding) {
									FieldBinding temp = findField((ReferenceBinding) resolvedImport, name, invocationSite, needResolve);
									if (temp != null) {
										if (!temp.isValidBinding()) {
											problemField = temp;
										} else if (temp.isStatic()) {
											ImportReference importReference = importBinding.reference;
											if (importReference != null) importReference.used = true;
											if (foundInImport)
												// Answer error binding -- import on demand conflict; name found in two import on demand packages.
												return new ProblemReferenceBinding(name, Ambiguous);
											foundField = temp;
											foundInImport = true;
										}
									}
								}
							}
						}
						if (foundField != null) return foundField;
					}
				}
			}

			// We did not find a local or instance variable.
			if ((mask & Binding.TYPE) != 0) {
				if ((binding = getBaseType(name)) != null)
					return binding;
				binding = getTypeOrPackage(name, (mask & Binding.PACKAGE) == 0 ? Binding.TYPE : Binding.TYPE | Binding.PACKAGE);
				if (binding.isValidBinding() || mask == Binding.TYPE)
					return binding;
				// answer the problem type binding if we are only looking for a type
			} else if ((mask & Binding.PACKAGE) != 0) {
				compilationUnitScope().recordSimpleReference(name);
				if ((binding = environment().getTopLevelPackage(name)) != null)
					return binding;
			}
			if (problemField != null) return problemField;
			return new ProblemBinding(name, enclosingSourceType(), NotFound);
		} catch (AbortCompilation e) {
			e.updateContext(invocationSite, referenceCompilationUnit().compilationResult);
			throw e;
		}
	}

	public MethodBinding getConstructor(ReferenceBinding receiverType, TypeBinding[] argumentTypes, InvocationSite invocationSite) {
		try {
			CompilationUnitScope unitScope = compilationUnitScope();
			unitScope.recordTypeReference(receiverType);
			unitScope.recordTypeReferences(argumentTypes);
			MethodBinding methodBinding = receiverType.getExactConstructor(argumentTypes);
			if (methodBinding != null && methodBinding.canBeSeenBy(invocationSite, this)) {
			    // targeting a non generic constructor with type arguments ?
			    if (invocationSite.genericTypeArguments() != null)
			    	methodBinding = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);				
				return methodBinding;
			}
			MethodBinding[] methods = receiverType.getMethods(TypeConstants.INIT);
			if (methods == NoMethods)
				return new ProblemMethodBinding(
					TypeConstants.INIT,
					argumentTypes,
					NotFound);

			MethodBinding[] compatible = new MethodBinding[methods.length];
			int compatibleIndex = 0;
			MethodBinding problemMethod = null;
			for (int i = 0, length = methods.length; i < length; i++) {
				MethodBinding compatibleMethod = computeCompatibleMethod(methods[i], argumentTypes, invocationSite);
				if (compatibleMethod != null) {
					if (compatibleMethod.isValidBinding())
						compatible[compatibleIndex++] = compatibleMethod;
					else if (problemMethod == null)
						problemMethod = compatibleMethod;
				}
			}
			if (compatibleIndex == 0) {
				if (problemMethod == null)
					return new ProblemMethodBinding(TypeConstants.INIT, argumentTypes, NotFound);
				return problemMethod;
			}
			// need a more descriptive error... cannot convert from X to Y
	
			MethodBinding[] visible = new MethodBinding[compatibleIndex];
			int visibleIndex = 0;
			for (int i = 0; i < compatibleIndex; i++) {
				MethodBinding method = compatible[i];
				if (method.canBeSeenBy(invocationSite, this))
					visible[visibleIndex++] = method;
			}
			if (visibleIndex == 1) return visible[0];
			if (visibleIndex == 0)
				return new ProblemMethodBinding(
					compatible[0],
					TypeConstants.INIT,
					compatible[0].parameters,
					NotVisible);
			// all of visible are from the same declaringClass, even before 1.4 we can call this method instead of mostSpecificClassMethodBinding
			return mostSpecificMethodBinding(visible, visibleIndex, argumentTypes, invocationSite);
		} catch (AbortCompilation e) {
			e.updateContext(invocationSite, referenceCompilationUnit().compilationResult);
			throw e;
		}
	}

	public final PackageBinding getCurrentPackage() {
		Scope scope, unitScope = this;
		while ((scope = unitScope.parent) != null)
			unitScope = scope;
		return ((CompilationUnitScope) unitScope).fPackage;
	}

	/**
	 * Returns the modifiers of the innermost enclosing declaration.
	 * @return modifiers
	 */
	public int getDeclarationModifiers(){
		switch(this.kind){
			case Scope.BLOCK_SCOPE :
			case Scope.METHOD_SCOPE :
				MethodScope methodScope = methodScope();
				if (!methodScope.isInsideInitializer()){
					// check method modifiers to see if deprecated
					MethodBinding context = ((AbstractMethodDeclaration)methodScope.referenceContext).binding;
					if (context != null)
						return context.modifiers;
				} else {
					SourceTypeBinding type = ((BlockScope) this).referenceType().binding;

					// inside field declaration ? check field modifier to see if deprecated
					if (methodScope.initializedField != null)
						return methodScope.initializedField.modifiers;
					if (type != null)
						return type.modifiers;
				}
				break;
			case Scope.CLASS_SCOPE :
				ReferenceBinding context = ((ClassScope)this).referenceType().binding;
				if (context != null)
					return context.modifiers;
				break;
		}
		return -1;
	}

	public FieldBinding getField(TypeBinding receiverType, char[] fieldName, InvocationSite invocationSite) {
		try {
			FieldBinding field = findField(receiverType, fieldName, invocationSite, true /*resolve*/);
			if (field != null) return field;
	
			return new ProblemFieldBinding(
				receiverType instanceof ReferenceBinding ? (ReferenceBinding) receiverType : null,
				fieldName,
				NotFound);
		} catch (AbortCompilation e) {
			e.updateContext(invocationSite, referenceCompilationUnit().compilationResult);
			throw e;
		}			
	}

	/* API
	 *	
	 *	Answer the method binding that corresponds to selector, argumentTypes.
	 *	Start the lookup at the enclosing type of the receiver.
	 *	InvocationSite implements 
	 *		isSuperAccess(); this is used to determine if the discovered method is visible.
	 *		setDepth(int); this is used to record the depth of the discovered method
	 *			relative to the enclosing type of the receiver. (If the method is defined
	 *			in the enclosing type of the receiver, the depth is 0; in the next enclosing
	 *			type, the depth is 1; and so on
	 * 
	 *	If no visible method is discovered, an error binding is answered.
	 */
	public MethodBinding getImplicitMethod(char[] selector, TypeBinding[] argumentTypes, InvocationSite invocationSite) {

		boolean insideStaticContext = false;
		boolean insideConstructorCall = false;
		MethodBinding foundMethod = null;
		MethodBinding foundFuzzyProblem = null;
		// the weird method lookup case (matches method name in scope, then arg types, then visibility)
		MethodBinding foundInsideProblem = null;
		// inside Constructor call or inside static context
		Scope scope = this;
		int depth = 0;
		done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
			switch (scope.kind) {
				case METHOD_SCOPE :
					MethodScope methodScope = (MethodScope) scope;
					insideStaticContext |= methodScope.isStatic;
					insideConstructorCall |= methodScope.isConstructorCall;
					break;
				case CLASS_SCOPE :
					ClassScope classScope = (ClassScope) scope;
					SourceTypeBinding receiverType = classScope.referenceContext.binding;
					boolean isExactMatch = true;
					// retrieve an exact visible match (if possible)
					// compilationUnitScope().recordTypeReference(receiverType);   not needed since receiver is the source type
					MethodBinding methodBinding =
						(foundMethod == null)
							? classScope.findExactMethod(receiverType, selector, argumentTypes, invocationSite)
							: classScope.findExactMethod(receiverType, foundMethod.selector, foundMethod.parameters, invocationSite);
					if (methodBinding == null) {
						// answers closest approximation, may not check argumentTypes or visibility
						isExactMatch = false;
						methodBinding = classScope.findMethod(receiverType, selector, argumentTypes, invocationSite);
						// methodBinding = findMethod(receiverType, selector, argumentTypes, invocationSite);
					}
					if (methodBinding != null) { // skip it if we did not find anything
						if (methodBinding.problemId() == Ambiguous) {
							if (foundMethod == null || foundMethod.problemId() == NotVisible) {
								// supercedes any potential InheritedNameHidesEnclosingName problem
								return methodBinding;
							}
							// make the user qualify the method, likely wants the first inherited method (javac generates an ambiguous error instead)
							return new ProblemMethodBinding(
								methodBinding, // closest match
								selector,
								argumentTypes,
								InheritedNameHidesEnclosingName);
						}
						MethodBinding fuzzyProblem = null;
						MethodBinding insideProblem = null;
						if (methodBinding.isValidBinding()) {
							if (!isExactMatch) {
								MethodBinding compatibleMethod = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);
								if (compatibleMethod == null) {
									if (foundMethod == null || foundMethod.problemId() == NotVisible)
										// inherited mismatch is reported directly, not looking at enclosing matches
										return new ProblemMethodBinding(methodBinding, selector, argumentTypes, NotFound);
									// make the user qualify the method, likely wants the first inherited method (javac generates an ambiguous error instead)
									fuzzyProblem = new ProblemMethodBinding(methodBinding, selector, methodBinding.parameters, InheritedNameHidesEnclosingName);
								} else if (!compatibleMethod.isValidBinding()) {
									fuzzyProblem = compatibleMethod;
								} else {
									methodBinding = compatibleMethod;
									if (!methodBinding.canBeSeenBy(receiverType, invocationSite, classScope)) {
										// using <classScope> instead of <this> for visibility check does grant all access to innerclass
										fuzzyProblem = new ProblemMethodBinding(methodBinding, selector, methodBinding.parameters, NotVisible);
									}
								}
							}
							if (fuzzyProblem == null && !methodBinding.isStatic()) {
								if (insideConstructorCall) {
									insideProblem =
										new ProblemMethodBinding(
											methodBinding, // closest match
											methodBinding.selector,
											methodBinding.parameters,
											NonStaticReferenceInConstructorInvocation);
								} else if (insideStaticContext) {
									insideProblem =
										new ProblemMethodBinding(
											methodBinding, // closest match
											methodBinding.selector,
											methodBinding.parameters,
											NonStaticReferenceInStaticContext);
								}
							}

							if (receiverType == methodBinding.declaringClass
 (receiverType.getMethods(selector)) != NoMethods
 ((fuzzyProblem == null || fuzzyProblem.problemId() != NotVisible) && environment().options.complianceLevel >= ClassFileConstants.JDK1_4)) {
								// found a valid method in the 'immediate' scope (ie. not inherited)
								// OR the receiverType implemented a method with the correct name
								// OR in 1.4 mode (inherited visible shadows enclosing)
								if (foundMethod == null) {
									if (depth > 0){
										invocationSite.setDepth(depth);
										invocationSite.setActualReceiverType(receiverType);
									}
									// return the methodBinding if it is not declared in a superclass of the scope's binding (that is, inherited)
									if (fuzzyProblem != null)
										return fuzzyProblem;
									if (insideProblem != null)
										return insideProblem;
									return methodBinding;
								}
								// if a method was found, complain when another is found in an 'immediate' enclosing type (that is, not inherited)
								// NOTE: Unlike fields, a non visible method hides a visible method
								if (foundMethod.declaringClass != methodBinding.declaringClass)
									// ie. have we found the same method - do not trust field identity yet
									return new ProblemMethodBinding(
										methodBinding, // closest match
										methodBinding.selector,
										methodBinding.parameters,
										InheritedNameHidesEnclosingName);
							}
						}

						if (foundMethod == null || (foundMethod.problemId() == NotVisible && methodBinding.problemId() != NotVisible)) {
							// only remember the methodBinding if its the first one found or the previous one was not visible & methodBinding is...
							// remember that private methods are visible if defined directly by an enclosing class
							if (depth > 0) {
								invocationSite.setDepth(depth);
								invocationSite.setActualReceiverType(receiverType);
							}
							foundFuzzyProblem = fuzzyProblem;
							foundInsideProblem = insideProblem;
							if (fuzzyProblem == null)
								foundMethod = methodBinding; // only keep it if no error was found
						}
					}
					depth++;
					insideStaticContext |= receiverType.isStatic();
					// 1EX5I8Z - accessing outer fields within a constructor call is permitted
					// in order to do so, we change the flag as we exit from the type, not the method
					// itself, because the class scope is used to retrieve the fields.
					MethodScope enclosingMethodScope = scope.methodScope();
					insideConstructorCall =
						enclosingMethodScope == null ? false : enclosingMethodScope.isConstructorCall;
					break;
				case COMPILATION_UNIT_SCOPE :
					break done;
			}
			scope = scope.parent;
		}

		if (foundFuzzyProblem != null)
			return foundFuzzyProblem;
		if (foundInsideProblem != null)
			return foundInsideProblem;
		if (foundMethod != null)
			return foundMethod;

		if (insideStaticContext && environment().options.sourceLevel >= ClassFileConstants.JDK1_5) {
			// at this point the scope is a compilation unit scope & need to check for imported static methods
			CompilationUnitScope unitScope = (CompilationUnitScope) scope;
			ImportBinding[] imports = unitScope.imports;
			if (imports != null) {
				int importLevel = -1; // -1 = not found, 0 = on demand match, 1 = single import match
				for (int i = 0, length = imports.length; i < length; i++) {
					ImportBinding importBinding = imports[i];
					if (importBinding.isStatic()) {
						Binding resolvedImport = importBinding.resolvedImport;
						MethodBinding possible = null;
						if (!importBinding.onDemand && importBinding.isStatic()) {
							if (resolvedImport instanceof MethodBinding) {
								MethodBinding staticMethod = (MethodBinding) resolvedImport;
								if (CharOperation.equals(staticMethod.selector, selector))
									// answers closest approximation, may not check argumentTypes or visibility
									possible = findMethod(staticMethod.declaringClass, selector, argumentTypes, invocationSite);
							} else if (resolvedImport instanceof FieldBinding) {
								// check to see if there are also methods with the same name
								FieldBinding staticField = (FieldBinding) resolvedImport;
								if (CharOperation.equals(staticField.name, selector)) {
									// must find the importRef's type again since the field can be from an inherited type
									char[][] importName = importBinding.reference.tokens;
									TypeBinding referencedType = getType(importName, importName.length - 1);
									if (referencedType != null)
										// answers closest approximation, may not check argumentTypes or visibility
										possible = findMethod((ReferenceBinding) referencedType, selector, argumentTypes, invocationSite);
								}
							}
						} else if (importBinding.onDemand && importLevel < 1 && resolvedImport instanceof ReferenceBinding) {
							// answers closest approximation, may not check argumentTypes or visibility
							possible = findMethod((ReferenceBinding) resolvedImport, selector, argumentTypes, invocationSite);
						}
						if (possible != null) {
							if (!possible.isValidBinding()) {
								if (foundMethod == null)
									foundMethod = possible; // answer as error case match
							} else if (possible.isStatic()) {
								MethodBinding compatibleMethod = computeCompatibleMethod(possible, argumentTypes, invocationSite);
								if (compatibleMethod != null) {
									if (compatibleMethod.isValidBinding()) {
										if (compatibleMethod.canBeSeenBy(unitScope.fPackage)) {
											ImportReference importReference = importBinding.reference;
											if (importReference != null) importReference.used = true;
											int matchingImportLevel = importBinding.onDemand ? 0 : 1;
											if (matchingImportLevel == importLevel) {
												scope = this;
												while (true) {
													switch (scope.kind) {
														case CLASS_SCOPE :
															return new ProblemMethodBinding(selector, argumentTypes, ((ClassScope) scope).referenceContext.binding, Ambiguous);
														case COMPILATION_UNIT_SCOPE :
															return new ProblemMethodBinding(compatibleMethod, selector, compatibleMethod.parameters, Ambiguous);
													}
													scope = scope.parent;
												}
											}
											foundMethod = compatibleMethod;
											importLevel = matchingImportLevel;
										} else if (foundMethod == null) {
											foundMethod = new ProblemMethodBinding(compatibleMethod, selector, compatibleMethod.parameters, NotVisible);
										}
									}
								} else if (foundMethod == null) {
									foundMethod = new ProblemMethodBinding(possible, selector, argumentTypes, NotFound);
								}
							}
						}
					}
				}
			}
			if (foundMethod != null)
				return foundMethod;
		}
		return new ProblemMethodBinding(selector, argumentTypes, NotFound);
	}

	public final ReferenceBinding getJavaIoSerializable() {
		compilationUnitScope().recordQualifiedReference(JAVA_IO_SERIALIZABLE);
		ReferenceBinding type = environment().getType(JAVA_IO_SERIALIZABLE);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_IO_SERIALIZABLE, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	
	public final ReferenceBinding getJavaLangAnnotationAnnotation() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ANNOTATION_ANNOTATION);
		ReferenceBinding type = environment().getType(JAVA_LANG_ANNOTATION_ANNOTATION);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_ANNOTATION_ANNOTATION, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	
	public final ReferenceBinding getJavaLangAssertionError() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ASSERTIONERROR);
		ReferenceBinding type = environment().getType(JAVA_LANG_ASSERTIONERROR);
		if (type != null) return type;
		problemReporter().isClassPathCorrect(JAVA_LANG_ASSERTIONERROR, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangClass() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_CLASS);
		ReferenceBinding type = environment().getType(JAVA_LANG_CLASS);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_CLASS, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangCloneable() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_CLONEABLE);
		ReferenceBinding type = environment().getType(JAVA_LANG_CLONEABLE);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_CLONEABLE, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	public final ReferenceBinding getJavaLangEnum() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ENUM);
		ReferenceBinding type = environment().getType(JAVA_LANG_ENUM);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_ENUM, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	public final ReferenceBinding getJavaLangError() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ERROR);
		ReferenceBinding type = environment().getType(JAVA_LANG_ERROR);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_ERROR, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	public final ReferenceBinding getJavaLangIterable() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ITERABLE);
		ReferenceBinding type = environment().getType(JAVA_LANG_ITERABLE);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_ITERABLE, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	public final ReferenceBinding getJavaLangObject() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_OBJECT);
		ReferenceBinding type = environment().getType(JAVA_LANG_OBJECT);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_OBJECT, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangRuntimeException() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_RUNTIMEEXCEPTION);
		ReferenceBinding type = environment().getType(JAVA_LANG_RUNTIMEEXCEPTION);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_RUNTIMEEXCEPTION, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangString() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_STRING);
		ReferenceBinding type = environment().getType(JAVA_LANG_STRING);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_STRING, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangThrowable() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_THROWABLE);
		ReferenceBinding type = environment().getType(JAVA_LANG_THROWABLE);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_THROWABLE, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}
	public final ReferenceBinding getJavaUtilIterator() {
		compilationUnitScope().recordQualifiedReference(JAVA_UTIL_ITERATOR);
		ReferenceBinding type = environment().getType(JAVA_UTIL_ITERATOR);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_UTIL_ITERATOR, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	/* Answer the type binding corresponding to the typeName argument, relative to the enclosingType.
	*/
	public final ReferenceBinding getMemberType(char[] typeName, ReferenceBinding enclosingType) {
		ReferenceBinding memberType = findMemberType(typeName, enclosingType);
		if (memberType != null) return memberType;
		return new ProblemReferenceBinding(typeName, NotFound);
	}

	public MethodBinding getMethod(TypeBinding receiverType, char[] selector, TypeBinding[] argumentTypes, InvocationSite invocationSite) {
		try {
			if (receiverType.isBaseType())
				return new ProblemMethodBinding(selector, argumentTypes, NotFound);

			compilationUnitScope().recordTypeReference(receiverType);
			if (receiverType.isArrayType())
				return findMethodForArray((ArrayBinding) receiverType, selector, argumentTypes, invocationSite);

			ReferenceBinding currentType = (ReferenceBinding) receiverType;
			if (!currentType.canBeSeenBy(this))
				return new ProblemMethodBinding(selector, argumentTypes, ReceiverTypeNotVisible);
	
			// retrieve an exact visible match (if possible)
			MethodBinding methodBinding = findExactMethod(currentType, selector, argumentTypes, invocationSite);
			if (methodBinding != null) return methodBinding;
	
			// TODO (kent) performance - we are accumulating super methods which are *hidden* during the walk (see testcase from bug 69141)
			// answers closest approximation, may not check argumentTypes or visibility
			methodBinding = findMethod(currentType, selector, argumentTypes, invocationSite);
			if (methodBinding == null)
				return new ProblemMethodBinding(selector, argumentTypes, NotFound);
			if (methodBinding.isValidBinding()) {
				MethodBinding compatibleMethod = computeCompatibleMethod(methodBinding, argumentTypes, invocationSite);
				if (compatibleMethod == null)
					return new ProblemMethodBinding(methodBinding, selector, argumentTypes, NotFound);
				if (!compatibleMethod.isValidBinding())
					return compatibleMethod;
	
				methodBinding = compatibleMethod;
				if (!methodBinding.canBeSeenBy(currentType, invocationSite, this))
					return new ProblemMethodBinding( methodBinding, selector, methodBinding.parameters, NotVisible);
			}
			// special treatment for Object.getClass() in 1.5 mode (substitute parameterized return type)
			if (receiverType.id != T_JavaLangObject
				&& argumentTypes == NoParameters
			    && CharOperation.equals(selector, GETCLASS)
			    && methodBinding.returnType.isParameterizedType()/*1.5*/) {
					return ParameterizedMethodBinding.instantiateGetClass(receiverType, methodBinding, this);
		    }			
			return methodBinding;
		} catch (AbortCompilation e) {
			e.updateContext(invocationSite, referenceCompilationUnit().compilationResult);
			throw e;
		}
	}

	/* Answer the package from the compoundName or null if it begins with a type.
	* Intended to be used while resolving a qualified type name.
	*
	* NOTE: If a problem binding is returned, senders should extract the compound name
	* from the binding & not assume the problem applies to the entire compoundName.
	*/
	public final Binding getPackage(char[][] compoundName) {
		compilationUnitScope().recordQualifiedReference(compoundName);
		Binding binding = getTypeOrPackage(compoundName[0], Binding.TYPE | Binding.PACKAGE);
		if (binding == null)
			return new ProblemReferenceBinding(compoundName[0], NotFound);
		if (!binding.isValidBinding())
			return (ReferenceBinding) binding;

		if (!(binding instanceof PackageBinding)) return null; // compoundName does not start with a package

		int currentIndex = 1;
		PackageBinding packageBinding = (PackageBinding) binding;
		while (currentIndex < compoundName.length) {
			binding = packageBinding.getTypeOrPackage(compoundName[currentIndex++]);
			if (binding == null)
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					NotFound);
			if (!binding.isValidBinding())
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					binding.problemId());
			if (!(binding instanceof PackageBinding))
				return packageBinding;
			packageBinding = (PackageBinding) binding;
		}
		return new ProblemReferenceBinding(compoundName, NotFound);
	}

	/* Answer the type binding that corresponds the given name, starting the lookup in the receiver.
	* The name provided is a simple source name (e.g., "Object" , "Point", ...)
	*/
	// The return type of this method could be ReferenceBinding if we did not answer base types.
	// NOTE: We could support looking for Base Types last in the search, however any code using
	// this feature would be extraordinarily slow.  Therefore we don't do this
	public final TypeBinding getType(char[] name) {
		// Would like to remove this test and require senders to specially handle base types
		TypeBinding binding = getBaseType(name);
		if (binding != null) return binding;
		return (ReferenceBinding) getTypeOrPackage(name, Binding.TYPE);
	}

	/* Answer the type binding that corresponds to the given name, starting the lookup in the receiver
	* or the packageBinding if provided.
	* The name provided is a simple source name (e.g., "Object" , "Point", ...)
	*/
	public final TypeBinding getType(char[] name, PackageBinding packageBinding) {
		if (packageBinding == null)
			return getType(name);

		Binding binding = packageBinding.getTypeOrPackage(name);
		if (binding == null)
			return new ProblemReferenceBinding(
				CharOperation.arrayConcat(packageBinding.compoundName, name),
				NotFound);
		if (!binding.isValidBinding())
			return new ProblemReferenceBinding(
				CharOperation.arrayConcat(packageBinding.compoundName, name),
				binding.problemId());

		ReferenceBinding typeBinding = (ReferenceBinding) binding;
		if (!typeBinding.canBeSeenBy(this))
			return new ProblemReferenceBinding(
				CharOperation.arrayConcat(packageBinding.compoundName, name),
				typeBinding,
				NotVisible);
		return typeBinding;
	}

	/* Answer the type binding corresponding to the compoundName.
	*
	* NOTE: If a problem binding is returned, senders should extract the compound name
	* from the binding & not assume the problem applies to the entire compoundName.
	*/
	public final TypeBinding getType(char[][] compoundName, int typeNameLength) {
		if (typeNameLength == 1) {
			// Would like to remove this test and require senders to specially handle base types
			TypeBinding binding = getBaseType(compoundName[0]);
			if (binding != null) return binding;
		}

		CompilationUnitScope unitScope = compilationUnitScope();
		unitScope.recordQualifiedReference(compoundName);
		Binding binding =
			getTypeOrPackage(compoundName[0], typeNameLength == 1 ? Binding.TYPE : Binding.TYPE | Binding.PACKAGE);
		if (binding == null)
			return new ProblemReferenceBinding(compoundName[0], NotFound);
		if (!binding.isValidBinding())
			return (ReferenceBinding) binding;

		int currentIndex = 1;
		boolean checkVisibility = false;
		if (binding instanceof PackageBinding) {
			PackageBinding packageBinding = (PackageBinding) binding;
			while (currentIndex < typeNameLength) {
				binding = packageBinding.getTypeOrPackage(compoundName[currentIndex++]); // does not check visibility
				if (binding == null)
					return new ProblemReferenceBinding(
						CharOperation.subarray(compoundName, 0, currentIndex),
						NotFound);
				if (!binding.isValidBinding())
					return new ProblemReferenceBinding(
						CharOperation.subarray(compoundName, 0, currentIndex),
						binding.problemId());
				if (!(binding instanceof PackageBinding))
					break;
				packageBinding = (PackageBinding) binding;
			}
			if (binding instanceof PackageBinding)
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					NotFound);
			checkVisibility = true;
		}

		// binding is now a ReferenceBinding
		ReferenceBinding typeBinding = (ReferenceBinding) binding;
		unitScope.recordTypeReference(typeBinding);
		if (checkVisibility) // handles the fall through case
			if (!typeBinding.canBeSeenBy(this))
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding,
					NotVisible);

		while (currentIndex < typeNameLength) {
			typeBinding = getMemberType(compoundName[currentIndex++], typeBinding);
			if (!typeBinding.isValidBinding()) {
				if (typeBinding instanceof ProblemReferenceBinding) {
					ProblemReferenceBinding problemBinding = (ProblemReferenceBinding) typeBinding;
					return new ProblemReferenceBinding(
						CharOperation.subarray(compoundName, 0, currentIndex),
						problemBinding.original,
						typeBinding.problemId());
				}
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding.problemId());
			}
		}
		return typeBinding;
	}

	/* Internal use only 
	*/
	final Binding getTypeOrPackage(char[] name, int mask) {
		Scope scope = this;
		ReferenceBinding foundType = null;
		boolean insideStaticContext = false;
		if ((mask & Binding.TYPE) == 0) {
			Scope next = scope;
			while ((next = scope.parent) != null)
				scope = next;
		} else {
			done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
				switch (scope.kind) {
					case METHOD_SCOPE :
						MethodScope methodScope = (MethodScope) scope;
						AbstractMethodDeclaration methodDecl = methodScope.referenceMethod();
						if (methodDecl != null && methodDecl.binding != null) {
							TypeVariableBinding typeVariable = methodDecl.binding.getTypeVariable(name);
							if (typeVariable != null)	return typeVariable;
						}
						insideStaticContext |= methodScope.isStatic;
					case BLOCK_SCOPE :
						ReferenceBinding localType = ((BlockScope) scope).findLocalType(name); // looks in this scope only
						if (localType != null) {
							if (foundType != null && foundType != localType)
								return new ProblemReferenceBinding(name, InheritedNameHidesEnclosingName);
							return localType;
						}
						break;
					case CLASS_SCOPE :
						SourceTypeBinding sourceType = ((ClassScope) scope).referenceContext.binding;
						if (sourceType.isHierarchyBeingConnected()) {
							// type variables take precedence over the source type, ex. class X <X> extends X == class X <Y> extends Y 
							TypeVariableBinding typeVariable = sourceType.getTypeVariable(name);
							if (typeVariable != null)
								return typeVariable;
							if (CharOperation.equals(name, sourceType.sourceName))
								return sourceType;
							break;
						}
						// type variables take precedence over member types
						TypeVariableBinding typeVariable = sourceType.getTypeVariable(name);
						if (typeVariable != null) {
							if (insideStaticContext) // do not consider this type modifiers: access is legite within same type
								return new ProblemReferenceBinding(name, NonStaticReferenceInStaticContext);
							return typeVariable;
						}
						insideStaticContext |= (sourceType.modifiers & AccStatic) != 0; // not isStatic()
						// 6.5.5.1 - member types have precedence over top-level type in same unit
						ReferenceBinding memberType = findMemberType(name, sourceType);
						if (memberType != null) { // skip it if we did not find anything
							if (memberType.problemId() == Ambiguous) {
								if (foundType == null || foundType.problemId() == NotVisible)
									// supercedes any potential InheritedNameHidesEnclosingName problem
									return memberType;
								// make the user qualify the type, likely wants the first inherited type
								return new ProblemReferenceBinding(name, InheritedNameHidesEnclosingName);
							}
							if (memberType.isValidBinding()) {
								if (sourceType == memberType.enclosingType()
 environment().options.complianceLevel >= ClassFileConstants.JDK1_4) {
									// found a valid type in the 'immediate' scope (ie. not inherited)
									// OR in 1.4 mode (inherited shadows enclosing)
									if (foundType == null)
										return memberType; 
									// if a valid type was found, complain when another is found in an 'immediate' enclosing type (ie. not inherited)
									if (foundType.isValidBinding() && foundType != memberType)
										return new ProblemReferenceBinding(name, InheritedNameHidesEnclosingName);
								}
							}
							if (foundType == null || (foundType.problemId() == NotVisible && memberType.problemId() != NotVisible))
								// only remember the memberType if its the first one found or the previous one was not visible & memberType is...
								foundType = memberType;
						}
						if (CharOperation.equals(sourceType.sourceName, name)) {
							if (foundType != null && foundType != sourceType && foundType.problemId() != NotVisible)
								return new ProblemReferenceBinding(name, InheritedNameHidesEnclosingName);
							return sourceType;
						}
						break;
					case COMPILATION_UNIT_SCOPE :
						break done;
				}
				scope = scope.parent;
			}
			if (foundType != null && foundType.problemId() != NotVisible)
				return foundType;
		}

		// at this point the scope is a compilation unit scope
		CompilationUnitScope unitScope = (CompilationUnitScope) scope;
		PackageBinding currentPackage = unitScope.fPackage; 
		// ask for the imports + name
		if ((mask & Binding.TYPE) != 0) {
			// check single type imports.

			ImportBinding[] imports = unitScope.imports;
			if (imports != null) {
				HashtableOfObject typeImports = unitScope.resolvedSingeTypeImports;
				if (typeImports != null) {
					ImportBinding typeImport = (ImportBinding) typeImports.get(name);
					if (typeImport != null) {
						ImportReference importReference = typeImport.reference;
						if (importReference != null) importReference.used = true;
						return typeImport.resolvedImport; // already know its visible
					}
				} else {
					// walk all the imports since resolvedSingeTypeImports is not yet initialized
					for (int i = 0, length = imports.length; i < length; i++) {
						ImportBinding typeImport = imports[i];
						if (!typeImport.onDemand) {
							if (CharOperation.equals(typeImport.compoundName[typeImport.compoundName.length - 1], name)) {
								if (unitScope.resolveSingleImport(typeImport) != null) {
									ImportReference importReference = typeImport.reference;
									if (importReference != null)
										importReference.used = true;
									return typeImport.resolvedImport; // already know its visible
								}
							}
						}
					}
				}
			}
			// check if the name is in the current package, skip it if its a sub-package
			unitScope.recordReference(currentPackage.compoundName, name);
			Binding binding = currentPackage.getTypeOrPackage(name);
			if (binding instanceof ReferenceBinding) return binding; // type is always visible to its own package

			// check on demand imports
			if (imports != null) {
				boolean foundInImport = false;
				ReferenceBinding type = null;
				for (int i = 0, length = imports.length; i < length; i++) {
					ImportBinding someImport = imports[i];
					if (someImport.onDemand) {
						Binding resolvedImport = someImport.resolvedImport;
						ReferenceBinding temp = resolvedImport instanceof PackageBinding
							? findType(name, (PackageBinding) resolvedImport, currentPackage)
							: (someImport.isStatic()
								? findMemberType(name, (ReferenceBinding) resolvedImport) // static imports are allowed to see inherited member types
								: findDirectMemberType(name, (ReferenceBinding) resolvedImport));
						if (temp != null) {
							if (temp.isValidBinding()) {
								ImportReference importReference = someImport.reference;
								if (importReference != null) importReference.used = true;
								if (foundInImport)
									// Answer error binding -- import on demand conflict; name found in two import on demand packages.
									return new ProblemReferenceBinding(name, Ambiguous);
								type = temp;
								foundInImport = true;
							} else if (foundType == null) {
								foundType = temp;
							}
						}
					}
				}
				if (type != null) return type;
			}
		}

		unitScope.recordSimpleReference(name);
		if ((mask & Binding.PACKAGE) != 0) {
			PackageBinding packageBinding = unitScope.environment.getTopLevelPackage(name);
			if (packageBinding != null) return packageBinding;
		}

		// Answer error binding -- could not find name
		if (foundType != null) return foundType; // problem type from above
		return new ProblemReferenceBinding(name, NotFound);
	}

	// Added for code assist... NOT Public API
	// DO NOT USE to resolve import references since this method assumes 'A.B' is relative to a single type import of 'p1.A'
	// when it may actually mean the type B in the package A
	// use CompilationUnitScope.getImport(char[][]) instead
	public final Binding getTypeOrPackage(char[][] compoundName) {
		int nameLength = compoundName.length;
		if (nameLength == 1) {
			TypeBinding binding = getBaseType(compoundName[0]);
			if (binding != null) return binding;
		}
		Binding binding = getTypeOrPackage(compoundName[0], Binding.TYPE | Binding.PACKAGE);
		if (!binding.isValidBinding()) return binding;

		int currentIndex = 1;
		boolean checkVisibility = false;
		if (binding instanceof PackageBinding) {
			PackageBinding packageBinding = (PackageBinding) binding;

			while (currentIndex < nameLength) {
				binding = packageBinding.getTypeOrPackage(compoundName[currentIndex++]);
				if (binding == null)
					return new ProblemReferenceBinding(
						CharOperation.subarray(compoundName, 0, currentIndex),
						NotFound);
				if (!binding.isValidBinding())
					return new ProblemReferenceBinding(
						CharOperation.subarray(compoundName, 0, currentIndex),
						binding.problemId());
				if (!(binding instanceof PackageBinding))
					break;
				packageBinding = (PackageBinding) binding;
			}
			if (binding instanceof PackageBinding) return binding;
			checkVisibility = true;
		}
		// binding is now a ReferenceBinding
		ReferenceBinding qualifiedType = null;

		ReferenceBinding typeBinding = (ReferenceBinding) binding;
		if (typeBinding.isGenericType()) {
			qualifiedType = this.environment().createRawType(typeBinding, qualifiedType);
		} else {
			qualifiedType = (qualifiedType != null && (qualifiedType.isRawType() || qualifiedType.isParameterizedType()))
				? this.createParameterizedType(typeBinding, null, qualifiedType)
				: typeBinding;
		}

		if (checkVisibility) // handles the fall through case
			if (!typeBinding.canBeSeenBy(this))
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding,
					NotVisible);

		while (currentIndex < nameLength) {
			typeBinding = getMemberType(compoundName[currentIndex++], typeBinding);

			if (typeBinding.isGenericType()) {
				qualifiedType = this.environment().createRawType(typeBinding, qualifiedType);
			} else {
				qualifiedType = (qualifiedType != null && (qualifiedType.isRawType() || qualifiedType.isParameterizedType()))
					? this.createParameterizedType(typeBinding, null, qualifiedType)
					: typeBinding;
			}

			// checks visibility
			if (!qualifiedType.isValidBinding())
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					qualifiedType.problemId());
		}
		return qualifiedType;
	}
	
	// 5.1.10
	public TypeBinding[] greaterLowerBound(TypeBinding[] types) {
		if (types == null) return null;
		int length = types.length;
		TypeBinding[] result = types;
		int removed = 0;
		for (int i = 0; i < length; i++) {
			TypeBinding iType = result[i];
			for (int j = 0; j < length; j++) {
				if (i == j) continue;
				TypeBinding jType = result[j];
				if (jType == null) continue;
				if (iType.isCompatibleWith(jType)) { // if Vi <: Vj, Vj is removed
					if (result == types) { // defensive copy
						System.arraycopy(result, 0, result = new TypeBinding[length], 0, length);
					}
					result[j] = null;
					removed ++;
				}
			}
		}
		if (removed == 0) return result;
		TypeBinding[] trimmedResult = new TypeBinding[length - removed];
		for (int i = 0, index = 0; i < length; i++) {
			TypeBinding iType = result[i];
			if (iType != null) {
				trimmedResult[index++] = iType;
			}
		}
		return trimmedResult;
	}

	public boolean isBoxingCompatibleWith(TypeBinding left, TypeBinding right) {
		return left.isBaseType() != right.isBaseType() && environment().isBoxingCompatibleWith(left, right);
	}

	/* Answer true if the scope is nested inside a given field declaration.
	 * Note: it works as long as the scope.fieldDeclarationIndex is reflecting the field being traversed 
	 * e.g. during name resolution.
	*/
	public final boolean isDefinedInField(FieldBinding field) {
		Scope scope = this;
		do {
			if (scope instanceof MethodScope) {
				MethodScope methodScope = (MethodScope) scope;
				if (methodScope.initializedField == field) return true;
			}
			scope = scope.parent;
		} while (scope != null);
		return false;
	}

	/* Answer true if the scope is nested inside a given method declaration
	*/
	public final boolean isDefinedInMethod(MethodBinding method) {
		Scope scope = this;
		do {
			if (scope instanceof MethodScope) {
				ReferenceContext refContext = ((MethodScope) scope).referenceContext;
				if (refContext instanceof AbstractMethodDeclaration)
					if (((AbstractMethodDeclaration) refContext).binding == method)
						return true;
			}
			scope = scope.parent;
		} while (scope != null);
		return false;
	}

	/* Answer whether the type is defined in the same compilation unit as the receiver
	*/
	public final boolean isDefinedInSameUnit(ReferenceBinding type) {
		// find the outer most enclosing type
		ReferenceBinding enclosingType = type;
		while ((type = enclosingType.enclosingType()) != null)
			enclosingType = type;

		// find the compilation unit scope
		Scope scope, unitScope = this;
		while ((scope = unitScope.parent) != null)
			unitScope = scope;

		// test that the enclosingType is not part of the compilation unit
		SourceTypeBinding[] topLevelTypes = ((CompilationUnitScope) unitScope).topLevelTypes;
		for (int i = topLevelTypes.length; --i >= 0;)
			if (topLevelTypes[i] == enclosingType)
				return true;
		return false;
	}
		
	/* Answer true if the scope is nested inside a given type declaration
	*/
	public final boolean isDefinedInType(ReferenceBinding type) {
		Scope scope = this;
		do {
			if (scope instanceof ClassScope)
				if (((ClassScope) scope).referenceContext.binding == type)
					return true;
			scope = scope.parent;
		} while (scope != null);
		return false;
	}

	public boolean isInsideDeprecatedCode(){
		switch(this.kind){
			case Scope.BLOCK_SCOPE :
			case Scope.METHOD_SCOPE :
				MethodScope methodScope = methodScope();
				if (!methodScope.isInsideInitializer()){
					// check method modifiers to see if deprecated
					MethodBinding context = ((AbstractMethodDeclaration)methodScope.referenceContext).binding;
					if (context != null && context.isViewedAsDeprecated())
						return true;
				} else {
					SourceTypeBinding type = ((BlockScope)this).referenceType().binding;
					// inside field declaration ? check field modifier to see if deprecated
					if (methodScope.initializedField != null && methodScope.initializedField.isViewedAsDeprecated())
						return true;
					if (type != null && type.isViewedAsDeprecated())
						return true;
				}
				break;
			case Scope.CLASS_SCOPE :
				ReferenceBinding context = ((ClassScope)this).referenceType().binding;
				if (context != null && context.isViewedAsDeprecated())
					return true;
				break;
		}
		return false;
	}
	private TypeBinding leastContainingInvocation(TypeBinding mec, Set invocations, List lubStack) {
		int length = invocations.size();
		Iterator iter = invocations.iterator();
		if (length == 1) return (TypeBinding) iter.next();
		int argLength = mec.typeVariables().length;
		if (argLength == 0) return mec; // should be caught by no invocation check

		// infer proper parameterized type from invocations
		TypeBinding[] bestArguments = new TypeBinding[argLength];
		while (iter.hasNext()) {
			TypeBinding invocation = (TypeBinding)iter.next();
			TypeVariableBinding[] invocationVariables = invocation.typeVariables();
			if (invocation.isGenericType()) {
				for (int i = 0; i < argLength; i++) {
					TypeBinding bestArgument = leastContainingTypeArgument(bestArguments[i], invocationVariables[i], (ReferenceBinding) mec, i, lubStack);
					if (bestArgument == null) return null;
					bestArguments[i] = bestArgument;
				}
			} else if (invocation.isParameterizedType()) {
				ParameterizedTypeBinding parameterizedType = (ParameterizedTypeBinding)invocation;
				for (int i = 0; i < argLength; i++) {
					TypeBinding bestArgument = leastContainingTypeArgument(bestArguments[i], parameterizedType.arguments[i], (ReferenceBinding) mec, i, lubStack);
					if (bestArgument == null) return null;
					bestArguments[i] = bestArgument;
				}
			} else if (invocation.isRawType()) {
				return invocation; // raw type is taking precedence
			}
		}
		return createParameterizedType((ReferenceBinding) mec.erasure(), bestArguments, null);
	}
	
	// JLS 15.12.2
	private TypeBinding leastContainingTypeArgument(TypeBinding u, TypeBinding v, ReferenceBinding genericType, int rank, List lubStack) {
		if (u == null) return v;
		if (u == v) return u;
		if (v.isWildcard()) {
			WildcardBinding wildV = (WildcardBinding) v;
			if (u.isWildcard()) {
				WildcardBinding wildU = (WildcardBinding) u;
				switch (wildU.kind) {
					// ? extends U
					case Wildcard.EXTENDS :
						switch(wildV.kind) {
							// ? extends U, ? extends V
							case Wildcard.EXTENDS :  
								TypeBinding lub = lowerUpperBound(new TypeBinding[]{wildU.bound,wildV.bound}, lubStack);
								if (lub == null) return null;
								// int is returned to denote cycle detected in lub computation - stop recursion by answering unbound wildcard
								if (lub == IntBinding) return environment().createWildcard(genericType, rank, null, null /*no extra bound*/, Wildcard.UNBOUND);
								return environment().createWildcard(genericType, rank, lub, null /*no extra bound*/, Wildcard.EXTENDS);	
							// ? extends U, ? SUPER V
							case Wildcard.SUPER : 
								if (wildU.bound == wildV.bound) return wildU.bound;
								return environment().createWildcard(genericType, rank, null, null /*no extra bound*/, Wildcard.UNBOUND);
						}
						break;
						// ? super U
					case Wildcard.SUPER : 
						// ? super U, ? super V
						if (wildU.kind == Wildcard.SUPER) {
							TypeBinding[] glb = greaterLowerBound(new TypeBinding[]{wildU.bound,wildV.bound});
							if (glb == null) return null;
							return environment().createWildcard(genericType, rank, glb[0], null /*no extra bound*/, Wildcard.SUPER);	// TODO (philippe) need to capture entire bounds
						}
				}				
			} else {
				switch (wildV.kind) {
					// U, ? extends V
					case Wildcard.EXTENDS :
						TypeBinding lub = lowerUpperBound(new TypeBinding[]{u,wildV.bound}, lubStack);
						if (lub == null) return null;
						// int is returned to denote cycle detected in lub computation - stop recursion by answering unbound wildcard
						if (lub == IntBinding) return environment().createWildcard(genericType, rank, null, null /*no extra bound*/, Wildcard.UNBOUND);
						return environment().createWildcard(genericType, rank, lub, null /*no extra bound*/, Wildcard.EXTENDS);	
					// U, ? super V
					case Wildcard.SUPER :
						TypeBinding[] glb = greaterLowerBound(new TypeBinding[]{u,wildV.bound});
						if (glb == null) return null;
						return environment().createWildcard(genericType, rank, glb[0], null /*no extra bound*/, Wildcard.SUPER);	// TODO (philippe) need to capture entire bounds
					case Wildcard.UNBOUND :
				}
			}
		} else if (u.isWildcard()) {
			WildcardBinding wildU = (WildcardBinding) u;
			switch (wildU.kind) {
				// U, ? extends V
				case Wildcard.EXTENDS :
					TypeBinding lub = lowerUpperBound(new TypeBinding[]{wildU.bound, v}, lubStack);
					if (lub == null) return null;
					// int is returned to denote cycle detected in lub computation - stop recursion by answering unbound wildcard
					if (lub == IntBinding) return environment().createWildcard(genericType, rank, null, null /*no extra bound*/, Wildcard.UNBOUND);
					return environment().createWildcard(genericType, rank, lub, null /*no extra bound*/, Wildcard.EXTENDS);	
				// U, ? super V
				case Wildcard.SUPER :
					TypeBinding[] glb = greaterLowerBound(new TypeBinding[]{wildU.bound, v});
					if (glb == null) return null;
					return environment().createWildcard(genericType, rank, glb[0], null /*no extra bound*/, Wildcard.SUPER); // TODO (philippe) need to capture entire bounds		
				case Wildcard.UNBOUND :
			}
		}
		TypeBinding lub = lowerUpperBound(new TypeBinding[]{u,v}, lubStack);
		if (lub == null) return null;
		// int is returned to denote cycle detected in lub computation - stop recursion by answering unbound wildcard
		if (lub == IntBinding) return environment().createWildcard(genericType, rank, null, null /*no extra bound*/, Wildcard.UNBOUND);
		return environment().createWildcard(genericType, rank, lub, null /*no extra bound*/, Wildcard.EXTENDS);
	}

	// 15.12.2
	public TypeBinding lowerUpperBound(TypeBinding[] types) {
		return lowerUpperBound(types, new ArrayList(1));
	}
	
	// 15.12.2
	private TypeBinding lowerUpperBound(TypeBinding[] types, List lubStack) {
		
		int typeLength = types.length;
		if (typeLength == 1) {
			TypeBinding type = types[0];
			return type == null ? VoidBinding : type;
		}
		// cycle detection
		int stackLength = lubStack.size();
		nextLubCheck: for (int i = 0; i < stackLength; i++) {
			TypeBinding[] lubTypes = (TypeBinding[])lubStack.get(i);
			int lubTypeLength = lubTypes.length;
			if (lubTypeLength < typeLength) continue nextLubCheck;
			nextTypeCheck:	for (int j = 0; j < typeLength; j++) {
				TypeBinding type = types[j];
				for (int k = 0; k < lubTypeLength; k++) {
					if (lubTypes[k] == type) continue nextTypeCheck; // type found, jump to next one
				}
				continue nextLubCheck; // type not found in current lubTypes
			}
			// all types are included in some lub, cycle detected - stop recursion by answering special value (int)
			return IntBinding;
		}

		lubStack.add(types);
		Map invocations = new HashMap(1);
		TypeBinding[] mecs = minimalErasedCandidates(types, invocations);
		if (mecs == null) return null;
		int length = mecs.length;
		if (length == 0) return VoidBinding;
		int count = 0;
		TypeBinding firstBound = null;
		for (int i = 0; i < length; i++) {
			TypeBinding mec = mecs[i];
			if (mec == null) continue;
			mec = leastContainingInvocation(mec, (Set)invocations.get(mec), lubStack);
			if (mec == null) return null;
			if (mec.isClass()) firstBound = mec;
			mecs[count++] = mec; // recompact them to the front

		}
		switch (count) {
			case 0 : return VoidBinding;
			case 1 : return mecs[0];
			case 2 : 
				if (mecs[1].id == T_JavaLangObject) return mecs[0];
				if (mecs[0].id == T_JavaLangObject) return mecs[1];
		}
		TypeBinding[] otherBounds = new TypeBinding[count - 1];
		int rank = 0;
		for (int i = 0; i < count; i++) {
			TypeBinding mec = mecs[i];
			if (mec.isInterface()) {
				otherBounds[rank++] = (ReferenceBinding)mec;
			}
		}
		return environment().createWildcard(null, 0, firstBound, otherBounds, Wildcard.EXTENDS);
	}
	
	public final MethodScope methodScope() {
		Scope scope = this;
		do {
			if (scope instanceof MethodScope)
				return (MethodScope) scope;
			scope = scope.parent;
		} while (scope != null);
		return null;
	}

	/**
	 * Returns the most specific set of types compatible with all given types.
	 * (i.e. most specific common super types)
	 * If no types is given, will return an empty array. If not compatible 
	 * reference type is found, returns null. In other cases, will return an array 
	 * of minimal erased types, where some nulls may appear (and must simply be
	 * ignored).
	 */
	private TypeBinding[] minimalErasedCandidates(TypeBinding[] types, Map allInvocations) {
		int length = types.length;
		int indexOfFirst = -1, actualLength = 0;
		for (int i = 0; i < length; i++) {
			TypeBinding type = types[i];
			if (type == null) continue;
			if (type.isBaseType()) return null;
			if (indexOfFirst < 0) indexOfFirst = i;
			actualLength ++;
		}
		switch (actualLength) {
			case 0: return NoTypes;
			case 1: return types;
		}

		// record all supertypes of type
		// intersect with all supertypes of otherType
		TypeBinding firstType = types[indexOfFirst];
		TypeBinding[] superTypes;
		int superLength;
		if (firstType.isBaseType()) {
			return null; 
		} else if (firstType.isArrayType()) {
			superLength = 4;
			if (firstType.erasure() != firstType) {
				ArrayList someInvocations = new ArrayList(1);
				someInvocations.add(firstType);
				allInvocations.put(firstType.erasure(), someInvocations);
			}
			superTypes = new TypeBinding[] { // inject well-known array supertypes
					firstType.erasure(), 
					getJavaIoSerializable(),
					getJavaLangCloneable(),
					getJavaLangObject(),
			};
		} else {
			ArrayList typesToVisit = new ArrayList(5);
			if (firstType.erasure() != firstType) {
				Set someInvocations = new HashSet(1);
				someInvocations.add(firstType);
				allInvocations.put(firstType.erasure(), someInvocations);
			}			
			typesToVisit.add(firstType.erasure());
			ReferenceBinding currentType = (ReferenceBinding)firstType;
			for (int i = 0, max = 1; i < max; i++) {
				currentType = (ReferenceBinding) typesToVisit.get(i);
				// inject super interfaces prior to superclass
				ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
				for (int j = 0, count = itsInterfaces.length; j < count; j++) {
					TypeBinding itsInterface = itsInterfaces[j];
					TypeBinding itsInterfaceErasure = itsInterface.erasure();
					if (!typesToVisit.contains(itsInterfaceErasure)) {
						if (itsInterfaceErasure != itsInterface) {
							Set someInvocations = new HashSet(1);
							someInvocations.add(itsInterface);
							allInvocations.put(itsInterfaceErasure, someInvocations);
						}						
						typesToVisit.add(itsInterfaceErasure);
						max++;
					}
				}
				TypeBinding itsSuperclass = currentType.superclass();
				if (itsSuperclass != null) {
					TypeBinding itsSuperclassErasure = itsSuperclass.erasure();
					if (!typesToVisit.contains(itsSuperclassErasure)) {
						if (itsSuperclassErasure != itsSuperclass) {
							Set someInvocations = new HashSet(1);
							someInvocations.add(itsSuperclass);
							allInvocations.put(itsSuperclassErasure, someInvocations);
						}
						typesToVisit.add(itsSuperclassErasure);
						max++;
					}
				}
			}
			superLength = typesToVisit.size();
			superTypes = new TypeBinding[superLength];
			typesToVisit.toArray(superTypes);
		}
		// intersecting first type supertypes with other types' ones, nullifying non matching supertypes
		int remaining = superLength;
		nextOtherType: for (int i = indexOfFirst+1; i < length; i++) {
			TypeBinding otherType = types[i];
			if (otherType == null) continue nextOtherType;
			if (otherType.isArrayType()) {
				nextSuperType: for (int j = 0; j < superLength; j++) {
					TypeBinding superType = superTypes[j];
					if (superType == null || superType == otherType) continue nextSuperType;
					switch (superType.id) {
						case T_JavaIoSerializable :
						case T_JavaLangCloneable :
						case T_JavaLangObject :
							continue nextSuperType;
					}
					superTypes[j] = null;
					if (--remaining == 0) return null;
					
				}
				continue nextOtherType;
			}
			ReferenceBinding otherRefType = (ReferenceBinding) otherType;
			nextSuperType: for (int j = 0; j < superLength; j++) {
				TypeBinding superType = superTypes[j];
				if (superType == null) continue nextSuperType;
				TypeBinding match;
				if (superType == otherType || superType.id == T_JavaLangObject && otherType.isInterface()) {
					match = superType;
				} else {
					if (superType.isArrayType()) {
						match = null;
					} else {
						match = otherRefType.findSuperTypeErasingTo((ReferenceBinding)superType);
					}
					if (match == null) { // incompatible super type
						superTypes[j] = null;
						if (--remaining == 0) return null;
						continue nextSuperType;
					}
				}
				// record invocation
				Set someInvocations = (Set) allInvocations.get(superType);
				if (someInvocations == null) someInvocations = new HashSet(1);
				someInvocations.add(match);
				allInvocations.put(superType, someInvocations);
			}				
		}
		// eliminate non minimal super types
		if (remaining > 1) {
			nextType: for (int i = 0; i < superLength; i++) {
				ReferenceBinding superType = (ReferenceBinding)superTypes[i];
				if (superType == null) continue nextType;
				nextOtherType: for (int j = 0; j < superLength; j++) {
					if (i == j) continue nextOtherType;
					ReferenceBinding otherType = (ReferenceBinding)superTypes[j];
					if (otherType == null) continue nextOtherType;
					if (otherType.id == T_JavaLangObject && superType.isInterface()) continue nextOtherType;
					if (superType.findSuperTypeErasingTo(otherType) != null) {
						superTypes[j] = null; // discard non minimal supertype
						remaining--;
					}
				}
			}
		}
		return superTypes;
	}
	
	// Internal use only
	/* All methods in visible are acceptable matches for the method in question...
	* The methods defined by the receiver type appear before those defined by its
	* superclass and so on. We want to find the one which matches best.
	*
	* Since the receiver type is a class, we know each method's declaring class is
	* either the receiver type or one of its superclasses. It is an error if the best match
	* is defined by a superclass, when a lesser match is defined by the receiver type
	* or a closer superclass.
	*/
	protected final MethodBinding mostSpecificClassMethodBinding(MethodBinding[] visible, int visibleSize, InvocationSite invocationSite) {
		MethodBinding problemMethod = null;
		MethodBinding previous = null;
		nextVisible : for (int i = 0; i < visibleSize; i++) {
			MethodBinding method = visible[i];
			if (previous != null && method.declaringClass != previous.declaringClass)
				break; // cannot answer a method farther up the hierarchy than the first method found

			if (!method.isStatic()) previous = method; // no ambiguity for static methods
			for (int j = 0; j < visibleSize; j++) {
				if (i == j) continue;
				if (!visible[j].areParametersCompatibleWith(method.parameters))
					continue nextVisible;
			}
			compilationUnitScope().recordTypeReferences(method.thrownExceptions);
			return method;
		}
		if (problemMethod == null)
			return new ProblemMethodBinding(visible[0].selector, visible[0].parameters, Ambiguous);
		return problemMethod;
	}
	
	// Internal use only
	/* All methods in visible are acceptable matches for the method in question...
	* Since the receiver type is an interface, we ignore the possibility that 2 inherited
	* but unrelated superinterfaces may define the same method in acceptable but
	* not identical ways... we just take the best match that we find since any class which
	* implements the receiver interface MUST implement all signatures for the method...
	* in which case the best match is correct.
	*
	* NOTE: This is different than javac... in the following example, the message send of
	* bar(X) in class Y is supposed to be ambiguous. But any class which implements the
	* interface I MUST implement both signatures for bar. If this class was the receiver of
	* the message send instead of the interface I, then no problem would be reported.
	*
	interface I1 {
		void bar(J j);
	}
	interface I2 {
	//	void bar(J j);
		void bar(Object o);
	}
	interface I extends I1, I2 {}
	interface J {}
	
	class X implements J {}
	
	class Y extends X {
		public void foo(I i, X x) { i.bar(x); }
	}
	*/
	protected final MethodBinding mostSpecificInterfaceMethodBinding(MethodBinding[] visible, int visibleSize, InvocationSite invocationSite) {
		MethodBinding problemMethod = null;
		nextVisible : for (int i = 0; i < visibleSize; i++) {
			MethodBinding method = visible[i];
			for (int j = 0; j < visibleSize; j++) {
				if (i == j) continue;
				if (!visible[j].areParametersCompatibleWith(method.parameters))
					continue nextVisible;
			}
			compilationUnitScope().recordTypeReferences(method.thrownExceptions);
			return method;
		}
		if (problemMethod == null)
			return new ProblemMethodBinding(visible[0].selector, visible[0].parameters, Ambiguous);
		return problemMethod;
	}

	protected final MethodBinding mostSpecificMethodBinding(MethodBinding[] visible, int visibleSize, TypeBinding[] argumentTypes, InvocationSite invocationSite) {
		int[] compatibilityLevels = new int[visibleSize];
		for (int i = 0; i < visibleSize; i++)
			compatibilityLevels[i] = parameterCompatibilityLevel(visible[i], argumentTypes);

		for (int level = 0; level <= VARARGS_COMPATIBLE; level++) {
			nextVisible : for (int i = 0; i < visibleSize; i++) {
				if (compatibilityLevels[i] != level) continue nextVisible; // skip this method for now
				MethodBinding method = visible[i];
				for (int j = 0; j < visibleSize; j++) {
					if (i == j || compatibilityLevels[j] != level) continue;
					// tiebreak generic methods using variant where type params are substituted by their erasures
					if (!visible[j].tiebreakMethod().areParametersCompatibleWith(method.tiebreakMethod().parameters)) {
						if (method.isVarargs() && visible[j].isVarargs()) {
							int paramLength = method.parameters.length;
							if (paramLength == visible[j].parameters.length && paramLength == argumentTypes.length + 1) {
								TypeBinding elementsType = ((ArrayBinding) visible[j].parameters[paramLength - 1]).elementsType();
								if (method.parameters[paramLength - 1].isCompatibleWith(elementsType))
									continue; // special case to choose between 2 varargs methods when the last arg is missing
							}
						}
						continue nextVisible;
					}
				}
				compilationUnitScope().recordTypeReferences(method.thrownExceptions);
				return method;
			}
		}
		return new ProblemMethodBinding(visible[0].selector, visible[0].parameters, Ambiguous);
	}	

	public final ClassScope outerMostClassScope() {
		ClassScope lastClassScope = null;
		Scope scope = this;
		do {
			if (scope instanceof ClassScope)
				lastClassScope = (ClassScope) scope;
			scope = scope.parent;
		} while (scope != null);
		return lastClassScope; // may answer null if no class around
	}

	public final MethodScope outerMostMethodScope() {
		MethodScope lastMethodScope = null;
		Scope scope = this;
		do {
			if (scope instanceof MethodScope)
				lastMethodScope = (MethodScope) scope;
			scope = scope.parent;
		} while (scope != null);
		return lastMethodScope; // may answer null if no method around
	}

	protected int parameterCompatibilityLevel(MethodBinding method, TypeBinding[] arguments) {
		TypeBinding[] parameters = method.parameters;
		int paramLength = parameters.length;
		int argLength = arguments.length;

		LookupEnvironment env = environment();
		if (env.options.sourceLevel < ClassFileConstants.JDK1_5) {
			if (paramLength != argLength)
				return NOT_COMPATIBLE;
			for (int i = 0; i < argLength; i++) {
				TypeBinding param = parameters[i];
				TypeBinding arg = arguments[i];
				if (arg != param && !arg.isCompatibleWith(param))
					return NOT_COMPATIBLE;
			}
			return COMPATIBLE;
		}

		int level = COMPATIBLE; // no autoboxing or varargs support needed
		int lastIndex = argLength;
		if (method.isVarargs()) {
			lastIndex = paramLength - 1;
			if (paramLength == argLength) { // accept X or X[] but not X[][]
				TypeBinding param = parameters[lastIndex]; // is an ArrayBinding by definition
				TypeBinding arg = arguments[lastIndex];
				if (param != arg) {
					level = parameterCompatibilityLevel(arg, param, env);
					if (level == NOT_COMPATIBLE) {
						// expect X[], is it called with X
						param = ((ArrayBinding) param).elementsType();
						if (parameterCompatibilityLevel(arg, param, env) == NOT_COMPATIBLE)
							return NOT_COMPATIBLE;
						level = VARARGS_COMPATIBLE; // varargs support needed
					}
				}
			} else {
				if (paramLength < argLength) { // all remainig argument types must be compatible with the elementsType of varArgType
					TypeBinding param = ((ArrayBinding) parameters[lastIndex]).elementsType();
					for (int i = lastIndex; i < argLength; i++) {
						TypeBinding arg = arguments[i];
						if (param != arg && parameterCompatibilityLevel(arg, param, env) == NOT_COMPATIBLE)
							return NOT_COMPATIBLE;
					}
				}  else if (lastIndex != argLength) { // can call foo(int i, X ... x) with foo(1) but NOT foo();
					return NOT_COMPATIBLE;
				}
				level = VARARGS_COMPATIBLE; // varargs support needed
			}
			// now compare standard arguments from 0 to lastIndex
		}
		for (int i = 0; i < lastIndex; i++) {
			TypeBinding param = parameters[i];
			TypeBinding arg = arguments[i];
			if (arg != param) {
				int newLevel = parameterCompatibilityLevel(arg, param, env);
				if (newLevel == NOT_COMPATIBLE)
					return NOT_COMPATIBLE;
				if (newLevel > level)
					level = newLevel;
			}
		}
		return level;
	}

	private int parameterCompatibilityLevel(TypeBinding arg, TypeBinding param, LookupEnvironment env) {
		// only called if env.options.sourceLevel >= ClassFileConstants.JDK1_5
		if (arg.isCompatibleWith(param))
			return COMPATIBLE;
		if (arg.isBaseType() != param.isBaseType()) {
			TypeBinding convertedType = env.computeBoxingType(arg);
			if (convertedType == param || convertedType.isCompatibleWith(param))
				return AUTOBOX_COMPATIBLE;
		}
		return NOT_COMPATIBLE;
	}

	public abstract ProblemReporter problemReporter();

	public final CompilationUnitDeclaration referenceCompilationUnit() {
		Scope scope, unitScope = this;
		while ((scope = unitScope.parent) != null)
			unitScope = scope;
		return ((CompilationUnitScope) unitScope).referenceContext;
	}
	// start position in this scope - for ordering scopes vs. variables
	int startIndex() {
		return 0;
	}
	
	/**
	 * Returns the immediately enclosing switchCase statement (carried by closest blockScope),
	 */
	public CaseStatement switchCase() {
		Scope scope = this;
		do {
			if (scope instanceof BlockScope)
				return ((BlockScope) scope).switchCase;
			scope = scope.parent;
		} while (scope != null);
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9221.java