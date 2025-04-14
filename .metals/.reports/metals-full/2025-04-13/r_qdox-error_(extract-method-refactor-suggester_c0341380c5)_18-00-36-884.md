error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/172.java
text:
```scala
i@@f (extension != null && (extension.toLowerCase().equals("java"/*nonNLS*/) || extension.toLowerCase().equals("class"/*nonNLS*/))) {

package org.eclipse.jdt.internal.core.builder.impl;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import org.eclipse.jdt.internal.core.builder.*;

import java.util.*;

public class ReportCardImpl implements IReportCard {
	private StateImpl fState;
	private ImageContextImpl fImageContext;
public ReportCardImpl(StateImpl state, IImageContext context) {
	fState = state;
	fImageContext = (ImageContextImpl)context;
}
public IPath[] changedPaths(IReportCard previous) {
	Hashtable myProblemTable = problemTableFrom(getLeafProblemsFor(null));
	Hashtable prevProblemTable = problemTableFrom(previous.getLeafProblemsFor(null));
	Hashtable changed = new Hashtable();

	/* do for all problems in the new table */
	for (Enumeration e = myProblemTable.keys(); e.hasMoreElements();) {
		IPath path = (IPath) e.nextElement();
		Hashtable myProblems = (Hashtable) myProblemTable.get(path);
		Hashtable prevProblems = (Hashtable) prevProblemTable.remove(path);
		if (prevProblems == null || !setCompare(myProblems, prevProblems)) {
			changed.put(path, path);
		} 
	}

	/* pick up all problems that are only in the old table */
	for (Enumeration e = prevProblemTable.keys(); e.hasMoreElements();) {
		IPath path = (IPath) e.nextElement();
		changed.put(path, path);
	}

	/* convert results to array */
	IPath[] results = new IPath[changed.size()];
	int i = 0;
	for (Enumeration e = changed.keys(); e.hasMoreElements();i++) {
		results[i] = (IPath) e.nextElement();
	}
	return results;
}
	/**
	 * Returns all the problems for this state
	 */
	private void getAllProblems(Vector results) {
		/* iterate through problems in the problem table */
		for (Enumeration e = fState.getProblemReporter().getAllProblems(); e.hasMoreElements();) {
			results.addElement(e.nextElement());
		}
		
		/* go through all problems attached to the image itself */
		for (Enumeration e = fState.getProblems(); e.hasMoreElements();) {
			results.addElement(e.nextElement());
		}
	}
public IImageContext getImageContext() {
	return fImageContext;
}
public IProblemDetail[] getLeafProblemsFor(IPath path) {
	Vector vResults = new Vector();

	/* if we want all problems */
	if (path == null) {
		getAllProblems(vResults);
	}
	else {
		getProblemsForPath(path, vResults);
	}
	/* convert to array */
	IProblemDetail[] results = new IProblemDetail[vResults.size()];
	vResults.copyInto(results);
	return results;
}
public IPath[] getProblemPaths(IPath path) {
	Hashtable set = new Hashtable();

	/* build set of all element IDs */
	IProblemDetail[] problems = getLeafProblemsFor(path);
	for (int i = 0; i < problems.length; i++) {
		IPath problemPath = ((ProblemDetailImpl) problems[i]).getSourceEntry().getPath();
		set.put(problemPath, problemPath);
	}

	/* convert set to array */
	IPath[] results = new IPath[set.size()];
	int i = 0;
	for (Enumeration e = set.keys(); e.hasMoreElements();) {
		results[i++] = (IPath) e.nextElement();
	}
	return results;
}
private void getProblemsForPath(IPath path, Vector vResults) {
	for (Enumeration e = fState.getProblemReporter().getProblemKeys(); e.hasMoreElements();) {
		SourceEntry sEntry = (SourceEntry)e.nextElement();
		IPath sEntryPath = sEntry.getPath();
		if (path.isPrefixOf(sEntryPath)) {
			String extension = sEntryPath.getFileExtension();
			// test most frequent cases first
			if (extension != null && (extension.toLowerCase().equals("java") || extension.toLowerCase().equals("class"))) { //$NON-NLS-1$ //$NON-NLS-2$
				getProblemsForSourceEntry(sEntry, vResults);
			} else {
				if (fState.isZipElement(sEntryPath)) {
					getProblemsForZip(sEntryPath, vResults);
				}
			}
		}
	}
}
private void getProblemsForSourceEntry(SourceEntry sEntry, Vector results) {
	IProblemReporter pbReporter = fState.getProblemReporter();
	if (pbReporter.hasProblems(sEntry)) {
		// Only check image context if there are really problems with this element
		IPackage pkg = fState.getPathMap().packageHandleFromPath(sEntry.getPath().removeLastSegments(1));
		if (fImageContext == null || fImageContext.containsPackage(pkg)) {
			for (Enumeration e = pbReporter.getProblems(sEntry); e.hasMoreElements();) {
				results.addElement(e.nextElement());
			}
		}
	}
}
protected void getProblemsForZip(IPath zipPath, Vector results) {
	// avoid iterating over all entries in the zip file
	IProblemReporter pbReporter = fState.getProblemReporter();
	for (Enumeration e = pbReporter.getAllProblems(); e.hasMoreElements();) {
		ProblemDetailImpl problem = (ProblemDetailImpl) e.nextElement();
		SourceEntry sEntry = problem.getSourceEntry();
		if (sEntry.fZipEntryFileName != null && sEntry.getPath().equals(zipPath)) {
			// Check image context.
			if (fImageContext == null || fImageContext.containsPackage(fState.packageFromSourceEntry(sEntry))) {
				results.addElement(problem);
			}
		}
	}
}
public IState getState() {
	return fState;
}
public boolean hasLeafProblems(IPath path) {
	return getLeafProblemsFor(path).length > 0;
}
	/**
	 * Returns a dictionary of problems keyed by their path.
	 * Each entry in the dictionary is a set of problems.  The set is implemented as a 
	 * hashtable with identical keys and values.  Problems with no path are not 
	 * included in the result.
	 */
	private Hashtable problemTableFrom(IProblemDetail[] problems) {
		Hashtable table = new Hashtable(25);
		for (int i = 0; i < problems.length; i++) {
		IPath path = ((ProblemDetailImpl) problems[i]).getPath();
		if (path != null) {
				Hashtable entry = (Hashtable) table.get(path);
				if (entry == null) {
					entry = new Hashtable(10);
					table.put(path,entry);
				}
				entry.put(problems[i], problems[i]);
			}
		}
		return table;
	}
	/**
	 * Returns true if the two sets of problems are the same, false otherwise.
	 * Don't use ProblemDetail.equals() because it doesn't consider source 
	 * positions or severity
	 */
	private boolean setCompare(Hashtable myProblems, Hashtable prevProblems) {
		if (myProblems.size() != prevProblems.size()) {
			return false;
		}

		/* iterate through both tables */
		for (Enumeration e = myProblems.keys(); e.hasMoreElements();) {
			ProblemDetailImpl a = (ProblemDetailImpl) myProblems.get(e.nextElement());
			ProblemDetailImpl b =  (ProblemDetailImpl) prevProblems.get(a);
			if (b == null) {
				/* problem is not in prevProblems */
				return false;
			}
		}
		for (Enumeration e = prevProblems.keys(); e.hasMoreElements();) {
			ProblemDetailImpl a = (ProblemDetailImpl) prevProblems.get(e.nextElement());
			ProblemDetailImpl b =  (ProblemDetailImpl) myProblems.get(a);
			if (b == null) {
				/* problem is not in myProblems */
				return false;
			}
		}
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/172.java