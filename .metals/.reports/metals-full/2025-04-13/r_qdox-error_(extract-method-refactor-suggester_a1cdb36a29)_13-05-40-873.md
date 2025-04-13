error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/450.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/450.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/450.java
text:
```scala
i@@f (extension.equals("java"/*nonNLS*/) || extension.equals("class"/*nonNLS*/)) {

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.internal.core.builder.*;

import java.util.Enumeration;
import java.util.Vector;

public class ImageImplSWH extends StateSpecificHandleImpl implements IImage {
	ImageImpl fHandle;
	/**
	 * Internal - Create a new Image
	 */
	ImageImplSWH(StateImpl state, ImageImpl handle) {
		fState = state;
		fHandle = handle;
	}
/**
 * booleanType method comment.
 */
public IType booleanType() {
	return fState.fBooleanType;
}
/**
 * Returns the Type representing the primitive type byte
 */
public IType byteType() {
	return fState.fByteType;
}
/**
 *Returns the Type representing the primitive type char
 */
public IType charType() {
	return fState.fCharType;
}
/**
 * Returns an Image Context consisting of all the given packages.
 * This object and the packages must all be non-state-specific.
 * This is a handle-only operation.  The packages need not 
 *	be present in the image.
 */
public IImageContext createImageContext(IPackage[] packages) 
	throws StateSpecificException {
		throw new StateSpecificException();
		
}
/**
 * Returns the Type representing the primitive type double
 */
public IType doubleType() {
	return fState.fDoubleType;
}
/**
 * Returns the Type representing the primitive type float
 */
public IType floatType() {
	return fState.fFloatType;
}
/**
 * Returns an array containing Type objects representing all
 * classes and interfaces in the given ImageContext.
 * This includes public and default (package) access top-level 
 * classes, inner classes, and local inner classes.
 * The result is the intersection of all classes present in this image
 * and the classes in the ImageContext, so the resulting classes
 * are all present in the image.
 * The resulting Types are in no particular order.
 */
public IType[] getAllClasses(IImageContext context) {
	StateImpl state = fState;
	IPackage[] pkgs = (context == null ? state.getPackageMap().getAllPackagesAsArray() : context.getPackages());
		
	java.util.Vector result = new java.util.Vector(pkgs.length * 25);
	for (int i = 0; i < pkgs.length; i++) {
		TypeStructureEntry[] entries = state.getAllTypesForPackage(pkgs[i]);
		// entries is null if package is missing
		if (entries != null) {
			for (int j = 0, len = entries.length; j < len; ++j) {
				result.addElement(entries[j].getType().inState(state));
			}
		}
	}
	// convert the Vector to an array
	IType[] types = new IType[result.size()];
	result.copyInto(types);
	return types;
}
	/**
	 * Returns an array of all packages present in the image.  Note that this
	 * method defies the concept of a potentially infinite image, and should only
	 * be used by clients that must operate over the entire image (search, code assist)
	 */
	public IPackage[] getAllPackages() {
		IPackage[] pkgs = fState.getPackageMap().getAllPackagesAsArray();
		int pkgCount = pkgs.length;
		IPackage[] swh = new IPackage[pkgCount];
		for (int i = 0; i < pkgCount; i++) {
			swh[i] = (IPackage) pkgs[i].inState(fState);
		}
		return swh;
	}
	/**
	 * @see IImage
	 */
	public IType[] getBuiltClasses(IPath path) {
		Vector vResult = new Vector();
//		try {
			String extension = path.getFileExtension().toLowerCase();
			if (extension.equals("java") || extension.equals("class")) { //$NON-NLS-1$ //$NON-NLS-2$
				IPath pkgPath = path.removeLastSegments(1);
				IPackage pkg = fState.getPathMap().packageHandleFromPath(pkgPath);
				TypeStructureEntry[] tsEntries = fState.getAllTypesForPackage(pkg);
				if (tsEntries != null) {  // present?
					for (int i = 0, len = tsEntries.length; i < len; ++i) {
						TypeStructureEntry tsEntry = tsEntries[i];
						if (path.equals(tsEntry.getSourceEntry().getPath())) {
							vResult.addElement(tsEntry.getType().inState(fState));
						}
					}
				}
			}
			else if (fState.isZipElement(path)) {
				IPackage[] pkgs = fState.getPathMap().packageHandlesFromPath(path);
				for (int i = 0; i < pkgs.length; ++i) {
					IPackage pkg = pkgs[i];
					TypeStructureEntry[] tsEntries = fState.getAllTypesForPackage(pkg);
					if (tsEntries != null) {  // present?
						for (int j = 0, len = tsEntries.length; j < len; ++j) {
							TypeStructureEntry tsEntry = tsEntries[j];
							if (path.equals(tsEntry.getSourceEntry().getPath())) {
								vResult.addElement(tsEntry.getType().inState(fState));
							}
						}
					}
				}
			}
			else {
				if (path.equals(fState.getProject().getFullPath())) {
					try {
						IResource[] members = fState.getProject().members();
						for (int i = 0, max = members.length; i < max; i++) {
							IType[] tempResult = getBuiltClasses(members[i].getFullPath());
							for (int j = 0; j < tempResult.length; ++j) {
								vResult.addElement(tempResult[j]);
							}
						}
					} catch (CoreException e) {
						//couldn't access the project -- ignore and return empty array
					}
				}
				else { // package
					IPackage pkg = fState.getPathMap().packageHandleFromPath(path);
					TypeStructureEntry[] tsEntries = fState.getAllTypesForPackage(pkg);
					if (tsEntries != null) {  // present?
						for (int i = 0, len = tsEntries.length; i < len; ++i) {
							TypeStructureEntry tsEntry = tsEntries[i];
							if (path.isPrefixOf(tsEntry.getSourceEntry().getPath())) {
								vResult.addElement(tsEntry.getType().inState(fState));
							}
						}
					}
				}
			}
//		}
//		catch (ResourceAccessException e) {
//			fState.resourceAccessException(e);
//		}
		IType[] result = new IType[vResult.size()];
		vResult.copyInto(result);
		return result;
	}
/**
	 * Returns a handle representing the package with the given 
	 * name.  For named packages, this is the fully qualified
	 * name.  For unnamed packages, it is some internal identifying
	 * string.
	 * See <em>The Java Language Specification</em> section 7.4.1 and
	 * 7.4.2 for more details.
	 * This is a handle-only method; the specified package 
	 * may or may not actually be present in the image.
	 *
	 * @parameter name the name of the package.
	 * @parameter isUnnamed a boolean indicating whether the package is unnamed.
	 * @see IPackage#getName
	 * @see IHandle
	 */
	public IPackage getPackageHandle(String name, boolean isUnnamed) {
		return (IPackage)fHandle.getPackageHandle(name, isUnnamed).inState(fState);
	}
/**
 * Returns an array of Package objects representing all
 * packages contained in the given ImageContext.
 * The result is the intersection of the packages present in this image
 * and the packages in the ImageContext, so the resulting packages
 * are all present in the image.
 * The resulting Packages are in no particular order.
 */
public IPackage[] getPackages(IImageContext context) {
	if (context == null) {
		return getAllPackages();
	}
	IPackage[] ctxPkgs = context.getPackages();
	Vector result = new Vector();
	for (int i = 0; i < ctxPkgs.length; i++) {
		IPackage pkgSWH = (IPackage) ctxPkgs[i].inState(fState);
		if (pkgSWH.isPresent())
			result.addElement(pkgSWH);
	}
 	// convert the Vector to an array
	IPackage[] pkgs = new IPackage[result.size()];
	result.copyInto(pkgs);
	return pkgs;
}
/**
 * Returns the Type representing the primitive type int
 */
public IType intType() {
	return fState.fIntType;
}
/**
 * Returns whether the image is present.
 */
public boolean isPresent() {
	/* the image is always present */
	return true;
}
/**
 * kind method comment.
 */
public int kind() {
	return K_JAVA_IMAGE;
}
/**
 * Returns the Type representing the primitive type long
 */
public IType longType() {
	return fState.fLongType;
}
	/**
	  * Returns the non state specific handle
	  */
	 public IHandle nonStateSpecific() {
		 return fHandle;
	 }
/**
 * Returns the Type representing the primitive type short
 */
public IType shortType() {
	return fState.fShortType;
}
/**
 * Returns the Type representing the primitive type void
 */
public IType voidType() {
	return fState.fVoidType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/450.java