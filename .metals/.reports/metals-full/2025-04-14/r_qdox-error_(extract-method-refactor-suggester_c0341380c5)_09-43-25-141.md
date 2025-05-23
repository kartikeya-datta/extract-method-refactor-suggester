error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2864.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2864.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2864.java
text:
```scala
&@@& sourceType.isClass() // no need to check for enum, since implicitly static

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
package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfObject;

public class ClassScope extends Scope {
	public TypeDeclaration referenceContext;
	private TypeReference superTypeReference;

	private final static char[] IncompleteHierarchy = new char[] {'h', 'a', 's', ' ', 'i', 'n', 'c', 'o', 'n', 's', 'i', 's', 't', 'e', 'n', 't', ' ', 'h', 'i', 'e', 'r', 'a', 'r', 'c', 'h', 'y'};

	public ClassScope(Scope parent, TypeDeclaration context) {
		super(CLASS_SCOPE, parent);
		this.referenceContext = context;
	}
	
	void buildAnonymousTypeBinding(SourceTypeBinding enclosingType, ReferenceBinding supertype) {
		
		LocalTypeBinding anonymousType = buildLocalType(enclosingType, enclosingType.fPackage);

		SourceTypeBinding sourceType = referenceContext.binding;
		if (supertype.isInterface()) {
			sourceType.superclass = getJavaLangObject();
			sourceType.superInterfaces = new ReferenceBinding[] { supertype };
		} else {
			sourceType.superclass = supertype;
			sourceType.superInterfaces = TypeConstants.NoSuperInterfaces;
		}
		connectMemberTypes();
		buildFieldsAndMethods();
		anonymousType.faultInTypesForFieldsAndMethods();
		sourceType.verifyMethods(environment().methodVerifier());
	}
	
	private void buildFields() {
		boolean hierarchyIsInconsistent = referenceContext.binding.isHierarchyInconsistent();
		if (referenceContext.fields == null) {
			if (hierarchyIsInconsistent) { // 72468
				referenceContext.binding.fields = new FieldBinding[1];
				referenceContext.binding.fields[0] =
					new FieldBinding(IncompleteHierarchy, IntBinding, AccPrivate, referenceContext.binding, null);
			} else {
				referenceContext.binding.fields = NoFields;
			}
			return;
		}
		// count the number of fields vs. initializers
		FieldDeclaration[] fields = referenceContext.fields;
		int size = fields.length;
		int count = 0;
		for (int i = 0; i < size; i++) {
			switch (fields[i].getKind()) {
				case AbstractVariableDeclaration.FIELD:
				case AbstractVariableDeclaration.ENUM_CONSTANT:
					count++;
			}
		}

		if (hierarchyIsInconsistent)
			count++;
		// iterate the field declarations to create the bindings, lose all duplicates
		FieldBinding[] fieldBindings = new FieldBinding[count];
		HashtableOfObject knownFieldNames = new HashtableOfObject(count);
		boolean duplicate = false;
		count = 0;
		for (int i = 0; i < size; i++) {
			FieldDeclaration field = fields[i];
			if (field.getKind() == AbstractVariableDeclaration.INITIALIZER) {
				if (referenceContext.binding.isInterface())
					problemReporter().interfaceCannotHaveInitializers(referenceContext.binding, field);
			} else {
				FieldBinding fieldBinding = new FieldBinding(field, null, field.modifiers | AccUnresolved, referenceContext.binding);
				// field's type will be resolved when needed for top level types
				checkAndSetModifiersForField(fieldBinding, field);

				if (knownFieldNames.containsKey(field.name)) {
					duplicate = true;
					FieldBinding previousBinding = (FieldBinding) knownFieldNames.get(field.name);
					if (previousBinding != null) {
						for (int f = 0; f < i; f++) {
							FieldDeclaration previousField = fields[f];
							if (previousField.binding == previousBinding) {
								problemReporter().duplicateFieldInType(referenceContext.binding, previousField);
								previousField.binding = null;
								break;
							}
						}
					}
					knownFieldNames.put(field.name, null); // ensure that the duplicate field is found & removed
					problemReporter().duplicateFieldInType(referenceContext.binding, field);
					field.binding = null;
				} else {
					knownFieldNames.put(field.name, fieldBinding);
					// remember that we have seen a field with this name
					if (fieldBinding != null)
						fieldBindings[count++] = fieldBinding;
				}
			}
		}
		// remove duplicate fields
		if (duplicate) {
			FieldBinding[] newFieldBindings = new FieldBinding[fieldBindings.length];
			// we know we'll be removing at least 1 duplicate name
			size = count;
			count = 0;
			for (int i = 0; i < size; i++) {
				FieldBinding fieldBinding = fieldBindings[i];
				if (knownFieldNames.get(fieldBinding.name) != null)
					newFieldBindings[count++] = fieldBinding;
			}
			fieldBindings = newFieldBindings;
		}
		if (hierarchyIsInconsistent)
			fieldBindings[count++] = new FieldBinding(IncompleteHierarchy, IntBinding, AccPrivate, referenceContext.binding, null);

		if (count != fieldBindings.length)
			System.arraycopy(fieldBindings, 0, fieldBindings = new FieldBinding[count], 0, count);
		for (int i = 0; i < count; i++)
			fieldBindings[i].id = i;
		referenceContext.binding.fields = fieldBindings;
	}
	
	void buildFieldsAndMethods() {
		buildFields();
		buildMethods();

		SourceTypeBinding sourceType = referenceContext.binding;
		if (sourceType.isMemberType() && !sourceType.isLocalType())
			 ((MemberTypeBinding) sourceType).checkSyntheticArgsAndFields();

		ReferenceBinding[] memberTypes = sourceType.memberTypes;
		for (int i = 0, length = memberTypes.length; i < length; i++)
			 ((SourceTypeBinding) memberTypes[i]).scope.buildFieldsAndMethods();
	}
	
	private LocalTypeBinding buildLocalType(
		SourceTypeBinding enclosingType,
		PackageBinding packageBinding) {
	    
		referenceContext.scope = this;
		referenceContext.staticInitializerScope = new MethodScope(this, referenceContext, true);
		referenceContext.initializerScope = new MethodScope(this, referenceContext, false);

		// build the binding or the local type
		LocalTypeBinding localType = new LocalTypeBinding(this, enclosingType, this.innermostSwitchCase());
		referenceContext.binding = localType;
		checkAndSetModifiers();
		buildTypeVariables();
		
		// Look at member types
		ReferenceBinding[] memberTypeBindings = NoMemberTypes;
		if (referenceContext.memberTypes != null) {
			int size = referenceContext.memberTypes.length;
			memberTypeBindings = new ReferenceBinding[size];
			int count = 0;
			nextMember : for (int i = 0; i < size; i++) {
				TypeDeclaration memberContext = referenceContext.memberTypes[i];
				if (memberContext.kind() == IGenericType.INTERFACE_DECL) {
					problemReporter().nestedClassCannotDeclareInterface(memberContext);
					continue nextMember;
				}
				ReferenceBinding type = localType;
				// check that the member does not conflict with an enclosing type
				do {
					if (CharOperation.equals(type.sourceName, memberContext.name)) {
						problemReporter().hidingEnclosingType(memberContext);
						continue nextMember;
					}
					type = type.enclosingType();
				} while (type != null);
				// check the member type does not conflict with another sibling member type
				for (int j = 0; j < i; j++) {
					if (CharOperation.equals(referenceContext.memberTypes[j].name, memberContext.name)) {
						problemReporter().duplicateNestedType(memberContext);
						continue nextMember;
					}
				}
				ClassScope memberScope = new ClassScope(this, referenceContext.memberTypes[i]);
				LocalTypeBinding memberBinding = memberScope.buildLocalType(localType, packageBinding);
				memberBinding.setAsMemberType();
				memberTypeBindings[count++] = memberBinding;
			}
			if (count != size)
				System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
		}
		localType.memberTypes = memberTypeBindings;
		return localType;
	}
	
	void buildLocalTypeBinding(SourceTypeBinding enclosingType) {

		LocalTypeBinding localType = buildLocalType(enclosingType, enclosingType.fPackage);
		connectTypeHierarchy();
		buildFieldsAndMethods();
		localType.faultInTypesForFieldsAndMethods();

		referenceContext.binding.verifyMethods(environment().methodVerifier());
	}
	
	private void buildMemberTypes(AccessRestriction accessRestriction) {
	    SourceTypeBinding sourceType = referenceContext.binding;
		ReferenceBinding[] memberTypeBindings = NoMemberTypes;
		if (referenceContext.memberTypes != null) {
			int length = referenceContext.memberTypes.length;
			memberTypeBindings = new ReferenceBinding[length];
			int count = 0;
			nextMember : for (int i = 0; i < length; i++) {
				TypeDeclaration memberContext = referenceContext.memberTypes[i];
				if (memberContext.kind() == IGenericType.INTERFACE_DECL
					&& sourceType.isNestedType()
					&& sourceType.isClass()
					&& !sourceType.isStatic()) {
					problemReporter().nestedClassCannotDeclareInterface(memberContext);
					continue nextMember;
				}
				ReferenceBinding type = sourceType;
				// check that the member does not conflict with an enclosing type
				do {
					if (CharOperation.equals(type.sourceName, memberContext.name)) {
						problemReporter().hidingEnclosingType(memberContext);
						continue nextMember;
					}
					type = type.enclosingType();
				} while (type != null);
				// check that the member type does not conflict with another sibling member type
				for (int j = 0; j < i; j++) {
					if (CharOperation.equals(referenceContext.memberTypes[j].name, memberContext.name)) {
						problemReporter().duplicateNestedType(memberContext);
						continue nextMember;
					}
				}

				ClassScope memberScope = new ClassScope(this, memberContext);
				memberTypeBindings[count++] = memberScope.buildType(sourceType, sourceType.fPackage, accessRestriction);
			}
			if (count != length)
				System.arraycopy(memberTypeBindings, 0, memberTypeBindings = new ReferenceBinding[count], 0, count);
		}
		sourceType.memberTypes = memberTypeBindings;
	}
	
	private void buildMethods() {
		boolean isEnum = referenceContext.kind() == IGenericType.ENUM_DECL;
		if (referenceContext.methods == null && !isEnum) {
			referenceContext.binding.methods = NoMethods;
			return;
		}

		// iterate the method declarations to create the bindings
		AbstractMethodDeclaration[] methods = referenceContext.methods;
		int size = methods == null ? 0 : methods.length;
		// look for <clinit> method
		int clinitIndex = -1;
		for (int i = 0; i < size; i++) {
			if (methods[i].isClinit()) {
				clinitIndex = i;
				break;
			}
		}

		int count = isEnum ? 2 : 0; // reserve 2 slots for special enum methods: #values() and #valueOf(String)
		MethodBinding[] methodBindings = new MethodBinding[(clinitIndex == -1 ? size : size - 1) + count];
		// create special methods for enums
		if (isEnum) {
		    SourceTypeBinding sourceType = referenceContext.binding;
			methodBindings[0] = sourceType.addSyntheticEnumMethod(TypeConstants.VALUES); // add <EnumType>[] values() 
			methodBindings[1] = sourceType.addSyntheticEnumMethod(TypeConstants.VALUEOF); // add <EnumType> valueOf() 
		}
		// create bindings for source methods
		for (int i = 0; i < size; i++) {
			if (i != clinitIndex) {
				MethodScope scope = new MethodScope(this, methods[i], false);
				MethodBinding methodBinding = scope.createMethod(methods[i]);
				if (methodBinding != null) // is null if binding could not be created
					methodBindings[count++] = methodBinding;
			}
		}
		if (count != methodBindings.length)
			System.arraycopy(methodBindings, 0, methodBindings = new MethodBinding[count], 0, count);

		referenceContext.binding.methods = methodBindings;
	}
	
	SourceTypeBinding buildType(SourceTypeBinding enclosingType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
		// provide the typeDeclaration with needed scopes
		referenceContext.scope = this;
		referenceContext.staticInitializerScope = new MethodScope(this, referenceContext, true);
		referenceContext.initializerScope = new MethodScope(this, referenceContext, false);

		if (enclosingType == null) {
			char[][] className = CharOperation.arrayConcat(packageBinding.compoundName, referenceContext.name);
			referenceContext.binding = new SourceTypeBinding(className, packageBinding, this);
		} else {
			char[][] className = CharOperation.deepCopy(enclosingType.compoundName);
			className[className.length - 1] =
				CharOperation.concat(className[className.length - 1], referenceContext.name, '$');
			referenceContext.binding = new MemberTypeBinding(className, this, enclosingType);
		}

		SourceTypeBinding sourceType = referenceContext.binding;
		environment().setAccessRestriction(sourceType, accessRestriction);		
		sourceType.fPackage.addType(sourceType);
		checkAndSetModifiers();
		buildTypeVariables();
		buildMemberTypes(accessRestriction);
		return sourceType;
	}
	
	private void buildTypeVariables() {
	    
	    SourceTypeBinding sourceType = referenceContext.binding;
		TypeParameter[] typeParameters = referenceContext.typeParameters;
		
	    // do not construct type variables if source < 1.5
		if (typeParameters == null || environment().options.sourceLevel < ClassFileConstants.JDK1_5) {
		    sourceType.typeVariables = NoTypeVariables;
		    return;
		}
		sourceType.typeVariables = NoTypeVariables; // safety

		if (sourceType.id == T_JavaLangObject) { // handle the case of redefining java.lang.Object up front
			problemReporter().objectCannotBeGeneric(referenceContext);
			return; 
		}
		sourceType.typeVariables = createTypeVariables(typeParameters, sourceType);
		sourceType.modifiers |= AccGenericSignature;
	}
	
	private void checkAndSetModifiers() {
		SourceTypeBinding sourceType = referenceContext.binding;
		int modifiers = sourceType.modifiers;
		if ((modifiers & AccAlternateModifierProblem) != 0)
			problemReporter().duplicateModifierForType(sourceType);
		ReferenceBinding enclosingType = sourceType.enclosingType();
		boolean isMemberType = sourceType.isMemberType();
		if (isMemberType) {
			// checks for member types before local types to catch local members
			if (enclosingType.isStrictfp())
				modifiers |= AccStrictfp;
			if (enclosingType.isInterface())
				modifiers |= AccPublic;
			if (sourceType.isEnum()) {
				if (!enclosingType.isStatic())
					problemReporter().nonStaticContextForEnumMemberType(sourceType);
				else
					modifiers |= AccStatic;
			}
		} else if (sourceType.isLocalType()) {
			if (sourceType.isEnum()) {
				problemReporter().illegalLocalTypeDeclaration(referenceContext);
				sourceType.modifiers = 0;
				return;
			}
			if (sourceType.isAnonymousType()) {
			    modifiers |= AccFinal;
			    // set AccEnum flag for anonymous body of enum constants
			    if (referenceContext.allocation.type == null)
			    	modifiers |= AccEnum;
			}
			Scope scope = this;
			do {
				switch (scope.kind) {
					case METHOD_SCOPE :
						MethodScope methodScope = (MethodScope) scope;
						if (methodScope.isInsideInitializer()) {
							SourceTypeBinding type = ((TypeDeclaration) methodScope.referenceContext).binding;
			
							// inside field declaration ? check field modifier to see if deprecated
							if (methodScope.initializedField != null) {
									// currently inside this field initialization
								if (methodScope.initializedField.isViewedAsDeprecated() && !sourceType.isDeprecated())
									modifiers |= AccDeprecatedImplicitly;
							} else {
								if (type.isStrictfp())
									modifiers |= AccStrictfp;
								if (type.isViewedAsDeprecated() && !sourceType.isDeprecated()) 
									modifiers |= AccDeprecatedImplicitly;
							}					
						} else {
							MethodBinding method = ((AbstractMethodDeclaration) methodScope.referenceContext).binding;
							if (method != null) {
								if (method.isStrictfp())
									modifiers |= AccStrictfp;
								if (method.isViewedAsDeprecated() && !sourceType.isDeprecated())
									modifiers |= AccDeprecatedImplicitly;
							}
						}
						break;
					case CLASS_SCOPE :
						// local member
						if (enclosingType.isStrictfp())
							modifiers |= AccStrictfp;
						if (enclosingType.isViewedAsDeprecated() && !sourceType.isDeprecated())
							modifiers |= AccDeprecatedImplicitly;
						break;
				}
				scope = scope.parent;
			} while (scope != null);
		}

		// after this point, tests on the 16 bits reserved.
		int realModifiers = modifiers & AccJustFlag;

		if ((realModifiers & AccInterface) != 0) { // interface and annotation type
			// detect abnormal cases for interfaces
			if (isMemberType) {
				int unexpectedModifiers =
					~(AccPublic | AccPrivate | AccProtected | AccStatic | AccAbstract | AccInterface | AccStrictfp | AccAnnotation);
				if ((realModifiers & unexpectedModifiers) != 0) {
					if ((realModifiers & AccAnnotation) != 0)
						problemReporter().illegalModifierForAnnotationMemberType(sourceType);
					else
						problemReporter().illegalModifierForMemberInterface(sourceType);
				}
				/*
				} else if (sourceType.isLocalType()) { //interfaces cannot be defined inside a method
					int unexpectedModifiers = ~(AccAbstract | AccInterface | AccStrictfp);
					if ((realModifiers & unexpectedModifiers) != 0)
						problemReporter().illegalModifierForLocalInterface(sourceType);
				*/
			} else {
				int unexpectedModifiers = ~(AccPublic | AccAbstract | AccInterface | AccStrictfp | AccAnnotation);
				if ((realModifiers & unexpectedModifiers) != 0) {
					if ((realModifiers & AccAnnotation) != 0)
						problemReporter().illegalModifierForAnnotationType(sourceType);
					else
						problemReporter().illegalModifierForInterface(sourceType);
				}
			}
			modifiers |= AccAbstract;
		} else if ((realModifiers & AccEnum) != 0) {
			// detect abnormal cases for enums
			if (isMemberType) { // includes member types defined inside local types
				int unexpectedModifiers = ~(AccPublic | AccPrivate | AccProtected | AccStatic | AccStrictfp | AccEnum);
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForMemberEnum(sourceType);
			} else if (sourceType.isLocalType()) { // each enum constant is an anonymous local type
				int unexpectedModifiers = ~(AccStrictfp | AccFinal | AccEnum); // add final since implicitly set for anonymous type
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForLocalEnum(sourceType);
			} else {
				int unexpectedModifiers = ~(AccPublic | AccStrictfp | AccEnum);
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForEnum(sourceType);
			}

// what about inherited interface methods?
			if ((referenceContext.bits & ASTNode.HasAbstractMethods) != 0)
				modifiers |= AccAbstract;
		} else {
			// detect abnormal cases for classes
			if (isMemberType) { // includes member types defined inside local types
				int unexpectedModifiers = ~(AccPublic | AccPrivate | AccProtected | AccStatic | AccAbstract | AccFinal | AccStrictfp);
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForMemberClass(sourceType);
			} else if (sourceType.isLocalType()) {
				int unexpectedModifiers = ~(AccAbstract | AccFinal | AccStrictfp);
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForLocalClass(sourceType);
			} else {
				int unexpectedModifiers = ~(AccPublic | AccAbstract | AccFinal | AccStrictfp);
				if ((realModifiers & unexpectedModifiers) != 0)
					problemReporter().illegalModifierForClass(sourceType);
			}

			// check that Final and Abstract are not set together
			if ((realModifiers & (AccFinal | AccAbstract)) == (AccFinal | AccAbstract))
				problemReporter().illegalModifierCombinationFinalAbstractForClass(sourceType);
		}

		if (isMemberType) {
			// test visibility modifiers inconsistency, isolate the accessors bits
			if (enclosingType.isInterface()) {
				if ((realModifiers & (AccProtected | AccPrivate)) != 0) {
					problemReporter().illegalVisibilityModifierForInterfaceMemberType(sourceType);

					// need to keep the less restrictive
					if ((realModifiers & AccProtected) != 0)
						modifiers &= ~AccProtected;
					if ((realModifiers & AccPrivate) != 0)
						modifiers &= ~AccPrivate;
				}
			} else {
				int accessorBits = realModifiers & (AccPublic | AccProtected | AccPrivate);
				if ((accessorBits & (accessorBits - 1)) > 1) {
					problemReporter().illegalVisibilityModifierCombinationForMemberType(sourceType);

					// need to keep the less restrictive so disable Protected/Private as necessary
					if ((accessorBits & AccPublic) != 0) {
						if ((accessorBits & AccProtected) != 0)
							modifiers &= ~AccProtected;
						if ((accessorBits & AccPrivate) != 0)
							modifiers &= ~AccPrivate;
					} else if ((accessorBits & AccProtected) != 0 && (accessorBits & AccPrivate) != 0) {
						modifiers &= ~AccPrivate;
					}
				}
			}

			// static modifier test
			if ((realModifiers & AccStatic) == 0) {
				if (enclosingType.isInterface())
					modifiers |= AccStatic;
			} else if (!enclosingType.isStatic()) {
				// error the enclosing type of a static field must be static or a top-level type
				problemReporter().illegalStaticModifierForMemberType(sourceType);
			}
		}

		sourceType.modifiers = modifiers;
	}
	
	/* This method checks the modifiers of a field.
	*
	* 9.3 & 8.3
	* Need to integrate the check for the final modifiers for nested types
	*
	* Note : A scope is accessible by : fieldBinding.declaringClass.scope
	*/
	private void checkAndSetModifiersForField(FieldBinding fieldBinding, FieldDeclaration fieldDecl) {
		int modifiers = fieldBinding.modifiers;
		final ReferenceBinding declaringClass = fieldBinding.declaringClass;
		if ((modifiers & AccAlternateModifierProblem) != 0)
			problemReporter().duplicateModifierForField(declaringClass, fieldDecl);

		if (declaringClass.isInterface()) {
			int expectedValue = AccPublic | AccStatic | AccFinal;
			// set the modifiers
			modifiers |= expectedValue;

			// and then check that they are the only ones
			if ((modifiers & AccJustFlag) != expectedValue) {
				if ((declaringClass.modifiers  & AccAnnotation) != 0)
					problemReporter().illegalModifierForAnnotationField(fieldDecl);
				else
					problemReporter().illegalModifierForInterfaceField(fieldDecl);
			}
			fieldBinding.modifiers = modifiers;
			return;
		} else if (fieldDecl.getKind() == AbstractVariableDeclaration.ENUM_CONSTANT) {
			// check that they are not modifiers in source
			if ((modifiers & AccJustFlag) != 0)
				problemReporter().illegalModifierForEnumConstant(declaringClass, fieldDecl);
		
			// set the modifiers
			int implicitValue = AccPublic | AccStatic | AccFinal | AccEnum;
			fieldBinding.modifiers|= implicitValue;
			return;
		}

		// after this point, tests on the 16 bits reserved.
		int realModifiers = modifiers & AccJustFlag;
		int unexpectedModifiers = ~(AccPublic | AccPrivate | AccProtected | AccFinal | AccStatic | AccTransient | AccVolatile);
		if ((realModifiers & unexpectedModifiers) != 0) {
			problemReporter().illegalModifierForField(declaringClass, fieldDecl);
			modifiers &= ~AccJustFlag | ~unexpectedModifiers;
		}

		int accessorBits = realModifiers & (AccPublic | AccProtected | AccPrivate);
		if ((accessorBits & (accessorBits - 1)) > 1) {
			problemReporter().illegalVisibilityModifierCombinationForField(declaringClass, fieldDecl);

			// need to keep the less restrictive so disable Protected/Private as necessary
			if ((accessorBits & AccPublic) != 0) {
				if ((accessorBits & AccProtected) != 0)
					modifiers &= ~AccProtected;
				if ((accessorBits & AccPrivate) != 0)
					modifiers &= ~AccPrivate;
			} else if ((accessorBits & AccProtected) != 0 && (accessorBits & AccPrivate) != 0) {
				modifiers &= ~AccPrivate;
			}
		}

		if ((realModifiers & (AccFinal | AccVolatile)) == (AccFinal | AccVolatile))
			problemReporter().illegalModifierCombinationFinalVolatileForField(declaringClass, fieldDecl);

		if (fieldDecl.initialization == null && (modifiers & AccFinal) != 0)
			modifiers |= AccBlankFinal;
		fieldBinding.modifiers = modifiers;
	}

	private void checkForInheritedMemberTypes(SourceTypeBinding sourceType) {
		// search up the hierarchy of the sourceType to see if any superType defines a member type
		// when no member types are defined, tag the sourceType & each superType with the HasNoMemberTypes bit
		// assumes super types have already been checked & tagged
		ReferenceBinding currentType = sourceType;
		ReferenceBinding[][] interfacesToVisit = null;
		int lastPosition = -1;
		do {
			if (currentType.hasMemberTypes()) // avoid resolving member types eagerly
				return;

			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				if (interfacesToVisit == null)
					interfacesToVisit = new ReferenceBinding[5][];
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
		} while ((currentType = currentType.superclass()) != null && (currentType.tagBits & HasNoMemberTypes) == 0);

		if (interfacesToVisit != null) {
			// contains the interfaces between the sourceType and any superclass, which was tagged as having no member types
			boolean needToTag = false;
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++) {
					ReferenceBinding anInterface = interfaces[j];
					if ((anInterface.tagBits & HasNoMemberTypes) == 0) { // skip interface if it already knows it has no member types
						if (anInterface.hasMemberTypes()) // avoid resolving member types eagerly
							return;

						needToTag = true;
						ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
						if (itsInterfaces != NoSuperInterfaces) {
							if (++lastPosition == interfacesToVisit.length)
								System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
							interfacesToVisit[lastPosition] = itsInterfaces;
						}
					}
				}
			}

			if (needToTag) {
				for (int i = 0; i <= lastPosition; i++) {
					ReferenceBinding[] interfaces = interfacesToVisit[i];
					for (int j = 0, length = interfaces.length; j < length; j++)
						interfaces[j].tagBits |= HasNoMemberTypes;
				}
			}
		}

		// tag the sourceType and all of its superclasses, unless they have already been tagged
		currentType = sourceType;
		do {
			currentType.tagBits |= HasNoMemberTypes;
		} while ((currentType = currentType.superclass()) != null && (currentType.tagBits & HasNoMemberTypes) == 0);
	}
	// Perform deferred bound checks for parameterized type references (only done after hierarchy is connected)
	private void  checkParameterizedTypeBounds() {
		TypeReference superclass = referenceContext.superclass;
		if (superclass != null) {
			superclass.checkBounds(this);
		}
		TypeReference[] superinterfaces = referenceContext.superInterfaces;
		if (superinterfaces != null) {
			for (int i = 0, length = superinterfaces.length; i < length; i++) {
				superinterfaces[i].checkBounds(this);
			}
		}
		TypeParameter[] typeParameters = referenceContext.typeParameters;
		if (typeParameters != null) {
			for (int i = 0, paramLength = typeParameters.length; i < paramLength; i++) {
				typeParameters[i].checkBounds(this);
			}
		}
	}

	private void connectMemberTypes() {
		SourceTypeBinding sourceType = referenceContext.binding;
		ReferenceBinding[] memberTypes = sourceType.memberTypes;
		if (memberTypes != null && memberTypes != NoMemberTypes) {
			for (int i = 0, size = memberTypes.length; i < size; i++)
				 ((SourceTypeBinding) memberTypes[i]).scope.connectTypeHierarchy();
		}
	}
	/*
		Our current belief based on available JCK tests is:
			inherited member types are visible as a potential superclass.
			inherited interfaces are not visible when defining a superinterface.
	
		Error recovery story:
			ensure the superclass is set to java.lang.Object if a problem is detected
			resolving the superclass.
	
		Answer false if an error was reported against the sourceType.
	*/
	private boolean connectSuperclass() {
		SourceTypeBinding sourceType = referenceContext.binding;
		if (sourceType.id == T_JavaLangObject) { // handle the case of redefining java.lang.Object up front
			sourceType.superclass = null;
			sourceType.superInterfaces = NoSuperInterfaces;
			if (!sourceType.isClass())
				problemReporter().objectMustBeClass(sourceType);
			if (referenceContext.superclass != null || (referenceContext.superInterfaces != null && referenceContext.superInterfaces.length > 0))
				problemReporter().objectCannotHaveSuperTypes(sourceType);
			return true; // do not propagate Object's hierarchy problems down to every subtype
		}
		if (referenceContext.superclass == null) {
			if (sourceType.isEnum() && environment().options.sourceLevel >= JDK1_5) // do not connect if source < 1.5 as enum already got flagged as syntax error
				return connectEnumSuperclass();
			sourceType.superclass = getJavaLangObject();
			return !detectHierarchyCycle(sourceType, sourceType.superclass, null);
		}
		TypeReference superclassRef = referenceContext.superclass;
		ReferenceBinding superclass = findSupertype(superclassRef);
		if (superclass != null) { // is null if a cycle was detected cycle or a problem
			if (!superclass.isClass()) {
				problemReporter().superclassMustBeAClass(sourceType, superclassRef, superclass);
			} else if (superclass.isFinal()) {
				problemReporter().classExtendFinalClass(sourceType, superclassRef, superclass);
			} else if ((superclass.tagBits & TagBits.HasDirectWildcard) != 0) {
				problemReporter().superTypeCannotUseWildcard(sourceType, superclassRef, superclass);
			} else if (superclass.erasure().id == T_JavaLangEnum) {
				problemReporter().cannotExtendEnum(sourceType, superclassRef, superclass);
			} else {
				// only want to reach here when no errors are reported
				sourceType.superclass = superclass;
				return true;
			}
		}
		sourceType.tagBits |= HierarchyHasProblems;
		sourceType.superclass = getJavaLangObject();
		if ((sourceType.superclass.tagBits & BeginHierarchyCheck) == 0)
			detectHierarchyCycle(sourceType, sourceType.superclass, null);
		return false; // reported some error against the source type
	}

	/**
	 *  enum X (implicitly) extends Enum<X>
	 */
	private boolean connectEnumSuperclass() {
		SourceTypeBinding sourceType = referenceContext.binding;
		ReferenceBinding rootEnumType = getJavaLangEnum();
		boolean foundCycle = detectHierarchyCycle(sourceType, rootEnumType, null);
		// arity check for well-known Enum<E>
		TypeVariableBinding[] refTypeVariables = rootEnumType.typeVariables();
		if (refTypeVariables == NoTypeVariables) { // check generic
			problemReporter().nonGenericTypeCannotBeParameterized(null, rootEnumType, new TypeBinding[]{ sourceType });
			return false; // cannot reach here as AbortCompilation is thrown
		} else if (1 != refTypeVariables.length) { // check arity
			problemReporter().incorrectArityForParameterizedType(null, rootEnumType, new TypeBinding[]{ sourceType });
			return false; // cannot reach here as AbortCompilation is thrown
		}			
		// check argument type compatibility
		ParameterizedTypeBinding  superType = createParameterizedType(rootEnumType, new TypeBinding[]{ sourceType } , null);
		sourceType.superclass = superType;
		// bound check
		if (!refTypeVariables[0].boundCheck(superType, sourceType)) {
			problemReporter().typeMismatchError(rootEnumType, refTypeVariables[0], sourceType, null);
		}
		return !foundCycle;
	}

	/*
		Our current belief based on available JCK 1.3 tests is:
			inherited member types are visible as a potential superclass.
			inherited interfaces are visible when defining a superinterface.
	
		Error recovery story:
			ensure the superinterfaces contain only valid visible interfaces.
	
		Answer false if an error was reported against the sourceType.
	*/
	private boolean connectSuperInterfaces() {
		SourceTypeBinding sourceType = referenceContext.binding;
		sourceType.superInterfaces = NoSuperInterfaces;
		if (referenceContext.superInterfaces == null) {
			if (sourceType.isAnnotationType() && environment().options.sourceLevel >= JDK1_5) { // do not connect if source < 1.5 as annotation already got flagged as syntax error) {
				ReferenceBinding annotationType = getJavaLangAnnotationAnnotation();
				boolean foundCycle = detectHierarchyCycle(sourceType, annotationType, null);
				sourceType.superInterfaces = new ReferenceBinding[] { annotationType };
				return !foundCycle;
			}
			return true;
		}
		if (sourceType.id == T_JavaLangObject) // already handled the case of redefining java.lang.Object
			return true;

		boolean noProblems = true;
		int length = referenceContext.superInterfaces.length;
		ReferenceBinding[] interfaceBindings = new ReferenceBinding[length];
		int count = 0;
		nextInterface : for (int i = 0; i < length; i++) {
		    TypeReference superInterfaceRef = referenceContext.superInterfaces[i];
			ReferenceBinding superInterface = findSupertype(superInterfaceRef);
			if (superInterface == null) { // detected cycle
				sourceType.tagBits |= HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			}
			superInterfaceRef.resolvedType = superInterface; // hold onto the problem type
			// Check for a duplicate interface once the name is resolved, otherwise we may be confused (ie : a.b.I and c.d.I)
			for (int k = 0; k < count; k++) {
				if (interfaceBindings[k].erasure() == superInterface.erasure()) {
					problemReporter().duplicateSuperinterface(sourceType, referenceContext, (ReferenceBinding) superInterface.erasure());
					continue nextInterface;
				}
			}
			if (!superInterface.isInterface()) {
				problemReporter().superinterfaceMustBeAnInterface(sourceType, superInterfaceRef, superInterface);
				sourceType.tagBits |= HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			} else if (superInterface.isAnnotationType()){
				problemReporter().annotationTypeUsedAsSuperinterface(sourceType, superInterfaceRef, superInterface);
			}
			if ((superInterface.tagBits & TagBits.HasDirectWildcard) != 0) {
				problemReporter().superTypeCannotUseWildcard(sourceType, superInterfaceRef, superInterface);
				sourceType.tagBits |= HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			}
			ReferenceBinding invalid = findAmbiguousInterface(superInterface, sourceType);
			if (invalid != null) {
				ReferenceBinding generic = null;
				if (superInterface.isParameterizedType())
					generic = ((ParameterizedTypeBinding) superInterface).type;
				else if (invalid.isParameterizedType())
					generic = ((ParameterizedTypeBinding) invalid).type;
				problemReporter().superinterfacesCollide(generic, referenceContext, superInterface, invalid);
				sourceType.tagBits |= HierarchyHasProblems;
				noProblems = false;
				continue nextInterface;
			}

			// only want to reach here when no errors are reported
			interfaceBindings[count++] = superInterface;
		}
		// hold onto all correctly resolved superinterfaces
		if (count > 0) {
			if (count != length)
				System.arraycopy(interfaceBindings, 0, interfaceBindings = new ReferenceBinding[count], 0, count);
			sourceType.superInterfaces = interfaceBindings;
		}
		return noProblems;
	}
	
	void connectTypeHierarchy() {
		SourceTypeBinding sourceType = referenceContext.binding;
		if ((sourceType.tagBits & BeginHierarchyCheck) == 0) {
			sourceType.tagBits |= BeginHierarchyCheck;
			boolean noProblems = connectSuperclass();
			noProblems &= connectSuperInterfaces();
			noProblems &= connectTypeVariables(referenceContext.typeParameters);
			sourceType.tagBits |= EndHierarchyCheck;
			if (noProblems && sourceType.isHierarchyInconsistent())
				problemReporter().hierarchyHasProblems(sourceType);
		}
		// Perform deferred bound checks for parameterized type references (only done after hierarchy is connected)
		checkParameterizedTypeBounds();
		connectMemberTypes();
		try {
			checkForInheritedMemberTypes(sourceType);
		} catch (AbortCompilation e) {
			e.updateContext(referenceContext, referenceCompilationUnit().compilationResult);
			throw e;
		}
	}
	
	private void connectTypeHierarchyWithoutMembers() {
		// must ensure the imports are resolved
		if (parent instanceof CompilationUnitScope) {
			if (((CompilationUnitScope) parent).imports == null)
				 ((CompilationUnitScope) parent).checkAndSetImports();
		} else if (parent instanceof ClassScope) {
			// ensure that the enclosing type has already been checked
			 ((ClassScope) parent).connectTypeHierarchyWithoutMembers();
		}

		// double check that the hierarchy search has not already begun...
		SourceTypeBinding sourceType = referenceContext.binding;
		if ((sourceType.tagBits & BeginHierarchyCheck) != 0)
			return;

		sourceType.tagBits |= BeginHierarchyCheck;
		boolean noProblems = connectSuperclass();
		noProblems &= connectSuperInterfaces();
		noProblems &= connectTypeVariables(referenceContext.typeParameters);
		sourceType.tagBits |= EndHierarchyCheck;
		if (noProblems && sourceType.isHierarchyInconsistent())
			problemReporter().hierarchyHasProblems(sourceType);
	}

	public boolean detectHierarchyCycle(TypeBinding superType, TypeReference reference, TypeBinding[] argTypes) {
		if (!(superType instanceof ReferenceBinding)) return false;

		if (argTypes != null) {
			for (int i = 0, l = argTypes.length; i < l; i++) {
				TypeBinding argType = argTypes[i].leafComponentType();
				if ((argType.tagBits & BeginHierarchyCheck) == 0 && argType instanceof SourceTypeBinding)
			    	// ensure if this is a source argument type that it has already been checked
			    	((SourceTypeBinding) argType).scope.connectTypeHierarchyWithoutMembers();
			}
		}

		if (reference == this.superTypeReference) { // see findSuperType()
			if (superType.isTypeVariable())
				return false; // error case caught in resolveSuperType()
			// abstract class X<K,V> implements java.util.Map<K,V>
			//    static abstract class M<K,V> implements Entry<K,V>
			if (superType.isParameterizedType())
				superType = ((ParameterizedTypeBinding) superType).type;
			compilationUnitScope().recordSuperTypeReference(superType); // to record supertypes
			return detectHierarchyCycle(referenceContext.binding, (ReferenceBinding) superType, reference);
		}

		if ((superType.tagBits & BeginHierarchyCheck) == 0 && superType instanceof SourceTypeBinding)
			// ensure if this is a source superclass that it has already been checked
			((SourceTypeBinding) superType).scope.connectTypeHierarchyWithoutMembers();
		return false;
	}

	// Answer whether a cycle was found between the sourceType & the superType
	private boolean detectHierarchyCycle(SourceTypeBinding sourceType, ReferenceBinding superType, TypeReference reference) {
		if (superType.isRawType())
			superType = ((RawTypeBinding) superType).type;
		// by this point the superType must be a binary or source type

		if (sourceType == superType) {
			problemReporter().hierarchyCircularity(sourceType, superType, reference);
			sourceType.tagBits |= HierarchyHasProblems;
			return true;
		}

		if (superType.isMemberType()) {
			ReferenceBinding current = superType.enclosingType();
			do {
				if (current.isHierarchyBeingConnected()) {
					problemReporter().hierarchyCircularity(sourceType, current, reference);
					sourceType.tagBits |= HierarchyHasProblems;
					current.tagBits |= HierarchyHasProblems;
					return true;
				}
			} while ((current = current.enclosingType()) != null);
		}

		if (superType.isBinaryBinding()) {
			// force its superclass & superinterfaces to be found... 2 possibilities exist - the source type is included in the hierarchy of:
			//		- a binary type... this case MUST be caught & reported here
			//		- another source type... this case is reported against the other source type
			boolean hasCycle = false;
			ReferenceBinding parentType = superType.superclass();
			if (parentType != null) {
				if (sourceType == parentType) {
					problemReporter().hierarchyCircularity(sourceType, superType, reference);
					sourceType.tagBits |= HierarchyHasProblems;
					superType.tagBits |= HierarchyHasProblems;
					return true;
				}
				if (parentType.isParameterizedType())
					parentType = ((ParameterizedTypeBinding) parentType).type;
				hasCycle |= detectHierarchyCycle(sourceType, parentType, reference);
				if ((parentType.tagBits & HierarchyHasProblems) != 0) {
					sourceType.tagBits |= HierarchyHasProblems;
					parentType.tagBits |= HierarchyHasProblems; // propagate down the hierarchy
				}
			}

			ReferenceBinding[] itsInterfaces = superType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				for (int i = 0, length = itsInterfaces.length; i < length; i++) {
					ReferenceBinding anInterface = itsInterfaces[i];
					if (sourceType == anInterface) {
						problemReporter().hierarchyCircularity(sourceType, superType, reference);
						sourceType.tagBits |= HierarchyHasProblems;
						superType.tagBits |= HierarchyHasProblems;
						return true;
					}
					if (anInterface.isParameterizedType())
						anInterface = ((ParameterizedTypeBinding) anInterface).type;
					hasCycle |= detectHierarchyCycle(sourceType, anInterface, reference);
					if ((anInterface.tagBits & HierarchyHasProblems) != 0) {
						sourceType.tagBits |= HierarchyHasProblems;
						superType.tagBits |= HierarchyHasProblems;
					}
				}
			}
			return hasCycle;
		}

		if (superType.isHierarchyBeingConnected()) {
			if (((SourceTypeBinding) superType).scope.superTypeReference != null) { // if null then its connecting its type variables
				problemReporter().hierarchyCircularity(sourceType, superType, reference);
				sourceType.tagBits |= HierarchyHasProblems;
				superType.tagBits |= HierarchyHasProblems;
				return true;
			}
		}
		if ((superType.tagBits & BeginHierarchyCheck) == 0)
			// ensure if this is a source superclass that it has already been checked
			((SourceTypeBinding) superType).scope.connectTypeHierarchyWithoutMembers();
		if ((superType.tagBits & HierarchyHasProblems) != 0)
			sourceType.tagBits |= HierarchyHasProblems;
		return false;
	}

	private ReferenceBinding findAmbiguousInterface(ReferenceBinding newInterface, ReferenceBinding currentType) {
		TypeBinding newErasure = newInterface.erasure();
		if (newInterface == newErasure) return null;

		ReferenceBinding[][] interfacesToVisit = new ReferenceBinding[5][];
		int lastPosition = -1;
		do {
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != NoSuperInterfaces) {
				if (++lastPosition == interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
				interfacesToVisit[lastPosition] = itsInterfaces;
			}
		} while ((currentType = currentType.superclass()) != null);

		for (int i = 0; i <= lastPosition; i++) {
			ReferenceBinding[] interfaces = interfacesToVisit[i];
			for (int j = 0, length = interfaces.length; j < length; j++) {
				currentType = interfaces[j];
				if (currentType.erasure() == newErasure)
					if (currentType != newInterface)
						return currentType;

				ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
				if (itsInterfaces != NoSuperInterfaces) {
					if (++lastPosition == interfacesToVisit.length)
						System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
					interfacesToVisit[lastPosition] = itsInterfaces;
				}
			}
		}
		return null;
	}

	private ReferenceBinding findSupertype(TypeReference typeReference) {
		try {
			typeReference.aboutToResolve(this); // allows us to trap completion & selection nodes
			compilationUnitScope().recordQualifiedReference(typeReference.getTypeName());
			this.superTypeReference = typeReference;
			ReferenceBinding superType = (ReferenceBinding) typeReference.resolveSuperType(this);
			this.superTypeReference = null;
			return superType;
		} catch (AbortCompilation e) {
			e.updateContext(typeReference, referenceCompilationUnit().compilationResult);
			throw e;
		}			
	}

	/* Answer the problem reporter to use for raising new problems.
	*
	* Note that as a side-effect, this updates the current reference context
	* (unit, type or method) in case the problem handler decides it is necessary
	* to abort.
	*/
	public ProblemReporter problemReporter() {
		MethodScope outerMethodScope;
		if ((outerMethodScope = outerMostMethodScope()) == null) {
			ProblemReporter problemReporter = referenceCompilationUnit().problemReporter;
			problemReporter.referenceContext = referenceContext;
			return problemReporter;
		}
		return outerMethodScope.problemReporter();
	}

	/* Answer the reference type of this scope.
	* It is the nearest enclosing type of this scope.
	*/
	public TypeDeclaration referenceType() {
		return referenceContext;
	}
	
	public String toString() {
		if (referenceContext != null)
			return "--- Class Scope ---\n\n"  //$NON-NLS-1$
							+ referenceContext.binding.toString();
		return "--- Class Scope ---\n\n Binding not initialized" ; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2864.java