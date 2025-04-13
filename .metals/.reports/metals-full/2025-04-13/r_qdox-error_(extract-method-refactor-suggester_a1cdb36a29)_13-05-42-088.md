error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/692.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/692.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/692.java
text:
```scala
public v@@oid cleanup() {

package org.eclipse.jdt.internal.compiler.batch;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.*;

import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.util.*;

public class FileSystem implements INameEnvironment  {
	Classpath[] classpaths;
	String[] knownFileNames;

	interface Classpath {
		boolean exists(String filename, char[][] packageName);
		long lastModified(String filename, char[][] packageName);
		NameEnvironmentAnswer readClassFile(String filename, char[][] packageName);
		NameEnvironmentAnswer readJavaFile(String filename, char[][] packageName);
		boolean isPackage(char[][] compoundName, char[] packageName); 
		/**
		 * This method resets the environment. The resulting state is equivalent to
		 * a new name environment without creating a new object.
		 */
		void reset();
	}
/*
	classPathNames is a collection is Strings representing the full path of each class path
	initialFileNames is a collection is Strings, the trailing '.java' will be removed if its not already.
*/

public FileSystem(String[] classpathNames, String[] initialFileNames) {
	int classpathSize = classpathNames.length;
	classpaths = new Classpath[classpathSize];
	String[] pathNames = new String[classpathSize];
	int problemsOccured = 0;
	for (int i = 0; i < classpathSize; i++) {
		try {
			File file = new File(convertPathSeparators(classpathNames[i]));
			if (file.exists()) {
				if (file.isDirectory()) {
					classpaths[i] = new ClasspathDirectory(file);
					pathNames[i] = ((ClasspathDirectory) classpaths[i]).path;
				} else if (classpathNames[i].endsWith(".jar") | (classpathNames[i].endsWith(".zip"))) { //$NON-NLS-2$ //$NON-NLS-1$
					classpaths[i] = new ClasspathJar(file);
					pathNames[i] = classpathNames[i].substring(0, classpathNames[i].lastIndexOf('.'));
				}
			}
		} catch (IOException e) {
			classpaths[i] = null;
		}
		if (classpaths[i] == null)
			problemsOccured++;
	}
	if (problemsOccured > 0) {
		Classpath[] newPaths = new Classpath[classpathSize - problemsOccured];
		String[] newNames = new String[classpathSize - problemsOccured];
		for (int i = 0, current = 0; i < classpathSize; i++)
			if (classpaths[i] != null) {
				newPaths[current] = classpaths[i];
				newNames[current++] = pathNames[i];
			}
		classpathSize = newPaths.length;
		classpaths = newPaths;
		pathNames = newNames;
	}

	knownFileNames = new String[initialFileNames.length];
	for (int i = initialFileNames.length; --i >= 0;) {
		String fileName = initialFileNames[i];
		String matchingPathName = null;
		if (fileName.lastIndexOf(".") != -1) //$NON-NLS-1$
			fileName = fileName.substring(0, fileName.lastIndexOf('.')); // remove trailing ".java"

		fileName = convertPathSeparators(fileName);
		for (int j = 0; j < classpathSize; j++)
			if (fileName.startsWith(pathNames[j]))
				matchingPathName = pathNames[j];
		if (matchingPathName == null)
			knownFileNames[i] = fileName; // leave as is...
		else
			knownFileNames[i] = fileName.substring(matchingPathName.length());
	}
}
static String assembleName(char[] fileName, char[][] packageName, char separator) {
	return new String(CharOperation.concatWith(packageName, fileName, separator));
}
static String assembleName(String fileName, char[][] packageName, char separator) {
	return new String(
		CharOperation.concatWith(
			packageName,
			fileName == null ? null : fileName.toCharArray(),
			separator));
}
private String convertPathSeparators(String path) {
	if (File.separatorChar == '/')
		return path.replace('\\', '/');
	else
		return path.replace('/', '\\');
}
private NameEnvironmentAnswer findClass(char[] name, char[][] packageName) {
	String fullName = assembleName(name, packageName, File.separatorChar);
	for (int i = 0, length = knownFileNames.length; i < length; i++)
		if (fullName.equals(knownFileNames[i]))
			return null; // looking for a file which we know was provided at the beginning of the compilation

	String filename = new String(name);
	String binaryFilename = filename + ".class"; //$NON-NLS-1$
	String sourceFilename = filename + ".java"; //$NON-NLS-1$
	for (int i = 0, length = classpaths.length; i < length; i++) {
		Classpath classpath = classpaths[i];
		boolean binaryExists = classpath.exists(binaryFilename, packageName);
		boolean sourceExists = classpath.exists(sourceFilename, packageName);
		if (binaryExists == sourceExists) {
			if (binaryExists) { // so both are true
				long binaryModified = classpath.lastModified(binaryFilename, packageName);
				long sourceModified = classpath.lastModified(sourceFilename, packageName);
				if (binaryModified > sourceModified)
					return classpath.readClassFile(binaryFilename, packageName);
				if (sourceModified > 0)
					return classpath.readJavaFile(sourceFilename, packageName);
			}
		} else {
			if (binaryExists)
				return classpath.readClassFile(binaryFilename, packageName);
			else
				return classpath.readJavaFile(sourceFilename, packageName);
		}
	}
	return null; 
}
public NameEnvironmentAnswer findType(char[][] compoundName) {
	if (compoundName == null)
		return null;
	else
		return findClass(
			compoundName[compoundName.length - 1],
			CharOperation.subarray(compoundName, 0, compoundName.length - 1));
}
public NameEnvironmentAnswer findType(char[] name, char[][] compoundName) {
	if (name == null)
		return null;
	else
		return findClass(name, compoundName);
}
public boolean isPackage(char[][] compoundName, char[] packageName) {
	if (compoundName == null)
		compoundName = new char[0][];

	for (int i = 0, length = classpaths.length; i < length; i++)
		if (classpaths[i].isPackage(compoundName, packageName))
			return true;
	return false;
}

public void reset() {
	for (int i = 0, max = classpaths.length; i < max; i++) {
		classpaths[i].reset();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/692.java