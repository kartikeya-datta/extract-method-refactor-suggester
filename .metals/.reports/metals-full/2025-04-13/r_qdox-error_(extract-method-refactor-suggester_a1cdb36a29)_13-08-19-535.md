error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6081.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6081.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6081.java
text:
```scala
t@@hrow new IllegalArgumentException("Directory arguments should be a directory."/*nonNLS*/);

package org.eclipse.jdt.internal.core.util;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.io.*;
import java.net.*;


/** An anonymous file source creates files in the given directory.
 */
public class AnonymousFileSource {
	File fDirectory;
/**
 * Creates an anonymous file source which creates files in the given directory.
 */
public AnonymousFileSource(File directory) {
	if (!directory.exists()) {
		directory.mkdirs();
	} else if (!directory.isDirectory()) {
		throw new IllegalArgumentException("Directory arguments should be a directory.");
	}
	fDirectory = directory;	
}
/**
 * Allocates and returns a RandomAccessFile in R/W mode on a new anonymous file.
 * Guaranteed to be unallocated.
 */
synchronized public RandomAccessFile allocateAnonymousFile() throws IOException {
	
	File file = getAnonymousFile();
	return new RandomAccessFile(file, "rw"/*nonNLS*/);
}
/**
 * Returns a URL on a newly allocated file with the given initial content.
 * Guaranteed to be unallocated.
 */
synchronized public URL allocateAnonymousURL(byte[] bytes) throws IOException {
	try {
		byte hasharray[] = java.security.MessageDigest.getInstance("SHA"/*nonNLS*/).digest(bytes);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hasharray.length; i++) {
			sb.append(Character.forDigit((int)((hasharray[i] >> 4) & 0x0F), 16));
			sb.append(Character.forDigit((int)(hasharray[i] & 0x0F), 16));
		}
		sb.append(".jnk"/*nonNLS*/);
		String fileName = sb.toString();
		File file = fileForName(fileName);
		if (!file.exists()) {
			RandomAccessFile raf = new RandomAccessFile(file, "rw"/*nonNLS*/);
			raf.write(bytes);
			raf.close();
		}
		return convertFileToURL(file);
	} 
	catch (java.security.NoSuchAlgorithmException e) {
		throw new IOException(e.getMessage());
	}
}
/**
 * Returns a URL using the "file" protocol corresponding to the given File.
 */
static public URL convertFileToURL(File file) {
	try {
		String path = file.getCanonicalPath().replace(java.io.File.separatorChar, '/');
		return new URL("file"/*nonNLS*/, ""/*nonNLS*/, "/"/*nonNLS*/ + path);
	}
	catch (IOException ioe) {
		throw new Error();
	}
}
/**
 * Answer a File to use for the given simple file name.
 */
File fileForName(String fileName) {
	File dir;
	if (fileName.length() >= 1) {
		String dirName = Integer.toHexString((fileName.hashCode() % 255) & 255);
		dir = new File(fDirectory, dirName);
		dir.mkdirs();
	} else {
		dir = fDirectory;
	}
	return new File(dir, fileName);	
}
/**
 * Returns a new anonymous file, but does not allocate it.  
 * Not guaranteed to be free when used since it is unallocated.
 */
synchronized public File getAnonymousFile() {
	File file;
	file = fileForName(getAnonymousFileName());
	while (file.exists()) {
		try {
			Thread.sleep(1);
		} 
		catch (InterruptedException e) {
		}
		file = fileForName(getAnonymousFileName());
	}
	return file;
}
/**
 * Returns a new anonymous file name.  
 * Not guaranteed to be free since its directory is unknown.
 */
synchronized public String getAnonymousFileName() {
	return getAnonymousFileName(System.currentTimeMillis());
}
/**
 * Returns a new anonymous file name based on the given long.  
 * Not guaranteed to be free since its directory is unknown.
 */
synchronized public String getAnonymousFileName(long l) {
	if (l < 0) l = -l;
	StringBuffer sb = new StringBuffer();
	sb.append(Character.forDigit((int)(l % 26 + 10), 36));
	l /= 26;
	while (l != 0) {
		sb.append(Character.forDigit((int)(l % 36), 36));
		l /= 36;
	}
	sb.append(".jnk"/*nonNLS*/);
	return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6081.java