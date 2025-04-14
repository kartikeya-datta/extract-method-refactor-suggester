error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/95.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/95.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/95.java
text:
```scala
p@@kg = new String(parameterPackageNames[i]) + "."/*nonNLS*/;

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.*;
import org.eclipse.jdt.internal.compiler.IProblem;
import org.eclipse.jdt.internal.codeassist.ISelectionRequestor;
import org.eclipse.jdt.internal.codeassist.SelectionEngine;
import org.eclipse.jdt.core.*;

/**
 * Implementation of <code>ISelectionRequestor</code> to assist with
 * code resolve in a compilation unit. Translates names to elements.
 */
public class SelectionRequestor implements ISelectionRequestor {
	/**
	 * The name lookup facility used to resolve packages
	 */
	protected INameLookup fNameLookup= null;

	/**
	 * Fix for 1FVXGDK
	 *
	 * The compilation unit we are resolving in
	 */
	protected IJavaElement fCodeResolve;

	/**
	 * The collection of resolved elements.
	 */
	protected IJavaElement[] fElements= fgEmptyElements;

	/**
	 * Empty collection used for efficiency.
	 */
	protected static IJavaElement[] fgEmptyElements = new IJavaElement[]{};
/**
 * Creates a selection requestor that uses that given
 * name lookup facility to resolve names.
 *
 * Fix for 1FVXGDK
 */
public SelectionRequestor(INameLookup nameLookup, IJavaElement codeResolve) {
	super();
	fNameLookup = nameLookup;
	fCodeResolve = codeResolve;
}
/**
 * Resolve the binary method
 *
 * fix for 1FWFT6Q
 */
protected void acceptBinaryMethod(IType type, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames) {
	String[] parameterTypes= null;
	if (parameterTypeNames != null) {
		parameterTypes= new String[parameterTypeNames.length];
		for (int i= 0, max = parameterTypeNames.length; i < max; i++) {
			String pkg = IPackageFragment.DEFAULT_PACKAGE_NAME;
			if (parameterPackageNames[i] != null && parameterPackageNames[i].length > 0) {
				pkg = new String(parameterPackageNames[i]) + "."; //$NON-NLS-1$
			}
			parameterTypes[i]= Signature.createTypeSignature(
				pkg + new String(parameterTypeNames[i]), true);
		}
	}
	IMethod method= type.getMethod(new String(selector), parameterTypes);
	if (method.exists()) {
		fElements = growAndAddToArray(fElements, method);
	}
}
/**
 * Resolve the class.
 */
public void acceptClass(char[] packageName, char[] className, boolean needQualification) {
	acceptType(packageName, className, INameLookup.ACCEPT_CLASSES, needQualification);
}
/**
 * Do nothing.
 */
public void acceptError(IProblem error) {}
/**
 * Resolve the field.
 */
public void acceptField(char[] declaringTypePackageName, char[] declaringTypeName, char[] name) {
	IType type= resolveType(declaringTypePackageName, declaringTypeName,
		INameLookup.ACCEPT_CLASSES | INameLookup.ACCEPT_INTERFACES);
	if (type != null) {
		IField field= type.getField(new String(name));
		if (field.exists()) {
			fElements= growAndAddToArray(fElements, field);
		}
	}
}
/**
 * Resolve the interface
 */
public void acceptInterface(char[] packageName, char[] interfaceName, boolean needQualification) {
	acceptType(packageName, interfaceName, INameLookup.ACCEPT_INTERFACES, needQualification);
}
/**
 * Resolve the method
 */
public void acceptMethod(char[] declaringTypePackageName, char[] declaringTypeName, char[] selector, char[][] parameterPackageNames, char[][] parameterTypeNames) {
	IType type= resolveType(declaringTypePackageName, declaringTypeName,
		INameLookup.ACCEPT_CLASSES | INameLookup.ACCEPT_INTERFACES);
	// fix for 1FWFT6Q
	if (type != null) {
		if (type.isBinary()) {
			acceptBinaryMethod(type, selector, parameterPackageNames, parameterTypeNames);
		} else {
			acceptSourceMethod(type, selector, parameterPackageNames, parameterTypeNames);
		}
	}
}
/**
 * Resolve the package
 */
public void acceptPackage(char[] packageName) {
	IPackageFragment[] pkgs = fNameLookup.findPackageFragments(new String(packageName), false);
	if (pkgs != null) {
		for (int i = 0, length = pkgs.length; i < length; i++) {
			fElements = growAndAddToArray(fElements, pkgs[i]);	
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
	} catch (JavaModelException e) {
		return;
	}
	IJavaElement[] matches = new IJavaElement[] {};
	for (int i = 0; i < methods.length; i++) {
		if (methods[i].getElementName().equals(name) && methods[i].getParameterTypes().length == parameterTypeNames.length) {
			matches = growAndAddToArray(matches, methods[i]);
		}
	}

	// if no matches, nothing to report
	if (matches.length == 0) {
		return;
	}

	// if there is only one match, we've got it
	if (matches.length == 1) {
		fElements = growAndAddToArray(fElements, matches[0]);
		return;
	}

	// more than one match - must match simple parameter types
	for (int i = 0; i < matches.length; i++) {
		IMethod method= (IMethod)matches[i];
		String[] signatures = method.getParameterTypes();
		boolean match= true;
		for (int p = 0; p < signatures.length; p++) {
			String simpleName= Signature.getSimpleName(Signature.toString(signatures[p]));
			if (!simpleName.equals(new String(parameterTypeNames[p]))) {
				match = false;
				break;
			}
		}
		if (match) {
			fElements = growAndAddToArray(fElements, method);
		}
	}
	
}
/**
 * Resolve the type, adding to the resolved elements.
 */
protected void acceptType(char[] packageName, char[] typeName, int acceptFlags, boolean needQualification) {
	IType type= resolveType(packageName, typeName, acceptFlags);
	if (type != null) {
		fElements= growAndAddToArray(fElements, type);
	}
	
}
/**
 * Returns the resolved elements.
 */
public IJavaElement[] getElements() {
	return fElements;
}
/**
 * Adds the new element to a new array that contains all of the elements of the old array.
 * Returns the new array.
 */
protected IJavaElement[] growAndAddToArray(IJavaElement[] array, IJavaElement addition) {
	IJavaElement[] old = array;
	array = new IJavaElement[old.length + 1];
	System.arraycopy(old, 0, array, 0, old.length);
	array[old.length] = addition;
	return array;
}
/**
 * Resolve the type
 */
protected IType resolveType(char[] packageName, char[] typeName, int acceptFlags) {
	//fix for 1FVXGDK
	IType type= null;
	if (packageName == null || packageName.length == 0) {
		// default package
		type= fNameLookup.findType(new String(typeName), false, acceptFlags);
	} else {
		IPackageFragment[] pkgs = fNameLookup.findPackageFragments(new String(packageName), false);
		if (pkgs != null) {
			for (int i = 0, length = pkgs.length; i < length; i++) {
				type= fNameLookup.findType(new String(typeName), pkgs[i], false, acceptFlags);
				if (type != null) break;	
			}
		}
	}
	if (type == null) {
		String pName= IPackageFragment.DEFAULT_PACKAGE_NAME;
		if (packageName != null) {
			pName = new String(packageName);
		}
		if (fCodeResolve != null && fCodeResolve.getParent().getElementName().equals(pName)) {
			// look inside the type in which we are resolving in
			String tName= new String(typeName);
			tName = tName.replace('.','$');
			IType[] allTypes= null;
			try {
				java.util.Vector v = ((JavaElement)fCodeResolve).getChildrenOfType(IJavaElement.TYPE);
				allTypes = new IType[v.size()];
				v.copyInto(allTypes);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/95.java