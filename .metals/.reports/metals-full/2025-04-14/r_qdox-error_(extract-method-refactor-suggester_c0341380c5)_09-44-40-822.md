error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7633.java
text:
```scala
public static final S@@tring EMPTY_JAVADOC = org.eclipse.jdt.internal.compiler.util.Util.EMPTY_STRING;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.codeassist.CompletionEngine;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.JavaModelManager.PerProjectInfo;
import org.eclipse.jdt.internal.core.hierarchy.TypeHierarchy;
import org.eclipse.jdt.internal.core.util.MementoTokenizer;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Parent is an IClassFile.
 *
 * @see IType
 */

public class BinaryType extends BinaryMember implements IType, SuffixConstants {
	
	private static final IField[] NO_FIELDS = new IField[0];
	private static final IMethod[] NO_METHODS = new IMethod[0];
	private static final IType[] NO_TYPES = new IType[0];
	private static final IInitializer[] NO_INITIALIZERS = new IInitializer[0];
	public static final String EMPTY_JAVADOC = new String();
	
protected BinaryType(JavaElement parent, String name) {
	super(parent, name);
}
/*
 * Remove my cached children from the Java Model
 */
protected void closing(Object info) throws JavaModelException {
	ClassFileInfo cfi = getClassFileInfo();
	cfi.removeBinaryChildren();
}

/**
 * @see IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, ICompletionRequestor)
 * @deprecated
 */
public void codeComplete(char[] snippet,int insertion,int position,char[][] localVariableTypeNames,char[][] localVariableNames,int[] localVariableModifiers,boolean isStatic,ICompletionRequestor requestor) throws JavaModelException {
	codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, DefaultWorkingCopyOwner.PRIMARY);
}

/**
 * @see IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, ICompletionRequestor, WorkingCopyOwner)
 * @deprecated
 */
public void codeComplete(char[] snippet,int insertion,int position,char[][] localVariableTypeNames,char[][] localVariableNames,int[] localVariableModifiers,boolean isStatic,ICompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException {
	if (requestor == null) {
		throw new IllegalArgumentException("Completion requestor cannot be null"); //$NON-NLS-1$
	}
	codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, new org.eclipse.jdt.internal.codeassist.CompletionRequestorWrapper(requestor), owner);
}
/*
 * @see IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, ICompletionRequestor)
 */
public void codeComplete(char[] snippet,int insertion,int position,char[][] localVariableTypeNames,char[][] localVariableNames,int[] localVariableModifiers,boolean isStatic,CompletionRequestor requestor) throws JavaModelException {
	codeComplete(snippet, insertion, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic, requestor, DefaultWorkingCopyOwner.PRIMARY);
}

/*
 * @see IType#codeComplete(char[], int, int, char[][], char[][], int[], boolean, ICompletionRequestor, WorkingCopyOwner)
 */
public void codeComplete(char[] snippet,int insertion,int position,char[][] localVariableTypeNames,char[][] localVariableNames,int[] localVariableModifiers,boolean isStatic,CompletionRequestor requestor, WorkingCopyOwner owner) throws JavaModelException {
	if (requestor == null) {
		throw new IllegalArgumentException("Completion requestor cannot be null"); //$NON-NLS-1$
	}
	JavaProject project = (JavaProject) getJavaProject();
	SearchableEnvironment environment = project.newSearchableNameEnvironment(owner);
	CompletionEngine engine = new CompletionEngine(environment, requestor, project.getOptions(true), project);

	String source = getClassFile().getSource();
	if (source != null && insertion > -1 && insertion < source.length()) {
		// code complete
		
		char[] prefix = CharOperation.concat(source.substring(0, insertion).toCharArray(), new char[]{'{'});
		char[] suffix =  CharOperation.concat(new char[]{'}'}, source.substring(insertion).toCharArray());
		char[] fakeSource = CharOperation.concat(prefix, snippet, suffix);
		
		BasicCompilationUnit cu = 
			new BasicCompilationUnit(
				fakeSource, 
				null,
				getElementName(),
				project); // use project to retrieve corresponding .java IFile

		engine.complete(cu, prefix.length + position, prefix.length);
	} else {
		engine.complete(this, snippet, position, localVariableTypeNames, localVariableNames, localVariableModifiers, isStatic);
	}
	if (NameLookup.VERBOSE) {
		System.out.println(Thread.currentThread() + " TIME SPENT in NameLoopkup#seekTypesInSourcePackage: " + environment.nameLookup.timeSpentInSeekTypesInSourcePackage + "ms");  //$NON-NLS-1$ //$NON-NLS-2$
		System.out.println(Thread.currentThread() + " TIME SPENT in NameLoopkup#seekTypesInBinaryPackage: " + environment.nameLookup.timeSpentInSeekTypesInBinaryPackage + "ms");  //$NON-NLS-1$ //$NON-NLS-2$
	}
}

/*
 * @see IType#createField(String, IJavaElement, boolean, IProgressMonitor)
 */
public IField createField(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/*
 * @see IType#createInitializer(String, IJavaElement, IProgressMonitor)
 */
public IInitializer createInitializer(String contents, IJavaElement sibling, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/*
 * @see IType#createMethod(String, IJavaElement, boolean, IProgressMonitor)
 */
public IMethod createMethod(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/*
 * @see IType#createType(String, IJavaElement, boolean, IProgressMonitor)
 */
public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
public boolean equals(Object o) {
	if (!(o instanceof BinaryType)) return false;
	return super.equals(o);
}
/*
 * @see IType#findMethods(IMethod)
 */
public IMethod[] findMethods(IMethod method) {
	try {
		return findMethods(method, getMethods());
	} catch (JavaModelException e) {
		// if type doesn't exist, no matching method can exist
		return null;
	}
}
/*
 * @see IParent#getChildren()
 */
public IJavaElement[] getChildren() throws JavaModelException {
	ClassFileInfo cfi = getClassFileInfo();
	return cfi.binaryChildren;
}
public IJavaElement[] getChildrenForCategory(String category) throws JavaModelException {
	IJavaElement[] children = getChildren();
	int length = children.length;
	if (length == 0) return children;
	SourceMapper mapper= getSourceMapper();
	if (mapper != null) {
		// ensure the class file's buffer is open so that categories are computed
		((ClassFile)getClassFile()).getBuffer();
		
		HashMap categories = mapper.categories;
		IJavaElement[] result = new IJavaElement[length];
		int index = 0;
		if (categories != null) {
			for (int i = 0; i < length; i++) {
				IJavaElement child = children[i];
				String[] cats = (String[]) categories.get(child);
				if (cats != null) {
					for (int j = 0, length2 = cats.length; j < length2; j++) {
						if (cats[j].equals(category)) {
							result[index++] = child;
							break;
						}
					}
				}
			}
		}
		if (index < length)
			System.arraycopy(result, 0, result = new IJavaElement[index], 0, index);
		return result;
	}
	return NO_ELEMENTS;	
}
protected ClassFileInfo getClassFileInfo() throws JavaModelException {
	ClassFile cf = (ClassFile)this.parent;
	return (ClassFileInfo) cf.getElementInfo();
}
/*
 * @see IMember#getDeclaringType()
 */
public IType getDeclaringType() {
	IClassFile classFile = this.getClassFile();
	if (classFile.isOpen()) {
		try {
			char[] enclosingTypeName = ((IBinaryType) getElementInfo()).getEnclosingTypeName();
			if (enclosingTypeName == null) {
				return null;
			}
		 	enclosingTypeName = ClassFile.unqualifiedName(enclosingTypeName);
		 	
			// workaround problem with class files compiled with javac 1.1.* 
			// that return a non-null enclosing type name for local types defined in anonymous (e.g. A$1$B)
			if (classFile.getElementName().length() > enclosingTypeName.length+1 
					&& Character.isDigit(classFile.getElementName().charAt(enclosingTypeName.length+1))) {
				return null;
			} 
			
			return getPackageFragment().getClassFile(new String(enclosingTypeName) + SUFFIX_STRING_class).getType();
		} catch (JavaModelException npe) {
			return null;
		}
	} else {
		// cannot access .class file without opening it 
		// and getDeclaringType() is supposed to be a handle-only method,
		// so default to assuming $ is an enclosing type separator
		String classFileName = classFile.getElementName();
		int lastDollar = -1;
		for (int i = 0, length = classFileName.length(); i < length; i++) {
			char c = classFileName.charAt(i);
			if (Character.isDigit(c) && lastDollar == i-1) {
				// anonymous or local type
				return null;
			} else if (c == '$') {
				lastDollar = i;
			}
		}
		if (lastDollar == -1) {
			return null;
		} else {
			String enclosingName = classFileName.substring(0, lastDollar);
			String enclosingClassFileName = enclosingName + SUFFIX_STRING_class;
			return 
				new BinaryType(
					(JavaElement)this.getPackageFragment().getClassFile(enclosingClassFileName),
					Util.localTypeName(enclosingName, enclosingName.lastIndexOf('$'), enclosingName.length()));
		}
	}
}
public Object getElementInfo(IProgressMonitor monitor) throws JavaModelException {
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	Object info = manager.getInfo(this);
	if (info != null && info != JavaModelCache.NON_EXISTING_JAR_TYPE_INFO) return info;
	return openWhenClosed(createElementInfo(), monitor);
}
/*
 * @see IJavaElement
 */
public int getElementType() {
	return TYPE;
}
/*
 * @see IType#getField(String name)
 */
public IField getField(String fieldName) {
	return new BinaryField(this, fieldName);
}
/*
 * @see IType#getFields()
 */
public IField[] getFields() throws JavaModelException {
	ArrayList list = getChildrenOfType(FIELD);
	int size;
	if ((size = list.size()) == 0) {
		return NO_FIELDS;
	} else {
		IField[] array= new IField[size];
		list.toArray(array);
		return array;
	}
}
/*
 * @see IMember#getFlags()
 */
public int getFlags() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return info.getModifiers();
}
/*
 * @see IType#getFullyQualifiedName()
 */
public String getFullyQualifiedName() {
	return this.getFullyQualifiedName('$');
}
/*
 * @see IType#getFullyQualifiedName(char enclosingTypeSeparator)
 */
public String getFullyQualifiedName(char enclosingTypeSeparator) {
	try {
		return getFullyQualifiedName(enclosingTypeSeparator, false/*don't show parameters*/);
	} catch (JavaModelException e) {
		// exception thrown only when showing parameters
		return null;
	}
}

/*
 * @see IType#getFullyQualifiedParameterizedName()
 */
public String getFullyQualifiedParameterizedName() throws JavaModelException {
	return getFullyQualifiedName('.', true/*show parameters*/);
}

/*
 * @see JavaElement
 */
public IJavaElement getHandleFromMemento(String token, MementoTokenizer memento, WorkingCopyOwner workingCopyOwner) {
	switch (token.charAt(0)) {
		case JEM_COUNT:
			return getHandleUpdatingCountFromMemento(memento, workingCopyOwner);
		case JEM_FIELD:
			if (!memento.hasMoreTokens()) return this;
			String fieldName = memento.nextToken();
			JavaElement field = (JavaElement)getField(fieldName);
			return field.getHandleFromMemento(memento, workingCopyOwner);
		case JEM_INITIALIZER:
			if (!memento.hasMoreTokens()) return this;
			String count = memento.nextToken();
			JavaElement initializer = (JavaElement)getInitializer(Integer.parseInt(count));
			return initializer.getHandleFromMemento(memento, workingCopyOwner);
		case JEM_METHOD:
			if (!memento.hasMoreTokens()) return this;
			String selector = memento.nextToken();
			ArrayList params = new ArrayList();
			nextParam: while (memento.hasMoreTokens()) {
				token = memento.nextToken();
				switch (token.charAt(0)) {
					case JEM_TYPE:
					case JEM_TYPE_PARAMETER:
						break nextParam;
					case JEM_METHOD:
						if (!memento.hasMoreTokens()) return this;
						String param = memento.nextToken();
						StringBuffer buffer = new StringBuffer();
						while (param.length() == 1 && Signature.C_ARRAY == param.charAt(0)) { // backward compatible with 3.0 mementos
							buffer.append(Signature.C_ARRAY);
							if (!memento.hasMoreTokens()) return this;
							param = memento.nextToken();
						}
						params.add(buffer.toString() + param);
						break;
					default:
						break nextParam;
				}
			}
			String[] parameters = new String[params.size()];
			params.toArray(parameters);
			JavaElement method = (JavaElement)getMethod(selector, parameters);
			if (token != null) {
				switch (token.charAt(0)) {
					case JEM_TYPE:
					case JEM_TYPE_PARAMETER:
					case JEM_LOCALVARIABLE:
						return method.getHandleFromMemento(token, memento, workingCopyOwner);
					default:
						return method;
				}
			} else {
				return method;
			}
		case JEM_TYPE:
			String typeName;
			if (memento.hasMoreTokens()) {
				typeName = memento.nextToken();
				char firstChar = typeName.charAt(0);
				if (firstChar == JEM_FIELD || firstChar == JEM_INITIALIZER || firstChar == JEM_METHOD || firstChar == JEM_TYPE || firstChar == JEM_COUNT) {
					token = typeName;
					typeName = ""; //$NON-NLS-1$
				} else {
					token = null;
				}
			} else {
				typeName = ""; //$NON-NLS-1$
				token = null;
			}
			JavaElement type = (JavaElement)getType(typeName);
			if (token == null) {
				return type.getHandleFromMemento(memento, workingCopyOwner);
			} else {
				return type.getHandleFromMemento(token, memento, workingCopyOwner);
			}
		case JEM_TYPE_PARAMETER:
			if (!memento.hasMoreTokens()) return this;
			String typeParameterName = memento.nextToken();
			JavaElement typeParameter = new TypeParameter(this, typeParameterName);
			return typeParameter.getHandleFromMemento(memento, workingCopyOwner);
	}
	return null;
}
/*
 * @see IType#getInitializer(int occurrenceCount)
 */
public IInitializer getInitializer(int count) {
	return new Initializer(this, count);
}
/*
 * @see IType#getInitializers()
 */
public IInitializer[] getInitializers() {
	return NO_INITIALIZERS;
}
public String getKey(boolean forceOpen) throws JavaModelException {
	return getKey(this, forceOpen);
}
/*
 * @see IType#getMethod(String name, String[] parameterTypeSignatures)
 */
public IMethod getMethod(String selector, String[] parameterTypeSignatures) {
	return new BinaryMethod(this, selector, parameterTypeSignatures);
}
/*
 * @see IType#getMethods()
 */
public IMethod[] getMethods() throws JavaModelException {
	ArrayList list = getChildrenOfType(METHOD);
	int size;
	if ((size = list.size()) == 0) {
		return NO_METHODS;
	} else {
		IMethod[] array= new IMethod[size];
		list.toArray(array);
		return array;
	}
}
/*
 * @see IType#getPackageFragment()
 */
public IPackageFragment getPackageFragment() {
	IJavaElement parentElement = this.parent;
	while (parentElement != null) {
		if (parentElement.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
			return (IPackageFragment)parentElement;
		}
		else {
			parentElement = parentElement.getParent();
		}
	}
	Assert.isTrue(false);  // should not happen
	return null;
}

/**
 * @see IType#getSuperclassTypeSignature()
 * @since 3.0
 */
public String getSuperclassTypeSignature() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	char[] genericSignature = info.getGenericSignature();
	if (genericSignature != null) {
		int signatureLength = genericSignature.length;
		// skip type parameters
		int index = 0;
		if (genericSignature[0] == '<') {
			int count = 1;
			while (count > 0 && ++index < signatureLength) {
				switch (genericSignature[index]) {
					case '<': 
						count++;
						break;
					case '>':
						count--;
						break;
				}
			}
			index++;
		}
		int start = index;
		index = Util.scanClassTypeSignature(genericSignature, start) + 1;
		char[] superclassSig = CharOperation.subarray(genericSignature, start, index);
		return new String(ClassFile.translatedName(superclassSig));
	} else {
		char[] superclassName = info.getSuperclassName();
		if (superclassName == null) {
			return null;
		}
		return new String(Signature.createTypeSignature(ClassFile.translatedName(superclassName), true));
	}
}

public String getSourceFileName(IBinaryType info) {
	if (info == null) {
		try {
			info = (IBinaryType) getElementInfo();
		} catch (JavaModelException e) {
			// default to using the outer most declaring type name
			IType type = this;
			IType enclosingType = getDeclaringType();
			while (enclosingType != null) {
				type = enclosingType;
				enclosingType = type.getDeclaringType();
			}
			return type.getElementName() + Util.defaultJavaExtension();
		}
	}
	return sourceFileName(info);
}

/*
 * @see IType#getSuperclassName()
 */
public String getSuperclassName() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	char[] superclassName = info.getSuperclassName();
	if (superclassName == null) {
		return null;
	}
	return new String(ClassFile.translatedName(superclassName));
}
/*
 * @see IType#getSuperInterfaceNames()
 */
public String[] getSuperInterfaceNames() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	char[][] names= info.getInterfaceNames();
	int length;
	if (names == null || (length = names.length) == 0) {
		return CharOperation.NO_STRINGS;
	}
	names= ClassFile.translatedNames(names);
	String[] strings= new String[length];
	for (int i= 0; i < length; i++) {
		strings[i]= new String(names[i]);
	}
	return strings;
}

/**
 * @see IType#getSuperInterfaceTypeSignatures()
 * @since 3.0
 */
public String[] getSuperInterfaceTypeSignatures() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	char[] genericSignature = info.getGenericSignature();
	if (genericSignature != null) {
		ArrayList interfaces = new ArrayList();
		int signatureLength = genericSignature.length;
		// skip type parameters
		int index = 0;
		if (genericSignature[0] == '<') {
			int count = 1;
			while (count > 0 && ++index < signatureLength) {
				switch (genericSignature[index]) {
					case '<': 
						count++;
						break;
					case '>':
						count--;
						break;
				}
			}
			index++;
		}
		// skip superclass
		index = Util.scanClassTypeSignature(genericSignature, index) + 1;
		while (index  < signatureLength) {
			int start = index;
			index = Util.scanClassTypeSignature(genericSignature, start) + 1;
			char[] interfaceSig = CharOperation.subarray(genericSignature, start, index);
			interfaces.add(new String(ClassFile.translatedName(interfaceSig)));
		}
		int size = interfaces.size();
		String[] result = new String[size];
		interfaces.toArray(result);
		return result;
	} else {
		char[][] names= info.getInterfaceNames();
		int length;
		if (names == null || (length = names.length) == 0) {
			return CharOperation.NO_STRINGS;
		}
		names= ClassFile.translatedNames(names);
		String[] strings= new String[length];
		for (int i= 0; i < length; i++) {
			strings[i]= new String(Signature.createTypeSignature(names[i], true));
		}
		return strings;
	}
}

public ITypeParameter[] getTypeParameters() throws JavaModelException {
	String[] typeParameterSignatures = getTypeParameterSignatures();
	int length = typeParameterSignatures.length;
	if (length == 0) return TypeParameter.NO_TYPE_PARAMETERS;
	ITypeParameter[] typeParameters = new ITypeParameter[length];
	for (int i = 0; i < typeParameterSignatures.length; i++) {
		String typeParameterName = Signature.getTypeVariable(typeParameterSignatures[i]);
		typeParameters[i] = new TypeParameter(this, typeParameterName);
	}
	return typeParameters;
}

/**
 * @see IType#getTypeParameterSignatures()
 * @since 3.0
 */
public String[] getTypeParameterSignatures() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	char[] genericSignature = info.getGenericSignature();
	if (genericSignature == null) 
		return CharOperation.NO_STRINGS;
	
	char[] dotBaseSignature = CharOperation.replaceOnCopy(genericSignature, '/', '.');
	char[][] typeParams = Signature.getTypeParameters(dotBaseSignature);
	return CharOperation.toStrings(typeParams);
}

/*
 * @see IType#getType(String)
 */
public IType getType(String typeName) {
	IClassFile classFile= getPackageFragment().getClassFile(getTypeQualifiedName() + "$" + typeName + SUFFIX_STRING_class); //$NON-NLS-1$
	return new BinaryType((JavaElement)classFile, typeName);
}
public ITypeParameter getTypeParameter(String typeParameterName) {
	return new TypeParameter(this, typeParameterName);
}
/*
 * @see IType#getTypeQualifiedName()
 */
public String getTypeQualifiedName() {
	return this.getTypeQualifiedName('$');
}
/*
 * @see IType#getTypeQualifiedName(char)
 */
public String getTypeQualifiedName(char enclosingTypeSeparator) {
	try {
		return getTypeQualifiedName(enclosingTypeSeparator, false/*don't show parameters*/);
	} catch (JavaModelException e) {
		// exception thrown only when showing parameters
		return null;
	}
}
/*
 * @see IType#getTypes()
 */
public IType[] getTypes() throws JavaModelException {
	ArrayList list = getChildrenOfType(TYPE);
	int size;
	if ((size = list.size()) == 0) {
		return NO_TYPES;
	} else {
		IType[] array= new IType[size];
		list.toArray(array);
		return array;
	}
}

/*
 * @see IType#isAnonymous()
 */
public boolean isAnonymous() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return info.isAnonymous();
}
/*
 * @see IType#isClass()
 */
public boolean isClass() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return TypeDeclaration.kind(info.getModifiers()) == TypeDeclaration.CLASS_DECL;

}

/**
 * @see IType#isEnum()
 * @since 3.0
 */
public boolean isEnum() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return TypeDeclaration.kind(info.getModifiers()) == TypeDeclaration.ENUM_DECL;
}

/*
 * @see IType#isInterface()
 */
public boolean isInterface() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	switch (TypeDeclaration.kind(info.getModifiers())) {
		case TypeDeclaration.INTERFACE_DECL:
		case TypeDeclaration.ANNOTATION_TYPE_DECL: // annotation is interface too
			return true;
	}
	return false;
}
/**
 * @see IType#isAnnotation()
 * @since 3.0
 */
public boolean isAnnotation() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return TypeDeclaration.kind(info.getModifiers()) == TypeDeclaration.ANNOTATION_TYPE_DECL;
}

/*
 * @see IType#isLocal()
 */
public boolean isLocal() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return info.isLocal();
}
/*
 * @see IType#isMember()
 */
public boolean isMember() throws JavaModelException {
	IBinaryType info = (IBinaryType) getElementInfo();
	return info.isMember();
}
/* (non-Javadoc)
 * @see org.eclipse.jdt.core.IType#isResolved()
 */
public boolean isResolved() {
	return false;
}
/*
 * @see IType
 */
public ITypeHierarchy loadTypeHierachy(InputStream input, IProgressMonitor monitor) throws JavaModelException {
	return loadTypeHierachy(input, DefaultWorkingCopyOwner.PRIMARY, monitor);
}
/*
 * @see IType
 */
public ITypeHierarchy loadTypeHierachy(InputStream input, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException {
	return TypeHierarchy.load(this, input, owner);
}
/*
 * @see IType#newSupertypeHierarchy(IProgressMonitor monitor)
 */
public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	return this.newSupertypeHierarchy(DefaultWorkingCopyOwner.PRIMARY, monitor);
}
/*
 *@see IType#newSupertypeHierarchy(ICompilationUnit[], IProgressMonitor monitor)
 */
public ITypeHierarchy newSupertypeHierarchy(
	ICompilationUnit[] workingCopies,
	IProgressMonitor monitor)
	throws JavaModelException {
	
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, workingCopies, SearchEngine.createWorkspaceScope(), false);
	op.runOperation(monitor);
	return op.getResult();
}
/**
 * @param workingCopies the working copies that take precedence over their original compilation units
 * @param monitor the given progress monitor
 * @return a type hierarchy for this type containing this type and all of its supertypes
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 *
 * @see IType#newSupertypeHierarchy(IWorkingCopy[], IProgressMonitor)
 * @deprecated
 */
public ITypeHierarchy newSupertypeHierarchy(
	IWorkingCopy[] workingCopies,
	IProgressMonitor monitor)
	throws JavaModelException {
	
	ICompilationUnit[] copies;
	if (workingCopies == null) {
		copies = null;
	} else {
		int length = workingCopies.length;
		System.arraycopy(workingCopies, 0, copies = new ICompilationUnit[length], 0, length);
	}
	return newSupertypeHierarchy(copies, monitor);
}
/*
 * @see IType#newSupertypeHierarchy(WorkingCopyOwner, IProgressMonitor)
 */
public ITypeHierarchy newSupertypeHierarchy(
	WorkingCopyOwner owner,
	IProgressMonitor monitor)
	throws JavaModelException {

	ICompilationUnit[] workingCopies = JavaModelManager.getJavaModelManager().getWorkingCopies(owner, true/*add primary working copies*/);
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, workingCopies, SearchEngine.createWorkspaceScope(), false);
	op.runOperation(monitor);
	return op.getResult();
}
/*
 * @see IType#newTypeHierarchy(IJavaProject, IProgressMonitor)
 */
public ITypeHierarchy newTypeHierarchy(IJavaProject project, IProgressMonitor monitor) throws JavaModelException {
	return newTypeHierarchy(project, DefaultWorkingCopyOwner.PRIMARY, monitor);
}
/*
 * @see IType#newTypeHierarchy(IJavaProject, WorkingCopyOwner, IProgressMonitor)
 */
public ITypeHierarchy newTypeHierarchy(IJavaProject project, WorkingCopyOwner owner, IProgressMonitor monitor) throws JavaModelException {
	if (project == null) {
		throw new IllegalArgumentException(Messages.hierarchy_nullProject); 
	}
	ICompilationUnit[] workingCopies = JavaModelManager.getJavaModelManager().getWorkingCopies(owner, true/*add primary working copies*/);
	ICompilationUnit[] projectWCs = null;
	if (workingCopies != null) {
		int length = workingCopies.length;
		projectWCs = new ICompilationUnit[length];
		int index = 0;
		for (int i = 0; i < length; i++) {
			ICompilationUnit wc = workingCopies[i];
			if (project.equals(wc.getJavaProject())) {
				projectWCs[index++] = wc;
			}
		}
		if (index != length) {
			System.arraycopy(projectWCs, 0, projectWCs = new ICompilationUnit[index], 0, index);
		}
	}
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(
		this, 
		projectWCs,
		project, 
		true);
	op.runOperation(monitor);
	return op.getResult();
}
/**
 * @param monitor the given progress monitor
 * @exception JavaModelException if this element does not exist or if an
 *		exception occurs while accessing its corresponding resource.
 * @return a type hierarchy for this type containing
 * 
 * @see IType#newTypeHierarchy(IProgressMonitor monitor)
 * @deprecated
 */
public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	return newTypeHierarchy((IWorkingCopy[])null, monitor);
}
/*
 * @see IType#newTypeHierarchy(ICompilationUnit[], IProgressMonitor)
 */
public ITypeHierarchy newTypeHierarchy(
	ICompilationUnit[] workingCopies,
	IProgressMonitor monitor)
	throws JavaModelException {

	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, workingCopies, SearchEngine.createWorkspaceScope(), true);
	op.runOperation(monitor);
	return op.getResult();
}
/**
 * @see IType#newTypeHierarchy(IWorkingCopy[], IProgressMonitor)
 * @deprecated
 */
public ITypeHierarchy newTypeHierarchy(
	IWorkingCopy[] workingCopies,
	IProgressMonitor monitor)
	throws JavaModelException {

	ICompilationUnit[] copies;
	if (workingCopies == null) {
		copies = null;
	} else {
		int length = workingCopies.length;
		System.arraycopy(workingCopies, 0, copies = new ICompilationUnit[length], 0, length);
	}
	return newTypeHierarchy(copies, monitor);
}
/*
 * @see IType#newTypeHierarchy(WorkingCopyOwner, IProgressMonitor)
 */
public ITypeHierarchy newTypeHierarchy(
	WorkingCopyOwner owner,
	IProgressMonitor monitor)
	throws JavaModelException {
		
	ICompilationUnit[] workingCopies = JavaModelManager.getJavaModelManager().getWorkingCopies(owner, true/*add primary working copies*/);
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, workingCopies, SearchEngine.createWorkspaceScope(), true);
	op.runOperation(monitor);
	return op.getResult();	
}
public JavaElement resolved(Binding binding) {
	SourceRefElement resolvedHandle = new ResolvedBinaryType(this.parent, this.name, new String(binding.computeUniqueKey()));
	resolvedHandle.occurrenceCount = this.occurrenceCount;
	return resolvedHandle;
}
/*
 * @see IType#resolveType(String)
 */
public String[][] resolveType(String typeName) {
	// not implemented for binary types
	return null;
}
/*
 * @see IType#resolveType(String, WorkingCopyOwner)
 */
public String[][] resolveType(String typeName, WorkingCopyOwner owner) {
	// not implemented for binary types
	return null;
}
/*
 * Returns the source file name as defined in the given info.
 * If not present in the info, infers it from this type.
 */
public String sourceFileName(IBinaryType info) {
	char[] sourceFileName = info.sourceFileName();
	if (sourceFileName == null) {
		/*
		 * We assume that this type has been compiled from a file with its name
		 * For example, A.class comes from A.java and p.A.class comes from a file A.java
		 * in the folder p.
		 */
		if (info.isMember()) {
			IType enclosingType = getDeclaringType();
			if (enclosingType == null) return null; // play it safe
			while (enclosingType.getDeclaringType() != null) {
				enclosingType = enclosingType.getDeclaringType();
			}
			return enclosingType.getElementName() + Util.defaultJavaExtension();
		} else if (info.isLocal() || info.isAnonymous()){
			String typeQualifiedName = getTypeQualifiedName();
			int dollar = typeQualifiedName.indexOf('$');
			if (dollar == -1) {
				// malformed inner type: name doesn't contain a dollar
				return getElementName() + Util.defaultJavaExtension();
			}
			return typeQualifiedName.substring(0, dollar) + Util.defaultJavaExtension();
		} else {
			return getElementName() + Util.defaultJavaExtension();
		}
	} else {
		return  new String(sourceFileName);
	}
}
/*
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info, boolean showResolvedInfo) {
	buffer.append(this.tabString(tab));
	if (info == null) {
		toStringName(buffer);
		buffer.append(" (not open)"); //$NON-NLS-1$
	} else if (info == NO_INFO) {
		toStringName(buffer);
	} else {
		try {
			if (this.isAnnotation()) {
				buffer.append("@interface "); //$NON-NLS-1$
			} else if (this.isEnum()) {
				buffer.append("enum "); //$NON-NLS-1$
			} else if (this.isInterface()) {
				buffer.append("interface "); //$NON-NLS-1$
			} else {
				buffer.append("class "); //$NON-NLS-1$
			}
			toStringName(buffer);
		} catch (JavaModelException e) {
			buffer.append("<JavaModelException in toString of " + getElementName()); //$NON-NLS-1$
		}
	}
}
protected void toStringName(StringBuffer buffer) {
	if (getElementName().length() > 0)
		super.toStringName(buffer);
	else
		buffer.append("<anonymous>"); //$NON-NLS-1$
}
public String getAttachedJavadoc(IProgressMonitor monitor) throws JavaModelException {
	final String contents = getJavadocContents(monitor);
	if (contents == null) return null;
	final int indexOfStartOfClassData = contents.indexOf(JavadocConstants.START_OF_CLASS_DATA);
	if (indexOfStartOfClassData == -1) throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.UNKNOWN_JAVADOC_FORMAT, this));
	int indexOfNextSummary = contents.indexOf(JavadocConstants.NESTED_CLASS_SUMMARY);
	if (this.isEnum() && indexOfNextSummary == -1) {
		// try to find enum constant summary start
		indexOfNextSummary = contents.indexOf(JavadocConstants.ENUM_CONSTANT_SUMMARY);
	}
	if (this.isAnnotation() && indexOfNextSummary == -1) {
		// try to find required enum constant summary start
		indexOfNextSummary = contents.indexOf(JavadocConstants.ANNOTATION_TYPE_REQUIRED_MEMBER_SUMMARY);
		if (indexOfNextSummary == -1) {
			// try to find optional enum constant summary start
			indexOfNextSummary = contents.indexOf(JavadocConstants.ANNOTATION_TYPE_OPTIONAL_MEMBER_SUMMARY);
		}
	}
	if (indexOfNextSummary == -1) {
		// try to find field summary start
		indexOfNextSummary = contents.indexOf(JavadocConstants.FIELD_SUMMARY);
	}
	if (indexOfNextSummary == -1) {
		// try to find constructor summary start
		indexOfNextSummary = contents.indexOf(JavadocConstants.CONSTRUCTOR_SUMMARY);
	}
	if (indexOfNextSummary == -1) {
		// try to find method summary start
		indexOfNextSummary = contents.indexOf(JavadocConstants.METHOD_SUMMARY);
	}
	if (indexOfNextSummary == -1) {
		// we take the end of class data
		indexOfNextSummary = contents.indexOf(JavadocConstants.END_OF_CLASS_DATA);
	}
	if (indexOfNextSummary == -1) {
		throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.UNKNOWN_JAVADOC_FORMAT, this));
	}
	/*
	 * Check out to cut off the hierarchy see 119844
	 * We remove what the contents between the start of class data and the first <P>
	 */
	int start = indexOfStartOfClassData + JavadocConstants.START_OF_CLASS_DATA_LENGTH;
	int indexOfFirstParagraph = contents.indexOf("<P>", start); //$NON-NLS-1$
	if (indexOfFirstParagraph == -1) {
		indexOfFirstParagraph = contents.indexOf("<p>", start); //$NON-NLS-1$
	}
	if (indexOfFirstParagraph != -1 && indexOfFirstParagraph < indexOfNextSummary) {
		start = indexOfFirstParagraph;
	}	
	return contents.substring(start, indexOfNextSummary);
}
public String getJavadocContents(IProgressMonitor monitor) throws JavaModelException {
	PerProjectInfo projectInfo = JavaModelManager.getJavaModelManager().getPerProjectInfoCheckExistence(this.getJavaProject().getProject());
	String cachedJavadoc = null;
	synchronized (projectInfo.javadocCache) {
		cachedJavadoc = (String) projectInfo.javadocCache.get(this);
	}
	if (cachedJavadoc != null && cachedJavadoc != EMPTY_JAVADOC) {
		return cachedJavadoc;
	}
	URL baseLocation= getJavadocBaseLocation();
	if (baseLocation == null) {
		return null;
	}
	StringBuffer pathBuffer = new StringBuffer(baseLocation.toExternalForm());

	if (!(pathBuffer.charAt(pathBuffer.length() - 1) == '/')) {
		pathBuffer.append('/');
	}
	IPackageFragment pack= this.getPackageFragment();
	String typeQualifiedName = null;
	if (this.isMember()) {
		IType currentType = this;
		StringBuffer typeName = new StringBuffer();
		while (currentType != null) {
			typeName.insert(0, currentType.getElementName());
			currentType = currentType.getDeclaringType();
			if (currentType != null) {
				typeName.insert(0, '.');
			}
		}
		typeQualifiedName = new String(typeName.toString());
	} else {
		typeQualifiedName = this.getElementName();
	}
	
	pathBuffer.append(pack.getElementName().replace('.', '/')).append('/').append(typeQualifiedName).append(JavadocConstants.HTML_EXTENSION);
	
	if (monitor != null && monitor.isCanceled()) throw new OperationCanceledException();
	final String contents = getURLContents(String.valueOf(pathBuffer));
	synchronized (projectInfo.javadocCache) {
		projectInfo.javadocCache.put(this, contents);
	}
	return contents;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7633.java