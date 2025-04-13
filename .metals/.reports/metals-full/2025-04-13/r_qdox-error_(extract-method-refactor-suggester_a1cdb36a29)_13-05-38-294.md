error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1258.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1258.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1258.java
text:
```scala
i@@f (end == -1 || !Util.isClassFileName(this.fileName)) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.util.Util;

/**
 * A basic implementation of <code>ICompilationUnit</code>
 * for use in the <code>SourceMapper</code>.
 * @see ICompilationUnit
 */
public class BasicCompilationUnit implements ICompilationUnit {
	protected char[] contents;
	
	// Note that if this compiler ICompilationUnit's content is known in advance, the fileName is not used to retrieve this content.
	// Instead it is used to keep enough information to recreate the IJavaElement corresponding to this compiler ICompilationUnit.
	// Thus the fileName can be a path to a .class file, or even a path in a .jar to a .class file.
	// (e.g. /P/lib/mylib.jar|org/eclipse/test/X.class)
	protected char[] fileName; 
	
	protected char[][] packageName;
	protected char[] mainTypeName;
	protected String encoding;

public BasicCompilationUnit(char[] contents, char[][] packageName, String fileName) {
	this.contents = contents;
	this.fileName = fileName.toCharArray();
	this.packageName = packageName;
}

public BasicCompilationUnit(char[] contents, char[][] packageName, String fileName, String encoding) {
	this(contents, packageName, fileName);
	this.encoding = encoding;
}

public BasicCompilationUnit(char[] contents, char[][] packageName, String fileName, IJavaElement javaElement) {
	this(contents, packageName, fileName);
	initEncoding(javaElement);
}

/*
 * Initialize compilation unit encoding.
 * If we have a project, then get file name corresponding IFile and retrieve its encoding using
 * new API for encoding.
 * In case of a class file, then go through project in order to let the possibility to retrieve
 * a corresponding source file resource.
 * If we have a compilation unit, then get encoding from its resource directly...
 */
private void initEncoding(IJavaElement javaElement) {
	if (javaElement != null) {
		try {
			IJavaProject javaProject = javaElement.getJavaProject();
			switch (javaElement.getElementType()) {
				case IJavaElement.COMPILATION_UNIT:
					IFile file = (IFile) javaElement.getResource();
					if (file != null) {
						this.encoding = file.getCharset();
						break;
					}
					// if no file, then get project encoding
				default:
					IProject project = (IProject) javaProject.getResource();
					if (project != null) {
						this.encoding = project.getDefaultCharset();
					}
					break;
			}
		} catch (CoreException e1) {
			this.encoding = null;
		}
	} else  {
		this.encoding = null;
	}
}

public char[] getContents() {
	if (this.contents != null)
		return this.contents;   // answer the cached source

	// otherwise retrieve it
	try {
		return Util.getFileCharContent(new File(new String(this.fileName)), this.encoding);
	} catch (IOException e) {
		// could not read file: returns an empty array
	}
	return CharOperation.NO_CHAR;
}
/**
 * @see org.eclipse.jdt.internal.compiler.env.IDependent#getFileName()
 */
public char[] getFileName() {
	return this.fileName;
}
public char[] getMainTypeName() {
	if (this.mainTypeName == null) {
		int start = CharOperation.lastIndexOf('/', this.fileName) + 1;
		if (start == 0 || start < CharOperation.lastIndexOf('\\', this.fileName))
			start = CharOperation.lastIndexOf('\\', this.fileName) + 1;
		int separator = CharOperation.indexOf('|', this.fileName) + 1;
		if (separator > start) // case of a .class file in a default package in a jar
			start = separator;
	
		int end = CharOperation.lastIndexOf('$', this.fileName);
		if (end == -1) {
			end = CharOperation.lastIndexOf('.', this.fileName);
			if (end == -1)
				end = this.fileName.length;
		}
	
		this.mainTypeName = CharOperation.subarray(this.fileName, start, end);
	}
	return this.mainTypeName;
}
public char[][] getPackageName() {
	return this.packageName;
}
public String toString(){
	return "CompilationUnit: "+new String(this.fileName); //$NON-NLS-1$
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1258.java