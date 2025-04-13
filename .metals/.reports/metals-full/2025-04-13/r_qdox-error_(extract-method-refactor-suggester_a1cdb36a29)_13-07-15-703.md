error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9509.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9509.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9509.java
text:
```scala
b@@eginTask(Util.bind("classpath.settingProgress"), 2); //$NON-NLS-1$

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;

/**
 * This operation sets an <code>IJavaProject</code>'s classpath.
 *
 * @see IJavaProject
 */
public class SetClasspathOperation extends JavaModelOperation {
	IClasspathEntry[] oldResolvedPath;	
	IClasspathEntry[] newRawPath;
	boolean saveClasspath;
/**
 * When executed, this operation sets the classpath of the given project.
 */
public SetClasspathOperation(IJavaProject project, IClasspathEntry[] oldResolvedPath, IClasspathEntry[] newRawPath, boolean saveClasspath) {
	super(new IJavaElement[] {project});
	this.oldResolvedPath = oldResolvedPath;
	this.newRawPath = newRawPath;
	this.saveClasspath = saveClasspath;
}
	/**
	 * Adds deltas for the given roots, with the specified change flag,
	 * and closes the root. Helper method for #setClasspath
	 */
	protected void addDeltas(IPackageFragmentRoot[] roots, int flag, JavaElementDelta delta) {
		for (int i= 0; i < roots.length; i++) {
			IPackageFragmentRoot root= roots[i];
			delta.changed(root, flag);
			try {
				root.close();
			} catch (JavaModelException e) {
			}
		}
	}
	/**
	 * Returns the index of the item in the list if the given list contains the specified entry. If the list does
	 * not contain the entry, -1 is returned.
	 * A helper method for #setClasspath
	 */
	protected int classpathContains(IClasspathEntry[] list, IClasspathEntry entry) {
		for (int i= 0; i < list.length; i++) {
			if (list[i].equals(entry)) {
				return i;
			}
		}
		return -1;
	}
/**
 * Sets the classpath of the pre-specified project.
 */
protected void executeOperation() throws JavaModelException {
	beginTask(Util.bind("classpath.settingProgress"/*nonNLS*/), 2);
	JavaProject project= ((JavaProject) getElementsToProcess()[0]);
	
	project.setRawClasspath0(this.newRawPath);

	// change builder specs to build in the order given by the new classpath
	JavaModelManager manager = project.getJavaModelManager();
	manager.setBuildOrder(((JavaModel) project.getJavaModel()).computeBuildOrder(true));

	// flush markers
	project.flushClasspathProblemMarkers();

	// resolve new path (asking for marker creation if problems)
	IClasspathEntry[] newResolvedPath = project.getResolvedClasspath(true, true);
	
	if (this.oldResolvedPath != null) {
		generateClasspathChangeDeltas(this.oldResolvedPath, newResolvedPath, manager, project);
	} else {
		project.saveClasspath(this.saveClasspath);		
	}

	done();
}
	/**
	 * Generates the delta of removed/added/reordered roots.
	 * Use three deltas in case the same root is removed/added/reordered (i.e. changed from
	 * K_SOURCE to K_BINARY or visa versa)
	 */
	protected void generateClasspathChangeDeltas(IClasspathEntry[] oldResolvedPath, IClasspathEntry[] newResolvedPath, JavaModelManager manager, JavaProject project) {

		boolean hasChangedSourceEntries = false;
		
		JavaElementDelta delta= new JavaElementDelta(getJavaModel());
		boolean hasDelta = false;
		boolean oldResolvedPathLongest= oldResolvedPath.length >= newResolvedPath.length;
		for (int i= 0; i < oldResolvedPath.length; i++) {
			int index= classpathContains(newResolvedPath, oldResolvedPath[i]);
			if (index == -1) {
				IPackageFragmentRoot[] pkgFragmentRoots = project.getPackageFragmentRoots(oldResolvedPath[i]);
				addDeltas(pkgFragmentRoots, IJavaElementDelta.F_REMOVED_FROM_CLASSPATH, delta);
				hasChangedSourceEntries |= oldResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE;
				// force detach source on jar package fragment roots (source will be lazily computed when needed)
				for (int j = 0, length = pkgFragmentRoots.length; j < length; j++) {
					IPackageFragmentRoot root = pkgFragmentRoots[j];
					if (root instanceof JarPackageFragmentRoot) {
						JarPackageFragmentRoot jarRoot = (JarPackageFragmentRoot)root;
						try {
							jarRoot.getWorkspace().getRoot().setPersistentProperty(jarRoot.getSourceAttachmentPropertyName(), null); // loose info - will be recomputed
						} catch(CoreException ce){
						}
					}
				}
				
				hasDelta = true;
			} else if (oldResolvedPathLongest && index != i) { //reordering of the classpath
				addDeltas(project.getPackageFragmentRoots(oldResolvedPath[i]), IJavaElementDelta.F_CLASSPATH_REORDER, delta);
				hasChangedSourceEntries |= oldResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE;
				hasDelta = true;
			}
		}

		for (int i= 0; i < newResolvedPath.length; i++) {
			int index= classpathContains(oldResolvedPath, newResolvedPath[i]);
			if (index == -1) {
				addDeltas(project.getPackageFragmentRoots(newResolvedPath[i]), IJavaElementDelta.F_ADDED_TO_CLASSPATH, delta);
				hasChangedSourceEntries |= newResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE;
				hasDelta = true;
			} else if (!oldResolvedPathLongest && index != i) { //reordering of the classpath
				addDeltas(project.getPackageFragmentRoots(newResolvedPath[i]), IJavaElementDelta.F_CLASSPATH_REORDER, delta);
				hasChangedSourceEntries |= newResolvedPath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE;				
				hasDelta = true;
			}
		}
		if (hasDelta) {
			try {
				project.saveClasspath(this.saveClasspath);
			} catch(JavaModelException e){
			}
			this.addDelta(delta);
			// loose all built state - next build will be a full one
			manager.setLastBuiltState(project.getProject(), null);

			if (hasChangedSourceEntries) updateAffectedProjects(project.getProject().getFullPath());
		}
	}
/**
 * Possible failures: <ul>
 *  <li>NO_ELEMENTS_TO_PROCESS - the project supplied to the operation is
 * 		<code>null</code>.
 *	<li>NULL_PATH - the output location path supplied to the operation
 * 		is <code>null</code>.
 *	<li>PATH_OUTSIDE_PROJECT - the output location path supplied to the operation
 * 		is outside of the project supplied to this operation.
 *	<li>DEVICE_PATH - the path supplied to this operation must not specify a 
 * 		device
 *	<li>RELATIVE_PATH - the path supplied to this operation must be
 *		an absolute path
 *	<li>INVALID_PATH - the output location cannot overlap any package fragment
 *		root, except the project folder.
 *  <li>ELEMENT_DOES_NOT_EXIST - the Java project does not exist
 * </ul>
 */
public IJavaModelStatus verify() {
	IJavaModelStatus status = super.verify();
	if (!status.isOK()) {
		return status;
	}
	IJavaProject javaProject = (IJavaProject)getElementToProcess();

	// retrieve output location
	IPath outputLocation;
	try {
		outputLocation = javaProject.getOutputLocation();
	} catch(JavaModelException e){
		return e.getJavaModelStatus();
	}

	return JavaConventions.validateClasspath(javaProject, this.newRawPath, outputLocation);
}

/**
 * Update projects which are affected by this classpath change:
 * those which refers to the current project as source
 */
protected void updateAffectedProjects(IPath prerequisiteProjectPath){

	try {
		IJavaModel model = JavaModelManager.getJavaModelManager().getJavaModel();
		IJavaProject[] projects = model.getJavaProjects();
		for (int i = 0, projectCount = projects.length; i < projectCount; i++){
			try {
				JavaProject project = (JavaProject)projects[i];
				IClasspathEntry[] classpath = project.getRawClasspath();
				for (int j =0, entryCount = classpath.length; j < entryCount; j++){
					IClasspathEntry entry = classpath[j];
					if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT 
						&& entry.getPath().equals(prerequisiteProjectPath)){
							project.updateClassPath();
							break;
						}
				}
			} catch(JavaModelException e){
			}
		}
	} catch(JavaModelException e){
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9509.java