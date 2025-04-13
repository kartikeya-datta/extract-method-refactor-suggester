error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3021.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3021.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3021.java
text:
```scala
I@@ClasspathEntry[] entries = javaProject.getRawClasspath(); //only interested in source folders

package org.eclipse.jdt.internal.core.search.indexing;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.index.*;
import org.eclipse.jdt.internal.core.search.processing.*;
import org.eclipse.jdt.internal.core.index.impl.*;

import java.io.*;
import java.util.*;

public class IndexAllProject extends IndexRequest implements IResourceVisitor {
	IProject project;
	IndexManager manager;
	Hashtable indexedFileNames;
	final String OK = "OK"; //$NON-NLS-1$
	final String DELETED = "DELETED"; //$NON-NLS-1$
	long indexLastModified;
	public IndexAllProject(IProject project, IndexManager manager) {
		this.project = project;
		this.manager = manager;
		this.timeStamp = project.getModificationStamp();
	}
	public boolean belongsTo(String jobFamily) {
		return jobFamily.equals(project.getName());
	}
public boolean equals(Object o) {
	if (!(o instanceof IndexAllProject)) return false;
	return this.project.equals(((IndexAllProject)o).project);
}
public int hashCode() {
	return this.project.hashCode();
}		
	/**
	 * Ensure consistency of a project index. Need to walk all nested resources,
	 * and discover resources which have either been changed, added or deleted
	 * since the index was produced.
	 */
	public boolean execute(IProgressMonitor progressMonitor) {
		
		if (progressMonitor != null && progressMonitor.isCanceled()) return COMPLETE;

		if (!project.isOpen())
			return COMPLETE; // nothing to do

		IIndex index = manager.getIndex(project.getFullPath());
		if (index == null)
			return COMPLETE;
		ReadWriteMonitor monitor = manager.getMonitorFor(index);
		if (monitor == null)
			return COMPLETE; // index got deleted since acquired
		try {
			monitor.enterRead(); // ask permission to read

			/* if index has changed, commit these before querying */
			if (index.hasChanged()) {
				try {
					monitor.exitRead(); // free read lock
					monitor.enterWrite(); // ask permission to write
					if (IndexManager.VERBOSE)
						System.out.println("-> merging index : " + index.getIndexFile()); //$NON-NLS-1$
					index.save();
				} catch (IOException e) {
					return FAILED;
				} finally {
					monitor.exitWrite(); // finished writing
					monitor.enterRead(); // reacquire read permission
				}
			}
			this.indexLastModified = index.getIndexFile().lastModified();

			this.indexedFileNames = new Hashtable(100);
			IQueryResult[] results = index.queryInDocumentNames(""); // all file names //$NON-NLS-1$
			for (int i = 0, max = results == null ? 0 : results.length; i < max; i++) {
				String fileName = results[i].getPath();
				this.indexedFileNames.put(fileName, DELETED);
			}
			JavaCore javaCore = JavaCore.getJavaCore();
			IJavaProject javaProject = javaCore.create(this.project);
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
			IWorkspaceRoot root = this.project.getWorkspace().getRoot();
			for (int i = 0, length = entries.length; i < length; i++) {
				if (this.isCancelled) return FAILED;
				
				IClasspathEntry entry = entries[i];
				// Index only the project's source folders.
				// Indexing of libraries is done in a separate job
				if ((entry.getEntryKind() == IClasspathEntry.CPE_SOURCE)) {
					IPath entryPath = entry.getPath();
					IResource sourceFolder = root.findMember(entryPath);
					if (sourceFolder != null) {
						sourceFolder.accept(this);
					}
				}
			}
			
			Enumeration names = indexedFileNames.keys();
			while (names.hasMoreElements()) {
				if (this.isCancelled) return FAILED;

				String name = (String) names.nextElement();
				Object value = indexedFileNames.get(name);
				if (value instanceof IFile) {
					manager.add((IFile) value, this.project.getFullPath());
				} else if (value == DELETED) {
					manager.remove(name, this.project.getFullPath());
				}
			}
		} catch (CoreException e) {
			return FAILED;
		} catch (IOException e) {
			return FAILED;
		} finally {
			monitor.exitRead(); // free read lock
		}
		return COMPLETE;
	}
	public String toString() {
		return "indexing project " + project.getFullPath(); //$NON-NLS-1$
	}
	public boolean visit(IResource resource) {
		if (this.isCancelled) return false;
		
		if (resource.getType() == IResource.FILE) {
			String extension = resource.getFileExtension();
			if ((extension != null) && extension.equalsIgnoreCase("java")) { //$NON-NLS-1$
				IPath path = resource.getLocation();
				if (path != null) {
					File resourceFile = path.toFile();
					String name = new IFileDocument((IFile) resource).getName();
					if (this.indexedFileNames.get(name) == null) {
						this.indexedFileNames.put(name, resource);
					} else {
						this.indexedFileNames.put(
							name,
							resourceFile.lastModified() > this.indexLastModified
								? (Object) resource
								: (Object) OK);
					}
				}
			}
			return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3021.java