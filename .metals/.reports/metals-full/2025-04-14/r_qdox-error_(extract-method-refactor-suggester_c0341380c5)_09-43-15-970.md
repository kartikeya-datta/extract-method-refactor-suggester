error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7452.java
text:
```scala
n@@ew String[]{ IMarker.MESSAGE, IMarker.SEVERITY, "ID", IMarker.CHAR_START, IMarker.CHAR_END, IMarker.LINE_NUMBER}, //$NON-NLS-1$

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.internal.core.builder.*;
import org.eclipse.jdt.core.*;

import java.util.*;


/**
 * An <code>IProblemReporter</code> that reports problems using the <code>IMarker</code> API.
 * It keeps the problems backed in a ProblemTable as well, since the marker manager only 
 * maintains problems for the last built state.
 * In particular, the test suite requires that problems be retrieved for previously built states,
 * and for currently built states (batch and incremental builds of the same workspace).
 */
public class MarkerProblemReporter implements IProblemReporter {
	protected IProject fProject;
	protected ProblemTable fProblemTable = new ProblemTable();
	private IDevelopmentContext dc;
/**
 * Creates a new MarkerProblemReporter that is not initialized.
 * Used only during deserialization.
 */
public MarkerProblemReporter() {
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public IProblemReporter copy() {
	/* The copy still reports to the same marker manager, since there's only one,
	 * but the backing problem table is copied. */
	MarkerProblemReporter copy = new MarkerProblemReporter(fProject, this.dc);
	copy.fProblemTable = (ProblemTable) fProblemTable.copy();
	return copy;
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public Enumeration getAllProblems() {
	return fProblemTable.getAllProblems();
}
/**
 * Returns the extra flags for the given marker.
 */
protected int getFlags(IMarker marker) throws CoreException {
	if (!marker.exists()) return 0;
	Integer flags = (Integer) marker.getAttribute(IJavaModelMarker.FLAGS);
	return flags == null ? 0 : flags.intValue();
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public Enumeration getImageProblems() {
	return fProblemTable.getImageProblems();
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public Enumeration getProblemKeys() {
	return fProblemTable.getProblemKeys();
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public Enumeration getProblems(Object sourceID) {
	return fProblemTable.getProblems(sourceID);
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public Vector getProblemVector(Object sourceID) {
	return fProblemTable.getProblemVector(sourceID);
}
/**
 * Returns the resource for the given source ID.
 * Returns null if the source ID is not a SourceEntry or resource, 
 * or if the resource could not be found.
 */
protected IResource getResource(Object sourceID) {
	if (sourceID instanceof IResource) {
		return (IResource) sourceID;
	}
	if (!(sourceID instanceof SourceEntry))
		return null;
	SourceEntry entry = (SourceEntry) sourceID;
	IPath path = entry.getPath();
	if (path.isAbsolute()) {
		if (fProject.getFullPath().isPrefixOf(path)) {
			return fProject.getWorkspace().getRoot().getFile(path);
		}
	} else {
		return fProject.getFile(path);
	}
	return null;
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public boolean hasProblems(Object sourceID) {
	return fProblemTable.hasProblems(sourceID);
}
/**
 * Creates a marker from the given problem detail and add it to the resource.
 * The marker is as follows:
 *   - its type is T_PROBLEM
 *   - its plugin ID is the JavaBuilder's plugin ID
 *	 - its message is the problem's message
 *	 - its priority reflects the severity of the problem
 *	 - its range is the problem's range
 *	 - it has an extra attribute "ID" which holds the problem's id
 */
protected void markerFromProblemDetail(IResource resource, IProblemDetail problem) throws CoreException {
	
	IMarker marker = resource.createMarker(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER);
	int start = problem.getStartPos();
	int sev = problem.getSeverity();
	marker.setAttributes(
		new String[]{ IMarker.MESSAGE, IMarker.SEVERITY, "ID"/*nonNLS*/, IMarker.CHAR_START, IMarker.CHAR_END, IMarker.LINE_NUMBER},
		new Object[]{ 
			problem.getMessage(),
			new Integer((sev & IProblemDetail.S_ERROR) != 0 ? IMarker.SEVERITY_ERROR : IMarker.SEVERITY_WARNING), 
			new Integer(problem.getID()),
			new Integer(start),
			new Integer(problem.getEndPos() + 1),
			new Integer(problem.getLineNumber())
		});
		
	if ((sev & ProblemDetailImpl.S_SYNTAX_ERROR) != 0) {
		marker.setAttribute(IJavaModelMarker.FLAGS, ProblemDetailImpl.S_SYNTAX_ERROR == 0 ? null : new Integer(ProblemDetailImpl.S_SYNTAX_ERROR));
	}
	// compute a user-friendly location
	IJavaElement element = JavaCore.create(resource);
	if (element instanceof ICompilationUnit){ // try to find a finer grain element
		ICompilationUnit unit = (ICompilationUnit) element;
		IJavaElement fragment = unit.getElementAt(start);
		if (fragment != null) element = fragment;
	}
	String location = null;
	if (element instanceof org.eclipse.jdt.internal.core.JavaElement){
		location = ((org.eclipse.jdt.internal.core.JavaElement)element).readableName();
	}
	if (location != null) marker.setAttribute(IMarker.LOCATION, location);
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public void putProblem(Object sourceID, IProblemDetail problem) {
	/* Delegate first to the backing problem table. */
	fProblemTable.putProblem(sourceID, problem);

	/* Now update the markers. */
	IResource resource = getResource(sourceID);
	if (resource != null) {
		try {
			markerFromProblemDetail(resource, problem);
		} catch (CoreException e) {
			throw ((JavaDevelopmentContextImpl)this.dc).internalException(e);
		}
	}
}
/**
 * Remove problem markers for the given element. If removeSyntaxErrors is true, remove only syntax errors,
 * otherwise remove only non-syntax errors.
 */
protected void removeMarkers(Object sourceID, boolean removeSyntaxErrors) {
	IResource resource = getResource(sourceID);
	if (resource != null && resource.exists()) {
		try {
			IMarker[] markers = resource.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
			if (markers.length > 0) {
				Vector toRemove = new Vector(markers.length);
				for (int i = 0; i < markers.length; i++) {
					IMarker marker = markers[i];
					try {
						boolean isSyntaxError = (getFlags(marker) & ProblemDetailImpl.S_SYNTAX_ERROR) != 0;
						if (isSyntaxError == removeSyntaxErrors) {
							toRemove.addElement(marker);
						}
					} catch(CoreException e){ // marker state cannot be accessed - ignore it
					}
				}
				if (toRemove.size() > 0) {
					IMarker[] markersToRemove = new IMarker[toRemove.size()];
					toRemove.copyInto(markersToRemove);
					resource.getWorkspace().deleteMarkers(markersToRemove);
				}
			}
		} catch (CoreException e) { // silently absorb CoreException during marker deletion
		}
	}
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public void removeNonSyntaxErrors(Object sourceID) {
	/* Delegate first to the backing problem table. */
	fProblemTable.removeNonSyntaxErrors(sourceID);
	
	removeMarkers(sourceID, false);
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public void removeProblems(Object sourceID) {
	/* Delegate first to the backing problem table. */
	fProblemTable.removeProblems(sourceID);

	/* Now update the markers. */
	IResource resource = getResource(sourceID);
	if (resource != null) {
		try {
			// See PR 1G2NPUH
			// If the resource doesn't exist, we don't want to try to remove markers for it.
			// Simply ignore. This test is done to prevent from having an exception.
			if (resource.exists()) 
				resource.deleteMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			throw ((JavaDevelopmentContextImpl)this.dc).internalException(e);
		}
	}
}
/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public void removeSyntaxErrors(Object sourceID) {
	/* Delegate first to the backing problem table. */
	fProblemTable.removeSyntaxErrors(sourceID);
	
	removeMarkers(sourceID, true);
}
/**
 * Creates a new MarkerProblemReporter that reports problems as markers 
 * against the given project.
 */
public MarkerProblemReporter(IProject project, IDevelopmentContext dc) {
	initialize(project, dc);
}

/**
 * @see org.eclipse.jdt.internal.core.builder.IProblemReporter
 */
public void initialize(IProject project, IDevelopmentContext dc) {
	this.fProject = project;
	this.dc = dc;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7452.java