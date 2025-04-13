error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9529.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9529.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9529.java
text:
```scala
b@@uildSupertypes();

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.hierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.Openable;
import org.eclipse.jdt.internal.core.SearchableEnvironment;

public class RegionBasedHierarchyBuilder extends HierarchyBuilder {
	
	public RegionBasedHierarchyBuilder(TypeHierarchy hierarchy)
		throws JavaModelException {
			
		super(hierarchy);
	}
	
public void build(boolean computeSubtypes) {
		
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	try {
		// optimize access to zip files while building hierarchy
		manager.cacheZipFiles();
				
		if (this.hierarchy.focusType == null || computeSubtypes) {
			IProgressMonitor typeInRegionMonitor = 
				this.hierarchy.progressMonitor == null ? 
					null : 
					new SubProgressMonitor(this.hierarchy.progressMonitor, 30);
			HashMap allOpenablesInRegion = determineOpenablesInRegion(typeInRegionMonitor);
			this.hierarchy.initialize(allOpenablesInRegion.size());
			IProgressMonitor buildMonitor = 
				this.hierarchy.progressMonitor == null ? 
					null : 
					new SubProgressMonitor(this.hierarchy.progressMonitor, 70);
			createTypeHierarchyBasedOnRegion(allOpenablesInRegion, buildMonitor);
			((RegionBasedTypeHierarchy)this.hierarchy).pruneDeadBranches();
		} else {
			this.hierarchy.initialize(1);
			this.buildSupertypes();
		}
	} finally {
		manager.flushZipFiles();
	}
}
/**
 * Configure this type hierarchy that is based on a region.
 */
private void createTypeHierarchyBasedOnRegion(HashMap allOpenablesInRegion, IProgressMonitor monitor) {
	
	try {
		int size = allOpenablesInRegion.size();		
		if (monitor != null) monitor.beginTask("", size * 2/* 1 for build binding, 1 for connect hierarchy*/); //$NON-NLS-1$
		this.infoToHandle = new HashMap(size);
		Iterator javaProjects = allOpenablesInRegion.entrySet().iterator();
		while (javaProjects.hasNext()) {
			Map.Entry entry = (Map.Entry) javaProjects.next();  
			JavaProject project = (JavaProject) entry.getKey();
			ArrayList allOpenables = (ArrayList) entry.getValue();
			Openable[] openables = new Openable[allOpenables.size()];
			allOpenables.toArray(openables);
	
			try {
				// resolve
				SearchableEnvironment searchableEnvironment = project.newSearchableNameEnvironment(this.hierarchy.workingCopies);
				this.nameLookup = searchableEnvironment.nameLookup;
				this.hierarchyResolver.resolve(openables, null, monitor);
			} catch (JavaModelException e) {
				// project doesn't exit: ignore
			} 
		}
	} finally {
		if (monitor != null) monitor.done();
	}
}
	
	/**
	 * Returns all of the openables defined in the region of this type hierarchy.
	 * Returns a map from IJavaProject to ArrayList of Openable
	 */
	private HashMap determineOpenablesInRegion(IProgressMonitor monitor) {

		try {
			HashMap allOpenables = new HashMap();
			IJavaElement[] roots =
				((RegionBasedTypeHierarchy) this.hierarchy).region.getElements();
			int length = roots.length;
			if (monitor != null) monitor.beginTask("", length); //$NON-NLS-1$
			for (int i = 0; i <length; i++) {
				IJavaElement root = roots[i];
				IJavaProject javaProject = root.getJavaProject();
				ArrayList openables = (ArrayList) allOpenables.get(javaProject);
				if (openables == null) {
					openables = new ArrayList();
					allOpenables.put(javaProject, openables);
				}
				switch (root.getElementType()) {
					case IJavaElement.JAVA_PROJECT :
						injectAllOpenablesForJavaProject((IJavaProject) root, openables);
						break;
					case IJavaElement.PACKAGE_FRAGMENT_ROOT :
						injectAllOpenablesForPackageFragmentRoot((IPackageFragmentRoot) root, openables);
						break;
					case IJavaElement.PACKAGE_FRAGMENT :
						injectAllOpenablesForPackageFragment((IPackageFragment) root, openables);
						break;
					case IJavaElement.CLASS_FILE :
					case IJavaElement.COMPILATION_UNIT :
						openables.add(root);
						break;
					case IJavaElement.TYPE :
						IType type = (IType)root;
						if (type.isBinary()) {
							openables.add(type.getClassFile());
						} else {
							openables.add(type.getCompilationUnit());
						}
						break;
					default :
						break;
				}
				worked(monitor, 1);
			}
			return allOpenables;
		} finally {
			if (monitor != null) monitor.done();
		}
	}
	
	/**
	 * Adds all of the openables defined within this java project to the
	 * list.
	 */
	private void injectAllOpenablesForJavaProject(
		IJavaProject project,
		ArrayList openables) {
		try {
			IPackageFragmentRoot[] devPathRoots =
				((JavaProject) project).getPackageFragmentRoots();
			if (devPathRoots == null) {
				return;
			}
			for (int j = 0; j < devPathRoots.length; j++) {
				IPackageFragmentRoot root = devPathRoots[j];
				injectAllOpenablesForPackageFragmentRoot(root, openables);
			}
		} catch (JavaModelException e) {
			// ignore
		}
	}
	
	/**
	 * Adds all of the openables defined within this package fragment to the
	 * list.
	 */
	private void injectAllOpenablesForPackageFragment(
		IPackageFragment packFrag,
		ArrayList openables) {
			
		try {
			IPackageFragmentRoot root = (IPackageFragmentRoot) packFrag.getParent();
			int kind = root.getKind();
			if (kind != 0) {
				boolean isSourcePackageFragment = (kind == IPackageFragmentRoot.K_SOURCE);
				if (isSourcePackageFragment) {
					ICompilationUnit[] cus = packFrag.getCompilationUnits();
					for (int i = 0, length = cus.length; i < length; i++) {
						openables.add(cus[i]);
					}
				} else {
					IClassFile[] classFiles = packFrag.getClassFiles();
					for (int i = 0, length = classFiles.length; i < length; i++) {
						openables.add(classFiles[i]);
					}
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
	}
	
	/**
	 * Adds all of the openables defined within this package fragment root to the
	 * list.
	 */
	private void injectAllOpenablesForPackageFragmentRoot(
		IPackageFragmentRoot root,
		ArrayList openables) {
		try {
			IJavaElement[] packFrags = root.getChildren();
			for (int k = 0; k < packFrags.length; k++) {
				IPackageFragment packFrag = (IPackageFragment) packFrags[k];
				injectAllOpenablesForPackageFragment(packFrag, openables);
			}
		} catch (JavaModelException e) {
			return;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9529.java