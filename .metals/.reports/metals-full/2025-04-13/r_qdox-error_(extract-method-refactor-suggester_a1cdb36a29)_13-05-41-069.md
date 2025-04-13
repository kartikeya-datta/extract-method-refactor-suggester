error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5658.java
text:
```scala
n@@ew HierarchyResolver(this.searchableEnvironment, JavaCore.getOptions(), this, new DefaultProblemFactory());

package org.eclipse.jdt.internal.core.hierarchy;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import org.eclipse.jdt.internal.compiler.HierarchyResolver;
import org.eclipse.jdt.internal.compiler.IHierarchyRequestor;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.IGenericType;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

public abstract class HierarchyBuilder implements IHierarchyRequestor {
	/**
	 * The hierarchy being built.
	 */
	protected TypeHierarchy hierarchy;
	
	/**
	 * The name environment used by the HierarchyResolver
	 */
	protected SearchableEnvironment searchableEnvironment;

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
	 * for the types in the region (i.e. no supertypes outside
	 * the region).
	 */
	protected Hashtable infoToHandle;
public HierarchyBuilder(TypeHierarchy hierarchy) throws JavaModelException {
	this.hierarchy = hierarchy;
	JavaProject project = (JavaProject)hierarchy.javaProject();
	this.searchableEnvironment = (SearchableEnvironment)project.getSearchableNameEnvironment();
	this.nameLookup = project.getNameLookup();
	this.hierarchyResolver = 
		new HierarchyResolver(this.searchableEnvironment, this, new DefaultProblemFactory());
	this.infoToHandle = new Hashtable(5);
}
public abstract void build(boolean computeSubtypes) throws JavaModelException, CoreException;
/**
 * Configure this type hierarchy by computing the supertypes only.
 */
protected void buildSupertypes() {
	IType focusType = this.getType();
	if (focusType == null) return;
	
	// get generic type from focus type
	IGenericType type;
	try {
		type = (IGenericType)((JavaElement) focusType).getRawInfo();
	} catch (JavaModelException e) {
		// if the focus type is not present, or if cannot get workbench path
		// we cannot create the hierarchy
		return;
	}

	// resolve
	this.searchableEnvironment.unitToLookInside = (CompilationUnit)focusType.getCompilationUnit();
	this.hierarchyResolver.resolve(type);
	this.searchableEnvironment.unitToLookInside = null;

	// Add focus if not already in (case of a type with no explicit super type)
	if (!this.hierarchy.contains(focusType)) {
		this.hierarchy.addRootClass(focusType);
	}
}
/**
 * @see IHierarchyRequestor
 */
public void connect(IGenericType suppliedType, IGenericType superclass, IGenericType[] superinterfaces) {

	this.worked(1);
	
	// convert all infos to handles
	IType typeHandle= getHandle(suppliedType);

	/*
	 * Temporary workaround for 1G2O5WK: ITPJCORE:WINNT - NullPointerException when selecting "Show in Type Hierarchy" for a inner class
	 */
	if (typeHandle == null) return;
	
	IType superHandle= null;
	if (superclass != null) {
		superHandle= getHandle(superclass);
	}
	IType[] interfaceHandles= null;
	if (superinterfaces != null && superinterfaces.length > 0) {
		int length = superinterfaces.length;
		IType[] resolvedInterfaceHandles= new IType[length];
		int index = 0;
		for (int i= 0; i < length; i++) {
			if (superinterfaces[i] != null) {
				resolvedInterfaceHandles[index++]= getHandle(superinterfaces[i]);
			}
		}
		// resize
		System.arraycopy(resolvedInterfaceHandles, 0, interfaceHandles = new IType[index], 0, index);
	}

	// now do the caching
	if (suppliedType.isClass()) {
		if (superHandle == null) {
			this.hierarchy.addRootClass(typeHandle);
		} else {
			this.hierarchy.cacheSuperclass(typeHandle, superHandle);
		}
	} else {
		this.hierarchy.addInterface(typeHandle);
	}

	if (interfaceHandles == null) {
		interfaceHandles= this.hierarchy.fgEmpty;
	}
	this.hierarchy.cacheSuperInterfaces(typeHandle, interfaceHandles);
}
/**
 * Returns a handle for the given generic type or null if not found.
 */
protected IType getHandle(IGenericType genericType) {
	if (genericType == null) return null;
	if (genericType.isBinaryType()) {
		IClassFile classFile = (IClassFile)this.infoToHandle.get(genericType);
		// if it's null, it's from outside the region, so do lookup
		if (classFile == null) {
			IType handle = lookupBinaryHandle((IBinaryType)genericType);
			if (handle == null) return null; // case of an anonymous type (see 1G2O5WK: ITPJCORE:WINNT - NullPointerException when selecting "Show in Type Hierarchy" for a inner class)
			
			// optimization: remember the handle for next call (case of java.io.Serializable that a lot of classes implement)
			this.infoToHandle.put(genericType, handle.getParent());
			return handle;
		} else {
			try {
				return classFile.getType();
			} catch (JavaModelException e) {
				return null;
			}
		}
	} else if (genericType instanceof SourceTypeElementInfo) {
		return ((SourceTypeElementInfo)genericType).getHandle();
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
	if (typeInfo.isClass()) {
		flag = this.nameLookup.ACCEPT_CLASSES;
	} else {
		flag = this.nameLookup.ACCEPT_INTERFACES;
	}
	char[] bName = typeInfo.getName();
	qualifiedName = new String(ClassFile.translatedName(bName));
	return this.nameLookup.findType(qualifiedName, false, flag);
}
protected void worked(int work) {
	IProgressMonitor progressMonitor = this.hierarchy.fProgressMonitor;
	if (progressMonitor != null) {
		if (progressMonitor.isCanceled()) {
			throw new OperationCanceledException();
		} else {
			progressMonitor.worked(work);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5658.java