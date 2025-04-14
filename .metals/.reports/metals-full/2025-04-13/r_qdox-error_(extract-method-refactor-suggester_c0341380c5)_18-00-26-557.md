error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6265.java
text:
```scala
C@@lasspathChange classpathChange = perProjectInfo.setRawClasspath(this.newRawClasspath, this.newOutputLocation, JavaModelStatus.VERIFIED_OK/*format is ok*/);

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

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaModelManager.PerProjectInfo;

/**
 * This operation sets an <code>IJavaProject</code>'s classpath.
 *
 * @see IJavaProject
 */
public class SetClasspathOperation extends ChangeClasspathOperation {

	IClasspathEntry[] newRawClasspath;
	IPath newOutputLocation;
	JavaProject project;
			
	/**
	 * When executed, this operation sets the raw classpath and output location of the given project.
	 */
	public SetClasspathOperation(
		JavaProject project,
		IClasspathEntry[] newRawClasspath,
		IPath newOutputLocation,
		boolean canChangeResource) {

		super(new IJavaElement[] { project }, canChangeResource);
		this.project = project;
		this.newRawClasspath = newRawClasspath;
		this.newOutputLocation = newOutputLocation;
	}

	/**
	 * Sets the classpath of the pre-specified project.
	 */
	protected void executeOperation() throws JavaModelException {
		checkCanceled();
		try {
			// set raw classpath and null out resolved info
			PerProjectInfo perProjectInfo = this.project.getPerProjectInfo();
			ClasspathChange classpathChange = perProjectInfo.setClasspath(this.newRawClasspath, this.newOutputLocation, JavaModelStatus.VERIFIED_OK/*format is ok*/, null, null, null, null);
			
			// if needed, generate delta, update project ref, create markers, ...
			classpathChanged(classpathChange);
			
			// write .classpath file
			if (this.canChangeResources && perProjectInfo.writeAndCacheClasspath(this.project, this.newRawClasspath, this.newOutputLocation))
				setAttribute(HAS_MODIFIED_RESOURCE_ATTR, TRUE);
		} finally {		
			done();
		}
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer(20);
		buffer.append("SetClasspathOperation\n"); //$NON-NLS-1$
		buffer.append(" - classpath : "); //$NON-NLS-1$
		buffer.append("{"); //$NON-NLS-1$
		for (int i = 0; i < this.newRawClasspath.length; i++) {
			if (i > 0) buffer.append(","); //$NON-NLS-1$
			IClasspathEntry element = this.newRawClasspath[i];
			buffer.append(" ").append(element.toString()); //$NON-NLS-1$
		}
		buffer.append("\n - output location : ");  //$NON-NLS-1$
		buffer.append(this.newOutputLocation.toString());
		return buffer.toString();
	}

	public IJavaModelStatus verify() {
		IJavaModelStatus status = super.verify();
		if (!status.isOK())
			return status;
		return ClasspathEntry.validateClasspath(	this.project, this.newRawClasspath, this.newOutputLocation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6265.java