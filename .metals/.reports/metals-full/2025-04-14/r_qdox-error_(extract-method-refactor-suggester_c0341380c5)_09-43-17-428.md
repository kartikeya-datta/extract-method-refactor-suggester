error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4653.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4653.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4653.java
text:
```scala
S@@ystem.out.println("CLOSING Element ("+ Thread.currentThread()+"): " + this.getHandleIdentifier());  //$NON-NLS-1$//$NON-NLS-2$

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.core.resources.*;
import java.util.Vector;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.codeassist.ISelectionRequestor;
import org.eclipse.jdt.core.*;

/**
 * Parent is an IClassFile.
 *
 * @see IType
 */

public class BinaryType extends BinaryMember implements IType {
	private static final IField[] NO_FIELDS = new IField[0];
	private static final IMethod[] NO_METHODS = new IMethod[0];
	private static final IType[] NO_TYPES = new IType[0];
	private static final IInitializer[] NO_INITIALIZERS = new IInitializer[0];
	private static final String[] NO_STRINGS = new String[0];
protected BinaryType(IJavaElement parent, String name) {
	super(TYPE, parent, name);
	Assert.isTrue(name.indexOf('.') == -1);
}
/**
 * @see IOpenable
 */
public void close() throws JavaModelException {
	
	Object info = fgJavaModelManager.peekAtInfo(this);
	if (info != null) {
		ClassFileInfo cfi = getClassFileInfo();
		if (cfi.hasReadBinaryChildren()) {
			try {
				IJavaElement[] children = getChildren();
				for (int i = 0, size = children.length; i < size; ++i) {
					JavaElement child = (JavaElement) children[i];
					child.close();
				}
			} catch (JavaModelException e) {
			}
		}
		closing(info);
		fgJavaModelManager.removeInfo(this);
	}
}
/**
 * Remove my cached children from the Java Model
 */
protected void closing(Object info) throws JavaModelException {
	if (JavaModelManager.VERBOSE){
		System.out.println("CLOSING Element ("+ Thread.currentThread()+"): " + this.getHandleIdentifier()); 
	}
	ClassFileInfo cfi = getClassFileInfo();
	cfi.removeBinaryChildren();
}
/**
 * @see IType
 */
public IField createField(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/**
 * @see IType
 */
public IInitializer createInitializer(String contents, IJavaElement sibling, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/**
 * @see IType
 */
public IMethod createMethod(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/**
 * @see IType
 */
public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) throws JavaModelException {
	throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.READ_ONLY, this));
}
/**
 * @see IParent 
 */
public IJavaElement[] getChildren() throws JavaModelException {
	// ensure present
	// fix for 1FWWVYT
	if (!exists()) {
		throw newNotPresentException();
	}
	// get children
	ClassFileInfo cfi = getClassFileInfo();
	return cfi.getBinaryChildren();
}
protected ClassFileInfo getClassFileInfo() throws JavaModelException {
	ClassFile cf = (ClassFile) fParent;
	return (ClassFileInfo) cf.getElementInfo();
}
/**
 * @see IMember
 */
public IType getDeclaringType() {
	try {
		char[] enclosingTypeName = ((IBinaryType) getRawInfo()).getEnclosingTypeName();
		if (enclosingTypeName == null) {
			return null;
		}
		enclosingTypeName = ClassFile.unqualifiedName(enclosingTypeName);
		return getPackageFragment().getClassFile(new String(enclosingTypeName) + ".class").getType(); //$NON-NLS-1$
	} catch (JavaModelException npe) {
		return null;
	}
}
/**
 * @see IType#getField
 */
public IField getField(String name) {
	return new BinaryField(this, name);
}
/**
 * @see IType
 */
public IField[] getFields() throws JavaModelException {
	Vector v= getChildrenOfType(FIELD);
	int size;
	if ((size = v.size()) == 0) {
		return NO_FIELDS;
	} else {
		IField[] array= new IField[size];
		v.copyInto(array);
		return array;
	}
}
/**
 * @see IMember
 */
public int getFlags() throws JavaModelException {
	IBinaryType info = (IBinaryType) getRawInfo();
	return info.getModifiers();
}
/**
 * @see IType
 */
public String getFullyQualifiedName() {
	String packageName = getPackageFragment().getElementName();
	if (packageName.equals(IPackageFragment.DEFAULT_PACKAGE_NAME)) {
		return getTypeQualifiedName();
	}
	return packageName + '.' + getTypeQualifiedName();
}
/**
 * @see IType
 */
public IInitializer getInitializer(int occurrenceCount) {
	return new Initializer(this, occurrenceCount);
}
/**
 * @see IType
 */
public IInitializer[] getInitializers() {
	return NO_INITIALIZERS;
}
/**
 * @see IType#getMethod
 */
public IMethod getMethod(String name, String[] parameterTypeSignatures) {
	return new BinaryMethod(this, name, parameterTypeSignatures);
}
/**
 * @see IType
 */
public IMethod[] getMethods() throws JavaModelException {
	Vector v= getChildrenOfType(METHOD);
	int size;
	if ((size = v.size()) == 0) {
		return NO_METHODS;
	} else {
		IMethod[] array= new IMethod[size];
		v.copyInto(array);
		return array;
	}
}
/**
 * @see IType
 */
public IPackageFragment getPackageFragment() {
	IJavaElement parent = fParent;
	while (parent != null) {
		if (parent.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
			return (IPackageFragment) parent;
		}
		else {
			parent = parent.getParent();
		}
	}
	Assert.isTrue(false);  // should not happen
	return null;
}
/**
 * @see IType#getSuperclassName
 */
public String getSuperclassName() throws JavaModelException {
	IBinaryType info = (IBinaryType) getRawInfo();
	char[] superclassName = info.getSuperclassName();
	if (superclassName == null) {
		return null;
	}
	return new String(ClassFile.translatedName(superclassName));
}
/**
 * @see IType#getSuperInterfaceNames
 */
public String[] getSuperInterfaceNames() throws JavaModelException {
	IBinaryType info = (IBinaryType) getRawInfo();
	char[][] names= info.getInterfaceNames();
	int length;
	if (names == null || (length = names.length) == 0) {
		return NO_STRINGS;
	}
	names= ClassFile.translatedNames(names);
	String[] strings= new String[length];
	for (int i= 0; i < length; i++) {
		strings[i]= new String(names[i]);
	}
	return strings;
}
/**
 * @see IType#getType
 */
public IType getType(String name) {
	IClassFile classFile= getPackageFragment().getClassFile(getTypeQualifiedName() + "$" + name + ".class"); //$NON-NLS-2$ //$NON-NLS-1$
	return new BinaryType(classFile, name);
}
/**
 * @see IType#getTypeQualifiedName
 */
public String getTypeQualifiedName() {
	if (fParent.getElementType() == IJavaElement.CLASS_FILE) {
		String name= fParent.getElementName();
		return name.substring(0,name.lastIndexOf('.'));
	}
	if (fParent.getElementType() == IJavaElement.TYPE) {
		if (Character.isDigit(fName.charAt(0))) {
			return ((IType) fParent).getTypeQualifiedName();
		} else {
			return ((IType) fParent).getTypeQualifiedName() + '$' + fName;
		}
	}
	Assert.isTrue(false); // should not be reachable
	return null;
}
/**
 * @see IType
 */
public IType[] getTypes() throws JavaModelException {
	Vector v= getChildrenOfType(TYPE);
	int size;
	if ((size = v.size()) == 0) {
		return NO_TYPES;
	} else {
		IType[] array= new IType[size];
		v.copyInto(array);
		return array;
	}
}
/**
 * @see IParent 
 */
public boolean hasChildren() throws JavaModelException {
	return getChildren().length > 0;
}
/**
 * @see IType
 */
public boolean isClass() throws JavaModelException {
	return !isInterface();
}
/**
 * @see IType#isInterface
 */
public boolean isInterface() throws JavaModelException {
	IBinaryType info = (IBinaryType) getRawInfo();
	return info.isInterface();
}
/**
 * @see IType
 */
public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, SearchEngine.createWorkspaceScope(), false);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * @see IType
 */
public ITypeHierarchy newTypeHierarchy(IProgressMonitor monitor) throws JavaModelException {
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(this, SearchEngine.createWorkspaceScope(), true);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * @see IType
 */
public ITypeHierarchy newTypeHierarchy(IJavaProject project, IProgressMonitor monitor) throws JavaModelException {
	if (project == null) {
		throw new IllegalArgumentException(Util.bind("hierarchy.nullProject")); //$NON-NLS-1$
	}
	CreateTypeHierarchyOperation op= new CreateTypeHierarchyOperation(
		this, 
		SearchEngine.createJavaSearchScope(new IJavaElement[] {project}), 
		true);
	runOperation(op, monitor);
	return op.getResult();
}
/**
 * Removes all cached info from the Java Model, including all children,
 * but does not close this element.
 */
protected void removeInfo() {
	Object info = fgJavaModelManager.peekAtInfo(this);
	if (info != null) {
		try {
			IJavaElement[] children = getChildren();
			for (int i = 0, size = children.length; i < size; ++i) {
				JavaElement child = (JavaElement) children[i];
				child.removeInfo();
			}
		} catch (JavaModelException e) {
		}
		fgJavaModelManager.removeInfo(this);
		try {
			ClassFileInfo cfi = getClassFileInfo();
			cfi.removeBinaryChildren();
		} catch (JavaModelException npe) {
		}
	}
}
public String[][] resolveType(String typeName) throws JavaModelException {
	// not implemented for binary types
	return null;
}
/**
 * @private Debugging purposes
 */
protected void toStringInfo(int tab, StringBuffer buffer, Object info) {
	if (info == null) {
		buffer.append(this.getElementName());
		buffer.append(" (not open)"); //$NON-NLS-1$
	} else {
		try {
			if (this.isInterface()) {
				buffer.append("interface "); //$NON-NLS-1$
			} else {
				buffer.append("class "); //$NON-NLS-1$
			}
			buffer.append(this.getElementName());
		} catch (JavaModelException e) {
			buffer.append("<JavaModelException in toString of " + getElementName()); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4653.java