error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8734.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8734.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8734.java
text:
```scala
I@@ClasspathEntry[] entries = this.project.getResolvedClasspath(true);

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.Util;

public class ProjectResourceCopier implements IResourceVisitor {

	/* internal state */
	private final IJavaProject project;
	private final JavaDevelopmentContextImpl devContext;
	private final IProject projectRsc;
	private final IWorkspace workspace;
	private final IWorkspaceRoot root;
	private final IPath outputLocation;
	private final BuildNotifier notifier;
	private final float totalAvailableProgress;
	private IResource[] sourceFolders;
public ProjectResourceCopier(IJavaProject project, JavaDevelopmentContextImpl devContext, BuildNotifier notifier, float totalAvailableProgress){
	
	this.project = project;
	this.devContext = devContext;
	this.projectRsc = this.project.getProject();
	this.workspace = this.projectRsc.getWorkspace();
	this.root = this.workspace.getRoot();
	try {
		this.outputLocation = this.project.getOutputLocation();
		IClasspathEntry[] entries = this.project.getExpandedClasspath(true);
		this.sourceFolders = new IResource[entries.length];
		for (int i = 0, length = entries.length; i < length; i++) {
			IClasspathEntry entry = entries[i];
			if ((entry.getEntryKind() == IClasspathEntry.CPE_SOURCE)) {
				this.sourceFolders[i] = this.root.findMember(entry.getPath());
			}
		}
	} catch(JavaModelException e){
		throw new ImageBuilderInternalException(e);
	}
	this.notifier = notifier;
	this.totalAvailableProgress = totalAvailableProgress;
}
public void copyAllResourcesOnClasspath(){

	boolean hasNotified = false;
	try {
		for (int i = 0, length = this.sourceFolders.length; i < length; i++) {
			if (sourceFolders[i] != null){
				if (this.outputLocation.equals(this.sourceFolders[i].getFullPath())) continue; // do nothing if output is same as source folder
				if (!hasNotified){
					hasNotified = true;
					if (notifier != null) notifier.subTask(Util.bind("build.copyingResources")); //$NON-NLS-1$
				}
				this.sourceFolders[i].accept(this);
			}
			if (notifier != null) notifier.updateProgressDelta(totalAvailableProgress / length);	
		}
	} catch(CoreException e){
		//throw this.devContext.internalException(e);
	}
}
/**
 * Copy a given resource into the output folder (if non java source file)
 */
private boolean copyToOutput(IResource resource) {

	if (!resource.exists()) return false;
	IPath sourceFolderPath = getSourceFolderPath(resource);
	IPath resourcePath = resource.getFullPath();

	switch (resource.getType()){
		case IResource.FILE :
			if (sourceFolderPath == null) return false; // resource is not inside the classpath		
			if (!"java".equals(resource.getFileExtension())){ // ignore source files //$NON-NLS-1$
				
				IFile currentFile = (IFile) resource;
				IPath pathSuffix = resourcePath.removeFirstSegments(sourceFolderPath.segmentCount());
				IPath targetPath = this.outputLocation.append(pathSuffix);
				try {
					IFile previousFile = this.root.getFile(targetPath);
					if (previousFile.exists()) previousFile.delete(true, false, null);
					currentFile.copy(targetPath, true, null);
				} catch(CoreException e){
					//throw this.devContext.internalException(e);
				}
			}
			break;
		case IResource.PROJECT :
			if (resourcePath.equals(this.outputLocation)) return false; // do not visit the binary output
			if (resourcePath.equals(sourceFolderPath)) return true; // skip source folder itself
			break;
		case IResource.FOLDER :
			if (resourcePath.equals(this.outputLocation)) return false; // do not visit the binary output
			if (sourceFolderPath == null) return true; // continue inside folder (source folder might be one of its children)		
			if (resourcePath.equals(sourceFolderPath)) return true; // skip source folder itself
			IContainer currentFolder = (IFolder) resource;
				
			IPath pathSuffix = resourcePath.removeFirstSegments(sourceFolderPath.segmentCount());
			IPath targetPath = this.outputLocation.append(pathSuffix);
			IFolder targetFolder = this.root.getFolder(targetPath);
			if (!targetFolder.exists()){
				try {
					targetFolder.create(true, true, null);
				} catch(CoreException e){
					//throw this.devContext.internalException(e);
				}
			}
			break;
	}
	return true;
}
/**
 * Delete the corresponding resource from the output folder
 */
private boolean deleteResourceCopyFromOutput(IResource resource) {

	IPath sourceFolderPath = getSourceFolderPath(resource);
	if (sourceFolderPath == null) return false; // resource is not inside the classpath

	IPath resourcePath = resource.getFullPath();
	if (resourcePath.equals(sourceFolderPath)) return true; // skip source folder itself
	if (resourcePath.equals(this.outputLocation)) return false; // do not visit the binary output	
	IPath pathSuffix, targetPath;
	
	switch (resource.getType()){
		case IResource.FILE :
			if (!"java".equals(resource.getFileExtension())){ // ignore source files //$NON-NLS-1$
				
				IFile currentFile = (IFile) resource;
				pathSuffix = resourcePath.removeFirstSegments(sourceFolderPath.segmentCount());
				targetPath = this.outputLocation.append(pathSuffix);
				try {
					IFile previousFile = this.root.getFile(targetPath);
					if (previousFile.exists()) previousFile.delete(true, true, null);
				} catch(CoreException e){
				}
			}
			break;
		case IResource.FOLDER :
			IContainer currentFolder = (IFolder) resource;
			pathSuffix = resourcePath.removeFirstSegments(sourceFolderPath.segmentCount());
			targetPath = this.outputLocation.append(pathSuffix);
			IFolder targetFolder = this.root.getFolder(targetPath);
			if (targetFolder.exists()){
				try {
					targetFolder.delete(true, false, null);
				} catch(CoreException e){
				}
			}
			break;
	}
	return false;
}
/**
 * Answer the path of the classpath source folder entry enclosing a given resource (if the resource is on the classpath)
 */
private IPath getSourceFolderPath(IResource resource) {

	IPath resourcePath = resource.getFullPath();
	for (int i = 0, length = this.sourceFolders.length; i < length; i++){
		if (this.sourceFolders[i] != null){
			IPath sourceFolderPath = this.sourceFolders[i].getFullPath();
			if (sourceFolderPath.isPrefixOf(resourcePath)){
				return sourceFolderPath;
			}
		}
	}
	return null;
}
/**
 * Traverse an existing delta and update the affected resources in the binary output
 */
 public void updateAffectedResources(IResourceDelta delta){

	// check that there is anything to do (if any source folder is not coincidating with the binary output)
	boolean hasNotified = false;
	for (int i = 0, length = this.sourceFolders.length; i < length; i++) {
		if (sourceFolders[i] != null){
			if (this.outputLocation.equals(this.sourceFolders[i].getFullPath())) continue; // do nothing if output is same as source folder
			if (!hasNotified){
				hasNotified = true;
				if (notifier != null) notifier.subTask(Util.bind("build.updatingResources")); //$NON-NLS-1$
			}
		}
	}
	if (hasNotified) 
		updateAffectedResources0(delta);
}
/**
 * Traverse an existing delta and update the affected resources in the binary output
 */
 private void updateAffectedResources0(IResourceDelta delta){

	IResource affectedResource = delta.getResource();
	boolean processChildren = true;
	switch (delta.getKind()) {
		case IResourceDelta.CHANGED:
			if ((delta.getFlags() & IResourceDelta.CONTENT) == 0) break; // only consider content change
		case IResourceDelta.ADDED:
			processChildren = copyToOutput(affectedResource);
			break;
		case IResourceDelta.REMOVED:
			processChildren = deleteResourceCopyFromOutput(affectedResource);
	}
	if (processChildren){
		IResourceDelta[] children = delta.getAffectedChildren();
		for (int i = 0; i < children.length; i++) {
			updateAffectedResources0(children[i]);
		}
	}
}
/** Visits the given resource.
 *
 * @param resource the resource to visit
 * @return <code>true</code> if the resource's members should
 * be visited; <code>false</code> if they should be skipped
 * @exception CoreException if the visit fails for some reason.
 */
public boolean visit(IResource resource) throws CoreException {

	return copyToOutput(resource); // binary output should be empty, just copy current resources
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8734.java