error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9138.java
text:
```scala
I@@ClasspathEntry[] entries = ((JavaProject)javaProject).getExpandedClasspath(true);

package org.eclipse.jdt.internal.core.search;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.search.AbstractSearchScope;

import java.util.*;

/**
 * A Java-specific scope for searching relative to one or more projects.
 * The scope can be configured to follow the respective classpath of 
 * in-scope projects, and to not search binaries. By default, both classpaths 
 * and binaries are included. 
 */
public class JavaSearchScope extends AbstractSearchScope implements IJavaSearchScope {

	private boolean includesBinaries = true;
	private boolean includesClasspaths = true;
    
	private IResource fLastCheckedResource;
	private boolean fLastResult;

	/* The paths of the resources in this search scope 
	   (or the classpath entries' paths 
	   if the resources are projects) */
	private IPath[] paths = new IPath[1];
	private int pathsCount = 0;
	
/**
 * Adds the given resource to this search scope.
 */
public void add(IResource element) {
	this.add(element, true);
}

/**
 * Adds the given resource to this search scope.
 * If asked and if the element is a project, also add its external jars,
 * jars that are internal to the project and source folders.
 */
public void add(IResource element, boolean addJarsAndSourceFolders) {
	super.add(element);

	// clear indexer cache
	fLastCheckedResource = null;

	if (addJarsAndSourceFolders && element instanceof IProject) {
		// remember the paths of its classpath entries
		IJavaModel javaModel = JavaModelManager.getJavaModel(element.getWorkspace());
		IJavaProject javaProject = javaModel.getJavaProject(element.getName());
		try {
			// add only external jars, internal jars (that are internal to the project)
			// and source folders of the project
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
			for (int i = 0, length = entries.length; i < length; i++) {
				IClasspathEntry entry = entries[i];
				IPath path = entry.getPath();
				switch (entry.getEntryKind()) {
					case IClasspathEntry.CPE_LIBRARY:
						if (element.getFullPath().isPrefixOf(path) // jar internal to project 
 element.getWorkspace().getRoot().findMember(path) == null) { // jar external to workspace
							this.add(path);
						}
						break;
					case IClasspathEntry.CPE_SOURCE:
						if (element.getFullPath().isPrefixOf(path)) { // source folder inside project
							this.add(path);
						}
						break;
				}
			}
		} catch (JavaModelException e) {
		}
	} else {
		this.add(element.getFullPath());
	}
}

/**
 * Adds the given path to this search scope.
 */
private void add(IPath path) {
	if (this.paths.length == this.pathsCount) {
		System.arraycopy(
			this.paths,
			0,
			this.paths = new IPath[this.pathsCount * 2],
			0,
			this.pathsCount);
	}
	this.paths[this.pathsCount++] = path;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#encloses(String)
 */
public boolean encloses(String resourcePathString) {
	int separatorIndex = resourcePathString.indexOf(JAR_FILE_ENTRY_SEPARATOR);
	if (separatorIndex != -1) {
		resourcePathString = resourcePathString.substring(0, separatorIndex);
	}
	IPath resourcePath = new Path(resourcePathString);
	for (int i = 0; i < this.pathsCount; i++){
		if (this.paths[i].isPrefixOf(resourcePath)) {
			return true;
		}
	}
	return false;
}

/**
 * Returns whether this search scope encloses the given resource.
 */
protected boolean encloses(IResource element) {
	boolean encloses = false;
	IPath elementPath = element.getFullPath();
	for (int i = 0; i < this.pathsCount; i++) {
		if (this.paths[i].isPrefixOf(elementPath)) {
			encloses = true;
			break;
		}
	}
	fLastCheckedResource = element;
	fLastResult = encloses;
	return encloses;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#encloses(IJavaElement)
 */
public boolean encloses(IJavaElement element) {
	try {
		IResource resource = element.getUnderlyingResource();
		if (resource == null) {
			// case of a binary in an external jar
			return true;
		} else if (resource.equals(fLastCheckedResource)) {
			return fLastResult;
		}
		return encloses(resource);
	} catch (JavaModelException e) {
		return false;
	}
}

/* (non-Javadoc)
 * @see IJavaSearchScope#enclosingProjectsAndJars()
 */
public IPath[] enclosingProjectsAndJars() {
	try {
		Vector paths = new Vector();
		IJavaModel javaModel = JavaModelManager.getJavaModel(ResourcesPlugin.getWorkspace());
		IWorkspaceRoot root = javaModel.getWorkspace().getRoot();
		for (int i = 0; i < this.elementCount; i++){
			IResource element = this.elements[i];
			IPath path = element.getProject().getFullPath();
			IProject project = element.getProject();
			if (project.exists() && project.isOpen()) {
				if (!paths.contains(path)) paths.add(path);
				if (this.includesClasspaths) {
					IJavaProject javaProject = javaModel.getJavaProject(project.getName());
					IClasspathEntry[] entries = javaProject.getExpandedClasspath(true);
					for (int j = 0; j < entries.length; j++) {
						IClasspathEntry entry = entries[j];
						switch (entry.getEntryKind()) {
							case IClasspathEntry.CPE_PROJECT:
								path = entry.getPath();
								if (!paths.contains(path) && root.getProject(path.lastSegment()).isAccessible()) {
									paths.add(path);
								}
								break;
							case IClasspathEntry.CPE_LIBRARY:
								if (this.includesBinaries) {
									path = entry.getPath();
									if (!paths.contains(path)) paths.add(path);
								}
								break;
						}
					}
				}
			}
		}
		IPath[] result = new IPath[paths.size()];
		paths.copyInto(result);
		return result;
	} catch (JavaModelException e) {
		return new IPath[0];
	}
}

/* (non-Javadoc)
 * @see IJavaSearchScope#includesBinaries()
 */
public boolean includesBinaries() {
	return this.includesBinaries;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#includesClasspaths()
 */
public boolean includesClasspaths() {
	return this.includesClasspaths;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#setIncludesBinaries
 */
public void setIncludesBinaries(boolean includesBinaries) {
	this.includesBinaries = includesBinaries;
}

/* (non-Javadoc)
 * @see IJavaSearchScope#setIncludeClasspaths
 */
public void setIncludesClasspaths(boolean includesClasspaths) {
	this.includesClasspaths = includesClasspaths;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9138.java