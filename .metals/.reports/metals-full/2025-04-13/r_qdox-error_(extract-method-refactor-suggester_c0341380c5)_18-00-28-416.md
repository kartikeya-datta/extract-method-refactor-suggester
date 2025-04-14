error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8156.java
text:
```scala
I@@ClasspathEntry[] classpath = project.getResolvedClasspath(true);

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.compiler.util.ObjectSet;
import org.eclipse.jdt.internal.compiler.util.ObjectVector;

/**
 * This operation sets an <code>IJavaProject</code>'s classpath.
 *
 * @see IJavaProject
 */
public class SetClasspathOperation extends JavaModelOperation {

	IClasspathEntry[] oldExpandedPath;
	IClasspathEntry[] newRawPath;
	boolean canChangeResource;

	/**
	 * When executed, this operation sets the classpath of the given project.
	 */
	public SetClasspathOperation(
		IJavaProject project,
		IClasspathEntry[] oldExpandedPath,
		IClasspathEntry[] newRawPath,
		boolean canChangeResource) {

		super(new IJavaElement[] { project });
		this.oldExpandedPath = oldExpandedPath;
		this.newRawPath = newRawPath;
		this.canChangeResource = canChangeResource;
	}

	/**
	 * Adds deltas for the given roots, with the specified change flag,
	 * and closes the root. Helper method for #setClasspath
	 */
	protected void addDeltas(
		IPackageFragmentRoot[] roots,
		int flag,
		JavaElementDelta delta) {

		for (int i = 0; i < roots.length; i++) {
			IPackageFragmentRoot root = roots[i];
			delta.changed(root, flag);
			if (flag == IJavaElementDelta.F_REMOVED_FROM_CLASSPATH){
				try {
					root.close();
				} catch (JavaModelException e) {
				}
			}
		}
	}

	/**
	 * Returns the index of the item in the list if the given list contains the specified entry. If the list does
	 * not contain the entry, -1 is returned.
	 * A helper method for #setClasspath
	 */
	protected int classpathContains(
		IClasspathEntry[] list,
		IClasspathEntry entry) {

		for (int i = 0; i < list.length; i++) {
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

		beginTask(Util.bind("classpath.settingProgress"), 2); //$NON-NLS-1$
		JavaProject project = ((JavaProject) getElementsToProcess()[0]);

		String[] oldRequired = project.getRequiredProjectNames();
		
		project.setRawClasspath0(this.newRawPath);

		// change builder specs to build in the order given by the new classpath
		JavaModelManager manager = project.getJavaModelManager();
		manager.setBuildOrder(
			((JavaModel) project.getJavaModel()).computeBuildOrder(true));

		// flush markers
		project.flushClasspathProblemMarkers();

		// resolve new path (asking for marker creation if problems)
		IClasspathEntry[] newExpandedPath = project.getExpandedClasspath(true, true);

		if (this.oldExpandedPath != null) {
			generateClasspathChangeDeltas(
				this.oldExpandedPath,
				newExpandedPath,
				manager,
				project);
		} else {
			project.saveClasspath(this.canChangeResource);
			updateAffectedProjects(project.getProject().getFullPath());
		}
		updateProjectReferences(oldRequired, project.getRequiredProjectNames());

		
		done();
	}

	/**
	 * Generates the delta of removed/added/reordered roots.
	 * Use three deltas in case the same root is removed/added/reordered (i.e. changed from
	 * K_SOURCE to K_BINARY or visa versa)
	 */
	protected void generateClasspathChangeDeltas(
		IClasspathEntry[] oldResolvedPath,
		IClasspathEntry[] newResolvedPath,
		JavaModelManager manager,
		JavaProject project) {

		boolean hasChangedSourceEntries = false;

		JavaElementDelta delta = new JavaElementDelta(getJavaModel());
		boolean hasDelta = false;
		boolean oldResolvedPathLongest =
			oldResolvedPath.length >= newResolvedPath.length;

		for (int i = 0; i < oldResolvedPath.length; i++) {

			int index = classpathContains(newResolvedPath, oldResolvedPath[i]);
			if (index == -1) {
				IPackageFragmentRoot[] pkgFragmentRoots =
					project.getPackageFragmentRoots(oldResolvedPath[i]);
				addDeltas(pkgFragmentRoots, IJavaElementDelta.F_REMOVED_FROM_CLASSPATH, delta);

				int changeKind = oldResolvedPath[i].getEntryKind();
				hasChangedSourceEntries |= changeKind == IClasspathEntry.CPE_SOURCE;

				// force detach source on jar package fragment roots (source will be lazily computed when needed)
				for (int j = 0, length = pkgFragmentRoots.length; j < length; j++) {
					IPackageFragmentRoot root = pkgFragmentRoots[j];
					if (root instanceof JarPackageFragmentRoot) {
						((JarPackageFragmentRoot) root).setSourceAttachmentProperty(null);// loose info - will be recomputed
					}
				}
				hasDelta = true;

			} else if (
				oldResolvedPathLongest && index != i) { //reordering of the classpath
				addDeltas(
					project.getPackageFragmentRoots(oldResolvedPath[i]),
					IJavaElementDelta.F_CLASSPATH_REORDER,
					delta);
				int changeKind = oldResolvedPath[i].getEntryKind();
				hasChangedSourceEntries |= changeKind == IClasspathEntry.CPE_SOURCE;

				hasDelta = true;
			}
		}

		for (int i = 0; i < newResolvedPath.length; i++) {

			int index = classpathContains(oldResolvedPath, newResolvedPath[i]);
			if (index == -1) {
				addDeltas(
					project.getPackageFragmentRoots(newResolvedPath[i]),
					IJavaElementDelta.F_ADDED_TO_CLASSPATH,
					delta);
				int changeKind = newResolvedPath[i].getEntryKind();
				hasChangedSourceEntries |= changeKind == IClasspathEntry.CPE_SOURCE;
				hasDelta = true;

			} else if (
				!oldResolvedPathLongest && index != i) { //reordering of the classpath
				addDeltas(
					project.getPackageFragmentRoots(newResolvedPath[i]),
					IJavaElementDelta.F_CLASSPATH_REORDER,
					delta);
				int changeKind = newResolvedPath[i].getEntryKind();
				hasChangedSourceEntries |= changeKind == IClasspathEntry.CPE_SOURCE;
				hasDelta = true;
			}
		}
		if (hasDelta) {
			try {
				project.saveClasspath(this.canChangeResource);
			} catch (JavaModelException e) {
			}
			this.addDelta(delta);
			// loose all built state - next build will be a full one
			manager.setLastBuiltState(project.getProject(), null);

			if (hasChangedSourceEntries)
				updateAffectedProjects(project.getProject().getFullPath());
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
		IJavaProject javaProject = (IJavaProject) getElementToProcess();

		// retrieve output location
		IPath outputLocation;
		try {
			outputLocation = javaProject.getOutputLocation();
		} catch (JavaModelException e) {
			return e.getJavaModelStatus();
		}

		return JavaConventions.validateClasspath(
			javaProject,
			this.newRawPath,
			outputLocation);
	}

	/**
	 * Update projects which are affected by this classpath change:
	 * those which refers to the current project as source
	 */
	protected void updateAffectedProjects(IPath prerequisiteProjectPath) {

		try {
			IJavaModel model = JavaModelManager.getJavaModelManager().getJavaModel();
			IJavaProject[] projects = model.getJavaProjects();
			for (int i = 0, projectCount = projects.length; i < projectCount; i++) {
				try {
					JavaProject project = (JavaProject) projects[i];
					IClasspathEntry[] classpath = project.getRawClasspath();
					for (int j = 0, entryCount = classpath.length; j < entryCount; j++) {
						IClasspathEntry entry = classpath[j];
						if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT
							&& entry.getPath().equals(prerequisiteProjectPath)) {
							project.updateClassPath(this.fMonitor, this.canChangeResource);
							break;
						}
					}
				} catch (JavaModelException e) {
				}
			}
		} catch (JavaModelException e) {
		}
	}

	/**
	 * Update projects references so that the build order is consistent with the classpath
	 */
	protected void updateProjectReferences(String[] oldRequired, String[] newRequired) {

		try {		
			if (!this.canChangeResource) return;

			JavaProject jproject = ((JavaProject) getElementsToProcess()[0]);
			IProject project = jproject.getProject();
			IProjectDescription description = project.getDescription();
			 
			IProject[] projectReferences = description.getReferencedProjects();
			ObjectSet updatedReferences = new ObjectSet(projectReferences.length);
			updatedReferences.addAll(projectReferences);

			ObjectSet removed = new ObjectSet(oldRequired.length);
			for (int i = 0; i < oldRequired.length; i++){
				String projectName = oldRequired[i];
				removed.add(projectName);
			}
			ObjectSet added = new ObjectSet(newRequired.length);
			for (int i = 0; i < newRequired.length; i++){
				String projectName = newRequired[i];
				if (!removed.remove(projectName)){
					added.add(projectName);
				}
			}
			if (!added.isEmpty() || !removed.isEmpty()){
				Enumeration enum = added.elements();
				while (enum.hasMoreElements()){
					String name = (String)enum.nextElement();
					updatedReferences.add(project.getWorkspace().getRoot().getProject(name));
				}
				enum = removed.elements();
				while (enum.hasMoreElements()){
					String name = (String)enum.nextElement();
					updatedReferences.remove(project.getWorkspace().getRoot().getProject(name));
				}

				IProject[] requiredProjectArray = new IProject[updatedReferences.size()];
				updatedReferences.copyInto(requiredProjectArray);
				description.setReferencedProjects(requiredProjectArray);
				project.setDescription(description, this.fMonitor);
			}
		} catch(CoreException e){
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8156.java