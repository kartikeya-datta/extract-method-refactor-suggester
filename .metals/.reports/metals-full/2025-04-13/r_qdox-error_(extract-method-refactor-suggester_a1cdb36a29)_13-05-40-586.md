error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9416.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9416.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9416.java
text:
```scala
i@@f (this.hierarchy.type == null || computeSubtypes) {

package org.eclipse.jdt.internal.core.hierarchy;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jdt.internal.compiler.env.IGenericType;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;

public class RegionBasedHierarchyBuilder extends HierarchyBuilder {
	
	public RegionBasedHierarchyBuilder(TypeHierarchy hierarchy)
		throws JavaModelException {
			
		super(hierarchy);
	}
	
	public void build(boolean computeSubtypes) {
		
		if (this.hierarchy.fType == null || computeSubtypes) {
			ArrayList allTypesInRegion = determineTypesInRegion();
			this.hierarchy.initialize(allTypesInRegion.size());
			createTypeHierarchyBasedOnRegion(allTypesInRegion);
		} else {
			this.hierarchy.initialize(1);
			this.buildSupertypes();
		}
	}
	/**
	 * Configure this type hierarchy that is based on a region.
	 */
	private void createTypeHierarchyBasedOnRegion(ArrayList allTypesInRegion) {
		
		int size = allTypesInRegion.size();
		if (size != 0) {
			this.infoToHandle = new HashMap(size);
		}
		ArrayList temp = new ArrayList(size);
		types : for (int i = 0; i < size; i++) {
			try {
				IType type = (IType) allTypesInRegion.get(i);
				IGenericType info = (IGenericType) ((JavaElement) type).getRawInfo();
				temp.add(info);
				if (info.isBinaryType()) {
					this.infoToHandle.put(info, type.getParent());
				}
				worked(1);
			} catch (JavaModelException npe) {
				continue types;
			}
		}

		size = temp.size();
		if (size > 0) {
			IGenericType[] genericTypes = new IGenericType[size];
			temp.toArray(genericTypes);
			IType focusType = this.getType();
			if (focusType != null) {
				this.searchableEnvironment.unitToLookInside =
					(CompilationUnit) focusType.getCompilationUnit();
			}
			this.hierarchyResolver.resolve(genericTypes);
			if (focusType != null) {
				this.searchableEnvironment.unitToLookInside = null;
			}

		}
	}
	
	/**
	 * Returns all of the types defined in the region of this type hierarchy.
	 */
	private ArrayList determineTypesInRegion() {

		ArrayList types = new ArrayList();
		IJavaElement[] roots =
			((RegionBasedTypeHierarchy) this.hierarchy).fRegion.getElements();
		for (int i = 0; i < roots.length; i++) {
			try {
				IJavaElement root = roots[i];
				switch (root.getElementType()) {
					case IJavaElement.JAVA_PROJECT :
						injectAllTypesForJavaProject((IJavaProject) root, types);
						break;
					case IJavaElement.PACKAGE_FRAGMENT_ROOT :
						injectAllTypesForPackageFragmentRoot((IPackageFragmentRoot) root, types);
						break;
					case IJavaElement.PACKAGE_FRAGMENT :
						injectAllTypesForPackageFragment((IPackageFragment) root, types);
						break;
					case IJavaElement.CLASS_FILE :
						types.add(((IClassFile) root).getType());
						break;
					case IJavaElement.COMPILATION_UNIT :
						IType[] cuTypes = ((ICompilationUnit) root).getAllTypes();
						for (int j = 0; j < cuTypes.length; j++) {
							types.add(cuTypes[j]);
						}
						break;
					case IJavaElement.TYPE :
						types.add(root);
						break;
					default :
						break;
				}
			} catch (JavaModelException e) {
				// just continue
			}
		}
		return types;
	}
	
	/**
	 * Adds all of the types defined within this java project to the
	 * list.
	 */
	private void injectAllTypesForJavaProject(
		IJavaProject project,
		ArrayList types) {
		try {
			IPackageFragmentRoot[] devPathRoots =
				((JavaProject) project).getPackageFragmentRoots();
			if (devPathRoots == null) {
				return;
			}
			for (int j = 0; j < devPathRoots.length; j++) {
				IPackageFragmentRoot root = devPathRoots[j];
				injectAllTypesForPackageFragmentRoot(root, types);
			}
		} catch (JavaModelException e) {
		}
	}
	
	/**
	 * Adds all of the types defined within this package fragment to the
	 * list.
	 */
	private void injectAllTypesForPackageFragment(
		IPackageFragment packFrag,
		ArrayList types) {
			
		try {
			IPackageFragmentRoot root = (IPackageFragmentRoot) packFrag.getParent();
			int kind = root.getKind();
			if (kind != 0) {
				boolean isSourcePackageFragment = (kind == IPackageFragmentRoot.K_SOURCE);
				if (isSourcePackageFragment) {
					ICompilationUnit[] typeContainers = packFrag.getCompilationUnits();
					injectAllTypesForTypeContainers(typeContainers, types);
				} else {
					IClassFile[] typeContainers = packFrag.getClassFiles();
					injectAllTypesForTypeContainers(typeContainers, types);
				}
			}
		} catch (JavaModelException e) {
		}
	}
	
	/**
	 * Adds all of the types defined within this package fragment root to the
	 * list.
	 */
	private void injectAllTypesForPackageFragmentRoot(
		IPackageFragmentRoot root,
		ArrayList types) {
		try {
			IJavaElement[] packFrags = root.getChildren();
			for (int k = 0; k < packFrags.length; k++) {
				IPackageFragment packFrag = (IPackageFragment) packFrags[k];
				injectAllTypesForPackageFragment(packFrag, types);
			}
		} catch (JavaModelException e) {
			return;
		}
	}
	
	/**
	 * Adds all of the types defined within the type containers (IClassFile).
	 */
	private void injectAllTypesForTypeContainers(
		IClassFile[] containers,
		ArrayList types) {
			
		try {
			for (int i = 0; i < containers.length; i++) {
				IClassFile cf = containers[i];
				types.add(cf.getType());
				this.worked(1);
			}
		} catch (JavaModelException e) {
		}
	}
	
	/**
	 * Adds all of the types defined within the type containers (ICompilationUnit).
	 */
	private void injectAllTypesForTypeContainers(
		ICompilationUnit[] containers,
		ArrayList types) {
			
		try {
			for (int i = 0; i < containers.length; i++) {
				ICompilationUnit cu = containers[i];
				IType[] cuTypes = cu.getAllTypes();
				for (int j = 0; j < cuTypes.length; j++) {
					types.add(cuTypes[j]);
				}
				this.worked(1);
			}
		} catch (JavaModelException e) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9416.java