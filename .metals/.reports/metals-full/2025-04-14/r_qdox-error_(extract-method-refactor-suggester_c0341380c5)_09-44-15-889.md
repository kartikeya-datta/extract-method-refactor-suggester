error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4433.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4433.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4433.java
text:
```scala
S@@ystem.out.println("INDEX ("+ Thread.currentThread()+"): " + zip.getName()); //$NON-NLS-1$//$NON-NLS-2$

package org.eclipse.jdt.internal.core.search.indexing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.internal.core.search.processing.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.index.impl.*;

import java.io.*;
import java.util.zip.*;
import java.util.*;

import org.eclipse.jdt.internal.core.Util;

class AddJarFileToIndex implements IJob {

	IndexManager manager;
	String projectName;
	IFile resource;
	private String toString;
	IPath path;

	public AddJarFileToIndex(
		IFile resource,
		IndexManager manager,
		String projectName) {
		this.resource = resource;
		this.path = resource.getFullPath();
		this.manager = manager;
		this.projectName = projectName;
	}
	public boolean belongsTo(String jobFamily) {
		return jobFamily.equals(projectName);
	}
	public boolean execute(IProgressMonitor progressMonitor) {
		
		if (progressMonitor != null && progressMonitor.isCanceled()) return COMPLETE;
		try {
			if (this.resource != null) {
				if (!this.resource.isLocal(IResource.DEPTH_ZERO)) {
					return FAILED;
				}
			}
			IPath indexedPath = this.path;
			// if index already cached, then do not perform any check
			IIndex index = (IIndex) manager.getIndex(indexedPath, false);
			if (index != null)
				return COMPLETE;

			index = manager.getIndex(indexedPath);
			if (index == null)
				return COMPLETE;
			ReadWriteMonitor monitor = manager.getMonitorFor(index);
			if (monitor == null)
				return COMPLETE; // index got deleted since acquired
			ZipFile zip = null;
			try {
				// this path will be a relative path to the workspace in case the zipfile in the workspace otherwise it will be a path in the
				// local file system
				Path zipFilePath = null;

				monitor.enterWrite(); // ask permission to write
				if (resource != null) {
					IPath location = this.resource.getLocation();
					if (location == null)
						return FAILED;
					zip = new ZipFile(location.toFile());
					zipFilePath = (Path) this.resource.getFullPath().makeRelative();
					// absolute path relative to the workspace
				} else {
					zip = new ZipFile(this.path.toFile());
					zipFilePath = (Path) this.path;
					// absolute path relative to the local file system
					// make it a canonical path to avoid duplicate entries
					zipFilePath = (Path) JavaProject.canonicalizedPath(zipFilePath);
				}

				if (JobManager.VERBOSE)
					System.out.println("INDEX ("+ Thread.currentThread()+"): " + zip.getName()); //$NON-NLS-1$
				long initialTime = System.currentTimeMillis();

				final Hashtable indexedFileNames = new Hashtable(100);
				IQueryResult[] results = index.queryInDocumentNames(""); // all file names //$NON-NLS-1$
				int resultLength = results == null ? 0 : results.length;
				if (resultLength != 0) {
					/* check integrity of the existing index file
					 * if the length is equal to 0, we want to index the whole jar again
					 * If not, then we want to check that there is no missing entry, if
					 * one entry is missing then we 
					 */
					for (int i = 0; i < resultLength; i++) {
						String fileName = results[i].getPath();
						indexedFileNames.put(fileName, fileName);
					}
					boolean needToReindex = false;
					for (Enumeration e = zip.entries(); e.hasMoreElements();) {
						// iterate each entry to index it
						ZipEntry ze = (ZipEntry) e.nextElement();
						if (Util.isClassFileName(ze.getName())) {
							JarFileEntryDocument entryDocument =
								new JarFileEntryDocument(ze, null, zipFilePath);
							if (indexedFileNames.remove(entryDocument.getName()) == null) {
								needToReindex = true;
								break;
							}
						}
					}
					if (!needToReindex && indexedFileNames.size() == 0) {
						return COMPLETE;
					}
				}

				/*
				 * Index the jar for the first time or reindex the jar in case the previous index file has been corrupted
				 */
				if (index != null) {
					// index already existed: recreate it so that we forget about previous entries
					index = manager.recreateIndex(indexedPath);
				}
				for (Enumeration e = zip.entries(); e.hasMoreElements();) {
					// iterate each entry to index it
					ZipEntry ze = (ZipEntry) e.nextElement();
					if (Util.isClassFileName(ze.getName())) {
						byte[] classFileBytes =
							org.eclipse.jdt.internal.compiler.util.Util.getZipEntryByteContent(ze, zip);
						// Add the name of the file to the index
						index.add(
							new JarFileEntryDocument(ze, classFileBytes, zipFilePath),
							new BinaryIndexer(true));
					}
				}
				if (JobManager.VERBOSE)
					System.out.println("INDEX : " //$NON-NLS-1$
					+zip.getName() + " COMPLETE in " //$NON-NLS-1$
					+ (System.currentTimeMillis() - initialTime) + " ms"); //$NON-NLS-1$
			} finally {
				if (zip != null)
					zip.close();
				monitor.exitWrite(); // free write lock
			}
		} catch (IOException e) {
			return FAILED;
		}
		return COMPLETE;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (10/10/00 1:27:18 PM)
	 * @return java.lang.String
	 */
	public String toString() {
		if (toString == null) {
			if (resource != null) {
				IPath location = resource.getLocation();
				if (location == null) {
					toString = "indexing "; //$NON-NLS-1$
				} else {
					toString = "indexing " + location.toFile().toString(); //$NON-NLS-1$
				}
			} else {
				toString = "indexing " + this.path.toFile().toString(); //$NON-NLS-1$
			}
		}
		return toString;
	}

	public AddJarFileToIndex(
		IPath path,
		IndexManager manager,
		String projectName) {
		// external JAR scenario - no resource
		this.path = path;
		this.manager = manager;
		this.projectName = projectName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4433.java