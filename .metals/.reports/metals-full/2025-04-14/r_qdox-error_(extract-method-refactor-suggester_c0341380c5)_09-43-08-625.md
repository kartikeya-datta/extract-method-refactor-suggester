error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/201.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/201.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/201.java
text:
```scala
O@@bjectVector found = new ObjectVector(); //TODO should rewrite to remove #matchingMethod since found is allocated anyway

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.jdt.internal.compiler.util.ObjectVector;

public abstract class Scope
	implements
		BaseTypes,
		BindingIds,
		CompilerModifiers,
		ProblemReasons,
		TagBits,
		TypeConstants,
		TypeIds {

	public Scope parent;
	public int kind;

	public final static int BLOCK_SCOPE = 1;
	public final static int METHOD_SCOPE = 2;
	public final static int CLASS_SCOPE = 3;
	public final static int COMPILATION_UNIT_SCOPE = 4;
	protected Scope(int kind, Scope parent) {
		this.kind = kind;
		this.parent = parent;
	}

	public abstract ProblemReporter problemReporter();

	// Internal use only
	protected final boolean areParametersAssignable(TypeBinding[] parameters, TypeBinding[] arguments) {
		if (parameters == arguments)
			return true;

		int length = parameters.length;
		if (length != arguments.length)
			return false;

		for (int i = 0; i < length; i++)
			if (parameters[i] != arguments[i])
				if (!arguments[i].isCompatibleWith(parameters[i]))
					return false;
		return true;
	}

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

	public ArrayBinding createArray(TypeBinding type, int dimension) {
		if (type.isValidBinding())
			return environment().createArrayType(type, dimension);
		else
			return new ArrayBinding(type, dimension);
	}

	public final ClassScope enclosingClassScope() {
		Scope scope = this;
		while ((scope = scope.parent) != null) {
			if (scope instanceof ClassScope) return (ClassScope)scope;
		}
		return null; // may answer null if no type around
	}

	public final MethodScope enclosingMethodScope() {
		Scope scope = this;
		while ((scope = scope.parent) != null) {
			if (scope instanceof MethodScope) return (MethodScope)scope;
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

	// Internal use only
	public ReferenceBinding findDirectMemberType(char[] typeName, ReferenceBinding enclosingType) {
		if ((enclosingType.tagBits & HasNoMemberTypes) != 0)
			return null; // know it has no member types (nor inherited member types)

		SourceTypeBinding enclosingSourceType = enclosingSourceType();
		compilationUnitScope().recordReference(enclosingType.compoundName, typeName);
		ReferenceBinding memberType = enclosingType.getMemberType(typeName);
		if (memberType != null) {
			compilationUnitScope().recordTypeReference(memberType); // to record supertypes
			if (enclosingSourceType == null
				? memberType.canBeSeenBy(getCurrentPackage())
				: memberType.canBeSeenBy(enclosingType, enclosingSourceType))
				return memberType;
			else
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

		compilationUnitScope().recordTypeReference(receiverType);
		compilationUnitScope().recordTypeReferences(argumentTypes);
		MethodBinding exactMethod = receiverType.getExactMethod(selector, argumentTypes);
		if (exactMethod != null) {
			compilationUnitScope().recordTypeReferences(exactMethod.thrownExceptions);
			if (receiverType.isInterface() || exactMethod.canBeSeenBy(receiverType, invocationSite, this))
				return exactMethod;
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
	public FieldBinding findField(TypeBinding receiverType, char[] fieldName, InvocationSite invocationSite) {
		if (receiverType.isBaseType()) return null;
		if (receiverType.isArrayType()) {
			TypeBinding leafType = receiverType.leafComponentType();
			if (leafType instanceof ReferenceBinding) {
				if (!((ReferenceBinding) leafType).canBeSeenBy(this))
					return new ProblemFieldBinding((ReferenceBinding)leafType, fieldName, ReceiverTypeNotVisible);
			}
			if (CharOperation.equals(fieldName, LENGTH))
				return ArrayBinding.ArrayLength;
			return null;
		}

		compilationUnitScope().recordTypeReference(receiverType);

		ReferenceBinding currentType = (ReferenceBinding) receiverType;
		if (!currentType.canBeSeenBy(this))
			return new ProblemFieldBinding(currentType, fieldName, ReceiverTypeNotVisible);

		FieldBinding field = currentType.getField(fieldName);
		if (field != null) {
			if (field.canBeSeenBy(currentType, invocationSite, this))
				return field;
			else
				return new ProblemFieldBinding(field.declaringClass, fieldName, NotVisible);
		}
		// collect all superinterfaces of receiverType until the field is found in a supertype
		ReferenceBinding[][] interfacesToVisit = null;
		int lastPosition = -1;
		FieldBinding visibleField = null;
		boolean keepLooking = true;
		boolean notVisible = false;
		// we could hold onto the not visible field for extra error reporting
		while (keepLooking) {
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				if (interfacesToVisit == null)
					interfacesToVisit = new ReferenceBinding[5][];
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(
						interfacesToVisit,
						0,
						interfacesToVisit = new ReferenceBinding[lastPosition * 2][],
						0,
						lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
			if ((currentType = currentType.superclass()) == null)
				break;

			if ((field = currentType.getField(fieldName)) != null) {
				keepLooking = false;
				if (field.canBeSeenBy(receiverType, invocationSite, this)) {
					if (visibleField == null)
						visibleField = field;
					else
						return new ProblemFieldBinding(visibleField.declaringClass, fieldName, Ambiguous);
				} else {
					notVisible = true;
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
						if ((field = anInterface.getField(fieldName)) != null) {
							if (visibleField == null) {
								visibleField = field;
							} else {
								ambiguous = new ProblemFieldBinding(visibleField.declaringClass, fieldName, Ambiguous);
								break done;
							}
						} else {
							ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
							if (itsInterfaces != NoSuperInterfaces) {
								if (++lastPosition == interfacesToVisit.length)
									System.arraycopy(
										interfacesToVisit,
										0,
										interfacesToVisit = new ReferenceBinding[lastPosition * 2][],
										0,
										lastPosition);
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
		if (notVisible)
			return new ProblemFieldBinding(currentType, fieldName, NotVisible);
		return null;
	}

	// Internal use only
	public ReferenceBinding findMemberType(char[] typeName, ReferenceBinding enclosingType) {
		if ((enclosingType.tagBits & HasNoMemberTypes) != 0)
			return null; // know it has no member types (nor inherited member types)

		SourceTypeBinding enclosingSourceType = enclosingSourceType();
		PackageBinding currentPackage = getCurrentPackage();
		compilationUnitScope().recordReference(enclosingType.compoundName, typeName);
		ReferenceBinding memberType = enclosingType.getMemberType(typeName);
		if (memberType != null) {
			compilationUnitScope().recordTypeReference(memberType); // to record supertypes
			if (enclosingSourceType == null
				? memberType.canBeSeenBy(currentPackage)
				: memberType.canBeSeenBy(enclosingType, enclosingSourceType))
				return memberType;
			else
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
			if (itsInterfaces != NoSuperInterfaces) {
				if (interfacesToVisit == null)
					interfacesToVisit = new ReferenceBinding[5][];
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(
						interfacesToVisit,
						0,
						interfacesToVisit = new ReferenceBinding[lastPosition * 2][],
						0,
						lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
			if ((currentType = currentType.superclass()) == null)
				break;

			compilationUnitScope().recordReference(currentType.compoundName, typeName);
			if ((memberType = currentType.getMemberType(typeName)) != null) {
				compilationUnitScope().recordTypeReference(memberType); // to record supertypes
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
						compilationUnitScope().recordReference(anInterface.compoundName, typeName);
						if ((memberType = anInterface.getMemberType(typeName)) != null) {
							compilationUnitScope().recordTypeReference(memberType); // to record supertypes
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
									System.arraycopy(
										interfacesToVisit,
										0,
										interfacesToVisit = new ReferenceBinding[lastPosition * 2][],
										0,
										lastPosition);
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
		ObjectVector found = new ObjectVector();

		compilationUnitScope().recordTypeReference(receiverType);
		compilationUnitScope().recordTypeReferences(argumentTypes);

		if (currentType.isInterface()) {
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

		boolean isCompliant14 = compilationUnitScope().environment.options.complianceLevel >= ClassFileConstants.JDK1_4;
		// superclass lookup
		ReferenceBinding classHierarchyStart = currentType;
		while (currentType != null) {
			MethodBinding[] currentMethods = currentType.getMethods(selector);
			int currentLength = currentMethods.length;
			
			/*
			 * if 1.4 compliant, must filter out redundant protected methods from superclasses
			 */
			if (isCompliant14){			 
				nextMethod: for (int i = 0; i < currentLength; i++){
					MethodBinding currentMethod = currentMethods[i];
					// protected method need to be checked only - default access is already dealt with in #canBeSeen implementation
					// when checking that p.C -> q.B -> p.A cannot see default access members from A through B.
					if ((currentMethod.modifiers & AccProtected) == 0) continue nextMethod;
					if (matchingMethod != null){
						if (currentMethod.areParametersEqual(matchingMethod)){
							currentLength--;
							currentMethods[i] = null; // discard this match
							continue nextMethod;
						}
					} else {
						for (int j = 0, max = found.size; j < max; j++) {
							if (((MethodBinding)found.elementAt(j)).areParametersEqual(currentMethod)){
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
		if (foundSize > 0) {
			// argument type compatibility check
			for (int i = 0; i < foundSize; i++) {
				MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
				if (areParametersAssignable(methodBinding.parameters, argumentTypes)) {
					switch (candidatesCount) {
						case 0: 
							matchingMethod = methodBinding; // if only one match, reuse matchingMethod
							checkedMatchingMethod = true; // matchingMethod is known to exist and match params here
							break;
						case 1:
							candidates = new MethodBinding[foundSize]; // only lazily created if more than one match
							candidates[0] = matchingMethod; // copy back
							matchingMethod = null;
							// fall through
						default:
							candidates[candidatesCount] = methodBinding;
					}
					candidatesCount++;
				}
			}
		}
		// if only one matching method left (either from start or due to elimination of rivals), then match is in matchingMethod
		if (matchingMethod != null) {
			if (checkedMatchingMethod || areParametersAssignable(matchingMethod.parameters, argumentTypes)) {
				// (if no default abstract) must explicitly look for one instead, which could be a better match
				if (!matchingMethod.canBeSeenBy(receiverType, invocationSite, this)) {
					// ignore matching method (to be consistent with multiple matches, none visible (matching method is then null)
					MethodBinding interfaceMethod = findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, null, found);						
					if (interfaceMethod != null) return interfaceMethod;
					compilationUnitScope().recordTypeReferences(matchingMethod.thrownExceptions);
					return matchingMethod;
				}
			} 
			return findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
		}

		// no match was found, try to find a close match when the parameter order is wrong or missing some parameters
		if (candidatesCount == 0) {
			MethodBinding interfaceMethod =
				findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
			if (interfaceMethod != null) return interfaceMethod;

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
			return (MethodBinding) found.elementAt(0); // no good match so just use the first one found
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
			compilationUnitScope().recordTypeReferences(candidates[0].thrownExceptions);
			return candidates[0];
		}
		if (visiblesCount == 0) {
			MethodBinding interfaceMethod =
				findDefaultAbstractMethod(receiverType, selector, argumentTypes, invocationSite, classHierarchyStart, matchingMethod, found);
			if (interfaceMethod != null) return interfaceMethod;
			return new ProblemMethodBinding(candidates[0], candidates[0].selector, candidates[0].parameters, NotVisible);
		}	
		if (candidates[0].declaringClass.isClass()) {
			return mostSpecificClassMethodBinding(candidates, visiblesCount);
		} else {
			return mostSpecificInterfaceMethodBinding(candidates, visiblesCount);
		}
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
		int foundSize = found.size;
		if (foundSize == startFoundSize) {
			if (matchingMethod != null) compilationUnitScope().recordTypeReferences(matchingMethod.thrownExceptions);
			return matchingMethod; // maybe null
		}
		MethodBinding[] candidates = new MethodBinding[foundSize - startFoundSize];
		int candidatesCount = 0;
		// argument type compatibility check
		for (int i = startFoundSize; i < foundSize; i++) {
			MethodBinding methodBinding = (MethodBinding) found.elementAt(i);
			if (areParametersAssignable(methodBinding.parameters, argumentTypes))
				candidates[candidatesCount++] = methodBinding;
		}
		if (candidatesCount == 1) {
			compilationUnitScope().recordTypeReferences(candidates[0].thrownExceptions);
			return candidates[0]; 
		}
		if (candidatesCount == 0) { // try to find a close match when the parameter order is wrong or missing some parameters
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
		return mostSpecificInterfaceMethodBinding(candidates, candidatesCount);
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
			if (argumentTypes == NoParameters && CharOperation.equals(selector, CLONE))
				return new UpdatedMethodBinding(
					environment().options.targetJDK >= ClassFileConstants.JDK1_4 ? (TypeBinding)receiverType : (TypeBinding)object, // remember its array type for codegen purpose on target>=1.4.0
					(methodBinding.modifiers ^ AccProtected) | AccPublic,
					CLONE,
					methodBinding.returnType,
					argumentTypes,
					null,
					object);
			if (methodBinding.canBeSeenBy(receiverType, invocationSite, this))
				return methodBinding;
		}
		// answers closest approximation, may not check argumentTypes or visibility
		methodBinding = findMethod(object, selector, argumentTypes, invocationSite);
		if (methodBinding == null)
			return new ProblemMethodBinding(selector, argumentTypes, NotFound);
		if (methodBinding.isValidBinding()) {
			if (!areParametersAssignable(methodBinding.parameters, argumentTypes))
				return new ProblemMethodBinding(
					methodBinding,
					selector,
					argumentTypes,
					NotFound);
			if (!methodBinding.canBeSeenBy(receiverType, invocationSite, this))
				return new ProblemMethodBinding(
					methodBinding,
					selector,
					methodBinding.parameters,
					NotVisible);
		}
		return methodBinding;
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
	
	public TypeBinding getBaseType(char[] name) {
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
	public Binding getBinding(char[] name, int mask, InvocationSite invocationSite) {
			
		Binding binding = null;
		FieldBinding problemField = null;
		if ((mask & VARIABLE) != 0) {
			if (this.kind == BLOCK_SCOPE || this.kind == METHOD_SCOPE) {
				LocalVariableBinding variableBinding = findVariable(name);
				// looks in this scope only
				if (variableBinding != null) return variableBinding;
			}

			boolean insideStaticContext = false;
			boolean insideConstructorCall = false;
			if (this.kind == METHOD_SCOPE) {
				MethodScope methodScope = (MethodScope) this;
				insideStaticContext |= methodScope.isStatic;
				insideConstructorCall |= methodScope.isConstructorCall;
			}

			FieldBinding foundField = null;
			// can be a problem field which is answered if a valid field is not found
			ProblemFieldBinding foundInsideProblem = null;
			// inside Constructor call or inside static context
			Scope scope = parent;
			if (scope == null) return new ProblemBinding(name, null, NotFound);
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
						FieldBinding fieldBinding =
							classScope.findField(enclosingType, name, invocationSite);
						// Use next line instead if willing to enable protected access accross inner types
						// FieldBinding fieldBinding = findField(enclosingType, name, invocationSite);
						if (fieldBinding != null) { // skip it if we did not find anything
							if (fieldBinding.problemId() == Ambiguous) {
								if (foundField == null || foundField.problemId() == NotVisible)
									// supercedes any potential InheritedNameHidesEnclosingName problem
									return fieldBinding;
								else
									// make the user qualify the field, likely wants the first inherited field (javac generates an ambiguous error instead)
									return new ProblemFieldBinding(
										fieldBinding.declaringClass,
										name,
										InheritedNameHidesEnclosingName);
							}

							ProblemFieldBinding insideProblem = null;
							if (fieldBinding.isValidBinding()) {
								if (!fieldBinding.isStatic()) {
									if (insideConstructorCall) {
										insideProblem =
											new ProblemFieldBinding(
												fieldBinding.declaringClass,
												name,
												NonStaticReferenceInConstructorInvocation);
									} else if (insideStaticContext) {
										insideProblem =
											new ProblemFieldBinding(
												fieldBinding.declaringClass,
												name,
												NonStaticReferenceInStaticContext);
									}
								}
								if (enclosingType == fieldBinding.declaringClass
 environment().options.complianceLevel >= ClassFileConstants.JDK1_4){
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
												fieldBinding.declaringClass,
												name,
												InheritedNameHidesEnclosingName);
								}
							}

							if (foundField == null
 (foundField.problemId() == NotVisible
									&& fieldBinding.problemId() != NotVisible)) {
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

			if (foundInsideProblem != null){
				return foundInsideProblem;
			}
			if (foundField != null) {
				if (foundField.isValidBinding()){
					if (foundDepth > 0){
						invocationSite.setDepth(foundDepth);
						invocationSite.setActualReceiverType(foundActualReceiverType);
					}
					return foundField;
				}
				problemField = foundField;
			}
		}

		// We did not find a local or instance variable.
		if ((mask & TYPE) != 0) {
			if ((binding = getBaseType(name)) != null)
				return binding;
			binding = getTypeOrPackage(name, (mask & PACKAGE) == 0 ? TYPE : TYPE | PACKAGE);
			if (binding.isValidBinding() || mask == TYPE)
				return binding;
			// answer the problem type binding if we are only looking for a type
		} else if ((mask & PACKAGE) != 0) {
			compilationUnitScope().recordSimpleReference(name);
			if ((binding = environment().getTopLevelPackage(name)) != null)
				return binding;
		}
		if (problemField != null)
			return problemField;
		else
			return new ProblemBinding(name, enclosingSourceType(), NotFound);
	}

	public final PackageBinding getCurrentPackage() {
		Scope scope, unitScope = this;
		while ((scope = unitScope.parent) != null)
			unitScope = scope;
		return ((CompilationUnitScope) unitScope).fPackage;
	}

	public final ReferenceBinding getJavaIoSerializable() {
		compilationUnitScope().recordQualifiedReference(JAVA_IO_SERIALIZABLE);
		ReferenceBinding type = environment().getType(JAVA_IO_SERIALIZABLE);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_IO_SERIALIZABLE, referenceCompilationUnit());
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

	public final ReferenceBinding getJavaLangError() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ERROR);
		ReferenceBinding type = environment().getType(JAVA_LANG_ERROR);
		if (type != null) return type;
	
		problemReporter().isClassPathCorrect(JAVA_LANG_ERROR, referenceCompilationUnit());
		return null; // will not get here since the above error aborts the compilation
	}

	public final ReferenceBinding getJavaLangAssertionError() {
		compilationUnitScope().recordQualifiedReference(JAVA_LANG_ASSERTIONERROR);
		ReferenceBinding type = environment().getType(JAVA_LANG_ASSERTIONERROR);
		if (type != null) return type;
		problemReporter().isClassPathCorrect(JAVA_LANG_ASSERTIONERROR, referenceCompilationUnit());
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

	/* Answer the type binding corresponding to the typeName argument, relative to the enclosingType.
	*/
	public final ReferenceBinding getMemberType(char[] typeName, ReferenceBinding enclosingType) {
		ReferenceBinding memberType = findMemberType(typeName, enclosingType);
		if (memberType != null) return memberType;
		return new ProblemReferenceBinding(typeName, NotFound);
	}

	/* Answer the type binding corresponding to the compoundName.
	*
	* NOTE: If a problem binding is returned, senders should extract the compound name
	* from the binding & not assume the problem applies to the entire compoundName.
	*/
	public final TypeBinding getType(char[][] compoundName) {
		int typeNameLength = compoundName.length;
		if (typeNameLength == 1) {
			// Would like to remove this test and require senders to specially handle base types
			TypeBinding binding = getBaseType(compoundName[0]);
			if (binding != null) return binding;
		}

		compilationUnitScope().recordQualifiedReference(compoundName);
		Binding binding =
			getTypeOrPackage(compoundName[0], typeNameLength == 1 ? TYPE : TYPE | PACKAGE);
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
		compilationUnitScope().recordTypeReference(typeBinding); // to record supertypes
		if (checkVisibility) // handles the fall through case
			if (!typeBinding.canBeSeenBy(this))
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding,
					NotVisible);

		while (currentIndex < typeNameLength) {
			typeBinding = getMemberType(compoundName[currentIndex++], typeBinding);
			if (!typeBinding.isValidBinding())
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding.problemId());
		}
		return typeBinding;
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
		return (ReferenceBinding) getTypeOrPackage(name, TYPE);
	}

	// Added for code assist... NOT Public API
	public final Binding getTypeOrPackage(char[][] compoundName) {
		int nameLength = compoundName.length;
		if (nameLength == 1) {
			TypeBinding binding = getBaseType(compoundName[0]);
			if (binding != null) return binding;
		}
		Binding binding = getTypeOrPackage(compoundName[0], TYPE | PACKAGE);
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
		ReferenceBinding typeBinding = (ReferenceBinding) binding;
		if (checkVisibility) // handles the fall through case
			if (!typeBinding.canBeSeenBy(this))
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding,
					NotVisible);

		while (currentIndex < nameLength) {
			typeBinding = getMemberType(compoundName[currentIndex++], typeBinding);
			// checks visibility
			if (!typeBinding.isValidBinding())
				return new ProblemReferenceBinding(
					CharOperation.subarray(compoundName, 0, currentIndex),
					typeBinding.problemId());
		}
		return typeBinding;
	}

	/* Internal use only 
	*/
	final Binding getTypeOrPackage(char[] name, int mask) {
		Scope scope = this;
		ReferenceBinding foundType = null;
		if ((mask & TYPE) == 0) {
			Scope next = scope;
			while ((next = scope.parent) != null)
				scope = next;
		} else {
			done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
				switch (scope.kind) {
					case METHOD_SCOPE :
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
						// 6.5.5.1 - simple name favors member type over top-level type in same unit
						ReferenceBinding memberType = findMemberType(name, sourceType);
						if (memberType != null) { // skip it if we did not find anything
							if (memberType.problemId() == Ambiguous) {
								if (foundType == null || foundType.problemId() == NotVisible)
									// supercedes any potential InheritedNameHidesEnclosingName problem
									return memberType;
								else
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
									if (foundType.isValidBinding())
										// if a valid type was found, complain when another is found in an 'immediate' enclosing type (ie. not inherited)
										if (foundType != memberType)
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
		if ((mask & TYPE) != 0) {
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
								if (unitScope.resolveSingleTypeImport(typeImport) != null) {
									ImportReference importReference = typeImport.reference;
									if (importReference != null) importReference.used = true;
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
			boolean foundInImport = false;
			ReferenceBinding type = null;
			if (imports != null) {
				for (int i = 0, length = imports.length; i < length; i++) {
					ImportBinding someImport = imports[i];
					if (someImport.onDemand) {
						Binding resolvedImport = someImport.resolvedImport;
						ReferenceBinding temp = resolvedImport instanceof PackageBinding
								? findType(name, (PackageBinding) resolvedImport, currentPackage)
								: findDirectMemberType(name, (ReferenceBinding) resolvedImport);
						if (temp != null && temp.isValidBinding()) {
							ImportReference importReference = someImport.reference;
							if (importReference != null) importReference.used = true;
							if (foundInImport)
								// Answer error binding -- import on demand conflict; name found in two import on demand packages.
								return new ProblemReferenceBinding(name, Ambiguous);
							type = temp;
							foundInImport = true;
						}
					}
				}
			}
			if (type != null) return type;
		}

		unitScope.recordSimpleReference(name);
		if ((mask & PACKAGE) != 0) {
			PackageBinding packageBinding = unitScope.environment.getTopLevelPackage(name);
			if (packageBinding != null) return packageBinding;
		}

		// Answer error binding -- could not find name
		if (foundType != null) return foundType; // problem type from above
		return new ProblemReferenceBinding(name, NotFound);
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
		SourceTypeBinding[] topLevelTypes =
			((CompilationUnitScope) unitScope).topLevelTypes;
		for (int i = topLevelTypes.length; --i >= 0;)
			if (topLevelTypes[i] == enclosingType)
				return true;
		return false;
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
				ReferenceContext refContext = methodScope.referenceContext;
				if (refContext instanceof TypeDeclaration
						&& ((TypeDeclaration)refContext).binding == field.declaringClass
						&& methodScope.fieldDeclarationIndex == field.id) {
					return true;
				}
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
				if (refContext instanceof AbstractMethodDeclaration
						&& ((AbstractMethodDeclaration)refContext).binding == method) {
					return true;
				}
			}
			scope = scope.parent;
		} while (scope != null);
		return false;
	}
		
	/* Answer true if the scope is nested inside a given type declaration
	*/
	public final boolean isDefinedInType(ReferenceBinding type) {
		Scope scope = this;
		do {
			if (scope instanceof ClassScope)
				if (((ClassScope) scope).referenceContext.binding == type){
					return true;
				}
			scope = scope.parent;
		} while (scope != null);
		return false;
	}

	public boolean isInsideDeprecatedCode(){
		switch(kind){
			case Scope.BLOCK_SCOPE :
			case Scope.METHOD_SCOPE :
				MethodScope methodScope = methodScope();
				if (!methodScope.isInsideInitializer()){
					// check method modifiers to see if deprecated
					MethodBinding context = ((AbstractMethodDeclaration)methodScope.referenceContext).binding;
					if (context != null && context.isViewedAsDeprecated()) {
						return true;
					}
				} else {
					SourceTypeBinding type = ((BlockScope)this).referenceType().binding;

					// inside field declaration ? check field modifier to see if deprecated
					if (methodScope.fieldDeclarationIndex != MethodScope.NotInFieldDecl) {
						for (int i = 0; i < type.fields.length; i++){
							if (type.fields[i].id == methodScope.fieldDeclarationIndex) {
								// currently inside this field initialization
								if (type.fields[i].isViewedAsDeprecated()){
									return true;
								}
								break;
							}
						}
					}
					if (type != null && type.isViewedAsDeprecated()) {
						return true;
					}
				}
				break;
			case Scope.CLASS_SCOPE :
				ReferenceBinding context = ((ClassScope)this).referenceType().binding;
				if (context != null && context.isViewedAsDeprecated()) {
					return true;
				}
				break;
		}
		return false;
	}
	
	public final boolean isJavaIoSerializable(TypeBinding tb) {
		return tb == getJavaIoSerializable();
	}

	public final boolean isJavaLangCloneable(TypeBinding tb) {
		return tb == getJavaLangCloneable();
	}

	public final boolean isJavaLangObject(TypeBinding type) {
		return type.id == T_JavaLangObject;
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
	protected final MethodBinding mostSpecificClassMethodBinding(MethodBinding[] visible, int visibleSize) {

		MethodBinding method = null;
		MethodBinding previous = null;

		nextVisible : for (int i = 0; i < visibleSize; i++) {
			method = visible[i];
						
			if (previous != null && method.declaringClass != previous.declaringClass)
				break; // cannot answer a method farther up the hierarchy than the first method found
			previous = method;
			for (int j = 0; j < visibleSize; j++) {
				if (i == j) continue;
				MethodBinding next = visible[j];
				if (!areParametersAssignable(next.parameters, method.parameters))
					continue nextVisible;
			}
			compilationUnitScope().recordTypeReferences(method.thrownExceptions);
			return method;
		}
		return new ProblemMethodBinding(visible[0].selector, visible[0].parameters, Ambiguous);
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
	protected final MethodBinding mostSpecificInterfaceMethodBinding(MethodBinding[] visible, int visibleSize) {
		MethodBinding method = null;
		nextVisible : for (int i = 0; i < visibleSize; i++) {
			method = visible[i];
			for (int j = 0; j < visibleSize; j++) {
				if (i == j) continue;
				MethodBinding next = visible[j];
				if (!areParametersAssignable(next.parameters, method.parameters))
					continue nextVisible;
			}
			compilationUnitScope().recordTypeReferences(method.thrownExceptions);
			return method;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/201.java