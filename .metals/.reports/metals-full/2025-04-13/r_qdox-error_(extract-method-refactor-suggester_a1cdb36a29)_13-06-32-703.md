error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9058.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9058.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9058.java
text:
```scala
c@@reateProblemFor(resource, Util.bind("build.duplicateResource", id), javaBuilder.javaProject.getOption(JavaCore.CORE_JAVA_BUILD_DUPLICATE_RESOURCE, true)); //$NON-NLS-1$

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
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaModelManager;
import org.eclipse.jdt.internal.core.Util;

import java.util.*;

public class BatchImageBuilder extends AbstractImageBuilder {

protected BatchImageBuilder(JavaBuilder javaBuilder) {
	super(javaBuilder);
	this.nameEnvironment.isIncrementalBuild = false;
}

public void build() {
	if (JavaBuilder.DEBUG)
		System.out.println("FULL build"); //$NON-NLS-1$

	try {
		notifier.subTask(Util.bind("build.cleaningOutput")); //$NON-NLS-1$
		JavaModelManager.getJavaModelManager().deltaProcessor.addForRefresh(javaBuilder.javaProject);
		JavaBuilder.removeProblemsAndTasksFor(javaBuilder.currentProject);
		cleanOutputFolders();
		notifier.updateProgressDelta(0.1f);

		notifier.subTask(Util.bind("build.analyzingSources")); //$NON-NLS-1$
		ArrayList sourceFiles = new ArrayList(33);
		addAllSourceFiles(sourceFiles);
		notifier.updateProgressDelta(0.15f);

		if (sourceFiles.size() > 0) {
			SourceFile[] allSourceFiles = new SourceFile[sourceFiles.size()];
			sourceFiles.toArray(allSourceFiles);

			notifier.setProgressPerCompilationUnit(0.75f / allSourceFiles.length);
			workQueue.addAll(allSourceFiles);
			compile(allSourceFiles);
		}

		if (javaBuilder.javaProject.hasCycleMarker())
			javaBuilder.mustPropagateStructuralChanges();
	} catch (CoreException e) {
		throw internalException(e);
	} finally {
		cleanUp();
	}
}

protected void addAllSourceFiles(final ArrayList sourceFiles) throws CoreException {
	for (int i = 0, l = sourceLocations.length; i < l; i++) {
		final ClasspathMultiDirectory sourceLocation = sourceLocations[i];
		final char[][] exclusionPatterns = sourceLocation.exclusionPatterns;
		final boolean isAlsoProject = sourceLocation.sourceFolder.equals(javaBuilder.currentProject);
		sourceLocation.sourceFolder.accept(
			new IResourceProxyVisitor() {
				public boolean visit(IResourceProxy proxy) throws CoreException {
					IResource resource = null;
					if (exclusionPatterns != null) {
						resource = proxy.requestResource();
						if (Util.isExcluded(resource, exclusionPatterns)) return false;
					}
					switch(proxy.getType()) {
						case IResource.FILE :
							if (Util.isJavaFileName(proxy.getName())) {
								if (resource == null)
									resource = proxy.requestResource();
								sourceFiles.add(new SourceFile((IFile) resource, sourceLocation, encoding));
							}
							return false;
						case IResource.FOLDER :
							if (isAlsoProject && isExcludedFromProject(proxy.requestFullPath())) return false;
					}
					return true;
				}
			},
			IResource.NONE
		);
		notifier.checkCancel();
	}
}

protected void cleanOutputFolders() throws CoreException {
	boolean deleteAll = JavaCore.CLEAN.equals(
		javaBuilder.javaProject.getOption(JavaCore.CORE_JAVA_BUILD_CLEAN_OUTPUT_FOLDER, true));
	if (deleteAll) {
		ArrayList visited = new ArrayList(sourceLocations.length);
		for (int i = 0, l = sourceLocations.length; i < l; i++) {
			notifier.subTask(Util.bind("build.cleaningOutput")); //$NON-NLS-1$
			ClasspathMultiDirectory sourceLocation = sourceLocations[i];
			if (sourceLocation.hasIndependentOutputFolder) {
				IContainer outputFolder = sourceLocation.binaryFolder;
				if (!visited.contains(outputFolder)) {
					visited.add(outputFolder);
					IResource[] members = outputFolder.members(); 
					for (int j = 0, m = members.length; j < m; j++)
						members[j].delete(IResource.FORCE, null);
				}
				notifier.checkCancel();
				copyExtraResourcesBack(sourceLocation, true);
			} else {
				boolean isOutputFolder = sourceLocation.sourceFolder.equals(sourceLocation.binaryFolder);
				final char[][] exclusionPatterns =
					isOutputFolder
						? sourceLocation.exclusionPatterns
						: null; // ignore exclusionPatterns if output folder == another source folder... not this one
				sourceLocation.binaryFolder.accept(
					new IResourceProxyVisitor() {
						public boolean visit(IResourceProxy proxy) throws CoreException {
							IResource resource = null;
							if (exclusionPatterns != null) {
								resource = proxy.requestResource();
								if (Util.isExcluded(resource, exclusionPatterns)) return false;
							}
							if (proxy.getType() == IResource.FILE) {
								if (Util.isClassFileName(proxy.getName())) {
									if (resource == null)
										resource = proxy.requestResource();
									resource.delete(IResource.FORCE, null);
								}
								return false;
							}
							notifier.checkCancel();
							return true;
						}
					},
					IResource.NONE
				);
				if (!isOutputFolder) {
					notifier.checkCancel();
					copyPackages(sourceLocation);
				}
			}
			notifier.checkCancel();
		}
	} else {
		for (int i = 0, l = sourceLocations.length; i < l; i++) {
			ClasspathMultiDirectory sourceLocation = sourceLocations[i];
			if (sourceLocation.hasIndependentOutputFolder)
				copyExtraResourcesBack(sourceLocation, false);
			else if (!sourceLocation.sourceFolder.equals(sourceLocation.binaryFolder))
				copyPackages(sourceLocation); // output folder is different from source folder
			notifier.checkCancel();
		}
	}
}

protected void copyExtraResourcesBack(ClasspathMultiDirectory sourceLocation, final boolean deletedAll) throws CoreException {
	// When, if ever, does a builder need to copy resources files (not .java or .class) into the output folder?
	// If we wipe the output folder at the beginning of the build then all 'extra' resources must be copied to the output folder.

	notifier.subTask(Util.bind("build.copyingResources")); //$NON-NLS-1$
	final int segmentCount = sourceLocation.sourceFolder.getFullPath().segmentCount();
	final char[][] exclusionPatterns = sourceLocation.exclusionPatterns;
	final IContainer outputFolder = sourceLocation.binaryFolder;
	final boolean isAlsoProject = sourceLocation.sourceFolder.equals(javaBuilder.currentProject);
	sourceLocation.sourceFolder.accept(
		new IResourceProxyVisitor() {
			public boolean visit(IResourceProxy proxy) throws CoreException {
				IResource resource = null;
				switch(proxy.getType()) {
					case IResource.FILE :
						if (Util.isJavaFileName(proxy.getName()) || Util.isClassFileName(proxy.getName())) return false;

						resource = proxy.requestResource();
						if (javaBuilder.filterExtraResource(resource)) return false;
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;

						IPath partialPath = resource.getFullPath().removeFirstSegments(segmentCount);
						IResource copiedResource = outputFolder.getFile(partialPath);
						if (copiedResource.exists()) {
							if (deletedAll) {
								IResource originalResource = findOriginalResource(partialPath);
								String id = originalResource.getFullPath().removeFirstSegments(1).toString();
								createErrorFor(resource, Util.bind("build.duplicateResource", id)); //$NON-NLS-1$
								return false;
							}
							copiedResource.delete(IResource.FORCE, null); // last one wins
						}
						resource.copy(copiedResource.getFullPath(), IResource.FORCE, null);
						copiedResource.setDerived(true);
						return false;
					case IResource.FOLDER :
						resource = proxy.requestResource();
						if (javaBuilder.filterExtraResource(resource)) return false;
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;

						IPath folderPath = resource.getFullPath();
						if (isAlsoProject && isExcludedFromProject(folderPath)) return false; // the sourceFolder == project
						createFolder(folderPath.removeFirstSegments(segmentCount), outputFolder);
				}
				return true;
			}
		},
		IResource.NONE
	);
}

protected void copyPackages(ClasspathMultiDirectory sourceLocation) throws CoreException {
	final int segmentCount = sourceLocation.sourceFolder.getFullPath().segmentCount();
	final char[][] exclusionPatterns = sourceLocation.exclusionPatterns;
	final IContainer outputFolder = sourceLocation.binaryFolder;
	final boolean isAlsoProject = sourceLocation.sourceFolder.equals(javaBuilder.currentProject);
	sourceLocation.sourceFolder.accept(
		new IResourceProxyVisitor() {
			public boolean visit(IResourceProxy proxy) throws CoreException {
				switch(proxy.getType()) {
					case IResource.FILE :
						return false;
					case IResource.FOLDER :
						IResource resource = proxy.requestResource();
						if (javaBuilder.filterExtraResource(resource)) return false;
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;

						IPath folderPath = resource.getFullPath();
						if (isAlsoProject && isExcludedFromProject(folderPath)) return false; // the sourceFolder == project
						createFolder(folderPath.removeFirstSegments(segmentCount), outputFolder);
				}
				return true;
			}
		},
		IResource.NONE
	);
}

protected IResource findOriginalResource(IPath partialPath) {
	for (int i = 0, l = sourceLocations.length; i < l; i++) {
		ClasspathMultiDirectory sourceLocation = sourceLocations[i];
		if (sourceLocation.hasIndependentOutputFolder) {
			IResource originalResource = sourceLocation.sourceFolder.getFile(partialPath);
			if (originalResource.exists()) return originalResource;
		}
	}
	return null;
}

public String toString() {
	return "batch image builder for:\n\tnew state: " + newState; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9058.java