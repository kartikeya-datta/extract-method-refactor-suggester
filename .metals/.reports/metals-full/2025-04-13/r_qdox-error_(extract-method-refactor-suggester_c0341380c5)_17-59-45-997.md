error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10036.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10036.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10036.java
text:
```scala
c@@lasspath = project.getResolvedClasspath(true/*ignoreUnresolvedEntry*/, false/*don't generateMarkerOnError*/, false/*don't returnResolutionInProgress*/);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.util.Util;

/** 
 * Info for IJavaProject.
 * <p>
 * Note: <code>getChildren()</code> returns all of the <code>IPackageFragmentRoots</code>
 * specified on the classpath for the project.  This can include roots external to the
 * project. See <code>JavaProject#getAllPackageFragmentRoots()</code> and 
 * <code>JavaProject#getPackageFragmentRoots()</code>.  To get only the <code>IPackageFragmentRoots</code>
 * that are internal to the project, use <code>JavaProject#getChildren()</code>.
 */

/* package */
class JavaProjectElementInfo extends OpenableElementInfo {

	/**
	 * A array with all the non-java resources contained by this PackageFragment
	 */
	private Object[] nonJavaResources;
	
	/*
	 * A cache of all package fragment roots of this project.
	 */
	public IPackageFragmentRoot[] allPkgFragmentRootsCache;
	
	/*
	 * A cache of all package fragments in this project.
	 * (a map from String (the package name) to IPackageFragment[] (the package fragments with this name)
	 */
	private HashMap allPkgFragmentsCache;

	/**
	 * Create and initialize a new instance of the receiver
	 */
	public JavaProjectElementInfo() {
		this.nonJavaResources = null;
	}
	
	/**
	 * Compute the non-java resources contained in this java project.
	 */
	private Object[] computeNonJavaResources(JavaProject project) {
		
		// determine if src == project and/or if bin == project
		IPath projectPath = project.getProject().getFullPath();
		boolean srcIsProject = false;
		boolean binIsProject = false;
		char[][] inclusionPatterns = null;
		char[][] exclusionPatterns = null;
		IClasspathEntry[] classpath = null;
		IPath projectOutput = null;
		try {
			classpath = project.getResolvedClasspath(true/*ignore unresolved variable*/);
			for (int i = 0; i < classpath.length; i++) {
				IClasspathEntry entry = classpath[i];
				if (projectPath.equals(entry.getPath())) {
					srcIsProject = true;
					inclusionPatterns = ((ClasspathEntry)entry).fullInclusionPatternChars();
					exclusionPatterns = ((ClasspathEntry)entry).fullExclusionPatternChars();
					break;
				}
			}
			projectOutput = project.getOutputLocation();
			binIsProject = projectPath.equals(projectOutput);
		} catch (JavaModelException e) {
			// ignore
		}

		Object[] resources = new IResource[5];
		int resourcesCounter = 0;
		try {
			IResource[] members = ((IContainer) project.getResource()).members();
			for (int i = 0, max = members.length; i < max; i++) {
				IResource res = members[i];
				switch (res.getType()) {
					case IResource.FILE :
						IPath resFullPath = res.getFullPath();
						String resName = res.getName();
						
						// ignore a jar file on the classpath
						if (org.eclipse.jdt.internal.compiler.util.Util.isArchiveFileName(resName) && this.isClasspathEntryOrOutputLocation(resFullPath, classpath, projectOutput)) {
							break;
						}
						// ignore .java file if src == project
						if (srcIsProject 
							&& Util.isValidCompilationUnitName(resName)
							&& !Util.isExcluded(res, inclusionPatterns, exclusionPatterns)) {
							break;
						}
						// ignore .class file if bin == project
						if (binIsProject && Util.isValidClassFileName(resName)) {
							break;
						}
						// else add non java resource
						if (resources.length == resourcesCounter) {
							// resize
							System.arraycopy(
								resources,
								0,
								(resources = new IResource[resourcesCounter * 2]),
								0,
								resourcesCounter);
						}
						resources[resourcesCounter++] = res;
						break;
					case IResource.FOLDER :
						resFullPath = res.getFullPath();
						
						// ignore non-excluded folders on the classpath or that correspond to an output location
						if ((srcIsProject && !Util.isExcluded(res, inclusionPatterns, exclusionPatterns) && Util.isValidFolderNameForPackage(res.getName()))
 this.isClasspathEntryOrOutputLocation(resFullPath, classpath, projectOutput)) {
							break;
						}
						// else add non java resource
						if (resources.length == resourcesCounter) {
							// resize
							System.arraycopy(
								resources,
								0,
								(resources = new IResource[resourcesCounter * 2]),
								0,
								resourcesCounter);
						}
						resources[resourcesCounter++] = res;
				}
			}
			if (resources.length != resourcesCounter) {
				System.arraycopy(
					resources,
					0,
					(resources = new IResource[resourcesCounter]),
					0,
					resourcesCounter);
			}
		} catch (CoreException e) {
			resources = NO_NON_JAVA_RESOURCES;
			resourcesCounter = 0;
		}
		return resources;
	}

	IPackageFragmentRoot[] getAllPackageFragmentRoots(JavaProject project) {
		if (this.allPkgFragmentRootsCache == null) {
			try {
				this.allPkgFragmentRootsCache = project.getAllPackageFragmentRoots();
			} catch (JavaModelException e) {
				// project does not exist: cannot happend since this is the info of the project
			}
		}
		return this.allPkgFragmentRootsCache;
	}
	
	HashMap getAllPackageFragments(JavaProject project) {
		if (this.allPkgFragmentsCache == null) {
			HashMap cache = new HashMap();
			IPackageFragmentRoot[] roots = getAllPackageFragmentRoots(project);
			IPackageFragment[] frags = this.getPackageFragmentsInRoots(roots, project);
			for (int i= 0; i < frags.length; i++) {
				IPackageFragment fragment= frags[i];
				IPackageFragment[] entry= (IPackageFragment[]) cache.get(fragment.getElementName());
				if (entry == null) {
					entry= new IPackageFragment[1];
					entry[0]= fragment;
					cache.put(fragment.getElementName(), entry);
				} else {
					IPackageFragment[] copy= new IPackageFragment[entry.length + 1];
					System.arraycopy(entry, 0, copy, 0, entry.length);
					copy[entry.length]= fragment;
					cache.put(fragment.getElementName(), copy);
				}
			}
			this.allPkgFragmentsCache = cache;
		}
		return this.allPkgFragmentsCache;
	}
	
	/**
	 * Returns an array of non-java resources contained in the receiver.
	 */
	Object[] getNonJavaResources(JavaProject project) {

		if (this.nonJavaResources == null) {
			this.nonJavaResources = computeNonJavaResources(project);
		}
		return this.nonJavaResources;
	}
	
	/**
	 * Returns all the package fragments found in the specified
	 * package fragment roots. Make sure the returned fragments have the given
	 * project as great parent. This ensures the name lookup will not refer to another
	 * project (through jar package fragment roots)
	 */
	private IPackageFragment[] getPackageFragmentsInRoots(IPackageFragmentRoot[] roots, IJavaProject project) {

		// The following code assumes that all the roots have the given project as their parent
		ArrayList frags = new ArrayList();
		for (int i = 0; i < roots.length; i++) {
			IPackageFragmentRoot root = roots[i];
			try {
				IJavaElement[] pkgs = root.getChildren();

				/* 2 jar package fragment roots can be equals but not belonging 
				   to the same project. As a result, they share the same element info.
				   So this jar package fragment root could get the children of
				   another jar package fragment root.
				   The following code ensures that the children of this jar package
				   fragment root have the given project as a great parent.
				 */
				int length = pkgs.length;
				if (length == 0) continue;
				if (pkgs[0].getParent().getParent().equals(project)) {
					// the children have the right parent, simply add them to the list
					for (int j = 0; j < length; j++) {
						frags.add(pkgs[j]);
					}
				} else {
					// create a new handle with the root as the parent
					for (int j = 0; j < length; j++) {
						frags.add(root.getPackageFragment(pkgs[j].getElementName()));
					}
				}
			} catch (JavaModelException e) {
				// do nothing
			}
		}
		IPackageFragment[] fragments = new IPackageFragment[frags.size()];
		frags.toArray(fragments);
		return fragments;
	}

	/*
	 * Returns whether the given path is a classpath entry or an output location.
	 */
	private boolean isClasspathEntryOrOutputLocation(IPath path, IClasspathEntry[] resolvedClasspath, IPath projectOutput) {
		if (projectOutput.equals(path)) return true;
		for (int i = 0, length = resolvedClasspath.length; i < length; i++) {
			IClasspathEntry entry = resolvedClasspath[i];
			if (entry.getPath().equals(path)) {
				return true;
			}
			IPath output;
			if ((output = entry.getOutputLocation()) != null && output.equals(path)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Reset the package fragment roots and package fragment caches
	 */
	void resetCaches() {
		this.allPkgFragmentRootsCache = null;
		this.allPkgFragmentsCache = null;
	}
	
	/**
	 * Set the fNonJavaResources to res value
	 */
	void setNonJavaResources(Object[] resources) {

		this.nonJavaResources = resources;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10036.java