error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4161.java
text:
```scala
f@@BinaryChildren = NO_CHILDREN;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.compiler.env.IBinaryField;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryNestedType;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;

/**
 * Element info for <code>ClassFile</code> handles.
 */
 
/* package */ class ClassFileInfo extends OpenableElementInfo implements SuffixConstants {
	/** 
	 * The children of the <code>BinaryType</code> corresponding to our
	 * <code>ClassFile</code>. These are kept here because we don't have
	 * access to the <code>BinaryType</code> info (<code>ClassFileReader</code>).
	 * <p>
	 * The children are lazily initialized, on the first call to
	 * <code>getBinaryChildren()</code>, which in turn is called by
	 * <code>BinaryType.getChildren()</code>. 
	 */
	protected IJavaElement[] fBinaryChildren = null;
	/**
	 * Back-pointer to the IClassFile to allow lazy initialization.
	 */
	protected IClassFile fClassFile = null;
/**
 * Creates a new <code>ClassFileInfo</code> for <code>classFile</code>.
 */
ClassFileInfo(IClassFile classFile) {
	fClassFile = classFile;
}
/**
 * Creates the handles and infos for the fields of the given binary type.
 * Adds new handles to the given vector.
 */
private void generateFieldInfos(IType type, IBinaryType typeInfo, HashMap newElements, ArrayList children) {
	// Make the fields
	IBinaryField[] fields = typeInfo.getFields();
	if (fields == null) {
		return;
	}
	for (int i = 0, fieldCount = fields.length; i < fieldCount; i++) {
		IBinaryField fieldInfo = fields[i];
		IField field = new BinaryField(type, new String(fieldInfo.getName()));
		newElements.put(field, fieldInfo);
		children.add(field);
	}
}
/**
 * Creates the handles and infos for the inner types of the given binary type.
 * Adds new handles to the given vector.
 */
private void generateInnerClassInfos(IType type, IBinaryType typeInfo, HashMap newElements, ArrayList children) {
	// Add inner types
	// If the current type is an inner type, innerClasses returns
	// an extra entry for the current type.  This entry must be removed.
	// Can also return an entry for the enclosing type of an inner type.
	IBinaryNestedType[] innerTypes = typeInfo.getMemberTypes();
	if (innerTypes != null) {
		for (int i = 0, typeCount = innerTypes.length; i < typeCount; i++) {
			IBinaryNestedType binaryType = innerTypes[i];
			IClassFile classFile= ((IPackageFragment)fClassFile.getParent()).getClassFile(new String(ClassFile.unqualifiedName(binaryType.getName())) + SUFFIX_STRING_class);
			IType innerType = new BinaryType(classFile, new String(ClassFile.simpleName(binaryType.getName())));
			children.add(innerType);
		}
	}
}
/**
 * Creates the handles and infos for the methods of the given binary type.
 * Adds new handles to the given vector.
 */
private void generateMethodInfos(IType type, IBinaryType typeInfo, HashMap newElements, ArrayList children) {
	IBinaryMethod[] methods = typeInfo.getMethods();
	if (methods == null) {
		return;
	}
	for (int i = 0, methodCount = methods.length; i < methodCount; i++) {
		IBinaryMethod methodInfo = methods[i];
		String[] pNames= Signature.getParameterTypes(new String(methodInfo.getMethodDescriptor()));
		char[][] paramNames= new char[pNames.length][];
		for (int j= 0; j < pNames.length; j++) {
			paramNames[j]= pNames[j].toCharArray();
		}
		char[][] parameterTypes = ClassFile.translatedNames(paramNames);
		String selector = new String(methodInfo.getSelector());
		if (methodInfo.isConstructor()) {
			selector = type.getElementName();
		}
		for (int j= 0; j < pNames.length; j++) {
			pNames[j]= new String(parameterTypes[j]);
		}
		IMethod method = new BinaryMethod(type, selector, pNames);
		children.add(method);
		newElements.put(method, methodInfo);
	}
}
/**
 * Returns the list of children (<code>BinaryMember</code>s) of the
 * <code>BinaryType</code> of our <code>ClassFile</code>.
 */
IJavaElement[] getBinaryChildren() throws JavaModelException {
	if (fBinaryChildren == null) {
		readBinaryChildren();
	}
	return fBinaryChildren;
}
/**
 * Returns true iff the <code>readBinaryChildren</code> has already
 * been called.
 */
boolean hasReadBinaryChildren() {
	return fBinaryChildren != null;
}
/**
 * Creates the handles for <code>BinaryMember</code>s defined in this
 * <code>ClassFile</code> and adds them to the
 * <code>JavaModelManager</code>'s cache.
 */
private void readBinaryChildren() {
	ArrayList children = new ArrayList();
	HashMap newElements = new HashMap();
	BinaryType type = null;
	IBinaryType typeInfo = null;
	JavaModelManager manager = (JavaModelManager) JavaModelManager.getJavaModelManager();
	try {
		type = (BinaryType) fClassFile.getType();
		typeInfo = (IBinaryType) manager.getInfo(type);
	} catch (JavaModelException npe) {
		return;
	}
	if (typeInfo != null) { //may not be a valid class file
		generateFieldInfos(type, typeInfo, newElements, children);
		generateMethodInfos(type, typeInfo, newElements, children);
		generateInnerClassInfos(type, typeInfo, newElements, children);
	}
	
	for (Iterator iter = newElements.entrySet().iterator(); iter.hasNext();) {
		Map.Entry entry = (Map.Entry) iter.next();
		manager.putInfo(
			(IJavaElement) entry.getKey(), 
			entry.getValue());
	}
	fBinaryChildren = new IJavaElement[children.size()];
	children.toArray(fBinaryChildren);
}
/**
 * Removes the binary children handles and remove their infos from
 * the <code>JavaModelManager</code>'s cache.
 */
void removeBinaryChildren() {
	if (fBinaryChildren != null) {
		JavaModelManager manager = (JavaModelManager) JavaModelManager.getJavaModelManager();
		for (int i = 0; i <fBinaryChildren.length; i++) {
			manager.removeInfo(fBinaryChildren[i]);
		}
		fBinaryChildren = fgEmptyChildren;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4161.java