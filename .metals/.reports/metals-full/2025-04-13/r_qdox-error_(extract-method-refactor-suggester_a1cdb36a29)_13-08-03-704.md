error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4510.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4510.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4510.java
text:
```scala
g@@etPerProjectInfo().setRawClasspath(rawClasspath, defaultOutputLocation(), JavaModelStatus.VERIFIED_OK/*no .classpath format problem*/);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ExternalJavaProject extends JavaProject {
	
	/*
	 * Note this name can be surfaced in the UI (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=128258)
	 */
	public static final String EXTERNAL_PROJECT_NAME = " "; //$NON-NLS-1$

	public ExternalJavaProject(IClasspathEntry[] rawClasspath) {
		super(ResourcesPlugin.getWorkspace().getRoot().getProject(EXTERNAL_PROJECT_NAME), JavaModelManager.getJavaModelManager().getJavaModel());
		try {
			getPerProjectInfo().setClasspath(rawClasspath, defaultOutputLocation(), JavaModelStatus.VERIFIED_OK/*no .classpath format problem*/, null/*no resolved claspath*/, null/*no reverse map*/, null/*no resolve entry map*/, null/*no resolved status*/);
		} catch (JavaModelException e) {
			// getPerProjectInfo() never throws JavaModelException for an ExternalJavaProject
		}
	}
	
	public boolean equals(Object o) {
		return this == o;
	}

	public boolean exists() {
		// external project never exists
		return false;
	}
	
	public String getOption(String optionName, boolean inheritJavaCoreOptions) {
		if (JavaCore.COMPILER_PB_FORBIDDEN_REFERENCE.equals(optionName)
 JavaCore.COMPILER_PB_DISCOURAGED_REFERENCE.equals(optionName))
			return JavaCore.IGNORE;
		return super.getOption(optionName, inheritJavaCoreOptions);
	}
	
	public boolean isOnClasspath(IJavaElement element) {
		// since project is external, no element is on classpath (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=61013#c16)
		return false;
	}

	public boolean isOnClasspath(IResource resource) {
		// since project is external, no resource is on classpath (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=61013#c16)
		return false;
	}

	protected IStatus validateExistence(IResource underlyingResource) {
		// allow opening of external project
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4510.java