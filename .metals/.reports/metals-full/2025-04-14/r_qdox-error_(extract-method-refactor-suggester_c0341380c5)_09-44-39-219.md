error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2288.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2288.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2288.java
text:
```scala
i@@nt result = change.generateDelta(delta, true/*add classpath change*/);

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;

/*
 * Abstract class for operations that change the classpath
 */
public abstract class ChangeClasspathOperation extends JavaModelOperation {

	protected boolean canChangeResources;

	public ChangeClasspathOperation(IJavaElement[] elements, boolean canChangeResources) {
		super(elements);
		this.canChangeResources = canChangeResources;
	}

	protected boolean canModifyRoots() {
		// changing the classpath can modify roots
		return true;
	}

	/*
	 * The resolved classpath of the given project may have changed:
	 * - generate a delta
	 * - trigger indexing
	 * - update project references
	 * - create resolved classpath markers
	 */
	protected void classpathChanged(ClasspathChange change) throws JavaModelException {
		// reset the project's caches early since some clients rely on the project's caches being up-to-date when run inside an IWorkspaceRunnable
		// (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=212769#c5 )
		JavaProject project = change.project;
		project.resetCaches();

		if (this.canChangeResources) {
			// workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=177922
			if (isTopLevelOperation() && !ResourcesPlugin.getWorkspace().isTreeLocked()) {
				new ClasspathValidation(project).validate();
			}

			// delta, indexing and classpath markers are going to be created by the delta processor
			// while handling the resource change (either .classpath change, or project touched)

			// however ensure project references are updated
			// since some clients rely on the project references when run inside an IWorkspaceRunnable
			new ProjectReferenceChange(project, change.oldResolvedClasspath).updateProjectReferencesIfNecessary();

			// and ensure that external folders are updated as well
			new ExternalFolderChange(project, change.oldResolvedClasspath).updateExternalFoldersIfNecessary(true/*refresh if external linked folder already exists*/, null);

		} else {
			DeltaProcessingState state = JavaModelManager.getDeltaState();
			JavaElementDelta delta = new JavaElementDelta(getJavaModel());
			int result = change.generateDelta(delta);
			if ((result & ClasspathChange.HAS_DELTA) != 0) {
				// create delta
				addDelta(delta);

				// need to recompute root infos
				state.rootsAreStale = true;

				// ensure indexes are updated
				change.requestIndexing();

				// ensure classpath is validated on next build
				state.addClasspathValidation(project);
			}
			if ((result & ClasspathChange.HAS_PROJECT_CHANGE) != 0) {
				// ensure project references are updated on next build
				state.addProjectReferenceChange(project, change.oldResolvedClasspath);
			}
			if ((result & ClasspathChange.HAS_LIBRARY_CHANGE) != 0) {
				// ensure external folders are updated on next build
				state.addExternalFolderChange(project, change.oldResolvedClasspath);
			}
		}
	}

	protected ISchedulingRule getSchedulingRule() {
		return null; // no lock taken while changing classpath
	}

	public boolean isReadOnly() {
		return !this.canChangeResources;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2288.java