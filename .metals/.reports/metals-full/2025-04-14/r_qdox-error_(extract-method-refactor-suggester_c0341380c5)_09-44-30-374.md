error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5273.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5273.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5273.java
text:
```scala
r@@esource.copy(copiedResource.getFullPath(), IResource.FORCE | IResource.DEEP, null);

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
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.core.JavaCore;
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
		sourceLocation.sourceFolder.accept(
			new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
						return false;
					if (resource.getType() == IResource.FILE) {
						if (JavaBuilder.JAVA_EXTENSION.equalsIgnoreCase(resource.getFileExtension()))
							sourceFiles.add(new SourceFile((IFile) resource, sourceLocation, encoding));
						return false;
					}
					return true;
				}
			}
		);
		notifier.checkCancel();
	}
}

protected void cleanOutputFolders() throws CoreException {
	boolean deleteAll = JavaCore.CLEAN.equals(
		javaBuilder.javaProject.getOption(JavaCore.CORE_JAVA_BUILD_CLEAN_OUTPUT_FOLDER, true));
	ArrayList visited = new ArrayList(sourceLocations.length);
	next : for (int i = 0, l = sourceLocations.length; i < l; i++) {
		notifier.subTask(Util.bind("build.cleaningOutput")); //$NON-NLS-1$
		ClasspathMultiDirectory sourceLocation = sourceLocations[i];
		if (sourceLocation.hasIndependentOutputFolder) {
			IContainer outputFolder = sourceLocation.binaryFolder;
			if (!visited.contains(outputFolder)) {
				visited.add(outputFolder);
				if (deleteAll) {
					IResource[] members = outputFolder.members(); 
					for (int j = 0, m = members.length; j < m; j++)
						members[j].delete(IResource.FORCE, null);
				} else {
					outputFolder.accept(
						new IResourceVisitor() {
							public boolean visit(IResource resource) throws CoreException {
								if (resource.getType() == IResource.FILE) {
									if (JavaBuilder.CLASS_EXTENSION.equalsIgnoreCase(resource.getFileExtension()))
										resource.delete(IResource.FORCE, null);
									return false;
								}
								return true;
							}
						}
					);
				}
			}
			copyExtraResourcesBack(sourceLocation, deleteAll);
		} else {
			final char[][] exclusionPatterns =
				sourceLocation.sourceFolder.equals(sourceLocation.binaryFolder)
					? sourceLocation.exclusionPatterns
					: null; // ignore exclusionPatterns if output folder == another source folder... not this one
			sourceLocation.binaryFolder.accept(
				new IResourceVisitor() {
					public boolean visit(IResource resource) throws CoreException {
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;
						if (resource.getType() == IResource.FILE) {
							if (JavaBuilder.CLASS_EXTENSION.equalsIgnoreCase(resource.getFileExtension()))
								resource.delete(IResource.FORCE, null);
							return false;
						}
						return true;
					}
				}
			);
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
	sourceLocation.sourceFolder.accept(
		new IResourceVisitor() {
			public boolean visit(IResource resource) throws CoreException {
				switch(resource.getType()) {
					case IResource.FILE :
						String extension = resource.getFileExtension();
						if (JavaBuilder.JAVA_EXTENSION.equalsIgnoreCase(extension)) return false;
						if (JavaBuilder.CLASS_EXTENSION.equalsIgnoreCase(extension)) return false;
						if (javaBuilder.filterExtraResource(resource)) return false;
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;

						IPath partialPath = resource.getFullPath().removeFirstSegments(segmentCount);
						IResource copiedResource = outputFolder.getFile(partialPath);
						if (copiedResource.exists()) {
							if (deletedAll) {
								createErrorFor(resource, Util.bind("build.duplicateResource")); //$NON-NLS-1$
								return false;
							}
							copiedResource.delete(IResource.FORCE, null); // last one wins
						}
						resource.copy(copiedResource.getFullPath(), IResource.FORCE, null);
						copiedResource.setDerived(true);
						return false;
					case IResource.FOLDER :
						if (resource.equals(outputFolder)) return false;
						if (javaBuilder.filterExtraResource(resource)) return false;
						if (exclusionPatterns != null && Util.isExcluded(resource, exclusionPatterns))
							return false;

						createFolder(resource.getFullPath().removeFirstSegments(segmentCount), outputFolder);
				}
				return true;
			}
		}
	);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5273.java