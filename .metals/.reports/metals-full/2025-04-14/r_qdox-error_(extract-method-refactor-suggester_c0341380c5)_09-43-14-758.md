error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9204.java
text:
```scala
i@@f (root.getKind() == IPackageFragmentRoot.K_BINARY) {

package org.eclipse.jdt.internal.core.search;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.Path;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.AbstractSearchScope;

import java.util.*;

/**
 * A Java-specific scope for searching relative to one or more java elements.
 */
public class JavaSearchScope implements IJavaSearchScope {
	
	private ArrayList elements;

	/* The paths of the resources in this search scope 
	   (or the classpath entries' paths 
	   if the resources are projects) */
	private IPath[] paths = new IPath[1];
	private boolean[] pathWithSubFolders = new boolean[1];
	private int pathsCount = 0;
	
	private IPath[] enclosingProjectsAndJars = new IPath[0];
	
private void addEnclosingProjectOrJar(IPath path) {
	int length = this.enclosingProjectsAndJars.length;
	for (int i = 0; i < length; i++) {
		if (this.enclosingProjectsAndJars[i].equals(path)) return;
	}
	System.arraycopy(
		this.enclosingProjectsAndJars,
		0,
		this.enclosingProjectsAndJars = new IPath[length+1],
		0,
		length);
	this.enclosingProjectsAndJars[length] = path;
}

public void add(IJavaProject javaProject, boolean includesPrereqProjects, HashSet visitedProjects) throws JavaModelException {
	IProject project = javaProject.getProject();
	if (!project.isAccessible() || !visitedProjects.add(project)) return;

	this.addEnclosingProjectOrJar(project.getFullPath());

	IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
	IJavaModel model = javaProject.getJavaModel();
	for (int i = 0, length = entries.length; i < length; i++) {
		IClasspathEntry entry = entries[i];
		switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_LIBRARY:
				IPath path = entry.getPath();
				this.add(path, true);
				this.addEnclosingProjectOrJar(path);
				break;
			case IClasspathEntry.CPE_PROJECT:
				if (includesPrereqProjects) {
					this.add(model.getJavaProject(entry.getPath().lastSegment()), true, visitedProjects);
				}
				break;
			case IClasspathEntry.CPE_SOURCE:
				this.add(entry.getPath(), true);
				break;
		}
	}
}
public void add(IJavaElement element) throws JavaModelException {
	IPackageFragmentRoot root = null;
	switch (element.getElementType()) {
		case IJavaElement.JAVA_MODEL:
			// a workspace sope should be used
			break; 
		case IJavaElement.JAVA_PROJECT:
			this.add((IJavaProject)element, true, new HashSet(2));
			break;
		case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			root = (IPackageFragmentRoot)element;
			this.add(root.getPath(), true);
			break;
		case IJavaElement.PACKAGE_FRAGMENT:
			root = (IPackageFragmentRoot)element.getParent();
			if (root.isArchive()) {
				this.add(root.getPath().append(new Path(element.getElementName().replace('.', '/'))), false);
			} else {
				IResource resource = element.getUnderlyingResource();
				if (resource != null && resource.isAccessible()) {
					this.add(resource.getFullPath(), false);
				}
			}
			break;
		default:
			IResource resource = element.getUnderlyingResource();
			if (resource != null && resource.isAccessible()) {
				// remember sub-cu (or sub-class file) java elements
				if (element instanceof IMember) {
					if (this.elements == null) {
						this.elements = new ArrayList();
					}
					this.elements.add(element);
				}
				this.add(resource.getFullPath(), true);
				
				// find package fragment root including this java element
				IJavaElement parent = element.getParent();
				while (parent != null && !(parent instanceof IPackageFragmentRoot)) {
					parent = parent.getParent();
				}
				if (parent instanceof IPackageFragmentRoot) {
					root = (IPackageFragmentRoot)parent;
				}
			}	
	}
	
	if (root != null) {
		if (root.isArchive()) {
			this.addEnclosingProjectOrJar(root.getPath());
		} else {
			this.addEnclosingProjectOrJar(root.getJavaProject().getProject().getFullPath());
		}
	}
}

/**
 * Adds the given path to this search scope. Remember if subfolders need to be included as well.
 */
private void add(IPath path, boolean withSubFolders) {
	if (this.paths.length == this.pathsCount) {
		System.arraycopy(
			this.paths,
			0,
			this.paths = new IPath[this.pathsCount * 2],
			0,
			this.pathsCount);
		System.arraycopy(
			this.pathWithSubFolders,
			0,
			this.pathWithSubFolders = new boolean[this.pathsCount * 2],
			0,
			this.pathsCount);
	}
	this.paths[this.pathsCount] = path;
	this.pathWithSubFolders[this.pathsCount++] = withSubFolders; 
}

/* (non-Javadoc)
 * @see IJavaSearchScope#encloses(String)
 */
public boolean encloses(String resourcePathString) {
	IPath resourcePath;
	int separatorIndex = resourcePathString.indexOf(JAR_FILE_ENTRY_SEPARATOR);
	if (separatorIndex != -1) {
		resourcePath = 
			new Path(resourcePathString.substring(0, separatorIndex)).
				append(new Path(resourcePathString.substring(separatorIndex+1)));
	} else {
			resourcePath = new Path(resourcePathString);
	}
	return this.encloses(resourcePath);
}

/**
 * Returns whether this search scope encloses the given path.
 */
private boolean encloses(IPath path) {
	for (int i = 0; i < this.pathsCount; i++) {
		if (this.pathWithSubFolders[i]) {
			if (this.paths[i].isPrefixOf(path)) {
				return true;
			}
		} else {
			IPath scopePath = this.paths[i];
			if (scopePath.isPrefixOf(path) 
				&& (scopePath.segmentCount() == path.segmentCount() - 1)) {
				return true;
			}
		}
	}
	return false;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#encloses(IJavaElement)
 */
public boolean encloses(IJavaElement element) {
	if (this.elements != null) {
		for (int i = 0, length = this.elements.size(); i < length; i++) {
			IJavaElement scopeElement = (IJavaElement)this.elements.get(i);
			IJavaElement searchedElement = element;
			while (searchedElement != null) {
				if (searchedElement.equals(scopeElement)) {
					return true;
				} else {
					searchedElement = searchedElement.getParent();
				}
			}
		}
		return false;
	} else {
		return this.encloses(this.fullPath(element));
	}
}

/* (non-Javadoc)
 * @see IJavaSearchScope#enclosingProjectsAndJars()
 */
public IPath[] enclosingProjectsAndJars() {
	return this.enclosingProjectsAndJars;
}
private IPath fullPath(IJavaElement element) {
	if (element instanceof IPackageFragmentRoot) {
		return ((IPackageFragmentRoot)element).getPath();
	} else 	{
		IJavaElement parent = element.getParent();
		IPath parentPath = parent == null ? null : this.fullPath(parent);
		IPath childPath;
		if (element instanceof IPackageFragment) {
			childPath = new Path(element.getElementName().replace('.', '/'));
		} else if (element instanceof IOpenable) {
			childPath = new Path(element.getElementName());
		} else {
			return parentPath;
		}
		return parentPath == null ? childPath : parentPath.append(childPath);
	}
}

/* (non-Javadoc)
 * @see IJavaSearchScope#includesBinaries()
 * @deprecated
 */
public boolean includesBinaries() {
	return true;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#includesClasspaths()
 * @deprecated
 */
public boolean includesClasspaths() {
	return true;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#setIncludesBinaries
 * @deprecated
 */
public void setIncludesBinaries(boolean includesBinaries) {
}

/* (non-Javadoc)
 * @see IJavaSearchScope#setIncludeClasspaths
 * @deprecated
 */
public void setIncludesClasspaths(boolean includesClasspaths) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9204.java