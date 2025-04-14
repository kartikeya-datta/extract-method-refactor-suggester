error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4301.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4301.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4301.java
text:
```scala
c@@lasspath = ((JavaProject)javaProject).getExpandedClasspath(true);

package org.eclipse.jdt.internal.core;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.core.*;

import java.io.File;
import java.util.*;

/**
 * This operation sets an <code>IJavaProject</code>'s output location.
 *
 * @see IJavaProject
 */
public class SetOutputLocationOperation extends JavaModelOperation {
	/**
	 * The new output location for the Java project
	 */
	protected IPath fOutputLocation;
/**
 * When executed, this operation sets the output location of the given project.
 * The output location is where the builder writes <code>.class</code> files.
 */
public SetOutputLocationOperation(IJavaProject project, IPath outputLocation) {
	super(new IJavaElement[] {project});
	fOutputLocation = outputLocation;
}
/**
 * Recursively adds all subfolders of <code>folder</code> to the given collection.
 */
protected void collectAllSubfolders(IFolder folder, Vector collection) throws JavaModelException {
	try {
		IResource[] members= folder.members();
		for (int i = 0, max = members.length; i < max; i++) {
			IResource r= members[i];
			if (r.getType() == IResource.FOLDER) {
				collection.addElement(r);
				collectAllSubfolders((IFolder)r, collection);
			}
		}	
	} catch (CoreException e) {
		throw new JavaModelException(e);
	}
}
/**
 * Returns a collection of package fragments that have been added/removed
 * as the result of changing the output location to/from the given
 * location. The collection is empty if no package fragments are
 * affected.
 */
protected Vector determineAffectedPackageFragments(IPath location) throws JavaModelException {
	Vector fragments = new Vector();
	JavaProject project = ((JavaProject) getElementsToProcess()[0]);

	// see if this will cause any package fragments to be affected
	IWorkspace workspace = getWorkspace();
	IResource resource = null;
	if (location != null) {
		resource = workspace.getRoot().findMember(location);
	}
	if (resource != null && resource.getType() == IResource.FOLDER) {
		IFolder folder = (IFolder) resource;
		// only changes if it actually existed
		IClasspathEntry[] classpath = project.getExpandedClasspath(true);
		for (int i = 0; i < classpath.length; i++) {
			IClasspathEntry entry = classpath[i];
			IPath path = classpath[i].getPath();
			if (entry.getEntryKind() != IClasspathEntry.CPE_PROJECT && path.isPrefixOf(location) && !path.equals(location)) {
				IPackageFragmentRoot[] roots = project.getPackageFragmentRoots(classpath[i]);
				IPackageFragmentRoot root = roots[0];
				// now the output location becomes a package fragment - along with any subfolders
				Vector folders = new Vector();
				folders.addElement(folder);
				collectAllSubfolders(folder, folders);
				Enumeration elements = folders.elements();
				int segments = path.segmentCount();
				while (elements.hasMoreElements()) {
					IFolder f = (IFolder) elements.nextElement();
					IPath relativePath = f.getFullPath().removeFirstSegments(segments);
					String name = relativePath.toOSString();
					name = name.replace(File.pathSeparatorChar, '.');
					if (name.endsWith(".")) { //$NON-NLS-1$
						name = name.substring(0, name.length() - 1);
					}
					IPackageFragment pkg = root.getPackageFragment(name);
					fragments.addElement(pkg);
				}
			}
		}
	}
	return fragments;
}
/**
 * Sets the output location of the pre-specified project.
 *
 * <p>This can cause changes in package fragments - i.e. if the
 * old and new output location folder could be considered as
 * a package fragment.
 */
protected void executeOperation() throws JavaModelException {
	beginTask(Util.bind("classpath.settingOutputLocationProgress"), 2); //$NON-NLS-1$
	JavaProject project= ((JavaProject) getElementsToProcess()[0]);
	
	IPath oldLocation= project.getOutputLocation();
	IPath newLocation= fOutputLocation;

	// see if this will cause any package fragments to be added
	boolean deltaToFire= false;
	JavaElementDelta delta = newJavaElementDelta();
	Vector added= determineAffectedPackageFragments(oldLocation);
	Enumeration pkgs= added.elements();
	while (pkgs.hasMoreElements()) {
		IPackageFragment frag= (IPackageFragment)pkgs.nextElement();
		((IPackageFragmentRoot)frag.getParent()).close();
		delta.added(frag);
		deltaToFire = true;
	}

	// see if this will cause any package fragments to be removed
	Vector removed= determineAffectedPackageFragments(newLocation);
	pkgs= removed.elements();
	while (pkgs.hasMoreElements()) {
		IPackageFragment frag= (IPackageFragment)pkgs.nextElement();
		((IPackageFragmentRoot)frag.getParent()).close();
		delta.removed(frag);
		deltaToFire = true;
	}
	
	project.setOutputLocation0(fOutputLocation);
	if (deltaToFire) {
		addDelta(delta);	
	}
	worked(1);
	project.saveClasspath();
	worked(1);

	// loose all built state - next build will be a full one
	JavaModelManager.getJavaModelManager().setLastBuiltState(project.getProject(), null);
	done();
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
	// retrieve classpath
	IClasspathEntry[] classpath = null;
	IJavaProject javaProject= (IJavaProject)getElementToProcess();
	IPath projectPath= javaProject.getProject().getFullPath();	
	try {
		classpath = javaProject.getExpandedClasspath(true);
	} catch (JavaModelException e) {
		return e.getJavaModelStatus();
	}
	return JavaConventions.validateClasspath((IJavaProject) fElementsToProcess[0], classpath, fOutputLocation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4301.java