error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9222.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9222.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9222.java
text:
```scala
I@@ndexManager indexManager = JavaModelManager.getIndexManager();

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search;

import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.index.Index;
import org.eclipse.jdt.internal.core.search.indexing.IndexManager;
import org.eclipse.jdt.internal.core.search.indexing.ReadWriteMonitor;
import org.eclipse.jdt.internal.core.search.matching.MatchLocator;
import org.eclipse.jdt.internal.core.search.processing.IJob;
import org.eclipse.jdt.internal.core.search.processing.JobManager;
import org.eclipse.jdt.internal.core.util.Util;

public class PatternSearchJob implements IJob {

protected SearchPattern pattern;
protected IJavaSearchScope scope;
protected SearchParticipant participant;
protected IndexQueryRequestor requestor;
protected boolean areIndexesReady;
protected long executionTime = 0;

public PatternSearchJob(SearchPattern pattern, SearchParticipant participant, IJavaSearchScope scope, IndexQueryRequestor requestor) {
	this.pattern = pattern;
	this.participant = participant;
	this.scope = scope;
	this.requestor = requestor;
}
public boolean belongsTo(String jobFamily) {
	return true;
}
public void cancel() {
	// search job is cancelled through progress 
}
public void ensureReadyToRun() {
	if (!this.areIndexesReady)
		getIndexes(null/*progress*/); // may trigger some index recreation
}
public boolean execute(IProgressMonitor progressMonitor) {
	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	boolean isComplete = COMPLETE;
	executionTime = 0;
	Index[] indexes = getIndexes(progressMonitor);
	try {
		int max = indexes.length;
		if (progressMonitor != null)
			progressMonitor.beginTask("", max); //$NON-NLS-1$
		for (int i = 0; i < max; i++) {
			isComplete &= search(indexes[i], progressMonitor);
			if (progressMonitor != null) {
				if (progressMonitor.isCanceled()) throw new OperationCanceledException();
				progressMonitor.worked(1);
			}
		}
		if (JobManager.VERBOSE)
			Util.verbose("-> execution time: " + executionTime + "ms - " + this);//$NON-NLS-1$//$NON-NLS-2$
		return isComplete;
	} finally {
		if (progressMonitor != null)
			progressMonitor.done();
	}
}
public Index[] getIndexes(IProgressMonitor progressMonitor) {
	// acquire the in-memory indexes on the fly
	IPath[] indexLocations = this.participant.selectIndexes(this.pattern, this.scope);
	int length = indexLocations.length;
	Index[] indexes = new Index[length];
	int count = 0;
	IndexManager indexManager = JavaModelManager.getJavaModelManager().getIndexManager();
	for (int i = 0; i < length; i++) {
		if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();
		// may trigger some index recreation work
		IPath indexLocation = indexLocations[i];
		Index index = indexManager.getIndex(indexLocation);
		if (index == null) {
			// only need containerPath if the index must be built
			IPath containerPath = (IPath) indexManager.indexLocations.keyForValue(indexLocation);
			if (containerPath != null) // sanity check
				index = indexManager.getIndex(containerPath, indexLocation, true /*reuse index file*/, false /*do not create if none*/);
		}
		if (index != null)
			indexes[count++] = index; // only consider indexes which are ready
	}
	if (count == length) 
		this.areIndexesReady = true;
	else
		System.arraycopy(indexes, 0, indexes=new Index[count], 0, count);
	return indexes;
}	
public boolean search(Index index, IProgressMonitor progressMonitor) {
	if (index == null) return COMPLETE;
	if (progressMonitor != null && progressMonitor.isCanceled()) throw new OperationCanceledException();

	ReadWriteMonitor monitor = index.monitor;
	if (monitor == null) return COMPLETE; // index got deleted since acquired
	try {
		monitor.enterRead(); // ask permission to read
		long start = System.currentTimeMillis();
		MatchLocator.findIndexMatches(this.pattern, index, requestor, this.participant, this.scope, progressMonitor);
		executionTime += System.currentTimeMillis() - start;
		return COMPLETE;
	} catch (IOException e) {
		if (e instanceof java.io.EOFException)
			e.printStackTrace();
		return FAILED;
	} finally {
		monitor.exitRead(); // finished reading
	}
}
public String toString() {
	return "searching " + pattern.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9222.java