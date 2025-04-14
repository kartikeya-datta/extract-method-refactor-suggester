error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3596.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3596.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3596.java
text:
```scala
p@@arent.getClassFile(simpleTypeName + ".class"); //$NON-NLS-1$

package org.eclipse.jdt.internal.core.search.matching;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchResultCollector;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.util.CharOperation;
import org.eclipse.jdt.internal.core.*;

import java.io.*;
import java.util.zip.ZipFile;

public class PotentialMatch {
	private static char[] EMPTY_FILE_NAME = new char[0];
	private MatchLocator locator;
	public IResource resource;
	public Openable openable;
	private CompilationUnitDeclaration parsedUnit;
	private MatchSet matchSet;
public PotentialMatch(MatchLocator locator, IResource resource, Openable openable) {
	this.locator = locator;
	this.resource = resource;
	this.openable = openable;
	if (openable instanceof CompilationUnit) {
		this.buildTypeBindings(this.getSource());
	} else if (openable instanceof org.eclipse.jdt.internal.core.ClassFile) {
		try {
			String source = ((org.eclipse.jdt.internal.core.ClassFile)openable).getSource();
			if (source != null) {
				this.buildTypeBindings(source.toCharArray());
				
				// try to use the main type's class file as the openable
				TypeDeclaration[] types = this.parsedUnit.types;
				if (types != null && types.length > 0) {
					String simpleTypeName = new String(types[0].name);
					IPackageFragment parent = (IPackageFragment)openable.getParent();
					org.eclipse.jdt.core.IClassFile classFile = 
						parent.getClassFile(simpleTypeName + ".class");
					if (classFile.exists()) {
						this.openable = (Openable)classFile;
					} 
				}
			}
		} catch (JavaModelException e) {
		}
	}
}
private void buildTypeBindings(final char[] source) {
	// get qualified name
	char[] qualifiedName;
	if (this.openable instanceof CompilationUnit) {
		// get file name
		String fileName = this.resource.getFullPath().lastSegment();
		// get main type name
		char[] mainTypeName = fileName.substring(0, fileName.length()-5).toCharArray(); 
		CompilationUnit cu = (CompilationUnit)this.openable;
		qualifiedName = cu.getType(new String(mainTypeName)).getFullyQualifiedName().toCharArray();
	} else {
		org.eclipse.jdt.internal.core.ClassFile classFile = (org.eclipse.jdt.internal.core.ClassFile)this.openable;
		try {
			qualifiedName = classFile.getType().getFullyQualifiedName().toCharArray();
		} catch (JavaModelException e) {
			return; // nothing we can do here
		}
	}

	// create match set	
	this.matchSet = new MatchSet(this.locator);
	this.locator.parser.matchSet = this.matchSet;

	this.parsedUnit = (CompilationUnitDeclaration)this.locator.parsedUnits.get(qualifiedName);
	if (this.parsedUnit == null) {
		// source unit
		ICompilationUnit sourceUnit = new ICompilationUnit() {
			public char[] getContents() {
				return source;
			}
			public char[] getFileName() {
				return EMPTY_FILE_NAME; // not used
			}
			public char[] getMainTypeName() {
				return null; // don't need to check if main type name == compilation unit name
			}
		};
		
		// diet parse
		CompilationResult compilationResult = new CompilationResult(sourceUnit, 0, 0);  
		this.parsedUnit = this.locator.parser.dietParse(sourceUnit, compilationResult);

		// initial type binding creation
		this.locator.lookupEnvironment.buildTypeBindings(this.parsedUnit);
	} else {
		// free memory
		this.locator.parsedUnits.put(qualifiedName, null);
	}
}
public char[] getSource() {
	try {
		if (this.openable instanceof CompilationUnit) {
			return Util.getResourceContentsAsCharArray((IFile)this.resource);
		} else if (this.openable instanceof org.eclipse.jdt.internal.core.ClassFile) {
			org.eclipse.jdt.internal.core.ClassFile classFile = (org.eclipse.jdt.internal.core.ClassFile)this.openable;
			return classFile.getSource().toCharArray();
		} else {
			return null;
		}
	} catch (JavaModelException e) {
		return null;
	}
}
public void locateMatches() throws CoreException {
	if (this.openable instanceof CompilationUnit) {
		this.locateMatchesInCompilationUnit(this.getSource());
	} else if (this.openable instanceof org.eclipse.jdt.internal.core.ClassFile) {
		org.eclipse.jdt.internal.core.ClassFile classFile = (org.eclipse.jdt.internal.core.ClassFile)this.openable;
		String source = classFile.getSource();
		if (source != null) {
			this.locateMatchesInCompilationUnit(source.toCharArray());
		} else {
			this.locateMatchesInClassFile();
		}
	}
}
/**
 * Locate declaration in the current class file. This class file is always in a jar.
 */
private void locateMatchesInClassFile() throws CoreException, JavaModelException {
	org.eclipse.jdt.internal.core.ClassFile classFile = (org.eclipse.jdt.internal.core.ClassFile)this.openable;
	BinaryType binaryType = (BinaryType)classFile.getType();
	IBinaryType info;
	if (classFile.isOpen()) {
		// reuse the info from the java model cache
		info = (IBinaryType)binaryType.getRawInfo();
	} else {
		// create a temporary info
		try {
			IJavaElement pkg = classFile.getParent();
			PackageFragmentRoot root = (PackageFragmentRoot)pkg.getParent();
			if (root.isArchive()) {
				// class file in a jar
				String pkgPath = pkg.getElementName().replace('.', '/');
				String classFilePath = 
					(pkgPath.length() > 0) ?
						pkgPath + "/" + classFile.getElementName() : //$NON-NLS-1$
						classFile.getElementName();
				ZipFile zipFile = null;
				try {
					zipFile = ((JarPackageFragmentRoot)root).getJar();
					info = org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader.read(
						zipFile,
						classFilePath);
				} finally {
					if (zipFile != null) {
						try {
							zipFile.close();
						} catch (IOException e) {
							// ignore 
						}
					}
				}
			} else {
				// class file in a directory
				String osPath = this.resource.getFullPath().toOSString();
				info = org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader.read(osPath);
			}
		} catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException e) {
			e.printStackTrace();
			return;
		} catch (java.io.IOException e) {
			throw new JavaModelException(e, IJavaModelStatusConstants.IO_EXCEPTION);
		}
		
	}

	// check class definition
	if (this.locator.pattern.matchesBinary(info, null)) {
		this.locator.reportBinaryMatch(binaryType, info, IJavaSearchResultCollector.EXACT_MATCH);
	}

	boolean compilationAborted = false;
	if (this.locator.pattern.needsResolve) {
		// resolve
		BinaryTypeBinding binding = null;
		try {
			binding = this.locator.lookupEnvironment.cacheBinaryType(info);
			if (binding == null) { // it was already cached as a result of a previous query
				char[][] compoundName = CharOperation.splitOn('.', binaryType.getFullyQualifiedName().toCharArray());
				ReferenceBinding referenceBinding = this.locator.lookupEnvironment.getCachedType(compoundName);
				if (referenceBinding != null && (referenceBinding instanceof BinaryTypeBinding)) {
					// if the binding could be found and if it comes from a source type,
					binding = (BinaryTypeBinding)referenceBinding;
				}
			}

			// check methods
			if (binding != null) {
				MethodBinding[] methods = binding.methods();
				for (int i = 0; i < methods.length; i++) {
					MethodBinding method = methods[i];
					int level = this.locator.pattern.matchLevel(method);
					switch (level) {
						case SearchPattern.IMPOSSIBLE_MATCH:
						case SearchPattern.INACCURATE_MATCH:
							break;
						default:
							IMethod methodHandle = 
								binaryType.getMethod(
									new String(method.isConstructor() ? binding.compoundName[binding.compoundName.length-1] : method.selector),
									Signature.getParameterTypes(new String(method.signature()).replace('/', '.'))
								);
							this.locator.reportBinaryMatch(
								methodHandle, 
								info, 
								level == SearchPattern.ACCURATE_MATCH ? 
									IJavaSearchResultCollector.EXACT_MATCH : 
									IJavaSearchResultCollector.POTENTIAL_MATCH);
					}
				}
			}
		
			// check fields
			if (binding != null) {
				FieldBinding[] fields = binding.fields();
				for (int i = 0; i < fields.length; i++) {
					FieldBinding field = fields[i];
					int level = this.locator.pattern.matchLevel(field);
					switch (level) {
						case SearchPattern.IMPOSSIBLE_MATCH:
						case SearchPattern.INACCURATE_MATCH:
							break;
						default:
							IField fieldHandle = binaryType.getField(new String(field.name));
							this.locator.reportBinaryMatch(
								fieldHandle, 
								info, 
								level == SearchPattern.ACCURATE_MATCH ? 
									IJavaSearchResultCollector.EXACT_MATCH : 
									IJavaSearchResultCollector.POTENTIAL_MATCH);
					}
				}
			}
		} catch (AbortCompilation e) {
			binding = null;
		}

		// no need to check binary info if resolve was successful
		compilationAborted = binding == null;
		if (!compilationAborted) return;
	}

	// if compilation was aborted it is a problem with the class path: 
	// report as a potential match if binary info matches the pattern
	int accuracy = compilationAborted ? IJavaSearchResultCollector.POTENTIAL_MATCH : IJavaSearchResultCollector.EXACT_MATCH;
	
	// check methods
	IBinaryMethod[] methods = info.getMethods();
	int length = methods == null ? 0 : methods.length;
	for (int i = 0; i < length; i++) {
		IBinaryMethod method = methods[i];
		if (this.locator.pattern.matchesBinary(method, info)) {
			IMethod methodHandle = 
				binaryType.getMethod(
					new String(method.isConstructor() ? info.getName() : method.getSelector()),
					Signature.getParameterTypes(new String(method.getMethodDescriptor()).replace('/', '.'))
				);
			this.locator.reportBinaryMatch(methodHandle, info, accuracy);
		}
	}

	// check fields
	IBinaryField[] fields = info.getFields();
	length = fields == null ? 0 : fields.length;
	for (int i = 0; i < length; i++) {
		IBinaryField field = fields[i];
		if (this.locator.pattern.matchesBinary(field, info)) {
			IField fieldHandle = binaryType.getField(new String(field.getName()));
			this.locator.reportBinaryMatch(fieldHandle, info, accuracy);
		}
	}
}
private void locateMatchesInCompilationUnit(char[] source) throws CoreException {
	if (this.parsedUnit != null) {
		this.locator.parser.matchSet = this.matchSet;
		this.locator.parser.scanner.setSourceBuffer(source);
		this.locator.parser.parseBodies(this.parsedUnit);
		// report matches that don't need resolve
		this.matchSet.cuHasBeenResolved = false;
		this.matchSet.accuracy = IJavaSearchResultCollector.EXACT_MATCH;
		this.matchSet.reportMatching(parsedUnit);
		
		// resolve if needed
		if (this.matchSet.needsResolve()) {
			if (this.parsedUnit.types != null) {
				try {
					if (this.parsedUnit.scope != null) {
						this.parsedUnit.scope.faultInTypes();
						this.parsedUnit.resolve();
					}
					// report matches that needed resolve
					this.matchSet.cuHasBeenResolved = true;
					this.matchSet.accuracy = IJavaSearchResultCollector.EXACT_MATCH;
					this.matchSet.reportMatching(this.parsedUnit);
				} catch (AbortCompilation e) {
					// could not resolve (reasons include "could not find library class") -> ignore and report the unresolved nodes
					this.matchSet.cuHasBeenResolved = false;
					this.matchSet.accuracy = IJavaSearchResultCollector.POTENTIAL_MATCH;
					this.matchSet.reportMatching(this.parsedUnit);
				}
			}
		}
	}
}
/**
 * Free memory.
 */
public void reset() {
	this.parsedUnit = null;
	this.matchSet = null;
}
public String toString() {
	return this.openable.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3596.java