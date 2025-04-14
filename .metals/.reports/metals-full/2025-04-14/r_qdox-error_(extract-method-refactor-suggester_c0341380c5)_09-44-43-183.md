error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1021.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1021.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1021.java
text:
```scala
r@@eturn "Binary classpath directory " + binaryPath; //$NON-NLS-1$

package org.eclipse.jdt.internal.core.newbuilder;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jdt.internal.core.util.LookupTable;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

import java.io.*;
import java.util.*;

class ClasspathDirectory extends ClasspathLocation {

String binaryPath; // includes .class files for a single directory
LookupTable missingPackages;
LookupTable directoryCache;

ClasspathDirectory(String binaryPath) {
	this.binaryPath = binaryPath;
	if (!binaryPath.endsWith("/")) //$NON-NLS-1$
		this.binaryPath += "/"; //$NON-NLS-1$

	this.missingPackages = new LookupTable(11);
	this.directoryCache = new LookupTable(11);
}

void clear() {
	this.missingPackages = null;
	this.directoryCache = null;
}

String[] directoryList(String pathPrefix, char[][] compoundName, char[] packageName) {
	String partialPath = NameEnvironment.assembleName(packageName, compoundName, '/');
	if (missingPackages.containsKey(partialPath)) return null;

	String fullPath = pathPrefix + partialPath;
	String[] dirList = (String[]) directoryCache.get(fullPath);
	if (dirList != null) return dirList;

	File dir = new File(fullPath);
	if (dir != null && dir.isDirectory()) {
		boolean matchesName = packageName == null;
		if (!matchesName) {
			int index = packageName.length;
			while (--index >= 0 && !Character.isUpperCase(packageName[index])) {}
			matchesName = index < 0 || exists(pathPrefix, new String(packageName), compoundName);
		}
		if (matchesName) {
			if ((dirList = dir.list()) == null)
				dirList = new String[0];
			directoryCache.put(fullPath, dirList);
			return dirList;
		}
	}
	missingPackages.put(partialPath, partialPath); // value is not used
	return null;
}

public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof ClasspathDirectory)) return false;

	return binaryPath.equals(((ClasspathDirectory) o).binaryPath);
}

boolean exists(String pathPrefix, String filename, char[][] packageName) {
	String[] dirList = directoryList(pathPrefix, packageName, null);
	if (dirList != null)
		for (int i = dirList.length; --i >= 0;)
			if (filename.equals(dirList[i]))
				return true;
	return false;
}

NameEnvironmentAnswer findClass(char[] className, char[][] packageName) {
	String binaryFilename = new String(className) + ".class"; //$NON-NLS-1$
	if (exists(binaryPath, binaryFilename, packageName)) {
		try {
			return new NameEnvironmentAnswer(
				ClassFileReader.read(binaryPath + NameEnvironment.assembleName(binaryFilename, packageName, '/')));
		} catch (Exception e) {
		}
	}
	return null;
}

boolean isPackage(char[][] compoundName, char[] packageName) {
	return directoryList(binaryPath, compoundName, packageName) != null;
}

void reset() {
	this.missingPackages = new LookupTable(11);
	this.directoryCache = new LookupTable(11);
}

public String toString() {
	return "ClasspathDirectory " + binaryPath; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1021.java