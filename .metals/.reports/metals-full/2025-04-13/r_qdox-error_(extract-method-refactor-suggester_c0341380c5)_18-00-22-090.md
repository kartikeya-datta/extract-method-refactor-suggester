error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5312.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5312.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5312.java
text:
```scala
I@@Index index = manager.getIndexForUpdate(this.indexPath, false, /*do not reuse index file*/ false /*do not create if none*/);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.search.indexing;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.index.IIndex;
import org.eclipse.jdt.internal.core.index.IQueryResult;
import org.eclipse.jdt.internal.core.index.impl.JarFileEntryDocument;
import org.eclipse.jdt.internal.core.search.processing.JobManager;
import org.eclipse.jdt.internal.core.util.SimpleLookupTable;

class AddJarFileToIndex extends IndexRequest {
	IFile resource;

	public AddJarFileToIndex(IFile resource, IndexManager manager) {
		super(resource.getFullPath(), manager);
		this.resource = resource;
	}
	public AddJarFileToIndex(IPath indexPath, IndexManager manager) {
		// external JAR scenario - no resource
		super(indexPath, manager);
	}
	public boolean belongsTo(String projectNameOrJarPath) {
		// used to remove pending jobs because the project was deleted... not to delete index files
		// can be found either by project name or JAR path name
		return super.belongsTo(projectNameOrJarPath)
 projectNameOrJarPath.equals(this.indexPath.toString());
	}
	public boolean equals(Object o) {
		if (o instanceof AddJarFileToIndex) {
			if (this.resource != null)
				return this.resource.equals(((AddJarFileToIndex) o).resource);
			if (this.indexPath != null)
				return this.indexPath.equals(((AddJarFileToIndex) o).indexPath);
		}
		return false;
	}
	public int hashCode() {
		if (this.resource != null)
			return this.resource.hashCode();
		if (this.indexPath != null)
			return this.indexPath.hashCode();
		return -1;
	}
	public boolean execute(IProgressMonitor progressMonitor) {

		if (this.isCancelled || progressMonitor != null && progressMonitor.isCanceled()) return true;

		try {
			// if index is already cached, then do not perform any check
			// MUST reset the IndexManager if a jar file is changed
			IIndex index = (IIndex) manager.getIndexForUpdate(this.indexPath, false, /*do not reuse index file*/ false /*do not create if none*/);
			if (index != null) {
				if (JobManager.VERBOSE)
					JobManager.verbose("-> no indexing required (index already exists) for " + this.indexPath); //$NON-NLS-1$
				return true;
			}

			index = manager.getIndexForUpdate(this.indexPath, true, /*reuse index file*/ true /*create if none*/);
			if (index == null) {
				if (JobManager.VERBOSE)
					JobManager.verbose("-> index could not be created for " + this.indexPath); //$NON-NLS-1$
				return true;
			}
			ReadWriteMonitor monitor = manager.getMonitorFor(index);
			if (monitor == null) {
				if (JobManager.VERBOSE)
					JobManager.verbose("-> index for " + this.indexPath + " just got deleted"); //$NON-NLS-1$//$NON-NLS-2$
				return true; // index got deleted since acquired
			}
			ZipFile zip = null;
			try {
				// this path will be a relative path to the workspace in case the zipfile in the workspace otherwise it will be a path in the
				// local file system
				Path zipFilePath = null;

				monitor.enterWrite(); // ask permission to write
				if (resource != null) {
					IPath location = this.resource.getLocation();
					if (location == null) return false;
					if (JavaModelManager.ZIP_ACCESS_VERBOSE)
						System.out.println("(" + Thread.currentThread() + ") [AddJarFileToIndex.execute()] Creating ZipFile on " + location); //$NON-NLS-1$	//$NON-NLS-2$
					zip = new ZipFile(location.toFile());
					zipFilePath = (Path) this.resource.getFullPath().makeRelative();
					// absolute path relative to the workspace
				} else {
					if (JavaModelManager.ZIP_ACCESS_VERBOSE)
						System.out.println("(" + Thread.currentThread() + ") [AddJarFileToIndex.execute()] Creating ZipFile on " + this.indexPath); //$NON-NLS-1$	//$NON-NLS-2$
					zip = new ZipFile(this.indexPath.toFile());
					zipFilePath = (Path) this.indexPath;
					// path is already canonical since coming from a library classpath entry
				}

				if (this.isCancelled) {
					if (JobManager.VERBOSE)
						JobManager.verbose("-> indexing of " + zip.getName() + " has been cancelled"); //$NON-NLS-1$ //$NON-NLS-2$
					return false;
				}

				if (JobManager.VERBOSE)
					JobManager.verbose("-> indexing " + zip.getName()); //$NON-NLS-1$
				long initialTime = System.currentTimeMillis();

				IQueryResult[] results = index.queryInDocumentNames(""); // all file names //$NON-NLS-1$
				int max = results == null ? 0 : results.length;
				if (max != 0) {
					/* check integrity of the existing index file
					 * if the length is equal to 0, we want to index the whole jar again
					 * If not, then we want to check that there is no missing entry, if
					 * one entry is missing then we recreate the index
					 */
					String EXISTS = "OK"; //$NON-NLS-1$
					String DELETED = "DELETED"; //$NON-NLS-1$
					SimpleLookupTable indexedFileNames = new SimpleLookupTable(max == 0 ? 33 : max + 11);
					for (int i = 0; i < max; i++)
						indexedFileNames.put(results[i].getPath(), DELETED);
					for (Enumeration e = zip.entries(); e.hasMoreElements();) {
						// iterate each entry to index it
						ZipEntry ze = (ZipEntry) e.nextElement();
						if (Util.isClassFileName(ze.getName())) {
							JarFileEntryDocument entryDocument = new JarFileEntryDocument(ze, null, zipFilePath);
							indexedFileNames.put(entryDocument.getName(), EXISTS);
						}
					}
					boolean needToReindex = indexedFileNames.elementSize != max; // a new file was added
					if (!needToReindex) {
						Object[] valueTable = indexedFileNames.valueTable;
						for (int i = 0, l = valueTable.length; i < l; i++) {
							if (valueTable[i] == DELETED) {
								needToReindex = true; // a file was deleted so re-index
								break;
							}
						}
						if (!needToReindex) {
							if (JobManager.VERBOSE)
								JobManager.verbose("-> no indexing required (index is consistent with library) for " //$NON-NLS-1$
								+ zip.getName() + " (" //$NON-NLS-1$
								+ (System.currentTimeMillis() - initialTime) + "ms)"); //$NON-NLS-1$
							return true;
						}
					}
				}

				// Index the jar for the first time or reindex the jar in case the previous index file has been corrupted
				// index already existed: recreate it so that we forget about previous entries
				index = manager.recreateIndex(this.indexPath);
				for (Enumeration e = zip.entries(); e.hasMoreElements();) {
					if (this.isCancelled) {
						if (JobManager.VERBOSE)
							JobManager.verbose("-> indexing of " + zip.getName() + " has been cancelled"); //$NON-NLS-1$ //$NON-NLS-2$
						return false;
					}

					// iterate each entry to index it
					ZipEntry ze = (ZipEntry) e.nextElement();
					if (Util.isClassFileName(ze.getName())) {
						byte[] classFileBytes = org.eclipse.jdt.internal.compiler.util.Util.getZipEntryByteContent(ze, zip);
						// Add the name of the file to the index
						index.add(
							new JarFileEntryDocument(ze, classFileBytes, zipFilePath),
							new BinaryIndexer(true));
					}
				}
				this.manager.saveIndex(index);
				if (JobManager.VERBOSE)
					JobManager.verbose("-> done indexing of " //$NON-NLS-1$
						+ zip.getName() + " (" //$NON-NLS-1$
						+ (System.currentTimeMillis() - initialTime) + "ms)"); //$NON-NLS-1$
			} finally {
				if (zip != null) {
					if (JavaModelManager.ZIP_ACCESS_VERBOSE)
						System.out.println("(" + Thread.currentThread() + ") [AddJarFileToIndex.execute()] Closing ZipFile " + zip); //$NON-NLS-1$	//$NON-NLS-2$
					zip.close();
				}
				monitor.exitWrite(); // free write lock
			}
		} catch (IOException e) {
			if (JobManager.VERBOSE) {
				JobManager.verbose("-> failed to index " + this.indexPath + " because of the following exception:"); //$NON-NLS-1$ //$NON-NLS-2$
				e.printStackTrace();
			}
			manager.removeIndex(this.indexPath);
			return false;
		}
		return true;
	}
	protected Integer updatedIndexState() {
		return IndexManager.REBUILDING_STATE;
	}
	public String toString() {
		return "indexing " + this.indexPath.toString(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5312.java