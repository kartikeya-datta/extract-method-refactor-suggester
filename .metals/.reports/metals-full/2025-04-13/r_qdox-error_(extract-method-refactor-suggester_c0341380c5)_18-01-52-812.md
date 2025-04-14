error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4435.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4435.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4435.java
text:
```scala
p@@arameterSignatures[0] = new String(Engine.getSignature(typeBinding.enclosingType()));

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
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;

import org.eclipse.jdt.core.Flags;
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
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.codeassist.ISelectionRequestor;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.internal.codeassist.impl.Engine;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.core.util.HandleFactory;

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
protected void acceptBinaryMethod(IType type, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, String[] paramterSignatures) {
	IMethod method= type.getMethod(new String(selector), paramterSignatures);
	if (method.exists()) {
		addElement(method);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept method("); //$NON-NLS-1$
			System.out.print(method.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
	}
}
/**
 * Resolve the class.
 */
public void acceptClass(char[] packageName, char[] className, boolean needQualification, boolean isDeclaration, int start, int end) {
	acceptType(packageName, className, NameLookup.ACCEPT_CLASSES, needQualification, isDeclaration, start, end);
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
public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name, boolean isDeclaration, int start, int end) {
	if(isDeclaration) {
		IType type= resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES,
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
		IType type= resolveType(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES);
		if (type != null) {
			IField field= type.getField(new String(name));
			if (field.exists()) {
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
/**
 * Resolve the interface
 */
public void acceptInterface(char[] packageName, char[] interfaceName, boolean needQualification, boolean isDeclaration, int start, int end) {
	acceptType(packageName, interfaceName, NameLookup.ACCEPT_INTERFACES, needQualification, isDeclaration, start, end);
}
public void acceptLocalField(SourceTypeBinding typeBinding, char[] name, CompilationUnitDeclaration parsedUnit) {
	IType type = (IType)this.handleFactory.createElement(typeBinding.scope.referenceContext, parsedUnit, this.openable);
	if (type != null) {
		IField field= type.getField(new String(name));
		if (field.exists()) {
			addElement(field);
			if(SelectionEngine.DEBUG){
				System.out.print("SELECTION - accept field("); //$NON-NLS-1$
				System.out.print(field.toString());
				System.out.println(")"); //$NON-NLS-1$
			}
		}
	}
}
public void acceptLocalMethod(SourceTypeBinding typeBinding, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, String[] parameterSignatures, boolean isConstructor, CompilationUnitDeclaration parsedUnit, boolean isDeclaration, int start, int end) {
	IType type = (IType)this.handleFactory.createElement(typeBinding.scope.referenceContext, parsedUnit, this.openable);
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
				parameterSignatures[0] = Engine.getSignature(typeBinding.enclosingType());
			}
			
			acceptBinaryMethod(type, selector, parameterPackageNames, parameterTypeNames, parameterSignatures);
		} else {
			acceptSourceMethod(type, selector, parameterPackageNames, parameterTypeNames);
		}
	}
}
public void acceptLocalType(SourceTypeBinding typeBinding, CompilationUnitDeclaration parsedUnit) {
	IJavaElement type = this.handleFactory.createElement(typeBinding.scope.referenceContext, parsedUnit, this.openable);
	if (type != null) {
		addElement(type);
		if(SelectionEngine.DEBUG){
			System.out.print("SELECTION - accept local type("); //$NON-NLS-1$
			System.out.print(type.toString());
			System.out.println(")"); //$NON-NLS-1$
		}
	}
}
public void acceptLocalVariable(LocalVariableBinding binding, CompilationUnitDeclaration parsedUnit) {
	IJavaElement localVar = this.handleFactory.createElement(binding.declaration, parsedUnit, this.openable);
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
public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, String enclosingDeclaringTypeSignature, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames, String[] parameterSignatures, boolean isConstructor, boolean isDeclaration, int start, int end) {
	if(isDeclaration) {
		IType type = resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES,
				start, end);
		
		if(type != null) {
			this.acceptMethodDeclaration(type, selector, start, end);
		}
	} else {
		IType type = resolveType(declaringTypePackageName, declaringTypeName,
			NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES);
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
				
				acceptBinaryMethod(type, selector, parameterPackageNames, parameterTypeNames, parameterSignatures);
			} else {
				acceptSourceMethod(type, selector, parameterPackageNames, parameterTypeNames);
			}
		}
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
protected void acceptSourceMethod(IType type, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames) {
	String name = new String(selector);
	IMethod[] methods = null;
	try {
		methods = type.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getElementName().equals(name)
					&& methods[i].getParameterTypes().length == parameterTypeNames.length) {
				addElement(methods[i]);
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
			String simpleName= Signature.getSimpleName(Signature.toString(signatures[p]));
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
/**
 * Resolve the type, adding to the resolved elements.
 */
protected void acceptType(char[] packageName, char[] typeName, int acceptFlags, boolean needQualification, boolean isDeclaration, int start, int end) {
	IType type = null;
	if(isDeclaration) {
		type = resolveTypeByLocation(packageName, typeName, acceptFlags, start, end);
	} else {
		type = resolveType(packageName, typeName, acceptFlags);
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
public void acceptTypeParameter(char[] declaringTypePackageName, char[] declaringTypeName, char[] typeParameterName, boolean isDeclaration, int start, int end) {
	IType type;
	if(isDeclaration) {
		type = resolveTypeByLocation(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES,
				start, end);
	} else {
		type = resolveType(declaringTypePackageName, declaringTypeName,
				NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES);
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
			NameLookup.ACCEPT_CLASSES | NameLookup.ACCEPT_INTERFACES,
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
			type= this.nameLookup.findType(new String(typeName), pkgs[i], false, acceptFlags);
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
			type= this.nameLookup.findType(new String(typeName), pkgs[i], false, acceptFlags);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4435.java