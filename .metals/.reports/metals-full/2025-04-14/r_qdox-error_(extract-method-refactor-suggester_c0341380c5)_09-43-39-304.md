error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8965.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8965.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8965.java
text:
```scala
i@@ndex.add(new IFileDocument(resource, contents), new SourceIndexer(resource));

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.core.search.indexing;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.core.index.IIndex;
import org.eclipse.jdt.internal.core.index.impl.IFileDocument;
import org.eclipse.jdt.internal.core.search.processing.JobManager;

class AddCompilationUnitToIndex extends IndexRequest {
	IFile resource;
	IndexManager manager;
	IPath indexedContainer;
	char[] contents;
	public AddCompilationUnitToIndex(
		IFile resource,
		IndexManager manager,
		IPath indexedContainer) {
		this.resource = resource;
		this.manager = manager;
		this.indexedContainer = indexedContainer;
	}
	public boolean belongsTo(String jobFamily) {
		return jobFamily.equals(this.indexedContainer.segment(0));
	}
	public boolean execute(IProgressMonitor progressMonitor) {

		if (progressMonitor != null && progressMonitor.isCanceled()) return COMPLETE;
		try {
			IIndex index = manager.getIndex(this.indexedContainer, true /*reuse index file*/, true /*create if none*/);

			/* ensure no concurrent write access to index */
			if (index == null)
				return COMPLETE;
			ReadWriteMonitor monitor = manager.getMonitorFor(index);
			if (monitor == null)
				return COMPLETE; // index got deleted since acquired
			try {
				monitor.enterWrite(); // ask permission to write
				char[] contents = this.getContents();
				if (contents == null)
					return FAILED;
				index.add(new IFileDocument(resource, contents), new SourceIndexer());
			} finally {
				monitor.exitWrite(); // free write lock
			}
		} catch (IOException e) {
			if (JobManager.VERBOSE) {
				JobManager.verbose("-> failed to index " + this.resource + " because of the following exception:"); //$NON-NLS-1$ //$NON-NLS-2$
				e.printStackTrace();
			}
			return FAILED;
		}
		return COMPLETE;
	}
	private char[] getContents() {
		if (this.contents == null)
			this.initializeContents();
		return contents;
	}
	public void initializeContents() {

		try {
			IPath location = resource.getLocation();
			if (location != null) {
				this.contents =
					org.eclipse.jdt.internal.compiler.util.Util.getFileCharContent(
						location.toFile(), null);
			}
		} catch (IOException e) {
		}
	}
	public String toString() {
		return "indexing " + resource.getFullPath(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8965.java