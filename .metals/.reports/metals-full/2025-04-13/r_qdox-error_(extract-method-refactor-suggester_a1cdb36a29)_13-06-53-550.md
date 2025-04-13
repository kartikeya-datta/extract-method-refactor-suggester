error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8665.java
text:
```scala
w@@riteShortAt(int i, int pos) {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS.utils;

import java.io.*;
import java.math.*;

/**
 * An extension of ByteArrayOutputStream to support directly writing types
 * used by DNS routines.
 * @see DataByteInputStream
 *
 * @author Brian Wellington
 */


public class DataByteOutputStream extends ByteArrayOutputStream {

/**
 * Create a new DataByteOutputStream with a specified initial size
 * @param size The initial size
 */
public
DataByteOutputStream(int size) {
	super(size);
}

/**
 * Create a new DataByteOutputStream with the default initial size
 * @param size The initial size
 */
public
DataByteOutputStream() {
	super();
}

/**
 * Writes a byte to the stream
 * @param i The byte to be written
 */
public void
writeByte(int i) {
	write(i);
}

/**
 * Writes a short to the stream
 * @param i The short to be written
 */
public void
writeShort(int i) {
	write((i >>> 8) & 0xFF);
	write(i & 0xFF);
}

/**
 * Writes an int to the stream
 * @param i The int to be written
 */
public void
writeInt(int i) {
	write((i >>> 24) & 0xFF);
	write((i >>> 16) & 0xFF);
	write((i >>> 8) & 0xFF);
	write(i & 0xFF);
}

/**
 * Writes a long to the stream
 * @param l The long to be written
 */
public void
writeLong(long l) {
	write((int)(l >>> 56) & 0xFF);
	write((int)(l >>> 48) & 0xFF);
	write((int)(l >>> 40) & 0xFF);
	write((int)(l >>> 32) & 0xFF);
	write((int)(l >>> 24) & 0xFF);
	write((int)(l >>> 16) & 0xFF);
	write((int)(l >>> 8) & 0xFF);
	write((int)l & 0xFF);
}

/**
 * Writes a String to the stream, encoded as a length byte followed by data
 * @param s The String to be written
 */
public void
writeString(String s) {
	writeByte(s.length());
	writeArray(s.getBytes());
}

/**
 * Writes a string represented by a byte array to the stream, encoded as a
 * length byte followed by data
 * @param s The byte array containing the string to be written
 * @param start The start of the string withing the byte array.
 */
public void
writeString(byte [] s, int start) {
	write(s, start, s[start] + 1);
}

/**
 * Writes a full byte array to the stream.
 * @param b The byte array to be written.
 */
public void
writeArray(byte [] b, boolean writeLength) {
	if (writeLength)
		writeByte(b.length);
	write(b, 0, b.length);
}

/**
 * Writes a full byte array to the stream.
 * @param b The byte array to be written.
 */
public void
writeArray(byte [] b) {
	write(b, 0, b.length);
}

/**
 * Writes a BigInteger to the stream, encoded as binary data.  If present,
 * the leading 0 byte is removed.
 * @param i The BigInteger to be written
 */
public void
writeBigInteger(BigInteger i) {
	byte [] b = i.toByteArray();
	if (b[0] == 0)
		write(b, 1, b.length - 1);
	else
		write(b, 0, b.length);
}

/**
 * Writes a short to the stream at a specific location
 * @param i The short to be written
 * @param pos The position at which the write occurs
 */
public void
writeShortAt(int i, int pos) throws IllegalArgumentException {
	if (pos < 0 || pos > count)
		throw new IllegalArgumentException(pos + " out of range");
	int oldcount = count;
	count = pos;
	writeShort(i);
	count = oldcount;
}

/**
 * Set the current position in the stream
 * @param pos The current position
 */
public void
setPos(int pos) {
	count = pos;
}

/**
 * Get the current position in the stream
 * @return The current position
 */
public int
getPos() {
	return count;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8665.java