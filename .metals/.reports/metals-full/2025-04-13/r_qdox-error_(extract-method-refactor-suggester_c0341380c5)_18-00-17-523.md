error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/925.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/925.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/925.java
text:
```scala
s@@etAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;

public class MovePackageFragmentRootOperation extends CopyPackageFragmentRootOperation {
	/*
	 * Renames the classpath entries equal to the given path in the given project.
	 * If an entry with the destination path already existed, remove it.
	 */
	protected void renameEntryInClasspath(IPath rootPath, IJavaProject project) throws JavaModelException {
			
		IClasspathEntry[] classpath = project.getRawClasspath();
		IClasspathEntry[] newClasspath = null;
		int cpLength = classpath.length;
		int newCPIndex = -1;

		for (int i = 0; i < cpLength; i++) {
			IClasspathEntry entry = classpath[i];
			IPath entryPath = entry.getPath();
			if (rootPath.equals(entryPath)) {
				// rename entry
				if (newClasspath == null) {
					newClasspath = new IClasspathEntry[cpLength];
					System.arraycopy(classpath, 0, newClasspath, 0, i);
					newCPIndex = i;
				}
				newClasspath[newCPIndex++] = copy(entry);
			} else if (this.destination.equals(entryPath)) {
				// remove entry equals to destination
				if (newClasspath == null) {
					newClasspath = new IClasspathEntry[cpLength];
					System.arraycopy(classpath, 0, newClasspath, 0, i);
					newCPIndex = i;
				}
			} else if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				// update exclusion/inclusion patterns
				IPath projectRelativePath = rootPath.removeFirstSegments(1);
				IPath[] newExclusionPatterns = renamePatterns(projectRelativePath, entry.getExclusionPatterns());
				IPath[] newInclusionPatterns = renamePatterns(projectRelativePath, entry.getInclusionPatterns());
				if (newExclusionPatterns != null || newInclusionPatterns != null) {
					if (newClasspath == null) {
						newClasspath = new IClasspathEntry[cpLength];
						System.arraycopy(classpath, 0, newClasspath, 0, i);
						newCPIndex = i;
					}
					newClasspath[newCPIndex++] = 
						JavaCore.newSourceEntry(
							entry.getPath(), 
							newInclusionPatterns == null ? entry.getInclusionPatterns() : newInclusionPatterns, 
							newExclusionPatterns == null ? entry.getExclusionPatterns() : newExclusionPatterns, 
							entry.getOutputLocation(), 
							entry.getExtraAttributes());
				} else if (newClasspath != null) {
					newClasspath[newCPIndex++] = entry;
				}
			} else if (newClasspath != null) {
				newClasspath[newCPIndex++] = entry;
			}
		}
		
		if (newClasspath != null) {
			if (newCPIndex < newClasspath.length) {
				System.arraycopy(newClasspath, 0, newClasspath = new IClasspathEntry[newCPIndex], 0, newCPIndex);
			}
			IJavaModelStatus status = JavaConventions.validateClasspath(project, newClasspath, project.getOutputLocation());
			if (status.isOK())
				project.setRawClasspath(newClasspath, progressMonitor);
			// don't update classpath if status is not ok to avoid JavaModelException (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=129991)
		}
	}

	private IPath[] renamePatterns(IPath rootPath, IPath[] patterns) {
		IPath[] newPatterns = null;
		int newPatternsIndex = -1;
		for (int i = 0, length = patterns.length; i < length; i++) {
			IPath pattern = patterns[i];
			if (pattern.equals(rootPath)) {
				if (newPatterns == null) {
					newPatterns = new IPath[length];
					System.arraycopy(patterns, 0, newPatterns, 0, i);
					newPatternsIndex = i;
				}
				IPath newPattern = this.destination.removeFirstSegments(1);
				if (pattern.hasTrailingSeparator())
					newPattern = newPattern.addTrailingSeparator();
				newPatterns[newPatternsIndex++] = newPattern;
			}
		}
		return newPatterns;
	}

	public MovePackageFragmentRootOperation(
		IPackageFragmentRoot root,
		IPath destination,
		int updateResourceFlags,
		int updateModelFlags,
		IClasspathEntry sibling) {
			
		super(
			root,
			destination,
			updateResourceFlags,
			updateModelFlags,
			sibling);
	}
	protected void executeOperation() throws JavaModelException {
		
		IPackageFragmentRoot root = (IPackageFragmentRoot)this.getElementToProcess();
		IClasspathEntry rootEntry = root.getRawClasspathEntry();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		
		// move resource
		if (!root.isExternal() && (this.updateModelFlags & IPackageFragmentRoot.NO_RESOURCE_MODIFICATION) == 0) {
			moveResource(root, rootEntry, workspaceRoot);
		}
		
		// update refering projects classpath excluding orignating project
		IJavaProject originatingProject = root.getJavaProject();
		if ((this.updateModelFlags & IPackageFragmentRoot.OTHER_REFERRING_PROJECTS_CLASSPATH) != 0) {
			updateReferringProjectClasspaths(rootEntry.getPath(), originatingProject);
		}

		boolean isRename = this.destination.segment(0).equals(originatingProject.getElementName());
		boolean updateOriginating = (this.updateModelFlags & IPackageFragmentRoot.ORIGINATING_PROJECT_CLASSPATH) != 0;
		boolean updateDestination = (this.updateModelFlags & IPackageFragmentRoot.DESTINATION_PROJECT_CLASSPATH) != 0;

		// update originating classpath
		if (updateOriginating) {
			if (isRename && updateDestination) {
				renameEntryInClasspath(rootEntry.getPath(), originatingProject);
			} else {
				removeEntryFromClasspath(rootEntry.getPath(), originatingProject);
			}
		}
		
		// update destination classpath
		if (updateDestination) {
			if (!isRename || !updateOriginating) {
				addEntryToClasspath(rootEntry, workspaceRoot);
			}  // else reference has been updated when updating originating project classpath
		}
	}
	protected void moveResource(
		IPackageFragmentRoot root,
		IClasspathEntry rootEntry,
		final IWorkspaceRoot workspaceRoot)
		throws JavaModelException {
			
		final char[][] exclusionPatterns = ((ClasspathEntry)rootEntry).fullExclusionPatternChars();
		IResource rootResource = root.getResource();
		if (rootEntry.getEntryKind() != IClasspathEntry.CPE_SOURCE || exclusionPatterns == null) {
			try {
				IResource destRes;
				if ((this.updateModelFlags & IPackageFragmentRoot.REPLACE) != 0
						&& (destRes = workspaceRoot.findMember(this.destination)) != null) {
					destRes.delete(this.updateResourceFlags, progressMonitor);
				}
				rootResource.move(this.destination, this.updateResourceFlags, progressMonitor);
			} catch (CoreException e) {
				throw new JavaModelException(e);
			}
		} else {
			final int sourceSegmentCount = rootEntry.getPath().segmentCount();
			final IFolder destFolder = workspaceRoot.getFolder(this.destination);
			final IPath[] nestedFolders = getNestedFolders(root);
			IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
				public boolean visit(IResourceProxy proxy) throws CoreException {
					if (proxy.getType() == IResource.FOLDER) {
						IPath path = proxy.requestFullPath();
						if (prefixesOneOf(path, nestedFolders)) {
							if (equalsOneOf(path, nestedFolders)) {
								// nested source folder
								return false;
							} else {
								// folder containing nested source folder
								IFolder folder = destFolder.getFolder(path.removeFirstSegments(sourceSegmentCount));
								if ((updateModelFlags & IPackageFragmentRoot.REPLACE) != 0
										&& folder.exists()) {
									return true;
								}
								folder.create(updateResourceFlags, true, progressMonitor);
								return true;
							}
						} else {
							// subtree doesn't contain any nested source folders
							IPath destPath = destination.append(path.removeFirstSegments(sourceSegmentCount));
							IResource destRes;
							if ((updateModelFlags & IPackageFragmentRoot.REPLACE) != 0
									&& (destRes = workspaceRoot.findMember(destPath)) != null) {
								destRes.delete(updateResourceFlags, progressMonitor);
							}
							proxy.requestResource().move(destPath, updateResourceFlags, progressMonitor);
							return false;
						}
					} else {
						IPath path = proxy.requestFullPath();
						IPath destPath = destination.append(path.removeFirstSegments(sourceSegmentCount));
						IResource destRes;
						if ((updateModelFlags & IPackageFragmentRoot.REPLACE) != 0
								&& (destRes = workspaceRoot.findMember(destPath)) != null) {
							destRes.delete(updateResourceFlags, progressMonitor);
						}
						proxy.requestResource().move(destPath, updateResourceFlags, progressMonitor);
						return false;
					}
				}
			};
			try {
				rootResource.accept(visitor, IResource.NONE);
			} catch (CoreException e) {
				throw new JavaModelException(e);
			}
		}
		this.setAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE); 
	}
	/*
	 * Renames the classpath entries equal to the given path in all Java projects.
	 */
	protected void updateReferringProjectClasspaths(IPath rootPath, IJavaProject projectOfRoot) throws JavaModelException {
		IJavaModel model = this.getJavaModel();
		IJavaProject[] projects = model.getJavaProjects();
		for (int i = 0, length = projects.length; i < length; i++) {
			IJavaProject project = projects[i];
			if (project.equals(projectOfRoot)) continue;
			renameEntryInClasspath(rootPath, project);
		}
	}
	/*
	 * Removes the classpath entry equal to the given path from the given project's classpath.
	 */
	protected void removeEntryFromClasspath(IPath rootPath, IJavaProject project) throws JavaModelException {
		
		IClasspathEntry[] classpath = project.getRawClasspath();
		IClasspathEntry[] newClasspath = null;
		int cpLength = classpath.length;
		int newCPIndex = -1;
		
		for (int i = 0; i < cpLength; i++) {
			IClasspathEntry entry = classpath[i];
			if (rootPath.equals(entry.getPath())) {
				if (newClasspath == null) {
					newClasspath = new IClasspathEntry[cpLength];
					System.arraycopy(classpath, 0, newClasspath, 0, i);
					newCPIndex = i;
				}
			} else if (newClasspath != null) {
				newClasspath[newCPIndex++] = entry;
			}
		}
		
		if (newClasspath != null) {
			if (newCPIndex < newClasspath.length) {
				System.arraycopy(newClasspath, 0, newClasspath = new IClasspathEntry[newCPIndex], 0, newCPIndex);
			}
			project.setRawClasspath(newClasspath, progressMonitor);
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/925.java