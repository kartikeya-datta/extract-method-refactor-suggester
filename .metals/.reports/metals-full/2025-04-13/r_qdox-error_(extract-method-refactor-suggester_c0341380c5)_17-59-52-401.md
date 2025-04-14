error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5946.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5946.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5946.java
text:
```scala
r@@eturn "Classpath for jar file " + zipFile; //$NON-NLS-1$

package org.eclipse.jdt.internal.compiler.batch;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.classfmt.*;

class ClasspathJar implements FileSystem.Classpath {
	ZipFile zipFile;
	Hashtable directoryCache;	
ClasspathJar(File file) throws IOException {
	zipFile = new ZipFile(file);
	buildDirectoryStructure();
}
void buildDirectoryStructure() {
	directoryCache = new Hashtable(101);
	for (Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
		String fileName = ((ZipEntry) e.nextElement()).getName();

		// extract the package name
		int last = fileName.lastIndexOf('/');
		if (last > 0 && directoryCache.get(fileName.substring(0, last)) == null) {
			// add the package name & all of its parent packages
			for (int i = 0; i <= last; i++) {
				i = fileName.indexOf('/', i);
				String packageName = fileName.substring(0, i);
				if (directoryCache.get(packageName) == null)
					directoryCache.put(packageName, packageName);
			}
		}
	}
}
public boolean exists(String filename, char[][] packageName) {
	return zipFile.getEntry(FileSystem.assembleName(filename, packageName, '/')) != null;
}
public boolean isPackage(char[][] compoundName, char[] packageName) {
	return 
		directoryCache.get(
			FileSystem.assembleName(packageName, compoundName, '/'))
				!= null;
}
public long lastModified(String filename, char[][] packageName) {
	ZipEntry entry = zipFile.getEntry(FileSystem.assembleName(filename, packageName, '/'));
	if (entry == null)
		return -1L;
	else
		return entry.getTime();
}
public NameEnvironmentAnswer readClassFile(String filename, char[][] packageName) {
	try {
		return new NameEnvironmentAnswer(
			ClassFileReader.read(zipFile, FileSystem.assembleName(filename, packageName, '/')));
	} catch (Exception e) {
		return null; // treat as if class file is missing
	}
}
public NameEnvironmentAnswer readJavaFile(String filename, char[][] packageName) {
	try {
		String fullName = FileSystem.assembleName(filename, packageName, '/');
		ZipEntry entry = zipFile.getEntry(fullName);
		InputStreamReader reader = new InputStreamReader(zipFile.getInputStream(entry));
		int length;
		char[] contents = new char[length = (int) entry.getSize()];
		int len = 0;
		int readSize = 0;
		while ((readSize != -1) && (len != length)) {
			readSize = reader.read(contents, len, length - len);
			len += readSize;
		}
		reader.close();
		return new NameEnvironmentAnswer(new CompilationUnit(contents, fullName));
	} catch (Exception e) {
		return null; // treat as if source file is missing
	}
}
public String toString() {
	return "Classpath for jar file "/*nonNLS*/ + zipFile;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5946.java