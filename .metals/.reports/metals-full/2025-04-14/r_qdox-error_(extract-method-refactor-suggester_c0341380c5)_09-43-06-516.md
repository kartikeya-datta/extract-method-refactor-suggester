error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2406.java
text:
```scala
I@@Type focusType = getType();

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
package org.eclipse.jdt.internal.core.hierarchy;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.util.ResourceCompilationUnit;
import org.eclipse.jdt.internal.core.util.Util;

public abstract class HierarchyBuilder {
	/**
	 * The hierarchy being built.
	 */
	protected TypeHierarchy hierarchy;
	/**
	 * @see NameLookup
	 */
	protected NameLookup nameLookup;
	/**
	 * The resolver used to resolve type hierarchies
	 * @see HierarchyResolver
	 */
	protected HierarchyResolver hierarchyResolver;
	/**
	 * A temporary cache of infos to handles to speed info
	 * to handle translation - it only contains the entries
	 * for the types in the region (in other words, it contains
	 * no supertypes outside the region).
	 */
	protected Map infoToHandle;
	/*
	 * The dot-separated fully qualified name of the focus type, or null of none.
	 */
	protected String focusQualifiedName;
	
	public HierarchyBuilder(TypeHierarchy hierarchy) throws JavaModelException {
		
		this.hierarchy = hierarchy;
		JavaProject project = (JavaProject) hierarchy.javaProject();
		
		IType focusType = hierarchy.getType();
		org.eclipse.jdt.core.ICompilationUnit unitToLookInside = focusType == null ? null : focusType.getCompilationUnit();
		org.eclipse.jdt.core.ICompilationUnit[] workingCopies = this.hierarchy.workingCopies;
		org.eclipse.jdt.core.ICompilationUnit[] unitsToLookInside;
		if (unitToLookInside != null) {
			int wcLength = workingCopies == null ? 0 : workingCopies.length;
			if (wcLength == 0) {
				unitsToLookInside = new org.eclipse.jdt.core.ICompilationUnit[] {unitToLookInside};
			} else {
				unitsToLookInside = new org.eclipse.jdt.core.ICompilationUnit[wcLength+1];
				unitsToLookInside[0] = unitToLookInside;
				System.arraycopy(workingCopies, 0, unitsToLookInside, 1, wcLength);
			}
		} else {
			unitsToLookInside = workingCopies;
		}
		if (project != null) {
			SearchableEnvironment searchableEnvironment = project.newSearchableNameEnvironment(unitsToLookInside);
			this.nameLookup = searchableEnvironment.nameLookup;
			this.hierarchyResolver =
				new HierarchyResolver(
					searchableEnvironment,
					project.getOptions(true),
					this,
					new DefaultProblemFactory());
		}
		this.infoToHandle = new HashMap(5);
		this.focusQualifiedName = focusType == null ? null : focusType.getFullyQualifiedName();
	}
	
	public abstract void build(boolean computeSubtypes)
		throws JavaModelException, CoreException;
	/**
	 * Configure this type hierarchy by computing the supertypes only.
	 */
	protected void buildSupertypes() {
		IType focusType = this.getType();
		if (focusType == null)
			return;
		// get generic type from focus type
		IGenericType type;
		try {
			type = (IGenericType) ((JavaElement) focusType).getElementInfo();
		} catch (JavaModelException e) {
			// if the focus type is not present, or if cannot get workbench path
			// we cannot create the hierarchy
			return;
		}
		//NB: no need to set focus type on hierarchy resolver since no other type is injected
		//    in the hierarchy resolver, thus there is no need to check that a type is 
		//    a sub or super type of the focus type.
		this.hierarchyResolver.resolve(type);

		// Add focus if not already in (case of a type with no explicit super type)
		if (!this.hierarchy.contains(focusType)) {
			this.hierarchy.addRootClass(focusType);
		}
	}
	/**
	 * Connect the supplied type to its superclass & superinterfaces.
	 * The superclass & superinterfaces are the identical binary or source types as
	 * supplied by the name environment.
	 */
	public void connect(
		IGenericType type,
		IType typeHandle,
		IType superclassHandle,
		IType[] superinterfaceHandles) {

		/*
		 * Temporary workaround for 1G2O5WK: ITPJCORE:WINNT - NullPointerException when selecting "Show in Type Hierarchy" for a inner class
		 */
		if (typeHandle == null)
			return;
		if (TypeHierarchy.DEBUG) {
			System.out.println(
				"Connecting: " + ((JavaElement) typeHandle).toStringWithAncestors()); //$NON-NLS-1$
			System.out.println(
				"  to superclass: " //$NON-NLS-1$
					+ (superclassHandle == null
						? "<None>" //$NON-NLS-1$
						: ((JavaElement) superclassHandle).toStringWithAncestors()));
			System.out.print("  and superinterfaces:"); //$NON-NLS-1$
			if (superinterfaceHandles == null || superinterfaceHandles.length == 0) {
				System.out.println(" <None>"); //$NON-NLS-1$
			} else {
				System.out.println();
				for (int i = 0, length = superinterfaceHandles.length; i < length; i++) {
					if (superinterfaceHandles[i] == null) continue;
					System.out.println(
						"    " + ((JavaElement) superinterfaceHandles[i]).toStringWithAncestors()); //$NON-NLS-1$
				}
			}
		}
		// now do the caching
		switch (TypeDeclaration.kind(type.getModifiers())) {
			case TypeDeclaration.CLASS_DECL :
			case TypeDeclaration.ENUM_DECL :
				if (superclassHandle == null) {
					this.hierarchy.addRootClass(typeHandle);
				} else {
					this.hierarchy.cacheSuperclass(typeHandle, superclassHandle);
				}
				break;
			case TypeDeclaration.INTERFACE_DECL :
			case TypeDeclaration.ANNOTATION_TYPE_DECL :
				this.hierarchy.addInterface(typeHandle);
				break;
		}		
		if (superinterfaceHandles == null) {
			superinterfaceHandles = TypeHierarchy.NO_TYPE;
		}
		this.hierarchy.cacheSuperInterfaces(typeHandle, superinterfaceHandles);
		 
		// record flags
		this.hierarchy.cacheFlags(typeHandle, type.getModifiers());
	}
	/**
	 * Returns a handle for the given generic type or null if not found.
	 */
	protected IType getHandle(IGenericType genericType, ReferenceBinding binding) {
		if (genericType == null)
			return null;
		if (genericType instanceof HierarchyType) {
			IType handle = (IType)this.infoToHandle.get(genericType);
			if (handle == null) {
				handle = ((HierarchyType)genericType).typeHandle;
				handle = (IType) ((JavaElement) handle).resolved(binding);
				this.infoToHandle.put(genericType, handle);
			}
			return handle;
		} else if (genericType.isBinaryType()) {
			ClassFile classFile = (ClassFile) this.infoToHandle.get(genericType);
			// if it's null, it's from outside the region, so do lookup
			if (classFile == null) {
				IType handle = lookupBinaryHandle((IBinaryType) genericType);
				if (handle == null)
					return null;
				// case of an anonymous type (see 1G2O5WK: ITPJCORE:WINNT - NullPointerException when selecting "Show in Type Hierarchy" for a inner class)
				// optimization: remember the handle for next call (case of java.io.Serializable that a lot of classes implement)
				classFile = (ClassFile) handle.getParent();
				this.infoToHandle.put(genericType, classFile);
			} 
			return new ResolvedBinaryType(classFile, classFile.getTypeName(), new String(binding.computeUniqueKey()));
		} else if (genericType instanceof SourceTypeElementInfo) {
			IType handle = ((SourceTypeElementInfo) genericType).getHandle();
			return (IType) ((JavaElement) handle).resolved(binding);
		} else
			return null;
	}
	protected IType getType() {
		return this.hierarchy.getType();
	}
	/**
	 * Looks up and returns a handle for the given binary info.
	 */
	protected IType lookupBinaryHandle(IBinaryType typeInfo) {
		int flag;
		String qualifiedName;
		switch (TypeDeclaration.kind(typeInfo.getModifiers())) {
			case TypeDeclaration.CLASS_DECL :
				flag = NameLookup.ACCEPT_CLASSES;
				break;
			case TypeDeclaration.INTERFACE_DECL :
				flag = NameLookup.ACCEPT_INTERFACES;
				break;
			case TypeDeclaration.ENUM_DECL :
				flag = NameLookup.ACCEPT_ENUMS;
				break;
			default:
				//case IGenericType.ANNOTATION :
				flag = NameLookup.ACCEPT_ANNOTATIONS;
				break;
		}			
		char[] bName = typeInfo.getName();
		qualifiedName = new String(ClassFile.translatedName(bName));
		if (qualifiedName.equals(this.focusQualifiedName)) return getType();
		NameLookup.Answer answer = this.nameLookup.findType(qualifiedName,
			false,
			flag,
			true/* consider secondary types */,
			false/* do NOT wait for indexes */,
			false/*don't check restrictions*/,
			null);
		return answer == null || answer.type == null || !answer.type.isBinary() ? null : answer.type;
		
	}
	protected void worked(IProgressMonitor monitor, int work) {
		if (monitor != null) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			} else {
				monitor.worked(work);
			}
		}
	}
/**
 * Create an ICompilationUnit info from the given compilation unit on disk.
 */
protected ICompilationUnit createCompilationUnitFromPath(Openable handle, IFile file) {
	final char[] elementName = handle.getElementName().toCharArray();
	return new ResourceCompilationUnit(file, file.getLocationURI()) {
		public char[] getFileName() {
			return elementName;
		}
	};
}
	/**
 * Creates the type info from the given class file on disk and
 * adds it to the given list of infos.
 */
protected IBinaryType createInfoFromClassFile(Openable handle, IResource file) {
	IBinaryType info = null;
	try {
		info = Util.newClassFileReader(file);
	} catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	} catch (java.io.IOException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	} catch (CoreException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	}						
	this.infoToHandle.put(info, handle);
	return info;
}
	/**
 * Create a type info from the given class file in a jar and adds it to the given list of infos.
 */
protected IBinaryType createInfoFromClassFileInJar(Openable classFile) {
	PackageFragment pkg = (PackageFragment) classFile.getParent();
	String classFilePath = Util.concatWith(pkg.names, classFile.getElementName(), '/');
	IBinaryType info = null;
	java.util.zip.ZipFile zipFile = null;
	try {
		zipFile = ((JarPackageFragmentRoot)pkg.getParent()).getJar();
		info = org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader.read(
			zipFile,
			classFilePath);
	} catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	} catch (java.io.IOException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	} catch (CoreException e) {
		if (TypeHierarchy.DEBUG) {
			e.printStackTrace();
		}
		return null;
	} finally {
		JavaModelManager.getJavaModelManager().closeZipFile(zipFile);
	}
	this.infoToHandle.put(info, classFile);
	return info;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2406.java