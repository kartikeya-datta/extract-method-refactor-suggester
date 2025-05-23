error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2897.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2897.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2897.java
text:
```scala
e@@xceptions[i] = (ReferenceBinding) BinaryTypeBinding.resolveType(exceptions[i], this.environment, true /* raw conversion */);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;
import org.eclipse.jdt.internal.compiler.util.SimpleSet;

public class MethodVerifier {
	SourceTypeBinding type;
	HashtableOfObject inheritedMethods;
	HashtableOfObject currentMethods;
	LookupEnvironment environment;
	private boolean allowCompatibleReturnTypes;
/*
Binding creation is responsible for reporting all problems with types:
	- all modifier problems (duplicates & multiple visibility modifiers + incompatible combinations - abstract/final)
		- plus invalid modifiers given the context (the verifier did not do this before)
	- qualified name collisions between a type and a package (types in default packages are excluded)
	- all type hierarchy problems:
		- cycles in the superclass or superinterface hierarchy
		- an ambiguous, invisible or missing superclass or superinterface
		- extending a final class
		- extending an interface instead of a class
		- implementing a class instead of an interface
		- implementing the same interface more than once (ie. duplicate interfaces)
	- with nested types:
		- shadowing an enclosing type's source name
		- defining a static class or interface inside a non-static nested class
		- defining an interface as a local type (local types can only be classes)
*/
MethodVerifier(LookupEnvironment environment) {
	this.type = null;  // Initialized with the public method verify(SourceTypeBinding)
	this.inheritedMethods = null;
	this.currentMethods = null;
	this.environment = environment;
	this.allowCompatibleReturnTypes =
		environment.globalOptions.complianceLevel >= ClassFileConstants.JDK1_5
			&& environment.globalOptions.sourceLevel < ClassFileConstants.JDK1_5;
}
boolean areMethodsCompatible(MethodBinding one, MethodBinding two) {
	return isParameterSubsignature(one, two) && areReturnTypesCompatible(one, two);
}
boolean areParametersEqual(MethodBinding one, MethodBinding two) {
	TypeBinding[] oneArgs = one.parameters;
	TypeBinding[] twoArgs = two.parameters;
	if (oneArgs == twoArgs) return true;

	int length = oneArgs.length;
	if (length != twoArgs.length) return false;

	for (int i = 0; i < length; i++)
		if (!areTypesEqual(oneArgs[i], twoArgs[i])) return false;
	return true;
}
boolean areReturnTypesCompatible(MethodBinding one, MethodBinding two) {
	if (one.returnType == two.returnType) return true;

	if (areTypesEqual(one.returnType, two.returnType)) return true;
	
	// when sourceLevel < 1.5 but compliance >= 1.5, allow return types in binaries to be compatible instead of just equal
	if (this.allowCompatibleReturnTypes &&
			one.declaringClass instanceof BinaryTypeBinding &&
			two.declaringClass instanceof BinaryTypeBinding) {
		return areReturnTypesCompatible0(one, two);
	}
	return false;
}
boolean areReturnTypesCompatible0(MethodBinding one, MethodBinding two) {
	// short is compatible with int, but as far as covariance is concerned, its not
	if (one.returnType.isBaseType()) return false;

	if (!one.declaringClass.isInterface()) {
		if (one.declaringClass.id == TypeIds.T_JavaLangObject)
			return two.returnType.isCompatibleWith(one.returnType); // interface methods inherit from Object
		return one.returnType.isCompatibleWith(two.returnType);
	}

	// check for methods from Object, every interface inherits from Object
	if (two.declaringClass.id == TypeIds.T_JavaLangObject)
		return one.returnType.isCompatibleWith(two.returnType);

	// both are interfaces, see if they're related
	if (one.declaringClass.implementsInterface(two.declaringClass, true))
		return one.returnType.isCompatibleWith(two.returnType);
	if (two.declaringClass.implementsInterface(one.declaringClass, true))
		return two.returnType.isCompatibleWith(one.returnType);

	// unrelated interfaces... one must be a subtype of the other
	return one.returnType.isCompatibleWith(two.returnType)
 two.returnType.isCompatibleWith(one.returnType);
}
boolean areTypesEqual(TypeBinding one, TypeBinding two) {
	if (one == two) return true;

	// its possible that an UnresolvedReferenceBinding can be compared to its resolved type
	// when they're both UnresolvedReferenceBindings then they must be identical like all other types
	// all wrappers of UnresolvedReferenceBindings are converted as soon as the type is resolved
	// so its not possible to have 2 arrays where one is UnresolvedX[] and the other is X[]
	if (one instanceof UnresolvedReferenceBinding)
		return ((UnresolvedReferenceBinding) one).resolvedType == two;
	if (two instanceof UnresolvedReferenceBinding)
		return ((UnresolvedReferenceBinding) two).resolvedType == one;
	return false; // all other type bindings are identical
}
boolean canSkipInheritedMethods() {
	if (this.type.superclass() != null && this.type.superclass().isAbstract())
		return false;
	return this.type.superInterfaces() == Binding.NO_SUPERINTERFACES;
}
boolean canSkipInheritedMethods(MethodBinding one, MethodBinding two) {
	return two == null // already know one is not null
 one.declaringClass == two.declaringClass;
}
void checkAbstractMethod(MethodBinding abstractMethod) {
	if (mustImplementAbstractMethod(abstractMethod.declaringClass)) {
		TypeDeclaration typeDeclaration = this.type.scope.referenceContext;
		if (typeDeclaration != null) {
			MethodDeclaration missingAbstractMethod = typeDeclaration.addMissingAbstractMethodFor(abstractMethod);
			missingAbstractMethod.scope.problemReporter().abstractMethodMustBeImplemented(this.type, abstractMethod);
		} else {
			problemReporter().abstractMethodMustBeImplemented(this.type, abstractMethod);
		}
	}
}
void checkAgainstInheritedMethods(MethodBinding currentMethod, MethodBinding[] methods, int length, MethodBinding[] allInheritedMethods) {
	if (this.type.isAnnotationType()) { // annotation cannot override any method
		problemReporter().annotationCannotOverrideMethod(currentMethod, methods[length - 1]);
		return; // do not repoort against subsequent inherited methods
	}
	CompilerOptions options = type.scope.compilerOptions();
	// need to find the overridden methods to avoid blaming this type for issues which are already reported against a supertype
	// but cannot ignore an overridden inherited method completely when it comes to checking for bridge methods
	int[] overriddenInheritedMethods = length > 1 ? findOverriddenInheritedMethods(methods, length) : null;
	nextMethod : for (int i = length; --i >= 0;) {
		MethodBinding inheritedMethod = methods[i];
		if (overriddenInheritedMethods == null || overriddenInheritedMethods[i] == 0) {
			if (currentMethod.isStatic() != inheritedMethod.isStatic()) {  // Cannot override a static method or hide an instance method
				problemReporter(currentMethod).staticAndInstanceConflict(currentMethod, inheritedMethod);
				continue nextMethod;
			}

			// want to tag currentMethod even if return types are not equal
			if (inheritedMethod.isAbstract()) {
				if (inheritedMethod.declaringClass.isInterface()) {
					currentMethod.modifiers |= ExtraCompilerModifiers.AccImplementing;
				} else {
					currentMethod.modifiers |= ExtraCompilerModifiers.AccImplementing | ExtraCompilerModifiers.AccOverriding;
				}
//			with the above change an abstract method is tagged as implementing the inherited abstract method
//			if (!currentMethod.isAbstract() && inheritedMethod.isAbstract()) {
//				if ((currentMethod.modifiers & CompilerModifiers.AccOverriding) == 0)
//					currentMethod.modifiers |= CompilerModifiers.AccImplementing;
			} else if (inheritedMethod.isPublic() || !type.isInterface()) {
				// interface I { @Override Object clone(); } does not override Object#clone()
				currentMethod.modifiers |= ExtraCompilerModifiers.AccOverriding;
			}
	
			if (!areReturnTypesCompatible(currentMethod, inheritedMethod)
					&& (currentMethod.returnType.tagBits & TagBits.HasMissingType) == 0) {
				if (reportIncompatibleReturnTypeError(currentMethod, inheritedMethod))
					continue nextMethod;
			}
	
			if (currentMethod.thrownExceptions != Binding.NO_EXCEPTIONS)
				checkExceptions(currentMethod, inheritedMethod);
			if (inheritedMethod.isFinal())
				problemReporter(currentMethod).finalMethodCannotBeOverridden(currentMethod, inheritedMethod);
			if (!isAsVisible(currentMethod, inheritedMethod))
				problemReporter(currentMethod).visibilityConflict(currentMethod, inheritedMethod);
			if (options.reportDeprecationWhenOverridingDeprecatedMethod && inheritedMethod.isViewedAsDeprecated()) {
				if (!currentMethod.isViewedAsDeprecated() || options.reportDeprecationInsideDeprecatedCode) {
					// check against the other inherited methods to see if they hide this inheritedMethod
					ReferenceBinding declaringClass = inheritedMethod.declaringClass;
					if (declaringClass.isInterface())
						for (int j = length; --j >= 0;)
							if (i != j && methods[j].declaringClass.implementsInterface(declaringClass, false))
								continue nextMethod;
	
					problemReporter(currentMethod).overridesDeprecatedMethod(currentMethod, inheritedMethod);
				}
			}
		}
		checkForBridgeMethod(currentMethod, inheritedMethod, allInheritedMethods);
	}
}

void checkConcreteInheritedMethod(MethodBinding concreteMethod, MethodBinding[] abstractMethods) {
	// Remember that interfaces can only define public instance methods
	if (concreteMethod.isStatic())
		// Cannot inherit a static method which is specified as an instance method by an interface
		problemReporter().staticInheritedMethodConflicts(type, concreteMethod, abstractMethods);	
	if (!concreteMethod.isPublic()) {
		int index = 0, length = abstractMethods.length;
		if (concreteMethod.isProtected()) {
			for (; index < length; index++)
				if (abstractMethods[index].isPublic()) break;
		} else if (concreteMethod.isDefault()) {
			for (; index < length; index++)
				if (!abstractMethods[index].isDefault()) break;
		}
		if (index < length)
			problemReporter().inheritedMethodReducesVisibility(type, concreteMethod, abstractMethods);
	}
	if (concreteMethod.thrownExceptions != Binding.NO_EXCEPTIONS)
		for (int i = abstractMethods.length; --i >= 0;)
			checkExceptions(concreteMethod, abstractMethods[i]);
}

/*
"8.4.4"
Verify that newExceptions are all included in inheritedExceptions.
Assumes all exceptions are valid and throwable.
Unchecked exceptions (compatible with runtime & error) are ignored (see the spec on pg. 203).
*/
void checkExceptions(MethodBinding newMethod, MethodBinding inheritedMethod) {
	ReferenceBinding[] newExceptions = resolvedExceptionTypesFor(newMethod);
	ReferenceBinding[] inheritedExceptions = resolvedExceptionTypesFor(inheritedMethod);
	for (int i = newExceptions.length; --i >= 0;) {
		ReferenceBinding newException = newExceptions[i];
		int j = inheritedExceptions.length;
		while (--j > -1 && !isSameClassOrSubclassOf(newException, inheritedExceptions[j])){/*empty*/}
		if (j == -1)
			if (!newException.isUncheckedException(false)
					&& (newException.tagBits & TagBits.HasMissingType) == 0) {
				problemReporter(newMethod).incompatibleExceptionInThrowsClause(this.type, newMethod, inheritedMethod, newException);
			}
	}
}

void checkForBridgeMethod(MethodBinding currentMethod, MethodBinding inheritedMethod, MethodBinding[] allInheritedMethods) {
	// no op before 1.5
}

void checkForRedundantSuperinterfaces(ReferenceBinding superclass, ReferenceBinding[] superInterfaces) {
	if (superInterfaces == Binding.NO_SUPERINTERFACES) return;

	SimpleSet interfacesToCheck = new SimpleSet(superInterfaces.length);
	for (int i = 0, l = superInterfaces.length; i < l; i++)
		interfacesToCheck.add(superInterfaces[i]);
	ReferenceBinding[] itsInterfaces = null;
	SimpleSet inheritedInterfaces = new SimpleSet(5);
	ReferenceBinding superType = superclass;
	while (superType != null && superType.isValidBinding()) {
		if ((itsInterfaces = superType.superInterfaces()) != Binding.NO_SUPERINTERFACES) {
			for (int i = 0, l = itsInterfaces.length; i < l; i++) {
				ReferenceBinding inheritedInterface = itsInterfaces[i];
				if (!inheritedInterfaces.includes(inheritedInterface) && inheritedInterface.isValidBinding()) {
					if (interfacesToCheck.includes(inheritedInterface)) {
						TypeReference[] refs = this.type.scope.referenceContext.superInterfaces;
						for (int r = 0, rl = refs.length; r < rl; r++) {
							if (refs[r].resolvedType == inheritedInterface) {
								problemReporter().redundantSuperInterface(this.type, refs[r], inheritedInterface, superType);
								break;
							}
						}
					} else {
						inheritedInterfaces.add(inheritedInterface);
					}
				}
			}
		}
		superType = superType.superclass();
	}

	int nextPosition = inheritedInterfaces.elementSize;
	if (nextPosition == 0) return;
	ReferenceBinding[] interfacesToVisit = new ReferenceBinding[nextPosition];
	inheritedInterfaces.asArray(interfacesToVisit);
	for (int i = 0; i < nextPosition; i++) {
		superType = interfacesToVisit[i];
		if ((itsInterfaces = superType.superInterfaces()) != Binding.NO_SUPERINTERFACES) {
			int itsLength = itsInterfaces.length;
			if (nextPosition + itsLength >= interfacesToVisit.length)
				System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
			for (int a = 0; a < itsLength; a++) {
				ReferenceBinding inheritedInterface = itsInterfaces[a];
				if (!inheritedInterfaces.includes(inheritedInterface) && inheritedInterface.isValidBinding()) {
					if (interfacesToCheck.includes(inheritedInterface)) {
						TypeReference[] refs = this.type.scope.referenceContext.superInterfaces;
						for (int r = 0, rl = refs.length; r < rl; r++) {
							if (refs[r].resolvedType == inheritedInterface) {
								problemReporter().redundantSuperInterface(this.type, refs[r], inheritedInterface, superType);
								break;
							}
						}
					} else {
						inheritedInterfaces.add(inheritedInterface);
						interfacesToVisit[nextPosition++] = inheritedInterface;
					}
				}
			}
		}
	}
}

void checkInheritedMethods(MethodBinding[] methods, int length) {
	if (length > 1) {
		int[] overriddenInheritedMethods = findOverriddenInheritedMethods(methods, length);
		if (overriddenInheritedMethods != null) {
			// detected some overridden methods that can be ignored when checking return types
			// but cannot ignore an overridden inherited method completely when it comes to checking for bridge methods
			int index = 0;
			MethodBinding[] closestMethods = new MethodBinding[length];
			for (int i = 0; i < length; i++)
				if (overriddenInheritedMethods[i] == 0)
					closestMethods[index++] = methods[i];
			if (index > 1 && !checkInheritedReturnTypes(closestMethods, index))
				return;
		} else if (!checkInheritedReturnTypes(methods, length)) {
			return;
		}
	}

	MethodBinding concreteMethod = null;
	if (!type.isInterface()) {  // ignore concrete methods for interfaces
		for (int i = length; --i >= 0;) {  // Remember that only one of the methods can be non-abstract
			if (!methods[i].isAbstract()) {
				concreteMethod = methods[i];
				break;
			}
		}
	}
	if (concreteMethod == null) {
		if (!this.type.isAbstract()) {
			for (int i = length; --i >= 0;) {
				if (mustImplementAbstractMethod(methods[i].declaringClass)) {
					TypeDeclaration typeDeclaration = this.type.scope.referenceContext;
					if (typeDeclaration != null) {
						MethodDeclaration missingAbstractMethod = typeDeclaration.addMissingAbstractMethodFor(methods[0]);
						missingAbstractMethod.scope.problemReporter().abstractMethodMustBeImplemented(this.type, methods[0]);
					} else {
						problemReporter().abstractMethodMustBeImplemented(this.type, methods[0]);
					}
					return;
				}
			}
		}
		return;
	}

	if (length > 1) {
		MethodBinding[] abstractMethods = new MethodBinding[length - 1];
		int index = 0;
		for (int i = length; --i >= 0;)
			if (methods[i] != concreteMethod)
				abstractMethods[index++] = methods[i];
		checkConcreteInheritedMethod(concreteMethod, abstractMethods);
	}
}

boolean checkInheritedReturnTypes(MethodBinding[] methods, int length) {
	MethodBinding first = methods[0];
	int index = length;
	while (--index > 0 && areReturnTypesCompatible(first, methods[index])){/*empty*/}
	if (index == 0) 
		return true;

	// All inherited methods do NOT have the same vmSignature
	if (this.type.isInterface())
		for (int i = length; --i >= 0;)
			if (methods[i].declaringClass.id == TypeIds.T_JavaLangObject)
				return false; // do not complain since the super interface already got blamed
	problemReporter().inheritedMethodsHaveIncompatibleReturnTypes(this.type, methods, length);
	return false;
}
/*
For each inherited method identifier (message pattern - vm signature minus the return type)
	if current method exists
		if current's vm signature does not match an inherited signature then complain 
		else compare current's exceptions & visibility against each inherited method
	else
		if inherited methods = 1
			if inherited is abstract && type is NOT an interface or abstract, complain
		else
			if vm signatures do not match complain
			else
				find the concrete implementation amongst the abstract methods (can only be 1)
				if one exists then
					it must be a public instance method
					compare concrete's exceptions against each abstract method
				else
					complain about missing implementation only if type is NOT an interface or abstract
*/
void checkMethods() {
	boolean mustImplementAbstractMethods = mustImplementAbstractMethods();
	boolean skipInheritedMethods = mustImplementAbstractMethods && canSkipInheritedMethods(); // have a single concrete superclass so only check overridden methods
	char[][] methodSelectors = this.inheritedMethods.keyTable;
	nextSelector : for (int s = methodSelectors.length; --s >= 0;) {
		if (methodSelectors[s] == null) continue nextSelector;

		MethodBinding[] current = (MethodBinding[]) this.currentMethods.get(methodSelectors[s]);
		if (current == null && skipInheritedMethods)
			continue nextSelector;

		MethodBinding[] inherited = (MethodBinding[]) this.inheritedMethods.valueTable[s];
		if (inherited.length == 1 && current == null) { // handle the common case
			if (mustImplementAbstractMethods && inherited[0].isAbstract())
				checkAbstractMethod(inherited[0]);
			continue nextSelector;
		}

		int index = -1;
		MethodBinding[] matchingInherited = new MethodBinding[inherited.length];
		if (current != null) {
			for (int i = 0, length1 = current.length; i < length1; i++) {
				MethodBinding currentMethod = current[i];
				for (int j = 0, length2 = inherited.length; j < length2; j++) {
					MethodBinding inheritedMethod = computeSubstituteMethod(inherited[j], currentMethod);
					if (inheritedMethod != null) {
						if (isParameterSubsignature(currentMethod, inheritedMethod)) {
							matchingInherited[++index] = inheritedMethod;
							inherited[j] = null; // do not want to find it again
						}
					}
				}
				if (index >= 0) {
					checkAgainstInheritedMethods(currentMethod, matchingInherited, index + 1, inherited); // pass in the length of matching
					while (index >= 0) matchingInherited[index--] = null; // clear the contents of the matching methods
				}
			}
		}

		for (int i = 0, length = inherited.length; i < length; i++) {
			MethodBinding inheritedMethod = inherited[i];
			if (inheritedMethod == null) continue;

			matchingInherited[++index] = inheritedMethod;
			for (int j = i + 1; j < length; j++) {
				MethodBinding otherInheritedMethod = inherited[j];
				if (canSkipInheritedMethods(inheritedMethod, otherInheritedMethod))
					continue;
				otherInheritedMethod = computeSubstituteMethod(otherInheritedMethod, inheritedMethod);
				if (otherInheritedMethod != null) {
					if (isParameterSubsignature(inheritedMethod, otherInheritedMethod)) {
						matchingInherited[++index] = otherInheritedMethod;
						inherited[j] = null; // do not want to find it again
					}
				}
			}
			if (index == -1) continue;
			if (index > 0)
				checkInheritedMethods(matchingInherited, index + 1); // pass in the length of matching
			else if (mustImplementAbstractMethods && matchingInherited[0].isAbstract())
				checkAbstractMethod(matchingInherited[0]);
			while (index >= 0) matchingInherited[index--] = null; // clear the contents of the matching methods
		}
	}
}

void checkPackagePrivateAbstractMethod(MethodBinding abstractMethod) {
	// check that the inherited abstract method (package private visibility) is implemented within the same package
	PackageBinding necessaryPackage = abstractMethod.declaringClass.fPackage;
	if (necessaryPackage == this.type.fPackage) return; // not a problem

	ReferenceBinding superType = this.type.superclass();
	char[] selector = abstractMethod.selector;
	do {
		if (!superType.isValidBinding()) return;
		if (!superType.isAbstract()) return; // closer non abstract super type will be flagged instead

		if (necessaryPackage == superType.fPackage) {
			MethodBinding[] methods = superType.getMethods(selector);
			nextMethod : for (int m = methods.length; --m >= 0;) {
				MethodBinding method = methods[m];
				if (method.isPrivate() || method.isConstructor() || method.isDefaultAbstract())
					continue nextMethod;
				if (areMethodsCompatible(method, abstractMethod))
					return; // found concrete implementation of abstract method in same package
			}
		}
	} while ((superType = superType.superclass()) != abstractMethod.declaringClass);

	// non visible abstract methods cannot be overridden so the type must be defined abstract
	problemReporter().abstractMethodCannotBeOverridden(this.type, abstractMethod);
}

void computeInheritedMethods() {
	ReferenceBinding superclass = this.type.isInterface()
		? this.type.scope.getJavaLangObject() // check interface methods against Object
		: this.type.superclass(); // class or enum
	computeInheritedMethods(superclass, type.superInterfaces());
	checkForRedundantSuperinterfaces(superclass, type.superInterfaces());
}

/*
Binding creation is responsible for reporting:
	- all modifier problems (duplicates & multiple visibility modifiers + incompatible combinations)
		- plus invalid modifiers given the context... examples:
			- interface methods can only be public
			- abstract methods can only be defined by abstract classes
	- collisions... 2 methods with identical vmSelectors
	- multiple methods with the same message pattern but different return types
	- ambiguous, invisible or missing return/argument/exception types
	- check the type of any array is not void
	- check that each exception type is Throwable or a subclass of it
*/
void computeInheritedMethods(ReferenceBinding superclass, ReferenceBinding[] superInterfaces) {
	// only want to remember inheritedMethods that can have an impact on the current type
	// if an inheritedMethod has been 'replaced' by a supertype's method then skip it

	this.inheritedMethods = new HashtableOfObject(51); // maps method selectors to an array of methods... must search to match paramaters & return type
	ReferenceBinding[] interfacesToVisit = null;
	int nextPosition = 0;
	ReferenceBinding[] itsInterfaces = superInterfaces;
	if (itsInterfaces != Binding.NO_SUPERINTERFACES) {
		nextPosition = itsInterfaces.length;
		interfacesToVisit = itsInterfaces;
	}

	ReferenceBinding superType = superclass;
	HashtableOfObject nonVisibleDefaultMethods = new HashtableOfObject(3); // maps method selectors to an array of methods
	boolean allSuperclassesAreAbstract = true;

	while (superType != null && superType.isValidBinding()) {
	    if (allSuperclassesAreAbstract) {
		    if (superType.isAbstract()) {
				// only need to include superinterfaces if immediate superclasses are abstract
				if ((itsInterfaces = superType.superInterfaces()) != Binding.NO_SUPERINTERFACES) {
					if (interfacesToVisit == null) {
						interfacesToVisit = itsInterfaces;
						nextPosition = interfacesToVisit.length;
					} else {
						int itsLength = itsInterfaces.length;
						if (nextPosition + itsLength >= interfacesToVisit.length)
							System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
						nextInterface : for (int a = 0; a < itsLength; a++) {
							ReferenceBinding next = itsInterfaces[a];
							for (int b = 0; b < nextPosition; b++)
								if (next == interfacesToVisit[b]) continue nextInterface;
							interfacesToVisit[nextPosition++] = next;
						}
					}
				}
			} else {
			    allSuperclassesAreAbstract = false;
			}
		}

		MethodBinding[] methods = superType.unResolvedMethods();
		nextMethod : for (int m = methods.length; --m >= 0;) {
			MethodBinding inheritedMethod = methods[m];
			if (inheritedMethod.isPrivate() || inheritedMethod.isConstructor() || inheritedMethod.isDefaultAbstract())
				continue nextMethod;
			MethodBinding[] existingMethods = (MethodBinding[]) this.inheritedMethods.get(inheritedMethod.selector);
			if (existingMethods != null) {
				for (int i = 0, length = existingMethods.length; i < length; i++) {
					if (existingMethods[i].declaringClass != inheritedMethod.declaringClass && areMethodsCompatible(existingMethods[i], inheritedMethod)) {
						if (inheritedMethod.isDefault() && inheritedMethod.isAbstract())
							checkPackagePrivateAbstractMethod(inheritedMethod);
						continue nextMethod;
					}
				}
			}
			MethodBinding[] nonVisible = (MethodBinding[]) nonVisibleDefaultMethods.get(inheritedMethod.selector);
			if (nonVisible != null)
				for (int i = 0, l = nonVisible.length; i < l; i++)
					if (areMethodsCompatible(nonVisible[i], inheritedMethod))
						continue nextMethod;

			if (!inheritedMethod.isDefault() || inheritedMethod.declaringClass.fPackage == type.fPackage) {
				if (existingMethods == null) {
					existingMethods = new MethodBinding[] {inheritedMethod};
				} else {
					int length = existingMethods.length;
					System.arraycopy(existingMethods, 0, existingMethods = new MethodBinding[length + 1], 0, length);
					existingMethods[length] = inheritedMethod;
				}
				this.inheritedMethods.put(inheritedMethod.selector, existingMethods);
			} else {
				if (nonVisible == null) {
					nonVisible = new MethodBinding[] {inheritedMethod};
				} else {
					int length = nonVisible.length;
					System.arraycopy(nonVisible, 0, nonVisible = new MethodBinding[length + 1], 0, length);
					nonVisible[length] = inheritedMethod;
				}
				nonVisibleDefaultMethods.put(inheritedMethod.selector, nonVisible);

				if (inheritedMethod.isAbstract() && !this.type.isAbstract()) // non visible abstract methods cannot be overridden so the type must be defined abstract
					problemReporter().abstractMethodCannotBeOverridden(this.type, inheritedMethod);

				MethodBinding[] current = (MethodBinding[]) this.currentMethods.get(inheritedMethod.selector);
				if (current != null && !inheritedMethod.isStatic()) { // non visible methods cannot be overridden so a warning is issued
					foundMatch : for (int i = 0, length = current.length; i < length; i++) {
						if (!current[i].isStatic() && areMethodsCompatible(current[i], inheritedMethod)) {
							problemReporter().overridesPackageDefaultMethod(current[i], inheritedMethod);
							break foundMatch;
						}
					}
				}
			}
		}
		superType = superType.superclass();
	}
	if (nextPosition == 0) return;

	SimpleSet skip = findSuperinterfaceCollisions(superclass, superInterfaces);
	for (int i = 0; i < nextPosition; i++) {
		superType = interfacesToVisit[i];
		if (superType.isValidBinding()) {
			if (skip != null && skip.includes(superType)) continue;
			if ((itsInterfaces = superType.superInterfaces()) != Binding.NO_SUPERINTERFACES) {
				int itsLength = itsInterfaces.length;
				if (nextPosition + itsLength >= interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
				nextInterface : for (int a = 0; a < itsLength; a++) {
					ReferenceBinding next = itsInterfaces[a];
					for (int b = 0; b < nextPosition; b++)
						if (next == interfacesToVisit[b]) continue nextInterface;
					interfacesToVisit[nextPosition++] = next;
				}
			}

			MethodBinding[] methods = superType.unResolvedMethods();
			nextMethod : for (int m = methods.length; --m >= 0;) { // Interface methods are all abstract public
				MethodBinding inheritedMethod = methods[m];
				MethodBinding[] existingMethods = (MethodBinding[]) this.inheritedMethods.get(inheritedMethod.selector);
				if (existingMethods == null) {
					existingMethods = new MethodBinding[] {inheritedMethod};
				} else {
					int length = existingMethods.length;
					// look to see if any of the existingMethods implement this inheritedMethod
					for (int e = 0; e < length; e++)
						if (isInterfaceMethodImplemented(inheritedMethod, existingMethods[e], superType))
							continue nextMethod; // skip interface method with the same signature if visible to its declaringClass
					System.arraycopy(existingMethods, 0, existingMethods = new MethodBinding[length + 1], 0, length);
					existingMethods[length] = inheritedMethod;
				}
				this.inheritedMethods.put(inheritedMethod.selector, existingMethods);
			}
		}
	}
}

void computeMethods() {
	MethodBinding[] methods = type.methods();
	int size = methods.length;
	this.currentMethods = new HashtableOfObject(size == 0 ? 1 : size); // maps method selectors to an array of methods... must search to match paramaters & return type
	for (int m = size; --m >= 0;) {
		MethodBinding method = methods[m];
		if (!(method.isConstructor() || method.isDefaultAbstract())) { // keep all methods which are NOT constructors or default abstract
			MethodBinding[] existingMethods = (MethodBinding[]) this.currentMethods.get(method.selector);
			if (existingMethods == null)
				existingMethods = new MethodBinding[1];
			else
				System.arraycopy(existingMethods, 0,
					(existingMethods = new MethodBinding[existingMethods.length + 1]), 0, existingMethods.length - 1);
			existingMethods[existingMethods.length - 1] = method;
			this.currentMethods.put(method.selector, existingMethods);
		}
	}
}

MethodBinding computeSubstituteMethod(MethodBinding inheritedMethod, MethodBinding currentMethod) {
	if (inheritedMethod == null) return null;
	if (currentMethod.parameters.length != inheritedMethod.parameters.length) return null; // no match
	return inheritedMethod;
}

boolean couldMethodOverride(MethodBinding method, MethodBinding inheritedMethod) {
	if (!org.eclipse.jdt.core.compiler.CharOperation.equals(method.selector, inheritedMethod.selector))
		return false;
	if (method == inheritedMethod || method.isStatic() || inheritedMethod.isStatic())
		return false;
	if (inheritedMethod.isPrivate())
		return false;
	if (inheritedMethod.isDefault() && method.declaringClass.getPackage() != inheritedMethod.declaringClass.getPackage())
		return false;
	if (!method.isPublic()) { // inheritedMethod is either public or protected & method is less than public
		if (inheritedMethod.isPublic())
			return false;
		if (inheritedMethod.isProtected() && !method.isProtected())
			return false;
	}
	return true;
}

// Answer whether the method overrides the inheritedMethod
// Check the necessary visibility rules & inheritance from the inheritedMethod's declaringClass
// See isMethodSubsignature() for parameter comparisons
public boolean doesMethodOverride(MethodBinding method, MethodBinding inheritedMethod) {
	if (!couldMethodOverride(method, inheritedMethod))
		return false;

	inheritedMethod = inheritedMethod.original();
	TypeBinding match = method.declaringClass.findSuperTypeOriginatingFrom(inheritedMethod.declaringClass);
	if (!(match instanceof ReferenceBinding))
		return false; // method's declaringClass does not inherit from inheritedMethod's 

	return isParameterSubsignature(method, inheritedMethod);
}

SimpleSet findSuperinterfaceCollisions(ReferenceBinding superclass, ReferenceBinding[] superInterfaces) {
	return null; // noop in 1.4
}

int[] findOverriddenInheritedMethods(MethodBinding[] methods, int length) {
	// NOTE assumes length > 1
	// inherited methods are added as we walk up the superclass hierarchy, then each superinterface
	// so method[1] from a class can NOT override method[0], but methods from superinterfaces can
	// since superinterfaces can be added from different superclasses or other superinterfaces
	int[] toSkip = null;
	int i = 0;
	ReferenceBinding declaringClass = methods[i].declaringClass;
	if (!declaringClass.isInterface()) {
		// in the first pass, skip overridden methods from superclasses
		// only keep methods from the closest superclass, all others from higher superclasses can be skipped
		// NOTE: methods were added in order by walking up the superclass hierarchy
		ReferenceBinding declaringClass2 = methods[++i].declaringClass;
		while (declaringClass == declaringClass2) {
			if (++i == length) return null;
			declaringClass2 = methods[i].declaringClass;
		}
		if (!declaringClass2.isInterface()) {
			// skip all methods from different superclasses
			toSkip = new int[length];
			do {
				toSkip[i] = -1;
				if (++i == length) return toSkip;
				declaringClass2 = methods[i].declaringClass;
			} while (!declaringClass2.isInterface());
		}
	}
	// in the second pass, skip overridden methods from superinterfaces
	// NOTE: superinterfaces can appear in 'random' order
	nextMethod : for (; i < length; i++) {
		if (toSkip != null && toSkip[i] == -1) continue nextMethod;
		declaringClass = methods[i].declaringClass;
		for (int j = i + 1; j < length; j++) {
			if (toSkip != null && toSkip[j] == -1) continue;
			ReferenceBinding declaringClass2 = methods[j].declaringClass;
			if (declaringClass == declaringClass2) continue;
			if (declaringClass.implementsInterface(declaringClass2, true)) {
				if (toSkip == null)
					toSkip = new int[length];
				toSkip[j] = -1;
			} else if (declaringClass2.implementsInterface(declaringClass, true)) {
				if (toSkip == null)
					toSkip = new int[length];
				toSkip[i] = -1;
				continue nextMethod;
			}
		}
	}
	return toSkip;
}

boolean isAsVisible(MethodBinding newMethod, MethodBinding inheritedMethod) {
	if (inheritedMethod.modifiers == newMethod.modifiers) return true;

	if (newMethod.isPublic()) return true;		// Covers everything
	if (inheritedMethod.isPublic()) return false;

	if (newMethod.isProtected()) return true;
	if (inheritedMethod.isProtected()) return false;

	return !newMethod.isPrivate();		// The inheritedMethod cannot be private since it would not be visible
}

boolean isInterfaceMethodImplemented(MethodBinding inheritedMethod, MethodBinding existingMethod, ReferenceBinding superType) {
	// skip interface method with the same signature if visible to its declaringClass
	return areParametersEqual(existingMethod, inheritedMethod) && existingMethod.declaringClass.implementsInterface(superType, true);
}

public boolean isMethodSubsignature(MethodBinding method, MethodBinding inheritedMethod) {
	return org.eclipse.jdt.core.compiler.CharOperation.equals(method.selector, inheritedMethod.selector)
		&& isParameterSubsignature(method, inheritedMethod);
}

boolean isParameterSubsignature(MethodBinding method, MethodBinding inheritedMethod) {
	return areParametersEqual(method, inheritedMethod);
}

boolean isSameClassOrSubclassOf(ReferenceBinding testClass, ReferenceBinding superclass) {
	do {
		if (testClass == superclass) return true;
	} while ((testClass = testClass.superclass()) != null);
	return false;
}

boolean mustImplementAbstractMethod(ReferenceBinding declaringClass) {
	// if the type's superclass is an abstract class, then all abstract methods must be implemented
	// otherwise, skip it if the type's superclass must implement any of the inherited methods
	ReferenceBinding superclass = this.type.superclass();
	if (declaringClass.isClass()) {
		while (superclass.isAbstract() && superclass != declaringClass)
			superclass = superclass.superclass(); // find the first concrete superclass or the abstract declaringClass
	} else {
		if (this.type.implementsInterface(declaringClass, false)) {
			if (this.type.isAbstract()) return false; // leave it for the subclasses
			if (!superclass.implementsInterface(declaringClass, true)) // only if a superclass does not also implement the interface
				return true;
		}
		while (superclass.isAbstract() && !superclass.implementsInterface(declaringClass, false))
			superclass = superclass.superclass(); // find the first concrete superclass or the superclass which implements the interface
	}
	return superclass.isAbstract();		// if it is a concrete class then we have already reported problem against it
}

boolean mustImplementAbstractMethods() {
	return !this.type.isInterface() && !this.type.isAbstract();
}

ProblemReporter problemReporter() {
	return this.type.scope.problemReporter();
}

ProblemReporter problemReporter(MethodBinding currentMethod) {
	ProblemReporter reporter = problemReporter();
	if (currentMethod.declaringClass == type && currentMethod.sourceMethod() != null)	// only report against the currentMethod if its implemented by the type
		reporter.referenceContext = currentMethod.sourceMethod();
	return reporter;
}

/**
 * Return true and report an incompatibleReturnType error if currentMethod's
 * return type is strictly incompatible with inheritedMethod's, else return 
 * false and report an unchecked conversion warning. Do not call when
 * areReturnTypesCompatible(currentMethod, inheritedMethod) returns true.
 * @param currentMethod the (potentially) inheriting method
 * @param inheritedMethod the inherited method
 * @return true if currentMethod's return type is strictly incompatible with
 *         inheritedMethod's
 */
boolean reportIncompatibleReturnTypeError(MethodBinding currentMethod, MethodBinding inheritedMethod) {
	problemReporter(currentMethod).incompatibleReturnType(currentMethod, inheritedMethod);
	return true;
}

ReferenceBinding[] resolvedExceptionTypesFor(MethodBinding method) {
	ReferenceBinding[] exceptions = method.thrownExceptions;
	if ((method.modifiers & ExtraCompilerModifiers.AccUnresolved) == 0)
		return exceptions;

	if (!(method.declaringClass instanceof BinaryTypeBinding))
		return Binding.NO_EXCEPTIONS; // safety check

	for (int i = exceptions.length; --i >= 0;)
		exceptions[i] = BinaryTypeBinding.resolveType(exceptions[i], this.environment, true);
	return exceptions;
}

void verify(SourceTypeBinding someType) {
	this.type = someType;
	computeMethods();
	computeInheritedMethods();
	checkMethods();
}

public String toString() {
	StringBuffer buffer = new StringBuffer(10);
	buffer.append("MethodVerifier for type: "); //$NON-NLS-1$
	buffer.append(type.readableName());
	buffer.append('\n');
	buffer.append("\t-inherited methods: "); //$NON-NLS-1$
	buffer.append(this.inheritedMethods);
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2897.java