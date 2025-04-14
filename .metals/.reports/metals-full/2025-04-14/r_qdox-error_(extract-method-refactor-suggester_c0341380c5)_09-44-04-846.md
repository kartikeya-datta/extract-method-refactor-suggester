error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3716.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3716.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3716.java
text:
```scala
I@@Index index = this.indexManager.getIndex(path, true /*reuse index file*/, false /*do not create if none*/);

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.search;

import java.util.ArrayList;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.index.IIndex;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;

/**
 * Selects the indexes that correspond to projects in a given search scope
 * and that are dependent on a given focus element.
 */
public class IndexSelector {
	IJavaSearchScope searchScope;
	IJavaElement focus;
	IndexManager indexManager;
	IIndex[] indexes;
public IndexSelector(
	IJavaSearchScope searchScope,
	IJavaElement focus,
	IndexManager indexManager) {
	this.searchScope = searchScope;
	this.focus = focus;
	this.indexManager = indexManager;
}
/**
 * Returns whether elements of the given project or jar can see the focus element
 * either because the focus is part of the project or the jar, or because it is 
 * accessible throught the project's classpath
 */
private boolean canSeeFocus(IPath projectOrJarPath) {
	// if it is a workspace scope, focus is visible from everywhere
	if (this.searchScope instanceof JavaWorkspaceScope) return true;
	
	try {
		while (!(this.focus instanceof IJavaProject) && !(this.focus instanceof JarPackageFragmentRoot)) {
			this.focus = this.focus.getParent();
		}
		IJavaModel model = this.focus.getJavaModel();
		IJavaProject project = this.getJavaProject(projectOrJarPath, model);
		if (this.focus instanceof JarPackageFragmentRoot) {
			// focus is part of a jar
			JarPackageFragmentRoot jar = (JarPackageFragmentRoot)this.focus;
			IPath jarPath = jar.getPath();
			if (project == null) {
				// consider that a jar can see another jar only they are both referenced by the same project
				return this.haveSameParent(projectOrJarPath, jarPath, model); 
			} else {
				IClasspathEntry[] entries = ((JavaProject)project).getExpandedClasspath(true);
				for (int i = 0, length = entries.length; i < length; i++) {
					IClasspathEntry entry = entries[i];
					if ((entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) 
						&& entry.getPath().equals(jarPath)) {
							return true;
					}
				}
				return false;
			}
		} else {
			// focus is part of a project
			IJavaProject focusProject = (IJavaProject)this.focus;
			if (project == null) {
				// consider that a jar can see a project only if it is referenced by this project
				IClasspathEntry[] entries = ((JavaProject)focusProject).getExpandedClasspath(true);
				for (int i = 0, length = entries.length; i < length; i++) {
					IClasspathEntry entry = entries[i];
					if ((entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) 
						&& entry.getPath().equals(projectOrJarPath)) {
							return true;
					}
				}
				return false;
			} else {
				if (focusProject.equals(project)) {
					return true;
				} else {
					IPath focusPath = focusProject.getProject().getFullPath();
					IClasspathEntry[] entries = ((JavaProject)project).getExpandedClasspath(true);
					for (int i = 0, length = entries.length; i < length; i++) {
						IClasspathEntry entry = entries[i];
						if ((entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) 
							&& entry.getPath().equals(focusPath)) {
								return true;
						}
					}
					return false;
				}
			}
		}
	} catch (JavaModelException e) {
		return false;
	}
}
private void computeIndexes() {
	ArrayList indexesInScope = new ArrayList();
	IPath[] projectsAndJars = this.searchScope.enclosingProjectsAndJars();
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace()	.getRoot();
	for (int i = 0; i < projectsAndJars.length; i++) {
		IPath location;
		IPath path = projectsAndJars[i];
		if ((!root.getProject(path.lastSegment()).exists()) // if project does not exist
			&& path.segmentCount() > 1
			&& ((location = root.getFile(path).getLocation()) == null
 !new java.io.File(location.toOSString()).exists()) // and internal jar file does not exist
			&& !new java.io.File(path.toOSString()).exists()) { // and external jar file does not exist
				continue;
		}
		if (this.focus == null || this.canSeeFocus(path)) {
			IIndex index = this.indexManager.getIndex(path, false);
			if (index != null && indexesInScope.indexOf(index) == -1) {
				indexesInScope.add(index);
			}
		}
	}
	this.indexes = new IIndex[indexesInScope.size()];
	indexesInScope.toArray(this.indexes);
}
public IIndex[] getIndexes() {
	if (this.indexes == null) {
		this.computeIndexes();
	}
	return this.indexes;
}
/**
 * Returns the java project that corresponds to the given path.
 * Returns null if the path doesn't correspond to a project.
 */
private IJavaProject getJavaProject(IPath path, IJavaModel model) {
	IJavaProject project = model.getJavaProject(path.lastSegment());
	if (project.exists()) {
		return project;
	} else {
		return null;
	}
}
/**
 * Returns whether the given jars are referenced in the classpath of the same project.
 */
private boolean haveSameParent(IPath jarPath1, IPath jarPath2, IJavaModel model) {
	if (jarPath1.equals(jarPath2)) {
		return true;
	}
	try {
		IJavaProject[] projects = model.getJavaProjects();
		for (int i = 0, length = projects.length; i < length; i++) {
			IJavaProject project = projects[i];
			IClasspathEntry[] entries = ((JavaProject)project).getExpandedClasspath(true);
			boolean referencesJar1 = false;
			boolean referencesJar2 = false;
			for (int j = 0, length2 = entries.length; j < length2; j++) {
				IClasspathEntry entry = entries[j];
				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					IPath entryPath = entry.getPath();
					if (entryPath.equals(jarPath1)) {
						referencesJar1 = true;
					} else if (entryPath.equals(jarPath2)) {
						referencesJar2 = true;
					}
				}
			}
			if (referencesJar1 && referencesJar2) {
				return true;
			}
		
		}
	} catch (JavaModelException e) {
		e.printStackTrace();
	}	
	return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3716.java