error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1905.java
text:
```scala
I@@File file = container.getFile(filePath.addFileExtension(JavaBuilder.CLASS_EXTENSION));

package org.eclipse.jdt.internal.core.newbuilder;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.util.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.impl.*;
import org.eclipse.jdt.internal.compiler.problem.*;

import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.util.CharOperation;

import java.io.*;
import java.util.*;

/**
 * The abstract superclass of image builders.
 * Provides the building and compilation mechanism
 * in common with the batch and incremental builders.
 */
public abstract class AbstractImageBuilder implements ICompilerRequestor {

protected JavaBuilder javaBuilder;

// local copies
protected IContainer outputFolder;
protected IContainer[] sourceFolders;
protected BuildNotifier notifier;

protected boolean hasSeparateOutputFolder;
protected NameEnvironment nameEnvironment;
protected Compiler compiler;
protected State newState;
protected WorkQueue workQueue;
protected ArrayList problemTypeLocations;

private boolean inCompiler;

public static int MAX_AT_ONCE = 1000;
static final String ProblemMarkerTag = IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER;

protected AbstractImageBuilder(JavaBuilder javaBuilder) {
	this.javaBuilder = javaBuilder;

	// local copies
	this.outputFolder = javaBuilder.outputFolder;
	this.sourceFolders = javaBuilder.sourceFolders;
	this.notifier = javaBuilder.notifier;

	this.hasSeparateOutputFolder = !outputFolder.getFullPath().equals(javaBuilder.currentProject.getFullPath());
	this.nameEnvironment = new NameEnvironment(javaBuilder.classpath, javaBuilder.lastState);
	this.compiler = newCompiler();
	this.newState = new State(javaBuilder);
	this.workQueue = new WorkQueue();
	this.problemTypeLocations = new ArrayList(3);
}

public void acceptResult(CompilationResult result) {
	// In Batch mode, we write out the class files, hold onto the dependency info
	// & additional types and report problems.

	// In Incremental mode, when writing out a class file we need to compare it
	// against the previous file, remembering if structural changes occured.
	// Before reporting the new problems, we need to update the problem count &
	// remove the old problems. Plus delete additional class files that no longer exist.

	char[] fileId = result.getFileName();  // the full filesystem path 'd:/xyz/eclipse/Test/p1/p2/A.java'
	String filename = new String(fileId);
	if (!workQueue.isCompiled(filename)) {
		try {
			workQueue.finished(filename);
	
			ICompilationUnit compilationUnit = result.getCompilationUnit();
			ClassFile[] classFiles = result.getClassFiles();
			int length = classFiles.length;
			ArrayList otherTypeNames = new ArrayList(length);
			char[] mainTypeName = compilationUnit.getMainTypeName(); // may not match any produced class file
			for (int i = 0; i < length; i++) {
				ClassFile classFile = classFiles[i];
				char[][] compoundName = classFile.getCompoundName();
				char[] typeName = compoundName[compoundName.length - 1];
				if (CharOperation.equals(mainTypeName, typeName)) {
					writeClassFile(classFile, false);
				} else {
					boolean isSecondaryType = !CharOperation.contains('$', typeName);
					otherTypeNames.add(writeClassFile(classFile, isSecondaryType));
				}
			}
			newState.recordDependencies(fileId, result.qualifiedReferences, result.simpleNameReferences);
			updateProblemsFor(result);
			if (otherTypeNames.isEmpty()) {
				finishedWith(fileId, new char[0][]);
			} else {
				char[][] additionalTypeNames = new char[otherTypeNames.size()][];
				otherTypeNames.toArray(additionalTypeNames);
				finishedWith(fileId, additionalTypeNames);
			}
			notifier.compiled(compilationUnit);
		} catch (CoreException e) {
			throw internalException(e);
		}
	}
}

protected void cleanUp() {
	this.javaBuilder = null;
	this.compiler = null;
	this.nameEnvironment = null;
	this.workQueue = null;
	this.problemTypeLocations = null;
	this.newState.cleanup();
}

/* Compile the given elements, adding more elements to the work queue 
* if they are affected by the changes.
*/
protected void compile(String[] filenames, String[] initialTypeNames) {
	int toDo = filenames.length;
	if (toDo <= MAX_AT_ONCE) {
		// do them all now
		CompilationUnit[] toCompile = new CompilationUnit[toDo];
		for (int i = 0; i < toDo; i++) {
			String filename = filenames[i];
			if (JavaBuilder.DEBUG)
				System.out.println("About to compile " + filename);
			toCompile[i] = new CompilationUnit(null, filename);
		}
		compile(toCompile, initialTypeNames, null);
	} else {
		int i = 0;
		boolean compilingFirstGroup = true;
		while (i < toDo) {
			int doNow = Math.min(toDo, MAX_AT_ONCE);
			int index = 0;
			CompilationUnit[] toCompile = new CompilationUnit[doNow];
			String[] initialNamesInLoop = new String[doNow];
			while (i < toDo && index < doNow) {
				String filename = filenames[i];
				// Although it needed compiling when this method was called, it may have
				// already been compiled when it was referenced by another unit.
				if (compilingFirstGroup || workQueue.isWaiting(filename)) {
					if (JavaBuilder.DEBUG)
						System.out.println("About to compile " + filename);
					toCompile[index] = new CompilationUnit(null, filename);
					initialNamesInLoop[index++] = initialTypeNames[i];
				}
				i++;
			}
			if (index < doNow) {
				System.arraycopy(toCompile, 0, toCompile = new CompilationUnit[index], 0, index);
				System.arraycopy(initialNamesInLoop, 0, initialNamesInLoop = new String[index], 0, index);
			}
			String[] additionalFilenames = new String[toDo - i];
			System.arraycopy(filenames, i, additionalFilenames, 0, additionalFilenames.length);
			compilingFirstGroup = false;
			compile(toCompile, initialNamesInLoop, additionalFilenames);
		}
	}
}

void compile(CompilationUnit[] units, String[] initialTypeNames, String[] additionalFilenames) {
	if (units.length == 0) return;
	notifier.compiled(units[0]); // just to change the message

	// extend additionalFilenames with all hierarchical problem types found during this entire build
	if (!problemTypeLocations.isEmpty()) {
		int toAdd = problemTypeLocations.size();
		int length = additionalFilenames == null ? 0 : additionalFilenames.length;
		if (length == 0)
			additionalFilenames = new String[toAdd];
		else
			System.arraycopy(additionalFilenames, 0, additionalFilenames = new String[length + toAdd], 0, length);
		for (int i = 0; i < toAdd; i++)
			additionalFilenames[length + i] = (String) problemTypeLocations.get(i);
	}
	nameEnvironment.setNames(initialTypeNames, additionalFilenames);
	notifier.checkCancel();
	try {
		inCompiler = true;
		compiler.compile(units);
	} finally {
		inCompiler = false;
	}
	// Check for cancel immediately after a compile, because the compiler may
	// have been cancelled but without propagating the correct exception
	notifier.checkCancel();
}

protected void finishedWith(char[] fileId, char[][] additionalTypeNames) throws CoreException {
	newState.rememberAdditionalTypes(fileId, additionalTypeNames);
}

protected RuntimeException internalException(Throwable t) {
	ImageBuilderInternalException imageBuilderException = new ImageBuilderInternalException(t);
	if (inCompiler)
		return new AbortCompilation(true, imageBuilderException);
	return imageBuilderException;
}

protected Compiler newCompiler() {
	// called once when the builder is initialized... can override if needed
	return new Compiler(
		nameEnvironment,
		DefaultErrorHandlingPolicies.proceedWithAllProblems(),
		JavaCore.getOptions(),
		this,
		ProblemFactory.getProblemFactory(Locale.getDefault()));
}

protected IMarker[] getProblemsFor(IResource resource) {
	try {
		if (resource != null && resource.exists())
			return resource.findMarkers(ProblemMarkerTag, false, IResource.DEPTH_INFINITE);
	} catch (CoreException e) {
//@PM THIS CODE SHOULD PROBABLY HANDLE THE CORE EXCEPTION LOCALLY ?
		throw internalException(e);
	}
	return new IMarker[0];
}

protected void removeProblemsFor(IResource resource) {
	try {
		if (resource != null && resource.exists())
			resource.deleteMarkers(ProblemMarkerTag, false, IResource.DEPTH_INFINITE);
	} catch (CoreException e) {
//@PM THIS CODE SHOULD PROBABLY HANDLE THE CORE EXCEPTION LOCALLY ?
		throw internalException(e);

	}
}

/**
 * Creates a marker from each problem and adds it to the resource.
 * The marker is as follows:
 *   - its type is T_PROBLEM
 *   - its plugin ID is the JavaBuilder's plugin ID
 *	 - its message is the problem's message
 *	 - its priority reflects the severity of the problem
 *	 - its range is the problem's range
 *	 - it has an extra attribute "ID" which holds the problem's id
 */
protected void storeProblemsFor(IResource resource, IProblem[] problems) {
	if (problems == null || problems.length == 0) return;
	for (int i = 0, length = problems.length; i < length; i++) {
		try {
			IProblem problem = problems[i];
			int id = problem.getID();
			switch (id) {
				case ProblemIrritants.SuperclassMustBeAClass :
				case ProblemIrritants.SuperInterfaceMustBeAnInterface :
				case ProblemIrritants.HierarchyCircularitySelfReference :
				case ProblemIrritants.HierarchyCircularity :
				case ProblemIrritants.HierarchyHasProblems :
				case ProblemIrritants.InvalidSuperclassBase :
				case ProblemIrritants.InvalidSuperclassBase + 1 :
				case ProblemIrritants.InvalidSuperclassBase + 2 :
				case ProblemIrritants.InvalidSuperclassBase + 3 :
				case ProblemIrritants.InvalidSuperclassBase + 4 :
				case ProblemIrritants.InvalidInterfaceBase :
				case ProblemIrritants.InvalidInterfaceBase + 1 :
				case ProblemIrritants.InvalidInterfaceBase + 2 :
				case ProblemIrritants.InvalidInterfaceBase + 3 :
				case ProblemIrritants.InvalidInterfaceBase + 4 :
					// ensure that this file is always retrieved from source for the rest of the build
					String fileLocation = resource.getLocation().toString();
					if (!problemTypeLocations.contains(fileLocation))
						problemTypeLocations.add(fileLocation);
			}

			IMarker marker = resource.createMarker(ProblemMarkerTag);
			marker.setAttributes(
				new String[] {IMarker.MESSAGE, IMarker.SEVERITY, "ID", IMarker.CHAR_START, IMarker.CHAR_END, IMarker.LINE_NUMBER}, //$NON-NLS-1$
				new Object[] { 
					problem.getMessage(),
					new Integer(problem.isError() ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING), 
					new Integer(id),
					new Integer(problem.getSourceStart()),
					new Integer(problem.getSourceEnd() + 1),
					new Integer(problem.getSourceLineNumber())
				});

// Do we need to do this?
//@PM WE SHOULD HAVE IT COME FROM THE PROBLEM ITSELF INSTEAD OF POPULATING THE JAVA MODEL
			// compute a user-friendly location
			IJavaElement element = JavaCore.create(resource);
			if (element instanceof org.eclipse.jdt.core.ICompilationUnit) { // try to find a finer grain element
				org.eclipse.jdt.core.ICompilationUnit unit = (org.eclipse.jdt.core.ICompilationUnit) element;
				IJavaElement fragment = unit.getElementAt(problem.getSourceStart());
				if (fragment != null) element = fragment;
			}
			String location = null;
			if (element instanceof JavaElement)
				location = ((JavaElement) element).readableName();
			if (location != null)
				marker.setAttribute(IMarker.LOCATION, location);
		} catch(CoreException e) {
			throw internalException(e);
		}
	}
}

protected void updateProblemsFor(CompilationResult result) {
	// expect subclasses to override
}

protected IContainer getOutputFolder(IPath packagePath) throws CoreException {
	IFolder folder = outputFolder.getFolder(packagePath);
// when can this ever happen if builders build the package tree before compiling source files?
	if (!folder.exists()) {
		getOutputFolder(packagePath.removeLastSegments(1));
		folder.create(true, true, null);
	}
	return folder;
}

protected boolean isClassFileChanged(IFile file, String fileName, byte[] bytes, boolean isSecondaryType) throws CoreException {
	// In Incremental mode, compare the bytes against the previous file for structural changes
	return true;
}

protected char[] writeClassFile(ClassFile classFile, boolean isSecondaryType) throws CoreException {
	// Before writing out the class file, compare it to the previous file
	// If structural changes occured then add dependent source files
	String fileName = new String(classFile.fileName());
	IPath filePath = new Path(fileName);			
	IContainer container = outputFolder;
	if (filePath.segmentCount() > 1) {
		container = getOutputFolder(filePath.removeLastSegments(1));
		filePath = new Path(filePath.lastSegment());
	}

	IFile file = container.getFile(filePath.addFileExtension(JavaBuilder.ClassExtension));
	byte[] bytes = classFile.getBytes();
	if (isClassFileChanged(file, fileName, bytes, isSecondaryType)) {
		if (JavaBuilder.DEBUG)
			System.out.println("Writing class file " + file.getName());
		file.create(new ByteArrayInputStream(bytes), true, null);
	} else if (JavaBuilder.DEBUG) {
		System.out.println("Skipped over unchanged class file " + file.getName());
	}
	// answer the name of the class file as in Y or Y$M
	return filePath.lastSegment().toCharArray();
//@PM THIS CODE SHOULD PROBABLY HANDLE THE CORE EXCEPTION LOCALLY ?
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1905.java