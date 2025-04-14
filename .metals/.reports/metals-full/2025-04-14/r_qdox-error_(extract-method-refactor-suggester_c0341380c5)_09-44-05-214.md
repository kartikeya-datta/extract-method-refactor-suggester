error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7631.java
text:
```scala
t@@his.imports = new ISourceImport[length];

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
package org.eclipse.jdt.internal.core;

import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.env.IConstants;
import org.eclipse.jdt.internal.compiler.env.ISourceField;
import org.eclipse.jdt.internal.compiler.env.ISourceImport;
import org.eclipse.jdt.internal.compiler.env.ISourceMethod;
import org.eclipse.jdt.internal.compiler.env.ISourceType;

/** 
 * Element info for an IType element that originated from source. 
 */
public class SourceTypeElementInfo extends MemberElementInfo implements ISourceType {

	protected static final ISourceImport[] NO_IMPORTS = new ISourceImport[0];
	protected static final ISourceField[] NO_FIELDS = new ISourceField[0];
	protected static final ISourceMethod[] NO_METHODS = new ISourceMethod[0];
	protected static final ISourceType[] NO_TYPES = new ISourceType[0];
	/**
	 * The name of the superclass for this type. This name
	 * is fully qualified for binary types and is NOT
	 * fully qualified for source types.
	 */
	protected char[] superclassName;
	
	/**
	 * The names of the interfaces this type implements or
	 * extends. These names are fully qualified in the case
	 * of a binary type, and are NOT fully qualified in the
	 * case of a source type
	 */
	protected char[][] superInterfaceNames;
	
	/**
	 * The enclosing type name for this type.
	 *
	 * @see getEnclosingTypeName
	 */
	protected char[] enclosingTypeName;

	/**
	 * The name of the source file this type is declared in.
	 */
	protected char[] sourceFileName;

	/**
	 * The name of the package this type is contained in.
	 */
	protected char[] packageName;

	/**
	 * The qualified name of this type.
	 */
	protected char[] qualifiedName;

	/**
	 * The infos of the imports in this type's compilation unit
	 */
	private ISourceImport[] imports;
	
	/**
	 * Backpointer to my type handle - useful for translation
	 * from info to handle.
	 */
	protected IType fHandle= null;

/**
 * Returns the ISourceType that is the enclosing type for this
 * type, or <code>null</code> if this type is a top level type.
 */
public ISourceType getEnclosingType() {
	IJavaElement parent= fHandle.getParent();
	if (parent != null && parent.getElementType() == IJavaElement.TYPE) {
		try {
			return (ISourceType)((JavaElement)parent).getElementInfo();
		} catch (JavaModelException e) {
			return null;
		}
	} else {
		return null;
	}
}
/**
 * @see ISourceType
 */
public char[] getEnclosingTypeName() {
	return enclosingTypeName;
}
/**
 * @see ISourceType
 */
public ISourceField[] getFields() {
	int length = this.fChildren.length;
	if (length == 0) return NO_FIELDS;
	ISourceField[] fields = new ISourceField[length];
	int fieldIndex = 0;
	for (int i = 0; i < length; i++) {
		IJavaElement child = this.fChildren[i];
		if (child instanceof SourceField) {
			try {
				ISourceField field = (ISourceField)((SourceField)child).getElementInfo();
				fields[fieldIndex++] = field;
			} catch (JavaModelException e) {
			}
		}
	}
	if (fieldIndex == 0) return NO_FIELDS;
	System.arraycopy(fields, 0, fields = new ISourceField[fieldIndex], 0, fieldIndex);
	return fields;
}
/**
 * @see ISourceType
 */
public char[] getFileName() {
	return this.sourceFileName;
}
/**
 * Returns the handle for this type info
 */
public IType getHandle() {
	return this.fHandle;
}
/**
 * @see ISourceType
 */
public ISourceImport[] getImports() {
	if (this.imports == null) {
		try {
			IImportDeclaration[] importDeclarations = this.fHandle.getCompilationUnit().getImports();
			int length = importDeclarations.length;
			if (length == 0) {
				this.imports = NO_IMPORTS;
			} else {
				this.imports = new ISourceImport[importDeclarations.length];
				for (int i = 0; i < length; i++) {
					imports[i] = (ImportDeclarationElementInfo)((ImportDeclaration)importDeclarations[i]).getElementInfo();
				}
			}
		} catch (JavaModelException e) {
			this.imports = NO_IMPORTS;
		}
	}
	return this.imports;
}
/**
 * @see ISourceType
 */
public char[][] getInterfaceNames() {
	return this.superInterfaceNames;
}
/**
 * @see ISourceType
 */
public ISourceType[] getMemberTypes() {
	int length = this.fChildren.length;
	if (length == 0) return NO_TYPES;
	ISourceType[] memberTypes = new ISourceType[length];
	int typeIndex = 0;
	for (int i = 0; i < length; i++) {
		IJavaElement child = fChildren[i];
		if (child instanceof SourceType) {
			try {
				ISourceType type = (ISourceType)((SourceType)child).getElementInfo();
				memberTypes[typeIndex++] = type;
			} catch (JavaModelException e) {
			}
		}
	}
	if (typeIndex == 0) return NO_TYPES;
	System.arraycopy(memberTypes, 0, memberTypes = new ISourceType[typeIndex], 0, typeIndex);
	return memberTypes;
}
/**
 * @see ISourceType
 */
public ISourceMethod[] getMethods() {
	int length = fChildren.length;
	if (length == 0) return NO_METHODS;
	ISourceMethod[] methods = new ISourceMethod[length];
	int methodIndex = 0;
	for (int i = 0; i < length; i++) {
		IJavaElement child = fChildren[i];
		if (child instanceof SourceMethod) {
			try {
				ISourceMethod method = (ISourceMethod)((SourceMethod)child).getElementInfo();
				methods[methodIndex++] = method;
			} catch (JavaModelException e) {
			}
		}
	}
	if (methodIndex == 0) return NO_METHODS;
	System.arraycopy(methods, 0, methods = new ISourceMethod[methodIndex], 0, methodIndex);
	return methods;
}
/**
 * @see ISourceType
 */
public char[] getPackageName() {
	return this.packageName;
}
/**
 * @see ISourceType
 */
public char[] getQualifiedName() {
	return this.qualifiedName;
}
/**
 * @see ISourceType
 */
public char[] getSuperclassName() {
	return superclassName;
}
/**
 * @see ISourceType
 */
public boolean isBinaryType() {
	return false;
}
/**
 * @see ISourceType
 */
public boolean isClass() {
	return (this.flags & IConstants.AccInterface) == 0;
}
/**
 * @see ISourceType
 */
public boolean isInterface() {
	return (this.flags & IConstants.AccInterface) != 0;
}
/**
 * Sets the (unqualified) name of the type that encloses this type.
 */
protected void setEnclosingTypeName(char[] enclosingTypeName) {
	this.enclosingTypeName = enclosingTypeName;
}
/**
 * Sets the handle for this type info
 */
protected void setHandle(IType handle) {
	this.fHandle= handle;
}
/**
 * Sets the name of the package this type is declared in.
 */
protected void setPackageName(char[] name) {
	this.packageName= name;
}
/**
 * Sets this type's qualified name.
 */
protected void setQualifiedName(char[] name) {
	this.qualifiedName= name;
}
/**
 * Sets the name of the source file this type is declared in.
 */
protected void setSourceFileName(char[] name) {
	this.sourceFileName= name;
}
/**
 * Sets the (unqualified) name of this type's superclass
 */
protected void setSuperclassName(char[] superclassName) {
	this.superclassName = superclassName;
}
/**
 * Sets the (unqualified) names of the interfaces this type implements or extends
 */
protected void setSuperInterfaceNames(char[][] superInterfaceNames) {
	this.superInterfaceNames = superInterfaceNames;
}
public String toString() {
	return "Info for " + fHandle.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7631.java