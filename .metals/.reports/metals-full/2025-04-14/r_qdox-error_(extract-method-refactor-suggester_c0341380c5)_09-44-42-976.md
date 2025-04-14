error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3405.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3405.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3405.java
text:
```scala
r@@eturn "ClasspathDirectory "/*nonNLS*/ + path;

package org.eclipse.jdt.internal.compiler.batch;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.*;
import java.util.*;

import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.classfmt.*;

class ClasspathDirectory implements FileSystem.Classpath {
	String path;
	Hashtable missingPackages;
	Hashtable directoryCache;
ClasspathDirectory(File directory) {
	this.path = directory.getAbsolutePath();
	if (!path.endsWith(File.separator))
		this.path += File.separator;
	this.missingPackages = new Hashtable(11);
	this.directoryCache = new Hashtable(11);
}
private String[] directoryList(char[][] compoundName, char[] packageName) {
	String partialPath = FileSystem.assembleName(packageName, compoundName, File.separatorChar);
	String[] dirList = (String[])directoryCache.get(partialPath);
	if (dirList != null)
		return dirList;
	if (missingPackages.containsKey(partialPath))
		return null;

	File dir = new File(path + partialPath);
	if (dir != null && dir.isDirectory()) {
		boolean matchesName = packageName == null;
		if (!matchesName) {
			int index = packageName.length;
			while (--index >= 0 && !Character.isUpperCase(packageName[index])) {}
			matchesName = index < 0 || exists(new String(packageName), compoundName); // verify that the case sensitive packageName really does exist
		}
		if (matchesName) {
			if ((dirList = dir.list()) == null)
				dirList = new String[0];
			directoryCache.put(partialPath, dirList);
			return dirList;
		}
	}
	missingPackages.put(partialPath, partialPath); // value is not used
	return null;
}
public boolean exists(String filename, char[][] packageName) {
	String[] dirList = directoryList(packageName, null);
	if (dirList != null)
		for (int i = dirList.length; --i >= 0;)
			if (filename.equals(dirList[i]))
				return true;
	return false;
}
public boolean isPackage(char[][] compoundName, char[] packageName) {
	return directoryList(compoundName, packageName) != null;
}
public long lastModified(String filename, char[][] packageName) {
	File file = new File(path + FileSystem.assembleName(filename, packageName, File.separatorChar));
	return file.lastModified();
}
public NameEnvironmentAnswer readClassFile(String filename, char[][] packageName) {
	try {
		return new NameEnvironmentAnswer(
			ClassFileReader.read(path + FileSystem.assembleName(filename, packageName, File.separatorChar)));
	} catch (Exception e) {
		return null; // treat as if class file is missing
	}
}
public NameEnvironmentAnswer readJavaFile(String fileName, char[][] packageName) {
	String fullName = path + FileSystem.assembleName(fileName, packageName, File.separatorChar);
	return new NameEnvironmentAnswer(new CompilationUnit(null, fullName));
}
public String toString() {
	return "ClasspathDirectory " + path;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3405.java