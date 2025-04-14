error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4964.java
text:
```scala
r@@eturn zipFilename.equals(((ClasspathJar) o).zipFilename);

package org.eclipse.jdt.internal.core.newbuilder;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

import java.io.*;
import java.util.*;
import java.util.zip.*;

class ClasspathJar extends ClasspathLocation {

String zipFilename; // keep for equals
ZipFile zipFile;
SimpleLookupTable directoryCache;	

ClasspathJar(String zipFilename) {
	this.zipFilename = zipFilename;
	this.zipFile = null;
	this.directoryCache = null;
}

void buildDirectoryStructure() {
	this.directoryCache = new SimpleLookupTable(101);

	try {
		this.zipFile = new ZipFile(zipFilename);
	} catch(IOException e) {
		return;
	}
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

void cleanup() {
	if (zipFile != null) {
		try { zipFile.close(); } catch(IOException e) {}
	}
	this.zipFile = null;
	this.directoryCache = null;
}

public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof ClasspathJar)) return false;

	return zipFilename.equalsIgnoreCase(((ClasspathJar) o).zipFilename);
}

NameEnvironmentAnswer findClass(char[] className, char[][] packageName) {
	if (directoryCache == null) buildDirectoryStructure();
	try {
		String binaryFilename =
			NameEnvironment.assembleName(new String(className) + ".class", packageName, '/'); //$NON-NLS-1$
		if (zipFile.getEntry(binaryFilename) == null) return null;

		return new NameEnvironmentAnswer(ClassFileReader.read(zipFile, binaryFilename));
	} catch (Exception e) {
		return null; // treat as if class file is missing
	}
}

boolean isPackage(char[][] compoundName, char[] packageName) {
	if (directoryCache == null) buildDirectoryStructure();
	return
		directoryCache.get(
			NameEnvironment.assembleName(packageName, compoundName, '/'))
				!= null;
}

public String toString() {
	return "Classpath jar file " + zipFilename; //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4964.java