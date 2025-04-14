error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/808.java
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.*;

public class DeletePackageFragmentRootOperation extends JavaModelOperation {

	int updateResourceFlags;
	int updateModelFlags;

	public DeletePackageFragmentRootOperation(
		IPackageFragmentRoot root,
		int updateResourceFlags,
		int updateModelFlags) {
			
		super(root);
		this.updateResourceFlags = updateResourceFlags;
		this.updateModelFlags = updateModelFlags;
	}

	protected void executeOperation() throws JavaModelException {
		
		IPackageFragmentRoot root = (IPackageFragmentRoot)this.getElementToProcess();
		IClasspathEntry rootEntry = root.getRawClasspathEntry();
		
		// remember olds roots
		DeltaProcessor deltaProcessor = JavaModelManager.getJavaModelManager().getDeltaProcessor();
		if (deltaProcessor.oldRoots == null)
			deltaProcessor.oldRoots = new HashMap();
		
		// update classpath if needed
		if ((updateModelFlags & IPackageFragmentRoot.ORIGINATING_PROJECT_CLASSPATH) != 0) {
			updateProjectClasspath(rootEntry.getPath(), root.getJavaProject(), deltaProcessor.oldRoots);
		}
		if ((updateModelFlags & IPackageFragmentRoot.OTHER_REFERRING_PROJECTS_CLASSPATH) != 0) {
			updateReferringProjectClasspaths(rootEntry.getPath(), root.getJavaProject(), deltaProcessor.oldRoots);
		}
		
		// delete resource
		if (!root.isExternal() && (this.updateModelFlags & IPackageFragmentRoot.NO_RESOURCE_MODIFICATION) == 0) {
			deleteResource(root, rootEntry);
		}
	}

	protected void deleteResource(
		IPackageFragmentRoot root,
		IClasspathEntry rootEntry)
		throws JavaModelException {
		final char[][] exclusionPatterns = ((ClasspathEntry)rootEntry).fullExclusionPatternChars();
		IResource rootResource = root.getResource();
		if (rootEntry.getEntryKind() != IClasspathEntry.CPE_SOURCE || exclusionPatterns == null) {
			try {
				rootResource.delete(this.updateResourceFlags, progressMonitor);
			} catch (CoreException e) {
				throw new JavaModelException(e);
			}
		} else {
			final IPath[] nestedFolders = getNestedFolders(root);
			IResourceProxyVisitor visitor = new IResourceProxyVisitor() {
				public boolean visit(IResourceProxy proxy) throws CoreException {
					if (proxy.getType() == IResource.FOLDER) {
						IPath path = proxy.requestFullPath();
						if (prefixesOneOf(path, nestedFolders)) {
							// equals if nested source folder
							return !equalsOneOf(path, nestedFolders);
						} else {
							// subtree doesn't contain any nested source folders
							proxy.requestResource().delete(updateResourceFlags, progressMonitor);
							return false;
						}
					} else {
						proxy.requestResource().delete(updateResourceFlags, progressMonitor);
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
	 * Deletes the classpath entries equals to the given rootPath from all Java projects.
	 */
	protected void updateReferringProjectClasspaths(IPath rootPath, IJavaProject projectOfRoot, Map oldRoots) throws JavaModelException {
		IJavaModel model = this.getJavaModel();
		IJavaProject[] projects = model.getJavaProjects();
		for (int i = 0, length = projects.length; i < length; i++) {
			IJavaProject project = projects[i];
			if (project.equals(projectOfRoot)) continue;
			updateProjectClasspath(rootPath, project, oldRoots);
		}
	}

	/*
	 * Deletes the classpath entries equals to the given rootPath from the given project.
	 */
	protected void updateProjectClasspath(IPath rootPath, IJavaProject project, Map oldRoots) throws JavaModelException {
		// remember old roots
		oldRoots.put(project, project.getPackageFragmentRoots());
		
		IClasspathEntry[] classpath = project.getRawClasspath();
		IClasspathEntry[] newClasspath = null;
		int cpLength = classpath.length;
		int newCPIndex = -1;
		for (int j = 0; j < cpLength; j++) {
			IClasspathEntry entry = classpath[j];
			if (rootPath.equals(entry.getPath())) {
				if (newClasspath == null) {
					newClasspath = new IClasspathEntry[cpLength-1];
					System.arraycopy(classpath, 0, newClasspath, 0, j);
					newCPIndex = j;
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
	protected IJavaModelStatus verify() {
		IJavaModelStatus status = super.verify();
		if (!status.isOK()) {
			return status;
		}
		IPackageFragmentRoot root = (IPackageFragmentRoot) this.getElementToProcess();
		if (root == null || !root.exists()) {
			return new JavaModelStatus(IJavaModelStatusConstants.ELEMENT_DOES_NOT_EXIST, root);
		}

		IResource resource = root.getResource();
		if (resource instanceof IFolder) {
			if (resource.isLinked()) {
				return new JavaModelStatus(IJavaModelStatusConstants.INVALID_RESOURCE, root);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/808.java