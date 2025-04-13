error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7116.java
text:
```scala
r@@eturn new JavaModelStatus(IJavaModelStatusConstants.INVALID_SIBLING, this.sibling.toString());

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
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.util.Messages;

public class CopyPackageFragmentRootOperation extends JavaModelOperation {
	IPath destination;
	int updateResourceFlags;
	int updateModelFlags;
	IClasspathEntry sibling;

	public CopyPackageFragmentRootOperation(
		IPackageFragmentRoot root,
		IPath destination,
		int updateResourceFlags,
		int updateModelFlags,
		IClasspathEntry sibling) {
			
		super(root);
		this.destination = destination;
		this.updateResourceFlags = updateResourceFlags;
		this.updateModelFlags = updateModelFlags;
		this.sibling = sibling;
	}
	protected void executeOperation() throws JavaModelException {
		
		IPackageFragmentRoot root = (IPackageFragmentRoot)this.getElementToProcess();
		IClasspathEntry rootEntry = root.getRawClasspathEntry();
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		// copy resource
		if (!root.isExternal() && (this.updateModelFlags & IPackageFragmentRoot.NO_RESOURCE_MODIFICATION) == 0) {
			copyResource(root, rootEntry, workspaceRoot);
		}
		
		// update classpath if needed
		if ((this.updateModelFlags & IPackageFragmentRoot.DESTINATION_PROJECT_CLASSPATH) != 0) {
			addEntryToClasspath(rootEntry, workspaceRoot);
		}
	}
	protected void copyResource(
		IPackageFragmentRoot root,
		IClasspathEntry rootEntry,
		final IWorkspaceRoot workspaceRoot)
		throws JavaModelException {
		final char[][] exclusionPatterns = ((ClasspathEntry)rootEntry).fullExclusionPatternChars();
		IResource rootResource = root.getResource();
		if (root.getKind() == IPackageFragmentRoot.K_BINARY || exclusionPatterns == null) {
			try {
				IResource destRes;
				if ((this.updateModelFlags & IPackageFragmentRoot.REPLACE) != 0) {
					if (rootEntry.getPath().equals(this.destination)) return;
					if ((destRes = workspaceRoot.findMember(this.destination)) != null) {
						destRes.delete(this.updateResourceFlags, progressMonitor);
					}
				}
				rootResource.copy(this.destination, this.updateResourceFlags, progressMonitor);
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
							proxy.requestResource().copy(destPath, updateResourceFlags, progressMonitor);
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
						proxy.requestResource().copy(destPath, updateResourceFlags, progressMonitor);
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
	protected void addEntryToClasspath(IClasspathEntry rootEntry, IWorkspaceRoot workspaceRoot) throws JavaModelException {
		
		IProject destProject = workspaceRoot.getProject(this.destination.segment(0));
		IJavaProject jProject = JavaCore.create(destProject);
		IClasspathEntry[] classpath = jProject.getRawClasspath();
		int length = classpath.length;
		IClasspathEntry[] newClasspath;
		
		// case of existing entry and REPLACE was specified
		if ((this.updateModelFlags & IPackageFragmentRoot.REPLACE) != 0) {
			// find existing entry
			for (int i = 0; i < length; i++) {
				if (this.destination.equals(classpath[i].getPath())) {
					newClasspath = new IClasspathEntry[length];
					System.arraycopy(classpath, 0, newClasspath, 0, length);
					newClasspath[i] = copy(rootEntry);
					jProject.setRawClasspath(newClasspath, progressMonitor);
					return;
				}
			}
		} 
		
		// other cases
		int position;
		if (this.sibling == null) {
			// insert at the end
			position = length;
		} else {
			// insert before sibling
			position = -1;
			for (int i = 0; i < length; i++) {
				if (this.sibling.equals(classpath[i])) {
					position = i;
					break;
				}
			}
		}
		if (position == -1) {
			throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_SIBLING, this.sibling.toString()));
		}
		newClasspath = new IClasspathEntry[length+1];
		if (position != 0) {
			System.arraycopy(classpath, 0, newClasspath, 0, position);
		}
		if (position != length) {
			System.arraycopy(classpath, position, newClasspath, position+1, length-position);
		}
		IClasspathEntry newEntry = copy(rootEntry);
		newClasspath[position] = newEntry;
		jProject.setRawClasspath(newClasspath, progressMonitor);
	}
	/*
	 * Copies the given classpath entry replacing its path with the destination path
	 * if it is a source folder or a library.
	 */
	protected IClasspathEntry copy(IClasspathEntry entry) throws JavaModelException {
		switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_CONTAINER:
				return JavaCore.newContainerEntry(entry.getPath(), entry.getAccessRules(), entry.getExtraAttributes(), entry.isExported());
			case IClasspathEntry.CPE_LIBRARY:
				try {
					return JavaCore.newLibraryEntry(this.destination, entry.getSourceAttachmentPath(), entry.getSourceAttachmentRootPath(), entry.getAccessRules(), entry.getExtraAttributes(), entry.isExported());
				} catch (AssertionFailedException e) {
					IJavaModelStatus status = new JavaModelStatus(IJavaModelStatusConstants.INVALID_PATH, e.getMessage());
					throw new JavaModelException(status);
				}
			case IClasspathEntry.CPE_PROJECT:
				return JavaCore.newProjectEntry(entry.getPath(), entry.getAccessRules(), entry.combineAccessRules(), entry.getExtraAttributes(), entry.isExported());
			case IClasspathEntry.CPE_SOURCE:
				return JavaCore.newSourceEntry(this.destination, entry.getInclusionPatterns(), entry.getExclusionPatterns(), entry.getOutputLocation(), entry.getExtraAttributes());
			case IClasspathEntry.CPE_VARIABLE:
				try {
					return JavaCore.newVariableEntry(entry.getPath(), entry.getSourceAttachmentPath(), entry.getSourceAttachmentRootPath(), entry.getAccessRules(), entry.getExtraAttributes(), entry.isExported());
				} catch (AssertionFailedException e) {
					IJavaModelStatus status = new JavaModelStatus(IJavaModelStatusConstants.INVALID_PATH, e.getMessage());
					throw new JavaModelException(status);
				}
			default:
				throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, this.getElementToProcess()));
		}
	}
	public IJavaModelStatus verify() {
		IJavaModelStatus status = super.verify();
		if (!status.isOK()) {
			return status;
		}
		IPackageFragmentRoot root = (IPackageFragmentRoot)getElementToProcess();
		if (root == null || !root.exists()) {
			return new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, root);
		}

		IResource resource = root.getResource();
		if (resource instanceof IFolder) {
			if (resource.isLinked()) {
				return new JavaModelStatus(IJavaModelStatusConstants.INVALID_RESOURCE, root);
			}
		}

		if ((this.updateModelFlags & IPackageFragmentRoot.DESTINATION_PROJECT_CLASSPATH) != 0) {
			String destProjectName = this.destination.segment(0);
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(destProjectName);
			if (JavaProject.hasJavaNature(project)) {
				try {
					IJavaProject destProject = JavaCore.create(project);
					IClasspathEntry[] destClasspath = destProject.getRawClasspath();
					boolean foundSibling = false;
					boolean foundExistingEntry = false;
					for (int i = 0, length = destClasspath.length; i < length; i++) {
						IClasspathEntry entry = destClasspath[i];
						if (entry.equals(this.sibling)) {
							foundSibling = true;
							break;
						}
						if (entry.getPath().equals(this.destination)) {
							foundExistingEntry = true;
						}
					}
					if (this.sibling != null && !foundSibling) {
						return new JavaModelStatus(IJavaModelStatusConstants.INVALID_SIBLING, this.sibling == null ? "null" : this.sibling.toString()); //$NON-NLS-1$
					}
					if (foundExistingEntry && (this.updateModelFlags & IPackageFragmentRoot.REPLACE) == 0) {
						return new JavaModelStatus(
							IJavaModelStatusConstants.NAME_COLLISION, 
							Messages.bind(Messages.status_nameCollision, new String[] {this.destination.toString()})); 
					}
				} catch (JavaModelException e) {
					return e.getJavaModelStatus();
				}
			}
		}

		return JavaModelStatus.VERIFIED_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7116.java