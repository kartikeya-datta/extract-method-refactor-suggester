error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8800.java
text:
```scala
public v@@oid acceptError(CategorizedProblem error) {

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
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.codeassist.ISelectionRequestor;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.core.util.HandleFactory;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Implementation of <code>ISelectionRequestor</code> to assist with
 * code resolve in a compilation unit. Translates names to elements.
 */
public class SelectionRequestor implements ISelectionRequestor {
	/*
	 * The name lookup facility used to resolve packages
	 */
	protected NameLookup nameLookup;

	/*
	 * The compilation unit or class file we are resolving in
	 */
	protected Openable openable;

	/*
	 * The collection of resolved elements.
	 */
	protected IJavaElement[] elements = JavaElement.NO_ELEMENTS;
	protected int elementIndex = -1;
	
	protected HandleFactory handleFactory = new HandleFactory();

/**
 * Creates a selection requestor that uses that given
 * name lookup facility to resolve names.
 *
 * Fix for 1FVXGDK
 */
public SelectionRequestor(NameLookup nameLookup, Openable openable) {
	super();
	this.nameLookup = nameLookup;
	this.openable = openable;
}
/**
 * Resolve the binary method
 *
 * fix for 1FWFT6Q
 */
protected void acceptBinaryMethod(IType type, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, String[] parameterSignatures, char[] uniqueKey, boolean isConstructor) {
	IMethod method= type.getMethod(new String(selector), parameterSignatures);
	if (method.exists()) {
		try {
			if(!isConstructor || ((JavaElement)method).getSourceMapper() == null) {
				if (uniqueKey != null)
					method = new ResolvedBinaryMethod(
							(JavaElement)method.getParent(),
							method.getElementName(),
							method.getParameterTypes(),
							new String(uniqueKey));
				addElement(method);
				if(SelectionEngine.DEBUG){
					System.out.print("SELECTION - accept method("); //$NON-NLS-1$
					System.out.print(method.toString());
					System.out.println(")"); //$NON-NLS-1$
				}
			} else {
				ISourceRange range = method.getSourceRange();
				if (range.getOffset() != -1 && range.getLength() != 0 ) {
					if (uniqueKey != null)
						method = new ResolvedBinaryMethod(
								(JavaElement)method.getParent(),
								method.getElementName(),
								method.getParameterTypes(),
								new String(uniqueKey));
					addElement(method);
					if(SelectionEngine.DEBUG){
						System.out.print("SELECTION - accept method("); //$NON-NLS-1$
						System.out.print(method.toString());
						System.out.println(")"); //$NON-NLS-1$
					}
				} else {
					// no range was actually found, but a method was originally given -> default constructor
					addElement(type);
					if(SelectionEngine.DEBUG){
						System.out.print("SELECTION - accept type("); //$NON-NLS-1$
						System.out.print(type.toString());
						System.out.println(")"); //$NON-NLS-1$
					}
				}
			}
		} catch (JavaModelException e) {
			// an exception occurs, return nothing
		}
	}
}
/**
 * Resolve the type.
 */
public void acceptType(char[] packageName, char[] typeName, int modifiers, boolean isDeclaration, char[] uniqueKey, int start, int end) {
	int acceptFlags = 0;
	int kind = modifiers & (ClassFileConstants.AccInterface|ClassFileConstants.AccEnum|ClassFileConstants.AccAnnotation);
	switch (kind) {
		case ClassFileConstants.AccAnnotation:
		case ClassFileConstants.AccAnnotation|ClassFileConstants.AccInterface:
			acceptFlags = NameLookup.ACCEPT_ANNOTATIONS;
			break;
		case ClassFileConstants.AccEnum:
			acceptFlags = NameLookup.ACCEPT_ENUMS;
			break;
		case ClassFileConstants.AccInterface:
			acceptFlags = NameLookup.ACCEPT_INTERFACES;
			break;
		default:
			acceptFlags = NameLookup.ACCEPT_CLASSES;
			break;
	}
	IType type = null;
	if(isDeclaration) {
		type = resolveTypeByLocation(packageName, typeName, acceptFlags, start, end);
	} else {
		type = resolveType(packageName, typeName, acceptFlags);
		if(type != null ) {
			String key = uniqueKey == null ? type.getKey() : new String(uniqueKey);
			if(type.isBinary()) {
				type = new ResolvedBinaryType((JavaElement)type.getParent(), type.getElementName(), key);
			} else {
				type = new ResolvedSourceType((JavaElement)type.getParent(), type.getElementName(), key);
			}
		}
	}
	
	if (type != null) {
		addElement(type);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept type("); //$NON-NLS-1$
			System.out.print(type.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
	} 
}
/**
 * @see ISelectionRequestor#acceptError
 */
public void acceptError(IProblem error) {
	// do nothing
}
/**
 * Resolve the field.
 */
public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, boolean isDeclaration, char[] uniqueKey, int start, int end) {
	if(isDeclaration) {
		IType type= resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_ALL,
				start, end);
		if(type != null) {
			try {
				IField[] fields = type.getFields();
				for (int i = 0; i < fields.length; i++) {
					IField field = fields[i];
					ISourceRange range = field.getNameRange();
					if(range.getOffset() <= start
							&& range.getOffset() + range.getLength() >= end
							&& field.getElementName().equals(new String(name))) {
						addElement(fields[i]);
						if(SelectionEngine.DEBUG){
							System.out.print("SELECTION - accept field("); //$NON-NLS-1$
							System.out.print(field.toString());
							System.out.println(")"); //$NON-NLS-1$
						}
						return; // only one method is possible
					}
				}
			} catch (JavaModelException e) {
				return; 
			}
		}
	} else {
		IType type= resolveType(declaringTypePackageName, declaringTypeName, NameLookup.ACCEPT_ALL);
		if (type != null) {
			IField field= type.getField(new String(name));
			if (field.exists()) {
				if (uniqueKey != null) {
					if(field.isBinary()) {
						field = new ResolvedBinaryField(
								(JavaElement)field.getParent(),
								field.getElementName(),
								new String(uniqueKey));
					} else {
						field = new ResolvedSourceField(
								(JavaElement)field.getParent(),
								field.getElementName(),
								new String(uniqueKey));
					}
				}
				addElement(field);
				if(SelectionEngine.DEBUG){
					System.out.print("SELECTION - accept field("); //$NON-NLS-1$
					System.out.print(field.toString());
					System.out.println(")"); //$NON-NLS-1$
				}
			}
		}
	}
}
public void acceptLocalField(FieldBinding fieldBinding) {
	IJavaElement res;
	if(fieldBinding.declaringClass instanceof ParameterizedTypeBinding) {
		LocalTypeBinding localTypeBinding = (LocalTypeBinding)((ParameterizedTypeBinding)fieldBinding.declaringClass).type;
		res = findLocalElement(localTypeBinding.sourceStart());
	} else {
		SourceTypeBinding typeBinding = (SourceTypeBinding)fieldBinding.declaringClass;
		res = findLocalElement(typeBinding.sourceStart());
	}
	if (res != null && res.getElementType() == IJavaElement.TYPE) {
		IType type = (IType) res;
		IField field= type.getField(new String(fieldBinding.name));
		if (field.exists()) {
			char[] uniqueKey = fieldBinding.computeUniqueKey();
			if(field.isBinary()) {
				field = new ResolvedBinaryField(
						(JavaElement)field.getParent(),
						field.getElementName(),
						new String(uniqueKey));
			} else {
				field = new ResolvedSourceField(
						(JavaElement)field.getParent(),
						field.getElementName(),
						new String(uniqueKey));
			}
			addElement(field);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept field("); //$NON-NLS-1$
				System.out.print(field.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptLocalMethod(MethodBinding methodBinding) {
	IJavaElement res = findLocalElement(methodBinding.sourceStart());
	if(res != null) {
		if(res.getElementType() == IJavaElement.METHOD) {
			IMethod method = (IMethod) res;
			
			char[] uniqueKey = methodBinding.computeUniqueKey();
			if(method.isBinary()) {
				res = new ResolvedBinaryMethod(
						(JavaElement)res.getParent(),
						method.getElementName(),
						method.getParameterTypes(), 
						new String(uniqueKey));
			} else {
				res = new ResolvedSourceMethod(
						(JavaElement)res.getParent(),
						method.getElementName(),
						method.getParameterTypes(), 
						new String(uniqueKey));
			}
			addElement(res);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept method("); //$NON-NLS-1$
				System.out.print(res.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		} else if(methodBinding.selector == TypeConstants.INIT && res.getElementType() == IJavaElement.TYPE) {
			// it's a default constructor
			res = new ResolvedSourceType((JavaElement)res.getParent(), res.getElementName(), new String(methodBinding.declaringClass.computeUniqueKey()));
			addElement(res);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type("); //$NON-NLS-1$
				System.out.print(res.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptLocalType(TypeBinding typeBinding) {
	IJavaElement res =  null;
	if(typeBinding instanceof ParameterizedTypeBinding) {
		LocalTypeBinding localTypeBinding = (LocalTypeBinding)((ParameterizedTypeBinding)typeBinding).type;
		res = findLocalElement(localTypeBinding.sourceStart());
	} else if(typeBinding instanceof SourceTypeBinding) {
		res = findLocalElement(((SourceTypeBinding)typeBinding).sourceStart());
	}
	if(res != null && res.getElementType() == IJavaElement.TYPE) {
		res = new ResolvedSourceType((JavaElement)res.getParent(), res.getElementName(), new String(typeBinding.computeUniqueKey()));
		addElement(res);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept type("); //$NON-NLS-1$
			System.out.print(res.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
	}
}
public void acceptLocalTypeParameter(TypeVariableBinding typeVariableBinding) {
	IJavaElement res;
	if(typeVariableBinding.declaringElement instanceof ParameterizedTypeBinding) {
		LocalTypeBinding localTypeBinding = (LocalTypeBinding)((ParameterizedTypeBinding)typeVariableBinding.declaringElement).type;
		res = findLocalElement(localTypeBinding.sourceStart());
	} else {
		SourceTypeBinding typeBinding = (SourceTypeBinding)typeVariableBinding.declaringElement;
		res = findLocalElement(typeBinding.sourceStart());
	}
	if (res != null && res.getElementType() == IJavaElement.TYPE) {
		IType type = (IType) res;
		ITypeParameter typeParameter = type.getTypeParameter(new String(typeVariableBinding.sourceName));
		if (typeParameter.exists()) {
			addElement(typeParameter);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type parameter("); //$NON-NLS-1$
				System.out.print(typeParameter.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptLocalMethodTypeParameter(TypeVariableBinding typeVariableBinding) {
	MethodBinding methodBinding = (MethodBinding)typeVariableBinding.declaringElement;
	IJavaElement res = findLocalElement(methodBinding.sourceStart());
	if(res != null && res.getElementType() == IJavaElement.METHOD) {
		IMethod method = (IMethod) res;
		
		ITypeParameter typeParameter = method.getTypeParameter(new String(typeVariableBinding.sourceName));
		if (typeParameter.exists()) {
			addElement(typeParameter);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type parameter("); //$NON-NLS-1$
				System.out.print(typeParameter.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptLocalVariable(LocalVariableBinding binding) {
	LocalDeclaration local = binding.declaration;
	IJavaElement parent = findLocalElement(local.sourceStart); // findLocalElement() cannot find local variable
	IJavaElement localVar = null;
	if(parent != null) {
		localVar = new LocalVariable(
				(JavaElement)parent, 
				new String(local.name), 
				local.declarationSourceStart,
				local.declarationSourceEnd,
				local.sourceStart,
				local.sourceEnd,
				Util.typeSignature(local.type));
	}
	if (localVar != null) {
		addElement(localVar);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept local variable("); //$NON-NLS-1$
			System.out.print(localVar.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
	}
}
/**
 * Resolve the method
 */
public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, String enclosingDeclaringTypeSignature, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, String[] parameterSignatures, boolean isConstructor, boolean isDeclaration, char[] uniqueKey, int start, int end) {
	IJavaElement[] previousElement = this.elements;
	int previousElementIndex = this.elementIndex;
	this.elements = JavaElement.NO_ELEMENTS;
	this.elementIndex = -1;
	
	if(isDeclaration) {
		IType type = resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_ALL,
				start, end);
		
		if(type != null) {
			this.acceptMethodDeclaration(type, selector, start, end);
		}
	} else {
		IType type = resolveType(declaringTypePackageName, declaringTypeName,
			NameLookup.ACCEPT_ALL);
		// fix for 1FWFT6Q
		if (type != null) {
			if (type.isBinary()) {
				
				// need to add a paramater for constructor in binary type
				IType declaringDeclaringType = type.getDeclaringType();
				
				boolean isStatic = false;
				try {
					isStatic = Flags.isStatic(type.getFlags());
				} catch (JavaModelException e) {
					// isStatic == false
				}
				
				if(declaringDeclaringType != null && isConstructor	&& !isStatic) {
					int length = parameterPackageNames.length;
					System.arraycopy(parameterPackageNames, 0, parameterPackageNames = new char[length+1][], 1, length);
					System.arraycopy(parameterTypeNames, 0, parameterTypeNames = new char[length+1][], 1, length);
					System.arraycopy(parameterSignatures, 0, parameterSignatures = new String[length+1], 1, length);
					
					parameterPackageNames[0] = declaringDeclaringType.getPackageFragment().getElementName().toCharArray();
					parameterTypeNames[0] = declaringDeclaringType.getTypeQualifiedName().toCharArray();
					parameterSignatures[0] = enclosingDeclaringTypeSignature;
				}
				
				acceptBinaryMethod(type, selector, parameterPackageNames, parameterTypeNames, parameterSignatures, uniqueKey, isConstructor);
			} else {
				acceptSourceMethod(type, selector, parameterPackageNames, parameterTypeNames, uniqueKey);
			}
		}
	}
	
	if(previousElementIndex > -1) {
		int elementsLength = this.elementIndex + previousElementIndex + 2;
		if(elementsLength > this.elements.length) {
			System.arraycopy(this.elements, 0, this.elements = new IJavaElement[elementsLength * 2 + 1], 0, this.elementIndex + 1);
		}
		System.arraycopy(previousElement, 0, this.elements, this.elementIndex + 1, previousElementIndex + 1);
		this.elementIndex += previousElementIndex + 1;
	}
}
/**
 * Resolve the package
 */
public void acceptPackage(char[] packageName) {
	IPackageFragment[] pkgs = this.nameLookup.findPackageFragments(new String(packageName), false);
	if (pkgs != null) {
		for (int i = 0, length = pkgs.length; i < length; i++) {
			addElement(pkgs[i]);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept package("); //$NON-NLS-1$
				System.out.print(pkgs[i].toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
/**
 * Resolve the source method
 *
 * fix for 1FWFT6Q
 */
protected void acceptSourceMethod(IType type, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, char[] uniqueKey) {
	String name = new String(selector);
	IMethod[] methods = null;
	try {
		methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getElementName().equals(name)
					&& methods[i].getParameterTypes().length == parameterTypeNames.length) {
				IMethod method = methods[i];
				if (uniqueKey != null)
					method = new ResolvedSourceMethod(
						(JavaElement)method.getParent(),
						method.getElementName(),
						method.getParameterTypes(),
						new String(uniqueKey));
				addElement(method);
			}
		}
	} catch (JavaModelException e) {
		return; 
	}

	// if no matches, nothing to report
	if (this.elementIndex == -1) {
		// no match was actually found, but a method was originally given -> default constructor
		addElement(type);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept type("); //$NON-NLS-1$
			System.out.print(type.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
		return;
	}

	// if there is only one match, we've got it
	if (this.elementIndex == 0) {
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept method("); //$NON-NLS-1$
			System.out.print(this.elements[0].toString());
			System.out.println(")"); //$NON-NLS-1$
		}
		return;
	}

	// more than one match - must match simple parameter types
	IJavaElement[] matches = this.elements;
	int matchesIndex = this.elementIndex;
	this.elements = JavaElement.NO_ELEMENTS;
	this.elementIndex = -1;
	for (int i = 0; i <= matchesIndex; i++) {
		IMethod method= (IMethod)matches[i];
		String[] signatures = method.getParameterTypes();
		boolean match= true;
		for (int p = 0; p < signatures.length; p++) {
			String simpleName= Signature.getSimpleName(Signature.toString(Signature.getTypeErasure(signatures[p])));
			char[] simpleParameterName = CharOperation.lastSegment(parameterTypeNames[p], '.');
			if (!simpleName.equals(new String(simpleParameterName))) {
				match = false;
				break;
			}
		}
		if (match) {
			addElement(method);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept method("); //$NON-NLS-1$
				System.out.print(method.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
	
}
protected void acceptMethodDeclaration(IType type, char[] selector, int start, int end) {
	String name = new String(selector);
	IMethod[] methods = null;
	try {
		methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			ISourceRange range = methods[i].getNameRange();
			if(range.getOffset() <= start
					&& range.getOffset() + range.getLength() >= end
					&& methods[i].getElementName().equals(name)) {
				addElement(methods[i]);
				if(SelectionEngine.DEBUG){
					System.out.print("SELECTION - accept method("); //$NON-NLS-1$
					System.out.print(this.elements[0].toString());
					System.out.println(")"); //$NON-NLS-1$
				}
				return; // only one method is possible
			}
		}
	} catch (JavaModelException e) {
		return; 
	}

	// no match was actually found
	addElement(type);
	if(SelectionEngine.DEBUG){
		System.out.print("SELECTION - accept type("); //$NON-NLS-1$
		System.out.print(type.toString());
		System.out.println(")"); //$NON-NLS-1$
	}
	return;
}
public void acceptTypeParameter(char[] declaringTypePackageName, char[] declaringTypeName, char[] typeParameterName, boolean isDeclaration, int start, int end) {
	IType type;
	if(isDeclaration) {
		type = resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_ALL,
				start, end);
	} else {
		type = resolveType(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_ALL);
	}
			
	if(type != null) {
		ITypeParameter typeParameter = type.getTypeParameter(new String(typeParameterName));
		if(typeParameter == null) {
			addElement(type);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type("); //$NON-NLS-1$
				System.out.print(type.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		} else {
			addElement(typeParameter);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type parameter("); //$NON-NLS-1$
				System.out.print(typeParameter.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptMethodTypeParameter(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector,int selectorStart, int selectorEnd, char[] typeParameterName, boolean isDeclaration, int start, int end) {
	IType type = resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
			NameLookup.ACCEPT_ALL,
			selectorStart, selectorEnd);
	
	if(type != null) {
		IMethod method = null;
		
		String name = new String(selector);
		IMethod[] methods = null;
		
		try {
			methods = type.getMethods();
			done : for (int i = 0; i < methods.length; i++) {
				ISourceRange range = methods[i].getNameRange();
				if(range.getOffset() >= selectorStart
						&& range.getOffset() + range.getLength() <= selectorEnd
						&& methods[i].getElementName().equals(name)) {
					method = methods[i];
					break done;
				}
			}
		} catch (JavaModelException e) {
			//nothing to do
		}

		if(method == null) {
			addElement(type);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept type("); //$NON-NLS-1$
				System.out.print(type.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		} else {
			ITypeParameter typeParameter = method.getTypeParameter(new String(typeParameterName));
			if(typeParameter == null) {
				addElement(method);
				if(SelectionEngine.DEBUG){
					System.out.print("SELECTION - accept method("); //$NON-NLS-1$
					System.out.print(method.toString());
					System.out.println(")"); //$NON-NLS-1$
				}
			} else {
				addElement(typeParameter);
				if(SelectionEngine.DEBUG){
					System.out.print("SELECTION - accept method type parameter("); //$NON-NLS-1$
					System.out.print(typeParameter.toString());
					System.out.println(")"); //$NON-NLS-1$
				}
			}
		}
	}
}
/*
 * Adds the given element to the list of resolved elements.
 */
protected void addElement(IJavaElement element) {
	int elementLength = this.elementIndex + 1;
	if (elementLength == this.elements.length) {
		System.arraycopy(this.elements, 0, this.elements = new IJavaElement[(elementLength*2) + 1], 0, elementLength);
	}
	this.elements[++this.elementIndex] = element;
}

/*
 * findLocalElement() cannot find local variable
 */
protected IJavaElement findLocalElement(int pos) {
	IJavaElement res = null;
	if(this.openable instanceof ICompilationUnit) {
		ICompilationUnit cu = (ICompilationUnit) this.openable;
		try {
			res = cu.getElementAt(pos);
		} catch (JavaModelException e) {
			// do nothing
		}
	} else if (this.openable instanceof ClassFile) {
		ClassFile cf = (ClassFile) this.openable;
		try {
			 res = cf.getElementAtConsideringSibling(pos);
		} catch (JavaModelException e) {
			// do nothing
		}
	}
	return res;
}
/**
 * Returns the resolved elements.
 */
public IJavaElement[] getElements() {
	int elementLength = this.elementIndex + 1;
	if (this.elements.length != elementLength) {
		System.arraycopy(this.elements, 0, this.elements = new IJavaElement[elementLength], 0, elementLength);
	}
	return this.elements;
}
/**
 * Resolve the type
 */
protected IType resolveType(char[] packageName, char[] typeName, int acceptFlags) {

	IType type= null;
	
	if (this.openable instanceof CompilationUnit && ((CompilationUnit)this.openable).isWorkingCopy()) {
		CompilationUnit wc = (CompilationUnit) this.openable;
		try {
			if(((packageName == null || packageName.length == 0) && wc.getPackageDeclarations().length == 0) ||
				(!(packageName == null || packageName.length == 0) && wc.getPackageDeclaration(new String(packageName)).exists())) {
					
				char[][] compoundName = CharOperation.splitOn('.', typeName);
				if(compoundName.length > 0) {
					type = wc.getType(new String(compoundName[0]));
					for (int i = 1, length = compoundName.length; i < length; i++) {
						type = type.getType(new String(compoundName[i]));
					}
				}
				
				if(type != null && !type.exists()) {
					type = null;
				}
			}
		}catch (JavaModelException e) {
			type = null;
		}
	}

	if(type == null) {
		IPackageFragment[] pkgs = this.nameLookup.findPackageFragments(
			(packageName == null || packageName.length == 0) ? IPackageFragment.DEFAULT_PACKAGE_NAME : new String(packageName), 
			false);
		// iterate type lookup in each package fragment
		for (int i = 0, length = pkgs == null ? 0 : pkgs.length; i < length; i++) {
			type= this.nameLookup.findType(new String(typeName), pkgs[i], false, acceptFlags, true/*consider secondary types*/);
			if (type != null) break;	
		}
		if (type == null) {
			String pName= IPackageFragment.DEFAULT_PACKAGE_NAME;
			if (packageName != null) {
				pName = new String(packageName);
			}
			if (this.openable != null && this.openable.getParent().getElementName().equals(pName)) {
				// look inside the type in which we are resolving in
				String tName= new String(typeName);
				tName = tName.replace('.','$');
				IType[] allTypes= null;
				try {
					ArrayList list = this.openable.getChildrenOfType(IJavaElement.TYPE);
					allTypes = new IType[list.size()];
					list.toArray(allTypes);
				} catch (JavaModelException e) {
					return null;
				}
				for (int i= 0; i < allTypes.length; i++) {
					if (allTypes[i].getTypeQualifiedName().equals(tName)) {
						return allTypes[i];
					}
				}
			}
		}
	}
	return type;
}
protected IType resolveTypeByLocation(char[] packageName, char[] typeName, int acceptFlags, int start, int end) {

	IType type= null;
	
	// TODO (david) post 3.0 should remove isOpen check, and investigate reusing ICompilationUnit#getElementAt. may need to optimize #getElementAt to remove recursions
	if (this.openable instanceof CompilationUnit && ((CompilationUnit)this.openable).isOpen()) {
		CompilationUnit wc = (CompilationUnit) this.openable;
		try {
			if(((packageName == null || packageName.length == 0) && wc.getPackageDeclarations().length == 0) ||
				(!(packageName == null || packageName.length == 0) && wc.getPackageDeclaration(new String(packageName)).exists())) {
					
				char[][] compoundName = CharOperation.splitOn('.', typeName);
				if(compoundName.length > 0) {
					
					IType[] tTypes = wc.getTypes();
					int i = 0;
					int depth = 0;
					done : while(i < tTypes.length) {
						ISourceRange range = tTypes[i].getSourceRange();
						if(range.getOffset() <= start
								&& range.getOffset() + range.getLength() >= end
								&& tTypes[i].getElementName().equals(new String(compoundName[depth]))) {
							if(depth == compoundName.length - 1) {
								type = tTypes[i];
								break done;
							}
							tTypes = tTypes[i].getTypes();
							i = 0;
							depth++;
							continue done;
						}
						i++;
					}
				}
				
				if(type != null && !type.exists()) {
					type = null;
				}
			}
		}catch (JavaModelException e) {
			type = null;
		}
	}

	if(type == null) {
		IPackageFragment[] pkgs = this.nameLookup.findPackageFragments(
			(packageName == null || packageName.length == 0) ? IPackageFragment.DEFAULT_PACKAGE_NAME : new String(packageName), 
			false);
		// iterate type lookup in each package fragment
		for (int i = 0, length = pkgs == null ? 0 : pkgs.length; i < length; i++) {
			type= this.nameLookup.findType(new String(typeName), pkgs[i], false, acceptFlags, true/*consider secondary types*/);
			if (type != null) break;	
		}
		if (type == null) {
			String pName= IPackageFragment.DEFAULT_PACKAGE_NAME;
			if (packageName != null) {
				pName = new String(packageName);
			}
			if (this.openable != null && this.openable.getParent().getElementName().equals(pName)) {
				// look inside the type in which we are resolving in
				String tName= new String(typeName);
				tName = tName.replace('.','$');
				IType[] allTypes= null;
				try {
					ArrayList list = this.openable.getChildrenOfType(IJavaElement.TYPE);
					allTypes = new IType[list.size()];
					list.toArray(allTypes);
				} catch (JavaModelException e) {
					return null;
				}
				for (int i= 0; i < allTypes.length; i++) {
					if (allTypes[i].getTypeQualifiedName().equals(tName)) {
						return allTypes[i];
					}
				}
			}
		}
	}
	return type;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8800.java