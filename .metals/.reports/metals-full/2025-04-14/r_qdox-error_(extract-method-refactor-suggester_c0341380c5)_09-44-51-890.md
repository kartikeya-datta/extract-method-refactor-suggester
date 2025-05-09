error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3162.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3162.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3162.java
text:
```scala
e@@ntries = projectElement.getResolvedClasspath(true);

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.Assert;
import org.eclipse.jdt.internal.core.builder.IState;
import org.eclipse.jdt.internal.core.builder.IType;
import org.eclipse.jdt.internal.core.Util;

import java.io.*;
import java.util.*;

/**
 * A <ProjectBinaryOutput> is a <BinaryOutput> that stores the
 * binaries in a folder of an <IProject>.
 */
public class ProjectBinaryOutput extends BinaryOutput {
	private IProject project;
	private IPath outputPath;
	private JavaDevelopmentContextImpl dc;

/**
 * Creates a new ProjectBinaryOutput for the given project and output path
 * in this project.
 */
public ProjectBinaryOutput(IProject project, IPath outputPath, JavaDevelopmentContextImpl dc) {
	this.project = project;
	this.outputPath = outputPath;
	this.dc = dc;

	/* create the output folder is it doesn't exist */
	if (!project.getFullPath().equals(outputPath)) {
		this.makeContainersIfNecessary(outputPath);
	}
}
/**
 * @see BinaryOutput
 */
protected void basicPutBinary(TypeStructureEntry tsEntry, byte[] binary, int crc) {

	IType type;
	IPath path = getPathForBinary(type = tsEntry.getType());
	deleteBinary(type);
	
	IContainer container = makeContainersIfNecessary(path.removeLastSegments(1));

	PackageElement element = new PackageElement(type.getPackage(), new SourceEntry(path, null, null));
	IFile file = container.getFile(new Path(path.lastSegment()));
	
	try {
		ByteArrayInputStream stream = new ByteArrayInputStream(binary);
		file.create(stream, true, null);
	} catch (CoreException e) {
		throw this.dc.internalException(e);
	}
}
/**
 * Deletes everything in the given container.
 */
private void deleteAllInContainer(IContainer container) {
	try {
		if (!container.exists())
			return;
		IResource[] members = container.members(); 
		for (int i = 0, max = members.length; i < max;i++) {
			IResource resource = (IResource) members[i];
			resource.delete(true, null);
		}
	} catch (CoreException e) {
	}
}
/**
 * @see BinaryOutput
 */
public void deleteBinary(IType type) {
	IPath path = getPathForBinary(type);
	IFile file = getFile(path);
	try {
		file.delete(true, null);
	} catch (CoreException e) {
	}
}
/**
 * Deletes the classes in the given container, recursively.
 * Delete any folders which become empty.
 */
private void deleteClassesInContainer(IContainer container) {
	try {
		if (!container.exists())
			return;
		IResource[] members = container.members(); 
		for (int i = 0, max = members.length; i < max;i++) {
			IResource resource = (IResource) members[i];
			switch (resource.getType()) {
				case IResource.FILE :
					if (resource.getName().toLowerCase().endsWith(".class")) { //$NON-NLS-1$
						resource.delete(true, null);
					}
					break;
				case IResource.PROJECT :
				case IResource.FOLDER :
					deleteClassesInContainer((IContainer) resource);
					break;
			}
		}
//
//		Don't delete empty folders, since the output may overlap with the source, and
//		we don't want to delete empty folders which the user may have created.
//
//		if (container.getType() == IResource.FOLDER && !container.members().hasMoreElements()) {
//			container.delete(true, null);
//		}
	} catch (CoreException e) {
	}
}
/**
 * @see BinaryOutput
 */
public void garbageCollect(IState[] statesInUse) {
	// Nothing to do for a Project binary output
}
/**
 * @see BinaryOutput
 */
public byte[] getBinary(TypeStructureEntry tsEntry, IType type) {
	IPath path = getPathForBinary(type);
	IFile file = getFile(path);
	try {
		return Util.getResourceContentsAsByteArray(file);
	} catch (CoreException e) {
		return new byte[0];
	}
}
/**
 * Returns the container for a path.
 */
private IContainer getContainer(IPath path) {
	if (path.isAbsolute()){
		if (this.project.getFullPath().equals(path)){
			return this.project;
		} else { 
			return this.project.getWorkspace().getRoot().getFolder(path);
		}
	} 
	return this.project.getFolder(path);
}
/**
 * Returns the file for a path.
 */
private IFile getFile(IPath path) {
	if (path.isAbsolute()){
			return this.project.getWorkspace().getRoot().getFile(path);
	} 
	return this.project.getFile(path);
}
/**
 * Returns the path for the output package fragment root.
 */
IPath getOutputPath() {
	return this.outputPath;
}
/**
 * Returns the path in the output folder for the given type.
 */
private IPath getPathForBinary(IType type) {
	return getOutputPath().append(type.getName().replace('.', '/') + ".class"); //$NON-NLS-1$
}
/**
 * Returns the container at the given path, creating it and any parent folders if necessary.
 */
IContainer makeContainersIfNecessary(IPath path) {
	try {
		IContainer container = getContainer(path);
		if (container.exists())
			return container;
		Assert.isTrue(container instanceof IFolder);
		makeContainersIfNecessary(path.removeLastSegments(1));
		((IFolder) container).create(true, true, null);
		return container;
	} catch (CoreException e) {
		throw this.dc.internalException(e);
	}
}
/**
 * @see BinaryOutput
 */
public void scrubOutput() {

	IJavaProject projectElement = JavaCore.create(this.project);
	IClasspathEntry[] entries;
	try {
		entries = projectElement.getExpandedClasspath(true);
	} catch(JavaModelException e){
		throw this.dc.internalException(e);
	}

	/* detect whether the binary ouput coincidates with source folder */
	boolean flushAllOutput = true;
	boolean hasSource = false;
	for (int i = 0, length = entries.length; i < length; i++) {
		IClasspathEntry entry = entries[i];
		if ((entry.getEntryKind() == IClasspathEntry.CPE_SOURCE)) {
			hasSource = true;
			if (this.outputPath.equals(entry.getPath())){
				flushAllOutput = false; // output coincidates, cannot flush
				break;
			}
		}
	}
	if (hasSource) {
		if (flushAllOutput){
			deleteAllInContainer(getContainer(this.outputPath));
		} else {
			deleteClassesInContainer(getContainer(this.outputPath));
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3162.java