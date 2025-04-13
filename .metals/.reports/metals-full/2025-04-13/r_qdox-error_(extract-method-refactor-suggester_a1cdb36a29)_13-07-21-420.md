error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8139.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8139.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8139.java
text:
```scala
i@@f (this.locator.pattern.mustResolve) {

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
package org.eclipse.jdt.internal.core.search.matching;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.IJavaSearchResultCollector;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.*;

public class PotentialMatch implements ICompilationUnit, SuffixConstants {
	public static final String NO_SOURCE_FILE_NAME = "NO SOURCE FILE NAME"; //$NON-NLS-1$

	public static IType getTopLevelType(IType binaryType) {
		
		// ensure it is not a local or anoymous type (see bug 28752  J Search resports non-existent Java element)
		String typeName = binaryType.getElementName();
		int lastDollar = typeName.lastIndexOf('$');
		int length = typeName.length();
		if (lastDollar != -1 && lastDollar < length-1) {
			if (Character.isDigit(typeName.charAt(lastDollar+1))) {
				// local or anonymous type
				typeName = typeName.substring(0, lastDollar);
				IClassFile classFile = binaryType.getPackageFragment().getClassFile(typeName+SUFFIX_STRING_class);
				try {
					binaryType = classFile.getType();
				} catch (JavaModelException e) {
					// ignore as implementation of getType() cannot throw this exception
				}
			}
		}
		
		// ensure it is a top level type
		IType declaringType = binaryType.getDeclaringType();
		while (declaringType != null) {
			binaryType = declaringType;
			declaringType = binaryType.getDeclaringType();
		}
		return binaryType;
	}
	public char[][] compoundName;

	private MatchLocator locator;
	MatchingNodeSet matchingNodeSet;
	public Openable openable;
	public IResource resource;
	private String sourceFileName;

	public PotentialMatch(
			MatchLocator locator, 
			IResource resource, 
			Openable openable) {
		this.locator = locator;
		this.resource = resource;
		this.openable = openable;
		this.matchingNodeSet = new MatchingNodeSet(locator);
		char[] qualifiedName = getQualifiedName();
		if (qualifiedName != null) {
			this.compoundName = CharOperation.splitOn('.', qualifiedName);
		}
	}
	public boolean equals(Object obj) {
		if (this.compoundName == null) return super.equals(obj);
		if (!(obj instanceof PotentialMatch)) return false;
		return CharOperation.equals(this.compoundName, ((PotentialMatch)obj).compoundName);
	}

	/*
	 * Finds the source of this class file.
	 * Returns null if not found.
	 */
	private char[] findClassFileSource() {
		String fileName = getSourceFileName();
		if (fileName == NO_SOURCE_FILE_NAME) return null;
		char[] source = null; 
		try {
			SourceMapper sourceMapper = this.openable.getSourceMapper();
			if (sourceMapper != null) {
				IType type = ((ClassFile)this.openable).getType();
				source = sourceMapper.findSource(type, fileName);
			}
		} catch (JavaModelException e) {
		}
		return source;
	}
	public char[] getContents() {
		char[] source = null;
		try {
			if (this.openable instanceof CompilationUnit) {
				if (((CompilationUnit)this.openable).isWorkingCopy()) {
					IBuffer buffer = this.openable.getBuffer();
					if (buffer == null) return null;
					source = buffer.getCharacters();
				} else {
					source = Util.getResourceContentsAsCharArray((IFile)this.resource);
				}
			} else if (this.openable instanceof ClassFile) {
				source = findClassFileSource();
			}
		} catch (JavaModelException e) {
		}
		if (source == null) return CharOperation.NO_CHAR;
		return source;
	}

	public char[] getFileName() {
		return this.openable.getPath().toString().toCharArray();
	}

	public char[] getMainTypeName() {
		return null; // cannot know the main type name without opening .java or .class file
		                  // see http://bugs.eclipse.org/bugs/show_bug.cgi?id=32182
	}
	public char[][] getPackageName() {
		int length;
		if ((length = this.compoundName.length) > 1) {
			return CharOperation.subarray(this.compoundName, 0, length-1);
		} else {
			return CharOperation.NO_CHAR_CHAR;
		}
	}
	/*
	 * Returns the fully qualified name of the main type of the compilation unit
	 * or the main type of the .java file that defined the class file.
	 */
	private char[] getQualifiedName() {
		if (this.openable instanceof CompilationUnit) {
			// get file name
			String fileName = this.resource.getFullPath().lastSegment();
			// get main type name
			char[] mainTypeName = fileName.substring(0, fileName.length()-5).toCharArray(); 
			CompilationUnit cu = (CompilationUnit)this.openable;
			return cu.getType(new String(mainTypeName)).getFullyQualifiedName().toCharArray();
		} else if (this.openable instanceof ClassFile) {
			String fileName = getSourceFileName();
			if (fileName == NO_SOURCE_FILE_NAME) {
				try {
					return ((ClassFile)this.openable).getType().getFullyQualifiedName('.').toCharArray();
				} catch (JavaModelException e) {
					return null;
				}
			}
			String simpleName = fileName.substring(0, fileName.length()-5); // length-".java".length()
			String pkgName = this.openable.getParent().getElementName();
			if (pkgName.length() == 0) {
				return simpleName.toCharArray();
			} else {
				return (pkgName + '.' + simpleName).toCharArray();
			}
		} else {
			return null;
		}
	}
	/*
	 * Returns the source file name of the class file.
	 * Returns NO_SOURCE_FILE_NAME if not found.
	 */
	private String getSourceFileName() {
		if (this.sourceFileName != null) return this.sourceFileName;
		this.sourceFileName = NO_SOURCE_FILE_NAME; 
		try {
			SourceMapper sourceMapper = this.openable.getSourceMapper();
			if (sourceMapper != null) {
				IType type = ((ClassFile)this.openable).getType();
				ClassFileReader reader = this.locator.classFileReader(type);
				if (reader != null) {
					this.sourceFileName = sourceMapper.findSourceFileName(type, reader);
				}
			}
		} catch (JavaModelException e) {
		}
		return this.sourceFileName;
	}	
	public int hashCode() {
		if (this.compoundName == null) return super.hashCode();
		int hashCode = 0;
		for (int i = 0, length = this.compoundName.length; i < length; i++) {
			hashCode += CharOperation.hashCode(this.compoundName[i]);
		}
		return hashCode;
	}
	/**
	 * Locate declaration in the current class file. This class file is always in a jar.
	 */
	public void locateMatchesInClassFile() throws CoreException {
		org.eclipse.jdt.internal.core.ClassFile classFile = (org.eclipse.jdt.internal.core.ClassFile)this.openable;
		IBinaryType info = this.locator.getBinaryInfo(classFile, this.resource);
		if (info == null) 
			return; // unable to go further
	
		// check class definition
		BinaryType binaryType = (BinaryType)classFile.getType();
		if (this.locator.pattern.matchesBinary(info, null)) {
			this.locator.reportBinaryMatch(binaryType, info, IJavaSearchResultCollector.EXACT_MATCH);
		}
	
		boolean compilationAborted = false;
		if (this.locator.pattern.needsResolve) {
			// resolve
			BinaryTypeBinding binding = null;
			try {
				binding = this.locator.cacheBinaryType(binaryType);
				if (binding != null) {
					// filter out element not in hierarchy scope
					if (!this.locator.typeInHierarchy(binding)) {
						return;
					}
		
					// check methods
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
			
					// check fields
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
	public String toString() {
		return this.openable == null ? "Fake PotentialMatch" : this.openable.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8139.java