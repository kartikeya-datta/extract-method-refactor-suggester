error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1125.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1125.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1125.java
text:
```scala
public static b@@oolean DEBUG = false;

package org.eclipse.jdt.internal.core.newbuilder;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.util.*;

import org.eclipse.jdt.internal.compiler.util.CharOperation;

import java.util.*;

public class JavaBuilder extends IncrementalProjectBuilder {

IProject currentProject;
IJavaProject javaProject;
IWorkspaceRoot workspaceRoot;
ClasspathLocation[] classpath;
IContainer outputFolder;
IContainer[] sourceFolders;
LookupTable prereqOutputFolders; // maps a prereq project to its output folder
State lastState;
BuildNotifier notifier;

public static final String JavaExtension = "java"; //$NON-NLS-1$
public static final String ClassExtension = "class"; //$NON-NLS-1$

public static final boolean DEBUG = false;

public JavaBuilder() {
}

protected IProject[] build(int kind, Map ignored, IProgressMonitor monitor) throws CoreException {
	this.currentProject = getProject();
	if (currentProject == null || !currentProject.exists()) return new IProject[0];

	this.notifier = new BuildNotifier(monitor, currentProject);
	notifier.begin();
	boolean ok = false;
	try {
		initializeBuilder();

		if (kind == FULL_BUILD) {
			buildAll();
		} else {
			if (lastState == null || hasClasspathChanged() || hasOutputLocationChanged()) {
				// if the output location changes, do not delete the binary files from old location
				// the user may be trying something
				buildAll();
			} else if (sourceFolders.length > 0) {
				// if there is no source to compile & no classpath changes then we are done
				LookupTable deltas = findDeltas();
				if (deltas == null)
					buildAll();
				else
					buildDeltas(deltas);
			}
		}
		ok = true;
	} catch (ImageBuilderInternalException e) {
		// Fix for 1FW2XY6: ITPJCORE:ALL - Image builder wrappers CoreException
		// WHY not just let the CoreException thru instead of wrappering it?
		if (e.getThrowable() instanceof CoreException)
			throw (CoreException) e.getThrowable();
		throw new CoreException(
			new Status(IStatus.ERROR, JavaCore.PLUGIN_ID, -1,
				Util.bind("build.builderName"), e)); //$NON-NLS-1$
	} catch (OperationCanceledException e) {
		// Do nothing for now, and avoid propagating the exception.  
		// The finally block ensures we will do a full build next time.
		// See 1FVJ5Z8: ITPCORE:ALL - How should builders handle cancel?
	} finally {
		if (!ok)
			// If the build failed, clear the previously built state, forcing a full build next time.
			setLastState(null);
		notifier.done();
		cleanup();
	}
	return getRequiredProjects();
}

private void buildAll() throws CoreException {
	if (DEBUG)
		System.out.println("FULL build of: " + currentProject.getName()); //$NON-NLS-1$

	notifier.subTask(Util.bind("build.preparingBuild")); //$NON-NLS-1$
	setLastState(null); // free last state since we do not need it
	BatchImageBuilder imageBuilder = new BatchImageBuilder(this);
	imageBuilder.build();
	setLastState(imageBuilder.newState);
}

private void buildDeltas(LookupTable deltas) throws CoreException {
	if (DEBUG)
		System.out.println("INCREMENTAL build of: " + currentProject.getName()); //$NON-NLS-1$

	notifier.subTask(Util.bind("build.preparingBuild")); //$NON-NLS-1$
	IncrementalImageBuilder imageBuilder = new IncrementalImageBuilder(this);
	if (imageBuilder.build(deltas))
		setLastState(imageBuilder.newState);
	else
		buildAll();
}

private void cleanup() {
	this.workspaceRoot = null;
	this.classpath = null;
	this.outputFolder = null;
	this.sourceFolders = null;
	this.prereqOutputFolders = null;
	this.lastState = null;
	this.notifier = null;
}

private LookupTable findDeltas() {
	notifier.subTask(Util.bind("build.readingDelta", currentProject.getName())); //$NON-NLS-1$
	IResourceDelta delta = getDelta(currentProject);
	LookupTable deltas = new LookupTable();
	if (delta != null) {
		deltas.put(currentProject, delta);
	} else {
		if (DEBUG)
			System.out.println("Missing delta for: " + currentProject.getName()); //$NON-NLS-1$
		notifier.subTask(""); //$NON-NLS-1$
		return null;
	}

	Enumeration enum = prereqOutputFolders.keys();
	while (enum.hasMoreElements()) {
		IProject prereqProject = (IProject) enum.nextElement();
		notifier.subTask(Util.bind("build.readingDelta", prereqProject.getName())); //$NON-NLS-1$
		delta = getDelta(prereqProject);
		if (delta != null) {
			deltas.put(prereqProject, delta);
		} else {
			if (DEBUG)
				System.out.println("Missing delta for: " + prereqProject.getName());	 //$NON-NLS-1$
			notifier.subTask(""); //$NON-NLS-1$
			return null;
		}
	}
	notifier.subTask(""); //$NON-NLS-1$
	return deltas;
}

private IJavaProject getJavaProject(IProject project) {
	return JavaCore.create(project);
}

private State getLastState() {
	return (State) JavaModelManager.getJavaModelManager().getLastBuiltState2(currentProject, notifier.monitor);
}

private void setLastState(State state) {
	JavaModelManager.getJavaModelManager().setLastBuiltState2(currentProject, state);
}

/* Return the list of projects for which it requires a resource delta. This builder's project
* is implicitly included and need not be specified. Builders must re-specify the list 
* of interesting projects every time they are run as this is not carried forward
* beyond the next build. Missing projects should be specified but will be ignored until
* they are added to the workspace.
*/
private IProject[] getRequiredProjects() {
	if (javaProject == null) return new IProject[0];

	// this is NOT what you want but it will do for now see if JavaProject cannot
	// provide the answer by itself - see getRequiredProjectNames()
	IPackageFragmentRoot[] roots;
	try {
		roots = ((JavaProject) javaProject).getBuilderRoots(null);
	} catch(JavaModelException e) {
		return new IProject[0];
	}

	ArrayList projects = new ArrayList();
	for (int i = 0; i < roots.length; ++i) {
		IJavaProject p = roots[i].getJavaProject();
		if (p != null && p != javaProject) {
			IProject project = p.getProject();
			if (!projects.contains(project))
				projects.add(project);
		}
	}
	IProject[] result = new IProject[projects.size()];
	projects.toArray(result);
	return result;
}

private boolean hasClasspathChanged() {
	ClasspathLocation[] oldClasspathLocations = lastState.classpathLocations;
	if (classpath.length != oldClasspathLocations.length) return true;

	for (int i = 0, length = classpath.length; i < length; i++)
		if (!classpath[i].equals(oldClasspathLocations[i])) return true;
	return false;
}

private boolean hasOutputLocationChanged() {
	return !outputFolder.getLocation().toString().equals(lastState.outputLocationString);
}

private void createFolder(IContainer folder) throws CoreException {
	if (!folder.exists()) {
		IContainer parent = folder.getParent();
		if (currentProject.getFullPath() != parent.getFullPath())
			createFolder(parent);
		((IFolder) folder).create(true, true, null);
	}
}

private void initializeBuilder() throws CoreException {
	this.javaProject = getJavaProject(currentProject);
	this.workspaceRoot = currentProject.getWorkspace().getRoot();
	this.outputFolder = (IContainer) workspaceRoot.findMember(javaProject.getOutputLocation());
	if (this.outputFolder == null) {
		this.outputFolder = workspaceRoot.getFolder(javaProject.getOutputLocation());
		createFolder(this.outputFolder);
	}
	this.lastState = getLastState();

	/* Some examples of resolved class path entries.
	* Remember to search class path in the order that it was defined.
	*
	* 1a. typical project with no source folders:
	*   /Test[CPE_SOURCE][K_SOURCE] -> D:/eclipse.test/Test
	* 1b. project with source folders:
	*   /Test/src1[CPE_SOURCE][K_SOURCE] -> D:/eclipse.test/Test/src1
	*   /Test/src2[CPE_SOURCE][K_SOURCE] -> D:/eclipse.test/Test/src2
	*  NOTE: These can be in any order & separated by prereq projects or libraries
	* 1c. project external to workspace (only detectable using getLocation()):
	*   /Test/src[CPE_SOURCE][K_SOURCE] -> d:/eclipse.zzz/src
	*  Need to search source folder & output folder TOGETHER
	*  Use .java file if its more recent than .class file
	*
	* 2. zip files:
	*   D:/j9/lib/jclMax/classes.zip[CPE_LIBRARY][K_BINARY][sourcePath:d:/j9/lib/jclMax/source/source.zip]
	*      -> D:/j9/lib/jclMax/classes.zip
	*  ALWAYS want to take the library path as is
	*
	* 3a. prereq project (regardless of whether it has a source or output folder):
	*   /Test[CPE_PROJECT][K_SOURCE] -> D:/eclipse.test/Test
	*  ALWAYS want to append the output folder & ONLY search for .class files
	*/

	IClasspathEntry[] entries = ((JavaProject) javaProject).getExpandedClasspath(true);
	int count = 0;
	int max = entries.length;
	this.sourceFolders = new IContainer[max];
	this.classpath = new ClasspathLocation[entries.length];
	this.prereqOutputFolders = new LookupTable();
	for (int i = 0; i < max; i++) {
		IClasspathEntry entry = entries[i];
		IResource member = workspaceRoot.findMember(entry.getPath());
		switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_SOURCE :
				sourceFolders[count] = (IContainer) member;
				classpath[i] = ClasspathLocation.forSourceFolder(
					sourceFolders[count++].getLocation().toString(),
					outputFolder.getLocation().toString());
				break;
			case IClasspathEntry.CPE_PROJECT :
				IProject prereqProject = (IProject) member;
				IPath prereqPath = getJavaProject(prereqProject).getOutputLocation();
				IResource prereqOutputFolder = prereqProject.getFullPath().equals(prereqPath)
					? prereqProject
					: workspaceRoot.findMember(prereqPath);
				prereqOutputFolders.put(prereqProject, prereqOutputFolder);
				classpath[i] = ClasspathLocation.forRequiredProject(prereqOutputFolder.getLocation().toString());
				break;
			case IClasspathEntry.CPE_LIBRARY :
				// Classpath entries do not know whether their path is workspace relative or
				// filesystem based so see if a resource exists to match...
				classpath[i] = member == null
					? ClasspathLocation.forLibrary(entry.getPath().toString())
					: ClasspathLocation.forLibrary(member.getLocation().toString());
				break;
		}
	}
	if (count < max)
		System.arraycopy(sourceFolders, 0, (sourceFolders = new IContainer[count]), 0, count);
}

/**
 * String representation for debugging purposes
 */
public String toString() {
	State state = getLastState();
	if (state != null)
		return "JavaBuilder(" //$NON-NLS-1$
			+ state + ")"; //$NON-NLS-1$
	return "JavaBuilder(no built state)"; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1125.java