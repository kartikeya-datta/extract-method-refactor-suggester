error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/334.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/334.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/334.java
text:
```scala
r@@esources = computeNonJavaResources(underlyingResource, handle);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.util.Util;

/**
 * The element info for <code>PackageFragmentRoot</code>s.
 */
class PackageFragmentRootInfo extends OpenableElementInfo {

	/**
	 * The SourceMapper for this JAR (or <code>null</code> if
	 * this JAR does not have source attached).
	 */
	protected SourceMapper sourceMapper = null;

	/**
	 * The kind of the root associated with this info.
	 * Valid kinds are: <ul>
	 * <li><code>IPackageFragmentRoot.K_SOURCE</code>
	 * <li><code>IPackageFragmentRoot.K_BINARY</code></ul>
	 */
	protected int rootKind= IPackageFragmentRoot.K_SOURCE;

	/**
	 * A array with all the non-java resources contained by this PackageFragment
	 */
	protected Object[] nonJavaResources;
/**
 * Create and initialize a new instance of the receiver
 */
public PackageFragmentRootInfo() {
	this.nonJavaResources = null;
}
/**
 * Starting at this folder, create non-java resources for this package fragment root 
 * and add them to the non-java resources collection.
 * 
 * @exception JavaModelException  The resource associated with this package fragment does not exist
 */
static Object[] computeFolderNonJavaResources(IPackageFragmentRoot root, IContainer folder, char[][] inclusionPatterns, char[][] exclusionPatterns) throws JavaModelException {
	IResource[] nonJavaResources = new IResource[5];
	int nonJavaResourcesCounter = 0;
	JavaProject project = (JavaProject) root.getJavaProject();
	try {
		IClasspathEntry[] classpath = project.getResolvedClasspath();
		IResource[] members = folder.members();
		int length = members.length;
		if (length > 0) {
			String sourceLevel = project.getOption(JavaCore.COMPILER_SOURCE, true);
			String complianceLevel = project.getOption(JavaCore.COMPILER_COMPLIANCE, true);
			nextResource: for (int i = 0; i < length; i++) {
				IResource member = members[i];
				switch (member.getType()) {
					case IResource.FILE :
						String fileName = member.getName();
					
						// ignore .java files that are not excluded
						if (Util.isValidCompilationUnitName(fileName, sourceLevel, complianceLevel) && !Util.isExcluded(member, inclusionPatterns, exclusionPatterns)) 
							continue nextResource;
						// ignore .class files
						if (Util.isValidClassFileName(fileName, sourceLevel, complianceLevel)) 
							continue nextResource;
						// ignore .zip or .jar file on classpath
						if (isClasspathEntry(member.getFullPath(), classpath)) 
							continue nextResource;
						break;

					case IResource.FOLDER :
						// ignore valid packages or excluded folders that correspond to a nested pkg fragment root
						if (Util.isValidFolderNameForPackage(member.getName(), sourceLevel, complianceLevel)
								&& (!Util.isExcluded(member, inclusionPatterns, exclusionPatterns) 
 isClasspathEntry(member.getFullPath(), classpath)))
							continue nextResource;
						break;
				}
				if (nonJavaResources.length == nonJavaResourcesCounter) {
					// resize
					System.arraycopy(nonJavaResources, 0, (nonJavaResources = new IResource[nonJavaResourcesCounter * 2]), 0, nonJavaResourcesCounter);
				}
				nonJavaResources[nonJavaResourcesCounter++] = member;
			}	
		}
		if (ExternalFoldersManager.isInternalPathForExternalFolder(folder.getFullPath())) {
			IJarEntryResource[] jarEntryResources = new IJarEntryResource[nonJavaResourcesCounter];
			for (int i = 0; i < nonJavaResourcesCounter; i++) {
				jarEntryResources[i] = new NonJavaResource(root, nonJavaResources[i]);
			}
			return jarEntryResources;
		} else if (nonJavaResources.length != nonJavaResourcesCounter) {
			System.arraycopy(nonJavaResources, 0, (nonJavaResources = new IResource[nonJavaResourcesCounter]), 0, nonJavaResourcesCounter);
		}
		return nonJavaResources;
	} catch (CoreException e) {
		throw new JavaModelException(e);
	}
}
/**
 * Compute the non-package resources of this package fragment root.
 */
private Object[] computeNonJavaResources(IResource underlyingResource, PackageFragmentRoot handle) {
	Object[] resources = NO_NON_JAVA_RESOURCES;
	try {
		// the underlying resource may be a folder or a project (in the case that the project folder
		// is actually the package fragment root)
		if (underlyingResource.getType() == IResource.FOLDER || underlyingResource.getType() == IResource.PROJECT) {
			resources = 
				computeFolderNonJavaResources(
					handle, 
					(IContainer) underlyingResource,  
					handle.fullInclusionPatternChars(),
					handle.fullExclusionPatternChars());
		}
	} catch (JavaModelException e) {
		// ignore
	}
	return resources;
}
/**
 * Returns an array of non-java resources contained in the receiver.
 */
synchronized Object[] getNonJavaResources(IJavaProject project, IResource underlyingResource, PackageFragmentRoot handle) {
	Object[] resources = this.nonJavaResources;
	if (resources == null) {
		resources = this.computeNonJavaResources(underlyingResource, handle);
		this.nonJavaResources = resources;
	}
	return resources;
}
/**
 * Returns the kind of this root.
 */
public int getRootKind() {
	return this.rootKind;
}
/**
 * Retuns the SourceMapper for this root, or <code>null</code>
 * if this root does not have attached source.
 */
protected SourceMapper getSourceMapper() {
	return this.sourceMapper;
}
private static boolean isClasspathEntry(IPath path, IClasspathEntry[] resolvedClasspath) {
	for (int i = 0, length = resolvedClasspath.length; i < length; i++) {
		IClasspathEntry entry = resolvedClasspath[i];
		if (entry.getPath().equals(path)) {
			return true;
		}
	}
	return false;
}
/**
 * Set the fNonJavaResources to res value
 */
void setNonJavaResources(Object[] resources) {
	this.nonJavaResources = resources;
}
/**
 * Sets the kind of this root.
 */
protected void setRootKind(int newRootKind) {
	this.rootKind = newRootKind;
}
/**
 * Sets the SourceMapper for this root.
 */
protected void setSourceMapper(SourceMapper mapper) {
	this.sourceMapper= mapper;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/334.java