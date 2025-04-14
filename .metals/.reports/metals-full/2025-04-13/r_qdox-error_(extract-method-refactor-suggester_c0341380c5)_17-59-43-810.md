error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/752.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/752.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/752.java
text:
```scala
i@@f (!doesFileExist(qualifiedPackageName, ""))  //$NON-NLS-1$

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
Hashtable directoryCache;
String[] missingPackageHolder = new String[1];
String encoding;

ClasspathDirectory(File directory, String encoding) {
	this.path = directory.getAbsolutePath();
	if (!path.endsWith(File.separator))
		this.path += File.separator;
	this.directoryCache = new Hashtable(11);
	this.encoding = encoding;
}
String[] directoryList(String qualifiedPackageName) {
	String[] dirList = (String[]) directoryCache.get(qualifiedPackageName);
	if (dirList == missingPackageHolder) return null; // package exists in another classpath directory or jar
	if (dirList != null) return dirList;

	File dir = new File(path + qualifiedPackageName);
	notFound : if (dir != null && dir.isDirectory()) {
		// must protect against a case insensitive File call
		// walk the qualifiedPackageName backwards looking for an uppercase character before the '/'
		int index = qualifiedPackageName.length();
		int last = qualifiedPackageName.lastIndexOf(File.separatorChar);
		while (--index > last && !Character.isUpperCase(qualifiedPackageName.charAt(index))) {}
		if (index > last) {
			if (last == -1) {
				if (!doesFileExist(qualifiedPackageName, ""))
					break notFound;
			} else {
				String packageName = qualifiedPackageName.substring(last + 1);
				String parentPackage = qualifiedPackageName.substring(0, last);
				if (!doesFileExist(packageName, parentPackage))
					break notFound;
			}
		}
		if ((dirList = dir.list()) == null)
			dirList = new String[0];
		directoryCache.put(qualifiedPackageName, dirList);
		return dirList;
	}
	directoryCache.put(qualifiedPackageName, missingPackageHolder);
	return null;
}
boolean doesFileExist(String fileName, String qualifiedPackageName) {
	String[] dirList = directoryList(qualifiedPackageName);
	if (dirList == null) return false; // most common case

	for (int i = dirList.length; --i >= 0;)
		if (fileName.equals(dirList[i]))
			return true;
	return false;
}
public NameEnvironmentAnswer findClass(char[] typeName, String qualifiedPackageName, String qualifiedBinaryFileName) {
	if (!isPackage(qualifiedPackageName)) return null; // most common case

	String fileName = new String(typeName);
	boolean binaryExists = doesFileExist(fileName + ".class", qualifiedPackageName); //$NON-NLS-1$
	boolean sourceExists = doesFileExist(fileName + ".java", qualifiedPackageName); //$NON-NLS-1$
	if (sourceExists) {
		String fullSourcePath = path + qualifiedBinaryFileName.substring(0, qualifiedBinaryFileName.length() - 6)  + ".java"; //$NON-NLS-1$
		if (!binaryExists)
			return new NameEnvironmentAnswer(new CompilationUnit(null, fullSourcePath, this.encoding));

		String fullBinaryPath = path + qualifiedBinaryFileName;
		long binaryModified = new File(fullBinaryPath).lastModified();
		long sourceModified = new File(fullSourcePath).lastModified();
		if (sourceModified > binaryModified)
			return new NameEnvironmentAnswer(new CompilationUnit(null, fullSourcePath, this.encoding));
	}
	if (binaryExists) {
		try {
			ClassFileReader reader = ClassFileReader.read(path + qualifiedBinaryFileName);
			if (reader != null) return new NameEnvironmentAnswer(reader);
		} catch (Exception e) {} // treat as if file is missing
	}
	return null;
}
public boolean isPackage(String qualifiedPackageName) {
	return directoryList(qualifiedPackageName) != null;
}
public void reset() {
	this.directoryCache = new Hashtable(11);
}
public String toString() {
	return "ClasspathDirectory " + path; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/752.java