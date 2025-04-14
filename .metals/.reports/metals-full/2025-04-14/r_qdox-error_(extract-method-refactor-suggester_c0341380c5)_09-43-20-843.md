error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7440.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7440.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7440.java
text:
```scala
I@@ndexManager manager = JavaModelManager.getIndexManager();

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
package org.eclipse.jdt.internal.core.search;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.internal.compiler.util.SimpleSet;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.search.matching.MatchLocator;
import org.eclipse.jdt.internal.core.search.matching.MethodPattern;

/**
 * Selects the indexes that correspond to projects in a given search scope
 * and that are dependent on a given focus element.
 */
public class IndexSelector {
	IJavaSearchScope searchScope;
	SearchPattern pattern;
	IPath[] indexLocations; // cache of the keys for looking index up
	
public IndexSelector(
		IJavaSearchScope searchScope,
		SearchPattern pattern) {
	
	this.searchScope = searchScope;
	this.pattern = pattern;
}
/**
 * Returns whether elements of the given project or jar can see the given focus (an IJavaProject or
 * a JarPackageFragmentRot) either because the focus is part of the project or the jar, or because it is 
 * accessible throught the project's classpath
 */
public static boolean canSeeFocus(IJavaElement focus, boolean isPolymorphicSearch, IPath projectOrJarPath) {
	try {
		IClasspathEntry[] focusEntries = null;
		if (isPolymorphicSearch) {
			JavaProject focusProject = focus instanceof JarPackageFragmentRoot ? (JavaProject) focus.getParent() : (JavaProject) focus;
			focusEntries = focusProject.getExpandedClasspath();
		}
		IJavaModel model = focus.getJavaModel();
		IJavaProject project = getJavaProject(projectOrJarPath, model);
		if (project != null)
			return canSeeFocus(focus, (JavaProject) project, focusEntries);

		// projectOrJarPath is a jar
		// it can see the focus only if it is on the classpath of a project that can see the focus
		IJavaProject[] allProjects = model.getJavaProjects();
		for (int i = 0, length = allProjects.length; i < length; i++) {
			JavaProject otherProject = (JavaProject) allProjects[i];
			IClasspathEntry entry = otherProject.getClasspathEntryFor(projectOrJarPath);
			if (entry != null 
					&& entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY 
					&& canSeeFocus(focus, otherProject, focusEntries))
				return true;
		}
		return false;
	} catch (JavaModelException e) {
		return false;
	}
}
public static boolean canSeeFocus(IJavaElement focus, JavaProject javaProject, IClasspathEntry[] focusEntriesForPolymorphicSearch) {
	try {
		if (focus.equals(javaProject))
			return true;

		if (focusEntriesForPolymorphicSearch != null) {
			// look for refering project
			IPath projectPath = javaProject.getProject().getFullPath();
			for (int i = 0, length = focusEntriesForPolymorphicSearch.length; i < length; i++) {
				IClasspathEntry entry = focusEntriesForPolymorphicSearch[i];
				if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT && entry.getPath().equals(projectPath))
					return true;
			}
		}
		if (focus instanceof JarPackageFragmentRoot) {
			// focus is part of a jar
			IPath focusPath = focus.getPath();
			IClasspathEntry[] entries = javaProject.getExpandedClasspath();
			for (int i = 0, length = entries.length; i < length; i++) {
				IClasspathEntry entry = entries[i];
				if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY && entry.getPath().equals(focusPath))
					return true;
			}
			return false;
		}
		// look for dependent projects
		IPath focusPath = ((JavaProject) focus).getProject().getFullPath();
		IClasspathEntry[] entries = javaProject.getExpandedClasspath();
		for (int i = 0, length = entries.length; i < length; i++) {
			IClasspathEntry entry = entries[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT && entry.getPath().equals(focusPath))
				return true;
		}
		return false;
	} catch (JavaModelException e) {
		return false;
	}
}
/*
 *  Compute the list of paths which are keying index files.
 */
private void initializeIndexLocations() {
	IPath[] projectsAndJars = this.searchScope.enclosingProjectsAndJars();
	IndexManager manager = JavaModelManager.getJavaModelManager().getIndexManager();
	SimpleSet locations = new SimpleSet();
	IJavaElement focus = MatchLocator.projectOrJarFocus(this.pattern);
	if (focus == null) {
		for (int i = 0; i < projectsAndJars.length; i++)
			locations.add(manager.computeIndexLocation(projectsAndJars[i]));
	} else {
		try {
			// find the projects from projectsAndJars that see the focus then walk those projects looking for the jars from projectsAndJars
			int length = projectsAndJars.length;
			JavaProject[] projectsCanSeeFocus = new JavaProject[length];
			SimpleSet visitedProjects = new SimpleSet(length);
			int projectIndex = 0;
			SimpleSet jarsToCheck = new SimpleSet(length);
			IClasspathEntry[] focusEntries = null;
			if (this.pattern instanceof MethodPattern) { // should consider polymorphic search for method patterns
				JavaProject focusProject = focus instanceof JarPackageFragmentRoot ? (JavaProject) focus.getParent() : (JavaProject) focus;
				focusEntries = focusProject.getExpandedClasspath();
			}
			IJavaModel model = JavaModelManager.getJavaModelManager().getJavaModel();
			for (int i = 0; i < length; i++) {
				IPath path = projectsAndJars[i];
				JavaProject project = (JavaProject) getJavaProject(path, model);
				if (project != null) {
					visitedProjects.add(project);
					if (canSeeFocus(focus, project, focusEntries)) {
						locations.add(manager.computeIndexLocation(path));
						projectsCanSeeFocus[projectIndex++] = project;
					}
				} else {
					jarsToCheck.add(path);
				}
			}
			for (int i = 0; i < projectIndex && jarsToCheck.elementSize > 0; i++) {
				IClasspathEntry[] entries = projectsCanSeeFocus[i].getResolvedClasspath();
				for (int j = entries.length; --j >= 0;) {
					IClasspathEntry entry = entries[j];
					if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
						IPath path = entry.getPath();
						if (jarsToCheck.includes(path)) {
							locations.add(manager.computeIndexLocation(entry.getPath()));
							jarsToCheck.remove(path);
						}
					}
				}
			}
			// jar files can be included in the search scope without including one of the projects that references them, so scan all projects that have not been visited
			if (jarsToCheck.elementSize > 0) {
				IJavaProject[] allProjects = model.getJavaProjects();
				for (int i = 0, l = allProjects.length; i < l && jarsToCheck.elementSize > 0; i++) {
					JavaProject project = (JavaProject) allProjects[i];
					if (!visitedProjects.includes(project)) {
						IClasspathEntry[] entries = project.getResolvedClasspath();
						for (int j = entries.length; --j >= 0;) {
							IClasspathEntry entry = entries[j];
							if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
								IPath path = entry.getPath();
								if (jarsToCheck.includes(path)) {
									locations.add(manager.computeIndexLocation(entry.getPath()));
									jarsToCheck.remove(path);
								}
							}
						}
					}
				}
			}
		} catch (JavaModelException e) {
			// ignored
		}
	}

	this.indexLocations = new IPath[locations.elementSize];
	Object[] values = locations.values;
	int count = 0;
	for (int i = values.length; --i >= 0;)
		if (values[i] != null)
			this.indexLocations[count++] = (IPath) values[i];
}
public IPath[] getIndexLocations() {
	if (this.indexLocations == null) {
		this.initializeIndexLocations(); 
	}
	return this.indexLocations;
}

/**
 * Returns the java project that corresponds to the given path.
 * Returns null if the path doesn't correspond to a project.
 */
private static IJavaProject getJavaProject(IPath path, IJavaModel model) {
	IJavaProject project = model.getJavaProject(path.lastSegment());
	if (project.exists()) {
		return project;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7440.java