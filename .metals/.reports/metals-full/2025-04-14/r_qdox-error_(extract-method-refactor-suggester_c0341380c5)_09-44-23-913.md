error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8881.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8881.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8881.java
text:
```scala
i@@f (path.segmentCount() == 1) {

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
package org.eclipse.jdt.internal.core.util;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * Creates java element handles.
 */
public class HandleFactory {

	/**
	 * Cache package fragment root information to optimize speed performance.
	 */
	private String lastPkgFragmentRootPath;
	private PackageFragmentRoot lastPkgFragmentRoot;

	/**
	 * Cache package handles to optimize memory.
	 */
	private HashtableOfArrayToObject packageHandles;

	private JavaModel javaModel;

	public HandleFactory() {
		this.javaModel = JavaModelManager.getJavaModelManager().getJavaModel();
	}
	

	/**
	 * Creates an Openable handle from the given resource path.
	 * The resource path can be a path to a file in the workbench (eg. /Proj/com/ibm/jdt/core/HandleFactory.java)
	 * or a path to a file in a jar file - it then contains the path to the jar file and the path to the file in the jar
	 * (eg. c:/jdk1.2.2/jre/lib/rt.jar|java/lang/Object.class or /Proj/rt.jar|java/lang/Object.class)
	 * NOTE: This assumes that the resource path is the toString() of an IPath, 
	 *       in other words, it uses the IPath.SEPARATOR for file path
	 *            and it uses '/' for entries in a zip file.
	 * If not null, uses the given scope as a hint for getting Java project handles.
	 */
	public Openable createOpenable(String resourcePath, IJavaSearchScope scope) {
		int separatorIndex;
		if ((separatorIndex= resourcePath.indexOf(IJavaSearchScope.JAR_FILE_ENTRY_SEPARATOR)) > -1) {
			// path to a class file inside a jar
			// Optimization: cache package fragment root handle and package handles
			int rootPathLength;
			if (this.lastPkgFragmentRootPath == null 
 (rootPathLength = this.lastPkgFragmentRootPath.length()) != resourcePath.length()
 !resourcePath.regionMatches(0, this.lastPkgFragmentRootPath, 0, rootPathLength)) {
				String jarPath= resourcePath.substring(0, separatorIndex);
				PackageFragmentRoot root= getJarPkgFragmentRoot(jarPath, scope);
				if (root == null)
					return null; // match is outside classpath
				this.lastPkgFragmentRootPath= jarPath;
				this.lastPkgFragmentRoot= root;
				this.packageHandles= new HashtableOfArrayToObject(5);
			}
			// create handle
			String classFilePath= resourcePath.substring(separatorIndex + 1);
			String[] simpleNames = new Path(classFilePath).segments();
			String[] pkgName;
			int length = simpleNames.length-1;
			if (length > 0) {
				pkgName = new String[length];
				System.arraycopy(simpleNames, 0, pkgName, 0, length);
			} else {
				pkgName = CharOperation.NO_STRINGS;
			}
			IPackageFragment pkgFragment= (IPackageFragment) this.packageHandles.get(pkgName);
			if (pkgFragment == null) {
				pkgFragment= this.lastPkgFragmentRoot.getPackageFragment(pkgName);
				this.packageHandles.put(pkgName, pkgFragment);
			}
			IClassFile classFile= pkgFragment.getClassFile(simpleNames[length]);
			return (Openable) classFile;
		} else {
			// path to a file in a directory
			// Optimization: cache package fragment root handle and package handles
			int rootPathLength = -1;
			if (this.lastPkgFragmentRootPath == null 
 !(resourcePath.startsWith(this.lastPkgFragmentRootPath) 
					&& !org.eclipse.jdt.internal.compiler.util.Util.isExcluded(resourcePath.toCharArray(), this.lastPkgFragmentRoot.fullInclusionPatternChars(), this.lastPkgFragmentRoot.fullExclusionPatternChars(), false)
					&& (rootPathLength = this.lastPkgFragmentRootPath.length()) > 0
					&& resourcePath.charAt(rootPathLength) == '/')) {
				PackageFragmentRoot root= getPkgFragmentRoot(resourcePath);
				if (root == null)
					return null; // match is outside classpath
				this.lastPkgFragmentRoot = root;
				this.lastPkgFragmentRootPath = this.lastPkgFragmentRoot.internalPath().toString();
				this.packageHandles = new HashtableOfArrayToObject(5);
			}
			// create handle
			resourcePath = resourcePath.substring(this.lastPkgFragmentRootPath.length() + 1);
			String[] simpleNames = new Path(resourcePath).segments();
			String[] pkgName;
			int length = simpleNames.length-1;
			if (length > 0) {
				pkgName = new String[length];
				System.arraycopy(simpleNames, 0, pkgName, 0, length);
			} else {
				pkgName = CharOperation.NO_STRINGS;
			}
			IPackageFragment pkgFragment= (IPackageFragment) this.packageHandles.get(pkgName);
			if (pkgFragment == null) {
				pkgFragment= this.lastPkgFragmentRoot.getPackageFragment(pkgName);
				this.packageHandles.put(pkgName, pkgFragment);
			}
			String simpleName= simpleNames[length];
			if (org.eclipse.jdt.internal.core.util.Util.isJavaLikeFileName(simpleName)) {
				ICompilationUnit unit= pkgFragment.getCompilationUnit(simpleName);
				return (Openable) unit;
			} else {
				IClassFile classFile= pkgFragment.getClassFile(simpleName);
				return (Openable) classFile;
			}
		}
	}	
	
	/**
	 * Returns a handle denoting the class member identified by its scope.
	 */
	public IJavaElement createElement(ClassScope scope, ICompilationUnit unit, HashSet existingElements, HashMap knownScopes) {
		return createElement(scope, scope.referenceContext.sourceStart, unit, existingElements, knownScopes);
	}
	/**
	 * Create handle by adding child to parent obtained by recursing into parent scopes.
	 */
	private IJavaElement createElement(Scope scope, int elementPosition, ICompilationUnit unit, HashSet existingElements, HashMap knownScopes) {
		IJavaElement newElement = (IJavaElement)knownScopes.get(scope);
		if (newElement != null) return newElement;
	
		switch(scope.kind) {
			case Scope.COMPILATION_UNIT_SCOPE :
				newElement = unit;
				break;			
			case Scope.CLASS_SCOPE :
				IJavaElement parentElement = createElement(scope.parent, elementPosition, unit, existingElements, knownScopes);
				switch (parentElement.getElementType()) {
					case IJavaElement.COMPILATION_UNIT :
						newElement = ((ICompilationUnit)parentElement).getType(new String(scope.enclosingSourceType().sourceName));
						break;						
					case IJavaElement.TYPE :
						newElement = ((IType)parentElement).getType(new String(scope.enclosingSourceType().sourceName));
						break;
					case IJavaElement.FIELD :
					case IJavaElement.INITIALIZER :
					case IJavaElement.METHOD :
					    IMember member = (IMember)parentElement;
					    if (member.isBinary()) {
					        return null;
					    } else {
							newElement = member.getType(new String(scope.enclosingSourceType().sourceName), 1);
							// increment occurrence count if collision is detected
							if (newElement != null) {
								while (!existingElements.add(newElement)) ((SourceRefElement)newElement).occurrenceCount++;
							}
					    }
						break;						
				}
				if (newElement != null) {
					knownScopes.put(scope, newElement);
				}
				break;
			case Scope.METHOD_SCOPE :
				IType parentType = (IType) createElement(scope.parent, elementPosition, unit, existingElements, knownScopes);
				MethodScope methodScope = (MethodScope) scope;
				if (methodScope.isInsideInitializer()) {
					// inside field or initializer, must find proper one
					TypeDeclaration type = methodScope.referenceType();
					int occurenceCount = 1;
					for (int i = 0, length = type.fields.length; i < length; i++) {
						FieldDeclaration field = type.fields[i];
						if (field.declarationSourceStart <= elementPosition && elementPosition <= field.declarationSourceEnd) {
							switch (field.getKind()) {
								case AbstractVariableDeclaration.FIELD :
								case AbstractVariableDeclaration.ENUM_CONSTANT :
									newElement = parentType.getField(new String(field.name));
									break;
								case AbstractVariableDeclaration.INITIALIZER :
									newElement = parentType.getInitializer(occurenceCount);
									break;
							}
							break;
						} else if (field.getKind() == AbstractVariableDeclaration.INITIALIZER) {
							occurenceCount++;
						}
					}
				} else {
					// method element
					AbstractMethodDeclaration method = methodScope.referenceMethod();
					newElement = parentType.getMethod(new String(method.selector), Util.typeParameterSignatures(method));
					if (newElement != null) {
						knownScopes.put(scope, newElement);
					}
				}
				break;				
			case Scope.BLOCK_SCOPE :
				// standard block, no element per se
				newElement = createElement(scope.parent, elementPosition, unit, existingElements, knownScopes);
				break;
		}
		return newElement;
	}
	/**
	 * Returns the package fragment root that corresponds to the given jar path.
	 * See createOpenable(...) for the format of the jar path string.
	 * If not null, uses the given scope as a hint for getting Java project handles.
	 */
	private PackageFragmentRoot getJarPkgFragmentRoot(String jarPathString, IJavaSearchScope scope) {

		IPath jarPath= new Path(jarPathString);
		
		Object target = JavaModel.getTarget(jarPath, false);
		if (target instanceof IFile) {
			// internal jar: is it on the classpath of its project?
			//  e.g. org.eclipse.swt.win32/ws/win32/swt.jar 
			//        is NOT on the classpath of org.eclipse.swt.win32
			IFile jarFile = (IFile)target;
			JavaProject javaProject = (JavaProject) this.javaModel.getJavaProject(jarFile);
			try {
				IClasspathEntry entry = javaProject.getClasspathEntryFor(jarPath);
				if (entry != null) {
					return (PackageFragmentRoot) javaProject.getPackageFragmentRoot(jarFile);
				}
			} catch (JavaModelException e) {
				// ignore and try to find another project
			}
		}
		
		// walk projects in the scope and find the first one that has the given jar path in its classpath
		IJavaProject[] projects;
		if (scope != null) {
			IPath[] enclosingProjectsAndJars = scope.enclosingProjectsAndJars();
			int length = enclosingProjectsAndJars.length;
			projects = new IJavaProject[length];
			int index = 0;
			for (int i = 0; i < length; i++) {
				IPath path = enclosingProjectsAndJars[i];
				if (!org.eclipse.jdt.internal.compiler.util.Util.isArchiveFileName(path.lastSegment())) {
					projects[index++] = this.javaModel.getJavaProject(path.segment(0));
				}
			}
			if (index < length) {
				System.arraycopy(projects, 0, projects = new IJavaProject[index], 0, index);
			}
			PackageFragmentRoot root = getJarPkgFragmentRoot(jarPath, target, projects);
			if (root != null) {
				return root;
			}
		} 
		
		// not found in the scope, walk all projects
		try {
			projects = this.javaModel.getJavaProjects();
		} catch (JavaModelException e) {
			// java model is not accessible
			return null;
		}
		return getJarPkgFragmentRoot(jarPath, target, projects);
	}
	
	private PackageFragmentRoot getJarPkgFragmentRoot(
		IPath jarPath,
		Object target,
		IJavaProject[] projects) {
		for (int i= 0, projectCount= projects.length; i < projectCount; i++) {
			try {
				JavaProject javaProject= (JavaProject)projects[i];
				IClasspathEntry classpathEnty = javaProject.getClasspathEntryFor(jarPath);
				if (classpathEnty != null) {
					if (target instanceof IFile) {
						// internal jar
						return (PackageFragmentRoot) javaProject.getPackageFragmentRoot((IFile)target);
					} else {
						// external jar
						return (PackageFragmentRoot) javaProject.getPackageFragmentRoot0(jarPath);
					}
				}
			} catch (JavaModelException e) {
				// JavaModelException from getResolvedClasspath - a problem occurred while accessing project: nothing we can do, ignore
			}
		}
		return null;
	}
	
	/**
	 * Returns the package fragment root that contains the given resource path.
	 */
	private PackageFragmentRoot getPkgFragmentRoot(String pathString) {

		IPath path= new Path(pathString);
		IProject[] projects= ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int i= 0, max= projects.length; i < max; i++) {
			try {
				IProject project = projects[i];
				if (!project.isAccessible() 
 !project.hasNature(JavaCore.NATURE_ID)) continue;
				IJavaProject javaProject= this.javaModel.getJavaProject(project);
				IPackageFragmentRoot[] roots= javaProject.getPackageFragmentRoots();
				for (int j= 0, rootCount= roots.length; j < rootCount; j++) {
					PackageFragmentRoot root= (PackageFragmentRoot)roots[j];
					if (root.internalPath().isPrefixOf(path) && !Util.isExcluded(path, root.fullInclusionPatternChars(), root.fullExclusionPatternChars(), false)) {
						return root;
					}
				}
			} catch (CoreException e) {
				// CoreException from hasNature - should not happen since we check that the project is accessible
				// JavaModelException from getPackageFragmentRoots - a problem occured while accessing project: nothing we can do, ignore
			}
		}
		return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8881.java