error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8518.java
text:
```scala
t@@his.javaModel = JavaModelManager.getJavaModelManager().getJavaModel();

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.index.impl.*;

/**
 * Creates java element handles.
 */
public class HandleFactory {

	/**
	 * Cache package fragment root information to optimize speed performance.
	 */
	private String lastPkgFragmentRootPath;
	private IPackageFragmentRoot lastPkgFragmentRoot;

	/**
	 * Cache package handles to optimize memory.
	 */
	private Map packageHandles;

	private IWorkspace workspace;
	private JavaModel javaModel;


	public HandleFactory(IWorkspace workspace) {
		this.workspace = workspace;
		this.javaModel = JavaModelManager.getJavaModel(workspace);
	}
	/**
	 * Creates an Openable handle from the given resource path.
	 * The resource path can be a path to a file in the workbench (eg. /Proj/com/ibm/jdt/core/HandleFactory.java)
	 * or a path to a file in a jar file - it then contains the path to the jar file and the path to the file in the jar
	 * (eg. c:/jdk1.2.2/jre/lib/rt.jar|java/lang/Object.class or c:/workbench/Proj/rt.jar|java/lang/Object.class)
	 * NOTE: This assumes that the resource path is the toString() of an IPath, 
	 *       i.e. it uses the IPath.SEPARATOR for file path
	 *            and it uses '/' for entries in a zip file.
	 */
	public Openable createOpenable(String resourcePath) {
		int separatorIndex;
		if ((separatorIndex= resourcePath.indexOf(JarFileEntryDocument.JAR_FILE_ENTRY_SEPARATOR)) > -1) {
			// path to a class file inside a jar
			String jarPath= resourcePath.substring(0, separatorIndex);
			// Optimization: cache package fragment root handle and package handles
			if (!jarPath.equals(this.lastPkgFragmentRootPath)) {
				IPackageFragmentRoot root= this.getJarPkgFragmentRoot(jarPath);
				if (root == null)
					return null; // match is outside classpath
				this.lastPkgFragmentRootPath= jarPath;
				this.lastPkgFragmentRoot= root;
				this.packageHandles= new HashMap(5);
			}
			// create handle
			String classFilePath= resourcePath.substring(separatorIndex + 1);
			int lastSlash= classFilePath.lastIndexOf('/');
			String packageName= lastSlash > -1 ? classFilePath.substring(0, lastSlash).replace('/', '.') : IPackageFragment.DEFAULT_PACKAGE_NAME;
			IPackageFragment pkgFragment= (IPackageFragment) this.packageHandles.get(packageName);
			if (pkgFragment == null) {
				pkgFragment= this.lastPkgFragmentRoot.getPackageFragment(packageName);
				this.packageHandles.put(packageName, pkgFragment);
			}
			IClassFile classFile= pkgFragment.getClassFile(classFilePath.substring(lastSlash + 1));
			return (Openable) classFile;
		} else {
			// path to a file in a directory
			// Optimization: cache package fragment root handle and package handles
			int length = -1;
			if (this.lastPkgFragmentRootPath == null 
 !(resourcePath.startsWith(this.lastPkgFragmentRootPath) 
					&& (length = this.lastPkgFragmentRootPath.length()) > 0
					&& resourcePath.charAt(length) == '/')) {
				IPackageFragmentRoot root= this.getPkgFragmentRoot(resourcePath);
				if (root == null)
					return null; // match is outside classpath
				this.lastPkgFragmentRoot= root;
				this.lastPkgFragmentRootPath= this.lastPkgFragmentRoot.getPath().toString();
				this.packageHandles= new HashMap(5);
			}
			// create handle
			int lastSlash= resourcePath.lastIndexOf(IPath.SEPARATOR);
			String packageName= lastSlash > (length= this.lastPkgFragmentRootPath.length()) ? resourcePath.substring(length + 1, lastSlash).replace(IPath.SEPARATOR, '.') : IPackageFragment.DEFAULT_PACKAGE_NAME;
			IPackageFragment pkgFragment= (IPackageFragment) this.packageHandles.get(packageName);
			if (pkgFragment == null) {
				pkgFragment= this.lastPkgFragmentRoot.getPackageFragment(packageName);
				this.packageHandles.put(packageName, pkgFragment);
			}
			String simpleName= resourcePath.substring(lastSlash + 1);
			if (Util.isJavaFileName(simpleName)) {
				ICompilationUnit unit= pkgFragment.getCompilationUnit(simpleName);
				return (Openable) unit;
			} else {
				IClassFile classFile= pkgFragment.getClassFile(simpleName);
				return (Openable) classFile;
			}
		}
	}
/**
	 * Returns the package fragment root that corresponds to the given jar path.
	 * See createOpenable(...) for the format of the jar path string.
	 */
	private IPackageFragmentRoot getJarPkgFragmentRoot(String jarPathString) {

		IPath jarPath= new Path(jarPathString);
		IResource jarFile= this.workspace.getRoot().findMember(jarPath);
		if (jarFile != null) {
			// internal jar
			return this.javaModel.getJavaProject(jarFile).getPackageFragmentRoot(jarFile);
		} else {
			// external jar: walk all projects and find the first one that has the given jar path in its classpath
			IProject[] projects= this.workspace.getRoot().getProjects();
			for (int i= 0, projectCount= projects.length; i < projectCount; i++) {
				try {
					IProject project = projects[i];
					if (!project.isAccessible() 
 !project.hasNature(JavaCore.NATURE_ID)) continue;
					JavaProject javaProject= (JavaProject)this.javaModel.getJavaProject(project);
					IClasspathEntry[] classpathEntries= javaProject.getResolvedClasspath(true);
					for (int j= 0, entryCount= classpathEntries.length; j < entryCount; j++) {
						if (classpathEntries[j].getPath().equals(jarPath)) {
							return javaProject.getPackageFragmentRoot0(jarPathString);
						}
					}
				} catch (CoreException e) {
					// CoreException from hasNature - should not happen since we check that the project is accessible
					// JavaModelException from getResolvedClasspath - a problem occured while accessing project: nothing we can do, ignore
				}
			}
			return null;
		}
	}
/**
	 * Returns the package fragment root that contains the given resource path.
	 */
	private IPackageFragmentRoot getPkgFragmentRoot(String pathString) {

		IPath path= new Path(pathString);
		IProject[] projects= this.workspace.getRoot().getProjects();
		for (int i= 0, max= projects.length; i < max; i++) {
			try {
				IProject project = projects[i];
				if (!project.isAccessible() 
 !project.hasNature(JavaCore.NATURE_ID)) continue;
				IJavaProject javaProject= this.javaModel.getJavaProject(project);
				IPackageFragmentRoot[] roots= javaProject.getPackageFragmentRoots();
				for (int j= 0, rootCount= roots.length; j < rootCount; j++) {
					IPackageFragmentRoot root= roots[j];
					if (root.getPath().isPrefixOf(path)) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8518.java