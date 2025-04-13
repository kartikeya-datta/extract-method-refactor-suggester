error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2605.java
text:
```scala
s@@tatus = ClasspathEntry.validateClasspathEntry(this.project, rawClasspath[i], false/*src attach*/, false /*not referred by a container*/);

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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.builder.JavaBuilder;

/*
 * Validates the raw classpath format and the resolved classpath of this project,
 * updating markers if necessary.
 */
public class ClasspathValidation {

	private JavaProject project;

	public ClasspathValidation(JavaProject project) {
		this.project = project;
	}

	public void validate() {
		JavaModelManager.PerProjectInfo perProjectInfo;
		try {
			perProjectInfo = this.project.getPerProjectInfo();
		} catch (JavaModelException e) {
			// project doesn't exist
			IProject resource = this.project.getProject();
			if (resource.isAccessible()) {
				this.project.flushClasspathProblemMarkers(true/*flush cycle markers*/, true/*flush classpath format markers*/);

				// remove problems and tasks created  by the builder
				JavaBuilder.removeProblemsAndTasksFor(resource);
			}
			return;
		}

		// use synchronized block to ensure consistency
		IClasspathEntry[] rawClasspath;
		IPath outputLocation;
		IJavaModelStatus status;
		synchronized (perProjectInfo) {
			rawClasspath = perProjectInfo.rawClasspath;
			outputLocation = perProjectInfo.outputLocation;
			status = perProjectInfo.rawClasspathStatus; // status has been set during POST_CHANGE
		}

		// update classpath format problems
		this.project.flushClasspathProblemMarkers(false/*cycle*/, true/*format*/);
		if (!status.isOK())
			this.project.createClasspathProblemMarker(status);

		// update resolved classpath problems
		this.project.flushClasspathProblemMarkers(false/*cycle*/, false/*format*/);

		if (rawClasspath != JavaProject.INVALID_CLASSPATH && outputLocation != null) {
		 	for (int i = 0; i < rawClasspath.length; i++) {
				status = ClasspathEntry.validateClasspathEntry(this.project, rawClasspath[i], false/*src attach*/, true /*recurse in container*/);
				if (!status.isOK()) {
					if (status.getCode() == IJavaModelStatusConstants.INVALID_CLASSPATH && ((ClasspathEntry) rawClasspath[i]).isOptional())
						continue; // ignore this entry
					this.project.createClasspathProblemMarker(status);
				}
			 }
			status = ClasspathEntry.validateClasspath(this.project, rawClasspath, outputLocation);
			if (!status.isOK())
				this.project.createClasspathProblemMarker(status);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2605.java