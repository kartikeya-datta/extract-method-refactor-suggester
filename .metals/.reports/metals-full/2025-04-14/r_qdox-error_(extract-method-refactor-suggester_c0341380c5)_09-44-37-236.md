error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9746.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9746.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9746.java
text:
```scala
public I@@File resource;

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.eclipse.jdt.internal.core.util.Util;

public class SourceFile implements ICompilationUnit {

IFile resource;
ClasspathMultiDirectory sourceLocation;
String initialTypeName;
boolean updateClassFile;

public SourceFile(IFile resource, ClasspathMultiDirectory sourceLocation) {
	this.resource = resource;
	this.sourceLocation = sourceLocation;
	this.initialTypeName = extractTypeName();
	this.updateClassFile = false;
}

public SourceFile(IFile resource, ClasspathMultiDirectory sourceLocation, boolean updateClassFile) {
	this(resource, sourceLocation);

	this.updateClassFile = updateClassFile;
}

public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof SourceFile)) return false;

	SourceFile f = (SourceFile) o;
	return this.sourceLocation == f.sourceLocation && this.resource.getFullPath().equals(f.resource.getFullPath());
} 

String extractTypeName() {
	// answer a String with the qualified type name for the source file in the form: 'p1/p2/A'
	IPath fullPath = this.resource.getFullPath();
	int resourceSegmentCount = fullPath.segmentCount();
	int sourceFolderSegmentCount = this.sourceLocation.sourceFolder.getFullPath().segmentCount();
	int charCount = (resourceSegmentCount - sourceFolderSegmentCount - 1);
	resourceSegmentCount--; // deal with the last segment separately
	for (int i = sourceFolderSegmentCount; i < resourceSegmentCount; i++)
		charCount += fullPath.segment(i).length();
	String lastSegment = fullPath.segment(resourceSegmentCount);
	int extensionIndex = Util.indexOfJavaLikeExtension(lastSegment);
	charCount += extensionIndex;

	char[] result = new char[charCount];
	int offset = 0;
	for (int i = sourceFolderSegmentCount; i < resourceSegmentCount; i++) {
		String segment = fullPath.segment(i);
		int size = segment.length();
		segment.getChars(0, size, result, offset);
		offset += size;
		result[offset++] = '/';
	}
	lastSegment.getChars(0, extensionIndex, result, offset);
	return new String(result);
}

public char[] getContents() {

	try {	
		return Util.getResourceContentsAsCharArray(this.resource);
	} catch (CoreException e) {
		throw new AbortCompilation(true, new MissingSourceFileException(this.resource.getFullPath().toString()));
	}
}

/**
 * @see org.eclipse.jdt.internal.compiler.env.IDependent#getFileName()
 */
public char[] getFileName() {
	return this.resource.getFullPath().toString().toCharArray(); // do not know what you want to return here
}

public char[] getMainTypeName() {
	char[] typeName = this.initialTypeName.toCharArray();
	int lastIndex = CharOperation.lastIndexOf('/', typeName);
	return CharOperation.subarray(typeName, lastIndex + 1, -1);
}

public char[][] getPackageName() {
	char[] typeName = this.initialTypeName.toCharArray();
	int lastIndex = CharOperation.lastIndexOf('/', typeName);
	return CharOperation.splitOn('/', typeName, 0, lastIndex);
}
public int hashCode() {
	return this.initialTypeName.hashCode();
}
String typeLocator() {
	return this.resource.getProjectRelativePath().toString();
}

public String toString() {
	return "SourceFile[" //$NON-NLS-1$
		+ this.resource.getFullPath() + "]";  //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9746.java