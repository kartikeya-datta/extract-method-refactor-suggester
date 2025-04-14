error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1226.java
text:
```scala
t@@hrow new NotPresentException(Util.bind("element.notPresent")); //$NON-NLS-1$

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.Util;
import org.eclipse.jdt.internal.core.builder.IHandle;
import org.eclipse.jdt.internal.core.builder.IImageContext;
import org.eclipse.jdt.internal.core.builder.IPackage;
import org.eclipse.jdt.internal.core.builder.ISourceFragment;
import org.eclipse.jdt.internal.core.builder.IType;
import org.eclipse.jdt.internal.core.builder.NotPresentException;

public class PackageImplSWH extends StateSpecificHandleImpl implements IPackage {
	PackageImpl fHandle;
	/**
	 * Internal - Create a new Package
	 */
	PackageImplSWH(StateImpl state, PackageImpl handle) throws NotPresentException {
		if (state == null) throw new NotPresentException();
		fState = state;
		fHandle = handle;
	}
	/**
	 * Returns an array containing Type objects representing all
	 * classes and interfaces in the package represented by this object.
	 * This includes public and default (package) access top-level 
	 * classes, inner classes, and local inner classes.
	 * Returns an array of length 0 if this package has no
	 * classes or interfaces.
	 * The Types are in no particular order.
	 * This is a slow method.  getDeclaredClasses() should be used for most cases.
	 */
	public IType[] getAllClasses() throws NotPresentException {

		TypeStructureEntry[] entries = fState.getAllTypesForPackage(fHandle);
		if (entries == null) {
			throw new NotPresentException();
		}
		IType[] results = new IType[entries.length];
		for (int i = 0, num = entries.length; i < num; ++i) {
			results[i] = (IType)entries[i].getType().inState(fState);
		}
		return results;
	}
/**
 * getClassHandle method comment.
 * Returns a handle representing the class or interface
 * with the given name.  The name is the VM class name,
 * not including the package name.
 * For inner classes, the name is as described in the 
 * <em>Inner Classes Specification</em>.
 * This is a handle-only method; the specified class 
 * may or may not actually be present in the image.
 */
public IType getClassHandle(String name) {
	return (IType)fHandle.getClassHandle(name).inState(fState);
}
/**
 * Returns an array of Type objects representing all the classes
 * and interfaces declared as members of the package represented by
 * this object. This includes public and default (package) access
 * classes and interfaces declared as members of the package. 
 * This does not include inner classes and interfaces.
 * Returns an array of length 0 if this package declares no classes
 * or interfaces as members.
 * The Types are in no particular order.
 */
public IType[] getDeclaredClasses() throws NotPresentException {
	TypeStructureEntry[] entries = fState.getAllTypesForPackage(fHandle);
	if (entries == null) {
		throw new NotPresentException();
	}
	int num = entries.length;
	IType[] results = new IType[num];
	int count = 0;
	for (int i = 0; i < num; ++i) {
		if (BinaryStructure.isPackageMember(fState.getBinaryType(entries[i]))) {
			results[count++] = (IType) entries[i].getType().inState(fState);
		}
	}
	if (count < num) {
		System.arraycopy(results, 0, results = new IType[count], 0, count);
	}
	return results;
}
/**
 * 	Returns the fully-qualified name of the package represented 
 * 	by this object, as a String. 
 * 	If the package is unnamed, returns the internal identifier
 * 	string of this unnamed packaged.
 * 	This is a handle-only method.
 */
public String getName() {
	return fHandle.getName();
}
/**
 * Returns an array of Package objects representing all other
 * packages which this package directly references.
 * This is the union of all packages directly referenced by all 
 * classes and interfaces in this package, including packages
 * mentioned in import declarations.
 * <p>
 * A direct reference in source code is a use of a package's
 * name other than as a prefix of another package name.
 * For example, 'java.lang.Object' contains a direct reference
 * to the package 'java.lang', but not to the package 'java'.
 * Also note that every package that declares at least one type
 * contains a direct reference to java.lang in virtue of the
 * automatic import of java.lang.*.
 * The result does not include this package (so contrary to the note
 * above, the result for package java.lang does not include java.lang).
 * In other words, the result is non-reflexive and typically
 * non-transitive.
 * <p>
 * The resulting packages may or may not be present in the image,
 * since the classes and interfaces in this package may refer to missing
 * packages.
 * The resulting packages are in no particular order.
 */
public IPackage[] getReferencedPackages() throws NotPresentException {
	if (!isPresent())
		throw new NotPresentException();
	IPackage[] pkgs = fState.getReferencedPackages((IPackage)nonStateSpecific());

	/* wrapped returned packages in state handles */
	for (int i = 0; i < pkgs.length; i++) {
		pkgs[i] = (IPackage) pkgs[i].inState(fState);
	}
	return pkgs;
		
}
	/**
	 * Returns an array of Package objects representing all packages
	 * in the given image context which directly reference this package.
	 * The result does not include this package.
	 * In other words, the result is non-transitive and non-reflexive.
	 * <p>
	 * The intersection of all packages in the image and those in the
	 * image context are considered, so the resulting packages are 
	 * guaranteed to be present in the image.
	 * The resulting packages are in no particular order.
	 */
	public IPackage[] getReferencingPackages(IImageContext context) 
	  throws NotPresentException {
		if (!isPresent())
			throw new NotPresentException(Util.bind("element.notPresent"/*nonNLS*/));

		IPackage[] pkgs = fState.getReferencingPackages(fHandle, context);

		/* wrap packages in state */
		for (int i = 0; i < pkgs.length; i++) {
			pkgs[i] = (IPackage)pkgs[i].inState(fState);
		}
		return pkgs;
	}
/**
 * Returns an array of SourceFragments describing the source package 
 * fragments from which this built package is derived.
 * Returns an empty array if this package is not derived directly from source
 * The source coordinates in the results are set to #(-1, -1).
 *
 * If this is a default package, we must resolve the project name from the
 * internal identifier
 */
public ISourceFragment[] getSourceFragments() throws NotPresentException {

	IPath[] paths = fState.getPackageMap().getFragments(fHandle);
	if (paths == null) {
		throw new NotPresentException();
	}
	int max = paths.length;
	ISourceFragment[] frags = new ISourceFragment[max];
	for (int i = 0; i < max; i++) {
		frags[i] = new SourceFragmentImpl(-1, -1, paths[i]);
	}
	return frags;
}
/**
 * isPresent method comment.
 */
public boolean isPresent() {
	return fState.getPackageMap().containsPackage(fHandle);
}
/**
 * Returns true if this package is an unnamed package, false 
 * otherwise.  See <em>The Java Language Specification</em>, 
 * sections 7.4.1 and 7.4.2, for details.
 * This is a handle-only method.
 */
public boolean isUnnamed() {
	return fHandle.isUnnamed();
}
	/**
	  * Returns the non state specific handle
	  */
	 public IHandle nonStateSpecific() {
		 return fHandle;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1226.java