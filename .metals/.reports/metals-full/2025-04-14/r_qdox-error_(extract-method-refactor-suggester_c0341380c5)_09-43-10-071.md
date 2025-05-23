error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2856.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2856.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2856.java
text:
```scala
S@@tring[] paths = index.queryDocumentNames(""); // all file names //$NON-NLS-1$

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
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.ClasspathEntry;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.core.index.Index;
import org.eclipse.jdt.internal.core.search.JavaSearchDocument;
import org.eclipse.jdt.internal.core.search.processing.JobManager;
import org.eclipse.jdt.internal.core.util.SimpleLookupTable;
import org.eclipse.jdt.internal.core.util.Util;

public class IndexAllProject extends IndexRequest {
	IProject project;

	public IndexAllProject(IProject project, IndexManager manager) {
		super(project.getFullPath(), manager);
		this.project = project;
	}
	public boolean equals(Object o) {
		if (o instanceof IndexAllProject)
			return this.project.equals(((IndexAllProject) o).project);
		return false;
	}
	/**
	 * Ensure consistency of a project index. Need to walk all nested resources,
	 * and discover resources which have either been changed, added or deleted
	 * since the index was produced.
	 */
	public boolean execute(IProgressMonitor progressMonitor) {

		if (this.isCancelled || progressMonitor != null && progressMonitor.isCanceled()) return true;
		if (!project.isAccessible()) return true; // nothing to do

		Index index = this.manager.getIndexForUpdate(this.containerPath, true, /*reuse index file*/ true /*create if none*/);
		if (index == null) return true;
		ReadWriteMonitor monitor = this.manager.getMonitorFor(index);
		if (monitor == null) return true; // index got deleted since acquired

		try {
			monitor.enterRead(); // ask permission to read
			saveIfNecessary(index, monitor);

			String[] paths = index.queryInDocumentNames(""); // all file names //$NON-NLS-1$
			int max = paths == null ? 0 : paths.length;
			final SimpleLookupTable indexedFileNames = new SimpleLookupTable(max == 0 ? 33 : max + 11);
			final String OK = "OK"; //$NON-NLS-1$
			final String DELETED = "DELETED"; //$NON-NLS-1$
			for (int i = 0; i < max; i++)
				indexedFileNames.put(paths[i], DELETED);
			final long indexLastModified = max == 0 ? 0L : index.getIndexFile().lastModified();

			JavaProject javaProject = (JavaProject)JavaCore.create(this.project);
			// Do not create marker nor log problems while getting raw classpath (see bug 41859)
			IClasspathEntry[] entries = javaProject.getRawClasspath(false, false);
			IWorkspaceRoot root = this.project.getWorkspace().getRoot();
			for (int i = 0, length = entries.length; i < length; i++) {
				if (this.isCancelled) return false;

				IClasspathEntry entry = entries[i];
				if ((entry.getEntryKind() == IClasspathEntry.CPE_SOURCE)) { // Index only source folders. Libraries are done as a separate job
					IResource sourceFolder = root.findMember(entry.getPath());
					if (sourceFolder != null) {
						
						// collect output locations if source is project (see http://bugs.eclipse.org/bugs/show_bug.cgi?id=32041)
						final HashSet outputs = new HashSet();
						if (sourceFolder.getType() == IResource.PROJECT) {
							// Do not create marker nor log problems while getting output location (see bug 41859)
							outputs.add(javaProject.getOutputLocation(false, false));
							for (int j = 0; j < length; j++) {
								IPath output = entries[j].getOutputLocation();
								if (output != null) {
									outputs.add(output);
								}
							}
						}
						final boolean hasOutputs = !outputs.isEmpty();
						
						final char[][] patterns = ((ClasspathEntry) entry).fullExclusionPatternChars();
						if (max == 0) {
							sourceFolder.accept(
								new IResourceProxyVisitor() {
									public boolean visit(IResourceProxy proxy) {
										if (isCancelled) return false;
										switch(proxy.getType()) {
											case IResource.FILE :
												if (org.eclipse.jdt.internal.compiler.util.Util.isJavaFileName(proxy.getName())) {
													IFile file = (IFile) proxy.requestResource();
													if (file.getLocation() != null && (patterns == null || !Util.isExcluded(file, patterns)))
														indexedFileNames.put(new JavaSearchDocument(file, null).getPath(), file);
												}
												return false;
											case IResource.FOLDER :
												if (patterns != null && Util.isExcluded(proxy.requestResource(), patterns))
													return false;
												if (hasOutputs && outputs.contains(proxy.requestFullPath())) {
													return false;
												}
										}
										return true;
									}
								},
								IResource.NONE
							);
						} else {
							sourceFolder.accept(
								new IResourceProxyVisitor() {
									public boolean visit(IResourceProxy proxy) {
										if (isCancelled) return false;
										switch(proxy.getType()) {
											case IResource.FILE :
												if (org.eclipse.jdt.internal.compiler.util.Util.isJavaFileName(proxy.getName())) {
													IFile file = (IFile) proxy.requestResource();
													IPath location = file.getLocation();
													if (location != null && (patterns == null || !Util.isExcluded(file, patterns))) {
														String path = new JavaSearchDocument(file, null).getPath();
														indexedFileNames.put(path,
															indexedFileNames.get(path) == null || indexLastModified < location.toFile().lastModified()
																? (Object) file
																: (Object) OK);
													}
												}
												return false;
											case IResource.FOLDER :
												if (patterns != null && Util.isExcluded(proxy.requestResource(), patterns))
													return false;
												if (hasOutputs && outputs.contains(proxy.requestFullPath())) {
													return false;
												}
										}
										return true;
									}
								},
								IResource.NONE
							);
						}
					}
				}
			}

			Object[] names = indexedFileNames.keyTable;
			Object[] values = indexedFileNames.valueTable;
			for (int i = 0, length = names.length; i < length; i++) {
				String name = (String) names[i];
				if (name != null) {
					if (this.isCancelled) return false;

					Object value = values[i];
					if (value != OK) {
						if (value == DELETED)
							this.manager.remove(name, this.containerPath);
						else
							this.manager.addSource((IFile) value, this.containerPath);
					}
				}
			}

			// request to save index when all cus have been indexed... also sets state to SAVED_STATE
			this.manager.request(new SaveIndex(this.containerPath, this.manager));
		} catch (CoreException e) {
			if (JobManager.VERBOSE) {
				JobManager.verbose("-> failed to index " + this.project + " because of the following exception:"); //$NON-NLS-1$ //$NON-NLS-2$
				e.printStackTrace();
			}
			this.manager.removeIndex(this.containerPath);
			return false;
		} catch (IOException e) {
			if (JobManager.VERBOSE) {
				JobManager.verbose("-> failed to index " + this.project + " because of the following exception:"); //$NON-NLS-1$ //$NON-NLS-2$
				e.printStackTrace();
			}
			this.manager.removeIndex(this.containerPath);
			return false;
		} finally {
			monitor.exitRead(); // free read lock
		}
		return true;
	}
	public int hashCode() {
		return this.project.hashCode();
	}
	protected Integer updatedIndexState() {
		return IndexManager.REBUILDING_STATE;
	}
	public String toString() {
		return "indexing project " + this.project.getFullPath(); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2856.java