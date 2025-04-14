error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5772.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5772.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5772.java
text:
```scala
n@@otifier.updateProblemCounts(problems);

package org.eclipse.jdt.internal.core.newbuilder;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.IProblem;
import org.eclipse.jdt.internal.core.Util;

import java.util.*;

public class BatchImageBuilder extends AbstractImageBuilder {

protected BatchImageBuilder(JavaBuilder javaBuilder) {
	super(javaBuilder);
}

public void build() {
	if (JavaBuilder.DEBUG)
		System.out.println("FULL build"); //$NON-NLS-1$

	try {
		notifier.subTask(Util.bind("build.scrubbingOutput")); //$NON-NLS-1$
		scrubOutputFolder();
		notifier.updateProgressDelta(0.05f);
		removeProblemsFor(javaBuilder.currentProject);
		notifier.updateProgressDelta(0.05f);
		notifier.checkCancel();

		notifier.subTask(Util.bind("build.analyzingSources")); //$NON-NLS-1$
		ArrayList locations = new ArrayList(33);
		ArrayList typeNames = new ArrayList(33);
		addAllSourceFiles(locations, typeNames);
		notifier.updateProgressDelta(0.15f);
		notifier.checkCancel();

		if (locations.size() > 0) {
			String[] allSourceFiles = new String[locations.size()];
			locations.toArray(allSourceFiles);
			String[] initialTypeNames = new String[typeNames.size()];
			typeNames.toArray(initialTypeNames);

			notifier.setProgressPerCompilationUnit(0.75f / allSourceFiles.length);
			workQueue.addAll(allSourceFiles);
			compile(allSourceFiles, initialTypeNames);
		}
	} catch (CoreException e) {
		throw internalException(e);
	} finally {
		cleanUp();
	}
}

protected void addAllSourceFiles(final ArrayList locations, final ArrayList typeNames) throws CoreException {
	for (int i = 0, length = sourceFolders.length; i < length; i++) {
		final int count = sourceFolders[i].getFullPath().segmentCount();
		sourceFolders[i].accept(
			new IResourceVisitor() {
				public boolean visit(IResource resource) {
					if (resource.getType() == IResource.FILE) {
						if (JavaBuilder.JAVA_EXTENSION.equalsIgnoreCase(resource.getFileExtension())) {
							locations.add(resource.getLocation().toString());
							typeNames.add(
								resource.getFullPath().removeFirstSegments(count).removeFileExtension().toString());
						}
						return false;
					}
					return true;
				}
			}
		);
	}
}

protected void scrubOutputFolder() throws CoreException {
	if (hasSeparateOutputFolder) {
		// outputPath is not on the class path so wipe it clean then copy extra resources back
		IResource[] members = outputFolder.members(); 
		for (int i = 0, length = members.length; i < length; i++)
				members[i].delete(true, null);
		copyExtraResourcesBack();
	} else {
		// outputPath == the source folder so just remove class files
		outputFolder.accept(
			new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						if (JavaBuilder.CLASS_EXTENSION.equalsIgnoreCase(resource.getFileExtension()))
							resource.delete(true, null);
						return false;
					}
					return true;
				}
			}
		);
	}
}

protected void copyExtraResourcesBack() throws CoreException {
	// When, if ever, does a builder need to copy resources files (not .java
	// or .class) into the output folder?
	// If we wipe the output folder at the beginning of the build then all 'extra'
	// resources must be copied to the output folder.

	final IPath outputPath = outputFolder.getFullPath();
	for (int i = 0, length = sourceFolders.length; i < length; i++) {
		IContainer sourceFolder = sourceFolders[i];
		final IPath sourcePath = sourceFolder.getFullPath();
		final int segmentCount = sourcePath.segmentCount();
		sourceFolder.accept(
			new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					switch(resource.getType()) {
						case IResource.FILE :
							String extension = resource.getFileExtension();
							if (JavaBuilder.JAVA_EXTENSION.equalsIgnoreCase(extension)) return false;
							if (JavaBuilder.CLASS_EXTENSION.equalsIgnoreCase(extension)) return false;

							resource.copy(
								outputPath.append(
									resource.getFullPath().removeFirstSegments(segmentCount)),
								true,
								null);
							return false;
						case IResource.FOLDER :
							if (resource.getFullPath().equals(outputPath)) return false;
							if (resource.getFullPath().equals(sourcePath)) return true;

							getOutputFolder(resource.getFullPath().removeFirstSegments(segmentCount));
					}
					return true;
				}
			}
		);
	}
}

protected void updateProblemsFor(CompilationResult result) throws CoreException {
	IProblem[] problems = result.getProblems();
	if (problems == null || problems.length == 0) return;

	notifier.updateProblemCounts(null, problems);

	IPath filePath = new Path(new String(result.getFileName()));
	IResource resource = javaBuilder.workspaceRoot.getFileForLocation(filePath);
	storeProblemsFor(resource, problems);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5772.java