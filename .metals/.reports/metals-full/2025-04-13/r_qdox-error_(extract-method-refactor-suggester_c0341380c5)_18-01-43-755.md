error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/566.java
text:
```scala
i@@f (!"java"/*nonNLS*/.equalsIgnoreCase(extension) && !"class"/*nonNLS*/.equalsIgnoreCase(extension)) {

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

import java.util.Enumeration;

/**
 * The element info for <code>PackageFragmentRoot</code>s.
 */
class PackageFragmentRootInfo extends OpenableElementInfo {

	/**
	 * The kind of the root associated with this info.
	 * Valid kinds are: <ul>
	 * <li><code>IPackageFragmentRoot.K_SOURCE</code>
	 * <li><code>IPackageFragmentRoot.K_BINARY</code></ul>
	 */
	protected int fRootKind= IPackageFragmentRoot.K_SOURCE;

	/**
	 * A array with all the non-java resources contained by this PackageFragment
	 */
	protected Object[] fNonJavaResources;
/**
 * Create and initialize a new instance of the receiver
 */
public PackageFragmentRootInfo() {
	fNonJavaResources = null;
}
/**
 * Starting at this folder, create non-java resources for this package fragment root 
 * and add them to the non-java resources collection.
 * 
 * @exception JavaModelException  The resource associated with this package fragment does not exist
 */
private Object[] computeFolderNonJavaResources(IJavaProject project, IContainer folder) throws JavaModelException {
	Object[] nonJavaResources = new IResource[5];
	int nonJavaResourcesCounter = 0;
	try {
		IResource[] members = folder.members();
		for (int i = 0, max = members.length; i < max; i++) {
			IResource member = members[i];
			if (member.getType() == IResource.FILE) {
				String extension = member.getProjectRelativePath().getFileExtension();
				if (!"java".equalsIgnoreCase(extension) && !"class".equalsIgnoreCase(extension)) { //$NON-NLS-1$ //$NON-NLS-2$
					if (project.findPackageFragmentRoot(member.getFullPath()) == null) {
						if (nonJavaResources.length == nonJavaResourcesCounter) {
							// resize
							System.arraycopy(nonJavaResources, 0, (nonJavaResources = new IResource[nonJavaResourcesCounter * 2]), 0, nonJavaResourcesCounter);
						}
						nonJavaResources[nonJavaResourcesCounter++] = member;
					}
				}
			}
		}
		if (nonJavaResources.length != nonJavaResourcesCounter) {
			System.arraycopy(nonJavaResources, 0, (nonJavaResources = new IResource[nonJavaResourcesCounter]), 0, nonJavaResourcesCounter);
		}
		return nonJavaResources;
	} catch (CoreException e) {
		throw new JavaModelException(e);
	}
}
/**
 * Compute the non-package resources of this package fragment root.
 * 
 * @exception JavaModelException  The resource associated with this package fragment root does not exist
 */
private Object[] computeNonJavaResources(IJavaProject project, IResource underlyingResource) {
	Object[] nonJavaResources = NO_NON_JAVA_RESOURCES;
	try {
		// the underlying resource may be a folder or a project (in the case that the project folder
		// is actually the package fragment root)
		if (underlyingResource.getType() == IResource.FOLDER || underlyingResource.getType() == IResource.PROJECT) {
			nonJavaResources = computeFolderNonJavaResources(project, (IContainer) underlyingResource);
		}
	} catch (JavaModelException e) {
	}
	return nonJavaResources;
}
/**
 * Returns an array of non-java resources contained in the receiver.
 */
synchronized Object[] getNonJavaResources(IJavaProject project, IResource underlyingResource) {
	Object[] nonJavaResources = fNonJavaResources;
	if (nonJavaResources == null) {
		nonJavaResources = this.computeNonJavaResources(project, underlyingResource);
		fNonJavaResources = nonJavaResources;
	}
	return nonJavaResources;
}
/**
 * Returns the kind of this root.
 */
public int getRootKind() {
	return fRootKind;
}
/**
 * Set the fNonJavaResources to res value
 */
synchronized void setNonJavaResources(Object[] resources) {
	fNonJavaResources = resources;
}
/**
 * Sets the kind of this root.
 */
protected void setRootKind(int newRootKind) {
	fRootKind = newRootKind;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/566.java